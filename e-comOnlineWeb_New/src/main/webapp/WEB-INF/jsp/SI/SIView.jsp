<%@page import="net.ecom.vds.serviceVo.BLChargeVo"%>
<%@page contentType="text/html; charset=UTF-8" import="java.util.ArrayList, java.util.HashMap,java.util.Set,java.util.Hashtable,net.ecom.vo.SIDtableVO, net.ecom.vo.SIPartiesVO,net.ecom.vo.SIContainerVO,java.util.List,net.ecom.common.Validation,java.util.Map,net.ecom.vo.SIContainerDetailVO,net.ecom.vo.SIMasterVO,net.ecom.vo.SISealVO,net.ecom.vo.SIConatainerValueVO,net.ecom.common.utils.DateConversion_New,net.ecom.vo.ChargesVo,net.ecom.common.GeneralConf,net.ecom.common.NumberFormat,net.ecom.common.utils.MaskPortUtil"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    session.setAttribute("SISEND", "SISend");
    String blno = (String) session.getAttribute("edit_blno") != null ? (String) session.getAttribute("edit_blno") : "";
    String clientcode = (String) session.getAttribute("clientcode");
    String Ventilationlabel = (String) getServletContext().getAttribute("Ventilation");
    String agencyRefNo = (String) request.getAttribute("SIAGENCYREFNO") != null ? (String) request.getAttribute("SIAGENCYREFNO") : "";
    String sino_new = (String) session.getAttribute("edit_sino") != null ? (String) session.getAttribute("edit_sino") : "";
    List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String enableaesno = (String) getServletContext().getAttribute(GeneralConf.ENABLE_AES_NUMBER);
    String loadportcountry = (String) request.getAttribute("LoadportCountry");
    String dischargeportcountry = (String) request.getAttribute("dischargeportcountry");
    String SHOW_CHARGES_IN_SIBL = (String) getServletContext().getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
    String Display_Charge_Tab = (String) getServletContext().getAttribute(GeneralConf.Display_Charge_Tab);
    List Chargedetails = (List) request.getAttribute("CHARGEDETAILS");
    List<BLChargeVo> Chargesummary = ( List<BLChargeVo>) session.getAttribute("SIBLChargeSummary");
    String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
    List EBK_pluginList = (List) getServletContext().getAttribute("EBK_PLUGIN_DETAILS");
    boolean is_SI_restriction = false;
    if (EBK_pluginList != null && EBK_pluginList.contains("ESP")) {
        is_SI_restriction = true;
    }
    List addInfoList = (List) getServletContext().getAttribute(GeneralConf.AdditionalInfoBkg);
    boolean is_SI_Addinfo = false;
    if (addInfoList != null && addInfoList.contains("ACD")) {
        is_SI_Addinfo = true;
    }
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
    String HIDE_MASKPORT_CONTAINER = (String) getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER);
    String isinsurance = null, quoteno = "", quotenostr = null;
    String Enable_BLType_Selection = (String) getServletContext().getAttribute(GeneralConf.Enable_BLType_Selection);

    String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    boolean is_SI_OOG = false;
    if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
        is_SI_OOG = true;
    }
    String EnableReeferDetails = (String) getServletContext().getAttribute(GeneralConf.EnableReeferDetails);
    String EnableSIFreightOption = (String) getServletContext().getAttribute(GeneralConf.Enable_SI_Freight_Option);
    String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);
    String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
    String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
     String Enable_T2LFiling = (String) getServletContext().getAttribute(GeneralConf.Enable_T2LFiling);
     String Enable_NVOCC_In_SI = (String) getServletContext().getAttribute(GeneralConf.Enable_NVOCC_In_SI);
     String SI_NVOCC_Eligible_Country = (String) getServletContext().getAttribute(GeneralConf.SI_NVOCC_Eligible_Country);
     String Enable_ReleaseType = (String) getServletContext().getAttribute(GeneralConf.Enable_Telex_ReleaseType);
     String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
     String enbleafricaReqsMandatory = (String) getServletContext().getAttribute(GeneralConf.Enable_africaReqsMandatory);
    long jsdate = new java.util.Date().getTime();
    String enableENSTab = "N";
    if ("Y".equalsIgnoreCase(GENS)) {
        enableENSTab = (String) session.getAttribute("IsEnableENSTabInSIBL");
    }
    boolean showNvocc = false;
    if ("Y".equalsIgnoreCase(Enable_NVOCC_In_SI) && dischargeportcountry != null && SI_NVOCC_Eligible_Country != null) {
        for (String cty : SI_NVOCC_Eligible_Country.split(",")) {
            if (dischargeportcountry.equalsIgnoreCase(cty.trim())) {
                showNvocc = true;
                break;
            }
        }
    }
    String t2lfiling="",ensfiling="",enstype="",nvocc="";
     HashMap hm_rout = (HashMap) session.getAttribute("bk_port_details");
                        SIPartiesVO parties_rout = (SIPartiesVO) hm_rout.get("routing");
    if("Y".equalsIgnoreCase(Enable_T2LFiling)){
                        if (parties_rout != null) {
                            t2lfiling = "Y".equalsIgnoreCase(parties_rout.getEbook_t2lfiling())?"Yes":"No";
     }
    }
    if(showNvocc){
                        String nvoccVal = null;
                        Object formObj = request.getAttribute("SIEditForm");
                        if (formObj instanceof net.ecom.formbeans.SIEditForm) {
                            nvoccVal = ((net.ecom.formbeans.SIEditForm) formObj).getEbook_nvocc();
                        }
                        if (nvoccVal == null && parties_rout != null) {
                            nvoccVal = parties_rout.getEbook_nvocc();
                        }
                        nvocc = "Y".equalsIgnoreCase(nvoccVal)?"Yes":"No";
     }
    if("Y".equalsIgnoreCase(TENS_Documentation)){
                        if (parties_rout != null) {
                            String ensfl=parties_rout.getEbook_EnsFiling();
                            System.out.println("ensfl=>"+ensfl+"entype"+parties_rout.getEbook_EnsDocType());
                            ensfiling = "N".equalsIgnoreCase(ensfl)?"NA":"S".equalsIgnoreCase(ensfl)?"Single":"P".equalsIgnoreCase(ensfl)?"Partial":"";
                            enstype = "S".equalsIgnoreCase(parties_rout.getEbook_EnsDocType())?"Straight B/L":"C".equalsIgnoreCase(parties_rout.getEbook_EnsDocType())?"Consolidated":"";
     }
    }
    String orgTelType="";
    if("Y".equalsIgnoreCase(Enable_ReleaseType)){
                        if (parties_rout != null) {
                            orgTelType = "O".equalsIgnoreCase(parties_rout.getOrgTelType())?"Original":"Telex";
     }  
    }
    String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
    System.out.println("[ECOM-WAR] SIView Enable_Zip_Code==>" + Enable_Zip_Code);
    String enableAddinfoBltype = (String) getServletContext().getAttribute(GeneralConf.ENABLE_ADDINFO_BLTYPE);
%>
<%! int tablerowcount = 0;%>
<style>
    /*    .tooltip-inner {
            background-color: #1FBBA6;
            color: #fff;
        }
    
        .tooltip.right .tooltip-arrow  {
            border-right-color: #1FBBA6;
            color: #fff;
        }
        textarea {
            resize: none;
        }*/
