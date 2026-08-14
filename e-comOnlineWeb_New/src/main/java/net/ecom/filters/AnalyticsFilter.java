/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.annotation.WebListener;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.CustomerProfile;


/**
 *
 * @author prakash.mani
 */
@WebListener
public class AnalyticsFilter implements Filter, HttpSessionListener {

  private static final String SK_CURRENT_MODULE = "AN_CURRENT_MODULE";
  private static final String SK_LAST_ANALYTICS_ID = "AN_LAST_ANALYTICS_ID";
  private static final String SK_WF_ID = "WF_ACTIVE_ID";
  private static final String SK_WF_MODULE = "WF_ACTIVE_MODULE";
  private static final String SK_WF_STATUS = "WF_ACTIVE_STATUS";
  private static final String CLOSE_BEACON_PATH = "/_analyticsClose.do";

  private static Map<String, String> moduleFromRequest(HttpServletRequest req) {

    Map<String, String> moduleMap = new HashMap<>();
    String path = "";
    if (req != null && req.getServletPath() != null) {
      path = req.getServletPath().toLowerCase();
    }

    if ("/tracking.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "TRK");
      return moduleMap;
    } else if ("/accesslogin.do".equalsIgnoreCase(path)) {
      if ("dashboard".equalsIgnoreCase(req.getParameter("type"))
          || "login".equalsIgnoreCase(req.getParameter("type"))) {
        moduleMap.put("Parent", "DBD");
        return moduleMap;
      } else if ("rates".equalsIgnoreCase(req.getParameter("type"))) {
        moduleMap.put("Parent", "TAR");
        moduleMap.put("Child", "Rates");
        return moduleMap;
      } else if ("tariff".equalsIgnoreCase(req.getParameter("type"))) {
        moduleMap.put("Parent", "TAR");
        moduleMap.put("Child", "Tariff");
        return moduleMap;
      } else if ("surcharge".equalsIgnoreCase(req.getParameter("type"))) {
        moduleMap.put("Parent", "TAR");
        moduleMap.put("Child", "Surcharge");
        return moduleMap;
      } else if ("ratescontract".equalsIgnoreCase(req.getParameter("type"))) {
        moduleMap.put("Parent", "TAR");
        moduleMap.put("Child", "Rates Contract");
        return moduleMap;
      } else if ("ratesquote".equalsIgnoreCase(req.getParameter("type"))) {
        moduleMap.put("Parent", "TAR");
        moduleMap.put("Child", "Rates Quote");
        return moduleMap;
      }
    } else if ("/schedules.do".equalsIgnoreCase(path)
        || "/WEB-INF/jsp/Schedules/SchedulebetweenlocationView.jsp".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "BLS");
      return moduleMap;
    }
    // } else if ("/byport.do".equalsIgnoreCase(path)) { // check this is want or
    // not
    // moduleMap.put("Parent", "BPS");
    // return moduleMap;
    // } else if ("/byvessel.do".equalsIgnoreCase(path)) { // check this is want or
    // not
    // moduleMap.put("Parent", "BVS");
    // return moduleMap;
    // }
    else if ("/ViewBookingAll.do".equalsIgnoreCase(path)) {
      if ("ViewBookingAll".equalsIgnoreCase(req.getParameter("BookingRequestType"))) {
        moduleMap.put("Parent", "BKG");
        moduleMap.put("Child", "Summary");
        return moduleMap;
      } else if ("oblprint".equalsIgnoreCase(req.getParameter("BookingRequestType"))) {
        moduleMap.put("Parent", "BOL");
        moduleMap.put("Child", "Summary");
        return moduleMap;
      }
    } else if ("/ViewEbookingEntry.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "BKG");
      moduleMap.put("Child", "View");
      return moduleMap;
    } else if ("/ebooking_crud.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "BKG");
      moduleMap.put("Child", "Reuse");
      return moduleMap;
    } else if ("/NewBooking.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "BKG");
      moduleMap.put("Child", "Create");
      return moduleMap;
    } else if ("/editamendmentbooking.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "BKG");
      moduleMap.put("Child", "Edit");
      return moduleMap;
    } else if ("/ebookingsaveNew.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "BKG");
      moduleMap.put("Child", "Save");
      return moduleMap;
    } else if ("/croaction.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "CRO");
      return moduleMap;
    } else if ("/BLEditAction.do".equalsIgnoreCase(path)) {
      if ("view".equalsIgnoreCase(req.getParameter("type"))) { // we need to check this type is correct or not
        moduleMap.put("Parent", "BOL");
        moduleMap.put("Child", "Create");
        return moduleMap;
      } else if ("submit".equalsIgnoreCase(req.getParameter("type"))) { // we need to check this type is correct or not
        moduleMap.put("Parent", "BOL");
        moduleMap.put("Child", "Edit");
        return moduleMap;
      }
      return moduleMap;

    } else if ("/ShippingInstruction.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "SHI");
      moduleMap.put("Child", "Summary");
      return moduleMap;
    } else if ("/EditSI.do".equalsIgnoreCase(path)) {
      if ("edit".equalsIgnoreCase(req.getParameter("type"))) { // we need to check this type is correct or not
        moduleMap.put("Parent", "SHI");
        moduleMap.put("Child", "Create");
        return moduleMap;
      } else if ("Draft".equalsIgnoreCase(req.getParameter("type"))) { // we need to check this type is correct or not
        moduleMap.put("Parent", "SHI");
        moduleMap.put("Child", "Edit");
        return moduleMap;
      }
    } else if ("/ecomvgm.do".equalsIgnoreCase(path)) {
      moduleMap.put("Parent", "VGM");
      return moduleMap;
    }
    moduleMap.put("Parent", "Unknown");
    LogUtil.log("AnalyticsFilter moduleFromRequest Path: " + path + " moduleMap: " + moduleMap);
    return moduleMap;

  }

  private static final Map<String, String> WF_START_PATHS = new HashMap<>();
  static {

    // Booking
    WF_START_PATHS.put("/NewBooking.do", "BKG");
    WF_START_PATHS.put("/editamendmentbooking.do", "BKG");
    WF_START_PATHS.put("/ebooking_crud.do", "BKG");

    WF_START_PATHS.put("/EditSI.do", "SHI");
    WF_START_PATHS.put("/BLEditAction.do", "BOL");
    WF_START_PATHS.put("/ecomvgm.do", "VGM");

  }

  // --- Final submit endpoints (mark Completed only here) ---
  private static final Map<String, String> WF_COMPLETE_PATHS = new HashMap<>();
  static {
    WF_COMPLETE_PATHS.put("/ebookingsaveNew.do".toLowerCase(), "BKG");
    WF_COMPLETE_PATHS.put("/amendmentbookingsave.do".toLowerCase(), "BKG");
  }

  private static final Set<String> TRACKED_PREFIXES = new HashSet<>();
  static {

  }

  private static class StatusCapture extends HttpServletResponseWrapper {
    private int status = 200;

    StatusCapture(HttpServletResponse r) {
      super(r);
    }

    @Override
    public void sendError(int sc) throws IOException {
      /* TODO: set status & delegate */ }

    @Override
    public void sendError(int sc, String msg) throws IOException {
      /* TODO */ }

    @Override
    public void setStatus(int sc) {
      /* TODO */ }

    @Override
    public void setStatus(int sc, String sm) {
      /* TODO */ }

    @Override
    public int getStatus() {
      return status;
    }
  }

  // ========= Filter lifecycle =========
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // TODO: any initialization if required
  }

  @Override
  public void destroy() {
    // TODO: any cleanup if required
  }

  // ========= Core filtering =========
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    // --- Casts & basic objects --- 1531451
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    HttpSession session = req.getSession(true);
    final String servletPath = req.getServletPath().toLowerCase();
    final Map<String, String> module = moduleFromRequest(req);
    // --- (A) ANALYTICS: per Core Rule A ---
    String currentModule = (String) session.getAttribute(SK_CURRENT_MODULE);
    Long lastAnId = (Long) session.getAttribute(SK_LAST_ANALYTICS_ID);

    String custCode = (String) session.getAttribute("SESSION_CUSTCODE");
    String countryCode = (String) session.getAttribute("SESSION_USER_COUNTRY");
    LogUtil.log("AnalyticsFilter module: " + module + " custCode: " + custCode + " countryCode: " + countryCode
        + " req.getServletPath(): " + req.getServletPath());
    String enablePlatformAnalytics = (String) request.getServletContext()
        .getAttribute(GeneralConf.Enable_PlatformAnalytics);
    LogUtil.log("AnalyticsFilter enablePlatformAnalytics: " + enablePlatformAnalytics);

    // Skip analytics logic if enablePlatformAnalytics is not "Y"
    if (!"Y".equalsIgnoreCase(enablePlatformAnalytics)) {
      chain.doFilter(request, response);
      return;
    }

    if (module != null && module.get("Parent") != null && !"Unknown".equalsIgnoreCase(module.get("Parent"))
        && (currentModule == null || !currentModule.equals(module.get("Parent")))) {
      EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
      LogUtil.log("AnalyticsFilter Request: " + req + " moduleFromRequest: " + req.getServletPath()
          + " servletPath: " + servletPath + " module: " + module + " session: " + session
          + " currentModule: " + currentModule + " lastAnId: " + lastAnId
          + " module.get(\"Parent\"): " + module.get("Parent"));
      if (lastAnId != null && lastAnId > 0) {
        try {
          ecomAnalyticsObj.closeEndTime(lastAnId); // comment this line for New CR implementation
          LogUtil.log("AnalyticsFilter Analytics closeEndTime() called with ID: " + lastAnId);
        } catch (Exception ignore) {
          LogUtil.logException("AnalyticsFilter Error calling closeEndTime(): " + ignore.getMessage());
        }
        session.removeAttribute(SK_LAST_ANALYTICS_ID);
      }
      long newId = 0;
      try {
        newId = ecomAnalyticsObj.insertStart(countryCode, custCode, module.get("Parent")); // comment this line for New
                                                                                           // CR implementation
        LogUtil.log("AnalyticsFilter Analytics insertStart() called with ID: " + newId + " module.get(\"Parent\"): "
            + module.get("Parent") + " custCode: " + custCode + " countryCode: " + countryCode);
      } catch (Exception ignore) {
        LogUtil.logException("AnalyticsFilter Error calling insertStart(): " + ignore.getMessage());
      }
      session.setAttribute(SK_CURRENT_MODULE, module.get("Parent"));
      session.setAttribute(SK_LAST_ANALYTICS_ID, newId);
      lastAnId = newId;
      // currentModule = module;
    }

    // --- (B) WORKFLOW STATE MACHINE (no timestamps) ---
    // read active workflow (if any)
    System.out.println("WF_START_PATHS: " + WF_START_PATHS + "WF_START_PATHS not null: " + (WF_START_PATHS != null)
        + "WF_START_PATHS containsKey: " + WF_START_PATHS.containsKey(req.getServletPath()));
    if (WF_START_PATHS != null && WF_START_PATHS.containsKey(req.getServletPath())) {

      String startModule = WF_START_PATHS.get(req.getServletPath());
      clearWorkflow(session);
      Long wfId = (Long) session.getAttribute(SK_WF_ID);
      System.out.println("startModule: " + startModule + " wfId: " + wfId);
      if (startModule != null && startModule.length() > 0 && (wfId == null || wfId <= 0)) {
        long newWfId = 0;
        System.out
            .println("Workflow ID is null or less than or equal to 0 That means we have to start the new workflow");
        try {
          EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
          newWfId = ecomAnalyticsObj.insertModuleStatus(custCode, startModule, "S"); // comment this line for New CR
                                                                                     // implementation
        } catch (Exception ignore) {
          System.out.println("Error calling insertModuleStatus(): " + ignore.getMessage());
        }
        session.setAttribute(SK_WF_ID, newWfId);
        session.setAttribute(SK_WF_MODULE, startModule);
        session.setAttribute(SK_WF_STATUS, "S");
        System.out.println(
            "After setting the workflow ID: " + session.getAttribute(SK_WF_ID) + "startModule---->" + startModule);
      }
      if (module.isEmpty()) {
        chain.doFilter(request, response);
        return;
      }
    }
    StatusCapture resw = new StatusCapture(resp);
    try {
      chain.doFilter(request, resw);
    } finally {

    }

  }

  private static void clearWorkflow(HttpSession s) {
    long newWfId = 0;
    s.setAttribute(SK_WF_ID, newWfId);
    s.setAttribute(SK_WF_MODULE, null);
    s.setAttribute(SK_WF_STATUS, null);
  }

  // ========= Session Listener Methods =========
  @Override
  public void sessionCreated(HttpSessionEvent se) {
    // No action needed on session creation
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    // Called when session times out or is invalidated
    HttpSession session = se.getSession();
    String enablePlatformAnalytics = (String) session.getServletContext()
        .getAttribute(GeneralConf.Enable_PlatformAnalytics);
    LogUtil.log("AnalyticsFilter enablePlatformAnalytics from sessionDestroyed(): " + enablePlatformAnalytics);
    if ("Y".equalsIgnoreCase(enablePlatformAnalytics)) {
      closeAnalyticsSession(session);
    }
  }

  // ========= Common Method for Logout/Session Timeout =========
  /**
   * Common method to close analytics session and clear session attributes.
   * Call this method on user logout or session timeout.
   * 
   * @param session The HttpSession to close analytics for
   */
  public static void closeAnalyticsSession(HttpSession session) {
    if (session == null) {
      return;
    }

    try {
      Long lastAnId = (Long) session.getAttribute(SK_LAST_ANALYTICS_ID);
      LogUtil.log("AnalyticsFilter closeAnalyticsSession() called - lastAnId: " + lastAnId);

      if (lastAnId != null && lastAnId > 0) {
        try {
          EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();
          ecomAnalyticsObj.closeEndTime(lastAnId);
          LogUtil.log("AnalyticsFilter Analytics closeEndTime() called successfully on logout/timeout with ID: "
              + lastAnId);
        } catch (Exception e) {
          LogUtil.logException("AnalyticsFilter Error calling closeEndTime() on logout/timeout: " + e.getMessage());
        }
      }

      // Clear all analytics-related session attributes
      session.removeAttribute(SK_LAST_ANALYTICS_ID);
      session.removeAttribute(SK_CURRENT_MODULE);
      clearWorkflow(session);

      LogUtil.log("AnalyticsFilter Analytics session cleared successfully");
    } catch (Exception e) {
      LogUtil.logException("AnalyticsFilter Error in closeAnalyticsSession(): " + e.getMessage());
    }
  }

}
