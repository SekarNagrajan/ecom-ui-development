<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="net.ecom.common.utils.MaskPortUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,net.ecom.vo.OBLPrintVo,java.util.Hashtable,net.ecom.common.GeneralConf,net.ecom.common.Validation,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<style>
    /*    .tooltip-inner {
            background-color: #1FBBA6;
            color: #fff;
        }
        .tooltip.left .tooltip-arrow  {
            border-left-color: #1FBBA6;
            color: #fff;
        }
        .label{
            font-size : 100%;
        }
        .label-green {
            background-color: #1FBBA6;
        }*/
    .ready-to-confirm {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        min-width: 180px;
    }
      .dataTable .btn-lg {
      margin-right: 1px !important; 
     }

</style>
<script>
    function editBL(blno, blstatus, bookno, appversion, fcnno)
    {
        if ('C' === blstatus.toUpperCase() && enableBLTermsConditionAccept === 'Y') {
            pendingBLEditBlno = blno;
            pendingBLEditBlstatus = blstatus;
            pendingBLEditBookno = bookno;
            pendingBLEditAppversion = appversion;
            pendingBLEditFcnno = fcnno;
            showBLConfEditTC();
            return;
        }
        proceedBLEdit(blno, blstatus, bookno, appversion, fcnno);
    }

    function proceedBLEdit(blno, blstatus, bookno, appversion, fcnno)
    {
        $.ajax({
            type: "POST",
            cache: false,
            url: "BLAjax?blno=" + blno + "&type=isvoyageclosed",
            data: $(this).serializeArray(),
            success: function (data)
            {
                var isvoyageclosed = data;
                console.log("isvoyageclosed...>>>" + isvoyageclosed);
                if (isvoyageclosed === 'Y')
                {
                    alert('<bean:message key="ecom.msg.bl.isvoyageclosed" />');
                }
            }
        })
        openLoader();
        $('#appversion').val(appversion);
        $('#blno').val(blno);
        $('#bookno').val(bookno);
        $('#fcnno').val(fcnno);
        $('#type').val("edit");
        document.forms[0].action = "BLEditAction.do";
        document.forms[0].submit();
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

    function showBLConfEditTC() {
        $.fancybox.open(blConfEditTCHtml, {
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

    function agreeBLConfEditTC() {
        $.fancybox.close();
        proceedBLEdit(pendingBLEditBlno, pendingBLEditBlstatus, pendingBLEditBookno, pendingBLEditAppversion, pendingBLEditFcnno);
    }
    function callBLView(sino, bkno, blno, appversion) {
        openLoader();
        $('#appversion').val(appversion);
        $('#blno').val(blno);
        $('#bookno').val(bkno);
        $('#sino').val(sino);
        $('#type').val("view");
        document.forms[0].action = "BLEditAction.do";
        document.forms[0].submit();
    }
    function callBLChargeView(blno) {
 $.ajax({
    type: "POST",
    cache: false,
    url: "BLChargeView?blno=" + blno,
    data: $(this).serializeArray(),
    success: function (data) {
//        console.log(data);
        openFancyBox(data); 
    },
    error: function (xhr, status, error) {
        if (xhr.status === 403) {
            window.location.href = "SessionExpired.jsp"; // Redirect to session expired page
        } else {
            console.error("Error: ", error); // Log error to the console
            alert("An error occurred while processing your request."); // Fallback error message
        }
    }
});
    }
    function openFancyBox(data) {
    $.fancybox.open(data, {
        showCloseButton: false,
        closeClick: false,
        maxWidth: 1200,
        maxHeight: 600,
        fitToView: true,
        width: '100%',
        height: '100%',
        autoSize: true,
        scrollOutside: true,
        hideOnOverlayClick: false,
        helpers: {
            overlay: {
                closeClick: false
            }
        },
        keys: {
            close: [27] //close
        },
        afterClose: function () {


        },
        afterShow: function () {
//            $(".fancybox-opened").addClass("modal-draggable");
            //  $(".modal-draggable .fancybox-skin").draggable({
            //       handle: ".fancybox-outer"
            //   });
        }

    });
}

    function callBLAmendment(sino, bkno, blno, appversion) {
        openLoader();
        $('#appversion').val(appversion);
        $('#blno').val(blno);
        $('#bookno').val(bkno);
        $('#sino').val(sino);
        $('#type').val("blamend");
        document.forms[0].action = "BLEditAction.do";
        document.forms[0].submit();
    }

</script>
<% String errormsg = (String) request.getAttribute("OBLEDOCERROR");
    errormsg = errormsg != null ? errormsg : "";
    String clientcode = (String) session.getAttribute("clientcode");
    String dispNonRatedBL = (String) getServletContext().getAttribute(GeneralConf.DisplayNon_RatedBLMsg);
    String dispBLNNprint = (String) getServletContext().getAttribute(GeneralConf.DisplayBL_NNCopypdfprint);
    List EBK_pluginList = (List) getServletContext().getAttribute("EBK_PLUGIN_DETAILS");
    String EnablecallMnfstPrint = (String) getServletContext().getAttribute(GeneralConf.Enable_call_Manifest_Print);
    String hideAgencyRefno = (String) getServletContext().getAttribute(GeneralConf.HIDE_AGENCYREF_BL_SUMMARY);
    String showReadytoConfirm = (String) getServletContext().getAttribute(GeneralConf.SHOW_READYTOCONFIRM);
    String Display_Charge_Summary = (String) getServletContext().getAttribute(GeneralConf.Display_Charge_Summary);
    String Enable_BLAmendStatusMsg = (String) getServletContext().getAttribute(GeneralConf.Enable_BLAmendStatusMsg);
    String Enable_TermsCondition_Accept = (String) getServletContext().getAttribute(GeneralConf.Enable_TermsCondition_Accept);
    String blConfEditTC = (String) getServletContext().getAttribute("BL_ConfEdit_TermsCondition");
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
    System.out.println("[ECOM-WAR] BLSummary Display_Charge_Summary"+Display_Charge_Summary);
   String strActionTableClass = "";
    if("Y".equalsIgnoreCase(showReadytoConfirm)){
        strActionTableClass ="ready-to-confirm";
    } else {
        strActionTableClass = "table-row";
    }
    boolean confirm_edit = false, Issued_edit = false;
    System.out.println("[ECOM-WAR] BLSummary confirm_edit>>>>" + confirm_edit+" confirm_edit>>>>" + Issued_edit);
    if (EBK_pluginList != null && EBK_pluginList.contains("EBC")) {
        confirm_edit = true;
        System.out.println("[ECOM-WAR] BLSummary confirm_edit>>>>" + confirm_edit);
    }
    if (EBK_pluginList != null && EBK_pluginList.contains("EBI")) {
        Issued_edit = true;
        System.out.println("[ECOM-WAR] BLSummary Issued_edit>>>>" + Issued_edit);
    }

%>
<script type="text/javascript">
    var enableBLTermsConditionAccept = '<%=Enable_TermsCondition_Accept%>';
    var blConfEditTCHtml = "<div id='blConfEditTCDiv'><style>#blConfEditTCDiv h4{margin-top:0;} #blConfEditTCDiv.fancybox-content{padding:0px !important;width:70% !important;max-width:70vw !important;}</style><div class='w-100 h-100' ><div class='card-header page_header' style='margin-top:0px;margin-right:0px;'><img class='header_icon_pages' src='Design/assets/images/page_icons/schdule.png'> BL Correction - Terms and Conditions</div><div style='max-height: calc(100vh - 200px);overflow-y: auto;padding:15px;'><%=blConfEditTC != null ? blConfEditTC.replace("\"", "'").replace("\n", "").replace("\r", "") : ""%></div><div class='text-center'><a href='javascript:;' class='btn btn-info btn-lg m-t-5 m-b-5 text-white' style='width: 200px;' onclick='agreeBLConfEditTC()'>I Agree</a></div></div></div>";
    var pendingBLEditBlno = '', pendingBLEditBlstatus = '', pendingBLEditBookno = '', pendingBLEditAppversion = '', pendingBLEditFcnno = '';
</script>
<%if (errormsg != null && errormsg.length() > 0) {%>
<script type="text/javascript">
    alert('<bean:message key="ecom.msg.blopr" bundle="<%=clientcode%>"/>');</script>
    <%}%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.billoflading" bundle="<%=clientcode%>"/></div>
<div class="details_search page_bottom_content">
    <%if (isPaymentEnabled) {
        int blJsNo = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    %>
    <%if (isStripeEnabled) {%>
    <script type="text/javascript" src="js/stripe-payment-util.js?<%=blJsNo%>"></script>
    <script type="text/javascript">
        var csrfToken = '<%= session.getAttribute("STRIPE_CSRF_TOKEN") %>';
        if (typeof StripePaymentUtil !== 'undefined') { StripePaymentUtil.initializeCSRFToken(csrfToken); }
    </script>
    <%}%>
    <%if (isNGeniusEnabled) {%>
    <script type="text/javascript" src="js/ngenius-payment-util.js?<%=blJsNo%>"></script>
    <script type="text/javascript">
        var ngeniusCsrfToken = '<%= session.getAttribute("NGENIUS_CSRF_TOKEN") %>';
        if (typeof NGeniusPaymentUtil !== 'undefined') { NGeniusPaymentUtil.initializeCSRFToken(ngeniusCsrfToken); }
    </script>
    <%}%>
    <div id="blPaymentBar" class="ecom-pay-bar">
        <div class="ecom-pay-content">
            <div class="ecom-pay-info">
                <div class="ecom-pay-count"><span id="blSelectedCount">0</span> BLs selected</div>
                <div class="ecom-pay-total">Total: <span id="blTotalAmount"><%=paymentCurrencyConfigured ? paymentCurrency + " 0.00" : paymentCurrencyMsg%></span></div>
            </div>
            <div class="ecom-pay-actions">
                <input type="number" id="blEditableAmount" class="ecom-pay-amount" step="0.01" min="0" value="0.00" placeholder="Amount">
                <button type="button" class="ecom-pay-btn-clear" onclick="clearBlSelection()">Clear</button>
                <button type="button" class="ecom-pay-btn-pay" onclick="proceedToBlPayment()"><i class="fa fa-credit-card"></i> Pay Selected</button>
            </div>
        </div>
    </div>
    <%}%>
    <form name="blsummary" method="post"  id="blsummary" >
        <div class="table-responsive card card-body card-shadow ">
            <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
            <table class="table table-striped table-bordered" id="datatablebybooking">
                <thead>
                    <tr>
                        <%if (isPaymentEnabled) {%>
                        <th style="width:30px;text-align:center"><input type="checkbox" class="ecom-pay-checkbox" id="blSelectAll" onclick="toggleAllBlCheckboxes(this)"></th>
                        <%}%>
                        <%if ("Y".equalsIgnoreCase(showReadytoConfirm)) {%>
                        <th ><bean:message key="Obl.action" bundle="<%=clientcode%>"/></th>
                         <%} else {%>
                        <th><bean:message key="Obl.action" bundle="<%=clientcode%>"/></th>
                        <%}%>
                        <th><bean:message key="Obl.status" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.blnumber" bundle="<%=clientcode%>"/></th>
                        <th>MCN No</th>
                        <th><bean:message key="Obl.bookno" bundle="<%=clientcode%>"/></th>
                            <%if (!"Y".equalsIgnoreCase(hideAgencyRefno)) {%>
                        <th><bean:message key="ebooking.agency.reference" bundle="<%=clientcode%>"/></th>
                            <%}%>
                        <th><bean:message key="Tracking.origin" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.load" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.discharge" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.confirmeddate" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="EBookingView.crdate" bundle="<%=clientcode%>" ></bean:message></th>


                        </tr>
                    </thead>
                    <tbody>
                    <%
                        String cust_oblsts = (String) session.getAttribute("cust_oblStauts");
                        String printaccess = (String) session.getAttribute("OBLPRINTACCESS");
                        String userCode = (String) session.getAttribute("SESSION_USERCODE");
                        ArrayList book = (ArrayList) session.getAttribute("oblprintView");
                        System.out.println("[ECOM-WAR] BLSummary cust_oblsts>>>" + cust_oblsts);
                        System.out.println("[ECOM-WAR] BLSummary printaccess>>>>" + printaccess);
                        Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                        String status = "", label_status = "", originalcp = "", draftcp = "", agencydocid = "", refval = "", islocked = "", blno = "", sino = "";
                        if (book != null && book.size() > 0) {
                            int size = book.size();
                            Set blSet = (Set) session.getAttribute("blAccessList");
                            if (blSet == null) {
                                blSet = new HashSet();
                            }
                            for (int i = 0; i < size; i++) {
                                OBLPrintVo eBooking = (OBLPrintVo) book.get(i);
                                status = eBooking.getStatus() != null ? eBooking.getStatus() : "";
                                originalcp = eBooking.getOriginals() != null ? eBooking.getOriginals() : "";
                                draftcp = eBooking.getCopies() != null ? eBooking.getCopies() : "";
                                refval = eBooking.getBlAcceptByUserStatus();
                                islocked = eBooking.getIssilocked();
                                agencydocid = eBooking.getDocumentalagencyid() != null ? eBooking.getDocumentalagencyid() : "";
                                if (status.equalsIgnoreCase("C")) {
                                label_status = "<span class='label label-success'>Confirmed</span>";
                                } else if (status.equalsIgnoreCase("I")) {
                                    label_status = "<span class='label label-warning'>Issued</span>";
                                } else if (status.equalsIgnoreCase("D")) {
                                    label_status = "<span class='label label-info'>Draft</span>";
                                } else if (status.equalsIgnoreCase("A")) {
                                    label_status = "<span class='label label-green'>Accepted</span>";
                                } else if (status.equalsIgnoreCase("R")) {
                                    label_status = "<span class='label label-danger'>Rejected</span>";
                                } else if (status.equalsIgnoreCase("InComplete")) {
                                    label_status = "<span class='label label-danger'>InComplete</span>";
                                } else if (status.equalsIgnoreCase("S")) {
                                    if("Y".equalsIgnoreCase(Enable_BLAmendStatusMsg)){
                                     label_status = "<span class='label label-danger'>Amendment Submitted</span>";
                                    }else{
                                         label_status = "<span class='label label-danger'>Send Correction</span>";
                                    }
                                }
                                if (status.equalsIgnoreCase("D") && "Y".equalsIgnoreCase(refval)) {
                                    label_status = "<span class='label label-green'>Accepted</span>";
                                }
                    %>
                    <tr>
                        <%if (isPaymentEnabled) {
                            String blPayEligible = eBooking.getPaymentEligible();
                            String blPayCompleted = eBooking.getPaymentCompleted();
                            String blPayAmt = eBooking.getPayAmountUsd();
                            String blPayCustType = eBooking.getPayCustomerType();
                            String blPayAgency = eBooking.getPay_agency();
                            boolean blCbEnabled = "Y".equalsIgnoreCase(blPayEligible) && !"Y".equalsIgnoreCase(blPayCompleted);
                        %>
                        <td style="text-align:center">
                            <%if ("Y".equalsIgnoreCase(blPayCompleted)) {%>
                                <span data-toggle="tooltip" title="Payment already completed"><input type="checkbox" class="ecom-pay-checkbox" disabled></span>
                            <%} else if (!blCbEnabled) {%>
                                <span data-toggle="tooltip" title="Not eligible for payment"><input type="checkbox" class="ecom-pay-checkbox" disabled></span>
                            <%} else {%>
                                <input type="checkbox" class="ecom-pay-checkbox bl-row-cb"
                                       data-blno="<%=eBooking.getBookNo()%>"
                                       data-amount="<%=blPayAmt%>"
                                       data-custtype="<%=blPayCustType%>"
                                       data-agencycode="<%=blPayAgency != null ? blPayAgency : ""%>"
                                       onchange="updateBlPaymentSummary()">
                            <%}%>
                        </td>
                        <%}%>

                        <td class="<%=strActionTableClass%>">
                            <%if ("Y".equalsIgnoreCase(islocked)) {
                                    label_status = "<span class='label label-danger'>Locked</span>";
                            %>
                            <a class="btn btn-lg text-danger" href="#" data-toggle="tooltip" title="Locked" data-placement="left"><i class="fa fa-lock"></i></a>
                                <%
                                } else {
                                    if ("C".equalsIgnoreCase(eBooking.getStatus().trim())) {
                                %>

                            <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'D', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Draft" data-placement="left" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a> 
                                <%if (confirm_edit) {%>
                            |<a onClick="editBL('<%=eBooking.getBookNo()%>', '<%=eBooking.getStatus()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getAppVersion()%>', '<%=Validation.checkNull(eBooking.getFcnverno())%>')"  data-toggle="tooltip" title="Edit" data-placement="left" class="btn btn-lg text-warning" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-pencil-square-o"></i></a>
                                <%}%>
                            <%if ("Y".equalsIgnoreCase(Display_Charge_Summary)) {%>
                            | <a onClick="callBLChargeView('<%=eBooking.getBookNo()%>')"  data-toggle="tooltip" title="Charge Summary" data-placement="left" class="btn btn-lg blue-button" data-fancybox-type="iframe" href="javascript:void(0)" style="color: #003da5;" ><i class="fa fa-list-alt"></i></a> 
                                <%}%>
                                <%}%>
                                <% if ("C".equalsIgnoreCase(eBooking.getStatus().trim()) && "Y".equalsIgnoreCase(dispBLNNprint)) {%> 


                            | <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'C', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Non Negotiable" data-placement="left" class="fancybox1 btn btn-lg text-warning" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a>
                            <%}%>
                            <% if ("C".equalsIgnoreCase(eBooking.getStatus().trim()) && "Y".equalsIgnoreCase(eBooking.getPrintstatus().trim()) && "Y".equalsIgnoreCase(cust_oblsts)) {%> <!-- removed print access condition -> && "YES".equalsIgnoreCase(printaccess)-->
                            |  <a href="javascript:void(0)" onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'O', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Original" data-placement="left" class="btn btn-lg text-success" data-fancybox-type="iframe"><i class="fa fa-print"></i></a>
                            <%
                            } else if ("I".equalsIgnoreCase(eBooking.getStatus().trim())) {
                            %>
                            <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'I', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Non Negotiable" data-placement="left" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a> | <a class="btn btn-lg text-danger"  href="#" data-toggle="tooltip" title="Issued" data-placement="left"><i class="fa fa-lock"></i></a>
                                <%
                                    if ("Y".equalsIgnoreCase(eBooking.getIsinsurance())) {%>
                            | <a class="btn btn-lg text-info" href="javascript:void(0)" data-toggle="tooltip" title="<%=eBooking.getPolicyno()%>" data-placement="left" onclick="/*viewpolicy('<=eBooking.getPolicyno()*/ % > ')"><i class="fa fa-shield"></i></a>
                                <%}%>
                            <%if ("Y".equalsIgnoreCase(Display_Charge_Summary)) {%>
                            | <a onClick="callBLChargeView('<%=eBooking.getBookNo()%>')"  data-toggle="tooltip" title="Charge Summary" data-placement="left" class="btn btn-lg blue-button" data-fancybox-type="iframe" href="javascript:void(0)" style="color: #003da5;" ><i class="fa fa-list-alt"></i></a> 
                                <%}%>
                                <%         } else if ("S".equalsIgnoreCase(eBooking.getStatus().trim())) {
                                %>
                            <a href="javascript:void(0)" onClick="callBLView('<%=eBooking.getEcomsino()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getBookNo()%>', '<%=eBooking.getAppVersion()%>')" data-toggle="tooltip" title="View" data-placement="left" class="fancybox1 btn btn-lg text-info" data-fancybox-type="iframe"><i class="fa fa-search"></i></a>
                            <a onClick="callBLAmendment('<%=eBooking.getEcomsino()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getBookNo()%>', '<%=eBooking.getAppVersion()%>')"  data-toggle="tooltip" title="Amendment" data-placement="left" class="btn btn-lg text-warning" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-pencil-square-o"></i></a>
                            <a href="javascript:void(0)" onclick="cancelSubmitedBL('<%=eBooking.getEcomsino()%>', '<%=eBooking.getAppVersion()%>')" title="Cancel" class="btn btn-lg text-danger" data-toggle="tooltip"  data-placement="left"><i class="fa fa-times"></i></a>
                               <%if ("Y".equalsIgnoreCase(Display_Charge_Summary)) {%>
                            | <a onClick="callBLChargeView('<%=eBooking.getBookNo()%>')"  data-toggle="tooltip" title="Charge Summary" data-placement="left" class="btn btn-lg blue-button" data-fancybox-type="iframe" href="javascript:void(0)" style="color: #003da5;" ><i class="fa fa-list-alt"></i></a> 
                                <%}%>
                            <%} else if ("D".equalsIgnoreCase(eBooking.getStatus().trim()) && "Y".equalsIgnoreCase(refval)) {%>
                            <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getStatus()%>', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Draft" data-placement="left" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a>
                            <%if ("Y".equalsIgnoreCase(Display_Charge_Summary)) {%>
                            | <a onClick="callBLChargeView('<%=eBooking.getBookNo()%>')"  data-toggle="tooltip" title="Charge Summary" data-placement="left" class="btn btn-lg blue-button" data-fancybox-type="iframe" href="javascript:void(0)" style="color: #003da5;" ><i class="fa fa-list-alt"></i></a> 
                                <%}%>   
                            <%
                                } else if ("D".equalsIgnoreCase(eBooking.getStatus().trim())) {%>
                            <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getStatus()%>', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Draft" data-placement="left" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a> 
                            |<a onClick="editBL('<%=eBooking.getBookNo()%>', '<%=eBooking.getStatus()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getAppVersion()%>', '<%=Validation.checkNull(eBooking.getFcnverno())%>')"  data-toggle="tooltip" title="Edit" data-placement="left" class="btn btn-lg text-warning" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-pencil-square-o"></i></a>
                                <%if (!"1".equalsIgnoreCase(eBooking.getAppVersion())) {%>
                                <%if ("Y".equalsIgnoreCase(showReadytoConfirm)) {%>
                            |<a onClick="acceptBLByUser('<%=eBooking.getBookNo()%>', '<%=agencydocid%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getAgencyRefNo()%>', '<%=eBooking.getOriginPortId() + " - " + portlist.get(eBooking.getOriginPortId())%>', '<%=eBooking.getFinalPortId() + " - " + portlist.get(eBooking.getFinalPortId())%>')"  data-toggle="tooltip" title="Ready to Confirm" data-placement="left" class="btn btn-lg text-success" data-fancybox-type="iframe" href="javascript:void(0)" style=" display: block; color: #26c650;">Ready to Confirm</a>
                            <%} else {%>
                            |<a onClick="acceptBLByUser('<%=eBooking.getBookNo()%>', '<%=agencydocid%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getAgencyRefNo()%>', '<%=eBooking.getOriginPortId() + " - " + portlist.get(eBooking.getOriginPortId())%>', '<%=eBooking.getFinalPortId() + " - " + portlist.get(eBooking.getFinalPortId())%>')"  data-toggle="tooltip" title="Accept" data-placement="left" class="btn btn-lg text-success" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-check"></i></a>
                                <%}
                                    }%>
                             <%if ("Y".equalsIgnoreCase(Display_Charge_Summary)) {%>
                             | <a onClick="callBLChargeView('<%=eBooking.getBookNo()%>')"  data-toggle="tooltip" title="Charge Summary" data-placement="left" class="btn btn-lg blue-button" data-fancybox-type="iframe" href="javascript:void(0)" style="color:#003da5 ;" ><i class="fa fa-list-alt"></i></a> 
                                <%}%>
                                <%
                                } else {
                                %>

                             <%}%>
                            <%if ("Y".equalsIgnoreCase(EnablecallMnfstPrint) && (!"S".equalsIgnoreCase(status))) {%>
                            <a onClick="manifestPrintOptions('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>')"  data-toggle="tooltip" title="Manifest" data-placement="left" class="fancybox1 btn btn-lg text-success" data-fancybox-type="iframe" href="javascript:void(0)"  ><i class="fa fa-file-pdf-o"></i></a> 
                                <%}%>
                                <%}%>
                             
                        </td>
                        <td><%=label_status%></td>
                        <td><%=eBooking.getBookNo()%></td>
                        <td><%=Validation.checkNull(eBooking.getFcnverno())%></td>
                        <td><%=eBooking.getBook_no()%></td>
                        <%if (!"Y".equalsIgnoreCase(hideAgencyRefno)) {%>
                        <td><%=eBooking.getAgencyRefNo()%></td>
                        <%}%>
                        <td><%=MaskPortUtil.swapActualPortToMaskPortWithName(eBooking.getOriginPortId() + " - " + portlist.get(eBooking.getOriginPortId()))%></td>
                        <td><%=MaskPortUtil.swapActualPortToMaskPortWithName(eBooking.getLoadPortID() + " - " + portlist.get(eBooking.getLoadPortID()))%></td>
                        <td><%=MaskPortUtil.swapActualPortToMaskPortWithName(eBooking.getDischargePortId() + " - " + portlist.get(eBooking.getDischargePortId()))%></td>
                        <td><%=MaskPortUtil.swapActualPortToMaskPortWithName(eBooking.getFinalPortId() + " - " + portlist.get(eBooking.getFinalPortId()))%></td>
                        <td><%=eBooking.getConfirmedDate()%></td>
                        <td><%=eBooking.getCrdate()%></td>

                    </tr>
                    <%

                                blno = eBooking.getBookNo();
                                sino = eBooking.getEcomsino();
                                if (blno != null && blno != "") {
                                    blSet.add(blno);
                                }
                                if (sino != null && sino != "") {
                                    blSet.add(sino);
                                }

                            } //end for loop
                            session.setAttribute("blAccessList", blSet);
                        }

                    %>
                </tbody>
            </table>
        </div>
        <input type="hidden" id="usercode" name="usercode" value="<%=userCode%>">
        <input type="hidden" id="appversion" name="appversion">
        <input type="hidden" id="blno" name="blno">       
        <input type="hidden" id="bookno" name="bookno">  
        <input type="hidden" id="sino" name="sino">
        <input type="hidden" id="fcnno" name="fcnno">
        <input type="hidden" id="type" name="type"> 
        <input type="hidden" id="bl_no" name="bl_no"> 
        <input type="hidden" id="agency_Code" name="agency_Code"> 
    </form>
</div>
<!-- manifestPrint dialog-->
<div class="modal fade" id="manifestprint" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title">Manifest Print</h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body" style="padding: 19px 11px 20px 10px;">
                <div class="form-group-row  text-center">
                    <input type="radio" id="cargoCheckbox" name="cargoCheckbox" value="C" style="margin-left:2%;height: 20px;width: 20px;" /> &nbsp; 
                    <label class=" control-label">
                        <span for="cargoCheckbox" style="padding-right: 20px;">Cargo</span>
                    </label>
                    <input type="radio" id="freightCheckbox" name="cargoCheckbox" value="F" style="margin-left:2%;height: 20px;width: 20px;" />  &nbsp;
                    <label class="  control-label">
                        <span for="freightCheckbox">Freight</span>&nbsp;
                    </label>
                    <div class=" control-label text-center"><a href="javascript:void(0)" title="Print" class="btn btn-info btn-xs" onclick="manifestClose()" id="mailbtn" style="width: 170px;"> Print</a></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End manifestPrint dialog-->

<div class="modal fade" id="myModal" role="dialog" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content" style="width:1132px;heigth:900px;margin-left: 7%;">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"><bean:message key="bl.head" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" onclick="refreshobldata()" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div  id="oblprintmodal" style="padding-left:2%;padding-right:2%">
            </div>

        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<%if ("Y".equalsIgnoreCase(dispNonRatedBL)) {%>

<div class="BLNotecontent text-danger"><bean:message key="ecom.bkg.filenote" bundle="<%=clientcode%>"/>: <bean:message key="ecom.bl.notecontent" bundle="<%=clientcode%>"/></div>
<%}%>
<div id="BLDialog">
</div>
<script type="text/javascript">
    //Our XmlHttpRequest object to get the auto suggest
    var searchReq = getXmlHttpRequestObject();
    function getXmlHttpRequestObject() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert("<bean:message key="ecom.msg.browsernotsupported" bundle="<%=clientcode%>"/>");
        }
    }
    function refreshobldata()
    {
        location.reload();
    }
    $(document).ready(function () {
        // $('[data-toggle="tooltip"]').tooltip();
        $('#datatablebybooking').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
            <%if (isPaymentEnabled) {%>
            ,"columnDefs": [{ "orderable": false, "targets": 0 }]
            <%}%>
        });
        $(".table-row").each(function () {
            var appVersion = $(this).data("app-version").toString().toLowerCase();
            var showReadyToConfirm = $(this).data("show-ready-to-confirm");
            if (appVersion !== "1" && showReadyToConfirm === "Y") {
                $(this).addClass("ready-to-confirm");
            }
        });
    });</script>
