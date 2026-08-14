/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function openCopyPasteModal(containerid, commodityid) {
    var option, id;
    option = "";
    var markclasscount = 0;
    var markdescclass;
    var cntrid, cntrval, selectedcntrid, selectedcntrval, markval, descval;
    var extracmdyiddub;
    selectedcntrid = containerid;
    document.getElementById("copypasteall").checked = false;
    $("#availablecontainerList").attr("disabled", false);
    selectedcntrid = selectedcntrid.replace("containernosicontainerid", "");
    markval = $("#marksicontainerid" + selectedcntrid + commodityid).val();
    descval = $("#descpsicontainerid" + selectedcntrid + commodityid).val();
    if (
            (markval != null && markval != "") ||
            (descval != null && descval != "")
            ) {
        selectedcntrval = $("#" + containerid).val();
        $("#copydmarksid").val("");
        $("#copycomdtyid").val("");
        $(".copypasteclass").each(function () {
            markclasscount = 0;
            cntrval = $(this).val();
            cntrid = $(this).attr("id");
            id = cntrid.replace("containernosicontainerid", "");
            markdescclass = "markdescclass" + id;
            if (cntrval != selectedcntrval) {
                option += "<option value = '" + id + "'>" + $(this).val() + "</option>";
            } else if (cntrval == selectedcntrval) {
                $("." + markdescclass).each(function () {
                    //console.log('entered in each function');
                    if (markclasscount == 1) {
                        option += "<option value = '" + id + "'>" + cntrval + "</option>";
                    }
                    markclasscount++;
                });
            }
        });
        //console.log("option>>" + option);
        if (option != "") {
            $("#copydmarksid").val(selectedcntrid);
            $("#copycomdtyid").val(commodityid);
            $("#availablecontainerList").html(option);
            $("#copypastemodal").modal("show");
        } else {
            alert(addComm);
        }
    } else {
        if (
                (markval == null || markval == "") &&
                (descval == null || descval == "")
                ) {
            alert(addMarkorDesc);
        }
    }
}
function markDescPasteFunc() {
    var checkboxcheck = "N";
    var markid, comid, cntrid;
    if ($("#copypasteall").is(":checked")) {
        checkboxcheck = "Y";
    }
    var selectedcontainercount = 0;
    console.log(
            "$('#copydmarksid').val()>>>" +
            $("#copydmarksid").val() +
            " $('#copycomdtyid').val()>>" +
            $("#copycomdtyid").val()
            );
    var id = $("#copydmarksid").val().replace("containernosicontainerid", "");
    var comdtyid = $("#copycomdtyid").val();
    var markVal = $("#marksicontainerid" + id + comdtyid).val();
    var descVal = $("#descpsicontainerid" + id + comdtyid).val();
    console.log("markVal>>" + markVal);
    console.log("descVal>>" + descVal);
    if (checkboxcheck == "Y") {
        $(".copypasteclass").each(function () {
            cntrid = $(this).attr("id");
            cntrid = cntrid.replace("containernosicontainerid", "");
            $("." + "markdescclass" + cntrid).each(function () {
                markid = $(this).attr("id");
                comid = markid.replace("marksicontainerid", "");
                console.log("markid>>" + markid);
                console.log(
                        '"#descpsicontainerid"+comid>>' + "#descpsicontainerid" + comid
                        );
                $(this).val(markVal);
                $("#descpsicontainerid" + comid).val(descVal);
            });
        });
    } else {
        $("#availablecontainerList :selected").each(function (i, selected) {
            selectedcontainercount++;
            var selectidval = $(selected).val();
            console.log("selectidval>>" + selectidval);
            $("." + "markdescclass" + selectidval).each(function () {
                markid = $(this).attr("id");
                comid = markid.replace("marksicontainerid", "");
                console.log("markid>>" + markid);
                console.log(
                        '"#descpsicontainerid"+comid>>' + "#descpsicontainerid" + comid
                        );
                $(this).val(markVal);
                $("#descpsicontainerid" + comid).val(descVal);
            });
        });
    }
    if (checkboxcheck == "Y" || selectedcontainercount > 0) {
        $("#copypastemodal").modal("hide");
        $("#copydmarksid").val("");
        $("#copycomdtyid").val("");
    } else {
        alert(selectCont);
    }
}

function copyPasteCBFunc() {
    if ($("#copypasteall").is(":checked")) {
        $("#availablecontainerList").attr("disabled", true);
    } else {
        $("#availablecontainerList").attr("disabled", false);
    }
}
function setShipperSealMandatory(val) {
    console.log("setShipperSealMandatory>>>" + val);
    if (val == "YES") {
        $(".shipperseallabelclass").each(function () {
            $(this).html($(this).html() + '<span class="mandatory"> * </span>');
        });
        $(".shippersealinputclass").each(function () {
            $(this).addClass("required");
        });
    }
}

