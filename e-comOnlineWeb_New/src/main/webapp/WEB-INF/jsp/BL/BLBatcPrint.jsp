<%-- 
    Document   : BLBatcPrint
    Created on : 13 Sep, 2024, 6:11:09 PM
    Author     : preethi.elumalai
--%>

<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,net.ecom.vo.OBLPrintVo,java.util.Hashtable,net.ecom.common.GeneralConf,net.ecom.common.Validation,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<style>
    div.dataTables_filter label, div.dataTables_wrapper div.dataTables_length label {
        text-align: right;
        margin-right: -64px;
    }
</style>
<% String errormsg = (String) request.getAttribute("OBLEDOCERROR");
    errormsg = errormsg != null ? errormsg : "";
    String clientcode = (String) session.getAttribute("clientcode");
//    List EBK_pluginList = (List) getServletContext().getAttribute("EBK_PLUGIN_DETAILS");
//    boolean confirm_edit = false, Issued_edit = false;
//    System.out.println("[ECOM-WAR] BLBatchPrint confirm_edit>>>>" + confirm_edit);
//    System.out.println("[ECOM-WAR] BLBatchPrint confirm_edit>>>>" + Issued_edit);
//    if (EBK_pluginList != null && EBK_pluginList.contains("EBC")) {
//        confirm_edit = true;
//        System.out.println("[ECOM-WAR] BLBatchPrint confirm_edit>>>>" + confirm_edit);
//    }
//    if (EBK_pluginList != null && EBK_pluginList.contains("EBI")) {
//        Issued_edit = true;
//        System.out.println("[ECOM-WAR] BLBatchPrint Issued_edit>>>>" + Issued_edit);
//    }

%>
<%if (errormsg != null && errormsg.length() > 0) {%>
<script type="text/javascript">
    alert('<bean:message key="ecom.msg.blopr" bundle="<%=clientcode%>"/>');</script>
    <%}%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.blbatchprint" bundle="<%=clientcode%>"/></div>
