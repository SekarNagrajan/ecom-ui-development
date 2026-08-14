<%--
    Document   : CSInvoiceDetails
    Created on : Apr 30, 2015, 2:25:11 PM
    Author     : deepa.p
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,java.util.Map,net.ecom.phase2.service.vo.CSInvoiceSummaryVO,net.ecom.common.Validation,net.ecom.phase2.service.vo.AmountSummaryVO,java.util.Hashtable,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat,net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<% String clientcode = (String) session.getAttribute("clientcode");
    String enableStripePayment  = (String) getServletContext().getAttribute(GeneralConf.Enable_Stripe_Payment);
    String enableNGeniusPayment = (String) getServletContext().getAttribute(GeneralConf.Enable_Ngenius_Payment);
    boolean isStripeEnabled  = "Y".equalsIgnoreCase(enableStripePayment);
    boolean isNGeniusEnabled = "Y".equalsIgnoreCase(enableNGeniusPayment);
    boolean isPaymentEnabled = isStripeEnabled || isNGeniusEnabled;
    String rawPaymentCurrency = isStripeEnabled ? (String) getServletContext().getAttribute(GeneralConf.Stripe_Payment_Currency)
            : (isNGeniusEnabled ? (String) getServletContext().getAttribute(GeneralConf.Ngenius_Payment_Currency) : null);
    boolean paymentCurrencyConfigured = rawPaymentCurrency != null && rawPaymentCurrency.trim().length() > 0;
    String paymentCurrency = paymentCurrencyConfigured ? rawPaymentCurrency.trim().toUpperCase() : "";
    String paymentCurrencyMsg = "Payment currency is not configured. Please contact the administrator.";
    Map<String, List> mainList = (Map<String, List>) session.getAttribute("CSINVOICESUMMARYLIST");
    if (mainList != null) {
        List<CSInvoiceSummaryVO> invSmryList = (List<CSInvoiceSummaryVO>) mainList.get("InvoiceSummary");
        List<AmountSummaryVO> amtSmryList = (List<AmountSummaryVO>) mainList.get("AmountSummary");
        CSInvoiceSummaryVO summaryVO = null;
        AmountSummaryVO amtVO = null;

%>
<%if (isPaymentEnabled) {
    int invJsNo = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>
<%if (isStripeEnabled) {%>
<script type="text/javascript" src="js/stripe-payment-util.js?<%=invJsNo%>"></script>
<script type="text/javascript">
    var csrfToken = '<%= session.getAttribute("STRIPE_CSRF_TOKEN") %>';
    if (typeof StripePaymentUtil !== 'undefined') { StripePaymentUtil.initializeCSRFToken(csrfToken); }
</script>
<%}%>
<%if (isNGeniusEnabled) {%>
<script type="text/javascript" src="js/ngenius-payment-util.js?<%=invJsNo%>"></script>
<script type="text/javascript">
    var ngeniusCsrfToken = '<%= session.getAttribute("NGENIUS_CSRF_TOKEN") %>';
    if (typeof NGeniusPaymentUtil !== 'undefined') { NGeniusPaymentUtil.initializeCSRFToken(ngeniusCsrfToken); }
</script>
<%}%>
<div id="invPaymentBar" class="ecom-pay-bar">
    <div class="ecom-pay-content">
        <div class="ecom-pay-info">
            <div class="ecom-pay-count"><span id="invSelectedCount">0</span> Invoice(s) selected</div>
            <div class="ecom-pay-total">Total: <span id="invTotalAmount"><%=paymentCurrencyConfigured ? paymentCurrency + " 0.00" : paymentCurrencyMsg%></span></div>
        </div>
        <div class="ecom-pay-actions">
            <input type="number" id="invEditableAmount" class="ecom-pay-amount" step="0.01" min="0" value="0.00" placeholder="Amount" readonly>
            <button type="button" class="ecom-pay-btn-clear" onclick="clearInvSelection()">Clear</button>
            <button type="button" class="ecom-pay-btn-pay" onclick="proceedToInvPayment()"><i class="fa fa-credit-card"></i> Pay Selected</button>
        </div>
    </div>
</div>
<%}%>
<div class="form-group">

    <div class="header_tle"> <bean:message key="ecom.do.invdet" bundle="<%=clientcode%>"/></div>

</div>
<div class="table-responsive card card-body card-shadow" style="clear:both">
    <table class="table table-striped table-bordered" id="invSmryTable">
        <thead>
            <tr align="center">
                <%if (isPaymentEnabled) {%>
                <th style="width:30px;text-align:center"><input type="checkbox" class="ecom-pay-checkbox" id="invSelectAll" onclick="toggleAllInvCheckboxes(this)"></th>
                <%}%>
                <th><bean:message key="ecom.do.invnum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.invdate" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.tariff.loccurrency" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.do.invamt" bundle="<%=clientcode%>"/></th>
                <%if (isPaymentEnabled) {%>
                <th><%=paymentCurrencyConfigured ? "Balance (" + paymentCurrency + ")" : "Balance"%></th>
                <%}%>
                <th><bean:message key="ecom.do.deborcrenote" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.booknum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.arn.blno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="tracking.Voyage" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="register.refno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cro.print" bundle="<%=clientcode%>"/></th>
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
                <%if (isPaymentEnabled) {
                    String invPayEligible = summaryVO.getIs_payment_eligible();
                    String invPayCompleted = summaryVO.getIs_payment_completed();
                    String invBalUsd = summaryVO.getInvBalAmountUSD() != null ? summaryVO.getInvBalAmountUSD() : "0";
                    String invCustType = summaryVO.getPay_customer_type();
                    boolean invCbEnabled = "Y".equalsIgnoreCase(invPayEligible) && !"Y".equalsIgnoreCase(invPayCompleted);
                %>
                <td style="text-align:center">
                    <%if ("Y".equalsIgnoreCase(invPayCompleted)) {%>
                        <span data-toggle="tooltip" title="Payment already completed"><input type="checkbox" class="ecom-pay-checkbox" disabled></span>
                    <%} else if (!invCbEnabled) {%>
                        <span data-toggle="tooltip" title="Not eligible for payment"><input type="checkbox" class="ecom-pay-checkbox" disabled></span>
                    <%} else {%>
                        <input type="checkbox" class="ecom-pay-checkbox inv-row-cb"
                               data-invno="<%=summaryVO.getInvoiceNo()%>"
                               data-amount="<%=invBalUsd%>"
                               data-custtype="<%=invCustType%>"
                               data-agencycode="<%=Validation.checkNull(summaryVO.getLocationCode())%>"
                               onchange="updateInvPaymentSummary()">
                    <%}%>
                </td>
                <%}%>
                <td><%=Validation.checkNull(summaryVO.getInvoiceNo())%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormat(summaryVO.getInvoiceDate()))%></td>
                <td><%=Validation.checkNull(summaryVO.getInvCurrency())%></td>
                <td><%=amount%></td>
                <%if (isPaymentEnabled) {%>
                <td><%
                    String balUsdDisplay = summaryVO.getInvBalAmountUSD();
                    balUsdDisplay = balUsdDisplay != null && balUsdDisplay.length() > 0 ? df.format(Double.parseDouble(balUsdDisplay)) : "0.00";
                %><%=balUsdDisplay%></td>
                <%}%>
                <td><%=strInvType%></td>
                <td><%=Validation.checkNull(summaryVO.getBookNo())%></td>
                <td><%=Validation.checkNull(summaryVO.getBlNo())%></td>
                <td><%=Validation.checkNull((String) vessellist.get(summaryVO.getVessel()))%></td>
                <td><%=Validation.checkNull(summaryVO.getVoyage())+Validation.checkNull(summaryVO.getBound())%></td>
                <td><%=Validation.checkNull(summaryVO.getRefNo())%></td>
                <td><a href="javascript:void(0);" onClick="viewInvMethod('<%=summaryVO.getInvoiceNo()%>')" class="btn btn-lg text-success" title="Print" data-toggle="tooltip"  data-placement="right" ><i class="fa fa-print"></i></a></td>
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
    <table class="table table-striped table-bordered" id="invcurSmryTable">
        <thead>
            <tr align="center">
                <th><bean:message key="ecom.tariff.loccurrency" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.cs.invamt" bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%                if (amtSmryList != null) {
                    String invcurbal = "0.00";
                    for (int i = 0; i < amtSmryList.size(); i++) {
                        amtVO = (AmountSummaryVO) amtSmryList.get(i);
                        invcurbal = amtVO.getCiInvAmount();
                        invcurbal = invcurbal != null && invcurbal.length() > 0 ? df.format(Double.parseDouble(invcurbal)) : "0.00";
            %>
            <tr align="center">
                <td><%=Validation.checkNull(amtVO.getCiInvCurrency())%></td>
                <td><%=invcurbal%></td>
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
            <%if (isPaymentEnabled) {%>
            ,"columnDefs": [{ "orderable": false, "targets": 0 }]
            <%}%>
        });
        var ptable1 = $('#invcurSmryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });

    <%if (isPaymentEnabled) {%>
    function toggleAllInvCheckboxes(source) {
        var checkboxes = document.querySelectorAll('.inv-row-cb:not(:disabled)');
        checkboxes.forEach(function(cb) { cb.checked = source.checked; });
        updateInvPaymentSummary();
    }

    function updateInvPaymentSummary() {
        var checked = Array.prototype.slice.call(document.querySelectorAll('.inv-row-cb:checked'));
        var bar = document.getElementById('invPaymentBar');
        var total = 0.00;

        if (checked.length > 0) {
            var firstAgency = checked[0].getAttribute('data-agencycode') || '';
            var mixedAgency = checked.some(function(cb) {
                return (cb.getAttribute('data-agencycode') || '') !== firstAgency;
            });

            if (mixedAgency) {
                alert('Selected invoices belong to different agencies. Please select invoices from the same agency only.');
                checked.forEach(function(cb) {
                    if ((cb.getAttribute('data-agencycode') || '') !== firstAgency) {
                        cb.checked = false;
                    }
                });
                checked = Array.prototype.slice.call(document.querySelectorAll('.inv-row-cb:checked'));
            }

            if (checked.length > 0) {
                checked.forEach(function(cb) {
                    total += parseFloat(cb.getAttribute('data-amount') || '0');
                });
                document.getElementById('invSelectedCount').textContent = checked.length;
                if (<%=paymentCurrencyConfigured%>) {
                    document.getElementById('invTotalAmount').textContent = '<%=paymentCurrency%> ' + total.toFixed(2);
                } else {
                    document.getElementById('invTotalAmount').textContent = '<%=paymentCurrencyMsg%>';
                }
                document.getElementById('invEditableAmount').value = total.toFixed(2);
                bar.style.display = 'block';
            } else {
                bar.style.display = 'none';
            }
        } else {
            bar.style.display = 'none';
        }

        var allCbs = document.querySelectorAll('.inv-row-cb:not(:disabled)');
        var selectAll = document.getElementById('invSelectAll');
        if (selectAll) {
            selectAll.checked = allCbs.length > 0 && checked.length === allCbs.length;
            selectAll.indeterminate = checked.length > 0 && checked.length < allCbs.length;
        }
    }

    function clearInvSelection() {
        var checkboxes = document.querySelectorAll('.inv-row-cb');
        checkboxes.forEach(function(cb) { cb.checked = false; });
        var selectAll = document.getElementById('invSelectAll');
        if (selectAll) { selectAll.checked = false; selectAll.indeterminate = false; }
        updateInvPaymentSummary();
    }

    function proceedToInvPayment() {
        if (!<%=paymentCurrencyConfigured%>) {
            alert('<%=paymentCurrencyMsg%>');
            return;
        }
        var checked = document.querySelectorAll('.inv-row-cb:checked');
        if (checked.length === 0) {
            alert('Please select at least one Invoice to pay.');
            return;
        }

        var requestedAmount = parseFloat(document.getElementById('invEditableAmount').value);
        if (isNaN(requestedAmount) || requestedAmount <= 0) {
            alert('Please enter a valid payment amount.');
            document.getElementById('invEditableAmount').focus();
            return;
        }

        var references = [];
        var agencyCode = '';
        var mixedAgency = false;
        checked.forEach(function(cb) {
            references.push({
                refNo: cb.getAttribute('data-invno'),
                amount: parseFloat(cb.getAttribute('data-amount') || '0'),
                customerType: cb.getAttribute('data-custtype') || 'IN'
            });
            var cbAgency = cb.getAttribute('data-agencycode') || '';
            if (!agencyCode) {
                agencyCode = cbAgency;
            } else if (cbAgency !== agencyCode) {
                mixedAgency = true;
            }
        });

        if (mixedAgency) {
            alert('Selected invoices belong to different agencies. Please select invoices from the same agency only.');
            return;
        }

        var paymentData = {
            paymentType: 'INV',
            agencyCode: agencyCode,
            totalAmount: requestedAmount,
            references: references
        };

        <%-- N-Genius takes priority when enabled --%>
        <%if (isNGeniusEnabled) {%>
        if (typeof NGeniusPaymentUtil !== 'undefined') {
            NGeniusPaymentUtil.processPayment(paymentData);
        } else {
            alert('Payment gateway is not available. Please refresh the page.');
        }
        <%} else {%>
        if (typeof StripePaymentUtil !== 'undefined') {
            StripePaymentUtil.processPayment(paymentData);
        } else {
            alert('Payment gateway is not available. Please refresh the page.');
        }
        <%}%>
    }
    <%}%>

</script>


