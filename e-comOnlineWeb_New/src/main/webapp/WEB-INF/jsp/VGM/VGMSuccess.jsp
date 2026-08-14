<%-- 
    Document   : VGMSuccess
    Created on : Jul 7, 2016, 8:55:45 PM
    Author     : deepa.p
--%>
<%@page import="net.ecom.common.GeneralConf"%>
<%@page import="net.ecom.common.Validation"%>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<% String clientcode = (String) session.getAttribute("clientcode");
    String restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
    String restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
    String fileSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
%>
<div class="row">

    <div class="col-sm-2"> </div>
    <div class="col-sm-8">

        <div class="box_sess_clr"> 
            <div class=" text-center">

                <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/sub_page.svg" /> </div>
            </div>

            <div class="text-center" >
                <h3 style="color: #6C7077;font-size: 23px;">  <bean:message key="vgm.msg.detsubsuc" bundle="<%=clientcode%>"/></h3>
                <div style="font-size: 18px;">   <bean:message key="vgm.msg.gotovgm" bundle="<%=clientcode%>"/> <a href="ecomvgm.do" onclick="backtovgm();" style="text-decoration: underline;">    <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/></a>. </div>
                <%if (restrictedFiled.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrict' bundle="<%=clientcode%>"/>(<%=restrictedFiled%>)</div>
                <%}%>
                <%if (restrictedFiledSize.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrictsize' bundle="<%=clientcode%>"/><%=fileSize%> (<%=restrictedFiledSize%>)</div>
                <%}%>
            </div>

        </div>

    </div>

    <div class="col-sm-2"> </div>

</div>