<div class="details_search page_bottom_content">
    <form name="blsummary" method="post"  id="blsummary" >
        <div class="table-responsive card card-body card-shadow ">
            <!--            <div style="text-align: right;">  
                            <button type="button" class="btn  btn-success text-white" style="padding:12px 65px" onclick="getSelectedBookings()" >Original Print</button>
                        </div>-->
            <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
            <table class="table table-striped table-bordered" id="datatablebybooking">
                <thead>
                    <tr>
                        <th><bean:message key="Schedule.View.select" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.status" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.blnumber" bundle="<%=clientcode%>"/></th>
                        <th>MCN No</th>
                        <th><bean:message key="Obl.bookno" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="ebooking.agency.reference" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Tracking.origin" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.load" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.discharge" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Obl.confirmeddate" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="EBookingView.crdate" bundle="<%=clientcode%>" ></bean:message></th>


                        </tr>
                    </thead>
                    <tbody>
                    <%
                        String cust_oblsts = (String) session.getAttribute("cust_oblStauts");
                        String printaccess = (String) session.getAttribute("OBLPRINTACCESS");
                        String userCode = (String) session.getAttribute("SESSION_USERCODE");
                        ArrayList book = (ArrayList) session.getAttribute("oblbatchprintView");
                        System.out.println("[ECOM-WAR] BLBatchPrint cust_oblsts>>>" + cust_oblsts+" printaccess>>>>" + printaccess);
                        Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                        String shipmentdate = "", fcnno = "", shipmentStatus = "", issueagency = "", blstatus = "", status = "", label_status = "", originalcp = "", draftcp = "", agencydocid = "", refval = "", islocked = "", blno = "", sino = "";
                        if (book != null && book.size() > 0) {
                            int size = book.size();
                            for (int i = 0; i < size; i++) {
                                OBLPrintVo eBooking = (OBLPrintVo) book.get(i);
                                status = eBooking.getStatus() != null ? eBooking.getStatus() : "";
                                originalcp = eBooking.getOriginals() != null ? eBooking.getOriginals() : "";
                                draftcp = eBooking.getCopies() != null ? eBooking.getCopies() : "";
                                refval = eBooking.getBlAcceptByUserStatus();
                                islocked = eBooking.getIssilocked();
                                agencydocid = eBooking.getDocumentalagencyid() != null ? eBooking.getDocumentalagencyid() : "";
                                blno = eBooking.getBookNo() != null ? eBooking.getBookNo() : "";
                                originalcp = eBooking.getOriginals() != null ? eBooking.getOriginals() : "";
                                draftcp = eBooking.getCopies() != null ? eBooking.getCopies() : "";
                                blstatus = eBooking.getStatus() != null ? eBooking.getStatus() : "";
                                agencydocid = eBooking.getDocumentalagencyid() != null ? eBooking.getDocumentalagencyid() : "";
                                issueagency = eBooking.getAgencyId() != null ? eBooking.getAgencyId() : "";
                                shipmentStatus = eBooking.getShipmentstatus() != null ? eBooking.getShipmentstatus() : "";
                                shipmentdate = eBooking.getReceiptshipdate() != null ? eBooking.getReceiptshipdate() : "";
                                fcnno = eBooking.getFcnno() != null ? eBooking.getFcnno() : "";
                                if (status.equalsIgnoreCase("C")) {
                                    label_status = "<span class='label label-success'>Confirm</span>";
                                }
                                if ("Y".equalsIgnoreCase(cust_oblsts)) {
                    %>
                    <tr>

                        <td>
                            <span></span>
                            <input type="checkbox" class="booking-checkbox" 
                                   value='{"blno":"<%=blno%>","type":"O","originalcp":"<%=originalcp%>","draftcp":"<%=draftcp%>","blstatus":"<%=blstatus%>","agencydocid":"<%=agencydocid%>","issueagency":"<%=issueagency%>","shipmentStatus":"<%=shipmentStatus%>","shipmentdate":"<%=shipmentdate%>","fcnno":"<%=fcnno%>"}'>
                        </td>
                        <td><%=label_status%></td>
                        <td><%=eBooking.getBookNo()%></td>
                        <td><%=Validation.checkNull(eBooking.getFcnverno())%></td>
                        <td><%=eBooking.getBook_no()%></td>
                        <td><%=eBooking.getAgencyRefNo()%></td>
                        <td><%=eBooking.getOriginPortId() + " - " + portlist.get(eBooking.getOriginPortId())%></td>
                        <td><%=eBooking.getLoadPortID() + " - " + portlist.get(eBooking.getLoadPortID())%></td>
                        <td><%=eBooking.getDischargePortId() + " - " + portlist.get(eBooking.getDischargePortId())%></td>
                        <td><%=eBooking.getFinalPortId() + " - " + portlist.get(eBooking.getFinalPortId())%></td>
                        <td><%=eBooking.getConfirmedDate()%></td>
                        <td><%=eBooking.getCrdate()%></td>

                    </tr>
                    <%
                                }
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
        <input type="hidden" id="bl_no" name="bl_no"> 
        <input type="hidden" id="agency_Code" name="agency_Code"> 
    </form>

    <div class="form-group">
        <div class="row ">
            <div class="col-xs-12 col-sm-12 note-info-trk" id="disclaim">
                <div style="font-weight: bold;"> <bean:message key="tracking.msg.head.disclaimer" bundle="<%=clientcode%>"/></div> 
                <div> <span > Any Electronic data transfer failure in batch BL print will be treated as a 'document lost, destroyed or damaged' case; LOI must be submitted to the local Documentation dept. to apply for reprinting.</span>   </div>

            </div>

        </div>

    </div>
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
            dom: 'Blfrtip',
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": [],
            buttons: [
                {
                    text: 'Original Print',
                    className: 'btn btn-success text-white',
                    action: function () {
                        getSelectedBookings();
                    },
                    init: function (api, node, config) {
                        $(node).css({
                            'padding': '8px 65px', // Increase padding for width
                            'width': '250px', // Increase the overall width
                            'height': '37px',
                            'white-space': 'nowrap',
                            'right': '479px'
                        });
                    }
                }
            ]
        });
    });
    function getSelectedBookings() {
        console.log('getSelectedBookings');
//        closeLoader();
        const checkboxes = $('.booking-checkbox:checked');
        const bookings = [];
        if (checkboxes.length > 0) {
            checkboxes.each(function () {
                const value = $(this).val();
                try {
                    const booking = JSON.parse(value);
                    bookings.push(booking);
                } catch (e) {
                    console.error('Failed to parse JSON:', e);
                }
            });
            var bookingsJson = JSON.stringify(bookings);
            console.log(bookings);
            openLoader();
            var url = "OblPrintAction.do?blprintdetails=" + bookingsJson;
            var params = "blprintdetails=" + bookingsJson;
            searchReq.open("POST", url, true);
            searchReq.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            searchReq.onreadystatechange = setOblPrintDialog;
            searchReq.send(params);
        } else {
            alert('Select the required BL to print');
        }
    }
</script>
<script type="text/javascript">
    var status;
    var URLStr;
    var type;
    var originals;
    var copies;
    function setOblPrintDialog() {
        if (searchReq.readyState == 4 && searchReq.status == 200) {
            document.getElementById("BLDialog").innerHTML = searchReq.responseText;
            var tblno = document.getElementById("originalBlno").value;
            if (document.getElementById("oblmsgres").value == "fail")
            {
                closeLoader();
                //alert("BL Signature is not available for " + agencycode + " agency");
                alert(tblno + ' ' + '<bean:message key="ecom.msg.e-signmissing" bundle="<%=clientcode%>"/>');
                return false
            } else if (document.getElementById("oblmsgres").value == "Success") {
                closeLoader();
                var responseType = document.getElementById("responseType").value;
                var viewtype = document.getElementById("viewtype").value;
//                var type = document.getElementById("prinType").value;
                document.getElementById("blsummary").action = "BLOriginalPrint?blno=" + tblno + "&responseType=" + responseType + "&viewtype=" + viewtype + "&printype=oblbatchprint";
                document.getElementById("blsummary").submit();
            } else
            {
                closeLoader();
                var errormsgs =  document.getElementById("oblmsgres").value ;
             alert(errormsgs);
                return false;
            }
        } else if (searchReq.readyState == 4 && searchReq.status == 403) {
            window.location.href = "SessionExpired.jsp";
        }
    }


</script>

