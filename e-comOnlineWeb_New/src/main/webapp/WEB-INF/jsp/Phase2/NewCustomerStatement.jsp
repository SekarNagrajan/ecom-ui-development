<%-- 
    Document   : NewCustomerStatement
    Created on : May 11, 2020, 9:29:25 AM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.common.Currencyformat"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Map,java.util.List,net.ecom.vo.NewCustomerstatementvo,net.ecom.common.utils.DateConversion_New,java.util.Hashtable,net.ecom.common.Validation"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    Hashtable vessellist = (Hashtable) session.getServletContext().getAttribute("SESSION_VESSELLIST");
    Hashtable portlist = (Hashtable) session.getServletContext().getAttribute("SESSION_PORTLIST");
    Hashtable serlist = (Hashtable) session.getServletContext().getAttribute("SESSION_SERVICELIST");
    String clientcode = (String) session.getAttribute("clientcode");
    Map resultMap = (Map) request.getAttribute("NewCuststmtResultMap");
    String requesteddate = (String) request.getAttribute("requestedOnDate");
    List custstmtlis = null, custstmtgrouplis = null;
    if (resultMap != null) {
        custstmtlis = (List) resultMap.get("CustStmtList");
        custstmtgrouplis = (List) resultMap.get("CustStmtGroupList");
    }
    NewCustomerstatementvo custvo = null;
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />  <bean:message key="ecom.cs.csview" bundle="<%=clientcode%>"/>  </div>

<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body">
            <div class="container">
                <html:form method="post" action="newccrcuststmt.do" styleId="NewCCRCustomerStatementForm"  >
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-12 col-sm-1 m-t-15 text-right" style="max-width: 38% !important;flex: 0 0 9.333333%;"> <bean:message key='ecom.cs.ondate' bundle="<%=clientcode%>"/> :  </div>
                            <div class="col-xs-12 col-sm-4">
                                <html:text property="ondate" styleId="ondate" styleClass="form-control inside_input remove_autocomplete required"/>
                                <img class="pages_icon" src="Design/assets/images/page_icons/date.png">     
                            </div>


                            <div class="col-xs-12 col-sm-2">
                                <button class="btn btn-info" style="padding:12px 65px" ><bean:message key="ecom.Search" bundle="<%=clientcode%>"/> </button>
                            </div>

                            <div class="col-xs-12 col-sm-4 text-right" style="margin-top: 8px;"><a class="btn btn-md btn-success text-white" onclick="callpdf();" href="#"><i class="fa fa-file-pdf-o" aria-hidden="true"></i> <bean:message key='ecom.cs.generatepdf' bundle="<%=clientcode%>"/></a></div>

                        </div>
                    </div>
                </html:form>
            </div>
        </div>
    </div>
</div>

<div id="custstmtblock">
    <div class="table-responsive card card-body card-shadow">
        <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
        <table class="table table-striped table-bordered" id="customerstatementreport">
            <thead>
                <tr>
                    <th><bean:message key='ecom.cs.compcode' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.transdate' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.trck.activitydate' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cs.ipdate' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.duedate' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='bl.BLNumber' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.mcnnnum' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.bkgnum' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='rr.service' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='Arr.vessel' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='Arr.voyage' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='Schedule.View.Bound' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.port' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ebooking.terminal.display' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='Arr.origin' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='Arr.destination' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.division' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='Surcharge.Currency' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='Surcharge.Amount' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.pay' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.balance' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.creditlim' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.term' bundle="<%=clientcode%>"/></th>
                    <th><bean:message key='ecom.cs.agingdays' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.curr' bundle="<%=clientcode%>"/></th>
                    <th> 1-17</th>
                    <th> 18-30</th>
                    <!--                    <th>0-30</th>-->
                    <th>31-60 </th>
                    <th> 61-90</th>
                    <th> <bean:message key='ecom.cs.ovr90' bundle="<%=clientcode%>"/></th>
                </tr>
            </thead>
            <tbody>
                <%if (custstmtlis != null && custstmtlis.size() > 0) {
                        for (int i = 0; i < custstmtlis.size(); i++) {
                            custvo = (NewCustomerstatementvo) custstmtlis.get(i);

                %>
                <tr>
                    <td><%=custvo.getCmpcode()%></td>
                    <td><%=DateConversion_New.convertDefaultFormatwithTime(custvo.getTransdate())%></td>
                    <td><%=DateConversion_New.convertDefaultFormatwithTime(custvo.getActivitydate())%></td>
                    <td><%=DateConversion_New.convertDefaultFormatwithTime(custvo.getInputdate())%></td>
                    <td><%=DateConversion_New.convertDefaultFormatwithTime(custvo.getDuedate())%></td>
                    <td><%=custvo.getBlno()%></td>
                    <td><%=custvo.getFcnverno()%></td>
                    <td><%=custvo.getBookno()%></td>
                    <td><%=custvo.getServicecode() + " - " + Validation.checkNull((String) serlist.get(custvo.getServicecode()))%></td>
                    <td><%=custvo.getVesselcode() + " - " + Validation.checkNull((String) vessellist.get(custvo.getVesselcode()))%></td>
                    <td><%=custvo.getVoyage()%></td>
                    <td><%=custvo.getBound()%></td>
                    <td><%=custvo.getPort() + " - " + Validation.checkNull((String) portlist.get(custvo.getPort()))%></td>
                    <td><%=custvo.getTerminal()%></td>
                    <td><%=custvo.getPol() + " - " + Validation.checkNull((String) portlist.get(custvo.getPol()))%></td>
                    <td><%=custvo.getPod() + " - " + Validation.checkNull((String) portlist.get(custvo.getPod()))%></td>
                    <td><%=custvo.getDivision()%></td>
                    <td><%=custvo.getCurrency()%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getBlamount())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getReceivedamt())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getBlbalamount())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getCreditlimit())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getCreditperiod())%></td>
                    <td><%=custvo.getAgingdays()%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdayscurrent())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays1upto17())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays18upto30())%></td>
                    <!--                   <td><=Currencyformat.rupeeFormat(custvo.getAgingdays0upto30())%></td>-->
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays30upto60())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays60upto90())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdaysover90())%></td>
                </tr>
                <%}
                    }%>
            </tbody>
        </table>
    </div>