</style>
<style>
    /*    .form-control[readonly]{
            background-color: #fff;
        }
        textarea.form-control {
            height: auto !important;
        }*/
</style>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />  <bean:message key="si.head.siview" bundle="<%=clientcode%>"/><img class="back_btn" onclick="backtosiview()" src="Design/assets/images/page_icons/back_btn.svg" title="Back" /> </div>
<div class="details_search page_bottom_content">
    <html:form method="post" action="SubmitSI.do" styleId="SIEditForm" styleClass="form-horizontal">
        <html:hidden   property="sino" styleId="sino" />

        <div class="card card-shadow">

            <div class="card-header preview_header"> <bean:message key="ebooking.custdetails.display" bundle="<%=clientcode%>"/> </div>
            <div class="card-body">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><bean:message key="shipping.Bookingno" bundle="<%=clientcode%>"/> :  </label>

                    <label class="col-sm-2 control-label"> <%=(String) session.getAttribute("edit_bookno")%>  </label>
                    <%if (sino_new.length() > 0) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.sino" bundle="<%=clientcode%>"/> : </label>

                    <label class="col-sm-2 control-label"> <%=(String) session.getAttribute("edit_sino")%> </label>
                    <%}%>
                    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                    <label class="col-sm-2 float-right "> <button type="button" onclick="printSI();" class="btn btn-info " style="padding: 3px 45px;font-weight: bold;">Print</button></label>
                    <%}%>
                </div>

                <div class="form-group">

                    <%if (blno.length() > 0) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.blno" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-2 control-label"> <%=(String) session.getAttribute("edit_blno")%>  </label>
                    <%}%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.agency.refno" bundle="<%=clientcode%>"/> : </label>

                    <label class="col-sm-2 control-label"> <%=agencyRefNo%> </label>

                </div>
                <div class="form-group">
                    <%if ("Y".equalsIgnoreCase(Enable_BLType_Selection)) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.bl.type" bundle="<%=clientcode%>"/> : </label>
                    <div class="col-sm-4">
                        <label class="form-control input-sm" style="border: none;"><bean:write name="SIEditForm" property="bltype"/></label>
                    </div>
                    <%}%>

                    <%if ("Y".equalsIgnoreCase(EnableSIFreightOption)) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.freight" bundle="<%=clientcode%>"/> : </label>
                    <div class="col-sm-4">
                        <label class="form-control light_border" style="border: none;"><bean:write name="SIEditForm" property="freightoption"/></label>
                    </div>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(Enable_T2LFiling)) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="ebooking.booking.T2LFiling" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-2 control-label">
                      <%=t2lfiling%>  
                    </label>
                    <%}%>
                    <%if (showNvocc) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="ebooking.booking.NVOCC" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-2 control-label">
                      <%=nvocc%>  
                    </label>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                    <label class="col-sm-2 control-label"> ENS Filing : </label>
                    <label class="col-sm-2 control-label">
                      <%=ensfiling%>  
                    </label>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(Enable_ReleaseType)) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="ebooking.booking.releaseType" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-1 control-label">
                     <%=orgTelType%>
                    </label>
                    <%}%>
                </div> 
                 <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                <div class="form-group">
                
                    <label class="col-sm-2 control-label"> Documentation Type : </label>
                    <label class="col-sm-2 control-label">
                      <%=enstype%>  
                    </label>
                </div>
                <%}%>
                <hr/>
                <div class="row">
                    <div class="col col-md-4">
                        <div id="shipper" class="panel_right">
                            <div class="custom-panel-head"> <bean:message key="shipping.booking.party.shipperType" bundle="<%=clientcode%>"/> </div>     
                            <div class="row">
                                <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                            </div>

                            <div class="row">
                                <div class="col-sm-12 form-group">
                                    <html:text property="spnameheader" styleId="ebook_shippernameheader" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <html:textarea styleId="headershipper" property="headerShipper"  styleClass="form-control required" rows="5" readonly="true">
                                    </html:textarea>
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
                                <html:text  property="consnameheader" styleId="ebook_consigneenameheader" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <html:textarea styleId="headerconsignee" property="headerConsignee"  styleClass="form-control required" rows="5" readonly="true">
                                </html:textarea>
                            </div>
                        </div>


                    </div>


                    <div class="col-md-4 ">

                        <div class="custom-panel-head"> <bean:message key="Booking.party.notify" bundle="<%=clientcode%>"/>
                        </div>

                        <div class="row">

                            <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                        </div>

                        <div class="row">
                            <div class="col-sm-12 form-group">
                                <html:text  property="npnameheader" styleId="ebook_notifynameheader" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <html:textarea styleId="headernotify" property="headerNotify"  styleClass="form-control required" rows="5" readonly="true">
                                </html:textarea>
                            </div>
                        </div>

                    </div>
                </div>
                <% if (custtypelist.contains("N1")) {%>
                <div class="row">

                    <div class="col-md-12">
                        <div class="normalpanel" id="nt_id2">


                            <div class="custom-panel-head"><bean:message key="Booking.party.notify1" bundle="<%=clientcode%>"/>
                                <span class="pull-right"><a data-toggle="collapse" href="#collapseNotify2" aria-expanded="false" aria-controls="collapseNotify2"><i class="fa fa-plus-square-o"></i></a></span> </div>
                        </div>
                        <div class="collapse" id="collapseNotify2">
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2name" styleId="ebook_notifyname2" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2addr" styleId="ebook_notifyaddress2" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">

                                    <html:text  property="np2addr1" styleId="ebook_notifyaddress2" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>
                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="np2city" styleId="ebook_notifycity2"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2CountryName" styleId="ebook_notifycountry_name2" styleClass="form-control required input-sm" readonly="true" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="np2phone" styleId="ebook_notifyphone2" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>
                            </div>


                            <div class="row form-group">


                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2fax" styleId="ebook_notifyfax2" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>
                                <%}%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="np2email" styleId="ebook_notifyemail2" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="np2state" styleId="ebook_notifystate2" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <html:text property="np2zipcode" styleId="ebook_notify2zipcode" styleClass="form-control zip_code input-sm" maxlength="15" readonly="true"/>
                                </div>
                                <%}%>
                            </div>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="np2eori" styleId="ebook_notify2eori" styleClass="form-control input-sm" maxlength="17" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="np2persontype" styleId="notify2typeperson" styleClass="form-control  input-sm" readonly="true"/>

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

                            <div class="custom-panel-head"><bean:message key="Booking.party.notify2" bundle="<%=clientcode%>"/> <span class="pull-right"><a data-toggle="collapse" href="#collapseNotify3" aria-expanded="false" aria-controls="collapseNotify3"><i class="fa fa-plus-square-o"></i></a></span> </div></div>
                        <div class="collapse" id="collapseNotify3">
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3name" styleId="ebook_notifyname3" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3addr" styleId="ebook_notifyaddress3" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">
                                    <html:text  property="np3addr1" styleId="ebook_notifyaddress3" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                            </div>

                            <div class="row form-group">



                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="np3city" styleId="ebook_notifycity3"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3CountryName" styleId="ebook_notifycountry_name3" styleClass="form-control required input-sm" readonly="true" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="np3phone" styleId="ebook_notifyphone3" styleClass="form-control input-sm" maxlength="20" readonly="true"/>
                                </div>
                            </div>


                            <div class="row form-group">
                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3fax" styleId="ebook_notifyfax3" styleClass="form-control input-sm" maxlength="20" readonly="true"/>
                                </div>
                                <%}%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="np3email" styleId="ebook_notifyemail3" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="np3state" styleId="ebook_notifystate3" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <html:text property="np3zipcode" styleId="ebook_notify3zipcode" styleClass="form-control zip_code input-sm" maxlength="15" readonly="true"/>
                                </div>
                                <%}%>
                            </div>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="np3eori" styleId="ebook_notify3eori" styleClass="form-control input-sm" maxlength="17" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="np3persontype" styleId="notify3typeperson" styleClass="form-control  input-sm" readonly="true"/>

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

                            <div class="custom-panel-head"> <bean:message key="ecom.bkg.custfw" bundle="<%=clientcode%>"/>
                                <span class="pull-right"><a data-toggle="collapse" href="#collapseForwarder" aria-expanded="false" aria-controls="collapseForwarder"><i class="fa fa-plus-square-o"></i></a></span> </div>
                        </div>
                        <div class="collapse" id="collapseForwarder">
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwname" styleId="ebook_forwardername" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwaddr" styleId="ebook_forwarderaddress" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">

                                    <html:text  property="fwaddr1" styleId="ebook_forwarderaddress1" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>
                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="fwcity" styleId="ebook_forwardercity"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwCountryName" styleId="ebook_forwardercountry_name" styleClass="form-control required input-sm" readonly="true" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="fwphone" styleId="ebook_forwarderphone2" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>
                            </div>


                            <div class="row form-group">

                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwfax" styleId="ebook_forwarderfax" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>
                                <%}%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="fwemail" styleId="ebook_forwarderemail" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="fwstate" styleId="ebook_forwarderstate3" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <html:text property="fwzipcode" styleId="ebook_forwarderzipcode" styleClass="form-control zip_code input-sm" maxlength="15" readonly="true"/>
                                </div>
                                <%}%>
                            </div>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="fweori" styleId="ebook_fweori" styleClass="form-control input-sm" maxlength="17" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="fwpersontype" styleId="fwtypeperson" styleClass="form-control  input-sm" readonly="true"/>

                                </div>
                            </div>
                            <%}%>
                        </div>
                    </div>
                </div>
                <%}%>
                 <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
               <jsp:include page="/WEB-INF/jsp/SI/SIBLCommonAddCustomer.jsp">
              <jsp:param name="custype" value="Seller" />
              <jsp:param name="id" value="1" />
              <jsp:param name="flag" value="true" />
             </jsp:include>   
             <jsp:include page="/WEB-INF/jsp/SI/SIBLCommonAddCustomer.jsp">
             <jsp:param name="custype" value="Buyer" />
             <jsp:param name="id" value="1" />
             <jsp:param name="flag" value="true" />
             </jsp:include>
                <%}%>
            </div>
        </div>
        <div class="card card-shadow">

            <div class="card-header preview_header"> <bean:message key="ecom.shippingins.headinfo" bundle="<%=clientcode%>"/>   </div>
            <div class="card-body">
                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="Tracking.origin" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <html:text property="hid_pla_free_txt" styleId="hid_pla_free_txt" readonly="true"  styleClass="form-control required input-sm" maxlength="149"/>

                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <html:text property="pla_free_txt" styleId="pla_free_txt"  styleClass="form-control required input-sm" maxlength="149" readonly="true"/>

                    </div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.portofload.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <html:text property="hid_pol_free_txt" styleId="hid_pol_free_txt" readonly="true"  styleClass="form-control required input-sm" maxlength="149"/>
                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <html:text property="pol_free_txt" styleId="pol_free_txt"  styleClass="form-control required input-sm" maxlength="149" readonly="true"/>

                    </div>

                </div>


                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.portofdischarge.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <html:text property="hid_pod_free_txt" styleId="hid_pod_free_txt" readonly="true" styleClass="form-control required input-sm" maxlength="149"/>
                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">

                        <html:text property="pod_free_txt" styleId="pod_free_txt" styleClass="form-control required input-sm" maxlength="149" readonly="true"/>

                    </div>

                </div>


                <div class="row form-group">

                    <label class="col-sm-2 control-label"> <bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <html:text property="hid_pld_free_txt" styleId="hid_pld_free_txt" readonly="true" styleClass="form-control required input-sm" maxlength="149"/>
                    </div>

                    <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                    <div class="col-sm-4 ">
                        <html:text property="pld_free_txt" styleId="pld_free_txt" styleClass="form-control required input-sm" maxlength="149" readonly="true"/>
                        <html:hidden property="vslvoyage_free_txt" styleId="vslvoyage_free_txt"></html:hidden>
                        </div>

                    </div>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key="ecom.shippingins.routedet" bundle="<%=clientcode%>"/> </div>

                </div>
                <div id="siroutingdetailspreview">
                    <%
                        int transittime = 0;
                        HashMap hm_routing = (HashMap) session.getAttribute("bk_port_details");
                        SIPartiesVO parties_routing = (SIPartiesVO) hm_routing.get("routing");
                        if (parties_routing != null) {
                            transittime = DateConversion_New.daysDifferentBetweenTwoDays(parties_routing.getPolat(), parties_routing.getPodat());
                            // Check if POL or POD is a mask port - if true, hide sensitive route details
                            boolean isMaskPortHidden = MaskPortUtil.checkIsMaskPort(parties_routing.getPol(), parties_routing.getPod());
                    %>
                    <div id="siroutingdetails">
                        <div id="routingdetails">
                            <div class="form-group">
                                <div class="row">

                                    <div class="col-xs-12 col-sm-12">

                                        <div class="panel_inner_left txt_box">
                                            <div class="panel_inner_top">
                                                <div class="panel_inner_top_1">
                                                    <table>
                                                        <tr>
                                                            <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                                            <td>
                                                        <dt>
                                                        <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd>
                                                        <dd><strong><%=parties_routing.getPolat()%></strong></dd>
                                                        <dd><%=isMaskPortHidden ? "" : parties_routing.getReceipt()%></dd>
                                                        </dt>
                                                        </td>
                                                        <td><img class="content_icon" src="Design/assets/images/page_icons/Rvessel.svg" /></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="panel_inner_top_2">
                                                    <table>
                                                        <tr>
                                                            <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                                            <td>
                                                        <dt>
                                                        <dd><bean:message key="ecom.schdl.arrv" bundle="<%=clientcode%>"/></dd>
                                                        <dd><strong><%=parties_routing.getPodat()%></strong></dd>
                                                        <dd><%=isMaskPortHidden ? "" : parties_routing.getDelivery()%></dd>
                                                        </dt>
                                                        </td>
                                                        <td><img class="content_icon" src="Design/assets/images/page_icons/Lvessel.svg" /></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="panel_inner_top_3 vessel_info">
                                                    <table>
                                                        <tr>
                                                            <td><img class="header_icon" src="Design/assets/images/page_icons/vessel.svg" /></td>
                                                            <td>
                                                        <dt>
                                                        <dd><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></dd>
                                                        <%if (parties_routing.isIsmaskport()) {%>
                                                        <dd><strong><%=isMaskPortHidden ? "" : parties_routing.getVesselName()%></strong></dd>
                                                        <dd><%=isMaskPortHidden ? "" : parties_routing.getVoyagecode()%><%=isMaskPortHidden ? "" : parties_routing.getBound()%></dd>
                                                        <%}%>
                                                        </dt>
                                                        </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="panel_inner_top_4" style="width: 20%;">
                                                    <table>
                                                        <tr>
                                                            <td><img class="header_icon" src="Design/assets/images/page_icons/clock.svg" /></td>
                                                            <td>
                                                        <dt>
                                                        <dd>  <bean:message key="ecom.schdl.ttime" bundle="<%=clientcode%>"/> : <strong><%=transittime%> days</strong></dd>
                                                        <dd> <bean:message key="ebooking.Haulage.Origin" bundle="<%=clientcode%>"/>: <strong><%=parties_routing.getTransportOrigin()%></strong></dd>
                                                        <dd> <bean:message key="ebooking.Haulage.Destination" bundle="<%=clientcode%>"/> : <strong><%=parties_routing.getTransportDestination()%></strong></dd>
                                                        </dt>
                                                        </td>
                                                        </tr>
                                                    </table>

                                                </div>

                                            </div> 

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%}%>
                </div>
            </div>
        </div>
        <div class="card card-shadow">

            <div class="card-header preview_header"> <bean:message key="shipping.cargo.head" bundle="<%=clientcode%>"/> </div>
            <div class="card-body">
                <div id="cargopreview">
                    <table class="table table-striped table-bordered remove_td_title" id="sitablepreview">
                        <thead>
                            <tr>
                                <th width="5"><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>"/></th>
                                <th width="30%"><bean:message key="ecom.shippingins.cargo.contno" bundle="<%=clientcode%>"/></th>
                                    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                <th width="80%"><bean:message key="adminscreen.description" bundle="<%=clientcode%>"/></th>
                                    <%} else {%>
                                <th width="80%"><bean:message key="ecom.shippingins.cargo.packdesc" bundle="<%=clientcode%>"/></th>
                                    <%}%>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                String requestAction = (String) request.getAttribute("SUBMISSION_EDIT");
                                requestAction = Validation.checkNull(requestAction);
                                String weigthunit = "", volumeunit = "";
                                Hashtable cntDetails = (Hashtable) session.getAttribute("SI_CONTAINER_DETAILS");
                                int cntDtIncr = 1;
                                String dtCmdName = "", dtCmdId = "", dtWtName = "", dtWtId = "", ntWtId = "", ntWtName = "", dtUnitName = "", dtUnitId = "", dtQtyName = "", dtQtyId = "", dtCidName = "", dtCidId = "";
                                String dtCmd = "", dtWt = "", ntWt = "", dtUnit = "", dtQty = "", dtCid = "", soc = "", socname = "", hscodevalue = "", nor = "", settemp = "", tempunit = "", setVentilation = "",sln="",slc="",byn="",byc="",chbl="";
                                String comdId = "", cmdEvt = "", csId = "", csEvt = "", ssId = "", ssEvt = "", wtId = "", ntId = "", qtyId = "", selectOnClickFun = "", cidId = "",buyerName = null,sellerName = null,cmdtyhblno=null,buyerCode=null,sellerCode=null,sellerSelect=null,buyerSelect=null;
                                String cntNo = "", shipperSealName = "", carrierSealName = "", shipperSeal = "", carrierSeal = "", tareweight = "", grossweight = "", eqpType = "", eqpTypeName = "", strIsSelect = "NO", strCargoId = "", strCargoIdName = "", strEqpType = "", strEqpTypeName = "";
                                //added by Shikin 1/11/2012 for PLARPENH-440
                                String dtPkgName = "", pkgId = "", dtPkg = "", dtVolName = "", volId = "", dtVol = "", dtVutName = "", vutId = "", dtVut = "", dtPkgId = "", dtVolId = "", dtVutId = "", pkgEvt = "";
                                String dtMrkName = "", mrkId = "", dtMrk = "", dtDesName = "", desId = "", dtDes = "", dtMrkId = "", dtDesId = "";
                                String oogflag = "", oogleft = "", oogright = "", oogforward = "", oogalt = "", oogtop = "", oogunit = "";
                                StringBuilder selectOnclick = new StringBuilder("");
                                tablerowcount = 0;
                                int alsize = 0;
                                int cntListSize = 0;
                                int selCnt = 0;
                                String strCntOrder = "";
                                String scriptVef = "";
                                int con = 0, aa = 1;
                                int headSize = 1;
                                String rowstyle = "even";
                                Hashtable comm_table = (Hashtable) session.getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                                Hashtable pack_table = (Hashtable) session.getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                                HashMap<SIMasterVO, SIConatainerValueVO> cntMapObj = (HashMap<SIMasterVO, SIConatainerValueVO>) session.getAttribute("containerListForSI");

                                if (cntMapObj != null && cntMapObj.size() > 0) {
                                    alsize = cntMapObj.size();
                                    Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                                    for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                                        cntDtIncr = 1;
                                        con = 0;
                                        comdId = "";
                                        cmdEvt = "";
                                        csId = "";
                                        csEvt = "";
                                        ssId = "";
                                        ssEvt = "";
                                        wtId = "";
                                        ntId = "";
                                        qtyId = "";
                                        cidId = "";
                                        oogflag = "";
                                        strEqpType = "";
                                        strCargoId = "";
                                        selectOnClickFun = "";
                                        strCntOrder = "";
                                        scriptVef = "";
                                        strCargoIdName = "";
                                        strEqpTypeName = "";
                                        socname = "";
                                        SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                                        SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                                        SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                                        cntNo = siMasterVO.getStrCntNo();
                                        con = siMasterVO.getStrSelect();
                                        soc = siMasterVO.getStrsoc();
                                        strEqpType = siMasterVO.getStrEqpType();
                                        strCargoId = siSealVOObj.getStrCargoid();
                                        strIsSelect = siSealVOObj.getStrIsSelect();
                                        shipperSeal = siSealVOObj.getStrShipperSeal();
                                        carrierSeal = siSealVOObj.getStrCarrierSeal();
                                        tareweight = siMasterVO.getTareweight();
                                        grossweight = siMasterVO.getGrossweight();
                                        setVentilation = siMasterVO.getVentilation();
                                        settemp = siMasterVO.getSetTemp();
                                        tempunit = siMasterVO.getTempunit();
                                        oogflag = siMasterVO.getOog();
                                        nor = siMasterVO.getNor();
                                        if ("Y".equalsIgnoreCase(nor)) {
                                            settemp = "";
                                            tempunit = "";
                                            setVentilation = "";
                                        }
                                        csId = "carrierseal" + cntNo + "1";
                                        csEvt = "csChk('" + csId + "');";
                                        ssId = "shipperseal" + cntNo + "1";
                                        ssEvt = "ssChk('" + ssId + "');";
                                        selectOnclick = new StringBuilder("");
                                        selectOnclick.append("setContNo('containerNo");
                                        selectOnclick.append(cntNo);
                                        selectOnclick.append("','");
                                        selectOnclick.append(cntNo);
                                        selectOnclick.append("',this)");
                                        selectOnClickFun = selectOnclick.toString();
                                        eqpType = siMasterVO.getStrType();
                                        socname = cntNo + "#socID#";
                                        strCargoIdName = cntNo + "#cargoID#";
                                        shipperSealName = cntNo + "#shipper#";
                                        carrierSealName = cntNo + "#carrier#";
                                        eqpTypeName = cntNo + "#eqpType#";
                                        strCntOrder = cntNo + "#CNTORDER#";
                                        scriptVef = cntNo + "#Scipt#";
                                        strEqpTypeName = cntNo + "#typeID#";
                                        ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                                        if (cntList != null && cntList.size() > 0) {
                                            cntListSize = 0;
                                            cntListSize = cntList.size();
                                            for (int j = 0; j < cntListSize; j++) {
                                                oogleft = "";
                                                oogright = "";
                                                oogforward = "";
                                                oogalt = "";
                                                oogtop = "";
                                                oogunit = "";
                                                SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                                comdId = "commodity" + cntNo + cntDtIncr;
                                                cmdEvt = "dynamicCommodity('" + comdId + "');";
                                                qtyId = "qty" + cntNo + cntDtIncr;
                                                wtId = "weight" + cntNo + cntDtIncr;
                                                cidId = "cid" + cntNo + cntDtIncr;
                                                dtCmdName = cntNo + "#commodity#";
                                                dtCmdId = "commodity" + cntNo + cntDtIncr;
                                                dtCidName = cntNo + "#cid#";
                                                dtWtName = cntNo + "#weight#";
                                                dtWtId = "weight" + cntNo + cntDtIncr;
                                                dtUnitName = cntNo + "#unit#";
                                                dtUnitId = "unit" + cntNo + cntDtIncr;
                                                dtQtyName = cntNo + "#qty#";
                                                dtQtyId = "qty" + cntNo + cntDtIncr;
                                                ntWtName = cntNo + "#netweight#";
                                                ntWtId = "netweight" + cntNo + cntDtIncr;
                                                dtCmd = siCntDetVo.getCommodityName();
                                                hscodevalue = siCntDetVo.getHsCodeDesc();
                                                dtWt = siCntDetVo.getStrWgt();
                                                ntWt = siCntDetVo.getNettwt();
                                                sln = siCntDetVo.getSlsname();
                                                slc = siCntDetVo.getSlscode();
                                                byn = siCntDetVo.getBysname();
                                                byc = siCntDetVo.getByscode();
                                                chbl = siCntDetVo.getCmdty_hblno();
                                                dtUnit = siCntDetVo.getStrUnit();
                                                dtQty = siCntDetVo.getStrQty();
                                                dtCid = siCntDetVo.getStrCid();

                                                pkgId = "package" + cntNo + cntDtIncr;
                                                pkgEvt = "dynamicPackage('" + pkgId + "');";
                                                volId = "volume" + cntNo + cntDtIncr;
                                                vutId = "vunit" + cntNo + cntDtIncr;
                                                mrkId = "mark" + cntNo + cntDtIncr;
                                                desId = "descp" + cntNo + cntDtIncr;

                                                dtPkgName = cntNo + "#package#";
                                                dtPkgId = "package" + cntNo + cntDtIncr;
                                                dtVolName = cntNo + "#volume#";
                                                dtVolId = "volume" + cntNo + cntDtIncr;
                                                dtVutName = cntNo + "#vunit#";
                                                dtVutId = "vunit" + cntNo + cntDtIncr;
                                                dtMrkName = cntNo + "#mark#";
                                                dtMrkId = "mark" + cntNo + cntDtIncr;
                                                dtDesName = cntNo + "#descp#";
                                                dtDesId = "descp" + cntNo + cntDtIncr;
                                                dtPkg = Validation.checkNull(siCntDetVo.getStrPkg());
                                                dtVol = siCntDetVo.getStrVol();
                                                dtVut = siCntDetVo.getStrVut();
                                                dtMrk = siCntDetVo.getStrMrk();
                                                dtDes = siCntDetVo.getStrDes();

                                                if (dtUnit.equalsIgnoreCase("T")) {
                                                    weigthunit = "Metric Tons";
                                                } else if (dtUnit.equalsIgnoreCase("P")) {
                                                    weigthunit = "Pounds";
                                                } else if (dtUnit.equalsIgnoreCase("K")) {
                                                    weigthunit = "Kilos";
                                                } else {
                                                    weigthunit = dtUnit;
                                                }

                                                if (dtVut.equalsIgnoreCase("M")) {
                                                    volumeunit = "Cubic Meters";
                                                } else if (dtVut.equalsIgnoreCase("F")) {
                                                    volumeunit = "Cubic Feet";
                                                } else {
                                                    volumeunit = dtVut;
                                                }

                                                dtCmd = dtCmd != null && dtCmd.length() > 0 ? dtCmd : "";
                                                hscodevalue = hscodevalue != null && hscodevalue.length() > 0 ? hscodevalue : "";
                                                if (hscodevalue == null || hscodevalue.length() == 0) {
                                                    hscodevalue = siCntDetVo.getHsCode();
                                                }
                                                hscodevalue = hscodevalue != null && hscodevalue.length() > 0 ? hscodevalue : "";
                                                dtPkg = dtPkg != null && dtPkg.length() > 0 ? Validation.checkNull((String) pack_table.get(dtPkg)) : dtPkg;

                                                if (isinsurance == null || (!"Y".equalsIgnoreCase(isinsurance))) {
                                                    isinsurance = Validation.checkNull(siCntDetVo.getIsinsuranceavailable());
                                                }
                                                quotenostr = Validation.checkNull(siCntDetVo.getQuoteno());
                                                System.out.println("[ECOM-WAR] SIView isinsurance==> " + isinsurance + " quoteno=> " + quotenostr);
                                                if (quotenostr.length() > 0 && quotenostr.contains("~~")) {
                                                    quoteno = Validation.checkNull(quotenostr).split("~~")[0];
                                                }
                                                oogleft = Validation.checkNull(siCntDetVo.getItemleft());
                                                oogright = Validation.checkNull(siCntDetVo.getItemright());
                                                oogforward = Validation.checkNull(siCntDetVo.getItemforward());
                                                oogalt = Validation.checkNull(siCntDetVo.getItemaft());
                                                oogtop = Validation.checkNull(siCntDetVo.getItemtop());
                                                oogunit = Validation.checkNull(siCntDetVo.getItemlengthUnit());
                            %>
                            <tr id="<%=cntNo%>">
                                <% if (cntDtIncr == 1) {%>
                                <td valign="top" align="left">
                                    <div class="form-group form-group-sm">
                                        <label class="col-sm-1 "><%=aa%></label>
                                    </div>

                                </td>
                                <td valign="top" align="left">
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.container.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <%if("Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)){%>
                                        <label class="col-sm-5 text-info" id="sicontno_lable">Container <%=aa%></label>
                                        <%} else {%>
                                        <label class="col-sm-5 text-info" id="sicontno_lable"><%=cntNo%></label>
                                        <%}%>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left">SOC</label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sisoc_lable"><%=soc%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.eqpsize.display" bundle="<%=clientcode%>"/> </label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sieqptyow_lable"><%=eqpType%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.carrierseal.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sicarrierseal_lable"><%=carrierSeal%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.shipperseal.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sishipperseal_lable" style="word-break: break-all;"><%=shipperSeal%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.tareweight.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sitareweight_lable"><%=tareweight%></label>
                                    </div>
                                    <%if (is_SI_OOG) {%>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key="ebooking.oog.text.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="oogflag_lable"><%=("Y".equalsIgnoreCase(oogflag) ? "Yes" : "No")%></label>
                                    </div>
                                    <%}%>
                                    <%if ("R".equalsIgnoreCase(strEqpType) && "Y".equalsIgnoreCase(EnableReeferDetails)) {%>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.NOR.display' bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sinor_lable"><%=nor%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sisettemp_lable"><%=NumberFormat.numberFormat(Validation.checkNull(settemp))%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.temp.unit' bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sitempunit_lable"><%=("C".equalsIgnoreCase(tempunit) ? "Celsius" : ("F".equalsIgnoreCase(tempunit) ? "Fahrenheit" : ""))%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.vent' bundle="<%=clientcode%>"/><%= Ventilationlabel%></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-5 text-info" id="sivent_lable"><%=Validation.checkNull(setVentilation)%></label>
                                    </div>
                                    <%}%>
                                </td>
                                <%
                                } else {%>
                                <td></td><td></td>
                                <%}%>
                                <td valign="top" align="left">

                                    <div class="form-group row">
                                        <label class="col-sm-2 font-weight-bold text-left">
                                            <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                            <bean:message key="shipping.commoditygrp.display" bundle="<%=clientcode%>"/>
                                            <%} else {%>
                                            <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                                            <%}%>
                                            <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%> </label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-4 text-info" id="sicmdtylabel"><%=dtCmd%></label>
                                        <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="siqtylabel"><%=dtQty%></label>
                                        <%} else {%>
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="siqtylabel"><%=dtQty%></label>
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
                                        <label class=" col-sm-4 text-info" id="sihscodelabel"><%=hscodevalue%></label>
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.volume.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="sivollabel" ><%=dtVol%> <%=volumeunit%></label>
                                        <!--                                    <label class=" col-sm-2 text-info" id="sivolunitlabel"></label>-->
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
                                        <label class=" col-sm-4 text-info" id="sipacklabel"><%=dtPkg%></label>

                                        <label class=" col-sm-2 font-weight-bold text-left"><bean:message key="shipping.cargoweight.display" bundle="<%=clientcode%>"/></label> <label class=""></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="siwtlabel"><%=Validation.checkNull(dtWt).length() > 0 ? dtWt + " " + weigthunit : "" %></label>
                                        <!--                                    <label class=" col-sm-2 text-info" id="siwtunitlabel"></label>-->
                                    </div>
                                    <!--                                               netweight-->
                                    <div class="form-group row" >
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="ecom.cargo.netweight" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-4 text-info" id="sinetweight_lable" ><%=ntWt%></label>
                                        <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="ebooking.cmdtyhblno.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class="col-sm-2 text-info" id="sicmd_hblno" ><%=chbl%></label>
                                        <%}%>
                                    </div>
                                      <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                                    <div class="form-group row" >
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="ebooking.sellersName.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-4 text-info" id="sislname_lable" ><%=sln%></label>
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="ebooking.buyersName.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="sislcode_lable" ><%=byn%></label>
                                     </div>
                                      <%}%>
                                    <div class="form-group row" >
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.marks.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-9 text-info" id="<%=dtMrkId%>" ><%=dtMrk%></label>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.description.display" bundle="<%=clientcode%>"/></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-9 text-info" id="<%=dtDesId%>"  ><%=dtDes%></label>
                                    </div>
                                    <div class="form-group row">
                                        <div class="col-sm-12 text-right">
                                            <a onclick="markDescView('<%=dtMrkId%>', '<%=dtDesId%>');" class='linkeffect mark_desc_cls' href="javascript:void(0)" title="Marks No. and Commodity Description View" ><bean:message key='ecom.shippingins.marksanddescview' bundle="<%=clientcode%>"/></a>
                                        </div>
                                    </div>      
                                    <%if (is_SI_OOG && "Y".equalsIgnoreCase(oogflag)) {%>
                                    <div class="form-group row">

                                        <label class=" col-sm-2 font-weight-bold text-left">
                                            <bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/>
                                        </label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-4 text-info" id="sioogunitlabel"><%= oogunit%></label>

                                        <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="sioogleftlabel"><%=oogleft%></label>
                                    </div>
                                    <div class="form-group row">

                                        <label class=" col-sm-2 font-weight-bold text-left">
                                            <bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/>
                                        </label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-4 text-info" id="sioogrightlabel"><%=oogright%></label>

                                        <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="sioogforwardlabel"><%=oogforward%></label>
                                    </div>
                                    <div class="form-group row">

                                        <label class=" col-sm-2 font-weight-bold text-left">
                                            <bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/>
                                        </label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-4 text-info" id="sioogaltlabel"><%=oogalt%></label>

                                        <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                        <label class="col-sm-1 text-center">:</label>
                                        <label class=" col-sm-2 text-info" id="sioogtoplabel"><%=oogtop%></label>
                                    </div>
                                    <%}%>
                                </td>

                            </tr>
                            <%
                                                cntDtIncr = cntDtIncr + 1;
                                                tablerowcount++;
                                            }
                                        }
                                        aa++;
                                    }
                                }%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <%if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {%>
        <div class="card card-shadow">
            <div class="card-header preview_header"> <bean:message key="ecom.si.charges" bundle="<%=clientcode%>"/>  </div>
            <div class="card-body">
                <div class="col-sm-12">
                    <div id="previewdatatablebychargediv">
                        <table class="table table-striped table-bordered" id="previewdatatablebycharge">
                            <thead>
                                <tr>
                                    <th title="sno">S.No</th>
                                    <th title="chargeid" class='d-none'>Charge Code</th>
                                    <th title="chargename" >Charge Description</th>
                                    <th title="fprepaidcollect" >Frieght</th>
                                    <th title="custtypeid" >Payor</th>
                                    <th title="payagencyid" >Location</th>
                                    <th title="eqptypeid" >Eqp.type</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    ChargesVo charge = null;
                                    HashMap frieghtmap = new HashMap();
                                    frieghtmap.put("P", "Prepaid");
                                    frieghtmap.put("C", "Collect");
                                    frieghtmap.put("E", "Third Party");
                                    if (Chargedetails != null && Chargedetails.size() > 0) {
                                        for (int i = 0; i < Chargedetails.size(); i++) {
                                            charge = (ChargesVo) Chargedetails.get(i);

                                %>
                                <tr>
                                    <td><%=i + 1%></td>
                                    <td class='d-none'><%=charge.getChargeid()%></td>
                                    <td><%=charge.getChargename()%></td>
                                    <td class="prepaidcollect"><%=frieghtmap.get(charge.getFprepaidcollect().toUpperCase())%></td>
                                    <td class="custtypeid"><%=charge.getCusttypeid()%></td>
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
                            <html:select property="releasetype" styleId="releasetype" styleClass="form-control" disabled="true">
                                <html:option value="">Select</html:option>
                                <html:option value="Normal">Normal</html:option>
                                <html:option value="Express">Express</html:option>
                            </html:select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%}%>             

        <%
            System.out.println("[ECOM-WAR] SIView ENABLE_INSURANCE=> " + ENABLE_INSURANCE+" isinsurance=> " + isinsurance + " " + quoteno);
            if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(isinsurance) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
        <div class="card card-shadow">
            <div class="card-header preview_header"> <bean:message key="booking.cargo.insurance" bundle="<%=clientcode%>" />  </div>
            <div class="card-body" id="SIBLviewinsurance">

            </div>
        </div>
        <%}%>
        <%
            if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
        <div class="card card-shadow">
            <div class="card-header preview_header"> <bean:message key="booking.cargo.additionalproductandcharges" bundle="<%=clientcode%>" />  </div>
            <html:hidden  styleId="cargoprotectid"  property="cargoprotectid" />
            <div class="card-body" id="cargoprotect_table_div">

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
                                <div class="col-sm-4 single-row-radio">
                                    <label>
                                        <html:radio property="euCustZone" value="Y" styleId="euCustZoneY" styleClass="form-control medium-radio"  disabled="true"/>
                                        Yes
                                    </label>
                                    <label>
                                        <html:radio property="euCustZone" value="N" styleId="euCustZoneN" styleClass="form-control medium-radio"  disabled="true"/>
                                        No
                                    </label>
                                </div>


                                <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.of.bl" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-4 ">
                                    <html:text  property="blTypeENS"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                            </div>
                            <div class="row form-group">

                                <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.ens.filling" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-4 ">
                                    <html:text  property="ensFillingType"  styleId="ensFillingType" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>                                   
                                </div>

                                <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.method.of.payment" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-4 ">
                                    <html:text  property="paymentMethod"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
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
                                                <html:text  property="byname"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text  property="byaddr"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                            <div class="col-sm-3">

                                                <html:text  property="byaddr1" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                            </div>
                                        </div>

                                        <div class="row form-group">

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text property="bycity"   styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text  property="byCountryName"  styleClass="form-control required input-sm" readonly="true" />
                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <html:text property="byphone"  styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                            </div>
                                        </div>


                                        <div class="row form-group">

                                            <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text  property="byfax"  styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                            </div>
                                            <%}%>
                                            <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text size="43" property="byemail" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>

                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <html:text  property="bystate" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                            </div>
                                            <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                                            <label class="col-sm-1 control-label"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> </label>
                                            <div class="col-sm-3">
                                                <html:text property="byzipcode" styleClass="form-control input-sm" maxlength="10" readonly="true"/>
                                            </div>
                                            <%}%>

                                        </div>
                                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                        <div class="row form-group">

                                            <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text size="43" property="byeori"  styleClass="form-control input-sm" maxlength="17" readonly="true"/>

                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <html:text  property="bypersontype" styleClass="form-control  input-sm" readonly="true"/>

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
                                                <html:text  property="slname"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text  property="sladdr"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                            <div class="col-sm-3">

                                                <html:text  property="sladdr1" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                            </div>
                                        </div>

                                        <div class="row form-group">

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text property="slcity"   styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                            </div>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text  property="slCountryName"  styleClass="form-control required input-sm" readonly="true" />
                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <html:text property="slphone"  styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                            </div>
                                        </div>


                                        <div class="row form-group">

                                            <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>

                                            <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text  property="slfax"  styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                            </div>
                                            <%}%>
                                            <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text size="43" property="slemail" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>

                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <html:text  property="slstate" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                            </div>
                                            <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                                            <label class="col-sm-1 control-label"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> </label>
                                            <div class="col-sm-3">
                                                <html:text property="slzipcode" styleClass="form-control input-sm" maxlength="10" readonly="true"/>
                                            </div>
                                            <%}%>

                                        </div>
                                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                        <div class="row form-group">

                                            <label class="col-sm-1 control-label"> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3 ">
                                                <html:text size="43" property="sleori"  styleClass="form-control input-sm" maxlength="17" readonly="true"/>

                                            </div>
                                            <label class="col-sm-1 control-label"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>

                                            <div class="col-sm-3">
                                                <html:text  property="slpersontype" styleClass="form-control  input-sm" readonly="true"/>

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
                                <html:text property="declarantName" styleId="declarantName" styleClass="form-control setdata light_border declarantcomclass"   readonly="true"/>
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.address' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantAddress" styleId="declarantAddress" styleClass="form-control setdata light_border declarantcomclass"  readonly="true" />
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.address1' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantAddress1" styleId="declarantAddress1" styleClass="form-control setdata light_border declarantcomclass"  readonly="true" />
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.city' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantCity" styleId="declarantCity" styleClass="form-control setdata light_border declarantcomclass"   readonly="true"/>
                            </div>
                            <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantZipcode" styleId="declarantZipcode" styleClass="form-control setdata light_border declarantcomclass"  readonly="true" />
                            </div>
                            <%}%>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.country' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantCountry" styleId="declarantCountry" styleClass="form-control setdata light_border declarantcomclass"  readonly="true" />
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.state' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantState" styleId="declarantState" styleClass="form-control setdata light_border declarantcomclass"   readonly="true"/>
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.telephone' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantTelephone" styleId="declarantTelephone" styleClass="form-control setdata light_border declarantcomclass"  readonly="true" />
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.eori' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantEORI" styleId="declarantEORI" styleClass="form-control setdata light_border declarantcomclass"  readonly="true" />
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label><bean:message key='shipping.ens.declarant.email' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantEmail" styleId="declarantEmail" styleClass="form-control setdata email light_border declarantcomclass"  readonly="true" />
                            </div>
                            <div class="col-xs-12 col-sm-3">
                                <label class="control-label"><bean:message key='shipping.ens.declarant.fillingtype' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                                <html:text property="declarantFillingType" styleId="declarantFillingType" styleClass="form-control setdata email light_border declarantcomclass"  readonly="true" />

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
                         <table class="table table-striped table-bordered" id="previewdatatablebychargetab">
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
                        <td > <%=("P".equalsIgnoreCase(charge.getPrepaidCollect()) ? "Prepaid" : 
                             ("C".equalsIgnoreCase(charge.getPrepaidCollect()) ? "Collect" : 
                             ("E".equalsIgnoreCase(charge.getPrepaidCollect()) ? "PayAt" : "")))%>
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
            <div class="card-header preview_header"><bean:message key='shipping.addremarks.display' bundle="<%=clientcode%>"/></div>
            <div class="card-body card-shadow">
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Declared Value</label>
                    <div class="col-sm-6">
                        <html:text styleId="declaredValue" property="declaredValue"  styleClass="form-control light_border" readonly="true" maxlength="1500">
                        </html:text>
                    </div>
                </div>

                <% if (is_SI_Addinfo) {%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">ACID</label>
                    <div class="col-sm-3">
                        <html:text styleId="acidValue" property="acidValue"  styleClass="form-control light_border" readonly="true"  maxlength="20">
                        </html:text>
                    </div>
                </div>
                <%}
                    if ("Y".equalsIgnoreCase(enableaesno) && "US".equalsIgnoreCase(loadportcountry)) {%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key='ecom.si.aesno' bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-10">
                        <html:text styleId="siAESnumber" property="siAESnumber"  styleClass="form-control input-si" maxlength="50" readonly="true"/>
                        <span class="text-info"> <bean:message key='ecom.si.aesmsg' bundle="<%=clientcode%>"/></span>
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-10">
                        <label class=" col-sm-8 control-label">
                            <bean:message key='ecom.si.aesdismsg' bundle="<%=clientcode%>"/>

                        </label>
                        <label class=" col-sm-8 control-label AESDisclaimer">
                            <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required - valued at $2,500 or less" disabled="true"/>  <bean:message key='ecom.si.aesdismsg1' bundle="<%=clientcode%>"/>

                        </label>
                        <label class=" col-sm-8 control-label AESDisclaimer">
                            <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required, section 30.39 FTSR, C.A.S" disabled="true"/> <bean:message key='ecom.si.aesdismsg2' bundle="<%=clientcode%>"/>
                        </label>
                        <!-- <label class=" col-sm-8 control-label AESDisclaimer">
                            <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required, section 30.55 (H) FTSR" disabled="true"/>  <bean:message key='ecom.si.aesdismsg3' bundle="<%=clientcode%>"/>
                        </label> -->
                    </div>
                </div>
                <%}%>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key='shipping.addremarks.display' bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-10">
                        <html:textarea styleId="sicustremarks" property="sicustremarks"  styleClass="form-control" rows="3" readonly="true"></html:textarea>
                        </div>
                    </div>
                       <% if ("Y".equalsIgnoreCase(enbleafricaReqsMandatory)) {%>
                     <div class="form-group row">
                    <label class="col-sm-2 control-label">Packing List</label>
                    <div class="col-sm-3">
                        <html:text styleId="packinglst" property="packinglst"  styleClass="form-control light_border" readonly="true"  maxlength="30">
                        </html:text>
                     </div>
                    </div>
                    <div class="form-group row">
                    <label class="col-sm-2 control-label">Invoice Upload</label>
                    <div class="col-sm-3">
                        <html:text styleId="invoiceupload" property="invoiceupload"  styleClass="form-control light_border" readonly="true"  maxlength="30">
                        </html:text>
                     </div>
                    </div>
                  <%}%>
                  <% if ("Y".equalsIgnoreCase(enableAddinfoBltype) && "AE".equalsIgnoreCase(dischargeportcountry)) {%>
                  <div class="form-group row">
                    <label class="col-sm-2 control-label">BL Type - UAE Specific</label>
                    <div class="col-sm-3">
                        <html:text styleId="blType_UAE" property="blType_UAE"  styleClass="form-control light_border" readonly="true"  maxlength="30">
                        </html:text>
                     </div>
                  </div>
                  <div class="form-group row">
                    <label class="col-sm-2 control-label">MPCI Id</label>
                    <div class="col-sm-3">
                        <html:text styleId="mpciId_UAE" property="mpciId_UAE"  styleClass="form-control light_border" readonly="true"  maxlength="10">
                        </html:text>
                     </div>
                  </div>
                  <%}%>
                    <div class="text-right">
                        <button type="button" class="btn btn-primary btn-sm" onclick="backtosiview()"> <bean:message key='common.back.button' bundle="<%=clientcode%>"/></button>
                </div>
            </div>
        </div>
    </html:form>
