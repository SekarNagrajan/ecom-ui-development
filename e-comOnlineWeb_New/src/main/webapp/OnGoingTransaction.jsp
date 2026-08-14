<%-- 
    Document   : OnGoingTransaction
    Created on : Sep 26, 2019, 4:19:11 PM
    Author     : gobinath.t
--%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList,net.ecom.vo.OnGoingTransactionVo,net.ecom.common.Validation,net.ecom.common.utils.MaskPortUtil,net.ecom.common.GeneralConf"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String bookno = "";
    String originport = "";
    String finalport = "";
    String LRPStatus = "";
    String polAt = "";
    String bl_no = "", containerno = "", lastestAct = "", tuesCount = "";
    ArrayList daslis = (ArrayList) request.getAttribute("ONGOINGDASHBOARDDETAILS");
    OnGoingTransactionVo dasvo = null;
    String clientcode = (String) session.getAttribute("clientcode");
    String FeusExists = (String) getServletContext().getAttribute("FEUexists");
    String HIDE_MASKPORT_CONTAINER = (String) getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER);


%>
<div class="container">
    <form method="post" action="ViewEbookingEntry.do" id="EBookingViewAllForm"  >

        <input type="hidden" id="bookno" name="bookno">
        <input type="hidden" id="flag" name="flag">
        <input type="hidden" id="action" name="action">
        <input type="hidden" id="book_type" name="book_type">
        <input type="hidden" id="ecomlrptype" name="ecomlrptype">
        <input type="hidden" id="lrpbookno" name="lrpbookno">
        <input type="hidden" id="ecomBookNo" name="ecomBookNo">
        <input type="hidden" id="manageTemplate" name="manageTemplate" value="N">
        <input type="hidden" id="appversion" name="appversion">
        <input type="hidden" id="backtodashboard" name="backtodashboard">
        <input type="hidden" id="type" name="type">
        <input type="hidden" id="blno" name="blno">
    </form>
</div>




