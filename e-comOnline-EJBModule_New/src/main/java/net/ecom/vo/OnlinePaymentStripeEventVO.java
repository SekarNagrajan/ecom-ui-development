/*
 * Online Payment Stripe Event Value Object
 * Represents the Ecom_OnlinePayment_StripeEvent table structure
 */
package net.ecom.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Value Object for Stripe Event operations
 * Maps to Ecom_OnlinePayment_StripeEvent table
 * 
 * @author system
 */
public class OnlinePaymentStripeEventVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private long id;
    private String eventId;
    private String eventType;
    private UUID requestId;
    private String stripeSessionId;
    private String stripePaymentIntentId;
    private String failureCode;
    private String declineCode;
    private String failureMessage;
    private String failureType;
    private String paymentStatus;
    private Date receivedAt;
    
    // Default constructor
    public OnlinePaymentStripeEventVO() {
        this.receivedAt = new Date();
    }
    
    // Constructor with essential fields
    public OnlinePaymentStripeEventVO(String eventId, String eventType) {
        this();
        this.eventId = eventId;
        this.eventType = eventType;
    }
    
    // Constructor with all common fields
    public OnlinePaymentStripeEventVO(String eventId, String eventType, UUID requestId, 
                                     String stripeSessionId, String stripePaymentIntentId) {
        this();
        this.eventId = eventId;
        this.eventType = eventType;
        this.requestId = requestId;
        this.stripeSessionId = stripeSessionId;
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public UUID getRequestId() {
        return requestId;
    }
    
    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
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

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getDeclineCode() {
        return declineCode;
    }

    public void setDeclineCode(String declineCode) {
        this.declineCode = declineCode;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getFailureType() {
        return failureType;
    }

    public void setFailureType(String failureType) {
        this.failureType = failureType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public Date getReceivedAt() {
        return receivedAt;
    }
    
    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }
    
    @Override
    public String toString() {
        return "OnlinePaymentStripeEventVO{" +
                "id=" + id +
                ", eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", requestId=" + requestId +
                ", stripeSessionId='" + stripeSessionId + '\'' +
                ", stripePaymentIntentId='" + stripePaymentIntentId + '\'' +
                ", failureCode='" + failureCode + '\'' +
                ", declineCode='" + declineCode + '\'' +
                ", failureMessage='" + failureMessage + '\'' +
                ", failureType='" + failureType + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", receivedAt=" + receivedAt +
                '}';
    }
}