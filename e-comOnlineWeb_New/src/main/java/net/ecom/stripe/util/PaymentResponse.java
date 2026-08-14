package net.ecom.stripe.util;

/**
 * DTO representing the result of a payment session creation attempt.
 * Use the static factory methods {@link #success} and {@link #error} to create instances.
 */
public class PaymentResponse {

    private boolean success;
    private String message;
    private String stripeSessionUrl;
    private String requestId;

    PaymentResponse() {
    }

    public static PaymentResponse success(String stripeSessionUrl, String requestId) {
        PaymentResponse response = new PaymentResponse();
        response.success = true;
        response.stripeSessionUrl = stripeSessionUrl;
        response.requestId = requestId;
        return response;
    }

    public static PaymentResponse error(String message) {
        PaymentResponse response = new PaymentResponse();
        response.success = false;
        response.message = message;
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getStripeSessionUrl() {
        return stripeSessionUrl;
    }

    public String getRequestId() {
        return requestId;
    }
}
