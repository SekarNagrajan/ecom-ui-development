/*
 * Agencydto.java
 *
 * Created on September 1, 2008, 6:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author lavanya
 */
public class Agencydto implements Serializable {
    
    private String agencycode="" ;
    private String agencyname="";
    private String agencyEmailId="";
    private String agencyPort="";
    private String agencyAddr="";
    private String agencyCity="";
    private String agencyCountry="";
    private String agencyTele="";
    private String agencyFax="";

    private String agencycontactname="";
    private String agencycontactdesignation="";
    private String agencycontactphoneno="";
    private String agencycontactemailid="";


    
    /** Creates a new instance of Agencydto */
    public Agencydto() {
    }


     public String getAgencycontactdesignation() {
       return Validation.checkNull(agencycontactdesignation);
    }

    public void setAgencycontactdesignation(String agencycontactdesignation) {
        this.agencycontactdesignation = agencycontactdesignation;
    }

    public String getAgencycontactemailid() {
         return Validation.checkNull(agencycontactemailid);

    }

    public void setAgencycontactemailid(String agencycontactemailid) {
        this.agencycontactemailid = agencycontactemailid;
    }

    public String getAgencycontactname() {
         return Validation.checkNull(agencycontactname);

    }

    public void setAgencycontactname(String agencycontactname) {
        this.agencycontactname = agencycontactname;
    }

    public String getAgencycontactphoneno() {
        return Validation.checkNull(agencycontactphoneno);
    }

    public void setAgencycontactphoneno(String agencycontactphoneno) {
        this.agencycontactphoneno = agencycontactphoneno;
    }
    
    public String getAgencycode(){
        return Validation.checkNull(agencycode);
    }
    public void setAgencycode(String agencycode){
        this.agencycode = agencycode;
    }

    public String getAgencyname() {
        return Validation.checkNull(agencyname);
    }

    public void setAgencyname(String agencyname) {
        this.agencyname = agencyname;
    }

    /**
     * @return the agencyEmailId
     */
    public String getAgencyEmailId() {
        return Validation.checkNull(agencyEmailId);
    }

    /**
     * @param agencyEmailId the agencyEmailId to set
     */
    public void setAgencyEmailId(String agencyEmailId) {
        this.agencyEmailId = agencyEmailId;
    }

    /**
     * @return the agencyPort
     */
    public String getAgencyPort() {
        return Validation.checkNull(agencyPort);
    }

    /**
     * @param agencyPort the agencyPort to set
     */
    public void setAgencyPort(String agencyPort) {
        this.agencyPort = agencyPort;
    }

    /**
     * @return the agencyAddr
     */
    public String getAgencyAddr() {
        return agencyAddr;
    }

    /**
     * @param agencyAddr the agencyAddr to set
     */
    public void setAgencyAddr(String agencyAddr) {
        this.agencyAddr = agencyAddr;
    }

    /**
     * @return the agencyCity
     */
    public String getAgencyCity() {
        return agencyCity;
    }

    /**
     * @param agencyCity the agencyCity to set
     */
    public void setAgencyCity(String agencyCity) {
        this.agencyCity = agencyCity;
    }

    /**
     * @return the agencyCountry
     */
    public String getAgencyCountry() {
        return agencyCountry;
    }

    /**
     * @param agencyCountry the agencyCountry to set
     */
    public void setAgencyCountry(String agencyCountry) {
        this.agencyCountry = agencyCountry;
    }

    /**
     * @return the agencyTele
     */
    public String getAgencyTele() {
        return agencyTele;
    }

    /**
     * @param agencyTele the agencyTele to set
     */
    public void setAgencyTele(String agencyTele) {
        this.agencyTele = agencyTele;
    }

    /**
     * @return the agencyFax
     */
    public String getAgencyFax() {
        return agencyFax;
    }

    /**
     * @param agencyFax the agencyFax to set
     */
    public void setAgencyFax(String agencyFax) {
        this.agencyFax = agencyFax;
    }
}