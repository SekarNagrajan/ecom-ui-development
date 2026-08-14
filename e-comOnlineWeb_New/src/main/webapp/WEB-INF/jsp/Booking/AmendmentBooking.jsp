
<%@page contentType="text/html"  pageEncoding="UTF-8"%>
<%@taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@page import="java.util.Locale,java.util.ArrayList,java.util.LinkedHashMap,org.apache.struts.util.MessageResources,org.apache.struts.action.ActionForm,net.ecom.common.MaskPort,net.ecom.common.utils.MaskPortUtil"%>
<%@page import="net.ecom.formbeans.AmendmentBookingFormBean,java.util.Date,net.ecom.common.Validation,net.ecom.vo.Route,java.util.Map,java.util.List,net.ecom.vo.CutoffConfigVO,net.ecom.vo.TransportServiceVO,net.ecom.vo.CustomerDetailsVO,java.util.Hashtable,java.util.Set,net.ecom.common.GeneralConf,net.ecom.vo.BookingHaulagedetailsVo,net.ecom.common.utils.DateConversion_New"%>
<%
    String cacheVersion = (String) getServletContext().getAttribute("CACHEVERSION");
    cacheVersion = cacheVersion != null && cacheVersion.length() > 0 ? "?" + cacheVersion : "";
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    Map<String, String> temrminalMap = (Map<String, String>) getServletContext().getAttribute("TEMINALSMAPCONTEXT");
    String custTypeConfig = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.APBPCONFIGFRONTEND));
    String v1DataEnableStatus = (String) getServletContext().getAttribute(GeneralConf.V1DATAENABLESTATUS);
    //String emptyPlaceWithoutTerminal = (String) getServletContext().getAttribute(GeneralConf.EMPTYPLACEWITHOUTTERMINAL);
    //String emptyPlaceWithTerminal = (String) getServletContext().getAttribute(GeneralConf.EMPTYPLACEWITHTERMINAL);
    String pdfpath = (String) getServletContext().getAttribute("Imagepath");
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String errors_new = (String) request.getAttribute("bookingerrors") != null ? (String) request.getAttribute("bookingerrors") : "";
    String allowIncompRegPlugin = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN));
    String incompleteRegSession = Validation.checkNull((String) session.getAttribute("SESSION_INCOMPLETED_REG"));
    String kycPending = ("Y".equalsIgnoreCase(allowIncompRegPlugin) && "Y".equalsIgnoreCase(incompleteRegSession)) ? "Y" : "N";
    session.setAttribute("Booking", "Send");
    String apDisable = (String) request.getAttribute("AMENDMENTAPDISABLE");
    String defOrgDestHaul = (String) getServletContext().getAttribute("DefOriginDestHaulValue");
    String ccMailId = (String) session.getAttribute("SESSION_USER_CC_MAILID");
    String type12 = ((String) session.getAttribute("type"));
    String bookingType = ((String) session.getAttribute("type"));
    String sch_routeid = ((String) session.getAttribute("routeid"));
    String user = (String) session.getAttribute("sessionAdmin");
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    List addInfoList = (List) getServletContext().getAttribute(GeneralConf.AdditionalInfoBkg);
    String hscode_autoset = (String) getServletContext().getAttribute(GeneralConf.Disable_Auto_HSCode_Populate);
    String addInfoID = null;
    long jsdate = new Date().getTime();
    String clientcode = (String) session.getAttribute("clientcode");
    ArrayList route = (ArrayList) session.getAttribute("route");
    Map<String, String> mailAlertMap = (Map<String, String>) session.getAttribute("USERMAILALERTSESSIONMAP");
    String checkMailConfig = "N";
    String hideMinMaxTemp = (String) getServletContext().getAttribute(GeneralConf.Hide_Min_MaxTemp);
    String userFruitCust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
    String custMandatory = (String) getServletContext().getAttribute(GeneralConf.CUSTMANDATORY);
    String fakCommodity = (String) getServletContext().getAttribute(GeneralConf.FAKCOMMODITY);
    String fakComdtyCode = "";
    String fakComdtyName = "";
    System.out.println("[ECOM-WAR] AmendmentBooking fakCommodity>>>>" + fakCommodity);
    fakCommodity = fakCommodity != null ? fakCommodity : "";
    if (fakCommodity != null && fakCommodity.length() > 0) {
        String fakCommodityArr[] = fakCommodity.split("~~");
        if (fakCommodityArr.length == 2) {
            fakComdtyCode = fakCommodityArr[0].trim();
            fakComdtyName = fakCommodityArr[1].trim();
        }
    }
    Map<String, List<String>> map = (Map<String, List<String>>) getServletContext().getAttribute("MAILALERTACTIVITYLIST");
    String Enable_Customize_Import_Export_Excel = (String) getServletContext().getAttribute(GeneralConf.Enable_Customize_Import_Export_Excel);
    if (map != null) {
        List<String> mailActList = map.get("BKG");
        System.out.println("[ECOM-WAR] AmendmentBooking mailActList>>>" + mailActList+ "  mailAlertMap>>>" + mailAlertMap);
        if (mailActList != null && mailAlertMap != null) {
            for (int i = 0; i < mailActList.size(); i++) {
                System.out.println("[ECOM-WAR] AmendmentBooking mailAlertMap.get(mailActList.get(i))>>" + mailAlertMap.get(mailActList.get(i)));
                if ("Y".equalsIgnoreCase(mailAlertMap.get(Validation.checkNull(mailActList.get(i)).trim()))) {
                    checkMailConfig = "Y";
                }
            }
        }
    }
    List<BookingHaulagedetailsVo> haulagelist = (List<BookingHaulagedetailsVo>) request.getAttribute("EBOOKINGHAULAGEDETAILS");
    String custCode = (String) session.getAttribute("SESSION_CUSTCODE");
    String splcustgrp = (String) session.getAttribute("SESSION_SPLCUSTGRP");
    System.out.println("[ECOM-WAR] AmendmentBooking checkMailConfig>>" + checkMailConfig);
    String exceluploadpath = (String) getServletContext().getAttribute(GeneralConf.exceluploadpath);
    request.setAttribute("Preview_Booking_Type", "A");
    ArrayList routeDetails = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");
    String termsandconditions = (String) getServletContext().getAttribute("Terms_and_Conditions");
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
    String Disable_CommodityGRP = (String) getServletContext().getAttribute(GeneralConf.Disable_CommodityGRP);
    String Enable_ConfirmedBooking_Edit = (String) getServletContext().getAttribute(GeneralConf.Enable_ConfirmedBooking_Edit);
    String ENABLE_OOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String hscode_search = (String) getServletContext().getAttribute(GeneralConf.Enable_HSCode_Search);
    String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
    String TENS = (String) getServletContext().getAttribute(GeneralConf.TENS);
    String openCommodityJson = (String) getServletContext().getAttribute("OpenCommodityJson");
    String EuropeCountryJson = (String) getServletContext().getAttribute("EuropeCountryJson");
    String Enable_preferAgency = (String) getServletContext().getAttribute(GeneralConf.Enable_PreferredAgency);
    String preferredAgencyJson = (String) getServletContext().getAttribute("preferredAgencyJson");
    String preferredAgencyNameJson = (String) getServletContext().getAttribute("preferredAgencyNameJson");
    String agencyRefNoLength = (String) getServletContext().getAttribute(GeneralConf.AgencyRefNo_Length);
    String Enable_SISubmit_Party = (String) getServletContext().getAttribute(GeneralConf.Enable_SISubmit_Party);
     String sISubmit_Party_CusType =(String) getServletContext().getAttribute("customernameJson");
     String Enable_Zip_Code =(String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
        
        if (agencyRefNoLength == null || agencyRefNoLength.length() == 0) {
            agencyRefNoLength = "10";
        }
    String Enable_T2LFiling = (String) getServletContext().getAttribute(GeneralConf.Enable_T2LFiling);
    String Hide_AP_CUST_BKG_Master = (String) getServletContext().getAttribute(GeneralConf.Hide_AP_CUST_BKG_Master);
    String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);

    String is_ecom_OOG = "N";
    if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
        is_ecom_OOG = "Y";
    }
    String DPWShipperTypeList = (String) getServletContext().getAttribute(GeneralConf.DPWShipperTypeList);
    String[] DPWShipperTypeListArr = null;
    if (DPWShipperTypeList != null) {
        DPWShipperTypeListArr = DPWShipperTypeList.split("~~");
    }
    if (route != null) {
%>
<script>
    $(document).ready(function () {
        $(".routingdiv").show();
        $(".vesseldiv").hide();
    });
</script>
<%}%>
<script type="text/javascript">
    var fakComdtyCode = "<%=fakComdtyCode%>";
    var fakComdtyName = "<%=fakComdtyName%>";
    var checkMailConfig = '<%=checkMailConfig%>';
    var v1DataEnableStatus = '<%=v1DataEnableStatus%>';
    var dryEquipmentListJs;
    var openEquipmentListJs;
    var referEquipmentListJs;
    var allEquipmentListJs;
    var defOrgDesHaulValue = "<%=defOrgDestHaul%>";
    var openCommodityJson = '<%=openCommodityJson%>';
    var openCommodityJsonArray = JSON.parse(openCommodityJson);
    var europeCountryJson = JSON.parse('<%=EuropeCountryJson%>');
    var preferredAgencyJson = JSON.parse('<%=preferredAgencyJson%>');
    var preferredAgencyNameJson = JSON.parse('<%=preferredAgencyNameJson%>');
    var customernameJson = JSON.parse('<%=sISubmit_Party_CusType%>');
    var DPWShipperTypeList = '<%=DPWShipperTypeList%>';
    var DPWShipperTypeListArr = DPWShipperTypeList ? DPWShipperTypeList.split("~~") : [];

