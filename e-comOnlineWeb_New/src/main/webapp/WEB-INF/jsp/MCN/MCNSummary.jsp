<%-- 
    Document   : MCNSummary
    Created on : 26 Oct, 2023, 3:12:12 PM
    Author     : prakash.mani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,net.ecom.vo.OBLPrintVo,java.util.Hashtable,net.ecom.common.GeneralConf,net.ecom.common.Validation,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<style>
    /*    .tooltip-inner {
            background-color: #1FBBA6;
            color: #fff;
        }
        .tooltip.left .tooltip-arrow  {
            border-left-color: #1FBBA6;
            color: #fff;
        }
        .label{
            font-size : 100%;
        }
        .label-green {
            background-color: #1FBBA6;
        }*/
</style>
<script>
    function editBL(blno, blstatus, bookno, appversion, fcnno)
    {
        $.ajax({
            type: "POST",
            cache: false,
            url: "BLAjax?blno=" + blno + "&type=isvoyageclosed",
            data: $(this).serializeArray(),
            success: function (data)
            {
                var isvoyageclosed = data;
                console.log("isvoyageclosed...>>>" + isvoyageclosed);
                /* if (isvoyageclosed === 'Y')
                 {
                 alert('<//bean:message key="ecom.msg.bl.isvoyageclosed" />');
                 } */
            }
        })
        openLoader();
        $('#appversion').val(appversion);
        $('#blno').val(blno);
        $('#bookno').val(bookno);
        $('#fcnno').val(fcnno);
        $('#type').val("edit");
        document.forms[0].action = "MCNEditAction.do";
        document.forms[0].submit();
    }
    function callBLView(sino, bkno, blno, appversion) {
        openLoader();
        $('#appversion').val(appversion);
        $('#blno').val(blno);
        $('#bookno').val(bkno);
        $('#sino').val(sino);
        $('#type').val("view");
        document.forms[0].action = "MCNEditAction.do";
        document.forms[0].submit();
    }
    function callBLAmendment(sino, bkno, blno, appversion) {
        openLoader();
        $('#appversion').val(appversion);
        $('#blno').val(blno);
        $('#bookno').val(bkno);
        $('#sino').val(sino);
        $('#type').val("blamend");
        document.forms[0].action = "MCNEditAction.do";
        document.forms[0].submit();
    }

</script>
<% String errormsg = (String) request.getAttribute("OBLEDOCERROR");
    errormsg = errormsg != null ? errormsg : "";
    String clientcode = (String) session.getAttribute("clientcode");
    String dispNonRatedBL = (String) getServletContext().getAttribute(GeneralConf.DisplayNon_RatedBLMsg);
    String dispBLNNprint = (String) getServletContext().getAttribute(GeneralConf.DisplayBL_NNCopypdfprint);
    List EBK_pluginList = (List) getServletContext().getAttribute("EBK_PLUGIN_DETAILS");
    boolean confirm_edit = false, Issued_edit = false;
    System.out.println("[ECOM-WAR] MCNSummary confirm_edit>>>>" + confirm_edit+ "  confirm_edit>>>>" + Issued_edit);
    if (EBK_pluginList != null && EBK_pluginList.contains("EBC")) {
        confirm_edit = true;
        System.out.println("[ECOM-WAR] MCNSummary confirm_edit>>>>" + confirm_edit);
    }
    if (EBK_pluginList != null && EBK_pluginList.contains("EBI")) {
        Issued_edit = true;
        System.out.println("[ECOM-WAR] MCNSummary Issued_edit>>>>" + Issued_edit);
    }

%>
<%if (errormsg != null && errormsg.length() > 0) {%>
<script type="text/javascript">
    alert('<bean:message key="ecom.msg.blopr" bundle="<%=clientcode%>"/>');</script>
    <%}%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.mcn" bundle="<%=clientcode%>"/></div>