</div>


<!--marks and desc view modal-->
<div class="modal fade" id="markdescmodal" role="dialog" tabindex='-1'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title"><bean:message key='ecom.shippingins.marksanddescview' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <label class="col-sm-12"><bean:message key='shipping.marks.display' bundle="<%=clientcode%>"/></label>
                        </div>
                        <hr/>
                        <div class="form-group row">
                            <div class="col-sm-12">
                                <textarea name="modalmarkid" id="modalmarkid" value="" class="form-control" rows="3" readonly="true" style="background-color: #fff;"></textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label class="col-sm-12"><bean:message key='ecom.shippingins.commdescview' bundle="<%=clientcode%>"/></label>
                        </div>
                        <hr/>
                        <div class="form-group row">
                            <div class="col-sm-12">
                                <textarea name="modaldescid" id="modaldescid" value="" class="form-control" rows="9" readonly="true" style="background-color: #fff;"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="text-right"><a href="javascript:void(0)" title="Close"  class="btn btn-info btn-xs" onclick="markDescClose()" id="mailbtn">  <bean:message key="ecom.close" bundle="<%=clientcode%>"/></a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--Marks and desc modal view end-->
<script type='text/javascript' src='js/SIBLCommonJS.js?<%=jsdate%>'></script>
<script type="text/javascript">
                            $(document).ready(function () {
                                $('[data-toggle="tooltip"]').tooltip();
                                if($("#ensFillingType"))
                                {
                                    if($("#ensFillingType").val()=='Single Filing'){
                                        $("#buyerseller_preview").show();
                                    } else if($("#ensFillingType").val()=='Multiple Filing'){
                                        $("#supplementarydeclarant_preview").show();
                                    }
                                }
//                                var form = $("#SIEditForm").show();
//                                form.steps({
//                                    headerTag: "h3",
//                                    bodyTag: "fieldset",
//                                    autoFocus: true,
//                                    enableAllSteps: true,
//                                    enablePagination: false
//                                });
//                                $(".steps li").addClass("done");
//                                $(".steps li.first").removeClass("done");
//                                $(".steps li a span.number").addClass("glyphicon glyphicon-ok").text("");
//
                            });
                            function backtosiview() {
                                //  callLoading();
                                location.href = "ShippingInstruction.do";
                            }
                            function markDescView(markval, descval) {
                                // var markval = $('#' + marksNoId).val();
                                // var descval = $('#' + descId).val();
                                $('#modalmarkid').val($('#' + markval).text());
                                $('#modaldescid').val($('#' + descval).text());
                                $("#markdescmodal").modal("show");
                            }
                            function markDescClose() {
                                $("#markdescmodal").modal("hide");
                            }
                            viewinsurance('<%=quoteno%>');
                            function viewinsurance(quote) {
                                if (quote != '' && quote.length > 0) {
                                    //openLoader();
                                    $.ajax({
                                        type: "POST",
                                        cache: false,
                                        url: "InsuranceAjax?type=quoteview&quoteno=" + quote,
                                        async: false,
                                        data: $(this).serializeArray(),
                                        success: function (data) {
                                            $('#SIBLviewinsurance').html(data);
                                            //closeLoader();
                                            /* $.fancybox.open(data, {
                                             padding: 0,
                                             showCloseButton: false,
                                             closeClick: false,
                                             maxWidth: 1200,
                                             maxHeight: 600,
                                             fitToView: true,
                                             async: false,
                                             width: '90%',
                                             height: '100%',
                                             autoSize: true,
                                             scrollOutside: true,
                                             closeBtn: true,
                                             hideOnOverlayClick: false,
                                             overlayShow: false,
                                             helpers: {
                                             overlay: {closeClick: false}
                                             }
                                             
                                             });*/
                                        },
                                        error: function (xhr) {
                                            if (xhr.status == 403) {
                                                window.location.href = "SessionExpired.jsp";
                                            }
                                        }
                                    });
                                }
                            }
                            function printSI() {
                                $.ajax({
                                    type: "POST",
                                    cache: false,
                                    url: "SIAjax?type=PrintSIData",
                                    async: false,
                                    data: $(this).serializeArray(),
                                    success: function (data) {
                                        var newWin = window.open('', 'Print-Window');
                                        newWin.document.open();
                                        newWin.document.write('<html><body onload="window.print()">' + data + '</body></html>');
                                        newWin.document.close();
                                    },
                                    error: function (xhr) {
                                        if (xhr.status == 403) {
                                            window.location.href = "SessionExpired.jsp";
                                        }
                                    }
                                });

                            }
                            loadCargoProtect('view');
    function openCollapseFun(id) {
        if ($('#' + id).css('display') == 'none') {
            $('#' + id).slideDown();
            $('#' + id).css("visibility", "inherit");
            $('#' + id).parent().find(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
        } else {
            $('#' + id).slideUp();
            $('#' + id).parent().find(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
        }

    }
</script>
