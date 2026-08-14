package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.Validation;
import net.ecom.formbeans.SIEditForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISubmitVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.common.CommonMail;
import net.ecom.common.CommonMailForAgency;
import net.ecom.common.CommonMailForCHCust;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.NumberComparator;
import net.ecom.common.SIValidation;
import net.ecom.vo.CityVo;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SISealVO;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.FileExtensionValidation;
import net.ecom.common.utils.InsuranceUtils;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.ChargesVo;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.FileDetailsVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.upload.FormFile;
import net.ecom.common.utils.MaskPortUtil;

public class SubmitSI extends org.apache.struts.action.Action {

    EcomShippingInstructionBeanRemote sibeanobj = EJBLookUp.getSIBeanRemoteLookUp();
    EComCntrlBeanRemote cntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "success";
        FileOutputStream fos = null;
        String file_name1 = null, file_name2 = null, file_name3 = null, file_name4 = null, file_name5 = null;
        String apname = null;
        FileDetailsVO fileDetailsVo = new FileDetailsVO();
        try {
            System.out.println("[ECOM-WAR] SubmitSI   >>>encoding>>>" + request.getCharacterEncoding());
            HttpSession session = request.getSession();
            apname = (String) session.getAttribute("apname");
            String pagereq = (String) session.getAttribute("SISEND");
            ServletContext contexts = servlet.getServletContext();
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            String custCountry = "";
            if (!"Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.Disable_Lang_Mail))) {
                custCountry = Validation.checkNull((String) session.getAttribute("SESSION_USER_COUNTRY"));
            }
            String custCompany = Validation.checkNull((String) session.getAttribute("SESSION_USER_CONTACT"));
            session.setAttribute("ENABLE_SI_AMENDMENT_MAIL", "N");
            if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.Enable_Amendment_Comparison_previousRecord_Mail))) {
                session.setAttribute("ENABLE_SI_AMENDMENT_MAIL", "Y");
            }
            String agencymail = (String) contexts.getAttribute("clientfrommail");
            List EBK_pluginList = (List) contexts.getAttribute("EBK_PLUGIN_DETAILS");
            boolean is_SI_restriction = false;
            if (EBK_pluginList != null && EBK_pluginList.contains("ESP")) {
                is_SI_restriction = true;
            }
            String addccMailid = null;
            String TENS_Documentation = (String) contexts.getAttribute(GeneralConf.TENS_Documentation);
            if ("SISend".equalsIgnoreCase(pagereq)) {
                session.setAttribute("SISEND", "SISaved");
                ActionErrors errors = new ActionErrors();
                SIValidation siValidate = new SIValidation();
                //carrier code
                String client_code = (String) contexts.getAttribute("client_code");
                //
                System.out.println("[ECOM-WAR] SubmitSI   token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN"));
                System.out.println("[ECOM-WAR] SubmitSI   token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN"));
                String sessionToken = (String) session.getAttribute("org.apache.struts.action.TOKEN");
                if (isTokenValid(request)) {
                    String fakCommodity = (String) contexts.getAttribute(GeneralConf.FAKCOMMODITY);
                    String fakComdtyCode = "";
                    String fakComdtyName = "";
                    System.out.println("[ECOM-WAR] SubmitSI   fakCommodity>>>>" + fakCommodity);
                    fakCommodity = fakCommodity != null ? fakCommodity : "";
                    resetToken(request);
                    if (fakCommodity != null && fakCommodity.length() > 0) {
                        String fakCommodityArr[] = fakCommodity.split("~~");
                        if (fakCommodityArr.length == 2) {
                            fakComdtyCode = fakCommodityArr[0].trim();
                            fakComdtyName = fakCommodityArr[1].trim();
                        }
                    }
                    SIEditForm siOBJ = (SIEditForm) form;
                    FormFile file1 = siOBJ.getFile_path1();
                    FormFile file2 = siOBJ.getFile_path2();
                    FormFile file3 = siOBJ.getFile_path3();
                    FormFile file4 = siOBJ.getFile_path4();
                    FormFile file5 = siOBJ.getFile_path5();

                    FormFile file6 = siOBJ.getFile_path6();
                    FormFile file7 = siOBJ.getFile_path7();
                    FormFile file8 = siOBJ.getFile_path8();
                    FormFile file9 = siOBJ.getFile_path9();
                    FormFile file10 = siOBJ.getFile_path10();
                    FormFile file11 = siOBJ.getFile_path11();
                    FormFile file12 = siOBJ.getFile_path12();
                    FormFile file13 = siOBJ.getFile_path13();
                    FormFile file14 = siOBJ.getFile_path14();
                    FormFile file15 = siOBJ.getFile_path15();
                    Hashtable countryHashObj = (Hashtable) contexts.getAttribute("SESSION_COUNTRYOBJECT");
                    Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
                    HashMap<String, ArrayList<CityVo>> countryCityObj = (HashMap<String, ArrayList<CityVo>>) contexts.getAttribute("CONTEXT_COUNTRYCITY");
                    String strCity = "", strCountryCode = "";
                    String custcode = (String) session.getAttribute("SESSION_USERCODE");
                    HashMap hm = new HashMap();
                    ArrayList countryCityList = new ArrayList();
                    int icountryCitySize = 0;
                    String strCityName = "";
                    String bl_no = (String) session.getAttribute("edit_blno");
                    SISubmitVO submitSHVO = new SISubmitVO();

                    String savetype = Validation.checkNull(siOBJ.getSavetype());
                    String spname = Validation.checkNull(siOBJ.getSpname());
                    String spCustCode = Validation.checkNull(siOBJ.getSpCustCode());
                    String spaddr = Validation.checkNull(siOBJ.getSpaddr());
                    String spaddr1 = Validation.checkNull(siOBJ.getSpaddr1());
                    String spcity = Validation.checkNull(siOBJ.getSpcity());
                    String spphone = Validation.checkNull(siOBJ.getSpphone());
                    String spfax = Validation.checkNull(siOBJ.getSpfax());
                    String spcountry = Validation.checkNull(siOBJ.getSpcountry());
                    String spEmail = Validation.checkNull(siOBJ.getSpemail());
                    String speori = Validation.checkNull(siOBJ.getSpeori());
                    String sppersontype = Validation.checkNull(siOBJ.getSppersontype());
                    String spstate = Validation.checkNull(siOBJ.getSpstate());
                    String sino = Validation.checkNull(siOBJ.getSino());
                    String taxId = Validation.checkNull(siOBJ.getSptaxId());
                    String spzipcode = Validation.checkNull(siOBJ.getSpzipcode());
                    //sino = sino.replace(GeneralConf.SI_PREFIX, "");
                    strCity = "";
                    strCountryCode = "";
                    spcity = (spcity.length() > 0) ? spcity.toUpperCase() : "";
                    spcountry = (spcountry.length() > 0) ? spcountry.toUpperCase() : "";
                    countryCityList = new ArrayList();
                    countryCityList = countryCityObj.get(spcountry);
                    icountryCitySize = 0;
                    strCityName = "";
                    if (countryCityList != null && countryCityList.size() > 0) {
                        icountryCitySize = countryCityList.size();
                        for (int cc = 0; cc < icountryCitySize; cc++) {
                            CityVo cityObj = (CityVo) countryCityList.get(cc);
                            strCityName = cityObj.getStrCityName();
                            if (strCityName.contains(spcity)) {
                                strCity = strCityName;
                            }
                        }
                    }
                    spstate = Validation.checkNull((spstate != null ? (spstate.split("-")[0]) : spstate));

                    strCountryCode = Validation.checkNull((String) countryHashObj.get(spcountry));
                    siOBJ.setSpIsCountry(strCountryCode);
                    siOBJ.setSpIsCity(strCity);
                    submitSHVO.setCustCode(spCustCode);
                    submitSHVO.setCustname(spname);
                    submitSHVO.setAddress(spaddr);
                    submitSHVO.setAddress1(spaddr1);
                    submitSHVO.setCity(spcity);
                    submitSHVO.setTelephone(spphone);
                    submitSHVO.setFax(spfax);
                    submitSHVO.setCountryid(spcountry);
                    submitSHVO.setEmail(spEmail);
                    submitSHVO.setNotifyCheck(null);
                    submitSHVO.setOrdercheck(null);
                    submitSHVO.setState(spstate);
                    submitSHVO.setEori(speori);
                    submitSHVO.setPersontype(sppersontype);
                    submitSHVO.setZipCode(spzipcode);
                    hm.put("SH", submitSHVO);

                    SISubmitVO submitCNVO = new SISubmitVO();
                    String consname = Validation.checkNull(siOBJ.getConsname());
                    String consCustCode = Validation.checkNull(siOBJ.getConsCustCode());
                    String consaddr = Validation.checkNull(siOBJ.getConsaddr());
                    String consaddr1 = Validation.checkNull(siOBJ.getConsaddr1());
                    String conscity = Validation.checkNull(siOBJ.getConscity());
                    String consphone = Validation.checkNull(siOBJ.getConsphone());
                    String consfax = Validation.checkNull(siOBJ.getConsfax());
                    String conscountry = Validation.checkNull(siOBJ.getConscountry());
                    String consEmail = Validation.checkNull(siOBJ.getConsemail());
                    String consstate = Validation.checkNull(siOBJ.getConsstate());
                    String concheckorder = Validation.checkNull(siOBJ.getConsigneeordercheck());
                    String conszipcode = Validation.checkNull(siOBJ.getConszipcode());
                    if ("Y".equalsIgnoreCase(concheckorder)) {
                        if (consname == null || consname.length() == 0) {
                            consname = Validation.checkNull(siOBJ.getConsnameheader());
                        }
                        concheckorder = "Y";
                    } else {
                        concheckorder = "N";
                    }
                    System.out.println("[ECOM-WAR] SubmitSI   Welcome conscheck::::::" + concheckorder);
                    strCity = "";
                    strCountryCode = "";

                    conscity = (conscity.length() > 0) ? conscity.toUpperCase() : "";
                    conscountry = (conscountry.length() > 0) ? conscountry.toUpperCase() : "";
                    strCountryCode = Validation.checkNull((String) countryHashObj.get(conscountry));

                    countryCityList = new ArrayList();
                    countryCityList = countryCityObj.get(conscountry);
                    icountryCitySize = 0;
                    strCityName = "";
                    if (countryCityList != null && countryCityList.size() > 0) {
                        icountryCitySize = countryCityList.size();
                        for (int cc = 0; cc < icountryCitySize; cc++) {
                            CityVo cityObj = (CityVo) countryCityList.get(cc);
                            strCityName = cityObj.getStrCityName();
                            if (strCityName.contains(conscity)) {
                                strCity = strCityName;
                            }
                        }
                    }
                    consstate = Validation.checkNull((consstate != null ? (consstate.split("-")[0]) : consstate));
                    siOBJ.setConsIsCountry(strCountryCode);
                    siOBJ.setConsIsCity(strCity);
                    submitCNVO.setCustname(consname);
                    submitCNVO.setCustCode(consCustCode);
                    submitCNVO.setAddress(consaddr);
                    submitCNVO.setAddress1(consaddr1);
                    submitCNVO.setCity(conscity);
                    submitCNVO.setTelephone(consphone);
                    submitCNVO.setFax(consfax);
                    submitCNVO.setCountryid(conscountry);
                    submitCNVO.setEmail(consEmail);
                    submitCNVO.setNotifyCheck(null);
                    submitCNVO.setOrdercheck(concheckorder);
                    submitCNVO.setState(consstate);
                    submitCNVO.setEori(Validation.checkNull(siOBJ.getConseori()));
                    submitCNVO.setPersontype(Validation.checkNull(siOBJ.getConspersontype()));
                    submitCNVO.setZipCode(conszipcode);
                    hm.put("CN", submitCNVO);

                    SISubmitVO submitNPVO = new SISubmitVO();
                    String npname = Validation.checkNull(siOBJ.getNpname());
                    System.out.println("[ECOM-WAR] SubmitSI   npname::::::::" + npname);
                    String npCustCode = Validation.checkNull(siOBJ.getNpCustCode());
                    String npaddr = Validation.checkNull(siOBJ.getNpaddr());
                    String npaddr1 = Validation.checkNull(siOBJ.getNpaddr1());
                    String npcity = Validation.checkNull(siOBJ.getNpcity());
                    String npphone = Validation.checkNull(siOBJ.getNpphone());
                    String npfax = Validation.checkNull(siOBJ.getNpfax());
                    String npEmail = Validation.checkNull(siOBJ.getNpemail());
                    String npcountry = Validation.checkNull(siOBJ.getNpcountry());
                    String notifyCheck = Validation.checkNull(siOBJ.getNotifyCheck());
                    String npState = Validation.checkNull(siOBJ.getNpstate());
                    String npzipcode = Validation.checkNull(siOBJ.getNpzipcode());
                    if ("Y".equalsIgnoreCase(notifyCheck)) {
                        notifyCheck = "Y";
                    } else {
                        notifyCheck = "N";
                    }
                    String notifycheckorder = Validation.checkNull(siOBJ.getNotifyordercheck());
                    if ("Y".equalsIgnoreCase(notifycheckorder)) {
                        notifycheckorder = "Y";
                    } else {
                        notifycheckorder = "N";
                    }
                    strCity = "";
                    strCountryCode = "";
                    //      System.out.println("[ECOM-WAR] SubmitSI   Welcome notifycheckorder::::::"+notifycheckorder);

                    npcity = (npcity.length() > 0) ? npcity.toUpperCase() : "";
                    npcountry = (npcountry.length() > 0) ? npcountry.toUpperCase() : "";
                    strCountryCode = Validation.checkNull((String) countryHashObj.get(npcountry));

                    countryCityList = new ArrayList();
                    countryCityList = countryCityObj.get(npcountry);
                    icountryCitySize = 0;
                    strCityName = "";

                    if (countryCityList != null && countryCityList.size() > 0) {
                        icountryCitySize = countryCityList.size();
                        for (int cc = 0; cc < icountryCitySize; cc++) {
                            CityVo cityObj = (CityVo) countryCityList.get(cc);
                            strCityName = cityObj.getStrCityName();

                            if (strCityName.contains(npcity)) {
                                strCity = strCityName;
                            }
                        }
                    }
                    npState = Validation.checkNull((npState != null ? (npState.split("-")[0]) : npState));
                    siOBJ.setNpIsCountry(strCountryCode);
                    siOBJ.setNpIsCity(strCity);
                    submitNPVO.setCustname(npname);
                    submitNPVO.setCustCode(npCustCode);
                    submitNPVO.setAddress(npaddr);
                    submitNPVO.setAddress1(npaddr1);
                    submitNPVO.setCity(npcity);
                    submitNPVO.setTelephone(npphone);
                    submitNPVO.setFax(npfax);
                    submitNPVO.setCountryid(npcountry);
                    submitNPVO.setEmail(npEmail);
                    submitNPVO.setNotifyCheck(notifyCheck);
                    submitNPVO.setOrdercheck(notifycheckorder);
                    submitNPVO.setState(npState);
                    submitNPVO.setEori(Validation.checkNull(siOBJ.getNpeori()));
                    submitNPVO.setPersontype(Validation.checkNull(siOBJ.getNppersontype()));
                    submitNPVO.setZipCode(npzipcode);
                    hm.put("NT", submitNPVO);  // NT

                    SISubmitVO submitNP2VO = new SISubmitVO();
                    String npname2 = Validation.checkNull(siOBJ.getNp2name());

                    String npCustCode2 = Validation.checkNull(siOBJ.getNp2CustCode());
                    String np2addr = Validation.checkNull(siOBJ.getNp2addr());
                    String np2addr1 = Validation.checkNull(siOBJ.getNp2addr1());
                    System.out.println("[ECOM-WAR] SubmitSI   npaddr2::::::::" + np2addr + "::::" + np2addr1);
                    String npcity2 = Validation.checkNull(siOBJ.getNp2city());
                    String npphone2 = Validation.checkNull(siOBJ.getNp2phone());
                    String npfax2 = Validation.checkNull(siOBJ.getNp2fax());
                    String npEmail2 = Validation.checkNull(siOBJ.getNp2email());
                    String npcountry2 = Validation.checkNull(siOBJ.getNp2country());
                    String notifyCheck2 = Validation.checkNull(siOBJ.getNotify2Check());
                    String npState2 = Validation.checkNull(siOBJ.getNp2state());
                    String np2zipcode = Validation.checkNull(siOBJ.getNp2zipcode());
                    if ("Y".equalsIgnoreCase(notifyCheck2)) {
                        notifyCheck2 = "Y";
                    } else {
                        notifyCheck2 = "N";
                    }
                    String notifycheckorder2 = Validation.checkNull(siOBJ.getNotify2ordercheck());
                    if ("Y".equalsIgnoreCase(notifycheckorder2)) {
                        notifycheckorder2 = "Y";
                    } else {
                        notifycheckorder2 = "N";
                    }
                    strCity = "";
                    strCountryCode = "";
                    //      System.out.println("[ECOM-WAR] SubmitSI   Welcome notifycheckorder::::::"+notifycheckorder);

                    npcity2 = (npcity2.length() > 0) ? npcity2.toUpperCase() : "";
                    npcountry2 = (npcountry2.length() > 0) ? npcountry2.toUpperCase() : "";
                    strCountryCode = Validation.checkNull((String) countryHashObj.get(npcountry2));

                    countryCityList = new ArrayList();
                    countryCityList = countryCityObj.get(npcountry2);
                    icountryCitySize = 0;
                    strCityName = "";
                    npState2 = Validation.checkNull((npState2 != null ? (npState2.split("-")[0]) : npState2));

                    submitNP2VO.setCustname(npname2);
                    submitNP2VO.setCustCode(npCustCode2);
                    submitNP2VO.setAddress(np2addr);
                    submitNP2VO.setAddress1(np2addr1);
                    submitNP2VO.setCity(npcity2);
                    submitNP2VO.setTelephone(npphone2);
                    submitNP2VO.setFax(npfax2);
                    submitNP2VO.setCountryid(npcountry2);
                    submitNP2VO.setEmail(npEmail2);
                    submitNP2VO.setNotifyCheck(notifyCheck2);
                    submitNP2VO.setOrdercheck(notifycheckorder2);
                    submitNP2VO.setState(npState2);
                    submitNP2VO.setEori(Validation.checkNull(siOBJ.getNp2eori()));
                    submitNP2VO.setPersontype(Validation.checkNull(siOBJ.getNp2persontype()));
                    submitNP2VO.setZipCode(np2zipcode);
                    hm.put("N1", submitNP2VO);  // NT2sicontainerid

                    //notify 3
                    SISubmitVO submitNP3VO = new SISubmitVO();
                    String npname3 = Validation.checkNull(siOBJ.getNp3name());
                    System.out.println("[ECOM-WAR] SubmitSI   npname::::::::" + npname3);
                    String npCustCode3 = Validation.checkNull(siOBJ.getNp3CustCode());
                    String np3addr = Validation.checkNull(siOBJ.getNp3addr());
                    String np3addr1 = Validation.checkNull(siOBJ.getNp3addr1());
                    System.out.println("[ECOM-WAR] SubmitSI   npaddr3::::::::" + np3addr + "::::" + np3addr1);
                    String npcity3 = Validation.checkNull(siOBJ.getNp3city());
                    String npphone3 = Validation.checkNull(siOBJ.getNp3phone());
                    String npfax3 = Validation.checkNull(siOBJ.getNp3fax());
                    String npEmail3 = Validation.checkNull(siOBJ.getNp3email());
                    String npcountry3 = Validation.checkNull(siOBJ.getNp3country());
                    String notifyCheck3 = Validation.checkNull(siOBJ.getNotify3Check());
                    String npState3 = Validation.checkNull(siOBJ.getNp3state());
                    String np3zipcode = Validation.checkNull(siOBJ.getNp3zipcode());
                    if ("Y".equalsIgnoreCase(notifyCheck3)) {
                        notifyCheck3 = "Y";
                    } else {
                        notifyCheck3 = "N";
                    }
                    String notifycheckorder3 = Validation.checkNull(siOBJ.getNotify3ordercheck());
                    if ("Y".equalsIgnoreCase(notifycheckorder3)) {
                        notifycheckorder3 = "Y";
                    } else {
                        notifycheckorder3 = "N";
                    }
                    strCity = "";
                    strCountryCode = "";
                    //      System.out.println("[ECOM-WAR] SubmitSI   Welcome notifycheckorder::::::"+notifycheckorder);

                    npcity = (npcity3.length() > 0) ? npcity3.toUpperCase() : "";
                    npcountry = (npcountry3.length() > 0) ? npcountry3.toUpperCase() : "";
                    strCountryCode = Validation.checkNull((String) countryHashObj.get(npcountry3));

                    npState3 = Validation.checkNull((npState3 != null ? (npState3.split("-")[0]) : npState3));

                    siOBJ.setNpIsCountry(strCountryCode);
                    siOBJ.setNpIsCity(strCity);
                    submitNP3VO.setCustname(npname3);
                    submitNP3VO.setCustCode(npCustCode3);
                    submitNP3VO.setAddress(np3addr);
                    submitNP3VO.setAddress1(np3addr1);
                    submitNP3VO.setCity(npcity3);
                    submitNP3VO.setTelephone(npphone3);
                    submitNP3VO.setFax(npfax3);
                    submitNP3VO.setCountryid(npcountry3);
                    submitNP3VO.setEmail(npEmail3);
                    submitNP3VO.setNotifyCheck(notifyCheck3);
                    submitNP3VO.setOrdercheck(notifycheckorder3);
                    submitNP3VO.setState(npState3);
                    submitNP3VO.setEori(Validation.checkNull(siOBJ.getNp3eori()));
                    submitNP3VO.setPersontype(Validation.checkNull(siOBJ.getNp3persontype()));
                    submitNP3VO.setZipCode(np3zipcode);
                    hm.put("N2", submitNP3VO);    // NT3

                    //FW
                    SISubmitVO submitFWVO = new SISubmitVO();

                    String fwname = Validation.checkNull(siOBJ.getFwname());
                    String fwCustCode = Validation.checkNull(siOBJ.getFwCustCode());
                    String fwaddr = Validation.checkNull(siOBJ.getFwaddr());
                    String fwaddr1 = Validation.checkNull(siOBJ.getFwaddr1());
                    String fwcity = Validation.checkNull(siOBJ.getFwcity());
                    String fwphone = Validation.checkNull(siOBJ.getFwphone());
                    String fwfax = Validation.checkNull(siOBJ.getFwfax());
                    String fwcountry = Validation.checkNull(siOBJ.getFwcountry());
                    String fwEmail = Validation.checkNull(siOBJ.getFwemail());
                    String fwstate = Validation.checkNull(siOBJ.getFwstate());
                    fwcity = (fwcity.length() > 0) ? fwcity.toUpperCase() : "";
                    fwcountry = (fwcountry.length() > 0) ? fwcountry.toUpperCase() : "";
                    strCountryCode = Validation.checkNull((String) countryHashObj.get(fwcountry));

                    countryCityList = new ArrayList();
                    countryCityList = countryCityObj.get(fwcountry);
                    icountryCitySize = 0;
                    strCityName = "";
                    fwstate = Validation.checkNull((fwstate != null ? (fwstate.split("-")[0]) : fwstate));

                    submitFWVO.setCustname(fwname);
                    submitFWVO.setCustCode(fwCustCode);
                    submitFWVO.setAddress(fwaddr);
                    submitFWVO.setAddress1(fwaddr1);
                    submitFWVO.setCity(fwcity);
                    submitFWVO.setTelephone(fwphone);
                    submitFWVO.setFax(fwfax);
                    submitFWVO.setCountryid(fwcountry);
                    submitFWVO.setEmail(fwEmail);
                    submitFWVO.setState(fwstate);
                    submitFWVO.setEori(Validation.checkNull(siOBJ.getFweori()));
                    submitFWVO.setPersontype(Validation.checkNull(siOBJ.getFwpersontype()));
                    submitFWVO.setZipCode(Validation.checkNull(siOBJ.getFwzipcode()));
                    hm.put("FW", submitFWVO);

                    SISubmitVO submitBYVO = new SISubmitVO();
                    submitBYVO.setCustname(Validation.checkNull(siOBJ.getByname()));
                    submitBYVO.setCustCode(Validation.checkNull(siOBJ.getByCustCode()));
                    submitBYVO.setAddress(Validation.checkNull(siOBJ.getByaddr()));
                    submitBYVO.setAddress1(Validation.checkNull(siOBJ.getByaddr1()));
                    submitBYVO.setCity(Validation.checkNull(siOBJ.getBycity()));
                    submitBYVO.setTelephone(Validation.checkNull(siOBJ.getByphone()));
                    submitBYVO.setFax(Validation.checkNull(siOBJ.getByfax()));
                    submitBYVO.setCountryid(Validation.checkNull(siOBJ.getBycountry()));
                    submitBYVO.setEmail(Validation.checkNull(siOBJ.getByemail()));
                    submitBYVO.setState(Validation.checkNull(siOBJ.getBystate()).split("-")[0].trim());
                    submitBYVO.setEori(Validation.checkNull(siOBJ.getByeori()));
                    submitBYVO.setPersontype(Validation.checkNull(siOBJ.getBypersontype()));
                    submitBYVO.setZipCode(Validation.checkNull(siOBJ.getByzipcode()));
                    hm.put("BY", submitBYVO);

                    SISubmitVO submitSLVO = new SISubmitVO();
                    submitSLVO.setCustname(Validation.checkNull(siOBJ.getSlname()));
                    submitSLVO.setCustCode(Validation.checkNull(siOBJ.getSlCustCode()));
                    submitSLVO.setAddress(Validation.checkNull(siOBJ.getSladdr()));
                    submitSLVO.setAddress1(Validation.checkNull(siOBJ.getSladdr1()));
                    submitSLVO.setCity(Validation.checkNull(siOBJ.getSlcity()));
                    submitSLVO.setTelephone(Validation.checkNull(siOBJ.getSlphone()));
                    submitSLVO.setFax(Validation.checkNull(siOBJ.getSlfax()));
                    submitSLVO.setCountryid(Validation.checkNull(siOBJ.getSlcountry()));
                    submitSLVO.setEmail(Validation.checkNull(siOBJ.getSlemail()));
                    submitSLVO.setState(Validation.checkNull(siOBJ.getSlstate()).split("-")[0].trim());
                    submitSLVO.setEori(Validation.checkNull(siOBJ.getSleori()));
                    submitSLVO.setPersontype(Validation.checkNull(siOBJ.getSlpersontype()));
                    submitSLVO.setZipCode(Validation.checkNull(siOBJ.getSlzipcode()));
                    hm.put("SL", submitSLVO);
                    //Seller
                    Map customerMap = null;
                    List<SISubmitVO> buyerList = null;
                    List<SISubmitVO> sellerList = null;

                    String[] buyerCustindex = request.getParameterValues("BuyercountRowID");
                    System.out.println("buyerCustindex==>" + buyerCustindex);
                    if (buyerCustindex != null) {
                        buyerList = new ArrayList<>();
                        for (String rowID : buyerCustindex) {
                            String custType = "Buyer";
                            String prefix = custType;
                            String name = Validation.checkNull(request.getParameter("ebook_" + prefix + "name" + rowID));
                            System.out.println("name" + name + "//" + rowID);
                            if (name.length() > 0) {

                                String persontype = Validation.checkNull(request.getParameter("ebook_" + prefix + "persontype" + rowID));
                                String eori = Validation.checkNull(request.getParameter("ebook_" + prefix + "eori" + rowID));
                                String stateRaw = Validation.checkNull(request.getParameter("ebook_" + prefix + "state" + rowID));
                                String state = stateRaw.contains("-") ? stateRaw.split("-")[0].trim() : stateRaw;
                                String email = Validation.checkNull(request.getParameter("ebook_" + prefix + "email" + rowID));
                                String fax = Validation.checkNull(request.getParameter("ebook_" + prefix + "fax" + rowID));
                                String phone = Validation.checkNull(request.getParameter("ebook_" + prefix + "phone" + rowID));
                                String country = Validation.checkNull(request.getParameter("ebook_" + prefix + "country" + rowID));
                                String city = Validation.checkNull(request.getParameter("ebook_" + prefix + "city" + rowID));
                                String address1 = Validation.checkNull(request.getParameter("ebook_" + prefix + "address1" + rowID));
                                String address = Validation.checkNull(request.getParameter("ebook_" + prefix + "address" + rowID));
                                String code = Validation.checkNull(request.getParameter("ebook_" + prefix + "CustCode" + rowID));

                                SISubmitVO vo = new SISubmitVO();
                                vo.setPersontype(persontype);
                                vo.setEori(eori);
                                vo.setState(state);
                                vo.setEmail(email);
                                vo.setFax(fax);
                                vo.setTelephone(phone);
                                vo.setCountryid(country);
                                vo.setCity(city);
                                vo.setAddress1(address1);
                                vo.setAddress(address);
                                vo.setCustname(name);
                                vo.setCustCode(code);
                                buyerList.add(vo);
                            }
                        }

                    }

// ===== Seller Processing =====
                    String[] sellerCustindex = request.getParameterValues("SellercountRowID");
                    System.out.println("sellerCustindex==>" + sellerCustindex);
                    if (sellerCustindex != null) {
                        sellerList = new ArrayList<>();
                        for (String rowID : sellerCustindex) {
                            String custType = "Seller";
                            String prefix = custType;
                            String name = Validation.checkNull(request.getParameter("ebook_" + prefix + "name" + rowID));
                            if (name.length() > 0) {
                                String persontype = Validation.checkNull(request.getParameter("ebook_" + prefix + "persontype" + rowID));
                                String eori = Validation.checkNull(request.getParameter("ebook_" + prefix + "eori" + rowID));
                                String stateRaw = Validation.checkNull(request.getParameter("ebook_" + prefix + "state" + rowID));
                                String state = stateRaw.contains("-") ? stateRaw.split("-")[0].trim() : stateRaw;
                                String email = Validation.checkNull(request.getParameter("ebook_" + prefix + "email" + rowID));
                                String fax = Validation.checkNull(request.getParameter("ebook_" + prefix + "fax" + rowID));
                                String phone = Validation.checkNull(request.getParameter("ebook_" + prefix + "phone" + rowID));
                                String country = Validation.checkNull(request.getParameter("ebook_" + prefix + "country" + rowID));
                                String city = Validation.checkNull(request.getParameter("ebook_" + prefix + "city" + rowID));
                                String address1 = Validation.checkNull(request.getParameter("ebook_" + prefix + "address1" + rowID));
                                String address = Validation.checkNull(request.getParameter("ebook_" + prefix + "address" + rowID));

                                String code = Validation.checkNull(request.getParameter("ebook_" + prefix + "CustCode" + rowID));

                                SISubmitVO vo = new SISubmitVO();
                                vo.setPersontype(persontype);
                                vo.setEori(eori);
                                vo.setState(state);
                                vo.setEmail(email);
                                vo.setFax(fax);
                                vo.setTelephone(phone);
                                vo.setCountryid(country);
                                vo.setCity(city);
                                vo.setAddress1(address1);
                                vo.setAddress(address);
                                vo.setCustname(name);
                                vo.setCustCode(code);
                                sellerList.add(vo);
                            }
                        }

                    }

                    if (sellerList != null && !sellerList.isEmpty() || buyerList != null && !buyerList.isEmpty()) {
                        customerMap = new HashMap<>();
                        customerMap.put("Seller", sellerList);
                        customerMap.put("Buyer", buyerList);
                    }
                    System.out.println("customerMap==>" + customerMap + sellerList + "//" + buyerList);

                    int detailLen = 0;
                    Map<SIMasterVO, SIConatainerValueVO> submitteSICntInfoHashObj = new LinkedHashMap<SIMasterVO, SIConatainerValueVO>();
                    String select[] = request.getParameterValues("select");
                    SIMasterVO submittedMasterVo = new SIMasterVO();
                    ArrayList<SIContainerDetailVO> submittedAListObj = new ArrayList<SIContainerDetailVO>();
                    SIContainerDetailVO submittedCntDetails = new SIContainerDetailVO();
                    String containerNo = "", carrierSeal = "", shipperSeal = "", strEqptype = "", cntOrder = "", strType = "";
                    String cmdName = "", wtName = "", ntName = "", qtyName = "", unitName = "", cidName = "", cargoIdName = "", cargoId = "", bycode = "", byname = "", slcode = "", slname = "", cmdty_hblno = "";
                    String pkgName = "", volName = "", vutName = "", mrkName = "", desName = "", eqpStatusName = null;
                    String contno = "", containerid = "", soc = "", socid = "", norname = "", hscodename = null, hscodesplit = null, hssuffixcode = null;
                    String hsArr[] = null;
                    String cargononame = "", tareweightname = "", cargoweightname = "", grossweightname = "", weightunitname = null;
                    String tempminname = "", settempname = "", oogname = "", refername = "", tempmaxname = "", tempunitname = "";
                    String imocontactname = "", imophonename = "", itemforwardname = "", itemtopname = "";
                    String itemrightname = "", itemaftname = "", hazardousname = "", itemlengthUnitname = "", itemleftname = "";
                    double cargoWt = 0, grossWt = 0, tarWt = 0, netWt = 0;
                    String strCargoWt = null, strTareWt = null, dgFlag = "", dgFlagName = "", strNetWt = "";
                    String humidityId = null, humidityFlagId = null, drainsId = null, co2Id = null, o2Id = null, strrateeqpid = null;
                    String ventilationId = null;

                    SIConatainerValueVO siCNTValueVO = new SIConatainerValueVO();
                    SISealVO siSealVOObj = new SISealVO();
                    if (select != null && select.length > 0) {
                        detailLen = 0;
                        int selectLen = select.length;
                        for (int i = 0; i < selectLen; i++) {
                            submittedMasterVo = new SIMasterVO();
                            siCNTValueVO = new SIConatainerValueVO();
                            siSealVOObj = new SISealVO();
                            submittedAListObj = new ArrayList<SIContainerDetailVO>();
                            containerNo = "";
                            carrierSeal = "";
                            shipperSeal = "";
                            containerNo = Validation.checkNull(select[i]);
                            //   System.out.println("[ECOM-WAR] SubmitSI   Welcome container number:::::::::" + containerNo);
                            contno = containerNo + "#containerID#";
                            shipperSeal = containerNo + "#shipper#";
                            carrierSeal = containerNo + "#carrier#";
                            strEqptype = "";
                            strEqptype = containerNo + "#eqpType#";
                            cntOrder = "";
                            cntOrder = containerNo + "#CNTORDER#";
                            cargoIdName = "";
                            cargoIdName = containerNo + "#cargoID#";
                            strType = "";
                            strType = containerNo + "#typeID#";
                            soc = "";
                            soc = containerNo + "#socID#";
                            strrateeqpid = containerNo + "rateeqptypeid";
//  Draft Hidden cargo values
                            cargononame = containerNo + "#cargono#";
                            tareweightname = containerNo + "#tareweight#";
                            cargoweightname = containerNo + "#cargoweight#";
                            grossweightname = containerNo + "#grossweight#";
                            weightunitname = containerNo + "#unit#";
                            tempmaxname = containerNo + "#tempmax#";
                            tempminname = containerNo + "#tempmin#";
                            settempname = containerNo + "#settemp#";
                            eqpStatusName = containerNo + "#eqpstatus#";
                            dgFlagName = containerNo + "#dgflagName#";
                            oogname = containerNo + "#oog#";
                            norname = containerNo + "#nor#";
                            refername = containerNo + "#refer#";
                            tempunitname = containerNo + "#tempunit#";
//
                            //              Draft Hidden containervalues values
                            imocontactname = containerNo + "#imocontact#";
                            imophonename = containerNo + "#imophone#";
                            itemforwardname = containerNo + "#itemforward#";
                            itemtopname = containerNo + "#itemtop#";
                            itemrightname = containerNo + "#itemright#";
                            itemaftname = containerNo + "#itemaft#";
                            hazardousname = containerNo + "#hazardous#";
                            itemlengthUnitname = containerNo + "#itemlengthUnit#";
                            itemleftname = containerNo + "#itemleft#";

                            humidityId = containerNo + "#humidity#";
                            humidityFlagId = containerNo + "#humidityflag#";
                            drainsId = containerNo + "#drains#";
                            co2Id = containerNo + "#co2#";
                            o2Id = containerNo + "#o2#";
                            ventilationId = containerNo + "#ventilation#";
//
                            System.out.println("[ECOM-WAR] SubmitSI   >>containerNo>>" + containerNo);
                            carrierSeal = Validation.checkNull(request.getParameter(carrierSeal));
                            shipperSeal = Validation.checkNull(request.getParameter(shipperSeal));
                            strEqptype = Validation.checkNull(request.getParameter(strEqptype));
                            cntOrder = Validation.checkNull(request.getParameter(cntOrder));
                            cntOrder = (cntOrder.length() == 0) ? "0" : cntOrder;
                            cargoId = Validation.checkNull(request.getParameter(cargoIdName));
                            strType = Validation.checkNull(request.getParameter(strType));
                            containerid = Validation.checkNull(request.getParameter(contno));
                            socid = Validation.checkNull(request.getParameter(soc));
                            submittedMasterVo.setStrSelect(Integer.parseInt(cntOrder));
                            submittedMasterVo.setStrCntNo(containerid);
                            submittedMasterVo.setStrType(strEqptype);
                            submittedMasterVo.setStrEqpType(strType);
                            submittedMasterVo.setRateeqptypeid(request.getParameter(strrateeqpid));
                            System.out.println("[ECOM-WAR] SubmitSI   >>socid>>>" + socid);
                            submittedMasterVo.setStrsoc(socid);
                            //  Draft Hidden cargo values
                            submittedMasterVo.setCargoNo(request.getParameter(cargononame));
                            submittedMasterVo.setTareweight(request.getParameter(tareweightname));
                            submittedMasterVo.setCargoweight(request.getParameter(cargoweightname));
                            submittedMasterVo.setGrossweight(request.getParameter(grossweightname));
                            submittedMasterVo.setWeightUnit(request.getParameter(weightunitname));
                            submittedMasterVo.setTempMax(request.getParameter(tempmaxname));
                            submittedMasterVo.setTempMin(request.getParameter(tempminname));
                            submittedMasterVo.setSetTemp(request.getParameter(settempname));
                            submittedMasterVo.setEqpStatus(request.getParameter(eqpStatusName));
                            submittedMasterVo.setOog(request.getParameter(oogname));
                            submittedMasterVo.setNor(request.getParameter(norname));
                            submittedMasterVo.setRefer(request.getParameter(refername));
                            submittedMasterVo.setTempunit(request.getParameter(tempunitname));

                            submittedMasterVo.setHumidity(request.getParameter(humidityId));
                            submittedMasterVo.setHumidity_flag(request.getParameter(humidityFlagId));
                            submittedMasterVo.setDrains(request.getParameter(drainsId));
                            submittedMasterVo.setCo2value(request.getParameter(co2Id));
                            submittedMasterVo.setO2value(request.getParameter(o2Id));
                            submittedMasterVo.setVentilation(request.getParameter(ventilationId));
                            System.out.println("[ECOM-WAR] SubmitSI   ventvaluesi" + request.getParameter(ventilationId));
                            dgFlag = request.getParameter(dgFlagName);

                            //
                            siSealVOObj.setStrShipperSeal(shipperSeal);
                            siSealVOObj.setStrCarrierSeal(carrierSeal);
                            siSealVOObj.setStrIsSelect("YES");
                            siSealVOObj.setStrCargoid(cargoId);
                            cmdName = containerNo + "#newcmdty#";
                            wtName = containerNo + "#newwt#";
                            ntName = containerNo + "#newnt#";
                            bycode = containerNo + "#bycode#";
                            byname = containerNo + "#byname#";
                            slcode = containerNo + "#slcode#";
                            slname = containerNo + "#slname#";
                            cmdty_hblno = containerNo + "#cmdty_hblno#";
                            qtyName = containerNo + "#newqty#";
                            unitName = containerNo + "#unit#";
                            cidName = containerNo + "#cid#";
                            pkgName = containerNo + "#newpack#";
                            volName = containerNo + "#newvol#";
                            vutName = containerNo + "#vunit#";
                            mrkName = containerNo + "#mark#";
                            desName = containerNo + "#descp#";
                            hscodename = containerNo + "#hscode#";
                            hssuffixcode = containerNo + "hssuffixcode";
                            String[] cmdObj = request.getParameterValues(cmdName);
                            String[] wtObj = request.getParameterValues(wtName);
                            String[] ntObj = request.getParameterValues(ntName);
                            String[] bycoObj = request.getParameterValues(bycode);
                            String[] bynObj = request.getParameterValues(byname);
                            String[] slcObj = request.getParameterValues(slcode);
                            String[] slnObj = request.getParameterValues(slname);
                            String[] hblnoObj = request.getParameterValues(cmdty_hblno);
                            String[] qtyObj = request.getParameterValues(qtyName);
                            String[] unitObj = request.getParameterValues(unitName);
                            String[] cidObj = request.getParameterValues(cidName);
                            String[] pkgObj = request.getParameterValues(pkgName);
                            String[] volObj = request.getParameterValues(volName);
                            String[] vutObj = request.getParameterValues(vutName);
                            String[] mrkObj = request.getParameterValues(mrkName);
                            String[] desObj = request.getParameterValues(desName);
                            String[] hscodeObj = request.getParameterValues(hscodename);
                            String[] hssuffixcodeobj = request.getParameterValues(hssuffixcode);

                            //   Draft Hidden cargo values
                            String[] imocontactobj = request.getParameterValues(imocontactname);
                            String[] imophoneobj = request.getParameterValues(imophonename);
                            String[] itemforwardobj = request.getParameterValues(itemforwardname);
                            String[] itemtopobj = request.getParameterValues(itemtopname);
                            String[] itemrightobj = request.getParameterValues(itemrightname);
                            String[] itemaftobj = request.getParameterValues(itemaftname);
                            String[] hazardousobj = request.getParameterValues(hazardousname);
                            String[] itemlengthUnitobj = request.getParameterValues(itemlengthUnitname);
                            String[] itemleftobj = request.getParameterValues(itemleftname);
                            //
                            cargoWt = 0;
                            grossWt = 0;
                            tarWt = 0;
                            netWt = 0;
                            strTareWt = Validation.checkNull(request.getParameter(tareweightname));

                            if (strTareWt.length() > 0) {
                                tarWt = Double.parseDouble(strTareWt);
                            }
                            System.out.println("[ECOM-WAR] SubmitSI   >>>unitObj>>" + unitObj);
                            //
                            if (wtObj != null && wtObj.length > 0) {
                                System.out.println("[ECOM-WAR] SubmitSI   >>>hscodeObj>>>" + hscodeObj.length);
                                detailLen = wtObj.length;
                                for (int j = 0; j < detailLen; j++) {
                                    submittedCntDetails = new SIContainerDetailVO();
                                    submittedCntDetails.setStrCommodity(Validation.checkNull(cmdObj[j]));
                                    String comdty = Validation.checkNull(cmdObj[j]);
                                    String commodyCode = "";
                                    String comArr[];
                                    System.out.println("[ECOM-WAR] SubmitSI   >>>comdty>>>" + comdty);
                                    if (comdty.length() > 0) {
                                        comArr = comdty.split("~~");
                                        if (comArr.length > 1) {
                                            commodyCode = comArr[1].trim();
                                        }
                                    }
                                    System.out.println("[ECOM-WAR] SubmitSI   >>commodyCode>>>" + commodyCode);
                                    if ("1".equalsIgnoreCase(siOBJ.getAppVersion())) {
                                        submittedCntDetails.setHsCode(Validation.checkNull(hscodeObj[j]));
                                    } else {
                                        System.out.println("[ECOM-WAR] SubmitSI   >>>hscodeObj[j]>>>" + hscodeObj[j]);
                                        submittedCntDetails.setIsFakCommodity("N");
                                        System.out.println("[ECOM-WAR] SubmitSI   >>fakComdtyCode>>" + fakComdtyCode);
                                        if (commodyCode != null && commodyCode.length() > 0 && commodyCode.equalsIgnoreCase(fakComdtyCode)) {
                                            System.out.println("[ECOM-WAR] SubmitSI   >>>entered in if");
                                            submittedCntDetails.setIsFakCommodity("Y");
                                            submittedCntDetails.setHsCode(Validation.checkNull(hscodeObj[j]).replaceAll("&quot;", "\""));
                                        } else {

                                            hsArr = Validation.checkNull(hscodeObj[j]).split("~~");
                                            if (hsArr.length > 1) {
                                                System.out.println("[ECOM-WAR] SubmitSI   >>hs code in submit si>>" + Validation.checkNull(hsArr[1]));
                                                submittedCntDetails.setHsCode(Validation.checkNull(hsArr[1]).trim());
                                                submittedCntDetails.setHsCodeDesc(Validation.checkNull(hsArr[0]).trim().replaceAll("&quot;", "\""));
                                            }
                                        }
                                        System.out.println("[ECOM-WAR] SubmitSI   >>>submittedCntDetails>>>" + submittedCntDetails.getIsFakCommodity());
                                    }
                                    if (hssuffixcodeobj != null && j < hssuffixcodeobj.length) {
                                        submittedCntDetails.setHssuffixcode(hssuffixcodeobj[j]);
                                    }
                                    submittedCntDetails.setStrQty(Validation.checkNull(qtyObj[j]));
                                    submittedCntDetails.setStrUnit(Validation.checkNull(unitObj[j]));
                                    strCargoWt = Validation.checkNull(Validation.checkNull(wtObj[j]));
                                    strCargoWt = strCargoWt.replace(",", "");
                                    submittedCntDetails.setStrWgt(Validation.checkNull(strCargoWt));
                                    if (strCargoWt.length() > 0) {
                                        cargoWt = cargoWt + Double.parseDouble(strCargoWt);
                                    }
                                    submittedCntDetails.setStrCid(Validation.checkNull(cidObj[j]));
                                    submittedCntDetails.setStrPkg(Validation.checkNull(pkgObj[j]));
                                    submittedCntDetails.setStrVol(Validation.checkNull(volObj[j]));
                                    submittedCntDetails.setStrVut(Validation.checkNull(vutObj[j]));
                                    submittedCntDetails.setStrMrk(Validation.checkNull(mrkObj[j]));
                                    submittedCntDetails.setStrDes(Validation.checkNull(desObj[j]));
                                    System.out.println("[ECOM-WAR] SubmitSI   >>>descript>>>>" + Validation.checkNull(desObj[j]));
                                    // Draft Hidden cargo values
                                    submittedCntDetails.setImocontact(imocontactobj[0]);
                                    submittedCntDetails.setImophone(imophoneobj[0]);
                                    if (itemforwardobj != null && itemforwardobj.length > j) {
                                        submittedCntDetails.setItemforward(itemforwardobj[j]);
                                        submittedCntDetails.setItemtop(itemtopobj[j]);
                                        submittedCntDetails.setItemright(itemrightobj[j]);
                                        submittedCntDetails.setItemaft(itemaftobj[j]);
                                        submittedCntDetails.setItemlengthUnit(itemlengthUnitobj[j]);
                                        submittedCntDetails.setItemleft(itemleftobj[j]);
                                    } else {
                                        submittedCntDetails.setItemforward(itemforwardobj[0]);
                                        submittedCntDetails.setItemtop(itemtopobj[0]);
                                        submittedCntDetails.setItemright(itemrightobj[0]);
                                        submittedCntDetails.setItemaft(itemaftobj[0]);
                                        submittedCntDetails.setItemlengthUnit(itemlengthUnitobj[0]);
                                        submittedCntDetails.setItemleft(itemleftobj[0]);
                                    }

                                    submittedCntDetails.setHazardous(hazardousobj[j]);
                                    submittedCntDetails.setNettwt(ntObj[j]);
                                    strNetWt = Validation.checkNull(Validation.checkNull(ntObj[j]));
                                    if (strNetWt.length() > 0) {
                                        netWt = netWt + Double.parseDouble(strNetWt);
                                    }
                                    //

                                    if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                                        submittedCntDetails.setByscode(bycoObj[j]);
                                        submittedCntDetails.setBysname(bynObj[j]);
                                        submittedCntDetails.setSlscode(slcObj[j]);
                                        submittedCntDetails.setSlsname(slnObj[j]);
                                        submittedCntDetails.setCmdty_hblno(hblnoObj[j]);
                                    }
                                    submittedAListObj.add(submittedCntDetails);
                                }
                            }
                            submittedMasterVo.setCargoweight(String.valueOf(cargoWt));
                            submittedMasterVo.setNetweight(String.valueOf(netWt));
                            System.out.println("[ECOM-WAR] SubmitSI   >>>>>netWt>>" + netWt);
                            System.out.println("[ECOM-WAR] SubmitSI   >>>tarewrt!!!!!!=>" + tarWt);
                            System.out.println("[ECOM-WAR] SubmitSI   >>>>>cargoWt>>" + cargoWt);
                            grossWt = tarWt + cargoWt;
                            System.out.println("[ECOM-WAR] SubmitSI   >>grossWt>>" + grossWt);
                            submittedMasterVo.setGrossweight(String.valueOf(grossWt));
                            submittedMasterVo.setDgFlag(dgFlag);
                            siCNTValueVO.setSiSealObj(siSealVOObj);
                            siCNTValueVO.setCntDeatilsObj(submittedAListObj);
                            submitteSICntInfoHashObj.put(submittedMasterVo, siCNTValueVO);
                        }
                    }
                    if (submitteSICntInfoHashObj != null && submitteSICntInfoHashObj.size() > 0) {
                        Collection<SIMasterVO> collection = submitteSICntInfoHashObj.keySet();
                        SIMasterVO[] empArray = new SIMasterVO[submitteSICntInfoHashObj.size()];
                        SIMasterVO[] array = (SIMasterVO[]) collection.toArray(empArray);
                        Arrays.sort(array, new NumberComparator());
                    }
                    System.out.println("[ECOM-WAR] SubmitSI   >>submitteSICntInfoHashObj>>" + submitteSICntInfoHashObj);
                    Map<SIMasterVO, SIConatainerValueVO> SICntInfoHashObj = (HashMap<SIMasterVO, SIConatainerValueVO>) session.getAttribute("containerListForSI");
                    Map<SIMasterVO, SIConatainerValueVO> DraftSICntInfoHashObj = (HashMap<SIMasterVO, SIConatainerValueVO>) session.getAttribute("containerListDraftForSI");
                    errors = siValidate.validateSIForm(siOBJ, SICntInfoHashObj, submitteSICntInfoHashObj, contexts, session); // server validation
                    request.setAttribute("ACTION", "SUBMISSION_EDIT");
                    int alsize = 0;
                    if (SICntInfoHashObj != null && SICntInfoHashObj.size() > 0) {
                        alsize = SICntInfoHashObj.size();
                        Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = SICntInfoHashObj.entrySet();
                        for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                            SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                            SIConatainerValueVO cntDetails = new SIConatainerValueVO();
                            if (submitteSICntInfoHashObj.get(siMasterVO) != null) {
                                cntDetails = submitteSICntInfoHashObj.get(siMasterVO);
                                ArrayList s = cntDetails.getCntDeatilsObj();
                                for (int cv = 0; cv < s.size(); cv++) {
                                }
                            } else {
                                cntDetails = (SIConatainerValueVO) cntMap.getValue();
                                ArrayList s = cntDetails.getCntDeatilsObj();
                                for (int cv = 0; cv < s.size(); cv++) {
                                }
                            }
                            SICntInfoHashObj.put(siMasterVO, cntDetails);
                        }
                    }
                    ArrayList voidcargoObj = new ArrayList();
                    ArrayList voidcomObj = new ArrayList();
                    if (DraftSICntInfoHashObj != null && DraftSICntInfoHashObj.size() > 0) {
                        Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = DraftSICntInfoHashObj.entrySet();
                        for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                            SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                            SIConatainerValueVO cntDetails = new SIConatainerValueVO();
                            SIConatainerValueVO DraftcntDetails = new SIConatainerValueVO();
                            ArrayList<SIContainerDetailVO> comVoListObj = new ArrayList<SIContainerDetailVO>();
                            ArrayList<SIContainerDetailVO> DraftcomVoListObj = new ArrayList<SIContainerDetailVO>();
                            SIContainerDetailVO comVo = new SIContainerDetailVO();
                            SIContainerDetailVO DraftcomVo = new SIContainerDetailVO();
                            if (submitteSICntInfoHashObj.get(siMasterVO) != null) {
                                cntDetails = submitteSICntInfoHashObj.get(siMasterVO);
                                DraftcntDetails = DraftSICntInfoHashObj.get(siMasterVO);
                                comVoListObj = cntDetails.getCntDeatilsObj();
                                if (DraftcntDetails != null) {
                                    DraftcomVoListObj = DraftcntDetails.getCntDeatilsObj();
                                    if (DraftcomVoListObj != null && DraftcomVoListObj.size() > 0) {
                                        int comSize = DraftcomVoListObj.size();
                                        for (int i = 0; i < comSize; i++) {
                                            DraftcomVo = (SIContainerDetailVO) DraftcomVoListObj.get(i);
                                            int isExists = comVoListObj.indexOf(DraftcomVo);
                                            if (isExists == -1) {
                                                voidcomObj.add(DraftcomVo.getStrCid());
                                            }
                                        }
                                    }
                                }
                            } else {
                                SIConatainerValueVO voiddcntDetails = new SIConatainerValueVO();
                                SISealVO voidsealvo = new SISealVO();
                                voiddcntDetails = DraftSICntInfoHashObj.get(siMasterVO);
                                if (voiddcntDetails != null) {
                                    voidsealvo = voiddcntDetails.getSiSealObj();
                                    voidcargoObj.add(voidsealvo.getStrCargoid());
                                }
                            }
                        }
                    }
                    session.setAttribute("containerListForSI", SICntInfoHashObj);
                    String bookno = (String) session.getAttribute("edit_bookno");
                    HashMap hm_routing = (HashMap) session.getAttribute("bk_port_details");
                    SIPartiesVO parties_routing = (SIPartiesVO) hm_routing.get("routing");
                    // Swap mask port to actual port for Origin (POL) and Delivery (POD) free text fields before saving
                    // Check port code if it's a mask port, swap to actual port and get port name, else use normal workflow from SESSION_PORTLIST
                    String pla_free_txt_val = MaskPortUtil.getPortNameByCode(parties_routing.getReceipt(), siOBJ.getPla_free_txt());
                    String pol_free_txt_val = MaskPortUtil.getPortNameByCode(parties_routing.getPol(), siOBJ.getPol_free_txt());
                    String pod_free_txt_val = MaskPortUtil.getPortNameByCode(parties_routing.getPod(), siOBJ.getPod_free_txt());
                    String pld_free_txt_val = MaskPortUtil.getPortNameByCode(parties_routing.getDelivery(), siOBJ.getPld_free_txt());
                    
                    // System.out.println("[ECOM-WAR] SubmitSI Before swap - pla_free_txt: " + siOBJ.getPla_free_txt() 
                    //         + ", pol_free_txt: " + siOBJ.getPol_free_txt()
                    //         + ", pod_free_txt: " + siOBJ.getPod_free_txt()
                    //         + ", pld_free_txt: " + siOBJ.getPld_free_txt());
                    // System.out.println("[ECOM-WAR] SubmitSI After swap - pla_free_txt: " + pla_free_txt_val 
                    //         + ", pol_free_txt: " + pol_free_txt_val
                    //         + ", pod_free_txt: " + pod_free_txt_val
                    //         + ", pld_free_txt: " + pld_free_txt_val);
                    
                    parties_routing.setPla_free_txt(pla_free_txt_val);
                    parties_routing.setPol_free_txt(pol_free_txt_val);
                    parties_routing.setPod_free_txt(pod_free_txt_val);
                    parties_routing.setPld_free_txt(pld_free_txt_val);
                    parties_routing.setVslvoyage_free_txt(siOBJ.getVslvoyage_free_txt());
                    parties_routing.setShipperHeader(siOBJ.getHeaderShipper());
                    parties_routing.setConsigneeHeader(siOBJ.getHeaderConsignee());
                    parties_routing.setNotifyHeader(siOBJ.getHeaderNotify());
                    System.out.println("[ECOM-WAR] SubmitSI   >>siOBJ.getHeaderNotify()>>>" + siOBJ.getHeaderNotify());
                    System.out.println("[ECOM-WAR] SubmitSI   getHeaderShipper=> " + siOBJ.getHeaderShipper());
                    System.out.println("[ECOM-WAR] SubmitSI   getHeaderConsignee==> " + siOBJ.getHeaderConsignee());
                    parties_routing.setShipperName(siOBJ.getSpnameheader());
                    parties_routing.setConsigneeName(siOBJ.getConsnameheader());
                    parties_routing.setNotifyName(siOBJ.getNpnameheader());
                    parties_routing.setAESNumber(siOBJ.getSiAESnumber());
                    parties_routing.setAESDiscliamer(siOBJ.getAESDisclaimer());
                    parties_routing.setReleasetype(siOBJ.getReleasetype());
                    parties_routing.setDeclaredValue(Validation.checkNull(siOBJ.getDeclaredValue()).replaceAll(",", ""));
                    parties_routing.setACID(siOBJ.getAcidValue());
                    parties_routing.setFreight(siOBJ.getFreightoption());
                    parties_routing.setBltype(Validation.checkNull(siOBJ.getBltype()));
                    parties_routing.setEbook_t2lfiling(Validation.checkNull(siOBJ.getEbook_t2lfiling()));
                    parties_routing.setEbook_nvocc(resolveNvoccForSave(contexts, siOBJ, parties_routing));
                    parties_routing.setPackinglst(Validation.checkNull(siOBJ.getPackinglst()));
                    parties_routing.setInvoiceupload(Validation.checkNull(siOBJ.getInvoiceupload()));
                    parties_routing.setEbook_EnsDocType(Validation.checkNull(siOBJ.getEbook_EnsDocType()));
                    parties_routing.setBltype_UAE(Validation.checkNull(siOBJ.getBlType_UAE()));
                    parties_routing.setMpci_id(Validation.checkNull(siOBJ.getMpciId_UAE()));
                    parties_routing.setCargoprotectid(siOBJ.getCargoprotectid());
                    parties_routing.setOrgTelType(siOBJ.getOrgTelType());
                    System.out.println("Release type==>" + parties_routing.getOrgTelType() + "//" + siOBJ.getOrgTelType());
                    String bookinguserid = "Ecomm";//"web";
                    request.setAttribute("SIEditForm", siOBJ);
                    saveErrors(request, errors);

                    System.out.println("[ECOM-WAR] SubmitSI   freightoptionval=================" + siOBJ.getFreightoption());
                    if (errors.size() > 0) {
                        forward = "failure";
                        request.setAttribute("sierrors", "sierrors");
                    } else {
                        String filepath = "";
                        boolean flag = false;
                        //
                        fileDetailsVo.setFilePath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH));
                        String enableFileBackendValidation = (String) contexts.getAttribute(GeneralConf.EnableFileBackendValidation);
                        String ecomFileExt = (String) contexts.getAttribute(GeneralConf.EcomFileExtension);
                        FileExtensionValidation flext = new FileExtensionValidation();

                        String restrictedFiled = null, restrictedFiledSize = null;

                        if (file1.getFileName() != null && file1.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file1, ecomFileExt, request)) {
                            /*File Ffilepath1 = new File(file_path, file1.getFileName());
                             fos = new FileOutputStream(Ffilepath1);
                             fos.write(file1.getFileData());
                             flag = true;*/
                            file_name1 = file1.getFileName();
                            fileDetailsVo.setVgmFileName(file1.getFileName());
                            fileDetailsVo.setVgmFileData(file1.getFileData());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file1 != null && file1.getFileName() != null && file1.getFileName().length() > 0) {
                            //fileDetailsVo.setVgmFileName(siOBJ.getFilename1());
                            //file_name1 = siOBJ.getFilename1();
                            fileDetailsVo.setVgmFileName(null);
                        }

                        if (file2.getFileName() != null && file2.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file2, ecomFileExt, request)) {
                            /*File Ffilepath2 = new File(file_path, file2.getFileName());
                             fos = new FileOutputStream(Ffilepath2);
                             fos.write(file2.getFileData());
                             flag = true;*/
                            file_name2 = file2.getFileName();
                            fileDetailsVo.setDgFileName(file2.getFileName());
                            fileDetailsVo.setDgFileData(file2.getFileData());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file2.getFileName() != null && file2.getFileName().length() > 0) {
                            //fileDetailsVo.setDgFileName(siOBJ.getFilename2());
                            //file_name2 = siOBJ.getFilename2();
                            fileDetailsVo.setDgFileName(null);

                        }
                        if (file3.getFileName() != null && file3.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file3, ecomFileExt, request)) {
                            /*File Ffilepath3 = new File(file_path, file3.getFileName());
                             fos = new FileOutputStream(Ffilepath3);
                             fos.write(file3.getFileData());
                             flag = true;*/
                            file_name3 = file3.getFileName();
                            fileDetailsVo.setLolFileName(file3.getFileName());
                            fileDetailsVo.setLolFileData(file3.getFileData());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file3.getFileName() != null && file3.getFileName().length() > 0) {
                            //fileDetailsVo.setLolFileName(siOBJ.getFilename3());
                            //file_name3 = siOBJ.getFilename3();
                            fileDetailsVo.setLolFileName(null);
                        }
                        if (file4.getFileName() != null && file4.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file4, ecomFileExt, request)) {
                            /*File Ffilepath4 = new File(file_path, file4.getFileName());
                             fos = new FileOutputStream(Ffilepath4);
                             fos.write(file4.getFileData());
                             flag = true;*/
                            file_name4 = file4.getFileName();
                            fileDetailsVo.setMsdsFileName(file4.getFileName());
                            fileDetailsVo.setMsdsFileData(file4.getFileData());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file4.getFileName() != null && file4.getFileName().length() > 0) {
                            //fileDetailsVo.setMsdsFileName(siOBJ.getFilename4());
                            //file_name4 = siOBJ.getFilename4();
                            fileDetailsVo.setMsdsFileName(null);
                        }
                        if (file5.getFileName() != null && file5.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file5, ecomFileExt, request)) {
                            /*File Ffilepath5 = new File(file_path, file5.getFileName());
                             fos = new FileOutputStream(Ffilepath5);
                             fos.write(file5.getFileData());
                             flag = true;*/
                            file_name5 = file5.getFileName();
                            fileDetailsVo.setOthersFileName(file5.getFileName());
                            fileDetailsVo.setOthersFileData(file5.getFileData());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file5.getFileName() != null && file5.getFileName().length() > 0) {
                            //fileDetailsVo.setOthersFileName(siOBJ.getFilename5());
                            //file_name5 = siOBJ.getFilename5();
                            fileDetailsVo.setOthersFileName(null);
                        }
                        if (file6 != null && file6.getFileName() != null && file6.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file6, ecomFileExt, request)) {
                            fileDetailsVo.setVgmFileData1(file6.getFileData());
                            fileDetailsVo.setVgmFileName1(file6.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file6.getFileName() != null && file6.getFileName().length() > 0) {
                            fileDetailsVo.setVgmFileName1(null);
                        }
                        if (file7 != null && file7.getFileName() != null && file7.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file7, ecomFileExt, request)) {
                            fileDetailsVo.setVgmFileData2(file7.getFileData());
                            fileDetailsVo.setVgmFileName2(file7.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file7.getFileName() != null && file7.getFileName().length() > 0) {
                            fileDetailsVo.setVgmFileName2(null);
                        }
                        if (file8 != null && file8.getFileName() != null && file8.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file8, ecomFileExt, request)) {
                            fileDetailsVo.setDgFileData1(file8.getFileData());
                            fileDetailsVo.setDgFileName1(file8.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file8.getFileName() != null && file8.getFileName().length() > 0) {
                            fileDetailsVo.setDgFileName1(null);
                        }
                        if (file9 != null && file9.getFileName() != null && file9.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file9, ecomFileExt, request)) {
                            fileDetailsVo.setDgFileData2(file9.getFileData());
                            fileDetailsVo.setDgFileName2(file9.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file9.getFileName() != null && file9.getFileName().length() > 0) {
                            fileDetailsVo.setDgFileName2(null);
                        }
                        if (file10 != null && file10.getFileName() != null && file10.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file10, ecomFileExt, request)) {
                            fileDetailsVo.setLolFileData1(file10.getFileData());
                            fileDetailsVo.setLolFileName1(file10.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file10.getFileName() != null && file10.getFileName().length() > 0) {
                            fileDetailsVo.setLolFileName1(null);
                        }
                        if (file11 != null && file11.getFileName() != null && file11.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file11, ecomFileExt, request)) {
                            fileDetailsVo.setLolFileData2(file11.getFileData());
                            fileDetailsVo.setLolFileName2(file11.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file11.getFileName() != null && file11.getFileName().length() > 0) {
                            fileDetailsVo.setLolFileName2(null);
                        }
                        if (file12 != null && file12.getFileName() != null && file12.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file12, ecomFileExt, request)) {
                            fileDetailsVo.setMsdsFileData1(file12.getFileData());
                            fileDetailsVo.setMsdsFileName1(file12.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file12.getFileName() != null && file12.getFileName().length() > 0) {
                            fileDetailsVo.setMsdsFileName1(null);
                        }
                        if (file13 != null && file13.getFileName() != null && file13.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file13, ecomFileExt, request)) {
                            fileDetailsVo.setMsdsFileData2(file13.getFileData());
                            fileDetailsVo.setMsdsFileName2(file13.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file13.getFileName() != null && file13.getFileName().length() > 0) {
                            fileDetailsVo.setMsdsFileName2(null);
                        }
                        if (file14 != null && file14.getFileName() != null && file14.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file14, ecomFileExt, request)) {
                            fileDetailsVo.setOthersFileData1(file14.getFileData());
                            fileDetailsVo.setOthersFileName1(file14.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file14.getFileName() != null && file14.getFileName().length() > 0) {
                            fileDetailsVo.setOthersFileName1(null);
                        }
                        if (file15 != null && file15.getFileName() != null && file15.getFileName().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file15, ecomFileExt, request)) {
                            fileDetailsVo.setOthersFileData2(file15.getFileData());
                            fileDetailsVo.setOthersFileName2(file15.getFileName());
                            fileDetailsVo.setFileAvailable(true);
                        } else if (file15.getFileName() != null && file15.getFileName().length() > 0) {
                            fileDetailsVo.setOthersFileName2(null);
                        }

                        restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
                        restrictedFiled = (restrictedFiled != null && restrictedFiled.length() > 0) ? restrictedFiled.substring(0, restrictedFiled.length() - 1) : "";
                        request.setAttribute("restrictedFile", restrictedFiled);
                        restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
                        restrictedFiledSize = (restrictedFiledSize != null && restrictedFiledSize.length() > 0) ? restrictedFiledSize.substring(0, restrictedFiledSize.length() - 1) : "";
                        request.setAttribute("restrictedFileSize", restrictedFiledSize);

                        fileDetailsVo.setDeletedfilename(Validation.checkNull(siOBJ.getDeletedfilename()));
                        fileDetailsVo.setEnableZipConversion(Validation.checkNull((String) contexts.getAttribute(GeneralConf.Enable_ZipFile_Conversion)));
                        /*if (flag) {
                         fos.flush();
                         fos.close();
                         }*/
 /*if (file_name1 != null && file_name1.length() > 0) {
                         filepath += sino + EcomConsts.FILETYPE_VGM + file_name1 + "~~~";
                         }

                         if (file_name2 != null && file_name2.length() > 0) {
                         filepath += sino + EcomConsts.FILETYPE_DG + file_name2 + "~~~";
                         }

                         if (file_name3 != null && file_name3.length() > 0) {
                         filepath += sino + EcomConsts.FILETYPE_LOL + file_name3 + "~~~";
                         }

                         if (file_name4 != null && file_name4.length() > 0) {
                         filepath += sino + EcomConsts.FILETYPE_MSDS + file_name4 + "~~~";
                         }

                         if (file_name5 != null && file_name5.length() > 0) {
                         filepath += sino + EcomConsts.FILETYPE_OTHERS + file_name5 + "~~~";
                         }

                         if (filepath != null && filepath.length() > 0) {
                         filepath = filepath.substring(0, filepath.length() - 3);
                         }

                         System.out.println("[ECOM-WAR] SubmitSI   #########filepath#########" + filepath);*/
                        String ccmailid = Validation.checkNull((String) session.getAttribute("SESSION_USER_CC_MAILID"));
                        String isCCMail = "NO";
                        if (ccmailid.length() > 0 && "NO DATA".equalsIgnoreCase(ccmailid)) {
                            session.setAttribute("SESSION_USER_CC_MAILID", siOBJ.getCcMailId());
                            isCCMail = "YES";
                        } else {
                            isCCMail = "NO";
                        }
                        String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                        String addremarks = siOBJ.getSicustremarks();
                        System.out.println("[ECOM-WAR] SubmitSI   >>addremarksbl>>" + addremarks);
                        String selectedLang = (String) session.getAttribute("SelectedLanguage");

                        //Get Charge details 
                        String jsonchargestr = (String) request.getParameter("ChargeDetailsJSON");
                        System.out.println("[ECOM-WAR] SubmitSI   jsonchargestr=>" + jsonchargestr);
                        List<ChargesVo> chargelist = null;
                        if (jsonchargestr != null && jsonchargestr.length() > 0) {
                            Gson gson = new Gson();
                            Type jstype = new TypeToken<List<ChargesVo>>() {
                            }.getType();
                            chargelist = gson.fromJson(jsonchargestr, jstype);
                            System.out.println("[ECOM-WAR] SubmitSI   chargelist=> " + chargelist);
                            parties_routing.setChargeDetails(chargelist);
                        }
                        ENSDeclarantVo edvo = new ENSDeclarantVo();
                        BeanUtils.copyProperties(edvo, siOBJ);
                        parties_routing.setEnsdecvo(edvo);
                        System.out.println("[ECOM-WAR] SubmitSI   edvo----> " + edvo.getEuCustZone());
                        ReturnVO returnVO = (ReturnVO) sibeanobj.saveToSI(bookno, custcode, bookinguserid, submitteSICntInfoHashObj, hm, parties_routing, savetype, sino, voidcargoObj, voidcomObj, loginUser, bl_no, client_code, fileDetailsVo, addremarks, custTypeConfig, siOBJ.getAppVersion(), siOBJ.getCcMailId(), isCCMail, selectedLang, custCountry, customerMap);
                        if (returnVO.getStrRefNo().length() > 0) {
                            if ("Y".equalsIgnoreCase(Validation.checkNull((String) session.getAttribute("ENABLE_SI_AMENDMENT_MAIL")))) {
                                HashMap bookingCache = new HashMap();
                                bookingCache.put("LRPBookNo", bookno);
                                bookingCache.put("EDIReaderID", returnVO.getReaderid());
                                bookingCache.put("SI_No", returnVO.getStrRefNo());
                                bookingCache.put("BL_No", bl_no);
                                bookingCache.put("Type", "SI");
                                bookingCache.put("ToMail", Validation.checkNull(returnVO.getAdmToMailId()).length() > 0 ? "Y" : "N");
                                System.out.println("[ECOM-WAR] SubmitSI   bookingCache===>" + bookingCache);
                                session.setAttribute("AMENDMENT_BOOKING_MAIL_CACHE", bookingCache);
                            }
                            try {
                                InsuranceUtils IU = new InsuranceUtils();
                                String isaviblequote = (String) session.getAttribute("CREATE_QUOTE_NEW");
                                System.out.println("[ECOM-WAR] SubmitSI   isaviblequote " + isaviblequote);
                                List insurancelist = null;
                                List insurancecusttypelis = (List) session.getAttribute("CREATE_QUOTE_CUST_TYPE");
                                List insuredpartylist = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY");
                                List insuredpartyemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL");
                                List insuredpartyprimaryemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL");
                                System.out.println("[ECOM-WAR] SubmitSI   insurancecusttypelis==> " + insurancecusttypelis);
                                System.out.println("[ECOM-WAR] SubmitSI   CREATE_QUOTE_INPUT_JSON==> " + session.getAttribute("CREATE_QUOTE_INPUT_JSON"));
                                if (isaviblequote != null && "Y".equalsIgnoreCase(isaviblequote)) {
                                    insurancelist = IU.callAPI("quotecreate", returnVO.getStrRefNo(), contexts, session);
                                    System.out.println("[ECOM-WAR] SubmitSI   insurancelist==> " + insurancelist);
                                    List inslis = IU.arrangeInsuranceDetailsToInsert(insurancecusttypelis, insurancelist, "SI", insuredpartylist, insuredpartyemail, insuredpartyprimaryemail);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String strDate = null;
                            Date dt = null;
                            SimpleDateFormat reqFormat = null;
                            reqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dt = new Date();
                            String message = null;
                            String useremail = (String) session.getAttribute("SESSION_EMAIL");
                            strDate = reqFormat.format(dt);
                            String formatDate = DateConversion_New.convertDefaultFormat(strDate);
                            String initiatorID = "ECOM-SI";
                            addccMailid = (String) contexts.getAttribute(GeneralConf.additionalsiccmail);

                            if ("S".equalsIgnoreCase(returnVO.getStrSaveType())) {
                                //
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
                                        Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
                                        Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
                                        Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                                        Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
                                        Hashtable packagemap = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                                        String carriercode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
                                        String agencyRefNo = (String) session.getAttribute("SIAGENCYREFNO_For_Mail");
                                        String Ventilationlabel = "Ventilation" + (String) contexts.getAttribute("Ventilation");
                                        masterCache.put("VESSELLIST", vessellist);
                                        masterCache.put("CONTRYLIST", contrylist);
                                        masterCache.put("SERVICELIST", serlist);
                                        masterCache.put("PACKAGELIST", packagemap);
                                        masterCache.put("PORTLIST", portlist);
                                        masterCache.put("carriercode", carriercode);
                                        masterCache.put("comlist", comlist);
                                        masterCache.put("agencyRefNo", agencyRefNo);
                                        masterCache.put("SHOW_CHARGES_IN_SIBL", (String) contexts.getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL));
                                        masterCache.put("is_SI_restriction", is_SI_restriction);
                                        masterCache.put(GeneralConf.Display_HS_NAME_CODE, (String) contexts.getAttribute(GeneralConf.Display_HS_NAME_CODE));
                                        masterCache.put("EnableReeferDetails", (String) contexts.getAttribute(GeneralConf.EnableReeferDetails));
                                        masterCache.put("Enable_Eori_Details", (String) contexts.getAttribute(GeneralConf.GENS));
                                        String imagepath = (String) contexts.getAttribute("Imagepath");
                                        String logo_name = (String) contexts.getAttribute(GeneralConf.Logo_Name);
                                        masterCache.put("Ventilation", Ventilationlabel);
                                        masterCache.put("enable_zipcode", (String) contexts.getAttribute(GeneralConf.Enable_Zip_Code));
                                        details_mail = new CommonMail().SIBLcommonMailMail(imagepath, logo_name, returnVO.getStrRefNo(), masterCache, bookno, "", siOBJ, parties_routing, submitteSICntInfoHashObj, "SI");
                                    }
                                    //preview mail end
                                }
                                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_CUST))) {
                                    message = details_mail;
                                } else if (custCountry != null && "CN".equalsIgnoreCase(custCountry)) {
                                    Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                                    Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
                                    HashMap masterCache = new HashMap();
                                    masterCache.put("CustCompany", custCompany);
                                    masterCache.put("comlist", comlist);
                                    masterCache.put("PORTLIST", portlist);
                                    masterCache.put("VESSELLIST", vessellist);
                                    message = new CommonMailForCHCust().newCHSIMail(masterCache, siOBJ, parties_routing, submitteSICntInfoHashObj, returnVO);
                                } else {
                                    message = returnVO.getStrMailMsg();
                                    message = message.replace("@@si_no", "SION" + sino);
                                    message = message.replace("@date", formatDate);
                                    
                                    // Check if POL or POD is a mask port - if so, swap actual port names to mask port names
                                    String polCode = Validation.checkNull(parties_routing.getPol());
                                    String podCode = Validation.checkNull(parties_routing.getPod());
                                    String pol = (String) portlist.get(polCode);
                                    String pod = (String) portlist.get(podCode);
                                    boolean isMaskPort = MaskPortUtil.isActualPort(polCode) || MaskPortUtil.isActualPort(podCode);
                                    if (isMaskPort) {
                                        pol = MaskPortUtil.swapActualPortToMaskPortWithName(polCode + " - " + pol);
                                        pod = MaskPortUtil.swapActualPortToMaskPortWithName(podCode + " - " + pod);
                                        System.out.println("[ECOM-WAR] SubmitSI Mask port detected - swapped pol: " + pol + ", pod: " + pod);
                                    }
                                    message = message.replace("@pol", pol);
                                    message = message.replace("@pod", pod);

                                    System.out.println("[ECOM-WAR] SubmitSI   >>mailAlertMap>>" + mailAlertMap);
                                    System.out.println("[ECOM-WAR] SubmitSI   >>sendMail>>" + sendMail);
                                }
                                String apEnable = Validation.checkNull((String) session.getAttribute("ENABLE_SI_AMENDMENT_MAIL"));
                                Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                                Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
                                HashMap masterCache = new HashMap();
                                masterCache.put("apname", apname);
                                masterCache.put("comlist", comlist);
                                masterCache.put("PORTLIST", portlist);
                                masterCache.put("VESSELLIST", vessellist);
                                masterCache.put("ENABLEAMENDMENTCOMMAIL", apEnable);

                                if (sendMail) {
                                    String ccMail = Validation.checkNull(siOBJ.getCcMailId());
//                                    if (addccMailid != null && addccMailid.trim().length() > 0) {
//                                        if (ccMail.length() > 0) {
//                                            ccMail = addccMailid + "," + ccMail;
//                                        } else {
//
//                                            ccMail = addccMailid;
//
//                                        }
//                                    }
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
                                        //1   mail.sendMail(authObj, agencymail);
                                        cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                                        mail = new Mailer();
                                    } catch (Exception exp) {
                                        exp.printStackTrace();
                                    }
                                }
                                if (Validation.checkNull(returnVO.getAdmToMailId()).length() > 0) {
                                    if ("N".equalsIgnoreCase(apEnable)) {
                                        //
                                        try {
                                            String message1 = "";
                                            if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_AGENCY))) {
                                                message1 = details_mail;
                                            } else {
//                                        message1 = returnVO.getAdmMailMsg();
//                                        message1 = message1.replace("@apname", apname);
//                                        message1 = message1.replace("@customer_remarks", Validation.checkNull(addremarks));
//                                        message1 = message1.replace("@remarks", Validation.checkNull(addremarks));
//                                        message1 = message1.replace("@pol", (String) portlist.get(Validation.checkNull(parties_routing.getPol())));
//                                        message1 = message1.replace("@pod", (String) portlist.get(Validation.checkNull(parties_routing.getPod())));
                                                message1 = new CommonMailForAgency().newAgencySIMail(masterCache, siOBJ, parties_routing, submitteSICntInfoHashObj, returnVO);
                                            }
                                            System.out.println("[ECOM-WAR] SubmitSI   ===========================>>>>>>>" + returnVO.getAdmToMailId());
                                            mail.setTo(returnVO.getAdmToMailId());
                                            String subject = returnVO.getAdmSubject();
                                            subject = subject.replace("@apname", apname);
                                            mail.setSubject(subject);
                                            mail.setMessage(message1);
                                            mail.setCc(null);
                                            mail.setAttachment(returnVO.getFilenames());
                                            mail.setAttachmentpath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH) + File.separator + returnVO.getStrRefNo() + File.separator);
                                            //1  mail.sendMail(authObj, agencymail);
                                            cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                                        } catch (Exception exp) {
                                            exp.printStackTrace();
                                        }
                                    } else {
                                        String mailmsg = "";
                                        String messagee = new CommonMailForAgency().newAgencySIMail(masterCache, siOBJ, parties_routing, submitteSICntInfoHashObj, returnVO);
                                        HashMap mailCache = new HashMap();
                                        System.out.println("[ECOM-WAR] SubmitSI   AdmToMailId" + returnVO.getAdmToMailId() + "AdmSubject" + returnVO.getAdmSubject() + "Filenames" + returnVO.getFilenames());
                                        mailCache.put("AdmToMailId", returnVO.getAdmToMailId().toString());
                                        mailCache.put("AdmSubject", returnVO.getAdmSubject());
                                        mailCache.put("Filenames", returnVO.getFilenames());
                                        mailCache.put("StrRefNo", returnVO.getStrRefNo());
                                        mailCache.put("Mailmsg", messagee);
//                                    mailCache.put("amendmentMaster", amendmentVO);
                                        session.setAttribute("SI_AMEDMAIL_DETAILS", mailCache); //agency
                                        System.out.println("[ECOM-WAR] SubmitSI   mailCache==>" + mailCache);

                                    }
                                }

                            } else if ("D".equalsIgnoreCase(returnVO.getStrSaveType())) {
                            }
                        }
                        request.setAttribute("SI_RESPONSE_VO", returnVO);
                    }
                } else {
                    System.out.println("[ECOM-WAR] SubmitSI   Duplicate Submission of the form");
                    request.setAttribute("SI_RESPONSE_VO", new ReturnVO());
                }
            } else {
                session.setAttribute("IS_ACTIVEPAGE", "SI");
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";
                } else {
                    session.setAttribute("systemid", session.getId());
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                String custcode = (String) session.getAttribute("SESSION_USERCODE");
                String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
                String SICreForCnfrmBk = (String) contexts.getAttribute(GeneralConf.SI_CREATE_FOR_CONFIRMED_BOOKING);
                ArrayList SIdetails = sibeanobj.getSISummary(custcode, custTypeConfig, v1DataEnableStatus, SICreForCnfrmBk);
                session.setAttribute("SIdetails", SIdetails);
                forward = "duplicate";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM-WAR] SubmitSI   =========SI forward============>>>>" + forward);
        return mapping.findForward(forward);
    }

    private String resolveNvoccForSave(ServletContext contexts, SIEditForm siOBJ, SIPartiesVO parties_routing) {
        String enableNvocc = Validation.checkNull((String) contexts.getAttribute(GeneralConf.Enable_NVOCC_In_SI));
        String eligibleCountries = Validation.checkNull((String) contexts.getAttribute(GeneralConf.SI_NVOCC_Eligible_Country));
        if (!"Y".equalsIgnoreCase(enableNvocc) || eligibleCountries.length() == 0) {
            return "";
        }
        String podCountry = Validation.checkNull(siOBJ.getFinalportid());
        if (podCountry.length() == 0 && parties_routing != null) {
            podCountry = Validation.checkNull(parties_routing.getFinalport());
        }
        if (podCountry.length() == 0 && parties_routing != null) {
            podCountry = Validation.checkNull(parties_routing.getDelivery());
        }
        if (podCountry.length() > 2) {
            podCountry = podCountry.substring(0, 2);
        }
        boolean eligible = false;
        for (String cty : eligibleCountries.split(",")) {
            if (podCountry.equalsIgnoreCase(cty.trim())) {
                eligible = true;
                break;
            }
        }
        if (!eligible) {
            return "";
        }
        return "Y".equalsIgnoreCase(Validation.checkNull(siOBJ.getEbook_nvocc())) ? "Y" : "N";
    }

}
