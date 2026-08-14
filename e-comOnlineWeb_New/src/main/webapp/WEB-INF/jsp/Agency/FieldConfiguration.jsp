<%-- 
    Document   : FieldConfiguration
    Created on : Nov 7, 2017, 9:15:56 AM
    Author     : deepa.p
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"  import="net.ecom.common.GeneralConf" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Field Configuration</div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">

            <html:form styleId="FieldConfigForm"  method="post" action="/savefieldconfig" styleClass="form-horizontal" >
                <html:hidden styleId="saveType" property="saveType" />
                <input type="hidden" id="typeheadhidden" name="typeheadhidden">
                <html:hidden styleId="configid" property="configid" />

                <div class="form-group row">
                    <label class="col-sm-2 control-label">Field Name<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:select property="fieldName"  styleId="fieldName"  styleClass="form-control light_border required " onchange="setInputType(this.value);setShipmentType(this.value);">
                            <html:options collection="FIELDNAMECONFIGLIST" property="value" labelProperty="label" />
                        </html:select>
                    </div>
                    <label class="col-sm-2 control-label">Input Type<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:select property="inputType"  styleId="inputType"  styleClass="form-control light_border required ">
                            <html:option value="">Select Input Type</html:option>
                        </html:select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Search Type<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:select property="searchType"  styleId="searchType"  styleClass="form-control light_border required " onchange="selectSearchType(this.value);" >
                            <html:option value="">Select Search Type</html:option>
                            <html:option value="C">By Country</html:option>
                            <html:option value="P">By Port</html:option>
                        </html:select>
                    </div>
                    <label class="col-sm-2 control-label">Search Value<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="searchValue"  styleId="searchValue"  styleClass="form-control light_border portsnotes required "  disabled="true"  />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Shipment Type<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="shipmentType"  styleId="shipmentType"  styleClass="form-control light_border required "   readonly="true" />
                    </div>
                    <label class="col-sm-2 control-label">Status<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:select property="status"  styleId="status"  styleClass="form-control light_border required ">
                            <html:option value="">Select Type</html:option>
                            <html:option value="A">Active</html:option>
                            <html:option value="I">Inactive</html:option>
                        </html:select>
                    </div>
                </div>
                <br/>
                <div class="form-group row">
                    <div class="col-sm-5">
                    </div>
                    <div class="col-sm-5">
                        <button type="submit" class="btn btn-primary btn-lg search_button"  id="searchID"> Submit</button>
                        <button type="button" class="btn btn-danger btn-lg search_button" onclick="resetFieldConfig();"> Reset</button>
                    </div>
                </div>
            </html:form>

        </div>
    </div>
