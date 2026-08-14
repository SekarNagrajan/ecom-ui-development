<%-- 
    Document   : LoginPopup
    Created on : Feb 24, 2020, 3:45:22 PM
    Author     : gobinath.t
--%>
<%@page import="net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String enable_encryption = (String) getServletContext().getAttribute(GeneralConf.Enable_Login_Password_Encryption);
    String accountLockedMessageForLoginFail = (String) getServletContext().getAttribute("AccountLockedMessageForLoginFail");
    if(Validation.checkNull(accountLockedMessageForLoginFail).length() == 0){
        accountLockedMessageForLoginFail = "Your account has been locked due to multiple failed login attempts. Please contact the agency to reactivate your account.";
    }
%>
<div id="mySidenav" class="sidenav_right_popup">
    <html:form styleId="loginformpopup" method="post" action="Accesslogin.do?type=login" styleClass="form-horizontal">
        <div class="login_box_popup">
            <div class="form-group row col-sm-12" id="loginerror" style="display:none">
                <div class="col-sm-12">
                    <div class="alert alert-danger text-center alert-dismissible fade show" role="alert">
                        <strong><bean:message key="ecom.login.invup" bundle="<%=clientcode%>"/></strong> 
                        <button type="button" class="close d-none" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                </div>
            </div>
            <div class="txt_line"><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></div>

            <div class="row form-group col-sm-12">
                <html:text property="userName" styleId="username" maxlength="50" styleClass = "input_box1 form-control required" />
            </div>

            <div class="row form-group col-sm-12">
                <html:password property="password" styleId="loginpasswordpop" maxlength="20" styleClass="input_box1 form-control required" />
            </div>

            <div class="row col-sm-12">
                <button class="login_btn" id="loginbtn" type="submit" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off"><bean:message key="ecom.login.Heading" bundle="<%=clientcode%>"/></button>
                <label class="error" id="loginerrormsgpop" style="margin-bottom: 5px;margin-top: 2px;"></label>
            </div>

            <div class="row col-sm-12">
                <div class="col-sm-12">
                    <span class="forget_txt">  <a href ="Accesslogin.do?type=forgot"><bean:message key="ForgottenPassword.Header.ForgotPassword" bundle="<%=clientcode%>"/> </a> </span> <br/>
                    <span class="dont_txt"><bean:message key="ecom.login.havingnoacc" bundle="<%=clientcode%>"/> <a href ="Accesslogin.do?type=Registration"  class="rem_txt" ><bean:message key="ecom.toregister" bundle="<%=clientcode%>"/> </a> </span>
                </div>
            </div>
        </div>

    </html:form>
