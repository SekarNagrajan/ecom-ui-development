/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAmendmentBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.CommonMail;
import net.ecom.common.CommonMailForAgency;
import net.ecom.common.CommonMailForCHCust;
import net.ecom.common.GeneralConf;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import static net.ecom.common.Validation.checkNull;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.FileExtensionValidation;
import net.ecom.common.utils.InsuranceUtils;
import net.ecom.formbeans.AmendmentBookingFormBean;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.EbookingViewAll;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.MaskPortVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;
import net.ecom.common.utils.MaskPortUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author deepa.p
 */
public class BKGAmemdmentSaveAction extends Action {

    EcomAmendmentBookingBeanRemote ecomAmendmentObj = EJBLookUp.getAmendmentBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        FormFile file1 = null, file2 = null, file3 = null, file4 = null, file5 = null;
        String loginUser = null, cust_code = null, routehidden = null;
        String depdate = null, arrdate = null, strPol = null, strPod = null, strPla = null, strPld = null, strServiceCode = null;
        ArrayList routingList = null;
        String str_type = "N", slottype = null;
        //Cargo strings
        String SaveType = null, eqpType = null, eqpSize = null, eqpQty = null, soc = null, hzd = null, commodity = null, packagename = null, hscode = null;
        String packageQty = null, eqpStatus = null, tareWt = null, cargoWt = null, netWt = null, grossWt = null, volume = null, netWtUnit = null, cargoWtUnit = null, actCommoName = null, cmdtyhblno = null, sellersName = null, buyersName = null, buyersCode = null, sellersCode = null;
        String volUnit = null, eqp_type = null, marinePollut = null, unno = null, flashPoint = null, flashUnit = null, commodityothers = null;
        String shippingName = null, dgClass = null, presetTemp = null, minTemp = null, maxTemp = null, tempUnit = null, nor = null;
        String dimUnit = null, owLeft = null, owRight = null, oh = null, olAft = null, olForward = null;
        String setType = null, checkother = null, containerNo = null;
        String hidden_oog = null, hidden_lcl = null, hidden_rfr = null, packagecodes = null, packagecode = null, commoditycode = null, commodities = null;
        String comid = null, conid = null, isdummy = null, isFakComdty = null, othercommodity = null, otherhscode = null, commodityname = null, hscommoditycode = null, hscommodities = null;
        String humidityflag = null, humidity = null, drains = null, co2 = null, o2 = null, ventilation = null;
        Route routeVO = null;
        String addccMailid = null;
        String file_path = null, message = null;
        FileOutputStream fos = null;
        String file_name1 = null, file_name2 = null, file_name3 = null, file_name4 = null, file_name5 = null, fileNames = null;
        int selectLen = 0;
        List<net.ecom.vo.AmendmentBookingFormBean> containerList = null;
        net.ecom.vo.AmendmentBookingFormBean amendmentCargoVO = null;
        Map<String, List<net.ecom.vo.AmendmentBookingFormBean>> CustomerMap = null;
        net.ecom.vo.AmendmentBookingFormBean amendmentCustomerVo = null;
        ArrayList<net.ecom.vo.AmendmentBookingFormBean> sellerList = null;
        ArrayList<net.ecom.vo.AmendmentBookingFormBean> BuyerList = null;
        boolean flag = false;
        ReturnVO returnVO = null;
        String client_code = null, unnos = null, unnocode = null, Ebkg_type = null;
        try {
            HttpSession session = request.getSession();
            ServletContext contexts = servlet.getServletContext();
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
//             System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN"));
//             System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN"));
            System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN") + ", token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN"));

            String sessionToken = (String) session.getAttribute("org.apache.struts.action.TOKEN");
            if (isTokenValid(request)) {
                //Get user details from session
                Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
                loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                cust_code = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                String contactName = Validation.checkNull((String) session.getAttribute("SESSION_USERNAME"));
                String contactPhone = Validation.checkNull((String) session.getAttribute("SESSION_USER_PHONE"));
                String useremail = (String) session.getAttribute("SESSION_EMAIL");

                client_code = (String) contexts.getAttribute("client_code");
                resetToken(request);
                String custCountry = "";
                if (!"Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.Disable_Lang_Mail))) {
                    custCountry = Validation.checkNull((String) session.getAttribute("SESSION_USER_COUNTRY"));
                }
                String custCompany = Validation.checkNull((String) session.getAttribute("SESSION_USER_CONTACT"));
                String apname = (String) session.getAttribute("apname");
                Map<String, String> emptyPickUpMap = (Map<String, String>) contexts.getAttribute("EMPTYPICKUPMAP");
                //
                AmendmentBookingFormBean amendmentFormBean = (AmendmentBookingFormBean) form;
                net.ecom.vo.AmendmentBookingFormBean amendmentVO = new net.ecom.vo.AmendmentBookingFormBean();
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>templatename>>" + amendmentVO.getEbook_templatename());
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        amendmentFormBean.getBooking_refno(>>)>>" + amendmentFormBean.getBooking_refno());
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        amendmentFormBean.getEbkg_type(>>)>>" + amendmentFormBean.getEbkg_type());
                String strtmpname = "";
                String fakCommodity = (String) contexts.getAttribute(GeneralConf.FAKCOMMODITY);
                String fakComdtyCode = "";
                String fakComdtyName = "";
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        fakCommodity>>>>" + fakCommodity);
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        templatename: " + amendmentVO.getEbook_templatename() + ", booking_refno: " + amendmentFormBean.getBooking_refno() + ", ebkg_type: " + amendmentFormBean.getEbkg_type() + ", fakCommodity: " + fakCommodity);

                fakCommodity = fakCommodity != null ? fakCommodity : "";
                if (fakCommodity != null && fakCommodity.length() > 0) {
                    String fakCommodityArr[] = fakCommodity.split("~~");
                    if (fakCommodityArr.length == 2) {
                        fakComdtyCode = fakCommodityArr[0].trim();
                        fakComdtyName = fakCommodityArr[1].trim();
                    }
                }
                amendmentFormBean.setShipment_type("NR");
                String btn_type = amendmentFormBean.getSubmit_btn();
                strtmpname = request.getParameter("hdntempaltename");
                amendmentVO.setEbook_templatename(strtmpname);
                amendmentVO.setEnableMultiAgency((String) contexts.getAttribute(GeneralConf.ENABLE_MULTIAGENCY));
                strtmpname = amendmentVO.getEbook_templatename().toString();
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>strtmpname===>>" + strtmpname);
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>btn_type===>>" + btn_type + ",strtmpname:" + strtmpname);
                file1 = amendmentFormBean.getFile_path1();
                file2 = amendmentFormBean.getFile_path2();
                file3 = amendmentFormBean.getFile_path3();
                file4 = amendmentFormBean.getFile_path4();
                file5 = amendmentFormBean.getFile_path5();
                FormFile file6 = amendmentFormBean.getFile_path6();
                FormFile file7 = amendmentFormBean.getFile_path7();
                FormFile file8 = amendmentFormBean.getFile_path8();
                FormFile file9 = amendmentFormBean.getFile_path9();
                FormFile file10 = amendmentFormBean.getFile_path10();
                FormFile file11 = amendmentFormBean.getFile_path11();
                FormFile file12 = amendmentFormBean.getFile_path12();
                FormFile file13 = amendmentFormBean.getFile_path13();
                FormFile file14 = amendmentFormBean.getFile_path14();
                FormFile file15 = amendmentFormBean.getFile_path15();
                String ccmailid = Validation.checkNull((String) session.getAttribute("SESSION_USER_CC_MAILID"));
                if (ccmailid.length() > 0 && "NO DATA".equalsIgnoreCase(ccmailid)) {
                    session.setAttribute("SESSION_USER_CC_MAILID", amendmentFormBean.getCcMailId());
                    amendmentFormBean.setIsCCMailId("YES");
                } else {
                    amendmentFormBean.setIsCCMailId("NO");
                }
                //
                session.setAttribute("ENABLEAMENDMENTCOMMAIL", "N");
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.Enable_Amendment_Comparison_previousRecord_Mail)) && amendmentFormBean.getLrpBookNo() != null && amendmentFormBean.getLrpBookNo().length() > 0) {
                    session.setAttribute("ENABLEAMENDMENTCOMMAIL", "Y");
                }

                //
                Ebkg_type = amendmentFormBean.getEbkg_type();
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>amendmentFormBean.getEbook_pickup_date()>>" + amendmentFormBean.getEbook_pickup_date());
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>ebookbean.getEbook_drop_date()>>" + amendmentFormBean.getEbook_drop_date());
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        amendmentFormBean.getEbook_pickup_date(): " + amendmentFormBean.getEbook_pickup_date() + ", amendmentFormBean.getEbook_drop_date(): " + amendmentFormBean.getEbook_drop_date());

                if (amendmentFormBean.getEbook_pickup_date() != null && amendmentFormBean.getEbook_pickup_date().length() > 0) {
                    amendmentFormBean.setEbook_pickup_date(DateConversion_New.convertDefaultDBFormat(amendmentFormBean.getEbook_pickup_date()));
                }
                if (amendmentFormBean.getEbook_drop_date() != null && amendmentFormBean.getEbook_drop_date().length() > 0) {
                    amendmentFormBean.setEbook_drop_date(DateConversion_New.convertDefaultDBFormat(amendmentFormBean.getEbook_drop_date()));
                }
                //
                //Cutoff Details
                amendmentFormBean.setBkgcutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookbkgcutoff"))));
                amendmentFormBean.setDoccutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookdoccutoff"))));
                amendmentFormBean.setVgmcutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookvgmcutoff"))));
                amendmentFormBean.setBbrorocutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookbrorocutoff"))));
                amendmentFormBean.setHazcutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookhazcutoff"))));
                amendmentFormBean.setPortentrycutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookportentrycutoff"))));
                //
                //
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>amendmentFormBean>version>" + amendmentFormBean.getAppVersion());
                //Copy amendment form bean values to amendment vo
                BeanUtils.copyProperties(amendmentVO, amendmentFormBean);
                amendmentVO.setEmptyPickUpName(Validation.checkNull(emptyPickUpMap.get(amendmentFormBean.getEmptyPickup())));
                amendmentVO.setContactName(contactName);
                amendmentVO.setCustPhoneNo(contactPhone);
                //Routing details
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>>amendmentVO>>verwison>>>" + amendmentVO.getAppVersion());
                routehidden = amendmentFormBean.getEbook_masthdn();
                routingList = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");

