<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ page import="java.util.*, org.apache.struts.util.MessageResources, org.apache.struts.action.ActionForm,net.ecom.common.GeneralConf" %>
<%@ page import=" net.ecom.vo.*, net.ecom.common.*, net.ecom.common.utils.DateConversion_New" %>

<%
try {
    long jsdate = new Date().getTime();
    String clientcode = (String) session.getAttribute("clientcode");
    String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
    System.out.println("TENS_Documentation preview ==> " + TENS_Documentation);
%>
<% if ("Y".equalsIgnoreCase(TENS_Documentation)) { %>

<div id='sld_hd'>
    <div class="row">
        <div class="col-md-12">
            <div class="normalpanel" id="sl_id">

                <!-- Seller Header -->
                <div class="custom-panel-head">
                    <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>
                </div>

                <!-- Seller Section -->
                <div id="sellerTypePreviewContainer"></div>
                <div id="sellerTypePreviewTemplate" style="display:none;" class="customer-preview ">
                <div class="row form-group">
                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Sellername_label" name="CustType_Sellername_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Selleradress_label" name="CustType_Selleradress_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Selleradress1_label" name="CustType_Selleradress1_label" disabled="true" class="form-control input-si">
                    </div>
                </div>

                <div class="row form-group">
                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Sellercity_label" name="CustType_Sellercity_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Sellercountry_label" name="CustType_Sellercountry_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Sellerphone_label" name="CustType_Sellerphone_label" disabled="true" class="form-control input-si">
                    </div>
                </div>

                <div class="row form-group">
                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Selleremail_label" name="CustType_Selleremail_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key="Reg.State" bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Sellerstate_label" name="CustType_Sellerstate_label" disabled="true" class="form-control input-si">
                    </div>
                </div>


            </div>

            </div>
        </div>
    </div>
</div>

<div class="normalpanel" id="by_id">
    
    <!-- Buyer Header -->
    <div class="custom-panel-head">
        <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>
    </div>

    <!-- Buyer Section -->
    <div id="buyerTypePreviewContainer"></div>
    <div id="customerTypePreviewTemplate" style="display:none;" class="customer-preview">
                <div class="row form-group">
                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyername_label" name="CustType_Buyername_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyeradress_label" name="CustType_Buyeradress_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyeradress1_label" name="CustType_Buyeradress1_label" disabled="true" class="form-control input-si">
                    </div>
                </div>

                <div class="row form-group">
                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyercity_label" name="CustType_Buyercity_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyercountry_label" name="CustType_Buyercountry_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyerphone_label" name="CustType_Buyerphone_label" disabled="true" class="form-control input-si">
                    </div>
                </div>

                <div class="row form-group">

                    <label class="col-sm-1 control-label">
                        <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyeremail_label" name="CustType_Buyeremail_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-1 control-label">
                        <bean:message key="Reg.State" bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <input type="text" id="CustType_Buyerstate_label" name="CustType_Buyerstate_label" disabled="true" class="form-control input-si">
                    </div>
                </div>


       </div>

</div>

<script type='text/javascript'>
    // Your JS if needed
</script>

<% } %>
<% } catch (Exception e) {
    e.printStackTrace();
} %>
