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

    // Get token from URL parameter or from validated token attribute
    String token = request.getParameter("token");
    if (token == null) {
        token = (String) request.getAttribute("token");
    }
    if (token == null) {
        token = "";
    }

    // Get error and success messages
    String errorMsg = (String) request.getAttribute("ErrorMsg");
    String successMsg = (String) request.getAttribute("SuccessMsg");
    long jsdate = new java.util.Date().getTime();
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String logo_name = (String) getServletContext().getAttribute(GeneralConf.Logo_Name);
    String logo = imagepath + logo_name;
    String icoimage = imagepath + "ico.ico";
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));

    String footercopyright = (String) getServletContext().getAttribute("footercompanyname");
    String passnote = Validation.checkNull((String) getServletContext().getAttribute("PasswordValidationNote"));
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
            .form-group {
                margin-bottom: 1.5rem;
            }
            .input-group-text {
                background: #f3f4f6;
                border: none;
                color: #888;
                font-size: 1.2rem;
            }
            .form-control {
                border-radius: 8px !important;
                border: 1px solid #e0e0e0;
                background: #f3f4f6;
                font-size: 1.08rem;
                padding: 12px 14px;
                transition: border-color 0.2s;
            }
            .form-control:focus {
                border-color: #1dbfc1;
                background: #fff;
                box-shadow: none;
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
            .alert {
                border-radius: 8px;
                border: none;
                font-size: 1rem;
            }
            .invalid-feedback {
                display: block;
                color: #e53935;
                font-size: 0.98rem;
                margin-top: 0.25rem;
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
            .tooltip-inner {
                max-width: 350px !important;
                min-width: 250px;
                white-space: pre-line;
                text-align: left;
                background: #22cfcf !important;
                color: #fff !important;
                font-size: 1.00rem !important;
                padding: 18px 20px !important;
                border-radius: 8px !important;
                font-weight: normal !important;
                margin-top: 8px;
                box-shadow: none !important;
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
                    <span class="reset-tab-label">Reset Password</span>
                </div>
                <div class="reset-card-body">
                    <% if (errorMsg != null && !errorMsg.isEmpty()) {%>
                    <div class="alert alert-danger" role="alert">
                        <i class="fa fa-exclamation-triangle mr-2"></i>
                        <%= errorMsg%>
                    </div>
                    <% } %>
                    
                    <html:form styleId="forgotPasswordResetForm" method="post" action="forgotPasswordReset.do">
                        <input type="hidden" name="token" value="<%= token%>" />
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa fa-lock"></i></span>
                                </div>
                                <html:password property="newPassword" styleId="newPassword" styleClass="form-control PasswordValidationNote" maxlength="20" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa fa-lock"></i></span>
                                </div>
                                <html:password property="confirmPassword" styleId="confirmPassword" styleClass="form-control" maxlength="20" />
                            </div>
                            <div class="invalid-feedback" id="passwordMatchError" style="display:none;">
                                Passwords do not match.
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="resetBtn">
                            <i class="fa fa-lock mr-2"></i>Reset Password
                        </button>
                        <a href="<%= request.getContextPath() %>/Welcome.do" class="btn btn-secondary mt-2" style="width:100%;">
                            <i class="fa fa-home"></i> Back to Home
                        </a>
                    </html:form>
                    
                </div>
            </div>
            <div class="copyright">
                Copyright © <%=footercopyright%>
            </div>
        </div>
        <script type="text/javascript" type="text/javascript" src="Design/app-assets/vendors/js/vendors.min.js"></script>
        
        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/jquery.validate.min.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/additional-methods.js?<%=jsdate%>"></script>
        <script>
            
            function loadtypeaheadtooltipmsg() {
                var enablePasswordValidation = '<%= "Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_Password_Validation)) ? "Y" : "N" %>';
                if (enablePasswordValidation === 'Y') {
                    $('.PasswordValidationNote').attr('data-toggle', 'tooltip');
                    $('.PasswordValidationNote').attr('data-placement', 'left');
                    $('.PasswordValidationNote').attr('data-html', 'true');
                    $('.PasswordValidationNote').attr('title', '<%=passnote%>');
                    $('[data-toggle="tooltip"]').tooltip();
                }
            }

            // Function to validate token on page load
            // This ensures that any direct access to this page with a token parameter
            // will be validated before the page is displayed
            function validateTokenOnLoad() {
                var urlParams = new URLSearchParams(window.location.search);
                var token = urlParams.get('token');
                
                // Check if we're coming from validation action (no token in URL but token attribute exists)
                var validatedToken = '<%= token != null && !token.isEmpty() ? "true" : "false" %>';
                
                if (token && token.trim() !== '' && validatedToken === 'false') {
                    // Redirect to token validation action only if not already validated
                    window.location.href = '<%= request.getContextPath() %>/validatePasswordResetToken.do?token=' + encodeURIComponent(token);
                }
            }

            $(document).ready(function () {
                // Validate token on page load
                validateTokenOnLoad();
                
                loadtypeaheadtooltipmsg();
                
                // Initialize jQuery validation with password strength validation
                $("#forgotPasswordResetForm").validate({
                    rules: {
                        newPassword: {
                            required: true,
                            checkPassword: "#newPassword"
                        },
                        confirmPassword: {
                            required: true,
                            equalTo: "#newPassword"
                        }
                    },
                    messages: {
                        newPassword: {
                            required: "Please enter a new password"
                        },
                        confirmPassword: {
                            required: "Please confirm your password",
                            equalTo: "Passwords do not match"
                        }
                    },
                    errorElement: 'div',
                    errorClass: 'invalid-feedback',
                    highlight: function(element) {
                        $(element).addClass('is-invalid');
                    },
                    unhighlight: function(element) {
                        $(element).removeClass('is-invalid');
                        // Remove the error message when valid
                        $(element).nextAll('.invalid-feedback').remove();
                    },
                    errorPlacement: function(error, element) {
                        $(element).nextAll('.invalid-feedback').remove();
                        error.insertAfter(element);
                    },
                    onkeyup: false,
                    onclick: false,
                    onfocusout: function(element) {
                        this.element(element);
                    },
                    submitHandler: function(form) {
                        form.submit();
                    }
                });
            });
        </script>
    </body>
</html> 