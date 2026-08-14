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
public class InsuredGoodInput {

    private String harmonizedSystemCode;
    private String currency;
    private String sumInsured;
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

}
