/*
 * EbookingValidationidatorFormBeanNew.java
 *
 * Created on  PraBaKaRaN.S August 28, 2011, 11:21 AM
 */
package net.ecom.formbeans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.GeneralConf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.Route;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author PraBaKaRaN.S
 * @version
 */
public class AmendmentBookingFormBean extends org.apache.struts.validator.ValidatorForm {

    public AmendmentBookingFormBean() {
        super();
    }
    private String name = "";
    private String booking_refno = "";
    private int number;
    private String ebook_date = "";
    private String ebook_dateBut = "";
    private String ebook_portofloading = "";
    private String ebook_portofdischarge = "";
    private String ebook_servicecode = "";
    private String ebook_portofloading_desc = "";
    private String ebook_portofdischarge_desc = "";
    private String ebooking_service = "";
    private String ebook_lineservicecode = "";
    private String ebook_lineservice = "";
    private String ebook_vesselcode = "";
    private String ebooking_vessel = "";
    private String ebook_linevesselcode = "";
    private String ebook_linevessel = "";
    private String ebook_voyage = "";
    private String ebook_bound_leg = "";
    private String ebook_linebound_leg = "";
    private String ebook_linevoyage = "";
    private String ebook_origin = "";
    private String ebook_origin_country = "";
    private String ebook_pol_country = "";
    private String ebook_pol = "";
    private String ebook_terminal = "";
    private String ebook_terminal_country = "";
    private String ebook_pol_etd = "";
    private String ebook_pod = "";
    private String ebook_pod_country = "";
    private String ebook_terminalpod = "";
    private String ebook_terminalpod_country = "";
    private String ebook_pol_eta = "";
    private String ebook_delivery = "";
    private String ebook_delivery_country = "";
    private String ebook_status_remarks = "";
    private String ebook_userstatus = "";
    private String ebook_bookagency = "";
    private String ebook_bookagency_desc = "";
    private String ebook_bookagency_hdn = "";
    private String ebook_date_hdn = "";
    private String ebook_pol_hdn = "";
    private String ebook_pod_hdn = "";
    private String ebook_origin_hdn = "";
    private String ebook_delivery_hdn = "";
    private String ebook_shippername = "";
    private String ebook_shippercode = "";
    private String ebook_shipperzipcode = "";
    private String ebook_shipperaddress = "";
    private String ebook_shippercountry = "";
    private String ebook_shippercountry_name = "";
    private String ebook_shippercountrycode = "";
    private String ebook_shippercity = "";
    private String ebook_shipperphone = "";
    private String ebook_shipperfax = "";
    private String ebook_shipperemail = "";
    private String ebook_shippercountry_desc = "";
    private String ebook_shippercity_desc = "";
    private String ebook_shippercity_hdn = "";
    private String ebook_consigneename = "";
    private String ebook_consigneecode = "";
    private String ebook_consigneezipcode = "";
    private String ebook_consigneeaddress = "";
    private String ebook_consigneecountry = "";
    private String ebook_consigneecountry_name = "";
    private String ebook_consigneecountrycode = "";
    private String ebook_consigneecity = "";
    private String ebook_consigneephone = "";
    private String ebook_consigneefax = "";
    private String ebook_consigneeemail = "";
    private String ebook_consigneecountry_desc = "";
    private String ebook_consigneecity_desc = "";
    private String ebook_consigneecity_hdn = "";
    private String ebook_notifychk = "";
    private String ebook_notifyname = "";
    private String ebook_notifycode = "";
    private String ebook_notifyzipcode = "";
    private String ebook_notifyaddress = "";
    private String ebook_notifycountry = "";
    private String ebook_notifycountry_name = "";
    private String ebook_notifycountrycode = "";
    private String ebook_notifycity = "";
    private String ebook_notifyphone = "";
    private String ebook_notifyfax = "";
    private String ebook_notifyemail = "";
    private String ebook_notifycountry_desc = "";
    private String ebook_notifycity_desc = "";
    private String ebook_notifycity_hdn = "";
    private String ebook_notifychk_n1 = "";
    private String ebook_notifyname_n1 = "";
    private String ebook_notifycode_n1 = "";
    private String ebook_notifyzipcode_n1 = "";
    private String ebook_notifyaddress_n1 = "";
    private String ebook_notifycountry_n1 = "";
    private String ebook_notifycountry_name_n1 = "";
    private String ebook_notifycountrycode_n1 = "";
    private String ebook_notifycity_n1 = "";
    private String ebook_notifyphone_n1 = "";
    private String ebook_notifyfax_n1 = "";
    private String ebook_notifyemail_n1 = "";
    private String ebook_notifycountry_desc_n1 = "";
    private String ebook_notifycity_desc_n1 = "";
    private String ebook_notifycity_hdn_n1 = "";
    private String ebook_forwardername = "";
    private String ebook_forwardercode = "";
    private String ebook_forwarderzipcode = "";
    private String ebook_forwarderaddress = "";
    private String ebook_forwardercountry = "";
    private String ebook_forwardercountrycode = "";
    private String ebook_forwardercity = "";
    private String ebook_forwarderphone = "";
    private String ebook_forwarderfax = "";
    private String ebook_forwarderemail = "";
    private String ebook_forwardercountry_desc = "";
    private String ebook_forwardercity_desc = "";
    private String ebook_forwarder_id = "";
    private String ebook_forwardercity_hdn = "";
    private String ebook_forwardercountry_name = "";
    private String ebook_forwarderaddress1 = "";
    private String ebook_Warehousecity_hdn = "";
    private String ebook_Warehousecountry_name = "";
    private String ebook_Warehouseaddress1 = "";

    private String ebook_Warehousename = "";
    private String ebook_Warehousecode = "";
    private String ebook_Warehousezipcode = "";
    private String ebook_Warehouseaddress = "";
    private String ebook_Warehousecountry = "";
    private String ebook_Warehousecountrycode = "";
    private String ebook_Warehousecity = "";
    private String ebook_Warehousephone = "";
    private String ebook_Warehousefax = "";
    private String ebook_Warehouseemail = "";
    private String ebook_Warehousecountry_desc = "";
    private String ebook_Warehousecity_desc = "";
    private String ebook_Warehouse_id = "";

    private String ebook_tareweight = "";
    private String ebook_weightunit = "";
    private String ebooking_nor = "";
    private String ebooking_reefer = "";
    private String ebook_minimumtemp = "";
    private String ebook_tempunit = "";
    private String ebook_settemp = "";
    private String ebook_maxitemp = "";
    private String ebook_genset = "";
    private String ebook_packcode = "";
    private String ebooking_package = "";
    private String ebook_packquantity = "";
    private String ebook_hazardous = "";
    private String ebook_hazardouscontact = "";
    private String ebook_telephonecontact = "";
    private String ebook_cargoweight = "";
    private String ebook_grossweight = "";
    private String ebook_reefercargo = "";
    private String ebook_drycargo = "";
    private String ebook_commoditycode = "";
    private String ebooking_commodity = "";
    private String ebooking_commodity_desc = "";
    private String ebook_hazardous_desc = "";
    private String ebook_drycargo_desc = "";
    private String ebook_reefercargo_desc = "";
    private String[] ebook_equipmentsize;
    private String[] ebook_quantity;
    private String[] ebook_equipment;
    private String ebook_equipment_name = "";
    private String ebook_equiptype = "";
    private String ebook_equipsize = "";
    private String ebook_equipqty = "";
    private String ebook_templateno = "";
    private String ebook_templatename = "";
    private String cargoclickadd = "";
    private String ebook_conthdn = "";
    private String ebook_commhdn = "";
    private String ebook_masthdn = "";
    private String cont_flag = "";
    private String submit_btn = "";
    private String btntype = "";
    private String btntype1 = "";
    private String btntype2 = "";
    private String emptyPickup = "";
    private String inTransit = "";
    private String typeofMovementcode = "";
    private String typeofMovement = "";
    private String carriageTermCode = "";
    private String carriageTerms = "";
    private String preCarriage = "";
    private String onCarriage = "";
    private String soc = "";
    private String ebook_origin_desc = "";
    private String ebook_delivery_desc = "";
    private String ebook_eqp = "";
    private String ebook_eqp_qty = "";
    private String ebook_eqp_type = "";
    private String ebook_eqp_type_desc = "";
    private String ebook_bound = "";
    private String ebook_routeid = "";
    private String ebook_comid = "";
    private String ebook_conid = "";
    private String ebook_shipper_id = "";
    private String ebook_consigne_id = "";
    private String ebook_notify_id = "";
    private String ebook_notify_id_n1 = "";
    private String emptyReturn = "";
    private String eqpstatus = "";
    private String ebkg_type = "";
    private String ebook_actCommoName = "";
    private String ensfiling = null;
    //
    private FormFile file_path1 = null;
    private FormFile file_path2 = null;
    private FormFile file_path3 = null;
    private FormFile file_path4 = null;
    private FormFile file_path5 = null;

    private FormFile file_path6 = null;
    private FormFile file_path7 = null;
    private FormFile file_path8 = null;
    private FormFile file_path9 = null;
    private FormFile file_path10 = null;
    private FormFile file_path11 = null;
    private FormFile file_path12 = null;
    private FormFile file_path13 = null;
    private FormFile file_path14 = null;
    private FormFile file_path15 = null;

    private String filename1 = null;
    private String filename2 = null;
    private String filename3 = null;
    private String filename4 = null;
    private String filename5 = null;
    private String shipperCheck = null;
    private String shipperAPCheck = null;
    private String notifyCheck = null;
    private String notify1Check = null;
    private String filesNames = null;
    private String slotType = null;
    private String ecomBookNo = null;
    private String lrpBookNo = null;
    private String custCode = null;
    private String custName = null;
    private String loginName = null;
    private String containerNo = null;
    private String carrierContact = null;
    private String ebook_ocean_freight = null;
    private String ebook_pickup_point = null;
    private String emptyPickUpDate = null;
    private String emptyPickupHid = null;
    private String saveStatus = null;
    private String amendment = null;
    private String emptyPickUpName = null;
    private String hidSHToOrder = null;
    private String hidCNToOrder = null;
    private String hidNTToOrder = null;
    private String hidNT1ToOrder = null;
    private String podintransit = null;
    private String hidpodintransit = null;
    private String hidETDDate = null;
    private String hidPodIntransitName = null;
    private String documentalagencyid = null;
    private String agencyissueid = null;
    private String agencyid = null;
    private String ebook_natcode = null;
    private String ebook_bookingcountry = "";
    private String ebook_bookingcode = "";
    private String ebook_bookingname = "";
    private String ebook_bookingcountry_name = "";
    private String ebook_bookingcountrycode = "";
    private String ebook_bookingcity = "";
    private String ebook_bookingphone = "";
    private String ebook_bookingfax = "";
    private String ebook_bookingemail = "";
    private String ebook_bookingzipcode = "";
    private String ebook_bookingaddress = "";
    private String ebook_bookingaddress1 = "";
    private String ebook_bookingcity_desc = "";
    private String ebook_bookingeori = "";
    private String bookingtypeperson = "";

    private String ebook_isdummy = "";
    private String appVersion;
    private String hsCommodityVal = null;
    private String hidbkgtype = null;
    private String salesRateAgree;
    private String ccMailId = null;
    private String isCCMailId = null;
    private String ebook_haulercode = null;
    private String contactName = null;
    private String custPhoneNo = null;
    private String isFakCommodity;
    private String ebook_haulagetype;
    private String ebook_pickup_date;
    private String ebook_drop_date;
    private String ebook_haulageDesc;

    private String humidityflag;
    private String humidity;
    private String drains;
    private String co2;
    private String o2;
    private String ventilation;

    private String bkgcutoff;
    private String doccutoff;
    private String vgmcutoff;
    private String bbrorocutoff;
    private String hazcutoff;
    private String portentrycutoff;

    private String shipment_type;
    private String ebook_custpo;
    private String ebook_reftype;
    private String ebook_exportref;

    private String hidden_empty = "";
    private String hidden_soc = "";
    private String groupcallid = "";
    private String ebook_rate = "";
    private String ebook_rate_desc = "";
    private String ebook_rate_reference = "";
    private String ebook_agency_reference = "";
    private String ebook_party = "";
    private String ebook_party_desc = "";
    private String ebook_bookingparty_desc = "";
    private String ebook_shipment = "";
    private String ebook_shipment_desc = "";
    private String ebook_shipterm_desc = "";
    private String hzd = "";
    private String rfr = "";
    private String oog = "";
    private String lcl = "";
    private String ebook_packagecode = "";
    private String ebook_package = "";
    private String ebook_cargovolume = "";
    private String ebook_volumeunit = "";
    private String ebook_netweight = "";
    private String ebook_netweightunit = "";
    private String ebook_unno = "";
    private String ebook_shipname = "";
    private String ebook_dgclass = "";
    private String ebook_flashpoint = "";
    private String ebook_flashunit = "";
    private String ebook_poluttunit = "";
    private String ebook_nor = "";
    
    // Multiple DG Details Support
    // Use EJB VO class type for proper serialization when passing to EJB
    private List<net.ecom.vo.AmendmentBookingFormBean> dgDetailsList;
    private int dgCount = 0;
    private String ebook_presetemp = "";
    private String ebook_mintemp = "";
    private String ebook_maxtemp = "";
    private String ebook_temperunit = "";
    private String ebook_olforward = "";
    private String ebook_owleft = "";
    private String ebook_oh = "";
    private String ebook_dimensionunit = "";
    private String ebook_olaft = "";
    private String ebook_owright = "";
    private String hidden_rfr = "";
    private String hidden_hzd = "";
    private String hidden_lcl = "";
    private String hidden_oog = "";
    private String remarks = "";
    private String ebook_cmdtybuyercode = "";
    private String ebook_cmdtysellercode = "";
    private String ebook_cmdtyhblno = "";
    private String ebook_cmdtybuyerName = "";
    private String ebook_cmdtysellerName = "";

    private String hdntempaltename;
    private String ebook_ratetype = "";
    private String ebook_servtype = "";
    private String ebook_orderNo = "";
    private String cargo_hidden_id = "";
    private String cargo_hidden_type = "";

    private String hscode = "";
    private String commodityothers = "";
    private String ebook_notifyphone1_n1 = "";
    private String ebook_notifyphone2_n1 = "";
    private String ebook_notifyphone3_n1 = "";
    private String ebook_notifyfax1_n1 = "";
    private String ebook_notifyfax2_n2 = "";
    private String ebook_notifyfax3_n3 = "";
    private String ebook_notifyaddress1_n1 = "";
    //
    private String ebook_notifyphone1 = "";
    private String ebook_notifyphone2 = "";
    private String ebook_notifyphone3 = "";
    private String ebook_notifyfax1 = "";
    private String ebook_notifyfax2 = "";
    private String ebook_notifyfax3 = "";
    private String ebook_consigneefax1 = "";
    private String ebook_consigneefax2 = "";
    private String ebook_consigneefax3 = "";
    private String ebook_consigneephone1 = "";
    private String ebook_consigneephone2 = "";
    private String ebook_consigneephone3 = "";
    private String ebook_shipperfax1 = "";
    private String ebook_shipperfax2 = "";
    private String ebook_shipperfax3 = "";
    private String ebook_shipperphone1 = "";
    private String ebook_shipperphone2 = "";
    private String ebook_shipperphone3 = "";
    private String trpservice = "";
    private String servicehd = "";
    private String servicehdedit = "";
    private String ebook_agreename = "";
    private String ebook_agreecode = "";
    private String ebook_agreezipcode = "";
    private String ebook_agreeaddress = "";
    private String ebook_agreecountry = "";
    private String ebook_agreecountry_name = "";
    private String ebook_agreecountrycode = "";
    private String ebook_agreecity = "";
    private String ebook_agreephone = "";
    private String ebook_agreefax = "";
    private String ebook_agreeemail = "";
    private String ebook_agreecountry_desc = "";
    private String ebook_agreecity_desc = "";
    private String ebook_agreecity_hdn = "";
    private String ebook_agree_id = "";
    private String cargoid_new = "";
    private String duplicateEqp = "";
    private String ebook_shipperaddress1 = "";
    private String ebook_consigneeaddress1 = "";
    private String ebook_notifyaddress1 = "";
    private String ebook_agreeaddress1 = "";
    private String routepolname = "";
    private String routepodname = "";
    private String routeoriginname = "";
    private String routedeliveryname = "";
    private String routepolter = "";
    private String routepodter = "";

    private String ebook_bookingstate = "";
    private String ebook_agreestate = "";
    private String ebook_shipperstate = "";
    private String ebook_consigneestate = "";
    private String ebook_notifystate = "";
    private String ebook_notifystate_n1 = "";
    private String ebook_forwarderstate = "";
    private String ebook_Warehousestate = "";

    private String ebook_NamedCustomerstate = "";
    private String ebook_NamedCustomer_id = "";
    private String ebook_NamedCustomername = "";
    private String ebook_NamedCustomercode = "";
    private String ebook_NamedCustomerzipcode = "";
    private String ebook_NamedCustomeraddress = "";
    private String ebook_NamedCustomercountry = "";
    private String ebook_NamedCustomercountrycode = "";
    private String ebook_NamedCustomercity = "";
    private String ebook_NamedCustomerphone = "";
    private String ebook_NamedCustomerphone1 = "";
    private String ebook_NamedCustomerphone2 = "";
    private String ebook_NamedCustomerphone3 = "";
    private String ebook_NamedCustomerfax1 = "";
    private String ebook_NamedCustomerfax2 = "";
    private String ebook_NamedCustomerfax3 = "";
    private String ebook_NamedCustomerfax = "";
    private String ebook_NamedCustomeremail = "";
    private String ebook_NamedCustomercountry_desc = "";
    private String ebook_NamedCustomercity_desc = "";
    private String ebook_NamedCustomeraddress1 = "";
    private String ebook_NamedCustomercountry_name = "";
    private String ebook_NamedCustomercity_hdn = "";
    private String ebook_NamedCustomereori = "";
    private String ebook_agreeeori = "";
    private String ebook_consigneeeori = "";
    private String ebook_shippereori = "";
    private String ebook_notifyeori = "";
    private String ebook_Warehouseeori = "";
    private String ebook_forwardereori = "";
    private String ebook_notifyeori_n1 = "";
    private String agreetypeperson = "";
    private String shippertypeperson = "";
    private String consigneetypeperson = "";
    private String notifytypeperson = "";
    private String notifytypeperson_n1 = "";
    private String Warehousetypeperson = "";
    private String forwardertypeperson = "";
    private String ebook_NamedCustomerpersontype = "";
    private String deletedfilename = "";

    // Seller
    private String ebook_Seller_id = "";
    private String ebook_Sellercity = "";
    private String ebook_Sellercity_hdn = "";
    private String ebook_Sellercountry = "";
    private String ebook_Sellername = "";
    private String ebook_Selleraddress = "";
    private String ebook_Selleraddress1 = "";
    private String ebook_Sellercity_desc = "";
    private String ebook_Sellercountry_name = "";
    private String SellerSelect = "";
    private String ebook_Sellercode = "";
    private String ebook_Sellerphone = "";
    private String ebook_Sellerfax = "";
    private String ebook_Selleremail = "";
    private String ebook_Sellerstate = "";
    private String ebook_Sellereori = "";
    private String ebook_Sellerpersontype = "";
    private String ebook_Sellercountry_desc = "";
    private String ebook_Sellerphone1 = "";
    private String ebook_Sellerphone2 = "";
    private String ebook_Sellerphone3 = "";
    private String ebook_Sellerfax1 = "";
    private String ebook_Sellerfax2 = "";
    private String ebook_Sellerfax3 = "";

// Buyer
    private String ebook_Buyer_id = "";
    private String BuyerSelect = "";
    private String ebook_Buyername = "";
    private String ebook_Buyeraddress = "";
    private String ebook_Buyeraddress1 = "";
    private String ebook_Buyercity_desc = "";
    private String ebook_Buyercountry_name = "";
    private String ebook_Buyerphone = "";
    private String ebook_Buyerfax = "";
    private String ebook_Buyeremail = "";
    private String ebook_Buyerstate = "";
    private String ebook_Buyereori = "";
    private String ebook_Buyercode = "";
    private String ebook_Buyerpersontype = "";
    private String ebook_Buyercity = "";
    private String ebook_Buyercity_hdn = "";
    private String ebook_Buyercountry = "";
    private String ebook_Buyercountry_desc = "";
    private String ebook_Buyerphone1 = "";
    private String ebook_Buyerfax1 = "";
    private String ebook_Buyerphone2 = "";
    private String ebook_Buyerfax2 = "";
    private String ebook_Buyerphone3 = "";
    private String ebook_Buyerfax3 = "";

    private String isinsuranceavailable = null;
    private String quoteno = null;
    private String insuredemail = null;

    private String customerreference = null;
    private String ACID = null;
    private String cargoprotectid;
    private String shipperReference = null;
    private String shipperReferenceType = null;
    
    private String ebook_preferred_agency = null;
    private String ebook_t2lfiling = null;
    private String ebook_EnsDocType = null;
    private String ebook_sisubmit_party = null;

    //Cust
    private String ebook_Custcode = null;
    private String ebook_Custcountry = null;
    private String ebook_Custname = null;
    private String ebook_Custaddress = null;
    private String ebook_Custcity_desc = null;
    private String ebook_Custphone = null;
    private String ebook_Custfax = null;
    private String ebook_Custemail = null;
    private String ebook_Custaddress1 = null;
    private String ebook_Custstate = null;
    private String ebook_Custeori = null;
    private String ebook_Custpersontype = null;
    private String ebook_Custid;
    private String ebook_CustSelect;
    private String ebook_Custcity;
    private String ebook_Custcountry_desc = null;
    //Zip Code
    private String ebook_Sellerzipcode = null;
    private String ebook_Buyerzipcode = null;
    private ENSDeclarantVo ensdecvo = null;

    //ENS Filling
    private String euCustZone = "Y";
    private String blTypeENS = null;
    private String ensFillingType = null;
    private String paymentMethod = null;

    private String declarantName = null;
    private String declarantAddress = null;
    private String declarantAddress1 = null;
    private String declarantCity = null;
    private String declarantCountry = null;
    private String declarantState = null;
    private String declarantTelephone = null;
    private String declarantEORI = null;
    private String declarantEmail = null;
    private String declarantFillingType = null;
    private String declarantZipcode = null;
    public String getEbook_bookingeori() {
        return ebook_bookingeori;
    }

    public void setEbook_bookingeori(String ebook_bookingeori) {
        this.ebook_bookingeori = ebook_bookingeori;
    }

    public String getBookingtypeperson() {
        return bookingtypeperson;
    }

    public void setBookingtypeperson(String bookingtypeperson) {
        this.bookingtypeperson = bookingtypeperson;
    }
    
    public String getDeclarantZipcode() {
        return declarantZipcode;
    }

    public void setDeclarantZipcode(String declarantZipcode) {
        this.declarantZipcode = declarantZipcode;
    }

    public String getEbook_Buyerzipcode() {
        return ebook_Buyerzipcode;
    }

    public void setEbook_Buyerzipcode(String ebook_Buyerzipcode) {
        this.ebook_Buyerzipcode = ebook_Buyerzipcode;
    }

    public String getEbook_Sellerzipcode() {
        return ebook_Sellerzipcode;
    }

    public void setEbook_Sellerzipcode(String ebook_Sellerzipcode) {
        this.ebook_Sellerzipcode = ebook_Sellerzipcode;
    }

    public String getEbook_cmdtybuyercode() {
        return ebook_cmdtybuyercode;
    }

    public void setEbook_cmdtybuyercode(String ebook_buyersName) {
        this.ebook_cmdtybuyercode = ebook_buyersName;
    }

    public String getEbook_cmdtysellercode() {
        return ebook_cmdtysellercode;
    }

    public void setEbook_cmdtysellercode(String ebook_sellersName) {
        this.ebook_cmdtysellercode = ebook_sellersName;
    }

    public String getEbook_cmdtyhblno() {
        return ebook_cmdtyhblno;
    }

    public void setEbook_cmdtyhblno(String ebook_cmdtyhblno) {
        this.ebook_cmdtyhblno = ebook_cmdtyhblno;
    }

    public String getEbook_Custcode() {
        return ebook_Custcode;
    }

    public void setEbook_Custcode(String ebook_Custcode) {
        this.ebook_Custcode = ebook_Custcode;
    }

    public String getEbook_Custcountry() {
        return ebook_Custcountry;
    }

    public void setEbook_Custcountry(String ebook_Custcountry) {
        this.ebook_Custcountry = ebook_Custcountry;
    }

    public String getEbook_Custname() {
        return ebook_Custname;
    }

    public void setEbook_Custname(String ebook_Custname) {
        this.ebook_Custname = ebook_Custname;
    }

    public String getEbook_Custaddress() {
        return ebook_Custaddress;
    }

    public void setEbook_Custaddress(String ebook_Custaddress) {
        this.ebook_Custaddress = ebook_Custaddress;
    }

    public String getEbook_Custcity_desc() {
        return ebook_Custcity_desc;
    }

    public void setEbook_Custcity_desc(String ebook_Custcity_desc) {
        this.ebook_Custcity_desc = ebook_Custcity_desc;
    }

    public String getEbook_Custphone() {
        return ebook_Custphone;
    }

    public void setEbook_Custphone(String ebook_Custphone) {
        this.ebook_Custphone = ebook_Custphone;
    }

    public String getEbook_Custfax() {
        return ebook_Custfax;
    }

    public void setEbook_Custfax(String ebook_Custfax) {
        this.ebook_Custfax = ebook_Custfax;
    }

    public String getEbook_Custemail() {
        return ebook_Custemail;
    }

    public void setEbook_Custemail(String ebook_Custemail) {
        this.ebook_Custemail = ebook_Custemail;
    }

    public String getEbook_Custaddress1() {
        return ebook_Custaddress1;
    }

    public void setEbook_Custaddress1(String ebook_Custaddress1) {
        this.ebook_Custaddress1 = ebook_Custaddress1;
    }

    public String getEbook_Custstate() {
        return ebook_Custstate;
    }

    public void setEbook_Custstate(String ebook_Custstate) {
        this.ebook_Custstate = ebook_Custstate;
    }

    public String getEbook_Custeori() {
        return ebook_Custeori;
    }

    public void setEbook_Custeori(String ebook_Custeori) {
        this.ebook_Custeori = ebook_Custeori;
    }

    public String getEbook_Custpersontype() {
        return ebook_Custpersontype;
    }

    public void setEbook_Custpersontype(String ebook_Custpersontype) {
        this.ebook_Custpersontype = ebook_Custpersontype;
    }

    public String getEbook_Custid() {
        return ebook_Custid;
    }

    public void setEbook_Custid(String ebook_Custid) {
        this.ebook_Custid = ebook_Custid;
    }

    public String getEbook_CustSelect() {
        return ebook_CustSelect;
    }

    public void setEbook_CustSelect(String ebook_CustSelect) {
        this.ebook_CustSelect = ebook_CustSelect;
    }

    public String getEbook_Custcity() {
        return ebook_Custcity;
    }

    public void setEbook_Custcity(String ebook_Custcity) {
        this.ebook_Custcity = ebook_Custcity;
    }

    public String getEbook_Custcountry_desc() {
        return ebook_Custcountry_desc;
    }

    public void setEbook_Custcountry_desc(String ebook_Custcountry_desc) {
        this.ebook_Custcountry_desc = ebook_Custcountry_desc;
    }

    public String getEbook_EnsDocType() {
        return ebook_EnsDocType;
    }

    public void setEbook_EnsDocType(String ebook_EnsDocType) {
        this.ebook_EnsDocType = ebook_EnsDocType;
    }

    public String getEbook_custpo() {
        return ebook_custpo;
    }

    public String getEbook_agreeeori() {
        return ebook_agreeeori;
    }

    public void setEbook_agreeeori(String ebook_agreeeori) {
        this.ebook_agreeeori = ebook_agreeeori;
    }

    public String getEbook_consigneeeori() {
        return ebook_consigneeeori;
    }

    public void setEbook_consigneeeori(String ebook_consigneeeori) {
        this.ebook_consigneeeori = ebook_consigneeeori;
    }

