
<%@page import="net.ecom.common.Validation"%>
<%@page import="net.ecom.common.GeneralConf"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<style>
    /*    .form-horizontal .form-group {
            margin-right: 0px;
            margin-left: 0px;
        }*/
    
    /* Resend OTP button styling */
    #resendOtpBtn {
        color: #007bff;
        text-decoration: none;
        font-size: 12px;
        padding: 2px 8px;
        border: none;
        background: none;
        transition: color 0.3s ease;
    }
    
    #resendOtpBtn:hover {
        color: #0056b3;
        text-decoration: underline;
    }
    
    #resendOtpBtn:disabled {
        color: #6c757d;
        cursor: not-allowed;
        text-decoration: none;
    }
    
    #resendOtpStatus {
        display: block;
        margin-top: 5px;
        font-size: 11px;
    }
</style>

<%
    String toDisplayGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
    String strErrorMsg1 = (String) request.getAttribute("ErrorMsg1");
    String strErrorMsg2 = (String) request.getAttribute("ErrorMsg2");
    String strErrorMsg3 = (String) request.getAttribute("ErrorMsg3");

    /*    if (strErrorMsg1 == null) {
        strErrorMsg1 = "";
    } else {
        strErrorMsg1 = strErrorMsg1;
    }

    if (strErrorMsg2 == null) {
        strErrorMsg2 = "";
    } else {
        strErrorMsg2 = strErrorMsg2;
    }

    if (strErrorMsg3 == null) {
        strErrorMsg3 = "";
    } else {
        strErrorMsg3 = strErrorMsg3;
    }
     */
    String LoginName = (String) session.getAttribute("LoginName");
    String OPassword = (String) session.getAttribute("OPassword");
    String NPassword = (String) session.getAttribute("NPassword");
    String UserEmail = (String) session.getAttribute("UserEmail");

    String login = (String) session.getAttribute("SESSION_LOGINNAME");

    if (LoginName == null) {
        LoginName = "";
    } else {
        LoginName = LoginName;
    }

    if (OPassword == null) {
        OPassword = "";
    } else {
        OPassword = OPassword;
    }

    if (NPassword == null) {
        NPassword = "";
    } else {
        NPassword = NPassword;
    }

    if (UserEmail == null) {
        UserEmail = "";
    } else {
        UserEmail = UserEmail;
    }
    session.setAttribute("strType", "new");
    String contextRoot = request.getContextPath();
    String clientcode = (String) session.getAttribute("clientcode");
    String user = (String) session.getAttribute("sessionAdmin");

    String isPasswordExpired = (String) session.getAttribute("isPasswordExpired");
    String PasswordValidationMsg = (String) session.getAttribute("PasswordValidationMsg");
    String chgPassCaptcha = (String) getServletContext().getAttribute(GeneralConf.Enable_ChangePassword_Captcha);
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    Object attemptCountObj = session.getAttribute("ChangePasswordCount");
    int passattemptCount = (attemptCountObj != null) ? (int) attemptCountObj : 0;
    String passexpotp = (String) request.getServletContext().getAttribute(GeneralConf.Enable_OTP_Password_Expired);
%>
<div class="card-header page_header fixed-top"><img class="header_icon_pages" src="Design/assets/images/page_icons/chan_pass.png" /> <bean:message key="ecom.pwdreset" bundle="<%=clientcode%>" /></div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">

            <html:form styleId="PasswordResetForm" method="post" action="passwordreset.do" styleClass="form-horizontal">
                <input type="hidden" id="htype" name="htype" value="new"/>
                <div class="form-group alert-error" id="loginerror" style="display:none">
                    <div class="alert alert-danger alert-dismissible" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <strong><bean:message key="ecom.msg.warning" bundle="<%=clientcode%>"/></strong>  <bean:message key="ecom.msg.involdpwd" bundle="<%=clientcode%>"/> </div>
                </div>
                <%if ("Y".equalsIgnoreCase(isPasswordExpired)) {%>
                <div class="form-group alert-error" >
                    <div class="alert alert-danger alert-dismissible" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <strong> <%=PasswordValidationMsg%> </strong> </div>
                </div>
                <%}%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="Res.oldpassword" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                    <div class="col-sm-3">
                        <input id="login" name="login" type="hidden" class="usernameValidation" value="<%=login%>"/>
                        <html:password property="oldpassword" maxlength="20" styleId="Old" styleClass="form-control light_border required"/>
                        <label id="Old-error" class="error" for="Old"></label>
                    </div>

                    <label class="col-sm-2 control-label"><bean:message key="Res.newpassword" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                    <div class="col-sm-3">
                        <html:password property="newpassword" maxlength="20" styleId="New" styleClass="form-control light_border required PasswordValidationNote" onblur="checkpassword(this.value)"/>
                    </div>

                </div>

                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="Res.repassword" bundle="<%=clientcode%>" /> <span class="mandatory">*</span> </label>
                    <div class="col-sm-3">
                        <html:password property="repassword" maxlength="20" styleId="Retype" styleClass="form-control light_border required"/>
                    </div>
                    <%if ("Y".equalsIgnoreCase(chgPassCaptcha) && (passattemptCount >= 3)) {%>
                    <label class="col-sm-2 control-label">Captcha<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <div id="passresetrecaptiid" ></div>
                        <div id="g-recaptcha-error-passreset"></div>  
                    </div>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(passexpotp) && "Y".equalsIgnoreCase(isPasswordExpired)) {%>
                    <label class="col-sm-2 control-label">OTP<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                          <html:text property="otp" maxlength="6" styleId="otp" styleClass="form-control light_border required"/>
                          <div class="mt-2">
                              <button type="button" id="resendOtpBtn" class="btn btn-link btn-sm" onclick="resendOTP()">
                                  <i class="fa fa-refresh"></i> <bean:message key="ecom.resend.otp" bundle="<%=clientcode%>"/>
                              </button>
                              <span id="resendOtpStatus" class="text-muted small"></span>
                          </div>
                    </div>
                    <%}%>
