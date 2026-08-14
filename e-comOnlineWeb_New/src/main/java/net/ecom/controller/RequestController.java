/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;
import org.apache.struts.tiles.TilesRequestProcessor;
import net.ecom.lookup.EJBLookUp;

public class RequestController extends TilesRequestProcessor {

    public RequestController() {
    }

    // Changes for analytics purpose by Prakash Mani
    // --- Final submit endpoints (mark Completed only here) --- start
    private static final Map<String, String> WF_COMPLETE_PATHS = new HashMap<>();

    static {
        WF_COMPLETE_PATHS.put("/ebookingsaveNew.do", "BKG");
        WF_COMPLETE_PATHS.put("/amendmentbookingsave.do", "BKG");

        WF_COMPLETE_PATHS.put("/SubmitSI.do", "SHI");
        WF_COMPLETE_PATHS.put("/SubmitBL.do", "BOL");
        WF_COMPLETE_PATHS.put("/SubmitVGM.do", "VGM");
    }
    private static final String SK_WF_ID = "WF_ACTIVE_ID";
    private static final String SK_WF_MODULE = "WF_ACTIVE_MODULE";
    private static final String SK_WF_STATUS = "WF_ACTIVE_STATUS";

    private static void clearWorkflow(HttpSession s) {
        long newWfId = 0;
        s.setAttribute(SK_WF_ID, newWfId);
        s.setAttribute(SK_WF_MODULE, null);
        s.setAttribute(SK_WF_STATUS, null);
    }