//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>route id in save action>" + amendmentFormBean.getEbook_routeid());
//                 System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>routehidden>>" + routehidden);
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        amendmentFormBean version: " + amendmentFormBean.getAppVersion() + ", amendmentVO version: " + amendmentVO.getAppVersion() + ", route id in save action: " + amendmentFormBean.getEbook_routeid() + ", routehidden: " + routehidden);

                if (routingList != null && routingList.size() > 0 && routehidden != null && routehidden.equalsIgnoreCase("Y")) {
                    str_type = "N";
                    routeVO = new Route();

                    for (int j = 0; j < routingList.size(); j++) {
                        routeVO = (Route) routingList.get(j);
                        if (!"I".equalsIgnoreCase(routeVO.getLordporttype())) {
                            depdate = depdate != null ? depdate : routeVO.getETD1();
                            amendmentVO.setEbook_pol_etd(DateConversion_New.convertDefaultDBFormatwithTime(depdate));
                        }
                        if (!"I".equalsIgnoreCase(routeVO.getDisporttype())) {
                            arrdate = routeVO.getETA1();
                            amendmentVO.setEbook_pol_eta(DateConversion_New.convertDefaultDBFormatwithTime(arrdate));
                        }
                        System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>>str_type>>>" + str_type + ">>routeVO.getLegType()>>>" + routeVO.getLegType());
                        if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(routeVO.getLegType()) || "F".equalsIgnoreCase(routeVO.getLegType()))) {
                            str_type = "Y";
                            amendmentVO.setEbook_servicecode(routeVO.getService());
                            amendmentVO.setEbook_vesselcode(routeVO.getVessel());
                            amendmentVO.setEbook_voyage(routeVO.getVoyage());
                            if (routeVO.getLoadter() != null && routeVO.getLoadter().length() > 0) {
                                amendmentVO.setEbook_terminal(routeVO.getLoadter());
                            }
                            amendmentVO.setEbook_bound_leg(routeVO.getBound());
                            amendmentVO.setGroupcallid(routeVO.getGrpCallId());
                            System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>routeVO.getGrpCallId()>>" + routeVO.getGrpCallId());
                            slottype = routeVO.getGrpCallId();
                        }
                        strServiceCode = routeVO.getService();
                        if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                            strPol = strPol == null ? routeVO.getLoadport() : strPol;
                            strPod = routeVO.getDisport();
                            strPld = routeVO.getDisport();
                            if (routeVO.getDister() != null && routeVO.getDister().length() > 0) {
                                amendmentVO.setEbook_terminalpod(routeVO.getDister());
                            }
                        } else {
                            strPla = strPla == null ? routeVO.getLoadport() : strPla;
                            strPld = routeVO.getDisport();
                        }
                        //
                        strPla = strPla == null ? strPol : strPla;
                        //
