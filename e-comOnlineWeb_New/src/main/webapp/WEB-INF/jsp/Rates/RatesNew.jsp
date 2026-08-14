<%-- 
    Document   : RatesNew
    Created on : Sep 25, 2019, 4:17:11 PM
    Author     : gobinath.t
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.vo.Equipments,java.util.Map,net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
//    String clientcode = (String) getServletContext().getAttribute("clientcode");
    String userCode = (String) session.getAttribute("SESSION_USERCODE");
    System.out.println("[ECOM-WAR] RatesNew usercode:::" + userCode);
    String clientcode = (String) session.getAttribute("clientcode");
    System.out.println("[ECOM-WAR] RatesNew clientcode:::" + clientcode);
    String contextRoot = request.getContextPath();
    String loginpol = (String) request.getAttribute("loginratepol");
    String loginpod = (String) request.getAttribute("loginratepod");
    String loginrateshipdate = (String) request.getAttribute("loginrateshipdate");
    String logineqp = (String) request.getAttribute("logineqp");
    String loginratetype = (String) request.getAttribute("loginratetype");
    String pastdateconfig = (String) session.getServletContext().getAttribute(GeneralConf.PAST_DATE_CONFIG);
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/schdule.png" /> <bean:message key="ecom.rates" bundle="<%=clientcode%>"/>
</div>

<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="col-sm-12">
            <html:form  method="post" action="RatesAction.do" styleId="RatesForm"  styleClass="form-horizontal">
                <div class="row form-group">
                    <div class="col-xs-12 col-sm-4 swap_input_width">
                        <div class='full-input portsnotes'>
                            <label><bean:message key='Tracking.origin' bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                            <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                            <html:text property="polcode"  styleId="polcode" styleClass="required"  />
                            <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>
                        </div>
                    </div>

                    <a href="javascript:void(0);" onclick='swapPort()'> <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" />  </a>

                    <div class="col-xs-12 col-sm-4 swap_input_width">
                        <div class='full-input portsnotes'>
                            <label><bean:message key='Tracking.delivery' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                            <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                            <html:text property="podcode"  styleId="podcode" styleClass="required"  />
                            <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-4 swap_input_width" >
                        <div class='full-input'>
                            <label><bean:message key='ecom.tariff.shipmentdate' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                            <img class="input_icon" src="Design/assets/images/page_icons/date.png" />
                            <html:text property="shipmentdate" styleId="shipmentdate" styleClass="required"  />
                            <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                        </div>
                    </div>

                </div>

                <div class="row form-group">
                    <div class="col-xs-12 col-sm-4 swap_input_width">
                        <html:select styleClass="form-control inside_input required" styleId="eqptype" property="eqptype">
                            <html:option value="">Select Equipment</html:option>
                            <html:options collection="SESSION_EQUIPMENT_TYPE" property="value" labelProperty="label" />
                        </html:select>

                    </div>


                    <label for="contrl_agency" class="col-sm-1 control-label ecom_padding_left p-t-10 d-none"> <bean:message key="ebooking.quantity.display" bundle="<%=clientcode%>"/></label>

                    <div class="col-sm-3 p-t-10 d-none">
                        <div class="slidecontainer" style="width: 78%">
                            <input type="range" min="1" max="100" value="1" id="quantity_range" name="quantity_range" onchange="setvaluehidden(this.value)" class="range_slider">
                            <html:hidden property="quantity" styleId="quantity" value="1"  />
                            <span id="ragne_value"></span>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-4 m-l-30 swap_input_width">
                        <html:text property="commodity" styleId="commodity" styleClass="form-control inside_input autocompletetmsg"  />
                        <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
                    </div>
                    <% if (userCode != null && userCode.length() > 0) {%>
                    <div class="col-xs-12 col-sm-2 " style="display:none">
                        <input type="text"  name="captcha" id="captcha" class="form-control inside_input remove_autocomplete required"  />
                    </div>
                    <div class="col-xs-12 col-sm-2" style="display:none">
                        <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
                        <a class="refresh_cap" title="Reload captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');" href="javascript:void(0);" ><i class="fa fa-refresh"></i></a>
                    </div>
                    <%} else {%>
                    <div class="col-xs-12 col-sm-2 ">
                        <input type="text"  name="captcha" id="captcha" class="form-control inside_input remove_autocomplete required" />
                    </div>
                    <div class="col-xs-12 col-sm-2">
                        <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
                        <a class="refresh_cap" title="Reload captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');" href="javascript:void(0);"><i class="fa fa-refresh"></i></a>
                    </div>
                    <%}%>

                </div>

                <div class="row form-group">
                    <div class="col-xs-12 col-sm-4 swap_input_width"></div>
                    <div class="col-sm-4 m-l-30 swap_input_width">
                        <button type="submit" class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                    </div>
                </div>
            </html:form>
        </div>
    </div>
