<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.phase2.service.vo.DOSummaryVO,java.util.Hashtable,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,java.util.Map,java.util.LinkedHashMap,net.ecom.common.utils.MaskPortUtil"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String fromDate = Validation.checkNull((String) request.getAttribute("dofromdate"));
    String toDate = Validation.checkNull((String) request.getAttribute("dotodate"));
    String errormsg = (String) request.getAttribute("DOEDOCERROR");
    errormsg = errormsg != null ? errormsg : "";
    String clientcode = (String) session.getAttribute("clientcode");
    System.out.println("[ECOM-WAR] DeliveryOrderSummary  error ms.........." + errormsg);
%>
<%if (errormsg.length() > 0) {%>
<script>
    alert('<bean:message key="ecom.msg.blopr" bundle="<%=clientcode%>"/>');
</script>
<%}%>
<!--<style type="text/css">
    input.error {
        border-color:#ee3942;
    }
    .tooltip-inner {
        background-color: #1FBBA6;
        color: #fff;
    }

    .tooltip.right .tooltip-arrow  {
        border-right-color: #1FBBA6;
        color: #fff;
    }


</style>-->

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key='ecom.do.do' bundle="<%=clientcode%>"/> </div>
<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body">
            <div class="container">
                <form id="doform" method="post" class="form-horizontal" >
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
            </div>
        </div>
    </div>
    <div class="table-responsive card card-body card-shadow">
        <table class="table table-striped table-bordered" id="deliveryorddatatable">
            <thead>
                <tr align="center">
                    <th><bean:message key='ecom.do.donumber' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.blno' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.pol' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.pod' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.vessel' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.voyage' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.dichargedate' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.date' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.dovaldate' bundle="<%=clientcode%>"/></th>

                    <!--<th>Discharge Terminal</th>-->
                    <th><bean:message key='ebooking.status.display' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.do.action' bundle="<%=clientcode%>"/></th>
                </tr>
            </thead>
            <tbody>
                <%
                    Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                    //   System.out.println("[ECOM-WAR] DeliveryOrderSummary ...................." + portlist);
                    Hashtable vessellist = (Hashtable) getServletContext().getAttribute("SESSION_VESSELLIST");
                    Hashtable terminallist = (Hashtable) getServletContext().getAttribute("SESSION_TERMINALLIST");
                    LinkedHashMap eqptype = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                    List<DOSummaryVO> summaryList = (List<DOSummaryVO>) request.getAttribute("dosummarylist");
                    String type = null;
                    DOSummaryVO summaryVo = null;
                    if (summaryList != null) {
                        int size = summaryList.size();
                        for (int i = 0; i < size; i++) {
                            summaryVo = (DOSummaryVO) summaryList.get(i);
                            if (summaryVo.getPrintstatus().equalsIgnoreCase("N")) {
                                type = "Confirmed";
                            } else if (summaryVo.getPrintstatus().equalsIgnoreCase("Y")) {
                                type = "Issued";
                            }

                            System.out.println("[ECOM-WAR] DeliveryOrderSummary ........... date..........." + summaryVo.getDelorddate());
                            
                            String loadport = "", dischargeport = "", vessel = "";
                            loadport = (String) portlist.get(Validation.checkNull(summaryVo.getLoadport()));
                            dischargeport = (String) portlist.get(Validation.checkNull(summaryVo.getDischargeport()));
                            vessel = (String) vessellist.get(Validation.checkNull(summaryVo.getVessel()));
                            
                            // Check if POL or POD is a mask port
                            boolean isPolMaskPort = MaskPortUtil.isActualPort(Validation.checkNull(summaryVo.getLoadport()));
                            boolean isPodMaskPort = MaskPortUtil.isActualPort(Validation.checkNull(summaryVo.getDischargeport()));
                            boolean isMaskPort = isPolMaskPort || isPodMaskPort;
                            
                            // Swap actual port names to mask port names
                            loadport = MaskPortUtil.getDisplayPortName(Validation.checkNull(summaryVo.getLoadport()), loadport);
                            dischargeport = MaskPortUtil.getDisplayPortName(Validation.checkNull(summaryVo.getDischargeport()), dischargeport);
                %>
                <tr>
                    <td><%=Validation.checkNull(summaryVo.getDelordno())%></td>
                    <td><%=Validation.checkNull(summaryVo.getBlnumber())%></td>
                    <td><%=Validation.checkNull(loadport)%></td>
                    <td><%=Validation.checkNull(dischargeport)%></td>
                    <td><%=isMaskPort ? "" : Validation.checkNull(vessel)%></td>
                    <td><%=isMaskPort ? "" : (Validation.checkNull(summaryVo.getVoyage()) + Validation.checkNull(summaryVo.getBound()))%></td>
                    <td><%=Validation.checkNull(summaryVo.getArrdate())%></td>
                    <td><%=Validation.checkNull(summaryVo.getDelorddate())%></td>
                    <td><%=Validation.checkNull(summaryVo.getDovaldate())%></td>

                   <!-- <td><%=Validation.checkNull((String) terminallist.get(Validation.checkNull(summaryVo.getTerminal())))%></td>-->
                    <td><%=type%></td>
                    <%
                        if (Validation.checkNull(summaryVo.getPrintstatus()).equalsIgnoreCase("N") && Validation.checkNull(summaryVo.getEcomprintstatus()).equalsIgnoreCase("1")) {
                    %>
                    <td><a href="javascript:void(0);" onClick="printDOMethod('<%=summaryVo.getDelordno()%>')" class="btn btn-lg text-success"  data-toggle="tooltip"  data-placement="right" title="Print"><i class="fa fa-print"></i></a></td>
                            <%
                            } else {
                            %>
                    <td></td>
                    <%                                                                                                }
                    %>
                </tr>

                <%
                        }
                    }

                %>
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content" style="width:1132px;height:600px;margin-left: 7%;">
            <div class="modal-header bg-info text-white"> <h5 class="modal-title"> <bean:message key="ecom.do.doprint" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" onclick="refreshdodata()" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <div  id="doprintmodal" style="padding-left:2%;padding-right:2%;height:500px;" class="modaldoprint">
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div>
</div><!-- /.modal -->
<div id="DODialog">
</div>


