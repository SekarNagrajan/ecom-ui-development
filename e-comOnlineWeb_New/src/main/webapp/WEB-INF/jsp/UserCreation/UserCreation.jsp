<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="net.ecom.common.Validation,java.util.ArrayList,net.ecom.vo.UserData,net.ecom.vo.CustomerProfile"%>
<%@page import="net.ecom.common.Validation,net.ecom.common.GeneralConf"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
    session.setAttribute("RegValidate", "Send");
    String contextRoot = request.getContextPath();
    String errors = Validation.checkNull((String) session.getAttribute("regerrors"));

    String clientcode = (String) session.getAttribute("clientcode");

    CustomerProfile profileVO = (CustomerProfile) session.getAttribute("viewusrArray");

    String strcompname = "";

    if (profileVO.getCustomerName() != null && profileVO.getCustomerName().length() > 0) {
        strcompname = profileVO.getCustomerName();
    } else {
        strcompname = "-";
    }
    int accesscount = 0;
    String accessallowed = (String) session.getAttribute("accessallowed");
    if (accessallowed != null && accessallowed.length() > 0) {
        accesscount = Integer.parseInt(accessallowed);
    }
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);

%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.usercreation" bundle="<%=clientcode%>"/> </div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="details_search">

            <div class="col-sm-12">

                <%if (errors.length() > 0 && errors.equalsIgnoreCase("regerrors")) {
                %>
                <div class="row">
                    <div class="alert alert-success alert-dismissible">
                        <html:errors></html:errors >
                        </div>
                    </div>
                <%}%>
                <form action="Accesslogin.do?type=usercreationsubmit" id="UserCreationForm" class="form-horizontal" method="post">
                    <div class="form-group row">
                        <label class="col-sm-1 control-label"><bean:message key="Reg.ComapanyName" bundle="<%=clientcode%>" /><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <input type="text" name="comname"  id="comname" class="form-control light_border setdata" disabled="disabled" value="<%=strcompname%>">
                            <input type="hidden" name="usercompany" id="usercompany" class="form-control setdata"   value="<%=strcompname%>"  />
                            <input type="hidden" name="companyname" id="companyname" value="<%=strcompname%>"/>
                            <input type="hidden" name="accesscount" id="accesscount" value="<%=accesscount%>" />
                        </div>
                        <div>
                            <input type="hidden" name="companyId" id="companyId"/>
                            <input id="hiddencity" name="hiddencity" type="hidden"/>
                            <input id="hiddencountry" name="hiddencountry" type="hidden"/>
                            <input id="hiddenstate" name="hiddenstate" type="hidden"/>
                            <input type="hidden" name="hiddencustname" id="hiddencustname"/>
                            <input type="hidden" name="hiddenEmail" id="hiddenEmail"/>
                            <input type="hidden" name="agencycode" id="agencycode"/>
                            <input type="hidden" name="countrycode" id="countrycode"/>
                            <input type="hidden" name="savetype" id="savetype" value="new"/>
                            <input type="hidden"  name="email"  id="email"/>
                            <input type="hidden" name="city_desc" id="city_desc"/>
                        </div>

                        <label class="col-sm-2 control-label"><bean:message key="Reg.emailid" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <input type ="text" name="userloginname"  id="userloginname" class="form-control light_border required email setdata usernameValidation"  maxlength="50"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-1 control-label"><bean:message key="Reg.password" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <input type='password' name="userpassword"  id="userpassword" class="form-control light_border required setdata PasswordValidationNote"  maxlength="20" onblur="checkpasswordUserCreation();" />
                        </div>

                        <label class="col-sm-2 control-label"><bean:message key="Reg.Cpassword" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <input type='password' name="usercpassword"   id="usercpassword" class="form-control light_border required setdata" maxlength="20" onblur="checkpasswordUserCreation();"></div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-1 control-label"><bean:message key="Reg.firstname" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <input type ="text"  name="firstname" id="firstname" class="form-control light_border required setdata" maxlength="50"/>
                        </div>
                        <label class="col-sm-2 control-label"><bean:message key="Reg.LastName" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <input type ="text"  name="lastname" id="lastname" class="form-control light_border required setdata" maxlength="50"/>
                        </div>
                    </div>


                    <div class="form-group row">
                        <label class="col-sm-1 control-label"><bean:message key="Reg.Phone" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-1">
                            <input type ="text"  name="cust_country_code" id="cust_country_code" class="form-control light_border required number " maxlength="4"/>
                        </div>
                        <div class="col-sm-1">
                            <input type ="text"  name="cust_phone_code" id="cust_phone_code"  class="form-control light_border number" maxlength="4"/>
                        </div>
                        <div class="col-sm-2">
                            <input type ="text"  name="cust_phone_no" id="cust_phone_no" class="form-control light_border required number" maxlength="10"/>
                        </div>

                        <label class="col-sm-2 control-label"><bean:message key="Reg.Mobile" bundle="<%=clientcode%>"/></label>
                        <div class="col-sm-1">
                            <input type ="text"  name="mobile_code" id="mobile_code"   class="form-control light_border number" maxlength="4"/>
                        </div>
                        <div class="col-sm-3">
                            <input type ="text"  name="mobile_no" id="mobile_no"  class="form-control light_border number" maxlength="20"/>

                        </div>
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="form-group row">

                        <label class="col-sm-1 control-label"><bean:message key="Reg.Fax" bundle="<%=clientcode%>"/></label>
                        <div class="col-sm-1">
                            <input type ="text"  name="fax_country_code"   id="fax_country_code" class="form-control light_border number" maxlength="4"/>
                        </div>
                        <div class="col-sm-1">
                            <input type ="text"  name="fax_phone_code"  id="fax_phone_code" class="form-control light_border number" maxlength="4"/>
                        </div>
                        <div class="col-sm-2">
                            <input type ="text"  name="fax_phone_no"  id="fax_phone_no" class="form-control light_border number" maxlength="10"/>

                        </div>

                        <label class="col-sm-2 control-label"><bean:message key="Registration.DefaultView" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <select name="deflanguage" id="deflanguage" class="form-control light_border required">
                                <option value="EN">ENGLISH</option>   
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-1 control-label"><bean:message key="Registration.PreferredView" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <select name="preflanguage" id="preflanguage" class="form-control light_border required">

                                <%
                                    LinkedHashMap langlist = (LinkedHashMap) getServletContext().getAttribute(GeneralConf.LangSelection);
                                    System.out.println("[ECOM-WAR] UserCreation hashmaplangrest" + langlist);
                                    Iterator it = null;
                                    it = langlist.entrySet().iterator();
                                    while (it.hasNext()) {
                                        Map.Entry pairs = (Map.Entry) it.next();

                                %>
                                <option value="<%=pairs.getKey().toString()%>"><%=pairs.getValue().toString()%></option>
                                <%
                                    }
                                %>

                            </select>
                        </div>
                        <label class="col-sm-2 control-label"><bean:message key="Reg.captcha" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-2">
                            <input type ="text" name="captcha" id="captcha"  class="form-control light_border required"/>
                        </div>
                        <div class="col-sm-3">
                            <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
                            <a class="refresh_cap" title="Reload captcha" onClick="CreateNewCaptcha();" href="javascript:void(0);"><i class="fa fa-refresh"></i></a>
                        </div>
                    </div>
                    <%} else {%>
                    <div class="form-group row">



                        <label class="col-sm-1 control-label"><bean:message key="Registration.DefaultView" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <select name="deflanguage" id="deflanguage" class="form-control light_border required">
                                <option value="EN">ENGLISH</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-1 control-label"><bean:message key="Registration.PreferredView" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <select name="preflanguage" id="preflanguage" class="form-control light_border required">
                                <%
                                    HashMap langlist = (HashMap) getServletContext().getAttribute(GeneralConf.LangSelection);
                                    Iterator it = null;
                                    it = langlist.entrySet().iterator();
                                    while (it.hasNext()) {
                                        Map.Entry pairs = (Map.Entry) it.next();

                                %>
                                <option value="<%=pairs.getKey().toString()%>"><%=pairs.getValue().toString()%></option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                        <label class="col-sm-2 control-label"><bean:message key="Reg.captcha" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <div class="col-sm-2">
                            <input type ="text" name="captcha" id="captcha"  class="form-control light_border required"/>
                        </div>
                        <div class="col-sm-3">
                            <img src="<%=contextRoot%>/CaptchaGeneration"  id="captchaImage"/>
                            <a class="refresh_cap" title="Reload captcha" onClick="CreateNewCaptcha();" href="javascript:void(0);"><i class="fa fa-refresh"></i></a>
                        </div>
                    </div>

                    <%}%>
                    <div class="form-group row">

                        <div class="col-sm-2 offset-md-5" >
                            <button type="submit" class="btn btn-primary btn-lg" autocomplete="off" id="submitbtn"> <bean:message key="ecom.submit" bundle="<%=clientcode%>" /></button>
                            <button type="reset" class="btn btn-danger btn-lg" onClick="$('#UserCreationForm label.error').remove();$('#UserCreationForm input.error').css('border-color', '#ccc');" id="resetbtn"> <bean:message key="ecom.reset" bundle="<%=clientcode%>" /></button>
                        </div>
                    </div>

                </form>

            </div>

        </div>
    </div>
</div>

<div id="userdiv"></div>

<script type="text/javascript" src="js/registration.js"></script>
<script type="text/javascript">
                                $(document).ready(function () {
                                    $('#company_country_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                    $('#company_phone_code').attr('placeholder', '<bean:message key="Reg.area" bundle="<%=clientcode%>"/>');
                                    $('#company_phone_no').attr('placeholder', '<bean:message key="Reg.phonenum" bundle="<%=clientcode%>"/>');

                                    $('#cust_country_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                    $('#cust_phone_code').attr('placeholder', '<bean:message key="Reg.area" bundle="<%=clientcode%>"/>');
                                    $('#cust_phone_no').attr('placeholder', '<bean:message key="Reg.phonenum" bundle="<%=clientcode%>"/>');

                                    $('#mobile_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                    $('#mobile_no').attr('placeholder', '<bean:message key="reg.mobnum" bundle="<%=clientcode%>"/>');

                                    $('#fax_country_code').attr('placeholder', '<bean:message key="reg.countrycode" bundle="<%=clientcode%>"/>');
                                    $('#fax_phone_code').attr('placeholder', '<bean:message key="Reg.area" bundle="<%=clientcode%>"/>');
                                    $('#fax_phone_no').attr('placeholder', '<bean:message key="reg.faxnum" bundle="<%=clientcode%>"/>');
                                });
                                $(document).ready(function () {
                                    getuserList();
                                });

                                function getuserList()
                                {
                                    openLoader();
                                    var custcode = "";
                                    $.post("Accesslogin.do?type=usercreationview&custcode=" + custcode, function (data) {
                                        $("#userdiv").html(data);
                                        $('#resetbtn').click();
                                        closeLoader();
                                    });
                                }

                                //

                                var capreq = getXmlHttpRequestObject();

                                function deletUserDetails(userloginname, contactname) {

                                    var b = confirm("<bean:message key="ecom.msg.deactivateuserprofile" bundle="<%=clientcode%>"/>");
                                    if (b == false)
                                    {

                                        return false;
                                    } else
                                    {
                                        openLoader();
                                        if (capreq.readyState == 0 || capreq.readyState == 4)
                                        {
                                            var url = "UserProfileAjax?type=deleteuserdetails&loginname=" + $.trim(userloginname) + "&contactname=" + contactname;
                                            capreq.open("POST", url, false);
                                            capreq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                            capreq.onreadystatechange = getValrslt;
                                            capreq.send(null);
                                        }
                                    }

                                }

                                function getValrslt() {
                                    if (capreq.readyState == 4) {
                                        if (capreq.status == 200) {
                                            var response = capreq.responseText;
                                            getuserList();
                                            //  alert("User details De-Activated");
                                            // location.reload();

                                        }
                                    }
                                }

                                function activeUserDetails(userloginname, contactname) {

                                    $.ajax({
                                        type: "POST",
                                        cache: false,
                                        async: false,
                                        url: "ecomAjax?type=checkAccess",
                                        success: function (response) {
                                            document.getElementById('accesscount').value = response;
                                        }
                                    });
                                    var cnt = document.getElementById('usercount').value;
                                    var accesscount = document.getElementById('accesscount').value;
                                    if (parseInt(accesscount) <= parseInt(cnt))
                                    {
                                        alert(accesscount + "<bean:message key="ecom.msg.usercount" bundle="<%=clientcode%>"/>")
                                        return false;
                                    }

                                    var b = confirm("<bean:message key="ecom.msg.activateuserprofile" bundle="<%=clientcode%>"/>");
                                    if (b == false)
                                    {

                                        return false;
                                    } else
                                    {
                                        openLoader();
                                        if (capreq.readyState == 0 || capreq.readyState == 4)
                                        {
                                            var url = "UserProfileAjax?type=activeUserDetails&loginname=" + $.trim(userloginname) + "&contactname=" + contactname;
                                            capreq.open("POST", url, false);
                                            capreq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                            capreq.onreadystatechange = getactiveUserDetails;
                                            capreq.send(null);
                                        }
                                    }

                                }

                                function getactiveUserDetails() {
                                    if (capreq.readyState == 4) {
                                        if (capreq.status == 200) {
                                            var response = capreq.responseText;
                                            getuserList();
                                            //   alert("User details Actived");
                                            //location.reload();

                                        }
                                    }
                                }

                                //
                                var rootpath = "<%=contextRoot%>";
                                console.log(rootpath);

                                var remotepath = rootpath + "/CaptchaValidation";
                                var remotemail = rootpath + "/UserProfileAjax?type=useremail&emailtype=useremail";
                                var remotecompanymail = rootpath + "/UserProfileAjax?type=useremail&emailtype=companyemail";
                                var capchapath = rootpath + "/CaptchaGeneration?";
                                var remoteloginname = rootpath + "/UserProfileAjax?type=userid";
                                $(document).ready(function () {
                                    var form = document.forms[0];
                                    form.setAttribute("autocomplete", "off");
                                    $('#UserCreationForm').attr('autocomplete', 'off');

                                    $("#UserCreationForm").validate({
                                        rules: {
                                            userloginname: {
                                                required: true,
                                                minlength: 5,
                                                Numbersspecialonly: "#userloginname",
                                                remote: {
                                                    url: remoteloginname,
                                                    data: {'userid': function () {
                                                            return $('#userloginname').val()
                                                        }
                                                    }
                                                }
                                            },
                                            userpassword: {
                                                checkPassword: "#userpassword"
                                            },
                                            usercpassword: {
                                                equalTo: "#userpassword"
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
                                                        required: function (element) {
                                                            return $("#mobile_code").val() != "" || $("#mobile_no").val() != "";
                                                        },
                                                        ZeroTest: "#mobile_code"
                                                    },
                                            mobile_no:
                                                    {
                                                        required: function (element) {
                                                            return $("#mobile_code").val() != "" || $("#mobile_no").val() != "";
                                                        },
                                                        ZeroTest: "#mobile_no"
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
                                            captcha: {
                                                required: true,
                                                remote: remotepath
                                            },
                                            email: {
                                                required: true,
                                                remote: {
                                                    url: remotemail,
                                                    data: {'useremail': function () {
                                                            return $('#email').val()
                                                        }, 'compid': function () {
                                                            return $('#companyId').val()
                                                        }
                                                    }
                                                }
                                            },
                                            comp_email: {
                                                required: true,
                                                remote: {
                                                    url: remotecompanymail,
                                                    data: {'useremail': function () {
                                                            return $('#comp_email').val()
                                                        }, 'compid': function () {
                                                            return $('#companyId').val()
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        messages: {
                                            userloginname: {
                                                email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                                                required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                minlength: "<bean:message key="ecom.msg.mincharlengthpwd" bundle="<%=clientcode%>"/>",
                                                Numbersspecialonly: "<bean:message key="ecom.msg.validloginname" bundle="<%=clientcode%>"/>",
                                                remote: "<bean:message key="ecom.msg.existsloginname" bundle="<%=clientcode%>"/>"

                                            },
                                            usercpassword: {
                                                equalTo: "<bean:message key="err.reg.client.notmatchpassword" bundle="<%=clientcode%>"/>"
                                            },
                                            cust_country_code:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidcountrycode" bundle="<%=clientcode%>"/>"
                                                    },
                                            cust_phone_code:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidareacode" bundle="<%=clientcode%>"/>"
                                                    },
                                            cust_phone_no:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidphonenum" bundle="<%=clientcode%>"/>"
                                                    },
                                            mobile_code:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidcountrycode" bundle="<%=clientcode%>"/>"
                                                    },
                                            mobile_no:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidmobnum" bundle="<%=clientcode%>"/>"
                                                    },
                                            fax_country_code:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidfaxcountrycode" bundle="<%=clientcode%>"/>"
                                                    },
                                            fax_phone_code:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidfaxareacode" bundle="<%=clientcode%>"/>"
                                                    },
                                            fax_phone_no:
                                                    {
                                                        number: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>",
                                                        ZeroTest: "<bean:message key="ecom.msg.invalidfaxnum" bundle="<%=clientcode%>"/>"
                                                    },
                                            captcha: {
                                                required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                                remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                            },
                                            email: {
                                                email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                                                required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                remote: "<bean:message key="ecom.msg.regemail" bundle="<%=clientcode%>"/>"
                                            },
                                            agree:
                                                    {
                                                        required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                    },
                                            comp_email: {
                                                email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                                                required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                remote: "<bean:message key="ecom.msg.companyregemail" bundle="<%=clientcode%>"/>"
                                            }
                                        },
                                        submitHandler: function (form) {

                                            $.ajax({
                                                type: "POST",
                                                cache: false,
                                                async: false,
                                                url: "ecomAjax?type=checkAccess",
                                                success: function (response) {
                                                    document.getElementById('accesscount').value = response;
                                                }
                                            });
                                            var fax_country_code = '';
                                            var fax_phone_code = '';
                                            var fax_phone_no = '';

                                            var companyname = $('#companyname').val();
                                            var userloginname = $('#userloginname').val();
                                            var usercpassword = $('#userpassword').val();
                                            var firstname = $('#firstname').val();
                                            var lastname = $('#lastname').val();
                                            var cust_country_code = $('#cust_country_code').val();
                                            var cust_phone_code = $('#cust_phone_code').val();
                                            var cust_phone_no = $('#cust_phone_no').val();
                                            if ($('#fax_country_code').length > 0) {
                                                fax_country_code = $('#fax_country_code').val();
                                                fax_phone_code = $('#fax_phone_code').val();
                                                fax_phone_no = $('#fax_phone_no').val();
                                            }
                                            var mobile_code = $('#mobile_code').val();
                                            var mobile_no = $('#mobile_no').val();
                                            var deflanguage = $('#deflanguage').val();
                                            var preflanguage = $('#preflanguage').val();
                                            console.log("fax_country_code +> " + fax_country_code + " " + fax_phone_code + " " + fax_phone_no);

                                            var email = $('#email').val();

                                            var cnt = $('#usercount').val();
                                            var accesscount = $('#accesscount').val();
                                            if (parseInt(accesscount) <= parseInt(cnt))
                                            {
                                                alert(accesscount + "<bean:message key="ecom.msg.usercount" bundle="<%=clientcode%>"/>")
                                                return false;
                                            }

                                            var b = confirm("<bean:message key="ecom.msg.adduserprofile" bundle="<%=clientcode%>"/>");
                                            if (b == false)
                                            {
                                                return false;
                                            } else
                                            {
                                                var urlStr = "type=UserCreationsubmit&companyname=" + encodeURIComponent(companyname) + "&loginname=" + encodeURIComponent(userloginname) + "&password=" + encodeURIComponent(usercpassword) + "&firstname=" + encodeURIComponent(firstname) + "&lastname=" + encodeURIComponent(lastname) + "&cust_country_code=" + encodeURIComponent(cust_country_code) + "&cust_phone_code=" + encodeURIComponent(cust_phone_code) + "&cust_phone_no=" + encodeURIComponent(cust_phone_no) + "&fax_country_code=" + encodeURIComponent(fax_country_code) + "&fax_phone_code=" + encodeURIComponent(fax_phone_code) + "&fax_phone_no=" + encodeURIComponent(fax_phone_no) + "&mobile_code=" + mobile_code + "&mobile_no=" + encodeURIComponent(mobile_no) + "&deflanguage=" + encodeURIComponent(deflanguage) + "&preflanguage=" + encodeURIComponent(preflanguage) + "&email=" + encodeURIComponent(userloginname) + "&user_count=" + encodeURIComponent(cnt);
                                                if (capreq.readyState == 0 || capreq.readyState == 4)
                                                {
                                                    openLoader();
                                                    var url = "Accesslogin.do?" + urlStr;
                                                    capreq.open("POST", url, true);
                                                    capreq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                                    capreq.onreadystatechange = getValresult;
                                                    capreq.send(null);
                                                }
                                            }
                                        }
                                    });

                                });
                                function getValresult() {
                                    if (capreq.readyState == 4) {
                                        if (capreq.status == 200) {
                                            var response = capreq.responseText;
                                            getuserList();
                                            //    alert("Successfully User details added.");
                                            //location.reload();
                                            return false;
                                        }
                                    }
                                }
                                function CreateNewCaptcha()
                                {
                                    $("#captchaImage").attr('src', capchapath + Math.random());
                                    $("#captcha").val('');
                                }

                                $(document).ready(function () {
                                    $('#userloginname,#email,#userpassword,#usercpassword,#firstname,#lastname,#mobile_code,#mobile_no').val("");
                                    $('#cust_country_code,#cust_phone_code,#cust_phone_no,#fax_country_code,#fax_phone_code,#fax_phone_no,#captcha').val("");
                                    setTimeout(function () {
                                        $('#loginname').val('');
                                    }, 10);

                                    setTimeout(function () {
                                        $('#userpassword').val('');
                                    }, 10);

                                });

    <%
        if ("zh".equalsIgnoreCase(clientcode) || "ma".equalsIgnoreCase(clientcode) || "es".equalsIgnoreCase(clientcode)) {
    %>
                                toastr.info("<bean:message key="ecom.msg.formsineng" bundle="<%=clientcode%>"/>", "INFO");
    <%}%>


</script>
