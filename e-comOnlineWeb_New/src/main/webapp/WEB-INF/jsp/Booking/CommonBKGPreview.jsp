<%@page contentType="text/html"   pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page import="java.util.Locale,java.util.ArrayList,java.util.LinkedHashMap,org.apache.struts.util.MessageResources,org.apache.struts.action.ActionForm" %>
<%@page import="net.ecom.formbeans.EbookingValidatorFormBeanNew,java.util.Date,net.ecom.vo.Route,net.ecom.common.Validation,net.ecom.common.GeneralConf,java.util.List,java.util.Map,net.ecom.vo.TransportServiceVO,net.ecom.vo.CustomerDetailsVO,net.ecom.vo.CutoffConfigVO,java.util.Hashtable,java.util.Set,net.ecom.vo.BookingHaulagedetailsVo,net.ecom.common.utils.DateConversion_New"%>
<%
    try {
        String cacheVersion = (String) getServletContext().getAttribute("CACHEVERSION");
        cacheVersion = cacheVersion != null && cacheVersion.length() > 0 ? "?" + cacheVersion : "";
        String manageTemplage = (String) request.getAttribute("MANAGETEMPLATE");
        System.out.println("[ECOM-WAR] CommonBKGPreview >manageTemplage>>" + manageTemplage);
        String ccMailId = Validation.checkNull((String) session.getAttribute("SESSION_USER_CC_MAILID"));
        System.out.println("[ECOM-WAR] CommonBKGPreview ccMailId>>>>in jsp>>" + ccMailId);
        String hideMinMaxTemp = (String) getServletContext().getAttribute(GeneralConf.Hide_Min_MaxTemp);
        String custTypeConfig = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.APBPCONFIGFRONTEND));
        String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
        //String emptyPlaceWithoutTerminal = (String) getServletContext().getAttribute(GeneralConf.EMPTYPLACEWITHOUTTERMINAL);
        //String emptyPlaceWithTerminal = (String) getServletContext().getAttribute(GeneralConf.EMPTYPLACEWITHTERMINAL);
        String pdfpath = (String) getServletContext().getAttribute("Imagepath");
        String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
        String bookingType = ((String) session.getAttribute("type"));
        String user = (String) session.getAttribute("sessionAdmin");
        String allowIncompRegPlugin = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN));
        String incompleteRegSession = Validation.checkNull((String) session.getAttribute("SESSION_INCOMPLETED_REG"));
        boolean isKycPending = "Y".equalsIgnoreCase(allowIncompRegPlugin) && "Y".equalsIgnoreCase(incompleteRegSession);
        List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
        String clientcode = (String) session.getAttribute("clientcode");
        String userFruitCust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
        System.out.println("[ECOM-WAR] CommonBKGPreview fruitcust" + userFruitCust);
        System.out.println("[ECOM-WAR] CommonBKGPreview Preview_Booking_Type => " + request.getAttribute("Preview_Booking_Type"));
        String enableActCommoName = (String) getServletContext().getAttribute(GeneralConf.EnableActualCommoName);
        String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
        String bkgpreviewtype = (String) request.getAttribute("Preview_Booking_Type");
        List getAddInfo = (List) getServletContext().getAttribute(GeneralConf.AdditionalInfoBkg);
        List addInfoList = new ArrayList();
        if (getAddInfo != null && getAddInfo.size() > 0) {
            addInfoList.addAll(getAddInfo);
        }
        String addInfoID = null;
        String Enable_Drains_Humidity = (String) getServletContext().getAttribute(GeneralConf.Enable_Drains_Humidity);
        String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
         String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
        boolean is_ecom_OOG = false;
        if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
            is_ecom_OOG = true;
        }
        String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);
        String Ventilationlabel = (String) getServletContext().getAttribute("Ventilation");
        String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
        String TENS = (String) getServletContext().getAttribute(GeneralConf.TENS);
        String Enable_T2LFiling = (String) getServletContext().getAttribute(GeneralConf.Enable_T2LFiling);
        String preferAgency = (String) getServletContext().getAttribute(GeneralConf.Enable_PreferredAgency);
        String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
        System.out.println("TENS_Documentation==>" + TENS_Documentation);
        String Enable_SISubmit_Party = (String) getServletContext().getAttribute(GeneralConf.Enable_SISubmit_Party);
        String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
        String Hide_AP_CUST_BKG_Master = (String) getServletContext().getAttribute(GeneralConf.Hide_AP_CUST_BKG_Master);
       
%>
<style>
    .siparty_disclaimer1{
        float: left !important;
        font-weight: bold !important;
        margin-left: 10px;
        text-align: right !important;
        color:  #009ace;
    }
    .siparty_disclaimer2{
        float: left !important;
        margin-left: 10px;
        margin-right: 20px;
        color:  #009ace;
    }
</style>
<h6><i class="step-icon fa_preview"></i><bean:message key="Booking.msg.previewdet" bundle="<%=clientcode%>" /> </h6>

