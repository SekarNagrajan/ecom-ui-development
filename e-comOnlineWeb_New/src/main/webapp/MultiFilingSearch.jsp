<%-- 
    Document   : MultiFilingSearch
    Created on : 17 Dec, 2024, 10:09:21 AM
    Author     : jayashree.muthukumar
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" import="net.ecom.common.Validation, net.ecom.vo.CustomerProfile,java.util.ArrayList,java.util.List,net.ecom.common.GeneralConf,java.util.Hashtable" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div class="table-responsive card card-body card-shadow">
    <%
        String clientcode = (String) session.getAttribute("clientcode"); 
    %>
    <table class="table table-striped table-bordered" id="multiFilingCustomerTable">
        <thead>
            <tr>
                <th><bean:message key="bl.action" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="shipping.ens.declarant.name" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="shipping.ens.declarant.address" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="shipping.ens.declarant.address1" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="shipping.ens.declarant.city" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="shipping.ens.declarant.zipcode" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="shipping.ens.declarant.country" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="shipping.ens.declarant.state" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="shipping.ens.declarant.telephone" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="shipping.ens.declarant.eori" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="shipping.ens.declarant.email" bundle="<%=clientcode%>" /></th>
                <th><bean:message key="shipping.ens.declarant.fillingtype" bundle="<%=clientcode%>" /></th>
            </tr>
        </thead>
        <tbody>
            <%
                List<CustomerProfile> customerList = (List<CustomerProfile>) request.getAttribute("MULTIFILINGSEARCH"); 
                CustomerProfile custpro = null;
                if (customerList != null ) {
                     for (int i = 0; i < customerList.size(); i++) {
                          custpro = (CustomerProfile) customerList.get(i);
                          custpro.setCustName(Validation.checkNull(custpro.getCustName()));
                          custpro.setAddr1(Validation.checkNull(custpro.getAddr1()));
                          custpro.setAddr2(Validation.checkNull(custpro.getAddr2()));
                          custpro.setCity(Validation.checkNull(custpro.getCity()));
                          custpro.setZipcode(Validation.checkNull(custpro.getZipcode()));
                          custpro.setCountry(Validation.checkNull(custpro.getCountry()));
                          custpro.setState(Validation.checkNull(custpro.getState()));
                          custpro.setTeleNo(Validation.checkNull(custpro.getTeleNo()));
                          custpro.setEmailId(Validation.checkNull(custpro.getEmailId()));
                          custpro.setEnseori(Validation.checkNull(custpro.getEnseori()));
                          custpro.setFilingtype(Validation.checkNull(custpro.getFilingtype()));
            %>
            <tr>
                <td>                                                                                                                                                                                                                                                                                                              
                    <a href="javascript:void(0)" onclick="setMultifilingDetails('<%=custpro.getCustName()%>', '<%=custpro.getAddr1()%>', '<%=custpro.getAddr2()%>', '<%=custpro.getCountry()%>', '<%=custpro.getTeleNo()%>', '<%=custpro.getEnseori()%>', '<%=custpro.getEmailId()%>', '<%=custpro.getCity()%>', '<%=custpro.getState()%>' , '<%=custpro.getFilingtype()%>' , '<%=custpro.getZipcode()%>' )"  title="select" class="btn btn-warning btn-sm text-white"> Select</a>
                </td>
                <td><%=custpro.getCustName()%></td>
                <td><%=custpro.getAddr1()%></td>
                <td><%=custpro.getAddr2()%></td>
                <td><%=custpro.getCity()%></td>
                <td><%=custpro.getZipcode()%></td>
                <td><%=custpro.getCountry()%></td>
                <td><%=custpro.getState()%></td>
                <td><%=custpro.getTeleNo()%></td>
                <td><%=custpro.getEnseori()%></td>
                <td><%=custpro.getEmailId()%></td>
                <td><%=custpro.getFilingtype()%></td>
            </tr> 
             <%       }
                }
            %>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#multiFilingCustomerTable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>
