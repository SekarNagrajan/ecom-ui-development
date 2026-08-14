<%-- 
    Document   : CargoProtectPDF
    Created on : 2 Feb, 2024, 5:32:24 PM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.common.GeneralConf"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String SelectedLanguage = (String) session.getAttribute("SelectedLanguage");
    String pdfFilename = "Cargo Protect_Roulette.pdf";
    if ("CH".equalsIgnoreCase(SelectedLanguage)) {
        pdfFilename = "CHI Cargo Protect Brochure_Slots.pdf";
    } else {
        pdfFilename = "Cargo Protect_Roulette.pdf";
    }
    String jsno = (String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO);
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/schdule.png" /> <bean:message key="ecom.cargoprotect" bundle="<%=clientcode%>"/>  </div>

<object
    data="/Ecommaster/<%=pdfFilename%>?<%=jsno%>"
    type="application/pdf"
    width="100%"
    height="100%"
    id="Ecom_PDF_Viewer"
    ></object>
