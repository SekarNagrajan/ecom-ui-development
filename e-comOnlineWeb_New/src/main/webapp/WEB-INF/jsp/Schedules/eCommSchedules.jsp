<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="net.ecom.common.GeneralConf"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode = (String) session.getAttribute("clientcode");
    String loginpol = (String) request.getAttribute("loginschepol");
    String loginpod = (String) request.getAttribute("loginschepod");
    String loginfdate = (String) request.getAttribute("loginschefdate");
    String logintdate = (String) request.getAttribute("loginschetdate");
    String loginSchType = (String) request.getAttribute("schetype");
    String pastdateconfig = (String) session.getServletContext().getAttribute(GeneralConf.PAST_DATE_CONFIG);
    String Enable_Schedule_Calendar = (String) session.getServletContext().getAttribute(GeneralConf.Enable_Schedule_Calendar);
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String contextRoot = request.getContextPath();
    String enableScheduleCardCarbon = (String) session.getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_CARD_CARBON);
    boolean scheduleCardCarbonEnabled = "Y".equalsIgnoreCase(enableScheduleCardCarbon);

    String scheduleDateRangeDaysVal = (String) getServletContext().getAttribute(GeneralConf.Schedule_Date_Range_Days);
    int scheduleDateRangeDays = 30;
    if (scheduleDateRangeDaysVal != null && scheduleDateRangeDaysVal.trim().length() > 0) {
        try { scheduleDateRangeDays = Integer.parseInt(scheduleDateRangeDaysVal.trim()); } catch (Exception e) { scheduleDateRangeDays = 30; }
    }

    System.out.println("[ECOM-WAR] eCommSchedules Enable_Schedule_Calendar"+Enable_Schedule_Calendar);
%>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/routingports.js?<%=jsno%>"></script>

 <link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.2/main.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.2/main.min.js"></script>
  <style>
    #calendar {
      max-width: 900px;
      margin: 40px auto;
    }
    #scheduleView {
    display: none;
    text-align: end;
}
    #calendarContainer {
    display: none;
}
  </style>
<% if (scheduleCardCarbonEnabled) { %>
<link rel="stylesheet" type="text/css" href="Design/assets/css/carbon-modal.css?<%=jsno%>"/>
<% } %>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/schdule.png" /><bean:message key="ecom.schedules" bundle="<%=clientcode%>"/></div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">
            <html:form styleId="searchbylocation"  method="post" action="eCommShedLocation.do?type=SchdByBlwLocation">
                <div class="form-group">
                    <div class="row">
                        <div class="col-md-4 offset-md-2 col-sm-4">
                            <div class='full-input portsnotes'>
                                <label><bean:message key="Tracking.origin" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                                <html:text property="polname" styleId="polcode" styleClass="required" maxlength="100" />
                                <input type="hidden" name="typeheadhidden" id="typeheadhidden" />
                            </div>
                        </div>

                        <a href="javascript:swapPort();"> <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" /> </a>

                        <div class="col col-sm-4">
                            <div class='full-input portsnotes'>
                                <label><bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                                <html:text property="podname" styleId="podcode" styleClass="required" maxlength="100"/>
                                <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-md-4 offset-md-2 col-sm-4">
                            <div class='full-input'>
                                <label><bean:message key="Schd.frdate" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <img class="input_icon_login" src="Design/assets/images/page_icons/date.png" />
                                <html:text property="deptDate" styleId="deptDate" styleClass="remove_autocomplete required"/>
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </div>
                        <div class="col-sm-4 m-l-30">
                            <div class='full-input'>
                                <label><bean:message key="Schd.todate" bundle="<%=clientcode%>" /><span class="mandatory">*</span></label>
                                <img class="input_icon_login" src="Design/assets/images/page_icons/date.png" />
                                <html:text property="arrDate"  styleId="arrDate" styleClass="remove_autocomplete required"/>
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12  col-sm-4 swap_input_width"></div>
                        <div class="col-xs-12  col-sm-4 swap_input_width m-l-30">
                            <button type="submit" id="searchbtn" class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                        </div>
                    </div>
                </div>
                    <%if("Y".equalsIgnoreCase(Enable_Schedule_Calendar)){%>
                    <div class="form-group row" id="scheduleView" >
                    <div class="col-sm-12">
                        <button type="button" class="btn btn-info book_now_btn"   onclick="SchdByBlwLocationList()" id="searchlist" > List View</button>
                        <button type="button" class="btn btn-info book_now_btn" onclick="SchdByBlwLocationcalendar();">Calendar View</button>
                    </div>
                    </div>
                   <%}%>
            </html:form>
        </div>
    </div>