</script>
</style>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/bkgroutingports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsdate%>"></script>
<%if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {%>
<script type="text/javascript" src="/Ecommaster/v1commodity.js?<%=jsno%>"></script>
<%}%>
<script type="text/javascript" src="/Ecommaster/dgdetail.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/equipment.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/packages.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/country.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/state.js?<%=jsno%>"></script>
<!--<script type="text/javascript" src="Design/app-assets/vendors/js/extensions/jquery.steps.min.js?<%=jsdate%>"></script>-->
<div class="card-header page_header  fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ebooking.head.bkgedit" bundle="<%=clientcode%>"/> <img class="back_btn" onclick="backtobookview()" src="Design/assets/images/page_icons/back_btn.svg" title="Back" /></div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body app-content">
        <html:form action="/amendmentbookingsave" styleId="AmendmentBookingFormBean" method="post" styleClass="form-horizontal icons-tab-steps wizard-circle" enctype="multipart/form-data">
            <%if (errors_new.length() > 0 && errors_new.equalsIgnoreCase("bookingerrors")) {%>
            <div class="alert alert-danger alert-dismissible">
                <html:errors></html:errors >
                </div>
            <%}%>
            <html:hidden property="ebook_conthdn" styleId="ebook_conthdn" />
            <html:hidden property="number" styleId="number" />
            <html:hidden property="hdntempaltename" styleId="hdntempaltename"  value=""/>
            <html:hidden property="ebook_masthdn" styleId="ebook_masthdn" />
            <html:hidden  styleId="ebook_pol" property="ebook_pol" />
            <html:hidden  styleId="ebook_pod"  property="ebook_pod" />
            <html:hidden property="hidden_empty" styleId="hidden_empty" />
            <html:hidden property="hidden_soc" styleId="hidden_soc" />
            <html:hidden property="ccMailId" styleId="ccMailId" />
            <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>
            <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
            <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
            <input type="hidden" name="typeheadhidden3" id="typeheadhidden3"/>
            <input type="hidden" name="typeheadhidden4" id="typeheadhidden4"/>
            <input type="hidden" name="typeheadhidden5" id="typeheadhidden5"/>
            <input type="hidden" name="typeheadhidden6" id="typeheadhidden6"/>
            <input type="hidden" name="typeheadhidden7" id="typeheadhidden7"/>
            <input type="hidden" name="typeheadhidden8" id="typeheadhidden8"/>
            <input type="hidden" name="typeheadhidden9" id="typeheadhidden9"/>
            <input type="hidden" name="typeheadhidden10" id="typeheadhidden10"/>
            <input type="hidden" name="typeheadhidden11" id="typeheadhidden11"/>
            <input type="hidden" name="typeheadhidden12" id="typeheadhidden12"/>
            <input type="hidden" name="typeheadhidden13" id="typeheadhidden13"/>
            <input type="hidden" name="typeheadhidden14" id="typeheadhidden14"/>
            <input type="hidden" name="typeheadhidden15" id="typeheadhidden15"/>
            <input type="hidden" name="typeheadhidden16" id="typeheadhidden16"/>
            <input type="hidden" name="typeheadhidden17" id="typeheadhidden17"/>
            <html:hidden property="ebook_origin" styleId="ebook_origin" />
            <html:hidden property="ebook_origin_desc" styleId="ebook_origin_desc" />
            <html:hidden property="ebook_delivery" styleId="ebook_delivery" />
            <html:hidden property="ebook_delivery_desc" styleId="ebook_delivery_desc"/>
            <html:hidden property="ebook_portofloading" styleId="ebook_portofloading" />
            <html:hidden property="ebook_portofdischarge" styleId="ebook_portofdischarge" />
            <html:hidden property="ebook_routeid" styleId="ebook_routeid"></html:hidden>
            <html:hidden property="documentalagencyid" styleId="ebook_routeid"></html:hidden>
            <html:hidden property="agencyissueid" styleId="ebook_routeid"></html:hidden>
            <html:hidden property="agencyid" styleId="ebook_routeid"></html:hidden>
            <html:hidden property="ebook_bookingcountry" styleId="ebook_bookingcountry" />
            <html:hidden property="ebook_bookingcode" styleId="ebook_bookingcode" ></html:hidden>
            <html:hidden property="ebook_shippercode" styleId="ebook_shippercode" ></html:hidden>
            <html:hidden property="ebook_shipper_id" styleId="ebook_shipper_id" ></html:hidden>
            <html:hidden property="ebook_shippercountry" styleId="ebook_shippercountry" />
            <html:hidden property="ebook_shippercity" styleId="ebook_shippercity" />
            <html:hidden property="ebook_agreecity" styleId="ebook_agreecity" />
            <html:hidden property="ebook_agreecountry" styleId="ebook_agreecountry" />
            <html:hidden property="ebook_agree_id" styleId="ebook_agree_id" ></html:hidden>
            <html:hidden property="ebook_agreecode" styleId="ebook_agreecode" ></html:hidden>
            <html:hidden property="ebook_shippercity_hdn" styleId="ebook_shippercity_hdn" />
            <html:hidden property="ebook_consigneecode" styleId="ebook_consigneecode" ></html:hidden>
            <html:hidden property="ebook_consigne_id" styleId="ebook_consigne_id" ></html:hidden>
            <html:hidden property="ebook_consigneecountry" styleId="ebook_consigneecountry" />
            <html:hidden property="ebook_consigneecity" styleId="ebook_consigneecity" />
            <html:hidden property="ebook_consigneecity_hdn" styleId="ebook_consigneecity_hdn" />
            <html:hidden property="ebook_forwardercode" styleId="ebook_forwardercode" ></html:hidden>
            <html:hidden property="ebook_forwarder_id" styleId="ebook_forwarder_id" ></html:hidden>
            <html:hidden property="ebook_forwardercountry" styleId="ebook_forwardercountry" />
            <html:hidden property="ebook_forwardercity" styleId="ebook_forwardercity" />
            <html:hidden property="ebook_forwardercity_hdn" styleId="ebook_forwardercity_hdn" />
            <html:hidden property="ebook_Warehousecode" styleId="ebook_Warehousecode" ></html:hidden>
            <html:hidden property="ebook_Warehouse_id" styleId="ebook_Warehouse_id" ></html:hidden>
            <html:hidden property="ebook_Warehousecountry" styleId="ebook_Warehousecountry" />
            <html:hidden property="ebook_Warehousecity" styleId="ebook_Warehousecity" />
            <html:hidden property="ebook_Warehousecity_hdn" styleId="ebook_Warehousecity_hdn" />
            <html:hidden property="ebook_NamedCustomercode" styleId="ebook_NamedCustomercode" ></html:hidden>
            <html:hidden property="ebook_NamedCustomer_id" styleId="ebook_NamedCustomer_id" ></html:hidden>
            <html:hidden property="ebook_NamedCustomercountry" styleId="ebook_NamedCustomercountry" />
            <html:hidden property="ebook_NamedCustomercity" styleId="ebook_NamedCustomercity" />
            <html:hidden property="ebook_NamedCustomercity_hdn" styleId="ebook_NamedCustomercity_hdn" />
            <html:hidden property="ebook_notifycode" styleId="ebook_notifycode" ></html:hidden>
            <html:hidden property="ebook_notify_id" styleId="ebook_notify_id" ></html:hidden>
            <html:hidden property="ebook_notifycountry" styleId="ebook_notifycountry" />
            <html:hidden property="ebook_notifycity" styleId="ebook_notifycity" />
            <html:hidden property="ebook_notifycity_hdn" styleId="ebook_notifycity_hdn" />
            <html:hidden property="ebook_notifycode_n1" styleId="ebook_notifycode_n1" ></html:hidden>
            <html:hidden property="ebook_notify_id_n1" styleId="ebook_notify_id_n1" ></html:hidden>
            <html:hidden property="ebook_notifycountry_n1" styleId="ebook_notifycountry_n1" />
            <html:hidden property="ebook_notifycity_n1" styleId="ebook_notifycity_n1" />
            <html:hidden property="ebook_notifycity_hdn_n1" styleId="ebook_notifycity_hdn_n1" />
            <html:hidden property="ebkg_type" styleId="ebkg_type" />
            <html:hidden property="cont_flag" styleId="cont_flag" />
            <html:hidden property="ebook_commhdn" styleId="ebook_commhdn" />
            <html:hidden property="submit_btn" styleId="submit_btn" />
            <html:hidden property="ebook_servicecode" styleId="ebook_servicecode" />
            <html:hidden property="ebooking_service" styleId="ebooking_service" />
            <html:hidden property="ebook_lineservicecode" styleId="ebook_lineservicecode" />
            <html:hidden property="ebook_lineservice" styleId="ebook_lineservice" />
            <html:hidden property="ebook_vesselcode" styleId="ebook_vesselcode" />
            <html:hidden property="ebooking_vessel" styleId="ebooking_vessel" />
            <html:hidden property="ebook_linevesselcode" styleId="ebook_linevesselcode" />
            <html:hidden property="ebook_linevessel" styleId="ebook_linevessel" />
            <html:hidden property="ebook_voyage" styleId="ebook_voyage" />
            <html:hidden property="ebook_linevoyage" styleId="ebook_linevoyage" />
            <html:hidden property="ebook_pol_etd" styleId="ebook_pol_etd" />
            <html:hidden property="ebook_pol_eta" styleId="ebook_pol_eta" />
            <html:hidden property="ebook_terminal" styleId="ebook_terminal" />
            <html:hidden property="ebook_terminal_country" styleId="ebook_terminal_country" />
            <html:hidden property="ebook_terminalpod" styleId="ebook_terminalpod" />
            <html:hidden property="ebook_terminalpod_country" styleId="ebook_terminalpod_country" />
            <html:hidden property="ebook_bound_leg" styleId="ebook_bound_leg" />
            <html:hidden property="ebook_linebound_leg" styleId="ebook_linebound_leg" />
            <html:hidden property="ebook_pol_hdn" styleId="ebook_pol_hdn" />
            <html:hidden property="ebook_pod_hdn" styleId="ebook_pod_hdn" />
            <html:hidden property="ebook_origin_hdn" styleId="ebook_origin_hdn" />
            <html:hidden property="ebook_delivery_hdn" styleId="ebook_delivery_hdn" />
            <html:hidden property="ebook_date_hdn" styleId="ebook_date_hdn" />
            <html:hidden property="groupcallid" styleId="groupcallid" />
            <html:hidden property="ebook_templatename" styleId="ebook_templatename" />
            <html:hidden property="routepolname" styleId="routepolname" />
            <html:hidden property="routepodname" styleId="routepodname" />
            <html:hidden property="routeoriginname" styleId="routeoriginname" />
            <html:hidden property="routedeliveryname" styleId="routedeliveryname" />
            <html:hidden property="routepolter" styleId="routepolter" />
            <html:hidden property="routepodter" styleId="routepodter" />
            <input type="hidden" name="cargoeditdate" id="cargoeditdate"/>
            <html:hidden property="shipperCheck" styleId="shipperCheck" />
            <html:hidden property="notifyCheck" styleId="notifyCheck" />
            <html:hidden property="notify1Check" styleId="notify1Check" />
            <html:hidden property="hidSHToOrder" styleId="hidSHToOrder" />
            <html:hidden property="hidCNToOrder" styleId="hidCNToOrder"/>
            <html:hidden property="hidNTToOrder" styleId="hidNTToOrder"/>
            <html:hidden property="hidNT1ToOrder" styleId="hidNT1ToOrder" />
            <html:hidden property="lrpBookNo" styleId="lrpBookNo" />
            <html:hidden property="hidETDDate" styleId="hidETDDate" />
            <input type="hidden" name="hidINVessel" id="hidINVessel" />
            <input type="hidden" name="hidINServiceCode" id="hidINServiceCode" />
            <html:hidden property="saveStatus" styleId="saveStatus" />
            <html:hidden property="hidPodIntransitName" styleId="hidPodIntransitName" />
            <html:hidden property="appVersion" styleId="appVersion" />
            <html:hidden property="hidbkgtype" styleId="hidbkgtype" />
            <html:hidden property="ebook_haulageDesc" styleId="ebook_haulageDesc" />
            <html:hidden property="deletedfilename" styleId="deletedfilename" />
            <html:hidden property="ebook_Sellercity" styleId="ebook_Sellercity" />
            <html:hidden property="ebook_Sellercity_hdn" styleId="ebook_Sellercity_hdn" />
            <html:hidden property="ebook_Sellercountry" styleId="ebook_Sellercountry" />
            <html:hidden property="ebook_Seller_id" styleId="ebook_Seller_id" />


            <html:hidden property="ebook_Buyercity" styleId="ebook_Buyercity" />
            <html:hidden property="ebook_Buyercity_hdn" styleId="ebook_Buyercity_hdn" />
            <html:hidden property="ebook_Buyercountry" styleId="ebook_Buyercountry" />
            <html:hidden property="ebook_Sellercode" styleId="ebook_Sellercode" />
            <html:hidden property="ebook_Buyercode" styleId="ebook_Buyercode" />
            <html:hidden property="ebook_Buyer_id" styleId="ebook_Buyer_id" />
            <html:hidden property="ebook_preferred_agency" styleId="ebook_preferred_agency" />
             <html:hidden property="ebook_sisubmit_party" styleId="ebook_sisubmit_party" />
             <input type="hidden" name="stepWizardNewIndex" id="stepWizardNewIndex"/>
            <input type="hidden" id="ebook_shippercountryhdn"/>
            <input type="hidden" id="ebook_consigneecountryhdn"/>
            <input type="hidden" id="ebook_notifycountryhdn"/>
            <input type="hidden" id="ebook_notify1countryhdn"/>
            <input type="hidden" id="ebook_agreecountryhdn"/>
            <input type="hidden" id="ebook_forwardercountryhdn"/>
            <input type="hidden" id="ebook_Warehousecountryhdn"/>
            <input type="hidden" id="ebook_NamedCustomercountryhdn"/>
            <input type="hidden" id="ebook_Sellercountryhdn"/>
            <input type="hidden" id="ebook_Buyercountryhdn"/>
            <input type="hidden" id="srchd"/>
            <input type="hidden" id="hidn1check"/>
            <input type="hidden" id="hidn2check"/>
            <input type="hidden" id="checkRoutePol"/>
            <input type="hidden" id="checkRoutePod"/>
            <input type="hidden" id="changeDateValue"/>
            <input type="hidden" id="appversionchange"/>
            <input type="hidden" id="OOG_Plugin" value="<%=is_ecom_OOG%>"/>
            <input type="hidden" id="euport" value="N"/>
            <input type="hidden" id="hidRoutePOL"/>
            <input type="hidden" id="hidRoutePOD"/>

            <h6><i class="step-icon fa_master"></i> <bean:message key="ebooking.masterdetails.display" bundle="<%=clientcode%>" /> </h6>
            <fieldset>  
                <div class="inner">
                    <div class="details_search txt_box_book"> <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4">
                                    <bean:message key='ebooking.origin.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                                    <html:text property="ebook_origin_country" styleId="ebook_origin_country"   maxlength="100" styleClass="form-control required atport_pla portsnotes" onblur="getPort('ebook_origin_country','ebook_origin_desc','ebook_origin',defOrgDesHaulValue);checkRoutePort('checkRoutePol','ebook_origin_country');getEmptyPickUp(this.value);toSetCarriageCntct();chkIsEuPort();enablePreferredAgency('');chkIsEuPortT2lFiling();setENSType();chkEU_ENSDeclaration();" onchange="toenableshipperref('ebook_origin_country','ebook_origin_desc');clearshipperref();" />
                                    <!--                                    <img class="pages_icon" src="Design/assets/images/page_icons/marker.svg"> -->
                                </div>
                                <html:hidden styleId="ebook_portofdischarge_desc"  property="ebook_portofdischarge_desc" />
                                <html:hidden styleId="ebook_portofloading_desc"  property="ebook_portofloading_desc" />
                                <div class="col-xs-12 col-sm-4">
                                    <bean:message key='ebooking.Delivery.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                                    <html:text property="ebook_delivery_country" styleId="ebook_delivery_country"  maxlength="100" styleClass="form-control required atport_pld setdata portsnotes" onblur="getPort('ebook_delivery_country','ebook_delivery_desc','ebook_delivery', defOrgDesHaulValue); checkRoutePort('checkRoutePod','ebook_delivery_country');getPODIntransit(this.value);toSetCarriageCntct();chkIsEuPort();chkIsEuPortT2lFiling();setENSType();chkEU_ENSDeclaration();" />
                                    <!--                                    <img class="pages_icon" src="Design/assets/images/page_icons/marker.svg">-->
                                </div>
                                <div class="col-xs-12 col-sm-4">
                                    <bean:message key="ebooking.date.display" bundle="<%=clientcode%>" /> <span class="mandatory">*</span>
                                    <html:text property="ebook_date"  styleClass="form-control required"  styleId="ebook_date" />
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4">
                                    <label><bean:message key="ebooking.Haulage.Origin" bundle="<%=clientcode%>"/></label>
                                    <br>
                                    <div class="btn-group booking_btn" id="preCarriage_btn">
                                        <button type="button" class="btn btn-primary car">Carrier</button>
                                        <button type="button" class="btn btn-primary mer">Merchant</button>

                                    </div>
                                    <html:hidden property="preCarriage" styleId="preCarriage"/>
                                </div>
                                <div class="col-xs-12 col-sm-4">

                                    <label><bean:message key="ebooking.Haulage.Destination" bundle="<%=clientcode%>"/></label>
                                    <br>
                                    <div class="btn-group booking_btn" id="onCarriage_btn">
                                        <button type="button" class="btn btn-primary car">Carrier</button>
                                        <button type="button" class="btn btn-primary mer">Merchant</button>

                                    </div>
                                    <html:hidden property="onCarriage" styleId="onCarriage"/>
                                </div>

                                <div class="col-xs-12 col-sm-4">
                                    <label><bean:message key='ebooking.carrier.contact' bundle="<%=clientcode%>"/></label>
                                    <html:select property="carrierContact" styleId="carrierContact" styleClass="form-control" >
                                        <html:options collection="CARRIERCONTACTS"  property="value" labelProperty="label"/>
                                    </html:select>

                                </div>

                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4">
                                    <label><bean:message key="ebooking.ref.no" bundle="<%=clientcode%>" /></label>
                                    <html:text property="booking_refno" styleId="booking_refno" styleClass="form-control setdata" readonly="true" />
                                </div>
                                <%if (!"Y".equalsIgnoreCase(Hide_AP_CUST_BKG_Master)) {%>
                                <div class="col-xs-12 col-sm-4">
                                    <label><bean:message key="ebooking.booking.party" bundle="<%=clientcode%>"/></label>
                                    <input type="text" name="bookingpartynew" id="bookingpartynew" class="form-control"  readonly/>
                                    <html:hidden property="ebook_party" styleId="ebook_party"/>
                                    <html:hidden property="ebook_party_desc" styleId="ebook_party_desc"/>
                                    <html:hidden styleId="ebook_bookingparty_desc"  property="ebook_bookingparty_desc"/>
                                </div>
                                <%}%>
                                
                                    <%if ("Y".equalsIgnoreCase(TENS)) {%> 
                                    <div class="col-xs-12 col-sm-3" >
                                    <label><bean:message key="ebooking.booking.ENSFiling" bundle="<%=clientcode%>"/></label>
                                    <div class="btn-group booking_btn" id="ensfilling_btn">
                                        <button type="button" class="btn btn-primary sng"><bean:message key="ebooking.booking.single" bundle="<%=clientcode%>"/></button>
                                        <button type="button" class="btn btn-primary prl"><bean:message key="ebooking.booking.partial" bundle="<%=clientcode%>"/></button>
                                        <button type="button" class="btn btn-primary ena"><bean:message key="ebooking.booking.na" bundle="<%=clientcode%>"/></button>
                                    </div>
                                    <html:hidden property="ensfiling" styleId="ensfiling"/>
                                     </div>
                                    <%}%>
                                    <%if ("Y".equalsIgnoreCase(Enable_T2LFiling)) {%>
                                    <div class="col-xs-12 col-sm-1 p-0" >
                                    <label><bean:message key="ebooking.booking.T2LFiling" bundle="<%=clientcode%>"/></label>
                                    <br>
                                    <label class="switch">
                                        <input type="checkbox" value="N" id="t2lfiling"  onchange="t2lfilingEnable(this)" >
                                        <span class="slider round"></span>
                                        <html:hidden property="ebook_t2lfiling" styleId="ebook_t2lfiling"/>
                                    </label>
                                     </div>
                                    <%}%>
                               
                                <%if ("Y".equalsIgnoreCase(Enable_preferAgency)) {%> 
                                <div class="col-xs-12 col-sm-4" >
                                    <label><bean:message key='ebooking.preferred.agency' bundle="<%=clientcode%>"/></label>
                                    <select id="preferred_agency" class="form-control" onchange="setPreferredAgency(this.value)" disabled>
                                    </select>
                                </div>
                                <%}%> 

                            </div>
                        </div>
                 <%if("Y".equalsIgnoreCase(TENS_Documentation)){%>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4">
                                    <label><bean:message key="ebooking.booking.shipmentType" bundle="<%=clientcode%>"/></label>
                                    <br>
                                    <div class="btn-group booking_btn" id="ensDocType_btn">
                                        <button type="button" class="btn btn-primary sbl">Straight B/L</button>
                                        <button type="button" class="btn btn-primary cnd">Consolidated</button>
                                    </div>
                                    <html:hidden property="ebook_EnsDocType" styleId="ebook_EnsDocType"/>
                                </div>
                                </div>
                                </div>
                                <%}%>
                        <%if (addInfoList != null && addInfoList.size() > 0) {%> 
                        <div class="form-group">
                            <div class="">

                                <div id="addi_details" style="cursor: pointer" data-toggle="collapse" data-target="#addiDetails"> <i class="fa fa-plus"></i> <bean:message key="ebooking.additional.info" bundle="<%=clientcode%>"/> </div>

                            </div>
                        </div>
                        <div id="addiDetails" class="collapse addi_details">
                            <div class="form-group">
                                <div class="row">
                                    <%for (int i = 0; i < addInfoList.size(); i++) {
                                            addInfoID = (String) addInfoList.get(i);
                                            if ("EOR".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key='ebooking.rate.reference' bundle="<%=clientcode%>"/></label>
                                        <html:hidden property="ebook_rate" styleId="ebook_rate"  />
                                        <html:hidden property="ebook_rate_desc" styleId="ebook_rate_desc"/>
                                        <html:text property="ebook_rate_reference" styleId="ebook_rate_reference" styleClass="form-control setdata" maxlength="20" />
                                    </div>   
                                    <%}
                                        if ("AGR".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key='ebooking.agency.reference' bundle="<%=clientcode%>"/></label>
                                        <html:text property="ebook_agency_reference" styleId="ebook_agency_reference" styleClass="form-control setdata" maxlength="<%=agencyRefNoLength%>" />

                                    </div>
                                    <%}
                                        if ("EOF".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key='ebooking.ocean.freight' bundle="<%=clientcode%>"/></label>
                                        <html:select property="ebook_ocean_freight" styleId="ebook_ocean_freight" styleClass="form-control" >
                                            <html:option value="P">Prepaid</html:option>
                                            <html:option value="C">Collect </html:option>
                                        </html:select>
                                    </div>
                                    <%}
                                        if ("PFR".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <%if ("ESL".equalsIgnoreCase(carrierCode)) {%>
                                        <label><bean:message key="ebooking.place.final.receipt.ch" bundle="<%=clientcode%>" /></label>
                                        <%} else {%>
                                        <label><bean:message key="ebooking.place.final.receipt" bundle="<%=clientcode%>" /></label>
                                        <%}%>
                                        <html:select property="podintransit" styleId="podintransit" styleClass="form-control" >
                                            <html:option value="">Select Place of Final Receipt</html:option>
                                        </html:select>
                                        <html:hidden styleId="hidpodintransit" property="hidpodintransit"/>
                                    </div>

                                    <%}
                                        if ("NAT".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key='ebooking.booking.natcode' bundle="<%=clientcode%>"/></label>
                                        <html:text property="ebook_natcode" styleId="ebook_natcode" styleClass="form-control setdata" maxlength="11" />
                                    </div>
                                    <%}
                                        if ("ETH".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.haulage.type" bundle="<%=clientcode%>" /></label>
                                        <html:select property="ebook_haulagetype" styleId="ebook_haulagetype" styleClass="form-control">
                                            <html:option value="">Select Haulage Type</html:option>
                                            <html:option value="L">Live Load</html:option>
                                            <html:option value="D">Drop Only</html:option>
                                            <html:option value="P">Pickup only</html:option>
                                            <html:option value="B">Pickup & Drop</html:option>
                                        </html:select>
                                    </div>
                                    <%}
                                        if ("ELD".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.pickup.date" bundle="<%=clientcode%>" /></label>
                                        <html:text property="ebook_pickup_date"  styleClass="form-control required"  styleId="ebook_pickup_date" disabled="true"/>
                                    </div>
                                    <%}
                                        if ("EDD".equalsIgnoreCase(addInfoID)) {%>  
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.drop.date" bundle="<%=clientcode%>" /></label>
                                        <html:text property="ebook_drop_date"  styleClass="form-control required"  styleId="ebook_drop_date" disabled="true"/>
                                        <span id="ebook_drop_date_ero" class="help-block" style="display:none">Drop date should be greater than pickup date</span>

                                    </div>
                                    <%}
                                        if ("HAU".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.hauler.code" bundle="<%=clientcode%>" /></label>
                                        <html:text property="ebook_haulercode" styleId="ebook_haulercode" styleClass="form-control setdata" maxlength="20" />
                                    </div>
                                    <%}
                                        if ("EPO".equalsIgnoreCase(addInfoID)) {%>
                                  <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.customer.PO" bundle="<%=clientcode%>" /></label>
                                        <html:text property="ebook_custpo" styleId="ebook_custpo" styleClass="form-control setdata" maxlength="200" />
                                    </div>
                                    <%}
                                        if ("ERT".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key='ebooking.ref.type' bundle="<%=clientcode%>"/></label>
                                        <html:select property="ebook_reftype" styleId="ebook_reftype" styleClass="form-control" >
                                            <html:option value="Normal">Normal</html:option>
                                            <html:option value="Express">Express</html:option>
                                        </html:select>
                                    </div>
                                    <%}
                                        if ("ERF".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.exp.ref" bundle="<%=clientcode%>" /></label>
                                        <html:text property="ebook_exportref" styleId="ebook_exportref" styleClass="form-control setdata" maxlength="50" />
                                    </div>
                                    <%}
                                        if ("EOP".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.empty.pickup" bundle="<%=clientcode%>"/></label>
                                        <html:select property="ebook_pickup_point" styleId="ebook_pickup_point" styleClass="form-control" onchange="getEmptyPickUppoint('ebook_origin_country');">
                                            <html:option value="T">Terminal </html:option>
                                            <html:option value="D">Depot</html:option>
                                        </html:select>
                                    </div>
                                    <%}
                                        if ("EPP".equalsIgnoreCase(addInfoID)) {%>  
                                    <div class="col-xs-12 col-sm-3">
                                        <label></label>
                                        <html:select property="emptyPickup" styleId="emptyPickup" styleClass="form-control" onchange="onChangeEmptyPickup(this.value);">
                                            <html:option value="">Select Empty Pick Up</html:option>
                                        </html:select>
                                        <html:hidden styleId="emptyPickupHid" property="emptyPickupHid"/>
                                    </div>
                                    <%}
                                        if ("EPD".equalsIgnoreCase(addInfoID)) {%>       
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.empty.pickup.date" bundle="<%=clientcode%>"/></label>
                                        <html:text property="emptyPickUpDate"  styleClass="form-control"  styleId="emptyPickUpDate" disabled="true"/>
                                        <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    </div>
                                    <%}
                                        if ("CRN".equalsIgnoreCase(addInfoID)) {%>      
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.customer.reference" bundle="<%=clientcode%>"/></label>
                                        <html:text property="customerreference"  styleClass="form-control"  styleId="customerreference" maxlength="50" />
                                    </div>
                                    <%}%>
                                    <% if ("ACD".equalsIgnoreCase(addInfoID)) {%>
                                    <div class="col-xs-12 col-sm-3">
                                        <label><bean:message key="ebooking.book.ACID" bundle="<%=clientcode%>"/></label>
                                        <html:text property="ACID"  styleClass="form-control"  styleId="ACID" maxlength="20" />
                                    </div>
                                    <% } if ("DPT".equalsIgnoreCase(addInfoID)) {%>
                                        <div class="col-xs-12 col-sm-3" >
                                            <label id="DPWShipperType"><bean:message key="ebooking.book.DPWshipperType" bundle="<%=clientcode%>"/></label>
                                            <html:select property="shipperReferenceType" styleId="shipperReferenceType" styleClass="form-control " >
                                                <% if (DPWShipperTypeListArr != null) {
                                                       for (String shipperType : DPWShipperTypeListArr) { 
                                                           String[] parts = shipperType.split("-");
                                                           String value = parts.length > 0 ? parts[0].trim() : "";
                                                %>
                                                    <html:option value="<%= value %>"><%= shipperType %></html:option>
                                                <%     }
                                                   } 
                                                %>
                                            </html:select>
                                       
                                        </div>
                                    <% } if ("DPW".equalsIgnoreCase(addInfoID)) {%>
                                       
                                    <div class="col-xs-12 col-sm-3">
                                        <label id="DPWShipperRef"><bean:message key="ebooking.book.DPWshipperCode" bundle="<%=clientcode%>" /></label>
                                        <html:text property="shipperReference"   styleClass="form-control"  styleId="shipperReference" maxlength="45" disabled="true" />
                                    </div>
                                    <% }%>
                                    <%   if ((i + 1) % 4 == 0 && i != addInfoList.size() - 1) {%>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row">
                                    <%}
                                        }%>
                                </div>
                            </div>
                        </div>

                        <%}%>

                        <!-- Change Route Deatils start  -->
                        <input type="hidden"  id="selectroute" name="selectroute" title="Select Route" />
                        <html:hidden property="trpservice" styleId="trpservice" value=""/>
                        <html:hidden property="servicehd" styleId="servicehd" value=""/>
                        <html:hidden property="servicehdedit" styleId="servicehdedit"/>
                        <%

                            if (routeDetails != null && routeDetails.size() > 0) {
                                String INServiceCode = "";
                        %>
                        <div class="col-sm-12" id="routingdetails">
                            <div class="header_tle"> <bean:message key="ecom.bkg.rutdet" bundle="<%=clientcode%>"/>  </div>
                            <a onclick ="getRouteDetails()" id="routehref" href="javascript:void(0)" class="btn btn-warning text-white"><bean:message key="ecom.bkg.changerut" bundle="<%=clientcode%>"/> </a>
                            <%
                                int transittime = 0, transpcount = 0;
                                boolean isInlandMve = false;
                                String vesselfeeder = null, polName = null, podName = null, voyage = null, etd = null, eta = null, bound = null, str_type = "N", strServiceCode = null, vessel = null, maskport_trans1orpod = "N", pol = null, pod = null;
                               
                                boolean ismaskporthdn = false;
                                for (int i = 0; i < routeDetails.size(); i++) {
                                    Route list = (Route) routeDetails.get(i);
                                    if ("N".equalsIgnoreCase(maskport_trans1orpod) && "Y".equalsIgnoreCase(str_type)) {
                                        maskport_trans1orpod = list.getLoadport();
                                    }
                                    if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(list.getLegType()) || "F".equalsIgnoreCase(list.getLegType()))) {
                                        polName = list.getPolPortName();
                                        voyage = list.getVoyage();
                                        bound = list.getBound();
                                        etd = list.getETD1();
                                        str_type = "Y";
                                        vessel = list.getVessel();
                                        INServiceCode = list.getService();
                                        pol = list.getLoadport();
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
                                String polMportName = MaskPortUtil.getMportNameByActualPort(pol);
                                String displayPolName = (polMportName != null && !polMportName.isEmpty()) ? polMportName : polName;
                                String podMportName = MaskPortUtil.getMportNameByActualPort(pod);
                                String displayPodName = (podMportName != null && !podMportName.isEmpty()) ? podMportName : podName;
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
                                                        <dd><%=ismaskporthdn ? "": displayPolName%></dd>
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
                                                        <dd><%=ismaskporthdn ? "": displayPodName%></dd>
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
                                                        <dd> <bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></dd>
                                                        
                                                         <%if (!ismaskporthdn) {%>
                                                        <dd><strong id="BKG_vessel_value"><%=vesselfeeder%></strong></dd>
                                                        <dd><%=voyage%><%=bound%></dd>
                                                        <%}%>
														  					
                                                       
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
                                                <div id="show_details" style="cursor: pointer" class="show_details_class_book" onclick="toggleText('<%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>)', this, '#bkgshowDetails');"><%= isInlandMve ? "Multimodal Shipment" : "Transshipment"%> (<%=transpcount - 1%>) <i class="fa fa-chevron-down"></i></div>       
                                                    <%} else {%>
                                                <div id="show_details" class="show_details_class_book" <%if (isInlandMve) {%> style="cursor: pointer" onclick="toggleText('Multimodal Shipment ', this, '#bkgshowDetails')" <%}%>><%= isInlandMve ? "Multimodal Shipment<i class='fa fa-chevron-down'></i>" : "Direct Shipment"%></div>
                                                <%}%>
                                            </div> 
                                            <div id="bkgshowDetails" class="collapse show_details">
                                                <div class="timeline">
                                                    <%

                                                        String legType = "";
                                                        ismaskporthdn = false;
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
                                                            System.out.println("[ECOM-WAR] AmendmentBooking legType--> " + legType);
                                                           
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
                                                                   
                                                                    <dd><strong><%=ismaskporthdn ?"": vesselfeeder%></strong></dd>
                                                                   
                                                                   
                                                                    </dt>
                                                                    </td>
                                                                    <%if ("Vessel".equalsIgnoreCase(legType)) {%>
																	<%if(!ismaskporthdn){%>	
                                                                    <td>
                                                                    <dt>
                                                                    <dd><bean:message key="tracking.Service" bundle="<%=clientcode%>"/></dd>
                                                                    <dd><strong><%=dto.getServiceName()%></strong></dd>
                                                                    </dt>
                                                                    </td>
                                                                    <td>
                                                                    <dt>
                                                                    <dd><bean:message key="ecom.shippingins.VoyageorBound" bundle="<%=clientcode%>"/></dd>
                                                                    <dd><strong><%=dto.getVoyage()%><%=dto.getBound()%></strong></dd>
                                                                    </dt>
                                                                    </td>
                                                                    <%}%>
                                                                   <%}%> 
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
                                                                <span class="port_name"><%=MaskPortUtil.getDisplayPortName(dto.getDisport(), dto.getPodPortName())%></span>
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
                                boolean isbkgcutoff = (boolean) request.getAttribute("ISBKGCUTOFF");
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
                            <%} else if (cutOffVO != null) {%>
                            <div class="">
                                <table class="table table-striped table-bordered">
                                    <tbody>
                                        <tr>
                                            <td><bean:message key='ebooking.prev.docclosing' bundle="<%=clientcode%>"/></td>
                                            <td><%=cutOffVO.getDocClosing()%></td>
                                            <td><bean:message key='ebooking.prev.cfcclosing' bundle="<%=clientcode%>"/></td>
                                            <td><%=cutOffVO.getFullContClosing()%></td>
                                        </tr>
                                        <tr>
                                            <td><bean:message key='ebooking.prev.vgmclosing' bundle="<%=clientcode%>"/></td>
                                            <td><%=cutOffVO.getVgmClosing()%></td>
                                            <td><bean:message key='ebooking.prev.cfsclosing' bundle="<%=clientcode%>"/></td>
                                            <td><%=cutOffVO.getCfsGateClosing()%></td>
                                        </tr>
                                        <tr>
                                            <td><bean:message key='ebooking.prev.edidecclosing' bundle="<%=clientcode%>"/></td>
                                            <td><%=cutOffVO.getEdiDecClosing()%></td>
                                            <td><bean:message key='ebooking.prev.deptterminal' bundle="<%=clientcode%>"/></td>
                                            <td><%=ismaskporthdn ?"": Validation.checkNull(temrminalMap.get(cutOffVO.getTerminal()))%></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <%}%>
                        </div>
                        <script type="text/javascript">
                            $(document).ready(function () {
                                $('#hidINVessel').val('<%=vessel%>');
                                $('#hidINServiceCode').val('<%=INServiceCode%>');
                                $('#hidRoutePOL').val('<%=pol%>');
                                $('#hidRoutePOD').val('<%=pod%>');
                            });
                              <%if ("Y".equalsIgnoreCase(Enable_preferAgency)) {%>
                              enablePreferredAgency('previous');
                              <%}%>
                        </script>
                        <%} else {%>
                        <div class="col-sm-12" id="routingdetails"></div>
                        <%}%>

                        <!-- Change Route Deatils End  --></div>
                </div>
            </fieldset>
            <jsp:include page="CommonCustDetails.jsp"/>

            <h6><i class="step-icon fa_cargo"></i>  <bean:message key="shipping.cargo.head" bundle="<%=clientcode%>" /> </h6>

            <fieldset>
                <div class="form-group alert-error" id="bookingcargoerror" style="display:none">
                    <div class="alert alert-danger alert-dismissible" role="alert">
                        <strong><bean:message key="ecom.msg.warning" bundle="<%=clientcode%>"/></strong><bean:message key="Booking.msg.cargo" bundle="<%=clientcode%>" /> </div>
                </div>
                <%if (exceluploadpath != null && exceluploadpath.length() > 0) {%>
                <div class="form-group row">
                    <div class="col-sm-12">
                        <span class="pull-right"> <a href="javascript:void(0)" onclick="ExportData()" title="Download Template" class="btn btn-info btn-sm si_imp_exp_excel" id="exportsearch"><i class="fa fa-download"></i>  <%if ("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) {%> Download Excel <%} else {%> <bean:message key="ebooking.cargo.expexcel" bundle="<%=clientcode%>"/> <%}%> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:void(0)"  data-toggle="modal" data-target="#myImportModal" title="Upload Template" class="btn btn-info btn-sm si_imp_exp_excel"   id="importsearch1"><i class="fa fa-upload"></i> <%if ("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) {%> Upload Excel <%} else {%> <bean:message key="ebooking.cargo.impexcel" bundle="<%=clientcode%>"/> <%}%> </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    </div>
                </div>
                <%}%>
                <div id="datatablebycargodiv">

                </div>
                <div class="form-group overflow-hidden">
                    <div class="col-12 col-sm-12  ">
                        <a  href='javascript:void(0)' onClick="addamendmentcargo()" class="btn new_add float-right">
                            <i class="fa fa-plus" > </i> <bean:message key="ecom.bkg.but.add" bundle="<%=clientcode%>"/>
                        </a>
                    </div>
                </div>
            </fieldset>
            <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
            <h6><i class="step-icon fa fa_haulage"></i>   <bean:message key="ecom.bkg.haulagedet" bundle="<%=clientcode%>"/> </h6>  
            <fieldset>
                <div class="haulagetabdiv">
                    <div class="col-sm-12">
                        <input type="hidden" id="haulagedetailsjsonstr" name="haulagedetailsjsonstr">
                        <div class="form-group row">

                            <div class="col-sm-4">
                                <label><bean:message key="ecom.bkg.eqpid" bundle="<%=clientcode%>"/>  <span class="mandatory">*</span> </label>
                                <select id="hauleqprowid" name="hauleqprowid" class="form-control light_border required" onchange="setEqpType(this.value, 'conno');getRowIdFromConatainer(this);">
                                </select>
                                <input type="hidden" id="hiddenhaulagerowid" name="hiddenhaulagerowid">
                            </div>


                            <div class="col-sm-4">
                                <label><bean:message key="ecom.bkg.eqptype" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                                <input type="text" id="hauleqptype" name="hauleqptype" class="form-control light_border required" readonly>
                            </div>


                            <div class="col-sm-4">
                                <label> <bean:message key="ecom.bkg.pickuploc" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> 
                                    <div class="checkbox-inline" style="">
                                        <label style="margin-bottom:0px;padding-left: 15px;">
                                            <input type="checkbox" id="haulageShipper" name="haulageShipper" onclick="setShipperDetailsToHaulage();" value="1" style="margin-top:0.5%">
                                            <bean:message key="haul.same.ship" bundle="<%=clientcode%>"/> </label>

                                        <label class="p-l-15">
                                            <a onclick="shownewhaulmodal();" id="pickuplocnew" href="javascript:void(0)" data-toggle="tooltip" title="" data-placement="right" class="btn btn-warning btn-xs new_btn_si" data-original-title="Edit">
                                                <i class="fa fa-pencil-square" id="ccupdateimg"></i>  <bean:message key="haul.but.new" bundle="<%=clientcode%>"/></a>
                                        </label>
                                    </div>
                                </label>
                                <div class="input-group">
                                    <input type="hidden" id="haulpickuploccode" name="haulpickuploccode" class="form-control light_border required" onchange="setContactdetails()">
                                    <input type="text" id="haulpickuplocname" name="haulpickuplocname" class="form-control light_border required" readonly>
                                    <div class="input-group-addon group_search">
                                        <span class="input-group-text" onclick="getPickUpLocDetails('<%=splcustgrp%>')"  >
                                            <i class="fa fa-search"></i>
                                        </span>
                                    </div> 
                                </div>
                            </div>

                        </div>

                        <div class="form-group row">
                            <div class="col-sm-4">
                                <label><bean:message key="ecom.bkg.refno" bundle="<%=clientcode%>"/></label>
                                <input type="text" id="haulrefno" name="haulrefno" class="form-control light_border" maxlength="50">
                            </div>
                            <div class="col-sm-2">
                                <label><bean:message key="ecom.bkg.dropdate" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                                <input type="text" id="hauldropdate" name="hauldropdate" class="form-control light_border required"> 
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>  
                            </div>
                            <div class="col-sm-2">   
                                <label>Time </label>
                                <input type="text" class="form-control bs-timepicker light_border timeformatvalidation required" id="hauldropdatetime" value="00:00" name="hauldropdatetime">
                            </div>    
                            <div class="col-sm-4">
                                <label><bean:message key="Reg.Address" bundle="<%=clientcode%>"/> </label>
                                <textarea id="hauladdress" name="hauladdress" class="form-control light_border" readonly> </textarea>
                            </div>

                        </div>
                        <div class="form-group row">

                            <div class="col-sm-4">
                                <label><bean:message key="ecom.bkg.stopseq" bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                <input type="number" id="haulstopseq" name="haulstopseq" min="1" max="99" class="form-control light_border required numbervalpoint number">
                            </div>

                            <div class="col-sm-4">
                                <label> <bean:message key="ecom.bkg.contctper" bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                                <div class="input-group">
                                    <input type="text" id="haulcontactperson" name="haulcontactperson" class="form-control light_border required" readonly> 
                                    <div class="input-group-addon group_search">
                                        <span class="input-group-text" onclick='getContactDetails()' >
                                            <i class="fa fa-search"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>


                            <div class="col-sm-4">
                                <label> <bean:message key="ecom.bkg.contctphone" bundle="<%=clientcode%>"/> </label>
                                <input type="text" id="haulcontactphone" name="haulcontactphone" class="form-control light_border " readonly>
                            </div>

                        </div>

                        <div class="form-group row">

                            <div class="col-sm-4">
                                <label><bean:message key="ecom.bkg.contctmail" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                <input type="email" id="haulcontactemail" name="haulcontactemail" class="form-control light_border email required" readonly> 
                            </div>


                            <div class="col-sm-4">
                                <label><bean:message key="ebooking.remarks.display" bundle="<%=clientcode%>"/></label>
                                <textarea id="haulremarks" name="haulremarks" class="form-control light_border"> </textarea>
                            </div>

                            <div class="col-sm-4 mt-4">
                                <label for="haultwrsoverweight" class="label_switch"><bean:message key="ecom.bkg.twrsovrwt" bundle="<%=clientcode%>"/> </label>
                                <label class="switch">
                                    <input type="checkbox" id="haultwrsoverweight" name="haultwrsoverweight" class='bigcheckbox' > 
                                    <span class="slider round"></span>
                                </label>   
                            </div>


                        </div>
                        <div class="form-group row">

                            <div class="col-sm-3">
                                <input type="hidden" id="hauloprtype" name="hauloprtype" value="add">
                                <input type="hidden" id="haulbookingtype" name="haulbookingtype" value="amnd">
                                <button  type="button" class="btn new_add btn-sm"  onclick="addHaulage('AmendmentBookingFormBean')" id="haulsubmitID"><i class="fa fa-plus"></i> <bean:message key="ecom.bkg.but.add" bundle="<%=clientcode%>"/></button>
                                <button type="button" class="btn reset_class btn-sm" id="haulresetID" onclick="resetHaulage()"> <span class="fa fa-undo" ></span> <bean:message key="ecom.bkg.but.Reset" bundle="<%=clientcode%>"/></button>
                            </div>
                            <div class="col-sm-6 m-t-20" style="margin-left: -60px !important;">
                                <label class="text-info" style="font-size: 16px !important;"> <bean:message key="note.add.hauldet" bundle="<%=clientcode%>"/></label> 
                            </div>
                        </div>
                        <div class="col-lg-12">
                            <span class="lbl_clr_txt"> <bean:message key="point1.haudet.time" bundle="<%=clientcode%>"/>
                                <br>
                                <bean:message key="point2.haudet.time" bundle="<%=clientcode%>"/>
                                <br>
                                <bean:message key="point3.haudet.time" bundle="<%=clientcode%>"/>
                            </span> 
                        </div>
                        <hr>
                    </div>
                </div>
                <div class="col-sm-12">
                    <div class="table-responsive" id="datatablebyhaulagediv">
                        <table class="table table-striped table-bordered" id="datatablebyhaulage">
                            <thead>
                                <tr>
                                    <th title="action"><bean:message key="Tracking.action" bundle="<%=clientcode%>"/></th>
                                    <th title="eqpid" ><bean:message key="ecom.bkg.eqpid" bundle="<%=clientcode%>"/></th>
                                    <th title="eqptype" ><bean:message key="ecom.bkg.eqptype" bundle="<%=clientcode%>"/></th>
                                    <th title="pickloccode" class='d-none' > <bean:message key="ecom.bkg.pickuploc" bundle="<%=clientcode%>"/></th>
                                    <th title="picklocname" ><bean:message key="ecom.bkg.pickuploccode" bundle="<%=clientcode%>"/></th>
                                    <th title="address" class='' ><bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/></th>
                                    <th title="refno" class='' ><bean:message key="ecom.bkg.refno" bundle="<%=clientcode%>"/></th>
                                    <th title="dropdate" ><bean:message key="ecom.bkg.dropdate" bundle="<%=clientcode%>"/></th >
                                    <th title="stopsequence" ><bean:message key="ecom.bkg.stopseq" bundle="<%=clientcode%>"/></th>
                                    <th title="contactperson" > <bean:message key="ecom.bkg.contctper" bundle="<%=clientcode%>"/></th>
                                    <th title="contactphone" ><bean:message key="ecom.bkg.contctphone" bundle="<%=clientcode%>"/></th>
                                    <th title="contactemail" class='' ><bean:message key="ecom.bkg.contctmail" bundle="<%=clientcode%>"/></th>
                                    <th title="twrsoverweight" ><bean:message key="ecom.bkg.tmsovrwt" bundle="<%=clientcode%>"/></th>
                                    <th title="remarks" ><bean:message key="ebooking.remarks.display" bundle="<%=clientcode%>"/></th>
                                    <th title="eqprowid"  ><bean:message key="ecom.bkg.eqpseqno" bundle="<%=clientcode%>"/></th>

                                </tr>
                            </thead>
                            <tbody>
                                <%if (haulagelist != null && haulagelist.size() > 0) {
                                        BookingHaulagedetailsVo haulvo = null;
                                        for (int i = 0; i < haulagelist.size(); i++) {
                                            haulvo = haulagelist.get(i);
                                %>
                                <tr>
                                    <td>
                                        <a href='#' onclick='editHaulage(this)' class='btn btn-lg text-warning p-0'><i class='fa fa-edit'></i></a> 
                                        <a href='javascript:void(0)' onclick='deleteHaulage(this)' class='btn btn-lg text-danger p-0'><i class='fa fa-trash-o'></i></a>
                                    </td>
                                    <td class="eqpid"><%=Validation.checkNull(haulvo.getEqpid())%></td>
                                    <td class="eqptype"><%=Validation.checkNull(haulvo.getEqptype())%></td>
                                    <td class='d-none' ><%=Validation.checkNull(haulvo.getPickloccode())%></td>
                                    <td><%=Validation.checkNull(haulvo.getPicklocname())%></td>
                                    <td class='' ><%=Validation.checkNull(haulvo.getAddress())%></td>
                                    <td class='' ><%=Validation.checkNull(haulvo.getRefno())%></td>
                                    <td><%=Validation.checkNull(haulvo.getDropdate())%></td>
                                    <td class="stopseq"><%=haulvo.getStopsequence()%></td>
                                    <td><%=Validation.checkNull(haulvo.getContactperson())%></td>
                                    <td><%=Validation.checkNull(haulvo.getContactphone())%></td>
                                    <td class='' ><%=Validation.checkNull(haulvo.getContactemail())%></td>
                                    <td><%=Validation.checkNull(haulvo.getTwrsoverweight())%></td>
                                    <td><%=Validation.checkNull(haulvo.getRemarks())%></td>
                                    <td  class="eqprowid"><%=haulvo.getEqprowid()%></td>

                                </tr>
                                <%     }
                                    }%>
                            </tbody>
                        </table>
                    </div>
                </div>
            </fieldset> 
            <%}%>
           
            <jsp:include page="CommonBKGInsurance.jsp"/>
            <jsp:include page="../Common/Cargo_Protect.jsp"/>
            <jsp:include page="CommonENS.jsp" />
            <jsp:include page="CommonBKGFileUpload.jsp"/>

            <jsp:include page="CommonBKGPreview.jsp"/>

        </html:form>
    </div>
</div>

<div class="modal fade" id="myModalSearch" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header bg-info text-white"> <h5 class="modal-title"><bean:message key="ebooking.cust.custsearch" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="myImportModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white"> <h5 class="modal-title"><bean:message key="ecom.bkg.importexcel" bundle="<%=clientcode%>"/> </h5>
                        <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close" onclick="closeModel();"><i class="fa fa-times-circle"></i></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <div class="col-sm-offset-0 col-sm-11">
                                <div class="custom-file">
                                    <input type="file" name="importexcel" id="importexcel" class='custom-file-input'>
                                    <label class="custom-file-label" for="">  <bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                                <!--                                <span class="text-left">  <input type="file" name="importexcel" id="importexcel" > &nbsp;&nbsp;&nbsp;</span>-->
                            </div>
                            <div class="col-sm-offset-0 col-sm-11">
                                <span class="text-left"> <label class="error" style="color:red"><bean:message key="ecom.bkg.xlfileformat" bundle="<%=clientcode%>"/> </label> </span>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="text-right">
                            <a href="javascript:void(0)" class="btn btn-info btn-xs" onclick="ImportData()" id="importsearch"><i class="fa fa-upload"></i> <%if ("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) {%> Upload  Excel <%} else {%><bean:message key="ebooking.cargo.impexcel" bundle="<%=clientcode%>"/><%}%> </a>
                            <a href="javascript:void(0)"  data-dismiss="modal" class="btn btn-info btn-xs" title="Close" data-placement="right" onclick="closeModel();"><i class="fa fa-times-circle"></i> <bean:message key="ecom.close" bundle="<%=clientcode%>"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="myModalImportErrors" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white"> <h5 class="modal-title"> <bean:message key="ecom.bkg.importexcel" bundle="<%=clientcode%>"/> </h5>
                <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close" onclick="closeErrorModel();"><i class="fa fa-times-circle"></i></button>
            </div>
            <div class="modal-body">
                <div class="col-sm-12" id="importerr" style="height: 450px;overflow: auto;"></div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white "> <h5 class="modal-title">  <bean:message key="ebooking.template.display" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <form id="savetemplateform" action="" method="post" class="form-horizontal">
                    <div class="form-group row">
                        <label class="col-sm-3 control-label"><bean:message key="ebooking.tempname.display" bundle="<%=clientcode%>"/></label>
                        <div class="col-sm-5">
                            <input  type="text" id="tmponame"  name="tmponame"  maxlength="20" class="form-control light_border" onkeypress="return stopRKey(event)"/>
                        </div>
                    </div>
                    <div class="text-right">
                        <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                        <button type="button" class="btn btn-primary" title="Access denied" disabled><bean:message key="ecom.but.savechanges" bundle="<%=clientcode%>"/></button>
                        <%} else {%>
                        <button type="button" class="btn btn-primary" onclick="saveTemplate_reuse('save')" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn"><bean:message key="ecom.but.savechanges" bundle="<%=clientcode%>"/></button>
                        <%}%>
                        <button type="button" class="btn btn-default" data-dismiss="modal"> <bean:message key="ecom.close" bundle="<%=clientcode%>"/></button>
                    </div>
                </form>
            </div>
        </div>

    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="comdtySearchModal" role="dialog">
    <div class="modal-dialog modal-xl" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"> <bean:message key="ebooking.cargo.commsearch" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="myModalImportMsg" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white"> <bean:message key="ecom.bkg.xcelupdate" bundle="<%=clientcode%>"/>
                <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close" onclick="closeMsgModel();"><i class="fa fa-times-circle"></i></button>
            </div>
            <div class="modal-body">
                <div class="col-sm-offset-0 col-sm-11" id="importmsg" style="height: 450px;overflow: auto;"></div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="bkgccmailmodal" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white"><h5 class="modal-title"> <bean:message key="ebooking.msg.sendccmail" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <form id="ccmailidform" action="" method="post" class="form-horizontal">

                    <div class="form-group row">
                        <label class="col-sm-9 control-label" style="text-align: left;"><bean:message key="ebooking.msg.ccmailid" bundle="<%=clientcode%>"/> :  <span class="label label-warning text-white"><bean:message key="ebooking.msg.commasepmutiplemail" bundle="<%=clientcode%>"/></span></label>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-10">
                            <textarea class="form-control" id="ccmailidmodal" name="ccmailidmodal" rows="3"  maxlength="200" cols="50" style="resize:none"></textarea>

                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-9 control-label" style="text-align: left;color: #337ab7;"><bean:message key="ebooking.msg.updatemailid" bundle="<%=clientcode%>"/></label>
                    </div>
                </form>
                <br/>
                <div class="text-right">
                    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    <button type="button" class="btn btn-primary" title="Access denied"><bean:message key="ecom.but.sendtoagency" bundle="<%=clientcode%>"/></button>
                    <%} else {%>
                    <button type="button" class="btn btn-primary" onclick="submitBooking();" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn"><bean:message key="ecom.but.sendtoagency" bundle="<%=clientcode%>"/></button>
                    <%}%>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="ecom.close" bundle="<%=clientcode%>"/></button>
                </div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="ccalertmoal" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title">  <bean:message key="ecom.but.sendtoagency" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <form id="ccmailidform" action="" method="post" class="form-horizontal">
                    <div class="form-group row">
                        <label class="col-sm-6 control-label" style="text-align: left;"><bean:message key="ecom.msg.mailtocolleague" bundle="<%=clientcode%>"/></label>
                    </div>

                </form>
                <div class="text-right">
                    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    <button type="button" class="btn btn-primary" title="Access denied">Yes</button>
                    <%} else {%>
                    <button type="button" class="btn btn-primary" onclick="submitwithcc();" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn">Yes</button>
                    <%}%>
                    <button type="button" class="btn btn-default" onclick="submitwithoutcc();">No</button>
                </div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!--new customer modal-->
