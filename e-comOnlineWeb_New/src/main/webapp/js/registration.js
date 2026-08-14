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

var searchReq1 = getXmlHttpRequestObject();
var searchReq2 = getXmlHttpRequestObject();

function getXmlHttpRequestObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert(incompatibleforAjax);
    }
}
function setHiddenValue(tag, src) {
    document.getElementById('hdnagency').value = "";
    document.getElementById(tag).value = document.getElementById(src).value;
    document.getElementById('hdnagency').value = document.getElementById(src).value;
}

function setCityDesc(id) {
    document.getElementById('city_desc').value = "";
    document.getElementById('hdncity').value = "";
    var city = document.getElementById(id).value;
    document.getElementById('city_desc').value = city;
    document.getElementById('hdncity').value = city;
}
function getlocationlist(countrycode, time) {
    document.getElementById('location').options[0] = new Option("Loading.... ", "");
    //document.getElementById('company_city').options[0] = new Option("Loading.... ", "");
    setHiddenValue('countrycode', 'country');
    if (searchReq.readyState == 4 || searchReq.readyState == 0)
    {
        var url = "UserProfileAjax?type=location&countrycode=" + countrycode + "&ldate=" + time;
        searchReq.open("GET", url, true);
        searchReq.onreadystatechange = setLocation;
        searchReq.send(null);
    }

}

/**
 * Same logic as #country onchange on Registration.jsp.
 * Used by manual country change and Google address search auto-fill.
 */
function applyRegistrationCountryChange(countryCode) {
    var countryField = document.getElementById('country');
    if (countryField && countryCode) {
        countryField.value = countryCode;
    }
    var code = countryCode || (countryField ? countryField.value : '');
    if (!code) {
        return;
    }

    getlocationlist(code, new Date().getTime());
    setHiddenValue('countrycode', 'country');

    if (typeof setPostalCodeValidation === 'function') {
        setPostalCodeValidation();
    }
    if (typeof showFields === 'function') {
        showFields();
    }
    if (typeof setCountryDialCode === 'function') {
        setCountryDialCode(code);
    }
    if (typeof jQuery !== 'undefined') {
        jQuery('#country').trigger('change');
    }
}
function setLocation() {
    if (searchReq.readyState == 4) {
        if (searchReq.status == 200) {
            var response = searchReq.responseText;
            var ports = response.split("~~~");
            if (ports[0] == "valid") {
                var portlist = ports[1].split("##");
                if (portlist[0] == "noports") {
                    document.getElementById('location').options.length = 1;
                    document.getElementById('location').options[0] = new Option("No Location", "");
                } else {
                    var loca = ports[1].split("~#~");
                    document.getElementById('location').options.length = loca.length - 1;
                    document.getElementById('location').options[0] = new Option("Select Nearest Agency Location", "");
                    var j = 1;
                    for (var i = 0; i < loca.length - 1; i++) {
                        portname = loca[i].split("##");
                        document.getElementById('location').options[j] = new Option(trim(portname[0].split("~~")[0]), portname[1]);
                        j++;
                    }
                    $('#location').val(reg_locationcode);
                }

            } else if (ports[0] == "invalid") {

                alert(excep);
            }
        }
    }
    //getCitylist(new Date().getTime());
}
function getCitylist(time) {
    setHiddenValue('countrycode', 'country');
    var countrycode = document.getElementById('country').value;
    document.getElementById('company_city').options[0] = new Option("Loading.... ", "");
    if (searchReq.readyState == 4 || searchReq.readyState == 0)
    {
        var url = "UserProfileAjax?type=city&countrycode=" + countrycode + "&ldate=" + time;
        searchReq.open("GET", url, true);
        searchReq.onreadystatechange = setCityList;
        searchReq.send(null);
    }
}
function setCityList() {
    var othercity = document.getElementById('othercity').value;
    if (searchReq.readyState == 4) {
        if (searchReq.status == 200) {
            var response = searchReq.responseText;
            document.getElementById('company_city').options[0] = new Option("Loading.... ", "");
            var ports = response.split("~~");
            if (ports[0] == "valid") {
                var portlist = ports[1].split("##");
                if (portlist[0] == "nocity") {
                    document.getElementById('company_city').options.length = 1;
                    document.getElementById('company_city').options[0] = new Option("No City", "");

                } else {
                    if (jQuery.trim(othercity) == "Y") {
                        document.getElementById('company_city').options.length = ports.length;
                        document.getElementById('company_city').options[0] = new Option("Select City", "");
                        for (var i = 1; i < ports.length; i++) {
                            portname = ports[i].split("##");
                            if (i == ports.length - 1) {
                                document.getElementById('company_city').options[i] = new Option("Others", "Others");
                            } else {
                                document.getElementById('company_city').options[i] = new Option(portname[0], portname[1]);
                            }
                        }
                    } else {
                        document.getElementById('company_city').options.length = ports.length - 1;
                        document.getElementById('company_city').options[0] = new Option("Select City", "");
                        for (var i = 1; i < ports.length - 1; i++) {
                            portname = ports[i].split("##");
                            document.getElementById('company_city').options[i] = new Option(portname[0], portname[1]);
                        }
                    }
                }
            } else if (ports[0] == "invalid") {
                alert(excep);
            }
        }
    }
}
function checkpassword() {
    var password = document.getElementById('password').value;
    var cpassword = document.getElementById('cpassword').value;
    if (trim(password) == "" && trim(cpassword) != "") {
        document.getElementById('cpassword').value = "";
        setTimeout(function () {
            document.getElementById('password').focus();
        }, 10);
        return false;
    }
    if (password != "" && cpassword != "") {
        if (trim(password) != trim(cpassword)) {
            document.getElementById('cpassword').value = "";
            setTimeout(function () {
                document.getElementById('cpassword').focus();
            }, 10);
            return false;
        }
    }
}
function checkpasswordUserCreation() {
    var password = document.getElementById('userpassword').value;
    var cpassword = document.getElementById('usercpassword').value;
    if (trim(password) == "" && trim(cpassword) != "") {
        document.getElementById('usercpassword').value = "";
        setTimeout(function () {
            document.getElementById('userpassword').focus();
        }, 10);
        return false;
    }
    if (password != "" && cpassword != "") {
        if (trim(password) != trim(cpassword)) {
            document.getElementById('usercpassword').value = "";
            setTimeout(function () {
                document.getElementById('usercpassword').focus();
            }, 10);
            return false;
        }
    }
}

