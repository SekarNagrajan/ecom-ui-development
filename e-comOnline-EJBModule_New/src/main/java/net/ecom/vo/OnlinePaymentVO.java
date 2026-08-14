/*
 * Online Payment Value Object
 * Represents the Ecom_OnlinePayment table structure
 */
package net.ecom.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Value Object for Online Payment operations
 * Maps to Ecom_OnlinePayment table
 * 
 * @author system
 */
public class OnlinePaymentVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private long id;
    private UUID requestId;
    private String customerCode;
    private String loginName;
    private String paymentType; // A = Advance, I = Invoice
    private String currency;
    private BigDecimal amountUsd;
    private long amountCents;
    private String status;
    private String stripeSessionId;
    private String stripePaymentIntentId;
    private String stripeChargeId;
    private String stripeTransactionId;
    private String stripeReceiptUrl;
    private String description;
    private String errorMessage;
    private String agencyCode;
    private Date createdAt;
    private Date updatedAt;
    
    // Default constructor
    public OnlinePaymentVO() {
        this.currency = "USD";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    // Constructor with essential fields
    public OnlinePaymentVO(UUID requestId, String customerCode, String loginName, 
                          String paymentType, BigDecimal amountUsd, long amountCents, String status) {
        this();
        this.requestId = requestId;
        this.customerCode = customerCode;
        this.loginName = loginName;
        this.paymentType = paymentType;
        this.amountUsd = amountUsd;
        this.amountCents = amountCents;
        this.status = status;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public UUID getRequestId() {
        return requestId;
    }
    
    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
    
    public String getCustomerCode() {
        return customerCode;
    }
    
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    
    public String getLoginName() {
        return loginName;
    }
    
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    
    public String getPaymentType() {
        return paymentType;
    }
    
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public BigDecimal getAmountUsd() {
        return amountUsd;
    }
    
    public void setAmountUsd(BigDecimal amountUsd) {
        this.amountUsd = amountUsd;
    }
    
    public long getAmountCents() {
        return amountCents;
    }
    
    public void setAmountCents(long amountCents) {
        this.amountCents = amountCents;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStripeSessionId() {
        return stripeSessionId;
    }
    
    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }
    
    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }
    
    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
    
    public String getStripeChargeId() {
        return stripeChargeId;
    }
    
    public void setStripeChargeId(String stripeChargeId) {
        this.stripeChargeId = stripeChargeId;
    }
    
    public String getStripeTransactionId() {
        return stripeTransactionId;
    }
    
    public void setStripeTransactionId(String stripeTransactionId) {
        this.stripeTransactionId = stripeTransactionId;
    }
    
    public String getStripeReceiptUrl() {
        return stripeReceiptUrl;
    }
    
    public void setStripeReceiptUrl(String stripeReceiptUrl) {
        this.stripeReceiptUrl = stripeReceiptUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getAgencyCode() {
        return agencyCode;
    }
    
    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "OnlinePaymentVO{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", customerCode='" + customerCode + '\'' +
                ", loginName='" + loginName + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", currency='" + currency + '\'' +
                ", amountUsd=" + amountUsd +
                ", amountCents=" + amountCents +
                ", status='" + status + '\'' +
                ", stripeSessionId='" + stripeSessionId + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}