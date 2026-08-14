/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.insurance.vo;

import java.time.Instant;
import java.util.List;

/**
 *
 * @author vijay.j
 */
public class SwissApiInput {

    private String inceptionDate;
    private String onlineBookingNumber;
    private PolicyHolder policyHolder;
    private Transportation transportation;
    private List<InsuredGoodInput> insuredGoods;
    private String quoteNumber;
    private BookingParty bookingParty;
    /**
     * @return the inceptionDate
     */
    public String getInceptionDate() {
        return inceptionDate;
    }

    /**
     * @param inceptionDate the inceptionDate to set
     */
    public void setInceptionDate(String inceptionDate) {
        this.inceptionDate = inceptionDate;
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
     * @return the policyHolder
     */
    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    /**
     * @param policyHolder the policyHolder to set
     */
    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    /**
     * @return the transportation
     */
    public Transportation getTransportation() {
        return transportation;
    }

    /**
     * @param transportation the transportation to set
     */
    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    /**
     * @return the insuredGoods
     */
    public List<InsuredGoodInput> getInsuredGoods() {
        return insuredGoods;
    }

    /**
     * @param insuredGoods the insuredGoods to set
     */
    public void setInsuredGoods(List<InsuredGoodInput> insuredGoods) {
        this.insuredGoods = insuredGoods;
    }

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

    public BookingParty getBookingParty() {
        return bookingParty;
    }

    public void setBookingParty(BookingParty bookingParty) {
        this.bookingParty = bookingParty;
    }
    
}
