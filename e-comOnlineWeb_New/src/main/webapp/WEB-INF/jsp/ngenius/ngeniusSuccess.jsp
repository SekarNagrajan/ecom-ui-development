<%@page contentType="text/html" pageEncoding="UTF-8"
    import="java.util.List,net.ecom.vo.OnlinePaymentVO,net.ecom.vo.OnlinePaymentReferenceVO,
            net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat"%>
<%
    OnlinePaymentVO payment = (OnlinePaymentVO) request.getAttribute("payment");
    List<OnlinePaymentReferenceVO> refs = (List<OnlinePaymentReferenceVO>) request.getAttribute("paymentRefs");
    boolean hasPayment = (payment != null);

    // Set by NGeniusSuccessAction when N-Genius is still processing the payment
    // (e.g. 3DS, bank processing). Webhook will arrive shortly.
    boolean ngeniusProcessing = Boolean.TRUE.equals(request.getAttribute("ngeniusProcessing"));

    DecimalFormat df = new DecimalFormat("#,##0.00");

    String status = hasPayment ? Validation.checkNull(payment.getStatus()) : "";
    boolean isCompleted  = "COMPLETED".equalsIgnoreCase(status);
    boolean isAuth       = "AUTHORISED".equalsIgnoreCase(status);
    boolean isFailed     = "FAILED".equalsIgnoreCase(status);
    boolean isCancelled  = "CANCELLED".equalsIgnoreCase(status);
    // isPending = still unresolved in DB (SESSION_CREATED / PENDING)
    boolean isPending    = !isCompleted && !isAuth && !isFailed && !isCancelled;
