<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,net.ecom.phase2.service.vo.PaymentHistoryVO,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat,net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
        String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
        String fromDate = Validation.checkNull((String) request.getAttribute("phFromDate"));
        String toDate = Validation.checkNull((String) request.getAttribute("phToDate"));
        String clientcode = (String) session.getAttribute("clientcode");
        List<PaymentHistoryVO> paymentHistoryList = (List<PaymentHistoryVO>) request.getAttribute("paymentHistoryList");
        String phMaxDays = Validation.checkNull((String) session.getServletContext().getAttribute(GeneralConf.PAYMENT_hISTORY_DATE_INTERVAL));
        if (phMaxDays.length() <= 0) {
            phMaxDays = "0";
        }
%>
<div class="card-header page_header fixed-top">
    <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />
    <bean:message key="ecom.paymenthistory" bundle="<%=clientcode%>"/>
</div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="details_search">
            <div class="container">
                <form id="paymentHistoryForm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 col-sm-3">
                                <input type="text" name="frdate" id="frdate" class="form-control inside_input required" placeholder="<bean:message key="ecom.ph.paymentdatefrom" bundle="<%=clientcode%>"/>" autocomplete="off" />
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <input type="text" name="todate" id="todate" class="form-control inside_input required" placeholder="<bean:message key="ecom.ph.paymentdateto" bundle="<%=clientcode%>"/>" autocomplete="off" />
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <button type="submit" class="btn btn-info btn_block_custom" autocomplete="off" id="searchID">
                                    <bean:message key="ecom.Search" bundle="<%=clientcode%>"/>
                                </button>
                            </div>
                            <div class="col-xs-12 col-sm-3 text-right">
                                <a class="btn btn-md btn-success text-white" onclick="callpdf();" href="#"><i class="fa fa-file-pdf-o" aria-hidden="true"></i> <bean:message key='ecom.cs.generatepdf' bundle="<%=clientcode%>"/></a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div id="paymentHistoryTableDiv" class="card card-shadow">
    <div class="table-responsive card-body">
        <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
        <table class="table table-striped table-bordered" id="paymentHistoryDataTable">
            <thead>
                <tr>
                    <th><bean:message key="ecom.ph.bankname" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.bankcode" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.currency" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.accountno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.referenceno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.statementdate" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.customername" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.customercode" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.customeraccountno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.checkno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.checkamount" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.checkallocatedamt" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.unallocatedamt" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.transactionno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.transactiondate" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.blno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.mcnno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.blamount" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.blallocatedamt" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.ph.blbalanceamt" bundle="<%=clientcode%>"/></th>
                </tr>
            </thead>
            <tbody>
                <%
                    DecimalFormat df = new DecimalFormat("#,##0.00");
                    if (paymentHistoryList != null) {
                        PaymentHistoryVO vo = null;
                        String checkAmt = "", checkAllocAmt = "", unallocAmt = "", blAmt = "", blAllocAmt = "", blBalAmt = "";
                        for (int i = 0; i < paymentHistoryList.size(); i++) {
                            vo = paymentHistoryList.get(i);
                            checkAmt = Validation.checkNull(vo.getCheckamount());
                            checkAmt = checkAmt.length() > 0 && !"null".equalsIgnoreCase(checkAmt) ? df.format(Double.parseDouble(checkAmt)) : "";
                            checkAllocAmt = Validation.checkNull(vo.getCheckallocatedamount());
                            checkAllocAmt = checkAllocAmt.length() > 0 && !"null".equalsIgnoreCase(checkAllocAmt) ? df.format(Double.parseDouble(checkAllocAmt)) : "";
                            unallocAmt = Validation.checkNull(vo.getUnallocatedamount());
                            unallocAmt = unallocAmt.length() > 0 && !"null".equalsIgnoreCase(unallocAmt) ? df.format(Double.parseDouble(unallocAmt)) : "";
                            blAmt = Validation.checkNull(vo.getBlamount());
                            blAmt = blAmt.length() > 0 && !"null".equalsIgnoreCase(blAmt) ? df.format(Double.parseDouble(blAmt)) : "";
                            blAllocAmt = Validation.checkNull(vo.getBlallocatedamount());
                            blAllocAmt = blAllocAmt.length() > 0 && !"null".equalsIgnoreCase(blAllocAmt) ? df.format(Double.parseDouble(blAllocAmt)) : "";
                            blBalAmt = Validation.checkNull(vo.getBlbalanceamount());
                            blBalAmt = blBalAmt.length() > 0 && !"null".equalsIgnoreCase(blBalAmt) ? df.format(Double.parseDouble(blBalAmt)) : "";
                %>
                <tr>
                    <td><%=Validation.checkNull(vo.getBankname())%></td>
                    <td><%=Validation.checkNull(vo.getBankcode())%></td>
                    <td><%=Validation.checkNull(vo.getCurrency())%></td>
                    <td><%=Validation.checkNull(vo.getAccountno())%></td>
                    <td><%=Validation.checkNull(vo.getReferenceno())%></td>
                    <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormat(vo.getStatementdate()))%></td>
                    <td><%=Validation.checkNull(vo.getCustomername())%></td>
                    <td><%=Validation.checkNull(vo.getCustomercode())%></td>
                    <td><%=Validation.checkNull(vo.getCustomeraccountno())%></td>
                    <td><%=Validation.checkNull(vo.getCheckno())%></td>
                    <td><%=checkAmt%></td>
                    <td><%=checkAllocAmt%></td>
                    <td><%=unallocAmt%></td>
                    <td><%=Validation.checkNull(vo.getTransactionno())%></td>
                    <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormat(vo.getAllocationdate()))%></td>
                    <td><%=Validation.checkNull(vo.getBlno())%></td>
                    <td><%=Validation.checkNull(vo.getMcnno())%></td>
                    <td><%=blAmt%></td>
                    <td><%=blAllocAmt%></td>
                    <td><%=blBalAmt%></td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</div>

