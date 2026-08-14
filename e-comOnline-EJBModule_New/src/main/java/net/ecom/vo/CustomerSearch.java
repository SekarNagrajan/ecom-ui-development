/*
 * CustomerSearch.java
 *
 * Created on March 29, 2008, 3:30 PM
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
public class CustomerSearch implements Serializable  {
    private String companyName="";
    private String firstName="";
    private String lastName="";
    private String locationCode="";
    private String mailID="";
    private String address="";
    private String searchImg="";
     private String vendorsearchImg="";
    
     StringBuffer sb=new StringBuffer("");
      StringBuffer sbVendor=new StringBuffer(""); // phase - 2
    /** Creates a new instance of CustomerSearch */
    public CustomerSearch() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getMailID() {
        return mailID;
    }

    public void setMailID(String mailID) {
        this.mailID = mailID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void setSearchImg(String searchImg){
        this.searchImg=searchImg;
    }
    public String getSearchImg(){
      //String firstNameAlias=firstName.replaceAll(" ","%20");
        sb.append("<a  style=\"cursor:pointer;\"");
        sb.append(" onclick=\"setSearchInfo('"+firstName+"','CustomerInfo');\">");
        sb.append("<img src=\"images/Pick16.gif\" align=\"absmiddle\">");
        sb.append("</a>");
        return sb.toString();
    }

     // phase-2
    public String getVendorsearchImg() {
        sbVendor.append("<a  style=\"cursor:pointer;\"");
        sbVendor.append(" onclick=\"setSearchVendorname('"+companyName+"','VendorInfo');\">");
        sbVendor.append("<img src=\"images/Pick16.gif\" align=\"absmiddle\">");
        sbVendor.append("</a>");
        return sbVendor.toString();

    }

    /**
     * @param vendorsearchImg the vendorsearchImg to set
     */
    public void setVendorsearchImg(String vendorsearchImg) {
        this.vendorsearchImg = vendorsearchImg;
    }
    
}
