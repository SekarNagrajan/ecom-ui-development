<%@page contentType="text/html"  pageEncoding="UTF-8" import="java.util.Random,java.util.Map,java.util.Iterator,java.util.Hashtable,java.util.List,net.ecom.vo.CustomerAdvisoryVo,java.util.Date,net.ecom.common.GeneralConf,net.ecom.vo.RegisterMenuVO,java.util.LinkedHashMap"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String BG_Image_Name = (String) getServletContext().getAttribute(GeneralConf.BG_Image_Name);
    BG_Image_Name = ((BG_Image_Name != null && BG_Image_Name.trim().length() > 0) ? BG_Image_Name : "pg_T.png");
    String bgimage = imagepath + BG_Image_Name;
    String strDateFormat = (String) getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String contextRoot = request.getContextPath();
    long jsdate = new java.util.Date().getTime();
    List<RegisterMenuVO> menulist = (List<RegisterMenuVO>) getServletContext().getAttribute("usermenu");
    String ratesMenuCategory = menulist.stream().filter(o -> "RAT".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    String trackingMenuCategory = menulist.stream().filter(o -> "TRK".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    String schdMenuCategory = menulist.stream().filter(o -> "SCH".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    Map langmsg = (Map) getServletContext().getAttribute(GeneralConf.Config_Msg_Lang);
    String homemsg = "";
    if (langmsg != null && langmsg.size() > 0) {
        String selectlang = (String) session.getAttribute("SelectedLanguage");
        langmsg = (Map) langmsg.get(selectlang);
        homemsg = (String) langmsg.get("home-home_msg");
    }
    homemsg = (homemsg == null) ? "" : homemsg;
    String pastdateconfig = (String) session.getServletContext().getAttribute(GeneralConf.PAST_DATE_CONFIG);
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String toDisplayGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
    
int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String scheduleDateRangeDaysVal = (String) getServletContext().getAttribute(GeneralConf.Schedule_Date_Range_Days);
    int scheduleDateRangeDays = 30;
    if (scheduleDateRangeDaysVal != null && scheduleDateRangeDaysVal.trim().length() > 0) {
        try { scheduleDateRangeDays = Integer.parseInt(scheduleDateRangeDaysVal.trim()); } catch (Exception e) { scheduleDateRangeDays = 30; }
    }
%>
<style type="text/css">
    body  {
        font-family: 'Lato', sans-serif !important;
        font-size: 14px !important;
        background: #e4dede;

    }
    .app-content.content {
        background: transparent !important;
    }
    /** New css **/

    li.nav-item.col-sm-3 {
        float: left;
        max-width: 33.3%;
        flex: 0 0 33.3%;
        padding: 0;
        border: 2px solid #fff;
    }
    .btn_tra,.btn_rate,.btn_sch {width: 100%;left: 0;padding: 5px;}
    .search_grp {
        /* background: #6564647a;*/
        background: rgba(101, 100, 100 ,0.5);
        border-radius:0;
        margin-left: 0px;
        border-bottom-left-radius: 8px;
        border-bottom-right-radius: 8px;
        box-sizing:border-box;
    }
    button.book-an-app-btn {
        background: #fec526;
        border: none;
        padding: 6px;
        font-weight: bold;
        min-width: 180px;
        border-radius: 5px;
        color:#0e4e99;
        margin-top:15px;
    } 
    .main {
        margin:0;

        min-height:70vh;
    }
    body {
        background-image: url(<%=bgimage + "?" + jsdate%>)  !important;
        background-size: 100% 100% !important;
        background-repeat: no-repeat !important;
    }
    .new-column-cnt h3 {
        color:#0e4b8c;
        font-weight:bold;
    }
    .footer-img {
        padding-top: 10px;
        position: fixed;
        bottom: 30px;
        padding-left: 0;
        padding-right: 0;
        background:#fff;
        z-index: 99;
        height: 100px;
    }
    .header_text a {
        color: #fdc629;
        text-transform: uppercase;
        margin-left: 10px;
    }
    .header_text span {
        /*        color: #0056b3;*/
        font-size: 22px;
        text-transform: capitalize;
    }
    .footer-img img {
        width:100%;
    }
    .new-footer-cnt .col-md-6 {float:left}
    html body .content .content-wrapper{padding:0;}
    .header_text {padding-top:0}
    .header_text img {max-width:180px;}
    .header_text span{display:block}
    .footer.footer-light {color: #4579b0;
                          font-weight: bold;}
    .footer.footer-light a {color: #4579b0 !important;}

    /*mobile view start*/

    @media only screen and (max-width: 980px) {
        .header_text {
            margin-top: 0px; 
        }
        .header_text span {
            font-size: 15px;
        }
        .header_text img {
            max-width: 120px;
        }
        .row .col-sm-8 {margin-left: 0px !important;}
        li.nav-item.col-sm-3 {
            max-width: 48.3%;
            flex: 0 0 48.3%;
            border:none;
        }
        .btn_tra, .btn_rate, .btn_sch {
            border: 2px solid #FFF;
        }
        .txt_top,.txt_top1 {
            padding-left: 0;
            margin-right: 0px !important;
        }
        .swap_btn {
            display: none;
        }
        body .m-l-30 {
            margin-left: 0px !important;
        }
        .footer-img {
            position: static;
            height: auto;
        }
        .app-content.content {
            padding-bottom: 100px !important;
        }
        body .app-content.content {background-image: none !important;}
        .new-column-cnt h3 {
            font-size: 18px;
        }
        body .search_grp {
            width: 100% !important;
        }
        .dropdown.dropdown-language.nav-item.m-t-10 {
            margin-top: 0 !important;
        }

    }
    /*mobile view stop*/


</style>
<style type="text/css">



    @font-face {
        font-family: 'OpenSans-Bold';
        font-style: normal;
        font-weight: 100;
        src: local('OpenSans Bold'), local('OpenSans-Bold'), url(fonts/OpenSans-Bold.ttf) format('ttf');
    }


    body  {
        font-family: 'Lato', sans-serif !important;
        font-size: 14px !important;
        background: #e4dede;
        /*  background: url(../images/bg.png) no-repeat;*/
        background-size: cover;
    }

    .imgmenu {
        height: 45px;
        margin-top: 10px;
    }

    .login_txt {
        float: right;
        margin-top: 30px;
        padding-right: 10px;
        border-right: 2px solid #c1bbbb;
        color: #378c48;
    }
    .bat_txt{
        float: right;
        margin-top: 30px;
        color: #007bff;
        padding-right: 10px;

    }

    .regi_txt {
        float: right;
        margin-top: 30px;
        padding-left: 10px;
        color: #378c48;
    }

    .header_logo {
        margin-top: -30px;
    }

    .main {
        margin-top: 60px;
    }

    .contact_txt a {
        color: #fff;
        font-size:17px;
    }

    .reg_btn {
        color: #ff6633;
        font-weight: bold;
        height: 40px;
        width: 125px;
        border: none;
        border-radius: 4px;
        background: #fff;
        float: right;
    }

    .login_box {
        margin-top: 140px;/*170px*/
    }

    .txt_line {
        font-size: 18px;
        margin-bottom: 10px;
        text-align: center;
    }

    .input_box {
        border: 1px solid #ccc;
        border-radius: 4px;
        height: 50px;
        background: #fff !important;
    }
    .login_btn {
        color: #fff;
        font-weight: bold;
        height: 50px;
        width: 100%;
        border: none;
        border-radius: 4px;
        background: #ff6633;
        margin-bottom: 10px;
        font-size: 18px;
    }

    .forget_txt a {
        text-decoration: underline;

    }
    .dont_txt {

    }
    .rem_txt {
        color: #c4533d !important;
        text-decoration: underline;
    }
    .footer {
        border-top: 1px solid rgba(115, 140, 152, 0.2);
        bottom: 0;
        color: #818a91;
        text-align: left !important;
        padding: 5px;
        position: fixed;
        right: 0;
        left: 0;
        background-color: #fff;
        z-index: 2;
    }
    a .txt_clr {
        color: #0ca4ca !important;
    }

    .ui_ver {
        color: #8a8888; 
        padding-left: 50px;
        font-size: 12px !important
    }
    .copy_txt a {
        color: #8a8888; 
        float: right;
        font-size: 12px !important;
        margin-right: 20px;
    }


    /* Right side bar start*/

    .sidenav_right {
        height: 100%;
        width: 0;
        position: fixed;
        z-index: 1001;
        top: 56px;
        right: 0;
        background-color: #fff;
        overflow-x: hidden;
        transition: 0.5s;
        /*padding-top: 60px;*/
        border: 2px solid #d0caca;
        overflow: auto;
    }

    .sidenav_right .closebtn {
        position: absolute;
        top: 0;
        right: 25px;
        font-size: 36px;
        margin-left: 50px;
    }

    .regi_txt h3 {
        text-transform: uppercase;
        text-align: center;
        font-weight: bold;
    }

    .regi_txt h5 {
        text-align: center;
    }

    .reg_fillds select {
        text-align: center;
    }

    .txt_filds {
        padding-left: 50px !important;
    }


    /* Right side bar end*/

    /*landing page styles start*/

    .header_text {
        color: #378c48;
        text-transform: uppercase;
        font-weight: bold;
        text-align: center;
        padding-top: 15px;
        padding-bottom: 10px;
    }
    .header_text .txt_bold {
        font-weight: bold;
    }
    .header_text .txt_blue {
        color:#0093c0; 
    }

    .header_text .txt_grn {
        color: #29a495;
    }

    .btn_grp {
        text-align: center;
        padding-top: 20px;
    }

    select.form-control {
        background: url(Design/assets/images/page_icons/arrow_down_G.png) no-repeat right #fff !important;
        -webkit-appearance: none;
        background-position-x: 97% !important;
    }

    .btn_sch {
        background: #ededed !important;
        width: 100%;
        color: #333;
        border-radius: 0px !important;
        text-align: center;
        font-weight: bold;
        font-size: 18px;
    }
    .btn_tra {
        background: #ededed !important;
        width: 100%;
        color: #333;
        border-radius: 0px !important;
        text-align: center;
        font-weight: bold;
        font-size: 18px;
        left: 0px;
        position: relative;
    }

    .btn_rate {
        background:#ededed !important;
        width: 100%;
        color: #333;
        border-radius: 0px !important;
        text-align: center;
        font-weight: bold;
        font-size: 18px;
        left: 0px;
        position: relative;
        /* border-radius: 5px; */
    }

    .btn_icon {
        width: 24px;
        margin-right: 5px;
    }

    .btn_icon_arrow {
        width: 40px;
        height: 40px;
        margin-left: 10px;
    }

    .search_grp {
        /* padding-left: 20px; */
        margin-left: 14px;
        background: #ffffff;
        /*border-top-right-radius: 40px;
        border-bottom-left-radius: 40px;*/
    }

    .input_icon_login {
        width: 20px;
        height: 20px;
        position: absolute;
        float: right;
        right: 20px;
        top: 15px;
    }

    .btn-sub {
        background: #378c48;
        color: #fff;
        width: 100%;
        height: 45px;
        /*margin-left: 15px;*/
    }

    .right_icon {
        width: 50px;
        height: 40px;
        margin-left: 20px;
    }
    .txt_top {
        padding-top: 40px;
        padding-left: 25px;
        padding-bottom: 15px;
    }

    .txt_top1 {
        padding-bottom: 35px;
        padding-left: 25px;
    }

    .Search_btn {

    }

    /*End*/

    .daterangepicker .calendar {
        margin: 0px !important;
        padding: 3px !important;
    }

    .daterangepicker .calendar-time {
        margin: 2px 0px !important;
    }

    .prev.available:after {
        content: '\f100';
        font-family: FontAwesome;
        font-size: 24px;
        color: #000;
        font-weight: bold !important;
    }


    .next.available:after {
        content: '\f101';
        font-family: FontAwesome;
        font-size: 24px;
        color: #000;
        font-weight: bold !important;
    }

    .daterangepicker .table-condensed .month {
        font-weight: bold !important;
    }

    .daterangepicker th {
        color: #151414 !important;
        font-weight: bold !important;
    }

    .daterangepicker td.active, .daterangepicker td.active:focus, .daterangepicker td.active:hover {
        border-radius: 22px !important;
    } 

    .daterangepicker  table tr td.today.available {
        color: #000;
        background-color: #f7ca77;
        border-color: #f1a417;
        border-radius: 22px;
    }

    /*.daterangepicker td.available:focus, .daterangepicker td.available:hover {
        background-color: #aba7a7 !important;
        border-radius: 22px !important;
        color: #000 !important;
    }*/

    .refresh_cap {
        font-size: 25px;
        vertical-align: middle;
        margin-left: 10px;
    }

    /*.data_tabs {
      margin-bottom: 5px;
    }*/

    .input_box1 {
        height: 50px; 
    }

    ::placeholder {
        color: #000 !important;
    }

    .custom-form-group {
        margin-bottom: 10px;
    }

    .nav-pills .nav-link.active {
        color: #333 !important;
        background-color: #ededed !important;
    } 

    #RegistrationForm .control-label {
        color: #3f3fea !important;
    }

    .main-menu.menu-fixed.login_menu {
        top:73px;
    }

    .sidenav-overlay.login_overlay_top {
        top: 73px;
    }

    /* Mobile responsive  screen size Start */

    @media only screen and (max-width: 600px) and (min-width: 360px)  {

        .container-fluid {
            margin-bottom: 300px !important;
        }

        .btn_sch, .btn_tra, .btn_rate {
            margin-bottom: 10px;
        }

        .btn_tra, .btn_rate {
            left: 0px !important;
            width: 120% !important;
        }

        .search_grp {
            width: 136% !important;
            margin-left: -32px;
        }

        .login_box {
            margin-left: 80px !important;
        }

        #company_country_code, #company_phone_code, #company_phone_no, #cust_country_code, #cust_phone_code, #cust_phone_no, #mobile_code, #fax_country_code, #fax_phone_code {
            margin-bottom: 8px;
        }

        .input_box {
            margin-bottom: 10px !important;
        }

        .app-content .wizard>.steps>ul>li {
            z-index: 0 !important;
        }

        #RegistrationForm .btn-lg {
            font-size: 16px !important;
            top: 51px !important;
            position: absolute;
            right: 4px !important;
        }

        .app-content #RegistrationForm.wizard>.steps>ul>li.last {
            margin-left: -134px;
            margin-top: 6px;
        }

        .login_txt {
            margin-top: 18px;
        }
        .regi_txt {
            margin-bottom: 13px;
            margin-top: 18px;
        }
        .sidenav_right .closebtn {
            top: 21px;
        }
        .page_bottom_content_regi {
            margin-left: -20px;
            margin-right: -20px;
        }

        .header_text {
            margin-top: -75px;
            padding-bottom: 0px;
            padding-top: 5px
        }

    }

    /* Mobile responsive  screen size End */

    /*Ipad Responsive  Screen Size Start */

    @media only screen and (max-width: 1024px) and (min-width: 768px)  {

        .main {
            margin-top: 0px !important;
        }

        .btn_sch, .btn_tra, .btn_rate  {
            width: 142% !important;
        }

        body {
            background-size: auto !important;
        }

        .header_text {
            padding-top: 20px;
            padding-bottom: 0px;
        }

        .txt_top .col-sm-4, .txt_top1 .col-sm-4 {
            flex: 0 0 50% !important;
            width: 50% !important;
            max-width: 50% !important;
        }

        .container-fluid {
            margin-bottom: 600px !important;
        }
    }
    /*Ipad Responsive  Screen Size End */   
    .selected-language,i.fa-language {
        color: #378C48 !important;
    }
    a, a:hover, a:focus {
        color: #378C48;
    }
    .refresh_cap {
        color: #378C48;
    }
    .data_tabs {
        margin-left: 14px;
    }