//cc mail notification script
function ccmailclick() {
    if ($("#ccmailcheckbox").is(":checked")) {
        $("#ccmailidmodal,#ccaddbtn,#ccmaillist,#removebtn").attr(
                "disabled",
                false
                );
    } else {
        $("#ccmailidmodal,#ccaddbtn,#ccmaillist,#removebtn").attr("disabled", true);
    }
}
function nor(obj, contno) {
    console.log("Inside the nor Function .....>");
    if (obj == "N") {
        console.log("Inside the nor Function .....> obj value is " + obj);
        $("#" + contno + "\\#settemp\\#").attr("disabled", false);
        $("#" + contno + "\\#tempunit\\#").attr("disabled", false);
        $("#" + contno + "\\#ventilation\\#").attr("disabled", false);
    } else {
        console.log("Inside the nor Function .....> obj value is " + obj);
        $("#" + contno + "\\#settemp\\#").attr("disabled", true);
        $("#" + contno + "\\#tempunit\\#").attr("disabled", true);
        $("#" + contno + "\\#ventilation\\#").attr("disabled", true);
        $("#" + contno + "\\#settemp\\#").val("");
        $("#" + contno + "\\#tempunit\\#").val("");
        $("#" + contno + "\\#ventilation\\#").val("");
    }
}
function addccmailid() {
    if ($("#ccmailidmodal").val() != "") {
        var checkdupccmail = false;
        $("#ccmaillist option").each(function () {
            if (trim($("#ccmailidmodal").val()) == trim($(this).val())) {
                checkdupccmail = true;
            }
        });
        if (checkdupccmail) {
            alert(uniqueMail);
        } else if (!validateEmailToCommon($("#ccmailidmodal").val())) {
            alert(validMail);
        } else {
            var htmldata = $("#ccmaillist").html();
            htmldata =
                    htmldata +
                    '<option value="' +
                    $("#ccmailidmodal").val() +
                    '">' +
                    $("#ccmailidmodal").val() +
                    "</option>";
            $("#ccmaillist").html(htmldata);
            $("#ccmailidmodal").val("");
        }
    } else {
        alert(entCCMailId);
    }
}
function removeCCMailId() {
    $(
            "#ccmaillist option[value='" + $("#ccmaillist :selected").val() + "']"
            ).remove();
}
function validateEmailToCommon(value) {
    var regex = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    return regex.test(value) ? true : false;
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

function setCustDetails(value, type, printOnBlAddMand) {
    console.log("printOnBlAddMand" + printOnBlAddMand);
    if ($("#" + type + "newcustimg").length > 0) {
        $("#" + type + "newcustimg").html(
                '<i class="fa fa-pencil-square" id="ccupdateimg"></i>  New'
                );
    }
    if (value == "" || value == "new") {
        $("#ebook_" + type + "country_name").typeahead("destroy");
        document.getElementById("ebook_" + type + "name").value = "";
        document.getElementById(type + "CustCode").value = "";
        document.getElementById("ebook_" + type + "name").value = "";
        document.getElementById("ebook_" + type + "address").value = "";
        document.getElementById("ebook_" + type + "address1").value = "";
        document.getElementById("ebook_" + type + "email").value = "";
        document.getElementById("ebook_" + type + "country_name").value = "";
        document.getElementById("ebook_" + type + "country").value = "";
        document.getElementById("ebook_" + type + "phone").value = "";
        document.getElementById("ebook_" + type + "fax").value = "";
        document.getElementById("ebook_" + type + "city").value = "";
        document.getElementById("ebook_" + type + "city_desc").value = "";
        document.getElementById("ebook_" + type + "state").value = "";
        if ($("#ebook_" + type + "zipcode").length > 0) {
            $("#ebook_" + type + "zipcode").val("");
        }
        if ($("#" + type + "typeperson").length > 0) {
            $("#" + type + "typeperson").val("");
        }
        if ($("#ebook_" + type + "eori").length > 0) {
            $("#ebook_" + type + "eori").val("");
        }
        $("#ebook_" + type + "state").typeahead("val", "");
        if (type == "shipper" || type == "consignee" || type == "notify") {
            document.getElementById("header" + type).value = "";
            document.getElementById("ebook_" + type + "nameheader").value = "";
            $("#header" + type).attr("readonly", true);
            $("#ebook_" + type + "nameheader").attr("readonly", true);
        }

        callAutocompleteintialize("#ebook_" + type + "country_name");
        $("#ebook_" + type + "country_name").css("background-color", "white");
        loadStateAutoComplete(
                "ebook_" + type + "country_name",
                "ebook_" + type + "state",
                "typeheadhidden10",
                "load"
                );

    } else if (value != "") {

        var headerShipper = "";
        var custval = value.split("~~");
       
        if (
                (type == "shipper" || type == "consignee" || type == "notify") &&
                (printOnBlAddMand == "y" || printOnBlAddMand == "Y")
                ) {
            $("#header" + type).attr("readonly", true);
            $("#ebook_" + type + "nameheader").attr("readonly", true);
            var stateval = "";
            if (
                    custval[10] != null &&
                    custval[10] != "null" &&
                    custval[10] != "" &&
                    custval[10].trim().length > 0
                    ) {
                stateval = custval[10];
                console.log("stateval==> " + stateval);
                stateval = stateval.split("-")[0];
                console.log("stateval==> " + stateval);
            }

            headerShipper =
                    custval[2] +
                    " " +
                    custval[9] +
                    " " +
                    custval[3] +
                    " " +
                    stateval +
                    " " +
                    custval[11] +
                    " " +
                    custval[4].toUpperCase();
            if (custval[7] != "") {
                headerShipper = headerShipper + " T: " + custval[7];
            }
            if (custval[8] != "") {
                headerShipper = headerShipper + " F: " + custval[8];
            }
            if (custval[6] != "") {
                headerShipper = headerShipper + " Email:: " + custval[6];
            }

            if (
                    custval[12] != null &&
                    custval[12] != "null" &&
                    custval[12] != "" &&
                    custval[12].trim().length > 0
                    ) {
                headerShipper = headerShipper + " Tax Id: " + custval[12];
                console.log("taxval==> " + custval[12]);
            }

            document.getElementById("ebook_" + type + "nameheader").value =
                    custval[0];
            document.getElementById("header" + type).value = headerShipper;
            if($("#" +"ebook_" + type + "eori").length > 0 &&     custval[14] != null ){
                document.getElementById("ebook_" + type + "eori").value = custval[14];
            }
            if($("#" +type + "typeperson").length > 0 && custval[13] != null ){
                    document.getElementById( type + "typeperson").value = custval[13];
            }
            console.log(">>" + custval[15]);
            if($("#" +"ebook_" + type + "zipcode").length > 0 && custval[15] != null ){
                document.getElementById("ebook_" + type + "zipcode").value = custval[15];
            }

        } else if (type == "shipper" || type == "consignee" || type == "notify") {
            $("#header" + type).attr("readonly", false);
            $("#ebook_" + type + "nameheader").attr("readonly", true);
            var stateval = "";
            if (
                    custval[10] != null &&
                    custval[10] != "null" &&
                    custval[10] != "" &&
                    custval[10].trim().length > 0
                    ) {
                stateval = custval[10];
                console.log("stateval==> " + stateval);
                stateval = stateval.split("-")[0];
                console.log("stateval==> " + stateval);
            }
            headerShipper =
                    custval[2] +
                    " " +
                    custval[9] +
                    " " +
                    custval[3] +
                    " " +
                    stateval +
                    " " +
                    custval[11] +
                    " " +
                    custval[4].toUpperCase();
            if (custval[7] != "") {
                headerShipper = headerShipper + " T: " + custval[7];
            }
            if (custval[8] != "") {
                headerShipper = headerShipper + " F: " + custval[8];
            }
            if (custval[6] != "") {
                headerShipper = headerShipper + " Email:: " + custval[6];
            }

            if (
                    custval[12] != null &&
                    custval[12] != "null" &&
                    custval[12] != "" &&
                    custval[12].trim().length > 0
                    ) {
                console.log("taxval===> " + custval[12]);

                headerShipper = headerShipper + " Tax Id: " + custval[12];
            }
            document.getElementById("ebook_" + type + "nameheader").value =
                    custval[0];
            document.getElementById("header" + type).value = headerShipper;
        }

        $("#ebook_" + type + "country_name").typeahead("destroy");
        document.getElementById("ebook_" + type + "name").value = custval[0];
        document.getElementById(type + "CustCode").value = custval[1];
        document.getElementById("ebook_" + type + "name").value = custval[0];
        document.getElementById("ebook_" + type + "address").value = custval[2];
        document.getElementById("ebook_" + type + "email").value = custval[6];
        document.getElementById("ebook_" + type + "country_name").value =
                custval[4].toUpperCase();
        document.getElementById("ebook_" + type + "country").value = custval[5];
        document.getElementById("ebook_" + type + "phone").value = custval[7];
        document.getElementById("ebook_" + type + "fax").value = custval[8];
        document.getElementById("ebook_" + type + "address1").value = custval[9];
        document.getElementById("ebook_" + type + "city").value = custval[3];
        document.getElementById("ebook_" + type + "city_desc").value = custval[3];
        if ($("#" +"ebook_" + type + "zipcode").length > 0) {
            $("#" +"ebook_" + type + "zipcode").val(custval[15]);
        }

        callAutocompleteintialize("#ebook_" + type + "country_name");
        $("#ebook_" + type + "country_name").css("background-color", "white");
        $(
                "#ebook_consigneename-error,#ebook_consigneeaddress-error,#ebook_consigneecountry_name-error,#ebook_consigneecity-error"
                ).html("");
        $(
                "#ebook_consigneephone-error,#ebook_consigneeemail-error,#ebook_consigneefax-error"
                ).html("");
        $("#ebook_" + type + "country_name").trigger("change");

        document.getElementById("ebook_" + type + "state").value = custval[10];
        $("#ebook_" + type + "state").typeahead("val", custval[10]);
        if ($("#" + type + "typeperson").length > 0) {
            $("#" + type + "typeperson").val(custval[13]);
        }
        if ($("#ebook_" + type + "eori").length > 0) {
            $("#ebook_" + type + "eori").val(custval[14]);
        }

    }
}
function checkToOrder(custtype, id) {
    $(
            "#ebook_" +
            custtype +
            "name-error,#ebook_" +
            custtype +
            "address-error,#ebook_" +
            custtype +
            "address1-error,#ebook_" +
            custtype +
            "country_name-error"
            ).hide();
    $(
            "#ebook_" +
            custtype +
            "city-error,#ebook_" +
            custtype +
            "phone-error,#ebook_" +
            custtype +
            "fax-error,#ebook_" +
            custtype +
            "email-error"
            ).hide();
    $(
            "#" +
            custtype +
            "CustCode,#ebook_" +
            custtype +
            "name,#ebook_" +
            custtype +
            "address,#ebook_" +
            custtype +
            "address1,#ebook_" +
            custtype +
            "city,#ebook_" +
            custtype +
            "country_name,#ebook_" +
            custtype +
            "phone,#ebook_" +
            custtype +
            "fax,#ebook_" +
            custtype +
            "email"
            ).val("");
    $("#ebook_" + custtype + "country_name").css("background-color", "white");
    $("#" + custtype + "newcustimg").html(
            '<i class="fa fa-pencil-square" id="ccupdateimg"></i>  New'
            );
    $("#ebook_" + custtype + "nameheader").val("");
    $("#header" + custtype).val("");
    $("#" + custtype + "newcustimgtype").val("new");
    $("#" + custtype + "Select option:selected").text("Select Customer");
    callAutocompleteintialize("#ebook_" + custtype + "country_name");
    $("#ebook_" + custtype + "name").focus();
    if ($("#" + id).is(":checked")) {
        $("#ebook_consigneenameheader,#headerconsignee").attr("readonly", false);
        $("#" + custtype + "Select,#" + custtype + "search").attr("disabled", true);
        $("#notifyconsig,#notify2consig,#notify3consig").attr("disabled", true);
        $("#" + custtype + "newcustimg").attr("disabled", true);
        $("#" + custtype + "ordercheck").val("Y");
        $("#headerconsignee").removeClass("required");
    } else {
        if ($("#ebook_consigneenameheader").val() == "") {
            //$('#ebook_consigneenameheader,#headerconsignee').attr("readonly", true);
            $("#ebook_consigneenameheader").attr("readonly", true);
            $("#headerconsignee").attr("readonly", false);
        } else {
            $("#ebook_consigneenameheader,#headerconsignee").attr("readonly", false);
        }
        $("#" + custtype + "Select,#" + custtype + "search").attr(
                "disabled",
                false
                );
        $("#notifyconsig,#notify2consig,#notify3consig").attr("disabled", false);
        $("#" + custtype + "newcustimg").attr("disabled", false);
        $("#" + custtype + "ordercheck").val("N");
        $("#headerconsignee").addClass("required");
    }
}
function setSISameAsConsignee(custtype, id) {
    var checkValues = false;
    if ($("#" + id).is(":checked")) {
        if (
                $("#ebook_consigneename").val() != "" &&
                $("#ebook_consigneeaddress").val() != "" &&
                $("#ebook_consigneecountry_name").val() != "" &&
                $("#ebook_consigneecity").val() != ""
                ) {
            checkValues = true;
        } else {
            checkValues = false;
        }
        if (custtype == "notify") {
            checkValues = false;
            if (
                    $("#ebook_consigneenameheader").val() != "" &&
                    $("#headerconsignee").val() != ""
                    ) {
                checkValues = true;
            }
        }
        if (checkValues) {
            //$('#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity,#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail').attr("readonly", true);
            $(
                    "#ebook_" +
                    custtype +
                    "country_name,#ebook_" +
                    custtype +
                    "city,#ebook_" +
                    custtype +
                    "city_desc,#ebook_" +
                    custtype +
                    "name,#ebook_" +
                    custtype +
                    "address,#ebook_" +
                    custtype +
                    "address1,#ebook_" +
                    custtype +
                    "phone,#ebook_" +
                    custtype +
                    "fax,#ebook_" +
                    custtype +
                    "email"
                    ).attr("readonly", true);
            $("#ebook_" + custtype + "country_name").val(
                    $("#ebook_consigneecountry_name").val()
                    );
            $("#ebook_" + custtype + "country").val(
                    $("#ebook_consigneecountry").val()
                    );
            $("#ebook_" + custtype + "city").val($("#ebook_consigneecity").val());
            $("#ebook_" + custtype + "city_desc").val(
                    $("#ebook_consigneecity_desc").val()
                    );
            $("#ebook_" + custtype + "name").val($("#ebook_consigneename").val());
            $("#ebook_" + custtype + "address").val(
                    $("#ebook_consigneeaddress").val()
                    );
            $("#ebook_" + custtype + "address1").val(
                    $("#ebook_consigneeaddress1").val()
                    );
            $("#ebook_" + custtype + "phone").val($("#ebook_consigneephone").val());
            $("#ebook_" + custtype + "fax").val($("#ebook_consigneefax").val());
            $("#ebook_" + custtype + "email").val($("#ebook_consigneeemail").val());
            $("#ebook_" + custtype + "state").val($("#ebook_consigneestate").val());
            $("#ebook_" + custtype + "state").typeahead(
                    "val",
                    $("#ebook_consigneestate").val()
                    );
            $("#" + custtype + "CustCode").val($("#consigneeCustCode").val());
            $("#consigneesearch").attr("disabled", true);
            $("#" + custtype + "search").attr("disabled", true);
            $("#consigneeorder").attr("disabled", true);
            $("#" + custtype + "Check").val("Y");
            $("#" + custtype + "order,#" + custtype + "Select,#consigneeSelect").attr(
                    "disabled",
                    true
                    );
            $("#ebook_" + custtype + "country_name").css("background-color", "#eee");
            if (custtype == "notify") {
                $("#consigneenewcustimg,#notifynewcustimg").attr("disabled", true);
                $("#ebook_" + custtype + "nameheader").val(
                        $("#ebook_consigneenameheader").val()
                        );
                $("#header" + custtype).val($("#headerconsignee").val());
                $("#ebook_" + custtype + "nameheader").attr("readonly", true);
                $("#header" + custtype).attr("readonly", true);
                $("#" + custtype + "Select option:selected").text("Select Customer");
                $("#headerconsignee").attr("readonly", true);
            }
            if (
                    $("#ebook_consigneeeori") &&
                    $("#ebook_consigneeeori").length > 0
                    ) {
                $("ebook_" + custtype + "eori").val($("#ebook_consigneeeori").val());
                $("ebook_" + custtype + "persontype").val($("#ebook_consigneepersontype").val());
            }
        } else {
            alert(consDet);
            $("#" + custtype + "consig").prop("checked", false);
            checkValues = false;
            return false;
        }
    } else {
        $("#" + custtype + "Check").val("N");
        $(
                "#" +
                custtype +
                "CustCode,#ebook_" +
                custtype +
                "name,#ebook_" +
                custtype +
                "address,#ebook_" +
                custtype +
                "address1,#ebook_" +
                custtype +
                "city,#ebook_" +
                custtype +
                "country_name,#ebook_" +
                custtype +
                "phone,#ebook_" +
                custtype +
                "fax,#ebook_" +
                custtype +
                "email"
                ).val("");
        $("#" + custtype + "search").attr("disabled", false);
        $(
                "#ebook_" +
                custtype +
                "name,#ebook_" +
                custtype +
                "address,#ebook_" +
                custtype +
                "address1,#ebook_" +
                custtype +
                "city,#ebook_" +
                custtype +
                "country_name,#ebook_" +
                custtype +
                "phone,#ebook_" +
                custtype +
                "fax,#ebook_" +
                custtype +
                "email"
                ).attr("readonly", false);
        $("#" + custtype + "ordercheck").val("N");
        $("#ebook_" + custtype + "country_name").css("background-color", "white");
        $("#" + custtype + "Select").attr("disabled", false);
        $("#" + custtype + "order").attr("disabled", false);
        $("#" + custtype + "order").prop("checked", false);
        if (
                $("#notifyCheck").val() != "Y" &&
                $("#notify2Check").val() != "Y" &&
                $("#notify3Check").val() != "Y"
                ) {
            $(
                    "#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecity,#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate"
                    ).attr("readonly", false);
            if ($("#consigneeorder").is(":checked")) {
                $("#consigneesearch").attr("disabled", true);
            } else {
                $("#consigneesearch,#consigneeSelect").attr("disabled", false);
            }
            $("#ebook_consigneecountry_name").css("background-color", "white");

            $("#consigneeorder").attr("disabled", false);
        }
        if (custtype == "notify") {
            $("#consigneenewcustimg,#notifynewcustimg").attr("disabled", false);
            $("#ebook_" + custtype + "nameheader").val("");
            $("#header" + custtype).val("");
            $("#ebook_" + custtype + "nameheader").attr("readonly", false);
            $("#header" + custtype).attr("readonly", false);
            $("#" + custtype + "Select option:selected").text("Select Customer");
            $("#headerconsignee").attr("readonly", false);

            if ($("#ebook_shippernameheader").prop("readonly") == true) {
                $("#ebook_notifynameheader").prop("readonly", true);
            }

            if ($("#headershipper").prop("readonly") == true) {
                $("#headerconsignee,#headernotify").prop("readonly", true);
            }
        }
        if (
                $("#ebook_consigneeeori") &&
                $("#ebook_consigneeeori").length > 0) {
            $("ebook_" + custtype + "eori").val("");
            $("ebook_" + custtype + "persontype").val("");
        }
    }
}
function shownewcustmodal(type) {
    var newcustaddtype = "";
    newcustaddtype = $("#" + type + "newcustimgtype").val();
    //alert(newcustaddtype);
    if (type === "consignee") {
        $("#newcustemail").addClass("required");
        $("#newcustemailmand").removeClass("d-none");
    } else {
        $("#newcustemail").removeClass("required");
        $("#newcustemailmand").addClass("d-none");
    }
    newCustModalReset();
    if (newcustaddtype == "new") {
    } else {
        //
        $("#newcustname").val($("#ebook_" + type + "name").val());
        $("#" + type + "CustCode").val("");
        $("#newcustaddr").val($("#ebook_" + type + "address").val());
        $("#newcustemail").val($("#ebook_" + type + "email").val());
        $("#newcustcountry")
                .val($("#ebook_" + type + "country_name").val())
                .trigger("change");
        $("#newcustcountry").typeahead(
                "val",
                $("#ebook_" + type + "country_name").val()
                );
        $("#newcustphone").val($("#ebook_" + type + "phone").val());
        $("#newcustfax").val($("#ebook_" + type + "fax").val());
        $("#newcustaddr1").val($("#ebook_" + type + "address1").val());
        $("#newcustcity").val($("#ebook_" + type + "city").val());
        $("#newcustcity").val($("#ebook_" + type + "city_desc").val());
        $("#newcuststate").typeahead("val", $("#ebook_" + type + "state").val());
        $("#newcusteori").val($("#ebook_" + type + "eori").val());
        $("#newcusttypeperson").val($("#" + type + "typeperson").val());
        if ($("#ebook_" + type + "zipcode").length > 0) {
            $("#newcustzipcode").val($("#ebook_" + type + "zipcode").val());
        }
        $("#" + type + "Select").val("");
    }
    $("#newcusttype").val(type);
    $("#newcustmodal").modal("show");
}

function newCustModalReset() {
    $("#newcustform label.error").remove();
    $(
            "#newcustname,#newcustaddr,#newcustaddr1,#newcustcity,#newcustcountry,#newcustphone,#newcustfax,#newcustemail,#newcuststate,#newcustcountrycode,#newcusteori,#newcusttypeperson"
            ).val("");
    if ($("#newcustzipcode").length > 0) {
        $("#newcustzipcode").val("");
    }
    $("#newcuststate").typeahead("val", "");
    $("#newcustcountry").typeahead("val", "");
    $("#newcuststate").typeahead("destroy");
    // $('#newcusttype').val('');
}
function addNewCustomer(printOnBlAddMand) {
    var type = $("#newcusttype").val();
    var country = $("#newcustcountry").val().split("-");
    var countryname = trim(country[1]);
    country = trim(country[0]);

    if (!$("#newcuststate").val().includes("-")) {
        $("#newcuststate").typeahead("val", "");
        $("#newcuststate").val("");
    }
    var headerShipper = "";
    var stateval = "";
    if (
            $("#newcuststate").val() != null &&
            $("#newcuststate").val() != "null" &&
            $("#newcuststate").val() != "" &&
            $("#newcuststate").val().trim().length > 0
            ) {
        stateval = $("#newcuststate").val();
        stateval = stateval.split("-")[0];
    }
    headerShipper =
            $("#newcustaddr").val() +
            " " +
            $("#newcustaddr1").val() +
            " " +
            $("#newcustcity").val() +
            " " +
            stateval +
            " " +
            ($("#newcustzipcode").length > 0 ? $("#newcustzipcode").val() + " " : "") +
            trim(countryname);
    if ($("#newcustphone").val() != "") {
        headerShipper = headerShipper + " T: " + $("#newcustphone").val();
    }
    if ($("#newcustfax").val() != "" && $("#newcustfax").length > 0) {
        headerShipper = headerShipper + " F: " + $("#newcustfax").val();
    }
    if ($("#newcustemail").val() != "") {
        headerShipper = headerShipper + " Email: " + $("#newcustemail").val();
    }

    $("#ebook_" + type + "nameheader").val($("#newcustname").val());
    $("#header" + type).val(headerShipper);
    $("#ebook_" + type + "name").val($("#newcustname").val());
    $("#" + type + "CustCode").val("");
    $("#ebook_" + type + "address").val($("#newcustaddr").val());
    $("#ebook_" + type + "email").val($("#newcustemail").val());
    $("#ebook_" + type + "country_name").val($("#newcustcountry").val());
    $("#ebook_" + type + "country").val(country);
    $("#ebook_" + type + "phone").val($("#newcustphone").val());
    $("#ebook_" + type + "fax").val($("#newcustfax").val());
    $("#ebook_" + type + "address1").val($("#newcustaddr1").val());
    $("#ebook_" + type + "city").val($("#newcustcity").val());
    $("#ebook_" + type + "city_desc").val($("#newcustcity").val());
    $("#ebook_" + type + "state").val($("#newcuststate").val());
    $("#ebook_" + type + "eori").val($("#newcusteori").val());
    if ($("#newcustzipcode").length > 0) {
        $("#ebook_" + type + "zipcode").val($("#newcustzipcode").val());
    }
    $("#" + type + "typeperson").val($("#newcusttypeperson").val());
    console.log(
            $("#ebook_" + type + "eori").val() +
            "===" +
            $("#ebook_" + type + "persontype").val()
            );
    $("#" + type + "Select").val("");
    $("#" + type + "newcustimgtype").val("edit");
    $("#" + type + "newcustimg").html(
            '<i class="fa fa-pencil-square" id="ccupdateimg"></i>  Edit'
            );
    $("#newcustmodal").modal("hide");
    if (printOnBlAddMand == "y" || printOnBlAddMand == "Y") {
        $("#header" + type).attr("readonly", true);
        $("#ebook_" + type + "nameheader").attr("readonly", true);
    } else {
        $("#header" + type).attr("readonly", false);
        $("#ebook_" + type + "nameheader").attr("readonly", true);
    }
}
function setCustHeaderInfo(type) {
    var name = $("#ebook_" + type + "nameheader").val();
    var headerval = $("#header" + type).val();
    var headerdet = "";
    var country = $("#ebook_" + type + "country_name")
            .val()
            .split("-");
    if (country.length > 1) {
        country = trim(country[1]);
    }
    if (name == null || name == "") {
        //console.log('header name value>>'+$('#ebook_' + type + 'name').val());
        $("#ebook_" + type + "nameheader").val($("#ebook_" + type + "name").val());
    }
    var stateval = "";
    if (
            $("#ebook_" + type + "state").val() != null &&
            $("#ebook_" + type + "state").val() != "null" &&
            $("#ebook_" + type + "state").val() != "" &&
            $("#ebook_" + type + "state")
            .val()
            .trim().length > 0
            ) {
        stateval = $("#ebook_" + type + "state").val();
        stateval = stateval.split("-")[0];
    }
    if (headerval == null || headerval == "") {
        headerdet =
                $("#ebook_" + type + "address").val() +
                " " +
                $("#ebook_" + type + "address1").val() +
                " " +
                $("#ebook_" + type + "city").val() +
                " " +
                stateval +
                " " +
                ($("#ebook_" + type + "zipcode").length > 0 ? $("#ebook_" + type + "zipcode").val() + " " : "") +
                country;
           console.log("header det value>>" + headerdet);
        if ($("#ebook_" + type + "phone").val() != "") {
            headerdet = headerdet + " T: " + $("#ebook_" + type + "phone").val();
        }
        if ($("#newcustfax").val() != "" && $("#newcustfax").length > 0) {
            headerdet = headerdet + " F: " + $("#ebook_" + type + "fax").val();
        }
        if ($("#ebook_" + type + "email").val() != "") {
            headerdet = headerdet + " Email: " + $("#ebook_" + type + "email").val();
        }
        $("#header" + type).val(headerdet);
    }
    if (
            $("#" + type + "CustCode").val() == "" &&
            $("#ebook_" + type + "name").val() != ""
            ) {
        $("#" + type + "newcustimg").html(
                '<i class="fa fa-pencil-square" id="ccupdateimg"></i>  Edit'
                );
        $("#" + type + "newcustimgtype").val("edit");
    }
    if (trim(name) == "" && trim($("#ebook_" + type + "name").val()) == "") {
        $("#header" + type).attr("readonly", true);
        $("#ebook_" + type + "nameheader").attr("readonly", true);
    }
}
function enableHeaderInfo(type, printOnBlAddMand) {
    if (printOnBlAddMand == "y" || printOnBlAddMand == "Y") {
        $("#header" + type).attr("readonly", true);
        $("#ebook_" + type + "nameheader").attr("readonly", true);
    } else {
        $("#header" + type).attr("readonly", false);
        $("#ebook_" + type + "nameheader").attr("readonly", true);
    }
}
function convertWeightUnit(value, fromUnit, toUnit) {
    if (value != "" && value.length > 0) {
        if (fromUnit == "K") {
            if (toUnit == "T") {
                value = parseFloat(value) / 1000.0;
            } else if (toUnit == "P") {
                value = parseFloat(value) * 2.20462;
            }
        } else if (fromUnit == "T") {
            if (toUnit == "K") {
                value = parseFloat(value) * 1000.0;
            } else if (toUnit == "P") {
                value = parseFloat(value) * 2204.6226200000001;
            }
        } else if (fromUnit == "P") {
            if (toUnit == "K") {
                value = parseFloat(value) / 2.20462;
            } else if (toUnit == "T") {
                value = parseFloat(value) / 2204.6226200000001;
            }
        }
        value = parseFloat(value).toFixed(3);
    }
    return value;
}

function convertWeight(cno, id) {
    var cargoWt = $("#weight" + id); //trigger onblur
    var tareWt = $("#tareweight" + cno);
    var netWt = $("#netweight" + id);
    var cargoWtHid = document.getElementById(cno + "#cargoweight#");
    //var netWtHid = document.getElementById(cno + '#netweight#');
    var grossWtHid = document.getElementById(cno + "#grossweight#");
    var tareWtLabel = $("#twt" + cno);
    var newcargoWt = $("#newwtid" + id);
    var newnetWt = $("#newntid" + id);
    var tareWtUnitHid = $("#tareweighthidunit" + cno);
    var newunit = $("#unit" + id).val();
    var oldunit = $("#unit" + id + "_previous").val();

    cargoWt
            .val(convertWeightUnit(cargoWt.val(), oldunit, newunit))
            .trigger("onblur");
    netWt.val(convertWeightUnit(netWt.val(), oldunit, newunit));
    tareWt.val(convertWeightUnit(tareWt.val(), oldunit, newunit));

    cargoWtHid.value = convertWeightUnit(cargoWtHid.value, oldunit, newunit);
    grossWtHid.value = convertWeightUnit(grossWtHid.value, oldunit, newunit);
    //netWtHid.value = convertWeightUnit(netWtHid.value, oldunit, newunit);
    tareWtLabel.html(convertWeightUnit(tareWtLabel.html(), oldunit, newunit));
    newcargoWt.val(convertWeightUnit(newcargoWt.val(), oldunit, newunit));
    newnetWt.val(convertWeightUnit(newnetWt.val(), oldunit, newunit));

    tareWtUnitHid.val(newunit);
    $("#unit" + id + "_previous").val(newunit);
}

function calculateGrossWeight(cno, id) {
    var cargoWt = $("#weight" + id).val();
    var tareWt = $("#tareweight" + cno).val();
    var grossWtLabel = $("#gwt" + cno);
    var grosswtprev = $("#grossweight" + cno);
    var tarewtsplit = tareWt.split(".");
    var cargowtsplit = cargoWt.split(".");
    var tardot = 0,
            cargodot = 0;
    if (tarewtsplit.length > 1) {
        tardot = tarewtsplit[1].length;
    }
    if (cargowtsplit.length > 1) {
        cargodot = cargowtsplit[1].length;
    }
    if (cargodot < tardot) {
        cargodot = tardot;
    }
    console.log("calculateGrossWeight cargoWt" + cargoWt);
    var grossweight = (parseFloat(cargoWt) + parseFloat(tareWt)).toFixed(
            cargodot
            );
    console.log(
            "calculateGrossWeight parseFloat(cargoWt) -" + parseFloat(cargoWt)
            );
    grossWtLabel.html(grossweight);
    var grossWtHid = document.getElementById(cno + "#grossweight#");
    grossWtHid.value = grossweight;

    $("#grossweight" + cno).val(grossweight);
}
function PayLoadValidation(containernoid, eqptypeid) {
    var id = containernoid.replace("containernosicontainerid", "");
    var i = 1;
    var cargowt = 0;
    for (i = 1; i < 100; i++) {
        if ($("#weightsicontainerid" + id + i).length == 0) {
            break;
        } else {
            cargowt = cargowt + parseFloat($("#weightsicontainerid" + id + i).val());
            console.log("PayLoadValidation cargoWt" + cargowt);
        }
    }
    var tarewt = $("#tareweightsicontainerid" + id).val();
    var grosswt = cargowt + parseFloat(tarewt);
    var wtunit = $("#unitsicontainerid" + id + "1").val();
    var containerno = $("#" + containernoid).val();
    var eqptype = $("#" + eqptypeid)
            .html()
            .trim();
    var errormsg = "";
    console.log(
            "cargowt=" +
            cargowt +
            "&grosswt=" +
            grosswt +
            "&wtunit=" +
            wtunit +
            "&containerno=" +
            containerno +
            "&eqptype=" +
            eqptype
            );
    if (!(isNaN(cargowt) && isNaN(grosswt))) {
        console.log("PayLoadValidation !isNaN cargoWt" + cargowt);
        $.ajax({
            type: "POST",
            cache: false,
            url:
                    "PayloadValidationAjax?cargowt=" +
                    cargowt +
                    "&grosswt=" +
                    grosswt +
                    "&wtunit=" +
                    wtunit +
                    "&containerno=" +
                    containerno +
                    "&eqptype=" +
                    eqptype,

            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                if (data != null && data.length > 0) {
                    console.log("PayloadValidationAjax !isNaN cargoWt" + cargowt);
                    var res = data.split("~~");
                    var status = res[0];
                    if (status == "invalid") {
                        if ("T" == wtunit) {
                            errormsg =
                                    res[1] +
                                    " weight should be less than or equal to " +
                                    res[2] +
                                    " Tons";
                            errormsg = errormsg.replace("50", res[2]);
                        } else if ("P" == wtunit) {
                            errormsg =
                                    res[1] +
                                    " weight should be less than or equal to " +
                                    res[2] +
                                    " Pounds";
                            errormsg = errormsg.replace("1,00,000", res[2]);
                        } else if ("K" == wtunit) {
                            errormsg =
                                    res[1] +
                                    " weight should be less than or equal to " +
                                    res[2] +
                                    " Kilos";
                        }
                        alert(errormsg);
                        for (i = 1; i < 100; i++) {
                            if ($("#weightsicontainerid" + id + i).length == 0) {
                                break;
                            } else {
                                cargowt =
                                        cargowt +
                                        parseFloat($("#weightsicontainerid" + id + i).val(""));
                                console.log("cargowt !cargowt cargoWt" + cargowt);
                            }
                        }
                    }
                }
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            },
        });
    }
}
    function setCityDesc_New(field1, field2) {
        if (document.getElementById(field1).value != "") {
            document.getElementById(field2).value = document.getElementById(field1).value;
        }
    }
