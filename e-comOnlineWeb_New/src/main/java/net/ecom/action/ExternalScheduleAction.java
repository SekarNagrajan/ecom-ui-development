/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

/*
 * @author gobinath.t
 */
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.utils.HMACSHA256;
import net.ecom.controller.EcomCacheLoader;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExternalScheduleAction extends org.apache.struts.action.Action {

    private static final int TIME_TOLERANCE = 5 * 60 * 1000; // 5 minutes in milliseconds

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "sessionex";
        boolean isvalid = true;
        try {
            System.out.println("[Ecom] ExternalScheduleAction");
            // Get parameters from URL
            HttpSession session = request.getSession(false);
            String origin = request.getParameter("origin");
            String delivery = request.getParameter("delivery");
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            String timestamp = request.getParameter("timestamp");
            String signature = request.getParameter("signature");
            Hashtable<String, String> portlist = (Hashtable<String, String>) request.getServletContext().getAttribute("SESSION_PORTLIST");

            // Validate required parameters
            if (origin == null || delivery == null || fromDate == null || toDate == null || timestamp == null || signature == null) {
                forward = "sessionex";
                isvalid = false;
            }
            // Validate timestamp (ensure it is within 5-minute tolerance)
            long currentTime = Instant.now().getEpochSecond(); // current time in seconds
            long requestTime = Long.parseLong(timestamp);
            System.out.println("[Ecom] ExternalScheduleAction diff=> " + Math.abs(currentTime - requestTime));
            System.out.println("[Ecom] ExternalScheduleAction TIME_TOLERANCE=> " + (TIME_TOLERANCE / 1000));
            if (Math.abs(currentTime - requestTime) > TIME_TOLERANCE / 1000) {
                // Timestamp is not within the 5-minute tolerance window
                forward = "sessionex";
                isvalid = false;
            }
            // Generate data string to hash: origin + delivery + fromDate + toDate + timestamp
            String dataToHash = origin + delivery + fromDate + toDate + timestamp;

            // Generate signature using HMAC-SHA256 with the shared secret key
            String generatedSignature = HMACSHA256.hashWithKey(dataToHash);

            if (portlist != null) {
                origin = origin + " - " + portlist.get(origin);
                delivery = delivery + " - " + portlist.get(delivery);
            }
            // Compare generated signature with URL signature
            if (generatedSignature.equals(signature) && isvalid) {
                request.setAttribute("schetype", "loginschedule");
                request.setAttribute("loginschepol", origin);
                request.setAttribute("loginschepod", delivery);
                request.setAttribute("loginschefdate", fromDate);
                request.setAttribute("loginschetdate", toDate);
                session.setAttribute("systemid", session.getId());
                session.setAttribute("SESSION_LOGIN_TYPE", "U");
                forward = "locationSearch";

            } else {
                forward = "sessionex";
            }
            System.out.println("[Ecom] ExternalScheduleAction forward==> " + forward);
            //
            try {
                ServletContext context = servlet.getServletContext();
                ArrayList objlst = (ArrayList) context.getAttribute("PORTLISTOBJ");
                String checkCacheRefresh = (String) context.getAttribute("checkCacheRefresh");
                System.out.println("[Ecom] ExternalScheduleAction     context.getAttribute('checkCacheRefresh')===> " + context.getAttribute("checkCacheRefresh"));
                saveToken(request);
                if (objlst == null || checkCacheRefresh == null || (!"CACHEREFRESHED".equalsIgnoreCase(checkCacheRefresh))) {
                    EcomCacheLoader loader = new EcomCacheLoader();
                    System.out.println("[Ecom] ExternalScheduleAction     >>>object is null in mainpage");
                    loader.cacheLoader(context);
                    loader.cacheLoaderWS(context);
                    System.out.println("[Ecom] ExternalScheduleAction   checkCacheRefresh========> CACHEREFRESHED");
                    context.setAttribute("checkCacheRefresh", "CACHEREFRESHED");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            //

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(forward);
    }

}
