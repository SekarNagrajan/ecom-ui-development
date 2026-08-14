<%-- 
    Document   : TrackingAllMovement
    Created on : May 6, 2020, 9:42:05 AM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.tracking.vo.TrackingCargoActivity"%>
<%@page import="net.ecom.tracking.vo.TrackingCargoActivityResult"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="net.ecom.common.utils.DateConversion_New,java.util.List,java.util.Map,java.util.Hashtable,java.util.ArrayList,net.ecom.vo.TrackingVo,net.com.ecom.ws.vo.MovementDetailsList,net.com.ecom.ws.vo.MovementDetailsVo,net.ecom.common.Validation,net.ecom.tracking.vo.TrackingActvityResult,net.ecom.common.GeneralConf,net.ecom.tracking.vo.TrackingMasterVo" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('#email_text').keyup(function () {
            $('#mailreq').hide();
        });
    });
</script>
<%
    String strTrackingType = (String) request.getAttribute("trackingType");
    String invalidnumber = (String) request.getAttribute("Invalidnumber");
    if (invalidnumber != null) {
        invalidnumber = invalidnumber.startsWith(",") ? invalidnumber.substring(1) : invalidnumber;
    }
    String searchnumbers = "";
    String clientcode = (String) session.getAttribute("clientcode");
    List trackingalllist = (List) request.getAttribute("allmovementList");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String domainname = request.getRequestURL().toString();
    domainname = domainname.replace((request.getServletPath()), "");
    Hashtable vessellist = (Hashtable) session.getServletContext().getAttribute("SESSION_VESSELLIST");
    Hashtable serlist = (Hashtable) session.getServletContext().getAttribute("SESSION_SERVICELIST");
%>



<div class="table-responsive" id="referenceinvalid" style="display:none">
    <div class="alert alert-danger" role="danger"><bean:message key="tracking.msg.invalidrefornomoveavail" bundle="<%=clientcode%>"/> : <label id="invalidrefnum" style="font-weight:bold"></label>
    </div>
</div>

<div class="form-group">
    <div class="table-responsive" id="referenceid" style="display:none">
        <table>
            <tr>
                <th style="color:#FF0000"><label><bean:message key="tracking.msg.invalidrefornomoveavail" bundle="<%=clientcode%>"/> : </label></th>
                <td style="color:#0033CC"><label id="invalidrefno"></label><input type="hidden" id="searchtype" name="searchtype" value="<%=strTrackingType%>"/><input type="hidden" id="searchnumbers" name="searchnumbers"/></td>
            </tr>
        </table>
    </div>