<div class="modal fade" id="newhaulagemodal" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"> <bean:message key="ecom.shippingins.newcust" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="newhaulagepickuploc" role="form" name="newhaulagepickuploc"  autocomplete="off" method="post">
                    <div class="form-group row">
                        <label class="col-sm-2 control-label"> <bean:message key="ecom.bkg.pickuploc" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border required" name="newhaulpickuploc" id="newhaulpickuploc" maxlength="100"/>
                        </div>
                        <label class="col-sm-2 control-label"> <bean:message key="ecom.bkg.contctper" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border required" name="newhaulcontperson" id="newhaulcontperson" maxlength="200"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label"> <bean:message key="ecom.bkg.contctphone" bundle="<%=clientcode%>"/> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border" name="newhaulcontphone" id="newhaulcontphone" maxlength="25"/>
                        </div>
                        <label class="col-sm-2 control-label"> <bean:message key="ecom.bkg.contctmail" bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control email light_border required" name="newhaulcontemail" id="newhaulcontemail" maxlength="100"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label"> <bean:message key="Reg.Address" bundle="<%=clientcode%>"/> <span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <textarea id="newhauladdress" name="newhauladdress" class="form-control light_border required"></textarea>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-5">
                        </div>
                        <div class="col-sm-4">
                            <button type="submit" class="btn  btn-success btn-sm" id="submithaulagebtn"><span class="fa fa-plus"></span> <bean:message key='specialprivi.add' bundle="<%=clientcode%>"/> </button>
                            <button type="button" class="btn  btn-danger btn-sm" id="resethaulage" onclick="resetNewHaulage();"> <bean:message key='ecom.reset' bundle="<%=clientcode%>"/>  </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class='d-none'>
    <form action="FileAjax" method="post" id='filedownloadform'>
        <input type="hidden" id="BKGfilename" name="filename"/>
        <input type="hidden" id="BKGtype" name="type"/>
        <input type="hidden" id="BKGrefno" name="refno"/>
    </form>
</div>
<div class="modal fade" id="kycPendingModal" role="dialog" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white"> <h5 class="modal-title">KYC Verification Pending</h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <div class="form-group row">
                    <label class="col-sm-12 control-label" style="text-align: left;"><bean:message key="error.kyc.pending" bundle="<%=clientcode%>"/></label>
                </div>
                <div class="text-right">
                    <button type="button" class="btn btn-primary" data-dismiss="modal"><bean:message key="ecom.close" bundle="<%=clientcode%>"/></button>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var termsconditionshtml = "<div id='termsconditionhidden'><div class='w-100 h-100'><div class='card-header page_header' ><img class='header_icon_pages' src='Design/assets/images/page_icons/schdule.png'> <bean:message key='ecom.termsandcond.head' bundle="<%=clientcode%>"/></div> <div style='max-height: 495px;overflow-y: scroll;'><%=termsandconditions%></div>  <div class='text-center'> <a href='javascript:;' class='btn btn-info btn-lg m-t-5 m-b-5 text-white' style='width: 200px;' onclick='agreebtnclicked()'> <bean:message key='ecom.but.iagree' bundle="<%=clientcode%>"/> </a> </div> </div> </div>";
    var isKycPending = '<%=kycPending%>';
</script>
<script type="text/javascript">
    function showtermsconditions() {
        $.fancybox.open(termsconditionshtml, {
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

        });
    }
    function agreebtnclicked() {
        if (checkMailConfig == 'Y') {
            $('#ccalertmoal').modal('show');
        } else {
            submitwithoutcc();
        }
        $.fancybox.close();
    }
</script>
<script type="text/javascript">
    var form1 = $("#AmendmentBookingFormBean").show();
</script>



