/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author arunprasath
 */
public class SISubmitVO implements Serializable {

    private String containerno = null;
    private String type = null;
    private String carrierseal = null;
    private String shipperseal = null;
    private String otherseal = null;
    private String commodity = null;
    private String cargowt = null;
    private String unit = null;
    private String qty = null;

    private String address1 = null;
    private String custname = null;
    private String city = null;
    private String telephone = null;
    private String fax = null;
    private String email = null;
    private String countryid = null;
    private String custCode = "";
    private String notifyCheck = null;
    private String ordercheck = null;
    private String zipCode = null;
    private String address2 = null;

    private String address = null;

    private String state = null;
    private String eori = null;
    private String persontype = null;
    

    public String getOrdercheck() {
        return ordercheck;
    }

    public void setOrdercheck(String ordercheck) {
        this.ordercheck = ordercheck;
    }

    private String select = "";

    /**
     * @return the containerno
     */
    public String getContainerno() {
        return Validation.checkNull(containerno);
    }

    /**
     * @param containerno the containerno to set
     */
    public void setContainerno(String containerno) {
        this.containerno = containerno;
    }

    /**
     * @return the type
     */
    public String getType() {
        return Validation.checkNull(type);
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the carrierseal
     */
    public String getCarrierseal() {
        return Validation.checkNull(carrierseal);
    }

    /**
     * @param carrierseal the carrierseal to set
     */
    public void setCarrierseal(String carrierseal) {
        this.carrierseal = carrierseal;
    }

    /**
     * @return the shipperseal
     */
    public String getShipperseal() {
        return Validation.checkNull(shipperseal);
    }

    /**
     * @param shipperseal the shipperseal to set
     */
    public void setShipperseal(String shipperseal) {
        this.shipperseal = shipperseal;
    }

    /**
     * @return the otherseal
     */
    public String getOtherseal() {
        return Validation.checkNull(otherseal);
    }

    /**
     * @param otherseal the otherseal to set
     */
    public void setOtherseal(String otherseal) {
        this.otherseal = otherseal;
    }

    /**
     * @return the commodity
     */
    public String getCommodity() {
        return Validation.checkNull(commodity);
    }

    /**
     * @param commodity the commodity to set
     */
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    /**
     * @return the cargowt
     */
    public String getCargowt() {
        return Validation.checkNull(cargowt);
    }

    /**
     * @param cargowt the cargowt to set
     */
    public void setCargowt(String cargowt) {
        this.cargowt = cargowt;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return Validation.checkNull(unit);
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return Validation.checkNull(address);
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the custname
     */
    public String getCustname() {
        return Validation.checkNull(custname);
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
        return Validation.checkNull(city);
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
        return Validation.checkNull(telephone);
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
        return Validation.checkNull(fax);
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
        return Validation.checkNull(email);
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
        return Validation.checkNull(countryid);
    }

    /**
     * @param countryid the countryid to set
     */
    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return Validation.checkNull(qty);
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the custCode
     */
    public String getCustCode() {
        return Validation.checkNull(custCode);
    }

    /**
     * @param custCode the custCode to set
     */
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    /**
     * @return the select
     */
    public String getSelect() {
        return Validation.checkNull(select);
    }

    /**
     * @param select the select to set
     */
    public void setSelect(String select) {
        this.select = select;
    }

    /**
     * @return the notifyCheck
     */
    public String getNotifyCheck() {
        return notifyCheck;
    }

    /**
     * @param notifyCheck the notifyCheck to set
     */
    public void setNotifyCheck(String notifyCheck) {
        this.notifyCheck = notifyCheck;
    }

    public String getAddress1() {
        return Validation.checkNull(address1);
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress2() {
        return Validation.checkNull(address2);
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

}
