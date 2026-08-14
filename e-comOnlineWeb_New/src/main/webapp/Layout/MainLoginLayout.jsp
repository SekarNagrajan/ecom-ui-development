<%@page contentType="text/html" errorPage="../SessionExpired.jsp" pageEncoding="UTF-8"%>
<%@page import="net.ecom.common.GeneralConf,net.ecom.common.Validation,java.util.List,java.util.Iterator,java.util.Map,java.util.LinkedHashMap,java.util.TreeMap,net.ecom.vo.HotlineContactsVO,java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib prefix="tiles" uri="/WEB-INF/struts-tiles.tld"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<!DOCTYPE html>
<%
    response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
    response.addHeader("X-XSS-Protection", "1; mode=block");
    response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
    response.setHeader("Pragma", "no-cache"); //HTTP 1.0
    response.setDateHeader("Expires", 0); //prevents caching at the proxy server

    session.setAttribute("OBLPRINTACCESS", "NO");
    response.addHeader("X-FRAME-OPTIONS", "DENY");
    response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");

    long jsdate = new java.util.Date().getTime();
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String logo_name = (String) getServletContext().getAttribute(GeneralConf.Logo_Name);
    String logo = imagepath + logo_name;
    String icoimage = imagepath + "ico.ico";
    //String loginForMenu = (String) session.getAttribute("LOGINFORMENU");
    //session.setAttribute("LOGINFORMENU", null);
    //System.out.println("[ECOM-WAR] MainLoginLayout  ####- loginForMenu -=-=-=-=====> " + loginForMenu);
    String selectedlan = (String) session.getAttribute("SelectedLanguage");
    System.out.println("[ECOM-WAR] MainLoginLayout  selectedlangis" + selectedlan);
    String clientcode = (String) session.getAttribute("clientcode");
    String isViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
    String strUserName = (String) session.getAttribute("SESSION_USERNAME");
    String logincustcode = (String) session.getAttribute("SESSION_CUSTCODE");
    String user = (String) session.getAttribute("sessionAdmin");
    String logintype = (String) session.getAttribute("SESSION_LOGIN_TYPE");
    String user_roll_admin = (String) session.getAttribute("USER_ROLL_ADMIN");
    String rateRegMenu = (String) getServletContext().getAttribute(GeneralConf.RATEREQMENU);
    String enableStripePaymentMenu  = (String) getServletContext().getAttribute(GeneralConf.Enable_Stripe_Payment);
    String enableNGeniusPaymentMenu = (String) getServletContext().getAttribute(GeneralConf.Enable_Ngenius_Payment);
    String liveChatId = (String) getServletContext().getAttribute(GeneralConf.LIVE_CHAT_ID);
    String hideMenuIcon = (String) getServletContext().getAttribute(GeneralConf.HIDE_MENU_ICON);
    String hideMaskPortRecords = (String) getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_RECORDS);
    String maskPortListGson = (String) getServletContext().getAttribute("MaskPortListGson");
    String allowIncompletedRegToLogin = (String) getServletContext().getAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN);
    String incompletedReg = (String) session.getAttribute("SESSION_INCOMPLETED_REG");
   
    /* if ("V".equalsIgnoreCase(logintype)) {
        strUserName = "Admin";
    }*/
    if (strUserName == null || strUserName.length() == 0) {
        strUserName = "Admin";
    } 
    String result = (String) request.getAttribute("LOGINRESPONSE_DATA");
    System.out.println("[ECOM-WAR] MainLoginLayout  logintype====> " + logintype + " logintype.length()====> " + logintype.length());
    String loginemailid = (String) session.getAttribute("SESSION_EMAIL");
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
    TreeMap HotlineContacts = (TreeMap) getServletContext().getAttribute("HotlineContacts");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String isPasswordExpired = (String) session.getAttribute("isPasswordExpired");
    System.out.println("[ECOM-WAR] MainLoginLayout  isPasswordExpired==> " + isPasswordExpired);
    String passnote = Validation.checkNull((String) getServletContext().getAttribute("PasswordValidationNote"));
    String enablenotification = (String) getServletContext().getAttribute(GeneralConf.Enable_Ecom_Notification);
    String enablesessiontimeout = (String) getServletContext().getAttribute(GeneralConf.Enable_Session_Timeout);
    String enableFileBackendValidation = (String) getServletContext().getAttribute(GeneralConf.EnableFileBackendValidation);
    String ecomFileExt = (String) getServletContext().getAttribute(GeneralConf.EcomFileExtension);
    String ecomFileUpdSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
    String defaultloginopen = (String) request.getAttribute("defaultloginopen");
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String hideFullScreenIcon = (String) getServletContext().getAttribute(GeneralConf.HIDE_FULLSCREEN_ICON);
    String hideLangOption = (String) getServletContext().getAttribute(GeneralConf.HIDE_LANG_OPTION);
    String logincaptcha = (String) getServletContext().getAttribute(GeneralConf.ENABLE_LOGIN_CAPTCHA);
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    Object attemptCountObj = session.getAttribute("InvalidpassattemptCount");
    String loginaccount_msg = (String) request.getAttribute("Loginaccount_msg");
    loginaccount_msg=  loginaccount_msg != null && loginaccount_msg.trim().length() > 0 ? loginaccount_msg : "Invalid Username / Password";
    System.out.println("[ECOM-WAR] MainLoginLayout  loginaccount_msg==> " + loginaccount_msg);
    int passattemptCount = (attemptCountObj != null) ? (int) attemptCountObj : 0;
    String enable_encryption = (String) getServletContext().getAttribute(GeneralConf.Enable_Login_Password_Encryption);
    String createQuote = (String) getServletContext().getAttribute(GeneralConf.Enable_Create_Quote);
    String quoteMenuURL=("Y".equalsIgnoreCase(createQuote) ? "Accesslogin.do?type=quote" :"Accesslogin.do?type=raterequest");
    String disableCommonValidation = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Disable_Common_Validation));
    String cookieMsg = (String) getServletContext().getAttribute(GeneralConf.COOKIE_MSG); 
    String serviceLoginName = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.SERVICE_LOGIN_NAME));
    boolean useServiceLoginName = serviceLoginName.length() > 0;
