<%@page import="net.ecom.common.GeneralConf"%>
<%@ page contentType="text/html;" language="java" import="java.util.Map,java.util.ArrayList,java.util.Hashtable,net.ecom.vo.UnclearedCargoVo,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%
   /* Map<String, String> dashboardMap = (Map<String, String>) request.getAttribute("CUSTOMERDASHBOARDCOUNT");
    String topDesStr = null, topPortPairStr = null, topDestTeu = null;
    topDesStr = dashboardMap.get("TOPDESTINATIONCOUNT");
    topPortPairStr = dashboardMap.get("TOPPORTPAIRCOUNT");
    topDestTeu = dashboardMap.get("TOPDESTINATIONTEUCOUNT");*/

    String blNumber, loadport, dischargeport, vessArrDate, actCont, delCont, remCont;
    int remcunt = 0;
    UnclearedCargoVo dasvo = null;
    int remainingsum = 0, deliveredsum = 0;
    Hashtable portlist = (Hashtable) request.getServletContext().getAttribute("SESSION_PORTLIST");
    String Dash_display = (String) request.getServletContext().getAttribute(GeneralConf.DASHBOARD_DISPLAY);
    String clientcode = (String) session.getAttribute("clientcode");
    String Enable_DashBoard_Filter = (String) request.getServletContext().getAttribute(GeneralConf.Enable_DashBoard_Filter);

