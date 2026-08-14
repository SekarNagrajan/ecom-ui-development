<%-- 
    Document   : NewInvoiceTableDet
    Created on : 8 Dec, 2020, 3:48:10 PM
    Author     : priyadharshinim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,java.util.Map,net.ecom.phase2.service.vo.CSInvoiceSummaryVO,net.ecom.common.Validation,net.ecom.phase2.service.vo.AmountSummaryVO,java.util.Hashtable,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<% String clientcode = (String) session.getAttribute("clientcode");
    Map<String, List> mainList = (Map<String, List>) session.getAttribute("CSINVOICESUMMARYLIST");
    if (mainList != null) {
        List<CSInvoiceSummaryVO> invSmryList = (List<CSInvoiceSummaryVO>) mainList.get("InvoiceSummary");
        List<AmountSummaryVO> amtSmryList = (List<AmountSummaryVO>) mainList.get("AmountSummary");
        CSInvoiceSummaryVO summaryVO = null;
        AmountSummaryVO amtVO = null;
       

%>
<div class="form-group">

    <div class="header_tle"> <bean:message key="ecom.do.invdet" bundle="<%=clientcode%>"/></div>

</div>
<div class="table-responsive card card-body card-shadow" style="clear:both">
    <table class="table table-striped table-bordered" id="invSmryTable">
        <thead>
            <tr align="center">
                <th><bean:message key="ecom.do.invnum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.invdate" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.head.invcurrency" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.invamt" bundle="<%=clientcode%>"/></th>
<!--                <th><bean:message key="ecom.do.deborcrenote" bundle="<%=clientcode%>"/></th>-->
                <th><bean:message key="Tracking.booknum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.arn.blno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="tracking.Service" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="tracking.Voyage" bundle="<%=clientcode%>"/></th>
<!--                <th><bean:message key="register.refno" bundle="<%=clientcode%>"/></th>-->
                <th><bean:message key="ecom.cro.print" bundle="<%=clientcode%>"/></th>
                <!--<th>Action</th>-->
            </tr>
        </thead>
        <tbody>
            <%
                Hashtable vessellist = (Hashtable) getServletContext().getAttribute("SESSION_VESSELLIST");
                double invAmt = 0;
                String strInvAmt = null, strInvType = null;
                DecimalFormat df = new DecimalFormat("0.00");
                String amount = "0.00";
                if (invSmryList != null) {
                    for (int i = 0; i < invSmryList.size(); i++) {
                        summaryVO = (CSInvoiceSummaryVO) invSmryList.get(i);
                        strInvAmt = summaryVO.getInvAmount() != null ? summaryVO.getInvAmount() : "0.00";
                        invAmt = Double.valueOf(strInvAmt);
                        amount = summaryVO.getInvAmount();
                        amount = amount != null && amount.length() > 0 ? df.format(Double.parseDouble(amount)) : "0.00";
                        strInvType = invAmt >= 0 ? "DEBIT NOTE " : "CREDIT NOTE";

            %>
            <tr align="center">
                <td><%=Validation.checkNull(summaryVO.getInvoiceNo())%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormat(summaryVO.getInvoiceDate()))%></td>
                <td><%=Validation.checkNull(summaryVO.getInvCurrency())%></td>
                <td><%=amount%></td>
<!--                <td><%=strInvType%></td>-->
                <td><%=Validation.checkNull(summaryVO.getBookNo())%></td>
                <td><%=Validation.checkNull(summaryVO.getBlNo())%></td>
                <td><%=Validation.checkNull(summaryVO.getService())%></td>
                <td><%=Validation.checkNull((String) vessellist.get(summaryVO.getVessel()))%></td>
                <td><%=Validation.checkNull(summaryVO.getVoyage())+Validation.checkNull(summaryVO.getBound())%></td>
<!--                <td><%=Validation.checkNull(summaryVO.getRefNo())%></td>-->
                <td><a href="javascript:void(0);" onClick="viewInvMethod('<%=summaryVO.getInvoiceNo()%>')" class="btn btn-lg text-success" title="Print" data-toggle="tooltip"  data-placement="right" ><i class="fa fa-print"></i></a></td>
            </tr>
            <%                                }
                }
            %>
        </tbody>
    </table>
</div>


<%    }
%>
<div id="INVDialog">
</div>
<script type="text/javascript">
    //Our XmlHttpRequest object to get the auto suggest
    var searchReqI = getXmlHttpRequestObject();
    function getXmlHttpRequestObject() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert("<bean:message key="ecom.msg.browsernotsupported" bundle="<%=clientcode%>"/>");
        }
    }
    var Invono;
    function viewInvMethod(InvNumber) {
        openLoader();
        Invono = InvNumber;
        var url = "InvoicePrintAction.do?InvNumber=" + InvNumber;
        var params = "InvNumber=" + InvNumber;
        searchReqI.open("POST", url, true);
        searchReqI.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        searchReqI.onreadystatechange = setINVPrintDialog;
        searchReqI.send(params);
    }
    function setINVPrintDialog() {
        if (searchReqI.readyState == 4 && searchReqI.status == 200) {
            document.getElementById("INVDialog").innerHTML = searchReqI.responseText;
            if (document.getElementById("oblmsgres").value == "fail")
            {
                closeLoader();
                return false
            } else if (document.getElementById("oblmsgres").value == "Success") {
                closeLoader();
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("invform").action = "ArrivalPrintServlet?refNumber=" + Invono + "&responseType=" + responseType + "&viewtype=" + viewtype;
                document.getElementById("invform").submit();
            } else
            {
                closeLoader();
                alert(document.getElementById("oblmsgres").value);
                return false
            }
        } else if (searchReqI.readyState == 4 && searchReqI.status == 403) {
            window.location.href = "SessionExpired.jsp";
        }
    }
    $(document).ready(function () {
        var ptable = $('#invSmryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        //cfilter("invSmryTable", "pfiltercolumndata", ptable, 1);
        var ptable1 = $('#invcurSmryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        //  cfilter("invcurSmryTable", "pfiltercolumndata", ptable1, 1);
    });


</script>


