<%--
    Document   : EcomVGMView
    Created on : Jul 7, 2016, 1:56:42 PM
    Author     : deepa.p
--%>
<%@page language="java" import="java.util.List,net.ecom.vo.VgmVO,net.ecom.vo.VGMCommonVO,net.ecom.common.GeneralConf,net.ecom.common.Validation,java.text.DecimalFormat" errorPage=""%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");

    String agencyId = null, originLocation = null;
    VGMCommonVO vgmVO = (VGMCommonVO) request.getAttribute("VGMCONTAINERLIST");
    List<VgmVO> list = vgmVO.getVgmContList();
    String useremail = (String) session.getAttribute("SESSION_EMAIL");
    VgmVO vo = null;
    String exceluploadpath = (String) getServletContext().getAttribute(GeneralConf.exceluploadpath);
    String SESSION_LOGINNAME = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
    String HIDE_MASKPORT_CONTAINER = (String) getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER);
%>
<%if (exceluploadpath != null && exceluploadpath.length() > 0) {%>
<div class="si_imp_exp_excel pull-right p-b-10">
    <span class="pull-right"> <a href="javascript:void(0)" onclick="ExportData()" title="Download Template" class="btn btn-info btn-sm si_imp_exp_excel" id="exportsearch"><i class="fa fa-upload"></i> <bean:message key="ebooking.cargo.expexcel" bundle="<%=clientcode%>"/> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <span class="pull-right"> <a href="javascript:void(0)"  data-toggle="modal" title="Upload Template" data-target="#myImportModal" class="btn btn-info btn-sm si_imp_exp_excel" id="importsearch"><i class="fa fa-download"></i> <bean:message key="ebooking.cargo.impexcel" bundle="<%=clientcode%>"/> </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</span>
</div>
<%}%>
<div class="table-responsive card card-body card-shadow">
    <input type="hidden" name="bookingagencyid" id="bookingagencyid">
    <input type="hidden" name="originlocation" id="originlocation">
    <table class="table table-striped table-bordered" id="datatablebyeslvgm">
        <thead>
            <tr>
                <%if ("blno".equalsIgnoreCase(vgmVO.getSubmissionType())) {%>
                <th><bean:message key="vgm.blno" bundle="<%=clientcode%>"/></th>
                    <%}%>
                <th><bean:message key="vgm.bookno" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="vgm.equipmentid" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="vgm.equipmenttype" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="ecom.VGM.tareweight" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="vgm.weight" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="vgm.unit" bundle="<%=clientcode%>"/></th>
                <th><bean:message key="Tracking.action" bundle="<%=clientcode%>"/></th>
            </tr>
        </thead>
        <tbody>
            <%
                DecimalFormat df = new DecimalFormat("0.00");
                String tareWt = null;
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        vo = list.get(i);
                        agencyId = vo.getAgencyid();
                        originLocation = vo.getOriginportid();
                        System.out.println("[ECOM-WAR] EcomVGMView >>vgmVO.getApName()>>" + vgmVO.getApName());
                        tareWt = vo.getTareWeight();
                        System.out.println("[ECOM-WAR] EcomVGMView >>vo.tare()>>" + vo.getTareWeight());
                        tareWt = tareWt != null && tareWt.length() > 0 ? df.format(Double.parseDouble(tareWt)) : tareWt.equals("0.0") ? "0.00" : "";
                        System.out.println("[ECOM-WAR] EcomVGMView >>vo.tare()>>>>>conv" + tareWt);
                        if(SESSION_LOGINNAME==null || SESSION_LOGINNAME.length()==0){
                            vo.setDummyCntr(true);
                            vo.setEqpid("");
                        }

            %>
            <tr id="tablerowid<%=i%>">
                <%if ("blno".equalsIgnoreCase(vgmVO.getSubmissionType())) {%>
                <td><%=vgmVO.getBlNo()%></td>
                <%}%>
                <td><%=vo.getBookNo()%><input type="hidden" name="booknohid" id="booknohid<%=i%>" value="<%=vo.getBookNo()%>"/></td>
                <td>
                   
                    <%if (!vo.isDummyCntr()) {%>
                        <%if("Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)){%>
                            <input type="text" class="form-control input-sm light_border" maxlength="12" id="containerno<%=i%>_display" name="containerno<%=i%>_display" value="Container <%=(i+1)%>" style="background-color: #e9ecef;" readonly>
                            <input type="hidden" name="containernoname<%=i%>" id="containerno<%=i%>" value="<%=vo.getEqpid()%>" class="form-control input-sm required containernovalidation autooffclass maxtwo containererrorclass light_border checkvgmcontclass"/>
                           
                        <%} else {%>
                        <input type="text" name="containernoname<%=i%>" id="containerno<%=i%>" class="form-control input-sm required containernovalidation autooffclass maxtwo containererrorclass light_border checkvgmcontclass" onblur="setValues(this.value, 'containernohid<%=i%>', 'containernoerror<%=i%>', '<%=vo.getBookNo()%>');" value="<%=vo.getEqpid()%>" maxlength="12" readonly="true"/>
                    <%} } else {%>
                    <input type="text" name="containernoname<%=i%>" id="containerno<%=i%>" class="form-control input-sm required containernovalidation autooffclass maxtwo containererrorclass light_border checkvgmcontclass" onblur="setValues(this.value, 'containernohid<%=i%>', 'containernoerror<%=i%>', '<%=vo.getBookNo()%>');
                            setTareWeight(this.value, 'tareWeightId<%=i%>', '<%=vo.getEqptype()%>');" value="<%=vo.getEqpid()%>" maxlength="12"/>
                    <%}%>
                    <input type="hidden" id="containernohid<%=i%>" name="containernohid" value="<%=vo.getEqpid()%>" class="containernohidclass"/>
                    <label id="containernoerror<%=i%>" class="req " style="display:none" >Invalid Container.</label>
                    <input type="hidden" name="eqptypehid" value="<%=vo.getEqptype()%>" id="eqptype<%=i%>"/>
                    <input type="hidden" name="hidsoc" value="<%=vo.getShipperown()%>" id="soc<%=i%>"/>
                </td>
                <td>
                    <%=vo.getEqptype()%>
                </td>
                <td id="tareWeightId<%=i%>">
                    <%=tareWt%>
                </td>
                <td>
                    <input type="hidden" name="vgmWeight" id="weighthid<%=i%>" value="<%=vo.getVgm_weight()%>"/>
                    <label id="vgmtd<%=i%>" style="display: none"> --</label>
                    <input  id="vgmtd1<%=i%>" name="vgmtxt<%=i%>" onblur="setVGMWeight(this.value, 'vgmtd<%=i%>', 'vgmtd1<%=i%>', 'weighthid<%=i%>')" class="form-control input-sm number required autooffclass weightnocheck light_border checkvgmwtwithtarewtclass" value="<%=vo.getVgm_weight()%>" maxlength="9">
                </td>
                <td>
                    <input type="hidden" name="vgmUnit" id="unithid<%=i%>" value="<%=vo.getVgm_unit()%>"/>
                    <label id="unittd<%=i%>" style="display: none">Kgs</label>
                    <select id="unitselect<%=i%>"  onblur="setVGMUnit(this.value, 'unittd<%=i%>', 'unitselect<%=i%>', 'unithid<%=i%>')" class="form-control input-sm weightunitcheck light_border" value="<%=vo.getVgm_unit()%>">
                        <option value="K"  <%=(vo.getVgm_unit().equalsIgnoreCase("K")) ? "selected" : ""%>>Kgs</option>
