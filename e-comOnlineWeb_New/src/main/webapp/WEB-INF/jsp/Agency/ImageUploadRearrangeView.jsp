<%--
    Document   : ImageUploadRearrangeView
    Created on : Jun 5, 2018, 12:37:31 PM
    Author     : gobinath.t
--%>
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List,java.util.Hashtable"%>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="configdatatablerearrange">
        <thead>
            <tr>
                <th style="display: none">Keyid</th>
                <th>ImageName</th>
                <th width="80px">Rearrange</th>
            </tr>
        </thead>
        <tbody>
            <%

                        List<ImageUploadVo> imguploadList = (List) request.getAttribute("ImgUploadTableList");
                        ImageUploadVo imgvo = null;
                        if (imguploadList != null) {
                            for (int i = 0; i < imguploadList.size(); i++) {
                                imgvo = (ImageUploadVo) imguploadList.get(i);

            %>
            <tr>
                <td style="display: none"><%=imgvo.getKeyid()%></td>
                <td><%=imgvo.getImagename()%></td>
                <td width="80px" class="text-center">
                    <a href="javascript:void(0)" title="top" class="top btn btn-primary btn-xs"><i class="fa fa-angle-double-up" aria-hidden="true"></i></a>
                    <a href="javascript:void(0)" title="up" class="up btn btn-primary btn-xs"><i class="fa fa-angle-up" aria-hidden="true"></i></a>
                    <a href="javascript:void(0)" title="down" class="down btn btn-primary btn-xs"><i class="fa fa-angle-down" aria-hidden="true"></i></a>
                    <a href="javascript:void(0)" title="bottom" class="bottom btn btn-primary btn-xs"><i class="fa fa-angle-double-down" aria-hidden="true"></i></a>
                </td>
            </tr>
            <%       }
                        }
            %>

        </tbody>
    </table>

</div>

<script type="text/javascript">
//    $(document).ready(function() {
//        var ptable = $('#configdatatablerearrange').DataTable({
//            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
//            "aaSorting": []
//        });
//    });
</script>
<script type="text/javascript">
    var rearrangeactive=false;
    $(document).ready(function(){
        $(".up,.down,.top,.bottom").click(function(){
            rearrangeactive=true;
            var row = $(this).parents("tr:first");
            if ($(this).is(".up")) {
                row.insertBefore(row.prev());
            } else if ($(this).is(".down")) {
                row.insertAfter(row.next());
            } else if ($(this).is(".top")) {
                row.insertBefore($("#configdatatablerearrange tbody tr:first"));
            }else {
                row.insertAfter($("#configdatatablerearrange tbody tr:last"));
            }
        });
    });
</script>
