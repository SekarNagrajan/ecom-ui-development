package net.ecom.carbon;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.ecom.common.GeneralConf;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CarbonScheduleAjax extends HttpServlet {

    private static final String FALLBACK_ERROR =
            "We could not perform the calculations at this time. Please try again.";

    private final Gson gson = new Gson();
    private final CarbonScheduleService carbonScheduleService = new CarbonScheduleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            JsonObject payload = readJsonBody(request);
            if (payload == null) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(errorPayload(FALLBACK_ERROR)));
                return;
            }

            String weightKg = payload.has("weight_kg") ? payload.get("weight_kg").getAsString() : "10000";
            String quantity = payload.has("quantity") ? payload.get("quantity").getAsString() : "1";

            CarbonScheduleResult result;
            if (payload.has("schedule_route_index") && !payload.get("schedule_route_index").isJsonNull()) {
                String scheduleCardCarbon = (String) getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_CARD_CARBON);
                if (!"Y".equalsIgnoreCase(scheduleCardCarbon)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(gson.toJson(errorPayload("Carbon calculator is not enabled for schedule cards.")));
                    return;
                }
                int scheduleRouteIndex = payload.get("schedule_route_index").getAsInt();
                result = carbonScheduleService.calculateForScheduleRouteIndex(
                        scheduleRouteIndex, weightKg, quantity, getServletContext(), request.getSession());
            } else {
                String origin = payload.has("origin") ? payload.get("origin").getAsString() : "";
                String delivery = payload.has("delivery") ? payload.get("delivery").getAsString() : "";
                result = carbonScheduleService.calculateForRoute(
                        origin, delivery, weightKg, quantity, getServletContext(), request.getSession());
            }

            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(toResponseMap(result)));
        } catch (Exception ex) {
            CarbonApiLogger.log(getServletContext(), "[ECOM-WAR] CarbonScheduleAjax request failed message=" + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(errorPayload(FALLBACK_ERROR)));
        }
    }

    /** Response shape consumed by {@code carbon-calculator.js} (success path uses {@code climatiq_response} only). */
    private Map<String, Object> toResponseMap(CarbonScheduleResult result) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        ClimatiqResponse climatiqResponse = result.getClimatiqResponse();
        output.put("climatiq_response", climatiqResponse == null ? null : climatiqResponse.getBody());
        return output;
    }

    private JsonObject readJsonBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        if (body.length() == 0) {
            return null;
        }
        return gson.fromJson(body.toString(), JsonObject.class);
    }

    private Map<String, String> errorPayload(String message) {
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", message);
        return error;
    }
}
