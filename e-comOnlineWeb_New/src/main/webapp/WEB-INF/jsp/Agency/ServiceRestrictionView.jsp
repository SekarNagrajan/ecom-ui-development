<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.ServiceRestrictionVO,java.util.List,java.util.Hashtable,java.util.Map" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<hr/>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="fieldconfigtable">
        <thead>
            <tr>
                <th>S.No</th>
                <th>PLA</th>
                <th>PLD</th>
                <th>Service</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                List<ServiceRestrictionVO> svrcRestrictList = (List<ServiceRestrictionVO>) request.getAttribute("SERVICERESTRICTIONLIST");
                ServiceRestrictionVO configVO = null;
                String pla = null, pld = null, service = null, status = null, id = null;
                if (svrcRestrictList != null && svrcRestrictList.size() > 0) {
                    for (int i = 0; i < svrcRestrictList.size(); i++) {
                        configVO = svrcRestrictList.get(i);
                        pla = configVO.getPla() + " - " + Validation.checkNull((String) portlist.get(configVO.getPla()));
                        pld = configVO.getPld() + " - " + Validation.checkNull((String) portlist.get(configVO.getPld()));
                        status = configVO.getStatus();
                        service = configVO.getService();
                        id = configVO.getId();
                        if ("I".equalsIgnoreCase(status)) {
                            status = "Inactive";
                        } else {
                            status = "Active";
                        }
            %>
            <tr id="rowid<%=i%>">
                <td><%=(i + 1)%></td>
                <td><%= configVO.getPla()%></td>
                <td><%=configVO.getPld()%></td>
                <td><%=service%></td>
                <td><%=status%></td>
                <td>
                    <a href="javascript:void(0)" onclick="editFieldConfigDetails('<%=configVO.getId()%>', '<%=pla%>', '<%=pld%>', '<%=service%>', '<%=configVO.getStatus()%>')"  title="Edit" class="btn btn-lg text-warning">
                        <i class="fa fa-edit"></i></a>
                    <a href="javascript:void(0)" onclick="deleteFieldConfigDetails('<%=configVO.getId()%>')"  title="Delete" class="btn btn-lg text-danger">
                        <i class="fa fa-trash-o"></i></a>
                </td>
            </tr>
            <%                            }
                }%>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var ptable = $('#fieldconfigtable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>



