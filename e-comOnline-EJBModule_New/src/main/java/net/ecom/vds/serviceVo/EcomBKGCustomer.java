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
 * @author sasikumar.r
 */
public class EcomBKGCustomer implements Serializable {

    @SerializedName("custtypeid")
    @Expose
    private String custtypeid;
    @SerializedName("custid")
    @Expose
    private String custid;
    @SerializedName("countryid")
    @Expose
    private String countryid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zipcode")
    @Expose
    private String zipCode;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("stateid")
    @Expose
    private String stateid;

    @SerializedName("eori")
    @Expose
    private String eori;

    @SerializedName("persontype")
    @Expose
    private String persontype;

    /**
     * @return the custtypeid
     */
    public String getCusttypeid() {
        return custtypeid;
    }

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
     * @param custtypeid the custtypeid to set
     */
    public void setCusttypeid(String custtypeid) {
        this.custtypeid = custtypeid;
    }

    /**
     * @return the custid
     */
    public String getCustid() {
        return custid;
    }

    /**
     * @param custid the custid to set
     */
    public void setCustid(String custid) {
        this.custid = custid;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
     * @return the edireaderid
     */
    public String getEdireaderid() {
        return edireaderid;
    }

    /**
     * @param edireaderid the edireaderid to set
     */
    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    /**
     * @return the book_no
     */
    public String getBook_no() {
        return book_no;
    }

    /**
     * @param book_no the book_no to set
     */
    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

}
