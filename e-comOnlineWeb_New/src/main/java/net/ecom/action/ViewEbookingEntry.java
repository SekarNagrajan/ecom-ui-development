package net.ecom.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAmendmentBookingBeanRemote;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.Route;

public class ViewEbookingEntry extends Action {

    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomV1BookingBeanRemote v1BkgObj = EJBLookUp.getV1BKGBeanLookUp();
    //EcomV1BookingBeanRemote v1BkgObj=EJBLookUp.getV1BKGBeanLookUp();
    EcomAmendmentBookingBeanRemote amendmentLookUpObj = EJBLookUp.getAmendmentBeanLookUp();
    Map tmsmap = null, stateMap = null, stateMapCountry = null;

    ;

    @SuppressWarnings("static-access")
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "successNew";
        try {
            //v1BkgObj.checkV1Bean();
            String bookNo = Validation.checkNull(request.getParameter("bookno"));
            String lrpBookNo = Validation.checkNull(request.getParameter("lrpbookno"));
            System.out.println("[ECOM-WAR] ViewEbookingEntry bookNo...." + bookNo + "lrpBookNo......" + lrpBookNo);
            ServletContext contexts = servlet.getServletContext();
            String flag = null;
            ArrayList bookinglist = new ArrayList();
            ENSDeclarantVo ics = null;
            List cargolist = new ArrayList();
            ArrayList routing_list = null;
            List<BookingHaulagedetailsVo> haullist = null;
            String originport = null;
            String appVersion = request.getParameter("appversion");
            String lrpecomstatus = Validation.checkNull(request.getParameter("ecomlrptype"));
            System.out.println("[ECOM-WAR] ViewEbookingEntry ======>lrpecomstatus-" + lrpecomstatus);
            HttpSession session = request.getSession();
            Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
            Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
            Hashtable citylist = (Hashtable) contexts.getAttribute("SESSION_CITYLIST");
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PORTLIST");
            Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
            Hashtable terlist = (Hashtable) contexts.getAttribute("SESSION_TERMINALLIST");
            Hashtable packagemap = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
            LinkedHashMap eqpmap = (LinkedHashMap) contexts.getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
            String TENS_Documentation = (String) request.getServletContext().getAttribute(GeneralConf.TENS_Documentation);
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            String clearTemperatureWhileCopy = Validation.checkNull((String) contexts.getAttribute(GeneralConf.CLEAR_TEMPERATURE_WHILE_COPY));
            //V1 Cache
            Map<String, Map<String, String>> masterMap = (Map<String, Map<String, String>>) contexts.getAttribute("ECOMV1MASTERCACHE");
            Map<String, String> v1ComdtyMap = masterMap.get("COMMODITY");
            CutoffConfigVO cutOffVO = null;
            HashMap<String, List<EbookingValidatorFormBeanNew>> custMap = new HashMap<>();
            Set bookSet = (Set) session.getAttribute("bookingAccessList");
            Set dashBoardSet = (Set) session.getAttribute("dashBoardAccessList");
            try {
                System.out.println("[ECOM-WAR] ViewEbookingEntry >>lrpBookNo>>" + lrpBookNo + ">>lrpecomstatus>>" + lrpecomstatus);
                if ((bookSet != null && bookSet.contains(lrpBookNo)) || (bookSet != null && bookSet.contains(bookNo)) || (dashBoardSet != null && dashBoardSet.contains(lrpBookNo))) {
                    if ("1".equalsIgnoreCase(appVersion) && "L".equalsIgnoreCase(lrpecomstatus)) {
                        bookinglist = v1BkgObj.getV1LRPBookingViewDetails(lrpBookNo);
                        cargolist = v1BkgObj.getV1LRPBookingViewCargoDetails(lrpBookNo);
                        routing_list = v1BkgObj.getV1LRPBookingRoutingDetails(lrpBookNo);

                    } else {
                        if (lrpecomstatus.equalsIgnoreCase("L")) {
                            bookinglist = bookingObj.getLRPBookingViewDetails(lrpBookNo);
                            cargolist = bookingObj.getLRPBookingViewCargoDetails(lrpBookNo, bookNo);
                            routing_list = bookingObj.getLRPBookingRoutingDetails(lrpBookNo);
                            if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                                custMap = (HashMap<String, List<EbookingValidatorFormBeanNew>>) bookingObj.getLRPBookingCutomerDetails(lrpBookNo);
                            }
                            haullist = amendmentLookUpObj.getLRPBookingHaulageDetails(lrpBookNo, "V", null);
                            cutOffVO = amendmentLookUpObj.getLRPBookingConfDetails(lrpBookNo);
                            ics = amendmentLookUpObj.getLRPBookingENSTabDetails(lrpBookNo);
                        } else {
                            bookinglist = bookingObj.getEbookingDetails(bookNo, flag, "view");
                            cargolist = bookingObj.getEbookingCargoDetails(bookNo, flag, clearTemperatureWhileCopy);
                            routing_list = bookingObj.getEbookingRoutingDetailsfrombookingleg(bookNo);
                            if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                                custMap = (HashMap<String, List<EbookingValidatorFormBeanNew>>) bookingObj.getEbookingViewCustDetails(bookNo, "view");
                            }

                            haullist = bookingObj.getEcomBookingHaulageDetails(bookNo);
                            cutOffVO = bookingObj.getEcomBookingConfDetails(bookNo);
                            ics = bookingObj.getENSTabDetails(bookNo);
                        }
                    }
                    if (bookinglist != null) {
                        int size = bookinglist.size();
                        EbookingValidatorFormBeanNew masterdata = null;
                        for (int i = 0; i < size; i++) {
                            masterdata = (EbookingValidatorFormBeanNew) bookinglist.get(i);
                            masterdata.setBooking_refno(Validation.checkNull(lrpBookNo));
                            System.out.println("[ECOM-WAR] ViewEbookingEntry  =>>>>" + masterdata.getEmptyPickup());
                            masterdata.setEcombooking_refno(Validation.checkNull(bookNo));
                            
                           
                            String deliveryPort = masterdata.getEbook_delivery() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(masterdata.getEbook_delivery()).toUpperCase()));
                            masterdata.setEbook_delivery_country(MaskPortUtil.swapActualPortToMaskPortWithName(deliveryPort));
                           
                            String originPort = masterdata.getEbook_origin() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(masterdata.getEbook_origin()).toUpperCase()));
                            masterdata.setEbook_origin_country(MaskPortUtil.swapActualPortToMaskPortWithName(originPort));
                           
                            
                            masterdata.setEbooking_vessel(Validation.checkNull((String) vessellist.get(Validation.checkNull(masterdata.getEbook_vesselcode()).toUpperCase())));
                            masterdata.setEbook_linevessel(Validation.checkNull((String) vessellist.get(Validation.checkNull(masterdata.getEbook_linevesselcode()).toUpperCase())));
                            masterdata.setEbooking_service(Validation.checkNull((String) serlist.get(Validation.checkNull(masterdata.getEbook_servicecode()).toUpperCase())));
                            masterdata.setEbook_lineservice(Validation.checkNull((String) serlist.get(Validation.checkNull(masterdata.getEbook_servicecode()).toUpperCase())));
                            masterdata.setEbook_pol_country(masterdata.getEbook_pol() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(masterdata.getEbook_pol()).toUpperCase())));
                            masterdata.setEbook_terminal_country(Validation.checkNull((String) terlist.get(masterdata.getEbook_terminal())));
                            masterdata.setEbook_pod_country(masterdata.getEbook_pod() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(masterdata.getEbook_pod()).toUpperCase())));
                            masterdata.setEbook_terminalpod_country(Validation.checkNull((String) terlist.get(masterdata.getEbook_terminalpod())));
                            masterdata.setEbook_shippercity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_shippercity_desc())));
                            masterdata.setEbook_shippercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_shippercountry())));
                            masterdata.setEbook_forwardercity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_forwardercity_desc())));
                            masterdata.setEbook_forwardercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_forwardercountry())));
                            masterdata.setEbook_Warehousecity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_Warehousecity_desc())));
                            masterdata.setEbook_Warehousecountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Warehousecountry())));
                            masterdata.setEbook_NamedCustomercity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_NamedCustomercity_desc())));
                            masterdata.setEbook_NamedCustomercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_NamedCustomercountry())));

                            masterdata.setEbook_consigneecity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_consigneecity())));
                            masterdata.setEbook_consigneecountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_consigneecountry())));
                            masterdata.setEbook_notifycity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_notifycity())));
                            masterdata.setEbook_notifycountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_notifycountry())));
                            masterdata.setEbook_notifycity_desc_n1(Validation.checkNull((String) citylist.get(masterdata.getEbook_notifycity())));
                            masterdata.setEbook_notifycountry_desc_n1(Validation.checkNull((String) contrylist.get(masterdata.getEbook_notifycountry_n1())));
                            System.out.println("[ECOM-WAR] ViewEbookingEntry agreement party county : " + masterdata.getEbook_agreecountry());
                            masterdata.setEbook_agreecountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_agreecountry())));
                            masterdata.setEbook_bookingcountry_name(Validation.checkNull((String) contrylist.get(masterdata.getEbook_bookingcountry())));

                            masterdata.setEbook_agreestate(concatStateName(masterdata.getEbook_agreestate(), masterdata.getEbook_agreecountry()));
                            masterdata.setEbook_bookingstate(concatStateName(masterdata.getEbook_bookingstate(), masterdata.getEbook_bookingcountry()));
                            masterdata.setEbook_shipperstate(concatStateName(masterdata.getEbook_shipperstate(), masterdata.getEbook_shippercountry()));
                            masterdata.setEbook_consigneestate(concatStateName(masterdata.getEbook_consigneestate(), masterdata.getEbook_consigneecountry()));
                            masterdata.setEbook_forwarderstate(concatStateName(masterdata.getEbook_forwarderstate(), masterdata.getEbook_forwardercountry()));
                            masterdata.setEbook_Warehousestate(concatStateName(masterdata.getEbook_Warehousestate(), masterdata.getEbook_Warehousecountry()));
                            masterdata.setEbook_notifystate(concatStateName(masterdata.getEbook_notifystate(), masterdata.getEbook_notifycountry()));
                            masterdata.setEbook_notifystate_n1(concatStateName(masterdata.getEbook_notifystate_n1(), masterdata.getEbook_notifycountry_n1()));
                            masterdata.setEbook_NamedCustomerstate(concatStateName(masterdata.getEbook_NamedCustomerstate(), masterdata.getEbook_NamedCustomercountry()));
                            masterdata.setEbook_Sellerstate(concatStateName(masterdata.getEbook_Sellerstate(), masterdata.getEbook_Sellercountry()));
                            masterdata.setEbook_Sellercity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_Sellercity())));
                            masterdata.setEbook_Sellercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Sellercountry())));
                            masterdata.setEbook_Buyerstate(concatStateName(masterdata.getEbook_Buyerstate(), masterdata.getEbook_Buyercountry()));
                            masterdata.setEbook_Buyercity_desc(Validation.checkNull((String) citylist.get(masterdata.getEbook_Buyercity())));
                            masterdata.setEbook_Buyercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Buyercountry())));
                            System.out.println("[ECOM-WAR] ViewEbookingEntry Ebook Seller State " + masterdata.getEbook_Sellerstate()
                                    + " Ebook Seller City Description " + masterdata.getEbook_Sellercity_desc()
                                    + " Ebook Seller Country Description " + masterdata.getEbook_Sellercountry_desc()
                                    + " Ebook Buyer State " + masterdata.getEbook_Buyerstate()
                                    + " Ebook Buyer City Description " + masterdata.getEbook_Buyercity_desc()
                                    + " Ebook Buyer Country Description " + masterdata.getEbook_Buyercountry_desc());

                            masterdata.setEbook_date(masterdata.getEbook_date());

                            if (masterdata.getEbook_pol_etd() != null && masterdata.getEbook_pol_etd().length() > 0) {
                                masterdata.setEbook_pol_etd(masterdata.getEbook_pol_etd());
                            } else {
                                masterdata.setEbook_pol_etd("");
                            }
                            if (masterdata.getEbook_pol_eta() != null && masterdata.getEbook_pol_eta().length() > 0) {
                                masterdata.setEbook_pol_etd(masterdata.getEbook_pol_eta());
                            } else {
                                masterdata.setEbook_pol_eta("");
                            }
                            if (masterdata.getEbook_dateBut() != null && masterdata.getEbook_dateBut().length() > 0) {
                                masterdata.setEbook_dateBut(DateConversion_New.convertDefaultFormat(masterdata.getEbook_dateBut()));
                            }
                            System.out.println("[ECOM-WAR] ViewEbookingEntry >>masterdata.getEmptyPickUpDate()>>" + masterdata.getEmptyPickUpDate());
                            if (masterdata.getEmptyPickUpDate() != null && masterdata.getEmptyPickUpDate().length() > 0) {
                                masterdata.setEmptyPickUpDate(DateConversion_New.convertDefaultFormatTime(masterdata.getEmptyPickUpDate()));
                            }
                            masterdata.setRemarks(Validation.checkNull((String) contrylist.get(masterdata.getEbook_status_remarks())));
                            masterdata.setEbook_haulagetype(masterdata.getEbook_haulagetype());
                            if (masterdata.getEbook_pickup_date() != null && masterdata.getEbook_pickup_date().length() > 0) {
                                masterdata.setEbook_pickup_date(DateConversion_New.convertDefaultFormat(masterdata.getEbook_pickup_date()));
                            } else {
                                masterdata.setEbook_pickup_date("");
                            }
                            if (masterdata.getEbook_drop_date() != null && masterdata.getEbook_drop_date().length() > 0) {
                                masterdata.setEbook_drop_date(DateConversion_New.convertDefaultFormat(masterdata.getEbook_drop_date()));
                            } else {
                                masterdata.setEbook_drop_date("");
                            }
                        }
                        originport = masterdata.getEbook_origin_country().split("-")[0].trim();
                    }
                    if ("1".equalsIgnoreCase(appVersion)) {
                        if (cargolist != null) {
                            int cargo_size = cargolist.size();
                            for (int i = 0; i < cargo_size; i++) {
                                EbookingValidatorFormBeanNew containerdata = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                containerdata.setEbooking_commodity(Validation.checkNull((String) v1ComdtyMap.get(containerdata.getEbook_commoditycode())));
                                containerdata.setAppVersion(appVersion);
                                containerdata.setEbook_packagecode(Validation.checkNull((String) packagemap.get(containerdata.getEbook_packagecode())));
                                containerdata.setEbook_eqp(Validation.checkNull((String) eqpmap.get(containerdata.getEbook_eqp())));
                            }
                        }
                    } else {
                        if (cargolist != null) {
                            int cargo_size = cargolist.size();
                            for (int i = 0; i < cargo_size; i++) {
                                EbookingValidatorFormBeanNew containerdata = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                containerdata.setEbooking_commodity(Validation.checkNull((String) comlist.get(containerdata.getEbook_commoditycode())));
                                containerdata.setAppVersion(appVersion);
                                containerdata.setHsCommodityVal(Validation.checkNull((String) comlist.get(containerdata.getHscode())));
                                containerdata.setEbook_packagecode(Validation.checkNull((String) packagemap.get(containerdata.getEbook_packagecode())));
                                containerdata.setEbook_eqp(Validation.checkNull((String) eqpmap.get(containerdata.getEbook_eqp())));
                            }
                        }
                    }
                    Route routeDTO = null;
                    if (routing_list != null && routing_list.size() > 0) {
                        int routearr_len = routing_list.size();
                        for (int i = 0; i < routearr_len; i++) {
                            routeDTO = (Route) routing_list.get(i);
                            if (routeDTO != null) {
                                routeDTO.setPolPortName((String) portlist.get(Validation.checkNull(routeDTO.getLoadport()).toUpperCase()));
                                routeDTO.setPodPortName((String) portlist.get(Validation.checkNull(routeDTO.getDisport()).toUpperCase()));
                                routeDTO.setServiceName((String) serlist.get(Validation.checkNull(routeDTO.getService()).toUpperCase()));
                                routeDTO.setVesselName((String) vessellist.get(Validation.checkNull(routeDTO.getVessel()).toUpperCase()));
                                routeDTO.setETA1(DateConversion_New.convertDefaultFormatwithTime(routeDTO.getETA1()));
                                routeDTO.setETD1(DateConversion_New.convertDefaultFormatwithTime(routeDTO.getETD1()));
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
                                routing_list.set(i, routeDTO);
                            }
                        }
                    }

                    //get Haulage details
                    tmsmap = bookingObj.getBkgHaulTMSClientDetails(originport);
                    if (haullist != null && haullist.size() > 0) {
                        haullist.forEach(haullis -> {
                            if (haullis.getTmsclientcode() != null && haullis.getTmsclientcode().length() > 0 && tmsmap != null) {
                                haullis.setTmsclientname((String) tmsmap.get(haullis.getTmsclientcode()));
                            }
                            haullis.setDropdate(DateConversion_New.convertDefaultFormatwithTime(haullis.getDropdate()));
                        });
                    }
                    //
                } else {
                    SUCCESS = "sessionex";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cutOffVO == null) {
                cutOffVO = new CutoffConfigVO();
            }

            request.setAttribute("ROUTECUTOFFVO", cutOffVO);
            session.setAttribute("EBOOKING_GETINDEXVALUE", routing_list);
            request.setAttribute("bookinglist", bookinglist);
            if (ics != null) {
                request.setAttribute("EnstDeclarantView", ics);
            } else {
                request.setAttribute("EnstDeclarantView", new ENSDeclarantVo());
            }
            request.setAttribute("cargolist", cargolist);
            request.setAttribute("custMap", custMap);
            request.setAttribute("EBOOKINGHAULAGEDETAILS", haullist);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
