<%-- 
    Document   : RatesViewNew
    Created on : Sep 25, 2019, 4:17:30 PM
    Author     : gobinath.t
--%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.vo.Equipments,java.util.Map"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
    List ratelis = (List) session.getAttribute("RATESDETAILS");
    String clientcode = (String) (String) session.getAttribute("clientcode");

%>
<div class="card1 card-shadow1 page_bottom_content">
    <div class="card-body1">
        <div class="col-sm-12">
            <%if (ratelis != null && ratelis.size() > 0) {%>

            <!--Data b/w port/sea start  -->
            <div class="row">
                <div class="col-sm-1"> </div>
                <div class="box_ship_rls"> 
                    <div class="col-sm-7">
                        <div class="stepwizard m-l-70 m-t-20">
                            <div class="stepwizard-row setup-panel">
                                <div class=" stepwizard-step " style=" "> 
                                    <span class="rate_port_name"> Chennai </span>
                                    <img class="tracker_icon" src="Design/assets/images/page_icons/marker_container.svg" />
                                    <span class="track_ship_20">  <img class="rate_ship_icon" src="Design/assets/images/page_icons/Rvessel.svg" /> </span>
                                </div> 

                                <div class=" stepwizard-step " style="float: right;"> 
                                    <span class="rate_port_name_right"> COLOMBO </span>
                                    <img class="tracker_icon" src="Design/assets/images/page_icons//marker_container.svg" />
                                </div>
                            </div>

                        </div>

                        <div class="row quant_list m-l-30">
                            <div class="col-sm-4">
                                <label> Equipment </label>
                                <span class="list_val_clr"> 20 Dry Container </span>
                            </div>
                            <div class="col-sm-4">
                                <label> Commodity </label>
                                <span class="list_val_clr"> general cargo </span>
                            </div>
                            <div class="col-sm-3">
                                <label> Quantity </label>
                                <span class="list_val_clr"> 20  </span>
                            </div>

                        </div>

                    </div>

                    <div class="right_info">
                        <span id ="sum_amount_total" class="rate_total_amount">   </span>
                        <button  type="button" class="rates_toggle-btn " > <i class="fa fa-plus" data-toggle="collapse" data-target="#rates_table_data"> </i> </button>
                        <div class="req_spl_rate "> <a href="#" class=""><bean:message key="ecom.schdl.reqsplrate" bundle="<%=clientcode%>"/> </a> </div>
                    </div>


                    <!-- table data hide & show start -->
                    <div class="form-group m-t-30 m-l-30 collapse" id="rates_table_data">
                        <div class="col-sm-12 ">
                            <div class="table-responsive">
                                <table class="table table-borderless"  id="rates_total_data">
                                    <thead>
                                        <tr>
                                            <th> <bean:message key="ecom.schdl.charge" bundle="<%=clientcode%>"/> </th>
                                            <th> <bean:message key="ecom.surcharge.amount" bundle="<%=clientcode%>"/><span class="table_sm_label">(per Container)</span> </th>
                                            <th><bean:message key="ebooking.quantity.display" bundle="<%=clientcode%>"/> </th>
                                            <th> <bean:message key="ecom.surcharge.amount" bundle="<%=clientcode%>"/></th>

                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td> THL </td>
                                            <td> $100 </td>
                                            <td> 3 </td>
                                            <td > $<span class="totalprice">600</span>  </td>
                                        </tr>

                                        <tr>
                                            <td> DOC </td>
                                            <td> $100 </td>
                                            <td> LP </td>
                                            <td > $<span class="totalprice">500</span>  </td>
                                        </tr>

                                        <tr>
                                            <td> DOC </td>
                                            <td> $100 </td>
                                            <td> 3 </td>
                                            <td> $<span class="totalprice">600</span>  </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div class="col-sm-4 m-r-50-r float-right">
                                <label class="total-label"><bean:message key="ecom.schdl.tot" bundle="<%=clientcode%>"/></label>
                                <span id ="sum_amount" class="total_amount_table">  </span>
                            </div>
                        </div>
                    </div>
                    <!-- table data hide & show End -->
                </div>
            </div>
            <!--Data b/w port/sea end   -->

            <%} else {%>

            <!--No results msg start  -->
            <div class="row">
                <div class="col-sm-1"> </div>
                <div class="col-sm-10">
                    <div class="box_no_rls"> 
                        <div class="text-center" style=" ">
                            <h2 class="txt_no_msg" style=""> <bean:message key="rates.msg.nores" bundle="<%=clientcode%>"/>  </h2>
                            <button type="submit"  onclick="navigateToQuote();" class="btn btn_block_quote" ><bean:message key="ecom.rr.reqforrate" bundle="<%=clientcode%>"/></button>
                        </div>
                    </div>
                </div>
                <div class="col-sm-2"> </div>
            </div>
            <!--No results msg end  -->
            <%}%>
        </div>
    </div>
</div>

<script>

    //toggle icon for the rates

    $(document).ready(function () {

        $('.rates_toggle-btn').click(function () {
            $("i", this).toggleClass('fa-minus fa-plus');
        });

    });

</script>
