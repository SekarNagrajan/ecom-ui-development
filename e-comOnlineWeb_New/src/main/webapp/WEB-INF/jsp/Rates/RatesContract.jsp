<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.vo.Equipments,java.util.Map,net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode = (String) (String) session.getAttribute("clientcode");
    System.out.println("[ECOM-WAR] RateContract clientcode:::" + clientcode);
    String pastdateconfig = (String) session.getServletContext().getAttribute(GeneralConf.PAST_DATE_CONFIG);
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key='ecom.contract.title' bundle="<%=clientcode%>"/></div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">
            <html:form  method="post" action="RateContractSubmit.do" styleId="RatesContractForm"  styleClass="form-horizontal">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-4 swap_input_width">
                            <div class='full-input portsnotes'>
                                <label><bean:message key='Tracking.origin' bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                                <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                                <html:text property="pol"  styleId="pol" styleClass="required"  />
                                <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>
                            </div>
                        </div>
                        <a href="javascript:swapPort();"> <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" /> </a>
                        <div class="col-xs-12 col-sm-4 swap_input_width">
                            <div class='full-input portsnotes'>
                                <label><bean:message key='Tracking.delivery' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                                <html:text property="pod"  styleId="pod" styleClass="required"  />
                                <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-4 swap_input_width">
                            <div class='full-input'>
                                <label><bean:message key='ecom.contract.shipmentdate' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                <img class="input_icon" src="Design/assets/images/page_icons/date.png" />
                                <html:text property="shipmentDate" styleId="shipmentDate" styleClass="required"  />
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-4 swap_input_width">
                            <html:select styleClass="form-control inside_input" styleId="eqpType" property="eqpType">
                                <html:option value="">Select Equipment</html:option>
                                <html:options collection="SESSION_EQUIPMENT_TYPE" property="value" labelProperty="label" />
                            </html:select>
                        </div>
                        <div class="col-xs-12 col-sm-4 m-l-30 swap_input_width">
                            <html:text property="commodity" maxlength="100" styleId="commodity" styleClass="form-control inside_input autocompletetmsg"/>
                            <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
                        </div>
                        <div class="col-xs-12 col-sm-4 p-l-18 p-r-50">
                            <button type="submit" id="searchbtn" class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                        </div>
                    </div>
                </div>
            </html:form>   
        </div>
    </div>
</div>
<div id="rateContractResult">
</div>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsno%>"></script>

<script type="text/javascript">

    var commodityJson = commodityJson;
    function resetValues() {
        $('#pol,#pod,#commodity').val("");
        $("#eqpType").prop('selectedIndex', 0);
        $('#pol,#pod,#commodity').typeahead('val', '');
        var fromdaterfr = new Date();
        fromdaterfr.setDate(fromdaterfr.getDate());
        $('#shipmentDate').datepicker("setDate", fromdaterfr);
        $('#rateContractResult').val("");
        $('#rateContractResult').hide();

    }
    $(document).ready(function () {
        $('#commodity').attr('placeholder', '<bean:message key="ecom.contract.commodity" bundle="<%=clientcode%>"/>');
        $('#pol,#pod').attr('placeholder', '<bean:message key="ecom.schdl.portcode" bundle="<%=clientcode%>"/>');
        $('#shipmentDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>' <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>});
        resetValues();
        getavailablecontract();
        $("#RatesContractForm").validate({
            rules: {
                polname: {
                    required: true
                },
                podname: {
                    required: true
                }

            },
            messages: {
                polname: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                podname: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                $('#btnreset').prop('disabled', true);
                var eqpId = trim($('#eqpType').val());
                var comcode = trim($('#commodity').val());
                var cmdty = "";
                if (comcode != "") {
                    comcode = comcode.split('-');
                    cmdty = comcode[0];
                }
                var pol = trim($('#pol').val());
                var pod = trim($('#pod').val());
                var shipmentDate = trim($('#shipmentDate').val());
                pol = pol.split('-');
                pod = pod.split('-');
                //$("#btnsearch").button('loading');
                openLoader();
                $.ajax({
                    type: "POST",
                    url: "RateContractSubmit.do?eqpType=" + eqpId + "&pol=" + trim(pol[0]) + "&pod=" + trim(pod[0]) + "&commodity=" + trim(cmdty) + "&shipmentDate=" + shipmentDate,
                    success: function (response) {
                        $("#rateContractResult").html(response);
                        $('#rateContractResult').show();
                        //   $('#btnreset').prop('disabled', false);
                        //   $("#btnsearch").button('reset');
                        closeLoader();
                    },
                    error: function (xhr) {
                        if (xhr.status == 403) {
                            window.location.href = "SessionExpired.jsp";
                        }
                    }
                });

                return false;
            }
        });
    });
    function rateContractSurchargeView(rateno, itemno, ratetype) {
        $.ajax({
            type: "POST",
            url: "RatesAjax?rateno=" + rateno + "&itemno=" + itemno + "&ratetype=" + ratetype,
            success: function (response) {
                $.fancybox.open(response, {
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
                    afterShow: function () {
                        $(".fancybox-opened").addClass("modal-draggable");
                        $(".modal-draggable .fancybox-skin").draggable({
                            handle: ".fancybox-outer"
                        });
                    }
                });

            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    var portlist = portJson;
    $(document).ready(function() {
        portlist = updateMaskPortList(portlist);
        
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
    });
    $(document).ready(function() {

        var ports = new Bloodhound({
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

    $(document).ready(function () {
        var fromdate = new Date();
        fromdate.setDate(fromdate.getDate());
        $('#shipmentDate').datepicker("setDate", fromdate);
        loadtypeaheadtooltipmsg();
    });
    function getavailablecontract() {
        $.ajax({
            type: "POST",
            url: "RateContractSubmit.do?eqpType=&pol=&pod=&commodity=&shipmentDate=",
            success: function (response) {
                $("#rateContractResult").html(response);
                $('#rateContractResult').show();
                // $('#btnreset').prop('disabled', false);
                //$("#btnsearch").button('reset');
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function swapPort() {
        var temp = $('#pol').val();
        $('#pol').typeahead("val", $('#pod').val());
        $('#pod').typeahead("val", temp);
    }
</script>