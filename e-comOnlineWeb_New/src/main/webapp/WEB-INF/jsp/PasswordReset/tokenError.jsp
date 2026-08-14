<%@page import="net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    if (clientcode == null) {
        clientcode = "default";
    }

    // Get error message
    String errorMsg = (String) request.getAttribute("ErrorMsg");
    long jsdate = new java.util.Date().getTime();
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String logo_name = (String) getServletContext().getAttribute(GeneralConf.Logo_Name);
    String logo = imagepath + logo_name;
    String icoimage = imagepath + "ico.ico";
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));

    String footercopyright = (String) getServletContext().getAttribute("footercompanyname");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%=page_title%></title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="X-XSS-Protection" content="1; mode=block">
        <link rel="shortcut icon" type="image/x-icon" href="<%=icoimage%>?<%=jsdate%>"/>

        <link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i%7COpen+Sans:300,300i,400,400i,600,600i,700,700i" rel="stylesheet">

        <!-- Bootstrap 4 CSS -->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap.min.css?<%=jsdate%>" />
        <!-- Font Awesome for icons -->
        <link type="text/css" href="Design/app-assets/fonts/font-awesome/css/font-awesome.min.css" rel="stylesheet"  />

        <style>
            body {
                background: #f3f4f6;
                min-height: 100vh;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .reset-center {
                min-height: 100vh;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
            }
            .reset-card {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 8px 32px rgba(0,0,0,0.12);
                max-width: 400px;
                width: 100%;
                margin: 32px 0 16px 0;
                padding: 0;
                border: none;
            }
            .reset-logo {
                display: flex;
                flex-direction: column;
                align-items: center;
                padding-top: 32px;
            }
            .reset-logo img {
                max-width: 180px;
                margin-bottom: 8px;
            }
            .reset-tab {
                width: 100%;
                text-align: center;
                border-bottom: 1px solid #e0e0e0;
                margin-top: 18px;
                margin-bottom: 0;
                position: relative;
            }
            .reset-tab-label {
                display: inline-block;
                background: #fff;
                color: #888;
                font-weight: 600;
                font-size: 1.1rem;
                padding: 0 18px;
                position: relative;
                top: 12px;
                z-index: 2;
            }
            .reset-card-body {
                padding: 32px 32px 24px 32px;
            }
            .alert {
                border-radius: 8px;
                border: none;
                font-size: 1rem;
            }
            .btn-primary {
                background: #1dbfc1;
                border: none;
                border-radius: 8px;
                padding: 12px 0;
                font-size: 1.15rem;
                font-weight: 600;
                width: 100%;
                margin-top: 8px;
                margin-bottom: 8px;
                transition: background 0.2s;
            }
            .btn-primary:hover {
                background: #159fa1;
            }
            .copyright {
                text-align: center;
                color: #999;
                font-size: 1rem;
                margin-top: 18px;
                margin-bottom: 12px;
            }
            @media (max-width: 600px) {
                .reset-card { margin: 0.5rem; }
                .reset-card-body { padding: 18px 6px 18px 6px; }
            }
        </style>
    </head>
    <body>
        <div class="reset-center">
            <div class="reset-card">
                <div class="reset-logo">
                    <img src="<%=logo%>?<%=jsdate%>" alt="Logo" />
                </div>
                <div class="reset-tab">
                    <span class="reset-tab-label">Password Reset</span>
                </div>
                <div class="reset-card-body">
                    <% if (errorMsg != null && !errorMsg.isEmpty()) {%>
                    <div class="alert alert-danger" role="alert">
                        <i class="fa fa-exclamation-triangle mr-2"></i>
                        <%= errorMsg%>
                    </div>
                    <% } %>
                    
                    <div class="text-center">
                        <p class="text-muted mb-4">
                            The password reset link you used is invalid or has expired. 
                            Please request a new password reset link.
                        </p>
                        
                        <a href="<%= request.getContextPath() %>/Accesslogin.do?type=forgot" class="btn btn-primary">
                            <i class="fa fa-key mr-2"></i>Request New Reset Link
                        </a>
                        
                        <a href="<%= request.getContextPath() %>/Welcome.do" class="btn btn-secondary mt-2" style="width:100%;">
                            <i class="fa fa-home"></i> Back to Home
                        </a>
                    </div>
                </div>
            </div>
            <div class="copyright">
                Copyright © <%=footercopyright%>
            </div>
        </div>
    </body>
</html> 