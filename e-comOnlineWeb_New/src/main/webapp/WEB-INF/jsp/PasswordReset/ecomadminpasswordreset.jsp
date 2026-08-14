<%@page language="java" import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<style>
    /*    .form-horizontal .form-group {
            margin-right: 0px;
            margin-left: 0px;
        }*/
</style>

<%

    String strErrorMsg1 = (String) request.getAttribute("ErrorMsg1");
    String strErrorMsg2 = (String) request.getAttribute("ErrorMsg2");
    String strErrorMsg3 = (String) request.getAttribute("ErrorMsg3");

//    if (strErrorMsg1 == null) {
//        strErrorMsg1 = "";
//    } else {
//        strErrorMsg1 = strErrorMsg1;
//    }
//
//    if (strErrorMsg2 == null) {
//        strErrorMsg2 = "";
//    } else {
//        strErrorMsg2 = strErrorMsg2;
//    }
//
//    if (strErrorMsg3 == null) {
//        strErrorMsg3 = "";
//    } else {
//        strErrorMsg3 = strErrorMsg3;
//    }
    System.out.println("[ECOM-WAR] ecomadminpasswordreset ~~~~~~~~" + strErrorMsg1 + strErrorMsg2 + strErrorMsg3);

    String LoginName = (String) session.getAttribute("LoginName");
    String OPassword = (String) session.getAttribute("OPassword");
    String NPassword = (String) session.getAttribute("NPassword");
    String UserEmail = (String) session.getAttribute("UserEmail");
    session.setAttribute("adminpwdreset", "send");
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
    String clientcode = (String) session.getAttribute("clientcode");
    String contextRoot = request.getContextPath();
%>


<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/chan_pass.png" /> Change Password</div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">
            <html:form styleId="PasswordResetForm" method="post" action="passwordresetadmin.do" styleClass="form-horizontal">
                <div class="form-group alert-error" id="loginerror" style="display:none">
                    <div class="alert alert-danger alert-dismissible" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <strong>Warning!</strong> Invalid old Password </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="Res.oldpassword" bundle="<%=clientcode%>" /> <span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <input id="login" name="login" type="hidden" value="<%=login%>"/>
                        <html:password property="oldpassword_admin" maxlength="20" styleId="Old_admin" styleClass="form-control light_border required"/>
                    </div>

                    <label class="col-sm-2 control-label"><bean:message key="Res.newpassword" bundle="<%=clientcode%>" /> <span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:password property="newpassword_admin" maxlength="20" styleId="New_admin" styleClass="form-control light_border required" onblur="checkpassword(this.value)"/>
                    </div>

                </div>

                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="Res.repassword" bundle="<%=clientcode%>" /> <span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:password property="repassword_admin" maxlength="20" styleId="Retype_admin" styleClass="form-control light_border required"/>
                    </div>

                    <label class="col-sm-2 control-label"><bean:message key="track.capt.char" bundle="<%=clientcode%>"/>" <span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <input type="text" name="captcha" id="captcha" class="form-control light_border required" />
                    </div>

                    <div class="col-sm-2">
                        <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
                        <a class="refresh_cap"   title="Reload captcha" onClick="CreateNewCaptcha('<%=contextRoot%>');" href="javascript:void(0);" data-original-title="Reload captcha"><i class="fa fa-refresh"></i></a>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-2 control-label"></label>
                    <div class="col-sm-2">
                        <button type="submit" class="btn btn-primary btn-lg search_button" id="searchID"> Submit</button>
                    </div>
                </div>
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
    function CreateNewCaptcha(contextRoot)
    {
        var d = new Date();
        $("#captchaImage").attr('src', capchagenerate + d.getTime());
        $("#captcha").val('');
    }
    $(document).ready(function () {
        $('#captcha').keyup(function () {
            if (this.value.match(/[^a-zA-Z0-9]/g)) {
                console.log("coming>>");
                this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
            }
        });
        $('#PasswordResetForm').attr('autocomplete', 'off');
        $("#PasswordResetForm").validate({
            rules: {
                oldpassword_admin: {
                    required: true,
                    remote: {
                        url: "UserProfileAjax?type=adminPwdCheck",
                        data: {'q': function () {
                                return $('#Old_admin').val()
                            }
                        }
                    }
                },
                captcha: {
                    required: true,
                    remote: remotevalidate
                },
                newpassword_admin: {
                    required: true,
                    minlength: 5
                },
                repassword_admin: {
                    required: true,
                    equalTo: "#New_admin"
                }
            },
            messages: {
                oldpassword_admin: {
                    required: "This field is requied",
                    remote: "Invalid Old Password"
                },
                captcha: {
                    required: "Please Enter The Captcha Code",
                    remote: "Captcha Entered Incorrectly"
                },
                newpassword_admin: {
                    required: "This filed is required",
                    equalTo: "New and confirm Password should be same",
                    minlength: "Password must have at least 5 characters"

                },
                repassword_admin: {
                    required: "This filed is required",
                    equalTo: "New and confirm Password should be same"
                }
            }
        });
    });

    function checkpassword(pass)
    {
        if ($('#Old_admin').val() != "" && $('#New_admin').val() != "") {
            if ($('#Old_admin').val() == $('#New_admin').val()) {
                alert('Old and New Password are Same.');
                $("#New_admin").val("");
                $("#Retype_admin").val("");
                $("#New_admin").focus();
                return false;
            }
        }

    }

</script>

<%
    if (strErrorMsg1 != null || strErrorMsg2 != null || strErrorMsg3 != null) {
%>
<script type="text/javascript">
    $('#loginerror').show();

</script>
<%
    }
%>

