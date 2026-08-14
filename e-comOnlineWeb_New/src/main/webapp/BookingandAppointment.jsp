<%@page import="java.util.ArrayList,java.util.List,java.util.Hashtable,java.util.Set,java.util.Iterator,java.util.Random,net.ecom.vo.CustomerProfile"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>


<style>
    .form-horizontal .control-label {
        text-align: left;
    }
</style>
<%
    String stroutput = "";
    try {
        Random rand = new Random();
        int randnum = rand.nextInt(8000);
        String randomnumber = String.valueOf(randnum);
        session.setAttribute("bkgandapptrandomid", randomnumber);
        stroutput = String.valueOf(request.getAttribute("display_Message"));
        if (stroutput.equalsIgnoreCase(null) || stroutput.equalsIgnoreCase("null")) {
            stroutput = "";
        }
        CustomerProfile profileVO = (CustomerProfile) session.getAttribute("bookingandappmt");
        String strusername = "";
        String strcompname = "";
        String strcity = "";
        String strcountry = "";
        String struserphone = "";
        String strusermobile = "";
        String stremail = "";
        if (profileVO != null) {

            if (profileVO.getContactName() != null && profileVO.getContactName().length() > 0) {
                strusername = profileVO.getContactName();
            } else {
                strusername = "-";
            }
            if (profileVO.getCustomerName() != null && profileVO.getCustomerName().length() > 0) {
                strcompname = profileVO.getCustomerName();
            } else {
                strcompname = "-";
            }
            if (profileVO.getCity() != null && profileVO.getCity().length() > 0) {
                strcity = profileVO.getCity();
            } else {
                strcity = "-";
            }

            if (profileVO.getCountry() != null && profileVO.getCountry().length() > 0) {
                strcountry = profileVO.getCountry();
            } else {
                strcountry = "-";
            }

            if (profileVO.getUserPhoneNo() != null && profileVO.getUserPhoneNo().length() > 0) {
                struserphone = profileVO.getUserPhoneNo();
            } else {
                struserphone = "-";
            }

            if (profileVO.getMobileNo() != null && profileVO.getMobileNo().length() > 0) {
                strusermobile = profileVO.getMobileNo();
            } else {
                strusermobile = "-";
            }

            if (profileVO.getEmailId() != null && profileVO.getEmailId().length() > 0) {
                stremail = profileVO.getEmailId();
            } else {
                stremail = "-";
            }

        }
        String strUserName = (String) session.getAttribute("SESSION_LOGINNAME");
        String clientcode = (String) session.getAttribute("clientcode");
        String client_code = (String) getServletContext().getAttribute("client_code");

        Hashtable countrylist = (Hashtable) getServletContext().getAttribute("SESSION_CONTRYLIST");
        String country = "", countrycode = "", countryvalue = "";
        country = ((String) countrylist.get(strcountry) != null) ? (String) countrylist.get(strcountry) : strcountry;
        String user = (String) session.getAttribute("sessionAdmin");

%>
<div class="card-header page_header fixed-top"><img class="header_icon_pages" src="Design/assets/images/page_icons/contact.png" />  <bean:message key="home.bat.head" bundle="<%=clientcode%>"/> </div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="container">
            <div class="details_search">

                <html:form action="BkgandAppointment.do" styleId="BkgandAppointmentForm" method="post" styleClass="form-horizontal">

                    <div class="col-lg-12">
                        <div class="form-group alert-error" style="display:none" id='alertResetSuccess'>
                            <div class="alert alert-success alert-dismissible" role="alert" >
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <strong><bean:message key="ecom.msg.success" bundle="<%=clientcode%>"/></strong> <bean:message key="ecom.msg.bkgandapptussuccess1" bundle="<%=clientcode%>"/></div>
                        </div>
                        <div class="form-group alert-error" style="display:none" id='alertResetFail'>
                            <div class="alert alert-danger alert-dismissible" role="alert">
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <strong><bean:message key="ecom.msg.errormsg" bundle="<%=clientcode%>"/></strong><bean:message key="ecom.msg.contactusfailed" bundle="<%=clientcode%>"/></div>
                        </div>
                    </div>




                    <div class="col-md-6 column">
                        <div class="panel-body">
                            <%if (profileVO != null) {%>
                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Name" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <label class="col-sm-8 control-label" id="username"><%=strusername%></label><input type="hidden" id="username1" name="username1" value="<%=strusername%>"/>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.CompanyName" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <label class="col-sm-8 control-label" id="compname"><%=strcompname%></label><input type="hidden" id="compname1" name="compname1" value="<%=strcompname%>"/>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.Country" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <label class="col-sm-8 control-label" id="country"><%=country%></label><input type="hidden" id="country1" name="country1" value="<%=country%>"/>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.City" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <label class="col-sm-8 control-label" id="city"><%=strcity%></label><input type="hidden" id="city1" name="city1" value="<%=strcity%>"/>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Phone" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <label class="col-sm-8 control-label" id="userphone"><%=struserphone%></label><input type="hidden" id="userphone1" name="userphone1" value="<%=struserphone%>"/>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Mobile" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <label class="col-sm-8 control-label"  id="strusermobile"><%=strusermobile%></label><input type="hidden" id="strusermobile1" name="strusermobile1" value="<%=strusermobile%>"/>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Email" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <label class="col-sm-8 control-label" id="useremail"><%=stremail%></label><input type="hidden" id="useremail1" name="useremail1" value="<%=stremail%>"/>
                            </div>
                            <%--
                               <div class="form-group">
                                                            <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.ControllingAgency" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                                            <label class="col-sm-1 control-label">:</label>
                                                            <div class="col-sm-8">
                                                                <select name="agencyname" id="agencyname" class="form-control setdata required" aria-required="true"  >
                                                                    <option value="" selected="selected">-- Select --</option>
                                                                </select>
                                                            </div>

                                </div>
                            --%>

                            <%} else {%>
                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Name" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <html:text property="username" styleId="username" styleClass="form-control light_border" maxlength="100" size="50"/>

                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.CompanyName" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <html:text property="compname" styleId="compname" styleClass="form-control light_border" maxlength="50" size="50"/>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.Country" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <select name="country" id="country" class="form-control setdata light_border" aria-required="true" onblur="getCitylist(new Date().getTime());">
                                        <option value="" selected="selected">-- Select --</option>
                                        <%
                                            Set<String> keys = countrylist.keySet();
                                            Iterator<String> itr = keys.iterator();
                                            while (itr.hasNext()) {
                                                countrycode = itr.next();
                                                countryvalue = (String) countrylist.get(countrycode);
                                        %>
                                        <option value="<%=countrycode%>"><%=countryvalue%></option>
                                        <% }%>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.City" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" >
                                                <input type="hidden" id="othercitystatus" name="othercitystatus"/>
                                                <input type="hidden" id="city_desc" name="city_desc"/>
                                                <input type="checkbox" id="checkcitybtn" aria-label="..." onClick="citystatus(this.id)"/>     
                                                <label style="font-size:12px">Others </label>
                                            </span>
                                        </div>
                                        <select name="city" id="city" class="form-control required light_border setdata compcity"  onblur="setCityDesc('city');" style="display:block">
                                            <option value="" selected="selected">Select City</option>
                                        </select>
                                        <input type="text" id="other_city" name="other_city"  class="form-control light_border setdata othercity" onblur="setCityDesc('other_city');" maxlength="150" style="display:none"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Phone" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <html:text property="userphone" styleId="userphone" styleClass="form-control light_border setdata" maxlength="15" size="50"/>

                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Mobile" bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <html:text property="strusermobile" styleId="strusermobile" styleClass="form-control light_border setdata" maxlength="11" size="50"/>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.userifo.Email" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <html:text property="useremail" styleId="useremail" styleClass="form-control light_border email" maxlength="300" size="50"/>
                                </div>
                            </div>
                            <%}%>

                            <div class="form-group row">
                                <label class="col-sm-3 control-label"><bean:message key="home.bat.preftime" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <label class="col-sm-1 control-label">:</label>
                                <div class="col-sm-8">
                                    <html:text property="preftime" styleId="preftime" styleClass="form-control light_border required" maxlength="100" size="50"/>
                                    <input type="hidden" name="hiddenrandomid"  id="hiddenrandomid" value="<%=randomnumber%>" />
                                    <input type="hidden" name="randomnum" id="randomnum" />
                                    <input type="hidden" name="identify" id="identify"  value="yes"/>
                                    <input type="hidden" name="hiddenusername" id="hiddenusername"  value="<%=strUserName%>"/>
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-12"><div class="pull-right">  <button type="submit"  class="btn btn-primary btn-lg" autocomplete="off"  id="mailbtn" <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%>>   <bean:message key="ecom.mail" bundle="<%=clientcode%>"/></button>
                                        <button type="button" class="btn btn-danger btn-lg" onClick="$('#BkgandAppointmentForm label.error').remove();
                                                $('#BkgandAppointmentForm input.error').css('border-color', '#ccc');
                                                refreshpage();" id="resetbtn"><span class="glyphicon glyphicon-refresh"></span> <bean:message key="ecom.reset" bundle="<%=clientcode%>"/></button></div> </div>
                            </div>
                        </div>
                    </div>
                </html:form>
            </div>
        </div></div></div>


<script type="text/javascript" src="js/BookandAppointment.js"></script>
<script type="text/javascript">
                                            $(document).ready(function () {
                                                $("#BkgandAppointmentForm").validate({
                                                    rules: {
                                                        username: {
                                                            required: function (element) {
                                                                return $('#hiddenusername').val() == 'null' || $('#hiddenusername').val() == null
                                                            }
                                                        },
                                                        compname: {
                                                            required: function (element) {
                                                                return $('#hiddenusername').val() == 'null' || $('#hiddenusername').val() == null
                                                            }
                                                        },
                                                        country: {
                                                            required: function (element) {
                                                                return $('#hiddenusername').val() == 'null' || $('#hiddenusername').val() == null
                                                            }
                                                        },
                                                        userphone: {
                                                            required: function (element) {
                                                                return $('#hiddenusername').val() == 'null' || $('#hiddenusername').val() == null
                                                            },
                                                            phonefax: ["#userphone"]
                                                        },
                                                        strusermobile: {
                                                            phonefax: ["#strusermobile"]
                                                        },
                                                        useremail: {
                                                            required: function (element) {
                                                                return $('#hiddenusername').val() == 'null' || $('#hiddenusername').val() == null
                                                            }
                                                        }
                                                    },
                                                    messages: {
                                                        username: {
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                        },
                                                        compname: {
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                        },
                                                        country: {
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                        },
                                                        userphone: {
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                                            phonefax: "<bean:message key="ecom.msg.invalidphonenum" bundle="<%=clientcode%>"/>"
                                                        },
                                                        strusermobile: {
                                                            phonefax: "<bean:message key="ecom.msg.invalidmobnum" bundle="<%=clientcode%>"/>"
                                                        },
                                                        preftime: {
                                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                        },
                                                        useremail: {
                                                            email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>"
                                                        },

                                                    },
                                                    submitHandler: function (form) {
                                                        var hiddenrandom = $("#hiddenrandomid").val();
                                                        $("#randomnum").val(hiddenrandom);
                                                        return true;
                                                    }
                                                });
                                            });

                                            function sentcontactMail() {
                                                if (searchRequest.readyState == 4) {
                                                    var response = searchRequest.responseText;
                                                    if (response.toUpperCase() == "INVALID") {
                                                        $("#mailbtn").button('reset');
                                                        alert(" <bean:message key="ecom.msg.nocontmail" bundle="<%=clientcode%>"/>");
                                                        return false;
                                                    } else {
                                                        alert("<bean:message key="ecom.msg.contactus" bundle="<%=clientcode%>"/>");
                                                        if (hiddenusername == null || hiddenusername == "null" || hiddenusername.length == 0)
                                                        {
                                                            $("#mailbtn").button('reset');
                                                            document.getElementById('username').value = "";
                                                            document.getElementById('compname').value = "";
                                                            document.getElementById('country').value = "";
                                                            document.getElementById('agency').value = "";
                                                            document.getElementById('userphone').value = "";
                                                            document.getElementById('strusermobile').value = "";
                                                            document.getElementById('useremail').value = "";
                                                            document.getElementById('preftime').value = "";
                                                            document.getElementById('agencymail').value = "";
                                                            document.getElementById("country").selectedIndex = 0;
                                                            document.getElementById("agency").selectedIndex = 0;
                                                            return false;
                                                        } else
                                                        {
                                                            $("#mailbtn").button('reset');

                                                            return false;
                                                        }
                                                    }
                                                }
                                            }

                                            function refreshpage() {
                                                $('#username,#compname,#userphone,#strusermobile,#useremail,#preftime').val('');
                                                $('#BkgandAppointmentForm label.error').remove();
                                                $('#alertResetSuccess').hide();
                                                $('#alertResetFail').hide();

                                            }

                                            $(document).ready(function () {
                                                $('#username,#compname,#userphone,#strusermobile,#useremail,#preftime').val('');
                                                $('#BkgandAppointmentForm label.error').remove();

                                                var resultVal = '<%=stroutput%>';
                                                if (resultVal == 'mailSend') {
                                                    $('#alertResetSuccess').show();
                                                } else if (resultVal == 'mailNotSend') {
                                                    $('#alertResetFail').show();
                                                }
                                            });

                                            function citystatus(id)
                                            {

                                                if (document.getElementById("checkcitybtn").checked == true)
                                                {
                                                    document.getElementById("othercitystatus").value = 'Y';
                                                    document.getElementById("city").style.display = 'none';
                                                    document.getElementById("other_city").style.display = 'block';
                                                    $('.othercity').addClass('required');
                                                    $('.compcity').removeClass('required');
                                                    return false;
                                                }
                                                if (document.getElementById("checkcitybtn").checked == false)
                                                {
                                                    document.getElementById("othercitystatus").value = 'N';
                                                    document.getElementById("other_city").value = "";
                                                    document.getElementById("city").style.display = 'block';
                                                    document.getElementById("other_city").style.display = 'none';
                                                    $('.othercity').removeClass('required');
                                                    $('.compcity').addClass('required');
                                                    return false;
                                                }
                                            }
</script>

<%} catch (Exception e) {
          e.printStackTrace();
      }%>
