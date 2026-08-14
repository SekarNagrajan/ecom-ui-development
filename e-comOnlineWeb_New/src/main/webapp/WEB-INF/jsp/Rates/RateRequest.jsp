<%--
    Document   : RateRequest
    Created on : Dec 4, 2017, 3:25:56 PM
    Author     : deepa.p
--%>

<%@page import="java.util.LinkedHashMap"%>
<%@page import="net.ecom.common.Validation,java.util.ArrayList,java.util.Hashtable,java.util.Map,net.ecom.common.GeneralConf"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java"%>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String contextRoot = request.getContextPath();
    StringBuffer requesturl = new StringBuffer(request.getRequestURL());
    StringBuffer requesturi = new StringBuffer(request.getRequestURI());
    String requrl = requesturl.toString();
    String requri = requesturi.toString();
    requrl = requrl.replace(requri, "");
    String success = (String) request.getAttribute("RateRequestSuccess");
    success = success != null && success.length() > 0 ? success : "";
    System.out.println("[ECOM-WAR] RateRequest SUCCESS==================> " + success);
    String loginName = (String) session.getAttribute("SESSION_LOGINNAME");
    loginName = loginName != null && loginName.length() > 0 ? loginName : "";
    String userName = (String) session.getAttribute("SESSION_USERNAME");
    userName = userName != null && userName.length() > 0 ? userName : "";
    String email = (String) session.getAttribute("SESSION_EMAIL");
    email = email != null && email.length() > 0 ? email : "";
    String compName = (String) session.getAttribute("apname");
    compName = compName != null && compName.length() > 0 ? compName : "";
    System.out.println("[ECOM-WAR] RateRequest compName>>" + compName);
    Hashtable countryHashObj = (Hashtable) getServletContext().getAttribute("SESSION_COUNTRYOBJECT");
    String country = (String) session.getAttribute("SESSION_USER_COUNTRY");
    country = country != null && country.length() > 0 ? country + " - " + Validation.checkNull((String) countryHashObj.get(country)) : "";
    String user = (String) session.getAttribute("sessionAdmin");
    String actualfreight = request.getParameter("actualfreight");
    String pol = request.getParameter("pol");
    String pod = request.getParameter("pod");
    String eqptype = request.getParameter("eqptype");
    String quantity = request.getParameter("quantity");
    String commodity = request.getParameter("commodity");
    LinkedHashMap eqpmap = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
    if (eqptype != null && eqpmap != null) {
        eqptype = eqptype + " - " + eqpmap.get(eqptype.trim());
    }
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));

%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/rate-request.png" /> 
    <%if (actualfreight != null && actualfreight.length() > 0) {%> 
    <bean:message key="ecom.rr.specialrate" bundle="<%=clientcode%>"/>
    <%} else {%>
    <bean:message key="ecom.rr.quote" bundle="<%=clientcode%>"/>
    <%}%>   </div>
