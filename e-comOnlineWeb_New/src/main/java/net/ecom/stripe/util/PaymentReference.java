package net.ecom.stripe.util;

import java.math.BigDecimal;

/**
 * DTO representing a single payment reference (booking, BL, or invoice number with its amount).
 */
public class PaymentReference {

    private String refNo;
    private BigDecimal amount;
    private String customerType;

    public PaymentReference() {
    }

    public PaymentReference(String refNo, BigDecimal amount) {
        this.refNo = refNo;
        this.amount = amount;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}
