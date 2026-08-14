<%@page import="net.ecom.common.Validation,net.ecom.common.GeneralConf"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@page import="java.util.Hashtable"%>
<%
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String icoimage = imagepath + "ico.ico";
    String footerpowerby = "";
    String footercopyright = "", Version = "";
    footerpowerby = (String) getServletContext().getAttribute("footerpowerby");
    footercopyright = (String) getServletContext().getAttribute("footercompanyname");
    Version = (String) getServletContext().getAttribute("Version");
    footerpowerby = (footerpowerby != null) ? footerpowerby : "";
    footercopyright = (footercopyright != null) ? footercopyright : "";
    Version = (Version != null) ? Version : "";
    session.invalidate();
    long jsdate = new java.util.Date().getTime();
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
    String autoredirect = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Enable_Auto_Redirect_To_Homepage));
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title> <%=page_title%> </title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" href="<%=icoimage%>?<%=jsdate%>"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/vendors/css/vendors.min.css?<%=jsdate%>"/>
        <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap.min.css?<%=jsdate%>" />
        <link rel="stylesheet" type="text/css" href="Design/assets/css/style.css?<%=jsdate%>" />
        <link rel="stylesheet" type="text/css" href="Design/assets/css/ecom.css?<%=jsdate%>" />
        <link type="text/css" href="Design/assets/css/login.css?<%=jsdate%>" rel="stylesheet"/>
        <script type="text/javascript" src="Design/app-assets/vendors/js/vendors.min.js?<%=jsdate%>"></script>
    </head>
    <body>
        <div class="container-fluid">
            <!-- session exprired msg start  -->
            <div class="row">

                <div class="col-sm-2"> </div>
                <div class="col-sm-8">
                    <div class="box_sess_clr"> 
                        <div class=" text-center" style=" ">

                            <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/expired.png" /> </div>
                        </div>

                        <div class="text-center" style=" ">
                            <h3 class="ses_exp" style=" ">  Session Expired  </h3>

                            <div style="font-size: 20px; padding-bottom: 8px;"> Your login session has expired due to the inactive. </div>

                            <div style="font-size: 18px;"> <a href="Welcome.do" style="text-decoration: underline;"> Click here </a> to login.</div>
                            <%if ("Y".equalsIgnoreCase(autoredirect)) {%>
                            <div id="dvCountDown" style="display: none;font-size: 16px; padding-top: 8px;">
                                You will be redirected to the home page after <span id="lblCount"></span>&nbsp;seconds.
                            </div>
<!--                            <div id="dvCountDown1" style="display: none;font-size: 16px; padding-top: 2px;">
                                If not redirected, please click the Click here link.
                            </div>-->
                            <%}%>
                        </div>

                    </div>

                </div>

                <div class="col-sm-2"> </div>

            </div>


            <!-- session exprired msg end  -->
            <footer class="footer">
                <span class="text-right">
                    Version:&nbsp;<%=Version%>
                </span>
                <span class="float-right">
                    <a target="_blank" href="#" style="font-size: 11px;"><b> Copyright © <%=footercopyright%> </b></a>
                </span>
            </footer>
        </div>
    </body>
</html>
<%if ("Y".equalsIgnoreCase(autoredirect)) {%>
<script type="text/javascript">
    $(function () {
        var seconds = 5;
        $("#dvCountDown,#dvCountDown1").show();
        $("#lblCount").html(seconds);
        setInterval(function () {
            seconds--;
            $("#lblCount").html(seconds);
            if (seconds == 0) {
                $("#dvCountDown,#dvCountDown1").hide();
                window.location = "Welcome.do";
            }
        }, 1000);
    });
</script>
<%}%>