</div>
<script>
    $(document).ready(function () {
        $('#username').attr('placeholder', '<bean:message key="ViewProfile.userifo.UserName" bundle="<%=clientcode%>"/>');
        $('#loginpasswordpop').attr('placeholder', '<bean:message key="login.password" bundle="<%=clientcode%>"/>');
    });
    // SHA-256 encryption function
    async function sha256(message) {
        const msgBuffer = new TextEncoder().encode(message);
        const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
        const hashArray = new Uint8Array(hashBuffer);
        let binary = '';
        hashArray.forEach((byte) => binary += String.fromCharCode(byte));
        return btoa(binary);  // Base64 encoding
    }
    $("#loginformpopup").validate({
        submitHandler: function (form) {
            // Disable the login button and show loading state
            $("#loginbtn").button('loading');
            // Optionally show loader (can be used if you have an actual loader)
            // $('#loginloader').removeClass('d-none').addClass('d-block');

            // Get the password value from the form
            const password = document.getElementById('loginpasswordpop').value;

            // Hash the password using SHA-256
            sha256(password).then(hashedPassword => {
                console.log('Hashed: ', hashedPassword);  // Debug log to check if password is hashed correctly

                // Set the hashed password back to the form
    <%if ("Y".equalsIgnoreCase(enable_encryption)) {%>
                document.getElementById('loginpasswordpop').value = hashedPassword;
    <%}%>

                // Create an object for the data to be sent in the AJAX request
                let formData = {};
                // Serialize all the form fields except the password (we'll manually add the hashed password)
                $('#loginformpopup').serializeArray().forEach(function (field) {
                    formData[field.name] = field.value;
                });
    <%if ("Y".equalsIgnoreCase(enable_encryption)) {%>
                // Ensure the password is added with the hashed value
                formData['password'] = hashedPassword;
    <%}%>
                // Perform AJAX call to validate login with the hashed password
                $.ajax({
                    url: "LoginAjax",
                    type: 'POST',
                    data: formData, // Send the data object with the hashed password
                    success: function (data) {
                        // Hide loader after AJAX success
                        // $('#loginloader').removeClass('d-block').addClass('d-none');

                        // Handle different responses based on server feedback
                        if (data === 'error') {
                            $('#loginformpopup').find('#loginerror').show();
                            $('#loginformpopup').find('#loginerror strong').html('Error Occurred!');
                        } else if (data === 'invalid') {
                            $('#loginformpopup').find('#loginerror').show();
                            $('#loginformpopup').find('#loginerror strong').html('Invalid Username / Password');
                        } else if (data === 'expired') {
                            $('#loginformpopup').find('#loginerror').show();
                            $('#loginformpopup').find('#loginerror strong').html('Your password has expired.');
                        } else if (data === 'accountLocked') {
                            $('#loginformpopup').find('#loginerror').show();
                            $('#loginformpopup').find('#loginerror strong').html('Login attempts exceeded. Account temporarily locked.');
                        } else if (data === 'alreadyAccountLocked') {
                            $('#loginformpopup').find('#loginerror').show();
                            $('#loginformpopup').find('#loginerror strong').html('Account is locked. Please try again later.');
                        } else if (data === 'inactivated') {
                            $('#loginerrormsgpop').show();
                            $('#loginerrormsgpop').html('Your account has expired. Please await the re-validation of your account by our agency.');
                        } else if(data === 'AccountLockedContactAgency'){
                            $('#loginerrormsgpop').show();
                            $('#loginerrormsgpop').html('<%=accountLockedMessageForLoginFail%>');
                        } else {
                            // If login is successful, redirect or submit form
                            // window.location.href = data; // Uncomment if you want to redirect
                            document.getElementById('booknows').submit(); // Submit the booking form
                        }
                    },
                    error: function (xhr) {
                        if (xhr.status === 403) {
                            window.location.href = "SessionExpired.jsp";
                        } else {
                            // Handle other error scenarios (optional)
                            alert("An error occurred while processing your login request.");
                        }
                    },
                    complete: function () {
                        // Re-enable the button after AJAX call (in case of success or error)
                        $("#loginbtn").button('reset');
                        // Optionally hide loader here as well
                        // $('#loginloader').removeClass('d-block').addClass('d-none');
                    }
                });
            });

            return false; // Prevent form submission to handle via AJAX
        }
    });


    /*    $("#loginformpopup").validate({
     submitHandler: function (form) {
     $("#loginbtn").button('loading');
     //$("#resetbtn").attr("disabled", true);
     //  return true;
     
     // new logic
     //  $('#loginloader').removeClass('d-none').addClass('d-block');
     console.log($('#loginformpopup').serializeArray());
     $.ajax({
     url: "LoginAjax",
     type: 'post',
     async: false,
     data: $('#loginformpopup').serializeArray(), //{username: $('#username').val(), password: $('#loginpassword').val()},
     success: function (data) {
     // $('#loginloader').removeClass('d-block').addClass('d-none');
     if (data == 'error') {
     $('#loginformpopup').find('#loginerror').show();
     $('#loginformpopup').find('#loginerror').find('strong').html('Error Occurred!');
     } else if (data == 'invalid') {
     $('#loginformpopup').find('#loginerror').show();
     $('#loginformpopup').find('#loginerror').find('strong').html('Invalid Username / Password');
     } else if (data == 'expired') {
     $('#loginformpopup').find('#loginerror').show();
     $('#loginformpopup').find('#loginerror').find('strong').html('Your password has expired.');
     } else if (data == 'accountLocked') {
     $('#loginformpopup').find('#loginerror').show();
     $('#loginformpopup').find('#loginerror').find('strong').html('Login attempts exceeded. Account temporarily locked.');
     } else if (data == 'alreadyAccountLocked') {
     $('#loginformpopup').find('#loginerror').show();
     $('#loginformpopup').find('#loginerror').find('strong').html('Account is locked. Please try again later.');
     } else if (data == 'inactivated') {
     $('#loginerrormsgpop').show();
     $('#loginerrormsgpop').html('Your account has expired. Please await the re-validation of your account by our agency.');
     } else {
     //window.location.href = data;
     document.getElementById('booknows').submit();
     }
     },
     error: function (xhr) {
     if (xhr.status == 403) {
     window.location.href = "SessionExpired.jsp";
     }
     }
     });
     return false;
     //
     
     }
     }); */
</script>