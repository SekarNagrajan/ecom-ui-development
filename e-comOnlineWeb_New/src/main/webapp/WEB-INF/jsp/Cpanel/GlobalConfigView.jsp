<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<hr/>
<div class="table-responsive card card-body card-shadow">
   <table class="table table-striped table-bordered" id="configdatatable">
        <thead>
            <tr>
                <th>Id</th>
                <th>Key</th>
                <th>Value</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody >
            <%
                        List configList = (List) request.getAttribute("GlobalConfigList");
                        String admincpanel = (String) session.getAttribute("adminmenupage");
                        GlobalConfigVO configVO = null;
                        if (configList != null) {
                            for (int i = 0; i < configList.size(); i++) {
                                configVO = (GlobalConfigVO) configList.get(i);
            %>
            <tr>
                <td><%= i + 1%></td>
                <td><%=configVO.getKey()%></td>
                <td ><%=configVO.getValue()%></td>
                <td >
                    <a href="javascript:void(0)" onclick="editConfigDetails('<%=configVO.getKey()%>','<%=configVO.getValue()%>')"  title="Edit" class="btn btn-lg text-warning">
                        <i class="fa fa-edit"></i></a>
                        <%if (admincpanel != null && admincpanel.equalsIgnoreCase("cpanelmenupage")) {%>
                    <a href="javascript:void(0)" onclick="deleteConfigDetails('<%=configVO.getKey()%>')"  title="Delete" class="btn btn-lg text-danger">
                        <i class="fa fa-trash-o"></i></a>
                        <%
                                                        }%>
                </td>

            </tr>
            <%
                                        }
            %>
            <%
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


