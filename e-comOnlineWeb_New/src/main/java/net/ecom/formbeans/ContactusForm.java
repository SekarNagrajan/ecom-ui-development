/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author vasanthakumar.r
 */
public class ContactusForm extends org.apache.struts.action.ActionForm {

    private String username = "";
    private String compname = "";
    private String agency = "";
    private String userphone = "";
    private String strusermobile = "";
    private String useremail = "";
    private String subject = "";
    private String city = "";
    private String other_city = "";
    private String city_desc = "";
    private String othercitystatus = "";
    private String state = "";
    private String itiUserPhone = "";
    private String itiUserMobile = "";

    public String getCity_desc() {
        return city_desc;
    }

    public void setCity_desc(String city_desc) {
        this.city_desc = city_desc;
    }

    public String getOthercitystatus() {
        return othercitystatus;
    }

    public void setOthercitystatus(String othercitystatus) {
        this.othercitystatus = othercitystatus;
    }

    public String getOther_city() {
        return other_city;
    }

    public void setOther_city(String other_city) {
        this.other_city = other_city;
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
    private String remarks = "";
    private String country = "";

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getCompname() {
        return compname;
    }

    public void setCompname(String compname) {
        this.compname = compname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStrusermobile() {
        return strusermobile;
    }

    public void setStrusermobile(String strusermobile) {
        this.strusermobile = strusermobile;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }
    public String getItiUserPhone() {
        return itiUserPhone;
    }
    public void setItiUserPhone(String itiUserPhone) {
        this.itiUserPhone = itiUserPhone;
    }
    public String getItiUserMobile() {
        return itiUserMobile;
    }
    public void setItiUserMobile(String itiUserMobile) {
        this.itiUserMobile = itiUserMobile;
    }
    private String name;
    private int number;

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param i
     */
    public void setNumber(int i) {
        number = i;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    

    /**
     *
     */
    public ContactusForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }
}
