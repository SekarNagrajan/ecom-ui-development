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
public class AgencyMaster implements Serializable{
    
      private String agencycode=null;
      private String agencyname=null;
      private String address=null;
      private String countryname=null;
      private String telephone=null;
      private String fax=null;
      private String email=null;
      private String location=null;
      private String city=null;
      private String contactname=null;
      private String contactdesignation=null;
      private String contactphoneno=null;
      private String contactemailid=null;

    /**
     * @return the agencycode
     */
    public String getAgencycode() {
        return agencycode;
    }

    /**
     * @param agencycode the agencycode to set
     */
    public void setAgencycode(String agencycode) {
        this.agencycode = agencycode;
    }

    /**
     * @return the agencyname
     */
    public String getAgencyname() {
        return agencyname;
    }

    /**
     * @param agencyname the agencyname to set
     */
    public void setAgencyname(String agencyname) {
        this.agencyname = agencyname;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
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
     * @return the contactdesignation
     */
    public String getContactdesignation() {
        return contactdesignation;
    }

    /**
     * @param contactdesignation the contactdesignation to set
     */
    public void setContactdesignation(String contactdesignation) {
        this.contactdesignation = contactdesignation;
    }

    /**
     * @return the contactphoneno
     */
    public String getContactphoneno() {
        return contactphoneno;
    }

    /**
     * @param contactphoneno the contactphoneno to set
     */
    public void setContactphoneno(String contactphoneno) {
        this.contactphoneno = contactphoneno;
    }

    /**
     * @return the contactemailid
     */
    public String getContactemailid() {
        return contactemailid;
    }

    /**
     * @param contactemailid the contactemailid to set
     */
    public void setContactemailid(String contactemailid) {
        this.contactemailid = contactemailid;
    }
    
}
