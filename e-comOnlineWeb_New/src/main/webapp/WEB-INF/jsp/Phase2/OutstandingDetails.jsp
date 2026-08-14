<%--
    Document   : OutstandingDetails
    Created on : Apr 30, 2015, 2:25:26 PM
    Author     : deepa.p
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Map,java.util.List,net.ecom.phase2.service.vo.AmountSummaryVO,java.util.Hashtable,net.ecom.common.Validation,net.ecom.phase2.service.vo.OutstandingSummaryVO,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    Map<String, List> mainList = (Map<String, List>) session.getAttribute("OUTSTANDINGSUMMARYLIST");
    if (mainList != null) {
        List<OutstandingSummaryVO> outSmryList = (List<OutstandingSummaryVO>) mainList.get("OutstandingSummary");
        List<AmountSummaryVO> amtSmryList = (List<AmountSummaryVO>) mainList.get("AmountSummary");
        OutstandingSummaryVO summaryVO = null;
        AmountSummaryVO amtVO = null;
%>
<div class="form-group">

    <div class="header_tle"> <bean:message key="ecom.cs.outstdet" bundle="<%=clientcode%>"/>
 </div>

</div>
<div class="table-responsive card card-body card-shadow" style="clear:both">
    <table class="table table-striped table-bordered" id="outSmryTable">
        <thead>
            <tr align="center">
                <th><bean:message key="ecom.arn.blno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.invdate" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.invnum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.tariff.loccurrency" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.invamt" bundle="<%=clientcode%>"/></th>
                <th>ROE</th>
                <th><bean:message key="ecom.cs.invamtinusd" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.ageing" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.recptno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.paidamt" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.paymode" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.balamt" bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%
                String invAmount = "0.00", invAmountUSD = "0.00", payAmtUSD = "0.00", excessAmtUSD = "0.00", outAmtUSD = "0.00";
                DecimalFormat df = new DecimalFormat("0.00");
                if (outSmryList != null) {
                    for (int i = 0; i < outSmryList.size(); i++) {
                        summaryVO = (OutstandingSummaryVO) outSmryList.get(i);
                        invAmount = summaryVO.getInvAmount();
                        invAmount = invAmount != null && invAmount.length() > 0 ? df.format(Double.parseDouble(invAmount)) : "0.00";
                        invAmountUSD = summaryVO.getInvAmountUSD();
                        invAmountUSD = invAmountUSD != null && invAmountUSD.length() > 0 ? df.format(Double.parseDouble(invAmountUSD)) : "0.00";
                        payAmtUSD = summaryVO.getPaidAmtUSD();
                        payAmtUSD = payAmtUSD != null && payAmtUSD.length() > 0 ? df.format(Double.parseDouble(payAmtUSD)) : "0.00";
                        excessAmtUSD = summaryVO.getExcessAmtUSD();
                        excessAmtUSD = excessAmtUSD != null && excessAmtUSD.length() > 0 ? df.format(Double.parseDouble(excessAmtUSD)) : "0.00";
                        outAmtUSD = summaryVO.getOutStandingAmtUSD();
                        outAmtUSD = outAmtUSD != null && outAmtUSD.length() > 0 ? df.format(Double.parseDouble(outAmtUSD)) : "0.00";
            %>
            <tr align="center">
                <td><%=Validation.checkNull(summaryVO.getBlNo())%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormat(summaryVO.getInvoiceDate()))%></td>
                <td><%=Validation.checkNull(summaryVO.getInvoiceNo())%></td>
                <td><%=Validation.checkNull(summaryVO.getInvCurrency())%></td>
                <td><%=invAmount%></td>
                <td><%=Validation.checkNull(summaryVO.getInvROE())%></td>
                <td><%=invAmountUSD%></td>
                <td><%=Validation.checkNull(summaryVO.getAgeing())%></td>
                <td><%=Validation.checkNull(summaryVO.getReceiptNo())%></td>
                <td><%=payAmtUSD%></td>
                <td><%=Validation.checkNull(summaryVO.getPaymentMode())%></td>
                <td><%=outAmtUSD%></td>
            </tr>
            <%                                }

                }

            %>
        </tbody>
    </table>
</div>
<div class="form-group">

    <div class="header_tle"> <bean:message key="ecom.cs.currencybal" bundle="<%=clientcode%>"/> </div>

</div>
<div class="table-responsive card card-body card-shadow" style="clear:both">
    <table class="table table-striped table-bordered" id="outcurSmryTable">
        <thead>
            <tr align="center">
                <th><bean:message key="ecom.tariff.loccurrency" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.invamt" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.invamtusd" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.paidamtusd" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.excessamtusd" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.balamt" bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%                if (amtSmryList != null) {
                    invAmount = "0.00";
                    invAmountUSD = "0.00";
                    payAmtUSD = "0.00";
                    excessAmtUSD = "0.00";
                    outAmtUSD = "0.00";
                    for (int i = 0; i < amtSmryList.size(); i++) {
                        amtVO = (AmountSummaryVO) amtSmryList.get(i);
                        invAmount = amtVO.getInvAmount();
                        invAmount = invAmount != null && invAmount.length() > 0 ? df.format(Double.parseDouble(invAmount)) : "0.00";
                        invAmountUSD = amtVO.getInvAmtUSD();
                        invAmountUSD = invAmountUSD != null && invAmountUSD.length() > 0 ? df.format(Double.parseDouble(invAmountUSD)) : "0.00";
                        payAmtUSD = amtVO.getPayAmtUSD();
                        payAmtUSD = payAmtUSD != null && payAmtUSD.length() > 0 ? df.format(Double.parseDouble(payAmtUSD)) : "0.00";
                        excessAmtUSD = amtVO.getExcessAmtUSD();
                        excessAmtUSD = excessAmtUSD != null && excessAmtUSD.length() > 0 ? df.format(Double.parseDouble(excessAmtUSD)) : "0.00";
                        outAmtUSD = amtVO.getOutStandingAmtUSD();
                        outAmtUSD = outAmtUSD != null && outAmtUSD.length() > 0 ? df.format(Double.parseDouble(outAmtUSD)) : "0.00";
            %>
            <tr align="center">
                <td><%=Validation.checkNull(amtVO.getInvCurrency())%></td>
                <td><%=invAmount%></td>
                <td><%=invAmountUSD%></td>
                <td><%=payAmtUSD%></td>
                <td><%=excessAmtUSD%></td>
                <td><%=outAmtUSD%></td>
            </tr>
            <%                                }
                }

            %>
        </tbody>
    </table>
</div>

<%    }
%>

<script type="text/javascript">
    $(document).ready(function () {
        $('#outSmryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        //cfilter("outcurSmryTable", "pfiltercolumndata", ptable, 1);
        $('#outcurSmryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
       // cfilter("outcurSmryTable", "pfiltercolumndata", ptable1, 1);
    });


</script>
