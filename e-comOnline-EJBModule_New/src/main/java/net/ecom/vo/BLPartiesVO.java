/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;
import java.io.Serializable;
import net.ecom.common.Validation;
/**
 *
 * @author durkgadevi
 */
public class BLPartiesVO implements Serializable{
 private String custtype=null;
    private String address1 = null;
    private String address2 = null;
    private String custname = null;
    private String city = null;
    private String telephone = null;
    private String fax = null;
    private String email = null;
    private String countryid = null;
    private String custCode="";

    private String vesselcode = null;
    private String voyagecode = null;
    private String bound = null;
    private String pol = null;
    private String pod = null;
    private String receipt = null;
    private String delivery = null;

    /**
     * @return the custtype
     */
    public String getCusttype() {
        return custtype;
    }

    /**
     * @param custtype the custtype to set
     */
    public void setCusttype(String custtype) {
        this.custtype = custtype;
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
     * @return the custCode
     */
    public String getCustCode() {
        return custCode;
    }

    /**
     * @param custCode the custCode to set
     */
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    /**
     * @return the vesselcode
     */
    public String getVesselcode() {
         return Validation.checkNull(vesselcode);
    }

    /**
     * @param vesselcode the vesselcode to set
     */
    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    /**
     * @return the voyagecode
     */
    public String getVoyagecode() {
        return Validation.checkNull(voyagecode);
    }

    /**
     * @param voyagecode the voyagecode to set
     */
    public void setVoyagecode(String voyagecode) {
        this.voyagecode = voyagecode;
    }

    /**
     * @return the bound
     */
    public String getBound() {
       return Validation.checkNull(bound);
    }

    /**
     * @param bound the bound to set
     */
    public void setBound(String bound) {
        this.bound = bound;
    }

    /**
     * @return the pol
     */
    public String getPol() {
        return Validation.checkNull(pol);
    }

    /**
     * @param pol the pol to set
     */
    public void setPol(String pol) {
        this.pol = pol;
    }

    /**
     * @return the pod
     */
    public String getPod() {
       return Validation.checkNull(pod);
    }

    /**
     * @param pod the pod to set
     */
    public void setPod(String pod) {
        this.pod = pod;
    }

    /**
     * @return the receipt
     */
    public String getReceipt() {
        return Validation.checkNull(receipt);
    }

    /**
     * @param receipt the receipt to set
     */
    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    /**
     * @return the delivery
     */
    public String getDelivery() {
       return Validation.checkNull(delivery);
    }

    /**
     * @param delivery the delivery to set
     */
    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
}
