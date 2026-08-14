<%@page contentType="text/html" pageEncoding="UTF-8"
    import="java.util.List,java.util.Map,java.util.ArrayList,
            net.ecom.vo.OnlinePaymentVO,net.ecom.vo.OnlinePaymentReferenceVO,
            net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat,net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String strDateFormat = (String) getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    List<OnlinePaymentVO> paymentList = (List<OnlinePaymentVO>) request.getAttribute("PAYMENT_HISTORY_LIST");
    Map<String, List<OnlinePaymentReferenceVO>> refMap =
            (Map<String, List<OnlinePaymentReferenceVO>>) request.getAttribute("PAYMENT_REF_MAP");
    if (refMap == null) refMap = new java.util.HashMap<>();
    DecimalFormat df = new DecimalFormat("#,##0.00");
    String filterFromDate = (String) request.getAttribute("FILTER_FROM_DATE");
    String filterToDate = (String) request.getAttribute("FILTER_TO_DATE");
    if (filterFromDate == null) filterFromDate = "";
    if (filterToDate == null) filterToDate = "";
    int totalRecords = (paymentList != null) ? paymentList.size() : 0;
    boolean isStripeEnabled  = "Y".equalsIgnoreCase((String) getServletContext().getAttribute(GeneralConf.Enable_Stripe_Payment));
    boolean isNGeniusEnabled = "Y".equalsIgnoreCase((String) getServletContext().getAttribute(GeneralConf.Enable_Ngenius_Payment));
    String paymentCurrency = isStripeEnabled ? (String) getServletContext().getAttribute(GeneralConf.Stripe_Payment_Currency)
            : (isNGeniusEnabled ? (String) getServletContext().getAttribute(GeneralConf.Ngenius_Payment_Currency) : null);
    paymentCurrency = (paymentCurrency != null && paymentCurrency.trim().length() > 0) ? paymentCurrency.trim().toUpperCase() : "";
