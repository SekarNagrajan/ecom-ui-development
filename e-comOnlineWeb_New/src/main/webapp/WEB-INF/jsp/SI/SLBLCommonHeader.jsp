<%-- 
    Document   : SLBLCommonHeader
    Created on : Nov 5, 2020, 12:14:38 PM
    Author     : gobinath.t
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.HashMap,net.ecom.vo.SIPartiesVO,net.ecom.common.utils.DateConversion_New,net.ecom.common.utils.MaskPortUtil"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
    String clientcode = (String) session.getAttribute("clientcode");
%>

<h6><i class="step-icon fa_header"></i> <bean:message key="ecom.shippingins.headinfo" bundle="<%=clientcode%>"/> </h6>

<fieldset>  	
    <div class="details_search ">

        <div class="header_tle">  <bean:message key="ecom.shippingins.headinfo" bundle="<%=clientcode%>"/>  </div>


        <div class="row form-group">

            <label class="col-sm-2 control-label"> <bean:message key="Tracking.origin" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">
                <html:text property="hid_pla_free_txt" styleId="hid_pla_free_txt" readonly="true"  styleClass="form-control required light_border" maxlength="149"/>

            </div>

            <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">
                <html:text property="pla_free_txt" styleId="pla_free_txt"  styleClass="form-control required light_border" maxlength="149" readonly="true"/>

            </div>

        </div>

        <div class="row form-group">

            <label class="col-sm-2 control-label"> <bean:message key="shipping.portofload.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">
                <html:text property="hid_pol_free_txt" styleId="hid_pol_free_txt" readonly="true"  styleClass="form-control required light_border" maxlength="149"/>
            </div>

            <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">
                <html:text property="pol_free_txt" styleId="pol_free_txt"  styleClass="form-control required light_border" maxlength="149" readonly="true"/>

            </div>

        </div>


        <div class="row form-group">

            <label class="col-sm-2 control-label"> <bean:message key="shipping.portofdischarge.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">
                <html:text property="hid_pod_free_txt" styleId="hid_pod_free_txt" readonly="true" styleClass="form-control required light_border" maxlength="149"/>

            </div>

            <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">

                <html:text property="pod_free_txt" styleId="pod_free_txt" styleClass="form-control required light_border" maxlength="149" readonly="true"/>

            </div>

        </div>


        <div class="row form-group">

            <label class="col-sm-2 control-label"> <bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">
                <html:text property="hid_pld_free_txt" styleId="hid_pld_free_txt" readonly="true" styleClass="form-control required light_border" maxlength="149"/>
            </div>

            <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

            <div class="col-sm-4 ">
                <html:text property="pld_free_txt" styleId="pld_free_txt" styleClass="form-control required light_border" maxlength="149" readonly="true"/>
                <html:hidden property="vslvoyage_free_txt" styleId="vslvoyage_free_txt"></html:hidden>
                </div>

            </div>
        <%
            int transittime = 0;
            HashMap hm_routing = (HashMap) session.getAttribute("bk_port_details");
            SIPartiesVO parties_routing = (SIPartiesVO) hm_routing.get("routing");
            if (parties_routing != null) {
                transittime = DateConversion_New.daysDifferentBetweenTwoDays(parties_routing.getPolat(), parties_routing.getPodat());
             
                boolean isMaskPortHidden = MaskPortUtil.checkIsMaskPort(parties_routing.getPol(), parties_routing.getPod());
               
                String displayReceipt = MaskPortUtil.getDisplayPortName(parties_routing.getPol(),parties_routing.getReceipt());
                String displayDelivery = MaskPortUtil.getDisplayPortName( parties_routing.getPod(),parties_routing.getDelivery());
      
                %>
        
        <div class="header_tle"> <bean:message key="ecom.shippingins.routedet" bundle="<%=clientcode%>"/> </div>
        <div id="siroutingdetails">
            <div id="routingdetails">
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
                                            <dd><strong><%=parties_routing.getPolat()%></strong><input type="hidden" id="hidETDDate" value="<%=parties_routing.getPolat()%>"></dd>
                                            <dd><%=isMaskPortHidden? "":displayReceipt%></dd><input type="hidden" id="OriginPort" value="<%=displayReceipt%>">
                                            <input type="hidden" id="hidRoutePOL" value="<%=parties_routing.getPol()%>">
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
                                            <dd><strong><%=parties_routing.getPodat()%></strong></dd>
                                            <dd><%=isMaskPortHidden? "":displayDelivery%></dd><input type="hidden" id="DeliveryPort" value="<%=displayDelivery%>">
                                            <input type="hidden" id="hidRoutePOD" value="<%=parties_routing.getPod()%>">
                                            </dt>
                                            </td>
                                            <td><img class="content_icon" src="Design/assets/images/page_icons/Lvessel.svg" /></td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="panel_inner_top_3 vessel_info">
                                        <table>
                                            <tr>
                                                <td><img class="header_icon" src="Design/assets/images/page_icons/vessel.svg" /></td>
                                                <td>
                                            <dt>
                                            <dd><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></dd>
                                            <input type="hidden" id="hidINVessel" name="hidINVessel" value="<%=parties_routing.getVesselcode()%>">
                                            <input type="hidden" name="hidINServiceCode" id="hidINServiceCode" value="<%=parties_routing.getServicecode()%>" />
                                            <%if (parties_routing.isIsmaskport()) {%>
                                            <dd><strong id="BKG_vessel_value"><%=isMaskPortHidden? "":parties_routing.getVesselName()%></strong></dd>
                                            <dd><%=isMaskPortHidden? "":parties_routing.getVoyagecode()%><%=isMaskPortHidden? "":parties_routing.getBound()%></dd>
                                            <%}%>
                                            </dt>
                                            </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="panel_inner_top_4" style="width: 20%;">
                                        <table>
                                            <tr>
                                                <td><img class="header_icon" src="Design/assets/images/page_icons/clock.svg" /></td>
                                                <td>
                                            <dt>
                                            <dd>  <bean:message key="ecom.schdl.ttime" bundle="<%=clientcode%>"/> : <strong><%=transittime%> days</strong></dd>
                                            <dd>  <bean:message key="ebooking.Haulage.Origin" bundle="<%=clientcode%>"/> : <strong><%=parties_routing.getTransportOrigin()%></strong></dd>
                                            <dd>  <bean:message key="ebooking.Haulage.Destination" bundle="<%=clientcode%>"/> : <strong><%=parties_routing.getTransportDestination()%></strong></dd>
                                            </dt>
                                            </td>
                                            </tr>
                                        </table>

                                    </div>

                                </div> 

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
       
    </div>

</fieldset> 