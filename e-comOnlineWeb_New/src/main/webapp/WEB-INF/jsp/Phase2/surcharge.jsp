<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.vo.Equipments,java.util.Map,net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode = (String) session.getAttribute("clientcode");
    String contextRoot = request.getContextPath();
    StringBuffer requesturl = new StringBuffer(request.getRequestURL());
    StringBuffer requesturi = new StringBuffer(request.getRequestURI());

    String requrl = requesturl.toString();
    String requri = requesturi.toString();

    requrl = requrl.replace(requri, "");
    String loginpol = (String) request.getAttribute("loginratepol");
    String loginpod = (String) request.getAttribute("loginratepod");
    String loginrateshipdate = (String) request.getAttribute("loginrateshipdate");
    String logineqp = (String) request.getAttribute("logineqp");
    String loginratetype = (String) request.getAttribute("loginratetype");
    System.out.println("[ECOM-WAR] surcharge loginfdate>>>>>>>>>>>" + loginrateshipdate);
    System.out.println("[ECOM-WAR] surcharge logintdate----------" + loginratetype);
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key='ecom.surcharge.surcharge' bundle="<%=clientcode%>"/></div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">
            <html:form  method="post" action="SurchargeDetails.do" styleId="SurchargeForm"  styleClass="form-horizontal">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-4 swap_input_width">
                            <div class='full-input portsnotes'>
                                <label><bean:message key='ecom.surcharge.pol' bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                                <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                                <html:text property="polname" styleId="polcode" styleClass="required"  />
                                <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>
                            </div>
                        </div>
                        <a href="javascript:swapPort();"> <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" /> </a>
                        <div class="col-xs-12 col-sm-4 swap_input_width">
                            <div class='full-input portsnotes'>
                                <label><bean:message key='ecom.surcharge.pod' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                                <html:text property="podname" styleId="podcode" styleClass="required"  />
                                <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-4  swap_input_width m-l-20">
                            <div class='full-input'>
                                <label><bean:message key='ecom.tariff.shipmentdate' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                <img class="input_icon" src="Design/assets/images/page_icons/date.png" />
                                <html:text property="fromDate" styleId="fromDate" styleClass="required"  />
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-4 swap_input_width">
                            <html:select styleClass="form-control inside_input required" styleId="eqtype" property="eqtype">
                                <html:option value="">Select Equipment</html:option>
                                <html:options collection="SESSION_EQUIPMENT_TYPE" property="value" labelProperty="label" />
                            </html:select>
                        </div>
                        <div class="col-xs-12 col-sm-2 m-l-30">
                            <input type="text" name="captcha" id="captcha" class="form-control inside_input remove_autocomplete required" />
                        </div>
                        <div class="col-xs-12 col-sm-2">
                            <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
                            <a class="refresh_cap" title="Reload captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');" href="javascript:void(0);"><i class="fa fa-refresh"></i></a>
                        </div>
                        <div class="col-xs-12 col-sm-4 p-r-65 p-l-0">
                            <button type="submit" id="searchbtn" class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                        </div>
                    </div>
                </div>
            </html:form>
        </div>
    </div>
</div>
<div id="surchargeResult">
</div>

