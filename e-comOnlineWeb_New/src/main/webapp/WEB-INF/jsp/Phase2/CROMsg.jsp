<%-- 
    Document   : CROMsg
    Created on : Feb 12, 2018, 5:37:33 PM
    Author     : deepa.p
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
    String msg = (String) request.getAttribute("croprintstatus");
    System.out.println("[ECOM-WAR] CROMsg msg>>"+msg);
    String responseType = (String) request.getAttribute("responetype");
    String viewType = (String) request.getAttribute("viewType");
%>
<input type="hidden" id="cromsgres" value="<%=msg%>"/>
<input type="hidden" id="responseType" value="<%=responseType%>"/>
<input type="hidden" id="viewtype" value="<%=viewType%>"/>