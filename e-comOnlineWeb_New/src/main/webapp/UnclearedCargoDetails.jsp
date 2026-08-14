<%-- 
    Document   : UnclearedCargoDetails
    Created on : 4 Oct, 2019, 10:12:08 AM
    Author     : priyadharshinim
--%>
<%@page import="java.util.ArrayList,java.util.Hashtable,net.ecom.vo.UnclearedCargoVo,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String blNumber = "";
    String loadport = "";
    String dischargeport = "";
    String vessArrDate = "";
    String actCont = "";
    String delCont = "";
    String remCont = "";
    int remcunt = 0;

    ArrayList daslis = (ArrayList) session.getAttribute("UnclearedCargoDetails");
    UnclearedCargoVo dasvo = null;
    Hashtable portlist = (Hashtable) request.getServletContext().getAttribute("SESSION_PORTLIST");
    String clientcode = (String) session.getAttribute("clientcode");

%>

<div class="table-responsive" style="max-height: 250px;height: 250px;padding: 10px;">
    <table class="table table-hover mb-0 ps-container ps-theme-default table_text_center" id="datatablebyunclearedcargo">
        <thead>
            <tr>
              <th><bean:message key="Tracking.blnum" bundle="<%=clientcode%>"/></th>
              <th><bean:message key="shipping.pol.label" bundle="<%=clientcode%>"/></th>
              <th><bean:message key="shipping.pod.label" bundle="<%=clientcode%>"/></th>
              <th><bean:message key="Tracking.eta" bundle="<%=clientcode%>"/></th>
              <th><bean:message key="ecom.dashb.actcontcnt" bundle="<%=clientcode%>"/></th>
              <th><bean:message key="ecom.dashb.delcontcnt" bundle="<%=clientcode%>"/></th>
              <th><bean:message key="ecom.dashb.remcontcnt" bundle="<%=clientcode%>"/></th>
           
</tr>

        </thead>
        <tbody>
            <%
                if (daslis != null && daslis.size() > 0) {
                    int size = daslis.size();
                    for (int i = 0; i < size; i++) {
                        dasvo = (UnclearedCargoVo) daslis.get(i);
                        blNumber = Validation.checkNull(dasvo.getBlnumber());
                        loadport = Validation.checkNull(dasvo.getLoadport());
                        dischargeport = Validation.checkNull(dasvo.getDischargeport());
                        vessArrDate = Validation.checkNull(dasvo.getVesselarrdate());
                        actCont = Validation.checkNull(dasvo.getActcontrcnt());
                        delCont = Validation.checkNull(dasvo.getDelcontrcnt());
                        remCont = Validation.checkNull(dasvo.getRemcontrcnt());
                        remcunt = Integer.parseInt(remCont);
                        loadport = loadport + " - " + portlist.get(loadport);
                        dischargeport= dischargeport + " - " + portlist.get(dischargeport);
                        vessArrDate = DateConversion_New.convertDefaultFormatwithTime(vessArrDate);
                        if (remcunt > 0) {
            %>
            <tr>
                <td class="table_text_highlight"><%=blNumber%></td>
                <td><%=loadport%></td>
                <td><%=dischargeport%></td>
                <td><%=vessArrDate%></td>
                <td><%=actCont%></td>
                <td><%=delCont%></td>
                <td><%=remCont%></td>
            </tr>
            <%
                        }
                    }
                }%>
        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#datatablebyunclearedcargo').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []

        });

    });
</script>