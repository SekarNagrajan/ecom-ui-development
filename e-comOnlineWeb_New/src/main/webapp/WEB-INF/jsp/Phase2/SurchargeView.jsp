<%@page import="java.util.LinkedHashMap"%>
<%@page import="net.ecom.common.GeneralConf"%>
<%@ page language="java" import="net.ecom.common.Currencyformat,net.ecom.common.Validation,java.util.List,net.ecom.phase2.service.vo.SurchargeSummaryVO,java.util.Hashtable,java.util.Map" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page pageEncoding="UTF-8"%>
<%try {
        String eqptype = (String) request.getAttribute("surchargeeqptype");
        eqptype = (eqptype != null && (!"".equalsIgnoreCase(eqptype))) ? eqptype.trim() : "";
        String pol = (String) request.getAttribute("surchargepol");
        pol = (pol != null && (!"".equalsIgnoreCase(pol))) ? pol.trim() : "";
        String pod = (String) request.getAttribute("surchargepod");
        pod = (pod != null && (!"".equalsIgnoreCase(pod))) ? pod.trim() : "";
        String clientcode = (String) session.getAttribute("clientcode");
%>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="surchargedatatable">
        <thead>
            <%    String surchargeDetails = (String) getServletContext().getAttribute(GeneralConf.surcharge_details);
                System.out.println("[ECOM-WAR] SurchargeView surchargeDetails is" + surchargeDetails);
                if ("N".equalsIgnoreCase(surchargeDetails)) {%>
            <tr>
                <th><bean:message key='ecom.surcharge.chargename' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.chargecode' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.origin' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.pol' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.pod' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.delivery' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.cargotype' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.currency' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.amount' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.nor' bundle="<%=clientcode%>"/></th>
            </tr>

        </thead>

        <tbody>

            <%
                String amount = "";
                Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                LinkedHashMap eqptypelist = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                List<SurchargeSummaryVO> summaryList = (List<SurchargeSummaryVO>) request.getAttribute("SURCHARGE_LIST");
                System.out.println("[ECOM-WAR] SurchargeView >>>summaryList>>" + summaryList);
                SurchargeSummaryVO summaryVo = null;
                if (summaryList != null) {
                    int size = summaryList.size();
                    for (int i = 0; i < size; i++) {
                        summaryVo = (SurchargeSummaryVO) summaryList.get(i);
                        amount = (summaryVo.getAmount() != null) ? summaryVo.getAmount().trim() : "";
                        if (amount != null && amount.length() > 0) {
                            amount = Currencyformat.rupeeFormat(summaryVo.getAmount());
                        } else {
                            amount = amount;
                        }
            %>
            <tr>
                <td><%=Validation.checkNull(summaryVo.getChargename())%></td>
                <td><%=Validation.checkNull(summaryVo.getChargecode())%></td>
                <td><%=Validation.checkNull(summaryVo.getOrigin() + " - " + (String) portlist.get(Validation.checkNull(summaryVo.getOrigin())))%></td>
                <td><%=Validation.checkNull(summaryVo.getLoadRegion() + " - " + (String) portlist.get(Validation.checkNull(summaryVo.getLoadRegion())))%></td>
                <td><%=Validation.checkNull(summaryVo.getDischargeRegion() + " - " + (String) portlist.get(Validation.checkNull(summaryVo.getDischargeRegion())))%></td>
                <td><%=Validation.checkNull(summaryVo.getDelivery() + " - " + (String) portlist.get(Validation.checkNull(summaryVo.getDelivery())))%></td>
                <td><%=Validation.checkNull((String) eqptypelist.get(Validation.checkNull(summaryVo.getEqptype())))%></td>
                <td><%=Validation.checkNull(summaryVo.getCurrency())%></td>
                <td><%=amount%></td>
                <td><%=Validation.checkNull(summaryVo.getNor())%></td>
            </tr>
            <%}
                }

            } else {%>
            <tr>
                <th><bean:message key='ecom.surcharge.chargename' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.pol' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.pod' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.cargotype' bundle="<%=clientcode%>"/></th>                
                <th><bean:message key='ecom.surcharge.currency' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.amount' bundle="<%=clientcode%>"/></th>
            </tr>

            </thead>

        <tbody>

            <%
                String amount = "";
                Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
               LinkedHashMap eqptypelist = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                List<SurchargeSummaryVO> summaryList = (List<SurchargeSummaryVO>) request.getAttribute("SURCHARGE_LIST");
                System.out.println("[ECOM-WAR] SurchargeView >>>summaryList>>" + summaryList);
                SurchargeSummaryVO summaryVo = null;
                if (summaryList != null) {
                    int size = summaryList.size();
                    for (int i = 0; i < size; i++) {
                        summaryVo = (SurchargeSummaryVO) summaryList.get(i);
                        amount = (summaryVo.getAmount() != null) ? summaryVo.getAmount().trim() : "";
                        if (amount != null && amount.length() > 0) {
                            amount = Currencyformat.rupeeFormat(summaryVo.getAmount());
                        } else {
                            amount = amount;
                        }
            %>
            <tr>
                <td><%=Validation.checkNull(summaryVo.getChargename())%></td>
                <td><%=Validation.checkNull(summaryVo.getLoadRegion() + " - " + (String) portlist.get(Validation.checkNull(summaryVo.getLoadRegion())))%></td>
                <td><%=Validation.checkNull(summaryVo.getDischargeRegion() + " - " + (String) portlist.get(Validation.checkNull(summaryVo.getDischargeRegion())))%></td>
                <td><%=Validation.checkNull((String) eqptypelist.get(Validation.checkNull(summaryVo.getEqptype())))%></td>
                <td><%=Validation.checkNull(summaryVo.getCurrency())%></td>
                <td><%=amount%></td>
            </tr>
            <%}
                    }

                }%>


        </tbody>
    </table>
</div>




<%} catch (Exception e) {
        e.printStackTrace();
    }%>

<script type="text/javascript">
    $(document).ready(function () {
        $('#surchargedatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });

</script>
<span class="field_error"> <html:errors property="etype"/></span>
<span class="field_error"> <html:errors property="plname"/></span>
<span class="field_error"> <html:errors property="pdname" /> </span>
