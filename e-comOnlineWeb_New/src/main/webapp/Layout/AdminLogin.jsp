<%-- 
    Document   : CpanelLayout
    Created on : Sep 4, 2019, 2:55:42 PM
    Author     : gobinath.t
--%>
<%@page contentType="text/html" errorPage="../SessionExpired.jsp" pageEncoding="UTF-8"%>
<%@page import="net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE html>
<html lang="en">
    <!-- BEGIN: Head-->
    <%
        response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
        response.setHeader("Pragma", "no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server

        long jsdate = new java.util.Date().getTime();
        String imagepath = (String) getServletContext().getAttribute("Imagepath");
        String logo_name = (String) getServletContext().getAttribute(GeneralConf.Logo_Name);
        String logo = imagepath + logo_name;
        String icoimage = imagepath + "ico.ico";
        String footercopyright = (String) getServletContext().getAttribute("footercompanyname");
        String copyrightslink = (String) getServletContext().getAttribute(GeneralConf.AGENCYLINK);
        String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
        String removeCopyrightsLink = (String) getServletContext().getAttribute(GeneralConf.REMOVE_COPYRIGHTS_LINK);
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" type="image/x-icon" href="<%=icoimage%>?<%=jsdate%>"/>
        <title> <%=page_title%> </title>
        <link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i%7COpen+Sans:300,300i,400,400i,600,600i,700,700i" rel="stylesheet">

        <!-- BEGIN: Vendor CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/vendors.min.css?<%=jsdate%>"/>
        <!--        <link rel="stylesheet" type="text/css" href="../../../app-assets/vendors/css/forms/icheck/icheck.css">-->
        <!--        <link rel="stylesheet" type="text/css" href="../../../app-assets/vendors/css/forms/icheck/custom.css">-->
        <!-- END: Vendor CSS-->

        <!-- BEGIN: Theme CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap.min.css?<%=jsdate%>" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap-extended.min.css?<%=jsdate%>" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/colors.min.css?<%=jsdate%>" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/components.min.css?<%=jsdate%>" />
        <!-- END: Theme CSS-->

        <!-- BEGIN: Page CSS-->
        <!--        <link rel="stylesheet" type="text/css" href="../../../app-assets/css/core/menu/menu-types/vertical-menu-modern.css">
                <link rel="stylesheet" type="text/css" href="../../../app-assets/css/core/colors/palette-gradient.min.css">-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/pages/login-register.min.css?<%=jsdate%>">
        <!-- END: Page CSS-->

        <!-- BEGIN: Custom CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/fonts/simple-line-icons/style.min.css?<%=jsdate%>" />
        <!-- END: Custom CSS-->
    </head>
    <!-- END: Head-->

    <!-- BEGIN: Body-->
    <body class="vertical-layout vertical-menu-modern 1-column   blank-page blank-page" data-open="click" data-menu="vertical-menu-modern" data-col="1-column">
        <!-- BEGIN: Content-->
        <div class="app-content content">
            <div class="content-wrapper">
                <div class="content-header row">
                </div>
                <div class="content-body"><section class="flexbox-container">
                        <div class="col-12 d-flex align-items-center justify-content-center">
                            <div class="col-lg-4 col-md-8 col-10 box-shadow-2 p-0">
                                <div class="card border-grey border-lighten-3 m-0">
                                    <div class="card-header border-0">
                                        <div class="card-title text-center">
                                            <div class="p-1"><img style="max-height:60px;" src="<%=logo%>?<%=jsdate%>" alt="logo"></div>
                                        </div>
                                        <h6 class="card-subtitle line-on-side text-muted text-center font-small-3 pt-2"><span>Login</span></h6>
                                    </div>
                                    <div class="card-content">
                                        <div class="card-body">
                                            <html:form styleId="lg-form" method="post" action="Accesslogin.do?type=adminlogin" styleClass="form-horizontal form-simple">
                                                <div class="form-group alert-error" id="loginerror" style="display:none">
                                                    <div class="alert alert-danger alert-dismissible" role="alert">
                                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                        <strong>Warning!</strong> Invalid Username / Password </div>
                                                </div>
                                                <fieldset class="form-group position-relative has-icon-left mb-0">
                                                    <html:text property="userName" styleId="username" maxlength="50" styleClass = "form-control form-control-lg required" />
                                                    <div class="form-control-position">
                                                        <i class="ft-user"></i>
                                                    </div>
                                                </fieldset>
                                                <fieldset class="form-group position-relative has-icon-left">
                                                    <html:password property="password" styleId="password" maxlength="50" styleClass="form-control form-control-lg required"  />
                                                    <div class="form-control-position">
                                                        <i class="fa fa-key"></i>
                                                    </div>
                                                </fieldset>

                                                <button type="submit" class="btn btn-primary btn-lg btn-block"><i class="ft-unlock"></i> Login</button>
                                            </html:form>
                                        </div>
                                    </div>
                                    <div class="card-footer">
                                        <div class="text-center">
                                            <p class="m-0">
                                               <% if(!("Y").equalsIgnoreCase(removeCopyrightsLink)) { %>
                                               <a target="_blank" style="color: #818a91;" href="<%=copyrightslink%>" > <% } %>
                                                     <span class="d-none d-lg-block"> Copyright &copy; <%=footercopyright%>   </span> 
                                                     <% if(!("Y").equalsIgnoreCase(removeCopyrightsLink)) { %> </a> <% } %>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>

                </div>
            </div>
        </div>
        <!-- END: Content-->


        <!-- BEGIN: Vendor JS-->
        <script src="Design/app-assets/vendors/js/vendors.min.js?<%=jsdate%>"></script>
        <!-- BEGIN Vendor JS-->

        <!-- BEGIN: Page Vendor JS-->
        <!--        <script src="../../../app-assets/vendors/js/forms/icheck/icheck.min.js"></script>
                <script src="../../../app-assets/vendors/js/forms/validation/jqBootstrapValidation.js"></script>-->
        <!-- END: Page Vendor JS-->

        <!-- BEGIN: Theme JS-->
        <!--        <script src="../../../app-assets/js/core/app-menu.min.js"></script>
                <script src="../../../app-assets/js/core/app.min.js"></script>-->
        <!-- END: Theme JS-->

        <!-- BEGIN: Page JS-->
        <script src="Design/app-assets/js/scripts/forms/form-login-register.min.js?<%=jsdate%>"></script>
        <!-- END: Page JS-->
        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/jquery.validate.min.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/additional-methods.js?<%=jsdate%>"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#username").focus();
                $("#username").attr('placeholder', 'Username');
                $('#password').attr('placeholder', 'Password');
                $("#lg-form").validate();
            });

        </script>
        <%
            String result = (String) request.getAttribute("LOGINRESPONSE_DATA");
            if (result != null && result.length() > 0) {
        %>
        <script type="text/javascript">
            $('#loginerror').show();
        </script>
        <% }%>

    </body>
    <!-- END: Body-->
</html>