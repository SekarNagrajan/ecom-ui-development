<%-- 
    Document   : RateContractView_New
    Created on : Feb 10, 2021, 3:41:53 PM
    Author     : gobinath.t
--%>

<%@page import="java.util.Map,java.util.Set,java.util.Iterator,java.util.List,java.util.stream.Collectors,net.ecom.vo.ContractVo,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,java.util.HashMap,java.util.Hashtable,net.ecom.common.Currencyformat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Map contractMap = (Map) request.getAttribute("RATECONTRACTNEWMAP");
    Set chargecodeset = (Set) contractMap.get("Chargecodeset");
    Map contractdetails = (Map) contractMap.get("ContractDetails");
    Map applicablecharges = (Map) contractMap.get("ApplicableCharges");
    Map commoditynamelis = (Map) contractMap.get("commoditynamelis");
    Set classset = (Set) contractMap.get("ClassDetailsSet");
    Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
    String classname = null;
    Map appcharge = null;
    List comlis = null;
    if (classset != null) {
        Iterator<String> c = classset.iterator();
        while (c.hasNext()) {
            classname = c.next();
            comlis = (List) commoditynamelis.get(classname);
%>
<div>
    <div class="table-responsive card card-body card-shadow">     
        <span class="text-info mousehover_note"> * To view the full content, place the mouse cursor on the text.</span>
        <div><strong> Class : <%=classname%> </strong>- <%= (comlis.stream().map(Object::toString).collect(Collectors.joining(",")))%></div>
        <div class="col-sm-12"><div class="input-color float-right" style="margin-right: 40px;"><div class="color-box"></div><label>Denote VATOS charges</label></div></div>
        <table class="table table-striped table-bordered ratecontracttable" id="ratecontracttable">
            <thead>
                <tr>
                    <th>Rate No.</th>
                    <th>Amendment No.</th>
                    <th>Item No</th>
                    <th>Origin</th>
                    <th>Origin State Code</th>
                    <th>Origin Country Code</th>
                    <th>Destination</th>
                    <th>Destination State code</th>
                    <th>Destination Country code</th>
                    <th>Type of Service</th>
                    <th>Eqp. Type</th>
                    <th>NOR</th>
                    <th>SOC</th>
                    <th>POL</th>
                    <th>POD</th>
                    <th>All-In Rate</th>
                    <th>NOTES</th>
                    <th>EFFECTIVE</th>
                    <th>EXPIRATION</th>
                    <th>Base Rate</th>
                    <th>Valid From</th>
                    <th>Valid To</th>
                    <th>VID</th>
                        <%              if (chargecodeset != null) {
                                Iterator<String> i = chargecodeset.iterator();
                                while (i.hasNext()) {%>
                    <th><%=i.next()%></th>
                        <%      }
                            }
                        %>

                </tr>
            </thead>
            <tbody>
                <%
                    comlis = (List) contractdetails.get(classname);
                    if (comlis != null) {
                        String effective = null, expiration = null;
                        ContractVo cvo = null;
                        for (int i1 = 0; i1 < comlis.size(); i1++) {
                            cvo = (ContractVo) comlis.get(i1);
                            effective = Validation.checkNull(cvo.getEffective());
                            expiration = Validation.checkNull(cvo.getExpiration());
                            effective = (effective.equalsIgnoreCase("TERM") ? effective : DateConversion_New.convertDefaultFormatTime(effective));
                            expiration = (expiration.equalsIgnoreCase("TERM") ? expiration : DateConversion_New.convertDefaultFormatTime(expiration));
                %>
                <tr>
                    <td><%=Validation.checkNull(cvo.getRateno())%></td>
                    <td><%=Validation.checkNull(cvo.getAmendmentno())%></td>
                    <td><%=Validation.checkNull(cvo.getItemno())%></td>
                    <td><%=Validation.checkNull(cvo.getOrigin()) + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(cvo.getOrigin())))%></td>
                    <td><%=Validation.checkNull(cvo.getOriginstatecode())%></td>
                    <td><%=Validation.checkNull(cvo.getOrigincountrycode())%></td>
                    <td><%=Validation.checkNull(cvo.getDestination()) + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(cvo.getDestination())))%></td>
                    <td><%=Validation.checkNull(cvo.getDestinationstatecode())%></td>
                    <td><%=Validation.checkNull(cvo.getDestinationcountrycode())%></td>
                    <td><%=Validation.checkNull(cvo.getTypeofservice())%></td>
                    <td><%=Validation.checkNull(cvo.getEqptype())%></td>
                    <td><%=("Y".equalsIgnoreCase(Validation.checkNull(cvo.getNor())) ? "Yes" : "No")%></td>
                    <td><%=("Y".equalsIgnoreCase(Validation.checkNull(cvo.getSoc())) ? "Yes" : "No")%></td>
                    <td><%=Validation.checkNull(cvo.getPol())%></td>
                    <td><%=Validation.checkNull(cvo.getPod())%></td>
                    <td><%=Currencyformat.rupeeFormat(Validation.checkNull(cvo.getAllinrate()))%></td>
                    <td><%=Validation.checkNull(cvo.getNotes())%></td>
                    <td><%=Validation.checkNull(effective)%></td>
                    <td><%=Validation.checkNull(expiration)%></td>
                    <td><%=Currencyformat.rupeeFormat(Validation.checkNull(cvo.getBaserate()))%></td>
                    <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(cvo.getValidfrom()))%></td>
                    <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(cvo.getValidto()))%></td>
                    <td><%=Validation.checkNull(cvo.getVid())%></td>
                    <%
                        if (applicablecharges != null) {
                            appcharge = (Map) applicablecharges.get(cvo.getRateno() + "-" + cvo.getItemno());
                            appcharge = (appcharge != null) ? appcharge : new HashMap();
                            if (chargecodeset != null) {
                                String charge = null, amt = null, vatos = null;
                                Iterator<String> i2 = chargecodeset.iterator();
                                while (i2.hasNext()) {
                                    charge = Validation.checkNull((String) appcharge.get(i2.next()));
                                    amt = Currencyformat.removeZero(Currencyformat.rupeeFormat(Validation.checkNull(charge.split("~~")[0])));
                                   // System.out.println("[ECOM-WAR] RateContractView_New chargecharge=> " + charge);
                                    if (charge.contains("~~")) {
                                        vatos = Validation.checkNull(charge.split("~~")[1]);
                                    }
                    %>
                    <td <%=(("Y".equalsIgnoreCase(vatos) && amt.length() > 0) ? "style='background-color: #b3e5fc;'" : "")%> ><%=amt%></td>
                    <%}
                        }%>
                </tr>
                <%}
                        }
                    }%>
            </tbody>
        </table>
    </div>

</div>
<%}
    }%>

<script type="text/javascript">
    $(document).ready(function () {
        $('.ratecontracttable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>
<style>
    #ratecontracttable thead th{
        white-space: nowrap;
    }
</style>