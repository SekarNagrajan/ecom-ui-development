/*
 * Schedule API Service
 * Calls the Phase2/betweenlocation API to fetch schedule data
 */
package net.ecom.common.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.com.ecom.ws.vo.BetweenLocationApiVo;
import net.com.ecom.ws.vo.LegApiVo;
import net.ecom.vo.Route;

/**
 * Service class to fetch schedule data from external API
 * Used when Fetch_Schedule_From_API plugin is enabled
 * 
 * @author gobinath.t
 */
public class ScheduleAPIService {

    private String baseWSUrl;
    private String feederRestriction;

    public ScheduleAPIService(String baseWSUrl, String feederRestriction) {
        this.baseWSUrl = baseWSUrl;
        this.feederRestriction = feederRestriction != null ? feederRestriction : "N";
    }

    /**
     * Fetch schedule data from API and transform to match geteBookRoutingInfo response
     * 
     * @param origin Origin port code
     * @param delivery Delivery port code
     * @param fromdate Start date (yyyy-MM-dd format)
     * @param todate End date (yyyy-MM-dd format)
     * @return Map with keys "actualschedule" and "altchedule" containing List of Route objects
     */
    public Map<String, List> getScheduleFromAPI(String origin, String delivery, String fromdate, String todate) {
        Map<String, List> result = new HashMap<>();
        List<List<Route>> actualScheduleList = new ArrayList<>();
        List<List<Route>> altScheduleList = new ArrayList<>();

        try {
            // Build API URL
            String apiUrl = buildAPIUrl(origin, delivery, fromdate, todate);
            System.out.println("[ECOM-WAR] ScheduleAPIService API URL: " + apiUrl);

            // Make GET request
            String jsonResponse = executeGetRequest(apiUrl);
           // System.out.println("[ECOM-WAR] ScheduleAPIService API Response received");

            if (jsonResponse != null && jsonResponse.trim().length() > 0) {
                // Parse JSON response
                Gson gson = new Gson();
                Type listType = new TypeToken<List<BetweenLocationApiVo>>() {}.getType();
                List<BetweenLocationApiVo> apiSchedules = gson.fromJson(jsonResponse, listType);

                if (apiSchedules != null && apiSchedules.size() > 0) {
                    System.out.println("[ECOM-WAR] ScheduleAPIService Schedules found: " + apiSchedules.size());
                    
                    // Transform API response to Route objects
                    for (BetweenLocationApiVo schedule : apiSchedules) {
                        List<Route> routeList = transformToRouteList(schedule);
                        if (routeList != null && routeList.size() > 0) {
                            actualScheduleList.add(routeList);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("[ECOM-WAR] ScheduleAPIService Error fetching schedule from API: " + e.getMessage());
            e.printStackTrace();
        }

        result.put("actualschedule", actualScheduleList);
        result.put("altchedule", altScheduleList);

        return result;
    }

    /**
     * Build the API URL with query parameters
     */
    private String buildAPIUrl(String origin, String delivery, String fromdate, String todate) throws Exception {
        StringBuilder url = new StringBuilder();
        
        // Remove trailing slash if present
        String baseUrl = baseWSUrl;
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        
        url.append(baseUrl);
        url.append("/betweenlocation?");
        url.append("origin=").append(URLEncoder.encode(origin, "UTF-8"));
        url.append("&delivery=").append(URLEncoder.encode(delivery, "UTF-8"));
        url.append("&fromdate=").append(URLEncoder.encode(fromdate, "UTF-8"));
        url.append("&todate=").append(URLEncoder.encode(todate, "UTF-8"));
        url.append("&feeder_restriction=").append(URLEncoder.encode(feederRestriction, "UTF-8"));

        return url.toString();
    }

    /**
     * Execute HTTP GET request and return response as string
     */
    private String executeGetRequest(String apiUrl) throws Exception {
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            int responseCode = connection.getResponseCode();
            System.out.println("[ECOM-WAR] ScheduleAPIService Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } else {
                System.out.println("[ECOM-WAR] ScheduleAPIService API returned non-OK status: " + responseCode);
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response.toString();
    }

    /**
     * Transform API response to list of Route objects
     */
    private List<Route> transformToRouteList(BetweenLocationApiVo schedule) {
        List<Route> routeList = new ArrayList<>();

        try {
            if (schedule == null || schedule.getPortpair() == null) {
                return routeList;
            }

            List<LegApiVo> legs = schedule.getPortpair();
            int legNo = 1;

            for (LegApiVo leg : legs) {
                Route route = new Route();

                // Set from schedule
                route.setRouteId(checkNull(schedule.getRouteid()));
                route.setDefRoute(checkNull(schedule.getDefault_route()));

                // Set from leg
                route.setService(checkNull(leg.getService_code()));
                route.setVessel(checkNull(leg.getVessel_code()));
                route.setVoyage(checkNull(leg.getVoyage_number()));
                route.setBound(checkNull(leg.getBound()));
                route.setDischargebound(checkNull(leg.getDbound()));
                route.setLoadport(checkNull(leg.getPol()));
                route.setDisport(checkNull(leg.getPod()));
                route.setETD1(checkNull(leg.getEtd()));
                route.setETA1(checkNull(leg.getEta()));
                route.setLegType(mapLegTypeCode(checkNull(leg.getSchedule_type())));
                route.setLoadcallid(checkNull(leg.getLoad_callid()));
                route.setDischargecallid(checkNull(leg.getDisch_callid()));
                route.setGrpCallId(checkNull(leg.getGroupcallid()));
                route.setBlpolTer(checkNull(leg.getPol_terminal()));
                route.setBlpodTer(checkNull(leg.getPod_terminal()));
                route.setCutoff_polterminal(checkNull(leg.getPol_terminal()));
                route.setCutoff_polarrdate(checkNull(leg.getPol_arrdate()));
                route.setClosingtime(checkNull(leg.getLct_hrs()));

                route.setLegNo(legNo);
                routeList.add(route);
                legNo++;
            }

        } catch (Exception e) {
            System.out.println("[ECOM-WAR] ScheduleAPIService Error transforming schedule: " + e.getMessage());
            e.printStackTrace();
        }

        return routeList;
    }

    /**
     * Check and return empty string if null
     */
    private String checkNull(String value) {
        return value != null ? value.trim() : "";
    }

    /**
     * Map leg type name to single character code
     * Road -> T, Rail -> R, Mainline -> M, Feeder -> F, Barge -> B, Combined -> C, ITT -> I
     */
    private String mapLegTypeCode(String legType) {
        if (legType == null || legType.isEmpty()) {
            return "";
        }
        String type = legType.toUpperCase();
        if (type.startsWith("ROAD") || "T".equals(type)) {
            return "T";
        } else if (type.startsWith("RAIL") || "R".equals(type)) {
            return "R";
        } else if (type.startsWith("MAIN") || "M".equals(type)) {
            return "M";
        } else if (type.startsWith("FEEDER") || "F".equals(type)) {
            return "F";
        } else if (type.startsWith("BARGE") || "B".equals(type)) {
            return "B";
        } else if (type.startsWith("COMBINED") || "C".equals(type)) {
            return "C";
        } else if (type.startsWith("ITT") || "I".equals(type)) {
            return "I";
        }
        // Return first character if no match
        return legType.substring(0, 1).toUpperCase();
    }
}
