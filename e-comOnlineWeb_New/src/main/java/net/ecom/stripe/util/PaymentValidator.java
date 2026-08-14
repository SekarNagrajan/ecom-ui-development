package net.ecom.stripe.util;

import java.math.BigDecimal;

/**
 * Validates payment requests before processing.
 * Returns {@code null} when the request is valid, or an error message string otherwise.
 */
public class PaymentValidator {

    private static final BigDecimal MAX_AMOUNT = new BigDecimal("999999.99");
    private static final int MAX_REF_NO_LENGTH = 50;
    private static final int MAX_REFERENCES = 100;

    private PaymentValidator() {
    }

    /**
     * Validate a payment request for completeness and correctness.
     *
     * @param request the payment request to validate
     * @return {@code null} if valid; an error message string if invalid
     */
    public static String validate(PaymentRequest request) {
        if (request == null) {
            return "Payment request is required";
        }

        if (!isValidPaymentType(request.getPaymentType())) {
            return "Invalid payment type";
        }

        if (request.getTotalAmount() == null
                || request.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Invalid payment amount";
        }
        if (request.getTotalAmount().compareTo(MAX_AMOUNT) > 0) {
            return "Payment amount exceeds maximum allowed (" + MAX_AMOUNT + ")";
        }

        if (request.getTotalAmount().scale() > 2) {
            return "Payment amount must not have more than 2 decimal places";
        }

        if (request.getReferences() == null || request.getReferences().isEmpty()) {
            return "Payment references are required";
        }

        if (request.getReferences().size() > MAX_REFERENCES) {
            return "Too many references (max " + MAX_REFERENCES + ")";
        }

        for (int i = 0; i < request.getReferences().size(); i++) {
            PaymentReference ref = request.getReferences().get(i);
            String refError = validateReference(ref, i);
            if (refError != null) {
                return refError;
            }
        }

        return null;
    }

    private static String validateReference(PaymentReference ref, int index) {
        if (ref == null) {
            return "Reference at position " + (index + 1) + " is null";
        }

        String refNo = ref.getRefNo();
        if (refNo == null || refNo.trim().isEmpty()) {
            return "Reference number is required at position " + (index + 1);
        }

        if (refNo.length() > MAX_REF_NO_LENGTH) {
            return "Reference number too long at position " + (index + 1);
        }

        if (ref.getAmount() != null && ref.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return "Reference amount cannot be negative at position " + (index + 1);
        }

        return null;
    }

    private static boolean isValidPaymentType(String type) {
        return "BKG".equals(type) || "BL".equals(type) || "INV".equals(type);
    }
}
