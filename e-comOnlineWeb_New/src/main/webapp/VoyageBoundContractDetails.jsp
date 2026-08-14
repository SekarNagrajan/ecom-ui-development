<%-- 
    Document   : VoyageBoundDetails
    Created on : Oct 6, 2021, 2:47:40 PM
    Author     : gobinath.t
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="net.ecom.common.Validation,java.util.List,java.util.Map" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%
    String type = (String) request.getAttribute("VoyageBoundContractType");
    if ("VoyageBound".equalsIgnoreCase(type)) {
%>
<table class="table table-striped table-bordered" id="voyageboundtable">
    <thead>
        <tr>
            <th>S.No</th>
            <th>Voyage</th>
            <th>Bound</th>
            <th>Select </th>
        </tr>
    </thead>
    <tbody>
        <%
            List voylis = (List) request.getAttribute("VoyageBoundDetails");
            if (voylis != null) {
                Map voymap = null;
                String voyage = null, bound = null;
                for (int i = 0; i < voylis.size(); i++) {
                    voymap = (Map) voylis.get(i);
                    voyage = Validation.checkNull((String) voymap.get("voyagecode"));
                    bound = Validation.checkNull((String) voymap.get("bound"));
        %>
        <tr>
            <td><%=i + 1%></td>
            <td><%=voyage%></td>
            <td><%=bound%></td>
            <td>
                <a href="javascript:void(0)" onclick="setVoyageBoundDetails('<%=voyage%>', '<%=bound%>');"  title="select" class="btn btn-warning btn-xs text-white"> Select</a>
            </td>
        </tr>
        <%       }
            }
        %>

    </tbody>
</table>

<script type="text/javascript">
    $(document).ready(function () {
        $('#voyageboundtable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []

        });

    });
</script>

<%} else { %>
<table class="table table-striped table-bordered" id="contracttable">
    <thead>
        <tr>
            <th>S.No</th>
            <th>Contract Number</th>
            <th>Amendment Number</th>
            <th>Select </th>
        </tr>
    </thead>
    <tbody>
        <%
            List contlis = (List) request.getAttribute("ContractDetails");
            if (contlis != null) {
                Map contmap = null;
                String contractno = null, amendmentno = null;
                for (int i = 0; i < contlis.size(); i++) {
                    contmap = (Map) contlis.get(i);
                    contractno = Validation.checkNull((String) contmap.get("rateno"));
                    amendmentno = Validation.checkNull((String) contmap.get("amendmentno"));
        %>
        <tr>
            <td><%=i + 1%></td>
            <td><%=contractno%></td>
            <td><%=amendmentno%></td>
            <td>
                <a href="javascript:void(0)" onclick="setContractDetails('<%=contractno%>', '<%=amendmentno%>');"  title="select" class="btn btn-warning btn-xs text-white"> Select</a>
            </td>
        </tr>
        <%       }
            }
        %>

    </tbody>
</table>

<script type="text/javascript">
    $(document).ready(function () {
        $('#contracttable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []

        });

    });
</script>
<%}%>