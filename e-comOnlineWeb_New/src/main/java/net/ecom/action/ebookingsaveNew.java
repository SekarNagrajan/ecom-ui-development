package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.nfr.mail.bean.IMail;

import static net.ecom.common.Validation.checkNull;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import net.ecom.lookup.EJBLookUp;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.CommonMail;
import net.ecom.common.CommonMailForAgency;
import net.ecom.common.CommonMailForCHCust;
import net.ecom.common.GeneralConf;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.FileExtensionValidation;
import net.ecom.common.utils.InsuranceUtils;
import net.ecom.formbeans.EbookingValidatorFormBeanNew;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.EbookingViewAll;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.MaskPortVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;
import net.ecom.common.utils.MaskPortUtil;
import org.apache.struts.upload.FormFile;

public class ebookingsaveNew extends Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();

    @Inject
    IMail mailer;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        EbookingValidatorFormBeanNew ebookbean = (EbookingValidatorFormBeanNew) form;
        ebookbean.setEbook_date(DateConversion_New.convertDefaultDBFormat(ebookbean.getEbook_date()));
        if (ebookbean.getEmptyPickUpDate() != null && ebookbean.getEmptyPickUpDate().length() > 0) {
            ebookbean.setEmptyPickUpDate(DateConversion_New.convertDefaultDBFormatwithoutTime(ebookbean.getEmptyPickUpDate()));
        }
        System.out.println("[ECOM-WAR] ebookingsaveNew >>ebookbean.getEbook_pickup_date()>>" + ebookbean.getEbook_pickup_date() + ">>ebookbean.getEbook_drop_date()>>" + ebookbean.getEbook_drop_date());
        if (ebookbean.getEbook_pickup_date() != null && ebookbean.getEbook_pickup_date().length() > 0) {
            ebookbean.setEbook_pickup_date(DateConversion_New.convertDefaultDBFormat(ebookbean.getEbook_pickup_date()));
        }
        if (ebookbean.getEbook_drop_date() != null && ebookbean.getEbook_drop_date().length() > 0) {
            ebookbean.setEbook_drop_date(DateConversion_New.convertDefaultDBFormat(ebookbean.getEbook_drop_date()));
        }
        ebookbean.setRemarks(ebookbean.getEbook_status_remarks());
        String btn_type = ebookbean.getSubmit_btn();
        ServletContext contexts = servlet.getServletContext();
        String eqpQuantity = "1";
        String s = ebookbean.getEbook_eqp_qty();
        ArrayList container_grid_array = new ArrayList();
        EbookingValidatorFormBeanNew ebookbeancargo = null;
        String SUCCESS = null, slottype = "", routehidden = null;
        String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGFRONTEND));
        String addccMailid = null;
        String apname = null;
        List inslis = null;
        ArrayList customerDetailes_array = null;
        EbookingValidatorFormBeanNew ebookbeancust = null;
        Map custMap = null;
        try {
            SUCCESS = "new";
            HttpSession session = request.getSession();
            apname = (String) session.getAttribute("apname");
            System.out.println("[ECOM-WAR] ebookingsaveNew token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN"));
            System.out.println("[ECOM-WAR] ebookingsaveNew token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN"));
            String sessionToken = (String) session.getAttribute("org.apache.struts.action.TOKEN");
            
            if (isTokenValid(request)) {
                String fakCommodity = (String) contexts.getAttribute(GeneralConf.FAKCOMMODITY);
                String fakComdtyCode = "";
                String fakComdtyName = "";
                System.out.println("[ECOM-WAR] ebookingsaveNew fakCommodity>>>>" + fakCommodity);
                fakCommodity = fakCommodity != null ? fakCommodity : "";
                if (fakCommodity != null && fakCommodity.length() > 0) {
                    String fakCommodityArr[] = fakCommodity.split("~~");
                    if (fakCommodityArr.length == 2) {
                        fakComdtyCode = fakCommodityArr[0].trim();
                        fakComdtyName = fakCommodityArr[1].trim();
                    }
                }
                resetToken(request);
                FormFile file1 = ebookbean.getFile_path1();
                FormFile file2 = ebookbean.getFile_path2();
                FormFile file3 = ebookbean.getFile_path3();
                FormFile file4 = ebookbean.getFile_path4();
                FormFile file5 = ebookbean.getFile_path5();
                FormFile file6 = ebookbean.getFile_path6();
                FormFile file7 = ebookbean.getFile_path7();
                FormFile file8 = ebookbean.getFile_path8();
                FormFile file9 = ebookbean.getFile_path9();
                FormFile file10 = ebookbean.getFile_path10();
                FormFile file11 = ebookbean.getFile_path11();
                FormFile file12 = ebookbean.getFile_path12();
                FormFile file13 = ebookbean.getFile_path13();
                FormFile file14 = ebookbean.getFile_path14();
                FormFile file15 = ebookbean.getFile_path15();

                String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                String contactName = Validation.checkNull((String) session.getAttribute("SESSION_USERNAME"));
                String contactPhone = Validation.checkNull((String) session.getAttribute("SESSION_USER_PHONE"));
                String ccmailid = Validation.checkNull((String) session.getAttribute("SESSION_USER_CC_MAILID"));
                if (ccmailid.length() > 0 && "NO DATA".equalsIgnoreCase(ccmailid)) {
                    session.setAttribute("SESSION_USER_CC_MAILID", ebookbean.getCcMailId());
                    ebookbean.setIsCCMailId("YES");
                } else {
                    ebookbean.setIsCCMailId("NO");
                }
                ebookbean.setShipment_type("NR");
                Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
                String cust_code = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                String custCountry = "";
                if (!"Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.Disable_Lang_Mail))) {
                    custCountry = Validation.checkNull((String) session.getAttribute("SESSION_USER_COUNTRY"));
                }
                String custCompany = Validation.checkNull((String) session.getAttribute("SESSION_USER_CONTACT"));

                Map<String, String> emptyPickUpMap = (Map<String, String>) contexts.getAttribute("EMPTYPICKUPMAP");
                routehidden = ebookbean.getEbook_masthdn();
                ArrayList routing_array1 = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");
                net.ecom.vo.EbookingValidatorFormBeanNew ve = new net.ecom.vo.EbookingValidatorFormBeanNew();
                System.out.println("[ECOM-WAR] ebookingsaveNew TemplateName " + ve.getEbook_templatename()
                        + " EbookBean " + ebookbean.getEbook_natcode()
                        + " PickPoint " + ebookbean.getEbook_pickup_point());

                //carrier code
                String client_code = (String) contexts.getAttribute("client_code");
                //
                Route eBookingrouting = null;
                ArrayList routing_array = null;
                ve.setEnableMultiAgency((String) contexts.getAttribute(GeneralConf.ENABLE_MULTIAGENCY));
                //Cutoff Details
                ebookbean.setBkgcutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookbkgcutoff"))));
                ebookbean.setDoccutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookdoccutoff"))));
                ebookbean.setVgmcutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookvgmcutoff"))));
                ebookbean.setBbrorocutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookbrorocutoff"))));
                ebookbean.setHazcutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookhazcutoff"))));
                ebookbean.setPortentrycutoff(DateConversion_New.convertDefaultDBFormatwithTime(Validation.checkNull((String) request.getParameter("ebookportentrycutoff"))));
//
                String selectedLang = (String) session.getAttribute("SelectedLanguage");
                System.out.println("[ECOM-WAR] ebookingsaveNew selectedlangis" + selectedLang);
                ve.setLangType(selectedLang);
                String strtmpname = "";
                strtmpname = request.getParameter("hdntempaltename");
                ve.setEbook_templatename(strtmpname);
                strtmpname = ve.getEbook_templatename().toString();
                System.out.println("[ECOM-WAR] ebookingsaveNew >>strtmpname>>" + strtmpname);
                BeanUtils.copyProperties(ve, ebookbean);
                ve.setRemarks(ebookbean.getEbook_status_remarks());
                ve.setEmptyPickUpName(Validation.checkNull(emptyPickUpMap.get(ebookbean.getEmptyPickup())));
                ve.setContactName(contactName);
                ve.setCustPhoneNo(contactPhone);
                System.out.println("[ECOM-WAR] ebookingsaveNew CNToOrder " + ebookbean.getHidCNToOrder()
                        + " NTToOrder " + ebookbean.getHidNTToOrder()
                        + " NT1ToOrder " + ebookbean.getHidNT1ToOrder()
                        + " BookNo " + ebookbean.getBooking_refno());
                String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                	 List personalInfo = ecomCntrlObj.getAPDetails(userCode);
                    String ApEmailID = (personalInfo != null && personalInfo.size() > 5) ? (String) personalInfo.get(5) : "";
					request.setAttribute("PersonalInfo1", personalInfo);
                //
                if (routing_array1 != null && routing_array1.size() > 0 && routehidden != null && routehidden.equalsIgnoreCase("Y")) {
                    int arraySize = routing_array1.size();
                    String str_type = "N";
                    eBookingrouting = new Route();
                    routing_array = new ArrayList();
                    String depdate = null, arrdate = null;
                    String strPol = null;
                    String strPod = null;
                    String strPla = null;
                    String strPld = null;
                    String strServiceCode = null;
                    for (int j = 0; j < arraySize; j++) {
                        eBookingrouting = (Route) routing_array1.get(j);
                        if (!"I".equalsIgnoreCase(eBookingrouting.getLordporttype())) {
                            depdate = depdate != null ? depdate : eBookingrouting.getETD1();
                            ve.setEbook_pol_etd(DateConversion_New.convertDefaultDBFormatwithTime(depdate));
                        }
                        if (!"I".equalsIgnoreCase(eBookingrouting.getDisporttype())) {
                            arrdate = eBookingrouting.getETA1();
                            ve.setEbook_pol_eta(DateConversion_New.convertDefaultDBFormatwithTime(arrdate));
                        }
                        if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(eBookingrouting.getLegType()) || "F".equalsIgnoreCase(eBookingrouting.getLegType()))) {
                            str_type = "Y";
                            ve.setEbook_servicecode(eBookingrouting.getService());
                            ve.setEbook_vesselcode(eBookingrouting.getVessel());
                            ve.setEbook_voyage(eBookingrouting.getVoyage());
                            if (eBookingrouting.getLoadter() != null && eBookingrouting.getLoadter().length() > 0) {
                                ve.setEbook_terminal(eBookingrouting.getLoadter());
                            }
                            ve.setEbook_bound_leg(eBookingrouting.getBound());
                            ve.setGroupcallid(eBookingrouting.getGrpCallId());
                            slottype = eBookingrouting.getLoadcallid();
                        }
                        strServiceCode = eBookingrouting.getService();
                        if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                            strPol = strPol == null ? eBookingrouting.getLoadport() : strPol;
                            strPod = eBookingrouting.getDisport();
                            strPld = eBookingrouting.getDisport();
                            if (eBookingrouting.getDister() != null && eBookingrouting.getDister().length() > 0) {
                                ve.setEbook_terminalpod(eBookingrouting.getDister());
                            }
                        } else {
                            strPla = strPla == null ? eBookingrouting.getLoadport() : strPla;
                            strPld = eBookingrouting.getDisport();
                        }
                        //
                        strPla = strPla == null ? strPol : strPla;
                        //
                        System.out.println("[ECOM-WAR] ebookingsaveNew strPla " + strPla + " strPol " + strPol + " strPod " + strPod + " strPld " + strPld);

                        //
                        ve.setEbook_origin(strPla);
                        ve.setEbook_portofloading(strPol);
                        ve.setEbook_portofdischarge(strPod);
                        ve.setEbook_delivery(strPld);
                        //
                        eBookingrouting.setArrdate(DateConversion_New.convertDefaultDBFormatwithTime(eBookingrouting.getArrdate()));
                        eBookingrouting.setETA1(DateConversion_New.convertDefaultDBFormatwithTime(eBookingrouting.getETA1()));
                        eBookingrouting.setETD1(DateConversion_New.convertDefaultDBFormatwithTime(eBookingrouting.getETD1()));
                        routing_array.add(eBookingrouting);
                    }
                } else {
                    ve.setEbook_origin(ebookbean.getEbook_origin());
                    ve.setEbook_delivery(ebookbean.getEbook_delivery());
                    //
                    ve.setEbook_servicecode("");
                    ve.setEbook_vesselcode("");
                    ve.setEbook_voyage("");
                    ve.setEbook_terminal("");
                    ve.setEbook_bound_leg("");
                    ve.setGroupcallid("");
                    slottype = "";
                    ve.setEbook_pol_etd(null);
                    ve.setEbook_pol_eta(null);
                    ve.setEbook_terminalpod("");
                    ve.setEbook_portofloading("");
                    ve.setEbook_portofdischarge("");
                    //
                    String lordporttype = Validation.checkNull(bookingObj.getPortType(ebookbean.getEbook_origin()));
                    String disporttype = Validation.checkNull(bookingObj.getPortType(ebookbean.getEbook_delivery()));
                    if (!"I".equalsIgnoreCase(lordporttype)) {
                        ve.setEbook_portofloading(ebookbean.getEbook_origin());
                    }
                    if (!"I".equalsIgnoreCase(disporttype)) {
                        ve.setEbook_portofdischarge(ebookbean.getEbook_delivery());
                    }
                }
                //Buyer Customer
                String index[] = request.getParameterValues("BuyercountRowID");
                String custCode = "", customerCountry = "", custName = "", custAddress = "", custCityDesc = "", custPhone = "", custFax = "", custEmail = "", custAddress1 = "", custState = "", custEori = "", custPersonType = "";

                if (index != null && index.length > 0) {
                    int indexLen = index.length;
                    customerDetailes_array = new ArrayList();
                    System.out.println("Buyer index==>" + index + "indexLen=>" + indexLen);
                    for (int i = 0; i < indexLen; i++) {
                        custName = "ebook_Buyername" + index[i];
                        if (Validation.checkNull((String) request.getParameter(custName)).length() > 0) {
                            custCode = "ebook_Buyercode" + index[i];
//                            System.out.println("index==>" + index[i]);
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
                            ebookbeancust = new EbookingValidatorFormBeanNew();
                            ebookbeancust.setEbook_Custcode(Validation.checkNull((String) request.getParameter(custCode)));
                            ebookbeancust.setEbook_Custemail(Validation.checkNull((String) request.getParameter(custEmail)));
                            ebookbeancust.setEbook_Custname(Validation.checkNull((String) request.getParameter(custName)));
                            ebookbeancust.setEbook_Custphone(Validation.checkNull((String) request.getParameter(custPhone)));
                            ebookbeancust.setEbook_Custstate(Validation.checkNull((String) request.getParameter(custState)));
                            ebookbeancust.setEbook_Custaddress(Validation.checkNull((String) request.getParameter(custAddress)));
                            ebookbeancust.setEbook_Custaddress1(Validation.checkNull((String) request.getParameter(custAddress1)));
                            ebookbeancust.setEbook_Custcity_desc(Validation.checkNull((String) request.getParameter(custCityDesc)));
                            ebookbeancust.setEbook_Custcountry(Validation.checkNull((String) request.getParameter(custCountry)));
                            ebookbeancust.setEbook_Custfax(Validation.checkNull((String) request.getParameter(custFax)));
                            ebookbeancust.setEbook_Custeori(Validation.checkNull((String) request.getParameter(custEori)));
                            ebookbeancust.setEbook_Custpersontype(Validation.checkNull((String) request.getParameter(custPersonType)));

// ebookbeancust.setEbook_Custid(Validation.checkNull((String) request.getParameter("custId")));
// ebookbeancust.setEbook_CustSelect(Validation.checkNull((String) request.getParameter("custSelect")));
// ebookbeancust.setEbook_Custcountry_desc(Validation.checkNull((String) request.getParameter("custCountryDesc")));
                            customerDetailes_array.add(ebookbeancust);
                        }
                    }

                }
                ArrayList buyer_list = null;
                if (customerDetailes_array != null && customerDetailes_array.size() > 0) {
                    buyer_list = new ArrayList();
                    int con_len = customerDetailes_array.size();
                    for (int i = 0; i < con_len; i++) {
                        EbookingValidatorFormBeanNew customer_grid = (EbookingValidatorFormBeanNew) customerDetailes_array.get(i);
                        net.ecom.vo.EbookingValidatorFormBeanNew cust_grid = new net.ecom.vo.EbookingValidatorFormBeanNew();
                        BeanUtils.copyProperties(cust_grid, customer_grid);
                        buyer_list.add(cust_grid);
                    }
                }
                //Seller Customer
                String sellerindex[] = request.getParameterValues("SellercountRowID");
                // String custCode = "", customerCountry = "", custName = "", custAddress = "", custCityDesc = "", custPhone = "", custFax = "", custEmail = "", custAddress1 = "", custState = "", custEori = "", custPersonType = "";

                if (sellerindex != null && sellerindex.length > 0) {
                    int indexLen = sellerindex.length;
                    customerDetailes_array = new ArrayList();
//                    System.out.println("index==>" + sellerindex + "indexLen=>" + indexLen);

                    for (int i = 0; i < indexLen; i++) {
                        custName = "ebook_Sellername" + sellerindex[i];
                        if (Validation.checkNull((String) request.getParameter(custName)).length() > 0) {
                            custCode = "ebook_Sellercode" + sellerindex[i];
//                            System.out.println("index==>" + sellerindex[i]);
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
                            ebookbeancust = new EbookingValidatorFormBeanNew();
                            ebookbeancust.setEbook_Custcode(Validation.checkNull((String) request.getParameter(custCode)));
                            ebookbeancust.setEbook_Custemail(Validation.checkNull((String) request.getParameter(custEmail)));
                            ebookbeancust.setEbook_Custname(Validation.checkNull((String) request.getParameter(custName)));
                            ebookbeancust.setEbook_Custphone(Validation.checkNull((String) request.getParameter(custPhone)));
                            ebookbeancust.setEbook_Custstate(Validation.checkNull((String) request.getParameter(custState)));
                            ebookbeancust.setEbook_Custaddress(Validation.checkNull((String) request.getParameter(custAddress)));
                            ebookbeancust.setEbook_Custaddress1(Validation.checkNull((String) request.getParameter(custAddress1)));
                            ebookbeancust.setEbook_Custcity_desc(Validation.checkNull((String) request.getParameter(custCityDesc)));
                            ebookbeancust.setEbook_Custcountry(Validation.checkNull((String) request.getParameter(custCountry)));
                            ebookbeancust.setEbook_Custfax(Validation.checkNull((String) request.getParameter(custFax)));
                            ebookbeancust.setEbook_Custeori(Validation.checkNull((String) request.getParameter(custEori)));
                            ebookbeancust.setEbook_Custpersontype(Validation.checkNull((String) request.getParameter(custPersonType)));

// ebookbeancust.setEbook_Custid(Validation.checkNull((String) request.getParameter("custId")));
// ebookbeancust.setEbook_CustSelect(Validation.checkNull((String) request.getParameter("custSelect")));
// ebookbeancust.setEbook_Custcountry_desc(Validation.checkNull((String) request.getParameter("custCountryDesc")));
                            customerDetailes_array.add(ebookbeancust);
                        }
                    }

                }
                ArrayList seller_list = null;
                if (customerDetailes_array != null && customerDetailes_array.size() > 0) {
                    seller_list = new ArrayList();
                    int con_len = customerDetailes_array.size();
                    for (int i = 0; i < con_len; i++) {
                        EbookingValidatorFormBeanNew customer_grid = (EbookingValidatorFormBeanNew) customerDetailes_array.get(i);
                        net.ecom.vo.EbookingValidatorFormBeanNew cust_grid = new net.ecom.vo.EbookingValidatorFormBeanNew();
                        BeanUtils.copyProperties(cust_grid, customer_grid);
                        seller_list.add(cust_grid);
                    }
                }

                if (seller_list != null && !seller_list.isEmpty() || buyer_list != null && !buyer_list.isEmpty()) {
                    custMap = new HashMap<>();
                    custMap.put("SL", seller_list);
                    custMap.put("BY", buyer_list);
                }

                //
                String eqpType = null, eqpSize = null, eqpQty = null, soc = null, hzd = null, commodity = null, packagename = null, hscode = null;
                String packageQty = null, eqpStatus = null, tareWt = null, cargoWt = null, netWt = null, grossWt = null, volume = null, netWtUnit = null, cargoWtUnit = null, actCommoName = null, cmdtyhblno = null, sellersName = null, buyersName = null, buyersCode = null, sellersCode = null;
                String grossWtUnit = null, volUnit = null, othersCmdty = null, marinePollut = null, unno = null, flashPoint = null, flashUnit = null, commodityothers = null;
                String shippingName = null, dgClass = null, presetTemp = null, minTemp = null, maxTemp = null, tempUnit = null, nor = null;
                String dimUnit = null, owLeft = null, owRight = null, oh = null, olAft = null, olForward = null, hzdDiv = null, refDiv = null, oogDiv = null, eqptypeval = null;
                String commodityval = null, packageval = null, refclass = null, oogclass = null, hzdclass = null, checkother = null;
                String hidden_oog = null, hidden_lcl = null, hidden_hzd = null, hidden_rfr = null, hidden_soc = null, packagecodes = null, packagecode = null, commoditycode = null, commodities = null, hscommodities = null;
                String comid = null, conid = null, othercommodity = null, otherhscode = null, isFakComdty = null, commodityname = null, hscommoditycode = null;
                String humidityflag = null, humidity = null, drains = null, co2 = null, o2 = null, ventilation = null;
                String select[] = request.getParameterValues("cargocount");
                if (select != null && select.length > 0) {
                    int selectLen = select.length;
                    for (int i = 0; i < selectLen; i++) {
                        eqpType = "eqpType" + select[i];
                        eqpSize = "eqpSize" + select[i];
                        eqpQty = "eqpQty" + select[i];
                        soc = "soc" + select[i];
                        hzd = "hzd" + select[i];
                        commodity = "commodity" + select[i];
                        packagename = "packagename" + select[i];
                        hscode = "hscode" + select[i];
                        packageQty = "packageQty" + select[i];
                        eqpStatus = "eqpStatus" + select[i];
                        tareWt = "tareWt" + select[i];
                        cargoWt = "cargoWt" + select[i];
                        netWt = "netWt" + select[i];
                        actCommoName = "actCommoName" + select[i];
                        sellersName = "sellersName" + select[i];
                        buyersName = "buyersName" + select[i];
                        cmdtyhblno = "cmdtyhblno" + select[i];
                        buyersCode = "buyersCode" + select[i];
                        sellersCode = "sellerCode" + select[i];
                        grossWt = "grossWt" + select[i];
                        volume = "volume" + select[i];
                        cargoWtUnit = "cargoWtUnit" + select[i];
                        netWtUnit = "netWtUnit" + select[i];
                        grossWtUnit = "grossWtUnit" + select[i];
                        volUnit = "volUnit" + select[i];
                        othersCmdty = "othersCmdty" + select[i];
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
                        hzdDiv = "hzdDiv" + select[i];
                        refDiv = "refDiv" + select[i];
                        oogDiv = "oogDiv" + select[i];
                        refclass = "refclass" + select[i];
                        oogclass = "oogclass" + select[i];
                        hzdclass = "hzdclass" + select[i];
                        hidden_oog = "hidden_oog" + select[i];
                        hidden_lcl = "hidden_oog" + select[i];
                        hidden_hzd = "hidden_hzd" + select[i];
                        hidden_rfr = "hidden_rfr" + select[i];
                        hidden_soc = "hidden_soc" + select[i];
                        othercommodity = "othercommodity" + select[i];
                        otherhscode = "otherhscode" + select[i];
                        comid = "comid" + select[i];
                        conid = "conid" + select[i];
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
                        commoditycode = "";
                        commodities = Validation.checkNull(request.getParameter(commodity));
                        checkother = Validation.checkNull(request.getParameter(commodityothers));
                        if (commodities != null && "N".equalsIgnoreCase(checkother)) {
                            String[] commodityvalue = commodities.split("~~");
                            commoditycode = commodityvalue[1];
                            // commodityname = commodityvalue[0];
                        }
                        hscommodities = Validation.checkNull(request.getParameter(hscode));
                        commodityname = "";
                        hscommoditycode = "";
                        isFakComdty = "N";
                        System.out.println("[ECOM-WAR] ebookingsaveNew >>>hscommodities>>>>" + hscommodities);
                        if (hscommodities != null && hscommodities.length() > 0 && !"1".equalsIgnoreCase(ebookbean.getAppVersion())) {
                            System.out.println("[ECOM-WAR] ebookingsaveNew >>fakComdtyCode>>" + fakComdtyCode + ">>>commoditycode>>" + commoditycode);
                            if (hscommodities.contains("~~")) {
                                String[] commodityvalue = hscommodities.split("~~");
                                hscommoditycode = commodityvalue[1];
                                commodityname = commodityvalue[0];
                            } else if (commoditycode != null && commoditycode.length() > 0 && commoditycode.trim().equalsIgnoreCase(fakComdtyCode)) {
                                isFakComdty = "Y";
                                hscommoditycode = hscommodities;
                                commodityname = hscommodities;
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
                        System.out.println("[ECOM-WAR] ebookingsaveNew >>hscommoditycode>>" + hscommoditycode);
                        ebookbeancargo = new EbookingValidatorFormBeanNew();
                        ebookbeancargo.setIsFakCommodity(isFakComdty);
                        ebookbeancargo.setEbooking_commodity_desc(Validation.checkNull(commodityname));
                        ebookbeancargo.setHscode(Validation.checkNull(hscommoditycode));
                        ebookbeancargo.setEbook_commoditycode(Validation.checkNull(commoditycode));
                        ebookbeancargo.setEbooking_commodity(Validation.checkNull(commoditycode));
                        ebookbeancargo.setEbook_packquantity(Validation.checkNull((String) request.getParameter(packageQty)));
                        ebookbeancargo.setEbook_actCommoName(Validation.checkNull((String) request.getParameter(actCommoName)));
                        ebookbeancargo.setEbook_cmdtyhblno(Validation.checkNull((String) request.getParameter(cmdtyhblno)));
                        ebookbeancargo.setEbook_cmdtysellerName(Validation.checkNull((String) request.getParameter(sellersName)));
                        ebookbeancargo.setEbook_cmdtybuyerName(Validation.checkNull((String) request.getParameter(buyersName)));
                        ebookbeancargo.setEbook_cmdtysellercode(Validation.checkNull((String) request.getParameter(sellersCode)));
                        ebookbeancargo.setEbook_cmdtybuyercode(Validation.checkNull((String) request.getParameter(buyersCode)));
                        ebookbeancargo.setEbook_cargoweight(Validation.checkNull((String) request.getParameter(cargoWt)));
                        ebookbeancargo.setEbook_netweight(Validation.checkNull((String) request.getParameter(netWt)));
                        ebookbeancargo.setEbook_weightunit(Validation.checkNull((String) request.getParameter(cargoWtUnit)));
                        ebookbeancargo.setEbook_netweightunit(Validation.checkNull((String) request.getParameter(netWtUnit)));
                        String eqp_type = Validation.checkNull((String) request.getParameter(eqpType));
                        String setType = Validation.checkNull((String) request.getParameter(eqpSize)) + Validation.checkNull((String) request.getParameter(eqpType));
                        ebookbeancargo.setEbook_eqp(Validation.checkNull((String) request.getParameter(eqpSize)));
                        ebookbeancargo.setEbook_eqp_qty(Validation.checkNull((String) request.getParameter(eqpQty)));
                        ebookbeancargo.setEbook_eqp_type(eqp_type);
                        ebookbeancargo.setEbook_eqp_type_desc(setType);
                        ebookbeancargo.setEbook_mintemp((String) request.getParameter(minTemp));
                        ebookbeancargo.setEbook_maxtemp((String) request.getParameter(maxTemp));
                        ebookbeancargo.setEbook_owleft(Validation.checkNull((String) request.getParameter(owLeft)));
                        ebookbeancargo.setEbook_owright(Validation.checkNull((String) request.getParameter(owRight)));
                        ebookbeancargo.setEbook_oh(Validation.checkNull((String) request.getParameter(oh)));
                        ebookbeancargo.setEbook_olaft(Validation.checkNull((String) request.getParameter(olAft)));
                        ebookbeancargo.setEbook_olforward(Validation.checkNull((String) request.getParameter(olForward)));
                        ebookbeancargo.setEbook_dimensionunit(Validation.checkNull((String) request.getParameter(dimUnit)));
                        ebookbeancargo.setEbook_flashpoint(Validation.checkNull((String) request.getParameter(flashPoint)));
                        ebookbeancargo.setEbook_unno(Validation.checkNull((String) request.getParameter(unno)));
                        ebookbeancargo.setEbook_dgclass(Validation.checkNull((String) request.getParameter(dgClass)));
                        ebookbeancargo.setEbook_shipname(Validation.checkNull((String) request.getParameter(shippingName)));
                        ebookbeancargo.setEbook_cargovolume(Validation.checkNull((String) request.getParameter(volume)));
                        ebookbeancargo.setEbook_volumeunit(Validation.checkNull((String) request.getParameter(volUnit)));
                        ebookbeancargo.setEbook_packagecode(Validation.checkNull(packagecode));
                        ebookbeancargo.setEbook_presetemp(Validation.checkNull((String) request.getParameter(presetTemp)));
                        ebookbeancargo.setEbook_flashunit(Validation.checkNull((String) request.getParameter(flashUnit)));
                        ebookbeancargo.setEbook_poluttunit(Validation.checkNull((String) request.getParameter(marinePollut)));
                        ebookbeancargo.setEbook_nor(Validation.checkNull((String) request.getParameter(nor)));
                        ebookbeancargo.setEbook_temperunit(Validation.checkNull((String) request.getParameter(tempUnit)));
                        ebookbeancargo.setOog(Validation.checkNull((String) request.getParameter(hidden_oog)));
                        ebookbeancargo.setHzd(Validation.checkNull((String) request.getParameter(hzd)));
                        ebookbeancargo.setLcl(Validation.checkNull((String) request.getParameter(hidden_lcl)));
                        ebookbeancargo.setRfr(Validation.checkNull((String) request.getParameter(hidden_rfr)));
                        ebookbeancargo.setSoc(("Y".equalsIgnoreCase(Validation.checkNull((String) request.getParameter(soc))) ? "Y" : "N"));
                        ebookbeancargo.setEbook_tareweight(Validation.checkNull((String) request.getParameter(tareWt)));
                        ebookbeancargo.setEbook_grossweight(Validation.checkNull((String) request.getParameter(grossWt)));

                        ebookbeancargo.setEqpstatus(Validation.checkNull((String) request.getParameter(eqpStatus)));
                        ebookbeancargo.setCommodityothers(Validation.checkNull((String) request.getParameter(commodityothers)));
                        ebookbeancargo.setEbook_comid(Validation.checkNull(request.getParameter(comid)));
                        ebookbeancargo.setEbook_conid(Validation.checkNull(request.getParameter(conid)));

                        ebookbeancargo.setHumidityflag(Validation.checkNull(request.getParameter(humidityflag)));
                        ebookbeancargo.setHumidity(Validation.checkNull(request.getParameter(humidity)));
                        ebookbeancargo.setDrains(Validation.checkNull(request.getParameter(drains)));
                        ebookbeancargo.setCo2(Validation.checkNull(request.getParameter(co2)));
                        ebookbeancargo.setO2(Validation.checkNull(request.getParameter(o2)));
                        ebookbeancargo.setVentilation(Validation.checkNull(request.getParameter(ventilation)));
                        System.out.println("[ECOM-WAR] ebookingsaveNew ventvalue" + request.getParameter(ventilation));
                        
                        // Unified DG Extraction - Always extract as list (works for single & multiple)
                        if ("Y".equalsIgnoreCase(ebookbeancargo.getHzd())) {
                            // Use EJB VO class for dgList to avoid serialization issues
                            List<net.ecom.vo.EbookingValidatorFormBeanNew> dgListVO = new ArrayList<>();
                            String dgCountParam = "dgCount" + select[i];
                            String dgCountStr = Validation.checkNull(request.getParameter(dgCountParam));
                            int dgCount = 1; // Default to 1 for single entry
                            
                            try {
                                if (dgCountStr != null && dgCountStr.length() > 0) {
                                    dgCount = Integer.parseInt(dgCountStr);
                                }
                            } catch (Exception e) {
                                System.out.println("[ECOM-WAR] ebookingsaveNew Error parsing dgCount: " + e.getMessage());
                            }
                            
                            System.out.println("[ECOM-WAR] ebookingsaveNew DG Extraction: Container=" + select[i] + ", dgCount=" + dgCount);
                            
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
                                    // Create EJB VO object (not web formbean)
                                    net.ecom.vo.EbookingValidatorFormBeanNew dgEntry = new net.ecom.vo.EbookingValidatorFormBeanNew();
                                    dgEntry.setEbook_unno(unnoVal);
                                    dgEntry.setEbook_dgclass(Validation.checkNull(request.getParameter(dgClassParam)));
                                    dgEntry.setEbook_flashpoint(Validation.checkNull(request.getParameter(flashPointParam)));
                                    dgEntry.setEbook_flashunit(Validation.checkNull(request.getParameter(flashUnitParam)));
                                    dgEntry.setEbook_poluttunit(Validation.checkNull(request.getParameter(marinePollutParam)));
                                    dgEntry.setEbook_shipname(Validation.checkNull(request.getParameter(shippingNameParam)));
                                    dgEntry.setEbook_comid(String.valueOf(dgIdx)); // Set itemno for this DG entry
                                    dgListVO.add(dgEntry);
                                    
                                    System.out.println("[ECOM-WAR] ebookingsaveNew DG Entry " + dgIdx + ": UN=" + unnoVal 
                                        + ", Class=" + dgEntry.getEbook_dgclass() 
                                        + ", FP=" + dgEntry.getEbook_flashpoint());
                                }
                            }
                            
                            if (!dgListVO.isEmpty()) {
                                ebookbeancargo.setDgDetailsList(dgListVO);
                                ebookbeancargo.setDgCount(dgListVO.size());
                                System.out.println("[ECOM-WAR] ebookingsaveNew Set dgDetailsList with " + dgListVO.size() + " entries");
                            }
                        }
                        
                        container_grid_array.add(ebookbeancargo);
                    }
                }
                ArrayList container_grid_new = new ArrayList();
                String book_no = "";
                String SaveType = "";
                ArrayList all = new ArrayList();
                if (container_grid_array != null && container_grid_array.size() > 0) {
                    int con_len = container_grid_array.size();
                    for (int i = 0; i < con_len; i++) {
                        EbookingValidatorFormBeanNew container_grid = (EbookingValidatorFormBeanNew) container_grid_array.get(i);
                        net.ecom.vo.EbookingValidatorFormBeanNew cont_grid = new net.ecom.vo.EbookingValidatorFormBeanNew();
                        BeanUtils.copyProperties(cont_grid, container_grid);
                        container_grid_new.add(cont_grid);
                        all.add(container_grid_new);
                        if ("".equals(container_grid.getEbook_eqp_qty())) {
                        } else {
                            eqpQuantity = container_grid.getEbook_eqp_qty();
                        }
                    }

                } else {
                    all.add(container_grid_new);
                }

                if (routing_array != null && routing_array.size() > 0) {
                    all.add(routing_array);
                } else {
                    all.add(routing_array);
                }
                ArrayList voidcargolist = null;
                ArrayList voidcomlist = null;
                System.out.println("[ECOM-WAR] ebookingsaveNew Save Type = " + btn_type);
                if ("add".equalsIgnoreCase(btn_type)) {
                    SaveType = "S";
                } else if ("draftadd".equalsIgnoreCase(btn_type)) {
                    SaveType = "D";
                } else if ("edit".equalsIgnoreCase(btn_type)) {
                    SaveType = "S";
                } else if ("draftedit".equalsIgnoreCase(btn_type)) {
                    SaveType = "D";
                } else if ("tempadd".equalsIgnoreCase(btn_type)) {
                    SaveType = "T";
                } else if ("templateedit".equalsIgnoreCase(btn_type)) {
                    SaveType = "TE";
                    ve.setBooking_refno(ebookbean.getHidEcomBookNo());
                }
                // Getting Void Status Container
                ArrayList Draft_Container_list = null;
                if ("edit".equalsIgnoreCase(btn_type) || "draftedit".equalsIgnoreCase(btn_type)) {
                    Draft_Container_list = (ArrayList) session.getAttribute("Draft_ebooking_cargo_grid");
                }
                if (Draft_Container_list != null && Draft_Container_list != null) {
                    int len = Draft_Container_list.size();
                    net.ecom.vo.EbookingValidatorFormBeanNew DarftVo = null;
                    voidcargolist = new ArrayList();
                    voidcomlist = new ArrayList();
                    for (int m = 0; m < len; m++) {
                        DarftVo = (net.ecom.vo.EbookingValidatorFormBeanNew) Draft_Container_list.get(m);
                        int compare = container_grid_new.indexOf(DarftVo);
                        if (compare == -1) {
                            voidcargolist.add(DarftVo.getEbook_conid());
                            voidcomlist.add(DarftVo.getEbook_comid());
                        }
                    }
                }
                //Get Haulage details 
                String jsonhaulagestr = (String) request.getParameter("haulagedetailsjsonstr");
                System.out.println("[ECOM-WAR] ebookingsaveNew jsonhaulagestr===> " + jsonhaulagestr);
                List<BookingHaulagedetailsVo> haulagelist = null;
                if (jsonhaulagestr != null && jsonhaulagestr.length() > 0) {
                    Gson gson = new Gson();
                    Type jstype = new TypeToken<List<BookingHaulagedetailsVo>>() {
                    }.getType();
                    haulagelist = gson.fromJson(jsonhaulagestr, jstype);
                    if (haulagelist != null && haulagelist.size() > 0) {
                        haulagelist.forEach(haullis -> {
                            haullis.setDropdate(DateConversion_New.convertDefaultDBFormatwithTime(haullis.getDropdate()));
                        });
                    }
                    ve.setHaulagelist(haulagelist);
                }

                //
                book_no = ebookbean.getBooking_refno();
                //                
                FileDetailsVO fls = new FileDetailsVO();

                fls.setFilePath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH));
                String enableFileBackendValidation = (String) contexts.getAttribute(GeneralConf.EnableFileBackendValidation);
                System.out.println("[ECOM-WAR] ebookingsaveNew enableFileBackendValidation" + enableFileBackendValidation);
                String ecomFileExt = (String) contexts.getAttribute(GeneralConf.EcomFileExtension);
                System.out.println("[ECOM-WAR] ebookingsaveNew ecomFileExt" + ecomFileExt);
                FileExtensionValidation flext = new FileExtensionValidation();
                String restrictedFiled = null;
                String restrictedFiledSize = null;

                if (file1 != null && file1.getFileName() != null && file1.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file1, ecomFileExt, request)) {
                    fls.setVgmFileData(file1.getFileData());
                    fls.setVgmFileName(file1.getFileName());
                    fls.setFileAvailable(true);
                } else if (file1 != null && file1.getFileName() != null && file1.getFileName().length() > 0) {
                    // fls.setVgmFileName(ebookbean.getFilename1());
                    fls.setVgmFileName(null);
                }

                if (file2 != null && file2.getFileName() != null && file2.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file2, ecomFileExt, request)) {
                    fls.setDgFileData(file2.getFileData());
                    fls.setDgFileName(file2.getFileName());
                    fls.setFileAvailable(true);
                } else if (file2 != null && file2.getFileName() != null && file2.getFileName().length() > 0) {
                    // fls.setDgFileName(ebookbean.getFilename2());
                    fls.setDgFileName(null);
                }

                if (file3 != null && file3.getFileName() != null && file3.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file3, ecomFileExt, request)) {
                    fls.setLolFileData(file3.getFileData());
                    fls.setLolFileName(file3.getFileName());
                    fls.setFileAvailable(true);
                } else if (file3 != null && file3.getFileName() != null && file3.getFileName().length() > 0) {
                    //fls.setLolFileName(ebookbean.getFilename3());
                    fls.setLolFileName(null);
                }
                if (file4 != null && file4.getFileName() != null && file4.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file4, ecomFileExt, request)) {
                    fls.setMsdsFileData(file4.getFileData());
                    fls.setMsdsFileName(file4.getFileName());
                    fls.setFileAvailable(true);
                } else if (file4 != null && file4.getFileName() != null && file4.getFileName().length() > 0) {
                    //fls.setMsdsFileName(ebookbean.getFilename4());
                    fls.setMsdsFileData(null);
                }
                if (file5 != null && file5.getFileName() != null && file5.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file5, ecomFileExt, request)) {
                    fls.setOthersFileData(file5.getFileData());
                    fls.setOthersFileName(file5.getFileName());
                    fls.setFileAvailable(true);
                } else if (file5 != null && file5.getFileName() != null && file5.getFileName().length() > 0) {
                    //fls.setOthersFileName(ebookbean.getFilename5());
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

                ve.setEnableZipConversion(Validation.checkNull((String) contexts.getAttribute(GeneralConf.Enable_ZipFile_Conversion)));
                System.out.println("[ECOM-WAR] ebookingsaveNew  ===>>>book_no" + book_no);
              
                ENSDeclarantVo edvo = new ENSDeclarantVo();
                BeanUtils.copyProperties(edvo, ebookbean);
                ve.setEnsdecvo(edvo);
             
                    String originPort = ve.getEbook_origin();
                    String deliveryPort = ve.getEbook_delivery();
                    String originCountry = ve.getEbook_origin_country();
                    String deliveryCountry = ve.getEbook_delivery_country();
                    
                    // System.out.println("[ECOM-WAR] ebookingsaveNew Before swap - Origin: " + originPort 
                    //         + ", Delivery: " + deliveryPort
                    //         + ", OriginCountry: " + originCountry
                    //         + ", DeliveryCountry: " + deliveryCountry);
                    
                    // Swap origin (PLA) to actual port
                    String actualOrigin = MaskPortUtil.swapmaskporttoactual(originPort);
                    ve.setEbook_origin(actualOrigin);
                    
                    // Swap delivery (PLD) to actual port
                    String actualDelivery = MaskPortUtil.swapmaskporttoactual(deliveryPort);
                    ve.setEbook_delivery(actualDelivery);
                    
                    // Swap origin country to actual port with name
                    String actualOriginCountry = MaskPortUtil.swapmaskporttoactualportwithname(originCountry);
                    ve.setEbook_origin_country(actualOriginCountry);
                    
                    // Swap delivery country to actual port with name
                    String actualDeliveryCountry = MaskPortUtil.swapmaskporttoactualportwithname(deliveryCountry);
                    ve.setEbook_delivery_country(actualDeliveryCountry);
                    
                    // System.out.println("[ECOM-WAR] ebookingsaveNew After swap - Origin: " + actualOrigin 
                    //         + ", Delivery: " + actualDelivery
                    //         + ", OriginCountry: " + actualOriginCountry
                    //         + ", DeliveryCountry: " + actualDeliveryCountry);
            

               
                String addremarks = ebookbean.getEbook_status_remarks();
                ReturnVO returnVO = bookingObj.saveToBooking(ve, all, cust_code, loginUser, book_no, SaveType, voidcargolist, voidcomlist, eqpQuantity, client_code, strtmpname, slottype, ApEmailID, fls, custTypeConfig, custCountry, custMap);
                String useremail = (String) session.getAttribute("SESSION_EMAIL");
                String strDate = null;
                Date dt = null;
                SimpleDateFormat reqFormat = null;
                reqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dt = new Date();
                String message = null;
                strDate = reqFormat.format(dt);
                String formatDate = DateConversion_New.convertDefaultFormat(strDate);
                String frommail = (String) contexts.getAttribute("clientfrommail");
                String initiatorID = "ECOM-BKG";
                addccMailid = (String) contexts.getAttribute(GeneralConf.additionalbkgccmail);
                System.out.println("[ECOM-WAR] ebookingsaveNew >>returnVO.getStrRefNo().equalsIgnoreCase(\"Error\")>>" + returnVO.getStrRefNo().equalsIgnoreCase("Error"));
                if (returnVO.getStrRefNo().length() > 0 && !returnVO.getStrRefNo().equalsIgnoreCase("Error")) {
                    System.out.println("[ECOM-WAR] ebookingsaveNew save Type = " + returnVO.getStrSaveType());
                    try {
                        InsuranceUtils IU = new InsuranceUtils();
                        String isaviblequote = (String) session.getAttribute("CREATE_QUOTE_NEW");
                        System.out.println("[ECOM-WAR] ebookingsaveNew isaviblequote " + isaviblequote);
                        List insurancelist = null;
                        List insurancecusttypelis = (List) session.getAttribute("CREATE_QUOTE_CUST_TYPE");
                        List insuredpartylist = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY");
                        List insuredpartyemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL");
                        List insuredpartyprimaryemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL");
                        System.out.println("[ECOM-WAR] ebookingsaveNew insurancecusttypelis==> " + insurancecusttypelis + " CREATE_QUOTE_INPUT_JSON==> " + session.getAttribute("CREATE_QUOTE_INPUT_JSON"));
                        if (isaviblequote != null && "Y".equalsIgnoreCase(isaviblequote)) {
                            insurancelist = IU.callAPI("quotecreate", returnVO.getStrRefNo(), contexts, session);
                            System.out.println("[ECOM-WAR] ebookingsaveNew insurancelist==> " + insurancelist);
                            inslis = IU.arrangeInsuranceDetailsToInsert(insurancecusttypelis, insurancelist, "BKG", insuredpartylist, insuredpartyemail, insuredpartyprimaryemail);
                            bookingObj.setInsurancedetails(inslis, returnVO.getReaderid());
                            if (inslis != null && inslis.size() > 0) {
                                request.setAttribute("InsuranceMSGMessage", "Insurance details are saved successfully.");
                            } else {
                                request.setAttribute("InsuranceMSGMessage", "Insurance details are not saved.");
                            }
                            session.setAttribute("CREATE_QUOTE_NEW", "N");
                            session.setAttribute("CREATE_QUOTE_CUST_TYPE", null);
                            session.setAttribute("CREATE_QUOTE_INPUT_JSON", null);
                            session.setAttribute("CREATE_QUOTE_INSURED_PARTY", null);
                            session.setAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL", null);
                            session.setAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL", null);
                        }
                        String insuranceoptout = (String) session.getAttribute("INSURANCE_OPTOUT_JSON");
                        System.out.println("[ECOM-WAR] ebookingsaveNew isuranceoptout....." + insuranceoptout);
                        if (insuranceoptout != null && insuranceoptout.length() > 0) {
                            insurancelist = IU.callAPI("optout", returnVO.getStrRefNo(), contexts, session);
                            session.setAttribute("INSURANCE_OPTOUT_JSON", null);//  
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if ("S".equalsIgnoreCase(returnVO.getStrSaveType())) {
                        String bkgAutoProcessCountries = Validation.checkNull((String) contexts.getAttribute(GeneralConf.Reg_Auto_Process_Country));
                        if (bkgAutoProcessCountries.length() > 0) {
                            String originCountryCode = Validation.checkNull(ve.getEbook_bookingcountry());
                            if (originCountryCode.length() > 0 && ("," + bkgAutoProcessCountries.toUpperCase() + ",").contains("," + originCountryCode.toUpperCase() + ",")) {
                                request.setAttribute("yemenBookingMsg", Validation.checkNull((String) contexts.getAttribute("BKG_Auto_Process_Msg")));
                                System.out.println("[ECOM-WAR] ebookingsaveNew Auto Process country booking detected, originPort=" + originPort + ", countries=" + bkgAutoProcessCountries);
                            }
                        }
                        Mailer mail = new Mailer();
                        Map<String, String> mailAlertMap = (Map<String, String>) session.getAttribute("USERMAILALERTSESSIONMAP");
                        boolean sendMail = false;
                        if (mailAlertMap != null && mailAlertMap.size() > 0 && "Y".equalsIgnoreCase(mailAlertMap.get(returnVO.getActivityCode()))) {
                            sendMail = true;
                        }
                        String details_mail = "";
                        if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_CUST)) || "Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_AGENCY))) {
                            //Preview mail start
                            if (sendMail || (returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0)) {

                                HashMap masterCache = new HashMap();
                                HashMap carriageContacts = (HashMap) session.getAttribute("CARRIERCONTACTSMAP");
                                List getAddInfo = (List) contexts.getAttribute(GeneralConf.AdditionalInfoBkg);
                                Map<String, String> intransitMap = (Map<String, String>) contexts.getAttribute("PODINTRANSITMAPCONTEXT");
                                String Ventilationlabel = "Ventilation" + (String) contexts.getAttribute("Ventilation");
                                Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
                                Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
                                Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                                Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
                                Hashtable packagemap = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                                LinkedHashMap eqpmap = (LinkedHashMap) contexts.getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                                String carriercode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
                                Map dgshipper = (Map) contexts.getAttribute("SESSION_DGCLASS");
                                String hideMinMaxTemp = (String) contexts.getAttribute(GeneralConf.Hide_Min_MaxTemp);
                                String Enable_Drains_Humidity = (String) contexts.getAttribute(GeneralConf.Enable_Drains_Humidity);
                                String userFruitCust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
                                String disclaimerMsg = (String) session.getAttribute("NEWALLDISCLAIMERMSG");
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
                                masterCache.put("EnableActualCommoName", (String) contexts.getAttribute(GeneralConf.EnableActualCommoName));
                                masterCache.put("Enable_Drains_Humidity", Enable_Drains_Humidity);
                                masterCache.put("Enable_Eori_Details", (String) contexts.getAttribute(GeneralConf.GENS));
                                masterCache.put("enable_zipcode", (String) contexts.getAttribute(GeneralConf.Enable_Zip_Code));
                                masterCache.put(GeneralConf.Display_HS_NAME_CODE, (String) contexts.getAttribute(GeneralConf.Display_HS_NAME_CODE));
                                String imagepath = (String) contexts.getAttribute("Imagepath");
                                String logo_name = (String) contexts.getAttribute(GeneralConf.Logo_Name);
                                masterCache.put("IsEnableENSTabInBooking", iseuport);
                                masterCache.put("Ventilation", Ventilationlabel);
                                masterCache.put(GeneralConf.Hide_AP_CUST_BKG_Master, (String) contexts.getAttribute(GeneralConf.Hide_AP_CUST_BKG_Master));

                                details_mail = new CommonMail().newBookingMail(ve, imagepath, logo_name, returnVO.getStrRefNo(), masterCache, all);
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

                            message = new CommonMailForCHCust().newCHBookingMail(ve, masterCache, all, returnVO);

                        } //
                        else {
                            String agencyrefno = "", agencyrefnumber = "";
                            agencyrefno = Validation.checkNull(ve.getEbook_agency_reference());
                            if (agencyrefno.length() > 0 && "CN".equalsIgnoreCase(returnVO.getAgencycountry())) {
                                agencyrefnumber = "<br><br>Agency Reference Number:" + agencyrefno;
                            }
                            message = returnVO.getStrMailMsg();
                            message = message.replace("@date", formatDate);
                            
                            // Check if POL or POD is a mask port - if so, swap actual port names to mask port names
                            String polCode = Validation.checkNull(ve.getEbook_origin().toUpperCase());
                            String podCode = Validation.checkNull(ve.getEbook_delivery().toUpperCase());
                            String pol = Validation.checkNull(String.valueOf(portlist.get(polCode)));
                            String pod = Validation.checkNull(String.valueOf(portlist.get(podCode)));
                            boolean isMaskPort = MaskPortUtil.isActualPort(polCode) || MaskPortUtil.isActualPort(podCode);
                            if (isMaskPort) {
                                pol = MaskPortUtil.swapActualPortToMaskPortWithName(polCode + " - " + pol);
                                pod = MaskPortUtil.swapActualPortToMaskPortWithName(podCode + " - " + pod);
                                System.out.println("[ECOM-WAR] ebookingsaveNew Mask port detected - swapped pol: " + pol + ", pod: " + pod);
                            }
                            message = message.replace("@pol", pol);
                            message = message.replace("@pod", pod + agencyrefnumber);

                            System.out.println("[ECOM-WAR] ebookingsaveNew returnVO ActivityCode " + returnVO.getActivityCode() + " ebookbean StatusRemarks " + ebookbean.getEbook_status_remarks() + " returnVO AdmMailMsg " + returnVO.getAdmMailMsg());
                        }
                        if (sendMail) {
                            String ccMail = Validation.checkNull(ve.getCcMailId());
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
                            ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                        }
                        //agencysetAdmToMailId
                        System.out.println("[ECOM-WAR] ebookingsaveNew apname:::::::" + apname);
                        if (returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0) {
                            String message1 = "";
                            String filenames = returnVO.getFilenames();
                            if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_AGENCY))) {
                                message1 = details_mail;
                            } else {
                                Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                                Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");

                                HashMap masterCache = new HashMap();
                                masterCache.put("apname", apname);
                                masterCache.put("comlist", comlist);
                                masterCache.put("PORTLIST", portlist);
                                masterCache.put("VESSELLIST", vessellist);

                                message1 = new CommonMailForAgency().newAgencyBookingMail(ve, masterCache, all, returnVO);

//                                message1 = returnVO.getAdmMailMsg();
//                                String pol1 = String.valueOf(portlist.get(ve.getEbook_origin().toUpperCase()));
//                                pol1 = Validation.checkNull(pol1);
//                                String pod1 = String.valueOf(portlist.get(ve.getEbook_delivery().toUpperCase()));
//                                pod1 = Validation.checkNull(pod1);
//                                message1 = message1.replace("@apname", apname);
//                                message1 = message1.replace("@pol", pol1);
//                                message1 = message1.replace("@pod", pod1);
//                                message1 = message1.replace("@customer_remarks", Validation.checkNull(addremarks));
//                                message1 = message1.replace("@remarks", Validation.checkNull(addremarks));
//                                message1 = message1.replace("@salesagreed", Validation.checkNull(ve.getSalesRateAgree()));
//                                message1 = message1.replace("@vesselandvoyage",ve.getEbook_vesselcode()+ve.getEbook_voyage()+ve.getEbook_bound_leg()) ;
                            }
                            mail.setTo(returnVO.getAdmToMailId());
                            String subject = returnVO.getAdmSubject();
                            subject = subject.replace("@apname", apname);
                            mail.setSubject(subject);
                            mail.setMessage(message1);
                            //System.out.println("[ECOM-WAR] ebookingsaveNew >>message1>>" + message1);
                            mail.setAttachment(filenames);
                            mail.setCc(null);
                            mail.setAttachmentpath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH) + File.separator + returnVO.getStrRefNo() + File.separator);
                            try {
                                ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                        }
                    } else if ("D".equalsIgnoreCase(returnVO.getStrSaveType())) {
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

                        ArrayList view = new ArrayList();
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
                            request.setAttribute("updatetemplatemsg","Your template updated successfully.");
                        } else {
                            session.setAttribute("SAVETEMPLATE", "save");
                            request.setAttribute("updatetemplatemsg", "Your template saved successfully.");
                        }

                        SUCCESS = "showrecords";
                    } else {
                        SUCCESS = "success";
                    }
                } else if (returnVO.getStrRefNo().length() > 0 && returnVO.getStrRefNo().equalsIgnoreCase("Error")) {
                    SUCCESS = "success";
                }
                request.setAttribute("SaveResult", returnVO);
            } else {
                SUCCESS = "success";
                System.out.println("[ECOM-WAR] ebookingsaveNew Duplicate Submission of the form" + SUCCESS);
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] ebookingsaveNew Exception in ebookingsaveNew " + e.getMessage());
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
