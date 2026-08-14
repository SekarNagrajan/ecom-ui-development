package net.ecom.carbon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.ecom.common.Validation;
import net.ecom.vo.Route;

public final class ScheduleCarbonPayloadMapper {

    private static final String DEFAULT_SEA_VESSEL_TYPE = "container";

    private ScheduleCarbonPayloadMapper() {
    }

    public static final class BuildResult {
        private final ClimatiqRequest climatiqRequest;
        private final List<CarbonScheduleLegMetadata> legMetadata;

        private BuildResult(ClimatiqRequest climatiqRequest, List<CarbonScheduleLegMetadata> legMetadata) {
            this.climatiqRequest = climatiqRequest;
            this.legMetadata = legMetadata;
        }

        public ClimatiqRequest getClimatiqRequest() {
            return climatiqRequest;
        }

        public List<CarbonScheduleLegMetadata> getLegMetadata() {
            return legMetadata;
        }
    }

    public static ClimatiqRequest buildRequest(List<Route> routes, double weightKg, int quantity) {
        return buildFromRoutes(routes, weightKg, quantity).getClimatiqRequest();
    }

    public static BuildResult buildFromRoutes(List<Route> routes, double weightKg, int quantity) {
        List<String> locations = new ArrayList<String>();
        JsonArray legs = new JsonArray();
        List<Map<String, String>> routingLog = new ArrayList<Map<String, String>>();
        List<CarbonScheduleLegMetadata> legMetadata = new ArrayList<CarbonScheduleLegMetadata>();

        if (routes == null || routes.isEmpty()) {
            return new BuildResult(new ClimatiqRequest(locations, legs, weightKg, quantity, routingLog), legMetadata);
        }

        for (Route route : routes) {
            if (route == null) {
                continue;
            }

            String loadport = normalizePort(route.getLoadport());
            String disport = normalizePort(route.getDisport());
            if (loadport.isEmpty() || disport.isEmpty() || loadport.equalsIgnoreCase(disport)) {
                continue;
            }

            if (locations.isEmpty()) {
                locations.add(loadport);
            } else {
                String previous = locations.get(locations.size() - 1);
                if (!previous.equalsIgnoreCase(loadport)) {
                    locations.add(loadport);
                }
            }

            if (!locations.get(locations.size() - 1).equalsIgnoreCase(disport)) {
                locations.add(disport);
            }

            JsonObject leg = new JsonObject();
            String transportMode = mapLegTypeToTransportMode(route.getLegType());
            leg.addProperty("transport_mode", transportMode);
            if (isSeaMode(transportMode)) {
                leg.addProperty("vessel_type", DEFAULT_SEA_VESSEL_TYPE);
            }
            legs.add(leg);

            Map<String, String> legLog = new LinkedHashMap<String, String>();
            legLog.put("loadport", loadport);
            legLog.put("disport", disport);
            legLog.put("legType", Validation.checkNull(route.getLegType()));
            legLog.put("transport_mode", transportMode);
            legLog.put("vessel", Validation.checkNull(route.getVessel()));
            legLog.put("vesselName", Validation.checkNull(route.getVesselName()));
            legLog.put("service", Validation.checkNull(route.getService()));
            legLog.put("vessel_type", isSeaMode(transportMode) ? DEFAULT_SEA_VESSEL_TYPE : "");
            routingLog.add(legLog);

            legMetadata.add(new CarbonScheduleLegMetadata(
                    loadport,
                    disport,
                    nullableValue(route.getLegType()),
                    transportMode,
                    nullableValue(route.getVesselName()),
                    nullableValue(route.getServiceName()),
                    nullableValue(route.getVoyage()),
                    null, // ScheduleAPIService does not map LegApiVo.vessel_imo onto Route yet.
                    null)); // ScheduleAPIService does not map LegApiVo.vessel_operator onto Route yet.
        }

        return new BuildResult(new ClimatiqRequest(locations, legs, weightKg, quantity, routingLog), legMetadata);
    }

    public static String mapLegTypeToTransportMode(String legType) {
        String code = Validation.checkNull(legType).trim().toUpperCase();
        if (code.isEmpty()) {
            return "sea";
        }
        if ("T".equals(code)) {
            return "road";
        }
        if ("R".equals(code)) {
            return "rail";
        }
        if ("B".equals(code) || "I".equals(code)) {
            return "inland_waterways";
        }
        if ("M".equals(code) || "F".equals(code) || "C".equals(code)) {
            return "sea";
        }
        return "sea";
    }

    private static boolean isSeaMode(String transportMode) {
        return "sea".equalsIgnoreCase(Validation.checkNull(transportMode));
    }

    private static String normalizePort(String portCode) {
        return Validation.checkNull(portCode).trim().toUpperCase();
    }

    private static String nullableValue(String value) {
        String normalized = Validation.checkNull(value).trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