</div>


<div id="custstmtblock1">
    <div class="table-responsive card card-body card-shadow">
        <table class="table table-striped table-bordered" id="customerstatementreport1">
            <thead>
                <tr>
                    <th><bean:message key='ecom.cs.compcode' bundle="<%=clientcode%>"/>  </th>
                    <th><bean:message key='ecom.schdl.tot' bundle="<%=clientcode%>"/> </th>
                    <th><bean:message key='ecom.cs.curr' bundle="<%=clientcode%>"/></th>
                    <th> 1-17</th>
                    <th> 18-30</th>
                    <!--                    <th>0-30</th>-->
                    <th>31-60</th>
                    <th>61-90 </th>
                    <th><bean:message key='ecom.cs.ovr90' bundle="<%=clientcode%>"/></th>
                </tr>
            </thead>
            <tbody>
                <%if (custstmtgrouplis != null && custstmtgrouplis.size() > 0) {
                        for (int i = 0; i < custstmtgrouplis.size(); i++) {
                            custvo = (NewCustomerstatementvo) custstmtgrouplis.get(i);
                %>
                <tr>
                    <td><%=custvo.getCmpcode()%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getTotal())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdayscurrent())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays1upto17())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays18upto30())%></td>
                    <!--                    <td><=Currencyformat.rupeeFormat(custvo.getAgingdays0upto30())%></td>-->
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays30upto60())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdays60upto90())%></td>
                    <td><%=Currencyformat.rupeeFormat(custvo.getAgingdaysover90())%></td>
                </tr>
                <%}
                    }%>
            </tbody>
        </table>
    </div>
</div>
<div id='csprintdiv'></div>

<form id='csform' method="post" class="form-horizontal">

</form>
<script type="text/javascript" >
    $(document).ready(function () {
        $('#ondate').attr('placeholder', 'As On Date');

        $('#ondate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
        var ondate = new Date();
        ondate.setDate(ondate.getDate());
        $('#ondate').datepicker("setDate", ondate);
        $('#customerstatementreport').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        $('#customerstatementreport1').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    <%if (requesteddate != null && requesteddate.length() > 0) {%>
        $('#ondate').datepicker("setDate", '<%=requesteddate%>');
    <%}%>
    });

    function callpdf() {
        openLoader();
        $.ajax({
            type: "POST",
            url: "CreditControlJasper.do?activitydate=" + $('#ondate').val()
        }).done(function (response) {
            closeLoader();
            $('#csprintdiv').html(response);
            if ($('#cromsgres').val() == 'fail') {
                alert('<bean:message key="ecom.cro.errmsg" bundle="<%=clientcode%>"/>');
            } else {

                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("csform").action = "CROPrintServlet?croNo=CustomerStatement&responseType=" + responseType + "&viewtype=" + viewtype;
                document.getElementById("csform").submit();
            }
        });
    }
</script>