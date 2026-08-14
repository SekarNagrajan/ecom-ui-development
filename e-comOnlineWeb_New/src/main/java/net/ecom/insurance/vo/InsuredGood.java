/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.insurance.vo;

/**
 *
 * @author vijay.j
 */
public class InsuredGood {

    private String harmonizedSystemCode;
    private String currency;
    private String sumInsured;
    private String premium;
    private boolean isCovered;
    /**
     * @return the harmonizedSystemCode
     */
    public String getHarmonizedSystemCode() {
        return harmonizedSystemCode;
    }

    /**
     * @param harmonizedSystemCode the harmonizedSystemCode to set
     */
    public void setHarmonizedSystemCode(String harmonizedSystemCode) {
        this.harmonizedSystemCode = harmonizedSystemCode;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the sumInsured
     */
    public String getSumInsured() {
        return sumInsured;
    }

    /**
     * @param sumInsured the sumInsured to set
     */
    public void setSumInsured(String sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public boolean isIsCovered() {
        return isCovered;
    }

    public void setIsCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }
    
}