function setcompany(field)
{
    var company = trim(document.getElementById('companyname').value);
    document.getElementById('hiddencustname').value = company;
}

var popUpWin = 0;
function popUpWindowReg(URLStr, left, top, width, height)
{
    if (popUpWin) {
        if (!popUpWin.closed)
            popUpWin.close();
    }
    var styleStr = 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=' + width + ',height=' + height + ',left=' + left + ', top=' + top + ',screenX=' + left + ',screenY=' + top;
    popUpWinTerms = open(URLStr, 'popUpWinTerms', styleStr);
}

function getCitylist1(time) {
    var countrycode = document.getElementById('country').value;
    if (countrycode != "")
    {
        document.getElementById('company_city').options[0] = new Option("Loading.... ", "");
        if (searchReq1.readyState == 4 || searchReq1.readyState == 0)
        {
            var url = "UserProfileAjax?type=city&countrycode=" + countrycode + "&ldate=" + time;
            searchReq1.open("GET", url, true);
            searchReq1.send(null);
            searchReq1.onreadystatechange = function ()
            {
                var response = searchReq1.responseText;
                document.getElementById('company_city').options[0] = new Option("Loading.... ", "");
                var ports = response.split("~~");
                if (ports[0] == "valid") {
                    var portlist = ports[1].split("##");
                    if (portlist[0] == "nocity") {
                        document.getElementById('company_city').options.length = 1;
                        document.getElementById('company_city').options[0] = new Option("No City", "");
                    } else {
                        if (jQuery.trim(othercity) == "Y") {
                            document.getElementById('company_city').options.length = ports.length;
                            document.getElementById('company_city').options[0] = new Option("Select City", "");
                            for (var i = 1; i < ports.length; i++) {
                                portname = ports[i].split("##");
                                if (i == ports.length - 1) {
                                    document.getElementById('company_city').options[i] = new Option("Others", "Others");
                                } else {
                                    document.getElementById('company_city').options[i] = new Option(portname[0], portname[1]);
                                }
                            }
                        } else {
                            document.getElementById('company_city').options.length = ports.length - 1;
                            document.getElementById('company_city').options[0] = new Option("Select City", "");
                            for (var i = 1; i < ports.length - 1; i++) {
                                portname = ports[i].split("##");
                                document.getElementById('company_city').options[i] = new Option(portname[0], portname[1]);

                            }
                        }
                    }

                } else if (ports[0] == "invalid") {

                    alert(excep);
                }
            }
        }
    }
}
function excustidcheck(id)
{
    $('#customerno').val("");
    if (document.getElementById("checkbutton").checked == true)
    {
        $('#customerno').attr("readonly", false);
        $('#customerno').addClass("required");
        $('#companyname,#country,#company_address').attr("disabled", true);
    } else if (document.getElementById("checkbutton").checked == false)
    {
        $('#customerno').removeClass("required");
        $('#customerno').attr("readonly", true);
        $('#companyname,#country,#company_address').attr("disabled", false);
        //
        document.getElementById("country").selectedIndex = 0;
        document.getElementById("location").selectedIndex = 0;
        $('#hiddencustname').val("");
        $('#companyId,#country,#countrycode,#othercity,#agencycode,#company_address,#company_address1,#other_city').val("");
        $('#company_postcode,#hiddencity,#hiddenstate,#company_country_code,#company_phone_code,#company_phone_no,#companyname').val("");
        if ($("#control_location_Non_Mandatory").length > 0 && $("#control_location_Non_Mandatory").val() == 'Y') {
            if (!$("#location").is('.required')) {
                $("#location").addClass('required');
                $("#locationmandatorylbl").html("*");
            }
        }
    }
}