</div>
<div id="locationSearch" >
</div>
 <div id="calendarContainer" class="card card-body card-shadow page_bottom_content" ></div>
 <div id="locationSearchtable"></div>
<!-- Scroll popup data start -->

<div class="Header_scroll">

    <div class="sticky_hide" >
        <div class="row">
            <div class="col-md-12 col-sm-12">

                <label class="label_hide"> <bean:message key="Tracking.origin" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_frm_prt">  </span> </label>

                <label class="label_hide"> <bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_to_prt" >  </span> </label>

                <label class="label_hide"> <bean:message key="Schd.frdate" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_from_date" > </span> </label>

                <label class="label_hide"> <bean:message key="Schd.todate" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_to_date" > </span> </label>

                <a href="#"> <i class="fa fa-edit scroll_edit text-warning"></i> </a>

            </div>

        </div>

    </div>

</div>

<!-- Scroll popup data end -->

<!-- View rates dialouge start -->

<div class="modal fade" id="view_rates" data-backdrop="false">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title"><bean:message key="ecom.scdl.selconttype" bundle="<%=clientcode%>"/></h4>
                <button type="button" class="close close dialouge_close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">

                <div class="row form-group txt_bor">
                    <div class="col-sm-6">

                        <div class="con_lable"> <span class="txt_ft cont_clr_20ft"> 20 FT </span>
                            <img class="contain_20ft" src="Design/assets/images/page_icons/co_20ft.png" />
                        </div>
                        <input type="hidden" id="eqptype_20ft" name="eqptype_20ft" value="DV20"/>
                    </div>

                    <div class="col-sm-3 inc_dec_dlg">

                        <div class="value-button_dlg" id="decrease" onclick="decreaseValue(this)" value="Decrease Value">-</div>
                        <input type="number" id="20ft_quantity" class="number_val_cls bg-light"  value="0" />
                        <div class="value-button_dlg" id="increase" onclick="increaseValue(this)" value="Increase Value">+</div>

                    </div>

                    <div class="col-sm-2 m-l-20">
                        <div class="switchToggle">
                            <span class="toggle_label">Reefer</span>
                            <input type="checkbox" id="20ft_reefer" />
                            <label class="tgl_round" for="20ft_reefer"> </label>
                        </div>
                        <!--                        <label for="switcherySize5" class=""> Reefer</label> <br/>
                                                <input type="checkbox" id="20ft_reefer" class="switchery" data-size="xs"  />-->

                    </div>

                </div>

                <div class="row form-group txt_bor">
                    <div class="col-sm-6  ">

                        <div class="con_lable"> <span class="txt_ft cont_clr_40ft"> 40 FT  </span>
                            <img class="contain_icon" src="Design/assets/images/page_icons/co_40fthc.png" />
                        </div>
                        <input type="hidden" id="eqptype_40ft" name="eqptype_40ft" value="DV40"/>

                    </div>

                    <div class="col-sm-3 inc_dec_dlg">

                        <div class="value-button_dlg" id="decrease" onclick="decreaseValue(this)" value="Decrease Value">-</div>
                        <input type="number" id="40ft_quantity" class="number_val_cls bg-light"  value="0" />
                        <div class="value-button_dlg" id="increase" onclick="increaseValue(this)" value="Increase Value">+</div>

                    </div>

                    <div class="col-sm-2 m-l-20">
                        <div class="switchToggle">
                            <span class="toggle_label">Reefer</span>
                            <input type="checkbox" id="40ft_reefer" />
                            <label class="tgl_round" for="40ft_reefer"> </label>
                        </div>
                        <!--                        <label for="switcherySize4" class=""> Reefer</label> <br/>
                                                <input type="checkbox" id="40ft_reefer" class="switchery" data-size="xs"  />-->

                    </div>

                </div>

                <div class="row txt_bor form-group">
                    <div class="col-sm-6  ">

                        <div class="con_lable" style="margin-left: 10px;"> <span class="txt_ft cont_clr_fthc"> 40 FT HC </span>
                            <img class="contain_icon_fthc" src="Design/assets/images/page_icons/co_40ft.png" />
                        </div>
                        <input type="hidden" id="eqptype_40fthc" name="eqptype_40fthc" value="RE40"/>

                    </div>

                    <div class="col-sm-3 inc_dec_dlg">

                        <div class="value-button_dlg" id="decrease" onclick="decreaseValue(this)" value="Decrease Value">-</div>
                        <input type="number" id="40ft_hc_quantity"  class="number_val_cls bg-light" value="0" />
                        <div class="value-button_dlg" id="increase" onclick="increaseValue(this)" value="Increase Value">+</div>

                    </div>

                    <div class="col-sm-2 m-l-20">
                        <div class="switchToggle">
                            <span class="toggle_label">Reefer</span>
                            <input type="checkbox" id="40ft_hc_reefer" />
                            <label class="tgl_round" for="40ft_hc_reefer"> </label>
                        </div>
                        <!--                        <label for="switcherySize3" class=""> Reefer</label> <br/>
                                                <input type="checkbox" id="40ft_hc_reefer" class="switchery" data-size="xs" />-->

                    </div>

                </div>

                <div class="row  m-l-20 form-group">
                    <label class="col-sm-2 control-label"> <bean:message key="COMMODITY_LABEL" bundle="<%=clientcode%>"/>  </label>

                    <div class="col-sm-9 ">
                        <input type="text" class="form-control light_border" id="ratecommodity" name="ratecommodity"/>
                        <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
                    </div>
                </div>

                <div class=" row form-group">

                    <div class="col-sm-2"> </div>

                    <div class="col-sm-5 m-l-30">
                        <button type="submit"  onclick="applyRates();" class="btn btn-info btn_block_custom "> Apply </button>

                    </div>

                </div>

            </div>

            <!-- Modal footer -->
            <!--  <div class="modal-footer">
               <button type="button" class="btn btn-primary" data-dismiss="modal"> Apply</button>
             </div> -->
        </div>
    </div>
