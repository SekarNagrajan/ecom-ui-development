<%-- 
    Document   : DashBoardCharts
    Created on : Dec 29, 2022, 11:14:20 PM
    Author     : gobinath.t
--%>

<%@page import="java.util.Map,net.ecom.common.GeneralConf"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script src="Design/assets/plugins/amcharts/js/amchart.js"></script>
<script src="Design/assets/plugins/amcharts/js/serial.js"></script>
<script src="Design/assets/plugins/amcharts/js/light.js"></script>
<script src="Design/assets/plugins/amcharts/js/pie.js"></script>

<%
    if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_Product_Dashboard))) {
        String Draft = "";
        String gency = "";
        String Declined = "";
        String Approved = "";
        String Confirmed = "", Confirmedbl = "", Issued = "";
        Draft = (String) session.getAttribute("Draft");
        gency = (String) session.getAttribute("Send to Agency");
        Declined = (String) session.getAttribute("Declined");
        Approved = (String) session.getAttribute("Approved");
        Confirmed = (String) session.getAttribute("Confirmed");
        Confirmedbl = (String) session.getAttribute("Confirmedbl");
        Issued = (String) session.getAttribute("Issued");
        String Submittedsi = (String) session.getAttribute("Submittedsi");
        String Draftsi = (String) session.getAttribute("Draftsi");
        String Accepted = (String) session.getAttribute("Accepted");

%>


<div class="row min-height">
    <div class="col-md-6 column">
        <div class="card panel panel-info mb-3">
            <div class="card-header panel-heading "><span class="glyphicon glyphicon-tasks"></span> Booking
                <div class="pull-right">
                    <a href="#" onclick="hidecustomepanel('info')"><i class="fa fa-chevron-circle-down"></i></a>
                </div>
            </div>
            <div id="panel-info" class="panel-body">
                <div id="flot-lines" style="height:250px;"></div>
            </div>
        </div>
    </div>
    <div class="col-md-6 column">
        <div class="card panel panel-warning mb-3">
            <div class="card-header panel-heading "><span class="glyphicon glyphicon-print"></span> Bill of Lading
                <div class="pull-right">
                    <a href="#" onclick="hidecustomepanel('warning')"><i class="fa fa-chevron-circle-down"></i></a>
                </div>
            </div>
            <div class="panel-body" id="panel-warning">
                <div id="flot-bars" style="height:250px;"></div>
            </div>
        </div>
    </div>
    <div class="col-md-12 column">
        <div class="card panel panel-danger mb-3">
            <div class="card-header panel-heading "><span class="fa fa-ship"></span> Shipping Instruction
                <div class="pull-right">
                    <a href="#" onclick="hidecustomepanel('danger')"><i class="fa fa-chevron-circle-down"></i></a>
                </div>
            </div>
            <div class="panel-body" id="panel-danger">
                <div id="flot-pies" style="height:250px;"></div>
            </div>
        </div>
    </div>
</div>


<script src="js/amcharts/amcharts.js" type="text/javascript"></script>
<script src="js/new/flot/jquery.flot.min.js" type="text/javascript"></script>
<script src="js/new/flot/jquery.flot.pie.min.js" type="text/javascript"></script>
<script src="js/new/flot/jquery.flot.resize.min.js" type="text/javascript"></script>


<script type="text/javascript"> /* SCRIPTS */

                        function hidecustomepanel(container)
                        {
                            $("#panel-" + container).toggle('slow').find('input').val('');
                        }
                        //  $(function () {

                        var Draft =<%=Draft%>;
                        var gency =<%=gency%>;
                        var Declined =<%=Declined%>;
                        var Approved =<%=Approved%>;
                        var Confirmed =<%=Confirmed%>;
                        var Confirmedbl =<%=Confirmedbl%>;
                        var Issueds =<%=Issued%>;
                        var Submittedsi =<%=Submittedsi%>;
                        var Draftsi =<%=Draftsi%>;
                        var Acceptedsi =<%=Accepted%>;
                        var legend;


                        var chart;

                        var chartData = [{
                                year: "Draft",
                                income: Draft

                            }, {
                                year: "Send to Agency",
                                income: gency

                            }, {
                                year: "Accepted",
                                income: Approved

                            }, {
                                year: "Declined",
                                income: Declined

                            }, {
                                year: "Confirmed",
                                income: Confirmed
                            }];

                        console.log('AmCharts===> ' + AmCharts);
                        console.log('AmCharts ready ===> ');
                        AmCharts.handleLoad();
                        var i = 0;
                        AmCharts.ready(function () {
                            i = 1;
                            console.log("flot-lines AmCharts ready");
                            getBookingChart();

                        });
                        if (i == 0) {
                            getBookingChart();
                        }
                        function getBookingChart() {
                            console.log("inside getBookingChart ");
                            // SERIAL CHART
                            chart = new AmCharts.AmSerialChart();
                            chart.dataProvider = chartData;
                            chart.categoryField = "year";
                            chart.startDuration = 1;
                            // AXES
                            // category
                            var categoryAxis = chart.categoryAxis;
                            categoryAxis.gridPosition = "start";
                            categoryAxis.axisColor = "#DADADA";
                            categoryAxis.dashLength = 5;

                            // value
                            var valueAxis = new AmCharts.ValueAxis();
                            valueAxis.dashLength = 5;
                            valueAxis.axisAlpha = 0.2;
                            valueAxis.integersOnly = true;
                            valueAxis.title = "";
                            chart.addValueAxis(valueAxis);

                            // GRAPHS
                            // column graph
                            var graph1 = new AmCharts.AmGraph();
                            graph1.type = "column";
                            graph1.title = "Income";
                            graph1.valueField = "income";
                            graph1.lineAlpha = 0;
                            graph1.fillColors = "#17c0e6";
                            graph1.fillAlphas = 1;
                            chart.addGraph(graph1);

                            chart.write("flot-lines");
                        }

                        var chartDatasi = [{
                                country: "Draft",
                                litres: Draftsi
                            }, {
                                country: "Submitted",
                                litres: Submittedsi

                            }, {
                                country: "Accepted",
                                litres: Acceptedsi

                            }];
                        i = 0;
                        AmCharts.ready(function () {
                            // PIE CHART
                            i = 1;
                            getBlChart();

                        });
                        if (i == 0) {
                            getBlChart();
                        }
                        function getBlChart() {
                            console.log('inside getBlChart');
                            chart = new AmCharts.AmPieChart();
                            chart.dataProvider = chartDatasi;
                            chart.titleField = "country";
                            chart.valueField = "litres";
                            chart.outlineColor = "#FFFFFF";
                            chart.outlineAlpha = 0.8;
                            chart.outlineThickness = 2;
                            chart.labelText = "[[value]]";
                            chart.balloonText = "[[value]]";
                            chart.depth3D = 15;
                            chart.angle = 30;
                            chart.colors = ["#f3c300", "#5a88d1", "#8bc34a"]
                            var legend = new AmCharts.AmLegend();
                            legend.position = "right";
                            legend.borderAlpha = 0.3;
                            legend.horizontalGap = 10;

                            chart.addLegend(legend);
                            // WRITE
                            chart.write("flot-pies");
                        }
                        var chartDataa = [{
                                country: "Issued",
                                litres: Issueds
                            }, {
                                country: "Confirmed",
                                litres: Confirmedbl
                            }];
                        i = 0;
                        AmCharts.ready(function () {
                            // PIE CHART
                            i = 1;
                            getSiChart();

                        });
                        if (i == 0) {
                            getSiChart();
                        }
                        function getSiChart() {
                            chart = new AmCharts.AmPieChart();
                            chart.dataProvider = chartDataa;
                            chart.titleField = "country";
                            chart.valueField = "litres";
                            chart.outlineColor = "#FFFFFF";
                            chart.outlineAlpha = 0.8;
                            chart.outlineThickness = 2;
                            chart.labelText = "[[value]]";
                            chart.balloonText = "[[value]]";
                            chart.depth3D = 15;
                            chart.angle = 30;
                            chart.colors = ["#5a88d1", "#8bc34a"]
                            var legend = new AmCharts.AmLegend();
                            legend.position = "right";
                            legend.borderAlpha = 0.3;
                            legend.horizontalGap = 10;

                            chart.addLegend(legend);
                            // WRITE
                            chart.write("flot-bars");
                        }
                        /* FOR THE REALTIME CHART */
                        // })
</script>
<%}%>