<div class="details_search page_bottom_content">
    <form name="blsummary" method="post"  id="blsummary" >
        <div class="table-responsive card card-body card-shadow ">
            <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
            <table class="table table-striped table-bordered" id="datatablebybooking">
                <thead>
                    <tr>
                        <th><bean:message key="Obl.action" bundle="<%=clientcode%>"/></th>
                        <th>MCN Status</th>
                        <th>MCN No</th>
                        <th><bean:message key="Obl.blnumber" bundle="<%=clientcode%>"/></th>
                        <th>BL <bean:message key="Obl.status" bundle="<%=clientcode%>"/></th>                      
                        <th><bean:message key="Obl.bookno" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="ebooking.agency.reference" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Tracking.origin" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.load" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.discharge" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="EBookingView.crdate" bundle="<%=clientcode%>" ></bean:message></th>


                        </tr>
                    </thead>
                    <tbody>
                    <%
                        String cust_oblsts = (String) session.getAttribute("cust_oblStauts");
                        String printaccess = (String) session.getAttribute("OBLPRINTACCESS");
                        String userCode = (String) session.getAttribute("SESSION_USERCODE");
                        ArrayList book = (ArrayList) session.getAttribute("mcnprintView");
                        System.out.println("[ECOM-WAR] MCNSummary cust_oblsts>>>" + cust_oblsts+ "  printaccess>>>>" + printaccess);
                        Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                        String status = "", label_status = "", originalcp = "", draftcp = "", agencydocid = "", refval = "", islocked = "", mcnStatus = "", mcnStatusLabel = "";
                        if (book != null && book.size() > 0) {
                            int size = book.size();
                            for (int i = 0; i < size; i++) { //
                                OBLPrintVo eBooking = (OBLPrintVo) book.get(i);
                                status = eBooking.getStatus() != null ? eBooking.getStatus() : "";
                                originalcp = eBooking.getOriginals() != null ? eBooking.getOriginals() : "";
                                draftcp = eBooking.getCopies() != null ? eBooking.getCopies() : "";
                                refval = eBooking.getBlAcceptByUserStatus();
                                islocked = eBooking.getIssilocked();
                                mcnStatus = eBooking.getMcnstatus();
                                agencydocid = eBooking.getDocumentalagencyid() != null ? eBooking.getDocumentalagencyid() : "";
                                if (status.equalsIgnoreCase("C")) {
                                    label_status = "<span class='label label-success'>Confirm</span>";
                                } else if (status.equalsIgnoreCase("I")) {
                                    label_status = "<span class='label label-warning'>Issued</span>";
                                } else if (status.equalsIgnoreCase("D")) {
                                    label_status = "<span class='label label-info'>Draft</span>";
                                } else if (status.equalsIgnoreCase("A")) {
                                    label_status = "<span class='label label-green'>Accepted</span>";
                                } else if (status.equalsIgnoreCase("R")) {
                                    label_status = "<span class='label label-danger'>Rejected</span>";
                                } else if (status.equalsIgnoreCase("InComplete")) {
                                    label_status = "<span class='label label-danger'>InComplete</span>";
                                } else if (status.equalsIgnoreCase("S")) {
                                    label_status = "<span class='label label-danger'>Send Correction</span>";
                                }
                                if (status.equalsIgnoreCase("D") && "Y".equalsIgnoreCase(refval)) {
                                    label_status = "<span class='label label-green'>Accepted</span>";
                                }
                                if ("A".equalsIgnoreCase(mcnStatus)) {
                                    mcnStatusLabel = "<span class='label label-success'>Approved</span>";
                                } else if ("D".equalsIgnoreCase(mcnStatus)) {
                                    mcnStatusLabel = "<span class='label label-info'>Pending</span>";
                                } else if ("C".equalsIgnoreCase(mcnStatus)) {
                                    mcnStatusLabel = "<span class='label label-green'>Submitted</span>";
                                } else if ("S".equalsIgnoreCase(mcnStatus)) {
                                    mcnStatusLabel = "<span class='label label-danger'>Send Correction</span>";
                                } else {
                                    mcnStatusLabel = "";
                                }
                    %>
                    <tr>

                        <td>
                            <%if ("Y".equalsIgnoreCase(islocked)) {
                                    label_status = "<span class='label label-danger'>Locked</span>";
                            %>
                            <a class="btn btn-lg text-danger" href="#" data-toggle="tooltip" title="Locked" data-placement="left"><i class="fa fa-lock"></i></a>
                                <%
                                } else {

                                %>

                            <% if ("A".equalsIgnoreCase(mcnStatus) && "Y".equalsIgnoreCase(eBooking.getPrintstatus().trim()) && "Y".equalsIgnoreCase(cust_oblsts)) {%> <!-- removed print access condition -> && "YES".equalsIgnoreCase(printaccess)-->
                            <a href="javascript:void(0)" onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'O', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Original" data-placement="left" class="btn btn-lg text-success" data-fancybox-type="iframe"><i class="fa fa-print"></i></a>
                            | <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'I', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Non Negotiable" data-placement="left" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a> | <a class="btn btn-lg text-danger"  href="#" data-toggle="tooltip" title="Issued" data-placement="left"><i class="fa fa-lock"></i></a>

                            <%
                            } else if ("D".equalsIgnoreCase(mcnStatus) || "C".equalsIgnoreCase(mcnStatus)) {
                            %>
                            <a onClick="editBL('<%=eBooking.getBookNo()%>', '<%=eBooking.getStatus()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Edit" data-placement="left" class="btn btn-lg text-warning" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-pencil-square-o"></i></a>
                            | <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'D', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Draft" data-placement="left" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a>

                            <%                       } else if ("S".equalsIgnoreCase(mcnStatus)) {
                            %>
                            <a href="javascript:void(0)" onClick="callBLView('<%=eBooking.getEcomsino()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getBookNo()%>', '<%=eBooking.getAppVersion()%>')" data-toggle="tooltip" title="View" data-placement="left" class="fancybox1 btn btn-lg text-info" data-fancybox-type="iframe"><i class="fa fa-search"></i></a>
                            |  <a onClick="callBLAmendment('<%=eBooking.getEcomsino()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getBookNo()%>', '<%=eBooking.getAppVersion()%>')"  data-toggle="tooltip" title="Amendment" data-placement="left" class="btn btn-lg text-warning" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-pencil-square-o"></i></a>
                            | <a href="javascript:void(0)" onclick="cancelSubmitedBL('<%=eBooking.getEcomsino()%>', '<%=eBooking.getAppVersion()%>')" title="Cancel" class="btn btn-lg text-danger" data-toggle="tooltip"  data-placement="left"><i class="fa fa-times"></i></a>
                                <%
                                } else if ("A".equalsIgnoreCase(mcnStatus) || "I".equalsIgnoreCase(eBooking.getStatus().trim())) {
                                %>
                            <a onClick="editBL('<%=eBooking.getBookNo()%>', '<%=eBooking.getStatus()%>', '<%=eBooking.getBook_no()%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Edit" data-placement="left" class="btn btn-lg text-warning" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-pencil-square-o"></i></a>
                            | <a onClick="callbl('<%=eBooking.getBookNo()%>', '<%=eBooking.getAgencyId()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getBook_no()%>', 'I', '<%=originalcp%>', '<%=draftcp%>', '<%=status%>', '<%=agencydocid%>', '<%=eBooking.getAppVersion()%>', '<%=eBooking.getShipmentstatus()%>', '<%=eBooking.getReceiptshipdate()%>', '<%=eBooking.getFcnno()%>')"  data-toggle="tooltip" title="Non Negotiable" data-placement="left" class="fancybox1 btn btn-lg text-danger" data-fancybox-type="iframe" href="javascript:void(0)"><i class="fa fa-file-pdf-o"></i></a> 

                            <%
                            } else {
                            %>

                            <%}
                                }%>
                        </td>
                        <td><%=mcnStatusLabel%></td>
                        <td><%=Validation.checkNull(eBooking.getFcnverno())%></td>
                        <td><%=eBooking.getBookNo()%></td>
                        <td><%=label_status%></td>
                        <td><%=eBooking.getBook_no()%></td>
                        <td><%=eBooking.getAgencyRefNo()%></td>
                        <td><%=eBooking.getOriginPortId() + " - " + portlist.get(eBooking.getOriginPortId())%></td>
                        <td><%=eBooking.getLoadPortID() + " - " + portlist.get(eBooking.getLoadPortID())%></td>
                        <td><%=eBooking.getDischargePortId() + " - " + portlist.get(eBooking.getDischargePortId())%></td>
                        <td><%=eBooking.getFinalPortId() + " - " + portlist.get(eBooking.getFinalPortId())%></td>
                        <td><%=eBooking.getCrdate()%></td>

                    </tr>
                    <%
                            } //end for loop
                        }
                    %>
                </tbody>
            </table>
        </div>
        <input type="hidden" id="usercode" name="usercode" value="<%=userCode%>">
        <input type="hidden" id="appversion" name="appversion">
        <input type="hidden" id="blno" name="blno">       
        <input type="hidden" id="bookno" name="bookno">  
        <input type="hidden" id="sino" name="sino">
        <input type="hidden" id="fcnno" name="fcnno">
        <input type="hidden" id="type" name="type"> 
    </form>
