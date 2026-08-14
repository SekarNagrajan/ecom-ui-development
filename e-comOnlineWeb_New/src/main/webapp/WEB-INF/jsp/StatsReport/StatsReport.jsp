<%-- 
    Document   : StatsReport
    Created on : Sep 16, 2021, 3:17:40 PM
    Author     : gobinath.t
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String strDateFormat = (String) getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />Stats Report</div>
<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body">
            <div class="container">
                <form method="post" action="ecomAjax?type=getStatsReport" id="StatsReportForm">
                    <input type="hidden" id="typeheadhidden" name="typeheadhidden">
                    <input type="hidden" id="typeheadhidden1" name="typeheadhidden1">
                    <input type="hidden" id="typeheadhidden2" name="typeheadhidden2">
                    <input type="hidden" id="typeheadhidden3" name="typeheadhidden3">
                    <input type="hidden" id="typeheadhidden4" name="typeheadhidden4">
                    <input type="hidden" id="typeheadhidden5" name="typeheadhidden5">
                    <input type="hidden" id="typeheadhidden6" name="typeheadhidden6">

                    <div class="form-group">
                        <div class="row">
                            <label class="col-sm-1 control-label"> Options </label>
                            <div class="col-xs-12 col-sm-2">
                                <select name="options"  id="options" class="form-control light_border required" onchange="optionsonchange(this.value)">
                                    <option value="Main">Main Line</option>
                                    <option value="First">First Load</option>
                                    <option value="POL">Sail Date</option>
                                    <option value="POD">Final Port Date</option>
                                    <option value="CRD">Created Date</option>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label"> Service </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="service"  id="service" class="form-control light_border autocompletetmsg"/>
                            </div>

                            <label class="col-sm-1 control-label"> Vessel </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="vessel"  id="vessel" class="form-control light_border autocompletetmsg"/>
                            </div>

                            <label class="col-sm-1 control-label"> Voyage </label>
                            <div class="col-xs-12 col-sm-2">
                                <div class="input-group">
                                    <input type="text" name="voyage"  id="voyage" class="form-control light_border"/>
                                    <div class="input-group-addon group_search"> 
                                        <span class="input-group-text" onclick="getVoyageBoundDetails()" >
                                            <i class="fa fa-search"></i>
                                        </span>
                                    </div>
                                </div>

                            </div>

                        </div>
                    </div>   
                    <div class="form-group">
                        <div class="row">

                            <label class="col-sm-1 control-label"> Bound </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="bound"  id="bound" class="form-control light_border"/>
                            </div> 
                            <div class="col-sm-6 boundcls">
                                <label for="northbound" class="control-label"> <input type="checkbox" id="northbound" name="bound" value="North Bound"> North Bound </label>
                                <label for="southbound" class="control-label"> <input type="checkbox" id="southbound" name="bound" value="South Bound"> South Bound </label>
                                <label for="intertropics" class="control-label"> <input type="checkbox" id="intertropics" name="bound" value="Intertropics"> Intertropics </label>
                                <label for="europe" class="control-label"> <input type="checkbox" id="europe" name="bound" value="Europe"> Europe </label>
                            </div>
                            <label class="col-sm-1 control-label"> PLA </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="pla"  id="pla" class="form-control light_border autocompletetmsg"/>
                            </div>

                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <label class="col-sm-1 control-label"> POL </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="pol"  id="pol" class="form-control light_border autocompletetmsg"/>
                            </div>

                            <label class="col-sm-1 control-label"> POD </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="pod"  id="pod" class="form-control light_border autocompletetmsg"/>
                            </div>

                            <label class="col-sm-1 control-label"> PLD </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="pld"  id="pld" class="form-control light_border autocompletetmsg"/>
                            </div>

                            <label class="col-sm-1 control-label"> From Date </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="fromdate"  id="fromdate" class="form-control light_border "/>
                            </div>

                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <label class="col-sm-1 control-label"> To Date </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="todate"  id="todate" class="form-control light_border"/>
                            </div>

                            <label class="col-sm-1 control-label"> Commodity </label>
                            <div class="col-xs-12 col-sm-2">
                                <input type="text" name="commodity"  id="commodity" class="form-control light_border"/>
                            </div>

                            <label class="col-sm-1 control-label"> Equipment Type </label>
                            <div class="col-xs-12 col-sm-2">
                                <select name="equipmenttype"  id="equipmenttype" class="form-control light_border">
                                </select>
                            </div>

                            <label class="col-sm-1 control-label"> Contract Number </label>
                            <div class="col-xs-12 col-sm-2">
                                <div class="input-group">
                                    <input type="text" name="contractno"  id="contractno" class="form-control light_border" readonly="true"/>
                                    <div class="input-group-addon group_search"> 
                                        <span class="input-group-text" onclick="getContractDetails()" >
                                            <i class="fa fa-search"></i>
                                        </span>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <label class="col-sm-1 control-label"> Amendment Number </label>
                            <div class="col-sm-2">
                                <input type="text" name="amendmentno"  id="amendmentno" class="form-control light_border" readonly="true"/>
                            </div>
                            <div class="col-sm-1"></div>
                            <div class="col-xs-12 col-sm-2 text-center">
                                <button type="submit" class="btn btn-info" style="padding:12px 65px" ><bean:message key="ecom.Search" bundle="<%=clientcode%>"/> </button>
                            </div>
                            <div class="col-xs-12 col-sm-2 text-center">
                                <button type="button" class="btn btn-danger" style="padding:12px 65px" onclick="resetstatsreport()"><bean:message key="ecom.reset" bundle="<%=clientcode%>"/> </button>
                            </div>
                            <div class="col-xs-12 col-sm-4"></div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div id="statsreportviewdiv">
    </div>
