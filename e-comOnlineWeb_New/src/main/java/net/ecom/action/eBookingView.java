/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.RemoveDuplicateValues;
import net.ecom.common.UserTimeZoneConversion;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.EbookingViewAll;
import net.ecom.vo.OBLPrintVo;
import net.ecom.vo.Route;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

public class eBookingView extends org.apache.struts.action.Action {

    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBLBeanRemote ecombean = EJBLookUp.getBLBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomV1BookingBeanRemote v1BkgObj = EJBLookUp.getV1BKGBeanLookUp();
    Map stateMap = null, stateMapCountry = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String mapping_string = "viewAll";
        RemoveDuplicateValues removeDup = new RemoveDuplicateValues();

        try {
            HttpSession session = request.getSession();
            ServletContext contexts = servlet.getServletContext();
            String isPasswordExpired = Validation.checkNull((String) session.getAttribute("isPasswordExpired"));
            if ("Y".equalsIgnoreCase(isPasswordExpired)) {
                response.sendRedirect("Accesslogin.do?type=reset");
            }
            saveToken(request);
            if (session.getAttribute("STRIPE_CSRF_TOKEN") == null) {
                session.setAttribute("STRIPE_CSRF_TOKEN", UUID.randomUUID().toString());
            }
            if (session.getAttribute("NGENIUS_CSRF_TOKEN") == null) {
                session.setAttribute("NGENIUS_CSRF_TOKEN", UUID.randomUUID().toString());
            }
            List pluginlis = (List) contexts.getAttribute("LRP_PLUGIN_DETAILS");
            boolean chkbkgcutoff = false;
            List cutlis = null;
            Map cutmap = null;
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            System.out.println("[ECOM-WAR] eBookingView code in session--->" + userCode);
            Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
            String requestType = Validation.checkNull(request.getParameter("BookingRequestType"));
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            session.setAttribute("IS_ACTIVEPAGE", "Booking");
            String toEnableTimeZone = (String) contexts.getAttribute(GeneralConf.EnableTimeZone);
            System.out.println("[ECOM-WAR] eBookingView ENABLETIMEZONEVAlue " + toEnableTimeZone + "//" + requestType);
            String incompletedReg = Validation.checkNull((String) session.getAttribute("SESSION_INCOMPLETED_REG"));
            String allowIncompletedRegToLogin = Validation.checkNull((String) contexts.getAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN));
            String preferredTimeZone = Validation.checkNull((String) session.getAttribute("USERPREFERREDTIMEZONE"));
            System.out.println("[ECOM-WAR] eBookingView preferredtimezoneValue" + preferredTimeZone);
            Map<String, String> versionUTCMap = (Map<String, String>) contexts.getAttribute(GeneralConf.versionUTCValue);
            System.out.println("[ECOM-WAR] eBookingView versionUTCMapValue" + versionUTCMap);
            UserTimeZoneConversion obj = new UserTimeZoneConversion();

