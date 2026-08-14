/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author salomi.a
 */
public class Bl_cust implements Serializable {

    @SerializedName("address2")
    @Expose
    private String address2;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("address1")
    @Expose
    private String address1;

    @SerializedName("custid")
    @Expose
    private String custid;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    @SerializedName("fax")
    @Expose
    private String fax;

    @SerializedName("countryid")
    @Expose
    private String countryid;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("custtypeid")
    @Expose
    private String custtypeid;

    @SerializedName("stateid")
    @Expose
    private String stateid;

    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    @SerializedName("eori")
    @Expose
    private String eori;

    @SerializedName("persontype")
    @Expose
    private String persontype;

    

    public String getPersontype() {
        return persontype;
    }

    public void setPersontype(String persontype) {
        this.persontype = persontype;
    }

    public String getEori() {
        return eori;
    }

    public void setEori(String eori) {
        this.eori = eori;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCusttypeid() {
        return custtypeid;
    }

    public void setCusttypeid(String custtypeid) {
        this.custtypeid = custtypeid;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