<script type="text/javascript" src="js/Booking.js?<%=jsdate%>"></script>
<script type="text/javascript" src="js/BookingPreviewScript.js?<%=jsdate%>"></script>
<script type='text/javascript' src='js/BkgexcelImport.js?<%=jsdate%>'></script>
<script type="text/javascript" src="js/BookingHaulage.js?<%=jsdate%>"></script>
<%
    if ("zh".equalsIgnoreCase(clientcode)) {
%>
<script type="text/javascript" src="newdesign/notify/notify.js<%=cacheVersion%>"></script>
<%            }%>
<script type="text/javascript">
    var ccMailId = '<%=ccMailId%>';
    function loadIcon(thisid)
    {
        var findclass = $("#" + thisid).hasClass("shown");
        if (findclass == false)
        {
            $("#" + thisid).addClass("shown");
        } else if (findclass == true)
        {
            $("#" + thisid).removeClass("shown");
        }
    }

    var portJson = portJson;
    var portlist = portJson;
    var commodityJson = commodityJson;
    var v1commodityJson;
    if (v1DataEnableStatus == 'Y') {
        v1commodityJson = v1commodityJson;
    }
    var packagesJson = packagesJson;
    var countryJson = countryJson;
    var counlist = countryJson;
    var hscodelst;
    var cmdgrp = commodityJson.filter(function (item) {
        return (item["parentcode"] == "0")
    });
    var allHScode = commodityJson.filter(function (item) {
        return (item["parentcode"] != "0")
    });
    var lrpbooknumber = $('#lrpBookNo').val();
    if (lrpbooknumber.length > 0)
    {
    <% boolean isvalid = (boolean) request.getAttribute("Enable_ORIGIN_DELIVERY");
        if ("Y".equalsIgnoreCase(Enable_ConfirmedBooking_Edit)) {
            if (!isvalid) {
    %>
        console.log("lrpbooknumber====>" + lrpbooknumber);
        $('#ebook_origin_country').attr("readonly", true);
        $('#ebook_delivery_country').attr("readonly", true);
    <%}
    } else {%>
        $('#ebook_origin_country').attr("readonly", true);
        $('#ebook_delivery_country').attr("readonly", true);
    <%}%>

    }

    function toSetCarriageCntct() {
    <% if ("GWF".equalsIgnoreCase(carrierCode)) {%>
        var org = $('#ebook_origin_country').val();
        var dest = $('#ebook_delivery_country').val();
        if (org !== "" && dest !== "") {
            $.ajax({
                type: "POST",
                cache: false,
                url: "BookingAjax?type=porttypevalue&org=" + org + "&dest=" + dest,
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    var str = trim(data);
                    $('#carrierContact').val(str);
                    $("#carrierContact option[value=" + str + "]").attr("selected", "selected");
                    console.log('carrierContact===>' + $('#carrierContact').val());

                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }
    <%}%>
    }

    function clearshipperref() {
        if ($('#shipperReference').length > 0) {
            $('#shipperReference').val('');
        }
    }

    function hideV2OtherCommodity(otherscomdtyid, labelid) {
        if ($('#appVersion').val() == '1') {
            //$('#' + otherscomdtyid).show();
            $('#' + labelid).show();
            $('.commoditywidthremove').each(function () {
                $(this).removeClass('commoditywidth');
                $(this).addClass('commoditywidth');
            });
        } else {
            //$('#' + otherscomdtyid).hide();
            $('#' + labelid).hide();
            $('.commoditywidthremove').each(function () {
                $(this).removeClass('commoditywidth');
            });
        }
    }
    function commodityload(comid, hiddencomid, id)
    {
        if ($('#appVersion').val() == '1') {
            var portlist = v1commodityJson;
            var ports = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: portlist
            });
            ports.initialize();
            $("#" + comid).typeahead(
                    {
                        hint: true,
                        highlight: true,
                        minLength: 2
                    },
                    {
                        name: comid,
                        limit: 5,
                        displayKey: function (portlist) {
                            return portlist.commodityname + " ~~ " + portlist.commoditycode;
                        },
                        source: ports.ttAdapter()
                    }).on('typeahead:render', function (obj, datum) {
                $("#" + hiddencomid).val("");
            }).on('typeahead:selected', function (obj, datum) {
                $("#" + hiddencomid).val(datum.commodityname);
            }).on('typeahead:autocomplete', function (obj, datum) {
                $("#" + hiddencomid).val(datum.commodityname);
            }).on("typeahead:change", function (obj, datum) {
                var hiddenValue = $("#" + hiddencomid).val();
                if (hiddenValue == null || hiddenValue == "") {
                    $("#" + comid).typeahead('val', "");
                    $(obj.currentTarget).val("");
                }
            });
        } else {
            var portlist = cmdgrp;
            var ports = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: portlist
            });
            var hiddenValue = $("#hdcommoidtycode" + id).val();
            $('#hscode' + id).typeahead('destroy');
            if (hiddenValue != null && hiddenValue != '' && hiddenValue != fakComdtyCode) {
                if (openCommodityJsonArray.includes(hiddenValue)) {
                    console.log("openCommodityJsonArray");
                    hscodelst = allHScode;
                } else {
                    console.log("hiddenValue>>>" + hiddenValue);
                    hscodelst = commodityJson.filter(function (item) {
                        return (item["grpcode"] == hiddenValue)
                    });
                }
                hscommodityload("hscode" + id, "hiddenhscodeid" + id, hscodelst);
                loadtypeaheadtooltipmsg();
            } else {
    <%if ("Y".equalsIgnoreCase(hscode_search)) {%>
                hscommodityload("hscode" + id, "hiddenhscodeid" + id, allHScode);
                loadtypeaheadtooltipmsg();
    <%}%>
            }
            ports.initialize();
            $("#" + comid).typeahead(
                    {
                        hint: true,
                        highlight: true,
                        minLength: 2
                    },
                    {
                        name: comid,
                        limit: 5,
                        displayKey: function (portlist) {
                            return portlist.commodityname + " ~~ " + portlist.commoditycode;
                        },
                        source: ports.ttAdapter()
                    }).on('typeahead:render', function (obj, datum) {
                $("#" + hiddencomid).val("");
            }).on('typeahead:selected', function (obj, datum) {
                $('#hscode' + id).val("");
                $('#hscode' + id).typeahead('val', '');
                $('#hiddenhscodeid' + id).val("");
                $("#" + hiddencomid).val(datum.commoditycode);
            }).on('typeahead:autocomplete', function (obj, datum) {
                $("#" + hiddencomid).val(datum.commoditycode);
            }).on("typeahead:change", function (obj, datum) {
                var hiddenValue = $("#" + hiddencomid).val();
                $('#hscode' + id).typeahead('destroy');
                $('#hscode' + id).val("");
                $('#hscode' + id).typeahead('val', '');
                $('#hiddenhscodeid' + id).val("");
                if (hiddenValue != null && hiddenValue != '' && hiddenValue != fakComdtyCode) {
                    if (openCommodityJsonArray.includes(hiddenValue)) {
                        console.log("openCommodityJsonArray");
                        hscodelst = allHScode;
                    } else {
                        hscodelst = commodityJson.filter(function (item) {
                            return (item["grpcode"] == hiddenValue)
                        });
                    }
    <%if ("Y".equalsIgnoreCase(hscode_search)) {%>
                    if (hiddenValue == '') {
                        hscodelst = allHScode;
                    }
    <%}%>

                    hscommodityload("hscode" + id, "hiddenhscodeid" + id, hscodelst);
                } else {
    <%if ("Y".equalsIgnoreCase(hscode_search)) {%>
                    hscommodityload("hscode" + id, "hiddenhscodeid" + id, allHScode);
    <%}%>
                }
                if (hiddenValue == null || hiddenValue == "") {
                    $("#" + comid).typeahead('val', "");
                    $(obj.currentTarget).val("");
                }
            });
        }
        loadtypeaheadtooltipmsg();
    }
    function hscommodityload(comid, hiddenhscodeid, hscodelst)
    {
        if ($('#appVersion').val() != '1') {
            var ports = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: hscodelst
            });
            ports.initialize();
            $("#" + comid).typeahead(
                    {
                        hint: true,
                        highlight: true,
                        minLength: 2
                    },
                    {
                        name: comid,
                        limit: 5,
                        displayKey: function (hscodelst) {
                            return hscodelst.commodityname + " ~~ " + hscodelst.commoditycode;
                        },
                        source: ports.ttAdapter()
                    }).on('typeahead:render', function (obj, datum) {
                $("#" + hiddenhscodeid).val("");
            }).on('typeahead:selected', function (obj, datum) {
                $("#" + hiddenhscodeid).val(datum.commodityname);
                setParentCommodity(comid, datum.grpcode);
            }).on('typeahead:autocomplete', function (obj, datum) {
                $("#" + hiddenhscodeid).val(datum.commodityname);
                setParentCommodity(comid, datum.grpcode);
            }).on("typeahead:change", function (obj, datum) {
                var hiddenValue = $("#" + hiddenhscodeid).val();
                if (hiddenValue == null || hiddenValue == "") {
                    $("#" + comid).typeahead('val', "");
                    $(obj.currentTarget).val("");
                }
            });
    <%if ("Y".equalsIgnoreCase(hscode_autoset)) {%>
            if (hscodelst.length == 1) {
                $("#" + comid).typeahead('val', hscodelst[0].commodityname + " ~~ " + hscodelst[0].commoditycode);
            }
    <%}%>
        } else {
            $("#" + comid).attr('maxlength', "100");
        }
    }
    function setParentCommodity(hscodeid, hscode) {
    <%if ("Y".equalsIgnoreCase(hscode_search)) {%>
        console.log("hscode==> " + hscode);
        var comdtydisable = '<%=Disable_CommodityGRP%>';
        var id = hscodeid.replace("hscode", "");
        var cmdid2 = "commodity" + id;
        var isopencmdt = 'N';
        if ($('#' + cmdid2).val() != null && $('#' + cmdid2).val() != '') {
            var cmdval = $('#' + cmdid2).val().split('~~')[1].trim();
            if (openCommodityJsonArray.includes(cmdval)) {
                isopencmdt = 'Y';
            }
        }
        if (comdtydisable == 'Y' || isopencmdt == 'N') {//($("#" + cmdid2).val() == '' || $("#" + cmdid2).val().length == 0)
            var cmdtyobj = commodityJson.filter(function (item) {
                return (item["commoditycode"] == hscode);
            });
            $("#" + cmdid2).typeahead('val', cmdtyobj[0].commodityname + " ~~ " + cmdtyobj[0].commoditycode);
        }
    <%}%>
    }
    function saveTempBookingAmendment() {
        $('.eqpstatusclass').attr("disabled", false);
        $(".eqpstatusclass").each(function () {
            $(this).attr("disabled", false);
        });
        $('#bookingSelect,#ebook_bookingname,#ebook_bookingaddress,#ebook_bookingaddress1,#ebook_bookingcountry_name,#ebook_bookingcity_desc,#ebook_bookingphone,#ebook_bookingfax,#ebook_bookingemail,#ebook_bookingstate').attr("readonly", false);
        $('#bookingSelect').attr('disabled', false);
        if($('#ebook_bookingzipcode').length > 0){
            $('#ebook_bookingzipcode').attr("readonly", false);
        }
        document.getElementById("srchd").value = document.getElementById("trpservice").value;
        document.getElementById("submit_btn").value = "tempadd";
    }
    function saveDraftBookingAmendment() {


        document.getElementById("submit_btn").value = "draftadd";
        //callLoading();
        $('.eqpstatusclass').attr("disabled", false);
        $(".eqpstatusclass").each(function () {
            $(this).attr("disabled", false);
        });
        $('#agreeSelect,#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate').attr("readonly", false);
        if($('#ebook_agreezipcode').length > 0){
            $('#ebook_agreezipcode').attr("readonly", false);
        }
        $('#preCarriage,#onCarriage').attr("disabled", false);
        $('.commoditytypehead').prop('disabled', false);
    <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
        $('#bookingSelect,#ebook_bookingname,#ebook_bookingaddress,#ebook_bookingaddress1,#ebook_bookingcountry_name,#ebook_bookingcity_desc,#ebook_bookingphone,#ebook_bookingfax,#ebook_bookingemail,#ebook_bookingstate').attr("readonly", false);
        $('#bookingSelect').attr('disabled', false);
        if($('#ebook_bookingzipcode').length > 0){
            $('#ebook_bookingzipcode').attr("readonly", false);
        }
    <%}%>
        document.getElementById('AmendmentBookingFormBean').action = "amendmentbookingsave.do";
        document.getElementById('AmendmentBookingFormBean').submit();
    }
    function packageload(packid, hiddenpackid)
    {
        var portlist = packagesJson;
        //<![CDATA[
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('packagename', 'packagecode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: portlist
        });
        ports.initialize();
        $("#" + packid).typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2
                },
                {
                    name: packid,
                    limit: 5,
                    displayKey: function (portlist) {
                        return portlist.packagename + " ~~ " + portlist.packagecode;
                    },
                    source: ports.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#" + hiddenpackid).val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#" + hiddenpackid).val(datum.packagename);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#" + hiddenpackid).val(datum.packagename);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#" + hiddenpackid).val();
            if (hiddenValue == null || hiddenValue == "") {
                $("#" + packid).typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    }
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
    function addamendmentcargo()
    {

        var id = 0;
        var i = 1;
        var rowval = 1;
        var rowid = 0;
        $('.packtypehead').typeahead('destroy');
        $('.commoditytypehead').typeahead('destroy');
        var rowCount = $('#datatablebycargo >tbody >tr').length;
        $('.preview').each(function () {
            id = $(this).attr('id');
            commodityload('commodity' + id, 'hiddencomid' + id, id);
            // hscommodityload('hscode' + id, 'hiddenhscodeid' + id);
            packageload('packagename' + id, 'hiddenpackid' + id);
        });
        rowCount = parseInt(id) + 1;
        $.ajax({
            url: "AddAmendmentCargoDetails.jsp?rowid=" + rowCount + "&type=add",
            cache: false,
            success: function (data) {
                $('#datatablebycargodiv').append(data);
//                $('#datatablebycargo>tbody').last().after(data);
//                $('.rowidchange').each(function () {
//                    //alert(rowval)
//                    rowid = $(this).attr('id');
//                    $('#' + rowid).html(rowval);
//                    rowval++;
//                });
                loadtypeaheadtooltipmsg();
    <%if ("Y".equalsIgnoreCase(Disable_CommodityGRP)) {%>
                $('.commoditytypehead').prop('disabled', true);
    <%}%>
         disableCustAddButton();
         setSellerNameSelectOption();
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    <%
        if ("zh".equalsIgnoreCase(clientcode) || "ma".equalsIgnoreCase(clientcode) || "es".equalsIgnoreCase(clientcode)) {
    %>
    toastr.info("<bean:message key="ecom.msg.formsineng" bundle="<%=clientcode%>"/>", "INFO");
    <%}%>

function addCustRowAjax(cust, type) {
console.log('New booking '+type+'//cust//'+cust)
    const container = $('#'+cust+'Container');
    var rowid=0;
    let currentVal = parseInt($('#' + cust + 'Count').val(),10) || 0;
    let nextIndex = currentVal + 1;
    $.ajax({
        url: 'AddAmendmentCustomerDetails.jsp?id=' + nextIndex + '&custype=' + cust,
        cache: false,
        success: function (response) {
            console.log('nextIndex: ' + nextIndex);
           
            container.append(response);
          container.find('.previewCust').each(function (index) {
    const id = $(this).attr('id');
    console.log('id---->' + id);

    loadStateAutoComplete(
        'ebook_' + cust + 'country_name' + id,
        'ebook_' + cust + 'state' + id,
        'typecustheadhidden' + id,
        'load'
    );
    loadCuntryName(
        'ebook_' + cust + 'country_name' + id,
        'typecustcountryheadhidden' + id
    );
    setCountry_name(cust, id,'typecustcountryheadhidden'+id);
    $('#' + cust + 'Count').val(id);

    console.log('==>//' + $('#' + cust + 'Count').val());
});

 applyRules('AmendmentBookingFormBean',cust, '<%=HideFaxNo%>' );          
    disableCustAddButton();
    setSellerNameSelectOption();
        },
        error: function () {
            alert('Failed to load buyer row.');
        }
    });
}

function loadCuntryName(cust){
     var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
     ports.initialize();
                $('#ebook_'+cust+'country_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()}).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden2").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden2").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_'+cust+'country_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
}


function removeBuyerRow(btn) {
    btn.closest('.buyer-row').remove();

    // Recount and update hidden value
    const count = $('#buyerContainer .buyer-row').length+1;
    $('#buyerCount').val(count);
}

</script>

<script type="text/javascript">
    var typecheck = "<%=type12%>";
    if (document.getElementById('ebook_masthdn').value == "Y")
    {
        $('.next').html("Next <span class=\"glyphicon glyphicon-arrow-right\"></span>").removeClass("selectroutepop");
        $(".routingdiv").show()
    }
    document.getElementById('changeDateValue').value = trim(document.getElementById('ebook_date').value);
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
        $('#ebook_status_remarks').attr('maxlength', "450");
        $("#panel-c1,#panel-c2,#panel-c3,#panel-c4,#panel-c5").hide();
        $("#AmendmentBookingFormBean").validate({
            rules: {
                ebook_origin_country: {
                    required: true,
                    notEqualTo: ['#ebook_delivery_country']},
                ebook_delivery_country: {
                    required: true,
                    notEqualTo: ['#ebook_origin_country']
                },
                ebook_agreename: {
                    required: true
                },
                ebook_agreeaddress: {
                    required: true
                },
                ebook_agreecountry_name: {
                    required: true
                },
                ebook_agreecity_desc: {
                    required: true
                },

                ebook_agreeemail: {
                    required: function (element) {
                        return($("#ebook_agreename").val().trim().length > 0 && $("#ebook_agreeeori").length > 0 && $("#ebook_agreeeori").val() == "" && isNewCustomer("ebook_agreecode") && checkEUCountry('ebook_agreecountry_name'))
                    }
                },
                ebook_shipperemail: {
                    required: function (element) {
                        return($("#ebook_shippername").val().trim().length > 0 && $("#ebook_shippereori").length > 0 && $("#ebook_shippereori").val() == "" && isNewCustomer("ebook_shippercode") && checkEUCountry('ebook_shippercountry_name'))
                    }
                },
                ebook_consigneeemail: {
                    required: function (element) {
                        return($("#ebook_consigneename").val().trim().length > 0 && $("#ebook_consigneeeori").length > 0 && $("#ebook_consigneeeori").val() == "" && isNewCustomer("ebook_consigneecode") && checkEUCountry('ebook_consigneecountry_name'))
                    }
                },
                ebook_forwarderemail: {
                    required: function (element) {
                        return($("#ebook_forwardername").val().trim().length > 0 && $("#ebook_forwardereori").length > 0 && $("#ebook_forwardereori").val() == "" && isNewCustomer("ebook_forwardercode") && checkEUCountry('ebook_forwardercountry_name'))
                    }
                },
                ebook_notifyemail_n1: {
                    required: function (element) {
                        return($("#ebook_notifyname_n1").val().trim().length > 0 && $("#ebook_notifyeori_n1").length > 0 && $("#ebook_notifyeori_n1").val() == "" && isNewCustomer("ebook_notifycode_n1") && checkEUCountry('ebook_notifycountry_name_n1'))
                    }
                },
                ebook_notifyemail: {
                    required: function (element) {
                        return($("#ebook_notifyname").val().trim().length > 0 && $("#ebook_notifyeori").length > 0 && $("#ebook_notifyeori").val() == "" && isNewCustomer("ebook_notifycode") && checkEUCountry('ebook_notifycountry_name'))
                    }
                },
                ebook_Warehouseemail: {
                    required: function (element) {
                        return($("#ebook_Warehousename").val().trim().length > 0 && $("#ebook_Warehouseeori").length > 0 && $("#ebook_Warehouseeori").val() == "" && isNewCustomer("ebook_Warehousecode") && checkEUCountry('ebook_Warehousecountry_name'))
                    }
                },
                ebook_NamedCustomeremail: {
                    required: function (element) {
                        return($("#ebook_NamedCustomername").val().trim().length > 0 && $("#ebook_NamedCustomereori").length > 0 && $("#ebook_NamedCustomereori").val() == "" && isNewCustomer("ebook_NamedCustomercode") && checkEUCountry('ebook_NamedCustomercountry_name'))
                    }
                },
                ebook_shippercountry_name: {
                    required: function (element) {
                        return $("#ebook_shippername").val() != "" || $("#ebook_shipperaddress").val() != "" || $("#ebook_shippercountry_name").val() != "" || $("#ebook_shippercity_desc").val() != "" || $("#ebook_shipperphone").val() != "" || $("#ebook_shipperaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_shipperfax").val() != "") || $("#ebook_shipperemail").val() != "" || $("#ebook_shipperstate").val() != "" || <%=custMandatory.contains("SH")%>;
                    }
                },
                ebook_agreefax: {
                    phonefax: ["#ebook_agreefax"]
                },
                ebook_agreephone: {
                    phonefax: ["#ebook_agreephone"],
                    required: function (element) {
                        return($("#ebook_agreename").val().trim().length > 0 && $("#ebook_agreeeori").length > 0 && $("#ebook_agreeeori").val() == "" && isNewCustomer("ebook_agreecode") && checkEUCountry('ebook_agreecountry_name'))
                    }
                },
                ebook_shipperfax: {
                    validnumbers: "#ebook_shipperfax",
                    ZeroTest: "#ebook_shipperfax",
                    phonefax: ["#ebook_shipperfax"]
                },
                ebook_shipperphone: {
                    validnumbers: "#ebook_shipperphone",
                    ZeroTest: "#ebook_shipperphone",
                    phonefax: ["#ebook_shipperphone"],
                    required: function (element) {
                        return($("#ebook_shippername").val().trim().length > 0 && $("#ebook_shippereori").length > 0 && $("#ebook_shippereori").val() == "" && isNewCustomer("ebook_shippercode") && checkEUCountry('ebook_shippercountry_name'))
                    }
                },
                ebook_shippercity_desc: {
                    required: function (element) {
                        return $("#ebook_shippername").val() != "" || $("#ebook_shipperaddress").val() != "" || $("#ebook_shippercountry_name").val() != "" || $("#ebook_shippercity_desc").val() != "" || $("#ebook_shipperphone").val() != "" || $("#ebook_shipperaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_shipperfax").val() != "") || $("#ebook_shipperemail").val() != "" || $("#ebook_shipperstate").val() != "" || <%=custMandatory.contains("SH")%>;
                    }
                },
                ebook_shippername: {
                    required: function (element) {
                        return $("#ebook_shippername").val() != "" || $("#ebook_shipperaddress").val() != "" || $("#ebook_shippercountry_name").val() != "" || $("#ebook_shippercity_desc").val() != "" || $("#ebook_shipperphone").val() != "" || $("#ebook_shipperaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_shipperfax").val() != "") || $("#ebook_shipperemail").val() != "" || $("#ebook_shipperstate").val() != "" || <%=custMandatory.contains("SH")%>;
                    }
                },
                ebook_shipperaddress: {
                    required: function (element) {
                        return $("#ebook_shippername").val() != "" || $("#ebook_shipperaddress").val() != "" || $("#ebook_shippercountry_name").val() != "" || $("#ebook_shippercity_desc").val() != "" || $("#ebook_shipperphone").val() != "" || $("#ebook_shipperaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_shipperfax").val() != "") || $("#ebook_shipperemail").val() != "" || $("#ebook_shipperstate").val() != "" || <%=custMandatory.contains("SH")%>;
                    }
                },
                consigneeSelect: {equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#notifySelect" : "#consigneeSelect";
                    }
//                    ,
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#notify1Select" : "#consigneeSelect";
//                    }
                },
                ebook_consigneename: {
                    required: function (element) {
                        return $('#consigneetoorder').is(":checked") || $("#ebook_consigneename").val() != "" || $("#ebook_consigneeaddress").val() != "" || $("#ebook_consigneecountry_name").val() != "" || $("#ebook_consigneecity_desc").val() != "" || $("#ebook_consigneephone").val() != "" || $("#ebook_consigneeaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_consigneefax").val() != "") || $("#ebook_consigneeemail").val() != "" || $("#ebook_consigneestate").val() != "" || <%=custMandatory.contains("CN")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifyname" : "#ebook_consigneename";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifyname_n1" : "#ebook_consigneename";
//                    }
                },
                ebook_consigneeaddress: {
                    required: function (element) {
                        return (!$('#consigneetoorder').is(":checked")) && ($("#ebook_consigneename").val() != "" || $("#ebook_consigneeaddress").val() != "" || $("#ebook_consigneecountry_name").val() != "" || $("#ebook_consigneecity_desc").val() != "" || $("#ebook_consigneephone").val() != "" || $("#ebook_consigneeaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_consigneefax").val() != "") || $("#ebook_consigneeemail").val() != "" || $("#ebook_consigneestate").val() != "") || <%=custMandatory.contains("CN")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifyaddress" : "#ebook_consigneeaddress";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifyaddress_n1" : "#ebook_consigneeaddress";
//                    }
                },
                ebook_consigneeaddress1: {
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifyaddress1" : "#ebook_consigneeaddress1";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifyaddress1_n1" : "#ebook_consigneeaddress1";
//                    }
                },
                ebook_consigneecountry_name: {
                    required: function (element) {
                        return (!$('#consigneetoorder').is(":checked")) && ($("#ebook_consigneename").val() != "" || $("#ebook_consigneeaddress").val() != "" || $("#ebook_consigneecountry_name").val() != "" || $("#ebook_consigneecity_desc").val() != "" || $("#ebook_consigneephone").val() != "" || $("#ebook_consigneeaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_consigneefax").val() != "") || $("#ebook_consigneeemail").val() != "" || $("#ebook_consigneestate").val() != "") || <%=custMandatory.contains("CN")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifycountry_name" : "#ebook_consigneecountry_name";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifycountry_name_n1" : "#ebook_consigneecountry_name";
//                    }
                },
                ebook_consigneecity_desc: {
                    required: function (element) {
                        return (!$('#consigneetoorder').is(":checked")) && ($("#ebook_consigneename").val() != "" || $("#ebook_consigneeaddress").val() != "" || $("#ebook_consigneecountry_name").val() != "" || $("#ebook_consigneecity_desc").val() != "" || $("#ebook_consigneephone").val() != "" || $("#ebook_consigneeaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_consigneefax").val() != "") || $("#ebook_consigneeemail").val() != "" || $("#ebook_consigneestate").val() != "") || <%=custMandatory.contains("CN")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifycity_desc" : "#ebook_consigneecity_desc";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifycity_desc_n1" : "#ebook_consigneecity_desc";
//                    }
                }, ebook_consigneephone: {
                    validnumbers: "#ebook_consigneephone",
                    ZeroTest: "#ebook_consigneephone",
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifyphone" : "#ebook_consigneephone";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifyphone_n1" : "#ebook_consigneephone";
//                    },
                    phonefax: ["#ebook_consigneephone"],
                    required: function (element) {
                        return($("#ebook_consigneename").val().trim().length > 0 && $("#ebook_consigneeeori").length > 0 && $("#ebook_consigneeeori").val() == "" && isNewCustomer("ebook_consigneecode") && checkEUCountry('ebook_consigneecountry_name'))
                    }
                },
                ebook_consigneefax: {
                    validnumbers: "#ebook_consigneefax",
                    ZeroTest: "#ebook_consigneefax",
                    phonefax: ["#ebook_consigneefax"]
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifyfax" : "#ebook_consigneefax";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifyfax_n1" : "#ebook_consigneefax";
//                    }
                },
//                ebook_consigneeemail: {
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_notifyemail" : "#ebook_consigneeemail";
//                    },
//                    EqualTo_con: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_notifyemail_n1" : "#ebook_consigneeemail";
//                    }
//                },
                ebook_notifyname: {
                    required: function (element) {
                        return $("#ebook_notifyname").val() != "" || $("#ebook_notifyaddress").val() != "" || $("#ebook_notifycountry_name").val() != "" || $("#ebook_notifycity_desc").val() != "" || $("#ebook_notifyphone").val() != "" || $("#ebook_notifyaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax").val() != "") || $("#ebook_notifyemail").val() != "" || $("#ebook_notifystate").val() != "" || <%=custMandatory.contains("NT")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneename" : "#ebook_notifyname";
//                    }
                },
                ebook_notifyaddress: {
                    required: function (element) {
                        return  ($("#ebook_notifyname").val() != "" || $("#ebook_notifyaddress").val() != "" || $("#ebook_notifycountry_name").val() != "" || $("#ebook_notifycity_desc").val() != "" || $("#ebook_notifyphone").val() != "" || $("#ebook_notifyaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax").val() != "") || $("#ebook_notifyemail").val() != "" || $("#ebook_notifystate").val() != "") || <%=custMandatory.contains("NT")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneeaddress" : "#ebook_notifyaddress";
//                    }
                },
                ebook_notifyaddress1: {
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneeaddress1" : "#ebook_notifyaddress1";
//                    }
                },
                ebook_notifycountry_name: {
                    required: function (element) {
                        return ($("#ebook_notifyname").val() != "" || $("#ebook_notifyaddress").val() != "" || $("#ebook_notifycountry_name").val() != "" || $("#ebook_notifycity_desc").val() != "" || $("#ebook_notifyphone").val() != "" || $("#ebook_notifyaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax").val() != "") || $("#ebook_notifyemail").val() != "" || $("#ebook_notifystate").val() != "") || <%=custMandatory.contains("NT")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneecountry_name" : "#ebook_notifycountry_name";
//                    }
                },
                ebook_notifycity_desc: {
                    required: function (element) {
                        return ($("#ebook_notifyname").val() != "" || $("#ebook_notifyaddress").val() != "" || $("#ebook_notifycountry_name").val() != "" || $("#ebook_notifycity_desc").val() != "" || $("#ebook_notifyphone").val() != "" || $("#ebook_notifyaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax").val() != "") || $("#ebook_notifyemail").val() != "" || $("#ebook_notifystate").val() != "") || <%=custMandatory.contains("NT")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneecity_desc" : "#ebook_notifycity_desc";
//                    }
                },
                ebook_notifyphone: {
                    validnumbers: "#ebook_notifyphone",
                    ZeroTest: "#ebook_notifyphone",
                    phonefax: ["#ebook_notifyphone"],
                    required: function (element) {
                        return($("#ebook_notifyname").val().trim().length > 0 && $("#ebook_notifyeori").length > 0 && $("#ebook_notifyeori").val() == "" && isNewCustomer("ebook_notifycode") && checkEUCountry('ebook_notifycountry_name'))
                    }

//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneephone" : "#ebook_notifyphone";
//                    }
                },
                ebook_notifyfax: {
                    validnumbers: "#ebook_notifyfax",
                    ZeroTest: "#ebook_notifyfax",
                    phonefax: ["#ebook_notifyfax"]
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneefax" : "#ebook_notifyfax";
//                    }
                },
//                ebook_notifyemail: {
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn1check').val() == '1' ? "#ebook_consigneeemail" : "#ebook_notifyemail";
//                    }
//                },
                ebook_notifyname_n1: {
                    required: function (element) {
                        return  $("#ebook_notifyname_n1").val() != "" || $("#ebook_notifyaddress_n1").val() != "" || $("#ebook_notifycountry_name_n1").val() != "" || $("#ebook_notifycity_desc_n1").val() != "" || $("#ebook_notifyphone_n1").val() != "" || $("#ebook_notifyaddress1_n1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax_n1").val() != "") || $("#ebook_notifyemail_n1").val() != "" || $("#ebook_notifystate_n1").val() != "" || <%=custMandatory.contains("N1")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneename" : "#ebook_notifyname_n1";
//                    }
                },
                ebook_notifyaddress_n1: {
                    required: function (element) {
                        return ($("#ebook_notifyname_n1").val() != "" || $("#ebook_notifyaddress_n1").val() != "" || $("#ebook_notifycountry_name_n1").val() != "" || $("#ebook_notifycity_desc_n1").val() != "" || $("#ebook_notifyphone_n1").val() != "" || $("#ebook_notifyaddress1_n1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax_n1").val() != "") || $("#ebook_notifyemail_n1").val() != "" || $("#ebook_notifystate_n1").val() != "" || <%=custMandatory.contains("N1")%>);
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneeaddress" : "#ebook_notifyaddress_n1";
//                    }
                },
                ebook_notifyaddress1_n1: {
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneeaddress1" : "#ebook_notifyaddress1_n1";
//                    }
                },
                ebook_notifycountry_name_n1: {
                    required: function (element) {
                        return  ($("#ebook_notifyname_n1").val() != "" || $("#ebook_notifyaddress_n1").val() != "" || $("#ebook_notifycountry_name_n1").val() != "" || $("#ebook_notifycity_desc_n1").val() != "" || $("#ebook_notifyphone_n1").val() != "" || $("#ebook_notifyaddress1_n1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax_n1").val() != "") || $("#ebook_notifyemail_n1").val() != "" || $("#ebook_notifystate_n1").val() != "") || <%=custMandatory.contains("N1")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneecountry_name" : "#ebook_notifycountry_name_n1";
//                    }
                },
                ebook_notifycity_desc_n1: {
                    required: function (element) {
                        return ($("#ebook_notifyname_n1").val() != "" || $("#ebook_notifyaddress_n1").val() != "" || $("#ebook_notifycountry_name_n1").val() != "" || $("#ebook_notifycity_desc_n1").val() != "" || $("#ebook_notifyphone_n1").val() != "" || $("#ebook_notifyaddress1_n1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notifyfax_n1").val() != "") || $("#ebook_notifyemail_n1").val() != "" || $("#ebook_notifystate_n1").val() != "") || <%=custMandatory.contains("N1")%>;
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneecity_desc" : "#ebook_notifycity_desc_n1";
//                    }
                }, ebook_notifyphone_n1: {
                    validnumbers: "#ebook_notifyphone_n1",
                    ZeroTest: "#ebook_notifyphone_n1",
                    phonefax: ["#ebook_notifyphone"],
                    required: function (element) {
                        return($("#ebook_notifyname_n1").val().trim().length > 0 && $("#ebook_notifyeori_n1").length > 0 && $("#ebook_notifyeori_n1").val() == "" && isNewCustomer("ebook_notifycode_n1") && checkEUCountry('ebook_notifycountry_name_n1'))
                    }
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneephone" : "#ebook_notifyphone_n1";
//                    }
                },
                ebook_notifyfax_n1: {
                    validnumbers: "#ebook_notifyfax_n1",
                    ZeroTest: "#ebook_notifyfax_n1",
                    phonefax: ["#ebook_notifyfax"]
//                    ,
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneefax" : "#ebook_notifyfax_n1";
//                    }
                },
//                ebook_notifyemail_n1: {
//                    EqualTo_concheck: function (element) {
//                        return $('#hidn2check').val() == '1' ? "#ebook_consigneeemail" : "#ebook_notifyemail_n1";
//                    }
//                },
                ebook_forwardername: {
                    required: function (element) {
                        return  $("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity_desc").val() != "" || $("#ebook_forwarderphone").val() != "" || $("#ebook_forwarderaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "" || $("#ebook_forwarderstate").val() != "" || <%=custMandatory.contains("FW")%>;
                    }
//                   
                },
                ebook_forwarderaddress: {
                    required: function (element) {
                        return  ($("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity_desc").val() != "" || $("#ebook_forwarderphone").val() != "" || $("#ebook_forwarderaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "" || $("#ebook_forwarderstate").val() != "") || <%=custMandatory.contains("FW")%>;
                    }
//                   
                },

                ebook_forwardercountry_name: {
                    required: function (element) {
                        return ($("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity_desc").val() != "" || $("#ebook_forwarderphone").val() != "" || $("#ebook_forwarderaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "" || $("#ebook_forwarderstate").val() != "") || <%=custMandatory.contains("FW")%>;
                    }
//                    
                },
                ebook_forwardercity_desc: {
                    required: function (element) {
                        return  ($("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity_desc").val() != "" || $("#ebook_forwarderphone").val() != "" || $("#ebook_forwarderaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "" || $("#ebook_forwarderstate").val() != "") || <%=custMandatory.contains("FW")%>;
                    }
//                    ,
//                   
                },
                ebook_forwarderphone: {
                    validnumbers: "#ebook_forwarderphone",
                    ZeroTest: "#ebook_forwarderphone",
                    phonefax: ["#ebook_forwarderphone"],
                    required: function (element) {
                        return($("#ebook_forwardername").val().trim().length > 0 && $("#ebook_forwardereori").length > 0 && $("#ebook_forwardereori").val() == "" && isNewCustomer("ebook_forwardercode") && checkEUCountry('ebook_forwardercountry_name'))
                    }
//                   
                },
                ebook_forwarderfax: {
                    validnumbers: "#ebook_forwarderfax",
                    ZeroTest: "#ebook_forwarderfax",
                    phonefax: ["#ebook_forwarderfax"]
//                    
                },

                ebook_Warehousename: {
                    required: function (element) {
                        return  $("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity_desc").val() != "" || $("#ebook_Warehousephone").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "" || $("#ebook_Warehousestate").val() != "" || <%=custMandatory.contains("WH")%>;
                    }
//                    
                },
                ebook_Warehouseaddress: {
                    required: function (element) {
                        return  ($("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity_desc").val() != "" || $("#ebook_Warehousephone").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "" || $("#ebook_Warehousestate").val() != "") || <%=custMandatory.contains("WH")%>;
                    }
//                    
                },

                ebook_Warehousecountry_name: {
                    required: function (element) {
                        return ($("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity_desc").val() != "" || $("#ebook_Warehousephone").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "" || $("#ebook_Warehousestate").val() != "") || <%=custMandatory.contains("WH")%>;
                    }
//                    
                },
                ebook_Warehousecity_desc: {
                    required: function (element) {
                        return  ($("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity_desc").val() != "" || $("#ebook_Warehousephone").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "" || $("#ebook_Warehousestate").val() != "") || <%=custMandatory.contains("WH")%>;
                    }
//                    
                },
                ebook_Warehousephone: {
                    validnumbers: "#ebook_Warehousephone",
                    ZeroTest: "#ebook_Warehousephone",
                    phonefax: ["#ebook_Warehousephone"],
                    required: function (element) {
                        return($("#ebook_Warehousename").val().trim().length > 0 && $("#ebook_Warehouseeori").length > 0 && $("#ebook_Warehouseeori").val() == "" && isNewCustomer("ebook_Warehousecode") && checkEUCountry('ebook_Warehousecountry_name'))
                    }
//                   
                },
                ebook_Warehousefax: {
                    validnumbers: "#ebook_Warehousefax",
                    ZeroTest: "#ebook_Warehousefax",
                    phonefax: ["#ebook_Warehousefax"]
//                    
                },

                ebook_NamedCustomername: {
                    required: function (element) {
                        return  $("#ebook_NamedCustomername").val() != "" || $("#ebook_NamedCustomeraddress").val() != "" || $("#ebook_NamedCustomercountry_name").val() != "" || $("#ebook_Warehousecity_desc").val() != "" || $("#ebook_NamedCustomerphone").val() != "" || $("#ebook_NamedCustomeraddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_NamedCustomerfax").val() != "") || $("#ebook_NamedCustomeremail").val() != "" || $("#ebook_NamedCustomerstate").val() != "" || <%=custMandatory.contains("NC")%>;
                    }
//                    
                },
                ebook_NamedCustomeraddress: {
                    required: function (element) {
                        return  ($("#ebook_NamedCustomername").val() != "" || $("#ebook_NamedCustomeraddress").val() != "" || $("#ebook_NamedCustomercountry_name").val() != "" || $("#ebook_NamedCustomercity_desc").val() != "" || $("#ebook_NamedCustomerphone").val() != "" || $("#ebook_NamedCustomeraddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_NamedCustomerfax").val() != "") || $("#ebook_NamedCustomeremail").val() != "" || $("#ebook_NamedCustomerstate").val() != "") || <%=custMandatory.contains("NC")%>;
                    }
//                    
                },

                ebook_NamedCustomercountry_name: {
                    required: function (element) {
                        return ($("#ebook_NamedCustomername").val() != "" || $("#ebook_NamedCustomeraddress").val() != "" || $("#ebook_NamedCustomercountry_name").val() != "" || $("#ebook_NamedCustomercity_desc").val() != "" || $("#ebook_NamedCustomerphone").val() != "" || $("#ebook_NamedCustomeraddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_NamedCustomerfax").val() != "") || $("#ebook_NamedCustomeremail").val() != "" || $("#ebook_NamedCustomerstate").val() != "") || <%=custMandatory.contains("NC")%>;
                    }
//                    
                },
                ebook_NamedCustomercity_desc: {
                    required: function (element) {
                        return  ($("#ebook_NamedCustomername").val() != "" || $("#ebook_NamedCustomeraddress").val() != "" || $("#ebook_NamedCustomercountry_name").val() != "" || $("#ebook_NamedCustomercity_desc").val() != "" || $("#ebook_NamedCustomerphone").val() != "" || $("#ebook_NamedCustomeraddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_NamedCustomerfax").val() != "") || $("#ebook_NamedCustomeremail").val() != "" || $("#ebook_NamedCustomerstate").val() != "") || <%=custMandatory.contains("NC")%>;
                    }
//                    
                },
                ebook_NamedCustomerphone: {
                    validnumbers: "#ebook_NamedCustomerphone",
                    ZeroTest: "#ebook_NamedCustomerphone",
                    phonefax: ["#ebook_NamedCustomerphone"],
                    required: function (element) {
                        return($("#ebook_NamedCustomername").val().trim().length > 0 && $("#ebook_NamedCustomereori").length > 0 && $("#ebook_NamedCustomereori").val() == "" && isNewCustomer("ebook_NamedCustomercode") && checkEUCountry('ebook_NamedCustomercountry_name'))
                    }
//                   
                },
                ebook_NamedCustomerfax: {
                    validnumbers: "#ebook_NamedCustomerfax",
                    ZeroTest: "#ebook_NamedCustomerfax",
                    phonefax: ["#ebook_NamedCustomerfax"]
//                    
                },

                termsagree: {
                    required: true
                },
                haulcontactphone: {
                    validnumbers: "#ebook_shipperphone",
                    ZeroTest: "#ebook_shipperphone",
                    phonefax: ["#ebook_shipperphone"]
                },
                ebook_Selleremail: {
                    required: function (element) {
                        return($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
                },
                ebook_Sellername: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                                                // return  $("#ebook_Sellername").val() != "" || $("#ebook_Selleraddress").val() != "" || $("#ebook_Sellercountry_name").val() != "" || $("#ebook_Sellercity_desc").val() != "" || $("#ebook_Sellerphone").val() != "" || $("#ebook_Selleraddress1").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Sellerfax").val() != "") || $("#ebook_Selleremail").val() != "" || $("#ebook_Sellerstate").val() != "" || <%=custMandatory.contains("SL")%>;
                    }
//                    
                },
                ebook_Selleraddress: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
//                    
                },

                ebook_Sellercountry_name: {
                    required: function (element) {
                        return ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
//                    
                },
                ebook_Sellercity_desc: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
//                    
                },
                ebook_Sellerphone: {
                    validnumbers: "#ebook_Sellerphone",
                    ZeroTest: "#ebook_Sellerphone",
                    phonefax: ["#ebook_Sellerphone"],
                    required: function (element) {
                        return($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 &&  isNewCustomer("ebook_Sellercode") )
                    }
//                   
                },
                ebook_Buyeremail: {
                    required: function (element) {
                        return($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
                },
                ebook_Buyername: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4)
                    }
//                    
                },
                ebook_Buyeraddress: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4)
                    }
//                    
                },

                ebook_Buyercountry_name: {
                    required: function (element) {
                        return ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
//                    
                },
                ebook_Buyercity_desc: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
//                    
                },
                ebook_Buyerphone: {
                    validnumbers: "#ebook_Buyerphone",
                    ZeroTest: "#ebook_Buyerphone",
                    phonefax: ["#ebook_Buyerphone"],
                    required: function (element) {
                        return($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 && isNewCustomer("ebook_Buyercode"))
                    }
//                   
                },
                ebook_Buyerfax: {
                    validnumbers: "#ebook_Buyerfax",
                    ZeroTest: "#ebook_Buyerfax",
                    phonefax: ["#ebook_Buyerfax"]
                }, ebook_Sellerfax: {
                    validnumbers: "#ebook_Sellerfax",
                    ZeroTest: "#ebook_Sellerfax",
                    phonefax: ["#ebook_Sellerfax"]
                },
                agreetypeperson: {
                    required: function (element) {
                        return($("#agreetypeperson").length > 0 && $("#ebook_agreename").val().trim().length > 0)
                    }
                },
                bookingtypeperson: {
                    required: function (element) {
                        return($("#bookingtypeperson").length > 0 && $("#ebook_bookingname").val().trim().length > 0)
                    }
                },
                shippertypeperson: {
                    required: function (element) {
                        return($("#shippertypeperson").length > 0 &&  $("#ebook_shippername").val().trim().length > 0);
                    }
                },
                consigneetypeperson: {
                    required: function (element) {
                        return($("#consigneetypeperson").length > 0  && $("#ebook_consigneename").val().trim().length > 0)
                    }
                },
                notifytypeperson: {
                    required: function (element) {
                        return($("#notifytypeperson").length > 0 && $("#ebook_notifyname").val().trim().length > 0)
                    }
                },
                notifytypeperson_n1: {
                    required: function (element) {
                        return($("#notifytypeperson_n1").length > 0  && $("#ebook_notifyname_n1").val().trim().length > 0)
                    }
                },
                forwardertypeperson: {
                    required: function (element) {
                        return($("#forwardertypeperson").length > 0  && $("#ebook_forwardername").val().trim().length > 0)
                    }
                },
                Warehousetypeperson: {
                    required: function (element) {
                        return($("#Warehousetypeperson").length > 0 && $("#ebook_Warehousename").val().trim().length > 0)
                    }
                },
                NamedCustomertypeperson: {
                    required: function (element) {
                        return($("#NamedCustomertypeperson").length > 0  && $("#ebook_NamedCustomername").val().trim().length > 0)
                    }
                },
                ebook_Sellerpersontype: {
                    required: function (element) {
                        return(  $("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
                },
                ebook_Buyerpersontype: {
                    required: function (element) {
                        return($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4)
                    }
                },
                ebook_Buyerzipcode: {
                    required: function (element) {
                        return($("#euport").length > 0 && $("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4)
                    }
                },
                ebook_Sellerzipcode: {
                    required: function (element) {
                        return($("#euport").length > 0 && $("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() >= 4 )
                    }
                },
                ebook_bookingzipcode: {
                    required: function (element) {
                        return($("#ebook_bookingzipcode").length > 0  && $("#ebook_bookingname").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_agreezipcode: {
                    required: function (element) {
                        return($("#ebook_agreezipcode").length > 0  && $("#ebook_agreename").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_shipperzipcode: {
                    required: function (element) {
                        return($("#ebook_shipperzipcode").length > 0  && $("#ebook_shippername").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },

                ebook_Warehousezipcode: {
                    required: function (element) {
                        return($("#ebook_Warehousezipcode").length > 0  && $("#ebook_Warehousename").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_NamedCustomerzipcode: {
                    required: function (element) {
                        return($("#ebook_NamedCustomerzipcode").length > 0  && $("#ebook_NamedCustomername").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_forwarderzipcode: {
                    required: function (element) {
                        return($("#ebook_forwarderzipcode").length > 0  && $("#ebook_forwardername").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_notifyzipcode_n1: {
                    required: function (element) {
                        return($("#ebook_notifyzipcode_n1").length > 0  && $("#ebook_notifyname_n1").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_notifyzipcode: {
                    required: function (element) {
                        return($("#ebook_notifyzipcode").length > 0 && $("#ebook_notifyname").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_consigneezipcode: {
                    required: function (element) {
                        return($("#ebook_consigneezipcode").length > 0 && $("#ebook_consigneename").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y')
                    }
                },
                ebook_agreeeori: {
                    required: function (element) {
                        return($("#ebook_agreeeori").length > 0 && $("#ebook_agreecountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_agreecountry_name'))
                    }
                },
                ebook_shippereori: {
                    required: function (element) {
                        return($("#ebook_shippereori").length > 0 && $("#ebook_shippercountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_shippercountry_name'))
                    }
                },
                ebook_consigneeeori: {
                    required: function (element) {
                        return($("#ebook_consigneeeori").length > 0 && $("#ebook_consigneecountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_consigneecountry_name'))
                    }
                },
                ebook_notifyeori: {
                    required: function (element) {
                        return($("#ebook_notifyeori").length > 0 && $("#ebook_notifycountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_notifycountry_name'))
                    }
                },
                ebook_notifyeori_n1: {
                    required: function (element) {
                        return($("#ebook_notifyeori_n1").length > 0 && $("#ebook_notifycountry_name_n1").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_notifycountry_name_n1'))
                    }
                },
                ebook_forwardereori: {
                    required: function (element) {
                        return($("#ebook_forwardereori").length > 0 && $("#ebook_forwardercountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_forwardercountry_name'))
                    }
                },
                ebook_NamedCustomereori: {
                    required: function (element) {
                        return($("#ebook_NamedCustomereori").length > 0 && $("#ebook_NamedCustomercountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_NamedCustomercountry_name'))
                    }
                },
                ebook_Warehouseeori: {
                    required: function (element) {
                        return($("#ebook_Warehouseeori").length > 0 && $("#ebook_Warehousecountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_Warehousecountry_name'))
                    }
                },
                ebook_Sellereori: {
                    required: function (element) {
                        return($("#ebook_Sellereori").length > 0 && $("#ebook_Sellercountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_Sellercountry_name')&& $("#stepWizardNewIndex").val() >= 4 && $("#ensFillingType").val() == 'Single Filing' )
                    }
                },
                ebook_Buyereori: {
                    required: function (element) {
                        return($("#ebook_Buyereori").length > 0 && $("#ebook_Buyercountry_name").val().length > 0 && $("#euport").length > 0 && $("#euport").val() == 'Y' && checkEUCountry('ebook_Buyercountry_name') && $("#stepWizardNewIndex").val() >= 4 && $("#ensFillingType").val() == 'Single Filing' )
                    }
                },
                declarantName: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4);
                    }
                },
                declarantAddress: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantAddress1: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantCity: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantCountry: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantState: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantTelephone: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantEORI: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantEmail: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantFillingType: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                },
                declarantZipcode: {
                    required: function (element) {
                        return  ($("#euport").val() == 'Y'&& $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() >= 4 );
                    }
                }
            },
            messages: {
                ebook_origin_country: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    notEqualTo: "<bean:message key="ecom.msg.bkgdiffport" bundle="<%=clientcode%>"/>"
                },
                ebook_delivery_country: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    notEqualTo: "<bean:message key="ecom.msg.bkgdiffport" bundle="<%=clientcode%>"/>"
                },
                ebook_agreecountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    remote: "<bean:message key="error.reg.country_invalid" bundle="<%=clientcode%>"/>"
                },
                ebook_shippercountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },

                consigneeSelect: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_consigneename: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_consigneeaddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_consigneeaddress1: {
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_consigneecountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_consigneecity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_consigneephone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                },
                ebook_consigneefax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },

                ebook_consigneeemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",

                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_agreeemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>"
                },
                ebook_shipperemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },

                ebook_notifyname: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyaddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>", EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyaddress1: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifycountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifycity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyfax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyname_n1: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyaddress_n1: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyaddress1_n1: {
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifycountry_name_n1: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifycity_desc_n1: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyphone_n1: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyfax_n1: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_notifyemail_n1: {
                    EqualTo_concheck: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    EqualTo_con: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_agreefax: {
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_agreephone: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"

                },

                ebook_forwardername: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                },
                ebook_forwarderaddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                },

                ebook_forwardercountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",

                },
                ebook_forwardercity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",

                },
                ebook_forwarderphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_forwarderfax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_Warehousename: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                },
                ebook_Warehouseaddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                },

                ebook_Warehousecountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",

                },
                ebook_Warehousecity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",

                },
                ebook_Warehousephone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_Warehousefax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",

                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_NamedCustomername: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                },
                ebook_NamedCustomeraddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                },

                ebook_NamedCustomercountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",

                },
                ebook_NamedCustomercity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",

                },
                ebook_NamedCustomerphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_NamedCustomerfax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_shipperfax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_shipperphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_forwarderemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"

                },
                ebook_Warehouseemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"

                },
                ebook_NamedCustomeremail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"

                },
                ebook_agreename: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },

                ebook_agreeaddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },

                ebook_agreecity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_shippername: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_shipperaddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },

                ebook_shippercity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                termsagree: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                haulcontactphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"

                },
                ebook_Selleremail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"

                },
                ebook_Sellername: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_Selleraddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },

                ebook_Sellercountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_Sellercity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"

                },
                ebook_Sellerphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_Sellerfax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                ebook_Buyeremail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"

                },
                ebook_Buyername: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_Buyeraddress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },

                ebook_Buyercountry_name: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_Buyercity_desc: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"

                },
                ebook_Buyerphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                ebook_Buyerfax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                }
            },
            highlight: function (element) {
                $(element).closest('.form-control').addClass('has-error');
            },
            unhighlight: function (element) {
                $(element).closest('.form-control').removeClass('has-error');
            },
            errorElement: 'span',
            errorClass: 'help-block',
            errorPlacement: function (error, element) {
                var id = element.attr("id") + "-error";
                if ($("#" + id).length) {
                 $("#" + id).text(error.text());
                }else{
                if (element.parents('.input-group').is('.input-group')) {
                    error.insertAfter(element.parents('.input-group'));
                } else if (element.parents('.inc_dec').is('.inc_dec')) {
                    error.insertAfter(element.parents('.inc_dec'));
                } else {
                    error.insertAfter(element);
                }
            }
            }
        });
        $("#newhaulagepickuploc").validate({
            rules: {
                newhauladdress: {
                    required: true
                },
                newhaulcontphone: {
                    validnumbers: "#ebook_Warehousefax",
                    ZeroTest: "#ebook_Warehousefax",
                    phonefax: ["#ebook_Warehousefax"]
                }
            },
            messages: {
                newhauladdress: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                newhaulcontphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                $('#haulpickuploccode').val('');
                $("#haulageShipper").prop("checked", false);
                $("#haulpickuplocname").val($("#newhaulpickuploc").val());
                $("#hauladdress").val($("#newhauladdress").val());
                $("#haulcontactperson").val($("#newhaulcontperson").val());
                $("#haulcontactphone").val($("#newhaulcontphone").val());
                $("#haulcontactemail").va;
                ($("#newhaulcontemail").val());
                $('#newhaulagemodal').modal('hide');
                return false;
            }
        });

        var form = $("#AmendmentBookingFormBean").show();
        form.steps({
            headerTag: "h6",
            bodyTag: "fieldset",
            autoFocus: true,
            enableAllSteps: true,
            transitionEffect: "fade",
            transitionEffectSpeed: 500,
            titleTemplate: '<span class="step">#index#</span> #title#',
            labels: {
                finish: "<i class=\"fa fa-check\"></i> <bean:message key="ecom.but.sendtoagency" bundle="<%=clientcode%>"/>",
                next: "<bean:message key="ecom.next" bundle="<%=clientcode%>"/> <span class=\"glyphicon glyphicon-arrow-right\"></span>",
                previous: " <span class=\"glyphicon glyphicon-arrow-left\"></span><bean:message key="ecom.prev" bundle="<%=clientcode%>"/>"
            },
            onInit: function (event, current)
            {
                //$(window).load(function () {
                RoutingOrigin = updateMaskPortList(RoutingOrigin);
                portlist = updateMaskPortList(portlist);
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: RoutingOrigin
                });
                ports.initialize();
                $('#ebook_origin_country').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {name: 'ports',
                            limit: 20,
                            displayKey: function (portlist) {
                                return portlist.PortCode + " - " + portlist.PortName;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden").val(datum.PortName);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden").val(datum.PortName);
                    //getEmptyPickUp(datum.PortCode);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_origin_country').typeahead('val', "");
                        $(obj.currentTarget).val("");
                        setEmptyValForEmptypickup();
                        onChangeEmptyPickup("");
                    }
                });
                // });
                //  $(window).load(function () {
                RoutingDelivery = updateMaskPortList(RoutingDelivery);
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: RoutingDelivery
                });
                ports.initialize();
                $('#ebook_delivery_country').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {name: 'ports',
                            limit: 20,
                            displayKey: function (portlist) {
                                return portlist.PortCode + " - " + portlist.PortName;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden1").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden1").val(datum.PortName);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden1").val(datum.PortName);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden1").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_delivery_country').typeahead('val', "");
                        $(obj.currentTarget).val("");
                        setEmptyValPodIntransit();
                    }
                });
                //  });


                getPort('ebook_origin_country', 'ebook_origin_desc', 'ebook_origin', defOrgDesHaulValue);
                getPort('ebook_delivery_country', 'ebook_delivery_desc', 'ebook_delivery', defOrgDesHaulValue);
                //toSetCarriageCntct();
                if (current > 0) {
                    $('.next').html("<bean:message key="ecom.next" bundle="<%=clientcode%>"/> <span class=\"glyphicon glyphicon-arrow-right\"></span>").removeClass("selectroutepop");
                } else {
                    $('.next').html($('#selectvslroute').text() + " <span class=\"glyphicon glyphicon-arrow-right\"></span>").addClass("selectroutepop");
                }
                if (document.getElementById('ebook_masthdn').value == "Y")
                {
                    $('.next').html("<bean:message key="ecom.next" bundle="<%=clientcode%>"/> <span class=\"glyphicon glyphicon-arrow-right\"></span>").removeClass("selectroutepop");
                } else {
                    $('.next').html($('#selectvslroute').text() + " <span class=\"glyphicon glyphicon-arrow-right\"></span>").addClass("selectroutepop");
                }
//agree country
                //   $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_agreecountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden2").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden2").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_agreecountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                $('#ebook_NamedCustomercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()}).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden2").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden2").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_NamedCustomercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                // });
                // ebook_shippercountry_name

                //  $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_shippercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden2").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden2").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_shippercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //  });
                // ebook_consigneecountry_name
                //   $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_consigneecountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden3").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden3").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_consigneecountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_Sellercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden3").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden3").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_Sellercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_Buyercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden3").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden3").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_Buyercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                // });
                // ebook_notifycountry_name
                //  $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_notifycountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden4").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden4").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden4").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden4").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_notifycountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //  });
                // ebook_notifycountry_name_n1
                // $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_notifycountry_name_n1').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden5").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden5").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden5").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden5").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_notifycountry_name_n1').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //  });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_forwardercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()}).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden2").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden2").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_forwardercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_Warehousecountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()}).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden2").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden2").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_Warehousecountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                <%if ("Y".equalsIgnoreCase(GENS)) {%>
        var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#declarantCountry').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'countrysl',
                            limit: 5,
                            displayKey: function (counlist) {
                                return  counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden17").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden17").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden17").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden17").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#declarantCountry').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
        <%}%>

                var newdate = new Date();
                newdate.setDate(newdate.getDate() + 7);
                $('#ebook_date').datepicker({format: "<%=strDateFormat%>", autoclose: true, clearBtn: true, todayHighlight: true, startDate: "-infinity"}).on('changeDate', function (ev) {
                    if ($('#ebook_date').val() != $('#changeDateValue').val()) {
                        $(".routingdiv").hide();
                        $("#routingdetails").html("");
                        document.getElementById('ebook_masthdn').value = "N";
                        document.getElementById("selectroute").value = '0';
                        $('#changeDateValue').val($('#ebook_date').val());
                        //$('#routeshowdiv').show();
                        $('.next').html($('#selectvslroute').text() + " <span class=\"glyphicon glyphicon-arrow-right\"></span>").addClass("selectroutepop");
                        $('.next').removeClass("disabled");

                    }
                });
                $('#emptyPickUpDate').datepicker({format: "<%=strDateFormat%>", autoclose: true, clearBtn: true, todayHighlight: true, startDate: "-infinity"});
                if (document.getElementById('ebook_masthdn').value == 'N') {
                    $('#ebook_date').datepicker("setDate", new Date());
                    //$('#routeshowdiv').show();
                    $("#routingdetails").html("");
                    $(".routingdiv").hide();
                    document.getElementById("selectroute").value = '0';
                    $('.next').html($('#selectvslroute').text() + " <span class=\"glyphicon glyphicon-arrow-right\"></span>").addClass("selectroutepop");
                    $('.next').removeClass("disabled");
                }
                $('#ebook_origin_country').keyup(function () {
                    $('#invalidpolport').css('display', 'none');
                });
                $('#ebook_delivery_country').keyup(function () {
                    $('#invalidpodport').css('display', 'none');
                });
                loadEmptypickup($('#ebook_origin_country').val());
                loadPODIntransit($('#ebook_delivery_country').val());
                var haulage_type = $('ebook_haulagetype').val();

                if (haulage_type == "L" || haulage_type == "D")
                {
                    $("#ebook_drop_date").attr("disabled", false);
                } else if (haulage_type == "P")
                {
                    $("#ebook_pickup_date").attr("disabled", false);
                } else if (haulage_type == "B")
                {
                    $("#ebook_pickup_date").attr("disabled", false);
                    $("#ebook_drop_date").attr("disabled", false);
                }

                if (document.getElementById('ebook_origin_country').value != null) {
                    var country = document.getElementById('ebook_origin_country').value
                    var trimmedCountry = country.slice(0, 2);
                    console.log('Trimmed country - - --->' + trimmedCountry);
                    if ($('#shipperReference').length > 0) {
                        if (trimmedCountry == 'AE') {
                            $("#shipperReference").attr("disabled", false);
                            $('#shipperReference').attr("required", true);
                            console.log('inside shipper ref');

                            var dpwShipperRefLabel = $('#DPWShipperRef');
                            var currentText = dpwShipperRefLabel.text();
                            if (dpwShipperRefLabel.length) {
                                dpwShipperRefLabel.html(currentText + ' <span class="mandatory">*</span>');
                            }
                        } else {
                            $('#shipperReference').attr("disabled", true);
                            $('#shipperReference').attr("required", false);
                            console.log('else AE');

                            var dpwShipperRefLabel = $('#DPWShipperRef');
                            if (dpwShipperRefLabel.length) {
                                var updatedText = dpwShipperRefLabel.html().replace(' <span class="mandatory">*</span>', '');
                                dpwShipperRefLabel.html(updatedText);
                            }
                        }
                    }
                }
    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                if ($('#ebook_agreeeori').length > 0 && $('#ebook_agreename').val().length > 0 && $('#ebook_agreeeori').val().length > 0 || $('#agreetypeperson').length > 0 &&  $('#agreetypeperson').val().length > 0) {
                    $('#ebook_agreeeori').show();
                    $('#agreetypeperson').show();
                }
                if ($('#ebook_shippereori').length > 0 && $('#ebook_shippername').val().length > 0 && $('#ebook_shippereori').val().length > 0 ||$('#shippertypeperson').length > 0 &&  $('#shippertypeperson').val().length > 0) {
                    $('#shippertypeperson').show();
                    $('#ebook_shippereori').show();
                }
                if ($('#ebook_consigneeeori').length > 0 && $('#ebook_consigneename').val().length > 0 && $('#ebook_consigneeeori').val().length > 0 || $('#consigneetypeperson').length > 0 &&  $('#consigneetypeperson').val().length > 0) {
                    $('#ebook_consigneeeori').show();
                    $('#consigneetypeperson').show();
                }
                if ($('#ebook_notifyeori') && $('#ebook_notifyname').val().length > 0 && $('#ebook_notifyeori').val().length > 0 || $('#notifytypeperson').length > 0 &&  $('#notifytypeperson').val().length > 0) {
                    $('#ebook_notifyeori').show();
                    $('#notifytypeperson').show();
                }
//      console.log($('#ebook_notifyeori_n1').val()+'///'+$('#ebook_notifyname_n1').val()+'//'+$('#notify1Select').val()='');
                if ($('#ebook_notifyeori_n1').length > 0 && $('#ebook_notifyname_n1').val().length > 0 && $('#ebook_notifyeori_n1').val().length > 0 || $('#notifytypeperson_n1').length > 0 && $('#notifytypeperson_n1').val().length > 0) {
                    console.log($('#ebook_notifyeori_n1').val());
                    $('#ebook_notifyeori_n1').show();
                    $('#notifytypeperson_n1').show();
                }
//     console.log($('#forwarderSelect').val()+'///'+$('#ebook_forwardername').val()+'//'+$('#ebook_forwardereori').val()='');
                if ($('#ebook_forwardereori').length > 0 && $('#ebook_forwardername').val().length > 0 && $('#ebook_forwardereori').val().length > 0 ||$('#forwardertypeperson').length > 0 &&  $('#forwardertypeperson').val().length > 0) {
                    console.log('inside');
                    $('#ebook_forwardereori').show();
                    $('#forwardertypeperson').show();
                }
                if ($('#ebook_NamedCustomereori').length > 0 && $('#ebook_NamedCustomername').val().length > 0 && $('#ebook_NamedCustomereori').val().length > 0 ||$('#NamedCustomertypeperson').length > 0 &&  $('#NamedCustomertypeperson').val().length > 0) {
                    $('#ebook_NamedCustomereori').show();
                    $('#NamedCustomertypeperson').show();
                }
//    if ($('#ebook_Warehouseeori')&& $('#ebook_Warehousename').val().length >0&& $('#ebook_Warehouseeori').val().length >0||$('#Warehousetypeperson').val().length >0) {
//        $('#ebook_Warehouseeori').show();
//        $('#Warehousetypeperson').show();
//    }

    <%}%>
    <%if ("Y".equalsIgnoreCase(TENS)) {%>
            chkIsEuPort();
    <%}%>
    <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
           setENSType();
    <%}%>
    <%if ("Y".equalsIgnoreCase(GENS)) {%>
        chkEU_ENSDeclaration();
    <%}%>
    <%if ("Y".equalsIgnoreCase(Enable_T2LFiling)) {%>
                var pol = $('#ebook_origin_country').val();
                var pod = $('#ebook_delivery_country').val();
                setT2LFiling(pol, pod);
    <%}%>
    <%if ("Y".equalsIgnoreCase(Enable_SISubmit_Party)) {%>
                              enableSiSubmitParty();
    <%}%>
  
<%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
    
        addCustRowAjax('Seller','');
        addCustRowAjax('Buyer','');
    <%}%>

    toenableshipperref('ebook_origin_country','ebook_origin_desc');
            },
            onStepChanging: function (event, currentIndex, newIndex)
            {
                if(newIndex == 1){
                    chkEU_ENSDeclaration();
                    if($('#ebook_buyerphone, #ebook_sellerphone').length > 0){
                    $('#ebook_buyerfax, #ebook_buyerphone, #ebook_sellerphone, #ebook_sellerfax').each(function () {
                        $(this).val($(this).val().replaceAll("[^0-9+\\-]", "").replaceAll("(?<!^)\\+", "").replaceAll("\\+-", "+").replaceAll("--+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
                    });
                   }
                }   
                form.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly]";

                if (currentIndex == 0 && form.valid() && $("#selectroute").val() < 1 && document.getElementById('ebook_masthdn').value != "Y")
                {
                    $('.next').html("<i class='fa fa-spinner fa-spin'></i> Loading...");
                    $('.next').addClass("disabled");
                    getVesselSchedule();
                    //toSetCarriageCntct();
                    return false;
                }
                if (currentIndex > newIndex) {
                    $(".haulagetabdiv").css("display", "block");
                    return true;
                }
                if (newIndex == 1) {
                    disableCustAddButton();
                    callnatcodeMandatoryScript();
                    $('#addiDetails').addClass('show');
                    if (form.valid()) {
    <%if (!"TSH".equalsIgnoreCase(carrierCode)) {%>
                        $('#addiDetails').removeClass('show');
    <%}%>
                    }
                }
                                if (newIndex == 2) {
                    //callHSCodeMandatoryScript();
                    if(checkForDuplicateNames()){
                      return false;
                    }
                    setSellerNameSelectOption();
                }
                if (newIndex == 3 && ($('#lrpBookNo').val().trim().length > 0)) {
                    var contnovalidate = true;
                    var sicontnostrlis = "";
                    var sicontstarcut = 0;
                    $("#datatablebycargodiv").find('.containerclass').each(function () {
                        if (sicontstarcut != 0) {
                            sicontnostrlis = sicontnostrlis + ",";
                        }
                        sicontnostrlis = sicontnostrlis + $(this).val();
                        sicontstarcut = sicontstarcut + 1;
                    });
                    console.log("sicontnostrlis===> " + sicontnostrlis);
                    var contstrlis1;
                    var resultdata = checkSIContainerNoValidation(sicontnostrlis);
                    if (resultdata != '' && resultdata.length > 0) {
                        var contarr = resultdata.split('~~~');
                        if (contarr[0] == 'false') {
                            contstrlis1 = "The below container already used in other booking " + contarr[1];
                            contnovalidate = false;
                        } else if (contarr[0] == 'true') {
                            contnovalidate = true;
                        } else if (contarr[0] == 'error') {
                            contstrlis1 = "Error occured,Try again later.";
                            contnovalidate = false;
                        }
                    }

                    if (!contnovalidate) {
                        alert(contstrlis1);
                        return contnovalidate;
                    }
                }
    <% if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                if (newIndex == 3) {
                    $(".haulagetabdiv").css("display", "block");
                    var depdatearr = $('#hidETDDate').val().split(" ");
                    var depdate = depdatearr[0];
                    $("#hauldropdate").datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>", startDate: "-infinity", endDate: depdate});
                    $('#hauldropdatetime').timepicker();
                    $('#hauleqprowid').find('option').remove();
                    $('<option>').val("").text("").appendTo('#hauleqprowid');
                    $('#datatablebycargodiv').find('.preview').find(".containerclass").each(function () {
                        $('<option>').val($(this).val()).text($(this).val()).appendTo('#hauleqprowid');
                    });
                    getHaulageDocType("<%=custCode%>");
                    // getTMSClientDetails($("#ebook_origin").val());//GTGUA
                    //setShipperDetailsToHaulage();
                }
                if (currentIndex == 3) {
                    if ($("#hauleqprowid").val().length > 0 || $("#hauldropdate").val().length > 0 || $("#hauleqptype").val().length > 0 || $("#haulpickuplocname").val().length > 0 || $("#haulstopseq").val().length > 0) {
                        alert("Please add / reset the Haulage details");
                        return false;
                    } else {
                        $(".haulagetabdiv").css("display", "none");
                    }
                }
                if (newIndex == 4) {
                    // callDgClassMandatoryScript();
                    callMsdsMandatoryScript();
                }
                <%if ("Y".equalsIgnoreCase(GENS)) {%>
                if (newIndex > 4) {
                    if ($("#ensFillingType").val() == 'Single Filing' && $("#blTypeENS").val() != 'Straight BL') {
                        alert("Single Filing is applicable only for Straight BL.");
                        return false;
                    }
                    if ($("#ensFillingType").val() == 'Multiple Filing' && $("#blTypeENS").val() != 'Master BL') {
                        alert("Multiple Filing is applicable only for Master BL.");
                        return false;
                    }       
                }
    <%}%>
    <%} else if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
                /*if (newIndex == 3) {
                 setInsuranceDetails();
                 }*/
                var isinsavble = true;
                $("#datatablebycargodiv").find('.preview').each(function () {
                    if ('Y' == $(this).find('.inavailable').val()) {
                        isinsavble = false;
                    }
                });
                console.log('isinsavble=> ' + isinsavble);
                if (newIndex == 4 && isinsavble && $("#insurance_not_need").prop('checked') == false) {
                    alert("Do you want to continue without adding insurance.\nPlease check the 'I do not need insurance protection for my cargo'");
                    return false;
                }
    <% if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                if (newIndex == 5) {
                    if (typecheck != 'templateedit') {
                        //callDgClassMandatoryScript();
                        callMsdsMandatoryScript();
                    }
                }
    <%} else {%>

                if (newIndex == 4) {
                    if (typecheck != 'templateedit') {
                        //callDgClassMandatoryScript();
                        callMsdsMandatoryScript();
                    }
                }

    <%}
    } else if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                if (newIndex == 4) {
                    if (typecheck != 'templateedit') {
                        //callDgClassMandatoryScript();
                        callMsdsMandatoryScript();
                    }
                }
    <%} else {%>
                if (newIndex == 3) {
                    //callDgClassMandatoryScript();
                    callMsdsMandatoryScript();
                }
    <%}%>
                if (currentIndex == 0) {
                    var jshidetddate = $('#hidETDDate').val();
                    console.log('jshidetddate==>' + jshidetddate);
                    var jspickupdate = $('#emptyPickUpDate').val();
                    var checkDateDiff = true;
                    if (document.getElementById('ebook_masthdn').value == "Y") {
                        $.ajax({
                            type: "POST",
                            cache: false,
                            url: "ecomAjax?type=checktransitdatevalidation&date1=" + encodeURIComponent(jspickupdate) + "&date2=" + encodeURIComponent(jshidetddate),
                            async: false,
                            data: $(this).serializeArray(),
                            success: function (data) {
                                if (data == 'Y') {
                                    checkDateDiff = false;
                                    alert("<bean:message key="ecom.msg.pickanddeptdate" bundle="<%=clientcode%>"/>");
                                    return false;
                                }
                            },
                            error: function (xhr) {
                                if (xhr.status == 403) {
                                    window.location.href = "SessionExpired.jsp";
                                }
                            }
                        });
                    }
                    $('.commoditytypehead').each(function () {
                        $(this).typeahead('destroy');
                        if ($('#appversionchange').val() != $('#appVersion').val()) {
                            $(this).val('');
                        }
                    });
                    $('.hscodetypehead').each(function () {
                        $(this).typeahead('destroy');
                        if ($('#appversionchange').val() != $('#appVersion').val()) {
                            $(this).val('');
                        }
                        if ($('#appVersion').val() == '1') {
                            $(this).attr('maxlength', "100");
                        }
                    });
                    <%if (!("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode))) {%>
                    callHSCodeMandatoryScript();
                    <%}%>
    <%if ("Y".equalsIgnoreCase(Disable_CommodityGRP)) {%>
                    $('.commoditytypehead').prop('disabled', true);
    <%}%>
                    $('.commoditytypehead').each(function () {
                        var comid = $(this).attr("id");
                        var id = comid.replace("commodity", "");
                        var hidcomid = "hiddencomid" + id;
                        var hscodeid = "hscode" + id;
                        commodityload(comid, hidcomid, id);
                    });

                    $('.othercomdtyhideclass').each(function () {
                        if ($('#appversionchange').val() != $('#appVersion').val()) {
                            if ($('#appVersion').val() == '1') {
                                $(this).show();
                            } else {
                                $(this).hide();
                            }
                        }
                    });
                    $('.othercomdtyhideclass').each(function () {
                        console.log("entered in other comclassssssssssssssssss");
                        console.log("-----------------" + $('#appVersion').val());
                        if ($('#appVersion').val() != '1') {
                            console.log('iiiiiiiiiiiiiiiiiiiiii');
                            $(this).hide();
                        }
                    });
    <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
                    $("#bookingSelect > option").each(function () {
                        if (this.text == $('#ebook_bookingname').val())
                        {
                            $('#bookingSelect').val(this.value);
                        }
                    });
    <%}%>
                    $("#agreeSelect > option").each(function () {

                        if (this.text == $('#ebook_agreename').val())
                        {
                            $('#agreeSelect').val(this.value);
                        }

                    });
                    $("#consigneeSelect > option").each(function () {
                        if (this.text == $('#ebook_consigneename').val())
                        {
                            $('#consigneeSelect').val(this.value);
                        }

                    });
                    $("#shipperSelect > option").each(function () {
                        if (trim(this.text) == trim($('#ebook_shippername').val()))
                        {
                            $('#shipperSelect').val(this.value);
                        }
                    });
                    $("#notifySelect > option").each(function () {
                        if (this.text == $('#ebook_notifyname').val())
                        {
                            $('#notifySelect').val(this.value);
                        }

                    });
                    $("#forwarderSelect > option").each(function () {
                        if (this.text == $('#ebook_forwardername').val())
                        {
                            $('#forwarderSelect').val(this.value);
                        }
                    });
                    $("#WarehouseSelect > option").each(function () {
                        if (this.text == $('#ebook_Warehousename').val())
                        {
                            $('#WarehouseSelect').val(this.value);
                        }
                    });
                    $("#NamedCustomerSelect > option").each(function () {
                        if (this.text == $('#ebook_NamedCustomername').val())
                        {
                            $('#NamedCustomerSelect').val(this.value);
                        }
                    });

                    if (checkDateDiff == false) {
                        return false;
                    }
                }
                if (currentIndex == 1 && newIndex != 1)
                {
                    $('#collapseBookingParty,#collapseAgree,#collapseShipper,#collapseConsignee,#collapseNotify,#collapseNotify1,#collapseForwarder,#collapseWarehouse,#collapseNamedCustomer,#collapseBuyer,#collapseSeller').addClass('show');
                    $('#collapseBookingParty,#collapseAgree,#collapseShipper,#collapseConsignee,#collapseNotify,#collapseNotify1,#collapseForwarder,#collapseWarehouse,#collapseNamedCustomer,#collapseBuyer,#collapseSeller').parent().find(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
                    if (form.valid()) {
                        $('#collapseBookingParty,#collapseAgree,#collapseShipper,#collapseConsignee,#collapseNotify,#collapseNotify1,#collapseForwarder,#collapseWarehouse,#collapseNamedCustomer,#collapseBuyer,#collapseSeller').removeClass('show');
                        $('#collapseBookingParty,#collapseAgree,#collapseShipper,#collapseConsignee,#collapseNotify,#collapseNotify1,#collapseForwarder,#collapseWarehouse,#collapseNamedCustomer,#collapseBuyer,#collapseSeller').parent().find(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
                        //$("#collapseShipper").toggle('slow');
                    }
                }

    <%if ("Y".equalsIgnoreCase(enableCargoProtect) && ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess))) {%>
                if (newIndex == 6 && typecheck != 'templateedit') {
                    setBkgPreviewDetails('normal');
                    convertHaulagedetailstoJson();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
                if (newIndex == 5 && typecheck == 'templateedit') {
                    setBkgPreviewDetails();
                    convertHaulagedetailstoJson();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
    <%} else if ("GWF".equalsIgnoreCase(carrierCode) || "Y".equalsIgnoreCase(enableCargoProtect) || ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess))) {%>
                if (newIndex == 5) {
                    setBkgPreviewDetails('amendment');
                    convertHaulagedetailstoJson();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
    <% } else {%>
                if (newIndex == 4) {
                    setBkgPreviewDetails('amendment');
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
    <%}%>
                if (currentIndex < newIndex)
                {
                    form.find(".body:eq(" + newIndex + ") label.error").remove();
                    form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
                }
                setnamedisable();
                if ($('#hidCNToOrder').val() == 'Y') {
                    $('#consigneetoorder').prop("checked", true);
                    setInitToOrder('consignee');
                    $('#notifyconsig,#notifyconsig1').attr("disabled", true);
                }

                if (($('#shipperCheck').val() == 'B') || ($('#shipperCheck').val() == 'A')) {
                    $('#ebook_shippername,#ebook_shipperaddress,#ebook_shipperaddress1,#ebook_shippercountry_name,#ebook_shippercity_desc,#ebook_shipperphone').attr("readonly", true);
                    $('#ebook_shipperfax,#ebook_shipperemail,#ebook_shipperstate').attr("readonly", true);
                    $('#shipperSelect').attr("disabled", true);
                    $('#ebook_shippercountry_name').css("background-color", "#eee");
                    if ($('#shipperCheck').val() == 'B') {

                        $('#shipperCheck').val("B");
                        $('#shippersearch').attr('disabled', true);
                        $('#agreeShipper').prop("checked", true);
                    }
                    if ($('#shipperCheck').val() == 'A') {

                        $('#shipperCheck').val("A");
                        $('#agreementShipper').prop("checked", true);
                        $('#agreeShipper').prop("checked", false);

                    }
                }
                if ($('#notifyCheck').val() == 'Y') {
                    $('#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneestate').attr("readonly", true);
                    $('#ebook_consigneeemail,#ebook_notifyname,#ebook_notifyaddress,#ebook_notifyaddress1,#ebook_notifycountry_name,#ebook_notifycity_desc,#ebook_notifyphone,#ebook_notifystate').attr("readonly", true);
                    if($('#ebook_consigneezipcode').length > 0){
                        $('#ebook_consigneezipcode').attr("readonly", true);
                        $('#ebook_notifyzipcode').attr("readonly", true);
                    }
                    $('#ebook_notifyfax,#ebook_notifyemail').attr("readonly", true);
                    $('#notifySelect,#consigneeSelect').attr("disabled", true);
                    $('#ebook_consigneecountry_name,#ebook_notifycountry_name').css("background-color", "#eee");
                    $('#notifyCheck').val("Y");
                    $('#consigneetoorder').attr("disabled", true);
                    document.getElementById('notifyconsig').checked = true;
                    $('#consigneesearch,#notifysearch').attr('disabled', true);
                    if ($('#hidCNToOrder').val() == 'Y') {
                        $('#consigneetoorder').prop("checked", true);
                    }
                }
                if ($('#notify1Check').val() == 'Y') {
                    $('#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneeaddress1,#ebook_consigneecountry_name,#ebook_consigneecity_desc,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneestate').attr("readonly", true);
                    $('#ebook_consigneeemail,#ebook_notifyname_n1,#ebook_notifyaddress_n1,#ebook_notifyaddress1_n1,#ebook_notifycountry_name_n1,#ebook_notifycity_desc_n1,#ebook_notifyphone_n1,#ebook_notifystate_n1').attr("readonly", true);
                    if($('#ebook_consigneezipcode').length > 0){
                        $('#ebook_consigneezipcode').attr("readonly", true);
                        $('#ebook_notifyzipcode_n1').attr("readonly", true);
                    }
                    $('#ebook_notifyfax_n1,#ebook_notifyemail_n1').attr("readonly", true);
                    $('#notify1Select,#consigneeSelect').attr("disabled", true);
                    $('#ebook_consigneecountry_name,#ebook_notifycountry_name_n1').css("background-color", "#eee");
                    $('#notify1Check').val("Y");
                    $('#consigneetoorder').attr("disabled", true);
                    document.getElementById('notifyconsig1').checked = true;
                    $('#consigneesearch,#notifysearch1').attr('disabled', true);
                    if ($('#hidCNToOrder').val() == 'Y') {
                        $('#consigneetoorder').prop("checked", true);
                    }
                }
                $('.mintempclass').each(function () {
                    var id = $(this).attr('id');
                    id = id.replace("minTemp", "");
                    $(this).rules('add', {
                        minTemp: ["#maxTemp" + id],
                        messages: {
                            minTemp: "<bean:message key="ecom.msg.mintemp" bundle="<%=clientcode%>"/>"
                        }});
                });
                /* $('.grossWeightCheck').each(function () {
                 var id = $(this).attr('id');
                 id = id.replace("cargoWt", "");
                 if ($('#cargoWtUnit' + id).val() == 'T') {
                 $(this).rules('remove', 'grossWeightKilo');
                 $(this).rules('add', {
                 grossWeightTon: ["#grossWt" + id],
                 messages: {
                 grossWeightTon: "<bean:message key="ecom.msg.grossweightton" bundle="<%=clientcode%>"/>"
                 }
                 });
                 } else {
                 $(this).rules('remove', 'grossWeightTon');
                 $(this).rules('add', {
                 grossWeightKilo: ["#grossWt" + id],
                 messages: {
                 grossWeightKilo: "<bean:message key="ecom.msg.grossweightkilos" bundle="<%=clientcode%>"/>"
                 }
                 });
                 }
                 });*/
                $('.maxtempclass').each(function () {
                    var id = $(this).attr('id');
                    id = id.replace("maxTemp", "");
                    $(this).rules('add', {
                        maxTemp: ["#minTemp" + id],
                        messages: {
                            maxTemp: "<bean:message key="ecom.msg.maxtemp" bundle="<%=clientcode%>"/>"
                        }
                    });
                });
                $('.settempclass').each(function () {
                    var id = $(this).attr('id');
                    id = id.replace("presetTemp", "");
                    $(this).rules('add', {
                        minTemp: ["#maxTemp" + id],
                        maxTemp: ["#minTemp" + id],
                        messages: {
                            minTemp: "<bean:message key="ecom.msg.presettemp" bundle="<%=clientcode%>"/>",
                            maxTemp: "<bean:message key="ecom.msg.presettemp" bundle="<%=clientcode%>"/>"
                        }
                    });
                });
                $('.zerocheck').each(function () {
                    $(this).rules('add', {
                        zerocheck: true,
                        messages: {
                            zerocheck: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>"
                        }
                    });
                });
                $('.numbervalpoint').each(function () {
                    $(this).rules('add', {
                        numbervalpoint: true,
                        messages: {
                            numbervalpoint: "<bean:message key="ecom.msg.invalidnum" bundle="<%=clientcode%>"/>"
                        }
                    });
                });

                /*      if (form.valid() && currentIndex == 2 && newIndex > 2) {
                 var checdupcntr = true;
                 var cntrarray = new Array();
                 var cntrarrcount = 0;
                 $('.checkcntrdupclass').each(function () {
                 if ($(this).hasClass("required")) {
                 cntrarray[cntrarrcount] = $(this).val();
                 cntrarrcount++;
                 }
                 
                 });
                 for (var ii = 0; ii < cntrarray.length; ii++) {
                 for (var jj = ii + 1; jj < cntrarray.length; jj++) {
                 if (cntrarray[ii].length > 0 && cntrarray[jj].length > 0 && (cntrarray[ii] == cntrarray[jj])) {
                 checdupcntr = false;
                 }
                 }
                 }
                 if (!checdupcntr) {
                 alert("Same container number cannot be added twice.");
                 return checdupcntr;
                 }
                 } */
           
                $('.containerclass').each(function () {
                    $(this).rules('add', {
                        checkdupcontainer: true,
                        messages: {
                            checkdupcontainer: "<bean:message key="ecom.msg.dupcontainer" bundle="<%=clientcode%>"/>"
                        }
                    });
                });
               
                if ((currentIndex > newIndex) && form.valid())
                {
                    return true;
                }
               
                return form.valid();
            },
            onStepChanged: function (event, currentIndex, priorIndex) {

                $('#stepWizardNewIndex').val(currentIndex);


    <% if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
                if (currentIndex == 3) {
                    setInsuranceDetails();
                    loadQuoteDetails();
                }
    <% if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                if (currentIndex == 4) {
                    loadCargoProtect('edit');
                }
    <%}
    } else if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                if (currentIndex == 3) {
                    loadCargoProtect('edit');
                }
    <%}%>
            }, onFinishing: function (event, currentIndex)
            {
                if ($('#bookingtable_new tr').length == 1) {
                    $('#bookingcargoerror').show()
                    return false;
                }
                  validationENSDocType('sellerCode', 'sellersName', 'Seller'); 
                  validationENSDocType('buyersCode', 'buyersName', 'Buyer');
                $(".haulagetabdiv").find('.required').removeClass("required");
                var ret = false;
                if (form.valid()) {
                    form.validate().settings.ignore = ":disabled,[readonly=readonly]";
                    ret = form.valid();
                    if (ret == false) {
                        alert("<bean:message key="ecom.msg.fieldmissing" bundle="<%=clientcode%>"/>");
                    }
                }
                if ($('#cargoprotectid').length > 0 && $('#temp_cargoprotectid').val().length > 0) {
                    $('#cargoprotectid').val($('#temp_cargoprotectid').val());
                }
                return ret;
            },
            onFinished: function (event, currentIndex)
            {
                if (isKycPending === 'Y') {
                    $('#kycPendingModal').modal('show');
                    return;
                }
    <% if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                showtermsconditions();
    <%} else {%>
                if (checkMailConfig == 'Y') {
                    $('#ccalertmoal').modal('show');
                } else {
                    submitwithoutcc();
                }
    <%}%>
            }
        });
        $(".steps li").addClass("done");
        $(".steps li.first").removeClass("done");
        //$(".steps li").removeClass('disabled');
        $('#ebook_origin_country').on('typeahead:selected', function (evt, item) {
            $('.help-block').html("");
        });
        $('#ebook_delivery_country').on('typeahead:selected', function (evt, item) {
            $('.help-block').html("");
        });
    });
    function submitwithcc() {
        $('#ccalertmoal').modal('hide');
        $('#ccmailidmodal').val("");
        if ($('#ccMailId').val() != null && $('#ccMailId').val() != '') {
            $('#ccmailidmodal').val($('#ccMailId').val());
        } else if (ccMailId != '' && ccMailId != 'NO DATA') {
            $('#ccmailidmodal').val(ccMailId);
        }
        $('#bkgccmailmodal').modal('show');
    }
    function submitwithoutcc() {
        $('#ccMailId').val("");
        //callLoading();
        $('.eqpstatusclass').attr("disabled", false);
        $(".eqpstatusclass").each(function () {
            $(this).attr("disabled", false);
        });
        if ($('#podintransit').val() != null && $('#podintransit').val() != '') {
            $('#hidPodIntransitName').val(trim($('#podintransit option:selected').text()));
        }
        if ($('#ebook_haulagetype').val() != null && $('#ebook_haulagetype').val() != '' && $('#ebook_haulagetype').val() != 'undefined' && $('#ebook_haulagetype').val() != undefined) {
            $('#ebook_haulageDesc').val(trim($('#ebook_haulagetype option:selected').text()));
        }
        $('#ebook_origin_country,#ebook_delivery_country').attr("disabled", false);
        $('#agreeSelect,#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate').attr("readonly", false);
        if($('#ebook_agreezipcode').length > 0){
            $('#ebook_agreezipcode').attr("readonly", false);
        }
        //  $('#forwarderSelect,#ebook_forwardername,#ebook_forwarderaddress,#ebook_forwarderaddress1,#ebook_forwardercountry_name,#ebook_forwardercity_desc,#ebook_forwarderphone,#ebook_forwarderfax,#ebook_forwarderemail').attr("readonly", false);
        // $('#WarehouseSelect,#ebook_Warehousename,#ebook_Warehouseaddress,#ebook_Warehouseaddress1,#ebook_Warehousecountry_name,#ebook_Warehousecity_desc,#ebook_Warehousephone,#ebook_Warehousefax,#ebook_Warehouseemail').attr("readonly", false);
        $('#preCarriage,#onCarriage').attr("disabled", false);
        $('.commoditytypehead').prop('disabled', false);
    <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
        $('#bookingSelect,#ebook_bookingname,#ebook_bookingaddress,#ebook_bookingaddress1,#ebook_bookingcountry_name,#ebook_bookingcity_desc,#ebook_bookingphone,#ebook_bookingfax,#ebook_bookingemail,#ebook_bookingstate').attr("readonly", false);
        if($('#ebook_bookingzipcode').length > 0){
            $('#ebook_bookingzipcode').attr("readonly", false);
        }
        <%}%>
        document.getElementById('AmendmentBookingFormBean').action = "amendmentbookingsave.do";
        document.getElementById('AmendmentBookingFormBean').submit();
    }
    function submitBooking() {
        var checksubmit = true;
        if ($('#ccmailidmodal').val() != '') {
            if (!validateDupMailId(trim($('#ccmailidmodal').val()))) {
                if (!validEmailCheckToCommon(trim($('#ccmailidmodal').val()))) {
                    alert("<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>");
                    checksubmit = false;
                }
            } else {
                alert("<bean:message key="ecom.msg.repeatmailid" bundle="<%=clientcode%>"/>");
                checksubmit = false;
            }
        } else {
            alert('<bean:message key="ecom.msg.ccmailid" bundle="<%=clientcode%>"/>');
            checksubmit = false;
        }
        //alert(cclist);
        if (checksubmit) {
            splitCCMailId($('#ccmailidmodal').val());
            //$('#ccMailId').val($('#ccmailidmodal').val());
            //callLoading();
            $('.eqpstatusclass').attr("disabled", false);
            $(".eqpstatusclass").each(function () {
                $(this).attr("disabled", false);
            });
            if ($('#podintransit').val() != null && $('#podintransit').val() != '') {
                $('#hidPodIntransitName').val(trim($('#podintransit option:selected').text()));
            }
            if ($('#ebook_haulagetype').val() != null && $('#ebook_haulagetype').val() != '' && $('#ebook_haulagetype').val() != 'undefined' && $('#ebook_haulagetype').val() != undefined) {
                $('#ebook_haulageDesc').val(trim($('#ebook_haulagetype option:selected').text()));
            }
            $('#ebook_origin_country,#ebook_delivery_country').attr("disabled", false);
            $('#agreeSelect,#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate').attr("readonly", false);
            if($('#ebook_agreezipcode').length > 0){
                $('#ebook_agreezipcode').attr("readonly", false);
            }
            //  $('#forwarderSelect,#ebook_forwardername,#ebook_forwarderaddress,#ebook_forwarderaddress1,#ebook_forwardercountry_name,#ebook_forwardercity_desc,#ebook_forwarderphone,#ebook_forwarderfax,#ebook_forwarderemail').attr("readonly", false);
            //  $('#WarehouseSelect,#ebook_Warehousename,#ebook_Warehouseaddress,#ebook_Warehouseaddress1,#ebook_Warehousecountry_name,#ebook_Warehousecity_desc,#ebook_Warehousephone,#ebook_Warehousefax,#ebook_Warehouseemail').attr("readonly", false);
            $('#preCarriage,#onCarriage').attr("disabled", false);
            $('.commoditytypehead').prop('disabled', false);
    <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
            $('#bookingSelect,#ebook_bookingname,#ebook_bookingaddress,#ebook_bookingaddress1,#ebook_bookingcountry_name,#ebook_bookingcity_desc,#ebook_bookingphone,#ebook_bookingfax,#ebook_bookingemail,#ebook_bookingstate').attr("readonly", false);
            if($('#ebook_bookingzipcode').length > 0){
                $('#ebook_bookingzipcode').attr("readonly", false);
            }
            <%}%>
            document.getElementById('AmendmentBookingFormBean').action = "amendmentbookingsave.do";
            document.getElementById('AmendmentBookingFormBean').submit();
        }
    }
    function loadCutomerDetails(container)
    {
        $("#panel-" + container).toggle('slow').find('input').val('');
    }
    function hidecustomepanel(container)
    {
        $("#panel-" + container).toggle('slow').find('input').val('');
    }
    function setCountry_New(field1, field2) {
        if (document.getElementById(field1).value == "") {
            document.getElementById(field2).value = "";
        }
        if (document.getElementById(field1).value != "") {
            document.getElementById(field2).value = trim(document.getElementById(field1).value.split("-")[0]);
        }
    }
    function setCityDesc_New(field1, field2) {
        if (document.getElementById(field1).value != "") {
            document.getElementById(field2).value = document.getElementById(field1).value;
        }
    }
    $('#bookingpartynew').text($('#ebook_bookingparty_desc').val());
    document.getElementById('checkRoutePol').value = document.getElementById('ebook_origin').value;
    document.getElementById('checkRoutePod').value = document.getElementById('ebook_delivery').value;
    $(document).ready(function () {
        $("#collapseAgree,#collapseShipper,#collapseAgreementParty,#collapseConsignee,#collapseNotify,#collapseNotify1,#collapseForwarder,#collapseWarehouse,#collapseNamedCustomer,#collapseBuyer,#collapseSeller").on('shown.bs.collapse', function () {
            $(this).parent().find(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
        }).on('hidden.bs.collapse', function () {
            $(this).parent().find(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
        });
    });
    <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
    if (document.getElementById('bookingSelect').options.length == 2) {
        var text = document.getElementById('ebook_bookingname').value;
        var val = document.getElementById('ebook_bookingname').value + "~~" + document.getElementById('ebook_bookingcode').value + "~~" +
                document.getElementById('ebook_bookingaddress').value + "~~" + document.getElementById('ebook_bookingcity_desc').value + "~~" + document.getElementById('ebook_bookingcountry_name').value + "~~" + document.getElementById('ebook_bookingcountry').value + "~~" + document.getElementById('ebook_bookingemail').value + "~~" +
                document.getElementById('ebook_bookingphone').value + "~~" + document.getElementById('ebook_bookingfax').value + "~~" + document.getElementById('ebook_bookingaddress').value + "~~" + document.getElementById('ebook_bookingstate').value;
        document.getElementById('bookingSelect').options[parseInt(document.getElementById('bookingSelect').options.length) - 1] = new Option(document.getElementById('ebook_bookingname').value, val);
        document.getElementById('bookingSelect').options[parseInt(document.getElementById('bookingSelect').options.length)] = new Option("New Customer", "new");
        document.getElementById('bookingSelect').value = val;
        document.getElementById('bookingSelect').selectedIndex = 2;
    }
    <%}%>
</script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#appversionchange').val($('#appVersion').val());
        if ($('#lrpBookNo').val() == null || $('#lrpBookNo').val() == '') {
            if ($('#ebook_agreecode').val() != '') {
                $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate').attr("readonly", true);

            } else {
                $('#ebook_agreecountry_name').css("background-color", "white");
            }
            //$('#ebook_agreecountry_name').css("background-color", "black");
            //$('#agreeSelect,#agreesearch').attr('disabled', true);
        } else {
            if ($('#ebook_agreename').val() != null && $('#ebook_agreename').val() != '' && $('#ebook_agreeaddress').val() != null && $('#ebook_agreeaddress').val() != '') {
                $('#agreeSelect,#agreesearch').attr('disabled', true);
                $('#ebook_agreename,#ebook_agreeaddress,#ebook_agreeaddress1,#ebook_agreecountry_name,#ebook_agreecity_desc,#ebook_agreephone,#ebook_agreefax,#ebook_agreeemail,#ebook_agreestate').attr("readonly", true);
                // $('#ebook_agreecountry_name').css("background-color", "white");

            }
        }
//        if ($('#lrpBookNo').val() == null || $('#lrpBookNo').val() == '') {
//            if ($('#ebook_forwardercode').val() != '') {
//                $('#ebook_forwardername,#ebook_forwarderaddress,#ebook_forwarderaddress1,#ebook_forwardercountry_name,#ebook_forwardercity_desc,#ebook_forwarderphone,#ebook_forwarderfax,#ebook_forwarderemail').attr("readonly", true);
//            } else {
//                $('#ebook_forwardercountry_name').css("background-color", "white");
//            }
//            //$('#ebook_agreecountry_name').css("background-color", "black");
//            //$('#agreeSelect,#agreesearch').attr('disabled', true);
//        } else {
//            if ($('#ebook_forwardername').val() != null && $('#ebook_forwardername').val() != '' && $('#ebook_forwarderaddress').val() != null && $('#ebook_forwarderaddress').val() != '') {
//                $('#forwarderSelect,#forwardersearch').attr('disabled', true);
//                $('#ebook_forwardername,#ebook_forwarderaddress,#ebook_forwarderaddress1,#ebook_forwardercountry_name,#ebook_forwardercity_desc,#ebook_forwarderphone,#ebook_forwarderfax,#ebook_forwarderemail').attr("readonly", true);
//                // $('#ebook_agreecountry_name').css("background-color", "white");
//            }
//        }
//        if ($('#lrpBookNo').val() == null || $('#lrpBookNo').val() == '') {
//            if ($('#ebook_Warehousecode').val() != '') {
//                $('#ebook_Warehousename,#ebook_Warehouseaddress,#ebook_Warehouseaddress1,#ebook_Warehousecountry_name,#ebook_Warehousecity_desc,#ebook_Warehousephone,#ebook_Warehousefax,#ebook_Warehouseemail').attr("readonly", true);
//            } else {
//                $('#ebook_Warehousecountry_name').css("background-color", "white");
//            }
//            //$('#ebook_agreecountry_name').css("background-color", "black");
//            //$('#agreeSelect,#agreesearch').attr('disabled', true);
//        } else {
//            if ($('#ebook_Warehousename').val() != null && $('#ebook_Warehousename').val() != '' && $('#ebook_Warehouseaddress').val() != null && $('#ebook_Warehouseaddress').val() != '') {
//                $('#WarehouseSelect,#Warehousesearch').attr('disabled', true);
//                $('#ebook_Warehousename,#ebook_Warehouseaddress,#ebook_Warehouseaddress1,#ebook_Warehousecountry_name,#ebook_Warehousecity_desc,#ebook_Warehousephone,#ebook_Warehousefax,#ebook_Warehouseemail').attr("readonly", true);
//                // $('#ebook_agreecountry_name').css("background-color", "white");
//            }
//        }
    <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
        $('#bookingSelect,#ebook_bookingname,#ebook_bookingaddress,#ebook_bookingaddress1,#ebook_bookingcountry_name,#ebook_bookingcity_desc,#ebook_bookingphone,#ebook_bookingfax,#ebook_bookingemail,#ebook_bookingstate').attr("readonly", true);
        $('#bookingSelect').attr('disabled', true);
        if($('#ebook_bookingzipcode').length > 0){
            $('#ebook_bookingzipcode').attr("readonly", true);
        }   
        <%}%>
    });
    document.getElementById('ebook_status_remarks').setAttribute('maxlength', '400');
