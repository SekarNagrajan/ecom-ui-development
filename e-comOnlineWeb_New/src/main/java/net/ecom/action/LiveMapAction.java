package net.ecom.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LiveMapAction extends org.apache.struts.action.Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            HttpSession session = request.getSession();
            ServletContext context = servlet.getServletContext();

            String enableLiveTracking = Validation.checkNull((String) context.getAttribute(GeneralConf.Enable_Live_Tracking));
            if (!"Y".equalsIgnoreCase(enableLiveTracking)) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Live tracking is not enabled");
                out.print(jsonResponse.toString());
                out.flush();
                return null;
            }

            String containerIso = Validation.checkNull(request.getParameter("containerIso"));
            if (containerIso.length() == 0) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Container number is required");
                out.print(jsonResponse.toString());
                out.flush();
                return null;
            }

            @SuppressWarnings("unchecked")
            Set<String> allowedContainers = (Set<String>) session.getAttribute("LIVEMAP_ALLOWED_CONTAINERS");
            if (allowedContainers == null || allowedContainers.isEmpty()) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Please search for a booking first");
                out.print(jsonResponse.toString());
                out.flush();
                return null;
            }

            String[] requestedContainers = containerIso.split(",");
            for (String reqCont : requestedContainers) {
                if (!allowedContainers.contains(reqCont.trim().toUpperCase())) {
                    System.out.println("[ECOM-WAR] LiveMapAction Unauthorized container request: " + reqCont.trim());
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Unauthorized container request");
                    out.print(jsonResponse.toString());
                    out.flush();
                    return null;
                }
            }

            String snapgisApiUrl = Validation.checkNull((String) context.getAttribute(GeneralConf.SnapGIS_API_URL));
            String snapgisApiKey = Validation.checkNull((String) context.getAttribute(GeneralConf.SnapGIS_API_Key));
            String snapgisApiKeySecret = Validation.checkNull((String) context.getAttribute(GeneralConf.SnapGIS_API_Key_Secret));

            if (snapgisApiUrl.length() == 0 || snapgisApiKey.length() == 0 || snapgisApiKeySecret.length() == 0) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "SnapGIS configuration is incomplete");
                out.print(jsonResponse.toString());
                out.flush();
                return null;
            }

            String authString = snapgisApiKey + ":" + snapgisApiKeySecret;
            String authStringEnc = javax.xml.bind.DatatypeConverter.printBase64Binary(authString.getBytes("UTF-8"));

            String apiUrl = snapgisApiUrl + containerIso;
            System.out.println("[ECOM-WAR] LiveMapAction SnapGIS API URL: " + apiUrl);

            Client restClient = Client.create();
            WebResource webResource = restClient.resource(apiUrl);
            ClientResponse resp = webResource
                    .accept("application/json")
                    .header("Authorization", "Basic " + authStringEnc)
                    .header("User-Agent", "ecomOnline/1.0")
                    .get(ClientResponse.class);

            int httpStatus = resp.getStatus();
            String apiOutput = resp.getEntity(String.class);
            System.out.println("[ECOM-WAR] LiveMapAction SnapGIS response status: " + httpStatus);

            if (httpStatus == 200) {
                JsonArray containers = new JsonArray();
                parseAndAddContainers(apiOutput, containers);

                if (containers.size() > 0) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.add("containers", containers);
                } else {
                    jsonResponse.addProperty("status", "nodata");
                    jsonResponse.addProperty("message", "No live location data available for the given container(s)");
                }
            } else if (httpStatus == 429) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Rate limit exceeded. Please try again after a minute.");
            } else {
                System.out.println("[ECOM-WAR] LiveMapAction SnapGIS error body: " + apiOutput);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Unable to fetch live location. Status: " + httpStatus);
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] LiveMapAction Exception: " + e.getMessage());
            e.printStackTrace();
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "An error occurred while fetching live location");
        }

        out.print(jsonResponse.toString());
        out.flush();
        return null;
    }

    private void parseAndAddContainers(String apiOutput, JsonArray containers) {
        try {
            JsonObject respObj = new JsonParser().parse(apiOutput).getAsJsonObject();
            JsonArray dataArray = respObj.has("data") ? respObj.getAsJsonArray("data") : null;

            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    JsonObject asset = dataArray.get(i).getAsJsonObject();
                    JsonObject containerObj = new JsonObject();

                    String assetName = getJsonString(asset, "name");
                    containerObj.addProperty("name", assetName);

                    String contIso = assetName;
                    if (asset.has("details") && asset.get("details").isJsonObject()) {
                        JsonObject details = asset.getAsJsonObject("details");
                        String detailIso = getJsonString(details, "containerIso");
                        if (detailIso.length() > 0) {
                            contIso = detailIso;
                        }
                    }
                    containerObj.addProperty("containerIso", contIso);

                    double lat = 0.0, lng = 0.0;
                    String reportTime = "";
                    String reportType = "";
                    if (asset.has("report") && asset.get("report").isJsonObject()) {
                        JsonObject report = asset.getAsJsonObject("report");
                        reportType = getJsonString(report, "reportType");
                        if (report.has("position") && report.get("position").isJsonObject()) {
                            JsonObject position = report.getAsJsonObject("position");
                            lat = getJsonDouble(position, "latitude");
                            lng = getJsonDouble(position, "longitude");
                            reportTime = getJsonString(position, "reportTime", "receivedTime");
                        }
                    }
                    containerObj.addProperty("latitude", lat);
                    containerObj.addProperty("longitude", lng);
                    containerObj.addProperty("lastUpdated", reportTime);
                    containerObj.addProperty("reportType", reportType);

                    int batteryLevel = -1;
                    if (asset.has("deviceDetails") && asset.get("deviceDetails").isJsonObject()) {
                        JsonObject deviceDetails = asset.getAsJsonObject("deviceDetails");
                        if (deviceDetails.has("batteryLevel") && !deviceDetails.get("batteryLevel").isJsonNull()) {
                            batteryLevel = deviceDetails.get("batteryLevel").getAsInt();
                        }
                    }
                    containerObj.addProperty("batteryLevel", batteryLevel);

                    if (lat != 0.0 || lng != 0.0) {
                        containers.add(containerObj);
                    }
                }
            }
        } catch (Exception parseEx) {
            System.out.println("[ECOM-WAR] LiveMapAction Error parsing SnapGIS response: " + parseEx.getMessage());
        }
    }

    private String getJsonString(JsonObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key) && !obj.get(key).isJsonNull()) {
                return obj.get(key).getAsString();
            }
        }
        return "";
    }

    private double getJsonDouble(JsonObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key) && !obj.get(key).isJsonNull()) {
                try {
                    return obj.get(key).getAsDouble();
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        return 0.0;
    }
}
