<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.ecom.common.GeneralConf"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.ArrayList,java.util.Hashtable,net.ecom.vo.CustomerProfile"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<%
    CustomerProfile profileVO = (CustomerProfile) session.getAttribute("userDetails");
    String strusername = "";
    String strcompname = "";
    String straddress = "";
    String straddress1 = "";
    String strcity = "";
    String strcountry = "";
    String strpostal = "";
    String strcompphone = "";
    String struserphone = "";
    String strusermobile = "";
    String struserfax = "";
    String stremail = "";
    String strdef = "ENGLISH";
    String strpref = "", ccMailId = "", timeZone = "";

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

    if (profileVO.getAddr1() != null && profileVO.getAddr1().length() > 0) {
        straddress = profileVO.getAddr1();
    } else {
        straddress = "-";
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
    if (profileVO.getPobox() != null && profileVO.getPobox().length() > 0) {
        strpostal = profileVO.getPobox().toString();
    } else {
        strpostal = "-";
    }

    if (profileVO.getTeleNo() != null && profileVO.getTeleNo().length() > 0) {
        strcompphone = profileVO.getTeleNo();
    } else {
        strcompphone = "-";
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

    if (profileVO.getFax() != null && profileVO.getFax().length() > 0) {
        struserfax = profileVO.getFax();
    } else {
        struserfax = "-";
    }

    if (profileVO.getEmailId() != null && profileVO.getEmailId().length() > 0) {
        stremail = profileVO.getEmailId();
    } else {
        stremail = "-";
    }
    if (profileVO.getAddr2() != null && profileVO.getAddr2().length() > 0) {
        straddress1 = profileVO.getAddr2();
    } else {
        straddress1 = "-";
    }
    ccMailId = profileVO.getCcMailId();
    timeZone = profileVO.getTimezone();
    String clientcode = (String) session.getAttribute("clientcode");
    strpref = (String) session.getAttribute("SESSION_LANGUAGE");
    Hashtable countrylist = (Hashtable) getServletContext().getAttribute("SESSION_CONTRYLIST");
    String country = "";
    country = ((String) countrylist.get(strcountry) != null) ? (String) countrylist.get(strcountry) : strcountry;

    String user = (String) session.getAttribute("sessionAdmin");
    String toenableTimeZone = (String) getServletContext().getAttribute(GeneralConf.EnableTimeZone);


%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/view-profile.png" />  <bean:message key="ViewProfile.Title" bundle="<%=clientcode%>"/>  </div>
<div class="container-fluid page_bottom_content">
    <div class="card card-body card-shadow">
        <div class="form-group">
            <div class="row">
                <div class="col-sm-12 col-md-6 col-xl-6 txt_data">
                    <div class="">

                        <div class="heading_trainee_bottom ">  </div>

                        <div class="heading_trainee "> <bean:message key="ViewProfile.Title.Compinfo" bundle="<%=clientcode%>"/> </div>

                        <div class=" box_line">

                            <div class="row form-group">

                                <label class="col-sm-3 control-label"><bean:message key="ViewProfile.compinfo.CompanyName" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strcompname%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-3 control-label"> <bean:message key="ViewProfile.compinfo.Address" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=straddress%> </label>

                                </div>
                            </div>

                            <div class="row form-group">

                                <label class="col-sm-3 control-label"> <bean:message key="ViewProfile.compinfo.Address1" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"><%=straddress1%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-3 control-label"> <bean:message key="ViewProfile.compinfo.City" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strcity%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-3 control-label"> <bean:message key="ViewProfile.compinfo.Country" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=country%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-3 control-label"> <bean:message key="ViewProfile.compinfo.PostalCode" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strpostal%> </label>

                                </div>
                            </div> 

                            <div class="row form-group">

                                <label class="col-sm-3 control-label"> <bean:message key="ViewProfile.compinfo.CompanyPhone" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strcompphone%> </label>

                                </div>
                            </div> 



                        </div> <!-- card-body  close -->


                    </div>

                </div>
                <div class="col-sm-12 col-md-6 col-xl-6 txt_data">

                    <div class="">
                        <div class="heading_trainee_bottom ">  </div>

                        <div class="heading_trainee ">  <bean:message key="ViewProfile.Title.userifo" bundle="<%=clientcode%>"/> </div>


                        <div class="box_line"> 



                            <div class="row form-group">

                                <label class="col-sm-4 control-label"> <bean:message key="ViewProfile.userifo.UserName" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strusername%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-4 control-label"> <bean:message key="ViewProfile.userifo.Phone" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=struserphone%> </label>

                                </div>
                            </div>

                            <div class="row form-group">

                                <label class="col-sm-4 control-label"> <bean:message key="ViewProfile.userifo.Mobile" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strusermobile%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-4 control-label"> <bean:message key="ViewProfile.userifo.Fax" bundle="<%=clientcode%>" /> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=struserfax%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-4 control-label"> <bean:message key="ViewProfile.userifo.Email" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=stremail%> </label>

                                </div>
                            </div> 


                            <div class="row form-group">

                                <label class="col-sm-4 control-label"> <bean:message key="ViewProfile.userifo.DefaultLanguage" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strdef%> </label>

                                </div>
                            </div> 

                            <div class="row form-group">

                                <label class="col-sm-4 control-label"> <bean:message key="ViewProfile.userifo.PreferredLanguage" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-8 "> 

                                    <label class="labl_pro"> <%=strpref%> </label>

                                </div>
                            </div>


                            <div class="row form-group">

                                <label class="col-sm-4 control-label"><bean:message key="ViewProfile.ccmailid" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-6 "> 

                                    <label class="labl_pro" id="ccmailidlabel" style="word-break: break-word;"> <%=ccMailId%> </label>

                                </div>

                                <label class="col-sm-2  ">
                                    <a  onclick="showprofilemodal();" id="updateccmailid" href="javascript:void(0)" class='btn btn-primary <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%>' style="padding: 2px;font-size: 12px;width: 67px;" >
                                        <i class="fa fa-pencil-square" id="ccupdateimg"></i> <bean:message key="ecom.Update" bundle="<%=clientcode%>"/> </a></b>
                                </label>

                            </div>
                            <!--                                timezone-->
                            <%if ("Y".equalsIgnoreCase(toenableTimeZone)) {%>

                            <div class="row form-group">

                                <label class="col-sm-4 control-label"><bean:message key="ecom.reg.timezone" bundle="<%=clientcode%>"/> : </label>

                                <div class="col-sm-6 "> 

                                    <label class="labl_pro" id="timezoneidlabel" style="word-break: break-word;"> <%=timeZone%> </label>

                                </div>

                                <label class="col-sm-2  ">
                                    <a  onclick="showtimezonemodal();" id="updatetimezoneid" href="javascript:void(0)" class='btn btn-primary <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%>' style="padding: 2px;font-size: 12px;width: 67px;" >
                                        <i class="fa fa-pencil-square" id="ccupdateimg"></i> <bean:message key="ecom.Update" bundle="<%=clientcode%>"/> </a></b>
                                </label>

                            </div>
                            <%}%>
                            <!--timezone-->

                        </div>   <!-- card-body close -->


                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal for Mail Notification -->
<div class="modal fade"  id="ccupdatemodal" role="dialog" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title"><bean:message key='ebooking.msg.sendccmail' bundle="<%=clientcode%>"/> </h5>
                <button type="button" class="close dialouge_close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">

                <div class="row form-group">
                    <div class="col-sm-8">
                        <label class=""> <bean:message key='ebooking.msg.ccmailid' bundle="<%=clientcode%>"/> :   <span class="label label-info"><bean:message key='ebooking.msg.commasepmutiplemail' bundle="<%=clientcode%>"/></span> </label>
                    </div>
                </div>  

                <div class="row form-group">
                    <div class="col-sm-12">
                        <textarea class="form-control" id="profileccmail" name="profileccmail" rows="3"  maxlength="200" cols="50" style="resize:none"></textarea>
                    </div>
                </div> 
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-success" onclick="saveCCMailId();" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn"><bean:message key='ecom.Save' bundle="<%=clientcode%>"/></button>
                <button type="button" class="btn btn-danger" onclick="deleteCCMailId();" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn"><bean:message key='ecom.Delete' bundle="<%=clientcode%>"/></button>
            </div>

        </div>
    </div>
</div>
<!--            timezone model-->
<div class="modal fade"  id="timezoneupdatemodal" role="dialog" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title"><bean:message key='ebooking.msg.timezoneupdationheading' bundle="<%=clientcode%>"/> </h5>
                <button type="button" class="close dialouge_close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">

                <div class="row form-group">
                    <div class="col-sm-8">
                        <label class=""> <bean:message key='ecom.reg.timezone' bundle="<%=clientcode%>"/> :  </label>
                    </div>
                </div>  

                <div class="row form-group">
                    <div class="col-sm-12">
                        <select name="timezone" id="timezone" class="form-control light_border required">
                            <option value="">Select TimeZone</option>  
                            <%
                                HashMap langlist = (HashMap) getServletContext().getAttribute(GeneralConf.timeZoneSelection);
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
                </div> 
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-success" onclick="saveTimeZone();" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn"><bean:message key='ecom.Save' bundle="<%=clientcode%>"/></button>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript">
    function trim(value) {
        return LTrim(RTrim(value));
    }
    function LTrim(value) {
        var re = /\s*((\S+\s*)*)/;
        return value.replace(re, "$1");
    }
    // Removes ending whitespaces
    function RTrim(value) {
        var re = /((\s*\S+)*)\s*/;
        return value.replace(re, "$1");
    }
    var userEmail = "<%=stremail%>";
    var existccmail = "<%=ccMailId.replaceAll("\\r\\n", "")%>";
    var existTimeZone = "<%=timeZone%>";
    function showprofilemodal() {
        $('#profileccmail').val(existccmail);
        $('#ccupdatemodal').modal("show");
    }
    function showtimezonemodal() {
        $('#timezone').val(existTimeZone);
        $('#timezoneupdatemodal').modal("show");
    }
    function saveCCMailId() {
        var checksubmit = true;
        if ($('#profileccmail').val() != '') {
            if (!validEmailCheckToCommon(trim($('#profileccmail').val()))) {
                alert("<bean:message key="vgm.msg.invalidmail" bundle="<%=clientcode%>"/>");
                checksubmit = false;
            }
        } else {
            alert('<bean:message key="ecom.msg.ccmailid" bundle="<%=clientcode%>"/>');
            checksubmit = false;
        }
        //alert(cclist);
        if (checksubmit) {
            splitCCMailId($('#profileccmail').val());
            openLoader();
            $.ajax({
                type: "POST",
                cache: false,
                async: false,
                url: "RegistrationAjax?type=updateccmail&ccmailid=" + encodeURIComponent($('#profileccmail').val()) + "&loginid=" + encodeURIComponent(userEmail),
                data: $(this).serializeArray(),
                success: function (data) {
                    $('#ccupdatemodal').modal("hide");
                    closeLoader();
                    $('#ccmailidlabel').html($('#profileccmail').val());
                    existccmail = $('#profileccmail').val();
                    //alert("Updated successfully.");
                    toastr.success("<bean:message key="ecom.viewprof.mailupdate" bundle="<%=clientcode%>"/>", "SUCCESS");
                }, error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }
    }
    function deleteCCMailId() {
        var con = confirm('<bean:message key="ecom.msg.delbooking" bundle="<%=clientcode%>"/>');
        if (con == true) {
            openLoader();
            $.ajax({
                type: "POST",
                cache: false,
                async: false,
                url: "RegistrationAjax?type=updateccmail&ccmailid=&loginid=" + encodeURIComponent(userEmail),
                data: $(this).serializeArray(),
                success: function (data) {
                    $('#ccupdatemodal').modal("hide");
                    closeLoader();
                    $('#profileccmail').val('');
                    $('#ccmailidlabel').html($('#profileccmail').val());
                    existccmail = $('#profileccmail').val();
                    toastr.success(" <bean:message key="ecom.viewprof.delmail" bundle="<%=clientcode%>"/>", "SUCCESS");
                    //alert("Deleted successfully.");
                }, error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }
    }
    function validateEmailToCommon(value) {
        var regex = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        return (regex.test(value)) ? true : false;
    }

    function validEmailCheckToCommon(val) {
        var email = val;
        email = email.replace(",,", ",");
        email = email.replace(";,", ",");
        email = email.replace(";,", ",");
        email = email.replace(",;", ",");
        email = email.replace(/;/gi, ",");
        var result = email.replace(/\s/g, "").split(/,|;/);
        for (var i = 0; i < result.length; i++) {
            if (!validateEmailToCommon(result[i])) {
                return false;
            }
        }
        return true;
    }
    function splitCCMailId(val) {
        var email = val;
        email = email.replace(",,", ",");
        email = email.replace(";,", ",");
        email = email.replace(";,", ",");
        email = email.replace(",;", ",");
        email = email.replace(/;/gi, ",");
        var result = email.replace(/\s/g, "").split(/,|;/);
        for (var i = 0; i < result.length; i++) {
            if (i == 0) {
                email = result[i];
            } else {
                email = email + "," + result[i];
            }
        }
        $('#profileccmail').val(email);
    }


    function saveTimeZone() {
        var checktimezone = true;
        var e = document.getElementById("timezone").value;
        if (e === "") {
            alert('<bean:message key="ecom.msg.timezone" bundle="<%=clientcode%>"/>');
            checktimezone = false;
        }

        if (checktimezone) {

            openLoader();
            $.ajax({
                type: "POST",
                cache: false,
                async: false,
                url: "RegistrationAjax?type=updatetimezone&timezone=" + encodeURIComponent($('#timezone').val()) + "&loginid=" + encodeURIComponent(userEmail),
                data: $(this).serializeArray(),
                success: function (data) {
                    $('#timezoneupdatemodal').modal("hide");
                    closeLoader();
                    $('#timezoneidlabel').html($('#timezone').val());
                    existTimeZone = $('#timezone').val();
                    //alert("Updated successfully.");
                    toastr.success("<bean:message key="ecom.viewprof.mailupdate" bundle="<%=clientcode%>"/>", "SUCCESS");
                }, error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }
    }
</script>
