<%-- Document : RateRequest Created on : Dec 4, 2017, 3:25:56 PM Author :
deepa.p --%> 
<%@page import="net.ecom.vo.DropDownVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedHashMap"%> <%@page import="net.ecom.common.Validation,java.util.ArrayList,java.util.Hashtable,java.util.Map,net.ecom.common.GeneralConf"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %> 
<%@ taglib  prefix="bean" uri="/WEB-INF/struts-bean.tld" %> 
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %> 
<%@ page contentType="text/html;charset=iso-8859-1" language="java"%> 

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String pastdateconfig = (String) session.getServletContext().getAttribute(GeneralConf.PAST_DATE_CONFIG);
    String loginName = (String) session.getAttribute("SESSION_LOGINNAME");
    loginName = loginName != null && loginName.length() > 0 ? loginName : "";
    String custName = (String) session.getAttribute("SESSION_USER_CONTACT");
    String custCode = (String) session.getAttribute("SESSION_CUSTCODE");
    String compName = (String) session.getAttribute("apname");
    compName = compName != null && compName.length() > 0 ? compName : "";
    System.out.println("[ECOM-WAR] RateRequest compName>>" + compName);

    LinkedHashMap eqpmap = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");

    List tlist = (List) getServletContext().getAttribute("TransportServiceList");
    List<DropDownVO> tservicelist = null;
    if (tlist != null && tlist.size() > 0) {
        tservicelist = (List<DropDownVO>) tlist.get(0);
    }

    List<DropDownVO> toslist = (List<DropDownVO>) getServletContext().getAttribute("TOSList");
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>

<div class="card-header page_header fixed-top d-flex justify-content-between align-items-center">
    <span>
        <img class="header_icon_pages" src="Design/assets/images/page_icons/rate-request.png" />
        <bean:message key="ecom.rr.quote" bundle="<%=clientcode%>" />
    </span>

</div>

