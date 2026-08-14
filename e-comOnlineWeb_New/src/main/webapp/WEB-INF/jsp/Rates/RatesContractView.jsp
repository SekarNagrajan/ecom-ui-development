<%@page import="java.util.LinkedHashMap,net.ecom.common.GeneralConf,net.ecom.common.MaskPort,net.ecom.common.utils.MaskPortUtil"%>
<%@page language="java" import="java.text.DecimalFormat,net.ecom.common.Validation,java.util.List,java.util.Hashtable,net.ecom.vo.RatesContractsVO,java.util.Map" errorPage="" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String clientcode = (String) (String) session.getAttribute("clientcode");
    System.out.println("[ECOM-WAR] RatesContractView tariff view>>>");
    String contractActionDisplay = (String) getServletContext().getAttribute(GeneralConf.RateContractActionDisplay);
    String carriercode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String currencyUnit = (String) getServletContext().getAttribute(GeneralConf.currencyunitvalue);

%>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="ratecontracttable">
        <thead>
            <tr>
                <th><bean:message key='ebooking.trust.Sno' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.origin' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.delivery' bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.rr.ratenum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.contract.eqptype' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.contract.commodity' bundle="<%=clientcode%>"/></th>
                    <%if ("GWF".equalsIgnoreCase(carriercode)) {%>
                <th><bean:message key='ebooking.ocean.freight' bundle="<%=clientcode%>"/></th>
                    <%} else {%>
                <th><bean:message key='rates.head.agreedrate' bundle="<%=clientcode%>"/></th>
                    <%}%>
                <th><bean:message key='rates.head.subtocharges' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='rates.head.soc' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='rates.head.carrterms' bundle="<%=clientcode%>"/></th>

                <%if ("Y".equalsIgnoreCase(contractActionDisplay)) {%>
                <th><bean:message key="ecom.shippingins.action" bundle="<%=clientcode%>" /></th>
                    <%}%>
            </tr>
        </thead>
        <tbody>
            <%
                Hashtable commoditylist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                LinkedHashMap eqptypelist = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                List<RatesContractsVO> summaryList = (List<RatesContractsVO>) request.getAttribute("RATECONTRACTLIST");
                RatesContractsVO summaryVo = null;
                String ofr = null, origin = null, delivery = null, originPort = null, pol = null, deliveryPort = null, pod = null, subToCharges = null, subToChargesfinal = null;
                DecimalFormat formatter = new DecimalFormat("0.00");
                if (summaryList != null) {
                    int size = summaryList.size();
                    int sno = 0;
                    for (int i = 0; i < size; i++) {
                        subToChargesfinal = null;
                        summaryVo = (RatesContractsVO) summaryList.get(i);
                        ofr = summaryVo.getOfr();
                        ofr = formatter.format(Double.parseDouble(ofr));
                        if (summaryVo.getSurAmount() != null && summaryVo.getSurAmount() != "" && summaryVo.getSurAmount().length() > 0) {
                            subToCharges = Validation.checkNull(summaryVo.getSurAmount());
                            subToChargesfinal = subToCharges.equals("0.0") ? "" : (formatter.format(Double.parseDouble(subToCharges)));
                        }

                        originPort = Validation.checkNull(summaryVo.getOriginPort());
                        pol = Validation.checkNull(summaryVo.getLoadPort());
                        deliveryPort = Validation.checkNull(summaryVo.getDeliveryPort());
                        pod = Validation.checkNull(summaryVo.getDischPort());
                        origin = originPort.length() > 0 ? originPort : pol;
                        delivery = deliveryPort.length() > 0 ? deliveryPort : pod;
                        boolean isMaskPort = MaskPort.isMaskPorts(origin + "," + delivery, getServletContext());
                        if (isMaskPort) {
                            sno = sno + 1;
            %>
            <tr>
                <td><%=(sno)%></td>
                <td><%=Validation.checkNull(MaskPortUtil.getDisplayPortName(origin,(String)portlist.get(origin)))%></td>
                <td><%=Validation.checkNull(MaskPortUtil.getDisplayPortName(delivery,(String)portlist.get(delivery)))%></td>
                <td><%=Validation.checkNull(summaryVo.getRateNo())%></td>
                <td><%=Validation.checkNull((String) eqptypelist.get(Validation.checkNull(summaryVo.getEqpType())))%></td>
                <td ><%=Validation.checkNull((String) commoditylist.get(Validation.checkNull(summaryVo.getCommodity())))%></td>
                <td><%=currencyUnit%> &nbsp;<%=ofr%></td>
                <% if (subToChargesfinal != null && subToChargesfinal != "" && subToChargesfinal.length() > 0) {%>
                <td><%=currencyUnit%>  &nbsp;<%=subToChargesfinal%></td>
                <%} else {%>
                <td></td>
                <%}%>
                <td><%=summaryVo.getSoc()%></td>
                <td><%=summaryVo.getCarrTerms()%></td>
                <%if ("Y".equalsIgnoreCase(contractActionDisplay)) {%>
                <td><a href="javascript:void(0)" onclick="rateContractSurchargeView('<%=summaryVo.getRateNo()%>', '<%=summaryVo.getItemNo()%>', '<%=summaryVo.getRatesType()%>')" title='View'
                       class="btn btn-lg text-success" data-toggle="tooltip" data-placement="right">
                        <i class="fa fa-search"></i></a>
                </td>
                <%}%>
            </tr>
            <%
                        }
                    }
                }

            %>

        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#ratecontracttable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });

</script>