</script>
<%
    String wizardwidth = null;
    if ("GWF".equalsIgnoreCase(carrierCode)) {
        wizardwidth = "16.66%";
    } else {
        wizardwidth = "20%";
    }
%>
<style type="text/css">
    /*    .wizard .steps {
            margin: 0 5% 80px 5%;
        }
        .wizard > .steps > ul > li {
            width: <%=wizardwidth%>;
        }
        #perviewpanel .control-label{
            text-align: left;
        }
        .colonclass{
            width: 2.33333333%;
        }
        #perviewpanel p {
            margin: 0 0 10px;
            word-wrap: break-word;
        }*/
</style>
<style  type="text/css">
    /*    #perviewpanel .form-control[disabled]{
            background-color: #fff;
        }
        .country_new_booking{
            background-color: #eee;
        }
    
        .country_new_agree{
            background-color: #eee; 
        }
        .fontstyle{
            font-weight:100 !important;
        }*/
</style>
<!--<script src="js/jquery-ui.js"></script>-->
<script type="text/javascript">


//    $(function () {
//        $(".modal-draggable .modal-dialog").draggable({
//            handle: ".panel-heading"
//        });
//    });
    function callAutocompleteintialize(id) {
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: counlist
        });
        ports.initialize();
        $(id).typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2
                },
                {
                    name: 'country',
                    limit: 5,
                    displayKey: function (counlist) {
                        return counlist.countrycode + " - " + counlist.countryname;
                    },
                    source: ports.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#typeheadhidden4").val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#typeheadhidden4").val();
            if (hiddenValue == null || hiddenValue == "") {
                $(id).typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    }
    var maxFileSize = 0;
    var validFileExtensions = [".Exe", ".Jar", ".Bat", ".Dat", ".swf", ".wav", ".mp3", ".mp4", ".mpg", ".wmv", ".avi", ".flv", ".mpg", ".mpeg", ".avi", ".wmv", ".mov", ".rm", ".ram", ".ogg", ".webm", ".mp4", ".vob", ".gifv", ".yuv", ".3gp", ".msi"];
    function ValidateSingleInput(oInput) {
        if (oInput.type == "file") {
            var sFileName = oInput.value;
            if (sFileName.length > 0) {
                var blnValid = false;
                for (var j = 0; j < validFileExtensions.length; j++) {
                    var sCurExtension = validFileExtensions[j];
                    if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                        blnValid = true;
                        break;
                    }
                }
                if (blnValid) {
                    //alert("The following file format is not allowed: " + validFileExtensions.join(", "));
                    alert("<bean:message key="ecom.msg.invalidfileformat" bundle="<%=clientcode%>"/>");
                    oInput.value = "";
                    return false;
                }
            }
            var thisFile = $(oInput);
            var fileSize = thisFile[0].files[0].size;
            maxFileSize = parseInt(maxFileSize) + parseInt(fileSize);
            var sizeInMB = (parseInt(maxFileSize) / (1024 * 1024)).toFixed(2);
            if (parseInt(sizeInMB) >= 10) { //do something if file size more than 1 mb (1048576)
                maxFileSize = parseInt(maxFileSize) - parseInt(fileSize);
                alert("<bean:message key="ecom.msg.filesizelimit" bundle="<%=clientcode%>"/>");
                oInput.value = "";
                return false;
            }
        }
        return true;
    }
    function callAutocompleteforsearch(sor, tar) {

        $(sor).typeahead('destroy');
        $(sor).val($(tar).val());
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: counlist
        });
        ports.initialize();
        $(sor).typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2
                },
                {
                    name: 'country',
                    limit: 5,
                    displayKey: function (counlist) {
                        return counlist.countrycode + " - " + counlist.countryname;
                    },
                    source: ports.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#typeheadhidden4").val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#typeheadhidden4").val();
            if (hiddenValue == null || hiddenValue == "") {
                $(sor).typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    }
    loadEmptypickup($('#ebook_origin').val());
    var dgDetJson = DGJson;
    function dgload(dgid, hidid, classid, shipid)
    {
        var dg = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('unNo', 'shippingName'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: dgDetJson
        });
        dg.initialize();
        $("#" + dgid).typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2},
                {
                    name: 'ebook_unno',
                    limit: 50,
                    displayKey: function (dgDetJson) {
                        return dgDetJson.unNo + " ~~ " + dgDetJson.shippingName;
                    },
                    source: dg.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#" + hidid).val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#" + hidid).val(datum.shippingName);
            $("#" + shipid).val(datum.shippingName);
            $("#" + classid).val(datum.dgClass);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#" + hidid).val(datum.shippingName);
            $("#" + shipid).val(datum.shippingName);
            $("#" + classid).val(datum.dgClass);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#" + hidid).val();
            if (hiddenValue == null || hiddenValue == "") {
                $("#" + dgid).typeahead('val', "");
                $(obj.currentTarget).val("");
                $("#" + classid).val("");
                $("#" + shipid).val("");
            }
        });
    }
