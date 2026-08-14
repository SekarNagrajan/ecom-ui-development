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
public class BLSubmitVO implements Serializable{

     private String containerno=null;
    private String type=null;
    private String carrierseal = null;
    private String shipperseal = null;
    private String otherseal = null;
    private String commodity = null;
    private String cargowt = null;
    private String unit = null;
    private String qty = null;


    private String address = null;
    private String custname = null;
    private String city = null;
    private String telephone = null;
    private String fax = null;
    private String email = null;
    private String countryid = null;
    private String custCode="";

    private String select="";

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

}
