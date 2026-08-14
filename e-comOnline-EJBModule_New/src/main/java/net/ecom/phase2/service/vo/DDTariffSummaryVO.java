/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.phase2.service.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author deepa.p
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DDTariffSummary")
public class DDTariffSummaryVO {
    @XmlElement(name = "port")
    private String port = null;
    @XmlElement(name = "eqpType")
    private String eqpType = null;
    @XmlElement(name = "cntrStatus")
    private String cntrStatus = null;
    @XmlElement(name = "slabFrom")
    private String slabFrom = null;
    @XmlElement(name = "slabTo")
    private String slabTo = null;
    @XmlElement(name = "amount")
    private String amount = null;
    @XmlElement(name = "currency")
    private String currency = null;
    @XmlElement(name = "validFrom")
    private String validFrom = null;
    @XmlElement(name = "validTo")
    private String validTo = null;
    @XmlElement(name = "customerCode")
    private String customerCode = null;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCntrStatus() {
        return cntrStatus;
    }

    public void setCntrStatus(String cntrStatus) {
        this.cntrStatus = cntrStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getEqpType() {
        return eqpType;
    }

    public void setEqpType(String eqpType) {
        this.eqpType = eqpType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSlabFrom() {
        return slabFrom;
    }

    public void setSlabFrom(String slabFrom) {
        this.slabFrom = slabFrom;
    }

    public String getSlabTo() {
        return slabTo;
    }

    public void setSlabTo(String slabTo) {
        this.slabTo = slabTo;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}
