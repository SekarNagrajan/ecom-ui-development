<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="net.ecom.common.GeneralConf"%>
<%@page import="net.ecom.common.utils.MaskPortUtil"%>
<%@page contentType="text/html" language="java"  import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    String cacheVersion = (String) getServletContext().getAttribute("CACHEVERSION");
    cacheVersion = cacheVersion != null && cacheVersion.length() > 0 ? "?" + cacheVersion : "";
%>
<%
    String strDateFormat = (String) getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode = (String) session.getAttribute("clientcode");
    System.out.println("[ECOM-WAR] ebookView ebkgviewccvalue" + clientcode);
    String template = (String) session.getAttribute("SAVETEMPLATE");
    String amendmentError = (String) request.getAttribute("SAVEAMENDMENTERROR");
    amendmentError = amendmentError != null && amendmentError.length() > 0 ? amendmentError : "";
    String value = "";
    if (template != null && template.length() > 0 && template.equalsIgnoreCase("save")) {
        value = "save";
    } else if (template != null && template.length() > 0 && template.equalsIgnoreCase("update")) {
        value = "update";
    }
    session.setAttribute("SAVETEMPLATE", "");
    String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
    long jsdate = new java.util.Date().getTime();
    String hideLRPBookingCancel = (String) getServletContext().getAttribute(GeneralConf.HideLRPBookingCancel);
    String FeusExists = (String) getServletContext().getAttribute("FEUexists");
    System.out.println("[ECOM-WAR] ebookView FeusExists jsp" + FeusExists);
    String tempupdatemsg = (String) request.getAttribute("updatetemplatemsg");
    String allowIncompRegPlugin = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN));
    String incompleteRegSession = Validation.checkNull((String) session.getAttribute("SESSION_INCOMPLETED_REG"));
    boolean isKycPending = "Y".equalsIgnoreCase(allowIncompRegPlugin) && "Y".equalsIgnoreCase(incompleteRegSession);
    String saveTemplateMsg = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Save_Template_Message));
    boolean showTemplateMsgPopup = isKycPending && saveTemplateMsg.length() > 0 && tempupdatemsg != null && tempupdatemsg.length() > 0;
    String Enable_ConfirmedBooking_Edit = (String) getServletContext().getAttribute(GeneralConf.Enable_ConfirmedBooking_Edit);
    String Enable_Cancel_Option_Confirmed_BKG = (String) getServletContext().getAttribute(GeneralConf.Enable_Cancel_Option_Confirmed_BKG);
    String Enable_TermsCondition_Accept = (String) getServletContext().getAttribute(GeneralConf.Enable_TermsCondition_Accept);
    List bkgCancelReasonList = (List) getServletContext().getAttribute(GeneralConf.BKG_Cancel_Reason_List);
    String bkgCancelTC = (String) getServletContext().getAttribute("BKG_Cancel_TermsCondition");
    String bkgConfEditTC = (String) getServletContext().getAttribute("BKG_ConfEdit_TermsCondition");
    String carriercode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String hideAgencyRefno = (String) getServletContext().getAttribute(GeneralConf.HIDE_AGENCYREF_BOOKING_SUMMARY);
    String showdisclaimer = (String) getServletContext().getAttribute(GeneralConf.SHOW_DISCLAIMER_IN_BOOKINGSUMMARY);
    HashSet<String> disableDraftPrintAgency = (HashSet<String>) getServletContext().getAttribute("disableDraftPrintAgency");
    String enableStripePayment  = (String) getServletContext().getAttribute(GeneralConf.Enable_Stripe_Payment);
    String enableNGeniusPayment = (String) getServletContext().getAttribute(GeneralConf.Enable_Ngenius_Payment);
    boolean isStripeEnabled  = "Y".equalsIgnoreCase(enableStripePayment);
    boolean isNGeniusEnabled = "Y".equalsIgnoreCase(enableNGeniusPayment);
    boolean isPaymentEnabled = isStripeEnabled || isNGeniusEnabled;
    String rawPaymentCurrency = isStripeEnabled ? (String) getServletContext().getAttribute(GeneralConf.Stripe_Payment_Currency)
            : (isNGeniusEnabled ? (String) getServletContext().getAttribute(GeneralConf.Ngenius_Payment_Currency) : null);
    boolean paymentCurrencyConfigured = rawPaymentCurrency != null && rawPaymentCurrency.trim().length() > 0;
    String paymentCurrency = paymentCurrencyConfigured ? rawPaymentCurrency.trim().toUpperCase() : "";
    String paymentCurrencyMsg = "Payment currency is not configured. Please contact the  administrator.";
    String enableScheduleDisclaimer = (String) getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_DISCLAIMER);
    String scheduleDisclaimerMessage = (String) getServletContext().getAttribute("ScheduleDisclaimerMessage");
%>
<%    String bookno = "";
    String originport = "";
    String deliveryport = "";
    String trust_action_no = "";
    String crdate = "";
    String LRPStatus = "", statusDesc = "", shotRefNo = "";
    String LRPStatus_new = "";
    String cruser = "";
    String Subdate = "";
    String ecomLrpType = "";
    String agencyrefno = "";
    String agencyid = "";
    String haulageTracking = "";
    ArrayList book = (ArrayList) session.getAttribute("BookingView");
    EbookingViewAll eBooking = null;
    System.out.println("[ECOM-WAR] ebookView book=> " + book.size());
%>
<style>
    .booking_disclaimer1{
        float: left !important;
        font-weight: bold !important;
        margin-left: 30px;
        text-align: right !important;
        color: #337ab7;
    }
    .booking_disclaimer2{
        float: left !important;
        font-weight: bold !important;
        margin-left: 30px;
        text-align: right !important;
        color: #5cb85c;
    }
</style>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /><bean:message key="ebooking.head.ebk" bundle="<%=clientcode%>"/>

    <div class="btn_grp_align">

        <button onclick="tempbookingshow()" id="mgtemp" class="btn btn_mang">  <bean:message key="Booking.managetemp" bundle="<%=clientcode%>"/> </button>

        <a onclick="newBooking();" href="javascript:void(0)" title="New Booking" > <button class="btn btn_new_book">  <i class="fa fa-plus"></i> <bean:message key="Booking.newbooking" bundle="<%=clientcode%>"/>  </button> </a>

    </div>

