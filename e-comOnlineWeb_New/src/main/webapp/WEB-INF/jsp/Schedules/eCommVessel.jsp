<%@page import="net.ecom.common.GeneralConf"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode = (String) session.getAttribute("clientcode");
    String vesseltype = (String) request.getAttribute("vesseltype");
    String loginvesselfdate = (String) request.getAttribute("loginvesselfdate");
    String loginvesseltdate = (String) request.getAttribute("loginvesseltdate");
    String loginbyvessel = (String) request.getAttribute("loginbyvessel");
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String scheduleDateRangeDaysVal = (String) getServletContext().getAttribute(GeneralConf.Schedule_Date_Range_Days);
    int scheduleDateRangeDays = 30;
    if (scheduleDateRangeDaysVal != null && scheduleDateRangeDaysVal.trim().length() > 0) {
        try { scheduleDateRangeDays = Integer.parseInt(scheduleDateRangeDaysVal.trim()); } catch (Exception e) { scheduleDateRangeDays = 30; }
    }
%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="Schd.byvessel" bundle="<%=clientcode%>"/></div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">
            <html:form styleId="searchbyvessel"  method="post" action="eCommAction.do"  styleClass="form-horizontal">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 offset-md-3 col-sm-6">
                            <div class='full-input'>
                                <label><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                                <img class="input_icon" src="Design/assets/images/page_icons/vessel.svg" />
                                <html:text property="name" styleId="vesselcode" styleClass=" atvessel required" />
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
                                <img class="input_icon_login" src="Design/assets/images/page_icons/date.png" />
                                <html:text property="frdate" styleId="frdatevessel" styleClass="remove_autocomplete required"/>
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                            </div>
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <div class='full-input'>
                                    <label><bean:message key="Schd.todate" bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                    <img class="input_icon_login" src="Design/assets/images/page_icons/date.png" />
                                    <html:text property="todate" styleId="todatevessel" styleClass="remove_autocomplete required" />
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
<div id="vesselSearch">
</div>

<!-- Scroll popup data start -->
<div class="Header_scroll">
    <div class="sticky_hide" >
        <div class="row">
            <div class="col-md-12 col-sm-12">
                <label class="label_hide"> <bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_vsl_name">  </span> </label>
                <label class="label_hide"> <bean:message key="Schd.frdate" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_from_date" >  </span> </label>
                <label class="label_hide"> <bean:message key="Schd.todate" bundle="<%=clientcode%>"/> : <span class="label_scroll" id="hdr_to_date" > </span> </label>
                <a href="#"> <i class="fa fa-edit scroll_edit text-warning"></i> </a>
            </div>
        </div>
    </div>
