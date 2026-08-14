<%-- 
    Document   : UNPortList
    Created on : Dec 17, 2020, 7:01:52 PM
    Author     : gobinath.t
--%>


<%@page  contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.Map,java.util.List,java.util.Hashtable,java.util.Date,net.ecom.vo.PortVO" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!--/* Carrier table */-->
<div class="table-responsive card card-body card-shadow">
    <%
        String clientcode = (String) session.getAttribute("clientcode");
        String SIPayor =  (String) request.getAttribute("SIPayor");
        String SIfrieght =  (String) request.getAttribute("SIfrieght");
        List UNPortList = (List) request.getAttribute("UNPortList");
      

    %>
    <table class="table table-striped table-bordered" id="unporttable">
        <thead>
            <tr>
                <th>Port Code</th>
                <th>Port Name</th>
                <th><bean:message key="bl.action" bundle="<%=clientcode%>" /></th>
            </tr>
        </thead>
        <tbody>
            <%if(UNPortList!=null){
                PortVO vo=null;
                for(int i=0;i<UNPortList.size();i++){
                    vo = (PortVO) UNPortList.get(i);
               %>
            <tr>

                <td><%=vo.getPortCode()%></td>
                <td><%=vo.getPortName()%></td> 
                <td>
                    <a href="javascript:void(0)" onclick="setchargeDetails('<%=SIfrieght%>','<%=SIPayor%>','<%=vo.getPortCode()%>')"  title="select" class="btn btn-warning btn-sm text-white"> Select</a>
                </td>

            </tr>
            <%       }
                }
            %>

        </tbody>
    </table>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#unporttable').DataTable({
                lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
                "aaSorting": []
            });
        });
    </script>
