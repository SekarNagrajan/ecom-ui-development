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
public class PaymentSummaryVO implements Serializable {

    @SerializedName("receiptno")
    @Expose
    private String receiptNo = null;

    @SerializedName("receiptmode")
    @Expose
    private String paymentMode = null;

    @SerializedName("invdate")
    @Expose
    private String invoiceDate = null;

    @SerializedName("bl_no")
    @Expose
    private String blNo = null;

    @SerializedName("invno")
    @Expose
    private String invoiceNo = null;

    @SerializedName("invamount")
    @Expose
    private String invAmount = null;

    @SerializedName("currency")
    @Expose
    private String invCurrency = null;

    @SerializedName("roe")
    @Expose
    private String invROE = null;

    @SerializedName("invamountinusd")
    @Expose
    private String invAmountUSD = null;

    @SerializedName("receiptamtinusd")
    @Expose
    private String paidAmtUSD = null;

    @SerializedName("balanceamtinusd")
    @Expose
    private String outStandingAmtUSD = null;

    @SerializedName("excessamtinusd")
    @Expose
    private String excessAmtUSD = null;

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

    @SerializedName("invservice")
    @Expose
    private String service = null;

    @SerializedName("invvessel")
    @Expose
    private String vessel = null;

    @SerializedName("invvoyage")
    @Expose
    private String voyage = null;

    @SerializedName("book_no")
    @Expose
    private String bookNo = null;

    @SerializedName("receiptdate")
    @Expose
    private String receiptDate = null;

    @SerializedName("paymenttype")
    @Expose
    private String paymentType = null;

    @SerializedName("receiptamt")
    @Expose
    private String paidAmt = null;

    @SerializedName("invbound")
    @Expose
    private String bound = null;

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
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

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getExcessAmtUSD() {
        return excessAmtUSD;
    }

    public void setExcessAmtUSD(String excessAmtUSD) {
        this.excessAmtUSD = excessAmtUSD;
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

    public String getOutStandingAmtUSD() {
        return outStandingAmtUSD;
    }

    public void setOutStandingAmtUSD(String outStandingAmtUSD) {
        this.outStandingAmtUSD = outStandingAmtUSD;
    }

    public String getPaidAmtUSD() {
        return paidAmtUSD;
    }

    public void setPaidAmtUSD(String paidAmtUSD) {
        this.paidAmtUSD = paidAmtUSD;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
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

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
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
