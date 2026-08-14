<%-- 
    Document   : Cargo_Protect
    Created on : 19 Jan, 2024, 10:47:11 AM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.common.GeneralConf"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<%
    String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);
    String clientcode = (String) session.getAttribute("clientcode");
    String enablenewblink = (String) getServletContext().getAttribute(GeneralConf.Enable_New_Text_blink);
    if ("Y".equalsIgnoreCase(enableCargoProtect)) {
%>
<h6>
    <i class="step-icon fa_preview"></i><bean:message key="booking.cargo.additionalproductandcharges" bundle="<%=clientcode%>" /> 
    <%if ("Y".equalsIgnoreCase(enablenewblink)) {%>
    <span class="ecom_new_future">New</span>
    <%}%>
</h6>

<fieldset>
    <div class="txt_box_book" id="bkg_cargoprotect">
        <html:hidden  styleId="cargoprotectid"  property="cargoprotectid" />
        <input type="hidden" id="temp_cargoprotectid"/>
        <div class="form-group row">
            <div class="col-sm-12">
                <span id="selected_tier" style="display: none">
                    <h4>Selected Tier:</h4>
                    <hr> 
                </span>
                <div id="cargoprotect_table_div" style="display: none">

                </div> 
            </div>
        </div>
        <div class="form-group row">

            <div class="col-sm-12">
                <span id="available_tier" style="display: none">
                    <h4>Available Tier(s):</h4>
                    <hr>
                </span>
                <div id="cargoprotect_table_available_div" style="display: none">

                </div> 
            </div>
        </div>
    </div>
</fieldset>

<%
    }
%>