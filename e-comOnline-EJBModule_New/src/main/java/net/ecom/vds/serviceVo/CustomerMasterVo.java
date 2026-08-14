/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import java.io.Serializable;

/**
 *
 * @author sasikumar.r
 */
public class CustomerMasterVo implements Serializable{
    
    private String cmpcode=null;
    private String custname=null;
    private String address1=null;
    private String address2=null;
    private String city=null;
    private String countryname =null;
    private String fax=null;
    private String email=null;
    private String phone=null;
    private String controllocation=null;
    
   
    private String website;
    private String otherscity;
    private String pobox;
    private String countryid;
    private String zipcode;
    private String state;  
    private String eori;  
    private String persontype;  
    private String billcity;
    private String billstate;
    private String billzipcode;
    private String billcountry;

    public String getEori() {
        return eori;
    }

    public void setEori(String eori) {
        this.eori = eori;
    }

    public String getPersontype() {
        return persontype;
    }

    public void setPersontype(String persontype) {
        this.persontype = persontype;
    }

    /**
     * @return the cmpcode
     */
    public String getCmpcode() {
        return cmpcode;
    }

    /**
     * @param cmpcode the cmpcode to set
     */
    public void setCmpcode(String cmpcode) {
        this.cmpcode = cmpcode;
    }

    /**
     * @return the custname
     */
    public String getCustname() {
        return custname;
    }

    /**
     * @param custname the custname to set
     */
    public void setCustname(String custname) {
        this.custname = custname;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }


    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the countryname
     */
    public String getCountryname() {
        return countryname;
    }

    /**
     * @param countryname the countryname to set
     */
    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    /**
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * @param website the website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * @return the otherscity
     */
    public String getOtherscity() {
        return otherscity;
    }

    /**
     * @param otherscity the otherscity to set
     */
    public void setOtherscity(String otherscity) {
        this.otherscity = otherscity;
    }

    /**
     * @return the pobox
     */
    public String getPobox() {
        return pobox;
    }

    /**
     * @param pobox the pobox to set
     */
    public void setPobox(String pobox) {
        this.pobox = pobox;
    }

    /**
     * @return the countryid
     */
    public String getCountryid() {
        return countryid;
    }

    /**
     * @param countryid the countryid to set
     */
    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the controllocation
     */
    public String getControllocation() {
        return controllocation;
    }

    /**
     * @param controllocation the controllocation to set
     */
    public void setControllocation(String controllocation) {
        this.controllocation = controllocation;
    }
  
    public String getBillcity() {
        return billcity;
    }

    public void setBillcity(String billcity) {
        this.billcity = billcity;
    }
    
    
    public String getBillstate() {
        return billstate;
    }

    public void setBillstate(String billstate) {
        this.billstate = billstate;
    }
    
    
    public String getBillzipcode() {
        return billzipcode;
    }

    public void setBillzipcode(String billzipcode) {
        this.billzipcode = billzipcode;
    }
    
    public String getBillcountry() {
        return billcountry;
    }

    public void setBillcountry(String billcountry) {
        this.billcountry = billcountry;
    }
}