</style>
<div class="main"> 
    <div class="header_text">
        <img src="<%=imagepath%>chelsea.png"/>
        <span>CHELSEA GETS YOU THERE!</span>
    </div>


    <div class="row col-sm-12">
        <div class="col-sm-2 " style="max-width: 16%"> </div>
        <div class="col-sm-8 " >
            <ul class="nav nav-pills data_tabs" role="tablist">
                <li class="nav-item col-sm-3">
                    <a class="nav-link active btn_sch" <%=(("P".equalsIgnoreCase(schdMenuCategory)) ? " onclick='openNav()' href='#' " : " data-toggle='pill' href='#schedule' ")%> > <img class="btn_icon" src="Design/assets/images/menu_icons/schedule_G.png"/> <bean:message key="ecom.login.scdhead" bundle="<%=clientcode%>"/></a>
                </li>

                <li class="nav-item col-sm-3">
                    <a class="nav-link btn_tra" <%=(("P".equalsIgnoreCase(trackingMenuCategory)) ? " onclick='openNav()' href='#' " : " data-toggle='pill' href='#trackers' ")%> >  <img class="btn_icon" src="Design/assets/images/menu_icons/location_G.png"/>  <bean:message key="ecom.login.trckhead" bundle="<%=clientcode%>"/></a></li>

                <li class="nav-item col-sm-3">
                    <a class="nav-link btn_rate" <%=(("P".equalsIgnoreCase(ratesMenuCategory)) ? " onclick='openNav()' href='#' " : " data-toggle='pill' href='#rates' ")%> > <img class="btn_icon" src="Design/assets/images/menu_icons/tag_rates_G.png"/><bean:message key="ecom.rates" bundle="<%=clientcode%>"/></a>
                </li>

            </ul>
        </div>
    </div>

    <div class="row col-sm-12 ">
        <div class="col-sm-2" style="max-width: 16%"> </div>
        <div class="col-sm-8">
            <div class="search_grp">
                <div class="tab-content">
                    <div id="schedule" class="container tab-pane active">
                        <form id="loginschedsearch" name="loginschedsearch" method="post" class="form-horizontal" action="schedules.do" >
                            <div class="txt_top" style="margin-right: -95px;">
                                <div class="row">
                                    <div class="col-sm-5">
                                        <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>
                                        <input type="hidden" name="schetype" id="schetype" value="loginschedule"/>
                                        <input type="hidden" name="type" id="type" value="location"/>
                                        <input type="text" class="input_box form-control required portsnotes" id='pol'  name="pol" placeholder="<bean:message key="Tracking.origin" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker_G.svg">
                                    </div>
                                    <a href="javascript:swapPort();" > <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" /> </a>
                                    <div class="col-sm-5">
                                        <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
                                        <input type="text" class="input_box form-control required portsnotes" id='pod' name="pod" placeholder="<bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker_G.svg"> 
                                    </div>
                                </div>
                            </div>

                            <div class="txt_top1" style="margin-right: -95px;">
                                <div class="row">
                                    <div class="col-sm-5">
                                        <input type="text" class="input_box form-control required"  name="scdshipmentdate" id='scdshipmentdate' autocomplete="off" placeholder="<bean:message key="Schd.frdate" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/date_G.png"> 
                                    </div>
                                    <div class="col-sm-5 m-l-30">
                                        <input type="text" class="input_box form-control required" id="todateq" name="todate"  autocomplete="off"  placeholder="<bean:message key="Schd.todate" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/date_G.png"> 
                                    </div>
                                    <div class="col-sm-2 m-t-10 offset-md-4">
                                        <button type="submit" class="btn btn-sub" name="submit" value="Submit"> <bean:message key="ecom.submit" bundle="<%=clientcode%>"/></button>
                                    </div>
                                </div>

                            </div>
                        </form>
                    </div>

                    <div id="trackers" class="container tab-pane">
                        <form id="logintracsearch" name="logintracsearch" method="post" class="form-horizontal" action="tracking.do" >
                            <div class="txt_top">
                                <div class="row form-group">
                                    <div class="col-sm-8">
                                        <input type="hidden" name="tracktype" id="tracktype" value="logintracking"/>
                                        <input class="input_box form-control required" type="text" placeholder="<bean:message key="ecom.login.blorbknum" bundle="<%=clientcode%>"/>" id="logintracno" name="logintracno"> 
                                    </div>
                                </div>
                                <%if (!"Y".equalsIgnoreCase(toDisplayGgleCaptcha)) {%>       
                                <div class="row form-group">
                                    <div class="col-sm-5">
                                        <input class="input_box form-control remove_autocomplete required" name="captcha" id="captcha" type="text" placeholder="<bean:message key="Res.captchachar" bundle="<%=clientcode%>"/>" >
                                    </div>

                                    <div class="col-sm-3">
                                        <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage" alt="captcha" /> 
                                        <a href="javascript:void(0)" title="Reload captcha" class="refresh_cap" id="captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');"> <i class="fa fa-refresh"></i> </a>
                                    </div>

                                    <div class="col-sm-2">
                                        <button type="submit" class="btn btn-sub"> <bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                                    </div>
                                </div>
                                <%} else {%>
                                <div class="row form-group">

                                    <div class="col-sm-5">
                                        <div id="recaptiid" class="g-recaptcha" styleClass="form-control light_border required" data-sitekey="<%=siteKey%>"    required="true" data-expired-callback="recaptchaExpired" data-callback="verifyCaptcha"></div>
                                        <div id="g-recaptcha-error"></div>  
                                    </div>
                                    <div class="col-sm-2">
                                        <button type="submit" class="btn btn-sub" onclick="checkGoogleRecaptcha();"> <bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                                    </div>
                                </div>
                                <%}%>
                            </div>
                        </form>
                    </div>

                    <div id="rates" class="container tab-pane ">
                        <form id="loginrates" name="loginrates" method="post" class="form-horizontal" action="Accesslogin.do?type=rates" >
                            <div class="txt_top">
                                <div class="row custom-form-group">
                                    <div class="col-sm-5">
                                        <input type="hidden" name="loginratetype" id="loginratetype" value="loginratetype"/>
                                        <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
                                        <input class="input_box form-control portsnotes required" id="ratepol" name="ratepol" type="text" placeholder="<bean:message key="Tracking.origin" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker_G.svg">
                                    </div>
                                    <a href="javascript:swapPortRate();" > <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" /> </a>
                                    <div class="col-sm-5">
                                        <input type="hidden" name="typeheadhidden3" id="typeheadhidden3"/>
                                        <input class="input_box form-control portsnotes required"  type="text" id="ratepod" name="ratepod" placeholder="<bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker_G.svg"> 
                                    </div>
                                </div>

                                <div class="row custom-form-group">
                                    <div class="col-sm-5">
                                        <select id="rateseqp" name="rateseqp" class="input_box form-control">
                                            <option value=""><bean:message key="ecom.login.seleqp" bundle="<%=clientcode%>"/></option>
                                            <%
                                                LinkedHashMap eqplist = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                                                Iterator it = null;
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

                                    <div class="col-sm-5 m-l-30">
                                        <input class="input_box form-control required" type="text" id="shipmentdate" name="shipmentdate" autocomplete="off" placeholder="<bean:message key="Tariff.shipmentdate" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/date_G.png">
                                    </div>
                                </div>

                                <div class="row custom-form-group">
                                    <div class="col-sm-5">
                                        <input class="input_box form-control remove_autocomplete required" name="captchaR" id="captchaR" type="text" placeholder="<bean:message key="Res.captchachar" bundle="<%=clientcode%>"/>" >
                                    </div>

                                    <div class="col-sm-3 m-l-30" style="max-width:24%;">
                                        <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImageR" alt="captcha" /> 
                                        <a href="javascript:void(0)" title="Reload captcha" class="refresh_cap" id="captchar" onClick="CreateNewCaptcha('rate');"> <i class="fa fa-refresh"></i> </a>
                                    </div>

                                    <div class="col-sm-2">
                                        <button type="submit" class="btn btn-sub" name="submit" value="Submit"> <bean:message key="ecom.submit" bundle="<%=clientcode%>"/></button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


