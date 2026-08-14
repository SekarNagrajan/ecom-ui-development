<%-- 
    Document   : CustomerAdvisoryView
    Created on : Jun 14, 2018, 2:08:04 PM
    Author     : gobinath.t
--%>
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List,java.util.Hashtable" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<hr/>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered"  id="configdatatable">
        <thead>
            <tr>
                <th>S.No.</th>
                <th>Advisory Name</th>
                <th>Description</th>
                <th>Created Date</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%

                        List<CustomerAdvisoryVo> custadvisList = (List) request.getAttribute("CustAdvisTableList");
                        CustomerAdvisoryVo cusadvvo = null;
                        String description = null;
                        if (custadvisList != null) {
                            for (int i = 0; i < custadvisList.size(); i++) {
                                cusadvvo = custadvisList.get(i);
                                description = cusadvvo.getDescription().replaceAll("\\r\\n", "<br>");

            %>
            <tr>
                <td><%=i + 1%></td>
                <td><%=cusadvvo.getAdvisoryname()%></td>
                <td><%=description%></td>
                <td><%=cusadvvo.getCreated_date()%></td>
                <td>
                    <%
                                                    description = description.replaceAll("\"", "%22"); //or &quot;
                                                    description = description.replaceAll("'", "%27");
                    %>
                    <a href="#" onclick="editConfigDetails('<%=cusadvvo.getAdvisoryname()%>','<%=description%>','<%=cusadvvo.getKeyid()%>')" title="Edit" class="btn btn-lg text-warning">
                        <i class="fa fa-edit"></i></a>
                    <a href="javascript:void(0)" onclick="deleteCustAdvisoryDet('<%=cusadvvo.getKeyid()%>')"  title="Delete" class="btn btn-lg text-danger">
                        <i class="fa fa-trash-o"></i></a>
                </td>
            </tr>
            <%       }
                        }
            %>

        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        var ptable = $('#configdatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });

    });
</script>
