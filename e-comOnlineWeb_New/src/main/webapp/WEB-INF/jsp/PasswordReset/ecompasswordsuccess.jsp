<%@page import="net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
    String icoimage = imagepath + "ico.ico";
    long jsdate = new java.util.Date().getTime();
    session.invalidate();
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

            <div class="row">

                <div class="col-sm-2"> </div>
                <div class="col-sm-8">

                    <div class="box_sess_clr" style="margin-top: 18%;"> 
                        <div class=" text-center">

                            <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/sub_page.svg" /> </div>
                        </div>

                        <div class="text-center" >

                            <h3 style="color: #6C7077;font-size: 23px;"><p id="error-desc" ><bean:message key="Res.successh1" bundle="<%=clientcode%>"/></p></h3>
                            <h5 style="color: #6C7077;font-size: 17px;"><p id="error-desc"><bean:message key="Res.successh2" bundle="<%=clientcode%>" /></p></h5>
                            <div style="color: #6C7077;font-size: 16px;"> <a href="Welcome.do" style="text-decoration: underline;"> <bean:message key="ecom.msg.vgm.clickhere" bundle="<%=clientcode%>"/> </a></a><bean:message key="ecom.msg.tologin" bundle="<%=clientcode%>"/></div>


                        </div>

                    </div>

                </div>

                <div class="col-sm-2"> </div>

            </div>

        </div>
    </body>
</html>