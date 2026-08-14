<%--
    Document   : termsCondition
    Created on : Nov 30, 2011, 9:37:43 AM
    Author     : prabakaran.s
--%>

<%@page import="net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String icoimage = imagepath + "ico.ico";
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" type="image/x-icon" href="<%=icoimage%>">
        <title><%=page_title%></title>
    </head>
    <%
        String term = (String) request.getAttribute("TERMS");
    %>
    <body>

        <form > <table><tr><td><%=term%></td></tr></table></form>
    </body>
</html>
