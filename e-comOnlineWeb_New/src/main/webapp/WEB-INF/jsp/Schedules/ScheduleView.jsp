<%@page language="java" contentType="text/html; charset=UTF-8" import="net.ecom.common.utils.DateConversion_New,net.ecom.common.Validation,net.ecom.vds.serviceVo.ScheduleCommonVO,net.com.ecom.ws.vo.ScheduleBlwLocationList,net.com.ecom.ws.vo.ScheduleBlwLocationVo,java.util.ArrayList,java.util.List,java.util.HashMap,java.util.Hashtable,net.ecom.common.GeneralConf,net.ecom.common.utils.MaskPortUtil" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%
    String resData = (String) request.getAttribute("SCHDSEARCH");
    String userCode = (String) session.getAttribute("SESSION_USERCODE");
    String user = (String) session.getAttribute("sessionAdmin");
    String clientcode = (String) session.getAttribute("clientcode");
    String client_code = (String) getServletContext().getAttribute("client_code");
    Hashtable servicelist = (Hashtable) session.getServletContext().getAttribute("SESSION_SERVICELIST");
    Hashtable terminallist = (Hashtable) session.getServletContext().getAttribute("SESSION_TERMINALLIST");
    String feederRestriction = (String) request.getServletContext().getAttribute(GeneralConf.FEEDER_RESTRICTION);
    String userfuritcust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
    HashMap vslrest = (HashMap) session.getServletContext().getAttribute("VesselRestrictionDetails");
    String Display_LCT_CUTOFF = (String) request.getServletContext().getAttribute(GeneralConf.Display_LCT_CUTOFF);
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String schedulecaptcha = (String) getServletContext().getAttribute(GeneralConf.ENABLE_MASS_SUBMISSION_CAPTCHA);
    String enableScheduleDisclaimer = (String) request.getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_DISCLAIMER);
    String scheduleDisclaimerMessage = (String) request.getServletContext().getAttribute("ScheduleDisclaimerMessage");
    session.removeAttribute("ScheduleMailCount");
%>

