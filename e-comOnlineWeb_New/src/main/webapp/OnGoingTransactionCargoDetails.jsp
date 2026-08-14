<%-- 
    Document   : OnGoingTransactionCargoDetails
    Created on : Sep 26, 2019, 7:34:35 PM
    Author     : gobinath.t
--%>
<%@page import="java.util.ArrayList,net.ecom.vo.OnGoingTransactionVo,net.ecom.common.Validation"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String containerno = "", activityname = "";
    ArrayList daslis = (ArrayList) request.getAttribute("ONGOINGDASHBOARDCARGODETAILS");
    OnGoingTransactionVo dasvo = null;
    String clientcode = (String) session.getAttribute("clientcode");
    String maskport = Validation.checkNull(request.getParameter("maskport"));
%>
<div>
    <div class="table-responsive">
        <table class="table table-striped table-bordered text-center" id="datatableongoingtranscargo">
            <thead>
                <tr>
                    <th><bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="Tracking.latestactive" bundle="<%=clientcode%>"/></th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (daslis != null && daslis.size() > 0) {
                        int size = daslis.size();
                        for (int i = 0; i < size; i++) {
                            dasvo = (OnGoingTransactionVo) daslis.get(i);
                            activityname = Validation.checkNull(dasvo.getLatestact());
                            containerno = Validation.checkNull(dasvo.getContainerno());
                %>
                <tr>
                    <td><%="Y".equalsIgnoreCase(maskport) ? "" : containerno%></td>
                    <td><%=activityname%></td>
                </tr>
                <%
                        }
                    }%>
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#datatableongoingtranscargo').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []

        });

    });
</script>