<!--                    <label class="col-sm-2 control-label"><bean:message key="Res.captchachar" bundle="<%=clientcode%>" /> <span class="mandatory">*</span></label>
<div class="col-sm-3">
<input type="text" name="captcha" id="captcha" class="form-control light_border required" />
</div>

<div class="col-sm-2">
<img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
<a class="refresh_cap"  style="color:blue" title="Reload captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');" href="javascript:void(0);"><i class="fa fa-refresh"></i></a>
</div>

<div class="row form-group col-sm-2 control-label">

<div class="col-sm-13">
<div id="recaptiid" class="g-recaptcha" styleClass="form-control light_border required" data-sitekey="<%=siteKey%>"    required="true" data-expired-callback="recaptchaExpired" data-callback="verifyCaptcha" style="padding-left: 70px;"></div>
<div id="g-recaptcha-error"></div>  
</div>
</div>-->

                </div> 


                <div class="form-group row">
                    <label class="col-sm-2 control-label"></label>
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary btn-lg" id="searchID" <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%>> <bean:message key="ecom.submit" bundle="<%=clientcode%>" /></button>
                    </div>
                </div>
                <!--
            
            <div class="form-group row">
                <label class="col-sm-2 control-label"></label>
                <div class="col-sm-3">
                    <button type="submit" class="btn btn-primary btn-lg" id="searchID" <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%> onclick="checkGoogleRecaptcha();"> <bean:message key="ecom.submit" bundle="<%=clientcode%>" /></button>
                </div>
            </div> 
           
                -->
            </html:form>
        </div>
    </div>
</div>
</div>


