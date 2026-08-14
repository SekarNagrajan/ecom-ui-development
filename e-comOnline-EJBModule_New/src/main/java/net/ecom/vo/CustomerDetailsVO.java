package net.ecom.vo;

import java.io.Serializable;

public class CustomerDetailsVO implements Serializable {

    private String name = null;
    private String address = null;
    private String address1 = null;
    private String phoneNo = null;
    private String fax = null;
    private String email = null;
    private String country = null;
    private String city = null;
    private String countryCode = null;
    private String toorder = null;
    private String notifyCheck = null;
    private String state = null;
    private String contactperson = null;
    private String zipcode = null;
    private String taxId = null;
    private String eroi = null;
    private String perontype = null;

    public String getNotifyCheck() {
        return notifyCheck;
    }

    public void setNotifyCheck(String notifyCheck) {
        this.notifyCheck = notifyCheck;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }
    private String custType = null;
    private String custCode = null;

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getToorder() {
        return toorder;
    }

    public void setToorder(String toorder) {
        this.toorder = toorder;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getEroi() {
        return eroi;
    }

    public void setEroi(String eroi) {
        this.eroi = eroi;
    }

    public String getPerontype() {
        return perontype;
    }

    public void setPerontype(String perontype) {
        this.perontype = perontype;
    }

}
