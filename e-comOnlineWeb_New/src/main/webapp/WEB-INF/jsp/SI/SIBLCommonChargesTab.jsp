<%-- 
    Document   : SIBLCommonChargesTab
    Created on : 6 Dec, 2024, 4:23:22 PM
    Author     : preethi
--%>

<%@page import="net.ecom.vds.serviceVo.BLChargeVo"%>
<%@page contentType="text/html; charset=UTF-8" import="java.util.ArrayList,net.ecom.common.GeneralConf,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String Display_Charge_Tab = (String) getServletContext().getAttribute(GeneralConf.Display_Charge_Tab);
    List<BLChargeVo> Chargedetails = null;
    Chargedetails = (List<BLChargeVo>)session.getAttribute("SIBLChargeSummary");
%>
<%if ("Y".equalsIgnoreCase(Display_Charge_Tab)) {%>

<h6><i class="step-icon fa_upload"></i> <bean:message key="ecom.si.charges" bundle="<%=clientcode%>"/> </h6>

<fieldset>  	
    <div class="details_search">
            <!--            <div class="si_imp_exp_excel pull-right">
                            <span class="pull-right"> <a href="javascript:void(0)" class="btn btn-info btn-sm" onclick="applyPaste()"><i class="fa fa-paste"></i> Paste </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <span class="pull-right"> <a href="javascript:void(0)" title="Copy" class="btn btn-info btn-sm" onclick="copycharges()" ><i class="fa fa-copy"></i> Copy </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        </div>-->
            <table class="table table-striped table-bordered" id="datatablebychargetab">
                <thead>
                    <tr>
                        <th title="chargecode"> <bean:message key="bl.chargecode" bundle="<%=clientcode%>"/></th>
                        <th title="chargedesc" > <bean:message key="bl.chargedesc" bundle="<%=clientcode%>"/></th>
                        <th title="baserate" > <bean:message key="bl.baserate" bundle="<%=clientcode%>"/></th>
                        <th title="baseratequantity"> <bean:message key="bl.baseratequantity" bundle="<%=clientcode%>"/></th>
                        <th title="amount" > <bean:message key="bl.amount" bundle="<%=clientcode%>"/></th>
                        <th title="currency" > <bean:message key="bl.currency" bundle="<%=clientcode%>"/></th>
                        <th title="equipmentType" > <bean:message key="bl.equipmentType" bundle="<%=clientcode%>"/></th>
                        <th title="baserateunit" > <bean:message key="bl.baserateunit" bundle="<%=clientcode%>"/></th>
                        <th title="paymentmode" > <bean:message key="bl.paymentmode" bundle="<%=clientcode%>"/></th>
                        <th title="paylocation" > <bean:message key="bl.paylocation" bundle="<%=clientcode%>"/></th>
                        <th title="payby" > <bean:message key="bl.payby" bundle="<%=clientcode%>"/></th>
                    </tr>
                </thead>
                <tbody>
                    <%

                        BLChargeVo charge = null;
                        if (Chargedetails != null && Chargedetails.size() > 0) {
                            for (int i = 0; i < Chargedetails.size(); i++) {
                                charge = (BLChargeVo) Chargedetails.get(i);
                    %>
                    <tr>
                        <td><%=charge.getChargeId()%></td>
                        <td><%=charge.getChargeName()%></td>
                        <td><%=charge.getBaseRate()%></td>
                        <td><%=charge.getChargeRoe()%></td>
                        <td><%=charge.getChargeAmount()%></td>
                        <td><%=charge.getCurrencyId()%></td>
                        <td><%=charge.getEqpTypeId()%></td>
                        <td><%=charge.getBaseRateUnit()%></td>
                        <td >
                           <%=("P".equalsIgnoreCase(charge.getPrepaidCollect()) ? "Prepaid" : 
                            ("C".equalsIgnoreCase(charge.getPrepaidCollect()) ? "Collect" : 
                            ("E".equalsIgnoreCase(charge.getPrepaidCollect()) ? "PayAt" : "")))%>
                        </td>
                        <td><%=charge.getPayAgencyId()%></td>
                        <td > <%=charge.getCustTypeId()%></td>
                    </tr>
                    <%}
                        }
                    %>
                </tbody>
            </table>
    </div>
</fieldset> 

<%}%>
