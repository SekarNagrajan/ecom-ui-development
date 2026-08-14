<%@page import="java.util.ArrayList"%>
<%@page import="net.ecom.vo.HotlineContactsVO"%>
<%@page contentType="text/html" errorPage="../SessionExpired.jsp" pageEncoding="UTF-8"%>
<%@page import="net.ecom.common.GeneralConf,net.ecom.common.Validation,java.util.List,java.util.Iterator,java.util.Map,java.util.LinkedHashMap,java.util.TreeMap"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib prefix="tiles" uri="/WEB-INF/struts-tiles.tld"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<!DOCTYPE html>
<%
    response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
    response.addHeader("X-XSS-Protection", "1; mode=block");
    response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
    response.setHeader("Pragma", "no-cache"); //HTTP 1.0
    response.setDateHeader("Expires", 0); //prevents caching at the proxy server

    session.setAttribute("OBLPRINTACCESS", "NO");
    response.addHeader("X-FRAME-OPTIONS", "DENY");
    response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");

    long jsdate = new java.util.Date().getTime();
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String logo_name = (String) getServletContext().getAttribute(GeneralConf.Logo_Name);
    String logo = imagepath + logo_name;
    String icoimage = imagepath + "ico.ico";
    //String loginForMenu = (String) session.getAttribute("LOGINFORMENU");
    //session.setAttribute("LOGINFORMENU", null);
    //System.out.println("[ECOM-WAR] login_Mobile ####- loginForMenu -=-=-=-=====> " + loginForMenu);
    String selectedlan = (String) session.getAttribute("SelectedLanguage");
    System.out.println("[ECOM-WAR] login_Mobile selectedlangis" + selectedlan);
    String clientcode = (String) session.getAttribute("clientcode");
    String isViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
    String strUserName = (String) session.getAttribute("SESSION_USERNAME");
    String user = (String) session.getAttribute("sessionAdmin");
    String logintype = (String) session.getAttribute("SESSION_LOGIN_TYPE");
    String user_roll_admin = (String) session.getAttribute("USER_ROLL_ADMIN");
    String rateRegMenu = (String) getServletContext().getAttribute(GeneralConf.RATEREQMENU);
    String liveChatId = (String) getServletContext().getAttribute(GeneralConf.LIVE_CHAT_ID);
    /* if ("V".equalsIgnoreCase(logintype)) {
        strUserName = "Admin";
    }*/
    if (strUserName == null || strUserName.length() == 0) {
        strUserName = "Admin";
    }
    String result = (String) request.getAttribute("LOGINRESPONSE_DATA");
    System.out.println("[ECOM-WAR] login_Mobile logintype====> " + logintype + " logintype.length()====> " + logintype.length());
    String loginemailid = (String) session.getAttribute("SESSION_EMAIL");
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
    TreeMap HotlineContacts = (TreeMap) getServletContext().getAttribute("HotlineContacts");
    String enable_encryption = (String) getServletContext().getAttribute(GeneralConf.Enable_Login_Password_Encryption);
