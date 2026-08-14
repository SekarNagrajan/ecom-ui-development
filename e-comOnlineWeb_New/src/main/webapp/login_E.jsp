<%@page contentType="text/html"  pageEncoding="UTF-8" import="java.util.Random,java.util.Map,java.util.Iterator,java.util.Hashtable,java.util.List,net.ecom.vo.CustomerAdvisoryVo,java.util.Date,net.ecom.common.GeneralConf,net.ecom.vo.RegisterMenuVO,java.util.LinkedHashMap"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String BG_Image_Name = (String) getServletContext().getAttribute(GeneralConf.BG_Image_Name);
    BG_Image_Name = ((BG_Image_Name != null && BG_Image_Name.trim().length() > 0) ? BG_Image_Name : "bg1.jpg");
    String bgimage = imagepath + BG_Image_Name;
    String strDateFormat = (String) getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String contextRoot = request.getContextPath();
    long jsdate = new java.util.Date().getTime();
    List<RegisterMenuVO> menulist = (List<RegisterMenuVO>) getServletContext().getAttribute("usermenu");
    String ratesMenuCategory = menulist.stream().filter(o -> "RAT".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    String trackingMenuCategory = menulist.stream().filter(o -> "TRK".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    String schdMenuCategory = menulist.stream().filter(o -> "SCH".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
    Map langmsg = (Map) getServletContext().getAttribute(GeneralConf.Config_Msg_Lang);
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String toDisplayGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
    String homemsg = "";
    if (langmsg != null && langmsg.size() > 0) {
        String selectlang = (String) session.getAttribute("SelectedLanguage");
        langmsg = (Map) langmsg.get(selectlang);
        homemsg = (String) langmsg.get("home-home_msg");
    }
    homemsg = (homemsg == null) ? "" : homemsg;
    String pastdateconfig = (String) session.getServletContext().getAttribute(GeneralConf.PAST_DATE_CONFIG);
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
    .app-content.content {
        background-image: url(<%=bgimage + "?" + jsdate%>)  !important;
        background-size: 100% 100% !important;
        background-position: 0 0 !important;
        background-repeat: no-repeat !important;
        min-height: calc(100% - 88px);
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
    .header_text .smarter {
        color: #fdc629;
        text-transform: uppercase;
        margin-left: 10px;
    }
    .header_text span {
        color: #0056b3;
        font-size: 18px;
        text-transform: capitalize;
    }
    .footer-img img {
        width:100%;
    }
    .new-footer-cnt .col-md-6 {float:left}
    html body .content .content-wrapper{padding:0;}
    .header_text {padding-top:0}
    .header_text img {max-width:180px;}
    .header_text .headertext{display:block}
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
<div class="main"> 
    <div class="header_text">
        <img src="<%=imagepath%>e_sea.png?<%=jsdate%>" />
        <span class="headertext" ><bean:message key="ecom.home.header.prefix" bundle="<%=clientcode%>"/> <span class="smarter" ><bean:message key="ecom.home.header.suffix" bundle="<%=clientcode%>"/></span></span>
    </div>

    <div class="row col-sm-12">
        <div class="col-sm-2 " style="max-width: 18.6%"> </div>
        <div class="col-sm-8 " style="margin-left: 150px;">
            <ul class="nav nav-pills data_tabs" role="tablist">
                <li class="nav-item col-sm-3">
                    <a class="nav-link active btn_sch" <%=(("P".equalsIgnoreCase(schdMenuCategory)) ? " onclick='openNav()' href='#' " : " data-toggle='pill' href='#schedule' ")%> > <img class="btn_icon" src="Design/assets/images/menu_icons/schedule.png"/> <bean:message key="ecom.login.scdhead" bundle="<%=clientcode%>"/></a>
                </li>

                <li class="nav-item col-sm-3">
                    <a class="nav-link btn_tra" <%=(("P".equalsIgnoreCase(trackingMenuCategory)) ? " onclick='openNav()' href='#' " : " data-toggle='pill' href='#trackers' ")%> >  <img class="btn_icon" src="Design/assets/images/menu_icons/location.png"/>  <bean:message key="ecom.login.trckhead" bundle="<%=clientcode%>"/></a></li>

                <li class="nav-item col-sm-3 d-none">
                    <a class="nav-link btn_rate" <%=(("P".equalsIgnoreCase(ratesMenuCategory)) ? " onclick='openNav()' href='#' " : " data-toggle='pill' href='#rates' ")%> > <img class="btn_icon" src="Design/assets/images/menu_icons/tag_rates.png"/><bean:message key="ecom.rates" bundle="<%=clientcode%>"/></a>
                </li>

            </ul>
        </div>
    </div>

    <div class="row col-sm-12 ">
        <div class="col-sm-2" style="max-width: 18.6%"> </div>
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
                                        <input type="text" class="input_box form-control portsnotes required" id='pol'  name="pol" placeholder="<bean:message key="Tracking.origin" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker.svg">
                                    </div>
                                    <a href="javascript:swapPort();" > <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" /> </a>
                                    <div class="col-sm-5">
                                        <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
                                        <input type="text" class="input_box form-control portsnotes required" id='pod' name="pod" placeholder="<bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker.svg"> 
                                    </div>
                                </div>
                            </div>

                            <div class="txt_top1" style="margin-right: -95px;">
                                <div class="row">
                                    <div class="col-sm-5">
                                        <input type="text" class="input_box form-control required"  name="scdshipmentdate" id='scdshipmentdate' autocomplete="off" placeholder="<bean:message key="Schd.frdate" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/date.png"> 
                                    </div>
                                    <div class="col-sm-5 m-l-30">
                                        <input type="text" class="input_box form-control required" id="todateq" name="todate"  autocomplete="off"  placeholder="<bean:message key="Schd.todate" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/date.png"> 
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
                                        <input class="input_box form-control portsnotes required" id="ratepol" name="ratepol" type="text" placeholder="<bean:message key="Tracking.origin" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker.svg">
                                    </div>
                                    <a href="javascript:swapPortRate();" > <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" /> </a>
                                    <div class="col-sm-5">
                                        <input type="hidden" name="typeheadhidden3" id="typeheadhidden3"/>
                                        <input class="input_box form-control portsnotes required"  type="text" id="ratepod" name="ratepod" placeholder="<bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/marker.svg"> 
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
                                        <input class="input_box form-control required" type="text" id="shipmentdate" name="shipmentdate" autocomplete="off" placeholder="<bean:message key="Tariff.shipmentdate" bundle="<%=clientcode%>"/>" > <img class="input_icon_login" src="Design/assets/images/page_icons/date.png">
                                    </div>
                                </div>

                                <div class="row custom-form-group">
                                    <div class="col-sm-5">
                                        <input class="input_box form-control remove_autocomplete required" name="captchaR" id="captchaR" type="text" placeholder="<bean:message key="Res.captchachar" bundle="<%=clientcode%>"/>" >
                                    </div>

                                    <div class="col-sm-3 m-l-30" style="max-width:24%;">
                                        <img src="<%=contextRoot%>/CaptchaGeneration?type=LoginRate"  id="captchaImageR" alt="captcha" /> 
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
<div class="footer-img col-md-12" style="overflow: hidden;">
    <div class="row">
        <div class="col-md-10 offset-md-2 new-footer-cnt">
            <div class="col-md-12">
                <div class="col-md-6 new-column-cnt">
                    <h3><bean:message key="ecom.home.header.buttom1" bundle="<%=clientcode%>"/></h3>
                    <p><bean:message key="ecom.home.header.buttom2" bundle="<%=clientcode%>"/></p>
                </div>
                <div class="col-md-6">
                    <button class="book-an-app-btn" onclick="window.location.href = 'Accesslogin.do?type=contactus'"><bean:message key="home.bat.head" bundle="<%=clientcode%>"/> </button>
                </div>
            </div>
        </div>
    </div>            
    <img src="<%=imagepath%>footer.png"/>
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

                        $('#scdshipmentdate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>
                        }
                        );
                        $('#todateq').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>
                        }
                        );
                        var fromdate = new Date();
                        var todate = new Date();
                        //fromdate.setDate(fromdate.getDate());
                        todate.setDate(fromdate.getDate() + <%=scheduleDateRangeDays%>);
                        $('#scdshipmentdate').datepicker("setDate", fromdate);
                        $('#todateq').datepicker("setDate", todate);
                        $('#shipmentdate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>
                        }
                        );
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
                                }, submitHandler: function (form) {
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