<div id="DOPrintDialog" class="box" ></div>
<script type="text/javascript">
    var frmdateval = "<%=fromDate%>";
    var todateval = "<%=toDate%>";
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
        var datFormat = "yy-mm-dd";

        if (frmdateval.length > 0 && todateval.length > 0) {
            document.getElementById('frdateport').value = frmdateval;
            document.getElementById('todateport').value = todateval;
        }

    });

    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
        $('#frdateport').attr('placeholder', '<bean:message key="ecom.cro.fromdate" bundle="<%=clientcode%>"/>');
        $('#todateport').attr('placeholder', '<bean:message key="ecom.cro.todate" bundle="<%=clientcode%>"/>');

        $('#deliveryorddatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        $('#frdateport,#todateport').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
        $("#doform").validate({
            submitHandler: function (form) {
                $("#searchID").button('loading');
                document.getElementById('type').value = 'dateSearch';
                document.getElementById('doform').action = "deliveryorder.do";
                document.getElementById('doform').submit();
                return false;
            }
        });
    });
</script>
<script>
    var searchReqDO = getXmlHttpRequestObject();
    function getXmlHttpRequestObject() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert("<bean:message key="ecom.msg.browsernotsupported" bundle="<%=clientcode%>"/>");
        }
    }
    function printDOMethodold(doNumber) {
        document.getElementById("doform").action = "DOController?doNo=" + encodeURIComponent(doNumber),
                document.getElementById("doform").submit();


    }
    var dono;
    function printDOMethod(doNumber) {
        openLoader();
        var url = "doprintaction.do?doNumber=" + doNumber;
        var params = "doNumber=" + doNumber;
        dono = doNumber;
        searchReqDO.open("POST", url, true);
        searchReqDO.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        searchReqDO.onreadystatechange = setDOPrintDialog;
        searchReqDO.send(params);
    }
    function setDOPrintDialog() {
        if (searchReqDO.readyState == 4 && searchReqDO.status == 200) {
            document.getElementById("DODialog").innerHTML = searchReqDO.responseText;
            if (document.getElementById("oblmsgres").value == "fail")
            {
                closeLoader();
                //alert("BL Signature is not available for " + agencycode + " agency");
                return false
            } else if (document.getElementById("oblmsgres").value == "Success") {
                closeLoader();
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("doform").action = "DOPrintServlet?dono=" + dono + "&responseType=" + responseType + "&viewtype=" + viewtype;
                document.getElementById("doform").submit();
            } else
            {
                closeLoader();
                alert(document.getElementById("oblmsgres").value);
                return false
            }
        } else if (searchReqDO.readyState == 4 && searchReqDO.status == 403) {
            window.location.href = "SessionExpired.jsp";
        }
    }
    function refreshdodata() {
        openLoader();
        document.getElementById('type').value = 'dateSearch';
        document.getElementById('doform').setAttribute('action', 'deliveryorder.do');
        document.getElementById('doform').submit();
    }
</script>
</html>