</div>
<%
    String book_no = null, bl_no = null, eta = null, pol = null, pod = null, enable = "", etd = "";
    int trackpercentage = 0;
    TrackingActvityResult trackvo = null;
    TrackingCargoActivity trackcarVO = null;
    TrackingMasterVo res = null;
    if (trackingalllist != null && trackingalllist.size() > 0) {
        for (int i = 0; i < trackingalllist.size(); i++) {
            res = (TrackingMasterVo) trackingalllist.get(i);
            if (res != null) {
                book_no = Validation.checkNull(res.getBookNo());
                bl_no = Validation.checkNull(res.getBlNo());
                pod = (res.getFinalPortDesc() != null) ? res.getFinalPortDesc() : "";
                pol = (res.getOriginPortDesc() != null) ? res.getOriginPortDesc() : "";
                eta = (res.getPodAt() != null) ? DateConversion_New.convertETATrackingFormat(res.getPodAt().trim(), (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT), carrierCode) : "";
                if (eta.contains("1900")) {
                    eta = "";
                }

                trackpercentage = DateConversion_New.calculateTrackingPercentage(res.getPolAt(), res.getPodAt(), (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT));
%>

<div class="card card-body card-shadow page_scrol_data container_track_mail">
    <div class="container-fluid">
        <div class="form-group">
            <div class="row">

                <div class="col-sm-5">
                    <div class="txt_tra_clr"> <span><bean:message key="Tracking.booknum" bundle="<%=clientcode%>"/> </span></div>
                    <div class="txt_tra_data mail_bkgno"> <%=book_no%> </div>


                </div>

                <div class="col-sm-5">
                    <div class="txt_tra_clr"><bean:message key="ecom.trck.bilno" bundle="<%=clientcode%>"/> </div>
                    <div class="txt_tra_data mail_blno"> <%=bl_no%> </div>


                </div>

                <div class="col-sm-2">
                    <div class="txt_tra_clr"><%if("GWF".equalsIgnoreCase(carrierCode)){%><span><bean:message key="Tracking.pod" bundle="<%=clientcode%>"/> </span> <%}%> <bean:message key="Tracking.eta" bundle="<%=clientcode%>"/> </div>
                    <div class="txt_tra_data mail_eta"> <%=eta%> </div>


                </div>



            </div>
        </div>
        <div class = "hori_trackig_line"></div> 
        <div class="form-group">
            <div class="row">

                <div class="container">
                    <div class="stepwizard">
                        <div class="stepwizard-row setup-panel">

                            <div class=" stepwizard-step " style=" "> 
                                <img class="tracker_icon" src="<%=domainname%>/Design/assets/images/page_icons/marker_container.svg" />
                                <span class="txt_port_name mail_origin"> <%=pol%> </span>

                                <span class="track_ship_20" style="left: <%=trackpercentage%>%;">  <img class="ship_icon" src="<%=domainname%>/Design/assets/images/page_icons/ship_y.png" /> </span>

                            </div>

                            <div class="stepwizard-step " style="float: right"> 
                                <img class="tracker_icon" src="<%=domainname%>/Design/assets/images/page_icons/marker_container.svg" />
                                <span class="txt_port_name_right mail_delivery"> <%=pod%> </span>

                            </div>

                        </div>
                    </div>


                </div>

            </div>

        </div>
        <%
            String containernum = "", latestactivity = "", activitylocation = "";
            List<TrackingCargoActivityResult> movementList = res.getMovementList();
            if (movementList != null) {
                for (int k = 0; k < movementList.size(); k++) {
                    TrackingCargoActivityResult trackmstvo = (TrackingCargoActivityResult) movementList.get(k);
                    containernum = trackmstvo.getContainerNo();
                    List<TrackingCargoActivity> tracact = (List<TrackingCargoActivity>) trackmstvo.getContainerActivity();
                    if (tracact != null && tracact.size() > 0) {
                        trackcarVO = (TrackingCargoActivity) tracact.get(0);
                        latestactivity = Validation.checkNull(trackcarVO.getMovementDesc());
                        activitylocation = Validation.checkNull(trackcarVO.getPortName());
        %>

        <section>
            <div class="wrapper">
                <div class="row">
                    <div class="col-md-12">
                        <div class="accord">
                            <div data-toggle="collapse" href="#Trackingmovement<%=i + "" + k%>"><h3><strong><bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/> :</strong> <%=containernum%> <div class="float-right"><span class="p-r-10 mobile_hide"><bean:message key="Tracking.latestactive" bundle="<%=clientcode%>"/></span><span class="p-l-15 border-left mobile_hide"><%=latestactivity%> at <%=activitylocation%> </span> <i class="fa fa-chevron-down p-l-20" aria-hidden="true"></i></div></h3> </div>
                            <div class="cnt-ban collapse show" id="Trackingmovement<%=i + "" + k%>">
                                <div class="table-responsive overflow-hidden" >
                                    <div class="row d-none mobile_latest_activity" >
                                        <div class="col-md-12">
                                            <div><span class="latestactivitylbl"><bean:message key="Tracking.latestactive" bundle="<%=clientcode%>"/></span><p><%=latestactivity%> at <%=activitylocation%> </p></div>
                                        </div>
                                    </div>
                                    <div class="table-cnt">
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th><bean:message key="Tracking.Activitydate" bundle="<%=clientcode%>"/></th>
                                                    <th><bean:message key="Tracking.containertype" bundle="<%=clientcode%>"/></th>

                                                    <th><bean:message key="tracking.Service" bundle="<%=clientcode%>"/></th>
                                                    <th><bean:message key="tracking.Vessel" bundle="<%=clientcode%>"/></th>
                                                    <th><bean:message key="tracking.Voyage" bundle="<%=clientcode%>"/></th>
                                                    <th><bean:message key="tracking.Bound" bundle="<%=clientcode%>"/></th>

                                                    <th><bean:message key="ecom.trck.mvmttype" bundle="<%=clientcode%>"/></th>
                                                    <th><bean:message key="Tracking.activitylocation" bundle="<%=clientcode%>"/></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%for (int j = 0; j < tracact.size(); j++) {
                                                        trackcarVO = (TrackingCargoActivity) tracact.get(j);
                                                %>
                                                <tr>
                                                    <td><%=Validation.checkNull(trackcarVO.getActivityDate())%></td>
                                                    <td><%=Validation.checkNull(trackcarVO.getEqpTypeId())%></td>

                                                    <td><%=Validation.checkNull(trackcarVO.getServiceCode()) + " - " + Validation.checkNull((String) serlist.get(trackcarVO.getServiceCode()))%></td>
                                                    <td><%=Validation.checkNull(trackcarVO.getVessel()) + " - " + Validation.checkNull((String) vessellist.get(trackcarVO.getVessel()))%></td>
                                                    <td><%=Validation.checkNull(trackcarVO.getVoyage())%></td>
                                                    <td><%=Validation.checkNull(trackcarVO.getBound())%></td>

                                                    <td><%=Validation.checkNull(trackcarVO.getMovementDesc())%></td>
                                                    <td><%=Validation.checkNull(trackcarVO.getPortName())%></td>
                                                </tr>
                                                <%}%>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <%}
                }
            }%>


    </div>
</div>
<%}
        }
    }%>
