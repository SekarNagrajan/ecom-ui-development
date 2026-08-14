<%@page contentType="text/html" pageEncoding="UTF-8" import="net.ecom.common.utils.DateConversion_New,java.util.List,java.util.ArrayList,net.ecom.phase2.service.vo.ARNSummaryVO,java.util.Hashtable,net.ecom.common.Validation,java.util.Map,java.util.LinkedHashMap,net.ecom.common.GeneralConf,net.ecom.common.utils.MaskPortUtil"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!--<style type="text/css">

    .tooltip-inner {
        background-color: #1FBBA6;
        color: #fff;
    }

    .tooltip.right .tooltip-arrow  {
        border-right-color: #1FBBA6;
        color: #fff;
    }
</style>-->
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String fromDate = Validation.checkNull((String) request.getAttribute("arnfromdate"));
    String toDate = Validation.checkNull((String) request.getAttribute("arntodate"));
    String user = (String) session.getAttribute("sessionAdmin");
    System.out.println("[ECOM-WAR] ArrivalNotice user::::" + user);
    String clientcode = (String) session.getAttribute("clientcode");
    String check_ARN_send_to_customer = Validation.checkNull((String) (String) getServletContext().getAttribute(GeneralConf.check_ARN_Send_To_Customer));
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key='ecom.arn.arn' bundle="<%=clientcode%>"/> </div>
<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body">
            <div class="container">
                <form method="post" id="arrivalform" class="form-horizontal">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 col-sm-4">
                                <input type="text" name="frdate" id="frdateport" class="form-control inside_input required" autocomplete="off"/>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">     
                            </div>
                            <div class="col-xs-12 col-sm-4">
                                <input type="text" name="todate" id="todateport"  class="form-control inside_input required" autocomplete="off"/>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">  
                            </div>
                            <div class="col-xs-12 col-sm-4">
                                <button class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                            </div>
                        </div>
                    </div>
                </form>
                <div class="form-group alert-error" id="arnviewerror" style="display:none">
                    <br/>
                    <div class="alert alert-danger alert-dismissible" role="alert">

                        <strong><bean:message key="ecom.msg.warning" bundle="<%=clientcode%>"/></strong><bean:message key="ecom.arn.arntogenerate" bundle="<%=clientcode%>"/> </div>
                </div>
                <br/>
            </div>
        </div>
    </div>
    <div class="table-responsive card card-body card-shadow">
        <table class="table table-striped table-bordered" id="arndatatable">
            <thead>
                <tr align="center" class="scheduleTrHead">
                    <th><bean:message key='ecom.arn.blno' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="arn.fulform.arnnum" bundle="<%=clientcode%>" /></th>
                    <th class='d-none'><bean:message key="arn.fulform.hblnum" bundle="<%=clientcode%>" /></th>
                    <th><bean:message key='Tracking.origin' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='Tracking.delivery' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.arn.vessel' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.arn.voyage' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.arn.eta' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.arn.dischterminal' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.arn.action' bundle="<%=clientcode%>"/></th>
                </tr>
            </thead>
            <tbody>
                <%
                    Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                    Hashtable vessellist = (Hashtable) getServletContext().getAttribute("SESSION_VESSELLIST");
                    Hashtable terminallist = (Hashtable) getServletContext().getAttribute("SESSION_TERMINALLIST");
                    LinkedHashMap eqptype = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                    List<ARNSummaryVO> summaryList = (List<ARNSummaryVO>) request.getAttribute("arnsummarylist");
                    String printStatus = "";
                    ARNSummaryVO summaryVo = null;
                    if (summaryList != null) {
                        int size = summaryList.size();
                        for (int i = 0; i < size; i++) {
                            summaryVo = (ARNSummaryVO) summaryList.get(i);
                            String originport = "", deliveryport = "", vessel = "";
                            printStatus = Validation.checkNull(summaryVo.getEcomprintstatus());
                            originport = (String) portlist.get(summaryVo.getOriginportid());
                            deliveryport = (String) portlist.get(summaryVo.getDeliveryportid());
                            vessel = (String) vessellist.get(summaryVo.getVesselcode());
                            
                            // Check if POL or POD is a mask port
                            boolean isPolMaskPort = MaskPortUtil.isActualPort(Validation.checkNull(summaryVo.getOriginportid()));
                            boolean isPodMaskPort = MaskPortUtil.isActualPort(Validation.checkNull(summaryVo.getDeliveryportid()));
                            boolean isMaskPort = isPolMaskPort || isPodMaskPort;
                            if(isMaskPort){
                                originport = MaskPortUtil.getDisplayPortName(Validation.checkNull(summaryVo.getOriginportid()), originport);
                                deliveryport = MaskPortUtil.getDisplayPortName(Validation.checkNull(summaryVo.getDeliveryportid()), deliveryport);
                            }
                           
                %>
                <tr>
                    <td><%=Validation.checkNull(summaryVo.getBlnumber())%></td>
                    <td><%=Validation.checkNull(summaryVo.getArrivalnoticeno())%></td>
                    <td class='d-none'><%=Validation.checkNull(summaryVo.getHblnumber())%></td>
                    <td><%=Validation.checkNull(originport)%></td>
                    <td><%=Validation.checkNull(deliveryport)%></td>
                    <td><%=isMaskPort ? "" : Validation.checkNull(vessel)%></td>
                    <td><%=isMaskPort ? "" : (Validation.checkNull(summaryVo.getVoyage()) + Validation.checkNull(summaryVo.getBound()))%></td>
                    <td><%=DateConversion_New.convertDefaultTimeFormat(Validation.checkNull(summaryVo.getArrdate()))%></td>
                    <td><%=isMaskPort ? "" : Validation.checkNull((String) terminallist.get(Validation.checkNull(summaryVo.getPodterminal())))%></td>

                    <td>
                        <%if (printStatus.equalsIgnoreCase("1") || (check_ARN_send_to_customer.length() > 0 && "N".equalsIgnoreCase(check_ARN_send_to_customer))) {
                        %>
                        <a href="javascript:void(0);" onClick="viewARNMethod('<%=summaryVo.getArrivalnoticeno()%>')" class="btn btn-lg text-success" title="Print" data-toggle="tooltip"  data-placement="right" ><i class="fa fa-print"></i></a>
                            <%}%>
                    </td>
                </tr>
                <%

                        }
                    }%>
            </tbody>
        </table>
    </div>
