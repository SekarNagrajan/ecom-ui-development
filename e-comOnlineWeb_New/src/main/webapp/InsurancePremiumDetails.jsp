<%-- 
    Document   : InsurancePremiumDetails
    Created on : Oct 30, 2021, 10:40:53 AM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.common.Currencyformat"%>
<%@page import="net.ecom.insurance.vo.SwissApiOutput,java.util.List,net.ecom.insurance.vo.InsuredGood,net.ecom.common.Validation,java.util.Map,net.ecom.common.NumberFormat,java.util.Hashtable,java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<%
     String clientcode = (String) session.getAttribute("clientcode");
    String ViewType = Validation.checkNull((String) request.getAttribute("ViewType"));
    String Insurance_terms_condition=null;
    String SelectedLanguage = (String) session.getAttribute("SelectedLanguage");
    if("CH".equalsIgnoreCase(SelectedLanguage)){
        Insurance_terms_condition = (String) getServletContext().getAttribute("Insurance_Terms_and_Conditions_Lang");
    } else {
        Insurance_terms_condition = (String) getServletContext().getAttribute("Insurance_Terms_and_Conditions");
    }
    Hashtable comlist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
    String isrecalculated = Validation.checkNull((String) request.getAttribute("isrecalculated"));
    String blockedcommodity = "";