</div>

<!-- View rates dialouge end -->

<% if (false) { %><!-- legacy carbon modal removed: use SchedulebetweenlocationView.jsp -->
<div class="modal fade" id="schd_carbon_modal_legacy" data-backdrop="false" tabindex="-1" role="dialog" aria-hidden="true" style="display:none !important;">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="schdCarbonModalTitle"><bean:message key="ecom.carbon.schedule.title" bundle="<%=clientcode%>"/></h4>
                <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close">&times;</button>
            </div>
            <div class="modal-body">
                <div class="schd-carbon-modal-route" id="schdCarbonModalRoutePreview"></div>
                <div class="schd-carbon-form-panel">
                    <div id="schdCarbonModalErrorForm" class="alert alert-danger schd-carbon-modal-alert" style="display:none;" role="alert"></div>
                    <div class="schd-carbon-form-row">
                        <div class="schd-carbon-form-field">
                            <label class="schd-carbon-form-label" for="schdCarbonModalQty"><bean:message key="ecom.carbon.quantity" bundle="<%=clientcode%>"/></label>
                            <input type="number" min="1" step="1" class="form-control" id="schdCarbonModalQty" value="1"/>
                        </div>
                        <div class="schd-carbon-form-field">
                            <label class="schd-carbon-form-label" for="schdCarbonModalWeight"><bean:message key="ecom.carbon.weight" bundle="<%=clientcode%>"/></label>
                            <input type="number" min="0" step="1" class="form-control" id="schdCarbonModalWeight" value=""/>
                        </div>
                    </div>
                    <button type="button" class="btn btn-info" id="schdCarbonModalCalculate">
                        <bean:message key="ecom.carbon.calculate" bundle="<%=clientcode%>"/>
                    </button>
                </div>
                <div id="schdCarbonModalLoading" style="display:none;">
                    <i class="fa fa-spinner fa-spin"></i>
                    <bean:message key="ecom.carbon.calculating" bundle="<%=clientcode%>"/>
                </div>
                <div id="schdCarbonModalError" class="alert alert-danger schd-carbon-modal-alert" style="display:none;" role="alert"></div>
                <div id="schdCarbonResultsPanel" class="carbon-results-panel" style="display:none;">
                    <div class="carbon-results-inner">
                        <div class="carbon-route-label" id="schdCarbonRouteLabel"></div>
                        <div class="carbon-metrics-row">
                            <div class="carbon-metric">
                                <div class="carbon-metric-label"><bean:message key="ecom.carbon.total" bundle="<%=clientcode%>"/></div>
                                <div class="carbon-metric-value" id="schdCarbonCo2Total">&mdash;</div>
                                <div class="carbon-metric-sub" id="schdCarbonCo2TotalSub"></div>
                            </div>
                            <div class="carbon-metric">
                                <div class="carbon-metric-label"><bean:message key="ecom.carbon.ttw" bundle="<%=clientcode%>"/></div>
                                <div class="carbon-metric-value" id="schdCarbonCo2Ttw">&mdash;</div>
                                <div class="carbon-metric-sub" id="schdCarbonCo2TtwSub"></div>
                            </div>
                            <div class="carbon-metric">
                                <div class="carbon-metric-label"><bean:message key="ecom.carbon.wtt" bundle="<%=clientcode%>"/></div>
                                <div class="carbon-metric-value" id="schdCarbonCo2Wtt">&mdash;</div>
                                <div class="carbon-metric-sub" id="schdCarbonCo2WttSub"></div>
                            </div>
                            <div class="carbon-metric">
                                <div class="carbon-metric-label"><bean:message key="ecom.carbon.distance" bundle="<%=clientcode%>"/></div>
                                <div class="carbon-metric-value" id="schdCarbonDistance">&mdash;</div>
                                <div class="carbon-metric-sub">km</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="schdCarbonInfoSections" class="schd-carbon-info-sections" style="display:none;" aria-label="Eco calculator information">
                    <div class="schd-carbon-info-card schd-carbon-info-card--purpose">
                        <button type="button" class="schd-carbon-info-toggle" aria-expanded="false">
                            <i class="fa fa-info-circle" aria-hidden="true"></i>
                            <span>Purpose of our Eco Calculator</span>
                            <i class="fa fa-chevron-down schd-carbon-info-chevron" aria-hidden="true"></i>
                        </button>
                        <div class="schd-carbon-info-body">
                            <p>As part of our sustainability strategy and commitment to reducing greenhouse gas emissions, we developed this calculator to support customers and partners in managing their cargo-related carbon footprint.</p>
                            <p>This tool provides an easy way to estimate <strong>CO<sub>2</sub> emissions for shipments</strong>, helping stakeholders make informed decisions toward greener transport solutions.</p>
                            <p>The calculation is based on globally recognized methodologies such as the <strong>GLEC Framework</strong> and <strong>Clean Cargo guidelines</strong>, ensuring transparency and accuracy in reporting.</p>
                        </div>
                    </div>
                    <div class="schd-carbon-info-card schd-carbon-info-card--methodology">
                        <button type="button" class="schd-carbon-info-toggle" aria-expanded="false">
                            <i class="fa fa-file-text-o" aria-hidden="true"></i>
                            <span>Methodology</span>
                            <i class="fa fa-chevron-down schd-carbon-info-chevron" aria-hidden="true"></i>
                        </button>
                        <div class="schd-carbon-info-body">
                            <p>Our calculator estimates emissions based on actual route distances, vessel type, fuel consumption, cargo weight, and industry-standard emission factors.</p>
                            <p>The methodology follows standards such as <strong>IMO</strong> and the <strong>GLEC Framework</strong> to calculate CO<sub>2</sub> equivalent emissions for ocean transport shipments.</p>
                            <p>The calculation focuses mainly on ocean-leg emissions and may include tank-to-wheel and well-to-wheel emission estimates. Results are estimated values and may vary depending on voyage conditions, weather, port congestion, and operational factors.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<% } %>

