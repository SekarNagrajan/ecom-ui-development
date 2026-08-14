<%@page import="net.ecom.vds.serviceVo.BLChargeVo"%>
<%@page contentType="text/html; charset=UTF-8" import="net.ecom.common.GeneralConf,java.util.ArrayList, java.util.HashMap,java.util.Set,java.util.Hashtable,net.ecom.vo.SIDtableVO, net.ecom.vo.SIPartiesVO,net.ecom.vo.SIContainerVO,java.util.List,net.ecom.common.Validation,java.util.Map,net.ecom.vo.SIContainerDetailVO,net.ecom.vo.SIMasterVO,net.ecom.vo.SISealVO,net.ecom.vo.SIConatainerValueVO,net.ecom.vo.CustomerDetailsVO,net.ecom.common.utils.DateConversion_New,net.ecom.common.NumberFormat,net.ecom.vo.ChargesVo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
    session.setAttribute("SISEND", "SISend");
  String blno = (String) session.getAttribute("edit_blno") != null ? (String) session.getAttribute("edit_blno") : "";
  String agencyRefNo = (String) request.getAttribute("SIAGENCYREFNO") != null ? (String) request.getAttribute("SIAGENCYREFNO") : "";
   List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    String enableaesno = (String) getServletContext().getAttribute(GeneralConf.ENABLE_AES_NUMBER);
    String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
    String clientcode = (String) session.getAttribute("clientcode");
    String Ventilationlabel = (String) getServletContext().getAttribute("Ventilation");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    List addInfoList = (List) getServletContext().getAttribute(GeneralConf.AdditionalInfoBkg);
    boolean is_SI_Addinfo = false;
    if (addInfoList != null && addInfoList.contains("ACD")) {
        is_SI_Addinfo = true;
    }
    String loadportcountry = (String) request.getAttribute("LoadportCountry");
    String dischargeportcountry = (String) request.getAttribute("dischargeportcountry");
    System.out.println("[ECOM-WAR] BLPreview dischargeportcountry===>" + dischargeportcountry);
    String SHOW_CHARGES_IN_SIBL = (String) getServletContext().getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
    String Enable_BLType_Selection = (String) getServletContext().getAttribute(GeneralConf.Enable_BLType_Selection);
    String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    boolean is_SI_OOG = false;
    if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
        is_SI_OOG = true;
    }
    String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);
    String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
    String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
    String enableENSTab = "N";
    if ("Y".equalsIgnoreCase(GENS)) {
        enableENSTab = (String) session.getAttribute("IsEnableENSTabInSIBL");
    }
    List<BLChargeVo> Chargesummary = (ArrayList<BLChargeVo>) session.getAttribute("SIBLChargeSummary");
    String Display_Charge_Tab = (String) getServletContext().getAttribute(GeneralConf.Display_Charge_Tab);
    String Enable_T2LFiling = (String) getServletContext().getAttribute(GeneralConf.Enable_T2LFiling);
    String Enable_NVOCC_In_SI = (String) getServletContext().getAttribute(GeneralConf.Enable_NVOCC_In_SI);
    String SI_NVOCC_Eligible_Country = (String) getServletContext().getAttribute(GeneralConf.SI_NVOCC_Eligible_Country);
    String enbleafricaReqsMandatory = (String) getServletContext().getAttribute(GeneralConf.Enable_africaReqsMandatory);
    String Enable_ReleaseType = (String) getServletContext().getAttribute(GeneralConf.Enable_Telex_ReleaseType);
    String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
    String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
    System.out.println("[ECOM-WAR] BLEdit Enable_Zip_Code==>" + Enable_Zip_Code);
    String Enable_Addinfo_Bltype = (String) getServletContext().getAttribute(GeneralConf.ENABLE_ADDINFO_BLTYPE);
    boolean showNvocc = false;
    if ("Y".equalsIgnoreCase(Enable_NVOCC_In_SI) && dischargeportcountry != null && SI_NVOCC_Eligible_Country != null) {
        for (String cty : SI_NVOCC_Eligible_Country.split(",")) {
            if (dischargeportcountry.equalsIgnoreCase(cty.trim())) {
                showNvocc = true;
                break;
            }
        }
    }

%>

<h6><i class="step-icon fa_preview"></i> <bean:message key="ecom.blprev.head" bundle="<%=clientcode%>"/> </h6>