            String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
            String strMenu = "YES";
            if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                strMenu = "NO";
            } else {
                session.setAttribute("systemid", session.getId());
            }
            session.setAttribute("IS_MENU_LOCK", strMenu);
            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
            ArrayList carrierContacts = bookingObj.getTransportService();
            if (carrierContacts != null && carrierContacts.size() == 2) {
                session.setAttribute("CARRIERCONTACTS", LabelBeanConversion.labelListConversion((List<DropDownVO>) carrierContacts.get(0)));
                session.setAttribute("CARRIERCONTACTSMAP", carrierContacts.get(1));
            }
            //Map<String, String> emptyPickUpMap = locobj.getEmptyPickUpMap();
            //session.setAttribute("EMPTYPICKUPMAP", emptyPickUpMap);
            if ("ViewBookingAll".equalsIgnoreCase(requestType)) {
                ArrayList view = new ArrayList();
                if("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedReg)) {
                    System.out.println("[ECOM-WAR] eBookingView incompletedReg: " + incompletedReg);
                } else { 
                    view = bookingObj.getBookingDetail(userCode, "", "", "A", custTypeConfig, v1DataEnableStatus);
                }
                //
                session.setAttribute("NEWDESIGNPRODUCT", "booking");

                mapping_string = "success";
                if (view != null && view.size() > 0) {
                    int len = view.size();
                    for (int i = 0; i < len; i++) {
                        EbookingViewAll eBooking = (EbookingViewAll) view.get(i);
                        if (portlist.containsKey(Validation.checkNull(eBooking.getOriginPortId()).toUpperCase())) {
                            eBooking.setOriginPortText(eBooking.getOriginPortId() + "-" + Validation.checkNull((String) portlist.get(eBooking.getOriginPortId().toUpperCase())));
                        }
                        if (portlist.containsKey(Validation.checkNull(eBooking.getFinalPortId()).toUpperCase())) {
                            eBooking.setFinalPortText(eBooking.getFinalPortId() + "-" + Validation.checkNull((String) portlist.get(eBooking.getFinalPortId().toUpperCase())));
                        }
                        if (portlist.containsKey(Validation.checkNull(eBooking.getLoadPortID()).toUpperCase())) {
                            eBooking.setLoadPortText(eBooking.getLoadPortID() + "-" + Validation.checkNull((String) portlist.get(eBooking.getLoadPortID().toUpperCase())));
                        }
                        if (portlist.containsKey(Validation.checkNull(eBooking.getDischargePortId()).toUpperCase())) {
                            eBooking.setDischargePortText(eBooking.getDischargePortId() + "-" + Validation.checkNull((String) portlist.get(eBooking.getDischargePortId().toUpperCase())));
                        }

                        if (eBooking.getBookingDate().length() > 0) {
                            eBooking.setBookingDate(DateConversion_New.convertDefaultTimeFormat(eBooking.getBookingDate()));
                        }
                        if (Validation.checkNull(eBooking.getPoletd()).length() > 0) {
                            eBooking.setPoletd(DateConversion_New.convertDefaultTimeFormat(eBooking.getPoletd()));
                        }

                        //timezone
                        System.out.println("[ECOM-WAR] eBookingView crdate" + eBooking.getCrdate());

                        //CREATEDDATE
                        if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                            if (eBooking.getCrdate().length() > 0) {

                                String date1 = eBooking.getCrdate();
                                Date resulttt = null;
                                SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());
                                resulttt = targettt.parse(date1);
                                System.out.println("[ECOM-WAR] eBookingView StringtoDate" + resulttt);
                                String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                                System.out.println("[ECOM-WAR] eBookingView userTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                                System.out.println("[ECOM-WAR] eBookingView bookingcreated date before conversion" + eBooking.getCrdate());
                                if (userTimeZoneConverted.length() > 0) {
                                    eBooking.setCrdate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                                }
                            }
                        } else {
                            if (eBooking.getCrdate().length() > 0) {
                                eBooking.setCrdate(DateConversion_New.convertDefaultTimeFormat(eBooking.getCrdate()));
                            }
                        }
                        System.out.println("[ECOM-WAR] eBookingView bookingcreated date after conversion" + eBooking.getCrdate());
                        //SUBMITTED DATE
                        if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                            if (eBooking.getSubmitdate().length() > 0) {

                                String date1 = eBooking.getSubmitdate();
                                Date resulttt = null;
                                SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());
                                resulttt = targettt.parse(date1);
                                System.out.println("[ECOM-WAR] eBookingView StringtoDate" + resulttt);
                                String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                                System.out.println("[ECOM-WAR] eBookingView userTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                                System.out.println("[ECOM-WAR] eBookingView bookingSUBMITTED date before conversion" + eBooking.getSubmitdate());
                                if (userTimeZoneConverted.length() > 0) {
                                    eBooking.setSubmitdate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                                }
                            }
                        } else {
                            if (eBooking.getSubmitdate().length() > 0) {
                                eBooking.setSubmitdate(DateConversion_New.convertDefaultTimeFormat(eBooking.getSubmitdate()));
                            }
                        }
                        System.out.println("[ECOM-WAR] eBookingView bookingSUBMITTED date after conversion" + eBooking.getSubmitdate());

                        //confirmed DATE
                        if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                            if (eBooking.getConfirmedDate().length() > 0) {

                                String date1 = eBooking.getConfirmedDate();
                                Date resulttt = null;
                                SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());
                                resulttt = targettt.parse(date1);
                                System.out.println("[ECOM-WAR] eBookingView StringtoDate" + resulttt);
                                String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                                System.out.println("[ECOM-WAR] eBookingView userTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                                System.out.println("[ECOM-WAR] eBookingView bookingconfirmed date before conversion" + eBooking.getConfirmedDate());
                                if (userTimeZoneConverted.length() > 0) {
                                    eBooking.setConfirmedDate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                                }
                            }
                        } else {

                            if ("C".equalsIgnoreCase(eBooking.getStatus())) {
                                eBooking.setConfirmedDate(Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(eBooking.getConfirmedDate())));
                            } else {
                                eBooking.setConfirmedDate("");
                            }
                        }
                        System.out.println("[ECOM-WAR] eBookingView bookingSUBMITTED date after conversion" + eBooking.getSubmitdate());

                        view.set(i, eBooking);
                        eBooking = null;
                    }
                }
                session.setAttribute("BookingView", view);

                //Get Transport terms
            } else if ("oblprint".equalsIgnoreCase(requestType)) {
                session.setAttribute("NEWDESIGNPRODUCT", "oblprint");
                List<OBLPrintVo> oblview = null;
                try {
                    oblview = ecombean.getBLSummary(userCode, custTypeConfig, v1DataEnableStatus, "N");
                    //TIMEZONE BL
                    //BL CREATEDATE

                    oblview.forEach(vo -> {
                        try {
                            if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                                if (vo.getCrdate().length() > 0) {
                                    String date1 = vo.getCrdate();
                                    Date resulttt = null;
                                    SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());

                                    resulttt = targettt.parse(date1);

                                    System.out.println("[ECOM-WAR] eBookingView StringtoDate" + resulttt);
                                    String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                                    System.out.println("[ECOM-WAR] eBookingView userTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                                    System.out.println("[ECOM-WAR] eBookingView BLcreated date before conversion" + vo.getCrdate());
                                    if (userTimeZoneConverted.length() > 0) {
                                        vo.setCrdate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                                    }
                                    System.out.println("[ECOM-WAR] eBookingView blcreated date after conversion" + vo.getCrdate());

                                    if ("C".equalsIgnoreCase(vo.getStatus()) || "I".equalsIgnoreCase(vo.getStatus())) {
                                        String date11 = vo.getConfirmedDate();
                                        Date resultt = null;
                                        SimpleDateFormat targett = new SimpleDateFormat(GeneralConf.getStrDbFormat());

                                        resultt = targett.parse(date11);

                                        System.out.println("[ECOM-WAR] eBookingView StringtoDate" + resultt);
                                        String userTimeZoneConvertedbl = obj.getUserDateFormat(resultt, preferredTimeZone, versionUTCMap);
                                        System.out.println("[ECOM-WAR] eBookingView userTimeZoneConvertedfromejb" + userTimeZoneConvertedbl);

//
                                        System.out.println("[ECOM-WAR] eBookingView BLconfirmed date before conversion" + vo.getCrdate());
                                        if (userTimeZoneConvertedbl.length() > 0) {
                                            vo.setConfirmedDate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConvertedbl));
                                        }

                                        System.out.println("[ECOM-WAR] eBookingView blconfirmed date after conversion" + vo.getConfirmedDate());

                                    } else {
                                        vo.setConfirmedDate("");
                                        // vo.setCrdate("");
                                    }
                                }
                            } else {

                                vo.setCrdate(Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(vo.getCrdate())));

                                if ("C".equalsIgnoreCase(vo.getStatus()) || "I".equalsIgnoreCase(vo.getStatus())) {
                                    vo.setConfirmedDate(Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(vo.getConfirmedDate())));

                                } else {
                                    vo.setConfirmedDate("");
                                    // vo.setCrdate("");
                                }

                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(eBookingView.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NamingException ex) {
                            Logger.getLogger(eBookingView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                session.setAttribute("oblprintView", oblview);
                mapping_string = "oblprint";
            } else if ("booknow".equalsIgnoreCase(requestType)) {
                ArrayList routeDetails = null;
                ArrayList setrouteDetails = null;
                String cutOffPort = request.getParameter("cutoffport");
                String cutOffTerminal = request.getParameter("cutoffterminal");
                String cutOffDate = request.getParameter("cutoffdate");
                String service = request.getParameter("service");
                String vessel = request.getParameter("vessel");
                String voyage = request.getParameter("voyage");
                String bound = request.getParameter("bound");
                String svcType = request.getParameter("svcType");
                String depdate = request.getParameter("depdate");
                String pol = request.getParameter("pol");
                String pod = request.getParameter("pod");
                String grpcallid = request.getParameter("grpcallid");
                String hidVessel = request.getParameter("hidINVessel");
                String hidETDDate = request.getParameter("hidETDDate");
                String hidServiceCode = request.getParameter("hidINServiceCode");
                request.setAttribute("hidVessel", hidVessel);
                request.setAttribute("hidETDDate", hidETDDate);
                request.setAttribute("hidServiceCode", hidServiceCode);
                System.out.println("[ECOM-WAR] eBookingView cutOffPort>>" + cutOffPort);
                CutoffConfigVO cutOffVO = null;
                System.out.println("[ECOM-WAR] eBookingView pluginlis " + pluginlis);
                if (pluginlis != null && pluginlis.contains("BEC")) {
                    chkbkgcutoff = true;
                    cutlis = bookingObj.getCutOffConfigDetails(grpcallid, pol, pod);
                    System.out.println("[ECOM-WAR] eBookingView cutlis " + cutlis);
                    if (cutlis != null && cutlis.size() > 0) {
                        cutmap = (Map) cutlis.get(0);
                        System.out.println("[ECOM-WAR] eBookingView cutmap " + cutmap.size());
                        if (cutmap != null && cutmap.size() > 0) {
                            cutOffVO = new CutoffConfigVO();
                            if (cutmap.containsKey("Booking Cutoff")) {
                                cutOffVO.setBkgcutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Booking Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("Documentation Cutoff")) {
                                cutOffVO.setDoccutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Documentation Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("VGM Cutoff")) {
                                cutOffVO.setVgmcutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("VGM Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("BBRORO Cutoff")) {
                                cutOffVO.setBbrorocutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("BBRORO Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("Hazmat Cutoff")) {
                                cutOffVO.setHazcutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Hazmat Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("Port Entry Cutoff")) {
                                cutOffVO.setPortentrycutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Port Entry Cutoff").toString().trim())));
                            }
                        }
                    }
                }
                if (!chkbkgcutoff) {
                    cutOffVO = locobj.getRoutingCutOffDetails(cutOffPort, cutOffTerminal);
                    //  System.out.println(">>doc date>>" + DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getDocClosing())));
                    if (cutOffVO != null) {
                        cutOffVO.setCfsGateClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getCfsGateClosing())));
                        cutOffVO.setDocClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getDocClosing())));
                        cutOffVO.setVgmClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getVgmClosing())));
                        cutOffVO.setEdiDecClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getEdiDecClosing())));
                        cutOffVO.setFullContClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getFullContClosing())));
                    }
                }
                if (cutOffVO == null) {
                    cutOffVO = new CutoffConfigVO();
                }
                request.setAttribute("ROUTECUTOFFVO", cutOffVO);
                request.setAttribute("ISBKGCUTOFF", chkbkgcutoff);
                String htIndex = request.getParameter("index");
                String routeid = null;
                int indxval = Integer.parseInt(htIndex);
                Route route = new Route();
                List routeInfo = (ArrayList) session.getAttribute("Schedule_info");
                if (routeInfo != null && routeInfo.size() > 0) {
                    routeDetails = (ArrayList) routeInfo.get(indxval);
                    if (routeDetails != null && routeDetails.size() > 0) {
                        int routearr_len = routeDetails.size();
                        setrouteDetails = new ArrayList();
                        for (int i = 0; i < routearr_len; i++) {
                            route = (Route) routeDetails.get(i);
                            if (route != null) {
                                if (i == 0) {
                                    route.setPolPortName(route.getLoadport() + " - " + (String) portlist.get(route.getLoadport().toUpperCase()));
                                    route.setPodPortName(route.getDisport() + " - " + (String) portlist.get(route.getDisport().toUpperCase()));
                                }
                                if (i != 0 && routearr_len - 1 == i) {
                                    route.setPodPortName(route.getDisport() + " - " + (String) portlist.get(route.getDisport().toUpperCase()));
                                }
                                routeid = route.getRouteId();
                            }
                            setrouteDetails.add(route);
                        }
                    }
                }
                String strDbFormat = "yyyy-MM-dd HH:mm:ss";
                String currentFormat = "yyyy-MM-dd";
                SimpleDateFormat source = new SimpleDateFormat(currentFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                String feederCutOffDate = (String) contexts.getAttribute(GeneralConf.V1FEEDERCUTOFFDATE);
                if (feederCutOffDate != null && feederCutOffDate.length() > 0) {
                    feederCutOffDate = traget.format(source.parse(feederCutOffDate));
                }
                depdate = depdate.split(" ")[0];
                if (depdate != null && depdate.length() > 0) {
                    depdate = DateConversion_New.convertDefaultDBFormat(depdate);
                }
                String appVersion = locobj.getScheuleVersion(service, vessel, voyage, bound, depdate, svcType, feederCutOffDate);
                /**
                 * String arr[] = appVersion.split("~~"); if (arr != null &&
                 * arr.length == 2) { System.out.println(">>arr[0]>>" + arr[0]);
                 * System.out.println(">>arr[1]>>" + arr[1]); appVersion =
                 * arr[0]; }*
                 */
                System.out.println("[ECOM-WAR] eBookingView appVersion in book now>>>" + appVersion);
                request.setAttribute("BOOKNOWAPPVERSION", appVersion);
                session.setAttribute("routeid", routeid);
                session.setAttribute("route", setrouteDetails);
                List personalInfo1 = null;
                personalInfo1 = locobj.getAPDetails(userCode);
                if (personalInfo1 != null && personalInfo1.size() >= 5) {
                    session.setAttribute("APEMAILID", personalInfo1.get(5));
                }
                session.setAttribute("EBOOKING_GETINDEXVALUE", routeDetails);
                session.setAttribute("EBOOKING_ROUTING_INFO", routeDetails);
                session.setAttribute("master_list", new ArrayList());
                session.setAttribute("EnsDeclarant_ICS",  new ArrayList());
                session.setAttribute("ebooking_cargo_grid", new ArrayList());
                session.setAttribute("Draft_ebooking_cargo_grid", new ArrayList());
                session.setAttribute("APInfo", personalInfo1);
                request.setAttribute("PersonalInfo1", personalInfo1);
                session.setAttribute("NEWDESIGNPRODUCT", "booking");
                session.setAttribute("type", "booknow");
                mapping_string = "newbooking";
            } else {
                saveToken(request);
                List personalInfo1 = null;
                personalInfo1 = locobj.getAPDetails(userCode);
                session.setAttribute("type", "new");
                session.setAttribute("EBOOKING_GETINDEXVALUE", new ArrayList());
                session.setAttribute("master_list", new ArrayList());
                session.setAttribute("EnsDeclarant_ICS",  new ArrayList());
                session.setAttribute("ebooking_cargo_grid", new ArrayList());
                session.setAttribute("route", null);
                session.setAttribute("Draft_ebooking_cargo_grid", new ArrayList());
                session.setAttribute("APInfo", personalInfo1);
                request.setAttribute("PersonalInfo1", personalInfo1);
                session.setAttribute("route", null);
                mapping_string = "newbooking";
            }
            String hideFaxno = (String) contexts.getAttribute(GeneralConf.HideFaxNo);
            String custType = null;
            CustomerDetailsVO custVO = null;
            List shList = null;
            List cnList = null;
            List ntList = null;
            List apList = null;
            List fwList = null;
            List whList = null;
            List ncList = null;
            List slList = null;
            List byList = null;
            List finalCustList = null;
            List custList = bookingObj.getMoreCustomerDetails(userCode, custTypeConfig);
            finalCustList = custList;
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                List v1CustList = null;
                if ("oblprint".equalsIgnoreCase(requestType)) {
                    v1CustList = v1BkgObj.getV1MoreCustomerDetails(userCode, "si");
                } else {
                    v1CustList = v1BkgObj.getV1MoreCustomerDetails(userCode, "booking");
                }
                if (v1CustList != null && custList != null) {
                    finalCustList = (List) Stream.concat(custList.stream(), v1CustList.stream()).collect(Collectors.toList());
                }
                System.out.println("finalCustList" + finalCustList.size());
            }
            if (finalCustList != null) {
                shList = new ArrayList();
                cnList = new ArrayList();
                ntList = new ArrayList();
                apList = new ArrayList();
                fwList = new ArrayList();
                whList = new ArrayList();
                ncList = new ArrayList();
                slList = new ArrayList();
                byList = new ArrayList();

                for (int i = 0; i < finalCustList.size(); i++) {
                    custVO = (CustomerDetailsVO) finalCustList.get(i);
                    custType = Validation.checkNull(custVO.getCustType());
                    custVO.setState(concatStateName(custVO.getState(), custVO.getCountry()));

                    if (custType.equalsIgnoreCase("AP")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }

                        apList.add(custVO);
                    } else if (custType.equalsIgnoreCase("SH")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        shList.add(custVO);
                    } else if (custType.equalsIgnoreCase("CN")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        cnList.add(custVO);
                    } else if (custType.equalsIgnoreCase("NT")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        ntList.add(custVO);
                    } else if (custType.equalsIgnoreCase("FW")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        fwList.add(custVO);
                    } else if (custType.equalsIgnoreCase("WH")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        whList.add(custVO);
                    } else if (custType.equalsIgnoreCase("NC")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        ncList.add(custVO);
                    } else if (custType.equalsIgnoreCase("SL")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        slList.add(custVO);
                    } else if (custType.equalsIgnoreCase("BY")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        byList.add(custVO);
                    }
                }
            }
            ntList = removeDup.removeNotifyDuplicate(ntList);
            if ("oblprint".equalsIgnoreCase(requestType)) {
                session.setAttribute("CUSTPARTIESDET_BL", custList);
                session.setAttribute("APCUSTLIST_BL", apList);
                session.setAttribute("SHCUSTLIST_BL", shList);
                session.setAttribute("CNCUSTLIST_BL", cnList);
                session.setAttribute("NTCUSTLIST_BL", ntList);
                session.setAttribute("FWCUSTLIST_BL", fwList);
                session.setAttribute("WHCUSTLIST_BL", whList);
                session.setAttribute("NCCUSTLIST_BL", ncList);
                session.setAttribute("BYCUSTLIST_SI", byList);
                session.setAttribute("SLCUSTLIST_SI", slList);
            } else {
                session.setAttribute("CUSTPARTIESDET", custList);
                session.setAttribute("APCUSTLIST", apList);
                session.setAttribute("SHCUSTLIST", shList);
                session.setAttribute("CNCUSTLIST", cnList);
                session.setAttribute("NTCUSTLIST", ntList);
                session.setAttribute("FWCUSTLIST", fwList);
                session.setAttribute("WHCUSTLIST", whList);
                session.setAttribute("NCCUSTLIST", ncList);
                session.setAttribute("BYCUSTLIST", byList);
                session.setAttribute("SLCUSTLIST", slList);
                System.out.println("slList" + slList.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(mapping_string);
    }

    public String concatStateName(String state, String country) {
        try {
            stateMapCountry = (Map) stateMap.get(Validation.checkNull(country).toUpperCase());
            if (state != null && state.length() > 0 && stateMapCountry != null) {
                state = state + " - " + Validation.checkNull((String) stateMapCountry.get(state));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Validation.checkNull(state);
    }
}