<!-- Rates dialouge start -->

<div class="modal fade" id="view_rates_details" data-backdrop="false">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title"> <bean:message key="ecom.rates" bundle="<%=clientcode%>"/> </h4>
                <button type="button" class="close close dialouge_close" data-dismiss="modal">&times;</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                <div class="form-group">
                    <div class=" ">
                        <div class="table-responsive">
                            <table class="table table-borderless table-fixed" id="rates_total">
                                <thead>
                                    <tr>
                                        <th> <bean:message key="ecom.schdl.charge" bundle="<%=clientcode%>"/> </th>
                                        <th> <bean:message key="Surcharge.Amount" bundle="<%=clientcode%>"/> <span class="table_sm_label"><bean:message key="ecom.schdl.amtpercont" bundle="<%=clientcode%>"/></span> </th>
                                        <th>  <bean:message key="ebooking.quantity.display" bundle="<%=clientcode%>"/> </th>
                                        <th><bean:message key="Surcharge.Amount" bundle="<%=clientcode%>"/> </th>
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>
                        </div>
                        <div class="col-sm-5 m-r-50-r m-t-15 float-right">
                            <label class="total-label"><bean:message key="ecom.schdl.tot" bundle="<%=clientcode%>"/></label>
                            <span id ="sum_amount" class="total_amount"> $ <span></span>  </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<!-- Rates dialouge end -->