</div>
<div id="ratesdetailsdiv" style="display: none"></div>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsno%>"></script>
<script type="text/javascript">
                            var contextRoot = "<%=contextRoot%>";
                            var remotevalidate = contextRoot + "/CaptchaValidation";
                            var capchavalidate = contextRoot + "/CaptchaGeneration?";
                            function CreateNewCaptcha(contextRoot)
                            {
                                var d = new Date();
                                $("#captchaImage").attr('src', capchavalidate + d.getTime());
                                $("#captcha").val('');
                            }
                            $(document).ready(function () {
                                $('#commodity').attr('placeholder', '<bean:message key="ecom.contract.commodity" bundle="<%=clientcode%>"/>');
                                $('#polcode,#podcode').attr('placeholder', '<bean:message key="ecom.schdl.portcode" bundle="<%=clientcode%>"/>');
                                $('#captcha').attr('placeholder', '<bean:message key="ecom.rr.entcaptcha" bundle="<%=clientcode%>"/>');
                                $('#shipmentdate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>' <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>});
                                var fromdaterfr = new Date();
                                fromdaterfr.setDate(fromdaterfr.getDate());
                                $('#shipmentdate').datepicker("setDate", fromdaterfr);
                                $("#RatesForm").validate({
                                    rules: {
                                        podcode: {
                                            required: true
                                        },
                                        polcode: {
                                            required: true
                                        },
                                        shipmentdate: {
                                            required: true
                                        },
                                        captcha: {
                                            required: true,
                                            remote: remotevalidate
                                        }

                                    },
                                    messages: {
                                        podcode: {
                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                        },
                                        polcode: {
                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                        },

                                        shipmentdate: {
                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                        },
                                        captcha: {
                                            required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                            remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                        }
                                    },
                                    submitHandler: function (form) {
                                        var eqpId = $('#eqptype').val();
                                        var comcode = $('#commodity').val();
                                        var cmdty = "";
                                        if (comcode != "") {
                                            comcode = comcode.split('-');
                                            cmdty = comcode[0];
                                        }
                                        var pol = $('#polcode').val();
                                        var pod = $('#podcode').val();
                                        var shipmentdate = $('#shipmentdate').val();
                                        pol = pol.split('-');
                                        pod = pod.split('-');
                                        $("#btnsearch").button('loading');
                                        openLoader();
                                        $.ajax({
                                            type: "POST",
                                            url: "RatesAction.do?eqptype=" + eqpId + "&polcode=" + trim(pol[0]) + "&podcode=" + trim(pod[0]) + "&commodity=" + trim(cmdty) + "&shipmentdate=" + shipmentdate,
                                            success: function (response) {
                                                $("#ratesdetailsdiv").html(response);
                                                $('#ratesdetailsdiv').show();
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
                            $(document).ready(function () {
                                portlist = updateMaskPortList(portlist);
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
                            $(document).ready(function () {
                                portlist = updateMaskPortList(portlist);
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
                            $(window).one("load", function () {
                                var commodity = new Bloodhound({
                                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                                    local: commodityJson
                                });
                                commodity.initialize();
                                $('#commodity').typeahead(
                                        {
                                            hint: true,
                                            highlight: true,
                                            minLength: 2
                                        },
                                        {
                                            name: 'commodity',
                                            limit: 5,
                                            displayKey: function (commodityJson) {
                                                return commodityJson.commoditycode + " - " + commodityJson.commodityname;
                                            },
                                            source: commodity.ttAdapter()
                                        }).on('typeahead:render', function (obj, datum) {
                                    $("#typeheadhidden2").val("");
                                }).on('typeahead:selected', function (obj, datum) {
                                    $("#typeheadhidden2").val(datum.commodityname);
                                }).on('typeahead:autocomplete', function (obj, datum) {
                                    $("#typeheadhidden2").val(datum.commodityname);
                                }).on("typeahead:change", function (obj, datum) {
                                    var hiddenValue = $("#typeheadhidden2").val();
                                    if (hiddenValue == null || hiddenValue == "") {
                                        $('#commodity').typeahead('val', "");
                                        $(obj.currentTarget).val("");
                                    }
                                });
                            });
                            function swapPort() {
                                var temp = $('#polcode').val();
                                $('#polcode').typeahead("val", $('#podcode').val());
                                $('#podcode').typeahead("val", temp);
                            }
                            function setvaluehidden(val) {
                                $('#quantity').val(val);
                            }
                            function resetValues() {
                                $('#polcode,#podcode,#commodity,#eqptype').val("");
                                $("#quantity_range,#quantity").val(1);
                                $('#polcode,#podcode,#commodity').typeahead('val', '');
                                var fromdaterfr = new Date();
                                fromdaterfr.setDate(fromdaterfr.getDate());
                                $('#shipmentdate').datepicker("setDate", fromdaterfr);

                            }
                            var slider = document.getElementById("quantity_range");
                            var output = document.getElementById("ragne_value");
                            output.innerHTML = slider.value;

                            slider.oninput = function () {
                                output.innerHTML = this.value;
                            }
                            function navigateToQuote() {
                                var pol = $('#polcode').val();
                                var pod = $('#podcode').val();
                                var eqptype = $('#eqptype').val();
                                var quantity = $('#quantity').val();
                                var commodity = $('#commodity').val();
                                window.location = "Accesslogin.do?type=raterequest&pol=" + pol + "&pod=" + pod + "&eqptype=" + eqptype + "&quantity=" + quantity + "&commodity=" + commodity;
                            }

    <%
        if ("loginratetype".equalsIgnoreCase(loginratetype)) {
    %>
                            $(document).ready(function () {
                                $('#polcode').val('<%=loginpol%>');
                                $('#podcode').val('<%=loginpod%>');
                                $('#eqptype').val('<%=logineqp%>');
                                $('#shipmentdate').datepicker("setDate", '<%=loginrateshipdate%>');
                                callrateuleforlogin('<%=loginpol%>', '<%=loginpod%>', '<%=loginrateshipdate%>', '<%=logineqp%>');

                            });
    <%                    }
    %>
                            function callrateuleforlogin(pol, pod, shipdate, eqp) {
                                var eqpId = trim(eqp);
                                var pol = trim(pol);
                                var pod = trim(pod);
                                var cmdty = '';
                                //  $('#btnreset').prop('disabled', true);
                                pol = pol.split('-');
                                pod = pod.split('-');
                                $("#btnsearch").button('loading');
                                openLoader();
                                $.ajax({
                                    type: "POST",
                                    url: "RatesAction.do?eqptype=" + eqpId + "&polcode=" + trim(pol[0]) + "&podcode=" + trim(pod[0]) + "&commodity=" + trim(cmdty) + "&shipmentdate=" + shipdate,
                                    success: function (response) {
                                        $("#ratesdetailsdiv").html(response);
                                        $('#ratesdetailsdiv').show();
                                        closeLoader();
                                    },
                                    error: function (xhr) {
                                        if (xhr.status == 403) {
                                            window.location.href = "SessionExpired.jsp";
                                        }
                                    }
                                });
                            }
</script>