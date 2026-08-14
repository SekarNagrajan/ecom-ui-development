<%@page import="net.ecom.common.GeneralConf"%>
<%@page  contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%

    String strForgotPassword = "";
    String strForgotuserName = "";
    String sltOption = "";
    sltOption = String.valueOf(request.getAttribute("forgotpassword_select_Opion"));

    if (sltOption.equalsIgnoreCase("forgotpassword")) {
        strForgotPassword = String.valueOf(request.getAttribute("forgotpassword_display_Message"));
        if (strForgotPassword.equalsIgnoreCase(null) || strForgotPassword.equalsIgnoreCase("null")) {
            strForgotPassword = "";
        }
    } else {
        strForgotuserName = String.valueOf(request.getAttribute("forgotpassword_display_Message"));
        if (strForgotuserName.equalsIgnoreCase(null) || strForgotuserName.equalsIgnoreCase("null")) {
            strForgotuserName = "";
        }
    }
    String contextRoot = request.getContextPath();

    String captchaservlet = contextRoot + "/CaptchaGeneration";
    String clientcode = (String) session.getAttribute("clientcode");
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String toDisplayGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);

%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.head.forgotpwd" bundle="<%=clientcode%>" /></div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">

            <html:form action="ForgotPassword.do" styleId="ForgottenFormBean" method="post" styleClass="form-horizontal">
                <html:hidden property="strType" styleId="strType"/>
                <input type="hidden" id="forgotvalid" name="forgotvalid" />
                <div class="card-body">
                    <div class="col-lg-12">
                        <div class="form-group alert-error" style="display:none" id='alertResetSuccess'>
                            <div class="alert alert-success alert-dismissible" role="alert" >
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <strong><bean:message key="ecom.msg.success" bundle="<%=clientcode%>" /></strong> <bean:message key="ecom.Forgotten.ForgotPassword.Correct" bundle="<%=clientcode%>" /> </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 control-label"><bean:message key="forgot.login" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                        <div class="col-sm-9">
                            <html:text property="strLoginName" styleId="strLoginName" styleClass="form-control light_border required" maxlength="50" size="50" style="width:50%;"/>
                        </div>
                    </div>
                    <%if ("Y".equalsIgnoreCase(toDisplayGgleCaptcha)) {%>
                    <div class="form-group row">
                        <label class="col-sm-3 control-label"></label>
                        <div class="col-sm-9">
                            <div id="recaptid" class="g-recaptcha" styleClass="form-control light_border required" data-sitekey="<%=siteKey%>"    required="true" data-expired-callback="recaptchaExpired" data-callback="verifyCaptcha"></div>
                            <div id="g-recaptcha-error"></div>  
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 control-label"></label>
                        <div class="col-sm-9">
                            <button type="submit" onclick="submitResetPassword();checkGoogleRecaptcha();" class="btn btn-primary btn-lg"><bean:message key="ecom.submit" bundle="<%=clientcode%>" />  <span class="glyphicon glyphicon-circle-arrow-right"></span></button>
                            <!--<button type="button" class="btn btn-danger btn-lg" onClick="resetLoginvalues();$('#ForgottenFormBean label.error').remove();"><bean1:message key="ecom.reset" bundle="<=clientcode%>" />  <span class="glyphicon glyphicon-refresh"></span></button>-->
                        </div>
                    </div>
                    <%} else {%>

                    <div class="form-group row">
                        <label class="col-sm-3 control-label"><bean:message key="forgot.retype" bundle="<%=clientcode%>" /></label>
                        <div class="col-sm-9"> <img src="<%=captchaservlet%>" class="img-responsive img-thumbnail" id="imgId1"> <a class="refresh_cap" title="Reload captcha" onClick="CreateNewCaptchaRP('imgId1');" href="javascript:void(0);"><i class="fa fa-refresh"></i></a> </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 control-label"></label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control required light_border remove_autocomplete captcha" name="captcha" id="captcha" maxlength="5"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 control-label"></label>
                        <div class="col-sm-9">
                            <button type="submit" onclick="submitResetPassword();" class="btn btn-primary btn-lg"><bean:message key="ecom.submit" bundle="<%=clientcode%>" />  <span class="glyphicon glyphicon-circle-arrow-right"></span></button>
                            <!--<button type="button" class="btn btn-danger btn-lg" onClick="resetLoginvalues();$('#ForgottenFormBean label.error').remove();"><bean1:message key="ecom.reset" bundle="<=clientcode%>" />  <span class="glyphicon glyphicon-refresh"></span></button>-->
                        </div>
                    </div>
                    <%}%>


                </div>
            </html:form>
        </div>
    </div>