</div>
<div id="fieldconfigdetails" style="display:none;"></div>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/country.js?<%=jsno%>"></script>
<script type="text/javascript">
                                var portJson = portJson;
                                var counlist = countryJson;
                                $('#saveType').val('save');
                                $(document).ready(function () {
                                    // $('#fieldName,#searchType,#searchValue,#shipmentType,#inputType').attr("placeholder", "In hours");
                                    $("#FieldConfigForm").validate({
                                        submitHandler: function (form) {
                                            var check = true;
                                            //  callLoading();
                                            var searchvalue = $('#searchValue').val().split("-");
                                            searchvalue = trim(searchvalue[0]);
                                            $.ajax({
                                                type: "POST",
                                                async: false,
                                                cache: false,
                                                url: "ecomAjax?type=checkfieldconfig&fieldcode=" + encodeURIComponent($('#fieldName').val()) + "&searchtype=" + encodeURIComponent($('#searchType').val())
                                                        + "&searchvalue=" + searchvalue + "&shipmenttyp=" + $('#shipmentType').val() + "&inputtype=" + $('#inputType').val() + "&saveType=" + $('#saveType').val()
                                                        + "&id=" + $('#configid').val(),
                                                success: function (response) {
                                                    console.log(response);
                                                    if (response == 'Yes') {
                                                        //  destroyLoading();
                                                        check = false;
                                                        alert('Duplicate field configuration.');
                                                        return false;
                                                    } else {
                                                        return true;
                                                    }
                                                }
                                            });
                                            if (check) {
                                                return true;
                                            }

                                        }
                                    });
                                    var form = $("#FieldConfigForm").show();
                                    form.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly]";

                                });

                                function setInputType(val) {
                                    var option = '<option value = "">Select Input Type</option>';
                                    if (val == 'PBL' || val == 'SHC') {
                                        option += "<option value = 'E'>Enable</option>";
                                        option += "<option value = 'D'>Disable</option>";
                                    } else if (val != "") {
                                        option += "<option value = 'M'>Mandatory</option>";
                                        option += "<option value = 'N'>Non Mandatory</option>";
                                    }
                                    $('#inputType').html(option);
                                }
                                function setShipmentType(val) {
                                    if (val != "") {
                                        $.ajax({
                                            type: "POST",
                                            cache: false,
                                            url: "ecomAjax?type=fieldconfigshipmenttype&fieldcode=" + encodeURIComponent(val),
                                            success: function (response) {
                                                if (response == 'E') {
                                                    $('#shipmentType').val("Export");
                                                } else {
                                                    $('#shipmentType').val("Import");
                                                }
                                            }
                                        });
                                    } else {
                                        $('#shipmentType').val("");
                                    }
                                }
                                function selectSearchType(val) {
                                    $('#searchValue').attr('disabled', false);
                                    $('#searchValue').val("");
                                    $('#searchValue').css("background-color", "white");
                                    if (val == 'C') {
                                        loadConfigCountry();
                                    } else if (val == 'P') {
                                        loadConfigPort();
                                    } else {
                                        $('#searchValue').attr('disabled', true);
                                        $('#searchValue').css("background-color", "#eee");
                                    }
                                }
                                function loadConfigPort() {
                                    $('#searchValue').typeahead('destroy');
                                    var ports = new Bloodhound({
                                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                                        local: portJson});
                                    ports.initialize();
                                    $('#searchValue').typeahead(
                                            {
                                                hint: true,
                                                highlight: true,
                                                minLength: 2
                                            },
                                            {
                                                name: 'ports',
                                                limit: 20,
                                                displayKey: function (portJson) {
                                                    return portJson.PortCode + " - " + portJson.PortName;
                                                },
                                                source: ports.ttAdapter()
                                            }).on('typeahead:render', function (obj, datum) {
                                        $("#typeheadhidden").val("");
                                    }).on('typeahead:selected', function (obj, datum) {
                                        $("#typeheadhidden").val(datum.PortName);
                                    }).on('typeahead:autocomplete', function (obj, datum) {
                                        $("#typeheadhidden").val(datum.PortName);
                                    }).on("typeahead:change", function (obj, datum) {
                                        var hiddenValue = $("#typeheadhidden").val();
                                        if (hiddenValue == null || hiddenValue == "") {
                                            $('#searchValue').typeahead('val', "");
                                            $(obj.currentTarget).val("");
                                        }
                                    });
                                }
                                function loadConfigCountry() {
                                    $('#searchValue').typeahead('destroy');
                                    var ports = new Bloodhound({
                                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                                        local: counlist});
                                    ports.initialize();
                                    $('#searchValue').typeahead(
                                            {
                                                hint: true,
                                                highlight: true,
                                                minLength: 2
                                            },
                                            {
                                                name: 'country',
                                                limit: 5,
                                                displayKey: function (counlist) {
                                                    return counlist.countrycode + " - " + counlist.countryname;
                                                },
                                                source: ports.ttAdapter()}).on('typeahead:render', function (obj, datum) {
                                        $("#typeheadhidden").val("");
                                    }).on('typeahead:selected', function (obj, datum) {
                                        $("#typeheadhidden").val(datum.countryname);
                                    }).on('typeahead:autocomplete', function (obj, datum) {
                                        $("#typeheadhidden").val(datum.countryname);
                                    }).on("typeahead:change", function (obj, datum) {
                                        var hiddenValue = $("#typeheadhidden").val();
                                        if (hiddenValue == null || hiddenValue == "") {
                                            $('#searchValue').typeahead('val', "");
                                            $(obj.currentTarget).val("");
                                        }
                                    });
                                }
                                getFieldConfig();
                                function getFieldConfig() {
                                    $.ajax({
                                        type: "POST",
                                        cache: false,
                                        url: "fieldconfigview.do",
                                        success: function (response) {
                                            $('#fieldconfigdetails').html(response);
                                            $('#fieldconfigdetails').show();
                                        }
                                    });
                                }
                                function editFieldConfigDetails(fieldcode, searchtype, searchvalue, shipmenttype, inputtype, status, id) {
                                    $('#fieldName').val(fieldcode);
                                    $('#searchType').val(searchtype);
                                    $('#searchValue').val(searchvalue);
                                    if (shipmenttype == 'E') {
                                        $('#shipmentType').val("Export");
                                    } else {
                                        $('#shipmentType').val("Import");
                                    }
                                    setInputType(fieldcode);
                                    $('#inputType').val(inputtype);
                                    $('#status').val(status);
                                    $('#configid').val(id);
                                    $('#saveType').val("edit");
                                    $('#fieldName').attr('disabled', true);
                                    $('#searchValue').attr('disabled', false);
                                    $('#searchValue').css("background-color", "white");
                                    if (searchtype == 'C') {
                                        loadConfigCountry();
                                    } else if (searchtype == 'P') {
                                        loadConfigPort();
                                    }
                                }
                                function deleteFieldConfigDetails(id) {
                                    var retVal = false;
                                    retVal = confirm("Do you want to delete?");
                                    if (retVal) {
                                        //  callLoading();
                                        $.ajax({
                                            type: "POST",
                                            cache: false,
                                            url: "ecomAjax?type=deletefieldconfig&id=" + encodeURIComponent(id),
                                            success: function (response) {
                                                // destroyLoading();
                                                getFieldConfig();
                                            }
                                        });
                                    }
                                }
                                function resetFieldConfig() {
                                    $('#fieldName').val("");
                                    $('#searchType').val("");
                                    $('#searchValue').val("");
                                    $('#shipmentType').val("");
                                    $('#inputType').val("");
                                    $('#status').val("");
                                    $('#configid').val("");
                                    $('#saveType').val("save");
                                    $('#searchValue').typeahead('destroy');
                                    $('#searchValue').attr('disabled', true);
                                    $('#fieldName').attr('disabled', false);
                                    $('#searchValue').css("background-color", "#eee");
                                }
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
</script>