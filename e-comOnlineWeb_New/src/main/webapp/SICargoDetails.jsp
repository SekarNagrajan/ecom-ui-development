<%-- 
    Document   : SICargoDetails
    Created on : Apr 21, 2017, 4:52:35 PM
    Author     : deepa.p
--%>
<%@page import="net.ecom.common.GeneralConf"%>
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String containerNo = (String) request.getParameter("containerNo");
    String id = (String) request.getParameter("id");
    String containercountid = (String) request.getParameter("containercountid");
    String appversion = request.getParameter("appversion");
    String incclass = (String) request.getParameter("incclass");
    String isRequired = (String) request.getParameter("isRequired");
    String checkboxid = (String) request.getParameter("checkboxid");
    String hscodeconfig = (String) request.getParameter("hscodemandary");
    String eqpstatusval = (String) request.getParameter("eqpstatusval");
    String oogvalue = (String) request.getParameter("oogvalue");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String commodityDescDisclaimer = (String) getServletContext().getAttribute(GeneralConf.CommodityDescInOnlyENG_Disclaimer);
    String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
    String Enable_cargodesc_mandatory = (String) getServletContext().getAttribute(GeneralConf.Enable_cargodesc_mandatory);
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
    String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    String enableapplyallcont = (String) getServletContext().getAttribute(GeneralConf.Enable_Apply_To_All_Customer);
    String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
    String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
    boolean is_SI_OOG = false;
    if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
        is_SI_OOG = true;
    }

    String hslencheck = (String) getServletContext().getAttribute(GeneralConf.ENABLE_HSCODE_LENGTH_VALIDATION);
    String cdId = null, comdyId_new = null, cmdId = null, hiddencomid = null, qtyId = null, pkgId = null, hiddenpackid = null;
    String wtId = null, ntId = "", utId = null, volId = null, vutId = null, mrkId = null, qtyId_new = null, wtId_new = null, ntId_new = null, desId = null,buyerName = null,sellerName = null,cmdtyhblno=null,buyerCode=null,sellerCode=null,sellerSelect=null,buyerSelect=null;
     String buyerNameid = null,sellerNameid = null,cmdtyhblnoid=null,buyerCodeid=null,sellerCodeid=null,sellerSelectid=null,buyerSelectid=null;
    String volId_new = null, packId_new = null, hscodeid = null, hiddenhsid = null, commoditycn = null, packagecn = null;
    String ntcn = "", wtcn = null, volcn = null, qtycn = null, classname = null, qtyclass = null, weigthclass = null, volumeclass = null;
    String incclassid = null, netweightclass = null, hidhscodeid = null, unitclass = null, hazardousname = null, hazardousid = null;
    String hscodecls = "", markdescclass = null;
    String hscodeclass = null, insurance = null;
    String itemforwardname = "", itemtopname = "", itemrightname = "", itemaftname = "", itemleftname = "", itemlengthUnitname = "", oogclass = "";
    weigthclass = "weigthclass" + containercountid;
    netweightclass = "netweightclass" + containercountid;
    unitclass = "unitclass" + containercountid;
    markdescclass = "markdescclass" + containercountid;
    if ("true".equalsIgnoreCase(isRequired)) {
        classname = "reqclass" + containercountid + " required";
        qtyclass = "qtyclass" + containercountid + " maxone";
        if (!"M".equalsIgnoreCase(eqpstatusval)) {
            weigthclass = weigthclass + " validatezeropoint";
            weigthclass = weigthclass + " validatepoint ";
        }
        if ("M".equalsIgnoreCase(eqpstatusval)) {
            weigthclass = weigthclass + " valuezero ";
            weigthclass = weigthclass + " validatezeropoint ";
        }
        volumeclass = "volumeclass" + containercountid + " validatepoint";
    }
    if ("2".equalsIgnoreCase(appversion)) {
        hscodecls += "reqclass" + containercountid + " required";
    } else {
        hscodecls += "reqclass" + containercountid;
    }
    if ("Y".equalsIgnoreCase(hslencheck)) {
        hscodecls += " hscodelengthcheck";
    }
    cdId = "cid" + containerNo + id;
    comdyId_new = "newcmdty" + containerNo + id;
    cmdId = "commodity" + containerNo + id;
    hiddencomid = "hiddencomid" + containerNo + id;
    qtyId = "qty" + containerNo + id;
    pkgId = "package" + containerNo + id;
    hiddenpackid = "hiddenpackid" + containerNo + id;
    wtId = "weight" + containerNo + id;
    ntId = "netweight" + containerNo + id;
    buyerCodeid= "bycode"+ containerNo + id;
    buyerNameid= "byname"+ containerNo + id;
    sellerCodeid=  "slcode"+ containerNo + id;
    sellerNameid=  "slname"+ containerNo + id;
    sellerSelectid= "slselect"+ containerNo + id;
    buyerSelectid=  "byselect"+ containerNo + id;
    cmdtyhblnoid= "cmdty_hblno"+ containerNo + id;
    utId = "unit" + containerNo + id;
    volId = "volume" + containerNo + id;
    vutId = "vunit" + containerNo + id;
    mrkId = "mark" + containerNo + id;
    desId = "descp" + containerNo + id;
    qtyId_new = "newqtyid" + containerNo + id;
    wtId_new = "newwtid" + containerNo + id;
    ntId_new = "newntid" + containerNo + id;
    volId_new = "newvolid" + containerNo + id;
    packId_new = "newpackid" + containerNo + id;
    hscodeid = "hscode" + containerNo + id;
     buyerCode= containerNo + "#bycode#";
     buyerName= containerNo + "#byname#";
     sellerCode= containerNo + "#slcode#";
     sellerName= containerNo + "#slname#";
	sellerSelect= containerNo + "#slselect#";
	buyerSelect= containerNo + "#byselect#";
     cmdtyhblno= containerNo + "#cmdty_hblno#";
    hiddenhsid = "hiddenhsid" + containerNo + id;
    incclassid = incclass + id;
    hidhscodeid = "hidhscodeval" + containerNo + id;
    commoditycn = "form-control " + classname + " light_border commodity_new";
    hscodeclass = "form-control " + hscodecls;
    packagecn = "form-control " + classname + " light_border package_new";
    wtcn = "form-control " + classname + " number light_border " + weigthclass;
    ntcn = "form-control number light_border " + netweightclass;
    volcn = "form-control number light_border " + volumeclass;
    qtycn = "form-control " + classname + " number light_border " + qtyclass;
    hazardousname = containerNo + "#hazardous#";
    hazardousid = containerNo + "#hazardous#" + id;
    insurance = "insurance" + containerNo + id;
    itemforwardname = containerNo + "#itemforward#";
    itemtopname = containerNo + "#itemtop#";
    itemrightname = containerNo + "#itemright#";
    itemaftname = containerNo + "#itemaft#";
    itemlengthUnitname = containerNo + "#itemlengthUnit#";
    itemleftname = containerNo + "#itemleft#";
    oogclass = "oogclass" + containerNo;
