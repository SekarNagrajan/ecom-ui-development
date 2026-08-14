/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class VgmVO implements Serializable {

    @SerializedName("eqptypeid")
    @Expose
    private String eqptypeid;

    @SerializedName("eqpid")
    @Expose
    private String eqpid;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("bookNo")
    @Expose
    private String bookNo;

    @SerializedName("containerno")
    @Expose
    private String containerno;

    @SerializedName("agencycode")
    @Expose
    private String agencycode;

    @SerializedName("contactemailid")
    @Expose
    private String contactemailid;

    @SerializedName("version_no")
    @Expose
    private String version_no;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("eqptype")
    @Expose
    private String eqptype;

    @SerializedName("vgm_source")
    @Expose
    private String vgm_source;

    @SerializedName("vgm_ref_number")
    @Expose
    private String vgm_ref_number;

    @SerializedName("vgm_order_number")
    @Expose
    private String vgm_order_number;

    @SerializedName("vgm_obtained_date")
    @Expose
    private String vgm_obtained_date;

    @SerializedName("vgm_weight")
    @Expose
    private String vgm_weight;

    @SerializedName("vgm_unit")
    @Expose
    private String vgm_unit;

    @SerializedName("vgm_obtained_method")
    @Expose
    private String vgm_obtained_method;

    @SerializedName("vgm_company_name")
    @Expose
    private String vgm_company_name;

    @SerializedName("vgm_address1")
    @Expose
    private String vgm_address1;

    @SerializedName("vgm_address2")
    @Expose
    private String vgm_address2;

    @SerializedName("vgm_zipcode")
    @Expose
    private String vgm_zipcode;

    @SerializedName("vgm_city")
    @Expose
    private String vgm_city;

    @SerializedName("vgm_country")
    @Expose
    private String vgm_country;

    @SerializedName("vgm_authorized_person")
    @Expose
    private String vgm_authorized_person;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    @SerializedName("fax")
    @Expose
    private String fax;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("vgm_type")
    @Expose
    private String vgm_type;

    @SerializedName("usercr")
    @Expose
    private String usercr;

    @SerializedName("userup")
    @Expose
    private String userup;

    @SerializedName("oprstatus")
    @Expose
    private String oprstatus;

    @SerializedName("datecr")
    @Expose
    private String datecr;

    @SerializedName("dateup")
    @Expose
    private String dateup;

    @SerializedName("bl_no")
    @Expose
    private String bl_no;

    @SerializedName("dummyCntr")
    @Expose
    private boolean dummyCntr;

    @SerializedName("originportid")
    @Expose
    private String originportid;

    @SerializedName("agencyid")
    @Expose
    private String agencyid;

    @SerializedName("shipperown")
    @Expose
    private String shipperown;

    @SerializedName("equipmenttype")
    @Expose
    private String equipmenttype;

    @SerializedName("isdummy")
    @Expose
    private String isdummy;

    @SerializedName("tarewt")
    @Expose
    private String tareWeight;
    @SerializedName("finalportid")
    @Expose
    private String finalportid;

    @SerializedName("loadportid")
    @Expose
    private String loadportid;

    @SerializedName("dischargeportid")
    @Expose
    private String dischargeportid;

    public String getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(String tareWeight) {
        this.tareWeight = tareWeight;
    }

    public String getIsdummy() {
        return isdummy;
    }

    public void setIsdummy(String isdummy) {
        this.isdummy = isdummy;
    }

    public String getEquipmenttype() {
        return equipmenttype;
    }

    public void setEquipmenttype(String equipmenttype) {
        this.equipmenttype = equipmenttype;
    }

    public String getShipperown() {
        return shipperown;
    }

    public void setShipperown(String shipperown) {
        this.shipperown = shipperown;
    }

    public String getVgm_address1() {
        return vgm_address1;
    }

    public void setVgm_address1(String vgm_address1) {
        this.vgm_address1 = vgm_address1;
    }

    public String getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    public String getOriginportid() {
        return originportid;
    }

    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    public boolean isDummyCntr() {
        return dummyCntr;
    }

    public void setDummyCntr(boolean dummyCntr) {
        this.dummyCntr = dummyCntr;
    }

    public String getBl_no() {
        return bl_no;
    }

    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getDatecr() {
        return datecr;
    }

    public void setDatecr(String datecr) {
        this.datecr = datecr;
    }

    public String getDateup() {
        return dateup;
    }

    public void setDateup(String dateup) {
        this.dateup = dateup;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getEqptype() {
        return eqptype;
    }

    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    public String getVgm_source() {
        return vgm_source;
    }

    public void setVgm_source(String vgm_source) {
        this.vgm_source = vgm_source;
    }

    public String getVgm_ref_number() {
        return vgm_ref_number;
    }

    public void setVgm_ref_number(String vgm_ref_number) {
        this.vgm_ref_number = vgm_ref_number;
    }

    public String getVgm_obtained_date() {
        return vgm_obtained_date;
    }

    public void setVgm_obtained_date(String vgm_obtained_date) {
        this.vgm_obtained_date = vgm_obtained_date;
    }

    public String getVgm_weight() {
        return vgm_weight;
    }

    public void setVgm_weight(String vgm_weight) {
        this.vgm_weight = vgm_weight;
    }

    public String getVgm_unit() {
        return vgm_unit;
    }

    public void setVgm_unit(String vgm_unit) {
        this.vgm_unit = vgm_unit;
    }

    public String getVgm_obtained_method() {
        return vgm_obtained_method;
    }

    public void setVgm_obtained_method(String vgm_obtained_method) {
        this.vgm_obtained_method = vgm_obtained_method;
    }

    public String getVgm_company_name() {
        return vgm_company_name;
    }

    public void setVgm_company_name(String vgm_company_name) {
        this.vgm_company_name = vgm_company_name;
    }

    public String getVgm_address2() {
        return vgm_address2;
    }

    public void setVgm_address2(String vgm_address2) {
        this.vgm_address2 = vgm_address2;
    }

    public String getVgm_zipcode() {
        return vgm_zipcode;
    }

    public void setVgm_zipcode(String vgm_zipcode) {
        this.vgm_zipcode = vgm_zipcode;
    }

    public String getVgm_city() {
        return vgm_city;
    }

    public void setVgm_city(String vgm_city) {
        this.vgm_city = vgm_city;
    }

    public String getVgm_country() {
        return vgm_country;
    }

    public void setVgm_country(String vgm_country) {
        this.vgm_country = vgm_country;
    }

    public String getVgm_authorized_person() {
        return vgm_authorized_person;
    }

    public void setVgm_authorized_person(String vgm_authorized_person) {
        this.vgm_authorized_person = vgm_authorized_person;
    }

    public String getVgm_type() {
        return vgm_type;
    }

    public void setVgm_type(String vgm_type) {
        this.vgm_type = vgm_type;
    }

    public String getUsercr() {
        return usercr;
    }

    public void setUsercr(String usercr) {
        this.usercr = usercr;
    }

    public String getUserup() {
        return userup;
    }

    public void setUserup(String userup) {
        this.userup = userup;
    }

    public String getOprstatus() {
        return oprstatus;
    }

    public void setOprstatus(String oprstatus) {
        this.oprstatus = oprstatus;
    }

    public String getVersion_no() {
        return version_no;
    }

    public void setVersion_no(String version_no) {
        this.version_no = version_no;
    }

    public String getAgencycode() {
        return agencycode;
    }

    public void setAgencycode(String agencycode) {
        this.agencycode = agencycode;
    }

    public String getContactemailid() {
        return contactemailid;
    }

    public void setContactemailid(String contactemailid) {
        this.contactemailid = contactemailid;
    }

    public String getContainerno() {
        return containerno;
    }

    public void setContainerno(String containerno) {
        this.containerno = containerno;
    }

    public String getEqptypeid() {
        return eqptypeid;
    }

    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
    }

    public String getEqpid() {
        return eqpid;
    }

    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVgm_order_number() {
        return vgm_order_number;
    }

    public void setVgm_order_number(String vgm_order_number) {
        this.vgm_order_number = vgm_order_number;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFinalportid() {
        return finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }

    public String getLoadportid() {
        return loadportid;
    }

    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    public String getDischargeportid() {
        return dischargeportid;
    }

    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

}
