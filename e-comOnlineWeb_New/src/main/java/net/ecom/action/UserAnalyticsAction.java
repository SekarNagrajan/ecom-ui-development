package net.ecom.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote;
import net.ecom.lookup.EJBLookUp;

/**
 * Action class to handle User Analytics data requests
 *
 * @author prakash.mani
 */
public class UserAnalyticsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        System.out.println("UserAnalyticsAction.execute() called");
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request parameters: " + request.getQueryString());
        
        try {
            String action = request.getParameter("action");
            System.out.println("Action parameter: " + action);
            String startDate = request.getParameter("start");
            String endDate = request.getParameter("end");
            String customer = request.getParameter("customer");

            // Validate required parameters (except for customerSearch and loadAllCustomers)
            if (!"customerSearch".equals(action) && !"loadAllCustomers".equals(action) && 
                (startDate == null || startDate.trim().isEmpty() || 
                 endDate == null || endDate.trim().isEmpty())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"start and end date parameters are required\"}");
                return null;
            }

            List<Map<String, Object>> data = null;

            switch (action) {
                case "moduleUsage":
                    data = getModuleUsageCounts(startDate, endDate, customer);
                    break;
                case "timeSpent":
                    data = getHoursThisWeekPerModule(startDate, endDate, customer);
                    break;
                case "completedVsAbandoned":
                    data = getCompletedVsAbandoned(startDate, endDate, customer);
                    break;
                case "userLocation":
                    data = getUniqueCustomersByCountry(startDate, endDate, customer);
                    break;
                case "topRoutes":
                    data = getTopRoutes(startDate, endDate, customer);
                    break;
                case "customerSearch":
                    data = getCustomerSearch(customer);
                    break;
                case "loadAllCustomers":
                    System.out.println("UserAnalyticsAction: Processing loadAllCustomers request");
                    data = getAllCustomers();
                    System.out.println("UserAnalyticsAction: getAllCustomers returned " + (data != null ? data.size() : "null") + " customers");
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid action parameter\"}");
                    return null;
            }

            // Convert data to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResponse = gson.toJson(data);
            
            System.out.println("UserAnalyticsAction: JSON response length: " + jsonResponse.length());
            System.out.println("UserAnalyticsAction: JSON response preview: " + 
                (jsonResponse.length() > 200 ? jsonResponse.substring(0, 200) + "..." : jsonResponse));

            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            // Write JSON response
            PrintWriter out = response.getWriter();
            out.write(jsonResponse);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }

        return null;
    }

    // Database query methods with date range and customer filtering
    private List<Map<String, Object>> getModuleUsageCounts(String startDate, String endDate, String customer) {
        EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            out = ecomAnalyticsObj.moduleUsageCounts(startDate, endDate, customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private List<Map<String, Object>> getHoursThisWeekPerModule(String startDate, String endDate, String customer) {
        EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            out = ecomAnalyticsObj.hoursThisWeekPerModule(startDate, endDate, customer);
            System.out.println("output: "+out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private List<Map<String, Object>> getCompletedVsAbandoned(String startDate, String endDate, String customer) {
        EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            out = ecomAnalyticsObj.completedVsAbandoned(startDate, endDate, customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private List<Map<String, Object>> getUniqueCustomersByCountry(String startDate, String endDate, String customer) {
        EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            out = ecomAnalyticsObj.uniqueCustomersByCountry(startDate, endDate, customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private List<Map<String, Object>> getTopRoutes(String startDate, String endDate, String customer) {
        EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            out = ecomAnalyticsObj.topRoutes(startDate, endDate, customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private List<Map<String, Object>> getCustomerSearch(String searchTerm) {
        EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            out = ecomAnalyticsObj.customerSearch(searchTerm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private List<Map<String, Object>> getAllCustomers() {
        EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            out = ecomAnalyticsObj.loadAllCustomers();
            System.out.println("UserAnalyticsAction.getAllCustomers: Retrieved " + out.size() + " customers");
        } catch (Exception e) {
            System.err.println("Error in UserAnalyticsAction.getAllCustomers: " + e.getMessage());
            e.printStackTrace();
        }
        return out;
    }
}