function loadStateAutoComplete(countryid, stateid, hiddenid, type) {
    $("#" + stateid).typeahead("destroy");

    if (
        $("#" + countryid).length > 0 &&
        $("#" + countryid).val() !== "" &&
        $("#" + countryid).val().length > 0
    ) {
        if (type !== "load") {
            $("#" + stateid).val("");
            $("#" + stateid).typeahead("val", "");
            $("#" + hiddenid).val("");
        }

        var countryCode = ($("#" + countryid).val().split("-")[0] || "").trim();

        var statejs = StateJson.filter(function (item) {
            return item["CountryCode"] === countryCode;
        });

        var states = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace("StateName", "StateCode"),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: statejs,
        });

        states.initialize();

        $("#" + stateid)
            .typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2,
                },
                {
                    name: "state",
                    limit: 5,
                    displayKey: function (state) {
                        return state.StateCode + " - " + state.StateName;
                    },
                    source: states.ttAdapter(),
                }
            )
            .on("typeahead:render", function () {
                $("#" + hiddenid).val("");
            })
            .on("typeahead:selected", function (obj, datum) {
                $("#" + hiddenid).val(datum.StateName);
            })
            .on("typeahead:autocomplete", function (obj, datum) {
                $("#" + hiddenid).val(datum.StateName);
            })
            .on("typeahead:change", function (obj) {
                var hiddenValue = $("#" + hiddenid).val();
                if (!hiddenValue) {
                    $("#" + stateid).typeahead("val", "");
                    $(obj.currentTarget).val("");
                }
            });
    } else {
        $("#" + stateid).val("");
        $("#" + stateid).typeahead("val", "");
        $("#" + hiddenid).val("");
    }

