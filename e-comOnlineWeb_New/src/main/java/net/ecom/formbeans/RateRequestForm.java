/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author deepa.p
 */
public class RateRequestForm extends org.apache.struts.action.ActionForm {

    private String name;

    private int number;
    private String companyName;
    private String country;
    private String contactName;
    private String emailId;
    private String origin;
    private String pol;
    private String pod;
    private String destination;
    private String eqpType;
    private String eqpSize;
    private String cargoQty;
    private String cargoWt;
    private String commodity;
    private String dg;
    private String comments;
    private String captcha;
    private String actualfreight;
    private String requestrate;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEqpType() {
        return eqpType;
    }

    public void setEqpType(String eqpType) {
        this.eqpType = eqpType;
    }

    public String getEqpSize() {
        return eqpSize;
    }

    public void setEqpSize(String eqpSize) {
        this.eqpSize = eqpSize;
    }

    public String getCargoQty() {
        return cargoQty;
    }

    public void setCargoQty(String cargoQty) {
        this.cargoQty = cargoQty;
    }

    public String getCargoWt() {
        return cargoWt;
    }

    public void setCargoWt(String cargoWt) {
        this.cargoWt = cargoWt;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

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

    /**
     *
     */
    public RateRequestForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    public String getActualfreight() {
        return actualfreight;
    }

    public void setActualfreight(String actualfreight) {
        this.actualfreight = actualfreight;
    }

    public String getRequestrate() {
        return requestrate;
    }

    public void setRequestrate(String requestrate) {
        this.requestrate = requestrate;
    }
    
}