</div>
<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body">
            <div class="container">
                <html:form method="post" action="NewBooking.do" styleId="EBookingViewAllForm"  >
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 col-sm-4">
                                <html:text property="frdateport" styleId="frdateport" styleClass="form-control inside_input remove_autocomplete required"/>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">     
                            </div>
                            <div class="col-xs-12 col-sm-4">
                                <html:text property="todateport" styleId="todateport" styleClass="form-control inside_input remove_autocomplete required" />
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">  
                            </div>

                            <div class="col-xs-12 col-sm-2">
                                <button class="btn btn-info" style="padding:12px 65px" ><bean:message key="ecom.Search" bundle="<%=clientcode%>"/> </button>
                            </div>
                            <div class="col-xs-12 col-sm-2">
                                <button type="reset" class="btn btn-danger" style="padding:12px 65px" onClick="$('#EBookingViewAllForm label.error').remove();
                                        $('#EBookingViewAllForm input.error').css('border-color', '#ff6966');
                                        setdefault();"><span class="glyphicon glyphicon-refresh"></span> <bean:message key="ecom.reset" bundle="<%=clientcode%>" />  </button>

                            </div>

                        </div>   
                    </div>
                    <input type="hidden" id="bookno" name="bookno">
                    <input type="hidden" id="flag" name="flag">
                    <input type="hidden" id="action" name="action">
                    <input type="hidden" id="book_type" name="book_type">
                    <input type="hidden" id="ecomlrptype" name="ecomlrptype">
                    <input type="hidden" id="lrpbookno" name="lrpbookno">
                    <input type="hidden" id="ecomBookNo" name="ecomBookNo">
                    <input type="hidden" id="manageTemplate" name="manageTemplate" value="N">
                    <input type="hidden" id="appversion" name="appversion">
                </html:form>
            </div>
            <%if ("Y".equalsIgnoreCase(showdisclaimer)) {%>              
            <div class="row" ><span class=" booking_disclaimer1">Awaiting acceptance – Booking is awaiting the acceptance</span></div>
            <div class="row" ><span class=" booking_disclaimer2">Confirmed status – Booking is accepted</span></div>
            <%}%>
        </div>
    </div>
    <%if (isPaymentEnabled) {
        int bkgJsNo = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    %>
    <%if (isStripeEnabled) {%>
    <script type="text/javascript" src="js/stripe-payment-util.js?<%=bkgJsNo%>"></script>
    <script type="text/javascript">
        var csrfToken = '<%= session.getAttribute("STRIPE_CSRF_TOKEN") %>';
        if (typeof StripePaymentUtil !== 'undefined') { StripePaymentUtil.initializeCSRFToken(csrfToken); }
    </script>
    <%}%>
    <%if (isNGeniusEnabled) {%>
    <script type="text/javascript" src="js/ngenius-payment-util.js?<%=bkgJsNo%>"></script>
    <script type="text/javascript">
        var ngeniusCsrfToken = '<%= session.getAttribute("NGENIUS_CSRF_TOKEN") %>';
        if (typeof NGeniusPaymentUtil !== 'undefined') { NGeniusPaymentUtil.initializeCSRFToken(ngeniusCsrfToken); }
    </script>
    <%}%>
    <div id="bkgPaymentBar" class="ecom-pay-bar">
        <div class="ecom-pay-content">
            <div class="ecom-pay-info">
                <div class="ecom-pay-count"><span id="bkgSelectedCount">0</span> bookings selected</div>
                <div class="ecom-pay-total">Total: <span id="bkgTotalAmount"><%=paymentCurrencyConfigured ? paymentCurrency + " 0.00" : paymentCurrencyMsg%></span></div>
            </div>
            <div class="ecom-pay-actions">
                <input type="number" id="bkgEditableAmount" class="ecom-pay-amount" step="0.01" min="0" value="0.00" placeholder="Amount">
                <button type="button" class="ecom-pay-btn-clear" onclick="clearBkgSelection()">Clear</button>
                <button type="button" class="ecom-pay-btn-pay" onclick="proceedToBookingPayment()"><i class="fa fa-credit-card"></i> Pay Selected</button>
            </div>
        </div>
    </div>
    <%}%>
    <div id="bookingsearchviewdiv">
        <%if ("Y".equalsIgnoreCase(enableScheduleDisclaimer) && scheduleDisclaimerMessage != null && !scheduleDisclaimerMessage.isEmpty()) {%>
        <div class="row"><span class="lbl_clr_txt" style="margin-left:15px;"><%=scheduleDisclaimerMessage%></span></div>
        <%}%>
        <div class="table-responsive card card-body card-shadow">
            <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
            <table class="table table-striped table-bordered" id="datatablebybooking">
                <thead>
                    <tr>
                        <%if (isPaymentEnabled) {%>
                        <th style="width:30px;text-align:center"><input type="checkbox" class="ecom-pay-checkbox" id="bkgSelectAll" onclick="toggleAllBkgCheckboxes(this)"></th>
                        <%}%>
                        <th><bean:message key="EBookingView.action" bundle="<%=clientcode%>"  ></bean:message></th>
                        <th><bean:message key="EBookingView.trustbookkno" bundle="<%=clientcode%>" ></bean:message></th>
                        <th><bean:message key="EBookingView.bookno" bundle="<%=clientcode%>" ></bean:message></th>
                            <%if (!"Y".equalsIgnoreCase(hideAgencyRefno)) {%>
                        <th><bean:message key="ebooking.agency.reference" bundle="<%=clientcode%>" ></bean:message></th>
                            <%}%>
                        <th><bean:message key="EBookingView.LRPStatus" bundle="<%=clientcode%>" ></bean:message></th>
                        <th><bean:message key='ebooking.origin.display' bundle="<%=clientcode%>"></bean:message></th>
                        <th><bean:message key='ebooking.Delivery.display' bundle="<%=clientcode%>"></bean:message></th>
                        <th><bean:message key="EBookingView.crdate" bundle="<%=clientcode%>" ></bean:message></th>
                        <th><bean:message key="EBookingView.confirmeddate" bundle="<%=clientcode%>" ></bean:message></th>
                            <%  if ("GWF".equalsIgnoreCase(carriercode)) {%>
                        <th>POL ETD</th>                        
                        <th>User ID</th>
                            <%}%>
                        <th><bean:message key="EBookingView.dgstatus" bundle="<%=clientcode%>" ></bean:message></th>
                            <%  if ("YES".equalsIgnoreCase(FeusExists)) {%>
                        <th><bean:message key="ebooking.fuescount" bundle="<%=clientcode%>" ></bean:message></th>
                            <%} else {%>
                        <th><bean:message key="ebooking.tuescount" bundle="<%=clientcode%>" ></bean:message></th>

                        <%}%>
                        <th><bean:message key="EBookingView.submitdate" bundle="<%=clientcode%>" ></bean:message></th>
                        <th><bean:message key="EBookingView.crusername" bundle="<%=clientcode%>" ></bean:message></th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (book != null && book.size() > 0) {
                            int size = book.size();
                            Set bookset = (Set) session.getAttribute("bookingAccessList");
                            if (bookset == null) {
                                bookset = new HashSet();
                            }
                            for (int i = 0; i < size; i++) {
                                eBooking = (EbookingViewAll) book.get(i);
                                LRPStatus = eBooking.getLrpstatus();
                                trust_action_no = eBooking.getTrust_action();
                                trust_action_no = (trust_action_no != null && (!"".equalsIgnoreCase(trust_action_no))) ? trust_action_no.trim() : "N/A";
                                bookno = eBooking.getBookNo();
                                originport = MaskPortUtil.swapActualPortToMaskPortWithName(eBooking.getOriginPortText());
                                deliveryport = MaskPortUtil.swapActualPortToMaskPortWithName(eBooking.getFinalPortText());
                                cruser = eBooking.getCruser();
                                crdate = eBooking.getCrdate();
                                Subdate = eBooking.getSubmitdate();
                                ecomLrpType = eBooking.getLrpEcomStatus();
                                String lprBook_no = eBooking.getTrust_book_no();
                                agencyrefno = eBooking.getAgencyrefno();
                                agencyid =  eBooking.getAgencyId();
                                if (Subdate != null) {
                                    Subdate = eBooking.getSubmitdate();
                                } else {
                                    Subdate = "-";
                                }
                                LRPStatus = eBooking.getLrpstatus();
                                shotRefNo = eBooking.getShowExtBookNo();
                                statusDesc = eBooking.getStatusDesc();
                                haulageTracking = eBooking.getHaulageflag();
                                if ("C".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Confirmed";
                                    LRPStatus_new = "<span class='label label-success'>" + statusDesc + "</span>";
                                } else if ("D".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Draft";
                                    LRPStatus_new = "<span class='label label-primary'>" + statusDesc + "</span>";
                                    cruser = "-";
                                    Subdate = "-";
                                } else if ("V".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Declined";
                                    LRPStatus_new = "<span class='label label-danger'>" + statusDesc + "</span>";
                                } else if ("I".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Declined";
                                    LRPStatus_new = "<span class='label label-danger'>" + statusDesc + "</span>";
                                } else if ("P".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Accepted";
                                    if ("Y".equalsIgnoreCase(showdisclaimer)) {
                                        LRPStatus_new = "<span class='label label-warning' style='background:#337ab7'>" + statusDesc + "</span>";
                                    } else {
                                        LRPStatus_new = "<span class='label label-green'>" + statusDesc + "</span>";
                                    }
                                } else if ("S".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Sent To Agency";
                                    LRPStatus_new = "<span class='label label-info'>" + statusDesc + "</span>";
                                } else if ("L".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Cancelled";
                                    LRPStatus_new = "<span class='label label-danger'>" + statusDesc + "</span>";
                                } else if ("X".equalsIgnoreCase(LRPStatus)) {
                                    //LRPStatus = "Cancellation Submitted";
                                    LRPStatus_new = "<span class='label label-warning'>" + statusDesc + "</span>";
                                }
                                if ("Y".equalsIgnoreCase(shotRefNo) || (bookno != null && bookno.length() > 0)) {
                    %>
                    <tr >
                        <%if (isPaymentEnabled) {
                            String payEligible = eBooking.getPaymentEligible();
                            String payCompleted = eBooking.getPaymentCompleted();
                            String payAmt = eBooking.getPayAmountUsd();
                            String payCustType = eBooking.getPayCustomerType();
                            String payAgency = eBooking.getPayAgency();
                            boolean cbEnabled = "Y".equalsIgnoreCase(payEligible) && !"Y".equalsIgnoreCase(payCompleted);
                            String cbTooltip = "Y".equalsIgnoreCase(payCompleted) ? "Payment already completed" : (!"Y".equalsIgnoreCase(payEligible) ? "Not eligible for payment" : "");
                        %>
                        <td style="text-align:center">
                            <%if ("Y".equalsIgnoreCase(payCompleted)) {%>
                                <span data-toggle="tooltip" title="Payment already completed"><input type="checkbox" class="ecom-pay-checkbox" disabled></span>
                            <%} else if (!cbEnabled) {%>
                                <span data-toggle="tooltip" title="Not eligible for payment"><input type="checkbox" class="ecom-pay-checkbox" disabled></span>
                            <%} else {%>
                                <input type="checkbox" class="ecom-pay-checkbox bkg-row-cb"
                                       data-bookno="<%=lprBook_no%>"
                                       data-amount="<%=payAmt%>"
                                       data-custtype="<%=payCustType%>"
                                       data-agencycode="<%=payAgency != null ? payAgency : ""%>"
                                       onchange="updateBkgPaymentSummary()">
                            <%}%>
                        </td>
                        <%}%>

                        <%if ("C".equalsIgnoreCase(LRPStatus)) {%>
                        <td>
                            <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=lprBook_no%>', '<%=eBooking.getAppVersion()%>')"
                               class="btn btn-lg text-info" data-toggle="tooltip" title="View" data-placement="right">
                                <i class="fa fa-search"></i></a>
                                <%if ("Y".equalsIgnoreCase(Enable_ConfirmedBooking_Edit)) {%>
                            <a id="Edit_Confirmed" href="javascript:void(0)" onclick="validateConfirmedBookingEdit('<%=LRPStatus%>', '<%=lprBook_no%>', '<%=bookno%>', '<%=eBooking.getAppVersion()%>')" title="Amendment" class="btn btn-lg text-warning" data-toggle="tooltip"  data-placement="left"><i class="fa fa-pencil-square-o"></i></a>
                                <%}%>
                            <a href="javascript:void(0);" onClick="viewBookMethod_new('<%=lprBook_no%>', '<%=agencyid%>', '<%=eBooking.getAppVersion()%>')" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" data-toggle="tooltip"  data-placement="right" title="Print"><i class="fa fa-file-pdf-o"></i></a>
                            <a href="javascript:void(0)" onclick="bookingReuse('<%=LRPStatus%>', '<%=lprBook_no%>', '<%=bookno%>', '<%=eBooking.getAppVersion()%>')" title="Reuse" class="btn btn-lg text-success" data-toggle="tooltip"  data-placement="left"><i class="fa fa-recycle"></i></a>     
                                <% if ("GWF".equalsIgnoreCase(carriercode) && "Y".equalsIgnoreCase(haulageTracking)) {%>
                            <a href="javascript:void(0)" onclick="bookingHaulageDetails('<%=lprBook_no%>')" title="Haulage"   data-toggle="tooltip"  data-placement="left" class="btn btn-lg text-info" ><i class="fa fa-truck"></i></a>                                              
                            <%}%>
                                <%if ("Y".equalsIgnoreCase(Enable_Cancel_Option_Confirmed_BKG)) {%>
                            <a href="javascript:void(0)" onclick="cancelConfirmedBooking('<%=lprBook_no%>', '<%=bookno%>')" title="Cancel" class="btn btn-lg text-danger" data-toggle="tooltip" data-placement="left"><i class="fa fa-times"></i></a>
                                <%}%>
                        </td>
                        <%} else if ("S".equalsIgnoreCase(LRPStatus)) {%>
                        <td>
                            <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=lprBook_no%>', '<%=eBooking.getAppVersion()%>')"
                               class="btn btn-lg text-info" data-toggle="tooltip" title="View" data-placement="right">
                                <i class="fa fa-search"></i></a> 
                            <a href="javascript:void(0)" onclick="bookingAmendment('<%=LRPStatus%>', '<%=lprBook_no%>', '<%=bookno%>', '<%=eBooking.getAppVersion()%>')" title="Amendment" class="btn btn-lg text-warning" data-toggle="tooltip"  data-placement="left"><i class="fa fa-pencil-square-o"></i></a>
                            <a href="javascript:void(0)" onclick="cancelSubmitBooking('<%=lprBook_no%>', '<%=bookno%>', '<%=eBooking.getAppVersion()%>')" title="Cancel" class="btn btn-lg text-danger" data-toggle="tooltip"  data-placement="left"><i class="fa fa-times"></i></a>
                        </td>
                        <%} else if ("P".equalsIgnoreCase(LRPStatus)) {%>
                        <td>
                            <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=lprBook_no%>', '<%=eBooking.getAppVersion()%>')"
                               class="btn btn-lg text-info" data-toggle="tooltip" title="View" data-placement="right">
                                <i class="fa fa-search"></i></a>
                            <a href="javascript:void(0)" onclick="bookingAmendment('<%=LRPStatus%>', '<%=lprBook_no%>', '<%=bookno%>', '<%=eBooking.getAppVersion()%>')" title="Amendment" class="btn btn-lg text-warning" data-toggle="tooltip"  data-placement="left"><i class="fa fa-pencil-square-o"></i></a>
                            <%if(agencyid != null && !disableDraftPrintAgency.contains(agencyid)){%>
                            <a href="javascript:void(0);" onClick="viewBookMethod_new('<%=lprBook_no%>', '<%=agencyid%>', '<%=eBooking.getAppVersion()%>')" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" data-toggle="tooltip"  data-placement="left" title="Print"><i class="fa fa-file-pdf-o"></i></a>
                            <%}%>
                            <a href="javascript:void(0)" onclick="bookingReuse('<%=LRPStatus%>', '<%=lprBook_no%>', '<%=bookno%>', '<%=eBooking.getAppVersion()%>')" title="Reuse" class="btn btn-lg text-success" data-toggle="tooltip"  data-placement="left"><i class="fa fa-recycle"></i></a>
                                <%if (!"Y".equalsIgnoreCase(hideLRPBookingCancel)) {%>
                            <a href="javascript:void(0)" onclick="cancelLRPBooking('<%=lprBook_no%>', '<%=bookno%>')" title="Cancel" class="btn btn-lg text-danger" data-toggle="tooltip"  data-placement="left"><i class="fa fa-times"></i></a>                               
                                <%}%>
                                <% if ("GWF".equalsIgnoreCase(carriercode) && "Y".equalsIgnoreCase(haulageTracking)) {%>
                            <a href="javascript:void(0)" onclick="bookingHaulageDetails('<%=lprBook_no%>')" title="Haulage"   data-toggle="tooltip"  data-placement="left" class="btn btn-lg text-info"  ><i class="fa fa-truck"></i></a>                                              
                                <%}%>
                        </td>
                        <%} else if ("V".equalsIgnoreCase(LRPStatus)) {%>
                        <td>
                            <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=trust_action_no%>', '<%=eBooking.getAppVersion()%>')" title='View'
                               class="btn btn-lg text-info" data-toggle="tooltip" data-placement="right">
                                <i class="fa fa-search"></i></a>
                            <a href="javascript:void(0)" onclick="bookingAmendmentConfirm('<%=LRPStatus%>', '<%=lprBook_no%>', '<%=bookno%>', '<%=eBooking.getAppVersion()%>')" title="Resubmit" class="btn btn-lg text-success" data-toggle="tooltip"  data-placement="left"><i class="fa fa-repeat"></i></a>
                        </td>
                        <%} else if ("L".equalsIgnoreCase(LRPStatus)) {%>
                        <td>
                            <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=lprBook_no%>', '<%=eBooking.getAppVersion()%>')" title='View'
                               class="btn btn-lg text-info" data-toggle="tooltip" data-placement="right">
                                <i class="fa fa-search"></i></a></td>

                        <%} else if ("I".equalsIgnoreCase(LRPStatus)) {%>
                        <td> <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=trust_action_no%>', '<%=eBooking.getAppVersion()%>')"  class="btn btn-lg text-info" data-toggle="tooltip" title="View" data-placement="right"><i class="fa fa-search"></i></a>
                        </td>
                        <%} else if ("X".equalsIgnoreCase(LRPStatus)) {%>
                        <td>
                            <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=lprBook_no%>', '<%=eBooking.getAppVersion()%>')"
                               class="btn btn-lg text-info" data-toggle="tooltip" title="View" data-placement="right">
                                <i class="fa fa-search"></i></a>
                        </td>
                        <%} else {%>

                        <td> <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=trust_action_no%>', '<%=eBooking.getAppVersion()%>')"  class="btn btn-lg text-info" data-toggle="tooltip" title="View" data-placement="right"><i class="fa fa-search"></i></a>

                            <a href="javascript:void(0)" onclick="bookingEdit('edit', 'B', 'editdetails', '<%=bookno%>', '', '<%=eBooking.getAppVersion()%>')" title="Edit" class="btn btn-lg text-warning" data-toggle="tooltip"  data-placement="left"><i class="fa fa-pencil-square-o"></i></a>
                        </td> 
                        <%}%>
                        <%if ("Y".equalsIgnoreCase(shotRefNo)) {%>
                        <td><%=lprBook_no%></td>
                        <%} else {%>
                        <td></td>
                        <%}%>
                        <td><%=bookno%></td>
                        <%if (!"Y".equalsIgnoreCase(hideAgencyRefno)) {%>
                        <td><%=agencyrefno%></td>
                        <%}%>
                        <td><%=LRPStatus_new%></td>
                        <td><%=originport%></td>
                        <td><%=deliveryport%></td>
                        <td><%=crdate%></td>
                        <td><%=eBooking.getConfirmedDate()%></td>
                        <%  if ("GWF".equalsIgnoreCase(carriercode)) {%>
                        <td><%=eBooking.getPoletd()%></td>
                        <td><%=eBooking.getLrpusercr()%></td>
                        <%}%>
                        <td><%=eBooking.getDgStatus()%></td>
                        <td><%=eBooking.getTuesCount()%></td>
                         <td><%=Subdate%></td>
                        <td><%=cruser%></td>
                    </tr>
                    <%

                                    if (lprBook_no != null && lprBook_no.length() > 0) {
                                     //   System.out.println("[ECOM-WAR] ebookView lprBook_no....." + lprBook_no);
                                        bookset.add(lprBook_no);
                                    }
                                    if (bookno != null && bookno.length() > 0) {
                                      //  System.out.println("[ECOM-WAR] ebookView bookno........." + bookno);
                                        bookset.add(bookno);
                                    }
                                }
                            }
                            session.setAttribute("bookingAccessList", bookset);
                        }%>
                </tbody>

            </table>

        </div>
    </div>