<!--                         <option value="T"  <%=(vo.getVgm_unit().equalsIgnoreCase("T")) ? "selected" : ""%>>Tons</option>-->
                    </select>
                </td>
                <td><a href="javascript:void(0)" onclick="editTable('containernohid<%=i%>');" title="Copy" class="btn btn-lg text-success"  data-placement="right"><i class="fa fa-clipboard" aria-hidden="true"></i></a>
                    |<a href="javascript:void(0)" onclick="deleteRow('tablerowid<%=i%>');" title="Delete" class="btn btn-lg text-danger"  data-placement="right"><i class="fa fa-trash-o"></i></a>
                </td>
            </tr>
            <%                                        }
                }%>
        </tbody>
    </table>
    <input type="hidden" id="submittype" name="submittype" value="<%=vgmVO.getSubmissionType()%>">
    <input type="hidden" id="hidbookno" name="hidbookno" value="<%=vgmVO.getBookNo()%>">
    <input type="hidden" id="hidblno" name="hidblno" value="<%=vgmVO.getBlNo()%>">
    <input type="hidden" id="hiddenapname" name="hiddenapname" value="<%=vgmVO.getApName()%>">
    <input type="hidden" id="hiddenshname" name="hiddenshname" value="<%=vgmVO.getShName()%>">
    <input type="hidden" id="hiddenorigin" name="hiddenorigin" value="<%=vgmVO.getOrigin()%>">
    <input type="hidden" id="hiddendestination" name="hiddendestination" value="<%=vgmVO.getDestination()%>">
    <input type="hidden" id="hiddenloadport" name="hiddenloadport" value="<%=vgmVO.getLoadPortId()%>">
    <input type="hidden" id="hiddendischarge" name="hiddendischarge" value="<%=vgmVO.getDischargePortId()%>">
    <input type="hidden" id="hiddenvgmstatus" name="hiddenvgmstatus" value="<%=vgmVO.getVgmStatus()%>">
