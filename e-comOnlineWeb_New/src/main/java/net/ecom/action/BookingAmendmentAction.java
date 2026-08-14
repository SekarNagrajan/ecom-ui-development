/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAmendmentBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.AjaxSplitter;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.formbeans.AmendmentBookingFormBean;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1AmendmentBookingBeanRemote;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.Route;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class BookingAmendmentAction extends org.apache.struts.action.Action {

    EcomAmendmentBookingBeanRemote amendmentLookUpObj = EJBLookUp.getAmendmentBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomV1BookingBeanRemote v1BkgObj = EJBLookUp.getV1BKGBeanLookUp();
    EcomV1AmendmentBookingBeanRemote v1AmendmentLookUpObj = EJBLookUp.getV1AmendmentBKGLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    Map tmsmap = null, stateMap = null, stateMapCountry = null;
    String Enable_ConfirmedBooking_Edit = null, TENS_Documentation = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext contexts = servlet.getServletContext();
        net.ecom.vo.AmendmentBookingFormBean bookingVO = null;
        ENSDeclarantVo ics = null;
        String SUCCESS = "success";
        try {
            String ecomBookNo = Validation.checkNull(request.getParameter("ecomBookNo"));
            String lrpBookNo = Validation.checkNull(request.getParameter("lrpbookno"));
            String type = Validation.checkNull(request.getParameter("book_type"));

            //
            String cutOffPort = null, cutOffTerminal = null, cutOffDate = null;
            List cargoList = null;
            ArrayList routing_list = null;
            AmendmentBookingFormBean ebookBeanNew = null;
            List<BookingHaulagedetailsVo> haullist = null;
            List<BookingHaulagedetailsVo> finalhaullist = null;
            AjaxSplitter ajx = new AjaxSplitter();
            HttpSession session = request.getSession();
            String appVersion = request.getParameter("appversion");
            Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
            Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
            Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
            Hashtable terlist = (Hashtable) contexts.getAttribute("SESSION_TERMINALLIST");
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            Enable_ConfirmedBooking_Edit = (String) contexts.getAttribute(GeneralConf.Enable_ConfirmedBooking_Edit);
            TENS_Documentation = (String) contexts.getAttribute(GeneralConf.TENS_Documentation);
            String clearTemperatureWhileCopy = Validation.checkNull((String) contexts.getAttribute(GeneralConf.CLEAR_TEMPERATURE_WHILE_COPY));
            boolean chkbkgcutoff = false;
            List pluginlis = (List) contexts.getAttribute("LRP_PLUGIN_DETAILS");
            boolean editorgdlr = false;
            boolean isconfirmededit = false;
            if ("Y".equalsIgnoreCase(Enable_ConfirmedBooking_Edit)) {
                isconfirmededit = true;
            }
            //
            //V1 Cache
            Map<String, Map<String, String>> masterMap = (Map<String, Map<String, String>>) contexts.getAttribute("ECOMV1MASTERCACHE");
            Map<String, String> v1ComdtyMap = masterMap.get("COMMODITY");
            Map<String, String> HSCODECONFIGMAP = (Map<String, String>) contexts.getAttribute("HSCODECONFIGMAP");
            //
//             System.out.println("[ECOM-WAR] BookingAmendmentAction >>>entered in amentdement>>>");
//             System.out.println("[ECOM-WAR] BookingAmendmentAction >>>type>>" + type);
//             System.out.println("[ECOM-WAR] BookingAmendmentAction >>>bookNo>>>" + lrpBookNo);
//             System.out.println("[ECOM-WAR] BookingAmendmentAction >>ecom book no>>" + ecomBookNo);
//             System.out.println("[ECOM-WAR] BookingAmendmentAction >>>appVersion>>" + appVersion);

            Set bookSet = (Set) session.getAttribute("bookingAccessList");
//             System.out.println("[ECOM-WAR] BookingAmendmentAction bookSet.contains(ecomBookNo)......" + bookSet.contains(ecomBookNo) + "~~ bookSet.contains(lrpBookNo)" + bookSet.contains(lrpBookNo));
            System.out.println("[ECOM-WAR] BookingAmendmentAction bookSet.contains(ecomBookNo): " + bookSet.contains(ecomBookNo) + ", bookSet.contains(lrpBookNo): " + bookSet.contains(lrpBookNo) + ", type: " + type + ", bookNo: " + lrpBookNo + ", ecom book no: " + ecomBookNo + ", appVersion: " + appVersion);
            Map custMap = new HashMap<>();
            ArrayList<AmendmentBookingFormBean> buyerCustlist = null;
            ArrayList<AmendmentBookingFormBean> sellerCustlist = null;
            if ((bookSet != null && bookSet.contains(ecomBookNo)) || (bookSet != null && bookSet.contains(lrpBookNo))) {

                if ("1".equalsIgnoreCase(appVersion)) {
                    if ("P".equalsIgnoreCase(type)) {
//                         System.out.println("[ECOM-WAR] BookingAmendmentAction >>>accepted booking");
                        bookingVO = v1AmendmentLookUpObj.getV1LRPBookingMasterDetails(lrpBookNo);
                        cargoList = v1AmendmentLookUpObj.getV1LRPBookingViewCargoDetails(lrpBookNo, type);
                        routing_list = v1AmendmentLookUpObj.getV1LRPBookingRoutingDetails(lrpBookNo);
                        session.setAttribute("type", "");
                    } else if ("R".equalsIgnoreCase(type)) {//ReUse Booking
//                         System.out.println("[ECOM-WAR] BookingAmendmentAction >>>accepted booking");
                        bookingVO = v1AmendmentLookUpObj.getV1LRPBookingMasterDetails(lrpBookNo);
                        cargoList = v1AmendmentLookUpObj.getV1LRPBookingViewCargoDetails(lrpBookNo, type);
                        routing_list = v1AmendmentLookUpObj.getV1LRPBookingRoutingDetails(lrpBookNo);
                        session.setAttribute("type", "reuse");
                    } else {
//                         System.out.println("[ECOM-WAR] BookingAmendmentAction >>submitterd booking");
                        bookingVO = amendmentLookUpObj.getEcomBookingDetails(ecomBookNo);
                        cargoList = amendmentLookUpObj.getEcomBookingCargoDetails(ecomBookNo);
                        routing_list = amendmentLookUpObj.getEcomRoutingDetailsfrombookingleg(ecomBookNo);
                        session.setAttribute("type", "");
                    }
                } else {
                    if ("P".equalsIgnoreCase(type) || (isconfirmededit && "C".equalsIgnoreCase(type))) {
//                         System.out.println("[ECOM-WAR] BookingAmendmentAction >>>accepted booking");
                        bookingVO = amendmentLookUpObj.getLRPBookingMasterDetails(lrpBookNo);
                        cargoList = amendmentLookUpObj.getLRPBookingViewCargoDetails(lrpBookNo, type, ecomBookNo, "");
                        if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                            custMap = amendmentLookUpObj.getLRPBookingCutomerDetails(lrpBookNo);
                        }
                        routing_list = amendmentLookUpObj.getLRPBookingRoutingDetails(lrpBookNo);
                        haullist = amendmentLookUpObj.getLRPBookingHaulageDetails(lrpBookNo, type, null);
                        session.setAttribute("type", "");

                        List filenamelist = ecomCntrlObj.getSubmittedFileNames(lrpBookNo, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH), "BKG");
                        request.setAttribute("FILENAMELIST", filenamelist);
                        fileCopyFromBooking(ecomBookNo, lrpBookNo, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH));
                        ics = amendmentLookUpObj.getLRPBookingENSTabDetails(lrpBookNo);
                    } else if ("R".equalsIgnoreCase(type)) {//ReUse Booking
//                         System.out.println("[ECOM-WAR] BookingAmendmentAction >>>accepted booking");
                        bookingVO = amendmentLookUpObj.getLRPBookingMasterDetails(lrpBookNo);
                        cargoList = amendmentLookUpObj.getLRPBookingViewCargoDetails(lrpBookNo, type, ecomBookNo, clearTemperatureWhileCopy);
                        if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                            custMap = amendmentLookUpObj.getLRPBookingCutomerDetails(lrpBookNo);
                        }
                        routing_list = amendmentLookUpObj.getLRPBookingRoutingDetails(lrpBookNo);
                        if (cargoList != null && cargoList.size() > 0) {
                            net.ecom.vo.AmendmentBookingFormBean ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoList.get(cargoList.size() - 1);
                            HashMap cargonomap = ebookVo.getCargonomap();
                            System.out.println("[ECOM-WAR] BookingAmendmentAction ^^^^^^cargonomap^^^^ " + cargonomap);
                            haullist = amendmentLookUpObj.getLRPBookingHaulageDetails(lrpBookNo, type, cargonomap);
                        }
                        ics = amendmentLookUpObj.getLRPBookingENSTabDetails(lrpBookNo);
                        session.setAttribute("type", "reuse");
                    } else {
//                         System.out.println("[ECOM-WAR] BookingAmendmentAction >>submitted booking");
                        bookingVO = amendmentLookUpObj.getEcomBookingDetails(ecomBookNo);
                        cargoList = amendmentLookUpObj.getEcomBookingCargoDetails(ecomBookNo);
                        custMap = amendmentLookUpObj.getEcomCutomerDetails(ecomBookNo);
                        routing_list = amendmentLookUpObj.getEcomRoutingDetailsfrombookingleg(ecomBookNo);
                        haullist = bookingObj.getEcomBookingHaulageDetails(ecomBookNo);
                        ics = bookingObj.getENSTabDetails(ecomBookNo);
                        session.setAttribute("type", "");
                        List filenamelist = ecomCntrlObj.getSubmittedFileNames(ecomBookNo, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH), "Ecom");
                        request.setAttribute("FILENAMELIST", filenamelist);
                    }
                }
                if (lrpBookNo != null && lrpBookNo.length() > 0) {
                    request.setAttribute("AMENDMENTAPDISABLE", "YES");
                } else {
                    request.setAttribute("AMENDMENTAPDISABLE", "NO");
                }

                //  update container no in Haulage details
                if (haullist != null) {
                    net.ecom.vo.AmendmentBookingFormBean ebookVo = null;
                    BookingHaulagedetailsVo haulvo = null;
                    boolean iscontainerno = false;
                    for (int j = 0; j < haullist.size(); j++) {
                        haulvo = haullist.get(j);
                        if (haulvo.getEqpid() != null && haulvo.getEqpid().length() > 0) {
                            iscontainerno = true;
                            break;
                        }
                    }
                    if (iscontainerno) {
                        if (cargoList != null) {
                            for (int i = 0; i < cargoList.size(); i++) {
                                ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoList.get(i);
                                for (int j = 0; j < haullist.size(); j++) {
                                    haulvo = haullist.get(j);
                                    if (haulvo.getEqpid().equalsIgnoreCase(ebookVo.getContainerNo())) {
                                        haulvo.setEqprowid(i + 1);
                                    }
                                }
                            }
                        }
                        finalhaullist = haullist;
                    } else {
                        if (cargoList != null) {
                            int cargo_ini_size = cargoList.size();
                            int tempconid;
                            finalhaullist = new ArrayList<>();
                            for (int i = 0; i < cargo_ini_size; i++) {
                                ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoList.get(i);
                                for (int j = 0; j < haullist.size(); j++) {
                                    haulvo = haullist.get(j);
                                    if (haulvo.getEqprowid() == ebookVo.getConid()) {
                                        haulvo.setEqpid(ebookVo.getContainerNo());
                                        tempconid = haulvo.getEqprowid();
                                        haulvo.setEqprowid(i + 1);
                                        finalhaullist.add((BookingHaulagedetailsVo) haulvo.clone());
                                        haulvo.setEqprowid(tempconid);
                                    }
                                }
                            }
                        }
                    }
                }

                if ("1".equalsIgnoreCase(appVersion)) {
                    if (cargoList != null) {
                        int cargo_ini_size = cargoList.size();
                        for (int i = 0; i < cargo_ini_size; i++) {
                            net.ecom.vo.AmendmentBookingFormBean ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoList.get(i);
                            ebookVo.setEbooking_commodity(Validation.checkNull((String) v1ComdtyMap.get(ebookVo.getEbook_commoditycode())));
                            ebookVo.setAppVersion(appVersion);
                            ebookBeanNew = new AmendmentBookingFormBean();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            ebookBeanNew.setNumber(i);
                            cargoList.set(i, ebookBeanNew);
                        }
                    }
                } else {
                    if (cargoList != null) {
                        int cargo_ini_size = cargoList.size();
                        for (int i = 0; i < cargo_ini_size; i++) {
                            net.ecom.vo.AmendmentBookingFormBean ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoList.get(i);
                            ebookVo.setEbooking_commodity(Validation.checkNull((String) comlist.get(ebookVo.getEbook_commoditycode())));
                            ebookVo.setHsCommodityVal(Validation.checkNull((String) comlist.get(ebookVo.getHscode())));
                            ebookVo.setAppVersion(appVersion);
                            ebookBeanNew = new AmendmentBookingFormBean();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            ebookBeanNew.setNumber(i);
                            cargoList.set(i, ebookBeanNew);
                        }
                    }
                    if (custMap != null && custMap.containsKey("SL")) {
                        sellerCustlist = new ArrayList<AmendmentBookingFormBean>();
                        ArrayList<net.ecom.vo.AmendmentBookingFormBean> custlist_ini = (ArrayList<net.ecom.vo.AmendmentBookingFormBean>) custMap.get("SL");
                        int cust_ini_size = custlist_ini.size();
                        for (int i = 0; i < cust_ini_size; i++) {
                            net.ecom.vo.AmendmentBookingFormBean ebookVo = (net.ecom.vo.AmendmentBookingFormBean) custlist_ini.get(i);
                            ebookVo.setAppVersion(appVersion);
                            if (Validation.checkNull(ebookVo.getEbook_Custcountry()).length() > 0) {
                                bookingVO.setEbook_shipperstate(concatStateName(bookingVO.getEbook_shipperstate(), bookingVO.getEbook_shippercountry()));
                                bookingVO.setEbook_shippercountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_shippercountry())));
                                ebookVo.setEbook_Custstate(concatStateName(ebookVo.getEbook_Custstate(), ebookVo.getEbook_Custcountry()));
//                                System.out.println("state=>" + ebookVo.getEbook_Custstate() + "===>" + ebookVo.getEbook_Custcountry() + "///" + ebookVo.getEbook_Custstate());
                                ebookVo.setEbook_Custcountry_desc(ebookVo.getEbook_Custcountry() + " - " + (Validation.checkNull((String) contrylist.get(ebookVo.getEbook_Custcountry()))).toUpperCase());
                            }
                            ebookBeanNew = new AmendmentBookingFormBean();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            sellerCustlist.add(ebookBeanNew);
                        }
                    }
                    if (custMap != null && custMap.containsKey("BY")) {
                        buyerCustlist = new ArrayList<AmendmentBookingFormBean>();
                        ArrayList<net.ecom.vo.AmendmentBookingFormBean> custlist_ini = (ArrayList<net.ecom.vo.AmendmentBookingFormBean>) custMap.get("BY");
                        int cust_ini_size = custlist_ini.size();
                        for (int i = 0; i < cust_ini_size; i++) {
                            net.ecom.vo.AmendmentBookingFormBean ebookVo = (net.ecom.vo.AmendmentBookingFormBean) custlist_ini.get(i);
                            ebookVo.setAppVersion(appVersion);
                            if (Validation.checkNull(ebookVo.getEbook_Custcountry()).length() > 0) {
                                ebookVo.setEbook_Custstate(concatStateName(ebookVo.getEbook_Custstate(), ebookVo.getEbook_Custcountry()));
//                                System.out.println("state=>" + ebookVo.getEbook_Custstate() + "===>" + ebookVo.getEbook_Custcountry() + "///" + ebookVo.getEbook_Custstate());
                                ebookVo.setEbook_Custcountry_desc(ebookVo.getEbook_Custcountry() + " - " + (Validation.checkNull((String) contrylist.get(ebookVo.getEbook_Custcountry()))).toUpperCase());
                            }
                            ebookBeanNew = new AmendmentBookingFormBean();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            buyerCustlist.add(ebookBeanNew);
                        }
                    }
                }
