/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class ENSDeclarantVo implements Serializable {
    //ENS Filling

    private String euCustZone = null;
    private String blTypeENS = null;
    private String ensFillingType = null;
    private String paymentMethod = null;

    private String declarantName = null;
    private String declarantAddress = null;
    private String declarantAddress1 = null;
    private String declarantCity = null;
    private String declarantCountry = null;
    private String declarantState = null;
    private String declarantTelephone = null;
    private String declarantEORI = null;
    private String declarantEmail = null;
    private String declarantFillingType = null;
    private String declarantZipcode = null;

    public String getDeclarantZipcode() {
        return declarantZipcode;
    }

    public void setDeclarantZipcode(String declarantZipcode) {
        this.declarantZipcode = declarantZipcode;
    }

    public String getEuCustZone() {
        return euCustZone;
    }

    public void setEuCustZone(String euCustZone) {
        this.euCustZone = euCustZone;
    }

    public String getBlTypeENS() {
        return blTypeENS;
    }

    public void setBlTypeENS(String blTypeENS) {
        this.blTypeENS = blTypeENS;
    }

    public String getEnsFillingType() {
        return ensFillingType;
    }

    public void setEnsFillingType(String ensFillingType) {
        this.ensFillingType = ensFillingType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeclarantName() {
        return declarantName;
    }

    public void setDeclarantName(String declarantName) {
        this.declarantName = declarantName;
    }

    public String getDeclarantAddress() {
        return declarantAddress;
    }

    public void setDeclarantAddress(String declarantAddress) {
        this.declarantAddress = declarantAddress;
    }

    public String getDeclarantAddress1() {
        return declarantAddress1;
    }

    public void setDeclarantAddress1(String declarantAddress1) {
        this.declarantAddress1 = declarantAddress1;
    }

    public String getDeclarantCity() {
        return declarantCity;
    }

    public void setDeclarantCity(String declarantCity) {
        this.declarantCity = declarantCity;
    }

    public String getDeclarantCountry() {
        return declarantCountry;
    }

    public void setDeclarantCountry(String declarantCountry) {
        this.declarantCountry = declarantCountry;
    }

    public String getDeclarantState() {
        return declarantState;
    }

    public void setDeclarantState(String declarantState) {
        this.declarantState = declarantState;
    }

    public String getDeclarantTelephone() {
        return declarantTelephone;
    }

    public void setDeclarantTelephone(String declarantTelephone) {
        this.declarantTelephone = declarantTelephone;
    }

    public String getDeclarantEORI() {
        return declarantEORI;
    }

    public void setDeclarantEORI(String declarantEORI) {
        this.declarantEORI = declarantEORI;
    }

    public String getDeclarantEmail() {
        return declarantEmail;
    }

    public void setDeclarantEmail(String declarantEmail) {
        this.declarantEmail = declarantEmail;
    }

    public String getDeclarantFillingType() {
        return declarantFillingType;
    }

    public void setDeclarantFillingType(String declarantFillingType) {
        this.declarantFillingType = declarantFillingType;
    }

}
