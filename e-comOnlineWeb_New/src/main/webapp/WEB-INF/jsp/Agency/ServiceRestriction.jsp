<%@page contentType="text/html" pageEncoding="UTF-8" import="net.ecom.common.GeneralConf" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>

  <div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Ecom Service Restriction</div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="container">
            <div class="details_search">
                <html:form styleId="ServiceRestrictionForm"  method="post" action="/savesvrcrestrict" styleClass="form-horizontal" >
                    <html:hidden styleId="saveType" property="saveType" />
                    <input type="hidden" id="typeheadhidden" name="typeheadhidden" >
                    <html:hidden styleId="id" property="id" />

                    <div class="form-group row">
                        <label class="col-sm-2 control-label" >Origin<span class="mandatory">*</span></label>
                        <div class="col-sm-3">
                            <html:text property="pla"  styleId="pla"  styleClass="form-control light_border portsnotes required "  />
                        </div>
                        <label class="col-sm-2 control-label">Delivery<span class="mandatory">*</span></label>
                        <div class="col-sm-3">
                            <html:text property="pld"  styleId="pld"  styleClass="form-control light_border portsnotes required " />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label">Service<span class="mandatory">*</span></label>
                        <div class="col-sm-3">
                            <html:text property="service"  styleId="service"  styleClass="form-control light_border required " maxlength="4"/>
                        </div>
                        <label class="col-sm-2 control-label">Status<span class="mandatory">*</span></label>
                        <div class="col-sm-3">
                            <html:select property="status"  styleId="status"  styleClass="form-control light_border required ">
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
                            <button type="submit" class="btn btn-primary btn-lg search_button" id="searchID"> Submit</button>
                            <button type="button" class="btn btn-danger btn-lg search_button" onclick="resetSvrcRestrict();"> Reset</button>
                        </div>
                    </div>
                </html:form>
                
            </div>
        </div>
    </div>
</div>
  <div id="servicerestrictdetails" style="display:none;"></div>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript">
                                var portJson = portJson;
                                $('#saveType').val('save');
                                $(document).ready(function () {
                                    $('#pla').on('typeahead:selected', function (evt, item) {
                                        $('#pla-error').html("");
                                    });
                                    $('#pld').on('typeahead:selected', function (evt, item) {
                                        $('#pld-error').html("");
                                    });
                                    // $('#fieldName,#searchType,#searchValue,#shipmentType,#inputType').attr("placeholder", "In hours");
                                    $("#ServiceRestrictionForm").validate({
                                        rules: {
                                            pla: {
                                                required: true,
                                                notEqualTo: ['#pld']
                                            },
                                            pld: {
                                                required: true,
                                                notEqualTo: ['#pla']
                                            }
                                        }, messages: {
                                            pla: {
                                                required: "This field is required",
                                                notEqualTo: "Origin and Delivery Port Should be Different"
                                            },
                                            pld: {
                                                required: "This field is required",
                                                notEqualTo: "Origin and Delivery Port Should be Different"
                                            }
                                        },
                                        submitHandler: function (form) {
                                            var check = true;
                                        
                                            $.ajax({
                                                type: "POST",
                                                async: false,
                                                cache: false,
                                                url: "AgencyAjax?type=checksvrcrestrict&pla=" + encodeURIComponent($('#pla').val()) + "&pld=" + encodeURIComponent($('#pld').val())
                                                        + "&service=" + $('#service').val() + "&id=" + $('#id').val(),
                                                success: function (response) {
                                                    console.log(response);
                                                    if (response =='Exist') {
                                                      
                                                        check = false;
                                                        alert('Duplicate Service Restriction.');
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
                                    var form = $("#ServiceRestrictionForm").show();
                                    form.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly]";
                                });



                                    $(window).one("load", function () {
                                    var ports = new Bloodhound({
                                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                                        local: portJson
                                    });
                                    ports.initialize();
                                    $('#pla').typeahead(
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
                                            $('#portcode').typeahead('val', "");
                                            $(obj.currentTarget).val("");
                                        }
                                    });
                                });

                                  $(window).one("load", function () {
 
                                    var ports = new Bloodhound({
                                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                                        local: portJson
                                    });
                                    ports.initialize();
                                    $('#pld').typeahead(
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
                                            $('#portcode').typeahead('val', "");
                                            $(obj.currentTarget).val("");
                                        }
                                    });
                                });
                                getServiceRestriction();
                                function getServiceRestriction() {
                                    $.ajax({
                                        type: "POST",
                                        cache: false,
                                        url: "servicerestrictview.do",
                                        success: function (response) {
                                            $('#servicerestrictdetails').html(response);
                                            $('#servicerestrictdetails').show();
                                        }
                                    });
                                }
                                function editFieldConfigDetails(id, pla, pld, service, status) {
                                    $('#id').val(id);
                                    $('#pla').val(pla);
                                    $('#pld').val(pld);
                                    $('#service').val(service);
                                    $('#status').val(status);
                                    $('#saveType').val("edit");
                                }
                                function deleteFieldConfigDetails(id) {
                                    var retVal = false;
                                    retVal = confirm("Do you want to delete?");
                                    if (retVal) {
                                        //callLoading();
                                        $.ajax({
                                            type: "POST",
                                            cache: false,
                                            url: "AgencyAjax?type=deletesvrcrestrict&id=" + encodeURIComponent(id),
                                            success: function (response) {
                                              //  destroyLoading();
                                                getServiceRestriction();
                                            }
                                        });
                                    }
                                }
                                function resetSvrcRestrict() {
                                    $('#pla').val("");
                                    $('#pld').val("");
                                    $('#pla').typeahead('val', "");
                                    $('#pld').typeahead('val', "");
                                    $('#id').val("");
                                    $('#saveType').val("save");
                                    $('#status').val("A");
                                    $('#service').val("");
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






