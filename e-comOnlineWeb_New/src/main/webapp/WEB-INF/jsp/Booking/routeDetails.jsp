<%-- Route Details JSP - Extracted from ebookingcrmnew.jsp --%>
<%@page import="java.util.ArrayList,java.util.Map,net.ecom.vo.Route,net.ecom.vo.CutoffConfigVO,net.ecom.common.Validation,net.ecom.common.MaskPort,net.ecom.common.utils.MaskPortUtil,net.ecom.common.utils.DateConversion_New,net.ecom.common.GeneralConf"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
    // Get required variables from session/request/servletContext
    String clientcode = (String) session.getAttribute("clientcode");
    ArrayList routeDetails = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");
    CutoffConfigVO cutOffVO = (CutoffConfigVO) request.getAttribute("ROUTECUTOFFVO");
    boolean isbkgcutoff = false;
    if (request.getAttribute("ISBKGCUTOFF") != null) {
        isbkgcutoff = (boolean) request.getAttribute("ISBKGCUTOFF");
    }
    String Display_LCT_CUTOFF = (String) request.getServletContext().getAttribute(GeneralConf.Display_LCT_CUTOFF);
    Map<String, String> temrminalMap = (Map<String, String>) getServletContext().getAttribute("TEMINALSMAPCONTEXT");
