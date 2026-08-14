<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.google.gson.Gson"%>
<%@page contentType="text/html" language="java" import="net.ecom.common.GeneralConf,net.ecom.common.Validation,net.ecom.vo.EbookingValidatorFormBeanNew,net.ecom.vo.Route,java.util.ArrayList,java.util.Hashtable,java.util.*,net.ecom.vo.BookingHaulagedetailsVo,net.ecom.vo.CutoffConfigVO,net.ecom.common.utils.DateConversion_New,net.ecom.common.NumberFormat,net.ecom.common.MaskPort,net.ecom.common.utils.MaskPortUtil,net.ecom.vo.ENSDeclarantVo" errorPage="" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<style>
    /*    .form-control[readonly]{
            background-color: #fff;
        }*/
</style>
<%
    long jsdate = new Date().getTime();
    String backtodashboard = request.getParameter("backtodashboard");
    System.out.println("[ECOM-WAR] viewEbookingNew  BKG backtodashboard ==> " + backtodashboard);
    String isinsurance = null, quoteno = "", quotenostr = null;
    try {
        String ua = request.getHeader("User-Agent");
        boolean isFirefox = (ua != null && ua.indexOf("Firefox/") != -1);
        boolean isMSIE = (ua != null && ua.indexOf("MSIE") != -1);
        response.setHeader("Vary", "User-Agent");
        String path = "";
        String type = "";
        String LRPStatus = "", trpservice = "", spterm = "";
        String Remarks = "", salesRateAgree = "";
        path = (String) session.getAttribute("selCSS");
        type = (String) session.getAttribute("selCSS");
        path = "css/" + path + ".css";
        ArrayList bookinglist = (ArrayList) request.getAttribute("bookinglist");
        ENSDeclarantVo icslist = (ENSDeclarantVo) request.getAttribute("EnstDeclarantView");
        ArrayList cargolist = (ArrayList) request.getAttribute("cargolist");
        session.setAttribute("CarogoDetails_New", cargolist);
        String enableActCommoName = (String) getServletContext().getAttribute(GeneralConf.EnableActualCommoName);
        List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
        List getAddInfo = (List) getServletContext().getAttribute(GeneralConf.AdditionalInfoBkg);
        String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);

        List addInfoList = new ArrayList();
        if (getAddInfo != null && getAddInfo.size() > 0) {
            addInfoList.addAll(getAddInfo);
        }
        String addInfoID = null;
        String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
        HashMap transportmap = (HashMap) session.getAttribute("TRANSPORTSERVICEMAP");
        Hashtable shipmentmap = (Hashtable) getServletContext().getAttribute("SESSION_SHIPTERMS");
        String oncarriage = "Carrier", precarriage = "Carrier", ensfiling = "",t2lfiling="",ensDocType="";
        String clientcode = (String) session.getAttribute("clientcode");
        String Ventilationlabel = (String) getServletContext().getAttribute("Ventilation");
        Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
        HashMap carriageContacts = (HashMap) session.getAttribute("CARRIERCONTACTSMAP");
        Map<String, String> emptyPickUpMap = (Map<String, String>) getServletContext().getAttribute("EMPTYPICKUPMAP");
        if (emptyPickUpMap != null) {
            System.out.println("[ECOM-WAR] viewEbookingNew emptyPickUpMap>>" + emptyPickUpMap.size());
        }
        if (carriageContacts != null) {
            System.out.println("[ECOM-WAR] viewEbookingNew carriageContacts>>" + carriageContacts.size());
        } else {
            carriageContacts = new HashMap();
        }
        String emptyPlaceWithoutTerminal = (String) getServletContext().getAttribute(GeneralConf.EMPTYPLACEWITHOUTTERMINAL);
        String emptyPlaceWithTerminal = (String) getServletContext().getAttribute(GeneralConf.EMPTYPLACEWITHTERMINAL);
        String hideMinMaxTemp = (String) getServletContext().getAttribute(GeneralConf.Hide_Min_MaxTemp);
        String userFruitCust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
        String fakCommodity = (String) getServletContext().getAttribute(GeneralConf.FAKCOMMODITY);
        String fakComdtyCode = "";
        String fakComdtyName = "";
        System.out.println("[ECOM-WAR] viewEbookingNew fakCommodity>>>>" + fakCommodity);
        fakCommodity = fakCommodity != null ? fakCommodity : "";
        if (fakCommodity != null && fakCommodity.length() > 0) {
            String fakCommodityArr[] = fakCommodity.split("~~");
            if (fakCommodityArr.length == 2) {
                fakComdtyCode = fakCommodityArr[0].trim();
                fakComdtyName = fakCommodityArr[1].trim();
            }
        }
        String isAmendment = null;
        Map<String, String> intransitMap = (Map<String, String>) getServletContext().getAttribute("PODINTRANSITMAPCONTEXT");
        String custTypeConfig = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.APBPCONFIGFRONTEND));
        String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
        List<BookingHaulagedetailsVo> haulagelist = (List<BookingHaulagedetailsVo>) request.getAttribute("EBOOKINGHAULAGEDETAILS");
        List lrpconf = (List) getServletContext().getAttribute("LRP_PLUGIN_DETAILS");
        boolean isbkgcutoff = false;
        if (lrpconf != null && lrpconf.contains("BEC")) {
            isbkgcutoff = true;
        }
        String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
        String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
        String HIDE_MASKPORT_CONTAINER = (String) getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER);
        String Enable_Drains_Humidity = (String) getServletContext().getAttribute(GeneralConf.Enable_Drains_Humidity);
        String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
        String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
        String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
        String TENS = (String) getServletContext().getAttribute(GeneralConf.TENS);
        String preferAgency =(String) getServletContext().getAttribute(GeneralConf.Enable_PreferredAgency);
       String preferredAgencyJson = (String) getServletContext().getAttribute("preferredAgencyJson");
     
       String preferredAgencyNameJson = (String) getServletContext().getAttribute("preferredAgencyNameJson");
        String Enable_T2LFiling = (String) getServletContext().getAttribute(GeneralConf.Enable_T2LFiling);
        String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
        String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
        String Hide_AP_CUST_BKG_Master = (String) getServletContext().getAttribute(GeneralConf.Hide_AP_CUST_BKG_Master);
        
        System.out.println("[ECOM-WAR] viewEbookingNew preferAgency=>"+preferAgency);
        boolean is_ecom_OOG = false;
        if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
            is_ecom_OOG = true;
        }
        String cargoprotectid = null;
         HashMap<String, List<EbookingValidatorFormBeanNew>> custMap = (HashMap<String, List<EbookingValidatorFormBeanNew>> ) request.getAttribute("custMap");
          System.out.println("[ECOM-WAR] viewEbookingNew custMap=>"+custMap.size()+"///"+custMap);   
  Hashtable contrylist = (Hashtable) getServletContext().getAttribute("SESSION_CONTRYLIST");
  String DPWShipperTypeList = (String) getServletContext().getAttribute(GeneralConf.DPWShipperTypeList);
  String[] DPWShipperTypeListArr = null;
  HashMap<String, String> DPWShipperTypeListMap = new HashMap<>();
  if (DPWShipperTypeList != null) {
    DPWShipperTypeList = DPWShipperTypeList.replaceAll("[–—−]", "-");

    DPWShipperTypeListArr = DPWShipperTypeList.split("~~");
    for (String shippertype : DPWShipperTypeListArr) {
        String[] parts = shippertype.split("-");
        if (parts.length > 0) {
            DPWShipperTypeListMap.put(parts[0].trim(), shippertype.trim());
        }
    }
}
%>

<%!
    private String checkNull(String value) {
        return (value != null && (!value.equalsIgnoreCase(""))) ? value.trim() : "";
    }
%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ebooking.head.bkgview" bundle="<%=clientcode%>"/> <img class="back_btn" onclick="backtobookview()" src="Design/assets/images/page_icons/back_btn.svg" title="Back" /></div>