%>
<script type="text/javascript">
    <%if ("Y".equalsIgnoreCase(Enable_DashBoard_Filter)) { %>
    var  monthArray =["January","February","March","April","May","June","July","August","September","October","November","December"];
    $(document).ready(function() {
        var filterSelect = document.getElementById("filtervalue");
        var today = new Date();
        var month;
        var year;
        var d;
          for(var i = 0; i < 6; i++) {
            d = new Date(today.getFullYear(), today.getMonth() - i, 1);
            month = d.getMonth();
            year = d.getFullYear();
            filterSelect.options.add( new Option(monthArray[month]+"-"+year,month+1+"-"+year));
        }
        console.log("filterSelect",filterSelect);
        $('#filtervalue').index(0);
        filterData();
    }); 
    <%}%>
    function filterData()
    {
      // callLoading();
       var selectedMonth=$("#filtervalue").val();
        console.log("selectedMonth====>",selectedMonth);
      
       document.getElementById('processLoad').style.display="block";
        $.ajax({
            type: "POST",
            async:false,
            cache : false,
            url: "Accesslogin.do?type=dashboardFilterValBased&selectedMonth="+selectedMonth,
            success: function (data) {
               //destroyLoading();
                $("#filtervalbased").html(data);
                document.getElementById('processLoad').style.display="none";
                $("#filtervalbased").show();
                
            },
            error: function (xhr) {
                if(xhr.status==403){
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
</script>
<div class="card-header page_header  fixed-top"> <img class="header_ico\n_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.dashboard" bundle="<%=clientcode%>"/> </div>
<div class="container-fluid page_bottom_content">
    <%if ("Y".equalsIgnoreCase(Enable_DashBoard_Filter)) { %>
        <div class="col-sm-12 text-center" style="margin-top: 15px;padding-bottom: 12px;padding-top: 10px;">
            <label for="filtervalue" class="control-label" style="padding-top:10px; margin-right: 10px;">Filter By Month</label>
                  <select id="filtervalue" name="filtervalue" class="form-control input-sm selectwithsearch d-inline-block"  style="width: 200px; display: inline-block;" onchange="filterData()">
                 </select> 
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div id="filtervalbased" style="display: none"></div>
            </div>
        </div>
        
    <%}else{%>
        <jsp:include page="DashBoardCharts.jsp"/>
        <%}%>

    <div class="row" style="display: none">
        <div class="col-md-4">

            <div class="card panel-info mb-3">
                <div class="card-header panel-heading"><i class="fa fa-tasks m-r-10"></i> </span><bean:message key="ecom.dash.dest" bundle="<%=clientcode%>"/>
                    <div class="pull-right">
                        <a href="#" onclick="hidecustomepanel('info')"><i class="fa fa-chevron-circle-down"></i></a>
                    </div>
                </div>


                <div class="panel-body" id="panel-info">
                    <div id="topdestinationdiv" style="height:250px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card panel-warning  mb-3">
                <div class="card-header panel-heading"><i class="fa fa-print m-r-10"></i><bean:message key="ecom.dash.port" bundle="<%=clientcode%>"/>
                    <div class="pull-right">
                        <a href="#" onclick="hidecustomepanel('warning')"><i class="fa fa-chevron-circle-down"></i></a>

                    </div>
                </div>
                <div class="panel-body" id="panel-warning">
                    <div id="topportpairdiv" style="height:250px;"></div>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card panel-danger  mb-3">
                <div class="card-header panel-heading"><i class="fa fa-ship m-r-10"></i> <bean:message key="ecom.dash.teu" bundle="<%=clientcode%>"/>
                    <div class="pull-right">


                        <a href="#" onclick="hidecustomepanel('danger')"><i class="fa fa-chevron-circle-down"></i></a>
                    </div>

                </div>
                <div class="panel-body" id="panel-danger">
                    <div id="topdestteudiv" style="height:250px;"></div>
                </div>
            </div>
        </div>
    </div>
    <%if ("Y".equalsIgnoreCase(Dash_display)) {
            String outbal = (String) request.getAttribute("DASH_OUTSTANDING_BALANCE");
            remainingsum = (int) request.getAttribute("dashremainingsum");
            deliveredsum = (int) request.getAttribute("dashdeliveredsum");
            System.out.println("[ECOM-WAR] dashboard outbal=========> " + outbal);
            boolean outbal_available = false;
            if (outbal != null && outbal.length() > 0) {
                outbal_available = true;
            }
            ArrayList daslis = (ArrayList) request.getAttribute("UnclearedCargoDetails");
    %>
    <!-- new dashboard summary start-->
    <div class="row">
        <div class="col-md-4">
            <div class="card panel-danger  mb-3">
                <div class="card-header panel-heading"><i class="fa fa-print m-r-10"></i> <bean:message key="ecom.dashb.outbal" bundle="<%=clientcode%>"/>
                    <div class="pull-right">
                        <a href="#" onclick="hidecustomepanel('out_bal')"><i class="fa fa-chevron-circle-down"></i></a>

                    </div>
                </div>
                <div class="panel-body" id="panel-out_bal">
                    <div  style="height:250px;">
                        <%if (outbal_available) {%>
                        <label class="col-sm-12 text-center"  style="margin-top: 20%;font-size: 13px !important;color: #99a2a9 !important;"> <bean:message key="ecom.dashb.outbal" bundle="<%=clientcode%>"/></label>
                        <br>
                        <label class="col-sm-12 table_text_highlight text-center" id="out_bal_amt" style="font-size: 30px !important;text-decoration: none"> $ <span><%=outbal%> </span></label>
                        <%} else {%>
                        <label class="col-sm-12 text-center" style="margin-top: 26%;font-size: 13px !important;color: #99a2a9 !important;"> <bean:message key="ecom.dashb.nooutbal" bundle="<%=clientcode%>"/> </label>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-8">
            <div class="card panel-warning  mb-3">
                <div class="card-header panel-heading"><i class="fa fa-truck m-r-10" ></i>  <bean:message key="ecom.dashb.unclrdcargo" bundle="<%=clientcode%>"/>
                    <div class="pull-right">
                        <a href="javascript:void(0);" onclick="toggleChartTable(this)" class="table_text_highlight dschart m-r-10"><bean:message key="ecom.dashb.table" bundle="<%=clientcode%>"/></a>
                        <a href="#" onclick="hidecustomepanel('uncleared_cargo_details')"><i class="fa fa-chevron-circle-down"></i></a>
                    </div>
                </div>
                <div class="panel-body" id="panel-uncleared_cargo_details">
                    <div id="unclrcargochart" style="height: 250px;"></div>
                    <div id="unclrcargotable" style="display: none;">
                        <div class="table-responsive" style="max-height: 250px;height: 250px;padding: 10px;">
                            <table class="table table-hover mb-0 ps-container ps-theme-default table_text_center" id="datatablebyunclearedcargo">
                                <thead>
                                    <tr>
                                        <th><bean:message key="Tracking.blnum" bundle="<%=clientcode%>"/></th>
                                        <th><bean:message key="shipping.pol.label" bundle="<%=clientcode%>"/></th>
                                        <th><bean:message key="shipping.pod.label" bundle="<%=clientcode%>"/></th>
                                        <th><bean:message key="Tracking.eta" bundle="<%=clientcode%>"/></th>
                                        <th><bean:message key="ecom.dashb.actcontcnt" bundle="<%=clientcode%>"/></th>
                                        <th><bean:message key="ecom.dashb.delcontcnt" bundle="<%=clientcode%>"/></th>
                                        <th><bean:message key="ecom.dashb.remcontcnt" bundle="<%=clientcode%>"/></th>
                                    </tr>

                                </thead>
                                <tbody>
                                    <%
                                        if (daslis != null && daslis.size() > 0) {
                                            int size = daslis.size();                                          
                                            for (int i = 0; i < size; i++) {
                                                dasvo = (UnclearedCargoVo) daslis.get(i);
                                                blNumber = Validation.checkNull(dasvo.getBlnumber());
                                                loadport = Validation.checkNull(dasvo.getLoadport());
                                                dischargeport = Validation.checkNull(dasvo.getDischargeport());
                                                vessArrDate = Validation.checkNull(dasvo.getVesselarrdate());
                                                actCont = Validation.checkNull(dasvo.getActcontrcnt());
                                                delCont = Validation.checkNull(dasvo.getDelcontrcnt());
                                                remCont = Validation.checkNull(dasvo.getRemcontrcnt());
                                                remcunt = Integer.parseInt(remCont);
                                                loadport = loadport + " - " + portlist.get(loadport);
                                                dischargeport = dischargeport + " - " + portlist.get(dischargeport);
                                                vessArrDate = Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(vessArrDate));
                                                if (remcunt > 0) {
                                    %>
                                    <tr>
                                        <td class="table_text_highlight"><%=blNumber%></td>
                                        <td><%=loadport%></td>
                                        <td><%=dischargeport%></td>
                                        <td><%=vessArrDate%></td>
                                        <td><%=actCont%></td>
                                        <td><%=delCont%></td>
                                        <td><%=remCont%></td>
                                    </tr>
                                    <%
                                                }
                                            }
                                        }%>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%}%>
    <!-- new dashboard summary stop -->
    <div class="row">
        <div class="col-md-12">
            <div class="card panel-info  mb-3">
                <div class="card-header panel-heading"><i class="fa fa-print m-r-10"></i> <bean:message key="ecom.dashb.ongoingtrans" bundle="<%=clientcode%>"/>
                    <div class="pull-right">
                        <a href="#" onclick="hidecustomepanel('on_going_transactions')"><i class="fa fa-chevron-circle-down"></i></a>
                    </div>
                </div>
                <div class="panel-body" id="panel-on_going_transactions">

                </div>
            </div>

        </div>
    </div>
</div>



<script type="text/javascript">

    $(document).ready(function () {
        openLoader();
        $.ajax({
            type: "POST",
            url: "DashboardAjax?type=OnGoingTransaction",
            success: function (response) {
                console.log('inside panel-on_going_transactions');
                $("#panel-on_going_transactions").html(response);
                closeLoader();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
        $('#datatablebyunclearedcargo1').DataTable({
            lengthMenu: [[5, 10, 25, 50, -1], [5, 10, 25, 50, "All"]],
            "aaSorting": []

        });
    });
    function getUnclearCargoDetails() {
        openLoader();
        $.ajax({
            type: "POST",
            url: "UnclearedCargoDetails.jsp",
            success: function (response) {
                closeLoader();
                $.fancybox.open(response, {
                    showCloseButton: false,
                    closeClick: false,
                    maxWidth: 1200,
                    maxHeight: 600,
                    padding: 0,
                    fitToView: true,
                    async: false,
                    width: '90%',
                    height: '100%',
                    autoSize: true,
                    scrollOutside: true,
                    closeBtn: false,
                    hideOnOverlayClick: false,
                    overlayShow: false,
                    helpers: {
                        overlay: {closeClick: false}
                    },
                    afterClose: function () {

                    },
                    keys: {
                        close: null
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
    function ContainerDashboardInfo(bookno, blno, maskport) {
        openLoader();
        $.ajax({
            type: "POST",
            url: "DashboardAjax?type=OnGoingTransactionCargodtls&bookno=" + bookno + "&blno=" + blno + "&maskport=" + maskport,
            success: function (response) {
                closeLoader();
                $.fancybox.open(response, {
                    showCloseButton: false,
                    closeClick: false,
                    maxWidth: 1200,
                    maxHeight: 600,
                    padding: 0,
                    fitToView: true,
                    async: false,
                    width: '90%',
                    height: '100%',
                    autoSize: true,
                    scrollOutside: true,
                    closeBtn: false,
                    hideOnOverlayClick: false,
                    overlayShow: false,
                    helpers: {
                        overlay: {closeClick: false}
                    },
                    afterClose: function () {

                    },
                    keys: {
                        close: null
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
</script>


<script src="Design/assets/plugins/amcharts/js/amchart.js"></script>
<script src="Design/assets/plugins/amcharts/js/serial.js"></script>
<script src="Design/assets/plugins/amcharts/js/light.js"></script>
<script src="Design/assets/plugins/amcharts/js/pie.js"></script>

<script type="text/javascript"> /* SCRIPTS */
    function hidecustomepanel(container)
    {
        $("#panel-" + container).toggle('slow').find('input').val('');
    }
    <%if ("Y".equalsIgnoreCase(Dash_display)) {%>
    var unclearcargodata = [{
            "title": "Delivered Container Count",
            "value": <%=deliveredsum%>,
            "color": "#03a84e"
        }, {
            "title": "Remaining Container Count",
            "value": <%=remainingsum%>,
            "color": "#f4ce09"
        }];
    unclearedCargoChartDashboard(unclearcargodata);
    <%}%>
   // topDestinationDashboard(<=topDesStr%>);
   // topDestinationTEUDashboard(<=topDestTeu%>);
  //  topPortPairDashboard(<=topPortPairStr%>);

    function topDestinationDashboard(value) {
        var chartdata = value;
        var chart = AmCharts.makeChart("topdestinationdiv", {
            "type": "serial",
            "theme": "none",
            "titles": [{
                    "text": "<bean:message key="ecom.msg.chartxaxistxt1" bundle="<%=clientcode%>"/>"
                }],
            "dataProvider": chartdata,
            "valueAxes": [{
                    "axisAlpha": 0,
                    "position": "left",
                    "title": "<bean:message key="ecom.msg.chartyaxistxt" bundle="<%=clientcode%>"/>"
                }],
            "startDuration": 1,
            "graphs": [{
                    "alphaField": "alpha",
                    "title": "Count",
                    "labelText": "[[countval]]",
                    "labelPosition": "middle",
                    "valueField": "countval",
                    "balloonText": '<span style="font-size:13px;">[[title]] in [[category]]:<b>[[value]]</b> [[additional]]</span> <br/>[[description]]',
                    "dashLengthField": "dashLengthColumn",
                    "fillAlphas": 1,
                    "type": "column",
                    "urlField": "url",
                    "colorField": "color",
                    "lineColorField": "color"
                }], "categoryField": "finalportid",
            "categoryAxis": {
                "gridPosition": "start",
                "axisAlpha": 0,
                "gridAlpha": 0,
                "tickLength": 0
            }

        });
        chart.validateData();
        chart.animateAgain();
    }

    function topPortPairDashboard(value) {
        var chartdata = value;
        var chart = AmCharts.makeChart("topportpairdiv", {
            "type": "serial",
            "theme": "none",
            "titles": [{
                    "text": "<bean:message key="ecom.msg.chartxaxistxt2" bundle="<%=clientcode%>"/>"
                }],
            "dataProvider": chartdata,
            "valueAxes": [{
                    "axisAlpha": 0,
                    "position": "left",
                    "title": "<bean:message key="ecom.msg.chartyaxistxt" bundle="<%=clientcode%>"/>"
                }],
            "startDuration": 1,
            "graphs": [{
                    "alphaField": "alpha",
                    "title": "Count",
                    "labelText": "[[countval]]",
                    "labelPosition": "middle",
                    "valueField": "countval",
                    "balloonText": '<span style="font-size:13px;">[[title]] in [[category]]:<b>[[value]]</b> [[additional]]</span> <br/>[[description]]',
                    "dashLengthField": "dashLengthColumn",
                    "fillAlphas": 1,
                    "type": "column",
                    "urlField": "url",
                    "colorField": "color",
                    "lineColorField": "color",
                }], "categoryField": "finalportid",
            "categoryAxis": {
                "gridPosition": "start",
                "axisAlpha": 0,
                "gridAlpha": 0,
                "labelRotation": 45,
                "tickLength": 0
            }

        });
        chart.validateData();
        chart.animateAgain();
    }
    function topDestinationTEUDashboard(value) {
        var chartdata = value;
        var chart = AmCharts.makeChart("topdestteudiv", {
            "type": "serial",
            "theme": "none",
            "titles": [{
                    "text": "<bean:message key="ecom.msg.chartxaxistxt3" bundle="<%=clientcode%>"/>"
                }],
            "dataProvider": chartdata,
            "valueAxes": [{
                    "axisAlpha": 0,
                    "position": "left",
                    "title": "Count"
                }],
            "startDuration": 1,
            "graphs": [{
                    "alphaField": "alpha",
                    "title": "<bean:message key="ecom.msg.chartyaxistxt" bundle="<%=clientcode%>"/>",
                    "labelText": "[[countval]]",
                    "labelPosition": "middle",
                    "valueField": "countval",
                    "balloonText": '<span style="font-size:13px;">[[title]] in [[category]]:<b>[[value]]</b> [[additional]]</span> <br/>[[description]]',
                    "dashLengthField": "dashLengthColumn",
                    "fillAlphas": 1,
                    "type": "column",
                    "urlField": "url",
                    "colorField": "color",
                    "lineColorField": "color"
                }], "categoryField": "finalportid",
            "categoryAxis": {
                "gridPosition": "start",
                "axisAlpha": 0,
                "gridAlpha": 0,
                "tickLength": 0
            }

        });
        chart.validateData();
        chart.animateAgain();
    }
    function unclearedCargoChartDashboard(value) {
        var chartdata = value;
        var chart = AmCharts.makeChart("unclrcargochart", {
            "type": "pie",
            "theme": "light",
            "dataProvider": chartdata,
            "titleField": "title",
            "valueField": "value",
            "labelRadius": 5,
            "colorField": "color",
            "radius": "35%",
            "innerRadius": "40%",
            "labelText": "[[title]]  : [[value]] ([[percents]]%)"
        });
        chart.validateData();
        chart.animateAgain();
    }
    function toggleChartTable(obj) {
        if ($(obj).is('.dschart')) {
            $(obj).removeClass('dschart');
            $(obj).addClass('dstable');
            $(obj).text('Chart');
            $('#unclrcargochart').hide();
            $('#unclrcargotable').show();
        } else {
            $(obj).removeClass('dstable');
            $(obj).addClass('dschart');
            $(obj).text('Table');
            $('#unclrcargotable').hide();
            $('#unclrcargochart').show();
        }
    }
</script>


