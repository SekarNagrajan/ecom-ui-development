<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="net.ecom.common.Validation,net.ecom.common.GeneralConf"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%
    long jsdate = new java.util.Date().getTime();
    session.setAttribute("RegValidate", "Send");
    session.setAttribute("NEWDESIGNPRODUCT", "registration");
    String term = (String) request.getAttribute("TERMS");
    String contextRoot = request.getContextPath();
    String errors = Validation.checkNull((String) session.getAttribute("regerrors"));
    String clientcode = (String) session.getAttribute("clientcode");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String mobileMandatory = (String) getServletContext().getAttribute(GeneralConf.MOBMANDATORY);
    String mobileReq = ("Y").equalsIgnoreCase(mobileMandatory) ? "required" : "";
    String POSTALCODE_OPTIONAL_COUNTRY = (String) getServletContext().getAttribute(GeneralConf.POSTALCODE_OPTIONAL_COUNTRY);
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    String toDisplayCustCode = (String) getServletContext().getAttribute(GeneralConf.DisplayRegCustCode);
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String toDisplayGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
    String toenableTimeZone = (String) getServletContext().getAttribute(GeneralConf.EnableTimeZone);
    String displayRegGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.RegGoogleCaptcha);
    displayRegGgleCaptcha = (displayRegGgleCaptcha != null ? displayRegGgleCaptcha.toUpperCase() : displayRegGgleCaptcha);
    String acceptOnlyEng = (String) getServletContext().getAttribute(GeneralConf.RegAcceptOnlyEng);
    String KYCfileupload = (String) getServletContext().getAttribute(GeneralConf.KYC_fileupload);
    String customerCodeMandatory = (String) getServletContext().getAttribute(GeneralConf.customercode_mandatory);
    String CUSTOMERCODE_REQ_URL = (String) getServletContext().getAttribute(GeneralConf.CUSTOMERCODE_REQ_URL);
    String enableGoogleAddressSearch = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Enable_Google_Address_Search));
    String googleMapsApiKeyConfigured = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Google_Maps_API_Key));
    boolean googleAddressSearchActive = "Y".equalsIgnoreCase(enableGoogleAddressSearch);
    String postalcode_required = "required", companyphone_required = "required";
    if ("Y".equalsIgnoreCase((String) getServletContext().getAttribute(GeneralConf.Registration_PostalCode_Optional))) {
        postalcode_required = "";
    }
    if ("Y".equalsIgnoreCase((String) getServletContext().getAttribute(GeneralConf.Registration_CompanyPhone_Optional))) {
        companyphone_required = "";
    }
    String ecomFileUpdSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
    String ecomFileExt = (String) getServletContext().getAttribute(GeneralConf.EcomFileExtension);
    String custValidationOnNext = (String) getServletContext().getAttribute(GeneralConf.REG_Cust_Validation_On_Next_Btn);
    String phoneNumberLength5 = (String) getServletContext().getAttribute(GeneralConf.PHONE_NUMBER_LENGTH_5);
    String maxLengthValue = "Y".equalsIgnoreCase(phoneNumberLength5) ? "5" : "4";
    String userPhoneMandatory = (String) getServletContext().getAttribute(GeneralConf.USER_PHONE_MANDATORY);
    String enable_contactTitle = (String) getServletContext().getAttribute(GeneralConf.Enable_contactTitle);
    String hideRegAddress2 = (String) getServletContext().getAttribute(GeneralConf.HIDE_REG_ADDRESS2);
    String hideRegRecentBL = (String) getServletContext().getAttribute(GeneralConf.HIDE_REG_RECENTBL);
    String allowexistingUsertoReg = (String) getServletContext().getAttribute(GeneralConf.ALLOW_EXISTING_USER_TO_REGISTER);
    String control_location_Non_Mandatory = (String) getServletContext().getAttribute(GeneralConf.Reg_Control_location_Non_Mandatory);
    String disableDefaultView = (String) getServletContext().getAttribute(GeneralConf.DISABLE_DEFAULT_VIEW);
    String disablePreferredView = (String) getServletContext().getAttribute(GeneralConf.DISABLE_PREFERRED_VIEW); 
    String enableCompanyMobile = (String) getServletContext().getAttribute(GeneralConf.ENABLE_COMP_MOBILE);
    String eitherCompMobOrPhoneMandatory = (String) getServletContext().getAttribute(GeneralConf.EITHER_COMP_MOB_OR_PHONE_MANDATORY);
    String EnableTaxId = (String) getServletContext().getAttribute(GeneralConf.Enable_TaxId);
    String enableCountryCode = (String) getServletContext().getAttribute(GeneralConf.ENABLE_COUNTRY_CODE);
    %>
<style type="text/css">
    /*    .tooltip-inner {
            background-color: #1FBBA6;
            color: #fff;
        }
        .tooltip.right .tooltip-arrow  {
            border-right-color: #1FBBA6;
            color: #fff;
        }*/
    select.form-control.light_border.user_title {
        background-position-x: 83% !important;
    }
