/*
 * EbookingValidatorFormBean.java
 *
 * Created on April 18, 2008, 10:04 AM
 */
package net.ecom.vo;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author senthilkumar
 * @version
 */
public class EbookingValidatorFormBean implements Serializable  {
    
    private String name;
    private String booking_refno;
    private int number;
    private String ebook_date;
    private String ebook_carrier;
    private String ebook_bookagency;
    private String ebook_bookagencyname;
    private String ebook_ratetype;
    private String ebook_raterefno;
    private String ebook_portofloading;
    private String ebook_portofdischarge;
    private String ebook_servicecode;
    private String ebooking_service;
    private String ebook_lineservicecode;
    private String ebook_lineservice;
    private String ebook_vesselcode;
    private String ebooking_vessel;
    private String ebook_linevesselcode;
    private String ebook_linevessel;
    private String ebook_voyage;
    private String ebook_bound_leg;
    private String ebook_linebound_leg;
    private String ebook_linevoyage;
    private String ebook_origin;
    private String ebook_origin_country;
    private String ebook_pol_country;
    private String ebook_pol;
    private String ebook_terminal;
    private String ebook_terminal_country;
    private String ebook_pol_etd;
    private String ebook_pod;
    private String ebook_pod_country;
    private String ebook_terminalpod;
    private String ebook_terminalpod_country;
    private String ebook_pol_eta;
    private String ebook_delivery;
    private String ebook_delivery_country;
    private String ebook_bookready;
    private String ebook_cutoff;
    private String ebook_oncarriage="on_carriage";
    private String ebook_precarriage="on_carriage";
    private String ebook_status="Draft";
    private String ebook_customertype;
    private String ebook_customer;
    private String ebook_customercity;
    private String ebook_custcountry;
    private String ebook_custcountrycode;
    private String ebook_customername;
    private String ebook_countryzip;
    private String ebook_customeraddress;
    private String ebook_telephone;
    private String ebook_email;
    private String ebook_custtelephone;
    private String ebook_submit_add;
    private String ebook_trans_movement;
    private String ebook_equipment;
    private String ebook_equipment_name;    
    private String ebook_carrierseal;
    private String ebook_shipperseal;
    private String ebook_shipperowned;
    private String ebook_ventilation;
    private String ebook_equipstatus;
    private String ebook_containerposition;
    private String ebook_oog;
    private String ebook_chasis;
    private String ebook_tareweight;
    private String ebook_weightunit;
    private String ebooking_nor;
    private String ebooking_reefer;
    private String ebook_minimumtemp;
    private String ebook_tempunit;
    private String ebook_settemp;
    private String ebook_maxitemp;
    private String ebook_genset;
    private String ebook_quantity;
    private String ebook_packcode;
    private String ebooking_package;
    private String ebook_commoditycode;
    private String ebooking_commodity;
    private String ebook_commodityweight;
    private String ebook_commodityweightunit;
    private String ebook_commodityvolume;
    private String ebook_commodityvolumeunit;
    private String ebook_hazardous;
    private String ebook_hazardouscontact;
    private String ebook_telephonecontact;
    private String ebook_dimensionleft;
    private String ebook_dimensionright;
    private String ebook_dimensionunit;
    private String ebook_dimensionforward;
    private String ebook_dimensionalt;
    private String ebook_dimensiontop;
    private String ebook_cargoweight;
    private String ebook_grossweight;
    private String ebook_equipmentid_commodity;
    private String cust_flag;
    private String cont_flag;
    private String comm_flag;
    private String ebook_userstatus;
    private String ebook_status_remarks;   
    private String ebook_templateno;
    private String ebook_no_containers;
    private String ebook_templatename;
    
    private String ebook_custtype_desc;
    private String ebook_equiptype_desc;
    private String ebook_package_desc;
    private String ebook_commodity_desc;
    private String ebook_contposition_desc;
    private String ebook_loadcondn_desc;
    private String ebook_equipstat_desc;
    private String ebook_oog_desc;
    private String ebook_hazardous_desc;
    private String ebook_shipown_desc;
     //addeed by gnanamoorthi.s
     private String hdntempaltename;
    