<fieldset>
    <div class="card card-shadow" id='preview_master_details'>

        <div class="card-header preview_header"> <bean:message key="ebooking.masterdetails.display" bundle="<%=clientcode%>" /> <a href="javascript:void(0);" onclick="$('#<%="A".equalsIgnoreCase(bkgpreviewtype) ? "AmendmentBookingFormBean" : "EbookingValidatorFormBeanNew"%>-t-0').click();" class="float-right text-white" > <i class="fa fa-pencil"></i>  </a> </div>
        <div class="card-body">
            <div class="row form-group">

                <label class="col-sm-2 "> <bean:message key="ebooking.ref.no" bundle="<%=clientcode%>" />  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview" id="booking_ref_label"></span>

                <%if (!"Y".equalsIgnoreCase(Hide_AP_CUST_BKG_Master)) {%>
                <label class="col-sm-2 "><bean:message key="ebooking.booking.party" bundle="<%=clientcode%>"/>  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview" id="booking_party_label"></span>
                <%}%>

                <label class="col-sm-2 "> <bean:message key='ebooking.carrier.contact' bundle="<%=clientcode%>"/>  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview labe_width" id="carriercontact_label"></span>

            </div>

            <div class="row form-group">

                <label class="col-sm-2 "> <bean:message key='ebooking.origin.display' bundle="<%=clientcode%>"/>  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview" id="pickupplace_label"></span>


                <label class="col-sm-2 "> <bean:message key='ebooking.Delivery.display' bundle="<%=clientcode%>"/> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview" id="deliveryplace_label"></span>


                <label class="col-sm-2 "> <bean:message key="ebooking.date.display" bundle="<%=clientcode%>" />  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview labe_width" id="bookdate_label"></span>

            </div>

            <div class="row form-group">

                <label class="col-sm-2 "> <bean:message key="ebooking.Haulage.Origin" bundle="<%=clientcode%>"/> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview" id="precarriage_label" ></span>


                <label class="col-sm-2 "> <bean:message key="ebooking.Haulage.Destination" bundle="<%=clientcode%>"/></label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview" id="oncarriage_label"></span>
                 <%if("Y".equalsIgnoreCase(TENS)){%>
                <label class="col-sm-2 "> <bean:message key="ebooking.booking.ENSFiling" bundle="<%=clientcode%>" />  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview labe_width" id="ensfiling_label"></span>
                <%}%>
                
                 <%if("Y".equalsIgnoreCase(preferAgency)){%>
                <label class="col-sm-2 "> <bean:message key="ebooking.preferred.agency" bundle="<%=clientcode%>" />  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview labe_width" id="ebook_preferred_agency_label"></span>
                <%}%>
            </div>
            <div class="row form-group">
                 <%if("Y".equalsIgnoreCase(Enable_T2LFiling)){%>
                <label class="col-sm-2 "> <bean:message key="ebooking.booking.T2LFiling" bundle="<%=clientcode%>"/>  </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview" id="t2lfiling_label"></span>
                <%}%>
                <%if("Y".equalsIgnoreCase(TENS_Documentation)){%>
                    <label class="col-sm-2 "><bean:message key="ebooking.booking.shipmentType" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>
    
                    <span class="col-sm-2 labl_preview labe_width" id="ensDocType_label"></span>
                <%}%>

            </div>
            <%if (addInfoList != null && addInfoList.size() > 0) {%> 

            <div class="form-group">
                <div class="row">
                    <%
                        for (int k = 0; k < addInfoList.size(); k++) {
                            addInfoList.remove("EOP");
                            addInfoID = (String) addInfoList.get(k);
                            if ("EOR".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key='ebooking.rate.reference' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="rateref_label">  </span>
                    <%}
                        if ("AGR".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key='ebooking.agency.reference' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="agencyref_label"></span>
                    <%}
                        if ("EOF".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key='ebooking.ocean.freight' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="oceanfreight_label"> </span>
                    <%}
                        if ("PFR".equalsIgnoreCase(addInfoID)) {%>
                    <%if ("ESL".equalsIgnoreCase(carrierCode)) {%>
                    <label class="col-sm-2 "><bean:message key="ebooking.place.final.receipt.ch" bundle="<%=clientcode%>" /></label>
                    <%} else {%>
                    <label class="col-sm-2 "><bean:message key="ebooking.place.final.receipt" bundle="<%=clientcode%>" /></label>
                    <%}%>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"   id="instransit_label" > </span>
                    <%}
                        if ("NAT".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.booking.natcode" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="natcode_label"></span>

                    <%}
                        if ("ETH".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.haulage.type" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="haulagetype_label"></span>
                    <%}
                        if ("ELD".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.pickup.date" bundle="<%=clientcode%>" /></label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="pickupdate_label"></span>
                    <%}
                        if ("EDD".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.drop.date" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="dropdate_label"></span>
                    <%}
                        if ("HAU".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.hauler.code" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="haulercode_label"> </span>
                    <%}
                        if ("EPO".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.customer.PO" bundle="<%=clientcode%>" /></label>
                    <span class=""> : </span>
                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"   id="custpo_label" > </span>
                    <%}
                        if ("ERT".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.ref.type" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="reftype_label"> </span>
                    <%}
                        if ("ERF".equalsIgnoreCase(addInfoID)) {%>

                    <label class="col-sm-2 "> <bean:message key='ebooking.exp.ref' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="expref_label"></span>

                    <%}
                        if ("EPP".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.empty.pickup" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="emptypickup_label" ></span>
                    <%}
                        if ("EPD".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="book.empty.pickupdate" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="emptypickupdate_label"></span>
                    <%}
                        if ("CRN".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.customer.reference" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="customer_reference_label"></span>
                    <%}
                        if ("ACD".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.book.ACID" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="ACID_label"></span>
                    <%}  if ("DPT".equalsIgnoreCase(addInfoID)) {%>
                        <label class="col-sm-2 "> <bean:message key="ebooking.book.DPWshipperType" bundle="<%=clientcode%>" /> </label>
                        <span class=""> : </span>
                        <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="shipperReferenceType_label"></span>
                    <%}  if ("DPW".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.book.DPWshipperCode" bundle="<%=clientcode%>" /> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="shipperReference_label"></span>
                    <%}%>
                    <%   if ((k + 1) % 3 == 0 && k != addInfoList.size() - 1) {%>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <%}
                        }%>
                </div>
            </div>
            <%}%>
            <div class="row form-group">
                <div class="heading_trainee_bottom_booking ">  </div>

                <div class="heading_trainee_booking "><bean:message key="Booking.head.vesselschddet" bundle="<%=clientcode%>" /> </div>

            </div>

            <div id="previewRoute">
            </div>

        </div>
    </div>

    <div class="card card-shadow">
        <div class="card-header preview_header"><bean:message key='ebooking.custdetails.display' bundle="<%=clientcode%>"/> <a href="javascript:void(0);" onclick="$('#<%="A".equalsIgnoreCase(bkgpreviewtype) ? "AmendmentBookingFormBean" : "EbookingValidatorFormBeanNew"%>-t-1').click();" class="float-right text-white" > <i class="fa fa-pencil"></i>  </a> </div>
        <div class="card-body">
            <div class="txt_side_borderxx">
                <div id='bp_hd'>
                    <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "><bean:message key='ebooking.BookingParty.display' bundle="<%=clientcode%>"/></div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="bookingname_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="bookingadress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>
                        <span class="col-sm-5 labl_preview" id="bookingadress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview" id="bookingcity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="bookingcountry_label" ></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="bookingphone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>
                        <span class="col-sm-2 labl_preview labe_width" id="bookingfax_label"> </span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="bookingemail_label"> </span>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="bookingzipcode_label"> </span>
                        <%}%>

                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="bookingstate_label" ></span>
                      <%if("Y".equalsIgnoreCase(GENS)){%>
                        <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="bookingeori_label" ></span>

                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="bookingpersontype_label" ></span>
                   
                        <%}%>
                    </div>
                    <%}%>

                </div>
                <div id='ap_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='shipping.booking.party.agreementType' bundle="<%=clientcode%>"/>  </div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="agreename_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="agreeadress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="agreeadress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="agreecity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="agreecountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="agreephone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="agreefax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="agreeemail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="agreezipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>
                         <span class="col-sm-2 labl_preview" id="agreestate_label" ></span>
                  <%if("Y".equalsIgnoreCase(GENS)){%>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="agreeeori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="agreepersontype_label" ></span>
                          <%}%> 
                    </div>
                </div>
                <div id='sh_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "><bean:message key='shipping.booking.party.shipperType' bundle="<%=clientcode%>"/></div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="shippername_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="shipperadress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="shipperadress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="shippercity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="shippercountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="shipperphone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="shipperfax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="shipperemail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="shipperzipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>
                         <span class="col-sm-2 labl_preview" id="shipperstate_label" ></span>
             <%if("Y".equalsIgnoreCase(GENS)){%>
                       
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="shippereori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="shipperpersontype_label" ></span>
                          <%}%> 
                        
                    </div> 
                </div>

                <div id='cn_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='shipping.booking.party.consigneeType' bundle="<%=clientcode%>"/></div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="consigneename_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="consigneeadress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="consigneeadress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="consigneecity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="consigneecountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="consigneephone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="consigneefax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="consigneeemail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="consigneezipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="consigneestate_label" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="consigneeeori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="consigneepersontype_label" ></span>
                          <%}%> 
                    </div> 
                </div>   
                <div id='nt_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "><bean:message key='Booking.party.notify' bundle="<%=clientcode%>"/></div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="notifyname_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="notifyadress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="notifyadress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="notifycity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifycountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifyphone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="notifyfax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="notifyemail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="notifyzipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifystate_label" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifyeori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifypersontype_label" ></span>
                          <%}%> 
                    </div>
                </div>
                <% if (custtypelist.contains("N1")) {%>

                <div id='nt1_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='Booking.party.notify1' bundle="<%=clientcode%>"/></div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="notifyname_label_n1"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="notifyadress_label_n1"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="notifyadress1_label_n1"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="notifycity_label_n1"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifycountry_label_n1"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifyphone_label_n1"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="notifyfax_label_n1"></span>
                        <%}%>              
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="notifyemail_label_n1"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="notifyzipcode_label_n1"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifystate_label_n1" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifyeori_label_n1" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="notifypersontype_label_n1" ></span>
                          <%}%> 
                    </div>
                </div>
                <%}%>
                <% if (custtypelist.contains("FW")) {%>

                <div id='fw_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> Forwarder</div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="forwardername_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="forwarderadress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="forwarderadress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="forwardercity_label"></span>

                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="forwardercountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="forwarderphone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="forwarderfax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="forwarderemail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="forwarderzipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="forwarderstate_label" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="forwardereori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="forwarderpersontype_label" ></span>
                          <%}%> 
                    </div>
                </div>   
                <%}%>
                <% if (custtypelist.contains("WH") && false) {%>

                <div id='wh_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking ">Warehouse</div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="Warehousename_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="Warehouseadress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="Warehouseadress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="Warehousecity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Warehousecountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Warehousephone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="Warehousefax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="Warehouseemail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="Warehousezipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Warehousestate_label" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Warehouseeori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Warehousepersontype_label" ></span>
                          <%}%> 
                    </div>
                </div>   

                <%}%> 

                <div id='nc_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custnc' bundle="<%=clientcode%>"/>  </div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="NamedCustomername_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="NamedCustomeradress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="NamedCustomeradress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="NamedCustomercity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="NamedCustomercountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="NamedCustomerphone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="NamedCustomerfax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="NamedCustomeremail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="NamedCustomerzipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="NamedCustomerstate_label" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="NamedCustomereori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="NamedCustomerpersontype_label" ></span>
                          <%}%> 
                    </div>
                </div>   
                <%if("Y".equalsIgnoreCase(TENS) && "N".equalsIgnoreCase(TENS_Documentation)){%>
                <div id='sl_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>  </div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="Sellername_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="Selleradress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="Selleradress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="Sellercity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Sellercountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Sellerphone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="Sellerfax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="Selleremail_label"></span>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="Sellerzipcode_label"> </span>
                        <%}%>
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Sellerstate_label" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Sellereori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Sellerpersontype_label" ></span>
                          <%}%> 
                    </div>
                </div> 
                   <div id='by_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>  </div>

                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="Buyername_label"> </span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="Buyeradress_label"></span>



                    </div>

                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-5 labl_preview" id="Buyeradress1_label"></span>

                        <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-4 labl_preview " id="Buyercity_label"></span>



                    </div>


                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Buyercountry_label"></span>

                        <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Buyerphone_label"></span>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview labe_width" id="Buyerfax_label"></span>
                        <%}%>
                        <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview " id="Buyeremail_label"></span>
                            <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="Buyerzipcode_label"> </span>
                        <%}%>   
                    </div>
                    <div class="row form-group">

                        <label class="col-sm-1 "> <bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Buyerstate_label" ></span>
                        <%if("Y".equalsIgnoreCase(GENS)){%>
                           <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Buyereori_label" ></span>
                        
                        <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                        <span class=""> : </span>

                        <span class="col-sm-2 labl_preview" id="Buyerpersontype_label" ></span>
                          <%}%> 
                    </div>
                </div> 
              

                <%}%>

 <%if("Y".equalsIgnoreCase(TENS_Documentation)){%>


 <div id='sld_hd'>
                    <div class="row form-group">

                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>  </div>

                    </div>   
                    <!-- Dynamic preview container -->
<div id="sellerTypePreviewContainer"></div>

<div id="sellerTypePreviewTemplate" style="display:none;" class="customer-preview ">
    


        <div class="row form-group">
            <label class="col-sm-1"><bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-5 labl_preview" id="CustType_Sellername_label"></span>

            <label class="col-sm-1"><bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-4 labl_preview" id="CustType_Selleradress_label"></span>
        </div>

        <div class="row form-group">
            <label class="col-sm-1"><bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-5 labl_preview" id="CustType_Selleradress1_label"></span>

            <label class="col-sm-1"><bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-4 labl_preview" id="CustType_Sellercity_label"></span>
        </div>

        <div class="row form-group">
            <label class="col-sm-1"><bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Sellercountry_label"></span>

            <label class="col-sm-1"><bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Sellerphone_label"></span>

              <label class="col-sm-1"><bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Selleremail_label"></span> 

          
        </div>

        <div class="row form-group">


            <label class="col-sm-1"><bean:message key="Reg.State" bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Sellerstate_label"></span>

         
        </div>
    
</div>
</div>


<div id='byd_hd'>
                 <!-- Hidden customer type preview template -->
       <div class="row form-group">
                        <div class="heading_trainee_bottom_booking ">  </div>

                        <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>  </div>

                    </div>   
                    <!-- Dynamic preview container -->
<div id="buyerTypePreviewContainer"></div> 

<div id="customerTypePreviewTemplate" style="display:none;" class="customer-preview">
    


        <div class="row form-group">
            <label class="col-sm-1"><bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-5 labl_preview" id="CustType_Buyername_label"></span>

            <label class="col-sm-1"><bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-4 labl_preview" id="CustType_Buyeradress_label"></span>
        </div>

        <div class="row form-group">
            <label class="col-sm-1"><bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-5 labl_preview" id="CustType_Buyeradress1_label"></span>

            <label class="col-sm-1"><bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-4 labl_preview" id="CustType_Buyercity_label"></span>
        </div>

        <div class="row form-group">
            <label class="col-sm-1"><bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Buyercountry_label"></span>

            <label class="col-sm-1"><bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Buyerphone_label"></span>

            <label class="col-sm-1"><bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Buyeremail_label"></span>
            
           
        </div>

        <div class="row form-group">
          
            <label class="col-sm-1"><bean:message key="Reg.State" bundle="<%=clientcode%>"/></label><span>: </span>
            <span class="col-sm-2 labl_preview" id="CustType_Buyerstate_label"></span>

          
        </div>
    
</div>
</div>
 <%}%>

            </div>
        </div>
    </div>
    <div class="card card-shadow">
        <div class="card-header preview_header"><bean:message key="shipping.cargo.head" bundle="<%=clientcode%>" /> <a href="javascript:void(0);" onclick="$('#<%="A".equalsIgnoreCase(bkgpreviewtype) ? "AmendmentBookingFormBean" : "EbookingValidatorFormBeanNew"%>-t-2').click();" class="float-right text-white" > <i class="fa fa-pencil"></i>  </a> </div>
        <div class="card-body">
            <div class="premainrow" id="bkgrowid" style="display:none">
                <div class="form-group row">