</div>


<script type="text/javascript">
    var contextRoot = "<%=contextRoot%>";
    var remotevalidate = contextRoot + "/CaptchaValidation";
    var capchavalidate = contextRoot + "/CaptchaGeneration?";
    var enableGgleCaptcha = "<%=toDisplayGgleCaptcha%>";

    $(document).ready(function () {
        $('#ForgottenFormBean').attr('autocomplete', 'off');
        $('#ForgottenLogin').attr('autocomplete', 'off');
        $("#strLoginName").val('');
        $("#strFirstName").val('');
        $("#strEmailId").val('');
        $("#strLoginName").focus();
        $("#ForgottenFormBean").validate({
            rules: {
                captcha: {
                    required: true,
                    remote: remotevalidate
                },
                strLoginName: {
                    required: true
                }

            },
            messages: {
                strLoginName: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                captcha: {
                    required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                    remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                }
            },

            submitHandler: function (form) {
                if (enableGgleCaptcha == 'Y') {
                    var captchaval = $('#g-recaptcha-response').val();
                    if ($('#g-recaptcha-response').length == 0 || captchaval == null || captchaval == 'null' || captchaval == '' || captchaval.length == 0) {
                        return false;
                    }
                }
                return true;
            }
        });
        $("#ForgottenLogin").validate({
            rules: {
                captchauser: {
                    required: true,
                    remote: remotevalidate
                }
            },
            messages: {
                captchauser: {
                    required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                    remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                },
                strLoginName: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            }
        });
    });
    function CreateNewCaptchaRP(IDs)
    {
        $("#" + IDs).attr('src', capchavalidate + Math.random());
        $("#captcha").val('');
    }
    function CreateNewCaptchaFL(IDs)
    {
        $("#" + IDs).attr('src', capchavalidate + Math.random());
        $("#captchauser").val('');
    }
    function submitResetPassword() {
        $("#strType").val('forgotpassword');
        $("#forgotvalid").val('forgotvalid');
        return true;
    }
    function checkGoogleRecaptcha() {

        var response = grecaptcha.getResponse();
        if (response.length == 0) {
            document.getElementById('g-recaptcha-error').innerHTML = '<span style="color:red;"><bean:message key="ecom.gglecaptcha.err" bundle="<%=clientcode%>"/></span>';
            return false;
        }
        return true;
    }

    function verifyCaptcha() {
        document.getElementById('g-recaptcha-error').innerHTML = '';
    }




    function submitLoginName() {
        $("#strType1").val('forgotloginname');
        $("#forgotvalid1").val('forgotvalid1');
        return true;
    }

    function recaptchaExpired() {
        alert("<bean:message key="ecom.gglecaptcha.expired" bundle="<%=clientcode%>"/>");
    }
    var resultVal = '<%=strForgotPassword%>';
    /*if (resultVal == 'mailSend') {
     $('#alertResetSuccess').show();
     } else if (resultVal == 'mailNotSend') {
     $('#alertResetFail').show();
     } */
    if (resultVal.length > 0) {
        $('#alertResetSuccess').show();
    }
    var resultLoginName = '<%=strForgotuserName%>';
    if (resultLoginName == 'mailSend') {
        $('#alertForgotSuccess').show();
    } else if (resultLoginName == 'mailNotSend') {
        $('#alertForgotFail').show();
    }
    function resetLoginvalues() {
        $('#strLoginName').val("");
        $('#captcha').val("");
        $('#ForgottenFormBean label.error').remove();
        $('#alertResetSuccess').hide();
        $('#alertResetFail').hide();
    }
    function resetforgotvalues() {
        $('#strFirstName').val("");
        $('#strEmailId').val("");
        $('#captchauser').val("");
        $('#ForgottenLogin label.error').remove();
        $('#alertForgotSuccess').hide();
        $('#alertForgotFail').hide();
    }

</script>