<fieldset>  	
    <div class="details_search">

        <div class="card card-shadow">

            <div class="card-header preview_header">  <bean:message key="ebooking.custdetails.display" bundle="<%=clientcode%>"/><a href="#" onclick="$('#SIEditForm-t-0').click();" class="float-right text-white" > <i class="fa fa-pencil"></i>  </a> </div>
            <div class="card-body">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><bean:message key="bl.Bookingno" bundle="<%=clientcode%>"/> :</label>

                    <label class="col-sm-2 control-label"> <%=(String) session.getAttribute("edit_bookno")%>  </label>
                    <%if (blno.length() > 0) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="bl.blno" bundle="<%=clientcode%>"/> : </label>

                    <label class="col-sm-2 control-label"> <%=(String) session.getAttribute("edit_blno")%></label>
                    <%}%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.agency.refno" bundle="<%=clientcode%>"/> :  </label>

                    <label class="col-sm-2 control-label"><%=agencyRefNo%>  </label>

                </div>
                <div class="form-group">
                    <%if ("Y".equalsIgnoreCase(Enable_BLType_Selection)) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.bl.type" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-3 control-label text-left" id="preview_bltype"></label>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(Enable_T2LFiling)) {%>
                    <label class="col-sm-2 control-label"><bean:message key="ebooking.booking.T2LFiling" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-3 control-label text-left" id="preview_t2lfiling"></label>
                    <%}%>
                    <%if (showNvocc) {%>
                    <label class="col-sm-2 control-label"><bean:message key="ebooking.booking.NVOCC" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-3 control-label text-left" id="preview_nvocc"></label>
                    <%}%>
                     <%if ("Y".equalsIgnoreCase(Enable_ReleaseType)) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="ebooking.booking.releaseType" bundle="<%=clientcode%>"/>  : </label>
                    <label class="col-sm-3 control-label text-left" id="preview_orgTelType"></label>
                    <%}%>
                  <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                    <label class="col-sm-2 control-label"> Documentation Type : </label>
                    <label class="col-sm-3 control-label text-left" id="preview_ensdocTpe"></label>
                    <%}%>
                  
                </div>

                <hr/>
                <div class="row">
                    <div class="col-md-4">
                        <div id="shipper" class="panel_right">
                            <div class="custom-panel-head"> <bean:message key="shipping.booking.party.shipperType" bundle="<%=clientcode%>"/>  </div>     
                            <div class="row">
                                <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                            </div>

                            <div class="row">
                                <div class="col-sm-12 form-group">
                                    <input type="text" id="shipperheadername_label" name="shipperheadername_label" disabled="true" class="form-control input-si">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <textarea name="shipperheader_label" id="shipperheader_label" class="form-control input-sm" rows="5" disabled="true"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>



                    <div class="col-md-4">

                        <div class="custom-panel-head"> <bean:message key="shipping.booking.party.consigneeType" bundle="<%=clientcode%>"/> </div>

                        <div class="row">

                            <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                        </div>

                        <div class="row">
                            <div class="col-sm-12 form-group">
                                <input type="text" id="consigneeheadername_label" name="consigneeheadername_label" disabled="true" class="form-control input-si">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <textarea name="consigneeheader_label" id="consigneeheader_label" class="form-control input-si" rows="5" disabled="true"></textarea>
                            </div>
                        </div>


                    </div>


                    <div class="col-md-4 ">

                        <div class="custom-panel-head"><bean:message key="Booking.party.notify" bundle="<%=clientcode%>"/>
                        </div>

                        <div class="row">

                            <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                        </div>

                        <div class="row">
                            <div class="col-sm-12 form-group">
                                <input type="text" id="notifyheadername_label" name="notifyheadername_label" disabled="true" class="form-control input-si">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <textarea name="notifyheader_label" id="notifyheader_label" class="form-control input-si" rows="5" disabled="true"></textarea>
                            </div>
                        </div>

                    </div>
                </div>
                <% if (custtypelist.contains("N1")) {%>

                <div class="row">

                    <div class="col-md-12">
                        <div class="normalpanel" id="nt_id2">

                            <div class="custom-panel-head"> <bean:message key="Booking.party.notify1" bundle="<%=clientcode%>"/> </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyname_label2" name="notifyname_label2" disabled="true" class="form-control input-si">

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyadress_label2" name="notifyadress_label2" disabled="true" class="form-control input-si">

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">
                                    <input type="text" id="notifyadress1_label2" name="notifyadress_label2" disabled="true" class="form-control input-si">

                                </div>
                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifycity_label2" name="notifycity_label2" disabled="true" class="form-control input-si">
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifycountry_label2" name="notifycountry_label2"  disabled="true" class="form-control input-si">
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text" id="notifyphone_label2" name="notifyphone_label2"   disabled="true" class="form-control input-si">

                                </div>
                            </div>


                            <div class="row form-group">

                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyfax_label2" name="notifyfax_label2" disabled="true" class="form-control input-si">

                                </div>
                                <%}%>

                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyemail_label2" name="notifyemail_label2" disabled="true" class="form-control input-si">

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text"  id="notifystate_label2" name="notifystate_label2"  disabled="true" class="form-control input-si">

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <input type="text" id="notifyzipcode_label2" name="notifyzipcode_label2" disabled="true" class="form-control input-si">
                                </div>
                                <%}%>
                            </div>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <div class="row form-group">



                                <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyeori_label2" name="notifyeori_label2"  class="form-control input-si">

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text"  id="notifypersontype_label2" name="notifypersontype_label2"   class="form-control input-si">

                                </div>

                            </div>
                            <%}%> 

                        </div>

                    </div>

                </div>
                <%}%>
                <% if (custtypelist.contains("N2")) {%>

                <div class="row">

                    <div class="col-md-12">
                        <div class="normalpanel" id="nt_id3">

                            <div class="custom-panel-head"> <bean:message key="Booking.party.notify2" bundle="<%=clientcode%>"/> </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyname_label3" name="notifyname_label3" disabled="true" class="form-control input-si">
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyadress_label3" name="notifyadress_label3" disabled="true" class="form-control input-si">
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">
                                    <input type="text" id="notifyadress1_label3" name="notifyadress1_label3" disabled="true" class="form-control input-si">
                                </div>

                            </div>

                            <div class="row form-group">



                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifycity_label3" name="notifycity_label3" disabled="true" class="form-control input-si">

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifycountry_label3" name="notifycountry_label3"  disabled="true" class="form-control input-si">
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text" id="notifyphone_label3" name="notifyphone_label3"  disabled="true" class="form-control input-si">
                                </div>
                            </div>


                            <div class="row form-group">
                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyfax_label3" name="notifyfax_label3" disabled="true" class="form-control input-si">
                                </div>
                                <%}%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyemail_label3" name="notifyemail_label3" disabled="true" class="form-control input-si">
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text"  id="notifystate_label3" name="notifystate_label3"  disabled="true" class="form-control input-si">

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <input type="text" id="notifyzipcode_label3" name="notifyzipcode_label3" disabled="true" class="form-control input-si">
                                </div>
                                <%}%>
                            </div>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <div class="row form-group">



                                <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="notifyeori_label3" name="notifyeori_label3" disabled="true" class="form-control input-si">

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text"  id="notifypersontype_label3" name="notifypersontype_label3" disabled="true" class="form-control input-si">

                                </div>

                            </div>
                            <%}%> 
                        </div>
                    </div>

                </div>
                <%}%>
                <% if (custtypelist.contains("FW")) {%>

                <div class="row">

                    <div class="col-md-12">
                        <div class="normalpanel" id="fw_id">

                            <div class="custom-panel-head"> <bean:message key="ecom.bkg.custfw" bundle="<%=clientcode%>"/> </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="forwardername_label" name="forawardername_label" disabled="true" class="form-control input-si">
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="forwarderadress_label" name="forwarderadress_label" disabled="true" class="form-control input-si">
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">
                                    <input type="text" id="forwarderadress1_label" name="forwarderadress1_label" disabled="true" class="form-control input-si">
                                </div>

                            </div>

                            <div class="row form-group">



                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text"  id="forwardercity_label" name="forwardercity_label"  disabled="true" class="form-control input-si">

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="forwardercountry_label" name="forwardercountry_label" disabled="true" class="form-control input-si">
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text" id="forwarderphone_label" name="forwarderphone_label"  disabled="true" class="form-control input-si">
                                </div>
                            </div>


                            <div class="row form-group">
                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="forwarderfax_label" name="forwarderfax_label" disabled="true" class="form-control input-si">
                                </div>
                                <%}%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="forwarderemail_label" name="forwarderemail_label" disabled="true" class="form-control input-si">
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text"  id="forwarderstate_label" name="forwarderstate_label"  disabled="true" class="form-control input-si">

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <input type="text" id="forwarderzipcode_label" name="forwarderzipcode_label" disabled="true" class="form-control input-si">
                                </div>
                                <%}%>
                            </div>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <div class="row form-group">



                                <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <input type="text" id="forwardereori_label" name="forwardereori_label" disabled="true" class="form-control input-si">

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <input type="text"  id="forwarderpersontype_label" name="forwarderpersontype_label" disabled="true" class="form-control input-si">

                                </div>

                            </div>
                            <%}%> 
                        </div>
                    </div>

                </div>
                <%}%>
               <jsp:include page="/WEB-INF/jsp/SI/SLBLCommonPreview.jsp" />
            </div>
        </div>
        <div class="card card-shadow">

            <div class="card-header preview_header"> <bean:message key="ecom.shippingins.headinfo" bundle="<%=clientcode%>"/> <a href="#" onclick="$('#SIEditForm-t-1').click();" class="float-right text-white" > <i class="fa fa-pencil"></i>  </a> </div>
            <div class="card-body">
                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="Tracking.origin" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <input type="text" id="hid_pla_free_txt_label" name="hid_pla_free_txt_label" disabled="true" class="form-control input-si">

                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <input type="text" id="pla_free_txt_label" name="pla_free_txt_label" disabled="true" class="form-control input-si">

                    </div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.portofload.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <input type="text" id="hid_pol_free_txt_label" name="hid_pol_free_txt_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <input type="text" id="pol_free_txt_label" name="pol_free_txt_label" disabled="true" class="form-control input-si">

                    </div>

                </div>


                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.portofdischarge.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <input type="text" id="hid_pod_free_txt_label" name="hid_pod_free_txt_label" disabled="true" class="form-control input-si">

                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">

                        <input type="text" id="pod_free_txt_label" name="pod_free_txt_label" disabled="true" class="form-control input-si">

                    </div>

                </div>


                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <input type="text" id="hid_pld_free_txt_label" name="hid_pld_free_txt_label" disabled="true" class="form-control input-si">
                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <input type="text" id="pld_free_txt_label" name="pld_free_txt_label" disabled="true" class="form-control input-si">
                    </div>

                </div>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>

                    <div class="heading_trainee_booking ">  <bean:message key="Booking.head.vesselschddet" bundle="<%=clientcode%>"/>  </div>

                </div>
                <div id="siroutingdetailspreview">

                </div>
            </div>
        </div>
        <div class="card card-shadow">

            <div class="card-header preview_header">  <bean:message key="shipping.cargo.head" bundle="<%=clientcode%>"/> <a href="#" onclick="$('#SIEditForm-t-2').click();" class="float-right text-white" > <i class="fa fa-pencil"></i>  </a> </div>
            <div class="card-body">
                <div id="cargopreview">
                    <div class="premainrowsi" id="premainrowsi" style="display:none">
                        <div class="form-group row">
                            <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.container.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-5 text-info" id="sicontno_lable"></label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-5 font-weight-bold text-left">SOC</label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-5 text-info" id="sisoc_lable"></label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.eqpsize.display" bundle="<%=clientcode%>"/> </label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-5 text-info" id="sieqptyow_lable"></label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.carrierseal.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-5 text-info" id="sicarrierseal_lable"></label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.shipperseal.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-5 text-info" id="sishipperseal_lable" style="word-break: break-all;"></label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.tareweight.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-5 text-info" id="sitareweight_lable"></label>
                        </div>
                        <%if (is_SI_OOG) {%>
                        <div class="form-group row">
                            <label class="col-sm-5 font-weight-bold text-left"><bean:message key="ebooking.oog.text.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-5 text-info" id="oogflag_lable"></label>
                        </div>
                        <%}%>
                        <span id="SIPreviewTemp" style="display: none;">
                            <div class="form-group row">
                                <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.NOR.display' bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class="col-sm-5 text-info" id="sinor_lable"></label>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class="col-sm-5 text-info" id="sisettemp_lable"></label>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.temp.unit' bundle="<%=clientcode%>"/></label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class="col-sm-5 text-info" id="sitempunit_lable"></label>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.vent' bundle="<%=clientcode%>"/><%= Ventilationlabel%></label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class="col-sm-5 text-info" id="sivent_lable"></label>
                            </div>
                        </span>
                    </div>
                    <div class="precargosi" id="precargosi" style="display:none">
                        <div class="form-group row">
                            <label class="col-sm-2 font-weight-bold text-left">
                                <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                <bean:message key="shipping.commoditygrp.display" bundle="<%=clientcode%>"/>
                                <%} else {%>
                                <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                                <%}%>
                                <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-4 text-info" id="sicmdtylabel"></label>
                            <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                            <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-2 text-info" id="siqtylabel"></label>
                            <%} else {%>
                            <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-2 text-info" id="siqtylabel"></label>
                            <%}%>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 font-weight-bold text-left">
                                <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                                <%} else if ("Y".equalsIgnoreCase(hsnamecode)) {%>
                                <bean:message key='ebooking.trust.hsnamecode' bundle="<%=clientcode%>"/>
                                <%} else {%>
                                <bean:message key="shipping.hscode.display" bundle="<%=clientcode%>"/>
                                <%}%>
                            </label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-4 text-info" id="sihscodelabel"></label>
                            <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.volume.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-2 text-info" id="sivollabel" ><span id="sivolunitlabel"></span></label>
                           <!--  <label class=" col-sm21 text-info" id="sivolunitlabel"></label>-->
                        </div>
                        <div class="form-group row">

                            <label class=" col-sm-2 font-weight-bold text-left">
                                <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                                <%} else {%>
                                <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                                <%}%>
                            </label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-4 text-info" id="sipacklabel"></label>

                            <label class=" col-sm-2 font-weight-bold text-left"><bean:message key="shipping.cargoweight.display" bundle="<%=clientcode%>"/></label> <label class=""></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-2 text-info" id="siwtlabel"></label>
                            <label class=" col-sm-2 text-info" id="siwtunitlabel"></label>
                        </div>
                        <%if("Y".equalsIgnoreCase(TENS_Documentation)){%>
                             <div class="form-group row">

                            <label class=" col-sm-2 font-weight-bold text-left">
                                <bean:message key="ebooking.sellersName.display" bundle="<%=clientcode%>"/>
                            </label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-4 text-info" id="sisellerlabel"></label>

                            <label class=" col-sm-2 font-weight-bold text-left"><bean:message key="ebooking.buyersName.display" bundle="<%=clientcode%>"/></label> <label class=""></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-2 text-info" id="sibuyerlabel"></label>
                        </div>
                            <%}%>
                        <!--                                         netweight
                        <div class="form-group row">
                            <label class="col-sm-2 font-weight-bold text-left"><bean:message key="ecom.cargo.netweight" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class="col-sm-4 text-info" id="sinetweight_lable"></label>
                            <%if("Y".equalsIgnoreCase(TENS_Documentation)){%>
                            <label class=" col-sm-2 font-weight-bold text-left"><bean:message key="ebooking.cmdtyhblno.display" bundle="<%=clientcode%>"/></label> <label class=""></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-2 text-info" id="sihblnolabel"></label>
                        
                            <%}%>
                        </div>


                        <div class="form-group row" >
                            <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.marks.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-9 text-info" id="simarkslabel" style="overflow:auto;width:600px;height:60px"></label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.description.display" bundle="<%=clientcode%>"/></label>
                            <label class="col-sm-1 text-center">:</label>
                            <label class=" col-sm-9 text-info" id="sidesclabel" <!--style="overflow:auto;width:600px;height:60px"></label>
          </div>--> 
                        <%if (is_SI_OOG) {%>
                        <span id="OOGpreviewblock" style="display: none;">
                            <div class="form-group row" >

                                <label class=" col-sm-2 font-weight-bold text-left">
                                    <bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/>
                                </label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class=" col-sm-4 text-info" id="sioogunitlabel"></label>

                                <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class=" col-sm-2 text-info" id="sioogleftlabel"></label>
                            </div>
                            <div class="form-group row">

                                <label class=" col-sm-2 font-weight-bold text-left">
                                    <bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/>
                                </label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class=" col-sm-4 text-info" id="sioogrightlabel"></label>

                                <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class=" col-sm-2 text-info" id="sioogforwardlabel"></label>
                            </div>
                            <div class="form-group row">

                                <label class=" col-sm-2 font-weight-bold text-left">
                                    <bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/>
                                </label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class=" col-sm-4 text-info" id="sioogaltlabel"></label>

                                <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                <label class="col-sm-1 text-center">:</label>
                                <label class=" col-sm-2 text-info" id="sioogtoplabel"></label>
                            </div>
                        </span>
                        <%}%>
                    </div>
                    <div>
                        <table class="table table-striped table-bordered remove_td_title" id="sitablepreview">
                            <thead>
                                <tr>
                                    <th width="5"><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>"/></th>
                                    <th width="30%"><bean:message key="ecom.shippingins.cargo.contno" bundle="<%=clientcode%>"/></th>
                                        <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                    <th width="80%"><bean:message key="adminscreen.description" bundle="<%=clientcode%>"/></th>                                            </tr>
                                        <%} else {%>
                            <th width="80%"><bean:message key="ecom.shippingins.cargo.packdesc" bundle="<%=clientcode%>"/></th>                                            </tr>
                                <%}%>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <%if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {%>
        <div class="card card-shadow">
            <div class="card-header preview_header"> <bean:message key="ecom.si.charges" bundle="<%=clientcode%>"/> <a href="javascript:void(0);" onclick="$('#SIEditForm-t-3').click();" class="float-right text-white"> <i class="fa fa-pencil"></i>  </a> </div>
            <div class="card-body">
                <div class="col-sm-12">
                    <div class="table-responsive" id="previewdatatablebychargediv">
                        <table class="table table-striped table-bordered" id="previewdatatablebycharge"></table>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.ref.type' bundle="<%=clientcode%>"/></label>
                        <label class="col-sm-1 text-center">:</label>
                        <label class=" col-sm-4 text-info" id="releasetypelbl"></label>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
        <%if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
        <div class="card card-shadow">
            <div class="card-header preview_header"> <bean:message key="booking.cargo.additionalproductandcharges" bundle="<%=clientcode%>" /> <a href="javascript:void(0);" onclick="$('#SIEditForm-t-3').click();" class="float-right text-white"> <i class="fa fa-pencil"></i>  </a> </div>
            <div class="card-body">
                <div class="col-sm-12">
                    <div class="table-responsive" id="cargoprotect_table_div_preview">

                    </div>

                </div>
            </div>
        </div>
        <%}%>
        <%if ("Y".equalsIgnoreCase(enableENSTab)) {%>
        <div class="card card-shadow">
            <div class="card-header preview_header"> <bean:message key="shipping.ens.heading" bundle="<%=clientcode%>" /> <a href="javascript:void(0);" onclick="$('#SIEditForm-t-4').click();" class="float-right text-white"> <i class="fa fa-pencil"></i>  </a> </div>
            <div class="card-body">
                <div class="col-sm-12">
                    <div id="ens_tab_div_preview">
                        <div id="ensmasterfields">
                            <div class="row form-group">

                                <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.eu.customs.zone" bundle="<%=clientcode%>"/>   </label>
                                <div class="col-sm-4 ">
                                    <input type="text" id="euCustZone_label" disabled="true" class="form-control input-si">
                                </div>
                                <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.of.bl" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-4 ">
                                    <input type="text" id="blTypeENS_label" disabled="true" class="form-control input-si">
                                </div>
                            </div>
                            <div class="row form-group">

                                <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.ens.filling" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-4 ">
                                    <input type="text" id="ensFillingType_label" disabled="true" class="form-control input-si">
                                </div>

                                <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.method.of.payment" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-4 ">
                                    <input type="text" id="paymentMethod_label" disabled="true" class="form-control input-si">
                                </div>

                            </div>
                        </div>
                        <div id="buyerseller_preview" style="display: none">
                            <div class="row" id="buyer_preview">

                                <div class="col-md-12">
                                    <div class="normalpanel" id="by_id">

                                        <div class="custom-panel-head"> <bean:message key="shipping.ens.declarant.buyer" bundle="<%=clientcode%>"/> </div>

                                        <div class="row form-group">

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="buyername_label"  disabled="true" class="form-control input-si">
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="buyeradress_label"  disabled="true" class="form-control input-si">
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                            <div class="col-sm-3">
                                                <input type="text" id="buyeradress1_label"  disabled="true" class="form-control input-si">
                                            </div>

                                        </div>

                                        <div class="row form-group">



                                            <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text"   id="buyercity_label"   disabled="true" class="form-control input-si">

                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="buyercountry_label" disabled="true" class="form-control input-si">
                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <input type="text" id="buyerphone_label" disabled="true" class="form-control input-si">
                                            </div>
                                        </div>


                                        <div class="row form-group">
                                            <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="buyerfax_label"disabled="true" class="form-control input-si">
                                            </div>
                                            <%}%>
                                            <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                                            <label class="col-sm-1 control-label"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/><span class="mandatory">* </span>  </label>
                                            <div class="col-sm-3">
                                                <input type="text" id="buyerzipcode_label" disabled="true" class="form-control input-si">
                                            </div>
                                            <%}%>
                                            <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="buyeremail_label" disabled="true" class="form-control input-si">
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <input type="text"  id="buyerstate_label"  disabled="true" class="form-control input-si">

                                            </div>

                                        </div>
                                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                        <div class="row form-group">



                                            <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="buyereori_label"   disabled="true" class="form-control input-si">

                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <input type="text"  id="buyerpersontype_label"   disabled="true"  class="form-control input-si">

                                            </div>

                                        </div>
                                        <%}%> 
                                    </div>
                                </div>

                            </div>
                            <div class="row" id="seller_preview">

                                <div class="col-md-12">
                                    <div class="normalpanel" id="sl_id">

                                        <div class="custom-panel-head"> <bean:message key="shipping.ens.declarant.seller" bundle="<%=clientcode%>"/> </div>

                                        <div class="row form-group">

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="sellername_label" disabled="true" class="form-control input-si">
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="selleradress_label" disabled="true" class="form-control input-si">
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                            <div class="col-sm-3">
                                                <input type="text" id="selleradress1_label" disabled="true" class="form-control input-si">
                                            </div>

                                        </div>

                                        <div class="row form-group">



                                            <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text"   id="sellercity_label" disabled="true" class="form-control input-si">

                                            </div>


                                            <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="sellercountry_label" disabled="true" class="form-control input-si">
                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <input type="text" id="sellerphone_label" disabled="true" class="form-control input-si">
                                            </div>
                                        </div>


                                        <div class="row form-group">
                                            <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="sellerfax_label" disabled="true" class="form-control input-si">
                                            </div>
                                            <%}%>
                                            <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                                                <label class="col-sm-1 control-label"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/><span class="mandatory">* </span>  </label>
                                                <div class="col-sm-3">
                                                    <input type="text" id="sellerzipcode_label" disabled="true" class="form-control input-si">
                                                </div>
                                                <%}%>
                                            <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="selleremail_label" disabled="true" class="form-control input-si">
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <input type="text"  id="sellerstate_label" disabled="true" class="form-control input-si">

                                            </div>

                                        </div>
                                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                        <div class="row form-group">



                                            <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <input type="text" id="sellereori_label" disabled="true" class="form-control input-si">

                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <input type="text"  id="sellerpersontype_label" disabled="true" class="form-control input-si">

                                            </div>

                                        </div>
                                        <%}%> 
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div id="supplementarydeclarant_preview" style="display: none">
                        <div class="header_tle">  <bean:message key="shipping.ens.supplementary.declarant" bundle="<%=clientcode%>"/>  </div>

                        <div class="row form-group">
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.name' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantName_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.address' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantAddress_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.address1' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantAddress1_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.city' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantCity_label" disabled="true" class="form-control input-si">
                            </div>
                            <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantZipcode_label" disabled="true" class="form-control input-si">
                            </div>
                            <%}%>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.country' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantCountry_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.state' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantState_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.telephone' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantTelephone_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.eori' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantEORI_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.email' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantEmail_label" disabled="true" class="form-control input-si">
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label class="control-label"><bean:message key='shipping.ens.declarant.fillingtype' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <input type="text" id="declarantFillingType_label" disabled="true" class="form-control input-si">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
        <%if ("Y".equalsIgnoreCase(Display_Charge_Tab)) {%>
        <div class="card card-shadow">
            <div class="card-header preview_header"> <bean:message key="ecom.si.charges" bundle="<%=clientcode%>"/> </div>
            <div class="card-body">
                <div class="col-sm-12">
                    <div class="table-responsive" id="previewdatatableblchargetabdiv">
                        <table class="table table-striped table-bordered" id="datatablebychargetabpreview">
                            <thead>
                                <tr>
                                    <th title="chargecode"> <bean:message key="bl.chargecode" bundle="<%=clientcode%>"/></th>
                                    <th title="chargedesc"> <bean:message key="bl.chargedesc" bundle="<%=clientcode%>"/></th>
                                    <th title="baserate" > <bean:message key="bl.baserate" bundle="<%=clientcode%>"/></th>
                                    <th title="baseratequantity"> <bean:message key="bl.baseratequantity" bundle="<%=clientcode%>"/></th>
                                    <th title="amount" > <bean:message key="bl.amount" bundle="<%=clientcode%>"/></th>
                                    <th title="currency" > <bean:message key="bl.currency" bundle="<%=clientcode%>"/></th>
                                    <th title="equipmentType" > <bean:message key="bl.equipmentType" bundle="<%=clientcode%>"/></th>
                                    <th title="baserateunit" > <bean:message key="bl.baserateunit" bundle="<%=clientcode%>"/></th>
                                    <th title="paymentmode" > <bean:message key="bl.paymentmode" bundle="<%=clientcode%>"/></th>
                                    <th title="paylocation" > <bean:message key="bl.paylocation" bundle="<%=clientcode%>"/></th>
                                    <th title="payby" > <bean:message key="bl.payby" bundle="<%=clientcode%>"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <%

                                    BLChargeVo charge = null;
                                    if (Chargesummary != null && Chargesummary.size() > 0) {
                                        for (int i = 0; i < Chargesummary.size(); i++) {
                                            charge = (BLChargeVo) Chargesummary.get(i);

                                %>
                                <tr>
                                    <td><%=charge.getChargeId()%></td>
                                    <td><%=charge.getChargeName()%></td>
                                    <td><%=charge.getBaseRate()%></td>
                                    <td><%=charge.getChargeRoe()%></td>
                                    <td><%=charge.getChargeAmount()%></td>
                                    <td><%=charge.getCurrencyId()%></td>
                                    <td><%=charge.getEqpTypeId()%></td>
                                    <td><%=charge.getBaseRateUnit()%></td>
                                    <td > <%=("P".equalsIgnoreCase(charge.getPrepaidCollect()) ? "Prepaid"
                                            : ("C".equalsIgnoreCase(charge.getPrepaidCollect()) ? "Collect"
                                            : ("E".equalsIgnoreCase(charge.getPrepaidCollect()) ? "PayAt" : "")))%>
                                    </td>
                                    <td><%=charge.getPayAgencyId()%></td>
                                    <td > <%=charge.getCustTypeId()%></td>
                                </tr>
                                <%}
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </div>
        <%}%>
        <div class="card">
            <div class="card-header preview_header">  <bean:message key='shipping.addremarks.display' bundle="<%=clientcode%>"/> </div>
            <div class="card-body card-shadow">

                <div class="form-group row">
                    <label class="col-sm-2 control-label">Declared Value</label>
                    <div class="col-sm-6">
                        <html:text styleId="declaredValue" property="declaredValue"  styleClass="form-control light_border"  maxlength="50">
                        </html:text>
                    </div>
                </div>

                <% if (is_SI_Addinfo) {%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">ACID</label>
                    <div class="col-sm-3">
                        <html:text styleId="acidValue" property="acidValue"  styleClass="form-control light_border"  maxlength="20">
                        </html:text>
                    </div>
                </div>

                <%}
                    if ("Y".equalsIgnoreCase(enableaesno) && "US".equalsIgnoreCase(loadportcountry)) {%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key='ecom.si.aesno' bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-10">
                        <html:text styleId="siAESnumber" property="siAESnumber"  styleClass="form-control input-si" maxlength="50" onchange="$('input[name=AESDisclaimer]').prop('checked', false);"/>
                        <span class="text-info"> <bean:message key='ecom.si.aesmsg' bundle="<%=clientcode%>"/></span>
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-10">
                        <label class=" col-sm-8 control-label">
                            <bean:message key='ecom.si.aesdismsg' bundle="<%=clientcode%>"/>
                        </label>
                        <label class=" col-sm-8 control-label AESDisclaimer">
                            <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required - valued at $2,500 or less" onchange="$('#siAESnumber').val('')"/>  <bean:message key='ecom.si.aesdismsg1' bundle="<%=clientcode%>"/>
                        </label>
                        <label class=" col-sm-8 control-label AESDisclaimer">
                            <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required, section 30.39 FTSR, C.A.S" onchange="$('#siAESnumber').val('')"/>  <bean:message key='ecom.si.aesdismsg2' bundle="<%=clientcode%>"/>
                        </label>
                        <!-- <label class=" col-sm-8 control-label AESDisclaimer">
                            <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required, section 30.55 (H) FTSR" onchange="$('#siAESnumber').val('')"/>  <bean:message key='ecom.si.aesdismsg3' bundle="<%=clientcode%>"/>
                        </label> -->
                    </div>
                </div>
                <%}%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key='shipping.addremarks.display' bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-10">
                        <html:textarea styleId="sicustremarks" property="sicustremarks"  styleClass="form-control" rows="3" ></html:textarea>
                        </div>
                    </div>
                <%if ("Y".equalsIgnoreCase(enbleafricaReqsMandatory)) {%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Packing List</label>
                    <div class="col-sm-3">
                        <html:text styleId="packinglst" property="packinglst"  styleClass="form-control light_border"  maxlength="30">
                        </html:text>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Invoice Upload</label>
                    <div class="col-sm-3">
                        <html:text styleId="invoiceupload" property="invoiceupload"  styleClass="form-control light_border"  maxlength="30">
                        </html:text>
                    </div>
                </div>
                <%}%>
                <% if ("Y".equalsIgnoreCase(Enable_Addinfo_Bltype) && "AE".equalsIgnoreCase(dischargeportcountry)) {%>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label">BL Type - UAE Specific <span class="mandatory">*</span>
                            <i class="fa fa-info-circle text-info" style="cursor: pointer; font-size: 16px; margin-left: 5px;" data-toggle="tooltip" data-placement="right" data-html="true"
                               title="As per the NAIC (UAE) guidelines,<br/><br/>If HBL is not involved – Select <b>'Direct Bill of Lading'</b><br/><br/>If HBL involved – Select <b>'Master Bill of Lading'</b> and update MPCI code."></i>
                        </label>
                        <div class="col-sm-3">
                            <html:select styleId="blType_UAE" property="blType_UAE" styleClass="form-control light_border required">
                                <html:option value="">Select BL Type</html:option>
                                <html:option value="Master BL">Master BL</html:option>
                                <html:option value="Direct BL">Direct BL</html:option>
                            </html:select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label">MPCI Id <span id="mpciIdMandatory" class="mandatory" style="display: none;">*</span></label>
                        <div class="col-sm-3">
                            <html:text styleId="mpciId_UAE" property="mpciId_UAE" styleClass="form-control light_border" maxlength="10" onkeyup="this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');" />
                        </div> 
                    </div>
                <%}%>
            </div>
        </div>
    </div>

</fieldset>
<script type="text/javascript">
    $(document).ready(function() {
        $('[data-toggle="tooltip"]').tooltip();
        
        $('#blType_UAE').on('change', function() {
            toggleMpciIdMandatory();
            // Clear BL Type error when value is selected
            if ($('#blType_UAE').val() != '') {
                $('#blType_UAE').removeClass('error');
                $('#blType_UAE-error').hide();
                $('#blType_UAE').parent().find('label.error').hide();
            }
        });
        
        toggleMpciIdMandatory();
    });
    
    function toggleMpciIdMandatory() {
        var blTypeVal = $('#blType_UAE').val();
        if (blTypeVal === 'Master BL') {
            $('#mpciIdMandatory').show(); 
            $('#mpciId_UAE').addClass('required');
            $('#mpciId_UAE').prop('disabled', false);
        } else {
            $('#mpciIdMandatory').hide();
            $('#mpciId_UAE').removeClass('required');
            // Clear error message when MPCI ID is no longer mandatory
            $('#mpciId_UAE').removeClass('error');
            $('#mpciId_UAE-error').hide();
            $('#mpciId_UAE').parent().find('label.error').hide();
            $('#mpciId_UAE').prop('disabled', true);
            $('#mpciId_UAE').val('');
        }
    } 
</script>