<div class="card card-shadow page_bottom_content" id='preview_master_details'>

    <div class="card-header preview_header">   <bean:message key="ebooking.masterdetails.display" bundle="<%=clientcode%>"/> </div>
    <div class="card-body">

        <%
            if (bookinglist != null) {
                int len = bookinglist.size();
                String haulgeType;
                 String preferredAgency="";
                for (int i = 0; i < len; i++) {
                    EbookingValidatorFormBeanNew masterdata = (EbookingValidatorFormBeanNew) bookinglist.get(i);
                    LRPStatus = "";
                    System.out.println("[ECOM-WAR] viewEbookingNew masterdata.getEmptyPickup()>>" + masterdata.getEmptyPickup());
                    isAmendment = masterdata.getAmendment();
                    oncarriage = masterdata.getOnCarriage();
                    precarriage = masterdata.getPreCarriage();
                    ensfiling = masterdata.getEnsfiling();
                    ensDocType = masterdata.getEbook_EnsDocType();
                    t2lfiling = masterdata.getEbook_t2lfiling();
                    salesRateAgree = checkNull(masterdata.getSalesRateAgree());
                    Remarks = masterdata.getEbook_status_remarks();
                    cargoprotectid = masterdata.getCargoprotectid();
                    if (precarriage.equalsIgnoreCase("M")) {
                        precarriage = "Merchant";
                    } else if (precarriage.equalsIgnoreCase("C")) {
                        precarriage = "Carrier";
                    } else {
                        precarriage = "NA";
                    }
                    if (oncarriage.equalsIgnoreCase("M")) {
                        oncarriage = "Merchant";
                    } else if (oncarriage.equalsIgnoreCase("C")) {
                        oncarriage = "Carrier";
                    } else {
                        oncarriage = "NA";
                    }
                    System.out.println("[ECOM-WAR] viewEbookingNew ensfiling=>"+ensfiling);
                    if (ensfiling.equalsIgnoreCase("S")) {
                        ensfiling = "Single";
                    } else if (ensfiling.equalsIgnoreCase("P")) {
                        ensfiling = "Partial";
                    } else {
                        ensfiling = "NA";
                    }
                    if (masterdata.getLrpstatus() == null) {
                    } else {
                        LRPStatus = masterdata.getLrpstatus();
                    }
                    if ("C".equalsIgnoreCase(LRPStatus)) {
                        LRPStatus = "Confirmed";
                    } else if ("E".equalsIgnoreCase(LRPStatus)) {
                        LRPStatus = "Draft";
                    } else if ("D".equalsIgnoreCase(LRPStatus)) {
                        LRPStatus = "Rejected";
                    } else if ("P".equalsIgnoreCase(LRPStatus)) {
                        LRPStatus = "Accepted";
                    } else if ("R".equalsIgnoreCase(LRPStatus)) {
                        LRPStatus = "SentToAgency";
                    } else if ("V".equalsIgnoreCase(LRPStatus)) {
                        LRPStatus = "Cancelled";
                    }
                    if (transportmap != null) {
                        trpservice = checkNull(masterdata.getTrpservice()) + " - " + checkNull((String) transportmap.get(checkNull(masterdata.getTrpservice())));
                    }
                    if (shipmentmap != null) {
                        spterm = checkNull(masterdata.getEbook_shipterm_desc());
                    }
                    haulgeType = checkNull(masterdata.getEbook_haulagetype());
                    if ("L".equalsIgnoreCase(haulgeType)) {
                        haulgeType = "Live Load";
                    } else if ("D".equalsIgnoreCase(haulgeType)) {
                        haulgeType = "Drop Only";
                    } else if ("P".equalsIgnoreCase(haulgeType)) {
                        haulgeType = "Pickup only";
                    } else if ("B".equalsIgnoreCase(haulgeType)) {
                        haulgeType = "Pickup & Drop";
                    }
                    if("Y".equalsIgnoreCase(t2lfiling)){
                        t2lfiling="Yes";
                    }else{
                     t2lfiling="No";
                    }
              HashMap<String, ArrayList<String>> pfagencyMap =null;
               HashMap<String,String> pfagencynameMap =null;
              Gson gson = new Gson();
              if (preferredAgencyJson.length() > 0 && !"{}".equalsIgnoreCase(preferredAgencyJson)) {
              Type pfAgencyType = new TypeToken<HashMap<String, ArrayList<String>>>() {}.getType();
              pfagencyMap = gson.fromJson(preferredAgencyJson, pfAgencyType);

             Type pfAgencyNameType = new TypeToken<HashMap<String, String>>() {}.getType();
             pfagencynameMap = gson.fromJson(preferredAgencyNameJson, pfAgencyNameType);
             
              String pol=checkNull(masterdata.getEbook_origin_country());
                    if(pol.length()>0&& pol.contains("-")){
                        pol=pol.split("-")[0];
                    }
              preferredAgency=pfagencyMap.containsKey(checkNull(pol))?checkNull(pfagencynameMap.get(masterdata.getEbook_preferred_agency())):"";
              System.out.println("[ECOM-WAR] viewEbookingNew preferredAgency=>"+preferredAgency);
              }
             
               
             if("C".equalsIgnoreCase(ensDocType)){
              ensDocType="Consolidated";
             }else{
             ensDocType="Straight B/L";
             }  
        %>
        <div class="row form-group">
            <label class="col-sm-2 "> <bean:message key="ebooking.ref.no" bundle="<%=clientcode%>" />  </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="booking_ref_label"><%=checkNull(masterdata.getEcombooking_refno())%></span>

            <label class="col-sm-2 "> <bean:message key="ebooking.lrpref.no" bundle="<%=clientcode%>"/> </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="booking_ref_label"><%=checkNull(masterdata.getBooking_refno())%></span>

            <%if (!"Y".equalsIgnoreCase(Hide_AP_CUST_BKG_Master)) {%>
            <label class="col-sm-2 "><bean:message key="ebooking.booking.party" bundle="<%=clientcode%>"/>  </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview labe_width" id="booking_party_label"><%=checkNull(masterdata.getEbook_agreename())%></span>
            <%}%>


        </div>
        <div class="row form-group">
            <label class="col-sm-2 "> <bean:message key='ebooking.origin.display' bundle="<%=clientcode%>"/>  </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="pickupplace_label"><%=checkNull(masterdata.getEbook_origin_country())%></span>


            <label class="col-sm-2 "> <bean:message key='ebooking.Delivery.display' bundle="<%=clientcode%>"/> </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="deliveryplace_label"><%=checkNull(masterdata.getEbook_delivery_country())%></span>


            <label class="col-sm-2 "> <bean:message key="ebooking.date.display" bundle="<%=clientcode%>" />  </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview labe_width" id="bookdate_label"><%=checkNull(masterdata.getEbook_dateBut())%></span>

        </div>

        <div class="row form-group">

            <label class="col-sm-2 "> <bean:message key="ebooking.Haulage.Origin" bundle="<%=clientcode%>"/> </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="precarriage_label" ><%=precarriage%></span>


            <label class="col-sm-2 "> <bean:message key="ebooking.Haulage.Destination" bundle="<%=clientcode%>"/></label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="oncarriage_label"><%=oncarriage%></span>

            <label class="col-sm-2 "> <bean:message key='ebooking.carrier.contact' bundle="<%=clientcode%>"/>  </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview labe_width" id="carriercontact_label"><%=checkNull((String) carriageContacts.get(checkNull(masterdata.getCarrierContact())))%></span>

        </div>
        
        <div class="row form-group">
       <%if ("Y".equalsIgnoreCase(TENS)) {%>
            <label class="col-sm-2 "> <bean:message key="ebooking.booking.ENSFiling" bundle="<%=clientcode%>" />  </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="ensfiling_label" ><%=ensfiling%></span>
      <%}%>
      <%if ("Y".equalsIgnoreCase(Enable_T2LFiling)) {%>
            <label class="col-sm-2 "><bean:message key="ebooking.booking.T2LFiling" bundle="<%=clientcode%>"/></label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview" id="t2lfiling_label" ><%=t2lfiling%></span>
     <%}%>
     <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
        <label class="col-sm-2 "> <bean:message key="ebooking.booking.shipmentType" bundle="<%=clientcode%>" />  </label>
        <span class=""> : </span>

        <span class="col-sm-2 labl_preview labe_width" id="ensDocType_view" ><%=ensDocType%></span>
  <%}%>
        </div>
      
        <%if ("Y".equalsIgnoreCase(preferAgency)) {%>
        <div class="row form-group">

            <label class="col-sm-2 "> <bean:message key="ebooking.preferred.agency" bundle="<%=clientcode%>" />  </label>
            <span class=""> : </span>

            <span class="col-sm-2 labl_preview labe_width" id="ebook_preferred_agency_label" ><%=preferredAgency %></span>

        </div>
        <%}%>

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

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="rateref_label"> <%=checkNull(masterdata.getEbook_rate_reference())%> </span>
                <%}%>
                <%   if ("AGR".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key='ebooking.agency.reference' bundle="<%=clientcode%>"/></label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="agencyref_label"><%=checkNull(masterdata.getEbook_agency_reference())%></span>
                <%}%>
                <%   if ("EOF".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key='ebooking.ocean.freight' bundle="<%=clientcode%>"/> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="oceanfreight_label"> <%="C".equalsIgnoreCase(masterdata.getEbook_ocean_freight()) ? "Collect" : "Prepaid"%></span>
                <%}
                    if ("PFR".equalsIgnoreCase(addInfoID)) {%>
                <%if ("ESL".equalsIgnoreCase(carrierCode)) {%>
                <label class="col-sm-2 "><bean:message key="ebooking.place.final.receipt.ch" bundle="<%=clientcode%>" /></label>
                <%} else {%>
                <label class="col-sm-2 "><bean:message key="ebooking.place.final.receipt" bundle="<%=clientcode%>" /></label>
                <%}%>
                <span class=""> : </span>
                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="instransit_label" ><%=checkNull((String) intransitMap.get(checkNull(masterdata.getPodintransit())))%> </span>
                <%}
                    if ("NAT".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.booking.natcode" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="natcode_label">
                    <%=checkNull(masterdata.getEbook_natcode())%>
                </span>
                <%}
                    if ("ETH".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.haulage.type" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="haulagetype_label"><%=checkNull(haulgeType)%></span>
                <%}
                    if ("ELD".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.pickup.date" bundle="<%=clientcode%>" /></label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="pickupdate_label"><%=checkNull(masterdata.getEbook_pickup_date())%></span>
                <%}
                    if ("EDD".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.drop.date" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="dropdate_label"><%=checkNull(masterdata.getEbook_drop_date())%></span>
                <%}
                    if ("HAU".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.hauler.code" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="haulercode_label"><%=checkNull(masterdata.getEbook_haulercode())%> </span>
                <%}
                    if ("EPO".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.customer.PO" bundle="<%=clientcode%>" /></label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"  id="custpo_label" ><%=checkNull(masterdata.getEbook_custpo())%></span>
                <%}
                    if ("ERT".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.ref.type" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="reftype_label"> <%=checkNull(masterdata.getEbook_reftype())%></span>
                <%}
                    if ("ERF".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key='ebooking.exp.ref' bundle="<%=clientcode%>"/></label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" id="expref_label"><%=checkNull(masterdata.getEbook_exportref())%></span>
                <%}
                    if ("EPP".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.empty.pickup" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>" ><%=checkNull(emptyPickUpMap.get(checkNull(masterdata.getEmptyPickup())))%></span>

                <%}
                    if ("EPD".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="book.empty.pickupdate" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"><%=checkNull(masterdata.getEmptyPickUpDate())%></span>
                <%}
                    if ("CRN".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.customer.reference" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"><%=checkNull(masterdata.getCustomerreference())%></span>
                <%}
                    if ("ACD".equalsIgnoreCase(addInfoID)) {%>
                <label class="col-sm-2 "> <bean:message key="ebooking.book.ACID" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"><%=checkNull(masterdata.getACID())%></span>
                <%}if ("DPT".equalsIgnoreCase(addInfoID)) {%>
                    <label class="col-sm-2 "> <bean:message key="ebooking.book.DPWshipperType" bundle="<%=clientcode%>" /> </label>     
                    <span class=""> : </span>
                    <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"><%=checkNull(DPWShipperTypeListMap.get(masterdata.getShipperReferenceType()))%></span>
                   <%System.out.println("[ECOM-WAR] viewEbookingNew DPWShipperTypeListMap.get(masterdata.getShipperReferenceType())=>"+DPWShipperTypeListMap.get(masterdata.getShipperReferenceType())+"///"+masterdata.getShipperReferenceType()+"///"+DPWShipperTypeListMap);%>
                    <%}if ("DPW".equalsIgnoreCase(addInfoID)) {%>
               <label class="col-sm-2 "> <bean:message key="ebooking.book.DPWshipperCode" bundle="<%=clientcode%>" /> </label>
                <span class=""> : </span>

                <span class="col-sm-2 labl_preview <%=(k + 1) % 3 == 0 ? "labe_width" : ""%>"><%=checkNull(!"ullnull".equalsIgnoreCase(masterdata.getShipperReference())
                    ?masterdata.getShipperReference():"")%></span>
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

            <div class="heading_trainee_booking "><bean:message key="Booking.head.vesselschddet" bundle="<%=clientcode%>"/> </div>

        </div>
        <%
            ArrayList routeDetails = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");
            int transittime = 0, transpcount = 0;
            boolean isInlandMve = false;
            String vesselfeeder = null, polName = null, podName = null, voyage = null, etd = null, eta = null, bound = null, str_type = "N", strServiceCode = null, pol = null, pod = null;
            String maskport_trans1orpod = "N";

            boolean ismaskporthdn = false;
            if (routeDetails != null) {
                for (int r = 0; r < routeDetails.size(); r++) {
                    Route list = (Route) routeDetails.get(r);
                    if ("N".equalsIgnoreCase(maskport_trans1orpod) && "Y".equalsIgnoreCase(str_type)) {
                        maskport_trans1orpod = list.getLoadport();
                    }
                    if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(list.getLegType()) || "F".equalsIgnoreCase(list.getLegType()))) {
                        polName = list.getPolPortName();
                        voyage = list.getVoyage();
                        bound = list.getBound();
                        etd = list.getETD1();
                        pol = list.getLoadport();
                        str_type = "Y";
                        if ("Feeder".equalsIgnoreCase(list.getLegtypename()) && (list.getVesselName() == null || list.getVesselName().toLowerCase().contains("dummy"))) {
                            vesselfeeder = "Feeder";
                        } else {
                            vesselfeeder = list.getVesselName();
                        }
                    }
                    strServiceCode = list.getService();
                    if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                        podName = list.getPodPortName();
                        eta = list.getETA1();
                        pod = list.getDisport();
                    }
                    if (!(strServiceCode != null && strServiceCode.trim().length() > 0)) {
                        isInlandMve = true;
                    }
                    if (i == 0 || i == routeDetails.size() - 1) {
                        if (strServiceCode != null && strServiceCode.trim().length() > 0) {
                            transpcount++;
                        }
                    } else {
                        transpcount++;
                    }
                }
                transittime = DateConversion_New.daysDifferentBetweenTwoDays(etd, eta);
                if ("N".equalsIgnoreCase(maskport_trans1orpod)) {
                    maskport_trans1orpod = pod;
                }
              
            
                ismaskporthdn = MaskPortUtil.checkIsMaskPort(pol, maskport_trans1orpod);
        %>
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
                                    <dd><strong><%=etd%></strong></dd>
                                    <dd><%=ismaskporthdn ? "" : MaskPortUtil.getDisplayPortName(pol, polName)%></dd>
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
                                    <dd><strong><%=eta%></strong></dd>
                                    <dd><%=ismaskporthdn ? "" : MaskPortUtil.getDisplayPortName(pod, podName)%></dd>
                                    </dt>
                                    </td>
                                    <td><img class="content_icon" src="Design/assets/images/page_icons/Lvessel.svg" /></td>
                                    </tr>
                                </table>
                            </div>
                            <div class="panel_inner_top_3 bkg_vessel_info">
                                <table>
                                    <tr>
                                        <td><img class="header_icon" src="Design/assets/images/page_icons/vessel.svg" /></td>
                                        <td>
                                    <dt>
                                    <dd><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></dd>
                                    
                                    <dd><strong><%=ismaskporthdn ? "" : vesselfeeder%></strong></dd>
                                    <dd><%=ismaskporthdn ? "" : voyage%><%=ismaskporthdn ? "" : bound%></dd>
                                    
                                   
                                    </dt>
                                    </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="panel_inner_top_4">
                                <table>
                                    <tr>
                                        <td><img class="header_icon" src="Design/assets/images/page_icons/clock.svg" /></td>
                                        <td>
                                    <dt>
                                    <dd><bean:message key="ecom.schdl.ttime" bundle="<%=clientcode%>"/></dd>
                                    <dd><strong><%=transittime%> days</strong></dd>
                                    </dt>
                                    </td>
                                    </tr>
                                </table>

                            </div>
                            <%if (transpcount > 1) {%>
                            <div id="show_details" style="cursor: pointer" class="show_details_class_book" onclick="toggleText('<%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>)', this, '#bkgshowDetails');" ><%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>) <i class="fa fa-chevron-down"></i></div>       
                                <%} else {%>
                            <div id="show_details" class="show_details_class_book" <%if (isInlandMve) {%> style="cursor: pointer" onclick="toggleText('Multimodal Shipment ', this, '#bkgshowDetails')" <%}%>><%= isInlandMve ? "Multimodal Shipment<i class='fa fa-chevron-down'></i>" : "Direct Shipment"%></div>
                            <%}%>

                        </div> 
                        <div id="bkgshowDetails" class="collapse show_details">
                            <div class="timeline">
                                <%

                                    String legType = "";
                                    for (int j = 0; j < routeDetails.size(); j++) {
                                        Route dto = (Route) routeDetails.get(j);
                                        vesselfeeder = dto.getVesselName();
                                        if ((vesselfeeder == null || vesselfeeder.toLowerCase().contains("dummy")) && dto.getLegtypename() != null && "Feeder".equalsIgnoreCase(dto.getLegtypename())) {
                                            vesselfeeder = "Feeder";
                                        } else {
                                            vesselfeeder = dto.getVesselName();
                                        }
                                        if (dto.getLegtypename() != null && ("Feeder".equalsIgnoreCase(dto.getLegtypename()) || "Mainline".equalsIgnoreCase(dto.getLegtypename()))) {
                                            legType = "vessel";
                                        } else {
                                            legType = dto.getLegtypename();
                                        }
                                        System.out.println("[ECOM-WAR] viewEbookingNew legType--> " + legType);
                                    
                                        ismaskporthdn = MaskPortUtil.checkIsMaskPort(dto.getLoadport(), dto.getDisport());
                                %>
                                <div class="show_details_departure show_details_line">
                                    <%if (j == 0) {%>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <span class="port_name"><%=MaskPortUtil.getDisplayPortName(dto.getLoadport(), dto.getPolPortName())%></span>
                                        </div>
                                    </div>
                                    <%}%>
                                    <div class="row">
                                        <div class="col-sm-5">
                                            <dt>
                                            <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd>
                                            <dd><strong><%=dto.getETD1()%></strong></dd>
                                            </dt>
                                        </div>
                                        <%if (legType != null && legType.trim().length() > 0) {%>
                                        <div class="col-sm-7">
                                            <table>
                                                <tr>
                                                    <td>
                                                <dt>
                                                    <img class="header_icon" src="Design/assets/images/page_icons/<%=legType%>.svg" />
                                                <dd><%=legType%></dd>
                                                
                                                <dd><strong><%=ismaskporthdn ? "":vesselfeeder%></strong></dd>
                                                
                                                
                                          
                                                </dt>
                                                </td>
                                                <%if ("Vessel".equalsIgnoreCase(legType)) {
											    if(!ismaskporthdn){%>		
                                                <td>
                                                <dt>
                                                <dd><bean:message key="tracking.Service" bundle="<%=clientcode%>"/></dd>
                                                <dd><strong><%=dto.getServiceName()%></strong></dd>
                                                </dt>
                                                </td>
                                                <td>
                                                <dt>
                                                <dd> <bean:message key="ecom.shippingins.VoyageorBound" bundle="<%=clientcode%>"/></dd>
                                                <dd><strong><%=dto.getVoyage()%><%=dto.getBound()%></strong></dd>
                                                </dt>
                                                </td>
                                                <%}
                                                 }%>
                                                </tr>
                                            </table>
                                        </div>
                                        <%}%>
                                    </div>
                                </div>
                                <%if (j != 0) {%>
                            </div>
                            <%}%>
                            <%if (j != routeDetails.size() - 1) {%>
                            <div class="show_details_status show_details_line">
                                <%}%>
                                <div class="show_details_arrival show_details_line">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <span class="port_name"><%= MaskPortUtil.getDisplayPortName(dto.getDisport(), dto.getPodPortName())%></span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-5">
                                            <dt>
                                            <dd><bean:message key="ecom.schdl.arrv" bundle="<%=clientcode%>"/></dd>
                                            <dd><strong><%=dto.getETA1()%></strong></dd>
                                            </dt>
                                        </div>
                                    </div>
                                </div>

                                <%                    }// j close %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%
            CutoffConfigVO cutOffVO = (CutoffConfigVO) request.getAttribute("ROUTECUTOFFVO");
            if (isbkgcutoff && cutOffVO != null) {
        %>
        <div class="">
            <table class="table table-striped table-bordered">
                <tbody>
                    <tr>
                        <td><bean:message key="ecom.bkg.bkgcutoff" bundle="<%=clientcode%>"/></td>
                        <td><%=Validation.checkNull(cutOffVO.getBkgcutoff())%><input type="hidden" name="ebookbkgcutoff" value="<%=cutOffVO.getBkgcutoff()%>"></td>
                        <td><bean:message key="ecom.bkg.doccutoff" bundle="<%=clientcode%>"/></td>
                        <td><%=Validation.checkNull(cutOffVO.getDoccutoff())%><input type="hidden" name="ebookdoccutoff" value="<%=cutOffVO.getDoccutoff()%>"></td>
                    </tr>
                    <tr>
                        <td><bean:message key="ecom.bkg.vgmcutoff" bundle="<%=clientcode%>"/></td>
                        <td><%=Validation.checkNull(cutOffVO.getVgmcutoff())%><input type="hidden" name="ebookvgmcutoff" value="<%=cutOffVO.getVgmcutoff()%>"></td>
                        <td><bean:message key="ecom.bkg.portentrycutoff" bundle="<%=clientcode%>"/></td>
                        <td><%=Validation.checkNull(cutOffVO.getPortentrycutoff())%><input type="hidden" name="ebookportentrycutoff" value="<%=cutOffVO.getPortentrycutoff()%>"></td>
                    </tr>
                    <tr>
                        <td><bean:message key="ecom.bkg.hazcutoff" bundle="<%=clientcode%>"/></td>
                        <td><%=Validation.checkNull(cutOffVO.getHazcutoff())%><input type="hidden" name="ebookhazcutoff" value="<%=cutOffVO.getHazcutoff()%>"></td>
                            <%if (cutOffVO.getBbrorocutoff() != null && cutOffVO.getBbrorocutoff().trim().length() > 0) {%>
                        <td><bean:message key="ecom.bkg.bbrorocutoff" bundle="<%=clientcode%>"/></td>
                        <td><%=Validation.checkNull(cutOffVO.getBbrorocutoff())%><input type="hidden" name="ebookbrorocutoff" value="<%=cutOffVO.getBbrorocutoff()%>"></td>
                            <%}%>
                    </tr>
                </tbody>
            </table>
        </div>
        <%}
            }%>
    </div>

