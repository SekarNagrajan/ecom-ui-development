/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.CommonPasswordValidation;
import net.ecom.common.CryptoLibrary;
import net.ecom.common.Currencyformat;
import net.ecom.common.GeneralConf;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.Mailer;
import net.ecom.common.PasswordExpiredOTPMail;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.LoginForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.MailAlertVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.UnclearedCargoVo;
import net.ecom.vo.UserData;
import org.infinispan.manager.CacheContainer;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "LoginAjax", urlPatterns = {"/LoginAjax"})
public class LoginAjax extends HttpServlet {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();
    EcomAdminCntrlBeanRemote adminBeanObj = EJBLookUp.getAdminBeanLookUp();
    EcomPhase2BeanRemote phase2bean = EJBLookUp.getPhase2BeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    CryptoLibrary crypt = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        doPost(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String forward = "invalid";
        String resData = "";
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        crypt = new CryptoLibrary();
        String loginName = null;
        String password = null;
        String incompletedreg = "";
        ArrayList templateno = new ArrayList();
        ArrayList backuptemplateno = new ArrayList();
        ArrayList tempmangetemplate = new ArrayList();
        String Dash_display = (String) request.getServletContext().getAttribute(GeneralConf.DASHBOARD_DISPLAY);
        String csfrominv = (String) request.getServletContext().getAttribute(GeneralConf.CSFROMINV);
        String Enable_Password_Validation = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Password_Validation);
        String Password_Validation_API_URL = (String) request.getServletContext().getAttribute(GeneralConf.Password_Validation_API_URL);
        String product_Dashboard = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Product_Dashboard);
        String custTypeConfig = Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.APBPCONFIGBACKEND));
        String enable_encryption = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Login_Password_Encryption);
        String Enable_Dashboard_Filter = (String) request.getServletContext().getAttribute(GeneralConf.Enable_DashBoard_Filter);
        String custstmtsp = (String) request.getServletContext().getAttribute(GeneralConf.USE_CUSTOMETSTATEMENT_SP);
        String allowIncompletedRegToLogin = (String) request.getServletContext().getAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN);
        String incompletedRegMENUlist = (String) request.getServletContext().getAttribute(GeneralConf.INCOMPLETED_REG_MENU_LIST);
        try {
            loginName = request.getParameter("userName");
            loginName = Validation.checkNull(loginName);
            System.out.println("[ECOM-WAR] LoginAjax          login name in access login ----------- " + loginName);
            password = request.getParameter("password");
            password = Validation.checkNull(password);
            //  styleCss = loginObj.getStyleName();
            //language = loginObj.getLanguage();
            ServletContext contexts = request.getServletContext();
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);

            if ((loginName == null) || (loginName.length() == 0) || (password == null) || (password.length() == 0)) {
                forward = "invalid";
            } else {
                ArrayList userDataOb = (ArrayList) ecomreg.getAgencyLoginDetEncrypt(loginName, password, ("Y".equalsIgnoreCase(enable_encryption)));
                password = crypt.encrypt(password);
                //    styleCss = Validation.checkNull(styleCss);
                //   language = Validation.checkNull(language);
                if ("V".equalsIgnoreCase(userDataOb.get(1).toString())) {
                    ArrayList userDataObj = ecomreg.getvendormodules(userDataOb.get(0).toString());
                    ArrayList menulist = ecomreg.getvendormoduleslist(loginName);
                    System.out.println("[ECOM-WAR] LoginAjax          menulist===> " + menulist);
                    session.setAttribute("SESSION_LOGIN_TYPE", "V");
                    session.setAttribute("VENDOR_MAIN_MENU", menulist.get(0));
                    session.setAttribute("VENDOR_SUB_MENU", menulist.get(1));
                    menulist = (ArrayList) menulist.get(0);
                    if (menulist.size() > 0) {
                        session.setAttribute("NEWDESIGNAGENCY", "Yes");
                        forward = "AdminAccess.do?type=specialprivi";
                        session.setAttribute("IS_ACTIVEPAGE", "Vendormodulesmapping");
                        session.setAttribute("systemid", session.getId());
                    }
                    session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "AdminAccess.do?type=specialprivi");
                    session.setAttribute("IS_VIA_LOGIN", "NO");
                    session.setAttribute("vendormenuList", userDataObj);
                    session.setAttribute("SESSION_USERNAME", userDataOb.get(0).toString());
                    session.setAttribute("SESSION_USERCODE", "vendor");
                    System.out.println("[ECOM-WAR] LoginAjax          agency id>>>" + userDataOb.get(2).toString());
                    session.setAttribute("SESSION_AGENCY_USERID", userDataOb.get(2).toString());

                    session.setAttribute("clientcode", "en");
                    session.setAttribute("SelectedLanguage", "en");

                } // phase 2
                else {
                    session.setAttribute("NEWDESIGNAGENCY", "NO");
                    String enable_Invalid_PasswordAttempts = (String) contexts.getAttribute(GeneralConf.Enable_Invalid_PasswordAttempts);
                    String lStatus = "N";
                    int attemptCount = 0;
                    int attemptLimit = 0;
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

                    if (!"Y".equalsIgnoreCase(lStatus)) {
                        UserData userDataObj = null;
                        if("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedreg)) {
                            userDataObj = getIncompletedreg;
                        } else {
                            userDataObj = ecomreg.getActiveDetails(loginName, password, ("Y".equalsIgnoreCase(enable_encryption))); //checking from ECOM 1st
                        }
                        String res2Data = "", ccmailid = null, timeZone = null, firsttimesplit = null, secondparttimeslit = null, lastpwdup = null, status = null;
                        if (userDataObj != null) {
                            ccmailid = Validation.checkNull(userDataObj.getCcMailid());
//                             System.out.println("[ECOM-WAR] LoginAjax          >>>ccmailid>>" + ccmailid);
                            timeZone = Validation.checkNull(userDataObj.getTimezone());
                            System.out.println("[ECOM-WAR] LoginAjax          >>>user preferred timezone>>" + timeZone + ",ccmailid:" + ccmailid);
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
//                             System.out.println("[ECOM-WAR] LoginAjax          firstslit" + firsttimesplit);
                            System.out.println("[ECOM-WAR] LoginAjax          secondslit" + secondparttimeslit + ",firsttimesplit:" + firsttimesplit);

                            res2Data = userDataObj.getResData();
                            if ("VALID".equalsIgnoreCase(res2Data)) {
                                if("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedreg)) {
                                    userDataObj = getIncompletedreg;
                                } else {
                                    userDataObj = ecomreg.getUserDetailsFromCustomermaster(loginName, password); //checking from LRPDB DB
                                }
                                System.out.println("[ECOM-WAR] LoginAjax          loginName----->" + loginName);
                                res2Data = userDataObj.getResData();
                                String send_Re_Activate_Request = (String) contexts.getAttribute(GeneralConf.Send_Re_Activate_Request);
                                String client_code = (String) contexts.getAttribute("client_code");
                                if (!"VALID".equalsIgnoreCase(res2Data) && "Y".equalsIgnoreCase(send_Re_Activate_Request)) {
                                    ReturnVO returnVO = ecomreg.getReqActivateActDeatails(loginName);
                                    Mailer mail = new Mailer();
                                    String useremail = returnVO.getStrToMailId();
                                    String initiatorID = "ECOM-REG";
                                    forward = "inactivated";
                                    if (useremail != null && useremail != "") {
                                        mail.setTo(useremail);
                                        mail.setSubject(returnVO.getStrSubject());
                                        mail.setMessage(returnVO.getStrMailMsg());
                                        ecomCntrlObj.sendMailFromEjb(mail, loginName, initiatorID, client_code);
                                    }
                                }

                            } else {
                                forward = "invalid";
                            }
                        }
                        String admin = "", custtype = null, loginname = "", email = "", phone = "", loginexpire = "", usercode = "", usertype = "", userName = "", preflanguage = "";
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
                                        forward = "expired";
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
//                                     System.out.println("[ECOM-WAR] LoginAjax          =====loginname>>>>" + loginname);
                                    ArrayList RegMenus = new ArrayList();
                                    if("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedreg)) {
                                        if(incompletedRegMENUlist != null && incompletedRegMENUlist.length() > 0) {
                                            RegMenus = new ArrayList(Arrays.stream(incompletedRegMENUlist.split(",")).map(String::trim).collect(Collectors.toList()));
                                        }
                                    } else {
                                        RegMenus = ecomCntrlObj.getCustMenuDetailsList(loginname.trim(), "C");
                                    }
                                    System.out.println("[ECOM-WAR] LoginAjax          $$$$$$$$$>>>>>>>>RegMenus" + RegMenus);
                                    //      Map<String, String> dashboardMap = ecomCntrlObj.getDashboardCount(usercode, v1DataEnableStatus);
                                    //      request.setAttribute("CUSTOMERDASHBOARDCOUNT", dashboardMap);
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
                                        System.out.println("[ECOM-WAR] LoginAjax          month" + monthyr);
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
                                    // forward = "successdash";
                                    // Check Landing Page Configuration
                                    String landingPageConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.LANDING_PAGE_NEW_BOOKING));
                                    if ("Y".equalsIgnoreCase(landingPageConfig)) {
                                        session.setAttribute("IS_ACTIVEPAGE", "Booking");
                                        forward = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
                                        session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "ViewBookingAll.do?BookingRequestType=ViewBookingAll");
                                    } else {
                                        // Default to Dashboard
                                        session.setAttribute("IS_ACTIVEPAGE", "dashboard");
                                        forward = "Accesslogin.do?type=dashboard";
                                        session.setAttribute("SESSION_REDIRECT_STARTING_PAGE", "Accesslogin.do?type=dashboard");
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
                                    }
                                    if ("MA".equalsIgnoreCase(preflanguage)) {
                                        preflanguage = "MALAY";
                                        session.setAttribute("clientcode", "ma");
                                        session.setAttribute("SelectedLanguage", "ma");
                                    }
                                    if ("ES".equalsIgnoreCase(preflanguage)) {
                                        preflanguage = "SPANISH";
                                        session.setAttribute("clientcode", "es");
                                        session.setAttribute("SelectedLanguage", "es");
                                    } else if ("EN".equalsIgnoreCase(preflanguage)) {
                                        preflanguage = "ENGLISH";
                                        session.setAttribute("clientcode", "en");
                                        session.setAttribute("SelectedLanguage", "en");
                                    } else {
                                        session.setAttribute("clientcode", "en");
                                        preflanguage = "ENGLISH";
                                        session.setAttribute("SelectedLanguage", "en");
                                    }
                                    //
                                    session.setAttribute("SESSION_LOGIN_TYPE", "U");
                                    session.setAttribute("bookallowed", "yes");
                                    session.setAttribute("cust_oblStauts", cust_oblStauts);
                                    session.setAttribute("RegMenus", RegMenus);
                                    session.setAttribute("IS_VIA_LOGIN", "YES");
                                    session.setAttribute("SESSION_LOGINNAME", loginname);
                                    session.setAttribute("SESSION_INCOMPLETED_REG", incompletedreg);
                                    session.setAttribute("SESSION_USERNAME", userName);
                                    session.setAttribute("SESSION_EMAIL", email);
                                    session.setAttribute("SESSION_USER_COUNTRY", userDataObj.getCountry());
                                    session.setAttribute("SESSION_TEMPLATE_NO", templateNoListObj);
                                    session.setAttribute("SESSION_TEMPLATE_NO", templateno);
                                    session.setAttribute("SESSION_USER_PHONE", phone);
                                    System.out.println("[ECOM-WAR] LoginAjax          ccmailid>>>access login>>>" + ccmailid);
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

                                    if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_Ecom_Notification))) {

                                        session.setAttribute("ECOM_NOTIFICATION_DETAILS", ecomCntrlObj.getEcomNotificationDetails(usercode, loginname));
                                    }
                                    ecomreg.updatelogindate(loginname);
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
                                            System.out.println("[ECOM-WAR] LoginAjax          pwdmap==>" + pwdmap.get("valid"));
                                            if ((boolean) pwdmap.get("valid")) {
                                                System.out.println("[ECOM-WAR] LoginAjax          Password Valid");
                                            } else {
                                                forward = "Accesslogin.do?type=reset";
                                                session.setAttribute("isPasswordExpired", "Y");
                                                session.setAttribute("PasswordValidationMsg", pwdmap.get("pwdmsg").toString());
                                                PasswordExpiredOTPMail.sendOTP(request);
//                                                 System.out.println("[ECOM-WAR] LoginAjax          pwdmsg==> " + pwdmap.get("pwdmsg"));
//                                                 System.out.println("[ECOM-WAR] LoginAjax          jjjjjjjjjjjjjj-> " + request.getAttribute("PasswordValidationMsg"));
                                                System.out.println("[ECOM-WAR] LoginAjax          pwdmsg==> " + pwdmap.get("pwdmsg") + ", jjjjjjjjjjjjjj-> " + request.getAttribute("PasswordValidationMsg"));

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
                                if (!"inactivated".equalsIgnoreCase(forward)) {
                                    forward = "invalid";
                                    if ("Y".equalsIgnoreCase(enable_Invalid_PasswordAttempts)) {
                                        attemptCount += 1;
                                        wrgPasRestrict(loginName, request, attemptLimit, attemptCount);
                                        if (attemptCount >= attemptLimit) {
                                            forward = "accountLocked";
                                        }
                                    }
                                    if ("Y".equalsIgnoreCase(enable_Account_Lock_WrongPassword)) {
                                        attemptCount += 1;
                                        wrgPasLocktoPendwithAgency(loginName, request, attemptLimit, attemptCount);
                                        if (attemptCount >= attemptLimit) {
                                           /*String accountStatusMessage = ecomreg.getAccountlockedMessage(loginName);
                                            System.out.println("[ECOM-WAR] LoginAjax          accountStatusMessage==>" + accountStatusMessage);
                                            session.setAttribute("Loginaccount_locked_msg", Validation.checkNull(accountStatusMessage).length() > 0 ? accountStatusMessage : "Account is locked. Please try again later."); */ 
                                            forward = "AccountLockedContactAgency";  
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if ("Y".equalsIgnoreCase(enable_Invalid_PasswordAttempts)) {
                        forward = "alreadyAccountLocked";
                        } else {
                          /*  String accountStatusMessage = ecomreg.getAccountlockedMessage(loginName);
                            System.out.println("[ECOM-WAR] LoginAjax          getAccountlockedMessage          accountStatusMessage==>" + accountStatusMessage);
                            session.setAttribute("Loginaccount_locked_msg", Validation.checkNull(accountStatusMessage).length() > 0 ? accountStatusMessage : "Account is locked. Please try again later.");
                            System.out.println("[ECOM-WAR] LoginAjax   getAttribute       accountStatusMessage==>" + session.getAttribute("Loginaccount_locked_msg"));
                            */
                            forward = "AccountLockedContactAgency";  
                        }
                        resData = "invalid";
                    }
                    request.setAttribute("LOGINRESPONSE_DATA", resData);
                }
            }
        } catch (Exception e) {
            forward = "error";
            e.printStackTrace();
        }
        out.print(forward);
    }

    public void wrgPasRestrict(String loginName, HttpServletRequest request, int attemptLimit, int attemptCount) {
        try {
            ecomreg.updateWrgPasAtmp(loginName);
            if (attemptLimit == attemptCount) {
                ecomreg.lockWrgPasAcct(loginName);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
