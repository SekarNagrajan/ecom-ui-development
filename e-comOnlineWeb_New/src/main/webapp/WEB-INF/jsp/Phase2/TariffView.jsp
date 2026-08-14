<%@page import="java.util.LinkedHashMap"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" import="net.ecom.common.Currencyformat,net.ecom.common.utils.DateConversion_New,net.ecom.common.Validation,java.util.List,java.util.Hashtable,net.ecom.phase2.service.vo.TariffSummaryVO,java.util.Map,net.ecom.common.GeneralConf" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<link rel="stylesheet" type="text/css" href="newdesign/assets/plugins/data-tables/dataTables.bootstrap.css"/>
<%
            String eqptype = (String) request.getAttribute("eqptype");
            eqptype = (eqptype != null && (!"".equalsIgnoreCase(eqptype))) ? eqptype.trim() : "";
            String pol = (String) request.getAttribute("pol");
            pol = (pol != null && (!"".equalsIgnoreCase(pol))) ? pol.trim() : "";
            String pod = (String) request.getAttribute("pod");
            pod = (pod != null && (!"".equalsIgnoreCase(pod))) ? pod.trim() : "";
            String clientcode = (String) (String)session.getAttribute("clientcode");
            System.out.println("[ECOM-WAR] TariffView tariff view>>>");
            String showExternalRateTotalAmount = (String) getServletContext().getAttribute(GeneralConf.SHOW_EXTERNALRATE_TOTAL_AMOUNT);
            
%>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="tariffdatatable">
        <thead>
            <tr>
                <th><bean:message key='ecom.tariff.pol' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.pod' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.eqptype' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.commodity' bundle="<%=clientcode%>"/></th>
                <!--<th><bean:message key='ecom.tariff.tariffusd' bundle="<%=clientcode%>"/></th>-->
                <th><bean:message key='ecom.tariff.loccurrency' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.locamount' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.fromdate' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.todate' bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%

                        Hashtable commoditylist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                        Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                       LinkedHashMap eqptypelist = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                        List<TariffSummaryVO> summaryList = (List<TariffSummaryVO>) request.getAttribute("TARIFF_LIST");
                        TariffSummaryVO summaryVo = null;

                            if (summaryList != null) {
                                int size = summaryList.size();
                                for (int i = 0; i < size; i++) {
                                    String amount = "", tarriffamount = "";
                                    summaryVo = (TariffSummaryVO) summaryList.get(i);
                                    String fromdate = summaryVo.getFmdate();
                                    fromdate = (fromdate != null && (!"".equalsIgnoreCase(fromdate))) ? fromdate.trim() : "";
                                    String todate = summaryVo.getTodate();
                                    todate = (todate != null && (!"".equalsIgnoreCase(todate))) ? todate.trim() : "";

                                    tarriffamount = (summaryVo.getTariffamt()!= null) ? summaryVo.getTariffamt().trim() : "";
                                    System.out.println("[ECOM-WAR] TariffView yessssssssssssssssss:::::" + tarriffamount);
                                    if (tarriffamount != null && tarriffamount.length() > 0) {
                                        tarriffamount = Currencyformat.rupeeFormat(tarriffamount);
                                    } else {
                                        tarriffamount = tarriffamount;
                                    }
                                    if("Y".equalsIgnoreCase(showExternalRateTotalAmount)) {
                                        amount = (summaryVo.getExternalrate()!= null) ? summaryVo.getExternalrate().trim() : "";
                                        System.out.println("[ECOM-WAR] TariffView getExternalrate:::::" + amount);
                                    } else{
                                        amount = (summaryVo.getLocamt()!= null) ? summaryVo.getLocamt().trim() : "";
                                        System.out.println("[ECOM-WAR] TariffView getLocamt:::::" + amount);
                                    }  
                                    if (amount != null && amount.length() > 0) {
                                        amount = Currencyformat.rupeeFormat(amount);
                                        System.out.println("[ECOM-WAR] TariffView Currencyformat:::::" + amount);
                                    } else {
                                        amount = amount;
                                        System.out.println("[ECOM-WAR] TariffView amount:::::" + amount);
                                    }


                                   
            %>
                <td><%=Validation.checkNull((String) portlist.get(summaryVo.getLoadport()))%></td>
                <td><%=Validation.checkNull((String) portlist.get(summaryVo.getDischport()))%></td>
                <td><%=Validation.checkNull((String) eqptypelist.get(summaryVo.getEqptype()))%></td>
                <td ><%=Validation.checkNull((String) commoditylist.get(Validation.checkNull(summaryVo.getCommoditycode())))%></td>
                <!--<td><%=tarriffamount%></td>-->
                <td><%=summaryVo.getCurrency()%></td>
                <td><%=amount%></td>
                <td><%=DateConversion_New.convertDefaultTimeFormat(fromdate)%></td>
                <td><%=DateConversion_New.convertDefaultTimeFormat(todate)%></td>
            </tr>
            <%
                                }
                            }
                        
            %>

        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#tariffdatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });

</script>
<span class="field_error"> <html:errors property="etype"/></span>
<span class="field_error"> <html:errors property="plname"/></span>
<span class="field_error"> <html:errors property="pdname" /> </span>