function setHiddenValueTime(tag, src) {
    document.getElementById('locationt').value = "";
    document.getElementById(tag).value = document.getElementById(src).value;
    console.log("tag-===========>" + document.getElementById(tag).value);
    document.getElementById('locationt').value = document.getElementById(src).value;
    console.log("loct-------->" + document.getElementById('locationt').value);

}
var reg_locationcode = '';
function loadCustomerDetails(custcode) {
    if (custcode == "")
    {
//        alert("Enter Existing System Code");
//        $('#hiddencustname').val('');
//        return false;
        document.getElementById("country").selectedIndex = 0;
        document.getElementById("location").selectedIndex = 0;
        $('#hiddencustname').val("");
        $('#companyId,#country,#countrycode,#othercity,#agencycode,#company_address,#company_address1,#other_city').val("");
        $('#company_postcode,#hiddencity,#hiddenstate,#company_country_code,#company_phone_code,#company_phone_no,#companyname').val("");
        if ($("#control_location_Non_Mandatory").length > 0 && $("#control_location_Non_Mandatory").val() == 'Y') {
            if (!$("#location").is('.required')) {
                $("#location").addClass('required');
                $("#locationmandatorylbl").html("*");
            }
        }
    }

    if (custcode.length > 0) {
        if (custcode == "-")
        {
            alert("Invalid System Code");
            return false;
        } else
        {
            $.ajax({
                type: "POST",
                cache: false,
                url: "RegistrationAjax?type=getcompanydetails&custcode=" + custcode,
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    var details = data.split("~~");
                    console.log(details);
                    if (trim(details[17]) == "VALID") {
                        $('#country').attr("readOnly", true);
                        $('#country,#companyname,#company_address').attr("disabled", true);
                        document.getElementById('companyId').value = details[1];
                        document.getElementById('country').value = details[3];
                        document.getElementById('countrycode').value = details[3];
                        $('#country').trigger('blur').trigger('change');
                        reg_locationcode = details[5];
                        getlocationlist(details[3], new Date().getTime());
                        document.getElementById('agencycode').value = details[5];
                        document.getElementById('company_address').value = details[6];
                        document.getElementById('company_address1').value = details[7];
                        document.getElementById('company_postcode').value = details[8];
                        document.getElementById('other_city').value = details[9];
                        document.getElementById('hiddenstate').value = details[11];
                        document.getElementById('company_country_code').value = details[12];
                        document.getElementById('company_phone_code').value = details[13];
                        document.getElementById('company_phone_no').value = details[14];
                        document.getElementById('othercity').value = details[15];
                        document.getElementById('companyname').value = details[16];
                        document.getElementById('hiddencustname').value = details[16];
                        document.getElementById('company_domain').value = details[18];
                        $('#location').val(details[5]);
                        document.getElementById('savetype').value = 'edit';
                        if ($("#control_location_Non_Mandatory").length > 0 && $("#control_location_Non_Mandatory").val() == 'Y') {
                            if ($("#location").is('.required')) {
                                $("#location").removeClass('required');
                                $("#locationmandatorylbl").html("");
                            }
                        }
                        return false;
                    } else {
                        alert("Invalid System Code");
                        $('#customerno').val("");
                        $('#companyname,#country,#company_address').attr("disabled", true);
                        //$('#country,#location,#company_city,#company_state').attr("disabled", true);
                        //$('#companyname,#company_address,#company_address1,#company_postcode').attr("readOnly", true);
                        //$('#company_country_code,#company_phone_code,#company_phone_no,#company_domain,#comp_email').attr("readOnly", true);
                        document.getElementById("country").selectedIndex = 0;
                        document.getElementById("location").selectedIndex = 0;
                        $('#hiddencustname').val("");
                        $('#companyId,#country,#countrycode,#othercity,#agencycode,#company_address,#company_address1,#other_city').val("");
                        $('#company_postcode,#hiddencity,#hiddenstate,#company_country_code,#company_phone_code,#company_phone_no,#companyname').val("");
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
    }
}

document.addEventListener('DOMContentLoaded', showFields);
//function handleCustomerTypeChange() {
//    showFields();
//}

function showFields() {
    var countrycode = document.getElementById('country').value;
    console.log("country---" + countrycode);
    if (searchReq.readyState == 4 || searchReq.readyState == 0) {
        var url = "RegistrationAjax?type=getcountryfileupload&countrycode=" + countrycode;
        searchReq.open("GET", url, true);
        searchReq.onreadystatechange = showOrHideFields;
        searchReq.send(null);
    }
}

function showOrHideFields() {
    if (searchReq.readyState == 4) {
        if (searchReq.status == 200) {
            var response = searchReq.responseText;
            try {
                var fieldDataArray = JSON.parse(response);
                var container = $('#fieldContainer');
                container.empty();

                if (fieldDataArray.length === 0) {
                    var defaultFieldDiv = $('<div>', {
                        class: 'inner txt_upload',
                        id: 'defaultField'
                    });
                    var customerType = document.querySelector('input[name="customerType"]:checked').value;
                    var showMandatory = ((customerType === "newcustomer") ? "required" : "");
                    var defaultInnerContent = `
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text-fileupload">Others ${((showMandatory == 'required') ? '<span class="mandatory">  *</span>' : '')}</span>
                            </div>
                            <div class="custom-file">
                                <input type="file" id="defaultFileField" name="defaultFileField" class="custom-file-input regFiles ${showMandatory}" style="width: 250px;" onchange="ValidateSingleInputForREG(this);">
                                <label class="custom-file-label" for="defaultField">Choose file</label>
                            </div>
                        </div><div>&nbsp;&nbsp;</div>
                    `;

                    defaultFieldDiv.append(defaultInnerContent);
                    container.append(defaultFieldDiv);
                } else {
                    for (var i = 0; i < fieldDataArray.length; i++) {
                        var fieldData = fieldDataArray[i];
                        var fieldName = fieldData.fieldname;
                        var isMandatory = fieldData.mandatory;

                        var customerType = document.querySelector('input[name="customerType"]:checked').value;
                        var showMandatory = customerType === "newcustomer" && isMandatory === 'Y';

                        var fieldDiv = $('<div>', {
                            class: 'inner txt_upload',
                            id: fieldName
                        });
                        var fieldid = fieldName;
                        fieldid = fieldid.replace(" ", "_");
                        var innerContent = `
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload">${fieldName}${showMandatory ? '<span class="mandatory">  *</span>' : ''}</span>
                                </div>
                                <div class="custom-file">
                                    <input type="file" name="${fieldid + i}" id="${fieldid + i}" class="custom-file-input regFiles ${showMandatory ? 'required' : ''}" style="width: 250px;" onchange="ValidateSingleInputForREG(this);">
                                    <label class="custom-file-label" for="${fieldName}">Choose file</label>
                                </div>
                            </div><div>&nbsp;&nbsp;</div>
                        `;

                        fieldDiv.append(innerContent);
                        container.append(fieldDiv);
                    }
                }
            } catch (error) {
                console.log("Error parsing JSON response: " + error);
            }

            var fileInputs = container.find('input[type="file"]');
            fileInputs.each(function () {
                $(this).on('change', function () {
                    var fileName = $(this).val().split("\\").pop();
                    $(this).next('.custom-file-label').html(fileName);
                });
            });
        } else {
            console.log("Response status is not 200");
        }
    }
}
function sendfiletoserver() {
    var formData = new FormData();
    var isfilethere = false;
    $('.regFiles').each(function () {
        var files = $(this)[0].files;
        console.log("files==> " + files);
        for (var i = 0; i < files.length; i++) {
            formData.append($(this).attr('name'), files[i]);
            isfilethere = true;
        }

    });
    if (isfilethere) {
        $.ajax({
            url: 'RegistrationAjax?type=uploadfileinregistration',
            type: 'POST',
            data: formData,
            contentType: false,
            cache: false,
            processData: false,
            success: function (response) {
                console.log("response=> " + response);
            }, error: function (xhr, status, error) {
                console.log("error=> " + error);
            }
        });
    }
}