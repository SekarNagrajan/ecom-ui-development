<%-- 
    Document   : RateContractSurchargView
    Created on : Dec 6, 2017, 3:35:35 PM
    Author     : deepa.p
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List,net.ecom.vo.RateContractSurchargeVO,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,net.ecom.common.GeneralConf" errorPage="" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%    String clientcode = (String) session.getAttribute("clientcode");

    %>
<div class="col-sm-12">
    <div class="table-responsive  mobile-table">
        <table  class="table table-striped table-bordered" id="ratecontractsurchargetable">
            <thead>
                <tr>
                    <th><bean:message key="ecom.surcharge.chargecode" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.surcharge.currency" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.surcharge.amount" bundle="<%=clientcode%>"/></th>
                    <th><bean:message key="ecom.rr.vatos" bundle="<%=clientcode%>"/></th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<RateContractSurchargeVO> summaryList = (List<RateContractSurchargeVO>) request.getAttribute("RATECONTRACTSURCHARGELIST");
                    RateContractSurchargeVO summaryVo = null;
                    if (summaryList != null) {
                        int size = summaryList.size();
                        for (int i = 0; i < size; i++) {
                            summaryVo = (RateContractSurchargeVO) summaryList.get(i);
                %>
                <tr>
                    <td><%=Validation.checkNull(summaryVo.getChargeCode())%></td>
                    <td><%=Validation.checkNull(summaryVo.getCurrency())%></td>
                    <td ><%=Validation.checkNull(summaryVo.getAmount())%></td>
                    <td><%=Validation.checkNull(summaryVo.getVatos())%></td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody> 
        </table>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
         $('#ratecontractsurchargetable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });

</script>