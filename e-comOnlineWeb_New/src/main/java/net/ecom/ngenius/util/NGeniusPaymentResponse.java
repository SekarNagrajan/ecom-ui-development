package net.ecom.ngenius.util;

/**
 * DTO representing the result of an N-Genius order creation attempt.
 * The field name {@code ngeniusOrderUrl} is intentional — the JS client reads this key.
 */
public class NGeniusPaymentResponse {

    private boolean success;
    private String message;
    private String ngeniusOrderUrl;
    private String requestId;

    NGeniusPaymentResponse() {
    }

    public static NGeniusPaymentResponse success(String ngeniusOrderUrl, String requestId) {
        NGeniusPaymentResponse r = new NGeniusPaymentResponse();
        r.success = true;
        r.ngeniusOrderUrl = ngeniusOrderUrl;
        r.requestId = requestId;
        return r;
    }

    public static NGeniusPaymentResponse error(String message) {
        NGeniusPaymentResponse r = new NGeniusPaymentResponse();
        r.success = false;
        r.message = message;
        return r;
    }

    public boolean isSuccess()         { return success; }
    public String getMessage()         { return message; }
    public String getNgeniusOrderUrl() { return ngeniusOrderUrl; }
    public String getRequestId()       { return requestId; }
}
