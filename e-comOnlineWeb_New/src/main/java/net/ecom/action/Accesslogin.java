package net.ecom.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.captcha.VerifyRecaptcha;
import net.ecom.common.CommonPasswordValidation;
import net.ecom.common.CryptoLibrary;
import net.ecom.common.Currencyformat;
import net.ecom.common.GeneralConf;
import net.ecom.formbeans.LoginForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.EbookingViewAll;
import net.ecom.vo.MailAlertVO;
import net.ecom.vo.RegisterMenuVO;
import net.ecom.vo.UserData;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.Mailer;
import net.ecom.common.PasswordExpiredOTPMail;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.vo.AdminCustomerVo;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.UnclearedCargoVo;
import net.ecom.vo.UserCreationVO;

public class Accesslogin extends org.apache.struts.action.Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomAdminCntrlBeanRemote adminBeanObj = EJBLookUp.getAdminBeanLookUp();
    EcomPhase2BeanRemote phase2bean = EJBLookUp.getPhase2BeanLookUp();
    private CacheContainer quickpinContainer;
    private Cache<String, String> quickPinSession;
    CryptoLibrary crypt = null;

    @Override
    @SuppressWarnings("static-access")
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "invalid";
        String resData = "";
        try {
            crypt = new CryptoLibrary();
            HttpSession session = request.getSession(false);
            String typeregistion = (String) request.getAttribute("typeregistion");
            String type = request.getParameter("type");
            ServletContext contexts = servlet.getServletContext();
            String client_code = (String) contexts.getAttribute("client_code");
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            String Dash_display = (String) request.getServletContext().getAttribute(GeneralConf.DASHBOARD_DISPLAY);
            String custstmtsp = (String) request.getServletContext().getAttribute(GeneralConf.USE_CUSTOMETSTATEMENT_SP);
            String product_Dashboard = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Product_Dashboard);
            String csfrominv = (String) request.getServletContext().getAttribute(GeneralConf.CSFROMINV);
            String secretKey = Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.CaptchaSecretKey));
            String toDisplayGgleCaptcha = (String) request.getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
            String Enable_Password_Validation = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Password_Validation);
            String Password_Validation_API_URL = (String) request.getServletContext().getAttribute(GeneralConf.Password_Validation_API_URL);
            String Disable_Admin_Login_In_Home_Page = (String) request.getServletContext().getAttribute(GeneralConf.Disable_Admin_Login_In_Home_Page);
            String Enable_Dashboard_Filter = (String) request.getServletContext().getAttribute(GeneralConf.Enable_DashBoard_Filter);
            String enable_EcomGate_AgencyMailID = (String) request.getServletContext().getAttribute(GeneralConf.Enable_EcomGate_AgencyMailID);
            String allowIncompletedRegToLogin = (String) request.getServletContext().getAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN);
            String incompletedRegMENUlist = (String) request.getServletContext().getAttribute(GeneralConf.INCOMPLETED_REG_MENU_LIST);
            if ("registerresult".equalsIgnoreCase(typeregistion)) {
                session.setAttribute("SESSION_LOGIN_TYPE", "U");
                session.setAttribute("IS_VIA_LOGIN", "NO");
                forward = "SuccessRegistration";
            } else if ("port".equalsIgnoreCase(type) || "vessel".equalsIgnoreCase(type) || "location".equalsIgnoreCase(type)) {
                System.out.println("[ECOM-WAR] Accesslogin    ====Session Id port=====>>>>" + session.getId());
                if ("port".equalsIgnoreCase(type)) {
                    forward = "portSearch";
                } else if ("vessel".equalsIgnoreCase(type)) {
                    forward = "vesselSearch";
                } else if ("location".equalsIgnoreCase(type)) {
                    if ("loginschedule".equalsIgnoreCase(request.getParameter("schetype"))) {
                        request.setAttribute("schetype", "loginschedule");
                        request.setAttribute("loginschepol", request.getParameter("pol"));
                        request.setAttribute("loginschepod", request.getParameter("pod"));
//                        request.setAttribute("loginschespdate", request.getParameter("scdshipmentdate"));
                        request.setAttribute("loginschefdate", request.getParameter("scdshipmentdate"));
                        request.setAttribute("loginschetdate", request.getParameter("todate"));
//  session.setAttribute("SESSION_LOGIN_TYPE", "U");
                    }
//                     System.out.println("[ECOM-WAR] Accesslogin    >>>loginschefdate>>" + request.getParameter("scdshipmentdate"));
//                     System.out.println("[ECOM-WAR] Accesslogin    >>>loginschetdate" + request.getParameter("todate"));
                    System.out.println("[ECOM-WAR] Accesslogin    loginschefdate: " + request.getParameter("scdshipmentdate") + ", loginschetdate: " + request.getParameter("todate"));

                    forward = "locationSearch";
                }
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId());  // session Id
                }
                System.out.println("[ECOM-WAR] Accesslogin    strIsViaLogin IN ACCESS LOGIN" + strIsViaLogin);
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_ACTIVEPAGE", "Schedule");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
            } else if ("Tracking".equalsIgnoreCase(type)) {
                forward = "newTracking";
                session.setAttribute("systemid", session.getId());  // session Id
                System.out.println("[ECOM-WAR] Accesslogin    >>>request.getParameter('tracktype')>>" + request.getParameter("tracktype"));
                if ("logintracking".equalsIgnoreCase(request.getParameter("tracktype"))) {
                    boolean verify = VerifyRecaptcha.verify((String) request.getParameter("g-recaptcha-response"), secretKey);
                    if ((!"Y".equalsIgnoreCase(toDisplayGgleCaptcha) || verify)) {
                        session.setAttribute("systemid", session.getId());
                        request.setAttribute("tracktype", "logintracking");
                        request.setAttribute("logintrackrefno", request.getParameter("logintracno"));
                        String refNoType = ecomCntrlObj.checkBLBookingNo(request.getParameter("logintracno"));
//                         System.out.println("[ECOM-WAR] Accesslogin    >>refNoType>>>" + refNoType);
                        request.setAttribute("refNoType", refNoType);
                        // session.setAttribute("SESSION_LOGIN_TYPE", "U");
//                         System.out.println("[ECOM-WAR] Accesslogin    >>>request.getParameter('logintracno')>>" + request.getParameter("logintracno"));
                        System.out.println("[ECOM-WAR] Accesslogin    refNoType: " + refNoType + ", request.getParameter('logintracno'): " + request.getParameter("logintracno"));

                    }
                }
                //session.setAttribute("IS_MENU_LOCK", strMenu);
                Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                session.setAttribute("SESSION_AUTHENTICATION", authObj);
                //session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("IS_ACTIVEPAGE", "Tracking");
                //session.setAttribute("NEWDESIGNPRODUCT", "Tracking");
            } else if ("ratescontract".equalsIgnoreCase(type)) {
                forward = "ratescontract";
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null) && (!"".equalsIgnoreCase(strIsViaLogin)) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";
                } else {
                    session.setAttribute("systemid", session.getId());
                }
                session.setAttribute("systemid", session.getId());
                session.setAttribute("IS_ACTIVEPAGE", "tariff");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("NEWDESIGNPRODUCT", "tariff");
            } else if ("ratesquote".equalsIgnoreCase(type)) {
                forward = "ratesquote";
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null) && (!"".equalsIgnoreCase(strIsViaLogin)) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";
                } else {
                    session.setAttribute("systemid", session.getId());
                }
                session.setAttribute("systemid", session.getId());
                session.setAttribute("IS_ACTIVEPAGE", "RateQuote");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("NEWDESIGNPRODUCT", "RateQuote");
            } else if ("tariff".equalsIgnoreCase(type)) {
                forward = "tariff";
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null) && (!"".equalsIgnoreCase(strIsViaLogin)) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";
                } else {
                    session.setAttribute("systemid", session.getId());
                }
                if ("loginratetype".equalsIgnoreCase(request.getParameter("loginratetype"))) {
                    request.setAttribute("loginratetype", "loginratetype");
                    request.setAttribute("loginratepol", request.getParameter("ratepol"));
                    request.setAttribute("loginratepod", request.getParameter("ratepod"));
                    request.setAttribute("loginrateshipdate", request.getParameter("shipmentdate"));
                    request.setAttribute("logineqp", request.getParameter("rateseqp"));
                    //  session.setAttribute("SESSION_LOGIN_TYPE", "U");
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_ACTIVEPAGE", "tariff");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                //    session.setAttribute("NEWDESIGNPRODUCT", "tariff");
            } else if ("surcharge".equalsIgnoreCase(type)) {
                forward = "surcharge";
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null) && (!"".equalsIgnoreCase(strIsViaLogin)) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";
                } else {
                    session.setAttribute("systemid", session.getId());
                }
                /* if ("loginratetype".equalsIgnoreCase(request.getParameter("loginratetype"))) {
                    request.setAttribute("loginratetype", "loginratetype");
                    request.setAttribute("loginratepol", request.getParameter("ratepol"));
                    request.setAttribute("loginratepod", request.getParameter("ratepod"));
                    request.setAttribute("loginrateshipdate", request.getParameter("shipmentdate"));
                    request.setAttribute("logineqp", request.getParameter("rateseqp"));
                    //  session.setAttribute("SESSION_LOGIN_TYPE", "U");
                } */
                // System.out.println("[ECOM-WAR] Accesslogin    SESSION ID IN ACCESS LOGIN" + session.getAttribute("systemid"));
                // System.out.println("[ECOM-WAR] Accesslogin    strIsViaLogin IN ACCESS LOGIN" + strIsViaLogin);
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_ACTIVEPAGE", "surcharge");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                //session.setAttribute("NEWDESIGNPRODUCT", "tariff");
            } else if ("rates".equalsIgnoreCase(type)) {
                forward = "rates";
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null) && (!"".equalsIgnoreCase(strIsViaLogin)) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";
                } else {
                    session.setAttribute("systemid", session.getId());
                }
                if ("loginratetype".equalsIgnoreCase(request.getParameter("loginratetype"))) {
                    request.setAttribute("loginratetype", "loginratetype");
                    request.setAttribute("loginratepol", request.getParameter("ratepol"));
                    request.setAttribute("loginratepod", request.getParameter("ratepod"));
                    request.setAttribute("loginrateshipdate", request.getParameter("shipmentdate"));
                    request.setAttribute("logineqp", request.getParameter("rateseqp"));
                    //  session.setAttribute("SESSION_LOGIN_TYPE", "U");
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_ACTIVEPAGE", "rates");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                //    session.setAttribute("NEWDESIGNPRODUCT", "tariff");
            } else if ("viewprofile".equalsIgnoreCase(type)) {
                CustomerProfile profileVO;
                String strloginame = (String) session.getAttribute("SESSION_LOGINNAME");
                if (strloginame != null && strloginame.length() > 0) {
                    profileVO = ecomreg.getuserProfile(strloginame);
                    session.setAttribute("userDetails", profileVO);
                    forward = "profile";
                } else {
                    forward = "";
                }
                session.setAttribute("IS_ACTIVEPAGE", "profile");
            } else if ("raterequest".equalsIgnoreCase(type)) {
                // Map eqpmap = (Map) contexts.getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                // request.setAttribute("actualfreight", request.getParameter("actualfreight"));
                // request.setAttribute("pol", request.getParameter("pol"));
                // request.setAttribute("pod", request.getParameter("pod"));
                // request.setAttribute("quantity", request.getParameter("quantity"));
                //request.setAttribute("commodity", request.getParameter("commodity"));
                //String eqptype = request.getParameter("eqptype");

                //request.setAttribute("eqptype",eqptype);
                session.setAttribute("IS_ACTIVEPAGE", "raterequest");
                session.setAttribute("systemid", session.getId());
                forward = "raterequest";
            } else if ("quote".equalsIgnoreCase(type)) {

                session.setAttribute("IS_ACTIVEPAGE", "quote");
                session.setAttribute("systemid", session.getId());
                forward = "quote";
            } else if ("Registration".equalsIgnoreCase(type)) {
                saveToken(request);
                session.setAttribute("IS_VIA_LOGIN", "NO");
                request.setAttribute("IS_REGISTRATION", "YES");
                session.setAttribute("systemid", session.getId());  // session Id
                ArrayList countryListObj = LabelBeanConversion.labelListConversion(ecomCntrlObj.getCountryNameandCode());
                //session.setAttribute("CAPIMAGEURL", "ecomonline");
                session.setAttribute("COUNTRYNAME_COUNTRYCODE", countryListObj);
                ArrayList timeZoneListValues = (ArrayList) ecomreg.getTimeZone();
                if (timeZoneListValues != null) {
                    session.setAttribute("PREFERREDTIMEZONELIST", LabelBeanConversion.labelListConversion((List<DropDownVO>) timeZoneListValues.get(0)));
                    System.out.println("[ECOM-WAR] Accesslogin    preflistwar" + session.getAttribute("PREFERREDTIMEZONELIST"));
                    session.setAttribute("PREFERREDTIMEZONESMAP", timeZoneListValues.get(1));
                }

                //
                forward = "newRegister";
                session.setAttribute("IS_MENU_LOCK", "YES");
            } else if ("reset".equalsIgnoreCase(type)) {
                ArrayList countryListObj = LabelBeanConversion.labelListConversion(ecomCntrlObj.getCountryNameandCode());
                session.setAttribute("COUNTRYNAME_COUNTRYCODE", countryListObj);
                forward = "resetpass";
                Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                //session.setAttribute("CAPIMAGEURL", "ecomonline");
                session.setAttribute("NEWDESIGNPRODUCT", "resetpassword");
                session.setAttribute("SESSION_AUTHENTICATION", authObj);
            } else if ("contactus".equalsIgnoreCase(type)) {
                saveToken(request);
                CustomerProfile profileVO = null;
                String strloginame = (String) session.getAttribute("SESSION_LOGINNAME");
                session.setAttribute("NEWDESIGNPRODUCT", "contactus");
                session.setAttribute("systemid", session.getId());
                Hashtable authmail = ecomCntrlObj.getglobalconfigMail();
                session.setAttribute("SESSION_globalconfig", authmail);
                Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                session.setAttribute("SESSION_AUTHENTICATION", authObj);
                ArrayList countryListObj = LabelBeanConversion.labelListConversion(ecomCntrlObj.getCountryNameandCode());
                session.setAttribute("COUNTRYNAME_COUNTRYCODE", countryListObj);
                if (strloginame != null && strloginame.length() > 0) {
                    profileVO = ecomreg.getuserProfile(strloginame);
                    session.setAttribute("usercontactDetails", profileVO);
                    forward = "contactus";
                } else {
                    session.setAttribute("usercontactDetails", profileVO);
                    forward = "contactus";
                }
            } else if ("bkgandapmt".equalsIgnoreCase(type)) {
                CustomerProfile profileVO = null;
                String strloginame = (String) session.getAttribute("SESSION_LOGINNAME");
                session.setAttribute("NEWDESIGNPRODUCT", "contactus");
                session.setAttribute("systemid", session.getId());
                Hashtable authmail = ecomCntrlObj.getglobalconfigMail();
                session.setAttribute("SESSION_globalconfig", authmail);
                Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                session.setAttribute("SESSION_AUTHENTICATION", authObj);
                if (strloginame != null && strloginame.length() > 0) {
                    profileVO = ecomreg.getuserProfile(strloginame);
                    session.setAttribute("bookingandappmt", profileVO);
                    forward = "bkgandapmt";
                } else {
                    session.setAttribute("bookingandappmt", profileVO);
                    forward = "bkgandapmt";
                }
            } else if ("mailalert".equalsIgnoreCase(type)) {
                session.setAttribute("IS_ACTIVEPAGE", "mailalert");
                session.setAttribute("NEWDESIGNPRODUCT", "mailalert");
                forward = "mailalert";
            } else if ("forgot".equalsIgnoreCase(type)) {
                session.setAttribute("IS_VIA_LOGIN", "NO");
                request.setAttribute("IS_REGISTRATION", "YES");
                session.setAttribute("systemid", session.getId());  // session Id
                ArrayList countryListObj = LabelBeanConversion.labelListConversion(ecomCntrlObj.getCountryNameandCode());
                session.setAttribute("COUNTRYNAME_COUNTRYCODE", countryListObj);
                forward = "forgotpass";
                session.setAttribute("IS_MENU_LOCK", "YES");
                Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                session.setAttribute("SESSION_AUTHENTICATION", authObj);
                //session.setAttribute("SESSION_LOGIN_TYPE", "U");
            } else if ("getConfigValues".equalsIgnoreCase(type)) {
                List GlobalConfigList = null;
                String admincpanel = Validation.checkNull(request.getParameter("adminpage"));
                GlobalConfigList = ecomCntrlObj.getGlobalConfigDetails(admincpanel);
                request.setAttribute("GlobalConfigList", GlobalConfigList);
                forward = "configView";
            } else if ("getImageUploadDetails".equalsIgnoreCase(type)) {
                List ConfigList = null;
                String showactive = Validation.checkNull(request.getParameter("showactive"));
                ConfigList = adminBeanObj.getImageUploadDetails(showactive);
                request.setAttribute("ImgUploadTableList", ConfigList);
                if ("Y".equalsIgnoreCase(showactive)) {
                    forward = "ImageUploadRearrangeView";
                } else {
                    forward = "ImageUploadView";
                }

            } else if ("getCustAdvisDetails".equalsIgnoreCase(type)) {
                List ConfigList = null;
                ConfigList = adminBeanObj.getCustomerAdvisoryDetails();
                request.setAttribute("CustAdvisTableList", ConfigList);
                forward = "CustomerAdvisoryView";

            } else if ("dashboard".equalsIgnoreCase(type)) {
                String userid = (String) session.getAttribute("SESSION_USERCODE");
                // Map<String, String> dashboardMap = ecomCntrlObj.getDashboardCount(userid, v1DataEnableStatus);
                // request.setAttribute("CUSTOMERDASHBOARDCOUNT", dashboardMap);
                if ("Y".equalsIgnoreCase(Dash_display)) {
                    List<UnclearedCargoVo> dashlis = adminBeanObj.getEcomUnclearedCargoDetails(userid);
                    int deliveredsum = dashlis.stream().filter(o -> o.getDelcontrcnt() != null).mapToInt(o -> Integer.parseInt(o.getDelcontrcnt())).sum();
                    int remainingsum = dashlis.stream().filter(o -> o.getRemcontrcnt() != null).mapToInt(o -> Integer.parseInt(o.getRemcontrcnt())).sum();
                    request.setAttribute("dashremainingsum", remainingsum);
                    request.setAttribute("dashdeliveredsum", deliveredsum);
                    request.setAttribute("UnclearedCargoDetails", dashlis);
                    String fromdate = DateConversion_New.getCurrentDateDbFormat();
                    String outbal = null;
                    System.out.println("[ECOM-WAR] Accesslogin    csfrominv=================================> " + csfrominv);
                    if ("N".equalsIgnoreCase(csfrominv)) {
                        outbal = adminBeanObj.getDashboardOutstandingBalance(fromdate, userid);
                    } else if ("Y".equalsIgnoreCase(custstmtsp)) {
                        outbal = adminBeanObj.getDashboardOutstandingBalfromcuststmtsp(fromdate, userid);
                    } else {
                        Map<String, List> resmap = phase2bean.getOutstandingDetails("A", userid, "", "", "CO");
                        if (resmap != null) {
                            List<String> lis = (List) resmap.get("totalOutstandingBal");
                            if (lis != null && lis.size() > 0) {
                                outbal = lis.get(0);
                            }
                        }
                    }
                    if (outbal != null && outbal.length() > 0) {
                        outbal = Currencyformat.rupeeFormat(outbal);
                    }
                    request.setAttribute("DASH_OUTSTANDING_BALANCE", outbal);
                }
                if ("Y".equalsIgnoreCase(product_Dashboard)) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    String monthyr = month + "-" + year;
                    System.out.println("[ECOM-WAR] Accesslogin    month" + monthyr);
                    Map<String, Map> countMap = (Map<String, Map>) ecomCntrlObj.getDashboardCounts(userid, monthyr,Enable_Dashboard_Filter);
                    //Map<String, String> count = (Map<String, String>) countMap.get("Booking");
                    Map<String, String> count = bookingObj.getBookingCount(userid, custTypeConfig, v1DataEnableStatus,"");
                    Set s = count.entrySet();
                    Iterator it = s.iterator();
                    while (it.hasNext()) {
                        Map.Entry m = (Map.Entry) it.next();
                        String key = (String) m.getKey();
                        String value = (String) m.getValue();
                        if ("Draft".equalsIgnoreCase(key)) {
                            session.setAttribute("Draft", value);
                        } else if ("Send to Agency".equalsIgnoreCase(key)) {
                            session.setAttribute("Send to Agency", value);
                        } else if ("Approved".equalsIgnoreCase(key)) {
                            session.setAttribute("Approved", value);
                        } else if ("Declined".equalsIgnoreCase(key)) {
                            session.setAttribute("Declined", value);
                        } else if ("Confirmed".equalsIgnoreCase(key)) {
                            session.setAttribute("Confirmed", value);
                        }
                    }
                    /*  bl count */
                    Map<String, String> blcount = (Map<String, String>) countMap.get("BL");
                    Set sval = blcount.entrySet();
                    Iterator it1 = sval.iterator();
                    while (it1.hasNext()) {
                        Map.Entry mm = (Map.Entry) it1.next();
                        String keye = (String) mm.getKey();
                        String values = (String) mm.getValue();
                        if ("Confirmed".equalsIgnoreCase(keye)) {
                            session.setAttribute("Confirmedbl", values);
                        } else if ("Issued".equalsIgnoreCase(keye)) {
                            session.setAttribute("Issued", values);
                        }
                    }
                    /* bl end count*/
                    Map<String, String> sival = (Map<String, String>) countMap.get("SI");
                    Set sicount = sival.entrySet();
                    Iterator itsi = sicount.iterator();
                    while (itsi.hasNext()) {
                        Map.Entry mm = (Map.Entry) itsi.next();
                        String keye = (String) mm.getKey();
                        //int values = (Integer) mm.getValue();
                        String str = String.valueOf(mm.getValue());
                        if ("Submitted".equalsIgnoreCase(keye)) {
                            session.setAttribute("Submittedsi", str);
                        } else if ("Drafts".equalsIgnoreCase(keye)) {
                            session.setAttribute("Draftsi", str);
                        } else if ("csi".equalsIgnoreCase(keye)) {
                            session.setAttribute("SingleSI", str);
                        } else if ("cmsi".equalsIgnoreCase(keye)) {
                            session.setAttribute("MultipleSI", str);
                        } else if ("Accepted".equalsIgnoreCase(keye)) {
                            session.setAttribute("Accepted", str);
                        }
                    }
                }
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId());  // session Id
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("successPage", "Dash");
                forward = "dashboard";
                session.setAttribute("IS_ACTIVEPAGE", "dashboard");
                session.setAttribute("NEWDESIGNPRODUCT", "dashboard");
            } else if ("prvView".equalsIgnoreCase(type)) {
                List RegisterMenuList = null;
                String Message = " ";
                String usertype = request.getParameter("usertype");

                if (usertype.equalsIgnoreCase("user")) {
                    usertype = "U";
                } else if (usertype.equalsIgnoreCase("Vendor")) {
                    usertype = "V";
                }
                RegisterMenuList = ecomCntrlObj.getRegisteredMenus(Message, usertype);
                request.setAttribute("RegisterMenuList", RegisterMenuList);
                forward = "loadvalue";
            } else if ("en".equalsIgnoreCase(type)) {
                session.setAttribute("clientcode", "en");
                session.setAttribute("SelectedLanguage", "en");
                //   session.setAttribute("org.apache.struts.action.LOCALE",Locale.ENGLISH);
                /* String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                if (userCode != null && userCode.length() > 0) {
                    session.setAttribute("successPage", "port");
                    forward = "portSearch";
                    session.setAttribute("IS_ACTIVEPAGE", "Schedule");
                    session.setAttribute("IS_MENU_LOCK", "NO");
                    session.setAttribute("NEWDESIGNPRODUCT", "schedules");
                } else {
                    forward = "preloginLanguage";
                }*/
                forward = "preloginLanguage";
            } else if ("Ch".equalsIgnoreCase(type)) {
                session.setAttribute("clientcode", "zh");
                session.setAttribute("SelectedLanguage", "ch");
                /* String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                //  session.setAttribute("org.apache.struts.action.LOCALE", Locale.CHINESE);
                if (userCode != null && userCode.length() > 0) {
                    session.setAttribute("successPage", "port");
                    forward = "portSearch";
                    session.setAttribute("IS_ACTIVEPAGE", "Schedule");
                    session.setAttribute("IS_MENU_LOCK", "NO");
                    session.setAttribute("NEWDESIGNPRODUCT", "schedules");
                } else {
                    forward = "preloginLanguage";
                } */
                forward = "preloginLanguage";
            } else if ("Ma".equalsIgnoreCase(type)) {
                session.setAttribute("clientcode", "ma");
                session.setAttribute("SelectedLanguage", "ma");
                forward = "preloginLanguage";
            } else if ("es".equalsIgnoreCase(type)) {
                session.setAttribute("clientcode", "es");
                session.setAttribute("SelectedLanguage", "es");
                forward = "preloginLanguage";
            } else if ("usercreation".equalsIgnoreCase(type)) {
                forward = "usercreation";
                CustomerProfile profileVO = null;
                String strUsercode = (String) session.getAttribute("SESSION_USERCODE");
                String strloginame = (String) session.getAttribute("SESSION_LOGINNAME");
                profileVO = ecomreg.getuserProfile(strloginame);
                session.setAttribute("userDetails", profileVO);
                session.setAttribute("viewusrArray", profileVO);
                session.setAttribute("NEWDESIGNPRODUCT", "usercreation");
                String accessallowed = ecomreg.getalloweduser(strUsercode);
                //session.setAttribute("CAPIMAGEURL", "ecomonline");
                session.setAttribute("accessallowed", accessallowed);
                session.setAttribute("successPage", "usercreation");
                session.setAttribute("IS_ACTIVEPAGE", "usercreation");
                session.setAttribute("IS_VIA_LOGIN", "YES");
                session.setAttribute("IS_MENU_LOCK", "NO");
                session.setAttribute("NEWDESIGNPRODUCT", "usercreation");
            } else if ("usercreationview".equalsIgnoreCase(type)) {
                forward = "usercreationview";
                String strUsercode = (String) session.getAttribute("SESSION_USERCODE");
                ArrayList viewusrArray = ecomreg.getuserDetails(strUsercode);
                session.setAttribute("systemid", session.getId());
                request.setAttribute("viewusrArray", viewusrArray);
                session.setAttribute("successPage", "usercreation");
                session.setAttribute("IS_ACTIVEPAGE", "usercreation");
                session.setAttribute("IS_VIA_LOGIN", "YES");
                session.setAttribute("IS_MENU_LOCK", "NO");
                session.setAttribute("NEWDESIGNPRODUCT", "usercreation");
            } else if ("usercreationsubmit".equalsIgnoreCase(type)) {
                String strUsercode = (String) session.getAttribute("SESSION_USERCODE");
                UserCreationVO uservo = new UserCreationVO();
                String result = null;
                String requrl = request.getRequestURL().toString();
                String requri = request.getRequestURI();
                requrl = requrl.replace(requri, "");
                requrl = requrl + request.getContextPath();
                try {
                    String loginname = request.getParameter("loginname");
                    String password = request.getParameter("password");
                    String firstname = request.getParameter("firstname");
                    String lastname = request.getParameter("lastname");
                    String cust_country_code = request.getParameter("cust_country_code");
                    String cust_phone_code = request.getParameter("cust_phone_code");
                    String company_name = request.getParameter("companyname");
                    String cust_phone_no = request.getParameter("cust_phone_no");
                    String fax_country_code = request.getParameter("fax_country_code");
                    String fax_phone_code = request.getParameter("fax_phone_code");
                    String fax_phone_no = request.getParameter("fax_phone_no");
                    String mobile_code = request.getParameter("mobile_code");
                    String mobile_no = request.getParameter("mobile_no");
                    String deflanguage = request.getParameter("deflanguage");
                    String preflanguage = request.getParameter("preflanguage");
                    String email = request.getParameter("email");
                    String count = request.getParameter("user_count");
                    int user_count = Integer.parseInt(count);
                    //
                    uservo.setCompanyId(strUsercode);
                    //
                    uservo.setLoginname(loginname);
                    uservo.setPassword(password);
                    uservo.setFirstname(firstname);
                    uservo.setLastname(lastname);
                    uservo.setCust_country_code(cust_country_code);
                    uservo.setCust_phone_code(cust_phone_code);
                    uservo.setCust_phone_no(cust_phone_no);
                    uservo.setFax_country_code(fax_country_code);
                    uservo.setFax_phone_code(fax_phone_code);
                    uservo.setCompanyname(company_name);
                    uservo.setFax_phone_no(fax_phone_no);
                    uservo.setMobile_code(mobile_code);
                    uservo.setMobile_no(mobile_no);
                    uservo.setDeflanguage(deflanguage);
                    uservo.setPreflanguage(preflanguage);
                    uservo.setEmail(email);
                    String saveType = "U";
                    String comp_code = strUsercode;
                    ReturnVO rvo = ecomreg.saveToAdminRegistration(uservo, saveType, comp_code, user_count, client_code);
                    if ("Y".equalsIgnoreCase(rvo.getStrName())) {
                        String initiatorID = "ECOM-REG";
                        String frommail = (String) contexts.getAttribute("clientfrommail");
                        Mailer mail = new Mailer();
                        //   mail.setFrom(rvo.getStrFromMailId());
                        mail.setTo(rvo.getStrToMailId());
                        mail.setSubject(rvo.getStrSubject());
                        String msg = rvo.getStrMailMsg();
                        msg = msg.replace("@url", requrl);
                        mail.setMessage(msg);
                        try {
                            //1   mail.sendMail(authObj, agencymail);
                            ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                        result = "User profile has been created sucessfully";
                    } else if ("E".equalsIgnoreCase(rvo.getStrName())) {
                        result = "Error while saving";
                    } else {
                        result = "Creation of user profile limit has been reached";
                    }

                    request.setAttribute("msg", result);
                    forward = "usercreation";

                } catch (Exception e) {
                    forward = "success";
                    result = "Error while saving data";
                    System.out.println("[ECOM-WAR] Accesslogin    =====>" + e);
                    request.setAttribute("msg", result);
                }
                //
            } else if ("adminlogin".equalsIgnoreCase(type)) {
                String strDateFormat = GeneralConf.getStrConfDefDate();
                session.setAttribute("SESSION_DATEFROMAT", strDateFormat);
                LoginForm loginObj = (LoginForm) form;
                String loginName = loginObj.getUserName();
                loginName = Validation.checkNull(loginName);
                String password = loginObj.getPassword();
                password = Validation.checkNull(password);
                String servletpath = (String) session.getAttribute("ADMIN_SERVLET_PATH");
                ArrayList userDataObj = (ArrayList) ecomCntrlObj.getAgencyLoginDetails(loginName, password);
                String logintype = (String) userDataObj.get(1);
                String userName = (String) userDataObj.get(0);
                if ("C".equalsIgnoreCase(logintype) && "admin".equalsIgnoreCase(servletpath)) {
                    List details = (ArrayList) ecomreg.getAdminLoginDetails(loginName, password);
                    String custcode = null, compname = null;
                    List sessionAdmin = new ArrayList();
                    if (details != null && details.size() > 0) {
                        session.setAttribute("sessionAdmin", "Y");
                        AdminCustomerVo admin = null;
                        int size = details.size();
                        for (int i = 0; i < size; i++) {
                            admin = new AdminCustomerVo();
                            admin = (AdminCustomerVo) details.get(i);
                            custcode = admin.getCustcode();
                            compname = admin.getCompname();
                            sessionAdmin.add(custcode + " - " + compname.toUpperCase());
                        }
                        session.setAttribute("sessionAdmindetails", sessionAdmin);
                    }
                    String userid = (String) session.getAttribute("SESSION_USERCODE");
                    // Map<String, String> dashboardMap = ecomCntrlObj.getDashboardCount(userid, v1DataEnableStatus);
                    // request.setAttribute("CUSTOMERDASHBOARDCOUNT", dashboardMap);
                    if ("Y".equalsIgnoreCase(Dash_display)) {
                        List<UnclearedCargoVo> dashlis = adminBeanObj.getEcomUnclearedCargoDetails(userid);
                        int deliveredsum = dashlis.stream().filter(o -> o.getDelcontrcnt() != null).mapToInt(o -> Integer.parseInt(o.getDelcontrcnt())).sum();
                        int remainingsum = dashlis.stream().filter(o -> o.getRemcontrcnt() != null).mapToInt(o -> Integer.parseInt(o.getRemcontrcnt())).sum();
                        request.setAttribute("dashremainingsum", remainingsum);
                        request.setAttribute("dashdeliveredsum", deliveredsum);
                        request.setAttribute("UnclearedCargoDetails", dashlis);
                        String fromdate = DateConversion_New.getCurrentDateDbFormat();
                        String outbal = null;
                        if ("N".equalsIgnoreCase(csfrominv)) {
                            outbal = adminBeanObj.getDashboardOutstandingBalance(fromdate, userid);
                        } else if ("Y".equalsIgnoreCase(custstmtsp)) {
                            outbal = adminBeanObj.getDashboardOutstandingBalfromcuststmtsp(fromdate, userid);
                        } else {
                            Map<String, List> resmap = phase2bean.getOutstandingDetails("A", userid, "", "", "CO");
                            if (resmap != null) {
                                List<String> lis = (List) resmap.get("totalOutstandingBal");
                                if (lis != null && lis.size() > 0) {
                                    outbal = lis.get(0);
                                }
                            }
                        }
                        if (outbal != null && outbal.length() > 0) {
                            outbal = Currencyformat.rupeeFormat(outbal);
                        }
                        request.setAttribute("DASH_OUTSTANDING_BALANCE", outbal);
                    }
                    if ("Y".equalsIgnoreCase(product_Dashboard)) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        String monthyr = month + "-" + year;
                        System.out.println("[ECOM-WAR] Accesslogin    month" + monthyr);
                        Map<String, Map> countMap = (Map<String, Map>) ecomCntrlObj.getDashboardCounts(userid, monthyr,Enable_Dashboard_Filter);
                        //Map<String, String> count = (Map<String, String>) countMap.get("Booking");
                        Map<String, String> count = bookingObj.getBookingCount(userid, custTypeConfig, v1DataEnableStatus,"");
                        Set s = count.entrySet();
                        Iterator it = s.iterator();
                        while (it.hasNext()) {
                            Map.Entry m = (Map.Entry) it.next();
                            String key = (String) m.getKey();
                            String value = (String) m.getValue();
                            if ("Draft".equalsIgnoreCase(key)) {
                                session.setAttribute("Draft", value);
                            } else if ("Send to Agency".equalsIgnoreCase(key)) {
                                session.setAttribute("Send to Agency", value);
                            } else if ("Approved".equalsIgnoreCase(key)) {
                                session.setAttribute("Approved", value);
                            } else if ("Declined".equalsIgnoreCase(key)) {
                                session.setAttribute("Declined", value);
                            } else if ("Confirmed".equalsIgnoreCase(key)) {
                                session.setAttribute("Confirmed", value);
                            }
                        }
                        /*  bl count */
                        Map<String, String> blcount = (Map<String, String>) countMap.get("BL");
                        Set sval = blcount.entrySet();
                        Iterator it1 = sval.iterator();
                        while (it1.hasNext()) {
                            Map.Entry mm = (Map.Entry) it1.next();
                            String keye = (String) mm.getKey();
                            String values = (String) mm.getValue();
                            if ("Confirmed".equalsIgnoreCase(keye)) {
                                session.setAttribute("Confirmedbl", values);
                            } else if ("Issued".equalsIgnoreCase(keye)) {
                                session.setAttribute("Issued", values);
                            }
                        }
                        /* bl end count*/
                        Map<String, String> sival = (Map<String, String>) countMap.get("SI");
                        Set sicount = sival.entrySet();
                        Iterator itsi = sicount.iterator();
                        while (itsi.hasNext()) {
                            Map.Entry mm = (Map.Entry) itsi.next();
                            String keye = (String) mm.getKey();
                            //int values = (Integer) mm.getValue();
                            String str = String.valueOf(mm.getValue());
                            if ("Submitted".equalsIgnoreCase(keye)) {
                                session.setAttribute("Submittedsi", str);
                            } else if ("Drafts".equalsIgnoreCase(keye)) {
                                session.setAttribute("Draftsi", str);
                            } else if ("csi".equalsIgnoreCase(keye)) {
                                session.setAttribute("SingleSI", str);
                            } else if ("cmsi".equalsIgnoreCase(keye)) {
                                session.setAttribute("MultipleSI", str);
                            } else if ("Accepted".equalsIgnoreCase(keye)) {
                                session.setAttribute("Accepted", str);
                            }
                        }
                    }
                    String landingPageConfigDash = Validation.checkNull((String) contexts.getAttribute(GeneralConf.LANDING_PAGE_NEW_BOOKING));
                    if ("Y".equalsIgnoreCase(landingPageConfigDash)) {
                        forward = "bookingsummary";
                        session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "ViewBookingAll.do?BookingRequestType=ViewBookingAll");
                        session.setAttribute("IS_ACTIVEPAGE", "Booking");
                    } else {
                        forward = "dashboard";
                        session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "Accesslogin.do?type=dashboard");
                    }
                    session.setAttribute("IS_MENU_LOCK", "NO");
                    session.setAttribute("IS_VIA_LOGIN", "YES");
                    session.setAttribute("systemid", session.getId());
                    session.setAttribute("SESSION_USERNAME", userName);
                    session.setAttribute("clientcode", "en");
                    session.setAttribute("SelectedLanguage", "en");
                    session.setAttribute("SESSION_LOGIN_TYPE", "U");
                } else if ("A".equalsIgnoreCase(logintype) && "cpanel".equalsIgnoreCase(servletpath)) {
                    ArrayList menulist = (ArrayList) adminBeanObj.getEcomCpanelMenudetails();
                    System.out.println("[ECOM-WAR] Accesslogin    menulist===> " + menulist);
                    session.setAttribute("SESSION_LOGIN_TYPE", "V");
                    session.setAttribute("VENDOR_MAIN_MENU", menulist.get(0));
                    session.setAttribute("VENDOR_SUB_MENU", menulist.get(1));
                    session.setAttribute("clientcode", "en");
                    session.setAttribute("SelectedLanguage", "en");

                    session.setAttribute("AdminAgency", "AdminLoing");
                    session.setAttribute("SESSION_USERNAME", userName);
                    session.setAttribute("IS_ACTIVEPAGE", "adminreg");
                    forward = "Agencylogin";
                    session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "AdminAccess.do?type=registermenu");
                    session.setAttribute("systemid", session.getId());
                } else if ("V".equalsIgnoreCase(logintype) && "eadmin".equalsIgnoreCase(servletpath)) {
                    ArrayList userDataOb = (ArrayList) ecomreg.getAgencyLoginDet(loginName, password);
                    ArrayList userDataObject = ecomreg.getvendormodules(userDataObj.get(0).toString());
                    ArrayList menulist = ecomreg.getvendormoduleslist(loginName);
                    System.out.println("[ECOM-WAR] Accesslogin    menulist===> " + menulist);
                    session.setAttribute("SESSION_LOGIN_TYPE", "V");
                    session.setAttribute("VENDOR_MAIN_MENU", menulist.get(0));
                    session.setAttribute("VENDOR_SUB_MENU", menulist.get(1));
                    menulist = (ArrayList) menulist.get(0);
                    if (menulist.size() > 0) {
                        session.setAttribute("NEWDESIGNAGENCY", "Yes");
                        forward = "specialPrivilege";
                        session.setAttribute("IS_ACTIVEPAGE", "Vendormodulesmapping");
                        session.setAttribute("systemid", session.getId());
                    } else {
                        forward = "successvendorempty";
                        session.setAttribute("IS_ACTIVEPAGE", "vendor");
                    }
                    session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "AdminAccess.do?type=specialprivi");
                    session.setAttribute("IS_VIA_LOGIN", "NO");
                    session.setAttribute("vendormenuList", userDataObject);
                    session.setAttribute("SESSION_USERNAME", userDataObj.get(0).toString());
                    session.setAttribute("SESSION_USERCODE", "vendor");
                    System.out.println("[ECOM-WAR] Accesslogin    agency id>>>" + userDataOb.get(2).toString());
                    session.setAttribute("SESSION_AGENCY_USERID", userDataOb.get(2).toString());

                    session.setAttribute("clientcode", "en");
                    session.setAttribute("SelectedLanguage", "en");
                } /*else if ("V".equalsIgnoreCase(logintype)) {
                    session.setAttribute("AdminAgency", "VendorLogin");
                }*/ else {
                    forward = "Adminloginfailed";
                    request.setAttribute("LOGINRESPONSE_DATA", "Invalid Username / Password");
                    //   session.setAttribute("LOGINRESPONSE_DATA", "Invalid Username / Password");
                }

            } else if ("carboncalculator".equalsIgnoreCase(type)) {
                forward = "carbonCalculator";
                    session.setAttribute("systemid", session.getId());
                    session.setAttribute("IS_ACTIVEPAGE", "CarbonCalculator");
            } else if ("statsreport".equalsIgnoreCase(type)) {
                session.setAttribute("IS_ACTIVEPAGE", "raterequest");
                session.setAttribute("systemid", session.getId());
                forward = "statsreport";
            } else if ("dashboardFilterValBased".equalsIgnoreCase(type)) {
                String userid = (String) session.getAttribute("SESSION_USERCODE");
                String monthyr = request.getParameter("selectedMonth");
                // System.out.println("userid dashboardFilterValBased " + userid+" strMonth "+monthyr);
                System.out.println("[ECOM-WAR] Accesslogin    month" + monthyr);
                Map<String, Map> countMap = (Map<String, Map>) ecomCntrlObj.getDashboardCounts(userid, monthyr,Enable_Dashboard_Filter);
                //Map<String, String> count = (Map<String, String>) countMap.get("Booking");
                Map<String, String> count = bookingObj.getBookingCount(userid, custTypeConfig, v1DataEnableStatus,monthyr);
                Set s = count.entrySet();
                Iterator it = s.iterator();
                while (it.hasNext()) {
                    Map.Entry m = (Map.Entry) it.next();
                    String key = (String) m.getKey();
                    String value = (String) m.getValue();
                    if ("Draft".equalsIgnoreCase(key)) {
                        session.setAttribute("Draft", value);
                    } else if ("Send to Agency".equalsIgnoreCase(key)) {
                        session.setAttribute("Send to Agency", value);
                    } else if ("Approved".equalsIgnoreCase(key)) {
                        session.setAttribute("Approved", value);
                    } else if ("Declined".equalsIgnoreCase(key)) {
                        session.setAttribute("Declined", value);
                    } else if ("Confirmed".equalsIgnoreCase(key)) {
                        session.setAttribute("Confirmed", value);
                    }
                }
                /*  bl count */
                Map<String, String> blcount = (Map<String, String>) countMap.get("BL");
                Set sval = blcount.entrySet();
                Iterator it1 = sval.iterator();
                while (it1.hasNext()) {
                    Map.Entry mm = (Map.Entry) it1.next();
                    String keye = (String) mm.getKey();
                    String values = (String) mm.getValue();
                    if ("Confirmed".equalsIgnoreCase(keye)) {
                        session.setAttribute("Confirmedbl", values);
                    } else if ("Issued".equalsIgnoreCase(keye)) {
                        session.setAttribute("Issued", values);
                    }
                }
                /* bl end count*/
                Map<String, String> sival = (Map<String, String>) countMap.get("SI");
                Set sicount = sival.entrySet();
                Iterator itsi = sicount.iterator();
                while (itsi.hasNext()) {
                    Map.Entry mm = (Map.Entry) itsi.next();
                    String keye = (String) mm.getKey();
                    //int values = (Integer) mm.getValue();
                    String str = String.valueOf(mm.getValue());
                    if ("Submitted".equalsIgnoreCase(keye)) {
                        session.setAttribute("Submittedsi", str);
                    } else if ("Drafts".equalsIgnoreCase(keye)) {
                        session.setAttribute("Draftsi", str);
                    } else if ("csi".equalsIgnoreCase(keye)) {
                        session.setAttribute("SingleSI", str);
                    } else if ("cmsi".equalsIgnoreCase(keye)) {
                        session.setAttribute("MultipleSI", str);
                    } else if ("Accepted".equalsIgnoreCase(keye)) {
                        session.setAttribute("Accepted", str);
                    }
                }
    
                    forward = "dashboardChart"; 
         
            }else {
                LoginForm loginObj = null;
                String loginName = null;
                String password = null;
                String styleCss = null;
                String language = null;
                ArrayList templateno = new ArrayList();
                ArrayList backuptemplateno = new ArrayList();
                ArrayList tempmangetemplate = new ArrayList();
                String sessionid = request.getParameter("sessionid");
                String sessionresult = null;
                if ((sessionid != null) && (sessionid.length() > 0)) {
                    try {
//                         System.out.println("[ECOM-WAR] Accesslogin    Initcache Ecomoline webbbbbbb::::::::::::::::::::::::::::");
                        this.quickpinContainer = ((CacheContainer) new InitialContext().lookup("java:jboss/infinispan/container/quickpin"));
                        this.quickPinSession = this.quickpinContainer.getCache("quickpinsession");
                    } catch (Exception e) {
                        System.out.println("[ECOM-WAR] Accesslogin    Exception at initCache");
                    }
                    System.out.println("[ECOM-WAR] Accesslogin    sessionid::::::::::::" + sessionid);
                    sessionresult = (String) this.quickPinSession.get(sessionid);
                    System.out.println("[ECOM-WAR] Accesslogin    sessionid::::::::::::" + sessionresult);
                    if ((sessionresult != null) && (sessionresult.contains("##~~##"))) {
                        int lastIndexOf = sessionresult.lastIndexOf("##~~##");
                        loginName = sessionresult.substring(0, lastIndexOf);

                        loginName = Validation.checkNull(loginName);
                        System.out.println("[ECOM-WAR] Accesslogin    login::::::::::::" + loginName);

                        password = sessionresult.substring(lastIndexOf + 6, sessionresult.length());
                        password = Validation.checkNull(password);
                        System.out.println("[ECOM-WAR] Accesslogin    password::::" + password);
                    }
                    System.out.println("[ECOM-WAR] Accesslogin    quickPinSession:::::::" + this.quickPinSession);
                    if (this.quickPinSession.containsKey(sessionid)) {
                        this.quickPinSession.remove(sessionid);
                        System.out.println("[ECOM-WAR] Accesslogin    removeddd:::::::");
                    }
                } else {
                    loginObj = (LoginForm) form;
                    if ("Y".equalsIgnoreCase(request.getParameter("isviawebsite"))) {
                        loginName = request.getParameter("username");
                        password = request.getParameter("password");
                    } else {
                        loginName = loginObj.getUserName();
                        password = loginObj.getPassword();
                    }
                    loginName = Validation.checkNull(loginName);
                    System.out.println("[ECOM-WAR] Accesslogin    login name in access login ----------- " + loginName);

                    password = Validation.checkNull(password);
                    styleCss = loginObj.getStyleName();
                    language = loginObj.getLanguage();
                }
                if ((loginName == null) || (loginName.length() == 0) || (password == null) || (password.length() == 0)) {
                    forward = "invalid";
                } else {
                    String enable_encryption = (String) contexts.getAttribute(GeneralConf.Enable_Login_Password_Encryption);
                    ArrayList userDataOb = (ArrayList) ecomreg.getAgencyLoginDetEncrypt(loginName, password,("Y".equalsIgnoreCase(enable_encryption)));
                   // System.out.println("password===> "+password);
                    password = crypt.encrypt(password);
                    styleCss = Validation.checkNull(styleCss);
                    language = Validation.checkNull(language);
                    if ("V".equalsIgnoreCase(userDataOb.get(1).toString()) && "N".equalsIgnoreCase(Disable_Admin_Login_In_Home_Page)) {
                        ArrayList userDataObj = ecomreg.getvendormodules(userDataOb.get(0).toString());
                        ArrayList menulist = ecomreg.getvendormoduleslist(loginName);
//                         System.out.println("[ECOM-WAR] Accesslogin    menulist===> " + menulist);
                        session.setAttribute("SESSION_LOGIN_TYPE", "V");
                        session.setAttribute("VENDOR_MAIN_MENU", menulist.get(0));
                        session.setAttribute("VENDOR_SUB_MENU", menulist.get(1));
                        menulist = (ArrayList) menulist.get(0);
                        if (menulist.size() > 0) {
                            session.setAttribute("NEWDESIGNAGENCY", "Yes");
                            forward = "specialPrivilege";
                            session.setAttribute("IS_ACTIVEPAGE", "Vendormodulesmapping");
                            session.setAttribute("systemid", session.getId());
                        } else {
                            forward = "successvendorempty";
                            session.setAttribute("IS_ACTIVEPAGE", "vendor");
                        }
                        session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "AdminAccess.do?type=specialprivi");
                        session.setAttribute("IS_VIA_LOGIN", "NO");
                        session.setAttribute("vendormenuList", userDataObj);
                        session.setAttribute("SESSION_USERNAME", userDataOb.get(0).toString());
                        session.setAttribute("SESSION_USERCODE", "vendor");
//                         System.out.println("[ECOM-WAR] Accesslogin    agency id>>>" + userDataOb.get(2).toString());
                        session.setAttribute("SESSION_AGENCY_USERID", userDataOb.get(2).toString());

                        session.setAttribute("clientcode", "en");
                        session.setAttribute("SelectedLanguage", "en");

                    } // phase 2
                    else {
                        session.setAttribute("NEWDESIGNAGENCY", "NO");
                        String enable_Invalid_PasswordAttempts = (String) contexts.getAttribute(GeneralConf.Enable_Invalid_PasswordAttempts);
                        String lStatus = "N";
                        String incompletedreg = "";
                        int attemptLimit = 0;
                        int attemptCount = 0;
                        if ("Y".equalsIgnoreCase(enable_Invalid_PasswordAttempts)) {
                            UserData wrguserDataobj = ecomreg.getWrgPasslogin(loginName);
                            lStatus = wrguserDataobj.getlStatus();
                            attemptCount = wrguserDataobj.getWrgPsAtmpt();
                            String lstLockedTime = wrguserDataobj.getlTime();
                            String account_Lock_Duration = (String) contexts.getAttribute(GeneralConf.Account_Lock_Duration);
                            int lock_Duration = Integer.parseInt(account_Lock_Duration);
                            String attemptLimits = (String) contexts.getAttribute(GeneralConf.AttemptLimit);
                            attemptLimit = Integer.parseInt(attemptLimits);
                            if ((lstLockedTime != "" && lstLockedTime != null) && "Y".equalsIgnoreCase(lStatus) && DateConversion_New.calculateMinutesbetweentwoDay(lstLockedTime) >= lock_Duration) {
                                ecomreg.resetWrgPasAtmp(loginName);
                                lStatus = "N";
                                attemptCount = 0;
                            }
                        }
                        String logincaptcha = Validation.checkNull((String) contexts.getAttribute(GeneralConf.ENABLE_LOGIN_CAPTCHA));
                        boolean verify = true;
                        int passattemptCount = 0;
                        if ("Y".equalsIgnoreCase(logincaptcha)) {
                            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
                            Object attemptCountObj = session.getAttribute("InvalidpassattemptCount");
                            passattemptCount = (attemptCountObj != null) ? (int) attemptCountObj : 0;
                            if (passattemptCount >= 3) {
                                verify = VerifyRecaptcha.verify(gRecaptchaResponse, secretKey);
                            }
                        }
                        UserData getIncompletedreg = null;
                        if("Y".equalsIgnoreCase(allowIncompletedRegToLogin)) {
                            getIncompletedreg = ecomreg.getIncompletedReglogin(loginName, password,("Y".equalsIgnoreCase(enable_encryption))); 
                            String isValid = getIncompletedreg.getIsValid(); 
                            if("M".equalsIgnoreCase(isValid) || "P".equalsIgnoreCase(isValid)) {
                                incompletedreg = "Y";
                            } else {
                                incompletedreg = "N";
                            }
                            System.out.println("[ECOM-WAR] Accesslogin  incompletedreg: " + incompletedreg + " isValid: " + isValid);  
                        }

                        String enable_Account_Lock_WrongPassword = (String) contexts.getAttribute(GeneralConf.ENABLE_ACCOUNT_LOCK_WRONGPASSWORD);
                        if ("Y".equalsIgnoreCase(enable_Account_Lock_WrongPassword)) {
                            UserData wrguserDataobj = ecomreg.getWrgPasslogin(loginName);
                            lStatus = wrguserDataobj.getlStatus();
                            attemptCount = wrguserDataobj.getWrgPsAtmpt(); 
                            String attemptLimits = (String) contexts.getAttribute(GeneralConf.AttemptLimit);
                            attemptLimit = Integer.parseInt(attemptLimits);
                            System.out.println("[ECOM-WAR] Accesslogin    attemptCount: " + attemptCount + ", attemptLimit: " + attemptLimit); 
                            // If wrong attempts >= limit, lock the account
                            if (attemptCount >= attemptLimit) {
                                System.out.println("[ECOM-WAR] Accesslogin lockWrgPasAcctMovetoPendwithAgency==> loginName: " + loginName); 
                                //ecomreg.lockWrgPasAcctMovetoPendwithAgency(loginName);
                            }
                        }

                        if (!"Y".equalsIgnoreCase(lStatus) && (verify)) {
                            UserData userDataObj = null;
                            if("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedreg)) {
                                userDataObj = getIncompletedreg; 
                            } else {
                                userDataObj = ecomreg.getActiveDetails(loginName, password,("Y".equalsIgnoreCase(enable_encryption))); //checking from ECOM 1st
                            }
                            String res2Data = "", ccmailid = null, timeZone = null, firsttimesplit = null, secondparttimeslit = null, lastpwdup = null, status = null;
                            if (userDataObj != null) {
                                ccmailid = Validation.checkNull(userDataObj.getCcMailid());
//                                 System.out.println("[ECOM-WAR] Accesslogin    >>>ccmailid>>" + ccmailid);
                                timeZone = Validation.checkNull(userDataObj.getTimezone());
//                                 System.out.println("[ECOM-WAR] Accesslogin    >>>user preferred timezone>>" + timeZone);
                                lastpwdup = Validation.checkNull(userDataObj.getLastpwdup());
                                status = Validation.checkNull(userDataObj.getStatus());

                                if (timeZone != null && timeZone.length() > 0) {
                                    String aftrTimesplit[] = timeZone.split("~~");
                                    if (aftrTimesplit.length > 0) {
                                        firsttimesplit = Validation.checkNull(aftrTimesplit[0]).trim();
                                        secondparttimeslit = Validation.checkNull(aftrTimesplit[1]).trim();
                                    }

                                }
                                session.setAttribute("USERPREFERREDTIMEZONE", firsttimesplit);
//                                 System.out.println("[ECOM-WAR] Accesslogin    firstslit" + firsttimesplit);
//                                 System.out.println("[ECOM-WAR] Accesslogin    secondslit" + secondparttimeslit);
                                System.out.println("[ECOM-WAR] Accesslogin    firstslit: " + firsttimesplit + ", secondslit: " + secondparttimeslit + ", user preferred timezone: " + timeZone + ", ccmailid: " + ccmailid);

                                res2Data = userDataObj.getResData();
                                if ("VALID".equalsIgnoreCase(res2Data)) {
                                    if("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedreg)) {
                                        userDataObj = getIncompletedreg;
                                    } else {
                                        userDataObj = ecomreg.getUserDetailsFromCustomermaster(loginName, password); //checking from LRPDB DB
                                    }
                                    System.out.println("[ECOM-WAR] Accesslogin    loginName----->" + loginName);
                                    res2Data = userDataObj.getResData();
                                    String send_Re_Activate_Request = (String) contexts.getAttribute(GeneralConf.Send_Re_Activate_Request);
                                    if (!"VALID".equalsIgnoreCase(res2Data) && "Y".equalsIgnoreCase(send_Re_Activate_Request)) {
                                        ReturnVO returnVO = ecomreg.getReqActivateActDeatails(loginName);
                                        Mailer mail = new Mailer();
                                        String useremail = returnVO.getStrToMailId();
                                        String initiatorID = "ECOM-REG";
                                        request.setAttribute("Loginaccount_expired_msg", "Your account has expired. Please await the re-validation of your account by our agency.");
                                        if (useremail != null && useremail != "") {
                                            mail.setTo(useremail);
                                            mail.setSubject(returnVO.getStrSubject());
                                            mail.setMessage(returnVO.getStrMailMsg());
                                            ecomCntrlObj.sendMailFromEjb(mail, loginName, initiatorID, client_code);
                                        }
                                    }
                                } else { 
                                    String accountStatusMessage = ecomreg.getAccountStatusMessage(loginName,enable_EcomGate_AgencyMailID);
                                    request.setAttribute("Loginaccount_msg", Validation.checkNull(accountStatusMessage).length() > 0 ? accountStatusMessage : "Invalid Username / Password");
                                    forward = "invalid";
                                }
                            }
                            String admin = "", custtype = null, loginname = "", email = "", phone = "", loginexpire = "", usercode = "", usertype = "", userName = "", preflanguage = "", cntctName = "";
                            ArrayList templateNoListObj = null;
                            if (userDataObj != null) {
                                resData = userDataObj.getResData();
                                if ("VALID".equalsIgnoreCase(resData)) {
                                    boolean checkForogotPassword = true;
                                    String forgotpasswordcheck = (String) contexts.getAttribute(GeneralConf.Temp_Password_For_ForgotPWD);
                                    if ("Y".equalsIgnoreCase(forgotpasswordcheck) && "F".equalsIgnoreCase(status)) {
                                        //forward = "resetpass";
                                        //session.setAttribute("isPasswordExpired", "Y");
                                        //session.setAttribute("PasswordValidationMsg", "Please change the temporary password.");
                                        if (DateConversion_New.calculateMinutesbetweentwoDay(lastpwdup) > 10) {
                                            session.setAttribute("IS_MENU_LOCK", "YES");
                                            forward = "invalid";
                                            resData = "invalid";
                                            checkForogotPassword = false;
                                            request.setAttribute("Temp_Password_Invalid_MSG", "Your password has expired.");
                                        }

                                    }
                                    if (checkForogotPassword) {
                                        session.setAttribute("systemid", session.getId());
                                        //
                                        loginname = userDataObj.getLoginName();
                                        loginname = Validation.checkNull(loginname);
                                        userName = userDataObj.getUserName();
                                        email = userDataObj.getEmail();
                                        email = Validation.checkNull(email);
                                        usercode = userDataObj.getUsercode();
                                        usercode = Validation.checkNull(usercode);
                                        usertype = userDataObj.getUsertype();
                                        usertype = Validation.checkNull(usertype);
                                        loginexpire = userDataObj.getLoginexpire();
                                        loginexpire = Validation.checkNull(loginexpire);
                                        preflanguage = userDataObj.getUserpreflanguage();
                                        phone = userDataObj.getPhone();
                                        admin = userDataObj.getAdmin();
                                        custtype = userDataObj.getCusttype();
                                        cntctName = Validation.checkNull(userDataObj.getContantname());

                                        //
                                        templateNoListObj = ecomCntrlObj.getTemplateNo(loginname, "T");
                                        Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                                        if (templateNoListObj != null && templateNoListObj.size() == 3) {
                                            templateno = LabelBeanConversion.labelListConversion((List<DropDownVO>) templateNoListObj.get(0));
                                            backuptemplateno = (ArrayList) templateNoListObj.get(1);
                                            tempmangetemplate = (ArrayList) templateNoListObj.get(2);  // vasanth
                                        }
                                        //Mail config set
                                        List<MailAlertVO> listAlertVO = ecomCntrlObj.getMailAlertActivities(loginname);
                                        MailAlertVO alertVO = null;
                                        Map<String, String> mailAlertMap = new HashMap<String, String>();
                                        if (listAlertVO != null && listAlertVO.size() > 0) {
                                            for (int i = 0; i < listAlertVO.size(); i++) {
                                                alertVO = listAlertVO.get(i);
                                                mailAlertMap.put(alertVO.getActivityCode(), alertVO.getMailAlert());
                                            }
                                        }
                                        session.setAttribute("USERMAILALERTSESSIONMAP", mailAlertMap);
                                        //
//                                         System.out.println("[ECOM-WAR] Accesslogin    =====loginname>>>>" + loginname);
                                        ArrayList RegMenus = new ArrayList();
                                        if("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedreg)) {
                                            if(incompletedRegMENUlist != null && incompletedRegMENUlist.length() > 0) {
                                                RegMenus = new ArrayList(Arrays.stream(incompletedRegMENUlist.split(",")).map(String::trim).collect(Collectors.toList()));
                                            }
                                        } else {
                                            RegMenus = ecomCntrlObj.getCustMenuDetailsList(loginname.trim(), "C");
                                        }
                                        System.out.println("[ECOM-WAR] Accesslogin    $$$$$$$$$>>>>>>>>RegMenus" + RegMenus + ",loginname:" + loginname);
//                                int b = 0, c = 0;
//                                for (int j = 0; j < RegMenus.size(); j++) {
//                                    if (RegMenus.contains("Booking")) {
//                                        b++;
//                                    } else if (RegMenus.contains("Dashboard")) {
//                                        c++;
//                                    }
//                                }
                                        // if (c > 0) {
                                        //  Map<String, String> dashboardMap = ecomCntrlObj.getDashboardCount(usercode, v1DataEnableStatus);
                                        //   request.setAttribute("CUSTOMERDASHBOARDCOUNT", dashboardMap);
                                        if ("Y".equalsIgnoreCase(Dash_display)) {
                                            List<UnclearedCargoVo> dashlis = adminBeanObj.getEcomUnclearedCargoDetails(usercode);
                                            int deliveredsum = dashlis.stream().filter(o -> o.getDelcontrcnt() != null).mapToInt(o -> Integer.parseInt(o.getDelcontrcnt())).sum();
                                            int remainingsum = dashlis.stream().filter(o -> o.getRemcontrcnt() != null).mapToInt(o -> Integer.parseInt(o.getRemcontrcnt())).sum();
                                            request.setAttribute("dashremainingsum", remainingsum);
                                            request.setAttribute("dashdeliveredsum", deliveredsum);
                                            request.setAttribute("UnclearedCargoDetails", dashlis);
                                            String fromdate = DateConversion_New.getCurrentDateDbFormat();
                                            String outbal = null;
                                            if ("N".equalsIgnoreCase(csfrominv)) {
                                                outbal = adminBeanObj.getDashboardOutstandingBalance(fromdate, usercode);
                                            } else if ("Y".equalsIgnoreCase(custstmtsp)) {
                                                outbal = adminBeanObj.getDashboardOutstandingBalfromcuststmtsp(fromdate, usercode);
                                            } else {
                                                Map<String, List> resmap = phase2bean.getOutstandingDetails("A", usercode, "", "", "CO");
                                                if (resmap != null) {
                                                    List<String> lis = (List) resmap.get("totalOutstandingBal");
                                                    if (lis != null && lis.size() > 0) {
                                                        outbal = lis.get(0);
                                                    }
                                                }
                                            }
                                            if (outbal != null && outbal.length() > 0) {
                                                outbal = Currencyformat.rupeeFormat(outbal);
                                            }
                                            request.setAttribute("DASH_OUTSTANDING_BALANCE", outbal);
                                        }
                                        if ("Y".equalsIgnoreCase(product_Dashboard)) {
                                            Calendar calendar = Calendar.getInstance();
                                            int year = calendar.get(Calendar.YEAR);
                                            int month = calendar.get(Calendar.MONTH) + 1;
                                            String monthyr = month + "-" + year;
                                            System.out.println("[ECOM-WAR] Accesslogin    month" + monthyr);
                                            Map<String, Map> countMap = (Map<String, Map>) ecomCntrlObj.getDashboardCounts(usercode, monthyr,Enable_Dashboard_Filter);
                                            //Map<String, String> count = (Map<String, String>) countMap.get("Booking");
                                            Map<String, String> count = bookingObj.getBookingCount(usercode, custTypeConfig, v1DataEnableStatus,"");
                                            Set s = count.entrySet();
                                            Iterator it = s.iterator();
                                            while (it.hasNext()) {
                                                Map.Entry m = (Map.Entry) it.next();
                                                String key = (String) m.getKey();
                                                String value = (String) m.getValue();
                                                if ("Draft".equalsIgnoreCase(key)) {
                                                    session.setAttribute("Draft", value);
                                                } else if ("Send to Agency".equalsIgnoreCase(key)) {
                                                    session.setAttribute("Send to Agency", value);
                                                } else if ("Approved".equalsIgnoreCase(key)) {
                                                    session.setAttribute("Approved", value);
                                                } else if ("Declined".equalsIgnoreCase(key)) {
                                                    session.setAttribute("Declined", value);
                                                } else if ("Confirmed".equalsIgnoreCase(key)) {
                                                    session.setAttribute("Confirmed", value);
                                                }
                                            }
                                            /*  bl count */
                                            Map<String, String> blcount = (Map<String, String>) countMap.get("BL");
                                            Set sval = blcount.entrySet();
                                            Iterator it1 = sval.iterator();
                                            while (it1.hasNext()) {
                                                Map.Entry mm = (Map.Entry) it1.next();
                                                String keye = (String) mm.getKey();
                                                String values = (String) mm.getValue();
                                                if ("Confirmed".equalsIgnoreCase(keye)) {
                                                    session.setAttribute("Confirmedbl", values);
                                                } else if ("Issued".equalsIgnoreCase(keye)) {
                                                    session.setAttribute("Issued", values);
                                                }
                                            }
                                            /* bl end count*/
                                            Map<String, String> sival = (Map<String, String>) countMap.get("SI");
                                            Set sicount = sival.entrySet();
                                            Iterator itsi = sicount.iterator();
                                            while (itsi.hasNext()) {
                                                Map.Entry mm = (Map.Entry) itsi.next();
                                                String keye = (String) mm.getKey();
                                                //int values = (Integer) mm.getValue();
                                                String str = String.valueOf(mm.getValue());
                                                if ("Submitted".equalsIgnoreCase(keye)) {
                                                    session.setAttribute("Submittedsi", str);
                                                } else if ("Drafts".equalsIgnoreCase(keye)) {
                                                    session.setAttribute("Draftsi", str);
                                                } else if ("csi".equalsIgnoreCase(keye)) {
                                                    session.setAttribute("SingleSI", str);
                                                } else if ("cmsi".equalsIgnoreCase(keye)) {
                                                    session.setAttribute("MultipleSI", str);
                                                } else if ("Accepted".equalsIgnoreCase(keye)) {
                                                    session.setAttribute("Accepted", str);
                                                }
                                            }
                                        }
                                        forward = "successdash";
                                        session.setAttribute("IS_ACTIVEPAGE", "dashboard");
                                        //session.setAttribute("successPage", "Dash");
                                        /* } else if (b > 0) {
                                    Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
                                    ArrayList view = null;
                                    view = bookingObj.getBookingDetail(usercode, "", "", "A", custTypeConfig, v1DataEnableStatus);
                                    if (view != null && view.size() > 0) {
                                        int len = view.size();
                                        for (int i = 0; i < len; i++) {
                                            EbookingViewAll eBooking = (EbookingViewAll) view.get(i);
                                            if (portlist.containsKey(eBooking.getOriginPortId().toUpperCase())) {
                                                eBooking.setOriginPortText(eBooking.getOriginPortId() + "-" + Validation.checkNull((String) portlist.get(Validation.checkNull(eBooking.getOriginPortId()).toUpperCase())));
                                            }
                                            if (portlist.containsKey(eBooking.getFinalPortId().toUpperCase())) {
                                                eBooking.setFinalPortText(eBooking.getFinalPortId() + "-" + Validation.checkNull((String) portlist.get(Validation.checkNull(eBooking.getFinalPortId()).toUpperCase())));
                                            }
                                            if (portlist.containsKey(eBooking.getLoadPortID().toUpperCase())) {
                                                eBooking.setLoadPortText(eBooking.getLoadPortID() + "-" + Validation.checkNull((String) portlist.get(Validation.checkNull(eBooking.getLoadPortID()).toUpperCase())));
                                            }
                                            if (portlist.containsKey(eBooking.getDischargePortId().toUpperCase())) {
                                                eBooking.setDischargePortText(eBooking.getDischargePortId() + "-" + Validation.checkNull((String) portlist.get(Validation.checkNull(eBooking.getDischargePortId()).toUpperCase())));
                                            }
                                            view.set(i, eBooking);
                                            eBooking = null;
                                        }
                                    }
                                    session.setAttribute("BookingView", view);
                                    session.setAttribute("NEWDESIGNPRODUCT", "booking");
                                    forward = "success";
                                    session.setAttribute("successPage", "Book");
                                } else {
                                    forward = "portSearch";
                                    session.setAttribute("PORTSELECT", "Schedule");
                                    session.setAttribute("successPage", "port");
                                }*/
                                    
                                        String landingPageConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.LANDING_PAGE_NEW_BOOKING));
                                        if ("Y".equalsIgnoreCase(landingPageConfig)) {
                                            forward = "bookingsummary";
                                            session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "ViewBookingAll.do?BookingRequestType=ViewBookingAll");
                                            session.setAttribute("IS_ACTIVEPAGE", "Booking");
                                        } else {
                                            forward = "dashboard";
                                            session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "Accesslogin.do?type=dashboard");
                                            session.setAttribute("IS_ACTIVEPAGE", "dashboard");
                                        }
                                        String cust_oblStauts = ecomCntrlObj.getCutomerOblStatus(usercode);
                                        if (cust_oblStauts == null) {
                                            cust_oblStauts = "N";
                                        }
                                        String apname = ecomreg.getAPNAME(usercode);
                                        session.setAttribute("apname", apname);
                                        //
                                        if ("CH".equalsIgnoreCase(preflanguage)) {
                                            preflanguage = "CHINESE";
                                            session.setAttribute("clientcode", "zh");
                                            session.setAttribute("SelectedLanguage", "ch");
                                        } else if ("EN".equalsIgnoreCase(preflanguage)) {
                                            preflanguage = "ENGLISH";
                                            session.setAttribute("clientcode", "en");
                                            session.setAttribute("SelectedLanguage", "en");
                                        } else if ("MA".equalsIgnoreCase(preflanguage)) {
                                            preflanguage = "MALAY";
                                            session.setAttribute("clientcode", "ma");
                                            session.setAttribute("SelectedLanguage", "ma");
                                        } else if ("es".equalsIgnoreCase(preflanguage)) {
                                            preflanguage = "SPANISH";
                                            session.setAttribute("clientcode", "es");
                                            session.setAttribute("SelectedLanguage", "es");
                                        } else {

                                            session.setAttribute("clientcode", "en");
                                            preflanguage = "ENGLISH";
                                            session.setAttribute("SelectedLanguage", "en");
                                        }
                                        //
                                        session.setAttribute("SESSION_LOGIN_TYPE", "U");
                                        session.setAttribute("SESSION_INCOMPLETED_REG", incompletedreg);
                                        session.setAttribute("bookallowed", "yes");
                                        session.setAttribute("cust_oblStauts", cust_oblStauts);
                                        session.setAttribute("RegMenus", RegMenus);
                                        session.setAttribute("IS_VIA_LOGIN", "YES");
                                        session.setAttribute("SESSION_LOGINNAME", loginname);
                                        session.setAttribute("SESSION_USERNAME", userName);
                                        session.setAttribute("SESSION_EMAIL", email);
                                        session.setAttribute("SESSION_USER_COUNTRY", userDataObj.getCountry());
                                        session.setAttribute("SESSION_USER_CONTACT", cntctName);

                                        session.setAttribute("SESSION_TEMPLATE_NO", templateNoListObj);
                                        session.setAttribute("SESSION_TEMPLATE_NO", templateno);
                                        session.setAttribute("SESSION_USER_PHONE", phone);
                                        System.out.println("[ECOM-WAR] Accesslogin    ccmailid>>>access login>>>" + ccmailid);
                                        if (ccmailid != null && ccmailid.length() > 0) {
                                            ccmailid = ccmailid.replaceAll("\\r\\n", "");
                                            session.setAttribute("SESSION_USER_CC_MAILID", ccmailid);
                                        } else {
                                            session.setAttribute("SESSION_USER_CC_MAILID", "NO DATA");
                                        }
                                        session.setAttribute("SESSION_BACKUP_TEMPLATE_NO", backuptemplateno);
                                        session.setAttribute("SESSION_MANAGE_TEMPLATE", tempmangetemplate); //vasanth
                                        session.setAttribute("SESSION_LOGINEXPIRE", loginexpire);
                                        session.setAttribute("SESSION_USERCODE", usercode);
                                        session.setAttribute("SESSION_CUSTCODE", usercode);
                                        session.setAttribute("SESSION_USERTYPE", usertype);
                                        session.setAttribute("SESSION_AUTHENTICATION", authObj);
                                        session.setAttribute("SESSION_LANGUAGE", preflanguage);
                                        session.setAttribute("USER_ROLL_ADMIN", admin);
                                        session.setAttribute("SESSION_USER_CUST_TYPE", custtype);
                                        if (custtype != null && custtype.contains("FC")) {
                                            session.setAttribute("SESSION_USER_FURIT_CUSTOMER", "Y");
                                        }
                                        //session.setAttribute("IS_MENU_LOCK", "NO");
                                        //session.setAttribute("AgencyAdminMap", "user");
                                        String res = request.getRequestURL().toString();
                                        String resulturl[] = res.split("/" + request.getContextPath());
                                        session.setAttribute("oblprintUrl", resulturl[0]);
                                        //get customer SpecialCustomerGroup
                                        String splcustgroup = Validation.checkNull(ecomCntrlObj.getCustomerSpecialCustomerGroup(usercode));
                                        session.setAttribute("SESSION_SPLCUSTGRP", splcustgroup);
                                        String insuranceaccess = Validation.checkNull(ecomCntrlObj.checkInsuranceAccess(usercode));
                                        session.setAttribute("SESSION_InsuranceAccess", insuranceaccess);
                                        //
                                        if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_Ecom_Notification))) {
                                            session.setAttribute("ECOM_NOTIFICATION_DETAILS", ecomCntrlObj.getEcomNotificationDetails(usercode, loginname));
                                        }
                                        ecomreg.updatelogindate(loginname);
                                        if (Validation.checkNull((String) session.getAttribute("websitetrackno")).length() > 0) {
                                            forward = "welcome";
                                        }
                                        //
                                        if ("Y".equalsIgnoreCase(Enable_Password_Validation)) {
                                            Map pwdmap = new HashMap();
                                            //lastpwdup = "2021-03-05 19:41:31.867";
                                            pwdmap.put("username", loginName);
                                            pwdmap.put("password", password);
                                            pwdmap.put("lastpwdup", lastpwdup);
                                            pwdmap.put("isExpirycheck", "Y");
                                            pwdmap = CommonPasswordValidation.validatePassword(pwdmap, Password_Validation_API_URL);
                                            if (pwdmap != null) {
                                                System.out.println("[ECOM-WAR] Accesslogin    pwdmap==>" + pwdmap.get("valid"));
                                                if ((boolean) pwdmap.get("valid")) {
                                                    System.out.println("[ECOM-WAR] Accesslogin    Password Valid");
                                                } else {
                                                    forward = "resetpass";
                                                    session.setAttribute("isPasswordExpired", "Y");
                                                    session.setAttribute("PasswordValidationMsg", pwdmap.get("pwdmsg").toString());
                                                    PasswordExpiredOTPMail.sendOTP(request);
//                                                     System.out.println("[ECOM-WAR] Accesslogin    pwdmsg==> " + pwdmap.get("pwdmsg"));
//                                                     System.out.println("[ECOM-WAR] Accesslogin    jjjjjjjjjjjjjj-> " + request.getAttribute("PasswordValidationMsg"));
                                                    System.out.println("[ECOM-WAR] Accesslogin    pwdmsg ==> " + pwdmap.get("pwdmsg") + ", PasswordValidationMsg ==> " + request.getAttribute("PasswordValidationMsg"));

                                                }
                                            }
                                        }
                                        if ("Y".equalsIgnoreCase(forgotpasswordcheck) && "F".equalsIgnoreCase(status)) {
                                            forward = "resetpass";
                                            session.setAttribute("isPasswordExpired", "Y");
                                            session.setAttribute("PasswordValidationMsg", "Please change the temporary password.");
                                        }
                                        ecomreg.resetWrgPasAtmp(loginName);
                                    }
                                } else {
                                    session.setAttribute("IS_MENU_LOCK", "YES");
                                    forward = "invalid";
                                    if ("Y".equalsIgnoreCase(enable_Invalid_PasswordAttempts)) {
                                        attemptCount += 1;
                                        wrgPasRestrict(loginName, request, attemptLimit, attemptCount);

                                    }
                                    if ("Y".equalsIgnoreCase(enable_Account_Lock_WrongPassword)) {
                                        attemptCount += 1;
                                        wrgPasLocktoPendwithAgency(loginName, request, attemptLimit, attemptCount);

                                    }
                                    if ("Y".equalsIgnoreCase(logincaptcha)) {

                                        passattemptCount = passattemptCount + 1;
                                        session.setAttribute("InvalidpassattemptCount", passattemptCount);
                                    }
                                }
                            }
                        } else {
                            forward = "invalid";
                            resData = "invalid";
                            if (verify) {
                                if ("Y".equalsIgnoreCase(enable_Invalid_PasswordAttempts)) {
                                request.setAttribute("Temp_Password_Invalid_MSG", "Account is locked. Please try again later.");
                                }
                                String accountStatusMessage = ecomreg.getAccountlockedMessage(loginName);
                                request.setAttribute("Loginaccount_msg", Validation.checkNull(accountStatusMessage).length() > 0 ? accountStatusMessage : "Account is locked. Please try again later.");
                                
                            }
                        }
                        
                        request.setAttribute("LOGINRESPONSE_DATA", resData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//         System.out.println("[ECOM-WAR] Accesslogin    forward==>" + forward);
        System.out.println("[ECOM-WAR] Accesslogin    resData==>" + resData + ",forward:" + forward);
        return mapping.findForward(forward);
    }

    public void wrgPasRestrict(String loginName, HttpServletRequest request, int attemptLimit, int attemptCount) {
        try {
            ecomreg.updateWrgPasAtmp(loginName);
            if (attemptCount == attemptLimit) {
                ecomreg.lockWrgPasAcct(loginName);
                request.setAttribute("Temp_Password_Invalid_MSG", "Login attempts exceeded. Account temporarily locked.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void wrgPasLocktoPendwithAgency(String loginName, HttpServletRequest request, int attemptLimit, int attemptCount) {
        try {
            ecomreg.updateWrgPasAtmp(loginName);
            if (attemptCount == attemptLimit) {
                ecomreg.lockWrgPasAcctMovetoPendwithAgency(loginName);
                String accountStatusMessage = ecomreg.getAccountlockedMessage(loginName);
                request.setAttribute("Loginaccount_msg", Validation.checkNull(accountStatusMessage).length() > 0 ? accountStatusMessage : "Account is locked. Please try again later.");
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