<!--                    <label class="col-sm-2"><bean1:message key='ebooking.trust.eqpment' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class="col-sm-2 labl_preview" id="eqptype_lable"></label>-->
                    <label class=" col-sm-2"><bean:message key='ebooking.trust.eqpmentsize' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="eqpsize_lable"></label>
                    <label class="col-sm-1"><bean:message key='<%="A".equalsIgnoreCase(bkgpreviewtype) ? "ebooking.containerno" : "ebooking.trust.eqpmentqty"%>' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <label class="col-sm-2 labl_preview" id="eqpqty_lable"></label>
                    <label class=" col-sm-2"><bean:message key='ebooking.soc.text.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="soc_lable"></label>
                </div>
                <div class="form-group row">
                    <label class=" col-sm-2" ><bean:message key='ebooking.trust.hazardous' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="hzd_lable"></label>
                    <label class=" col-sm-1"><bean:message key='ebooking.eqpstatus.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="eqpstatus_lable"></label>

<!--                                <label class=" col-sm-1"><ban:message key='ebooking.packageqty.display' bundle="<%=clientcode%>"/></label>
<span class=""> : </span>
<label class=" col-sm-3 labl_preview" id="pkgqty_lable"></label>-->
                    <label class=" col-sm-2">
                        <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                        <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                        <%} else {%>
                        <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                        <%}%>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="package_lable"></label>

                </div>
                <div class="form-group row">
                    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                    <label class=" col-sm-2">
                        <bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="pkgqty_lable"></label>
                    <%} else {%>
                    <label class=" col-sm-2">
                        <bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="pkgqty_lable"></label>
                    <%}%>

                    <label class=" col-sm-1"><bean:message key='ebooking.tareweight.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="tareweight_lable"></label>
                    <!--  
                    <label class=" col-sm-2 labl_preview" id="cargowtunit_lable"></label>-->


                    <!--                                <label class=" col-sm-2 labl_preview" id="grosswtunit_lable"></label>-->