//    loadtypeaheadtooltipmsg();
}

function downloadfile(filename) {
    var refno = $("#sino").val();
    var type = "Ecom";
    if (refno == null || refno == "" || refno.length == 0) {
        refno = $("#blno").val();
        type = "BL";
    }
    $("#BKGfilename").val(filename);
    $("#BKGtype").val(type);
    $("#BKGrefno").val(refno);
    $("#filedownloadform").attr("action", "FileAjax");
    $("#filedownloadform").submit();
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
        alert("For each segment only three files can be attached!!");
    }
}
function clearCustHsCode(id) {
    $("#" + id).val("");
}

function validateState(countryid, obj) {
    if (
            $("#" + countryid).length == 0 ||
            $("#" + countryid).val() == "" ||
            $("#" + countryid).val().length == 0
            ) {
        $(obj).val("");
        alert("Please select the country");
    }
}
function convertChargedetailstoJson() {
    var tbl = $("#datatablebychargetbl");
    var tblhead = $(tbl).find("thead");
    var tblbody = $(tbl).find("tbody");
    var header = [];
    var JObjectArray = [];
    $.each($(tblhead).find("tr>th"), function (i, j) {
        header.push($(j).attr("title").toLowerCase());
    });
    $.each($(tblbody).find("tr"), function (key, value) {
        var jObject = {};
        for (var x = 0; x < header.length; x++) {
            if ($(this).find("td").eq(x).find("select").is("select")) {
                jObject[header[x]] = $(this).find("td").eq(x).find("select").val();
            } else {
                jObject[header[x]] = $(this).find("td").eq(x).text();
            }
        }
        JObjectArray.push(jObject);
    });
    var jsonarraystr = JSON.stringify(JObjectArray);
    $("#ChargeDetailsJSON").val(jsonarraystr);
}
function setPayor(obj) {
    var value = $(obj).val();
    $(obj).parents("tr").find(".SIPayor").html("");
    if (value == "P") {
        $(obj)
                .parents("tr")
                .find(".SIPayor")
                .append('<option value="">Select</option>')
                .append('<option value="AP">Agr. Party</option>')
                .append('<option value="SH">Shipper</option>')
                .append('<option value="FW">Forwarder</option>');
    } else if (value == "C") {
        $(obj)
                .parents("tr")
                .find(".SIPayor")
                .append('<option value="">Select</option>')
                .append('<option value="FW">Forwarder</option>')
                .append('<option value="CN">Consignee</option>')
                .append('<option value="NT">Notify</option>');
    } else if (value == "E") {
        $(obj)
                .parents("tr")
                .find(".SIPayor")
                .append('<option value="">Select</option>')
                .append('<option value="AP">Agr. Party</option>');
    } else {
        $(obj)
                .parents("tr")
                .find(".SIPayor")
                .append('<option value="">Select</option>');
    }
}
var globalobj;
function applycharge(obj) {
    globalobj = obj;
    var frieght = $(obj).parents("tr").find(".SIfrieght").val();
    var payor = $(obj).parents("tr").find(".SIPayor").val();
    if (frieght == "" || payor == "") {
        alert("Frieght/Payor is missing,kindly select the values.");
    } else {
        if (frieght == "E") {
            $.ajax({
                type: "POST",
                cache: false,
                url:
                        "SIAjax?type=ThirdParty&SIfrieght=" +
                        frieght +
                        "&SIPayor=" +
                        payor +
                        "&lrpbookno=" +
                        $("#lrpbookno").val(),
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    $.fancybox.open(data, {
                        showCloseButton: false,
                        closeClick: false,
                        maxWidth: 1200,
                        maxHeight: 600,
                        fitToView: true,
                        width: "100%",
                        height: "100%",
                        autoSize: true,
                        scrollOutside: true,
                        hideOnOverlayClick: false,
                        overlayShow: false,
                        helpers: {
                            overlay: {
                                closeClick: false,
                            },
                        },
                        keys: {
                            close: null,
                        },
                    });
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                },
            });
        } else {
            $.ajax({
                type: "POST",
                cache: false,
                url:
                        "SIAjax?type=PrepaidCollect&SIfrieght=" +
                        frieght +
                        "&SIPayor=" +
                        payor +
                        "&lrpbookno=" +
                        $("#lrpbookno").val(),
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    console.log("DATA==> " + data);
                    setchargeDetails(frieght, payor, data);
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                },
            });
        }
    }
}

function setchargeDetails(frieght, payor, location) {
    $(globalobj).parents("tr").find(".payagencyid").html(location);
    $.fancybox.close();
}
function checkSIContainerNoValidation(sicontnostrlis) {
    var resultdate;
    $.ajax({
        type: "POST",
        cache: false,
        url:
                "SIAjax?type=SIContainernovalidation&lrpbookno=" +
                $("#lrpbookno").val() +
                "&containerlisstr=" +
                sicontnostrlis,
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            resultdate = data;
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        },
    });
    return resultdate;
}