<script type="text/javascript" src="js/Schedules.js?<%=jsno%>"></script>
<script type="text/javascript">
      function SchdByBlwLocationcalendar() {
    var type = "dept";
    console.log('inside SchdByBlwLocationcalendar');
    var deptdate = $("#deptDate").val();
    var arrdate = $("#arrDate").val();
    console.log('SchdByBlwLocationcalendar');
   $.ajax({
        type: "POST",
        url: "eCommShedCalendar.jsp?type=calendar& bwlocdeptdate="+deptdate+"&bwlocarrdate="+arrdate, // Ensure this file exists at the correct path
        success: function(response) {
               $('#locationSearch').hide();
               $('#export_button').hide();
               $('#locationSearchtable').hide();
               $('#calendarContainer').html(response);
               $('#calendarContainer').show();
           console.log('after the calendar');
        },
        error: function(xhr) {
             console.log('Error SchdByBlwLocationcalendar ');
            if (xhr.status == 404) {
                console.log('Error 404: Resource not found');
            } 
           
        }
        
    });
}

 function SchdByBlwLocationList() {
     $('#locationSearch').show();
     $('#export_button').show();
     $('#calendarContainer').hide();
     $('#locationSearchtable').hide(); 
 }
  </script>
<script type="text/javascript">
                            $(document).ready(function () {
                                $('#polcode,#podcode').attr('placeholder', '<bean:message key="ecom.schdl.portcode" bundle="<%=clientcode%>"/>');
                                        $('#deptDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>
    }
                                );
                                        $('#arrDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>" <%=((!"Y".equalsIgnoreCase(pastdateconfig)) ? " , startDate: '-infinity'" : "")%>
    }
                                );

//       
//        $("label.error").hide();

