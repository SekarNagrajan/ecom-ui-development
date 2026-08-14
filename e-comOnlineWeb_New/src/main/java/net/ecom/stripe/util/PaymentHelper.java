package net.ecom.stripe.util;

import com.google.gson.Gson;
import net.ecom.common.LogUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Shared helper methods used across the Stripe payment module:
 * payment-type mapping, amount conversion, description building, URL utilities, and JSON serialization.
 */
public class PaymentHelper {

    private static final String TAG = "[PaymentHelper]";
    private static final Gson GSON = new Gson();

    private PaymentHelper() {
    }

    public static String mapPaymentType(String type) {
        switch (type) {
            case "BKG":
            case "BL":
                return "A";
            case "INV":
                return "I";
            default:
                return "A";
        }
    }

    public static String getPaymentTypeName(String type) {
        switch (type) {
            case "BKG": return "Booking";
            case "BL":  return "Bill of Lading";
            case "INV": return "Invoice";
            default:    return "Payment";
        }
    }

    public static long convertToCents(BigDecimal amount) {
        if (amount == null) return 0;
        return amount.multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
    }

    public static String getPaymentDescription(PaymentRequest request) {
        String typeName = getPaymentTypeName(request.getPaymentType());
        int count = request.getReferences().size();
        return typeName + " Payment - " + count + " item" + (count > 1 ? "s" : "");
    }

    public static String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();
    }

    public static String toJson(PaymentResponse response) {
        return GSON.toJson(response);
    }

    public static PaymentRequest parsePaymentRequest(String json) {
        try {
            LogUtil.log(TAG, "Parsing payment request JSON, length=" + (json != null ? json.length() : 0));
            return GSON.fromJson(json, PaymentRequest.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid payment request format: " + e.getMessage());
        }
    }
}
