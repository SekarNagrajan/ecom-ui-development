/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class Editempbooking_ics_new implements Serializable {

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("payment_mode")
    @Expose
    private String payment_mode;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("enseori")
    @Expose
    private String enseori;

    @SerializedName("address1")
    @Expose
    private String address1;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("eucustom")
    @Expose
    private String eucustom;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("bill_type")
    @Expose
    private String bill_type;

    @SerializedName("cust_name")
    @Expose
    private String cust_name;

    @SerializedName("filing_type")
    @Expose
    private String filing_type;

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("ens_type")
    @Expose
    private String ens_type;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("zipcode")
	@Expose
	private String zipcode;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEnseori() {
        return enseori;
    }

    public void setEnseori(String enseori) {
        this.enseori = enseori;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getEdireaderid() {
        return edireaderid;
    }

    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    public String getEucustom() {
        return eucustom;
    }

    public void setEucustom(String eucustom) {
        this.eucustom = eucustom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getFiling_type() {
        return filing_type;
    }

    public void setFiling_type(String filing_type) {
        this.filing_type = filing_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEns_type() {
        return ens_type;
    }

    public void setEns_type(String ens_type) {
        this.ens_type = ens_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
