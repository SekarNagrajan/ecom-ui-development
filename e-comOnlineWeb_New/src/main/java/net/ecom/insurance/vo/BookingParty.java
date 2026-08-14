/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.insurance.vo;

import java.util.List;

/**
 *
 * @author gobinath.t
 */
public class BookingParty {

    private String contactType;
    private String contactName;
    private Address address;
    private String primaryEmailAddress;
    private List<String> additionalEmailAddresses;

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
