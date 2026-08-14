<%-- 
    Document   : SIBLCommonCharges
    Created on : Dec 16, 2020, 7:46:26 PM
    Author     : gobinath.t
--%>

<%@page import="java.util.Arrays"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.HashMap,net.ecom.vo.SIPartiesVO,net.ecom.common.utils.DateConversion_New,net.ecom.common.GeneralConf,java.util.List,net.ecom.vo.ChargesVo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String SHOW_CHARGES_IN_SIBL = (String) getServletContext().getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
    List Chargedetails = (List) request.getAttribute("CHARGEDETAILS");
%>
<%if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {%>

<h6><i class="step-icon fa_upload"></i> <bean:message key="ecom.si.charges" bundle="<%=clientcode%>"/> </h6>

<fieldset>  	
    <div class="details_search">
        <input type="hidden" name='ChargeDetailsJSON' id='ChargeDetailsJSON'>
        <input type="hidden" name='copySIPayor' id='copySIPayor'>
        <input type="hidden" name='copySIfrieght' id='copySIfrieght'>        
        <input type="hidden" name='copySIlocation' id='copySIlocation'>  
        <div class="card card-body card-shadow ">
            <span class="text-info font-weight-bold">
                To modify multiple rows at once:<br>
                <span style="padding-left: 30px">1.Change the Freight,  Payor and Location as needed  on the row of your choice.</span><br>
                <span style="padding-left: 30px">2.Select the modified Row and click on Copy.</span><br>
                <span style="padding-left: 30px">3.Either select All or select the desire Rows and click on Paste to copy the selection.</span><br>                
            </span>
            <div class="si_imp_exp_excel pull-right">
                <span class="pull-right"> <a href="javascript:void(0)" class="btn btn-info btn-sm" onclick="applyPaste()"><i class="fa fa-paste"></i> Paste </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <span class="pull-right"> <a href="javascript:void(0)" title="Copy" class="btn btn-info btn-sm" onclick="copycharges()" ><i class="fa fa-copy"></i> Copy </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
            </div>
            <table class="table table-striped table-bordered" id="datatablebychargetbl">
                <thead>
                    <tr>
                        <th title="all" ><input type="checkbox" id="chargescheckboxall"  onchange="selectallcharges()"> All</th>
                        <th title="sno">S.No</th>
                        <th title="chargeid" class='d-none'>Charge Code</th>
                        <th title="chargename" >Charge Description</th>
                        <th title="fprepaidcollect">Frieght</th>
                        <th title="custtypeid" >Payor</th>
                        <th title="payagencyid" >Location</th>
                        <th title="eqptypeid" >Eqp.type</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ArrayList custtypeidlis = new ArrayList(Arrays.asList("AP-SH-FW-CN-NT".split("-")));
                        ChargesVo charge = null;
                        if (Chargedetails != null && Chargedetails.size() > 0) {
                            for (int i = 0; i < Chargedetails.size(); i++) {
                                charge = (ChargesVo) Chargedetails.get(i);

                    %>
                    <tr>
                        <td><input type="checkbox" class="chargescheckbox" title="select"></td>
                        <td><%=(i+1)%></td>
                        <td class='d-none'><%=charge.getChargeid()%></td>
                        <td><%=charge.getChargename()%></td>
                        <td class="prepaidcollect">
                            <select  class="form-control SIfrieght required" onchange="setPayor(this)" >
                                <option value="">Select</option>
                                <option value="P" <%=("P".equalsIgnoreCase(charge.getFprepaidcollect()) ? "selected='selected'" : "")%> >Prepaid</option>
                                <option value="C" <%=("C".equalsIgnoreCase(charge.getFprepaidcollect()) ? "selected='selected'" : "")%> >Collect</option>
                                <option value="E" <%=("E".equalsIgnoreCase(charge.getFprepaidcollect()) ? "selected='selected'" : "")%> >Third Party</option>
                            </select>
                            <span class="d-none"></span>
                        </td>
                        <td class="custtypeid">
                            <select  class="form-control SIPayor required" onchange="applycharge(this)">
                                <option value="">Select</option>
                                <%if ("P".equalsIgnoreCase(charge.getFprepaidcollect())) {%>
                                <option value="AP" <%=("AP".equalsIgnoreCase(charge.getCusttypeid()) ? "selected='selected'" : "")%> >Agr. Party</option>
                                <option value="SH" <%=("SH".equalsIgnoreCase(charge.getCusttypeid()) ? "selected='selected'" : "")%> >Shipper</option>
                                <option value="FW" <%=("FW".equalsIgnoreCase(charge.getCusttypeid()) ? "selected='selected'" : "")%> >Forwarder</option>
                                <%} else if ("C".equalsIgnoreCase(charge.getFprepaidcollect())) {%>

                                <option value="FW" <%=("FW".equalsIgnoreCase(charge.getCusttypeid()) ? "selected='selected'" : "")%> >Forwarder</option>
                                <option value="CN" <%=("CN".equalsIgnoreCase(charge.getCusttypeid()) ? "selected='selected'" : "")%> >Consignee</option>
                                <option value="NT" <%=("NT".equalsIgnoreCase(charge.getCusttypeid()) ? "selected='selected'" : "")%> >Notify</option>
                                <%} else if ("E".equalsIgnoreCase(charge.getFprepaidcollect())) {%>
                                <option value="AP" <%=("AP".equalsIgnoreCase(charge.getCusttypeid()) ? "selected='selected'" : "")%> >Agr. Party</option>
                                <%}%>
                                <%if (custtypeidlis != null && (!custtypeidlis.contains(charge.getCusttypeid()))) {%>
                                <option value="<%=charge.getCusttypeid()%>"><%=charge.getCusttypeid()%></option>
                                <%}%>
                            </select>
                            <span class="d-none"></span>
                        </td>
                        <td class="payagencyid"><%=charge.getPayagencyid()%></td>
                        <td><%=charge.getEqptypeid()%></td>
                    </tr>
                    <%}
                        }
                    %>
                </tbody>
            </table>
        </div> 
        <div class="row form-group">

            <div class="col-xs-12 col-sm-3">
                <label><bean:message key='ebooking.ref.type' bundle="<%=clientcode%>"/></label>
                <html:select property="releasetype" styleId="releasetype" styleClass="form-control" >
                    <html:option value="">Select</html:option>
                    <html:option value="Normal">Normal</html:option>
                    <html:option value="Express">Express</html:option>
                </html:select>
            </div>

        </div>

    </div>
</fieldset> 
<%}%>