</div>
<div class="modal fade" id="reasonmodal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title">   <bean:message key="ebooking.msg.bkgcancel" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <div class="col-sm-offset-0 col-sm-11">
                                <label> <bean:message key="ebooking.msg.reasonbkg" bundle="<%=clientcode%>"/> :  </label>
                                <input type="hidden" id="cancelbookno">
                                <input type="hidden" id="ecombookno">
                                <textarea class="form-control" id="reason" name="reason" rows="3" cols="50" style="resize:none"></textarea> 
                                <label class="error" id="reasonreq" style="display:none"><bean:message key="ebooking.msg.bkgcancelreq" bundle="<%=clientcode%>"/></label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="pull-right">
                            <a href="javascript:void(0)" title="Submit"  class="btn btn-info btn-xs" onclick="cancelLRPBookingWithRemarks()" id="mailbtn">  <bean:message key="ecom.submit" bundle="<%=clientcode%>"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="cancelConfirmedReasonModal" role="dialog" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">
                        <h5 class="modal-title">Booking Cancellation</h5>
                        <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="cancelConfLrpBookNo">
                        <input type="hidden" id="cancelConfEcomBookNo">
                        <div class="form-group row">
                            <div class="col-sm-11">
                                <label>Reason for Cancellation <span class="text-danger">*</span></label>
                                <select class="form-control" id="cancelConfReason" onchange="toggleOtherReason()">
                                    <option value="">-- Select Reason --</option>
                                    <% if (bkgCancelReasonList != null) {
                                        for (int r = 0; r < bkgCancelReasonList.size(); r++) {
                                            String reasonVal = (String) bkgCancelReasonList.get(r);
                                    %>
                                    <option value="<%=reasonVal.trim()%>"><%=reasonVal.trim()%></option>
                                    <% } } %>
                                </select>
                                <label class="error" id="cancelConfReasonReq" style="display:none">Please select a reason for cancellation</label>
                            </div>
                        </div>
                        <div class="form-group row" id="otherReasonDiv" style="display:none">
                            <div class="col-sm-11">
                                <label>Specify Reason <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="cancelConfOtherReason" maxlength="100" placeholder="Enter reason (max 100 characters)">
                                <label class="error" id="cancelConfOtherReasonReq" style="display:none">Please specify the reason</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-11">
                                <label>Remarks <span class="text-danger">*</span></label>
                                <textarea class="form-control" id="cancelConfRemarks" rows="3" cols="50" maxlength="500" style="resize:none" placeholder="Enter remarks (max 500 characters)"></textarea>
                                <label class="error" id="cancelConfRemarksReq" style="display:none">Please enter remarks</label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="pull-right">
                            <button type="button" class="btn btn-default btn-xs" data-dismiss="modal" onclick="closeCancelConfirmedDialog()">Close</button>
                            <a href="javascript:void(0)" title="Submit" class="btn btn-info btn-xs" onclick="submitCancelConfirmedBooking()">Submit</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<% if (showTemplateMsgPopup) { %>
<div class="modal fade" id="saveTemplateMsgModal" role="dialog" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><bean:message key="ebooking.head.ebk" bundle="<%=clientcode%>"/></h5>
                <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-body">
                <p style="text-align: justify; line-height: 1.6; margin-bottom: 0;"><%=saveTemplateMsg.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")%></p>
            </div>
            <div class="modal-footer">
                <div class="pull-right">
                    <button type="button" class="btn btn-info btn-xs" data-dismiss="modal">OK</button>
                </div>
            </div>
        </div>
    </div>
</div>
<% } %>
<script type="text/javascript" >
    $(document).ready(function () {
        $('#frdateport').attr('placeholder', '<bean:message key="ebooking.label.fromdate" bundle="<%=clientcode%>"/>');
        $('#todateport').attr('placeholder', '<bean:message key="ebooking.label.todate" bundle="<%=clientcode%>"/>');
        $('#frdateport,#todateport').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
        //$('#frdateport,#todateport').attr('autocomplete', 'off');
        $("label.error").hide();

        $('#datatablebybooking').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
            <%if (isPaymentEnabled) {%>
            ,"columnDefs": [{ "orderable": false, "targets": 0 }]
            <%}%>
        });

    });

