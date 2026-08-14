/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.lookup.EJBLookUp;
import net.ecom.common.utils.HMACSHA256;
import net.ecom.controller.EcomCacheLoader;

/**
 *
 * @author gobinath.t
 */
public class ExternalTrackingAction extends org.apache.struts.action.Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    private static final int TIME_TOLERANCE = 5 * 60 * 1000; // 5 minutes in milliseconds

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "sessionex";
        boolean isvalid=true;
        try {
            System.out.println("[Ecom] ExternalTrackingAction");
            // Get parameters from URL
            HttpSession session = request.getSession(false);
            String referenceNo = request.getParameter("referenceNo");
            String timestamp = request.getParameter("timestamp");
            String signature = request.getParameter("signature");

            // Validate required parameters
            if (referenceNo == null || timestamp == null || signature == null) {
                System.out.println("[Ecom] ExternalTrackingAction IF1");
                forward = "sessionex";
                isvalid=false;
            }

            // Validate timestamp (ensure it is within 5-minute tolerance)
            long currentTime = Instant.now().getEpochSecond(); // current time in seconds
            long requestTime = Long.parseLong(timestamp);
            System.out.println("[Ecom] ExternalTrackingAction diff=> " + Math.abs(currentTime - requestTime));
            System.out.println("[Ecom] ExternalTrackingAction TIME_TOLERANCE=> " + (TIME_TOLERANCE / 1000));
            if (Math.abs(currentTime - requestTime) > TIME_TOLERANCE / 1000) {
                System.out.println("[Ecom] ExternalTrackingAction IF2");
                // Timestamp is not within the 5-minute tolerance window
                forward = "sessionex";
                isvalid=false;
            }
            // Generate signature using referenceNo and timestamp
            String dataToHash = referenceNo + timestamp;
            String generatedSignature = HMACSHA256.hashWithKey(dataToHash);

            // Compare generated signature with URL signature
            if (generatedSignature.equals(signature) && isvalid) {
                request.setAttribute("tracktype", "logintracking");
                request.setAttribute("logintrackrefno", referenceNo);
                String refNoType = ecomCntrlObj.checkBLBookingNo(referenceNo);
                request.setAttribute("refNoType", refNoType);

                Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                session.setAttribute("SESSION_AUTHENTICATION", authObj);
                session.setAttribute("IS_ACTIVEPAGE", "Tracking");
                session.setAttribute("systemid", session.getId());
                session.setAttribute("SESSION_LOGIN_TYPE", "U");
                forward = "newTracking";

            } else {
                System.out.println("[Ecom] ExternalTrackingAction IF3");
                forward = "sessionex";
            }
            System.out.println("[Ecom] ExternalTrackingAction forward==> "+forward);
                   //
            try {
                ServletContext context = servlet.getServletContext();
                ArrayList objlst = (ArrayList) context.getAttribute("PORTLISTOBJ");
                String checkCacheRefresh = (String) context.getAttribute("checkCacheRefresh");
                System.out.println("[Ecom] ExternalTrackingAction  context.getAttribute('checkCacheRefresh')===> " + context.getAttribute("checkCacheRefresh"));
                saveToken(request);
                if (objlst == null || checkCacheRefresh == null || (!"CACHEREFRESHED".equalsIgnoreCase(checkCacheRefresh))) {
                    EcomCacheLoader loader = new EcomCacheLoader();
                    System.out.println("[Ecom] ExternalTrackingAction     >>>object is null in mainpage");
                    loader.cacheLoader(context);
                    loader.cacheLoaderWS(context);
                    System.out.println("[[Ecom] ExternalTrackingAction  checkCacheRefresh========> CACHEREFRESHED");
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
