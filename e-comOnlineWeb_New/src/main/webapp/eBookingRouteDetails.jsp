<%@page import="net.ecom.common.MaskPort"%>
<%@page import="net.ecom.common.utils.MaskPortUtil"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.*,net.ecom.vo.Route,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,net.ecom.common.GeneralConf" errorPage="" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%    String clientcode = (String) session.getAttribute("clientcode");
    String enableScheduleDisclaimer = (String) getServletContext().getAttribute(GeneralConf.ENABLE_SCHEDULE_DISCLAIMER);
    String scheduleDisclaimerMessage = (String) getServletContext().getAttribute("ScheduleDisclaimerMessage");
%>

<div class="col-sm-12">
    <span class="lbl_clr_txt">
        <%if ("Y".equalsIgnoreCase(enableScheduleDisclaimer) && scheduleDisclaimerMessage != null && !scheduleDisclaimerMessage.isEmpty()) {%>
        <%=scheduleDisclaimerMessage%>
        <%} else {%>
        <bean:message key="ecom.msg.schddate" bundle="<%=clientcode%>"/>
        <%}%>
        <span class='pull-right'><span class="fa fa-schd_default_icon" title="Default Route"></span> <bean:message key="Schedule.View.Defaultroute" bundle="<%=clientcode%>"/></span> </span>
    <%
        String v1DataEnableStatus = (String) getServletContext().getAttribute(GeneralConf.V1DATAENABLESTATUS);
        Map<String, String> temrminalMap = (Map<String, String>) getServletContext().getAttribute("TEMINALSMAPCONTEXT");
        List routeInfo = (ArrayList) session.getAttribute("EBOOKING_ROUTING_INFO");
        ArrayList routeArray1 = null;
        String serviceCode = null, vesselCode = null, voyageCode = null, boundCode = null, serviceType = null, grpcallid = null, pol = null, pod = null, polTer = null, polTerLen = null, podTer = null, podTerLen = null;
        boolean flag = false;
       
        boolean ismaskporthdn = false;
        String cutOffPort = null, cutOffTerminal = null, cutOffArrDate = null;
        String hidDeptDate = null;
        if (routeInfo != null && routeInfo.size() > 0) {
            for (int k = 0; k < routeInfo.size(); k++) {
                routeArray1 = (ArrayList) routeInfo.get(k);
                if (routeArray1 != null && routeArray1.size() > 0) {
                    flag = true;
                }
            }
        }
        System.out.println("[ECOM-WAR] eBookingRouteDetails routeInfo=> " + routeInfo + " , " + flag);
        if (routeInfo != null && routeInfo.size() > 0 && flag == true) {
            int cc = 0;
            int count = 0;
            cc = 0;
            for (int m = 0; m < routeInfo.size(); m++) {
                String defroute = "", polname = "", podname = "", vessel = "", voy = "", service = "", arrdate = "", depdate = "", loadtype = "Single", bound = "";
                ArrayList routeArray = (ArrayList) routeInfo.get(m);
                Route eBookingrouting = null;
                String str_type = "N";
                String maskport_trans1orpod = "N";
                String strServiceCode = null;
                int result = 0, transittime = 0, transpcount = 0;
                boolean isInlandMve = false;
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
                            hidDeptDate = depdate.split(" ")[0];
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
                            pol = eBookingrouting.getLoadport();
                           // System.out.println("[ECOM-WAR] eBookingRouteDetails >>pol>>>" + pol);
                            String polMportName = MaskPortUtil.getMportNameByActualPort(pol);
                            polTer = (polMportName != null && !polMportName.isEmpty()) ? polMportName : Validation.checkNull(temrminalMap.get(eBookingrouting.getBlpolTer()));
                            if ("Feeder".equalsIgnoreCase(eBookingrouting.getLegtypename()) && (vessel == null || vessel.toLowerCase().contains("dummy"))) {
                                vessel = "Feeder";
                            }

                        }
                        strServiceCode = eBookingrouting.getService();
                        if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                            polname = polname != null && polname.length() > 0 ? polname : eBookingrouting.getPolPortName();
                            arrdate = eBookingrouting.getETA1();
                            podname = eBookingrouting.getPodPortName();
                            pod = eBookingrouting.getDisport();
                            String podMportName = MaskPortUtil.getMportNameByActualPort(pod);
                            podTer = (podMportName != null && !podMportName.isEmpty()) ? podMportName : Validation.checkNull(temrminalMap.get(eBookingrouting.getBlpodTer()));
                        }
                        System.out.println("[ECOM-WAR] eBookingRouteDetails >>service>" + service 
                        +" >>>polname>>>" + polname);
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
                    if ("N".equalsIgnoreCase(maskport_trans1orpod)) {
                        maskport_trans1orpod = pod;
                    }
                  
                     ismaskporthdn  = MaskPortUtil.checkIsMaskPort(pol, maskport_trans1orpod);  
  %>

    <div class="page_scrol_data">
        <div class="details_panel" id="row<%=cc%>">
            <div class="panel_inner_content">
                <%if (!ismaskporthdn) {%>
                <div class="panel_inner_service_note">
                    <div class='m-l-12'>
                        <%if ("Y".equalsIgnoreCase(defroute)) {%>
                        <i class="fa fa-schd_default_icon" title="Default Route"></i>
                        <%}%>
                        <span class="<%="Y".equalsIgnoreCase(defroute) ? "" : "m-l-30"%>"><%=service + " (" + serviceCode + ")"%></span>

                       
                    </div>
                </div>
                <%}%>
                <div class="panel_inner_left">
                    <div class="bkg_panel_inner_top remove_brdr_btm">
                        <div class="panel_inner_top_1">
                            <table>
                                <tr>
                                    <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                    <td>
                                <dt>
                                <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd>
                                <dd><strong>
                                        <%=depdate%>
                                    </strong></dd>
                                </dt>
                                </td>
                                <td style="padding: 0;"><img class="content_icon" src="Design/assets/images/page_icons/Rvessel.svg" /></td>
                                </tr>
                              
                                <tr>
                                    <td></td>
                                    <td colspan="2" style="padding-left: 0;font-size: 11px;">
                                        <span title="<%= ismaskporthdn?"" :polTer%>"><%= ismaskporthdn?"" :polTer%></span>                                    
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
                                <dd><strong><%=arrdate%></strong></dd>
                                </dt>
                                </td>
                                <td style="padding: 0;"><img class="content_icon" src="Design/assets/images/page_icons/Lvessel.svg" /></td>
                                </tr>
                                
                                <tr>
                                    <td></td>
                                    <td colspan="2" style="padding-left: 0;font-size: 11px;">
                                        <span title="<%= ismaskporthdn?"" :podTer%>"><%= ismaskporthdn?"" :podTer%></span>                                    
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
                                
                                <dd><strong><%= ismaskporthdn ? "":vessel%></strong></dd>
                                <dd><%=ismaskporthdn ? "":voy%><%=ismaskporthdn ? "":bound%></dd>
                               
                            
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
                    </div>
                </div>
                <div class="bkg_panel_inner_right">
                    <button class="btn btn btn-warning text-white" id="999" onclick="closeevent(this.id, '<%=m%>', '<%=result%>', '<%=eBookingrouting.getRouteId()%>', '<%=hidDeptDate%>', '<%=cutOffPort%>', '<%=cutOffTerminal%>', '<%=cutOffArrDate%>', '<%=serviceCode%>', '<%=vesselCode%>', '<%=voyageCode%>', '<%=boundCode%>', '<%=serviceType%>', '<%=grpcallid%>', '<%=pol%>', '<%=pod%>');"><bean:message key="Schedule.View.select" bundle="<%=clientcode%>"/></button>
                    <div class="bkgtransshipment">
                        <%if (transpcount > 1) {%>

                        <div id="show_details" style="cursor: pointer" class="show_details_class" onclick="toggleText('<%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>)', this, '#showDetails<%=m%>')"><%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>) <i class="fa fa-chevron-down"></i></div>
                            <%} else {%>
                        <div id="show_details" <%if (isInlandMve) {%> style="cursor: pointer" onclick="toggleText('Multimodal Shipment ', this, '#showDetails<%=m%>')" <%}%>><%= isInlandMve ? "Multimodal Shipment<i class='fa fa-chevron-down'></i>" : "Direct Shipment"%></div>

                        <%}%>
                    </div>
                </div>
            </div>

            <div id="showDetails<%=m%>" class="collapse show_details">
                <div class="timeline">
                    <%
                        String vesselfeeder = "";
                        String legType = "";
                       
                        for (int j = 0; j < arraySize; j++) {
                            Route dto = (Route) routeArray.get(j);
                            count++;
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
                            System.out.println("[ECOM-WAR] eBookingRouteDetails legType--> " + legType);
                           
                            ismaskporthdn = MaskPortUtil.checkIsMaskPort(dto.getLoadport(), dto.getDisport());
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
                                <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd></dd>
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
                                    
                                    <dd><strong><%=ismaskporthdn ? "":vesselfeeder%></strong></dd>
                                    
                                   
                                  
                                    </dt>
                                    </td>
                                    <%if ("Vessel".equalsIgnoreCase(legType)) {
									if(!ismaskporthdn){%>								
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


    <%
            } // routeArray close

        } // i close
    %>

    <%                    } else {

    %>
    <div class="details_panel card card-body card-shadow d-block" align="center">
        <span>No records found</sapn>
    </div>
    <%                            }%>
