
// Haulage tab function
function addHaulage(formid) {
    var haulform = $("#" + formid).show();
    $("#haulpickuplocname").prop('readonly', false);
    if (haulform.valid() && validateStopSequence(formid)) {
        var action = "<a href='#' onclick='editHaulage(this)' class='btn btn-lg p-0 text-warning'><i class='fa fa-edit'></i></a> <a href='javascript:void(0)' onclick='deleteHaulage(this)' class='btn btn-lg p-0 text-danger'><i class='fa fa-trash-o'></i></a>";
        var eqpid = $("#hauleqprowid").val().split('~~')[0].trim();
        var eqptype = $("#hauleqptype").val();
        var pickuploccode = $("#haulpickuploccode").val();
        var pickuplocname = $("#haulpickuplocname").val();
        var address = $("#hauladdress").val();
        var refno = $("#haulrefno").val();
        var dropdate = $("#hauldropdate").val() + " " + $("#hauldropdatetime").val();
        var stopsep = $("#haulstopseq").val();
        var conperson = $("#haulcontactperson").val();
        var conphone = $("#haulcontactphone").val();
        var conemail = $("#haulcontactemail").val();
        var twrsoverweight;
        if ($("#haultwrsoverweight").prop("checked") == true) {
            twrsoverweight = 'Y';
        } else {
            twrsoverweight = 'N';
        }
        var remarks = $("#haulremarks").val();
        var amendoptional = "";
        var cargorowid;
        var eqpidcls = 'eqprowid';
        if (formid == "AmendmentBookingFormBean") {
            cargorowid = $("#hiddenhaulagerowid").val();
            amendoptional = "<td class='eqprowid'>" + cargorowid + "</td>";
            eqpidcls = 'eqpid';
        } else {
            amendoptional = "<td class='eqprowid d-none'>" + eqpid + "</td>";
            eqpidcls = 'dummyeqp';
            eqpid = $("#hauleqprowid").val().split("~~")[1];
        }

        if ($("#hauloprtype").val() == 'add') {
//row create
            var newrow = "<tr>" +
                    "<td>" + action + "</td>" +
                    "<td class='" + eqpidcls + "'>" + eqpid + "</td>" +
                    "<td class='eqptype'>" + eqptype + "</td>" +
                    "<td class='d-none'>" + pickuploccode + "</td>" +
                    "<td>" + pickuplocname + "</td>" +
                    "<td class=''>" + address + "</td>" +
                    "<td class=''>" + refno + "</td>" +
                    "<td>" + dropdate + "</td>" +
                    "<td class='stopseq'>" + stopsep + "</td>" +
                    "<td>" + conperson + "</td>" +
                    "<td>" + conphone + "</td>" +
                    "<td class=''>" + conemail + "</td>" +
                    "<td>" + twrsoverweight + "</td>" +
                    "<td>" + remarks + "</td>" + amendoptional +
                    "</tr>";
            $("#datatablebyhaulage tbody").append(newrow);
            //
        } else if ($("#hauloprtype").val() == 'edit') {
            var editelement = hauleditelement;
            var par = $(editelement).parent().parent(); //tr
            par.children("td:nth-child(2)").html(eqpid);
            par.children("td:nth-child(3)").html(eqptype);
            par.children("td:nth-child(4)").html(pickuploccode);
            par.children("td:nth-child(5)").html(pickuplocname);
            par.children("td:nth-child(6)").html(address);
            par.children("td:nth-child(7)").html(refno);
            par.children("td:nth-child(8)").html(dropdate);
            par.children("td:nth-child(9)").html(stopsep);
            par.children("td:nth-child(10)").html(conperson);
            par.children("td:nth-child(11)").html(conphone);
            par.children("td:nth-child(12)").html(conemail);
            par.children("td:nth-child(13)").html(twrsoverweight);
            par.children("td:nth-child(14)").html(remarks);
            if (formid == "AmendmentBookingFormBean") {
                par.children("td:nth-child(15)").html(cargorowid);
            } else {
                var hauleqprowid = $("#hauleqprowid").val().split('~~')[0].trim();
                par.children("td:nth-child(15)").html(hauleqprowid);
            }

            $("#hauloprtype").val("add");
            $("#haulsubmitID").html("<i class='fa fa-plus'></i> ADD");
        }
        resetHaulage();
    }
    $("#haulpickuplocname").prop('readonly', true);
}
var hauleditelement;
function editHaulage(curelement) {
    var par = $(curelement).parent().parent(); //tr
    if ($("#haulbookingtype").length > 0 && $("#haulbookingtype").val() == 'new') {
        $("#hauleqprowid").val(par.children("td:nth-child(15)").html() + "~~" + par.children("td:nth-child(2)").html()).trigger('onchange');
    } else {
        $("#hauleqprowid").val(par.children("td:nth-child(2)").html()).trigger('onchange');
    }
    $("#hauleqptype").val(par.children("td:nth-child(3)").html());
    $("#haulpickuploccode").val(par.children("td:nth-child(4)").html());
    $("#haulpickuplocname").val(par.children("td:nth-child(5)").html());
    $("#hauladdress").val(par.children("td:nth-child(6)").html());
    $("#haulrefno").val(par.children("td:nth-child(7)").html());
    $("#hauldropdate").val(par.children("td:nth-child(8)").html().split(" ")[0]);
    $("#hauldropdatetime").val(par.children("td:nth-child(8)").html().split(" ")[1]);
    $("#haulstopseq").val(par.children("td:nth-child(9)").html());
    $("#haulcontactperson").val(par.children("td:nth-child(10)").html());
    $("#haulcontactphone").val(par.children("td:nth-child(11)").html());
    $("#haulcontactemail").val(par.children("td:nth-child(12)").html());
    if (par.children("td:nth-child(13)").html() == 'Y') {
        $("#haultwrsoverweight").prop("checked", true);
    } else {
        $("#haultwrsoverweight").prop("checked", false);
    }

    $("#haulremarks").val(par.children("td:nth-child(14)").html());
    $("#hauloprtype").val("edit");
    hauleditelement = curelement;
    $("#haulsubmitID").html("<i class='fa fa-edit'></i> EDIT");
    if (par.children("td:nth-child(10)").html().trim() == '' || par.children("td:nth-child(10)").html().trim().length == 0) {
        $("#haulcontactperson").prop('readonly', false);
    } else {
        $("#haulcontactperson").prop('readonly', true);
    }
    if (par.children("td:nth-child(12)").html().trim() == '' || par.children("td:nth-child(12)").html().trim().length == 0) {
        $("#haulcontactemail").prop('readonly', false);
    } else {
        $("#haulcontactemail").prop('readonly', true);
    }
}
function deleteHaulage(curelement) {
    var con = confirm(delBkgHaul);
    if (con == true) {
        var par = $(curelement).parent().parent(); //tr
        par.remove();
    }
}
function resetHaulage() {
    $("#hauleqprowid").val("");
    $("#hauleqptype").val("");
    $("#haulpickuploccode").val("");
    $("#haulpickuplocname").val("");
    $("#hauladdress").val("");
    $("#haulrefno").val("");
    $("#hauldropdate").val("");
    $("#hauldropdatetime").val("00:00");
    $("#haulstopseq").val("");
    $("#haulcontactperson").val("");
    $("#haulcontactphone").val("");
    $("#haulcontactemail").val("");
    $("#haultwrsoverweight").prop("checked", false);
    $("#haulageShipper").prop("checked", false);
    $("#haulremarks").val("");
    $('.haulagetabdiv label.error').remove();
    $("#hauloprtype").val("add");
    $("#haulsubmitID").html("<i class='fa fa-plus'></i> ADD");
    $("#haulcontactperson").prop('readonly', true);
    $("#haulcontactemail").prop('readonly', true);
    //setShipperDetailsToHaulage();
}

