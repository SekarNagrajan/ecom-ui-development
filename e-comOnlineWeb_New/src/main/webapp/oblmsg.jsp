<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    String msg = (String) request.getAttribute("SuccessMsg");
    String responseType = (String) request.getAttribute("responetype");
    String viewType = (String) request.getAttribute("viewType");
    String Blno = (String) request.getAttribute("BlNo");
   // String type = (String) request.getAttribute("printype");
    System.out.println("[ECOM-WAR] oblmsg viewType=>"+viewType+"///"+responseType);
%>
<input type="hidden" id="oblmsgres" value="<%=msg%>"/>
<input type="hidden" id="responseType" value="<%=responseType%>"/>
<input type="hidden" id="viewtype" value="<%=viewType%>"/>
<input type="hidden" id="originalBlno" value="<%=Blno%>"/>