<div class="table-responsive" style="padding: 10px;">
    <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
    <table class="table table-hover mb-0 ps-container ps-theme-default table_text_center" id="datatablebyongoingtrans">
        <thead>
            <tr>
                <th><bean:message key="ecom.dashb.ongoingtrans.bkgno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.dashb.ongoingtrans.refno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.blnum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.origin" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Schedule.View.POLETD" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.shippingins.blstatus" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.latestactive" bundle="<%=clientcode%>"/></th>
                    <%  if ("YES".equalsIgnoreCase(FeusExists)) {%>
                <th><bean:message key="ebooking.fuescount" bundle="<%=clientcode%>" ></bean:message></th>
                    <%} else {%>
                <th><bean:message key="ebooking.tuescount" bundle="<%=clientcode%>" ></bean:message></th>

                <%}%>
                <th><bean:message key="Tracking.action" bundle="<%=clientcode%>"/></th>
                <th style="display:none;">All Containers</th>
            </tr>
        </thead>
        <tbody>
            <%
                if (daslis != null && daslis.size() > 0) {
                    int size = daslis.size();
                    Set dashBoardSet = (Set) session.getAttribute("dashBoardAccessList");
                    if (dashBoardSet == null) {
                        dashBoardSet = new HashSet();
                    }
                    for (int i = 0; i < size; i++) {
                        dasvo = (OnGoingTransactionVo) daslis.get(i);
                        bookno = Validation.checkNull(dasvo.getOnlinerefno());
                        originport = MaskPortUtil.swapActualPortToMaskPortWithName(Validation.checkNull(dasvo.getOriginportid()));
                        finalport = MaskPortUtil.swapActualPortToMaskPortWithName(Validation.checkNull(dasvo.getFinalportid()));
                        polAt = Validation.checkNull(dasvo.getPolat());
                        String lprBook_no = Validation.checkNull(dasvo.getBook_no());
                        bl_no = Validation.checkNull(dasvo.getBl_no());
                        LRPStatus = Validation.checkNull(dasvo.getStatus());
                        // If Origin or Delivery port is masked, empty the container number
                        if (MaskPortUtil.isActualPort(dasvo.getOriginportid()) || MaskPortUtil.isActualPort(dasvo.getFinalportid())) {
                            containerno = "";
                        } else {
                            containerno = Validation.checkNull(dasvo.getContainerno());
                        }
                        lastestAct = Validation.checkNull(dasvo.getLatestact());
                        tuesCount = Validation.checkNull(dasvo.getTuescount());
                        if ("C".equalsIgnoreCase(LRPStatus)) {
                            LRPStatus = "<span class='label label-success'>Confirmed</span>";
                        } else if ("D".equalsIgnoreCase(LRPStatus)) {
                            LRPStatus = "<span class='label label-info'>Draft</span>";
                        } else if ("V".equalsIgnoreCase(LRPStatus)) {
                            LRPStatus = "<span class='label label-danger'>Cancelled</span>";
                        } else if ("I".equalsIgnoreCase(LRPStatus)) {
                            LRPStatus = "<span class='label label-warning'>Issued</span>";
                        }
            %>
            <tr>   

                <td><a href="javascript:void(0)" class="table_text_highlight" style="cursor:pointer" onclick="bookingView('bookingDetails', '<%=bookno%>', 'D', 'L', '<%=lprBook_no%>', '2')"><%=lprBook_no%></a></td>
                <td><%=bookno%></td>
                <td><a href="javascript:void(0)" class="table_text_highlight" style="cursor:pointer" onclick="BLView('<%=bl_no%>', '<%=lprBook_no%>', '2')"><%=bl_no%></a></td>
                <td><%=originport%></td>
                <td><%=finalport%></td>
                <td><%=polAt%></td>
                <td><%=LRPStatus%></td>
<!--                <td><a href="javascript:void(0)"  align="center" style="cursor:pointer" onclick="ContainerDashboardInfo('<%=lprBook_no%>', '<%=bl_no%>')">
                <%=containerno%></a></td>-->
                <td><%=containerno%></td>
                <td><%=lastestAct%></td>
                <td><%=tuesCount%></td>
                <td class="actionresult" align="center"><span onclick="ContainerDashboardInfo('<%=lprBook_no%>', '<%=bl_no%>', '<%=(MaskPortUtil.isActualPort(dasvo.getOriginportid()) || MaskPortUtil.isActualPort(dasvo.getFinalportid())) ? "Y" : "N"%>')"  data-toggle="tooltip" class="text-success" style="font-size: 15px;" title="More Details" data-placement="right"><i class="fa fa-plus"></i></span></td>
                <%if("Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)){%>
                <td style="display:none;"></td>
                <%} else {%>
                <td style="display:none;"><%=dasvo.getAllcontainernos()%></td>
                <%}%> 

            </tr>
            <%
                        if (lprBook_no != null && lprBook_no != "") {
                            dashBoardSet.add(lprBook_no);
                        }
                        if (bl_no != null && bl_no != "") {
                            dashBoardSet.add(bl_no);
                        }
                    }
                    session.setAttribute("dashBoardAccessList", dashBoardSet);
                }%>

        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#datatablebyongoingtrans').DataTable({
            dom: 'Blfrtip',
            buttons: [{
                    extend: 'excel',
                    text: '<span class="text-white fa fa-file-excel-o"></span>',
                    key: {
                        key: 'c',
                        altKey: true
                    }
                }],
            lengthMenu: [[5, 10, 25, 50, -1], [5, 10, 25, 50, "All"]],
            aaSorting: []
        });

    });
</script>
<script type="text/javascript">
    function bookingView(type, bookno, book_type, ecomlrptype, lrpbookno, appversion) {
        openLoader();
        console.log("inside met1");
        $("#ecomlrptype").val(ecomlrptype);
        $("#lrpbookno").val(lrpbookno);
        $("#type").val(type);
        $("#bookno").val(bookno);
        $("#book_type").val(book_type);
        $("#appversion").val(appversion);
        $('#backtodashboard').val('YES');
        $('#EBookingViewAllForm').attr('action', 'ViewEbookingEntry.do');
        $('#EBookingViewAllForm').submit();
    }
    function BLView(blno, bookno, appversion)
    {
        openLoader();
        $('#type').val("DashBLView");
        $('#blno').val(blno);
        $('#bookno').val(bookno);
        $('#appversion').val(appversion);
        $('#backtodashboard').val("YES");
        $('#EBookingViewAllForm').attr("action", "BLEditAction.do");
        $('#EBookingViewAllForm').submit();
    }
</script>

