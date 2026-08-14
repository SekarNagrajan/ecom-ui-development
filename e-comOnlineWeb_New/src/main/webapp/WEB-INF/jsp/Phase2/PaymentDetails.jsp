<%--
    Document   : PaymentDetails
    Created on : Apr 30, 2015, 2:25:40 PM
    Author     : deepa.p
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Map,java.util.List,net.ecom.phase2.service.vo.AmountSummaryVO,net.ecom.phase2.service.vo.PaymentSummaryVO,java.util.Hashtable,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%
        String clientcode = (String) session.getAttribute("clientcode");

    Map<String, List> mainList = (Map<String, List>) session.getAttribute("PAYMENTSUMMARYLIST");
    if (mainList != null) {
        List<PaymentSummaryVO> paymentSmryList = (List<PaymentSummaryVO>) mainList.get("PaymentSummary");
        List<AmountSummaryVO> amtSmryList = (List<AmountSummaryVO>) mainList.get("AmountSummary");
        PaymentSummaryVO summaryVO = null;
        AmountSummaryVO amtVO = null;
%>
<div class="form-group">

    <div class="header_tle"> <bean:message key="ecom.cs.paymntdet" bundle="<%=clientcode%>"/></div>

</div>
<div class="table-responsive card card-body card-shadow" style="clear:both">
    <table class="table table-striped table-bordered" id="paySmryTable">
        <thead>
            <tr align="center">
                <th><bean:message key="ecom.cs.receiptnum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.recptdate" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.paymode" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.paytype" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.paidamt" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.paidamtusd" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.excessamt" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.invoino" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.booknum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.blno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.vessel" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.voyage" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cro.print" bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%
                Hashtable vessellist = (Hashtable) getServletContext().getAttribute("SESSION_VESSELLIST");
                DecimalFormat df = new DecimalFormat("0.00");
                String payAmt = "0.00", payAmtUSD = "0.00", excessAmtUSD = "0.00";
                if (paymentSmryList != null) {
                    for (int i = 0; i < paymentSmryList.size(); i++) {
                        summaryVO = (PaymentSummaryVO) paymentSmryList.get(i);
                        payAmt = summaryVO.getPaidAmt();
                        payAmt = payAmt != null && payAmt.length() > 0 ? df.format(Double.parseDouble(payAmt)) : "0.00";
                        payAmtUSD = summaryVO.getPaidAmtUSD();
                        payAmtUSD = payAmtUSD != null && payAmtUSD.length() > 0 ? df.format(Double.parseDouble(payAmtUSD)) : "0.00";
                        excessAmtUSD = summaryVO.getExcessAmtUSD();
                        excessAmtUSD = excessAmtUSD != null && excessAmtUSD.length() > 0 ? df.format(Double.parseDouble(excessAmtUSD)) : "0.00";
            %>
            <tr align="center">
                <td><%=Validation.checkNull(summaryVO.getReceiptNo())%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormat(summaryVO.getReceiptDate()))%></td>
                <td><%=Validation.checkNull(summaryVO.getPaymentMode())%></td>
                <td><%=Validation.checkNull(summaryVO.getPaymentType())%></td>
                <td><%=payAmt%></td>
                <td><%=payAmtUSD%></td>
                <td><%=excessAmtUSD%></td>
                <td><%=Validation.checkNull(summaryVO.getInvoiceNo())%></td>
                <td><%=Validation.checkNull(summaryVO.getBookNo())%></td>
                <td><%=Validation.checkNull(summaryVO.getBlNo())%></td>
                <td><%=Validation.checkNull((String) vessellist.get(Validation.checkNull(summaryVO.getVessel())))%></td>
                <td><%=Validation.checkNull(summaryVO.getVoyage())+Validation.checkNull(summaryVO.getBound())%></td>
                <td><a href="javascript:void(0);" onClick="viewRecpMethod('<%=summaryVO.getReceiptNo()%>')" class="btn btn-lg text-success" title="Print" data-toggle="tooltip"  data-placement="right" ><i class="fa fa-print"></i></a></td>
            </tr>
            <%                                }
                }
            %>
        </tbody>
    </table>
</div>
<div class="form-group">

    <div class="header_tle">  <bean:message key="ecom.cs.currencybal" bundle="<%=clientcode%>"/> </div>

</div>
<div class="table-responsive card card-body card-shadow" style="clear:both">
    <table class="table table-striped table-bordered" id="paycurSmryTable">
        <thead>
            <tr align="center">
                <th><bean:message key="ecom.cs.paidamtusd" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.excessamt" bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%                if (amtSmryList != null) {
                    payAmt = "0.00";
                    excessAmtUSD = "0.00";
                    for (int i = 0; i < amtSmryList.size(); i++) {
                        amtVO = (AmountSummaryVO) amtSmryList.get(i);
                        payAmt = amtVO.getPayAmtUSD();
                        payAmt = payAmt != null && payAmt.length() > 0 ? df.format(Double.parseDouble(payAmt)) : "0.00";
                        excessAmtUSD = amtVO.getExcessAmtUSD();
                        excessAmtUSD = excessAmtUSD != null && excessAmtUSD.length() > 0 ? df.format(Double.parseDouble(excessAmtUSD)) : "0.00";
            %>
            <tr align="center">
                <td><%=payAmt%></td>
                <td><%=excessAmtUSD%></td>
            </tr>
            <%                                }
                }

            %>
        </tbody>
    </table>
</div>

<%    }
%>
<div id="recpDialog">
</div>
<script type="text/javascript">
    var searchReqr = getXmlHttpRequestObject();
    function getXmlHttpRequestObject() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert("<bean:message key="ecom.msg.browsernotsupported" bundle="<%=clientcode%>"/>");
        }
    }
    var recpno;
    function viewRecpMethod(recpNumber) {
        openLoader();
        recpno = recpNumber;
        var url = "RecepitPrintAction.do?recpNumber=" + recpNumber;
        var params = "recpNumber=" + recpNumber;
        searchReqr.open("POST", url, true);
        searchReqr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        searchReqr.onreadystatechange = setRECPPrintDialog;
        searchReqr.send(params);
    }
    function setRECPPrintDialog() {
        if (searchReqr.readyState == 4 && searchReqr.status == 200) {
            document.getElementById("recpDialog").innerHTML = searchReqr.responseText;
            if (document.getElementById("oblmsgres").value == "fail")
            {
                closeLoader();
                return false
            } else if (document.getElementById("oblmsgres").value == "Success") {
                closeLoader();
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("payform").action = "ArrivalPrintServlet?refNumber=" + recpno + "&responseType=" + responseType + "&viewtype=" + viewtype;
                document.getElementById("payform").submit();
            } else
            {
                closeLoader();
                alert(document.getElementById("oblmsgres").value);
                return false
            }
        } else if (searchReqr.readyState == 4 && searchRequ.status == 403) {
            window.location.href = "SessionExpired.jsp";
        }
    }
    $(document).ready(function () {
        $('#paySmryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        //cfilter("paySmryTable", "pfiltercolumndata", ptable, 1);
        $('#paycurSmryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        //cfilter("paycurSmryTable", "pfiltercolumndata", ptable1, 1);
    });


</script>


