/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.insurance.vo;

import java.util.List;

/**
 *
 * @author vijay.j
 */
public class SwissApiOutput {

    private String quoteNumber;
    private String onlineBookingNumber;
    private String totalPremium;
    private String currency;
    private List<InsuredGood> insuredGoods;

    /**
     * @return the quoteNumber
     */
    public String getQuoteNumber() {
        return quoteNumber;
    }

    /**
     * @param quoteNumber the quoteNumber to set
     */
    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    /**
     * @return the onlineBookingNumber
     */
    public String getOnlineBookingNumber() {
        return onlineBookingNumber;
    }

    /**
     * @param onlineBookingNumber the onlineBookingNumber to set
     */
    public void setOnlineBookingNumber(String onlineBookingNumber) {
        this.onlineBookingNumber = onlineBookingNumber;
    }

    /**
     * @return the totalPremium
     */
    public String getTotalPremium() {
        return totalPremium;
    }

    /**
     * @param totalPremium the totalPremium to set
     */
    public void setTotalPremium(String totalPremium) {
        this.totalPremium = totalPremium;
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
     * @return the insuredGoods
     */
    public List<InsuredGood> getInsuredGoods() {
        return insuredGoods;
    }

    /**
     * @param insuredGoods the insuredGoods to set
     */
    public void setInsuredGoods(List<InsuredGood> insuredGoods) {
        this.insuredGoods = insuredGoods;
    }
}
