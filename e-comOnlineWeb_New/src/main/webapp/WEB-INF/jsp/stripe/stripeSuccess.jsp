<%@page contentType="text/html" pageEncoding="UTF-8"
    import="java.util.List,net.ecom.vo.OnlinePaymentVO,net.ecom.vo.OnlinePaymentReferenceVO,
            net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat"%>
<%
    String errorMsg = (String) request.getAttribute("errorMsg");
    OnlinePaymentVO payment = (OnlinePaymentVO) request.getAttribute("payment");
    List<OnlinePaymentReferenceVO> refs = (List<OnlinePaymentReferenceVO>) request.getAttribute("paymentRefs");
    boolean hasError = (errorMsg != null && !errorMsg.isEmpty());
    boolean hasPayment = (payment != null);

    DecimalFormat df = new DecimalFormat("#,##0.00");

    String status = hasPayment ? Validation.checkNull(payment.getStatus()) : "";
    boolean isCompleted = "COMPLETED".equalsIgnoreCase(status);
    boolean isPending = "PENDING".equalsIgnoreCase(status) || "CREATED".equalsIgnoreCase(status);
%>
<style>
    .sp-detail-card { max-width: 600px; margin: 0 auto; }
    .sp-detail-row { display: flex; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
    .sp-detail-row:last-child { border-bottom: none; }
    .sp-detail-label { width: 40%; color: #888; font-size: 13px; font-weight: 500; }
    .sp-detail-value { width: 60%; color: #333; font-size: 13px; font-weight: 600; word-break: break-all; }
    .sp-section-title { font-size: 11px; color: #999; text-transform: uppercase; letter-spacing: 1px; font-weight: 700; margin: 20px 0 8px; padding-bottom: 6px; border-bottom: 2px solid #f0f0f0; }
    .sp-ref-tag { padding: 3px 10px; border-radius: 4px; font-size: 11px; font-weight: 600; display: inline-block; margin: 2px 3px; }
    .sp-ref-bkg { background: #e3f2fd; color: #1565c0; }
    .sp-ref-bl  { background: #e8f5e9; color: #2e7d32; }
    .sp-ref-inv { background: #fff8e1; color: #f57f17; }
</style>

<div class="card-header page_header fixed-top">
    <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Payment Status
</div>

<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body" style="padding:40px 30px;">

            <%if (hasError) {%>
            <div style="text-align:center;">
                <i class="fa fa-exclamation-circle" style="font-size:50px; color:#dc3545; margin-bottom:15px;"></i>
                <h4 style="color:#dc3545; margin-bottom:8px;">Payment Details Unavailable</h4>
                <p style="color:#666; font-size:14px;"><%=errorMsg.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;")%></p>
            </div>
            <%} else {%>

            <div style="text-align:center; margin-bottom:25px;">
                <%if (isCompleted) {%>
                <i class="fa fa-check-circle" style="font-size:50px; color:#28a745;"></i>
                <h4 style="color:#28a745; margin:10px 0 5px;">Payment Successful</h4>
                <%} else if (isPending) {%>
                <i class="fa fa-clock-o" style="font-size:50px; color:#f0ad4e;"></i>
                <h4 style="color:#f0ad4e; margin:10px 0 5px;">Payment Processing</h4>
                <%} else {%>
                <i class="fa fa-info-circle" style="font-size:50px; color:#009ace;"></i>
                <h4 style="color:#009ace; margin:10px 0 5px;">Payment Submitted</h4>
                <%}%>
                <p style="color:#888; font-size:13px; margin:0;">
                    <%if (isCompleted) {%>Your payment has been processed successfully.<%}%>
                    <%if (isPending) {%>Your payment is being verified. This may take a moment.<%}%>
                </p>
            </div>

            <%if (hasPayment) {%>
            <div class="sp-detail-card">

                <div class="sp-section-title">Transaction Details</div>

                <div class="sp-detail-row">
                    <div class="sp-detail-label">Status</div>
                    <div class="sp-detail-value">
                        <span style="padding:3px 12px; border-radius:12px; font-size:11px; font-weight:700;
                            <%if (isCompleted) {%>background:#d4edda; color:#155724;<%}%>
                            <%if (isPending) {%>background:#fff3cd; color:#856404;<%}%>
                            <%if (!isCompleted && !isPending) {%>background:#e2e3e5; color:#383d41;<%}%>
                        "><%=status%></span>
                    </div>
                </div>

                <%String txnId = Validation.checkNull(payment.getStripeTransactionId());%>
                <%if (!txnId.isEmpty()) {%>
                <div class="sp-detail-row">
                    <div class="sp-detail-label">Transaction ID</div>
                    <div class="sp-detail-value" style="font-family:monospace; font-size:12px;"><%=txnId%></div>
                </div>
                <%}%>

                <div class="sp-detail-row">
                    <div class="sp-detail-label">Amount</div>
                    <div class="sp-detail-value" style="font-size:18px; color:#2c3e50;">
                        <%=Validation.checkNull(payment.getCurrency())%>&nbsp;<%=payment.getAmountUsd() != null ? df.format(payment.getAmountUsd()) : "0.00"%>
                    </div>
                </div>

                <div class="sp-detail-row">
                    <div class="sp-detail-label">Payment Date</div>
                    <div class="sp-detail-value"><%=payment.getCreatedAt() != null ? DateConversion_New.convertDefaultFormatwithTime(DateConversion_New.convertDateToString(payment.getCreatedAt())) : "-"%></div>
                </div>

                <div class="sp-detail-row">
                    <div class="sp-detail-label">Payment User</div>
                    <div class="sp-detail-value"><%=Validation.checkNull(payment.getLoginName())%></div>
                </div>

                <%if (refs != null && !refs.isEmpty()) {%>
                <div class="sp-section-title">References</div>
                <div class="sp-detail-row">
                    <div class="sp-detail-label">Reference No(s)</div>
                    <div class="sp-detail-value">
                        <%for (int i = 0; i < refs.size(); i++) {
                            OnlinePaymentReferenceVO ref = refs.get(i);
                            String rt = Validation.checkNull(ref.getRefType()).toUpperCase();
                            String rc = "sp-ref-inv";
                            if ("BKG".equals(rt) || "B".equals(rt)) rc = "sp-ref-bkg";
                            else if ("BL".equals(rt) || "L".equals(rt)) rc = "sp-ref-bl";
                        %><span class="sp-ref-tag <%=rc%>"><%=Validation.checkNull(ref.getRefNo())%></span><%}%>
                    </div>
                </div>
                <%}%>

            </div>
            <%}%>

            <%}%>

            <hr style="margin:30px auto 20px; max-width:400px; border-color:#e0e0e0;">

            <div style="text-align:center; display:flex; justify-content:center; gap:12px; flex-wrap:wrap;">
                <a href="paymentHistory.do" class="btn btn-info" style="padding:10px 30px;">
                    <i class="fa fa-list"></i> Payment History
                </a>
                <a href="Welcome.do" class="btn btn-outline-secondary" style="padding:10px 30px;">
                    <i class="fa fa-home"></i> Back to Home
                </a>
            </div>

        </div>
    </div>
</div>