</div>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/vessels.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/equipment.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/services.js?<%=jsno%>"></script>
<script type="text/javascript" >
                                    var vesseldet;
                                    $(document).ready(function () {
                                        $('#fromdate').attr('placeholder', '<bean:message key="ebooking.label.fromdate" bundle="<%=clientcode%>"/>');
                                        $('#todate').attr('placeholder', '<bean:message key="ebooking.label.todate" bundle="<%=clientcode%>"/>');
                                        $('#fromdate,#todate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
                                        $('#fromdate,#todate').attr('autocomplete', 'off');
                                        var fromdaterfr = new Date();
                                        var todaterfr = new Date();
                                        fromdaterfr.setDate(fromdaterfr.getDate() - 30);
                                        todaterfr.setDate(todaterfr.getDate());
                                        $('#fromdate').datepicker("setDate", fromdaterfr);
                                        $('#todate').datepicker("setDate", todaterfr);
                                        $('#options').val('POL');
                                        $('#service,#vessel,#voyage,#bound').prop('readonly', true);
                                        $("#StatsReportForm").validate({
                                            rules: {
                                                fromdate: {
                                                    required: true
                                                },
                                                todate: {
                                                    required: true
                                                }
                                            },
                                            messages: {
                                                fromdate: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                },
                                                todate: {
                                                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                                }
                                            },
                                            submitHandler: function (form) {
                                                if (daysbetweentwodate() <= 90) {
                                                    var urlval = "";
                                                    var fdate = $('#fromdate').val();
                                                    var tdate = $('#todate').val();
                                                    var pla = $('#pla').val();
                                                    var pol = $('#pol').val();
                                                    var pod = $('#pod').val();
                                                    var pld = $('#pld').val();
                                                    var vessel = $('#vessel').val();
                                                    var bound = $('#bound').val();
                                                    var commodity = $('#commodity').val();
                                                    var service = $('#service').val();
                                                    var northbound = ($("#northbound").prop('checked') == true ? 'Y' : '');
                                                    var southbound = ($("#southbound").prop('checked') == true ? 'Y' : '');
                                                    var intertropics = ($("#intertropics").prop('checked') == true ? 'Y' : '');
                                                    var europe = ($("#europe").prop('checked') == true ? 'Y' : '');
                                                    vessel = vessel.split("-")[0];
                                                    pla = pla.split("-")[0];
                                                    pol = pol.split("-")[0];
                                                    pod = pod.split("-")[0];
                                                    pld = pld.split("-")[0];
                                                    commodity = commodity.split("-")[0];
                                                    service = service.split("-")[0];
                                                    urlval = "ecomAjax?type=getStatsReport" + "&fromdate=" + fdate + "&todate=" + tdate + "&options=" + $('#options').val() + "&service=" + service + "&vessel=" + vessel + "&voyage=" + $('#voyage').val() + "&pla=" + pla + "&pol=" + pol + "&pld=" + pld + "&pod=" + pod + "&commodity=" + commodity + "&equipmenttype=" + $('#equipmenttype').val() + "&contractno=" + $('#contractno').val() + "&northbound=" + northbound + "&southbound=" + southbound + "&intertropics=" + intertropics + "&europe=" + europe + "&bound=" + bound + "&amendmentno=" + $('#amendmentno').val();
                                                    openLoader();
                                                    $.ajax({
                                                        type: "POST",
                                                        url: urlval,
                                                        success: function (response) {
                                                            $("#statsreportviewdiv").html(response);
                                                            closeLoader();
                                                        },
                                                        error: function (xhr) {
                                                            if (xhr.status == 403) {
                                                                window.location.href = "SessionExpired.jsp";
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                } else {
                                                    alert('Difference between From and To date should be less than or equal to 90 days');
                                                }
                                            }
                                        });
                                        $('#equipmenttype').html('');
                                        var allEquipmentListJs = '<option value="">Select Eqp Size</option>';
                                        $.each(EquipmentJson, function (i, obj) {
                                            //use obj.id and obj.name here, for example:
                                            allEquipmentListJs = allEquipmentListJs + '<option value="' + obj.equipSize + '">' + obj.equipDesc + '</option>'
                                        });
                                        $('#equipmenttype').append(allEquipmentListJs);
                                    });
                                    var portlist = portJson;
                                    $(window).one("load", function () {
                                        var ports = new Bloodhound({
                                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                                            local: portlist
                                        });
                                        ports.initialize();
                                        $('#pol').typeahead(
                                                {
                                                    hint: true,
                                                    highlight: true,
                                                    minLength: 2
                                                },
                                                {
                                                    name: 'ports',
                                                    limit: 20,
                                                    displayKey: function (portlist) {
                                                        return portlist.PortCode + " - " + portlist.PortName;
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
                                                $('#pol').typeahead('val', "");
                                                $(obj.currentTarget).val("");
                                            }
                                        });

                                        ports = new Bloodhound({
                                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                                            local: portlist
                                        });
                                        ports.initialize();
                                        $('#pod').typeahead(
                                                {
                                                    hint: true,
                                                    highlight: true,
                                                    minLength: 2
                                                },
                                                {
                                                    name: 'ports',
                                                    limit: 20,
                                                    displayKey: function (portlist) {
                                                        return portlist.PortCode + " - " + portlist.PortName;
                                                    },
                                                    source: ports.ttAdapter()
                                                }).on('typeahead:render', function (obj, datum) {
                                            $("#typeheadhidden1").val("");
                                        }).on('typeahead:selected', function (obj, datum) {
                                            $("#typeheadhidden1").val(datum.PortName);
                                        }).on('typeahead:autocomplete', function (obj, datum) {
                                            $("#typeheadhidden1").val(datum.PortName);
                                        }).on("typeahead:change", function (obj, datum) {
                                            var hiddenValue = $("#typeheadhidden1").val();
                                            if (hiddenValue == null || hiddenValue == "") {
                                                $('#pod').typeahead('val', "");
                                                $(obj.currentTarget).val("");
                                            }
                                        });
                                        ports = new Bloodhound({
                                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                                            local: portlist
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
                                                    displayKey: function (portlist) {
                                                        return portlist.PortCode + " - " + portlist.PortName;
                                                    },
                                                    source: ports.ttAdapter()
                                                }).on('typeahead:render', function (obj, datum) {
                                            $("#typeheadhidden2").val("");
                                        }).on('typeahead:selected', function (obj, datum) {
                                            $("#typeheadhidden2").val(datum.PortName);
                                        }).on('typeahead:autocomplete', function (obj, datum) {
                                            $("#typeheadhidden2").val(datum.PortName);
                                        }).on("typeahead:change", function (obj, datum) {
                                            var hiddenValue = $("#typeheadhidden2").val();
                                            if (hiddenValue == null || hiddenValue == "") {
                                                $('#pla').typeahead('val', "");
                                                $(obj.currentTarget).val("");
                                            }
                                        });
                                        ports = new Bloodhound({
                                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                                            local: portlist
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
                                                    displayKey: function (portlist) {
                                                        return portlist.PortCode + " - " + portlist.PortName;
                                                    },
                                                    source: ports.ttAdapter()
                                                }).on('typeahead:render', function (obj, datum) {
                                            $("#typeheadhidden3").val("");
                                        }).on('typeahead:selected', function (obj, datum) {
                                            $("#typeheadhidden3").val(datum.PortName);
                                        }).on('typeahead:autocomplete', function (obj, datum) {
                                            $("#typeheadhidden3").val(datum.PortName);
                                        }).on("typeahead:change", function (obj, datum) {
                                            var hiddenValue = $("#typeheadhidden3").val();
                                            if (hiddenValue == null || hiddenValue == "") {
                                                $('#pld').typeahead('val', "");
                                                $(obj.currentTarget).val("");
                                            }
                                        });
                                        var commodity = new Bloodhound({
                                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                                            local: commodityJson
                                        });
                                        commodity.initialize();
                                        $('#commodity').typeahead(
                                                {
                                                    hint: true,
                                                    highlight: true,
                                                    minLength: 2
                                                },
                                                {
                                                    name: 'commodity',
                                                    limit: 5,
                                                    displayKey: function (commodityJson) {
                                                        return commodityJson.commoditycode + " - " + commodityJson.commodityname;
                                                    },
                                                    source: commodity.ttAdapter()
                                                }).on('typeahead:render', function (obj, datum) {
                                            $("#typeheadhidden4").val("");
                                        }).on('typeahead:selected', function (obj, datum) {
                                            $("#typeheadhidden4").val(datum.commodityname);
                                        }).on('typeahead:autocomplete', function (obj, datum) {
                                            $("#typeheadhidden4").val(datum.commodityname);
                                        }).on("typeahead:change", function (obj, datum) {
                                            var hiddenValue = $("#typeheadhidden4").val();
                                            if (hiddenValue == null || hiddenValue == "") {
                                                $('#commodity').typeahead('val', "");
                                                $(obj.currentTarget).val("");
                                            }
                                        });

                                        var service = new Bloodhound({
                                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('ServiceCode', 'ServiceName'),
                                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                                            local: ServiceJson
                                        });
                                        service.initialize();
                                        $('#service').typeahead(
                                                {
                                                    hint: true,
                                                    highlight: true,
                                                    minLength: 1
                                                },
                                                {
                                                    name: 'ports',
                                                    limit: 5,
                                                    displayKey: function (ServiceJson) {
                                                        return ServiceJson.ServiceCode + " - " + ServiceJson.ServiceName;
                                                    },
                                                    source: service.ttAdapter()
                                                }).on('typeahead:render', function (obj, datum) {
                                            $("#typeheadhidden6").val("");
                                        }).on('typeahead:selected', function (obj, datum) {
                                            $("#typeheadhidden6").val(datum.ServiceName);
                                        }).on('typeahead:autocomplete', function (obj, datum) {
                                            $("#typeheadhidden6").val(datum.ServiceName);
                                        }).on("typeahead:change", function (obj, datum) {
                                            var hiddenValue = $("#typeheadhidden6").val();
                                            if (hiddenValue == null || hiddenValue == "") {
                                                $('#service').typeahead('val', "");
                                                $(obj.currentTarget).val("");
                                            }
                                            loadVesslDetails();
                                        });
                                    });

                                    function optionsonchange(val) {
                                        if (val == 'Main' || val == 'First') {
                                            $('#service,#vessel,#voyage,#bound').prop('readonly', false);
                                            $('#fromdate,#todate').prop('readonly', true);
                                            $('#fromdate,#todate').val('');
                                        } else {
                                            $('#service,#vessel,#voyage,#bound').prop('readonly', true);
                                            $('#fromdate,#todate').prop('readonly', false);
                                            $('#voyage,#bound').val('');
                                            $('#service,#vessel').typeahead('val', '');
                                        }
                                    }
                                    function loadVesslDetails() {
                                        $('#vessel').typeahead('destroy');
                                        if ($('#service').val().length > 0) {
                                            var service = $('#service').val();
                                            service = service.split("-")[0];
                                            var VesselList = '';
                                            $.ajax({
                                                type: "POST",
                                                url: "ecomAjax?type=getvesseldetails&service=" + service,
                                                success: function (response) {
                                                    VesselList = response;
                                                    loadVessel(VesselList);
                                                },
                                                error: function (xhr) {
                                                    if (xhr.status == 403) {
                                                        window.location.href = "SessionExpired.jsp";
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    function getVoyageBoundDetails() {
                                        if ($('#service').val().length > 0 && $('#vessel').val().length > 0) {
                                            var service = $('#service').val();
                                            var vessel = $('#vessel').val();
                                            service = service.split("-")[0];
                                            vessel = vessel.split("-")[0];
                                            $.ajax({
                                                type: "POST",
                                                url: "ecomAjax?type=getvoyagedetails&service=" + service + "&vessel=" + vessel,
                                                success: function (response) {
                                                    openFancyBox(response);
                                                },
                                                error: function (xhr) {
                                                    if (xhr.status == 403) {
                                                        window.location.href = "SessionExpired.jsp";
                                                    }
                                                }
                                            });
                                        } else {
                                            alert("Kindly select the Service and Vessel Details.");
                                        }

                                    }
                                    function openFancyBox(data) {
                                        $.fancybox.open(data, {
                                            showCloseButton: false,
                                            closeClick: false,
                                            maxWidth: 800,
                                            maxHeight: 500,
                                            fitToView: true,
                                            width: '60%',
                                            height: '80%',
                                            autoSize: false,
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
                                    function setVoyageBoundDetails(voyage, bound) {
                                        $('#voyage').val(voyage);
                                        $('#bound').val(bound);
                                        $.fancybox.close();
                                    }
                                    function loadVessel(VesselList) {
                                        vesseldet = jQuery.parseJSON(VesselList);
                                        var vessel = new Bloodhound({
                                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('vesselname', 'vesselcode'),
                                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                                            local: vesseldet
                                        });
                                        vessel.initialize();
                                        $('#vessel').typeahead(
                                                {
                                                    hint: true,
                                                    highlight: true,
                                                    minLength: 1
                                                },
                                                {
                                                    name: 'ports',
                                                    limit: 5,
                                                    displayKey: function (VesselJson) {
                                                        return VesselJson.vesselcode + " - " + VesselJson.vesselname;
                                                    },
                                                    source: vessel.ttAdapter()
                                                }).on('typeahead:render', function (obj, datum) {
                                            $("#typeheadhidden5").val("");
                                        }).on('typeahead:selected', function (obj, datum) {
                                            $("#typeheadhidden5").val(datum.vesselname);
                                        }).on('typeahead:autocomplete', function (obj, datum) {
                                            $("#typeheadhidden5").val(datum.vesselname);
                                        }).on("typeahead:change", function (obj, datum) {
                                            var hiddenValue = $("#typeheadhidden5").val();
                                            if (hiddenValue == null || hiddenValue == "") {
                                                $('#vessel').typeahead('val', "");
                                                $(obj.currentTarget).val("");
                                            }
                                        });
                                    }
                                    function setContractDetails(contno, amendno) {
                                        $('#contractno').val(contno);
                                        $('#amendmentno').val(amendno);
                                        $.fancybox.close();
                                    }
                                    function getContractDetails() {
                                        $.ajax({
                                            type: "POST",
                                            url: "ecomAjax?type=getcontractdetails",
                                            success: function (response) {
                                                openFancyBox(response);
                                            },
                                            error: function (xhr) {
                                                if (xhr.status == 403) {
                                                    window.location.href = "SessionExpired.jsp";
                                                }
                                            }
                                        });


                                    }
                                    function resetstatsreport() {
                                        $('#options').val('POL');
                                        $('#service,#vessel,#voyage,#bound,#fromdate,#todate,#equipmenttype,#contractno,#amendmentno,#commodity,#pol,#pod,#pld,#pla').val('');
                                        $('#service,#vessel,#commodity,#pol,#pod,#pld,#pla').typeahead('val', '');
                                        $('#service,#vessel,#voyage,#bound').prop('readonly', true);
                                        $('#fromdate,#todate').prop('readonly', false);
                                        $('#northbound,#southbound,#intertropics,#europe').prop('checked', false);
                                        var fromdaterfr = new Date();
                                        var todaterfr = new Date();
                                        fromdaterfr.setDate(fromdaterfr.getDate() - 30);
                                        todaterfr.setDate(todaterfr.getDate());
                                        $('#fromdate').datepicker("setDate", fromdaterfr);
                                        $('#todate').datepicker("setDate", todaterfr);
                                    }
                                    function daysbetweentwodate() {
                                        if ($('#fromdate').val().length > 0 && $('#todate').val().length > 0) {
                                            var date1 = new Date($('#fromdate').val());
                                            var date2 = new Date($('#todate').val());
                                            var diffTime = Math.abs(date2 - date1);
                                            var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                                            console.log(diffDays + " days");
                                            return diffDays;
                                        } else {
                                            return 0;
                                        }
                                    }
</script>