function cargodescvalidation(obj) {
    var value = $(obj).val();
    var patt = new RegExp("[a-zA-Z]");
    if (
            value.toUpperCase() == "NIL" ||
            value.toUpperCase() == "NA" ||
            value.toUpperCase() == "N/A" ||
            value.toUpperCase() == "NA" ||
            value.toUpperCase() == "-" ||
            value.toUpperCase() == "." ||
            !patt.test(value)
            ) {
        $(obj).val("");
    }
}

function hscodevalidation(obj) {
    var value = $(obj).val();
    var patt = new RegExp("[a-zA-Z0-9]");
    if (
            value.toUpperCase() == "NIL" ||
            value.toUpperCase() == "NA" ||
            value.toUpperCase() == "N/A" ||
            value.toUpperCase() == "NA" ||
            value.toUpperCase() == "-" ||
            value.toUpperCase() == "." ||
            !patt.test(value)
            ) {
        $(obj).val("");
    }
}

var alluncheckedVals = [];
function ExportDataCustomize() {
    $(".selectcls").attr("disabled", false);
    $(".socslt").attr("disabled", false);
    alluncheckedVals = [];
    var noncheckid = "";
    $("[name=select]:not(:checked)").each(function () {
        //  $("input:checkbox:not(:checked)").each(function () {
        // alert("Id: " + $(this).attr("id") + " Value: " + $(this).val());                                     noncheckid = $(this).attr("id");
        document.getElementById(noncheckid).checked = true;
        alluncheckedVals.push($(this).attr("id"));
    });
    var jsonval = JSON.stringify($("form").serializeObject());
    //  console.log(jsonval);
    $.ajax({
        url: "SICustomizeExportExcel?formval=sessionjson",
        type: "POST",
        data: {formjonval: jsonval},
        dataType: "json",
        success: function (result) {
            getExcelCustomize();
        },
        error: function (result) {
            getExcelCustomize();
        },
    });
}

function getExcelCustomize() {
    var noncheckid = "";
    var j = 0;
    for (j = 0; j < alluncheckedVals.length; j++) {
        noncheckid = alluncheckedVals[j];
        document.getElementById(noncheckid).checked = false;
    }
    alluncheckedVals = [];
    var url = "SICustomizeExportExcel?formval=downloadexcel";
    document.getElementById("SIEditForm").action = url;
    document.getElementById("SIEditForm").submit();
    $(".selectcls").attr("disabled", true);
    $(".socslt").attr("disabled", true);
}
function selectallcharges() {
    if ($("#chargescheckboxall").prop("checked")) {
        $("#datatablebychargetbl")
                .find("tbody")
                .find("tr")
                .find("td")
                .find(".chargescheckbox")
                .prop("checked", true);
    } else {
        $("#datatablebychargetbl")
                .find("tbody")
                .find("tr")
                .find("td")
                .find(".chargescheckbox")
                .prop("checked", false);
    }
}

function copycharges() {
    var chargecopyflag = true;
    if ($(".chargescheckbox:checked").length == 1) {
        $("#datatablebychargetbl")
                .find("tbody")
                .find("tr")
                .each(function () {
                    if ($(this).find(".chargescheckbox").prop("checked")) {
                        $("#copySIfrieght").val($(this).find(".SIfrieght").val());
                        $("#copySIPayor").val($(this).find(".SIPayor").val());
                        $("#copySIlocation").val($(this).find(".payagencyid").text());
                        chargecopyflag = false;
                        toastr.success("", "Copied");
                    }
                });
        if (chargecopyflag) {
            alert("Kindly select a row to copy.");
        }
        $("#datatablebychargetbl")
                .find("tbody")
                .find("tr")
                .find(".chargescheckbox")
                .prop("checked", false);
        $("#chargescheckboxall").prop("checked", false);
    } else {
        alert("Kindly select a row to copy.");
    }
}

function applyPaste() {
    var chargecopyflag = true;
    if ($("#copySIfrieght").val() != "" || $("#copySIPayor").val() != "") {
        $("#datatablebychargetbl")
                .find("tbody")
                .find("tr")
                .each(function () {
                    if ($(this).find(".chargescheckbox").prop("checked")) {
                        $(this)
                                .find(".SIfrieght")
                                .val($("#copySIfrieght").val())
                                .trigger("onchange");
                        if ($("#copySIfrieght").val() == "E") {
                            $(this).find(".SIPayor").val($("#copySIPayor").val());
                            $(this).find(".payagencyid").text($("#copySIlocation").val());
                        } else {
                            $(this)
                                    .find(".SIPayor")
                                    .val($("#copySIPayor").val())
                                    .trigger("onchange");
                        }
                        chargecopyflag = false;
                    }
                });
        if (chargecopyflag) {
            alert("Kindly select a row to paste.");
        } else {
            $("#datatablebychargetbl")
                    .find("tbody")
                    .find("tr")
                    .find(".chargescheckbox")
                    .prop("checked", false);
            $("#chargescheckboxall").prop("checked", false);
            toastr.success("", "Pasted");
        }
    } else {
        alert("Kindly copy a row before clicking paste option.");
    }
}
function loadinsurancepage(quoteno, custtype) {
    var shipmentdate = $("#hidETDDate").val();
    var onlinebkgno = $("#SI_RefNo").val();
    var origin = $("#OriginPort").val();
    var delivery = $("#DeliveryPort").val();
    var AP_country = $("#apcountry").val();
    var vessel = $("#BKG_vessel_value").text();
    var vesselcode = $("#hidINVessel").val();
    var commodity = "";
    $("#cargo_details")
            .find(".si_cargo_pkg_des")
            .each(function () {
                var comdhs = ""; // $(this).find('.v1hscommodity:nth-child(2)').val();
                if (comdhs == "" || typeof comdhs === "undefined" || comdhs.length == 0) {
                    comdhs = $(this).find(".commodity_new:nth-child(2)").val();
                }
                console.log("comdhs => " + comdhs);
                if (typeof comdhs !== "undefined" && comdhs.length > 0) {
                    commodity = commodity + comdhs + "~~~";
                }
                console.log("commodity => " + commodity);
            });
    console.log("final => " + commodity);
    commodity = commodity.substring(0, commodity.length - 3);
    if (commodity.length > 0) {
        console.log(
                "TEST==> " +
                "Insurance.jsp?shipmentdate=" +
                shipmentdate +
                "&onlinebkgno=" +
                onlinebkgno +
                "&origin=" +
                origin +
                "&delivery=" +
                delivery +
                "&AP_country=" +
                AP_country +
                "&commodity=" +
                commodity +
                "&vessel=" +
                vessel +
                "&vesselcode=" +
                vesselcode
                );
        $.ajax({
            type: "POST",
            cache: false,
            url:
                    "Insurance.jsp?shipmentdate=" +
                    shipmentdate +
                    "&onlinebkgno=" +
                    onlinebkgno +
                    "&origin=" +
                    origin +
                    "&delivery=" +
                    delivery +
                    "&AP_country=" +
                    AP_country +
                    "&commodity=" +
                    commodity +
                    "&vessel=" +
                    vessel +
                    "&vesselcode=" +
                    vesselcode +
                    "&custtype=" +
                    custtype +
                    "&quoteno=" +
                    quoteno +
                    "&sourcetype=SI",
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
                    width: "90%",
                    height: "100%",
                    autoSize: true,
                    scrollOutside: true,
                    closeBtn: true,
                    hideOnOverlayClick: false,
                    overlayShow: false,
                    helpers: {
                        overlay: {closeClick: false},
                    },
                    onClosed: function () {
                        closeLoader();
                    },
                });
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            },
        });
    } else {
        alert("Please enter the Commodity Details");
    }
}
var quoteno = "";
function setInsuranceDetails() {
    var insuredemail = "";
    $("#IN_Country").text($("#apcountry").val());
    $("#IN_Origin").text($("#OriginPort").val());
    $("#IN_Delivery").text($("#DeliveryPort").val());
    $("#IN_Vessel").text($("#BKG_vessel_value").text());
    $("#vesselcode").val($("#hidINVessel").val());
    $("#IN_Shipmentdate").text($("#hidETDDate").val());
    var count = 0;
    $("#comdtyblock").html("");
    var custtype = "",
            incargoval = "";
    var cmdtyarr = new Array();
    $("#cargo_details")
            .find(".si_cargo_pkg_des")
            .each(function () {
                var comdhs = $(this).find(".commodity_new:nth-child(2)").val();
                custtype = $(this).find(".inquotecusttype").val();
                var ischecked = "";
                if (
                        $(this).find(".inquoteno").val() != "" &&
                        $(this).find(".inquoteno").val().length > 0
                        ) {
                    quoteno = $(this).find(".inquoteno").val();
                    insuredemail = $(this).find(".insuredemail").val();
                    ischecked = 'checked="checked"';
                }
                incargoval = $(this).find(".incargoval").val();
                var comdtyhtml =
                        '<div class="form-group row commoditydiv"><div class="col-sm-12"><div class="checkbox-inline col-sm-3" style="margin:7px 0px 0px -3px;"><div class="switchToggle" style="display: inline-flex;"><input type="checkbox"  id="IN_Commodity' +
                        count +
                        '" class="IN_Commodity" ' +
                        ischecked +
                        ' /><label class="tgl_round" for="IN_Commodity' +
                        count +
                        '"> </label><span class="toggle_label ecom_insurance_text" style="padding-left: 5px;">' +
                        Ins_commodity_hscode +
                        '</span></div></div><label class="col-sm-3 commodity_value" >' +
                        comdhs +
                        '</label><label class="col-sm-2 ecom_insurance_text" style="margin-left: 4.2%;max-width: 13.666667%;">' +
                        Ins_cargo_value +
                        '<span class="mandatory"> * </span> </label><div class="checkbox-inline scol-sm-3"><input type="text" id="IN_Cargovalue" class="IN_Cargovalue light_border" onchange="enableinsuranceswitchbtn()" value="' +
                        incargoval +
                        '"></div></div></div>';
                console.log("commodity => " + comdhs);
                console.log("cmdtyarr => " + cmdtyarr);
                console.log(
                        "jQuery.inArray(comdhs, cmdtyarr) => " +
                        jQuery.inArray(comdhs, cmdtyarr)
                        );
                if (jQuery.inArray(comdhs, cmdtyarr) == -1) {
                    $("#comdtyblock").append(comdtyhtml);
                    count = count + 1;
                }
                cmdtyarr.push(comdhs);
            });
    if ($("#IN_Email").val().length == 0 && insuredemail.length > 0) {
        $("#IN_Email").val(insuredemail);
    }
    if ("SH" == custtype || "sh" == custtype) {
        $("#IN_SH").prop("checked", true).trigger("onchange");

        $("#IN_PEmail").val($("#ebook_shipperemail").val());

        //enableSH();
    } else {
        $("#IN_AP").prop("checked", true).trigger("onchange");

        $("#IN_PEmail").val($("#apemail").val());

        //enableAP();
    }
    $("#IN_hidquoteno").val(quoteno);

    var isinsavble = true;
    $("#cargo_details")
            .find(".si_cargo_pkg_des")
            .each(function () {
                if ("Y" == $(this).find(".inavailable").val()) {
                    isinsavble = false;
                    $("#isInsuranceavailable").val("Y");
                }
            });
    if (isinsavble) {
        $("#insurance_not_need").prop("checked", true).trigger("onchange");
    }
    if (quoteno != "" && quoteno.length > 0) {
        $("#calpremiumbtn").text(re_calculate_premium);
    }
}
function loadQuoteDetails() {
    openLoader();
    if (quoteno != "" && quoteno.length > 0 && $("#In_quoteno").length == 0) {
        loadQuoteAmountDetails(quoteno);
    }
    closeLoader();
}
function enable_MandatoryfileforSeawaybl() {
    if ($("#bltype").length > 0) {
        if ($("#bltype").val() == "SEAWAY B/L") {
            if ($("#seawaymsg").length > 0) {
                $("#seawaymsg").remove();
            }
            $("#file_path3")
                    .parents(".input-group")
                    .after(
                            '<span class="text-info" id="seawaymsg">Please upload Seaway BL LOI before submission</span>'
                            );
        } else {
            $("#seawaymsg").remove();
        }
        /* if ($('#SI_RefNo').val().length > 0 && $('#hidden_bltype').val() == $('#bltype').val()) {
         $("#seawaymsg").remove();
         }*/
    }
}
function resetBLTypeFlag() {
    if ($("#hidden_bltype_flag").length > 0) {
        $("#hidden_bltype_flag").val("N");
    }
}
function validateFileValidationSeawayBL() {
    var res = true;
    if ($("#hidden_bltype_flag").length > 0) {
        if (
                $("#hidden_bltype_flag").val() != "Y" &&
                $("#bltype").val() == "SEAWAY B/L" &&
                $("#file_path3").val().length == 0
                ) {
            //$('#hidden_bltype').val() != $('#bltype').val() &&
            if (
                    confirm("Please upload the Seaway BL LOI before submission.") == true
                    ) {
                $("#hidden_bltype_flag").val("Y");
                res = false;
            } else {
                res = false;
            }
        }
    }
    return res;
}
function oogEnable(obj, con, oogcls) {
    console.log("$(obj).prop(checked)---> " + $(obj).val());
    if ($(obj).prop("checked") == true) {
        var oogid = $("#" + con + "\\#oog\\#");
        oogid.val("Y");
        $(obj).val("Y");
        $("." + oogcls).addClass("required");
        $("div#oogblock" + con).show();
        $("." + oogcls).val("");
        // ESLGEN-5007 - Initially, the line below was commented out. It has now been uncommented, and commodity index 1 has been hardcoded to fix the ESLGEN-5007 issue. If multiple commodities exist for a container, we may encounter the same issue.
        $("#" + con + "\\#itemlengthUnit\\#1").val("M");
    } else {
        var oogid = $("#" + con + "\\#oog\\#");
        oogid.val("N");
        $(obj).val("N");
        $("." + oogcls).removeClass("required");
        $("div#oogblock" + con).hide();
        $("." + oogcls).val("");
        // ESLGEN-5007 - Initially, the line below was commented out. It has now been uncommented, and commodity index 1 has been hardcoded to fix the ESLGEN-5007 issue. If multiple commodities exist for a container, we may encounter the same issue.
        $("#" + con + "\\#itemlengthUnit\\#1").val("M");
    }
}

