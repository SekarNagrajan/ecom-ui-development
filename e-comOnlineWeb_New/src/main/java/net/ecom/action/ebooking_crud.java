package net.ecom.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.Validation;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.formbeans.EbookingValidatorFormBeanNew;
import net.ecom.lookup.EJBLookUp;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.ecom.AppTierCntrlBean.EcomAmendmentBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.AjaxSplitter;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.Route;
import net.ecom.common.utils.MaskPortUtil;

public class ebooking_crud extends Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomAmendmentBookingBeanRemote amendmentLookUpObj = EJBLookUp.getAmendmentBeanLookUp();
    Map tmsmap = null, stateMap = null, stateMapCountry = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext contexts = servlet.getServletContext();
        String mapping_string = "", type_et = "";
        AjaxSplitter ajx = new AjaxSplitter();
        String cutOffPort = null, cutOffTerminal = null, cutOffDate = null;
        try {
            HttpSession session = request.getSession();
            String usercode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
            Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
            Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            String action_received = Validation.checkNull(request.getParameter("action"));
            String appVersion = request.getParameter("appversion");
            System.out.println("[ECOM-WAR] ebooking_crud >>appVersion> in edit booking>" + appVersion);
            String manageTemplateFlag = Validation.checkNull(request.getParameter("manageTemplate"));
            String TENS_Documentation = (String) request.getServletContext().getAttribute(GeneralConf.TENS_Documentation);
            String clearTemperatureWhileCopy = Validation.checkNull((String) contexts.getAttribute(GeneralConf.CLEAR_TEMPERATURE_WHILE_COPY));
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            System.out.println("[ECOM-WAR] ebooking_crud >>manageTemplateFlag in edit>>" + manageTemplateFlag);
            //V1 Cache
            Map<String, Map<String, String>> masterMap = (Map<String, Map<String, String>>) contexts.getAttribute("ECOMV1MASTERCACHE");
            Map<String, String> v1ComdtyMap = masterMap.get("COMMODITY");
            boolean chkbkgcutoff = false;
            List pluginlis = (List) contexts.getAttribute("LRP_PLUGIN_DETAILS");

            System.out.println("[ECOM-WAR] ebooking_crud (ebooking_crud)Type ===>" + action_received);
            request.setAttribute("EBOOK_FORMACTION", action_received);

            Set bookSet = (Set) session.getAttribute("bookingAccessList");
            String bookNo = Validation.checkNull(request.getParameter("bookno"));
            HashMap custlistMap = null;
            System.out.println("[ECOM-WAR] ebooking_crud BookSet..." + bookNo + "BookSet...Present....." + (bookSet != null && bookSet.contains(bookNo)));
            if ((bookSet != null && bookSet.contains(bookNo) && "edit".equalsIgnoreCase(action_received)) || "template".equalsIgnoreCase(action_received) || "templateedit".equalsIgnoreCase(action_received)) {
                saveToken(request);
                String flag = Validation.checkNull(request.getParameter("flag"));

                if ("edit".equalsIgnoreCase(action_received)) {
                    request.setAttribute("status_type", request.getParameter("type"));
                    session.setAttribute("type", "edit");
                    flag = "D";
                    type_et = "edit";
                } else if ("template".equalsIgnoreCase(action_received)) {
                    session.setAttribute("type", "template");
                    flag = "T";
                    appVersion = "2";
                    type_et = "template";
                } else if ("templateedit".equalsIgnoreCase(action_received)) {
                    session.setAttribute("type", "templateedit");
                    flag = "T";
                    type_et = "template";
                }
                List cargolist_ini = bookingObj.getEbookingCargoDetails(bookNo, flag, clearTemperatureWhileCopy);

                if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                    custlistMap = (HashMap) bookingObj.getEbookingViewCustDetails(bookNo, type_et);
                }

                EbookingValidatorFormBeanNew ebookBeanNew = null;
                ArrayList cargolist = null;
                ArrayList<EbookingValidatorFormBeanNew> buyerCustlist = null;
                ArrayList<EbookingValidatorFormBeanNew> sellerCustlist = null;
                if ("1".equalsIgnoreCase(appVersion)) {
                    if (cargolist_ini != null) {
                        cargolist = new ArrayList();
                        int cargo_ini_size = cargolist_ini.size();
                        for (int i = 0; i < cargo_ini_size; i++) {
                            net.ecom.vo.EbookingValidatorFormBeanNew ebookVo = (net.ecom.vo.EbookingValidatorFormBeanNew) cargolist_ini.get(i);
                            ebookVo.setEbooking_commodity(Validation.checkNull((String) v1ComdtyMap.get(ebookVo.getEbook_commoditycode())));
                            ebookVo.setAppVersion(appVersion);
                            System.out.println("[ECOM-WAR] ebooking_crud ========>>>getEbook_unno" + ebookVo.getEbook_unno());
                            ebookBeanNew = new EbookingValidatorFormBeanNew();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            ebookBeanNew.setNumber(i);
                            cargolist.add(ebookBeanNew);
                        }
                    }

                } else {
                    if (cargolist_ini != null) {
                        cargolist = new ArrayList();
                        int cargo_ini_size = cargolist_ini.size();
                        for (int i = 0; i < cargo_ini_size; i++) {
                            net.ecom.vo.EbookingValidatorFormBeanNew ebookVo = (net.ecom.vo.EbookingValidatorFormBeanNew) cargolist_ini.get(i);
                            ebookVo.setEbooking_commodity(Validation.checkNull((String) comlist.get(ebookVo.getEbook_commoditycode())));
                            ebookVo.setAppVersion(appVersion);
                            ebookVo.setHsCommodityVal(Validation.checkNull((String) comlist.get(ebookVo.getHscode())));
                            System.out.println("[ECOM-WAR] ebooking_crud ========>>>getEbook_unno" + ebookVo.getEbook_unno());
                            ebookBeanNew = new EbookingValidatorFormBeanNew();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            ebookBeanNew.setNumber(i);
                            cargolist.add(ebookBeanNew);
                        }
                    }
                    if (custlistMap != null && custlistMap.containsKey("SL")) {
                        sellerCustlist = new ArrayList<EbookingValidatorFormBeanNew>();
                        ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew> custlist_ini = (ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew>) custlistMap.get("SL");
                        int cust_ini_size = custlist_ini.size();
                        for (int i = 0; i < cust_ini_size; i++) {
                            net.ecom.vo.EbookingValidatorFormBeanNew ebookVo = (net.ecom.vo.EbookingValidatorFormBeanNew) custlist_ini.get(i);
                            ebookVo.setAppVersion(appVersion);
                            if (Validation.checkNull(ebookVo.getEbook_Custcountry()).length() > 0) {

                                ebookVo.setEbook_Custstate(concatStateName(ebookVo.getEbook_Custstate(), ebookVo.getEbook_Custcountry()));
                                System.out.println("state=>" + ebookVo.getEbook_Custstate() + "===>" + ebookVo.getEbook_Custcountry() + "///" + ebookVo.getEbook_Custstate());
                                ebookVo.setEbook_Custcountry_desc(ebookVo.getEbook_Custcountry() + " - " + (Validation.checkNull((String) contrylist.get(ebookVo.getEbook_Custcountry()))).toUpperCase());
                            }
                            ebookBeanNew = new EbookingValidatorFormBeanNew();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            sellerCustlist.add(ebookBeanNew);
                        }
                    }
                    if (custlistMap != null && custlistMap.containsKey("BY")) {
                        buyerCustlist = new ArrayList<EbookingValidatorFormBeanNew>();
                        ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew> custlist_ini = (ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew>) custlistMap.get("BY");
                        int cust_ini_size = custlist_ini.size();
                        for (int i = 0; i < cust_ini_size; i++) {
                            net.ecom.vo.EbookingValidatorFormBeanNew ebookVo = (net.ecom.vo.EbookingValidatorFormBeanNew) custlist_ini.get(i);
                            ebookVo.setAppVersion(appVersion);
                            if (Validation.checkNull(ebookVo.getEbook_Custcountry()).length() > 0) {
                                ebookVo.setEbook_Custstate(concatStateName(ebookVo.getEbook_Custstate(), ebookVo.getEbook_Custcountry()));
                                System.out.println("state=>" + ebookVo.getEbook_Custstate() + "===>" + ebookVo.getEbook_Custcountry() + "///" + ebookVo.getEbook_Custstate());
                                ebookVo.setEbook_Custcountry_desc(ebookVo.getEbook_Custcountry() + " - " + (Validation.checkNull((String) contrylist.get(ebookVo.getEbook_Custcountry()))).toUpperCase());
                            }
                            ebookBeanNew = new EbookingValidatorFormBeanNew();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            buyerCustlist.add(ebookBeanNew);
                        }
                    }
                }
                session.setAttribute("ebooking_cargo_grid", cargolist);
                session.setAttribute("Draft_ebooking_cargo_grid", cargolist_ini);
                session.setAttribute("ebooking_seller_grid", sellerCustlist);
                session.setAttribute("ebooking_buyere_grid", buyerCustlist);
                // session.setAttribute("Draft_ebooking_cargo_grid", cargolist_ini);
                //
                String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                List personalInfo = null;
                personalInfo = ecomCntrlObj.getAPDetails(userCode);
                request.setAttribute("PersonalInfo1", personalInfo);
                if (personalInfo != null && personalInfo.size() >= 6) {
                    session.setAttribute("APEMAILID", personalInfo.get(5));
                }
                //
                ArrayList master_list = bookingObj.getEbookingDetails(bookNo, flag, type_et);
                ENSDeclarantVo ics = bookingObj.getENSTabDetails(bookNo);
                net.ecom.vo.EbookingValidatorFormBeanNew masterdata = null;
                if (master_list != null) {
                    int masterDetailsSize = master_list.size();
                    for (int i = 0; i < masterDetailsSize; i++) {
                        masterdata = (net.ecom.vo.EbookingValidatorFormBeanNew) master_list.get(i);
                        masterdata.setHidEcomBookNo(bookNo);
                        cutOffPort = Validation.checkNull(masterdata.getEbook_portofloading());
                        cutOffTerminal = Validation.checkNull(masterdata.getEbook_terminal());
                        masterdata.setEbook_delivery_country(MaskPortUtil.swapActualPortToMaskPortWithName(masterdata.getEbook_delivery() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_delivery().toUpperCase()))));
                        masterdata.setEbook_origin_country(MaskPortUtil.swapActualPortToMaskPortWithName(masterdata.getEbook_origin() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_origin().toUpperCase()))));
                        masterdata.setEbook_pol_country(Validation.checkNull((String) portlist.get(masterdata.getEbook_pol().toUpperCase())));
                        masterdata.setEbook_pod_country(Validation.checkNull((String) portlist.get(masterdata.getEbook_pod().toUpperCase())));
                        masterdata.setEbook_shippercity_desc(masterdata.getEbook_shippercity());
                        masterdata.setEbook_shippercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_shippercountry())));
                        masterdata.setEbook_consigneecity_desc(masterdata.getEbook_consigneecity());
                        masterdata.setEbook_consigneecountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_consigneecountry())));
                        masterdata.setEbook_forwardercity_desc(masterdata.getEbook_forwardercity());
                        masterdata.setEbook_forwardercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_forwardercountry())));
                        masterdata.setEbook_Warehousecity_desc(masterdata.getEbook_Warehousecity());
                        masterdata.setEbook_Warehousecountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Warehousecountry())));
                        masterdata.setEbook_NamedCustomercity_desc(masterdata.getEbook_NamedCustomercity());
                        masterdata.setEbook_NamedCustomercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_NamedCustomercountry())));

                        masterdata.setEbook_notifycity_desc(masterdata.getEbook_notifycity());
                        masterdata.setEbook_notifycountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_notifycountry())));
                        masterdata.setEbook_notifycity_desc_n1(masterdata.getEbook_notifycity_n1());
                        masterdata.setEbook_notifycountry_desc_n1(Validation.checkNull((String) contrylist.get(masterdata.getEbook_notifycountry_n1())));
                        masterdata.setEbook_portofloading_desc(masterdata.getEbook_pol() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_pol().toUpperCase())));
                        masterdata.setEbook_portofdischarge_desc(masterdata.getEbook_pod() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_pod().toUpperCase())));
                        masterdata.setEbook_routeid(masterdata.getEbook_routeid());

                        masterdata.setEbook_agreestate(concatStateName(masterdata.getEbook_agreestate(), masterdata.getEbook_agreecountry()));
                        masterdata.setEbook_bookingstate(concatStateName(masterdata.getEbook_bookingstate(), masterdata.getEbook_bookingcountry()));
                        masterdata.setEbook_shipperstate(concatStateName(masterdata.getEbook_shipperstate(), masterdata.getEbook_shippercountry()));
                        masterdata.setEbook_consigneestate(concatStateName(masterdata.getEbook_consigneestate(), masterdata.getEbook_consigneecountry()));
                        masterdata.setEbook_forwarderstate(concatStateName(masterdata.getEbook_forwarderstate(), masterdata.getEbook_forwardercountry()));
                        masterdata.setEbook_Warehousestate(concatStateName(masterdata.getEbook_Warehousestate(), masterdata.getEbook_Warehousecountry()));
                        masterdata.setEbook_notifystate(concatStateName(masterdata.getEbook_notifystate(), masterdata.getEbook_notifycountry()));
                        masterdata.setEbook_notifystate_n1(concatStateName(masterdata.getEbook_notifystate_n1(), masterdata.getEbook_notifycountry_n1()));
                        masterdata.setEbook_NamedCustomerstate(concatStateName(masterdata.getEbook_NamedCustomerstate(), masterdata.getEbook_NamedCustomercountry()));
                        masterdata.setEbook_Sellercity_desc(masterdata.getEbook_Sellercity());
                        masterdata.setEbook_Sellercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Sellercountry())));
                        System.out.println("[ECOM-WAR] ebooking_crud Seller" + masterdata.getEbook_Sellercountry_desc() + "//" + masterdata.getEbook_Sellercountry() + "//" + (String) contrylist.get(masterdata.getEbook_Sellercountry()));
                        masterdata.setEbook_Sellerstate(concatStateName(masterdata.getEbook_Sellerstate(), masterdata.getEbook_Sellercountry()));
                        masterdata.setEbook_Buyercity_desc(masterdata.getEbook_Buyercity());
                        masterdata.setEbook_Buyercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Buyercountry())));
                        masterdata.setEbook_Buyerstate(concatStateName(masterdata.getEbook_Buyerstate(), masterdata.getEbook_Buyercountry()));

                        masterdata.setAppVersion(appVersion);
                        //
                        if (EcomConsts.FILETYPE_VMG.equalsIgnoreCase(masterdata.getFilelabel1())) {
                            masterdata.setFilename1(masterdata.getFilename1());
                        }
                        if (EcomConsts.FILETYPE_DGC.equalsIgnoreCase(masterdata.getFilelabel2())) {
                            masterdata.setFilename2(masterdata.getFilename2());
                        }
                        if (EcomConsts.FILETYPE_LOL.equalsIgnoreCase(masterdata.getFilelabel3())) {
                            masterdata.setFilename3(masterdata.getFilename3());
                        }
                        if (EcomConsts.FILETYPE_MSD.equalsIgnoreCase(masterdata.getFilelabel4())) {
                            masterdata.setFilename4(masterdata.getFilename4());
                        }
                        if (EcomConsts.FILETYPE_OTHERS.equalsIgnoreCase(masterdata.getFilelabel5())) {
                            masterdata.setFilename5(masterdata.getFilename5());
                        }
                    }

                    Route routeDTO = null;
                    boolean checkCutOffDate = true;
                    ArrayList routing_list = bookingObj.getEbookingRoutingDetailsfrombookingleg(bookNo);
                    System.out.println("[ECOM-WAR] ebooking_crud >>routing_list>ecom>>" + routing_list);
                    if (routing_list != null && routing_list.size() > 0) {
                        int routearr_len = routing_list.size();
                        String pol_str = null, pod_str = null;
                        String temp = null;
                        ArrayList ars = ajx.FindLordporttype(routing_list);
                        for (int i = 0; i < routearr_len; i++) {
                            routeDTO = (Route) routing_list.get(i);
                            if (routeDTO != null) {
                                temp = "N";
                                pol_str = (String) ars.get(0);
                                pod_str = (String) ars.get(1);
                                String lordporttype = Validation.checkNull(bookingObj.getPortType(routeDTO.getLoadport()));
                                String disporttype = Validation.checkNull(bookingObj.getPortType(routeDTO.getDisport()));
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
                                    System.out.println("[ECOM-WAR] ebooking_crud >>>routeDTO.getETA1()>>" + routeDTO.getETA1());
                                    masterdata.setEbook_date(routeDTO.getETD1());
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
//                                routeDTO.setPreCarriage(preCarriage);
//                                routeDTO.setOnCarriage(onCarriage);
                                routeDTO.setLordporttype(lordporttype);
                                routeDTO.setDisporttype(disporttype);
                                routeDTO.setPolPortName((String) portlist.get(routeDTO.getLoadport().toUpperCase()));
                                routeDTO.setPodPortName((String) portlist.get(routeDTO.getDisport().toUpperCase()));
                                routeDTO.setServiceName((String) serlist.get(routeDTO.getService().toUpperCase()));
                                routeDTO.setVesselName((String) vessellist.get(routeDTO.getVessel().toUpperCase()));
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
                    System.out.println("[ECOM-WAR] ebooking_crud CutOffPort " + cutOffPort + " CutOffTerminal " + cutOffTerminal + " CutOffDate " + cutOffDate);
                    if (pluginlis != null && pluginlis.contains("BEC")) {
                        chkbkgcutoff = true;
                        cutOffVO = bookingObj.getEcomBookingConfDetails(bookNo);
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
                    List<BookingHaulagedetailsVo> haullist = bookingObj.getEcomBookingHaulageDetails(bookNo);
                    tmsmap = bookingObj.getBkgHaulTMSClientDetails(masterdata.getEbook_origin_country().split("-")[0].trim());
                    if (haullist != null && haullist.size() > 0) {
                        haullist.forEach(haullis -> {
                            if (haullis.getTmsclientcode() != null && haullis.getTmsclientcode().length() > 0 && tmsmap != null) {
                                haullis.setTmsclientname((String) tmsmap.get(haullis.getTmsclientcode()));
                            }
                            haullis.setDropdate(DateConversion_New.convertDefaultFormatwithTime(haullis.getDropdate()));
                        });
                    }

                    if ("D".equalsIgnoreCase(flag)) {

                        List filenamelist = ecomCntrlObj.getSubmittedFileNames(bookNo, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH), "Ecom");
                        request.setAttribute("FILENAMELIST", filenamelist);
                    }
                    //s
                    session.setAttribute("master_list", master_list);
                    session.setAttribute("EnsDeclarant_ICS", ics);
                    request.setAttribute("ISBKGCUTOFF", chkbkgcutoff);
                    request.setAttribute("ROUTECUTOFFVO", cutOffVO);
                    session.setAttribute("EBOOKING_GETINDEXVALUE", routing_list);
                    request.setAttribute("MANAGETEMPLATE", manageTemplateFlag);
                    session.setAttribute("EBOOKING_GETINDEXVALUEROUTE", new ArrayList());
                    session.setAttribute("route", null);
                    request.setAttribute("EBOOKINGHAULAGEDETAILS", haullist);
                    mapping_string = "EDIT_BOOKING";
                } else {
                    ArrayList view = null;
                    String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
                    view = bookingObj.getBookingDetail(usercode, "", "", "A", custTypeConfig, v1DataEnableStatus);
                    session.setAttribute("BookingView", view);
                    mapping_string = "viewAll";
                }
                System.out.println("[ECOM-WAR] ebooking_crud preferred=>" + masterdata.getEbook_preferred_agency());
               
            } else if ("reuse".equalsIgnoreCase(action_received)) {
                session.setAttribute("type", "template");
                appVersion = "2";
                String ecomBookNo = Validation.checkNull(request.getParameter("ecomBookNo"));
                String lrpBookNo = Validation.checkNull(request.getParameter("lrpbookno"));
                String type = Validation.checkNull(request.getParameter("book_type"));
                System.out.println("[ECOM-WAR] ebooking_crud bookSet.contains(ecomBookNo)....." + bookSet.contains(ecomBookNo) + "bookSet.contains(lrpBookNo)....." + bookSet.contains(lrpBookNo));
                if ((bookSet != null && bookSet.contains(ecomBookNo)) || (bookSet != null && bookSet.contains(lrpBookNo))) {
                    net.ecom.vo.EbookingValidatorFormBeanNew masterdata = new net.ecom.vo.EbookingValidatorFormBeanNew();
                    AmendmentBookingFormBean bookingVO = amendmentLookUpObj.getLRPBookingMasterDetails(lrpBookNo);
                    bookingVO.setBooking_refno("");
                    bookingVO.setHidbkgtype("reusebkg");
                    bookingVO.setSaveStatus("");
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
                    BeanUtils.copyProperties(masterdata, bookingVO);

                    masterdata.setEbook_delivery_country(MaskPortUtil.swapActualPortToMaskPortWithName(masterdata.getEbook_delivery() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_delivery().toUpperCase()))));
                    masterdata.setEbook_origin_country(MaskPortUtil.swapActualPortToMaskPortWithName(masterdata.getEbook_origin() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_origin().toUpperCase()))));
                    masterdata.setEbook_pol_country(Validation.checkNull((String) portlist.get(masterdata.getEbook_pol().toUpperCase())));
                    masterdata.setEbook_pod_country(Validation.checkNull((String) portlist.get(masterdata.getEbook_pod().toUpperCase())));
                    masterdata.setEbook_shippercity_desc(masterdata.getEbook_shippercity());
                    masterdata.setEbook_shippercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_shippercountry())));
                    masterdata.setEbook_consigneecity_desc(masterdata.getEbook_consigneecity());
                    masterdata.setEbook_consigneecountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_consigneecountry())));
                    masterdata.setEbook_forwardercity_desc(masterdata.getEbook_forwardercity());
                    masterdata.setEbook_forwardercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_forwardercountry())));
                    masterdata.setEbook_Warehousecity_desc(masterdata.getEbook_Warehousecity());
                    masterdata.setEbook_Warehousecountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Warehousecountry())));
                    masterdata.setEbook_NamedCustomercity_desc(masterdata.getEbook_NamedCustomercity());
                    masterdata.setEbook_NamedCustomercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_NamedCustomercountry())));

                    masterdata.setEbook_notifycity_desc(masterdata.getEbook_notifycity());
                    masterdata.setEbook_notifycountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_notifycountry())));
                    masterdata.setEbook_notifycity_desc_n1(masterdata.getEbook_notifycity_n1());
                    masterdata.setEbook_notifycountry_desc_n1(Validation.checkNull((String) contrylist.get(masterdata.getEbook_notifycountry_n1())));
                    masterdata.setEbook_portofloading_desc(masterdata.getEbook_pol() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_pol().toUpperCase())));
                    masterdata.setEbook_portofdischarge_desc(masterdata.getEbook_pod() + " - " + Validation.checkNull((String) portlist.get(masterdata.getEbook_pod().toUpperCase())));
                    masterdata.setEbook_routeid(masterdata.getEbook_routeid());

                    masterdata.setEbook_agreestate(concatStateName(masterdata.getEbook_agreestate(), masterdata.getEbook_agreecountry()));
                    masterdata.setEbook_bookingstate(concatStateName(masterdata.getEbook_bookingstate(), masterdata.getEbook_bookingcountry()));
                    masterdata.setEbook_shipperstate(concatStateName(masterdata.getEbook_shipperstate(), masterdata.getEbook_shippercountry()));
                    masterdata.setEbook_consigneestate(concatStateName(masterdata.getEbook_consigneestate(), masterdata.getEbook_consigneecountry()));
                    masterdata.setEbook_forwarderstate(concatStateName(masterdata.getEbook_forwarderstate(), masterdata.getEbook_forwardercountry()));
                    masterdata.setEbook_Warehousestate(concatStateName(masterdata.getEbook_Warehousestate(), masterdata.getEbook_Warehousecountry()));
                    masterdata.setEbook_notifystate(concatStateName(masterdata.getEbook_notifystate(), masterdata.getEbook_notifycountry()));
                    masterdata.setEbook_notifystate_n1(concatStateName(masterdata.getEbook_notifystate_n1(), masterdata.getEbook_notifycountry_n1()));
                    masterdata.setEbook_NamedCustomerstate(concatStateName(masterdata.getEbook_NamedCustomerstate(), masterdata.getEbook_NamedCustomercountry()));
                    masterdata.setEbook_Sellercity_desc(masterdata.getEbook_Sellercity());
                    masterdata.setEbook_Sellercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Sellercountry())));
                    masterdata.setEbook_Sellerstate(concatStateName(masterdata.getEbook_Sellerstate(), masterdata.getEbook_Sellercountry()));
                    masterdata.setEbook_Buyercity_desc(masterdata.getEbook_Buyercity());
                    masterdata.setEbook_Buyercountry_desc(Validation.checkNull((String) contrylist.get(masterdata.getEbook_Buyercountry())));
                    masterdata.setEbook_Buyerstate(concatStateName(masterdata.getEbook_Buyerstate(), masterdata.getEbook_Buyercountry()));

                    masterdata.setAppVersion(appVersion);
                    masterdata.setCargoprotectid(null);
                    System.out.println("[ECOM-WAR] ebooking_crud preferred=>" + masterdata.getEbook_preferred_agency());

                    ArrayList master_list = new ArrayList();
                    master_list.add(masterdata);
                    if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                        custlistMap = (HashMap) bookingObj.getLRPBookingCutomerDetails(lrpBookNo);
                    }
                    ENSDeclarantVo ics = amendmentLookUpObj.getLRPBookingENSTabDetails(lrpBookNo);
                    EbookingValidatorFormBeanNew ebookBeanNewCut = null;
                    ArrayList<EbookingValidatorFormBeanNew> buyerCustlist = null;
                    ArrayList<EbookingValidatorFormBeanNew> sellerCustlist = null;
                    if (custlistMap != null && custlistMap.containsKey("SL")) {
                        sellerCustlist = new ArrayList<EbookingValidatorFormBeanNew>();
                        ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew> custlist_ini = (ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew>) custlistMap.get("SL");
                        int cust_ini_size = custlist_ini.size();
                        for (int i = 0; i < cust_ini_size; i++) {
                            net.ecom.vo.EbookingValidatorFormBeanNew ebookVo = (net.ecom.vo.EbookingValidatorFormBeanNew) custlist_ini.get(i);
                            ebookVo.setAppVersion(appVersion);
                            if (Validation.checkNull(ebookVo.getEbook_Custcountry()).length() > 0) {

                                ebookVo.setEbook_Custstate(concatStateName(ebookVo.getEbook_Custstate(), ebookVo.getEbook_Custcountry()));
                                System.out.println("state=>" + ebookVo.getEbook_Custstate() + "===>" + ebookVo.getEbook_Custcountry() + "///" + ebookVo.getEbook_Custstate());
                                ebookVo.setEbook_Custcountry_desc(ebookVo.getEbook_Custcountry() + " - " + (Validation.checkNull((String) contrylist.get(ebookVo.getEbook_Custcountry()))).toUpperCase());
                            }
                            ebookBeanNewCut = new EbookingValidatorFormBeanNew();
                            BeanUtils.copyProperties(ebookBeanNewCut, ebookVo);
                            sellerCustlist.add(ebookBeanNewCut);
                        }
                    }
                    if (custlistMap != null && custlistMap.containsKey("BY")) {
                        buyerCustlist = new ArrayList<EbookingValidatorFormBeanNew>();
                        ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew> custlist_ini = (ArrayList<net.ecom.vo.EbookingValidatorFormBeanNew>) custlistMap.get("BY");
                        int cust_ini_size = custlist_ini.size();
                        for (int i = 0; i < cust_ini_size; i++) {
                            net.ecom.vo.EbookingValidatorFormBeanNew ebookVo = (net.ecom.vo.EbookingValidatorFormBeanNew) custlist_ini.get(i);
                            ebookVo.setAppVersion(appVersion);
                            if (Validation.checkNull(ebookVo.getEbook_Custcountry()).length() > 0) {
                                ebookVo.setEbook_Custstate(concatStateName(ebookVo.getEbook_Custstate(), ebookVo.getEbook_Custcountry()));
                                System.out.println("state=>" + ebookVo.getEbook_Custstate() + "===>" + ebookVo.getEbook_Custcountry() + "///" + ebookVo.getEbook_Custstate());
                                ebookVo.setEbook_Custcountry_desc(ebookVo.getEbook_Custcountry() + " - " + (Validation.checkNull((String) contrylist.get(ebookVo.getEbook_Custcountry()))).toUpperCase());
                            }
                            ebookBeanNewCut = new EbookingValidatorFormBeanNew();
                            BeanUtils.copyProperties(ebookBeanNewCut, ebookVo);
                            buyerCustlist.add(ebookBeanNewCut);
                        }
                    }
                    session.setAttribute("ebooking_seller_grid", sellerCustlist);
                    session.setAttribute("ebooking_buyere_grid", buyerCustlist);
                    List<BookingHaulagedetailsVo> haullist = null;
                    net.ecom.vo.AmendmentBookingFormBean ebookVo = null;
                    List cargoList = amendmentLookUpObj.getLRPBookingViewCargoDetails(lrpBookNo, type, ecomBookNo, clearTemperatureWhileCopy);
                    if (cargoList != null && cargoList.size() > 0) {
                        ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoList.get(cargoList.size() - 1);
                        HashMap cargonomap = ebookVo.getCargonomap();
                        System.out.println("[ECOM-WAR] ebooking_crud ^^^^^^cargonomap^^^^ " + cargonomap);
                        haullist = amendmentLookUpObj.getLRPBookingHaulageDetails(lrpBookNo, type, cargonomap);
                    }
                    List finalcargoreuselis = new ArrayList();
                    if (cargoList != null) {
                        List eqptypelis = new ArrayList();
                        List cargoreuselis = new ArrayList();
                        Set eqptypeset = new HashSet();
                        Map eqpcount = new HashMap();
                        for (int i = 0; i < cargoList.size(); i++) {
                            ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoList.get(i);
                            if (!eqptypelis.contains(ebookVo.getEbook_eqp())) {
                                cargoreuselis.add(ebookVo);
                            }
                            eqptypelis.add(ebookVo.getEbook_eqp());
                            eqptypeset.add(ebookVo.getEbook_eqp());
                        }
                        System.out.println("[ECOM-WAR] ebooking_crud eqptypelis=> " + eqptypelis + " eqptypeset=> " + eqptypeset);
                        Iterator eqpitr = eqptypeset.iterator();
                        while (eqpitr.hasNext()) {
                            String eqpid = (String) eqpitr.next();
                            System.out.println("[ECOM-WAR] ebooking_crud eqpid==> " + eqpid + " => " + Collections.frequency(eqptypelis, eqpid));
                            eqpcount.put(eqpid, Collections.frequency(eqptypelis, eqpid));
                        }
                        EbookingValidatorFormBeanNew ebookBeanNew = null;
                        for (int j = 0; j < cargoreuselis.size(); j++) {
                            ebookVo = (net.ecom.vo.AmendmentBookingFormBean) cargoreuselis.get(j);
                            ebookBeanNew = new EbookingValidatorFormBeanNew();
                            BeanUtils.copyProperties(ebookBeanNew, ebookVo);
                            ebookBeanNew.setNumber(j);
                            ebookBeanNew.setContainerNo("");
                            ebookBeanNew.setEbook_cargoweight("");
                            ebookBeanNew.setEbook_eqp_qty((eqpcount.get(ebookBeanNew.getEbook_eqp()) + ""));
                            finalcargoreuselis.add(ebookBeanNew);
                        }
                    }
                    if (finalcargoreuselis != null) {
                        List cargolist = new ArrayList();
                        //EbookingValidatorFormBeanNew ebookBeanNew = null;
                        int cargo_ini_size = finalcargoreuselis.size();
                        for (int i = 0; i < cargo_ini_size; i++) {
                            EbookingValidatorFormBeanNew ebookVo1 = (EbookingValidatorFormBeanNew) finalcargoreuselis.get(i);
                            ebookVo1.setEbooking_commodity(Validation.checkNull((String) comlist.get(ebookVo.getEbook_commoditycode())));
                            ebookVo1.setAppVersion(appVersion);
                            ebookVo1.setHsCommodityVal(Validation.checkNull((String) comlist.get(ebookVo.getHscode())));
                            System.out.println("[ECOM-WAR] ebooking_crud ========>>>getEbook_unno" + ebookVo1.getEbook_unno());
                            // ebookBeanNew = new EbookingValidatorFormBeanNew();
                            // BeanUtils.copyProperties(ebookBeanNew, ebookVo1);
                            ebookVo1.setNumber(i);
                            cargolist.add(ebookVo1);
                        }
                        session.setAttribute("ebooking_cargo_grid", cargolist);
                        session.setAttribute("Draft_ebooking_cargo_grid", finalcargoreuselis);
                    }
                    List<BookingHaulagedetailsVo> finalhaullis = new ArrayList<BookingHaulagedetailsVo>();
                    if (haullist != null) {
                        haullist = haullist.stream().sorted((o1, o2) -> o1.getEqptype().compareTo(o2.getEqptype())).collect(Collectors.toList());
                        List heqptypelis = new ArrayList();
                        int cc = 0, eqpc = 1;
                        for (int i = 0; i < haullist.size(); i++) {
                            BookingHaulagedetailsVo hvo = (BookingHaulagedetailsVo) haullist.get(i);
                            if (!heqptypelis.contains(hvo.getEqptype())) {
                                eqpc = 1;
                                cc = cc + 1;
                                hvo.setEqprowid(cc);
                                hvo.setDummyeqp("TNB" + eqpc);

                                finalhaullis.add(hvo);
                            } else {
                                hvo.setEqprowid(cc);
                                hvo.setDummyeqp("TNB" + eqpc);

                                finalhaullis.add(hvo);
                            }

                            eqpc = eqpc + 1;
                            heqptypelis.add(hvo.getEqptype());
                        }
                    }
                    if (finalhaullis != null && finalhaullis.size() > 0) {
                        finalhaullis.forEach(haullis -> {
                            haullis.setDropdate(DateConversion_New.convertDefaultFormatwithTime(haullis.getDropdate()));
                        });
                    }
                    //
                    String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                    List personalInfo = null;
                    personalInfo = ecomCntrlObj.getAPDetails(userCode);
                    request.setAttribute("PersonalInfo1", personalInfo);
                    if (personalInfo != null && personalInfo.size() >= 6) {
                        session.setAttribute("APEMAILID", personalInfo.get(5));
                    }
                    //session.setAttribute("type", "edit");
                    session.setAttribute("master_list", master_list);
                    session.setAttribute("EnsDeclarant_ICS", ics);
                    request.setAttribute("ISBKGCUTOFF", chkbkgcutoff);
                    //request.setAttribute("ROUTECUTOFFVO", cutOffVO);
                    session.setAttribute("EBOOKING_GETINDEXVALUE", null);
                    //request.setAttribute("MANAGETEMPLATE", manageTemplateFlag);
                    //session.setAttribute("EBOOKING_GETINDEXVALUEROUTE", new ArrayList());
                    //session.setAttribute("route", null);
                    request.setAttribute("EBOOKINGHAULAGEDETAILS", finalhaullis);
                    mapping_string = "EDIT_BOOKING";
                } else {
                    mapping_string = "sessionex";
                }
            } else {
                mapping_string = "sessionex";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM-WAR] ebooking_crud mapping_string.........:" + mapping_string);
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
