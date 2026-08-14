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

function callsearchmethod(formName, searchType, con,custid) {
    if (con == 'agree') {
        $('#agreesearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'shipper') {
        $('#shippersearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'consignee') {
        $('#consigneesearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'notify') {
        $('#notifysearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'notify1') {
        $('#notifysearch1').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'forwarder') {
        $('#forwardersearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'Warehouse') {
        $('#Warehousesearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'NamedCustomer') {
        $('#NamedCustomersearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'Seller') {
        $('#Sellersearch').html("<i class='fa fa-spinner fa-spin'></i>");
    } else if (con == 'Buyer') {
        $('#Buyersearch').html("<i class='fa fa-spinner fa-spin'></i>");
    }else if (con == 'dyBuyer') {
        $('#'+con+'custsearch'+custid).html("<i class='fa fa-spinner fa-spin'></i>");
    }else if (con == 'dySeller') {
        $('#'+con+'custsearch'+custid).html("<i class='fa fa-spinner fa-spin'></i>");
    }
    var typeVal = "";
    var params = "";
    var dycustis = (custid && custid.length) ? custid : '';
     console.log(dycustis+'custid'+custid);
    var remainingParameter = "";
    typeVal = searchType;
    remainingParameter = "&fieldName=" + con;
    params = "type=" + typeVal +"&FieldID=" + dycustis+ remainingParameter;
    var URLStr = "DynamicColumn.do?" + params;
    $.ajax({
        type: "POST",
        cache: false,
        url: URLStr,
        data: $(this).serializeArray(),
        success: function (data) {
            $("#myModalSearch .modal-body").html(data);
            $("#myModalSearch").modal("show");
            $("#myModalSearch .modal-backdrop").css({
                "position": "inherit"
            });
            $('#agreesearch').html("<i class='fa fa-search'></i>");
            $('#shippersearch').html("<i class='fa fa-search'></i>");
            $('#consigneesearch').html("<i class='fa fa-search'></i>");
            $('#notifysearch').html("<i class='fa fa-search'></i>");
            $('#notifysearch1').html("<i class='fa fa-search'></i>");
            $('#forwardersearch').html("<i class='fa fa-search'></i>");
            $('#Warehousesearch').html("<i class='fa fa-search'></i>");
            $('#NamedCustomersearch').html("<i class='fa fa-search'></i>");
            $('#Sellersearch').html("<i class='fa fa-search'></i>");
            $('#Buyersearch').html("<i class='fa fa-search'></i>");
            $('#'+con+'custsearch'+custid).html("<i class='fa fa-search'></i>");
            
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });
}

function setFieldsState(container, disable) {
    var inputs = container.getElementsByTagName("input");
    for (var i = 0; i < inputs.length; i++) {
        inputs[i].disabled = disable;
    }
}

window.onload = directBooking;

function directBooking() {
    if ($('#NamedCustomer').length > 0) {
        var checkbox = document.getElementById("NamedCustomer");
        var nameField = document.getElementById('ebook_NamedCustomername');
        var collapseDiv = document.getElementById("collapseNamedCustomer");
        var searchButton = document.getElementById("NamedCustomersearch");
        if (nameField.value !== "") {
            checkbox.removeAttribute('checked');
        } else {
            checkbox.setAttribute('checked', 'checked');
        }
        if (document.getElementById('NamedCustomer').checked == true) {
            searchButton.classList.add('disabled');
            setFieldsState(collapseDiv, checkbox.checked);
            $('#NamedCustomerSelect').attr("disabled", true);
            document.getElementById('ebook_NamedCustomername').value = "";
            document.getElementById('ebook_NamedCustomeraddress').value = "";
            document.getElementById('ebook_NamedCustomeraddress1').value = "";
            document.getElementById('ebook_NamedCustomerphone').value = "";
            document.getElementById('ebook_NamedCustomerfax').value = "";
            document.getElementById('ebook_NamedCustomeremail').value = "";
            document.getElementById('ebook_NamedCustomercity').value = "";
            document.getElementById('ebook_NamedCustomercity_desc').value = "";
            document.getElementById('ebook_NamedCustomercountry_name').value = "";
            document.getElementById('ebook_NamedCustomercode').value = "";
            document.getElementById('ebook_NamedCustomerstate').value = "";
            if ($('#ebook_NamedCustomereori').length > 0) {
                $('#ebook_NamedCustomereori').val('');
                $('#NamedCustomertypeperson').val('');
                $('#NamedCustomertypeperson').attr("disabled", true);
            }
            searchButton.classList.add('disabled');
        } else {
            $('#ebook_NamedCustomername').attr("readonly", false);
            $('#NamedCustomerSelect').attr("disabled", false);
            searchButton.classList.remove('disabled');
            setFieldsState(collapseDiv, checkbox.unchecked);
            $('#ebook_NamedCustomercountry_name').typeahead('destroy');
            $('#ebook_NamedCustomername,#ebook_NamedCustomeraddress,#ebook_NamedCustomeraddress1,#ebook_NamedCustomercountry_name,#ebook_NamedCustomercity_desc,#ebook_NamedCustomerphone,#ebook_NamedCustomerstate').attr("readonly", false);
            if ($('#ebook_NamedCustomereori').length > 0) {
                $('#NamedCustomertypeperson').attr("disabled", false);
            }
            $('#ebook_NamedCustomerfax,#ebook_NamedCustomeremail').attr("readonly", false);
            $('#shippersearch').attr('disabled', false);
            $('#NamedCustomerSelect').val("");
            callAutocompleteintialize('#ebook_NamedCustomercountry_name');
            loadStateAutoComplete('ebook_NamedCustomercountry_name', 'ebook_NamedCustomerstate', 'typeheadhidden2', 'load');
        }
    }
}

function setCustDetails(value, type) {

    $('.help-block').html("");
    if (value == "" || value == 'new') {
        if (type == 'agree') {
            $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate,#agreesearch,#ebook_agreeeori,#agreetypeperson').attr("readonly", false);
           if($('#ebook_agreezipcode').length > 0){
            $('#ebook_agreezipcode').attr("readonly", false);
           }
        }
        $('#ebook_' + type + 'name').attr('readonly', false);
        $('#ebook_' + type + 'country_name').typeahead('destroy');
        document.getElementById('ebook_' + type + 'name').value = "";
        document.getElementById('ebook_' + type + 'code').value = "";
        document.getElementById('ebook_' + type + 'name').value = "";
        document.getElementById('ebook_' + type + 'address').value = "";
        document.getElementById('ebook_' + type + 'address1').value = "";
        document.getElementById('ebook_' + type + 'email').value = "";
        document.getElementById('ebook_' + type + 'country_name').value = "";
        document.getElementById('ebook_' + type + 'country').value = "";
        document.getElementById('ebook_' + type + 'phone').value = "";
        document.getElementById('ebook_' + type + 'fax').value = "";
        document.getElementById('ebook_' + type + 'city').value = "";
        document.getElementById('ebook_' + type + 'city_desc').value = "";
        document.getElementById('ebook_' + type + 'state').value = "";
        if($('#ebook_' + type + 'zipcode').length > 0){
            document.getElementById('ebook_' + type + 'zipcode').value = "";
         }
        $('#ebook_' + type + 'state').typeahead('');
        callAutocompleteintialize('#ebook_' + type + 'country_name');
        $('#ebook_' + type + 'country_name').css("background-color", "white");
        loadStateAutoComplete('ebook_' + type + 'country_name', 'ebook_' + type + 'state', 'typeheadhidden9', 'load');
        if ($('#' + type + 'typeperson').length > 0) {
            $('#' + type + 'typeperson').val("");
        }
        if ($('#ebook_' + type + 'eori').length > 0) {
            $('#ebook_' + type + 'eori').val("");
        }

    } else if (value != "") {

        var custval = value.split("~~");
        $('#ebook_' + type + 'country_name').typeahead('destroy');
        $('#ebook_' + type + 'name').attr('readonly', true);
        document.getElementById('ebook_' + type + 'name').value = custval[0];
        document.getElementById('ebook_' + type + 'code').value = custval[1];
        document.getElementById('ebook_' + type + 'name').value = custval[0];
        document.getElementById('ebook_' + type + 'address').value = custval[2];
        document.getElementById('ebook_' + type + 'address1').value = custval[9];
        document.getElementById('ebook_' + type + 'email').value = custval[6];
        document.getElementById('ebook_' + type + 'country_name').value = custval[4].toUpperCase();
        document.getElementById('ebook_' + type + 'country').value = custval[5];
        document.getElementById('ebook_' + type + 'phone').value = custval[7];
        if($('#ebook_' + type + 'zipcode').length > 0){
            console.log("zipcode"+custval[13]+'ebook_' + type + 'zipcode');
            document.getElementById('ebook_' + type + 'zipcode').value = custval[13];
            console.log(document.getElementById('ebook_' + type + 'zipcode').value);
        }
        //if($('ebook_' + type + 'fax').is(":hidden")){
        //  document.getElementById('ebook_' + type + 'fax').setAttribute("readonly", true);
        //   }
        //  else{
        document.getElementById('ebook_' + type + 'fax').value = custval[8];
        //    }
        document.getElementById('ebook_' + type + 'city').value = custval[3];
        document.getElementById('ebook_' + type + 'city_desc').value = custval[3];
        $('#ebook_' + type + 'country_name').css("background-color", "white");
        callAutocompleteintialize('#ebook_' + type + 'country_name');
        $('#ebook_' + type + 'country_name').trigger('change');
        document.getElementById('ebook_' + type + 'state').value = custval[10];
        $('#ebook_' + type + 'state').typeahead('val', custval[10]);
        if (type == 'agree') {
            $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#agreesearch,#ebook_agreeeori,#agreetypeperson').attr("readonly", true);
            if($('#ebook_agreezipcode').length > 0){
                $('#ebook_agreezipcode').attr("readonly", true);
            }
            $('#ebook_' + type + 'country_name').css("background-color", "#eee");
        }
        if ($('#' + type + 'typeperson').length > 0) {
            $('#' + type + 'typeperson').val(custval[11]);
        }
        if ($('#ebook_' + type + 'eori').length > 0) {
            $('#ebook_' + type + 'eori').val(custval[12]);
        }
    }
    if ($('#agreetypeperson').length > 0) {
        $('#agreetypeperson').attr("disabled", false);
    }

    if ('agree' == type) {
        chkAPEnsFilingAgreement();
    }
}
function setagreefielddisabled(type) {
    if (type = 'agree') {
        $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#agreesearch,#ebook_agreeeori,#agreetypeperson').attr("readonly", true);
        if($('#ebook_agreezipcode').length > 0){
            $('#ebook_agreezipcode').attr("readonly", true);
        }
        $('#ebook_' + type + 'country_name').css("background-color", "#eee");
    }
}
function setCustDetails1(value, type) {
    $('.help-block').html("");
    if (value == "" || value == 'new') {
        $('#ebook_' + type + 'country_name_n1').typeahead('destroy');
        $('#ebook_' + type + 'name_n1').attr('readonly', false);
        document.getElementById('ebook_' + type + 'name_n1').value = "";
        document.getElementById('ebook_' + type + 'code_n1').value = "";
        document.getElementById('ebook_' + type + 'name_n1').value = "";
        document.getElementById('ebook_' + type + 'address_n1').value = "";
        document.getElementById('ebook_' + type + 'address1_n1').value = "";
        document.getElementById('ebook_' + type + 'email_n1').value = "";
        document.getElementById('ebook_' + type + 'country_name_n1').value = "";
        document.getElementById('ebook_' + type + 'country_n1').value = "";
        document.getElementById('ebook_' + type + 'phone_n1').value = "";
        document.getElementById('ebook_' + type + 'fax_n1').value = "";
        document.getElementById('ebook_' + type + 'city_n1').value = "";
        document.getElementById('ebook_' + type + 'city_desc_n1').value = "";
        document.getElementById('ebook_' + type + 'state_n1').value = "";
        if($('#ebook_' + type + 'zipcode_n1').length > 0){
            document.getElementById('ebook_' + type + 'zipcode_n1').value = "";
         }
        callAutocompleteintialize('#ebook_' + type + 'country_name_n1');
        loadStateAutoComplete('ebook_' + type + 'country_name_n1', 'ebook_' + type + 'state_n1', 'typeheadhidden9', 'load');
        if ($('#' + type + 'typeperson').length > 0) {
            $('#' + type + 'typeperson').val("");
        }
        if ($('#ebook_' + type + 'eori').length > 0) {
            $('#ebook_' + type + 'eori').val("");
        }

    } else if (value != "") {
        var custval = value.split("~~");
        $('#ebook_' + type + 'name_n1').attr('readonly', true);
        $('#ebook_' + type + 'country_name_n1').typeahead('destroy');
        document.getElementById('ebook_' + type + 'name_n1').value = custval[0];
        document.getElementById('ebook_' + type + 'code_n1').value = custval[1];
        document.getElementById('ebook_' + type + 'name_n1').value = custval[0];
        document.getElementById('ebook_' + type + 'address_n1').value = custval[2];
        document.getElementById('ebook_' + type + 'address1_n1').value = custval[9];
        document.getElementById('ebook_' + type + 'email_n1').value = custval[6];
        document.getElementById('ebook_' + type + 'country_name_n1').value = custval[4].toUpperCase();
        document.getElementById('ebook_' + type + 'country_n1').value = custval[5];
        document.getElementById('ebook_' + type + 'phone_n1').value = custval[7];
        if($('#ebook_' + type + 'zipcode_n1').length > 0){
            document.getElementById('ebook_' + type + 'zipcode_n1').value = custval[13];
        }
        document.getElementById('ebook_' + type + 'fax_n1').value = custval[8];
        document.getElementById('ebook_' + type + 'city_n1').value = custval[3];
        document.getElementById('ebook_' + type + 'city_desc_n1').value = custval[3];
        callAutocompleteintialize('#ebook_' + type + 'country_name_n1');
        $('#ebook_' + type + 'country_name_n1').trigger('change');
        document.getElementById('ebook_' + type + 'state_n1').value = custval[10];
        $('#ebook_' + type + 'state_n1').typeahead('val', custval[10]);
        if ($('#' + type + 'typeperson_n1').length > 0) {
            $('#' + type + 'typeperson_n1').val(custval[11]);
        }
        if ($('#ebook_' + type + 'eori_n1').length > 0) {
            $('#ebook_' + type + 'eori_n1').val(custval[12]);
        }
    }


}
function checkRoutePort(source, target) {
    var sou = $('#' + source).val();
    var tar = $('#' + target).val();
    tar = trim(tar.substring(0, tar.indexOf('-')));
    if (sou != "" && tar != "") {
        if (sou != tar) {
            $(".routingdiv").hide();
            $("#routingdetails").html("");
            //$('#routeshowdiv').show();
            $('.next').html($('#selectvslroute').text() + " <span class=\"glyphicon glyphicon-arrow-right\"></span>").addClass("selectroutepop");
            document.getElementById("selectroute").value = '0';
            document.getElementById('ebook_masthdn').value = "N";
            document.getElementById('checkRoutePol').value = sou;
            document.getElementById('checkRoutePod').value = target;
        }
    }
}

function setSameAsBookingforAgreementParty() {
    if (document.getElementById('agree').checked == true) {
        $('#agreeSelect').prop('checked', false);
        if (document.getElementById('ebook_bookingname').value != "" && document.getElementById('ebook_bookingaddress').value != "" && document.getElementById('ebook_bookingcountry').value != "" && document.getElementById('ebook_bookingcity_desc').value != "") {
            document.getElementById('ebook_agreecountry').value = document.getElementById('ebook_bookingcountry').value;
            document.getElementById('ebook_agreecity_desc').value = document.getElementById('ebook_bookingcity_desc').value;
            document.getElementById('ebook_agreecity_desc').value = document.getElementById('ebook_bookingcity_desc').value;
            document.getElementById('ebook_agreename').value = document.getElementById('ebook_bookingname').value;
            document.getElementById('ebook_agreeaddress').value = document.getElementById('ebook_bookingaddress').value;
            document.getElementById('ebook_agreeaddress1').value = document.getElementById('ebook_bookingaddress1').value;
            document.getElementById('ebook_agreephone').value = document.getElementById('ebook_bookingphone').value;
            document.getElementById('ebook_agreefax').value = document.getElementById('ebook_bookingfax').value;
            document.getElementById('ebook_agreeemail').value = document.getElementById('ebook_bookingemail').value;
            document.getElementById('ebook_agreecode').value = document.getElementById('ebook_bookingcode').value;
            document.getElementById('ebook_agreecountry_name').value = document.getElementById('ebook_bookingcountry_name').value;
            if($('#ebook_agreezipcode').length > 0){
                document.getElementById('ebook_agreezipcode').value = document.getElementById('ebook_bookingzipcode').value;
            }
            if($('#ebook_agreeeori').length > 0){
                document.getElementById('ebook_agreeeori').value = document.getElementById('ebook_bookingeori').value;
            }
            if($('#agreetypeperson').length > 0){
                document.getElementById('agreetypeperson').value = document.getElementById('bookingtypeperson').value;
            }
            var selectText = $("#bookingSelect option:selected").text().trim();
            var selectVal = document.getElementById('bookingSelect').value;
            $('#agreeSelect').val("");
            $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperphone,#ebook_shipperstate,#ebook_shippereori,#shippertypeperson').attr("readonly", false);
            if($('#ebook_shipperzipcode').length > 0){
                $('#ebook_shipperzipcode').attr("readonly", false);
            }
            $('#ebook_shipperfax,#ebook_shipperemail').attr("readonly", false);
            $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreestate,#ebook_agreeeori,#agreetypeperson').attr("readonly", true);
            if($('#ebook_agreezipcode').length > 0){
                $('#ebook_agreezipcode').attr("readonly", true);
            }
            $('#ebook_agreefax,#ebook_agreeemail').attr("readonly", true);
            $('#agreeSelect').attr("disabled", true);
            $('#ebook_agreecountry_name').css("background-color", "#eee");
            $('agreeCheck').val("N");
            $('#agreesearch').attr('disabled', true);
            $('#ebook_agreecountry_name').trigger('change');
            document.getElementById('ebook_agreestate').value = document.getElementById('ebook_bookingstate').value;
            $('#ebook_agreestate').typeahead('val', $('#ebook_bookingstate').val());
            if ($('#agreetypeperson').length > 0) {
                $('#agreetypeperson').attr("disabled", true);
                $('#shippertypeperson').attr("disabled", false);
            }
        } else {
            alert(bkgDet);
            $('#agreeSelect').prop('checked', false);
            return false;
        }
        chkAPEnsFilingAgreement();
    } else {
        $('#agree').prop('checked', false);
        $('#ebook_agreecountry_name').typeahead('destroy');
        document.getElementById('ebook_agreename').value = "";
        document.getElementById('ebook_agreeaddress').value = "";
        document.getElementById('ebook_agreeaddress1').value = "";
        document.getElementById('ebook_agreephone').value = "";
        document.getElementById('ebook_agreefax').value = "";
        document.getElementById('ebook_agreeemail').value = "";
        document.getElementById('ebook_agreecity').value = "";
        document.getElementById('ebook_agreecity_desc').value = "";
        document.getElementById('ebook_agreecountry_name').value = "";
        document.getElementById('ebook_agreecode').value = "";
        document.getElementById('ebook_agreestate').value = "";
        if($('#ebook_agreezipcode').length > 0){
            document.getElementById('ebook_agreezipcode').value = "";
        }
        if($('#ebook_agreeeori').length > 0){
            document.getElementById('ebook_agreeeori').value = "";
        }
        if($('#agreetypeperson').length > 0){
            document.getElementById('agreetypeperson').value = "";
        }
        $('#agreeSelect').prop('checked', false);
        $('#ShipperSelect').attr("disabled", false);
        $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreestate,#ebook_agreeeori,#agreetypeperson').attr("readonly", false);
        if($('#ebook_agreezipcode').length > 0){
            $('#ebook_agreezipcode').attr("readonly", false);
        }
        $('#ebook_agreefax,#ebook_agreeemail').attr("readonly", false);
        $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperphone,#ebook_shipperstate,#ebook_shippereori,#shippertypeperson').attr("readonly", false);
        if($('#ebook_shipperzipcode').length > 0){
            $('#ebook_shipperzipcode').attr("readonly", false);
        }
        $('#ebook_shipperfax,#ebook_shipperemail').attr("readonly", false);
        $('#agreeSelect').attr("disabled", false);
        $('#ebook_shippercountry_name').css("background-color", "white");

        $('#shippersearch').attr('disabled', false);
        $('#agreeSelect').val("");
        callAutocompleteintialize('#ebook_agreecountry_name');
        loadStateAutoComplete('ebook_agreecountry_name', 'ebook_agreestate', 'typeheadhidden8', 'load');
        if ($('#agreetypeperson').length > 0) {
            $('#agreetypeperson').attr("disabled", false);
            $('#shippertypeperson').attr("disabled", false);
        }
    }
 }


function setSameAsBooking() {
    if (document.getElementById('agreeShipper').checked == true) {
        $('#agreementShipper').prop('checked', false);
        if (document.getElementById('ebook_bookingname').value != "" && document.getElementById('ebook_bookingaddress').value != "" && document.getElementById('ebook_bookingcountry').value != "" && document.getElementById('ebook_bookingcity_desc').value != "") {
            document.getElementById('ebook_shippercountry').value = document.getElementById('ebook_bookingcountry').value;
            document.getElementById('ebook_shippercity').value = document.getElementById('ebook_bookingcity_desc').value;
            document.getElementById('ebook_shippercity_desc').value = document.getElementById('ebook_bookingcity_desc').value;
            document.getElementById('ebook_shippername').value = document.getElementById('ebook_bookingname').value;
            document.getElementById('ebook_shipperaddress').value = document.getElementById('ebook_bookingaddress').value;
            document.getElementById('ebook_shipperaddress1').value = document.getElementById('ebook_bookingaddress1').value;
            document.getElementById('ebook_shipperphone').value = document.getElementById('ebook_bookingphone').value;
            document.getElementById('ebook_shipperfax').value = document.getElementById('ebook_bookingfax').value;
            document.getElementById('ebook_shipperemail').value = document.getElementById('ebook_bookingemail').value;
            document.getElementById('ebook_shippercode').value = document.getElementById('ebook_bookingcode').value;
            document.getElementById('ebook_shippercountry_name').value = document.getElementById('ebook_bookingcountry_name').value;
            if($('#ebook_shipperzipcode').length > 0){
                document.getElementById('ebook_shipperzipcode').value = document.getElementById('ebook_bookingzipcode').value;
            }
            if($('#ebook_shippereori').length > 0){
                document.getElementById('ebook_shippereori').value = document.getElementById('ebook_bookingeori').value;
            }
            if($('#shippertypeperson').length > 0){
                document.getElementById('shippertypeperson').value = document.getElementById('bookingtypeperson').value;
            }
            var selectText = $("#bookingSelect option:selected").text().trim();
            var selectVal = document.getElementById('bookingSelect').value;
            $('#shipperSelect').val("");
            $('#agreeSelect').attr("disabled", false);
            $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreestate').attr("readonly", false);
            $('#ebook_agreefax,#ebook_agreeemail').attr("readonly", false);
            if ($("#ebook_agreefax").is(":hidden")) {
                $('#ebook_agreefax').attr("readonly", true);
            }
            $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperphone,#ebook_shipperstate').attr("readonly", true);
            if($('#ebook_shipperzipcode').length > 0){
                $('#ebook_shipperzipcode').attr("readonly", true);
                $('#ebook_agreezipcode').attr("readonly", true);
            }
            if ($('#ebook_agreeeori').length > 0) {
                $('#ebook_agreeeori,#agreetypeperson').attr("readonly", false);
                $('#ebook_shippereori,#shippertypeperson').attr("readonly", true);
            }
            $('#ebook_shipperfax,#ebook_shipperemail').attr("readonly", true);
            $('#shipperSelect').attr("disabled", true);
            $('#ebook_shippercountry_name').css("background-color", "#eee");
            $('#shipperCheck').val("B");
            $('#agreeShipper').prop('checked', true);
            $('#shippersearch').attr('disabled', true);
            $('#ebook_shippercountry_name').trigger('change');
            document.getElementById('ebook_shipperstate').value = document.getElementById('ebook_bookingstate').value;
            $('#ebook_shipperstate').typeahead('val', $('#ebook_bookingstate').val());
        } else
        {
            alert(bkgDet);
            $('#agreementShipper').prop('checked', false);
            return false;
        }
    } else
    {
        $('#agreementShipper').prop('checked', false);
        $('#ebook_shippercountry_name').typeahead('destroy');
        document.getElementById('ebook_shippername').value = "";
        document.getElementById('ebook_shipperaddress').value = "";
        document.getElementById('ebook_shipperaddress1').value = "";
        document.getElementById('ebook_shipperphone').value = "";
        document.getElementById('ebook_shipperfax').value = "";
        document.getElementById('ebook_shipperemail').value = "";
        document.getElementById('ebook_shippercity').value = "";
        document.getElementById('ebook_shippercity_hdn').value = "";
        document.getElementById('ebook_shippercity_desc').value = "";
        document.getElementById('ebook_shippercountry_name').value = '';
        document.getElementById('ebook_shippercountry_name').style.backgroundImage = '';
        document.getElementById('ebook_shippercode').value = '';
        document.getElementById('ebook_shipperstate').value = "";
        $('#shipperSelect').val("");
        $('#agreeSelect').attr("disabled", false);
        $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreestate').attr("readonly", false);
        $('#ebook_agreefax,#ebook_agreeemail').attr("readonly", false);
        $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperphone,#ebook_shipperstate').attr("readonly", false);
        if($('#ebook_shipperzipcode').length > 0){
            $('#ebook_shipperzipcode').attr("readonly", false);
            $('#ebook_agreezipcode').attr("readonly", false);
            $('#ebook_shipperzipcode').val("");

        }
        if ($('#ebook_agreeeori').length > 0) {
            $('#ebook_agreeeori,#agreetypeperson').attr("readonly", false);
            $('#ebook_shippereori,#shippertypeperson').attr("readonly", false);
        }
        $('#ebook_shipperfax,#ebook_shipperemail').attr("readonly", false);
        $('#shipperSelect').attr("disabled", false);
        $('#ebook_shippercountry_name').css("background-color", "white");
        $('#shipperCheck').val("N");
        $('#shippersearch').attr('disabled', false);
        $('#ebook_shippername').attr('readonly', false);
        $('#ebook_agreename').attr('readonly', false);
        $("#shipperSelect option:selected").text("Select Customer");
        if ($('#ebook_agreecode').val() != null && $('#ebook_agreecode').val() != '') {
            $('#ebook_agreename').attr("readonly", true);
        }
        callAutocompleteintialize('#ebook_shippercountry_name');
        loadStateAutoComplete('ebook_shippercountry_name', 'ebook_shipperstate', 'typeheadhidden8', 'load');
        if ($('#ebook_shippereori').length > 0) {
            $('#ebook_shippereori,#shippertypeperson').val('');
        }
    }
   
}
function setSameAsAgree() {


    if (document.getElementById('agreementShipper').checked == true)

    {
        $('#agreeShipper').prop('checked', false);

        if (document.getElementById('ebook_agreename').value != "" && document.getElementById('ebook_agreeaddress').value != "" && document.getElementById('ebook_agreecountry_name').value != "" && document.getElementById('ebook_agreecity_desc').value != "") {
            document.getElementById('ebook_shippercountry').value = document.getElementById('ebook_bookingcountry').value;
            document.getElementById('ebook_shippercity').value = document.getElementById('ebook_agreecity_desc').value;
            document.getElementById('ebook_shippercity_desc').value = document.getElementById('ebook_agreecity_desc').value;
            document.getElementById('ebook_shippername').value = document.getElementById('ebook_agreename').value;
            document.getElementById('ebook_shipperaddress').value = document.getElementById('ebook_agreeaddress').value;
            document.getElementById('ebook_shipperaddress1').value = document.getElementById('ebook_agreeaddress1').value;
            document.getElementById('ebook_shipperphone').value = document.getElementById('ebook_agreephone').value;
            document.getElementById('ebook_shipperfax').value = document.getElementById('ebook_agreefax').value;
            document.getElementById('ebook_shipperemail').value = document.getElementById('ebook_agreeemail').value;
            document.getElementById('ebook_shippercode').value = document.getElementById('ebook_agreecode').value;
            document.getElementById('ebook_shippercountry_name').value = document.getElementById('ebook_agreecountry_name').value;
            if($('#ebook_shipperzipcode').length > 0){
                document.getElementById('ebook_shipperzipcode').value = document.getElementById('ebook_agreezipcode').value;
            }
            if($('#ebook_shippereori').length > 0){
                document.getElementById('ebook_shippereori').value = document.getElementById('ebook_agreeeori').value;
                document.getElementById('shippertypeperson').value = document.getElementById('agreetypeperson').value;
            }
            var selectText = $("#agreeSelect option:selected").text().trim();
            var selectVal = document.getElementById('agreeSelect').value;
            $('#shipperSelect').val("");
            $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreestate,#ebook_agreeeori,#agreetypeperson').attr("readonly", true);
            $('#ebook_agreefax,#ebook_agreeemail').attr("readonly", true);
            $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperphone,#ebook_shipperstate,#ebook_shippereori,#shippertypeperson').attr("readonly", true);
            if($('#ebook_shipperzipcode').length > 0){
                $('#ebook_shipperzipcode').attr("readonly", true);
                $('#ebook_agreezipcode').attr("readonly", true);
            }
            if($('#ebook_shippereori').length > 0){
                $('#ebook_shippereori,#agreetypeperson').attr("readonly", true);
            }
            $('#ebook_shipperfax,#ebook_shipperemail').attr("readonly", true);
            $('#shipperSelect').attr("disabled", true);
            $('#agreeSelect').attr("disabled", true);
            $('#ebook_shippercountry_name').css("background-color", "#eee");
            $('#shipperCheck').val("A");
            $('#agreementShipper').prop('checked', true);

            $('#shippersearch').attr('disabled', true);
            $('#ebook_shippercountry_name').trigger('change');
            document.getElementById('ebook_shipperstate').value = document.getElementById('ebook_agreestate').value;
            $('#ebook_shipperstate').typeahead('val', $('#ebook_agreestate').val());

        } else
        {
            alert(agreeDet);
            $('#agreeShipper').prop('checked', false);
            $('#agreementShipper').prop('checked', false);
            $('#ebook_shippercountry_name').typeahead('destroy');
            $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperphone,#ebook_shipperstate,#ebook_shippereori,#shippertypeperson').attr("readonly", false);


            $('#ebook_shipperfax,#ebook_shipperemail').attr("readonly", false);
            $('#shipperSelect').attr('disabled', false);
            $('#agreeSelect').attr("disabled", false);
            document.getElementById('shipperSelect').value = "";
            document.getElementById('ebook_shippername').value = "";
            document.getElementById('ebook_shipperaddress').value = "";
            document.getElementById('ebook_shipperaddress1').value = "";
            document.getElementById('ebook_shipperphone').value = "";
            document.getElementById('ebook_shipperfax').value = "";
            document.getElementById('ebook_shipperemail').value = "";
            document.getElementById('ebook_shippercity').value = "";
            document.getElementById('ebook_shippercity_hdn').value = "";
            document.getElementById('ebook_shippercity_desc').value = "";
            document.getElementById('ebook_shippercountry_name').value = '';
            document.getElementById('ebook_shippercountry_name').style.backgroundImage = '';
            document.getElementById('ebook_shippercode').value = '';
            document.getElementById('ebook_shipperstate').value = "";
            if($('#ebook_shipperzipcode').length > 0){
                $('#ebook_shipperzipcode').attr("readonly", false);
                document.getElementById('ebook_shipperzipcode').value = "";
            }
            if($('#ebook_shippereori').length > 0){
                document.getElementById('ebook_shippereori').value = '';
                $('#shippertypeperson').val('');
            }
            $('#agreeSelect').val("");
            //  $("#shipperSelect").val($("#shipperSelect option:first").val()).trigger('onchange');
            loadStateAutoComplete('ebook_shippercountry_name', 'ebook_shipperstate', 'typeheadhidden8', 'load');
            if ($('#ebook_shippereori').length > 0) {
                console.log('===>' + $('#ebook_shippereori'));
                document.getElementById('ebook_shippereori').value = '';
                $('#shippertypeperson').val('');
            }
            return false;
        }
    } else
    {
        $('#agreeShipper').prop('checked', false);
        $('#ebook_shippercountry_name').typeahead('destroy');
//        document.getElementById('shipperSelect').value = "";
        // ('#agreeSelect').attr('disabled', true);
        document.getElementById('ebook_shippername').value = "";
        document.getElementById('ebook_shipperaddress').value = "";
        document.getElementById('ebook_shipperaddress1').value = "";
        document.getElementById('ebook_shipperphone').value = "";
        document.getElementById('ebook_shipperfax').value = "";
        document.getElementById('ebook_shipperemail').value = "";
        document.getElementById('ebook_shippercity').value = "";
        document.getElementById('ebook_shippercity_hdn').value = "";
        document.getElementById('ebook_shippercity_desc').value = "";
        document.getElementById('ebook_shippercountry_name').value = '';
        document.getElementById('ebook_shippercountry_name').style.backgroundImage = '';
        document.getElementById('ebook_shippercode').value = '';
        document.getElementById('ebook_shipperstate').value = "";
        if($('#ebook_shipperzipcode').length > 0){
            $('#ebook_shipperzipcode').attr("readonly", false);
            document.getElementById('ebook_shipperzipcode').value = "";
        }
        if($('#ebook_shippereori').length > 0){
            document.getElementById('ebook_shippereori').value = '';
            $('#shippertypeperson').val('');
            $('#ebook_shippereori,#agreetypeperson').attr("readonly", false);
        }
        $('#shipperSelect').val("");
        $('#agreeSelect').val("");

        $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreestate,#ebook_agreeeori,#agreetypeperson').attr("readonly", false);
        if($('#ebook_agreezipcode').length > 0){
            $('#ebook_agreezipcode').attr("readonly", false);
        }
        $('#ebook_agreefax,#ebook_agreeemail').attr("readonly", false);

        $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shipperphone,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperstate,#ebook_shippereori,#shippertypeperson').attr("readonly", false);
       
        $('#ebook_shipperfax,#ebook_shipperemail').attr("readonly", false);
        $('#shipperSelect').attr("disabled", false);
        $('#agreeSelect').attr("disabled", false);
        $('#ebook_shippercountry_name').css("background-color", "white");
        $('#shipperCheck').val("N");
        $('#shippersearch').attr('disabled', false);
        $('#ebook_shippername').attr('readonly', false);
        $("#agreeSelect option:selected").text("Select Customer");
        if ($('#ebook_agreecode').val() != null && $('#ebook_agreecode').val() != '') {
            $('#ebook_agreename').attr("readonly", true);
        }
        callAutocompleteintialize('#ebook_shippercountry_name');
        loadStateAutoComplete('ebook_shippercountry_name', 'ebook_shipperstate', 'typeheadhidden8', 'load');
    }

    if (document.getElementById('agreementShipper').checked == true && $('#ebook_agreeeori') && $('#ebook_agreeeori').val().trim().length > 0) {
        document.getElementById('ebook_shippereori').value = document.getElementById('ebook_agreeeori').value;
        var selectedValue = document.getElementById('agreetypeperson').value;
        $('#shippertypeperson').val(selectedValue);
        $('#shippertypeperson,#ebook_shippereori,#ebook_agreeeori,#agreetypeperson').attr("readonly", true);
    } else if ($('#ebook_shippereori')) {
        console.log('===>' + $('#ebook_shippereori'));
        document.getElementById('ebook_shippereori').value = '';
        $('#shippertypeperson').val('');
        $('#shippertypeperson,#ebook_shippereori,#ebook_agreeeori,#agreetypeperson').attr("readonly", false);
    }
}



function populateNotify()
{
    if (document.getElementById('notifyconsig').checked == true)
    {
        if ($('#consigneetoorder').is(":checked")) {
            setSameAndToOrder('notify');
        } else {
            if (document.getElementById('ebook_consigneename').value != "" && document.getElementById('ebook_consigneeaddress').value != "" && document.getElementById('ebook_consigneecountry').value != "" && document.getElementById('ebook_consigneecity_desc').value != "") {
                form1.valid();
                if ($('#ebook_consigneephone').hasClass("has-error") && ($('#ebook_consigneephone-error').html().length > 0 || $('#ebook_consigneephone-error').css('display') == 'block')) {
                    document.getElementById('notifyconsig').checked = false;
                    return false;
                } else if ($('#ebook_consigneefax').hasClass("has-error") && ($('#ebook_consigneefax-error').html().length > 0 || $('#ebook_consigneefax-error').css('display') == 'block')) {
                    document.getElementById('notifyconsig').checked = false;
                    return false;
                } else if ($('#ebook_consigneeemail').hasClass("has-error") && ($('#ebook_consigneeemail-error').html().length > 0 || $('#ebook_consigneeemail-error').css('display') == 'block')) {
                    document.getElementById('notifyconsig').checked = false;
                    return false;
                } else {
                    $('.help-block').html("");
                    $('.help-block').css({
                        "border-color": "#ccc"
                    });
                    document.getElementById('ebook_notifycountry').value = document.getElementById('ebook_consigneecountry').value;
                    document.getElementById('ebook_notifycity').value = document.getElementById('ebook_consigneecity').value;
                    document.getElementById('ebook_notifycity_desc').value = document.getElementById('ebook_consigneecity_desc').value;
                    document.getElementById('ebook_notifycity_hdn').value = document.getElementById('ebook_consigneecity_hdn').value;
                    document.getElementById('ebook_notifyname').value = document.getElementById('ebook_consigneename').value;
                    document.getElementById('ebook_notifyaddress').value = document.getElementById('ebook_consigneeaddress').value;
                    document.getElementById('ebook_notifyaddress1').value = document.getElementById('ebook_consigneeaddress1').value;
                    document.getElementById('ebook_notifyphone').value = document.getElementById('ebook_consigneephone').value;
                    document.getElementById('ebook_notifyfax').value = document.getElementById('ebook_consigneefax').value;
                    document.getElementById('ebook_notifyemail').value = document.getElementById('ebook_consigneeemail').value;
                    document.getElementById('ebook_notifycode').value = document.getElementById('ebook_consigneecode').value;
                    document.getElementById('ebook_notifycountry_name').value = document.getElementById('ebook_consigneecountry_name').value;
                    if($('#ebook_consigneezipcode').length > 0){
                        document.getElementById('ebook_notifyzipcode').value = document.getElementById('ebook_consigneezipcode').value;
                    }
                    var selectText = $("#consigneeSelect option:selected").text().trim();
                    var selectVal = document.getElementById('consigneeSelect').value;
                    $('#notifySelect').val("");
                    document.getElementById('hidn1check').value = '1';
                    $('#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", true);
                    if($('#ebook_consigneezipcode').length > 0){
                        $('#ebook_notifyzipcode').attr("readonly", true);
                        $('#ebook_consigneezipcode').attr("readonly", true);
                    }
                    $('#ebook_notifyname,#ebook_notifyaddress,#ebook_notifyaddress1,#ebook_notifycountry_name,#ebook_notifycity_desc,#ebook_notifyphone,#ebook_notifystate').attr("readonly", true);
                    $('#ebook_notifyfax,#ebook_notifyemail').attr("readonly", true);
                    $('#notifySelect,#consigneeSelect').attr("disabled", true);
                    $('#ebook_consigneecountry_name,#ebook_notifycountry_name').css("background-color", "#eee");
                    $('#notifyCheck').val("Y");
                    $('#hidNTToOrder').val("N");
                    $('#consigneesearch,#notifysearch').attr('disabled', true);
                    $('#consigneetoorder').attr("disabled", true);
                    $('#ebook_notifycountry_name').trigger('change');
                    document.getElementById('ebook_notifystate').value = document.getElementById('ebook_consigneestate').value;
                    $('#ebook_notifystate').typeahead('val', $('#ebook_consigneestate').val());
                }
            } else
            {
                alert(consDet);
                document.getElementById('notifyconsig').checked = false;
                if ($('#ebook_notifyeori')) {
                    document.getElementById('ebook_notifyeori').value = '';
                    $('#notifytypeperson').val('');
                    $('#notifytypeperson,#ebook_notifyeori,#ebook_consigneeeori,#consigneetypeperson').attr("readonly", false);
                }
                return false;
            }
        }
    } else
    {
        $('#ebook_notifycountry_name').typeahead('destroy');
        if ($('#consigneetoorder').is(":checked")) {
            $('#ebook_notifyname').attr("readonly", false);
            $('#ebook_notifyname').val('');
            $('#ebook_notifyname').attr("disabled", false);
            $('#ebook_notifyname,#ebook_notifyaddress,#ebook_notifyaddress1,#ebook_notifycountry_name,#ebook_notifycity_desc,#ebook_notifyphone,#ebook_notifystate').attr("readonly", false);
            $('#ebook_notifyname,#ebook_notifyaddress,#ebook_notifyaddress1,#ebook_notifycountry_name,#ebook_notifycity_desc,#ebook_notifyphone,#ebook_notifycountry,#ebook_notifystate').val("");
            if($('#ebook_notifyzipcode').length > 0){
                document.getElementById('ebook_notifyzipcode').value = "";
                $('#ebook_notifyzipcode').attr("readonly", false);
            }
            $('#ebook_notifyfax,#ebook_notifyemail').attr("readonly", false);
            $('#notifySelect').attr("disabled", false);
            $('#ebook_notifycountry_name').css("background-color", "white");
            $('#notifyCheck').val("N");
            $('#hidNTToOrder').val("N");
            $('#notifysearch').attr('disabled', false);
            if ($('#notify1Check').val() != 'Y') {
                $('#consigneetoorder').attr("disabled", false);
                $('#ebook_consigneeemail,#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", false);
                if($('#ebook_consigneezipcode').length > 0){
                    $('#ebook_consigneezipcode').attr("readonly", false);
                }
                $('#ebook_consigneecountry_name').css("background-color", "white");
            }
            if ($('#notify1Check').val() != 'Y') {
                $('#ebook_consigneename').attr("readonly", false);
            }
            loadStateAutoComplete('ebook_notifycountry_name', 'ebook_notifystate', 'typeheadhidden10', 'load');
        } else {
            $('.help-block').html("");
            $('.help-block').css({
                "border-color": "#ccc"
            });

            document.getElementById('ebook_notifyname').value = "";
            document.getElementById('ebook_notifyaddress').value = "";
            document.getElementById('ebook_notifyaddress1').value = "";
            document.getElementById('ebook_notifyphone').value = "";
            document.getElementById('ebook_notifyfax').value = "";
            document.getElementById('ebook_notifyemail').value = "";
            document.getElementById('ebook_notifycity').value = "";
            document.getElementById('notifySelect').value = "";
            document.getElementById('ebook_notifycity_hdn').value = "";
            document.getElementById('ebook_notifycity_desc').value = "";
            document.getElementById('ebook_notifycountry_name').value = '';
            document.getElementById('ebook_notifycountry_name').style.backgroundImage = '';
            if($('#ebook_notifyzipcode').length > 0){
                document.getElementById('ebook_notifyzipcode').value = "";
                $('#ebook_notifyzipcode').attr("readonly", false);
            }
            document.getElementById('hidn1check').value = '0';
            document.getElementById('ebook_notifystate').value = "";
            $("#notifySelect option:selected").text("Select Customer");
            if ($('#notify1Check').val() != 'Y') {
                $('#ebook_consigneeemail,#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", false);
                if($('#ebook_consigneezipcode').length > 0){
                    $('#ebook_consigneezipcode').attr("readonly", false);
                }
                $('#consigneeSelect').attr("disabled", false);
                $('#ebook_consigneecountry_name').css("background-color", "white");
                $('#consigneesearch').attr('disabled', false);
            }
            $('#ebook_notifyname,#ebook_notifyaddress,#ebook_notifyaddress1,#ebook_notifycountry_name,#ebook_notifycity_desc,#ebook_notifyphone,#ebook_notifystate').attr("readonly", false);
             if($('#ebook_notifyzipcode').length > 0){
             $('#ebook_notifyzipcode').attr("readonly", false);
              }
            $('#ebook_notifyfax,#ebook_notifyemail').attr("readonly", false);
            $('#notifySelect').attr("disabled", false);
            $('#ebook_notifycountry_name').css("background-color", "white");
            $('#notifyCheck').val("N");
            $('#notifysearch').attr('disabled', false);
            if ($('#notify1Check').val() != 'Y') {
                $('#consigneetoorder').attr("disabled", false);
            }
            $('#hidNTToOrder').val("N");
            if ($('#ebook_consigneecode').val() != null && $('#ebook_consigneecode').val() != '') {
                $('#ebook_consigneename').attr("readonly", true);
            }

        }
        callAutocompleteintialize('#ebook_notifycountry_name');
        loadStateAutoComplete('ebook_notifycountry_name', 'ebook_notifystate', 'typeheadhidden10', 'load');
    }
    if (document.getElementById('notifyconsig').checked == true && $('#ebook_consigneeeori') && $('#ebook_consigneeeori').val().trim().length > 0) {

        document.getElementById('ebook_notifyeori').value = document.getElementById('ebook_consigneeeori').value;
        var selectedValue = document.getElementById('consigneetypeperson').value;
        $('#notifytypeperson').val(selectedValue);
        $('#notifytypeperson,#ebook_notifyeori,#ebook_consigneeeori,#consigneetypeperson').attr("readonly", true);
    } else {
        if ($('#ebook_notifyeori')) {
            document.getElementById('ebook_notifyeori').value = '';
            $('#notifytypeperson').val('');
            $('#notifytypeperson,#ebook_notifyeori,#ebook_consigneeeori,#consigneetypeperson').attr("readonly", false);
        }
    }
}

function populateNotify1()
{
    if (document.getElementById('notifyconsig1').checked == true)
    {
        if ($('#consigneetoorder').is(":checked")) {
            setSameAndToOrder('notify1');
        } else {
            if (document.getElementById('ebook_consigneename').value != "" && document.getElementById('ebook_consigneeaddress').value != "" && document.getElementById('ebook_consigneecountry').value != "" && document.getElementById('ebook_consigneecity_desc').value != "") {
                form1.valid();
                if ($('#ebook_consigneephone').hasClass("has-error") && ($('#ebook_consigneephone-error').html().length > 0 || $('#ebook_consigneephone-error').css('display') == 'block')) {

                    document.getElementById('notifyconsig1').checked = false;
                    return false;
                } else if ($('#ebook_consigneefax').hasClass("has-error") && ($('#ebook_consigneefax-error').html().length > 0 || $('#ebook_consigneefax-error').css('display') == 'block')) {
                    document.getElementById('notifyconsig1').checked = false;
                    return false;
                } else if ($('#ebook_consigneeemail').hasClass("has-error") && ($('#ebook_consigneeemail-error').html().length > 0 || $('#ebook_consigneeemail-error').css('display') == 'block')) {
                    document.getElementById('notifyconsig1').checked = false;
                    return false;
                } else {
                    $('.help-block').html("");
                    $('.help-block').css({
                        "border-color": "#ccc"
                    });
                    document.getElementById('ebook_notifycountry_name_n1').value = document.getElementById('ebook_consigneecountry_name').value;
                    document.getElementById('ebook_notifycountry_n1').value = document.getElementById('ebook_consigneecountry').value;
                    document.getElementById('ebook_notifycity_n1').value = document.getElementById('ebook_consigneecity').value;
                    document.getElementById('ebook_notifycity_desc_n1').value = document.getElementById('ebook_consigneecity_desc').value;
                    document.getElementById('ebook_notifycity_hdn_n1').value = document.getElementById('ebook_consigneecity_hdn').value;
                    document.getElementById('ebook_notifyname_n1').value = document.getElementById('ebook_consigneename').value;
                    document.getElementById('ebook_notifyaddress_n1').value = document.getElementById('ebook_consigneeaddress').value;
                    document.getElementById('ebook_notifyaddress1_n1').value = document.getElementById('ebook_consigneeaddress1').value;
                    document.getElementById('ebook_notifyphone_n1').value = document.getElementById('ebook_consigneephone').value;
                    document.getElementById('ebook_notifyfax_n1').value = document.getElementById('ebook_consigneefax').value;
                    document.getElementById('ebook_notifyemail_n1').value = document.getElementById('ebook_consigneeemail').value;
                    document.getElementById('ebook_notifycode_n1').value = document.getElementById('ebook_consigneecode').value;
                    if($('#ebook_consigneezipcode').length > 0){
                        document.getElementById('ebook_notifyzipcode_n1').value = document.getElementById('ebook_consigneezipcode').value;
                        $('#ebook_consigneezipcode').attr("readonly", true);
                        $('#ebook_notifyzipcode_n1').attr("readonly", true);
                    }
                    var selectText = $("#consigneeSelect option:selected").text().trim();
                    var selectVal = document.getElementById('consigneeSelect').value;
                    $('#notify1Select').val("");
                    document.getElementById('hidn2check').value = '1';
                    callAutocompleteintialize('#ebook_notifycountry_name_n1');
                    $('#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", true);
                    $('#ebook_consigneeemail,#ebook_notifyname_n1,#ebook_notifyaddress_n1,#ebook_notifyaddress1_n1,#ebook_notifycountry_name_n1,#ebook_notifycity_desc_n1,#ebook_notifyphone_n1,#ebook_notifystate_n1').attr("readonly", true);
                    $('#ebook_notifyfax_n1,#ebook_notifyemail_n1').attr("readonly", true);
                    $('#notify1Select,#consigneeSelect').attr("disabled", true);
                    $('#ebook_consigneecountry_name,#ebook_notifycountry_name_n1').css("background-color", "#eee");
                    $('#notify1Check').val("Y");
                    $('#consigneetoorder').attr("disabled", true);
                    $('#hidNT1ToOrder').val("N");
                    $('#consigneesearch,#notifysearch1').attr('disabled', true);
                    $('#ebook_notifycountry_name_n1').trigger('change');
                    document.getElementById('ebook_notifystate_n1').value = document.getElementById('ebook_consigneestate').value;
                    $('#ebook_notifystate_n1').typeahead('val', $('#ebook_consigneestate').val());
                }
            } else
            {
                alert(consDet);
                document.getElementById('notifyconsig1').checked = false;
                if ($('#ebook_notifyeori_n1')) {
                    document.getElementById('ebook_notifyeori_n1').value = '';
                    $('#notifytypeperson_n1').val('');
                    $('#notifytypeperson_n1,#ebook_notifyeori_n1,#ebook_consigneeeori,#consigneetypeperson').attr("readonly", false);
                }
                return false;
            }
        }
    } else
    {
        $('#ebook_notifycountry_name_n1').typeahead('destroy');
        if ($('#consigneetoorder').is(":checked")) {
            $('#ebook_notifyname_n1').attr("readonly", false);
            $('#ebook_notifyname_n1').val('');
            $('#ebook_notifyname_n1').attr("disabled", false);
            $('#ebook_notifyname_n1,#ebook_notifyaddress_n1,#ebook_notifyaddress1_n1,#ebook_notifycountry_name_n1,#ebook_notifycity_desc_n1,#ebook_notifyphone_n1,#ebook_notifystate_n1').attr("readonly", false);
            $('#ebook_notifyname_n1,#ebook_notifyaddress_n1,#ebook_notifyaddress1_n1,#ebook_notifycountry_name_n1,#ebook_notifycity_desc_n1,#ebook_notifyphone_n1,#ebook_notifystate_n1').val("");
            if($('#ebook_notifyzipcode_n1').length > 0){
                $('#ebook_notifyzipcode_n1').attr("readonly", false);
                $('#ebook_notifyzipcode_n1').val("");
            }
            $('#ebook_notifyfax_n1,#ebook_notifyemail_n1').attr("readonly", false);
            $('#notify1Select').attr("disabled", false);
            $('#ebook_notifycountry_name_n1').css("background-color", "white");
            $('#notify1Check').val("N");
            $('#hidNT1ToOrder').val("N");
            if ($('#notifyCheck').val() != 'Y') {
                $('#consigneetoorder').attr("disabled", false);
                $('#ebook_consigneecountry_name').css("background-color", "white");
                $('#ebook_consigneeemail,#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", false);
                if($('#ebook_consigneezipcode').length > 0){
                    $('#ebook_consigneezipcode').attr("readonly", false);
                }
            }
            $('#notifysearch1').attr('disabled', false);
            if ($('#notifyCheck').val() != 'Y') {
                $('#ebook_consigneename').attr("readonly", false);
            }
            loadStateAutoComplete('ebook_notifycountry_name_n1', 'ebook_notifystate_n1', 'typeheadhidden11', 'load');
        } else {
            $('.help-block').html("");
            $('.help-block').css({
                "border-color": "#ccc"
            });
            document.getElementById('ebook_notifyname_n1').value = "";
            document.getElementById('ebook_notifyaddress_n1').value = "";
            document.getElementById('ebook_notifyaddress1_n1').value = "";
            document.getElementById('ebook_notifyphone_n1').value = "";
            document.getElementById('ebook_notifyfax_n1').value = "";
            document.getElementById('ebook_notifyemail_n1').value = "";
            document.getElementById('ebook_notifycity_n1').value = "";
            document.getElementById('notify1Select').value = "";
            document.getElementById('ebook_notifycity_hdn_n1').value = "";
            document.getElementById('ebook_notifycity_desc_n1').value = "";
            document.getElementById('ebook_notifycountry_name_n1').value = '';
            document.getElementById('ebook_notifycountry_name_n1').style.backgroundImage = '';
            if($('#ebook_notifyzipcode_n1').length > 0){
                document.getElementById('ebook_notifyzipcode_n1').value = "";
                $('#ebook_notifyzipcode_n1').attr("readonly", false);
            }
            document.getElementById('hidn2check').value = '0';
            document.getElementById('ebook_notifystate_n1').value = "";
            $("#notify1Select option:selected").text("Select Customer");
            if ($('#notifyCheck').val() != 'Y') {
                $('#ebook_consigneeemail,#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", false);
                if($('#ebook_consigneezipcode').length > 0){
                    $('#ebook_consigneezipcode').attr("readonly", false);
                }
                $('#consigneeSelect').attr("disabled", false);
                $('#ebook_consigneecountry_name').css("background-color", "white");
                $('#consigneesearch').attr('disabled', false);
            }
            $('#ebook_notifyname_n1,#ebook_notifyaddress_n1,#ebook_notifyaddress1_n1,#ebook_notifycountry_name_n1,#ebook_notifycity_desc_n1,#ebook_notifyphone_n1,#ebook_notifystate_n1').attr("readonly", false);
            $('#ebook_notifyfax_n1,#ebook_notifyemail_n1').attr("readonly", false);
            $('#notify1Select').attr("disabled", false);
            $('#ebook_notifycountry_name_n1').css("background-color", "white");
            $('#notify1Check').val("N");
            $('#hidNT1ToOrder').val("N");
            $('#notifysearch1').attr('disabled', false);
            if ($('#notifyCheck').val() != 'Y') {
                $('#consigneetoorder').attr("disabled", false);
            }
            if ($('#ebook_consigneecode').val() != null && $('#ebook_consigneecode').val() != '') {
                $('#ebook_consigneename').attr("readonly", true);
            }
        }
        callAutocompleteintialize('#ebook_notifycountry_name_n1');
        loadStateAutoComplete('ebook_notifycountry_name_n1', 'ebook_notifystate_n1', 'typeheadhidden11', 'load');
    }
    if (document.getElementById('notifyconsig1').checked == true && $('#ebook_consigneeeori') && $('#ebook_consigneeeori').val().trim().length > 0) {

        document.getElementById('ebook_notifyeori_n1').value = document.getElementById('ebook_consigneeeori').value;
        var selectedValue = document.getElementById('consigneetypeperson').value;
        $('#notifytypeperson_n1').val(selectedValue);
        $('#notifytypeperson_n1,#ebook_notifyeori_n1,#ebook_consigneeeori,#consigneetypeperson').attr("readonly", true);
    } else {
        if ($('#ebook_notifyeori_n1')) {
            document.getElementById('ebook_notifyeori_n1').value = '';
            $('#notifytypeperson_n1').val('');

            $('#notifytypeperson_n1,#ebook_notifyeori_n1,#ebook_consigneeeori,#consigneetypeperson').attr("readonly", false);
        }
    }
}

function getRouteDetails() {
    var origin = $('#ebook_origin_country').val();
    var delivery = $('#ebook_delivery_country').val();
    if (origin == "" && delivery == "")
    {
        alert(placeOfOrigin);
        $('#ebook_origin_country').focus();
        return false;
    }
    if (delivery == "")
    {
        alert(placeOfDelivery);
        $('#ebook_delivery_country').focus();
        return false;
    }
    if (origin != "" && delivery != "") {
        //$('#routehref').html("<i class='fa fa-spinner fa-spin'></i> Change Route");
        $('.routehrefclass').addClass("disabled");
        document.getElementById('routehref').disabled = true;
        $.ajax({
            type: "POST",
            cache: false,
            url: "ebookRoutingDetails?origin_desc=" + $('#ebook_origin_country').val() + "&delivery_desc=" + $('#ebook_delivery_country').val() + "&indate=" + $('#ebook_date').val(),
            data: $(this).serializeArray(),
            success: function (data) {
                // $('#routehref').html('<i class="fa fa-pencil-square" id="routeimg"></i> Change Route');
                $.fancybox.open(data, {
                    showCloseButton: false,
                    closeClick: false,
                    maxWidth: 1200,
                    maxHeight: 600,
                    fitToView: true,
                    width: '90%',
                    height: '100%',
                    autoSize: true,
                    scrollOutside: true,
                    //closeBtn   : false,
                    hideOnOverlayClick: false,
                    overlayShow: false,
                    helpers: {
                        overlay: {
                            closeClick: false
                        }
                    },
                    keys: {
                        close: null
                    },
                    afterClose: function () {
                        if ($("#selectroute").val() > 1)
                        {
                            $('.next').removeClass("disabled");
                            $(".steps li").removeClass("done");
                            $(".steps li").addClass("disabled");
                            $(".steps li.first").removeClass("done");
                            $(".steps li.first").removeClass("disabled");
                            getPort('ebook_origin_country', 'ebook_origin_desc', 'ebook_origin', defOrgDesHaulValue);
                            getPort('ebook_delivery_country', 'ebook_delivery_desc', 'ebook_delivery', defOrgDesHaulValue);
                        }
                    },
                    afterShow: function () {
                        //$(".fancybox-opened").addClass("modal-draggable");

                        //$(".modal-draggable .fancybox-skin").draggable({
                        //    handle: ".fancybox-outer"
                        //});
                    }
                });

            },
            error: function (data, xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
                $('#routeimg').html('<i class="fa fa-pencil-square default-text-color" id="routeimg"></i>');
                alert(errorMsg);
            }
        });
    }
}
function getVesselSchedule() {
    var origin = $('#ebook_origin_country').val();
    var delivery = $('#ebook_delivery_country').val();
    document.getElementById('checkRoutePol').value = $('#ebook_origin').val();
    document.getElementById('checkRoutePod').value = $('#ebook_delivery').val();

    if (origin == "")
    {
        alert(PlaceOfOrigin);
        $('#ebook_origin_country').focus();
        return false;
    }

    if (delivery == "")
    {
        alert(placeOfDelivery);
        $('#ebook_delivery_country').focus();
        return false;
    }
    //$('#vesselhref').html("<i class='fa fa-spinner fa-spin'></i> Loading...");
    $.ajax({
        type: "POST",
        cache: false,
        //async : false,
        url: "ebookRoutingDetails?origin_desc=" + $('#ebook_origin_country').val() + "&delivery_desc=" + $('#ebook_delivery_country').val() + "&indate=" + $('#ebook_date').val(),
        data: $(this).serializeArray(),
        success: function (data) {
            $.fancybox.open(data, {
                showCloseButton: false,
                closeClick: false,
                maxWidth: 1200,
                maxHeight: 600,
                fitToView: true,
                width: '100%',
                height: '100%',
                autoSize: true,
                scrollOutside: true,
                hideOnOverlayClick: false,
                overlayShow: false,
                helpers: {
                    overlay: {
                        closeClick: false
                    }
                },
                keys: {
                    close: null
                },
                afterClose: function () {
                    console.log('after schedule close');
                    $('#vesselhref').html("<i class='fa fa-pencil-square' id='routeimg'></i> Select Vessel Schedule")
                    if ($("#selectroute").val() > 1)
                    {
                        document.getElementById('ebook_masthdn').value = "Y"
                        $('.next').html("Next <span class=\"glyphicon glyphicon-arrow-right\"></span>").removeClass("selectroutepop");
                        $(".routingdiv").show();
                        $(".vesseldiv").hide();
                        $('.next').removeClass("disabled");
                        $(".steps li").removeClass("done");
                        $(".steps li").addClass("disabled");
                        $(".steps li.first").removeClass("done");
                        $(".steps li.first").removeClass("disabled");
                        getPort('ebook_origin_country', 'ebook_origin_desc', 'ebook_origin', defOrgDesHaulValue);
                        getPort('ebook_delivery_country', 'ebook_delivery_desc', 'ebook_delivery', defOrgDesHaulValue);

                    } else
                    {
                        $('.next').html($('#selectvslroute').text() + " <span class=\"glyphicon glyphicon-arrow-right\"></span>").addClass("selectroutepop");
                        $('.next').removeClass("disabled");
                    }

                },
                afterShow: function () {
                    //$(".fancybox-opened").addClass("modal-draggable");

                    //$(".modal-draggable .fancybox-skin").draggable({
                    //    handle: ".fancybox-outer"
                    //  });
                }

            });

        },
        error: function (data, xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
            $('.next').html($('#selectvslroute').text() + " <span class=\"glyphicon glyphicon-arrow-right\"></span>").addClass("selectroutepop");
            alert(errorMsg);
        }
    });
    return false;

}

function callCarogoPage(indexid, type) {
    $.ajax({
        type: "POST",
        cache: false,
        url: "CargoEditAjax?booktype_new=" + type + "&indexid=" + indexid,
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            //alert(data)
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });

}
function backtobookview() {
    openLoader();
    location.href = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
}
function saveTemplate_new(temptype)
{
    var tempname = trim(document.getElementById('tmponame').value);
    if (tempname == "")
    {
        alert(entTempName);
        document.getElementById('tmponame').focus();
        return false;
    }
    $("#templatebtn").button('loading');
    if (temptype == "save")
    {
        $.ajax({
            type: "POST",
            async: false,
            cache: false,
            url: "ecomAjax?type=templateCheck&templateName=" + tempname,
            success: function (response) {
                if (response == 'Exist') {
                    alert(tempName);
                    $("#templatebtn").button('reset');
                } else {
                    $('#myModal').modal('hide');
                    openLoader();
                    $('#agreeSelect,#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate').attr("disabled", false);
                    if($('#ebook_agreezipcode').length > 0){
                        $('#ebook_agreezipcode').attr("readonly", false);
                    }
                    document.getElementById('ebook_templatename').value = tempname;
                    document.EbookingValidatorFormBeanNew.submit();
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
function saveTemplate_reuse(temptype)
{
    var tempname = trim(document.getElementById('tmponame').value);
    if (tempname == "")
    {
        alert(entTempName);
        document.getElementById('tmponame').focus();
        return false;
    }
    $("#templatebtn").button('loading');
    if (temptype == "save")
    {
        $.ajax({
            type: "POST",
            async: false,
            cache: false,
            url: "ecomAjax?type=templateCheck&templateName=" + tempname,
            success: function (response) {
                if (response == 'Exist') {
                    alert(tempName);
                    $("#templatebtn").button('reset');
                } else {
                    $('#myModal').modal('hide');
                    openLoader();
                    $('#agreeSelect,#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate').attr("disabled", false);
                    if($('#ebook_agreezipcode').length > 0){
                        $('#ebook_agreezipcode').attr("readonly", false);
                    } 
                    document.getElementById('ebook_templatename').value = tempname;
                    document.AmendmentBookingFormBean.submit();
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
function getEqpList_new(size, id) {

    if (allEquipmentListJs != null && allEquipmentListJs.length > 0) {
        $('#' + id).html('');
        $('#' + id).html('<option value="">Select Eqp Size</option>' + allEquipmentListJs);
    } else {
        $('#' + id).html('');
        $('#' + id).append($('<option>', {
            value: '',
            text: 'Select Eqp Size'
        }));
        $.each(EquipmentJson, function (i, obj) {
            //use obj.id and obj.name here, for example:
            allEquipmentListJs = allEquipmentListJs + '<option value="' + obj.equipSize + '">' + obj.equipDesc + '</option>'
        });
        $('#' + id).append(allEquipmentListJs);
    }

//    var i = 0;
//    if (size == 'D' && dryEquipmentListJs != null && dryEquipmentListJs.length > 0) {
//        $('#' + id).html('');
//        $('#' + id).html('<option value="">Select Eqp Size</option>' + dryEquipmentListJs);
//    } else if (size == 'R' && referEquipmentListJs != null && referEquipmentListJs.length > 0) {
//        $('#' + id).html('');
//        $('#' + id).html('<option value="">Select Eqp Size</option>' + referEquipmentListJs);
//    } else if (size == 'O' && openEquipmentListJs != null && openEquipmentListJs.length > 0) {
//        $('#' + id).html('');
//        $('#' + id).html('<option value="">Select Eqp Size</option>' + openEquipmentListJs);
//    } else {
//        $('#' + id).html('');
//        $('#' + id).append($('<option>', {
//            value: '',
//            text: 'Select Eqp Size'
//        }));
//        $.each(EquipmentJson, function (i, obj) {
//            //use obj.id and obj.name here, for example:
//            if (size == obj.equipType) {
//                i++;
//                if (size == 'D') {
//                    dryEquipmentListJs = dryEquipmentListJs + '<option value="' + obj.equipSize + '">' + obj.equipDesc + '</option>'
//                } else if (size == 'R') {
//                    referEquipmentListJs = referEquipmentListJs + '<option value="' + obj.equipSize + '">' + obj.equipDesc + '</option>'
//                } else if (size == 'O') {
//                    openEquipmentListJs = openEquipmentListJs + '<option value="' + obj.equipSize + '">' + obj.equipDesc + '</option>'
//                }
//                $('#' + id).append($('<option>', {
//                    value: obj.equipSize,
//                    text: obj.equipDesc
//                }));
//            } else if (size == 'D' && obj.equipType == 'T') {
//                dryEquipmentListJs = dryEquipmentListJs + '<option value="' + obj.equipSize + '">' + obj.equipDesc + '</option>'
//                if (size == 'D') {
//                    $('#' + id).append($('<option>', {
//                        value: obj.equipSize,
//                        text: obj.equipDesc
//                    }));
//                }
//            }
//        });
//    }
}
function setDGValues(id) {
    //alert(id);
    var type = $('#hzd' + id).val();
    var size = $('#eqpSize' + id).val();
    
    // Clear all DG entries for this container (support multiple DG)
    var dgContainer = $('#dgContainer' + id);
    if (dgContainer.length > 0) {
        dgContainer.find('.dg-entry').each(function() {
            var entryId = $(this).attr('id');
            var suffix = entryId.replace('dgEntry' + id, '');
            
            $('#shippingName' + id + suffix).val("");
            $('#dgClass' + id + suffix).val("");
            $('#unno' + id + suffix).val("");
        });
    } else {
        // Fallback for old structure (backward compatibility)
        $('#shippingName' + id).val("");
        $('#dgClass' + id).val("");
        $('#unno' + id).val("");
    }

}
function setShippingName(value, id, suffix) {
    // Support both old format (no suffix) and new format (with _1, _2, etc.)
    suffix = suffix || '';
    
    $.ajax({
        type: "POST",
        cache: false,
        url: "PortListAjax?type=IMDGClassDetails&unno=" + value,
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            var str = data.split("~~");
            $('#shippingName' + id + suffix).val(str[1]);
            $('#dgClass' + id + suffix).val(str[0]);
            $('#shippingName' + id + suffix).attr("readonly", true);
            $('#dgClass' + id + suffix).attr("readonly", true);
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });

}


function getTareweight(value, id) {
    var eqptype = $('#eqpType' + id).val();
    console.log('eqptype=====> ' + eqptype);
    $.ajax({
        type: "POST",
        cache: false,
        url: "PortListAjax?type=getTareweight&eqptype=" + eqptype + "&equipdesc=" + value,
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            console.log('data=====> ' + data);
            var str = data;

            $('#tareWt' + id).attr("readonly", true);
            var cargoWtunit = $('#cargoWtUnit' + id).val();
            var carwtunitcon = $('#cargoWtUnit' + id + '_previous').val();

            //document.getElementById('grossWt'+id).value="";
            //document.getElementById('cargoWt'+id).value="";
            var tare = convertWeightUnit(str, 'K', cargoWtunit);
            if (carwtunitcon == 'K') {
                var net = convertWeightUnit($('#netWt' + id).val(), carwtunitcon, 'P');
            } else if (carwtunitcon == 'P')
            {
                var net = convertWeightUnit($('#netWt' + id).val(), carwtunitcon, 'K');

            }

            $('#cargoWt' + id).val(convertWeightUnit($('#cargoWt' + id).val(), $('#cargoWtUnit' + id + '_previous').val(), cargoWtunit));
            $('#cargoWtUnit' + id + '_previous').val(cargoWtunit);
            $('#grossWtUnit' + id).val(cargoWtunit);
            $('#tareWt' + id).val(tare);
            if ($('#netWt').val() != '') {
                $('#netWt' + id).val(net);
                $('#netWtUnit' + id).val(cargoWtunit);
            }
            tare = tare.toString();
            net = net.toString();
            var cargoWt = $('#cargoWt' + id).val();
            var tarewtsplit = tare.split(".");
            var cargowtsplit = cargoWt.split(".");
            var netwtsplit = net.split(".");
            var tardot = 0, cargodot = 0, netdot = 0;
            if (tarewtsplit.length > 1) {
                tardot = tarewtsplit[1].length;
            }
            if (netwtsplit.length > 1) {
                netdot = netwtsplit[1].length;
            }

            if (cargowtsplit.length > 1) {
                cargodot = cargowtsplit[1].length;
            }
            if (cargodot < tardot) {
                cargodot = tardot;
            }
            if (cargodot < netdot) {
                cargodot = netdot;
            }

            var grossWt = (parseFloat(tare) + parseFloat(cargoWt)).toFixed(cargodot);
            $('#grossWt' + id).val(grossWt);

            if ($('#cargoWt' + id).val() == "" || $('#cargoWt' + id).val().length == 0) {
                $('#grossWt' + id).val("");
            }
            if ($('#soc' + id).prop('checked') == true) {
                $('#tareWt' + id).prop('readonly', false);
            }
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });
}
function getTareweightAmend(value, id) {
    var eqptype = $('#eqpType' + id).val();
    //$('#containerNo' + id).val("");  
    //var containerno=$('#containerNo'+id).val();
    console.log("eqptype---=-=-=-=-> " + eqptype);
    if (value != "") {
        $.ajax({
            type: "POST",
            cache: false,
            url: "PortListAjax?type=getTareweight&eqptype=" + eqptype + "&equipdesc=" + value,
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                console.log('data ----> ' + data)
                var str = data;
                //$('#tareWt' + id).attr("readonly", true);
                var cargoWtunit = $('#cargoWtUnit' + id).val();
                var carwtunitconk = $('#cargoWtUnit' + id + '_previous').val();
                //document.getElementById('grossWt'+id).value="";
                //document.getElementById('cargoWt'+id).value="";
                var tarek = convertWeightUnit(str, 'K', cargoWtunit);
                if (carwtunitconk == 'K') {
                    var netk = convertWeightUnit($('#netWt' + id).val(), carwtunitconk, 'P');
                } else if (carwtunitconk == 'P')
                {
                    var netk = convertWeightUnit($('#netWt' + id).val(), carwtunitconk, 'K');

                }

                $('#cargoWt' + id).val(convertWeightUnit($('#cargoWt' + id).val(), $('#cargoWtUnit' + id + '_previous').val(), cargoWtunit));
                $('#cargoWtUnit' + id + '_previous').val(cargoWtunit);
                $('#grossWtUnit' + id).val(cargoWtunit);
                $('#tareWt' + id).val(tarek);
                if ($('#netWt').val() != '') {
                    $('#netWt' + id).val(netk);
                    $('#netWtUnit' + id).val(cargoWtunit);
                }
                var cargoWtk = $('#cargoWt' + id).val();
                var tarewtsplit = tarek.split(".");
                var cargowtsplit = cargoWtk.split(".");
                var netwtsplit = netk.split(".");
                var tardot = 0, cargodot = 0, netdot = 0;
                if (tarewtsplit.length > 1) {
                    tardot = tarewtsplit[1].length;
                }
                if (netwtsplit.length > 1) {
                    netdot = netwtsplit[1].length;
                }
                if (cargowtsplit.length > 1) {
                    cargodot = cargowtsplit[1].length;
                }
                if (cargodot < tardot) {
                    cargodot = tardot;
                }
                if (cargodot < netdot) {
                    cargodot = netdot;
                }

                var grossWtk = (parseFloat(tarek) + parseFloat(cargoWtk)).toFixed(cargodot);
                $('#grossWt' + id).val(grossWtk);

                if ($('#cargoWt' + id).val() == "" || $('#cargoWt' + id).val().length == 0) {
                    $('#grossWt' + id).val("");
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
function stopRKey(evt) {
    var evt = (evt) ? evt : ((event) ? event : null);
    var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
    if ((evt.keyCode == 13) && (node.type == "text")) {
        return false;
    }
}
function cargoselectmethod(obj, rowid) {

    var refDiv = "#refDiv" + rowid;
    var oogDiv = "#oogDiv" + rowid;
    var refclass = ".refclass" + rowid;
    var oogclass = ".oogclass" + rowid;
    var hzdDiv = "#hzdDiv" + rowid;
    var hzd = "#hzd" + rowid;
    var presetTemp = "#presetTemp" + rowid;
    var minTemp = "#minTemp" + rowid;
    var maxTemp = "#maxTemp" + rowid;
    var tempUnit = "#tempUnit" + rowid;
    var nor = "#nor" + rowid;
    var owLeft = "#owLeft" + rowid;
    var dimUnit = "#dimUnit" + rowid;
    var owRight = "#owRight" + rowid;
    var oh = "#oh" + rowid;
    var olAft = "#olAft" + rowid;
    var olForward = "#olForward" + rowid;
    var humidityflag = "#humidityflag" + rowid;
    var humidity = "#humidity" + rowid;
    var drains = "#drains" + rowid;
    var co2 = "#co2" + rowid;
    var o2 = "#o2" + rowid;
    var ventilation = "#ventilation" + rowid;
    var norid = "nor" + rowid;
    if (obj.value == 'O' || obj == 'O')
    {
        $('#tareWt' + rowid).val("");
        $('#cargoWt' + rowid).val("");
        $('#netWt' + rowid).val("");
        $('#grossWt' + rowid).val("0");
        $('#hidden_oog' + rowid).val("N");
        if ($('#OOG_Plugin').val() == 'N') {
            $('#hidden_oog' + rowid).val("Y");
        }
        $('#hidden_rfr' + rowid).val("N");
        $(oogclass).removeClass('required');
        if ($('#OOG_Plugin').val() == 'N') {
            $(oogclass).addClass('required');
        }
        $(refclass).removeClass('required');
        $(owLeft + "," + owRight + "," + oh + "," + olAft + "," + olForward).val("");
        $(presetTemp + "," + minTemp + "," + maxTemp + "," + tempUnit + "," + humidity + "," + drains + "," + co2 + "," + o2 + "," + ventilation).val("");
        $(presetTemp + "," + minTemp + "," + maxTemp + "," + tempUnit).attr("disabled", false);
        $(humidityflag).prop("checked", false).trigger("onchange");
        document.getElementById("tempUnit" + rowid).selectedIndex = 0;
        document.getElementById("nor" + rowid).value = "N";
        $(hzd).attr('disabled', false);
        $(oogDiv).hide();
        if ($('#OOG_Plugin').val() == 'N') {
            $(oogDiv).show();
        } else {
            $('#oogflag' + rowid).attr('disabled', false).prop('checked', false);
        }
        $(refDiv).hide();
    } else if (obj.value == 'R' || obj == 'R') {
        $('#tareWt' + rowid).val("");
        $('#cargoWt' + rowid).val("");
        $('#netWt' + rowid).val("");
        $('#grossWt' + rowid).val("0");
        $('#hidden_oog' + rowid).val("N");
        $('#hidden_rfr' + rowid).val("Y");
        $(presetTemp + "," + minTemp + "," + maxTemp + "," + tempUnit + "," + humidity + "," + drains + "," + co2 + "," + o2 + "," + ventilation).val("");
        $(presetTemp + "," + minTemp + "," + maxTemp + "," + tempUnit + "," + drains + "," + co2 + "," + o2 + "," + humidityflag + "," + ventilation).attr("disabled", false);
        $(humidityflag).prop("checked", false).trigger("onchange");
        $(owLeft + "," + owRight + "," + oh + "," + olAft + "," + olForward).val("");
        document.getElementById("dimUnit" + rowid).selectedIndex = 0;
        document.getElementById("tempUnit" + rowid).selectedIndex = 0;
        if ((obj.value == 'R' || obj == 'R') && (document.getElementById(norid).value == "Y")) {
            $(hzd).attr('disabled', true);
            $(hzd).prop('checked', false);
            $(hzdDiv).hide();
        }
        $(refclass).addClass('required');
        $(oogclass).removeClass('required');
        $(refDiv).show();
        $(oogDiv).hide();
        $('#oogflag' + rowid).attr('disabled', true).prop('checked', false);

    } else if (obj.value == 'D' || obj == 'D')
    {
        $('#tareWt' + rowid).val("");
        $('#cargoWt' + rowid).val("");
        $('#netWt' + rowid).val("");
        $('#grossWt' + rowid).val("0");
        $('#hidden_oog' + rowid).val("N");
        $('#hidden_rfr' + rowid).val("N");
        $(presetTemp + "," + minTemp + "," + maxTemp + "," + tempUnit + "," + humidity + "," + drains + "," + co2 + "," + o2 + "," + ventilation).val("");
        $(humidityflag).prop("checked", false).trigger("onchange");
        document.getElementById("tempUnit" + rowid).selectedIndex = 0;
        $(owLeft + "," + owRight + "," + oh + "," + olAft + "," + olForward).val("");
        document.getElementById("dimUnit" + rowid).selectedIndex = 0;
        $(refclass).removeClass('required');
        $(oogclass).removeClass('required');
        $(hzd).attr('disabled', false);
        $(refDiv).hide();
        $(oogDiv).hide();
        $('#oogflag' + rowid).attr('disabled', true).prop('checked', false);
    }
}
function setTempSetEnble(rowid)
{
    var norid = "nor" + rowid;
    var presetTemp = "#presetTemp" + rowid;
    var minTemp = "#minTemp" + rowid;
    var maxTemp = "#maxTemp" + rowid;
    var tempUnit = "#tempUnit" + rowid;
    var humidityflag = "#humidityflag" + rowid;
    var humidity = "#humidity" + rowid;
    var drains = "#drains" + rowid;
    var co2 = "#co2" + rowid;
    var o2 = "#o2" + rowid;
    var ventilation = "#ventilation" + rowid;
    var hzd = "#hzd" + rowid;
    var hzdDiv = "#hzdDiv" + rowid;

    if (document.getElementById(norid).value == "Y")
    {
        $(hzd).attr('disabled', true);
        $(hzdDiv).hide();
        $(hzd).prop('checked', false);
        $(presetTemp).attr('disabled', true);
        $(minTemp).attr('disabled', true);
        $(maxTemp).attr('disabled', true);
        $(tempUnit).attr("disabled", true);
        $(humidityflag).attr("disabled", true);
        $(humidity).attr("disabled", true);
        $(drains).attr("disabled", true);
        $(co2).attr("disabled", true);
        $(o2).attr("disabled", true);
        $(ventilation).attr("disabled", true);
        $(humidityflag).prop("checked", false).trigger("onchange");
        $(presetTemp + "," + minTemp + "," + maxTemp + "," + tempUnit + "," + humidity + "," + drains + "," + co2 + "," + o2 + "," + ventilation).val("");
        document.getElementById("tempUnit" + rowid).selectedIndex = 0;

    } else
    {
        $(humidityflag).prop("checked", false).trigger("onchange");
        $(presetTemp).attr("disabled", false);
        $(minTemp).attr("disabled", false);
        $(maxTemp).attr("disabled", false);
        $(tempUnit).attr("disabled", false);
        $(humidityflag).attr("disabled", false);
        $(drains).attr("disabled", false);
        $(co2).attr("disabled", false);
        $(o2).attr("disabled", false);
        $(ventilation).attr("disabled", false);
        $(hzd).attr('disabled', false);



    }

}

function cargoselectmethod_hzd(obj, rowid) {
    var hzdDiv = "#hzdDiv" + rowid;
    var hzdclass = ".hzdclass" + rowid;

    if ($(obj).prop('checked') == true) {
        $(obj).val('Y');
        $(hzdDiv).show();
        $(hzdclass).addClass('required');
        $('#hscode' + rowid).addClass('required');
        
        // Reset first DG entry with _1 suffix
        var flashUnitElem = document.getElementById("flashUnit" + rowid + "_1");
        if (flashUnitElem) {
            flashUnitElem.selectedIndex = 0;
        }
    } else {
        $(obj).val('N');
        $(hzdclass).removeClass('required');
        $('#hscode' + rowid).removeClass('required');
        
        // Clear all DG entries (supports multiple DG)
        clearAllDGEntries(rowid);
        //$("#otherhscode" + rowid).removeClass('required');
        if ($('#hscode' + rowid).hasClass('hsrequired')) {
            $('#hscode' + rowid).addClass('required');
            /**if (document.getElementById('othersCmdty' + rowid).checked == false) {
             
             }**/
        }
        /**if ($('#othersCmdty' + rowid).hasClass('hsrequired')) {
         if (document.getElementById('othersCmdty' + rowid).checked == true) {
         $("#otherhscode" + rowid).addClass('required');
         }
         }**/
        $(hzdDiv).hide();
    }
}
function deletecargo(tableID, obj) {
    console.log("cargo count =========> " + $('#datatablebycargodiv').find('.preview').length);
    if ($('#datatablebycargodiv').find('.preview').length > 1) {
        $(obj).parents('.preview').remove();
        var rwid = $(obj).parents('.preview').find('.rowidchange').html();
        removeHaulageDetails(rwid, tableID, obj);
    } else {
        alert(addAtleastOneCargo);
    }
    var rowval = 1;
    var rowid = 0;
//    var rowCount = $('#' + tableID + ' tr').length;
//    var i = obj.parentNode.parentNode.rowIndex;
//    if (rowCount > 2)
//    {
//        document.getElementById(tableID).deleteRow(i);
//        removeHaulageDetails(i, tableID, obj);
//
//    } else {
//        alert("Add atleast one cargo details");
//    }
    $('.rowidchange').each(function () {
        //alert(rowval)
        rowid = $(this).attr('id');
        $('#' + rowid).html(rowval);
        rowval++;
    });
}
function checkcommodity(rowid, commodityothers, id)
{
    var commodityclass = ".commodityclass" + rowid;
    var comid = "commodity" + rowid;
    var hiddid = "hiddencomid" + rowid;
    var hscodeclass = ".hscodeclass" + rowid;
    var cmdval = $('#hzd' + rowid).val();

    if (document.getElementById(commodityothers).checked == true)
    {
        $('#othercommodity' + rowid).css("display", "block");
        $(commodityclass).removeClass('commoditytypehead');
        $(commodityclass).removeClass('required');
        $('#othercommodity' + rowid).addClass('required');
        $(commodityclass).typeahead('destroy');
        $('#commodity' + rowid).val('');
        $('#commodityothers' + rowid).val('Y');
        $('#commodity' + rowid).css("display", "none");
        $('#othercommodity' + rowid).attr('maxLength', '100');
        $('#commodity' + rowid + '_port').css("display", "none");
        $('#otherhscode' + rowid).css("display", "block");
        $(hscodeclass).removeClass('hscodetypehead');
        $(hscodeclass).typeahead('destroy');
        $('#hscode' + rowid).val('');
        $('#otherhscode' + rowid).val('');
        $('#hscode' + rowid).css("display", "none");
        $('#otherhscode' + rowid).attr('maxLength', '100');
        if (cmdval == 'Y')
        {
            $('#hscode' + rowid).removeClass('required');
            $('#otherhscode' + rowid).addClass('required');
        } else
        {
            $('#hscode' + rowid).removeClass('required');
            $('#otherhscode' + rowid).removeClass('required');
        }

    }
    if (document.getElementById(commodityothers).checked == false)
    {
        $(commodityclass).typeahead('destroy');
        $(commodityclass).addClass('commoditytypehead');
        $('#commodityothers' + rowid).val('N');
        $('#commodity' + rowid).css("display", "block");
        $('#commodity' + rowid).css("background-color", "white");
        $('#othercommodity' + rowid).css("display", "none");
        $(commodityclass).addClass('required');
        $('#othercommodity' + rowid).removeClass('required');
        $('#othercommodity' + rowid).removeAttr('maxlength');
        $('#othercommodity' + rowid).val('');
        $('#hscode' + rowid).val('');
        $('#otherhscode' + rowid).val('');
        $(hscodeclass).typeahead('destroy');
        $(hscodeclass).addClass('commoditytypehead');
        $('#hscode' + rowid).css("display", "block");
        $('#hscode' + rowid).css("background-color", "white");
        $('#otherhscode' + rowid).css("display", "none");
        if (cmdval == 'Y')
        {
            $('#hscode' + rowid).addClass('required');
            $('#otherhscode' + rowid).removeClass('required');
        } else
        {
            $('#hscode' + rowid).removeClass('required');
            $('#otherhscode' + rowid).removeClass('required');
        }
        commodityload(comid, hiddid);
    }
}

function equipchange(rowid, euip)
{
   $('#cargoWt' + rowid + '-error').css("display", "none");
    $('#netWt' + rowid + "-error").css("display", "none");
    $('#volume' + rowid + "-error").css("display", "none");
    var eid = $('#eqpStatus' + rowid).val();
    cargoweightcheck = ".cargoweightcheck" + rowid;
    netweightcheck = ".netweightcheck" + rowid;
    volumeweightcheck = ".volumeweightcheck" + rowid;
    if (eid == 'F') {
        console.log('F -laden');
        $(cargoweightcheck).removeClass('valuezero');
        $(volumeweightcheck).removeClass('validatezeropoint');
        $(netweightcheck).removeClass('validatezeropoint');
        $(cargoweightcheck).addClass('validatepoint');
        $(volumeweightcheck).addClass('validatepoint');
        $(netweightcheck).addClass('validatepoint');
    }
    if (eid == 'M') {
        console.log('M - empty');
        $(cargoweightcheck).removeClass('validatepoint');
        $(volumeweightcheck).removeClass('validatepoint');
        $(netweightcheck).removeClass('validatepoint');
        $(cargoweightcheck).addClass('valuezero');
        $(volumeweightcheck).addClass('validatezeropoint');
        $(netweightcheck).addClass('validatezeropoint');
    }
}

function cargoweightcalc(id)
{
    $('#cargoWt' + id + '-error').css("display", "none");
    var cargoWtUnit = $('#cargoWtUnit' + id).val();
    var eqptype = $('#eqpType' + id).val();
    var eqpsize = $('#eqpSize' + id).val();
    var cargoWt = $('#cargoWt' + id).val();
    if ((eqpsize != "" && cargoWt != "") && $('#tareWt' + id).val() != "")
    {
        var tareWt = $('#tareWt' + id).val();
        var cargoWt = $('#cargoWt' + id).val();
        var tarewtsplit = tareWt.split(".");
        var cargowtsplit = cargoWt.split(".");
        var tardot = 0, cargodot = 0;
        if (tarewtsplit.length > 1) {
            tardot = tarewtsplit[1].length;
        }
        if (cargowtsplit.length > 1) {
            cargodot = cargowtsplit[1].length;
        }
        if (cargodot < tardot) {
            cargodot = tardot;
        }
        var grossWt = (parseFloat(tareWt) + parseFloat(cargoWt)).toFixed(cargodot);
        document.getElementById('grossWt' + id).value = grossWt;

        if ($('#cargoWt' + id).val() == "" || $('#cargoWt' + id).val().length == 0) {
            $('#grossWt' + id).val("");
        }

    }
}

function changeCargoWT(id, value, type) {

    $('#cargoWt' + id + '-error').css("display", "none");
    $('#cargoWt' + id).removeClass('grossWeightTon');
    $('#cargoWt' + id).removeClass('grossWeightKilo');
    // document.getElementById('cargoWt' + id).value = "";
    //document.getElementById('grossWt' + id).value = "";
    var cargoWtUnit = $('#cargoWtUnit' + id).val();
    var eqptype = $('#eqpType' + id).val();
    if (type == 'amen') {
        checkBkgContNo('containerNo' + id, 'soc' + id, id);
    } else {
        getTareweight($('#eqpSize' + id).val(), id);
    }

//    if($('#tareWt'+id).val()!=""){
//        if(cargoWtunit=='T'){
//            var tare=parseFloat(str)/1000;
//            $('#tareWt'+id).val(tare);
//        }else{
//            var tarek=(str);
//            $('#tareWt'+id).val(tarek);
//        }
//    }
}



function enableEqpStatus(id, obj) {
    var tarewtid = $(obj).attr('id');
    tarewtid = tarewtid.replace('soc', '');
    tarewtid = '#tareWt' + tarewtid;
    console.log('tarewtid---> ' + tarewtid);
    if ($(obj).prop('checked') == true) {
        $('#' + id).attr("disabled", false);
        $(obj).val('Y');
        $(tarewtid).attr("readonly", false);
    } else {
        $('#' + id).attr("disabled", true);
        $('#' + id).val('F').trigger('change');
        $(obj).val('N');
        $(tarewtid).attr("readonly", true);
        document.getElementById(id).selectedIndex = 0;
    }
}
//
var nVer = navigator.appVersion;
var nAgt = navigator.userAgent;
var browserName = navigator.appName;
var nameOffset, verOffset, ix;

function ValDigittemp(elementRef, val) {
    var keyCodeEntered = (elementRef.which) ? elementRef.which : elementRef.keyCode;
    if ((verOffset = nAgt.indexOf("Chrome")) != -1 || (verOffset = nAgt.indexOf("MSIE")) != -1 || (verOffset = nAgt.indexOf("Safari")) != -1)
    {
        if (keyCodeEntered == 46)
        {
            keyCodeEntered = 0;
        }
    } else
    {
        if (keyCodeEntered == 46)
        {
            keyCodeEntered = elementRef.keyCode;
        }
    }

    if (keyCodeEntered == 8 || keyCodeEntered == 9 || keyCodeEntered == 37 || keyCodeEntered == 39 || keyCodeEntered == 45 || keyCodeEntered == 46 || (keyCodeEntered >= 48) && (keyCodeEntered <= 57)) {
        return true;
    } else if (keyCodeEntered == 0) {
        if ((val.value) && (val.value.indexOf('.') >= 0))
            return false;
        else
            return true;
    }
    return false;
}

function moneyFormat(textObj) {
    var newValue = textObj.value;
    var decAmount = "";
    var dolAmount = "";
    var decFlag = false;
    var aChar = "";
    var res;
    if (newValue != '' && newValue.length > 0) {
        // ignore all but digits and decimal points.
        for (i = 0; i < newValue.length; i++) {
            aChar = newValue.substring(i, i + 1);
            if (aChar >= "0" && aChar <= "9") {
                if (decFlag) {
                    decAmount = "" + decAmount + aChar;
                } else {
                    dolAmount = "" + dolAmount + aChar;
                }
            }
            if (aChar == ".") {
                if (decFlag) {
                    dolAmount = "";
                    break;
                }
                decFlag = true;
            }
        }

        // Ensure that at least a zero appears for the dollar amount.

        if (dolAmount == "") {
            dolAmount = "0";
        }
        // Strip leading zeros.
        if (dolAmount.length > 1) {
            while (dolAmount.length > 1 && dolAmount.substring(0, 1) == "0") {
                dolAmount = dolAmount.substring(1, dolAmount.length);
            }
        }

        // Round the decimal amount.
        if (decAmount.length > 1) {

            if (decAmount.substring(1, 2) > "3") {
                decAmount = parseInt(decAmount.substring(0, 1));
                if (decAmount < 10) {
                    decAmount = decAmount;
                } else {
                    decAmount = "" + decAmount;
                }
            } else {

                decAmount = decAmount.substring(0, 1);
            }

            if (decAmount == 100) {
                decAmount = decAmount - 1;
                //    decAmount = "00";
                //  dolAmount = parseInt(dolAmount) + 1;
                dolAmount = parseInt(dolAmount);

            }

        }


        // if (dolAmount.length > 2) {
        //     if (dolAmount.substring(2, 3) > "3") {
        //         dolAmount = parseInt(dolAmount.substring(0, 2));

        //         if (dolAmount < 10) {
        //             dolAmount = "0" + dolAmount;
        //         } else {
        //             dolAmount = "" + dolAmount;
        //         }
        //     } else {
        //         dolAmount = dolAmount.substring(0, 2);
        //     }

        //     if (dolAmount == 100) {
        //         alert(dolAmount);
        //         dolAmount = dolAmount - 1;

        //     }

        // }



        // Pad right side of decAmount
        if (decAmount.length == 1) {
            decAmount = decAmount;
        }
        if (decAmount.length == 0) {
            decAmount = decAmount + "0";
        }

        // Check for negative values and reset textObj
        if (newValue.substring(0, 1) != '-' ||
                (dolAmount == "0" && decAmount == "0")) {
            textObj.value = dolAmount + "." + decAmount;

        } else {
            textObj.value = '-' + dolAmount + "." + decAmount;
        }
    }

}

function tempValidate()
{
    var tempno = document.getElementById('ebook_templateno').value;



    if (tempno == "Select")
    {
        alert(selectTemp);
        document.getElementById('ebook_templateno').focus();
        return false;
    }
    if (tempno == "")
    {
        alert(selectTemp);
        document.getElementById('ebook_templateno').focus();
        return false;
    } else if (tempno == "Select Template") {
        openLoader();
        window.location.href = "NewBooking.do";
    } else
    {
        openLoader();
        $('#action').val("template");
        $('#flag').val("T");
        $('#bookno').val(tempno);
        // callLoading();
       document.getElementById('TemplateEditForm').action = "ebooking_crud.do";
        document.getElementById('TemplateEditForm').submit();

        // window.location.href = "ebooking_crud.do?action=template&flag=T&bookno=" + tempno;
        //document.newbooking.action="ebook_populatetemp.do?flag=T&tempno="+tempno;
        //document.newbooking.submit();
    }
}

function getPort(port, setport, code, defOriDesHaulage) {
    console.log("defOriDesHaulage" + defOriDesHaulage);
    var port = trim(document.getElementById(port).value);
    var portcode = trim(port.substring(0, port.indexOf('-')));
    document.getElementById(setport).value = portcode;
    document.getElementById(code).value = portcode;
/*
    $.ajax({
        type: "POST",
        cache: false,
        url: "UserProfileAjax?type=porttype&portcode=" + portcode,
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            var str = trim(data);
            if ('ebook_origin' == code)
            {
                if ('I' == str)
                {
                    $('#preCarriage_btn > button').attr("disabled", false);
                    //     if( ($('#preCarriage').val() == 'N')||($('#preCarriage').val() == '')) {
                    $('#preCarriage').val(defOriDesHaulage);
                    if ($('#preCarriage').val() == 'C') {
                        $('#preCarriage_btn > .car').addClass("active");
                    }
                    if ($('#preCarriage').val() == 'M') {
                        $('#preCarriage_btn > .mer').addClass("active");
                    }
                    console.log("precarriage===>" + $('#preCarriage').val());
                    //  }


                } else
                {
                    $('#preCarriage_btn > button').attr("disabled", true);
                    $('#preCarriage_btn').find('button').removeClass("active");
                    $('#preCarriage').val('N');
                    $('#preCarriage_btn > .na').addClass("active");
                }
            }
            if ('ebook_delivery' == code)
            {
                if ('I' == str)
                {
                    $('#onCarriage_btn > button').attr("disabled", false);
                    //   if( ($('#onCarriage').val() == 'N')||($('#onCarriage').val() == '')) {
                    $('#onCarriage').val(defOriDesHaulage);
                    if ($('#onCarriage').val() == 'C') {
                        $('#onCarriage_btn > .car').addClass("active");
                    }
                    if ($('#onCarriage').val() == 'M') {
                        $('#onCarriage_btn > .mer').addClass("active");
                    }
                    console.log("oncarriage===>" + $('#onCarriage').val());
                    //   }
                } else
                {
                    $('#onCarriage_btn > button').attr("disabled", true);
                    $('#onCarriage_btn').find('button').removeClass("active");
                    $('#onCarriage').val('N');
                    $('#onCarriage_btn > .na').addClass("active");
                }
            }
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });
*/
    if ('ebook_origin' == code) {
        var pol = trim($('#hidRoutePOL').val());
        if (portcode !== '' && pol !== '' && portcode !== pol) {
            $('#preCarriage_btn > button').attr("disabled", false);
            $('#preCarriage').val(defOriDesHaulage);
            $('#preCarriage_btn').find('button').removeClass("active");
            if ($('#preCarriage').val() == 'C') {
                $('#preCarriage_btn > .car').addClass("active");
            } else if ($('#preCarriage').val() == 'M') {
                $('#preCarriage_btn > .mer').addClass("active");
            }
            console.log("precarriage===>" + $('#preCarriage').val());
        } else {
            $('#preCarriage_btn > button').attr("disabled", true);
            $('#preCarriage_btn').find('button').removeClass("active");
            $('#preCarriage').val('N');
        }
    }

    if ('ebook_delivery' == code) {
        var pod = trim($('#hidRoutePOD').val());
        if (portcode !== '' && pod !== '' && portcode !== pod) {
            $('#onCarriage_btn > button').attr("disabled", false);
            $('#onCarriage').val(defOriDesHaulage);
            $('#onCarriage_btn').find('button').removeClass("active");
            if ($('#onCarriage').val() == 'C') {
                $('#onCarriage_btn > .car').addClass("active");
            } else if ($('#onCarriage').val() == 'M') {
                $('#onCarriage_btn > .mer').addClass("active");
            }
            console.log("oncarriage===>" + $('#onCarriage').val());
        } else {
            $('#onCarriage_btn > button').attr("disabled", true);
            $('#onCarriage_btn').find('button').removeClass("active");
            $('#onCarriage').val('N');
        }
    }
}

function toenableshipperref(port, setport) {
    if($('#shipperReference').length > 0){
    console.log('port');
    var port = trim(document.getElementById(port).value);
    var portcode = trim(port.substring(0, port.indexOf('-')));
    document.getElementById(setport).value = portcode;
    $.ajax({
        type: "POST",
        cache: false,
        url: "UserProfileAjax?type=portcountry&portcode=" + portcode,
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            var str = trim(data);
            console.log('success');
            if ($('#shipperReference').length > 0) {
                
                if ('AE' == str) {
                    console.log('ae');
                    $('#shipperReference').attr("disabled", false);
                    $('#shipperReference').attr("required", true);
                     $('#shipperReferenceType').prop('disabled', false);
                     $('#shipperReferenceType').attr("required", true);

                    var dpwShipperRefLabel = $('#DPWShipperRef');
                    var currentText = dpwShipperRefLabel.text();
                    if (dpwShipperRefLabel.length) {
                        dpwShipperRefLabel.html(currentText + ' <span class="mandatory">*</span>');
                        console.log('Label updated with *');
                    }

                } else {
                    $('#shipperReference').val('');
                    $('#shipperReference').attr("disabled", true);
                    $('#shipperReference').attr("required", false);
                    $('#shipperReferenceType').prop('disabled', true);
                    $('#shipperReferenceType').attr("required", false);
                    $('#shipperReferenceType').val('');
                    console.log('else AE');

                    var dpwShipperRefLabel = $('#DPWShipperRef');
                    if (dpwShipperRefLabel.length) {
                        var updatedText = dpwShipperRefLabel.html().replace(' <span class="mandatory">*</span>', '');
                        dpwShipperRefLabel.html(updatedText);
                        console.log('Asterisk removed from label');
                    }
                }
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

function getEmptyPickUppoint(portcode) {
    var port = document.getElementById(portcode).value;
    getEmptyPickUp(port);
}
function getEmptyPickUp(port) {
    var pointtype = $('#ebook_pickup_point').val();
    if (port != null && port.length > 0 && pointtype != 'undefined' && pointtype != undefined) {
        var str = port.split("-");
        port = trim(str[0]);
        $.ajax({
            type: "POST",
            cache: false,
            url: "ecomAjax?type=emptyPickUp&port=" + encodeURIComponent(port) + "&pointtype=" + encodeURIComponent(pointtype),
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                //alert(data);
                if (data != null && data.length > 0) {
                    var str = data.split("~~");
                    document.getElementById('emptyPickup').options.length = str.length - 1;
                    var str1;
                    document.getElementById('emptyPickup').options[0] = new Option("Select Empty pick up", "");
                    for (var i = 0; i < str.length; i++) {
                        //alert(str[i]);
                        str1 = str[i].split("##");
                        //alert(str1[1]);
                        document.getElementById('emptyPickup').options[i + 1] = new Option(str1[1], str1[0]);
                    }
                } else
                {
                    document.getElementById('emptyPickup').options.length = 0;
                    document.getElementById('emptyPickup').options[0] = new Option("Select Empty pick up", "");
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
function setEmptyValForEmptypickup() {
    document.getElementById('emptyPickup').options.length = 0;
    document.getElementById('emptyPickup').options[0] = new Option("Select Empty pick up", "");
}

function loadEmptypickup(loadport) {
    if (loadport != null && loadport.length > 0) {
        var str = loadport.split("-");
        getEmptyPickUp(loadport);
        if ($('#emptyPickupHid').val() != null && $('#emptyPickupHid').val().length > 0) {
            $('#emptyPickup').val($('#emptyPickupHid').val());
            onChangeEmptyPickup($('#emptyPickup').val());
        }
    }
}
function onChangeEmptyPickup(val) {
    if (val != null && val != "" && val.length > 0) {
        $('#emptyPickUpDate').attr("disabled", false);
        $('#emptyPickUpDate').addClass("required");
    } else {
        $('#emptyPickUpDate').attr("disabled", true);
        $('#emptyPickUpDate').val("");
        $('#emptyPickUpDate').removeClass("required");
    }
}

function checkToOrder(custType, obj) {
    if (custType == 'consignee') {
        $('#consigneeSelect,#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity_desc,#ebook_consigneestate').val('');
        $('#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneecode,#ebook_consigneecountry,#ebook_consigneestate').val('');
        $('#ebook_consigneename-error,#ebook_consigneeaddress-error,#ebook_consigneeaddress1-error,#ebook_consigneecity_desc-error,#ebook_consigneestate-error').hide();
        $('#ebook_consigneecountry_name-error,#ebook_consigneephone-error,#ebook_consigneefax-error,#ebook_consigneeemail-error').hide();
        if (obj.checked == true) {
            $('#ebook_consigneename').attr("disabled", false);
            $('#ebook_consigneename').attr("readonly", false);
            //$('#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity_desc').attr("readonly",true);
            //$('#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#consigneesearch').attr("readonly",true);
            $("#consigneeSelect option:selected").text("Select Customer");
            //$('#ebook_consigneecountry_name').css("background-color","#eee");
            $('#hidCNToOrder').val("Y");
            $('#consigneeSelect,#consigneesearch').attr("disabled", true);
            $('#notifyconsig,#notifyconsig1').attr("disabled", true);
            loadStateAutoComplete('ebook_consigneecountry_name', 'ebook_consigneestate', 'typeheadhidden9', 'load');

        } else {
            $('#ebook_consigneename,#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("disabled", false);
            $('#consigneeSelect,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity_desc,#consigneesearch').attr("disabled", false);
            $('#ebook_consigneename,#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", false);
            $('#consigneeSelect,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity_desc,#consigneesearch').attr("readonly", false);
            $('#hidCNToOrder').val("N");
            $('#ebook_consigneecountry_name').css("background-color", "white");
            $('#notifyconsig,#notifyconsig1').attr("disabled", false);
            loadStateAutoComplete('ebook_consigneecountry_name', 'ebook_consigneestate', 'typeheadhidden9', 'load');
        }
    }
}
function setSameAndToOrder(custType) {
    if (custType == 'notify') {
        if ($('#ebook_consigneename').val() != '') {
            checkToOrder(custType, document.getElementById('consigneetoorder'));
            $('#ebook_notifyaddress,#ebook_notifyaddress1,#ebook_notifycity_desc').attr("readonly", true);
            $('#ebook_notifycountry_name,#ebook_notifyphone,#ebook_notifyfax,#ebook_notifyemail,#notifysearch').attr("readonly", true);
            $('#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity_desc').attr("readonly", true);
            $('#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#consigneesearch').attr("readonly", true);
            if($('#ebook_consigneezipcode').length > 0){
                $('#ebook_consigneezipcode').attr("readonly", true);
                $('#ebook_notifyzipcode').attr("readonly", true);
            }
            $("#consigneeSelect option:selected").text("Select Customer");
            $('#ebook_consigneecountry_name').css("background-color", "#eee");
            $('#ebook_notifycountry_name').css("background-color", "#eee");
            $('#consigneeSelect,#consigneesearch').attr("disabled", true);
            $('#consigneetoorder').attr("disabled", true);
            $('#ebook_notifyname').val($('#ebook_consigneename').val());
            $('#ebook_notifycountry').val($('#ebook_consigneecountry').val());
            $('#ebook_notifycity').val($('#ebook_consigneecity').val());
            $('#ebook_notifycity_hdn').val($('#ebook_consigneecity_hdn').val());
            $('#ebook_notifyaddress').val($('#ebook_consigneeaddress').val());
            $('#ebook_notifyaddress1').val($('#ebook_consigneeaddress1').val());
            $('#ebook_notifyphone').val($('#ebook_consigneephone').val());
            $('#ebook_notifyfax').val($('#ebook_consigneefax').val());
            $('#ebook_notifyemail').val($('#ebook_consigneeemail').val());
            $('#ebook_notifycity_desc').val($('#ebook_consigneecity_desc').val());
            $('#ebook_notifycountry_name').val($('#ebook_consigneecountry_name').val());
            if($('#ebook_consigneezipcode').length > 0){
                $('#ebook_notifyzipcode').val($('#ebook_consigneezipcode').val());
            }
            if($('#ebook_consigneepersontype').length > 0){
                $('#ebook_notifypersontype').val($('#ebook_consigneepersontype').val());
            }
            $('#ebook_notifycode').val("");
            $('#ebook_notifyname,#ebook_consigneename').attr("readonly", true);
            $('#notifyCheck').val("Y");
            $('#ebook_consigneecountry_name').trigger('change');
            $('#ebook_notifystate').val($('#ebook_consigneestate').val());
            $('#ebook_notifystate').typeahead('val', $('#ebook_consigneestate').val());
        } else {
            alert(consDet);
            document.getElementById('notifyconsig').checked = false;
        }
    } else if (custType == 'notify1') {
        if ($('#ebook_consigneename').val() != '') {
            checkToOrder(custType, document.getElementById('consigneetoorder'));
            $('#ebook_notifyaddress_n1,#ebook_notifyaddress1_n1,#ebook_notifycity_desc_n1').attr("readonly", true);
            $('#ebook_notifycountry_name_n1,#ebook_notifyphone_n1,#ebook_notifyfax_n1,#ebook_notifyemail_n1,#notifysearch1').attr("readonly", true);
            $('#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity_desc').attr("readonly", true);
            $('#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#consigneesearch').attr("readonly", true);
            if($('#ebook_consigneezipcode').length > 0){
                $('#ebook_consigneezipcode').attr("readonly", true);
                $('#ebook_notifyzipcode_n1').attr("readonly", true);
            }
            $("#consigneeSelect option:selected").text("Select Customer");
            $('#ebook_consigneecountry_name').css("background-color", "#eee");
            $('#consigneeSelect,#consigneesearch').attr("disabled", true);
            $('#consigneetoorder').attr("disabled", true);
            $('#notify1Check').val("Y");
            $('#ebook_notifyname_n1').val($('#ebook_consigneename').val());
            $('#ebook_notifycountry_n1').val($('#ebook_consigneecountry').val());
            $('#ebook_notifycity_desc_n1').val($('#ebook_consigneecity_desc').val());
            $('#ebook_notifycity_n1').val($('#ebook_consigneecity').val());
            $('#ebook_notifycity_hdn_n1').val($('#ebook_consigneecity_hdn').val());
            $('#ebook_notifyaddress_n1').val($('#ebook_consigneeaddress').val());
            $('#ebook_notifyaddress1_n1').val($('#ebook_consigneeaddress1').val());
            $('#ebook_notifyphone_n1').val($('#ebook_consigneephone').val());
            $('#ebook_notifyfax_n1').val($('#ebook_consigneefax').val());
            $('#ebook_notifyemail_n1').val($('#ebook_consigneeemail').val());
            $('#ebook_notifycountry_name_n1').val($('#ebook_consigneecountry_name').val());
            if($('#ebook_consigneezipcode').length > 0){
                $('#ebook_notifyzipcode_n1').val($('#ebook_consigneezipcode').val());
            }  
            if($('#ebook_consigneepersontype').length > 0){
                $('#ebook_notifypersontype_n1').val($('#ebook_consigneepersontype').val());
            }
            $('#ebook_notifycode_n1').val("");
            $('#ebook_notifycountry_name_n1').css("background-color", "#eee");
            $('#ebook_notifyname_n1,#ebook_consigneename').attr("readonly", true);
            $('#ebook_notifycountry_name_n1').trigger('change');
            $('#ebook_notifystate_n1').val($('#ebook_consigneestate').val());
            $('#ebook_notifystate_n1').typeahead('val', $('#ebook_consigneestate').val());
        } else {
            alert(consDet);
            document.getElementById('notifyconsig1').checked = false;
        }
    }
}
function setInitToOrder(custType) {
    if (custType == 'consignee') {
        $('#ebook_consigneecountry_name').typeahead('destroy');
        $('#ebook_consigneename').attr("disabled", false);
        $('#ebook_consigneename').attr("readonly", false);
        //$('#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity_desc').attr("readonly",true);
        // $('#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#consigneesearch').attr("readonly",true);
        $("#consigneeSelect option:selected").text("Select Customer");
        //$('#ebook_consigneecountry_name').css("background-color","#eee");
        $('#hidCNToOrder').val("Y");
        $('#consigneeSelect,#consigneesearch').attr("disabled", true);
        callAutocompleteintialize('#ebook_consigneecountry_name');
    } else if (custType == 'notify') {
        $('#ebook_notifycountry_name').typeahead('destroy');
        $('#ebook_notifyname').attr("disabled", false);
        $('#ebook_notifyname').attr("readonly", false);
        //$('#ebook_notifyaddress,#ebook_notifyaddress1,#ebook_notifycity_desc').attr("readonly",true);
        //$('#ebook_notifycountry_name,#ebook_notifyphone,#ebook_notifyfax,#ebook_notifyemail,#notifysearch').attr("readonly",true);
        $("#notifySelect option:selected").text("Select Customer");
        //$('#ebook_notifycountry_name').css("background-color","#eee");
        $('#hidNTToOrder').val("Y");
        $('#notifySelect,#notifysearch').attr("disabled", true);
        callAutocompleteintialize('#ebook_notifycountry_name');
    } else if (custType == 'notify1') {
        $('#ebook_notifycountry_name_n1').typeahead('destroy');
        $('#ebook_notifyname_n1').attr("disabled", false);
        $('#ebook_notifyname_n1').attr("readonly", false);
        //$('#ebook_notifyaddress_n1,#ebook_notifyaddress1_n1,#ebook_notifycity_desc_n1').attr("readonly",true);
        //$('#ebook_notifycountry_name_n1,#ebook_notifyphone_n1,#ebook_notifyfax_n1,#ebook_notifyemail_n1,#notifysearch1').attr("readonly",true);
        $("#notify1Select option:selected").text("Select Customer");
        //$('#ebook_notifycountry_name_n1').css("background-color","#eee");
        $('#hidNT1ToOrder').val("Y");
        $('#notify1Select,#notifysearch1').attr("disabled", true);
        callAutocompleteintialize('#ebook_notifycountry_name_n1');
    }
}
function callnatcodeMandatoryScript() {
    var pol = $('#ebook_origin_country').val();
    var pod = $('#ebook_delivery_country').val();
    if (pol != '' || pod != '') {
        pol = trim(pol.split("-")[0]);
        pod = trim(pod.split("-")[0]);
        $.ajax({
            type: "POST",
            cache: false,
            url: "ecomAjax?type=setfieldconfigvalidation&fieldcode=NAT&pol=" + encodeURIComponent(pol) + "&pod=" + encodeURIComponent(pod),
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                if (data != null && data == 'Yes') {
                    var natid = '#ebook_natcode';
                    //var natcodelabel = $('#natcodelabel').html();
                    //natcodelabel = natcodelabel.replace('<span class="mandatory">*</span>', "");
                    //natcodelabel = natcodelabel + "<font class='req'>*</font>";
                    //$('#natcodelabel').html(natcodelabel)
                    $(natid).addClass('required');
                } else {
                    var natid = '#ebook_natcode';
                    $(natid).removeClass('required');
                    //  var natcodelabel = $('#natcodelabel').html();
                    //natcodelabel = natcodelabel.replace('<span class="mandatory">*</span>', "");
                    //$('#natcodelabel').html(natcodelabel)
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
function callDgClassMandatoryScript()
{

    var i = 0;
    var bkgtype = document.getElementById("submit_btn").value;
    $('.checkhazardous').each(function () {
        var id = $(this).attr('id');
        id = id.replace("hzd", "");
        var hzdid = '#hzd' + id;
        if ($(hzdid).val() == 'Y') {
            i++;
        }
    });
    if (bkgtype != 'add')
    {
        var filename2 = document.getElementById("filename2").value;
        if (filename2.length > 0)
        {
            i = 0;
        }
    }
    if (i > 0)
    {
        $('#file_path2').addClass('required');
    } else
    {
        $('#file_path2').removeClass('required');
    }
}
function callHSCodeMandatoryScript() {
    var pol = $('#ebook_origin_country').val();
    var pod = $('#ebook_delivery_country').val();
    if (pol != '' || pod != '') {
        pol = trim(pol.split("-")[0]);
        pod = trim(pod.split("-")[0]);
        $.ajax({
            type: "POST",
            cache: false,
            url: "ecomAjax?type=setfieldconfigvalidation&fieldcode=HSC&pol=" + encodeURIComponent(pol) + "&pod=" + encodeURIComponent(pod),
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                console.log("hscodeconfig>>" + data)
                if (data != null && data == 'Yes') {
                    $('.hscodemandatoryclass').each(function () {
                        console.log("comming inside");
                        if ($(this).attr('id') != 'undefined' && $(this).attr('id') != undefined) {
                            var id = $(this).attr('id');
                            id = id.replace("hscode", "");
                            var hsid = '#hscode' + id;
                            var hzdid = '#hzd' + id;
                            $(hsid).removeClass('required');
                            $("#otherhscode" + id).removeClass('required');
                            $(hsid).addClass('hsrequired');
                            //$("#otherhscode" + id).addClass('hsrequired');
                            if ($(hzdid).val() == 'Y') {
                                $(hsid).addClass('required');
                                /**if (document.getElementById('othersCmdty' + id).checked == false) {
                                 $(hsid).addClass('required');
                                 } else {
                                 $("#otherhscode" + id).addClass('required');
                                 }*/
                            } else {
                                $(hsid).addClass('required');
                                /**if (document.getElementById('othersCmdty' + id).checked == false) {
                                 $(hsid).addClass('required');
                                 } else {
                                 $("#otherhscode" + id).addClass('required');
                                 }**/
                            }
                        }
                    });
                    $('.hscodelabelclass').each(function () {
                        var hscodelabel = $(this).html();
                        hscodelabel = hscodelabel.replace('<span class="mandatory">*</span>', "");
                        $(this).html(hscodelabel);
                    });
                    $('.hscodelabelclass').each(function () {
                        var hscodelabel = $(this).html();
                        hscodelabel = hscodelabel + "<font class='req'>*</font>";
                        $(this).html(hscodelabel);
                    });

                } else {
                    $('.hscodemandatoryclass').each(function () {
                        if ($(this).attr('id') != 'undefined' && $(this).attr('id') != undefined) {
                            var id = $(this).attr('id');
                            id = id.replace("hscode", "");
                            var hsid = '#hscode' + id;
                            var hzdid = '#hzd' + id;
                            $(hsid).removeClass('required');
                            $(hsid).removeClass('hsrequired');
                            //$("#otherhscode" + id).removeClass('hsrequired');
                            //$("#otherhscode" + id).removeClass('required');
                            if ($(hzdid).val() == 'Y') {
                                $(hsid).addClass('required');
                                /**if (document.getElementById('othersCmdty' + id).checked == false) {
                                 $(hsid).addClass('required');
                                 } else {
                                 $("#otherhscode" + id).addClass('required');
                                 }**/
                            }
                        }
                    });
                    $('.hscodelabelclass').each(function () {
                        var hscodelabel = $(this).html();
                        hscodelabel = hscodelabel.replace("<font class='req'>*</font>", "");
                        $(this).html(hscodelabel);
                    });
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
function callHSCodeMandatoryScriptForId(id, hzid) {
    var pol = $('#ebook_origin_country').val();
    var pod = $('#ebook_delivery_country').val();
    if (pol != '' || pod != '') {
        pol = trim(pol.split("-")[0]);
        pod = trim(pod.split("-")[0]);
        $.ajax({
            type: "POST",
            cache: false,
            url: "ecomAjax?type=setfieldconfigvalidation&fieldcode=HSC&pol=" + encodeURIComponent(pol) + "&pod=" + encodeURIComponent(pod),
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                console.log("hscodeconfig>>" + data)
                if (data != null && data == 'yes') {
                    $("#" + id).removeClass('required');
                    $("#" + id).addClass('hsrequired');
                    if ($("#" + hzid).val() == 'Y') {
                    } else {
                        $("#" + id).addClass('required');
                    }
                    $('.hscodelabelclass').each(function () {
                        var hscodelabel = $(this).html();
                        hscodelabel = hscodelabel.replace('<span class="mandatory">*</span>', "");
                        $(this).html(hscodelabel);
                    });
                    $('.hscodelabelclass').each(function () {
                        var hscodelabel = $(this).html();
                        hscodelabel = hscodelabel + "<font class='req'>*</font>";
                        $(this).html(hscodelabel);
                    });
                } else {
                    $("#" + id).removeClass('required');
                    $("#" + id).removeClass('hsrequired');
                    if ($("#" + hzid).val() == 'Y') {
                        $("#" + id).addClass('required');
                    }
                    $('.hscodelabelclass').each(function () {
                        var hscodelabel = $(this).html();
                        hscodelabel = hscodelabel.replace("<font class='req'>*</font>", "");
                        $(this).html(hscodelabel);
                    });
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

function getPODIntransit(port) {
    if (port != null && port.length > 0) {
        var str = port.split("-");
        port = trim(str[0]);
        $.ajax({
            type: "POST",
            cache: false,
            url: "ecomAjax?type=podtransit&port=" + encodeURIComponent(port),
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                //alert(data);
                if (data != null && data.length > 0) {
                    var str = data.split("~~");
                    document.getElementById('podintransit').options.length = str.length - 1;
                    var str1;
                    document.getElementById('podintransit').options[0] = new Option("Select Place of Final Receipt", "");
                    for (var i = 0; i < str.length; i++) {
                        str1 = str[i].split("##");
                        document.getElementById('podintransit').options[i + 1] = new Option(str1[1], str1[0]);
                    }
                } else {
                    if ($("#podintransit").length > 0) {
                        document.getElementById('podintransit').options.length = 0;
                        document.getElementById('podintransit').options[0] = new Option("Select Place of Final Receipt", "");
                    }
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
function setEmptyValPodIntransit() {
    document.getElementById('podintransit').options.length = 0;
    document.getElementById('podintransit').options[0] = new Option("Select Place of Final Receipt", "");
}

function loadPODIntransit(port) {
    if (port != null && port != '') {
        getPODIntransit(port);
        if ($('#hidpodintransit').val() != null && $('#hidpodintransit').val().length > 0) {
            $('#podintransit').val($('#hidpodintransit').val());
        }
    }

}

function checkBkgContNo(contId, socId, id) {

    var eqptype = $('#eqpType' + id).val();
    var eqpDesc = $('#eqpSize' + id).val();
    console.log("soc>>" + $('#' + socId).val());
    if ($('#' + contId).val() == null || $('#' + contId).val() == "" || eqpDesc == "") {
        $('#cargoWt' + id).val(convertWeightUnit($('#cargoWt' + id).val(), $('#cargoWtUnit' + id + '_previous').val(), $('#cargoWtUnit' + id).val()));
        $('#tareWt' + id).val(convertWeightUnit($('#tareWt' + id).val(), $('#cargoWtUnit' + id + '_previous').val(), $('#cargoWtUnit' + id).val()));
        $('#grossWt' + id).val(convertWeightUnit($('#grossWt' + id).val(), $('#cargoWtUnit' + id + '_previous').val(), $('#cargoWtUnit' + id).val()));
        $('#cargoWtUnit' + id + '_previous').val($('#cargoWtUnit' + id).val());
        $('#netWt' + id).val(convertWeightUnit($('#netWt' + id).val(), $('#cargoWtUnit' + id + '_previous').val(), $('#cargoWtUnit' + id).val()));

    }
    if ($('#' + contId).val() != null && $('#' + contId).val() != "" && eqpDesc != "") {
        if ($('#' + socId).val() == "Y" && ($('#' + contId).val().indexOf($('#eqpSize' + id).val() + "TBN") != -1 || $('#' + contId).val().indexOf($('#eqpSize' + id).val() + "TNB") != -1)) {
            $('#tareWt' + id).val('');
        }
        $.ajax({
            type: "POST",
            cache: false,
            url: "PortListAjax?type=getTareweightAmend&eqptype=" + eqptype + "&equipdesc=" + eqpDesc + "&containerno=" + encodeURIComponent($('#' + contId).val()) + "&soc=" + $('#' + socId).val(),
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                var str = data;
                console.log('>>tarerweith>' + data);
                if (data != null && data.length > 0) {
                    // $('#tareWt' + id).attr("readonly", true);
                    var cargoWtunit = $('#cargoWtUnit' + id).val();
                    var carwtunitcon = $('#cargoWtUnit' + id + '_previous').val();
                    var tare = convertWeightUnit(str, 'K', cargoWtunit);
                    if (carwtunitcon == 'K') {
                        var net = convertWeightUnit($('#netWt' + id).val(), carwtunitcon, 'P');
                    } else if (carwtunitcon == 'P')
                    {
                        var net = convertWeightUnit($('#netWt' + id).val(), carwtunitcon, 'K');

                    }
                    $('#cargoWt' + id).val(convertWeightUnit($('#cargoWt' + id).val(), $('#cargoWtUnit' + id + '_previous').val(), cargoWtunit));
                    $('#cargoWtUnit' + id + '_previous').val(cargoWtunit);
                    $('#grossWtUnit' + id).val(cargoWtunit);
                    $('#tareWt' + id).val(tare);
                    if ($('#netWt').val() != '') {
                        $('#netWt' + id).val(net);
                        $('#netWtUnit' + id).val(cargoWtunit);
                    }
                    tare = tare.toString();
                    net = net.toString();
                    var cargoWt = $('#cargoWt' + id).val();
                    var tarewtsplit = tare.split(".");
                    var cargowtsplit = cargoWt.split(".");
                    var netwtsplit = net.split(".");
                    var tardot = 0, cargodot = 0, netdot = 0;
                    if (tarewtsplit.length > 1) {
                        tardot = tarewtsplit[1].length;
                    }
                    if (netwtsplit.length > 1) {
                        netdot = netwtsplit[1].length;
                    }
                    if (cargowtsplit.length > 1) {
                        cargodot = cargowtsplit[1].length;
                    }
                    if (cargodot < tardot) {
                        cargodot = tardot;
                    }
                    if (cargodot < netdot) {
                        cargodot = netdot;
                    }
                    var grossWt = (parseFloat(tare) + parseFloat(cargoWt)).toFixed(cargodot);
                    $('#grossWt' + id).val(grossWt);

                    if ($('#cargoWt' + id).val() == "" || $('#cargoWt' + id).val().length == 0) {
                        $('#grossWt' + id).val("");
                    }
                } else if ($('#' + socId).val() != 'Y' || ($('#' + contId).val().indexOf($('#eqpSize' + id).val() + "TBN") != -1 || $('#' + contId).val().indexOf($('#eqpSize' + id).val() + "TNB") != -1)) {
                    getTareweight($('#eqpSize' + id).val(), id);
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
function clearContainerInput(contId, socId, id)
{
    console.log("clearContainerInput ====> ");
    $('#' + contId).val('');
    $('#' + contId).focus();
    console.log($('#soc' + id).prop('checked'));
    var isdummyid = contId.replace('containerNo', '');
    isdummyid = 'isdummy' + isdummyid;
    if ($('#soc' + id).prop('checked') == false) {
        var dumeqpid = $('#eqpSize' + id).val() + 'TNB';
        var cargocount = $('#datatablebycargodiv').find('.eqpsize').length;
        cargocount = cargocount + 2;
        console.log("cargocount==> " + cargocount);
        var cargohash = new Array(cargocount);
        cargohash.fill(0);
        var eqpcount = 0;
        $('#datatablebycargodiv').find('.eqpsize').each(function () {
            if ($(this).val() == $('#eqpSize' + id).val()) {
                //eqpcount++;
                var cnno = $(this).parents('.preview').find('.containerclass').val();
                // console.log("cnno===> " + cnno);
                //  console.log(cnno.includes($(this).val() + "TNB"));
                if (cnno.includes($(this).val() + "TNB")) {
                    var indexno = cnno.replace($(this).val() + 'TNB', '');
                    //    console.log("indexno===> " + indexno);
                    cargohash[indexno] = cargohash[indexno] + 1;
                    //        console.log("cargohash===> " + cargohash);
                }
            }
        });
        //eqpcount++;
        var i = 1;
        for (i = 1; i < cargohash.length; i++) {
            console.log(i + ' ==> ' + cargohash[i]);
            //console.log(cargohash[i] == 0);
            // console.log(cargohash[i] == '0');
            if (cargohash[i] == 0) {
                eqpcount = i;
                console.log('break ==> ' + i);
                break;
            }
        }
        //console.log("eqpcount==> " + eqpcount);
        //console.log("dumeqpid===> " + dumeqpid);
        dumeqpid = dumeqpid + (eqpcount);
        console.log("dumeqpid===> " + dumeqpid);
        $('#' + contId).val(dumeqpid);
        $('#' + isdummyid).val('Y');
    }
}
function checkContainerValidate(contId, socId, id)
{
    var flag = true;
    var eqpSize1;
    var containerno = $('#containerNo' + id).val();
    var eqpSize = $('#eqpSize' + id).val();
    var eqpSizeValue = $('#eqpSize' + id).val();
    var soc = $('#soc' + id).val();
    console.log("containerno>>" + containerno);
    if ($('#' + contId).val() != null && $('#' + contId).val() != "") {
        eqpSize1 = eqpSize + "TBN";
        eqpSize = eqpSize + "TNB";
        console.log("eqpSize>>" + eqpSize)
        console.log("eqpSize1>>" + eqpSize1);
        var n = containerno.indexOf(eqpSize);
        var n1 = containerno.indexOf(eqpSize1);
        console.log("n<<<" + n);
        console.log("n<<<" + n);
        if (n != -1 || n1 != -1)
        {
            console.log(false);
            flag = false;
        }
        if (flag)
        {
            $.ajax({
                type: "POST",
                cache: false,
                url: "UserProfileAjax?type=containernovalidate&containerno=" + encodeURIComponent(containerno) + "&soc=" + soc + "&eqpSizeValue=" + encodeURIComponent(eqpSizeValue),
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    if (data != null && data.length > 0) {
                        if ('Invalid' == data && soc == 'N')
                        {
                            alert(validCont);
                            $('#' + contId).val('');
                            $('#' + contId).focus();
                        } else if ('starcare' === data) {
                            alert(invalidCont);
                            $('#' + contId).val('');
                            $('#' + contId).focus();
                        } else
                        {
                            $('#isdummy' + id).val("N");
                        }
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else
        {
            $('#isdummy' + id).val("Y");
        }
    }
}

//cc mail notification script
function ccmailclick() {
    if ($('#ccmailcheckbox').is(":checked")) {
        $('#ccmailidmodal,#ccaddbtn,#ccmaillist,#removebtn').attr("disabled", false);
    } else {
        $('#ccmailidmodal,#ccaddbtn,#ccmaillist,#removebtn').attr("disabled", true);
    }
}
function addccmailid() {
    if ($('#ccmailidmodal').val() != '') {
        var checkdupccmail = false;
        $("#ccmaillist option").each(function () {
            if (trim($('#ccmailidmodal').val()) == trim($(this).val())) {
                checkdupccmail = true;
            }
        });
        if (checkdupccmail) {
            alert(uniqueMail);
        } else if (!validateEmailToCommon($('#ccmailidmodal').val())) {
            alert(validMail);
        } else {
            var htmldata = $('#ccmaillist').html();
            htmldata = htmldata + '<option value="' + $('#ccmailidmodal').val() + '">' + $('#ccmailidmodal').val() + '</option>';
            $('#ccmaillist').html(htmldata);
            $('#ccmailidmodal').val("");
        }
    } else {
        alert(entCCMailId);
    }
}
function removeCCMailId() {
    $("#ccmaillist option[value='" + $('#ccmaillist :selected').val() + "']").remove();
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
function validateDupMailId(val) {
    var checkDup = false;
    var mailArr = val.split(",");
    for (var i = 0; i < mailArr.length; i++) {
        for (var j = i + 1; j < mailArr.length; j++) {
            if (trim(mailArr[i]) == trim(mailArr[j])) {
                checkDup = true;
                break;
            }
        }
    }
    return checkDup;
}

function callCommoditySearch(comId, hsCodeId, rowid) {
    $.ajax({
        type: "POST",
        cache: false,
        url: "BookingAjax?type=commoditysearchjsp&comId=" + encodeURIComponent(comId) + "&hsCodeId=" + encodeURIComponent(hsCodeId) + "&rowid=" + rowid + "&screenname=BKG",
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            $("#comdtySearchModal .modal-body").html(data);
            $('#comdtySearchModal').modal('show');
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });

}

function setnamedisable() {
    if ($('#ebook_agreecode').val() != null && $('#ebook_agreecode').val() != '') {
        $('#ebook_agreename').attr("readonly", true);
    }
    if ($('#ebook_shippercode').val() != null && $('#ebook_shippercode').val() != '') {
        $('#ebook_shippername').attr("readonly", true);
    }
    if ($('#ebook_consigneecode').val() != null && $('#ebook_consigneecode').val() != '') {
        $('#ebook_consigneename').attr("readonly", true);
    }
    if ($('#ebook_notifycode').val() != null && $('#ebook_notifycode').val() != '') {
        $('#ebook_notifyname').attr("readonly", true);
    }
    if ($('#ebook_notifycode_n1').val() != null && $('#ebook_notifycode_n1').val() != '') {
        $('#ebook_notifyname_n1').attr("readonly", true);
    }
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
    $('#ccMailId').val(email);
}

function setEqpNo(val, con_no_id, rowid) {
    var eqpSize;
    var eqpSize1;
    var containerno;
    var isdummyid = con_no_id.replace('containerNo', '');
    isdummyid = 'isdummy' + isdummyid;
    if (val != null && val != "") {
        eqpSize = "TNB";
        eqpSize1 = "TBN";
        var n = $('#' + con_no_id).val().indexOf(eqpSize);
        var n1 = $('#' + con_no_id).val().indexOf(eqpSize1);
        if (n != -1 || n1 != -1 || $('#' + con_no_id).val() == null || $('#' + con_no_id).val() == '' || $('#' + con_no_id).val().length == 0)
        {
//   containerno = val + $('#' + con_no_id).val().substring(4, 7) + rowid;
//   $('#' + con_no_id).val(containerno);
//  } else {
// $('#' + con_no_id).val("");
            var dumeqpid = val + 'TNB';
            var cargocount = $('#datatablebycargodiv').find('.eqpsize').length;
            cargocount = cargocount + 2;
            console.log("cargocount==> " + cargocount);
            var cargohash = new Array(cargocount);
            cargohash.fill(0);
            var eqpcount = 0;
            $('#datatablebycargodiv').find('.eqpsize').each(function () {
                if ($(this).val() == val) {
//eqpcount++;
                    var cnno = $(this).parents('.preview').find('.containerclass').val();
                    //  console.log("cnno===> " + cnno);
                    //  console.log(cnno.includes($(this).val() + "TNB"));
                    if (cnno.includes($(this).val() + "TNB")) {
                        var indexno = cnno.replace($(this).val() + 'TNB', '');
                        //      console.log("indexno===> " + indexno);
                        cargohash[indexno] = cargohash[indexno] + 1;
                        //      console.log("cargohash===> " + cargohash);
                    }
                }
            });
            //eqpcount++;
            var i = 1;
            for (i = 1; i < cargohash.length; i++) {
                console.log(i + ' ==> ' + cargohash[i]);
                //   console.log(cargohash[i] == 0);
                // console.log(cargohash[i] == '0');
                if (cargohash[i] == 0 || cargohash[i] == '0') {
                    eqpcount = i;
                    console.log(+'break ==> ' + i);
                    break;
                }
            }
// console.log("eqpcount==> " + eqpcount);
//  console.log("dumeqpid===> " + dumeqpid);
            dumeqpid = dumeqpid + (eqpcount);
            console.log("dumeqpid===> " + dumeqpid);
            $('#' + con_no_id).val(dumeqpid);
            $('#' + isdummyid).val('Y');
        }
    }
}

function enableHumidity(humidityflagid, humidityid) {
    if ($("#" + humidityflagid).prop("checked") == true) {
        $("#" + humidityid).addClass('required');
        $("#" + humidityid).prop("disabled", false);
        $("#" + humidityid + "_label").html($("#" + humidityid + "_label").html() + '<span class="mandatory">*</span>');
    } else {
        $("#" + humidityid).val("");
        $("#" + humidityid).removeClass('required');
        $("#" + humidityid + " .error").remove();
        $("#" + humidityid).prop("disabled", true);
        $("#" + humidityid + "_label").html($("#" + humidityid + "_label").html().replace('<span class="mandatory">*</span>', ""));
    }
}
function validateHumidityDigit(obj) {
    var ptrn = /^[0-9.-]*$/i;
    if (obj.value.match(ptrn)) {
        return true;
    } else {
        obj.value = obj.value.substring(0, obj.value.length - 1);
        return false;
    }
}
function removeHyphen(obj) {
    if (obj.value.startsWith("-")) {
        obj.value = obj.value.substring(1, obj.value.length);
    }
    if (obj.value.endsWith("-")) {
        obj.value = obj.value.substring(0, obj.value.length - 1);
    }
}
function convertWeightUnit(value, fromUnit, toUnit) {

    if (value != '' && value.length > 0) {
        if (fromUnit == 'K') {
            if (toUnit == 'T') {
                value = parseFloat(value) / 1000.0;
            } else if (toUnit == 'P') {
                value = parseFloat(value) * 2.20462;
            }

        } else if (fromUnit == 'T') {

            if (toUnit == 'K') {
                value = parseFloat(value) * 1000.0;
            } else if (toUnit == 'P') {
                value = parseFloat(value) * 2204.6226200000001;
            }

        } else if (fromUnit == 'P') {

            if (toUnit == 'K') {
                value = parseFloat(value) / 2.20462;
            } else if (toUnit == 'T') {
                value = parseFloat(value) / 2204.6226200000001;
            }
        }
        value = parseFloat(value).toFixed(3);
    }
    return value;
}

function setEqptype(eqptypeid, eqpobj) {
    $.each(EquipmentJson, function (i, obj) {
        if ($(eqpobj).val() == obj.equipSize) {
            $('#' + eqptypeid).val(obj.equipType).trigger('onchange');
        }
    });
}
function toggleText(transcount, obj, target) {
    if ($(obj).find('i').hasClass('fa-chevron-down')) {
        $(obj).html('Hide Details <i class="fa fa-chevron-up"></span>');
        $(target).collapse("show");
    } else {
        $(obj).html(transcount + ' <i class="fa fa-chevron-down"></span>');
        $(target).collapse("hide");
    }
}

function grossweightValidation() {
    $('.grossWeightCheck').each(function () {
        var id = $(this).attr('id');
        id = id.replace("cargoWt", "");
        if ($('#cargoWtUnit' + id).val() == 'T') {
            $(this).rules('remove', 'grossWeightKilo');
            $(this).rules('remove', 'grossWeightPound');
            $(this).rules('add', {
                grossWeightTon: ["#grossWt" + id],
                messages: {
                    grossWeightTon: grossWtTon
                }
            });
        } else if ($('#cargoWtUnit' + id).val() == 'P') {
            $(this).rules('remove', 'grossWeightKilo');
            $(this).rules('remove', 'grossWeightTon');
            $(this).rules('add', {
                grossWeightPound: ["#grossWt" + id],
                messages: {
                    grossWeightPound: grossWtPounds
                }
            });
        } else {
            $(this).rules('remove', 'grossWeightTon');
            $(this).rules('remove', 'grossWeightPound');
            $(this).rules('add', {
                grossWeightKilo: ["#grossWt" + id],
                messages: {
                    grossWeightKilo: grossWtKilos
                }
            });
        }

    });

}

function bookingcustomerexpand(obj) {

    if ($(obj).is('.expanded')) {
        $(obj).removeClass('expanded').addClass('collapsed');
        $(obj).html(colcust);
        $('#collapseBookingParty,#collapseAgree,#collapseShipper,#collapseConsignee,#collapseNotify,#collapseNotify1,#collapseForwarder,#collapseWarehouse,#collapseNamedCustomer,#collapseSeller,#collapseBuyer').addClass('show');
        $('.collapseBookingParty,.collapseAgree,.collapseShipper,.collapseConsignee,.collapseNotify,.collapseNotify1,.collapseForwarder,.collapseWarehouse,.collapseNamedCustomer,.collapseSeller,.collapseBuyer').find("i").removeClass("fa-plus").removeClass("fa-minus").addClass("fa-minus");
    } else {
        $(obj).removeClass('collapsed').addClass('expanded');
        $(obj).html(expcust);
        $('#collapseBookingParty,#collapseAgree,#collapseShipper,#collapseConsignee,#collapseNotify,#collapseNotify1,#collapseForwarder,#collapseWarehouse,#collapseNamedCustomer,#collapseSeller,#collapseBuyer').removeClass('show');
        $('.collapseBookingParty,.collapseAgree,.collapseShipper,.collapseConsignee,.collapseNotify,.collapseNotify1,.collapseForwarder,.collapseWarehouse,.collapseNamedCustomer,.collapseSeller,.collapseBuyer').find("i").removeClass("fa-minus").removeClass("fa-plus").addClass("fa-plus");
    }
}
function PayLoadValidation(cargowtid, grosswtid, wtunitid, containernoid, eqptypeid) {
    var cargowt = $('#' + cargowtid).val();
    var grosswt = $('#' + grosswtid).val();
    var wtunit = $('#' + wtunitid).val();
    var containerno = $('#' + containernoid).val();
    var eqptype = $('#' + eqptypeid).val();
    var errormsg = '';
    console.log("cargowt=" + cargowt + "&grosswt=" + grosswt + "&wtunit=" + wtunit + "&containerno=" + containerno + "&eqptype=" + eqptype);
    if (grosswt.length > 0 && cargowt.length > 0) {
        $.ajax({
            type: "POST",
            cache: false,
            url: "PayloadValidationAjax?cargowt=" + cargowt + "&grosswt=" + grosswt + "&wtunit=" + wtunit + "&containerno=" + containerno + "&eqptype=" + eqptype,
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                console.log('data======> ' + data);
                if (data != null && data.length > 0) {
                    var res = data.split('~~');
                    var status = res[0];
                    if (status == 'invalid') {
                        if ("T" == wtunit) {
                            errormsg = res[1] + " weight should be less than or equal to " + res[2] + " Tons";
                            errormsg = errormsg.replace("50", res[2]);
                        } else if ("P" == wtunit) {
                            errormsg = res[1] + " weight should be less than or equal to " + res[2] + " Pounds";
                            errormsg = errormsg.replace("1,00,000", res[2]);
                        } else if ("K" == wtunit) {
                            errormsg = res[1] + " weight should be less than or equal to " + res[2] + " Kilos";
                        }
                        alert(errormsg);
                        $('#' + cargowtid).val('');
                    }
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

function  callMsdsMandatoryScript()

{

    var i = 0;
    var bkgtype = document.getElementById("submit_btn").value;
    $('.checkhazardous').each(function () {
        var id = $(this).attr('id');
        id = id.replace("hzd", "");
        var hzdid = '#hzd' + id;
        if ($(hzdid).val() == 'Y') {
            i++;
        }
    });
    if (bkgtype != 'add')
    {
        var filename4 = ($("#filename4").length > 0 ? $("#filename4").val() : "");
        if (filename4.length > 0)
        {
            i = 0;
        }
    }
    if (i > 0)
    {
        $('#file_path4').addClass('required');
    } else
    {
        $('#file_path4').removeClass('required');
    }
}

function downloadfile(filename) {
    var refno = $('#booking_refno').val();
    var type = 'Ecom';
    if ($('#lrpBookNo').length > 0 && $('#lrpBookNo').val().length > 0) {
        type = "BKG";
        refno = $('#lrpBookNo').val();
    }
    $('#BKGfilename').val(filename);
    $('#BKGtype').val(type);
    $('#BKGrefno').val(refno);
    $('#filedownloadform').attr("action", "FileAjax");
    $('#filedownloadform').submit();
}
function deletefile(filename, obj) {

    $("#deletedfilename").val($("#deletedfilename").val() + "~~" + filename);
    $(obj).parents("tr").remove();

}
function moreFile(type) {
    if ($("#" + type + "1").is(".d-none")) {
        $("#" + type + "1").removeClass("d-none");
    } else if ($("#" + type + "2").is(".d-none")) {
        $("#" + type + "2").removeClass("d-none");
    } else {
        alert("For each segment only three files can be attached!!")
    }
}
function checkSIContainerNoValidation(sicontnostrlis) {
    var resultdate;
    $.ajax({
        type: "POST",
        cache: false,
        url: "SIAjax?type=SIContainernovalidation&lrpbookno=" + $('#lrpBookNo').val() + "&containerlisstr=" + sicontnostrlis,
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            resultdate = data;
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });
    return resultdate;
}
function loadinsurancepage(quoteno, custtype) {
    var shipmentdate = $('#hidETDDate').val();
    var onlinebkgno = $('#booking_refno').val();
    var origin = $('#ebook_origin_country').val();
    var delivery = $('#ebook_delivery_country').val();
    var AP_country = $('#ebook_agreecountry_name').val();
    var vessel = $('#BKG_vessel_value').text();
    var vesselcode = $("#hidINVessel").val();
    var commodity = '';
    $("#datatablebycargodiv").find('.preview').each(function () {
        var comdhs = '';// $(this).find('.hscodetypehead:nth-child(2)').val();
        if (comdhs == '' || (typeof comdhs === 'undefined') || comdhs.length == 0) {
            comdhs = $(this).find('.commoditytypehead:nth-child(2)').val();
        }
        console.log("comdhs => " + comdhs);
        if ((typeof comdhs !== 'undefined') && comdhs.length > 0) {
            commodity = commodity + comdhs + "~~~";
        }
        console.log("commodity => " + commodity);
    });
    console.log("final => " + commodity);
    commodity = commodity.substring(0, commodity.length - 3);
    if (commodity.length > 0) {
        console.log("TEST==> " + "Insurance.jsp?shipmentdate=" + shipmentdate + "&onlinebkgno=" + onlinebkgno + "&origin=" + origin + "&delivery=" + delivery + "&AP_country=" + AP_country + "&commodity=" + commodity + "&vessel=" + vessel + "&vesselcode=" + vesselcode);
        $.ajax({
            type: "POST",
            cache: false,
            url: "Insurance.jsp?shipmentdate=" + shipmentdate + "&onlinebkgno=" + onlinebkgno + "&origin=" + origin + "&delivery=" + delivery + "&AP_country=" + AP_country + "&commodity=" + commodity + "&vessel=" + vessel + "&vesselcode=" + vesselcode + "&custtype=" + custtype + "&quoteno=" + quoteno + "&sourcetype=BKG",
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                $.fancybox.open(data, {
                    padding: 0,
                    showCloseButton: false,
                    closeClick: false,
                    maxWidth: 1200,
                    maxHeight: 600,
                    fitToView: true,
                    async: false,
                    width: '90%',
                    height: '100%',
                    autoSize: true,
                    scrollOutside: true,
                    closeBtn: true,
                    hideOnOverlayClick: false,
                    overlayShow: false,
                    helpers: {
                        overlay: {closeClick: false}
                    },
                    onClosed: function () {
                        closeLoader();
                    }

                });
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    } else {
        alert("Please enter the Commodity Details");
    }
}
var quoteno = '';
function setInsuranceDetails() {
    var insuredemail = '';
    $('#IN_Country').text($('#ebook_agreecountry_name').val());
    $('#IN_Origin').text($('#ebook_origin_country').val());
    $('#IN_Delivery').text($('#ebook_delivery_country').val());
    $('#IN_Vessel').text($('#BKG_vessel_value').text());
    $('#vesselcode').val($("#hidINVessel").val());
    $('#IN_Shipmentdate').text($('#hidETDDate').val());
    var count = 0;
    $('#comdtyblock').html('');
    var custtype = '', incargoval = '';
    var cmdtyarr = new Array();
    $("#datatablebycargodiv").find('.preview').each(function () {
        var comdhs = $(this).find('.commoditytypehead:nth-child(2)').val();
        custtype = $(this).find('.inquotecusttype').val();
        var ischecked = '';
        if ($(this).find('.inquoteno').val() != '' && $(this).find('.inquoteno').val().length > 0) {
            quoteno = $(this).find('.inquoteno').val();
            insuredemail = $(this).find('.insuredemail').val();
            ischecked = 'checked="checked"';
        }
        if ($('#dischek').val() == 'new' || $('#dischek').val() == 'template') {
            ischecked = 'checked="checked"';
        }
        incargoval = $(this).find('.incargoval').val();
        var comdtyhtml = '<div class="form-group row commoditydiv"><div class="col-sm-12"><div class="checkbox-inline col-sm-3" style="margin:7px 0px 0px -3px;"><div class="switchToggle" style="display: inline-flex;"><input type="checkbox"  id="IN_Commodity' + count + '" class="IN_Commodity" ' + ischecked + ' /><label class="tgl_round" for="IN_Commodity' + count + '"> </label><span class="toggle_label ecom_insurance_text" style="padding-left: 5px;"> ' + Ins_commodity_hscode + '</span></div></div><label class="col-sm-3 commodity_value" >' + comdhs + '</label><label class="col-sm-2 ecom_insurance_text" style="margin-left: 4.2%;max-width: 13.666667%;">' + Ins_cargo_value + '<span class="mandatory"> * </span> </label><div class="checkbox-inline scol-sm-3"><input type="text" id="IN_Cargovalue" class="IN_Cargovalue light_border" onchange="enableinsuranceswitchbtn()" value="' + incargoval + '"></div></div></div>';
        console.log("commodity => " + comdhs);
        console.log("cmdtyarr => " + cmdtyarr);
        console.log("jQuery.inArray(comdhs, cmdtyarr) => " + jQuery.inArray(comdhs, cmdtyarr));
        if (jQuery.inArray(comdhs, cmdtyarr) == -1) {
            $('#comdtyblock').append(comdtyhtml);
            count = count + 1;
        }
        cmdtyarr.push(comdhs);
    });
    if ($('#IN_Email').val().length == 0 && insuredemail.length > 0) {
        $('#IN_Email').val(insuredemail);
    }
    if ("SH" == custtype || 'sh' == custtype) {
        $('#IN_SH').prop("checked", true).trigger('onchange');

        $('#IN_PEmail').val($('#ebook_shipperemail').val());

        //enableSH();
    } else {
        $('#IN_AP').prop("checked", true).trigger('onchange');

        $('#IN_PEmail').val($('#ebook_agreeemail').val());

        //enableAP();
    }
    $('#IN_hidquoteno').val(quoteno);

    var isinsavble = true;
    $("#datatablebycargodiv").find('.preview').each(function () {
        if ('Y' == $(this).find('.inavailable').val()) {
            isinsavble = false;
            $('#isInsuranceavailable').val("Y");
        }
    });
    if (isinsavble && $('#dischek').val() != 'new' && $('#dischek').val() != 'template') {
        $('#insurance_not_need').prop('checked', true).trigger('onchange');
    }
    if (quoteno != '' && quoteno.length > 0) {
        $('#calpremiumbtn').text(re_calculate_premium);
    }
}
function loadQuoteDetails() {
    //openLoader();
    if (quoteno != '' && quoteno.length > 0 && $('#In_quoteno').length == 0) {
        loadQuoteAmountDetails(quoteno);
    }
    //closeLoader();
}
function enableMeasurement(obj, rowid) {
    var oogclass = ".oogclass" + rowid;
    var oogDiv = "#oogDiv" + rowid;
    var owLeft = "#owLeft" + rowid;
    var dimUnit = "#dimUnit" + rowid;
    var owRight = "#owRight" + rowid;
    var oh = "#oh" + rowid;
    var olAft = "#olAft" + rowid;
    var olForward = "#olForward" + rowid;

    if ($(obj).prop('checked') == true) {
        $(oogclass).addClass('required');
        $('#hidden_oog' + rowid).val("Y");
        $(oogDiv).show();
    } else {
        $(oogclass).removeClass('required');
        $('#hidden_oog' + rowid).val("N");
        $(oogDiv).hide();
        $(owLeft + "," + owRight + "," + oh + "," + olAft + "," + olForward).val("");
        $(dimUnit).val("M");
    }
}
function openOOGBlockOnLoad(oogclass, oodiv, oogchk, oogval) {
    console.log("inside openOOGBlockOnLoad")
    if ($('#OOG_Plugin').val() == 'Y') {
        if (oogval == 'y' || oogval == 'Y') {
            console.log("inside iFFFFFF")
            $(oogclass).addClass('required');
            $(oodiv).show();
            $('#' + oogchk).attr('disabled', false);
            $('#' + oogchk).prop('checked', true).trigger('change');
        }
    }
}
function loadCargoProtect(page) {

    console.log("inside loadCargoProtect");
    var etdDate = $("#hidETDDate").val();
    if ($("#hidETDDate").length > 0 && etdDate && etdDate !== 'null') {
        var deliverycountry = $('#hidRoutePOD').val();
        var origincountry = $('#hidRoutePOL').val();
        var dgFlag = getContainerDGFlag();
        console.log('origincountry booking ' + origincountry + 'deliverycountry=>' + deliverycountry);
        $.ajax({
            url: "CargoProtectAjax?type=shipmentdate&searchvalue=" + $('#hidETDDate').val() + "&origincountry=" + origincountry + "&deliverycountry=" + deliverycountry + "&dgflag=" + dgFlag,
            type: "POST",
            async: false,
            success: function (data) {
                $("#cargoprotect_table_available_div").html(data);
                $('.cargoprotect_check').each(function () {
                    if ($(this).val() == $('#temp_cargoprotectid').val()) {
                        $(this).prop('checked', true);
                    }
                });
                $("#cargoprotect_table_available_div").show();
                $("#available_tier").show();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    console.log('===>' + $("#cargoprotectid").val());
    if ($("#cargoprotectid").length > 0 && $("#cargoprotectid").val() != '' && $("#cargoprotectid").val() != 'null' && $("#cargoprotectid").val() != '0' && $("#cargoprotectid").val().length > 0) {
        $.ajax({
            url: "CargoProtectAjax?type=id&searchvalue=" + $("#cargoprotectid").val() + "&page=" + page,
            type: "POST",
            async: false,
            success: function (data) {
                console.log('data==>' + data);
                $("#cargoprotect_table_div").html(data);
                $("#cargoprotect_table_div").show();
                $("#selected_tier").show();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
}

function setCargoprotectid(obj) {
    if ($(obj).prop('checked')) {
        $('#temp_cargoprotectid').val($(obj).val());
        $('.cargoprotect_check').prop('checked', false);
        $(obj).prop('checked', true);
    } else {
        $('#temp_cargoprotectid').val('');
    }
}
function removeCargoProtect(protectid, obj) {
    if (confirm("Do you want to remove Additional Products And Charges") == true) {
        $('#cargoprotectid').val('');
        $("#cargoprotect_table_div").html('');
        $("#cargoprotect_table_div").hide();
        $("#selected_tier").hide();
    }

}
function chkIsEuPort() {
    if ($('#ensfilling_btn').length > 0) {
        var pol = $('#ebook_origin_country').val();
        var pod = $('#ebook_delivery_country').val();
        const divbutton = $('#ensfilling_btn').find('button');
        divbutton.removeClass('active');
        if (pol !== "" && pod !== "") {
            $.ajax({
                type: 'POST',
                cache: false,
                url: 'EnsFilingAjax?type=checkEUPort&origin=' + pol + '&delivary=' + pod,
                async: false,
                data: $(this).serializeArray(),
                success: function (response) {
                    var isEuPort = response;
                    if (isEuPort == 'Y') {
                        $('#euport').val('Y');
                        chkAPEnsFilingAgreement();
                    } else {
                        divbutton.prop('disabled', true);
                        $('#ensfiling').val('N');
                        $('#ensfilling_btn > .ena').addClass('active');
                    }
                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            });
        } else {
            divbutton.prop('disabled', true);
            $('#ensfiling').val('N');
            $('#ensfilling_btn > .ena').addClass('active');
        }
    }
}

function chkAPEnsFilingAgreement() {
    if ($('#ensfilling_btn').length > 0) {
        const divbutton = $('#ensfilling_btn').find('button');
        divbutton.removeClass('active');
        if ('Y' == $('#euport').val()) {
            const apCode = $('#ebook_agreecode').val();
            console.log('apCode: ' + apCode);
            if ('' != apCode) {
                $.ajax({
                    type: 'POST',
                    cache: false,
                    url: 'EnsFilingAjax?type=isChkAPEnsFilingAgreement&apcode=' + apCode,
                    async: false,
                    data: $(this).serializeArray(),
                    success: function (response) {
                        var ensFilingType = response;
                        setEnsFilingType(ensFilingType);
                    },
                    error: function (xhr) {
                        if (xhr.status === 403) {
                            window.location.href = 'SessionExpired.jsp';
                        }
                    }
                });
            } else {
                divbutton.prop('disabled', true);
                $('#ensfiling').val('N');
                $('#ensfilling_btn > .ena').addClass('active');
            }

        } else {
            divbutton.prop('disabled', true);
            $('#ensfiling').val('N');
            $('#ensfilling_btn > .ena').addClass('active');
        }
    }
}

function setEnsFilingType(ensFilingType) {
    console.log('setEnsFilingType' + ensFilingType);
    if ($('#ensfilling_btn').length > 0) {
        const divbutton = $('#ensfilling_btn').find('button');
        divbutton.removeClass('active');
        if (ensFilingType == 'Single Filing' || ensFilingType == 'S') {
            divbutton.prop('disabled', true);
            $('#ensfiling').val('S');
            $('#ensfilling_btn > .sng').addClass('active');
        } else if (ensFilingType == 'Partial Filing' || ensFilingType == 'P') {
            divbutton.prop('disabled', true);
            $('#ensfiling').val('P');
            $('#ensfilling_btn > .prl').addClass('active');
        } else if (ensFilingType == 'All') {
            console.log($('#ensfiling').val())
            if ('P' == $('#ensfiling').val()) {
                $('#ensfiling').val('P');
                $('#ensfilling_btn > .prl').addClass('active');
            } else {
                $('#ensfiling').val('S');
                $('#ensfilling_btn > .sng').addClass('active');
            }
            divbutton.prop('disabled', false);
            $('.ena').prop('disabled', true);

        } else {
            divbutton.prop('disabled', true);
            $('#ensfiling').val('N');
            $('#ensfilling_btn > .ena').addClass('active');
        }

    }
    
}
function enablePreferredAgency(action) {
    if($('#preferred_agency').length>0){
    let port = $('#ebook_origin_country').val().trim();
    let ebookPreferredAgency = $('#ebook_preferred_agency').val();
    let pol = port.includes('-') ? port.split('-')[0].trim().toUpperCase() : port.toUpperCase();
    let isPreferred = Object.keys(preferredAgencyJson).some(key => key.trim().toUpperCase() === pol);
    let agencies = isPreferred ? preferredAgencyJson[pol] : [];
    let options = agencies.length > 0 ? agencies.map(agency => '<option value="' + agency + '">' + preferredAgencyNameJson[agency] + '</option>').join(''):'<option value=""></option>';
    $('#preferred_agency').html(options).prop('disabled',!isPreferred);
       console.log(ebookPreferredAgency);
       if('previous'!==action && !$('#lrpBookNo').val()){
       ebookPreferredAgency = '';
       $('#ebook_preferred_agency').val('');
       }

         if (ebookPreferredAgency && isPreferred) {

            let optionExists = $('#preferred_agency option[value="' + ebookPreferredAgency + '"]').length > 0;
            if (optionExists) {
                $('#preferred_agency').val(ebookPreferredAgency).trigger('change');
            } else {
                $('#preferred_agency').val($('#preferred_agency option:selected').val()).trigger('change');
            }
        } else {
            $('#preferred_agency').val($('#preferred_agency option:selected').val()).trigger('change');
        }

        if (port && isPreferred && $('#lrpBookNo').val()) {
         $('#preferred_agency').prop('disabled', true);
        }
        if(!isPreferred){
            $('#preferred_agency').val('');   
            $('#ebook_preferred_agency').val(''); 
        }
 }
}

          
    function setPreferredAgency(agency){
             console.log('agency'+agency);
             if(agency){
             $('#ebook_preferred_agency').val(agency); 
              }
     }

 function chkIsEuPortT2lFiling() {
    if ($('#t2lfiling').length) {
        var pol = $('#ebook_origin_country').val();
        var pod = $('#ebook_delivery_country').val();
        
        if (pol && pod) {
            $.ajax({
                type: 'POST',
                cache: false,
                url: 'EnsFilingAjax?type=isT2LFiling&origin=' + pol + '&delivary=' + pod,
                async: false,
                data: $(this).serializeArray(),
                success: function (response) {
                    var isEuPort = response === 'Y';
                    $('#t2lfiling').prop('disabled', !isEuPort);
                                        //  .prop('checked', isEuPort)
                                        //  .val(isEuPort ? 'Y' : 'N');
                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            });
        } else {
            $('#t2lfiling').prop('disabled', true)
                                 .prop('checked', false)
                                 .val('N');
        }
       $('#ebook_t2lfiling').val( $('#t2lfiling').val());
    }
}

 function setT2LFiling(pol,pod) {
    if ($('#t2lfiling').length) {
         console.log(pod+pol);
        if (pol && pod) {
            $.ajax({
                type: 'POST',
                cache: false,
                url: 'EnsFilingAjax?type=isT2LFiling&origin=' + pol + '&delivary=' + pod,
                async: false,
                data: $(this).serializeArray(),
                success: function (response) {
                    var t2lfiling =  $('#ebook_t2lfiling').val();
                    console.log('t2lfiling==>'+t2lfiling);
                     if(response ==='Y'){
                      $('#t2lfiling').prop('disabled', false)
                                         .val(t2lfiling)
                                         .prop('checked', t2lfiling=='Y'?true:false);
                                  
                       }else{
                         $('#t2lfiling').prop('disabled', true)
                                 .prop('checked', false)
                                 .val('N');  
                       }
                      $('#ebook_t2lfiling').val($('#t2lfiling').val());
                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            });
        }
    }
}

function t2lfilingEnable(obj) {
    console.log("$(obj).prop(checked)---> " + $(obj).val());
    if ($(obj).prop("checked") == true) {
        $(obj).val("Y");
    }else {
        $(obj).val("N");
    }
      $('#ebook_t2lfiling').val($(obj).val());
}


function enableSiSubmitParty() {
    if ($('#sisubmit_party').length > 0) {
        let options = '<option value=""></option>';
        Object.keys(customernameJson).forEach(key => {
            options += '<option value="' + key + '">' + customernameJson[key] + '</option>';
        });
        $('#sisubmit_party').html(options);
        const selectedVal = $('#ebook_sisubmit_party').val();
         console.log('selectedVal=>'+selectedVal);
        $('#sisubmit_party').val(selectedVal.length > 0 ? selectedVal : $('#sisubmit_party option:first').val());
    }
}




 function setSiSubmitParty(custype){
             console.log('custype'+custype);
             $('#ebook_sisubmit_party').val(custype); 
     }
     function setAddCustDetails(value, type,index) {
      console.log('value===>'+value);
        $('.help-block').html("");
        if (value == "" || value == 'new') {
            $('#ebook_' + type + 'name'+index).attr('readonly', false);
            $('#ebook_' + type + 'country_name'+index).typeahead('destroy');
            document.getElementById('ebook_' + type + 'name'+index).value = "";
            document.getElementById('ebook_' + type + 'code'+index).value = "";
            document.getElementById('ebook_' + type + 'name'+index).value = "";
            document.getElementById('ebook_' + type + 'address'+index).value = "";
            document.getElementById('ebook_' + type + 'address1'+index).value = "";
            document.getElementById('ebook_' + type + 'email'+index).value = "";
            document.getElementById('ebook_' + type + 'country_name'+index).value = "";
            document.getElementById('ebook_' + type + 'country'+index).value = "";
            document.getElementById('ebook_' + type + 'phone'+index).value = "";
            document.getElementById('ebook_' + type + 'fax'+index).value = "";
            document.getElementById('ebook_' + type + 'city'+index).value = "";
            document.getElementById('ebook_' + type + 'city_desc'+index).value = "";
            document.getElementById('ebook_' + type + 'state'+index).value = "";
            $('#ebook_' + type + 'state'+index).typeahead('');
            callAutocompleteintialize('#ebook_' + type + 'country_name'+index);
            $('#ebook_' + type + 'country_name'+index).css("background-color", "white");
            loadStateAutoComplete('ebook_' + type + 'country_name'+index, 'ebook_' + type + 'state'+index, 'typeheadhidden9', 'load');
            if ($('#' + type + 'typeperson').length > 0) {
                $('#' + type + 'typeperson').val("");
            }
            if ($('#ebook_' + type + 'eori'+index).length > 0) {
                $('#ebook_' + type + 'eori'+index).val("");
            }
    
        } else if (value != "") {
    
            var custval = value.split("~~");
            $('#ebook_' + type + 'country_name'+index).typeahead('destroy');
            $('#ebook_' + type + 'name'+index).attr('readonly', true);
            document.getElementById('ebook_' + type + 'name'+index).value = custval[0];
            document.getElementById('ebook_' + type + 'code'+index).value = custval[1];
            document.getElementById('ebook_' + type + 'name'+index).value = custval[0];
            document.getElementById('ebook_' + type + 'address'+index).value = custval[2];
            document.getElementById('ebook_' + type + 'address1'+index).value = custval[9];
            document.getElementById('ebook_' + type + 'email'+index).value = custval[6];
            document.getElementById('ebook_' + type + 'country_name'+index).value = custval[4].toUpperCase();
            document.getElementById('ebook_' + type + 'country'+index).value = custval[5];
            document.getElementById('ebook_' + type + 'phone'+index).value = custval[7];
            //if($('ebook_' + type + 'fax').is(":hidden")){
            //  document.getElementById('ebook_' + type + 'fax').setAttribute("readonly", true);
            //   }
            //  else{
            document.getElementById('ebook_' + type + 'fax'+index).value = custval[8];
            //    }
            document.getElementById('ebook_' + type + 'city'+index).value = custval[3];
            document.getElementById('ebook_' + type + 'city_desc'+index).value = custval[3];
            $('#ebook_' + type + 'country_name'+index).css("background-color", "white");
            callAutocompleteintialize('#ebook_' + type + 'country_name'+index);
            $('#ebook_' + type + 'country_name'+index).trigger('change');
            document.getElementById('ebook_' + type + 'state'+index).value = custval[10];
            $('#ebook_' + type + 'state'+index).typeahead('val', custval[10]);
          
            if ($('#' + type + 'typeperson').length > 0) {
                $('#' + type + 'typeperson').val(custval[11]);
            }
            if ($('#ebook_' + type + 'eori'+index).length > 0) {
                $('#ebook_' + type + 'eori'+index).val(custval[12]);
            }
        
       
        }
      const ensDocType = $('#ebook_EnsDocType');
      const isENS = ensDocType.length > 0 && ensDocType.val() === 'S' && $('#euport').val()== 'Y';
      if(isENS){  
//      clearCargoNameCode(type);
     }
    
    }
function setCountry_name(custType,rowid,hiddenid){
    var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_'+custType+'country_name'+rowid).typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country3',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#"+hiddenid).val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#"+hiddenid).val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#"+hiddenid).val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#"+hiddenid).val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_'+custType+'country_name'+rowid).typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
}
    
    
function setSellerNameSelectOption() {
    console.log('setSellerNameSelectOption');
  function populateSelectOptions(containerId, selectName, codePrefix, namePrefix, inputCodeId, inputNameId, logLabel) {
    if (!$('#ebook_EnsDocType').length) return;
    const select = $('select[name="' + selectName + '"]');
    const container = $('#' + containerId);
    let nameArray = [];
    let options = '<option value="">Select ' + logLabel + '</option>';
    let hasValidEntry = false;

    if ($('#euport').val() === 'Y') {
      container.find('.previewCust').each(function () {
        const rowID = $(this).attr('id');
        const codeVal = ($('#' + codePrefix + rowID).val() || '').trim();
        const nameVal = ($('#' + namePrefix + rowID).val() || '').trim();

        console.log(rowID + ' ' + logLabel + ' // ' + codeVal + ' // ' + nameVal);

        if (codeVal && nameVal) {
          nameArray.push(nameVal);
          options += '<option value="' + codeVal + '~~' + nameVal + '">' + nameVal + '</option>';
          hasValidEntry = true;
        }
      });
    }

    select.prop("disabled", !hasValidEntry);
    select.html(options);

    // Set select values based on cargo div
    if ($('#euport').val() === 'Y') {
      $('#datatablebycargodiv').find('.preview').each(function () {
        const id = $(this).attr('id');
        const nameInput = $('#' + inputNameId + id);
        const codeInput = $('#' + inputCodeId + id);
        const nameVal = (nameInput.val() || '').trim();
        const codeVal = (codeInput.val() || '').trim();

        if (nameArray.includes(nameVal) && codeVal) {
          if (logLabel === 'Buyer') {
            $('#buyerSelect' + id).val(codeVal + '~~' + nameVal);
          } else if (logLabel === 'Seller') {
            $('#sellerSelect' + id).val(codeVal + '~~' + nameVal);
          }
        } else {
          nameInput.val('');
          codeInput.val('');
          if (logLabel === 'Buyer') {
            $('#buyerSelect' + id).val('');
          } else if (logLabel === 'Seller') {
            $('#sellerSelect' + id).val('');
          }
        }
      });
    }
    // Optional: ENS doc validation (uncomment if needed)
    // validationENSDocType(inputNameId, inputCodeId, logLabel);
  }

  // Initialize for Seller and Buyer
  populateSelectOptions('SellerContainer', 'sellerNameList', 'ebook_Sellercode', 'ebook_Sellername', 'sellerCode', 'sellersName', 'Seller');

  populateSelectOptions('BuyerContainer', 'buyerNameList', 'ebook_Buyercode', 'ebook_Buyername','buyersCode', 'buyersName', 'Buyer');
}


function setCustNameCode(selectElem, codeId, nameId) {
    var selectedValue = $(selectElem).val(); // Use jQuery to get value
    var parts = selectedValue.split('~~');

    // Set values using dynamic IDs
    $('#' + codeId).val(parts[0] || '');
    $('#' + nameId).val(parts[1] || '');

}

function validationENSDocType(inputNameId, inputCodeId, logLabel) {
    if($('#ebook_EnsDocType').length> 0 && $('#euport').val()== 'Y'){
    const ensDocType = $('#ebook_EnsDocType');
    const ensFilling = $('#ensfiling');
    const isENS = ensDocType.length > 0 && ensDocType.val() === 'S' && ensFilling.length> 0 && ensFilling.val() == 'S';
        const buyerName1 = $('#ebook_Buyername1').val();
        const buyerCode1 = $('#ebook_Buyercode1').val();
        const sellerName1 = $('#ebook_Sellername1').val();
        const sellerCode1 = $('#ebook_Sellercode1').val();
        $('#datatablebycargodiv').find('.preview').each(function () {
            const id = $(this).attr('id');
            const nameInput = $('#' + inputNameId + id);
            const codeInput = $('#' + inputCodeId + id);
            const nameVal = nameInput.val() || '';
            const codeVal = codeInput.val() || '';
         if (isENS){
           
             if( nameVal.length > 0 &&  codeVal.length > 0 && logLabel == 'Buyer'){
               $('#buyerSelect' + id).val(codeVal + '~~' + nameVal);    
             }else{
                 if (buyerName1 && buyerCode1) {
                 $('#buyersName' + id).val(buyerName1);
                 $('#buyersCode' + id).val(buyerCode1);
                } 
             }
             if( nameVal.length > 0 &&  codeVal.length > 0 && logLabel == 'Seller'){
               $('#sellerSelect' + id).val(codeVal + '~~' + nameVal);    
             }else{
                 if (sellerName1 && sellerCode1) {
                $('#sellersName' + id).val(sellerName1);
                $('#sellerCode' + id).val(sellerCode1);
               }
             }
           
         }else if (nameVal && codeVal) {
                if (logLabel === 'Buyer') {
                    $('#buyerSelect' + id).val(codeVal + '~~' + nameVal);
                } else if (logLabel === 'Seller') {
                    $('#sellerSelect' + id).val(codeVal + '~~' + nameVal);
                }
         } else {
                nameInput.val('');
                codeInput.val('');
                if (logLabel === 'Buyer') {
                    $('#buyerSelect' + id).val('');
                } else if (logLabel === 'Seller') {
                    $('#sellerSelect' + id).val('');
                }
            }
    
        });

    }
}


function disableCustAddButton() {
  
    if ($('#ebook_EnsDocType').length > 0 && $('#euport').val()== 'Y') {
         const ensDocType = $('#ebook_EnsDocType');
         const ensFiling = $('#ensfiling');
         const addSellerBtn = $('#addSellerBtn');
         const addBuyerBtn = $('#addBuyerBtn');
       
        addSellerBtn.removeClass('disabled');
        addBuyerBtn.removeClass('disabled');
        // If conditions match, disable buttons + clean rows
        if (ensDocType.val() == 'S' || $('#euport').val()== 'N') {
            addSellerBtn.addClass('disabled');
            addBuyerBtn.addClass('disabled');
        }
    
    $('#datatablebycargodiv').find('.preview').each(function () {
     const cargoId = $(this).attr('id');
     const cmdtyhblno = $('#cmdtyhblno' + cargoId);
  
     cmdtyhblno.prop('readonly', true);
       if($('#euport').val()== 'Y'){
     if (ensDocType.val() === 'C') {
       cmdtyhblno.prop('readonly', false);
     }else{
      cmdtyhblno.val('');  
     } 
       }
     });
    }
}
function clearCargoNameCode(custType) {
    
  $('#datatablebycargodiv').find('.preview').each(function () {
    const id = $(this).attr('id');

    let nameInput, codeInput, selectInput;

    if (custType === 'Buyer') {
      nameInput = $('#buyersName' + id);
      codeInput = $('#buyersCode' + id);
      selectInput = $('#buyerSelect' + id);
    } else if (custType === 'Seller') {
      nameInput = $('#sellersName' + id);
      codeInput = $('#sellersCode' + id); // fixed typo from earlier
      selectInput = $('#sellerSelect' + id);
    }

    if (nameInput && nameInput.length && codeInput && codeInput.length && selectInput && selectInput.length) {
      nameInput.val('');
      codeInput.val('');
      selectInput.val('');
    }
  });
}



function setENSType() {
    if ($('#ensDocType_btn').length > 0) {
        var pol = $('#ebook_origin_country').val();
        var pod = $('#ebook_delivery_country').val();
        const divbutton = $('#ensDocType_btn').find('button');
        divbutton.removeClass('active');
        if (pol !== "" && pod !== "") {
            $.ajax({
                type: 'POST',
                cache: false,
                url: 'EnsFilingAjax?type=checkEUPort&origin=' + pol + '&delivary=' + pod,
                async: false,
                data: $(this).serializeArray(),
                success: function (response) {
                    var isEuPort = response;
                    const ensDocType = $('#ebook_EnsDocType');
                    if (isEuPort == 'Y') {
                    divbutton.removeClass('active').prop('disabled', false);
                     let ensTypeVal = ensDocType.val() || 'S';  
                    console.log('ens type ======> ' + ensTypeVal);
                    ensDocType.val(ensTypeVal);
                       if (ensTypeVal == 'S') {
                        $('#ensDocType_btn > .sbl').addClass('active');
                        } else if (ensTypeVal == 'C') {
                        $('#ensDocType_btn > .cnd').addClass('active');
                       }
                    } else {
                        divbutton.prop('disabled', true);
                        $('#ebook_EnsDocType').val('S');
                        $('#ensDocType_btn > .sbl').addClass('active');
                    }
                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            });
        } else {
            divbutton.prop('disabled', true);
            $('#ebook_EnsDocType').val('S');
            $('#ensDocType_btn > .sbl').addClass('active');
        }
    }
}

function checkForDuplicateNames() {
    const sellerNames = new Set();
    const buyerNames = new Set();
    let duplicateSeller = false;
    let duplicateBuyer = false;
    $('#BuyerContainer').find('.previewCust').each(function () {
        const id = this.id;
        const byname = ($('#ebook_Buyername' + id).val() || '').trim().toLowerCase();
        if (byname && !duplicateBuyer) {
            if (buyerNames.has(byname)) {
                duplicateBuyer = true;
            } else {
                buyerNames.add(byname);
            }
        }
    });
    $('#SellerContainer').find('.previewCust').each(function () {
        const id = this.id;
        const slname = ($('#ebook_Sellername' + id).val() || '').trim().toLowerCase();
        if (slname && !duplicateSeller) {
            if (sellerNames.has(slname)) {
                duplicateSeller = true;
            } else {
                sellerNames.add(slname);
            }
        }

    });
    if (duplicateSeller) alert('Duplicate customer in Seller');
    if (duplicateBuyer) alert('Duplicate customer in Buyer');
  return duplicateSeller || duplicateBuyer;
  }
function getCustomerValidationRules(index, hideFax, prefix) {
    const nameField = '#' + prefix + 'name' + index;

    function isNameEntered() {
        return $(nameField).val().trim().length > 0;
    }

    const rules = {
        [prefix + 'address' + index]: {
            required: isNameEntered
        },
        [prefix + 'country_name' + index]: {
            required: isNameEntered
        },
        [prefix + 'city_desc' + index]: {
            required: isNameEntered
        },
        [prefix + 'phone' + index]: {
            validnumbers: '#' + prefix + 'phone' + index,
            ZeroTest: '#' + prefix + 'phone' + index,
            phonefax: ['#' + prefix + 'phone' + index]
        },
        [prefix + 'email' + index]: {
            email: true
        },
//        [prefix + 'fax' + index] : {
//            validnumbers: '#' + prefix + 'phone' + index,
//            ZeroTest: '#' + prefix + 'phone' + index,
//            phonefax: ['#' + prefix + 'phone' + index]
//        }
    };

    const messages = {
        [prefix + 'address' + index]: {
            required: 'This field is required'
        },
        [prefix + 'country_name' + index]: {
            required: 'This field is required'
        },
        [prefix + 'city_desc' + index]: {
            required: 'This field is required'
        },
        [prefix + 'phone' + index]: {
            validnumbers: 'Invalid Phone Number',
            ZeroTest: 'Invalid Phone Number',
            phonefax: 'Invalid Phone Format'
        },
        [prefix + 'email' + index]: {
            email: 'Invalid Email Address'
        },
        [prefix + 'fax' + index] : {
            validnumbers: 'Invalid Fax Number',
            ZeroTest: 'Invalid Fax Number',
            phonefax: 'Invalid Fax Format'
        }
    };

 
    return { rules, messages };
}

//// Declare this globally once
//const addAmenCustRules = {};

function applyRules(formid, customerType,HideFaxNo) {
    const form = $('#'+formid);
    console.log(form.data('validator'));
   if (form.data('validator')) {
    $('#'+customerType+'Container').find('.previewCust').each(function () {
        const rowID = this.id;
     const { rules, messages } = getCustomerValidationRules(rowID, HideFaxNo, 'ebook_' + customerType);
// console.log(  rules, messages );
 $.each(rules, function (fieldName, ruleObj) {
    const field = form.find('[name="' + fieldName + '"]');
    if (field.length) {
        field.rules('add', {
            ...ruleObj,
            messages: messages[fieldName] || {}
        });
    }
});

    });
}
}
function chkEU_ENSDeclaration() {
    if ($('#GENS_TAB').length > 0) {
        var pol = $('#ebook_origin_country').val();
        var pod = $('#ebook_delivery_country').val();

        if (pol !== "" && pod !== "") {
            $.ajax({
                type: 'POST',
                cache: false,
                url: 'EnsFilingAjax?type=checkEUPort&origin=' + encodeURIComponent(pol) + '&delivary=' + encodeURIComponent(pod),
                async: false,
                success: function (response) {
                    console.log('response==> ' + response);
                    if (response === 'Y') {
                        $('#euport').val('Y');
                        enableMultiDeclarant('N');
                        $('.declarantcomclass').prop('disabled', false);
                        $('.ensbuyersellerclass').prop('disabled', false);
                        $('#Buyersearch,#Sellersearch').removeClass('disabled');
                        $('#ENSPreviewDiv').show();
                        $('#GENS_TAB_Fields').show();
                    } else {
                        $('#euport').val('N');
                        $('.declarantcomclass').prop('disabled', true);
                        $("#supplementarydeclarant").find('.declarantcomclass').val("");
                        $("#SellerSelect").val("").trigger("change");
                        $("#BuyerSelect").val("").trigger("change");
                        $('.ensbuyersellerclass').prop('disabled', true);
                        $('#ebook_Buyercity,#ebook_Sellercity').val('');
                        $('#Buyersearch,#Sellersearch').addClass('disabled');
                        $('#ENSPreviewDiv').hide();
                        $('#GENS_TAB_Fields').hide();
                        // enableMultiDeclarant('N');
                    }
                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            });
        } else {
            $('#euport').val('N');  
            $('.declarantcomclass').prop('disabled', true);
            $("#supplementarydeclarant").find('.declarantcomclass').val("");
            $("#SellerSelect").val("").trigger("change");
            $("#BuyerSelect").val("").trigger("change");
            $('.ensbuyersellerclass').prop('disabled', true); 
            $('#ebook_Buyercity,#ebook_Sellercity').val('');
           $('#Buyersearch,#Sellersearch').addClass('disabled');
            $('#ENSPreviewDiv').hide();
        }
     chkEU_ENSFilingType();
    }
}

function enableMultiDeclarant(ismanual) {
    if ( $('#euport').val() == 'Y') {
        if ($("#ensFillingType").val() == "Multiple Filing") {
            $("#buyerseller").hide();
            $("#supplementarydeclarant").show();
            if ("Y" == ismanual) {
                $('#declarantFillingTypeHBL').prop('checked', true);
            }
            //$("#buyerseller").find(".byslmandatory").removeClass('required');

        } else if ($("#ensFillingType").val() == "Single Filing") {
            $("#supplementarydeclarant").hide();
            $('input[name="declarantFillingType"]').prop('checked', false);
            // if ("Y" == ismanual) {
            //      $("#buyerseller").find(".byslmandatory").addClass('required');
            //  }
            $("#buyerseller").show();
        }
        if ("Y" == ismanual) {
            $("#supplementarydeclarant").find('.declarantcomclass').val("");
            $("#SellerSelect").val("").trigger("change");
            $("#BuyerSelect").val("").trigger("change");
        }
    }

}
    function chkEU_ENSFilingType() {
    console.log('chkEU_ENSDeclaration==> ' + $('#ensFillingType').val());
    if ($('#ensFillingType').val() == 'Single Filing' && $("#euport").length > 0 && $("#euport").val() == 'Y') {
        $('#ensCollapseBuyer, #ensCollapseSeller').addClass('show');
        $('.ensCollapseBuyer, .ensCollapseSeller').find("i").removeClass("fa-plus").removeClass("fa-minus").addClass("fa-minus");
        $('#ensCollapseBuyer, #ensCollapseSeller').removeClass('collapsed').addClass('expanded');
    }else {
        $('#ensCollapseBuyer, #ensCollapseSeller').removeClass('show');
        $('.ensCollapseBuyer, .ensCollapseSeller').find("i").removeClass("fa-minus").removeClass("fa-plus").addClass("fa-plus");
        $('#ensCollapseBuyer, #ensCollapseSeller').removeClass('expanded').addClass('collapsed');
    }
}
function getContainerDGFlag(){
    let dgFlag =    'N';
    $('#datatablebycargodiv').find('.preview').each(function () {
        const id = $(this).attr('id');
        const dgFlagInput = $('#' + 'hzd' + id);
        const dgFlagVal = dgFlagInput.val() || '';
        if(dgFlagVal == 'Y'){
            dgFlag = 'Y';
              return false;
        }
    });
    return dgFlag;
}

/**
 * Multiple DG Details Functions
 * Adds, removes, and manages multiple DG entries per container
 */

// Add a new DG entry row for the specified container
function addDGRow(containerRow) {
    console.log('[ECOM] addDGRow called for container: ' + containerRow);
    
    var dgContainer = $('#dgContainer' + containerRow);
    if (!dgContainer.length) {
        console.error('[ECOM] DG Container not found: dgContainer' + containerRow);
        return;
    }
    
    // Get current DG count and find the highest index to avoid conflicts
    var dgCountInput = $('#dgCount' + containerRow);
    var maxIndex = 0;
    
    // Find the highest existing index
    dgContainer.find('.dg-entry').each(function() {
        var entryId = $(this).attr('id');
        var match = entryId.match(/_(\d+)$/);
        if (match) {
            var index = parseInt(match[1]);
            if (index > maxIndex) {
                maxIndex = index;
            }
        }
    });
    
    var newCount = maxIndex + 1;
    
    console.log('[ECOM] Max existing index: ' + maxIndex + ', New Count: ' + newCount);
    
    // Clone the first DG entry template (prefer _1, fallback to any existing entry)
    var firstEntry = $('#dgEntry' + containerRow + '_1');
    if (!firstEntry.length) {
        console.warn('[ECOM] First DG entry (_1) not found, using first available entry as template');
        firstEntry = dgContainer.find('.dg-entry').first();
        if (!firstEntry.length) {
            console.error('[ECOM] No DG entries found in container');
            return;
        }
    }
    
    var newEntry = firstEntry.clone();
    
    // Update IDs and names with new index
    newEntry.attr('id', 'dgEntry' + containerRow + '_' + newCount);
    newEntry.find('input, select, textarea').each(function() {
        var element = $(this);
        var oldId = element.attr('id');
        var oldName = element.attr('name');
        
        if (oldId) {
            // Replace any _N suffix with the new count (works for _1, _2, _3, etc.)
            var newId = oldId.replace(/_\d+$/, '_' + newCount);
            element.attr('id', newId);
        }
        
        if (oldName) {
            // Replace any _N suffix with the new count (works for _1, _2, _3, etc.)
            var newName = oldName.replace(/_\d+$/, '_' + newCount);
            element.attr('name', newName);
        }
        
        // Clear values for new entry
        if (element.is('input[type="text"]') || element.is('textarea')) {
            element.val('');
        } else if (element.is('select')) {
            element.prop('selectedIndex', 0);
        }
    });
    
    // Update the button section - add both Add and Remove buttons
    var lastRow = newEntry.find('.form-group.row').last();
    
    // Remove existing button column if present
    lastRow.find('.col-sm-3').has('button').remove();
    
    // Add both Add and Remove buttons inline with Shipping Name field
    var buttonColumn = $('<div class="col-sm-3">' +
        '<button type="button" class="btn btn-sm btn-success" onclick="addDGRow(' + containerRow + ')" title="Add DG Entry" style="margin-top: 22px; margin-right: 5px;">' +
        '<i class="fa fa-plus"></i>' +
        '</button>' +
        '<button type="button" class="btn btn-sm btn-danger" onclick="removeDGRow(this, ' + containerRow + ')" title="Remove DG Entry" style="margin-top: 22px;">' +
        '<i class="fa fa-minus"></i>' +
        '</button>' +
        '</div>');
    lastRow.append(buttonColumn);
    
    // Add horizontal line separator
    newEntry.prepend('<div class="horizontal_line"></div>');
    
    // Append to container
    dgContainer.append(newEntry);
    
    // Update count
    dgCountInput.val(newCount);
    
    // Initialize typeahead for the new UN number field
    var unnoId = 'unno' + containerRow + '_' + newCount;
    var dgClassId = 'dgClass' + containerRow + '_' + newCount;
    var shippingNameId = 'shippingName' + containerRow + '_' + newCount;
    var hiddenId = 'hiddencomid' + containerRow;
    
    if (typeof dgload === 'function') {
        dgload(unnoId, hiddenId, dgClassId, shippingNameId);
    }
    
    console.log('[ECOM] DG Entry added successfully. New count: ' + newCount);
}

// Remove a DG entry row
function removeDGRow(btn, containerRow) {
    console.log('[ECOM] removeDGRow called for container: ' + containerRow);
    
    var entryDiv = $(btn).closest('.dg-entry');
    var entryId = entryDiv.attr('id');
    
    console.log('[ECOM] Removing DG entry: ' + entryId);
    
    var dgContainer = $('#dgContainer' + containerRow);
    var entryCount = dgContainer.find('.dg-entry').length;
    
    // Don't allow removing if only one entry remains
    if (entryCount <= 1) {
        alert('Cannot remove the last DG entry. At least one entry is required.');
        return;
    }
    
    // Don't allow removing the first entry (_1) - it's used as template
    if (entryId === 'dgEntry' + containerRow + '_1') {
        alert('Cannot remove the first DG entry. It is used as a template. Please remove other entries first.');
        return;
    }
    
    // Remove the entry
    entryDiv.remove();
    
    // Update count
    updateDGCount(containerRow);
    
    console.log('[ECOM] DG Entry removed successfully');
}

// Update the DG count for a container
function updateDGCount(containerRow) {
    var dgContainer = $('#dgContainer' + containerRow);
    var entryCount = dgContainer.find('.dg-entry').length;
    
    $('#dgCount' + containerRow).val(entryCount);
    
    console.log('[ECOM] updateDGCount: Container=' + containerRow + ', Count=' + entryCount);
}

// Clear all DG entries for a container (used when hazardous flag is unchecked)
function clearAllDGEntries(containerRow) {
    console.log('[ECOM] clearAllDGEntries called for container: ' + containerRow);
    
    var dgContainer = $('#dgContainer' + containerRow);
    
    // Check if dgContainer exists (new structure with multiple DG support)
    if (dgContainer.length > 0) {
        // Remove all entries except the first one
        dgContainer.find('.dg-entry:not(:first)').remove();
        
        // Clear values in the first entry
        dgContainer.find('.dg-entry:first input, .dg-entry:first select, .dg-entry:first textarea').each(function() {
            var element = $(this);
            if (element.is('input[type="text"]') || element.is('textarea')) {
                element.val('');
            } else if (element.is('select')) {
                element.prop('selectedIndex', 0);
            }
        });
    } else {
        // Fallback: Clear fields with _1, _2, _3... suffixes (direct field structure)
        var dgCountElem = document.getElementById('dgCount' + containerRow);
        var maxCount = dgCountElem ? parseInt(dgCountElem.value) || 5 : 5;
        
        for (var dgIdx = 1; dgIdx <= maxCount; dgIdx++) {
            var suffix = '_' + dgIdx;
            var marinePollutElem = document.getElementById('marinePollut' + containerRow + suffix);
            var unnoElem = document.getElementById('unno' + containerRow + suffix);
            var flashPointElem = document.getElementById('flashPoint' + containerRow + suffix);
            var flashUnitElem = document.getElementById('flashUnit' + containerRow + suffix);
            var shippingNameElem = document.getElementById('shippingName' + containerRow + suffix);
            var dgClassElem = document.getElementById('dgClass' + containerRow + suffix);
            
            if (marinePollutElem) marinePollutElem.value = '';
            if (unnoElem) unnoElem.value = '';
            if (flashPointElem) flashPointElem.value = '';
            if (flashUnitElem) flashUnitElem.selectedIndex = 0;
            if (shippingNameElem) shippingNameElem.value = '';
            if (dgClassElem) dgClassElem.value = '';
        }
    }
    
    // Reset count to 1
    var dgCountElem = document.getElementById('dgCount' + containerRow);
    if (dgCountElem) {
        dgCountElem.value = '1';
    }
    
    console.log('[ECOM] All DG entries cleared for container: ' + containerRow);
}

// Populate DG entries when editing (called from JSP)
function populateDGEntries(containerRow, dgList) {
    if (!dgList || dgList.length === 0) {
        console.log('[ECOM] No DG entries to populate for container: ' + containerRow);
        return;
    }
    
    console.log('[ECOM] populateDGEntries: Container=' + containerRow + ', Count=' + dgList.length);
    
    var dgContainer = $('#dgContainer' + containerRow);
    
    // Check if dgContainer exists (new structure with multiple DG support)
    if (dgContainer.length > 0) {
        // Clear existing entries except the first
        dgContainer.find('.dg-entry:not(:first)').remove();
        
        // Populate each DG entry
        for (var i = 0; i < dgList.length; i++) {
            var dgEntry = dgList[i];
            var dgIndex = i + 1;
            var suffix = '_' + dgIndex;
            
            // Add new row if needed (skip first as it already exists)
            if (i > 0) {
                addDGRow(containerRow);
            }
            
            // Build UN number display value in typeahead format: "UN ~~ Shipping Name"
            var unnoDisplayValue = dgEntry.ebook_unno || '';
            if (dgEntry.ebook_unno && dgEntry.ebook_shipname) {
                unnoDisplayValue = dgEntry.ebook_unno + ' ~~ ' + dgEntry.ebook_shipname;
            }
            
            // Populate fields
            $('#marinePollut' + containerRow + suffix).val(dgEntry.ebook_poluttunit || 'N');
            $('#unno' + containerRow + suffix).val(unnoDisplayValue);  
            $('#unno' + containerRow + suffix).typeahead('val', unnoDisplayValue);  // Set using typeahead API
            $('#flashPoint' + containerRow + suffix).val(dgEntry.ebook_flashpoint || '');
            $('#flashUnit' + containerRow + suffix).val(dgEntry.ebook_flashunit || 'C');
            $('#dgClass' + containerRow + suffix).val(dgEntry.ebook_dgclass || '');
            $('#shippingName' + containerRow + suffix).val(dgEntry.ebook_shipname || '');
            
            // If shipping name or DG class is empty but UN number exists, fetch from server
            if (dgEntry.ebook_unno && (!dgEntry.ebook_shipname || !dgEntry.ebook_dgclass)) {
                console.log('[ECOM] Fetching shipping name for UN: ' + dgEntry.ebook_unno + ', suffix: ' + suffix);
                setShippingName(dgEntry.ebook_unno, containerRow, suffix);
            }
            
            console.log('[ECOM] Populated DG entry ' + dgIndex + ': UN=' + dgEntry.ebook_unno + ', Shipping Name=' + dgEntry.ebook_shipname);
        }
    } else {
        // Fallback: Populate fields directly with _1, _2, _3... suffixes
        for (var i = 0; i < dgList.length; i++) {
            var dgEntry = dgList[i];
            var dgIndex = i + 1;
            var suffix = '_' + dgIndex;
            
            // Check if fields exist, if not and we need multiple entries, try to add a row
            var unnoElem = document.getElementById('unno' + containerRow + suffix);
            if (!unnoElem && i > 0 && typeof addDGRow === 'function') {
                // Try to add a new DG row dynamically
                addDGRow(containerRow);
                unnoElem = document.getElementById('unno' + containerRow + suffix);
            }
            
            // Build UN number display value in typeahead format: "UN ~~ Shipping Name"
            var unnoDisplayValue = dgEntry.ebook_unno || '';
            if (dgEntry.ebook_unno && dgEntry.ebook_shipname) {
                unnoDisplayValue = dgEntry.ebook_unno + ' ~~ ' + dgEntry.ebook_shipname;
            }
            
            // Populate fields if they exist
            var marinePollutElem = document.getElementById('marinePollut' + containerRow + suffix);
            var flashPointElem = document.getElementById('flashPoint' + containerRow + suffix);
            var flashUnitElem = document.getElementById('flashUnit' + containerRow + suffix);
            var shippingNameElem = document.getElementById('shippingName' + containerRow + suffix);
            var dgClassElem = document.getElementById('dgClass' + containerRow + suffix);
            
            if (marinePollutElem) marinePollutElem.value = dgEntry.ebook_poluttunit || 'N';
            if (unnoElem) $('#unno' + containerRow + suffix).typeahead('val', unnoDisplayValue);  // Set using typeahead API
            if (flashPointElem) flashPointElem.value = dgEntry.ebook_flashpoint || '';
            if (flashUnitElem) flashUnitElem.value = dgEntry.ebook_flashunit || 'C';
            if (shippingNameElem) shippingNameElem.value = dgEntry.ebook_shipname || '';
            if (dgClassElem) dgClassElem.value = dgEntry.ebook_dgclass || '';
            
            console.log('[ECOM] Populated DG entry ' + dgIndex + ': UN=' + unnoDisplayValue + ', Shipping Name=' + dgEntry.ebook_shipname);
        }
    }
    
    // Update count
    var dgCountElem = document.getElementById('dgCount' + containerRow);
    if (dgCountElem) {
        dgCountElem.value = dgList.length;
    }
    
    console.log('[ECOM] populateDGEntries completed for container: ' + containerRow);
}
