package net.ecom.stripe.util;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO representing a payment request from the frontend.
 * Supports payment types: BKG (Booking), BL (Bill of Lading), INV (Invoice).
 */
public class PaymentRequest {

    private String paymentType;
    private String customerType;
    private String agencyCode;
    private BigDecimal totalAmount;
    private List<PaymentReference> references;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<PaymentReference> getReferences() {
        return references;
    }

    public void setReferences(List<PaymentReference> references) {
        this.references = references;
    }
}
