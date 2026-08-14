/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.phase2.service.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InvoiceSummary")
public class InvoiceSummaryVO {
    @XmlElement(name = "invoiceNo")
    private String invoiceNo = null;
    @XmlElement(name = "invoiceDate")
    private String invoiceDate = null;
    @XmlElement(name = "invoiceAmount")
    private String invoiceAmount = null;
    @XmlElement(name = "invoiceCurrency")
    private String invoiceCurrency = null;
    @XmlElement(name = "invoiceType")
    private String invoiceType = null;
    @XmlElement(name = "blNumber")
    private String blNumber = null;
    @XmlElement(name = "vessel")
    private String vessel = null;
    @XmlElement(name = "voyage")
    private String voyage = null;
    @XmlElement(name = "invoiceAmtUsd")
    private String invoiceAmtUsd = null;

    public String getInvoiceAmtUsd() {
        return invoiceAmtUsd;
    }

    public void setInvoiceAmtUsd(String invoiceAmtUsd) {
        this.invoiceAmtUsd = invoiceAmtUsd;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

}
