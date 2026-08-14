/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import net.com.ecom.ws.vo.CountryList;
import net.com.ecom.ws.vo.CountryVO;
import net.com.ecom.ws.vo.GeneralVO;
import net.com.ecom.ws.vo.GeneralVOList;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomMaskBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.carbon.CarbonApiLogger;
import net.ecom.carbon.CarbonLogInitializer;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.InsuranceUtils;
import net.ecom.formbeans.GlobalConfigForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1MasterBeanRemote;
import net.ecom.vo.CityVo;
import net.ecom.vo.FieldConfigVO;
import net.ecom.vo.HotlineContactsVO;
import net.ecom.vo.RegisterMenuVO;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author deepa.p
 */
public class EcomCacheLoader {

    Validation validate = new Validation();
    private static String mapping_string = "viewAll";
    List fieldNameConfigList = null;
    Map<String, String> fieldConfigMap = null;
    Map<String, String> fieldShipmentMap = null;

    public void cacheLoader(ServletContext context) throws Exception {
        try {
            EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
            EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();
            EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
            EcomMaskBeanRemote ecomMaskObj = EJBLookUp.getEcomMaskBeanLookUp();

            System.out.println("[ECOM-WAR] EcomCacheLoader >>cacheLoader method1>>");
            String hsCodeArr[];
            String natCodeArr[];
            String CarriageContractArr[];
            String CarriageContractArr1[];
            String vgmDisclaimerMsg = locobj.getForwardMessage("E-VGM", "DISCLAIMER", "C");
            String allDisclaimerMsg = locobj.getForwardMessageForDisclaimer("All", "C");
            String vgmDisclaimerMsglang = locobj.getForwardMessageLang("E-VGM", "DISCLAIMER", "C", "ch");
            Map configDetails = (Map) locobj.getMasterConfigDetails();
            Map<String, List<String>> mailActivityConfig = locobj.getEcomMailAcitvityList();
            System.out.println("[ECOM-WAR] EcomCacheLoader cacheLoader method1 "
                    + " vgmDisclaimerMsg " + locobj.getForwardMessage("E-VGM", "DISCLAIMER", "C")
                    + " allDisclaimerMsg " + locobj.getForwardMessageForDisclaimer("All", "C")
                    + " DisclaimerMsglang " + locobj.getForwardMessageLang("E-VGM", "DISCLAIMER", "C", "ch")
                    + " mailActivityConfig " + locobj.getEcomMailAcitvityList());

            String getHSCodeCountry = (String) configDetails.get(GeneralConf.hscodecountry);
            List<String> hsCodeCountryLis = null;
            if (getHSCodeCountry != null && getHSCodeCountry.length() > 0) {
                hsCodeArr = getHSCodeCountry.split("~~");
                hsCodeCountryLis = new ArrayList<String>(Arrays.asList(hsCodeArr));
            }

            //
            //natcode
            String getnatCodeCountry = (String) configDetails.get(GeneralConf.natcodecountry);
            List<String> natCodeCountryLis = null;
            if (getnatCodeCountry != null && getnatCodeCountry.length() > 0) {
                natCodeArr = getnatCodeCountry.split("~~");
                natCodeCountryLis = new ArrayList<String>(Arrays.asList(natCodeArr));
            }
            //carriagecontract
            Map<String, String> carriageContractMap = new HashMap<>();
            String getCarriageContractVal = (String) configDetails.get(GeneralConf.CarriageContractBasedOnPorts);
            if (getCarriageContractVal != null && getCarriageContractVal.length() > 0) {
                CarriageContractArr = getCarriageContractVal.split("~~");
                if (CarriageContractArr != null && CarriageContractArr.length > 0) {
                    for (int i = 0; i < CarriageContractArr.length; i++) {
                        CarriageContractArr1 = null;
                        CarriageContractArr1 = CarriageContractArr[i].split("=");
                        carriageContractMap.put(Validation.checkNull(CarriageContractArr1[0]).trim(), Validation.checkNull(CarriageContractArr1[1]).trim());
                    }
                }
            }
            context.setAttribute(GeneralConf.CarriageContractBasedOnPorts, carriageContractMap);
            System.out.println("[ECOM-WAR] EcomCacheLoader cachecarriagecontractmap====>" + carriageContractMap);
            //Additional Info BKG
            List<String> addInfoBkgList = null;
            String getAddInfoBkg = (String) configDetails.get(GeneralConf.AdditionalInfoBkg);
            if (getAddInfoBkg != null && getAddInfoBkg.length() > 0) {
                addInfoBkgList = new ArrayList(Arrays.asList(getAddInfoBkg.split(",")));

            }

            //
            //TUES AND FEUS
            String FEUexists = locobj.checkPluginFEU("REI", "FEU");
            System.out.println("[ECOM-WAR] EcomCacheLoader FEUexistsvalue" + FEUexists);
            context.setAttribute("FEUexists", FEUexists);

            String strDateFormat = (String) configDetails.get(GeneralConf.strDateFormat);
            Map<String, String> transitMap = locobj.getPODIntransitMap();
            HashMap<String, String> portCountryMap = locobj.getPortCountryMap();
            HashMap<String, ArrayList<CityVo>> countryCityObj = locobj.fetchCountryCityList();
            HashMap<String, List<String>> agencyLocationMap = locobj.getAgencyLocation();
            String client_code = locobj.getClientNameForProperty();
            ArrayList usermenu = ecomreg.getMenuDetails();
            Hashtable globalconfig = locobj.getglobalconfigMail();
            context.setAttribute("usermenu", usermenu.get(0));
            context.setAttribute("usersubmenu", usermenu.get(1));
            context.setAttribute("userMenuCategory", usermenu.get(2));
            String Imagepath = locobj.getGlobalImagePath();
            String Version = locobj.getEcomReleaseVersion();
            System.out.println("[ECOM-WAR] EcomCacheLoader ======>Version" + Version);
            GeneralConf.setStrConfDefDate(strDateFormat);
            GeneralConf.setStrConfDefDateTimeFormat(strDateFormat + " " + "HH:mm");
            String datepicker = null;
            if (strDateFormat.contains("MMM")) {
                datepicker = strDateFormat.replace("MMM", "M");
            } else {
                datepicker = strDateFormat.replace("MM", "mm");
            }
            context.setAttribute("Imagepath", Imagepath);
            context.setAttribute("DATEFROMATfORDATEPICKER", datepicker);
            context.setAttribute("DATEFROMAT", strDateFormat);
            //context.setAttribute("TRACKPATH", "ecomonlinenew");
            context.setAttribute("APPLNRESTARTCHECK", "YES");

            System.out.println("[ECOM-WAR] EcomCacheLoader client_code=====> " + client_code);
            context.setAttribute("client_code", client_code);
            System.out.println("[ECOM-WAR] EcomCacheLoader context.getAttribute('client_code')=====> " + context.getAttribute("client_code"));
            context.setAttribute(GeneralConf.CACHEVERSION, (String) globalconfig.get(GeneralConf.CACHEVERSION));
            context.setAttribute(GeneralConf.APBPCONFIGFRONTEND, (String) globalconfig.get(GeneralConf.APBPCONFIGFRONTEND));
            context.setAttribute(GeneralConf.APBPCONFIGBACKEND, (String) globalconfig.get(GeneralConf.APBPCONFIGBACKEND));
            context.setAttribute(GeneralConf.V1DATAENABLESTATUS, (String) globalconfig.get(GeneralConf.V1DATAENABLESTATUS));
            context.setAttribute(GeneralConf.logowidth, (String) configDetails.get(GeneralConf.logowidth));
            context.setAttribute(GeneralConf.logoheight, (String) configDetails.get(GeneralConf.logoheight));
            context.setAttribute(GeneralConf.menucolor, (String) configDetails.get(GeneralConf.menucolor));
            context.setAttribute(GeneralConf.tabcolor, (String) configDetails.get(GeneralConf.tabcolor));
            context.setAttribute(GeneralConf.clientfrommail, (String) globalconfig.get(GeneralConf.clientfrommail));
            context.setAttribute(GeneralConf.clientglobalmail, (String) globalconfig.get(GeneralConf.clientglobalmail));
            //FOR TRACKING WEBSERVICE
            context.setAttribute(GeneralConf.TRACKINGDATEFORMAT, (String) globalconfig.get(GeneralConf.TRACKINGDATEFORMAT));
            context.setAttribute(GeneralConf.TRACKINGSEARCHURL, (String) globalconfig.get(GeneralConf.TRACKINGSEARCHURL));
            context.setAttribute(GeneralConf.TRACKINGMOVEMENTURL, (String) globalconfig.get(GeneralConf.TRACKINGMOVEMENTURL));
            context.setAttribute(GeneralConf.TRACKINGUSERNAME, (String) globalconfig.get(GeneralConf.TRACKINGUSERNAME));
            context.setAttribute(GeneralConf.TRACKINGPASSWORD, (String) globalconfig.get(GeneralConf.TRACKINGPASSWORD));
            // Live Container Tracking - SnapGIS + MapBox
            context.setAttribute(GeneralConf.Enable_Live_Tracking, (String) globalconfig.get(GeneralConf.Enable_Live_Tracking));
            context.setAttribute(GeneralConf.SnapGIS_API_URL, (String) globalconfig.get(GeneralConf.SnapGIS_API_URL));
            context.setAttribute(GeneralConf.SnapGIS_API_Key, (String) globalconfig.get(GeneralConf.SnapGIS_API_Key));
            context.setAttribute(GeneralConf.SnapGIS_API_Key_Secret, (String) globalconfig.get(GeneralConf.SnapGIS_API_Key_Secret));
            context.setAttribute(GeneralConf.MAPBOX_ACCESS_TOKEN, (String) globalconfig.get(GeneralConf.MAPBOX_ACCESS_TOKEN));
            context.setAttribute(GeneralConf.CLIMATIQ_API_KEY, (String) globalconfig.get(GeneralConf.CLIMATIQ_API_KEY));
            //
            context.setAttribute(GeneralConf.VGMCCMAILID, (String) globalconfig.get(GeneralConf.VGMCCMAILID));
            context.setAttribute(GeneralConf.VGMBCCMAILID, (String) globalconfig.get(GeneralConf.VGMBCCMAILID));
            context.setAttribute(GeneralConf.additionalbkgccmail, (String) globalconfig.get(GeneralConf.additionalbkgccmail));
            context.setAttribute(GeneralConf.additionalsiccmail, (String) globalconfig.get(GeneralConf.additionalsiccmail));

            //
            context.setAttribute(GeneralConf.filepath, (String) configDetails.get(GeneralConf.filepath));
            context.setAttribute(CarbonApiLogger.ECOM_LOG_BASE_PATH_KEY, (String) configDetails.get(CarbonApiLogger.ECOM_LOG_BASE_PATH_KEY));
            context.setAttribute(CarbonApiLogger.ECOM_CARBON_LOG_ROLLING_PERIOD,  (String) configDetails.get(CarbonApiLogger.ECOM_CARBON_LOG_ROLLING_PERIOD));
            context.setAttribute(GeneralConf.WSURL, (String) configDetails.get(GeneralConf.WSURL));
            context.setAttribute(GeneralConf.commonpath, (String) configDetails.get(GeneralConf.commonpath));
            context.setAttribute(GeneralConf.serverip, (String) configDetails.get(GeneralConf.serverip));
            context.setAttribute(GeneralConf.pdfviewurl, (String) configDetails.get(GeneralConf.pdfviewurl));
            context.setAttribute(GeneralConf.footerpowerby, (String) globalconfig.get(GeneralConf.footerpowerby));
            context.setAttribute(GeneralConf.footercompanyname, (String) globalconfig.get(GeneralConf.footercompanyname));
            context.setAttribute(GeneralConf.oblprinturl, (String) globalconfig.get(GeneralConf.oblprinturl));
            context.setAttribute(GeneralConf.ANPDFPATH, (String) globalconfig.get(GeneralConf.ANPDFPATH));
            context.setAttribute(GeneralConf.DOPDFPATH, (String) globalconfig.get(GeneralConf.DOPDFPATH));
            context.setAttribute(GeneralConf.COMMONURL, (String) globalconfig.get(GeneralConf.COMMONURL));
            context.setAttribute(GeneralConf.ECOMGATRACKID, (String) globalconfig.get(GeneralConf.ECOMGATRACKID));
            context.setAttribute(GeneralConf.MAILLOGOIMAGE, (String) globalconfig.get(GeneralConf.MAILLOGOIMAGE));
            context.setAttribute(GeneralConf.hscodecountry, hsCodeCountryLis);
            context.setAttribute(GeneralConf.natcodecountry, natCodeCountryLis);
            context.setAttribute(GeneralConf.AdditionalInfoBkg, addInfoBkgList);
            context.setAttribute(GeneralConf.exceluploadpath, (String) configDetails.get(GeneralConf.exceluploadpath));
            context.setAttribute(GeneralConf.pdfviewaccess, (String) configDetails.get(GeneralConf.pdfviewaccess));
            context.setAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL, (String) configDetails.get(GeneralConf.BL_PRINT_WEBSERVICE_URL));
            context.setAttribute(GeneralConf.AGENCYLINK, (String) configDetails.get(GeneralConf.AGENCYLINK));
            context.setAttribute(GeneralConf.V1OBLPRINTURL, (String) configDetails.get(GeneralConf.V1OBLPRINTURL));
            context.setAttribute(GeneralConf.RATEREQMENU, (String) configDetails.get(GeneralConf.RATEREQMENU));
            context.setAttribute(GeneralConf.V1FEEDERCUTOFFDATE, (String) configDetails.get(GeneralConf.V1FEEDERCUTOFFDATE));
            context.setAttribute(GeneralConf.FAKCOMMODITY, (String) configDetails.get(GeneralConf.FAKCOMMODITY));
            context.setAttribute(GeneralConf.EMPTYPLACEWITHTERMINAL, (String) configDetails.get(GeneralConf.EMPTYPLACEWITHTERMINAL));
            context.setAttribute(GeneralConf.EMPTYPLACEWITHOUTTERMINAL, (String) configDetails.get(GeneralConf.EMPTYPLACEWITHOUTTERMINAL));
            context.setAttribute(GeneralConf.VGMFILEUPLOADSTATUS, (String) configDetails.get(GeneralConf.VGMFILEUPLOADSTATUS));
            context.setAttribute(GeneralConf.CARRIER_CODE, (String) configDetails.get(GeneralConf.CARRIER_CODE));
            context.setAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL, (String) configDetails.get(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL));
            context.setAttribute(GeneralConf.ENABLE_HUMIDITY_CO2_O2, (String) configDetails.get(GeneralConf.ENABLE_HUMIDITY_CO2_O2));
            context.setAttribute(GeneralConf.WEIGHT_UNIT_KILOS_ONLY, (String) configDetails.get(GeneralConf.WEIGHT_UNIT_KILOS_ONLY));
            context.setAttribute(GeneralConf.SI_CREATE_FOR_CONFIRMED_BOOKING, (String) configDetails.get(GeneralConf.SI_CREATE_FOR_CONFIRMED_BOOKING));
            context.setAttribute(GeneralConf.LRP_PLUGINID_CONFIG, (String) configDetails.get(GeneralConf.LRP_PLUGINID_CONFIG));
            context.setAttribute(GeneralConf.FEEDER_RESTRICTION, (String) configDetails.get(GeneralConf.FEEDER_RESTRICTION));
            context.setAttribute(GeneralConf.surcharge_details, (String) configDetails.get(GeneralConf.surcharge_details));
            context.setAttribute(GeneralConf.HideFaxNo, (String) configDetails.get(GeneralConf.HideFaxNo));
            context.setAttribute(GeneralConf.Hide_Min_MaxTemp, (String) configDetails.get(GeneralConf.Hide_Min_MaxTemp));
            context.setAttribute(GeneralConf.POSTALCODE_OPTIONAL_COUNTRY, (String) configDetails.get(GeneralConf.POSTALCODE_OPTIONAL_COUNTRY));
            context.setAttribute(GeneralConf.DASHBOARD_DISPLAY, (String) configDetails.get(GeneralConf.DASHBOARD_DISPLAY));
            context.setAttribute(GeneralConf.LIVE_CHAT_ID, (String) configDetails.get(GeneralConf.LIVE_CHAT_ID));
            context.setAttribute(GeneralConf.NORMAL_MOVEMENT_CODE, (String) configDetails.get(GeneralConf.NORMAL_MOVEMENT_CODE));
            context.setAttribute(GeneralConf.GROWER_MOVEMENT_CODE, (String) configDetails.get(GeneralConf.GROWER_MOVEMENT_CODE));
            context.setAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH, locobj.getEcomBKGFileUploadPath());
            context.setAttribute(GeneralConf.KYC_FILE_UPLOAD_PATH, ecomreg.getfileuploadsharepath());
            context.setAttribute(GeneralConf.CUSTTYPECONFIG, locobj.getCustType());
            context.setAttribute(GeneralConf.CSFROMINV, (String) configDetails.get(GeneralConf.CSFROMINV));
            context.setAttribute(GeneralConf.REFIDCONFIG, locobj.getRefidForAddDet());
            context.setAttribute(GeneralConf.CUSTMANDATORY, (String) configDetails.get(GeneralConf.CUSTMANDATORY));
            context.setAttribute(GeneralConf.EBKPRINTSID, (String) configDetails.get(GeneralConf.EBKPRINTSID));
            context.setAttribute(GeneralConf.EBK_PLUGINID_CONFIG, (String) configDetails.get(GeneralConf.EBK_PLUGINID_CONFIG));
            context.setAttribute(GeneralConf.MOBMANDATORY, (String) configDetails.get(GeneralConf.MOBMANDATORY));
            context.setAttribute(GeneralConf.PrintOnBlAddMandatory, (String) configDetails.get(GeneralConf.PrintOnBlAddMandatory));
            context.setAttribute(GeneralConf.RateContractActionDisplay, (String) configDetails.get(GeneralConf.RateContractActionDisplay));
            context.setAttribute(GeneralConf.HideLRPBookingCancel, (String) configDetails.get(GeneralConf.HideLRPBookingCancel));
            context.setAttribute(GeneralConf.LangSelection, locobj.getEcomLangSelection());
            context.setAttribute(GeneralConf.timeZoneSelection, locobj.getEcomTimeZoneSelection());
            context.setAttribute(GeneralConf.versionUTCValue, locobj.getVersionUTC());
            context.setAttribute(GeneralConf.DisplayRegCustCode, (String) configDetails.get(GeneralConf.DisplayRegCustCode));
            context.setAttribute(GeneralConf.DisplayConsigneeToOrder, (String) configDetails.get(GeneralConf.DisplayConsigneeToOrder));
            context.setAttribute(GeneralConf.DisplayNon_RatedBLMsg, (String) configDetails.get(GeneralConf.DisplayNon_RatedBLMsg));
            context.setAttribute(GeneralConf.DisplayBL_NNCopypdfprint, (String) configDetails.get(GeneralConf.DisplayBL_NNCopypdfprint));
            context.setAttribute(GeneralConf.Enable_call_Manifest_Print, (String) configDetails.get(GeneralConf.Enable_call_Manifest_Print));
            context.setAttribute(GeneralConf.Enable_ContactUs_Companytype, (String) configDetails.get(GeneralConf.Enable_ContactUs_Companytype));
            context.setAttribute(GeneralConf.ENABLE_MULTIAGENCY, (String) configDetails.get(GeneralConf.ENABLE_MULTIAGENCY));
            context.setAttribute(GeneralConf.PAGE_TITLE, (String) configDetails.get(GeneralConf.PAGE_TITLE));
            context.setAttribute(GeneralConf.ENABLE_DEFAULT_ROUTE_ONLY, (String) configDetails.get(GeneralConf.ENABLE_DEFAULT_ROUTE_ONLY));
            context.setAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_CUST, (String) configDetails.get(GeneralConf.DETAIL_MAIL_CONTENT_FOR_CUST));
            context.setAttribute(GeneralConf.DETAIL_MAIL_CONTENT_FOR_AGENCY, (String) configDetails.get(GeneralConf.DETAIL_MAIL_CONTENT_FOR_AGENCY));
            context.setAttribute(GeneralConf.CaptchaSecretKey, (String) configDetails.get(GeneralConf.CaptchaSecretKey));
            context.setAttribute(GeneralConf.CaptchaSiteKey, (String) configDetails.get(GeneralConf.CaptchaSiteKey));
            context.setAttribute(GeneralConf.DisplayGoogleCaptcha, (String) configDetails.get(GeneralConf.DisplayGoogleCaptcha));
            context.setAttribute(GeneralConf.PAST_DATE_CONFIG, (String) configDetails.get(GeneralConf.PAST_DATE_CONFIG));
            context.setAttribute(GeneralConf.TRACKING_MOVEMENT_DISPLAY_ALL, (String) configDetails.get(GeneralConf.TRACKING_MOVEMENT_DISPLAY_ALL));
            context.setAttribute(GeneralConf.TRACKING_RESTRICTION, (String) configDetails.get(GeneralConf.TRACKING_RESTRICTION));
            context.setAttribute(GeneralConf.ENABLE_NON_MATCHING_SCHD, (String) configDetails.get(GeneralConf.ENABLE_NON_MATCHING_SCHD));
            context.setAttribute(GeneralConf.EnableTimeZone, (String) configDetails.get(GeneralConf.EnableTimeZone));
            context.setAttribute(GeneralConf.currencyunitvalue, (String) configDetails.get(GeneralConf.currencyunitvalue));
            context.setAttribute(GeneralConf.PORTNAME_WITH_STATECODE, (String) configDetails.get(GeneralConf.PORTNAME_WITH_STATECODE));
            context.setAttribute(GeneralConf.CommodityDescInOnlyENG, (String) configDetails.get(GeneralConf.CommodityDescInOnlyENG));
            context.setAttribute(GeneralConf.CommodityDescInOnlyENG_Disclaimer, (String) configDetails.get(GeneralConf.CommodityDescInOnlyENG_Disclaimer));
            context.setAttribute(GeneralConf.CommodityDescInOnlyENG_Regex, (String) configDetails.get(GeneralConf.CommodityDescInOnlyENG_Regex));
            context.setAttribute(GeneralConf.EnableActualCommoName, (String) configDetails.get(GeneralConf.EnableActualCommoName));
            context.setAttribute(GeneralConf.EnableVGMAgencyMail, (String) configDetails.get(GeneralConf.EnableVGMAgencyMail));
            context.setAttribute(GeneralConf.RegGoogleCaptcha, (String) configDetails.get(GeneralConf.RegGoogleCaptcha));
            context.setAttribute(GeneralConf.RegAcceptOnlyEng, (String) configDetails.get(GeneralConf.RegAcceptOnlyEng));
            context.setAttribute(GeneralConf.ENABLE_AES_NUMBER, (String) configDetails.get(GeneralConf.ENABLE_AES_NUMBER));
            context.setAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL, (String) configDetails.get(GeneralConf.SHOW_CHARGES_IN_SIBL));
            context.setAttribute(GeneralConf.EnableNewContract, (String) configDetails.get(GeneralConf.EnableNewContract));
            context.setAttribute(GeneralConf.Enable_Routing_Pol_Pod_Search, (String) configDetails.get(GeneralConf.Enable_Routing_Pol_Pod_Search));
            context.setAttribute(GeneralConf.Enable_cargodesc_mandatory, (String) configDetails.get(GeneralConf.Enable_cargodesc_mandatory));
            context.setAttribute(GeneralConf.CommodityName_as_HS_Chapter, (String) configDetails.get(GeneralConf.CommodityName_as_HS_Chapter));
            context.setAttribute(GeneralConf.bkgprintdbconfig, (String) configDetails.get(GeneralConf.bkgprintdbconfig));
            context.setAttribute(GeneralConf.Enable_Customize_Import_Export_Excel, (String) configDetails.get(GeneralConf.Enable_Customize_Import_Export_Excel));
            context.setAttribute(GeneralConf.BG_Image_Name, (String) configDetails.get(GeneralConf.BG_Image_Name));
            context.setAttribute(GeneralConf.Enable_Password_Validation, (String) configDetails.get(GeneralConf.Enable_Password_Validation));
            context.setAttribute(GeneralConf.Password_Validation_API_URL, (String) configDetails.get(GeneralConf.Password_Validation_API_URL));
            context.setAttribute(GeneralConf.ENABLE_INSURANCE, (String) configDetails.get(GeneralConf.ENABLE_INSURANCE));
            context.setAttribute(GeneralConf.INSURANCE_BASEURL, (String) configDetails.get(GeneralConf.INSURANCE_BASEURL));
            context.setAttribute(GeneralConf.INSURANCE_TOKENURL, (String) configDetails.get(GeneralConf.INSURANCE_TOKENURL));
            context.setAttribute(GeneralConf.INSURANCE_TOKENSECRETCODE, (String) configDetails.get(GeneralConf.INSURANCE_TOKENSECRETCODE));
            context.setAttribute(GeneralConf.Booking_Jasperid_From_Agency, (String) configDetails.get(GeneralConf.Booking_Jasperid_From_Agency));
            context.setAttribute(GeneralConf.Display_LCT_CUTOFF, (String) configDetails.get(GeneralConf.Display_LCT_CUTOFF));
            context.setAttribute(GeneralConf.CargoProtectMail, (String) configDetails.get(GeneralConf.CargoProtectMail));
            context.setAttribute(GeneralConf.CargoProtectMailSubject, (String) configDetails.get(GeneralConf.CargoProtectMailSubject));
            context.setAttribute(GeneralConf.Disable_CommodityGRP, (String) configDetails.get(GeneralConf.Disable_CommodityGRP));
            context.setAttribute(GeneralConf.Disable_Lang_Mail, (String) configDetails.get(GeneralConf.Disable_Lang_Mail));
            context.setAttribute(GeneralConf.check_ARN_Send_To_Customer, (String) configDetails.get(GeneralConf.check_ARN_Send_To_Customer));
            context.setAttribute(GeneralConf.Display_Tracking_Cutoff, (String) configDetails.get(GeneralConf.Display_Tracking_Cutoff));
            context.setAttribute(GeneralConf.Enable_Ecom_Notification, (String) configDetails.get(GeneralConf.Enable_Ecom_Notification));
            context.setAttribute(GeneralConf.Enable_Session_Timeout, (String) configDetails.get(GeneralConf.Enable_Session_Timeout));
            context.setAttribute(GeneralConf.Disable_Current_Date_Schedule, (String) configDetails.get(GeneralConf.Disable_Current_Date_Schedule));
            context.setAttribute(GeneralConf.Enable_Product_Dashboard, (String) configDetails.get(GeneralConf.Enable_Product_Dashboard));
            context.setAttribute(GeneralConf.SI_Shipper_Seal_Length, (String) configDetails.get(GeneralConf.SI_Shipper_Seal_Length));
            context.setAttribute(GeneralConf.Enable_Registration_Agency_Mail, (String) configDetails.get(GeneralConf.Enable_Registration_Agency_Mail));
            context.setAttribute(GeneralConf.Enable_ZipFile_Conversion, (String) configDetails.get(GeneralConf.Enable_ZipFile_Conversion));
            context.setAttribute(GeneralConf.Invoice_Print_Type, (String) configDetails.get(GeneralConf.Invoice_Print_Type));
            context.setAttribute(GeneralConf.Enable_Drains_Humidity, (String) configDetails.get(GeneralConf.Enable_Drains_Humidity));
            context.setAttribute(GeneralConf.Enable_BLType_Selection, (String) configDetails.get(GeneralConf.Enable_BLType_Selection));
            context.setAttribute(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours, (String) configDetails.get(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours));
            context.setAttribute(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours, (String) configDetails.get(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours));
            context.setAttribute(GeneralConf.Enable_Uppercase_Captcha, (String) configDetails.get(GeneralConf.Enable_Uppercase_Captcha));
            context.setAttribute(GeneralConf.arrivalPrintTypeAsEcom, (String) configDetails.get(GeneralConf.arrivalPrintTypeAsEcom));
            context.setAttribute(GeneralConf.Enable_ConfirmedBooking_Edit, (String) configDetails.get(GeneralConf.Enable_ConfirmedBooking_Edit));
            context.setAttribute(GeneralConf.Sort_Schedule_Based_On_Depdate, (String) configDetails.get(GeneralConf.Sort_Schedule_Based_On_Depdate));
            context.setAttribute(GeneralConf.ENABLE_OOG_DETAILS, (String) configDetails.get(GeneralConf.ENABLE_OOG_DETAILS));
            context.setAttribute(GeneralConf.EnableReeferDetails, (String) configDetails.get(GeneralConf.EnableReeferDetails));
            context.setAttribute(GeneralConf.EditEnableReeferDetails, (String) configDetails.get(GeneralConf.EditEnableReeferDetails));
            context.setAttribute(GeneralConf.Disable_Admin_Login_In_Home_Page, (String) configDetails.get(GeneralConf.Disable_Admin_Login_In_Home_Page));
            context.setAttribute(GeneralConf.Enable_Amendment_Comparison_previousRecord_Mail, (String) configDetails.get(GeneralConf.Enable_Amendment_Comparison_previousRecord_Mail));
            context.setAttribute(GeneralConf.REG_Cust_Validation_On_Next_Btn, (String) configDetails.get(GeneralConf.REG_Cust_Validation_On_Next_Btn));
            context.setAttribute(GeneralConf.JAVASCRIPT_VERSION_NO, (String) configDetails.get(GeneralConf.JAVASCRIPT_VERSION_NO));

            context.setAttribute(GeneralConf.EnableFileBackendValidation, (String) configDetails.get(GeneralConf.EnableFileBackendValidation));
            context.setAttribute(GeneralConf.EcomFileExtension, (String) configDetails.get(GeneralConf.EcomFileExtension));
            context.setAttribute(GeneralConf.KYC_fileupload, (String) configDetails.get(GeneralConf.KYC_fileupload));
            context.setAttribute(GeneralConf.EcomFileUploadSize, (String) configDetails.get(GeneralConf.EcomFileUploadSize));
            context.setAttribute(GeneralConf.Temp_Password_For_ForgotPWD, (String) configDetails.get(GeneralConf.Temp_Password_For_ForgotPWD));
            context.setAttribute(GeneralConf.Enable_Invalid_PasswordAttempts, (String) configDetails.get(GeneralConf.Enable_Invalid_PasswordAttempts));
            context.setAttribute(GeneralConf.Account_Lock_Duration, (String) configDetails.get(GeneralConf.Account_Lock_Duration));
            context.setAttribute(GeneralConf.AttemptLimit, (String) configDetails.get(GeneralConf.AttemptLimit));

            context.setAttribute(GeneralConf.customercode_mandatory, (String) configDetails.get(GeneralConf.customercode_mandatory));
            context.setAttribute(GeneralConf.disable_autoreg_verification, (String) configDetails.get(GeneralConf.disable_autoreg_verification));
            context.setAttribute(GeneralConf.Enable_Reg_Auto_Process, (String) configDetails.get(GeneralConf.Enable_Reg_Auto_Process));
            context.setAttribute(GeneralConf.Reg_Auto_Process_Country, Validation.checkNull((String) configDetails.get(GeneralConf.Reg_Auto_Process_Country)));
            context.setAttribute(GeneralConf.CUSTOMERCODE_REQ_URL, (String) configDetails.get(GeneralConf.CUSTOMERCODE_REQ_URL));
            context.setAttribute(GeneralConf.Enable_Xss_Script_Blocker, (String) configDetails.get(GeneralConf.Enable_Xss_Script_Blocker));
            context.setAttribute(GeneralConf.Enable_SI_Freight_Option, (String) configDetails.get(GeneralConf.Enable_SI_Freight_Option));
            context.setAttribute(GeneralConf.BL_TYPE_LIST, (String) configDetails.get(GeneralConf.BL_TYPE_LIST));

            context.setAttribute(GeneralConf.Registration_PostalCode_Optional, (String) configDetails.get(GeneralConf.Registration_PostalCode_Optional));
            context.setAttribute(GeneralConf.Registration_CompanyPhone_Optional, (String) configDetails.get(GeneralConf.Registration_CompanyPhone_Optional));
            context.setAttribute(GeneralConf.Send_Re_Activate_Request, (String) configDetails.get(GeneralConf.Send_Re_Activate_Request));
            context.setAttribute(GeneralConf.Enable_Registration_Agency_Mail, (String) configDetails.get(GeneralConf.Enable_Registration_Agency_Mail));
            context.setAttribute(GeneralConf.BL_Type_From_Reference, (String) configDetails.get(GeneralConf.BL_Type_From_Reference));
            context.setAttribute(GeneralConf.Enable_Verify_Agency_Mail, (String) configDetails.get(GeneralConf.Enable_Verify_Agency_Mail));
            context.setAttribute(GeneralConf.Enable_Apply_To_All_Customer, (String) configDetails.get(GeneralConf.Enable_Apply_To_All_Customer));
            context.setAttribute(GeneralConf.Duplicate_Commodity_Validation, (String) configDetails.get(GeneralConf.Duplicate_Commodity_Validation));
            context.setAttribute(GeneralConf.ENABLE_COMPLEX_CAPTCHA, (String) configDetails.get(GeneralConf.ENABLE_COMPLEX_CAPTCHA));

            context.setAttribute(GeneralConf.Enable_Cargo_Protect, (String) configDetails.get(GeneralConf.Enable_Cargo_Protect));

            context.setAttribute(GeneralConf.Enable_New_Text_blink, (String) configDetails.get(GeneralConf.Enable_New_Text_blink));
            context.setAttribute(GeneralConf.ENABLE_AP_CUSTCODE_SEARCH, (String) configDetails.get(GeneralConf.ENABLE_AP_CUSTCODE_SEARCH));
            context.setAttribute(GeneralConf.DISABLE_AP_PREVIOUS_BL_TRANSACTIONS, (String) configDetails.get(GeneralConf.DISABLE_AP_PREVIOUS_BL_TRANSACTIONS));
            context.setAttribute(GeneralConf.TEMPLATE_AP_DETAILS_BASED_ON_CONFIG, (String) configDetails.get(GeneralConf.TEMPLATE_AP_DETAILS_BASED_ON_CONFIG));
            context.setAttribute(GeneralConf.Enable_Auto_Redirect_To_Homepage, (String) configDetails.get(GeneralConf.Enable_Auto_Redirect_To_Homepage));
            context.setAttribute(GeneralConf.Enable_Menu_Endpoint_Validation, (String) configDetails.get(GeneralConf.Enable_Menu_Endpoint_Validation));
            context.setAttribute(GeneralConf.ENABLE_NAMED_CUSTOMER, (String) configDetails.get(GeneralConf.ENABLE_NAMED_CUSTOMER));
            context.setAttribute(GeneralConf.show_transportplan_track, (String) configDetails.get(GeneralConf.show_transportplan_track));
            context.setAttribute(GeneralConf.show_transportplan_date, (String) configDetails.get(GeneralConf.show_transportplan_date));
            context.setAttribute(GeneralConf.Multiple_agencies_single_location, (String) configDetails.get(GeneralConf.Multiple_agencies_single_location));
            context.setAttribute(GeneralConf.Enable_Schedule_Calendar, (String) configDetails.get(GeneralConf.Enable_Schedule_Calendar));
            context.setAttribute(GeneralConf.Single_SINofor_BL, (String) configDetails.get(GeneralConf.Single_SINofor_BL));
            context.setAttribute(GeneralConf.PHONE_NUMBER_LENGTH_5, (String) configDetails.get(GeneralConf.PHONE_NUMBER_LENGTH_5));
            context.setAttribute(GeneralConf.USER_PHONE_MANDATORY, (String) configDetails.get(GeneralConf.USER_PHONE_MANDATORY));
            context.setAttribute(GeneralConf.SHOW_MASKPORT_RECORDS, (String) configDetails.get(GeneralConf.SHOW_MASKPORT_RECORDS));
            // context.setAttribute(GeneralConf.Export_Restrict_cargoprotect_countrys, (String) configDetails.get(GeneralConf.Export_Restrict_cargoprotect_countrys));
            // context.setAttribute(GeneralConf.Import_Restrict_cargoprotect_countrys, (String) configDetails.get(GeneralConf.Import_Restrict_cargoprotect_countrys));
            context.setAttribute(GeneralConf.Disable_Auto_HSCode_Populate, (String) configDetails.get(GeneralConf.Disable_Auto_HSCode_Populate));
            context.setAttribute(GeneralConf.Display_HS_NAME_CODE, (String) configDetails.get(GeneralConf.Display_HS_NAME_CODE));
            context.setAttribute(GeneralConf.Refer_Routing_Transtime, (String) configDetails.get(GeneralConf.Refer_Routing_Transtime));
            context.setAttribute(GeneralConf.Ecom_Notice_Path, (String) configDetails.get(GeneralConf.Ecom_Notice_Path));
            context.setAttribute(GeneralConf.BOOKING_VOLUME_MANDATORY, (String) configDetails.get(GeneralConf.BOOKING_VOLUME_MANDATORY));
            context.setAttribute(GeneralConf.HSCodeMandatory, (String) configDetails.get(GeneralConf.HSCodeMandatory));

            context.setAttribute(GeneralConf.Enable_HSCode_Search, (String) configDetails.get(GeneralConf.Enable_HSCode_Search));
            context.setAttribute(GeneralConf.Enable_Open_Commodity, (String) configDetails.get(GeneralConf.Enable_Open_Commodity));
            context.setAttribute(GeneralConf.Display_Commodity_Code_Front, (String) configDetails.get(GeneralConf.Display_Commodity_Code_Front));
            context.setAttribute(GeneralConf.GENS, (String) configDetails.get(GeneralConf.GENS));
            context.setAttribute(GeneralConf.HIDE_TRACKING_VESSEL_DETAILS, (String) configDetails.get(GeneralConf.HIDE_TRACKING_VESSEL_DETAILS));
            context.setAttribute(GeneralConf.SI_CommodityGroup_Edit_Option, (String) configDetails.get(GeneralConf.SI_CommodityGroup_Edit_Option));
            context.setAttribute(GeneralConf.SHOW_READYTOCONFIRM, (String) configDetails.get(GeneralConf.SHOW_READYTOCONFIRM));
            context.setAttribute(GeneralConf.HIDE_AGENCYREF_BL_SUMMARY, (String) configDetails.get(GeneralConf.HIDE_AGENCYREF_BL_SUMMARY));
            context.setAttribute(GeneralConf.HIDE_AGENCYREF_BOOKING_SUMMARY, (String) configDetails.get(GeneralConf.HIDE_AGENCYREF_BOOKING_SUMMARY));
            context.setAttribute(GeneralConf.SHOW_DISCLAIMER_IN_BOOKINGSUMMARY, (String) configDetails.get(GeneralConf.SHOW_DISCLAIMER_IN_BOOKINGSUMMARY));
            context.setAttribute(GeneralConf.REMOVE_SAME_VESSEL_DETAILS, (String) configDetails.get(GeneralConf.REMOVE_SAME_VESSEL_DETAILS));
            context.setAttribute(GeneralConf.Display_Charge_Tab, (String) configDetails.get(GeneralConf.Display_Charge_Tab));

            context.setAttribute(GeneralConf.Display_Charge_Summary, (String) configDetails.get(GeneralConf.Display_Charge_Summary));
            context.setAttribute(GeneralConf.Enable_OOGRequired_Validation, (String) configDetails.get(GeneralConf.Enable_OOGRequired_Validation));

            context.setAttribute(GeneralConf.Enable_SI_Smart_Import, (String) configDetails.get(GeneralConf.Enable_SI_Smart_Import));
            context.setAttribute(GeneralConf.TENS, (String) configDetails.get(GeneralConf.TENS));
            context.setAttribute(GeneralConf.Enable_contactTitle, (String) configDetails.get(GeneralConf.Enable_contactTitle));
            context.setAttribute(GeneralConf.disable_DraftPrint_Country, (String) configDetails.get(GeneralConf.disable_DraftPrint_Country));
            context.setAttribute(GeneralConf.Enable_PreferredAgency, (String) configDetails.get(GeneralConf.Enable_PreferredAgency));
            context.setAttribute(GeneralConf.PreferredAgency, (String) configDetails.get(GeneralConf.PreferredAgency));
            context.setAttribute(GeneralConf.Enable_T2LFiling, (String) configDetails.get(GeneralConf.Enable_T2LFiling));
            context.setAttribute(GeneralConf.Enable_NVOCC_In_SI, (String) configDetails.get(GeneralConf.Enable_NVOCC_In_SI));
            context.setAttribute(GeneralConf.SI_NVOCC_Eligible_Country, (String) configDetails.get(GeneralConf.SI_NVOCC_Eligible_Country));
            context.setAttribute(GeneralConf.Enable_Tracking_NextPort, (String) configDetails.get(GeneralConf.Enable_Tracking_NextPort));
            context.setAttribute(GeneralConf.Enable_Telex_ReleaseType, (String) configDetails.get(GeneralConf.Enable_Telex_ReleaseType));

            context.setAttribute(GeneralConf.Enable_africaReqsMandatory, (String) configDetails.get(GeneralConf.Enable_africaReqsMandatory));
            context.setAttribute(GeneralConf.HIDE_FULLSCREEN_ICON, (String) configDetails.get(GeneralConf.HIDE_FULLSCREEN_ICON));
            context.setAttribute(GeneralConf.HIDE_REG_ADDRESS2, (String) configDetails.get(GeneralConf.HIDE_REG_ADDRESS2));
            context.setAttribute(GeneralConf.HIDE_REG_RECENTBL, (String) configDetails.get(GeneralConf.HIDE_REG_RECENTBL));
            context.setAttribute(GeneralConf.HIDE_CONTACTUS_STATE, (String) configDetails.get(GeneralConf.HIDE_CONTACTUS_STATE));
            context.setAttribute(GeneralConf.HIDE_CONTACTUS_MOBILE, (String) configDetails.get(GeneralConf.HIDE_CONTACTUS_MOBILE));
            context.setAttribute(GeneralConf.HIDE_LANG_OPTION, (String) configDetails.get(GeneralConf.HIDE_LANG_OPTION));

            context.setAttribute(GeneralConf.Reg_Control_location_Non_Mandatory, (String) configDetails.get(GeneralConf.Reg_Control_location_Non_Mandatory));
            context.setAttribute(GeneralConf.AgencyRefNo_Length, (String) configDetails.get(GeneralConf.AgencyRefNo_Length));

            context.setAttribute(GeneralConf.TENS_Documentation, (String) configDetails.get(GeneralConf.TENS_Documentation));
            context.setAttribute(GeneralConf.Enable_SISubmit_Party, (String) configDetails.get(GeneralConf.Enable_SISubmit_Party));

            context.setAttribute(GeneralConf.Enable_Create_Quote, (String) configDetails.get(GeneralConf.Enable_Create_Quote));
            context.setAttribute(GeneralConf.Quote_API_URL, (String) configDetails.get(GeneralConf.Quote_API_URL));

            context.setAttribute(GeneralConf.ALLOW_EXISTING_USER_TO_REGISTER, (String) configDetails.get(GeneralConf.ALLOW_EXISTING_USER_TO_REGISTER));
            context.setAttribute(GeneralConf.ENABLE_LOGIN_CAPTCHA, (String) configDetails.get(GeneralConf.ENABLE_LOGIN_CAPTCHA));
            context.setAttribute(GeneralConf.ENABLE_MASS_SUBMISSION_CAPTCHA, (String) configDetails.get(GeneralConf.ENABLE_MASS_SUBMISSION_CAPTCHA));

            context.setAttribute(GeneralConf.SHOW_FULLHISTORY_IN_TRACK_MAIL, (String) configDetails.get(GeneralConf.SHOW_FULLHISTORY_IN_TRACK_MAIL));

            context.setAttribute(GeneralConf.Enable_ContactUs_Captcha, (String) configDetails.get(GeneralConf.Enable_ContactUs_Captcha));
            context.setAttribute(GeneralConf.Enable_ChangePassword_Captcha, (String) configDetails.get(GeneralConf.Enable_ChangePassword_Captcha));
            context.setAttribute(GeneralConf.Enable_New_XSS_Pattern, (String) configDetails.get(GeneralConf.Enable_New_XSS_Pattern));

            context.setAttribute(GeneralConf.Enable_BLAmendStatusMsg, (String) configDetails.get(GeneralConf.Enable_BLAmendStatusMsg));
            context.setAttribute(GeneralConf.Enable_Login_Password_Encryption, (String) configDetails.get(GeneralConf.Enable_Login_Password_Encryption));
            context.setAttribute(GeneralConf.Enable_Zip_Code, (String) configDetails.get(GeneralConf.Enable_Zip_Code));
            context.setAttribute(GeneralConf.DPWShipperTypeList, (String) configDetails.get(GeneralConf.DPWShipperTypeList));
            context.setAttribute(GeneralConf.Enable_OTP_Password_Expired, (String) configDetails.get(GeneralConf.Enable_OTP_Password_Expired));
            context.setAttribute(GeneralConf.Enable_Password_Reset_By_URL, (String) configDetails.get(GeneralConf.Enable_Password_Reset_By_URL));
            context.setAttribute(GeneralConf.ContactUS_ClientCode_POPMSG, (String) configDetails.get(GeneralConf.ContactUS_ClientCode_POPMSG));
            context.setAttribute(GeneralConf.Enable_Country_Phone, (String) configDetails.get(GeneralConf.Enable_Country_Phone));
            context.setAttribute(GeneralConf.Enable_DashBoard_Filter, (String) configDetails.get(GeneralConf.Enable_DashBoard_Filter));
            context.setAttribute(GeneralConf.DISABLE_DEFAULT_VIEW, (String) configDetails.get(GeneralConf.DISABLE_DEFAULT_VIEW));
            context.setAttribute(GeneralConf.DISABLE_PREFERRED_VIEW, (String) configDetails.get(GeneralConf.DISABLE_PREFERRED_VIEW));
            context.setAttribute(GeneralConf.Enable_TWImprint, (String) configDetails.get(GeneralConf.Enable_TWImprint));   
            context.setAttribute(GeneralConf.ImprintLink, (String) configDetails.get(GeneralConf.ImprintLink));
            context.setAttribute(GeneralConf.Enable_EcomGate_AgencyMailID, (String) configDetails.get(GeneralConf.Enable_EcomGate_AgencyMailID));
            context.setAttribute(GeneralConf.Enable_Reefer_MixTemp_Req, (String) configDetails.get(GeneralConf.Enable_Reefer_MixTemp_Req));
            context.setAttribute(GeneralConf.Enable_cargoWeight_tooltip, (String) configDetails.get(GeneralConf.Enable_cargoWeight_tooltip));
            String tempMin = Validation.checkNull((String) bookingObj.getbkgconfigDetailsForHaulage("TMM"));
            context.setAttribute("Reefer_MinTemp_Limit", tempMin.length() > 0 ? tempMin : "");
            String tempMax = Validation.checkNull((String) bookingObj.getbkgconfigDetailsForHaulage("TMX"));
            context.setAttribute("Reefer_MaxTemp_Limit", tempMax.length() > 0 ? tempMax : "");
            context.setAttribute(GeneralConf.Logo_Name, (String) configDetails.get(GeneralConf.Logo_Name));
            context.setAttribute(GeneralConf.DATE_DIFFER_HIDE_ETA, (String) configDetails.get(GeneralConf.DATE_DIFFER_HIDE_ETA));
            context.setAttribute(GeneralConf.HIDE_MENU_ICON, (String) configDetails.get(GeneralConf.HIDE_MENU_ICON));
            context.setAttribute(GeneralConf.SHOW_EXTERNALRATE_TOTAL_AMOUNT, (String) configDetails.get(GeneralConf.SHOW_EXTERNALRATE_TOTAL_AMOUNT)); 

            context.setAttribute(GeneralConf.ENABLE_HSCODE_LENGTH_VALIDATION, (String) configDetails.get(GeneralConf.ENABLE_HSCODE_LENGTH_VALIDATION));

            context.setAttribute(GeneralConf.HIDE_MASKPORT_RECORDS, (String) configDetails.get(GeneralConf.HIDE_MASKPORT_RECORDS));
            context.setAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER, (String) configDetails.get(GeneralConf.HIDE_MASKPORT_CONTAINER));
            context.setAttribute(GeneralConf.Enable_TaxId, (String) configDetails.get(GeneralConf.Enable_TaxId));

            context.setAttribute(GeneralConf.USE_CUSTOMETSTATEMENT_SP, (String) configDetails.get(GeneralConf.USE_CUSTOMETSTATEMENT_SP));

            context.setAttribute(GeneralConf.LANDING_PAGE_NEW_BOOKING, (String) configDetails.get(GeneralConf.LANDING_PAGE_NEW_BOOKING));
            context.setAttribute(GeneralConf.ENABLE_COMP_MOBILE, (String) configDetails.get(GeneralConf.ENABLE_COMP_MOBILE));
            context.setAttribute(GeneralConf.EITHER_COMP_MOB_OR_PHONE_MANDATORY, (String) configDetails.get(GeneralConf.EITHER_COMP_MOB_OR_PHONE_MANDATORY));
            context.setAttribute(GeneralConf.CLEAR_TEMPERATURE_WHILE_COPY, (String) configDetails.get(GeneralConf.CLEAR_TEMPERATURE_WHILE_COPY));
            context.setAttribute(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN, (String) configDetails.get(GeneralConf.ALLOW_INCOMPLETED_REG_TO_LOGIN));
            context.setAttribute(GeneralConf.INCOMPLETED_REG_MENU_LIST, (String) configDetails.get(GeneralConf.INCOMPLETED_REG_MENU_LIST)); 
            context.setAttribute(GeneralConf.PAYMENT_hISTORY_COMP_CODE, (String) configDetails.get(GeneralConf.PAYMENT_hISTORY_COMP_CODE));
            context.setAttribute(GeneralConf.PAYMENT_hISTORY_DATE_INTERVAL, (String) configDetails.get(GeneralConf.PAYMENT_hISTORY_DATE_INTERVAL));
            context.setAttribute(GeneralConf.ENABLE_CONTACTUS_CAPTCHA_DEFAULT, (String) configDetails.get(GeneralConf.ENABLE_CONTACTUS_CAPTCHA_DEFAULT));
            context.setAttribute(GeneralConf.SERVICE_LOGIN_NAME, (String) configDetails.get(GeneralConf.SERVICE_LOGIN_NAME));

            if ("Y".equals((String) configDetails.get(GeneralConf.HIDE_MASKPORT_RECORDS))) {
                context.setAttribute("MaskPortListObj", ecomMaskObj.getmaskportlist());
                context.setAttribute("MaskPortListGson", new Gson().toJson(ecomMaskObj.getmaskportlist()));
            } else {
                context.setAttribute("MaskPortListObj", new java.util.ArrayList<>());
                context.setAttribute("MaskPortListGson", "[]");
            }

            context.setAttribute(GeneralConf.COOKIE_MSG, (String) configDetails.get(GeneralConf.COOKIE_MSG));
            context.setAttribute(GeneralConf.REMOVE_COPYRIGHTS_LINK, (String) configDetails.get(GeneralConf.REMOVE_COPYRIGHTS_LINK));
            context.setAttribute(GeneralConf.CENTRALIZE_COPYRIGHTS, (String) configDetails.get(GeneralConf.CENTRALIZE_COPYRIGHTS));

            // Stripe Payment Configuration - loaded from DB
            context.setAttribute(GeneralConf.Stripe_PublicKey, (String) configDetails.get(GeneralConf.Stripe_PublicKey));
            context.setAttribute(GeneralConf.Stripe_SecretKey, (String) configDetails.get(GeneralConf.Stripe_SecretKey));
            context.setAttribute(GeneralConf.Stripe_WebhookKey, (String) configDetails.get(GeneralConf.Stripe_WebhookKey));
            context.setAttribute(GeneralConf.Stripe_Payment_Currency, (String) configDetails.get(GeneralConf.Stripe_Payment_Currency));
            context.setAttribute(GeneralConf.Enable_Stripe_Payment, (String) configDetails.get(GeneralConf.Enable_Stripe_Payment));

            // N-Genius Payment Configuration - loaded from DB
            context.setAttribute(GeneralConf.Enable_Ngenius_Payment,       (String) configDetails.get(GeneralConf.Enable_Ngenius_Payment));
            context.setAttribute(GeneralConf.Ngenius_API_Key,              (String) configDetails.get(GeneralConf.Ngenius_API_Key));
            context.setAttribute(GeneralConf.Ngenius_Outlet_Id,            (String) configDetails.get(GeneralConf.Ngenius_Outlet_Id));
            context.setAttribute(GeneralConf.Ngenius_Payment_Currency,     (String) configDetails.get(GeneralConf.Ngenius_Payment_Currency));
            context.setAttribute(GeneralConf.Ngenius_Webhook_Header_Key,   (String) configDetails.get(GeneralConf.Ngenius_Webhook_Header_Key));
            context.setAttribute(GeneralConf.Ngenius_Webhook_Header_Value, (String) configDetails.get(GeneralConf.Ngenius_Webhook_Header_Value));
            context.setAttribute(GeneralConf.Ngenius_Webhook_Encryption_Secret,
                    (String) configDetails.get(GeneralConf.Ngenius_Webhook_Encryption_Secret));
            context.setAttribute(GeneralConf.Ngenius_Base_URL,             (String) configDetails.get(GeneralConf.Ngenius_Base_URL));
            context.setAttribute(GeneralConf.Payment_Event_Notification_Email,
                    (String) configDetails.get(GeneralConf.Payment_Event_Notification_Email));

            context.setAttribute(GeneralConf.Save_Template_Message, (String) configDetails.get(GeneralConf.Save_Template_Message));

            // it for temp --need to remove after testing
            // context.setAttribute("Enable_New_XSS_Pattern", (String) configDetails.get("Enable_New_XSS_Pattern"));
            context.setAttribute(GeneralConf.ENABLE_ADDINFO_BLTYPE, (String) configDetails.get(GeneralConf.ENABLE_ADDINFO_BLTYPE));
            context.setAttribute(GeneralConf.ENABLE_ACCOUNT_LOCK_WRONGPASSWORD, (String) configDetails.get(GeneralConf.ENABLE_ACCOUNT_LOCK_WRONGPASSWORD));

            
            context.setAttribute(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED, (String) configDetails.get(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED));
            context.setAttribute(GeneralConf.Fetch_Schedule_From_API, (String) configDetails.get(GeneralConf.Fetch_Schedule_From_API));
            context.setAttribute(GeneralConf.Schedule_Date_Range_Days, (String) configDetails.get(GeneralConf.Schedule_Date_Range_Days));

            context.setAttribute(GeneralConf.SHOW_DATA_WITH_NO_MOVEMENTENTRY, (String) configDetails.get(GeneralConf.SHOW_DATA_WITH_NO_MOVEMENTENTRY));

