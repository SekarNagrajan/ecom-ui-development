<%-- 
    Document   : InsurancePolicyView
    Created on : Nov 17, 2021, 4:24:13 PM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.insurance.vo.PolicyGetOutput,java.util.List,net.ecom.insurance.vo.InsuredGood,net.ecom.common.Validation,java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<div class="card ecom_insurance" id="ecom_insurance">
    <div class="card-header" style="box-shadow: 0px 2px 3px 0px #e0e0e0;height: 62px;background-color: white;">
        <div class="form-group row">
            <div class="col-sm-4 float-left">
                <img class="brand-logo" alt="logo" src="/Ecommaster/SwissRe_Logo.png">
            </div>
            <div class="col-sm-8 float-right">
                <h3 class="col-sm-11 text-right" style="margin-right: 20px;color: #00bcd4;margin-top: 8px;"><b> <bean:message key="ecom.insurance.spot.voyage" bundle="<%=clientcode%>" /> </b></h3>
            </div>
        </div>
    </div>
    <div class="card-body">
        <%
            PolicyGetOutput policy = (PolicyGetOutput) request.getAttribute("PolicyViewDetails");
            List policylis = policy.getInsuredGoods();
        %>
        <div class="form-group row">
            <div class="col-sm-12">
                <label class="col-sm-2"> Policy Number: </label>
                <label class="col-sm-2"><%=policy.getPolicyNumber()%></label>
                <label class="col-sm-2"> Policy Type: </label>
                <label class="col-sm-2"><%=policy.getPolicyType()%></label>
                <label class="col-sm-2"> Status: </label>
                <label class="col-sm-2"><%=policy.getStatus()%></label>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12">
                <label class="col-sm-2"> Policy Start Date: </label>
                <label class="col-sm-2"><%=policy.getPolicyStartDate()%></label>
                <label class="col-sm-2"> Policy End Date: </label>
                <label class="col-sm-2"><%=policy.getPolicyEndDate()%></label>
                <label class="col-sm-2"> Online Booking Number: </label>
                <label class="col-sm-2"><%=policy.getOnlineBookingNumber()%></label>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12">
                <label class="col-sm-2"> Total Premium: </label>
                <label class="col-sm-2"><%=policy.getTotalPremium()%></label>
                <label class="col-sm-2"> Currency: </label>
                <label class="col-sm-2"><%=policy.getCurrency()%></label>
            </div>
        </div>
        <hr>
        <table class="table text-center">
            <thead class="thead-dark">
                <tr>
                    <th scope="col">Commodity / HS Code</th>
                    <th scope="col">Sum Insured</th>
                </tr>
            </thead>
            <tbody>
                <%
                    double suminsured = 0;
                    if (policylis != null && policylis.size() > 0) {
                        for (int i = 0; i < policylis.size(); i++) {
                            InsuredGood ig = (InsuredGood) policylis.get(i);
                            suminsured = suminsured + Double.parseDouble(ig.getSumInsured());
                %>
                <tr>
                    <td class="ecom_insurance_text"><%=ig.getHarmonizedSystemCode()%></td>
                    <td><%=ig.getSumInsured() + " " + ig.getCurrency()%></td>

                </tr>

                <%}%>
                <tr>
                    <td class="ecom_insurance_text">Total Amount</td>
                    <td style="color: #2196f3;font-weight: bolder;"><h4><%=suminsured + " " + policy.getCurrency()%></h4></td>
                </tr>
                <% }%>
            </tbody>
        </table>
    </div>
</div>