%>
<!-- Change Route Deatils start  -->
<input type="hidden"  id="selectroute" name="selectroute" title="Select Route" />
<html:hidden property="trpservice" styleId="trpservice" value=""/>
<html:hidden property="servicehd" styleId="servicehd" value=""/>
<html:hidden property="servicehdedit" styleId="servicehdedit"/>
<%

    if (routeDetails != null && routeDetails.size() > 0) {
        String INServiceCode = "";
%>
<div class="col-sm-12" id="routingdetails">
    <div class="header_tle">  <bean:message key="ecom.bkg.rutdet" bundle="<%=clientcode%>"/>  </div>
    <a onclick ="getRouteDetails()" id="routehref" href="javascript:void(0)" class="btn btn-warning text-white"><bean:message key="ecom.bkg.changerut" bundle="<%=clientcode%>"/> </a>
    <%
        int transittime = 0, transpcount = 0;
        boolean isInlandMve = false;
        String vesselfeeder = null, polName = null, podName = null, voyage = null, etd = null, eta = null, bound = null, str_type = "N", strServiceCode = null, vessel = null, maskport_trans1orpod = "N", pol = null, pod = null;;
 
         boolean ismaskporthdn  = false;
        for (int i = 0; i < routeDetails.size(); i++) {
            Route list = (Route) routeDetails.get(i);
            if ("N".equalsIgnoreCase(maskport_trans1orpod) && "Y".equalsIgnoreCase(str_type)) {
                maskport_trans1orpod = list.getLoadport();
            }
            if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(list.getLegType()) || "F".equalsIgnoreCase(list.getLegType()))) {
                polName = list.getPolPortName();
                voyage = list.getVoyage();
                bound = list.getBound();
                etd = list.getETD1();
                if (((!isbkgcutoff) && cutOffVO != null) && "Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                    cutOffVO.setFullContClosing(Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(list.getClosingtime())));
                }
                str_type = "Y";
                vessel = list.getVessel();
                INServiceCode = list.getService();
                if ("Feeder".equalsIgnoreCase(list.getLegtypename()) && (list.getVesselName() == null || list.getVesselName().toLowerCase().contains("dummy"))) {
                    vesselfeeder = "Feeder";
                } else {
                    vesselfeeder = list.getVesselName();
                }
                pol = list.getLoadport();
            }
            strServiceCode = list.getService();
            if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                podName = list.getPodPortName();
                eta = list.getETA1();
                pod = list.getDisport();
            }
            if (!(strServiceCode != null && strServiceCode.trim().length() > 0)) {
                isInlandMve = true;
            }
            if (i == 0 || i == routeDetails.size() - 1) {
                if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                    transpcount++;
                }
            } else {
                transpcount++;
            }
        }
        transittime = DateConversion_New.daysDifferentBetweenTwoDays(etd, eta);
        if ("N".equalsIgnoreCase(maskport_trans1orpod)) {
            maskport_trans1orpod = pod;
        }
 
         ismaskporthdn  = MaskPortUtil.checkIsMaskPort(pol, maskport_trans1orpod);
         String polMportName = MaskPortUtil.getMportNameByActualPort(pol);
         String displayPolName = (polMportName != null && !polMportName.isEmpty()) ? polMportName : polName;
         String podMportName = MaskPortUtil.getMportNameByActualPort(pod);
         String displayPodName = (podMportName != null && !podMportName.isEmpty()) ? podMportName : podName;
    %>
    <div class="form-group">
        <div class="row">
            <div class="col-xs-12 col-sm-12">
                <div class="panel_inner_left txt_box">
                    <div class="panel_inner_top">
                        <div class="panel_inner_top_1">
                            <table>
                                <tr>
                                    <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                    <td>
                                <dt>
                                <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd>
                                <dd><strong><%=etd%></strong></dd>
                                <dd><%=ismaskporthdn ? "" : displayPolName%></dd>
                                </dt>
                                </td>
                                <td><img class="content_icon" src="Design/assets/images/page_icons/Rvessel.svg" /></td>
                                </tr>
                            </table>
                        </div>
                        <div class="panel_inner_top_2">
                            <table>
                                <tr>
                                    <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                    <td>
                                <dt>
                                <dd><bean:message key="ecom.schdl.arrv" bundle="<%=clientcode%>"/></dd>
                                <dd><strong><%=eta%></strong></dd>
                                <dd><%=ismaskporthdn ? "" : displayPodName%></dd>
                                </dt>
                                </td>
                                <td><img class="content_icon" src="Design/assets/images/page_icons/Lvessel.svg" /></td>
                                </tr>
                            </table>
                        </div>
                        <div class="panel_inner_top_3 bkg_vessel_info">
                            <table>
                                <tr>
                                    <td><img class="header_icon" src="Design/assets/images/page_icons/vessel.svg" /></td>
                                    <td>
                                <dt>
                                <dd><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></dd>
                               
                                <dd><strong id="BKG_vessel_value"><%=ismaskporthdn ? "" : vesselfeeder%></strong></dd>
                                <dd><%=ismaskporthdn ? "" : voyage%><%=ismaskporthdn ? "" : bound%></dd>
                                
                                
                              
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
                                <dd><bean:message key="ecom.schdl.ttime" bundle="<%=clientcode%>"/></dd>
                                <dd><strong><%=transittime%> days</strong></dd>
                                </dt>
                                </td>
                                </tr>
                            </table>

                        </div>
                        <%if (transpcount > 1) {%>
                        <div id="show_details" style="cursor: pointer" class="show_details_class_book" onclick="toggleText('<%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>)', this, '#bkgshowDetails');"><%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>) <i class="fa fa-chevron-down"></i></div>       
                            <%} else {%>
                        <div id="show_details" class="show_details_class_book" <%if (isInlandMve) {%> style="cursor: pointer" onclick="toggleText('Multimodal Shipment ', this, '#bkgshowDetails')" <%}%>><%= isInlandMve ? "Multimodal Shipment<i class='fa fa-chevron-down'></i>" : "Direct Shipment"%></div>
                        <%}%>

                    </div> 
                    <div id="bkgshowDetails" class="collapse show_details">
                        <div class="timeline">
                            <%

                                String legType = "";
                                for (int j = 0; j < routeDetails.size(); j++) {
                                    Route dto = (Route) routeDetails.get(j);
                                    vesselfeeder = dto.getVesselName();
                                    if ((vesselfeeder == null || vesselfeeder.toLowerCase().contains("dummy")) && dto.getLegtypename() != null && "Feeder".equalsIgnoreCase(dto.getLegtypename())) {
                                        vesselfeeder = "Feeder";
                                    } else {
                                        vesselfeeder = dto.getVesselName();
                                    }
                                    if (dto.getLegtypename() != null && ("Feeder".equalsIgnoreCase(dto.getLegtypename()) || "Mainline".equalsIgnoreCase(dto.getLegtypename()))) {
                                        legType = "vessel";
                                    } else {
                                        legType = dto.getLegtypename();
                                    }
                                    System.out.println("[ECOM-WAR] ebookingcrmnew legType--> " + legType);
                                  
                                    ismaskporthdn = MaskPortUtil.checkIsMaskPort(dto.getLoadport(), dto.getDisport());
                            %>
                            <div class="show_details_departure show_details_line">
                                <%if (j == 0) {%>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <span class="port_name"><%= MaskPortUtil.getDisplayPortName(dto.getLoadport(), dto.getPolPortName())%></span>
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
                                            
											 
                                            <dd><strong><%=ismaskporthdn ? "" : vesselfeeder%></strong></dd>
                                            
                                            </dt>
                                            </td>
                                            <%if ("Vessel".equalsIgnoreCase(legType)) {
									  if (!ismaskporthdn) {%>                                                                        
										<td>
                                            <dt>
                                            <dd><bean:message key="tracking.Service" bundle="<%=clientcode%>"/></dd>
                                            <dd><strong><%=dto.getServiceName()%></strong></dd>
                                            </dt>
                                            </td>
                                            <td>
                                            <dt>
                                            <dd><bean:message key="ecom.shippingins.VoyageorBound" bundle="<%=clientcode%>"/></dd>
                                            <dd><strong><%=dto.getVoyage()%><%=dto.getBound()%></strong></dd>
                                            </dt>
                                            </td>
                                            <%}
                                         }%>
                                            </tr>
                                        </table>
                                    </div>
                                    <%}%>
                                </div>
                            </div>
                            <%if (j != 0) {%>
                        </div>
                        <%}%>
                        <%if (j != routeDetails.size() - 1) {%>
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

                            <%                    }// j close %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%
        if (isbkgcutoff && cutOffVO != null) {
    %>
    <div class="">
        <table class="table table-striped table-bordered">
            <tbody>
                <tr>
                    <td><bean:message key="ecom.bkg.bkgcutoff" bundle="<%=clientcode%>"/></td>
                    <td><%=Validation.checkNull(cutOffVO.getBkgcutoff())%><input type="hidden" name="ebookbkgcutoff" value="<%=cutOffVO.getBkgcutoff()%>"></td>
                    <td><bean:message key="ecom.bkg.doccutoff" bundle="<%=clientcode%>"/></td>
                    <td><%=Validation.checkNull(cutOffVO.getDoccutoff())%><input type="hidden" name="ebookdoccutoff" value="<%=cutOffVO.getDoccutoff()%>"></td>
                </tr>
                <tr>
                    <td><bean:message key="ecom.bkg.vgmcutoff" bundle="<%=clientcode%>"/></td>
                    <td><%=Validation.checkNull(cutOffVO.getVgmcutoff())%><input type="hidden" name="ebookvgmcutoff" value="<%=cutOffVO.getVgmcutoff()%>"></td>
                    <td><bean:message key="ecom.bkg.portentrycutoff" bundle="<%=clientcode%>"/></td>
                    <td><%=Validation.checkNull(cutOffVO.getPortentrycutoff())%><input type="hidden" name="ebookportentrycutoff" value="<%=cutOffVO.getPortentrycutoff()%>"></td>

                </tr>
                <tr>
                    <td><bean:message key="ecom.bkg.hazcutoff" bundle="<%=clientcode%>"/></td>
                    <td><%=Validation.checkNull(cutOffVO.getHazcutoff())%><input type="hidden" name="ebookhazcutoff" value="<%=cutOffVO.getHazcutoff()%>"></td>
                        <%if (cutOffVO.getBbrorocutoff() != null && cutOffVO.getBbrorocutoff().trim().length() > 0) {%>
                    <td><bean:message key="ecom.bkg.bbrorocutoff" bundle="<%=clientcode%>"/></td>
                    <td><%=ismaskporthdn ? "" : Validation.checkNull(cutOffVO.getBbrorocutoff())%><input type="hidden" name="ebookbrorocutoff" value="<%=cutOffVO.getBbrorocutoff()%>"></td>
                        <%}%>
                </tr>
            </tbody>
        </table>
    </div>
    <%} else if (cutOffVO != null) {%>
    <div class="">
        <table class="table table-striped table-bordered">
            <tbody>
                <tr>
                    <td><bean:message key='ebooking.prev.docclosing' bundle="<%=clientcode%>"/></td>
                    <td><%=cutOffVO.getDocClosing()%></td>
                    <td>
                        <%if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {%>
                        <bean:message key='Schedule.View.LCT' bundle="<%=clientcode%>"/>
                        <%} else {%>
                        <bean:message key='ebooking.prev.cfcclosing' bundle="<%=clientcode%>"/>
                        <%}%>
                    </td>
                    <td><%=cutOffVO.getFullContClosing()%></td>
                </tr>
                <tr>
                    <td><bean:message key='ebooking.prev.vgmclosing' bundle="<%=clientcode%>"/></td>
                    <td><%=cutOffVO.getVgmClosing()%></td>
                    <td><bean:message key='ebooking.prev.cfsclosing' bundle="<%=clientcode%>"/></td>
                    <td><%=cutOffVO.getCfsGateClosing()%></td>
                </tr>
                <tr>
                    <td><bean:message key='ebooking.prev.edidecclosing' bundle="<%=clientcode%>"/></td>
                    <td><%=cutOffVO.getEdiDecClosing()%></td>
                    <td><bean:message key='ebooking.prev.deptterminal' bundle="<%=clientcode%>"/></td>
                    <td><%=ismaskporthdn ? "" :Validation.checkNull(temrminalMap.get(cutOffVO.getTerminal()))%></td>
                </tr>
            </tbody>
        </table>
    </div>
    <%}%>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#hidINVessel').val('<%=vessel%>');
        $('#hidINServiceCode').val('<%=INServiceCode%>');
        $('#hidRoutePOL ').val('<%=pol%>');
        $('#hidRoutePOD').val('<%=pod%>');


    });
</script>
<%} else {%>
<div class="col-sm-12" id="routingdetails"></div>
<%}%>

<!-- Change Route Deatils End  -->