<script>
    var contextRoot = "<%=contextRoot%>";
    var remotevalidate = contextRoot + "/CaptchaValidation";
    var capchagenerate = contextRoot + "/CaptchaGeneration?";
    var remotepassword = contextRoot + "/UserProfileAjax?type=changepass";
    var enableGgleCaptcha = "<%=toDisplayGgleCaptcha%>";
    var otpValidation=contextRoot + "/ecomAjax?type=ValidateOTP";
    var resendOtpUrl = contextRoot + "/ecomAjax?type=ResendOTP";

    var PassResetRecaptchaWidgetId;

    function CreateNewCaptcha(contextRoot)
    {
        var d = new Date();
        $("#captchaImage").attr('src', capchagenerate + d.getTime());
        $("#captcha").val('');
    }
    $(document).ready(function () {

        renderCaptcha();
        $('#captcha').keyup(function () {
            if (this.value.match(/[^a-zA-Z0-9]/g)) {
                console.log("coming>>");
                this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
            }
        });

        $('#PasswordResetForm').attr('autocomplete', 'off');
        $("#PasswordResetForm").validate({
            rules: {
                oldpassword: {
                    required: true
                            /*
                             remote: {
                             url: remotepassword,
                             type:"post",
                             data: {'userpassword': function () {
                             return $('#Old').val()
                             }, 'login': function () {
                             return $('#login').val()
                             }
                             }
                             }
                             */
                },
                newpassword: {
                    checkPassword: "#New"
                },
                repassword: {
                    equalTo: "#New"
                },
                captcha: {
                    required: true,
                    remote: remotevalidate
                },
                otp: {
                    required: true,
                    remote: otpValidation
                }
            },
            messages: {
                oldpassword: {
                    required: "<bean:message key='ecom.msg.fieldreq' bundle='<%=clientcode%>'/>"
                            /* 
                             remote: "<1bean:message key="ecom.msg.incorrectoldpwd" bundle="<clientcode%>"/>"
                             */
                },
                repassword: {
                    equalTo: "<bean:message key='ecom.msg.mismatchpwd' bundle='<%=clientcode%>'/>"
                },
                captcha: {
                    required: "<bean:message key='ecom.msg.entcapchacode' bundle='<%=clientcode%>'/>",
                    remote: "<bean:message key='ecom.msg.incorrectcaptcha' bundle='<%=clientcode%>'/>"
                },
                otp: {
                    required: "<bean:message key='ecom.msg.fieldreq' bundle='<%=clientcode%>'/>",
                    remote:"Enter a valid OTP."
                }
            },
            submitHandler: function (form) {

    <%if ("Y".equalsIgnoreCase(chgPassCaptcha) && (passattemptCount >= 3)) {%>
                var response = grecaptcha.getResponse(PassResetRecaptchaWidgetId);
                if (response.length == 0) {
                    document.getElementById('g-recaptcha-error-passreset').innerHTML = '<span style="color:red;"><bean:message key="ecom.gglecaptcha.err" bundle="<%=clientcode%>"/></span>';
                    return false;
                }
    <%}%>
                /* var oldPassword = $('#Old').val();
                 var loginName = $('#login').val();
                 
                 // Perform an AJAX request to validate the old password
                 /* $.ajax({
                 url: remotepassword,
                 type: 'POST',
                 data: {
                 userpassword: oldPassword,
                 login: loginName
                 },
                 success: function (response) {
                 if (response === "true") {
                 form.submit(); // Only submit if old password is correct
                 } else {
                 // alert('<1bean:message key="ecom.msg.incorrectoldpwd" bundle="<=clientcode%>"/>');
                 $('#Old-error').text('<bean1:message key="ecom.msg.incorrectoldpwd" bundle="<=clientcode%>"/>');
                 $('#Old-error').show();
                 }
                 },
                 error: function (error) {
                 console.log("Server Error: " + error);
                 }
                 });*/
                return true;
            }
        });
    });

    function checkpassword(pass)
    {
        if (document.PasswordResetForm.Old.value != "" && document.PasswordResetForm.New.value != "") {
            if (document.PasswordResetForm.Old.value == document.PasswordResetForm.New.value) {
                alert('<bean:message key="ecom.msg.samepwd" bundle="<%=clientcode%>"/>');
                $("#New").val("");
                $("#Retype").val("");
                document.PasswordResetForm.New.focus();
                return false;
            }
        }

    }

    function renderCaptcha() {
    <%if ("Y".equalsIgnoreCase(chgPassCaptcha) && (passattemptCount >= 3)) {%>
        PassResetRecaptchaWidgetId = grecaptcha.render("passresetrecaptiid", {
            'sitekey': '<%=siteKey%>'
        });
    <%}%>
    }

    function resendOTP() {
        var resendBtn = $('#resendOtpBtn');
        var statusSpan = $('#resendOtpStatus');
        
        // Check if OTP feature is enabled
        var isOtpEnabled = "<%=passexpotp%>";
        var isPasswordExpired = "<%=isPasswordExpired%>";
        
        if (isOtpEnabled !== "Y" || isPasswordExpired !== "Y") {
            statusSpan.html('<span class="text-warning"><i class="fa fa-info-circle"></i> OTP feature is not available.</span>');
            return;
        }
        
        // Disable button and show loading
        resendBtn.prop('disabled', true);
        resendBtn.html('<i class="fa fa-spinner fa-spin"></i> Sending...');
        statusSpan.html('<i class="fa fa-spinner fa-spin"></i> Sending OTP...');
        
        $.ajax({
            url: resendOtpUrl,
            type: 'POST',
            dataType: 'json',
            timeout: 30000, // 30 second timeout
            success: function(response) {
                if (response && response.success) {
                    statusSpan.html('<span class="text-success"><i class="fa fa-check"></i> OTP sent successfully!</span>');
                    // Clear OTP field
                    $('#otp').val('');
                    // Auto-hide success message after 3 seconds
                    setTimeout(function() {
                        statusSpan.html('');
                    }, 3000);
                } else {
                    var errorMsg = (response && response.message) ? response.message : 'Failed to send OTP';
                    statusSpan.html('<span class="text-danger"><i class="fa fa-exclamation-triangle"></i> ' + errorMsg + '</span>');
                }
            },
            error: function(xhr, status, error) {
                var errorMessage = 'Failed to send OTP. Please try again.';
                if (status === 'timeout') {
                    errorMessage = 'Request timed out. Please try again.';
                } else if (xhr.status === 403) {
                    errorMessage = 'Access denied. Please refresh the page.';
                } else if (xhr.status === 500) {
                    errorMessage = 'Server error. Please try again later.';
                }
                statusSpan.html('<span class="text-danger"><i class="fa fa-exclamation-triangle"></i> ' + errorMessage + '</span>');
                console.error('Resend OTP error:', error);
            },
            complete: function() {
                // Re-enable button after 30 seconds
                setTimeout(function() {
                    resendBtn.prop('disabled', false);
                    resendBtn.html('<i class="fa fa-refresh"></i> <bean:message key="ecom.resend.otp" bundle="<%=clientcode%>"/>');
                }, 30000);
            }
        });
    }

</script>

<%
    if (strErrorMsg1 != null || strErrorMsg2 != null || strErrorMsg3 != null) {
%>
<script type="text/javascript">
    $('#loginerror').show();

</script>
<%            }
%>