%>
<style>
    .ph-status { padding: 5px 14px; border-radius: 20px; font-size: 11px; font-weight: 700; letter-spacing: 0.5px; display: inline-block; text-transform: uppercase; }
    .ph-completed { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    .ph-pending   { background: #fff3cd; color: #856404; border: 1px solid #ffeeba; }
    .ph-failed    { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    .ph-cancelled { background: #e2e3e5; color: #383d41; border: 1px solid #d6d8db; }

    .ph-type { padding: 4px 12px; border-radius: 4px; font-size: 12px; font-weight: 600; display: inline-block; }
    .ph-type-bkg { background: #e8f4fd; color: #1565c0; border-left: 3px solid #1565c0; }
    .ph-type-bl  { background: #e8f5e9; color: #2e7d32; border-left: 3px solid #2e7d32; }
    .ph-type-inv { background: #fff8e1; color: #f57f17; border-left: 3px solid #f57f17; }

    .ph-ref-tag { padding: 2px 8px; border-radius: 3px; font-size: 11px; font-weight: 600; display: inline-block; margin: 1px 2px; }
    .ph-ref-bkg { background: #e3f2fd; color: #1565c0; }
    .ph-ref-bl  { background: #e8f5e9; color: #2e7d32; }
    .ph-ref-inv { background: #fff8e1; color: #f57f17; }

    .ph-amount { font-weight: 700; color: #2c3e50; font-size: 13px; }
    .ph-user { color: #555; font-size: 12px; }
    .ph-txn { font-family: monospace; font-size: 11px; color: #666; }
    .ph-date { font-size: 12px; color: #555; white-space: nowrap; }

    .ph-summary-row { display: flex; gap: 15px; margin-bottom: 10px; flex-wrap: wrap; }
    .ph-summary-card { background: #fff; border: 1px solid #e0e0e0; border-radius: 8px; padding: 12px 20px; flex: 1; min-width: 140px; text-align: center; box-shadow: 0 1px 3px rgba(0,0,0,0.06); }
    .ph-summary-card .ph-count { font-size: 22px; font-weight: 700; }
    .ph-summary-card .ph-label { font-size: 11px; color: #888; text-transform: uppercase; letter-spacing: 0.5px; margin-top: 2px; }
    .ph-count-total { color: #1565c0; }
    .ph-count-completed { color: #155724; }
    .ph-count-pending { color: #856404; }
    .ph-count-failed { color: #721c24; }

    #paymentHistoryTable thead th { background: #f5f7fa; color: #333; font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.3px; border-bottom: 2px solid #dee2e6; }
    #paymentHistoryTable tbody td { vertical-align: middle; }
</style>

<div class="card-header page_header fixed-top">
    <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Payment History
</div>

<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body">
            <div class="container">
                <form action="paymentHistory.do" method="get" id="payHistoryFilterForm">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 col-sm-4">
                                <input type="text" name="fromDate" id="payFromDate" class="form-control inside_input remove_autocomplete"
                                       value="<%=filterFromDate%>" readonly="readonly" placeholder="From Date" />
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">
                            </div>
                            <div class="col-xs-12 col-sm-4">
                                <input type="text" name="toDate" id="payToDate" class="form-control inside_input remove_autocomplete"
                                       value="<%=filterToDate%>" readonly="readonly" placeholder="To Date" />
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">
                            </div>
                            <div class="col-xs-12 col-sm-2">
                                <button type="submit" class="btn btn-info" style="padding:12px 45px"><i class="fa fa-search"></i> Search</button>
                            </div>
                            <div class="col-xs-12 col-sm-2">
                                <a href="paymentHistory.do" class="btn btn-danger" style="padding:12px 50px"><i class="fa fa-refresh"></i> Reset</a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <%
        int countCompleted = 0, countPending = 0, countFailed = 0, countCancelled = 0;
        if (paymentList != null) {
            for (OnlinePaymentVO p : paymentList) {
                String s = Validation.checkNull(p.getStatus());
                if ("COMPLETED".equalsIgnoreCase(s) || "AUTHORISED".equalsIgnoreCase(s)) countCompleted++;
                else if ("PENDING".equalsIgnoreCase(s) || "CREATED".equalsIgnoreCase(s)
                        || "SESSION_CREATED".equalsIgnoreCase(s)) countPending++;
                else if ("FAILED".equalsIgnoreCase(s)) countFailed++;
                else countCancelled++;
            }
        }
    %>
    <div class="ph-summary-row" style="margin-top:10px;">
        <div class="ph-summary-card">
            <div class="ph-count ph-count-total"><%=totalRecords%></div>
            <div class="ph-label">Total</div>
        </div>
        <div class="ph-summary-card">
            <div class="ph-count ph-count-completed"><%=countCompleted%></div>
            <div class="ph-label">Completed</div>
        </div>
        <div class="ph-summary-card">
            <div class="ph-count ph-count-pending"><%=countPending%></div>
            <div class="ph-label">Pending</div>
        </div>
        <div class="ph-summary-card">
            <div class="ph-count ph-count-failed"><%=countFailed + countCancelled%></div>
            <div class="ph-label">Failed / Cancelled</div>
        </div>
    </div>

    <div class="table-responsive card card-body card-shadow" style="margin-top:5px;">
        <table class="table table-striped table-bordered" id="paymentHistoryTable">
            <thead>
                <tr>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Payment User</th>
                    <th>Transaction ID</th>
                    <th>Payment Date</th>
                    <th>References</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (paymentList != null && paymentList.size() > 0) {
                        for (int i = 0; i < paymentList.size(); i++) {
                            OnlinePaymentVO vo = paymentList.get(i);
                            String status = Validation.checkNull(vo.getStatus());
                            String statusClass = "ph-cancelled";
                            String statusLabel;
                            if ("COMPLETED".equalsIgnoreCase(status)) {
                                statusClass = "ph-completed"; statusLabel = "Completed";
                            } else if ("AUTHORISED".equalsIgnoreCase(status)) {
                                statusClass = "ph-completed"; statusLabel = "Authorised";
                            } else if ("PENDING".equalsIgnoreCase(status) || "CREATED".equalsIgnoreCase(status)
                                    || "SESSION_CREATED".equalsIgnoreCase(status)) {
                                statusClass = "ph-pending"; statusLabel = "Pending";
                            } else if ("FAILED".equalsIgnoreCase(status)) {
                                statusClass = "ph-failed"; statusLabel = "Failed";
                            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                                statusClass = "ph-cancelled"; statusLabel = "Cancelled";
                            } else {
                                statusLabel = status;
                            }

                            String amount = vo.getAmountUsd() != null ? df.format(vo.getAmountUsd()) : "0.00";
                            String rowCurrency = Validation.checkNull(vo.getCurrency());
                            if (rowCurrency.isEmpty()) { rowCurrency = paymentCurrency; }
                            rowCurrency = rowCurrency.toUpperCase();
                            String txnId = Validation.checkNull(vo.getStripeTransactionId());
                            String loginName = Validation.checkNull(vo.getLoginName());
                            String requestId = vo.getRequestId() != null ? vo.getRequestId().toString() : "";
                            String paymentDate = vo.getCreatedAt() != null
                                    ? DateConversion_New.convertDefaultFormatwithTime(DateConversion_New.convertDateToString(vo.getCreatedAt()))
                                    : "";

                            List<OnlinePaymentReferenceVO> refs = refMap.get(requestId);

                            String payType = "";
                            if (refs != null && !refs.isEmpty()) {
                                payType = Validation.checkNull(refs.get(0).getRefType()).toUpperCase();
                            }
                            if (payType.isEmpty()) {
                                payType = Validation.checkNull(vo.getPaymentType()).toUpperCase();
                            }
                            String typeClass = "ph-type-inv";
                            String typeLabel = payType;
                            if ("BKG".equals(payType) || "B".equals(payType)) {
                                typeClass = "ph-type-bkg"; typeLabel = "Booking";
                            } else if ("BL".equals(payType) || "L".equals(payType)) {
                                typeClass = "ph-type-bl"; typeLabel = "BL";
                            } else if ("INV".equals(payType) || "I".equals(payType)) {
                                typeClass = "ph-type-inv"; typeLabel = "Invoice";
                            }
                            if (refs == null) refs = new ArrayList<>();
                            int refCount = refs.size();
                %>
                <tr>
                    <td><span class="ph-type <%=typeClass%>"><%=typeLabel%></span></td>
                    <td style="text-align:right"><span class="ph-amount"><%=rowCurrency.isEmpty() ? amount : rowCurrency + "&nbsp;" + amount%></span></td>
                    <td><%
                        String errMsg = Validation.checkNull(vo.getErrorMessage());
                        boolean hasReason = !errMsg.isEmpty() && ("EXPIRED".equalsIgnoreCase(status)
                                || "CANCELLED".equalsIgnoreCase(status) || "FAILED".equalsIgnoreCase(status));
                        if (hasReason) {
                    %><span class="ph-status <%=statusClass%>" data-toggle="tooltip" data-placement="top"
                           title="<%=errMsg.replace("\"","&quot;").replace("<","&lt;").replace(">","&gt;")%>"
                           style="cursor:help;"><%=statusLabel%> </span><%
                        } else {
                    %><span class="ph-status <%=statusClass%>"><%=statusLabel%></span><%
                        }
                    %></td>
                    <td><span class="ph-user"><%=loginName%></span></td>
                    <td><span class="ph-txn" data-toggle="tooltip" title="<%=txnId%>"><%=txnId.length() > 16 ? txnId.substring(0, 16) + "..." : txnId%></span></td>
                    <td><span class="ph-date"><%=paymentDate%></span></td>
                    <td<%
                        if (refCount > 0) {
                            StringBuilder allRefsSearch = new StringBuilder();
                            for (int r = 0; r < refs.size(); r++) {
                                if (r > 0) allRefsSearch.append(" ");
                                allRefsSearch.append(Validation.checkNull(refs.get(r).getRefNo()));
                            }
                    %> data-search="<%=allRefsSearch.toString().replace("\"","&quot;")%>"<%
                        }
                    %>><%
                        if (refCount > 0) {
                            OnlinePaymentReferenceVO firstRef = refs.get(0);
                            String frt = Validation.checkNull(firstRef.getRefType()).toUpperCase();
                            String frc = "ph-ref-inv";
                            if ("BKG".equals(frt) || "B".equals(frt)) frc = "ph-ref-bkg";
                            else if ("BL".equals(frt) || "L".equals(frt)) frc = "ph-ref-bl";
                    %><span class="ph-ref-tag <%=frc%>"><%=Validation.checkNull(firstRef.getRefNo())%></span><%
                            if (refCount > 1) {
                                StringBuilder allRefsTip = new StringBuilder();
                                for (int r = 1; r < refs.size(); r++) {
                                    if (r > 1) allRefsTip.append(", ");
                                    allRefsTip.append(Validation.checkNull(refs.get(r).getRefNo()));
                                }
                    %><span class="ph-ref-tag" style="background:#f0f0f0; color:#555; cursor:help;"
                            data-toggle="tooltip" data-placement="left"
                            title="<%=allRefsTip.toString().replace("\"","&quot;")%>">+<%=refCount - 1%> more</span><%
                            }
                        } else {
                    %><span class="text-muted">-</span><%
                        }
                    %></td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#payFromDate, #payToDate').datepicker({
            autoclose: true,
            clearBtn: true,
            todayHighlight: true,
            format: '<%=strDateFormat%>'
        });

        $('#paymentHistoryTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": [[5, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": 6 }
            ]
        });
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>
