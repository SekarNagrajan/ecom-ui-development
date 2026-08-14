<%@page contentType="text/html"  pageEncoding="UTF-8" import="java.util.Random,java.util.Map,java.util.Iterator,java.util.Hashtable,java.util.List,net.ecom.vo.CustomerAdvisoryVo,java.util.Date,net.ecom.common.GeneralConf,net.ecom.vo.RegisterMenuVO,java.util.LinkedHashMap,net.ecom.common.Validation"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
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
    String icoimage = imagepath + "ico.ico";
    //String loginForMenu = (String) session.getAttribute("LOGINFORMENU");
    //session.setAttribute("LOGINFORMENU", null);
    //System.out.println("[ECOM-WAR] login_C ####- loginForMenu -=-=-=-=====> " + loginForMenu);
    String selectedlan = (String) session.getAttribute("SelectedLanguage");
    System.out.println("[ECOM-WAR] login_C selectedlangis" + selectedlan);
    String clientcode = (String) session.getAttribute("clientcode");
    String isViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
    String strUserName = (String) session.getAttribute("SESSION_USERNAME");

    String logintype = (String) session.getAttribute("SESSION_LOGIN_TYPE");
    /* if ("V".equalsIgnoreCase(logintype)) {
        strUserName = "Admin";
    }*/
    if (strUserName == null || strUserName.length() == 0) {
        strUserName = "Admin";
    }
    String result = (String) request.getAttribute("LOGINRESPONSE_DATA");
    System.out.println("[ECOM-WAR] login_C logintype====> " + logintype + " logintype.length()====> " + logintype.length());
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
    String isPasswordExpired = (String) session.getAttribute("isPasswordExpired");
    System.out.println("[ECOM-WAR] login_C isPasswordExpired==> " + isPasswordExpired);
    List<RegisterMenuVO> menulist = (List<RegisterMenuVO>) getServletContext().getAttribute("usermenu");
    String schdMenuCategory = menulist.stream().filter(o -> "SCH".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    String strDateFormat = (String) getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String contextRoot = request.getContextPath();
    String ratesMenuCategory = menulist.stream().filter(o -> "RAT".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    String trackingMenuCategory = menulist.stream().filter(o -> "TRK".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    String pastdateconfig = (String) session.getServletContext().getAttribute(GeneralConf.PAST_DATE_CONFIG);
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String toDisplayGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
    String Version = (String) getServletContext().getAttribute("Version");
    String footercopyright = (String) getServletContext().getAttribute("footercompanyname");
    String copyrightslink = (String) getServletContext().getAttribute(GeneralConf.AGENCYLINK);
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String centralizeCopyrights = (String) getServletContext().getAttribute(GeneralConf.CENTRALIZE_COPYRIGHTS);
    String scheduleDateRangeDaysVal = (String) getServletContext().getAttribute(GeneralConf.Schedule_Date_Range_Days);
    int scheduleDateRangeDays = 30;
    if (scheduleDateRangeDaysVal != null && scheduleDateRangeDaysVal.trim().length() > 0) {
        try { scheduleDateRangeDays = Integer.parseInt(scheduleDateRangeDaysVal.trim()); } catch (Exception e) { scheduleDateRangeDays = 30; }
    }
%>
<html>
    <head>
        <title><%=page_title%></title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="shortcut icon" type="image/x-icon" href="<%=icoimage%>?<%=jsdate%>"/>

        <link type="text/css" href="Design/app-assets/fonts/font-awesome/css/font-awesome.min.css" rel="stylesheet"  />
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@200;300;400;500;700&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&display=swap" rel="stylesheet">
        <!-- BEGIN: Theme CSS-->
        <!--        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap.min.css" />-->
        <!-- END: Theme CSS-->
        <link rel="stylesheet" type="text/css" href="Design/assets/css/Bootstrap5.css"/>
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/datepicker/css/bootstrap-datepicker3.css" />
        <link rel="stylesheet" type="text/css" href="Design/assets/css/styleHome.css?<%=jsdate%>"/>
        <script type="text/javascript" type="text/javascript" src="Design/assets/js/C_HomeJS.js?<%=jsdate%>"></script>
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/autocomplete/typeahead.bundle.css"/>



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
        <style>
            html body {
                background-color: white;
                font-family: 'DM Sans', sans-serif;
            }
        
        
        
            /* Specific elements */
            input,
            select,
            textarea,
            button {
                font-family: 'DM Sans', sans-serif;
            }
        
            .nav-link,
            .dropdown-item,
            .btn,
            .submit-btn {
                font-family: 'DM Sans', sans-serif;
            }
        
            .form-control,
            .input_box,
            .input_box1 {
                font-family: 'DM Sans', sans-serif;
            }
        
        
            /* Tab Navigation Styling - Updated to match image design */
            #homeTab {
                list-style: none;
                padding: 0;
                margin: 0;
                display: flex;
                background-color: #f5f5f5;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }
        
            #homeTab .nav-item {
                flex: 1;
                margin: 0;
                border-right: 1px solid #e0e0e0;
            }
        
            #homeTab .nav-item:last-child {
                border-right: none;
            }
        
            #homeTab .nav-link {
                display: block;
                padding: 15px 20px;
                text-decoration: none;
                font-size: 14px;
                font-weight: 500;
                text-align: center;
                border: none;
                /*                background-color: #7b8aa6;
                                                                        color: #fff;*/
                background-color: #f8f9fa;
                color: #182d4d;
                transition: all 0.3s ease;
                position: relative;
                width: 100%;
            }
        
            #homeTab .nav-link:hover {
        
                /*                background-color: #7b8aa6;
                                                                        color: #fff;*/
                background-color: #182d4dde;
                color: #fff;
            }
        
            #homeTab .nav-link.active {
                background-color: #182d4d;
                color: #ffffff;
                font-weight: 600;
            }
        
            #homeTab .nav-link.active:hover {
                background-color: #182d4d;
                color: #ffffff;
            }
        
            .tab-content {
                display: block !important;
            }
        
            .cs_left {
                width: 50%;
                float: left;
                background-image: url(/Ecommaster/bg_G.jpg);
                background-size: cover;
                background-position: center;
                height: 100vh;
                border-bottom-right-radius: 50px;
                position: relative;
            }
        
            .cs_nav li a:hover {
                /*                color: #8b1e41 !important;
                                                                        background-color: rgba(139, 30, 65, 0.1);*/
                color: #050d95 !important;
                background-color: rgb(18 59 155 / 24%);
                text-decoration: none;
                border-radius: 20px;
            }
        
            button.submit-btn {
                background-color: #8b1e41 !important;
            }
        
            .nav-item a.dropdown-item {
                font-size: 12px;
                min-width: 100px;
            }
        
            /* Hero Text Section Styling */
            .hero-text-section {
                position: absolute;
                bottom: 18px;
                left: 50%;
                transform: translateX(-50%);
                text-align: center;
                z-index: 10;
                width: 90%;
                max-width: 90%;
                padding: 0 5%;
            }
        
            .hero-subtitle {
                color: #ffffff;
                font-size: clamp(14px, 2vw, 24px);
                font-weight: 400;
                margin-bottom: 20px;
                opacity: 0.9;
                line-height: 1.3;
            }
        
            .hero-title {
                color: #ffffff;
                /* font-size: clamp(24px, 3.5vw, 60px); */
                font-size: clamp(24px, 4.5vw, 60px);
                font-weight: 500;
                /* font-family: 'DM Sans', sans-serif; */
                font-family: "Bebas Neue", sans-serif;
                line-height: 1.1;
                text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
                font-family: "Bebas Neue", sans-serif;
            }
        
            .hero-line-1 {
                margin-bottom: 0;
                line-height: 1.2;
                max-width: 100%;
                word-wrap: break-word;
            }
        
        
            .form-group select {
                font-size: 12px;
            }
        
            body .accordion-button:not(.collapsed),
            .accordion-button,
            .title-sign {
                background-color: #182d4d;
            }
        
            .forgot-reg a {
                color: #182d4d;
            }
        
            .cs_nav li a.activelogin,
            .cs_nav li a:hover {
                color: #050d95;
            }
        
            .refresh_cap {
                color: #182d4d;
            }
        
            .cs_left img {
                position: absolute;
                top: 20px;
                left: 20px;
                z-index: 1;
                max-width: 80px !important;
            }
        
            .cs_left:after {
                position: absolute;
                content: '';
                width: 100%;
                height: 100%;
                background: #00000024;
                top: 0;
                border-bottom-right-radius: 50px;
            }
        
            .cs_acc {
                width: 85%;
                margin: 15% auto;
                box-shadow: 0 2px 8px rgb(0 0 0 / 22%);
                border-bottom-left-radius: 15px;
                border-bottom-right-radius: 15px;
            }
        
            .row {
                margin-bottom: 10px;
            }
        
            .title-sign {
        
                position: relative;
                padding-left: 40px;
            }
        
            body .accordion-button:not(.collapsed),
            .accordion-button,
            .title-sign {
                color: #000;
                background-color: unset;
                font-weight: 700;
                background-image: unset;
            }
        
            .title-sign:after {
                background-image: url("/ecomonline/Design/assets/images/homePage_icons/refer.png");
                background-size: 16px 16px;
                background-repeat: no-repeat;
                background-position: center;
                background-color: #192e4f;
                position: absolute;
                content: '';
                width: 30px;
                height: 30px;
                top: 0;
                left: 0;
                border-radius: 50%;
            }
        
            /* Mobile devices (small screens) */
            @media only screen and (max-width: 767px) {
                .cs_left {
                    width: 100%;
                    height: 40vh;
                    border-bottom-right-radius: 0;
                    border-bottom-left-radius: 50px;
                    float: none;
                    display: block;
                }
        
                .cs_right {
                    width: 100%;
                    float: none;
                    clear: both;
                    position: relative;
                    min-height: 60vh;
                }
        
                .cs_right_main {
                    display: flex;
                    flex-direction: column;
                    min-height: 60vh;
                }
        
                .cs_nav {
                    order: 1;
                    background-color: #fff;
                    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
                    padding: 8px 0;
                    margin: 0;
                }
        
                .cs_nav .cs_main_menu {
                    display: flex;
                    justify-content: space-around;
                    margin: 0;
                    padding: 0;
                    list-style: none;
                }
        
                .cs_nav li {
                    flex: 1;
                    text-align: center;
                }
        
                .cs_nav li a {
                    padding: 6px 2px;
                    font-size: 10px;
                    display: block;
                    text-decoration: none;
                    color: #182d4d;
                }
        
                .cs_nav li a i {
                    display: block;
                    margin-bottom: 2px;
                    font-size: 12px;
                }
        
                .cs_model {
                    order: 1;
                }
        
                .cs_model_sec {
                    top: 50%;
                    width: 90%;
                    margin: 20px auto;
                }
        
                .cs_acc {
                    order: 2;
                    margin: 0;
                    width: 100%;
                    box-shadow: none;
                    margin-top: 10%;
                    padding: 0px 5px;
                }
        
                #homeTab {
                    order: 1;
                    margin: 0;
                    border-radius: 0;
                    background-color: #f5f5f5;
                    position: relative;
                }
        
                .tab-content {
                    order: 2;
                    margin: 0;
                    padding: 20px 10px;
                }
        
                .copyrights {
                    display: none;
                }
        
                .refresh_cap {
                    margin-left: 35px;
                }
        
                .cs_left img {
                    margin: 20px 20px;
                    top: 0px;
                    left: 0px;
                    max-width: 50px !important;
                }
                .hero-text-section {
                    bottom: 6px;
                }
                .cs_left {
                    background-size: 100% 100%;
                }
        
            }
        
            /* 15-inch screens (typical laptop ~1366px width) */
            @media only screen and (min-width: 768px) and (max-width: 1440px) {}
        
        
            .cs_model_sec {
                height: 375px;
                box-shadow: 0 2px 8px rgb(0 0 0 / 22%);
                border-radius: 15px;
                margin-top: 4% !important;
            }
        
        
            .datepicker {
                color: #000;
                font-size: 12px;
            }
        
            .cs_nav li a {
                color: #182d4d;
            }
        
            .copyrights {
                bottom: 16px;
                position: absolute;
                right: 20px;
                color: #182d4d;
                font-weight: 500;
                font-size: 13px;
                left: unset;
            }
        
            .cs_left img {
                margin: 20px 20px;
            }
        </style>
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
    <body>
        <section>
            <div class="cs_left">
                <img src="/Ecommaster/logo_G.png" alt="logo">

                <!-- Hero Text Section -->
                <div class="hero-text-section">
                    <!--                    <div class="hero-subtitle">Refrigerated vessels, global coverage</div>-->
                    <div class="hero-title">
                        <div class="hero-line-1">Preferred in Perishables</div>
                    </div>
                </div>

                
            </div>

            <div class="cs_right">
                <div class="cs_right_main">
                    <div class="cs_nav navbar-container content">
                        <nav class="text-end">
                            <ul class="cs_main_menu">
                                <li>
                                    <a href="javascript:;" id="cs_login">
                                        <i class="fa fa-lock" aria-hidden="true"></i>
                                        <span><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></span>
                                    </a>
                                </li>
                                <li>


                                    <a href ="Accesslogin.do?type=Registration">  <i class="fa fa-user-plus" aria-hidden="true"></i><bean:message key="Home.registermnu" bundle="<%=clientcode%>"/> </a>

                                </li>
                                <li>


                                    <a href ="Accesslogin.do?type=contactus"><i class="fa fa-envelope-o" aria-hidden="true"></i>  <bean:message key="layout.contactus" bundle="<%=clientcode%>"/> </a>

                                </li>
                                <li class="nav-item m-t-10">
                                    <div class="dropdown">
                                        <a class="dropdown-toggle" type="button" id="langmenu" data-bs-toggle="dropdown" aria-expanded="false">
                                            <i class="fa fa-language"  ></i> <span class="selected-language"  ><bean:message key="layout.selectlanguage" bundle="<%=clientcode%>"/>  </span>
                                        </a>
                                        <ul class="dropdown-menu" aria-labelledby="langmenu">

                                            <%
                                                LinkedHashMap langlist = (LinkedHashMap) getServletContext().getAttribute(GeneralConf.LangSelection);
                                                Iterator it = null;
                                                it = langlist.entrySet().iterator();
                                                while (it.hasNext()) {
                                                    Map.Entry pairs = (Map.Entry) it.next();%>

                                            <li><a class="dropdown-item" href='./Accesslogin.do?type=<%=pairs.getKey().toString()%>'/><%=pairs.getValue().toString()%></a></li>
                                                <%}%>
                                        </ul>
                                    </div>

                                </li>
                            </ul>
                        </nav>
                    </div>
                    <%if (!"Yes".equalsIgnoreCase(isViaLogin)) {%>
                    <div class="cs_model">
                        <div class="cs_model_sec m-4 p-3" >
                            <div class="login-close"><a href="javascript:;" ><img src="Design/assets/images/homePage_icons/cancel.png" alt="close"></a></div>
                            <h2 class="title-sign" style="margin-top: 1% !important;">SIGN IN</h2>
                            <div class="form-group row col-sm-12" id="loginerror" style="margin-left: 4px;display:none">
                                <div class="alert alert-danger text-center">
                                    <strong><bean:message key="ecom.login.invup" bundle="<%=clientcode%>"/></strong>  
                                </div>
                            </div>
                            <html:form styleId="loginform" method="post" action="Accesslogin.do?type=login" styleClass="form-horizontal">
                                <div class="form-group cs-forms">
                                    <div class="row">
                                        <div class="col-12">
                                            <html:text property="userName" styleId="userName"  maxlength="50" styleClass = "input_box1 required" />
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-12">
                                            <html:password property="password" styleId="password"  maxlength="20" styleClass="input_box1 required" />
                                        </div>
                                    </div>
                                    <div class="col-5 offset-3">
                                        <button class="submit-btn" id="loginbtn" type="submit" autocomplete="off"><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></button>
                                    </div>
                                </div>
                            </html:form>
                            <div class="col-12 forgot-reg">
                                <p>
                                    <a href="javascript:;" class="cs_forgot"><span class="forget_txt">  <a href ="Accesslogin.do?type=forgot"><bean:message key="ForgottenPassword.Header.ForgotPassword" bundle="<%=clientcode%>"/> </a> </span></a>
                                </p>
                                <p class="m-1">
                                    <span> Don't have an account? <a href="javascript:;"><span class="dont_txt"> <bean:message key="ecom.login.havingnoacc" bundle="<%=clientcode%>"/> <a href ="Accesslogin.do?type=Registration"  class="rem_txt" > <bean:message key="ecom.toregister" bundle="<%=clientcode%>"/> </a> </span></a></span>
                                </p>
                            </div>
                        </div>

                    </div>  
                    <%}%>
                    <div class="cs_acc">
                        <div class="">

                            <!-- Tabs Navigation -->
                            <ul class="nav nav-tabs" id="homeTab" role="tablist">
                                <li class="nav-item" role="presentation">
                                    <button class="nav-link active" id="schedule-tab" <%=(("P".equalsIgnoreCase(schdMenuCategory)) ? " onclick='openLogin()'  " : "  data-bs-toggle='tab' data-bs-target='#schedule' ")%>
                                            type="button" role="tab" aria-controls="schedule" aria-selected="true"><bean:message key="ecom.login.scdhead" bundle="<%=clientcode%>"/></button>
                                </li>
                                <li class="nav-item" role="presentation">
                                    <button class="nav-link" id="tracking-tab" <%=(("P".equalsIgnoreCase(trackingMenuCategory)) ? " onclick='openLogin()' " : " data-bs-toggle='tab' data-bs-target='#tracking' ")%> 
                                            type="button" role="tab" aria-controls="tracking" aria-selected="false"><bean:message key="ecom.login.trckhead" bundle="<%=clientcode%>"/></button>
                                </li>
                                <li class="nav-item" role="presentation">
                                    <button class="nav-link" id="rates-tab" <%=(("P".equalsIgnoreCase(ratesMenuCategory)) ? " onclick='openLogin()' " : " data-bs-toggle='tab' data-bs-target='#rates' ")%> 
                                            type="button" role="tab" aria-controls="rates" aria-selected="false"><bean:message key="ecom.rates" bundle="<%=clientcode%>"/></button>
                                </li>
                            </ul>

                            <!-- Tabs Content -->
                            <div class="tab-content p-3 border-top-0" id="myTabContent">
                                <!-- Schedule Tab -->
                                <div class="tab-pane fade show active" id="schedule" role="tabpanel" aria-labelledby="schedule-tab">
                                    <form id="loginschedsearch" name="loginschedsearch" method="post" class="form-horizontal" action="schedules.do" novalidate="novalidate">
                                        <div class="form-group cs-forms">
                                            <div class="row">
                                                <div class="col-5">
                                                    <input type="hidden" name="typeheadhidden" id="typeheadhidden" value="">
                                                    <input type="hidden" name="schetype" id="schetype" value="loginschedule">
                                                    <input type="hidden" name="type" id="type" value="location">
                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;">
                                                        <input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);">
                                                        <input type="text" class="org-del portsnotes tt-input valid" id="pol" name="pol" placeholder="<bean:message key="Tracking.origin" bundle="<%=clientcode%>"/>" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;" aria-invalid="false">
                                                        <pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre>
                                                        <div class="tt-menu tt-empty" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;">
                                                            <div class="tt-dataset tt-dataset-ports">  </div>
                                                        </div>
                                                    </span>
                                                </div>
                                                <div class="col-1">
                                                    <a href="javascript:swapPort();"> <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg"> </a>
                                                </div>
                                                <div class="col-5">
                                                    <input type="hidden" name="typeheadhidden1" id="typeheadhidden1">
                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;">
                                                        <input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);">
                                                        <input type="text" class="org-del portsnotes tt-input" id="pod" name="pod" placeholder="<bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/>" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;">
                                                        <pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre>
                                                        <div class="tt-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;">
                                                            <div class="tt-dataset tt-dataset-ports">
                                                            </div>        
                                                        </div>
                                                    </span> 
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-5">
                                                    <input type="text" class="ft-date " name="scdshipmentdate" id="scdshipmentdate" autocomplete="off" placeholder="From Date">

                                                </div>
                                                <div class="col-1">

                                                </div>
                                                <div class="col-5">
                                                    <input type="text" class="ft-date " id="todateq" name="todate" autocomplete="off" placeholder="To Date">
                                                </div>
                                            </div>
                                            <div class="col-5 offset-3">
                                                <button type="submit" class="submit-btn" name="submit" value="Submit"> Submit</button>

                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <!-- Tracking Tab -->
                                <div class="tab-pane fade" id="tracking" role="tabpanel" aria-labelledby="tracking-tab">
                                    <form id="logintracsearch" name="logintracsearch" method="post" class="form-horizontal" action="tracking.do" >
                                        <div class="form-group cs-forms">
                                            <div class="row">
                                                <div class="col-12">
                                                    <input type="hidden" name="tracktype" id="tracktype" value="logintracking"/>
                                                    <input class="required" type="text" placeholder="<bean:message key="ecom.login.blorbknum" bundle="<%=clientcode%>"/>" id="logintracno" name="logintracno"> 
                                                </div>

                                            </div>
                                            <div class="row">
                                                <div class="col-sm-5">
                                                    <input class="input_box remove_autocomplete required" name="captcha" id="captcha" type="text" placeholder="<bean:message key="Res.captchachar" bundle="<%=clientcode%>"/>" >
                                                </div>

                                                <div class="col-sm-3">
                                                    <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage" alt="captcha" /> 

                                                </div>
                                                <div class="col-sm-2">
                                                    <a href="javascript:void(0)" title="Reload captcha" class="refresh_cap" id="captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');"> <i class="fa fa-refresh"></i> </a>
                                                </div>

                                            </div>
                                            <div class="col-5 offset-3">
                                                <button type="submit" class="submit-btn"> <bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>

                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <!-- Rates Tab -->
                                <div class="tab-pane fade" id="rates" role="tabpanel" aria-labelledby="rates-tab">
                                    <form id="loginrates" name="loginrates" method="post" class="form-horizontal" action="Accesslogin.do?type=rates" >
                                        <div class="form-group cs-forms">
                                            <div class="row">
                                                <div class="col-5">
                                                    <input type="hidden" name="loginratetype" id="loginratetype" value="loginratetype">
                                                    <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
                                                    <input type="hidden" name="type" id="type" value="location">
                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;">
                                                        <input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);">
                                                        <input type="text" class="org-del portsnotes tt-input valid" id="ratepol" name="ratepol" placeholder="<bean:message key="Tracking.origin" bundle="<%=clientcode%>"/>" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;" aria-invalid="false">
                                                        <pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre>
                                                        <div class="tt-menu tt-empty" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;">
                                                            <div class="tt-dataset tt-dataset-ports">  </div>
                                                        </div>
                                                    </span>
                                                </div>
                                                <div class="col-1">
                                                    <a href="javascript:swapPortRate();"> <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg"> </a>
                                                </div>
                                                <div class="col-5">
                                                    <input type="hidden" name="typeheadhidden3" id="typeheadhidden3"/>
                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;">
                                                        <input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);">
                                                        <input type="text" class="org-del portsnotes tt-input" id="ratepod" name="ratepod" placeholder="<bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/>" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;">
                                                        <pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre>
                                                        <div class="tt-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;">
                                                            <div class="tt-dataset tt-dataset-ports">
                                                            </div>        
                                                        </div>
                                                    </span> 
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-5">
                                                    <select id="rateseqp" name="rateseqp" class="input_box form-control">
                                                        <option value=""><bean:message key="ecom.login.seleqp" bundle="<%=clientcode%>"/></option>
                                                        <%
                                                            LinkedHashMap eqplist = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                                                            it = null;
                                                            it = eqplist.entrySet().iterator();
                                                            while (it.hasNext()) {
                                                                Map.Entry pairs = (Map.Entry) it.next();

                                                        %>
                                                        <option value="<%=pairs.getKey().toString()%>"><%=pairs.getValue().toString()%></option>
                                                        <%
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                                <div class="col-1">

                                                </div>
                                                <div class="col-5">
                                                    <input type="text" class="ft-date " name="shipmentdate" id="shipmentdate" autocomplete="off" placeholder="<bean:message key="Tariff.shipmentdate" bundle="<%=clientcode%>"/>">
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-5">
                                                    <input class="input_box remove_autocomplete required" name="captchaR" id="captchaR" type="text" placeholder="<bean:message key="Res.captchachar" bundle="<%=clientcode%>"/>" >
                                                </div>
                                                <div class="col-1">

                                                </div>
                                                <div class="col-3">
                                                    <img src="<%=contextRoot%>/CaptchaGeneration?type=LoginRate"  id="captchaImageR" alt="captcha" /> 

                                                </div>

                                                <div class="col-2">
                                                    <a href="javascript:void(0)" title="Reload captcha" class="refresh_cap" id="captchar" onClick="CreateNewCaptcha('rate');"> <i class="fa fa-refresh"></i> </a>
                                                </div>

                                            </div>
                                            <div class="col-5 offset-3">
                                                <button type="submit" class="submit-btn" name="submit" value="Submit"> <bean:message key="ecom.submit" bundle="<%=clientcode%>"/></button>
                                            </div>
                                        </div>
                                </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <p class="copyrights">
                    <span class="<%= "Y".equalsIgnoreCase(centralizeCopyrights) ? "" : "float-md-right " %>d-none d-lg-block"> Copyright &copy; <%=footercopyright%>   </span> 
                </p>
            </div>
        </div>
    </section>
    <script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
    <script type="text/javascript" src="/Ecommaster/routingports.js?<%=jsno%>"></script>

    <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/jquery.validate.min.js?<%=jsdate%>"></script>
    <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/additional-methods.js?<%=jsdate%>"></script>
    <script type="text/javascript" src="Design/assets/plugins/autocomplete/typeahead.bundle.js"></script>
    <script type="text/javascript" src="Design/assets/plugins/datepicker/js/moment.js"></script>
    <script type="text/javascript" src="Design/assets/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>
    <!--        <script type="text/javascript" src="js/CommonValidation.js?<=jsdate>"></script>-->
    <script type="text/javascript" src="/Ecommaster/vessels.js?2"></script>
    <script type="text/javascript">
                                                        $(document).ready(function () {
                                                            $('#userName').attr('placeholder', 'Username');
                                                            $('#password').attr('placeholder', 'Password');
                                                            $("#cs_login").click(function () {
                                                                $(".cs_model").show();
                                                                $(".cs_acc").hide();
                                                                $("#cs_login").addClass("activelogin");
                                                            });
                                                            $(".login-close").click(function () {
                                                                $(".cs_acc").show();
                                                                $(".cs_model").hide();
                                                                $("#cs_login").removeClass("activelogin");
                                                            })
                                                        });
                                                        var contextRoot = "<%=contextRoot%>";
                                                        var remotevalidate = contextRoot + "/CaptchaValidation";
                                                        var capchavalidate = contextRoot + "/CaptchaGeneration?";
                                                        var enableGgleCaptcha = "<%=toDisplayGgleCaptcha%>";
                                                        var portlist = portJson;
                                                        $(document).ready(function () {
                                                            $('#scdshipmentdate').datepicker({
                                                                autoclose: true,
                                                                clearBtn: true,
                                                                todayHighlight: true,
                                                                format: "<%=strDateFormat%>",
                                                                startDate: '-infinity'
                                                            });
                                                            $('#portscdshipmentdate').datepicker({
                                                                autoclose: true,
                                                                clearBtn: true,
                                                                todayHighlight: true,
                                                                format: "<%=strDateFormat%>",
                                                                startDate: '-infinity'
                                                            });
                                                            $('#vesselscdshipmentdate').datepicker({
                                                                autoclose: true,
                                                                clearBtn: true,
                                                                todayHighlight: true,
                                                                format: "<%=strDateFormat%>",
                                                                startDate: '-infinity'
                                                            });
                                                            $('#todateq').datepicker({
                                                                autoclose: true,
                                                                clearBtn: true,
                                                                todayHighlight: true,
                                                                format: "<%=strDateFormat%>",
                                                                startDate: '-infinity'
                                                            });
                                                            $('#porttodateq').datepicker({
                                                                autoclose: true,
                                                                clearBtn: true,
                                                                todayHighlight: true,
                                                                format: "<%=strDateFormat%>",
                                                                startDate: '-infinity'
                                                            });
                                                            $('#vessltodateq').datepicker({
                                                                autoclose: true,
                                                                clearBtn: true,
                                                                todayHighlight: true,
                                                                format: "<%=strDateFormat%>",
                                                                startDate: '-infinity'
                                                            });
                                                            var fromdate = new Date();
                                                            var todate = new Date();
                                                            //fromdate.setDate(fromdate.getDate());
                                                            todate.setDate(fromdate.getDate() + <%=scheduleDateRangeDays%>);
                                                            $('#scdshipmentdate').datepicker("setDate", fromdate);
                                                            $('#todateq').datepicker("setDate", todate);
                                                            $('#portscdshipmentdate').datepicker("setDate", fromdate);
                                                            $('#porttodateq').datepicker("setDate", todate);
                                                            $('#vesselscdshipmentdate').datepicker("setDate", fromdate);
                                                            $('#vessltodateq').datepicker("setDate", todate);
                                                            $('#shipmentdate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>}
                                                            );
                                                            $('#shipmentdate').datepicker("setDate", fromdate);

                                                            $("#loginschedsearch").validate({
                                                                rules: {
                                                                    pol: {
                                                                        required: true
                                                                    },
                                                                    pod: {
                                                                        required: true
                                                                    },
                                                                    scdshipmentdate: {
                                                                        required: true
                                                                    },
                                                                    todateq: {
                                                                        required: true
                                                                    }
                                                                },
                                                                messages: {
                                                                    pol: {
                                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                                    },
                                                                    pod: {
                                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                                    },
                                                                    scdshipmentdate: {
                                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                                    },
                                                                    todateq: {
                                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                                    }

                                                                }, submitHandler: function (form) {

                                                                    return true;
                                                                }
                                                            });
                                                            $("#loginform").validate({
                                                                submitHandler: function (form) {
                                                                    $("#btnsubmit").button('loading');
                                                                    $("#resetbtn").attr("disabled", true);
                                                                    return true;
                                                                }
                                                            });
                                                            $("#logintracsearch").validate({
                                                                rules: {
                                                                    captcha: {
                                                                        required: true,
                                                                        remote: remotevalidate
                                                                    }
                                                                },
                                                                messages: {
                                                                    captcha: {
                                                                        required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                                                        remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                                                    }
                                                                }, submitHandler: function (form) {

                                                                    return true;
                                                                }
                                                            });

                                                            $("#loginrates").validate({
                                                                rules: {
                                                                    ratepol: {
                                                                        required: true
                                                                    },
                                                                    ratepod: {
                                                                        required: true
                                                                    },
                                                                    rateseqp: {
                                                                        required: true
                                                                    },
                                                                    captchaR: {
                                                                        required: true,
                                                                        remote: remotevalidate + "?type=LoginRate"
                                                                    }
                                                                },
                                                                messages: {
                                                                    ratepol: {
                                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                                    },
                                                                    ratepod: {
                                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                                    },
                                                                    rateseqp: {
                                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                                    },
                                                                    captchaR: {
                                                                        required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                                                        remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                                                    }
                                                                }
                                                            });
                                                        });
                                                        $(window).one("load", function () {
                                                            var ports = new Bloodhound({
                                                                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                                                queryTokenizer: Bloodhound.tokenizers.whitespace,
                                                                local: RoutingOrigin
                                                            });
                                                            ports.initialize();
                                                            $('#pol').typeahead(
                                                                    {
                                                                        hint: true,
                                                                        highlight: true,
                                                                        minLength: 2
                                                                    },
                                                                    {
                                                                        name: 'ports',
                                                                        limit: 7,
                                                                        displayKey: function (portlist) {
                                                                            return portlist.PortCode + " - " + portlist.PortName;
                                                                        },
                                                                        source: ports.ttAdapter()
                                                                    }).on('typeahead:render', function (obj, datum) {
                                                                $("#typeheadhidden").val("");
                                                            }).on('typeahead:selected', function (obj, datum) {
                                                                $("#typeheadhidden").val(datum.PortName);
                                                            }).on('typeahead:autocomplete', function (obj, datum) {
                                                                $("#typeheadhidden").val(datum.PortName);
                                                            }).on("typeahead:change", function (obj, datum) {
                                                                var hiddenValue = $("#typeheadhidden").val();
                                                                if (hiddenValue == null || hiddenValue == "") {
                                                                    $('#pol').typeahead('val', "");
                                                                    $(obj.currentTarget).val("");
                                                                }
                                                            });
                                                            ports = new Bloodhound({
                                                                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                                                queryTokenizer: Bloodhound.tokenizers.whitespace,
                                                                local: RoutingDelivery
                                                            });
                                                            $('#pod').typeahead(
                                                                    {
                                                                        hint: true,
                                                                        highlight: true,
                                                                        minLength: 2
                                                                    },
                                                                    {
                                                                        name: 'ports',
                                                                        limit: 7,
                                                                        displayKey: function (portlist) {
                                                                            return portlist.PortCode + " - " + portlist.PortName;
                                                                        },
                                                                        source: ports.ttAdapter()
                                                                    }).on('typeahead:render', function (obj, datum) {
                                                                $("#typeheadhidden1").val("");
                                                            }).on('typeahead:selected', function (obj, datum) {
                                                                $("#typeheadhidden1").val(datum.PortName);
                                                            }).on('typeahead:autocomplete', function (obj, datum) {
                                                                $("#typeheadhidden1").val(datum.PortName);
                                                            }).on("typeahead:change", function (obj, datum) {
                                                                var hiddenValue = $("#typeheadhidden1").val();
                                                                if (hiddenValue == null || hiddenValue == "") {
                                                                    $('#pod').typeahead('val', "");
                                                                    $(obj.currentTarget).val("");
                                                                }
                                                            });

                                                            var ports = new Bloodhound({
                                                                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                                                queryTokenizer: Bloodhound.tokenizers.whitespace,
                                                                local: portlist
                                                            });
                                                            ports.initialize();
                                                            $('#port').typeahead(
                                                                    {
                                                                        hint: true,
                                                                        highlight: true,
                                                                        minLength: 2
                                                                    },
                                                                    {
                                                                        name: 'ports',
                                                                        limit: 7,
                                                                        displayKey: function (portlist) {
                                                                            return portlist.PortCode + " - " + portlist.PortName;
                                                                            console.log("portlist===>" + portlist.PortCode + " - " + portlist.PortName);
                                                                        },
                                                                        source: ports.ttAdapter()
                                                                    }).on('typeahead:render', function (obj, datum) {
                                                                $("#typeheadhidden2").val("");
                                                            }).on('typeahead:selected', function (obj, datum) {
                                                                $("#typeheadhidden2").val(datum.PortName);
                                                            }).on('typeahead:autocomplete', function (obj, datum) {
                                                                $("#typeheadhidden2").val(datum.PortName);
                                                            }).on("typeahead:change", function (obj, datum) {
                                                                var hiddenValue = $("#typeheadhidden2").val();
                                                                if (hiddenValue == null || hiddenValue == "") {
                                                                    $('#port').typeahead('val', "");
                                                                    $(obj.currentTarget).val("");
                                                                }
                                                            });

                                                            var ports = new Bloodhound({
                                                                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('vesselname', 'vesselcode'),
                                                                queryTokenizer: Bloodhound.tokenizers.whitespace,
                                                                local: VesselJson
                                                            });
                                                            ports.initialize();
                                                            $('#vessel').typeahead(
                                                                    {
                                                                        hint: true,
                                                                        highlight: true,
                                                                        minLength: 1
                                                                    },
                                                                    {
                                                                        name: 'ports',
                                                                        limit: 7,
                                                                        displayKey: function (VesselJson) {
                                                                            return VesselJson.vesselcode + " - " + VesselJson.vesselname;
                                                                            console.log("VesselJson==>" + VesselJson.vesselcode + " - " + VesselJson.vesselname);
                                                                        },
                                                                        source: ports.ttAdapter()
                                                                    }).on('typeahead:render', function (obj, datum) {
                                                                $("#typeheadhidden3").val("");
                                                            }).on('typeahead:selected', function (obj, datum) {
                                                                $("#typeheadhidden3").val(datum.vesselname);
                                                            }).on('typeahead:autocomplete', function (obj, datum) {
                                                                $("#typeheadhidden3").val(datum.vesselname);
                                                            }).on("typeahead:change", function (obj, datum) {
                                                                var hiddenValue = $("#typeheadhidden3").val();
                                                                if (hiddenValue == null || hiddenValue == "") {
                                                                    $('#vessel').typeahead('val', "");
                                                                    $(obj.currentTarget).val("");
                                                                }
                                                            });

                                                            ports = new Bloodhound({
                                                                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                                                queryTokenizer: Bloodhound.tokenizers.whitespace,
                                                                local: portlist
                                                            });
                                                            $('#ratepol').typeahead(
                                                                    {
                                                                        hint: true,
                                                                        highlight: true,
                                                                        minLength: 2
                                                                    },
                                                                    {
                                                                        name: 'ports',
                                                                        limit: 7,
                                                                        displayKey: function (portlist) {
                                                                            return portlist.PortCode + " - " + portlist.PortName;
                                                                        },
                                                                        source: ports.ttAdapter()
                                                                    }).on('typeahead:render', function (obj, datum) {
                                                                $("#typeheadhidden2").val("");
                                                            }).on('typeahead:selected', function (obj, datum) {
                                                                $("#typeheadhidden2").val(datum.PortName);
                                                            }).on('typeahead:autocomplete', function (obj, datum) {
                                                                $("#typeheadhidden2").val(datum.PortName);
                                                            }).on("typeahead:change", function (obj, datum) {
                                                                var hiddenValue = $("#typeheadhidden2").val();
                                                                if (hiddenValue == null || hiddenValue == "") {
                                                                    $('#ratepol').typeahead('val', "");
                                                                    $(obj.currentTarget).val("");
                                                                }
                                                            });
                                                            $('#ratepod').typeahead(
                                                                    {
                                                                        hint: true,
                                                                        highlight: true,
                                                                        minLength: 2
                                                                    },
                                                                    {
                                                                        name: 'ports',
                                                                        limit: 7,
                                                                        displayKey: function (portlist) {
                                                                            return portlist.PortCode + " - " + portlist.PortName;
                                                                        },
                                                                        source: ports.ttAdapter()
                                                                    }).on('typeahead:render', function (obj, datum) {
                                                                $("#typeheadhidden3").val("");
                                                            }).on('typeahead:selected', function (obj, datum) {
                                                                $("#typeheadhidden3").val(datum.PortName);
                                                            }).on('typeahead:autocomplete', function (obj, datum) {
                                                                $("#typeheadhidden3").val(datum.PortName);
                                                            }).on("typeahead:change", function (obj, datum) {
                                                                var hiddenValue = $("#typeheadhidden3").val();
                                                                if (hiddenValue == null || hiddenValue == "") {
                                                                    $('#ratepod').typeahead('val', "");
                                                                    $(obj.currentTarget).val("");
                                                                }
                                                            });
                                                        });
                                                        function swapPortRate() {
                                                            var temp = $('#ratepol').val();
                                                            $('#ratepol').typeahead("val", $('#ratepod').val());
                                                            $('#ratepod').typeahead("val", temp);
                                                        }
                                                        function swapPort() {
                                                            var temp = $('#pol').val();
                                                            $('#pol').typeahead("val", $('#pod').val());
                                                            $('#pod').typeahead("val", temp);
                                                        }
                                                        function CreateNewCaptcha(type)
                                                        {
                                                            var d = new Date();
                                                            if (type == 'rate') {
                                                                $("#captchaImageR").attr('src', capchavalidate + "type=LoginRate&version=" + d.getTime());
                                                                $("#captchaR").val('');
                                                            } else {
                                                                $("#captchaImage").attr('src', capchavalidate + d.getTime());
                                                                $("#captcha").val('');
                                                            }

                                                        }
                                                        function openLogin() {
                                                            $(".cs_model").show();
                                                            $(".cs_acc").hide();
                                                            $("#cs_login").addClass("activelogin");
                                                        }
    </script>
    <%
        if (result != null && result.length() > 0 && (!"VALID".equalsIgnoreCase(result))) {
    %>
    <script type="text/javascript">
        $('#loginerror').show();
        $(".cs_model").show();
        $(".cs_acc").hide();
        $("#cs_login").addClass("activelogin");
        $("#resetbtn").attr("disabled", false);
    </script>
    <% }%>
</body>
</html>
