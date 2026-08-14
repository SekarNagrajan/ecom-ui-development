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
public class PolicyHolder {

    private String insuredName;
    private Address address;
    private String primaryEmailAddress;
    private String companyId;
    private List<String> additionalEmailAddresses;

    /**
     * @return the insuredName
     */
    public String getInsuredName() {
        return insuredName;
    }

    /**
     * @param insuredName the insuredName to set
     */
    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the emailAddress
     */

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPrimaryEmailAddress() {
        return primaryEmailAddress;
    }

    public void setPrimaryEmailAddress(String primaryEmailAddress) {
        this.primaryEmailAddress = primaryEmailAddress;
    }

    public List<String> getAdditionalEmailAddresses() {
        return additionalEmailAddresses;
    }

    public void setAdditionalEmailAddresses(List<String> additionalEmailAddresses) {
        this.additionalEmailAddresses = additionalEmailAddresses;
    }
    
}
