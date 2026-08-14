<%@ page contentType="text/html; charset=UTF-8" language="java" import="net.ecom.common.GeneralConf" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode = (String) session.getAttribute("clientcode");
    String loginbyport = (String) request.getAttribute("loginbyport");
    String porttype = (String) request.getAttribute("porttype");
    String loginportfdate = (String) request.getAttribute("loginportfdate");
    String loginporttdate = (String) request.getAttribute("loginporttdate");

    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String scheduleDateRangeDaysVal = (String) getServletContext().getAttribute(GeneralConf.Schedule_Date_Range_Days);
    int scheduleDateRangeDays = 30;
    if (scheduleDateRangeDaysVal != null && scheduleDateRangeDaysVal.trim().length() > 0) {
        try { scheduleDateRangeDays = Integer.parseInt(scheduleDateRangeDaysVal.trim()); } catch (Exception e) { scheduleDateRangeDays = 30; }
    }

%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="Schd.byport" bundle="<%=clientcode%>"/></div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="container">
            <div class="details_search">
                <html:form styleId="searchbyport"  method="post" action="eCommAction.do"  styleClass="form-horizontal">
                   
                <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 offset-md-3 col-sm-6">
                                <div class='full-input portsnotes'>
                                    <label><bean:message key="Schd.portname" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                                    <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" />
                                    <html:text property="name" styleId="portcode" styleClass=" atport required" />
                                    <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>
                                </div>
                            </div>
                        </div>
                    </div>   
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 offset-md-3 col-sm-3">
                                <div class='full-input'>
                                    <label><bean:message key="Schd.frdate" bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                    <img class="input_icon_login" src="Design/assets/images/page_icons/date.png" align="right"/>
                                    <html:text property="frdate"  styleId="frdateport" styleClass="remove_autocomplete required"/>
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                </div>
                                </div>
                                <div class="col-xs-12 col-sm-3">
                                <div class='full-input'>
                                    <label><bean:message key="Schd.todate" bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                    <img class="input_icon_login" src="Design/assets/images/page_icons/date.png" />
                                    <html:text property="todate" styleId="todateport" styleClass="remove_autocomplete required" />
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 offset-md-4 col-sm-4">
                                <button type="submit" id="searchbtn" class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </html:form>
        </div>
    </div>	
</div>
<div id="portSearch" >
</div>


<!-- Scroll popup data start -->
<div class="Header_scroll">
    <div class="sticky_hide" >
        <div class="row">
            <div class="col-md-12 col-sm-12">
                <label class="label_hide"> <bean:message key="Schd.portname" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_prt_name">  </span> </label>
                <label class="label_hide"> <bean:message key="Schd.frdate" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_from_date" >  </span> </label>
                <label class="label_hide"> <bean:message key="Schd.todate" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_to_date" > </span> </label>
                <a href="#"> <i class="fa fa-edit scroll_edit text-warning"></i> </a>
            </div>
        </div>
    </div>
</div>
<!-- Scroll popup data end -->


<script type="text/javascript" src="js/Schedules.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>


<script type="text/javascript">
    $(document).ready(function () {
        $('#portcode').attr('placeholder', '<bean:message key="ecom.schdl.portcode" bundle="<%=clientcode%>"/>');
        $('#frdateport,#todateport').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>", orientation: "auto"});
        $("label.error").hide();
        $("#searchbtn").click(function () {
            $('#searchbyport label.error').remove();
        });
        $("#searchbyport").validate({
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                frdate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                todate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            errorPlacement: function (error, element) {
                if (element.parent('.full-input').is('.full-input')) {
                    error.insertAfter(element.parent('.full-input'));
                } else {
                    error.insertAfter(element);
                }

            },
            submitHandler: function (form) {
//                $("#searchbtn").button('loading');
                var port = $('#portcode').val();
                var fdate = $('#frdateport').val();
                var tdate = $('#todateport').val();
                $('#resetfunc').val('Y');
                openLoader();
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: "eCommAction.do?type=SchdByPort&name=" + port.split("-")[0] + "&frdate=" + fdate + "&todate=" + tdate + "&tmp=",
                    success: function (response) {
                        $("#portSearch").html(response);
                        $('#portSearch').show();
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
    //
    var portlist = portJson;
    
    
    $(document).ready(function() {
        portlist = updateMaskPortList(portlist);
        
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: portlist
        });
        ports.initialize();
        $('#portcode').typeahead(
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
                $('#portcode').typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    });
    //]]>
    <%
        if ("loginschedbyport".equalsIgnoreCase(porttype)) {
    %>
    $(document).ready(function () {
        $('#portcode').val('<%=loginbyport%>');
    <%if (loginportfdate != null && loginporttdate != null && loginporttdate.trim().length() > 0 && loginportfdate.trim().length() > 0) {%>
        $('#frdateport').datepicker("setDate", '<%=loginportfdate%>');
        $('#todateport').datepicker("setDate", '<%=loginporttdate%>');

    <%}%>
        callportforlogin('<%=loginbyport%>', $('#frdateport').val(), $('#todateport').val());
    });
    <%  }
    %>

    $(document).ready(function () {
        var fromdate = new Date();
        var todate = new Date();
        fromdate.setDate(fromdate.getDate());
        todate.setDate(fromdate.getDate() + <%=scheduleDateRangeDays%>);
        $('#frdateport').datepicker("setDate", fromdate);
        $('#todateport').datepicker("setDate", todate);
    });
    
    function callportforlogin(port, fdate, tdate) {
        $("#searchbtn").button('loading');
        openLoader();
        $.ajax({
            type: "POST",
            cache: false,
            url: "eCommAction.do?type=SchdByPort&name=" + port.split("-")[0] + "&frdate=" + fdate + "&todate=" + tdate + "&tmp=",
            success: function (response) {
                $("#portSearch").html(response);
                  $("#searchbtn").button('reset');
                $('#portSearch').show();
                closeLoader();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
</script>

<script>

    $(document).ready(function () {

        $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
                $("#hdr_prt_name").text($('#portcode').val());
                $("#hdr_from_date").text($('#frdateport').val());
                $("#hdr_to_date").text($('#todateport').val());
                $('.sticky_hide').fadeIn(500);
            } else {
                $('.sticky_hide').fadeOut(500);
            }
        });

    });

</script>