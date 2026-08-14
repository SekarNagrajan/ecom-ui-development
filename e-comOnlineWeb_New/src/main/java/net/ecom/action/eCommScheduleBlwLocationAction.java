package net.ecom.action;

import infinispan.com.google.common.collect.HashBiMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.formbeans.eCommScheduleBlwLocationForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.common.Validation;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionErrors;
import javax.servlet.ServletContext;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.AjaxSplitter;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.ScheduleAPIService;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.Route;
import net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote;
import com.google.gson.Gson;

public class eCommScheduleBlwLocationAction extends org.apache.struts.action.Action {

    private static String Schdsearch = "";
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    // Changes for analytics purpose by Prakash Mani
    EcomAnalyticsBeanRemote ecomAnalyticsObj = EJBLookUp.getEcomAnalyticsBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String Forward = "success";
        String portPair = null;
        try {
            System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction Inside eCommScheduleBlwLocationAction");
            String loadport = null;
            HashMap cutoffdatemap = null;
            HttpSession session = request.getSession();
            ServletContext context = servlet.getServletContext();
            // Changes for analytics purpose by Prakash Mani
            String custCode = (String) session.getAttribute("SESSION_CUSTCODE");
            Hashtable vessellist = (Hashtable) context.getAttribute("SESSION_VESSELLIST");
            Hashtable portlist = (Hashtable) context.getAttribute("SESSION_PORTLIST");
            Hashtable serlist = (Hashtable) context.getAttribute("SESSION_SERVICELIST");
            String feederRestriction = (String) request.getServletContext()
                    .getAttribute(GeneralConf.FEEDER_RESTRICTION);
            String carriercode = (String) request.getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
            String strdefroute = (String) context.getAttribute(GeneralConf.ENABLE_DEFAULT_ROUTE_ONLY);
            String EnableNonmatchingSchd = (String) context.getAttribute(GeneralConf.ENABLE_NON_MATCHING_SCHD);
            String disablecurrentdateschedule = (String) context
                    .getAttribute(GeneralConf.Disable_Current_Date_Schedule);
            List pluginlis = (List) context.getAttribute("LRP_PLUGIN_DETAILS");
            String userfuritcust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
            ActionErrors err = new ActionErrors();
            String strConfDefDate = GeneralConf.getStrConfDefDate();
            String strDbFormat = GeneralConf.getStrDbFormat();
            eCommScheduleBlwLocationForm ecomObj = (eCommScheduleBlwLocationForm) form;
            String polname = Validation.checkNull(ecomObj.getPolname());
            String podname = Validation.checkNull(ecomObj.getPodname());
            polname = MaskPortUtil.swapmaskporttoactual(polname);
            podname = MaskPortUtil.swapmaskporttoactual(podname);

            String fromdate = Validation.checkNull(ecomObj.getDeptDate());
            String todate = Validation.checkNull(ecomObj.getArrDate());
            String porturl = null, preCarriage = null, onCarriage = null, type = "";
            AjaxSplitter ajx = new AjaxSplitter();
            String pol = polname + "-" + Validation.checkNull((String) portlist.get(polname.toUpperCase()));
            String pod = podname + "-" + Validation.checkNull((String) portlist.get(podname.toUpperCase()));
            request.setAttribute("bwlocpol", pol);
            request.setAttribute("bwlocpod", pod);
            request.setAttribute("bwlocdeptdate", fromdate);
            request.setAttribute("bwlocarrdate", todate);

            // Changes for analytics purpose by Prakash Mani
            String enablePlatformAnalytics = (String) context.getAttribute(GeneralConf.Enable_PlatformAnalytics);
            System.out.println("enablePlatformAnalytics: " + enablePlatformAnalytics);
            System.out.println("ecommmScheduleBlwLocationAction polname===> " + polname + " podname===> " + podname);
            if ("Y".equalsIgnoreCase(enablePlatformAnalytics) && polname != null && podname != null && polname.trim().length() > 0 && podname.trim().length() > 0) {
                long routeId = ecomAnalyticsObj.insertRouteLog(custCode, polname, podname);
                System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction routeId===> " + routeId);
            }
            //end of Changes for analytics purpose by Prakash Mani

            session.setAttribute("bwlocdeptdate", fromdate);
            session.setAttribute("bwlocarrdate", todate);
            List setRouteInfo = new ArrayList();
            List setRut = null;
            Map ecomplugin = new HashMap();
            ecomplugin.put("Enable_Routing_Pol_Pod_Search",
                    context.getAttribute(GeneralConf.Enable_Routing_Pol_Pod_Search));
            ecomplugin.put("Display_LCT_CUTOFF", (String) context.getAttribute(GeneralConf.Display_LCT_CUTOFF));
            ecomplugin.put(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours,
                    (String) context.getAttribute(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours));
            ecomplugin.put(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours,
                    (String) context.getAttribute(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours));
            ecomplugin.put(GeneralConf.Sort_Schedule_Based_On_Depdate,
                    (String) context.getAttribute(GeneralConf.Sort_Schedule_Based_On_Depdate));
            ecomplugin.put(GeneralConf.Refer_Routing_Transtime,
                    (String) context.getAttribute(GeneralConf.Refer_Routing_Transtime));
            ecomplugin.put(GeneralConf.REMOVE_SAME_VESSEL_DETAILS,
                    (String) context.getAttribute(GeneralConf.REMOVE_SAME_VESSEL_DETAILS));
            ecomplugin.put(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED,
                    (String) context.getAttribute(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED));        
            boolean isschdresavbl = false;
            System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction >>err>>" + err);

            if (err != null && err.size() == 0) {
                boolean isMaskPort = MaskPort.isMaskPorts(polname + "," + podname, context);
                if (isMaskPort) {
                    SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
                    SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                    // Schedule design changes
                    Date in_date = source.parse(todate);

                    // Date in_date = DateConversion_New.convertStringToDate(fromdate);
                    Date from_date = source.parse(fromdate);
                    Date currentdate = new Date();
                    boolean isfdateiscdate = false;
                    if (currentdate.compareTo(from_date) == 1) {
                        isfdateiscdate = true;
                    }

                    if ("Y".equalsIgnoreCase(disablecurrentdateschedule) && isfdateiscdate) {
                        fromdate = traget.format(DateConversion_New.convertaddDays(from_date, 1));
                    } else {
                        fromdate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromdate);
                    }

                    todate = traget.format(DateConversion_New.convertaddDays(in_date, 1));
                    // todate =
                    // DateConversion_New.convertDateToString(DateConversion_New.convertaddDays(in_date,
                    // 30));
                    String poll = null;
                    String podd = null, porttype = "N";
                    String origintype = Validation.checkNull(bookingObj.getPortType(polname));
                    String deliverytype = Validation.checkNull(bookingObj.getPortType(podname));
                    if ("I".equalsIgnoreCase(origintype)) {
                        pol = bookingObj.getseaportfrominland(polname);
                        preCarriage = "C";
                        porttype = "Y";
                    } else {
                        pol = polname;
                        preCarriage = "M";
                    }
                    if ("I".equalsIgnoreCase(deliverytype)) {
                        onCarriage = "C";
                        pod = bookingObj.getseaportfrominland(podname);
                        porttype = "Y";
                    } else {
                        pod = podname;
                        onCarriage = "M";
                    }
                    portPair = polname + "#" + podname;
                    List<String> servList = null;
                    Map<String, List<String>> resSrvcMap = (Map<String, List<String>>) context
                            .getAttribute("CACHERESTRICTEDSERVICES");
                    if (resSrvcMap != null) {
                        servList = resSrvcMap.get(portPair);
                        System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction >.servList>>" + servList);
                    }
                    // get feeder service list
                    List<String> feederlis = null;
                    if ("Y".equalsIgnoreCase(feederRestriction) && (!("Y".equalsIgnoreCase(userfuritcust)))) {
                        feederlis = bookingObj.getFeederService();
                    }
                    if ("Y".equalsIgnoreCase(EnableNonmatchingSchd)) {
                        type = "schedule";
                    }
                    
                    // Check if schedule should be fetched from API
                    String fetchScheduleFromAPI = (String) context.getAttribute(GeneralConf.Fetch_Schedule_From_API);
                    Map finalResult = null;
                    
                    if ("Y".equalsIgnoreCase(fetchScheduleFromAPI)) {
                        // Fetch schedule from API
                       // System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction Fetching schedule from API");
                        String commonWSPath = (String) context.getAttribute(GeneralConf.WSURL);
                        ScheduleAPIService scheduleAPIService = new ScheduleAPIService(commonWSPath, feederRestriction);
                        finalResult = scheduleAPIService.getScheduleFromAPI(polname, podname, fromdate, todate);
                    } else {
                        // Use existing EJB method
                     //   System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction Fetching schedule from EJB");
                        finalResult = bookingObj.geteBookRoutingInfo(polname, podname, fromdate, "", "", "", porttype,
                                pol, pod, fromdate, todate, origintype, deliverytype, servList, feederlis, strdefroute,
                                type, ecomplugin);
                    }
                    //
                    //System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction finalResult JSON===>> " + new Gson().toJson(finalResult));
                    List RouteInfo = (List) finalResult.get("actualschedule");
                    List AltrouteInfo = (List) finalResult.get("altchedule");
                    //
                    String cutoffpol = null, cutoffpod = null, cutoffcallid = null, str_type = "N";
                    HashSet callIdSet = new HashSet();
                    if (RouteInfo != null && RouteInfo.size() > 0) {
                        System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction ========Sch=======RouteInfo"
                                + RouteInfo.size());
                        for (int j = 0; j < RouteInfo.size(); j++) {
                            setRut = new ArrayList();
                            ArrayList routeArray = (ArrayList) RouteInfo.get(j);
                            System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction ========Sch=======routeArray"
                                    + RouteInfo.size());
                            str_type = "N";
                            if (routeArray != null && routeArray.size() > 0) {
                                int routearr_len = routeArray.size();
                                String pol_str = null, pod_str = null;
                                String temp = null;
                                ArrayList ars = ajx.FindLordporttype(routeArray);
                                for (int i = 0; i < routearr_len; i++) {
                                    Route routeDTO = null;
                                    routeDTO = (Route) routeArray.get(i);
                                    if (routeDTO != null) {
                                        temp = "N";
                                        pol_str = (String) ars.get(0);
                                        pod_str = (String) ars.get(1);
                                        loadport = routeDTO.getLoadport();
                                        System.out.println(
                                                "[ECOM-WAR] eCommScheduleBlwLocationAction =======routeDTO.getLoadport()"
                                                        + routeDTO.getLoadport());
                                        String lordporttype = Validation
                                                .checkNull(bookingObj.getPortType(routeDTO.getLoadport()));
                                        String disporttype = Validation
                                                .checkNull(bookingObj.getPortType(routeDTO.getDisport()));
                                        if ("I".equalsIgnoreCase(lordporttype)) {
                                            preCarriage = "C";
                                            routeDTO.setLoadtype("");
                                            routeDTO.setDisctype("");
                                            temp = "Y";
                                        } else {
                                            preCarriage = "M";
                                            if (pol_str.equalsIgnoreCase(routeDTO.getLoadport())) {
                                                routeDTO.setLoadtype("L");
                                            } else {
                                                routeDTO.setLoadtype("T");
                                            }
                                        }
                                        if (!"Y".equalsIgnoreCase(temp)) {
                                            if ("I".equalsIgnoreCase(disporttype)) {
                                                onCarriage = "C";
                                                routeDTO.setLoadtype("");
                                                routeDTO.setDisctype("");
                                            } else {
                                                onCarriage = "M";
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
                                        routeDTO.setPreCarriage(preCarriage);
                                        routeDTO.setOnCarriage(onCarriage);
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
                                        isschdresavbl = true;
                                        if ("N".equalsIgnoreCase(str_type)
                                                && ("M".equalsIgnoreCase(routeDTO.getLegType())
                                                        || "F".equalsIgnoreCase(routeDTO.getLegType()))) {
                                            str_type = "Y";
                                            cutoffpol = routeDTO.getLoadport();
                                            callIdSet.add(routeDTO.getGrpCallId());
                                        }

                                        if (routeDTO.getService() != null
                                                && routeDTO.getService().trim().length() > 0) {
                                            cutoffpod = routeDTO.getDisport();
                                        }
                                    }
                                }
                            }
                            setRouteInfo.add(setRut);
                        }
                        System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction pluginlis===> " + pluginlis);
                        if (pluginlis != null && pluginlis.contains("BEC")) {
                            Iterator itr = callIdSet.iterator();
                            List cutlis = null;
                            Map cutmap = null;
                            cutoffdatemap = new HashMap();
                            CutoffConfigVO cutOffVO = null;
                            while (itr.hasNext()) {
                                cutoffcallid = Validation.checkNull(itr.next().toString());
                                cutlis = bookingObj.getCutOffConfigDetails(cutoffcallid, cutoffpol, cutoffpod);
                                if (cutlis != null && cutlis.size() > 0) {
                                    cutmap = (Map) cutlis.get(0);
                                    System.out.println(
                                            "[ECOM-WAR] eCommScheduleBlwLocationAction cutmap====> " + cutmap.size());
                                    if (cutmap != null && cutmap.size() > 0) {
                                        cutOffVO = new CutoffConfigVO();
                                        if (cutmap.containsKey("Booking Cutoff")) {
                                            cutOffVO.setBkgcutoff(Validation
                                                    .checkNull(DateConversion_New.convertDefaultFormatwithTime(
                                                            cutmap.get("Booking Cutoff").toString().trim())));
                                        }
                                        if (cutmap.containsKey("Documentation Cutoff")) {
                                            cutOffVO.setDoccutoff(Validation
                                                    .checkNull(DateConversion_New.convertDefaultFormatwithTime(
                                                            cutmap.get("Documentation Cutoff").toString().trim())));
                                        }
                                        if (cutmap.containsKey("VGM Cutoff")) {
                                            cutOffVO.setVgmcutoff(Validation
                                                    .checkNull(DateConversion_New.convertDefaultFormatwithTime(
                                                            cutmap.get("VGM Cutoff").toString().trim())));
                                        }
                                        if (cutmap.containsKey("BBRORO Cutoff")) {
                                            cutOffVO.setBbrorocutoff(Validation
                                                    .checkNull(DateConversion_New.convertDefaultFormatwithTime(
                                                            cutmap.get("BBRORO Cutoff").toString().trim())));
                                        }
                                        if (cutmap.containsKey("Hazmat Cutoff")) {
                                            cutOffVO.setHazcutoff(Validation
                                                    .checkNull(DateConversion_New.convertDefaultFormatwithTime(
                                                            cutmap.get("Hazmat Cutoff").toString().trim())));
                                        }
                                        if (cutmap.containsKey("Port Entry Cutoff")) {
                                            cutOffVO.setPortentrycutoff(Validation
                                                    .checkNull(DateConversion_New.convertDefaultFormatwithTime(
                                                            cutmap.get("Port Entry Cutoff").toString().trim())));
                                        }
                                        cutoffdatemap.put(cutoffcallid, cutOffVO);
                                    }
                                }
                            }
                        } else {
                            System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction loadport==> " + cutoffpol);
                            cutoffdatemap = ecomCntrlObj.getRoutingCutOffDetailsFromPort(cutoffpol);
                        }
                    }

                    request.setAttribute("SCHD", setRouteInfo);
                    Forward = "success";

                    Schdsearch = "SchdByBlwLocation";
                    request.setAttribute("SCHDSEARCH", Schdsearch);
                    request.setAttribute("SCHD", setRouteInfo);

                    request.setAttribute("RoutingCutOffDetailsFromPort", cutoffdatemap);

                    Forward = "success";
                    if (!isschdresavbl) {
                        request.setAttribute("ALTSCHEDULE", AltrouteInfo);

                        Forward = "nonmatchingschedule";
                    }
                } else {
                    // request.setAttribute("ALTSCHEDULE", null);

                    Forward = "nonmatchingschedule";
                }
            }
            return mapping.findForward(Forward);
        } catch (Exception e) {
            Forward = "failure";
            Schdsearch = "SchdByBlwLocation";
            request.setAttribute("SCHDSEARCH", Schdsearch);
            request.setAttribute("Error_report", EcomConsts.WS_ERROR_MESSAGE);
            System.out.println("[ECOM-WAR] eCommScheduleBlwLocationAction ===>>>" + e);
        }

        System.out.println(
                "Forward ======> " + Forward);
        return mapping.findForward(Forward);
    }
}
