<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="net.ecom.common.utils.MaskPortUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.Hashtable,java.util.List,net.ecom.vo.SIsummaryVO,net.ecom.common.Validation"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%String user = (String) session.getAttribute("sessionAdmin");
    String clientcode = (String) session.getAttribute("clientcode");
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/schdule.png" /> <bean:message key="si.head.sisum" bundle="<%=clientcode%>"/></div>
<form method="post" id="siform"  >
    <div class="table-responsive card card-body card-shadow page_bottom_content">
        <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
        <table class="table table-striped table-bordered" id="datatablebybooking">
            <thead>
                <tr> 
                    <th><bean:message key="ecom.shippingins.action" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.shippingins.status" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="EBookingView.trustbookkno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.shippingins.bookingstatus" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="shipping.blno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.shippingins.blstatus" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="si.head.sino" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="shipping.agency.refno" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.surcharge.origin" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.surcharge.delivery" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.shippingins.depdate" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="EBookingView.crdate" bundle="<%=clientcode%>" ></bean:message></th>
                    <th><bean:message key="EBookingView.submitdate" bundle="<%=clientcode%>" ></bean:message></th>

                    </tr>
                </thead>
                <tbody>
                <%
                    Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                    if (session.getAttribute("SIdetails") != null) {
                        String bookno = "";
                        String bookstatus = "";
                        String shipperrefno = "";
                        String pol = "";
                        String pod = "";
                        String origin = "";
                        String delivery = "";
                        String depdate = "";
                        String dateCr = "";
                        String dateUp = "";
                        String status = "";
                        String action = "";
                        String sino = "";
                        String blno = "";
                        String blstatus = "";
                        String decline_status = "";
                        String label_status = "";
                        String labelbkstatus = "";
                        String islocked = "";
                        int pos = 0;
                        int i = 1;
                        int docid = 0;
                        String strStyleName = "";
                        ArrayList si_list1 = new ArrayList();
                        si_list1 = (ArrayList) session.getAttribute("SIdetails");
                        Set siSet = (Set) session.getAttribute("siAccessList");
                        if (siSet == null) {
                            siSet = new HashSet();
                        }
                        for (int k = 0; k < si_list1.size(); k++) {
                            SIsummaryVO sivo = (SIsummaryVO) si_list1.get(k);
                            strStyleName = "";
                            bookno = sivo.getBookno();
                            shipperrefno = sivo.getShipperrefno();
                            if (shipperrefno != null) {
                                shipperrefno = shipperrefno;
                            } else {
                                shipperrefno = "-";
                            }
                            pol = sivo.getPol() + " - " + portlist.get(sivo.getPol());
                            pod = sivo.getPod() + " - " + portlist.get(sivo.getPod());
                            origin = MaskPortUtil.swapActualPortToMaskPortWithName(sivo.getOriginPort() + " - " + portlist.get(sivo.getOriginPort()));
                            delivery = MaskPortUtil.swapActualPortToMaskPortWithName(sivo.getDeliveryPort() + " - " + portlist.get(sivo.getDeliveryPort()));
                            depdate = sivo.getDepdate();
                            dateCr = sivo.getDatecr();
                            dateUp = "";
                            status = sivo.getStatus();
                            islocked = sivo.getIssilocked();
                            if ((status.equalsIgnoreCase("Submitted") || status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Declined")) && (!("Y".equalsIgnoreCase(islocked)))) {
                                dateUp = sivo.getDateup();
                            }
                            decline_status = sivo.getStatus();
                            sino = sivo.getSI_no();
                            docid = sivo.getDocid();
                            blno = sivo.getBl_no();
                            System.out.println("[ECOM-WAR] SISummary blno========>" + blno);
                            blstatus = sivo.getBl_status();
                            if (blstatus.equalsIgnoreCase("D")) {
                                blstatus = "Draft";
                            } else if (blstatus.equalsIgnoreCase("C")) {
                                blstatus = "Confirmed";
                            } else if (blstatus.equalsIgnoreCase("I")) {
                                blstatus = "Issued";
                            } else if (blstatus.equalsIgnoreCase("V")) {
                                blstatus = "Cancelled";
                            }
                            bookstatus = sivo.getBookstatus();
                            if (bookstatus.equalsIgnoreCase("D")) {
                                bookstatus = "Draft";
                            } else if (bookstatus.equalsIgnoreCase("C")) {
                                bookstatus = "Confirmed";
                            }
                            action = "";
                            pos = 1;
                            if (i % 2 == 0) {
                                strStyleName = "even";
                            } else {
                                strStyleName = "odd";
                            }
                            if (status.equalsIgnoreCase("Submitted")) {
                                label_status = "<span class='label label-success'>Submitted</span>";
                            } else if (status.equalsIgnoreCase("Draft")) {
                                label_status = "<span class='label label-primary'>Draft</span>";
                            } else if (status.equalsIgnoreCase("Create SI") || status.equalsIgnoreCase("-")) {
                                label_status = "<span class='label label-info'>Create SI</span>";
                            } else if (status.equalsIgnoreCase("Create Multiple SI")) {
                                label_status = "<span class='label label-warning'>Create Multiple SI</span>";
                            } else if (status.equalsIgnoreCase("Accepted")) {
                                label_status = "<span class='label label-green'>Accepted</span>";
                            } else if (status.equalsIgnoreCase("Declined")) {
                                label_status = "<span class='label label-danger'>Declined</span>";
                            }

                            if (bookstatus.equalsIgnoreCase("Confirmed")) {
                                labelbkstatus = "<span class='label label-success'>Confirmed</span>";
                            } else if (bookstatus.equalsIgnoreCase("Draft")) {
                                labelbkstatus = "<span class='label label-green'>Accepted</span>";
                            }
                %>
                <tr class="<%=strStyleName%>">
                    <%if ("Y".equalsIgnoreCase(islocked)) {
                            label_status = "<span class='label label-danger'>Locked</span>";
                    %>
                    <td align="center" class="text"><a class="btn btn-lg text-danger" href="#" data-toggle="tooltip" title="Locked" data-placement="left"><i class="fa fa-lock"></i></a></td>
                            <%
                            } else {

                                if (status.equals("-") && ((blno == null || blno.length() == 0 || blno == ""))) {
                            %>
                    <td align="center"><a onclick="redirectPage('<%=bookno%>', '<%=pos++%>', 'edit', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')" class="btn btn-lg text-success" data-toggle="tooltip" title="Create SI" data-placement="left"><i class="fa fa-plus-square"></i></a></td>
                            <%
                                }
                                if ("Declined".equals(status)) {

                            %>
                    <td align="center">
                        <!--<a href="javascript:void(0)" class="btn btn-lg text-danger" data-toggle="tooltip" title="Declined" data-placement="left"><i class="fa fa-ban"></i></a>-->
                        <a class="btn btn-lg text-info" href="#" data-toggle="tooltip" title="View" data-placement="left" onclick="callSIView('<%=sino%>', '<%=status%>', '<%=bookno%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')"><i class="fa fa-search"></i></a>
                        <a href="#" onclick="checkBLStatus('<%=bookno%>', '<%=pos++%>', 'Draft', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=sivo.getAppVersion()%>', '<%=status%>')" class="btn btn-lg text-success" data-toggle="tooltip" title="Resubmit" data-placement="left"> <i class="fa fa-repeat"></i></a>
                    </td>

                    <%                                                                                            }
                        if ("Create SI".equals(status) && ((blno == null || blno.length() == 0 || blno == ""))) {

                    %>
                    <td align="center" class="text">
                        <a  href="javascript:void(0)" onclick="editSI('<%=bookno%>', '<%=pos++%>', 'edit', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')" class="btn btn-lg text-warning" data-toggle="tooltip" title="Create SI" data-placement="left"><i class="fa fa-pencil-square-o"></i></a>     </td>
                            <%
                                }

                                if ("Create Multiple SI".equals(status)) {

                            %>
                    <td align="center" class="text">
                        <a href="javascript:void(0)" onclick="editSI('<%=bookno%>', '<%=pos++%>', 'edit', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')" class="btn btn-lg text-warning" data-toggle="tooltip" title="Create Multiple SI" data-placement="left"><i class="fa fa-pencil-square-o"></i></a> </td>
                            <%
                                }

                                if ("Submitted".equals(status) || "".equals(status)) {

                            %>
                    <td align="center" class="text">
                        <a class="btn btn-lg text-info" href="#" data-toggle="tooltip" title="View" data-placement="left" onclick="callSIView('<%=sino%>', '<%=status%>', '<%=bookno%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')"><i class="fa fa-search"></i></a>
                            <% if ("Submitted".equals(status)) {//&& "Draft".equals(bookstatus)%>
                        <a href="#" onclick="editAcceptSI('<%=bookno%>', '<%=pos++%>', 'Draft', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=bookstatus%>', '<%=sivo.getAppVersion()%>')" class="btn btn-lg text-warning" data-toggle="tooltip" title="Edit" data-placement="left"> <i class="fa fa-pencil-square-o"></i></a>
                            <%}%>
                            <% if ("Cancelled".equals(blstatus)) {%>
                        <a href="#" onclick="checkBLStatus('<%=bookno%>', '<%=pos++%>', 'Draft', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=sivo.getAppVersion()%>', '<%=status%>')" class="btn btn-lg text-success" data-toggle="tooltip" title="Resubmit" data-placement="left"> <i class="fa fa-repeat"></i></a>
                            <%}%>
                        <a href="javascript:void(0)" onclick="cancelSubmitedSI('<%=sino%>', '<%=bookno%>', '<%=sivo.getAppVersion()%>')" title="Cancel" class="btn btn-lg text-danger" data-toggle="tooltip"  data-placement="left"><i class="fa fa-times"></i></a>
                    </td>
                    <%                               }

                        if ("Accepted".equals(status) || "".equals(status)) {

                    %>
                    <td align="center" class="text"><a class="btn btn-lg text-danger" href="#" data-toggle="tooltip" title="Accepted" data-placement="left"><i class="fa fa-lock"></i></a>
                        <a class="btn btn-lg text-info" href="#" data-toggle="tooltip" title="View" data-placement="left" onclick="callSIView('<%=sino%>', '<%=status%>', '<%=bookno%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')"><i class="fa fa-search"></i></a>
                            <% if ("Cancelled".equals(blstatus)) {%>
                        <a href="#" onclick="checkBLStatus('<%=bookno%>', '<%=pos++%>', 'Draft', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')" class="btn btn-lg text-success" data-toggle="tooltip" title="Resubmit" data-placement="left"> <i class="fa fa-repeat"></i></a>
                            <%}%>
                    </td>
                    <%                               }

                        if ("Draft".equals(status)) {
                    %>

                    <td align="center" class="text">
                        <a href="#" onclick="editSI('<%=bookno%>', '<%=pos++%>', 'Draft', '<%=sino%>', '<%=blstatus%>', '<%=blno%>', '<%=sivo.getAppVersion()%>')" class="btn btn-lg text-warning" data-toggle="tooltip" title="Edit" data-placement="left"> <i class="fa fa-pencil-square-o"></i></a>
                    </td>

                    <%}
                        }
                    %>
                    <td><%=label_status%></td>
                    <td><%=bookno%></td>
                    <td><%=labelbkstatus%></td>
                    <td><%=blno%></td>
                    <td><%=blstatus%></td>
                    <td><%=sino%></td>
                    <td><%=sivo.getAgencyRefNo()%></td>
                    <td><%=origin%></td>
                    <td><%=delivery%></td>
                    <td><%=depdate%></td>
                    <td><%=dateCr%></td>
                    <td><%=dateUp%></td>

                </tr>

                <%       i = i + 1;

                            if (bookno != null && bookno != "") {
                                siSet.add(bookno);
                            }
                            if (sino != null && sino != "") {
                                siSet.add(sino);
                            }
                        }
                        session.setAttribute("siAccessList", siSet);
                    }
                %>
            </tbody>
        </table>
    </div>
    <input type="hidden" id="blno" name="blno">
    <input type="hidden" id="appversion" name="appversion">
    <input type="hidden" id="bookno" name="bookno">
    <input type="hidden" id="sino" name="sino">
    <input type="hidden" id="submittedsi" name="submittedsi">
    <input type="hidden" id="type" name="type"> 
    <input type="hidden" id="bookstatus" name="bookstatus"> 
    <input type="hidden" id="resubmission" name="resubmission">
</form>

<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title">    <bean:message key="si.tocreatesi" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body" >
                <div class="form-group row">
                    <label for="recipient-name" class="control-label">  <bean:message key="si.choosenoofsi" bundle="<%=clientcode%>"/></label>
                    <select name="noOfSi" id="noOfSi" class="BoldText"><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option></select>
                </div>
            </div>
            <div class="modal-footer">

                <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                <button type="button" class="btn btn-primary"  title="Access denied" >  <bean:message key="si.tocreatesi" bundle="<%=clientcode%>"/></button>
                <%} else {%>
                <button type="button" class="btn btn-primary" onclick="createSI()" >  <bean:message key="si.tocreatesi" bundle="<%=clientcode%>"/></button>
                <%}%>
                <button type="button" class="btn btn-default" data-dismiss="modal">  <bean:message key="ecom.close" bundle="<%=clientcode%>"/></button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">

    $(document).ready(function () {
        $('#datatablebybooking').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        $('[data-toggle="tooltip"]').tooltip();
    });
    var bookno = "";
    var position = "";

    function createSI()
    {
        var bookstatus = "";
        if (blstatus == 'Confirmed') {
            alert("<bean:message key="ecom.msg.createsi" bundle="<%=clientcode%>"/>");
        } else if (blstatus == 'Issued') {
            alert("<bean:message key="ecom.msg.issuebl" bundle="<%=clientcode%>"/>");
        } else {
            openLoader();
            $('#blno').val(blno);
            document.forms[0].action = "EditSI.do?type=" + type + "&bookno=" + bkno + "&sino=" + sino + "&bookstatus=" + bookstatus;
            document.forms[0].submit();
        }
        /**$('#myModal').modal('hide');
         callLoading();
         var noofsi=document.getElementById("noOfSi").value;
         document.forms[0].action="CreateSI.do?type=view&bookno="+bookno+"&sicount="+noofsi+"&arraypos="+position;
         document.forms[0].submit();**/
    }

    function redirectPage(bkno, pos, type, sino, blstatus, blno, appversion) {
        $('#appversion').val(appversion);
        var bookstatus = "";
        $.ajax({
            url: "ecomAjax?type=checkblnofrombookno&bookNo=" + encodeURIComponent(bkno) + "&appversion=" + appversion,
            type: 'post',
            async: false,
            success: function (data) {
                if (data == 'C' || data == 'c') {
                    alert("<bean:message key="ecom.msg.createsi" bundle="<%=clientcode%>"/>");
                } else {
                    openLoader();
                    $('#blno').val(blno);
                    $('#type').val(type);
                    $('#bookno').val(bkno);
                    $('#sino').val(sino);
                    $('#bookstatus').val(bookstatus);
                    document.forms[0].action = "EditSI.do";
                    document.forms[0].submit();
                }
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });

        /**if(blstatus=='Confirmed'){
         alert("BL is Confirmed hence SI Cannot be created");
         }else if(blstatus=='Issued'){
         alert("BL is Issued hence SI Cannot be created");
         }else if(blstatus=='Declined'){
         alert("Bookning is Declined hence SI Cannot be created");
         }
         else{
         position=pos;
         bookno=bkno;
         $('#myModal').modal('show');
         return false;
         }**/
    }
    function checkBLStatus(bkno, pos, type, sino, blstatus, blno, appversion, sistatus) {
        $('#appversion').val(appversion);
        if (sistatus == 'Declined') {
            $.ajax({
                type: "POST",
                async: false,
                cache: false,
                url: "SIAjax?type=getsideclinedmsg&sino=" + encodeURIComponent(sino),
                success: function (response) {
                    var b = confirm("<bean:message key="ecom.msg.declinedremarks" bundle="<%=clientcode%>"/> : " + response + "\n\n<bean:message key="ecom.msg.editsi" bundle="<%=clientcode%>"/>");
                    if (b == false)
                    {
                        return false;
                    } else
                    {
                        $.ajax({
                            url: "ecomAjax?type=checkblnofrombookno&bookNo=" + encodeURIComponent(bkno) + "&appversion=" + appversion,
                            type: 'post',
                            async: false,
                            success: function (data) {
                                if (data == 'C' || data == 'c') {
                                    alert("<bean:message key="ecom.msg.createsi" bundle="<%=clientcode%>"/>");
                                } else if (data == 'D' || data == 'd') {
                                    alert("<bean:message key="ecom.msg.correctionthroughbl" bundle="<%=clientcode%>"/>");
                                } else {
                                    openLoader();
                                    $('#blno').val(blno);
                                    $('#type').val(type);
                                    $('#bookno').val(bkno);
                                    $('#sino').val(sino);
                                    $('#resubmission').val('yes');
                                    document.forms[0].action = "EditSI.do";
                                    document.forms[0].submit();
                                }
                            },
                            error: function (xhr) {
                                if (xhr.status == 403) {
                                    window.location.href = "SessionExpired.jsp";
                                }
                            }
                        });
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else {
            editSI(bkno, pos, type, sino, blstatus, blno, appversion);
        }

    }

    function editSI(bkno, pos, type, sino, blstatus, blno, appversion)
    {
        $('#appversion').val(appversion)
        var bookstatus = "";
        $.ajax({
            url: "ecomAjax?type=checkblnofrombookno&bookNo=" + encodeURIComponent(bkno) + "&appversion=" + appversion,
            type: 'post',
            async: false,
            success: function (data) {
                if (data == 'C' || data == 'c') {
                    alert("<bean:message key="ecom.msg.createsi" bundle="<%=clientcode%>"/>");
                } else {
                    openLoader();
                    $('#blno').val(blno);
                    $('#type').val(type);
                    $('#bookno').val(bkno);
                    $('#sino').val(sino);
                    $('#bookstatus').val(bookstatus);
                    document.forms[0].action = "EditSI.do";
                    document.forms[0].submit();
                }
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function editAcceptSI(bkno, pos, type, sino, blstatus, blno, bookstatus, appversion)
    {
        $('#appversion').val(appversion);
        var bookstatus = "D";
        if (blstatus == 'Confirmed') {
            alert("<bean:message key="ecom.msg.createsi" bundle="<%=clientcode%>"/>");
        } else if (blstatus == 'Issued') {
            alert("<bean:message key="ecom.msg.issuebl" bundle="<%=clientcode%>"/>");
        } else {
            openLoader();
            $('#blno').val(blno);
            $('#submittedsi').val("S");
            $('#blno').val(blno);
            $('#type').val(type);
            $('#bookno').val(bkno);
            $('#sino').val(sino);
            $('#bookstatus').val(bookstatus);
            document.forms[0].action = "EditSI.do";
            document.forms[0].submit();
        }
    }
    function callSIView(sino, status, bkno, blno, appversion) {
        $('#appversion').val(appversion);
        openLoader();
        $('#blno').val(blno);
        $('#type').val('view');
        $('#sino').val(sino);
        $('#bookno').val(bkno);
        document.forms[0].action = "EditSI.do";
        document.forms[0].submit();
    }
    function cancelSubmitedSI(sino, bookno, appversion) {
        var cancel = confirm("<bean:message key="ecom.msg.cancelsi" bundle="<%=clientcode%>"/>");
        if (cancel) {
            $.ajax({
                type: "POST",
                cache: false,
                url: "SIAjax?type=cancelsi&sino=" + encodeURIComponent(sino) + "&lrpbookno=" + encodeURIComponent(bookno) + "&appversion=" + encodeURIComponent(appversion),
                data: $(this).serializeArray(),
                success: function (data) {
                    if (data == 'Processed') {
                        alert("<bean:message key="ecom.msg.siprocessed" bundle="<%=clientcode%>"/>");
                    } else {
                        alert("<bean:message key="ecom.msg.cancelsi" bundle="<%=clientcode%>"/>");
                        openLoader();
                        location.href = "ShippingInstruction.do";
                    }
                },
                error: function (data, xhr) {
                    closeLoader();
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                    //$("#mgtemp").html("Manage Template");
                }
            });
        }
    }
</script>
