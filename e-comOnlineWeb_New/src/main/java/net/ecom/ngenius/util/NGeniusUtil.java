package net.ecom.ngenius.util;

import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;
import net.ecom.ngenius.util.NGeniusOrderCreator.NGeniusOrderResult;
import net.ecom.stripe.util.PaymentHelper;
import net.ecom.stripe.util.PaymentRepository;
import net.ecom.stripe.util.PaymentRequest;
import net.ecom.stripe.util.PaymentValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Orchestrator for N-Genius payment processing.
 *
 * Reuses existing Stripe module classes for DB persistence:
 * - {@link PaymentValidator}  — same input validation rules
 * - {@link PaymentRepository} — same EJB + same {@code Ecom_OnlinePayment} table
 * - {@link PaymentHelper}     — same helpers (amount conversion, base URL, etc.)
 *
 * N-Genius specific columns mapped to existing Stripe columns in the table:
 * - stripeSessionId        → N-Genius order reference UUID
 * - stripePaymentIntentId  → N-Genius payment reference UUID (from webhook)
 * - stripeChargeId         → N-Genius authorization code
 * - stripeTransactionId    → N-Genius RRN (Retrieval Reference Number)
 */
public class NGeniusUtil {

    private static final String TAG = "[NGeniusUtil]";

    private NGeniusUtil() {
    }

    public static NGeniusPaymentResponse createPaymentOrder(PaymentRequest paymentRequest,
                                                             String customerCode,
                                                             String loginName,
                                                             HttpServletRequest request) {
        try {
            LogUtil.trace(TAG, "createPaymentOrder -> customer=" + customerCode
                    + ", type=" + paymentRequest.getPaymentType());

            // 1. Validate
            String validationError = PaymentValidator.validate(paymentRequest);
            if (validationError != null) {
                LogUtil.trace(TAG, "Validation failed: " + validationError);
                return NGeniusPaymentResponse.error(validationError);
            }

            // 2. Read N-Genius config from servlet context
            String apiKey    = (String) request.getServletContext().getAttribute(GeneralConf.Ngenius_API_Key);
            String outletRef = (String) request.getServletContext().getAttribute(GeneralConf.Ngenius_Outlet_Id);
            String currency  = (String) request.getServletContext().getAttribute(GeneralConf.Ngenius_Payment_Currency);

            if (apiKey == null || apiKey.trim().isEmpty()) {
                LogUtil.logException(TAG + " N-Genius API key not configured");
                return NGeniusPaymentResponse.error("Payment gateway not configured");
            }
            if (outletRef == null || outletRef.trim().isEmpty()) {
                LogUtil.logException(TAG + " N-Genius Outlet ID not configured");
                return NGeniusPaymentResponse.error("Payment gateway not configured");
            }
            if (currency == null || currency.trim().isEmpty()) {
                currency = "USD";
            }

            // 3. Generate requestId and persist PENDING record (reusing Stripe PaymentRepository)
            UUID requestId = UUID.randomUUID();
            LogUtil.log(TAG, "Generated requestId=" + requestId
                    + ", amount=" + paymentRequest.getTotalAmount()
                    + ", refs=" + paymentRequest.getReferences().size());

            boolean dbResult = PaymentRepository.createPaymentRecord(requestId, paymentRequest, customerCode, loginName, currency);
            if (!dbResult) {
                LogUtil.logException(TAG + " Failed to create payment record for requestId=" + requestId);
                return NGeniusPaymentResponse.error("Failed to create payment record");
            }

            // 4. Create N-Genius order (token fetch + order POST)
            NGeniusOrderResult orderResult = NGeniusOrderCreator.create(
                    requestId, paymentRequest, apiKey.trim(), outletRef.trim(), currency.trim(), loginName, request);

            if (orderResult == null) {
                LogUtil.logException(TAG + " Failed to create N-Genius order for requestId=" + requestId);
                return NGeniusPaymentResponse.error("Failed to create payment session");
            }

            // 5. Persist N-Genius order reference (stored in stripeSessionId column)
            PaymentRepository.updateStripeSessionId(requestId, orderResult.getOrderRef());

            LogUtil.trace(TAG, "createPaymentOrder -> SUCCESS, requestId=" + requestId
                    + ", orderRef=" + orderResult.getOrderRef()
                    + ", payPageUrl=" + orderResult.getPayPageUrl());

            return NGeniusPaymentResponse.success(orderResult.getPayPageUrl(), requestId.toString());

        } catch (Exception e) {
            LogUtil.logException(TAG, e);
            return NGeniusPaymentResponse.error("Payment setup failed");
        }
    }
}
