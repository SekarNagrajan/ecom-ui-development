<%-- 
    Document   : StatsReportView
    Created on : Sep 16, 2021, 3:18:16 PM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.vds.serviceVo.StatsReportVo,java.util.List,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New,net.ecom.common.Currencyformat,net.ecom.common.NumberFormat,net.ecom.common.Validation1"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
%>
<div class="card card-body card-shadow">
    <div class="form-group" style="margin-left: 13%;">
        <div class="row">
            <label class="col-sm-1 control-label"> Total 20s : </label>
            <label class="col-sm-2 control-label stats_label" id="total20s">  </label>

            <label class="col-sm-1 control-label"> Total 40s : </label>
            <label class="col-sm-3 control-label stats_label" id="total40s">  </label>

            <label class="col-sm-1 control-label"> Total OFR : </label>
            <label class="col-sm-3 control-label stats_label" id="totalofr">  </label>

        </div>
    </div>  

    <div class="form-group" style="margin-left: 13%;">
        <div class="row">
            <label class="col-sm-1 control-label"> Total Revenue : </label>
            <label class="col-sm-2 control-label stats_label" id="totalrev">  </label>

            <label class="col-sm-1 control-label"> Avg. OFR/FEU : </label>
            <div class="col-sm-3">
            <label class="control-label stats_label" id="avgofr">  </label>
            <br><span>(Excluding Feus of Zero OFR)</span>
            </div>

            <label class="col-sm-1 control-label"> Avg. Rev./FEU : </label>
            <div class="col-sm-3">
            <label class="control-label stats_label" id="avgrev">  </label>
            <br><span>(Excluding Feus of Zero Rev )</span>
            </div>

        </div>
    </div>  
    <hr>
    <table class="table table-striped table-bordered" id="datatablebystatsreport">
        <thead>
            <tr>
                <th>Booking Party</th>
                <th>Agreement Party</th>
                <th>Shipper</th>
                <th>Consignee</th>
                <th>Freight Forwarder</th>
                <th>Pick Up Location</th>
                <th>BL Number</th>
                <th>MCN NO</th>
                <th>Number of Stops</th>
                <th>Contract Commodity</th>
                <th>BL Commodity</th>
                <th>Contract Number</th>
                <th>Amendment Number</th>
                <th>Origin</th>
                <th>POL</th>
                <th>Origin Country</th>
                <th>Destination</th>
                <th>Destination Country</th>
                <th>POD</th>
                <th>Vessel</th>
                <th>Voyage</th>
                <th>Week No</th>
                <th>Sailing Date</th>
                <th>Booking Received Date</th>
                <th>Booking Accepted Date</th>
                <th>Pick Up Date</th>
                <th>In Gate Date</th>
                <th>Destination Vessel Arrival</th>
                <th>Arrival Notice Print Date</th>
                <th>Dispatch Full</th>
                <th>Received Empty</th>
                <th>Booking Source</th>
                <th>SI Source</th>
                <th>Sales Rep</th>
                <th>Container No</th>
                <th>Total Quantity</th>
                <th>SOC</th>
                <th>Size Type</th>
                <th>NOR</th>
                <th>Temp (C)</th>
                <th>Carriage Contract</th>
                <th>Cargo Weight (LBS)</th>
                <th>Cargo Weight (Kgs)</th>
                <th>20 Ft</th>
                <th>40 Ft</th>
                <th>Shipment Type</th>
                <th>Total Revenue</th>
                <th>OFR</th>
                <th>PRE</th>
                <th>ONC</th>
                <th>BAF</th>
                <th>USS</th>
                <th>IFR</th>
                <th>SEC</th>
                <th>WHF</th>
                <th>LSS</th>
                <th>FIF</th>
                <th>HGT</th>
                <th>THL</th>
                <th>THD</th>
                <th>VHS</th>
                <th>CRA</th>
                <th>CAS</th>
                <th>IRS</th>
                <th>GRI</th>
                <th>Others</th>
            </tr>
        </thead>
        <tbody>
            <%
                StatsReportVo srvo = null;
                List srlist = (List) request.getAttribute("statsreportdetails");
                double _20ft = 0, _40ft = 0, total_revenue = 0, ofr = 0, ft20ofr = 0, ft20trev = 0, ft40ofr = 0, ft40trev = 0;
                if (srlist != null) {
                    for (int i = 0; i < srlist.size(); i++) {
                        srvo = (StatsReportVo) srlist.get(i);
                        _20ft += Double.parseDouble(Validation1.checkNullfloat(srvo.get20ft()));
                        _40ft += Double.parseDouble(Validation1.checkNullfloat(srvo.get40ft()));
                        total_revenue += Double.parseDouble(Validation1.checkNullfloat(srvo.getTotal_revenue()));
                        ofr += Double.parseDouble(Validation1.checkNullfloat(srvo.getOfr()));
                        if (Double.parseDouble(Validation1.checkNullfloat(srvo.getOfr())) > 0) {
                            ft20ofr += Double.parseDouble(Validation1.checkNullfloat(srvo.getFt20ofr()));
                        }
                        ft20trev += Double.parseDouble(Validation1.checkNullfloat(srvo.getFt20trev()));
                        if (Double.parseDouble(Validation1.checkNullfloat(srvo.getOfr())) > 0) {
                            ft40ofr += Double.parseDouble(Validation1.checkNullfloat(srvo.getFt40ofr()));
                        }
                        ft40trev += Double.parseDouble(Validation1.checkNullfloat(srvo.getFt40trev()));
            %>
            <tr>
                <td><%=Validation.checkNull(srvo.getBooking_party())%></td>
                <td><%=Validation.checkNull(srvo.getAgreement_party())%></td>
                <td><%=Validation.checkNull(srvo.getShipper())%></td>
                <td><%=Validation.checkNull(srvo.getConsignee())%></td>
                <td><%=Validation.checkNull(srvo.getFreight_forwarder())%></td>
                <td><%=Validation.checkNull(srvo.getPick_up_location())%></td>
                <td><%=Validation.checkNull(srvo.getBl_number())%></td>
                <td><%=Validation.checkNull(srvo.getMcn_no())%></td>
                <td><%=Validation.checkNull(srvo.getNumber_of_stops())%></td>
                <td><%=Validation.checkNull(srvo.getContract_commodity())%></td>
                <td><%=Validation.checkNull(srvo.getBl_commodity())%></td>
                <td><%=Validation.checkNull(srvo.getContract_number())%></td>
                <td><%=Validation.checkNull(srvo.getAmendment_number())%></td>
                <td><%=Validation.checkNull(srvo.getOrigin())%></td>
                <td><%=Validation.checkNull(srvo.getPol())%></td>
                <td><%=Validation.checkNull(srvo.getOrigin_country())%></td>
                <td><%=Validation.checkNull(srvo.getDestination())%></td>
                <td><%=Validation.checkNull(srvo.getDestination_country())%></td>
                <td><%=Validation.checkNull(srvo.getPod())%></td>
                <td><%=Validation.checkNull(srvo.getVessel())%></td>
                <td><%=Validation.checkNull(srvo.getVoyage())%></td>
                <td><%=Validation.checkNull(srvo.getWeek_no())%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(srvo.getSailing_date()))%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(srvo.getBooking_received_date()))%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(srvo.getBooking_accepted_date()))%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(srvo.getPick_up_date()))%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(srvo.getIn_gate_date()))%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(srvo.getDestination_vessel_arrival()))%></td>
                <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatTime(srvo.getArrival_notice_print_date()))%></td>
                <td><%=Validation.checkNull(srvo.getDispatch_full())%></td>
                <td><%=Validation.checkNull(srvo.getReceived_empty())%></td>
                <td><%=Validation.checkNull(srvo.getBooking_source())%></td>
                <td><%=Validation.checkNull(srvo.getSi_source())%></td>
                <td><%=Validation.checkNull(srvo.getSales_rep())%></td>
                <td><%=Validation.checkNull(srvo.getContainer_no())%></td>
                <td><%=Validation.checkNull(srvo.getTotal_quantity())%></td>
                <td><%=Validation.checkNull(srvo.getSoc())%></td>
                <td><%=Validation.checkNull(srvo.getSize_type())%></td>
                <td><%=Validation.checkNull(srvo.getNor())%></td>
                <td><%=Validation.checkNull(srvo.getTemp())%></td>
                <td><%=Validation.checkNull(srvo.getCarriage_contract())%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getCargo_weight_lbs()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getCargo_weight_kgs()))%></td>
                <td><%=Validation.checkNull(srvo.get20ft())%></td>
                <td><%=Validation.checkNull(srvo.get40ft())%></td>
                <td><%=Validation.checkNull(srvo.getShipment_type())%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getTotal_revenue()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getOfr()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getPre()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getOnc()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getBaf()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getUss()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getIfr()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getSec()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getWhf()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getLss()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getFif()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getHgt()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getThl()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getThd()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getVhs()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getCra()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getCas()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getIrs()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getGri()))%></td>
                <td><%=Validation.checkNull(NumberFormat.decimalFormat(srvo.getOthers()))%></td>
            </tr>
            <%}
                }%>
        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#datatablebystatsreport').DataTable({
            dom: 'Blfrtip',
            buttons: [{
                    extend: 'excel',
                    text: '<span class="text-white fa fa-file-excel-o"></span>',
                    key: {
                        key: 'c',
                        altKey: true
                    },
                    title: 'Stats Report'
                }],
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            aaSorting: []
        });

    });
</script>

<script type="text/javascript">
    <%
        double avgrev = ft20trev + ft40trev;
        double avgofr = ft20ofr + ft40ofr;
        double avgrevtotal = 0, avgofrtotal = 0;
        if (avgrev > 0.0D) {
            avgrevtotal = total_revenue / avgrev;
        }
        if (avgofr > 0.0D) {
            avgofrtotal = ofr / avgofr;
        }
    %>
    $('#total20s').text('<%=NumberFormat.decimalFormat(String.valueOf(_20ft))%>');
    $('#total40s').text('<%=NumberFormat.decimalFormat(String.valueOf(_40ft))%>');
    $('#totalofr').text('<%=NumberFormat.decimalFormat(String.valueOf(total_revenue))%>');
    $('#totalrev').text('<%=NumberFormat.decimalFormat(String.valueOf(ofr))%>');
    $('#avgofr').text('<%=NumberFormat.decimalFormat(String.valueOf(avgofrtotal))%>');
    $('#avgrev').text('<%=NumberFormat.decimalFormat(String.valueOf(avgrevtotal))%>');
</script>    