    /**
     *
     * @return
     */
    
    public String getName() {
        return name;
    }
    
    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }
    
    /**
     * @return
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * @param i
     */
    public void setNumber(int i) {
        number = i;
    }
    
    /**
     *
     */
    public EbookingValidatorFormBean() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public String getBooking_refno() {
        return booking_refno;
    }
    
    public void setBooking_refno(String booking_refno) {
        this.booking_refno = booking_refno;      
    }
    
    public String getEbook_date() {
        return ebook_date;
    }
    
    public void setEbook_date(String ebook_date) {
        this.ebook_date = ebook_date;
    }
    
    public String getEbook_carrier() {
        return ebook_carrier;
    }
    
    public void setEbook_carrier(String ebook_carrier) {
        this.ebook_carrier = ebook_carrier;
    }
    
    public String getEbook_bookagency() {
        return ebook_bookagency;
    }
    
    public void setEbook_bookagency(String ebook_bookagency) {
        this.ebook_bookagency = ebook_bookagency;
    }
    
    public String getEbook_ratetype() {
        return ebook_ratetype;
    }
    
    public void setEbook_ratetype(String ebook_ratetype) {
        this.ebook_ratetype = ebook_ratetype;
    }
    
    public String getEbook_raterefno() {
        return ebook_raterefno;
    }
    
    public void setEbook_raterefno(String ebook_raterefno) {
        this.ebook_raterefno = ebook_raterefno;
    }
    
    public String getEbook_portofloading() {
        return ebook_portofloading;
    }
    
    public void setEbook_portofloading(String ebook_portofloading) {
        this.ebook_portofloading = ebook_portofloading;
    }
    
    public String getEbook_portofdischarge() {
        return ebook_portofdischarge;
    }
    
    public void setEbook_portofdischarge(String ebook_portofdischarge) {
        this.ebook_portofdischarge = ebook_portofdischarge;
    }
    
    public String getEbook_servicecode() {
        return ebook_servicecode;
    }
    
    public void setEbook_servicecode(String ebook_servicecode) {
        this.ebook_servicecode = ebook_servicecode;
    }
    
    public String getEbooking_service() {
        return ebooking_service;
    }
    
    public void setEbooking_service(String ebooking_service) {
        this.ebooking_service = ebooking_service;
    }
    
    public String getEbook_lineservicecode() {
        return ebook_lineservicecode;
    }
    
    public void setEbook_lineservicecode(String ebook_lineservicecode) {
        this.ebook_lineservicecode = ebook_lineservicecode;
    }
    
    public String getEbook_lineservice() {
        return ebook_lineservice;
    }
    
    public void setEbook_lineservice(String ebook_lineservice) {
        this.ebook_lineservice = ebook_lineservice;
    }
    
    public String getEbook_vesselcode() {
        return ebook_vesselcode;
    }
    
    public void setEbook_vesselcode(String ebook_vesselcode) {
        this.ebook_vesselcode = ebook_vesselcode;
    }
    
    public String getEbooking_vessel() {
        return ebooking_vessel;
    }
    
    public void setEbooking_vessel(String ebooking_vessel) {
        this.ebooking_vessel = ebooking_vessel;
    }
    
    public String getEbook_linevesselcode() {
        return ebook_linevesselcode;
    }
    
    public void setEbook_linevesselcode(String ebook_linevesselcode) {
        this.ebook_linevesselcode = ebook_linevesselcode;
    }
    
    public String getEbook_linevessel() {
        return ebook_linevessel;
    }
    
    public void setEbook_linevessel(String ebook_linevessel) {
        this.ebook_linevessel = ebook_linevessel;
    }
    
    public String getEbook_voyage() {
        return ebook_voyage;
    }
    
    public void setEbook_voyage(String ebook_voyage) {
        this.ebook_voyage = ebook_voyage;
    }
    
    public String getEbook_bound_leg() {
        return ebook_bound_leg;
    }
    
    public void setEbook_bound_leg(String ebook_bound_leg) {
        this.ebook_bound_leg = ebook_bound_leg;
    }
    
    public String getEbook_linebound_leg() {
        return ebook_linebound_leg;
    }
    
    public void setEbook_linebound_leg(String ebook_linebound_leg) {
        this.ebook_linebound_leg = ebook_linebound_leg;
    }
    
    public String getEbook_linevoyage() {
        return ebook_linevoyage;
    }
    
    public void setEbook_linevoyage(String ebook_linevoyage) {
        this.ebook_linevoyage = ebook_linevoyage;
    }
    
    public String getEbook_origin() {
        return ebook_origin;
    }
    
    public void setEbook_origin(String ebook_origin) {
        this.ebook_origin = ebook_origin;
    }

    public String getEbook_origin_country() {
        return ebook_origin_country;
    }

    public void setEbook_origin_country(String ebook_origin_country) {
        this.ebook_origin_country = ebook_origin_country;
    }

    public String getEbook_pol_country() {
        return ebook_pol_country;
    }

    public void setEbook_pol_country(String ebook_pol_country) {
        this.ebook_pol_country = ebook_pol_country;
    }

    public String getEbook_pol() {
        return ebook_pol;
    }

    public void setEbook_pol(String ebook_pol) {
        this.ebook_pol = ebook_pol;
    }

    public String getEbook_terminal() {
        return ebook_terminal;
    }

    public void setEbook_terminal(String ebook_terminal) {
        this.ebook_terminal = ebook_terminal;
    }

    public String getEbook_terminal_country() {
        return ebook_terminal_country;
    }

    public void setEbook_terminal_country(String ebook_terminal_country) {
        this.ebook_terminal_country = ebook_terminal_country;
    }

    public String getEbook_pol_etd() {
        return ebook_pol_etd;
    }

    public void setEbook_pol_etd(String ebook_pol_etd) {
        this.ebook_pol_etd = ebook_pol_etd;
    }

    public String getEbook_pod() {
        return ebook_pod;
    }

    public void setEbook_pod(String ebook_pod) {
        this.ebook_pod = ebook_pod;
    }

    public String getEbook_pod_country() {
        return ebook_pod_country;
    }

    public void setEbook_pod_country(String ebook_pod_country) {
        this.ebook_pod_country = ebook_pod_country;
    }

    public String getEbook_terminalpod() {
        return ebook_terminalpod;
    }

    public void setEbook_terminalpod(String ebook_terminalpod) {
        this.ebook_terminalpod = ebook_terminalpod;
    }

    public String getEbook_terminalpod_country() {
        return ebook_terminalpod_country;
    }

    public void setEbook_terminalpod_country(String ebook_terminalpod_country) {
        this.ebook_terminalpod_country = ebook_terminalpod_country;
    }

    public String getEbook_pol_eta() {
        return ebook_pol_eta;
    }

    public void setEbook_pol_eta(String ebook_pol_eta) {
        this.ebook_pol_eta = ebook_pol_eta;
    }

    public String getEbook_delivery() {
        return ebook_delivery;
    }

    public void setEbook_delivery(String ebook_delivery) {
        this.ebook_delivery = ebook_delivery;
    }

    public String getEbook_delivery_country() {
        return ebook_delivery_country;
    }

    public void setEbook_delivery_country(String ebook_delivery_country) {
        this.ebook_delivery_country = ebook_delivery_country;
    }

    public String getEbook_bookready() {
        return ebook_bookready;
    }

    public void setEbook_bookready(String ebook_bookready) {
        this.ebook_bookready = ebook_bookready;
    }

    public String getEbook_cutoff() {
        return ebook_cutoff;
    }

    public void setEbook_cutoff(String ebook_cutoff) {
        this.ebook_cutoff = ebook_cutoff;
    }

    public String getEbook_oncarriage() {
        return ebook_oncarriage;
    }

    public void setEbook_oncarriage(String ebook_oncarriage) {
        this.ebook_oncarriage = ebook_oncarriage;
    }

    public String getEbook_precarriage() {
        return ebook_precarriage;
    }

    public void setEbook_precarriage(String ebook_precarriage) {
        this.ebook_precarriage = ebook_precarriage;
    }

    public String getEbook_status() {
        return ebook_status;
    }

    public void setEbook_status(String ebook_status) {
        this.ebook_status = ebook_status;
    }

    public String getEbook_customertype() {
       // System.out.println("[ECOM] EbookingValidatorFormBean ebook"+ebook_customertype);
        return ebook_customertype;
    }

    public void setEbook_customertype(String ebook_customertype) {
        this.ebook_customertype = ebook_customertype;
        //System.out.println("[ECOM] EbookingValidatorFormBean sdeting cust type"+ebook_customertype);
    }

    public String getEbook_customer() {
        return ebook_customer;
    }

    public void setEbook_customer(String ebook_customer) {
        this.ebook_customer = ebook_customer;
    }

    public String getEbook_customercity() {
        return ebook_customercity;
    }

    public void setEbook_customercity(String ebook_customercity) {
        this.ebook_customercity = ebook_customercity;
    }

    public String getEbook_custcountry() {
        return ebook_custcountry;
    }

    public void setEbook_custcountry(String ebook_custcountry) {
        this.ebook_custcountry = ebook_custcountry;
    }

    public String getEbook_custcountrycode() {
        return ebook_custcountrycode;
    }

    public void setEbook_custcountrycode(String ebook_custcountrycode) {
        this.ebook_custcountrycode = ebook_custcountrycode;
    }

    public String getEbook_customername() {
        return ebook_customername;
    }

    public void setEbook_customername(String ebook_customername) {
        this.ebook_customername = ebook_customername;
    }

    public String getEbook_countryzip() {
        return ebook_countryzip;
    }

    public void setEbook_countryzip(String ebook_countryzip) {
        this.ebook_countryzip = ebook_countryzip;
    }

    public String getEbook_customeraddress() {
        return ebook_customeraddress;
    }

    public void setEbook_customeraddress(String ebook_customeraddress) {
        this.ebook_customeraddress = ebook_customeraddress;
    }

    public String getEbook_telephone() {
        return ebook_telephone;
    }

    public void setEbook_telephone(String ebook_telephone) {
        this.ebook_telephone = ebook_telephone;
    }

    public String getEbook_email() {
        return ebook_email;
    }

    public void setEbook_email(String ebook_email) {
        this.ebook_email = ebook_email;
    }

    public String getEbook_custtelephone() {
        return ebook_custtelephone;
    }

    public void setEbook_custtelephone(String ebook_custtelephone) {
        this.ebook_custtelephone = ebook_custtelephone;
    }

    public String getEbook_submit_add() {
        return ebook_submit_add;
    }

    public void setEbook_submit_add(String ebook_submit_add) {
        this.ebook_submit_add = ebook_submit_add;
    }

  
    public String getEbook_trans_movement() {
        return ebook_trans_movement;
    }

    public void setEbook_trans_movement(String ebook_trans_movement) {
        this.ebook_trans_movement = ebook_trans_movement;
    }

    public String getEbook_equipment() {
        return ebook_equipment;
    }

    public void setEbook_equipment(String ebook_equipment) {
        this.ebook_equipment = ebook_equipment;
    }

    public String getEbook_carrierseal() {
        return ebook_carrierseal;
    }

    public void setEbook_carrierseal(String ebook_carrierseal) {
        this.ebook_carrierseal = ebook_carrierseal;
    }

    public String getEbook_shipperseal() {
        return ebook_shipperseal;
    }

    public void setEbook_shipperseal(String ebook_shipperseal) {
        this.ebook_shipperseal = ebook_shipperseal;
    }

    public String getEbook_shipperowned() {
        return ebook_shipperowned;
    }

    public void setEbook_shipperowned(String ebook_shipperowned) {
        this.ebook_shipperowned = ebook_shipperowned;
    }

    public String getEbook_ventilation() {
        return ebook_ventilation;
    }

    public void setEbook_ventilation(String ebook_ventilation) {
        this.ebook_ventilation = ebook_ventilation;
    }

    public String getEbook_equipstatus() {
        return ebook_equipstatus;
    }

    public void setEbook_equipstatus(String ebook_equipstatus) {
        this.ebook_equipstatus = ebook_equipstatus;
    }

    public String getEbook_containerposition() {
        return ebook_containerposition;
    }

    public void setEbook_containerposition(String ebook_containerposition) {
        this.ebook_containerposition = ebook_containerposition;
    }

    public String getEbook_oog() {
        return ebook_oog;
    }

    public void setEbook_oog(String ebook_oog) {
        this.ebook_oog = ebook_oog;
    }

    public String getEbook_chasis() {
        return ebook_chasis;
    }

    public void setEbook_chasis(String ebook_chasis) {
        this.ebook_chasis = ebook_chasis;
    }

    public String getEbook_tareweight() {
        return ebook_tareweight;
    }

    public void setEbook_tareweight(String ebook_tareweight) {
        this.ebook_tareweight = ebook_tareweight;
    }

    public String getEbook_weightunit() {
        return ebook_weightunit;
    }

    public void setEbook_weightunit(String ebook_weightunit) {
        this.ebook_weightunit = ebook_weightunit;
    }

    public String getEbooking_nor() {
        return ebooking_nor;
    }

    public void setEbooking_nor(String ebooking_nor) {
        this.ebooking_nor = ebooking_nor;
    }

    public String getEbooking_reefer() {
        return ebooking_reefer;
    }

    public void setEbooking_reefer(String ebooking_reefer) {
        this.ebooking_reefer = ebooking_reefer;
    }

    public String getEbook_minimumtemp() {
        return ebook_minimumtemp;
    }

    public void setEbook_minimumtemp(String ebook_minimumtemp) {
        this.ebook_minimumtemp = ebook_minimumtemp;
    }

    public String getEbook_tempunit() {
        return ebook_tempunit;
    }

    public void setEbook_tempunit(String ebook_tempunit) {
        this.ebook_tempunit = ebook_tempunit;
    }

    public String getEbook_settemp() {
        return ebook_settemp;
    }

    public void setEbook_settemp(String ebook_settemp) {
        this.ebook_settemp = ebook_settemp;
    }

    public String getEbook_maxitemp() {
        return ebook_maxitemp;
    }

    public void setEbook_maxitemp(String ebook_maxitemp) {
        this.ebook_maxitemp = ebook_maxitemp;
    }

    public String getEbook_genset() {
        return ebook_genset;
    }

    public void setEbook_genset(String ebook_genset) {
        this.ebook_genset = ebook_genset;
    }

    public String getEbook_quantity() {
        return ebook_quantity;
    }

    public void setEbook_quantity(String ebook_quantity) {
        this.ebook_quantity = ebook_quantity;
    }

    public String getEbook_packcode() {
        return ebook_packcode;
    }

    public void setEbook_packcode(String ebook_packcode) {
        this.ebook_packcode = ebook_packcode;
    }

    public String getEbooking_package() {
        return ebooking_package;
    }

    public void setEbooking_package(String ebooking_package) {
        this.ebooking_package = ebooking_package;
    }

    public String getEbook_commoditycode() {
        return ebook_commoditycode;
    }

    public void setEbook_commoditycode(String ebook_commoditycode) {
        this.ebook_commoditycode = ebook_commoditycode;
    }

    public String getEbooking_commodity() {
        return ebooking_commodity;
    }

    public void setEbooking_commodity(String ebooking_commodity) {
        this.ebooking_commodity = ebooking_commodity;
    }

    public String getEbook_commodityweight() {
        return ebook_commodityweight;
    }

    public void setEbook_commodityweight(String ebook_commodityweight) {
        this.ebook_commodityweight = ebook_commodityweight;
    }

    public String getEbook_commodityweightunit() {
        return ebook_commodityweightunit;
    }

    public void setEbook_commodityweightunit(String ebook_commodityweightunit) {
        this.ebook_commodityweightunit = ebook_commodityweightunit;
    }

    public String getEbook_commodityvolume() {
        return ebook_commodityvolume;
    }

    public void setEbook_commodityvolume(String ebook_commodityvolume) {
        this.ebook_commodityvolume = ebook_commodityvolume;
    }

    public String getEbook_commodityvolumeunit() {
        return ebook_commodityvolumeunit;
    }

    public void setEbook_commodityvolumeunit(String ebook_commodityvolumeunit) {
        this.ebook_commodityvolumeunit = ebook_commodityvolumeunit;
    }

    public String getEbook_hazardous() {
        return ebook_hazardous;
    }

    public void setEbook_hazardous(String ebook_hazardous) {
        this.ebook_hazardous = ebook_hazardous;
    }

    public String getEbook_hazardouscontact() {
        return ebook_hazardouscontact;
    }

    public void setEbook_hazardouscontact(String ebook_hazardouscontact) {
        this.ebook_hazardouscontact = ebook_hazardouscontact;
    }

    public String getEbook_telephonecontact() {
        return ebook_telephonecontact;
    }

    public void setEbook_telephonecontact(String ebook_telephonecontact) {
        this.ebook_telephonecontact = ebook_telephonecontact;
    }

    public String getEbook_dimensionleft() {
        return ebook_dimensionleft;
    }

    public void setEbook_dimensionleft(String ebook_dimensionleft) {
        this.ebook_dimensionleft = ebook_dimensionleft;
    }

    public String getEbook_dimensionright() {
        return ebook_dimensionright;
    }

    public void setEbook_dimensionright(String ebook_dimensionright) {
        this.ebook_dimensionright = ebook_dimensionright;
    }

    public String getEbook_dimensionunit() {
        return ebook_dimensionunit;
    }

    public void setEbook_dimensionunit(String ebook_dimensionunit) {
        this.ebook_dimensionunit = ebook_dimensionunit;
    }

    public String getEbook_dimensionforward() {
        return ebook_dimensionforward;
    }

    public void setEbook_dimensionforward(String ebook_dimensionforward) {
        this.ebook_dimensionforward = ebook_dimensionforward;
    }

    public String getEbook_dimensionalt() {
        return ebook_dimensionalt;
    }

    public void setEbook_dimensionalt(String ebook_dimensionalt) {
        this.ebook_dimensionalt = ebook_dimensionalt;
    }

    public String getEbook_dimensiontop() {
        return ebook_dimensiontop;
    }

    public void setEbook_dimensiontop(String ebook_dimensiontop) {
        this.ebook_dimensiontop = ebook_dimensiontop;
    }

    public String getEbook_cargoweight() {
        return ebook_cargoweight;
    }

    public void setEbook_cargoweight(String ebook_cargoweight) {
        this.ebook_cargoweight = ebook_cargoweight;
    }

    public String getEbook_grossweight() {
        return ebook_grossweight;
    }

    public void setEbook_grossweight(String ebook_grossweight) {
        this.ebook_grossweight = ebook_grossweight;
    }

    public String getEbook_equipmentid_commodity() {
        return ebook_equipmentid_commodity;
    }

    public void setEbook_equipmentid_commodity(String ebook_equipmentid_commodity) {
        this.ebook_equipmentid_commodity = ebook_equipmentid_commodity;
    }

    public String getEbook_bookagencyname() {
        return ebook_bookagencyname;
    }

    public void setEbook_bookagencyname(String ebook_bookagencyname) {
        this.ebook_bookagencyname = ebook_bookagencyname;
    }

    public String getEbook_equipment_name() {
        return ebook_equipment_name;
    }

    public void setEbook_equipment_name(String ebook_equipment_name) {
        this.ebook_equipment_name = ebook_equipment_name;
    }
    public String getCust_flag() {
        return cust_flag;
    }

    public void setCust_flag(String cust_flag) {
        this.cust_flag = cust_flag;
    }

    public String getCont_flag() {
        return cont_flag;
    }

    public void setCont_flag(String cont_flag) {
        this.cont_flag = cont_flag;
    }

    public String getComm_flag() {
        return comm_flag;
    }

    public void setComm_flag(String comm_flag) {
        this.comm_flag = comm_flag;
    }
    public String getEbook_userstatus() {
        return ebook_userstatus;
    }

    public void setEbook_userstatus(String ebook_userstatus) {
        this.ebook_userstatus = ebook_userstatus;
    }
    public String getEbook_status_remarks() {
        return ebook_status_remarks;
    }
    public void setEbook_status_remarks(String ebook_status_remarks) {
        this.ebook_status_remarks = ebook_status_remarks;
    }
    public String getEbook_templateno() {
        return ebook_templateno;
    }
    public void setEbook_templateno(String ebook_templateno) {
        this.ebook_templateno = ebook_templateno;
    }
    public String getEbook_no_containers() {
        return ebook_no_containers;
    }
    public void setEbook_no_containers(String ebook_no_containers) {
        this.ebook_no_containers = ebook_no_containers;
    }
    public String getEbook_templatename() {
        return ebook_templatename;
    }
    public void setEbook_templatename(String ebook_templatename) {
        this.ebook_templatename = ebook_templatename;
    }
    public String getEbook_custtype_desc() {
        return ebook_custtype_desc;
    }
    public void setEbook_custtype_desc(String ebook_custtype_desc) {
        this.ebook_custtype_desc = ebook_custtype_desc;
    }
    public String getEbook_contposition_desc() {
        return ebook_contposition_desc;
    }
    public void setEbook_contposition_desc(String ebook_contposition_desc) {
        this.ebook_contposition_desc = ebook_contposition_desc;
    }
    public String getEbook_equiptype_desc() {
        return ebook_equiptype_desc;
    }
    public void setEbook_equiptype_desc(String ebook_equiptype_desc) {
        this.ebook_equiptype_desc = ebook_equiptype_desc;
    }
    public String getEbook_package_desc() {
        return ebook_package_desc;
    }
    public void setEbook_package_desc(String ebook_package_desc) {
        this.ebook_package_desc = ebook_package_desc;
    }
    public String getEbook_commodity_desc() {
        return ebook_commodity_desc;
    }
    public void setEbook_commodity_desc(String ebook_commodity_desc) {
        this.ebook_commodity_desc = ebook_commodity_desc;
    }
    public String getEbook_loadcondn_desc() {
        return ebook_loadcondn_desc;
    }

    public void setEbook_loadcondn_desc(String ebook_loadcondn_desc) {
        this.ebook_loadcondn_desc = ebook_loadcondn_desc;
    }

    public String getEbook_equipstat_desc() {
        return ebook_equipstat_desc;
    }

    public void setEbook_equipstat_desc(String ebook_equipstat_desc) {
        this.ebook_equipstat_desc = ebook_equipstat_desc;
    }
    public String getEbook_oog_desc() {
        return ebook_oog_desc;
    }

    public void setEbook_oog_desc(String ebook_oog_desc) {
        this.ebook_oog_desc = ebook_oog_desc;
    }

    public String getEbook_hazardous_desc() {
        return ebook_hazardous_desc;
    }

    public void setEbook_hazardous_desc(String ebook_hazardous_desc) {
        this.ebook_hazardous_desc = ebook_hazardous_desc;
    }

    public String getEbook_shipown_desc() {
        return ebook_shipown_desc;
    }

    public void setEbook_shipown_desc(String ebook_shipown_desc) {
        this.ebook_shipown_desc = ebook_shipown_desc;
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
}