    //end of Changes for analytics purpose by Prakash Mani
    @Override
    protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
        boolean iscontinueaccess = true;
        try {
            ServletContext context = servlet.getServletContext();
            // page encoding
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            //
            System.out.println(
                    "[ECOM-WAR] RequestController request.getServletPath()-------- > " + request.getServletPath());
                    LogUtil.log("RequestController",
                    "request.getServletPath() Config-------- > " + request.getServletPath()
                            + " method=" + request.getMethod()
                            + " uri=" + request.getRequestURI()
                            + " qs=" + request.getQueryString());        

            if (!(request.getServletPath().equals("/Welcome.do") || request.getServletPath().equals("/Accesslogin.do")
                    || request.getServletPath().equals("/Agency.do") || request.getServletPath().equals("/Admin.do")
                    || request.getServletPath().equals("/DynamicColumn.do")
                    || request.getServletPath().equals("/cargoprotect.do")
                    || request.getServletPath().equals("/byport.do") || request.getServletPath().equals("/byvessel.do")
                    || request.getServletPath().equals("/schedules.do")
                    || request.getServletPath().equals("/carbonFromLogin.do")
                    || request.getServletPath().equals("/tracking.do") || request.getServletPath().equals("/notice.do")
                    || request.getServletPath().equals("/cargoprotectpdf.do")
                    || ("D".equalsIgnoreCase((String) context.getAttribute("VGMMenuCategory"))
                    && request.getServletPath().equals("/ecomvgm.do"))
                    || request.getServletPath().equals("/extSchedule.do")
                    || request.getServletPath().equals("/extTracking.do")
                    || request.getServletPath().equals("/forgotPasswordReset.do")
                    || request.getServletPath().equals("/validatePasswordResetToken.do")
                    || request.getServletPath().equals("/stripeWebhook.do")
                    || request.getServletPath().equals("/ngeniusWebhook.do"))) {
                LogUtil.log("RequestController", "=====>inside (protected path, session check)");
                HttpSession session = request.getSession(false);
                LogUtil.log("RequestController", "session=" + session);
                if (session != null) {
                    System.out.println("[ECOM-WAR] RequestController sessionId=" + session.getId()
                            + " systemid=" + session.getAttribute("systemid"));
                    if (!session.getId().equals((String) session.getAttribute("systemid"))) {
                        iscontinueaccess = false;
                        try {
                            if (request.getHeader("X-Requested-With") != null
                                    && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
                                response.sendError(403); // Response error set
                            } else {
                                response.sendRedirect("SessionExpired.jsp");// SessionOut
                            }
                        } catch (Exception e) {
                            iscontinueaccess = false;
                            System.out.println("[ECOM-WAR] RequestController Exception while Preprocessing: " + e);
                        }
                    }

                } else {
                    LogUtil.log("RequestController", "session is null — redirect SessionExpired");
                    try {
                        response.sendRedirect("SessionExpired.jsp");// SessionOut
                    } catch (Exception e) {
                        iscontinueaccess = false;
                        System.out.println(
                                "[ECOM-WAR] RequestController Exception while Preproces sSession expired " + e);
                    }
                }
            } else {
                HttpSession session = request.getSession(false);
                // System.out.println("[ECOM-WAR] RequestController
                // session.getAttribute(SESSION_LOGIN_TYPE) =>>>>>>>>>>
                // "+session.getAttribute("SESSION_LOGIN_TYPE") );
                // if ((!(request.getServletPath().equals("/Welcome.do") ||
                // request.getServletPath().equals("/admin") ||
                // request.getServletPath().equals("/cpanel"))) &&
                // (session.getAttribute("SESSION_LOGIN_TYPE") == null ||
                // session.getAttribute("SESSION_LOGIN_TYPE").toString().length()<=0)) {
                // response.sendRedirect("SessionExpired.jsp");//SessionOut
                // }
                LogUtil.log("RequestController", "Final ELSE (public / whitelisted path branch)");
                if ("Y".equalsIgnoreCase(request.getParameter("isviawebsite"))
                        || request.getServletPath().equals("/cargoprotect.do")
                        || request.getServletPath().equals("/cargoprotectpdf.do")) {
                    ArrayList objlst = (ArrayList) context.getAttribute("PORTLISTOBJ");
                    String checkCacheRefresh = (String) context.getAttribute("checkCacheRefresh");
                    LogUtil.log("RequestController", "context.getAttribute('checkCacheRefresh')===> "
                            + context.getAttribute("checkCacheRefresh"));
                    if (objlst == null || checkCacheRefresh == null
                            || (!"CACHEREFRESHED".equalsIgnoreCase(checkCacheRefresh))) {
                        EcomCacheLoader loader = new EcomCacheLoader();
                        LogUtil.log("RequestController", ">>>object is null in mainpage");
                        loader.cacheLoader(context);
                        loader.cacheLoaderWS(context);
                        LogUtil.log("RequestController", "checkCacheRefresh========> CACHEREFRESHED");
                        context.setAttribute("checkCacheRefresh", "CACHEREFRESHED");
                    }
                }

                if (request.getServletPath().equals("/Accesslogin.do")) {
                    String contextCheck = (String) context.getAttribute("APPLNRESTARTCHECK");
                    if (contextCheck == null) {
                        LogUtil.log("RequestController", ">>>context>>"
                                + context.getAttribute("APPLNRESTARTCHECK"));
                        RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
                        rd.forward(request, response);
                        // response.sendRedirect("index.jsp");//SessionOut
                    }
                    // HttpSession session = request.getSession(false);
                    // if (session == null || session.getAttribute("SESSION_LOGIN_TYPE") == null) {
                    // session.setAttribute("SESSION_LOGIN_TYPE", "U");
                    // }
                }
                if ("Y".equalsIgnoreCase(request.getParameter("isviawebsite"))
                        || request.getServletPath().equals("/cargoprotect.do")
                        || request.getServletPath().equals("/cargoprotectpdf.do")) {
                    LogUtil.log("RequestController", "MainPage action=====> "
                            + session.getAttribute("SESSION_LOGIN_TYPE"));
                    if (session.getAttribute("SESSION_LOGIN_TYPE") == null) {
                        session.setAttribute("SESSION_LOGIN_TYPE", "U");
                    }
                    String selectedLang = (String) session.getAttribute("SelectedLanguage");
                    if (selectedLang == null) {
                        session.setAttribute("SelectedLanguage", "en");
                    }
                }
            }
            if ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Enable_Menu_Endpoint_Validation))
                    && iscontinueaccess) {
                MenuAccess ma = new MenuAccess();
                iscontinueaccess = ma.validateAccess(request);
                if (!iscontinueaccess) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
                if (iscontinueaccess) {
                    AdminMenuAccess admenacs = new AdminMenuAccess();
                    iscontinueaccess = admenacs.validateAccess(request);
                    if (!iscontinueaccess) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                }
            }

            // Changes for analytics purpose by Prakash Mani
            String enablePlatformAnalytics = (String) context.getAttribute(GeneralConf.Enable_PlatformAnalytics);
            LogUtil.log("RequestController", "Enable_PlatformAnalytics (context)=" + enablePlatformAnalytics);
            if ("Y".equalsIgnoreCase(enablePlatformAnalytics)) {
                HttpSession session = request.getSession(false);
                LogUtil.log("PlatformAnalytics", ">>> FILTER " + request.getMethod()
                        + " uri=" + request.getRequestURI()
                        + " ctx=" + request.getContextPath()
                        + " sp=" + request.getServletPath()
                        + " pi=" + request.getPathInfo()
                        + " qs=" + request.getQueryString()
                        + " type=" + request.getDispatcherType());
                LogUtil.log("PlatformAnalytics", "WF_COMPLETE_PATHS.containsKey(req.getServletPath()): "
                        + WF_COMPLETE_PATHS.containsKey(request.getServletPath()));
                LogUtil.log("PlatformAnalytics", "before setting the workflow ID: " + session.getAttribute(SK_WF_ID)
                        + "  workflow module: " + session.getAttribute(SK_WF_MODULE)
                        + "  workflow status: " + session.getAttribute(SK_WF_STATUS));
                if (WF_COMPLETE_PATHS != null && WF_COMPLETE_PATHS.containsKey(request.getServletPath())) {

                    String completeModule = WF_COMPLETE_PATHS.get(request.getServletPath());
                    LogUtil.log("PlatformAnalytics",
                            "completeModule: " + completeModule + " response.getStatus(): " + response.getStatus());
                    if (completeModule != null && completeModule.length() > 0 && response.getStatus() >= 200
                            && response.getStatus() < 300) {
                        Long activeId = (Long) session.getAttribute(SK_WF_ID);
                        LogUtil.log("PlatformAnalytics", "activeId: " + activeId);
                        String activeMod = (String) session.getAttribute(SK_WF_MODULE);
                        String activeStatus = (String) session.getAttribute(SK_WF_STATUS);
                        LogUtil.log("PlatformAnalytics", "activeMod: " + activeMod);
                        LogUtil.log("PlatformAnalytics", "activeStatus: " + activeStatus);
                        if (activeId != null && activeId > 0 && "S".equals(activeStatus)
                                && completeModule.equals(activeMod)) {
                            try {
                                EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
                                 ecomAnalyticsObj.closeModuleStatus(activeId); // comment this line for New CR implementation
                            } catch (Exception ignore) {
                            }
                            session.setAttribute(SK_WF_STATUS, "C");
                            clearWorkflow(session);
                        }
                    }
                }
            }
            //end of Changes for analytics purpose by Prakash Mani
            LogUtil.log("RequestController", "iscontinueaccess-> " + iscontinueaccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iscontinueaccess;
    }
}