<form name="theForm" method="post" id="theForm" action="ActionExcel">
    <input type="hidden" id="strtype" name="strtype"/>
    <input type="hidden" id="scheduletype" name="scheduletype" value="<%=resData%>" />
    <input type="hidden" id="Captcha_Site_Key" value="<%=siteKey%>"/>
    <%
        if (resData != null && (resData.equalsIgnoreCase("SchdByPort"))) {
            String atport = (String) request.getAttribute("atport");
            String fromdate = (String) request.getAttribute("fromdate");
            String todate = (String) request.getAttribute("todate");
            String vessel = "";
            String vesselname = "";
            String voyage = "";
            String service = "";
            String Bound = "", Boundname = "";
            String port = "";
            String portname = "";
            String Terminal = "";
            String terminalname = "";
            String arrdate = "";
            String depdate = "";
            String type = "";
            String status = "";
            String addhoc = "";
            String LCT = "";
            ArrayList portmap = new ArrayList();
            List<ScheduleCommonVO> scheduleportDetails = (List<ScheduleCommonVO>) request.getAttribute("SCHD");

            if (userCode != null && scheduleportDetails != null && scheduleportDetails.size() > 0) {%>
    <div class="table-responsive">
        <div class="pull-right m-r-20"> <a href="javascript:Download('Port');"  data-toggle="tooltip" class="btn btn-info btn-sm" title="Export" data-placement="right"><i class="fa fa-download"></i><bean:message key="Schd.export" bundle="<%=clientcode%>"/></a> <a href="javascript:generatetoken()"  onclick="generatetoken()" data-toggle="modal" data-target="#myModal" class="btn btn-info btn-sm" title="Mail" data-placement="right"><i class="fa fa-envelope-o"></i> <bean:message key="Schd.mail" bundle="<%=clientcode%>"/></a> </div>
        <h5 class="text-left"><br />
            <hr>
        </h5>
    </div>
    <%}%>
    <div class="col-lg-12">
        <span class="lbl_clr_txt">
            <%if ("Y".equalsIgnoreCase(enableScheduleDisclaimer) && scheduleDisclaimerMessage != null && !scheduleDisclaimerMessage.isEmpty()) {%>
            <%=scheduleDisclaimerMessage%>
            <%} else {%>
            <bean:message key="ecom.msg.schddate" bundle="<%=clientcode%>"/>
            <%}%>
        </span>
    </div>
    <div class="table-responsive card card-body card-shadow">
        <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
        <table class="table table-striped table-bordered"  id="datatablebyport">
            <thead>
                <tr>
                    <th><bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.PortName" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.TerminalName" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.ETA" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.ETD" bundle="<%=clientcode%>"/></th>
                        <%if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {%>
                    <th><bean:message key="Schedule.View.LCT" bundle="<%=clientcode%>"/></th>
                        <%}%>
                </tr>
            </thead>
            <tbody>
                <%
                    List<ScheduleCommonVO> summaryList = null;
                    ScheduleCommonVO schdPortVo = null;
                    if (scheduleportDetails != null && scheduleportDetails.size() > 0) {
                        int i = 0;
                        summaryList = scheduleportDetails;
                        int size = summaryList.size();
                        for (i = 0; i < size; i++) {
                            schdPortVo = (ScheduleCommonVO) summaryList.get(i);
                            ArrayList arryport = new ArrayList();
                            vessel = schdPortVo.getVessel();
                            voyage = schdPortVo.getVoyage();
                            vesselname = schdPortVo.getVesselname();
                            service = schdPortVo.getService();
                            if ("N".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-success'> North</span>";
                                Boundname = "North";
                            } else if ("S".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-info'> South</span>";
                                Boundname = "South";
                            } else if ("W".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-west'> West</span>";
                                Boundname = "West";
                            } else if ("E".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-east'> East</span>";
                                Boundname = "East";
                            }
                            //Bound = schdPortVo.getBound();
                            port = schdPortVo.getPort();
                            portname = schdPortVo.getPortname();
                            Terminal = schdPortVo.getTerminal();
                            terminalname = schdPortVo.getTerminalname();
                            arrdate = (schdPortVo.getArrdate() != null) ? DateConversion_New.convertDefaultTimeFormat(schdPortVo.getArrdate().trim()) : "";
                            depdate = (schdPortVo.getDepdate() != null) ? DateConversion_New.convertDefaultTimeFormat(schdPortVo.getDepdate().trim()) : "";
                            type = schdPortVo.getType();
                            status = schdPortVo.getStatus();
                            addhoc = schdPortVo.getAddhoc();
                            LCT = Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(schdPortVo.getClosingtime()));
                            if ("FEEDER".equalsIgnoreCase(type) && (vesselname == null || vesselname.toLowerCase().contains("dummy"))) {
                                vesselname = "FEEDER";
                            }
                            if (!("FEEDER".equalsIgnoreCase(type) && "Y".equalsIgnoreCase(feederRestriction) && (!"Y".equalsIgnoreCase(userfuritcust)))) {
                                arryport.add(service);
                                arryport.add(vesselname);
                                arryport.add(voyage);
                                arryport.add(Boundname);
                                arryport.add(portname);
                                arryport.add(terminalname);
                                arryport.add(arrdate);
                                arryport.add(depdate);
                                if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                                    arryport.add(LCT);
                                }
                                portmap.add(arryport);
                %>
                <tr>
                    <td data-title=<bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/>><%=service%><input type="hidden" name="hiddenport" id="hiddenport" value="<%=atport%>" /></td>
                    <td data-title=<bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/>><%=vesselname%><input type="hidden" name="hiddenfromdate" id="hiddenfromdate" value="<%=fromdate%>" /></td>
                    <td data-title=<bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/>><%=voyage%><input type="hidden" name="hiddentodate" id="hiddentodate" value="<%=todate%>" /></td>
                    <td data-title=<bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/>><%=Bound%></td>
                    <td data-title=<bean:message key="Schedule.View.PortName" bundle="<%=clientcode%>"/>><%=portname%></td>
                    <td data-title=<bean:message key="Schedule.View.TerminalName" bundle="<%=clientcode%>"/>><%=terminalname%></td>
                    <td data-title=<bean:message key="Schedule.View.ETA" bundle="<%=clientcode%>"/>><%=arrdate%></td>
                    <td data-title=<bean:message key="Schedule.View.ETD" bundle="<%=clientcode%>"/>><%=depdate%></td>
                    <%if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {%>
                    <td data-title=<bean:message key="Schedule.View.LCT" bundle="<%=clientcode%>"/>><%=LCT%></td>
                    <%}%>
                </tr>
                <%}
                        }
                        session.setAttribute("PortList", portmap);
                        session.setAttribute("atport", atport);
                        session.setAttribute("fromdate", fromdate);
                        session.setAttribute("todate", todate);

                    }
                %>
            </tbody>
        </table>
    </div>
    <div class="col-lg-12 " style="display:none">

        <div class="table-responsive txt_datatable_top">

            <table id="datatablebyport1" class="table table-striped table-bordered table-responsive-xl"  style="width:100%" >
                <thead>
                    <tr>
                        <th><bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.PortName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.TerminalName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.ETA" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.ETD" bundle="<%=clientcode%>"/></th>
                            <%if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {%>
                        <th><bean:message key="Schedule.View.LCT" bundle="<%=clientcode%>"/></th>
                            <%}%>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int countList = portmap.size();
                        for (int n = 0; n < countList; n++) {
                            ArrayList schelist = (ArrayList) portmap.get(n);
                    %>
                    <tr><%
                        for (int vv = 0; vv < schelist.size(); vv++) {%>
                        <td><%=schelist.get(vv).toString()%></td>
                        <%}%>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
    </div>

    <%
    } else if (resData != null && (resData.equalsIgnoreCase("SchdByVessel"))) {
        String vslatport = (String) request.getAttribute("vslatport");
        String vslfromdate = (String) request.getAttribute("vslfromdate");
        String vsltodate = (String) request.getAttribute("vsltodate");
        ScheduleCommonVO schdPortVo = null;
        String vessel = "", vesselRest = null;
        String vesselname = "";
        String voyage = "";
        String service = "";
        String Bound = "", Boundname = "";
        String port = "";
        String portname = "";
        String Terminal = "";
        String terminalname = "";
        String arrdate = "";
        String depdate = "";
        String type = "";
        String status = "";
        String addhoc = "";
        String LCT = "";
        ArrayList portmap = new ArrayList();
        List<ScheduleCommonVO> scheduleportDetails = (List<ScheduleCommonVO>) request.getAttribute("SCHD");

        if (userCode != null && scheduleportDetails != null && scheduleportDetails.size() > 0) {%>
    <div class="table-responsive">
        <div class="pull-right m-r-20"> <a href="javascript:Download('Vessel');"  data-toggle="tooltip" class="btn btn-info btn-sm" title="Export" data-placement="right"><i class="fa fa-download"></i> <bean:message key="Schd.export" bundle="<%=clientcode%>"/></a> <a href="javascript:generatetoken()"  onclick="generatetoken()" data-toggle="modal" data-target="#myModal" class="btn btn-info btn-sm" title="Mail" data-placement="right"><i class="fa fa-envelope-o"></i> <bean:message key="Schd.mail" bundle="<%=clientcode%>"/></a> </div>
        <h5 class="text-left"><br />
            <hr>
        </h5>
    </div>
    <%}
    %>
    <div class="col-lg-12">
        <span class="lbl_clr_txt">
            <%if ("Y".equalsIgnoreCase(enableScheduleDisclaimer) && scheduleDisclaimerMessage != null && !scheduleDisclaimerMessage.isEmpty()) {%>
            <%=scheduleDisclaimerMessage%>
            <%} else {%>
            <bean:message key="ecom.msg.schddate" bundle="<%=clientcode%>"/>
            <%}%>
        </span>
    </div>
    <div class="table-responsive card card-body card-shadow ">
        <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
        <table class="table table-striped table-bordered"  id="datatablebyvessel">
            <thead>
                <tr>
                    <th><bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.PortName" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.TerminalName" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.ETA" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="Schedule.View.ETD" bundle="<%=clientcode%>"/></th>
                        <%if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {%>
                    <th><bean:message key="Schedule.View.LCT" bundle="<%=clientcode%>"/></th>
                        <%}%>
                </tr>
            </thead>
            <tbody>
                <%
                    List<ScheduleCommonVO> summaryList = null;
                    schdPortVo = null;
                    if (scheduleportDetails != null && scheduleportDetails != null && scheduleportDetails.size() > 0) {
                        int i = 0;
                        summaryList = scheduleportDetails;
                        int size = summaryList.size();
                        for (i = 0; i < size; i++) {
                            ArrayList arryport = new ArrayList();
                            schdPortVo = (ScheduleCommonVO) summaryList.get(i);
                            vessel = schdPortVo.getVessel();
                            voyage = schdPortVo.getVoyage();
                            vesselname = schdPortVo.getVesselname();
                            service = schdPortVo.getService();
                            vesselRest = (String) vslrest.get(vessel);
                            if ("N".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-success'> North</span>";
                                Boundname = "North";
                            } else if ("S".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-info'> South</span>";
                                Boundname = "South";
                            } else if ("W".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-west'> West</span>";
                                Boundname = "West";
                            } else if ("E".equalsIgnoreCase(Validation.checkNull(schdPortVo.getBound()))) {
                                Bound = "<span class='label label-east'> East</span>";
                                Boundname = "East";
                            }
                            //Bound = schdPortVo.getBound();
                            port = schdPortVo.getPort();
                            portname = schdPortVo.getPortname();
                            Terminal = schdPortVo.getTerminal();
                            terminalname = schdPortVo.getTerminalname();
                            arrdate = (schdPortVo.getArrdate() != null) ? DateConversion_New.convertDefaultTimeFormat(schdPortVo.getArrdate().trim()) : "";
                            depdate = (schdPortVo.getDepdate() != null) ? DateConversion_New.convertDefaultTimeFormat(schdPortVo.getDepdate().trim()) : "";
                            type = schdPortVo.getType();
                            status = schdPortVo.getStatus();
                            addhoc = schdPortVo.getAddhoc();
                            LCT = Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(schdPortVo.getClosingtime()));
                            if (!("FEEDER".equalsIgnoreCase(type) && "Y".equalsIgnoreCase(feederRestriction) && (!"Y".equalsIgnoreCase(userfuritcust)))) {
                                arryport.add(service);
                                arryport.add(vesselname);
                                arryport.add(voyage);
                                arryport.add(Boundname);
                                arryport.add(portname);
                                arryport.add(terminalname);
                                arryport.add(arrdate);
                                arryport.add(depdate);
                                if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                                    arryport.add(LCT);
                                }
                                portmap.add(arryport);
                                boolean isMaskPort = MaskPortUtil.isActualPort(port);
                %>
                <tr>
                    <td data-title=<bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/>><%=isMaskPort ? "" : service%><input type="hidden" name="hdnvessel" id="hdnvessel" value="<%=vslatport%>" /></td>
                    <td data-title=<bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/>><%if (!isMaskPort) {%><a href="javascript:void(0)" <%if ("S".equalsIgnoreCase(vesselRest)) {%> onclick="VesselDetails('<%=vessel%>')" class='linkeffect' <%}%> id="routehref"  data-toggle="tooltip"   title="Vessel Details" data-placement="right"><%=vesselname%></a><%}%><input type="hidden" name="hdnvesselfromdate" id="hdnvesselfromdate" value="<%=vslfromdate%>" /></td>
                    <td data-title=<bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/>><%=isMaskPort ? "" : voyage%><input type="hidden" name="hdnvesseltodate" id="hdnvesseltodate" value="<%=vsltodate%>" /></td>
                    <td data-title=<bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/>><%=isMaskPort ? "" : Bound%></td>
                    <td data-title=<bean:message key="Schedule.View.PortName" bundle="<%=clientcode%>"/>><%=MaskPortUtil.getDisplayPortName(port, portname)%></td>
                    <td data-title=<bean:message key="Schedule.View.TerminalName" bundle="<%=clientcode%>"/>><%=isMaskPort ? "" : terminalname%></td>
                    <td data-title=<bean:message key="Schedule.View.ETA" bundle="<%=clientcode%>"/>><%=arrdate%></td>
                    <td data-title=<bean:message key="Schedule.View.ETD" bundle="<%=clientcode%>"/>><%=depdate%></td>
                    <%if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {%>
                    <td data-title=<bean:message key="Schedule.View.LCT" bundle="<%=clientcode%>"/>><%=LCT%></td>
                    <%}%>
                </tr>
                <%
                            }
                        }
                        session.setAttribute("PortList", portmap);
                        session.setAttribute("atport", vslatport);
                        session.setAttribute("fromdate", vslfromdate);
                        session.setAttribute("todate", vsltodate);

                    }
                %>
            </tbody>
        </table>
    </div>
    <div class="col-lg-12" style="display:none" >
        <div class="table-responsive mobile-table">
            <table  class="table table-condensed table-striped table-bordered" id="datatablebyvessel1"></table>
        </div>
    </div>

    <%
    } else if (resData != null && (resData.equalsIgnoreCase("SchdByBlwLocation"))) {

        String bwlocpol = (String) request.getAttribute("bwlocpol");
        String bwlocpod = (String) request.getAttribute("bwlocpod");
        String bwlocfromdate = (String) request.getAttribute("bwlocdeptdate");
        String bwloctodate = (String) request.getAttribute("bwlocarrdate");
        ScheduleBlwLocationList schedulebetweenDetails = (ScheduleBlwLocationList) request.getAttribute("SCHD");
    %>
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 " >

        <div class="table-responsive txt_datatable_top">

            <table id="datatablebylocation" class="table table-striped table-bordered table-responsive-xl"  style="width:100%" >
                <thead>
                    <tr>
                        <th><bean:message key="Schedule.View.POLName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.POLTerminalName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.DeptDate" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.PODName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.PODTerminalName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.ArrivalDate" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Type" bundle="<%=clientcode%>"/></th>
                            <%
                                if (userCode != null && userCode.length() > 0) {
                            %>
                        <th class="actionval">Action</th>
                            <%}%>
                    </tr>
                </thead>
                <tbody>
                    <%
                        String service = "";
                        String voyage = "";
                        String bound = "";
                        String port = "";
                        String arrdate = "";
                        String depdate = "";
                        String type = "";
                        String terminal = "";
                        String vessel = "";
                        String portname = "";
                        String terminalname = "";
                        String vesselname = "";
                        String strpodarr = "", groupcallid = "";
                        String strpoddep = "", podname = "", podterminal = "";
                        String servicename = "", podterm = "";
                        List<ScheduleBlwLocationVo> summarybnList = null;
                        ScheduleBlwLocationVo schdblwloclvo = null;
                        if (schedulebetweenDetails != null && schedulebetweenDetails.getSchedulbtlocation() != null) {
                            int i = 0;
                            summarybnList = schedulebetweenDetails.getSchedulbtlocation();
                            int betsize = summarybnList.size();
                            ArrayList portmap = new ArrayList();
                            for (i = 0; i < betsize; i++) {
                                ArrayList arryport = new ArrayList();
                                schdblwloclvo = (ScheduleBlwLocationVo) summarybnList.get(i);
                                service = (schdblwloclvo.getService() != null) ? schdblwloclvo.getService().trim() : "";
                                voyage = schdblwloclvo.getVoyage();
                                bound = schdblwloclvo.getBound();
                                port = schdblwloclvo.getPort();
                                arrdate = (schdblwloclvo.getArrdate() != null) ? DateConversion_New.convertDefaultTimeFormat(schdblwloclvo.getArrdate().trim()) : "";
                                depdate = (schdblwloclvo.getDepdate() != null) ? DateConversion_New.convertDefaultTimeFormat(schdblwloclvo.getDepdate().trim()) : "";
                                type = schdblwloclvo.getType();
                                terminal = schdblwloclvo.getTerminal();
                                vessel = schdblwloclvo.getVessel();
                                portname = schdblwloclvo.getPortname();
                                vesselname = schdblwloclvo.getVesselname();
                                terminalname = schdblwloclvo.getTerminalname();
                                strpodarr = (schdblwloclvo.getPodeta() != null) ? DateConversion_New.convertDefaultTimeFormat(schdblwloclvo.getPodeta().trim()) : "";
                                strpoddep = (schdblwloclvo.getPodetd() != null) ? DateConversion_New.convertDefaultTimeFormat(schdblwloclvo.getPodetd().trim()) : "";
                                podname = (schdblwloclvo.getPodname() != null) ? schdblwloclvo.getPodname().trim() : "";
                                podterm = (schdblwloclvo.getPodterminal() != null) ? schdblwloclvo.getPodterminal().trim() : "";
                                groupcallid = schdblwloclvo.getGroupcallid();
                                servicename = (String) servicelist.get(service);
                                service = (servicename != null) ? servicename.trim() : service;
                                podterminal = (String) terminallist.get(podterm.toUpperCase().trim());
                                podterminal = (podterminal != null) ? podterminal.trim() : podterm;
                                String startdate = schdblwloclvo.getDepdate();
                                int result = 0;
                                if (startdate != null) {
                                    result = DateConversion_New.daysdifferent(startdate);
                                }
                                arryport.add(portname);
                                arryport.add(terminalname);
                                arryport.add(depdate);
                                arryport.add(podname);
                                arryport.add(podterminal);
                                arryport.add(strpodarr);
                                arryport.add(vesselname);
                                arryport.add(service);
                                arryport.add(voyage);
                                arryport.add(bound);
                                arryport.add(type);
                                portmap.add(i, arryport);
                    %>
                    <tr>
                        <td data-title=<bean:message key="Schedule.View.POLName" bundle="<%=clientcode%>"/>><%=portname%><input type="hidden" name="hdnpol" id="hdnpol" value="<%=bwlocpol%>" /></td>
                        <td data-title=<bean:message key="Schedule.View.POLTerminalName" bundle="<%=clientcode%>"/>><%=terminalname%><input type="hidden" name="hdnpod" id="hdnpod" value="<%=bwlocpod%>" /></td>
                        <td data-title=<bean:message key="Schedule.View.DeptDate" bundle="<%=clientcode%>"/>><%=depdate%><input type="hidden" name="hdnfromdate" id="hdnfromdate" value="<%=bwlocfromdate%>" /></td>
                        <td data-title=<bean:message key="Schedule.View.PODName" bundle="<%=clientcode%>"/>><%=podname%><input type="hidden" name="hdntodate" id="hdntodate" value="<%=bwloctodate%>" /></td>
                        <td data-title=<bean:message key="Schedule.View.PODTerminalName" bundle="<%=clientcode%>"/>><%=podterminal%></td>
                        <td data-title=<bean:message key="Schedule.View.ArrivalDate" bundle="<%=clientcode%>"/>><%=strpodarr%></td>
                        <td data-title=<bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/>><%=vesselname%></td>
                        <td data-title=<bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/>><%=service%></td>
                        <td data-title=<bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/>><%=voyage%></td>
                        <td data-title=<bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/>><%=bound%></td>
                        <td data-title=<bean:message key="Schedule.View.Type" bundle="<%=clientcode%>"/>><%=type%></td>
                        <%
                            if (userCode != null && userCode.length() > 0) {
                        %>
                        <td data-title="Action" class="bookid"><a href="#" class="btn btn-warning btn-xs" id="999" onclick="booknow('<%=schdblwloclvo.getPort()%>', '<%=schdblwloclvo.getPod()%>', '<%=schdblwloclvo.getTerminal()%>', '<%=schdblwloclvo.getPodterminal()%>', '<%=schdblwloclvo.getArrdate()%>', '<%=schdblwloclvo.getDepdate()%>', '<%=schdblwloclvo.getService()%>', '<%=schdblwloclvo.getVessel()%>', '<%=schdblwloclvo.getVoyage()%>', '<%=schdblwloclvo.getBound()%>', '<%=vesselname%>', '<%=groupcallid%>', '<%=result%>')">Book Now</a></td>
                        <%}%>
                    </tr>
                    <%
                            }
                            session.setAttribute("PortList", portmap);
                            session.setAttribute("bwlocpol", bwlocpol);
                            session.setAttribute("bwlocpod", bwlocpod);
                            session.setAttribute("bwlocfromdate", bwlocfromdate);
                            session.setAttribute("bwloctodate", bwloctodate);
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <%
        }
    %>
</form>
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title"><bean:message key="ecom.mail" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <label class="col-sm-12"><bean:message key="tracking.msg.entmail" bundle="<%=clientcode%>"/>:  <span class="label label-info"><bean:message key="tracking.msg.commasemicolonmultipleemail" bundle="<%=clientcode%>"/></span></label>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-offset-0 col-sm-11">
                                <textarea class="form-control" id="email_text" name="email_text" rows="3" cols="50" style="resize:none"></textarea>
                                <label class="error" id="mailerror" style="display:none"><bean:message key="tracking.msg.mailreq" bundle="<%=clientcode%>"/></label>
                                <input name="email_value"  id="email_value" type="hidden" size="55"/>
                            </div>
                        </div>
                        <%if ("Y".equalsIgnoreCase(schedulecaptcha)) {%>
                        <div class="form-group row" id="schedulemailcaptcha">
                            <div class="col-sm-offset-0 col-sm-11">
                                <div id="schedulerecaptiid"></div>  
                                <div id="g-recaptcha-error"></div>
                            </div>
                        </div>
                        <%}%>
                        <div class="modal-footer">
                            <button type="button" title="Mail"  class="btn btn-primary btn-sm" onclick="sendScheduleMail('<%=client_code%>');" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="mailbtn"> <i class="fa fa-envelope-o"></i> <bean:message key="ecom.mail" bundle="<%=clientcode%>"/></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form name="booknows" method="post" id="booknows" action="NewBooking.do" class="form-horizontal">
    <input type="hidden" id="pol" name="pol" />
    <input type="hidden" id="pod" name="pod" />
    <input type="hidden" id="polt" name="polt" />
    <input type="hidden" id="podt" name="podt" />
    <input type="hidden" id="edt" name="edt" />
    <input type="hidden" id="eta" name="eta" />
    <input type="hidden" id="service" name="service" />
    <input type="hidden" id="vessel" name="vessel" />
    <input type="hidden" id="voyage" name="voyage" />
    <input type="hidden" id="bound" name="bound" />
    <input type="hidden" id="vesselname" name="vesselname" />
    <input type="hidden" id="groupcallid" name="groupcallid" />
    <input type="hidden" id="BookingRequestType" name="BookingRequestType" />
</form>
<!--<script src="js/jquery-ui.js"></script>-->



<script>
    var scheduleCaptchaRendered = false;
    $(document).ready(function () {
        $('#datatablebyport').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        $('#datatablebyvessel').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
        $('#schedulerecaptiid').empty();
    });
    function Download(strval)
    {
        document.getElementById("strtype").value = strval;
        document.getElementById('theForm').submit();
    }



</script>
