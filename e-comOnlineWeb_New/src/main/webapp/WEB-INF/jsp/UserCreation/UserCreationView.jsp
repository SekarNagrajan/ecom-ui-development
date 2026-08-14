<%@page import="java.util.ArrayList,net.ecom.vo.UserData"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%     String clientcode = (String) session.getAttribute("clientcode");
%>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="datatablebyuser">
        <thead>
            <tr>
                <th><bean:message key="login.name" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ViewProfile.userifo.Name" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ViewProfile.userifo.onlinestatus" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.shippingins.action" bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%
                UserData usr = new UserData();
                ArrayList userdetails = (ArrayList) request.getAttribute("viewusrArray");
                int j = 0;
                if (userdetails != null) {
                    int size = userdetails.size();
                    int i = 0;
                    for (i = 0; i < size; i++) {
                        usr = (UserData) userdetails.get(i);
                        if ("Active".equalsIgnoreCase(usr.getOnlineactive())) {
                            j++;
                        }
            %>
            <tr>
                <td><%=usr.getLoginName()%></td>
                <td><%=usr.getContantname()%></td>
                <td><%=usr.getOnlineactive()%></td>
                <td>
                    <%if ("Active".equalsIgnoreCase(usr.getOnlineactive())) {%>
                    <a href="javascript:void(0)" onclick="deletUserDetails('<%=usr.getLoginName()%>', '<%=usr.getContantname()%>');"   data-toggle="tooltip" class="btn btn-danger btn-sm" title="De-Activate" data-placement="right"> <bean:message key="ecom.usercrt.deactivate" bundle="<%=clientcode%>"/></a>
                    <%} else {%>
                    <a href="javascript:void(0)" onclick="activeUserDetails('<%=usr.getLoginName()%>', '<%=usr.getContantname()%>');"   data-toggle="tooltip" class="btn btn-success btn-sm" title="Activate" data-placement="right"> <bean:message key="ecom.usercrt.activate" bundle="<%=clientcode%>"/></a>
                    <%}%>
                </td>
            </tr>
            <%}
                }%>
        </tbody>
    </table>
    <input type="hidden" id="usercount" value="<%=j%>"/>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var ptable = $('#datatablebyuser').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>



