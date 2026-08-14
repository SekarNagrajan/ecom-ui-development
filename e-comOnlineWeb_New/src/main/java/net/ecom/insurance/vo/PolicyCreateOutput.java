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
public class PolicyCreateOutput {

    private String quoteNumber;
    private int totalPremium;

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
     * @return the totalPremium
     */
    public int getTotalPremium() {
        return totalPremium;
    }

    /**
     * @param totalPremium the totalPremium to set
     */
    public void setTotalPremium(int totalPremium) {
        this.totalPremium = totalPremium;
    }
}