</style>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />  <bean:message key="ecom.head.reg" bundle="<%=clientcode%>"/> </div>
<div class="container" style="margin-bottom: 40px;">

    <div class="card card-shadow card-body app-content page_bottom_content_regi">
        <%if (errors.length() > 0 && errors.equalsIgnoreCase("regerrors")) {
        %>
        <div class="alert alert-success alert-dismissible">
            <html:errors></html:errors >
            </div>
        <% }%>
        <html:form action="SaveRegistration.do" styleId="RegistrationForm" styleClass="icons-tab-steps wizard-circle" method="post"  enctype="multipart/form-data">

            <h6> <i class="step-icon-reg"></i> <bean:message key="Registration.headings.companyINFO" bundle="<%=clientcode%>"/>  </h6>

            <fieldset>

                <div class="inner">
                    <%if ("Y".equalsIgnoreCase(KYCfileupload)) {%>
                    <div  class="form-group-row companytype" style="padding-left: 220px;padding-bottom: 15px;">

                        <label class="col-sm-3 control-label">
                            <input type="radio" id="existingCustomerRadio" checked="true" name="customerType" value="existingcustomer" style="height: 20px;width: 20px;" onchange="showFields();">&nbsp; 
                            <span for="existingCustomerRadio" style="padding-bottom: 20px;" >Existing Customer</span>
                        </label>
                        <label class="col-sm-3  control-label"  style="padding-right: 50px;">
                            <input type="radio" id="newCustomerRadio" name="customerType" value="newcustomer" style="margin-left:2%;height: 20px;width: 20px;" onchange="showFields();">&nbsp;
                            <span for="newCustomerRadio">New Customer</span>&nbsp;
                        </label>
                    </div>
                    <%}%>

                    <%if ("Y".equalsIgnoreCase(toDisplayCustCode)) {%>
                    <input type="hidden" id="control_location_Non_Mandatory" value="<%=control_location_Non_Mandatory%>"/>
                    <div class="row form-group">
                        <label class="col-sm-3 control-label" for="checkbutton">
                            <bean:message key="Registration.custcode" bundle="<%=clientcode%>" />
                        </label>
                        <%if ("Y".equalsIgnoreCase(customerCodeMandatory)) {%>
                        <div class="col-sm-7">
                            <html:text property="customerno" styleId="customerno" styleClass="form-control light_border required" maxlength="17" onchange='<%=(("Y".equalsIgnoreCase(custValidationOnNext)) ? "" : "loadCustomerDetails(this.value)")%>'  />
                        </div>
                        <div style="padding-left: 290px; padding-top: 10px;">
                            <a id="contactuslink" href="javascript:navigatetocontactus()"> <i class="fa fa-info-circle" aria-hidden="true"></i>&nbsp;<bean:message key='Registration.nocustcode' bundle='<%=clientcode%>'/> </a>
                        </div>

                        <%} else {%>
                        <div class="input-group  col-sm-7">

                            <div class="input-group-prepend">
                                <div class="input-group-text">
                                    <input type="checkbox" id="checkbutton"  onClick="excustidcheck(this.id)">
                                </div>
                            </div>
                            <html:text property="customerno" styleId="customerno" styleClass="form-control light_border" maxlength="17" readonly="true" onchange="loadCustomerDetails(this.value);" />
                        </div>
                        <a class="btn btn-lg text-info" data-toggle="tooltip" title="<bean:message key="reg.msg.cntctagency" bundle="<%=clientcode%>" />" data-placement="right" style="padding: 0px;" ><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                            <%}%>
                    </div>

                    <%}%>

                    <div class="row form-group">

                        <label for="companyname" class="col-sm-3 control-label" > <bean:message key="Reg.ComapanyName" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:text property="companyname" styleId="companyname"  styleClass="form-control light_border required setdata noneng google-address-search" maxlength="50" onblur="setcompany('this.id')" onkeyup="acceptCommoOnlyEnglish(this)"/>
                            <input type="hidden" id="checkclick" name="checkclick" value="yes"/>
                        </div>
                        <html:hidden property="companyId" styleId="companyId"></html:hidden>
                            <input id="hiddencity" name="hiddencity" type="hidden"/>
                            <input id="hiddencountry" name="hiddencountry" type="hidden"/>
                            <input id="hiddenstate" name="hiddenstate" type="hidden"/>
                        <html:hidden property="hiddencustname" styleId="hiddencustname" ></html:hidden>
                        <html:hidden property="company_state" styleId="company_state"/>
                        <html:hidden property="hiddenEmail" styleId="hiddenEmail" ></html:hidden>
                        <html:hidden property="agencycode" styleId="agencycode" ></html:hidden>
                        <html:hidden property="countrycode" styleId="countrycode" ></html:hidden>
                        <html:hidden property="savetype" styleId="savetype" value="new" ></html:hidden>
                            <!--<htmlhidden property="city_desc" styleId="city_desc"></html1:hidden>-->
                        <html:hidden property="contact_title" styleId="contact_title"></html:hidden>
                        <html:hidden property="contactperson" styleId="city_desc"></html:hidden>

                        <html:hidden property="othercitystatus" styleId="othercitystatus" value="Y"></html:hidden>
                            <input type="hidden" id="othercity" name="othercity" value=""/>
                        <html:hidden  property="email" styleId="email" styleClass="form-control" />

                        <html:hidden  property="hdncity" styleId="hdncity"/>
                        <html:hidden  property="hdnagency" styleId="hdnagency"/>
                        <html:hidden  property="locationt" styleId="locationt"/>

                    </div>


                    <div class="row form-group">

                        <label for="country" class="col-sm-3 control-label"> <bean:message key="Reg.Country" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:select property="country" styleId="country" styleClass="form-control light_border required setdata"  onchange="applyRegistrationCountryChange(this.value);" >
                                <html:options collection="COUNTRYNAME_COUNTRYCODE" property="value" labelProperty="label" />
                            </html:select>
                        </div>

                    </div>


                    <div class="row form-group">

                        <label for="location" class="col-sm-3 control-label"> <bean:message key="Reg.AgecnyContactLocation" bundle="<%=clientcode%>" /> <span class="mandatory" id="locationmandatorylbl">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:select property="location" styleId="location" styleClass="form-control light_border required setdata"
                                         onblur ="setHiddenValue('agencycode','location');" >
                                <html:option value="">Select Nearest Agency Location</html:option>
                            </html:select>

                        </div>

                    </div>


                    <div class="row form-group">

                        <label for="company_address" class="col-sm-3 control-label" > <bean:message key="Reg.Address1" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:text  property="company_address" styleId="company_address" styleClass="form-control light_border required setdata noneng" maxlength="150" onkeyup="acceptCommoOnlyEnglish(this)"/>
                        </div>

                    </div>
                    <%if (!"Y".equalsIgnoreCase(hideRegAddress2)) {%>
                    <div class="row form-group">

                        <label for="company_address1" class="col-sm-3 control-label" > <bean:message key="Reg.Address2" bundle="<%=clientcode%>" /> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:text  property="company_address1" styleId="company_address1" styleClass="form-control light_border setdata noneng"  maxlength="150" onkeyup="acceptCommoOnlyEnglish(this)" />

                        </div>

                    </div>
                    <%}%>
                    <div class="row form-group">

                        <label for="other_city" class="col-sm-3 control-label" > <bean:message key="Reg.City" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <!--        <div class="input-group">
                                               <span class="input-group-addon">
                                                   <input type="checkbox" id="checkcitybtn" aria-label="..." onClick="citystatus(this.id)"/>     <label style="font-size:12px">Others </label>
                                               </span>
       
                                                <htmlselect property="company_city" styleId="company_city" styleClass="form-control required setdata compcity"  onblur="setCityDesc('company_city');" style="display:block">
                                                   <htmloption value="">Select City</htmloption>
                                               </htmlselect>
                                               <htmltext property="other_city" styleId="other_city"  styleClass="form-control setdata othercity noneng" onblur= "setCityDesc('other_city');" maxlength="150" style="display:none"></htmltext>
                                               </div> -->
                            <html:text property="other_city" styleId="other_city"  styleClass="form-control setdata othercity noneng required light_border" onblur= "setCityDesc('other_city');" maxlength="150" onkeyup="acceptCommoOnlyEnglish(this)"/>

                        </div>

                    </div>

                    <div class="row form-group">

                        <label for="company_postcode" class="col-sm-3 control-label" id="company_postcode_lbl" > <bean:message key="Reg.Postalcode" bundle="<%=clientcode%>" /><%if (postalcode_required.length() > 2) {%>  <span class="mandatory">*</span> <%}%></label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:text  property="company_postcode"  styleId="company_postcode" styleClass="form-control light_border setdata " maxlength="10" />
                        </div>

                    </div>

                    <div class="row form-group">

                        <label for="company_country_code" class="col-sm-3 control-label"> <bean:message key="reg.companyphone" bundle="<%=clientcode%>" /> <% if (companyphone_required.length() > 2 || "Y".equalsIgnoreCase(eitherCompMobOrPhoneMandatory)) { %> <span class="mandatory" id="company_phone_mandatory">*</span> <% }%> </label>

                        <div class="col-sm-2 txt_error ecom_Padding_left0">
                            <html:text  property="company_country_code"  styleId="company_country_code" styleClass="form-control light_border  number setdata "  maxlength="<%= maxLengthValue%>"/> 

                        </div>

                        <div class="col-sm-2 txt_error ecom_Padding_left0">

                            <html:text  property="company_phone_code"  styleId="company_phone_code" styleClass="form-control light_border number setdata" maxlength="4" />

                        </div>

                        <div class="col-sm-3 txt_error ecom_Padding_left0">

                            <html:text  property="company_phone_no"  styleId="company_phone_no" styleClass="form-control light_border  number setdata " maxlength="10"/>

                        </div>

                    </div>
                    <%if ("Y".equalsIgnoreCase(enableCompanyMobile)) {%> 
                        <div class="row form-group">
    
                            <label for="mobile_code" class="col-sm-3 control-label"> <bean:message key="Reg.CompanyMobile" bundle="<%=clientcode%>"/> <% if ("Y".equalsIgnoreCase(eitherCompMobOrPhoneMandatory)) { %> <span class="mandatory" id="company_mobile_mandatory">*</span> <% }%> </label>
    
                                <div class="col-sm-2 txt_error ecom_Padding_left0">
                                    <html:text  property="company_mobile_code"  styleId="company_mobile_code" styleClass="form-control light_border number" maxlength="<%= maxLengthValue%>" />
    
                                </div>
    
    
    
                                <div class="col-sm-5 txt_error ecom_Padding_left0">
                                    <html:text  property="company_mobile_no"  styleId="company_mobile_no" styleClass="form-control light_border number" maxlength="15"/>
    
                                </div>
    
                            </div>  
                    <%}%>

                    <%if ("Y".equalsIgnoreCase(EnableTaxId)) {%>
                    <div class="row form-group">

                        <label for="tax_id" class="col-sm-3 control-label"> <bean:message key="Reg.TaxId" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:text property="tax_id" styleId="tax_id" styleClass="form-control light_border required setdata noneng" maxlength="20" onkeyup="acceptCommoOnlyEnglish(this)"/>
                        </div>

                    </div>
                    <%}%>
                    <%if (!"Y".equalsIgnoreCase(hideRegRecentBL)) {%>
                    <div class="row form-group">

                        <label class="col-sm-3 control-label"> <bean:message key="Reg.resentbl" bundle="<%=clientcode%>" /> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:text  property="recent_bl" styleId="recent_bl" styleClass="form-control light_border setdata noneng"  maxlength="100" onkeyup="acceptCommoOnlyEnglish(this)"/>
                        </div>

                    </div>
                    <%}%>
                    <div class="row form-group">

                        <label class="col-sm-3 control-label"> <bean:message key="Reg.CompanyDomain" bundle="<%=clientcode%>" /> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">

                            <html:text  property="company_domain" styleId="company_domain" styleClass="form-control light_border setdata noneng" value="http://www." maxlength="50" onkeyup="acceptCommoOnlyEnglish(this)"/>

                        </div>

                    </div>



                </div>
            </fieldset>

            <h6><i class="step-icon-reg"></i> <bean:message key="Registration.headings.userifo" bundle="<%=clientcode%>" /> </h6>

            <fieldset>
                <div class="inner">


                    <div class="row form-group">

                        <label for="loginname" class="col-sm-3 control-label"> <bean:message key="Reg.Emailid" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:text property="loginname"  styleId="loginname" styleClass="form-control light_border required email setdata noneng usernameValidation"  maxlength="50" onkeyup="acceptCommoOnlyEnglish(this)"/>

                        </div>

                    </div>


                    <div class="row form-group">

                        <label for="Password"  class="col-sm-3 control-label"> <bean:message key="Reg.password" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">
                            <html:password property="password"  styleId="password" styleClass="form-control light_border required setdata noneng PasswordValidationNote"  maxlength="20" onblur="checkpassword();" onkeyup="acceptCommoOnlyEnglish(this)" />
                        </div>

                    </div>


                    <div class="row form-group">

                        <label for="cpassword" class="col-sm-3 control-label"> <bean:message key="Registration.ConfirmPassword" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>

                        <div class="col-sm-7 txt_error ecom_Padding_left0">

                            <html:password property="cpassword"   styleId="cpassword" styleClass="form-control light_border required setdata noneng" maxlength="20" onblur="checkpassword();" onkeyup="acceptCommoOnlyEnglish(this)"/>
                        </div>

                    </div>

                    <div class="row form-group">

                        <label for="firstname" class="col-sm-3 control-label"> <bean:message key="Reg.firstname" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                        <%if ("Y".equalsIgnoreCase(enable_contactTitle)) {%>
                        <div class="col-sm-1 txt_error ecom_Padding_left0" style="padding-right:0px;padding-left: 0px;right: -16px;">
                            <select name="usertitle" id="usertitle" class="form-control light_border required valid user_title" aria-invalid="false" style=" position: relative;padding-left: 7px;" >
                                <option value=""></option>
                                <option value="Mr.">Mr.</option>
                                <option value="Messrs.">Messrs.</option>
                                <option value="Mrs.">Mrs.</option>
                                <option value="Ms.">Ms.</option></select>
                        </div>
                        <div class="col-sm-6 txt_error ecom_Padding_left0" style=" padding-left: 43px; padding-left: -13px;">
                            <html:text  property="firstname"  size="50" styleId="firstname" styleClass="form-control light_border required setdata noneng" maxlength="50" onkeyup="acceptCommoOnlyEnglish(this)"/>
                            <%} else {%>
                            <div class="col-sm-7 txt_error ecom_Padding_left0">
                                <html:text  property="firstname"  size="50" styleId="firstname" styleClass="form-control light_border required setdata noneng" maxlength="50" onkeyup="acceptCommoOnlyEnglish(this)"/>
                                <%}%>
                            </div>

                        </div>

                        <div class="row form-group">

                            <label for="lastname" class="col-sm-3 control-label"> <bean:message key="Reg.LastName" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>

                            <div class="col-sm-7 txt_error ecom_Padding_left0">
                                <html:text  property="lastname" size="50" styleId="lastname" styleClass="form-control light_border required setdata noneng" maxlength="50" onkeyup="acceptCommoOnlyEnglish(this)"/>

                            </div>

                        </div>
                        <% if ("Y".equalsIgnoreCase(userPhoneMandatory)) {%>
                        <div class="row form-group">

                            <label for="cust_country_code" class="col-sm-3 control-label"> <bean:message key="Reg.Phone" bundle="<%=clientcode%>"/>  <span class="mandatory">*</span> </label>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="cust_country_code"  styleId="cust_country_code" styleClass="form-control light_border required number" maxlength="<%= maxLengthValue%>" />

                            </div>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="cust_phone_code"  styleId="cust_phone_code" styleClass="form-control light_border number" maxlength="4"/>

                            </div>

                            <div class="col-sm-3 txt_error ecom_Padding_left0">
                                <html:text  property="cust_phone_no"  styleId="cust_phone_no"  styleClass="form-control light_border required number" maxlength="10"/>

                            </div>

                        </div>

                        <%} else {%> 
                        <div class="row form-group">

                            <label for="cust_country_code" class="col-sm-3 control-label"> <bean:message key="Reg.Phone" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="cust_country_code"  styleId="cust_country_code" styleClass="form-control light_border number" maxlength="<%= maxLengthValue%>" />

                            </div>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="cust_phone_code"  styleId="cust_phone_code" styleClass="form-control light_border number" maxlength="4"/>

                            </div>

                            <div class="col-sm-3 txt_error ecom_Padding_left0">
                                <html:text  property="cust_phone_no"  styleId="cust_phone_no"  styleClass="form-control light_border number" maxlength="10"/>

                            </div>

                        </div>
                        <%}%>

                        <%if ("Y".equalsIgnoreCase(mobileMandatory)) {%>

                        <div class="row form-group">

                            <label for="mobile_code" class="col-sm-3 control-label"> <bean:message key="Reg.Mobile" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label></label>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="mobile_code"  styleId="mobile_code" styleClass="form-control light_border required number" maxlength="<%= maxLengthValue%>" />

                            </div>



                            <div class="col-sm-5 txt_error ecom_Padding_left0">
                                <html:text  property="mobile_no"  styleId="mobile_no" styleClass="form-control light_border required number" maxlength="15"/>

                            </div>

                        </div>
                        <%} else {%>   

                        <div class="row form-group">

                            <label for="mobile_code" class="col-sm-3 control-label"> <bean:message key="Reg.Mobile" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="mobile_code" title="Country code" styleId="mobile_code" styleClass="form-control light_border mobileReq number" maxlength="<%= maxLengthValue%>" />

                            </div>



                            <div class="col-sm-5 txt_error ecom_Padding_left0">
                                <html:text  property="mobile_no" title="Mobile number" styleId="mobile_no" styleClass="form-control light_border number" maxlength="15"/>

                            </div>

                        </div>
                        <%}%>

                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <div class="row form-group">

                            <label for="fax_country_code" class="col-sm-3 control-label"> <bean:message key="Reg.Fax" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="fax_country_code"   styleId="fax_country_code" styleClass="form-control light_border number" maxlength="4"/>
                            </div>

                            <div class="col-sm-2 txt_error ecom_Padding_left0">
                                <html:text  property="fax_phone_code"  styleId="fax_phone_code" styleClass="form-control number light_border" maxlength="4"/>
                            </div>

                            <div class="col-sm-3 txt_error ecom_Padding_left0">
                                <html:text  property="fax_phone_no"  styleId="fax_phone_no" styleClass="form-control number light_border" maxlength="10"/>
                            </div>

                        </div>
                        <%}%>
                        <!--                    timezone-->
                        <%if ("Y".equalsIgnoreCase(toenableTimeZone)) {%>
                        <div class="row form-group">

                            <label for="timezone" class="col-sm-3 control-label"> <bean:message key="ecom.reg.timezone" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>

                            <div class="col-sm-7 txt_error ecom_Padding_left0">
                                <html:select property="timezone" styleId="timezone" styleClass="form-control light_border required setdata" onblur="setHiddenValueTime('locationt','timezone')"   >
                                    <html:options collection="PREFERREDTIMEZONELIST" property="value" labelProperty="label" />
                                </html:select>
                            </div>

                        </div>
                        <%}%>
                        <%if (!"Y".equalsIgnoreCase(disableDefaultView)) {%>
                        <div class="row form-group">

                            <label for="deflanguage" class="col-sm-3 control-label"> <bean:message key="Reg.defaultview" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>

                            <div class="col-sm-7 txt_error ecom_Padding_left0">
                                <html:select property="deflanguage" styleId="deflanguage" styleClass="form-control light_border required" disabled="true">
                                    <html:option value="EN">ENGLISH</html:option>
                                </html:select>

                            </div>

                        </div>
                        <%}%>
                        <%if (!"Y".equalsIgnoreCase(disablePreferredView)) {%>
                        <div class="row form-group">

                            <label for="preflanguage" class="col-sm-3 control-label"> <bean:message key="Reg.preferredview" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>

                            <div class="col-sm-7 txt_error ecom_Padding_left0">

                                <html:select property="preflanguage" styleId="preflanguage" styleClass="form-control light_border required">
                                    <%
                                        LinkedHashMap langlist = (LinkedHashMap) getServletContext().getAttribute(GeneralConf.LangSelection);
                                        Iterator it = null;
                                        it = langlist.entrySet().iterator();
                                        while (it.hasNext()) {
                                            Map.Entry pairs = (Map.Entry) it.next();

                                    %>
                                    <option value="<%=pairs.getKey().toString()%>"><%=pairs.getValue().toString()%></option>
                                    <%
                                        }
                                    %>

                                </html:select>

                            </div>

                        </div>
                        <%}%>

                    </div>
            </fieldset>

            <%if ("Y".equalsIgnoreCase(KYCfileupload)) {%>
            <h6><i class="step-icon-reg"></i> <bean:message key="Registration.headings.fileuploadoption" bundle="<%=clientcode%>" /> </h6>  
            <fieldset>
                <div id="fieldContainer" class="col-sm-10" style="padding-left: 130px;"></div>
            </fieldset>
            <%}%>

            <h6><i class="step-icon-reg"></i> <bean:message key="Registration.headings.Terms" bundle="<%=clientcode%>" /> </h6>

            <fieldset>
                <%if (!"Y".equalsIgnoreCase(displayRegGgleCaptcha)) {%>            
                <div class=" txt_box_book ">
                    <div class="txt_side_border">

                        <div class="row form-group">
                            <div class="col-sm-3">
                                <label>
                                    <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
                                    <a class="fa fa-refresh refresh_cap" title="Reload captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');" href="javascript:void(0);"></a>
                                </label>
                            </div>
                            <div class="col-sm-3">
                                <html:text property="captcha" styleId="captcha"  styleClass="form-control light_border required"/>
                                <span style="font-size: 12px;"> <bean:message key="Reg.retypecaptcha" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </span>
                            </div>
                        </div>
                        <div class="row form-group">
                            <div class="skin1 skin-flat1 input-group ml-3">  <label>
                                    <html:checkbox property="agree" styleId="agree" styleClass="required"></html:checkbox>
                                    <a href="javascript:void(0);" id="register" class="text-info" onClick="popUpWindowReg('NewRegistration.do?type=terms', 150, 100, 800, 500)"><bean:message key="Reg.Agree" bundle="<%=clientcode%>" /></a><span class="mandatory">*</span></label> 
                            </div>
                        </div>

                    </div>
                </div>
                <%} else {%>
                <div class=" txt_box_book ">
                    <div class="txt_side_border">
                        <div class="row form-group">
                            <div class="col-sm-5">
                                <div id="regrecaptiid" ></div>  
                                <div id="g-recaptcha-error"></div>
                            </div>
                        </div>
                    </div>


                    <div class="row form-group">
                        <div class="skin1 skin-flat1 input-group ml-3">  <label>
                                <html:checkbox property="agree" styleId="agree" styleClass="required"></html:checkbox>
                                <a href="javascript:void(0);" id="register" class="text-info" onClick="popUpWindowReg('NewRegistration.do?type=terms', 150, 100, 800, 500)">  <bean:message key="Reg.Agree" bundle="<%=clientcode%>" /> </a><span class="mandatory">*</span></label> 
                        </div>
                    </div>

                </div>

                <%}%>
            </fieldset>

        </html:form>
    </div>