%>
<div class="si_cargo_pkg_des w-100 form-group row">
    <div class="col-sm-10">		

        <div class="row box_height_gray <%=incclass%>" id="<%=incclassid%>">

            <div class="col-sm-4 form-group">
                <label>
                    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                    <bean:message key="shipping.commoditygrp.display" bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%> <span class="mandatory"> * </span> </label>
                <input type="text"  name="<%=cmdId%>"  id="<%=cmdId%>"  class="<%=commoditycn%> autocompletetmsg cp_commoditygroup" onblur="setnewvalues('<%=comdyId_new%>', '<%=cmdId%>');" readonly="true"/>
                <input type="hidden" name="<%=containerNo%>#cid#" id="<%=cdId%>" value="new"/>
                <input type="hidden" name="<%=containerNo%>#newcmdty#" id="<%=comdyId_new%>" class="checkcommodityvalue"/>
                <input type="hidden" id="<%=hiddencomid%>" name="<%=hiddencomid%>">
                <input type="hidden" id="<%=hazardousid%>" name="<%=hazardousname%>" class="cp_hazardous" value="N"/>
            </div>

            <div class="col-sm-4 form-group">

                <label>
                    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                    <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                    <%} else if ("Y".equalsIgnoreCase(hsnamecode)) {%>
                    <bean:message key='ebooking.trust.hsnamecode' bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key="shipping.hscode.display" bundle="<%=clientcode%>"/>
                    <%}%>
                    <%if (("2").equalsIgnoreCase(appversion)) {%><span class="mandatory"> * </span><%}%> </label>
                <input type="text" name="<%=hscodeid%>"  Id="<%=hscodeid%>" value="" class="form-control light_border <%=hscodeclass%> autocompletetmsg cp_hscode"   onblur="setnewvalues('<%=hidhscodeid%>', '<%=hscodeid%>');" <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%> onchange="hscodevalidation(this);"<%}%>/>
                <input type="hidden" id="<%=hiddenhsid%>" name="<%=hiddenhsid%>">
                <input type="hidden" name="<%=containerNo%>#hscode#" id="<%=hidhscodeid%>">
            </div>


            <div class="col-sm-4 form-group">

                <label> 
                    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                    <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                    <%}%>
                    <span class="mandatory"> * </span> </label>
                <input type="text"  name="<%=pkgId%>"  Id="<%=pkgId%>"  class="<%=packagecn%> autocompletetmsgfortype cp_packagetype" onblur="setnewvalues('<%=packId_new%>', '<%=pkgId%>');"/>
                <input type="hidden" name="<%=containerNo%>#Scipt#" id="<%=containerNo%>#Scipt#" value="<%=id%>"/>
                <input type="hidden" name="<%=containerNo%>#newpack#" id="<%=packId_new%>"/>
                <input type="hidden" id="<%=hiddenpackid%>" name="<%=hiddenpackid%>">
            </div>

            <div class="col-sm-4 form-group">
                <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                <label><bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label> 
                <input type="text" name="<%=qtyId%>" maxlength="8"  Id="<%=qtyId%>" class="<%=qtycn%><%=qtyclass%> cp_noofpackage" onblur="setnewvalues('<%=qtyId_new%>', '<%=qtyId%>');" onkeypress="keyCheckINT(event)"/>
                <input type="hidden" name="<%=containerNo%>#newqty#" id="<%=qtyId_new%>" />
                <%} else {%>

                <label><bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label> 
                <input type="text" name="<%=qtyId%>" maxlength="8"  Id="<%=qtyId%>" class="<%=qtycn%><%=qtyclass%> cp_noofpackage" onblur="setnewvalues('<%=qtyId_new%>', '<%=qtyId%>');" onkeypress="keyCheckINT(event)"/>
                <input type="hidden" name="<%=containerNo%>#newqty#" id="<%=qtyId_new%>" />
                <%}%>
            </div>


            <div class="col-xs-12 col-sm-2 ecom_padding_right">	
                <label>	<bean:message key="shipping.cargoweight.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label>

                <input type="text" maxlength="9" name="<%=wtId%>"  Id="<%=wtId%>" class="<%=wtcn%> cp_cargoweight cargweightmsg" onblur="setnewvalues('<%=wtId_new%>', '<%=wtId%>');PayLoadValidation('containerno<%=containerNo%>', 'eqptypeval<%=containerNo%>');"/>
                <input type="hidden" name="<%=containerNo%>#newwt#" id="<%=wtId_new%>" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=wtId%>');"/>

            </div>

            <div class="col-xs-12 col-sm-2 ecom_padding_left">
                <label> </label>
                <input type="hidden" id="<%=containerNo%>#unit#_previous" name="<%=containerNo%>#unit#_previous" value="K" />
                <select name="<%=containerNo%>#unit#" Id="<%=utId%>" class="form-control light_border  no-left-border <%=unitclass%> SI_Process_Restriction cp_cargoweightunit" onchange="calcSITareWeight(this.value, 'tareweight<%=containerNo%>', 'twt<%=containerNo%>', '<%=wtId%>', 'S');">
                    <option value="K" >Kilos</option>
                    <option value="T">Metric Tones</option>
                    <option value="P">Pounds</option>
                </select>
            </div>
            <!--netweight-->
            <div class="col-sm-4 form-group">	
                <label>	<bean:message key="ecom.cargo.netweight" bundle="<%=clientcode%>"/> </label>

                <input type="text" maxlength="8" name="<%=ntId%>"  Id="<%=ntId%>" class="<%=ntcn%> SI_Process_Restriction" onblur="setnewvalues('<%=ntId_new%>', '<%=ntId%>');"/>
                <input type="hidden" name="<%=containerNo%>#newnt#" id="<%=ntId_new%>" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=ntId%>');"/>

            </div>
             <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
           <div class="col-sm-4 form-group">
                     <label><bean:message key='ebooking.cmdtyhblno.display' bundle="<%=clientcode%>"/></label>
                    <input type="text" class="form-control light_border " id="<%=cmdtyhblnoid%>" name="<%=cmdtyhblno%>" maxlength="20">
          </div>           
		   <div class="col-sm-4 form-group">	
                   <label><bean:message key='ebooking.sellersName.display' bundle="<%=clientcode%>"/></label>
                 <select  class="form-control input-sm setdata  light_border" id="<%=sellerSelectid%>" name="sellerNameList" onchange="setCustNameCode(this,'<%=sellerCodeid%>','<%=sellerNameid%>')">
                        <option value="">Select Customer</option>
                    </select>
                     <input type="hidden" id="<%=sellerCodeid%>" name="<%=sellerCode%>" value="" />
                     <input type="hidden" id="<%=sellerNameid%>" name="<%=sellerName%>" value="" />
                </div>
				
                  <div class="col-sm-4 form-group">	
                   <label><bean:message key='ebooking.buyersName.display' bundle="<%=clientcode%>"/></label>
                   <select  class="form-control input-sm setdata light_border" id="<%=buyerSelectid%>" name="buyerNameList"  onchange="setCustNameCode(this,'<%=buyerCodeid%>','<%=buyerNameid%>')" )>
                        <option value="">Select Customer</option>
                    </select>
                    <input type="hidden" id="<%=buyerCodeid%>" name="<%=buyerCode%>" value="" />
                    <input type="hidden" id="<%=buyerNameid%>" name="<%=buyerName%>" value="" />
                </div>
				
				
              
           <%}%>

            <div class="col-xs-12 col-sm-2 ecom_padding_right">

                <label>	<bean:message key="shipping.volume.display" bundle="<%=clientcode%>"/></label>

                <input type="text" maxlength="8" name="<%=volId%>"  Id="<%=volId%>"  class="<%=volcn%> SI_Process_Restriction cp_volume" onblur="setnewvalues('<%=volId_new%>', '<%=volId%>');"/>
                <input type="hidden" name="<%=containerNo%>#newvol#" id="<%=volId_new%>" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=volId%>');"/>

            </div>

            <div class="col-xs-12 col-sm-2 ecom_padding_left">

                <label> </label>
                <select name="<%=containerNo%>#vunit#" Id="<%=vutId%>" class="form-control light_border  no-left-border SI_Process_Restriction cp_volumeunit" >
                    <option value="M" >Cubic Meter</option>
                    <option value="F">Cubic Feet</option>
                </select>

            </div>

            <div class="col-xs-12 col-sm-4">
                <label> <bean:message key="shipping.marks.display" bundle="<%=clientcode%>"/></label>
                <textarea name="<%=containerNo%>#mark#" Id="<%=mrkId%>"  class="form-control <%=markdescclass%> cp_marksno" rows="3"></textarea>
            </div>
            <%if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
            <input type="hidden" class="inquotecusttype" id="<%=insurance + "_custtype"%>" >
            <input type="hidden" class="inquoteno" id="<%=insurance + "_quoteno"%>" >
            <input type="hidden" class="incargoval" id="<%=insurance + "_cargoval"%>" >
            <input type="hidden" class="inavailable" id="<%=insurance + "_available"%>" value="N" >
            <!--            <div class="col-xs-12 col-sm-3 text-center">
                            <a href="javascript:void(0)" onclick="loadinsurancepage($('#<=insurance + "_quoteno"%>').val(), $('#<=insurance + "_custtype"%>').val())"  class="btn btn-info btn-md addinsurance" style="margin-top: 18px;"><i class=""></i> <bea:message key="booking.btn.cargo.insurance" bundle="<=clientcode%>"/> </a>
                            <div class="text-center" style="margin-top: 10px;">
                                <label for="<=insurance%>" class="label_switch"> <bea:message key="booking.cargo.insurance" bundle="<=clientcode%>"/> </label>
                                <label class="switch">
                                    <input type="checkbox" value="N" id="<=insurance%>" name="<=insurance%>" class="insuranceswitchbtn"  disabled="disabled" />
                                    <span class="slider round"></span>
                                </label>
                            </div>
                        </div>-->
            <%}%>

            <%if ("Y".equalsIgnoreCase(commodityDescDisclaimer)) {%>


            <div class="col-xs-12 col-sm-12">
                <label> <bean:message key="shipping.description.display" bundle="<%=clientcode%>"/> <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%><span class="mandatory"> * </span> <%}%> </label>

                <textarea name="<%=containerNo%>#descp#" Id="<%=desId%>"  class="form-control cp_cargodesc" rows="10" onkeyup="acceptCommoOnlyEnglish(this)" <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%> onchange="cargodescvalidation(this);"<%}%>></textarea>
                <span class='text-info'> <bean:message key="ecom.cargo.commlangeng" bundle="<%=clientcode%>"/></span>
            </div>
            <%} else {%>

            <div class="col-xs-12 col-sm-12">
                <label> <bean:message key="shipping.description.display" bundle="<%=clientcode%>"/> <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%><span class="mandatory"> * </span> <%}%> </label>

                <textarea name="<%=containerNo%>#descp#" Id="<%=desId%>"  class="form-control cp_cargodesc" rows="10" <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%> onchange="cargodescvalidation(this);"<%}%>></textarea>
                <span class='text-info'> <bean:message key='ecom.coomdesc.lang' bundle="<%=clientcode%>"/></span>
            </div>

            <%}%>

            <div class="col-xs-12 col-sm-12 " <%if ("N".equalsIgnoreCase(oogvalue)) {%>style="display: none;"<%}%> id="oogblock<%=containerNo%>">
                <%if (is_SI_OOG) {%>
                <!--<label class="lbl_pkcg">   <beanmessage key="ebooking.oog.text.display" bundle="<clientcode>"/> </label>-->
                <hr/>
                <div class=" row">

                    <div class="col-sm-4">
                        <label><bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <select class="form-control input-sm setdata <%=oogclass%> light_border "   id="<%=itemlengthUnitname + id%>" name="<%=itemlengthUnitname%>"  >
                            <option value="M">Meters</option>
                            <option value="F">Feet</option>
                            <option value="C">Centimeters</option>
                        </select>
                    </div>

                    <div class="col-sm-4">
                        <label><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border " id="<%=itemleftname + id%>"   name="<%=itemleftname%>" value="" />
                    </div>

                    <div class="col-sm-4">
                        <label><bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border "  id="<%=itemrightname + id%>" name="<%=itemrightname%>" value="" />
                    </div>
                </div>
                <div class="form-group row">

                    <div class="col-sm-4">
                        <label><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <input type="text"  maxlength="6" class="form-control number input-sm setdata validatezeropoint <%=oogclass%> light_border " id="<%=itemforwardname + id%>"   name="<%=itemforwardname%>" value="" />
                    </div>


                    <div class="col-sm-4">
                        <label><bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border "  id="<%=itemaftname + id%>"   name="<%=itemaftname%>" value="" />
                    </div>


                    <div class="col-sm-4">
                        <label><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                        <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border " id="<%=itemtopname + id%>"   name="<%=itemtopname%>" value="" />
                    </div>
                </div>
                <%}%>
            </div>
        </div>

    </div>

    <div class="col-sm-2 m-t-120"> 

        <button type="button" onclick="deleteRow_New('sitable', this);" title="Delete Cargo Details"  class="btn btn_add_si si_cmdty_remove_btn"> <i class="fa fa-minus"></i> Remove </button>
        <% if ("Y".equalsIgnoreCase(enableapplyallcont)) {%>
        <button type="button" onclick="applyToAllContainers(this, '<%=containerNo + id%>')" title="Apply to all containers" class="btn apply_container_btn"> <i class="fa fa-copy"></i>   Apply to all containers</button>
        <%}%>
    </div>
</div>
<script>
    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
    function () {
        loadtypeaheadtooltipmsg();
    }
    <%}%>
</script>