<%@page import="com.google.gson.Gson"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.*,net.ecom.vo.Route,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,net.ecom.vo.CutoffConfigVO,net.ecom.common.GeneralConf,net.ecom.common.MaskPort,net.ecom.common.utils.MaskPortUtil" errorPage="" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>

<%
    String resData = (String) request.getAttribute("SCHDSEARCH");
    String userCode = (String) session.getAttribute("SESSION_LOGINNAME");
    System.out.println("[ECOM-WAR] ScheudleBetweenLocationView polname--> usercode--> " + userCode );
                                  
    String user = (String) session.getAttribute("sessionAdmin");
    String clientcode = (String) session.getAttribute("clientcode");
    String client_code = (String) getServletContext().getAttribute("client_code");
    List routeInfo = (ArrayList) request.getAttribute("SCHD");
    session.setAttribute("Schedule_info", routeInfo);
    String bwlocpol = (String) request.getAttribute("bwlocpol");
    String bwlocpod = (String) request.getAttribute("bwlocpod");
    String bwlocfromdate = (String) request.getAttribute("bwlocdeptdate");
    String bwloctodate = (String) request.getAttribute("bwlocarrdate");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    List pluginlis = (List) getServletContext().getAttribute("LRP_PLUGIN_DETAILS");
    String Display_LCT_CUTOFF = (String) request.getServletContext().getAttribute(GeneralConf.Display_LCT_CUTOFF);
    String hideMenuIcon = (String) getServletContext().getAttribute(GeneralConf.HIDE_MENU_ICON);
    String Enable_Schedule_Calendar = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Schedule_Calendar);
    String enableScheduleDisclaimer = (String) request.getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_DISCLAIMER);
    String enableScheduleCardCarbon = (String) request.getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_CARD_CARBON);
    boolean scheduleCardCarbonEnabled = "Y".equalsIgnoreCase(enableScheduleCardCarbon);
    
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    
    String scheduleDisclaimerMessage = (String) request.getServletContext().getAttribute("ScheduleDisclaimerMessage");
    Map<String, String> temrminalMap = (Map<String, String>) getServletContext().getAttribute("TEMINALSMAPCONTEXT");
    ArrayList routeArray1 = null;
    boolean flag = false;
    if (routeInfo != null && routeInfo.size() > 0) {
        for (int j = 0; j < routeInfo.size(); j++) {
            routeArray1 = (ArrayList) routeInfo.get(j);
            if (routeArray1 != null && routeArray1.size() > 0) {
                flag = true;
            }
        }
    }
    String bookaccess = (String) session.getAttribute("bookallowed");
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String schedulecaptcha = (String) getServletContext().getAttribute(GeneralConf.ENABLE_MASS_SUBMISSION_CAPTCHA);
    session.removeAttribute("ScheduleMailCount");
    boolean hideMaskPort = false; // Initialize to false, will be calculated inside loop with transshipment port
    System.out.println("[ECOM-WAR] ScheudleBetweenLocationView hideMaskPort---> " + hideMaskPort);
    if ("OEL".equalsIgnoreCase(carrierCode)) {%>
<style type="text/css">
    .panel_inner_left   { width: 100% }
</style>
<%}%>
<form name="theForm" method="post" id="theForm" action="ActionExcel">
    <input type="hidden" id="strtype" name="strtype"/>
    <input type="hidden" id="scheduletype" name="scheduletype" value="<%=resData%>" />
    <input type="hidden" id="Captcha_Site_Key" value="<%=siteKey%>"/>
    <%if (userCode != null && routeInfo != null && routeInfo.size() > 0 && flag == true) {%>
    <div id="export_button" class="table-responsive" >
        <div class="pull-right m-r-20"> <a href="javascript:Download('Location');"  data-toggle="tooltip" class="btn btn-info btn-sm" title="Export" data-placement="right"><i class="fa fa-download"></i> <bean:message key="Schd.export" bundle="<%=clientcode%>"/></a> <a href="javascript:generatetoken();" onclick="generatetoken()"  data-toggle="modal" data-target="#myModal" class="btn btn-info btn-sm" title="Mail" data-placement="right"><i class="fa fa-envelope-o"></i> <bean:message key="Schd.mail" bundle="<%=clientcode%>"/></a> </div>
        <h5 class="text-left"><br />
            <hr>
        </h5>
    </div>
    <%}%>
    <% if (scheduleCardCarbonEnabled) { %>
    <link rel="stylesheet" type="text/css" href="Design/assets/css/carbon-modal.css?<%=jsno%>"/>
    <% } else { %>
    <style type="text/css">
        #schedulebetweenlocationview .panel_inner_content {
            display: flex;
            flex-wrap: wrap;
        }
        #schedulebetweenlocationview .panel_inner_service_note {
            flex: 0 0 100%;
            width: 100%;
        }
        #schedulebetweenlocationview .panel_inner_left {
            flex: 1 1 78%;
            float: none;
            width: 78%;
        }
        #schedulebetweenlocationview [class*="schd_book_now_block"].panel_inner_right {
            align-items: center;
            align-self: stretch;
            box-sizing: border-box;
            display: flex;
            flex: 0 0 22%;
            flex-direction: column;
            float: none;
            height: auto;
            justify-content: center;
            min-height: 190px;
            padding: 16px 12px;
            width: 22%;
        }
        #schedulebetweenlocationview [class*="schd_book_now_block"] > .booking_btn {
            margin-bottom: 0;
            padding: 0;
            text-align: center;
            width: 100%;
        }
    </style>
    <% } %>
    <div class="col-lg-12" id="schedulebetweenlocationview">
        <span class="lbl_clr_txt">
            <%if ("Y".equalsIgnoreCase(enableScheduleDisclaimer) && scheduleDisclaimerMessage != null && !scheduleDisclaimerMessage.isEmpty()) {%>
            <%=scheduleDisclaimerMessage%>
            <%} else {%>
            <bean:message key="ecom.msg.schddate" bundle="<%=clientcode%>"/>
            <%}%>
            <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
            <br>
            <bean:message key='ecom.schd.tt' bundle="<%=clientcode%>"/>
            <br>
            <bean:message key='ecom.schd.date' bundle="<%=clientcode%>"/>
            <%}%>
            <span class='pull-right'><span class="fa fa-schd_default_icon" title="Default Route"></span> <bean:message key="Schedule.View.Defaultroute" bundle="<%=clientcode%>"/></span></span>
            <%
                int countexcel = 0;
                String serviceCode = null, vesselCode = null, voyageCode = null, boundCode = null, serviceType = null;
                ArrayList portmap = new ArrayList();
                HashMap<String, CutoffConfigVO> cutoffdatemap = (HashMap) request.getAttribute("RoutingCutOffDetailsFromPort");
                System.out.println("[ECOM-WAR] ScheudleBetweenLocationView ********************************>" + cutoffdatemap);
                String docClosing = "", vgmClosing = "", contClosing = "";
                CutoffConfigVO cutOffVO = null;
               
                if (routeInfo != null && routeInfo.size() > 0 && flag == true) {
                    int cc = 0;
                    int count = 0;
                    cc = 0;
                    String jsonEventDataList = "";
                    List<Map<String, Object>> eventDataList = new ArrayList<>();
                    Map<String, Object> eventData = null;
                    Map<String, Object> event = null;
                    for (int k = 0; k < routeInfo.size(); k++) {
                        String defroute = "", polname = "", podname = "", vessel = "", voy = "", service = "", arrdate = "", depdate = "", loadtype = "Single", bound = "", pol = null, pod = null, grpcallid = null, lct = null, polTer = null, polTerLen = null, podTer = null, podTerLen = null;
                        ArrayList routeArray = (ArrayList) routeInfo.get(k);
                        Route eBookingrouting = null;
                        event = new HashMap<>();
                        eventData = new HashMap<>();
                        int result = 0, transittime = 0, transpcount = 0;
                        boolean isInlandMve = false;
                        String cutOffPort = null, cutOffTerminal = null, cutOffArrDate = null;
                        String str_type = "N";
                        String maskport_trans1orpod = "N";
                        String strServiceCode = null;
                        if (routeArray != null && routeArray.size() > 0) {
                            int arraySize = routeArray.size();
                            int rst = 0;
                            for (int i = 0; i < arraySize; i++) {
                                eBookingrouting = (Route) routeArray.get(i);
                                rst++;
                                if ("N".equalsIgnoreCase(maskport_trans1orpod) && "Y".equalsIgnoreCase(str_type)) {
                                    maskport_trans1orpod = eBookingrouting.getLoadport();
                                }
                                if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(eBookingrouting.getLegType()) || "F".equalsIgnoreCase(eBookingrouting.getLegType()))) {
                                    str_type = "Y";
                                    depdate = depdate != null && depdate.length() > 0 ? depdate : eBookingrouting.getETD1();
                                    polname = polname != null && polname.length() > 0 ? polname : MaskPortUtil.getDisplayPortName(eBookingrouting.getLoadport(), eBookingrouting.getPolPortName());
                                    service = service != null && service.length() > 0 ? service : eBookingrouting.getServiceName();
                                    vessel = vessel != null && vessel.length() > 0 ? vessel : eBookingrouting.getVesselName();
                                    voy = voy != null && voy.length() > 0 ? voy : eBookingrouting.getVoyage();
                                    bound = bound != null && bound.length() > 0 ? bound : eBookingrouting.getBound();
                                    cutOffPort = eBookingrouting.getLoadport();
                                    cutOffTerminal = eBookingrouting.getCutoff_polterminal();
                                    cutOffArrDate = eBookingrouting.getCutoff_polarrdate();
                                    serviceCode = eBookingrouting.getService();
                                    vesselCode = eBookingrouting.getVessel();
                                    voyageCode = eBookingrouting.getVoyage();
                                    boundCode = eBookingrouting.getBound();
                                    serviceType = eBookingrouting.getLegType();
                                    grpcallid = eBookingrouting.getGrpCallId();
                                    System.out.println("[ECOM-WAR] ScheudleBetweenLocationView grpcallid====================>  " + grpcallid);
                                    pol = eBookingrouting.getLoadport();
                                    String polMportName = MaskPortUtil.getMportNameByActualPort(pol);
                                    polTer = (polMportName != null && !polMportName.isEmpty()) ? polMportName : Validation.checkNull(temrminalMap.get(eBookingrouting.getBlpolTer()));
                                    lct = Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(eBookingrouting.getClosingtime()));
                                    if ("Feeder".equalsIgnoreCase(eBookingrouting.getLegtypename()) && (vessel == null || vessel.toLowerCase().contains("dummy"))) {
                                        vessel = "Feeder";
                                    }
                                }

                                strServiceCode = eBookingrouting.getService();
                                if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                                    polname = polname != null && polname.length() > 0 ? polname : MaskPortUtil.getDisplayPortName(eBookingrouting.getLoadport(), eBookingrouting.getPolPortName());
                                    arrdate = eBookingrouting.getETA1();
                                    podname = MaskPortUtil.getDisplayPortName(eBookingrouting.getDisport(), eBookingrouting.getPodPortName());
                                    pod = eBookingrouting.getDisport();
                                    String podMportName = MaskPortUtil.getMportNameByActualPort(pod);
                                    podTer = (podMportName != null && !podMportName.isEmpty()) ? podMportName : Validation.checkNull(temrminalMap.get(eBookingrouting.getBlpodTer()));
                                }
                                if (!(strServiceCode != null && strServiceCode.trim().length() > 0)) {
                                    isInlandMve = true;
                                }
                                if (i == 0 || i == arraySize - 1) {
                                    if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                                        transpcount++;
                                    }
                                } else {
                                    transpcount++;
                                }
                                if (transpcount > 1) {
                                    loadtype = "Multiple";
                                }
                                if (depdate != null && depdate.length() > 0) {
                                    result = DateConversion_New.daysdifferent(depdate);
                                }
                                defroute = eBookingrouting.getDefRoute();
                            }
                            transittime = DateConversion_New.daysDifferentBetweenTwoDays(depdate, arrdate);
                            docClosing = "";
                            vgmClosing = "";
                            contClosing = "";
                            if (pluginlis != null && pluginlis.contains("BEC")) {
                                cutOffVO = (CutoffConfigVO) cutoffdatemap.get(Validation.checkNull(grpcallid));
                                if (cutOffVO != null) {
                                    docClosing = Validation.checkNull(cutOffVO.getDoccutoff());
                                    vgmClosing = Validation.checkNull(cutOffVO.getVgmcutoff());
                                    contClosing = Validation.checkNull(cutOffVO.getPortentrycutoff());
                                    System.out.println("[ECOM-WAR] ScheudleBetweenLocationView docClosing==> " + docClosing + " vgmClosing==> " + vgmClosing + " contClosing==> " + contClosing);
                                }
                            } else {
                                System.out.println("[ECOM-WAR] ScheudleBetweenLocationView cutoffdatemap---> " + cutoffdatemap
                                        + " cutOffTerminal====> " + cutOffTerminal
                                        + " cutOffPort====> " + cutOffPort);
                                String portterminal = cutOffPort + "&&" + cutOffTerminal;
                                cutOffVO = (CutoffConfigVO) cutoffdatemap.get(portterminal);
                                portterminal = cutOffPort + "&&" + "All";
                                if (cutOffVO == null) {
                                    cutOffVO = (CutoffConfigVO) cutoffdatemap.get(portterminal);
                                }
                                portterminal = "All" + "&&" + "All";
                                if (cutOffVO == null) {
                                    cutOffVO = (CutoffConfigVO) cutoffdatemap.get(portterminal);
                                }
                                System.out.println("[ECOM-WAR] ScheudleBetweenLocationView cutOffVO1-> " + cutOffVO);
                                if (cutOffVO != null) {
                                    docClosing = Validation.checkNull(DateConversion_New.cutOffCalc(cutOffArrDate, Integer.parseInt(cutOffVO.getDocClosing())));
                                    vgmClosing = Validation.checkNull(DateConversion_New.cutOffCalc(cutOffArrDate, Integer.parseInt(cutOffVO.getVgmClosing())));
                                    contClosing = Validation.checkNull(DateConversion_New.cutOffCalc(cutOffArrDate, Integer.parseInt(cutOffVO.getFullContClosing())));
                                }
                            }
                            if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                                contClosing = lct;
                            }
                            if ("Y".equalsIgnoreCase(Enable_Schedule_Calendar)) {
                                event.put("title", serviceCode + " / " + vesselCode + " / " + voy + bound + "~~" + "row" + cc + "~~Service: " + service + "(" + serviceCode + ")<br>Vessel: " + vessel + " <br>Voyage: " + voy + bound);
                                event.put("start", depdate);
                                System.out.println("[ECOM-WAR] ScheudleBetweenLocationView depdate>" + depdate + "service>>" + service);
                                eventData.put("event", event);
                                eventDataList.add(eventData);
                            }
                            if ("N".equalsIgnoreCase(maskport_trans1orpod)) {
                                maskport_trans1orpod = pod;
                            }
                           
                            hideMaskPort = MaskPortUtil.checkIsMaskPort(pol, maskport_trans1orpod); // Recalculate with transshipment port
            %>
        <div class="page_scrol_data1">
            <div class="details_panel" id="row<%=cc%>">
                <div class="panel_inner_content">
                    <div class="panel_inner_service_note">
                        <div class='m-l-20'>
                            <%if ("Y".equalsIgnoreCase(defroute)) {%>
                            <i class="fa fa-schd_default_icon" title="Default Route"></i>
                            <%}%>
                            <%if (!hideMaskPort) {%>
                            <span class="<%="Y".equalsIgnoreCase(defroute) ? "" : "m-l-30"%>"><%=service + " (" + serviceCode + ")"%><input type="hidden" name="hdnfromdate" id="hdnfromdate" value="<%=bwlocfromdate%>" /></span>
                            <%}%>
                        </div>

                    </div>
                    <div class="panel_inner_left" <%if ("Y".equalsIgnoreCase(hideMenuIcon)) {%>style="width: 100%"<%}%>>
                        <div class="panel_inner_top">
                            <div class="panel_inner_top_1">
                                <table>
                                    <tr>
                                        <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                        <td>
                                    <dt>
                                    <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd>
                                    <dd><strong>
                                            <%=depdate%>
                                            <input type="hidden" name="hdnpod" id="hdnpod" value="<%=bwlocpod%>" />
                                            <input type="hidden" name="hdnpol" id="hdnpol" value="<%=bwlocpol%>"/>
                                        </strong></dd>
                                    </dt>
                                    </td>
                                    <td style="padding: 0;"><img class="content_icon" src="Design/assets/images/page_icons/Rvessel.svg" /></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td colspan="2" style="padding-left: 0;font-size: 11px;">
                                            <span title="<%=polTer%>"><%= hideMaskPort?"":polTer%></span>                                    
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="panel_inner_top_2">
                                <table>
                                    <tr>
                                        <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                        <td>
                                    <dt>
                                    <dd> <bean:message key="ecom.schdl.arrv" bundle="<%=clientcode%>"/></dd>
                                    <dd><strong><%= arrdate%></strong></dd>
                                    </dt>
                                    </td>
                                    <td style="padding: 0;"><img class="content_icon" src="Design/assets/images/page_icons/Lvessel.svg" /></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td colspan="2" style="padding-left: 0;font-size: 11px;">
                                            <span title="<%=podTer%>"><%= hideMaskPort?"":podTer%></span>                                    
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="panel_inner_top_3">
                                <table>
                                    <tr>
                                        <td><img class="header_icon" src="Design/assets/images/page_icons/vessel.svg" /></td>
                                        <td>
                                    <dt>
                                    <dd><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></dd>
                                    <input type="hidden" name="hdntodate" id="hdntodate" value="<%=bwloctodate%>" />
                                   
                                    <%if (!hideMaskPort) {%>
                                    <dd><strong><%=vessel%></strong></dd>
                                    <dd><%=voy%><%=bound%></dd>
                                    <%}%>
                                  
                                    </dt>
                                    </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="panel_inner_top_4">
                                <table>
                                    <tr>
                                        <td><img class="header_icon" src="Design/assets/images/page_icons/clock.svg" /></td>
                                        <td>
                                    <dt>
                                    <dd> <bean:message key="ecom.schdl.ttime" bundle="<%=clientcode%>"/></dd>
                                    <dd><strong><%=transittime%> days</strong></dd>
                                    </dt>
                                    </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="panel_inner_bottom">
                            <div class="panel_inner_bottom_header">
                                <img class="header_icon" src="Design/assets/images/page_icons/clock.svg" />
                                <span><bean:message key="ecom.schdl.deadline" bundle="<%=clientcode%>"/></span>
                            </div>	
                            <div class="panel_inner_bottom_1">
                                <dt>
                                <dd>
                                    <%if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {%>
                                    <bean:message key="Schedule.View.LCT" bundle="<%=clientcode%>"/>
                                    <%} else {%>
                                    <bean:message key="ecom.schdl.cntrgatein" bundle="<%=clientcode%>"/>
                                    <%}%>
                                </dd>
                                <dd><strong><%=contClosing%></strong></dd>
                                </dt>
                            </div>
                            <div class="panel_inner_bottom_2">
                                <dt>
                                <dd><bean:message key="ecom.schdl.sidoc" bundle="<%=clientcode%>"/></dd>
                                <dd><strong><%=docClosing%></strong></dd>
                                </dt>
                            </div>
                            <div class="panel_inner_bottom_3">
                                <dt>
                                <dd>VGM</dd>
                                <dd><strong><%=vgmClosing%></strong></dd>
                                </dt>
                            </div>

                            <div class="panel_inner_bottom_4">
                                <dt>
                                <dd>
                                    <%if (transpcount > 1) {%>
                                    <div id="show_details" style="cursor: pointer" class="show_details_class" onclick="toggleText('<%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>)', this, '#showDetails<%=k%>')"><%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>) <i class="fa fa-chevron-down"></i></div>
                                        <%} else {%>
                                    <div id="show_details" <%if (isInlandMve) {%> style="cursor: pointer" onclick="toggleText('Multimodal Shipment ', this, '#showDetails<%=k%>')" <%}%>><%= isInlandMve ? "Multimodal Shipment<i class='fa fa-chevron-down'></i>" : "Direct Shipment"%></div>
                                    <%}%>
                                </dd>
                                </dt>
                            </div>

                        </div>
                    </div>
                    <%if (!("OEL".equalsIgnoreCase(carrierCode))) {%>
                    <%if (!"Y".equalsIgnoreCase(hideMenuIcon)) {%>
                    <% if (scheduleCardCarbonEnabled) {
                        String schdCarbonRouteLabel = hideMaskPort
                                ? (Validation.checkNull(pol) + " \u2192 " + Validation.checkNull(pod))
                                : (Validation.checkNull(polname) + " \u2192 " + Validation.checkNull(podname));
                        schdCarbonRouteLabel = schdCarbonRouteLabel.replace("&", "&amp;").replace("\"", "&quot;");
                    %>
                    <div class="panel_inner_right schd_book_now_block<%=k%>" data-schedule-index="<%=k%>" data-route-label="<%=schdCarbonRouteLabel%>">
                        <% if (userCode != null && userCode.length() > 0 && "yes".equalsIgnoreCase(bookaccess)) {
                                    ArrayList arr = new ArrayList();
                                    arr.add(polname);
                                    arr.add(depdate);
                                    arr.add(hideMaskPort?"":service);
                                    arr.add(hideMaskPort?"":vessel);
                                    arr.add(hideMaskPort?"":voy);
                                    arr.add(hideMaskPort?"": bound);
                                    arr.add(podname);
                                    arr.add(arrdate);
                                    arr.add(loadtype);
                                    if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                                        arr.add(contClosing);
                                    }
                                    portmap.add(countexcel, arr);
                                    countexcel++;
                            %>
                            <div class="schd_rate_details" style="display:none;">
                                <div class="label_edit_data"> 1 X <span class="txt_ft_clr"> 20 </span> FT &nbsp;&nbsp;&nbsp; 1 X <span class="txt_ft_clr"> 40 </span> FT  <a href="#" onclick="editViewRates('schd_book_now_block<%=k%>');" class="float-right txt_edit_con" > <i class="fa fa-pencil"></i>  </a> </div>
                                <div class="label_name">  Frieght   </div>
                                <div class="total_conta_val"> $ <span></span> </div>
                                <div class="value-rates_dlg" id="increase" onclick="showRateDetails('schd_book_now_block<%=k%>')"  value="Increase Value"> + </div>
                                <input type="hidden" id="SchdCargoDetailsJson<%=k%>" name="SchdCargoDetailsJson" class="SchdCargoDetailsJson"/>
                                <input type="hidden" id="SchdRateDetailsJson<%=k%>" name="SchdRateDetailsJson" class="SchdRateDetailsJson"/>
                                <input type="hidden" id="SchdRateTotalAmount<%=k%>" name="SchdRateTotalAmount" class="SchdRateTotalAmount"/>
                                <input type="hidden" id="SchdRateTotalAmountInFloat<%=k%>" name="SchdRateTotalAmountInFloat" class="SchdRateTotalAmountInFloat"/>
                            </div>

                        <%}%>
                        <div class="schd_book_now_top booking_btn">
                            <button type="button" class="btn btn-info book_now_btn" id="999" onclick="booknow('<%=k%>', '<%=result%>', '<%=cutOffPort%>', '<%=cutOffTerminal%>', '<%=cutOffArrDate%>', '<%=serviceCode%>', '<%=vesselCode%>', '<%=voyageCode%>', '<%=boundCode%>', '<%=serviceType%>', '<%=depdate%>', '<%=pol%>', '<%=pod%>', '<%=grpcallid%>')"><% if (!(userCode != null && userCode.length() > 0 && "yes".equalsIgnoreCase(bookaccess))) {%><i class="fa fa-lock" aria-hidden="true"></i>&nbsp;<%}%><bean:message key="ecom.schd.msg.booknow" bundle="<%=clientcode%>"/></button>
                        </div>
                        <div class="schd_carbon_booking_btn booking_btn">
                            <button type="button" class="btn btn-info book_now_btn schd_carbon_open_btn" aria-label="<bean:message key="ecom.carboncalculator" bundle="<%=clientcode%>"/>">
                                <bean:message key="ecom.carboncalculator" bundle="<%=clientcode%>"/>
                            </button>
                        </div>
                        <% if (userCode != null && userCode.length() > 0 && "yes".equalsIgnoreCase(bookaccess)) {%>
                        <div class="schd_book_now_meta">
                        <div class="schd_slt_cont_details" style="display:none;">
                            <div class="booking_info"> <bean:message key="ecom.schdl.selcont" bundle="<%=clientcode%>"/></div>
                            <div class="booking_rate" onclick="showViewRates('schd_book_now_block<%=k%>');"> <bean:message key="ecom.schdl.viewrates" bundle="<%=clientcode%>"/></div> 
                        </div>
                        <div class="spl_rate" style="display:none;"><a href="javascript:void(0);" onclick="navigateToQuote('schd_book_now_block<%=k%>')"><bean:message key="ecom.schdl.reqsplrate" bundle="<%=clientcode%>"/></a></div>
                        </div>
                        <%}%>
                    </div>
                    <% } else { %>
                    <div class="panel_inner_right schd_book_now_block<%=k%>">
                        <div class=" booking_btn">
                            <% if (userCode != null && userCode.length() > 0 && "yes".equalsIgnoreCase(bookaccess)) {
                                    ArrayList arr = new ArrayList();
                                    arr.add(polname);
                                    arr.add(depdate);
                                    arr.add(hideMaskPort?"":service);
                                    arr.add(hideMaskPort?"":vessel);
                                    arr.add(hideMaskPort?"":voy);
                                    arr.add(hideMaskPort?"": bound);
                                    arr.add(podname);
                                    arr.add(arrdate);
                                    arr.add(loadtype);
                                    if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                                        arr.add(contClosing);
                                    }
                                    portmap.add(countexcel, arr);
                                    countexcel++;
                            %>
                            <div class="schd_rate_details" style="display:none;">
                                <div class="label_edit_data"> 1 X <span class="txt_ft_clr"> 20 </span> FT &nbsp;&nbsp;&nbsp; 1 X <span class="txt_ft_clr"> 40 </span> FT  <a href="#" onclick="editViewRates('schd_book_now_block<%=k%>');" class="float-right txt_edit_con" > <i class="fa fa-pencil"></i>  </a> </div>
                                <div class="label_name">  Frieght   </div>
                                <div class="total_conta_val"> $ <span></span> </div>
                                <div class="value-rates_dlg" id="increase" onclick="showRateDetails('schd_book_now_block<%=k%>')"  value="Increase Value"> + </div>
                                <input type="hidden" id="SchdCargoDetailsJson<%=k%>" name="SchdCargoDetailsJson" class="SchdCargoDetailsJson"/>
                                <input type="hidden" id="SchdRateDetailsJson<%=k%>" name="SchdRateDetailsJson" class="SchdRateDetailsJson"/>
                                <input type="hidden" id="SchdRateTotalAmount<%=k%>" name="SchdRateTotalAmount" class="SchdRateTotalAmount"/>
                                <input type="hidden" id="SchdRateTotalAmountInFloat<%=k%>" name="SchdRateTotalAmountInFloat" class="SchdRateTotalAmountInFloat"/>
                            </div>

                            <%}%>
                            <button type="button" class="btn btn-info book_now_btn" id="999" onclick="booknow('<%=k%>', '<%=result%>', '<%=cutOffPort%>', '<%=cutOffTerminal%>', '<%=cutOffArrDate%>', '<%=serviceCode%>', '<%=vesselCode%>', '<%=voyageCode%>', '<%=boundCode%>', '<%=serviceType%>', '<%=depdate%>', '<%=pol%>', '<%=pod%>', '<%=grpcallid%>')"><% if (!(userCode != null && userCode.length() > 0 && "yes".equalsIgnoreCase(bookaccess))) {%> <i class="fa fa-lock" aria-hidden="true"></i> &nbsp; <%}%><bean:message key="ecom.schd.msg.booknow" bundle="<%=clientcode%>"/></button>
                        </div>
                        <% if (userCode != null && userCode.length() > 0 && "yes".equalsIgnoreCase(bookaccess)) {%>
                        <div class="schd_slt_cont_details" style="display:none;">
                            <div class="booking_info"> <bean:message key="ecom.schdl.selcont" bundle="<%=clientcode%>"/></div>
                            <div class="booking_rate" onclick="showViewRates('schd_book_now_block<%=k%>');"> <bean:message key="ecom.schdl.viewrates" bundle="<%=clientcode%>"/></div> 
                        </div>
                        <div class="spl_rate" style="display:none;"> <a href="javascript:void(0);" onclick="navigateToQuote('schd_book_now_block<%=k%>')"><bean:message key="ecom.schdl.reqsplrate" bundle="<%=clientcode%>"/> </a> </div>
                         <%}%>
                    </div>
                    <% } %>
                    <%}%>
                    <%}%>
                </div>

                <div id="showDetails<%=k%>" class="collapse show_details">
                    <div class="timeline">
                        <%
                            String vesselfeeder = "";
                            String legType = "";
                            for (int j = 0; j < arraySize; j++) {
                                Route dto = (Route) routeArray.get(j);
                                count++;
                                if ("Feeder".equalsIgnoreCase(dto.getLegtypename()) && (dto.getVesselName() == null || dto.getVesselName().toLowerCase().contains("dummy"))) {
                                    vesselfeeder = "Feeder";
                                } else {
                                    vesselfeeder = dto.getVesselName();
                                }
                                if (dto.getLegtypename() != null && ("Feeder".equalsIgnoreCase(dto.getLegtypename()) || "Mainline".equalsIgnoreCase(dto.getLegtypename()))) {
                                    legType = "vessel";
                                } else {
                                    legType = dto.getLegtypename();
                                }
                                System.out.println("[ECOM-WAR] ScheudleBetweenLocationView legType--> " + legType);
                              
                                hideMaskPort = MaskPortUtil.checkIsMaskPort(dto.getLoadport(), dto.getDisport());
                                %>
                        <div class="show_details_departure show_details_line">
                            <%if (j == 0) {%>
                            <div class="row">
                                <div class="col-sm-12">
                                    <span class="port_name"><%=MaskPortUtil.getDisplayPortName(dto.getLoadport(), dto.getPolPortName())%></span>
                                </div>
                            </div>
                            <%}%>
                            <div class="row">
                                <div class="col-sm-5">
                                    <dt>
                                    <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd>
                                    <dd><strong><%=dto.getETD1()%></strong></dd>
                                    </dt>
                                </div>
                                <%if (legType != null && legType.trim().length() > 0) {%>
                                <div class="col-sm-7">
                                    <table>
                                        <tr>
                                            <td>
                                        <dt>
                                            <img class="header_icon" src="Design/assets/images/page_icons/<%=legType%>.svg" />
                                        <dd><%=legType%></dd>
                                        
                                        <dd><strong><%=hideMaskPort ?"":vesselfeeder%></strong></dd>
                                       
                                        </dt>
                                        </td>
                                        <%if ("Vessel".equalsIgnoreCase(legType)) {%>
                                        <td>
                                        <dt>
                                        <dd><bean:message key="ebooking.service.display" bundle="<%=clientcode%>"/></dd>
                                        <dd><strong><%=hideMaskPort ?"":dto.getServiceName()%></strong></dd>
                                        </dt>
                                        </td>
                                        <td>
                                        <dt>
                                        <dd> <bean:message key="ecom.shippingins.VoyageorBound" bundle="<%=clientcode%>"/></dd>
                                        <dd><strong><%=hideMaskPort ?"":dto.getVoyage()%><%=hideMaskPort ? "":dto.getBound()%></strong></dd>
                                        </dt>
                                        </td>
                                        <%}%>
                                        </tr>
                                    </table>
                                </div>
                                <%}%>
                            </div>
                        </div>
                        <%if (j != 0) {%>
                    </div>
                    <%}%>
                    <%if (j != arraySize - 1) {%>
                    <div class="show_details_status show_details_line">
                        <%}%>
                        <div class="show_details_arrival show_details_line">
                            <div class="row">
                                <div class="col-sm-12">
                                    <span class="port_name"><%=MaskPortUtil.getDisplayPortName(dto.getDisport(), dto.getPodPortName())%></span>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-5">
                                    <dt>
                                    <dd><bean:message key="ecom.schdl.arrv" bundle="<%=clientcode%>"/></dd>
                                    <dd><strong><%=dto.getETA1()%></strong></dd>
                                    </dt>
                                </div>
                            </div>
                        </div>

                        <%                    }// j close
                            cc++; %>
                    </div>
                </div> <!-- timeline -->
            </div>
        </div>
        <%         } // routeArray close
        %>

        <%
            } // i close

            session.setAttribute("PortList", portmap);
            session.setAttribute("bwlocpol", bwlocpol);
            session.setAttribute("bwlocpod", bwlocpod);
            session.setAttribute("bwlocfromdate", bwlocfromdate);
            session.setAttribute("bwloctodate", bwloctodate);
            jsonEventDataList = new Gson().toJson(eventDataList);
            session.setAttribute("jsonEventDataList", jsonEventDataList);

        %>
        <%                    } else {

        %>
        <div class="details_panel card card-body card-shadow d-block" align="center">
            <span><bean:message key="com.NoRecordsFound" bundle="<%=clientcode%>"/></span>
        </div>

        <%                            }%>
    </div>

    <div class="col-lg-12" style="display:none" >
        <div class="table-responsive">
            <table  class="table table-condensed table-striped table-bordered" id="datatablebylocation1">
                <thead>
                    <tr>
                        <th><bean:message key="Schedule.View.POLName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.DeptDate" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Service" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.VesselName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Voyage" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Bound" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.PODName" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.ArrivalDate" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="Schedule.View.Legtype" bundle="<%=clientcode%>"/></th>
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


    <div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog modal-lg" >
            <div class="modal-content">
                <div class="row">
                    <div class="col-md-12">
                        <div class="modal-header bg-info text-white">  <h5 class="modal-title"></span><bean:message key="ecom.mail" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
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
                        </div>
                        <div class="modal-footer">
                            <button type="button" title="Mail"  class="btn btn-primary btn-sm" onclick="sendScheduleMail('<%=client_code%>');" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="mailbtn"> <i class="fa fa-envelope-o"></i>  <bean:message key="ecom.mail" bundle="<%=clientcode%>"/></button>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

