<%@ page contentType="text/html;" language="java" import="net.ecom.common.utils.DateConversion_New,net.ecom.common.Validation,java.util.Hashtable,java.util.List,java.util.ArrayList,net.ecom.vo.TrackingVo,net.com.ecom.ws.vo.MovementDetailsList,net.com.ecom.ws.vo.MovementDetailsVo,net.ecom.tracking.vo.TrackingActvityResult,net.ecom.common.GeneralConf,net.ecom.common.MaskPort,net.ecom.common.utils.MaskPortUtil" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@page pageEncoding="UTF-8"%>

<%
    String strUserCode = (String) session.getAttribute("SESSION_USERCODE");
    String clientcode = (String) session.getAttribute("clientcode");
%>


<div class="timeline-small">
    <div class="media-header"> <bean:message key="ecom.trck.mvmtdet" bundle="<%=clientcode%>"/> </div>

    <div class="row m-b-25">

        <div class="col-sm-2"> </div>            

        <div class="col-sm-7">
            <label class="fancy_label"> <bean:message key="ecom.trck.mvmttype" bundle="<%=clientcode%>"/></label>

        </div>

        <div class="col-sm-3">
            <label class="fancy_label_date"> <bean:message key="ecom.trck.activitydate" bundle="<%=clientcode%>"/> </label>
        </div>

    </div>
    <%
        Hashtable vessellist = (Hashtable) session.getServletContext().getAttribute("SESSION_VESSELLIST");
        Hashtable serlist = (Hashtable) session.getServletContext().getAttribute("SESSION_SERVICELIST");
        Hashtable portlist = (Hashtable) session.getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PORTLIST");
        String hideTracVessldet = (String) request.getServletContext().getAttribute(GeneralConf.HIDE_TRACKING_VESSEL_DETAILS);
        String Movement = "", activitydate = "", activitylocation = "", service = "", vessel = "";
        String voyage = "", bound = "", vesselname = "", activeloc = "", servicename = "";

        List<TrackingActvityResult> lstresult = (List) session.getAttribute("ContMovemenet");
        TrackingActvityResult trackvo = null;
        int i = 0;
        if (lstresult != null) {
            for (i = 0; i < lstresult.size(); i++) {
                trackvo = new TrackingActvityResult();
                trackvo = lstresult.get(i);

                Movement = Validation.checkNull(trackvo.getMoverment());
                activitydate = (trackvo.getActivitydate() != null) ? DateConversion_New.convertTrackingFormat(trackvo.getActivitydate().trim(), (String) getServletContext().getAttribute(GeneralConf.TRACKINGDATEFORMAT)) : "";
                boolean isMaskPort = MaskPortUtil.isActualPort(trackvo.getActivityloccode());
//              System.out.println("[ECOM-WAR] TrackingMovement isMaskPort---> " + isMaskPort);
//             System.out.println("[ECOM-WAR] TrackingMovement activityloccode---> " + trackvo.getActivityloccode());

                if (!isMaskPort) {
                    activitylocation = Validation.checkNull(trackvo.getActivitylocation());
                    service = Validation.checkNull(trackvo.getService());
                    vessel = Validation.checkNull(trackvo.getVessel());
                    if (vessellist.containsKey(vessel)) {
                        //vesselname = vessel + "-" + (String) vessellist.get(vessel);
                        vesselname = (String) vessellist.get(vessel);
                    } else {
                        vesselname = vessel;
                    }

                    if (portlist.containsKey(activitylocation)) {
                        activeloc = activitylocation + "-" + (String) portlist.get(activitylocation);
                    } else {
                        activeloc = activitylocation;
                    }

                    if (serlist.containsKey(service)) {
                        servicename = service + "-" + (String) serlist.get(service);
                    } else {
                        servicename = service;
                    }
                    voyage = Validation.checkNull(trackvo.getVoyage());
                    bound = Validation.checkNull(trackvo.getBound());
                } else {
                    String maskportactlocode=Validation.checkNull(trackvo.getActivityloccode());
                    activitylocation = MaskPortUtil.getDisplayPortName(maskportactlocode, Validation.checkNull(trackvo.getActivitylocation()));
                    service = "";
                    vessel = "";
                    vesselname = "";
                    activeloc = MaskPortUtil.swapactualporttomaskport(maskportactlocode)+"-"+ MaskPortUtil.getDisplayPortName(maskportactlocode, Validation.checkNull(trackvo.getActivitylocation()));
//                    System.out.println("[ECOM-WAR] TrackingMovement activeloc---> " + activeloc);
                    servicename = "";
                    voyage = "";
                    bound = "";
                }
    %>
    <div class="media">
        <div class="timeline-round m-r-30 timeline-line-1 bg-primary"> <img class="inside_icon" src="Design/assets/images/page_icons/marker.svg" /> </div>

        <div class="media-body">

            <div class="row">

                <div class="col-sm-8">

                    <span class="movement_title">  <%=Movement%>  </span>

                </div> 

                <div class="col-sm-4 ">

                    <span class="date_track"> <%=activitydate%> </span> 
                </div>

            </div>

            <div class="row">

                <div class="col-sm-12 m-t-10">
                    <label> <bean:message key="ecom.trck.actloc" bundle="<%=clientcode%>"/> </label>
                    <span> <%=activeloc%>  </span> 

                </div>

            </div>
            <%if (!"Y".equalsIgnoreCase(hideTracVessldet)) {%>
            <%if (vesselname != null && vesselname.length() > 0 && service != null && service.length() > 0 && voyage != null && voyage.length() > 0 && bound != null && bound.length() > 0) {%>
            <div class="row">
                <div class="col-sm-12 m-t-10">
                    <label> <bean:message key="trck.head.vesdet" bundle="<%=clientcode%>"/> : </label>
                    <span title="Service / Vessel / Voyage / Bound"> <%=service + " / " + vesselname + " / " + voyage + " / " + bound%>  </span> 
                </div>
            </div>
            <%}%>
            <%}%>
        </div>
    </div>
    <% }
        }%>
</div>
