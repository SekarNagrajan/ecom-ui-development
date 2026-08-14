/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author salomi.a
 */
public class CustomerMaster implements Serializable{
    @SerializedName("custid")
    @Expose
    private String custid;
    
    public String getCustid() {
            return custid;
    }
    public void setCustid(String custid) {
            this.custid = custid;
    }
    
    @SerializedName("pobox")
    @Expose
    private String pobox;

    @SerializedName("uasccustcode")
    @Expose
    private String uasccustcode;

    @SerializedName("address2")
    @Expose
    private String address2;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address1")
    @Expose
    private String address1;

    @SerializedName("postalcode")
    @Expose
    private String postalcode;

    @SerializedName("custcode")
    @Expose
    private String custcode;

    @SerializedName("fax")
    @Expose
    private String fax;

    @SerializedName("custname")
    @Expose
    private String custname;

    @SerializedName("countryid")
    @Expose
    private String countryid;

    @SerializedName("email")
    @Expose
    private String email;


    public String getPobox() {
            return pobox;
    }
    public void setPobox(String pobox) {
            this.pobox = pobox;
    }
    public String getUasccustcode() {
            return uasccustcode;
    }
    public void setUasccustcode(String uasccustcode) {
            this.uasccustcode = uasccustcode;
    }
    public String getAddress2() {
            return address2;
    }
    public void setAddress2(String address2) {
            this.address2 = address2;
    }
    public String getCity() {
            return city;
    }
    public void setCity(String city) {
            this.city = city;
    }
    public String getPhone() {
            return phone;
    }
    public void setPhone(String phone) {
            this.phone = phone;
    }
    public String getAddress1() {
            return address1;
    }
    public void setAddress1(String address1) {
            this.address1 = address1;
    }
    public String getPostalcode() {
            return postalcode;
    }
    public void setPostalcode(String postalcode) {
            this.postalcode = postalcode;
    }
    public String getCustcode() {
            return custcode;
    }
    public void setCustcode(String custcode) {
            this.custcode = custcode;
    }
    public String getFax() {
            return fax;
    }
    public void setFax(String fax) {
            this.fax = fax;
    }
    public String getCustname() {
            return custname;
    }
    public void setCustname(String custname) {
            this.custname = custname;
    }
    public String getCountryid() {
            return countryid;
    }
    public void setCountryid(String countryid) {
            this.countryid = countryid;
    }
    public String getEmail() {
            return email;
    }
    public void setEmail(String email) {
            this.email = email;
    }
}