    public String getEbook_shippereori() {
        return ebook_shippereori;
    }

    public void setEbook_shippereori(String ebook_shippereori) {
        this.ebook_shippereori = ebook_shippereori;
    }

    public String getEbook_notifyeori() {
        return ebook_notifyeori;
    }

    public void setEbook_notifyeori(String ebook_notifyeori) {
        this.ebook_notifyeori = ebook_notifyeori;
    }

    public String getEbook_Warehouseeori() {
        return ebook_Warehouseeori;
    }

    public void setEbook_Warehouseeori(String ebook_Warehouseeori) {
        this.ebook_Warehouseeori = ebook_Warehouseeori;
    }

    public String getEbook_forwardereori() {
        return ebook_forwardereori;
    }

    public void setEbook_forwardereori(String ebook_forwardereori) {
        this.ebook_forwardereori = ebook_forwardereori;
    }

    public String getEbook_notifyeori_n1() {
        return ebook_notifyeori_n1;
    }

    public void setEbook_notifyeori_n1(String ebook_notifyeori_n1) {
        this.ebook_notifyeori_n1 = ebook_notifyeori_n1;
    }

    public void setEbook_custpo(String ebook_custpo) {
        this.ebook_custpo = ebook_custpo;
    }

    public String getEbook_reftype() {
        return ebook_reftype;
    }

    public void setEbook_reftype(String ebook_reftype) {
        this.ebook_reftype = ebook_reftype;
    }

    public String getEbook_exportref() {
        return ebook_exportref;
    }

    public void setEbook_exportref(String ebook_exportref) {
        this.ebook_exportref = ebook_exportref;
    }

    public String getShipment_type() {
        return shipment_type;
    }

    public void setShipment_type(String shipment_type) {
        this.shipment_type = shipment_type;
    }

    public String getBkgcutoff() {
        return bkgcutoff;
    }

    public void setBkgcutoff(String bkgcutoff) {
        this.bkgcutoff = bkgcutoff;
    }

    public String getDoccutoff() {
        return doccutoff;
    }

    public void setDoccutoff(String doccutoff) {
        this.doccutoff = doccutoff;
    }

    public String getVgmcutoff() {
        return vgmcutoff;
    }

    public void setVgmcutoff(String vgmcutoff) {
        this.vgmcutoff = vgmcutoff;
    }

    public String getBbrorocutoff() {
        return bbrorocutoff;
    }

    public void setBbrorocutoff(String bbrorocutoff) {
        this.bbrorocutoff = bbrorocutoff;
    }

    public String getHazcutoff() {
        return hazcutoff;
    }

    public void setHazcutoff(String hazcutoff) {
        this.hazcutoff = hazcutoff;
    }

    public String getPortentrycutoff() {
        return portentrycutoff;
    }

    public void setPortentrycutoff(String portentrycutoff) {
        this.portentrycutoff = portentrycutoff;
    }

    public String getHumidityflag() {
        return humidityflag;
    }