function getHaulageDocType(custCode) {
    $('#haultwrsdoctype').find('option').remove();
    $('<option>').val("").text("").appendTo('#haultwrsdoctype');
    var origin = $("#ebook_origin_country").val();
    var originarr = origin.split("-");
    var origincode = originarr[0].trim();
    if (origincode != null && origincode.length > 0) {
        $.ajax({
            type: "POST",
            cache: false,
            url: "BookingAjax?type=HaulageDocType&origincode=" + origincode + "&custcode=" + custCode,
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                if (data.trim() != null && data.trim() != "null" && data.trim().length > 0) {
                    var doctype = data.split(",");
                    var i = 0;
                    for (i = 0; i < doctype.length; i++) {
                        $('<option>').val(doctype[i]).text(doctype[i]).appendTo('#haultwrsdoctype');
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

function convertHaulagedetailstoJson() {
    var tbl = $('#datatablebyhaulage');
    var tblhead = $(tbl).find('thead');
    var tblbody = $(tbl).find('tbody');
    var header = [];
    var JObjectArray = [];
    $.each($(tblhead).find('tr>th'), function (i, j) {
        header.push($(j).attr('title').toLowerCase());
    });
    $.each($(tblbody).find('tr'), function (key, value)
    {
        var jObject = {};
        for (var x = 0; x < header.length; x++)
        {
            jObject[header[x]] = $(this).find('td').eq(x).text();
        }
        JObjectArray.push(jObject);

    });
    var jsonarraystr = JSON.stringify(JObjectArray);
    $("#haulagedetailsjsonstr").val(jsonarraystr);
}
function setEqpType(value, valueType) {
    var eqptype;
    if (valueType == 'rowid') {
        /*eqptype = $("#hauleqprowid").text();
         eqptype = eqptype.split("~~")[0].trim();*/
        value = value.split("~~")[0].trim();
        $("#datatablebycargodiv").find('.preview').each(function () {
            var rowid = $(this).find(".rowidchange").html();
            if (rowid == value) {
                eqptype = $(this).find('.eqpsize').val();
            }
        });
    } else if (valueType == 'conno') {
        $("#datatablebycargodiv").find('.preview').find(".containerclass").each(function () {
            if ($(this).val().trim() == value.trim()) {
                eqptype = $(this).parents(".preview").find('.eqpsize').val();
            }
        });
    }
    $("#hauleqptype").val(eqptype);
}
function changeEqpSizeToHaulage(obj) {
    var eqpsize = $(obj).val();
    var rowid = $(obj).parents(".preview").find(".rowidchange").html();
    $("#datatablebyhaulage").find("tbody").find('tr').each(function () {
        var haulrowid = $(this).find(".eqprowid").html();
        if (rowid == haulrowid) {
            if (eqpsize != null && eqpsize.length > 0) {
                $(this).find(".eqptype").html(eqpsize);
            }

        }
    });
}

function removeHaulageDetails(rowId, tableId, obj) {
    $("#datatablebyhaulage").find("tbody").find('tr').find(".eqprowid").each(function () {
        if ($(this).html() == rowId) {
            $(this).parents("tr").remove();
        }
        if ($(this).html() > rowId) {
            $(this).html(($(this).html() - 1));
        }
    });
}
function getContactDetails() {
    var pickupcode = $('#haulpickuploccode').val();
    $.ajax({
        type: "POST",
        cache: false,
        //async : false,
        url: "BookingAjax?type=HaulageContactDetails&custcode=" + pickupcode,
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
function getPickUpLocDetails(splcustgrp) {
    $.ajax({
        type: "POST",
        cache: false,
        //async : false,
        url: "BookingAjax?type=HaulageCustDetails&splcustgrp=" + splcustgrp,
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

function setHaulageCustomerDetails(custCode, custName, address, phone, email, contactperson, city, state) {
    address = address + ",\r\n" + city;
    if (state.length > 0) {
        address = address + ",\r\n" + state;
    }
    $("#haulpickuploccode").val(custCode).trigger("change");
    $("#haulpickuplocname").val(custName);
    $("#hauladdress").val(address);
    $("#haulcontactperson").val(contactperson);
    $("#haulcontactphone").val(phone);
    $("#haulcontactemail").val(email);
    if (contactperson == '' || contactperson.length == 0) {
        $("#haulcontactperson").prop('readonly', false);
    } else {
        $("#haulcontactperson").prop('readonly', true);
    }
    if (email == '' || email.length == 0) {
        $("#haulcontactemail").prop('readonly', false);
    } else {
        $("#haulcontactemail").prop('readonly', true);
    }
    $.fancybox.close();
}
function setHaulageContactDetails(name, phone, email) {
    $("#haulcontactperson").val(name);
    $("#haulcontactphone").val(phone);
    $("#haulcontactemail").val(email);
    if (name == '' || name.length == 0) {
        $("#haulcontactperson").prop('readonly', false);
    } else {
        $("#haulcontactperson").prop('readonly', true);
    }
    if (email == '' || email.length == 0) {
        $("#haulcontactemail").prop('readonly', false);
    } else {
        $("#haulcontactemail").prop('readonly', true);
    }
    $.fancybox.close();
}

function getTMSClientDetails(loadport) {
    $('#haultmsclient').find('option').remove();
    $('<option>').val("").text("").appendTo('#haultmsclient');
    $.ajax({
        type: "POST",
        cache: false,
        //async : false,
        url: "BookingAjax?type=HaulageTMSClientDetails&loadport=" + loadport,
        data: $(this).serializeArray(),
        success: function (data) {
            if (data != null && data.trim().length > 0) {
                var jsonObj = $.parseJSON(data);
                for (var key in jsonObj) {
                    if (jsonObj.hasOwnProperty(key)) {
                        $('<option>').val(key).text(jsonObj[key]).appendTo('#haultmsclient');
                    }
                }
            }
        },
        error: function (data, xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
            alert(errorMsg);
        }
    });
}

function setShipperDetailsToHaulage() {
    var shippercode = $("#ebook_shippercode").val();
    var shippername = $("#ebook_shippername").val();
    var shipperaddress = $("#ebook_shipperaddress").val() + ',\r\n' + $("#ebook_shippercity").val();
    var shipperphone = $("#ebook_shipperphone").val();
    var shipperemail = $("#ebook_shipperemail").val();
    if ($("#ebook_shipperstate").val().length > 0) {
        var statestr = $("#ebook_shipperstate").val();
        shipperaddress = shipperaddress + ',\r\n' + statestr.split('-')[0];
    }
    if (shippername.trim().length > 0 && shipperaddress.trim().length > 0 && ($("#haulageShipper").prop("checked") == true)) {//
        $("#haulpickuploccode").val(shippercode).trigger("change");
        $("#haulpickuplocname").val(shippername);
        $("#hauladdress").val(shipperaddress);
        $("#haulcontactperson").val(shippername);
        $("#haulcontactphone").val(shipperphone);
        $("#haulcontactemail").val(shipperemail);
        if (shippername == '' || shippername.length == 0) {
            $("#haulcontactperson").prop('readonly', false);
        } else {
            $("#haulcontactperson").prop('readonly', true);
        }
        if (shipperemail == '' || shipperemail.length == 0) {
            $("#haulcontactemail").prop('readonly', false);
        } else {
            $("#haulcontactemail").prop('readonly', true);
        }
    } else {
        if (($("#haulageShipper").prop("checked") == true)) {
            alert("Kindly select the shipper details");
            $("#haulageShipper").prop("checked", false);
        }
        $("#haulpickuploccode").val("").trigger("change");
        $("#haulpickuplocname").val("");
        $("#hauladdress").val("");
        $("#haulcontactperson").val("");
        $("#haulcontactphone").val("");
        $("#haulcontactemail").val("");
        $("#haulcontactperson").prop('readonly', true);
        $("#haulcontactemail").prop('readonly', true);
    }
}
function setContactdetails() {
    var pickupcode = $("#haulpickuploccode").val();
    $.ajax({
        type: "POST",
        cache: false,
        //async : false,
        url: "BookingAjax?type=HaulageFirstContDetails&custCode=" + pickupcode,
        data: $(this).serializeArray(),
        success: function (data) {
            if (data != null && data.length > 0) {
                var arr = data.split("~~");
                $("#haulcontactperson").val(arr[0].trim());
                $("#haulcontactphone").val(arr[1].trim());
                $("#haulcontactemail").val(arr[2].trim());
                if (arr[0].trim() == '' || arr[0].trim().length == 0) {
                    $("#haulcontactperson").prop('readonly', false);
                } else {
                    $("#haulcontactperson").prop('readonly', true);
                }
                if (arr[2].trim() == '' || arr[2].trim().length == 0) {
                    $("#haulcontactemail").prop('readonly', false);
                } else {
                    $("#haulcontactemail").prop('readonly', true);
                }
            }
        },
        error: function (data, xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
            alert(errorMsg);
        }
    });
}

function changeContainerNoToHaulage(eqpid, obj) {
    var cargoindex = $(obj).parents(".preview").find(".rowidchange").html();
    var haulrowid;
    var eqpno = $("#" + eqpid).val();
    $("#datatablebyhaulage").find("tbody").find('tr').each(function () {
        haulrowid = $(this).find(".eqprowid").html();
        if (cargoindex == haulrowid) {
            if (eqpno != null && eqpno.length > 0) {
                $(this).find(".eqpid").html(eqpno);
            }

        }
    });
}

//
function getRowIdFromConatainer(obj) {
    var eqpid = $(obj).val();
    var rowid;
    $('#datatablebycargodiv').find('.preview').find(".containerclass").each(function () {
        if ($(this).val().trim() == eqpid.trim()) {
            rowid = $(this).parents(".preview").find('.rowidchange').html();
        }
    });
    $('#hiddenhaulagerowid').val(rowid);
}
//
function resetNewHaulage() {
    $('#newhaulpickuploc').val('');
    $('#newhaulcontperson').val('');
    $('#newhaulcontphone').val('');
    $('#newhaulcontemail').val('');
    $('#newhauladdress').val('');
}
function shownewhaulmodal() {
    resetNewHaulage();
    if ($('#haulpickuploccode').val() == '' || $('#haulpickuploccode').val().length == 0) {
        $("#newhaulpickuploc").val($("#haulpickuplocname").val());
        $("#newhauladdress").val($("#hauladdress").val());
        $("#newhaulcontperson").val($("#haulcontactperson").val());
        $("#newhaulcontphone").val($("#haulcontactphone").val());
        $("#newhaulcontemail").val($("#haulcontactemail").val());
    }
    $('#newhaulagemodal').modal('show');
}
function validateStopSequence(formid) {
    var ssreturn = true;
    if (formid == "AmendmentBookingFormBean") {
        if ($("#hauloprtype").val() == 'edit') {
            var editelement1 = hauleditelement;
            var par1 = $(editelement1).parent().parent();
            if ($("#hauleqprowid").val() == $(par1).find('.eqpid').text() && $('#haulstopseq').val() == $(par1).find('.stopseq').text()) {
                ssreturn = true;
            } else {
                $('#datatablebyhaulagediv').find('tbody').find('tr').each(function () {
                    var h_eqpid = $(this).find('.eqpid').text();
                    var h_stopseq = $(this).find('.stopseq').text();
                    if ($("#hauleqprowid").val().split('~~')[0].trim() == h_eqpid && $('#haulstopseq').val() == h_stopseq) {
                        alert("Stop sequence " + $('#haulstopseq').val() + " already exists for the container number(" + $("#hauleqprowid").val() + ").");
                        ssreturn = false;
                    }
                });
            }
        } else {
            $('#datatablebyhaulagediv').find('tbody').find('tr').each(function () {
                var h_eqpid = $(this).find('.eqpid').text();
                var h_stopseq = $(this).find('.stopseq').text();
                if ($("#hauleqprowid").val().split('~~')[0].trim() == h_eqpid && $('#haulstopseq').val() == h_stopseq) {
                    alert("Stop sequence " + $('#haulstopseq').val() + " already exists for the container number(" + $("#hauleqprowid").val() + ").");
                    ssreturn = false;
                }
            });
        }
    } else {
        if ($("#hauloprtype").val() == 'edit') {
            var editelement1 = hauleditelement;
            var par1 = $(editelement1).parent().parent();
            if ($('#hauleqptype').val() == $(par1).find('.eqptype').text() && $('#haulstopseq').val() == $(par1).find('.stopseq').text() && $(par1).find('.dummyeqp').text() == $("#hauleqprowid").val().split("~~")[1].trim()) {
                ssreturn = true;
            } else {
                $('#datatablebyhaulagediv').find('tbody').find('tr').each(function () {
                    var h_eqptype = $(this).find('.eqptype').text();
                    var h_stopseq = $(this).find('.stopseq').text();
                    var dummyeqp = $(this).find('.dummyeqp').text();
                    var dummyeqpscrn = $("#hauleqprowid").val().split("~~")[1].trim();
                    if ($('#hauleqptype').val() == h_eqptype && $('#haulstopseq').val() == h_stopseq && dummyeqpscrn == dummyeqp) {
                        alert("Stop sequence " + $('#haulstopseq').val() + " already exists for the equipment type(" + $('#hauleqptype').val() + " ~~ " + $("#hauleqprowid").val().split("~~")[1].trim() + ").");
                        ssreturn = false;
                    }
                });
            }
        } else {
            $('#datatablebyhaulagediv').find('tbody').find('tr').each(function () {
                var h_eqptype = $(this).find('.eqptype').text();
                var h_stopseq = $(this).find('.stopseq').text();
                var dummyeqp = $(this).find('.dummyeqp').text();
                var dummyeqpscrn = $("#hauleqprowid").val().split("~~")[1].trim();
                if ($('#hauleqptype').val() == h_eqptype && $('#haulstopseq').val() == h_stopseq && dummyeqpscrn == dummyeqp) {
                    alert("Stop sequence " + $('#haulstopseq').val() + " already exists for the equipment type(" + $('#hauleqptype').val() + " ~~ " + $("#hauleqprowid").val().split("~~")[1].trim() + ").");
                    ssreturn = false;
                }
            });
        }
    }
    return ssreturn;
}
function removeHaulageDetailsOnQuantity(obj) {

    var rwid = $(obj).parents('.preview').find('.rowidchange').html();
    var eqpqnty = $(obj).parents('.preview').find('.checkeqpquantity').val();
    var oldeqpqnty = $(obj).parents('.preview').find('.eqpquantity').val();
    var eqpsize = $(obj).parents('.preview').find('.eqpsize').val();
    //if (eqpqnty < oldeqpqnty) {
    $("#datatablebyhaulage").find("tbody").find('tr').find(".eqprowid").each(function () {
        if ($(this).html() == rwid && $(this).parents("tr").find('.eqptype').html() == eqpsize) {
            var dummyeqp = $(this).parents("tr").find('.dummyeqp').html();
            dummyeqp = dummyeqp.replaceAll("TNB", "");
            if (dummyeqp > eqpqnty) {
                $(this).parents("tr").remove();
            }
        }
    });
    //}
    $(obj).parents('.preview').find('.eqpquantity').val($(obj).val());

}