// Export Excel
    $.fn.serializeObject = function ()
    {
        var o = {};
        var a = this.serializeArray();
        //a.MaxJsonLength = Int32.MaxValue;
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    function ExportData()
    {
        $('.eqpstatusclass').attr("disabled", false);
        $(".eqpstatusclass").each(function () {
            $(this).attr("disabled", false);
        });
        var jsonval = JSON.stringify($('form').serializeObject());
        $('.eqpstatusclass').attr("disabled", true);
        $(".eqpstatusclass").each(function () {
            $(this).attr("disabled", true);
        });
        $.ajax({
            url: 'ExcelExportBooking?formval=sessionjson',
            type: 'POST',
            data: {"formjonval": jsonval},
            dataType: 'json',
            success: function (result) {
                getExcel();
            },
            error: function (result) {
                getExcel();
            }
        });
    }
    function getExcel()
    {
        var url = "ExcelExportBooking?formval=downloadexcel";
        document.getElementById("AmendmentBookingFormBean").action = url;
        document.getElementById("AmendmentBookingFormBean").submit();
    }

    function closeErrorModel()
    {
        // document.getElementById('#importerr').innerHTML="";
        $('#importerr').html("");
        $("#myModalImportErrors").modal("hide");
        $("#myImportModal").modal("show");
        document.getElementById('importexcel').value = "";
        return false;
    }

    function closeMsgModel()
    {
        $('#importmsg').html("");
        $("#myModalImportMsg").modal("hide");
        return false;
    }

    function closeModel()
    {
        document.getElementById('importexcel').value = "";
        $("#myImportModal").modal("hide");
        return false;
    }

    function trim(value) {
        return LTrim(RTrim(value));
    }
    function LTrim(value) {
        var re = /\s*((\S+\s*)*)/;
        return value.replace(re, "$1");
    }
// Removes ending whitespaces
    function RTrim(value) {
        var re = /((\s*\S+)*)\s*/;
        return value.replace(re, "$1");
    }
// End
</script>
<script>
    $(document).ready(function () {
        $('#ebook_pickup_date').datepicker({format: "<%=strDateFormat%>", autoclose: true, clearBtn: true, todayHighlight: true, startDate: "-infinity"});
        $('#ebook_drop_date').datepicker({format: "<%=strDateFormat%>", autoclose: true, clearBtn: true, todayHighlight: true, startDate: "-infinity"}).on('changeDate', function (selected) {
            var pickdate = Date.parse($('#ebook_pickup_date').datepicker('getDate', ''));
            var dropdate = selected.date.valueOf();
            if (pickdate > dropdate)
            {
                $("#ebook_drop_date_ero").show();
                document.getElementById("ebook_drop_date").value = "";
            } else
            {
                $("#ebook_drop_date_ero").hide();
            }
        });

//        $('#ebook_pickup_date').datetimepicker({format: "<%=strDateFormat.toUpperCase()%> HH:mm", minDate: new Date(),
//            widgetPositioning: {
//                horizontal: 'auto',
//                vertical: 'top'
//            }
//        });
//        $('#ebook_drop_date').datetimepicker({format: "<%=strDateFormat.toUpperCase()%> HH:mm", minDate: new Date(),
//            widgetPositioning: {
//                horizontal: 'auto',
//                vertical: 'top'
//            }
//        })
        if ($("#ebook_haulagetype").length > 0) {
            if ($("#ebook_haulagetype").val() != '' && $("#ebook_haulagetype").val().trim().length > 0) {
                checkHaulageType($("#ebook_haulagetype").val().trim());
            }
        }
        //$("#ebook_pickup_date").attr("disabled", true);
        //$("#ebook_drop_date").attr("disabled", true);
        $("#ebook_pickup_date").attr("placeholder", "<bean:message key="ebooking.pickup.date" bundle="<%=clientcode%>"/>");
        $("#ebook_drop_date").attr("placeholder", "<bean:message key="ebooking.drop.date" bundle="<%=clientcode%>"/>");
        $("#ebook_drop_date_ero").hide();
        $("#ebook_haulagetype").change(function () {
            $("#ebook_pickup_date").attr("disabled", true);
            $("#ebook_drop_date").attr("disabled", true);
            document.getElementById("ebook_pickup_date").value = "";
            document.getElementById("ebook_drop_date").value = "";
            var haulage_type = $('#ebook_haulagetype').val();
            checkHaulageType(haulage_type);
        });

    });
    function checkHaulageType(haulage_type) {
        if (haulage_type == "L" || haulage_type == "D")
        {
            $("#ebook_drop_date").attr("disabled", false);
        } else if (haulage_type == "P")
        {
            $("#ebook_pickup_date").attr("disabled", false);
        } else if (haulage_type == "B")
        {
            $("#ebook_pickup_date").attr("disabled", false);
            $("#ebook_drop_date").attr("disabled", false);
        }
    }
</script>
<script type="text/javascript">
    $('#datatablebycargo>tbody').html("");
    setTimeout(function () {
        addamendmentcargo();
    }, 1200);
</script>

<style>
    /*    .modal-draggable .panel-heading:hover {
            cursor: move;
        }
        .d-none {
            display: none;
        }*/
</style>
<script>
    $(document).ready(function () {
        $('#bookingpartynew').val($('#ebook_bookingparty_desc').val());

        $('#ebook_origin_country').attr('placeholder', '<bean:message key="ebooking.origin.display1" bundle="<%=clientcode%>"/>');
        $('#ebook_delivery_country').attr('placeholder', '<bean:message key="shipping.placeofdelevery.display" bundle="<%=clientcode%>"/>');
        $('#ebook_date').attr('placeholder', '<bean:message key="ebooking.date.display" bundle="<%=clientcode%>"/>');
        //
        $('#ebook_bookingname').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_bookingaddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_bookingaddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_bookingcity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_bookingcountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_bookingphone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_bookingfax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_bookingemail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('#ebook_agreename').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_agreeaddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_agreeaddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_agreecity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_agreecountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_agreephone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_agreefax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_agreeemail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('#ebook_shippername').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_shipperaddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_shipperaddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_shippercity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_shippercountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_shipperphone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_shipperfax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_shipperemail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('#ebook_consigneename').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneeaddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneeaddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneecity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneecountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneephone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneefax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneeemail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('#ebook_notifyname').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyaddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyaddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifycity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_notifycountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyphone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyfax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyemail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('#ebook_notifyname_n1').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyaddress_n1').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyaddress1_n1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifycity_desc_n1').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_notifycountry_name_n1').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyphone_n1').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyfax_n1').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyemail_n1').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');


        $('#ebook_forwardername').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_forwarderaddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_forwarderaddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_forwardercity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_forwardercountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_forwarderphone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_forwarderfax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_forwarderemail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('#ebook_Warehousename').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehouseaddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehouseaddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehousecity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehousecountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehousephone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehousefax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehouseemail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('#ebook_NamedCustomername').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomeraddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomeraddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomercity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomercountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomerphone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomerfax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomeremail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');

        $('.ebooking_state').attr('placeholder', '<bean:message key="Reg.State" bundle="<%=clientcode%>"/>');
        $('.ebooking_eori').attr('placeholder', '<bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>');


        $('#ebook_Sellername').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Selleraddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Selleraddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Sellercity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_Sellercountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_Sellerphone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_Sellerfax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Selleremail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');


        $('#ebook_Buyername').attr('placeholder', '<bean:message key="bl.customername.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyeraddress').attr('placeholder', '<bean:message key="bl.adresscust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyeraddress1').attr('placeholder', '<bean:message key="ebooking.adress1cust.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyercity_desc').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyercountry_name').attr('placeholder', '<bean:message key="error.reg.client.country" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyerphone').attr('placeholder', '<bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyerfax').attr('placeholder', '<bean:message key="bl.fax.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyeremail').attr('placeholder', '<bean:message key="bl.email.display" bundle="<%=clientcode%>"/>');
   <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
        $('#ebook_bookingzipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_shipperzipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_consigneezipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyzipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_notifyzipcode_n1').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_forwarderzipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Warehousezipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_NamedCustomerzipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Buyerzipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_Sellerzipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        $('#ebook_agreezipcode').attr('placeholder', '<bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/>');
        <%}%>
