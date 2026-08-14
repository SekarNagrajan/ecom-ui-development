package net.ecom.ajax;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;

/**
 * Backend proxy for Google Places API - keeps API key server-side.
 */
@WebServlet(name = "GoogleAddressAjax", urlPatterns = {"/AddressLookupAjax", "/GoogleAddressAjax"})
public class GoogleAddressAjax extends HttpServlet {

    private static final Gson GSON = new Gson();
    private static final int MAX_INPUT_LENGTH = 200;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            ServletContext context = request.getServletContext();
            if (!isFeatureEnabled(context)) {
                out.print(GSON.toJson(errorResponse("Google address search is disabled")));
                return;
            }

            HttpSession session = request.getSession(false);
            if (!isAuthorized(session)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print(GSON.toJson(errorResponse("Unauthorized")));
                return;
            }

            String apiKey = Validation.checkNull((String) context.getAttribute(GeneralConf.Google_Maps_API_Key));
            if (apiKey.length() == 0) {
                out.print(GSON.toJson(errorResponse("Google Maps API key is not configured")));
                return;
            }

            String type = Validation.checkNull(request.getParameter("type"));
            if ("autocomplete".equalsIgnoreCase(type)) {
                out.print(GSON.toJson(handleAutocomplete(request, apiKey)));
            } else if ("details".equalsIgnoreCase(type)) {
                out.print(GSON.toJson(handlePlaceDetails(request, apiKey)));
            } else {
                out.print(GSON.toJson(errorResponse("Invalid request type")));
            }
        } catch (Exception e) {
            response.getWriter().print(GSON.toJson(errorResponse("Unable to fetch address details")));
        }
    }

    private boolean isFeatureEnabled(ServletContext context) {
        return "Y".equalsIgnoreCase(Validation.checkNull(
                (String) context.getAttribute(GeneralConf.Enable_Google_Address_Search)));
    }

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        String isViaLogin = Validation.checkNull((String) session.getAttribute("IS_VIA_LOGIN"));
        if ("Yes".equalsIgnoreCase(isViaLogin)) {
            return true;
        }
        String registrationFlow = Validation.checkNull((String) session.getAttribute("NEWDESIGNPRODUCT"));
        if ("registration".equalsIgnoreCase(registrationFlow)) {
            return true;
        }
        String regValidate = Validation.checkNull((String) session.getAttribute("RegValidate"));
        return "Send".equalsIgnoreCase(regValidate);
    }

    private Map<String, Object> handleAutocomplete(HttpServletRequest request, String apiKey) throws Exception {
        String input = Validation.checkNull(request.getParameter("input")).trim();
        if (input.length() < 2) {
            return errorResponse("Minimum 2 characters required");
        }
        if (input.length() > MAX_INPUT_LENGTH) {
            input = input.substring(0, MAX_INPUT_LENGTH);
        }

        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                + encode(input)
                + "&key="
                + encode(apiKey);

        JsonObject googleResponse = callGoogleApi(url);
        String status = getAsString(googleResponse, "status");
        if (!"OK".equalsIgnoreCase(status) && !"ZERO_RESULTS".equalsIgnoreCase(status)) {
            return errorResponse("Google autocomplete failed: " + status);
        }

        Map<String, Object> result = successResponse();
        List<Map<String, String>> predictions = new ArrayList<>();
        JsonArray items = googleResponse.has("predictions") ? googleResponse.getAsJsonArray("predictions") : null;
        if (items != null) {
            for (JsonElement element : items) {
                JsonObject item = element.getAsJsonObject();
                Map<String, String> prediction = new HashMap<>();
                prediction.put("placeId", getAsString(item, "place_id"));
                prediction.put("description", getAsString(item, "description"));
                if (item.has("structured_formatting") && item.get("structured_formatting").isJsonObject()) {
                    JsonObject formatting = item.getAsJsonObject("structured_formatting");
                    prediction.put("mainText", getAsString(formatting, "main_text"));
                    prediction.put("secondaryText", getAsString(formatting, "secondary_text"));
                }
                predictions.add(prediction);
            }
        }
        result.put("predictions", predictions);
        return result;
    }

    private Map<String, Object> handlePlaceDetails(HttpServletRequest request, String apiKey) throws Exception {
        String placeId = Validation.checkNull(request.getParameter("placeId")).trim();
        if (placeId.length() == 0) {
            return errorResponse("Place id is required");
        }

        String fields = "place_id,name,formatted_address,address_components,formatted_phone_number,international_phone_number";
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                + encode(placeId)
                + "&fields="
                + encode(fields)
                + "&key="
                + encode(apiKey);

        JsonObject googleResponse = callGoogleApi(url);
        String status = getAsString(googleResponse, "status");
        if (!"OK".equalsIgnoreCase(status)) {
            return errorResponse("Google place details failed: " + status);
        }

        JsonObject place = googleResponse.has("result") && googleResponse.get("result").isJsonObject()
                ? googleResponse.getAsJsonObject("result") : null;
        if (place == null) {
            return errorResponse("Place details not found");
        }

        Map<String, Object> result = successResponse();
        result.put("data", buildAddressData(place));
        return result;
    }

    private Map<String, Object> buildAddressData(JsonObject place) {
        JsonArray components = place.has("address_components") && place.get("address_components").isJsonArray()
                ? place.getAsJsonArray("address_components") : new JsonArray();

        String streetNumber = getComponent(components, "street_number", false);
        String route = getComponent(components, "route", false);
        String sublocality = firstNonEmpty(
                getComponent(components, "sublocality_level_1", false),
                getComponent(components, "sublocality", false));
        String neighborhood = getComponent(components, "neighborhood", false);
        String city = firstNonEmpty(
                getComponent(components, "locality", false),
                getComponent(components, "administrative_area_level_2", false));
        String state = getComponent(components, "administrative_area_level_1", false);
        String zipcode = getComponent(components, "postal_code", false);
        String countryCode = getComponent(components, "country", true);
        String countryName = getComponent(components, "country", false);

        String street = joinNonEmpty(" ", streetNumber, route);
        if (street.length() == 0) {
            street = firstNonEmpty(sublocality, neighborhood);
        }
        String addressLine2 = sublocality.length() > 0 && street.indexOf(sublocality) < 0 ? sublocality : "";

        Map<String, Object> data = new HashMap<>();
        data.put("placeId", getAsString(place, "place_id"));
        data.put("placeName", getAsString(place, "name"));
        data.put("fullAddress", getAsString(place, "formatted_address"));
        data.put("street", street);
        data.put("addressLine2", addressLine2);
        data.put("city", city);
        data.put("state", state);
        data.put("zipcode", zipcode);
        data.put("countryCode", countryCode);
        data.put("countryName", countryName);
        data.put("countryDisplay", buildCountryDisplay(countryCode, countryName));
        data.put("phone", firstNonEmpty(
                getAsString(place, "formatted_phone_number"),
                getAsString(place, "international_phone_number")));
        return data;
    }

    private JsonObject callGoogleApi(String url) {
        Client client = Client.create();
        WebResource resource = client.resource(url);
        String response = resource.get(String.class);
        return new JsonParser().parse(response).getAsJsonObject();
    }

    private String getComponent(JsonArray components, String type, boolean shortName) {
        for (JsonElement element : components) {
            JsonObject component = element.getAsJsonObject();
            JsonArray types = component.has("types") ? component.getAsJsonArray("types") : null;
            if (types == null) {
                continue;
            }
            for (JsonElement typeElement : types) {
                if (type.equalsIgnoreCase(typeElement.getAsString())) {
                    return shortName
                            ? getAsString(component, "short_name")
                            : getAsString(component, "long_name");
                }
            }
        }
        return "";
    }

    private String buildCountryDisplay(String countryCode, String countryName) {
        if (countryCode.length() > 0 && countryName.length() > 0) {
            return countryCode.toUpperCase() + " - " + countryName.toUpperCase();
        }
        return firstNonEmpty(countryCode, countryName);
    }

    private String joinNonEmpty(String delimiter, String... values) {
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            if (value != null && value.trim().length() > 0) {
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
                builder.append(value.trim());
            }
        }
        return builder.toString();
    }

    private String firstNonEmpty(String... values) {
        for (String value : values) {
            if (value != null && value.trim().length() > 0) {
                return value.trim();
            }
        }
        return "";
    }

    private String getAsString(JsonObject object, String field) {
        if (object == null || !object.has(field) || object.get(field).isJsonNull()) {
            return "";
        }
        return Validation.checkNull(object.get(field).getAsString());
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    private Map<String, Object> successResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
