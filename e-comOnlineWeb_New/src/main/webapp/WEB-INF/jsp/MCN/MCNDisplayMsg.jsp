<%@ page contentType="text/html"  pageEncoding="UTF-8"  language="java" import="net.ecom.vo.ReturnVO" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@page import="java.util.*,net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%-- 
    Document   : MCNDisplayMsg
    Created on : 4 Apr, 2024, 7:23:55 PM
    Author     : prakash.mani
--%>
<script language="javascript">
    function backtomcnview() {
        location.href = "mcn.do";
    }
</script>
<div class="row">

    <div class="col-sm-2"> </div>
    <div class="col-sm-8">

        <div class="box_sess_clr"> 
            <div class=" text-center">

                <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/sub_page.svg" /> </div>
            </div>

            <div class="text-center" >
                <% String insurancemsg = Validation.checkNull((String) request.getAttribute("InsuranceMSGMessage"));
                    String clientcode = (String) session.getAttribute("clientcode");
                    String restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
                    String restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
                    String fileSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
                    ReturnVO returnVO = (ReturnVO) request.getAttribute("SI_RESPONSE_VO");
                    String strResponseMsg = "";
                    if (returnVO != null && (!"Bill Of Lading not saved.".equalsIgnoreCase(returnVO.getStrDisplayMsg()))) {
                        strResponseMsg = returnVO.getStrDisplayMsg();
                        System.out.println("[ECOM-WAR] MCNDisplayMsg strResponseMsg---- From MCN---------->"+strResponseMsg);

                %>
                <h3 style="color: #6C7077;font-size: 23px;"><bean:message key="ecom.msg.mcn" bundle="<%=clientcode%>" /></h3>

                <div style="font-size: 18px;"> <bean:message key="mcn.msg.gotomcn" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtomcnview();" style="text-decoration: underline;">  <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/> </a> .</div>
                <%} else if (returnVO != null && ("Bill Of Lading not saved.".equalsIgnoreCase(returnVO.getStrDisplayMsg()))) {%>

                <h3 style="color: #6C7077;font-size: 23px;"><%=returnVO.getStrDisplayMsg()%></h3>

                <div style="font-size: 18px;"> <bean:message key="mcn.msg.gotomcn" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtomcnview();" style="text-decoration: underline;">  <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/> </a> .</div>
                <%} else {%>
                <h3 style="color: #6C7077;font-size: 23px;"><bean:message key="ecom.msg.bl.error" bundle="<%=clientcode%>"/></h3>

                <div style="font-size: 18px;"> <bean:message key="mcn.msg.gotomcn" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtomcnview();" style="text-decoration: underline;">  <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/></a> <bean:message key=" bl.msg.latest" bundle="<%=clientcode%>"/></div>
                <%}%>
                <%if (restrictedFiled.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrict' bundle="<%=clientcode%>"/>(<%=restrictedFiled%>)</div>
                <%}%>
                 <%if (restrictedFiledSize.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrictsize' bundle="<%=clientcode%>"/><%=fileSize%> (<%=restrictedFiledSize%>)</div>
                <%}%>
                <%if (insurancemsg.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;" class="text-center"><%=insurancemsg%> </div>
                <%}%>

            </div>

        </div>

    </div>

    <div class="col-sm-2"> </div>

</div>
