<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.CutoffConfigVO,java.util.List,java.util.Hashtable,java.util.Map" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<hr/>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="cutoffconfigtable">
        <thead>
            <tr>
                <th>Port</th>
                <th>Terminal</th>
                <th>CFS Full Container Gate-in Closing</th>
                <th>VGM Closing</th>
                <th>Document Closing</th>
                <th>EDI Declaration Closing</th>
                <th>Full Container Gate-in Closing</th>
                <th>Exclude Weekends</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
				portlist.put("All","All Port");
                Map<String, String> temrminalMap = (Map<String, String>) getServletContext().getAttribute("TEMINALSMAPCONTEXT");
				temrminalMap.put("All","All Terminal");
                List<CutoffConfigVO> cutOffList = (List<CutoffConfigVO>) request.getAttribute("CUTOFFLIST");
                CutoffConfigVO cutOffVO = null;
                if (cutOffList != null && cutOffList.size() > 0) {
                    for (int i = 0; i < cutOffList.size(); i++) {
                        cutOffVO = cutOffList.get(i);
            %>
            <tr id="rowid<%=i%>">
                <td><%=cutOffVO.getPort() + " - " + Validation.checkNull((String) portlist.get(cutOffVO.getPort()))%></td>
                <td><%=Validation.checkNull(temrminalMap.get(cutOffVO.getTerminal()))%></td>
                <td><%=cutOffVO.getCfsGateClosing()%></td>
                <td><%=cutOffVO.getVgmClosing()%></td>
                <td><%=cutOffVO.getDocClosing()%></td>
                <td><%=cutOffVO.getEdiDecClosing()%></td>
                <td><%=cutOffVO.getFullContClosing()%></td>
                <td><%=cutOffVO.getExcludeweekend()%></td>
                <td>
                    <a href="javascript:void(0)" onclick="editCutOffDetails('<%=cutOffVO.getPort() + " - " + Validation.checkNull((String) portlist.get(cutOffVO.getPort()))%>', '<%=cutOffVO.getTerminal()%>', '<%=cutOffVO.getCfsGateClosing()%>', '<%=cutOffVO.getVgmClosing()%>', '<%=cutOffVO.getDocClosing()%>', '<%=cutOffVO.getEdiDecClosing()%>', '<%=cutOffVO.getFullContClosing()%>', '<%=cutOffVO.getExcludeweekend()%>')"  title="Edit" class="btn btn-lg text-warning">
                        <i class="fa fa-edit"></i></a>
                    <a href="javascript:void(0)" onclick="deleteCutoffDetails('<%=cutOffVO.getPort()%>', '<%=cutOffVO.getTerminal()%>', 'rowid<%=i%>')"  title="Delete" class="btn btn-lg text-danger">
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
        var ptable = $('#cutoffconfigtable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });

    });
</script>