</div>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/routingports.js?<%=jsno%>"></script>
<script type="text/javascript" >
                                            var contextRoot = "<%=contextRoot%>";
                                            var remotevalidate = contextRoot + "/CaptchaValidation";
                                            var capchavalidate = contextRoot + "/CaptchaGeneration?";
                                            var enableGgleCaptcha = "<%=toDisplayGgleCaptcha%>";
                                            var portlist = portJson;
                                            $(document).ready(function () {
                                            $('#scdshipmentdate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>});
                                            $('#todateq').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>});
                                            var fromdate = new Date();
                                            var todate = new Date();
                                            //fromdate.setDate(fromdate.getDate());
                                            todate.setDate(fromdate.getDate() + <%=scheduleDateRangeDays%>);
                                            $('#scdshipmentdate').datepicker("setDate", fromdate);
                                            $('#todateq').datepicker("setDate", todate);
                                            $('#shipmentdate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>});
                                            $('#shipmentdate').datepicker("setDate", fromdate);
                                            $("#loginschedsearch").validate();
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
                                                    },
                                                    submitHandler: function (form) {
                                                    if (enableGgleCaptcha == 'Y') {
                                                    var captchaval = $('#g-recaptcha-response').val();
                                                    if ($('#g-recaptcha-response').length == 0 || captchaval == null || captchaval == 'null' || captchaval == '' || captchaval.length == 0) {
                                                    return false;
                                                    }
                                                    }
                                                    return true;
                                                    }

                                            });
                                            $("#loginrates").validate({
                                            rules: {
                                            captchaR: {
                                            required: true,
                                                    remote: remotevalidate + "?type=LoginRate"
                                            }
                                            },
                                                    messages: {
                                                    captchaR: {
                                                    required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                                            remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                                    }
                                                    }
                                            });
                                            }
                                            );
                                            var portlist = portJson;
                                            //<![CDATA[
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
                                                    limit: 20,
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
                                                    limit: 20,
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
                                                    limit: 20,
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
                                                    limit: 20,
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
                                            function swapPort() {
                                            var temp = $('#pol').val();
                                            $('#pol').typeahead("val", $('#pod').val());
                                            $('#pod').typeahead("val", temp);
                                            }
                                            function swapPortRate() {
                                            var temp = $('#ratepol').val();
                                            $('#ratepol').typeahead("val", $('#ratepod').val());
                                            $('#ratepod').typeahead("val", temp);
                                            }
                                            function checkGoogleRecaptcha() {

                                            var response = grecaptcha.getResponse();
                                            if (response.length == 0) {
                                            document.getElementById('g-recaptcha-error').innerHTML = '<span style="color:red;"><bean:message key="ecom.gglecaptcha.err" bundle="<%=clientcode%>"/></span>';
                                            return false;
                                            }
                                            return true;
                                            }

                                            function verifyCaptcha() {
                                            document.getElementById('g-recaptcha-error').innerHTML = '';
                                            }
                                            function recaptchaExpired() {
                                            alert("<bean:message key="ecom.gglecaptcha.expired" bundle="<%=clientcode%>"/>");
                                            }

</script>