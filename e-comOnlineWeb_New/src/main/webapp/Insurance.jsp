<%-- 
    Document   : Insurance
    Created on : Oct 25, 2021, 10:01:30 PM
    Author     : gobinath.t
--%>

<%@page import="java.util.Arrays,java.util.ArrayList,java.util.Set,net.ecom.common.Validation,java.util.LinkedHashSet,java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
    String shipmentdate = (String) request.getParameter("shipmentdate");
    String onlinebkgno = (String) request.getParameter("onlinebkgno");
    String origin = (String) request.getParameter("origin");
    String delivery = (String) request.getParameter("delivery");
    String AP_country = (String) request.getParameter("AP_country");
    String commodity = (String) request.getParameter("commodity");
    ArrayList commoditylist = new ArrayList(Arrays.asList(commodity.split("~~~")));
    String vessel = (String) request.getParameter("vessel");
    String vesselcode = (String) request.getParameter("vesselcode");
    String custtype = (String) request.getParameter("custtype");
    String quoteno = Validation.checkNull((String) request.getParameter("quoteno"));
    System.out.println("[ECOM-WAR] Insurance  custtype==> " + custtype + " &quoteno=>" + quoteno);
    String sourcetype = Validation.checkNull((String) request.getParameter("sourcetype"));
%>
<div class="card ecom_insurance" id="ecom_insurance">
    <div class="card-header" style="box-shadow: 0px 2px 3px 0px #e0e0e0;height: 62px;background-color: white;">
        <div class="form-group row">
            <div class="col-sm-4 float-left">
                <img class="brand-logo" alt="logo" src="/Ecommaster/SwissRe_Logo.png">
            </div>
            <div class="col-sm-8 float-right">
                <h3 class="col-sm-11 text-right" style="margin-right: 20px;color: #00bcd4;margin-top: 8px;"><b> <bean:message key="ecom.insurance.spot.voyage" bundle="<%=clientcode%>" /> </b></h3>
            </div>
        </div>
    </div>
    <div class="card-body">
        <div class="form-group row">
            <div class="col-sm-12 col-sm-6">
                <label class="col-sm-3 ecom_insurance_text">Party type </label>
                <input type="hidden" id="onlinebookno" value="<%=onlinebkgno%>">
                <input type="hidden" id="sourcetype" value="<%=sourcetype%>">
                <div class=" col-sm-3 checkbox-inline" style="margin:7px 0px 0px -3px;">

                    <div class="switchToggle" style="display: inline-flex;">
                        <span class="toggle_label" style="padding-right: 5px;">AP</span>
                        <input type="checkbox" id="IN_AP"  onchange="enableAP()" checked="checked"/>
                        <label class="tgl_round" for="IN_AP"> </label>
                    </div>
                    <div class="switchToggle" style="display: inline-flex;">
                        <span class="toggle_label" style="padding-right: 5px;">SH</span>
                        <input type="checkbox" id="IN_SH" onchange="enableSH()" />
                        <label class="tgl_round" for="IN_SH"> </label>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12 col-sm-6">
                <label class="col-sm-3 ecom_insurance_text">Country of Policyholder </label>
                <label class="col-sm-3" id="IN_Country"><%=AP_country%> </label>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12 col-sm-6">
                <label class="col-sm-3 ecom_insurance_text">Port of Origin </label>
                <label class="col-sm-3" id="IN_Origin"><%=origin%> </label>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12 col-sm-6">
                <label class="col-sm-3 ecom_insurance_text">Port of Destination </label>
                <label class="col-sm-3" id="IN_Delivery"><%=delivery%> </label>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12 col-sm-6">
                <label class="col-sm-3 ecom_insurance_text">Vessel </label>
                <label class="col-sm-3" id="IN_Vessel"><%=vessel%> </label>
                <input type="hidden" id="vesselcode" name="vesselcode" value="<%=vesselcode%>">
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12 col-sm-6">
                <label class="col-sm-3 ecom_insurance_text">Shipment Date </label>
                <label class="col-sm-3" id="IN_Shipmentdate"><%=shipmentdate%> </label>
            </div>
        </div>
        <%if (commoditylist != null && commoditylist.size() > 0) {
                Set commodityset = new LinkedHashSet(commoditylist);
                int i = 0;
                Iterator cmdty = commodityset.iterator();
                while (cmdty.hasNext()) {
                    i = i + 1;
        %>
        <div class="form-group row commoditydiv">
            <div class="col-sm-12">
                <div class="checkbox-inline col-sm-3" style="margin:7px 0px 0px -3px;">
                    <div class="switchToggle" style="display: inline-flex;">
                        <input type="checkbox"  id="IN_Commodity<%=i%>" class="IN_Commodity" checked="checked" />
                        <label class="tgl_round" for="IN_Commodity<%=i%>"> </label>
                        <span class="toggle_label ecom_insurance_text" style="padding-left: 5px;"> Commodity / HS Code</span>
                    </div>
                </div>
                <label class="col-sm-3 commodity_value" ><%=cmdty.next()%> </label>
                <label class="col-sm-2 ecom_insurance_text">Cargo Value (USD) </label>
                <div class="checkbox-inline scol-sm-3"> 
                    <input type="text" id="IN_Cargovalue" class="IN_Cargovalue light_border">
                </div>
            </div>
        </div>
        <%}
            }%>
        <div class="form-group row">
            <div class="col-sm-12">
                <div class="col-sm-4 float-right">
                    <button class="btn btn-info" style="padding:12px 65px" onclick="getPremiumAmount('<%=quoteno%>')"> <bean:message key="ecom.insurance.calculate.premium" bundle="<%=clientcode%>" /> </button>
                </div>
            </div>
        </div>
        <hr>

        <div id="Insurance_details">

        </div>
    </div>