<div class="form-group">
    <div class="row ">
        <div class="col-xs-12 col-sm-12 note-info-trk" id="disclaim">
            <div style="font-weight: bold;"> <bean:message key="tracking.msg.head.disclaimer" bundle="<%=clientcode%>"/></div> 
            <div> <bean:message key="tracking.msg.disclaimerline1" bundle="<%=clientcode%>"/> </div>
            <div> <bean:message key="tracking.msg.disclaimerline2" bundle="<%=clientcode%>"/></div>
            <div> <bean:message key="tracking.msg.disclaimerline3" bundle="<%=clientcode%>"/> </div>

        </div>

    </div>

</div>

<%
    if (invalidnumber != null && invalidnumber.length() > 0) {
%>
<script type="text/javascript">
    var invalidid = "<%=invalidnumber%>";
    document.getElementById("referenceinvalid").style.display = "block";
    document.getElementById("invalidrefno").innerHTML = invalidid;
    document.getElementById("invalidrefnum").innerHTML = invalidid;
</script>
<% }%>

<script type="text/javascript" src="Design/app-assets/js/scroll-custom.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var searchid = "<%=searchnumbers%>";
        document.getElementById("searchnumbers").value = searchid;
    });

</script>

<style type="text/css">
    .accord {
        font-size: 14px;
        max-width: 98%;
        margin:auto;
    }
    .accord h3 {
        border: 1px solid #0c3374 ;
        border-top-left-radius: 3px;
        background: #0c3374 ;
        margin: 2px 0 0 0;
        padding: 10px;
        border-top-right-radius: 3px;
        color: #fff;
    }
    .accord .cnt-ban {
        border: 1px solid #ccc;
        margin-bottom: 15px;
        padding: 8px;
    }
    .accord strong {
        color: #0c3374;
    }
    .accord table thead th {
        background-color: #fcac04 ;
        color: #fff ;
    }
    .accord h3 strong{color: #fff;}
    h1,h2,h3,h4,h5,h6{font-size: 100%;}
    .table td, .table th {padding: 5px;}
    .table td {font-size: 13px;}
    .latestactivitylbl{    font-weight: bold !important;color: #0c3374 !important;}

    @media only screen and (max-width: 980px) {
        .table-responsive.overflow-hidden{
            overflow: scroll !important;
        }
        .mobile_latest_activity {
            display: block !important;
        }
        .mobile_hide {
            display: none !important;
        }
    }
</style>