%>
<style>
    .ng-detail-card  { max-width:600px; margin:0 auto; }
    .ng-detail-row   { display:flex; padding:10px 0; border-bottom:1px solid #f0f0f0; }
    .ng-detail-row:last-child { border-bottom:none; }
    .ng-detail-label { width:40%; color:#888; font-size:13px; font-weight:500; }
    .ng-detail-value { width:60%; color:#333; font-size:13px; font-weight:600; word-break:break-all; }
    .ng-section-title { font-size:11px; color:#999; text-transform:uppercase; letter-spacing:1px; font-weight:700;
                        margin:20px 0 8px; padding-bottom:6px; border-bottom:2px solid #f0f0f0; }
    .ng-ref-tag { padding:3px 10px; border-radius:4px; font-size:11px; font-weight:600;
                  display:inline-block; margin:2px 3px; }
    .ng-ref-bkg { background:#e3f2fd; color:#1565c0; }
    .ng-ref-bl  { background:#e8f5e9; color:#2e7d32; }
    .ng-ref-inv { background:#fff8e1; color:#f57f17; }
</style>

<div class="card-header page_header fixed-top">
    <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Payment Status
</div>

<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body" style="padding:40px 30px;">

            <div style="text-align:center; margin-bottom:25px;">
                <%if (isCompleted) {%>
                <i class="fa fa-check-circle" style="font-size:50px; color:#28a745;"></i>
                <h4 style="color:#28a745; margin:10px 0 5px;">Payment Successful</h4>
                <p style="color:#888; font-size:13px; margin:0;">Your payment has been processed successfully.</p>
                <%} else if (isAuth) {%>
                <i class="fa fa-lock" style="font-size:50px; color:#009ace;"></i>
                <h4 style="color:#009ace; margin:10px 0 5px;">Payment Authorised</h4>
                <p style="color:#888; font-size:13px; margin:0;">Your payment has been authorised and is being captured.</p>
                <%} else if (isFailed) {%>
                <i class="fa fa-times-circle" style="font-size:50px; color:#dc3545;"></i>
                <h4 style="color:#dc3545; margin:10px 0 5px;">Payment Failed</h4>
                <p style="color:#888; font-size:13px; margin:0;">Your payment could not be processed. Please try again.</p>
                <%} else if (isCancelled) {%>
                <i class="fa fa-ban" style="font-size:50px; color:#6c757d;"></i>
                <h4 style="color:#6c757d; margin:10px 0 5px;">Payment Not Completed</h4>
                <p style="color:#888; font-size:13px; margin:0;">The payment session was not completed. No amount has been charged.</p>
                <%} else if (ngeniusProcessing) {%>
                <i class="fa fa-spinner fa-spin" style="font-size:50px; color:#f0ad4e;"></i>
                <h4 style="color:#f0ad4e; margin:10px 0 5px;">Payment Processing</h4>
                <p style="color:#888; font-size:13px; margin:0;">Your payment is being verified by the bank. Please wait&hellip;</p>
                <p style="color:#aaa; font-size:12px; margin:8px 0 0;">This page will refresh automatically in <span id="ngCountdown">10</span> seconds.</p>
                <%} else if (isPending) {%>
                <i class="fa fa-clock-o" style="font-size:50px; color:#f0ad4e;"></i>
                <h4 style="color:#f0ad4e; margin:10px 0 5px;">Payment Pending</h4>
                <p style="color:#888; font-size:13px; margin:0;">Your payment status could not be confirmed. Please check Payment History or contact support.</p>
                <%} else {%>
                <i class="fa fa-info-circle" style="font-size:50px; color:#009ace;"></i>
                <h4 style="color:#009ace; margin:10px 0 5px;">Payment Submitted</h4>
                <p style="color:#888; font-size:13px; margin:0;">Your payment has been submitted. You will receive a confirmation shortly.</p>
                <%}%>
            </div>

            <%if (hasPayment) {%>
            <div class="ng-detail-card">
                <div class="ng-section-title">Transaction Details</div>

                <div class="ng-detail-row">
                    <div class="ng-detail-label">Status</div>
                    <div class="ng-detail-value">
                        <%
                            String displayStatus;
                            String badgeStyle;
                            if (isCompleted)  { displayStatus = "Completed";       badgeStyle = "background:#d4edda; color:#155724;"; }
                            else if (isAuth)  { displayStatus = "Authorised";      badgeStyle = "background:#cce5ff; color:#004085;"; }
                            else if (isFailed){ displayStatus = "Failed";          badgeStyle = "background:#f8d7da; color:#721c24;"; }
                            else if (isCancelled){ displayStatus = "Not Completed"; badgeStyle = "background:#e2e3e5; color:#383d41;"; }
                            else              { displayStatus = "Pending";         badgeStyle = "background:#fff3cd; color:#856404;"; }
                        %>
                        <span style="padding:3px 12px; border-radius:12px; font-size:11px; font-weight:700; <%=badgeStyle%>"><%=displayStatus%></span>
                    </div>
                </div>

                <%
                    // stripeChargeId column holds N-Genius authorization code
                    String authCode = Validation.checkNull(payment.getStripeChargeId());
                    // stripeTransactionId column holds N-Genius RRN
                    String rrn = Validation.checkNull(payment.getStripeTransactionId());
                %>
                <%if (!authCode.isEmpty()) {%>
                <div class="ng-detail-row">
                    <div class="ng-detail-label">Auth Code</div>
                    <div class="ng-detail-value" style="font-family:monospace;"><%=authCode%></div>
                </div>
                <%}%>
                <%if (!rrn.isEmpty()) {%>
                <div class="ng-detail-row">
                    <div class="ng-detail-label">RRN</div>
                    <div class="ng-detail-value" style="font-family:monospace; font-size:12px;"><%=rrn%></div>
                </div>
                <%}%>

                <div class="ng-detail-row">
                    <div class="ng-detail-label">Amount</div>
                    <div class="ng-detail-value" style="font-size:18px; color:#2c3e50;">
                        <%=Validation.checkNull(payment.getCurrency())%>&nbsp;<%=payment.getAmountUsd() != null ? df.format(payment.getAmountUsd()) : "0.00"%>
                    </div>
                </div>

                <div class="ng-detail-row">
                    <div class="ng-detail-label">Payment Date</div>
                    <div class="ng-detail-value"><%=payment.getCreatedAt() != null ? DateConversion_New.convertDefaultFormatwithTime(DateConversion_New.convertDateToString(payment.getCreatedAt())) : "-"%></div>
                </div>

                <div class="ng-detail-row">
                    <div class="ng-detail-label">Payment User</div>
                    <div class="ng-detail-value"><%=Validation.checkNull(payment.getLoginName())%></div>
                </div>

                <%if (refs != null && !refs.isEmpty()) {%>
                <div class="ng-section-title">References</div>
                <div class="ng-detail-row">
                    <div class="ng-detail-label">Reference No(s)</div>
                    <div class="ng-detail-value">
                        <%for (int i = 0; i < refs.size(); i++) {
                            OnlinePaymentReferenceVO ref = refs.get(i);
                            String rt = Validation.checkNull(ref.getRefType()).toUpperCase();
                            String rc = "ng-ref-inv";
                            if ("BKG".equals(rt) || "B".equals(rt)) rc = "ng-ref-bkg";
                            else if ("BL".equals(rt) || "L".equals(rt)) rc = "ng-ref-bl";
                        %><span class="ng-ref-tag <%=rc%>"><%=Validation.checkNull(ref.getRefNo())%></span><%}%>
                    </div>
                </div>
                <%}%>
            </div>
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

<%-- Auto-refresh when N-Genius is still processing (3DS / bank processing).
     Reloads this same URL so NGeniusSuccessAction re-polls the API.
     Stops after 3 retries to avoid infinite loop if webhook is delayed. --%>
<%if (ngeniusProcessing) {%>
<script type="text/javascript">
(function () {
    var MAX_RETRIES  = 3;
    var REFRESH_SEC  = 10;
    var RETRY_PARAM  = "ngr";

    var url    = new URL(window.location.href);
    var retries = parseInt(url.searchParams.get(RETRY_PARAM) || "0", 10);

    if (retries < MAX_RETRIES) {
        var countdown = REFRESH_SEC;
        var el = document.getElementById("ngCountdown");

        var timer = setInterval(function () {
            countdown--;
            if (el) el.textContent = countdown;
            if (countdown <= 0) {
                clearInterval(timer);
                url.searchParams.set(RETRY_PARAM, retries + 1);
                window.location.href = url.toString();
            }
        }, 1000);
    } else {
        // Max retries reached — tell the user to check Payment History
        var msgEl = document.querySelector("p[style*='refresh automatically']");
        if (msgEl) {
            msgEl.textContent = "Status could not be confirmed automatically. Please check Payment History or contact support.";
        }
    }
})();
</script>
<%}%>
