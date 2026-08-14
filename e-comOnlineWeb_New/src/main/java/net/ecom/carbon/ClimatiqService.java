package net.ecom.carbon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class ClimatiqService {

    private static final String CLIMATIQ_URL = "https://api.climatiq.io/freight/v3/intermodal";
    private static final List<String> ALLOWED_TRANSPORT_MODES = Arrays.asList(
            "sea", "road", "rail", "inland_waterways");

    private final Gson gson = new Gson();

    public ClimatiqResponse calculate(ClimatiqRequest request, ServletContext servletContext) throws IOException {
        String apiKey = resolveApiKey(servletContext);
        if (Validation.checkNull(apiKey).isEmpty()) {
            return new ClimatiqResponse(503, errorPayload("Carbon calculator API key is not configured."));
        }

        List<String> locations = request.getLocations();
        JsonArray legs = request.getLegs();
        if (locations == null || locations.size() < 2) {
            return new ClimatiqResponse(400, errorPayload("locations must be an array with at least 2 entries."));
        }
        if (legs == null || legs.size() != locations.size() - 1) {
            return new ClimatiqResponse(400, errorPayload("legs must have exactly locations.length - 1 entries."));
        }

        int qty = Math.max(1, request.getQuantity());
        double weightKg = request.getWeightKg();
        if (weightKg <= 0) {
            return new ClimatiqResponse(400, errorPayload("Cargo weight must be greater than zero."));
        }

        String transportModeError = validateTransportModes(legs);
        if (transportModeError != null) {
            return new ClimatiqResponse(400, errorPayload(transportModeError));
        }

        JsonObject climatiqBody = buildClimatiqBody(locations, legs, weightKg);
        CarbonApiLogger.log(servletContext, "[ECOM-WAR] ClimatiqService request payload=" + climatiqBody.toString());
        JsonObject upstream = callClimatiq(apiKey, climatiqBody, servletContext);
        if (upstream.has("_httpStatus")) {
            int status = upstream.get("_httpStatus").getAsInt();
            upstream.remove("_httpStatus");
            Map<String, Object> errorBody = jsonObjectToMap(upstream);
            CarbonApiLogger.log(servletContext, "[ECOM-WAR] ClimatiqService response status=" + status + " body=" + gson.toJson(errorBody));
            return new ClimatiqResponse(status, errorBody);
        }

        Map<String, Object> enriched = enrichResponse(upstream, qty);
        CarbonApiLogger.log(servletContext, "[ECOM-WAR] ClimatiqService response status=200 body=" + gson.toJson(enriched));
        return new ClimatiqResponse(200, enriched);
    }

    private String validateTransportModes(JsonArray legs) {
        if (legs == null) {
            return null;
        }
        for (JsonElement element : legs) {
            if (!element.isJsonObject()) {
                continue;
            }
            String mode = resolveTransportMode(element.getAsJsonObject());
            if (mode == null) {
                return "Unknown transport_mode value.";
            }
        }
        return null;
    }

    private String resolveApiKey(ServletContext servletContext) {
        String contextKey = servletContext != null
                ? (String) servletContext.getAttribute(GeneralConf.CLIMATIQ_API_KEY) : null;
        String envKey = System.getenv(GeneralConf.CLIMATIQ_API_KEY);
        String propertyKey = System.getProperty(GeneralConf.CLIMATIQ_API_KEY);
        CarbonApiLogger.log(servletContext, "[ECOM-WAR] CarbonCalculator resolveApiKey generalConfContext="
                + describeKeySource(contextKey) + " env=" + describeKeySource(envKey)
                + " systemProperty=" + describeKeySource(propertyKey));

        if (!Validation.checkNull(contextKey).isEmpty()) {
            return contextKey.trim();
        }
        if (!Validation.checkNull(envKey).isEmpty()) {
            return envKey.trim();
        }
        return Validation.checkNull(propertyKey).trim();
    }

    private String describeKeySource(String key) {
        return Validation.checkNull(key).isEmpty() ? "missing" : "present";
    }

    private JsonObject buildClimatiqBody(List<String> locations, JsonArray legs, double weightKg) {
        JsonArray route = new JsonArray();
        for (int i = 0; i < locations.size(); i++) {
            JsonObject locationEntry = new JsonObject();
            locationEntry.add("location", toLocation(locations.get(i)));

            String hubMode = determineHubMode(i, legs);
            JsonObject locationOptions = new JsonObject();
            locationOptions.addProperty("logistics_hubs_type", resolveHubType(hubMode));
            locationEntry.add("location_options", locationOptions);

            route.add(locationEntry);
            if (i < legs.size()) {
                route.add(buildLeg(legs.get(i).getAsJsonObject()));
            }
        }

        JsonObject cargo = new JsonObject();
        cargo.addProperty("weight", weightKg);
        cargo.addProperty("weight_unit", "kg");

        JsonObject body = new JsonObject();
        body.add("route", route);
        body.add("cargo", cargo);
        return body;
    }

    /**
     * Resolves the appropriate logistics_hubs_type for a location based on the transport mode of the adjacent leg.
     *
     * <ul>
     *   <li>Sea and inland-waterways (barge / ITT) legs align with container-terminal handling per carrier mapping</li>
     *   <li>Road/rail legs touch inland depots (transshipment hubs)</li>
     *   <li>Default: transshipment (safe fallback for unknown modes or null)</li>
     * </ul>
     */
    private String resolveHubType(String transportMode) {
        if (transportMode == null) {
            return "transshipment";
        }
        if ("sea".equals(transportMode)) {
            return "maritime_container_terminals";
        }
        if ("inland_waterways".equals(transportMode) || "inland_waterway".equals(transportMode)) {
            return "maritime_container_terminals";
        }
        if ("rail".equals(transportMode) || "road".equals(transportMode)) {
            return "transshipment";
        }
        return "transshipment";
    }

    /**
     * Determines which transport mode "owns" this location for hub_type. At transshipment points where two modes meet,
     * sea wins because the physical port is a maritime terminal.
     */
    private String determineHubMode(int locationIndex, JsonArray legs) {
        String prevMode = null;
        String nextMode = null;

        if (locationIndex > 0 && (locationIndex - 1) < legs.size()) {
            prevMode = resolveTransportMode(legs.get(locationIndex - 1).getAsJsonObject());
        }
        if (locationIndex < legs.size()) {
            nextMode = resolveTransportMode(legs.get(locationIndex).getAsJsonObject());
        }

        if ("sea".equals(prevMode) || "sea".equals(nextMode)) {
            return "sea";
        }
        if (nextMode != null) {
            return nextMode;
        }
        if (prevMode != null) {
            return prevMode;
        }
        return null;
    }

    private JsonObject toLocation(String value) {
        String trimmed = Validation.checkNull(value).trim();
        String normalized = trimmed.toUpperCase().replaceAll("[\\s-]", "");
        JsonObject location = new JsonObject();
        if (normalized.matches("^[A-Z]{5}$")) {
            location.addProperty("locode", normalized.substring(0, 2) + "-" + normalized.substring(2));
        } else {
            location.addProperty("query", trimmed);
        }
        return location;
    }

    private JsonObject buildLeg(JsonObject leg) {
        JsonObject out = new JsonObject();
        String mode = resolveTransportMode(leg);
        out.addProperty("transport_mode", mode);

        if (!"sea".equals(mode)) {
            return out;
        }

        String vesselType = null;
        if (leg != null && leg.has("vessel_type") && !leg.get("vessel_type").isJsonNull()) {
            vesselType = Validation.checkNull(leg.get("vessel_type").getAsString()).trim().toLowerCase();
        }

        if (vesselType != null && vesselType.length() > 0) {
            JsonObject details = new JsonObject();
            details.addProperty("vessel_type", vesselType);
            out.add("leg_details", details);
        }
        return out;
    }

    private String resolveTransportMode(JsonObject leg) {
        if (leg == null || !leg.has("transport_mode") || leg.get("transport_mode").isJsonNull()) {
            return "sea";
        }
        String mode = Validation.checkNull(leg.get("transport_mode").getAsString()).trim().toLowerCase();
        if (mode.isEmpty()) {
            return "sea";
        }
        if ("inland_waterway".equals(mode)) {
            mode = "inland_waterways";
        }
        if (!ALLOWED_TRANSPORT_MODES.contains(mode)) {
            return null;
        }
        return mode;
    }

    private JsonObject callClimatiq(String apiKey, JsonObject body, ServletContext servletContext) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(CLIMATIQ_URL);
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(body.toString(), StandardCharsets.UTF_8));

        HttpResponse httpResponse = client.execute(post);
        int status = httpResponse.getStatusLine().getStatusCode();
        String responseBody = readResponse(httpResponse);
        CarbonApiLogger.log(servletContext, "[ECOM-WAR] ClimatiqService upstream status=" + status + " body=" + responseBody);
        JsonObject parsed = responseBody.isEmpty() ? new JsonObject() : new JsonParser().parse(responseBody).getAsJsonObject();

        if (status >= 400) {
            JsonObject error = new JsonObject();
            error.addProperty("_httpStatus", status);
            error.addProperty("error", parsed.has("message") ? parsed.get("message").getAsString()
                    : parsed.has("error") ? parsed.get("error").getAsString() : "Climatiq API error");
            error.add("detail", parsed);
            return error;
        }
        return parsed;
    }

    private String readResponse(HttpResponse httpResponse) throws IOException {
        if (httpResponse.getEntity() == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    private Map<String, Object> enrichResponse(JsonObject data, int qty) {
        Map<String, Object> enriched = gson.fromJson(data, Map.class);
        double co2ePerItemKg = data.has("co2e") ? data.get("co2e").getAsDouble() : 0D;
        double distanceKm = 0D;
        if (data.has("route") && data.get("route").isJsonArray()) {
            for (JsonElement element : data.getAsJsonArray("route")) {
                if (element.isJsonObject() && element.getAsJsonObject().has("distance_km")) {
                    distanceKm += element.getAsJsonObject().get("distance_km").getAsDouble();
                }
            }
        } else if (data.has("distance_km")) {
            distanceKm = data.get("distance_km").getAsDouble();
        }

        String mode = "sea";
        if (data.has("route") && data.get("route").isJsonArray()) {
            for (JsonElement element : data.getAsJsonArray("route")) {
                if (!element.isJsonObject()) {
                    continue;
                }
                JsonObject routePart = element.getAsJsonObject();
                if (routePart.has("transport_mode")) {
                    mode = routePart.get("transport_mode").getAsString().toLowerCase();
                    break;
                }
                if (routePart.has("mode")) {
                    mode = routePart.get("mode").getAsString().toLowerCase();
                    break;
                }
            }
        }

        Map<String, Object> co2e = new HashMap<String, Object>();
        co2e.put("per_item", buildBreakdown(data, 1));
        co2e.put("total", buildBreakdown(data, qty));
        co2e.put("unit", "g");

        enriched.put("quantity", qty);
        enriched.put("co2e", co2e);
        enriched.put("co2e_per_item_kg", co2ePerItemKg);
        enriched.put("co2e_total_kg", co2ePerItemKg * qty);
        enriched.put("distance_km", distanceKm);
        enriched.put("transport_mode", mode);
        enriched.put("_split_method", "Climatiq v3 intermodal breakdown");
        return enriched;
    }

    private Map<String, Object> buildBreakdown(JsonObject data, int multiplier) {
        double operationKg = data.has("vehicle_operation_co2e") ? data.get("vehicle_operation_co2e").getAsDouble() : 0D;
        double provisionKg = data.has("vehicle_energy_provision_co2e")
                ? data.get("vehicle_energy_provision_co2e").getAsDouble() : 0D;
        double totalKg = data.has("co2e") ? data.get("co2e").getAsDouble() : 0D;

        Map<String, Object> breakdown = new HashMap<String, Object>();
        breakdown.put("total", kgToGrams(totalKg * multiplier));
        breakdown.put("ttw", kgToGrams(operationKg * multiplier));
        breakdown.put("wtt", kgToGrams(provisionKg * multiplier));
        return breakdown;
    }

    private long kgToGrams(double kg) {
        return Math.round(kg * 1000D);
    }

    private Map<String, Object> errorPayload(String message) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put("error", message);
        return error;
    }

    private Map<String, Object> jsonObjectToMap(JsonObject jsonObject) {
        return gson.fromJson(jsonObject, Map.class);
    }
}