</div>

<script type='text/javascript'>

    function getPremiumAmount(quoteno) {
        var custdetails = "", comdval = "", isvalid = true, iscomdtyavable = false;
        if ($('#sourcetype').val() == 'SI') {
            if ($('#IN_SH').prop("checked") == true) {
                custdetails = "&custname=" + $('#ebook_shippername').val() + "&address=" + $('#ebook_shipperaddress').val() + "&address1=" + $('#ebook_shipperaddress1').val() + "&city=" + $('#ebook_shippercity').val() + "&country=" + $('#ebook_shippercountry_name').val() + "&state=" + $('#ebook_shipperstate').val() + "&email=" + $('#ebook_shipperemail').val() + "&postcode=" + $('#ebook_shipperzipcode').val() + "&custtype=SH";
            } else {
                custdetails = "&custname=" + $('#apname').val() + "&address=" + $('#apaddr').val() + "&address1=" + $('#apaddr1').val() + "&city=" + $('#apCityName').val() + "&country=" + $('#apCountryName').val() + "&state=" + $('#apstate').val() + "&email=" + $('#apemail').val() + "&postcode=" + $('#apzipcode').val() + "&custtype=AP";
            }
        } else {
            if ($('#IN_SH').prop("checked") == true) {
                custdetails = "&custname=" + $('#ebook_shippername').val() + "&address=" + $('#ebook_shipperaddress').val() + "&address1=" + $('#ebook_shipperaddress1').val() + "&city=" + $('#ebook_shippercity_desc').val() + "&country=" + $('#ebook_shippercountry_name').val() + "&state=" + $('#ebook_shipperstate').val() + "&email=" + $('#ebook_shipperemail').val() + "&postcode=" + "&custtype=SH";
            } else {
                custdetails = "&custname=" + $('#ebook_agreename').val() + "&address=" + $('#ebook_agreeaddress').val() + "&address1=" + $('#ebook_agreeaddress1').val() + "&city=" + $('#ebook_agreecity_desc').val() + "&country=" + $('#ebook_agreecountry_name').val() + "&state=" + $('#ebook_agreestate').val() + "&email=" + $('#ebook_agreeemail').val() + "&postcode=" + "&custtype=AP";
            }
        }
        console.log("custdetails=> " + custdetails);
        $('#ecom_insurance').find('.commoditydiv').each(function () {
            if ($(this).find('.IN_Commodity').prop('checked') == true) {
                iscomdtyavable = true;
                comdval = comdval + $(this).find('.commodity_value').text() + "~~~" + $(this).find('.IN_Cargovalue').val() + "@@@@@";
                if ($(this).find('.IN_Cargovalue').val().length == 0) {
                    isvalid = false;
                }
            }
        });
        console.log(comdval);
        comdval = comdval.substring(0, comdval.length - 5);
        console.log("InsuranceAjax?type=premium&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#onlinebookno').val() + "&vesselcode=" + $('#vesselcode').val());
        console.log("isvalid=> " + isvalid);
        if (isvalid && iscomdtyavable) {
            openLoader();
            $.ajax({
                type: "POST",
                url: "InsuranceAjax?type=premium&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#onlinebookno').val() + "&vesselcode=" + $('#vesselcode').val() + "&quoteno=" + quoteno+"&shipmentservicecode="+$('#hidINServiceCode').val(),
                success: function (response) {
                    console.log("Inside ajax");
                    $("#Insurance_details").html(response);
                    $("#Insurance_details").show();
                    closeLoader();
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else {
            if (!isvalid) {
                alert("Enter Cargo Value for the selected Commodity / HS Code");
            } else if (!iscomdtyavable) {
                alert("Please select the Commodity / HS Code");
            }
        }
        console.log("end getPremiumAmount ajax");
    }
    function enableAP() {
        $('#IN_SH').prop("checked", false);
        if ($('#IN_AP').attr("checked") == true || $('#IN_AP').attr("checked") =='checked') {
            $('#IN_Country').val($('#ebook_agreecountry_name').val());
        }
    }
    function enableSH() {
        $('#IN_AP').prop("checked", false);
        if ($('#IN_SH').prop("checked") == true || $('#IN_SH').attr("checked") =='checked') {
            if ($('#ebook_shippername').val().length > 0) {
                $('#IN_Country').val($('#ebook_shippercountry_name').val());
            } else {
                alert("Please enter the shipper customer details");
                $('#IN_SH').prop("checked", false);
                $('#IN_AP').prop("checked", true);
            }
        }
    }
    function addInsurance(quoteno) {
        var custdetails = "", comdval = "";
        openLoader();
        if ($('#sourcetype').val() == 'SI') {
            if ($('#IN_SH').prop("checked") == true) {
                custdetails = "&custname=" + $('#ebook_shippername').val() + "&address=" + $('#ebook_shipperaddress').val() + "&address1=" + $('#ebook_shipperaddress1').val() + "&city=" + $('#ebook_shippercity').val() + "&country=" + $('#ebook_shippercountry_name').val() + "&state=" + $('#ebook_shipperstate').val() + "&email=" + $('#ebook_shipperemail').val() + "&postcode=" + $('#ebook_shipperzipcode').val() + "&custtype=SH";
            } else {
                custdetails = "&custname=" + $('#apname').val() + "&address=" + $('#apaddr').val() + "&address1=" + $('#apaddr1').val() + "&city=" + $('#apCityName').val() + "&country=" + $('#apCountryName').val() + "&state=" + $('#apstate').val() + "&email=" + $('#apemail').val() + "&postcode=" + $('#apzipcode').val() + "&custtype=AP";
            }
        } else {
            if ($('#IN_SH').prop("checked") == true) {
                custdetails = "&custname=" + $('#ebook_shippername').val() + "&address=" + $('#ebook_shipperaddress').val() + "&address1=" + $('#ebook_shipperaddress1').val() + "&city=" + $('#ebook_shippercity_desc').val() + "&country=" + $('#ebook_shippercountry_name').val() + "&state=" + $('#ebook_shipperstate').val() + "&email=" + $('#ebook_shipperemail').val() + "&postcode=" + "&custtype=SH";
            } else {
                custdetails = "&custname=" + $('#ebook_agreename').val() + "&address=" + $('#ebook_agreeaddress').val() + "&address1=" + $('#ebook_agreeaddress1').val() + "&city=" + $('#ebook_agreecity_desc').val() + "&country=" + $('#ebook_agreecountry_name').val() + "&state=" + $('#ebook_agreestate').val() + "&email=" + $('#ebook_agreeemail').val() + "&postcode=" + "&custtype=AP";
            }
        }
        $('#ecom_insurance').find('.commoditydiv').each(function () {
            if ($(this).find('.IN_Commodity').prop('checked') == true) {
                comdval = comdval + $(this).find('.commodity_value').text() + "~~~" + $(this).find('.IN_Cargovalue').val() + "@@@@@";
            }
        });
        comdval = comdval.substring(0, comdval.length - 5);
        console.log(comdval);
        enableinsuranceswitchbtn();
        var typeval = 'quotecreate';
        if (quoteno.length > 0) {
            typeval = 'quoteupdate';
        }
        $.ajax({
            type: "POST",
            url: "InsuranceAjax?type=" + typeval + "&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#onlinebookno').val() + "&vesselcode=" + $('#vesselcode').val() + "&quoteno=" + quoteno+"&shipmentservicecode="+$('#hidINServiceCode').val(),
            success: function (response) {
                console.log("Inside ajax");
                closeLoader();
                if (quoteno.length > 0) {
                    alert("Insurance Updated Successfully!!");
                } else {
                    alert("Insurance Created Successfully!!");
                }
                //$("#Insurance_details").html(response);
                $.fancybox.close();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
        console.log("end addInsurance ajax");
    }
    function enableinsuranceswitchbtn() {
        if ($('#sourcetype').val() == 'SI') {
            $('#ecom_insurance').find('.commoditydiv').each(function () {
                if ($(this).find('.IN_Commodity').prop('checked') == true) {
                    var inscmdty = $(this).find('.commodity_value').text()
                    $("#cargo_details").find('.si_cargo_pkg_des').each(function () {
                        var comdhs = $(this).find('.v1hscommodity:nth-child(2)').val();
                        if (comdhs == '' || (typeof comdhs === 'undefined') || comdhs.length == 0) {
                            comdhs = $(this).find('.commodity_new:nth-child(2)').val();
                        }
                        console.log("inscmdty => " + inscmdty);
                        console.log("comdhs => " + comdhs);
                        if (inscmdty == comdhs) {
                            $(this).find('.insuranceswitchbtn').prop("checked", true);
                            var quote_no = $('#In_quoteno').val();
                            if (quote_no != '' && quote_no != 'null' && quote_no.length > 0) {
                                $(this).find('.inquoteno').val(quote_no);
                                if ($('#IN_SH').prop("checked") == true) {
                                    $(this).find('.inquotecusttype').val("SH");
                                } else {
                                    $(this).find('.inquotecusttype').val("AP");
                                }
                            }
                        }
                    });

                }
            });
        } else {
            $('#ecom_insurance').find('.commoditydiv').each(function () {
                if ($(this).find('.IN_Commodity').prop('checked') == true) {
                    var inscmdty = $(this).find('.commodity_value').text()
                    $("#datatablebycargodiv").find('.preview').each(function () {
                        var comdhs = $(this).find('.hscodetypehead:nth-child(2)').val();
                        if (comdhs == '' || (typeof comdhs === 'undefined') || comdhs.length == 0) {
                            comdhs = $(this).find('.commoditytypehead:nth-child(2)').val();
                        }
                        console.log("inscmdty => " + inscmdty);
                        console.log("comdhs => " + comdhs);
                        if (inscmdty == comdhs) {
                            $(this).find('.insuranceswitchbtn').prop("checked", true);
                            var quote_no = $('#In_quoteno').val();
                            if (quote_no != '' && quote_no != 'null' && quote_no.length > 0) {
                                $(this).find('.inquoteno').val(quote_no);
                                if ($('#IN_SH').prop("checked") == true) {
                                    $(this).find('.inquotecusttype').val("SH");
                                } else {
                                    $(this).find('.inquotecusttype').val("AP");
                                }
                            }
                        }
                    });

                }
            });
        }
    }
    $(document).ready(function () {
    <%if ("SH".equalsIgnoreCase(custtype)) {%>
        $('#IN_SH').prop("checked", true);
        enableSH();
    <%}%>
    <%if (quoteno.length() > 0) {%>
        loadQuoteAmountDetails('<%=quoteno%>');
    <%}%>
    });
    function loadQuoteAmountDetails(quote) {
        console.log('loadQuoteAmountDetails=> ' + quote);
        openLoader();
        $.ajax({
            type: "POST",
            cache: false,
            url: "InsuranceAjax?type=quoteview&logotype=N&quoteno=" + quote,
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                console.log("Inside ajax quoteview");
                $("#Insurance_details").html(data);
                $("#Insurance_details").show();
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