</form>

<% if (scheduleCardCarbonEnabled) { %>
<!-- Schedule card carbon calculator modal -->
<div class="modal fade carbon-modal" id="schd_carbon_modal" data-backdrop="false" tabindex="-1" role="dialog" aria-labelledby="schdCarbonModalTitle">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header border-0 pb-0">
                <div class="carbon-modal__header-brand">
                    <div>
                        <h4 class="carbon-modal__title" id="schdCarbonModalTitle"><bean:message key="ecom.carbon.schedule.title" bundle="<%=clientcode%>"/></h4>
                    </div>
                </div>
                <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-body pt-2">
                <div class="carbon-modal__route-banner" role="status">
                    <i class="fa fa-map-marker" aria-hidden="true"></i>
                    <span id="schdCarbonModalRoutePreview"></span>
                </div>
                <div class="schd-carbon-form-panel">
                    <div id="schdCarbonModalErrorForm" class="alert alert-danger schd-carbon-modal-alert" style="display:none;" role="alert"></div>
                    <div class="schd-carbon-form-row">
                        <div class="schd-carbon-form-field">
                            <label class="schd-carbon-form-label" for="schdCarbonModalQty"><bean:message key="ecom.carbon.quantity" bundle="<%=clientcode%>"/></label>
                            <input type="number" min="1" step="1" class="form-control" id="schdCarbonModalQty" value="1"/>
                        </div>
                        <div class="schd-carbon-form-field">
                            <label class="schd-carbon-form-label" for="schdCarbonModalWeight"><bean:message key="ecom.carbon.weight" bundle="<%=clientcode%>"/></label>
                            <input type="number" min="0" step="1" class="form-control" id="schdCarbonModalWeight" value=""/>
                        </div>
                        <div class="schd-carbon-form-actions">
                            <button type="button" class="btn" id="schdCarbonModalCalculate">
                                <i class="fa fa-calculator" aria-hidden="true"></i>
                                <bean:message key="ecom.carbon.calculate" bundle="<%=clientcode%>"/>
                            </button>
                        </div>
                    </div>
                </div>
                <div id="schdCarbonModalLoading" style="display:none;">
                    <i class="fa fa-spinner fa-spin" aria-hidden="true"></i>
                    <bean:message key="ecom.carbon.calculating" bundle="<%=clientcode%>"/>
                </div>
                <div id="schdCarbonModalError" class="alert alert-danger schd-carbon-modal-alert" style="display:none;" role="alert"></div>
                <div id="schdCarbonResultsPanel" class="carbon-results-panel" style="display:none;">
                    <div class="carbon-results-inner">
                        <div class="carbon-results-header">
                            <h5 class="carbon-results-heading">Emission Breakdown</h5>
                            <span class="carbon-calculated-badge"><i class="fa fa-check" aria-hidden="true"></i> Calculated</span>
                        </div>
                        <div class="carbon-metrics-row">
                            <div class="carbon-metric">
                                <div class="carbon-metric-icon"><i class="fa fa-eye" aria-hidden="true"></i></div>
                                <div class="carbon-metric-label">Total CO<sub>2</sub>e</div>
                                <div class="carbon-metric-value" id="schdCarbonCo2Total">&mdash;</div>
                                <div class="carbon-metric-sub" id="schdCarbonCo2TotalSub"></div>
                            </div>
                            <div class="carbon-metric">
                                <div class="carbon-metric-icon"><i class="fa fa-clock-o" aria-hidden="true"></i></div>
                                <div class="carbon-metric-label">TANK-TO-WHEEL (TTW)</div>
                                <div class="carbon-metric-value" id="schdCarbonCo2Ttw">&mdash;</div>
                                <div class="carbon-metric-sub" id="schdCarbonCo2TtwSub"></div>
                            </div>
                            <div class="carbon-metric">
                                <div class="carbon-metric-icon"><i class="fa fa-line-chart" aria-hidden="true"></i></div>
                                <div class="carbon-metric-label">WELL-TO-TANK (WTT)</div>
                                <div class="carbon-metric-value" id="schdCarbonCo2Wtt">&mdash;</div>
                                <div class="carbon-metric-sub" id="schdCarbonCo2WttSub"></div>
                            </div>
                            <div class="carbon-metric carbon-metric--distance">
                                <div class="carbon-metric-icon"><i class="fa fa-map-marker" aria-hidden="true"></i></div>
                                <div class="carbon-metric-label">Distance</div>
                                <div class="carbon-metric-value" id="schdCarbonDistance">&mdash;</div>
                              
                            </div>
                        </div>
                    </div>
                </div>
                <div id="schdCarbonInfoSections" class="schd-carbon-info-sections" aria-label="Eco calculator information">
                    <div class="schd-carbon-info-card schd-carbon-info-card--purpose is-open">
                        <button type="button" class="schd-carbon-info-toggle" aria-expanded="true">
                            <span class="carbon-info-icon"><i class="fa fa-info" aria-hidden="true"></i></span>
                            <span>Purpose of our Eco Calculator</span>
                            <i class="fa fa-chevron-down schd-carbon-info-chevron" aria-hidden="true"></i>
                        </button>
                        <div class="schd-carbon-info-body">
                            <p>As part of our sustainability strategy and commitment to reducing greenhouse gas emissions, we developed this calculator to support customers and partners in managing their cargo-related carbon footprint. This tool provides an easy way to estimate <strong>CO<sub>2</sub> emissions for shipments</strong>, helping stakeholders make informed decisions toward greener transport solutions. The calculation is based on globally recognized methodologies such as the <strong>GLEC Framework</strong> and <strong>Clean Cargo guidelines</strong>, ensuring transparency and accuracy in reporting.</p>
                        </div>
                    </div>
                    <div class="schd-carbon-info-card schd-carbon-info-card--methodology">
                        <button type="button" class="schd-carbon-info-toggle" aria-expanded="false">
                            <span class="carbon-info-icon"><i class="fa fa-file-text-o" aria-hidden="true"></i></span>
                            <span>Methodology</span>
                            <i class="fa fa-chevron-down schd-carbon-info-chevron" aria-hidden="true"></i>
                        </button>
                        <div class="schd-carbon-info-body">
                            <p>Our calculator estimates emissions based on actual route distances, vessel type, and fuel consumption using standards such as <strong>IMO</strong> and the <strong>GLEC Framework</strong>. Emission factors for fuel types are applied to calculate CO<sub>2</sub> equivalent per shipment, considering cargo weight and distance traveled. The methodology covers only ocean-leg emissions, focusing on tank-to-wheel and optionally well-to-wheel for upstream impacts. Please note that this is an estimate, and actual emissions may vary depending on voyage-specific factors such as weather conditions, port congestion, and operational variations.</p>
                        </div>
                    </div>
                </div>
               
            </div>
        </div>
    </div>
</div>
<% } %>

