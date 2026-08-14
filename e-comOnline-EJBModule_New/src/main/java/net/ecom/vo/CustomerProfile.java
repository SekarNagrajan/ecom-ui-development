/*
 * CustomerProfile.java
 *
 * Created on January 17, 2008, 6:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author udhaya
 */
public class CustomerProfile implements Serializable {

    /**
     * Creates a new instance of CustomerProfile
     */
    public CustomerProfile() {
    }

    private String custId = null;
    private String custName = null;
    private String emailId = null;
    private String mobileNo = null;
    private String country = null;
    private String state = null;
    private String city = null;
    private String zipcode = null;
    private String addr1 = null;
    private String addr2 = null;
    private String teleNo = null;
    private String fax = null;
    private String custType = null;
    private String position = null;
    private String currency = null;
    private String CustomerName = null;
    private String contactName = null;
    private String registSearchImg = "";
    private String searchImg = "";
    private String ebooksearchImg = "";
    private String countryName = "";
    private String pobox = "";
    private String custImg = "";
    StringBuffer sb = new StringBuffer("");
    StringBuffer sv = new StringBuffer("");
    StringBuffer cu = new StringBuffer("");
    StringBuffer sicu = new StringBuffer("");
    private String siCustImg = "";
    private String loginName = "";
    private String sisearchImg = "";
    private String citycode = null;
    private String fieldName = "";
    private String contact = "";
    private int id = 0;
    private String userPhoneNo=null;
    private String agency=null;
    private String preferedLang=null;
    private String defaultLamg=null;
    private String ccMailId=null;
    private String timezone = "";
    private String registereddate="";
    private String vatno=null;
    private String enseori = "";
    private String filingtype="";
    private String typeofperson = "";

    public String getFilingtype() {
        return filingtype;
    }

    public void setFilingtype(String filingtype) {
        this.filingtype = filingtype;
    }

    public String getEnseori() {
        return enseori;
    }

    public void setEnseori(String enseori) {
        this.enseori = enseori;
    }

    public String getCcMailId() {
        return ccMailId;
    }

    public void setCcMailId(String ccMailId) {
        this.ccMailId = ccMailId;
    }

    public String getPreferedLang() {
        return preferedLang;
    }

    public void setPreferedLang(String preferedLang) {
        this.preferedLang = preferedLang;
    }

    public String getDefaultLamg() {
        return defaultLamg;
    }

    public void setDefaultLamg(String defaultLamg) {
        this.defaultLamg = defaultLamg;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getTeleNo() {
        return teleNo;
    }

    public void setTeleNo(String teleNo) {
        this.teleNo = teleNo;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVatno() {
        return vatno;
    }

    public void setVatno(String vatno) {
        this.vatno = vatno;
    }

    

    public void setSearchImg(String searchImg) {
        this.searchImg = searchImg;
    }

    public String getSearchImg() {
        //String firstNameAlias=firstName.replaceAll(" ","%20");
        sb.append("<a  style=\"cursor:pointer;\"");
        sb.append("class='btn btn-warning btn-xs' onclick=\"setRegistrationSearchInfo('" + custId + "','RegistrationInfo');\">");
        sb.append("Select");
        sb.append("</a>");
        return sb.toString();
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setEbooksearchImg(String ebooksearchImg) {
        this.ebooksearchImg = ebooksearchImg;
    }

    public String getEbooksearchImg() {
        //String firstNameAlias=firstName.replaceAll(" ","%20");
        sv.append("<a  style=\"cursor:pointer;\"");
        sv.append("class='btn btn-warning btn-xs' onclick=\"seteBookingSearchInfo('" + custId + "','" + custName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + addr1.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + pobox + "','" + city.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + country + "','" + countryName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + teleNo + "','" + fax + "','" + emailId + "','" + zipcode + "', '" + citycode + "','" + addr2 + "','"+state+ "','"+enseori+ "','"+typeofperson+"');\">");
        sv.append("Select");
        sv.append("</a>");
        return sv.toString();
    }

    public String getRegistSearchImg() {
        sv.append("<a  style=\"cursor:pointer;\"");
        sv.append(" class='btn btn-warning btn-xs' onclick=\"setregistSearchInfo('" + custId + "','" + custName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "');\">");
        sv.append("Select");
        sv.append("</a>");
        return sv.toString();
    }

    /**
     * @param registSearchImg the registSearchImg to set
     */
    public void setRegistSearchImg(String registSearchImg) {
        this.registSearchImg = registSearchImg;
    } /* ends  here */


    public void setSisearchImg(String sisearchImg) {
        this.sisearchImg = sisearchImg;
    }

    public String getSisearchImg() {
        //String firstNameAlias=firstName.replaceAll(" ","%20");
        sv = new StringBuffer("");
    
        sv.append("<a  style=\"cursor:pointer;\"");
        sv.append(" class='btn btn-warning btn-xs' onclick=\"setSISearchInfo('" + custId + "','" + custName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + addr1.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + addr2.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + pobox + "','" + city.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + country + "','" + countryName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + teleNo + "','" + fax + "','" + emailId + "','" + zipcode + "', '" + citycode + "', '" + state + "','"+vatno+ "','"+enseori+ "','"+typeofperson+"');\">");
        sv.append("Select");
        sv.append("</a>");
        return sv.toString();
    }

    public String getPobox() {
        return pobox;
    }

    public void setPobox(String pobox) {
        this.pobox = pobox;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setCustImg(String custImg) {
        this.custImg = custImg;
    }
    /*
     *  Modification    :    By        Date      Purpose
     *                    K.Udhaya    25/08/08   countryName also passed as the parameter to set the Countryname in SI screen as per required by Mr.Mushtaq
     *
     */

    public String getCustImg() {
        cu.append("<div id=\"div" + id + "\" style=\"position:absolute;\"><input type=radio name=\"custInfo\" id=\"custInfo" + id + "\"  value='" + custId + "' ");
        cu.append(" onclick=\"setcustomerInfo('" + custId + "','" + custName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + addr1.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + pobox + "','" + city.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + country + "','" + countryName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "','" + teleNo + "','" + fax + "','" + emailId + "','" + zipcode + "','" + fieldName + "','" + state + "','" + id + "','" + countryName.replaceAll("\"", "&quot;").replaceAll(",", "&#44;").replaceAll("'", "&apos;") + "');\"></div>");
//    System.out.println("[ECOM] CustomerProfile Customer Radio +"+cu.toString());
        return cu.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public void setSiCustImg(String siCustImg) {
        this.siCustImg = siCustImg;
    }

    public String getSiCustImg() {
        sicu.append("<a  style=\"cursor:pointer;\"");
        sicu.append(" onclick=\"setSIUpdateCustInfo('" + loginName + "','" + custId + "','loginName',escape('" + custName + "'));\">");
        sicu.append("<img src=\"images/Pick16.gif\" align=\"absmiddle\">");
        sicu.append("</a>");
        return sicu.toString();
        // return siCustImg;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    /**
     * @return the CustomerName
     */
    public String getCustomerName() {
        return CustomerName;
    }

    /**
     * @param CustomerName the CustomerName to set
     */
    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getRegistereddate() {
        return registereddate;
    }

    public void setRegistereddate(String registereddate) {
        this.registereddate = registereddate;
    }

    public String getTypeofperson() {
        return typeofperson;
    }

    public void setTypeofperson(String typeofperson) {
        this.typeofperson = typeofperson;
    }
    
}
