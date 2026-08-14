/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;
/**
 *
 * @author udhaya
 */
public class EComVo implements Serializable  {
    private String corid ="";
    private String cust_code = "";
    private String strEmail="";
    private String strLoginname="";
    private String strPassword="";
    private String srUserName="";
    
    
    private Validation val = new Validation();


    /**
     * @return the corid
     */
    public String getCorid() {
        return val.checkNull(corid);
    }

    /**
     * @param corid the corid to set
     */
    public void setCorid(String corid) {
        this.corid = corid;
    }

    /**
     * @return the cust_code
     */
    public String getCust_code() {
        return val.checkNull(cust_code);
    }

    /**
     * @param cust_code the cust_code to set
     */
    public void setCust_code(String cust_code) {
        this.cust_code = cust_code;
    }

    /**
     * @return the strEmail
     */
    public String getStrEmail() {
        return val.checkNull(strEmail);
    }

    /**
     * @param strEmail the strEmail to set
     */
    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    /**
     * @return the strLoginname
     */
    public String getStrLoginname() {
        return val.checkNull(strLoginname);
    }

    /**
     * @param strLoginname the strLoginname to set
     */
    public void setStrLoginname(String strLoginname) {
        this.strLoginname = strLoginname;
    }

    /**
     * @return the strPassword
     */
    public String getStrPassword() {
        return val.checkNull(strPassword);
    }

    /**
     * @param strPassword the strPassword to set
     */
    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    /**
     * @return the srUserName
     */
    public String getSrUserName() {
        return val.checkNull(srUserName);
    }

    /**
     * @param srUserName the srUserName to set
     */
    public void setSrUserName(String srUserName) {
        this.srUserName = srUserName;
    }

    

    
}