//                 System.out.println("[ECOM-WAR] BookingAmendmentAction >>Booking-type>>" + type);
                if ("R".equalsIgnoreCase(type)) {//ReUse Booking
                    bookingVO.setBooking_refno("");
                    bookingVO.setHidbkgtype("reusebkg");
                    bookingVO.setSaveStatus(type);
                    bookingVO.setLrpBookNo("");
                    bookingVO.setEbook_rate_reference("");
                    bookingVO.setEbook_ocean_freight("");
                    bookingVO.setEmptyPickup("");
                    bookingVO.setEbook_pickup_point("");
                    bookingVO.setEbook_haulercode("");
                    bookingVO.setEbook_natcode("");
                    bookingVO.setEbook_haulagetype("");
                    bookingVO.setEbook_pickup_date("");
                    bookingVO.setEbook_drop_date("");
                    bookingVO.setEbook_exportref("");
                    bookingVO.setEbook_custpo("");
                    bookingVO.setShipperReference("");
                    bookingVO.setShipperReferenceType("");
                    bookingVO.setACID("");
                    bookingVO.setCustomerreference("");
                    bookingVO.setEbook_reftype("");
                    bookingVO.setEmptyPickUpDate(""); 
                } else {
                    bookingVO.setHidbkgtype("amendbkg");
                    bookingVO.setBooking_refno(ecomBookNo);
                    bookingVO.setSaveStatus(type);
                    bookingVO.setLrpBookNo(lrpBookNo);
                }
                cutOffPort = Validation.checkNull(bookingVO.getEbook_portofloading());
                cutOffTerminal = Validation.checkNull(bookingVO.getEbook_terminal());
                
            
                String deliveryPort = bookingVO.getEbook_delivery() + " - " + Validation.checkNull((String) portlist.get(bookingVO.getEbook_delivery().toUpperCase()));
                bookingVO.setEbook_delivery_country(MaskPortUtil.swapActualPortToMaskPortWithName(deliveryPort));

                String originPort = bookingVO.getEbook_origin() + " - " + Validation.checkNull((String) portlist.get(bookingVO.getEbook_origin().toUpperCase()));
                bookingVO.setEbook_origin_country(MaskPortUtil.swapActualPortToMaskPortWithName(originPort));
         
                
                bookingVO.setEbooking_vessel(Validation.checkNull((String) vessellist.get(bookingVO.getEbook_vesselcode().toUpperCase())));
                bookingVO.setEbook_linevessel(Validation.checkNull((String) vessellist.get(bookingVO.getEbook_linevesselcode())));
                bookingVO.setEbooking_service(Validation.checkNull((String) serlist.get(bookingVO.getEbook_servicecode().toUpperCase())));
                bookingVO.setEbook_lineservice(Validation.checkNull((String) serlist.get(bookingVO.getEbook_servicecode().toUpperCase())));
                bookingVO.setEbook_pol_country(Validation.checkNull((String) portlist.get(bookingVO.getEbook_pol().toUpperCase())));
                bookingVO.setEbook_terminal_country(Validation.checkNull((String) terlist.get(bookingVO.getEbook_terminal())));
                bookingVO.setEbook_pod_country(Validation.checkNull((String) portlist.get(bookingVO.getEbook_pod().toUpperCase())));
                bookingVO.setEbook_terminalpod_country(Validation.checkNull((String) terlist.get(bookingVO.getEbook_terminalpod())));
                bookingVO.setEbook_forwardercity_desc(bookingVO.getEbook_forwardercity());
                bookingVO.setEbook_forwardercountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_Warehousecountry())));
                bookingVO.setEbook_Warehousecity_desc(bookingVO.getEbook_Warehousecity());
                bookingVO.setEbook_Warehousecountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_Warehousecountry())));
                bookingVO.setEbook_shippercity_desc(bookingVO.getEbook_shippercity());
                bookingVO.setEbook_shippercountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_shippercountry())));
                bookingVO.setEbook_NamedCustomercity_desc(bookingVO.getEbook_NamedCustomercity());
                bookingVO.setEbook_NamedCustomercountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_NamedCustomercountry())));

                bookingVO.setEbook_consigneecity_desc(bookingVO.getEbook_consigneecity());
                bookingVO.setEbook_consigneecountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_consigneecountry())));
                bookingVO.setEbook_notifycity_desc(bookingVO.getEbook_notifycity());
                bookingVO.setEbook_notifycountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_notifycountry())));
                bookingVO.setEbook_notifycity_desc_n1(bookingVO.getEbook_notifycity_n1());
                bookingVO.setEbook_notifycountry_desc_n1(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_notifycountry_n1())));

                bookingVO.setEbook_portofloading_desc(bookingVO.getEbook_pol() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(bookingVO.getEbook_pol()).toUpperCase())));
                bookingVO.setEbook_portofdischarge_desc(bookingVO.getEbook_pod() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(bookingVO.getEbook_pod()).toUpperCase())));
                bookingVO.setEbook_routeid(bookingVO.getEbook_routeid());
                bookingVO.setAppVersion(appVersion);

                bookingVO.setEbook_agreestate(concatStateName(bookingVO.getEbook_agreestate(), bookingVO.getEbook_agreecountry()));
                bookingVO.setEbook_bookingstate(concatStateName(bookingVO.getEbook_bookingstate(), bookingVO.getEbook_bookingcountry()));
                bookingVO.setEbook_shipperstate(concatStateName(bookingVO.getEbook_shipperstate(), bookingVO.getEbook_shippercountry()));
                bookingVO.setEbook_consigneestate(concatStateName(bookingVO.getEbook_consigneestate(), bookingVO.getEbook_consigneecountry()));
                bookingVO.setEbook_forwarderstate(concatStateName(bookingVO.getEbook_forwarderstate(), bookingVO.getEbook_forwardercountry()));
                bookingVO.setEbook_Warehousestate(concatStateName(bookingVO.getEbook_Warehousestate(), bookingVO.getEbook_Warehousecountry()));
                bookingVO.setEbook_NamedCustomerstate(concatStateName(bookingVO.getEbook_NamedCustomerstate(), bookingVO.getEbook_NamedCustomercountry()));
                bookingVO.setEbook_notifystate(concatStateName(bookingVO.getEbook_notifystate(), bookingVO.getEbook_notifycountry()));
                bookingVO.setEbook_notifystate_n1(concatStateName(bookingVO.getEbook_notifystate_n1(), bookingVO.getEbook_notifycountry_n1()));
                bookingVO.setEbook_Sellerstate(concatStateName(bookingVO.getEbook_Sellerstate(), bookingVO.getEbook_Sellercountry()));
                bookingVO.setEbook_Sellercity_desc(bookingVO.getEbook_Sellercity());
                bookingVO.setEbook_Sellercountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_Sellercountry())));
                bookingVO.setEbook_Buyerstate(concatStateName(bookingVO.getEbook_Buyerstate(), bookingVO.getEbook_Buyercountry()));
                bookingVO.setEbook_Buyercity_desc(bookingVO.getEbook_Buyercity());
                bookingVO.setEbook_Buyercountry_desc(Validation.checkNull((String) contrylist.get(bookingVO.getEbook_Buyercountry())));

                if (routing_list != null && routing_list.size() > 0) {
                    int routearr_len = routing_list.size();
                    String pol_str = null, pod_str = null;
                    String temp = null;
                    Route routeDTO = null;
                    boolean checkCutOffDate = true;
                    ArrayList ars = ajx.FindLordporttype(routing_list);
                    for (int i = 0; i < routearr_len; i++) {
                        routeDTO = (Route) routing_list.get(i);
                        if ("A".equalsIgnoreCase(type) || "R".equalsIgnoreCase(type)) {
                            bookingVO.setEbook_routeid(routeDTO.getRouteId());
                        }
                        if (routeDTO != null) {
                            temp = "N";
                            pol_str = (String) ars.get(0);
                            pod_str = (String) ars.get(1);
                            String lordporttype = Validation.checkNull(ecomCntrlObj.getPortType(routeDTO.getLoadport()));
                            String disporttype = Validation.checkNull(ecomCntrlObj.getPortType(routeDTO.getDisport()));
                            if ("I".equalsIgnoreCase(lordporttype)) {
                                //  preCarriage = "C";
                                routeDTO.setLoadtype("");
                                routeDTO.setDisctype("");
                                temp = "Y";
                            } else {
                                //  preCarriage = "M";
                                if (pol_str.equalsIgnoreCase(routeDTO.getLoadport())) {
                                    routeDTO.setLoadtype("L");
                                } else {
                                    routeDTO.setLoadtype("T");
                                }
                            }
                            if (checkCutOffDate && ("F".equalsIgnoreCase(routeDTO.getLegType()) || "M".equalsIgnoreCase(routeDTO.getLegType()))) {
                                checkCutOffDate = false;
                                bookingVO.setEbook_date(routeDTO.getETD1());
                                cutOffDate = Validation.checkNull(routeDTO.getCutoff_polarrdate());
                                cutOffTerminal = (cutOffTerminal != null && cutOffTerminal.trim().length() > 0) ? cutOffTerminal : Validation.checkNull(routeDTO.getBlpolTer());
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
                            routeDTO.setLordporttype(lordporttype);
                            routeDTO.setDisporttype(disporttype);
                            routeDTO.setPolPortName((String) portlist.get(Validation.checkNull(routeDTO.getLoadport()).toUpperCase()));
                            routeDTO.setPodPortName((String) portlist.get(Validation.checkNull(routeDTO.getDisport()).toUpperCase()));
                            routeDTO.setServiceName((String) serlist.get(Validation.checkNull(routeDTO.getService()).toUpperCase()));
                            routeDTO.setVesselName((String) vessellist.get(Validation.checkNull(routeDTO.getVessel()).toUpperCase()));
                            routeDTO.setETA1(DateConversion_New.convertDefaultFormatwithTime(routeDTO.getETA1()));
                            routeDTO.setETD1(DateConversion_New.convertDefaultFormatwithTime(routeDTO.getETD1()));
                            routing_list.set(i, routeDTO);
                        }
                    }
                }
                CutoffConfigVO cutOffVO = null;
                cutOffPort = Validation.checkNull(cutOffPort);
                cutOffTerminal = Validation.checkNull(cutOffTerminal);
                cutOffDate = Validation.checkNull(cutOffDate);
//                 System.out.println("[ECOM-WAR] BookingAmendmentAction >>cutOffPort>>" + cutOffPort);
//                 System.out.println("[ECOM-WAR] BookingAmendmentAction >>cutOffTerminal>>" + cutOffTerminal);
//                 System.out.println("[ECOM-WAR] BookingAmendmentAction >>>cutOffDate>>" + cutOffDate);
                System.out.println("[ECOM-WAR] BookingAmendmentAction cutOffPort: " + cutOffPort + ", cutOffTerminal: " + cutOffTerminal + ", cutOffDate: " + cutOffDate);

                if (pluginlis != null && pluginlis.contains("BEC")) {
                    chkbkgcutoff = true;
                    if ("P".equalsIgnoreCase(type) || "R".equalsIgnoreCase(type) || (isconfirmededit && "C".equalsIgnoreCase(type))) {
                        cutOffVO = amendmentLookUpObj.getLRPBookingConfDetails(lrpBookNo);
                    } else {
                        cutOffVO = bookingObj.getEcomBookingConfDetails(ecomBookNo);
                    }
                }
                if ((!chkbkgcutoff) && cutOffPort.length() > 0 && cutOffTerminal.length() > 0 && cutOffDate.length() > 0) {
                    cutOffVO = ecomCntrlObj.getRoutingCutOffDetails(cutOffPort, cutOffTerminal);
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

                //get Haulage details
                tmsmap = bookingObj.getBkgHaulTMSClientDetails(bookingVO.getEbook_origin_country().split("-")[0].trim());
                if (finalhaullist != null && finalhaullist.size() > 0) {
                    finalhaullist.forEach(haullis -> {
                        if (haullis.getTmsclientcode() != null && haullis.getTmsclientcode().length() > 0 && tmsmap != null) {
                            haullis.setTmsclientname((String) tmsmap.get(haullis.getTmsclientcode()));
                        }
                        haullis.setDropdate(DateConversion_New.convertDefaultFormatwithTime(haullis.getDropdate()));
                    });
                }
                //
                String originDelivery = null;
                if ("Y".equalsIgnoreCase(Enable_ConfirmedBooking_Edit)) {
                    if (lrpBookNo != null && lrpBookNo.length() > 0 && !"R".equalsIgnoreCase(type)) {
                        originDelivery = bookingObj.validateConfirmBookingEdit(lrpBookNo);
                        if (originDelivery != null && "valid".equalsIgnoreCase(originDelivery)) {
                            editorgdlr = true;
                        }
                    }
                }
                request.setAttribute("Enable_ORIGIN_DELIVERY", editorgdlr);
                //
                request.setAttribute("ISBKGCUTOFF", chkbkgcutoff);
                request.setAttribute("ROUTECUTOFFVO", cutOffVO);
                if ("R".equalsIgnoreCase(type)) {
                    session.setAttribute("EBOOKING_GETINDEXVALUE", null);
                } else {
                    session.setAttribute("EBOOKING_GETINDEXVALUE", routing_list);
                }
                ArrayList list = new ArrayList();
                list.add(bookingVO);
                session.setAttribute("BKGAMENDMENTMASTERDETAILS", list);
                session.setAttribute("Amendment_EnsDeclarant_ICS", ics);
                session.setAttribute("BKGAMENDMENTCARGODETAILS", cargoList);
                request.setAttribute("EBOOKINGHAULAGEDETAILS", finalhaullist);
                session.setAttribute("ebooking_seller_grid", sellerCustlist);
                session.setAttribute("ebooking_buyere_grid", buyerCustlist);
            } else {
                SUCCESS = "sessionex";
            }
            System.out.println("[ECOM-WAR] BookingAmendmentAction Booking........" + SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM-WAR] BookingAmendmentAction Booking........" + SUCCESS);
        return mapping.findForward(SUCCESS);
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

    public void fileCopyFromBooking(String bookno, String lrpbookno, String path) {
        try {
            String ecompath = path + File.separator + bookno;
            String lrppath = path.replace("Ecom", "BKG") + File.separator + lrpbookno;
            File directory = new File(lrppath);
            if (directory.exists()) {
                File srcDir = new File(lrppath);
                File destDir = new File(ecompath);
                FileUtils.deleteDirectory(destDir);
                FileUtils.copyDirectory(srcDir, destDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