</div>




<script type="text/javascript" >
    $('#bookingagencyid').val('<%=agencyId%>');
    $('#originlocation').val('<%=originLocation%>');
    $(document).ready(function () {
        $('#sendemailid').keyup(function () {
            $('#emailvalidation').hide();
        });
        $('.autooffclass').attr('autocomplete', 'off');
        $('.weightnocheck').keyup(function () {
            if (this.value.match(/[^0-9.,]/g)) {
                this.value = this.value.replace(/[^0-9.,]/g, '');
            }
        });
        $('.containererrorclass').keyup(function () {
            var arr;
            var id = this.id;
            console.log("containererrorclassid-" + id);
            arr = id.split("containerno");
            id = "containernoerror" + arr[1];
            $("#" + id).hide();
        });
        /* $('#datatablebyeslvgm').DataTable({
         lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
         "aaSorting": []
         });*/
    });
    var FromEndDate = new Date();
    $('.tabledate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "yyyy-mm-dd", endDate: 'today', setDate: FromEndDate});
    $(document).ready(function () {
        $('.alphanumericwithoutspace').keyup(function () {
            if (this.value.match(/[^a-zA-Z0-9 ]/g)) {
                this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');
            }
        });

        var fromdate = new Date();
        fromdate.setDate(fromdate.getDate());
        $(".tabledate").each(function () {
            //$("#"+this.id).datepicker("setDate",fromdate);
            values = $("#" + this.id).val();
        });
        $(".obtaindatehid").each(function () {
            $("#" + this.id).val(values);
        });
        $('.containernovalidation').keyup(function () {
            if (this.value.match(/[^a-zA-Z0-9]/g)) {
                this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
            }
        });

    });
</script>

<script type="text/javascript" >
    function loadVGMData() {
        var tbl = $('#datatablebyeslvgm')
        var tblbody = $(tbl).find('tbody');
        var jObject = {};
        $.each($(tblbody).find('tr'), function (key, value)
        {
            $(this).find('input').each(function () {
                jObject[$(this).attr('name')] = $(this).val();
            });
        });
        var jsonstr = JSON.stringify(jObject);
        return jsonstr;
    }
    function ExportData() {

        var jsonval = loadVGMData();
        $('#vgmvalue').val(jsonval);
        $('#vgmRefNo').val($('#referenceNo').val());
        $('#vgmcount').val($('#datatablebyeslvgm').find('tbody').find('tr').length);
        document.getElementById("vgmexportexcel").action = "VGMExportExcel";
        document.getElementById("vgmexportexcel").submit();
    }

</script>