//                         System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        ##########>strPla" + strPla);
//                         System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        ##########>strPol" + strPol);
//                         System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        ##########>strPod" + strPod);
//                         System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        ##########>strPld" + strPld);
                        System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        strPla: " + strPla + ", strPol: " + strPol + ", strPod: " + strPod + ", strPld: " + strPld);

                        //
                        amendmentVO.setEbook_origin(strPla);
                        amendmentVO.setEbook_portofloading(strPol);
                        amendmentVO.setEbook_portofdischarge(strPod);
                        amendmentVO.setEbook_delivery(strPld);
                        String selectedLang = (String) session.getAttribute("SelectedLanguage");
                        System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        selectedlangisamendbkg" + selectedLang);
                        amendmentVO.setLangType(selectedLang);
                        //
                        routeVO.setArrdate(DateConversion_New.convertDefaultDBFormatwithTime(routeVO.getArrdate()));
                        routeVO.setETA1(DateConversion_New.convertDefaultDBFormatwithTime(routeVO.getETA1()));
                        routeVO.setETD1(DateConversion_New.convertDefaultDBFormatwithTime(routeVO.getETD1()));
                        routingList.set(j, routeVO);
                    }
                } else {
                    amendmentVO.setEbook_origin(amendmentFormBean.getEbook_origin());
                    amendmentVO.setEbook_delivery(amendmentFormBean.getEbook_delivery());
                    //
                    amendmentVO.setEbook_servicecode("");
                    amendmentVO.setEbook_vesselcode("");
                    amendmentVO.setEbook_voyage("");
                    amendmentVO.setEbook_terminal("");
                    amendmentVO.setEbook_bound_leg("");
                    amendmentVO.setGroupcallid("");
                    slottype = "";
                    amendmentVO.setEbook_pol_etd(null);
                    amendmentVO.setEbook_pol_eta(null);
                    amendmentVO.setEbook_terminalpod("");
                    amendmentVO.setEbook_portofloading("");
                    amendmentVO.setEbook_portofdischarge("");
                    //
                    String lordporttype = Validation.checkNull(ecomCntrlObj.getPortType(amendmentFormBean.getEbook_origin()));
                    String disporttype = Validation.checkNull(ecomCntrlObj.getPortType(amendmentFormBean.getEbook_delivery()));
                    if (!"I".equalsIgnoreCase(lordporttype)) {
                        amendmentVO.setEbook_portofloading(amendmentFormBean.getEbook_origin());
                    }
                    if (!"I".equalsIgnoreCase(disporttype)) {
                        amendmentVO.setEbook_portofdischarge(amendmentFormBean.getEbook_delivery());
                    }
                }
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        >>slottype>>" + slottype);
                amendmentVO.setSlotType(slottype);
                amendmentVO.setCustCode(cust_code);
                amendmentVO.setLoginName(loginUser);
                amendmentVO.setEcomBookNo(amendmentFormBean.getBooking_refno());
                amendmentVO.setEbook_date(DateConversion_New.convertDefaultDBFormat(amendmentFormBean.getEbook_date()));
                amendmentVO.setEmptyPickUpDate(DateConversion_New.convertDefaultDBFormatwithoutTime(amendmentFormBean.getEmptyPickUpDate()));
                amendmentVO.setRemarks(amendmentFormBean.getEbook_status_remarks());

                //Cargo details
                containerList = new ArrayList<net.ecom.vo.AmendmentBookingFormBean>();
                String select[] = request.getParameterValues("cargocount");
                if (select != null && select.length > 0) {
                    selectLen = select.length;
                    for (int i = 0; i < selectLen; i++) {
                        eqpType = "eqpType" + select[i];
                        eqpSize = "eqpSize" + select[i];
                        eqpQty = "eqpQty" + select[i];
                        soc = "soc" + select[i];
                        hzd = "hzd" + select[i];
                        commodity = "commodity" + select[i];
                        packagename = "packagename" + select[i];
                        actCommoName = "actCommoName" + select[i];
                        sellersName = "sellersName" + select[i];
                        buyersName = "buyersName" + select[i];
                        buyersCode = "buyersCode" + select[i];
                        sellersCode = "sellerCode" + select[i];
                        cmdtyhblno = "cmdtyhblno" + select[i];
                        hscode = "hscode" + select[i];
                        packageQty = "packageQty" + select[i];
                        eqpStatus = "eqpStatus" + select[i];
                        tareWt = "tareWt" + select[i];
                        cargoWt = "cargoWt" + select[i];
                        netWt = "netWt" + select[i];
                        grossWt = "grossWt" + select[i];
                        volume = "volume" + select[i];
                        cargoWtUnit = "cargoWtUnit" + select[i];
                        netWtUnit = "netWtUnit" + select[i];
                        containerNo = "containerNo" + select[i];
                        volUnit = "volUnit" + select[i];
                        commodityothers = "commodityothers" + select[i];
                        marinePollut = "marinePollut" + select[i];
                        unno = "unno" + select[i];
                        flashPoint = "flashPoint" + select[i];
                        flashUnit = "flashUnit" + select[i];
                        shippingName = "shippingName" + select[i];
                        dgClass = "dgClass" + select[i];
                        presetTemp = "presetTemp" + select[i];
                        minTemp = "minTemp" + select[i];
                        maxTemp = "maxTemp" + select[i];
                        tempUnit = "tempUnit" + select[i];
                        nor = "nor" + select[i];
                        owLeft = "owLeft" + select[i];
                        dimUnit = "dimUnit" + select[i];
                        owRight = "owRight" + select[i];
                        oh = "oh" + select[i];
                        olAft = "olAft" + select[i];
                        olForward = "olForward" + select[i];
                        hidden_oog = "hidden_oog" + select[i];
                        hidden_lcl = "hidden_oog" + select[i];
                        hidden_rfr = "hidden_rfr" + select[i];
                        otherhscode = "otherhscode" + select[i];
                        othercommodity = "othercommodity" + select[i];
                        comid = "comid" + select[i];
                        conid = "conid" + select[i];
                        isdummy = "isdummy" + select[i];

                        humidityflag = "humidityflag" + select[i];
                        humidity = "humidity" + select[i];
                        drains = "drains" + select[i];
                        co2 = "co2" + select[i];
                        o2 = "o2" + select[i];

                        ventilation = "ventilation" + select[i];

                        packagecodes = Validation.checkNull(request.getParameter(packagename));
                        packagename = "";
                        packagecode = "";
                        if (packagecodes != null && packagecodes.contains("~~")) {
                            String[] packages = packagecodes.split("~~");
                            packagename = packages[0];
                            packagecode = packages[1];
                        }
                        commodities = Validation.checkNull(request.getParameter(commodity));
                        checkother = Validation.checkNull(request.getParameter(commodityothers));
                        commoditycode = "";
                        if (commodities != null && "N".equalsIgnoreCase(checkother)) {
                            String[] commodityvalue = commodities.split("~~");
                            commoditycode = commodityvalue[1];
                            //  commodityname = commodityvalue[0];
                        }
                        hscommodities = Validation.checkNull(request.getParameter(hscode));
                        hscommoditycode = "";
                        commodityname = "";
                        isFakComdty = "N";
                        System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        %%%%%%%%%%%%%%%%%%%%%%%%%%%>>>>>>>>>>>>isdummy" + request.getParameter(isdummy));
                        if (hscommodities != null && hscommodities.length() > 0 && "N".equalsIgnoreCase(checkother) && !"1".equalsIgnoreCase(amendmentVO.getAppVersion())) {
                            if (commoditycode != null && commoditycode.length() > 0 && commoditycode.trim().equalsIgnoreCase(fakComdtyCode)) {
                                isFakComdty = "Y";
                                hscommoditycode = hscommodities;
                                commodityname = hscommodities;
                            } else {
                                String[] commodityvalue = hscommodities.split("~~");
                                hscommoditycode = commodityvalue[1];
                                commodityname = commodityvalue[0];
                            }
                        } else {
                            if (hscommodities != null && hscommodities.length() > 0) {
                                hscommoditycode = hscommodities;
                            }
                        }
                        if ("Y".equalsIgnoreCase(checkother)) {
                            commoditycode = (String) request.getParameter(othercommodity);
                            hscommoditycode = (String) request.getParameter(otherhscode);
                        }
                        amendmentCargoVO = new net.ecom.vo.AmendmentBookingFormBean();
                        amendmentCargoVO.setContainerNo(Validation.checkNull(request.getParameter(containerNo)));
                        amendmentCargoVO.setIsFakCommodity(isFakComdty);
                        amendmentCargoVO.setEbook_isdummy(Validation.checkNull((String) request.getParameter(isdummy)));
                        amendmentCargoVO.setEbooking_commodity_desc(Validation.checkNull(commodityname));
                        amendmentCargoVO.setEbook_commoditycode(Validation.checkNull(commoditycode));
                        amendmentCargoVO.setEbooking_commodity(Validation.checkNull(commoditycode));
                        amendmentCargoVO.setEbook_packquantity(Validation.checkNull((String) request.getParameter(packageQty)));
                        amendmentCargoVO.setEbook_actCommoName(Validation.checkNull((String) request.getParameter(actCommoName)));
                        amendmentCargoVO.setEbook_cmdtyhblno(Validation.checkNull((String) request.getParameter(cmdtyhblno)));
                        amendmentCargoVO.setEbook_cmdtysellerName(Validation.checkNull((String) request.getParameter(sellersName)));
                        amendmentCargoVO.setEbook_cmdtybuyerName(Validation.checkNull((String) request.getParameter(buyersName)));
                        amendmentCargoVO.setEbook_cmdtysellercode(Validation.checkNull((String) request.getParameter(sellersCode)));
                        amendmentCargoVO.setEbook_cmdtybuyercode(Validation.checkNull((String) request.getParameter(buyersCode)));
                        amendmentCargoVO.setEbook_cargoweight(Validation.checkNull((String) request.getParameter(cargoWt)));
                        amendmentCargoVO.setEbook_weightunit(Validation.checkNull((String) request.getParameter(cargoWtUnit)));
                        amendmentCargoVO.setEbook_netweight(Validation.checkNull((String) request.getParameter(netWt)));
                        amendmentCargoVO.setEbook_netweightunit(Validation.checkNull((String) request.getParameter(netWtUnit)));
                        eqp_type = Validation.checkNull((String) request.getParameter(eqpType));
                        setType = Validation.checkNull((String) request.getParameter(eqpSize)) + Validation.checkNull((String) request.getParameter(eqpType));
                        amendmentCargoVO.setEbook_eqp(Validation.checkNull((String) request.getParameter(eqpSize)));
                        amendmentCargoVO.setEbook_eqp_qty("1");
                        amendmentCargoVO.setEbook_eqp_type(eqp_type);
                        amendmentCargoVO.setEbook_eqp_type_desc(setType);
                        amendmentCargoVO.setEbook_mintemp(Validation.checkNull((String) request.getParameter(minTemp)));
                        amendmentCargoVO.setEbook_maxtemp(Validation.checkNull((String) request.getParameter(maxTemp)));
                        amendmentCargoVO.setEbook_owleft(Validation.checkNull((String) request.getParameter(owLeft)));
                        amendmentCargoVO.setEbook_owright(Validation.checkNull((String) request.getParameter(owRight)));
                        amendmentCargoVO.setEbook_oh(Validation.checkNull((String) request.getParameter(oh)));
                        amendmentCargoVO.setEbook_olaft(Validation.checkNull((String) request.getParameter(olAft)));
                        amendmentCargoVO.setEbook_olforward(Validation.checkNull((String) request.getParameter(olForward)));
                        amendmentCargoVO.setEbook_dimensionunit(Validation.checkNull((String) request.getParameter(dimUnit)));
                        amendmentCargoVO.setEbook_cargovolume(Validation.checkNull((String) request.getParameter(volume)));
                        amendmentCargoVO.setEbook_volumeunit(Validation.checkNull((String) request.getParameter(volUnit)));
                        amendmentCargoVO.setEbook_packagecode(Validation.checkNull(packagecode));
                        amendmentCargoVO.setEbook_presetemp(Validation.checkNull((String) request.getParameter(presetTemp)));
                        amendmentCargoVO.setEbook_nor(Validation.checkNull((String) request.getParameter(nor)));
                        amendmentCargoVO.setEbook_temperunit(Validation.checkNull((String) request.getParameter(tempUnit)));
                        amendmentCargoVO.setOog(Validation.checkNull((String) request.getParameter(hidden_oog)));
                        String hzdFlag = Validation.checkNull((String) request.getParameter(hzd));
                        amendmentCargoVO.setHzd(hzdFlag);
                        
                        // Unified DG Extraction - Always extract as list (works for single & multiple)
                        if ("Y".equalsIgnoreCase(hzdFlag)) {
                            List<net.ecom.vo.AmendmentBookingFormBean> dgList = new ArrayList<>();
                            String dgCountParam = "dgCount" + select[i];
                            String dgCountStr = Validation.checkNull(request.getParameter(dgCountParam));
                            int dgCount = 1; // Default to 1 for single entry

                            try {
                                if (dgCountStr != null && dgCountStr.length() > 0) {
                                    dgCount = Integer.parseInt(dgCountStr);
                                }
                            } catch (Exception e) {
                                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction Error parsing dgCount: " + e.getMessage());
                            }

                            System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction DG Extraction: Container=" + select[i] + ", dgCount=" + dgCount);

                            // Extract DG entries (always loop, even for single entry)
                            for (int dgIdx = 1; dgIdx <= dgCount; dgIdx++) {
                                String unnoParam = "unno" + select[i] + "_" + dgIdx;
                                String dgClassParam = "dgClass" + select[i] + "_" + dgIdx;
                                String flashPointParam = "flashPoint" + select[i] + "_" + dgIdx;
                                String flashUnitParam = "flashUnit" + select[i] + "_" + dgIdx;
                                String marinePollutParam = "marinePollut" + select[i] + "_" + dgIdx;
                                String shippingNameParam = "shippingName" + select[i] + "_" + dgIdx;

                                String unnoVal = Validation.checkNull(request.getParameter(unnoParam));
                                if (unnoVal != null && unnoVal.length() > 0) {
                                    // Split UN number from combined value (format: "1100 ~~ ALCOHOL")
                                    
                                    // Create EJB VO object (not web formbean) for proper serialization
                                    net.ecom.vo.AmendmentBookingFormBean dgEntry = new net.ecom.vo.AmendmentBookingFormBean();
                                    dgEntry.setEbook_unno(unnoVal);
                                    dgEntry.setEbook_dgclass(Validation.checkNull(request.getParameter(dgClassParam)));
                                    dgEntry.setEbook_flashpoint(Validation.checkNull(request.getParameter(flashPointParam)));
                                    dgEntry.setEbook_flashunit(Validation.checkNull(request.getParameter(flashUnitParam)));
                                    dgEntry.setEbook_poluttunit(Validation.checkNull(request.getParameter(marinePollutParam)));
                                    dgEntry.setEbook_shipname(Validation.checkNull(request.getParameter(shippingNameParam)));
                                    dgEntry.setEbook_comid(String.valueOf(dgIdx)); // Set itemno for this DG entry
                                    dgList.add(dgEntry);

                                    System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction DG Entry " + dgIdx + ": UN=" + unnoVal
                                        + ", Class=" + dgEntry.getEbook_dgclass()
                                        + ", FP=" + dgEntry.getEbook_flashpoint());
                                }
                            }

                            if (!dgList.isEmpty()) {
                                amendmentCargoVO.setDgDetailsList(dgList);
                                amendmentCargoVO.setDgCount(dgList.size());
                                
                                // Set first DG entry fields for backward compatibility
                                net.ecom.vo.AmendmentBookingFormBean firstDG = dgList.get(0);
                                amendmentCargoVO.setEbook_flashpoint(firstDG.getEbook_flashpoint());
                                amendmentCargoVO.setEbook_unno(firstDG.getEbook_unno());
                                amendmentCargoVO.setEbook_dgclass(firstDG.getEbook_dgclass());
                                amendmentCargoVO.setEbook_flashunit(firstDG.getEbook_flashunit());
                                amendmentCargoVO.setEbook_poluttunit(firstDG.getEbook_poluttunit());
                                amendmentCargoVO.setEbook_shipname(firstDG.getEbook_shipname());
                                
                                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction Set dgDetailsList with " + dgList.size() + " entries");
                            }
                        }
                        amendmentCargoVO.setLcl(Validation.checkNull((String) request.getParameter(hidden_lcl)));
                        amendmentCargoVO.setRfr(Validation.checkNull((String) request.getParameter(hidden_rfr)));
                        amendmentCargoVO.setSoc(("Y".equalsIgnoreCase(Validation.checkNull((String) request.getParameter(soc))) ? "Y" : "N"));
                        amendmentCargoVO.setEbook_tareweight(Validation.checkNull((String) request.getParameter(tareWt)));
                        amendmentCargoVO.setEbook_grossweight(Validation.checkNull((String) request.getParameter(grossWt)));
                        amendmentCargoVO.setHscode(Validation.checkNull(hscommoditycode));
                        amendmentCargoVO.setEqpstatus(Validation.checkNull((String) request.getParameter(eqpStatus)));
                        amendmentCargoVO.setCommodityothers(Validation.checkNull((String) request.getParameter(commodityothers)));
                        amendmentCargoVO.setEbook_comid(Validation.checkNull(request.getParameter(comid)));
                        amendmentCargoVO.setEbook_conid(Validation.checkNull(request.getParameter(conid)));

                        amendmentCargoVO.setHumidityflag(Validation.checkNull(request.getParameter(humidityflag)));
                        amendmentCargoVO.setHumidity(Validation.checkNull(request.getParameter(humidity)));
                        amendmentCargoVO.setDrains(Validation.checkNull(request.getParameter(drains)));
                        amendmentCargoVO.setCo2(Validation.checkNull(request.getParameter(co2)));
                        amendmentCargoVO.setO2(Validation.checkNull(request.getParameter(o2)));
                        amendmentCargoVO.setVentilation(Validation.checkNull(request.getParameter(ventilation)));
                        System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        ventvalue" + request.getParameter(ventilation));
                        containerList.add(amendmentCargoVO);
                    }
                }
                //Container Number Rearrange
                //Customer Details 

                String index[] = request.getParameterValues("BuyercountRowID");
                String custCode = "", customerCountry = "", custName = "", custAddress = "", custCityDesc = "", custPhone = "", custFax = "", custEmail = "", custAddress1 = "", custState = "", custEori = "", custPersonType = "";

                if (index != null && index.length > 0) {
                    int indexLen = index.length;
                    BuyerList = new ArrayList();
                    System.out.println("Buyer index==>" + index + "indexLen=>" + indexLen);
                    for (int i = 0; i < indexLen; i++) {
                        custName = "ebook_Buyername" + index[i];
                        if (Validation.checkNull((String) request.getParameter(custName)).length() > 0) {
                            custCode = "ebook_Buyercode" + index[i];
                            System.out.println("index==>" + index[i]);
                            custCountry = "ebook_Buyercountry" + index[i];

                            custAddress = "ebook_Buyeraddress" + index[i];
                            custCityDesc = "ebook_Buyercity_desc" + index[i];
                            custPhone = "ebook_Buyerphone" + index[i];
                            custFax = "ebook_Buyerfax" + index[i];
                            custEmail = "ebook_Buyeremail" + index[i];
                            custAddress1 = "ebook_Buyeraddress1" + index[i];
                            custState = "ebook_Buyerstate" + index[i];
                            custEori = "ebook_Buyereori" + index[i];
                            custPersonType = "ebook_Buyerpersontype" + index[i];
                            amendmentCustomerVo = new net.ecom.vo.AmendmentBookingFormBean();
                            amendmentCustomerVo.setEbook_Custcode(Validation.checkNull((String) request.getParameter(custCode)));
                            amendmentCustomerVo.setEbook_Custemail(Validation.checkNull((String) request.getParameter(custEmail)));
                            amendmentCustomerVo.setEbook_Custname(Validation.checkNull((String) request.getParameter(custName)));
                            amendmentCustomerVo.setEbook_Custphone(Validation.checkNull((String) request.getParameter(custPhone)));
                            amendmentCustomerVo.setEbook_Custstate(Validation.checkNull((String) request.getParameter(custState)));
                            amendmentCustomerVo.setEbook_Custaddress(Validation.checkNull((String) request.getParameter(custAddress)));
                            amendmentCustomerVo.setEbook_Custaddress1(Validation.checkNull((String) request.getParameter(custAddress1)));
                            amendmentCustomerVo.setEbook_Custcity_desc(Validation.checkNull((String) request.getParameter(custCityDesc)));
                            amendmentCustomerVo.setEbook_Custcountry(Validation.checkNull((String) request.getParameter(custCountry)));
                            amendmentCustomerVo.setEbook_Custfax(Validation.checkNull((String) request.getParameter(custFax)));
                            amendmentCustomerVo.setEbook_Custeori(Validation.checkNull((String) request.getParameter(custEori)));
                            amendmentCustomerVo.setEbook_Custpersontype(Validation.checkNull((String) request.getParameter(custPersonType)));
                        
// // Optionally set new fields if they are being used in your form/request:
// amendmentCustomerVo.setEbook_Custid(Validation.checkNull((String) request.getParameter("custId")));
// amendmentCustomerVo.setEbook_CustSelect(Validation.checkNull((String) request.getParameter("custSelect")));
// amendmentCustomerVo.setEbook_Custcountry_desc(Validation.checkNull((String) request.getParameter("custCountryDesc")));
                            BuyerList.add(amendmentCustomerVo);
                        }
                    }

                }

                //Seller Customer
                String sellerindex[] = request.getParameterValues("SellercountRowID");
                // String custCode = "", customerCountry = "", custName = "", custAddress = "", custCityDesc = "", custPhone = "", custFax = "", custEmail = "", custAddress1 = "", custState = "", custEori = "", custPersonType = "";

                if (sellerindex != null && sellerindex.length > 0) {
                    int indexLen = sellerindex.length;
                    sellerList = new ArrayList();
                    System.out.println("index==>" + sellerindex + "indexLen=>" + indexLen);
                    for (int i = 0; i < indexLen; i++) {
                        custName = "ebook_Sellername" + sellerindex[i];
                        if (Validation.checkNull((String) request.getParameter(custName)).length() > 0) {
                            custCode = "ebook_Sellercode" + sellerindex[i];
                            System.out.println("index==>" + sellerindex[i]);
                            custCountry = "ebook_Sellercountry" + sellerindex[i];

                            custAddress = "ebook_Selleraddress" + sellerindex[i];
                            custCityDesc = "ebook_Sellercity_desc" + sellerindex[i];
                            custPhone = "ebook_Sellerphone" + sellerindex[i];
                            custFax = "ebook_Sellerfax" + sellerindex[i];
                            custEmail = "ebook_Selleremail" + sellerindex[i];
                            custAddress1 = "ebook_Selleraddress1" + sellerindex[i];
                            custState = "ebook_Sellerstate" + sellerindex[i];
                            custEori = "ebook_Sellereori" + sellerindex[i];
                            custPersonType = "ebook_Sellerpersontype" + sellerindex[i];
                            amendmentCustomerVo = new net.ecom.vo.AmendmentBookingFormBean();
                            amendmentCustomerVo.setEbook_Custcode(Validation.checkNull((String) request.getParameter(custCode)));
                            amendmentCustomerVo.setEbook_Custemail(Validation.checkNull((String) request.getParameter(custEmail)));
                            amendmentCustomerVo.setEbook_Custname(Validation.checkNull((String) request.getParameter(custName)));
                            amendmentCustomerVo.setEbook_Custphone(Validation.checkNull((String) request.getParameter(custPhone)));
                            amendmentCustomerVo.setEbook_Custstate(Validation.checkNull((String) request.getParameter(custState)));
                            amendmentCustomerVo.setEbook_Custaddress(Validation.checkNull((String) request.getParameter(custAddress)));
                            amendmentCustomerVo.setEbook_Custaddress1(Validation.checkNull((String) request.getParameter(custAddress1)));
                            amendmentCustomerVo.setEbook_Custcity_desc(Validation.checkNull((String) request.getParameter(custCityDesc)));
                            amendmentCustomerVo.setEbook_Custcountry(Validation.checkNull((String) request.getParameter(custCountry)));
                            amendmentCustomerVo.setEbook_Custfax(Validation.checkNull((String) request.getParameter(custFax)));
                            amendmentCustomerVo.setEbook_Custeori(Validation.checkNull((String) request.getParameter(custEori)));
                            amendmentCustomerVo.setEbook_Custpersontype(Validation.checkNull((String) request.getParameter(custPersonType)));
                           
// // Optionally set new fields if they are being used in your form/request:
// amendmentCustomerVo.setEbook_Custid(Validation.checkNull((String) request.getParameter("custId")));
// amendmentCustomerVo.setEbook_CustSelect(Validation.checkNull((String) request.getParameter("custSelect")));
// amendmentCustomerVo.setEbook_Custcountry_desc(Validation.checkNull((String) request.getParameter("custCountryDesc")));
                            sellerList.add(amendmentCustomerVo);
                        }
                    }

                }

                if (sellerList != null && !sellerList.isEmpty() || BuyerList != null && !BuyerList.isEmpty()) {
                    CustomerMap = new HashMap<String, List<net.ecom.vo.AmendmentBookingFormBean>>();
                    CustomerMap.put("SL", sellerList);
                    CustomerMap.put("BY", BuyerList);
                }
                int count = 0;
                List tempeqpsizelist = new ArrayList();
                String eqpsize = null;
                String containerno = null;
                for (net.ecom.vo.AmendmentBookingFormBean amvo : containerList) {
                    eqpsize = amvo.getEbook_eqp();
                    containerno = amvo.getContainerNo();
                    if (checkNull(containerno).length() > 7 && (containerno.indexOf("TBN") != -1 || containerno.indexOf("TNB") != -1)) {
                        count = Collections.frequency(tempeqpsizelist, eqpsize);
                        tempeqpsizelist.add(eqpsize);
                        containerno = containerno.substring(0, 7) + (count + 1);
                        amvo.setContainerNo(containerno);
                    }
                }
                //

                //Get Haulage details 
                String jsonhaulagestr = (String) request.getParameter("haulagedetailsjsonstr");
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        jsonhaulagestr===> " + jsonhaulagestr);
                List<BookingHaulagedetailsVo> haulagelist = null;
                if (jsonhaulagestr != null && jsonhaulagestr.length() > 0) {
                    Gson gson = new Gson();
                    Type jstype = new TypeToken<List<BookingHaulagedetailsVo>>() {
                    }.getType();
                    haulagelist = gson.fromJson(jsonhaulagestr, jstype);

                    haulagelist.forEach(haullis -> {
                        haullis.setDropdate(DateConversion_New.convertDefaultDBFormatwithTime(haullis.getDropdate()));
                    });
                    amendmentVO.setHaulagelist(haulagelist);
                }
                //

                //File details
                FileDetailsVO fls = new FileDetailsVO();

                String enableFileBackendValidation = (String) contexts.getAttribute(GeneralConf.EnableFileBackendValidation);
                String ecomFileExt = (String) contexts.getAttribute(GeneralConf.EcomFileExtension);
                FileExtensionValidation flext = new FileExtensionValidation();
                String restrictedFiled = null;
                String restrictedFiledSize = null;

                fls.setFilePath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH));
                if (file1 != null && file1.getFileName() != null && file1.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file1, ecomFileExt, request)) {
                    fls.setVgmFileData(file1.getFileData());
                    fls.setVgmFileName(file1.getFileName());
                    fls.setFileAvailable(true);
                } else if (file1 != null && file1.getFileName() != null && file1.getFileName().length() > 0) {
                    fls.setVgmFileName(null);
                }

                if (file2 != null && file2.getFileName() != null && file2.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file2, ecomFileExt, request)) {
                    fls.setDgFileData(file2.getFileData());
                    fls.setDgFileName(file2.getFileName());
                    fls.setFileAvailable(true);
                } else if (file2 != null && file2.getFileName() != null && file2.getFileName().length() > 0) {
                    fls.setDgFileName(null);
                }

                if (file3 != null && file3.getFileName() != null && file3.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file3, ecomFileExt, request)) {
                    fls.setLolFileData(file3.getFileData());
                    fls.setLolFileName(file3.getFileName());
                    fls.setFileAvailable(true);
                } else if (file3 != null && file3.getFileName() != null && file3.getFileName().length() > 0) {
                    fls.setLolFileName(null);
                }
                if (file4 != null && file4.getFileName() != null && file4.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file4, ecomFileExt, request)) {
                    fls.setMsdsFileData(file4.getFileData());
                    fls.setMsdsFileName(file4.getFileName());
                    fls.setFileAvailable(true);
                } else if (file4 != null && file4.getFileName() != null && file4.getFileName().length() > 0) {
                    fls.setMsdsFileName(null);
                }
                if (file5 != null && file5.getFileName() != null && file5.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file5, ecomFileExt, request)) {
                    fls.setOthersFileData(file5.getFileData());
                    fls.setOthersFileName(file5.getFileName());
                    fls.setFileAvailable(true);
                } else if (file5 != null && file5.getFileName() != null && file5.getFileName().length() > 0) {
                    fls.setOthersFileName(null);
                }

                if (file6 != null && file6.getFileName() != null && file6.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file6, ecomFileExt, request)) {
                    fls.setVgmFileData1(file6.getFileData());
                    fls.setVgmFileName1(file6.getFileName());
                    fls.setFileAvailable(true);
                } else if (file6 != null && file6.getFileName() != null && file6.getFileName().length() > 0) {
                    fls.setVgmFileName1(null);
                }
                if (file7 != null && file7.getFileName() != null && file7.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file7, ecomFileExt, request)) {
                    fls.setVgmFileData2(file7.getFileData());
                    fls.setVgmFileName2(file7.getFileName());
                    fls.setFileAvailable(true);
                } else if (file7 != null && file7.getFileName() != null && file7.getFileName().length() > 0) {
                    fls.setVgmFileName2(null);
                }
                if (file8 != null && file8.getFileName() != null && file8.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file8, ecomFileExt, request)) {
                    fls.setDgFileData1(file8.getFileData());
                    fls.setDgFileName1(file8.getFileName());
                    fls.setFileAvailable(true);
                } else if (file8 != null && file8.getFileName() != null && file8.getFileName().length() > 0) {
                    fls.setDgFileName1(null);
                }
                if (file9 != null && file9.getFileName() != null && file9.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file9, ecomFileExt, request)) {
                    fls.setDgFileData2(file9.getFileData());
                    fls.setDgFileName2(file9.getFileName());
                    fls.setFileAvailable(true);
                } else if (file9 != null && file9.getFileName() != null && file9.getFileName().length() > 0) {
                    fls.setDgFileName2(null);
                }
                if (file10 != null && file10.getFileName() != null && file10.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file10, ecomFileExt, request)) {
                    fls.setLolFileData1(file10.getFileData());
                    fls.setLolFileName1(file10.getFileName());
                    fls.setFileAvailable(true);
                } else if (file10 != null && file10.getFileName() != null && file10.getFileName().length() > 0) {
                    fls.setLolFileName1(null);
                }
                if (file11 != null && file11.getFileName() != null && file11.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file11, ecomFileExt, request)) {
                    fls.setLolFileData2(file11.getFileData());
                    fls.setLolFileName2(file11.getFileName());
                    fls.setFileAvailable(true);
                } else if (file11 != null && file11.getFileName() != null && file11.getFileName().length() > 0) {
                    fls.setLolFileName2(null);
                }
                if (file12 != null && file12.getFileName() != null && file12.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file12, ecomFileExt, request)) {
                    fls.setMsdsFileData1(file12.getFileData());
                    fls.setMsdsFileName1(file12.getFileName());
                    fls.setFileAvailable(true);
                } else if (file12 != null && file12.getFileName() != null && file12.getFileName().length() > 0) {
                    fls.setMsdsFileName1(null);
                }
                if (file13 != null && file13.getFileName() != null && file13.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file13, ecomFileExt, request)) {
                    fls.setMsdsFileData2(file13.getFileData());
                    fls.setMsdsFileName2(file13.getFileName());
                    fls.setFileAvailable(true);
                } else if (file13 != null && file13.getFileName() != null && file13.getFileName().length() > 0) {
                    fls.setMsdsFileName2(null);
                }
                if (file14 != null && file14.getFileName() != null && file14.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file14, ecomFileExt, request)) {
                    fls.setOthersFileData1(file14.getFileData());
                    fls.setOthersFileName1(file14.getFileName());
                    fls.setFileAvailable(true);
                } else if (file14 != null && file14.getFileName() != null && file14.getFileName().length() > 0) {
                    fls.setOthersFileName1(null);
                }
                if (file15 != null && file15.getFileName() != null && file15.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file15, ecomFileExt, request)) {
                    fls.setOthersFileData2(file15.getFileData());
                    fls.setOthersFileName2(file15.getFileName());
                    fls.setFileAvailable(true);
                } else if (file15 != null && file15.getFileName() != null && file15.getFileName().length() > 0) {
                    fls.setOthersFileName2(null);
                }
                restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
                restrictedFiled = (restrictedFiled != null && restrictedFiled.length() > 0) ? restrictedFiled.substring(0, restrictedFiled.length() - 1) : "";
                request.setAttribute("restrictedFile", restrictedFiled);
                restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
                restrictedFiledSize = (restrictedFiledSize != null && restrictedFiledSize.length() > 0) ? restrictedFiledSize.substring(0, restrictedFiledSize.length() - 1) : "";
                request.setAttribute("restrictedFileSize", restrictedFiledSize);

                amendmentVO.setEnableZipConversion(Validation.checkNull((String) contexts.getAttribute(GeneralConf.Enable_ZipFile_Conversion)));
                /*
            if (file_name1 != null && file_name1.length() > 0) {
                filepath += file_name1 + "~~~";
            }

            if (file_name2 != null && file_name2.length() > 0) {
                filepath += file_name2 + "~~~";
            }

            if (file_name3 != null && file_name3.length() > 0) {
                filepath += file_name3 + "~~~";
            }

            if (file_name4 != null && file_name4.length() > 0) {
                filepath += file_name4 + "~~~";
            }

            if (file_name5 != null && file_name5.length() > 0) {
                filepath += file_name5 + "~~~";
            }

            amendmentVO.setFilesNames(filepath);*/

                if ("draftadd".equalsIgnoreCase(btn_type)) {
                    SaveType = "D";
                } else if ("tempadd".equalsIgnoreCase(btn_type)) {
                    SaveType = "T";
                } else {
                    SaveType = "S";
                }
              
                ENSDeclarantVo edvo = new ENSDeclarantVo();
                BeanUtils.copyProperties(edvo, amendmentFormBean);
                amendmentVO.setEnsdecvo(edvo);
            
                    String originPort = amendmentVO.getEbook_origin();
                    String deliveryPort = amendmentVO.getEbook_delivery();
                    String originCountry = amendmentVO.getEbook_origin_country();
                    String deliveryCountry = amendmentVO.getEbook_delivery_country();
                    
                    System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction Before swap - Origin: " + originPort 
                            + ", Delivery: " + deliveryPort
                            + ", OriginCountry: " + originCountry
                            + ", DeliveryCountry: " + deliveryCountry);
                    
                    String actualOrigin = MaskPortUtil.swapmaskporttoactual(originPort);
                    amendmentVO.setEbook_origin(actualOrigin);
                    
                
                    String actualDelivery = MaskPortUtil.swapmaskporttoactual(deliveryPort);
                    amendmentVO.setEbook_delivery(actualDelivery);
                    
                   
                    String actualOriginCountry = MaskPortUtil.swapmaskporttoactualportwithname(originCountry);
                    amendmentVO.setEbook_origin_country(actualOriginCountry);
                    
                
                    String actualDeliveryCountry = MaskPortUtil.swapmaskporttoactualportwithname(deliveryCountry);
                    amendmentVO.setEbook_delivery_country(actualDeliveryCountry);
                    
           
                List personalInfo = ecomCntrlObj.getAPDetails(userCode);
                String ApEmailID = (personalInfo != null && personalInfo.size() > 5) ? (String) personalInfo.get(5) : "";
                String addremarks = amendmentFormBean.getEbook_status_remarks();
                returnVO = ecomAmendmentObj.saveAmendmentBooking(amendmentVO, routingList, containerList, client_code, fls, custTypeConfig, SaveType, ApEmailID, Ebkg_type, custCountry, CustomerMap);
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        ======>SaveType===" + SaveType);
                if (returnVO != null && !returnVO.getStrRefNo().equalsIgnoreCase("ERROR")) {
                    try {
                        InsuranceUtils IU = new InsuranceUtils();
                        String isaviblequote = (String) session.getAttribute("CREATE_QUOTE_NEW");
//                         System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        isaviblequote " + isaviblequote);
                        List insurancelist = null;
                        List insurancecusttypelis = (List) session.getAttribute("CREATE_QUOTE_CUST_TYPE");
                        List insuredpartylist = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY");
                        List insuredpartyemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL");
                        List insuredpartyprimaryemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL");
//                         System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        insurancecusttypelis==> " + insurancecusttypelis);
//                         System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        CREATE_QUOTE_INPUT_JSON==> " + session.getAttribute("CREATE_QUOTE_INPUT_JSON"));
                        System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        isaviblequote: " + isaviblequote + ", insurancecusttypelis: " + insurancecusttypelis + ", CREATE_QUOTE_INPUT_JSON: " + session.getAttribute("CREATE_QUOTE_INPUT_JSON"));

                        if (isaviblequote != null && "Y".equalsIgnoreCase(isaviblequote)) {
                            insurancelist = IU.callAPI("quotecreate", returnVO.getStrRefNo(), contexts, session);
                            System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        insurancelist==> " + insurancelist);
                            List inslis = IU.arrangeInsuranceDetailsToInsert(insurancecusttypelis, insurancelist, "BKG", insuredpartylist, insuredpartyemail, insuredpartyprimaryemail);
                            bookingObj.setInsurancedetails(inslis, returnVO.getReaderid());
                            if (inslis != null && inslis.size() > 0) {
                                request.setAttribute("InsuranceMSGMessage", "Insurance details saved successfully.");
                            } else {
                                request.setAttribute("InsuranceMSGMessage", "Insurance details not saved.");
                            }
                            session.setAttribute("CREATE_QUOTE_NEW", "N");
                            session.setAttribute("CREATE_QUOTE_CUST_TYPE", null);
                            session.setAttribute("CREATE_QUOTE_INPUT_JSON", null);
                            session.setAttribute("CREATE_QUOTE_INSURED_PARTY", null);
                            session.setAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL", null);
                            session.setAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL", null);
                        }
                        if ("Y".equalsIgnoreCase(Validation.checkNull((String) session.getAttribute("ENABLEAMENDMENTCOMMAIL")))) {
                            HashMap bookingCache = new HashMap();
                            bookingCache.put("LRPBookNo", amendmentFormBean.getLrpBookNo());
                            bookingCache.put("EDIReaderID", returnVO.getReaderid());
                            bookingCache.put("EcomBookNo", amendmentFormBean.getBooking_refno());
                            bookingCache.put("Type", "Booking");
                            bookingCache.put("ToMail", checkNull(returnVO.getAdmToMailId()).length() > 0 ? "Y" : "N");
                            session.setAttribute("AMENDMENT_BOOKING_MAIL_CACHE", bookingCache);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (returnVO != null && !returnVO.getStrRefNo().equalsIgnoreCase("ERROR") && "S".equalsIgnoreCase(SaveType)) {
                    //send mail notification to customer
                    String frommail = (String) contexts.getAttribute("clientfrommail");
                    String subject = null;
                    Mailer mail = new Mailer();
                    String initiatorID = "ECOM-BKG";
                    message = returnVO.getStrMailMsg();
                    addccMailid = (String) contexts.getAttribute(GeneralConf.additionalbkgccmail);

                    Map<String, String> mailAlertMap = (Map<String, String>) session.getAttribute("USERMAILALERTSESSIONMAP");
                    boolean sendMail = false;
                    if (mailAlertMap != null && mailAlertMap.size() > 0 && "Y".equalsIgnoreCase(mailAlertMap.get(returnVO.getActivityCode()))) {
                        sendMail = true;
                    }
                    String details_mail = "";
                    if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_CUST)) || "Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_AGENCY))) {
                        //Preview mail start
                        if (sendMail || (returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0)) {
                            System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        sendMail>>" + sendMail);
                            HashMap masterCache = new HashMap();
                            HashMap carriageContacts = (HashMap) session.getAttribute("CARRIERCONTACTSMAP");
                            List getAddInfo = (List) contexts.getAttribute(GeneralConf.AdditionalInfoBkg);
                            Map<String, String> intransitMap = (Map<String, String>) contexts.getAttribute("PODINTRANSITMAPCONTEXT");

                            Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
                            Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
                            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                            Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
                            Hashtable packagemap = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                            LinkedHashMap eqpmap = (LinkedHashMap) contexts.getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                            String carriercode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
                            Map dgshipper = (Map) contexts.getAttribute("SESSION_DGCLASS");
                            String hideMinMaxTemp = (String) contexts.getAttribute(GeneralConf.Hide_Min_MaxTemp);
                            String userFruitCust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
                            String Enable_Drains_Humidity = (String) contexts.getAttribute(GeneralConf.Enable_Drains_Humidity);
                            String Ventilationlabel = "Ventilation" + (String) contexts.getAttribute("Ventilation");
                            String iseuport = (String) session.getAttribute("IsEnableENSTabInBooking");
                            masterCache.put("carriagecontact", carriageContacts);
                            masterCache.put("addinfo", getAddInfo);
                            masterCache.put("PODINTRANSITMAPCONTEXT", intransitMap);
                            masterCache.put("EMPTYPICKUPMAP", emptyPickUpMap);
                            masterCache.put("VESSELLIST", vessellist);
                            masterCache.put("CONTRYLIST", contrylist);
                            masterCache.put("SERVICELIST", serlist);
                            masterCache.put("PACKAGELIST", packagemap);
                            masterCache.put("EQUIPMENT_TYPE", eqpmap);
                            masterCache.put("PORTLIST", portlist);
                            masterCache.put("carriercode", carriercode);
                            masterCache.put("comlist", comlist);
                            masterCache.put("SESSION_DGCLASS", dgshipper);
                            masterCache.put("hideMinMaxTemp", hideMinMaxTemp);
                            masterCache.put("userFruitCust", userFruitCust);
                            masterCache.put("Enable_Drains_Humidity", Enable_Drains_Humidity);
                            masterCache.put("EnableActualCommoName", (String) contexts.getAttribute(GeneralConf.EnableActualCommoName));
                            masterCache.put("Enable_Eori_Details", (String) contexts.getAttribute(GeneralConf.GENS));
                            masterCache.put("Ebkg_type", Ebkg_type);
                            masterCache.put("enable_zipcode", (String) contexts.getAttribute(GeneralConf.Enable_Zip_Code));
                            masterCache.put(GeneralConf.Display_HS_NAME_CODE, (String) contexts.getAttribute(GeneralConf.Display_HS_NAME_CODE));
                            masterCache.put("IsEnableENSTabInBooking", iseuport);
                            masterCache.put("Ventilation", Ventilationlabel);
                            masterCache.put(GeneralConf.Hide_AP_CUST_BKG_Master, (String) contexts.getAttribute(GeneralConf.Hide_AP_CUST_BKG_Master));
                            String logo_name = (String) contexts.getAttribute(GeneralConf.Logo_Name);

                            String imagepath = (String) contexts.getAttribute("Imagepath");

                            details_mail = new CommonMail().amendmentBookingMail(amendmentVO, imagepath, logo_name, returnVO.getStrRefNo(), masterCache, routingList, containerList);
                        }
                        //preview mail end
                    }
                    if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_CUST))) {
                        message = details_mail;
                    } //EN-CH MAIL
                    else if (custCountry != null && "CN".equalsIgnoreCase(custCountry)) {
                        Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                        Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
                        HashMap masterCache = new HashMap();
                        masterCache.put("CustCompany", custCompany);
                        masterCache.put("comlist", comlist);
                        masterCache.put("PORTLIST", portlist);
                        masterCache.put("VESSELLIST", vessellist);
                        message = new CommonMailForCHCust().newCHamendmentBookingMail(amendmentVO, masterCache, containerList, returnVO);

                    } //
                    else {
                        String agencyrefno = "", agencyrefnumber = "";
                        agencyrefno = Validation.checkNull(amendmentFormBean.getEbook_agency_reference());
                        if (agencyrefno.length() > 0 && "CN".equalsIgnoreCase(returnVO.getAgencycountry())) {
                            agencyrefnumber = "<br><br>Agency Reference Number:" + agencyrefno;
                        }

                        message = message.replace("@date", DateConversion_New.getCurrentDate());
                        
                        // Check if POL or POD is a mask port - if so, swap actual port names to mask port names
                        String polCode = Validation.checkNull(amendmentFormBean.getEbook_origin().toUpperCase());
                        String podCode = Validation.checkNull(amendmentFormBean.getEbook_delivery().toUpperCase());
                        String pol = Validation.checkNull(String.valueOf(portlist.get(polCode)));
                        String pod = Validation.checkNull(String.valueOf(portlist.get(podCode)));
                        boolean isMaskPort = MaskPortUtil.isActualPort(polCode) || MaskPortUtil.isActualPort(podCode);
                        if (isMaskPort) {
                            pol = MaskPortUtil.swapActualPortToMaskPortWithName(polCode + " - " + pol);
                            pod = MaskPortUtil.swapActualPortToMaskPortWithName(podCode + " - " + pod);
                            System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction Mask port detected - swapped pol: " + pol + ", pod: " + pod);
                        }
                        message = message.replace("@pol", pol);
                        message = message.replace("@pod", pod + agencyrefnumber);
                    }
                    if (sendMail) {

                        String ccMail = Validation.checkNull(amendmentFormBean.getCcMailId());
//                            if (addccMailid != null && addccMailid.trim().length() > 0) {
//                             if(ccMail.length()>0){
//                              ccMail=  addccMailid + "," + ccMail;
//                              }
//                             else{
//                                 
//                              ccMail=  addccMailid;
//                            
//                            }
//                            }
                        if (addccMailid != null && addccMailid.trim().length() > 0 && ccMail.length() > 0) {

                            ccMail = addccMailid + "," + ccMail;

                        } else if (addccMailid != null && addccMailid.trim().length() > 0) {
                            ccMail = addccMailid;

                        } else if (ccMail.length() > 0) {
                            ccMail = ccMail;

                        }
                        mail.setCc(ccMail);

                        mail.setTo(useremail);
                        mail.setSubject(returnVO.getStrSubject());

                        mail.setMessage(message);
                        try {
                            ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                            System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        after sendMailFromEjb >>>>");
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }

                    //send mail notification to agency
                    message = null;
//                     System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        /send mail notification to agency");
                    String apEnable = Validation.checkNull((String) session.getAttribute("ENABLEAMENDMENTCOMMAIL"));
//                     System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        apEnable===>" + apEnable);
                    Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                    Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
                    System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        after comlist" + returnVO.getAdmToMailId() + ",apEnable:" + apEnable);
                    HashMap masterCache = new HashMap();
                    masterCache.put("apname", apname);
                    masterCache.put("comlist", comlist);
                    masterCache.put("PORTLIST", portlist);
                    masterCache.put("VESSELLIST", vessellist);
                    masterCache.put("ENABLEAMENDMENTCOMMAIL", apEnable);
                    if (returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0 && "N".equalsIgnoreCase(apEnable)) {

                        if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_AGENCY))) {
                            message = details_mail;
                        } else {

//                        message = returnVO.getAdmMailMsg();
//                        message = message.replace("@apname", apname);
//                        message = message.replace("@customer_remarks", Validation.checkNull(addremarks));
//                        message = message.replace("@remarks", Validation.checkNull(addremarks));
//                        message = message.replace("@salesagreed", Validation.checkNull(amendmentFormBean.getSalesRateAgree()));
//                        message = message.replace("@pol", Validation.checkNull(String.valueOf(portlist.get(amendmentFormBean.getEbook_origin().toUpperCase()))));
//                        message = message.replace("@pod", Validation.checkNull(String.valueOf(portlist.get(amendmentFormBean.getEbook_delivery().toUpperCase()))));
//                    
                            message = new CommonMailForAgency().newAgencyamendmentBookingMail(amendmentVO, masterCache, containerList, returnVO);

                        }
                        mail.setTo(returnVO.getAdmToMailId());
                        subject = returnVO.getAdmSubject();
                        subject = subject.replace("@apname", apname);
                        String filenames = returnVO.getFilenames();
                        mail.setSubject(subject);
                        mail.setMessage(message);
                        mail.setAttachment(filenames);
                        mail.setAttachmentpath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH) + File.separator + returnVO.getStrRefNo() + File.separator);
                        mail.setCc(null);
                        try {
                            ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }

                    } else if (returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0) {
                        String mailmsg = "";
                        mailmsg = new CommonMailForAgency().newAgencyamendmentBookingMail(amendmentVO, masterCache, containerList, returnVO);
                        HashMap mailCache = new HashMap();
                        System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        AdmToMailId" + returnVO.getAdmToMailId() + "AdmSubject" + returnVO.getAdmSubject() + "Filenames" + returnVO.getFilenames());
                        mailCache.put("AdmToMailId", returnVO.getAdmToMailId());
                        mailCache.put("AdmSubject", returnVO.getAdmSubject());
                        mailCache.put("Filenames", returnVO.getFilenames());
                        mailCache.put("StrRefNo", returnVO.getStrRefNo());
                        mailCache.put("Mailmsg", mailmsg);
                        mailCache.put("amendmentMaster", amendmentVO);
                        session.setAttribute("SESSION_AMEDMAIL_DETAILS", mailCache); //agency

                    }
                    //
                    returnVO.setStrSaveType("S");
                    SUCCESS = "success";
                }
                request.setAttribute("SaveResult", returnVO);
                if ("D".equalsIgnoreCase(SaveType)) {
                    //
                    returnVO.setStrSaveType("D");
                    SUCCESS = "success";
                }
                if ("T".equalsIgnoreCase(SaveType) || "TE".equalsIgnoreCase(SaveType)) {
                    ArrayList templateNoListObj = ecomCntrlObj.getTemplateNo(loginUser, "T");
                    ArrayList templateno = new ArrayList();
                    ArrayList backuptemplateno = new ArrayList();
                    ArrayList tempmangetemplate = new ArrayList();   // vasanth
                    if (templateNoListObj != null && templateNoListObj.size() == 3) {
                        templateno = LabelBeanConversion.labelListConversion((List<DropDownVO>) templateNoListObj.get(0));
                        backuptemplateno = (ArrayList) templateNoListObj.get(1);
                        tempmangetemplate = (ArrayList) templateNoListObj.get(2);  // vasanth
                    }
                    session.setAttribute("SESSION_TEMPLATE_NO", templateno);
                    session.setAttribute("SESSION_BACKUP_TEMPLATE_NO", backuptemplateno);
                    session.setAttribute("SESSION_MANAGE_TEMPLATE", tempmangetemplate); //vasanth
                    request.setAttribute("result", "Template Saved");

                    ArrayList view = null;
                    String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
                    view = bookingObj.getBookingDetail(userCode, "", "", "A", custTypeConfig, v1DataEnableStatus);
                    if (view != null && view.size() > 0) {
                        int len = view.size();
                        for (int i = 0; i < len; i++) {
                            EbookingViewAll eBooking = (EbookingViewAll) view.get(i);
                            if (portlist.containsKey(eBooking.getOriginPortId().toUpperCase())) {
                                eBooking.setOriginPortText(eBooking.getOriginPortId() + "-" + Validation.checkNull((String) portlist.get(eBooking.getOriginPortId().toUpperCase())));
                            }
                            if (portlist.containsKey(eBooking.getFinalPortId().toUpperCase())) {
                                eBooking.setFinalPortText(eBooking.getFinalPortId() + "-" + Validation.checkNull((String) portlist.get(eBooking.getFinalPortId().toUpperCase())));
                            }
                            if (portlist.containsKey(eBooking.getLoadPortID().toUpperCase())) {
                                eBooking.setLoadPortText(eBooking.getLoadPortID() + "-" + Validation.checkNull((String) portlist.get(eBooking.getLoadPortID().toUpperCase())));
                            }
                            if (portlist.containsKey(eBooking.getDischargePortId().toUpperCase())) {
                                eBooking.setDischargePortText(eBooking.getDischargePortId() + "-" + Validation.checkNull((String) portlist.get(eBooking.getDischargePortId().toUpperCase())));
                            }
                            if (eBooking.getBookingDate().length() > 0) {
                                eBooking.setBookingDate(DateConversion_New.convertDefaultTimeFormat(eBooking.getBookingDate()));
                            }
                            if (eBooking.getCrdate().length() > 0) {
                                eBooking.setCrdate(DateConversion_New.convertDefaultTimeFormat(eBooking.getCrdate()));
                            }
                            if (eBooking.getSubmitdate().length() > 0) {
                                eBooking.setSubmitdate(DateConversion_New.convertDefaultTimeFormat(eBooking.getSubmitdate()));
                            }
                            if (Validation.checkNull(eBooking.getPoletd()).length() > 0) {
                                eBooking.setPoletd(DateConversion_New.convertDefaultTimeFormat(eBooking.getPoletd()));
                            }
                            view.set(i, eBooking);
                            eBooking = null;
                        }
                    }
                    session.setAttribute("BookingView", view);
                    if ("TE".equalsIgnoreCase(SaveType)) {
                        session.setAttribute("SAVETEMPLATE", "update");
                    } else {
                        session.setAttribute("SAVETEMPLATE", "save");
                    }

                    SUCCESS = "showrecords";
                }
            } else {
                SUCCESS = "success";
                System.out.println("[ECOM-WAR] BKGAmemdmentSaveAction        Duplicate Submission of the form" + SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
