<%-- 
    Document   : CommoditySearchView
    Created on : Mar 29, 2018, 2:54:17 PM
    Author     : deepa.p
--%>

<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.CommoditySearchVO,java.util.ArrayList,java.util.List,net.ecom.common.GeneralConf,java.util.Hashtable" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page pageEncoding="UTF-8"%>

<!--/* Carrier table */-->
<%  String clientcode = (String) session.getAttribute("clientcode");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    Hashtable comlist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
    String frontcomdtycode = (String) getServletContext().getAttribute(GeneralConf.Display_Commodity_Code_Front);
%>
<hr/>
<div class="col-xs-12 col-sm-12">
    <div class="table-responsive" style="max-height: 300px">
        <table class="table table-striped table-bordered text-center" id="configdatatable">
            <thead>
                <tr>
                    <th><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>" /></th>
                        <%if (!"Y".equalsIgnoreCase(frontcomdtycode)) {%>
                    <th><bean:message key="ebooking.cargo.groupcode" bundle="<%=clientcode%>" /></th>
                        <%}%>
                    <th><bean:message key="ebooking.cargo.groupname" bundle="<%=clientcode%>" /></th>
                    <th>
                        <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                        <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/>
                        <%} else {%>
                        <bean:message key='ebooking.trust.hscode' bundle="<%=clientcode%>"/>
                        <%}%>
                    </th>
                    <th><bean:message key="ebooking.cargo.desc" bundle="<%=clientcode%>" /></th>
                    <th><bean:message key="EBookingView.action" bundle="<%=clientcode%>" /></th>
                </tr>
            </thead>
            <tbody >
                <%
                    String serviceType = "", commodity = "";
                    System.out.println("[ECOM-WAR] CommoditySearchView coommdoty search view jsp.......");
                    List<CommoditySearchVO> list = (List<CommoditySearchVO>) request.getAttribute("COMMODITYSEARCHVIEW");
                    CommoditySearchVO vo = null;
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            vo = (CommoditySearchVO) list.get(i);
                            commodity = vo.getChileComdtyName();
                            commodity = commodity.replaceAll("\"", "&quot;");
                            commodity = commodity.replaceAll("\\\\", "");
                            commodity = commodity.replaceAll("\\r|\\n", "");


                %>
                <tr>
                    <td><%= i + 1%></td>
                    <%if (!"Y".equalsIgnoreCase(frontcomdtycode)) {%>
                    <td><%=vo.getParentComdtyCode()%></td>
                    <%}%>
                    <td><%=Validation.checkNull((String) comlist.get(vo.getParentComdtyCode()))%></td>
                    <td><%=vo.getChileComdtyCode()%></td>
                    <td><%=vo.getChileComdtyName()%></td>
                    <td data-title="Select"><a href="javascript:void(0);" class="btn btn-warning btn-xs text-white" id="999" onclick="setComdtyForParent('<%=vo.getParentComdtyCode()%>', '<%=vo.getChileComdtyCode()%>', '<%=commodity%>');">Select</a></td>
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
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var ptable = $('#configdatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });

    });
</script>