//        $("#searchbtn").click(function () {
//            $('#searchbylocation label.error').remove();
//        });
                                $("#searchbylocation").validate({
                                    rules: {
                                        polname: {
                                            required: true
                                        },
                                        podname: {
                                            required: true,
                                            notEqualTo: ['#polcode']
                                        },
                                        deptDate: {
                                            required: true
                                        },
                                        arrDate: {
                                            required: true
                                        }

                                    },
                                    messages: {
                                        polname: {
                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                        },
                                        podname: {
                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                                            notEqualTo: "<bean:message key="ecom.msg.diffport" bundle="<%=clientcode%>"/>"
                                        },
                                        deptDate: {
                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                        },
                                        arrDate: {
                                            required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                        }
                                    },
                                    submitHandler: function (form) {
                                        //var data;
                                        var type = "dept";
                                        console.log('inside submitHandler:');
                                        //  $("#searchbtn").button('loading');
                                        var pol = $("#polcode").val();
                                        var pod = $("#podcode").val();
                                        var deptdate = $("#deptDate").val();
                                        var arrdate = $("#arrDate").val();
//                $('#resetfunc').val('Y');
//                if ($("#rdn_arr").is(':checked')) {
//                    type = "arr";
//                } else {
//                    type = "dept";
//                }
                                        openLoader();
                                        $.ajax({
                                            type: "POST",
                                            url: "eCommShedLocation.do?type=SchdByBlwLocation&polname=" + pol.split("-")[0] + "&podname=" + pod.split("-")[0] + "&deptDate=" + deptdate + "&arrDate=" + arrdate + "&date=" + type,
                                            success: function (response) {
                                                console.log('inside response');
                                                $("#locationSearch").html(response);
                                                $("#locationSearchtable").html(response);
                                               if ($(response).find('#schedulebetweenlocationview').length) {
                                                $('#scheduleView').css('display', 'block');
                                                } else {
                                                 $('#scheduleView').css('display', 'none');
                                                   } 
                                                $('#locationSearch').show();
                                                $('#export_button').show();
                                                $("#calendarContainer").hide();
                                                $("#locationSearchtable").hide();
                                                //  $("#searchbtn").button('reset');
    <% if (scheduleCardCarbonEnabled) { %>
                                                initScheduleCarbonIfReady();
    <% } %>
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
                            var portlist = portJson;
                           
                            //<![CDATA[
                            $(document).ready(function() {
                                portlist = updateMaskPortList(portlist);
                                var RoutingOriginlist = updateMaskPortList(RoutingOrigin);
                                var ports = new Bloodhound({
                                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                                    local: RoutingOriginlist
                                });
                                ports.initialize();
                                $('#polcode').typeahead(
                                        {
                                            hint: true,
                                            highlight: true,
                                            minLength: 1
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
                                        $('#polcode').typeahead('val', "");
                                        $(obj.currentTarget).val("");
                                    }
                                });
                                var RoutingDeliverylist = updateMaskPortList(RoutingDelivery);
                                ports = new Bloodhound({
                                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                                    local: RoutingDeliverylist
                                });
                                $('#podcode').typeahead(
                                        {
                                            hint: true,
                                            highlight: true,
                                            minLength: 1
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
                                        $('#podcode').typeahead('val', "");
                                        $(obj.currentTarget).val("");
                                    }
                                });
                            });
                            //]]>


                            $(document).ready(function () {
                                var fromdate = new Date();
                                var todate = new Date();
                                fromdate.setDate(fromdate.getDate());
                                todate.setDate(fromdate.getDate() + <%=scheduleDateRangeDays%>);
                                $('#deptDate').datepicker("setDate", fromdate);
                                $('#arrDate').datepicker("setDate", todate);
                            });

//    $('#polcode').on('typeahead:selected', function (evt, item) {
//        $('.error').html("");
//        $('.error').css({"border-color": "#ccc"});
//    });
//
//    $('#podcode').on('typeahead:selected', function (evt, item) {
//        $('.error').html("");
//        $('.error').css({"border-color": "#ccc"});
//    });
                            function callscheuleforlogin(pol, pod, fdat, tdate) {
                                $("#searchbtn").button('loading');
                                openLoader();
                                $.ajax({
                                    type: "POST",
                                    url: "eCommShedLocation.do?type=SchdByBlwLocation&polname=" + pol.split("-")[0] + "&podname=" + pod.split("-")[0] + "&deptDate=" + fdat + "&arrDate=" + tdate + "&date=dept&viahomepage=Yes",
                                    success: function (response) {
                                        $("#locationSearch").html(response);
                                         $("#locationSearchtable").html(response);
                                        if ($(response).find('#schedulebetweenlocationview').length) {
                                         $('#scheduleView').css('display', 'block');
                                          } else {
                                             $('#scheduleView').css('display', 'none');
                                             } 
                                        $('#locationSearch').show();
                                        $('#export_button').show();
                                        $("#calendarContainer").hide();
                                        $("#locationSearchtable").hide();
                                        $("#searchbtn").button('reset');
    <% if (scheduleCardCarbonEnabled) { %>
                                        initScheduleCarbonIfReady();
    <% } %>
                                        closeLoader();
                                    },
                                    error: function (xhr) {
                                        if (xhr.status == 403) {
                                            window.location.href = "SessionExpired.jsp";
                                        }
                                    }
                                });
                            }
    <%
        if ("loginschedule".equalsIgnoreCase(loginSchType)) {
    %>
                            $(document).ready(function () {
                                $('#polcode').val('<%=loginpol%>');
                                $('#podcode').val('<%=loginpod%>');
    <%if (loginfdate != null && logintdate != null && logintdate.trim().length() > 0 && loginfdate.trim().length() > 0) {%>
                                $('#deptDate').datepicker("setDate", '<%=loginfdate%>');
                                $('#arrDate').datepicker("setDate", '<%=logintdate%>');
    <%}%>
                                callscheuleforlogin('<%=loginpol%>', '<%=loginpod%>', $('#deptDate').val(), $('#arrDate').val());

                            });
    <%                    }
    %>
                            function swapPort() {
                                var temp = $('#polcode').val();
                                $('#polcode').typeahead("val", $('#podcode').val());
                                $('#podcode').typeahead("val", temp);
                            }
</script>

<script type="text/javascript">

    $(document).ready(function () {

        $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
                $("#hdr_frm_prt").text($('#polcode').val());
                $("#hdr_to_prt").text($('#podcode').val());
                $("#hdr_from_date").text($('#deptDate').val());
                $("#hdr_to_date").text($('#arrDate').val());
                $('.sticky_hide').fadeIn(500);
            } else {
                $('.sticky_hide').fadeOut(500);
            }
        });

    });

</script>

<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsno%>"></script>
<script type="text/javascript">

    $(window).one("load", function () {
        var commodity = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: commodityJson
        });
        commodity.initialize();
        $('#ratecommodity').typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2
                },
                {
                    name: 'ratecommodity',
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
                $('#ratecommodity').typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    });


    var selectedblock;

    function showViewRates(referenceclass) {
        resetRatesdialog();
        selectedblock = referenceclass;
        console.log('selectedblock==> ' + selectedblock);
        $("#view_rates").modal('show');
    }

    function editViewRates(referenceclass) {
        resetRatesdialog();
        selectedblock = referenceclass;
        console.log('selectedblock==> ' + selectedblock);
        var ratearr = JSON.parse($('.' + selectedblock).find('.SchdCargoDetailsJson').val());
        $.each(ratearr, function (idx, arr) {
            if (arr.type == '20FT') {
                $('#20ft_quantity').val(arr.quantity);
                if (arr.reefer == 'Y') {
                    $('#20ft_reefer').prop('checked', true);
                }
                $('#ratecommodity').typeahead('val', arr.commodity);
            }

            if (arr.type == '40FT') {
                $('#40ft_quantity').val(arr.quantity);
                if (arr.reefer == 'Y') {
                    $('#40ft_reefer').prop('checked', true);
                }
                $('#ratecommodity').typeahead('val', arr.commodity);
            }

            if (arr.type == '40FT_HC') {
                $('#40ft_hc_quantity').val(arr.quantity);
                if (arr.reefer == 'Y') {
                    $('#40ft_hc_reefer').prop('checked', true);
                }
                $('#ratecommodity').typeahead('val', arr.commodity);
            }

        });
        $("#view_rates").modal('show');
    }

    function applyRates() {

        $.ajax({
            url: "ScheduleRateAjax?pol=" + $('#polcode').val() + "&pod=" + $('#podcode').val() + "&shipmentdate=" + $('#deptDate').val() + "&eqptype=" + "DV20" + "&quantity=" + $('#20ft_quantity').val() + "&commodity=",
            async: false,
            success: function (data) {
                console.log(data);
                var resultdata = JSON.parse(data);
                var ratelis = resultdata.ratelist;
                var totalamount = resultdata.totalamount;
                var totalamount_currencyformat = resultdata.totalamount_currencyformat;
                $('.' + selectedblock).find('.SchdRateTotalAmount').val(totalamount_currencyformat);
                $('.' + selectedblock).find('.SchdRateTotalAmountInFloat').val(totalamount);
                $('.' + selectedblock).find('.total_conta_val > span').text(totalamount_currencyformat);
                $('.' + selectedblock).find('.SchdRateDetailsJson').val(JSON.stringify(ratelis));
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });


        var ratespanhtml = '';
        var ratearr = [];
        var rateobj = {};
        console.log('20ft_quantity---> ' + $('#20ft_quantity').val());
        if ($('#20ft_quantity').val() > 0) {
            rateobj['type'] = '20FT';
            rateobj['eqptype'] = $('#eqptype_20ft').val();
            rateobj['quantity'] = $('#20ft_quantity').val();
            if ($('#20ft_reefer').prop('checked') == true) {
                rateobj['reefer'] = 'Y';
            } else {
                rateobj['reefer'] = 'N';
            }
            rateobj['commodity'] = $('#ratecommodity').val();
            ratearr.push(rateobj);
            ratespanhtml = ratespanhtml + ' ' + $('#20ft_quantity').val() + ' X <span class="txt_ft_clr"> 20 </span> FT &nbsp;&nbsp;&nbsp;';
        }
        rateobj = {};
        if ($('#40ft_quantity').val() > 0) {
            rateobj['type'] = '40FT';
            rateobj['eqptype'] = $('#eqptype_40ft').val();
            rateobj['quantity'] = $('#40ft_quantity').val();
            if ($('#40ft_reefer').prop('checked') == true) {
                rateobj['reefer'] = 'Y';
            } else {
                rateobj['reefer'] = 'N';
            }

            rateobj['commodity'] = $('#ratecommodity').val();
            ratearr.push(rateobj);
            ratespanhtml = ratespanhtml + ' ' + $('#40ft_quantity').val() + ' X <span class="txt_ft_clr"> 40 </span> FT &nbsp;&nbsp;&nbsp;';
        }
        rateobj = {};
        if ($('#40ft_hc_quantity').val() > 0) {
            rateobj['type'] = '40FT_HC';
            rateobj['eqptype'] = $('#eqptype_40fthc').val();
            rateobj['quantity'] = $('#40ft_hc_quantity').val();
            if ($('#40ft_hc_reefer').prop('checked') == true) {
                rateobj['reefer'] = 'Y';
            } else {
                rateobj['reefer'] = 'N';
            }

            rateobj['commodity'] = $('#ratecommodity').val();
            ratearr.push(rateobj);
            ratespanhtml = ratespanhtml + ' ' + $('#40ft_hc_quantity').val() + ' X <span class="txt_ft_clr"> 20 </span> FT  HC &nbsp;&nbsp;&nbsp;';
        }

        ratespanhtml = ratespanhtml + "<a href='#' onclick=" + '"' + "editViewRates('" + selectedblock + "');" + '"' + " class='float-right txt_edit_con' > <i class='fa fa-pencil'></i>  </a>";
        console.log(ratespanhtml);
        console.log('--------------------------------------------------------------------------------------------');
        console.log(JSON.stringify(ratearr));
        console.log('selectedblock==> ' + selectedblock);
        $('.' + selectedblock).find('.label_edit_data').html(ratespanhtml);
        $('.' + selectedblock).find('.SchdCargoDetailsJson').val(JSON.stringify(ratearr));
        $('.' + selectedblock).find('.schd_rate_details').show();
        $('.' + selectedblock).find('.spl_rate').show();
        $('.' + selectedblock).find('.schd_slt_cont_details').hide();
        $('.' + selectedblock).find('.book_now_btn').css('margin-top', '0px');
        $("#view_rates").modal('hide');
    }
    function resetRatesdialog() {
        $('#ratecommodity,#typeheadhidden2').val('');
        $('#ratecommodity').typeahead('val', '');
        $('#20ft_quantity,#40ft_quantity,#40ft_hc_quantity').val('0');
        $('#20ft_reefer,#40ft_reefer,#40ft_hc_reefer').prop('checked', false);
    }
    function showRateDetails(referenceclass) {
        $('#sum_amount > span').text($('.' + referenceclass).find('.SchdRateTotalAmount').val());
        var ratedata = JSON.parse($('.' + referenceclass).find('.SchdRateDetailsJson').val());
        var tablecontent = '';
        $.each(ratedata, function (idx, arr) {
            tablecontent = tablecontent + '<tr>';
            tablecontent = tablecontent + '<td>' + arr.chargename + '</td>';
            tablecontent = tablecontent + '<td>' + arr.amount + '</td>';
            tablecontent = tablecontent + '<td>' + arr.quantity + '</td>';
            tablecontent = tablecontent + '<td>' + arr.amount + '</td>';
            tablecontent = tablecontent + '</tr>';
        });
        console.log("tablecontent=> " + tablecontent);
        $('#rates_total > tbody').html(tablecontent);
        $("#view_rates_details").modal('show');
    }
    function navigateToQuote(referenceclass) {
        var pol = $('#polcode').val();
        var pod = $('#podcode').val();
        var actualfreight = $('.' + referenceclass).find('.SchdRateTotalAmountInFloat').val();
        var eqptype, quantity, commodity;
        var ratearr = JSON.parse($('.' + referenceclass).find('.SchdCargoDetailsJson').val());
        $.each(ratearr, function (idx, arr) {
            eqptype = arr.eqptype;
            quantity = arr.quantity;
            commodity = arr.commodity;
            return false;
        });
        window.location = "Accesslogin.do?type=raterequest&pol=" + pol + "&pod=" + pod + "&eqptype=" + eqptype + "&quantity=" + quantity + "&commodity=" + commodity + "&actualfreight=" + actualfreight;
    }

</script>
<% if (scheduleCardCarbonEnabled) { %>
<script type="text/javascript" src="js/schedule-carbon.js?<%=jsno%>"></script>
<script type="text/javascript">
    window.scheduleCarbonConfig = {
        endpoint: '<%=contextRoot%>/CarbonScheduleAjax',
        messages: {
            quantityRequired: '<bean:message key="ecom.carbon.error.quantity" bundle="<%=clientcode%>"/>',
            weightRequired: '<bean:message key="ecom.carbon.error.weight" bundle="<%=clientcode%>"/>',
            requestFailed: '<bean:message key="ecom.carbon.error.request" bundle="<%=clientcode%>"/>',
            calculationFailed: '<bean:message key="ecom.carbon.error.calculation" bundle="<%=clientcode%>"/>',
            endpointMissing: '<bean:message key="ecom.carbon.error.endpointMissing" bundle="<%=clientcode%>"/>',
            apiNotConfigured: '<bean:message key="ecom.carbon.error.apiNotConfigured" bundle="<%=clientcode%>"/>',
            scheduleIndexInvalid: '<bean:message key="ecom.carbon.error.scheduleIndexInvalid" bundle="<%=clientcode%>"/>'
        }
    };
    function initScheduleCarbonIfReady() {
        if (typeof window.initScheduleCarbon === 'function') {
            window.initScheduleCarbon();
        }
    }
    $(document).ready(function () {
        initScheduleCarbonIfReady();
    });
</script>
<% } %>
