<%-- 
    Document   : RatesViewNew
    Created on : Sep 25, 2019, 4:17:30 PM
    Author     : gobinath.t
--%>
<%@page import="net.ecom.common.GeneralConf"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,java.util.ArrayList,java.util.List,net.ecom.vo.Equipments,java.util.Map,net.ecom.vo.RateVo,net.ecom.common.Validation,java.util.Hashtable,net.ecom.common.utils.MaskPortUtil"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
    List ratelis = (List) session.getAttribute("RATESDETAILS");
    String clientcode = (String) (String) session.getAttribute("clientcode");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String isNorExists=(String) request.getAttribute("IsNorValueExists");
    if (ratelis != null && ratelis.size() > 0) {%>
<div class="table-responsive card card-body card-shadow">
    <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
    <table class="table table-striped table-bordered" id="ratesdatatable">
        <thead>
            <tr>
                <th><bean:message key='ecom.surcharge.origin' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.surcharge.delivery' bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.rr.ratenum" bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.shipmentdate' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.eqptype' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.tariff.commodity' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ebooking.ocean.freight' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='ecom.rates.taf.addcharges' bundle="<%=clientcode%>"/></th>
                <%if ("Y".equalsIgnoreCase(isNorExists)) {%>
                <th><bean:message key='ecom.surcharge.nor' bundle="<%=clientcode%>"/></th>
                <%}%>
                <th><bean:message key='rates.head.soc' bundle="<%=clientcode%>"/></th>
                <th><bean:message key='rates.head.carrterms' bundle="<%=clientcode%>"/></th>

            </tr>
        </thead>
        <tbody>
            <%
                String currencyUnit = (String) getServletContext().getAttribute(GeneralConf.currencyunitvalue);
                Hashtable commoditylist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                LinkedHashMap eqptypelist = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                RateVo ratevo = null;
                for (int i = 0; i < ratelis.size(); i++) {
                ratevo = (RateVo) ratelis.get(i);
               
             %>
            <tr>
                <td><%=Validation.checkNull(MaskPortUtil.getDisplayPortName(ratevo.getOriginPort(),(String)portlist.get(Validation.checkNull(ratevo.getOriginPort()))))%></td>
                <td><%=Validation.checkNull(MaskPortUtil.getDisplayPortName(ratevo.getDeliveryPort(),(String)portlist.get(Validation.checkNull(ratevo.getDeliveryPort()))))%></td>
                <td><%=Validation.checkNull((String) ratevo.getRateNo())%></td>
                <td><%=Validation.checkNull((String) ratevo.getShipmentdate())%></td>
                <td><%=Validation.checkNull((String) eqptypelist.get(Validation.checkNull(ratevo.getEqptype())))%></td>
                <td><%=Validation.checkNull((String) commoditylist.get(Validation.checkNull(ratevo.getCommodity())))%></td>
               <td><%=currencyUnit%> &nbsp; <%=Validation.checkNull((String) ratevo.getAmount())%></td>
                <% if(ratevo.getSubToCharges()!=null && ratevo.getSubToCharges()!="" && ratevo.getSubToCharges().length()>0){%>
                <td><%=currencyUnit%> &nbsp; <%=Validation.checkNull(ratevo.getSubToCharges())%>
                <%}else{%>
                <td><%=Validation.checkNull(ratevo.getSubToCharges())%>
                    <%}%>
                 <%if ("Y".equalsIgnoreCase(isNorExists)) {%>
                <td><%=Validation.checkNull((String) ratevo.getNor())%></td>
                <%}%>
                <td><%=Validation.checkNull((String) ratevo.getSoc())%></td>
               <td><%=Validation.checkNull((String) ratevo.getCarrTerms())%></td>


            </tr>
            <%
                }

            %>

        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#ratesdatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });

</script>

<%} else {%>
<div class="page_bottom_content">
    <div class="col-sm-12">
        <!--No results msg start  -->
        <div class="row">
            <div class="col-sm-1"> </div>
            <div class="col-sm-10">
                <div class="box_no_rls"> 
                    <div class="text-center" style=" ">
                        <h2 class="txt_no_msg" style=""> <bean:message key="rates.msg.nores" bundle="<%=clientcode%>"/> </h2>
                        <button type="submit"  onclick="navigateToQuote();" class="btn btn_block_quote" ><bean:message key="ecom.rr.reqforrate" bundle="<%=clientcode%>"/></button>
                    </div>
                </div>
            </div>
            <div class="col-sm-2"> </div>
        </div>
        <!--No results msg end  -->
    </div>
</div>
<%}%>

<script>

    //toggle icon for the rates

    $(document).ready(function () {

        $('.rates_toggle-btn').click(function () {
            $("i", this).toggleClass('fa-minus fa-plus');
        });

    });

</script>

