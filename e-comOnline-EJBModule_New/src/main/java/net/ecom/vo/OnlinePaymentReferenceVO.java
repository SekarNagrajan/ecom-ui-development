/*
 * Online Payment Reference Value Object
 * Represents the Ecom_OnlinePayment_Reference table structure
 */
package net.ecom.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Value Object for Online Payment Reference operations
 * Maps to Ecom_OnlinePayment_Reference table
 * 
 * @author system
 */
public class OnlinePaymentReferenceVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private long id;
    private UUID requestId;
    private String refType; // BKG / BL / INV
    private String refNo;
    private String custType;
    private BigDecimal amountUsd;
    private long amountCents;
    private String remarks;
    private Date createdAt;
    
    // Default constructor
    public OnlinePaymentReferenceVO() {
        this.createdAt = new Date();
    }
    
    // Constructor with essential fields
    public OnlinePaymentReferenceVO(UUID requestId, String refType, String refNo, 
                                   String custType, BigDecimal amountUsd, long amountCents) {
        this();
        this.requestId = requestId;
        this.refType = refType;
        this.refNo = refNo;
        this.custType = custType;
        this.amountUsd = amountUsd;
        this.amountCents = amountCents;
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
    
    public String getRefType() {
        return refType;
    }
    
    public void setRefType(String refType) {
        this.refType = refType;
    }
    
    public String getRefNo() {
        return refNo;
    }
    
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
    
    public String getCustType() {
        return custType;
    }
    
    public void setCustType(String custType) {
        this.custType = custType;
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
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "OnlinePaymentReferenceVO{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", refType='" + refType + '\'' +
                ", refNo='" + refNo + '\'' +
                ", custType='" + custType + '\'' +
                ", amountUsd=" + amountUsd +
                ", amountCents=" + amountCents +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}