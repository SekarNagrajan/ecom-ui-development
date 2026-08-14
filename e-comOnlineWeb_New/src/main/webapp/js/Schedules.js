// JavaScript Document

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
var companyRequest = getXmlHttpRequestObject();
var searchReq = getXmlHttpRequestObject();
var searchRequest = getXmlHttpRequestObject();
var schedulemailtoken;

function getXmlHttpRequestObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert(incompatibleforAjax);
    }
}
//
function validateEmailToCommon(value) {
    var regex = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    return (regex.test(value)) ? true : false;
}

function validEmailCheckToCommon(idname) {
    var email = document.getElementById(idname).value;
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
    document.getElementById('email_text').value = result;
    return true;
}

function  EmailValidtion(field) {
    var emailId = trim(document.getElementById(field).value);
    if (document.getElementById('email_text').value.length == 0) {

        $('#mailerror').html(reqField);
        $('#mailerror').show();
        document.getElementById("email_text").focus();
        return false;
    } else if (emailId.length > 0) {
        if (!validEmailCheckToCommon(trim(field))) {

            $('#mailerror').html(invalidMailAlert);
            $('#mailerror').show();
            document.getElementById(field).focus();
            return false;
        }
    }
    return true;
}
function sendScheduleMail(clientcode)
{
    if ($('#schedulemailcaptcha').length > 0) {
        $.ajax({
            type: "POST",
            url: "UserProfileAjax?type=schedulemailcount",
            success: function (response) {
                if (response >= 3) {
                    var response1 = grecaptcha.getResponse(loginRecaptchaWidgetId);
                    if (response1.length > 0) {
                        sendScheduleMailAfterCaptcha(clientcode, response);
                    } else {
                        document.getElementById('g-recaptcha-error').innerHTML = '<span style="color:red;">I think you are a Robot.</span>';
                    }
                } else {
                    sendScheduleMailAfterCaptcha(clientcode, 0);
                }
            },
            error: function (xhr) {
                console.log('Error SchdByBlwLocationcalendar ');
                if (xhr.status == 404) {
                    console.log('Error 404: Resource not found');
                }
            }
        });
    } else {
        sendScheduleMailAfterCaptcha(clientcode, 0);
    }
}

function sendScheduleMailAfterCaptcha(clientcode, schcount) {
    if (EmailValidtion('email_text')) {
        $('#mailerror').hide();
        $('#mailbtn').html("<i class='fa fa-spinner fa-spin'></i> Sending...");
        var htm = "";
        if (document.getElementById("scheduletype").value == "SchdByPort")
        {
            htm = "datatablebyport1";
        } else if (document.getElementById("scheduletype").value == "SchdByVessel")
        {
            htm = "datatablebyvessel1";
        } else
        {
            htm = "datatablebylocation1";
        }
        var id = document.getElementById('email_text').value;
        htm = encodeURIComponent(htm);
        var capdate = {};
        if (schcount >= 3 && $('#schedulemailcaptcha').length > 0 ) {
            capdate = {
                'g-recaptcha-response': grecaptcha.getResponse(loginRecaptchaWidgetId)
            };
        }
        $.ajax({
            url: "ecomAjax?type=sendschedulemail&text=" + htm + "&id=" + id + "&clientcode=" + clientcode + "&date" + new Date() + "&scheduletoken=" + schedulemailtoken, // Backend endpoint
            type: 'POST',
            data: capdate,
            success: function (response) {
                $('#mailbtn').html('<i class="fa fa-envelope-o"></i>', sendMailBut);
                document.getElementById('email_text').value = "";
                $('#myModal').modal('hide');
                if (response.toUpperCase() == "INVALID") {
                    alert(mailNotDelivered);

                } else {
                    alert(scdlResSent);
                    return false;
                }
            },
            error: function () {
                alert(" Please try again.");
            }
        });
        /* if (searchReq.readyState == 4 || searchReq.readyState == 0)
         {
         
         var searchtype = "";
         var searchnumbers = "";
         if (document.getElementById("scheduletype").value == "SchdByPort")
         {
         searchtype = "By Port";
         var port = $('#hiddenport').val();
         var fdate = $('#hiddenfromdate').val();
         var tdate = $('#hiddentodate').val();
         searchnumbers = "At Port " + " : " + port + ".   From Date " + " - " + fdate + ".   To Date " + " - " + tdate + ".";
         } else if (document.getElementById("scheduletype").value == "SchdByVessel")
         {
         searchtype = "By Vessel";
         var vessel = $('#hdnvessel').val();
         var fdate = $('#hdnvesselfromdate').val();
         var tdate = $('#hdnvesseltodate').val();
         searchnumbers = "Vessel " + " : " + vessel + ".  From Date " + " - " + fdate + ".  To Date " + " - " + tdate + ".";
         } else
         {
         searchtype = "Between Location";
         var pol = $("#hdnpol").val();
         var pod = $("#hdnpod").val();
         var deptdate = $("#hdnfromdate").val();
         var arrdate = $("#hdntodate").val();
         searchnumbers = "Port of Load " + " : " + pol + ". Port of Discharge " + " - " + pod + ".  From Date  " + " - " + deptdate + ".  To Date  " + " - " + arrdate + ".";
         }
         
         var htm = "";
         if (document.getElementById("scheduletype").value == "SchdByPort")
         {
         htm = "datatablebyport1";
         } else if (document.getElementById("scheduletype").value == "SchdByVessel")
         {
         htm = "datatablebyvessel1";
         } else
         {
         htm = "datatablebylocation1";
         }
         var id = document.getElementById('email_text').value;
         htm = encodeURIComponent(htm);
         var urlStr = "ecomAjax?type=sendschedulemail&text = " + htm + "&id=" + id;
         searchRequest.open("POST", "ecomAjax", true);
         searchRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
         var params = "type=sendschedulemail&text=" + htm + "&id=" + id + "&clientcode=" + clientcode + "&date" + new Date() + "&scheduletoken=" + schedulemailtoken;
         searchRequest.onreadystatechange = sentMail;
         searchRequest.send(params);
         
         } */
    }
}
function sentMail() {
    if (searchRequest.readyState == 4) {
        $('#mailbtn').html('<i class="fa fa-envelope-o"></i>', sendMailBut);
        var response = searchRequest.responseText;
        document.getElementById('email_text').value = "";
        $('#myModal').modal('hide');
        if (response.toUpperCase() == "INVALID") {
            alert(mailNotDelivered);

        } else {
            alert(scdlResSent);
            return false;
        }

    }
}
var scheduleRecaptchaWidgetId;
function closeMail()
{
    document.getElementById('email_text').value = "";
}
function generatetoken() {
    $.ajax({
        url: "ecomAjax?type=gettoken",
        data: {},
        async: false,
        success: function (msg) {
            schedulemailtoken = msg;
        }
    });
    $('#mailbtn').html('<i class="fa fa-envelope-o"></i> Send Mail');
    if ($('#schedulemailcaptcha').length > 0) {
        $.ajax({
            type: "POST",
            url: "UserProfileAjax?type=schedulemailcount",
            success: function (response) {
                if (response >= 3) {
                    if (!scheduleCaptchaRendered) {
                        scheduleRecaptchaWidgetId = grecaptcha.render('schedulerecaptiid', {
                            'sitekey': $("#Captcha_Site_Key").val(), // your server-side variable
                            'callback': "verifyCaptcha",
                            'expired-callback': "recaptchaExpired"
                        });
                        scheduleCaptchaRendered = true; // Ensure it renders only once
                    } else {
                        grecaptcha.reset(scheduleRecaptchaWidgetId);
                    }
                }
            }
        });
    }
}