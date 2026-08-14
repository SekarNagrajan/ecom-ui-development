/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author prabakarans
 */
public class LoginCustomerVo implements Serializable {

    private String name = "";
    private String fax = "";
    private String phone = "";
    private String pincode = "";
    private String email = "";
    private String city = "";
    private String addtess = "";
    private String country = "";
    private String compname = "";
    Validation val = new Validation();

    /**
     * @return the name
     */
    public String getName() {
        return val.checkNull(name);
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return val.checkNull(fax);
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return val.checkNull(phone);
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the pincode
     */
    public String getPincode() {
        return val.checkNull(pincode);
    }

    /**
     * @param pincode the pincode to set
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return val.checkNull(email);
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return val.checkNull(city);
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the addtess
     */
    public String getAddtess() {
        return val.checkNull(addtess);
    }

    /**
     * @param addtess the addtess to set
     */
    public void setAddtess(String addtess) {
        this.addtess = addtess;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return val.checkNull(country);
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the compname
     */
    public String getCompname() {
        return compname;
    }

    /**
     * @param compname the compname to set
     */
    public void setCompname(String compname) {
        this.compname = compname;
    }
}
