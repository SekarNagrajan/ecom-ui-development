<%@page import="java.util.LinkedHashMap"%>
<%@page import="net.ecom.common.utils.MaskPortUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.phase2.service.vo.CRONumbers,java.util.Hashtable,net.ecom.common.Validation,net.ecom.phase2.service.vo.CROSummaryVO,java.util.Map"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
    try {
        System.out.println("[ECOM-WAR] CROSummary >>>entered in cro jsp>>>");
        String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
        String fromDate = Validation.checkNull((String) request.getAttribute("crofromdate"));
        String toDate = Validation.checkNull((String) request.getAttribute("crotodate"));
        String bookNo = Validation.checkNull((String) request.getAttribute("crobookno"));
        String releaseNo = Validation.checkNull((String) request.getAttribute("croreleaseno"));
        CRONumbers obj = (CRONumbers) request.getAttribute("booknolist");
        String clientcode = (String) session.getAttribute("clientcode");
        String customerCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
        List<String> noList = null;
        if (obj != null) {
            noList = obj.getBookNo();
        }

%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key='ecom.cro.cro' bundle="<%=clientcode%>"/> </div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="details_search">
            <div class="container">
                <form id="croform" method="post" class="form-horizontal">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 offset-md-2 col-sm-4">
                                <input type="text" name="frdate" id="frdate" class="form-control inside_input required" placeholder="<bean:message key="ecom.cro.fromdate" bundle="<%=clientcode%>"/>" autocomplete="off" />
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">     
                            </div>
                            <div class="col-xs-12 col-sm-4">
                                <input type="text" name="todate" id="todate" class="form-control inside_input required" placeholder="<bean:message key="ecom.cro.todate" bundle="<%=clientcode%>"/>"  autocomplete="off" />
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">  
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 offset-md-2 col-sm-4">
                                <input type="text" name="bookno" id="bookno" class="form-control inside_input" placeholder="<bean:message key="ecom.cro.bookno" bundle="<%=clientcode%>"/>" />
                            </div>
                            <div class="col-xs-12 col-sm-4">
                                <input type="text" name="releaseno" id="releaseno" class="form-control inside_input" placeholder="<bean:message key="ecom.cro.releaseno" bundle="<%=clientcode%>"/>"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 offset-md-6 col-sm-4">
                                <button type="submit" class="btn btn-info btn_block_custom" autocomplete="off" id="searchID" > <bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div id="crotable" class="card card-shadow">
    <div class="table-responsive card-body">
        <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
        <table class="table table-striped table-bordered" id="crodatatable">
            <thead>
                <tr>
                    <th><bean:message key='ecom.cro.bookno' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.vessel' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.voyage' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='Tracking.origin' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.loadport' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.dischport' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='Tracking.delivery' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.cntrtype' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.qtybooked' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.qtyreleased' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.emptyqtyreleased' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cro.releasedets' bundle="<%=clientcode%>"/></th>

                </tr>
            </thead>
            <tbody>
                <%
                    Hashtable vessellist = (Hashtable) getServletContext().getAttribute("SESSION_VESSELLIST");
                    Hashtable terminallist = (Hashtable) getServletContext().getAttribute("SESSION_TERMINALLIST");
                   LinkedHashMap eqptype = (LinkedHashMap) getServletContext().getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                    String releaseno = "", imgid1 = "", imgid2 = "",originPortText="",deliveryPortText="",loadPortText="",dischargePortText="",terminalText="",vesselText="",voyageText="";
                    List<CROSummaryVO> summaryList = (List<CROSummaryVO>) request.getAttribute("crosummarylist");
                    CROSummaryVO summaryVo = null;
                    if (summaryList != null) {
                        int size = summaryList.size();
                        for (int i = 0; i < size; i++) {
                            summaryVo = (CROSummaryVO) summaryList.get(i);
                            releaseno = "releaseNo" + (i + 1);
                            imgid1 = "#imgid1#" + (i + 1);
                            imgid2 = "#imgid2#" + (i + 1);
                            loadPortText=MaskPortUtil.swapActualPortToMaskPortWithName(summaryVo.getLoadPortText());
                            dischargePortText=MaskPortUtil.swapActualPortToMaskPortWithName(summaryVo.getDischargePortText());
                            originPortText=MaskPortUtil.swapActualPortToMaskPortWithName(summaryVo.getOriginPortText());
                            deliveryPortText=MaskPortUtil.swapActualPortToMaskPortWithName(summaryVo.getDeliveryPortText());
                            
                            if (MaskPortUtil.isActualPort(summaryVo.getOriginPortText()) || MaskPortUtil.isActualPort(summaryVo.getDeliveryPortText())) {
                                terminalText = "";
                                vesselText = "";
                                voyageText = "";
                            } else {
                                terminalText = Validation.checkNull(summaryVo.getTerminal());
                                vesselText = Validation.checkNull(summaryVo.getVslcode());
                                voyageText = Validation.checkNull(summaryVo.getVoycode()) + Validation.checkNull(summaryVo.getBound());
                            }

                %>
                <tr>
                    <td><%=Validation.checkNull(summaryVo.getFlsbookingno())%></td>
                    <td><%=vesselText%></td>
                    <td><%=voyageText%></td>
                    <td><%=originPortText%></td>
                    <td><%=loadPortText%></td>
                    <td><%=dischargePortText%></td>
                    <td><%=deliveryPortText%></td>
                    <td><%=Validation.checkNull(summaryVo.getEquipmenttype())%></td>
                    <td><%=Validation.checkNull(summaryVo.getBkgqty())%></td>
                    <td><%=Validation.checkNull(summaryVo.getRelqty())%></td>
                    <td><%=terminalText%></td>
                    <td><a id="<%=imgid1%>" href="javascript:void(0)" onclick="showCRODetails('<%=releaseno%>', '<%=summaryVo.getFlsbookingno()%>', '<%=summaryVo.getEquipmenttype()%>', '<%=imgid1%>', '<%=imgid2%>', '<%=summaryVo.getAgencycode()%>',this)">
                            <i class="fa fa-chevron-circle-right txt_up_down"></i></a>
                        <a id="<%=imgid2%>" style="display:none" href="javascript:void(0)" onclick="hideCRODetails('<%=releaseno%>', '<%=imgid1%>', '<%=imgid2%>',this);">
                            <i class="fa fa-chevron-circle-down txt_up_down"></i></a>
                    </td>
                </tr>
                <!--<tr id="<%=releaseno%>" style="display: none">
                    <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                </tr>-->
                <%
                        }
                    }

                %>

            </tbody>
        </table>
    </div>
</div>
<div id="croprintdiv">
</div>

<script type="text/javascript">
    var frmdateval = "<%=fromDate%>";
    var todateval = "<%=toDate%>";
    var bookNoVal = "<%=bookNo%>";
    var releaseNoVal = "<%=releaseNo%>";
    var customerCode = '<%=customerCode%>';

    $('#frdate,#todate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
    $(document).ready(function () {
        document.getElementById('bookno').value = bookNoVal;
        document.getElementById('releaseno').value = releaseNoVal;
        if (bookNoVal != "" || bookNoVal.length != 0) {
            document.getElementById('releaseno').value = releaseNoVal;
        }
        if (frmdateval.length > 0 && todateval.length > 0) {
            $('#frdate').datepicker("setDate", frmdateval);
            $('#todate').datepicker("setDate", todateval);
        }
        //validate

        $("#croform").validate({
            submitHandler: function (form) {
                $("#searchID").button('loading');
                document.getElementById('croform').action = "croaction.do?type=crosearch";
                document.getElementById('croform').submit();

            }
        });
    });
    var img1val, img2val, divVal;
    function showCRODetails(divid, bookNo, eqpType, img1, img2, agencycode,obj) {
        img1val = img1;
        img2val = img2;
        divVal = divid;
        var releaseno = document.getElementById('releaseno').value;
        $.ajax({
            type: "POST",
            url: "Phase2Ajax?type=crodetails&bookNo=" + bookNo + "&eqpType=" + eqpType + "&divid=" + escape(divid) + "&releaseno=" + releaseno + "&agencycode=" + agencycode
        }).done(function (response) {
            //$("#" + divid).html(response);
            $(obj).parents('tr').after(response);
            document.getElementById(img1val).style.display = "none";
            document.getElementById(img2val).style.display = "block";
            //$("#" + divid).show();
        });
    }
    function hideCRODetails(divid, img1, img2,obj) {
        img1val = img1;
        img2val = img2;
        divVal = divid;
        document.getElementById(img2val).style.display = "none";
        document.getElementById(img1val).style.display = "block";
        //document.getElementById(divVal).style.display = "none";
        //document.getElementById(divVal).innerHTML = "";
        $(obj).parents('tr').next().remove();
    }
    function printCRO(croNo, agencyCode) {
        openLoader();
        $.ajax({
            type: "POST",
            url: "CROJasper.do?customerCode=" + encodeURIComponent(customerCode) + "&croNo=" + encodeURIComponent(croNo) + "&agencyCode=" + encodeURIComponent(agencyCode)
        }).done(function (response) {
            closeLoader();
            $('#croprintdiv').html(response);
            if ($('#cromsgres').val() == 'fail') {
                alert('<bean:message key="ecom.cro.errmsg" bundle="<%=clientcode%>"/>');
            } else {

                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("croform").action = "CROPrintServlet?croNo=" + croNo + "&responseType=" + responseType + "&viewtype=" + viewtype;
                document.getElementById("croform").submit();
            }
        });
    }
    $(document).ready(function () {
        $('#crodatatable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": [],
            
        });
    });
    

</script>
<%} catch (Exception e) {
        e.printStackTrace();
    }%>
