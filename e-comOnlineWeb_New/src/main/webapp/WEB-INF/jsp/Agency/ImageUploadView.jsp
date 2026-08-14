<%-- 
    Document   : ImageUploadView
    Created on : Jun 16, 2018, 1:00:58 PM
    Author     : gobinath.t
--%>

<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List,java.util.Hashtable,java.util.Date" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!--/* Carrier table */-->
<div class="text-right">  <a href="javascript:void(0)"   id="rearrange" data-toggle="modal" data-target="#Rearrangedialog"  class="btn btn-info btn-xs" title="Image Rearrange" onclick="getRearrangedetails();" data-placement="right"><i class="fa fa-list-ol"></i> Rearrange</a> </div>
<hr>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered"  id="configdatatable">
        <thead>
            <tr>
                <th>S.No.</th>
                <th>Name</th>
                <th>Order</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                        String imagepath = (String) getServletContext().getAttribute("Imagepath");
                        List<ImageUploadVo> imguploadList = (List) request.getAttribute("ImgUploadTableList");
                        ImageUploadVo imgvo = null;
                        String iconclass = null, iconcolor = null;
                        if (imguploadList != null) {
                            for (int i = 0; i < imguploadList.size(); i++) {
                                imgvo = (ImageUploadVo) imguploadList.get(i);

            %>
            <tr>
                <td><%=i + 1%></td>
                <td><%=imgvo.getImagename()%></td>
                <td><%=imgvo.getOrder()%></td>
                <td>
                    <% if (imgvo.getStatus().equalsIgnoreCase("A")) {
                                                        iconclass = "fa-television";
                                                        iconcolor = "text-success";
                                                    } else {
                                                        iconclass = "fa-ban";
                                                        iconcolor = "text-danger";
                                                    }
                    %>
                    <a href="javascript:void(0)" onclick="deleteImageUploadDetails('<%=imgvo.getKeyid()%>','<%=imgvo.getStatus()%>')"  title="show/hide" class="btn btn-lg <%=iconcolor%>">
                        <i class="fa <%=iconclass%>"></i></a>
                    <a href="#" onclick="editConfigDetails('<%=imgvo.getImagename()%>','<%=imgvo.getKeyid()%>')" title="Edit" class="btn btn-lg text-warning">
                        <i class="fa fa-edit"></i></a>
                    <a href="<%=imagepath+imgvo.getImagepath()%>?version=<%=new Date().getTime()%>" class="showimage btn btn-lg text-success">
                        <i class="fa fa-eye" title="View Image" ></i></a>
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
        $('.showimage').fancybox({
           
        });
        var ptable = $('#configdatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>