<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript">
                                var contextRoot = "<%=contextRoot%>";
                                var remotevalidate = contextRoot + "/CaptchaValidation";
                                var capchavalidate = contextRoot + "/CaptchaGeneration?";
                                function trim(value) {
                                    return LTrim(RTrim(value));
                                }
                                function LTrim(value) {
                                    var re = /\s*((\S+\s*)*)/;
                                    return value.replace(re, "$1");
                                }
                                // Removes ending whitespaces
                                function RTrim(value) {
                                    var re = /((\s*\S+)*)\s*/;
                                    return value.replace(re, "$1");
                                }
                                function CreateNewCaptcha(contextRoot)
                                {
                                    var d = new Date();
                                    $("#captchaImage").attr('src', capchavalidate + d.getTime());
                                    $("#captcha").val('');
                                }
                                function resetValues() {
                                    $('#polcode,#podcode').val("");
                                    $("#eqtype").prop('selectedIndex', 0);
                                    $('#polcode,#podcode').typeahead('val', '');
                                    var fromdaterfr = new Date();
                                    fromdaterfr.setDate(fromdaterfr.getDate());
                                    $('#fromDate').datepicker("setDate", fromdaterfr);
                                    $('#surchargeResult').val("");
                                    $('#surchargeResult').hide();
                                }
                                $(document).ready(function () {
                                    $('#polcode,#podcode').attr('placeholder', '<bean:message key="ecom.schdl.portcode" bundle="<%=clientcode%>"/>');
                                    $('#captcha').attr('placeholder', '<bean:message key="ecom.rr.entcaptcha" bundle="<%=clientcode%>"/>');
                                    $('#polcode,#podcode,#eqtype').val("");
                                    $('#fromDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>', startDate: "-infinity"});
                                    $("#SurchargeForm").validate({
                                        rules: {
                                            polname: {
                                                required: true
                                            },
                                            podname: {
                                                required: true
                                            },
                                            captcha: {
                                                required: true,
                                                remote: remotevalidate
                                            }
                                        },
                                        messages: {
                                            polname: {
                                                required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                            },
                                            podname: {
                                                required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                            },
                                            captcha: {
                                                required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                                remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                            }
                                        },
                                        submitHandler: function (form) {
                                            var eqpId = trim(document.getElementById('eqtype').value);
                                            var pol = trim(document.getElementById('polcode').value);
                                            var pod = trim(document.getElementById('podcode').value);
                                            $('#btnreset').prop('disabled', true);
                                            pol = pol.split('-');
                                            pod = pod.split('-');
                                            var fromdate = document.getElementById('fromDate').value;
                                            $("#btnsearch").button('loading');
                                            openLoader();
                                            $.ajax({
                                                type: "POST",
                                                url: "SurchargeDetails.do?eqtype=" + eqpId + "&polname=" + trim(pol[0]) + "&podname=" + trim(pod[0]) + "&fromDate=" + fromdate,
                                                success: function (response) {
                                                    $("#surchargeResult").html(response);
                                                    $('#surchargeResult').show();
                                                    $('#btnreset').prop('disabled', false);
                                                    $("#btnsearch").button('reset');
                                                    closeLoader();
                                                },
                                                error: function (xhr) {
                                                    if (xhr.status == 403) {
                                                        window.location.href = "SessionExpired.jsp";
                                                    }
                                                }
                                            });

                                            return false;
                                        }
                                    });
                                });
                                var portlist = portJson;
                                $(window).one("load", function () {
                                    var ports = new Bloodhound({
                                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                                        local: portlist
                                    });
                                    ports.initialize();
                                    $('#polcode').typeahead(
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
                                            $('#polcode').typeahead('val', "");
                                            $(obj.currentTarget).val("");
                                        }
                                    });
                                });

                                $(window).one("load", function () {
                                    var ports = new Bloodhound({
                                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                                        local: portlist
                                    });
                                    ports.initialize();
                                    $('#podcode').typeahead(
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
                                            $('#podcode').typeahead('val', "");
                                            $(obj.currentTarget).val("");
                                        }
                                    });
                                });

                                function trim(value) {
                                    return LTrim(RTrim(value));
                                }
                                function LTrim(value) {
                                    var re = /\s*((\S+\s*)*)/;
                                    return value.replace(re, "$1");
                                }
                                // Removes ending whitespaces
                                function RTrim(value) {
                                    var re = /((\s*\S+)*)\s*/;
                                    return value.replace(re, "$1");
                                }

                                $(document).ready(function () {
                                    var fromdate = new Date();
                                    fromdate.setDate(fromdate.getDate());
                                    $('#fromDate').datepicker("setDate", fromdate);
                                });
    <%
        if ("loginratetype".equalsIgnoreCase(loginratetype)) {
    %>
                                $(document).ready(function () {
                                    $('#polcode').val('<%=loginpol%>');
                                    $('#podcode').val('<%=loginpod%>');
                                    $('#eqtype').val('<%=logineqp%>');
                                    $('#fromDate').datepicker("setDate", '<%=loginrateshipdate%>');
                                    callrateuleforlogin('<%=loginpol%>', '<%=loginpod%>', '<%=loginrateshipdate%>', '<%=logineqp%>');

                                });
    <%                    }
    %>
                                function callrateuleforlogin(pol, pod, shipdate, eqp) {
                                    var eqpId = trim(eqp);
                                    var pol = trim(pol);
                                    var pod = trim(pod);
                                    $('#btnreset').prop('disabled', true);
                                    pol = pol.split('-');
                                    pod = pod.split('-');
                                    var fromdate = shipdate;
                                    $("#btnsearch").button('loading');
                                    openLoader();
                                    $.ajax({
                                        type: "POST",
                                        url: "SurchargeDetails.do?eqtype=" + eqpId + "&polname=" + trim(pol[0]) + "&podname=" + trim(pod[0]) + "&fromDate=" + fromdate,
                                        success: function (response) {
                                            $("#surchargeResult").html(response);
                                            $('#surchargeResult').show();
                                            $('#btnreset').prop('disabled', false);
                                            $("#btnsearch").button('reset');
                                            closeLoader();
                                        },
                                        error: function (xhr) {
                                            if (xhr.status == 403) {
                                                window.location.href = "SessionExpired.jsp";
                                            }
                                        }
                                    });
                                }
                                function swapPort() {
                                    var temp = $('#polcode').val();
                                    $('#polcode').typeahead("val", $('#podcode').val());
                                    $('#podcode').typeahead("val", temp);
                                }
</script>