%>
<html lang="en">
    <head>
        <title><%=page_title%></title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-XSS-Protection" content="1; mode=block">
        <link rel="shortcut icon" type="image/x-icon" href="<%=icoimage%>?<%=jsdate%>"/>


        <link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i%7COpen+Sans:300,300i,400,400i,600,600i,700,700i" rel="stylesheet">

        <!--  <link href="../assets/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" /> -->

        <!-- BEGIN: Vendor CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/vendors.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/extensions/unslider.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/weather-icons/climacons.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/fonts/meteocons/style.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/tables/datatable/datatables.min.css">

        <!-- END: Vendor CSS-->

        <!-- BEGIN: Theme CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap-extended.min.css" />
        <!--        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/colors.min.css" />-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/components.min.css" />
        <!-- END: Theme CSS-->

        <!-- BEGIN: Page CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/core/menu/menu-types/vertical-menu-modern.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/core/menu/menu-types/vertical-overlay-menu.min.css">
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/core/colors/palette-gradient.min.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/fonts/simple-line-icons/style.min.css" />

        <!-- END: Page CSS-->


        <!-- BEGIN: Custom CSS-->
        <link type="text/css" href="Design/app-assets/fonts/font-awesome/css/font-awesome.min.css" rel="stylesheet"  />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/plugins/forms/wizard.min.css">
        <link rel="stylesheet" type="text/css" href="Design/assets/css/style.css?<%=jsdate%>" />
        <link rel="stylesheet" type="text/css" href="Design/assets/css/ecom.css?<%=jsdate%>" />
        <!--        <link rel="stylesheet" type="text/css" href="Design/assets/css/endless-style.css" />-->
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/autocomplete/typeahead.bundle.css"/>
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/datepicker/css/bootstrap-datepicker3.css" />
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/colorpicker/bootstrap-colorpicker.min.css"/>
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/datepicker/css/bootstrap-datetimepicker.min.css" />


        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/fancybox/jquery.fancybox.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/plugins/forms/switch.min.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/forms/toggle/switchery.min.css">
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/forms/icheck/icheck.css">
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/extensions/toastr.css">
        <link rel="stylesheet" type="text/css" href="Design/assets/plugins/timepicker/css/timepicker.min.css">
        <link rel="stylesheet" type="text/css" href="Design/assets/plugins/jExcel/css/jexcel.css">
        <link rel="stylesheet" type="text/css" href="Design/assets/plugins/jExcel/css/jsuites.css">
        <link type="text/css" href="Design/assets/css/login.css?<%=jsdate%>" rel="stylesheet"/>
        <!-- END: Custom CSS-->

        <!-- Load vendors.min.js first (contains jQuery and other libraries) -->
        <script type="text/javascript" src="Design/app-assets/vendors/js/vendors.min.js"></script>
        
        <!-- intl-tel-input CSS -->
        <link rel="stylesheet" href="Design/assets/plugins/countrynumber/intlTelInput.css">
         <script src="Design/assets/plugins/countrynumber/intlTelInput.min.js"></script>
        <script src="Design/assets/plugins/countrynumber/utils.js"></script>

        <!-- BEGIN: Page Vendor JS-->
        <!--        <script type="text/javascript" src="Design/app-assets/vendors/js/charts/raphael-min.js"></script>-->
        <!--        <script type="text/javascript" src="Design/app-assets/vendors/js/charts/morris.min.js"></script>-->
        <script type="text/javascript" src="Design/app-assets/vendors/js/extensions/unslider-min.js"></script>

        <!-- END: Page Vendor JS-->
        <%if ("Y".equalsIgnoreCase(logincaptcha) && (passattemptCount >= 3)) {%>
        <style>
            .login_box {
                margin-top: 50px;
            }
        </style>
        <%}%>
        <script type="text/javascript">
            $(document).ready(function () {
                disableBack();
                window.onpageshow = function (evt) {
                    if (evt.persisted)
                        disableBack();
                }
            });
            function disableBack() {
                window.history.forward(1);
                window.history.forward(-1);
            }
        </script>

        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/datatable/datatables.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/datatable/dataTables.buttons.min.js"></script>
        <script type="text/javascript" src="Design/assets/js/datesorting.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/buttons.flash.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/jszip.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/pdfmake.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/vfs_fonts.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/buttons.html5.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/buttons.print.min.js"></script>

        <!--        <script src="Design/app-assets/js/scripts/tables/datatables/datatable-advanced.min.js"></script>-->

        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/jquery.validate.min.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/additional-methods.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/assets/plugins/autocomplete/typeahead.bundle.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/datepicker/js/moment.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/fancybox/jquery.fancybox.min.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/NicEdit/nicEdit.js"></script>
        <script type="text/javascript" src="js/CommonValidation.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/extensions/jquery.steps.min.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/extensions/toastr.min.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/timepicker/js/timepicker.min.js?<%=jsno%>"></script>

        <script type="text/javascript" src="Design/assets/plugins/jExcel/js/jexcel.js?<%=jsno%>"></script>
        <script type="text/javascript" src="Design/assets/plugins/jExcel/js/jsuites.js?<%=jsno%>"></script>
        <script type="text/javascript" src="Design/assets/plugins/mapbox/mapbox-gl-loader.js?<%=jsno%>"></script>
        <script type="text/javascript" src="js/LiveMap.js?<%=jsno%>"></script>
        <!--tryrecaptcha -->

        <script src="https://www.recaptcha.net/recaptcha/api.js"></script>


        <!--        <script type="text/javascript" src="Design/assets/plugins/datepicker/js/bootstrap-datetimepicker.min.js"></script>-->
        <!--        <script type="text/javascript" src="Design/app-assets/js/scripts/ui/scrollable.min.js"></script>-->
        <%
            if ("en".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgsen.js?<%=jsdate%>"></script>

        <%            }%>
        <%
            if ("zh".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgsch.js?<%=jsdate%>"></script>

        <%            }%>
        <%
            if ("ma".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgsma.js?<%=jsdate%>"></script>

        <%            }%>
        <%
            if ("es".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgses.js?<%=jsdate%>"></script>

        <%            }%>

    </head>
    <body class="vertical-layout vertical-overlay-menu 2-columns fixed-navbar" data-open="click" data-menu="vertical-overlay-menu" data-col="2-columns">
        <input type="hidden" id="disableCommonValidation" value="<%=disableCommonValidation%>">
        <!--    <body class="vertical-layout vertical-menu-modern 2-columns fixed-navbar"  data-menu="vertical-menu-modern" data-col="2-columns">-->
        <div id="processLoad" >
            <div class="page-loader-overlay-img page-loader-overlay-fixed" style="width: auto; height: auto; display: block;"></div>
            <div class="page-loader-overlay">
                <table>
                    <tbody style="text-align: center;">
                        <tr>
                            <td>
                                <div class="spinner-border text-info" style="width: 5rem; height: 5rem;" role="status">
                                    <span class="sr-only">Loading...</span>
                                </div>
                            </td>
                        </tr>
                        <tr style="text-align: center;">
                            <td style="text-align: center;font-size: 25px;">
                                Please wait
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- BEGIN: Header-->
        <nav class="header-navbar navbar-expand-md navbar navbar-with-menu fixed-top navbar-semi-dark navbar-shadow navbar-brand-center">
            <!--        <nav class="header-navbar navbar-expand-lg navbar navbar-with-menu fixed-top navbar-semi-dark navbar-shadow">-->
            <div class="navbar-wrapper">

                <!--                <div class="navbar-header">
                                    <ul class="nav navbar-nav flex-row">
                
                                        <li class="nav-item mobile-menu d-lg-none mr-auto"><a class="nav-link nav-menu-main menu-toggle hidden-xs" href="#"><i class="ft-menu font-large-1"></i></a></li>
                
                                        <li class="nav-item mr-auto"><a class="navbar-brand" href="Welcome.do"> <img class="brand-logo" alt="SVM logo" src="<%=logo%>?<%=jsdate%>"></a></li>
                
                                                                <li class="nav-item d-none d-lg-block nav-toggle"><a class="nav-link modern-nav-toggle pr-0" data-toggle="collapse"><i class="toggle-icon ft-toggle-right font-medium-3 white" data-ticon="ft-toggle-right"></i></a></li>
                                        <li class="nav-item d-lg-none"><a class="nav-link open-navbar-container" data-toggle="collapse" data-target="#navbar-mobile"><i class="fa fa-ellipsis-v"></i></a></li>
                
                                    </ul>
                
                                </div>-->
                <div class="navbar-header">
                    <ul class="nav navbar-nav flex-row">
                        <%if (!"Y".equalsIgnoreCase(hideMenuIcon)) {%> 
                        <li class="nav-item mobile-menu d-md-none mr-auto"><a class="nav-link nav-menu-main menu-toggle hidden-xs menu-toggle" href="#" id="menuenable"><i class="ft-menu font-large-1"></i></a></li>
                        <%}%>
                        <li class="nav-item d-md-none mr-auto"><a class="navbar-brand hidden-xs" href="Welcome.do"> <img class="brand-logo" alt="logo" src="<%=logo%>?<%=jsdate%>"></a></li>
                        <%if (!"Y".equalsIgnoreCase(hideMenuIcon)) {%>   
                        <li class="nav-item d-md-none"><a class="nav-link open-navbar-container" data-toggle="collapse" data-target="#navbar-mobile"><i class="fa fa-ellipsis-v"></i></a></li>
                        <%}%>
                    </ul>

                </div>

                <div class="navbar-container content">
                    <div class="collapse navbar-collapse" id="navbar-mobile">
                        <!--                        <ul class="nav navbar-nav mr-auto float-left">
                        
                        
                        
                                                     <div class="logo-wrapper"> <a href=""> <img src="../../../app-assets/images/svm_logo.png" alt=""> </a> </div> 
                        
                        
                                                </ul>-->
                        <ul class="nav navbar-nav mr-auto float-left">

                            <%if (!"Y".equalsIgnoreCase(hideMenuIcon)) {%> 
                                <li class="nav-item d-none d-md-block"><a class="nav-link nav-menu-main menu-toggle hidden-xs text-dark <%=("Y".equalsIgnoreCase(isPasswordExpired) ? "restrictedmenu" : "")%>" href="#"><i class="ft-menu"></i></a></li>
                            <%}%>
                                <li class="nav-item mr-auto d-none d-md-block ">
                                <a class="navbar-brand hidden-xs <%=("Y".equalsIgnoreCase(isPasswordExpired) ? "restrictedmenu" : "")%>" href="Welcome.do"> <img class="brand-logo" alt="logo" src="<%=logo%>?<%=jsdate%>">
                                </a>
                            </li>
                            <li class="nav-item mr-auto d-none d-md-block ">
                                <%if ("Y".equalsIgnoreCase(user)) {%>
                                <div id="scrollable-dropdown-menu-main">
                                    <div class="input-group">
                                        <input type="text" name="customercode" id="customercode"  placeholder="select Customer" class="form-control admin_customercode_selection" >
                                        <span class="input-group-append">
                                            <button class="btn btn-primary" type="button" onClick="getCustomer();" autocomplete="off" id="searchcustbtn"> Search</button>
                                        </span>
                                    </div>
                                </div>
                                <%}%>
                            </li>
                        </ul>
                        <%if (!"Y".equalsIgnoreCase(hideMenuIcon)) {%>
                        <ul class="nav navbar-nav float-right">
                            <%if ("Y".equalsIgnoreCase(enablenotification) && "Yes".equalsIgnoreCase(isViaLogin)) {%>
                            <li class="nav-item show EcomNotification"><a class="nav-link" href="#"><i class="ficon feather icon-bell"></i><span class="badge badge-pill badge-danger badge-up" id="notificationcount">0</span></a></li>
                                        <%}%>
                                        <%if (!"Y".equalsIgnoreCase(hideFullScreenIcon)) {%>
                            <li class="nav-item d-none d-md-block"><a class="nav-link nav-link-expand" href="#"><i class="ficon ft-maximize"></i></a></li>
                                    <%}%>
                                    <%if ("Yes".equalsIgnoreCase(isViaLogin) || "V".equalsIgnoreCase(logintype)) {%>
                            <li class="dropdown dropdown-user nav-item m-t-10">
                                <a class="dropdown-toggle nav-link dropdown-user-link <%=("Y".equalsIgnoreCase(isPasswordExpired) ? "restrictedmenu" : "")%>" href="#" data-toggle="dropdown"> <span class="user-name" style=" color:#276fbe">  <bean:message key="layout.welcome" bundle="<%=clientcode%>"/>&nbsp; <%=strUserName%> </span> </a>
                                <div class="dropdown-menu dropdown-menu-right">
                                    <%if ("U".equalsIgnoreCase(logintype)) {%>
                                    <%if (!("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedReg))) {%>
                                    <a class="dropdown-item" href="Accesslogin.do?type=viewprofile"> <i class="ft-user"></i> <bean:message key="layout.profile" bundle="<%=clientcode%>"/> </a>
                                    <%}%>
                                    <a class="dropdown-item" href="Accesslogin.do?type=reset"> <i class="ft-lock"></i> <bean:message key="layout.changepassword" bundle="<%=clientcode%>"/></a>
                                    <a class="dropdown-item" href="Accesslogin.do?type=contactus"> <i class="ft-phone"></i> <bean:message key="layout.contactus" bundle="<%=clientcode%>"/> </a>
                                    <%if ("Y".equalsIgnoreCase(rateRegMenu)) {%>
                                    <%if (!("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedReg))) {%>
                                       <a class="dropdown-item" href="<%=quoteMenuURL%>"> <i class="ft-edit"></i> <bean:message key="ecom.head.rrq" bundle="<%=clientcode%>"/><!--<bean1:message key="Home.rate.request" bundle="<%=clientcode%>"/>--> </a>
                                    <%}%>
                                    <%}%>
                                    <%if (!("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedReg))) {%>
                                    <a class="dropdown-item" href="Accesslogin.do?type=mailalert"> <i class="ft-bell"></i> <bean:message key="layout.myalert" bundle="<%=clientcode%>"/> </a>
                                    <%}%>
                                    <%if ("Y".equalsIgnoreCase(enableStripePaymentMenu) || "Y".equalsIgnoreCase(enableNGeniusPaymentMenu)) {%>
                                    <%if (!("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedReg))) {%>
                                    <a class="dropdown-item" href="paymentHistory.do"> <i class="ft-credit-card"></i> Payment History </a>
                                    <%}%>
                                    <%}%>
                                    <%if ("Y".equalsIgnoreCase(user_roll_admin)) {%>
                                    <%if (!("Y".equalsIgnoreCase(allowIncompletedRegToLogin) && "Y".equalsIgnoreCase(incompletedReg))) {%>
                                    <a class="dropdown-item" href="Accesslogin.do?type=usercreation"> <i class="ft-user-plus"></i> <bean:message key="ecom.usercreation" bundle="<%=clientcode%>"/> </a>
                                    <%}%>
                                    <%}%>
                                    <div class="dropdown-divider"></div>
                                    <%}%>
                                    <a class="dropdown-item" href="logout.jsp"> <i class="ft-log-out"></i> <bean:message key="layout.logout" bundle="<%=clientcode%>"/> </a>
                                </div>
                            </li>
                            <%} else {
                            %>
                            <li class=" " style="margin-top: -9px;" > 
                                <span class="regi_txt">  <i class="fa fa-envelope"></i> <a href ="Accesslogin.do?type=contactus">  <bean:message key="layout.contactus" bundle="<%=clientcode%>"/> </a> </span>
                                <span class="regi_txt"> <i class="fa fa-user-plus"></i> <a href ="Accesslogin.do?type=Registration">  <bean:message key="Home.registermnu" bundle="<%=clientcode%>"/> </a> </span>
                                <span class="login_txt"  onclick="openNav()"> <i class="fa fa-user"></i> <a href ="javascript:void(0)" ><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/> </a> </span>
                            </li>
                            <%}%>
                            <%if (!"Y".equalsIgnoreCase(hideLangOption)) {%>
                            <li class="dropdown dropdown-language nav-item m-t-10">
                                <a href="#" class="dropdown-toggle nav-link dropdown-user-link" id="dropdown-flag"  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="fa fa-language" style="color: #276fbe;" ></i> <span class="selected-language" style="color:#276fbe;" ><bean:message key="layout.selectlanguage" bundle="<%=clientcode%>"/>  </span></a>
                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdown-flag">                                    
                                    <%
                                        LinkedHashMap langlist = (LinkedHashMap) getServletContext().getAttribute(GeneralConf.LangSelection);
                                        Iterator it = null;
                                        it = langlist.entrySet().iterator();
                                        while (it.hasNext()) {
                                            Map.Entry pairs = (Map.Entry) it.next();%>


                                    <a class="dropdown-item" href='./Accesslogin.do?type=<%=pairs.getKey().toString()%>'/><%=pairs.getValue().toString()%></a>
                                    <%}%>

                                </div>
                            </li>
                            <%}%>
                        </ul>
                        <%}%>
                    </div>
                </div>

            </div>
        </nav>
        <!-- END: Header-->

        <!-- BEGIN: Main Menu-->
        <div class="main-menu menu-fixed menu-dark menu-accordion menu-shadow ">
            <!--        <div class="main-menu menu-fixed menu-dark menu-accordion menu-shadow" data-scroll-to-active="true">-->
            <div class="main-menu-content">
                <tiles:insert attribute="menu"/>
            </div>
        </div>
        <!-- BEGIN: Content-->
        <div class="app-content content">
            <div class="content-wrapper">
                <div class="content-header row">
                </div>
                <div class="content-body"><!-- Stats -->
                    <tiles:insert attribute="body"/>
                </div>
            </div>
        </div>

        <%if ("Y".equalsIgnoreCase(enablenotification) && "Yes".equalsIgnoreCase(isViaLogin)) {%>
        <input type="hidden" id="logincustcode" value="<%=logincustcode%>">
        <script type="text/javascript" src="Design\assets\js\push.js"></script>
        <script type="text/javascript" src="js/Notification.js?<%=jsdate%>"></script>
        <jsp:include page="../Notification.jsp"/>
        <%}%>

        <%if (HotlineContacts != null && HotlineContacts.size() > 0) {%>
        <!-- BEGIN: Helpline--> 
        <div class="customizer border-left-blue-grey border-left-lighten-4 d-xl-block helpline_box" >
            <a class="customizer-close helpline_close  m-r-15" href="javascript:;">
                <i class="feather icon-x font-medium-3 fa fa-times"></i>
            </a>
            <a class="customizer-toggle helpline_btn" href="javascript:;">
                <span><i class="ft-phone"></i> Agency Hotline</span>
            </a>
            <!--<div class="customizer-content p-2 ps ps--active-y"></div>-->
            <div class="customizer-content p-2 ps ps--active-y overflow-auto">
                <div class="chat-sidebar">

                    <div class="chat-sidebar-list-wrapper ps ps--active-y">

                        <h4 class="px-2 pt-2 pb-25 mb-0" style="text-align: center !important;"> Agency Hotline</h4>
                        <div class="container">
                            <div id="accordion">
                                <%  Iterator itr = HotlineContacts.entrySet().iterator();
                                    ArrayList hclis = null;
                                    int in = 0;
                                    while (itr.hasNext()) {
                                        Map.Entry hc = (Map.Entry) itr.next();
                                        hclis = (ArrayList) hc.getValue();
                                        HotlineContactsVO hcvo = null;
                                %>
                                <div class="card hotline_contact_box" style="box-shadow: none !important;margin-bottom: 0.575rem;">
                                    <div class="card-header hotline_contact_header" data-toggle="collapse" href="#hotline<%=in%>">
                                        <%=(String) hc.getKey()%> <span class="float-right"><i class="fa fa-chevron-down" aria-hidden="true"></i></span>
                                    </div>
                                    <div id="hotline<%=in%>" class="collapse">
                                        <div class="card-body">
                                            <%if (hclis != null && hclis.size() > 0) {
                                                    for (int i = 0; i < hclis.size(); i++) {
                                                        hcvo = (HotlineContactsVO) hclis.get(i);
                                                        if (hcvo.getPortname() != null && hcvo.getPortname().trim().length() > 0) {
                                                            if (!("Dubai".equalsIgnoreCase(hcvo.getPortname()) && i == 1)) {
                                            %>
                                            <div class="card hotline_contact_box" style="box-shadow: none !important;    margin-bottom: 0.575rem;">
                                                <div class="card-header hotline_contact_header" data-toggle="collapse" href="#hotlinecontact<%=in + "" + i%>">
                                                    <font color="blue">  <%=hcvo.getPortname()%> </font><span class="float-right"><i class="fa fa-chevron-down" aria-hidden="true"></i></span>
                                                </div>
                                                <div id="hotlinecontact<%=in + "" + i%>" class="collapse">
                                                    <div class="card-body">
                                                        <%}%>
                                                        <div class="p-10">
                                                            <i class="fa fa-user"> </i> <span class='p-l-5'><%=hcvo.getContactName()%></span><br>
                                                            <i class="fa fa-phone-square"> </i><span class='p-l-5'><%=hcvo.getPhone()%></span><br>
                                                            <i class="fa fa-envelope"> </i><span class='p-l-5'><a class='linkeffect' href="mailto:<%=hcvo.getEmail()%>"><%=hcvo.getEmail()%></a></span><br>
                                                        </div>
                                                        <%if (!("Dubai".equalsIgnoreCase(hcvo.getPortname()) && i == 0)) {%>
                                                    </div>
                                                </div>
                                            </div>
                                            <%}%>
                                            <%} else {%>
                                            <div class="p-10">
                                                <i class="fa fa-user">  </i><span class='p-l-5'><%=hcvo.getContactName()%></span><br>
                                                <i class="fa fa-phone-square"> </i><span class='p-l-5'><%=hcvo.getPhone()%></span><br>
                                                <i class="fa fa-envelope"> </i><span class='p-l-5'><a class='linkeffect' href="mailto:<%=hcvo.getEmail()%>"><%=hcvo.getEmail()%></a></span><br>
                                            </div>
                                            <%}
                                                    }
                                                }%>
                                        </div>
                                    </div>
                                </div>
                                <%
                                        in++;
                                    }

                                %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END: Helpline-->   
        <%}%>

        <%if (!"Yes".equalsIgnoreCase(isViaLogin)) {%>
        <div id="mySidenav" class="sidenav_right" style="display: none;">
            <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
            <html:form styleId="loginform" method="post" action="Accesslogin.do?type=login" styleClass="form-horizontal">
                <div class="login_box">
                    <div class="form-group row col-sm-12" id="loginerror" style="display:none">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <div class="alert alert-danger text-center alert-dismissible fade show" role="alert">
                                <strong><%=loginaccount_msg%></strong>  
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="txt_line"><% if (useServiceLoginName) { %><%=serviceLoginName%><% } else { %><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/><% } %></div>

                    <div class="row form-group col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <html:text property="userName" styleId="username" maxlength="50" styleClass = "input_box1 form-control required" />
                        </div>
                    </div>

                    <div class="row form-group col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <html:password property="password" styleId="loginpassword" maxlength="20" styleClass="input_box1 form-control required" />
                        </div>
                    </div>
                    <%if ("Y".equalsIgnoreCase(logincaptcha) && (passattemptCount >= 3)) {%>
                    <div class="row form-group col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <div id="loginrecaptiid" ></div>
                            <div id="g-recaptcha-error-login"></div>  
                        </div>
                    </div>

                    <%}%>
                    <div class="row col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <button class="login_btn" id="loginbtn" type="submit" autocomplete="off"><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></button>
                            <label class="error d-none" id="loginerrormsg" style="margin-bottom: 5px;margin-top: 2px;"> </label>
                        </div>
                    </div>

                    <div class="row form-group col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <span class="forget_txt">  <a href ="Accesslogin.do?type=forgot"><bean:message key="ForgottenPassword.Header.ForgotPassword" bundle="<%=clientcode%>"/> </a> </span> <br/>
                            <span class="dont_txt"><bean:message key="ecom.login.havingnoacc" bundle="<%=clientcode%>"/> <a href ="Accesslogin.do?type=Registration"  class="rem_txt" ><bean:message key="ecom.toregister" bundle="<%=clientcode%>"/> </a> </span>
                        </div>
                    </div>
                </div>

            </html:form>
        </div>
        <%}%>
        <!--Cookie Acceptance start-->
        <div id="ecom-cookie-bar" class="ecom-cookie-bar">
            <div class="cookie-content-inner">
                <span class="cookie-content-left-side"><%=cookieMsg%></span>
                <span class="cookie-content-right-side">
                    <button id="btnacceptcookie" class="btnacceptcookie" tabindex="0" onclick="setAcceptCookies();">Accept</button>
                </span></div>
        </div>
        <!--Cookie Acceptance stop-->
        <!-- BEGIN: Footer-->
        <div class="sidenav-overlay"></div>
        <tiles:insert attribute="footer" />
        <!-- END: Footer-->
        <!--Start of Tawk.to Script-->
        <script type="text/javascript">
            var Tawk_API = Tawk_API || {}, Tawk_LoadStart = new Date();
            (function () {
                var s1 = document.createElement("script"), s0 = document.getElementsByTagName("script")[0];
                s1.async = true;
                s1.src = 'https://embed.tawk.to/<%=liveChatId%>/default';
                s1.charset = 'UTF-8';
                s1.setAttribute('crossorigin', '*');
                s0.parentNode.insertBefore(s1, s0);
            })();
            <%if ("Yes".equalsIgnoreCase(isViaLogin)) {%>
            Tawk_API = Tawk_API || {};
            Tawk_API.visitor = {
                name: '<%=strUserName%>',
                email: '<%=loginemailid%>'
            };
            <%}%>
            
        </script>
        <!--End of Tawk.to Script-->
    </body>
    <a href="" id="return-to-top" style="z-index: 3;"> <i class="fa fa-arrow-up"></i> </a>
    <!-- BEGIN: Theme JS-->
    <script src="Design/app-assets/js/core/app-menu.min.js"></script>
    <script src="Design/app-assets/js/core/app.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/toggle/bootstrap-checkbox.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/toggle/switchery.min.js"></script>
    <script src="Design/app-assets/js/scripts/forms/switch.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/icheck/icheck.min.js"></script>
    <script src="Design/app-assets/js/scrollreveal.min.js"></script>
    <script src="Design/app-assets/js/modernizr.min.js"></script>
    <script src="Design/app-assets/js/scripts/forms/checkbox-radio.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/repeater/jquery.repeater.min.js"></script>
    <script src="Design/app-assets/js/scripts/forms/form-repeater.min.js"></script>
    <script src="Design/app-assets/js/hover.js"></script>
    <!-- END: Theme JS-->
    <script type="text/javascript">
            // Back to top Arrow function


            $(document).ready(function () {

                $(window).scroll(function () {
                    if ($(this).scrollTop() > 100) {
                        $('#return-to-top').fadeIn();
                    } else {
                        $('#return-to-top').fadeOut();
                    }
                });

                // Click event to scroll to top
                $('#return-to-top').click(function () {
                    $('html, body').animate({scrollTop: 0}, 400);
                    return false;
                });
                $(".custom-file-input").on("change", function () {
                    var fileName = $(this).val().split("\\").pop();
                    $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
                });

            });

            $.validator.setDefaults({
                ignore: ":disabled,:hidden,[readonly=readonly]",
                ignoreTitle: true,
                errorPlacement: function (error, element) {
                    if (element.parents('.full-input').is('.full-input')) {
                        error.insertAfter(element.parents('.full-input'));
                    } else if (element.parents('.skin').is('.skin')) {
                        error.insertAfter(element.parents('.skin'));
                    } else if (element.parents('.input-group').is('.input-group')) {
                        error.insertAfter(element.parents('.input-group'));
                    } else if (element.parents('.ecom_group_error').is('.ecom_group_error')) {
                        error.insertAfter(element.parents('.ecom_group_error'));
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            $.extend(true, $.fn.dataTable.defaults, {
                destroy: true,
                columnDefs: [{type: 'date-dd-mmm-yyyy'}]
            });

            /*
             $(document).ready(function () {
             
             $(window).scroll(function () {
             if ($(this).scrollTop() > 100) {
             $(".page_header").css("padding", "6px");
             $(".header_icon_pages").css("display", "none");
             $(".back_btn").css({"height": "25px", "width": "25px"});
             $(".btn_mang, .btn_new_book").css("padding", "0px 2px 2px 2px");
             $(".btn_grp_align").css("margin-top", "-3px");
             } else {
             $(".page_header").css("padding", "6px");
             $(".header_icon_pages").css("display", "inline");
             $(".back_btn").css("height", "40px");
             $(".back_btn").css("width", "40px");
             $(".btn_mang, .btn_new_book").css("padding", "6px 6px");
             $(".btn_grp_align").css("margin-top", "6px");
             }
             });
             
             });*/

    </script>
    <script type="text/javascript" src="js/maskport.js"></script>
    <script type="text/javascript">

        var loginRecaptchaWidgetId;
        var hideMaskPortRecords = '<%=hideMaskPortRecords != null ? hideMaskPortRecords : "N"%>';
        var maskPortList = <%=maskPortListGson%>;

        $(document).ready(function () {
            $('#username').attr('placeholder', '<bean:message key="ViewProfile.userifo.UserName" bundle="<%=clientcode%>"/>');
            $('#loginpassword').attr('placeholder', '<bean:message key="login.password" bundle="<%=clientcode%>"/>');
            $('.remove_autocomplete').attr("autocomplete", "off");
            $('.sidenav-overlay').blur(function () {
                $(".sidenav_right").css("display", "none").css("width", "0px");
                return false;
            });
        <%if ("Y".equalsIgnoreCase(logincaptcha) && (passattemptCount >= 3) && (!"Yes".equalsIgnoreCase(isViaLogin))) {%>
            loginRecaptchaWidgetId = grecaptcha.render("loginrecaptiid", {
                'sitekey': '<%=siteKey%>'
            });
        <%}%>
        });
        // SHA-256 encryption function
        async function sha256(message) {
            const msgBuffer = new TextEncoder().encode(message);
            const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
            const hashArray = new Uint8Array(hashBuffer);
            let binary = '';
            hashArray.forEach((byte) => binary += String.fromCharCode(byte));
            return btoa(binary);  // Base64 encoding
        }

        $("#loginform").validate({
            submitHandler: function (form) {
                const password = document.getElementById('loginpassword').value;
                sha256(password).then(hashedPassword => {
        <%if ("Y".equalsIgnoreCase(enable_encryption)) {%>
                    document.getElementById('loginpassword').value = hashedPassword;
        <%}%>
                    $("#btnsubmit").button('loading');
                    $("#resetbtn").attr("disabled", true);
        <% if ("Y".equalsIgnoreCase(logincaptcha) && (passattemptCount >= 3)) {%>
                    var response = grecaptcha.getResponse(loginRecaptchaWidgetId);
                    if (response.length == 0) {
                        document.getElementById('g-recaptcha-error-login').innerHTML = '<span style="color:red;"><bean:message key="ecom.gglecaptcha.err" bundle="<%=clientcode%>"/></span>';
                        return false; // Prevent form submission if CAPTCHA is invalid
                    }
        <% } %>
                    form.submit(); // This will trigger the form submission
                });

                return false; // Prevent default form submission so that we can manually handle it
 /*
                 // new logic
                 //  $('#loginloader').removeClass('d-none').addClass('d-block');
                 $.ajax({
                 url: "LoginAjax",
                 type: 'post',
                 async: false,
                 data: {username: $('#username').val(), password: $('#loginpassword').val()},
                 success: function (data) {
                 console.log('data=======> ' + data);
                 // $('#loginloader').removeClass('d-block').addClass('d-none');
                 if (data == 'error') {
                 $('#loginerror').show();
                 $('#loginerror').find('strong').html('Error Occurred!');
                 } else if (data == 'invalid') {
                 $('#loginerror').show();
                 $('#loginerror').find('strong').html('Invalid Username / Password');
                 } else {
                 window.location.href = data;
                 }
                 },
                 error: function (xhr) {
                 if (xhr.status == 403) {
                 window.location.href = "SessionExpired.jsp";
                 }
                 }
                 });
                 return false;
                 //
                 */            }
        });


        /*  $("#loginform").validate({
         submitHandler: async function (form) {
         // Get the password value
         const password = document.getElementById('loginpassword').value;
         
         // Hash the password using SHA-256
         const hashedPassword = await sha256(password);
         console.log(hashedPassword);
         // Set the hashed password back to the form
         document.getElementById('loginpassword').value = hashedPassword;
         console.log('final--> ' + document.getElementById('loginpassword').value);
         $("#btnsubmit").button('loading');
         $("#resetbtn").attr("disabled", true);
         <%if ("Y".equalsIgnoreCase(logincaptcha) && (passattemptCount >= 3)) {%>
         var response = grecaptcha.getResponse(loginRecaptchaWidgetId);
         if (response.length == 0) {
         document.getElementById('g-recaptcha-error-login').innerHTML = '<span style="color:red;"><bean:message key="ecom.gglecaptcha.err" bundle="<%=clientcode%>"/></span>';
         return false;
         }
         <%}%>
         sha256(password).then(hashedPassword => {
         console.log(hashedPassword);
         
         // Set the hashed password back to the form
         document.getElementById('loginpassword').value = hashedPassword;
         console.log('final--> ' + document.getElementById('loginpassword').value);
         });
         return false;
         /*
         // new logic
         //  $('#loginloader').removeClass('d-none').addClass('d-block');
         $.ajax({
         url: "LoginAjax",
         type: 'post',
         async: false,
         data: {username: $('#username').val(), password: $('#loginpassword').val()},
         success: function (data) {
         console.log('data=======> ' + data);
         // $('#loginloader').removeClass('d-block').addClass('d-none');
         if (data == 'error') {
         $('#loginerror').show();
         $('#loginerror').find('strong').html('Error Occurred!');
         } else if (data == 'invalid') {
         $('#loginerror').show();
         $('#loginerror').find('strong').html('Invalid Username / Password');
         } else {
         window.location.href = data;
         }
         },
         error: function (xhr) {
         if (xhr.status == 403) {
         window.location.href = "SessionExpired.jsp";
         }
         }
         });
         return false;
         //
         */
        /*   }
         });*/
        function openNav() {
            document.getElementById("mySidenav").style.width = "440px";
            $('#mySidenav').show();
            // $('.sidenav-overlay').removeClass('d-none').addClass('d-block');
        }

        function closeNav() {
            document.getElementById("mySidenav").style.width = "0";
            $('#mySidenav').hide();
            // $('.sidenav-overlay').removeClass('d-block').addClass('d-none');
        }
        function KeyPress(e) {
            var evtobj = window.event ? event : e
            if (evtobj.keyCode == 77 && evtobj.ctrlKey) {
                $("#menuenable").click();
            }
        }
        document.onkeydown = KeyPress;

        function increaseValue(obj) {
            var num = $(obj).siblings('.number_val_cls').val();//.trigger('change');
            var value = parseInt(num, 10);
            value = isNaN(value) ? 0 : value;
            value++;
            $(obj).siblings('.number_val_cls').val(value);
            $(obj).siblings('.number_val_cls').trigger('blur');
        }

        function decreaseValue(obj) {
            var num = $(obj).siblings('.number_val_cls').val();//.trigger('change');
            var value = parseInt(num, 10);
            value = isNaN(value) ? 0 : value;
            value < 1 ? value = 1 : '';
            value--;
            $(obj).siblings('.number_val_cls').val(value);
            $(obj).siblings('.number_val_cls').trigger('blur');
        }


    </script>
    <script type="text/javascript">
        function gotoSection(cururl) {
            window.location.assign('./Accesslogin.do?type=' + cururl);
        }
    </script>
    <script type="text/javascript" src="js/mainecom.js"></script>
    <script type="text/javascript">
        <%if ("Y".equalsIgnoreCase(user)) {%>

        var substringMatcher = function (url) {
            return function findMatches(q, cb) {
                var matches, substrRegex, strs;
                var arr1 = new Array();
                $.ajax({
                    url: url,
                    type: 'post',
                    async: false,
                    success: function (data) {
                        if (data != null) {
                            var arr = data.split("~~");
                            for (var i = 1; i < arr.length; i++) {
                                arr1[i] = arr[i];
                            }
                            strs = arr1;
                        }

                    },
                    error: function (xhr) {
                        if (xhr.status == 403) {
                            window.location.href = "SessionExpired.jsp";
                        }
                    }
                });
                matches = [];
                substrRegex = new RegExp(q, 'i');
                $.each(strs, function (i, str) {
                    if (substrRegex.test(str)) {
                        matches.push({value: str});
                    }
                });

                cb(matches);
            };
        };
        $('#customercode').typeahead({
            hint: true,
            highlight: true,
            minLength: 3
        }, {
            name: 'states',
            displayKey: 'value',
            source: substringMatcher("PortListAjax?type=customer&q=")
        });

        <%}%>
        $(document).ready(function () {

            $(window).scroll(function () {
                if ($(this).scrollTop() > 100) {
                    $(".page_header").css("padding", "6px");
                    $(".header_icon_pages").css("display", "inline");
                    $(".header_icon_pages").css({"width": "30px", "height": "30px"});
                    $(".back_btn").css({"height": "25px", "width": "25px"});
                    $(".btn_mang, .btn_new_book").css("padding", "2px 8px 2px 8px");
                    $(".btn_grp_align").css("margin-top", "0px");
                    $("#genmailbtn").css("top", "60px");
                } else {
                    $(".page_header").css("padding", "6px");
                    $(".header_icon_pages").css("display", "inline");
                    $(".header_icon_pages").css({"width": "50px", "height": "50px"});
                    $(".back_btn").css("height", "40px");
                    $(".back_btn").css("width", "40px");
                    $(".btn_mang, .btn_new_book").css("padding", "6px 6px");
                    $(".btn_grp_align").css("margin-top", "6px");
                    $("#genmailbtn").css("top", "69px");
                }
            });

        });
    </script>
    <%
        if (result != null && result.length() > 0 && (!"VALID".equalsIgnoreCase(result))) {
    %>
    <script type="text/javascript">
        $('#loginerror').show();
        openNav();

        $("#loginpassword").val("");
        $("#resetbtn").attr("disabled", false);

        <% String loginerrormsg = (String) request.getAttribute("Loginaccount_expired_msg");
            System.out.println("[ECOM-WAR] MainLoginLayout  loginerrormsg=> " + loginerrormsg);
            if (loginerrormsg != null && loginerrormsg.length() > 0) {%>
        $('#loginerror').hide();
        $('#loginerrormsg').html('<%=loginerrormsg%>');
        $('#loginerrormsg').removeClass('d-none');
        <%
        } else {
            String Invalid_msg = (String) request.getAttribute("Temp_Password_Invalid_MSG");
            System.out.println("[ECOM-WAR] MainLoginLayout  Invalid_msg=> " + Invalid_msg);
            if (Invalid_msg != null && Invalid_msg.length() > 0) {
        %>
        $('#loginerror').find('strong').html('<%=Invalid_msg%>');
        <% }
            }%>
    </script>
    <% }%>
    <%if ("login".equalsIgnoreCase(defaultloginopen)) {%>
    <script type="text/javascript">
        openNav();
    </script>
    <%}%>
    <script type="text/javascript">
        function openLoader() {
            $("#processLoad").show();
        }
        function closeLoader() {
            $("#processLoad").hide();
        }
        closeLoader();

        function setAcceptCookies() {

            /* // $.cookie("ecom_cookie_acceptance", 'accepted');
             let cookieExpire = new Date();
             cookieExpire.setMonth(cookieExpire.getMonth() + 1);
             //;expires='+ expireDate.toGMTString() +'
             // document.cookie='ecom_cookie_acceptance=accepted;expires='+ cookieExpire.toGMTString() +';"HttpOnly;Secure;SameSite=Strict"';
             document.cookie = 'ecom_cookie_acceptance=accepted;expires=' + cookieExpire.toGMTString() + ';path=/;Secure;SameSite=Strict;';
             $('#ecom-cookie-bar').hide();*/

            $.ajax({
                url: 'ecomAjax?type=setcookieacceptance',
                type: 'POST',
                xhrFields: {
                    withCredentials: true // send cookies with request
                },
                success: function () {
                    $('#ecom-cookie-bar').hide(); // Hide the cookie bar
                },
                error: function () {
                    alert('Failed to set cookie');
                }
            });
        }
        /*$(document).ready(function () {
         
         var cookieaccept = document.cookie.split(';');
         //console.log(cookieaccept.indexOf('ecom_cookie_acceptance=accepted'));
         if (cookieaccept.indexOf('ecom_cookie_acceptance=accepted') >= 0) {
         //if (document.cookie.split(';').filter((item) => item.includes('ecom_cookie_acceptance=accepted')).length) {
         $('#ecom-cookie-bar').hide();
         } else {
         $('html').addClass('has-ecom-cookie-bar ecom-cookie-bar-bottom-bar ecom-cookie-bar-bar');
         }
         });*/
        $(document).ready(function () {
        <%
            String cookieValue = "";
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("ecom_cookie_acceptance".equals(cookie.getName())) {
                        cookieValue = cookie.getValue();
                        break;
                    }
                }
            }
        %>
            if ('accepted' == '<%=cookieValue%>') {
                $('#ecom-cookie-bar').hide();
            } else {
                $('html').addClass('has-ecom-cookie-bar ecom-cookie-bar-bottom-bar ecom-cookie-bar-bar');
            }
        });


    </script>
    <script>
        jQuery.extend(jQuery.validator.messages, {
            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
            number: "<bean:message key="ecom.msg.volvalnum" bundle="<%=clientcode%>"/>",
            maxone: "<bean:message key="ecom.msg.volvalnum" bundle="<%=clientcode%>"/>",
            maxzero: "<bean:message key="ecom.msg.volvalnum" bundle="<%=clientcode%>"/>",
            validatepoint: "<bean:message key="ecom.msg.volvalnum" bundle="<%=clientcode%>"/>",
            validatezeropoint: "<bean:message key="ecom.msg.volvalnum" bundle="<%=clientcode%>"/>",
            email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>"


        });
        $(function () {
            $("#myImportModal").on('hidden.bs.modal', function () {
                console.log("myImportModal hidden");
                document.getElementById('importexcel').value = "";
                $(this).find('.custom-file-label').text('<bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/>');
            });
        });
        $(function () {
            $(".helpline_btn").on('click', function () {
                console.log("Helpline button click");
                //$('.helpline_btn').addClass('d-none');
                if ($(".helpline_box").is('.open')) {
                    $('.helpline_box').removeClass('open');
                } else {
                    $('.helpline_box').addClass('open');
                }
            });
            $(".helpline_close").on('click', function () {
                console.log("Helpline button click");
                //$('.helpline_btn').removeClass('d-none');
                $('.helpline_box').removeClass('open');
            });
        });
        function loadtypeaheadtooltipmsg() {
        <%if ("GWF".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.CARRIER_CODE))) {%>
            $('.tt-input,.autocompletetmsg').attr('data-toggle', 'tooltip');
            $('.tt-input,.autocompletetmsg').attr('title', 'Type to search the values for selection');
            $('.autocompletetmsgfortype').attr('data-toggle', 'tooltip');
            $('.autocompletetmsgfortype').attr('title', 'Cargo Type listed in Quantity. Type to search the values for selection');
            $('.portsnotes').attr('data-toggle', 'tooltip');
            $('.portsnotes').attr('title', 'Use " , " between city and state to search a location');
            $('[data-toggle="tooltip"]').tooltip();
            $('.admin_customercode_selection').tooltip('disable');  
        <%}%>
        <%if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_cargoWeight_tooltip))) {%>
            $('.cargweightmsg').attr('data-toggle', 'tooltip');
            $('.cargweightmsg').attr('title','Cargo weight refers to the total weight of the product(s) loaded, including all pallets and packaging materials used');
            $('[data-toggle="tooltip"]').tooltip();
        <%}%>
        <%if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_Password_Validation))) {%>
            $('.PasswordValidationNote').attr('data-toggle', 'tooltip');
            $('.PasswordValidationNote').attr('data-placement', 'top');
            $('.PasswordValidationNote').attr('data-html', 'true');
            $('.PasswordValidationNote').attr('title', '<%=passnote%>');
            $('[data-toggle="tooltip"]').tooltip();
        <%}%>
        }

        $(document).ready(function () {
            loadtypeaheadtooltipmsg();
        });
    </script>
    <script type="text/javascript">
        var maxFileSize = 0;
        var validFileExtensions = [".Exe", ".Jar", ".Bat", ".Dat", ".swf", ".wav", ".mp3", ".mp4", ".mpg", ".wmv", ".avi", ".flv", ".mpg", ".mpeg", ".avi", ".wmv", ".mov", ".rm", ".ram", ".ogg", ".webm", ".mp4", ".vob", ".gifv", ".yuv", ".3gp", ".msi"];

        function ValidateSingleInput(oInput) {
            console.log('Inside ValidateSingleInput function....');
            if (oInput.type == "file") {
                var sFileName = oInput.value;
                if (sFileName.length > 0) {
                    var blnValid = false;
                    if ('<%=enableFileBackendValidation%>' == 'Y') {
                        blnValid = true;
                        var ext = sFileName.split('.');
                        ext = ext[ext.length - 1];
                        var onlyValidext = '<%=ecomFileExt%>'.split(',');
                        for (var i = 0; i < onlyValidext.length; i++) {
                            if (ext == onlyValidext[i]) {
                                blnValid = false;
                                break;
                            }
                        }
                    } else {
                        for (var j = 0; j < validFileExtensions.length; j++) {
                            var sCurExtension = validFileExtensions[j];
                            if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                                blnValid = true;
                                break;
                            }
                        }
                    }

                    if (blnValid) {
                        //alert("The following file format is not allowed: " + validFileExtensions.join(", "));
                        alert("<bean:message key="ecom.msg.invalidfileformat" bundle="<%=clientcode%>"/>");
                        oInput.value = "";
                        return false;
                    }
                }

                var file_id = $(oInput).attr('id');
                var fileInput = document.getElementById(file_id);
                console.log(fileInput);
                if (fileInput.files.length > 0) {
                    const fileSize = fileInput.files.item(0).size;
                    const maxFileSize = '<%=ecomFileUpdSize%>' * 1024 * 1024;
                    console.log(fileSize);
                    console.log(maxFileSize);
                    if (fileSize >= maxFileSize) {
                        fileInput.value = "";
                        alert("Please select a file less than 10MB.");
                    }
                }
            }
            return true;
        }
    </script>
    <%if ("Y".equalsIgnoreCase(enablesessiontimeout) && "Yes".equalsIgnoreCase(isViaLogin)) {%>
    <script type="text/javascript" src="Design\assets\plugins\SessionTimeout\bootstrap-session-timeout.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            callSessionTimeout();
        });
        function callSessionTimeout() {
            $.sessionTimeout({
                keepAliveUrl: 'UserProfileAjax?type=SessionRefresh', //UserProfileAjax?type=SessionRefresh
                logoutUrl: 'logout.jsp',
                redirUrl: 'SessionExpired.jsp',
                warnAfter: 1500000, //
                redirAfter: 1680000, //
                countdownBar: true,
                ignoreUserActivity: true,
                countdownSmart: true
            });

        }
    </script>
    <%}%>
</html>
