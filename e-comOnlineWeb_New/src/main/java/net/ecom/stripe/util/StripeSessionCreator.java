package net.ecom.stripe.util;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Creates Stripe Checkout sessions via the Stripe API.
 * Reads configuration (secret key, currency) from the {@link ServletContext}.
 */
public class StripeSessionCreator {

    private static final String TAG = "[StripeSessionCreator]";

    private StripeSessionCreator() {
    }

    public static Session create(UUID requestId, PaymentRequest paymentRequest,
                                  HttpServletRequest request) {
        try {
            String secretKey = getSecretKey(request);
            if (secretKey == null) {
                LogUtil.logException(TAG + " Stripe secret key not configured");
                return null;
            }
            Stripe.apiKey = secretKey;

            String currency = getCurrency(request);
            String baseUrl = PaymentHelper.getBaseUrl(request);
            LogUtil.log(TAG, "Creating session -> requestId=" + requestId
                    + ", currency=" + currency + ", baseUrl=" + baseUrl);

            SessionCreateParams params = buildSessionParams(requestId, paymentRequest, currency, baseUrl, request);
            Session session = Session.create(params);

            LogUtil.trace(TAG, "Stripe session created, sessionId=" + session.getId());
            LogUtil.log(TAG, "Session URL: " + session.getUrl());
            return session;

        } catch (Exception e) {
            LogUtil.logException(TAG + " create session failed for requestId=" + requestId, e);
            return null;
        }
    }

    private static String getSecretKey(HttpServletRequest request) {
        String secretKey = (String) request.getServletContext().getAttribute(GeneralConf.Stripe_SecretKey);
        return (secretKey != null && !secretKey.trim().isEmpty()) ? secretKey : null;
    }

    private static String getCurrency(HttpServletRequest request) {
        String currency = (String) request.getServletContext().getAttribute(GeneralConf.Stripe_Payment_Currency);
        return (currency != null) ? currency.toLowerCase() : "usd";
    }

    private static SessionCreateParams buildSessionParams(UUID requestId, PaymentRequest paymentRequest,
                                                          String currency, String baseUrl,
                                                          HttpServletRequest request) {
        String customerEmail = resolveCustomerEmail(request);

        SessionCreateParams.PaymentIntentData.Builder piData = SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("requestId", requestId.toString())
                .putMetadata("paymentType", paymentRequest.getPaymentType());
        if (customerEmail != null) {
            piData.putMetadata("customerEmail", customerEmail);
        }

        SessionCreateParams.Builder builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/stripeSuccess.do?session_id={CHECKOUT_SESSION_ID}&rid=" + requestId.toString())
                .setCancelUrl(baseUrl + "/stripeCancel.do?rid=" + requestId.toString())
                .addLineItem(buildLineItem(paymentRequest, currency))
                .putMetadata("requestId", requestId.toString())
                .putMetadata("paymentType", paymentRequest.getPaymentType())
                // Copy requestId onto the PaymentIntent so failed/declined webhooks can resolve the payment row
                .setPaymentIntentData(piData.build());

        if (customerEmail != null) {
            builder.setCustomerEmail(customerEmail);
            builder.putMetadata("customerEmail", customerEmail);
        }

        return builder.build();
    }

    private static String resolveCustomerEmail(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return null;
        }
        Object email = request.getSession(false).getAttribute("SESSION_EMAIL");
        if (email == null) {
            return null;
        }
        String value = email.toString().trim();
        return value.isEmpty() ? null : value;
    }

    private static SessionCreateParams.LineItem buildLineItem(PaymentRequest paymentRequest, String currency) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(PaymentHelper.convertToCents(paymentRequest.getTotalAmount()))
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(PaymentHelper.getPaymentDescription(paymentRequest))
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