</div>
<div class="d-none">
    <form id="navigateform" method="POST" action="Accesslogin.do">
        <input type="hidden" id="type" name="type" value="contactus" />
        <input type="hidden" id="fromRegistration" name="fromRegistration" value="Y" />
    </form>
</div>
<style>
    /*    .placeclass
        {
            color:#666 !important;
            text-align:left !important;
        }*/
</style>



<script type="text/javascript" src="js/registration.js?<%=jsdate%>"></script>
<style type="text/css">
    .google-address-search-dropdown {
        position: fixed;
        z-index: 10050;
        background: #fff;
        border: 1px solid #e2e8f0;
        border-radius: 0 0 8px 8px;
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
        max-height: 260px;
        overflow-y: auto;
    }
    .google-address-search-item {
        padding: 10px 12px;
        cursor: pointer;
        border-bottom: 1px solid #f1f5f9;
    }
    .google-address-search-item:hover {
        background: #f0f4ff;
    }
    .google-address-search-main {
        font-weight: 500;
        color: #1a1a2e;
    }
    .google-address-search-secondary {
        font-size: 12px;
        color: #64748b;
        margin-top: 2px;
    }
    .google-address-search-active {
        position: relative;
        z-index: 1101;
    }
</style>
<span id="googleAddressSearchStatus" style="display:none;"
      data-enabled="<%=googleAddressSearchActive ? "Y" : "N"%>"
      data-api-key="<%=googleMapsApiKeyConfigured.length() > 0 ? "Y" : "N"%>"></span>
