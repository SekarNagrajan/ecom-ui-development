
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<head>

    <script type="text/javascript">
        var isDesignMode = true;

        function ChangeMode()
        {
            if (isDesignMode == true)
            {
                isDesignMode = false;
                document.getElementById('mode').src = 'images/mode.html.gif';
                document.getElementById('mode').title = 'HTML View';
                Encoder.EncodeType = "entity";
                var encoded = Encoder.htmlEncode(getIFrameDocument("textEditor").body.innerHTML);
                getIFrameDocument("textEditor").body.innerHTML = encoded;
            } else
            {
                isDesignMode = true;
                document.getElementById('mode').src = 'images/mode.design.gif';
                document.getElementById('mode').title = 'Design View';
                var decoded = Encoder.htmlDecode(getIFrameDocument("textEditor").body.innerHTML);
                getIFrameDocument("textEditor").body.innerHTML = decoded;
            }
        }
        function setActivity()
        {
            var activity = "";
            $('#alertResetSuccess').hide();
            var e = document.getElementById("modulelist");
            $("#msgtxt").html("");
            var strUser = e.options[e.selectedIndex].value;

            if (strUser == "Default")
            {
                $("#moduleactivity").html("<option value='Default'>Select Activity</option>");
            } else if (strUser == "E-Booking")
            {
                activity = "<option value='Default'>Select Activity</option><option value='submitmail'>Submit to Agency(Customer)</option><option value='Update'>Submit to Agency(Agency)</option><option value='bkacc-cust'>Booking Accepted(Customer)</option>";
                activity = activity + "<option value='bkdec-cust'>Booking Declined(Customer)</option><option value='bamac-cust'>Amendment Booking Accepted(Customer)</option><option value='bamde-cust'>Amendment Booking Declined(Customer)</option><option value='BKAmClient'>Submit amendment to agency(Customer)</option><option value='BKAmAgen'>Submit amendment to agency(Agency)</option><option value='bkcnl-cust'>Booking cancellation (Customer)</option><option value='bkcnl-agen'>Booking cancellation (Agency)</option>";
                $("#moduleactivity").html(activity);
            } else if (strUser == "Registration")
            {
                $("#moduleactivity").html("<option value='Default'>Select Activity</option><option value='insert'>Verification</option><option value='Conmmail'>Accept</option><option value='Reject'>Reject</option><option value='Activate'>Activate</option><option value='Deactivate'>De-Activate</option>");
            } else if (strUser == "ForgotPasswordNew")
            {
                $("#moduleactivity").html("<option value='Default'>Select Activity</option><option value='Update'>Forgot password</option>");
            } else if (strUser == "PasswordReset")
            {
                $("#moduleactivity").html("<option value='Default'>Select Activity</option><option value='Update'>Reset password</option>");
            } else if (strUser == "ForgotUsernameNew")
            {
                $("#moduleactivity").html("<option value='Default'>Select Activity</option><option value='Update'>Forgot Username</option>");
            } else if (strUser == "SI")
            {
                $("#moduleactivity").html("<option value='Default'>--select--</option><option value='submitmail'>Submit to Agency(Customer)</option><option value='Update'>Submit to Agency(Agency)</option><option value='siacc-cust'>SI Accepted(Customer)</option><option value='sidec-cust'>SI Declined(Customer)</option>");
            } else if (strUser == "BL")
            {
                $("#moduleactivity").html("<option value='Default'>--select--</option><option value='submitmail'>Submit to Agency(Customer)</option><option value='Update'>Submit to Agency(Agency)</option><option value='blacc-cust'>BL Accepted(Customer)</option><option value='bldec-cust'>BL Declined(Customer)</option><option value='blcon-age'>BL Draft Accepted(Agency)</option><option value='blcon-cust'>BL Draft Accepted(Customer)</option>");
            } else if (strUser == "")
            {
                document.getElementById("defulatselect").style.display = "block";
                document.getElementById("ForgotPassword").style.display = "none";
                document.getElementById("PasswordReset").style.display = "none";
                document.getElementById("E-Booking").style.display = "none";
                document.getElementById("Registration").style.display = "none";
            } else
            {
                $("#moduleactivity").html("<option value='Default'>Select Activity</option>");
            }
        }
        function showemailSearch()
        {
            var serializedData = "";
            var e = document.getElementById("modulelist");
            var strmodules = e.options[e.selectedIndex].value;
            var c = document.getElementById("moduleactivity");
            var stractivity = c.options[c.selectedIndex].value;
            $.post("AdminAccess.do?type=emailformate&strmodules=" + strmodules + "&stractivity=" + stractivity, serializedData, function (data) {
                var maildata = data.split("~~~");
                nicEditors.findEditor("texteditor").setContent(maildata[1]);
//                var inputElement = document.getElementById("mailsubject");
//                inputElement.value = maildata[0];
                $("#mailsubject").val(maildata[0]);
            });
        }
        function setVendorDiv() {
            if (req.readyState == 4) {
                if (req.status == 200) {
                    var s = document.getElementById('textEditor');
                    s.contentDocument.write();
                    s.contentDocument.write(req.responseText);
                }
            }
        }

        function updatemail()
        {
            var nicE = new nicEditors.findEditor('texteditor');
            var content = nicE.getContent();
            var e = document.getElementById("modulelist");
            var strmodules = e.options[e.selectedIndex].value;
            var c = document.getElementById("moduleactivity");
            var stractivity = c.options[c.selectedIndex].value;
            var s = document.getElementById("mailsubject");
            var strsubject = s.value.trim();
            var params = "";
            var strupdate = "";
            if (strmodules == "Default")
            {
                alert("Select Module");
                return false;
            }
            if (stractivity == "Default")
            {
                alert("Select Module Activity");
                return false;
            }
            if (strsubject === "")
            {
                alert("Select Subject");
                return false;
            } else {
                $.ajax({

                    type: "POST",
                  url: "UserProfileAjax",
                    data: {
                        type: "emailupdateformate",
                        strupdate: content,
                        strmodules: strmodules,
                        stractivity: stractivity,
                        strsubject: strsubject
                    },  
                success: function (response) {

                        $('#alertResetSuccess').show();
                        document.getElementById('texteditor').innerHTML = "";
                        nicEditors.findEditor("texteditor").setContent('');  //set empty
                        $("#msgtxt").html("");
                        document.getElementById("moduleactivity").selectedIndex = 0; //Option 10
                        document.getElementById("modulelist").selectedIndex = 0;
//                        document.getElementById("mailsubject").value = '';
                        $("#mailsubject").val('');
                    },
                    error: function (xhr) {
                        if (xhr.status == 403) {
                            window.location.href = "SessionExpired.jsp";
                        }
                    }
                });
            }
        }


        function resetfields()
        {
            console.log("resetworked");
            document.getElementById('texteditor').innerHTML = "";
            nicEditors.findEditor("texteditor").setContent('');//set empty
            $("#msgtxt").html("");
            document.getElementById("moduleactivity").selectedIndex = 0; //Option 10
            document.getElementById("modulelist").selectedIndex = 0;
            $('#alertResetSuccess').hide();
        }
    </script>

    <% ArrayList vendormenuList = (ArrayList) session.getAttribute("vendormenuList");%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Email Template</div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="container">
            <div class="details_search">

                <div class="col-lg-12">
                    <div class="form-group alert-error" style="display:none;margin-bottom: 2%;margin-top: 1%" id='alertResetSuccess'>
                        <div class="alert alert-success alert-dismissible" role="alert">
                            <strong>Success ! </strong>Successfully updated.</div>
                    </div>
                </div>
                <div class="card-body-body">
                    <form method="post" id="arrivalform" class="form-horizontal">
                        <div class="form-group row">
                            <label class="col-sm-2 control-label">Module Name</label>
                            <div class="col-sm-3">
                                <select name="modulelist" id="modulelist" onChange="setActivity();" class="form-control light_border required">
                                    <option value="Default">Select Module</option>
                                    <% for (int i = 0; i < vendormenuList.size(); i++) {

                                            String strval = vendormenuList.get(i).toString();
                                            if ("E-Booking".equalsIgnoreCase(strval)) {
                                                strval = "E-booking";
                                            } else if ("PasswordReset".equalsIgnoreCase(strval)) {
                                                strval = "Password Reset";
                                            } else if ("ForgotPasswordNew".equalsIgnoreCase(strval)) {
                                                strval = "Forgot Password";
                                            } else if ("ForgotUsernameNew".equalsIgnoreCase(strval)) {
                                                strval = "Forgot Username";
                                            } else if ("SI".equalsIgnoreCase(strval)) {
                                                strval = "Shipping Instruction";
                                            } else if ("BL".equalsIgnoreCase(strval)) {
                                                strval = "Bill of Lading";
                                            }
                                            if (!"contactus".equalsIgnoreCase(strval) && !"schedule".equalsIgnoreCase(strval) && !"tracking".equalsIgnoreCase(strval) && !"Forgot Username".equalsIgnoreCase(strval) && !"RateRequest".equalsIgnoreCase(strval)) {
                                    %>

                                    <option value="<%=vendormenuList.get(i)%>"><%=strval%></option>
                                    <%}
                                        }%>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 control-label">Activity</label>
                            <div class="col-sm-3" id="defulatselect">
                                <select name="moduleactivity" id="moduleactivity" onChange="showemailSearch();" class="form-control light_border required">
                                    <option value="Default">Select Activity</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 control-label">Subject <span style="color: red;">*</span></label>
                            <div class="col-sm-3" id="defulatselect1">
                                <input id="mailsubject" class="form-control light_border required">

                            </div>
                        </div>
                        <div class="form-group row">
                            <div style="font-family:Verdana, Arial, Helvetica, sans-serif;font-size:12px;color:#FF0000;padding-left:300px;width:300px;display:none" id="msgtxt"></div>
                            <div class="col-sm-6">
                                <textarea name="area3" style="width: 650px; height: 280px;overflow:auto;" class="light_border" id="texteditor">
                                </textarea>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-5">
                                <button type="button" class="btn btn-primary btn-lg search_button"  id="searchID" onClick="updatemail();"> <span class="glyphicon glyphicon-search" ></span> Save</button>
                                <button type="reset" class="btn btn-danger btn-lg search_button"  id="searchID1"  onClick="resetfields();"> <span class="glyphicon glyphicon-refresh" ></span> Reset</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        // <![CDATA[
        bkLib.onDomLoaded(function () {
            //  new nicEditor({fullPanel: true, iconsPath: 'Design/assets/plugins/NicEdit/nicEditorIcons.gif'}).panelInstance('texteditor');
            nicEditors.allTextAreas();
        });
    </script>
    <script>
        function showFontColor()
        {
            var div = document.getElementById('showFore');
            if (div.style.visibility == 'visible') {
                div.style.visibility = 'hidden'
            } else if (div.style.visibility == 'hidden') {
                div.style.visibility = 'visible';
            }
        }
        function showbackColor()
        {
            var div = document.getElementById('showback');
            if (div.style.visibility == 'visible') {
                div.style.visibility = 'hidden'
            } else if (div.style.visibility == 'hidden') {
                div.style.visibility = 'visible';
            }
        }
    </script>

