<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page contentType="text/html"   pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html" language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List,net.ecom.common.GeneralConf" errorPage="" %>
<%
    String bookno = "";
    String originport = "";
    String deliveryport = "";
    String loadport = "";
    String dischargeport = "";
    String trust_action_no = "";
    String status = "";
    String crdate = "";
    String LRPStatus = "";
    String LRPStatus_new = "";
    String action = "";
    String cruser = "";
    String trColor = "", ecomLrpType = "", statusDesc = "", shotRefNo = "";
    String Subdate = "";
    String print = "";
    String agencyrefno = "";
    ArrayList book = (ArrayList) request.getAttribute("RequestList");
    EbookingViewAll eBooking = null;
    String clientcode = (String) session.getAttribute("clientcode");
    String hideLRPBookingCancel = (String) getServletContext().getAttribute(GeneralConf.HideLRPBookingCancel);
    String agencyid = "";
    String FeusExists = (String) getServletContext().getAttribute("FEUexists");
    String carriercode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String hideAgencyRefno = (String) getServletContext().getAttribute(GeneralConf.HIDE_AGENCYREF_BOOKING_SUMMARY);
    String showdisclaimer = (String) getServletContext().getAttribute(GeneralConf.SHOW_DISCLAIMER_IN_BOOKINGSUMMARY);
    String Enable_ConfirmedBooking_Edit = (String) getServletContext().getAttribute(GeneralConf.Enable_ConfirmedBooking_Edit);
    String Enable_Cancel_Option_Confirmed_BKG = (String) getServletContext().getAttribute(GeneralConf.Enable_Cancel_Option_Confirmed_BKG);
    HashSet<String> disableDraftPrintAgency = (HashSet<String>) getServletContext().getAttribute("disableDraftPrintAgency");
    String enableStripePayment  = (String) getServletContext().getAttribute(GeneralConf.Enable_Stripe_Payment);
    String enableNGeniusPayment = (String) getServletContext().getAttribute(GeneralConf.Enable_Ngenius_Payment);
    boolean isStripeEnabled  = "Y".equalsIgnoreCase(enableStripePayment);
    boolean isNGeniusEnabled = "Y".equalsIgnoreCase(enableNGeniusPayment);
    boolean isPaymentEnabled = isStripeEnabled || isNGeniusEnabled;
    String enableScheduleDisclaimer = (String) getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_DISCLAIMER);
    String scheduleDisclaimerMessage = (String) getServletContext().getAttribute("ScheduleDisclaimerMessage");
%>
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
                <th><bean:message key="EBookingView.action" bundle="<%=clientcode%>"></bean:message></th>
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
                        originport = eBooking.getOriginPortText();
                        deliveryport = eBooking.getFinalPortText();
                        cruser = eBooking.getCruser();
                        crdate = eBooking.getCrdate();
                        Subdate = eBooking.getSubmitdate();
                        ecomLrpType = eBooking.getLrpEcomStatus();
                        String lprBook_no = eBooking.getTrust_book_no();
                        agencyrefno = eBooking.getAgencyrefno();
                        agencyid = eBooking.getAgencyId();
                        if (Subdate != null) {
                            Subdate = eBooking.getSubmitdate();
                        } else {
                            Subdate = "-";
                        }
                        LRPStatus = eBooking.getLrpstatus();
                        shotRefNo = eBooking.getShowExtBookNo();
                        statusDesc = eBooking.getStatusDesc();
                        String haulageTracking = eBooking.getHaulageflag();
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
                    String payEligible  = eBooking.getPaymentEligible();
                    String payCompleted = eBooking.getPaymentCompleted();
                    String payAmt       = eBooking.getPayAmountUsd();
                    String payCustType  = eBooking.getPayCustomerType();
                    String payAgency    = eBooking.getPayAgency();
                    boolean cbEnabled   = "Y".equalsIgnoreCase(payEligible) && !"Y".equalsIgnoreCase(payCompleted);
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
                       class="btn btn-lg text-success" data-toggle="tooltip" title="View" data-placement="right">
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
                <%} else if ("I".equalsIgnoreCase(LRPStatus)) {%>
                <td> <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=trust_action_no%>', '<%=eBooking.getAppVersion()%>')"  class="btn btn-lg text-info" data-toggle="tooltip" title="View" data-placement="right"><i class="fa fa-search"></i></a>
                </td>
                <%} else if ("L".equalsIgnoreCase(LRPStatus)) {%>
                <td>
                    <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=lprBook_no%>', '<%=eBooking.getAppVersion()%>')" title='View'
                       class="btn btn-lg text-info" data-toggle="tooltip" data-placement="right">
                        <i class="fa fa-search"></i></a></td>
                <%} else if ("X".equalsIgnoreCase(LRPStatus)) {%>
                <td>
                    <a href="javascript:void(0)" onclick="bookingView('bookingDetails', '<%=bookno%>', '<%=LRPStatus%>', '<%=ecomLrpType%>', '<%=lprBook_no%>', '<%=eBooking.getAppVersion()%>')" title='View'
                       class="btn btn-lg text-info" data-toggle="tooltip" data-placement="right">
                        <i class="fa fa-search"></i></a>
                </td>
                        <%} //end
                        else {%>

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
                          //  System.out.println("[ECOM-WAR] Requestpage  lprBook_no....." + lprBook_no);
                            bookset.add(lprBook_no);
                        }
                        if (bookno != null && bookno.length() > 0) {
                          //  System.out.println("[ECOM-WAR] Requestpage  bookno........." + bookno);
                            bookset.add(bookno);
                        }
                                }
                            }
                    session.setAttribute("bookingAccessList", bookset);
                }%>
        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#datatablebybooking').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
            <%if (isPaymentEnabled) {%>
            ,"columnDefs": [{ "orderable": false, "targets": 0 }]
            <%}%>
        });
        <%if (isPaymentEnabled) {%>
        clearBkgSelection();
        <%}%>
    });
</script>