//

			context.setAttribute(GeneralConf.Enable_Multiple_DG_Details, (String) configDetails.get(GeneralConf.Enable_Multiple_DG_Details));
            context.setAttribute(GeneralConf.TARIFFSUMMARY_CHECK_ROUTING, (String) configDetails.get(GeneralConf.TARIFFSUMMARY_CHECK_ROUTING));
            context.setAttribute(GeneralConf.ENABLE_COUNTRY_CODE, (String) configDetails.get(GeneralConf.ENABLE_COUNTRY_CODE));
            context.setAttribute(GeneralConf.Enable_Google_Address_Search, (String) configDetails.get(GeneralConf.Enable_Google_Address_Search));
            context.setAttribute(GeneralConf.Google_Maps_API_Key, (String) configDetails.get(GeneralConf.Google_Maps_API_Key));
            context.setAttribute(GeneralConf.Hide_AP_CUST_BKG_Master, (String) configDetails.get(GeneralConf.Hide_AP_CUST_BKG_Master));

            GeneralConf.Enable_Sout_Log = ("Y".equalsIgnoreCase((String) configDetails.get("Enable_Sout_Log")) ? true : false);
            GeneralConf.Enable_Sout_With_Detail = ("Y".equalsIgnoreCase((String) configDetails.get("Enable_Sout_With_Detail")) ? true : false);
            System.out.println("[ECOM-WAR] logPrinter Enable_Sout_Log: " + GeneralConf.Enable_Sout_Log);
            //System.out.println("[ECOM-WAR] logPrinter configDetails.get(GeneralConf.Enable_Sout_Log): " + configDetails.get(GeneralConf.Enable_Sout_Log));
            context.setAttribute("VGMDISCLAIMERMSG", vgmDisclaimerMsg);
            context.setAttribute("NEWALLDISCLAIMERMSG", allDisclaimerMsg);
            context.setAttribute("VGMDISCLAIMERMSGLANG", vgmDisclaimerMsglang);
            System.out.println("[ECOM-WAR] EcomCacheLoader >>(String) configDetails.get(GeneralConf.V1FEEDERCUTOFFDATE)>>" + (String) configDetails.get(GeneralConf.V1FEEDERCUTOFFDATE)
                    + ">>>configDetails.get(GeneralConf.AGENCYLINK)>>" + configDetails.get(GeneralConf.AGENCYLINK));
            context.setAttribute("MAILALERTACTIVITYLIST", mailActivityConfig);

            context.setAttribute("CONTEXT_COUNTRYCITY", countryCityObj);
            context.setAttribute("CONTEXT_AGENCYLOCATION", agencyLocationMap);
            context.setAttribute("PORT_COUNTRY_MAP", portCountryMap);
            context.setAttribute("Version", Version);
            EcomConsts.SET_CARRIER_CODE = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
            //get Fieldname Configuration list
            List<FieldConfigVO> fieldNameList = locobj.getFieldNameConfigList();
            if (fieldNameList != null) {
                fieldConfigMap = new HashMap<String, String>();
                fieldShipmentMap = new HashMap<String, String>();
                fieldNameConfigList = new ArrayList();
                fieldNameConfigList.add(new LabelValueBean("Select Field Name", ""));
                fieldNameList.forEach(configvo -> {
                    fieldNameConfigList.add(new LabelValueBean(configvo.getFieldName(), configvo.getFieldCode()));
                    fieldConfigMap.put(configvo.getFieldCode(), configvo.getFieldName());
                    fieldShipmentMap.put(configvo.getFieldCode(), configvo.getShipmentType());
                });
                context.setAttribute("FIELDNAMECONFIGLIST", fieldNameConfigList);
                context.setAttribute("FIELDNAMECONFIGMAP", fieldConfigMap);
                context.setAttribute("FIELDCONFIGSHIPMENTMAP", fieldShipmentMap);
                System.out.println("[ECOM-WAR] EcomCacheLoader fieldShipmentMap......." + fieldShipmentMap);
            }

            //Load restricted service in cache
            Map<String, List<String>> resSrvcMap = locobj.getSvrcRestrictCacheList();
            context.setAttribute("CACHERESTRICTEDSERVICES", resSrvcMap);
            context.setAttribute("globalsettings", globalconfig);
            context.setAttribute("PODINTRANSITMAPCONTEXT", transitMap);

            Map<String, String> emptyPickUpMap = locobj.getEmptyPickUpMap();
            context.setAttribute("EMPTYPICKUPMAP", emptyPickUpMap);

            Map pluginmap = locobj.getLrpPluginDetails((String) context.getAttribute(GeneralConf.LRP_PLUGINID_CONFIG), (String) context.getAttribute(GeneralConf.EBK_PLUGINID_CONFIG));
            if (pluginmap != null) {
                context.setAttribute("LRP_PLUGIN_DETAILS", pluginmap.get("BKG"));
                context.setAttribute("EBK_PLUGIN_DETAILS", pluginmap.get("EBK"));
                context.setAttribute("ECC_PLUGIN_DETAILS", pluginmap.get("ECC"));
            }
            context.setAttribute(GeneralConf.Default_Carriage_Contract, locobj.getDefaultCarriageContract());
            String result = Validation.checkNull((String) bookingObj.getbkgconfigDetailsForHaulage("BCT"));
            if (result != null && result.length() > 0) {
                context.setAttribute("DefOriginDestHaulValue", result);
            } else {
                context.setAttribute("DefOriginDestHaulValue", "C");
            }

            String Ventilation = Validation.checkNull((String) bookingObj.getbkgconfigDetailsForHaulage("VUN"));
            if (Ventilation != null && Ventilation.length() > 0) {
                context.setAttribute("Ventilation", "(" + Ventilation + ")");
            } else {
                context.setAttribute("Ventilation", "(CBM)");
            }

            context.setAttribute(GeneralConf.Config_Msg_Lang, locobj.getMessagesBasedOnLang());
            Map<String, List<HotlineContactsVO>> hotlineContactsMap = locobj.getHotlineContacts();
            context.setAttribute("HotlineContacts", hotlineContactsMap);
            context.setAttribute("VesselRestrictionDetails", locobj.getVesselDetailsRestriction());
            context.setAttribute("Terms_and_Conditions", Validation.checkNull(ecomreg.getTerms("Registration")));
            context.setAttribute("EQPPayloadWeight", locobj.getEqptypeAndMaxWeight());
            List<RegisterMenuVO> menulist = (List<RegisterMenuVO>) context.getAttribute("usermenu");
            String VGMMenuCategory = menulist.stream().filter(o -> "VGM".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getCategory).reduce("", String::concat);
            context.setAttribute("VGMMenuCategory", VGMMenuCategory);
            context.setAttribute("VesselIMODetails", locobj.getVesselIMODetails());
            context.setAttribute("Insurance_Terms_and_Conditions", Validation.checkNull(locobj.getMessageContent("E-Booking", "insurance")));
            context.setAttribute("Insurance_Terms_and_Conditions_Lang", Validation.checkNull(locobj.getMessageContentLang("E-Booking", "insurance", "ch")));
            InsuranceUtils insoj = new InsuranceUtils();
            context.setAttribute("Insurance_Survey_List", insoj.getSurveyList(Validation.checkNull(locobj.getMessageContent("E-Booking", "ins_survey"))));
            context.setAttribute("Insurance_Survey_List_Lang", insoj.getSurveyList(Validation.checkNull(locobj.getMessageContentLang("E-Booking", "ins_survey", "ch"))));
            context.setAttribute("Registration_Success_Message", Validation.checkNull(locobj.getMessageContent(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA)));
            context.setAttribute("Registration_Link_Success_Message", Validation.checkNull(locobj.getMessageContent("Registration", "linksuces")));
            context.setAttribute("Registration_Link_Auto_Success_Message", Validation.checkNull(locobj.getMessageContent("Registration", "autoregsc")));
            context.setAttribute("BKG_Auto_Process_Msg", Validation.checkNull(locobj.getMessageContent("E-Booking", "bkgsucs")));
            if ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Enable_Password_Validation))) {
                Map passwordmap = (Map) locobj.getEcomPasswordConfig("PWD_MIN_LEN,PWD_MAX_LEN,NOTALLOWABLE_PWDS,PWD_UPPERCASE,PWD_LOWERCASE,PWD_NUMERIC,PWD_SPLCHAR");
                if (passwordmap != null && passwordmap.size() > 0) {
                    String passwordcontain = "";
                    if ("Y".equalsIgnoreCase(Validation.checkNull((String) passwordmap.get("PWD_UPPERCASE")))) {
                        passwordcontain += "Upper,";
                    }
                    if ("Y".equalsIgnoreCase(Validation.checkNull((String) passwordmap.get("PWD_LOWERCASE")))) {
                        passwordcontain += "Lower,";
                    }
                    if ("Y".equalsIgnoreCase(Validation.checkNull((String) passwordmap.get("PWD_NUMERIC")))) {
                        passwordcontain += "Numeric,";
                    }
                    if ("Y".equalsIgnoreCase(Validation.checkNull((String) passwordmap.get("PWD_SPLCHAR")))) {
                        passwordcontain += "Special,";
                    }
                    if (passwordcontain.length() > 0) {
                        passwordcontain = passwordcontain.substring(0, passwordcontain.length() - 1);
                    }
                    String passnote = "1) Password should not contain \"" + Validation.checkNull((String) passwordmap.get("NOTALLOWABLE_PWDS")) + " \".<br> 2) Password should contain Min of " + Validation.checkNull((String) passwordmap.get("PWD_MIN_LEN")) + " and Max of " + Validation.checkNull((String) passwordmap.get("PWD_MAX_LEN")) + " characters. <br> 3) Password should contain at least one " + passwordcontain + " Character.";
                    System.out.println("[ECOM-WAR] EcomCacheLoader passnote==> " + passnote);
                    context.setAttribute("PasswordValidationNote", passnote);
                }
            }
            context.setAttribute("Reg_File_Path", ecomreg.getfileuploadsharepath());
             context.setAttribute("Mask_Port_List", bookingObj.getMaskPortList());
            context.setAttribute("OpenCommodityJson", ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Enable_Open_Commodity)) ? locobj.getOpenCommodityList() : "[]"));
            context.setAttribute("EuropeCountryJson", ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.GENS)) ? locobj.getEUCountryList() : "[]"));
            HashSet<String> draftPrintAgency = bookingObj.getDisableDraftPrintAgency((String) context.getAttribute(GeneralConf.disable_DraftPrint_Country));
            context.setAttribute("disableDraftPrintAgency", draftPrintAgency);
            context.setAttribute("preferredAgencyJson", "Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Enable_PreferredAgency)) ? Validation.checkNull((String) context.getAttribute(GeneralConf.PreferredAgency)) : "{}");
            context.setAttribute("preferredAgencyNameJson", "Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Enable_PreferredAgency)) ? Validation.checkNull((String) configDetails.get(GeneralConf.PreferredAgencyName)) : "{}");
            context.setAttribute("africaReqsContryIdJson", "Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Enable_PreferredAgency)) ? Validation.checkNull((String) configDetails.get(GeneralConf.africaReqsContryIdJson)) : "[]");
            context.setAttribute("BKGConfPrintShenzenAgencyPort", Validation.checkNull((String) bookingObj.getbkgconfigDetailsForHaulage("CNH")));

            context.setAttribute("customernameJson", Validation.checkNull((String) configDetails.get(GeneralConf.sISubmit_Party_CusType)));

            if ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Enable_Create_Quote))) {
                context.setAttribute("TransportServiceList", bookingObj.getTransportService());
                context.setAttribute("TOSList", bookingObj.getTOS());
            }

            // Changes for analytics purpose by Prakash Mani
            context.setAttribute(GeneralConf.Enable_PlatformAnalytics, (String) configDetails.get(GeneralConf.Enable_PlatformAnalytics));
            context.setAttribute(GeneralConf.Disable_Common_Validation, (String) configDetails.get(GeneralConf.Disable_Common_Validation));
            //end of Changes for analytics purpose by Prakash Mani
            context.setAttribute("AccountLockedMessageForLoginFail", Validation.checkNull(locobj.getMessageContent("E-COM", "LOGINFAIL")));
            context.setAttribute(GeneralConf.ENABLE_SCHEDULE_DISCLAIMER, (String) configDetails.get(GeneralConf.ENABLE_SCHEDULE_DISCLAIMER));
            context.setAttribute(GeneralConf.ENABLE_SCHEDULE_CARD_CARBON, (String) configDetails.get(GeneralConf.ENABLE_SCHEDULE_CARD_CARBON));
            context.setAttribute(GeneralConf.COMPANY_NAME_SIMILARITY_THRESHOLD, Double.valueOf(parseConfigDouble( Validation.checkNull((String) configDetails.get(GeneralConf.COMPANY_NAME_SIMILARITY_THRESHOLD)))));
            context.setAttribute(GeneralConf.CITY_NAME_SIMILARITY_THRESHOLD, Double.valueOf(parseConfigDouble( Validation.checkNull((String) configDetails.get(GeneralConf.CITY_NAME_SIMILARITY_THRESHOLD)))));
            context.setAttribute("ScheduleDisclaimerMessage", Validation.checkNull(locobj.getMessageContent("Schedule", "DISCLAIMER")));


            context.setAttribute(GeneralConf.Enable_Cancel_Option_Confirmed_BKG, (String) configDetails.get(GeneralConf.Enable_Cancel_Option_Confirmed_BKG));
            context.setAttribute(GeneralConf.Disable_ConfirmBKG_Validation, (String) configDetails.get(GeneralConf.Disable_ConfirmBKG_Validation));
            context.setAttribute(GeneralConf.Enable_TermsCondition_Accept, (String) configDetails.get(GeneralConf.Enable_TermsCondition_Accept));
            List<String> bkgCancelReasonList = null;
            String getBkgCancelReasons = (String) configDetails.get(GeneralConf.BKG_Cancel_Reason_List);
            if (getBkgCancelReasons != null && getBkgCancelReasons.length() > 0) {
                bkgCancelReasonList = new ArrayList(Arrays.asList(getBkgCancelReasons.split(",")));
            }
            context.setAttribute(GeneralConf.BKG_Cancel_Reason_List, bkgCancelReasonList);
            context.setAttribute("BKG_Cancel_TermsCondition", Validation.checkNull(locobj.getMessageContent("E-Booking", "BKG_CAN_TC")));
            context.setAttribute("BKG_ConfEdit_TermsCondition", Validation.checkNull(locobj.getMessageContent("E-Booking", "BKGCNFEDIT")));
            context.setAttribute("BL_ConfEdit_TermsCondition", Validation.checkNull(locobj.getMessageContent("BL", "BLCNFEDIT")));

            CarbonLogInitializer.initialize(context);

            System.out.println("[ECOM-WAR] EcomCacheLoader End Of CacheLoader+++++++>>>>");
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] EcomCacheLoader Exception in CacheLoader+++++++>>>>");
            e.printStackTrace();
            throw e;
        }
    }

    public void cacheLoaderWS(ServletContext context) {
        EcomV1MasterBeanRemote v1MasObj = EJBLookUp.getV1MasterBeanLookUp();
        EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
        try {
            boolean frontcmdcode = ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.Display_Commodity_Code_Front))) ? true : false;
            System.out.println("[ECOM-WAR] EcomCacheLoader >>cacheLoader method2>>");
            CountryVO conVoObj;
            GeneralVO portVoList;
            GeneralVO vessVoList;
            ArrayList portListObj = null;
            ArrayList countyListObj = null;
            ArrayList commodityListObj = null;
            ArrayList commodityListJSONObj = null;
            ArrayList packageListJSONObj = null;
            ArrayList competitionListJSONObj = null;
            ArrayList countryListJSONObj = null;
            ArrayList agencyLsitObj = null;
            ArrayList eqpList = null;
            ArrayList packageListObj = null;
            ArrayList shipmentTermListObj = null;
            ArrayList RateRefListObj = null;
            ArrayList shttListObj = null;
            Hashtable portlist = new Hashtable();
            Hashtable comlist = new Hashtable();
            LinkedHashMap eqplist = new LinkedHashMap();
            Hashtable terlist = new Hashtable();
            Hashtable serlist = new Hashtable();
            Hashtable contrylist = new Hashtable();
            List cmdtyNewJSONObj = null;
            Map<String, String> terminalMap = new HashMap<String, String>();
            Hashtable citylist = new Hashtable();
            Hashtable vessellist = new Hashtable();
            Hashtable dgclassht = new Hashtable();
            Hashtable countryHashObj = new Hashtable();
            Hashtable paclist = new Hashtable();
            Hashtable shptlist = new Hashtable();
            List portListObjj = null;
            List vesselListObjj = new ArrayList();
            ArrayList eqpListColl = null;
            ArrayList seaPortListObj = new ArrayList();
            WebResource webResource = null;
            String WS_Url = null;
            String result = null;
            GeneralVOList obj = null;
            Map<String, Map<String, String>> masterMap = null;
            HashMap statemap = new HashMap();
            try {
                obj = new GeneralVOList();
                ArrayList listPort = null;
                Client client = Client.create();
                String commonWSPath = (String) context.getAttribute(GeneralConf.WSURL);
                System.out.println("[ECOM-WAR] EcomCacheLoader ================>>>>>commonWSPath" + commonWSPath);
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=port";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listPort = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /* end port */
 /*seaPortList*/
                ArrayList listseaPort = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=seaport";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listseaPort = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*end SeaPortList*/

 /* vessel*/
                ArrayList veslist = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=vessel";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    veslist = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /* End Vessel*/

 /* country*/
                ArrayList listcountry = null;
                try {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ===>>>Inside the country<<<====");
                    //
                    WS_Url = commonWSPath + "/masterdata?type=country";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    CountryList cobj = new CountryList();
                    JAXBContext jc = JAXBContext.newInstance(cobj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    cobj = (CountryList) marshaller.unmarshal(new StringReader(result));
                    listcountry = (ArrayList) cobj.getCountrylst();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ======>country list exception");
                    ex.printStackTrace();
                }
                /*End Country*/
 /* city*/
                ArrayList listcity = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=city";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listcity = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End city*/
 /* Terminal*/
 /*             ArrayList listPortTerminals = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=portTerminal";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listPortTerminals = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }*/
 /*End Terminal*/
 /* commodity*/
                ArrayList listcommodity = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=commodity";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listcommodity = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*end Commodity*/

 /*Eqp*/
                ArrayList listeqp = null;
                try {
                    //

                    WS_Url = commonWSPath + "/masterdata?type=eqp";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listeqp = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*Eqp End*/

 /*strEqp*/
                ArrayList liststreqp = null;
                try {
                    //

                    WS_Url = commonWSPath + "/masterdata?type=streqp";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    liststreqp = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*srtEqp End*/

 /* Servoice*/
                ArrayList listservice = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=service";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listservice = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End Service*/
 /* terminal*/
                ArrayList listterminal = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=terminal";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listterminal = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End terminal*/

 /* compe*/
                ArrayList listcompe = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=compe";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listcompe = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End compe*/

 /* Agency*/
                ArrayList listagency = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=agency";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listagency = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End Agency*/

 /* Package*/
                ArrayList listpack = null;
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=package";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listpack = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End Package*/

 /* shipment*/
                ArrayList listship = new ArrayList();
                try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=shipment";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    listship = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End shipment*/

 /* shipment*/
                ArrayList lstdgclass = new ArrayList();
                try {
                    WS_Url = commonWSPath + "/masterdata?type=dgclass";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    lstdgclass = (ArrayList) obj.getPortvo();

                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }
                /*End shipment*/

 /* State*/
                // ArrayList liststate = null;
                /* try {
                    //
                    WS_Url = commonWSPath + "/masterdata?type=state";
                    webResource = client.resource(WS_Url);
                    result = webResource.get(String.class);
                    JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                    Unmarshaller marshaller = jc.createUnmarshaller();
                    obj = (GeneralVOList) marshaller.unmarshal(new StringReader(result));
                    liststate = (ArrayList) obj.getPortvo();
                    //
                } catch (Exception ex) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader ============>" + ex);
                }*/
 /*End State*/
                int lendgclass = lstdgclass.size();
                for (int i = 0; i < lendgclass; i++) {
                    portVoList = (GeneralVO) lstdgclass.get(i);
                    dgclassht.put(portVoList.getCode(), portVoList.getName());
                }

                /*Port List Deatils*/
                if (listPort != null) {
                    portListObjj = new ArrayList();
                    int lenport = listPort.size();
                    for (int i = 0; i < lenport; i++) {
                        portVoList = (GeneralVO) listPort.get(i);
                        String res = portVoList.getCode().toUpperCase() + " - " + portVoList.getName();
                        portListObjj.add(res);
                    }
                }
                //    seaPortListObj = (ArrayList) AllObj.get(18);
                if (listseaPort != null) {
                    int lenportlist = listseaPort.size();
                    for (int i = 0; i < lenportlist; i++) {
                        portVoList = (GeneralVO) listseaPort.get(i);
                        String res = portVoList.getCode() + " - " + portVoList.getName();
                        seaPortListObj.add(res);
                    }
                }
                //   portListObj = (ArrayList) AllObj.get(0);
                portListObj = (ArrayList) listPort;
                int len = portListObj.size();
                for (int i = 0; i < len; i++) {
                    portVoList = (GeneralVO) portListObj.get(i);
                    portlist.put(portVoList.getCode().toUpperCase(), portVoList.getName());

                }
                /* End Port List Deatils*/

 /*commodity List*/
                commodityListObj = new ArrayList();
                commodityListObj.add(new LabelValueBean("Select Commodity", ""));
                ArrayList arr = null, arr1 = null;
                if (listcommodity != null) {
                    int lencomm = listcommodity.size();
                    arr = new ArrayList();
                    arr1 = new ArrayList();
                    for (int i = 0; i < lencomm; i++) {
                        portVoList = (GeneralVO) listcommodity.get(i);
                        commodityListObj.add(new LabelValueBean(portVoList.getName(), portVoList.getCode())); // commodityListObj = (ArrayList) AllObj.get(3);
                        String strcommjson = portVoList.getName() + " ~~ " + portVoList.getCode();
                        String newstrcommjson = portVoList.getName().toUpperCase() + " ~~ " + portVoList.getCode().toUpperCase();
                        arr1.add(newstrcommjson);
                        arr.add(strcommjson);  //commodityListJSONObj = (ArrayList) AllObj.get(12);
                        if (frontcmdcode) {
                            comlist.put(portVoList.getCode(), "(" + portVoList.getCode() + ") " + Validation.checkNull(portVoList.getName()).replaceAll("\"", "\\\\\"")); //comlist = (Hashtable) AllObj.get(8);
                        } else {
                            comlist.put(portVoList.getCode(), Validation.checkNull(portVoList.getName()).replaceAll("\"", "\\\\\"")); //comlist = (Hashtable) AllObj.get(8);
                        }
                    }
                }
                String fakCommodity = (String) context.getAttribute(GeneralConf.FAKCOMMODITY);
                String fakComdtyCode = "";
                String fakComdtyName = "";
                System.out.println("[ECOM-WAR] EcomCacheLoader fakCommodity>>>>" + fakCommodity);
                fakCommodity = fakCommodity != null ? fakCommodity : "";
                if (fakCommodity != null && fakCommodity.length() > 0) {
                    String fakCommodityArr[] = fakCommodity.split("~~");
                    if (fakCommodityArr.length == 2) {
                        fakComdtyCode = Validation.checkNull(fakCommodityArr[0]).trim();
                        fakComdtyName = Validation.checkNull(fakCommodityArr[1]).trim();
                    }
                    if (frontcmdcode) {
                        comlist.put(fakComdtyCode, "(" + fakComdtyCode + ") " + fakComdtyName);
                    } else {
                        comlist.put(fakComdtyCode, fakComdtyName);
                    }
                }
                //Adding fak commodity

                commodityListJSONObj = (ArrayList) arr;
                cmdtyNewJSONObj = (ArrayList) arr1;
                HashMap eqpMatchValue = new HashMap();
                /*end Commodity list*/
                if (liststreqp != null) {
                    int lenstreqp = liststreqp.size();
                    System.out.println("[ECOM-WAR] EcomCacheLoader .........eqplistsize...." + lenstreqp);
                    eqpList = new ArrayList();
                    eqpList.add(new LabelValueBean("-- Select --", ""));
                    for (int i = 0; i < lenstreqp; i++) {
                        portVoList = (GeneralVO) liststreqp.get(i);
                        eqpList.add(new LabelValueBean(portVoList.getName(), portVoList.getCode()));
                        eqpMatchValue.put(portVoList.getCode(), portVoList.getName());
                    }
                }
                if (listeqp != null) {
                    eqpListColl = new ArrayList();
                    int leneqp = listeqp.size();
                    for (int i = 0; i < leneqp; i++) {
                        portVoList = (GeneralVO) listeqp.get(i);
                        eqplist.put(portVoList.getCode(), portVoList.getName());
                        eqpListColl.add(new LabelValueBean(portVoList.getName(), portVoList.getCode()));
                    }
                }
                /* end Eqp List*/

 /* VesselList */
                if (veslist != null) {
                    int lenvessel = veslist.size();
                    for (int i = 0; i < lenvessel; i++) {
                        vessVoList = (GeneralVO) veslist.get(i);
                        String res = vessVoList.getCode() + " - " + vessVoList.getName();
                        vesselListObjj.add(res);
                        vessellist.put(Validation.checkNull(vessVoList.getCode()).toUpperCase(), vessVoList.getName());
                    }
                }
                /*End VesselList*/

 /* Country List*/
                if (listcountry != null) {
                    countyListObj = (ArrayList) listcountry;
                    int lenn = countyListObj.size();
                    countryListJSONObj = new ArrayList();
                    for (int i = 0; i < lenn; i++) {
                        conVoObj = (CountryVO) countyListObj.get(i);
                        contrylist.put(conVoObj.getCountrycode(), conVoObj.getCountryname().toUpperCase());
                        String strcour = conVoObj.getCountrycode();
                        countryHashObj.put(strcour.toUpperCase(), conVoObj.getCountryname().toUpperCase());
                        String con = conVoObj.getCountrycode() + " - " + conVoObj.getCountryname().toUpperCase();
                        countryListJSONObj.add(con);
                    }
                }
                /* End Country List*/

                // serlist = (Hashtable) AllObj.get(5);
                if (listservice != null) {
                    int lensevcie = listservice.size();
                    for (int i = 0; i < lensevcie; i++) {
                        portVoList = (GeneralVO) listservice.get(i);
                        serlist.put(Validation.checkNull(portVoList.getCode()).toUpperCase(), portVoList.getName());    //  vesselListObj = (ArrayList) AllObj.get(1);
                    }
                }
                if (listterminal != null) {
                    int lenterminal = listterminal.size();
                    for (int i = 0; i < lenterminal; i++) {
                        portVoList = (GeneralVO) listterminal.get(i);
                        terlist.put(portVoList.getCode(), portVoList.getName());    //  terlist = (Hashtable) AllObj.get(6);
                    }
                }

                //citylist = (Hashtable) AllObj.get(7);
                if (listcountry != null) {
                    int lencity = listcity.size();
                    for (int i = 0; i < lencity; i++) {
                        portVoList = (GeneralVO) listcity.get(i);
                        citylist.put(portVoList.getCode(), portVoList.getName());    //  terlist = (Hashtable) AllObj.get(6);
                    }
                }
                //   competitionListJSONObj = (ArrayList) AllObj.get(13);
                if (listcompe != null) {
                    competitionListJSONObj = new ArrayList();
                    int lencomp = listcompe.size();
                    for (int i = 0; i < lencomp; i++) {
                        portVoList = (GeneralVO) listcompe.get(i);
                        String strcomp = portVoList.getCode() + " - " + portVoList.getName();
                        competitionListJSONObj.add(strcomp);    //  terlist = (Hashtable) AllObj.get(6);
                    }
                }
                if (listagency != null) {
                    agencyLsitObj = new ArrayList();
                    int lenagency = listagency.size();
                    for (int i = 0; i < lenagency; i++) {
                        portVoList = (GeneralVO) listagency.get(i);
                        String stragency = portVoList.getCode() + " - " + portVoList.getName();
                        agencyLsitObj.add(stragency);    //  terlist = (Hashtable) AllObj.get(17);
                    }
                }

                packageListObj = new ArrayList();
                packageListObj.add(new LabelValueBean("Select Package", ""));
                List packListVal = null;
                if (listpack != null) {
                    int lenpack = listpack.size();
                    packageListJSONObj = new ArrayList();
                    packListVal = new ArrayList();
                    for (int i = 0; i < lenpack; i++) {
                        portVoList = (GeneralVO) listpack.get(i);
                        packageListObj.add(new LabelValueBean(portVoList.getName(), portVoList.getCode()));//(ArrayList) AllObj.get(19);
                        paclist.put(portVoList.getCode(), portVoList.getName());// eqplist = (Hashtable) AllObj.get(23);
                        String strpack = portVoList.getName() + " ~~ " + portVoList.getCode();//eqplist = (Hashtable) AllObj.get(24);
                        packageListJSONObj.add(strpack.toUpperCase());
                        packListVal.add(strpack.toUpperCase());

                    }
                }
                if (listship != null) {
                    int lenship = listship.size();
                    shipmentTermListObj = new ArrayList();
                    shttListObj = new ArrayList();
                    for (int i = 0; i < lenship; i++) {
                        portVoList = (GeneralVO) listship.get(i);
                        String strship = portVoList.getCode().toUpperCase() + " - " + portVoList.getName().toUpperCase();// shipmentTermListObj = (ArrayList) AllObj.get(20);
                        shipmentTermListObj.add(strship);
                        shttListObj.add(portVoList);// shttListObj = (ArrayList) AllObj.get(22);
                    }
                    int len2 = shttListObj.size();
                    for (int i = 0; i < len2; i++) {
                        portVoList = (GeneralVO) shttListObj.get(i);
                        shptlist.put(portVoList.getCode(), portVoList.getName());
                    }
                }
                // }
                RateRefListObj = new ArrayList();
                ArrayList listrate = new ArrayList();
                int lenrate = listrate.size();
                for (int i = 0; i < lenrate; i++) {
                    portVoList = (GeneralVO) listrate.get(i);
                    String strrate = portVoList.getCode() + " - " + portVoList.getName();// shipmentTermListObj = (ArrayList) AllObj.get(20);
                    RateRefListObj.add(strrate);//RateRefListObj = (ArrayList) AllObj.get(21);
                }
                portVoList = null;
                if (listterminal != null && listterminal.size() > 0) {
                    for (int i = 0; i < listterminal.size(); i++) {
                        portVoList = (GeneralVO) listterminal.get(i);
                        terminalMap.put(portVoList.getCode(), portVoList.getName());
                    }
                }
                /*portVoList = null;
                if (liststate != null && liststate.size() > 0) {
                    for (int i = 0; i < liststate.size(); i++) {
                        portVoList = (GeneralVO) liststate.get(i);
                        statemap.put(portVoList.getCode(), portVoList.getName());
                    }
                }*/
                statemap = locobj.getStateCache();

                context.setAttribute("SESSION_DGCLASS", dgclassht);

                context.setAttribute("PACKAGEARRAY", packListVal);
                context.setAttribute("valid", "Yes");
                context.setAttribute("PORTLISTOBJ", portListObjj);
                context.setAttribute("VESSELLISTOBJ", vesselListObjj);
                context.setAttribute("SESSION_COUNTRY", countyListObj);
                context.setAttribute("COMMODITYNAME_COMMODITYHASH", commodityListObj);
                context.setAttribute("SESSION_SERVICELIST", serlist);
                context.setAttribute("SESSION_TERMINALLIST", terlist);
                context.setAttribute("SESSION_CITYLIST", citylist);
                context.setAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST", comlist);
                context.setAttribute("SESSION_COMMODITYLIST", commodityListJSONObj);
                context.setAttribute("COUNTRYLISTOBJ", countryListJSONObj);
                context.setAttribute("SESSION_PORTLIST", portlist);
                context.setAttribute("SESSION_VESSELLIST", vessellist);
                context.setAttribute("SESSION_CONTRYLIST", contrylist);
                context.setAttribute("EQUIPMENT_TYPE", eqpList);
                context.setAttribute("NEW_COMDTY_JSON", cmdtyNewJSONObj);
                context.setAttribute("SESSION_EBOOKINGVIEW_PORTLIST", portlist);
                context.setAttribute("SESSION_COMPETITION", competitionListJSONObj);
                context.setAttribute("SESSION_COUNTRYOBJECT", countryHashObj);
                context.setAttribute("SESSION_EQUIPMENT_TYPE", eqpListColl);
                context.setAttribute("SESSION_HASH_EQUIPMENT_TYPE", eqplist);
                context.setAttribute("PACKAGE_TYPE", packageListObj);
                context.setAttribute("SHIPMENT_TERMS", shipmentTermListObj);
                context.setAttribute("RATE_REFERENCES", RateRefListObj);
                context.setAttribute("SESSION_SHIPTERMS", shptlist);
                context.setAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST", paclist);
                context.setAttribute("SESSION_PACKAGELIST", packageListJSONObj);
                context.setAttribute("AGENCYLISTOBJ", agencyLsitObj);
                context.setAttribute("SEAPORTLISTOBJ", seaPortListObj);
                context.setAttribute("EQPHASHMAP", eqpMatchValue);
                context.setAttribute("SESSION_STATEMAP", statemap);

                context.setAttribute("TEMINALSMAPCONTEXT", terminalMap);

                //ECOM V1 CACHE
                String v1DataEnableStatus = (String) context.getAttribute(GeneralConf.V1DATAENABLESTATUS);
                if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                    System.out.println("[ECOM-WAR] EcomCacheLoader >>>v1 cache details>>" + v1DataEnableStatus);
                    masterMap = v1MasObj.getEcomV1MasterCache();
                    context.setAttribute("ECOMV1MASTERCACHE", masterMap);
                } else {
                    masterMap = new HashMap();
                    context.setAttribute("ECOMV1MASTERCACHE", masterMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static double parseConfigDouble( String configKey) {
        String configValue = Validation.checkNull(configKey).trim();
        try {
            return Double.parseDouble(configValue);
        } catch (NumberFormatException e) {
            System.out.println("[ECOM-WAR] EcomCacheLoader invalid double for " + configKey);
            return 0.0;
        }
    }
}