</div>
<!-- Scroll popup data end -->
<script type="text/javascript" src="js/Schedules.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/vessels.js?<%=jsno%>"></script>
<script type="text/javascript">
    var VesselJson = VesselJson;
    $(document).ready(function () {
        $('#vesselcode').attr('placeholder', '<bean:message key="ecom.schdl.vescode" bundle="<%=clientcode%>"/>');
        $('#frdatevessel,#todatevessel').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>"});
        // $("label.error").hide();
        //  $("#searchbtn").click(function () {
        //       $('#searchbyvessel label.error').remove();
        //    });
        $("#searchbyvessel").validate({
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
            submitHandler: function (form) {
                $("#searchbtn").button('loading');
                var vessel = $('#vesselcode').val();
                var fdate = $('#frdatevessel').val();
                var tdate = $('#todatevessel').val();
                $('#resetfunc').val('Y');
                openLoader();
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: "eComVessel.do?type=SchdByVessel&name=" + vessel.split("-")[0] + "&frdate=" + fdate + "&todate=" + tdate + "&tmp=",
                    success: function (response) {
                        $("#vesselSearch").html(response);
                        $('#vesselSearch').show();
                        $("#searchbtn").button('reset');
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
    <%
        if ("loginbyvesselsearch".equalsIgnoreCase(vesseltype)) {
    %>
    $(document).ready(function () {

        $('#vesselcode').val('<%=loginbyvessel%>');
    <%if (loginvesselfdate != null && loginvesseltdate != null && loginvesseltdate.trim().length() > 0 && loginvesselfdate.trim().length() > 0) {%>
        $('#frdatevessel').datepicker("setDate", '<%=loginvesselfdate%>');
        $('#todatevessel').datepicker("setDate", '<%=loginvesseltdate%>');
    <%}%>
        callvesselforlogin('<%=loginbyvessel%>', $('#frdatevessel').val(), $('#todatevessel').val());

    });
    <%                    }
    %>
//<![CDATA[
    $(window).one("load", function () {
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('vesselname', 'vesselcode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: VesselJson
        });
        ports.initialize();
        $('#vesselcode').typeahead(
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
                    source: ports.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#typeheadhidden").val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#typeheadhidden").val(datum.vesselname);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#typeheadhidden").val(datum.vesselname);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#typeheadhidden").val();
            if (hiddenValue == null || hiddenValue == "") {
                $('#vesselcode').typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    });
//]]>
    function refreshpage()
    {
        var fromdaterfr = new Date();
        var todaterfr = new Date();
        $('#vesselcode,#frdatevessel,#todatevessel').val('');
        fromdaterfr.setDate(fromdaterfr.getDate());
        todaterfr.setDate(todaterfr.getDate() + <%=scheduleDateRangeDays%>);
        $('#frdatevessel').datepicker("setDate", fromdaterfr);
        $('#todatevessel').datepicker("setDate", todaterfr);
        $('#vesselSearch').val("");
        $('#vesselSearch').hide();
        $('#vesselcode').typeahead('val', '');
    }


    function VesselDetails(vesselcode) {
        openLoader();
        $.ajax({
            type: "POST",
            cache: false,
            url: "ecomAjax?type=vesseldetails&vesselcode=" + trim(vesselcode) + "&date=" + Date.parse(new Date()) + "&tmp=",
            data: $(this).serializeArray(),
            success: function (data) {
                closeLoader();
                $.fancybox.open(data, {
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
                        overlay: {closeClick: false}
                    },
                    keys: {
                        close: null
                    }

                });
            },
            error: function (data, xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
                $('#routehref').html("<i class='fa fa-search'></i>");
                alert("! Error");
            }
        });
    }


    $(document).ready(function () {
        var fromdate = new Date();
        var todate = new Date();
        fromdate.setDate(fromdate.getDate());
        todate.setDate(fromdate.getDate() + <%=scheduleDateRangeDays%>);
        $('#frdatevessel').datepicker("setDate", fromdate);
        $('#todatevessel').datepicker("setDate", todate);
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

    function callvesselforlogin(vessel, fdate, tdate) {
        $("#searchbtn").button('loading');
        openLoader();
        $.ajax({
            type: "POST",
            cache: false,
            url: "eComVessel.do?type=SchdByVessel&name=" + vessel.split("-")[0] + "&frdate=" + fdate + "&todate=" + tdate + "&tmp=",
            success: function (response) {
                $("#vesselSearch").html(response);
                $('#vesselSearch').show();
                $("#searchbtn").button('reset');
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

<script type="text/javascript">
    function getvesselpdf() {
        //  location.href="/ecomonline/VesselInformation";
        var win = window.open('VesselInformation', '_newtab');
        win.focus();
    }

    $('#vesselcode').on('typeahead:selected', function (evt, item) {
        $('.error').html("");
        $('.error').css({"border-color": "#ccc"});
    });



</script>

<script>

    $(document).ready(function () {

        $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
                $("#hdr_vsl_name").text($('#vesselcode').val());
                $("#hdr_from_date").text($('#frdatevessel').val());
                $("#hdr_to_date").text($('#todatevessel').val());
                $('.sticky_hide').fadeIn(500);
            } else {
                $('.sticky_hide').fadeOut(500);
            }
        });

    });

</script>
