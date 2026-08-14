<%-- 
    Document   : BookingHaulageCustomerDetails
    Created on : Jan 29, 2019, 4:57:24 PM
    Author     : gobinath.t
--%>

<%@page  contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.Map,java.util.List,java.util.Hashtable,java.util.Date,net.ecom.vo.CustomerDetailsVO" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!--/* Carrier table */-->
<div class="table-responsive card card-body card-shadow">
    <%
        String clientcode = (String) session.getAttribute("clientcode");
        String type = (String) request.getAttribute("type");
        Map stateMap = (Map) getServletContext().getAttribute("SESSION_STATEMAP");
        Map stateMapCountry = null;
        Hashtable countryHashObj = (Hashtable) getServletContext().getAttribute("SESSION_COUNTRYOBJECT");
        if ("customer".equalsIgnoreCase(type)) {
    %>
    <table class="table table-striped table-bordered" id="haulagecustomertable">
        <thead>
            <tr>
                <th><bean:message key="bl.action" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="bl.customername.display" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="bl.adresscust.display" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="ecom.bkg.contctper" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.bkg.contctphone" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.bkg.contctmail" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="bl.city.display" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="Reg.State" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="bl.country.display" bundle="<%=clientcode%>" /></th>
            </tr>
        </thead>
        <tbody>
            <%
                List<CustomerDetailsVO> custlis = (List) request.getAttribute("HaulageCustomerDetails");
                CustomerDetailsVO cusvo = null;
                String state = null, city = null, country = null, address = null;
                if (custlis != null) {
                    for (int i = 0; i < custlis.size(); i++) {
                        cusvo = (CustomerDetailsVO) custlis.get(i);
                        cusvo.setAddress(cusvo.getAddress().replace("\n", " "));
                        cusvo.setAddress(cusvo.getAddress().replace(" ", " "));
                        city = Validation.checkNull(cusvo.getCity());
                        state = Validation.checkNull(cusvo.getState());
                        country = Validation.checkNull(cusvo.getCountryCode());
                        if (country != null && country.trim().length() > 0) {
                            stateMapCountry = (Map) stateMap.get(Validation.checkNull(country).toUpperCase());
                            country = country + " - " + Validation.checkNull((String) countryHashObj.get(country));
                        }
                        if (state != null && state.trim().length() > 0) {
                            state = state + " - " + Validation.checkNull((String) stateMapCountry.get(state));
                        }
                        address = cusvo.getAddress();
                        //address = address+",\r\n"+city;
                        //address= address+(Validation.checkNull(cusvo.getState()).length()>0?(",\r\n"+cusvo.getState()):"");
            %>
            <tr>
                <td>
                    <a href="javascript:void(0)" onclick="setHaulageCustomerDetails('<%=cusvo.getCustCode()%>', '<%=cusvo.getName()%>', '<%=address%>', '<%=cusvo.getPhoneNo()%>', '<%=cusvo.getEmail()%>', '<%=cusvo.getContactperson()%>', '<%=city%>', '<%=Validation.checkNull(cusvo.getState())%>')"  title="select" class="btn btn-warning btn-sm text-white"> Select</a>
                </td>
                <td><%=cusvo.getName()%></td>
                <td><%=cusvo.getAddress()%></td>
                <td><%=cusvo.getContactperson()%></td>
                <td><%=cusvo.getPhoneNo()%></td>
                <td><%=cusvo.getEmail()%></td>
                <td><%=city%></td>
                <td><%=state%></td>
                <td><%=country%></td>

            </tr>
            <%       }
                }
            %>

        </tbody>
    </table>
    <% } else if ("contact".equalsIgnoreCase(type)) {%>
    <table class="table table-striped table-bordered" id="haulagecustomertable">
        <thead>
            <tr>
                <th><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="ecom.bkg.contctper" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.bkg.contctphone" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.bkg.contctmail" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="bl.action" bundle="<%=clientcode%>" /></th>
            </tr>
        </thead>
        <tbody>
            <%
                List<CustomerDetailsVO> conlis = (List) request.getAttribute("HaulageContactDetails");
                CustomerDetailsVO convo = null;
                if (conlis != null) {
                    for (int i = 0; i < conlis.size(); i++) {
                        convo = (CustomerDetailsVO) conlis.get(i);

            %>
            <tr>
                <td><%=i + 1%></td>
                <td><%=convo.getName()%></td>
                <td><%=convo.getPhoneNo()%></td>
                <td><%=convo.getEmail()%></td>
                <td>
                    <a href="javascript:void(0)" onclick="setHaulageContactDetails('<%=convo.getName()%>', '<%=convo.getPhoneNo()%>', '<%=convo.getEmail()%>')"  title="select" class="btn btn-warning btn-xs text-white"> Select</a>
                </td>
            </tr>
            <%       }
                }
            %>

        </tbody>
    </table>
    <%}%>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#haulagecustomertable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>