//        $(".btn-group > .car").click(function () {
//            $(".btn-group > .btn").removeClass("active");
//            $(this).addClass("active");
//        });
//
//        $(".btn-group > .mer").click(function () {
//            $(".btn-group > .btn").removeClass("active");
//            $(this).addClass("active");
//        });
//
//        $(".btn-group > .na").click(function () {
//            $(".btn-group > .btn").removeClass("active");
//            $(this).addClass("active");
//        });

        $(".btn-group > button").click(function () {
            console.log('preCarriage click==> ' + $('#preCarriage').val());
            console.log('onCarriage click==> ' + $('#onCarriage').val());
            $(this).parent('.btn-group').find('button').removeClass("active");
            $(this).addClass("active");
            if ($(this).parent().is('#preCarriage_btn')) {
                if ($(this).is('.car')) {
                    $('#preCarriage').val('C');
                } else if ($(this).is('.mer')) {
                    $('#preCarriage').val('M');
                } else if ($(this).is('.na')) {
                    $('#preCarriage').val('N');
                }
            } else if ($(this).parent().is('#onCarriage_btn')) {
                if ($(this).is('.car')) {
                    $('#onCarriage').val('C');
                } else if ($(this).is('.mer')) {
                    $('#onCarriage').val('M');
                } else if ($(this).is('.na')) {
                    $('#onCarriage').val('N');
                }
            } else if ($(this).parent().is('#ensfilling_btn')) {
                if ($(this).is('.sng')) {
                    $('#ensfiling').val('S');
                } else if ($(this).is('.prl')) {
                    $('#ensfiling').val('P');
                } else if ($(this).is('.ena')) {
                    $('#ensfiling').val('N');
                }
            }else if ($(this).parent().is('#ensDocType_btn')) {
                if ($(this).is('.sbl')) {
                    $('#ebook_EnsDocType').val('S');
                        if ($('#SellerContainer .buyer-row').length > 0) {
                       $('#SellerContainer .buyer-row').slice(1).remove();
                        
                      }

                     if ($('#BuyerContainer .buyer-row').length > 0) {
                     $('#BuyerContainer .buyer-row').slice(1).remove();
                 
                   }
                } else if ($(this).is('.cnd')) {
                    clearCargoNameCode('Buyer');
                    clearCargoNameCode('Seller');
                    $('#ebook_EnsDocType').val('C');
                } 
              
            }

            console.log('preCarriage click==> ' + $('#preCarriage').val());
            console.log('onCarriage click==> ' + $('#onCarriage').val());
            console.log('ensfiling click==> ' + $('#ensfiling').val());
        });

        if ($('#preCarriage').val() == 'C') {
            $('#preCarriage_btn > .car').addClass("active");
        } else if ($('#preCarriage').val() == 'M') {
            $('#preCarriage_btn > .mer').addClass("active");
        } else {
            $('#preCarriage').val('N');
            $('#preCarriage_btn > .na').addClass("active");
        }
        if ($('#onCarriage').val() == 'C') {
            $('#onCarriage_btn > .car').addClass("active");
        } else if ($('#onCarriage').val() == 'M') {
            $('#onCarriage_btn > .mer').addClass("active");
        } else {
            $('#onCarriage').val('N');
            $('#onCarriage_btn > .na').addClass("active");
        }
