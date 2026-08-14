<%-- 
    Document   : CommonBKGInsurance
    Created on : Nov 20, 2021, 8:03:14 PM
    Author     : gobinath.t
--%>

<%@page import="java.util.Arrays,java.util.ArrayList,java.util.Set,net.ecom.common.Validation,java.util.LinkedHashSet,java.util.Iterator,net.ecom.common.GeneralConf"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String Insurance_terms_condition = null;
    String SelectedLanguage = (String) session.getAttribute("SelectedLanguage");
    if ("CH".equalsIgnoreCase(SelectedLanguage)) {
        Insurance_terms_condition = (String) getServletContext().getAttribute("Insurance_Terms_and_Conditions_Lang");
    } else {
        Insurance_terms_condition = (String) getServletContext().getAttribute("Insurance_Terms_and_Conditions");
    }
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
%>
<%if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
<style>
    #removeinsurancebtn , #addinsurancebtn {
        margin-left: 0.6% !important;
    }
</style>
<h6><i class="step-icon fa_preview"></i><bean:message key="booking.cargo.insurance" bundle="<%=clientcode%>" /> </h6>

<fieldset>
    <div class="card" id="bkg_insurance">
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
            <div class="row">
                <div class="col-sm-6">
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.insuredparty" bundle="<%=clientcode%>" /> </label>
                            <div class=" col-sm-6 checkbox-inline" style="margin:7px 0px 0px -3px;">

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
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.countryofpolicyholder" bundle="<%=clientcode%>" /> </label>
                            <label class="col-sm-6" id="IN_Country"></label>
                        </div>
                    </div>
                    <div class="form-group row " >
                        <div class="col-sm-12" >
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.emailofpolicyholder" bundle="<%=clientcode%>" /></label>
                            <label class="col-sm-6" style="position: absolute;"><input type="text" id="IN_PEmail" class="IN_PEmail light_border" style="width: 100%;" readonly="readonly"/>
                            </label>
                        </div>
                    </div> 
                    <div class="form-group row " >
                        <div class="col-sm-12" style="padding-bottom: 10px;">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.additionalemail" bundle="<%=clientcode%>" /></label>
                            <label class="col-sm-6" style="position: absolute;"><input type="text" id="IN_Email" class="IN_Email light_border" style="width: 100%;" maxlength="190"/>
                                <span id="IN_email_notes" class="text-info" style="font-size: 12px;">Please use comma(,) for multiple email</span>
                            </label>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.portoforigin" bundle="<%=clientcode%>" /> </label>
                            <label class="col-sm-6" id="IN_Origin"> </label>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.portofdestination" bundle="<%=clientcode%>" /> </label>
                            <label class="col-sm-6" id="IN_Delivery"> </label>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.vessel" bundle="<%=clientcode%>" /> </label>
                            <label class="col-sm-6" id="IN_Vessel"> </label>
                            <input type="hidden" id="vesselcode" name="vesselcode" value="">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.shipmentdate" bundle="<%=clientcode%>" /> </label>
                            <label class="col-sm-6" id="IN_Shipmentdate"> </label>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.insurer" bundle="<%=clientcode%>" /> </label>
                            <label class="col-sm-6" id="IN_Shipmentdate">Swiss Re International SE, Hong Kong Branch </label>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="col-sm-5 ecom_insurance_text"><bean:message key="ecom.insurance.agent" bundle="<%=clientcode%>" /> </label>
                            <label class="col-sm-6" id="IN_Shipmentdate">Emirates Shipping Agencies (China) Limited, Hong Kong (HKIA's insurance agency licence no.:GA1036) </label>
                        </div>
                    </div>
                </div>
                <div class="col-sm-5">
                    <%=Insurance_terms_condition%>
                </div>
            </div>
            <hr>
            <div class="header_tle" style="text-transform: none;"> <bean:message key="ecom.insurance.select.commodity" bundle="<%=clientcode%>" /> <span style="padding-left: 1%;"> <span>(OR)</span> <span style="padding-left: 1%;"> <input type="checkbox" name="insurance_not_need" style="width: 16px;height: 16px;position: relative;top: 3px;" id="insurance_not_need" onchange="insurancenotneeded(this);"/> <span style="line-height: 15px;margin-left: 8px; margin-bottom: 14px;"><bean:message key="ecom.insurance.insurance.protection" bundle="<%=clientcode%>" /></span></span></span></div>
            <span id="comdtyblock">

            </span>
            <div class="form-group row">
                <div class="col-sm-12">
                    <div class="col-sm-12 offset-md-6" style="margin-left: 63.6%;">
                        <input type="hidden" id="IN_hidquoteno">
                        <input type="hidden" id="isInsuranceadded" >
                        <input type="hidden" id="isInsuranceavailable" >
                        <a href="javascript:void(0)" class="btn btn-info" style="padding: 10px 50px;font-size: 18px;" id="calpremiumbtn" onclick="getPremiumAmount($('#IN_hidquoteno').val())"> <bean:message key="ecom.insurance.calculate.premium" bundle="<%=clientcode%>" /> </a>
                    </div>
                </div>
            </div>
            <hr>

            <div id="Insurance_details">

            </div>
        </div>
    </div>
</fieldset>
<script type='text/javascript'>

    function getPremiumAmount(quoteno) {
        var custdetails = "", comdval = "", isvalid = true, iscomdtyavable = false;
        if ($('#IN_SH').prop("checked") == true) {
            custdetails = "&custname=" + ($('#ebook_shippername').val().replaceAll("&","~~")) + "&address=" + $('#ebook_shipperaddress').val() + "&address1=" + $('#ebook_shipperaddress1').val() + "&city=" + $('#ebook_shippercity').val() + "&country=" + $('#ebook_shippercountry_name').val() + "&state=" + $('#ebook_shipperstate').val() + "&email=" + $('#IN_Email').val() + "&postcode=" + $('#ebook_shipperzipcode').val() + "&Pemail=" + $('#IN_PEmail').val() + "&custtype=SH";
        } else {
            custdetails = "&custname=" + ($('#apname').val().replaceAll("&","~~")) + "&address=" + $('#apaddr').val() + "&address1=" + $('#apaddr1').val() + "&city=" + $('#apCityName').val() + "&country=" + $('#apCountryName').val() + "&state=" + $('#apstate').val() + "&email=" + $('#IN_Email').val() + "&postcode=" + $('#apzipcode').val() + "&Pemail=" + $('#IN_PEmail').val() + "&custtype=AP";
        }
        console.log("custdetails=> " + custdetails);
        $('#bkg_insurance').find('.commoditydiv').each(function () {
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
        console.log("InsuranceAjax?type=premium&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#SI_RefNo').val() + "&vesselcode=" + $('#vesselcode').val());
        console.log("isvalid=> " + isvalid);
        if (isvalid && iscomdtyavable) {
            openLoader();
            $.ajax({
                type: "POST",
                url: "InsuranceAjax?type=premium&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#SI_RefNo').val() + "&vesselcode=" + $('#vesselcode').val() + "&quoteno=" + quoteno + "&shipmentservicecode=" + $('#hidINServiceCode').val(),
                success: function (response) {
                    console.log("Inside ajax");
                    $("#Insurance_details").html(response);
                    $("#Insurance_details").show();
                    $('#isInsuranceadded').val('N');
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
        enableinsuranceswitchbtn();
        console.log("end getPremiumAmount ajax");
    }
    function enableAP() {
        $('#IN_SH').prop("checked", false);
        if ($('#IN_AP').attr("checked") == true || $('#IN_AP').attr("checked") == 'checked') {
            $('#IN_Country').text($('#ebook_agreecountry_name').val());
            $('#IN_PEmail').val($('#apemail').val());
        }
        enableinsuranceswitchbtn();
    }
    function enableSH() {
        $('#IN_AP').prop("checked", false);
        if ($('#IN_SH').prop("checked") == true || $('#IN_SH').attr("checked") == 'checked') {
            if ($('#ebook_shippername').val().length > 0) {
                $('#IN_Country').text($('#ebook_shippercountry').val());
                $('#IN_PEmail').val($('#ebook_shipperemail').val());
            } else {
                alert("Please enter the shipper customer details");
                $('#IN_SH').prop("checked", false);
                $('#IN_AP').prop("checked", true);
            }
        }
        enableinsuranceswitchbtn();
    }
    function addInsurance(quoteno) {
        var custdetails = "", comdval = "", isvalid = true, iscomdtyavable = false;
        if ($('#IN_SH').prop("checked") == true) {
            custdetails = "&custname=" + ($('#ebook_shippername').val().replaceAll("&","~~")) + "&address=" + $('#ebook_shipperaddress').val() + "&address1=" + $('#ebook_shipperaddress1').val() + "&city=" + $('#ebook_shippercity').val() + "&country=" + $('#ebook_shippercountry_name').val() + "&state=" + $('#ebook_shipperstate').val() + "&email=" + $('#IN_Email').val() + "&postcode=" + $('#ebook_shipperzipcode').val() + "&Pemail=" + $('#IN_PEmail').val() + "&custtype=SH";
        } else {
            custdetails = "&custname=" + ($('#apname').val().replaceAll("&","~~")) + "&address=" + $('#apaddr').val() + "&address1=" + $('#apaddr1').val() + "&city=" + $('#apCityName').val() + "&country=" + $('#apCountryName').val() + "&state=" + $('#apstate').val() + "&email=" + $('#IN_Email').val() + "&postcode=" + $('#apzipcode').val() + "&Pemail=" + $('#IN_PEmail').val() + "&custtype=AP";
        }

        $('#bkg_insurance').find('.commoditydiv').each(function () {
            if ($(this).find('.IN_Commodity').prop('checked') == true) {
                iscomdtyavable = true;
                comdval = comdval + $(this).find('.commodity_value').text() + "~~~" + $(this).find('.IN_Cargovalue').val() + "@@@@@";
                if ($(this).find('.IN_Cargovalue').val().length == 0) {
                    isvalid = false;
                }
            }
        });
        comdval = comdval.substring(0, comdval.length - 5);
        console.log(comdval);
        enableinsuranceswitchbtn();
        var typeval = 'quotecreate';
        if (quoteno.length > 0) {
            typeval = 'quoteupdate';
        }
        console.log("isvalid=> " + isvalid);
        if (isvalid && iscomdtyavable) {
            openLoader();
            $.ajax({
                type: "POST",
                url: "InsuranceAjax?type=" + typeval + "&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#SI_RefNo').val() + "&vesselcode=" + $('#vesselcode').val() + "&quoteno=" + quoteno + "&shipmentservicecode=" + $('#hidINServiceCode').val(),
                success: function (response) {
                    console.log("Inside ajax");
                    closeLoader();
                    if (response == 'success') {
                        if (quoteno.length > 0) {
                            alert("Insurance Updated Successfully!!");
                        } else {
                            alert("Insurance Created Successfully!!");
                        }
                        $('#isInsuranceadded').val('Y');
                        $('#isInsuranceavailable').val("Y");
                        $('#removeinsurancebtn').show();
                        $('#addinsurancebtn').hide();
                        if ($('#isrecalculated').length > 0 && $('#isrecalculated').val() == 'Y') {
                            $('#recalpremiumdetails').hide();
                            $('#mainpremiumdetails').show();
                        }
                        enableInsuranceInCargo();
                        //$("#Insurance_details").html(response);
                        // $.fancybox.close();
                    } else {
                        alert("Something went wrong. Please try again later!!");
                    }
                    //$("#Insurance_details").html(response);
                    // $.fancybox.close();
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
        console.log("end addInsurance ajax");
    }
    function enableinsuranceswitchbtn() {
        $('#bkg_insurance').find('.commoditydiv').each(function () {
            if ($(this).find('.IN_Commodity').prop('checked') == true) {
                var inscmdty = $(this).find('.commodity_value').text();
                var incargoval = $(this).find('.IN_Cargovalue').val();
                $("#cargo_details").find('.si_cargo_pkg_des').each(function () {
                    var comdhs = $(this).find('.commodity_new:nth-child(2)').val();
                    console.log("inscmdty => " + inscmdty);
                    console.log("comdhs => " + comdhs);
                    console.log("incargoval => " + incargoval);
                    console.log("if=> " + (inscmdty == comdhs));
                    if (inscmdty == comdhs) {
                        $(this).find('.insuranceswitchbtn').prop("checked", true);
                        $(this).find('.incargoval').val(incargoval);
                        if ($('#IN_SH').prop("checked") == true) {
                            $(this).find('.inquotecusttype').val("SH");
                        } else {
                            $(this).find('.inquotecusttype').val("AP");
                        }
                        var quote_no = $('#In_quoteno').val();
                        if (quote_no != '' && quote_no != 'null' && typeof quote_no !== 'undefined' && quote_no.length > 0) {
                            $(this).find('.inquoteno').val(quote_no);
                        }
                    }
                });

            }
        });

    }
    function enableInsuranceInCargo() {
        $('#bkg_insurance').find('.commoditydiv').each(function () {
            if ($(this).find('.IN_Commodity').prop('checked') == true) {
                var inscmdty = $(this).find('.commodity_value').text();
                var incargoval = $(this).find('.IN_Cargovalue').val();
                console.log("incargoval => " + incargoval);
                $("#cargo_details").find('.si_cargo_pkg_des').each(function () {
                    var comdhs = $(this).find('.commodity_new:nth-child(2)').val();
                    console.log("inscmdty => " + inscmdty);
                    console.log("comdhs => " + comdhs);
                    console.log('if=> ' + (inscmdty == comdhs))
                    if (inscmdty == comdhs) {
                        $(this).find('.inavailable').val("Y");
                        var quote_no = $('#In_quoteno').val();
                        if (quote_no != '' && quote_no != 'null' && typeof quote_no !== 'undefined' && quote_no.length > 0) {
                            $(this).find('.inquoteno').val(quote_no);
                        }
                    }
                });
            }
        });
    }

    function loadQuoteAmountDetails(quote) {
        console.log('loadQuoteAmountDetails=> ' + quote);
        openLoader();
        $.ajax({
            type: "POST",
            cache: false,
            url: "InsuranceAjax?type=quoteview&logotype=N&iseditload=Y&quoteno=" + quote,
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
    function removeInsuranceSurvey(quote) {
        console.log('removeInsuranceSurvey=> ' + quote);
        if (quote != '' && quote.length > 0) {

            $.ajax({
                type: "POST",
                cache: false,
                url: "InsuranceSurvey.jsp?quoteno=" + quote + "&bookno=" + $('#lrpbookno').val(),
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    $.fancybox.open(data, {
                        padding: 0,
                        showCloseButton: false,
                        closeClick: false,
                        maxWidth: 1200,
                        maxHeight: 600,
                        fitToView: true,
                        async: false,
                        width: '90%',
                        height: '100%',
                        autoSize: true,
                        scrollOutside: true,
                        closeBtn: true,
                        hideOnOverlayClick: false,
                        overlayShow: false,
                        helpers: {
                            overlay: {closeClick: false}
                        },
                        onClosed: function () {
                            closeLoader();
                        }

                    });
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else {
            removeInsurance(quote);
        }
    }
    function validateSurvey(quote) {
        if ($("input[name='insurance_survey']:checked").length > 0) {
            removeInsurance(quote);
        } else {
            alert("Please select the survey!!");
        }

    }
    function removeInsurance(quote) {
        console.log('removeInsurance=> ' + quote);
        openLoader();
        var surveymsg = '', book_no = '', surveycustominput = '', contactme = '';
        if (quote != '' && quote.length > 0) {
            book_no = $('#surveybook_no').val();
            contactme = $('#insurance_survey_improve_msg:checked').val();
            surveycustominput = $('#insurance_survey_input').val();
            surveymsg = '';
            $("input[name='insurance_survey']:checked").each(function () {
                surveymsg = surveymsg + "," + $(this).val();
            });
            if (surveymsg.length > 1) {
                surveymsg = surveymsg.substring(1);
            }
            console.log(surveymsg);
        }
        $.ajax({
            type: "POST",
            cache: false,
            url: "InsuranceAjax?type=removeInsurance&quoteno=" + quote + "&surveybook_no=" + book_no + "&surveymsg=" + surveymsg + "&surveycustominput=" + surveycustominput + "&contactme=" + contactme,
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                console.log("Inside ajax removeInsurance");
                closeLoader();
                if (data == 'success') {
                    alert("Insurance removed Successfully!!");
                    $("#Insurance_details").html("");
                    $('#isInsuranceadded').val('');
                    $('#isInsuranceavailable').val("N");
                    $("#IN_hidquoteno").val('');

                    if (quote != '' && quote.length > 0) {
                        $.fancybox.close();
                        $("#datatablebycargodiv").find('.preview').each(function () {
                            $(this).find('.inquoteno').val('');
                            $(this).find('.inavailable').val('N');
                        });
                    }
                } else {
                    alert(data);
                }
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function insurancenotneeded(obj) {
        if ($(obj).prop('checked') == true) {
            if ($('#isInsuranceavailable').val() == 'Y') {
                alert("Insurance already added. Please click on Remove Insurance.");
                $(obj).prop('checked', false);
            } else {
               // doNotInsured();
                $('#bkg_insurance').find('.commoditydiv').each(function () {
                    $(this).find('.IN_Cargovalue').val('');
                    $(this).find('.IN_Cargovalue').prop('disabled', true);
                });
                $('#calpremiumbtn').addClass('disabled');
                $('#Insurance_details').html("");
                $('#isInsuranceadded').val('N');
                $('#isInsuranceavailable').val("N");
            }
        } else {
            $('#bkg_insurance').find('.commoditydiv').each(function () {
                $(this).find('.IN_Cargovalue').val('');
                $(this).find('.IN_Cargovalue').prop('disabled', false);
                $(this).find('.IN_Commodity').prop('checked', true);
            });
            $('#calpremiumbtn').removeClass('disabled');
            $('#Insurance_details').html("");          
        }
    }
    function doNotInsured() {
        var custdetails = "", comdval = "";
        if ($('#IN_SH').prop("checked") == true) {
            custdetails = "&custname=" + ($('#ebook_shippername').val().replaceAll("&","~~")) + "&address=" + $('#ebook_shipperaddress').val() + "&address1=" + $('#ebook_shipperaddress1').val() + "&city=" + $('#ebook_shippercity').val() + "&country=" + $('#ebook_shippercountry_name').val() + "&state=" + $('#ebook_shipperstate').val() + "&email=" + $('#IN_Email').val() + "&postcode=" + $('#ebook_shipperzipcode').val() + "&Pemail=" + $('#IN_PEmail').val() + "&custtype=SH";
        } else {
            custdetails = "&custname=" + ($('#apname').val().replaceAll("&","~~")) + "&address=" + $('#apaddr').val() + "&address1=" + $('#apaddr1').val() + "&city=" + $('#apCityName').val() + "&country=" + $('#apCountryName').val() + "&state=" + $('#apstate').val() + "&email=" + $('#IN_Email').val() + "&postcode=" + $('#apzipcode').val() + "&Pemail=" + $('#IN_PEmail').val() + "&custtype=AP";
        }
        console.log("custdetails=> " + custdetails);
        $('#bkg_insurance').find('.commoditydiv').each(function () {
            if ($(this).find('.IN_Commodity').prop('checked') == true) {
                comdval = comdval + $(this).find('.commodity_value').text() + "~~~" + $(this).find('.IN_Cargovalue').val() + "@@@@@";
                if ($(this).find('.IN_Cargovalue').val().length == 0) {
                }
            }
        });
        console.log(comdval);
        comdval = comdval.substring(0, comdval.length - 5);
        console.log("InsuranceAjax?type=optout&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#SI_RefNo').val() + "&vesselcode=" + $('#vesselcode').val());
        openLoader();
        $.ajax({
            type: "POST",
            url: "InsuranceAjax?type=optout&origin=" + $('#IN_Origin').text() + "&delivery=" + $('#IN_Delivery').text() + "&vessel=" + $('#IN_Vessel').text() + "&shipmentdate=" + $('#IN_Shipmentdate').text() + "&commodityvalue=" + comdval + custdetails + "&onlinebookno=" + $('#SI_RefNo').val() + "&vesselcode=" + $('#vesselcode').val() + "&shipmentservicecode=" + $('#hidINServiceCode').val(),
            success: function (response) {
                console.log("Inside ajax");
                $("#Insurance_details").html(response);
                $("#Insurance_details").show();
                $('#isInsuranceadded').val('N');
                $('#isInsuranceavailable').val("N");
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
<%}%>