</div>
<div id="ARNDialog">
</div>
<script type="text/javascript">
    //Our XmlHttpRequest object to get the auto suggest
    var searchRequ = getXmlHttpRequestObject();
    function getXmlHttpRequestObject() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert("<bean:message key='ecom.msg.browsernotsupported' bundle="<%=clientcode%>"/>");
        }
    }
    var frmdateval = "<%=fromDate%>";
    var todateval = "<%=toDate%>";
    if (frmdateval.length > 0 && todateval.length > 0) {
        document.getElementById('frdateport').value = frmdateval;
        document.getElementById('todateport').value = todateval;
    }
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
        $('#frdateport').attr('placeholder', '<bean:message key="ecom.arn.fromdate" bundle="<%=clientcode%>"/>');
        $('#todateport').attr('placeholder', '<bean:message key="ecom.arn.todate" bundle="<%=clientcode%>"/>');

        $('#arndatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        $('#frdateport ,#todateport').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
        $("#arrivalform").validate({
            submitHandler: function (form) {
                $('#arnviewerror').hide();
                $("#searchID").button('loading');
                document.getElementById('arrivalform').action = "arrival.do";
                document.getElementById('arrivalform').submit();
                return false;
            }
        });
    });
    var arnno;
    function viewARNMethod(arnNumber) {
        openLoader();
        arnno = arnNumber;
        var url = "ArrivalPrintAction.do?arnNumber=" + arnNumber;
        var params = "arnNumber=" + arnNumber;
        searchRequ.open("POST", url, true);
        searchRequ.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        searchRequ.onreadystatechange = setARNPrintDialog;
        searchRequ.send(params);
    }
    function setARNPrintDialog() {
        if (searchRequ.readyState == 4 && searchRequ.status == 200) {
            document.getElementById("ARNDialog").innerHTML = searchRequ.responseText;
            if (document.getElementById("oblmsgres").value == "fail")
            {
                closeLoader();
                return false
            } else if (document.getElementById("oblmsgres").value == "Success") {
                closeLoader();
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("arrivalform").action = "ArrivalPrintServlet?refNumber=" + arnno + "&responseType=" + responseType + "&viewtype=" + viewtype;
                document.getElementById("arrivalform").submit();
            } else
            {
                closeLoader();
                alert(document.getElementById("oblmsgres").value);
                return false
            }
        } else if (searchRequ.readyState == 4 && searchRequ.status == 403) {
            window.location.href = "SessionExpired.jsp";
        }
    }
    function viewARNMethod1(arnNumber) {
        $('#arnviewerror').hide();
        $.ajax({
            type: "POST",
            cache: false,
            async: false,
            url: "ArrivalPrintAction.do?type=checkARNFile&arnNo=" + encodeURIComponent(arnNumber),
            data: $(this).serializeArray(),
            success: function (data) {
                var dataval = trim(data);
                if (dataval == 'success') {
                    openLoader();
                    $('.fancyboxnew').addClass('fancybox1');
                    $('#arnviewerror').hide();
                    $.ajax({
                        type: "POST",
                        cache: false,
                        url: "arnviewaction.do?arnNo=" + encodeURIComponent(arnNumber),
                        data: $(this).serializeArray(),
                        success: function (data) {
                            closeLoader();
                            if (data != null) {
                                $.fancybox({
                                    maxWidth: 900,
                                    maxHeight: 700,
                                    fitToView: false,
                                    width: '100%',
                                    height: '100%',
                                    autoSize: false,
                                    scrollOutside: true,
                                    closeClick: false,
                                    href: "ARNView.jsp",
                                    type: 'iframe',
                                    iframe: {
                                        preload: false // fixes issue with iframe and IE
                                    }
                                });
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                } else {
                    $('#arnviewerror').show();
                    $('.fancyboxnew').removeClass('fancybox1');

                }
            }
        });
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