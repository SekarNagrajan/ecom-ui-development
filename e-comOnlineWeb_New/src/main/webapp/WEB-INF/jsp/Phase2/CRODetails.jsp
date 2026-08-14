<%-- 
    Document   : CRODetails
    Created on : Jan 31, 2018, 10:54:41 AM
    Author     : deepa.p
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.phase2.service.vo.CRONumbers,java.util.Hashtable,net.ecom.common.Validation,net.ecom.phase2.service.vo.CROSummaryVO,java.util.Map,net.ecom.common.utils.DateConversion_New"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<% String clientcode = (String) (String)session.getAttribute("clientcode");
%>
<tr>
<td colspan="12">
    <table border="2" width="100%" class="table table-condensed table-striped table-bordered table-hover dataTable">
        <tr bgcolor='#5bc0de' style='background-color:#5bc0de'>
            <td><bean:message key="ecom.cro.relnum" bundle="<%=clientcode%>"/></td>
            <td><bean:message key="ecom.cro.croval" bundle="<%=clientcode%>"/></td>
            <td><bean:message key="ecom.cro.crodate" bundle="<%=clientcode%>"/></td>
            <td><bean:message key="ecom.cro.polarr" bundle="<%=clientcode%>"/></td>
            <td><bean:message key="ecom.cro.poldept" bundle="<%=clientcode%>"/></td>
            <td><bean:message key="ecom.cro.cntrtype" bundle="<%=clientcode%>"/></td>
            <td><bean:message key='ecom.cro.qtyreleased' bundle="<%=clientcode%>"/></td>
            <td><bean:message key='ebooking.remarks.display' bundle="<%=clientcode%>"/></td>
            <td><bean:message key='ecom.cro.print' bundle="<%=clientcode%>"/></td>
        </tr>
        <%
            String agencyCode=(String)request.getAttribute("CROAGENCYCODE");
            List<CROSummaryVO> summaryList = (List<CROSummaryVO>) request.getAttribute("CRODETAILS");
            CROSummaryVO summaryVo = null;
            if (summaryList != null && summaryList.size() > 0) {
                for (int i = 0; i < summaryList.size(); i++) {
                    summaryVo = summaryList.get(i);


        %>
        <tr>
            <td><%=Validation.checkNull(summaryVo.getConrelno())%></td>
            <td><%=DateConversion_New.convertDefaultFormat(summaryVo.getConvaliditydate())%></td>
            <td><%=DateConversion_New.convertDefaultFormat(summaryVo.getConreldate())%></td>
            <td><%=DateConversion_New.convertDefaultTimeFormat(summaryVo.getArrdate())%></td>
            <td><%=DateConversion_New.convertDefaultTimeFormat(summaryVo.getDepdate())%></td>
            <td><%=Validation.checkNull(summaryVo.getEquipmenttype())%></td>
            <td><%=Validation.checkNull(summaryVo.getQty())%></td>
            <td><%=Validation.checkNull(summaryVo.getRemarks())%></td>
            <td><a href="javascript:void(0);" onClick="printCRO('<%=Validation.checkNull(summaryVo.getConrelno())%>', '<%=agencyCode%>');">
                <i class="fa fa-print"></i></a></td>
        </tr>
        <%            }
        } else {
        %>
        <tr  bgcolor='#FFFFFF' > <td colspan="7">No records Available</td></tr>
        <%}%>
    </table>
</td>
</tr>