<form name="booknows" method="post" id="booknows" action="NewBooking.do" class="form-horizontal">
    <input type="hidden" id="index" name="index" />
    <input type="hidden" id="cutoffport" name="cutoffport" />
    <input type="hidden" id="cutoffterminal" name="cutoffterminal" />
    <input type="hidden" id="cutoffdate" name="cutoffdate" />
    <input type="hidden" id="BookingRequestType" name="BookingRequestType" />
    <input type="hidden" id="service" name="service" />
    <input type="hidden" id="vessel" name="vessel" />
    <input type="hidden" id="voyage" name="voyage" />
    <input type="hidden" id="bound" name="bound" />
    <input type="hidden" id="svcType" name="svcType" />
    <input type="hidden" id="depdate" name="depdate" />
    <input type="hidden" id="pol" name="pol" />
    <input type="hidden" id="pod" name="pod" />
    <input type="hidden" id="grpcallid" name="grpcallid" />
    <input type="hidden" id="hidETDDate" name="hidETDDate" />
    <input type="hidden" id="hidINVessel" name="hidINVessel" />
    <input type="hidden" id="hidINServiceCode" name="hidINServiceCode" />
    <input type="hidden" id="hidRoutePOL" name="hidRoutePOL" /> 
    <input type="hidden" id="hidRoutePOD" name="hidRoutePOD" />

