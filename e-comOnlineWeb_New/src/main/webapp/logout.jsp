<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
   session.invalidate();
   response.sendRedirect("Welcome.do");
%>


