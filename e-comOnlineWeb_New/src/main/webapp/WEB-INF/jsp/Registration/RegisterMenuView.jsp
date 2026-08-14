<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="menudatatable">
        <thead>
            <tr>
                <th>Menu Id</th>
                <th>Menu Name</th>
                <th>Status</th>
                <th>Category</th>
                <th>User Type</th>
                <th>Menu Order</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody >
            <%
                ArrayList RegisterMenuList = (ArrayList) request.getAttribute("RegisterMenuList");
                int slno = 0;
                int tarsize = 0;
                int maxid = 0;
                String Status = "";
                String Category = "";
                String Usertype = "";
                String ErrorMessage = "";
                if (RegisterMenuList != null && RegisterMenuList.size() > 0) {

                    tarsize = RegisterMenuList.size();
                    for (int i = 0; i < tarsize; i++) {
                        slno++;

                        RegisterMenuVO registerVo = (RegisterMenuVO) RegisterMenuList.get(i);
                        int Idvalue = registerVo.getMenuId();

                        if (registerVo.getMenuStatus().equalsIgnoreCase("A")) {
                            Status = "ACTIVE";
                        } else {
                            Status = "INACTIVE";
                        }
                        if (registerVo.getCategory().equalsIgnoreCase("P")) {
                            Category = "PRIVILEGED";
                        } else {
                            Category = "DEFAULT";
                        }

                        if (registerVo.getUserType().equalsIgnoreCase("U")) {
                            Usertype = "User";
                        } else {
                            Usertype = "Vendor";
                        }

                        ErrorMessage = registerVo.getMessage();
                        maxid = registerVo.getMaxId();
                        System.out.println("[ECOM-WAR] RegisterMenuVIew MAX id   :::: " + maxid);
                        String attrvalue =registerVo.getAttrValue();
                        attrvalue = attrvalue!=null ?attrvalue.replaceAll("\"", "%22").replaceAll("'", "%27"):attrvalue;
            %>
        <input type="hidden" name="errMsg" id="errMsg" value="<%=ErrorMessage%>"/>
        <tr>
            <td><%= registerVo.getMenuId()%></td>
            <td><%=registerVo.getMenuName()%></td>
            <td><%= Status%></td>
            <td><%= Category%></td>
            <td><%= Usertype%></td>
            <td><%= registerVo.getMenuOrder()%></td>
            <td><a onclick="editMenuReg('<%=Idvalue%>', '<%=registerVo.getMenuName()%>', '<%=registerVo.getDevelopedBy()%>', '<%=registerVo.getMenuStatus()%>', '<%=registerVo.getDate()%>', '<%=registerVo.getRefNo()%>', '<%=registerVo.getCategory()%>', '<%= registerVo.getCreatedBy()%>', '<%=maxid%>', '<%=Usertype%>', '<%= registerVo.getMenuOrder()%>', '<%= registerVo.getClassValue()%>', '<%= attrvalue%>', '<%= registerVo.getLabelValue()%>', '<%= registerVo.getMenuType()%>', '<%= registerVo.getParentMenu()%>');" href="javascript:void(0)" class="btn btn-lg text-warning" id="myEdit-<%=i%>">
                    <i class="fa fa-pencil-square-o"></i></a>
                <a id="myDel-<%=Idvalue%>" onclick = "deleteMenuReg('<%=Idvalue%>', '<%=Usertype%>');" href="javascript:void(0)" class="btn btn-lg text-danger"><i class="fa fa-trash-o"></i></a>
            </td>
        </tr>
        <%
            }
        %>
        <%
            }
        %>
<input type="hidden" id="maxiid" name="maxid" value='<%=maxid%>'/>
        </tbody>
    </table>
<!--    <table id="sampl">
        <tr><td style="display:none;"> </td></tr>
    </table>-->
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var ptable = $('#menudatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });

        //  cfilter("tariffdatatable", "pfiltercolumndata", ptable, 1);
    });

</script>