%>
<html lang="en">
    <head>
        <title><%=page_title%></title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" type="image/x-icon" href="<%=icoimage%>?<%=jsdate%>"/>


        <link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i%7COpen+Sans:300,300i,400,400i,600,600i,700,700i" rel="stylesheet">

        <!--  <link href="../assets/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" /> -->

        <!-- BEGIN: Vendor CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/vendors.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/extensions/unslider.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/weather-icons/climacons.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/fonts/meteocons/style.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/tables/datatable/datatables.min.css">

        <!-- END: Vendor CSS-->

        <!-- BEGIN: Theme CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap-extended.min.css" />
        <!--        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/colors.min.css" />-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/components.min.css" />
        <!-- END: Theme CSS-->

        <!-- BEGIN: Page CSS-->
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/core/menu/menu-types/vertical-menu-modern.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/core/menu/menu-types/vertical-overlay-menu.min.css">
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/core/colors/palette-gradient.min.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/fonts/simple-line-icons/style.min.css" />

        <!-- END: Page CSS-->


        <!-- BEGIN: Custom CSS-->
        <link type="text/css" href="Design/app-assets/fonts/font-awesome/css/font-awesome.min.css" rel="stylesheet"  />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/plugins/forms/wizard.min.css">
        <link rel="stylesheet" type="text/css" href="Design/assets/css/style.css?<%=jsdate%>" />
        <link rel="stylesheet" type="text/css" href="Design/assets/css/ecom.css?<%=jsdate%>" />
        <!--        <link rel="stylesheet" type="text/css" href="Design/assets/css/endless-style.css" />-->
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/autocomplete/typeahead.bundle.css"/>
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/datepicker/css/bootstrap-datepicker3.css" />
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/colorpicker/bootstrap-colorpicker.min.css"/>
        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/datepicker/css/bootstrap-datetimepicker.min.css" />


        <link type="text/css" rel="stylesheet" href="Design/assets/plugins/fancybox/jquery.fancybox.min.css"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/plugins/forms/switch.min.css" />
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/forms/toggle/switchery.min.css">
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/forms/icheck/icheck.css">
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/extensions/toastr.css">

        <link type="text/css" href="Design/assets/css/login.css?<%=jsdate%>" rel="stylesheet"/>
        <!-- END: Custom CSS-->



        <script type="text/javascript" type="text/javascript" src="Design/app-assets/vendors/js/vendors.min.js"></script>

        <!-- BEGIN: Page Vendor JS-->
        <!--        <script type="text/javascript" src="Design/app-assets/vendors/js/charts/raphael-min.js"></script>-->
        <!--        <script type="text/javascript" src="Design/app-assets/vendors/js/charts/morris.min.js"></script>-->
        <script type="text/javascript" src="Design/app-assets/vendors/js/extensions/unslider-min.js"></script>

        <!-- END: Page Vendor JS-->

        <script type="text/javascript">
            $(document).ready(function () {
                disableBack();
                window.onpageshow = function (evt) {
                    if (evt.persisted)
                        disableBack();
                }
            });
            function disableBack() {
                window.history.forward(1);
                window.history.forward(-1);
            }
        </script>


        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/datatable/datatables.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/datatable/dataTables.buttons.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/buttons.flash.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/jszip.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/pdfmake.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/vfs_fonts.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/buttons.html5.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/tables/buttons.print.min.js"></script>

        <!--        <script src="Design/app-assets/js/scripts/tables/datatables/datatable-advanced.min.js"></script>-->

        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/jquery.validate.min.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/forms/validation/additional-methods.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/assets/plugins/autocomplete/typeahead.bundle.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/datepicker/js/moment.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/datepicker/js/bootstrap-datepicker.min.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/fancybox/jquery.fancybox.min.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/NicEdit/nicEdit.js"></script>
        <script type="text/javascript" src="js/CommonValidation.js?<%=jsdate%>"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/extensions/jquery.steps.min.js"></script>
        <script type="text/javascript" src="Design/assets/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>
        <script type="text/javascript" src="Design/app-assets/vendors/js/extensions/toastr.min.js"></script>

        <!--        <script type="text/javascript" src="Design/assets/plugins/datepicker/js/bootstrap-datetimepicker.min.js"></script>-->
        <!--        <script type="text/javascript" src="Design/app-assets/js/scripts/ui/scrollable.min.js"></script>-->
        <%
            if ("en".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgsen.js?<%=jsdate%>"></script>

        <%            }%>
        <%
            if ("zh".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgsch.js?<%=jsdate%>"></script>

        <%            }%>
        <%
            if ("ma".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgsma.js?<%=jsdate%>"></script>

        <%            }%>
        <%
            if ("es".equalsIgnoreCase(clientcode)) {
        %>
        <script type="text/javascript" src="js/validationMsgses.js?<%=jsdate%>"></script>

        <%            }%>

    </head>
    <body class="vertical-layout vertical-overlay-menu 2-columns fixed-navbar" data-open="click" data-menu="vertical-overlay-menu" data-col="2-columns">
        <!--    <body class="vertical-layout vertical-menu-modern 2-columns fixed-navbar"  data-menu="vertical-menu-modern" data-col="2-columns">-->
        <div id="processLoad" >
            <div class="page-loader-overlay-img page-loader-overlay-fixed" style="width: auto; height: auto; display: block;"></div>
            <div class="page-loader-overlay">
                <table>
                    <tbody style="text-align: center;">
                        <tr>
                            <td>
                                <div class="spinner-border text-info" style="width: 5rem; height: 5rem;" role="status">
                                    <span class="sr-only">Loading...</span>
                                </div>
                            </td>
                        </tr>
                        <tr style="text-align: center;">
                            <td style="text-align: center;font-size: 25px;">
                                Please wait
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="mySidenav" class="sidenav_right" style="width: 120%;top: 0;padding-left: 22px;font-size: 18px">
            <html:form styleId="loginform" method="post" action="Accesslogin.do?type=login" styleClass="form-horizontal">
                <div class="login_box" style="margin-top: 130px;margin-left: 84px !important;">
                    <div class="form-group row col-sm-12">
                        <img class="brand-logo" style="margin-left: 20%;" alt="logo" src="<%=logo%>?<%=jsdate%>">
                    </div>
                    <div class="form-group row col-sm-12" id="loginerror" style="display:none">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <div class="alert alert-danger text-center alert-dismissible fade show" role="alert">
                                <strong><bean:message key="ecom.login.invup" bundle="<%=clientcode%>"/></strong> 
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="txt_line" style="text-align: unset;margin-left: 40%;"><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></div>

                    <div class="row form-group col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <html:text property="userName" styleId="username" maxlength="50" styleClass = "input_box1 form-control required" />
                        </div>
                    </div>

                    <div class="row form-group col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <html:password property="password" styleId="loginpassword" maxlength="20" styleClass="input_box1 form-control required" />
                        </div>
                    </div>

                    <div class="row col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <button class="login_btn" id="loginbtn" type="submit" autocomplete="off"><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></button>
                        </div>
                    </div>

                    <div class="row form-group col-sm-12">
                        <div class="col-sm-2 "> </div>
                        <div class="col-sm-10">
                            <span class="forget_txt">  <a href ="Accesslogin.do?type=forgot"> <bean:message key="ForgottenPassword.Header.ForgotPassword" bundle="<%=clientcode%>"/> </a> </span> <br/>
                            <span class="dont_txt"> <bean:message key="ecom.login.havingnoacc" bundle="<%=clientcode%>"/>  <a href ="Accesslogin.do?type=Registration"  class="rem_txt" > <bean:message key="ecom.toregister" bundle="<%=clientcode%>"/> </a> </span>
                        </div>
                    </div>
                </div>

            </html:form>
        </div>


    </body>
    <a href="" id="return-to-top" style="z-index: 3;"> <i class="fa fa-arrow-up"></i> </a>
    <!-- BEGIN: Theme JS-->
    <script src="Design/app-assets/js/core/app-menu.min.js"></script>
    <script src="Design/app-assets/js/core/app.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/toggle/bootstrap-checkbox.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/toggle/switchery.min.js"></script>
    <script src="Design/app-assets/js/scripts/forms/switch.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/icheck/icheck.min.js"></script>
    <script src="Design/app-assets/js/scrollreveal.min.js"></script>
    <script src="Design/app-assets/js/modernizr.min.js"></script>
    <script src="Design/app-assets/js/scripts/forms/checkbox-radio.min.js"></script>
    <script src="Design/app-assets/vendors/js/forms/repeater/jquery.repeater.min.js"></script>
    <script src="Design/app-assets/js/scripts/forms/form-repeater.min.js"></script>
    <script src="Design/app-assets/js/hover.js"></script>
    <!-- END: Theme JS-->
    <script type="text/javascript">
            $(document).ready(function () {
                $('#username').attr('placeholder', '<bean:message key="ViewProfile.userifo.UserName" bundle="<%=clientcode%>"/>');
                $('#loginpassword').attr('placeholder', '<bean:message key="login.password" bundle="<%=clientcode%>"/>');
            });
            // SHA-256 encryption function
            async function sha256(message) {
                const msgBuffer = new TextEncoder().encode(message);
                const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
                const hashArray = new Uint8Array(hashBuffer);
                let binary = '';
                hashArray.forEach((byte) => binary += String.fromCharCode(byte));
                return btoa(binary);  // Base64 encoding
            }

            $("#loginform").validate({
                submitHandler: function (form) {
                    const password = document.getElementById('loginpassword').value;
                    const username = document.getElementById('username').value;

                    // Hash password synchronously using SubtleCrypto
                    sha256(password).then(hashedPassword => {
                        // Submit manually with hashed password
        <%if (!"Y".equalsIgnoreCase(enable_encryption)) {%>
                        hashedPassword = password;
        <%}%>
                        $.ajax({
                            url: "LoginAjax",
                            type: 'post',
                            data: {
                                userName: username,
                                password: hashedPassword
                            },
                            success: function (data) {
                                if (data == 'error') {
                                    $('#loginform').find('#loginerror').show();
                                    $('#loginform').find('#loginerror strong').html('Error Occurred!');
                                } else if (data == 'invalid') {
                                    $('#loginform').find('#loginerror').show();
                                    $('#loginform').find('#loginerror strong').html('Invalid Username / Password');
                                } else {
                                    window.location.href = data;
                                }
                            },
                            error: function (xhr) {
                                if (xhr.status == 403) {
                                    window.location.href = "SessionExpired.jsp";
                                }
                            }
                        });
                    });

                    return false; // Prevent default submit
                }
            });
            function openLoader() {
                $("#processLoad").show();
            }
            function closeLoader() {
                $("#processLoad").hide();
            }
            closeLoader();
    </script>
</html>
