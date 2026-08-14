package net.ecom.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author preethi.elumalai
 */
public class BokingDetailds implements Serializable {

    private String ebook_origin_country;
    private String ebook_delivery_country;
    private String ebook_date;
    private String preCarriage_btn;
    private String onCarriage_btn;
    private String carrierContact;
    private String ebook_haulagetype;
    private String ebook_pickup_date;
    private String ebook_drop_date;
    private String ebook_haulercode;
    private String ebook_custpo;
    private String ebook_reftype;
    private String ebook_exportref;
    private String ebook_pickup_point;
    private String emptyPickup;
    private String emptyPickUpDate;
    private String customerreference;
    private String ACID;
    private String cargoprotectid;
    private String shipperReference;
    private String shipperReferenceType;
    private String agency_reference;
    private String amendmentTomailId;
     private String blType;

    public String getBlType() {
        return blType;
    }

    public void setBlType(String blType) {
        this.blType = blType;
    }
    //

    private List<EcomBookingCustomer> customerDetails = new ArrayList<EcomBookingCustomer>();
    private List<EcomBookingCargoDetails> cargoDetails = new ArrayList<EcomBookingCargoDetails>();
    private EcomBookingScheduleVo scheduleDetails = new EcomBookingScheduleVo();

    public String getCargoprotectid() {
        return cargoprotectid;
    }

    public void setCargoprotectid(String cargoprotectid) {
        this.cargoprotectid = cargoprotectid;
    }

    public List<EcomBookingCustomer> getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(List<EcomBookingCustomer> customerDetails) {
        this.customerDetails = customerDetails;
    }

    public String getEbook_date() {
        return ebook_date;
    }

    public void setEbook_date(String ebook_date) {
        this.ebook_date = ebook_date;
    }

    public String getCarrierContact() {
        return carrierContact;
    }

    public void setCarrierContact(String carrierContact) {
        this.carrierContact = carrierContact;
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

    public String getEbook_haulercode() {
        return ebook_haulercode;
    }

    public void setEbook_haulercode(String ebook_haulercode) {
        this.ebook_haulercode = ebook_haulercode;
    }

    public String getEbook_custpo() {
        return ebook_custpo;
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

    public String getEbook_pickup_point() {
        return ebook_pickup_point;
    }

    public void setEbook_pickup_point(String ebook_pickup_point) {
        this.ebook_pickup_point = ebook_pickup_point;
    }

    public String getEmptyPickup() {
        return emptyPickup;
    }

    public void setEmptyPickup(String emptyPickup) {
        this.emptyPickup = emptyPickup;
    }

    public String getEmptyPickUpDate() {
        return emptyPickUpDate;
    }

    public void setEmptyPickUpDate(String emptyPickUpDate) {
        this.emptyPickUpDate = emptyPickUpDate;
    }

    public String getCustomerreference() {
        return customerreference;
    }

    public void setCustomerreference(String customerreference) {
        this.customerreference = customerreference;
    }

    public String getACID() {
        return ACID;
    }

    public void setACID(String ACID) {
        this.ACID = ACID;
    }

    public List<EcomBookingCargoDetails> getCargoDetails() {
        return cargoDetails;
    }

    public void setCargoDetails(List<EcomBookingCargoDetails> cargoDetails) {
        this.cargoDetails = cargoDetails;
    }

    public String getAgency_reference() {
        return agency_reference;
    }

    public void setAgency_reference(String agency_reference) {
        this.agency_reference = agency_reference;
    }

    public EcomBookingScheduleVo getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(EcomBookingScheduleVo scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public String getEbook_origin_country() {
        return ebook_origin_country;
    }

    public void setEbook_origin_country(String ebook_origin_country) {
        this.ebook_origin_country = ebook_origin_country;
    }

    public String getEbook_delivery_country() {
        return ebook_delivery_country;
    }

    public void setEbook_delivery_country(String ebook_delivery_country) {
        this.ebook_delivery_country = ebook_delivery_country;
    }

    public String getPreCarriage_btn() {
        return preCarriage_btn;
    }

    public void setPreCarriage_btn(String preCarriage_btn) {
        this.preCarriage_btn = preCarriage_btn;
    }

    public String getOnCarriage_btn() {
        return onCarriage_btn;
    }

    public void setOnCarriage_btn(String onCarriage_btn) {
        this.onCarriage_btn = onCarriage_btn;
    }

    public String getAmendmentTomailId() {
        return amendmentTomailId;
    }

    public void setAmendmentTomailId(String amendmentTomailId) {
        this.amendmentTomailId = amendmentTomailId;
    }

    public String getShipperReference() {
        return shipperReference;
    }

    public void setShipperReference(String shipperReference) {
        this.shipperReference = shipperReference;
    }
    public String getShipperReferenceType() {
        return shipperReferenceType;
    }
    public void setShipperReferenceType(String shipperReferenceType) {
        this.shipperReferenceType = shipperReferenceType;
    }

}
