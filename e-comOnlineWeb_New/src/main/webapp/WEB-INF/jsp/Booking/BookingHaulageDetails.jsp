<%-- 
    Document   : BookingHaulageDetails
    Created on : 14 Sep, 2023, 12:18:14 PM
    Author     : prakash.mani
--%>
<%@page import="net.ecom.common.DateConversion_New"%>
<%@page import="net.ecom.common.Validation"%>
<%@page import="net.ecom.vo.BkgContainerHaulageDetails"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    ArrayList bkgHaulagedetails = (ArrayList) request.getAttribute("bkgHaulageDetails");
    BkgContainerHaulageDetails bkgHaulageVO = null;
    String eqpId = "", eqptypeid = "", cust_Ref = "", loc_Code = "", loc_Name = "", stop_Seq = "", address = "", TMS_Sche_Pickup = "", TMS_Act_Pickup = "", TMS_Sche_Drop = "", TMS_Act_Drop = "";

%>
<div>
    <div class="table-responsive">
        <table class="table table-striped table-bordered text-center" id="datatableongoingtranscargo">
            <thead>
                <tr>
                    <th><bean:message key="ebooking.haulage.ContainerNo" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.EquipmentType" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.CustomerReference" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.PickupLocationCode" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.PickupLocationName" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.StopSequence" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.Address" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.TMSScheduledPickup" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.TMSActualPickup" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.TMSscheduledDrop" bundle="<%=clientcode%>"/></th>
                    <th> <bean:message key="ebooking.haulage.TMSActualDrop" bundle="<%=clientcode%>"/></th>


                </tr>
            </thead>
            <tbody>
                <%
                    System.out.println("[ECOM-WAR] BookingHaulageDetails Inside if Condition >>>>>>..." + bkgHaulagedetails);
                    if (bkgHaulagedetails != null && bkgHaulagedetails.size() > 0) {
                        System.out.println("[ECOM-WAR] BookingHaulageDetails Inside if Condition >>>>>>...");
                        int size = bkgHaulagedetails.size();
                        for (int i = 0; i < size; i++) {
                            System.out.println("[ECOM-WAR] BookingHaulageDetails Inside if Condition >>>>>>...NO :" + i);
                            bkgHaulageVO = (BkgContainerHaulageDetails) bkgHaulagedetails.get(i);
                            eqpId = Validation.checkNull(bkgHaulageVO.getEqpId()) ;
                            eqptypeid = Validation.checkNull(bkgHaulageVO.getEqptypeid());
                            cust_Ref = Validation.checkNull(bkgHaulageVO.getCustRef());
                            loc_Code = Validation.checkNull(bkgHaulageVO.getLocCode());
                            loc_Name = Validation.checkNull(bkgHaulageVO.getLocName());
                            stop_Seq = Validation.checkNull(bkgHaulageVO.getStopSeq());
                            address = Validation.checkNull(bkgHaulageVO.getAddress());
                            TMS_Sche_Pickup =Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(bkgHaulageVO.getTMS_Sche_Pickup()));
                            TMS_Act_Pickup =Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(bkgHaulageVO.getTMS_Act_Pickup()));
                            TMS_Sche_Drop = Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(bkgHaulageVO.getTMS_Sche_Drop()));
                            TMS_Act_Drop = Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(bkgHaulageVO.getTMS_Act_Drop()));
                %>
                <tr>
                    <td><%=eqpId%></td>
                    <td><%=eqptypeid%></td>
                    <td><%=cust_Ref%></td>
                    <td><%=loc_Code%></td>
                    <td><%=loc_Name%></td>
                    <td><%=stop_Seq%></td>
                    <td><%=address%></td>
                    <td><%=TMS_Sche_Pickup%></td>
                    <td><%=TMS_Act_Pickup%></td>
                    <td><%=TMS_Sche_Drop%></td>
                    <td><%=TMS_Act_Drop%></td>

                </tr>
                <%
                        }
                    }%>
            </tbody>
        </table>
    </div>
    <p>
        <bean:message key="ebooking.haulage.Disclaimer" bundle="<%=clientcode%>"/>
    </p>
</div>