<!--                                <label class=" col-sm-1"><ban:message key='ebooking.containervolume.display' bundle="<%=clientcode%>"/></label>
  <span class=""> : </span>
  <label class=" col-sm-1 labl_preview" id="volume_lable"></label>
  <label class=" col-sm-2 labl_preview" id="volunit_lable"></label>-->
                    <label class=" col-sm-2"><bean:message key='ebooking.cargoweight.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="cargowt_lable"></label>

                </div>
                <div class="form-group row">                    
                    <label class=" col-sm-2"><bean:message key='ebooking.grossweight.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="grosswt_lable"></label>
                    <label class=" col-sm-1"><bean:message key='ecom.cargo.netweight' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="netwt_lable"></label>
                    <%if (is_ecom_OOG) {%>
                    <label class=" col-sm-1"><bean:message key='ebooking.oog.text.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="oog_lable"></label>
                    <!--                    <label class=" col-sm-1 labl_preview labe_width" id="volunit_lable"></label>-->
                    <%}%>

                </div>

                <div class="form-group row">

                    <label class=" col-sm-2">
                        <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                        <bean:message key='ebooking.trust.commoditygrp' bundle="<%=clientcode%>"/>
                        <%} else {%>
                        <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/>
                        <%}%>
                        <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%> </label>
                    <span class=""> : </span>
                    <label class=" col-sm-8 labl_preview" ><p id="commodity_lable"></p></label>
