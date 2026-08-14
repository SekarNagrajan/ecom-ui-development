<%@ page contentType="text/html; charset=UTF-8" language="java" import="net.ecom.vo.EbookingValidatorFormBeanNew,java.util.ArrayList,java.util.List,net.ecom.common.utils.MaskPortUtil" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String cacheVersion = (String) getServletContext().getAttribute("CACHEVERSION");
    cacheVersion = cacheVersion != null && cacheVersion.length() > 0 ? "?" + cacheVersion : "";
    String clientcode = (String) session.getAttribute("clientcode");

%>

<style type="text/css">
    /*    .fancybox-close  {
            position: absolute;
            top: 0px;
            right: 0px;
            width: 36px;
            height: 36px;
            cursor: pointer;
            z-index: 8040;
        }
        .tooltip-inner {
            background-color: #1FBBA6;
            color: #fff;
        }
        .tooltip.right .tooltip-arrow  {
            border-right-color: #1FBBA6;
            color: #fff;
        }
        .tooltip.left .tooltip-arrow  {
            border-left-color: #1FBBA6;
            color: #fff;
        }*/
</style>

    <div class="card">
        <div class="card-header bg-primary"><span class="fa fa-book"></span> <span class="hidden-xs" style="font-weight:700"><bean:message key="Booking.managetemp" bundle="<%=clientcode%>"/> </div>
        <div class="card-body card-body-border">
            <div class="table-responsive">
                <table class="table table-striped table-bordered" id="datatablebytempbooking">
                    <thead>
                        <tr>
                 <th><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>"/> </th>
                <th><bean:message key="ebooking.tempname.display" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.origin" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="EBookingView.action" bundle="<%=clientcode%>"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            ArrayList tempbook = (ArrayList) session.getAttribute("SESSION_MANAGE_TEMPLATE");
                            int sno = 0;
                            String tempbookno = "";
                            String templatename = "";
                            String origin = "";
                            String delivery = "";
                            EbookingValidatorFormBeanNew tempbooking = null;
                            if (tempbook != null && tempbook.size() > 0) {
                                //   System.out.println("[ECOM-WAR] manageTemplate yessss:::::");
                                int size = tempbook.size();
                                for (int i = 0; i < size; i++) {
                                    //          System.out.println("[ECOM-WAR] manageTemplate yessss11111:::::");
                                    tempbooking = (EbookingValidatorFormBeanNew) tempbook.get(i);
                                    //          System.out.println("[ECOM-WAR] manageTemplate tempbookno:::"+tempbooking);
                                    tempbookno = tempbooking.getEbook_templateno();
                                    templatename = tempbooking.getEbook_templatename();
                                    origin = MaskPortUtil.swapActualPortToMaskPortWithName(tempbooking.getEbook_pol());
                                    delivery = MaskPortUtil.swapActualPortToMaskPortWithName(tempbooking.getEbook_pod());
                                    sno++;
                        %>
                        <tr>
                            <td><%=sno%></td>
                            <td><%=templatename%></td>
                            <td><%=origin%></td>
                            <td><%=delivery%></td>
                            <td> <a href="javascript:void(0)" onclick="tempbookingEdit('<%=tempbookno%>')" title="Modify Template" class="btn btn-lg text-warning" data-toggle="tooltip"  data-placement="left"><i class="fa fa-pencil-square-o"></i></a>
                                <a href="javascript:void(0)"  onclick="tempbookingdelete('<%=tempbookno%>', '<%=templatename%>')"  class="btn btn-lg text-danger" data-toggle="tooltip" title="Delete" data-placement="left"><i class="fa fa-trash-o"></i></a>
                                <a href="javascript:void(0)"  onclick="templateNewBooking('<%=tempbookno%>')"  class="btn btn-lg text-success" data-toggle="tooltip" title="Create Booking" data-placement="left"><i class="fa fa-plus-square"></i></a></td>
                                    <% }
                                    }%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#datatablebytempbooking').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>