function oogEnableLoad(con, oogcls, oogid, oogval, eqptype) {
    if (oogval == "Y" || oogval == "y") {
        $("." + oogcls)
                .addClass("required")
                .prop("disabled", false);
        $("div#oogblock" + con).show();
        $("#" + oogid).attr("disabled", false);
        $("#" + oogid).prop("checked", true);
    } else {
        $.each(EquipmentJson, function (i, obj) {
            if (
                    eqptype == obj.equipSize &&
                    ("O" == obj.equipType || "o" == obj.equipType)
                    ) {
                $("#" + oogid).attr("disabled", false);
            }
        });
    }
}
function applyToAllContainers(obj, cmid) {
    if (
            $("#commodity" + cmid).valid() &&
            $("#hscode" + cmid).valid() &&
            $("#package" + cmid).valid() &&
            $("#qty" + cmid).valid() &&
            $("#weight" + cmid) &&
            $("#descp" + cmid).valid()
            ) {
        var cp_qtyid = "qty" + cmid;
        var cp_comgpval = $("#commodity" + cmid).val();
        var cp_hscodeval = $("#hscode" + cmid).val();
        var cp_packagetype = $("#package" + cmid).val();
        var cp_noofpackage = $("#qty" + cmid).val();
        var cp_cargoweight = $("#weight" + cmid).val();
        var cp_cargoweightunit = $("#unit" + cmid).val();
        var cp_volume = $("#volume" + cmid).val();
        var cp_volumeunit = $("#vunit" + cmid).val();
        var cp_marksno = $("#mark" + cmid).val();
        var cp_cargodesc = $("#descp" + cmid).val();

        $("#cargo_details")
                .find(".container-body-inner")
                .each(function () {
                    $(this)
                            .find(".si_cargo_pkg_des")
                            .each(function () {
                                console.log("log==> " + $(this).find(".cp_noofpackage").attr("id"));
                                var currqtyid = $(this).find(".cp_noofpackage").attr("id");
                                var contcpid = currqtyid.replaceAll("qty", "");
                                var makecomgpid = "commodity" + contcpid;
                                if (
                                        cp_qtyid != currqtyid &&
                                        cp_comgpval == $("#" + makecomgpid).val()
                                        ) {
                                    $("#hscode" + contcpid).typeahead("val", cp_hscodeval);
                                    $("#package" + contcpid).typeahead("val", cp_packagetype);
                                    $("#hscode" + contcpid).trigger("change"); //.trigger('blur');
                                    //$('#package' + contcpid).trigger('blur');
                                    $("#qty" + contcpid)
                                            .val(cp_noofpackage)
                                            .trigger("blur");
                                    $("#weight" + contcpid)
                                            .val(cp_cargoweight)
                                            .trigger("blur");
                                    $("#unit" + contcpid).val(cp_cargoweightunit);
                                    $("#volume" + contcpid)
                                            .val(cp_volume)
                                            .trigger("blur");
                                    $("#vunit" + contcpid).val(cp_volumeunit);
                                    $("#mark" + contcpid).val(cp_marksno);
                                    $("#descp" + contcpid)
                                            .val(cp_cargodesc)
                                            .trigger("blur");
                                    setnewvalues("newpackid" + contcpid, "package" + contcpid);
                                    setnewvalues("hidhscodeval" + contcpid, "hscode" + contcpid);
                                    /*
                                     $(this).find('.cp_hscode').typeahead('val', cp_hscodeval);
                                     $(this).find('.cp_packagetype').typeahead('val', cp_packagetype);
                                     $(this).find('.cp_hscode').val(cp_hscodeval);
                                     $(this).find('.cp_packagetype').val(cp_packagetype);
                                     $(this).find('.cp_noofpackage').val(cp_noofpackage);
                                     $(this).find('.cp_cargoweight').val(cp_cargoweight);
                                     $(this).find('.cp_cargoweightunit').val(cp_cargoweightunit);
                                     $(this).find('.cp_volume').val(cp_volume);
                                     $(this).find('.cp_volumeunit').val(cp_volumeunit);
                                     $(this).find('.cp_marksno').val(cp_marksno);
                                     $(this).find('.cp_cargodesc').val(cp_cargodesc);
                                     */
                                }
                            });
                });
    }
}

function duplicateComdtyVal() {
    var rtn = true;
    console.log(
            "Hello....I am DuplicateCommodityValidation Inside..............."
            );
    var duplicateIsPresent = false;
    var duplicateContNo = "";
    $("#cargo_details")
            .find(".container-body-inner")
            .each(function () {
                var comdtyStoreArray = [];
                var contNo = $(this).find(".checkcontclass").val();
                console.log("contNo" + contNo);
                $(this)
                        .find(".si_cargo_pkg_des")
                        .each(function () {
                            console.log("log==> " + $(this).find(".cp_noofpackage").attr("id"));
                            var currqtyid = $(this).find(".cp_noofpackage").attr("id");
                            var siId = currqtyid.replaceAll("qty", "");
                            var commodityID = $("#commodity" + siId).val();
                            var hsCode = $("#hscode" + siId).val();
                            var packageCode = $("#package" + siId).val();
                            var marksNo = $("#mark" + siId).val();
                            var commodityDesc = $("#descp" + siId).val();

                            var commodityDetails;
                            if (contNo != null && contNo != "") {
                                commodityID = commodityID.split("~~")[1];
                                hsCode = hsCode.includes("~~") ? hsCode.split("~~")[1] : hsCode;
                                packageCode = packageCode.includes("~~")
                                        ? packageCode.split("~~")[1]
                                        : packageCode;
                                marksNo = marksNo;
                                commodityDesc = commodityDesc;
                                commodityDetails =
                                        contNo +
                                        "~~" +
                                        commodityID +
                                        "~~" +
                                        hsCode +
                                        "~~" +
                                        packageCode +
                                        "~~" +
                                        marksNo +
                                        "~~" +
                                        commodityDesc;
                                duplicateIsPresent =
                                        comdtyStoreArray.length > 0 &&
                                        comdtyStoreArray.includes(commodityDetails)
                                        ? true
                                        : false;

                                console.log("commodityDetails......" + commodityDetails);
                                console.log("duplicateIsPresent......" + duplicateIsPresent);

                                if (duplicateIsPresent) {
                                    duplicateContNo += contNo + ",";
                                }

                                comdtyStoreArray.push(commodityDetails);
                                console.log(duplicateIsPresent);
                            }
                        });
            });

    if (duplicateContNo.length > 1) {
        duplicateContNo = duplicateContNo.slice(0, -1);
        alert("Duplicate Commodity Contains (" + duplicateContNo + ")");
        rtn = false;
    }

    return rtn;
}
/*
 var invalidPkgCodeContNo = '';
 $('#cargo_details').find('.container-body-inner').each(function () {
 var contNo = $(this).find('.checkcontclass').val();
 $(this).find('.si_cargo_pkg_des').each(function () {
 var currqtyid = $(this).find('.cp_noofpackage').attr('id');
 console.log('currqtyid' + currqtyid)
 var siId = currqtyid.replaceAll("qty", "");
 var packageCode = $('#package' + siId).val();
 console.log('Package' + packageCode);
 if (!packageCode.includes('~~') && !invalidPkgCodeContNo.includes(contNo)) {
 invalidPkgCodeContNo += contNo + ",";
 }
 }
 )
 }) */
function loadCargoProtect(page) {
    console.log("inside loadCargoProtect");
    var etdDate = $("#hidETDDate").val();
    if ($("#hidETDDate").length > 0 && etdDate && etdDate !== "null") {
        var originid = $("#hidRoutePOL").val();
        var deliveryid = $("#hidRoutePOD").val();
        var dgFlag = getContainerDGFlag();
        console.log(
                "origincountry SI " + originid + "deliverycountry=>" + deliveryid
                );
        $.ajax({
            url:
                    "CargoProtectAjax?type=shipmentdate&searchvalue=" +
                    $("#hidETDDate").val() +
                    "&origincountry=" +
                    originid +
                    "&deliverycountry=" +
                    deliveryid +
                    "&dgflag=" +
                    dgFlag,
            type: "POST",
            async: false,
            success: function (data) {
                $("#cargoprotect_table_available_div").html(data);
                $(".cargoprotect_check").each(function () {
                    if ($(this).val() == $("#temp_cargoprotectid").val()) {
                        $(this).prop("checked", true);
                    }
                });
                $("#cargoprotect_table_available_div").show();
                $("#available_tier").show();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            },
        });
    }
    if (
            $("#cargoprotectid").length > 0 &&
            $("#cargoprotectid").val() != "" &&
            $("#cargoprotectid").val() != "null" &&
            $("#cargoprotectid").val() != "0" &&
            $("#cargoprotectid").val().length > 0
            ) {
        $.ajax({
            url:
                    "CargoProtectAjax?type=id&searchvalue=" +
                    $("#cargoprotectid").val() +
                    "&page=" +
                    page,
            type: "POST",
            async: false,
            success: function (data) {
                console.log("data==>" + data);
                $("#cargoprotect_table_div").html(data);
                $("#cargoprotect_table_div").show();
                $("#selected_tier").show();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            },
        });
    }
}