<!--                                <label class=" col-sm-1"><ban:message key='ebooking.package.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-3 labl_preview" id="package_lable"></label>-->
                </div>
                <div class="form-group row">
                    <label class=" col-sm-2">
                        <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                        <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/>
                        <%} else if ("Y".equalsIgnoreCase(hsnamecode)) {%>
                        <bean:message key='ebooking.trust.hsnamecode' bundle="<%=clientcode%>"/>
                        <%} else {%>
                        <bean:message key='ebooking.trust.hscode' bundle="<%=clientcode%>"/>
                        <%}%>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-8 labl_preview" id="hscode_lable"></label>
                </div>
                <div class="form-group row">
                    <label class=" col-sm-2"><bean:message key='ebooking.containervolume.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="volume_lable"></label>
                    <%if ("Y".equalsIgnoreCase(enableActCommoName)) {%>

                    <label class=" col-sm-2">
                        <bean:message key="shipping.actcommoname.display" bundle="<%=clientcode%>"/>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-5 labl_preview" id="actcommoname_lable"></label>
                    <%}%>
                    
                </div>
                <div class="form-group row">
                   
                    <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                    <label class=" col-sm-2">
                      <bean:message key='ebooking.cmdtyhblno.display' bundle="<%=clientcode%>"/>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="cmdtyhblno_lable"></label>
                    <label class=" col-sm-1">
                     <bean:message key='ebooking.sellersName.display' bundle="<%=clientcode%>"/>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="sellersName_lable"></label>
                      <label class=" col-sm-1">
                     <bean:message key='ebooking.buyersName.display' bundle="<%=clientcode%>"/>
                    </label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="buyersName_lable"></label>
                    <%}%>
                </div>
            </div>
            <div class="hazardous" id="hzddivpreview"  style="display:none">
                <hr>
                <br>
                <!-- Multiple DG Details Container -->
                <div id="dgDetailsContainer"></div>
            </div>
            <div class="reefer" id="refdivpreview" style="display:none">
                <hr>
                <br>
                <div class="form-group row">
                    <label class=" col-sm-2"><bean:message key='ebooking.NOR.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="nor_lable"></label>
                    <label class="col-sm-1"><bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="settemp_lable"></label>
                    <span class=""> : </span>
                    <%if (!"Y".equalsIgnoreCase(hideMinMaxTemp)) {%>
                    <label class="col-sm-2"><bean:message key='ebooking.min.temp' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="mintemp_lable"></label>
                </div>
                <div class="form-group row">
                    <label class=" col-sm-2"><bean:message key='ebooking.max.temp' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="maxtemp_lable"></label>
                    <%}%>
                    <label class=" col-sm-1"><bean:message key='ebooking.temp.unit' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="tempunit_lable"></label>
                    <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>
                    <label class="col-sm-2">Humidity(%)</label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="humidity_lable"></label>
                    <%}%>
                </div>
                <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>
                <div class="form-group row">
                    <label class=" col-sm-2"><bean:message key='ebooking.drains' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="drains_lable"></label>
                    <label class=" col-sm-1"><bean:message key='ebooking.co2' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="co2_lable"></label>
                    <label class="col-sm-2"><bean:message key='ebooking.o2' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="o2_lable"></label>
                </div>
                <%}%>

                <div class="form-group row">
                    <label class=" col-sm-2"><bean:message key='ebooking.vent' bundle="<%=clientcode%>"/><%= Ventilationlabel %></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="vent_lable"></label>
                </div>
            </div>
            <div class="opencar" id="oogdivpreview"   style="display:none">
                <hr>
                <br>
                <div class="form-group row">
                    <label class="col-sm-2"><bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="dimunit_lable"></label>
                    <label class=" col-sm-1"><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="owleft_lable"></label>
                    <label class="col-sm-2"><bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="owright_lable"></label>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 "><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="olforward_lable"></label>
                    <label class="col-sm-1"><bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="olaft_lable"></label>

                    <label class=" col-sm-2"><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/></label>
                    <span class=""> : </span>
                    <label class=" col-sm-2 labl_preview" id="oh_lable"></label>
                </div>
            </div>
            <table class="table table-striped table-bordered remove_td_title" id="bkgpreviewtable1" >
                <thead>
                    <tr>
                        <th width="20px"><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>"/></th>
                        <th><div class="col-sm-12 text-center"><bean:message key="ebooking.cargo.Equipment" bundle="<%=clientcode%>"/> </div></th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
            <br/><br/>

        </div>

    </div>
    <%--Preview Haulage details --%>
    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
    <div class="card card-shadow">
        <div class="card-header preview_header"> <bean:message key="ecom.bkg.haulagedet" bundle="<%=clientcode%>"/>  <a href="javascript:void(0);" onclick="$('#<%="A".equalsIgnoreCase(bkgpreviewtype) ? "AmendmentBookingFormBean" : "EbookingValidatorFormBeanNew"%>-t-3').click();" class="float-right text-white"> <i class="fa fa-pencil"></i>  </a> </div>
        <div class="card-body">
            <div class="col-sm-12">
                <div class="table-responsive" id="previewdatatablebyhaulagediv">
                    <table class="table table-striped table-bordered" id="previewdatatablebyhaulage"></table>
                </div>
            </div>
        </div>
    </div>
    <%}%>
    <%if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
    <div class="card card-shadow">
        <div class="card-header preview_header"> <bean:message key="booking.cargo.additionalproductandcharges" bundle="<%=clientcode%>" />  <a href="javascript:void(0);" onclick="$('#<%="A".equalsIgnoreCase(bkgpreviewtype) ? "AmendmentBookingFormBean" : "EbookingValidatorFormBeanNew"%>-t-3').click();" class="float-right text-white"> <i class="fa fa-pencil"></i>  </a> </div>
        <div class="card-body">
            <div class="col-sm-12">
                <div class="table-responsive" id="cargoprotect_table_div_preview">

                </div>
            </div>
        </div>
    </div>
    <%}%>
    <%if ("Y".equalsIgnoreCase(GENS)) {%>
            <div class="card card-shadow" id="ENSPreviewDiv" style="display:none">
                <div class="card-header preview_header"> <bean:message key="shipping.ens.heading" bundle="<%=clientcode%>" /> <a href="javascript:void(0);" onclick="$('#<%="A".equalsIgnoreCase(bkgpreviewtype) ? "AmendmentBookingFormBean" : "EbookingValidatorFormBeanNew"%>-t-4').click();" class="float-right text-white"> <i class="fa fa-pencil"></i>  </a> </div>
                <div class="card-body">
                    <div class="col-sm-12">
                        <div id="ens_tab_div_preview">
                            <div id="ensmasterfields">
                                <div class="row form-group">
        
                                    <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.eu.customs.zone" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>
                                    
                                        <label class="col-sm-4 labl_preview" id="ens_eu_cust_zone_lable" >
                                         
                                        </label>
                                    
                                    <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.of.bl" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>
        
                                    <label class="col-sm-4 labl_preview" id="ens_bl_type_lable" ></label>
        
                                </div>
                                <div class="row form-group">
        
                                    <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.ens.filling" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>
        
                                    <label class="col-sm-4 labl_preview" id="ens_filling_type_lable" ></label>                                   
        
                                    <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.method.of.payment" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>
                                    <label class="col-sm-4 labl_preview" id="ens_payment_method_lable" ></label>
        
                                </div>
                            </div>
                                <div id="buyerseller_preview" style="display: none">    
                           
                        
                            <div id='by_hd'>
                               
                                    <div class="header_tle"> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>  </div>

                                <div class="row form-group">
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-5 labl_preview" id="Buyername_label"> </span>
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-4 labl_preview " id="Buyeradress_label"></span>
                
                
                
                                </div>
                
                                <div class="row form-group">
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-5 labl_preview" id="Buyeradress1_label"></span>
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-4 labl_preview " id="Buyercity_label"></span>
                
                
                
                                </div>
                
                
                                <div class="row form-group">
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Buyercountry_label"></span>
                
                                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Buyerphone_label"></span>
                                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                        <span class="col-sm-2 labl_preview labe_width" id="Buyerfax_label"></
                                    <%}%>
                                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview " id="Buyeremail_label"></span>
                                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                                    <span class="col-sm-1 labl_preview " id="Buyerzipcode_label"></span>
                                    <%}%>   
                                </div>
                                <div class="row form-group">
                
                                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Buyerstate_label" ></span>
                                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Buyereori_label" ></span>
                
                                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Buyerpersontype_label" ></span>
                                    <%}%>    
                                </div>
                            </div> 
                                 <div id='sl_hd'>
                
                                  
                                    <div class="header_tle "> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>  </div>
                
                              
                
                                <div class="row form-group">
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-5 labl_preview" id="Sellername_label"> </span>
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-4 labl_preview " id="Selleradress_label"></span>
                
                
                
                                </div>
                
                                <div class="row form-group">
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-5 labl_preview" id="Selleradress1_label"></span>
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-4 labl_preview " id="Sellercity_label"></span>
                
                
                
                                </div>
                
                
                                <div class="row form-group">
                
                                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Sellercountry_label"></span>
                
                                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Sellerphone_label"></span>
                                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview labe_width" id="Sellerfax_label"></span>
                                    <%}%>
                                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview " id="Selleremail_label"></span>
                                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                                    <span class=""> : </span>
                                    <span class="col-sm-1 labl_preview " id="Sellerzipcode_label"></span>
                                    <%}%>
                                </div>
                                <div class="row form-group">
                
                                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Sellerstate_label" ></span>
                                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
        
                                    <span class="col-sm-2 labl_preview" id="Sellereori_label" ></span>
                
                                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                                    <span class=""> : </span>
                
                                    <span class="col-sm-2 labl_preview" id="Sellerpersontype_label" ></span>
                                    <%}%>    
                                </div>
                              </div> 
                              </div>
                                <div id="supplementarydeclarant_preview" >
                            <div class="header_tle">  <bean:message key="shipping.ens.supplementary.declarant" bundle="<%=clientcode%>"/>  </div>
        
                            <div class="row form-group">
                              
                                    <label class="col-sm-1"><bean:message key='shipping.ens.declarant.name' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantName_label" style="border: none;"></label>
                                
                                
                                    <label class="col-sm-2" ><bean:message key='shipping.ens.declarant.address' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantAddress_label" style="border: none;"></label>
                                
                                
                                    <label class="col-sm-2"><bean:message key='shipping.ens.declarant.address1' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantAddress1_label" style="border: none;"></label>
                                </div>
                                <div class="row form-group">
                                
                                    <label class="col-sm-1" ><bean:message key='shipping.ens.declarant.city' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantCity_label" style="border: none;"></label>
                                   
                                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                    <label class="col-sm-2"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantZipcode_label" style="border: none;"></label>
                                    <%}%>


                                    <label class="col-sm-2"><bean:message key='shipping.ens.declarant.country' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantCountry_label" style="border: none;"></label>
                              
                            
                                 </div>
                                 <div class="row form-group">
                                    
                                    <label class="col-sm-1"><bean:message key='shipping.ens.declarant.state' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantState_label" style="border: none;"></label>
                             
                                    <label class="col-sm-2"><bean:message key='shipping.ens.declarant.telephone' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantTelephone_label" style="border: none;"></label>
                              
                                    <label class="col-sm-2"><bean:message key='shipping.ens.declarant.eori' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantEORI_label" style="border: none;"></label>
                              
                                 </div>
                                 <div class="row form-group">

                                    <label class="col-sm-1"><bean:message key='shipping.ens.declarant.email' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>       
                                    <label class=" col-sm-2 labl_preview" id="declarantEmail_label" style="border: none;"></label>
                               
                                    <label class="col-sm-2"><bean:message key='shipping.ens.declarant.fillingtype' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                                    <label class=" col-sm-2 labl_preview" id="declarantFillingType_label" style="border: none;"></label>
                               
                            </div>
                        </div>
                    </div>
                </div>
            </div>  </div>
         <%}%>
    <div class="card  card-shadow">
        <div class="card-header preview_header">  <bean:message key="ecom.bkg.addrem" bundle="<%=clientcode%>"/> </div>
        <div class="card-body">
              <%if ("Y".equalsIgnoreCase(Enable_SISubmit_Party)) {%> 
                               <div class="form-group row">
                                   <label class="col-sm-2" ><bean:message key='ebooking.sisubmit.party' bundle="<%=clientcode%>"/></label>
                                   <div class="col-sm-4">
                                    <select id="sisubmit_party" class="form-control light_border"  onchange="setSiSubmitParty(this.value)">
                                    </select>
                                   </div>
                                </div>
                <%}%>
            <div class="form-group row">
                <label class="col-sm-2"><bean:message key='ebooking.sales.rate' bundle="<%=clientcode%>"/></label>
                <div class="col-sm-4">
                    <html:text styleId="salesRateAgree" property="salesRateAgree"  styleClass="form-control light_border"  maxlength="200">
                    </html:text>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2"><bean:message key='ebooking.additional.info' bundle="<%=clientcode%>"/></label>
                <div class="col-sm-10">
                    <html:textarea styleId="ebook_status_remarks" property="ebook_status_remarks"  styleClass="form-control light_border" rows="3" >
                    </html:textarea>
                </div>
            </div>

			<%if ("Y".equalsIgnoreCase(Enable_SISubmit_Party)) {%> 
			<div class="form-group row">
				<div class="row siparty_disclaimer1"><bean:message key='ebooking.sisubmit.party.disclaimerhead' bundle="<%=clientcode%>"/></div>
					<div class="row siparty_disclaimer2"><bean:message key='ebooking.sisubmit.party.disclaimer' bundle="<%=clientcode%>"/></div>
			</div>
			<%}%>

            <div class="text-right">
                <%
                    if ("N".equalsIgnoreCase(bkgpreviewtype)) {
                        if (bookingType != null && !"edit".equalsIgnoreCase(bookingType) && !(manageTemplage != null && manageTemplage.equalsIgnoreCase("Y"))) {
                %>
                <button type="button" class="btn btn-primary btn-sm" onClick="saveTempBooking()"  id="searchbtn" data-toggle="modal" data-target="#myModal"><bean:message key="ecom.but.saveastemp" bundle="<%=clientcode%>"/> </button>
                <%                                                } else if (manageTemplage != null && manageTemplage.equalsIgnoreCase("Y")) {

                %>
                <button type="button" class="btn btn-primary btn-sm" onClick="updateTemplate()"  id="updatetemplate" ><bean:message key="ecom.updatetemp" bundle="<%=clientcode%>"/></button>
                <%}%>
                <% if (!isKycPending) {%>
                <%if (user != null && user.equalsIgnoreCase("Y")) {%>                    
                <button type="button" class="btn btn-primary btn-sm" title="Access denied" disabled><bean:message key="ecom.but.saveasdraft" bundle="<%=clientcode%>"/> </button>&nbsp;&nbsp;
                <%} else if (manageTemplage == null || manageTemplage.length() == 0 || manageTemplage.equalsIgnoreCase("N")) {%>
                <button type="button" class="btn btn-primary btn-sm" onClick="saveDraftBooking()"><bean:message key="ecom.but.saveasdraft" bundle="<%=clientcode%>"/> </button>&nbsp;&nbsp;<%}%>
                <%}%>
                <button type="button" class="btn btn-primary btn-sm" onclick="backtobookview()"><bean:message key="ecom.back" bundle="<%=clientcode%>"/>  </button>
                <%} else if ("A".equalsIgnoreCase(bkgpreviewtype) && "reuse".equalsIgnoreCase(bookingType)) {%>
                <button type="button" class="btn btn-primary btn-sm" onClick="saveTempBookingAmendment()"  id="searchbtn" data-toggle="modal" data-target="#myModal"><bean:message key="shipping.template.button" bundle="<%=clientcode%>"/> </button>
                <%if (!isKycPending) {%>
                <button type="button" class="btn btn-primary btn-sm" onClick="saveDraftBookingAmendment()"><bean:message key="shipping.save.button" bundle="<%=clientcode%>"/> </button>&nbsp;&nbsp;
                <%}%>
                <%}%>
            </div>
            <!--<%if ("GWF".equalsIgnoreCase(carrierCode) && (!"templateedit".equalsIgnoreCase(bookingType))) {%>
            <br>
            <div class="text-left">
                <div class="skin1 skin-flat1 input-group m-l-20">
                    <input type='checkbox' id="termsagree" name="termsagree" class="m-t-5 required">
                    <label for="termsagree">
                        <a href="<%=pdfpath%>Booking_Terms.pdf" class="text-info" target="_blank" id="register"><bean:message key="Reg.Agree" bundle="<%=clientcode%>" /></a><span class="mandatory">*</span>
                    </label>
                </div>
            </div>
            <%}%>-->
        </div>
    </div>
</fieldset>
<%} catch (Exception e) {
        e.printStackTrace();
    }%>