</div>
<div class="modal fade" id="myModal" role="dialog" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content" style="width:1132px;heigth:900px;margin-left: 7%;">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"><bean:message key="bl.head" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" onclick="refreshobldata()" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div  id="oblprintmodal" style="padding-left:2%;padding-right:2%">
            </div>

        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<%if ("Y".equalsIgnoreCase(dispNonRatedBL)) {%>

<div class="BLNotecontent text-danger"><bean:message key="ecom.bkg.filenote" bundle="<%=clientcode%>"/>: <bean:message key="ecom.bl.notecontent" bundle="<%=clientcode%>"/></div>
<%}%>
<div id="BLDialog">
</div>
<script type="text/javascript">
    //Our XmlHttpRequest object to get the auto suggest
    var searchReq = getXmlHttpRequestObject();
    function getXmlHttpRequestObject() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert("<bean:message key="ecom.msg.browsernotsupported" bundle="<%=clientcode%>"/>");
        }
    }
    function refreshobldata()
    {
        location.reload();
    }
    $(document).ready(function () {
        // $('[data-toggle="tooltip"]').tooltip();
        $('#datatablebybooking').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });</script>
<script type="text/javascript">
    var status;
    var URLStr;
    var type;
    var originals;
    var copies;
    var popUpWin = 0, tblno, blstatus, varprintcopies, varblno, shipmentstatus, bookno, sts, tbltype, toriginalcp, tdraftcp, tblstatus, tagencydocid, tissueagency;
    function callbl(blno, agencycode, shipstatus, book_no, bltype, originalcp, draftcp, blstatus, agencydocid, appversion, shipmentStatus, shipmentdate, fcnno) {
        if (appversion != '1') {
            tblno = blno;
            varblno = blno;
            blstatus = blstatus;
            shipmentstatus = shipstatus;
            bookno = book_no;
            sts = blstatus;
            tbltype = bltype;
            toriginalcp = originalcp;
            tdraftcp = draftcp;
            tblstatus = blstatus;
            tagencydocid = agencydocid;
            tissueagency = agencycode;
            openLoader();

//            var printDetails = '[{"blno":"' + tblno + '","type":"'+tbltype+'","originalcp":"' + toriginalcp + '","draftcp":"' + tdraftcp + '","blstatus":"' + tblstatus + '","agencydocid":"' + tagencydocid + '","issueagency":"' + tissueagency + '","shipmentStatus":"' + shipmentStatus + '","shipmentdate":"' + shipmentdate + '","fcnno":"' + fcnno + '"}]';
//            var url = "OblPrintAction.do?blprintdetails=" + printDetails;
//            var url = "OblPrintAction.do?blno=" + tblno + "&type=" + tbltype + "&originalcp=" + toriginalcp + "&draftcp=" + tdraftcp + "&blstatus=" + tblstatus + "&agencydocid=" + tagencydocid + "&issueagency=" + tissueagency + "&shipmentStatus=" + shipmentStatus + "&shipmentdate=" + shipmentdate + "&fcnno=" + fcnno;

            var url = "MCNPrintAction.do?blno=" + tblno + "&type=" + tbltype + "&originalcp=" + toriginalcp + "&draftcp=" + tdraftcp + "&blstatus=" + tblstatus + "&agencydocid=" + tagencydocid + "&issueagency=" + tissueagency + "&shipmentStatus=" + shipmentStatus + "&shipmentdate=" + shipmentdate + "&fcnno=" + fcnno;
            var params = "blno=" + tblno + "&type=" + tbltype + "&originalcp=" + toriginalcp + "&draftcp=" + tdraftcp + "&blstatus=" + tblstatus + "&agencydocid=" + tagencydocid + "&issueagency=" + tissueagency + "&shipmentStatus=" + shipmentStatus + "&shipmentdate=" + shipmentdate + "&fcnno=" + fcnno;
            searchReq.open("POST", url, true);
            searchReq.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            searchReq.onreadystatechange = setOblPrintDialog;
            searchReq.send(params);
        } else {
            callV1DraftBL(blno, agencycode, shipstatus, book_no, bltype, originalcp, draftcp, blstatus, agencydocid, appversion, shipmentStatus);
            //alert('This option is unavailable due to system upgrade. Kindly collect the details from your local agency.');
        }
    }

    function setOblPrintDialog() {
        if (searchReq.readyState == 4 && searchReq.status == 200) {
            document.getElementById("BLDialog").innerHTML = searchReq.responseText;
            if (document.getElementById("oblmsgres").value == "fail")
            {
                closeLoader();
                //alert("BL Signature is not available for " + agencycode + " agency");
                alert('<bean:message key="ecom.msg.e-signmissing" bundle="<%=clientcode%>"/>');
                return false
            } else if (document.getElementById("oblmsgres").value == "Success") {
                closeLoader();
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
                document.getElementById("blsummary").action = "BLOriginalPrint?blno=" + tblno + "&responseType=" + responseType + "&viewtype=" + viewtype + "&printype=oblprint&printype=mcn";
                document.getElementById("blsummary").submit();
            } else
            {
                closeLoader();
                alert(document.getElementById("oblmsgres").value);
                return false;
            }
        } else if (searchReq.readyState == 4 && searchReq.status == 403) {
            window.location.href = "SessionExpired.jsp";
        }
    }
    function callV1DraftBL(blno, agencycode, shipstatus, book_no, bltype, originalcp, draftcp, blstatus, agencydocid, appversion, shipmentStatus) {
        var blprinttype;
        if (bltype == 'D') {
            blprinttype = 'bldraft';
        } else if (bltype == 'I') {
            blprinttype = 'NN';
        }
        if (bltype == 'D' || bltype == 'I') {
            openLoader();
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLV1DraftJasper?blno=" + blno + "&type=" + blprinttype + "&usercode=" + $('#usercode').val(),
                data: $(this).serializeArray(),
                success: function (data) {
                    closeLoader();
                    if (data != null) {
                        $.fancybox.open({
                            maxWidth: 900,
                            maxHeight: 700,
                            fitToView: false,
                            width: '100%',
                            height: '100%',
                            autoSize: false,
                            scrollOutside: true,
                            closeClick: false,
                            src: "ViewPDF.jsp",
                            type: 'iframe',
                            overlayShow: false,
                            iframe: {
                                preload: false // fixes issue with iframe and IE
                            }
                        });
                        return true;
                    } else {
                        return false;
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else if (bltype == 'O') {
            $.ajax({
                type: "POST",
                cache: false,
                async: false,
                url: "BLAjax?book_no=" + book_no + "&type=v1signaturecheck&agencycode=" + agencycode + "&shipmentstatus=" + shipmentStatus,
                data: $(this).serializeArray(),
                success: function (data) {
                    var response = data;
                    response = response.split("~~");
                    if (response.length > 1) {
                        if (response[0] == 'No') {
                            alert('<bean:message key="ecom.msg.e-signmissing" bundle="<%=clientcode%>"/>');
                            return false;
                        }
                        if (response[1] == 0) {
                            openLoader();
                            document.getElementById("blsummary").action = "BLPrintController?type=Original" + "&blno=" + blno + "&originals=" + originalcp;
                            document.getElementById("blsummary").submit();// 
                        } else {
                            alert('<bean:message key="ecom.msg.blopracc" bundle="<%=clientcode%>"/>');
                            return false;
                        }
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }

    }
    function callviewbl(blno, bltype, originalcp, draftcp, blstatus, agencydocid, appversion) {
        if (appversion != '1') {
            openLoader();
            /**if (blstatus == 'C' || blstatus == 'D') {
             blstatus = "view";
             } else if (blstatus == 'I') {
             blstatus = "copies";
             }**/
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLJasper?blno=" + blno + "&type=" + bltype + "&originalcp=" + originalcp + "&draftcp=" + draftcp + "&blstatus=" + blstatus + "&agencydocid=" + agencydocid,
                data: $(this).serializeArray(),
                success: function (data) {
                    if ('Success' == data)
                    {
                        closeLoader();
                        $.fancybox.open({
                            maxWidth: 900,
                            maxHeight: 800,
                            fitToView: false,
                            width: '100%',
                            height: '100%',
                            autoSize: false,
                            scrollOutside: true,
                            src: "ViewPDF.jsp",
                            closeClick: false,
                            type: 'iframe',
                            overlayShow: false,
                            iframe: {
                                preload: false // fixes issue with iframe and IE
                            }
                        });
                    } else {
                        closeLoader();
                        alert(data);
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        } else {

            alert('<bean:message key="ecom.msg.sysupgrade" bundle="<%=clientcode%>"/>');
        }
    }

    function cancelSubmitedBL(sino, appversion) {
        var cancel = confirm("<bean:message key="ecom.msg.cancelbl" bundle="<%=clientcode%>"/>");
        if (cancel) {
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLAjax?type=cancelbl&sino=" + encodeURIComponent(sino) + "&appversion=" + encodeURIComponent(appversion),
                data: $(this).serializeArray(),
                success: function (data) {
                    if (data == 'Processed') {
                        alert("<bean:message key="ecom.msg.blcorrectionprocessed" bundle="<%=clientcode%>"/>");
                    } else {
                        alert("<bean:message key="ecom.msg.cancelsi" bundle="<%=clientcode%>"/>");
                        openLoader();
                        location.href = "mcn.do";
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
    function acceptBLByUser(blno, agencycode, bookno, appversion, agencyrefno, origin, delivery) {
        var cancel = confirm("<bean:message key="ecom.msg.cnfrmbl" bundle="<%=clientcode%>"/>");
        if (cancel) {
            $.ajax({
                type: "POST",
                cache: false,
                url: "BLAjax?type=acceptbl&blno=" + encodeURIComponent(blno) + "&agencycode=" + agencycode + "&bookno=" + bookno + "&agencyrefno=" + agencyrefno + "&origin=" + origin + "&delivery=" + delivery,
                data: $(this).serializeArray(),
                success: function (data) {
                    alert("<bean:message key="bl.msg.blaccept" bundle="<%=clientcode%>"/>");
                    openLoader();
                    location.href = "mcn.do";
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

    function viewpolicy(policyno) {
        openLoader();
        $.ajax({
            type: "POST",
            cache: false,
            url: "InsuranceAjax?type=getpolicy&policyno=" + policyno,
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                closeLoader();
                $.fancybox.open(data, {
                    padding: 0,
                    showCloseButton: false,
                    closeClick: false,
                    maxWidth: 1200,
                    maxHeight: 600,
                    fitToView: true,
                    async: false,
                    width: '90%',
                    height: '100%',
                    autoSize: true,
                    scrollOutside: true,
                    closeBtn: true,
                    hideOnOverlayClick: false,
                    overlayShow: false,
                    helpers: {
                        overlay: {closeClick: false}
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


