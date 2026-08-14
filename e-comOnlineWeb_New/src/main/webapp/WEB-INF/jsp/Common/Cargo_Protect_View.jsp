<%-- 
    Document   : Cargo_Protect
    Created on : 19 Jan, 2024, 10:47:11 AM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.common.NumberFormat"%>
<%@page import="net.ecom.common.GeneralConf,net.ecom.vo.CargoProtectVo,java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<%
    String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);
    String clientcode = (String) session.getAttribute("clientcode");
    String type = request.getParameter("type");
    String pagetype = request.getParameter("page");
    if ("Y".equalsIgnoreCase(enableCargoProtect)) {
        List<CargoProtectVo> cplis = (List<CargoProtectVo>) request.getAttribute("CargoProtectDetails");
        String carprotectrestrict = (String) request.getAttribute("disable_Cargoportect");
        System.out.println("[ECOM-WAR] Cargo_Protect_View  carprotectrestrict==> " + carprotectrestrict);
%>
<table id="cargoprotect_table" class="cargoprotect_table table">
    <thead>
        <tr>
            <%if ("shipmentdate".equalsIgnoreCase(type)) {%>
            <th style="width:10%">Selection</th>
                <%}%>
            <th style="width:20%">Tier</th>
            <th style="width:50%">Maximum Coverage</th>
            <th style="width:20%">Per Shipment (USD)</th>
                <%if ("id".equalsIgnoreCase(type) && (!"view".equalsIgnoreCase(pagetype))) {%>
            <th style="width:10%">Action</th>
                <%}%>  
        </tr>
    </thead>
    <tbody>
        <%if (cplis != null && cplis.size() > 0) {
                CargoProtectVo cpvo = null;
                
                for (int i = 0; i < cplis.size(); i++) {
                    cpvo = cplis.get(i);
        %>
        <tr>
            <%if ("shipmentdate".equalsIgnoreCase(type)) {%>
            <td><input type="checkbox" value="<%= cpvo.getId()%>" name="cargoprotect_check" class="cargoprotect_check" onchange="setCargoprotectid(this)" <%= "Y".equals(carprotectrestrict) ? "disabled" : "" %> /></td>
                <%}%>
            <td><%= cpvo.getTier()%></td>
            <td><%= cpvo.getMaxcoverage()%></td>
            <td><%= NumberFormat.decimalFormat(cpvo.getPershipment())%></td>
            <%if ("id".equalsIgnoreCase(type) && (!"view".equalsIgnoreCase(pagetype))) {%>
            <td><a href="javascript:removeCargoProtect('<%= cpvo.getId()%>',this);" >Remove</a></td>
            <%}%>  
        </tr>
        <%}
            }%>

    </tbody>
</table>
<%if ("Y".equalsIgnoreCase(carprotectrestrict)) {%>
<script type="text/javascript">
        $(document).ready(function () {
        $('#cargoprotectid').val('');
        $("#cargoprotect_table_div").html('');
        $("#cargoprotect_table_div").hide();
        $("#selected_tier").hide();
        // Uncheck all cargo protect checkboxes
        $('.cargoprotect_check').prop('checked', false);
    });
</script>
<%
    }
%>
<%
    }
%>