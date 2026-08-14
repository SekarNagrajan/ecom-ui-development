package net.ecom.stripe.util;

import com.stripe.model.checkout.Session;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Facade / orchestrator for Stripe payment processing.
 *
 * Delegates to single-responsibility classes:
 * <ul>
 *   <li>{@link PaymentValidator}      – input validation</li>
 *   <li>{@link PaymentRepository}     – database persistence</li>
 *   <li>{@link StripeSessionCreator}  – Stripe Checkout session creation</li>
 *   <li>{@link PaymentHelper}         – shared helpers &amp; JSON serialization</li>
 * </ul>
 *
 * DTOs: {@link PaymentRequest}, {@link PaymentReference}, {@link PaymentResponse}
 */
public class StripeUtil {

    private static final String TAG = "[StripeUtil]";

    private StripeUtil() {
    }

    public static PaymentResponse createPaymentSession(PaymentRequest paymentRequest,
                                                       String customerCode,
                                                       String loginName,
                                                       HttpServletRequest request) {
        try {
            LogUtil.trace(TAG, "createPaymentSession -> customer=" + customerCode
                    + ", type=" + paymentRequest.getPaymentType());

            String validationError = PaymentValidator.validate(paymentRequest);
            if (validationError != null) {
                LogUtil.trace(TAG, "Validation failed: " + validationError);
                return PaymentResponse.error(validationError);
            }

            UUID requestId = UUID.randomUUID();
            LogUtil.log(TAG, "Generated requestId=" + requestId
                    + ", amount=" + paymentRequest.getTotalAmount()
                    + ", refs=" + paymentRequest.getReferences().size());

            String currency = (String) request.getServletContext().getAttribute(GeneralConf.Stripe_Payment_Currency);

            boolean dbResult = PaymentRepository.createPaymentRecord(requestId, paymentRequest, customerCode, loginName, currency);
            if (!dbResult) {
                LogUtil.logException(TAG + " Failed to create payment record for requestId=" + requestId);
                return PaymentResponse.error("Failed to create payment record");
            }

            Session stripeSession = StripeSessionCreator.create(requestId, paymentRequest, request);
            if (stripeSession == null) {
                LogUtil.logException(TAG + " Failed to create Stripe session for requestId=" + requestId);
                return PaymentResponse.error("Failed to create Stripe session");
            }

            PaymentRepository.updateStripeSessionId(requestId, stripeSession.getId());

            LogUtil.trace(TAG, "createPaymentSession -> SUCCESS, requestId=" + requestId
                    + ", sessionId=" + stripeSession.getId());
            LogUtil.log(TAG, "Stripe redirect URL: " + stripeSession.getUrl());
            return PaymentResponse.success(stripeSession.getUrl(), requestId.toString());

        } catch (Exception e) {
            LogUtil.logException(TAG, e);
            return PaymentResponse.error("Payment setup failed");
        }
    }

}
