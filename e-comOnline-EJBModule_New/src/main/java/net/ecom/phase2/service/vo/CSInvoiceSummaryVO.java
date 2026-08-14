/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.phase2.service.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class CSInvoiceSummaryVO implements Serializable {

    @SerializedName("issueddate")
    @Expose
    private String blIssueDate = null;

    @SerializedName("invdate")
    @Expose
    private String invoiceDate = null;

    @SerializedName("blnumber")
    @Expose
    private String blNo = null;

    @SerializedName("invno")
    @Expose
    private String invoiceNo = null;

    @SerializedName("invamount")
    @Expose
    private String invAmount = null;

    @SerializedName("invcurrency")
    @Expose
    private String invCurrency = null;

    @SerializedName("invroe")
    @Expose
    private String invROE = null;

    @SerializedName("invamountusd")
    @Expose
    private String invAmountUSD = null;

    @SerializedName("paidamtusd")
    @Expose
    private String paidAmtUSD = null;

    @SerializedName("pol")
    @Expose
    private String pol = null;

    @SerializedName("pla")
    @Expose
    private String pla = null;

    @SerializedName("pod")
    @Expose
    private String pod = null;

    @SerializedName("pld")
    @Expose
    private String pld = null;

    @SerializedName("servicecode")
    @Expose
    private String service = null;

    @SerializedName("vslcode")
    @Expose
    private String vessel = null;

    @SerializedName("voycode")
    @Expose
    private String voyage = null;

    @SerializedName("bookno")
    @Expose
    private String bookNo = null;

    @SerializedName("invtype")
    @Expose
    private String invType = null;

    @SerializedName("custrefno")
    @Expose
    private String refNo = null;

    @SerializedName("bound")
    @Expose
    private String bound = null;

    @SerializedName("locationcode")
    @Expose
    private String locationCode = null;

    @SerializedName("invbalamount")
    @Expose
    private String invBalAmount = null;

    @SerializedName("invbalamountusd")
    @Expose
    private String invBalAmountUSD = null;

    @SerializedName("pay_customer_type")
    @Expose
    private String pay_customer_type;

    @SerializedName("is_payment_eligible")
    @Expose
    private String is_payment_eligible;

    @SerializedName("is_payment_completed")
    @Expose
    private String is_payment_completed;

    @SerializedName("paymentRequestId")
    @Expose
    private String paymentRequestId;

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

    public String getPay_customer_type() {
        return pay_customer_type;
    }

    public void setPay_customer_type(String pay_customer_type) {
        this.pay_customer_type = pay_customer_type;
    }

    public String getIs_payment_eligible() {
        return is_payment_eligible;
    }

    public void setIs_payment_eligible(String is_payment_eligible) {
        this.is_payment_eligible = is_payment_eligible;
    }

    public String getIs_payment_completed() {
        return is_payment_completed;
    }

    public void setIs_payment_completed(String is_payment_completed) {
        this.is_payment_completed = is_payment_completed;
    }
    
    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getInvBalAmount() {
        return invBalAmount;
    }

    public void setInvBalAmount(String invBalAmount) {
        this.invBalAmount = invBalAmount;
    }

    public String getInvBalAmountUSD() {
        return invBalAmountUSD;
    }

    public void setInvBalAmountUSD(String invBalAmountUSD) {
        this.invBalAmountUSD = invBalAmountUSD;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
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

    public String getBlIssueDate() {
        return blIssueDate;
    }

    public void setBlIssueDate(String blIssueDate) {
        this.blIssueDate = blIssueDate;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(String invAmount) {
        this.invAmount = invAmount;
    }

    public String getInvAmountUSD() {
        return invAmountUSD;
    }

    public void setInvAmountUSD(String invAmountUSD) {
        this.invAmountUSD = invAmountUSD;
    }

    public String getInvCurrency() {
        return invCurrency;
    }

    public void setInvCurrency(String invCurrency) {
        this.invCurrency = invCurrency;
    }

    public String getInvROE() {
        return invROE;
    }

    public void setInvROE(String invROE) {
        this.invROE = invROE;
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

    public String getPaidAmtUSD() {
        return paidAmtUSD;
    }

    public void setPaidAmtUSD(String paidAmtUSD) {
        this.paidAmtUSD = paidAmtUSD;
    }

    public String getPla() {
        return pla;
    }

    public void setPla(String pla) {
        this.pla = pla;
    }

    public String getPld() {
        return pld;
    }

    public void setPld(String pld) {
        this.pld = pld;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }
}