<div class="container-fluid page_bottom_content">
    <div class="card card-body card-shadow">
        <div class="details_search">
            <div class="text-center">
                <%if ("success".equalsIgnoreCase(success)) {%>
                <div class="form-group alert-error" id='ratereqSuccess'>
                    <div class="alert alert-success alert-dismissible" role="alert" >
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <strong><bean:message key="ecom.msg.success" bundle="<%=clientcode%>"/></strong> <bean:message key="ecom.rr.rateest" bundle="<%=clientcode%>"/> 
                    </div>
                </div>
                <%} else if ("fail".equalsIgnoreCase(success)) {%>
                <div class="form-group alert-error"  id='ratereqfail'>
                    <div class="alert alert-danger alert-dismissible" role="alert" >
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <strong>  <bean:message key="ecom.rr.failmsg" bundle="<%=clientcode%>"/></strong> <bean:message key="ecom.rr.rrnotsub" bundle="<%=clientcode%>"/>
                    </div>
                </div>
                <%}%>
            </div>
            <html:form action="RateRequest.do" styleId="RateRequestForm" styleClass="form-horizontal">
                <div class="col-sm-12">
                    <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>

                    <div class="header_qto">   <bean:message key="ecom.rr.route" bundle="<%=clientcode%>"/> </div>

                    <div class="form-group row">
                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.origin" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="origin" styleId="origin"  styleClass="form-control required  light_border portsnotes" />
                        </div>

                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.pol" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="pol" styleId="pol"  styleClass="form-control required light_border portsnotes"/>
                        </div>

                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.pod" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="pod" styleId="pod"  styleClass="form-control required light_border portsnotes" />
                        </div>
                    </div>

                    <div class="row form-group">
                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.destination" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="destination" styleId="destination"  styleClass="form-control required light_border portsnotes" />
                        </div>
                    </div>

                    <div class="header_qto">   <bean:message key="ecom.rr.freight" bundle="<%=clientcode%>"/> </div>

                    <div class="row form-group">
                        <label for="contrl_agency" class="col-sm-1 control-label">  <bean:message key="ecom.rr.actfreight" bundle="<%=clientcode%>"/></label>
                        <div class="col-sm-3 ">
                            <html:text property="actualfreight" styleId="actualfreight"  styleClass="form-control light_border"/>
                        </div>

                        <label for="contrl_agency" class="col-sm-1 control-label ecom_padding_left">   <bean:message key="ecom.rr.reqrate" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="requestrate" styleId="requestrate"  styleClass="form-control required light_border" />
                        </div>
                    </div>

                    <div class="header_qto">   <bean:message key="ecom.rr.cargo" bundle="<%=clientcode%>"/> </div>

                    <div class="row form-group">
                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.eqpsize" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="eqpSize" styleId="eqpSize"  styleClass="form-control required light_border" />
                        </div>

                        <label for="contrl_agency" class="col-sm-1 control-label ecom_padding_left"> <bean:message key="ecom.rr.cargowt" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="cargoWt" styleId="cargoWt"  styleClass="form-control required light_border number" maxlength="8" /> 
                        </div>

                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.commodity" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="commodity" styleId="commodity"  styleClass="form-control required light_border autocompletetmsg"  />
                        </div>
                    </div>

                    <div class="row form-group">
                        <label for="contrl_agency" class="col-sm-1 control-label ecom_padding_left">  <bean:message key="ecom.rr.cargoquant" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <div class="slidecontainer">
                                <input type="range" min="1" max="100" value="1" class="range_slider required" onchange="setcargoqtyhidden()" id="cargoQty_Range" maxlength="3">
                                <span id="ragne_value"></span>
                            </div>
                            <html:hidden property="cargoQty" styleId="cargoQty" value="1"/> 
                        </div>
                    </div>

                    <div class="header_qto">   <bean:message key="ecom.rr.company" bundle="<%=clientcode%>"/> </div>

                    <div class="row form-group">
                        <label for="contrl_agency" class="col-sm-1 control-label ecom_padding_left"> <bean:message key="ecom.rr.companyname" bundle="<%=clientcode%>" />  <span class="mandatory">*</span></label>
                        <div class="col-sm-3 ">
                            <html:text property="companyName" styleId="companyName"  styleClass="form-control required light_border" maxlength="150" />
                        </div>

                        <label for="contrl_agency" class="col-sm-1 control-label ecom_padding_left"> <bean:message key="ecom.rr.country" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="country" styleId="country"  styleClass="form-control light_border required autocompletetmsg"/>
                        </div>

                        <label for="contrl_agency" class="col-sm-1 control-label ecom_padding_left"> <bean:message key="ecom.rr.contactname" bundle="<%=clientcode%>" /><span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="contactName" styleId="contactName"  styleClass="form-control required  light_border" maxlength="100" />
                        </div>
                    </div>

                    <div class="row form-group">
                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.emailid" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-3 ">
                            <html:text property="emailId" styleId="emailId"  styleClass="form-control required light_border email" maxlength="100"  />
                        </div>
                    </div>


                    <div class="header_qto">   <bean:message key="ecom.rr.comments" bundle="<%=clientcode%>"/> </div>

                    <div class="row form-group">
                        <label for="contrl_agency" class="col-sm-1 control-label"> <bean:message key="ecom.rr.comments" bundle="<%=clientcode%>" /> </label>
                        <div class="col-sm-7 " style="display: inline-grid;">
                            <html:textarea styleId="comments"  styleClass="form-control light_border" property="comments" rows="4"/>
                        </div>
                    </div>
                </div>

                <div class="row form-group">
                    <div class="col-sm-1"> </div>
                    <div class="col-sm-2 pull-right">
                        <button type="submit" class="btn btn-primary btn-lg btn_block_custom" name="submit" value="Submit" autocomplete="off" id="btnsearch" <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%>>   <bean:message key="ecom.submit" bundle="<%=clientcode%>"/> </button>
                    </div>
                    <div class="col-sm-2 pull-right">
                        <button type="reset" class="btn btn-danger btn-lg btn_block_custom_reset"  onclick="resetValues();" name="submit" value="Submit">  <bean:message key="ecom.reset" bundle="<%=clientcode%>"/> </button>
                    </div>
                </div>

            </html:form>
        </div>
    </div>