<div id='payhisprintdiv'></div>
<form id='phform' method="post" class="form-horizontal"></form>

<script type="text/javascript">
    var frmdateval = "<%=fromDate%>";
    var todateval = "<%=toDate%>";
    var phDateInterval = parseInt("<%=phMaxDays%>", 10);

    $('#frdate,#todate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});

    $(document).ready(function () {
        if (frmdateval.length > 0 && todateval.length > 0) {
            $('#frdate').datepicker("setDate", frmdateval);
            $('#todate').datepicker("setDate", todateval);
        } else if (phDateInterval > 0) {
            var today = new Date();
            var defaultFrom = new Date();
            defaultFrom.setDate(today.getDate() - phDateInterval);
            $('#todate').datepicker("setDate", today);
            $('#frdate').datepicker("setDate", defaultFrom);
        }

        $("#paymentHistoryForm").validate({
            submitHandler: function (form) {
                var fromDateObj = $('#frdate').datepicker('getDate');
                var toDateObj = $('#todate').datepicker('getDate');
                if (fromDateObj && toDateObj) {
                    if (fromDateObj.getTime() > toDateObj.getTime()) {
                        alert('<bean:message key="ecom.msg.ph.fromdatelessthantodate" bundle="<%=clientcode%>"/>');
                        return false;
                    }
                    if (phDateInterval > 0) {
                        var diffTime = Math.abs(toDateObj.getTime() - fromDateObj.getTime());
                        var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                        if (diffDays > phDateInterval) {
                            alert('<bean:message key="ecom.msg.ph.dateinterval" bundle="<%=clientcode%>"/>' + phDateInterval + ' days');
                            return false;
                        }
                    }
                }
                $("#searchID").button('loading');
                document.getElementById('paymentHistoryForm').action = "customerPaymentHistory.do";
                document.getElementById('paymentHistoryForm').submit();
            }
        });

        $('#paymentHistoryDataTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": [],
            "scrollX": true
        });
    });

    function callpdf() {
        openLoader();
        $.ajax({
            type: "POST",
            url: "PaymentHistoryJasper.do"
        }).done(function (response) {
            closeLoader();
            $('#payhisprintdiv').html(response);
            if ($('#cromsgres').val() == 'fail') {
                alert('<bean:message key="ecom.cro.errmsg" bundle="<%=clientcode%>"/>');
            } else {
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("phform").action = "CROPrintServlet?croNo=PaymentHistory&responseType=" + responseType + "&viewtype=" + viewtype;
                document.getElementById("phform").submit();
            }
        });
    }
</script>

