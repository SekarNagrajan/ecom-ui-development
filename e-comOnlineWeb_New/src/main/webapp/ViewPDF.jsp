<%@page contentType="text/html" import="java.io.File,javax.servlet.ServletOutputStream"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    </head>
    <body>
        <%
                    System.out.println("[ECOM-WAR] ViewPDF ViewPDF JSP ==================> ");
                    byte[] bytePdfArray = (byte[]) session.getAttribute("jasperbyte");
                    if (bytePdfArray != null && bytePdfArray.length > 0) {
                        response.setContentType("application/pdf");
                        response.setHeader("Content-Disposition", "filename=ARNNO1.pdf");
                        ServletOutputStream sos = response.getOutputStream();
                        sos.write(bytePdfArray);
                        sos.flush();
                        sos.close();
                        out.clear();
                        out = pageContext.pushBody();
                    }
        %>
    </body>
</html>
