<%-- 
    Document   : BLCharge
    Created on : 6 Dec, 2024, 9:49:09 AM
    Author     : preethi.elumalai
--%>
<%@page import="net.ecom.vds.serviceVo.BLChargeVo"%>
<%@page contentType="text/html; charset=UTF-8" import="java.util.ArrayList, java.util.HashMap,java.util.Set,java.util.Hashtable,net.ecom.vo.SIDtableVO, net.ecom.vo.SIPartiesVO,net.ecom.vo.SIContainerVO,java.util.List,net.ecom.common.Validation,java.util.Map,net.ecom.vo.SIContainerDetailVO,net.ecom.vo.SIMasterVO,net.ecom.vo.SISealVO,net.ecom.vo.SIConatainerValueVO,net.ecom.common.utils.DateConversion_New,net.ecom.vo.ChargesVo,net.ecom.common.GeneralConf,net.ecom.common.NumberFormat"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String Display_Charge_Summary = (String) getServletContext().getAttribute(GeneralConf.Display_Charge_Summary);
    List<BLChargeVo> chargeSummarylst = (ArrayList<BLChargeVo>) session.getAttribute("blChargeview");
%>
<%if ("Y".equalsIgnoreCase(Display_Charge_Summary)) {%>
<div class="modal-content fancybox-content" style="padding: inherit;" >
    <div class="modal-header bg-info text-white">
        <h5 class="modal-title">Charge Summary</h5>
    </div>
    <div class="modal-body">
        <div id="dynamicForm">
            <table class="table table-striped table-bordered"  id="previewdatatableblcharge">
                <thead>
                    <tr>
                        <th title="chargeCode">Charge Code</th>
                        <th title="chargeDesc" >Charge Desc.</th>
                        <th title="curreny" >Curreny</th>
                        <th title="prepaid" >Prepaid</th>
                        <th title="collect" >Collect</th>
                        <th title="payAt" >PayAt</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        BLChargeVo charge = null;
                        if (chargeSummarylst != null && chargeSummarylst.size() > 0) {
                            for (int i = 0; i < chargeSummarylst.size(); i++) {
                                charge = (BLChargeVo) chargeSummarylst.get(i);
                    %>
                    <tr>
                        <td><%=charge.getChargeId()%></td>
                        <td ><%=charge.getChargeName()%></td>
                        <td><%=charge.getCurrencyId()%></td>
                        <%
                            String prepaidCollect = charge.getPrepaidCollect();
                            String chargeAmount = charge.getChargeAmount();
                        %>
                        <td>
                            <%= "P".equalsIgnoreCase(prepaidCollect) ? chargeAmount : ""%>
                        </td>
                        <td>
                            <%= "C".equalsIgnoreCase(prepaidCollect) ? chargeAmount : ""%>
                        </td>
                        <td>
                            <%= "E".equalsIgnoreCase(prepaidCollect) ? chargeAmount : ""%>
                        </td>

                    </tr>
                    <% }
                        }
                    %>
                </tbody>
            </table>
        </div>    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#previewdatatableblcharge').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>

<%}%>    