</div>
</div>
<div class="card card-shadow">
    <div class="card-header preview_header"> <bean:message key='ebooking.custdetails.display' bundle="<%=clientcode%>"/>  </div>
    <div class="card-body">
        <div class="txt_side_borderxx">
            <div id='bp_hd'>
                <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>

                    <div class="heading_trainee_booking "> <bean:message key='ebooking.BookingParty.display' bundle="<%=clientcode%>"/> </div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="bookingname_label"><%=checkNull(masterdata.getEbook_bookingname())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="bookingadress_label"><%=checkNull(masterdata.getEbook_bookingaddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>
                    <span class="col-sm-5 labl_preview" id="bookingadress1_label"><%=checkNull(masterdata.getEbook_bookingaddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview" id="bookingcity_label"><%=checkNull(masterdata.getEbook_bookingcity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="bookingcountry_label" ><%=checkNull(masterdata.getEbook_bookingcountry_name())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="bookingphone_label"><%=checkNull(masterdata.getEbook_bookingphone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="bookingfax_label"><%=checkNull(masterdata.getEbook_bookingfax())%> </span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="bookingemail_label"><%=checkNull(masterdata.getEbook_bookingemail())%> </span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="bookingzipcode_label"><%=checkNull(masterdata.getEbook_bookingzipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="bookingstate_label" ><%=checkNull(masterdata.getEbook_bookingstate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>   
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="bookingeori_label" ><%=checkNull(masterdata.getEbook_bookingeori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>   

                    <span class="col-sm-2 labl_preview" id="bookingpersontype_label" ><%=checkNull(masterdata.getBookingtypeperson())%></span>
                    <%}%>
                </div>
                <%}
                %>

            </div>
            <div id='ap_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>

                    <div class="heading_trainee_booking "> <bean:message key='shipping.booking.party.agreementType' bundle="<%=clientcode%>"/> </div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="agreename_label"><%=checkNull(masterdata.getEbook_agreename())%> </span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="agreeadress_label"><%=checkNull(masterdata.getEbook_agreeaddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="agreeadress1_label"><%=checkNull(masterdata.getEbook_agreeaddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="agreecity_label"><%=checkNull(masterdata.getEbook_agreecity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="agreecountry_label"><%=checkNull(masterdata.getEbook_agreecountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="agreephone_label"><%=checkNull(masterdata.getEbook_agreephone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="agreefax_label"><%=checkNull(masterdata.getEbook_agreefax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="agreeemail_label"><%=checkNull(masterdata.getEbook_agreeemail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="agreezipcode_label"><%=checkNull(masterdata.getEbook_agreezipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="agreestate_label" ><%=checkNull(masterdata.getEbook_agreestate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="agreeeori_label" ><%=checkNull(masterdata.getEbook_agreeeori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="agreepersontype_label" ><%=checkNull(masterdata.getAgreetypeperson())%></span>
                    <%}%>   
                </div>
            </div>
            <%if (masterdata.getEbook_shippername() != null && masterdata.getEbook_shippername().trim().length() > 0) {%>
            <div id='sh_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>

                    <div class="heading_trainee_booking "><bean:message key='shipping.booking.party.shipperType' bundle="<%=clientcode%>"/></div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="shippername_label"> <%=checkNull(masterdata.getEbook_shippername())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="shipperadress_label"><%=checkNull(masterdata.getEbook_shipperaddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="shipperadress1_label"><%=checkNull(masterdata.getEbook_shipperaddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="shippercity_label"><%=checkNull(masterdata.getEbook_shippercity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="shippercountry_label"><%=checkNull(masterdata.getEbook_shippercountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="shipperphone_label"><%=checkNull(masterdata.getEbook_shipperphone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="shipperfax_label"><%=checkNull(masterdata.getEbook_shipperfax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="shipperemail_label"><%=checkNull(masterdata.getEbook_shipperemail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="shipperzipcode_label"><%=checkNull(masterdata.getEbook_shipperzipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="shipperstate_label" ><%=checkNull(masterdata.getEbook_shipperstate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="shippereori_label" ><%=checkNull(masterdata.getEbook_shippereori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="shipperpersontype_label" ><%=checkNull(masterdata.getShippertypeperson())%></span>
                    <%}%>
                </div>
            </div>
            <%}%>
            <%if (masterdata.getEbook_consigneename() != null && masterdata.getEbook_consigneename().trim().length() > 0) {%>
            <div id='cn_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>

                    <div class="heading_trainee_booking "><bean:message key='shipping.booking.party.consigneeType' bundle="<%=clientcode%>"/></div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="consigneename_label"> <%=checkNull(masterdata.getEbook_consigneename())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="consigneeadress_label"><%=checkNull(masterdata.getEbook_consigneeaddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="consigneeadress1_label"><%=checkNull(masterdata.getEbook_consigneeaddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="consigneecity_label"><%=checkNull(masterdata.getEbook_consigneecity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="consigneecountry_label"><%=checkNull(masterdata.getEbook_consigneecountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="consigneephone_label"><%=checkNull(masterdata.getEbook_consigneephone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="consigneefax_label"><%=checkNull(masterdata.getEbook_consigneefax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="consigneeemail_label"><%=checkNull(masterdata.getEbook_consigneeemail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="consigneezipcode_label"><%=checkNull(masterdata.getEbook_consigneezipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="consigneestate_label" ><%=checkNull(masterdata.getEbook_consigneestate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="consigneeeori_label" ><%=checkNull(masterdata.getEbook_consigneeeori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="consigneepersontype_label" ><%=checkNull(masterdata.getConsigneetypeperson())%></span>
                    <%}%> 
                </div>
            </div>   
            <%}%>
            <%if (masterdata.getEbook_notifyname() != null && masterdata.getEbook_notifyname().trim().length() > 0) {%>
            <div id='nt_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>

                    <div class="heading_trainee_booking "><bean:message key='Booking.party.notify' bundle="<%=clientcode%>"/></div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="notifyname_label"><%=checkNull(masterdata.getEbook_notifyname())%> </span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="notifyadress_label"><%=checkNull(masterdata.getEbook_notifyaddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="notifyadress1_label"><%=checkNull(masterdata.getEbook_notifyaddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="notifycity_label"><%=checkNull(masterdata.getEbook_notifycity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifycountry_label"><%=checkNull(masterdata.getEbook_notifycountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifyphone_label"><%=checkNull(masterdata.getEbook_notifyphone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="notifyfax_label"><%=checkNull(masterdata.getEbook_notifyfax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="notifyemail_label"><%=checkNull(masterdata.getEbook_notifyemail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="notifyzipcode_label"><%=checkNull(masterdata.getEbook_notifyzipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifystate_label" ><%=checkNull(masterdata.getEbook_notifystate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifyeori_label" ><%=checkNull(masterdata.getEbook_notifyeori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifypersontype_label" ><%=checkNull(masterdata.getNotifytypeperson())%></span>
                    <%}%>   
                </div>
            </div>
            <%}%>

            <%if (masterdata.getEbook_notifyname_n1() != null && masterdata.getEbook_notifyname_n1().trim().length() > 0) {%>
            <div id='nt1_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>
                    <% if (custtypelist != null && custtypelist.contains("N1")) {%>

                    <div class="heading_trainee_booking "><bean:message key='Booking.party.notify1' bundle="<%=clientcode%>"/></div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="notifyname_label_n1"><%=checkNull(masterdata.getEbook_notifyname_n1())%> </span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="notifyadress_label_n1"><%=checkNull(masterdata.getEbook_notifyaddress_n1())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="notifyadress1_label_n1"><%=checkNull(masterdata.getEbook_notifyaddress1_n1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="notifycity_label_n1"><%=checkNull(masterdata.getEbook_notifycity_n1())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifycountry_label_n1"><%=checkNull(masterdata.getEbook_notifycountry_desc_n1())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifyphone_label_n1"><%=checkNull(masterdata.getEbook_notifyphone_n1())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="notifyfax_label_n1"><%=checkNull(masterdata.getEbook_notifyfax_n1())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="notifyemail_label_n1"><%=checkNull(masterdata.getEbook_notifyemail_n1())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="notifyzipcode_label_n1"><%=checkNull(masterdata.getEbook_notifyzipcode_n1())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifystate_label_n1" ><%=checkNull(masterdata.getEbook_notifystate_n1())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>     
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifyeori_label_n1" ><%=checkNull(masterdata.getEbook_notifyeori_n1())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="notifypersontype_label_n1" ><%=checkNull(masterdata.getNotifytypeperson_n1())%></span>
                    <%}%>
                </div>
            </div>
            <%}%>

            <%}%>
            <%if (masterdata.getEbook_forwardername() != null && masterdata.getEbook_forwardername().trim().length() > 0) {%>
            <div id='fw_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>
                    <% if (custtypelist != null && custtypelist.contains("FW")) {%>

                    <div class="heading_trainee_booking "><bean:message key="ecom.bkg.custfw" bundle="<%=clientcode%>"/></div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="forwardername_label"> <%=checkNull(masterdata.getEbook_forwardername())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="forwarderadress_label"><%=checkNull(masterdata.getEbook_forwarderaddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="forwarderadress1_label"><%=checkNull(masterdata.getEbook_forwarderaddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="forwardercity_label"><%=checkNull(masterdata.getEbook_forwardercity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="forwardercountry_label"><%=checkNull(masterdata.getEbook_forwardercountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="forwarderphone_label"><%=checkNull(masterdata.getEbook_forwarderphone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="forwarderfax_label"><%=checkNull(masterdata.getEbook_forwarderfax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="forwarderemail_label"><%=checkNull(masterdata.getEbook_forwarderemail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="forwarderzipcode_label"><%=checkNull(masterdata.getEbook_forwarderzipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="forwarderstate_label" ><%=checkNull(masterdata.getEbook_forwarderstate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="forwardereori_label" ><%=checkNull(masterdata.getEbook_forwardereori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="forwarderpersontype_label" ><%=checkNull(masterdata.getForwardertypeperson())%></span>
                    <%}%>
                </div>
            </div>   
            <%}%>
            <%}%>
            <%if (masterdata.getEbook_Warehousename() != null && masterdata.getEbook_Warehousename().trim().length() > 0 && false) {%>
            <div id='fw_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>
                    <% if (custtypelist != null && custtypelist.contains("WH")) {%>

                    <div class="heading_trainee_booking "> <bean:message key="ecom.bkg.custwh" bundle="<%=clientcode%>"/></div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="Warehousename_label"> <%=checkNull(masterdata.getEbook_Warehousename())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="Warehouseadress_label"><%=checkNull(masterdata.getEbook_Warehouseaddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="Warehouseadress1_label"><%=checkNull(masterdata.getEbook_Warehouseaddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="Warehousecity_label"><%=checkNull(masterdata.getEbook_Warehousecity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Warehousecountry_label"><%=checkNull(masterdata.getEbook_Warehousecountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Warehousephone_label"><%=checkNull(masterdata.getEbook_Warehousephone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="Warehousefax_label"><%=checkNull(masterdata.getEbook_Warehousefax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="Warehouseemail_label"><%=checkNull(masterdata.getEbook_Warehouseemail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="Warehousezipcode_label"><%=checkNull(masterdata.getEbook_Warehousezipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Warehousestate_label" ><%=checkNull(masterdata.getEbook_Warehousestate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Warehouseeori_label" ><%=checkNull(masterdata.getEbook_Warehouseeori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Warehousepersontype_label" ><%=checkNull(masterdata.getWarehousetypeperson())%></span>
                    <%}%>
                </div>   
            </div>   
            <%}%>

            <%}%>
            <%if (masterdata.getEbook_NamedCustomername() != null && masterdata.getEbook_NamedCustomername().trim().length() > 0) {%>
            <div id='nc_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>
                    <% if (custtypelist != null && custtypelist.contains("NC")) {%>
                    <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custnc' bundle="<%=clientcode%>"/>  </div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="NamedCustomername_label"> <%=checkNull(masterdata.getEbook_NamedCustomername())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="NamedCustomeradress_label"><%=checkNull(masterdata.getEbook_NamedCustomeraddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="NamedCustomeradress1_label"><%=checkNull(masterdata.getEbook_NamedCustomeraddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="NamedCustomercity_label"><%=checkNull(masterdata.getEbook_NamedCustomercity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="NamedCustomercountry_label"><%=checkNull(masterdata.getEbook_NamedCustomercountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="NamedCustomerphone_label"><%=checkNull(masterdata.getEbook_NamedCustomerphone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="NamedCustomerfax_label"><%=checkNull(masterdata.getEbook_NamedCustomerfax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="NamedCustomeremail_label"><%=checkNull(masterdata.getEbook_NamedCustomeremail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="NamedCustomerzipcode_label"><%=checkNull(masterdata.getEbook_NamedCustomerzipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="NamedCustomerstate_label" ><%=checkNull(masterdata.getEbook_NamedCustomerstate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="NamedCustomereori_label" ><%=checkNull(masterdata.getEbook_NamedCustomereori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="NamedCustomerpersontype_label" ><%=checkNull(masterdata.getEbook_NamedCustomerpersontype())%></span>
                    <%}%>    
                </div>
            </div> 
            <%
                if (masterdata.getEbook_NamedCustomername() != null && masterdata.getEbook_NamedCustomername().trim().length() > 0) {
            %>
            <script type="text/javascript">
                document.getElementById('direct_booking_checkbox_id').disabled = false;
                document.getElementById('direct_booking_checkbox_id').checked = false;
            </script>
            <%
                }
            %>

            <%}%>
            <%}%>
            <%if ("Y".equalsIgnoreCase(TENS) && "N".equalsIgnoreCase(TENS_Documentation)) {
            if (masterdata.getEbook_Sellername() != null && masterdata.getEbook_Sellername().trim().length() > 0) {%>
            <div id='sl_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>
                    <% if (custtypelist != null && custtypelist.contains("SL")) {%>
                    <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>  </div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="Sellername_label"> <%=checkNull(masterdata.getEbook_Sellername())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="Selleradress_label"><%=checkNull(masterdata.getEbook_Selleraddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="Selleradress1_label"><%=checkNull(masterdata.getEbook_Selleraddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="Sellercity_label"><%=checkNull(masterdata.getEbook_Sellercity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Sellercountry_label"><%=checkNull(masterdata.getEbook_Sellercountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Sellerphone_label"><%=checkNull(masterdata.getEbook_Sellerphone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="Sellerfax_label"><%=checkNull(masterdata.getEbook_Sellerfax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="Selleremail_label"><%=checkNull(masterdata.getEbook_Selleremail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="Sellerzipcode_label"><%=checkNull(masterdata.getEbook_Sellerzipcode())%> </span>
                    <%}%>
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Sellerstate_label" ><%=checkNull(masterdata.getEbook_Sellerstate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Sellereori_label" ><%=checkNull(masterdata.getEbook_Sellereori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Sellerpersontype_label" ><%=checkNull(masterdata.getEbook_Sellerpersontype())%></span>
                    <%}%>    
                </div>
            </div> 
            <%}
            }
            if (masterdata.getEbook_Buyername() != null && masterdata.getEbook_Buyername().trim().length() > 0) {%>
            <div id='by_hd'>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>
                    <% if (custtypelist != null && custtypelist.contains("BY")) {%>
                    <div class="heading_trainee_booking "> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>  </div>

                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="Buyername_label"> <%=checkNull(masterdata.getEbook_Buyername())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="Buyeradress_label"><%=checkNull(masterdata.getEbook_Buyeraddress())%></span>



                </div>

                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-5 labl_preview" id="Buyeradress1_label"><%=checkNull(masterdata.getEbook_Buyeraddress1())%></span>

                    <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-4 labl_preview " id="Buyercity_label"><%=checkNull(masterdata.getEbook_Buyercity())%></span>



                </div>


                <div class="row form-group">

                    <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Buyercountry_label"><%=checkNull(masterdata.getEbook_Buyercountry_desc())%></span>

                    <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Buyerphone_label"><%=checkNull(masterdata.getEbook_Buyerphone())%></span>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview labe_width" id="Buyerfax_label"><%=checkNull(masterdata.getEbook_Buyerfax())%></span>
                    <%}%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview " id="Buyeremail_label"><%=checkNull(masterdata.getEbook_Buyeremail())%></span>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                    <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                    <span class=""> : </span>
                    <span class="col-sm-1 labl_preview " id="Buyerzipcode_label"><%=checkNull(masterdata.getEbook_Buyerzipcode())%> </span>
                    <%}%>   
                </div>
                <div class="row form-group">

                    <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Buyerstate_label" ><%=checkNull(masterdata.getEbook_Buyerstate())%></span>
                    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                    <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Buyereori_label" ><%=checkNull(masterdata.getEbook_Buyereori())%></span>

                    <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                    <span class=""> : </span>

                    <span class="col-sm-2 labl_preview" id="Buyerpersontype_label" ><%=checkNull(masterdata.getEbook_Buyerpersontype())%></span>
                    <%}%>    
                </div>
            </div> 
            <%}
            }
           } if ("Y".equalsIgnoreCase(TENS_Documentation)) {
    if (custMap != null && custMap.containsKey("SL")) { 
    ArrayList<EbookingValidatorFormBeanNew> ebooking_sellertype_grid = (ArrayList<EbookingValidatorFormBeanNew>) custMap.get("SL");
    if (ebooking_sellertype_grid != null) { %>

<div class="row form-group">
    <div class="heading_trainee_bottom_booking "></div>
    <div class="heading_trainee_booking">
        <bean:message key="ecom.bkg.custsl" bundle="<%=clientcode%>" />
    </div>
</div>

<% for (int k = 0; k < ebooking_sellertype_grid.size(); k++) {
    EbookingValidatorFormBeanNew customergrid = ebooking_sellertype_grid.get(k);
    if (custtypelist != null && custtypelist.contains("SL")) { %>

<div id="customerTypePreviewTemplate" class="customer-preview m-b-3" style="<%= (k != 0) ? "border-top: 2px solid #f2b92a;padding-block: 26px;" : "" %>">

    <div class="row form-group">
        <label class="col-sm-1"><bean:message key="ebooking.customername.display" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-5 labl_preview" id="CustType_Sellername_label<%= k %>"><%= customergrid.getEbook_Custname() %></span>

        <label class="col-sm-1"><bean:message key="ebooking.adresscust.display" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-4 labl_preview" id="CustType_Selleradress_label<%= k %>"><%= customergrid.getEbook_Custaddress() %></span>
    </div>

    <div class="row form-group">
        <label class="col-sm-1"><bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-5 labl_preview" id="CustType_Selleradress1_label<%= k %>"><%= customergrid.getEbook_Custaddress1() %></span>

        <label class="col-sm-1"><bean:message key="ebooking.city.display" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-4 labl_preview" id="CustType_Sellercity_label<%= k %>"><%= customergrid.getEbook_Custcity_desc() %></span>
    </div>

    <div class="row form-group">
        <label class="col-sm-1"><bean:message key="ebooking.country.display" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-2 labl_preview" id="CustType_Sellercountry_label<%= k %>"><%=(customergrid.getEbook_Custcountry() + " - " + (checkNull((String) contrylist.get(customergrid.getEbook_Custcountry()))).toUpperCase()) %></span>

        <label class="col-sm-1"><bean:message key="ebooking.telephonecontact.display" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-2 labl_preview" id="CustType_Sellerphone_label<%= k %>"><%= customergrid.getEbook_Custphone() %></span>

         <label class="col-sm-1"><bean:message key="ebooking.email.display" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-2 labl_preview" id="CustType_Selleremail_label<%= k %>"><%= customergrid.getEbook_Custemail() %></span>
   

        </div>

    <div class="row form-group">

        <label class="col-sm-1"><bean:message key="Reg.State" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-2 labl_preview" id="CustType_Sellerstate_label<%= k %>"><%= customergrid.getEbook_Custstate() %></span>
    <%if ("Y".equalsIgnoreCase(GENS)) {%>
        <label class="col-sm-1"><bean:message key="Reg.Eori" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-2 labl_preview" id="CustType_Sellereori_label<%= k %>"><%= checkNull(customergrid.getEbook_Custeori()) %></span>

        <label class="col-sm-1"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>" /></label><span>: </span>
        <span class="col-sm-2 labl_preview" id="CustType_Sellerpersontype_label<%= k %>"><%= checkNull(customergrid.getEbook_Custpersontype()) %></span>
        <%}%> 
    </div>
</div>

<%  } // end if custtypelist
   } // end for loop
  } // end if ebooking_sellertype_grid
} // end if custMap
if (custMap != null && custMap.containsKey("BY")) {

    ArrayList<EbookingValidatorFormBeanNew> ebooking_Buyertype_grid =
        (ArrayList<EbookingValidatorFormBeanNew>) custMap.get("BY");

    if (ebooking_Buyertype_grid != null) { %>

    <div class="row form-group">
        <div class="heading_trainee_bottom_booking "></div>
        <div class="heading_trainee_booking">
            <bean:message key="ecom.bkg.custby" bundle="<%=clientcode%>" />
        </div>
    </div>

    <% for (int j = 0; j < ebooking_Buyertype_grid.size(); j++) {
        EbookingValidatorFormBeanNew customergrid = ebooking_Buyertype_grid.get(j);
        if (custtypelist != null && custtypelist.contains("BY")) {
    %>

    <div id="buyerTypePreviewTemplate" class="customer-preview m-b-3" style="<%= (j != 0) ? "border-top: 2px solid #f2b92a;padding-block: 26px;" : "" %>">
        <div class="row form-group">
            <label class="col-sm-1"><bean:message key="ebooking.customername.display" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-5 labl_preview"><%=customergrid.getEbook_Custname()%></span>

            <label class="col-sm-1"><bean:message key="ebooking.adresscust.display" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-4 labl_preview"><%=customergrid.getEbook_Custaddress()%></span>
        </div>

        <div class="row form-group">
            <label class="col-sm-1"><bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-5 labl_preview"><%=customergrid.getEbook_Custaddress1()%></span>

            <label class="col-sm-1"><bean:message key="ebooking.city.display" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-4 labl_preview"><%=customergrid.getEbook_Custcity_desc()%></span>
        </div>

        <div class="row form-group">
            <label class="col-sm-1"><bean:message key="ebooking.country.display" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-2 labl_preview"><%=(customergrid.getEbook_Custcountry() + " - " + (checkNull((String) contrylist.get(customergrid.getEbook_Custcountry()))).toUpperCase()) %></span>

            <label class="col-sm-1"><bean:message key="ebooking.telephonecontact.display" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-2 labl_preview"><%=customergrid.getEbook_Custphone()%></span>

             <label class="col-sm-1"><bean:message key="ebooking.email.display" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-2 labl_preview"><%=customergrid.getEbook_Custemail()%></span>

           
        </div>

        <div class="row form-group">


            <label class="col-sm-1"><bean:message key="Reg.State" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-2 labl_preview"><%=customergrid.getEbook_Custstate()%></span>
<%if ("Y".equalsIgnoreCase(GENS)) {%>
            <label class="col-sm-1"><bean:message key="Reg.Eori" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-2 labl_preview"><%=checkNull(customergrid.getEbook_Custeori())%></span>

            <label class="col-sm-1"><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>" /></label><span>: </span>
            <span class="col-sm-2 labl_preview"><%=checkNull(customergrid.getEbook_Custpersontype())%></span><%}%> 
        </div>
    </div>

    <% 
        } // end if custtypelist.contains("BY")
    } // end for loop
    } // end if ebooking_Buyertype_grid not null
} 
            }
%>

        </div>
    </div>
</div>
<% }
    }

%>
<div class="card card-shadow">
    <div class="card-header preview_header"> <bean:message key="shipping.cargo.head" bundle="<%=clientcode%>" /> </div>
    <div class="card-body">
        <table class="table table-striped table-bordered remove_td_title" id="bkgpreviewtable1" >
            <thead>
                <tr>
                    <th width="20px"><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>"/></th>
                    <th><div class="col-sm-12 text-center"><bean:message key="ebooking.cargo.Equipment" bundle="<%=clientcode%>"/> </div></th>

                </tr>
            </thead>
            <tbody>
                <%                    int count_row = 0;
                    String str_row = "", dgclassval = null;
                    Hashtable dgclassmap = (Hashtable) getServletContext().getAttribute("SESSION_DGCLASS");
                    String eqp = null, eqptype = null, equipstatus = null, eqpsize = null, nor = null, OOG = null;
                    String commcheck = null, commodityval = null, hscodeval = null, packageval = null, weightunit = null, volunit = null, cargovolumn = null, soc = null, hzd = null, flashunit = null, tempunit = null, oogunit = null;
                    if (cargolist != null) {
                        int com_len = cargolist.size();
                        for (int i = 0; i < com_len; i++) {
                            EbookingValidatorFormBeanNew containergrid = (EbookingValidatorFormBeanNew) cargolist.get(i);
                            eqptype = containergrid.getEbook_eqp_type();
                            equipstatus = containergrid.getEqpstatus();
                            dgclassval = "";
                            if (checkNull(containergrid.getEbook_unno()).length() > 0) {
                                dgclassval = checkNull((String) dgclassmap.get(checkNull(containergrid.getEbook_unno())));
                            }

                            eqpsize = checkNull(containergrid.getEbook_eqp());
                            commcheck = checkNull(containergrid.getCommodityothers());
                            if (commcheck.equalsIgnoreCase("Y")) {
                                commodityval = checkNull(containergrid.getEbook_commoditycode()).toUpperCase();
                            } else {
                                commodityval = (checkNull(containergrid.getEbooking_commodity()) + " ~~ " + checkNull(containergrid.getEbook_commoditycode())).toUpperCase();
                            }
                            String hscode = checkNull(containergrid.getHscode());
                            hscodeval = "";
                            if (commcheck.equalsIgnoreCase("Y")) {
                                hscodeval = hscode;
                            } else if (hscode != null && hscode.length() > 0 && !"1".equalsIgnoreCase(containergrid.getAppVersion())) {
                                if (checkNull(containergrid.getEbook_commoditycode()).trim().equalsIgnoreCase(fakComdtyCode)) {
                                    hscodeval = checkNull(containergrid.getHscode());
                                } else {
                                    hscodeval = (checkNull(containergrid.getHsCommodityVal()) + " ~~ " + checkNull(containergrid.getHscode())).toUpperCase();
                                }
                                //hscodeval = (checkNull(containergrid.getHsCommodityVal()) + " ~~ " + checkNull(containergrid.getHscode())).toUpperCase();
                            } else if (hscode != null && hscode.length() > 0) {
                                hscodeval = hscode;
                            }
                            packageval = containergrid.getEbook_packagecode();
                            if (eqptype.equalsIgnoreCase("D")) {
                                eqptype = "Dry";
                            } else if (eqptype.equalsIgnoreCase("O")) {
                                eqptype = "Open";
                            } else if (eqptype.equalsIgnoreCase("R")) {
                                eqptype = "Reefer";
                            }
                            if (equipstatus.equalsIgnoreCase("M")) {
                                equipstatus = "Empty";
                            } else if (equipstatus.equalsIgnoreCase("F")) {
                                equipstatus = "Laden";
                            }
                            weightunit = checkNull(containergrid.getEbook_weightunit());
                            if (weightunit.equalsIgnoreCase("T")) {
                                weightunit = "Metric Tons";
                            } else if (weightunit.equalsIgnoreCase("P")) {
                                weightunit = "Pounds";
                            } else if (weightunit.equalsIgnoreCase("K")) {
                                weightunit = "Kilos";
                            }
                            cargovolumn = checkNull(containergrid.getEbook_cargovolume());
                            volunit = checkNull(containergrid.getEbook_volumeunit());
                            if (cargovolumn.length() > 0) {
                                if (volunit.equalsIgnoreCase("F")) {
                                    volunit = "Cubic Feet";
                                } else {
                                    volunit = "Cubic Meters";
                                }
                            }
                            soc = checkNull(containergrid.getSoc());
                            if (soc.equalsIgnoreCase("Y")) {
                                soc = "Yes";
                            } else {
                                soc = "No";
                            }
                            OOG = checkNull(containergrid.getOog());
                            System.out.println("[ECOM-WAR] viewEbookingNew OOG==> " + OOG);
                            if (OOG.equalsIgnoreCase("Y")) {
                                OOG = "Yes";
                            } else {
                                OOG = "No";
                            }
                            hzd = checkNull(containergrid.getHzd());
                            if (hzd.equalsIgnoreCase("Y")) {
                                hzd = "Yes";
                            } else {
                                hzd = "No";
                            }
                            flashunit = checkNull(containergrid.getEbook_flashunit());
                            if (flashunit.equalsIgnoreCase("F")) {
                                flashunit = "Fahrenheit";
                            } else if ("C".equalsIgnoreCase(flashunit)) {
                                flashunit = "Celsius";
                            }
                            tempunit = checkNull(containergrid.getEbook_temperunit());
                            if (tempunit.equalsIgnoreCase("F")) {
                                tempunit = "Fahrenheit";
                            } else if ("C".equalsIgnoreCase(tempunit)) {
                                tempunit = "Celsius";
                            }
                            oogunit = checkNull(containergrid.getEbook_dimensionunit());
                            if (oogunit.equalsIgnoreCase("F")) {
                                oogunit = "Feet";
                            } else if ("M".equalsIgnoreCase(oogunit)) {
                                oogunit = "Meters";
                            }
                            nor = checkNull(containergrid.getEbook_nor());
                            if (nor.equalsIgnoreCase("Y")) {
                                nor = "Yes";
                            } else {
                                nor = "No";
                            }
                            if (count_row % 2 == 0) {
                                str_row = "active";
                            } else {
                                str_row = "info";
                            }
                            count_row++;
                            if (isinsurance == null || (!"Y".equalsIgnoreCase(isinsurance))) {
                                isinsurance = Validation.checkNull(containergrid.getIsinsuranceavailable());
                            }
                            quotenostr = Validation.checkNull(containergrid.getQuoteno());
                            if (quotenostr.length() > 0) {
                                quoteno = Validation.checkNull(quotenostr).split("~~")[0];
                            }
                %>
                <tr id="<%=count_row%>" class="preview <%=str_row%>">
                    <td><%=count_row%></td>
                    <td>
                        <div class="form-group row">
                           <!-- <label class="col-sm-2"><bean1:message key='ebooking.trust.eqpment' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="eqptype_lable"><%=eqptype%></label>-->

                            <label class=" col-sm-2"><bean:message key='ebooking.trust.eqpmentsize' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="eqpsize_lable"><%=eqpsize%></label>

                            <%if (isAmendment != null && isAmendment.equalsIgnoreCase("Y")) {
                            %>
                            <label class=" col-sm-1"><bean:message key='ebooking.containerno' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <%if("Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)){%>
                            <label class=" col-sm-2 labl_preview" id="eqpqty_lable">Container <%=count_row%></label>
                            <%} else {%>
                            <label class=" col-sm-2 labl_preview" id="eqpqty_lable"><%=containergrid.getContainerNo()%></label>
                            <%}%>
                            <%} else {%>
                            <label class=" col-sm-1"><bean:message key='ebooking.trust.eqpmentqty' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="eqpqty_lable"><%=containergrid.getEbook_eqp_qty()%></label>
                            <%}%>

                            <label class=" col-sm-2"><bean:message key='ebooking.soc.text.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="soc_lable"><%=soc%></label>
                        </div>


                        <div class="form-group row">
<!--                                <label class=" col-sm-2"><ban:message key='ebooking.packageqty.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="pkgqty_lable"><%=containergrid.getEbook_packquantity()%></label>-->                            

                            <label class=" col-sm-2" ><bean:message key='ebooking.trust.hazardous' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="hzd_lable"><%=hzd%></label>

                            <label class=" col-sm-1"><bean:message key='ebooking.eqpstatus.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="eqpstatus_lable"><%=equipstatus%></label>

                            <label class=" col-sm-2">
                                <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                                <%} else {%>
                                <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                                <%}%>
                            </label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="package_lable"><%=packageval%></label>

                        </div>


                        <div class="form-group row">

                            <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                            <label class=" col-sm-2">
                                <bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/>
                            </label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="pkgqty_lable"><%=containergrid.getEbook_packquantity()%></label>
                            <%} else {%>
                            <label class=" col-sm-2">
                                <bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/>
                            </label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="pkgqty_lable"><%=containergrid.getEbook_packquantity()%></label>
                            <%}%>

                            <label class=" col-sm-1"><bean:message key='ebooking.tareweight.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="tareweight_lable"><%=checkNull(containergrid.getEbook_tareweight()).length() > 0 ? containergrid.getEbook_tareweight()+" "+weightunit : "" %> </label>

                            <label class=" col-sm-2"><bean:message key='ebooking.cargoweight.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="cargowt_lable"><%=checkNull(containergrid.getEbook_cargoweight()).length() > 0  ? containergrid.getEbook_cargoweight() + " " + weightunit : "" %></label>

                        </div>

                        <div class="form-group row">

                            <label class=" col-sm-2"><bean:message key='ebooking.grossweight.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="grosswt_lable"><%=checkNull(containergrid.getEbook_grossweight()).length() > 0 ? containergrid.getEbook_grossweight() + " " + weightunit : "" %></label>

                            <label class=" col-sm-1"><bean:message key='ecom.cargo.netweight' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="netwt_lable"><%=checkNull(containergrid.getEbook_netweight()).length() > 0 ? containergrid.getEbook_netweight() + " " + weightunit : "" %></label>
                            <%if (is_ecom_OOG) {%>
                            <label class=" col-sm-1"><bean:message key='ebooking.oog.text.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="oog_lable"><%=OOG%></label>
                            <%}%>
                        </div>

                        <div class="form-group row">
                            <label class=" col-sm-2">
                                <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                <bean:message key='ebooking.trust.commoditygrp' bundle="<%=clientcode%>"/>
                                <%} else {%>
                                <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/>
                                <%}%>
                                <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%></label>
                            <span class=""> : </span>
                            <label class=" col-sm-8 labl_preview" id="commodity_lable"><p><%=commodityval%></p></label>
<!--                                <label class=" col-sm-1"><ben:message key='ebooking.package.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-3 labl_preview" id="package_lable"><p><%=packageval%></p></label>-->
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
                            <label class=" col-sm-8 labl_preview" id="hscode_lable"><%=hscodeval%></label>
                        </div>

                        <div class="form-group row">

                            <label class=" col-sm-2"><bean:message key='ebooking.containervolume.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="volume_lable"><%=containergrid.getEbook_cargovolume() + "  " + volunit%></label>
<!--                            <label class=" col-sm-1 labl_preview labe_width" id="volunit_lable"><%=volunit%></label>-->
                            <%if ("Y".equalsIgnoreCase(enableActCommoName)) {%>
                            <label class=" col-sm-2"><bean:message key='shipping.actcommoname.display' bundle="<%=clientcode%>"/></label>
                            <span class=""> : </span>
                            <label class=" col-sm-5 labl_preview" id="actcommoname_lable"><%=containergrid.getEbook_actCommoName()%></label>
                            <%}%>
                        </div>
                        <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                           <div class="form-group row">

                            <label class=" col-sm-2"><bean:message key='ebooking.cmdtyhblno.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="cmdtyhblno_lable"><%=containergrid.getEbook_cmdtyhblno() %></label>

                           

                            <label class=" col-sm-1"><bean:message key='ebooking.sellersName.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="sellersName_lable"><%=containergrid.getEbook_cmdtysellerName() %></label>

                           

                            <label class=" col-sm-1"><bean:message key='ebooking.buyersName.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="buyersName_lable"><%=containergrid.getEbook_cmdtybuyerName() %></label>
                     </div>
                       <%}%>
                        <%if (containergrid.getHzd() != null && containergrid.getHzd().equalsIgnoreCase("Y")) {%>
                        <div class="hazardous" id="hzddivpreview">
                            <hr>
                            <br>
                            <%
                                // Multiple DG Details Support - Display all DG entries
                                List<net.ecom.vo.EbookingValidatorFormBeanNew> dgDetailsList = containergrid.getDgDetailsList();
                               // System.out.println("dgDetailsList: " + dgDetailsList);
                                if (dgDetailsList != null && !dgDetailsList.isEmpty()) {
                                    // Display multiple DG entries
                                    //System.out.println("dgDetailsList size: " + dgDetailsList.size());
                                    for (int dgIdx = 0; dgIdx < dgDetailsList.size(); dgIdx++) {
                                        net.ecom.vo.EbookingValidatorFormBeanNew dgEntry = dgDetailsList.get(dgIdx);
                                        String dgUnno = checkNull(dgEntry.getEbook_unno());
                                     //   System.out.println("dgUnno: " + dgUnno);
                                        String dgShipName = "";
                                        if (dgUnno.length() > 0) {
                                            dgShipName = checkNull((String) dgclassmap.get(dgUnno));
                                        }
                                        
                                        if (dgIdx > 0) {
                            %>
                            <hr style="border-top: 1px dashed #ccc; margin: 15px 0;">
                            <%
                                        }
                            %>
                            <% if (dgDetailsList.size() > 1) { %>
                            <div class="form-group row">
                                <label class="col-sm-12" style="font-weight: bold; color: #333;">
                                    <bean:message key='ebooking.DG.details' bundle="<%=clientcode%>"/> #<%=(dgIdx + 1)%>
                                </label>
                            </div>
                            <% } %>
                            <div class="form-group row">
                                <label class="col-sm-2 "><bean:message key='ebooking.marine.pollut' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview"><%="Y".equalsIgnoreCase(dgEntry.getEbook_poluttunit()) ? "Yes" : "No"%></label>
                                <label class=" col-sm-1"><bean:message key='ebooking.UN.no' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview"><%=dgEntry.getEbook_unno()%></label>
                                <label class=" col-sm-2"><bean:message key='ebooking.flash.point' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview"><%=dgEntry.getEbook_flashpoint()%> <%=dgEntry.getEbook_flashunit()%></label>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-2 "><bean:message key='ebooking.DG.class' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview"><%=dgEntry.getEbook_dgclass()%></label>
                                <label class="col-sm-1 "><bean:message key='ebooking.shipping.name' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-4 labl_preview"><%=dgShipName%></label>
                            </div>
                            <%
                                    }
                                } else {
                                    // Fallback to single DG entry (backward compatibility)
                            %>
                            <div class="form-group row">
                                <label class="col-sm-2 "><bean:message key='ebooking.marine.pollut' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="pollutant_lable"><%="Y".equalsIgnoreCase(containergrid.getEbook_poluttunit()) ? "Yes" : "No"%></label>
                                <label class=" col-sm-1"><bean:message key='ebooking.UN.no' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="unno_lable"><%=containergrid.getEbook_unno()%></label>
                                <label class=" col-sm-2"><bean:message key='ebooking.flash.point' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="flashpt_lable"><%=containergrid.getEbook_flashpoint()%> <%=containergrid.getEbook_flashunit()%></label>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-2 "><bean:message key='ebooking.DG.class' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="dgclass_lable"><%=containergrid.getEbook_dgclass()%></label>
                                <label class="col-sm-1 "><bean:message key='ebooking.shipping.name' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-4 labl_preview" id="shippingname_lable" ><%=dgclassval%></label>
                            </div>
                            <%
                                }
                            %>
                            <hr>
                        </div>
                        <%}%>

                        <%if (containergrid.getEbook_eqp_type().equalsIgnoreCase("R")) {%>
                        <div class="reefer" id="refdivpreview">
                            <hr>
                            <br>
                            <div class="form-group row">
                                <label class=" col-sm-2"> <bean:message key="ebooking.cargo.NORhead" bundle="<%=clientcode%>"/> </label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="nor_lable"><%=nor%></label>

                                <label class="col-sm-1 "><bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="settemp_lable"><%=NumberFormat.numberFormat(checkNull(containergrid.getEbook_presetemp()))%></label>

                                <%if (!"Y".equalsIgnoreCase(hideMinMaxTemp)) {%>
                                <label class="col-sm-2"><bean:message key='ebooking.min.temp' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="mintemp_lable"><%=NumberFormat.numberFormat(checkNull(containergrid.getEbook_mintemp()))%></label>
                            </div>
                            <div class="form-group row">
                                <label class=" col-sm-2"><bean:message key="ebooking.max.temp" bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="maxtemp_lable"><%=NumberFormat.numberFormat(checkNull(containergrid.getEbook_maxtemp()))%></label>
                                <%}%>
                                <label class=" col-sm-1"><bean:message key="ebooking.temp.unit" bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="tempunit_lable"><%=checkNull(tempunit)%></label>
                                <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>
                                <label class="col-sm-2"><bean:message key='ebooking.humidity' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="humidity_lable"><%=checkNull(containergrid.getHumidity())%></label>
                                <%}%>
                            </div>
                            <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>
                            <div class="form-group row">
                                <label class=" col-sm-2"><bean:message key='ebooking.drains' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="drains_lable"><%=("O".equalsIgnoreCase(containergrid.getDrains()) ? "Open" : "C".equalsIgnoreCase(containergrid.getDrains()) ? "Closed" : "")%></label>
                                <label class=" col-sm-1"><bean:message key='ebooking.co2' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="co2_lable"><%=checkNull(containergrid.getCo2())%></label>
                                <label class="col-sm-2"><bean:message key='ebooking.o2' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="o2_lable"><%=checkNull(containergrid.getO2())%></label>
                            </div>
                            <%}%>
                            <div class="form-group row">
                                <label class="col-sm-2"><bean:message key='ebooking.vent' bundle="<%=clientcode%>"/><%= Ventilationlabel%></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="vent_lable"><%=checkNull(containergrid.getVentilation())%></label>
                            </div>
                            <hr>
                        </div>
                        <%}%>
                        <%if (containergrid.getEbook_eqp_type().equalsIgnoreCase("o")) {%>
                        <div class="opencar" id="oogdivpreview">
                            <hr>
                            <br>
                            <div class="form-group row">
                                <label class="col-sm-2 "><bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="dimunit_lable"><%=oogunit%></label>

                                <label class=" col-sm-1"><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="owleft_lable"><%=containergrid.getEbook_owleft()%></label>

                                <label class="col-sm-2"><bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="owright_lable"><%=containergrid.getEbook_owright()%></label>

                            </div>
                            <div class="form-group row">
                                <label class="col-sm-2 "><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="olforward_lable"><%=containergrid.getEbook_olforward()%></label>
                                <label class="col-sm-1"><bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="olaft_lable"><%=containergrid.getEbook_olaft()%></label>

                                <label class=" col-sm-2"><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/></label>
                                <span class=""> : </span>
                                <label class=" col-sm-2 labl_preview" id="oh_lable"><%=containergrid.getEbook_oh()%></label>
                            </div>
                            <hr>
                        </div>
                    </td>
                </tr>
                </td>
                <%}%>
                <%}
                    }%>
            </tbody>
        </table>
    </div>
</div>
<%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
<div class="card card-shadow">
    <div class="card-header preview_header"><bean:message key="ecom.bkg.haulagedet" bundle="<%=clientcode%>"/> </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-bordered" id="datatablebyhaulage">
                <thead>
                    <tr>

                        <%if (isAmendment != null && isAmendment.equalsIgnoreCase("Y")) {
                        %>
                        <th><bean:message key="ebooking.haul.EqpId" bundle="<%=clientcode%>"/></th>
                            <%} else {%>
                        <th><bean:message key="ecom.bkg.eqpseqno" bundle="<%=clientcode%>"/> </th>
                            <%}%>
                        <th><bean:message key="ecom.bkg.eqptype" bundle="<%=clientcode%>"/> </th>
                        <th class='d-none' ><bean:message key="ecom.bkg.pickuploccode" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="ecom.bkg.pickuploc" bundle="<%=clientcode%>"/></th>
                        <th class='' ><bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/></th>
                        <th class='' ><bean:message key="ecom.bkg.refno" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="ecom.bkg.dropdate" bundle="<%=clientcode%>"/></th >
                        <th><bean:message key="ecom.bkg.stopseq" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="ecom.bkg.contctper" bundle="<%=clientcode%>"/></th>
                        <th><bean:message key="ecom.bkg.contctphone" bundle="<%=clientcode%>"/></th>
                        <th class='' ><bean:message key="ecom.bkg.contctmail" bundle="<%=clientcode%>"/></th>

                        <th><bean:message key="ecom.bkg.tmsovrwt" bundle="<%=clientcode%>"/></th>

                        <th><bean:message key="ebooking.remarks.display" bundle="<%=clientcode%>"/></th>
                    </tr>
                </thead>
                <tbody>
                    <%if (haulagelist != null && haulagelist.size() > 0) {
                            BookingHaulagedetailsVo haulvo = null;
                            for (int i = 0; i < haulagelist.size(); i++) {
                                haulvo = haulagelist.get(i);
                    %>
                    <tr>
                        <%if (isAmendment != null && isAmendment.equalsIgnoreCase("Y")) {
                        %>
                        <td class="eqpid"><%=Validation.checkNull(haulvo.getEqpid())%></td>
                        <%} else {%>
                        <td class="eqprowid"><%=Validation.checkNull(haulvo.getDummyeqp())%></td>
                        <%}%>
                        <td><%=Validation.checkNull(haulvo.getEqptype())%></td>
                        <td class='d-none' ><%=Validation.checkNull(haulvo.getPickloccode())%></td>
                        <td><%=Validation.checkNull(haulvo.getPicklocname())%></td>
                        <td class='' ><%=Validation.checkNull(haulvo.getAddress())%></td>
                        <td class='' ><%=Validation.checkNull(haulvo.getRefno())%></td>
                        <td><%=Validation.checkNull(haulvo.getDropdate())%></td>
                        <td><%=haulvo.getStopsequence()%></td>
                        <td><%=Validation.checkNull(haulvo.getContactperson())%></td>
                        <td><%=Validation.checkNull(haulvo.getContactphone())%></td>
                        <td class='' ><%=Validation.checkNull(haulvo.getContactemail())%></td>

                        <td><%=Validation.checkNull(haulvo.getTwrsoverweight())%></td>

                        <td><%=Validation.checkNull(haulvo.getRemarks())%></td>

                    </tr>
                    <%     }
                        }%>
                </tbody>
            </table>
        </div>
    </div>   
</div>
<%}%>
<%
    System.out.println("[ECOM-WAR] viewEbookingNew ENABLE_INSURANCE=> " + ENABLE_INSURANCE);
    System.out.println("[ECOM-WAR] viewEbookingNew isinsurance=> " + isinsurance + " " + quoteno);
    if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(isinsurance) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
<div class="card  card-shadow">
    <div class="card-header preview_header"> <bean:message key="booking.cargo.insurance" bundle="<%=clientcode%>" /> </div>
    <div class="card-body" id="bkgviewinsurance">


    </div>
</div>
<%}
    String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);

    if ("Y".equalsIgnoreCase(enableCargoProtect)) {
%>
<div class="card  card-shadow">
    <div class="card-header preview_header"> <bean:message key="booking.cargo.additionalproductandcharges" bundle="<%=clientcode%>" /> </div>
    <input type="hidden"  id="cargoprotectid"  name="cargoprotectid" value="<%=cargoprotectid%>"/>
    <div class="card-body" id="cargoprotect_table_div">


    </div>
</div>

<%}%>
<%if ("Y".equalsIgnoreCase(GENS)) {%>
<%if (icslist != null) {%>
    <div class="card card-shadow" id="euportCard" style="display:none">
        <div class="card-header preview_header"> <bean:message key="shipping.ens.heading" bundle="<%=clientcode%>" /> <a href="javascript:void(0);" onclick="$('#SIEditForm-t-4').click();" class="float-right text-white"> <i class="fa fa-pencil"></i>  </a> </div>
        <div class="card-body">
            <div class="col-sm-12">
                <div id="ens_tab_div_preview">
                    <div id="ensmasterfields">
                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.eu.customs.zone" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>
                            
                                <label class="col-sm-4 labl_preview" >
                                    <%=
                                    ("Y".equalsIgnoreCase(Validation.checkNull(icslist.getEuCustZone())) ? "Yes" : "No")
                                %>
                                </label>
                            
                            <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.of.bl" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>

                            <label class="col-sm-4 labl_preview" ><%=Validation.checkNull(icslist.getBlTypeENS())%></label>

                        </div>
                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.type.ens.filling" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>

                            <label class="col-sm-4 labl_preview" ><%=Validation.checkNull(icslist.getEnsFillingType())%></label>                                   

                            <label class="col-sm-2 control-label"> <bean:message key="shipping.ens.method.of.payment" bundle="<%=clientcode%>"/>  <span class=""> : </span> </label>

                            <label class="col-sm-4 labl_preview" "><%=Validation.checkNull(icslist.getPaymentMethod())%></label>

                        </div>
                    </div>
                    <%
                    if (bookinglist != null) {
                        int len = bookinglist.size();
                        String haulgeType;
                         String preferredAgency="";
                        for (int i = 0; i < len; i++) {
                            EbookingValidatorFormBeanNew masterdata = (EbookingValidatorFormBeanNew) bookinglist.get(i);%>
                           
                
                   <%if (masterdata.getEbook_Buyername() != null && masterdata.getEbook_Buyername().trim().length() > 0) {%>
                    <div id='by_hd'>
                       
        
                            <% if (custtypelist != null && custtypelist.contains("BY")) {%>
                            <div class="header_tle"> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>  </div>
        
                     
        
                        <div class="row form-group">
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-5 labl_preview" id="Buyername_label"> <%=checkNull(masterdata.getEbook_Buyername())%></span>
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-4 labl_preview " id="Buyeradress_label"><%=checkNull(masterdata.getEbook_Buyeraddress())%></span>
        
        
        
                        </div>
        
                        <div class="row form-group">
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-5 labl_preview" id="Buyeradress1_label"><%=checkNull(masterdata.getEbook_Buyeraddress1())%></span>
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-4 labl_preview " id="Buyercity_label"><%=checkNull(masterdata.getEbook_Buyercity())%></span>
        
        
        
                        </div>
        
        
                        <div class="row form-group">
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Buyercountry_label"><%=checkNull(masterdata.getEbook_Buyercountry_desc())%></span>
        
                            <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Buyerphone_label"><%=checkNull(masterdata.getEbook_Buyerphone())%></span>
                            <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview labe_width" id="Buyerfax_label"><%=checkNull(masterdata.getEbook_Buyerfax())%></span>
                            <%}%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview " id="Buyeremail_label"><%=checkNull(masterdata.getEbook_Buyeremail())%></span>
                            <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="Buyerzipcode_label"><%=checkNull(masterdata.getEbook_Buyerzipcode())%> </span>
                            <%}%>   
                        </div>
                        <div class="row form-group">
        
                            <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Buyerstate_label" ><%=checkNull(masterdata.getEbook_Buyerstate())%></span>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Buyereori_label" ><%=checkNull(masterdata.getEbook_Buyereori())%></span>
        
                            <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Buyerpersontype_label" ><%=checkNull(masterdata.getEbook_Buyerpersontype())%></span>
                            <%}%>    
                        </div>
                    </div> 
                    <%}
                    }%>
                       <% if (masterdata.getEbook_Sellername() != null && masterdata.getEbook_Sellername().trim().length() > 0) {%>
                    <div id='sl_hd'>
                      
        
                            <% if (custtypelist != null && custtypelist.contains("SL")) {%>
                            <div class="header_tle "> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>  </div>
        
                      
        
                        <div class="row form-group">
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-5 labl_preview" id="Sellername_label"> <%=checkNull(masterdata.getEbook_Sellername())%></span>
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-4 labl_preview " id="Selleradress_label"><%=checkNull(masterdata.getEbook_Selleraddress())%></span>
        
        
        
                        </div>
        
                        <div class="row form-group">
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.adress1cust.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-5 labl_preview" id="Selleradress1_label"><%=checkNull(masterdata.getEbook_Selleraddress1())%></span>
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-4 labl_preview " id="Sellercity_label"><%=checkNull(masterdata.getEbook_Sellercity())%></span>
        
        
        
                        </div>
        
        
                        <div class="row form-group">
        
                            <label class="col-sm-1 "> <bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Sellercountry_label"><%=checkNull(masterdata.getEbook_Sellercountry_desc())%></span>
        
                            <label class="col-sm-1 p-l-10"> <bean:message key='ebooking.telephonecontact.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Sellerphone_label"><%=checkNull(masterdata.getEbook_Sellerphone())%></span>
                            <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.fax.display' bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview labe_width" id="Sellerfax_label"><%=checkNull(masterdata.getEbook_Sellerfax())%></span>
                            <%}%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.email.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview " id="Selleremail_label"><%=checkNull(masterdata.getEbook_Selleremail())%></span>
                            <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-1 "> <bean:message key='ebooking.zipcode.display' bundle="<%=clientcode%>"/> </label>
                            <span class=""> : </span>
                            <span class="col-sm-1 labl_preview " id="Sellerzipcode_label"><%=checkNull(masterdata.getEbook_Sellerzipcode())%> </span>
                            <%}%>
                        </div>
                        <div class="row form-group">
        
                            <label class="col-sm-1 "><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Sellerstate_label" ><%=checkNull(masterdata.getEbook_Sellerstate())%></span>
                            <%if ("Y".equalsIgnoreCase(GENS)) {%>
                            <label class="col-sm-1 "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>

                            <span class="col-sm-2 labl_preview" id="Sellereori_label" ><%=checkNull(masterdata.getEbook_Sellereori())%></span>
        
                            <label class="col-sm-1 "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/>  </label>
                            <span class=""> : </span>
        
                            <span class="col-sm-2 labl_preview" id="Sellerpersontype_label" ><%=checkNull(masterdata.getEbook_Sellerpersontype())%></span>
                            <%}%>    
                        </div>
                    </div> 
                    <%}
                    }
                    }
                    }%>
                    <div id="supplementarydeclarant_preview" style='<%= "Multiple Filing".equals(icslist.getEnsFillingType()) ? "":"display: none;" %>'>    
                    <div class="header_tle">  <bean:message key="shipping.ens.supplementary.declarant" bundle="<%=clientcode%>"/>  </div>

                    <div class="row form-group">
                      
                            <label class="col-sm-1"><bean:message key='shipping.ens.declarant.name' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantName_label" style="border: none;"><%=checkNull(icslist.getDeclarantName())%></label>
                        
                        
                            <label class="col-sm-2" ><bean:message key='shipping.ens.declarant.address' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantAddress_label" style="border: none;"><%= checkNull(icslist.getDeclarantAddress())%></label>
                        
                        
                            <label class="col-sm-2"><bean:message key='shipping.ens.declarant.address1' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantAddress1_label" style="border: none;"><%=checkNull(icslist.getDeclarantAddress1())%></label>
                        </div>
                        <div class="row form-group">
                        
                            <label class="col-sm-1" ><bean:message key='shipping.ens.declarant.city' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantCity_label" style="border: none;"><%=checkNull(icslist.getDeclarantCity())%></label>
                           
                            <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                            <label class="col-sm-2"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantZipcode_label" style="border: none;"><%=checkNull(icslist.getDeclarantZipcode())%></label>
                            <%}%>

                           
                            <label class="col-sm-2"><bean:message key='shipping.ens.declarant.country' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantCountry_label" style="border: none;"><%= checkNull(icslist.getDeclarantCountry())%></label>
                      
                    
                         </div>
                         <div class="row form-group">
                            
                            <label class="col-sm-1"><bean:message key='shipping.ens.declarant.state' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantState_label" style="border: none;"><%=checkNull(icslist.getDeclarantState())%></label>
                     

                            <label class="col-sm-2"><bean:message key='shipping.ens.declarant.telephone' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantTelephone_label" style="border: none;"><%=checkNull(icslist.getDeclarantTelephone())%></label>
                    
                            <label class="col-sm-2"><bean:message key='shipping.ens.declarant.eori' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantEORI_label" style="border: none;"><%=checkNull(icslist.getDeclarantEORI())%></label>
                      
                        </div>
                         <div class="row form-group">

                            <label class="col-sm-1"><bean:message key='shipping.ens.declarant.email' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>       
                            <label class=" col-sm-2 labl_preview" id="declarantEmail_label" style="border: none;"><%=checkNull(icslist.getDeclarantEmail())%></label>
                         
                            <label class="col-sm-2"><bean:message key='shipping.ens.declarant.fillingtype' bundle="<%=clientcode%>"/>  </label><span class=""> : </span>
                            <label class=" col-sm-2 labl_preview" id="declarantFillingType_label" style="border: none;"><%=checkNull(icslist.getDeclarantFillingType()  )%></label>
                       
                    </div>
                </div>
            </div>
        </div>
    </div>
  </div>
    <%}
}%>
<%
    String wizardwidth = null;

    if ("GWF".equalsIgnoreCase(carrierCode)) {
        wizardwidth = "25%";
    } else {
        wizardwidth = "33.33%";
    }
%>
<style type="text/css">
    /*    .wizard .steps {
            margin: 0 5% 80px 5%;
        }
        .wizard > .steps > ul > li {
            width: <%=wizardwidth%>;
        }*/
</style>
<%} catch (Exception e) {
        e.printStackTrace();
    }%>
<script type="text/javascript" src="js/Booking.js?<%=jsdate%>"></script>
<script type="text/javascript">
                function viewcargodetails(id) {
                    // document.getElementById('cargodetailsid').value = id;
                    $(".fancybox1").fancybox({
                        maxWidth: 1000,
                        maxHeight: 800,
                        fitToView: true,
                        width: '80%',
                        height: '60%',
                        autoSize: false,
                        scrollOutside: true,
                        href: "ViewBookingCargoDetails.jsp?id1=" + id,
                        closeClick: false
                    });

                }
//                                    $(document).ready(function () {
//
//                                        var form = $("#bookingviewform").show();
//                                        form.steps({
//                                            headerTag: "h3",
//                                            bodyTag: "fieldset",
//                                            autoFocus: true,
//                                            enableAllSteps: true,
//                                            enablePagination: false
//                                        });
//                                        $(".steps li").addClass("done");
//                                        $(".steps li.first").removeClass("done");
//                                        $(".steps li a span.number").addClass("glyphicon glyphicon-ok").text("");
//                                    });
                function backtobookview() {
                    //callLoading();
    <%if ("YES".equalsIgnoreCase(backtodashboard)) {%>
                    location.href = "Welcome.do";
    <%} else {%>
                    location.href = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
    <%}%>
                }
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
                                $('#bkgviewinsurance').html(data);
                                //closeLoader();
                                /*  $.fancybox.open(data, {
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
                viewinsurance('<%=quoteno%>');
                loadCargoProtect('view');
                chkEU_ENSDeclarationView();
   function chkEU_ENSDeclarationView() {
    if ($('#euportCard').length > 0) {
        var pol = $('#pickupplace_label').text();
        var pod = $('#deliveryplace_label').text();

        if (pol !== "" && pod !== "") {
            $.ajax({
                type: 'POST',
                cache: false,
                url: 'EnsFilingAjax?type=checkEUPort&origin=' + encodeURIComponent(pol) + '&delivary=' + encodeURIComponent(pod),
                async: false,
                success: function (response) {
                    if (response === 'Y') {
                        $('#euport').val('Y');
                        $('#euportCard').show();
                    } else {
                        $('#euport').val('N');
                        $('#euportCard').hide();
                    }
                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            });
        } else {
            $('#euport').val('N');  
            $('#euportCard').hide();
        }
    }
}
</script>

<style type="text/css">
    /*    #perviewpanel .control-label{
            text-align: left;
        }
        .fontstyle{
            font-weight:100 !important;
        }
        #perviewpanel p {
            margin: 0 0 10px;
            word-wrap: break-word;
        }*/
</style>
<style>
    /*    #comdtydropdown .tt-dropdown-menu {
            max-height: 140px;
            overflow-y: auto;
        }
        table {
            border-spacing: 0px;
            border-collapse: inherit;
        }
        .table > tbody > tr.active > td, .table > tbody > tr.active > th, .table > tbody > tr > td.active, .table > tbody > tr > th.active, .table > tfoot > tr.active > td, .table > tfoot > tr.active > th, .table > tfoot > tr > td.active, .table > tfoot > tr > th.active, .table > thead > tr.active > td, .table > thead > tr.active > th, .table > thead > tr > td.active, .table > thead > tr > th.active {
            background-color: #F5F5F5;
            border: 1px solid #CCC !important;
        }
        .table>tbody>tr.info>td, .table>tbody>tr.info>th, .table>tbody>tr>td.info, .table>tbody>tr>th.info, .table>tfoot>tr.info>td, .table>tfoot>tr.info>th, .table>tfoot>tr>td.info, .table>tfoot>tr>th.info, .table>thead>tr.info>td, .table>thead>tr.info>th, .table>thead>tr>td.info, .table>thead>tr>th.info {
            background-color: #d9edf7;
            border: 1px solid #CCC !important;
        }
        .haulagehide {
            display: none;
        }*/
</style>


