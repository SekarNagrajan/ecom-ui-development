<%@ page contentType="text/html; charset=UTF-8" language="java" import="net.ecom.common.utils.DateConversion_New,java.util.Set,java.util.List,java.util.Map,java.util.Hashtable,java.util.ArrayList,net.ecom.vo.TrackingVo,net.com.ecom.ws.vo.MovementDetailsList,net.com.ecom.ws.vo.MovementDetailsVo,net.ecom.common.Validation,net.ecom.tracking.vo.TrackingActvityResult,net.ecom.common.GeneralConf,java.util.HashMap,net.ecom.vo.CutoffConfigVO,net.ecom.tracking.vo.TrackingProtActivityResult,com.fasterxml.jackson.databind.ObjectMapper,net.ecom.common.MaskPort,net.ecom.common.utils.MaskPortUtil" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Set" %> 
<%@ page import="java.util.List" %> 
<%@ page import="java.text.SimpleDateFormat, java.util.Date" %>
<%@ page import="java.text.ParseException" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('#email_text').keyup(function () {
            $('#mailreq').hide();
        });
    });
</script>
<style>
    .larger-column {
        flex: 2.4;

        .smaller-column {
            flex: 1.7;
        }
        .small-column {
            flex: 1.5;
        }
        .separator_line {
    width: 100%;
    height: 1px;
    background: #e0e0e0;
    margin: 10px 0;
}
.livemap-row-btn {
    font-size: 11px;
    font-weight: 600;
    padding: 3px 8px;
    border-radius: 4px;
    white-space: nowrap;
    transition: background-color 0.2s ease, transform 0.15s ease, box-shadow 0.2s ease;
    box-shadow: 0 1px 4px rgba(40,167,69,0.3);
}
.livemap-row-btn:hover {
    background-color: #1e7e34;
    border-color: #1a7031;
    box-shadow: 0 2px 8px rgba(40,167,69,0.5);
    transform: translateY(-1px);
}
.livemap-row-btn i {
    font-size: 12px;
}
    </style>

    <%
        String strTrackingType = (String) request.getAttribute("trackingType");
        String invalidnumber = (String) request.getAttribute("Invalidnumber");
        if (invalidnumber != null) {
            invalidnumber = invalidnumber.startsWith(",") ? invalidnumber.substring(1) : invalidnumber;
        }
        String strUserCode = (String) session.getAttribute("SESSION_USERCODE");
        String searchnumbers = "";
        String user = (String) session.getAttribute("sessionAdmin");
        String clientcode = (String) session.getAttribute("clientcode");
        String imagepath = (String) getServletContext().getAttribute("Imagepath");
        String logo_name = (String) getServletContext().getAttribute(GeneralConf.Logo_Name);
        String logo = imagepath + logo_name;
        Map<String, List> trackingdetailmap = (Map) request.getAttribute("mov");
        Map<String, List> trackingdetailFullHistorymap = (Map) request.getAttribute("movFullHistory");
        List<TrackingActvityResult> lstresult = null;
        Map<String, List> tracklst = (Map) request.getAttribute("transportlist");
        String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
        String showfullHistoryinMail = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.SHOW_FULLHISTORY_IN_TRACK_MAIL));
        long jsdate = new java.util.Date().getTime();
        String domainname = request.getRequestURL().toString();
        domainname = domainname.replace((request.getServletPath()), "");
        String page_title = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.PAGE_TITLE));
        String display_Tracking_Cutoff = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Display_Tracking_Cutoff));
        String transportplan_track = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.show_transportplan_track));
        String show_transportplan_date = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.show_transportplan_date));
        String Enable_Tracking_NextPort = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Enable_Tracking_NextPort));
        String date_differ_hide_eta = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.DATE_DIFFER_HIDE_ETA)); 
        String showDataWithNoMovementPlugin = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.SHOW_DATA_WITH_NO_MOVEMENTENTRY));
        HashMap<String, HashMap> trackingcutoff = (HashMap) request.getAttribute("TrackingCutOff");
        Hashtable<String, String> portlist = (Hashtable<String, String>) request.getAttribute("portListObjj");
        String enableLiveTracking = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Enable_Live_Tracking));
        String mapboxAccessToken = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.MAPBOX_ACCESS_TOKEN));

        String json = null;
    %>

    <% if (strUserCode != null) {%>
    <div class="table-responsive remove_print">
        <div class="pull-right m-r-25" id="enablebtn" style="display:none">
                <a href="javascript:void(0)" onclick="makePrint('<%=logo%>');"  data-toggle="tooltip" class="btn btn-info btn-xs" title="Print" data-placement="right"><i class="fa fa-print"></i> <bean:message key="ecom.print" bundle="<%=clientcode%>"/></a> <a href="javascript:void(0)"  data-toggle="modal" data-target="#myModal" class="btn btn-info btn-xs" title="Mail" data-placement="right"><i class="fa fa-envelope-o"></i> <bean:message key="Schd.mail" bundle="<%=clientcode%>"/></a>
            </div>
            <h5 class="text-left"><br /><hr></h5>
        </div>
        <div id="hiddenprintormail" class="d-none remove_print">
            <div class="headdiv">
                <title><%=page_title%></title>
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i%7COpen+Sans:300,300i,400,400i,600,600i,700,700i" rel="stylesheet">
                <link rel="shortcut icon" type="image/x-icon" href="<%=imagepath%>/ico.ico?<%=jsdate%>">
                <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap.min.css">
                <link rel="stylesheet" type="text/css" href="Design/app-assets/css/bootstrap-extended.min.css">
                <link rel="stylesheet" type="text/css" href="Design/app-assets/css/components.min.css">   
                <link rel="stylesheet" type="text/css" href="Design/app-assets/fonts/font-awesome/css/font-awesome.min.css" >
                <link rel="stylesheet" type="text/css" href="Design/assets/css/style.css?<%=jsdate%>">
                <link rel="stylesheet" type="text/css" href="Design/assets/css/ecom.css?<%=jsdate%>">      
                <link rel="stylesheet" type="text/css" href="Design/assets/css/login.css?<%=jsdate%>" >
            </div>
            <div class="bodydiv"></div>
        </div>
        <div id="hiddenmail" class="d-none remove_print">
            <div class="bkg_split" id="bkg_split">
                <table class='port_table'>
                    <thead>
                        <tr>
                            <th><bean:message key='ebooking.origin.display' bundle="<%=clientcode%>"/></th>
                            <th></th>
                            <th></th>
                            <th></th>
                            <th><bean:message key='ebooking.Delivery.display' bundle="<%=clientcode%>"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class='origin'><b></b></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td class='delivery'><b></b></td>
                        </tr>

                    </tbody>
                </table>

                <table class='ref_table'>
                    <thead>
                        <tr>
                            <th><bean:message key="Tracking.booknum" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="ecom.trck.bilno" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="Tracking.eta" bundle="<%=clientcode%>"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><b class='book_no'></b></td>
                            <td><b class='bl_no'></b></td>
                            <td><b class='eta'></b></td>
                        </tr>
                    </tbody>
                </table>
                <table class='track_table'>
                    <thead>
                        <tr>
                            <th><bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="Tracking.latestactive" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="Tracking.activitylocation" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="Tracking.containertype" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="Tracking.Activitydate" bundle="<%=clientcode%>"/></th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>   
        <%}%>



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
            String book_no = null, bl_no = null, eta = null, pol = null, pod = null, enable = "", etd = "", loadport = "", polterminal = "", polarrdate = "", excludeweekend = "", actual_eta = null;
            int trackpercentage = 0;
            TrackingActvityResult trackvo = null;
            boolean hasNoMovementRecords = false;
            HashMap<String, CutoffConfigVO> innercutmap = null;
            CutoffConfigVO cutvo = null;
            String docClosing = null, vgmClosing = null, contClosing = null;
            String finalpol = "", finalpod = "", nextportetd = "", nextPort = "", nextActualPort = "", nextActualEta = "";
            if (trackingdetailmap != null && trackingdetailmap.size() > 0) {
                int index = 0;
                for (Map.Entry<String, List> track : trackingdetailmap.entrySet()) {
                    lstresult = track.getValue();
                    if (lstresult != null && lstresult.size() > 0) {
                        trackvo = lstresult.get(0);

                        if (trackvo != null) {
                            book_no = Validation.checkNull(trackvo.getBookno());
                            bl_no = Validation.checkNull(trackvo.getBlno());
                            loadport = Validation.checkNull(trackvo.getLoadportid());
                            polterminal = Validation.checkNull(trackvo.getPolterminal());
                            polarrdate = trackvo.getPolarrdate();
                            pod = (trackvo.getPod() != null) ? trackvo.getPod() : "";
                            pol = (trackvo.getPol() != null) ? trackvo.getPol() : "";
                            hasNoMovementRecords = trackvo.isHasNoMovementRecords();
                            System.out.println("[ECOM-WAR] TrackingDetails trackvo.getEta()=>" + trackvo.getEta());
                            eta = (trackvo.getEta() != null) ? DateConversion_New.convertETATrackingFormat(trackvo.getEta().trim(), (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT), carrierCode) : "";
                            if (eta.contains("1900")) {
                                eta = "";
                            }
                            actual_eta = eta;
                            if ("Y".equalsIgnoreCase(transportplan_track)) {
                                System.out.println("[ECOM-WAR] TrackingDetails eta==>" + eta);
                                if (!"In Transit".equalsIgnoreCase(eta)) {
                                    eta += trackvo.getDivation();
                                }
                                System.out.println("[ECOM-WAR] TrackingDetails eta=>" + eta);
                            }
                            trackpercentage = DateConversion_New.calculateTrackingPercentage(trackvo.getEtd(), trackvo.getEta(), (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT));
                            docClosing = "";
                            vgmClosing = "";
                            contClosing = "";
                            if ("Y".equalsIgnoreCase(display_Tracking_Cutoff) && trackingcutoff != null && trackingcutoff.size() > 0) {
                                innercutmap = (HashMap<String, CutoffConfigVO>) trackingcutoff.get(loadport);
                                if (innercutmap != null && innercutmap.size() > 0) {
                                    String portpolterminal = loadport + "&&" + polterminal;
                                    cutvo = (CutoffConfigVO) innercutmap.get(portpolterminal);
                                    System.out.println("[ECOM-WAR] TrackingDetails portpolterminal=> " + portpolterminal + " cutvo=> " + cutvo);
                                    if (cutvo == null) {
                                        portpolterminal = loadport + "&&" + "All";
                                        cutvo = (CutoffConfigVO) innercutmap.get(portpolterminal);
                                        System.out.println("[ECOM-WAR] TrackingDetails portpolterminal=> " + portpolterminal + " cutvo=> " + cutvo);
                                    }
                                    if (cutvo == null) {
                                        portpolterminal = "All" + "&&" + "All";
                                        cutvo = (CutoffConfigVO) innercutmap.get(portpolterminal);
                                        System.out.println("[ECOM-WAR] TrackingDetails portpolterminal=> " + portpolterminal + " cutvo=> " + cutvo);
                                    }
                                    System.out.println("[ECOM-WAR] TrackingDetails cutOffVO1-> " + cutvo);
                                    if (cutvo != null && polarrdate != null && !polarrdate.isEmpty()) {
                                        System.out.println("[ECOM-WAR] TrackingDetails java cutvo >>>>>>>>>>>>>>>>>>>>>>>>" + cutvo);
                                        excludeweekend = cutvo.getExcludeweekend();
                                        System.out.println("[ECOM-WAR] TrackingDetails");
                                        docClosing = Validation.checkNull(DateConversion_New.TrackingcutOffCalc(polarrdate, Integer.parseInt(cutvo.getDocClosing()), excludeweekend));
                                        vgmClosing = Validation.checkNull(DateConversion_New.TrackingcutOffCalc(polarrdate, Integer.parseInt(cutvo.getVgmClosing()), excludeweekend));
                                        contClosing = Validation.checkNull(DateConversion_New.TrackingcutOffCalc(polarrdate, Integer.parseInt(cutvo.getFullContClosing()), excludeweekend));
                                    }
                                }
                            }
                            if("Y".equalsIgnoreCase(date_differ_hide_eta) && eta != null && !eta.isEmpty()) {
                              
                                nextportetd = Validation.checkNull(trackvo.getNextPodAt());
                                nextportetd = (nextportetd.length() > 0) ? DateConversion_New.convertTrackingFormat(nextportetd, (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT)) : "";
                                
                                nextActualEta = Validation.checkNull(trackvo.getNextActualEta());
                                nextActualEta = (nextActualEta.length() > 0) ? DateConversion_New.convertTrackingFormat(nextActualEta, (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT)) : "";

                                if (nextportetd != null && !nextportetd.isEmpty()) {
                                    String cleanEta = eta;//.replaceAll("\\(.*?\\)", "").trim();
                                    String cleanNextPortEtd = nextportetd;//.replaceAll("\\(.*?\\)", "").trim();
                                    
                                   SimpleDateFormat sdf = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
                                   // SimpleDateFormat sdf = new SimpleDateFormat(cleanEta.contains(":") ? (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT) : GeneralConf.getStrConfDefDate());
                                    Date etaDate = sdf.parse(cleanEta);
                                    
                                    //sdf = new SimpleDateFormat(cleanNextPortEtd.contains(":") ? (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT) : GeneralConf.getStrConfDefDate());
                                    Date nextPortEtdDate = sdf.parse(cleanNextPortEtd);
                                    
                                    if (nextPortEtdDate.after(etaDate)) {
                                        eta = "";
                                        System.out.println("[ECOM-WAR] TrackingDetails: ETA cleared (nextportetd: " + nextportetd + " is after eta)");
                                    }
                                } else if(nextActualEta != null && !nextActualEta.isEmpty()) {
                                    String cleanEta = eta;//.replaceAll("\\(.*?\\)", "").trim();
                                    String cleanNextPortEtd = nextActualEta;//.replaceAll("\\(.*?\\)", "").trim();
                                    
                                    SimpleDateFormat sdf = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
                                    //SimpleDateFormat sdf = new SimpleDateFormat(cleanEta.contains(":") ? (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT) : GeneralConf.getStrConfDefDate());
                                    Date etaDate = sdf.parse(cleanEta);
                                    
                                    //sdf = new SimpleDateFormat(cleanNextPortEtd.contains(":") ? (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT) : GeneralConf.getStrConfDefDate());
                                    Date nextPortEtdDate = sdf.parse(cleanNextPortEtd);
                                    
                                    if (nextPortEtdDate.after(etaDate)) {
                                        eta = "";
                                        System.out.println("[ECOM-WAR] TrackingDetails: ETA cleared (nextportetd: " + nextportetd + " is after eta)");
                                    }
                                }
                           
                        }
                        System.out.println("[ECOM-WAR] TrackingDetails eta:::::" + eta);
                            if ("Y".equalsIgnoreCase(Enable_Tracking_NextPort)) {
                                finalpol = pol;
                                finalpod = pod;

                                nextportetd = Validation.checkNull(trackvo.getNextPodAt());
                                nextportetd = (nextportetd.length() > 0) ? DateConversion_New.convertTrackingFormat(nextportetd, (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT)) : "";
                                nextPort = Validation.checkNull(trackvo.getNextPod());

                                nextActualPort = Validation.checkNull(trackvo.getNextActualPort());
                                nextActualEta = Validation.checkNull(trackvo.getNextActualEta());
                                nextActualEta = (nextActualEta.length() > 0) ? DateConversion_New.convertTrackingFormat(nextActualEta, (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT)) : "";

                                if (nextPort.length() > 0) {
                                    nextPort = portlist.get(nextPort);
                                }

                                if (nextActualPort.length() > 0) {
                                    nextActualPort = portlist.get(nextActualPort);
                                } 

                                if(nextportetd  == null || nextportetd.isEmpty()) {    
                                    nextportetd = "-";
                                } 
                                if(nextActualEta == null || nextActualEta.isEmpty()) {
                                    nextActualEta = "-";
                                } 
                                if(nextPort == null ||  nextPort.isEmpty()) {
                                    nextPort = "-";
                                }
                                if(nextActualPort == null || nextActualPort.isEmpty()) {
                                    nextActualPort = "-";
                                }
                            } 

                        
        %>

        <div class="card card-body card-shadow page_scrol_data container_track_mail">
            <div class="container">
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
                            <div class="txt_tra_clr"> <bean:message key="Tracking.eta" bundle="<%=clientcode%>"/> </div>
                            <div class="txt_tra_data mail_eta"> <%=eta%> </div>


                        </div>
                    </div>
                </div>
                <div class = "hori_trackig_line"></div> 
                <%if (!"Y".equalsIgnoreCase(transportplan_track) && !"Y".equalsIgnoreCase(Enable_Tracking_NextPort)) {%>
                <div class="form-group">
                    <div class="row">

                        <div class="container">
                            <div class="stepwizard">
                                <div class="stepwizard-row setup-panel">

                                    <div class="stepwizard-step" style=" "> 
                                        <img class="tracker_icon" src="<%=domainname%>/Design/assets/images/page_icons/marker_container.svg" />
                                        <span class="txt_port_name mail_origin"> <%=pol%> </span>

                                        <span class="track_ship_20" style="left: <%=trackpercentage%>%;">  <img class="ship_icon" src="<%=domainname%>/Design/assets/images/page_icons/ship_y.png" /> </span>

                                </div>

                                <div class="stepwizard-step" style="float: right"> 
                                        <img class="tracker_icon" src="<%=domainname%>/Design/assets/images/page_icons/marker_container.svg" />
                                        <span class="txt_port_name_right mail_delivery"> <%=pod%> </span>

                                    </div>

                                </div>
                            </div>

                        </div>

                    </div>

                </div>
                <%}%>
                <%if ("Y".equalsIgnoreCase(transportplan_track)) {%>
                <div class="form-group">
                    <div class="container">
                        <div class="row">
                            <div class="stepwizard">
                                <div class="stepwizard-row">
                                    <div id="setup-panel-<%=index%>" class="row setup-panel">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>              
                </div>
                <%}%>
                <%if ("Y".equalsIgnoreCase(Enable_Tracking_NextPort)) {%>
                <div class="page_scrol_data1">
                    <div class="details_panel" id="row0">
                        <div class="track_panel_content">
                            <div class="track_panel_top">
                                <div class="track_panel_top_1">
                                    <table>
                                        <tbody>
                                            <tr>
                                                <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg"></td>
                                                <td>
                                        <dd><bean:message key="Tracking.origin" bundle="<%=clientcode%>"/> </dd>
                                        </td>
                                        </tr>
                                        <tr>
                                            <td></td>

                                            <td colspan="2" style="padding-left: 0;font-size: 11px;">

                                            <span title="<%=finalpol%>"><%=finalpol%></span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="track_panel_top_2">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg"></td>
                                            <td>
                                    <dd><bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/> </dd>
                                    </td>

                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td colspan="2" style="padding-left: 0;font-size: 11px;">

                                            <span title="<%=finalpod%>"><%=finalpod%></span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        
                        <!-- Actual Transhipment Location Section -->
                        <div style="overflow: auto; padding-top: 15px;">
                            <div class="track_panel_bottom_1">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg"></td>
                                            <td>
                                    <dd><bean:message key="tracking.ActualNextlocation" bundle="<%=clientcode%>"/> </dd>
                                    <dd><span title="<%=nextPort%>"><%=nextActualPort%></span></dd>
                                    </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="track_panel_bottom_2">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><img class="header_icon" src="Design/assets/images/page_icons/calendar.svg"></td>
                                            <td>
                                    <dd><bean:message key="tracking.ActualNextlocationeta" bundle="<%=clientcode%>"/></dd>
                                    <dd><span title="<%=nextportetd%>"><%=nextActualEta%></span></dd>
                                    </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                   
                        <!-- Separator line between Actual and Planned -->
                        <div style="clear: both; height: 2px; background: #ccc; margin: 15px 20px 15px 10px;"></div>
                        
                        <!-- Planned Transhipment Location Section -->
                        <div style="overflow: auto;">
                               <div class="track_panel_bottom_1">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg"></td>
                                            <td>
                                    <dd><bean:message key="tracking.PlannedNextlocation" bundle="<%=clientcode%>"/> </dd>
                                    <dd><span title="<%=nextPort%>"><%=nextPort%></span></dd>
                                    </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="track_panel_bottom_2">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><img class="header_icon" src="Design/assets/images/page_icons/calendar.svg"></td>
                                            <td>
                                    <dd><bean:message key="tracking.PlannedNextlocationeta" bundle="<%=clientcode%>"/></dd>
                                    <dd><span title="<%=nextportetd%>"><%=nextportetd%></span></dd>
                                    </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div> 
                        </div>
                </div>
            </div>
            <%}%>
            <%if ("Y".equalsIgnoreCase(display_Tracking_Cutoff)) {%>

            <div style="border: solid 1px #cccccc;border-radius: 6px;" class="row">

                <div class="col-sm-12">
                    <div class="panel_inner_bottom">

                        <div class="panel_inner_bottom_1" style="width: 18% !important;">
                            <img class="header_icon" src="Design/assets/images/page_icons/clock.svg">
                            <span><strong>Deadline</strong></span>

                        </div><div class="panel_inner_bottom_2" style="width: 27% !important;">
                            <dt>
                            </dt><dd>
                                Container Gate-In</dd>
                            <dd><strong><%=contClosing%></strong></dd>

                        </div>
                        <div class="panel_inner_bottom_3" style="width: 27% !important;">
                            <dt>
                            </dt><dd>SI Document</dd>
                            <dd><strong><%=docClosing%></strong></dd>

                        </div>
                        <div class="panel_inner_bottom_4" style="width: 27% !important;">
                            <dt>
                            </dt><dd>VGM</dd>
                            <dd><strong><%=vgmClosing%></strong></dd>

                        </div>

                    </div>
                </div>     
            </div>
            <%}%>
            <%}%>
            <%
           
            boolean shouldHideTable = ("Y".equalsIgnoreCase(showDataWithNoMovementPlugin) && hasNoMovementRecords);
            if (!shouldHideTable) {
            %>
            <div class="form-group">
                <div class="row">
                    <div class="table-responsive" id="test">
                        <table class="table table-borderless table_even_odd trackingtable" id="datatablebytrack">
                            <thead>
                                <tr>
                                    <th><bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/></th>
                                    <th><bean:message key="Tracking.latestactive" bundle="<%=clientcode%>"/></th>
                                    <th><bean:message key="Tracking.activitylocation" bundle="<%=clientcode%>"/></th>
                                    <th><bean:message key="Tracking.containertype" bundle="<%=clientcode%>"/></th>
                                    <th><bean:message key="Tracking.Activitydate" bundle="<%=clientcode%>"/></th>
                                    <th><bean:message key="Tracking.action" bundle="<%=clientcode%>"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    String containernum = "", containernumlbl = "", latestactivity = "", activitylocation = "";
                                    String containertype = "", activitydate = "";
                                    String strTableConId = "";
                                    String valid = "";
                                    //    List<TrackingActvityResult> lstresult
                                    int i = 0;
                                    if (lstresult != null) {
                                        for (i = 0; i < lstresult.size(); i++) {
                                            trackvo = lstresult.get(i);
                                            if (trackvo != null) {
                                                // activitylocation = (trackvo.getActivitylocation() != null) ? trackvo.getActivitylocation() : "";
                                                containernum = Validation.checkNull(trackvo.getContainerno());
                                                latestactivity = Validation.checkNull(trackvo.getLatestactivity());
                                                if (MaskPortUtil.isActualPort(Validation.checkNull(trackvo.getActivityloccode()))) {
                                                    activitylocation = MaskPortUtil.getDisplayPortName(Validation.checkNull(trackvo.getActivityloccode()),Validation.checkNull(trackvo.getActivitylocation()));
                                                } else {
                                                    activitylocation = Validation.checkNull(trackvo.getActivitylocation());
                                                }
                                                containertype = Validation.checkNull(trackvo.getContainertype());
                                                // activitydate=movVo.getStrActivityDate();
                                                activitydate = (trackvo.getActivitydate() != null) ? DateConversion_New.convertTrackingFormat(trackvo.getActivitydate().trim(), (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT)) : "";

                                                strTableConId = containernum + i;
                                                valid = (trackvo.getResult() != null) ? trackvo.getResult().trim() : "";

                                                containernumlbl = containernum;
                                                if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                                                    containernumlbl = "Container" + (i + 1);
                                                }
                                %>
                                <%if (valid == null || valid.length() == 0) {
                                        enable = "yes";
                                %> 
                                <tr data-contno="<%=containernum%>">
                                    <td ><%=containernumlbl%></td>
                                    <td ><%=latestactivity%></td>
                                    <td><%=activitylocation%></td>
                                    <td ><%=containertype%></td>
                                    <td ><%=activitydate%></td>
                                    <td id="moredtailstdid<%=i%>" > 
                                        <div class="table_tra_data"> <a class="various" href="javascript:void(0)" href="#" onclick="ContainerInfoNew('<%=containernum%>', '<%=book_no%>', '<%=strTableConId%>', 'moredtailstdid<%=i%>', 'moredtailsid<%=i%>')" id="moredtailsid<%=i%>"> <button  type="button" class="btn view_details ">  <bean:message key="trck.but.viewdet" bundle="<%=clientcode%>"/> </button>  </a>
                                        <%if ("Y".equalsIgnoreCase(enableLiveTracking)) {%>
                                            <a href="javascript:void(0)" onclick="LiveMap.openContainerMap('<%=containernum%>', '<%=mapboxAccessToken%>');" title="Track on Live Map" style="margin-left:5px;"><button type="button" class="btn btn-success btn-xs livemap-row-btn" data-toggle="tooltip" data-placement="top" title="Track on Live Map"><i class="fa fa-map-marker"></i>&nbsp;Map</button></a>
                                        <%}%>
                                        </div> 
                                    </td>
                                </tr>
                                <% }
                                            }
                                        }
                                    }%>

                                <%
                                    String containernummail = "", containernumlblmail = "", latestactivitymail = "", activitylocationmail = "";
                                    String containertypemail = "", activitydatemail = "";
                                    String strTableConIdmail = "";
                                    String validmail = "";
                                    Set<Map.Entry<String, List>> entrySet = null;
                                    if ("Y".equalsIgnoreCase(showfullHistoryinMail)) {
                                        entrySet = trackingdetailFullHistorymap.entrySet();
                                    } else {
                                        entrySet = trackingdetailmap.entrySet();
                                    }
                                    for (Map.Entry<String, List> trackformail : entrySet) {
                                        lstresult = trackformail.getValue();

                                        if (!"Y".equalsIgnoreCase(showfullHistoryinMail)) {
                                            trackvo = lstresult.get(0);
                                        }
                                        //    List<TrackingActvityResult> lstresult
                                        int j = 0;
                                        if (lstresult != null) {
                                            for (j = 0; j < lstresult.size(); j++) {
                                                trackvo = lstresult.get(j);
                                                if (trackvo != null) {
                                                    // activitylocation = (trackvo.getActivitylocation() != null) ? trackvo.getActivitylocation() : "";
                                                    containernummail = Validation.checkNull(trackvo.getContainerno());
                                                    latestactivitymail = Validation.checkNull(trackvo.getLatestactivity());
                                                    if (MaskPortUtil.isActualPort(Validation.checkNull(trackvo.getActivityloccode()))) {
                                                        activitylocationmail = MaskPortUtil.getDisplayPortName(Validation.checkNull(trackvo.getActivityloccode()),Validation.checkNull(trackvo.getActivitylocation()));
                                                    } else {
                                                        activitylocationmail = Validation.checkNull(trackvo.getActivitylocation());
                                                    }
                                                    containertypemail = Validation.checkNull(trackvo.getContainertype());
                                                    // activitydate=movVo.getStrActivityDate();
                                                    activitydatemail = (trackvo.getActivitydate() != null) ? DateConversion_New.convertTrackingFormat(trackvo.getActivitydate().trim(), (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT)) : "";

                                                    strTableConId = containernummail + j;
                                                    validmail = (trackvo.getResult() != null) ? trackvo.getResult().trim() : "";

                                                    containernumlblmail = containernummail;
                                                    if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                                                        containernumlblmail = "Container" + (j + 1);
                                                    }
                                %>
                                <%if (validmail == null || validmail.length() == 0) {
                                        enable = "yes";
                                %>
                                <tr class="mailcontent" style="display:none">
                                        <td class="mail_contno"><%=containernumlblmail%></td>
                                        <td class="mail_latestact"><%=latestactivitymail%></td>
                                        <td class="mail_actloc"><%=activitylocationmail%></td>
                                        <td class="mail_conttype"><%=containertypemail%></td>
                                        <td class="mail_actdate"><%=activitydatemail%></td>
                                    </tr>
                                    <% }
                                                    }
                                                }
                                            }
                                        }%>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
                <% } %>
            </div>
        </div>
        

        <%
                        index++;
                    }
                }
            }
        %>
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

    <script type="text/javascript" >

        function calculateColumns(argument) {
            console.log("argument=>" + argument);
            var colClass;
            switch (argument) {
                case 1:
                    colClass = "col-sm-6";
                    break;
                case 2:
                    colClass = "col-sm-4";
                    break;
                case 3:
                    colClass = "col-sm-3";
                    break;
                case 4:
                    colClass = "larger-column";
                    break;
                case 5:
                    colClass = "col-sm-2";
                    break;
                case 6:
                    colClass = "larger-column";
                    break;
                case 7:
                    colClass = "small-column";
                    break;
            }
            console.log('colClass=====>' + colClass);
            return colClass;
        }
        function transplan(argument) {
            console.log("argument=>" + argument);
            var colClass;
            switch (argument) {
                case "R":
                    colClass = "/Design/assets/images/page_icons/rail_track.png";
                    break;
                case "T":
                    colClass = "/Design/assets/images/page_icons/truck_track.png";
                    break;
                case "B":
                    colClass = "/Design/assets/images/page_icons/Barge_track.png";
                    break;
                case "C":
                    colClass = "/Design/assets/images/page_icons/Combined_track.png";
                    break;
                default:
                    colClass = "/Design/assets/images/page_icons/ship_y.png";
            }
            console.log('colClass=====>' + colClass);
            return colClass;
        }
        // JavaScript for adding a new step dynamically
        function addPort(location, type, col, path, last, first, listlen, index, mouseedate, position, divId) {
            console.log("index==>" + index + "type=>" + type + "listlen=>" + listlen + "last=>" + last + "col=>" + col + "location=>" + location + "first=>" + first + "mouseedate" + mouseedate);
            // Create a new stepwizard-step element
            var newStep = document.createElement("div");
            newStep.className = col + " stepwizard-step";
            if ("P" == position) {
                newStep.style.textAlign = "center";
            }
            if ("R" == position) {
                newStep.style.textAlign = "right";
            }
            if ("L" == position) {
                newStep.style.textAlign = "left";
            }
            if ("3" == listlen && "2" == index && "N" == last) {
                newStep.style.left = "47px";
            }
            if ("5" == listlen && "4" == index && "N" == last) {
                newStep.style.left = "15px";
            }
            if ("6" == listlen && "5" == index && "N" == last) {
                newStep.style.left = "9px";
            }
            if ("5" == listlen && "2" == index && "P" == position) {
                newStep.style.left = "50px";
            }
            if ("4" == listlen && "3" == index && "N" == last) {
                newStep.style.left = "25px";
            }
            if ("4" == listlen && "Y" == first) {
                newStep.style.left = "25px";
            }
            if ("4" == listlen && "P" == position) {
                newStep.style.left = "35px";
            }
            if ("6" == listlen && "Y" == first) {
                newStep.style.left = "33px";
            }

            // Create the location span
            var locationSpan = document.createElement("span");
            locationSpan.className = "txt_port_name";
            if ("Y" == first || "Y" === last) {
                locationSpan.title = location;
                $(locationSpan).tooltip();
                let result = location.length > 7 ? location.substring(0, 5) + ".." : location;
                locationSpan.textContent = result;
            } else {
                locationSpan.title = location;
                $(locationSpan).tooltip();
                let result = location.length > 10 ? location.substring(0, 10) + ".." : location;
                locationSpan.textContent = result;
                if ("4" == listlen && "3" == index && "N" == last) {
                    locationSpan.style.right = "-15px";
                }
            }
            newStep.appendChild(locationSpan);

            // Create the tracker icon
            var trackerIcon = document.createElement("img");
            trackerIcon.className = "tracker_icon";
            trackerIcon.src = "<%=domainname%>/Design/assets/images/page_icons/marker_container.svg";
        <%if ("Y".equalsIgnoreCase(show_transportplan_date)) {%>
            $(trackerIcon).attr('data-original-title', mouseedate).tooltip({
                html: true
            });
        <%}%>
            newStep.appendChild(trackerIcon);

            // Create the tracker icon
            if ("Y" != last) {
                var transIcon = document.createElement("img");
                transIcon.className = "ship_icon";
                transIcon.src = "<%=domainname%>" + path + "";
                transIcon.style.position = "relative";
                transIcon.style.float = "right";
                if (1 == listlen || (0 === index && "N" === first)) {
                    transIcon.style.float = "right";
                }
                if ("T" == type) {
                    transIcon.style.top = "-28px";
                }
                if ("R" == type) {
                    transIcon.style.top = "-41px";
                }
                if ("C" == type) {
                    transIcon.style.top = "-41px";
                }
                if ("B" == type) {
                    transIcon.style.top = "-55px";
                }
                if ("B" != type && "C" != type && "R" != type && "T" != type) {
                    transIcon.style.top = "-15px";
                }
                if ("2" == listlen && "P" == position) {
                    transIcon.style.left = "135px";
                }
                if ("3" == listlen && "Y" == first) {
                    transIcon.style.right = "95px";
                }
                if ("3" == listlen && "2" == index && "N" == last) {
                    transIcon.style.left = "165px";
                }
                if ("3" == listlen && "0" == index && "N" == first) {
                    transIcon.style.left = "50px";
                }
                if ("4" == listlen && "Y" == first) {
                    transIcon.style.right = "113px";
                }
                if ("4" == listlen && "3" == index && "N" === last) {
                    transIcon.style.left = "110px";
                }
                if ("4" == listlen && "P" == position) {
                    transIcon.style.left = "85px";
                }
                if ("5" == listlen && "Y" == first) {
                    transIcon.style.right = "45px";
                }
                if ("5" == listlen && "4" == index && "N" == last) {
                    transIcon.style.left = "139px";
                }
                if ("4" == listlen && "0" == index && "N" == first) {
                    transIcon.style.right = "27px";
                }
                if ("5" == listlen && "P" == position) {
                    transIcon.style.left = "47px";
                }
                if ("6" == listlen && "5" == index && "N" == last) {
                    transIcon.style.left = "125px";
                }
                if ("6" == listlen && "Y" == first) {
                    transIcon.style.right = "72px";
                }
                if ("6" == listlen && "P" == position) {
                    transIcon.style.left = "15px";
                }
                if ("6" == listlen && "0" == index && "N" === first) {
                    transIcon.style.right = "28px";
                }

                newStep.appendChild(transIcon);
            }

            var setupPanel = document.getElementById(divId); // Get div by unique ID
            //var setupPanel = document.querySelector(".setup-panel");
            if (setupPanel) {
                setupPanel.appendChild(newStep);
            }
        }
        function formatDate(dateString) {
            var options = {year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', hour12: true};
            var date = new Date(dateString);
            return date.toLocaleDateString('en-GB', options).replace(',', '');
        }
        function transportplanlist(lst, aulpod, divId) {
            let list;
            try {
                list = JSON.parse(lst);
                console.log("list=======>" + list);
            } catch (error) {
                console.error("Error parsing JSON:", error);
                return; // Exit function if JSON parsing fails
            }

            if (!list || list.length === 0) {
                console.log("List is empty or null.");
                return; // Exit function if list is empty or null
            }
            var trans = "/Design/assets/images/page_icons/ship_y.png";
            let lastindex = list.length - 1;
            let listlen = list.length;
            var col = calculateColumns(list.length);

            const planpod = list[lastindex].pod;
            console.log('aulpod' + aulpod + 'planpod' + planpod);
            //  if (aulpod.includes(planpod)) {

            if (list.length > 1) {
                var mousedate = "";
                list.forEach(function (rMap, i) {
                    var firstport = "N";
                    var lastport = "N";
                    var nextPol = (i + 1 < list.length) ? list[i + 1].pol : "";
                    var nextetd = (i + 1 < list.length) ? list[i + 1].etd : "";
                    var nextlegType = (i + 1 < list.length) ? list[i + 1].legType : "";
                    var prePod = (i - 1 >= 0) ? list[i - 1].pod : "";
                    var preeta = (i - 1 >= 0) ? list[i - 1].eta : "";
                    var trans = transplan(rMap.legType);
                    if (i === 0) {
                        console.log("rMap" + rMap.pod + rMap.pol + rMap.legType + rMap.eta + rMap.etd);
                        if (rMap.pol != nextPol && i + 1 != lastindex) {
                            mousedate = "ETD: " + formatDate(rMap.etd);
                            addPort(rMap.pol, rMap.legType, col, trans, "N", "Y", listlen, i, mousedate, "L", divId);
                            mousedate = "ETA: " + formatDate(rMap.eta) + " <br>ETD: " + formatDate(nextetd);
                            trans = transplan(nextlegType);
                            addPort(nextPol, nextlegType, col, trans, "N", "N", listlen, i, mousedate, "L", divId);
                            firstport = "Y";
                        } else if ("N" == firstport) {
                            mousedate = "ETD: " + formatDate(rMap.etd);
                            trans = transplan(rMap.legType);
                            addPort(rMap.pol, rMap.legType, col, trans, "N", "Y", listlen, i, mousedate, "L", divId);
                        }
                    } else {
                        if (rMap.pod == nextPol && i + 1 < lastindex) {
                            mousedate = "ETA: " + formatDate(rMap.eta) + "<br>ETD: " + formatDate(nextetd);
                            trans = transplan(nextlegType);
                            addPort(nextPol, nextlegType, col, trans, "N", "N", listlen, i, mousedate, "P", divId);
                            transport = "Y";
                        } else if (2 == list.length) {
                            mousedate = "ETA: " + formatDate(preeta) + "<br>ETD: " + formatDate(rMap.etd);
                            trans = transplan(nextlegType);
                            addPort(rMap.pol, rMap.legType, col, trans, "N", "N", listlen, i, mousedate, "P", divId);
                        }
                    }
                    if (i === lastindex) {
                        if (rMap.pod != prePod && i - 1 != 0) {
                            mousedate = "ETA: " + formatDate(preeta) + "<br>ETD: " + formatDate(rMap.etd);
                            trans = transplan(rMap.legType);
                            addPort(prePod, rMap.legType, col, trans, "N", "N", listlen, i, mousedate, "R", divId);
                            mousedate = "ETA: " + formatDate(rMap.eta);
                            trans = transplan(rMap.legType);
                            addPort(rMap.pod, rMap.legType, col, trans, "Y", "N", listlen, i, mousedate, "R", divId);
                            lastport = "Y";
                        } else if ("N" == lastport) {
                            mousedate = "ETA: " + formatDate(rMap.eta);
                            trans = transplan(rMap.legType);
                            addPort(rMap.pod, rMap.legType, col, trans, "Y", "N", listlen, i, mousedate, "R", divId);
                        }
                    }
                });
            } else {
                var mouseeedate = "";
                list.forEach(function (rMap) {
                    console.log("legType: " + rMap.legType + ", pol: " + rMap.pol + ", pod " + rMap.pod + "col=>" + col);
                    var trans = transplan(rMap.legType);
                    mouseeedate = formatDate(rMap.etd);
                    addPort(rMap.pol, rMap.legType, col, trans, "N", "Y", listlen, "1", mouseeedate, "L", divId);
                    mouseeedate = formatDate(rMap.eta);
                    addPort(rMap.pod, rMap.legType, col, trans, "Y", "N", listlen, "1", mouseeedate, "R", divId);
                });
            }
            /* } else {
             addPort('<=pol%>', "", "col-sm-6", trans, "N", "Y", "1", "1", "", "L", divId);
             addPort('<=pod%>', "", "col-sm-6", trans, "Y", "N", "1", "1", "", "R", divId);
             } */ //for Jira TSHG-1633 issue --Fixed by preethi
        }

        <% if ("Y".equalsIgnoreCase(transportplan_track)) {
                int index = 0;
                List<TrackingActvityResult> result = null;
                TrackingActvityResult vo = null;
                String aulpod = "";
                for (Map.Entry<String, List> track : trackingdetailmap.entrySet()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    result = track.getValue();
                    if (result != null && result.size() > 0) {
                        vo = result.get(0);
                        if (vo != null) {
                            aulpod = (vo.getPod() != null) ? vo.getPod() : "";
                        }
                    }
                    List<TrackingProtActivityResult> lst = (List<TrackingProtActivityResult>) tracklst.get(track.getKey());
                    json = objectMapper.writeValueAsString(lst);
                    System.out.println("[ECOM-WAR] TrackingDetails tracklst==>" + track.getKey() + "  tracklst json ==>" + json);
        %>
        transportplanlist('<%=json%>', '<%=aulpod%>', 'setup-panel-<%=index%>');
        <%
                    index++;
                }
            }%>
    </script>

    <% if (strUserCode != null) {%>
    <script type="text/javascript">
        var enable = "<%=enable%>";
        if (enable == "")
        {
            document.getElementById("enablebtn").style.display = "none";
        } else
        {
            document.getElementById("enablebtn").style.display = "block";
        }
    </script>
    <%}%>

    <div class="modal fade remove_print" id="myModal" role="dialog">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-info text-white">  <h5 class="modal-title">  <bean:message key="ecom.mail" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                        <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    </div>
                </div>
                <div class="modal-body">
                    <div class="form-group row">
                        <label class="col-sm-12"><bean:message key="tracking.msg.entmail" bundle="<%=clientcode%>"/> :  <span class="label label-info"><bean:message key="tracking.msg.commasemicolonmultipleemail" bundle="<%=clientcode%>"/></span></label>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-offset-0 col-sm-11">
                            <textarea class="form-control" id="email_text" name="email_text" rows="3" cols="50" style="resize:none"></textarea> <input name="email_value"  id="email_value" type="hidden" size="55"/>
                                <label class="error" id="mailreq" style="display:none"><bean:message key="tracking.msg.mailreq" bundle="<%=clientcode%>"/></label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="pull-right">
                            <a href="javascript:void(0)" title="Mail"  class="btn btn-info btn-xs" onclick="sendMail();" id="mailbtn"> <i class="fa fa-envelope-o"></i> <bean:message key="ecom.mail" bundle="<%=clientcode%>"/></a>
                            <a href="javascript:void(0)"  data-dismiss="modal" class="btn btn-info btn-xs" title="Close" data-placement="right" onclick="closeMail();"><i class="fa fa-times-circle"></i> <bean:message key="ecom.close" bundle="<%=clientcode%>"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript" src="Design/app-assets/js/scroll-custom.js"></script>

        <script type="text/javascript">
                                $(document).ready(function () {
                                    var searchid = "<%=searchnumbers%>";
                                    document.getElementById("searchnumbers").value = searchid;
                                });

        </script>



        <!--<script type="text/javascript">
            $(function () {
                $(".modal-draggable .modal-dialog").draggable({
                    handle: ".panel-heading"
                });
            });
        </script>-->
        <style>
            .panel_inner_bottom_4 dd {
            color: unset !important;
            text-transform: unset !important;
            margin-top: unset !important;
            font-weight: unset !important;
        }
        .panel_inner_bottom_4 {
            text-align: left !important;
        }

        .track_panel_bottom_line {
    clear: both;
    height: 1px;
    background-color: #ddd;
    margin: 15px 0;
    width: 100%;
    display: block;
}
        /*   .modal-draggable .panel-heading:hover {
               cursor: move;
           }
           p
           {
               margin: 0 0 0px !important;
           }*/
    </style>

