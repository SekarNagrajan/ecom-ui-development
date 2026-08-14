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
public class PolicyGetOutput {

    private String policyNumber;
    private String policyType;
    private float totalPremium;
    private String status;
    private String policyStartDate;
    private String policyEndDate;
    private String policyManagerId;
    private String policyRevisionId;
    private List<Agent> agents;
    private String onlineBookingNumber;
    private String currency;
    private List<InsuredGood> insuredGoods;

    /**
     * @return the policyNumber
     */
    public String getPolicyNumber() {
        return policyNumber;
    }

    /**
     * @param policyNumber the policyNumber to set
     */
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    /**
     * @return the policyType
     */
    public String getPolicyType() {
        return policyType;
    }

    /**
     * @param policyType the policyType to set
     */
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    /**
     * @return the totalPremium
     */
    public float getTotalPremium() {
        return totalPremium;
    }

    /**
     * @param totalPremium the totalPremium to set
     */
    public void setTotalPremium(int totalPremium) {
        this.totalPremium = totalPremium;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the policyStartDate
     */
    public String getPolicyStartDate() {
        return policyStartDate;
    }

    /**
     * @param policyStartDate the policyStartDate to set
     */
    public void setPolicyStartDate(String policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    /**
     * @return the policyEndDate
     */
    public String getPolicyEndDate() {
        return policyEndDate;
    }

    /**
     * @param policyEndDate the policyEndDate to set
     */
    public void setPolicyEndDate(String policyEndDate) {
        this.policyEndDate = policyEndDate;
    }

    /**
     * @return the policyManagerId
     */
    public String getPolicyManagerId() {
        return policyManagerId;
    }

    /**
     * @param policyManagerId the policyManagerId to set
     */
    public void setPolicyManagerId(String policyManagerId) {
        this.policyManagerId = policyManagerId;
    }

    /**
     * @return the policyRevisionId
     */
    public String getPolicyRevisionId() {
        return policyRevisionId;
    }

    /**
     * @param policyRevisionId the policyRevisionId to set
     */
    public void setPolicyRevisionId(String policyRevisionId) {
        this.policyRevisionId = policyRevisionId;
    }

    /**
     * @return the agents
     */
    public List<Agent> getAgents() {
        return agents;
    }

    /**
     * @param agents the agents to set
     */
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
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