</div>

<script type='text/javascript'>
    var checkv1enablestatus = '<%=v1DataEnableStatus%>';
    function closeevent(ids, indexid, result, routeid, depdate, cutoffport, cutoffterminal, cutoffdate, service, vessel, voyage, bound, svcType, grpcallid, pol, pod) {
        var appVersion = parent.document.getElementById('appVersion').value;
        var bkgType = parent.document.getElementById('hidbkgtype').value;
        var routeVersion;
        if (result <= 0)
        {
            alert("<bean:message key="ecom.msg.bkgfuturedate" bundle="<%=clientcode%>"/>");
            return false;
        } else
        {
            var $row = $("#" + ids).closest("tr");
            var $text = $row.text();
            var lines = $text.split('\n');
            var newtext = $.each(lines, function (n, splitdata) {
                return $.trim(splitdata);
            }).join("</td><td>");
            $.ajax({
                type: "POST",
                url: "RouteAjax?routeid=" + indexid + "&cutoffport=" + encodeURIComponent(cutoffport) + "&cutoffterminal=" + encodeURIComponent(cutoffterminal) + "&cutoffdate=" + encodeURIComponent(cutoffdate) + "&type=select&grpcallid=" + grpcallid + "&pol=" + pol + "&pod=" + pod,
                async: false,
                success: function (response) {
                    var routinghtml = response;
                    $.ajax({
                        type: "POST",
                        url: "RouteAjax?&type=version&depdate=" + encodeURIComponent(depdate) + "&service=" + encodeURIComponent(service) + "&vessel=" + vessel + "&voyage=" + voyage + "&bound=" + bound + "&svcType=" + svcType + "&appVersion=" + appVersion + "&bkgType=" + bkgType,
                        async: false,
                        success: function (response) {
                            routeVersion = response;
                            if (checkv1enablestatus == 'Y' && bkgType == 'amendbkg') {
                                if (routeVersion == appVersion) {
                                    parent.setSelectedRoute(ids, depdate, routeVersion, vessel, service, pol, pod);
                                    $('div#routingdetails td:nth-child(n+15)').hide();
                                    $(".routingdiv").removeClass("hidden");
                                    $("#ebook_routeid").val(routeid);
                                    $("#routingdetails").html(routinghtml);
                                } else {
                                    alert(routeVersion);
                                }
                            } else {
                                parent.setSelectedRoute(ids, depdate, routeVersion, vessel, service, pol, pod);
                                $('div#routingdetails td:nth-child(n+15)').hide();
                                $(".routingdiv").removeClass("hidden");
                                $("#ebook_routeid").val(routeid);
                                $("#routingdetails").html(routinghtml);
                            }

                        },
                        error: function (xhr) {
                            if (xhr.status == 403) {
                                window.location.href = "SessionExpired.jsp";
                            }
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
    }
    function setSelectedRoute(userText, depdate, routeVersion, vessel, service, pol, pod) {
        $.fancybox.close();
        $('#selectroute').val(userText);
        $('#hidETDDate').val(depdate);
        $('#hidINVessel').val(vessel);
        $('#hidINServiceCode').val(service);
        $('#hidRoutePOL').val(pol);
        $('#hidRoutePOD').val(pod);
        $('#appversionchange').val(parent.document.getElementById('appVersion').value);
        $('#appVersion').val(routeVersion);
        var dischek = $('#dischek').val();
        if (dischek == 'template' && $('#datatablebyhaulage').length > 0) {
            var depdatearr = depdate.split(" ");
            var depdate1 = depdatearr[0] + " 00:00";
            $('#datatablebyhaulage').find('tbody').find('tr').each(function () {
                $(this).find('td:nth-child(8)').html(depdate1);
            });
        }
    }
    function toggleText(transcount, obj, target) {
        if ($(obj).find('i').hasClass('fa-chevron-down')) {
            $(obj).html('<bean:message key="ecom.schd.hidedet" bundle="<%=clientcode%>"/><i class="fa fa-chevron-up"></span>');
            $(target).collapse("show");
        } else {
            $(obj).html(transcount + ' <i class="fa fa-chevron-down"></span>');
            $(target).collapse("hide");
        }
    }

</script>
