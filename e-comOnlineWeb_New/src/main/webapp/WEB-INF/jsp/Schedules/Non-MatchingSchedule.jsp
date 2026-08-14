<%-- 
    Document   : Non-MatchingSchedule
    Created on : Apr 18, 2020, 9:47:31 PM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.vo.RouteAvailVo,java.util.Hashtable,net.ecom.common.Validation,net.ecom.common.GeneralConf"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%
    List<RouteAvailVo> altschedres = (List) request.getAttribute("ALTSCHEDULE");
    Hashtable portlist = (Hashtable) session.getServletContext().getAttribute("SESSION_PORTLIST");
    String hideMenuIcon = (String) getServletContext().getAttribute(GeneralConf.HIDE_MENU_ICON);
    String clientcode = (String) session.getAttribute("clientcode");
    if (altschedres != null && altschedres.size() > 0) {
%>
<div class="page_bottom_content" id="no_result_schedule">
    <div class="non_matching_sched"> 
        <div class="card non_matching_bg_clr card-shadow">
            <div class="card-body">
                <div class="p-2">
                    <div>
                        <span style="color: #2196F3;font-size: 26px;"><i class="fa fa-times-circle" aria-hidden="true"></i></span>
                        <span style="padding-left: 5px;font-size: 18px;color: #0f5acc;"><bean:message key="ecom.non.matching.msg" bundle="<%=clientcode%>" /></span>
                    </div>

                    <div style="border-bottom: 1px solid;margin: 10px 0px;">
                    </div>

                    <div class="col-sm-12">
                        <%String origin = null, delivery = null;
                            for (RouteAvailVo altVo : altschedres) {
                                System.out.println("[ECOM-WAR] Non_MatchingSchedule Ori and Deli-->" + altVo.getOrigin() + "---" + altVo.getDelivery());
                                origin = altVo.getOrigin() + " - " + Validation.checkNull((String) portlist.get(altVo.getOrigin()));
                                delivery = altVo.getDelivery() + " - " + Validation.checkNull((String) portlist.get(altVo.getDelivery()));
                        %>
                        <div class="row p-t-5 p-b-5">

                            <div class="m-l-15 col-sm-4">
                                <span style="font-size: 15px;"> 
                                    <span><img style="height: 20px;width: 20px;margin-bottom: 5px;" src="Design/assets/images/page_icons/marker.svg"></span>
                                        <%=origin%> 
                                </span>
                            </div>
                            <div class="col-sm-1">
                                <img src="Design/assets/images/page_icons/swap_btn.svg" style="width:30px">
                            </div>
                            <div class="col-sm-4">
                                <span style="font-size: 16px;">
                                    <span> <img style="height: 20px;width: 20px;margin-bottom: 5px;" src="Design/assets/images/page_icons/marker.svg" class=""></span> 
                                        <%=delivery%> 
                                </span>
                            </div>
                            <div class="col-sm-1">
                                <a href="javascript:void(0)" onclick='searchportpair("<%=origin%>", "<%=delivery%>")' class="btn btn-sm" style="color: #fff;background-color: #2196F3;margin-top: -10px;border-radius: 15px;"><i class="fa fa-search"></i></a>
                            </div>
                        </div>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%} else {%>
<div class="page_bottom_content" id='no_result_schedule'>
    <div class="col-sm-12">
        <!--No results msg start  -->
        <div class="row">
            <div class="col-sm-1"> </div>
            <div class="col-sm-10">
                <div class="box_no_rls"> 
                    <div class="text-center" style=" ">
                        <h2 class="txt_no_msg m-b-5" style=""> We don't have any matching schedules for you </h2>
                        <%if (!"Y".equalsIgnoreCase(hideMenuIcon)) {%>
                        <div style="font-size: 18px;" class="m-b-5">Please leave your details,we shall contact you shortly</div>
                        <button type="submit"  onclick="window.location = 'Accesslogin.do?type=contactus';" class="btn btn_block_quote" >Contact us</button>
                        <%}%>
                    </div>
                </div>
            </div>
            <div class="col-sm-2"> </div>
        </div>
        <!--No results msg end  -->
    </div>
</div>

<%}%>

<script type="text/javascript">
    function searchportpair(originport, deliveryport) {
        $('#polcode').val(originport);
        $('#podcode').val(deliveryport);
        $('#searchbylocation').submit();
    }
</script>