</form>
<script type="text/javascript" src="Design/app-assets/js/scroll-custom.js"></script>
<script type='text/javascript'>
                                var scheduleCaptchaRendered = false;
                                function Download(strval)
                                {
                                    document.getElementById("strtype").value = strval;
                                    document.getElementById('theForm').submit();
                                }

                                function booknow(strval, result, cutoffport, cutoffterminal, cutoffdate, service, vessel, voyage, bound, svcType, depdate, pol, pod, grpcallid)
                                {
                                    if (result <= 0)
                                    {
                                        alert("<bean:message key="ecom.msg.bkgfuturedate" bundle="<%=clientcode%>"/>");
                                        return false;
                                    } else
                                    {
                                        document.getElementById("index").value = strval;
                                        document.getElementById("cutoffport").value = cutoffport;
                                        document.getElementById("cutoffterminal").value = cutoffterminal;
                                        document.getElementById("cutoffdate").value = cutoffdate;

                                        document.getElementById("service").value = service;
                                        document.getElementById("vessel").value = vessel;
                                        document.getElementById("voyage").value = voyage;
                                        document.getElementById("bound").value = bound;
                                        document.getElementById("svcType").value = svcType;
                                        document.getElementById("depdate").value = depdate;
                                        document.getElementById("hidETDDate").value = depdate;
                                        document.getElementById("hidINVessel").value = vessel;
                                        document.getElementById("hidINServiceCode").value = service;
                                        document.getElementById("hidRoutePOL").value = pol;
                                        document.getElementById("hidRoutePOD").value = pod;
                                        document.getElementById("pol").value = pol;
                                        document.getElementById("pod").value = pod;
                                        document.getElementById("grpcallid").value = grpcallid;

                                        document.getElementById("BookingRequestType").value = "booknow";
                                        document.getElementById('booknows').action = "NewBooking.do";

                                        var islogin = 'NO';
                                        $.ajax({
                                            url: "ecomAjax?type=checkViaLogin",
                                            type: 'POST',
                                            async: false,
                                            success: function (data) {
                                                console.log('inside booknow ajax ' + data);
                                                islogin = data;
                                                if (data != 'YES') {
                                                    $.ajax({
                                                        url: "LoginPopup.jsp",
                                                        type: 'POST',
                                                        async: false,
                                                        success: function (data) {
                                                            //console.log('inside booknow ajax ' + data);
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
                                                                },
                                                                afterClose: function () {

                                                                },
                                                                keys: {
                                                                    close: null
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
                                            },
                                            error: function (xhr) {
                                                if (xhr.status == 403) {
                                                    window.location.href = "SessionExpired.jsp";
                                                }
                                            }
                                        });
                                        if (islogin == 'YES') {
                                            document.getElementById('booknows').submit();
                                        }
                                    }
                                }
                                function toggleText(transcount, obj, target) {
                                    console.log("toggleText");
                                    if ($(obj).find('i').hasClass('fa-chevron-down')) {
                                        $(obj).html('Hide Details <i class="fa fa-chevron-up"></span>');
                                        console.log("toggleText", target);
    <% if ("Y".equalsIgnoreCase(Enable_Schedule_Calendar)) {%>
                                        $('#locationSearchtable .details_panel').find(target).collapse("show");
                                        $(target).collapse("show");
    <%} else {%>
                                        $(target).collapse("show");
    <%}%>
                                        console.log("Target shown");
                                    } else {
                                        $(obj).html(transcount + ' <i class="fa fa-chevron-down"></span>');
    <% if ("Y".equalsIgnoreCase(Enable_Schedule_Calendar)) {%>
                                        $('#locationSearchtable .details_panel').find(target).collapse("hide");
                                        $(target).collapse("hide");
    <%} else {%>
                                        $(target).collapse("hide");
    <%}%>
                                    }
                                }
                                $(document).ready(function () {

                                    $('#schedulerecaptiid').empty();
                                });
    <% if (scheduleCardCarbonEnabled) { %>
                                $(document).ready(function () {
                                    if (typeof window.initScheduleCarbonIfReady === 'function') {
                                        window.initScheduleCarbonIfReady();
                                    } else if (typeof window.initScheduleCarbon === 'function') {
                                        window.initScheduleCarbon();
                                    }
                                });
    <% } %>

</script>
