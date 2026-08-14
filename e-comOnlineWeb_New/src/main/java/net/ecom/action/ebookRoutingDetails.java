package net.ecom.action;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.AjaxSplitter;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.common.utils.ScheduleAPIService;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.Route;
import net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote;

public class ebookRoutingDetails extends HttpServlet {

    ServletContext contexts;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    // Changes for analytics purpose by Prakash Mani
    EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            HttpSession session = request.getSession();
            //
            // Changes for analytics purpose by Prakash Mani
            String custCode = (String) session.getAttribute("SESSION_CUSTCODE");
            Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
            Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
            String strDateFormat = (String) session.getAttribute("SESSION_DATEFROMAT");
            String strdefroute = (String) contexts.getAttribute(GeneralConf.ENABLE_DEFAULT_ROUTE_ONLY);
            String feederRestriction = (String) request.getServletContext()
                    .getAttribute(GeneralConf.FEEDER_RESTRICTION);
            String disablecurrentdateschedule = (String) contexts
                    .getAttribute(GeneralConf.Disable_Current_Date_Schedule);
            String userfuritcust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
            //
            String strConfDefDate = GeneralConf.getStrConfDefDate();
            String strDbFormat = GeneralConf.getStrDbFormat();
            String fromdate = "", todate = "", origin_desc = "", delivery_desc = "", gateInDate = "", portPair = null;
            String origin = "", delivery = "", preCarriage = "", onCarriage = "", pol = null, pod = null,
                    porttype = "N";
            AjaxSplitter ajx = new AjaxSplitter();
            Map ecomplugin = new HashMap();
            ecomplugin.put("Enable_Routing_Pol_Pod_Search",
                    contexts.getAttribute(GeneralConf.Enable_Routing_Pol_Pod_Search));
            ecomplugin.put("Display_LCT_CUTOFF", (String) contexts.getAttribute(GeneralConf.Display_LCT_CUTOFF));
            ecomplugin.put(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours,
                    (String) contexts.getAttribute(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours));
            ecomplugin.put(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours,
                    (String) contexts.getAttribute(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours));
            ecomplugin.put(GeneralConf.Sort_Schedule_Based_On_Depdate,
                    (String) contexts.getAttribute(GeneralConf.Sort_Schedule_Based_On_Depdate));
            ecomplugin.put(GeneralConf.Refer_Routing_Transtime,
                    (String) contexts.getAttribute(GeneralConf.Refer_Routing_Transtime));
            ecomplugin.put(GeneralConf.REMOVE_SAME_VESSEL_DETAILS,
                    (String) contexts.getAttribute(GeneralConf.REMOVE_SAME_VESSEL_DETAILS));
            ecomplugin.put(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED,
                    (String) contexts.getAttribute(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED));
            //
            SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
            SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
            //
            gateInDate = request.getParameter("indate");
            gateInDate = (gateInDate != null && (!"".equalsIgnoreCase(gateInDate))) ? gateInDate.trim() : "";
            if (gateInDate.length() > 0) {
                try {
                    Date in_date = source.parse(gateInDate);
                    System.out.println("[ECOM-WAR] ebookRoutingDetails ==========>>>in_date" + in_date);
                    Date currentdate = new Date();
                    boolean isfdateiscdate = false;
                    if (currentdate.compareTo(in_date) == 1) {
                        isfdateiscdate = true;
                    }
                    if ("Y".equalsIgnoreCase(disablecurrentdateschedule)) {
                        if (isfdateiscdate) {
                            fromdate = traget.format(DateConversion_New.convertaddDays(in_date, 1));
                        } else {
                            fromdate = traget.format(in_date);
                        }
                    } else {
                        fromdate = traget.format(DateConversion_New.convertsubDays(in_date, 4));
                    }
                    String scheduleDateRangeDaysStr = (String) contexts.getAttribute(GeneralConf.Schedule_Date_Range_Days);
                    int scheduleDateRangeDays = 45;
                    if (scheduleDateRangeDaysStr != null && scheduleDateRangeDaysStr.trim().length() > 0) {
                        try {
                            scheduleDateRangeDays = Integer.parseInt(scheduleDateRangeDaysStr.trim());
                        } catch (NumberFormatException nfe) {
                            scheduleDateRangeDays = 45;
                        }
                    }
                    Date to_date = source.parse(gateInDate);
                    todate = traget.format(DateConversion_New.convertaddDays(to_date, scheduleDateRangeDays));
                    gateInDate = DateConversion_New.convertDefaultDBFormat(gateInDate);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("[ECOM-WAR] ebookRoutingDetails ==========>>>After fromdate" + fromdate
                    + " ==>>>todatevalue" + todate);
            //
            Date date2 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today_date = sdf.format(date2);
            Date date1 = sdf.parse(fromdate);
            date2 = sdf.parse(today_date);
            int range = date1.compareTo(date2);
            //
            if (range == -1) {
                fromdate = today_date;
            }
            origin_desc = Validation.checkNull(request.getParameter("origin_desc"));
            delivery_desc = Validation.checkNull(request.getParameter("delivery_desc"));
            System.out.println(
                    "[ECOM-WAR] ebookRoutingDetails ==>origin_desc" + origin_desc + "==>delivery_desc" + delivery_desc);

            // Changes for analytics purpose by Prakash Mani
            String enablePlatformAnalytics = (String) contexts.getAttribute(GeneralConf.Enable_PlatformAnalytics);
            System.out.println("enablePlatformAnalytics: " + enablePlatformAnalytics);
            System.out.println("ebookRoutingDetails origin===> " + origin + " delivery===> " + delivery);
            if ("Y".equalsIgnoreCase(enablePlatformAnalytics) && origin != null && delivery != null
                    && origin.trim().length() > 0 && delivery.trim().length() > 0) {
                long routeId = ecomAnalyticsObj.insertRouteLog(custCode, origin, delivery);
                System.out.println("[ECOM-WAR] ebookRoutingDetails routeId===> " + routeId);
            }

            // end of Changes for analytics purpose by Prakash Mani
            if (origin_desc.contains("-")) {
                origin = origin_desc.substring(0, origin_desc.indexOf('-')).trim();
                origin_desc = origin_desc.substring(origin_desc.indexOf('-') + 1, origin_desc.length()).trim();
            }
            if (delivery_desc.contains("-")) {
                delivery = delivery_desc.substring(0, delivery_desc.indexOf('-')).trim();
                delivery_desc = delivery_desc.substring(delivery_desc.indexOf('-') + 1, delivery_desc.length()).trim();
            }

            // Swap mask port to actual port for origin and delivery to get the schedule
            String actualOrigin = MaskPortUtil.swapmaskporttoactual(origin);
            String actualDelivery = MaskPortUtil.swapmaskporttoactual(delivery);
            System.out.println("[ECOM-WAR] ebookRoutingDetails origin: " + origin + " -> actualOrigin: " + actualOrigin
                    + ", delivery: " + delivery + " -> actualDelivery: " + actualDelivery);

            // Use actual ports for schedule lookup
            origin = actualOrigin;
            delivery = actualDelivery;

            boolean isMaskPort = MaskPort.isMaskPorts(origin + "," + delivery, contexts);
            if (isMaskPort) {
                String origintype = Validation.checkNull(bookingObj.getPortType(origin));
                String deliverytype = Validation.checkNull(bookingObj.getPortType(delivery));
                if ("I".equalsIgnoreCase(origintype)) {
                    pol = bookingObj.getseaportfrominland(origin);
                    preCarriage = "C";
                    porttype = "Y";
                } else {
                    pol = origin;
                    preCarriage = "M";
                }
                if ("I".equalsIgnoreCase(deliverytype)) {
                    onCarriage = "C";
                    pod = bookingObj.getseaportfrominland(delivery);
                    porttype = "Y";
                } else {
                    pod = delivery;
                    onCarriage = "M";
                }
                portPair = origin + "#" + delivery;
                List<String> servList = null;
                Map<String, List<String>> resSrvcMap = (Map<String, List<String>>) contexts
                        .getAttribute("CACHERESTRICTEDSERVICES");
                if (resSrvcMap != null) {
                    servList = resSrvcMap.get(portPair);
                    System.out.println("[ECOM-WAR] ebookRoutingDetails >.servList>>" + servList);
                }

                // get feeder service list
                List<String> feederlis = null;
                if ("Y".equalsIgnoreCase(feederRestriction) && (!("Y".equalsIgnoreCase(userfuritcust)))) {
                    feederlis = bookingObj.getFeederService();
                }
                //
                List setRouteInfo = new ArrayList();
                
                // Check if schedule should be fetched from API
                String fetchScheduleFromAPI = (String) contexts.getAttribute(GeneralConf.Fetch_Schedule_From_API);
                Map finalResult = null;
                
                if ("Y".equalsIgnoreCase(fetchScheduleFromAPI)) {
                    // Fetch schedule from API
                    System.out.println("[ECOM-WAR] ebookRoutingDetails Fetching schedule from API");
                    String commonWSPath = (String) contexts.getAttribute(GeneralConf.WSURL);
                    ScheduleAPIService scheduleAPIService = new ScheduleAPIService(commonWSPath, feederRestriction);
                    finalResult = scheduleAPIService.getScheduleFromAPI(origin, delivery, fromdate, todate);
                } else {
                    // Use existing EJB method
                    System.out.println("[ECOM-WAR] ebookRoutingDetails Fetching schedule from EJB");
                    finalResult = bookingObj.geteBookRoutingInfo(origin, delivery, gateInDate, preCarriage, onCarriage,
                            strDateFormat, porttype, pol, pod, fromdate, todate, origintype, deliverytype, servList,
                            feederlis, strdefroute, "booking", ecomplugin);
                }
                
                System.out.println("[ECOM-WAR] ebookRoutingDetails finalResult===>>   " + finalResult);
                List RouteInfo = (List) finalResult.get("actualschedule");
                // List AltrouteInfo = (List) finalResult.get("altchedule");
                //

                if (RouteInfo != null && RouteInfo.size() > 0) {
                    for (int j = 0; j < RouteInfo.size(); j++) {
                        List setRut = new ArrayList();
                        ArrayList routeArray = (ArrayList) RouteInfo.get(j);
                        if (routeArray != null && routeArray.size() > 0) {
                            int routearr_len = routeArray.size();
                            String pol_str = null, pod_str = null;
                            String temp = null;
                            ArrayList ars = ajx.FindLordporttype(routeArray);
                            for (int i = 0; i < routearr_len; i++) {
                                Route routeDTO = (Route) routeArray.get(i);
                                if (routeDTO != null) {
                                    temp = "N";
                                    pol_str = (String) ars.get(0);
                                    pod_str = (String) ars.get(1);
                                    String lordporttype = Validation
                                            .checkNull(bookingObj.getPortType(routeDTO.getLoadport()));
                                    String disporttype = Validation
                                            .checkNull(bookingObj.getPortType(routeDTO.getDisport()));
                                    if ("I".equalsIgnoreCase(lordporttype)) {
                                        // preCarriage = "C";
                                        routeDTO.setLoadtype("");
                                        routeDTO.setDisctype("");
                                        temp = "Y";
                                    } else {
                                        // preCarriage = "M";
                                        if (pol_str.equalsIgnoreCase(routeDTO.getLoadport())) {
                                            routeDTO.setLoadtype("L");
                                        } else {
                                            routeDTO.setLoadtype("T");
                                        }
                                    }
                                    if (!"Y".equalsIgnoreCase(temp)) {
                                        if ("I".equalsIgnoreCase(disporttype)) {
                                            // onCarriage = "C";
                                            routeDTO.setLoadtype("");
                                            routeDTO.setDisctype("");
                                        } else {
                                            // onCarriage = "M";
                                            if (pod_str.equalsIgnoreCase(routeDTO.getDisport())) {
                                                routeDTO.setDisctype("L");
                                            } else {
                                                routeDTO.setDisctype("T");
                                            }
                                        }
                                    }
                                    if ("F".equalsIgnoreCase(routeDTO.getLegType())) {
                                        routeDTO.setLegtypename(EcomConsts.LEG_TYPE_FEEDER);
                                    } else if ("M".equalsIgnoreCase(routeDTO.getLegType())) {
                                        routeDTO.setLegtypename(EcomConsts.LEG_TYPE_MAINLINE);
                                    } else if ("R".equalsIgnoreCase(routeDTO.getLegType())) {
                                        routeDTO.setLegtypename(EcomConsts.LEG_TYPE_RAIL);
                                    } else if ("T".equalsIgnoreCase(routeDTO.getLegType())) {
                                        routeDTO.setLegtypename(EcomConsts.LEG_TYPE_ROAD);
                                    } else if ("B".equalsIgnoreCase(routeDTO.getLegType())) {
                                        routeDTO.setLegtypename(EcomConsts.LEG_TYPE_BARGE);
                                    } else if ("C".equalsIgnoreCase(routeDTO.getLegType())) {
                                        routeDTO.setLegtypename(EcomConsts.LEG_TYPE_COMBINED);
                                    } else if ("I".equalsIgnoreCase(routeDTO.getLegType())) {
                                        routeDTO.setLegtypename(EcomConsts.LEG_TYPE_ITT);
                                    }
                                    // routeDTO.setPreCarriage(preCarriage);
                                    // routeDTO.setOnCarriage(onCarriage);

                                    routeDTO.setLordporttype(lordporttype);
                                    routeDTO.setDisporttype(disporttype);
                                    routeDTO.setPolPortName((String) portlist
                                            .get(Validation.checkNull(routeDTO.getLoadport()).toUpperCase()));
                                    routeDTO.setPodPortName((String) portlist
                                            .get(Validation.checkNull(routeDTO.getDisport()).toUpperCase()));
                                    routeDTO.setServiceName((String) serlist
                                            .get(Validation.checkNull(routeDTO.getService()).toUpperCase()));
                                    routeDTO.setVesselName((String) vessellist
                                            .get(Validation.checkNull(routeDTO.getVessel()).toUpperCase()));
                                    routeDTO.setETA1(
                                            DateConversion_New.convertDefaultFormatwithTime(routeDTO.getETA1()));
                                    routeDTO.setETD1(
                                            DateConversion_New.convertDefaultFormatwithTime(routeDTO.getETD1()));
                                    setRut.add(routeDTO);
                                }

                            }

                        }

                        setRouteInfo.add(setRut);
                    }
                    // for (int j = 0; j < RouteInfo.size(); j++) {
                    // ArrayList routeArray = (ArrayList) RouteInfo.get(j);
                    // if (routeArray != null && routeArray.size() > 0) {
                    // int routearr_len = routeArray.size();
                    // for (int i = 0; i < routearr_len; i++) {
                    // Route routeDTO = (Route) routeArray.get(i);
                    // System.out.println("[ECOM-WAR] ebookRoutingDetails
                    // =======>routeDTOgetService"+routeDTO.getService());
                    // }
                    // }
                    // }
                }
                session.setAttribute("EBOOKING_ROUTING_INFO", setRouteInfo);
            } else {
                session.setAttribute("EBOOKING_ROUTING_INFO", null);
            }
            RequestDispatcher rd = request.getRequestDispatcher("/eBookingRouteDetails.jsp");
            rd.forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