%>
<div class="card" >
    <%if (("view".equalsIgnoreCase(ViewType))) {%>
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
    <%}%>
    <div class="card-body">
        <%
            String errorOccured = Validation.checkNull((String) request.getAttribute("ErrorOccured"));
            if (!"Y".equalsIgnoreCase(errorOccured)) {
        %>
        <div id="mainpremiumdetails" <%if ("Y".equalsIgnoreCase(isrecalculated)) {%>style="display: none" <%}%>>
            <table class="table" id="insurance_premium_table">
                <thead class="thead-dark">
                    <tr>
                        <th scope="col"><bean:message key="ecom.insurance.commodity.hscode" bundle="<%=clientcode%>" /></th>
                        <th scope="col" class="text-right"><bean:message key="ecom.insurance.sum.insured" bundle="<%=clientcode%>" /></th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        SwissApiOutput quote = (SwissApiOutput) request.getAttribute("CreateQuoteDetails");
                        String quoteno = Validation.checkNull((String) request.getAttribute("quoteno"));
                        List quotelis = quote.getInsuredGoods();
                        double suminsured = 0;
                        String bgcolor = null;
                        if (quotelis != null && quotelis.size() > 0) {
                            for (int i = 0; i < quotelis.size(); i++) {
                                InsuredGood ig = (InsuredGood) quotelis.get(i);
                                if (ig.isIsCovered()) {
                                    suminsured = suminsured + Double.parseDouble(ig.getSumInsured());
                                } else {
                                    blockedcommodity = blockedcommodity + ig.getHarmonizedSystemCode() + "~~";
                                }
                                bgcolor = "";
                                if (i % 2 == 1) {
                                    bgcolor = "background-color: #f9f9f9;";//#e7e7e7;
                                }
                    %>
                    <tr style="<%=bgcolor%>">
                        <td><%=Validation.checkNull((String) comlist.get(ig.getHarmonizedSystemCode())) + " ~~ " + ig.getHarmonizedSystemCode()%></td>
                        <% if (ig.isIsCovered()) {%>
                        <td class="text-right"><%=Validation.checkNull(Currencyformat.USDFormat(ig.getSumInsured())) + " " + ig.getCurrency().toUpperCase()%></td>
                        <%} else {%>
                        <td class="text-right"><bean:message key="ecom.insurance.commodity.cannot.be.insured" bundle="<%=clientcode%>" /></td>
                        <%}%>
                    </tr>

                    <%}
                        String suminsuredstr = Validation.checkNull(Currencyformat.USDFormat(String.valueOf(suminsured)));
                    %>
                    <tr>
                        <td style="font-weight: bold;"><bean:message key="ecom.insurance.total.amount" bundle="<%=clientcode%>" /></td>
                        <td style="color: #2196f3;font-weight: bolder;font-size: 18px !important;" class="text-right"><%=suminsuredstr + " " + quote.getCurrency().toUpperCase()%></td>
                    </tr>
                    <% }%>
                </tbody>
            </table>

            <div class="form-group row">
                <div class="col-sm-12">
                    <span class="col-sm-6" style="background: #96eba8;padding-top: 7px;display: inline-flex;position: relative;top: 5px;">
                        <label class="col-sm-6"><h3><bean:message key="ecom.insurance.premium" bundle="<%=clientcode%>" /> </h3></label>
                        <label class="col-sm-6">
                            <h3><%=Validation.checkNull(Currencyformat.USDFormat(quote.getTotalPremium())) + " " + quote.getCurrency().toUpperCase()%></h3>
                            <input type="hidden" id="In_quoteno" value="<%=Validation.checkNull(quote.getQuoteNumber())%>">
                        </label>
                    </span>
                    <span class="col-sm-5">
                        <%if (!("view".equalsIgnoreCase(ViewType))) {
                                String removebtnstyle = (quoteno.length() == 0 ? "display: none;" : "");
                                String addbtnstyle = ("Y".equalsIgnoreCase((String) request.getAttribute("iseditload")) ? "display: none;" : "");
                        %>
                        <a href="javascript:void(0)" class="btn btn-info" style="padding:12px 65px;<%=addbtnstyle%>" id="addinsurancebtn" onclick="addInsurance('<%=quoteno%>')" > <i class="fa fa-plus-circle"></i> <bean:message key="ecom.insurance.addinsurance" bundle="<%=clientcode%>" /> </a>
                        <a href="javascript:void(0)" class="btn btn-danger" style="padding:12px 65px;<%=removebtnstyle%>" id="removeinsurancebtn" onclick="removeInsuranceSurvey('<%=quoteno%>')"> <i class="fa fa-trash"></i> <bean:message key="ecom.insurance.removeinsurance" bundle="<%=clientcode%>" />  </a>
                        <%}%>
                    </span>
                </div>
            </div>
        </div>
        <%if ("Y".equalsIgnoreCase(isrecalculated)) {%>
        <div id="recalpremiumdetails">
            <input type="hidden" id="isrecalculated" value="<%=isrecalculated%>">
            <div id="mainpremiumdetails">
                <table class="table" id="insurance_premium_table">
                    <thead class="thead-dark">
                        <tr>
                            <th scope="col"><bean:message key="ecom.insurance.commodity.hscode" bundle="<%=clientcode%>" /></th>
                            <th scope="col" class="text-right"><bean:message key="ecom.insurance.current.sum.insured" bundle="<%=clientcode%>" /></th>
                            <th scope="col" class="text-right"><bean:message key="ecom.insurance.new.sum.insured" bundle="<%=clientcode%>" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            Map recalculatedpremium = (Map) request.getAttribute("recalculatedpremium");
                            quoteno = Validation.checkNull((String) request.getAttribute("quoteno"));
                            double currentsuminsured = 0, newsuminsured = 0;
                            Map innermap = null;
                            if (recalculatedpremium != null && recalculatedpremium.size() > 0) {
                                Iterator it = recalculatedpremium.entrySet().iterator();
                                int count = 0;

                                while (it.hasNext()) {
                                    Map.Entry pairs = (Map.Entry) it.next();
                                    innermap = (Map) pairs.getValue();

                                    currentsuminsured = currentsuminsured + ((innermap.get("currentpremium") != null) ? Double.parseDouble((String) innermap.get("currentpremium")) : 0);
                                    if (((boolean) innermap.get("isIsCovered"))) {
                                        newsuminsured = newsuminsured + ((innermap.get("newpremium") != null) ? Double.parseDouble((String) innermap.get("newpremium")) : 0);
                                    } else {
                                        blockedcommodity = blockedcommodity + pairs.getKey() + "~~";
                                    }
                                    bgcolor = "";
                                    if (count % 2 == 1) {
                                        bgcolor = "background-color: #f9f9f9;";//#e7e7e7;
                                    }
                                    count++;
                        %>
                        <tr style="<%=bgcolor%>">
                            <td><%=Validation.checkNull((String) comlist.get(pairs.getKey())) + " ~~ " + pairs.getKey()%></td>
                            <td class="text-right"><%=Validation.checkNull(Currencyformat.USDFormat((String) innermap.get("currentpremium"))) + " " + ((innermap.get("currentpremium")) != null ? ((String) request.getAttribute("currentpremiumcurrency")).toUpperCase() : "")%></td>
                            <%if (((boolean) innermap.get("isIsCovered"))) {%>
                            <td class="text-right"><%=Validation.checkNull(Currencyformat.USDFormat((String) innermap.get("newpremium"))) + " " + ((innermap.get("newpremium")) != null ? ((String) request.getAttribute("newpremiumcurrency")).toUpperCase() : "")%></td>
                            <%} else {%>
                            <td class="text-right"><bean:message key="ecom.insurance.commodity.cannot.be.insured" bundle="<%=clientcode%>" /></td>
                            <%}%>

                        </tr>

                        <%}
                            String currentsuminsuredstr = Validation.checkNull(Currencyformat.USDFormat(String.valueOf(currentsuminsured)));
                            String newsuminsuredstr = Validation.checkNull(Currencyformat.USDFormat(String.valueOf(newsuminsured)));
                        %>
                        <tr>
                            <td style="font-weight: bold;"><bean:message key="ecom.insurance.total.amount" bundle="<%=clientcode%>" /></td>
                            <td style="color: #2196f3;font-weight: bolder;font-size: 18px !important;" class="text-right"><%=currentsuminsuredstr + " " + ((String) request.getAttribute("currentpremiumcurrency")).toUpperCase()%></td>
                            <td style="color: #2196f3;font-weight: bolder;font-size: 18px !important;" class="text-right"><%=newsuminsuredstr + " " + ((String) request.getAttribute("newpremiumcurrency")).toUpperCase()%></td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>

                <div class="form-group row">
                    <div class="col-sm-12">
                        <span class="col-sm-6" style="background: #96eba8;padding-top: 7px;display: inline-flex;">
                            <label class="col-sm-6"><h3><bean:message key="ecom.insurance.current.premium" bundle="<%=clientcode%>" /> </h3></label>
                            <label class="col-sm-6">
                                <h3 style="margin-left: 38.6%;"><%=Validation.checkNull(Currencyformat.USDFormat(((String) request.getAttribute("currentpremiumtotal")))) + " " + ((String) request.getAttribute("currentpremiumcurrency")).toUpperCase()%></h3>
                                <input type="hidden" id="In_quoteno" value="<%=Validation.checkNull(quote.getQuoteNumber())%>">
                            </label>      
                        </span>
                        <span class="col-sm-5">

                        </span>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-sm-12">
                        <span class="col-sm-6" style="background: #f7d47c;padding-top: 7px;display: inline-flex;position: relative;top: 5px;">
                            <label class="col-sm-6"><h3><bean:message key="ecom.insurance.update.premium" bundle="<%=clientcode%>" /> </h3></label>
                            <label class="col-sm-6">
                                <h3 style="margin-left: 38.6%;"><%=Validation.checkNull(Currencyformat.USDFormat(((String) request.getAttribute("newpremiumtotal"))) + " " + ((String) request.getAttribute("newpremiumcurrency")).toUpperCase())%></h3>
                            </label>
                        </span>
                        <span class="col-sm-5">
                            <%if (!("view".equalsIgnoreCase(ViewType))) {
                                    String removebtnstyle = (quoteno.length() == 0 ? "display: none;" : "");
                                    String addbtnstyle = ("Y".equalsIgnoreCase((String) request.getAttribute("iseditload")) ? "display: none;" : "");
                            %>
                            <a href="javascript:void(0)" class="btn btn-info" style="padding:12px 65px;<%=addbtnstyle%>" id="addinsurancebtn" onclick="addInsurance('<%=quoteno%>')" > <i class="fa fa-plus-circle"></i> <bean:message key="ecom.insurance.update.insurance" bundle="<%=clientcode%>" /> </a>
                            <a href="javascript:void(0)" class="btn btn-danger" style="padding:12px 65px;<%=removebtnstyle%>" id="removeinsurancebtn" onclick="removeInsuranceSurvey('<%=quoteno%>')"> <i class="fa fa-trash"></i> <bean:message key="ecom.insurance.removeinsurance" bundle="<%=clientcode%>" /> </a>
                            <%}%>
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
        <%if (!("view".equalsIgnoreCase(ViewType))) {%>
        <span class="text-info mousehover_note" style="display: none;"> * Click add button to add the insurance to this booking.</span>
        <%}%>
        <%if (("view".equalsIgnoreCase(ViewType))) {%>
        <%=Insurance_terms_condition%>
        <%}%>
        <%} else { %>
        <table class="table">
            <thead class="thead-dark">
                <tr>
                    <th scope="col">S.No</th>
                    <th scope="col">Validation Failure</th>
                </tr>
            </thead>
            <tbody>
                <%
                    Map errormap = (Map) request.getAttribute("ErrorMessage");
                    if (errormap != null) {
                        List errlis = (List) errormap.get("errors");
                        if (errlis != null && errlis.size() > 0) {
                            for (int i = 0; i < errlis.size(); i++) {
                                errormap = (Map) errlis.get(i);
                %>
                <tr>
                    <td><%=i + 1%></td>
                    <td><%=Validation.checkNull((String) errormap.get("message"))%></td>

                </tr>

                <%}
                        }
                    }%>
            </tbody>
        </table>


        <%}%>
    </div>
</div>
<script type='text/javascript'>
    <%if (blockedcommodity != null && blockedcommodity.length() > 0) {
            blockedcommodity = blockedcommodity.substring(0, blockedcommodity.length() - 2);
    %>
    disableBlockedComdty();
    function disableBlockedComdty() {
        var blockcomdty = '<%=Validation.checkNull(blockedcommodity)%>';
        console.log(blockcomdty);
        if (blockcomdty.length > 0) {
            const blockarray = blockcomdty.split("~~");
            $('#bkg_insurance').find('.commoditydiv').each(function () {
                var inscmdty = $(this).find('.commodity_value').text();
                var comcode = $.trim(inscmdty.split("~~")[1]);
                console.log(comcode);
                console.log(blockarray);
                if (blockarray.includes(comcode)) {
                    $(this).find('.IN_Commodity').prop('checked', false);
                    $(this).find('.IN_Commodity').prop('disabled', true);
                    $(this).find('.IN_Cargovalue').val('');
                    $(this).find('.IN_Cargovalue').prop('disabled', true);
                }
            });
        }
    }
    <%}%>
</script>