function setCargoprotectid(obj) {
    if ($(obj).prop("checked")) {
        $("#temp_cargoprotectid").val($(obj).val());
        $(".cargoprotect_check").prop("checked", false);
        $(obj).prop("checked", true);
    } else {
        $("#temp_cargoprotectid").val("");
    }
}
function removeCargoProtect(protectid, obj) {
    if (confirm("Do you want to remove cargo protection") == true) {
        $("#cargoprotectid").val("");
        $("#cargoprotect_table_div").html("");
        $("#cargoprotect_table_div").hide();
        $("#selected_tier").hide();
    }
}
function callCommoditySearch(comId, hsCodeId, rowid) {
    $.ajax({
        type: "POST",
        cache: false,
        url:
                "BookingAjax?type=commoditysearchjsp&comId=" +
                encodeURIComponent(comId) +
                "&hsCodeId=" +
                encodeURIComponent(hsCodeId) +
                "&rowid=" +
                rowid +
                "&screenname=SI",
        async: false,
        data: $(this).serializeArray(),
        success: function (data) {
            $("#comdtySearchModal .modal-body").html(data);
            $("#comdtySearchModal").modal("show");
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        },
    });
}
function loadAccordionHeading() {
    $("#cargo_details .container-body-inner .si_cargo_pkg_des").each(function () {
        var acquantity = $(this).find(".cp_noofpackage").val() || "";

        if (acquantity.length > 0) {
            $(this).find(".acchead_quantity").text(acquantity);
        }
        $(this)
                .find(".cp_hscode")
                .each(function () {
                    var achscode = $(this).val() || "";

                    if (achscode.length > 0) {
                        var achscodeParts = achscode.split("~~");
                        if (achscodeParts.length > 1) {
                            $(this)
                                    .parents(".si_cargo_pkg_des")
                                    .find(".acchead_hscode")
                                    .text(achscodeParts[1]);
                        }
                    }
                });
    });
}
function enableMultiDeclarant(ismanual) {
    if ($("#GENS_TAB")) {
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
            $("#sellerSelect").val("").trigger("change");
            $("#buyerSelect").val("").trigger("change");
        }
    }
}
function oogDimensionCompare() {
    const divs = $('.box_bg');
    let isMismatch = false;
    let oogMismatch = true;
    // Check OOG mismatch containers
    divs.each(function (index) {
        const containerId = 'sicontainerid' + (index + 1);
        const tempOog = $('#' + containerId + 'tempoogcheckbox').val();
        const oog = $('#' + containerId + 'oogcheckbox').val();

        if (tempOog !== oog && 'N' == oog) {
            oogMismatch = false;
        }
    });
    console.log('oogMismatch', oogMismatch);
    // If no OOG mismatch
    if (oogMismatch) {
        divs.each(function (index) {
            const containerId = 'sicontainerid' + (index + 1);
            if ($('#' + containerId + 'tempoogcheckbox').val() == 'Y') {
                // Check each dimension
                $('.' + containerId + 'dimension').each(function (contIndex) {
                    const dimensions = [
                        'itemforward', 'itemtop', 'itemright', 'itemaft', 'itemleft', 'itemlengthUnit'
                    ];

                    for (var i = 0; i < dimensions.length; i++) {
                        var dim = dimensions[i];
                        var actual = $('#' + containerId + '\\#' + dim + '\\#' + (contIndex + 1)).val();
                        var temp = $('#' + containerId + '\\#temp' + dim + '\\#' + (contIndex + 1)).val();

                        if (actual != temp) {
                            console.log('actual' + actual + 'temp' + temp);
                            isMismatch = true;
                            break;
                        }
                    }
                });
            }
        });
    }
    // Prompt

    console.log('isMismatch', isMismatch);
    if (isMismatch) {
        var userPrompt = confirm('The OOG dimension is different from the Booking / BL. Do you want to proceed?');
        if (userPrompt) {
            return true; // OK
        } else {
            return reverseOogDimension(); // User clicked "Cancel"
        }
    } else if (!oogMismatch) {
        alert('Booking is placed with OOG flag, do you want to proceed as non-OOG?');
        return true;
    } else {
        return true;
    }
    console.log('isMismatch', isMismatch);
    if (isMismatch) {
        var userPrompt = confirm('The OOG dimensions differ from those in the Booking/BL. Do you want to proceed?');
        if (userPrompt) {
            return true; // OK
        } else {
            return reverseOogDimension(); // User clicked "Cancel"
        }
    } else if (!oogMismatch) {
        alert('The booking is marked with an OOG flag. Do you want to proceed as non-OOG?');
        return true;
    } else {
        return true;
    }
}

function reverseOogDimension() {
    const divs = $('.box_bg');
    let oog = false
    divs.each(function (index) {
        const containerId = 'sicontainerid' + (index + 1);

        if ($('#' + containerId + 'oogcheckbox').val() == 'Y') {
            $('.' + containerId + 'dimension').each(function (contIndex) {
                const dimensions = [
                    'itemforward', 'itemtop', 'itemright', 'itemaft', 'itemleft', 'itemlengthUnit'
                ];

                for (var i = 0; i < dimensions.length; i++) {
                    var dim = dimensions[i];
                    var item = $('#' + containerId + '\\#' + dim + '\\#' + (contIndex + 1));
                    var temp = $('#' + containerId + '\\#temp' + dim + '\\#' + (contIndex + 1)).val();

                    if (item.val() !== temp) {
                        item.val(temp); // Update value
                        oog = true;
                    }
                }
            });
        }
    });
    console.log(oog);
    return oog;
}

function callsearchmultifilingmethodsi() {
    var apCustCode = $('#apCustCode').val();
    $.ajax({
        type: "POST",
        cache: false,
        url: "SIAjax?type=esimultifilingSearch&apcustcode=" + encodeURIComponent(apCustCode),
        data: $(this).serializeArray(),
        success: function (data) {
            openFancyBox(data);
        },
        error: function (data, xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
            alert(errorMsg);
        }
    });
}

function setMultifilingDetails(custName, address, address1, country, telephone, EORI, email, city, state, filingtype,zipcode) {
    $("#declarantName").val(custName || "");
    $("#declarantAddress").val(address || "");
    $("#declarantAddress1").val(address1 || "");
    $("#declarantCity").val(city || "");
    $("#declarantCountry").val(country || "");
    $("#declarantCountry").typeahead("val", country);
    $("#declarantState").val(state || "");
    $("#declarantTelephone").val(telephone || "");
    $("#declarantEORI").val(EORI || "");
    $("#declarantEmail").val(email || "");
    $("#declarantZipcode").val(zipcode || "");
    if (filingtype) {
        $(`input[name='declarantFillingType'][value='${filingtype}']`).prop("checked", true);
    }
    $.fancybox.close();
}

function openFancyBox(data) {
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
        helpers: {
            overlay: {
                closeClick: false
            }
        },
        keys: {
            close: [27] //close
        },
        afterClose: function () {


        },
        afterShow: function () {
            $(".fancybox-opened").addClass("modal-draggable");
            //  $(".modal-draggable .fancybox-skin").draggable({
            //       handle: ".fancybox-outer"
            //   });
        }

    });
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
                     console.log('t2lfiling=>'+t2lfiling);
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

function setNvoccCheckbox() {
    if ($('#nvocc').length) {
        var nvocc = $('#ebook_nvocc').val();
        if (!nvocc) {
            nvocc = 'N';
        }
        $('#nvocc').val(nvocc).prop('checked', nvocc === 'Y');
        $('#ebook_nvocc').val(nvocc);
    }
}

function nvoccEnable(obj) {
    if ($(obj).prop("checked") == true) {
        $(obj).val("Y");
    } else {
        $(obj).val("N");
    }
    $('#ebook_nvocc').val($(obj).val());
}