<div class="container-fluid page_bottom_content">
    <div class="card card-body card-shadow">
        <div class="details_search">
            <div class="text-center">

            </div>

            <form id="CreateQuoteForm" class="form-horizontal" action="saveQuotation.do" method="post">

                <input type="hidden" id="typeheadhidden"/>
                <input type="hidden" id="typeheadhidden1"/>
                <input type="hidden" id="typeheadhidden2"/>

                <!-- Company, From, To, T-Service -->
                <div class="row mb-3">
                    <div class="col-md-3">
                        <label>Company <span class="mandatory">*</span> </label>
                        <input type="text" class="form-control light_border required" name="companyName" id="companyName" maxlength="150" disabled="true"  value="<%=custName%>"/>
                        <input type="hidden" name="companyCode" id="companyCode" value="<%=custCode%>" />
                    </div>
                    <div class="col-md-3">
                        <label>From Location <span class="mandatory">*</span></label>
                        <input type="text" class="form-control light_border required" name="origin" id="origin" />
                    </div>
                    <div class="col-md-3">
                        <label>To Location <span class="mandatory">*</span></label>
                        <input type="text" class="form-control light_border required" name="destination" id="destination" />
                    </div>
                    <div class="col-md-3">
                        <label>Shipment Date <span class="mandatory">*</span></label>
                        <input type="text" class="form-control light_border  datecheck required" name="shipmentDate" id="shipmentDate" />
                        <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>

                    </div>

                </div>

                <!-- Container Type, Pre/On Carriage, Commodity -->
                <div class="row mb-3 align-items-center">
                    <div class="col-md-3">
                        <label>Container Type <span class="mandatory">*</span></label>
                        <select class="form-control light_border required" name="containerTypeDropdown" id="containerTypeDropdown">
                            <%
                                if (eqpmap != null && !eqpmap.isEmpty()) {
                                    for (Object key : eqpmap.keySet()) {
                                        String label = (String) eqpmap.get(key);
                                        // Set DV20 as default
                                        String selected = (key.equals("20DC")) ? "selected" : "";
                            %>
                            <option value="<%= key%>" <%= selected%>><%= label%></option>
                            <%
                                }
                            } else {
                            %>
                            <option value="">No Equipment Types Available</option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label>Quantity <span class="mandatory">*</span></label>
                        <input type="number" class="form-control light_border number required" name="quantity" id="quantity" min="1" value="1" />
                    </div>
                    <div class="col-md-3">
                        <label>Weight(MT) <span class="mandatory">*</span></label>
                        <input type="number" class="form-control light_border number required" name="weight" id="weight" min="0" />
                    </div>
                    <div class="col-md-3">
                        <label>Commodity <span class="mandatory">*</span></label>
                        <input type="text" class="form-control light_border required" name="commodity" id="commodity" />
                    </div>


                </div>

                <!-- Container Type Dropdown, Quantity, TOS, Shipment Date -->
                <div class="row mb-3 align-items-center">
                    <div class="col-md-3">
                        <label>Pre Carriage</label><br />
                        <div class="btn-group booking_btn" id="preCarriage_btn">
                            <button type="button" class="btn btn-primary car">Carrier</button>
                            <button type="button" class="btn btn-primary mer">Merchant</button>
                        </div>
                        <input type="hidden" name="preCarriage" id="preCarriage"/>
                    </div>
                    <div class="col-md-3">
                        <label>On Carriage</label><br />
                        <div class="btn-group booking_btn" id="onCarriage_btn">
                            <button type="button" class="btn btn-primary car">Carrier</button>
                            <button type="button" class="btn btn-primary mer">Merchant</button>
                        </div>
                        <input type="hidden" name="onCarriage" id="onCarriage"/>
                    </div>
                    <div class="col-md-3">
                        <label>TOS</label>
                        <select class="form-control light_border required" name="tos" id="tos">
                            <%
                                if (toslist != null && !toslist.isEmpty()) {
                                    for (DropDownVO tos : toslist) {
                                        String label = tos.getName();

                                        String selected = (tos.getCode().equals("CIF")) ? "selected" : "";
                            %>
                            <option value="<%= tos.getCode()%>" <%= selected%>><%= label%></option>
                            <%
                                }
                            } else {
                            %>
                            <option value="">No TOS Available</option>
                            <%
                                }
                            %>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label>T-Service</label>
                        <input type="hidden" name="tservicename" id="tservicename" value="CY / CY"/>
                        <select class="form-control light_border required" name="tservice" id="tservice" onchange="settservicename(this)">
                            <%
                                if (tservicelist != null && !tservicelist.isEmpty()) {
                                    for (DropDownVO tser : tservicelist) {
                                        String label = tser.getName();

                                        String selected = (tser.getCode().equals("MM")) ? "selected" : "";
                            %>
                            <option value="<%= tser.getCode()%>" <%= selected%>><%= label%></option>
                            <%
                                }
                            } else {
                            %>
                            <option value="">No T-Service Available</option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                </div>

                <!-- Weight, Flags, Req Type, Request Rate -->
                <div class="row mb-3 align-items-center">

                    <div class="col-md-3 d-flex align-items-center">
                        <div class="form-check mr-3">
                            <input class="form-check-input" type="checkbox" name="dg" id="dg" />
                            <label class="form-check-label" for="dg">DG</label>
                        </div>
                        <div class="form-check mr-3">
                            <input class="form-check-input" type="checkbox" name="soc" id="soc" />
                            <label class="form-check-label" for="soc">SOC</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="nor" id="nor" />
                            <label class="form-check-label" for="nor">NOR</label>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <label>Req Type</label><br />
                        <div class="btn-group reqType_btn" id="reqType_btn">
                            <button type="button" class="btn btn-primary ofr">OFR</button>
                            <button type="button" class="btn btn-primary allin">ALL_IN</button>
                        </div>
                        <input type="hidden" name="reqType" id="reqType"/>
                    </div>
                    <div class="col-md-3">
                        <label>Request Rate(USD) <span class="mandatory">*</span></label>
                        <input type="number" class="form-control light_border required" name="requestRate" id="requestRate" min="0"  />
                    </div>
                </div>

                <!-- Remarks -->
                <div class="row mb-3">
                    <div class="col-md-12">
                        <label>Remarks</label>
                        <textarea class="form-control light_border " name="remarks" id="remarks" rows="2"></textarea>
                    </div>
                </div>



                <!-- Charges Table -->
                <div class="row mb-3">
                    <div class="col-md-12">
                        <input type="hidden" id="surchargeList" name="surchargeList"/>
                        <table class="table table-bordered table-striped"   id="surchargetable">
                            <thead>
                                <tr>
                                    <th>VATOS</th>
                                    <th>Charges</th>
                                    <th>Currency</th>
                                    <th>Allin</th>
                                    <th>Tariff(LOC)</th>
                                    <th>Tariff(USD)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td colspan="6" class="text-center">No Rows To Show</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="row mb-3">
                  <div class="col-sm-5 pull-right">                  
                  </div>
                  <div class="col-sm-2 pull-right">
                      <button type="submit" class="btn btn-primary btn-lg btn_block_custom" name="submit" value="Submit" autocomplete="off" id="btnsearch">   <bean:message key="ecom.submit" bundle="<%=clientcode%>"/> </button>
                  </div>
                  
              </div>
            </form>
        </div>
    </div>
</div>



<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsno%>" ></script>
<script type="text/javascript" src="/Ecommaster/equipment.js?<%=jsno%>"></script>
<script>
    $(document).ready(function () {
        var currentDate = new Date(); // Get the current date
        $('#shipmentDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>' <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>}).datepicker('setDate', currentDate);

        $('#commodity').val('999 - FAK - FREIGHT ALL KIND');
        $('#preCarriage').val('C');
        $('#onCarriage').val('C');
        $('#reqType').val('OFR');
        $('#preCarriage_btn').find('button.car').addClass('active');
        $('#onCarriage_btn').find('button.car').addClass('active');
        $('#reqType_btn').find('button.ofr').addClass('active');
        $('#weight').val('13');
        $('#commodity').typeahead('val', '999 - FAK - FREIGHT ALL KIND');
        $("#typeheadhidden2").val('999 - FAK - FREIGHT ALL KIND');

        // Add change event listeners for rate calculation
        $('#origin, #destination, #commodity, #weight, #tservice, #requestRate, #containerTypeDropdown').on('change', function() {
            calculateRate();
        });

        $("#CreateQuoteForm").validate({
            submitHandler: function (form) {
                openLoader();
                return true;
            }
        });
    });

    function calculateRate() {
        if($("#CreateQuoteForm").valid()){
            // Get values from form
            var origin = $('#origin').val().split(' - ')[0];
            var destination = $('#destination').val().split(' - ')[0];
            var commodity = $('#commodity').val();
            var weight = $('#weight').val();
            var tservice = $('#tservice').val();
            var tservicename = $('#tservicename').val();
            var requestRate = $('#requestRate').val();
            var containerType = $('#containerTypeDropdown').val();
            var custCode = $('#companyCode').val();
            requestRate= requestRate?requestRate:0;
            // Extract commodity code from the commodity value (format: "999 - FAK - FREIGHT ALL KIND")
            var commodityCode = commodity.split(' - ')[0];

            // Extract service code from tservice value
            var serviceCode = tservice;

            // Make AJAX call
            $.ajax({
                url: 'QuotationAjax',
                type: 'POST',
                data: {
                    origin: origin,
                    delivery: destination,
                    commodityCode: commodityCode,
                    weight: weight,
                    serviceCode: serviceCode,
                    tservicename: tservicename,
                    requestRate: requestRate,
                    containerType: containerType,
                    custCode: custCode
                },
                success: function(response) {
                    if(response.status === 'success') {
                        // Update the charges table with the response
                        updateChargesTable(response.surchargeList);
                        $("#surchargeList").val(JSON.stringify(response.surchargeList));
                    } else {
                        console.error('Error calculating rate:', response.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('AJAX Error:', error);
                }
            });
        }   
    }

        function updateChargesTable(surchargeList) {
            var tbody = $('#surchargetable').find('tbody');
            tbody.empty();

            if(surchargeList && surchargeList.length > 0) {
                surchargeList.forEach(function(charge) {
                    var row = $('<tr>');
                    row.append($('<td>').text('Y'));
                    row.append($('<td>').text(charge?.chgs || ''));
                    row.append($('<td>').text(charge?.curr || ''));
                    row.append($('<td>').html('<input type="checkbox" id="allin"  />'));
                    row.append($('<td>').text(charge?.tarr || ''));
                    row.append($('<td>').text(charge?.usd || ''));
                    tbody.append(row);
                });
            } else {
                tbody.append('<tr><td colspan="6" class="text-center">No Rows To Show</td></tr>');
            }
        }

    $(window).one("load", function () {
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: portlist
        });
        ports.initialize();
        $('#origin').typeahead(
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
                $('#origin').typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    });
    var commodityJson = commodityJson;
    var portlist = portJson;
    var EquipmentJson = EquipmentJson;
    $(window).one("load", function () {
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: portlist
        });
        ports.initialize();
        $('#destination').typeahead(
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
                $('#destination').typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    });

    $(window).one("load", function () {
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
                    name: 'tarrifCommodity',
                    limit: 5,
                    displayKey: function (commodityJson) {
                        return commodityJson.commoditycode + " - " + commodityJson.commodityname;
                    },
                    source: commodity.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#typeheadhidden2").val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#typeheadhidden2").val(datum.commodityname);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#typeheadhidden2").val(datum.commodityname);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#typeheadhidden2").val();
            if (hiddenValue == null || hiddenValue == "") {
                $('#commodity').typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    });
    function settservicename(tservice) {
        var tserviceName = tservice.options[tservice.selectedIndex].text;
        $("#tservicename").val(tserviceName);
    }

    $(".btn-group > button").click(function () {
            $(this).parent('.btn-group').find('button').removeClass("active");
            $(this).addClass("active");
            if ($(this).parent().is('#preCarriage_btn')) {
                if ($(this).is('.car')) {
                    $('#preCarriage').val('C');
                } else if ($(this).is('.mer')) {
                    $('#preCarriage').val('M');
                } else if ($(this).is('.na')) {
                    $('#preCarriage').val('N');
                }
            } else if ($(this).parent().is('#onCarriage_btn')) {
                if ($(this).is('.car')) {
                    $('#onCarriage').val('C');
                } else if ($(this).is('.mer')) {
                    $('#onCarriage').val('M');
                } else if ($(this).is('.na')) {
                    $('#onCarriage').val('N');
                }
            } else if ($(this).parent().is('#reqType_btn')) {
                if ($(this).is('.ofr')) {
                    $('#reqType').val('OFR');
                } else if ($(this).is('.allin')) {
                    $('#reqType').val('ALL_IN');
                }
            }
        });
</script>