//        const enstype = $('#ensfiling').val() ;
//        setEnsFilingType(enstype);
        chkAPEnsFilingAgreement();

        /*    if ($('#filename1').val() != null && $('#filename1').val().length > 0) {
         $('#filename1_span').text($('#filename1').val());
         $('#filename1_lbl').show();
         }
         if ($('#filename2').val() != null && $('#filename2').val().length > 0) {
         $('#filename2_span').text($('#filename2').val());
         $('#filename2_lbl').show();
         }
         if ($('#filename3').val() != null && $('#filename3').val().length > 0) {
         $('#filename3_span').text($('#filename3').val());
         $('#filename3_lbl').show();
         }
         if ($('#filename4').val() != null && $('#filename4').val().length > 0) {
         $('#filename4_span').text($('#filename4').val());
         $('#filename4_lbl').show();
         }
         if ($('#filename5').val() != null && $('#filename5').val().length > 0) {
         $('#filename5_span').text($('#filename5').val());
         $('#filename5_lbl').show();
         }*/
        loadStateAutoComplete('ebook_bookingcountry_name', 'ebook_bookingstate', 'typeheadhidden6', 'load');
        loadStateAutoComplete('ebook_agreecountry_name', 'ebook_agreestate', 'typeheadhidden7', 'load');
        loadStateAutoComplete('ebook_shippercountry_name', 'ebook_shipperstate', 'typeheadhidden8', 'load');
        loadStateAutoComplete('ebook_consigneecountry_name', 'ebook_consigneestate', 'typeheadhidden9', 'load');
        loadStateAutoComplete('ebook_notifycountry_name', 'ebook_notifystate', 'typeheadhidden10', 'load');
        loadStateAutoComplete('ebook_notifycountry_name_n1', 'ebook_notifystate_n1', 'typeheadhidden11', 'load');
        loadStateAutoComplete('ebook_forwardercountry_name', 'ebook_forwarderstate', 'typeheadhidden12', 'load');
        loadStateAutoComplete('ebook_NamedCustomercountry_name', 'ebook_NamedCustomerstate', 'typeheadhidden2', 'load');
        loadStateAutoComplete('ebook_Sellercountry_name', 'ebook_Sellerstate', 'typeheadhidden15', 'load');
        loadStateAutoComplete('ebook_Buyercountry_name', 'ebook_Buyerstate', 'typeheadhidden16', 'load');

        //loadStateAutoComplete('ebook_Warehousecountry_name', 'ebook_Warehousestate', 'typeheadhidden13', 'load');
    });
    console.log('preCarriage==> ' + $('#preCarriage').val());
    console.log('onCarriage==> ' + $('#onCarriage').val());

    function loadStateAutoComplete(countryid, stateid, hiddenid, type) {
        $('#' + stateid).typeahead('destroy');
        if ($('#' + countryid).length > 0 && $('#' + countryid).val() != '' && $('#' + countryid).val().length > 0) {

            if (type != 'load') {
                $('#' + stateid).val('');
                $('#' + stateid).typeahead('val', '');
                $('#' + hiddenid).val('');
            }
            var statejs = StateJson.filter(function (item) {
                return (item["CountryCode"] == trim($('#' + countryid).val().split('-')[0]));
            });
            var states = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('StateName', 'StateCode'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: statejs
            });
            states.initialize();
            $('#' + stateid).typeahead(
                    {
                        hint: true,
                        highlight: true,
                        minLength: 2
                    },
                    {
                        name: 'state',
                        limit: 5,
                        displayKey: function (state) {
                            return state.StateCode + " - " + state.StateName;
                        },
                        source: states.ttAdapter()}).on('typeahead:render', function (obj, datum) {
                $("#" + hiddenid).val("");
            }).on('typeahead:selected', function (obj, datum) {
                $("#" + hiddenid).val(datum.StateName);
            }).on('typeahead:autocomplete', function (obj, datum) {
                $("#" + hiddenid).val(datum.StateName);
            }).on("typeahead:change", function (obj, datum) {
                var hiddenValue = $("#" + hiddenid).val();
                if (hiddenValue == null || hiddenValue == "") {
                    $('#' + stateid).typeahead('val', "");
                    $(obj.currentTarget).val("");
                }
            });
        } else {
            $('#' + stateid).val('');
            $('#' + stateid).typeahead('val', '');
            $('#' + hiddenid).val('');
        }
        loadtypeaheadtooltipmsg();
    }
    function validateState(countryid, obj) {
        if ($('#' + countryid).length == 0 || $('#' + countryid).val() == '' || $('#' + countryid).val().length == 0) {
            $(obj).val('');
            alert("Please select the country");
        }
    }
    function checkEUCountry(countryid) {
        var countryValue = $("#" + countryid).val().trim();
        if (!countryValue) {
            return false;
        }
        var countrycode = countryValue.split('-')[0].trim().toUpperCase();
        return europeCountryJson.includes(countrycode);
    }
    function isNewCustomer(custcodeid) {
        if ($("#" + custcodeid).val() == "") {
            return true;
        }
        return false;
    }

</script>

<script>

    //toggle icon for the headings
    $(document).ready(function () {
        $('#addi_details').click(function () {
            $("i", this).toggleClass('fa-plus fa-minus');
        });
        $('.toggle-btn').click(function () {
            $("i", this).toggleClass('fa-minus fa-plus');
        });
    });
</script>
<%if ("TSH".equalsIgnoreCase(carrierCode)) {%>
<script>
    $(document).ready(function () {
        $("#addi_details").click();
    });

</script>
<%}%>