<script type="text/javascript">
    window.ecomGoogleAddressSearchEnabled = '<%=googleAddressSearchActive ? "Y" : "N"%>';
    window.ecomGoogleAddressAjaxUrl = '<%=contextRoot%>/AddressLookupAjax';
</script>
<script type="text/javascript" src="js/google-address-search.js?<%=jsdate%>"></script>
<%
    if ("zh".equalsIgnoreCase(clientcode)) {
%>
<script type="text/javascript" src="notify/notify.js?<%=jsdate%>"></script>
<%            }%>
<script type="text/javascript">
                                    var regRecaptchaWidgetId;
                                    var countrycodearr = "<%=POSTALCODE_OPTIONAL_COUNTRY%>";
                                    var allowexistingUsertoReg = "<%=allowexistingUsertoReg%>";
                                    var eitherCompMobOrPhoneMandatory = "<%=eitherCompMobOrPhoneMandatory%>";
                                    var enableCompanyMobile = "<%=enableCompanyMobile%>";
                                    var enableCountryCode = "<%=enableCountryCode%>";
                                    // Auto-fill applies to all of these country-code fields, regardless of mandatory status.
                                    var COUNTRY_CODE_FIELD_IDS = ['company_country_code', 'cust_country_code', 'mobile_code'];
                                    function getCountryCodeFields() {
                                        return $($.map(COUNTRY_CODE_FIELD_IDS, function (id) {
                                            return $('#' + id).length ? ('#' + id) : null;
                                        }).join(','));
                                    }
                                    $(document).ready(function () {
                                        if (typeof EcomGoogleAddressSearch !== 'undefined') {
                                            EcomGoogleAddressSearch.init();
                                            $(document).off('input.googleAddress keyup.googleAddress paste.googleAddress', '#companyname')
                                                    .on('input.googleAddress keyup.googleAddress paste.googleAddress', '#companyname', function () {
                                                        EcomGoogleAddressSearch.onInput(this);
                                                    });
                                        }
                                        $('#searchbtn').attr("disabled", true);
                                        $('#customerno').attr('placeholder', '<bean:message key="Registration.custcode" bundle="<%=clientcode%>"/>');
                                        $('#company_country_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                        $('#company_phone_code').attr('placeholder', '<bean:message key="Reg.area" bundle="<%=clientcode%>"/>');
                                        $('#company_phone_no').attr('placeholder', '<bean:message key="Reg.phonenum" bundle="<%=clientcode%>"/>');
                                        $('#cust_country_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                        //$('#cust_phone_code').attr('placeholder', '<bean:message key="Reg.phonenum" bundle="<%=clientcode%>"/>');
                                        $('#cust_phone_code').attr('placeholder', '<bean:message key="Reg.area" bundle="<%=clientcode%>"/>');
                                        $('#cust_phone_no').attr('placeholder', '<bean:message key="Reg.phonenum" bundle="<%=clientcode%>"/>');
                                        $('#mobile_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                        $('#mobile_no').attr('placeholder', '<bean:message key="reg.mobnum" bundle="<%=clientcode%>"/>');
                                        $('#fax_country_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                        $('#fax_phone_code').attr('placeholder', '<bean:message key="Reg.area" bundle="<%=clientcode%>"/>');
                                        $('#fax_phone_no').attr('placeholder', '<bean:message key="reg.faxnum" bundle="<%=clientcode%>"/>');

                                        // Country dial code auto-fill is only active when the global
                                        // ENABLE_COUNTRY_CODE flag is set to "Y" via admin configuration.
                                        if (enableCountryCode === 'Y') {
                                            // Deferred so any later $(document).ready blocks that operate on
                                            // these fields run first.
                                            setTimeout(initCountryCodePlugin, 0);
                                            $('#country').off('change.dialcode').on('change.dialcode', function () {
                                                setCountryDialCode(this.value);
                                            });
                                        }
                                    });

                                    // Configure the country-code phone fields so they work cleanly
                                    // with the auto-filled "+XX" dial code format.
                                    function initCountryCodePlugin() {
                                        // Bypass the "number" client-side validator on these fields because
                                        // the auto-filled value contains a leading "+" which is not a valid
                                        // JavaScript number. The "+" is stripped just before form submit so
                                        // the server still receives digits only.
                                        getCountryCodeFields().removeClass('number').attr('readonly', true);

                                        // Strip the leading "+" from country-code fields right before the
                                        // form is actually submitted. The form is submitted programmatically
                                        // via .submit() inside the wizard's onFinished callback, which
                                        // bypasses jQuery submit handlers, so we wrap the form's native
                                        // submit() method instead.
                                        var formEl = document.getElementById('RegistrationForm');
                                        if (formEl && !formEl._dialCodeSubmitWrapped) {
                                            var originalSubmit = formEl.submit.bind(formEl);
                                            formEl.submit = function () {
                                                stripPlusFromCountryCodeFields();
                                                return originalSubmit();
                                            };
                                            formEl._dialCodeSubmitWrapped = true;
                                        }
                                    }

                                    function stripPlusFromCountryCodeFields() {
                                        getCountryCodeFields().each(function () {
                                            var $el = $(this);
                                            var v = String($el.val() || '');
                                            if (v.charAt(0) === '+') {
                                                $el.val(v.substring(1));
                                            }
                                        });
                                    }

                                    // Auto-populate country dial code into phone country code fields
                                    // (company / customer / mobile / fax) based on the selected country.
                                    // Uses the intl-tel-input library data already loaded by the layout.
                                    function setCountryDialCode(countryCode) {
                                        if (enableCountryCode !== 'Y') {
                                            return;
                                        }
                                        if (!countryCode) {
                                            return;
                                        }
                                        if (typeof window.intlTelInputGlobals === 'undefined'
                                                || typeof window.intlTelInputGlobals.getCountryData !== 'function') {
                                            console.warn('[Registration] intlTelInputGlobals not available; cannot auto-populate country dial code.');
                                            return;
                                        }
                                        var countries = window.intlTelInputGlobals.getCountryData();
                                        var rawCode = String(countryCode).trim();
                                        var iso2 = rawCode.toLowerCase();
                                        var dialCode = '';

                                        // 1) Try ISO2 (alpha-2) match, e.g. "IN" -> "in"
                                        for (var i = 0; i < countries.length; i++) {
                                            if (countries[i].iso2 === iso2) {
                                                dialCode = countries[i].dialCode || '';
                                                break;
                                            }
                                        }

                                        // 2) Fallback: match by the selected option's displayed country name.
                                        //    Handles cases where the dropdown value is not an ISO2 code
                                        //    (e.g. 3-letter codes such as "IND" or carrier-specific codes).
                                        if (!dialCode) {
                                            var selectedName = $.trim($('#country option:selected').text() || '').toLowerCase();
                                            if (selectedName && selectedName !== 'select country') {
                                                for (var j = 0; j < countries.length; j++) {
                                                    var libName = String(countries[j].name || '').toLowerCase();
                                                    var libBaseName = libName.replace(/\s*\(.*?\)\s*/g, '').trim();
                                                    if (libName === selectedName
                                                            || libBaseName === selectedName
                                                            || libName.indexOf(selectedName) === 0) {
                                                        dialCode = countries[j].dialCode || '';
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        if (!dialCode) {
                                            return;
                                        }

                                        var displayValue = '+' + dialCode;
                                        getCountryCodeFields().each(function () {
                                            var $el = $(this);
                                            if (!$el.length) {
                                                return;
                                            }
                                            $el.val(displayValue);
                                            $el.removeClass('error');
                                            $el.next('label.error').remove();
                                        });
                                    }
                                    var rootpath = "<%=contextRoot%>";
                                    var enableGgleCaptcha = "<%=displayRegGgleCaptcha%>";
                                    //alert(rootpath);
                                    var remotepath = rootpath + "/CaptchaValidation";
                                    var remotemail = rootpath + "/UserProfileAjax?type=useremail&emailtype=useremail";
                                    var remotecompanymail = rootpath + "/UserProfileAjax?type=useremail&emailtype=companyemail";
                                    var capchapath = rootpath + "/CaptchaGeneration?";
                                    var remoteloginname = rootpath + "/UserProfileAjax?type=userid";
                                    $(document).ready(function () {
                                        var form = document.forms[0];
                                        form.setAttribute("autocomplete", "off");
                                        $('#RegistrationForm').attr('autocomplete', 'off');
                                        $("#RegistrationForm").validate({
                                            rules: {
                                                loginname: {
                                                    required: true,
                                                    minlength: 5,
                                                    Numbersspecialonly: "#loginname",
                                                    <%if (!"Y".equalsIgnoreCase(allowexistingUsertoReg)){%>
                                                          remote: {
                                                        url: remoteloginname,
                                                        data: {'userid': function () {
                                                                return $('#loginname').val()
                                                            }
                                                        }
                                                    } 
                                                <%}%>
                                             },
                                                password: {
                                                    checkPassword: "#password"
                                                },
                                                cpassword: {
                                                    equalTo: "#password"
                                                },
                                                captcha: {
                                                    required: true,
                                                    remote: remotepath
                                                },
                                                company_postcode:
                                                        {
                                                            postcodevalidate: "#company_postcode"
                                                        },
                                                company_country_code:
                                                        {
                                                            ZeroTest: "#company_country_code"
                                                        },
                                                company_phone_code:
                                                        {
                                                            ZeroTest: "#company_phone_code"
                                                        },
                                                company_phone_no:
                                                        {
                                                            ZeroTest: "#company_phone_no"
                                                        },
                                                cust_country_code:
                                                        {
                                                            ZeroTest: "#cust_country_code"
                                                        },
                                                cust_phone_code:
                                                        {
                                                            ZeroTest: "#cust_phone_code"
                                                        },
                                                cust_phone_no:
                                                        {
                                                            ZeroTest: "#cust_phone_no"
                                                        },
                                                mobile_code:
                                                        {

                                                            ZeroTest: "#mobile_code"
                                                        },
                                                mobile_no:
                                                        {

                                                            ZeroTest: "#mobile_no"
                                                        },
                                                company_mobile_code:
                                                        {

                                                            ZeroTest: "#company_mobile_code"
                                                        },
                                                company_mobile_no:
                                                        {

                                                            ZeroTest: "#company_mobile_no"
                                                        },
                                                fax_country_code:
                                                        {
                                                            required: function (element) {
                                                                return $("#fax_country_code").val() != "" || $("#fax_phone_code").val() != "" || $("#fax_phone_no").val() != "";
                                                            },
                                                            ZeroTest: "#fax_country_code"
                                                        },
                                                fax_phone_code:
                                                        {
                                                            ZeroTest: "#fax_phone_code"
                                                        },
                                                fax_phone_no:
                                                        {
                                                            required: function (element) {
                                                                return $("#fax_country_code").val() != "" || $("#fax_phone_code").val() != "" || $("#fax_phone_no").val() != "";
                                                            },
                                                            ZeroTest: "#fax_phone_no"
                                                        },
                                                company_domain:
                                                        {
                                                            domaincheck: "#company_domain"
                                                        }
                                            },
                                            messages: {
                                                loginname: {
                                                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                    minlength: "<bean:message key="ecom.msg.loginnameformat" bundle="<%=clientcode%>"/>",
                                                    Numbersspecialonly: "<bean:message key="ecom.msg.validloginname" bundle="<%=clientcode%>"/>",
                                                    <%if (!"Y".equalsIgnoreCase(allowexistingUsertoReg)){%>
                                                    remote: "<bean:message key="ecom.msg.existsloginname" bundle="<%=clientcode%>"/>"
                                                     <%}%>
                                                },
                                                password: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                },
                                                usertitle: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                },
                                                cpassword: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                    equalTo: "<bean:message key="err.reg.client.notmatchpassword" bundle="<%=clientcode%>"/>"
                                                },
                                                captcha: {

                                                    required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                                    remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>",
                                                },
                                                company_postcode:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidpostalcode" bundle="<%=clientcode%>"/>"
                                                        },
                                                company_country_code:
                                                        {number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidcountrycode" bundle="<%=clientcode%>"/>"
                                                        },
                                                company_phone_code:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidareacode" bundle="<%=clientcode%>"/>"
                                                        },
                                                company_phone_no:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidphonenum" bundle="<%=clientcode%>"/>"
                                                        },
                                                cust_country_code:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidcountrycode" bundle="<%=clientcode%>"/>"
                                                        },
                                                cust_phone_code:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidareacode" bundle="<%=clientcode%>"/>"
                                                        },
                                                cust_phone_no:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidphonenum" bundle="<%=clientcode%>"/>"
                                                        },
                                                mobile_code:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidcountrycode" bundle="<%=clientcode%>"/>"
                                                        },
                                                mobile_no:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidmobnum" bundle="<%=clientcode%>"/>"
                                                        },
                                                company_mobile_code:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidcountrycode" bundle="<%=clientcode%>"/>"
                                                        },
                                                company_mobile_no:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidmobnum" bundle="<%=clientcode%>"/>"
                                                        },
                                                fax_country_code:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidfaxcountrycode" bundle="<%=clientcode%>"/>"
                                                        },
                                                fax_phone_code:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidfaxareacode" bundle="<%=clientcode%>"/>"
                                                        },
                                                fax_phone_no:
                                                        {
                                                            number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            ZeroTest: "<bean:message key="ecom.msg.invalidfaxnum" bundle="<%=clientcode%>"/>"
                                                        },
                                                customerno: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                companyname: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                country: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                location: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                company_address: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                other_city: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                company_city: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                firstname: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                usertitle: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                lastname: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                agree:
                                                        {
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                        },
                                                company_domain:
                                                        {
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            domaincheck: "<bean:message key="ecom.msg.invaliddomain" bundle="<%=clientcode%>"/>"
                                                        }
                                            }
                                        });
                                        var form = $("#RegistrationForm").show();
                                        form.steps({
                                            headerTag: "h6",
                                            bodyTag: "fieldset",
                                            autoFocus: true,
                                            transitionEffect: "fade",
                                            transitionEffectSpeed: 500,
                                            titleTemplate: '<span class="step">#index#</span> #title#',
                                            labels: {
                                                finish: "<bean:message key="ecom.submit" bundle="<%=clientcode%>"/> ",
                                                next: "<bean:message key="ecom.next" bundle="<%=clientcode%>"/> <span class='glyphicon glyphicon-arrow-right'></span>",
                                                previous: " <span class='glyphicon glyphicon-arrow-left'></span> <bean:message key="ecom.prev" bundle="<%=clientcode%>"/>"
                                            },
                                            onInit: function (event, current)
                                            {
                                                if (window.ecomGoogleAddressSearchEnabled === 'Y'
                                                        && typeof EcomGoogleAddressSearch !== 'undefined') {
                                                    EcomGoogleAddressSearch.refresh();
                                                }
                                                $('#captcha').keyup(function () {
                                                    if (this.value.match(/[^a-zA-Z0-9]/g)) {
                                                        console.log("coming>>");
                                                        this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
                                                    }
                                                });
                                                $('.captchaval').keyup(function () {
                                                    if (this.value.match(/[^a-zA-Z0-9]/g)) {
                                                        console.log("coming>>");
                                                        this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
                                                    }
                                                });
                                                $('[data-toggle="tooltip"]').tooltip();
                                            },
                                            onStepChanging: function (event, currentIndex, newIndex)
                                            {
                                                var checkcust = true;
                                                // Allways allow previous action even if the current form is not valid!
                                                if (currentIndex > newIndex)
                                                {
                                                    return true;
                                                }
                                                form.validate().settings.ignore = ":disabled,:hidden";
                                                if (currentIndex == 0 && newIndex == 1 && form.valid()) {
                                                    console.log("Inside currentIndex 0");
                                                    if ($('#checkbutton').is(":checked")) {
                                                        $.ajax({
                                                            type: "POST",
                                                            cache: false,
                                                            async: false,
                                                            url: "ecomAjax?type=checkcompanydetails&custcode=" + encodeURIComponent($('#customerno').val()) + "&country=" + encodeURIComponent($('#country').val()),
                                                            data: $(this).serializeArray(),
                                                            success: function (response) {
                                                                if (response == 'Invalid') {
                                                                    checkcust = false;
                                                                    alert("<bean:message key="ecom.msg.invalidcustcode" bundle="<%=clientcode%>"/>");
                                                                    return false;
                                                                } else if (response != 'Valid') {
                                                                    checkcust = false;
                                                                    alert("<bean:message key="ecom.msg.exceedusercount" bundle="<%=clientcode%>"/>");
                                                                    return false;
                                                                }
                                                            },
                                                            error: function (xhr) {
                                                                if (xhr.status == 403) {
                                                                    window.location.href = "SessionExpired.jsp";
                                                                }
                                                            }
                                                        });
                                                    }
    <%if ("Y".equalsIgnoreCase(custValidationOnNext)) {%>
                                                    var custcode = $('#customerno').val();
                                                    if ($('#customerno').val() != null && $('#other_city').val() != null && $('#companyname').val() != null) {
                                                        if (custcode.length > 0) {
                                                            console.log("Inside customerno 0");
                                                            if (custcode == "-")
                                                            {
                                                                alert("Invalid System Code");
                                                                return false;
                                                            } else
                                                            {
                                                                console.log("City===>" + $('#other_city').val());
                                                                $.ajax({
                                                                    type: "POST",
                                                                    cache: false,
                                                                    url: "ecomAjax?type=checkcompanynameandcity&custcode=" + encodeURIComponent($('#customerno').val()) + "&companyname=" + encodeURIComponent($('#companyname').val()) + "&cityname=" + encodeURIComponent($('#other_city').val()),
                                                                    async: false,
                                                                    data: $(this).serializeArray(),
                                                                    success: function (data) {
                                                                        var details = data.split("~~");
                                                                        console.log(details);
                                                                        if (details[3] == "VALID") {
                                                                            var cusName = details[1];
                                                                            var city = details[2];
                                                                            if (cusName == "false" && city == "false") {
                                                                                checkcust = true;
                                                                            } else if (cusName == "true" && city == "true") {
                                                                                checkcust = false;
                                                                                alert("The information provided by you doesn’t match our records.\nCompany Name \nCity");
                                                                            } else {
                                                                                checkcust = false;
                                                                                var mismatch = cusName == "true" ? "Company Name " : "City ";
                                                                                alert("The information provided by you doesn’t match our records.\n" + mismatch);
                                                                            }


                                                                        } else {
                                                                            alert("Invalid Customer Code");
                                                                            $('#customerno').val("");
                                                                            checkcust = false;
                                                                        }
                                                                    },
                                                                    error: function (xhr) {
                                                                        if (xhr.status == 403) {
                                                                            window.location.href = "SessionExpired.jsp";
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }

                                                    }
    <%}%>

                                                }
                                                
                                                // Validation: Either Company Phone or Company Mobile is mandatory
                                                if (eitherCompMobOrPhoneMandatory === 'Y' && enableCompanyMobile === 'Y') {
                                                    var compPhoneCountryCode = $.trim($('#company_country_code').val());
                                                    var compPhoneNo = $.trim($('#company_phone_no').val());
                                                    var compMobileCode = $.trim($('#company_mobile_code').val());
                                                    var compMobileNo = $.trim($('#company_mobile_no').val());
                                                    
                                                    var hasCompanyPhone = (compPhoneCountryCode.length > 0 && compPhoneNo.length > 0);
                                                    var hasCompanyMobile = (compMobileCode.length > 0 && compMobileNo.length > 0);
                                                    
                                                    // Clear previous errors
                                                    $('#company_country_code').removeClass('error');
                                                    $('#company_phone_no').removeClass('error');
                                                    $('#company_mobile_code').removeClass('error');
                                                    $('#company_mobile_no').removeClass('error');
                                                    $('#company_country_code').next('label.error').remove();
                                                    $('#company_phone_no').next('label.error').remove();
                                                    $('#company_mobile_code').next('label.error').remove();
                                                    $('#company_mobile_no').next('label.error').remove();
                                                    
                                                    // If user started filling phone but not complete
                                                    var phonePartiallyFilled = (compPhoneCountryCode.length > 0 || compPhoneNo.length > 0) && !hasCompanyPhone;
                                                    // If user started filling mobile but not complete
                                                    var mobilePartiallyFilled = (compMobileCode.length > 0 || compMobileNo.length > 0) && !hasCompanyMobile;
                                                    
                                                    if (!hasCompanyPhone && !hasCompanyMobile) {
                                                        if (phonePartiallyFilled) {
                                                            // User started filling phone, show error only on phone to complete it
                                                            $('#company_country_code').addClass('error');
                                                            $('#company_phone_no').addClass('error');
                                                            $('#company_country_code').after('<label class="error"><bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/></label>');
                                                        } else if (mobilePartiallyFilled) {
                                                            // User started filling mobile, show error only on mobile to complete it
                                                            $('#company_mobile_code').addClass('error');
                                                            $('#company_mobile_no').addClass('error');
                                                            $('#company_mobile_code').after('<label class="error"><bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/></label>');
                                                        } else {
                                                            // Neither started, show error on phone fields only (as primary option)
                                                            $('#company_country_code').addClass('error');
                                                            $('#company_phone_no').addClass('error');
                                                            $('#company_country_code').after('<label class="error"><bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/></label>');
                                                        }
                                                        checkcust = false;
                                                        return false;
                                                    }
                                                }

                                                if (checkcust == false) {
                                                    return false;
                                                }
                                                // Needed in some cases if the user went back (clean up)
                                                if (currentIndex < newIndex)
                                                {
                                                    // To remove error styles
                                                    form.find(".body:eq(" + newIndex + ") label.error").remove();
                                                    form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
                                                }
                                                if (newIndex == 3) {
                                                    sendfiletoserver();
                                                }

                                                return form.valid();
                                            },
                                            onStepChanged: function (event, currentIndex, priorIndex) {
                                                
                                                var captchapage=2;
                                             <% if("Y".equalsIgnoreCase(KYCfileupload)) {%>
                                                     captchapage=3;
                                             <%}%>
                                                 if (currentIndex == captchapage ) {
                                                     if (enableGgleCaptcha == 'Y') {
                                                          if (typeof regRecaptchaWidgetId === 'undefined') {
                                                        regRecaptchaWidgetId = grecaptcha.render("regrecaptiid", {
                                                          'sitekey': '<%=siteKey%>'
                                                        });
                                                        }
                                                     }
                                                 }
                                            },
                                            onFinishing: function (event, currentIndex)
                                            {
                                                if (enableGgleCaptcha == 'Y') {
                                                    checkGoogleRecaptcha();
                                                }
                                                form.validate().settings.ignore = ":disabled";
                                                return form.valid();
                                            },
                                            onFinished: function (event, currentIndex)
                                            {

                                             /*   if (enableGgleCaptcha == 'Y') {
                                                    if (checkRecaptcha()) {

                                                        document.getElementById('RegistrationForm').action = "SaveRegistration.do";
                                                        document.getElementById('RegistrationForm').submit();
                                                        
                                                    }
                                                } else {*/
                                                    document.getElementById('RegistrationForm').action = "SaveRegistration.do";
                                                    document.getElementById('RegistrationForm').submit();
                                               // }
                                            }
                                        });
                                    });
                                    function CreateNewCaptcha(contextRoot)
                                    {

                                        $("#captchaImage").attr('src', capchapath + Math.random());
                                        $("#captcha").val('');
                                    }

                                    $(document).ready(function () {
                                        setTimeout(function () {
                                            $('#password').val('');
                                        }, 10);
                                        setTimeout(function () {
                                            $('#cpassword').val('');
                                        }, 10);
                                        setTimeout(function () {
                                            $('#captcha').val('');
                                        }, 10);
                                        /*  if (document.getElementById('country').value != "")
                                         {
                                         getCitylist1(new Date().getTime());
                                         
                                         var city = "";
                                         if (document.getElementById("othercitystatus").value == "Y")
                                         {
                                         document.getElementById("checkcitybtn").checked = true;
                                         document.getElementById("company_city").style.display = 'none';
                                         document.getElementById("other_city").style.display = 'block';
                                         $('.othercity').addClass('required');
                                         $('.compcity').removeClass('required');
                                         }
                                         else
                                         {
                                         document.getElementById("checkcitybtn").checked = false;
                                         document.getElementById("othercitystatus").value = 'N';
                                         document.getElementById("company_city").style.display = 'block';
                                         document.getElementById("other_city").style.display = 'none';
                                         $('.othercity').removeClass('required');
                                         $('.compcity').addClass('required');
                                         city = $('#hdncity').val();
                                         }
                                         
                                         if (city != "")
                                         {
                                         setTimeout(function () {
                                         $("#company_city").val(city);
                                         }, 1000);
                                         }
                                         }*/

                                    });
                                    /*    function citystatus(id)
                                     {
                                     if (document.getElementById("checkcitybtn").checked == true)
                                     {
                                     
                                     document.getElementById("othercitystatus").value = 'Y';
                                     document.getElementById("company_city").style.display = 'none';
                                     document.getElementById("other_city").style.display = 'block';
                                     $('.othercity').addClass('required');
                                     $('.compcity').removeClass('required');
                                     }
                                     if (document.getElementById("checkcitybtn").checked == false)
                                     {
                                     document.getElementById("othercitystatus").value = 'N';
                                     document.getElementById("other_city").value = "";
                                     document.getElementById("company_city").style.display = 'block';
                                     document.getElementById("other_city").style.display = 'none';
                                     $('.othercity').removeClass('required');
                                     $('.compcity').addClass('required');
                                     }
                                     } */

                                    function setPostalCodeValidation() {
                                        var contrycode = $('#country').val();
                                        contrycode = contrycode.toUpperCase();
                                        countrycodearr = countrycodearr.toUpperCase();
                                        var postlbl = $('#company_postcode_lbl').html();
                                        postlbl = postlbl.replace('<span class="mandatory">*</span>', '');
                                        $('#company_postcode').removeClass('required');
                                        if (!countrycodearr.includes(contrycode)) {
                                            postlbl = postlbl + '<span class="mandatory">*</span>';
                                            $('#company_postcode').addClass('required');
                                        }
                                        $('#company_postcode_lbl').html(postlbl);
                                    }
    <%
        if ("zh".equalsIgnoreCase(clientcode) || "ma".equalsIgnoreCase(clientcode) || "es".equalsIgnoreCase(clientcode)) {

    %>
                                    toastr.options = {
                                        positionClass: 'toast-top-center'
                                    };
                                    toastr.info("<bean:message key="ecom.msg.formsineng" bundle="<%=clientcode%>"/>", "INFO");
    <%}%>
</script>
<script>
    function EnableDisableTextBox(s) {
        var txtPassportNumber = document.getElementById("ccode");
        txtPassportNumber.disabled = chk_ccode.checked ? false : true;
        if (!txtPassportNumber.disabled) {
            txtPassportNumber.focus();
        }
    }
    function checkGoogleRecaptcha() {
         var response = grecaptcha.getResponse(regRecaptchaWidgetId);
         if (response.length == 0) {
            document.getElementById('g-recaptcha-error').innerHTML = '<span style="color:red;"><bean:message key="ecom.gglecaptcha.err" bundle="<%=clientcode%>"/></span>';
            return false;
         }
        return true;
    }

    function verifyCaptcha() {
        return new Promise(function (resolve, reject) {

            document.getElementById('g-recaptcha-error').innerHTML = '';
            resolve();
        });
    }
    function recaptchaExpired() {
        return new Promise(function (resolve, reject) {

            alert("<bean:message key="ecom.gglecaptcha.expired" bundle="<%=clientcode%>"/>");
            resolve();
        });
    }



//    function verifyCaptcha() {
//        document.getElementById('g-recaptcha-error').innerHTML = '';
//    }
//    function recaptchaExpired() {
//        alert("<bean:message key="ecom.gglecaptcha.expired" bundle="<%=clientcode%>"/>");
//    }
//    function checkRecaptcha() {
//
//        var captchaval = $('#g-recaptcha-response').val();
//        console.log("responsevalue" + captchaval);
//        if ($('#g-recaptcha-response').length == 0 || captchaval == null || captchaval == 'null' || captchaval == '' || captchaval.length == 0) {
//            return false;
//        }
//        return true;
//    }
    function navigatetocontactus() {

    <%if (Validation.checkNull(CUSTOMERCODE_REQ_URL).length() > 0) {%>
        window.open("<%=Validation.checkNull(CUSTOMERCODE_REQ_URL)%>");
    <%} else {%>
        $('#navigateform').submit();
    <%}%>
    }
    function acceptCommoOnlyEnglish(thisInput) {
    <% if ("Y".equalsIgnoreCase(acceptOnlyEng)) {
        String commoEngRegex = (String) getServletContext().getAttribute(GeneralConf.CommodityDescInOnlyENG_Regex);
        if (commoEngRegex != null && !commoEngRegex.isEmpty()) { %>
        thisInput.value = thisInput.value.split(<%= commoEngRegex %>).join('');
    <%  } else { %>
        thisInput.value = thisInput.value.split(/[^a-zA-Z0-9\s!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~]/).join('');
    <%  }
    }%>
    }

    var maxFileSize = 0;
    function ValidateSingleInputForREG(oInput) {
        console.log('Inside ValidateSingleInput function....');
        if (oInput.type == "file") {
            var sFileName = oInput.value;
            if (sFileName.length > 0) {
                var blnValid = false;
                blnValid = true;
                var ext = sFileName.split('.');
                ext = ext[ext.length - 1];
                var onlyValidext = '<%=ecomFileExt%>'.split(','); //jpg,jpeg,png,pdf,doc,docx,xls,xlsx
                for (var i = 0; i < onlyValidext.length; i++) {
                    if (ext == onlyValidext[i]) {
                        blnValid = false;
                        break;
                    }
                }


                if (blnValid) {
                    //alert("The following file format is not allowed: " + validFileExtensions.join(", "));
                    alert("<bean:message key="ecom.msg.invalidfileformat" bundle="<%=clientcode%>"/>");
                    oInput.value = "";
                    return false;
                }
            }

            var file_id = $(oInput).attr('id');
            var fileInput = document.getElementById(file_id);
            console.log(fileInput);
            if (fileInput.files.length > 0) {
                const fileSize = fileInput.files.item(0).size;
                const maxFileSize = '<%=ecomFileUpdSize%>' * 1024 * 1024;
                console.log(fileSize);
                console.log(maxFileSize);
                if (fileSize >= maxFileSize) {
                    fileInput.value = "";
                    alert("Please select a file less than 10MB.");
                }
            }
        }
        return true;
    }
    $(document).ready(function () {
    <%if (postalcode_required.length() > 2) {%>
        $('#company_postcode').addClass('required');
    <%}%>
    <%if (companyphone_required.length() > 2 && !"Y".equalsIgnoreCase(eitherCompMobOrPhoneMandatory)) {%>
        $('#company_country_code').addClass('required');
        $('#company_phone_no').addClass('required');
    <%}%>
    
    <% if ("Y".equalsIgnoreCase(eitherCompMobOrPhoneMandatory) && "Y".equalsIgnoreCase(enableCompanyMobile)) {%>
        // When user types in Company Phone, clear Company Mobile errors (mobile not required)
        $('#company_country_code, #company_phone_code, #company_phone_no').on('keyup change', function() {
            var compPhoneCountryCode = $.trim($('#company_country_code').val());
            var compPhoneNo = $.trim($('#company_phone_no').val());
            var compMobileCode = $.trim($('#company_mobile_code').val());
            var compMobileNo = $.trim($('#company_mobile_no').val());
            
            // If user is filling Company Phone, Company Mobile is not required
            if (compPhoneCountryCode.length > 0 || compPhoneNo.length > 0) {
                $('#company_mobile_code, #company_mobile_no').removeClass('error');
                $('#company_mobile_code').next('label.error').remove();
                // Hide * from Company Mobile label, show * on Company Phone label
                $('#company_mobile_mandatory').hide();
                $('#company_phone_mandatory').show();
            }
            // If phone is completely filled, clear phone errors too
            if (compPhoneCountryCode.length > 0 && compPhoneNo.length > 0) {
                $('#company_country_code, #company_phone_no').removeClass('error');
                $('#company_country_code').next('label.error').remove();
            }
            // If both are empty, show * on both
            if (compPhoneCountryCode.length == 0 && compPhoneNo.length == 0 && compMobileCode.length == 0 && compMobileNo.length == 0) {
                $('#company_phone_mandatory').show();
                $('#company_mobile_mandatory').show();
            }
        });
        
        // When user types in Company Mobile, clear Company Phone errors (phone not required)
        $('#company_mobile_code, #company_mobile_no').on('keyup change', function() {
            var compMobileCode = $.trim($('#company_mobile_code').val());
            var compMobileNo = $.trim($('#company_mobile_no').val());
            var compPhoneCountryCode = $.trim($('#company_country_code').val());
            var compPhoneNo = $.trim($('#company_phone_no').val());
            
            // If user is filling Company Mobile, Company Phone is not required
            if (compMobileCode.length > 0 || compMobileNo.length > 0) {
                $('#company_country_code, #company_phone_no').removeClass('error');
                $('#company_country_code').next('label.error').remove();
                // Hide * from Company Phone label, show * on Company Mobile label
                $('#company_phone_mandatory').hide();
                $('#company_mobile_mandatory').show();
            }
            // If mobile is completely filled, clear mobile errors too
            if (compMobileCode.length > 0 && compMobileNo.length > 0) {
                $('#company_mobile_code, #company_mobile_no').removeClass('error');
                $('#company_mobile_code').next('label.error').remove();
            }
            // If both are empty, show * on both
            if (compPhoneCountryCode.length == 0 && compPhoneNo.length == 0 && compMobileCode.length == 0 && compMobileNo.length == 0) {
                $('#company_phone_mandatory').show();
                $('#company_mobile_mandatory').show();
            }
        });
    <%}%>
    });

</script>