</div>


<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/country.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/equipment.js?<%=jsno%>"></script>
<script type="text/javascript">
                            var success = '<%=success%>';
                            console.log(success);
                            //    if (success == 'succes') {
                            //        alert("One of our representatives will contact you shortly with a rate estimate.");
                            //  }
                            var loginName = '<%=loginName%>';
                            var requrlserver = "<%=requrl%>";
                            var remotepath = "CaptchaValidation";

                            $(document).ready(function () {
                                $('#comments').attr("maxlength", '500')
                                $('#companyName,#country,#contactName,#emailId,#origin').val('');
                                $('#companyName,#country,#contactName,#emailId').attr('readonly', false);
                                $('#pol,#pod,#destination,#eqpSize,#cargoQty').val('');
                                $('#cargoWt,#commodity,#comments,#captcha,#actualfreight,#requestrate').val('');
                                $('#cargoQty_Range,#cargoQty').val('1');
                                $('#ragne_value').text('1');
                                $('#pol,#pod,#origin,#destination,#eqpSize,#commodity,#actualfreight').attr('readonly', false);
                                $('#pol,#pod,#origin,#destination,#eqpSize,#commodity').typeahead('val', '');
                                $('#cargoQty_Range').attr('disabled', false);
    <%if (actualfreight != null && actualfreight.length() > 0) {%>
                                $('#actualfreight').val("<%=actualfreight%>");
                                $('#actualfreight').attr("readonly", 'true');
    <%}
        if (pol != null && pol.length() > 0) {%>
                                $('#pol,#origin').val("<%=pol%>");
                                $('#pol,#origin').typeahead('val', "<%=pol%>");
                                $('#pol,#origin').attr("readonly", 'true');
    <%}
        if (pod != null && pod.length() > 0) {%>
                                $('#pod,#destination').val("<%=pod%>");
                                $('#pod,#destination').typeahead('val', "<%=pod%>");
                                $('#pod,#destination').attr("readonly", 'true');
    <%}
        if (quantity != null && quantity.length() > 0) {%>
                                $('#cargoQty_Range').val('<%=quantity%>').trigger('onchange');
                                $('#ragne_value').text('<%=quantity%>');
                                $('#cargoQty_Range').attr("disabled", 'true');
    <%}
        if (eqptype != null && eqptype.length() > 0) {%>
                                $('#eqpSize').val("<%=eqptype%>");
                                $('#eqpSize').typeahead('val', "<%=eqptype%>");
                                $('#eqpSize').attr("readonly", 'true');
    <%}
        if (commodity != null && commodity.length() > 0) {%>
                                $('#commodity').val("<%=commodity%>");
                                $('#commodity').typeahead('val', "<%=commodity%>");
                                $('#commodity').attr("readonly", 'true');
    <%}%>
                                if (loginName != null && loginName != '') {
                                    $('#contactName').val('<%=userName%>');
                                    $('#emailId').val('<%=email%>');
                                    $('#companyName').val('<%=compName%>');
                                    $('#country').val('<%=country%>');
                                    $('#country').typeahead('val', '<%=country%>');
                                    $('#companyName,#country,#contactName,#emailId').attr('readonly', true);
                                }
                                $("#RateRequestForm").validate({
                                    rules: {
                                        captcha: {
                                            required: true,
                                            remote: remotepath
                                        }
                                    },
                                    messages: {
                                        captcha: {
                                            required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                            remote: "  <bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                        }
                                    },
                                    submitHandler: function (form) {
                                        openLoader();
                                        $('#pol,#pod,#origin,#destination,#eqpSize,#commodity').attr('readonly', false);
                                        return true;
                                    }
                                });
                                var form = $("#RateRequestForm").show();
                                form.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly]";
                                loadtypeaheadtooltipmsg();
                            });
                            function resetValues() {
                                $('#comments').attr("maxlength", '500');
                                $('#companyName,#country,#contactName,#emailId,#origin,#actualfreight,#requestrate').val('');
                                $('#companyName,#country,#contactName,#emailId,#actualfreight').attr('readonly', false);
                                $('#pol,#pod,#destination,#eqpSize,#cargoQty').val('');
                                $('#cargoWt,#commodity,#comments,#captcha').val('');
                                $('#cargoQty_Range,#cargoQty').val('1');
                                $('#ragne_value').text('1');
                                $('#pol,#pod,#origin,#destination,#eqpSize,#commodity,#actualfreight').attr('readonly', false);
                                $('#pol,#pod,#origin,#destination,#eqpSize,#commodity').typeahead('val', '');
                                $('#cargoQty_Range').attr('disabled', false);
                            }
                            var commodityJson = commodityJson;
                            var portlist = portJson;
                            var EquipmentJson = EquipmentJson;
                            $(window).one("load", function () {
                                var eqp = new Bloodhound({
                                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('equipDesc', 'equipSize'),
                                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                                    local: EquipmentJson
                                });
                                eqp.initialize();
                                $('#eqpSize').typeahead(
                                        {
                                            hint: true,
                                            highlight: true,
                                            minLength: 2
                                        },
                                        {
                                            name: 'ports',
                                            limit: 5,
                                            displayKey: function (eqplist) {
                                                return eqplist.equipSize + " - " + eqplist.equipDesc;
                                            },
                                            source: eqp.ttAdapter()
                                        }).on('typeahead:render', function (obj, datum) {
                                    $("#typeheadhidden").val("");
                                }).on('typeahead:selected', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.equipSize);
                                }).on('typeahead:autocomplete', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.equipSize);
                                }).on("typeahead:change", function (obj, datum) {
                                    var hiddenValue = $("#typeheadhidden").val();
                                    if (hiddenValue == null || hiddenValue == "") {
                                        $('#eqpSize').typeahead('val', "");
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
                                $('#origin').typeahead(
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
                                        $('#origin').typeahead('val', "");
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
                            });
                            $(window).one("load", function () {
                                var ports = new Bloodhound({
                                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                                    local: portlist
                                });
                                ports.initialize();
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
                                    $("#typeheadhidden").val("");
                                }).on('typeahead:selected', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.PortName);
                                }).on('typeahead:autocomplete', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.PortName);
                                }).on("typeahead:change", function (obj, datum) {
                                    var hiddenValue = $("#typeheadhidden").val();
                                    if (hiddenValue == null || hiddenValue == "") {
                                        $('#pod').typeahead('val', "");
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
                                $('#destination').typeahead(
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
                                        $('#destination').typeahead('val', "");
                                        $(obj.currentTarget).val("");
                                    }
                                });
                            });
                            //
                            var counlist = countryJson;
                            $(window).one("load", function () {
                                var country = new Bloodhound({
                                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                                    local: counlist
                                });
                                country.initialize();
                                $('#country').typeahead(
                                        {
                                            hint: true,
                                            highlight: true,
                                            minLength: 2
                                        },
                                        {
                                            name: 'country',
                                            limit: 5,
                                            displayKey: function (counlist) {
                                                return counlist.countrycode + " - " + counlist.countryname;
                                            },
                                            source: country.ttAdapter()}).on('typeahead:render', function (obj, datum) {
                                    $("#typeheadhidden").val("");
                                }).on('typeahead:selected', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.countryname);
                                }).on('typeahead:autocomplete', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.countryname);
                                }).on("typeahead:change", function (obj, datum) {
                                    var hiddenValue = $("#typeheadhidden").val();
                                    if (hiddenValue == null || hiddenValue == "") {
                                        $('#country').typeahead('val', "");
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
                                            name: 'tarrifCommodity',
                                            limit: 5,
                                            displayKey: function (commodityJson) {
                                                return commodityJson.commoditycode + " - " + commodityJson.commodityname;
                                            },
                                            source: commodity.ttAdapter()
                                        }).on('typeahead:render', function (obj, datum) {
                                    $("#typeheadhidden").val("");
                                }).on('typeahead:selected', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.commodityname);
                                }).on('typeahead:autocomplete', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.commodityname);
                                }).on("typeahead:change", function (obj, datum) {
                                    var hiddenValue = $("#typeheadhidden").val();
                                    if (hiddenValue == null || hiddenValue == "") {
                                        $('#commodity').typeahead('val', "");
                                        $(obj.currentTarget).val("");
                                    }
                                });
                            });

                            var capchapath = "CaptchaGeneration?";
                            function CreateNewCaptcha(contextRoot)
                            {

                                $("#captchaImage").attr('src', capchapath + Math.random());
                                $("#captcha").val('');
                            }
                            function setcargoqtyhidden() {
                                $('#cargoQty').val($('#cargoQty_Range').val());
                            }
                            var slider = document.getElementById("cargoQty_Range");
                            var output = document.getElementById("ragne_value");
                            output.innerHTML = slider.value;

                            slider.oninput = function () {
                                output.innerHTML = this.value;
                            }

</script>
