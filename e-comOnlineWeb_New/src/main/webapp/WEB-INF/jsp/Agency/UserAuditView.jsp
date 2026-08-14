
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="auditdatatable">
 
        <thead>
            <tr>
                <th>Sl.No</th>
                <th>Customer Code</th>
                <th>Customer Name</th>
                <th>UserName</th>
                <th>DateTime</th>
                <th>Module</th>
                <th>Activity</th>
                <th>Reference</th>
                <th>Remarks</th>
            </tr>
        </thead>
        <tbody >
            <%
                        ArrayList UserLogList = (ArrayList) request.getAttribute("UserlogList");
                        System.out.println("[ECOM-WAR] UserAuditView  useraudit view.jsp  ------------- ");
                        int slno = 0;
                        int tarsize = 0;
                        int maxid = 0;
                        String Status = "";
                        String Category = "";
                        String UserName = "";
                        String UserCode = "";
                        if (UserLogList != null && UserLogList.size() > 0) {
                            tarsize = UserLogList.size();
                            for (int i = 0; i < tarsize; i++) {
                                slno++;
                                UserAuditVO usradtVo = (UserAuditVO) UserLogList.get(i);
                                if (usradtVo.getUserName() != null) {
                                    UserName = usradtVo.getUserName();
                                }
                                if (usradtVo.getUserCode() != null) {
                                    UserCode = usradtVo.getUserCode();
                                }
            %>
            <tr>
                <td><%=slno%></td>
                <td><%=UserCode%></td>
                <td><%=usradtVo.getCustomername()%></td>
                <td><%=UserName%></td>
                <td><%=usradtVo.getDateTime()%></td>
                <td><%=usradtVo.getModule()%></td>
                <td><%=usradtVo.getActivity()%></td>
                <td><%=usradtVo.getReference()%></td>
                <td><%=usradtVo.getRemarks()%></td>
            </tr>

            <%
                                        }
            %>
            <%
                        }
            %>
        <input type="hidden" id="maxiid" name="maxid" value='sssssssss'/>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        var ptable = $('#auditdatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });

        cfilter("tariffdatatable","pfiltercolumndata",ptable,1);
    });

</script>