function checkafricaReqsMandatory(podcountryid){
    var rls=true;
  //  var pod = pod;
    var lrpbookno = $('#lrpbookno').val();
      console.log('checkafricaReqsMandatory=>'+lrpbookno);
    if($("fieldset:visible").find('#packinglst').length&&$("fieldset:visible").find('#invoiceupload').length && lrpbookno.length >0 ){
        var isValidPod=false;
        if (podcountryid.length > 0) {  
          
            console.log(podcountryid);
           if (africaReqsContryIdJson.includes(podcountryid)) {
           isValidPod = true;
        }
        }
        $('#packinglst, #invoiceupload').removeClass('required');
 
        if(isValidPod){
             $.ajax({
                type: 'POST',
                cache: false,
                url: 'SIAjax?type=isafricaReqsinlandport&lrpbookno='+lrpbookno,
                async: false,
                data: $(this).serializeArray(),
                success: function (response) {
                     console.log('isinlandport=>'+response);
                     if(response === "true"){
                        console.log('response==>'+response);
                       $('#packinglst, #invoiceupload').addClass('required');
                        rls=false;
                       }

                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            }); 
        }
    }
    return rls;
}
function checkIfLOLFileExists() {
    var result = true;

    if ($('#sifilenamelist').length > 0) {
        $('#sifilenamelist tbody tr').each(function() {
            var filename = $(this).find('th').text().trim();
            var parts = filename.split('_');
            if (parts[0] == 'LOL') {
                result = false;
                return false; // break the loop early (since we found a match)
            }
        });
    }
    console.log('result==>'+result);
    return result;
}

function validateLOIMsg() {
    var releaseType = $('#orgTelType').val(); 
    var isLOIRequired = (releaseType === 'T');
    var fileInput = $('#file_path3');
    var fileUploaded = fileInput.length && fileInput.val();
    var res = true;

    var fieldsetVisible = $("fieldset:visible").find('#file_path3').length > 0;
    console.log('fieldsetVisible ===> ' + fieldsetVisible);
   $("#seawaymsg").remove();
    if ($('#orgTelType').length > 0 && checkIfLOLFileExists()) {
        if (isLOIRequired && fieldsetVisible) {
            if (!fileUploaded) {
                fileInput
                    .parents(".input-group")
                    .after('<span class="text-info" id="seawaymsg">Please upload LOI (Letter of Indemnity) before submission.</span>');

                if (confirm("Please upload LOI (Letter of Indemnity) before submission.") === true) {
                    $("#hidden_bltype_flag").val("Y");
                    res = false;
                } else {
                    res = false;
                }
             
            } else {
                console.log('File Uploaded ===> ' + fileUploaded);
                res = true;
            }
        }
    }
    return res;
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

 function setAddCustDetails(value, type,rowId) {
    console.log("value==>" + value);
    if ($("#" + type + "newcustimg").length > 0) {
        $("#" + type + "newcustimg").html(
                '<i class="fa fa-pencil-square" id="ccupdateimg"></i>  New'
                );
    }
    if (value == "" || value == "new") {
        $("#ebook_" + type + "country_name"+rowId).typeahead("destroy");
        document.getElementById("ebook_" + type + "name"+rowId).value = "";
        document.getElementById("ebook_" +type + "CustCode"+rowId).value = "";
        document.getElementById("ebook_" + type + "name"+rowId).value = "";
        document.getElementById("ebook_" + type + "address"+rowId).value = "";
        document.getElementById("ebook_" + type + "address1"+rowId).value = "";
        document.getElementById("ebook_" + type + "email"+rowId).value = "";
        document.getElementById("ebook_" + type + "country_name"+rowId).value = "";
        document.getElementById("ebook_" + type + "country"+rowId).value = "";
        document.getElementById("ebook_" + type + "phone"+rowId).value = "";
        document.getElementById("ebook_" + type + "fax"+rowId).value = "";
        document.getElementById("ebook_" + type + "city"+rowId).value = "";
        document.getElementById("ebook_" + type + "city_desc"+rowId).value = "";
        document.getElementById("ebook_" + type + "state"+rowId).value = "";
        if ($('#' + type + 'typeperson'+rowId).length > 0) {
            $('#' + type + 'typeperson'+rowId).val("");
        }
        if ($('#ebook_' + type + 'eori'+rowId).length > 0) {
            $('#ebook_' + type + 'eori'+rowId).val("");
        }
        $("#ebook_" + type + "state").typeahead("val", "");
        if (type == "shipper" || type == "consignee" || type == "notify") {
            document.getElementById("header" + type).value = "";
            document.getElementById("ebook_" + type + "nameheader").value = "";
            $("#header" + type).attr("readonly", true);
            $("#ebook_" + type + "nameheader").attr("readonly", true);
        }

        callAutocompleteintialize("#ebook_" + type + "country_name"+rowId);
        $("#ebook_" + type + "country_name"+rowId).css("background-color", "white");
        loadStateAutoComplete(
                "ebook_" + type + "country_name"+rowId,
                "ebook_" + type + "state"+rowId,
                "typeheadhidden"+rowId,
                "load"
                );

    } else if (value != "") {
        var headerShipper = "";
        var custval = value.split("~~");
       

        $("#ebook_" + type + "country_name"+rowId).typeahead("destroy");
        document.getElementById("ebook_" + type + "name"+rowId).value = custval[0];
        document.getElementById("ebook_" +type + "CustCode"+rowId).value = custval[1];
        document.getElementById("ebook_" + type + "name"+rowId).value = custval[0];
        document.getElementById("ebook_" + type + "address"+rowId).value = custval[2];
        document.getElementById("ebook_" + type + "email"+rowId).value = custval[6];
        document.getElementById("ebook_" + type + "country_name"+rowId).value =
                custval[4].toUpperCase();
        document.getElementById("ebook_" + type + "country"+rowId).value = custval[5];
        document.getElementById("ebook_" + type + "phone"+rowId).value = custval[7];
        document.getElementById("ebook_" + type + "fax"+rowId).value = custval[8];
        document.getElementById("ebook_" + type + "address1"+rowId).value = custval[9];
        document.getElementById("ebook_" + type + "city"+rowId).value = custval[3];
        document.getElementById("ebook_" + type + "city_desc"+rowId).value = custval[3];

        callAutocompleteintialize("#ebook_" + type + "country_name"+rowId);
        $("#ebook_" + type + "country_name"+rowId).css("background-color", "white");
        $(
                "#ebook_consigneename-error,#ebook_consigneeaddress-error,#ebook_consigneecountry_name-error,#ebook_consigneecity-error"
                ).html("");
        $(
                "#ebook_consigneephone-error,#ebook_consigneeemail-error,#ebook_consigneefax-error"
                ).html("");
        $("#ebook_" + type + "country_name"+rowId).trigger("change");

        document.getElementById("ebook_" + type + "state"+rowId).value = custval[10];
        $("#ebook_" + type + "state"+rowId).typeahead("val", custval[10]);
        if ($('#' + type + 'typeperson'+rowId).length > 0) {
            $('#' + type + 'typeperson'+rowId).val(custval[13]);
        }
        if ($('#ebook_' + type + 'eori'+rowId).length > 0) {
            $('#ebook_' + type + 'eori'+rowId).val(custval[14]);
        }

    }

} 

function setSellerNameSelectOption() {
  function populateSelectOptions(containerId, selectName, codePrefix, namePrefix, logLabel) {
    const select = $('select[name="' + selectName + '"]');
    const container = $('#' + containerId);
    let options = '<option value="">Select ' + logLabel + '</option>';
    let hasValidEntry = false;
    let nameArray = [];

    if ($('#euport').val() === 'Y') {
      container.find('.previewCust').each(function () {
        const rowID = $(this).attr('id');
        const codeVal = ($('#' + codePrefix + rowID).val() || '').trim();
        const nameVal = ($('#' + namePrefix + rowID).val() || '').trim();

        console.log(rowID + ' ' + logLabel + ' // ' + codeVal + ' // ' + nameVal);

        if (codeVal !== '' && nameVal !== '') {
          nameArray.push(nameVal);
          options += '<option value="' + codeVal + '~~' + nameVal + '">' + nameVal + '</option>';
          hasValidEntry = true;
        }

        const country = "ebook_" + logLabel + "country" + rowID;
        const countryName = "ebook_" + logLabel + "country_name" + rowID;
        setCountry_New(countryName, country);
      });
    }

    select.prop("disabled", !hasValidEntry);
    select.html(options);

    // Set select and disable the HBL No
   
      $("#cargo_details .container-body-inner").each(function () {
        $(this).find(".si_cargo_pkg_des").each(function () {
          const currqtyid = $(this).find(".cp_noofpackage").attr("id");
//          if (!currqtyid) return;

          const contcpid = currqtyid.replaceAll("qty", "");
          console.log(contcpid);
    if ($('#euport').val() === 'Y') {
          const nameInput = logLabel === 'Buyer' ? $('#byname' + contcpid) : $('#slname' + contcpid);
          const codeInput = logLabel === 'Buyer' ? $('#bycode' + contcpid) : $('#slcode' + contcpid);

          const nameVal = (nameInput.val() || '').trim();
          const codeVal = (codeInput.val() || '').trim();

          console.log(nameVal + ' /// ' + codeVal);

          if (nameArray.includes(nameVal) && codeVal) {
            if (logLabel === 'Buyer') {
              $('#byselect' + contcpid).val(codeVal + '~~' + nameVal);
            } else if (logLabel === 'Seller') {
              $('#slselect' + contcpid).val(codeVal + '~~' + nameVal);
            }
          } else {
            nameInput.val('');
            codeInput.val('');
            if (logLabel === 'Buyer') {
              $('#byselect' + contcpid).val('');
            } else if (logLabel === 'Seller') {
              $('#slselect' + contcpid).val('');
            }
          }
          }
          // Disable or clear HBL No
          const ensDocType = $('#ebook_EnsDocType');
          const cmdtyhblno = $('#cmdty_hblno' + contcpid);
          cmdtyhblno.prop('readonly', true);

          if ($('#euport').val() === 'Y') {
            if (ensDocType.val() === 'C') {
              cmdtyhblno.prop('readonly', false);
            } else {
              cmdtyhblno.val('');
            }
          }
        });
      });
//    }
  }

  populateSelectOptions('SellerContainer', 'sellerNameList', 'ebook_SellerCustCode', 'ebook_Sellername', 'Seller');
  populateSelectOptions('BuyerContainer', 'buyerNameList', 'ebook_BuyerCustCode', 'ebook_Buyername', 'Buyer');
}



function validationENSDocType(logLabel) {
    console.log('validationENSDocType**********');

    if ($('#ebook_EnsDocType').length > 0 && $('#euport').val()== 'Y') {
    const ensDocType = $('#ebook_EnsDocType');
    const ensFilling = $('#ebook_EnsFiling');
    const isENS = ensDocType.length > 0 && ensDocType.val() === 'S' && ensFilling.length > 0 && ensFilling.val() == 'S';
            const buyerName1 = $('#ebook_Buyername1').val();
            const buyerCode1 = $('#ebook_BuyerCustCode1').val();
            const sellerName1 = $('#ebook_Sellername1').val();
            const sellerCode1 = $('#ebook_SellerCustCode1').val();
$("#cargo_details").find(".container-body-inner").each(function () {
    $(this).find(".si_cargo_pkg_des").each(function () {
        var currqtyid = $(this).find(".cp_noofpackage").attr("id");
        var contcpid = currqtyid.replaceAll("qty", "");
       const nameInput = logLabel == 'Buyer'? $('#byname' + contcpid): $('#slname' + contcpid);
       const codeInput = logLabel == 'Buyer'? $('#bycode' + contcpid): $('#slcode' + contcpid);
        const nameVal = nameInput.length ? (nameInput.val() || '').trim() : '';
        const codeVal = codeInput.length ? (codeInput.val() || '').trim() : '';
                if (isENS) {
                if (buyerName1 && buyerCode1 &&  logLabel == 'Buyer') {
                    nameInput.val(buyerName1);
                    codeInput.val(buyerCode1);
                }

                if (sellerName1 && sellerCode1 &&  logLabel == 'Seller') {
                    nameInput.val(sellerName1);
                    codeInput.val(sellerCode1);
                }
            }
      });
  });
    }
}

function setCustNameCode(selectElem, codeId, nameId) {
    var selectedValue = selectElem.value;
    var parts = selectedValue.split('~~');

    document.getElementById(codeId).value = parts[0] || '';
    document.getElementById(nameId).value = parts[1] || '';
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
        [prefix + 'fax' + index] : {
            validnumbers: '#' + prefix + 'phone' + index,
            ZeroTest: '#' + prefix + 'phone' + index,
            phonefax: ['#' + prefix + 'phone' + index]
        }
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
//        [prefix + 'fax' + index] : {
//            validnumbers: 'Invalid Fax Number',
//            ZeroTest: 'Invalid Fax Number',
//            phonefax: 'Invalid Fax Format'
//        }
    };

 
    return { rules, messages };
}

//// Declare this globally once
//const addAmenCustRules = {};

function applyRules(containerSelector, customerType,HideFaxNo) {
    const form = $('#SIEditForm');
    console.log(form.data('validator'));
   if (form.data('validator')) {
    $('#'+containerSelector).find('.previewCust').each(function () {
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
function getContainerDGFlag(){
    let dgFlag = 'N';

    $("#cargo_details").find(".container-body-inner").each(function (containerIndex) {

        $(this).find(".si_cargo_pkg_des").each(function (pkgIndex) {

            var currId = $(this).find(".cp_hazardous").attr("id");
            const safeSelector = $.escapeSelector(currId); 
            const dgFlagVal = $("#" + safeSelector).val() || '';

            if (dgFlagVal == 'Y') {
                dgFlag = 'Y';
                return false; 
            }
        });

        if (dgFlag == 'Y') {
            return false; 
        }
    });
    return dgFlag;
}

// HS Code length check onchange event
var hsCodeWarningShown = {};
$(document).on('change', '.hscodelengthcheck', function() {
    var elementId = $(this).attr('id');
    // Only show warning if element has id attribute with length > 0
    console.log("hscodelengthcheck  : " + elementId);
    if (elementId && elementId.length > 0) {
        // Check if warning already shown for this element recently
        if (hsCodeWarningShown[elementId]) {
            return;
        }
        var value = $(this).val();
        if (value && value.length > 0) {
            var parts = value.split("~~");
            if (parts.length >= 2) {
                var hsCode = parts[1].trim();
                if (hsCode.length < 6) {
                    // Set flag to prevent duplicate messages
                    hsCodeWarningShown[elementId] = true;
                    toastr.warning("HS Code must have 6 or more characters.", "Warning", {
                        timeOut: 5000,
                        closeButton: true
                    });
                    // Reset flag after 1 second to allow future warnings
                    setTimeout(function() {
                        hsCodeWarningShown[elementId] = false;
                    }, 1000);
                }
            }
        }
    }
});