<script type="text/javascript">
    var status;
    var URLStr;
    var type;
    var originals;
    var copies;
    var popUpWin = 0, tblno, blstatus, varprintcopies, varblno, shipmentstatus, bookno, sts, tbltype, toriginalcp, tdraftcp, tblstatus, tagencydocid, tissueagency;
    function callbl(blno, agencycode, shipstatus, book_no, bltype, originalcp, draftcp, blstatus, agencydocid, appversion, shipmentStatus, shipmentdate, fcnno) {
        if (appversion != '1') {
            tblno = blno;
            varblno = blno;
            blstatus = blstatus;
            shipmentstatus = shipstatus;
            bookno = book_no;
            sts = blstatus;
            tbltype = bltype;
            toriginalcp = originalcp;
            tdraftcp = draftcp;
            tblstatus = blstatus;
            tagencydocid = agencydocid;
            tissueagency = agencycode;
            openLoader();
            var printDetails = '[{"blno":"' + tblno + '","type":"' + tbltype + '","originalcp":"' + toriginalcp + '","draftcp":"' + tdraftcp + '","blstatus":"' + tblstatus + '","agencydocid":"' + tagencydocid + '","issueagency":"' + tissueagency + '","shipmentStatus":"' + shipmentStatus + '","shipmentdate":"' + shipmentdate + '","fcnno":"' + fcnno + '"}]';
            var url = "OblPrintAction.do?blprintdetails=" + printDetails;
            var params = "blno=" + tblno + "&type=" + tbltype + "&originalcp=" + toriginalcp + "&draftcp=" + tdraftcp + "&blstatus=" + tblstatus + "&agencydocid=" + tagencydocid + "&issueagency=" + tissueagency + "&shipmentStatus=" + shipmentStatus + "&shipmentdate=" + shipmentdate + "&fcnno=" + fcnno;
            searchReq.open("POST", url, true);
            searchReq.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            searchReq.onreadystatechange = setOblPrintDialog;
            searchReq.send(params);
        } else {
            callV1DraftBL(blno, agencycode, shipstatus, book_no, bltype, originalcp, draftcp, blstatus, agencydocid, appversion, shipmentStatus);
            //alert('This option is unavailable due to system upgrade. Kindly collect the details from your local agency.');
        }
    }
    function manifestPrintOptions(blNo, agencyCode) {
        $('input[name="cargoCheckbox"]').prop('checked', false);
        $('#bl_no').val(blNo);
        $('#agency_Code').val(agencyCode);
        $("#manifestprint").modal("show");
    }
    function manifestClose() {
        var cargoSelected = document.querySelector('input[name="cargoCheckbox"]:checked');
        console.log("cargoSelected=>" + cargoSelected);
        var blNo = $('#bl_no').val();
        var agencyCode = $('#agency_Code').val();
        console.log(blNo + "agencyCode=>" + agencyCode);
        if (!cargoSelected) {
            alert("Please select at least one manifest type");
        } else {
            tblno = blNo;
            var manifestype = document.querySelector('input[name="cargoCheckbox"]:checked').value;
            $("#manifestprint").modal("hide");
            openLoader();
            var url = "ManifestPrintAction.do?blno=" + blNo + "&issueagency=" + agencyCode + "&manifestype=" + manifestype;
            var params = "blno=" + blNo;
            searchReq.open("POST", url, true);
            console.log("ManifestPrintAction_do" + manifestype);
            searchReq.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            searchReq.onreadystatechange = setOblPrintDialog;
            searchReq.send(params);
        }
    }

    function setOblPrintDialog() {
        if (searchReq.readyState == 4 && searchReq.status == 200) {
            document.getElementById("BLDialog").innerHTML = searchReq.responseText;
            if (document.getElementById("oblmsgres").value == "fail")
            {
                closeLoader();
                //alert("BL Signature is not available for " + agencycode + " agency");
                alert('<bean:message key="ecom.msg.e-signmissing" bundle="<%=clientcode%>"/>');
                return false
            } else if (document.getElementById("oblmsgres").value == "Success") {
                closeLoader();
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
//                var type = document.getElementById("prinType").value;
                document.getElementById("blsummary").action = "BLOriginalPrint?blno=" + tblno + "&responseType=" + responseType + "&viewtype=" + viewtype + "&printype=oblprint";
                document.getElementById("blsummary").submit();
            } else
            {
                closeLoader();
                alert(document.getElementById("oblmsgres").value);
                return false;
            }
        } else if (searchReq.readyState == 4 && searchReq.status == 403) {
            window.location.href = "SessionExpired.jsp";
        }
    }
    function callV1DraftBL(blno, agencycode, shipstatus, book_no, bltype, originalcp, draftcp, blstatus, agencydocid, appversion, shipmentStatus) {
        var blprinttype;
        if (bltype == 'D') {
            blprinttype = 'bldraft';
        } else if (bltype == 'I') {
            blprinttype = 'NN';
        }
        if (bltype == 'D' || bltype == 'I') {
            openLoader();
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLV1DraftJasper?blno=" + blno + "&type=" + blprinttype + "&usercode=" + $('#usercode').val(),
                data: $(this).serializeArray(),
                success: function (data) {
                    closeLoader();
                    if (data != null) {
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
                        return false;
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else if (bltype == 'O') {
            $.ajax({
                type: "POST",
                cache: false,
                async: false,
                url: "BLAjax?book_no=" + book_no + "&type=v1signaturecheck&agencycode=" + agencycode + "&shipmentstatus=" + shipmentStatus,
                data: $(this).serializeArray(),
                success: function (data) {
                    var response = data;
                    response = response.split("~~");
                    if (response.length > 1) {
                        if (response[0] == 'No') {
                            alert('<bean:message key="ecom.msg.e-signmissing" bundle="<%=clientcode%>"/>');
                            return false;
                        }
                        if (response[1] == 0) {
                            openLoader();
                            document.getElementById("blsummary").action = "BLPrintController?type=Original" + "&blno=" + blno + "&originals=" + originalcp;
                            document.getElementById("blsummary").submit();
                        } else {
                            alert('<bean:message key="ecom.msg.blopracc" bundle="<%=clientcode%>"/>');
                            return false;
                        }
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }

    }
    function callviewbl(blno, bltype, originalcp, draftcp, blstatus, agencydocid, appversion) {
        if (appversion != '1') {
            openLoader();
            /**if (blstatus == 'C' || blstatus == 'D') {
             blstatus = "view";
             } else if (blstatus == 'I') {
             blstatus = "copies";
             }**/
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLJasper?blno=" + blno + "&type=" + bltype + "&originalcp=" + originalcp + "&draftcp=" + draftcp + "&blstatus=" + blstatus + "&agencydocid=" + agencydocid,
                data: $(this).serializeArray(),
                success: function (data) {
                    if ('Success' == data)
                    {
                        closeLoader();
                        $.fancybox.open({
                            maxWidth: 900,
                            maxHeight: 800,
                            fitToView: false,
                            width: '100%',
                            height: '100%',
                            autoSize: false,
                            scrollOutside: true,
                            src: "ViewPDF.jsp",
                            closeClick: false,
                            type: 'iframe',
                            overlayShow: false,
                            iframe: {
                                preload: false // fixes issue with iframe and IE
                            }
                        });
                    } else {
                        closeLoader();
                        alert(data);
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else {

            alert('<bean:message key="ecom.msg.sysupgrade" bundle="<%=clientcode%>"/>');
        }
    }

    function cancelSubmitedBL(sino, appversion) {
        var cancel = confirm("<bean:message key="ecom.msg.cancelbl" bundle="<%=clientcode%>"/>");
        if (cancel) {
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLAjax?type=cancelbl&sino=" + encodeURIComponent(sino) + "&appversion=" + encodeURIComponent(appversion),
                data: $(this).serializeArray(),
                success: function (data) {
                    if (data == 'Processed') {
                        alert("<bean:message key="ecom.msg.blcorrectionprocessed" bundle="<%=clientcode%>"/>");
                    } else {
                        openLoader();
                        location.href = "ViewBookingAll.do?BookingRequestType=oblprint";
                    }
                },
                error: function (data, xhr) {
                    closeLoader();
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    //$("#mgtemp").html("Manage Template");
                }
            });
        }
    }
    function acceptBLByUser(blno, agencycode, bookno, appversion, agencyrefno, origin, delivery) {
        var cancel = confirm("<bean:message key="ecom.msg.cnfrmbl" bundle="<%=clientcode%>"/>");
        if (cancel) {
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLAjax?type=acceptbl&blno=" + encodeURIComponent(blno) + "&agencycode=" + agencycode + "&bookno=" + bookno + "&agencyrefno=" + agencyrefno + "&origin=" + origin + "&delivery=" + delivery,
                data: $(this).serializeArray(),
                success: function (data) {
                    alert("<bean:message key="bl.msg.blaccept" bundle="<%=clientcode%>"/>");
                    openLoader();
                    location.href = "ViewBookingAll.do?BookingRequestType=oblprint";
                },
                error: function (data, xhr) {
                    closeLoader();
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    //$("#mgtemp").html("Manage Template");
                }
            });
        }
    }

    <%if (isPaymentEnabled) {%>
    function toggleAllBlCheckboxes(source) {
        var checkboxes = document.querySelectorAll('.bl-row-cb:not(:disabled)');
        checkboxes.forEach(function(cb) { cb.checked = source.checked; });
        updateBlPaymentSummary();
    }

    function updateBlPaymentSummary() {
        var checked = Array.prototype.slice.call(document.querySelectorAll('.bl-row-cb:checked'));
        var bar = document.getElementById('blPaymentBar');
        var total = 0.00;

        if (checked.length > 0) {
            var firstAgency = checked[0].getAttribute('data-agencycode') || '';
            var mixedAgency = checked.some(function(cb) {
                return (cb.getAttribute('data-agencycode') || '') !== firstAgency;
            });

            if (mixedAgency) {
                alert('Selected BLs belong to different agencies. Please select BLs from the same agency only.');
                checked.forEach(function(cb) {
                    if ((cb.getAttribute('data-agencycode') || '') !== firstAgency) {
                        cb.checked = false;
                    }
                });
                checked = Array.prototype.slice.call(document.querySelectorAll('.bl-row-cb:checked'));
            }

            if (checked.length > 0) {
                checked.forEach(function(cb) {
                    total += parseFloat(cb.getAttribute('data-amount') || '0');
                });
                document.getElementById('blSelectedCount').textContent = checked.length;
                if (<%=paymentCurrencyConfigured%>) {
                    document.getElementById('blTotalAmount').textContent = '<%=paymentCurrency%> ' + total.toFixed(2);
                } else {
                    document.getElementById('blTotalAmount').textContent = '<%=paymentCurrencyMsg%>';
                }
                document.getElementById('blEditableAmount').value = total.toFixed(2);
                bar.style.display = 'block';
            } else {
                bar.style.display = 'none';
            }
        } else {
            bar.style.display = 'none';
        }

        var allCbs = document.querySelectorAll('.bl-row-cb:not(:disabled)');
        var selectAll = document.getElementById('blSelectAll');
        if (selectAll) {
            selectAll.checked = allCbs.length > 0 && checked.length === allCbs.length;
            selectAll.indeterminate = checked.length > 0 && checked.length < allCbs.length;
        }
    }

    function clearBlSelection() {
        var checkboxes = document.querySelectorAll('.bl-row-cb');
        checkboxes.forEach(function(cb) { cb.checked = false; });
        var selectAll = document.getElementById('blSelectAll');
        if (selectAll) { selectAll.checked = false; selectAll.indeterminate = false; }
        updateBlPaymentSummary();
    }

    function proceedToBlPayment() {
        if (!<%=paymentCurrencyConfigured%>) {
            alert('<%=paymentCurrencyMsg%>');
            return;
        }
        var checked = document.querySelectorAll('.bl-row-cb:checked');
        if (checked.length === 0) {
            alert('Please select at least one BL to pay.');
            return;
        }

        var requestedAmount = parseFloat(document.getElementById('blEditableAmount').value);
        if (isNaN(requestedAmount) || requestedAmount <= 0) {
            alert('Please enter a valid payment amount.');
            document.getElementById('blEditableAmount').focus();
            return;
        }

        var references = [];
        var agencyCode = '';
        var mixedAgency = false;
        checked.forEach(function(cb) {
            references.push({
                refNo: cb.getAttribute('data-blno'),
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
            alert('Selected BLs belong to different agencies. Please select BLs from the same agency only.');
            return;
        }

        var paymentData = {
            paymentType: 'BL',
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

    function viewpolicy(policyno) {
        openLoader();
        $.ajax({
            type: "POST",
            cache: false,
            url: "InsuranceAjax?type=getpolicy&policyno=" + policyno,
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                closeLoader();
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
</script>