</script>
<script type="text/javascript">
    function bookingEdit(action, flag, type, bookno, book_type, appversion) {
        //callLoading();
        $("#action").val(action);
        $("#flag").val(flag);
        $("#type").val(type);
        $("#bookno").val(bookno);
        $("#book_type").val(book_type);
        $("#appversion").val(appversion);
        document.getElementById('EBookingViewAllForm').setAttribute('action', 'ebooking_crud.do');
        document.EBookingViewAllForm.submit();
    }

    function bookingAmendmentConfirm(type, lrpBookNo, ecomBookNo, appversion)
    {
        $("#appversion").val(appversion);
        $.ajax({
            type: "POST",
            async: false,
            cache: false,
            url: "ecomAjax?type=getdeclinedremarks&ecombookno=" + encodeURIComponent(ecomBookNo) + "&status=" + encodeURIComponent(type),
            success: function (response) {
                var b = confirm("<bean:message key="ecom.msg.declinedremarks" bundle="<%=clientcode%>" /> " + response + "\n\n<bean:message key="ecom.msg.editbooking" bundle="<%=clientcode%>" />");
                if (b == false)
                {
                    return false;
                } else
                {
                    //   callLoading();
                    bookingAmendment(type, lrpBookNo, ecomBookNo, appversion);
                }
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function bookingAmendment(type, lrpBookNo, ecomBookNo, appversion) {
        $("#ecomBookNo").val(ecomBookNo);
        $("#lrpbookno").val(lrpBookNo);
        $("#appversion").val(appversion);
        $("#book_type").val(type);
        $.ajax({
            type: "POST",
            async: false,
            cache: false,
            url: "ecomAjax?type=checksicreation&lrpbookno=" + encodeURIComponent(lrpBookNo) + "&ecombookno=" + encodeURIComponent(ecomBookNo) + "&status=" + encodeURIComponent(type) + "&appversion=" + appversion,
            success: function (response) {
                //alert(response);
                if (response == 'NO') {
                    //   callLoading();
                    document.getElementById('EBookingViewAllForm').setAttribute('action', 'editamendmentbooking.do');
                    document.EBookingViewAllForm.submit();
                } else if (response == 'YES') {
                    alert("<bean:message key="ecom.msg.createdsiupdate" bundle="<%=clientcode%>" />");
                }
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function bookingReuse(type, lrpBookNo, ecomBookNo, appversion) {

        $("#ecomBookNo").val(ecomBookNo);
        $("#lrpbookno").val(lrpBookNo);
        $("#appversion").val(appversion);
        $("#book_type").val("R");
        $("#action").val("reuse");
        // callLoading();
        //document.getElementById('EBookingViewAllForm').setAttribute('action', 'editamendmentbooking.do');
        document.getElementById('EBookingViewAllForm').setAttribute('action', 'ebooking_crud.do');
        document.EBookingViewAllForm.submit();

    }
    function bookingView(type, bookno, book_type, ecomlrptype, lrpbookno, appversion) {
        //callLoading();
        $("#ecomlrptype").val(ecomlrptype);
        $("#lrpbookno").val(lrpbookno);
        $("#type").val(type);
        $("#bookno").val(bookno);
        $("#book_type").val(book_type);
        $("#appversion").val(appversion);
        document.getElementById('EBookingViewAllForm').setAttribute('action', 'ViewEbookingEntry.do');
        document.EBookingViewAllForm.submit();
    }

    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
        //  var ptable = $('#datatablebybooking').DataTable({
        //        "aaSorting": []
        //    });
        //$('#EBookingViewAllForm .input-daterange').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
        //$("label.error").hide();

        $("#searchbtn").click(function () {
            $('#EBookingViewAllForm label.error').remove();
        });

        $("#EBookingViewAllForm").validate({
            rules: {
                frdateport: {
                    required: function (element) {
                        return !$('#bookall').checked
                    }
                },
                todateport: {
                    required: function (element) {
                        return !$('#bookall').checked
                    }
                }
            },
            messages: {
                frdateport: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                todateport: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                var urlval = "";
                var fdate = $('#frdateport').val();
                var tdate = $('#todateport').val();
                urlval = "AjaxRequestDateRecords.do?type=search" + "&fromdate=" + fdate + "&todate=" + tdate;
                $("#searchbtn").button('loading');
                $.ajax({
                    type: "POST",
                    url: urlval,
                    success: function (response) {
                        $("#bookingsearchviewdiv").html(response);
                        $("#searchbtn").button('reset');
                    },
                    error: function (xhr) {
                        if (xhr.status == 403) {
                            window.location.href = "SessionExpired.jsp";
                        }
                    }
                });
                return false;
            }
        });
    });

    function newBooking() {
        //callLoading();
    <%
        session.setAttribute("BookingRequestType", "NewBooking");
    %>
        document.EBookingViewAllForm.submit();
    }

    function callTableProp() {
        $('#example').dataTable({
            "bJQueryUI": true
        });
    }

    function chkDate()
    {
        if (document.getElementById('bookall').checked)
        {
            document.getElementById('frdateport').value = "";
            document.getElementById('todateport').value = "";
            document.getElementById('frdateport').disabled = true;
            document.getElementById('todateport').disabled = true;
        } else
        {
            document.getElementById('frdateport').disabled = false;
            document.getElementById('todateport').disabled = false;
        }
    }


    function viewBookMethod_new(bookno, arnno, appversion) {
        if (appversion != '1') {
            openLoader();
            $("#appversion").val(appversion);
            var URLStr = "BookingJasper?bookno=" + bookno + "&agencyid=" + arnno;
            $.ajax({
                type: "POST",
                cache: false,
                url: URLStr,
                data: $(this).serializeArray(),
                success: function (data) {
                    closeLoader();
                    console.log(data);
                    if (data != null && 'Success' == data) {
                        console.log("if");
                        $.fancybox.open({
                            maxWidth: 900,
                            maxHeight: 700,
                            fitToView: false,
                            width: '100%',
                            height: '100%',
                            autoSize: false,
                            scrollOutside: true,
                            closeClick: false,
                            src: "ViewPDF.jsp",
                            type: 'iframe',
                            overlayShow: false,
                            iframe: {
                                preload: false // fixes issue with iframe and IE
                            }
                        });
                        return true;
                    } else {
                        alert(data);
                        return false;
                    }
                },
                error: function (data, xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    alert(data);
                }
            });
        } else {
            //alert('This option is unavailable due to system upgrade. Kindly collect the details from your local agency.');
            openLoader();
            $("#appversion").val(appversion);
            var URLStr = "BookingV1Jasper?bookno=" + bookno + "&agencyid=" + arnno;
            $.ajax({
                type: "POST",
                cache: false,
                url: URLStr,
                data: $(this).serializeArray(),
                success: function (data) {
                    closeLoader();
                    if (data != null && 'Success' == data) {
                        $.fancybox.open({
                            maxWidth: 900,
                            maxHeight: 700,
                            fitToView: false,
                            width: '100%',
                            height: '100%',
                            autoSize: false,
                            scrollOutside: true,
                            closeClick: false,
                            src: "ViewPDF.jsp",
                            type: 'iframe',
                            overlayShow: false,
                            iframe: {
                                preload: false // fixes issue with iframe and IE
                            }
                        });
                        return true;
                    } else {
                        alert(data);
                        return false;
                    }
                },
                error: function (data, xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    alert(data);
                }
            });
        }
    }
    function setdefault()
    {
        if (document.getElementById('frdateport').value != "" && document.getElementById('todateport').value != "")
        {
            //callLoading();
            location.href = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
        }
    }
    function tempbookingEdit(bookno)
    {
        var tempbookno = bookno;
        $('#manageTemplate').val("Y");
        $('#action').val("templateedit");
        $('#flag').val("T");
        $('#bookno').val(tempbookno);
        // callLoading();
        document.getElementById('EBookingViewAllForm').action = "ebooking_crud.do";
        document.EBookingViewAllForm.submit();
        //window.location.href="+"&manageTemplate=Y";
    }

    function tempbookingdelete(bookno, templatename)
    {
        var b = confirm("<bean:message key="ecom.msg.delbooking" bundle="<%=clientcode%>" />");
        if (b == false)
        {
            return false;
        } else
        {
            $.ajax({
                type: "POST",
                cache: false,
                beforeSend: function () {
                    if (1 == 1) {
                        //$("#mgtemp").html("<i class='fa fa-spinner fa-spin'></i> Loading...");
                    }
                },
                url: "ecomAjax?type=deletetempbooking&actiontype=deletetempbooking&bookno=" + bookno,
                data: $(this).serializeArray(),
                success: function (data) {
                    $.fancybox.open(data, {
                        showCloseButton: false,
                        closeClick: false,
                        maxWidth: 1200,
                        maxHeight: 600,
                        padding: 0,
                        fitToView: true,
                        async: false,
                        width: '90%',
                        height: '100%',
                        autoSize: true,
                        scrollOutside: true,
                        closeBtn: false,
                        hideOnOverlayClick: false,
                        overlayShow: false,
                        helpers: {
                            overlay: {closeClick: false}
                        },
                        afterClose: function () {
                            // $("#mgtemp").html("Manage Template");
                        },
                        keys: {
                            close: null
                        }

                    });
                },
                error: function (data, xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    //$("#mgtemp").html("Manage Template");
                }
            });
        }
    }
    function templateNewBooking(bookno) {
        //callLoading();
        $('#action').val("template");
        $('#flag').val("T");
        $('#bookno').val(bookno);
        // callLoading();
        document.getElementById('EBookingViewAllForm').action = "ebooking_crud.do";
        document.EBookingViewAllForm.submit();
//        window.location.href = "ebooking_crud.do?action=template&flag=T&bookno=" + bookno;
    }

    function tempbookingshow()
    {
        // callLoading();
        $.ajax({
            type: "POST",
            cache: false,
            beforeSend: function () {
                if (1 == 1) {
                    //$('#mgtemp').html("<i class='fa fa-spinner fa-spin'></i> Loading...");
                }
            },
            url: "ecomAjax?type=deletetempbooking&actiontype=viewtempbooking",
            data: $(this).serializeArray(),
            success: function (data) {
                //       destroyLoading();
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
                    afterClose: function () {
                        //$("#mgtemp").html("Manage Template");
                    },
                    keys: {
                        close: null
                    }

                });
            },
            error: function (data, xhr) {
                // destroyLoading();
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
                //$("#mgtemp").html("Manage Template");
            }
        });
    }

//    $(function () {
//        $(".modal-draggable .modal-dialog").draggable({
//            handle: ".panel-heading"
//        });
//    });
    function cancelSubmitBooking(lrpbookno, ecombookno, appversion) {
        var cancel = confirm("<bean:message key="ecom.msg.cancelbooking" bundle="<%=clientcode%>" />");
        if (cancel) {
            $.ajax({
                type: "POST",
                cache: false,
                url: "BookingAjax?type=cancelbooking&ecombookno=" + encodeURIComponent(ecombookno) + "&lrpbookno=" + encodeURIComponent(lrpbookno) + "&appversion=" + encodeURIComponent(appversion),
                data: $(this).serializeArray(),
                success: function (data) {
                    if (data == 'Processed') {
                        alert("<bean:message key="ecom.msg.bookingprocessed" bundle="<%=clientcode%>" />");
                    } else if (data == 'Error') {
                        alert("Something went wrong,please try again");
                    } else {
                        alert("Your submitted booking is cancelled successfully!");
                        //   callLoading();
                        location.href = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
                    }
                },
                error: function (data, xhr) {
                    //  destroyLoading();
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    //$("#mgtemp").html("Manage Template");
                }
            });
        }
    }

    function cancelLRPBooking(lrpbookno, ecombookno) {
        var cancel = confirm("<bean:message key="ebooking.msg.cancelbkg" bundle="<%=clientcode%>"/>");
        if (cancel) {
            $("#cancelbookno").val(lrpbookno);
            $("#ecombookno").val(ecombookno);
            $.ajax({
                type: "POST",
                cache: false,
                url: "BookingAjax?type=validateCancellrpbooking&lrpbookno=" + encodeURIComponent(lrpbookno) + "&validationtype=draftbkgcancel",
                data: $(this).serializeArray(),
                success: function (data) {
                    if (data == 'valid') {
                        $('#reasonmodal').modal('show');
                    } else if (data == 'cro') {
                        alert("<bean:message key="ebooking.msg.crodone" bundle="<%=clientcode%>"/>");
                    } else if (data == 'bl') {
                        alert("<bean:message key="ebooking.msg.blavail" bundle="<%=clientcode%>"/>");
                    } else if (data == 'operation') {
                        alert("<bean:message key="ebooking.msg.loadplandone" bundle="<%=clientcode%>"/>");
                    } else if (data == 'ems') {
                        alert("<bean:message key="ebooking.msg.unabletoclbkg" bundle="<%=clientcode%>"/>");
                    } else if (data == 'si') {
                        alert("<bean:message key="ebooking.msg.sicreatedcancelnotallowed" bundle="<%=clientcode%>"/>");
                    }


                },
                error: function (data, xhr) {
                    //    destroyLoading();
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });

        }
    }

    function validateConfirmedBookingEdit(type, lrpBookNo, ecomBookNo, appversion) {

        $.ajax({
            type: "POST",
            cache: false,
            url: "BookingAjax?type=validateCancellrpbooking&lrpbookno=" + encodeURIComponent(lrpBookNo) + "&validationtype=confirmbkgedit",
            data: $(this).serializeArray(),
            success: function (data) {
                if (data == 'valid') {
                    if (enableTermsConditionAccept === 'Y') {
                        pendingBkgAmendType = type;
                        pendingBkgAmendLrpBookNo = lrpBookNo;
                        pendingBkgAmendEcomBookNo = ecomBookNo;
                        pendingBkgAmendAppversion = appversion;
                        showBkgConfEditTC();
                    } else {
                        bookingAmendment(type, lrpBookNo, ecomBookNo, appversion);
                    }
                } else if (data == 'cro') {
                    alert("<bean:message key="ebooking.msg.crodone" bundle="<%=clientcode%>"/>");
                } else if (data == 'bl') {
                    alert("<bean:message key="ebooking.msg.blavail" bundle="<%=clientcode%>"/>");
                } else if (data == 'operation') {
                    alert("<bean:message key="ebooking.msg.loadplandone" bundle="<%=clientcode%>"/>");
                } else if (data == 'ems') {
                    alert("<bean:message key="ebooking.msg.unabletoeditbkg" bundle="<%=clientcode%>"/>");
                } else if (data == 'si') {
                    alert("<bean:message key="ecom.msg.createdsiupdate" bundle="<%=clientcode%>"/>");
                }


            },
            error: function (data, xhr) {
                //    destroyLoading();
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function cancelLRPBookingWithRemarks() {
        var lrpbookno = $("#cancelbookno").val();
        var reason = $("#reason").val();
        var ebookno = $("#ecombookno").val();
        if (document.getElementById('reason').value.length == 0) {
            $('#reasonreq').show();
            document.getElementById("reason").focus();
            return false;
        } else {
            $.ajax({
                type: "POST",
                cache: false,
                url: "BookingAjax?type=cancellrpbooking&lrpbookno=" + encodeURIComponent(lrpbookno) + "&reason=" + reason + "&ebookno=" + ebookno + "&canceluser=<%=loginUser%>",
                data: $(this).serializeArray(),
                success: function (data) {
                    if (data == 'Cancelled') {
                        alert("Your booking is cancelled successfully!");
                        //     callLoading();
                        location.href = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
                    }
                    $('#reasonmodal').hide();
                    document.getElementById('reason').value = "";
                },
                error: function (data, xhr) {
                    //  destroyLoading();
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    //$("#mgtemp").html("Manage Template");
                }
            });
        }
        return true;
    }
    function bookingHaulageDetails(bookno) {
        openLoader();
        $.ajax({
            type: "POST",
            url: "BookingAjax?type=bookingHaulageDetails&bookno=" + bookno,
            success: function (response) {
                closeLoader();
                $.fancybox.open(response, {
                    showCloseButton: false,
                    closeClick: false,
                    maxWidth: 1200,
                    maxHeight: 600,
                    padding: 0,
                    fitToView: true,
                    async: false,
                    width: '90%',
                    height: '100%',
                    autoSize: true,
                    scrollOutside: true,
                    closeBtn: false,
                    hideOnOverlayClick: false,
                    overlayShow: false,
                    helpers: {
                        overlay: {closeClick: false}
                    },
                    afterClose: function () {

                    },
                    keys: {
                        close: null
                    }

                });
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function closeDialog()
    {
        document.getElementById('reason').value = "";
    }

    var enableTermsConditionAccept = '<%=Enable_TermsCondition_Accept%>';
    var bkgCancelTCHtml = "<div id='bkgCancelTCDiv'><style>#bkgCancelTCDiv h4{margin-top:0;} #bkgCancelTCDiv.fancybox-content{padding:0px !important;width:70% !important;max-width:70vw !important;}</style><div class='w-100 h-100' ><div class='card-header page_header' style='margin-top:0px;margin-right:0px;'><img class='header_icon_pages' src='Design/assets/images/page_icons/schdule.png'> Booking Cancellation - Terms and Conditions</div><div style='max-height: calc(100vh - 200px);overflow-y: auto;padding:15px;'><%=bkgCancelTC != null ? bkgCancelTC.replace("\"", "'").replace("\n", "").replace("\r", "") : ""%></div><div class='text-center'><a href='javascript:;' class='btn btn-info btn-lg m-t-5 m-b-5 text-white' style='width: 200px;' onclick='agreeBkgCancelTC()'>I Agree</a></div></div></div>";
    var bkgConfEditTCHtml = "<div id='bkgConfEditTCDiv'><style>#bkgConfEditTCDiv h4{margin-top:0;} #bkgConfEditTCDiv.fancybox-content{padding:0px !important;width:70% !important;max-width:70vw !important;}</style><div class='w-100 h-100' ><div class='card-header page_header' style='margin-top:0px;margin-right:0px;'><img class='header_icon_pages' src='Design/assets/images/page_icons/schdule.png'> Booking Amendment - Terms and Conditions</div><div style='max-height: calc(100vh - 200px);overflow-y: auto;padding:15px;'><%=bkgConfEditTC != null ? bkgConfEditTC.replace("\"", "'").replace("\n", "").replace("\r", "") : ""%></div><div class='text-center'><a href='javascript:;' class='btn btn-info btn-lg m-t-5 m-b-5 text-white' style='width: 200px;' onclick='agreeBkgConfEditTC()'>I Agree</a></div></div></div>";
    var pendingBkgAmendType = '', pendingBkgAmendLrpBookNo = '', pendingBkgAmendEcomBookNo = '', pendingBkgAmendAppversion = '';

    function cancelConfirmedBooking(lrpbookno, ecombookno) {
        $.ajax({
            type: "POST",
            cache: false,
            url: "BookingAjax?type=validateCancellrpbooking&lrpbookno=" + encodeURIComponent(lrpbookno) + "&validationtype=confirmbkgcancel",
            data: $(this).serializeArray(),
            success: function (data) {
                if (data == 'valid') {
                    $("#cancelConfLrpBookNo").val(lrpbookno);
                    $("#cancelConfEcomBookNo").val(ecombookno);
                    if (enableTermsConditionAccept === 'Y') {
                        showBkgCancelTC();
                    } else {
                        $('#cancelConfirmedReasonModal').modal('show');
                    }
                } else if (data == 'cro') {
                    alert("<bean:message key="ebooking.msg.crodone" bundle="<%=clientcode%>"/>");
                } else if (data == 'bl') {
                    alert("<bean:message key="ebooking.msg.blavail" bundle="<%=clientcode%>"/>");
                } else if (data == 'operation') {
                    alert("<bean:message key="ebooking.msg.loadplandone" bundle="<%=clientcode%>"/>");
                } else if (data == 'ems') {
                    alert("<bean:message key="ebooking.msg.unabletoclbkg" bundle="<%=clientcode%>"/>");
                } else if (data == 'si') {
                    alert("<bean:message key="ebooking.msg.sicreatedcancelnotallowed" bundle="<%=clientcode%>"/>");
                }
            },
            error: function (data, xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }

    function applyTermsConditionPopupWidth(instance, slide) {
        var $content = (slide && slide.$content) ? slide.$content : $('.fancybox-content:visible').last();
        $content.css({width: '70%', 'max-width': '70vw'});
        if (instance && instance.update) {
            instance.update();
        } else if ($.fancybox && $.fancybox.update) {
            $.fancybox.update();
        }
    }

    function showBkgCancelTC() {
        $.fancybox.open(bkgCancelTCHtml, {
            padding: 0,
            showCloseButton: false,
            closeClick: false,
            maxWidth: '70%',
            maxHeight: 600,
            fitToView: false,
            async: false,
            width: '70%',
            height: '80%',
            autoSize: false,
            scrollOutside: true,
            closeBtn: true,
            hideOnOverlayClick: false,
            overlayShow: false,
            helpers: {
                overlay: {closeClick: false}
            },
            afterShow: applyTermsConditionPopupWidth
        });
    }

    function agreeBkgCancelTC() {
        $.fancybox.close();
        $('#cancelConfirmedReasonModal').modal('show');
    }

    function showBkgConfEditTC() {
        $.fancybox.open(bkgConfEditTCHtml, {
            padding: 0,
            showCloseButton: false,
            closeClick: false,
            maxWidth: '70%',
            maxHeight: 600,
            fitToView: false,
            async: false,
            width: '70%',
            height: '100%',
            autoSize: false,
            scrollOutside: true,
            closeBtn: true,
            hideOnOverlayClick: false,
            overlayShow: false,
            helpers: {
                overlay: {closeClick: false}
            },
            afterShow: applyTermsConditionPopupWidth
        });
    }

    function agreeBkgConfEditTC() {
        $.fancybox.close();
        bookingAmendment(pendingBkgAmendType, pendingBkgAmendLrpBookNo, pendingBkgAmendEcomBookNo, pendingBkgAmendAppversion);
    }

    function toggleOtherReason() {
        var selectedVal = $('#cancelConfReason').val();
        if (selectedVal === 'Other') {
            $('#otherReasonDiv').show();
        } else {
            $('#otherReasonDiv').hide();
            $('#cancelConfOtherReason').val('');
            $('#cancelConfOtherReasonReq').hide();
        }
        $('#cancelConfReasonReq').hide();
    }

    function submitCancelConfirmedBooking() {
        var reason = $('#cancelConfReason').val();
        var otherReason = $('#cancelConfOtherReason').val();
        var remarks = $('#cancelConfRemarks').val();

        if (!reason || reason.length === 0) {
            $('#cancelConfReasonReq').show();
            $('#cancelConfReason').focus();
            return false;
        }
        if (reason === 'Other' && (!otherReason || otherReason.trim().length === 0)) {
            $('#cancelConfOtherReasonReq').show();
            $('#cancelConfOtherReason').focus();
            return false;
        }
        if (!remarks || remarks.trim().length === 0) {
            $('#cancelConfRemarksReq').show();
            $('#cancelConfRemarks').focus();
            return false;
        }

        var finalReason = (reason === 'Other') ? otherReason.trim() : reason;
        var lrpbookno = $('#cancelConfLrpBookNo').val();
        var ecombookno = $('#cancelConfEcomBookNo').val();

        var cancelForm = document.createElement('form');
        cancelForm.method = 'POST';
        cancelForm.action = 'BookingCancelAction.do';
        cancelForm.style.display = 'none';

        var fields = {
            'lrpbookno': lrpbookno,
            'ecombookno': ecombookno,
            'cancelReason': finalReason,
            'cancelRemarks': remarks.trim()
        };
        for (var key in fields) {
            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = fields[key];
            cancelForm.appendChild(input);
        }

        document.body.appendChild(cancelForm);
        cancelForm.submit();
    }

    function closeCancelConfirmedDialog() {
        $('#cancelConfReason').val('');
        $('#cancelConfOtherReason').val('');
        $('#cancelConfRemarks').val('');
        $('#otherReasonDiv').hide();
        $('#cancelConfReasonReq').hide();
        $('#cancelConfOtherReasonReq').hide();
        $('#cancelConfRemarksReq').hide();
    }

    <%if (tempupdatemsg != null && tempupdatemsg.length() > 0) {%>
    <% if (showTemplateMsgPopup) { %>
    $(document).ready(function () {
        $('#saveTemplateMsgModal').modal('show');
    });
    <% } else { %>
    $(document).ready(function () {
        toastr.success("", "<%=tempupdatemsg%>");
    });
    <% } %>
    <%}%>

    <%if (isPaymentEnabled) {%>
    function toggleAllBkgCheckboxes(source) {
        var checkboxes = document.querySelectorAll('.bkg-row-cb:not(:disabled)');
        checkboxes.forEach(function(cb) { cb.checked = source.checked; });
        updateBkgPaymentSummary();
    }

    function updateBkgPaymentSummary() {
        var checked = Array.prototype.slice.call(document.querySelectorAll('.bkg-row-cb:checked'));
        var bar = document.getElementById('bkgPaymentBar');
        var total = 0.00;

        if (checked.length > 0) {
            var firstAgency = checked[0].getAttribute('data-agencycode') || '';
            var mixedAgency = checked.some(function(cb) {
                return (cb.getAttribute('data-agencycode') || '') !== firstAgency;
            });

            if (mixedAgency) {
                alert('Selected bookings belong to different agencies. Please select bookings from the same agency only.');
                checked.forEach(function(cb) {
                    if ((cb.getAttribute('data-agencycode') || '') !== firstAgency) {
                        cb.checked = false;
                    }
                });
                checked = Array.prototype.slice.call(document.querySelectorAll('.bkg-row-cb:checked'));
            }

            if (checked.length > 0) {
                checked.forEach(function(cb) {
                    total += parseFloat(cb.getAttribute('data-amount') || '0');
                });
                document.getElementById('bkgSelectedCount').textContent = checked.length;
                if (<%=paymentCurrencyConfigured%>) {
                    document.getElementById('bkgTotalAmount').textContent = '<%=paymentCurrency%> ' + total.toFixed(2);
                } else {
                    document.getElementById('bkgTotalAmount').textContent = '<%=paymentCurrencyMsg%>';
                }
                document.getElementById('bkgEditableAmount').value = total.toFixed(2);
                bar.style.display = 'block';
            } else {
                bar.style.display = 'none';
            }
        } else {
            bar.style.display = 'none';
        }

        var allCbs = document.querySelectorAll('.bkg-row-cb:not(:disabled)');
        var selectAll = document.getElementById('bkgSelectAll');
        if (selectAll) {
            selectAll.checked = allCbs.length > 0 && checked.length === allCbs.length;
            selectAll.indeterminate = checked.length > 0 && checked.length < allCbs.length;
        }
    }

    function clearBkgSelection() {
        var checkboxes = document.querySelectorAll('.bkg-row-cb');
        checkboxes.forEach(function(cb) { cb.checked = false; });
        var selectAll = document.getElementById('bkgSelectAll');
        if (selectAll) { selectAll.checked = false; selectAll.indeterminate = false; }
        updateBkgPaymentSummary();
    }

    function proceedToBookingPayment() {
        if (!<%=paymentCurrencyConfigured%>) {
            alert('<%=paymentCurrencyMsg%>');
            return;
        }
        var checked = document.querySelectorAll('.bkg-row-cb:checked');
        if (checked.length === 0) {
            alert('Please select at least one booking to pay.');
            return;
        }

        var requestedAmount = parseFloat(document.getElementById('bkgEditableAmount').value);
        if (isNaN(requestedAmount) || requestedAmount <= 0) {
            alert('Please enter a valid payment amount.');
            document.getElementById('bkgEditableAmount').focus();
            return;
        }

        var references = [];
        var agencyCode = '';
        var mixedAgency = false;
        checked.forEach(function(cb) {
            references.push({
                refNo: cb.getAttribute('data-bookno'),
                amount: parseFloat(cb.getAttribute('data-amount') || '0'),
                customerType: cb.getAttribute('data-custtype') || 'BP'
            });
            var cbAgency = cb.getAttribute('data-agencycode') || '';
            if (!agencyCode) {
                agencyCode = cbAgency;
            } else if (cbAgency !== agencyCode) {
                mixedAgency = true;
            }
        });

        if (mixedAgency) {
            alert('Selected bookings belong to different agencies. Please select bookings from the same agency only.');
            return;
        }

        var paymentData = {
            paymentType: 'BKG',
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
