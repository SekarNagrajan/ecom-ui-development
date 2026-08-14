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
    System.out.println("[ECOM-WAR] login_C logintype====> " + logintype+ " logintype.length()====> " + logintype.length());
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
            }


            #collapseOne ul {
                list-style: none;
                padding: 0;
                margin: 0;
                text-align: center;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            #collapseOne li.nav-item {
                font-size: 14px;
                font-weight: 500;
                color: rgba(0,0,0,.87);
                display: inline-block;
                margin: 1px;
                padding: 10px;
                border-radius: 5px;
                vertical-align: middle;
            }

            #collapseOne li.nav-item .active {font-size: 14px;font-weight: 500;color: #3F51B5;border-bottom: 2px solid #3F51B5 !important;}

            #collapseOne .tab-content{
                display: block !IMPORTANT;
            }
            #collapseOne .nav-tabs {
                border-bottom: none;
            }

            #collapseOne .nav-link:hover {
                background-color: #fff;
            }

            #collapseOne .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active {
                background-color: #fff;
                border-color: #fff;
            }
            #collapseOne .nav-tabs .nav-link:focus, .nav-tabs .nav-link:hover {
                border-color: #fff;
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
                <img src="/Ecommaster/logo_C.png" alt="cstarline">
                <p class="copyrights">
                    <span class="float-md-right d-none d-lg-block"> Copyright &copy; <%=footercopyright%>   </span> </p>
            </div>

            <div class="cs_right">
                <div class="cs_right_main">
                    <div class="cs_nav navbar-container content">
                        <nav class="">
                            <ul>
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
                        <div class="cs_model_sec m-4" style="margin-top: 4% !important;">
                            <div class="login-close"><a href="javascript:;" ><img src="Design/assets/images/homePage_icons/cancel.png" alt="close"></a></div>
                            <h2 class="title-sign" style="margin-top: 8% !important;">SIGN IN</h2>
                            <div class="form-group row col-sm-12" id="loginerror" style="margin-left: 4px;display:none">
                                <div class="alert alert-danger text-center">
                                    <strong><bean:message key="ecom.login.invup" bundle="<%=clientcode%>"/></strong>  
                                </div>
                            </div>
                            <html:form styleId="loginform" method="post" action="Accesslogin.do?type=login" styleClass="form-horizontal">
                                <div class="form-group cs-forms">
                                    <div class="row">
                                        <div class="col-12">
                                            <html:text property="userName"  maxlength="50" styleClass = "input_box1 required" />
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-12">
                                            <html:password property="password"  maxlength="20" styleClass="input_box1 required" />
                                        </div>
                                    </div>
                                    <div class="col-12">
                                        <button class="submit-btn" id="loginbtn" type="submit" autocomplete="off"><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></button>
                                    </div>
                                </div>
                            </html:form>
                            <div class="col-12 forgot-reg">
                                <p>
                                    <a href="javascript:;" class="cs_forgot"><span class="forget_txt">  <a href ="Accesslogin.do?type=forgot"><bean:message key="ForgottenPassword.Header.ForgotPassword" bundle="<%=clientcode%>"/> </a> </span></a>
                                </p>
                                <p class="m-1">
                                    <span>Don't have an account?<a href="javascript:;"><span class="dont_txt"><bean:message key="ecom.login.havingnoacc" bundle="<%=clientcode%>"/> <a href ="Accesslogin.do?type=Registration"  class="rem_txt" ><bean:message key="ecom.toregister" bundle="<%=clientcode%>"/> </a> </span></a></span>
                                </p>
                            </div>
                        </div>

                    </div>  
                    <%}%>
                    <div class="cs_acc">
                        <div class="m-5">
                            <div class="accordion" id="myAccordion">
                                <div class="accordion-item">
                                    <h2 class="accordion-header" id="headingOne">
                                        <button type="button" class="accordion-button " data-bs-toggle="collapse" data-bs-target="#collapseOne">

                                            <img src="Design/assets/images/homePage_icons/calendar1.png"><bean:message key="ecom.login.scdhead" bundle="<%=clientcode%>"/></button>									
                                    </h2>
                                    <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#myAccordion">
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" role="tablist" >
                                                <li class="nav-item">
                                                    <a class="nav-link active" data-bs-toggle="tab" href="#betweenloctab" role="tab"><bean:message key="ecom.betweenloc" bundle="<%=clientcode%>"/></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link " data-bs-toggle="tab" href="#byorigintab" role="tab"><bean:message key="ecom.byport" bundle="<%=clientcode%>"/></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" data-bs-toggle="tab" href="#byvesseltab" role="tab"><bean:message key="ecom.byvessel" bundle="<%=clientcode%>"/></a>
                                                </li>

                                            </ul><!-- Tab panes -->
                                            <div class="tab-content ">
                                                <div class="tab-pane active" id="betweenloctab" role="tabpanel">
                                                    <form id="loginschedsearch" name="loginschedsearch" method="post" class="form-horizontal" action="schedules.do" novalidate="novalidate">
                                                        <div class="form-group cs-forms">
                                                            <div class="row">
                                                                <div class="col-5">
                                                                    <input type="hidden" name="typeheadhidden" id="typeheadhidden" value="">
                                                                    <input type="hidden" name="schetype" id="schetype" value="loginschedule">
                                                                    <input type="hidden" name="type" id="type" value="location">
                                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;"><input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;https://ecom-cstarline.solverminds.net/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);"><input type="text" class="org-del portsnotes tt-input valid" id="pol" name="pol" placeholder="Origin" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;" aria-invalid="false"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><div class="tt-menu tt-empty" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;"><div class="tt-dataset tt-dataset-ports"></div></div></span>
                                                                </div>
                                                                <div class="col-1">
                                                                    <a href="javascript:swapPort();"> <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg"> </a>
                                                                </div>
                                                                <div class="col-5">
                                                                    <input type="hidden" name="typeheadhidden1" id="typeheadhidden1">
                                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;"><input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;https://ecom-cstarline.solverminds.net/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);"><input type="text" class="org-del portsnotes tt-input" id="pod" name="pod" placeholder="Delivery" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><div class="tt-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;"><div class="tt-dataset tt-dataset-ports"></div></div></span> 

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
                                                            <div class="col-12">
                                                                <button type="submit" class="submit-btn" name="submit" value="Submit"> Submit</button>

                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="tab-pane" id="byorigintab" role="tabpanel">
                                                    <form id="loginschedbyport" name="loginschedbyport" method="post" class="form-horizontal" action="byport.do" >
                                                        <div class="form-group cs-forms">
                                                            <div class="row">
                                                                <div class="col-5">
                                                                    <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
                                                                    <input type="hidden" name="porttype" id="porttype" value="loginschedbyport"/>
                                                                    <input type="hidden" name="type" id="type1" value="location"/>
                                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;"><input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;https://ecom-cstarline.solverminds.net/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);"><input type="text" class="org-del portsnotes tt-input valid" id="port" name="port" placeholder="Origin" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;" aria-invalid="false"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><div class="tt-menu tt-empty" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;"><div class="tt-dataset tt-dataset-ports"></div></div></span>
                                                                    <!--                                                                    <input type="text" class="org-del portsnotes " id='port'  name="port" placeholder="" >-->
                                                                </div>
                                                                <div class="col-1">

                                                                </div>
                                                                <div class="col-5">
                                                                    <input type="text" class="ft-date " name="portscdshipmentdate" id='portscdshipmentdate' autocomplete="off" placeholder="<bean:message key="Schd.frdate" bundle="<%=clientcode%>"/>" >

                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-5">
                                                                    <input type="text" class="ft-date " id="porttodateq" name="porttodateq"  autocomplete="off"  placeholder="<bean:message key="Schd.todate" bundle="<%=clientcode%>"/>" >
                                                                </div>
                                                            </div>
                                                            <div class="col-12">
                                                                <button type="submit" class="submit-btn" name="submit" value="Submit"> <bean:message key="ecom.submit" bundle="<%=clientcode%>"/></button>

                                                            </div>
                                                        </div>
                                                    </form>  
                                                </div>
                                                <div class="tab-pane" id="byvesseltab" role="tabpanel">
                                                    <form id="loginbyvesselsearch" name="loginbyvesselsearch" method="post" class="form-horizontal" action="byvessel.do" >
                                                        <div class="form-group cs-forms">
                                                            <div class="row">
                                                                <div class="col-5">
                                                                    <input type="hidden" name="typeheadhidden3" id="typeheadhidden3"/>
                                                                    <input type="hidden" name="vesseltype" id="vesseltype" value="loginbyvesselsearch"/>
                                                                    <input type="hidden" name="type" id="type2" value="location"/>
                                                                    <span class="twitter-typeahead" style="position: relative; display: inline-block;"><input type="text" class="org-del portsnotes tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: url(&quot;https://ecom-cstarline.solverminds.net/ecomonline/Design/assets/images/homePage_icons/location.png&quot;) 97% 8px / 18px 18px no-repeat scroll padding-box border-box rgb(237, 239, 245);"><input type="text" class="org-del portsnotes tt-input valid"  id="vessel"  name="vessel"  placeholder="<bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/>" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;" aria-invalid="false"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: Poppins, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><div class="tt-menu tt-empty" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;"><div class="tt-dataset tt-dataset-ports"></div></div></span>
                                                                    <!--                                                                    <input type="text" class="org-del portsnotes " id='vessel'  name="vessel" placeholder="" >-->
                                                                </div>
                                                                <div class="col-1">

                                                                </div>
                                                                <div class="col-5">
                                                                    <input type="text" class="ft-date " name="vesselscdshipmentdate" id='vesselscdshipmentdate' autocomplete="off" placeholder="<bean:message key="Schd.frdate" bundle="<%=clientcode%>"/>" >

                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-5">
                                                                    <input type="text" class="ft-date " id="vessltodateq" name="vessltodateq"  autocomplete="off"  placeholder="<bean:message key="Schd.todate" bundle="<%=clientcode%>"/>" >
                                                                </div>
                                                            </div>
                                                            <div class="col-12">
                                                                <button type="submit" class="submit-btn" name="submit" value="Submit"> <bean:message key="ecom.submit" bundle="<%=clientcode%>"/></button>

                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                                <div class="accordion-item">
                                    <h2 class="accordion-header" id="headingTwo">
                                        <button type="button" class="accordion-button collapsed" data-bs-toggle="collapse" data-bs-target="#collapseTwo"><img src="Design/assets/images/homePage_icons/tracker.png" style="max-width: 22px;">Tracking</button>
                                    </h2>
                                    <div id="collapseTwo" class="accordion-collapse collapse " data-bs-parent="#myAccordion">
                                        <div class="card-body">
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
                                                    <div class="col-12">
                                                        <button type="submit" class="submit-btn"> <bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>

                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
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

                $("#loginschedbyport").validate({
                    rules: {
                        port: {
                            required: true
                        }
                    },
                    messages: {
                        port: {
                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                        },
                        scdshipmentdate: {
                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                        },
                        porttodateq: {
                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                        }
                    }, submitHandler: function (form) {
                        return true;
                    }
                });
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
                $("#loginbyvesselsearch").validate({
                    rules: {
                        vessel: {
                            required: true
                        }
                    },
                    messages: {
                        vessel: {
                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                        },
                        vesselscdshipmentdate: {
                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                        },
                        vessltodateq: {
                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                        }
                    }, submitHandler: function (form) {

                        return true;
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
                            limit: 10,
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
                            limit: 10,
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
                            limit: 20,
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
                            limit: 5,
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
            });

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