    public void setHumidityflag(String humidityflag) {
        this.humidityflag = humidityflag;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getEbook_netweight() {
        return ebook_netweight;
    }

    public void setEbook_netweight(String ebook_netweight) {
        this.ebook_netweight = ebook_netweight;
    }

    public String getEbook_netweightunit() {
        return ebook_netweightunit;
    }

    public void setEbook_netweightunit(String ebook_netweightunit) {
        this.ebook_netweightunit = ebook_netweightunit;
    }

    public String getVentilation() {
        return ventilation;
    }

    public void setVentilation(String ventilation) {
        this.ventilation = ventilation;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getDrains() {
        return drains;
    }

    public void setDrains(String drains) {
        this.drains = drains;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public String getO2() {
        return o2;
    }

    public void setO2(String o2) {
        this.o2 = o2;
    }

    public String getEbook_haulageDesc() {
        return ebook_haulageDesc;
    }

    public void setEbook_haulageDesc(String ebook_haulageDesc) {
        this.ebook_haulageDesc = ebook_haulageDesc;
    }

    public String getEbook_haulagetype() {
        return ebook_haulagetype;
    }

    public void setEbook_haulagetype(String ebook_haulagetype) {
        this.ebook_haulagetype = ebook_haulagetype;
    }

    public String getEbook_pickup_date() {
        return ebook_pickup_date;
    }

    public void setEbook_pickup_date(String ebook_pickup_date) {
        this.ebook_pickup_date = ebook_pickup_date;
    }

    public String getEbook_drop_date() {
        return ebook_drop_date;
    }

    public void setEbook_drop_date(String ebook_drop_date) {
        this.ebook_drop_date = ebook_drop_date;
    }

    public String getIsFakCommodity() {
        return isFakCommodity;
    }

    public void setIsFakCommodity(String isFakCommodity) {
        this.isFakCommodity = isFakCommodity;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCustPhoneNo() {
        return custPhoneNo;
    }

    public void setCustPhoneNo(String custPhoneNo) {
        this.custPhoneNo = custPhoneNo;
    }

    public String getEbook_haulercode() {
        return ebook_haulercode;
    }

    public void setEbook_haulercode(String ebook_haulercode) {
        this.ebook_haulercode = ebook_haulercode;
    }

    public String getIsCCMailId() {
        return isCCMailId;
    }

    public void setIsCCMailId(String isCCMailId) {
        this.isCCMailId = isCCMailId;
    }

    public String getCcMailId() {
        return ccMailId;
    }

    public void setCcMailId(String ccMailId) {
        this.ccMailId = ccMailId;
    }

    public String getSalesRateAgree() {
        return salesRateAgree;
    }

    public void setSalesRateAgree(String salesRateAgree) {
        this.salesRateAgree = salesRateAgree;
    }

    public String getHidbkgtype() {
        return hidbkgtype;
    }

    public void setHidbkgtype(String hidbkgtype) {
        this.hidbkgtype = hidbkgtype;
    }

    public String getHsCommodityVal() {
        return hsCommodityVal;
    }

    public void setHsCommodityVal(String hsCommodityVal) {
        this.hsCommodityVal = hsCommodityVal;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getEbook_bookingcity_desc() {
        return ebook_bookingcity_desc;
    }

    public void setEbook_bookingcity_desc(String ebook_bookingcity_desc) {
        this.ebook_bookingcity_desc = ebook_bookingcity_desc;
    }

    public String getEbook_bookingcountry() {
        return ebook_bookingcountry;
    }

    public void setEbook_bookingcountry(String ebook_bookingcountry) {
        this.ebook_bookingcountry = ebook_bookingcountry;
    }

    public String getEbook_bookingcode() {
        return ebook_bookingcode;
    }

    public void setEbook_bookingcode(String ebook_bookingcode) {
        this.ebook_bookingcode = ebook_bookingcode;
    }

    public String getEbook_bookingname() {
        return ebook_bookingname;
    }

    public void setEbook_bookingname(String ebook_bookingname) {
        this.ebook_bookingname = ebook_bookingname;
    }

    public String getEbook_bookingcountry_name() {
        return ebook_bookingcountry_name;
    }

    public void setEbook_bookingcountry_name(String ebook_bookingcountry_name) {
        this.ebook_bookingcountry_name = ebook_bookingcountry_name;
    }

    public String getEbook_bookingcountrycode() {
        return ebook_bookingcountrycode;
    }

    public void setEbook_bookingcountrycode(String ebook_bookingcountrycode) {
        this.ebook_bookingcountrycode = ebook_bookingcountrycode;
    }

    public String getEbook_bookingcity() {
        return ebook_bookingcity;
    }

    public void setEbook_bookingcity(String ebook_bookingcity) {
        this.ebook_bookingcity = ebook_bookingcity;
    }

    public String getEbook_bookingphone() {
        return ebook_bookingphone;
    }

    public void setEbook_bookingphone(String ebook_bookingphone) {
        this.ebook_bookingphone = ebook_bookingphone;
    }

    public String getEbook_bookingfax() {
        return ebook_bookingfax;
    }

    public void setEbook_bookingfax(String ebook_bookingfax) {
        this.ebook_bookingfax = ebook_bookingfax;
    }

    public String getEbook_bookingemail() {
        return ebook_bookingemail;
    }

    public void setEbook_bookingemail(String ebook_bookingemail) {
        this.ebook_bookingemail = ebook_bookingemail;
    }

    public String getEbook_bookingzipcode() {
        return ebook_bookingzipcode;
    }

    public void setEbook_bookingzipcode(String ebook_bookingzipcode) {
        this.ebook_bookingzipcode = ebook_bookingzipcode;
    }

    public String getEbook_bookingaddress() {
        return ebook_bookingaddress;
    }

    public void setEbook_bookingaddress(String ebook_bookingaddress) {
        this.ebook_bookingaddress = ebook_bookingaddress;
    }

    public String getEbook_bookingaddress1() {
        return ebook_bookingaddress1;
    }

    public void setEbook_bookingaddress1(String ebook_bookingaddress1) {
        this.ebook_bookingaddress1 = ebook_bookingaddress1;
    }

    public String getHidETDDate() {
        return hidETDDate;
    }

    public void setHidETDDate(String hidETDDate) {
        this.hidETDDate = hidETDDate;
    }

    public String getHidpodintransit() {
        return hidpodintransit;
    }

    public void setHidpodintransit(String hidpodintransit) {
        this.hidpodintransit = hidpodintransit;
    }

    public String getPodintransit() {
        return podintransit;
    }

    public void setPodintransit(String podintransit) {
        this.podintransit = podintransit;
    }

    public String getHidCNToOrder() {
        return hidCNToOrder;
    }

    public void setHidCNToOrder(String hidCNToOrder) {
        this.hidCNToOrder = hidCNToOrder;
    }

    public String getHidNT1ToOrder() {
        return hidNT1ToOrder;
    }

    public void setHidNT1ToOrder(String hidNT1ToOrder) {
        this.hidNT1ToOrder = hidNT1ToOrder;
    }

    public String getHidNTToOrder() {
        return hidNTToOrder;
    }

    public void setHidNTToOrder(String hidNTToOrder) {
        this.hidNTToOrder = hidNTToOrder;
    }

    public String getHidSHToOrder() {
        return hidSHToOrder;
    }

    public void setHidSHToOrder(String hidSHToOrder) {
        this.hidSHToOrder = hidSHToOrder;
    }

    public String getEmptyPickUpName() {
        return emptyPickUpName;
    }

    public void setEmptyPickUpName(String emptyPickUpName) {
        this.emptyPickUpName = emptyPickUpName;
    }

    public String getAmendment() {
        return amendment;
    }

    public void setAmendment(String amendment) {
        this.amendment = amendment;
    }

    public String getSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(String saveStatus) {
        this.saveStatus = saveStatus;
    }

    public String getEmptyPickUpDate() {
        return emptyPickUpDate;
    }

    public void setEmptyPickUpDate(String emptyPickUpDate) {
        this.emptyPickUpDate = emptyPickUpDate;
    }

    public String getEbook_ocean_freight() {
        return ebook_ocean_freight;
    }

    public void setEbook_ocean_freight(String ebook_ocean_freight) {
        this.ebook_ocean_freight = ebook_ocean_freight;
    }

    public String getCarrierContact() {
        return carrierContact;
    }

    public void setCarrierContact(String carrierContact) {
        this.carrierContact = carrierContact;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEcomBookNo() {
        return ecomBookNo;
    }

    public void setEcomBookNo(String ecomBookNo) {
        this.ecomBookNo = ecomBookNo;
    }

    public String getFilesNames() {
        return filesNames;
    }

    public void setFilesNames(String filesNames) {
        this.filesNames = filesNames;
    }

    public String getLrpBookNo() {
        return lrpBookNo;
    }

    public void setLrpBookNo(String lrpBookNo) {
        this.lrpBookNo = lrpBookNo;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getNotify1Check() {
        return notify1Check;
    }

    public void setNotify1Check(String notify1Check) {
        this.notify1Check = notify1Check;
    }

    public String getNotifyCheck() {
        return notifyCheck;
    }

    public void setNotifyCheck(String notifyCheck) {
        this.notifyCheck = notifyCheck;
    }

    public String getShipperCheck() {
        return shipperCheck;
    }

    public void setShipperCheck(String shipperCheck) {
        this.shipperCheck = shipperCheck;
    }

    public String getEqpstatus() {
        return eqpstatus;
    }

    public void setEqpstatus(String eqpstatus) {
        this.eqpstatus = eqpstatus;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getCommodityothers() {
        return commodityothers;
    }

    public void setCommodityothers(String commodityothers) {
        this.commodityothers = commodityothers;
    }

    public String getEbook_notify_id_n1() {
        return ebook_notify_id_n1;
    }

    public void setEbook_notify_id_n1(String ebook_notify_id_n1) {
        this.ebook_notify_id_n1 = ebook_notify_id_n1;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEbook_notifyaddress_n1() {
        return ebook_notifyaddress_n1;
    }

    public void setEbook_notifyaddress_n1(String ebook_notifyaddress_n1) {
        this.ebook_notifyaddress_n1 = ebook_notifyaddress_n1;
    }

    public String getEbook_notifychk_n1() {
        return ebook_notifychk_n1;
    }

    public void setEbook_notifychk_n1(String ebook_notifychk_n1) {
        this.ebook_notifychk_n1 = ebook_notifychk_n1;
    }

    public String getEbook_notifycity_desc_n1() {
        return ebook_notifycity_desc_n1;
    }

    public void setEbook_notifycity_desc_n1(String ebook_notifycity_desc_n1) {
        this.ebook_notifycity_desc_n1 = ebook_notifycity_desc_n1;
    }

    public String getEbook_notifycity_hdn_n1() {
        return ebook_notifycity_hdn_n1;
    }

    public void setEbook_notifycity_hdn_n1(String ebook_notifycity_hdn_n1) {
        this.ebook_notifycity_hdn_n1 = ebook_notifycity_hdn_n1;
    }

    public String getEbook_notifycity_n1() {
        return ebook_notifycity_n1;
    }

    public void setEbook_notifycity_n1(String ebook_notifycity_n1) {
        this.ebook_notifycity_n1 = ebook_notifycity_n1;
    }

    public String getEbook_notifycode_n1() {
        return ebook_notifycode_n1;
    }

    public void setEbook_notifycode_n1(String ebook_notifycode_n1) {
        this.ebook_notifycode_n1 = ebook_notifycode_n1;
    }

    public String getEbook_notifycountry_desc_n1() {
        return ebook_notifycountry_desc_n1;
    }

    public void setEbook_notifycountry_desc_n1(String ebook_notifycountry_desc_n1) {
        this.ebook_notifycountry_desc_n1 = ebook_notifycountry_desc_n1;
    }

    public String getEbook_notifycountry_n1() {
        return ebook_notifycountry_n1;
    }

    public void setEbook_notifycountry_n1(String ebook_notifycountry_n1) {
        this.ebook_notifycountry_n1 = ebook_notifycountry_n1;
    }

    public String getEbook_notifycountry_name_n1() {
        return ebook_notifycountry_name_n1;
    }

    public void setEbook_notifycountry_name_n1(String ebook_notifycountry_name_n1) {
        this.ebook_notifycountry_name_n1 = ebook_notifycountry_name_n1;
    }

    public String getEbook_notifycountrycode_n1() {
        return ebook_notifycountrycode_n1;
    }

    public void setEbook_notifycountrycode_n1(String ebook_notifycountrycode_n1) {
        this.ebook_notifycountrycode_n1 = ebook_notifycountrycode_n1;
    }

    public String getEbook_notifyemail_n1() {
        return ebook_notifyemail_n1;
    }

    public void setEbook_notifyemail_n1(String ebook_notifyemail_n1) {
        this.ebook_notifyemail_n1 = ebook_notifyemail_n1;
    }

    public String getEbook_notifyfax1_n1() {
        return ebook_notifyfax1_n1;
    }

    public void setEbook_notifyfax1_n1(String ebook_notifyfax1_n1) {
        this.ebook_notifyfax1_n1 = ebook_notifyfax1_n1;
    }

    public String getEbook_notifyfax2_n2() {
        return ebook_notifyfax2_n2;
    }

    public void setEbook_notifyfax2_n2(String ebook_notifyfax2_n2) {
        this.ebook_notifyfax2_n2 = ebook_notifyfax2_n2;
    }

    public String getEbook_notifyfax3_n3() {
        return ebook_notifyfax3_n3;
    }

    public void setEbook_notifyfax3_n3(String ebook_notifyfax3_n3) {
        this.ebook_notifyfax3_n3 = ebook_notifyfax3_n3;
    }

    public String getEbook_notifyfax_n1() {
        return ebook_notifyfax_n1;
    }

    public void setEbook_notifyfax_n1(String ebook_notifyfax_n1) {
        this.ebook_notifyfax_n1 = ebook_notifyfax_n1;
    }

    public String getEbook_notifyname_n1() {
        return ebook_notifyname_n1;
    }

    public void setEbook_notifyname_n1(String ebook_notifyname_n1) {
        this.ebook_notifyname_n1 = ebook_notifyname_n1;
    }

    public String getEbook_notifyphone1_n1() {
        return ebook_notifyphone1_n1;
    }

    public void setEbook_notifyphone1_n1(String ebook_notifyphone1_n1) {
        this.ebook_notifyphone1_n1 = ebook_notifyphone1_n1;
    }

    public String getEbook_notifyphone2_n1() {
        return ebook_notifyphone2_n1;
    }

    public void setEbook_notifyphone2_n1(String ebook_notifyphone2_n1) {
        this.ebook_notifyphone2_n1 = ebook_notifyphone2_n1;
    }

    public String getEbook_notifyphone3_n1() {
        return ebook_notifyphone3_n1;
    }

    public String getEbook_cmdtybuyerName() {
        return ebook_cmdtybuyerName;
    }

    public void setEbook_cmdtybuyerName(String ebook_cmdtybuyerName) {
        this.ebook_cmdtybuyerName = ebook_cmdtybuyerName;
    }

    public String getEbook_cmdtysellerName() {
        return ebook_cmdtysellerName;
    }

    public void setEbook_cmdtysellerName(String ebook_cmdtysellerName) {
        this.ebook_cmdtysellerName = ebook_cmdtysellerName;
    }

    public void setEbook_notifyphone3_n1(String ebook_notifyphone3_n1) {
        this.ebook_notifyphone3_n1 = ebook_notifyphone3_n1;
    }

    public String getEbook_notifyphone_n1() {
        return ebook_notifyphone_n1;
    }

    public void setEbook_notifyphone_n1(String ebook_notifyphone_n1) {
        this.ebook_notifyphone_n1 = ebook_notifyphone_n1;
    }

    public String getEbook_notifyzipcode_n1() {
        return ebook_notifyzipcode_n1;
    }

    public void setEbook_notifyzipcode_n1(String ebook_notifyzipcode_n1) {
        this.ebook_notifyzipcode_n1 = ebook_notifyzipcode_n1;
    }
    //

    public String getEbook_notifyaddress1_n1() {
        return ebook_notifyaddress1_n1;
    }

    public void setEbook_notifyaddress1_n1(String ebook_notifyaddress1_n1) {
        this.ebook_notifyaddress1_n1 = ebook_notifyaddress1_n1;
    }

    public String getRoutedeliveryname() {
        return routedeliveryname;
    }

    public void setRoutedeliveryname(String routedeliveryname) {
        this.routedeliveryname = routedeliveryname;
    }

    public String getRouteoriginname() {
        return routeoriginname;
    }

    public void setRouteoriginname(String routeoriginname) {
        this.routeoriginname = routeoriginname;
    }

    public String getRoutepodname() {
        return routepodname;
    }

    public void setRoutepodname(String routepodname) {
        this.routepodname = routepodname;
    }

    public String getRoutepodter() {
        return routepodter;
    }

    public void setRoutepodter(String routepodter) {
        this.routepodter = routepodter;
    }

    public String getRoutepolname() {
        return routepolname;
    }

    public void setRoutepolname(String routepolname) {
        this.routepolname = routepolname;
    }

    public String getRoutepolter() {
        return routepolter;
    }

    public void setRoutepolter(String routepolter) {
        this.routepolter = routepolter;
    }

    public String getEbook_agreeaddress1() {
        return ebook_agreeaddress1;
    }

    public void setEbook_agreeaddress1(String ebook_agreeaddress1) {
        this.ebook_agreeaddress1 = ebook_agreeaddress1;
    }

    public String getEbook_consigneeaddress1() {
        return ebook_consigneeaddress1;
    }

    public void setEbook_consigneeaddress1(String ebook_consigneeaddress1) {
        this.ebook_consigneeaddress1 = ebook_consigneeaddress1;
    }

    public String getEbook_notifyaddress1() {
        return ebook_notifyaddress1;
    }

    public void setEbook_notifyaddress1(String ebook_notifyaddress1) {
        this.ebook_notifyaddress1 = ebook_notifyaddress1;
    }

    public String getEbook_shipperaddress1() {
        return ebook_shipperaddress1;
    }

    public void setEbook_shipperaddress1(String ebook_shipperaddress1) {
        this.ebook_shipperaddress1 = ebook_shipperaddress1;
    }

    public String getDuplicateEqp() {
        return duplicateEqp;
    }

    public void setDuplicateEqp(String duplicateEqp) {
        this.duplicateEqp = duplicateEqp;
    }

    public String getCargoid_new() {
        return cargoid_new;
    }

    public void setCargoid_new(String cargoid_new) {
        this.cargoid_new = cargoid_new;
    }

    public String getEbook_agree_id() {
        return ebook_agree_id;
    }

    public void setEbook_agree_id(String ebook_agree_id) {
        this.ebook_agree_id = ebook_agree_id;
    }

    public String getEbook_agreeaddress() {
        return ebook_agreeaddress;
    }

    public void setEbook_agreeaddress(String ebook_agreeaddress) {
        this.ebook_agreeaddress = ebook_agreeaddress;
    }

    public String getEbook_agreecity() {
        return ebook_agreecity;
    }

    public void setEbook_agreecity(String ebook_agreecity) {
        this.ebook_agreecity = ebook_agreecity;
    }

    public String getEbook_agreecity_desc() {
        return ebook_agreecity_desc;
    }

    public void setEbook_agreecity_desc(String ebook_agreecity_desc) {
        this.ebook_agreecity_desc = ebook_agreecity_desc;
    }

    public String getEbook_agreecity_hdn() {
        return ebook_agreecity_hdn;
    }

    public void setEbook_agreecity_hdn(String ebook_agreecity_hdn) {
        this.ebook_agreecity_hdn = ebook_agreecity_hdn;
    }

    public String getEbook_agreecode() {
        return ebook_agreecode;
    }

    public void setEbook_agreecode(String ebook_agreecode) {
        this.ebook_agreecode = ebook_agreecode;
    }

    public String getEbook_agreecountry() {
        return ebook_agreecountry;
    }

    public void setEbook_agreecountry(String ebook_agreecountry) {
        this.ebook_agreecountry = ebook_agreecountry;
    }

    public String getEbook_agreecountry_desc() {
        return ebook_agreecountry_desc;
    }

    public void setEbook_agreecountry_desc(String ebook_agreecountry_desc) {
        this.ebook_agreecountry_desc = ebook_agreecountry_desc;
    }

    public String getEbook_agreecountry_name() {
        return ebook_agreecountry_name;
    }

    public void setEbook_agreecountry_name(String ebook_agreecountry_name) {
        this.ebook_agreecountry_name = ebook_agreecountry_name;
    }

    public String getEbook_agreecountrycode() {
        return ebook_agreecountrycode;
    }

    public void setEbook_agreecountrycode(String ebook_agreecountrycode) {
        this.ebook_agreecountrycode = ebook_agreecountrycode;
    }

    public String getEbook_agreeemail() {
        return ebook_agreeemail;
    }

    public void setEbook_agreeemail(String ebook_agreeemail) {
        this.ebook_agreeemail = ebook_agreeemail;
    }

    public String getEbook_agreefax() {
        return ebook_agreefax;
    }

    public void setEbook_agreefax(String ebook_agreefax) {
        this.ebook_agreefax = ebook_agreefax;
    }

    public String getEbook_agreename() {
        return ebook_agreename;
    }

    public void setEbook_agreename(String ebook_agreename) {
        this.ebook_agreename = ebook_agreename;
    }

    public String getEbook_agreephone() {
        return ebook_agreephone;
    }

    public void setEbook_agreephone(String ebook_agreephone) {
        this.ebook_agreephone = ebook_agreephone;
    }

    public String getEbook_agreezipcode() {
        return ebook_agreezipcode;
    }

    public void setEbook_agreezipcode(String ebook_agreezipcode) {
        this.ebook_agreezipcode = ebook_agreezipcode;
    }

    public String getEbook_orderNo() {
        return ebook_orderNo;
    }

    public void setEbook_orderNo(String ebook_orderNo) {
        this.ebook_orderNo = ebook_orderNo;
    }

    public String getEbook_servtype() {
        return ebook_servtype;
    }

    public void setEbook_servtype(String ebook_servtype) {
        this.ebook_servtype = ebook_servtype;
    }

    public String getEbook_ratetype() {
        return Validation.checkNull(ebook_ratetype);
    }

    public void setEbook_ratetype(String ebook_ratetype) {
        this.ebook_ratetype = ebook_ratetype;
    }

    public String getHidden_oog() {
        return hidden_oog;
    }

    public void setHidden_oog(String hidden_oog) {
        this.hidden_oog = hidden_oog;
    }

    public String getEbook_package() {
        return ebook_package;
    }

    public void setEbook_package(String ebook_package) {
        this.ebook_package = ebook_package;
    }

    public String getHidden_hzd() {
        return hidden_hzd;
    }

    public void setHidden_hzd(String hidden_hzd) {
        this.hidden_hzd = hidden_hzd;
    }

    public String getHidden_lcl() {
        return hidden_lcl;
    }

    public void setHidden_lcl(String hidden_lcl) {
        this.hidden_lcl = hidden_lcl;
    }

    public String getHidden_rfr() {
        return hidden_rfr;
    }

    public void setHidden_rfr(String hidden_rfr) {
        this.hidden_rfr = hidden_rfr;
    }

    public String getEbook_cargovolume() {
        return ebook_cargovolume;
    }

    public void setEbook_cargovolume(String ebook_cargovolume) {
        this.ebook_cargovolume = ebook_cargovolume;
    }

    public String getEbook_dgclass() {
        return ebook_dgclass;
    }

    public void setEbook_dgclass(String ebook_dgclass) {
        this.ebook_dgclass = ebook_dgclass;
    }

    public String getEbook_dimensionunit() {
        return ebook_dimensionunit;
    }

    public void setEbook_dimensionunit(String ebook_dimensionunit) {
        this.ebook_dimensionunit = ebook_dimensionunit;
    }

    public String getEbook_flashpoint() {
        return ebook_flashpoint;
    }

    public void setEbook_flashpoint(String ebook_flashpoint) {
        this.ebook_flashpoint = ebook_flashpoint;
    }

    public String getEbook_flashunit() {
        return ebook_flashunit;
    }

    public void setEbook_flashunit(String ebook_flashunit) {
        this.ebook_flashunit = ebook_flashunit;
    }

    public String getEbook_maxtemp() {
        return ebook_maxtemp;
    }

    public void setEbook_maxtemp(String ebook_maxtemp) {
        this.ebook_maxtemp = ebook_maxtemp;
    }

    public String getEbook_mintemp() {
        return ebook_mintemp;
    }

    public void setEbook_mintemp(String ebook_mintemp) {
        this.ebook_mintemp = ebook_mintemp;
    }

    public String getEbook_nor() {
        return ebook_nor;
    }

    public void setEbook_nor(String ebook_nor) {
        this.ebook_nor = ebook_nor;
    }

    public String getEbook_oh() {
        return ebook_oh;
    }

    public void setEbook_oh(String ebook_oh) {
        this.ebook_oh = ebook_oh;
    }

    public String getEbook_olaft() {
        return ebook_olaft;
    }

    public void setEbook_olaft(String ebook_olaft) {
        this.ebook_olaft = ebook_olaft;
    }

    public String getEbook_olforward() {
        return ebook_olforward;
    }

    public void setEbook_olforward(String ebook_olforward) {
        this.ebook_olforward = ebook_olforward;
    }

    public String getEbook_owleft() {
        return ebook_owleft;
    }

    public void setEbook_owleft(String ebook_owleft) {
        this.ebook_owleft = ebook_owleft;
    }

    public String getEbook_owright() {
        return ebook_owright;
    }

    public void setEbook_owright(String ebook_owright) {
        this.ebook_owright = ebook_owright;
    }

    public String getEbook_poluttunit() {
        return ebook_poluttunit;
    }

    public void setEbook_poluttunit(String ebook_poluttunit) {
        this.ebook_poluttunit = ebook_poluttunit;
    }

    public String getEbook_presetemp() {
        return ebook_presetemp;
    }

    public void setEbook_presetemp(String ebook_presetemp) {
        this.ebook_presetemp = ebook_presetemp;
    }

    public String getEbook_shipname() {
        return ebook_shipname;
    }

    public void setEbook_shipname(String ebook_shipname) {
        this.ebook_shipname = ebook_shipname;
    }

    public String getEbook_temperunit() {
        return ebook_temperunit;
    }

    public void setEbook_temperunit(String ebook_temperunit) {
        this.ebook_temperunit = ebook_temperunit;
    }

    public String getEbook_unno() {
        return ebook_unno;
    }

    public void setEbook_unno(String ebook_unno) {
        this.ebook_unno = ebook_unno;
    }
    
    // Multiple DG Details Support - Getters and Setters
    public List<net.ecom.vo.AmendmentBookingFormBean> getDgDetailsList() {
        return dgDetailsList;
    }

    public void setDgDetailsList(List<net.ecom.vo.AmendmentBookingFormBean> dgDetailsList) {
        this.dgDetailsList = dgDetailsList;
    }

    public int getDgCount() {
        return dgCount;
    }

    public void setDgCount(int dgCount) {
        this.dgCount = dgCount;
    }

    public String getEbook_volumeunit() {
        return ebook_volumeunit;
    }

    public void setEbook_volumeunit(String ebook_volumeunit) {
        this.ebook_volumeunit = ebook_volumeunit;
    }

    public String getName() {
        return Validation.checkNull(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBooking_refno() {
        return Validation.checkNull(booking_refno);
    }

    public void setBooking_refno(String booking_refno) {
        this.booking_refno = booking_refno;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getEbook_date() {
        return Validation.checkNull(ebook_date);
    }

    public void setEbook_date(String ebook_date) {
        this.ebook_date = ebook_date;
    }

    public String getEbook_dateBut() {
        return Validation.checkNull(ebook_dateBut);
    }

    public void setEbook_dateBut(String ebook_dateBut) {
        this.ebook_dateBut = ebook_dateBut;
    }

    public String getEbook_portofloading() {
//        sisObj.sanitizeInput(ebook_portofloading);
        return Validation.checkNull(ebook_portofloading);
    }

    public void setEbook_portofloading(String ebook_portofloading) {
        this.ebook_portofloading = ebook_portofloading;
    }

    public String getEbook_portofdischarge() {
//        sisObj.sanitizeInput(ebook_portofdischarge);
        return Validation.checkNull(ebook_portofdischarge);
    }

    public void setEbook_portofdischarge(String ebook_portofdischarge) {
        this.ebook_portofdischarge = ebook_portofdischarge;
    }

    public String getEbook_servicecode() {
//        sisObj.sanitizeInput(ebook_servicecode);
        return Validation.checkNull(ebook_servicecode);
    }

    public void setEbook_servicecode(String ebook_servicecode) {
        this.ebook_servicecode = ebook_servicecode;
    }

    public String getEbook_portofloading_desc() {
//        sisObj.sanitizeInput(ebook_portofloading_desc);
        return Validation.checkNull(ebook_portofloading_desc);
    }

    public void setEbook_portofloading_desc(String ebook_portofloading_desc) {
        this.ebook_portofloading_desc = ebook_portofloading_desc;
    }

    public String getEbook_portofdischarge_desc() {
//        sisObj.sanitizeInput(ebook_portofdischarge_desc);
        return Validation.checkNull(ebook_portofdischarge_desc);
    }

    public void setEbook_portofdischarge_desc(String ebook_portofdischarge_desc) {
        this.ebook_portofdischarge_desc = ebook_portofdischarge_desc;
    }

    public String getEbooking_service() {
//        sisObj.sanitizeInput(ebooking_service);
        return Validation.checkNull(ebooking_service);
    }

    public void setEbooking_service(String ebooking_service) {
        this.ebooking_service = ebooking_service;
    }

    public String getEbook_lineservicecode() {
//        sisObj.sanitizeInput(ebook_lineservicecode);
        return Validation.checkNull(ebook_lineservicecode);
    }

    public void setEbook_lineservicecode(String ebook_lineservicecode) {
        this.ebook_lineservicecode = ebook_lineservicecode;
    }

    public String getEbook_lineservice() {
//        sisObj.sanitizeInput(ebook_lineservice);
        return Validation.checkNull(ebook_lineservice);
    }

    public void setEbook_lineservice(String ebook_lineservice) {
        this.ebook_lineservice = ebook_lineservice;
    }

    public String getEbook_vesselcode() {
//        sisObj.sanitizeInput(ebook_vesselcode);
        return Validation.checkNull(ebook_vesselcode);
    }

    public void setEbook_vesselcode(String ebook_vesselcode) {
        this.ebook_vesselcode = ebook_vesselcode;
    }

    public String getEbooking_vessel() {
//        sisObj.sanitizeInput(ebooking_vessel);
        return Validation.checkNull(ebooking_vessel);
    }

    public void setEbooking_vessel(String ebooking_vessel) {
        this.ebooking_vessel = ebooking_vessel;
    }

    public String getEbook_linevesselcode() {
//        sisObj.sanitizeInput(ebook_linevesselcode);
        return Validation.checkNull(ebook_linevesselcode);
    }

    public void setEbook_linevesselcode(String ebook_linevesselcode) {
        this.ebook_linevesselcode = ebook_linevesselcode;
    }

    public String getEbook_linevessel() {
//        sisObj.sanitizeInput(ebook_linevessel);
        return Validation.checkNull(ebook_linevessel);
    }

    public void setEbook_linevessel(String ebook_linevessel) {
        this.ebook_linevessel = ebook_linevessel;
    }

    public String getEbook_voyage() {
//        sisObj.sanitizeInput(ebook_voyage);
        return Validation.checkNull(ebook_voyage);
    }

    public void setEbook_voyage(String ebook_voyage) {
        this.ebook_voyage = ebook_voyage;
    }

    public String getEbook_bound_leg() {
        return Validation.checkNull(ebook_bound_leg);
    }

    public void setEbook_bound_leg(String ebook_bound_leg) {
        this.ebook_bound_leg = ebook_bound_leg;
    }

    public String getEbook_linebound_leg() {
        return Validation.checkNull(ebook_linebound_leg);
    }

    public void setEbook_linebound_leg(String ebook_linebound_leg) {
        this.ebook_linebound_leg = ebook_linebound_leg;
    }

    public String getEbook_linevoyage() {
//        sisObj.sanitizeInput(ebook_linevoyage);
        return Validation.checkNull(ebook_linevoyage);
    }

    public void setEbook_linevoyage(String ebook_linevoyage) {
        this.ebook_linevoyage = ebook_linevoyage;
    }

    public String getEbook_origin() {
        return Validation.checkNull(ebook_origin);
    }

    public void setEbook_origin(String ebook_origin) {
        this.ebook_origin = ebook_origin;
    }

    public String getEbook_origin_country() {
//        sisObj.sanitizeInput(ebook_origin_country);
        return Validation.checkNull(ebook_origin_country);
    }

    public void setEbook_origin_country(String ebook_origin_country) {
        this.ebook_origin_country = ebook_origin_country;
    }

    public String getEbook_pol_country() {
//        sisObj.sanitizeInput(ebook_pol_country);
        return Validation.checkNull(ebook_pol_country);
    }

    public void setEbook_pol_country(String ebook_pol_country) {
        this.ebook_pol_country = ebook_pol_country;
    }

    public String getEbook_pol() {
        return Validation.checkNull(ebook_pol);
    }

    public void setEbook_pol(String ebook_pol) {
        this.ebook_pol = ebook_pol;
    }

    public String getEbook_terminal() {
        return Validation.checkNull(ebook_terminal);
    }

    public void setEbook_terminal(String ebook_terminal) {
        this.ebook_terminal = ebook_terminal;
    }

    public String getEbook_terminal_country() {
//        sisObj.sanitizeInput(ebook_terminal_country);
        return Validation.checkNull(ebook_terminal_country);
    }

    public void setEbook_terminal_country(String ebook_terminal_country) {
        this.ebook_terminal_country = ebook_terminal_country;
    }

    public String getEbook_pol_etd() {
        return Validation.checkNull(ebook_pol_etd);
    }

    public void setEbook_pol_etd(String ebook_pol_etd) {
        this.ebook_pol_etd = ebook_pol_etd;
    }

    public String getEbook_pod() {
        return Validation.checkNull(ebook_pod);
    }

    public void setEbook_pod(String ebook_pod) {
        this.ebook_pod = ebook_pod;
    }

    public String getEbook_pod_country() {
//        sisObj.sanitizeInput(ebook_pod_country);
        return Validation.checkNull(ebook_pod_country);
    }

    public void setEbook_pod_country(String ebook_pod_country) {
        this.ebook_pod_country = ebook_pod_country;
    }

    public String getEbook_terminalpod() {
//        sisObj.sanitizeInput(ebook_terminalpod);
        return Validation.checkNull(ebook_terminalpod);
    }

    public void setEbook_terminalpod(String ebook_terminalpod) {
        this.ebook_terminalpod = ebook_terminalpod;
    }

    public String getEbook_terminalpod_country() {
//        sisObj.sanitizeInput(ebook_terminalpod_country);
        return Validation.checkNull(ebook_terminalpod_country);
    }

    public void setEbook_terminalpod_country(String ebook_terminalpod_country) {
        this.ebook_terminalpod_country = ebook_terminalpod_country;
    }

    public String getEbook_pol_eta() {
        return Validation.checkNull(ebook_pol_eta);
    }

    public void setEbook_pol_eta(String ebook_pol_eta) {
        this.ebook_pol_eta = ebook_pol_eta;
    }

    public String getEbook_delivery() {
//        sisObj.sanitizeInput(ebook_delivery);
        return Validation.checkNull(ebook_delivery);
    }

    public void setEbook_delivery(String ebook_delivery) {
        this.ebook_delivery = ebook_delivery;
    }

    public String getEbook_delivery_country() {
//        sisObj.sanitizeInput(ebook_delivery_country);
        return Validation.checkNull(ebook_delivery_country);
    }

    public String getEbook_status_remarks() {
//        sisObj.sanitizeInput(ebook_status_remarks);
        return Validation.checkNull(ebook_status_remarks);
    }

    public void setEbook_status_remarks(String ebook_status_remarks) {
        this.ebook_status_remarks = ebook_status_remarks;
    }

    public String getEbook_userstatus() {
        return Validation.checkNull(ebook_userstatus);
    }

    public void setEbook_userstatus(String ebook_userstatus) {
        this.ebook_userstatus = ebook_userstatus;
    }

    public String getEbook_bookagency() {
        return Validation.checkNull(ebook_bookagency);
    }

    public void setEbook_bookagency(String ebook_bookagency) {
        this.ebook_bookagency = ebook_bookagency;
    }

    public String getEbook_bookagency_hdn() {
        return Validation.checkNull(ebook_bookagency_hdn);
    }

    public void setEbook_bookagency_hdn(String ebook_bookagency_hdn) {
        this.ebook_bookagency_hdn = ebook_bookagency_hdn;
    }

    public void setEbook_delivery_country(String ebook_delivery_country) {
        this.ebook_delivery_country = ebook_delivery_country;
    }

    public String getEbook_shippername() {
        return Validation.checkNull(ebook_shippername);
    }

    public void setEbook_shippername(String ebook_shippername) {
        this.ebook_shippername = ebook_shippername;
    }

    public String getEbook_shippercode() {
        return Validation.checkNull(ebook_shippercode);
    }

    public void setEbook_shippercode(String ebook_shippercode) {
        this.ebook_shippercode = ebook_shippercode;
    }

    public String getEbook_shipperzipcode() {
        return Validation.checkNull(ebook_shipperzipcode);
    }

    public void setEbook_shipperzipcode(String ebook_shipperzipcode) {
        this.ebook_shipperzipcode = ebook_shipperzipcode;
    }

    public String getEbook_shipperaddress() {
        return Validation.checkNull(ebook_shipperaddress);
    }

    public void setEbook_shipperaddress(String ebook_shipperaddress) {
        this.ebook_shipperaddress = ebook_shipperaddress;
    }

    public String getEbook_shippercountry() {
        return Validation.checkNull(ebook_shippercountry);
    }

    public void setEbook_shippercountry(String ebook_shippercountry) {
        this.ebook_shippercountry = ebook_shippercountry;
    }

    public String getEbook_shippercity() {
        return Validation.checkNull(ebook_shippercity);
    }

    public void setEbook_shippercity(String ebook_shippercity) {
        this.ebook_shippercity = ebook_shippercity;
    }

    public String getEbook_shipperphone() {
        return Validation.checkNull(ebook_shipperphone);
    }

    public void setEbook_shipperphone(String ebook_shipperphone) {
        this.ebook_shipperphone = ebook_shipperphone;
    }

    public String getEbook_shipperfax() {
        return Validation.checkNull(ebook_shipperfax);
    }

    public void setEbook_shipperfax(String ebook_shipperfax) {
        this.ebook_shipperfax = ebook_shipperfax;
    }

    public String getEbook_shipperemail() {
        return Validation.checkNull(ebook_shipperemail);
    }

    public void setEbook_shipperemail(String ebook_shipperemail) {
        this.ebook_shipperemail = ebook_shipperemail;
    }

    public String getEbook_consigneename() {
        return Validation.checkNull(ebook_consigneename);
    }

    public void setEbook_consigneename(String ebook_consigneename) {
        this.ebook_consigneename = ebook_consigneename;
    }

    public String getEbook_consigneecode() {
//        System.out.println("[ECOM-WAR] AmendmentBookingFormBean -here formbean-"+ebook_consigneecode);
        return Validation.checkNull(ebook_consigneecode);
    }

    public void setEbook_consigneecode(String ebook_consigneecode) {
        this.ebook_consigneecode = ebook_consigneecode;
    }

    public String getEbook_consigneezipcode() {

        return Validation.checkNull(ebook_consigneezipcode);
    }

    public void setEbook_consigneezipcode(String ebook_consigneezipcode) {
        this.ebook_consigneezipcode = ebook_consigneezipcode;
    }

    public String getEbook_consigneeaddress() {
        return Validation.checkNull(ebook_consigneeaddress);
    }

    public void setEbook_consigneeaddress(String ebook_consigneeaddress) {
        this.ebook_consigneeaddress = ebook_consigneeaddress;
    }

    public String getEbook_consigneecountry() {
        return Validation.checkNull(ebook_consigneecountry);
    }

    public void setEbook_consigneecountry(String ebook_consigneecountry) {
        this.ebook_consigneecountry = ebook_consigneecountry;
    }

    public String getEbook_consigneecity() {
        return Validation.checkNull(ebook_consigneecity);
    }

    public void setEbook_consigneecity(String ebook_consigneecity) {
        this.ebook_consigneecity = ebook_consigneecity;
    }

    public String getEbook_consigneephone() {;
        return Validation.checkNull(ebook_consigneephone);
    }

    public void setEbook_consigneephone(String ebook_consigneephone) {
        this.ebook_consigneephone = ebook_consigneephone;
    }

    public String getEbook_consigneefax() {
        return Validation.checkNull(ebook_consigneefax);
    }

    public void setEbook_consigneefax(String ebook_consigneefax) {
        this.ebook_consigneefax = ebook_consigneefax;
    }

    public String getEbook_consigneeemail() {
        return Validation.checkNull(ebook_consigneeemail);
    }

    public void setEbook_consigneeemail(String ebook_consigneeemail) {
        this.ebook_consigneeemail = ebook_consigneeemail;
    }

    public String getEbook_notifyname() {
        return Validation.checkNull(ebook_notifyname);
    }

    public void setEbook_notifycode(String ebook_notifycode) {
        // System.out.println("[ECOM-WAR] AmendmentBookingFormBean -->" + ebook_notifycode);
        this.ebook_notifycode = ebook_notifycode;
    }

    public String getEbook_notifycode() {
        return Validation.checkNull(ebook_notifycode);
    }

    public void setEbook_notifyzipcode(String ebook_notifyzipcode) {
        this.ebook_notifyzipcode = ebook_notifyzipcode;
    }

    public String getEbook_notifyzipcode() {
        return Validation.checkNull(ebook_notifyzipcode);
    }

    public void setEbook_notifyname(String ebook_notifyname) {
        this.ebook_notifyname = ebook_notifyname;
    }

    public String getEbook_notifyaddress() {
        return Validation.checkNull(ebook_notifyaddress);
    }

    public void setEbook_notifyaddress(String ebook_notifyaddress) {
        this.ebook_notifyaddress = ebook_notifyaddress;
    }

    public String getEbook_notifycountry() {
        return Validation.checkNull(ebook_notifycountry);
    }

    public void setEbook_notifycountry(String ebook_notifycountry) {
        this.ebook_notifycountry = ebook_notifycountry;
    }

    public String getEbook_notifycity() {
        return Validation.checkNull(ebook_notifycity);
    }

    public void setEbook_notifycity(String ebook_notifycity) {
        this.ebook_notifycity = ebook_notifycity;
    }

    public String getEbook_notifyphone() {
        return Validation.checkNull(ebook_notifyphone);
    }

    public void setEbook_notifyphone(String ebook_notifyphone) {
        this.ebook_notifyphone = ebook_notifyphone;
    }

    public String getEbook_notifyfax() {
        return Validation.checkNull(ebook_notifyfax);
    }

    public void setEbook_notifyfax(String ebook_notifyfax) {
        this.ebook_notifyfax = ebook_notifyfax;
    }

    public String getEbook_notifyemail() {
        return Validation.checkNull(ebook_notifyemail);
    }

    public void setEbook_notifyemail(String ebook_notifyemail) {
        this.ebook_notifyemail = ebook_notifyemail;
    }

    public String getEbook_tareweight() {
//        sisObj.sanitizeInput(ebook_tareweight);
        return Validation.checkNull(ebook_tareweight);
    }

    public void setEbook_tareweight(String ebook_tareweight) {
        this.ebook_tareweight = ebook_tareweight;
    }

    public String getEbook_weightunit() {
//        sisObj.sanitizeInput(ebook_weightunit);
        return Validation.checkNull(ebook_weightunit);
    }

    public void setEbook_weightunit(String ebook_weightunit) {
        this.ebook_weightunit = ebook_weightunit;
    }

    public String getEbooking_nor() {
        return Validation.checkNull(ebooking_nor);
    }

    public void setEbooking_nor(String ebooking_nor) {
        this.ebooking_nor = ebooking_nor;
    }

    public String getEbooking_reefer() {
//        sisObj.sanitizeInput(ebooking_reefer);
        return Validation.checkNull(ebooking_reefer);
    }

    public void setEbooking_reefer(String ebooking_reefer) {
        this.ebooking_reefer = ebooking_reefer;
    }

    public String getEbook_minimumtemp() {
//        sisObj.sanitizeInput(ebook_minimumtemp);
        return Validation.checkNull(ebook_minimumtemp);
    }

    public void setEbook_minimumtemp(String ebook_minimumtemp) {
        this.ebook_minimumtemp = ebook_minimumtemp;
    }

    public String getEbook_tempunit() {
//        sisObj.sanitizeInput(ebook_tempunit);
        return Validation.checkNull(ebook_tempunit);
    }

    public void setEbook_tempunit(String ebook_tempunit) {
        this.ebook_tempunit = ebook_tempunit;
    }

    public String getEbook_settemp() {
//       sisObj.sanitizeInput(ebook_settemp);
        return Validation.checkNull(ebook_settemp);
    }

    public void setEbook_settemp(String ebook_settemp) {
        this.ebook_settemp = ebook_settemp;
    }

    public String getEbook_maxitemp() {
//        sisObj.sanitizeInput(ebook_maxitemp);
        return Validation.checkNull(ebook_maxitemp);
    }

    public void setEbook_maxitemp(String ebook_maxitemp) {
        this.ebook_maxitemp = ebook_maxitemp;
    }

    public String getEbook_genset() {
//        sisObj.sanitizeInput(ebook_genset);
        return Validation.checkNull(ebook_genset);
    }

    public void setEbook_genset(String ebook_genset) {
        this.ebook_genset = ebook_genset;
    }

    public String[] getEbook_quantity() {

        return ebook_quantity;
    }

    public void setEbook_quantity(String[] ebook_quantity) {
        this.ebook_quantity = ebook_quantity;
    }

    public String getEbook_packcode() {
//        sisObj.sanitizeInput(ebook_packcode);
        return Validation.checkNull(ebook_packcode);
    }

    public void setEbook_packcode(String ebook_packcode) {
        this.ebook_packcode = ebook_packcode;
    }

    public String getEbooking_package() {
        return Validation.checkNull(ebooking_package);
    }

    public void setEbooking_package(String ebooking_package) {
        this.ebooking_package = ebooking_package;
    }

    public String[] getEbook_equipment() {

        return ebook_equipment;
    }

    public void setEbook_equipment(String[] ebook_equipment) {
        this.ebook_equipment = ebook_equipment;
    }

    public String getEbook_equipment_name() {
//        sisObj.sanitizeInput(ebook_equipment_name);
        return Validation.checkNull(ebook_equipment_name);
    }

    public void setEbook_equipment_name(String ebook_equipment_name) {
        this.ebook_equipment_name = ebook_equipment_name;
    }

    public String getEbook_hazardous() {
//        sisObj.sanitizeInput(ebook_hazardous);
        return Validation.checkNull(ebook_hazardous);
    }

    public void setEbook_hazardous(String ebook_hazardous) {
        this.ebook_hazardous = ebook_hazardous;
    }

    public String getEbook_hazardouscontact() {
//        sisObj.sanitizeInput(ebook_hazardouscontact);
        return Validation.checkNull(ebook_hazardouscontact);
    }

    public void setEbook_hazardouscontact(String ebook_hazardouscontact) {
        this.ebook_hazardouscontact = ebook_hazardouscontact;
    }

    public String getEbook_telephonecontact() {
//        sisObj.sanitizeInput(ebook_telephonecontact);
        return Validation.checkNull(ebook_telephonecontact);
    }

    public void setEbook_telephonecontact(String ebook_telephonecontact) {
        this.ebook_telephonecontact = ebook_telephonecontact;
    }

    public String getEbook_cargoweight() {
//        sisObj.sanitizeInput(ebook_cargoweight);
        return Validation.checkNull(ebook_cargoweight);
    }

    public void setEbook_cargoweight(String ebook_cargoweight) {
        this.ebook_cargoweight = ebook_cargoweight;
    }

    public String getEbook_grossweight() {
//        sisObj.sanitizeInput(ebook_grossweight);
        return Validation.checkNull(ebook_grossweight);
    }

    public void setEbook_grossweight(String ebook_grossweight) {
        this.ebook_grossweight = ebook_grossweight;
    }

    public String getEbook_templateno() {
//        sisObj.sanitizeInput(ebook_templateno);
        return Validation.checkNull(ebook_templateno);
    }

    public void setEbook_templateno(String ebook_templateno) {
        this.ebook_templateno = ebook_templateno;
    }

    public String getEbook_templatename() {
//        sisObj.sanitizeInput(ebook_templatename);
        return Validation.checkNull(ebook_templatename);
    }

    public void setEbook_templatename(String ebook_templatename) {
        this.ebook_templatename = ebook_templatename;
    }

    public String getEbook_packquantity() {
        return Validation.checkNull(ebook_packquantity);
    }

    public void setEbook_packquantity(String ebook_packquantity) {
        this.ebook_packquantity = ebook_packquantity;
    }

    public String getEbook_actCommoName() {
        return ebook_actCommoName;
    }

    public void setEbook_actCommoName(String ebook_actCommoName) {
        this.ebook_actCommoName = ebook_actCommoName;
    }

    public String getCargoclickadd() {
        return Validation.checkNull(cargoclickadd);
    }

    public void setCargoclickadd(String cargoclickadd) {
        this.cargoclickadd = cargoclickadd;
    }

    public String getEbook_conthdn() {
        return Validation.checkNull(ebook_conthdn);
    }

    public void setEbook_conthdn(String ebook_conthdn) {
        this.ebook_conthdn = ebook_conthdn;
    }

    public String getEbook_masthdn() {
        return Validation.checkNull(ebook_masthdn);
    }

    public void setEbook_masthdn(String ebook_masthdn) {
        this.ebook_masthdn = ebook_masthdn;
    }

    public String getSubmit_btn() {
        return Validation.checkNull(submit_btn);
    }

    public void setSubmit_btn(String submit_btn) {
        this.submit_btn = submit_btn;
    }

    public String getBtntype() {
        return btntype;
    }

    public void setBtntype(String btntype) {
        this.btntype = btntype;
    }

    public String getBtntype1() {
        return btntype1;
    }

    public void setBtntype1(String btntype1) {
        this.btntype1 = btntype1;
    }

    public String getBtntype2() {
        return btntype2;
    }

    public void setBtntype2(String btntype2) {
        this.btntype2 = btntype2;
    }

    public String getEbook_reefercargo() {
//        sisObj.sanitizeInput(ebook_reefercargo);
        return Validation.checkNull(ebook_reefercargo);
    }

    public void setEbook_reefercargo(String ebook_reefercargo) {
        this.ebook_reefercargo = ebook_reefercargo;
    }

    public String getEbook_drycargo() {
//        sisObj.sanitizeInput(ebook_drycargo);
        return Validation.checkNull(ebook_drycargo);
    }

    public void setEbook_drycargo(String ebook_drycargo) {
        this.ebook_drycargo = ebook_drycargo;
    }

    public String getEbook_shippercountrycode() {
        return Validation.checkNull(ebook_shippercountrycode);
    }

    public void setEbook_shippercountrycode(String ebook_shippercountrycode) {
        this.ebook_shippercountrycode = ebook_shippercountrycode;
    }

    public String getEbook_consigneecountrycode() {
        return Validation.checkNull(ebook_consigneecountrycode);
    }

    public void setEbook_consigneecountrycode(String ebook_consigneecountrycode) {
        this.ebook_consigneecountrycode = ebook_consigneecountrycode;
    }

    public String getEbook_notifychk() {
        return Validation.checkNull(ebook_notifychk);
    }

    public void setEbook_notifychk(String ebook_notifychk) {
        this.ebook_notifychk = ebook_notifychk;
    }

    public String getEbook_notifycountrycode() {
        return Validation.checkNull(ebook_notifycountrycode);
    }

    public void setEbook_notifycountrycode(String ebook_notifycountrycode) {
        this.ebook_notifycountrycode = ebook_notifycountrycode;
    }

    public String getEbook_commoditycode() {
        return Validation.checkNull(ebook_commoditycode);
    }

    public void setEbook_commoditycode(String ebook_commoditycode) {
        this.ebook_commoditycode = ebook_commoditycode;
    }

    public String getEbooking_commodity() {
        return Validation.checkNull(ebooking_commodity);
    }

    public void setEbooking_commodity(String ebooking_commodity) {
        this.ebooking_commodity = ebooking_commodity;
    }

    public String getEbook_commhdn() {
        return Validation.checkNull(ebook_commhdn);
    }

    public void setEbook_commhdn(String ebook_commhdn) {
        this.ebook_commhdn = ebook_commhdn;
    }

    public String[] getEbook_equipmentsize() {
        return ebook_equipmentsize;
    }

    public void setEbook_equipmentsize(String[] ebook_equipmentsize) {
        this.ebook_equipmentsize = ebook_equipmentsize;
    }

    public String getEbook_shippercountry_desc() {
//        sisObj.sanitizeInput(ebook_shippercountry_desc);
        return Validation.checkNull(ebook_shippercountry_desc);
    }

    public void setEbook_shippercountry_desc(String ebook_shippercountry_desc) {
        this.ebook_shippercountry_desc = ebook_shippercountry_desc;
    }

    public String getEbook_consigneecountry_desc() {
//        sisObj.sanitizeInput(ebook_consigneecountry_desc);
        return Validation.checkNull(ebook_consigneecountry_desc);
    }

    public void setEbook_consigneecountry_desc(String ebook_consigneecountry_desc) {
        this.ebook_consigneecountry_desc = ebook_consigneecountry_desc;
    }

    public String getEbook_notifycountry_desc() {
//        sisObj.sanitizeInput(ebook_notifycountry_desc);
        return Validation.checkNull(ebook_notifycountry_desc);
    }

    public void setEbook_notifycountry_desc(String ebook_notifycountry_desc) {
        this.ebook_notifycountry_desc = ebook_notifycountry_desc;
    }

    public String getEbook_hazardous_desc() {
//        sisObj.sanitizeInput(ebook_hazardous_desc);
        return Validation.checkNull(ebook_hazardous_desc);
    }

    public void setEbook_hazardous_desc(String ebook_hazardous_desc) {
        this.ebook_hazardous_desc = ebook_hazardous_desc;
    }

    public String getEbook_drycargo_desc() {
//        sisObj.sanitizeInput(ebook_drycargo_desc);
        return Validation.checkNull(ebook_drycargo_desc);
    }

    public void setEbook_drycargo_desc(String ebook_drycargo_desc) {
        this.ebook_drycargo_desc = ebook_drycargo_desc;
    }

    public String getEbook_reefercargo_desc() {
//        sisObj.sanitizeInput(ebook_reefercargo_desc);
        return Validation.checkNull(ebook_reefercargo_desc);
    }

    public void setEbook_reefercargo_desc(String ebook_reefercargo_desc) {
        this.ebook_reefercargo_desc = ebook_reefercargo_desc;
    }

    public String getCont_flag() {
        return Validation.checkNull(cont_flag);
    }

    public void setCont_flag(String cont_flag) {
        this.cont_flag = cont_flag;
    }

    public String getEbook_shippercity_hdn() {
        return Validation.checkNull(ebook_shippercity_hdn);
    }

    public void setEbook_shippercity_hdn(String ebook_shippercity_hdn) {
        this.ebook_shippercity_hdn = ebook_shippercity_hdn;
    }

    public String getEbook_consigneecity_hdn() {
        return Validation.checkNull(ebook_consigneecity_hdn);
    }

    public void setEbook_consigneecity_hdn(String ebook_consigneecity_hdn) {
        this.ebook_consigneecity_hdn = ebook_consigneecity_hdn;
    }

    public String getEbook_notifycity_hdn() {
        return Validation.checkNull(ebook_notifycity_hdn);
    }

    public void setEbook_notifycity_hdn(String ebook_notifycity_hdn) {
        this.ebook_notifycity_hdn = ebook_notifycity_hdn;
    }

    public String getEbook_equiptype() {
        return Validation.checkNull(ebook_equiptype);
    }

    public void setEbook_equiptype(String ebook_equiptype) {
        this.ebook_equiptype = ebook_equiptype;
    }

    public String getEbook_equipsize() {
        return Validation.checkNull(ebook_equipsize);
    }

    public void setEbook_equipsize(String ebook_equipsize) {
        this.ebook_equipsize = ebook_equipsize;
    }

    public String getEbook_equipqty() {
        return Validation.checkNull(ebook_equipqty);
    }

    public void setEbook_equipqty(String ebook_equipqty) {
        this.ebook_equipqty = ebook_equipqty;
    }

    public String getEbook_bookagency_desc() {
//        sisObj.sanitizeInput(ebook_bookagency_desc);
        return Validation.checkNull(ebook_bookagency_desc);
    }

    public void setEbook_bookagency_desc(String ebook_bookagency_desc) {
        this.ebook_bookagency_desc = ebook_bookagency_desc;
    }

    public String getEbook_shippercity_desc() {
//        sisObj.sanitizeInput(ebook_shippercity_desc);
        return Validation.checkNull(ebook_shippercity_desc);
    }

    public void setEbook_shippercity_desc(String ebook_shippercity_desc) {
        this.ebook_shippercity_desc = ebook_shippercity_desc;
    }

    public String getEbook_consigneecity_desc() {
//        sisObj.sanitizeInput(ebook_consigneecity_desc);
        return Validation.checkNull(ebook_consigneecity_desc);
    }

    public void setEbook_consigneecity_desc(String ebook_consigneecity_desc) {
        this.ebook_consigneecity_desc = ebook_consigneecity_desc;
    }

    public String getEbook_notifycity_desc() {
//        sisObj.sanitizeInput(ebook_notifycity_desc);
        return Validation.checkNull(ebook_notifycity_desc);
    }

    public void setEbook_notifycity_desc(String ebook_notifycity_desc) {
        this.ebook_notifycity_desc = ebook_notifycity_desc;
    }

    public String getEbook_date_hdn() {
        return Validation.checkNull(ebook_date_hdn);
    }

    public void setEbook_date_hdn(String ebook_date_hdn) {
        this.ebook_date_hdn = ebook_date_hdn;
    }

    public String getEbook_pol_hdn() {
        return Validation.checkNull(ebook_pol_hdn);
    }

    public void setEbook_pol_hdn(String ebook_pol_hdn) {
        this.ebook_pol_hdn = ebook_pol_hdn;
    }

    public String getEbook_pod_hdn() {
        return Validation.checkNull(ebook_pod_hdn);
    }

    public void setEbook_pod_hdn(String ebook_pod_hdn) {
        this.ebook_pod_hdn = ebook_pod_hdn;
    }

    public String getEbook_origin_hdn() {
        return Validation.checkNull(ebook_origin_hdn);
    }

    public void setEbook_origin_hdn(String ebook_origin_hdn) {
        this.ebook_origin_hdn = ebook_origin_hdn;
    }

    public String getEbook_delivery_hdn() {
        return Validation.checkNull(ebook_delivery_hdn);
    }

    public void setEbook_delivery_hdn(String ebook_delivery_hdn) {
        this.ebook_delivery_hdn = ebook_delivery_hdn;
    }

    public String getEbook_shippercountry_name() {
        return Validation.checkNull(ebook_shippercountry_name);
    }

    public void setEbook_shippercountry_name(String ebook_shippercountry_name) {
        this.ebook_shippercountry_name = ebook_shippercountry_name;
    }

    public String getEbook_consigneecountry_name() {
        return Validation.checkNull(ebook_consigneecountry_name);
    }

    public void setEbook_consigneecountry_name(String ebook_consigneecountry_name) {
        this.ebook_consigneecountry_name = ebook_consigneecountry_name;
    }

    public String getEbook_notifycountry_name() {
        return Validation.checkNull(ebook_notifycountry_name);
    }

    public void setEbook_notifycountry_name(String ebook_notifycountry_name) {
        this.ebook_notifycountry_name = ebook_notifycountry_name;
    }

    public void setEmptyPickup(String emptyPickup) {
        this.emptyPickup = emptyPickup;
    }

    public String getEmptyPickup() {
        return Validation.checkNull(emptyPickup);
    }

    public void setInTransit(String inTransit) {
        this.inTransit = inTransit;
    }

    public String getInTransit() {
        return Validation.checkNull(inTransit);
    }
    //typeofMovementcode   typeofMovement

    public void setTypeofMovementcode(String typeofMovementcode) {
        this.typeofMovementcode = typeofMovementcode;
    }

    public String getTypeofMovementcode() {
        return Validation.checkNull(typeofMovementcode);
    }

    public void setTypeofMovement(String typeofMovement) {
        this.typeofMovement = typeofMovement;
    }

    public String getTypeofMovement() {
        return Validation.checkNull(typeofMovement);
    }
    // carriageTermCode   carriageTerms

    public void setCarriageTermCode(String carriageTermCode) {
        this.carriageTermCode = carriageTermCode;
    }

    public String getCarriageTermCode() {
        return Validation.checkNull(carriageTermCode);
    }

    public void setCarriageTerms(String carriageTerms) {
        this.carriageTerms = carriageTerms;
    }

    public String getCarriageTerms() {
        return Validation.checkNull(carriageTerms);
    }
    //preCarriage  onCarriage

    public void setPreCarriage(String preCarriage) {
        this.preCarriage = preCarriage;
    }

    public String getPreCarriage() {
        return Validation.checkNull(preCarriage);
    }

    public void setOnCarriage(String onCarriage) {
        this.onCarriage = onCarriage;
    }

    public String getOnCarriage() {
        return Validation.checkNull(onCarriage);
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getSoc() {
        return Validation.checkNull(soc);
    }

    public String getEbook_packagecode() {
        return Validation.checkNull(ebook_packagecode);
    }

    public void setEbook_packagecode(String ebook_packagecode) {
        this.ebook_packagecode = ebook_packagecode;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        HttpSession session = request.getSession();
        ServletContext contexts = servlet.getServletContext();
        String type = Validation.checkNull((String) session.getAttribute("type"));
        System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>engered in amendment reset>>type>>" + type);
        String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
        Hashtable country = new Hashtable();
        Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
        country = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
        setEbook_shipterm_desc(getEbook_shipment());
        ArrayList masterlist = (ArrayList) session.getAttribute("BKGAMENDMENTMASTERDETAILS");
        ENSDeclarantVo ics = (ENSDeclarantVo) session.getAttribute("Amendment_EnsDeclarant_ICS");
        String Enable_preferAgency = (String) contexts.getAttribute(GeneralConf.Enable_PreferredAgency);
        String hidefaxno = (String) contexts.getAttribute(GeneralConf.HideFaxNo);
        session.setAttribute("BKGAMENDMENTMASTERDETAILS", null);
        session.setAttribute("Amendment_EnsDeclarant_ICS", null);
        System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>>masterlist>>" + masterlist);
        if (masterlist != null) {
            int len = masterlist.size();
            System.out.println("[ECOM-WAR] AmendmentBookingFormBean lent--" + len);
            for (int i = 0; i < len; i++) {
                net.ecom.vo.AmendmentBookingFormBean ebookVo = (net.ecom.vo.AmendmentBookingFormBean) masterlist.get(i);
                setBooking_refno(ebookVo.getBooking_refno());
                setHidbkgtype(ebookVo.getHidbkgtype());
                System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>>ebookVo.getEbook_date()>>" + ebookVo.getEbook_date());
                setEbook_date(DateConversion_New.convertDefaultFormat(ebookVo.getEbook_date()));
                setFilename1(ebookVo.getFilename1());
                setFilename2(ebookVo.getFilename2());
                setFilename3(ebookVo.getFilename3());
                setFilename4(ebookVo.getFilename4());
                setFilename5(ebookVo.getFilename5());
                if (ebookVo.getEbook_ocean_freight() != null && ebookVo.getEbook_ocean_freight().length() > 0) {
                    setEbook_ocean_freight(ebookVo.getEbook_ocean_freight());
                } else {
                    setEbook_ocean_freight("P");
                }
                setHidETDDate(DateConversion_New.convertDefaultFormat(ebookVo.getEbook_date()));
                setCarrierContact(ebookVo.getCarrierContact());
                setEbook_pickup_point(ebookVo.getEbook_pickup_point());
                setEmptyPickup(ebookVo.getEmptyPickup());
                setEmptyPickupHid(ebookVo.getEmptyPickup());
                setPodintransit(ebookVo.getPodintransit());
                setCcMailId(ebookVo.getCcMailId());
                setEbook_haulercode(ebookVo.getEbook_haulercode());
                setHidpodintransit(ebookVo.getPodintransit());
                setEmptyPickUpDate(DateConversion_New.convertDefaultFormatTime(ebookVo.getEmptyPickUpDate()));
                setEmptyPickup(ebookVo.getEmptyPickup());
                setEmptyPickupHid(ebookVo.getEmptyPickup());
                setSaveStatus(ebookVo.getSaveStatus());
                if ("R".equalsIgnoreCase(ebookVo.getSaveStatus())) {
                    setEbkg_type("R");
                } else {
                    setEbkg_type("");
                }
                setEbook_haulagetype(ebookVo.getEbook_haulagetype());
                if (ebookVo.getEbook_pickup_date() != null && ebookVo.getEbook_pickup_date().length() > 0) {
                    setEbook_pickup_date(DateConversion_New.convertDefaultFormat(ebookVo.getEbook_pickup_date()));
                } else {
                    setEbook_pickup_date("");
                }
                if (ebookVo.getEbook_drop_date() != null && ebookVo.getEbook_drop_date().length() > 0) {
                    setEbook_drop_date(DateConversion_New.convertDefaultFormat(ebookVo.getEbook_drop_date()));
                } else {
                    setEbook_drop_date("");
                }
                setSalesRateAgree(ebookVo.getSalesRateAgree());
                setLrpBookNo(ebookVo.getLrpBookNo());
                setAppVersion(ebookVo.getAppVersion());
                System.out.println("[ECOM-WAR] AmendmentBookingFormBean ebookVo.getAppVersion()=== form ==> " + ebookVo.getAppVersion());
                setEmptyPickUpDate(DateConversion_New.convertDefaultFormatTime(ebookVo.getEmptyPickUpDate()));
                setEbook_portofloading(ebookVo.getEbook_portofloading());
                setEbook_portofdischarge(ebookVo.getEbook_portofdischarge());
                setEbook_bookagency(ebookVo.getEbook_bookagency());
                setEbook_bookagency_hdn(ebookVo.getEbook_bookagency());
                setEbook_servicecode(ebookVo.getEbook_servicecode());
                setEbooking_service(ebookVo.getEbooking_service());
                setEbook_lineservicecode(ebookVo.getEbook_lineservicecode());
                setEbook_lineservice(ebookVo.getEbook_lineservice());
                setEbook_vesselcode(ebookVo.getEbook_vesselcode());
                setEbooking_vessel(ebookVo.getEbooking_vessel());
                setEbook_linevesselcode(ebookVo.getEbook_linevesselcode());
                setEbook_linevessel(ebookVo.getEbook_linevessel());
                setEbook_voyage(ebookVo.getEbook_voyage());
                setEbook_bound_leg(ebookVo.getEbook_bound_leg());
                setEbook_bound(ebookVo.getEbook_bound());
                setEbook_linevoyage(ebookVo.getEbook_linevoyage());
                setEbook_linebound_leg(ebookVo.getEbook_linebound_leg());
                setEbook_origin(ebookVo.getEbook_origin());
                setEbook_origin_country(ebookVo.getEbook_origin_country());
                setEbook_pol(ebookVo.getEbook_pol());
                setEbook_pol_country(ebookVo.getEbook_pol_country());
                setEbook_terminal(ebookVo.getEbook_terminal());
                setEbook_custpo(ebookVo.getEbook_custpo());
                setEbook_exportref(ebookVo.getEbook_exportref());
                setCustomerreference(ebookVo.getCustomerreference());
                setACID(ebookVo.getACID());
                setCargoprotectid(ebookVo.getCargoprotectid());
                setShipperReference(Validation.checkNull(ebookVo.getShipperReference()));
                setShipperReferenceType(Validation.checkNull(ebookVo.getShipperReferenceType()));

                if (ebookVo.getEbook_reftype() != null && ebookVo.getEbook_reftype().length() > 0) {
                    setEbook_reftype(ebookVo.getEbook_reftype());
                } else {
                    setEbook_reftype("Normal");
                }

                setEbook_terminal_country(ebookVo.getEbook_terminal_country());
                if (ebookVo.getEbook_pol_etd() != null && ebookVo.getEbook_pol_etd().length() > 0 && !ebookVo.getEbook_pol_etd().equalsIgnoreCase("-")) {
                    setEbook_pol_etd(DateConversion_New.convertDefaultTimeFormat(ebookVo.getEbook_pol_etd()));
                } else {
                    setEbook_pol_etd("");
                }
                setEbook_pod(ebookVo.getEbook_pod());
                setEbook_pod_country(ebookVo.getEbook_pod_country());
                setEbook_terminalpod(ebookVo.getEbook_terminalpod());
                setEbook_terminalpod_country(ebookVo.getEbook_terminalpod_country());
                if (ebookVo.getEbook_pol_eta() != null && ebookVo.getEbook_pol_eta().length() > 0 && !ebookVo.getEbook_pol_eta().equalsIgnoreCase("-")) {
                    setEbook_pol_eta(DateConversion_New.convertDefaultTimeFormat(ebookVo.getEbook_pol_eta()));
                } else {
                    setEbook_pol_eta("");
                }

                setEbook_delivery(ebookVo.getEbook_delivery());
                setEbook_delivery_country(ebookVo.getEbook_delivery_country());
                setEbook_shipment(ebookVo.getEbook_shipment());
                setEbook_shipterm_desc(ebookVo.getEbook_shipment());
                setEbook_rate_reference(ebookVo.getEbook_rate_reference());
                setEbook_agency_reference(ebookVo.getEbook_agency_reference());

                setEbook_portofloading_desc(ebookVo.getEbook_portofloading_desc());
                setEbook_portofdischarge_desc(ebookVo.getEbook_portofdischarge_desc());
                if ("reuse".equalsIgnoreCase(type)) {
                    setEbook_status_remarks("");
                } else {
                    setEbook_status_remarks(ebookVo.getEbook_status_remarks());
                }
                setEbook_userstatus(ebookVo.getEbook_userstatus());
                setEbook_date_hdn(ebookVo.getEbook_date());
                setEbook_pol_hdn(ebookVo.getEbook_portofloading());
                setEbook_pod_hdn(ebookVo.getEbook_portofdischarge());
                setEbook_origin_hdn(ebookVo.getEbook_origin());
                setEbook_delivery_hdn(ebookVo.getEbook_delivery());
                setEmptyPickup(ebookVo.getEmptyPickup());
                setServicehdedit(ebookVo.getTrpservice());
                setPreCarriage(ebookVo.getPreCarriage());
                setOnCarriage(ebookVo.getOnCarriage());
                setEnsfiling(ebookVo.getEnsfiling());
                setEbook_t2lfiling(ebookVo.getEbook_t2lfiling());
                setEbook_EnsDocType(ebookVo.getEbook_EnsDocType());
                if ("Y".equalsIgnoreCase(Enable_preferAgency)) {
                    setEbook_preferred_agency(ebookVo.getEbook_preferred_agency());
                }
                setEbook_sisubmit_party(ebookVo.getEbook_sisubmit_party());
                setTrpservice(ebookVo.getTrpservice());
                //route details
                setRoutedeliveryname(Validation.checkNull((String) portlist.get(Validation.checkNull(ebookVo.getEbook_delivery()).toUpperCase())));
                setRouteoriginname(Validation.checkNull((String) portlist.get(Validation.checkNull(ebookVo.getEbook_origin()).toUpperCase())));
                setRoutepodname(Validation.checkNull((String) portlist.get(Validation.checkNull(ebookVo.getEbook_portofdischarge()).toUpperCase())));
                setRoutepolname(Validation.checkNull((String) portlist.get(Validation.checkNull(ebookVo.getEbook_portofloading()).toUpperCase())));
                setRoutepodter(ebookVo.getEbook_terminalpod());
                setRoutepolter(ebookVo.getEbook_terminal());
                setEbook_bound_leg(ebookVo.getEbook_bound_leg());
                setHidden_empty(ebookVo.getEmptyPickup());
                setGroupcallid(ebookVo.getGroupcallid());
                setEbook_routeid(ebookVo.getEbook_routeid());
                setDocumentalagencyid(ebookVo.getDocumentalagencyid());//sasi
                setAgencyissueid(ebookVo.getAgencyissueid());
                setAgencyid(ebookVo.getAgencyid());
                setEbook_natcode(ebookVo.getEbook_natcode());
                setEbook_natcode(ebookVo.getEbook_natcode());//To set the natcode-sasi
                System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>routeid>>" + ebookVo.getEbook_routeid());
                request.setAttribute("userstatus", ebookVo.getEbook_userstatus());
                //Customer Details
                if (ebookVo.getEbook_shippername().length() > 0) {
                    setEbook_shippercity(ebookVo.getEbook_shippercity());

                    setEbook_shippercity_desc(ebookVo.getEbook_shippercity());
                    setEbook_shippercountry(ebookVo.getEbook_shippercountry());

                    setEbook_shippercountry_name(ebookVo.getEbook_shippercountry() + " - " + ((String) country.get(ebookVo.getEbook_shippercountry())).toUpperCase());
                    setEbook_shippername(ebookVo.getEbook_shippername());
                    setEbook_shipperaddress(ebookVo.getEbook_shipperaddress());
                    setEbook_shipperaddress1(ebookVo.getEbook_shipperaddress1());
                    setEbook_shipperphone(ebookVo.getEbook_shipperphone());
                    setEbook_shipperphone1(ebookVo.getEbook_shipperphone1());
                    setEbook_shipperphone2(ebookVo.getEbook_shipperphone2());
                    setEbook_shipperphone3(ebookVo.getEbook_shipperphone3());
                    setShipperCheck(ebookVo.getShipperCheck());
                    setEbook_shipperfax1(ebookVo.getEbook_shipperfax1());
                    setEbook_shipperfax2(ebookVo.getEbook_shipperfax2());
                    setEbook_shipperfax3(ebookVo.getEbook_shipperfax3());
                    setEbook_shipperemail(ebookVo.getEbook_shipperemail());
                    setEbook_shipperstate(ebookVo.getEbook_shipperstate());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_shipperfax("");
                    } else {
                        setEbook_shipperfax(ebookVo.getEbook_shipperfax());
                    }
                    setEbook_shippercity_hdn(ebookVo.getEbook_shippercity());
                    setEbook_shippercode(ebookVo.getEbook_shippercode());
                    setEbook_shipper_id(ebookVo.getEbook_shipper_id());
                    setEbook_shippereori(ebookVo.getEbook_shippereori());
                    setShippertypeperson(ebookVo.getShippertypeperson());
                    setEbook_shipperzipcode(ebookVo.getEbook_shipperzipcode());

                }
                if (ebookVo.getEbook_bookingname().length() > 0) {
                    setEbook_bookingparty_desc(ebookVo.getEbook_bookingname());
                    setEbook_bookingcity(ebookVo.getEbook_bookingcity());
                    setEbook_bookingcity_desc(ebookVo.getEbook_bookingcity());
                    setEbook_bookingcountry(ebookVo.getEbook_bookingcountry());
                    setEbook_bookingcountry_name(ebookVo.getEbook_bookingcountry() + " - " + (Validation.checkNull(((String) country.get(Validation.checkNull(ebookVo.getEbook_bookingcountry())))).toUpperCase()));
                    setEbook_bookingname(ebookVo.getEbook_bookingname());
                    setEbook_bookingaddress(ebookVo.getEbook_bookingaddress());
                    setEbook_bookingaddress1(ebookVo.getEbook_bookingaddress1());
                    setEbook_bookingphone(ebookVo.getEbook_bookingphone());
                    setEbook_bookingemail(ebookVo.getEbook_bookingemail());
                    setEbook_bookingstate(ebookVo.getEbook_bookingstate());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_bookingfax("");
                    } else {
                        setEbook_bookingfax(ebookVo.getEbook_bookingfax());
                    }
                    setEbook_bookingcode(ebookVo.getEbook_bookingcode());
                    setEbook_bookingzipcode(ebookVo.getEbook_bookingzipcode());
                    setEbook_bookingeori(ebookVo.getEbook_bookingeori());
                    setBookingtypeperson(ebookVo.getBookingtypeperson());
                } else {
                    setEbook_bookingcity(ebookVo.getEbook_agreecity());
                    setEbook_bookingcity_desc(ebookVo.getEbook_agreecity());
                    setEbook_bookingcountry(ebookVo.getEbook_agreecountry());
                    setEbook_bookingcountry_name(ebookVo.getEbook_agreecountry() + " - " + (Validation.checkNull(((String) country.get(Validation.checkNull(ebookVo.getEbook_agreecountry())))).toUpperCase()));
                    setEbook_bookingname(ebookVo.getEbook_agreename());
                    setEbook_bookingaddress(ebookVo.getEbook_agreeaddress());
                    setEbook_bookingaddress1(ebookVo.getEbook_agreeaddress1());
                    setEbook_bookingphone(ebookVo.getEbook_agreephone());
                    setEbook_bookingemail(ebookVo.getEbook_agreeemail());
                    setEbook_bookingstate(ebookVo.getEbook_agreestate());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_bookingfax("");
                    } else {
                        setEbook_bookingfax(ebookVo.getEbook_agreefax());
                    }

                    setEbook_bookingcode(ebookVo.getEbook_agreecode());
                    setEbook_bookingzipcode(ebookVo.getEbook_agreezipcode());
                    setEbook_bookingeori(ebookVo.getEbook_bookingeori());
                    setBookingtypeperson(ebookVo.getBookingtypeperson());
                }
                if (ebookVo.getEbook_agreename().length() > 0) {
                    setEbook_bookingparty_desc(ebookVo.getEbook_agreename());
                    setEbook_agreecity(ebookVo.getEbook_agreecity());
                    setEbook_agreecity_desc(ebookVo.getEbook_agreecity());
                    setEbook_agreecountry(ebookVo.getEbook_agreecountry());
                    setEbook_agreecountry_name(ebookVo.getEbook_agreecountry() + " - " + (Validation.checkNull(((String) country.get(Validation.checkNull(ebookVo.getEbook_agreecountry())))).toUpperCase()));
                    setEbook_agreename(ebookVo.getEbook_agreename());
                    setEbook_agreeaddress(ebookVo.getEbook_agreeaddress());
                    setEbook_agreeaddress1(ebookVo.getEbook_agreeaddress1());
                    setEbook_agreephone(ebookVo.getEbook_agreephone());
                    setEbook_agreeemail(ebookVo.getEbook_agreeemail());
                    setEbook_agreestate(ebookVo.getEbook_agreestate());
                    setEbook_agreeeori(ebookVo.getEbook_agreeeori());
                    setAgreetypeperson(ebookVo.getAgreetypeperson());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_agreefax("");
                    } else {
                        setEbook_agreefax(ebookVo.getEbook_agreefax());
                    }

                    setEbook_agreecity_hdn(ebookVo.getEbook_agreecity());
                    setEbook_agreecode(ebookVo.getEbook_agreecode());
                    setEbook_agree_id(ebookVo.getEbook_agree_id());
                    setEbook_agreezipcode(ebookVo.getEbook_agreezipcode());
                }
                if (ebookVo.getEbook_consigneename().length() > 0) {
                    setEbook_consigneecity(ebookVo.getEbook_consigneecity());
                    setEbook_consigneecity_desc(ebookVo.getEbook_consigneecity());
                    setEbook_consigneecountry(ebookVo.getEbook_consigneecountry());
                    if (ebookVo.getEbook_consigneecountry() != null && ebookVo.getEbook_consigneecountry().length() > 0) {
                        setEbook_consigneecountry_name(ebookVo.getEbook_consigneecountry() + " - " + (Validation.checkNull((String) country.get(ebookVo.getEbook_consigneecountry()))).toUpperCase());
                    } else {
                        setEbook_consigneecountry_name("");
                    }
                    setHidCNToOrder(ebookVo.getHidCNToOrder());
                    setEbook_consigneename(ebookVo.getEbook_consigneename());
                    setEbook_consigneeaddress(ebookVo.getEbook_consigneeaddress());
                    setEbook_consigneeaddress1(ebookVo.getEbook_consigneeaddress1());
                    setEbook_consigneephone(ebookVo.getEbook_consigneephone());
                    setEbook_consigneephone1(ebookVo.getEbook_consigneephone1());
                    setEbook_consigneephone2(ebookVo.getEbook_consigneephone2());
                    setEbook_consigneephone3(ebookVo.getEbook_consigneephone3());
                    setEbook_consigneefax1(ebookVo.getEbook_consigneefax1());
                    setEbook_consigneefax2(ebookVo.getEbook_consigneefax2());
                    setEbook_consigneefax3(ebookVo.getEbook_consigneefax3());
                    setEbook_consigneeemail(ebookVo.getEbook_consigneeemail());
                    setEbook_consigneestate(ebookVo.getEbook_consigneestate());
                    setEbook_consigneeeori(ebookVo.getEbook_consigneeeori());
                    setConsigneetypeperson(ebookVo.getConsigneetypeperson());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_consigneefax("");
                    } else {
                        setEbook_consigneefax(ebookVo.getEbook_consigneefax());
                    }

                    setEbook_consigneecity_hdn(ebookVo.getEbook_consigneecity());
                    setEbook_consigneecode(ebookVo.getEbook_consigneecode());
                    setEbook_consigne_id(ebookVo.getEbook_consigne_id());
                    setEbook_consigneezipcode(ebookVo.getEbook_consigneezipcode());

                }

//                if ( "Y".equalsIgnoreCase(ebookVo.getEbook_notifychk())) {
                if (ebookVo.getEbook_notifyname().length() > 0) {
                    setEbook_notifychk(ebookVo.getEbook_notifychk());
                    setEbook_notifycity(ebookVo.getEbook_notifycity());

                    setEbook_notifycity_desc(ebookVo.getEbook_notifycity());
                    setEbook_notifycountry(ebookVo.getEbook_notifycountry());
                    if (ebookVo.getEbook_notifycountry() != null && ebookVo.getEbook_notifycountry().length() > 0) {
                        setEbook_notifycountry_name(ebookVo.getEbook_notifycountry() + " - " + (Validation.checkNull((String) country.get(ebookVo.getEbook_notifycountry())).toUpperCase()));
                    } else {
                        setEbook_notifycountry_name("");
                    }
                    setHidNTToOrder(ebookVo.getHidNTToOrder());
                    setEbook_notifyname(ebookVo.getEbook_notifyname());
                    setEbook_notifyaddress(ebookVo.getEbook_notifyaddress());
                    setEbook_notifyaddress1(ebookVo.getEbook_notifyaddress1());
                    setEbook_notifyphone(ebookVo.getEbook_notifyphone());
                    setEbook_notifyphone1(ebookVo.getEbook_notifyphone1());
                    setEbook_notifyphone2(ebookVo.getEbook_notifyphone2());
                    setEbook_notifyphone3(ebookVo.getEbook_notifyphone3());

                    setEbook_notifyfax1(ebookVo.getEbook_notifyfax1());
                    setEbook_notifyfax2(ebookVo.getEbook_notifyfax2());
                    setEbook_notifyfax3(ebookVo.getEbook_notifyfax3());
                    setEbook_notifyemail(ebookVo.getEbook_notifyemail());
                    setEbook_notifystate(ebookVo.getEbook_notifystate());
                    setEbook_notifyeori(ebookVo.getEbook_notifyeori());
                    setNotifytypeperson(ebookVo.getNotifytypeperson());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_notifyfax("");
                    } else {
                        setEbook_notifyfax(ebookVo.getEbook_notifyfax());
                    }

                    setEbook_notifycity_hdn(ebookVo.getEbook_notifycity());
                    setEbook_notifycode(ebookVo.getEbook_notifycode());
                    setEbook_notifyzipcode(ebookVo.getEbook_notifyzipcode());
                    setEbook_notifycode(ebookVo.getEbook_notifycode());
                    setEbook_notify_id(ebookVo.getEbook_notify_id());
                    setNotifyCheck(ebookVo.getNotifyCheck());

                    //System.out.println("[ECOM-WAR] AmendmentBookingFormBean ................");
                } else {
//                    setEbook_notifychk("N");
                    setEbook_notifyname("");
                    setEbook_notifyaddress("");
                    setEbook_notifyaddress1("");
                    setEbook_notifycountry("");
                    setEbook_notifycountry_desc("");
                    setEbook_notifycity("");
                    setEbook_notifycode("");
                    setEbook_notifycity_desc("");
                    setEbook_notifyphone("");
                    setEbook_notifyfax("");
                    setEbook_notifyemail("");
                    setEbook_notifystate("");
                    setEbook_notifyeori("");
                    setNotifytypeperson("");
                    setEbook_notifyzipcode("");
                }

                // Notify1
                if (ebookVo.getEbook_notifyname_n1().length() > 0) {
                    setEbook_notifychk_n1(ebookVo.getEbook_notifychk_n1());
                    setEbook_notifycity_n1(ebookVo.getEbook_notifycity_n1());

                    setEbook_notifycity_desc_n1(ebookVo.getEbook_notifycity_n1());
                    setEbook_notifycountry_n1(ebookVo.getEbook_notifycountry_n1());
                    if (ebookVo.getEbook_notifycountry_n1() != null && ebookVo.getEbook_notifycountry_n1().length() > 0) {
                        String notifycountrycode = (ebookVo.getEbook_notifycountry_n1() != null) ? ebookVo.getEbook_notifycountry_n1().trim() : "";
                        String notifycountry1 = Validation.checkNull(((String) country.get(notifycountrycode))).toUpperCase();
                        setEbook_notifycountry_name_n1(notifycountrycode + " - " + notifycountry1);
                    } else {
                        setEbook_notifycountry_name_n1("");
                    }
                    setHidNT1ToOrder(ebookVo.getHidNT1ToOrder());
                    setEbook_notifyname_n1(ebookVo.getEbook_notifyname_n1());
                    setEbook_notifyaddress_n1(ebookVo.getEbook_notifyaddress_n1());
                    setEbook_notifyaddress1_n1(ebookVo.getEbook_notifyaddress1_n1());
                    setEbook_notifyphone_n1(ebookVo.getEbook_notifyphone_n1());
                    setEbook_notifyphone1_n1(ebookVo.getEbook_notifyphone1_n1());
                    setEbook_notifyphone2_n1(ebookVo.getEbook_notifyphone2_n1());
                    setEbook_notifyphone3_n1(ebookVo.getEbook_notifyphone3_n1());
                    setEbook_notifyemail_n1(ebookVo.getEbook_notifyemail_n1());
                    setEbook_notifystate_n1(ebookVo.getEbook_notifystate_n1());
                    setEbook_notifyeori_n1(ebookVo.getEbook_notifyeori_n1());
                    setNotifytypeperson_n1(ebookVo.getNotifytypeperson_n1());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_notifyfax_n1("");
                    } else {
                        setEbook_notifyfax_n1(ebookVo.getEbook_notifyfax_n1());
                    }

                    setEbook_notifycity_hdn_n1(ebookVo.getEbook_notifycity_n1());
                    setEbook_notifycode_n1(ebookVo.getEbook_notifycode_n1());
                    setEbook_notifyzipcode_n1(ebookVo.getEbook_notifyzipcode_n1());
                    setEbook_notifycode_n1(ebookVo.getEbook_notifycode_n1());
                    setEbook_notify_id_n1(ebookVo.getEbook_notify_id_n1());
                    setNotify1Check(ebookVo.getNotify1Check());
                } else {
//                    setEbook_notifychk("N");
                    setEbook_notifyname_n1("");
                    setEbook_notifyaddress_n1("");
                    setEbook_notifyaddress1_n1("");
                    setEbook_notifycountry_n1("");
                    setEbook_notifycountry_desc_n1("");
                    setEbook_notifycity_n1("");
                    setEbook_notifycode_n1("");
                    setEbook_notifycity_desc_n1("");
                    setEbook_notifyphone_n1("");
                    setEbook_notifyfax_n1("");
                    setEbook_notifyemail_n1("");
                    setEbook_notifystate_n1("");
                    setEbook_notifyeori_n1("");
                    setNotifytypeperson_n1("");
                    setEbook_notifyzipcode_n1("");
                }
                if (ebookVo.getEbook_forwardername().length() > 0) {
                    setEbook_forwardername(ebookVo.getEbook_forwardername());
                    setEbook_forwardercity(ebookVo.getEbook_forwardercity());
                    setEbook_forwardercity_desc(ebookVo.getEbook_forwardercity());
                    setEbook_forwardercountry(ebookVo.getEbook_forwardercountry());
                    setEbook_forwardercountry_name(ebookVo.getEbook_forwardercountry() + " - " + (Validation.checkNull(((String) country.get(Validation.checkNull(ebookVo.getEbook_forwardercountry())))).toUpperCase()));
                    // setEbook_forwardername(ebookVo.getEbook_forwardername());
                    setEbook_forwarderaddress(ebookVo.getEbook_forwarderaddress());
                    setEbook_forwarderaddress1(ebookVo.getEbook_forwarderaddress1());
                    setEbook_forwarderphone(ebookVo.getEbook_forwarderphone());
                    setEbook_forwarderemail(ebookVo.getEbook_forwarderemail());
                    setEbook_forwarderstate(ebookVo.getEbook_forwarderstate());
                    setEbook_forwardereori(ebookVo.getEbook_forwardereori());
                    setForwardertypeperson(ebookVo.getForwardertypeperson());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_forwarderfax("");
                    } else {
                        setEbook_forwarderfax(ebookVo.getEbook_forwarderfax());
                    }

                    setEbook_forwardercity_hdn(ebookVo.getEbook_forwardercity());
                    setEbook_forwardercode(ebookVo.getEbook_forwardercode());
                    setEbook_forwarder_id(ebookVo.getEbook_forwarder_id());
                    setEbook_forwarderzipcode(ebookVo.getEbook_forwarderzipcode());
                } else {
                    setEbook_forwardername("");
                    setEbook_forwardercity("");
                    setEbook_forwardercity_desc("");
                    setEbook_forwardercountry("");
                    setEbook_forwardercountry_name("");
                    setEbook_forwarderaddress("");
                    setEbook_forwarderaddress1("");
                    setEbook_forwarderphone("");
                    setEbook_forwarderemail("");
                    setEbook_forwarderfax("");
                    setEbook_forwardercity_hdn("");
                    setEbook_forwardercode("");
                    setEbook_forwarder_id("");
                    setEbook_forwarderstate("");
                    setEbook_forwardereori("");
                    setForwardertypeperson("");
                    setEbook_forwarderzipcode("");
                }
                if (ebookVo.getEbook_Warehousename().length() > 0) {
                    setEbook_Warehousename(ebookVo.getEbook_Warehousename());
                    setEbook_Warehousecity(ebookVo.getEbook_Warehousecity());
                    setEbook_Warehousecity_desc(ebookVo.getEbook_Warehousecity());
                    setEbook_Warehousecountry(ebookVo.getEbook_Warehousecountry());
                    setEbook_Warehousecountry_name(ebookVo.getEbook_Warehousecountry() + " - " + (Validation.checkNull(((String) country.get(Validation.checkNull(ebookVo.getEbook_Warehousecountry())))).toUpperCase()));
                    setEbook_Warehousename(ebookVo.getEbook_Warehousename());
                    setEbook_Warehouseaddress(ebookVo.getEbook_Warehouseaddress());
                    setEbook_Warehouseaddress1(ebookVo.getEbook_Warehouseaddress1());
                    setEbook_Warehousephone(ebookVo.getEbook_Warehousephone());
                    setEbook_Warehouseemail(ebookVo.getEbook_Warehouseemail());
                    setEbook_Warehousestate(ebookVo.getEbook_Warehousestate());
                    setEbook_Warehouseeori(ebookVo.getEbook_Warehouseeori());
                    setWarehousetypeperson(ebookVo.getWarehousetypeperson());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_Warehousefax("");
                    } else {
                        setEbook_Warehousefax(ebookVo.getEbook_Warehousefax());
                    }

                    setEbook_Warehousecity_hdn(ebookVo.getEbook_Warehousecity());
                    setEbook_Warehousecode(ebookVo.getEbook_Warehousecode());
                    setEbook_Warehouse_id(ebookVo.getEbook_Warehouse_id());
                    setEbook_Warehousezipcode(ebookVo.getEbook_Warehousezipcode());
                } else {
                    setEbook_Warehousename("");
                    setEbook_Warehousecity("");
                    setEbook_Warehousecity_desc("");
                    setEbook_Warehousecountry("");
                    setEbook_Warehousecountry_name("");
                    setEbook_Warehousename("");
                    setEbook_Warehouseaddress("");
                    setEbook_Warehouseaddress1("");
                    setEbook_Warehousephone("");
                    setEbook_Warehouseemail("");
                    setEbook_Warehousefax("");
                    setEbook_Warehousecity_hdn("");
                    setEbook_Warehousecode("");
                    setEbook_Warehouse_id("");
                    setEbook_Warehousestate("");
                    setEbook_Warehouseeori("");
                    setWarehousetypeperson("");
                    setEbook_Warehousezipcode("");
                }

                if (ebookVo.getEbook_NamedCustomername().length() > 0) {
                    setEbook_NamedCustomercity(ebookVo.getEbook_NamedCustomercity());
                    setEbook_NamedCustomercity_desc(ebookVo.getEbook_NamedCustomercity());
                    setEbook_NamedCustomercountry(ebookVo.getEbook_NamedCustomercountry());
                    if (ebookVo.getEbook_NamedCustomercountry() != null && ebookVo.getEbook_NamedCustomercountry().length() > 0) {
                        setEbook_NamedCustomercountry_name(ebookVo.getEbook_NamedCustomercountry() + " - " + (Validation.checkNull((String) country.get(ebookVo.getEbook_NamedCustomercountry()))).toUpperCase());
                    } else {
                        setEbook_NamedCustomercountry_name("");
                    }
                    setHidCNToOrder(ebookVo.getHidCNToOrder());
                    setEbook_NamedCustomername(ebookVo.getEbook_NamedCustomername());
                    setEbook_NamedCustomeraddress(ebookVo.getEbook_NamedCustomeraddress());
                    setEbook_NamedCustomeraddress1(ebookVo.getEbook_NamedCustomeraddress1());
                    setEbook_NamedCustomerphone(ebookVo.getEbook_NamedCustomerphone());
                    setEbook_NamedCustomerphone1(ebookVo.getEbook_NamedCustomerphone1());
                    setEbook_NamedCustomerphone2(ebookVo.getEbook_NamedCustomerphone2());
                    setEbook_NamedCustomerphone3(ebookVo.getEbook_NamedCustomerphone3());
                    setEbook_NamedCustomerfax1(ebookVo.getEbook_NamedCustomerfax1());
                    setEbook_NamedCustomerfax2(ebookVo.getEbook_NamedCustomerfax2());
                    setEbook_NamedCustomerfax3(ebookVo.getEbook_NamedCustomerfax3());
                    setEbook_NamedCustomeremail(ebookVo.getEbook_NamedCustomeremail());
                    setEbook_NamedCustomerstate(ebookVo.getEbook_NamedCustomerstate());
                    setEbook_NamedCustomereori(ebookVo.getEbook_NamedCustomereori());
                    setEbook_NamedCustomerpersontype(ebookVo.getEbook_NamedCustomerpersontype());
                    System.out.println("[ECOM-WAR] AmendmentBookingFormBean ebookVo==>" + getEbook_NamedCustomerpersontype() + "ebookVo.getEbook_NamedCustomerpersontype()" + ebookVo.getEbook_NamedCustomerpersontype());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_NamedCustomerfax("");
                    } else {
                        setEbook_NamedCustomerfax(ebookVo.getEbook_NamedCustomerfax());
                    }

                    setEbook_NamedCustomercity_hdn(ebookVo.getEbook_NamedCustomercity());
                    setEbook_NamedCustomercode(ebookVo.getEbook_NamedCustomercode());
                    setEbook_NamedCustomer_id(ebookVo.getEbook_NamedCustomer_id());
                    setEbook_NamedCustomerzipcode(ebookVo.getEbook_NamedCustomerzipcode());

                } else {
                    setEbook_NamedCustomername("");
                    setEbook_NamedCustomercity("");
                    setEbook_NamedCustomercity_desc("");
                    setEbook_NamedCustomercountry("");
                    setEbook_NamedCustomercountry_name("");
                    setEbook_NamedCustomername("");
                    setEbook_NamedCustomeraddress("");
                    setEbook_NamedCustomeraddress1("");
                    setEbook_NamedCustomerphone("");
                    setEbook_NamedCustomeremail("");
                    setEbook_NamedCustomerfax("");
                    setEbook_NamedCustomercity_hdn("");
                    setEbook_NamedCustomercode("");
                    setEbook_NamedCustomer_id("");
                    setEbook_NamedCustomerstate("");
                    setEbook_NamedCustomereori("");
                    setEbook_NamedCustomerpersontype("");
                    setEbook_NamedCustomerzipcode("");
                }
//Seller
                if (ebookVo.getEbook_Sellername().length() > 0) {
                    setEbook_Sellercity(ebookVo.getEbook_Sellercity());
                    setEbook_Sellercity_desc(ebookVo.getEbook_Sellercity());
                    setEbook_Sellercountry(ebookVo.getEbook_Sellercountry());
                    if (ebookVo.getEbook_Sellercountry() != null && ebookVo.getEbook_Sellercountry().length() > 0) {
                        setEbook_Sellercountry_name(ebookVo.getEbook_Sellercountry() + " - " + (Validation.checkNull((String) country.get(ebookVo.getEbook_Sellercountry()))).toUpperCase());
                    } else {
                        setEbook_Sellercountry_name("");
                    }
                    setHidCNToOrder(ebookVo.getHidCNToOrder());
                    setEbook_Sellername(ebookVo.getEbook_Sellername());
                    setEbook_Selleraddress(ebookVo.getEbook_Selleraddress());
                    setEbook_Selleraddress1(ebookVo.getEbook_Selleraddress1());
                    setEbook_Sellerphone(ebookVo.getEbook_Sellerphone());
                    setEbook_Sellerphone1(ebookVo.getEbook_Sellerphone1());
                    setEbook_Sellerphone2(ebookVo.getEbook_Sellerphone2());
                    setEbook_Sellerphone3(ebookVo.getEbook_Sellerphone3());
                    setEbook_Sellerfax1(ebookVo.getEbook_Sellerfax1());
                    setEbook_Sellerfax2(ebookVo.getEbook_Sellerfax2());
                    setEbook_Sellerfax3(ebookVo.getEbook_Sellerfax3());
                    setEbook_Selleremail(ebookVo.getEbook_Selleremail());
                    setEbook_Sellerstate(ebookVo.getEbook_Sellerstate());
                    setEbook_Sellereori(ebookVo.getEbook_Sellereori());
                    setEbook_Sellerpersontype(ebookVo.getEbook_Sellerpersontype());
                    System.out.println("[ECOM-WAR] AmendmentBookingFormBean ebookVo==>" + getEbook_Sellerpersontype() + "ebookVo.getEbook_Sellerpersontype()" + ebookVo.getEbook_Sellerpersontype());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_Sellerfax("");
                    } else {
                        setEbook_Sellerfax(ebookVo.getEbook_Sellerfax());
                    }

                    setEbook_Sellercity_hdn(ebookVo.getEbook_Sellercity());
                    setEbook_Sellercode(ebookVo.getEbook_Sellercode());
                    setEbook_Seller_id(ebookVo.getEbook_Seller_id());
                    setEbook_Sellerzipcode(ebookVo.getEbook_Sellerzipcode());

                } else {
                    setEbook_Sellername("");
                    setEbook_Sellercity("");
                    setEbook_Sellercity_desc("");
                    setEbook_Sellercountry("");
                    setEbook_Sellercountry_name("");
                    setEbook_Sellername("");
                    setEbook_Selleraddress("");
                    setEbook_Selleraddress1("");
                    setEbook_Sellerphone("");
                    setEbook_Selleremail("");
                    setEbook_Sellerfax("");
                    setEbook_Sellercity_hdn("");
                    setEbook_Sellercode("");
                    setEbook_Seller_id("");
                    setEbook_Sellerstate("");
                    setEbook_Sellereori("");
                    setEbook_Sellerpersontype("");
                    setEbook_Sellerzipcode("");
                }
                //Buyer
                if (ebookVo.getEbook_Buyername().length() > 0) {
                    setEbook_Buyercity(ebookVo.getEbook_Buyercity());
                    setEbook_Buyercity_desc(ebookVo.getEbook_Buyercity());
                    setEbook_Buyercountry(ebookVo.getEbook_Buyercountry());
                    if (ebookVo.getEbook_Buyercountry() != null && ebookVo.getEbook_Buyercountry().length() > 0) {
                        setEbook_Buyercountry_name(ebookVo.getEbook_Buyercountry() + " - " + (Validation.checkNull((String) country.get(ebookVo.getEbook_Buyercountry()))).toUpperCase());
                    } else {
                        setEbook_Buyercountry_name("");
                    }
                    setHidCNToOrder(ebookVo.getHidCNToOrder());
                    setEbook_Buyername(ebookVo.getEbook_Buyername());
                    setEbook_Buyeraddress(ebookVo.getEbook_Buyeraddress());
                    setEbook_Buyeraddress1(ebookVo.getEbook_Buyeraddress1());
                    setEbook_Buyerphone(ebookVo.getEbook_Buyerphone());
                    setEbook_Buyerphone1(ebookVo.getEbook_Buyerphone1());
                    setEbook_Buyerphone2(ebookVo.getEbook_Buyerphone2());
                    setEbook_Buyerphone3(ebookVo.getEbook_Buyerphone3());
                    setEbook_Buyerfax1(ebookVo.getEbook_Buyerfax1());
                    setEbook_Buyerfax2(ebookVo.getEbook_Buyerfax2());
                    setEbook_Buyerfax3(ebookVo.getEbook_Buyerfax3());
                    setEbook_Buyeremail(ebookVo.getEbook_Buyeremail());
                    setEbook_Buyerstate(ebookVo.getEbook_Buyerstate());
                    setEbook_Buyereori(ebookVo.getEbook_Buyereori());
                    setEbook_Buyerpersontype(ebookVo.getEbook_Buyerpersontype());
                    System.out.println("[ECOM-WAR] AmendmentBookingFormBean ebookVo==>" + getEbook_Buyerpersontype() + "ebookVo.getEbook_Buyerpersontype()" + ebookVo.getEbook_Buyerpersontype());
                    if (hidefaxno.equalsIgnoreCase("y")) {
                        setEbook_Buyerfax("");
                    } else {
                        setEbook_Buyerfax(ebookVo.getEbook_Buyerfax());
                    }

                    setEbook_Buyercity_hdn(ebookVo.getEbook_Buyercity());
                    setEbook_Buyercode(ebookVo.getEbook_Buyercode());
                    setEbook_Buyer_id(ebookVo.getEbook_Buyer_id());
                    setEbook_Buyerzipcode(ebookVo.getEbook_Buyerzipcode());

                } else {
                    setEbook_Buyername("");
                    setEbook_Buyercity("");
                    setEbook_Buyercity_desc("");
                    setEbook_Buyercountry("");
                    setEbook_Buyercountry_name("");
                    setEbook_Buyername("");
                    setEbook_Buyeraddress("");
                    setEbook_Buyeraddress1("");
                    setEbook_Buyerphone("");
                    setEbook_Buyeremail("");
                    setEbook_Buyerfax("");
                    setEbook_Buyercity_hdn("");
                    setEbook_Buyercode("");
                    setEbook_Buyer_id("");
                    setEbook_Buyerstate("");
                    setEbook_Buyereori("");
                    setEbook_Buyerpersontype("");
                    setEbook_Buyerzipcode("");
                }

                //File name
                setFilename1(ebookVo.getFilename1());
                setFilename2(ebookVo.getFilename2());
                setFilename3(ebookVo.getFilename3());
                setFilename4(ebookVo.getFilename4());
                setFilename5(ebookVo.getFilename5());

                //
                //Container Details
                setEbook_equiptype("");
                setEbook_equipsize("");
                setEbook_equipqty("");
                setEbook_equipment(ebookVo.getEbook_equipment());
                setEbook_equipmentsize(ebookVo.getEbook_equipmentsize());
                setEbook_quantity(ebookVo.getEbook_quantity());

                //Cargo Details
                setEbook_commoditycode("");
                setEbooking_commodity("");
                setEbook_packquantity("");
                setEbook_actCommoName("");
                setEbook_cargoweight("");
                setEbook_drycargo("");
                setEbook_reefercargo("");
                setEbook_settemp("32");
                setEbook_tempunit("C");
                setEbook_hazardous("");
                setEbook_hazardouscontact("");
                setEbook_telephonecontact("");
                setEbook_userstatus("");
                setEbook_templatename("");
                setEbook_cmdtybuyercode("");
                setEbook_cmdtyhblno("");
                setEbook_cmdtysellercode("");
                setEbook_cmdtysellerName("");
                setEbook_cmdtybuyerName("");

                ArrayList route = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");
                System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>rou in beante>>" + route);
                Route eBookingrouting = null;
                //System.out.println("[ECOM-WAR] AmendmentBookingFormBean ");
                String bookdateval = getEbook_date();
                System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>bookdateval>>" + bookdateval);
                if (route != null && route.size() > 0 && bookdateval != null && bookdateval.length() > 0) {
                    Calendar cal = Calendar.getInstance();
                    DateFormat dateFormat = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
                    try {
                        System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>bookdateval>>" + bookdateval);
                        String toDate = dateFormat.format(cal.getTime());
                        System.out.println("[ECOM-WAR] AmendmentBookingFormBean >>toDate>>" + toDate);
                        Date d1 = dateFormat.parse(bookdateval);
                        Date d2 = dateFormat.parse(toDate);
                        if (d1.compareTo(d2) >= 0) {
                            setEbook_masthdn("Y");
                        } else {
                            setEbook_masthdn("N");
                            session.setAttribute("EBOOKING_GETINDEXVALUE", null);
                        }
                        setEbook_date(DateConversion_New.convertDefaultFormat(ebookVo.getEbook_dateBut()));
                    } catch (ParseException ex) {
                        Logger.getLogger(AmendmentBookingFormBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    setEbook_masthdn("N");
                    session.setAttribute("EBOOKING_GETINDEXVALUE", null);
                    setEbook_date(DateConversion_New.convertDefaultFormat(ebookVo.getEbook_dateBut()));
                }
            }
            if (ics != null) {
                setDeclarantName(ics.getDeclarantName());
                setDeclarantAddress(ics.getDeclarantAddress());
                setDeclarantAddress1(ics.getDeclarantAddress1());
                setDeclarantCity(ics.getDeclarantCity());
                setDeclarantCountry(ics.getDeclarantCountry());
                setDeclarantState(ics.getDeclarantState());
                setDeclarantTelephone(ics.getDeclarantTelephone());
                setDeclarantEORI(ics.getDeclarantEORI());
                setDeclarantEmail(ics.getDeclarantEmail());
                setDeclarantFillingType(ics.getDeclarantFillingType());
                setPaymentMethod(ics.getPaymentMethod());
                setBlTypeENS(ics.getBlTypeENS());
                setEnsFillingType(ics.getEnsFillingType());
                setEuCustZone(ics.getEuCustZone());
                setDeclarantZipcode(ics.getDeclarantZipcode());
            }
        }

    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
        EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
        String btn_type = Validation.checkNull(getSubmit_btn());
        ServletContext contexts = servlet.getServletContext();

        setSoc(getHidden_soc());
        try {
            if ("add".equalsIgnoreCase(btn_type) || "edit".equalsIgnoreCase(btn_type)) {
                ArrayList cargolist = (ArrayList) session.getAttribute("ebooking_cargo_grid");
                ArrayList routing_array = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");
                if (getEbook_origin_country().length() > 0) {
                    String portname = getEbook_origin_country();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkports(portname.trim()));
                        if (!(result.length() > 0)) {
                            errors.add("origincountry", new ActionMessage("error.origincountry_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    errors.add("origincountry", new ActionMessage("error.origincountry.add"));
                }
                Route eBookingrouting = null;
                String pol = "";
                String pod = "";
                String ori = "";
                String del = "";
                if (routing_array != null && routing_array.size() > 0) {
                    int len = routing_array.size();
                    for (int k = 0; k < len; k++) {
                        eBookingrouting = (Route) routing_array.get(k);
                        pol = eBookingrouting.getBlpol();
                        pod = eBookingrouting.getBlpod();
                        ori = eBookingrouting.getOrigin();
                        del = eBookingrouting.getDelivery();
                    }
                } //else {
                // errors.add("route_required", new ActionMessage("error.routeRequired"));
                // }

                if (getEbook_delivery_country().length() > 0) {
                    String portname = getEbook_delivery_country();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkports(portname.trim()));
                        if (!(result.length() > 0)) {
                            errors.add("deliverycountry", new ActionMessage("error.deliverycountry_invalid"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    errors.add("deliverycountry", new ActionMessage("error.deliverycountry.add"));
                }

                if (!(getEbook_date().length() > 0)) {
                    errors.add("ebookdate", new ActionMessage("error.ebookdate.add"));
                }

                /**
                 * if (getEbook_shipperphone().length() > 0) { String phone =
                 * getEbook_shipperphone(); if (phone.length() > 0) { if
                 * (!(checkNumeric(phone))) { errors.add("shipperphone", new
                 * ActionMessage("error.shipperphone_invalid")); } if
                 * (!isAllZero(phone)) { errors.add("shipperphone", new
                 * ActionMessage("error.shipperphone_zero")); } if
                 * (!Pattern.matches("^[0-9]{1,5}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{1,10}$",
                 * phone)) { errors.add("shipperphone", new
                 * ActionMessage("error.shipperphone_format")); } } } if
                 * (getEbook_shipperfax().length() > 0) { String fax =
                 * getEbook_shipperfax(); if (fax.length() > 0) { if
                 * (!(checkNumeric(fax))) { errors.add("shipperfax", new
                 * ActionMessage("error.shipperfax_invalid")); } if
                 * (!isAllZero(fax)) { errors.add("shipperfax", new
                 * ActionMessage("error.shipperfax_zero")); } if
                 * (!Pattern.matches("^[0-9]{1,5}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{1,10}$",
                 * fax)) { errors.add("shipperfax", new
                 * ActionMessage("error.shipperfax_format")); } } } if
                 * (getEbook_agreefax().length() > 0) { String fax =
                 * getEbook_agreefax(); if (fax.length() > 0) { if
                 * (!(checkNumeric(fax))) { errors.add("shipperfax", new
                 * ActionMessage("error.agreefax_invalid")); } if
                 * (!isAllZero(fax)) { errors.add("shipperfax", new
                 * ActionMessage("error.agreefax_zero")); } if
                 * (!Pattern.matches("^[0-9]{1,5}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{1,10}$",
                 * fax)) { errors.add("shipperfax", new
                 * ActionMessage("error.agreefax_format")); } } }*
                 */
                if (getEbook_shippercountry().length() > 0) {
                    String country = getEbook_shippercountry();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkcountry(country.trim()));
                        if (!(result.length() > 0)) {
                            errors.add("shippercountryname", new ActionMessage("error.shippercountryname_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (getEbook_shipperemail().length() > 0) {
                    String emailid = getEbook_shipperemail();
                    boolean check = true;
                    if (!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", emailid)) {
                        errors.add("shipperemail", new ActionMessage("error.shipperemail_invalid"));
                        check = false;
                    }
                    char[] charEmal = emailid.toCharArray();
                    int emailCount = 0;
                    if (charEmal.length > 0) {
                        int emailLen = charEmal.length;
                        for (int eL = 0; eL < emailLen; eL++) {
                            if (eL != 0) {
                                char charValidation = charEmal[eL];
                                char preValidation = charEmal[eL - 1];
                                if ((charValidation == '.' && preValidation == '.') || (charValidation == '_' && preValidation == '_') || (charValidation == '_' && preValidation == '.') || (charValidation == '.' && preValidation == '_') || (charValidation == '-' && preValidation == '-') || (charValidation == '.' && preValidation == '-') || (charValidation == '-' && preValidation == '.') || (charValidation == '-' && preValidation == '_') || (charValidation == '_' && preValidation == '-')) {
                                    emailCount = emailCount + 1;
                                }
                            }
                        }
                    }
                    if (emailCount > 0 && check) {
                        errors.add("shipperemail", new ActionMessage("error.shipperemail_invalid"));
                        check = false;
                    }
                }
                if (getEbook_consigneecountry().length() > 0) {
                    String country = getEbook_consigneecountry();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkcountry(country.trim()));
                        if (!(result.length() > 0)) {
                            errors.add("consigneecountryname", new ActionMessage("error.consigneecountryname_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (getEbook_consigneeemail().length() > 0) {
                    String emailid = getEbook_consigneeemail();
                    boolean check = true;
                    if (!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", emailid)) {
                        errors.add("consigneeemail", new ActionMessage("error.consigneeemail_invalid"));
                        check = false;
                    }
                    char[] charEmal = emailid.toCharArray();
                    int emailCount = 0;
                    if (charEmal.length > 0) {
                        int emailLen = charEmal.length;
                        for (int eL = 0; eL < emailLen; eL++) {
                            if (eL != 0) {
                                char charValidation = charEmal[eL];
                                char preValidation = charEmal[eL - 1];
                                if ((charValidation == '.' && preValidation == '.') || (charValidation == '_' && preValidation == '_') || (charValidation == '_' && preValidation == '.') || (charValidation == '.' && preValidation == '_') || (charValidation == '-' && preValidation == '-') || (charValidation == '.' && preValidation == '-') || (charValidation == '-' && preValidation == '.') || (charValidation == '-' && preValidation == '_') || (charValidation == '_' && preValidation == '-')) {
                                    emailCount = emailCount + 1;
                                }
                            }
                        }
                    }
                    if (emailCount > 0 && check) {
                        errors.add("consigneeemail", new ActionMessage("error.consigneeemail_invalid"));
                        check = false;
                    }
                }
                if (getEbook_notifycountry().length() > 0) {
                    String country = getEbook_notifycountry();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkcountry(country.trim()));
                        if (!(result.length() > 0)) {
                            errors.add("notifycountryname", new ActionMessage("error.notifycountryname_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (getEbook_notifyemail().length() > 0) {
                    String emailid = getEbook_notifyemail();
                    boolean check = true;

                    // System.out.println("[ECOM-WAR] AmendmentBookingFormBean ==>" + Pattern.matches("[a-zA-Z0-9][a-zA-Z0-9\\.]{1,100}[a-zA-Z0-9]", emailid));
                    if (!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", emailid)) {
                        errors.add("notifyemail", new ActionMessage("error.notifyemail_invalid"));
                        check = false;
                    }

                    char[] charEmal = emailid.toCharArray();
                    int emailCount = 0;
                    if (charEmal.length > 0) {
                        int emailLen = charEmal.length;
                        for (int eL = 0; eL < emailLen; eL++) {
                            if (eL != 0) {
                                char charValidation = charEmal[eL];
                                char preValidation = charEmal[eL - 1];
                                if ((charValidation == '.' && preValidation == '.') || (charValidation == '_' && preValidation == '_') || (charValidation == '_' && preValidation == '.') || (charValidation == '.' && preValidation == '_') || (charValidation == '-' && preValidation == '-') || (charValidation == '.' && preValidation == '-') || (charValidation == '-' && preValidation == '.') || (charValidation == '-' && preValidation == '_') || (charValidation == '_' && preValidation == '-')) {
                                    emailCount = emailCount + 1;
                                }
                            }
                        }
                    }
                    if (emailCount > 0 && check) {
                        errors.add("notifyemail", new ActionMessage("error.notifyemail_invalid"));
                        check = false;
                    }
                }

                // Container Check
                if (cargolist == null || cargolist.size() == 0) {
                    //  errors.add("details", new ActionMessage("error.cargodetails.add"));
                } else {
                    int conSize = cargolist.size();
                    List comListObj = (List) contexts.getAttribute("SESSION_COMMODITYLIST");
                    LinkedHashMap cargoHasObj = (LinkedHashMap) contexts.getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                    int comListSize = 0;
                    int cargoHasSize = 0;
                    if (comListObj != null) {
                        comListSize = comListObj.size();
                    }
                    if (cargoHasObj != null) {
                        cargoHasSize = cargoHasObj.size();
                    }
                    for (int n = 0; n < conSize; n++) {
                        AmendmentBookingFormBean container_grid = (AmendmentBookingFormBean) cargolist.get(n);
                        String lcl = getLcl();
                        //System.out.println("[ECOM-WAR] AmendmentBookingFormBean LCL in booking form-->" + lcl);
//                    if("N".equalsIgnoreCase(lcl))
                        if ("N".equalsIgnoreCase(lcl)) {
                            if (container_grid.getEbook_eqp_qty().length() > 0) {
                                if (!Pattern.matches("^[1-9][0-9]{0,7}$", container_grid.getEbook_eqp_qty())) {

                                    errors.add("cargoQty", new ActionMessage("error.cargoQty.invalid"));
                                }
                            } else {
                                errors.add("cargoQty", new ActionMessage("error.cargoQty.add"));
                            }

                            if (container_grid.getEbook_eqp().length() > 0) {
                                String eqpsize = container_grid.getEbook_eqp();
                                if (cargoHasSize > 0) {
                                    if (cargoHasObj.containsKey(eqpsize)) {
                                    } else {
                                        errors.add("cargoEqp", new ActionMessage("error.cargoSize.invalid"));
                                    }
                                }

                            } else {
                                errors.add("cargoEqp", new ActionMessage("error.cargoSize.add"));
                            }
                        }
                        if (container_grid.getEbook_eqp_type().length() > 0) {
                        } else {
                            errors.add("cargoEqpType", new ActionMessage("error.cargoType.add"));
                        }
                        if (container_grid.getEbook_weightunit().length() > 0) {
                        } else {
                            errors.add("commodityWeightunit", new ActionMessage("error.commodityweightunit.add"));
                        }
                        if (container_grid.getEbook_cargoweight().length() > 0) {
                            String dtWt = container_grid.getEbook_cargoweight();
                            if (!Pattern.matches("^[0-9][0-9]{0,7}[\\.]{0,1}[0-9]{0,3}$", dtWt)) {
                                errors.add("commodityWeight", new ActionMessage("error.commodityweightunit.invalid"));
                            }

                        } else {
                            errors.add("commodityWeight", new ActionMessage("error.commodityweight.add"));
                        }
                        if (container_grid.getEbook_packquantity().length() > 0) {
                            if (!Pattern.matches("^[1-9][0-9]{0,7}$", container_grid.getEbook_packquantity())) {
                                errors.add("commodityQty", new ActionMessage("error.commodityQty.invalid"));
                            }
                        } else {
                            errors.add("commodityQty", new ActionMessage("error.commodityQty.add"));
                        }
                        if (container_grid.getEbook_commoditycode().length() > 0) {
                            String dtCmd = "";
                            String strListObj = "";
                            boolean isCommodity = true;
                            if (comListSize > 0) {
                                isCommodity = false;
                                for (int i = 0; i < comListSize; i++) {
                                    dtCmd = dtCmd.toLowerCase();
                                    strListObj = "";
                                    strListObj = comListObj.get(i).toString().toLowerCase();
                                    if (strListObj.contains(dtCmd)) {
                                        isCommodity = true;
                                    }
                                }
                                if (!isCommodity) {
                                    errors.add("commodity", new ActionMessage("error.commodity.invalid"));
                                }
                            }
                        } else {
                            errors.add("commodity", new ActionMessage("error.commodity.add"));
                        }

                        if (container_grid.getEbook_unno().length() > 0) {
                            String unno = container_grid.getEbook_unno().trim();
                            String shipname = container_grid.getEbook_shipname();
                            String dgclass = container_grid.getEbook_dgclass().trim();
                            try {
                                String result = Validation.checkNull((String) ecomCntrlObj.checkDGDetails(unno, shipname, dgclass));
                                if ((result.length() == 0)) {
                                    errors.add("dgclass", new ActionMessage("error.dgclass.invalid"));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        String reef = getRfr();
                        String nor = container_grid.getEbook_nor();
                        if (nor.equalsIgnoreCase("N")) {
                            if (container_grid.getEbook_maxtemp().toString().length() > 0 && container_grid.getEbook_mintemp().toString().length() > 0) {
                                float maxtemp = Float.parseFloat(container_grid.getEbook_maxtemp());
                                float mintemp = Float.parseFloat(container_grid.getEbook_mintemp());
                                String preset = container_grid.getEbook_presetemp();
                                String port = getEbook_portofloading();
                                try {
                                    String result = Validation.checkNull((String) bookingObj.getbkgconfigDetails(port, "TMX", "TMM"));

                                    String[] details = result.split("##");
                                    String[] bkgcon = details[0].split(",");
                                    String[] bkgage = details[1].split(",");
                                    Integer maxbkg = Integer.parseInt(bkgcon[0]);
                                    Integer minbkg = Integer.parseInt(bkgcon[1]);
                                    Integer maxage = Integer.parseInt(bkgage[0]);
                                    Integer minage = Integer.parseInt(bkgage[1]);
                                    if ((mintemp < minbkg) || (maxtemp > maxbkg)) {
                                        errors.add("tempbkg", new ActionMessage("error.tempbkg.invalid"));
                                    } else if ((mintemp < minage) || (maxtemp > maxage)) {
                                        errors.add("tempbkg", new ActionMessage("error.tempbkg.invalid"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } // end of Cargo for loop
                } //end of CargoList ifelse
            } else {
                ArrayList cargolist = (ArrayList) session.getAttribute("ebooking_cargo_grid");
                ArrayList routing_array = (ArrayList) session.getAttribute("EBOOKING_GETINDEXVALUE");
                if (getEbook_origin_country().length() > 0) {
                    String portname = getEbook_origin_country();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkports(portname));
                        if (!(result.length() > 0)) {
                            errors.add("origincountry", new ActionMessage("error.origincountry_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    errors.add("origincountry", new ActionMessage("error.origincountry.add"));
                }

                if (getEbook_delivery_country().length() > 0) {
                    String portname = getEbook_delivery_country();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkports(portname));
                        if (!(result.length() > 0)) {
                            errors.add("deliverycountry", new ActionMessage("error.deliverycountry_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    errors.add("deliverycountry", new ActionMessage("error.deliverycountry.add"));
                }
                if (!(getEbook_date().length() > 0)) {
                    errors.add("ebookdate", new ActionMessage("error.ebookdate.add"));
                }

                Route eBookingrouting = null;
                String pol = "";
                String pod = "";
                String ori = "";
                String del = "";
                if (routing_array != null && routing_array.size() > 0) {
                    int len = routing_array.size();
                    for (int k = 0; k < len; k++) {
                        eBookingrouting = (Route) routing_array.get(k);
                        pol = eBookingrouting.getBlpol();
                        pod = eBookingrouting.getBlpod();
                        ori = eBookingrouting.getOrigin();
                        del = eBookingrouting.getDelivery();
                    }
                }
                Boolean shipper_flag = false;

                if (getEbook_shippercountry().length() > 0) {
                    shipper_flag = true;
                    String country = getEbook_shippercountry();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkcountry(country.trim()));
                        if (!(result.length() > 0)) {
                            errors.add("shippercountryname", new ActionMessage("error.shippercountryname_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (getEbook_shipperemail().length() > 0) {
                    shipper_flag = true;
                    String emailid = getEbook_shipperemail();
                    boolean check = true;

                    if (!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", emailid)) {
                        errors.add("shipperemail", new ActionMessage("error.shipperemail_invalid"));
                        check = false;
                    }

                    char[] charEmal = emailid.toCharArray();
                    int emailCount = 0;
                    if (charEmal.length > 0) {
                        int emailLen = charEmal.length;
                        for (int eL = 0; eL < emailLen; eL++) {
                            if (eL != 0) {
                                char charValidation = charEmal[eL];
                                char preValidation = charEmal[eL - 1];
                                if ((charValidation == '.' && preValidation == '.') || (charValidation == '_' && preValidation == '_') || (charValidation == '_' && preValidation == '.') || (charValidation == '.' && preValidation == '_') || (charValidation == '-' && preValidation == '-') || (charValidation == '.' && preValidation == '-') || (charValidation == '-' && preValidation == '.') || (charValidation == '-' && preValidation == '_') || (charValidation == '_' && preValidation == '-')) {
                                    emailCount = emailCount + 1;
                                }
                            }
                        }
                    }
                    if (emailCount > 0 && check) {
                        errors.add("shipperemail", new ActionMessage("error.shipperemail_invalid"));
                        check = false;
                    }
                }

                // Consigneee Details
                Boolean consigne_flag = false;

                if (getEbook_consigneecountry().length() > 0) {
                    consigne_flag = true;
                    String country = getEbook_consigneecountry();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkcountry(country));
                        if (!(result.length() > 0)) {
                            errors.add("consigneecountryname", new ActionMessage("error.consigneecountryname_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (getEbook_consigneeemail().length() > 0) {
                    consigne_flag = true;
                    String emailid = getEbook_consigneeemail();
                    boolean check = true;
                    if (!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", emailid)) {
                        errors.add("consigneeemail", new ActionMessage("error.consigneeemail_invalid"));
                        check = false;
                    }
                    char[] charEmal = emailid.toCharArray();
                    int emailCount = 0;
                    if (charEmal.length > 0) {
                        int emailLen = charEmal.length;
                        for (int eL = 0; eL < emailLen; eL++) {
                            if (eL != 0) {
                                char charValidation = charEmal[eL];
                                char preValidation = charEmal[eL - 1];
                                if ((charValidation == '.' && preValidation == '.') || (charValidation == '_' && preValidation == '_') || (charValidation == '_' && preValidation == '.') || (charValidation == '.' && preValidation == '_') || (charValidation == '-' && preValidation == '-') || (charValidation == '.' && preValidation == '-') || (charValidation == '-' && preValidation == '.') || (charValidation == '-' && preValidation == '_') || (charValidation == '_' && preValidation == '-')) {
                                    emailCount = emailCount + 1;
                                }
                            }
                        }
                    }
                    if (emailCount > 0 && check) {
                        errors.add("consigneeemail", new ActionMessage("error.consigneeemail_invalid"));
                        check = false;
                    }
                }

                // Notify
                Boolean notify_flag = false;
                if (getEbook_notifycountry().length() > 0) {
                    notify_flag = true;
                    String country = getEbook_notifycountry();
                    try {
                        String result = Validation.checkNull((String) ecomCntrlObj.checkcountry(country));
                        if (!(result.length() > 0)) {
                            errors.add("notifycountryname", new ActionMessage("error.notifycountryname_invalid"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (getEbook_notifyemail().length() > 0) {
                    notify_flag = true;
                    String emailid = getEbook_notifyemail();
                    boolean check = true;
                    if (!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", emailid)) {
                        errors.add("notifyemail", new ActionMessage("error.notifyemail_invalid"));
                        check = false;
                    }
                    char[] charEmal = emailid.toCharArray();
                    int emailCount = 0;
                    if (charEmal.length > 0) {
                        int emailLen = charEmal.length;
                        for (int eL = 0; eL < emailLen; eL++) {
                            if (eL != 0) {
                                char charValidation = charEmal[eL];
                                char preValidation = charEmal[eL - 1];
                                if ((charValidation == '.' && preValidation == '.') || (charValidation == '_' && preValidation == '_') || (charValidation == '_' && preValidation == '.') || (charValidation == '.' && preValidation == '_') || (charValidation == '-' && preValidation == '-') || (charValidation == '.' && preValidation == '-') || (charValidation == '-' && preValidation == '.') || (charValidation == '-' && preValidation == '_') || (charValidation == '_' && preValidation == '-')) {
                                    emailCount = emailCount + 1;
                                }
                            }
                        }
                    }
                    if (emailCount > 0 && check) {
                        errors.add("notifyemail", new ActionMessage("error.notifyemail_invalid"));
                        check = false;
                    }
                }

                // Commodity validation
                if (cargolist != null) {

                    int conSize = cargolist.size();
                    List comListObj = (List) contexts.getAttribute("SESSION_COMMODITYLIST");
                    LinkedHashMap cargoHasObj = (LinkedHashMap) contexts.getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
                    int comListSize = 0;
                    int cargoHasSize = 0;
                    if (comListObj != null) {
                        comListSize = comListObj.size();
                    }
                    if (cargoHasObj != null) {
                        cargoHasSize = cargoHasObj.size();
                    }
                    for (int n = 0; n < conSize; n++) {
                        AmendmentBookingFormBean container_grid = (AmendmentBookingFormBean) cargolist.get(n);
                        String lcl = getLcl();
                        if ("N".equalsIgnoreCase(lcl)) {
                            if (container_grid.getEbook_eqp_qty().length() > 0) {
                                if (!Pattern.matches("^[1-9][0-9]{0,7}$", container_grid.getEbook_eqp_qty())) {

                                    errors.add("cargoQty", new ActionMessage("error.cargoQty.invalid"));
                                }
                            } else {
                                errors.add("cargoQty", new ActionMessage("error.cargoQty.add"));
                            }
                            if (container_grid.getEbook_eqp().length() > 0) {
                                String eqpsize = container_grid.getEbook_eqp();
                                if (cargoHasSize > 0) {
                                    if (cargoHasObj.containsKey(eqpsize)) {
                                    } else {
                                        errors.add("cargoEqp", new ActionMessage("error.cargoSize.invalid"));
                                    }

                                }
                            } else {
                                errors.add("cargoEqp", new ActionMessage("error.cargoSize.add"));
                            }
                        }
                        if (container_grid.getEbook_eqp_type().length() > 0) {
                        } else {
                            errors.add("cargoEqpType", new ActionMessage("error.cargoType.add"));
                        }
                        if (container_grid.getEbook_weightunit().length() > 0) {
                        } else {
                            errors.add("commodityWeightunit", new ActionMessage("error.commodityweightunit.add"));
                        }
                        if (container_grid.getEbook_cargoweight().length() > 0) {
                            String dtWt = container_grid.getEbook_cargoweight();
                            if (!Pattern.matches("^[0-9][0-9]{0,7}[\\.]{0,1}[0-9]{0,3}$", dtWt)) {
                                errors.add("commodityWeight", new ActionMessage("error.commodityweightunit.invalid"));
                            }

                        } else {
                            errors.add("commodityWeight", new ActionMessage("error.commodityweight.add"));
                        }
                        if (container_grid.getEbook_packquantity().length() > 0) {
                            if (!Pattern.matches("^[1-9][0-9]{0,7}$", container_grid.getEbook_packquantity())) {

                                errors.add("commodityQty", new ActionMessage("error.commodityQty.invalid"));
                            }
                        } else {
                            errors.add("commodityQty", new ActionMessage("error.commodityQty.add"));
                        }
                        if (container_grid.getEbook_commoditycode().length() > 0) {
                            String dtCmd = "";
                            String strListObj = "";
                            boolean isCommodity = true;
                            if (comListSize > 0) {
                                isCommodity = false;
                                for (int i = 0; i < comListSize; i++) {
                                    dtCmd = dtCmd.toLowerCase();
                                    strListObj = "";
                                    strListObj = comListObj.get(i).toString().toLowerCase();
                                    if (strListObj.contains(dtCmd)) {
                                        isCommodity = true;
                                    }
                                }
                                if (!isCommodity) {
                                    errors.add("commodity", new ActionMessage("error.commodity.invalid"));
                                }
                            }
                        } else {
                            errors.add("commodity", new ActionMessage("error.commodity.add"));
                        }
                        if (container_grid.getEbook_unno().length() > 0) {
                            String unno = container_grid.getEbook_unno().trim();
                            String shipname = container_grid.getEbook_shipname();
                            String dgclass = container_grid.getEbook_dgclass().trim();
                            try {

                                String result = Validation.checkNull((String) ecomCntrlObj.checkDGDetails(unno, shipname, dgclass));
                                if (!(result.length() > 0)) {
                                    errors.add("dgclass", new ActionMessage("error.dgclass.invalid"));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        String reef = getRfr();
                        String nor = container_grid.getEbook_nor();
                        if (nor.equalsIgnoreCase("N")) {
                            float maxtemp = 0;
                            float mintemp = 0;
                            if (container_grid.getEbook_maxtemp() != null && !container_grid.getEbook_maxtemp().equalsIgnoreCase("")) {
                                maxtemp = Float.parseFloat(container_grid.getEbook_maxtemp());
                            }
                            if (container_grid.getEbook_mintemp() != null && !container_grid.getEbook_mintemp().equalsIgnoreCase("")) {
                                mintemp = Float.parseFloat(container_grid.getEbook_mintemp());
                            }
                            String preset = container_grid.getEbook_presetemp();
                            String port = getEbook_portofloading();
                            try {
                                String result = Validation.checkNull((String) bookingObj.getbkgconfigDetails(port, "TMX", "TMM"));

                                String[] details = result.split("##");
                                String[] bkgcon = details[0].split(",");
                                String[] bkgage = details[1].split(",");

                                Integer maxbkg = Integer.parseInt(bkgcon[0]);
                                Integer minbkg = Integer.parseInt(bkgcon[1]);
                                Integer maxage = Integer.parseInt(bkgage[0]);
                                Integer minage = Integer.parseInt(bkgage[1]);
                                if ((mintemp < minbkg) || (maxtemp > maxbkg)) {
                                    errors.add("tempbkg", new ActionMessage("error.tempbkg.invalid"));
                                } else if ((mintemp < minage) || (maxtemp > maxage)) {
                                    errors.add("tempbkg", new ActionMessage("error.tempbkg.invalid"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                if ("tempadd".equalsIgnoreCase(btn_type)) {
                    if (getEbook_templatename().trim().length() != 0) {
                        ArrayList templist = (ArrayList) session.getAttribute("SESSION_BACKUP_TEMPLATE_NO");
                        if (templist != null) {
                            int tem_len = templist.size();
                            for (int i = 0; i < tem_len; i++) {
                                if (getEbook_templatename().trim().equalsIgnoreCase((String) templist.get(i))) {
                                    errors.add("template", new ActionMessage("error.template.exists"));
                                }
                            }
                        }
                    }
                }
            }
            if (errors != null && errors.size() > 0) {
                setHidden_empty(getEmptyPickup());
                setHidden_soc(getSoc());
                List personalInfo = null;
                try {
                    String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                    personalInfo = ecomCntrlObj.getAPDetails(userCode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                request.setAttribute("PersonalInfo1", personalInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ECOM-WAR] AmendmentBookingFormBean Error in validation--->" + e);
        }
        if (errors != null && errors.size() > 0) {
            request.setAttribute("bookingerrors", "bookingerrors");
        } else {
            request.setAttribute("bookingerrors", "");
        }

        return errors;
    }

    /**
     * @return the ebook_origin_desc
     */
    public String getEbook_origin_desc() {
        return Validation.checkNull(ebook_origin_desc);
    }

    /**
     * @param ebook_origin_desc the ebook_origin_desc to set
     */
    public void setEbook_origin_desc(String ebook_origin_desc) {
        this.ebook_origin_desc = ebook_origin_desc;
    }

    /**
     * @return the ebook_delivery_desc
     */
    public String getEbook_delivery_desc() {
        return Validation.checkNull(ebook_delivery_desc);
    }

    /**
     * @param ebook_delivery_desc the ebook_delivery_desc to set
     */
    public void setEbook_delivery_desc(String ebook_delivery_desc) {
        this.ebook_delivery_desc = ebook_delivery_desc;
    }

    /**
     * @return the ebook_eqp
     */
    public String getEbook_eqp() {
        return Validation.checkNull(ebook_eqp);
    }

    /**
     * @param ebook_eqp the ebook_eqp to set
     */
    public void setEbook_eqp(String ebook_eqp) {
        this.ebook_eqp = ebook_eqp;
    }

    /**
     * @return the ebook_eqp_qty
     */
    public String getEbook_eqp_qty() {
        return Validation.checkNull(ebook_eqp_qty);
    }

    /**
     * @param ebook_eqp_qty the ebook_eqp_qty to set
     */
    public void setEbook_eqp_qty(String ebook_eqp_qty) {
        this.ebook_eqp_qty = ebook_eqp_qty;
    }

    /**
     * @return the ebook_eqp_type
     */
    public String getEbook_eqp_type() {
        return Validation.checkNull(ebook_eqp_type);
    }

    /**
     * @param ebook_eqp_type the ebook_eqp_type to set
     */
    public void setEbook_eqp_type(String ebook_eqp_type) {
        this.ebook_eqp_type = ebook_eqp_type;
    }

    /**
     * @return the ebook_eqp_type_desc
     */
    public String getEbook_eqp_type_desc() {
        return Validation.checkNull(ebook_eqp_type_desc);
    }

    /**
     * @param ebook_eqp_type_desc the ebook_eqp_type_desc to set
     */
    public void setEbook_eqp_type_desc(String ebook_eqp_type_desc) {
        this.ebook_eqp_type_desc = ebook_eqp_type_desc;
    }

    /**
     * @return the ebook_bound
     */
    public String getEbook_bound() {
        return Validation.checkNull(ebook_bound);
    }

    /**
     * @param ebook_bound the ebook_bound to set
     */
    public void setEbook_bound(String ebook_bound) {
        this.ebook_bound = ebook_bound;
    }

    private boolean checkNumeric(String phone) {
        String num = "0123456789-";
        boolean check = true;
        int j = 0;

        for (int i = 0; i < phone.length(); i++) {

            if ((num.indexOf(phone.charAt(i))) == -1) {
                j++;

            }
        }
        if (j > 0) {
            check = false;
        }
        return check;
    }

    private boolean isAllZero(String num) {
        boolean Validationue = true;
        try {
            int count = 0;
            int len = num.length();
            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    char charValidation = num.charAt(i);
                    if (charValidation == '0') {
                        count = count + 1;
                    }
                }
            }
            if (count == len) {
                Validationue = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Validationue;
    }

    /**
     * @return the ebook_routeid
     */
    public String getEbook_routeid() {
        return Validation.checkNull(ebook_routeid);
    }

    /**
     * @param ebook_routeid the ebook_routeid to set
     */
    public void setEbook_routeid(String ebook_routeid) {
        this.ebook_routeid = ebook_routeid;
    }

    /**
     * @return the ebook_comid
     */
    public String getEbook_comid() {
        return Validation.checkNull(ebook_comid);
    }

    /**
     * @param ebook_comid the ebook_comid to set
     */
    public void setEbook_comid(String ebook_comid) {
        this.ebook_comid = ebook_comid;
    }

    /**
     * @return the ebook_conid
     */
    public String getEbook_conid() {
        return Validation.checkNull(ebook_conid);
    }

    /**
     * @param ebook_conid the ebook_conid to set
     */
    public void setEbook_conid(String ebook_conid) {
        this.ebook_conid = ebook_conid;
    }

    /**
     * @return the ebook_shipper_id
     */
    public String getEbook_shipper_id() {
        return Validation.checkNull(ebook_shipper_id);
    }

    /**
     * @param ebook_shipper_id the ebook_shipper_id to set
     */
    public void setEbook_shipper_id(String ebook_shipper_id) {
        this.ebook_shipper_id = ebook_shipper_id;
    }

    /**
     * @return the ebook_consigne_id
     */
    public String getEbook_consigne_id() {
        return Validation.checkNull(ebook_consigne_id);
    }

    /**
     * @param ebook_consigne_id the ebook_consigne_id to set
     */
    public void setEbook_consigne_id(String ebook_consigne_id) {
        this.ebook_consigne_id = ebook_consigne_id;
    }

    /**
     * @return the ebook_notify_id
     */
    public String getEbook_notify_id() {
        return Validation.checkNull(ebook_notify_id);
    }

    /**
     * @param ebook_notify_id the ebook_notify_id to set
     */
    public void setEbook_notify_id(String ebook_notify_id) {
        this.ebook_notify_id = ebook_notify_id;
    }

    /**
     * @return the emptyReturn
     */
    public String getEmptyReturn() {
        return Validation.checkNull(emptyReturn);
    }

    /**
     * @param emptyReturn the emptyReturn to set
     */
    public void setEmptyReturn(String emptyReturn) {
        this.emptyReturn = emptyReturn;
    }

    /**
     * @return the hidden_empty
     */
    public String getHidden_empty() {
        return Validation.checkNull(hidden_empty);
    }

    /**
     * @param hidden_empty the hidden_empty to set
     */
    public void setHidden_empty(String hidden_empty) {
        this.hidden_empty = hidden_empty;
    }

    /**
     * @return the hidden_soc
     */
    public String getHidden_soc() {
        return Validation.checkNull(hidden_soc);
    }

    /**
     * @param hidden_soc the hidden_soc to set
     */
    public void setHidden_soc(String hidden_soc) {
        this.hidden_soc = hidden_soc;
    }

    /**
     * @return the groupcallid
     */
    public String getGroupcallid() {
        return groupcallid;
    }

    /**
     * @param groupcallid the groupcallid to set
     */
    public void setGroupcallid(String groupcallid) {
        this.groupcallid = groupcallid;
    }

    /**
     * @return the ebook_view_frmdate
     */
//Added By Shikin For Plarpenh-438
    public String getEbook_rate() {
        return Validation.checkNull(ebook_rate);
    }

    public void setEbook_rate(String ebook_rate) {
        this.ebook_rate = ebook_rate;
    }

    public String getEbook_rate_desc() {
        return Validation.checkNull(ebook_rate_desc);
    }

    public void setEbook_rate_desc(String ebook_rate_desc) {
        this.ebook_rate_desc = ebook_rate_desc;
    }

    public String getEbook_rate_reference() {
        return Validation.checkNull(ebook_rate_reference);
    }

    public void setEbook_rate_reference(String ebook_rate_reference) {
        this.ebook_rate_reference = ebook_rate_reference;
    }

    public String getEbook_party() {
        return Validation.checkNull(ebook_party);
    }

    public void setEbook_party(String ebook_party) {
        this.ebook_party = ebook_party;
    }

    public String getEbook_party_desc() {
        return Validation.checkNull(ebook_party_desc);
    }

    public void setEbook_party_desc(String ebook_party_desc) {
        this.ebook_party_desc = ebook_party_desc;
    }

    public String getEbook_bookingparty_desc() {
        return Validation.checkNull(ebook_bookingparty_desc);
    }

    public void setEbook_bookingparty_desc(String ebook_bookingparty_desc) {
        this.ebook_bookingparty_desc = ebook_bookingparty_desc;
    }

    public String getEbook_shipment() {
        return Validation.checkNull(ebook_shipment);
    }

    public void setEbook_shipment(String ebook_shipment) {
        this.ebook_shipment = ebook_shipment;
    }

    public String getEbook_shipment_desc() {
        return Validation.checkNull(ebook_shipment_desc);
    }

    public void setEbook_shipment_desc(String ebook_shipment_desc) {
        this.ebook_shipment_desc = ebook_shipment_desc;
    }

    public String getEbook_shipterm_desc() {
        return Validation.checkNull(ebook_shipterm_desc);
    }

    public void setEbook_shipterm_desc(String ebook_shipterm_desc) {
        this.ebook_shipterm_desc = ebook_shipterm_desc;
    }

    public void setHzd(String hzd) {
        this.hzd = hzd;
    }

    public String getHzd() {
        return Validation.checkNull(hzd);
    }

    public void setRfr(String rfr) {
        this.rfr = rfr;
    }

    public String getRfr() {
        return Validation.checkNull(rfr);
    }

    public void setOog(String oog) {
        this.oog = oog;
    }

    public String getOog() {
        return Validation.checkNull(oog);
    }

    public void setLcl(String lcl) {

        this.lcl = lcl;
    }

    public String getLcl() {

        return Validation.checkNull(lcl);
    }

    /**
     * @return the hdntempaltename
     */
    public String getHdntempaltename() {
        return hdntempaltename;
    }

    /**
     * @param hdntempaltename the hdntempaltename to set
     */
    public void setHdntempaltename(String hdntempaltename) {
        this.hdntempaltename = hdntempaltename;
    }

    /**
     * @return the cargo_hidden_id
     */
    public String getCargo_hidden_id() {
        return cargo_hidden_id;
    }

    /**
     * @param cargo_hidden_id the cargo_hidden_id to set
     */
    public void setCargo_hidden_id(String cargo_hidden_id) {
        this.cargo_hidden_id = cargo_hidden_id;
    }

    /**
     * @return the cargo_hidden_type
     */
    public String getCargo_hidden_type() {
        return cargo_hidden_type;
    }

    /**
     * @param cargo_hidden_type the cargo_hidden_type to set
     */
    public void setCargo_hidden_type(String cargo_hidden_type) {
        this.cargo_hidden_type = cargo_hidden_type;
    }

    /**
     * @return the ebook_notifyphone1
     */
    public String getEbook_notifyphone1() {
        return ebook_notifyphone1;
    }

    /**
     * @param ebook_notifyphone1 the ebook_notifyphone1 to set
     */
    public void setEbook_notifyphone1(String ebook_notifyphone1) {
        this.ebook_notifyphone1 = ebook_notifyphone1;
    }

    /**
     * @return the ebook_notifyphone2
     */
    public String getEbook_notifyphone2() {
        return ebook_notifyphone2;
    }

    /**
     * @param ebook_notifyphone2 the ebook_notifyphone2 to set
     */
    public void setEbook_notifyphone2(String ebook_notifyphone2) {
        this.ebook_notifyphone2 = ebook_notifyphone2;
    }

    /**
     * @return the ebook_notifyphone3
     */
    public String getEbook_notifyphone3() {
        return ebook_notifyphone3;
    }

    /**
     * @param ebook_notifyphone3 the ebook_notifyphone3 to set
     */
    public void setEbook_notifyphone3(String ebook_notifyphone3) {
        this.ebook_notifyphone3 = ebook_notifyphone3;
    }

    /**
     * @return the ebook_notifyfax1
     */
    public String getEbook_notifyfax1() {
        return ebook_notifyfax1;
    }

    /**
     * @param ebook_notifyfax1 the ebook_notifyfax1 to set
     */
    public void setEbook_notifyfax1(String ebook_notifyfax1) {
        this.ebook_notifyfax1 = ebook_notifyfax1;
    }

    /**
     * @return the ebook_notifyfax2
     */
    public String getEbook_notifyfax2() {
        return ebook_notifyfax2;
    }

    /**
     * @param ebook_notifyfax2 the ebook_notifyfax2 to set
     */
    public void setEbook_notifyfax2(String ebook_notifyfax2) {
        this.ebook_notifyfax2 = ebook_notifyfax2;
    }

    /**
     * @return the ebook_notifyfax3
     */
    public String getEbook_notifyfax3() {
        return ebook_notifyfax3;
    }

    /**
     * @param ebook_notifyfax3 the ebook_notifyfax3 to set
     */
    public void setEbook_notifyfax3(String ebook_notifyfax3) {
        this.ebook_notifyfax3 = ebook_notifyfax3;
    }

    /**
     * @return the ebook_consigneefax1
     */
    public String getEbook_consigneefax1() {
        return ebook_consigneefax1;
    }

    /**
     * @param ebook_consigneefax1 the ebook_consigneefax1 to set
     */
    public void setEbook_consigneefax1(String ebook_consigneefax1) {
        this.ebook_consigneefax1 = ebook_consigneefax1;
    }

    /**
     * @return the ebook_consigneefax2
     */
    public String getEbook_consigneefax2() {
        return ebook_consigneefax2;
    }

    /**
     * @param ebook_consigneefax2 the ebook_consigneefax2 to set
     */
    public void setEbook_consigneefax2(String ebook_consigneefax2) {
        this.ebook_consigneefax2 = ebook_consigneefax2;
    }

    /**
     * @return the ebook_consigneefax3
     */
    public String getEbook_consigneefax3() {
        return ebook_consigneefax3;
    }

    /**
     * @param ebook_consigneefax3 the ebook_consigneefax3 to set
     */
    public void setEbook_consigneefax3(String ebook_consigneefax3) {
        this.ebook_consigneefax3 = ebook_consigneefax3;
    }

    /**
     * @return the ebook_consigneephone1
     */
    public String getEbook_consigneephone1() {
        return ebook_consigneephone1;
    }

    /**
     * @param ebook_consigneephone1 the ebook_consigneephone1 to set
     */
    public void setEbook_consigneephone1(String ebook_consigneephone1) {
        this.ebook_consigneephone1 = ebook_consigneephone1;
    }

    /**
     * @return the ebook_consigneephone2
     */
    public String getEbook_consigneephone2() {
        return ebook_consigneephone2;
    }

    /**
     * @param ebook_consigneephone2 the ebook_consigneephone2 to set
     */
    public void setEbook_consigneephone2(String ebook_consigneephone2) {
        this.ebook_consigneephone2 = ebook_consigneephone2;
    }

    /**
     * @return the ebook_consigneephone3
     */
    public String getEbook_consigneephone3() {
        return ebook_consigneephone3;
    }

    /**
     * @param ebook_consigneephone3 the ebook_consigneephone3 to set
     */
    public void setEbook_consigneephone3(String ebook_consigneephone3) {
        this.ebook_consigneephone3 = ebook_consigneephone3;
    }

    /**
     * @return the ebook_shipperfax1
     */
    public String getEbook_shipperfax1() {
        return ebook_shipperfax1;
    }

    /**
     * @param ebook_shipperfax1 the ebook_shipperfax1 to set
     */
    public void setEbook_shipperfax1(String ebook_shipperfax1) {
        this.ebook_shipperfax1 = ebook_shipperfax1;
    }

    /**
     * @return the ebook_shipperfax2
     */
    public String getEbook_shipperfax2() {
        return ebook_shipperfax2;
    }

    /**
     * @param ebook_shipperfax2 the ebook_shipperfax2 to set
     */
    public void setEbook_shipperfax2(String ebook_shipperfax2) {
        this.ebook_shipperfax2 = ebook_shipperfax2;
    }

    /**
     * @return the ebook_shipperfax3
     */
    public String getEbook_shipperfax3() {
        return ebook_shipperfax3;
    }

    /**
     * @param ebook_shipperfax3 the ebook_shipperfax3 to set
     */
    public void setEbook_shipperfax3(String ebook_shipperfax3) {
        this.ebook_shipperfax3 = ebook_shipperfax3;
    }

    /**
     * @return the ebook_shipperphone1
     */
    public String getEbook_shipperphone1() {
        return ebook_shipperphone1;
    }

    /**
     * @param ebook_shipperphone1 the ebook_shipperphone1 to set
     */
    public void setEbook_shipperphone1(String ebook_shipperphone1) {
        this.ebook_shipperphone1 = ebook_shipperphone1;
    }

    /**
     * @return the ebook_shipperphone2
     */
    public String getEbook_shipperphone2() {
        return ebook_shipperphone2;
    }

    /**
     * @param ebook_shipperphone2 the ebook_shipperphone2 to set
     */
    public void setEbook_shipperphone2(String ebook_shipperphone2) {
        this.ebook_shipperphone2 = ebook_shipperphone2;
    }

    /**
     * @return the ebook_shipperphone3
     */
    public String getEbook_shipperphone3() {
        return ebook_shipperphone3;
    }

    /**
     * @param ebook_shipperphone3 the ebook_shipperphone3 to set
     */
    public void setEbook_shipperphone3(String ebook_shipperphone3) {
        this.ebook_shipperphone3 = ebook_shipperphone3;
    }

    /**
     * @return the trpservice
     */
    public String getTrpservice() {
        return Validation.checkNull(trpservice);
    }

    /**
     * @param trpservice the trpservice to set
     */
    public void setTrpservice(String trpservice) {
        this.trpservice = trpservice;
    }

    /**
     * @return the servicehd
     */
    public String getServicehd() {
        return servicehd;
    }

    /**
     * @param servicehd the servicehd to set
     */
    public void setServicehd(String servicehd) {
        this.servicehd = servicehd;
    }

    /**
     * @return the servicehdedit
     */
    public String getServicehdedit() {
        return servicehdedit;
    }

    /**
     * @param servicehdedit the servicehdedit to set
     */
    public void setServicehdedit(String servicehdedit) {
        this.servicehdedit = servicehdedit;
    }

    /**
     * @return the file_path1
     */
    public FormFile getFile_path1() {
        return file_path1;
    }

    /**
     * @param file_path1 the file_path1 to set
     */
    public void setFile_path1(FormFile file_path1) {
        this.file_path1 = file_path1;
    }

    /**
     * @return the file_path2
     */
    public FormFile getFile_path2() {
        return file_path2;
    }

    /**
     * @param file_path2 the file_path2 to set
     */
    public void setFile_path2(FormFile file_path2) {
        this.file_path2 = file_path2;
    }

    /**
     * @return the file_path3
     */
    public FormFile getFile_path3() {
        return file_path3;
    }

    /**
     * @param file_path3 the file_path3 to set
     */
    public void setFile_path3(FormFile file_path3) {
        this.file_path3 = file_path3;
    }

    /**
     * @return the file_path4
     */
    public FormFile getFile_path4() {
        return file_path4;
    }

    /**
     * @param file_path4 the file_path4 to set
     */
    public void setFile_path4(FormFile file_path4) {
        this.file_path4 = file_path4;
    }

    /**
     * @return the file_path5
     */
    public FormFile getFile_path5() {
        return file_path5;
    }

    /**
     * @param file_path5 the file_path5 to set
     */
    public void setFile_path5(FormFile file_path5) {
        this.file_path5 = file_path5;
    }

    /**
     * @return the filename1
     */
    public String getFilename1() {
        return filename1;
    }

    /**
     * @param filename1 the filename1 to set
     */
    public void setFilename1(String filename1) {
        this.filename1 = filename1;
    }

    /**
     * @return the filename2
     */
    public String getFilename2() {
        return filename2;
    }

    /**
     * @param filename2 the filename2 to set
     */
    public void setFilename2(String filename2) {
        this.filename2 = filename2;
    }

    /**
     * @return the filename3
     */
    public String getFilename3() {
        return filename3;
    }

    /**
     * @param filename3 the filename3 to set
     */
    public void setFilename3(String filename3) {
        this.filename3 = filename3;
    }

    /**
     * @return the filename4
     */
    public String getFilename4() {
        return filename4;
    }

    /**
     * @param filename4 the filename4 to set
     */
    public void setFilename4(String filename4) {
        this.filename4 = filename4;
    }

    /**
     * @return the filename5
     */
    public String getFilename5() {
        return filename5;
    }

    /**
     * @param filename5 the filename5 to set
     */
    public void setFilename5(String filename5) {
        this.filename5 = filename5;
    }

    /**
     * @return the ebooking_commodity_desc
     */
    public String getEbooking_commodity_desc() {
        return ebooking_commodity_desc;
    }

    /**
     * @param ebooking_commodity_desc the ebooking_commodity_desc to set
     */
    public void setEbooking_commodity_desc(String ebooking_commodity_desc) {
        this.ebooking_commodity_desc = ebooking_commodity_desc;
    }

    public String getEbook_agency_reference() {
        return ebook_agency_reference;
    }

    public void setEbook_agency_reference(String ebook_agency_reference) {
        this.ebook_agency_reference = ebook_agency_reference;
    }

    public String getEmptyPickupHid() {
        return emptyPickupHid;
    }

    public void setEmptyPickupHid(String emptyPickupHid) {
        this.emptyPickupHid = emptyPickupHid;
    }
    //

    /**
     * @return the hidPodIntransitName
     */
    public String getHidPodIntransitName() {
        return hidPodIntransitName;
    }

    /**
     * @param hidPodIntransitName the hidPodIntransitName to set
     */
    public void setHidPodIntransitName(String hidPodIntransitName) {
        this.hidPodIntransitName = hidPodIntransitName;
    }

    /**
     * @return the documentalagencyid
     */
    public String getDocumentalagencyid() {
        return documentalagencyid;
    }

    /**
     * @param documentalagencyid the documentalagencyid to set
     */
    public void setDocumentalagencyid(String documentalagencyid) {
        this.documentalagencyid = documentalagencyid;
    }

    /**
     * @return the agencyissueid
     */
    public String getAgencyissueid() {
        return agencyissueid;
    }

    /**
     * @param agencyissueid the agencyissueid to set
     */
    public void setAgencyissueid(String agencyissueid) {
        this.agencyissueid = agencyissueid;
    }

    /**
     * @return the agencyid
     */
    public String getAgencyid() {
        return agencyid;
    }

    /**
     * @param agencyid the agencyid to set
     */
    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    /**
     * @return the ebook_natcode
     */
    public String getEbook_natcode() {
        return ebook_natcode;
    }

    /**
     * @param ebook_natcode the ebook_natcode to set
     */
    public void setEbook_natcode(String ebook_natcode) {
        this.ebook_natcode = ebook_natcode;
    }

    /**
     * @return the ebook_isdummy
     */
    public String getEbook_isdummy() {
        return ebook_isdummy;
    }

    /**
     * @param ebook_isdummy the ebook_isdummy to set
     */
    public void setEbook_isdummy(String ebook_isdummy) {
        this.ebook_isdummy = ebook_isdummy;
    }

    /**
     * @return the ebook_pickup_point
     */
    public String getEbook_pickup_point() {
        return ebook_pickup_point;
    }

    /**
     * @param ebook_pickup_point the ebook_pickup_point to set
     */
    public void setEbook_pickup_point(String ebook_pickup_point) {
        this.ebook_pickup_point = ebook_pickup_point;
    }

    /**
     * @return the ebkg_type
     */
    public String getEbkg_type() {
        return ebkg_type;
    }

    /**
     * @param ebkg_type the ebkg_type to set
     */
    public void setEbkg_type(String ebkg_type) {
        this.ebkg_type = ebkg_type;
    }

    public String getEbook_forwardername() {
        return ebook_forwardername;
    }

    public void setEbook_forwardername(String ebook_forwardername) {
        this.ebook_forwardername = ebook_forwardername;
    }

    public String getEbook_forwardercode() {
        return ebook_forwardercode;
    }

    public void setEbook_forwardercode(String ebook_forwardercode) {
        this.ebook_forwardercode = ebook_forwardercode;
    }

    public String getEbook_forwarderzipcode() {
        return ebook_forwarderzipcode;
    }

    public void setEbook_forwarderzipcode(String ebook_forwarderzipcode) {
        this.ebook_forwarderzipcode = ebook_forwarderzipcode;
    }

    public String getEbook_forwarderaddress() {
        return ebook_forwarderaddress;
    }

    public void setEbook_forwarderaddress(String ebook_forwarderaddress) {
        this.ebook_forwarderaddress = ebook_forwarderaddress;
    }

    public String getEbook_forwardercountry() {
        return ebook_forwardercountry;
    }

    public void setEbook_forwardercountry(String ebook_forwardercountry) {
        this.ebook_forwardercountry = ebook_forwardercountry;
    }

    public String getEbook_forwardercountrycode() {
        return ebook_forwardercountrycode;
    }

    public void setEbook_forwardercountrycode(String ebook_forwardercountrycode) {
        this.ebook_forwardercountrycode = ebook_forwardercountrycode;
    }

    public String getEbook_forwardercity() {
        return ebook_forwardercity;
    }

    public void setEbook_forwardercity(String ebook_forwardercity) {
        this.ebook_forwardercity = ebook_forwardercity;
    }

    public String getEbook_forwarderphone() {
        return ebook_forwarderphone;
    }

    public void setEbook_forwarderphone(String ebook_forwarderphone) {
        this.ebook_forwarderphone = ebook_forwarderphone;
    }

    public String getEbook_forwarderfax() {
        return ebook_forwarderfax;
    }

    public void setEbook_forwarderfax(String ebook_forwarderfax) {
        this.ebook_forwarderfax = ebook_forwarderfax;
    }

    public String getEbook_forwarderemail() {
        return ebook_forwarderemail;
    }

    public void setEbook_forwarderemail(String ebook_forwarderemail) {
        this.ebook_forwarderemail = ebook_forwarderemail;
    }

    public String getEbook_forwardercountry_desc() {
        return ebook_forwardercountry_desc;
    }

    public void setEbook_forwardercountry_desc(String ebook_forwardercountry_desc) {
        this.ebook_forwardercountry_desc = ebook_forwardercountry_desc;
    }

    public String getEbook_forwardercity_desc() {
        return ebook_forwardercity_desc;
    }

    public void setEbook_forwardercity_desc(String ebook_forwardercity_desc) {
        this.ebook_forwardercity_desc = ebook_forwardercity_desc;
    }

    public String getEbook_forwarder_id() {
        return ebook_forwarder_id;
    }

    public void setEbook_forwarder_id(String ebook_forwarder_id) {
        this.ebook_forwarder_id = ebook_forwarder_id;
    }

    public String getEbook_Warehousename() {
        return ebook_Warehousename;
    }

    public void setEbook_Warehousename(String ebook_Warehousename) {
        this.ebook_Warehousename = ebook_Warehousename;
    }

    public String getEbook_Warehousecode() {
        return ebook_Warehousecode;
    }

    public void setEbook_Warehousecode(String ebook_Warehousecode) {
        this.ebook_Warehousecode = ebook_Warehousecode;
    }

    public String getEbook_Warehousezipcode() {
        return ebook_Warehousezipcode;
    }

    public void setEbook_Warehousezipcode(String ebook_Warehousezipcode) {
        this.ebook_Warehousezipcode = ebook_Warehousezipcode;
    }

    public String getEbook_Warehouseaddress() {
        return ebook_Warehouseaddress;
    }

    public void setEbook_Warehouseaddress(String ebook_Warehouseaddress) {
        this.ebook_Warehouseaddress = ebook_Warehouseaddress;
    }

    public String getEbook_Warehousecountry() {
        return ebook_Warehousecountry;
    }

    public void setEbook_Warehousecountry(String ebook_Warehousecountry) {
        this.ebook_Warehousecountry = ebook_Warehousecountry;
    }

    public String getEbook_Warehousecountrycode() {
        return ebook_Warehousecountrycode;
    }

    public void setEbook_Warehousecountrycode(String ebook_Warehousecountrycode) {
        this.ebook_Warehousecountrycode = ebook_Warehousecountrycode;
    }

    public String getEbook_Warehousecity() {
        return ebook_Warehousecity;
    }

    public void setEbook_Warehousecity(String ebook_Warehousecity) {
        this.ebook_Warehousecity = ebook_Warehousecity;
    }

    public String getEbook_Warehousephone() {
        return ebook_Warehousephone;
    }

    public void setEbook_Warehousephone(String ebook_Warehousephone) {
        this.ebook_Warehousephone = ebook_Warehousephone;
    }

    public String getEbook_Warehousefax() {
        return ebook_Warehousefax;
    }

    public void setEbook_Warehousefax(String ebook_Warehousefax) {
        this.ebook_Warehousefax = ebook_Warehousefax;
    }

    public String getEbook_Warehouseemail() {
        return ebook_Warehouseemail;
    }

    public void setEbook_Warehouseemail(String ebook_Warehouseemail) {
        this.ebook_Warehouseemail = ebook_Warehouseemail;
    }

    public String getEbook_Warehousecountry_desc() {
        return ebook_Warehousecountry_desc;
    }

    public void setEbook_Warehousecountry_desc(String ebook_Warehousecountry_desc) {
        this.ebook_Warehousecountry_desc = ebook_Warehousecountry_desc;
    }

    public String getEbook_Warehousecity_desc() {
        return ebook_Warehousecity_desc;
    }

    public void setEbook_Warehousecity_desc(String ebook_Warehousecity_desc) {
        this.ebook_Warehousecity_desc = ebook_Warehousecity_desc;
    }

    public String getEbook_Warehouse_id() {
        return ebook_Warehouse_id;
    }

    public void setEbook_Warehouse_id(String ebook_Warehouse_id) {
        this.ebook_Warehouse_id = ebook_Warehouse_id;
    }

    public String getShipperAPCheck() {
        return shipperAPCheck;
    }

    public void setShipperAPCheck(String shipperAPCheck) {
        this.shipperAPCheck = shipperAPCheck;
    }

    public String getEbook_forwardercity_hdn() {
        return ebook_forwardercity_hdn;
    }

    public void setEbook_forwardercity_hdn(String ebook_forwardercity_hdn) {
        this.ebook_forwardercity_hdn = ebook_forwardercity_hdn;
    }

    public String getEbook_forwardercountry_name() {
        return ebook_forwardercountry_name;
    }

    public void setEbook_forwardercountry_name(String ebook_forwardercountry_name) {
        this.ebook_forwardercountry_name = ebook_forwardercountry_name;
    }

    public String getEbook_forwarderaddress1() {
        return ebook_forwarderaddress1;
    }

    public void setEbook_forwarderaddress1(String ebook_forwarderaddress1) {
        this.ebook_forwarderaddress1 = ebook_forwarderaddress1;
    }

    public String getEbook_Warehousecity_hdn() {
        return ebook_Warehousecity_hdn;
    }

    public void setEbook_Warehousecity_hdn(String ebook_Warehousecity_hdn) {
        this.ebook_Warehousecity_hdn = ebook_Warehousecity_hdn;
    }

    public String getEbook_Warehousecountry_name() {
        return ebook_Warehousecountry_name;
    }

    public void setEbook_Warehousecountry_name(String ebook_Warehousecountry_name) {
        this.ebook_Warehousecountry_name = ebook_Warehousecountry_name;
    }

    public String getEbook_Warehouseaddress1() {
        return ebook_Warehouseaddress1;
    }

    public void setEbook_Warehouseaddress1(String ebook_Warehouseaddress1) {
        this.ebook_Warehouseaddress1 = ebook_Warehouseaddress1;
    }

    public String getEbook_bookingstate() {
        return ebook_bookingstate;
    }

    public void setEbook_bookingstate(String ebook_bookingstate) {
        this.ebook_bookingstate = ebook_bookingstate;
    }

    public String getEbook_agreestate() {
        return ebook_agreestate;
    }

    public void setEbook_agreestate(String ebook_agreestate) {
        this.ebook_agreestate = ebook_agreestate;
    }

    public String getEbook_shipperstate() {
        return ebook_shipperstate;
    }

    public void setEbook_shipperstate(String ebook_shipperstate) {
        this.ebook_shipperstate = ebook_shipperstate;
    }

    public String getEbook_consigneestate() {
        return ebook_consigneestate;
    }

    public void setEbook_consigneestate(String ebook_consigneestate) {
        this.ebook_consigneestate = ebook_consigneestate;
    }

    public String getEbook_notifystate() {
        return ebook_notifystate;
    }

    public void setEbook_notifystate(String ebook_notifystate) {
        this.ebook_notifystate = ebook_notifystate;
    }

    public String getEbook_notifystate_n1() {
        return ebook_notifystate_n1;
    }

    public void setEbook_notifystate_n1(String ebook_notifystate_n1) {
        this.ebook_notifystate_n1 = ebook_notifystate_n1;
    }

    public String getEbook_forwarderstate() {
        return ebook_forwarderstate;
    }

    public void setEbook_forwarderstate(String ebook_forwarderstate) {
        this.ebook_forwarderstate = ebook_forwarderstate;
    }

    public String getEbook_Warehousestate() {
        return ebook_Warehousestate;
    }

    public void setEbook_Warehousestate(String ebook_Warehousestate) {
        this.ebook_Warehousestate = ebook_Warehousestate;
    }

    public String getDeletedfilename() {
        return deletedfilename;
    }

    public void setDeletedfilename(String deletedfilename) {
        this.deletedfilename = deletedfilename;
    }

    public FormFile getFile_path6() {
        return file_path6;
    }

    public void setFile_path6(FormFile file_path6) {
        this.file_path6 = file_path6;
    }

    public FormFile getFile_path7() {
        return file_path7;
    }

    public void setFile_path7(FormFile file_path7) {
        this.file_path7 = file_path7;
    }

    public FormFile getFile_path8() {
        return file_path8;
    }

    public void setFile_path8(FormFile file_path8) {
        this.file_path8 = file_path8;
    }

    public FormFile getFile_path9() {
        return file_path9;
    }

    public void setFile_path9(FormFile file_path9) {
        this.file_path9 = file_path9;
    }

    public FormFile getFile_path10() {
        return file_path10;
    }

    public void setFile_path10(FormFile file_path10) {
        this.file_path10 = file_path10;
    }

    public FormFile getFile_path11() {
        return file_path11;
    }

    public void setFile_path11(FormFile file_path11) {
        this.file_path11 = file_path11;
    }

    public FormFile getFile_path12() {
        return file_path12;
    }

    public void setFile_path12(FormFile file_path12) {
        this.file_path12 = file_path12;
    }

    public FormFile getFile_path13() {
        return file_path13;
    }

    public void setFile_path13(FormFile file_path13) {
        this.file_path13 = file_path13;
    }

    public FormFile getFile_path14() {
        return file_path14;
    }

    public void setFile_path14(FormFile file_path14) {
        this.file_path14 = file_path14;
    }

    public FormFile getFile_path15() {
        return file_path15;
    }

    public void setFile_path15(FormFile file_path15) {
        this.file_path15 = file_path15;
    }

    public String getIsinsuranceavailable() {
        return isinsuranceavailable;
    }

    public void setIsinsuranceavailable(String isinsuranceavailable) {
        this.isinsuranceavailable = isinsuranceavailable;
    }

    public String getQuoteno() {
        return quoteno;
    }

    public void setQuoteno(String quoteno) {
        this.quoteno = quoteno;
    }

    public String getCustomerreference() {
        return customerreference;
    }

    public void setCustomerreference(String customerreference) {
        this.customerreference = customerreference;
    }

    public String getInsuredemail() {
        return insuredemail;
    }

    public void setInsuredemail(String insuredemail) {
        this.insuredemail = insuredemail;
    }

    public String getACID() {
        return ACID;
    }

    public void setACID(String ACID) {
        this.ACID = ACID;
    }

    public String getCargoprotectid() {
        return cargoprotectid;
    }

    public void setCargoprotectid(String cargoprotectid) {
        this.cargoprotectid = cargoprotectid;
    }

    public String getEbook_NamedCustomername() {
        return ebook_NamedCustomername;
    }

    public void setEbook_NamedCustomername(String ebook_NamedCustomername) {
        this.ebook_NamedCustomername = ebook_NamedCustomername;
    }

    public String getEbook_NamedCustomerstate() {
        return ebook_NamedCustomerstate;
    }

    public void setEbook_NamedCustomerstate(String ebook_NamedCustomerstate) {
        this.ebook_NamedCustomerstate = ebook_NamedCustomerstate;
    }

    public String getEbook_NamedCustomer_id() {
        return ebook_NamedCustomer_id;
    }

    public void setEbook_NamedCustomer_id(String ebook_NamedCustomer_id) {
        this.ebook_NamedCustomer_id = ebook_NamedCustomer_id;
    }

    public String getEbook_NamedCustomercode() {
        return ebook_NamedCustomercode;
    }

    public void setEbook_NamedCustomercode(String ebook_NamedCustomercode) {
        this.ebook_NamedCustomercode = ebook_NamedCustomercode;
    }

    public String getEbook_NamedCustomerzipcode() {
        return ebook_NamedCustomerzipcode;
    }

    public void setEbook_NamedCustomerzipcode(String ebook_NamedCustomerzipcode) {
        this.ebook_NamedCustomerzipcode = ebook_NamedCustomerzipcode;
    }

    public String getEbook_NamedCustomeraddress() {
        return ebook_NamedCustomeraddress;
    }

    public void setEbook_NamedCustomeraddress(String ebook_NamedCustomeraddress) {
        this.ebook_NamedCustomeraddress = ebook_NamedCustomeraddress;
    }

    public String getEbook_NamedCustomercountry() {
        return ebook_NamedCustomercountry;
    }

    public void setEbook_NamedCustomercountry(String ebook_NamedCustomercountry) {
        this.ebook_NamedCustomercountry = ebook_NamedCustomercountry;
    }

    public String getEbook_NamedCustomercountrycode() {
        return ebook_NamedCustomercountrycode;
    }

    public void setEbook_NamedCustomercountrycode(String ebook_NamedCustomercountrycode) {
        this.ebook_NamedCustomercountrycode = ebook_NamedCustomercountrycode;
    }

    public String getEbook_NamedCustomercity() {
        return ebook_NamedCustomercity;
    }

    public void setEbook_NamedCustomercity(String ebook_NamedCustomercity) {
        this.ebook_NamedCustomercity = ebook_NamedCustomercity;
    }

    public String getEbook_NamedCustomerphone() {
        return ebook_NamedCustomerphone;
    }

    public void setEbook_NamedCustomerphone(String ebook_NamedCustomerphone) {
        this.ebook_NamedCustomerphone = ebook_NamedCustomerphone;
    }

    public String getEbook_NamedCustomerfax() {
        return ebook_NamedCustomerfax;
    }

    public void setEbook_NamedCustomerfax(String ebook_NamedCustomerfax) {
        this.ebook_NamedCustomerfax = ebook_NamedCustomerfax;
    }

    public String getEbook_NamedCustomeremail() {
        return ebook_NamedCustomeremail;
    }

    public void setEbook_NamedCustomeremail(String ebook_NamedCustomeremail) {
        this.ebook_NamedCustomeremail = ebook_NamedCustomeremail;
    }

    public String getEbook_NamedCustomercountry_desc() {
        return ebook_NamedCustomercountry_desc;
    }

    public void setEbook_NamedCustomercountry_desc(String ebook_NamedCustomercountry_desc) {
        this.ebook_NamedCustomercountry_desc = ebook_NamedCustomercountry_desc;
    }

    public String getEbook_NamedCustomercity_desc() {
        return ebook_NamedCustomercity_desc;
    }

    public void setEbook_NamedCustomercity_desc(String ebook_NamedCustomercity_desc) {
        this.ebook_NamedCustomercity_desc = ebook_NamedCustomercity_desc;
    }

    public String getEbook_NamedCustomeraddress1() {
        return ebook_NamedCustomeraddress1;
    }

    public void setEbook_NamedCustomeraddress1(String ebook_NamedCustomeraddress1) {
        this.ebook_NamedCustomeraddress1 = ebook_NamedCustomeraddress1;
    }

    public String getEbook_NamedCustomercountry_name() {
        return ebook_NamedCustomercountry_name;
    }

    public void setEbook_NamedCustomercountry_name(String ebook_NamedCustomercountry_name) {
        this.ebook_NamedCustomercountry_name = ebook_NamedCustomercountry_name;
    }

    public String getEbook_NamedCustomercity_hdn() {
        return ebook_NamedCustomercity_hdn;
    }

    public void setEbook_NamedCustomercity_hdn(String ebook_NamedCustomercity_hdn) {
        this.ebook_NamedCustomercity_hdn = ebook_NamedCustomercity_hdn;
    }

    public String getEbook_NamedCustomerphone1() {
        return ebook_NamedCustomerphone1;
    }

    public void setEbook_NamedCustomerphone1(String ebook_NamedCustomerphone1) {
        this.ebook_NamedCustomerphone1 = ebook_NamedCustomerphone1;
    }

    public String getEbook_NamedCustomerphone2() {
        return ebook_NamedCustomerphone2;
    }

    public void setEbook_NamedCustomerphone2(String ebook_NamedCustomerphone2) {
        this.ebook_NamedCustomerphone2 = ebook_NamedCustomerphone2;
    }

    public String getEbook_NamedCustomerphone3() {
        return ebook_NamedCustomerphone3;
    }

    public void setEbook_NamedCustomerphone3(String ebook_NamedCustomerphone3) {
        this.ebook_NamedCustomerphone3 = ebook_NamedCustomerphone3;
    }

    public String getEbook_NamedCustomerfax1() {
        return ebook_NamedCustomerfax1;
    }

    public void setEbook_NamedCustomerfax1(String ebook_NamedCustomerfax1) {
        this.ebook_NamedCustomerfax1 = ebook_NamedCustomerfax1;
    }

    public String getEbook_NamedCustomerfax2() {
        return ebook_NamedCustomerfax2;
    }

    public void setEbook_NamedCustomerfax2(String ebook_NamedCustomerfax2) {
        this.ebook_NamedCustomerfax2 = ebook_NamedCustomerfax2;
    }

    public String getEbook_NamedCustomerfax3() {
        return ebook_NamedCustomerfax3;
    }

    public void setEbook_NamedCustomerfax3(String ebook_NamedCustomerfax3) {
        this.ebook_NamedCustomerfax3 = ebook_NamedCustomerfax3;
    }

    public String getShipperReference() {
        return shipperReference;
    }

    public void setShipperReference(String shipperReference) {
        this.shipperReference = shipperReference;
    }

    public String getAgreetypeperson() {
        return agreetypeperson;
    }

    public void setAgreetypeperson(String agreetypeperson) {
        this.agreetypeperson = agreetypeperson;
    }

    public String getConsigneetypeperson() {
        return consigneetypeperson;
    }

    public void setConsigneetypeperson(String consigneetypeperson) {
        this.consigneetypeperson = consigneetypeperson;
    }

    public String getNotifytypeperson() {
        return notifytypeperson;
    }

    public void setNotifytypeperson(String notifytypeperson) {
        this.notifytypeperson = notifytypeperson;
    }

    public String getNotifytypeperson_n1() {
        return notifytypeperson_n1;
    }

    public void setNotifytypeperson_n1(String notifytypeperson_n1) {
        this.notifytypeperson_n1 = notifytypeperson_n1;
    }

    public String getWarehousetypeperson() {
        return Warehousetypeperson;
    }

    public void setWarehousetypeperson(String Warehousetypeperson) {
        this.Warehousetypeperson = Warehousetypeperson;
    }

    public String getForwardertypeperson() {
        return forwardertypeperson;
    }

    public void setForwardertypeperson(String forwardertypeperson) {
        this.forwardertypeperson = forwardertypeperson;
    }

    public String getShippertypeperson() {
        return shippertypeperson;
    }

    public void setShippertypeperson(String shippertypeperson) {
        this.shippertypeperson = shippertypeperson;
    }

    public String getEbook_NamedCustomereori() {
        return ebook_NamedCustomereori;
    }

    public void setEbook_NamedCustomereori(String ebook_NamedCustomereori) {
        this.ebook_NamedCustomereori = ebook_NamedCustomereori;
    }

    public String getEbook_NamedCustomerpersontype() {
        return ebook_NamedCustomerpersontype;
    }

    public void setEbook_NamedCustomerpersontype(String ebook_NamedCustomerpersontype) {
        this.ebook_NamedCustomerpersontype = ebook_NamedCustomerpersontype;
    }

    public String getEbook_Sellercity() {
        return ebook_Sellercity;
    }

    public void setEbook_Sellercity(String ebook_Sellercity) {
        this.ebook_Sellercity = ebook_Sellercity;
    }

    public String getEbook_Sellercity_hdn() {
        return ebook_Sellercity_hdn;
    }

    public void setEbook_Sellercity_hdn(String ebook_Sellercity_hdn) {
        this.ebook_Sellercity_hdn = ebook_Sellercity_hdn;
    }

    public String getEbook_Sellercountry() {
        return ebook_Sellercountry;
    }

    public void setEbook_Sellercountry(String ebook_Sellercountry) {
        this.ebook_Sellercountry = ebook_Sellercountry;
    }

    public String getEbook_Sellername() {
        return ebook_Sellername;
    }

    public void setEbook_Sellername(String ebook_Sellername) {
        this.ebook_Sellername = ebook_Sellername;
    }

    public String getEbook_Selleraddress() {
        return ebook_Selleraddress;
    }

    public void setEbook_Selleraddress(String ebook_Selleraddress) {
        this.ebook_Selleraddress = ebook_Selleraddress;
    }

    public String getEbook_Selleraddress1() {
        return ebook_Selleraddress1;
    }

    public void setEbook_Selleraddress1(String ebook_Selleraddress1) {
        this.ebook_Selleraddress1 = ebook_Selleraddress1;
    }

    public String getEbook_Sellercity_desc() {
        return ebook_Sellercity_desc;
    }

    public void setEbook_Sellercity_desc(String ebook_Sellercity_desc) {
        this.ebook_Sellercity_desc = ebook_Sellercity_desc;
    }

    public String getEbook_Sellercountry_name() {
        return ebook_Sellercountry_name;
    }

    public void setEbook_Sellercountry_name(String ebook_Sellercountry_name) {
        this.ebook_Sellercountry_name = ebook_Sellercountry_name;
    }

    public String getSellerSelect() {
        return SellerSelect;
    }

    public void setSellerSelect(String SellerSelect) {
        this.SellerSelect = SellerSelect;
    }

    public String getEbook_Sellerphone() {
        return ebook_Sellerphone;
    }

    public void setEbook_Sellerphone(String ebook_Sellerphone) {
        this.ebook_Sellerphone = ebook_Sellerphone;
    }

    public String getEbook_Sellerfax() {
        return ebook_Sellerfax;
    }

    public void setEbook_Sellerfax(String ebook_Sellerfax) {
        this.ebook_Sellerfax = ebook_Sellerfax;
    }

    public String getEbook_Selleremail() {
        return ebook_Selleremail;
    }

    public void setEbook_Selleremail(String ebook_Selleremail) {
        this.ebook_Selleremail = ebook_Selleremail;
    }

    public String getEbook_Sellerstate() {
        return ebook_Sellerstate;
    }

    public void setEbook_Sellerstate(String ebook_Sellerstate) {
        this.ebook_Sellerstate = ebook_Sellerstate;
    }

    public String getEbook_Sellereori() {
        return ebook_Sellereori;
    }

    public void setEbook_Sellereori(String ebook_Sellereori) {
        this.ebook_Sellereori = ebook_Sellereori;
    }

    public String getEbook_Sellerpersontype() {
        return ebook_Sellerpersontype;
    }

    public void setEbook_Sellerpersontype(String ebook_Sellerpersontype) {
        this.ebook_Sellerpersontype = ebook_Sellerpersontype;
    }

    public String getBuyerSelect() {
        return BuyerSelect;
    }

    public void setBuyerSelect(String BuyerSelect) {
        this.BuyerSelect = BuyerSelect;
    }

    public String getEbook_Buyername() {
        return ebook_Buyername;
    }

    public void setEbook_Buyername(String ebook_Buyername) {
        this.ebook_Buyername = ebook_Buyername;
    }

    public String getEbook_Buyeraddress() {
        return ebook_Buyeraddress;
    }

    public void setEbook_Buyeraddress(String ebook_Buyeraddress) {
        this.ebook_Buyeraddress = ebook_Buyeraddress;
    }

    public String getEbook_Buyeraddress1() {
        return ebook_Buyeraddress1;
    }

    public void setEbook_Buyeraddress1(String ebook_Buyeraddress1) {
        this.ebook_Buyeraddress1 = ebook_Buyeraddress1;
    }

    public String getEbook_Buyercity_desc() {
        return ebook_Buyercity_desc;
    }

    public void setEbook_Buyercity_desc(String ebook_Buyercity_desc) {
        this.ebook_Buyercity_desc = ebook_Buyercity_desc;
    }

    public String getEbook_Buyercountry_name() {
        return ebook_Buyercountry_name;
    }

    public void setEbook_Buyercountry_name(String ebook_Buyercountry_name) {
        this.ebook_Buyercountry_name = ebook_Buyercountry_name;
    }

    public String getEbook_Buyerphone() {
        return ebook_Buyerphone;
    }

    public void setEbook_Buyerphone(String ebook_Buyerphone) {
        this.ebook_Buyerphone = ebook_Buyerphone;
    }

    public String getEbook_Buyerfax() {
        return ebook_Buyerfax;
    }

    public void setEbook_Buyerfax(String ebook_Buyerfax) {
        this.ebook_Buyerfax = ebook_Buyerfax;
    }

    public String getEbook_Buyeremail() {
        return ebook_Buyeremail;
    }

    public void setEbook_Buyeremail(String ebook_Buyeremail) {
        this.ebook_Buyeremail = ebook_Buyeremail;
    }

    public String getEbook_Buyerstate() {
        return ebook_Buyerstate;
    }

    public void setEbook_Buyerstate(String ebook_Buyerstate) {
        this.ebook_Buyerstate = ebook_Buyerstate;
    }

    public String getEbook_Buyereori() {
        return ebook_Buyereori;
    }

    public void setEbook_Buyereori(String ebook_Buyereori) {
        this.ebook_Buyereori = ebook_Buyereori;
    }

    public String getEbook_Buyerpersontype() {
        return ebook_Buyerpersontype;
    }

    public void setEbook_Buyerpersontype(String ebook_Buyerpersontype) {
        this.ebook_Buyerpersontype = ebook_Buyerpersontype;
    }

    public String getEbook_Buyercity() {
        return ebook_Buyercity;
    }

    public void setEbook_Buyercity(String ebook_Buyercity) {
        this.ebook_Buyercity = ebook_Buyercity;
    }

    public String getEbook_Buyercity_hdn() {
        return ebook_Buyercity_hdn;
    }

    public void setEbook_Buyercity_hdn(String ebook_Buyercity_hdn) {
        this.ebook_Buyercity_hdn = ebook_Buyercity_hdn;
    }

    public String getEbook_Buyercountry() {
        return ebook_Buyercountry;
    }

    public void setEbook_Buyercountry(String ebook_Buyercountry) {
        this.ebook_Buyercountry = ebook_Buyercountry;
    }

    public String getEbook_Buyercode() {
        return ebook_Buyercode;
    }

    public void setEbook_Buyercode(String ebook_Buyercode) {
        this.ebook_Buyercode = ebook_Buyercode;
    }

    public String getEbook_Buyercountry_desc() {
        return ebook_Buyercountry_desc;
    }

    public void setEbook_Buyercountry_desc(String ebook_Buyercountry_desc) {
        this.ebook_Buyercountry_desc = ebook_Buyercountry_desc;
    }

    public String getEbook_Sellerphone1() {
        return ebook_Sellerphone1;
    }

    public void setEbook_Sellerphone1(String ebook_Sellerphone1) {
        this.ebook_Sellerphone1 = ebook_Sellerphone1;
    }

    public String getEbook_Sellerphone2() {
        return ebook_Sellerphone2;
    }

    public void setEbook_Sellerphone2(String ebook_Sellerphone2) {
        this.ebook_Sellerphone2 = ebook_Sellerphone2;
    }

    public String getEbook_Sellerphone3() {
        return ebook_Sellerphone3;
    }

    public void setEbook_Sellerphone3(String ebook_Sellerphone3) {
        this.ebook_Sellerphone3 = ebook_Sellerphone3;
    }

    public String getEbook_Sellerfax1() {
        return ebook_Sellerfax1;
    }

    public void setEbook_Sellerfax1(String ebook_Sellerfax1) {
        this.ebook_Sellerfax1 = ebook_Sellerfax1;
    }

    public String getEbook_Sellerfax2() {
        return ebook_Sellerfax2;
    }

    public void setEbook_Sellerfax2(String ebook_Sellerfax2) {
        this.ebook_Sellerfax2 = ebook_Sellerfax2;
    }

    public String getEbook_Sellerfax3() {
        return ebook_Sellerfax3;
    }

    public void setEbook_Sellerfax3(String ebook_Sellerfax3) {
        this.ebook_Sellerfax3 = ebook_Sellerfax3;
    }

    public String getEbook_Buyerphone1() {
        return ebook_Buyerphone1;
    }

    public void setEbook_Buyerphone1(String ebook_Buyerphone1) {
        this.ebook_Buyerphone1 = ebook_Buyerphone1;
    }

    public String getEbook_Buyerfax1() {
        return ebook_Buyerfax1;
    }

    public void setEbook_Buyerfax1(String ebook_Buyerfax1) {
        this.ebook_Buyerfax1 = ebook_Buyerfax1;
    }

    public String getEbook_Buyerphone2() {
        return ebook_Buyerphone2;
    }

    public void setEbook_Buyerphone2(String ebook_Buyerphone2) {
        this.ebook_Buyerphone2 = ebook_Buyerphone2;
    }

    public String getEbook_Buyerfax2() {
        return ebook_Buyerfax2;
    }

    public void setEbook_Buyerfax2(String ebook_Buyerfax2) {
        this.ebook_Buyerfax2 = ebook_Buyerfax2;
    }

    public String getEbook_Buyerphone3() {
        return ebook_Buyerphone3;
    }

    public void setEbook_Buyerphone3(String ebook_Buyerphone3) {
        this.ebook_Buyerphone3 = ebook_Buyerphone3;
    }

    public String getEbook_Buyerfax3() {
        return ebook_Buyerfax3;
    }

    public void setEbook_Buyerfax3(String ebook_Buyerfax3) {
        this.ebook_Buyerfax3 = ebook_Buyerfax3;
    }

    public String getEbook_Sellercode() {
        return ebook_Sellercode;
    }

    public void setEbook_Sellercode(String ebook_Sellercode) {
        this.ebook_Sellercode = ebook_Sellercode;
    }

    public String getEbook_Seller_id() {
        return ebook_Seller_id;
    }

    public void setEbook_Seller_id(String ebook_Seller_id) {
        this.ebook_Seller_id = ebook_Seller_id;
    }

    public String getEbook_Buyer_id() {
        return ebook_Buyer_id;
    }

    public void setEbook_Buyer_id(String ebook_Buyer_id) {
        this.ebook_Buyer_id = ebook_Buyer_id;
    }

    public String getEbook_Sellercountry_desc() {
        return ebook_Sellercountry_desc;
    }

    public void setEbook_Sellercountry_desc(String ebook_Sellercountry_desc) {
        this.ebook_Sellercountry_desc = ebook_Sellercountry_desc;
    }

    public String getEnsfiling() {
        return ensfiling;
    }

    public void setEnsfiling(String ensfiling) {
        this.ensfiling = ensfiling;
    }

    public String getEbook_preferred_agency() {
        return ebook_preferred_agency;
    }

    public void setEbook_preferred_agency(String ebook_preferred_agency) {
        this.ebook_preferred_agency = ebook_preferred_agency;
    }

    public String getEbook_t2lfiling() {
        return ebook_t2lfiling;
    }

    public void setEbook_t2lfiling(String ebook_t2lfiling) {
        this.ebook_t2lfiling = ebook_t2lfiling;
    }

    public String getEbook_sisubmit_party() {
        return ebook_sisubmit_party;
    }

    public void setEbook_sisubmit_party(String ebook_sisubmit_party) {
        this.ebook_sisubmit_party = ebook_sisubmit_party;
    }

    public ENSDeclarantVo getEnsdecvo() {
        return ensdecvo;
    }

    public void setEnsdecvo(ENSDeclarantVo ensdecvo) {
        this.ensdecvo = ensdecvo;
    }

    public String getEuCustZone() {
        return euCustZone;
    }

    public void setEuCustZone(String euCustZone) {
        this.euCustZone = euCustZone;
    }

    public String getBlTypeENS() {
        return blTypeENS;
    }

    public void setBlTypeENS(String blTypeENS) {
        this.blTypeENS = blTypeENS;
    }

    public String getEnsFillingType() {
        return ensFillingType;
    }

    public void setEnsFillingType(String ensFillingType) {
        this.ensFillingType = ensFillingType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeclarantName() {
        return declarantName;
    }

    public void setDeclarantName(String declarantName) {
        this.declarantName = declarantName;
    }

    public String getDeclarantAddress() {
        return declarantAddress;
    }

    public void setDeclarantAddress(String declarantAddress) {
        this.declarantAddress = declarantAddress;
    }

    public String getDeclarantAddress1() {
        return declarantAddress1;
    }

    public void setDeclarantAddress1(String declarantAddress1) {
        this.declarantAddress1 = declarantAddress1;
    }

    public String getDeclarantCity() {
        return declarantCity;
    }

    public void setDeclarantCity(String declarantCity) {
        this.declarantCity = declarantCity;
    }

    public String getDeclarantCountry() {
        return declarantCountry;
    }

    public void setDeclarantCountry(String declarantCountry) {
        this.declarantCountry = declarantCountry;
    }

    public String getDeclarantState() {
        return declarantState;
    }

    public void setDeclarantState(String declarantState) {
        this.declarantState = declarantState;
    }

    public String getDeclarantTelephone() {
        return declarantTelephone;
    }

    public void setDeclarantTelephone(String declarantTelephone) {
        this.declarantTelephone = declarantTelephone;
    }

    public String getDeclarantEORI() {
        return declarantEORI;
    }

    public void setDeclarantEORI(String declarantEORI) {
        this.declarantEORI = declarantEORI;
    }

    public String getDeclarantEmail() {
        return declarantEmail;
    }

    public void setDeclarantEmail(String declarantEmail) {
        this.declarantEmail = declarantEmail;
    }

    public String getDeclarantFillingType() {
        return declarantFillingType;
    }

    public void setDeclarantFillingType(String declarantFillingType) {
        this.declarantFillingType = declarantFillingType;
    }

    public String getShipperReferenceType() {
        return shipperReferenceType;
    }

    public void setShipperReferenceType(String shipperReferenceType) {
        this.shipperReferenceType = shipperReferenceType;
    }



}
