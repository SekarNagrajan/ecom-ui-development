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
public class CustomerContacts implements Serializable {

    private String contactname = null;
    private String custname = null;
    private String address1 = null;
    private String phone = null;
    private String fax = null;
    private String city = null;
    private String email = null;
    private String countryid = null;
    
    private String cmpcode=null;
    private String custcode=null;
    private String mobile=null;
    private String crusr=null;
    private String onlineactive=null;
    private String webid=null;
    private String contacttype=null;
    private String crdate=null;
    private String status=null;
    private String carriercode=null;
    private String admin=null;
    private String custtype=null;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCarriercode() {
        return carriercode;
    }

    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
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
     * @return the contactname
     */
    public String getContactname() {
        return contactname;
    }

    /**
     * @param contactname the contactname to set
     */
    public void setContactname(String contactname) {
        this.contactname = contactname;
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
     * @return the custcode
     */
    public String getCustcode() {
        return custcode;
    }

    /**
     * @param custcode the custcode to set
     */
    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the crusr
     */
    public String getCrusr() {
        return crusr;
    }

    /**
     * @param crusr the crusr to set
     */
    public void setCrusr(String crusr) {
        this.crusr = crusr;
    }

    /**
     * @return the onlineactive
     */
    public String getOnlineactive() {
        return onlineactive;
    }

    /**
     * @param onlineactive the onlineactive to set
     */
    public void setOnlineactive(String onlineactive) {
        this.onlineactive = onlineactive;
    }

    /**
     * @return the webid
     */
    public String getWebid() {
        return webid;
    }

    /**
     * @param webid the webid to set
     */
    public void setWebid(String webid) {
        this.webid = webid;
    }

    /**
     * @return the contacttype
     */
    public String getContacttype() {
        return contacttype;
    }

    /**
     * @param contacttype the contacttype to set
     */
    public void setContacttype(String contacttype) {
        this.contacttype = contacttype;
    }

    /**
     * @return the crdate
     */
    public String getCrdate() {
        return crdate;
    }

    /**
     * @param crdate the crdate to set
     */
    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCusttype() {
        return custtype;
    }

    public void setCusttype(String custtype) {
        this.custtype = custtype;
    }

}
