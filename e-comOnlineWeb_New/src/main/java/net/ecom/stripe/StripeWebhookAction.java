package net.ecom.stripe;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionListParams;

import net.ecom.vo.OnlinePaymentStripeEventVO;
import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.common.LogUtil;
import net.ecom.common.GeneralConf;
import net.ecom.common.PaymentEventNotificationMail;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.util.stream.Collectors;
import java.util.UUID;

public class StripeWebhookAction extends Action {

    private static final String TAG = "[StripeWebhook]";

    private static final int MAX_LEN_FAILURE_CODE    = 30;
    private static final int MAX_LEN_DECLINE_CODE    = 30;
    private static final int MAX_LEN_FAILURE_MESSAGE = 500;
    private static final int MAX_LEN_FAILURE_TYPE    = 30;
    private static final int MAX_LEN_PAYMENT_STATUS  = 30;

    private String safeTrim(String value, int maxLen, String fieldName, String eventId) {
        if (value == null) return null;
        if (value.length() <= maxLen) return value;
        LogUtil.trace(TAG, "Trimming " + fieldName + " from " + value.length()
                + " to " + maxLen + " chars for event " + eventId
                + " | original: " + value);
        return value.substring(0, maxLen);
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "Webhook received");

        String payload;
        try (BufferedReader reader = request.getReader()) {
            payload = reader.lines().collect(Collectors.joining("\n"));
        }

        String sigHeader = request.getHeader("Stripe-Signature");
        String endpointSecret = (String) request.getServletContext().getAttribute(GeneralConf.Stripe_WebhookKey);

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            LogUtil.logException(TAG + " Invalid webhook signature");
            response.setStatus(400);
            response.getWriter().write("Invalid signature");
            return null;
        }

        String secretKey = (String) request.getServletContext().getAttribute(GeneralConf.Stripe_SecretKey);
        if (secretKey != null) {
            Stripe.apiKey = secretKey;
        }

        String eventType = event.getType();
        String eventId = event.getId();

        LogUtil.trace(TAG, "Event: " + eventId + " type=" + eventType);

        String notificationStatus = mapStripeEventStatus(eventType);
        String additionalInfo = null;
        try {
            storeStripeEvent(event);

            switch (eventType) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event, request.getServletContext());
                    additionalInfo = buildSessionNotificationDetails(event);
                    break;
                case "checkout.session.expired":
                    handleCheckoutSessionExpired(event);
                    additionalInfo = buildSessionNotificationDetails(event);
                    break;
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded(event);
                    additionalInfo = buildPaymentIntentNotificationDetails(event, false);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentIntentFailed(event, request.getServletContext());
                    additionalInfo = buildPaymentIntentNotificationDetails(event, true);
                    break;
                case "payment_intent.canceled":
                    handlePaymentIntentCanceled(event);
                    additionalInfo = buildPaymentIntentNotificationDetails(event, true);
                    break;
                default:
                    LogUtil.log(TAG, "Unhandled event type: " + eventType);
                    break;
            }

        } catch (Exception e) {
            LogUtil.logException(TAG + " Error processing event " + eventId, e);
            notificationStatus = "ERROR";
            if (additionalInfo == null) {
                additionalInfo = "Processing error: " + e.getMessage();
            }
        }

        PaymentEventNotificationMail.notifyIfConfigured(
                request.getServletContext(),
                "Stripe",
                eventType,
                notificationStatus,
                eventId,
                additionalInfo);

        response.setStatus(200);
        response.getWriter().write("OK");
        return null;
    }

    private String mapStripeEventStatus(String eventType) {
        if (eventType == null) {
            return "UNKNOWN";
        }
        switch (eventType) {
            case "checkout.session.completed":
            case "payment_intent.succeeded":
                return "COMPLETED";
            case "checkout.session.expired":
                return "EXPIRED";
            case "payment_intent.payment_failed":
                return "FAILED";
            case "payment_intent.canceled":
                return "CANCELLED";
            default:
                return eventType;
        }
    }

    // =========================================================================
    // Event data deserialization
    // =========================================================================

    private StripeObject getEventDataObject(Event event) {
        if (event.getDataObjectDeserializer() == null) {
            LogUtil.logException(TAG + " Deserializer null for event: " + event.getId());
            return null;
        }

        if (event.getDataObjectDeserializer().getObject().isPresent()) {
            return event.getDataObjectDeserializer().getObject().get();
        }

        try {
            LogUtil.log(TAG, "getObject() empty for " + event.getId() + ", trying deserializeUnsafe()");
            return event.getDataObjectDeserializer().deserializeUnsafe();
        } catch (Exception e) {
            LogUtil.logException(TAG + " deserializeUnsafe() failed for " + event.getId() + ": " + e.getMessage());
            return null;
        }
    }

    // =========================================================================
    // Store event in DB
    // =========================================================================

    private void storeStripeEvent(Event event) {
        try {
            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();

            OnlinePaymentStripeEventVO eventVO = new OnlinePaymentStripeEventVO();
            eventVO.setEventId(event.getId());
            eventVO.setEventType(event.getType());

            StripeObject eventData = getEventDataObject(event);
            if (eventData != null) {
                String eid = event.getId();
                if (eventData instanceof Session) {
                    Session session = (Session) eventData;
                    eventVO.setStripeSessionId(session.getId());
                    eventVO.setStripePaymentIntentId(session.getPaymentIntent());
                    eventVO.setPaymentStatus(safeTrim(session.getPaymentStatus(), MAX_LEN_PAYMENT_STATUS, "paymentStatus", eid));
                    if (session.getMetadata() != null && session.getMetadata().containsKey("requestId")) {
                        try {
                            eventVO.setRequestId(UUID.fromString(session.getMetadata().get("requestId")));
                        } catch (IllegalArgumentException ignored) { }
                    }
                } else if (eventData instanceof PaymentIntent) {
                    PaymentIntent pi = (PaymentIntent) eventData;
                    eventVO.setStripePaymentIntentId(pi.getId());
                    eventVO.setPaymentStatus(safeTrim(pi.getStatus(), MAX_LEN_PAYMENT_STATUS, "paymentStatus", eid));
                    if (pi.getLastPaymentError() != null) {
                        eventVO.setFailureCode(safeTrim(pi.getLastPaymentError().getCode(), MAX_LEN_FAILURE_CODE, "failureCode", eid));
                        eventVO.setDeclineCode(safeTrim(pi.getLastPaymentError().getDeclineCode(), MAX_LEN_DECLINE_CODE, "declineCode", eid));
                        eventVO.setFailureMessage(safeTrim(pi.getLastPaymentError().getMessage(), MAX_LEN_FAILURE_MESSAGE, "failureMessage", eid));
                        eventVO.setFailureType(safeTrim(pi.getLastPaymentError().getType(), MAX_LEN_FAILURE_TYPE, "failureType", eid));
                    }
                } else if (eventData instanceof Charge) {
                    Charge charge = (Charge) eventData;
                    eventVO.setStripePaymentIntentId(charge.getPaymentIntent());
                    eventVO.setPaymentStatus(safeTrim(charge.getStatus(), MAX_LEN_PAYMENT_STATUS, "paymentStatus", eid));
                    eventVO.setFailureCode(safeTrim(charge.getFailureCode(), MAX_LEN_FAILURE_CODE, "failureCode", eid));
                    eventVO.setFailureMessage(safeTrim(charge.getFailureMessage(), MAX_LEN_FAILURE_MESSAGE, "failureMessage", eid));
                }
                LogUtil.log(TAG, "Event data -> sessionId=" + eventVO.getStripeSessionId()
                        + ", PI=" + eventVO.getStripePaymentIntentId()
                        + ", requestId=" + eventVO.getRequestId()
                        + ", failureCode=" + eventVO.getFailureCode()
                        + ", declineCode=" + eventVO.getDeclineCode()
                        + ", paymentStatus=" + eventVO.getPaymentStatus());
            } else {
                LogUtil.logException(TAG + " Could not deserialize event data: " + event.getId());
            }

            long eventDbId = paymentBean.insertStripeEvent(eventVO);
            LogUtil.trace(TAG, "Stored event in DB, dbId=" + eventDbId + ", eventId=" + event.getId());

        } catch (Exception e) {
            LogUtil.logException(TAG + " Error storing event", e);
        }
    }

    // =========================================================================
    // checkout.session.completed
    // =========================================================================

    private void handleCheckoutSessionCompleted(Event event, javax.servlet.ServletContext context) {
        try {
            StripeObject eventData = getEventDataObject(event);
            if (!(eventData instanceof Session)) {
                LogUtil.logException(TAG + " checkout.session.completed: cannot deserialize Session");
                return;
            }

            Session session = (Session) eventData;
            LogUtil.trace(TAG, "checkout.session.completed -> " + session.getId());

            if (session.getMetadata() == null) {
                LogUtil.logException(TAG + " No metadata in session: " + session.getId());
                return;
            }

            String requestIdStr = session.getMetadata().get("requestId");
            if (requestIdStr == null) {
                LogUtil.logException(TAG + " No requestId in metadata for session: " + session.getId());
                return;
            }

            UUID requestId = UUID.fromString(requestIdStr);
            String paymentIntentId = session.getPaymentIntent();
            LogUtil.log(TAG, "Metadata -> requestId=" + requestIdStr
                    + ", paymentType=" + session.getMetadata().get("paymentType")
                    + ", PI=" + paymentIntentId);

            String chargeId = null;
            String transactionId = null;
            String receiptUrl = null;

            if (paymentIntentId != null) {
                try {
                    PaymentIntent pi = PaymentIntent.retrieve(paymentIntentId);
                    chargeId = pi.getLatestCharge();
                    LogUtil.log(TAG, "Retrieved PI -> latestCharge=" + chargeId);

                    if (chargeId != null) {
                        Charge charge = Charge.retrieve(chargeId);
                        transactionId = charge.getBalanceTransaction();
                        receiptUrl = charge.getReceiptUrl();
                        LogUtil.log(TAG, "Charge details -> txnId=" + transactionId
                                + ", receiptUrl=" + receiptUrl);
                    }
                } catch (Exception e) {
                    LogUtil.logException(TAG + " Could not retrieve charge for PI " + paymentIntentId, e);
                }
            }

            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
            int rows = paymentBean.updatePaymentWithStripeCompletion(
                    requestId, paymentIntentId, chargeId, transactionId, receiptUrl, "COMPLETED");

            LogUtil.trace(TAG, "Updated to COMPLETED -> requestId=" + requestId + ", rows=" + rows);

            if (rows > 0) {
                try {
                    boolean gatewayResult = paymentBean.insertPaymentGateway(requestId);
                    LogUtil.log(TAG, "PaymentGateway insert -> requestId=" + requestId + ", result=" + gatewayResult);
                } catch (Exception ge) {
                    LogUtil.logException(TAG + " PaymentGateway insert failed for requestId=" + requestId, ge);
                }

                // Customer success email when Enable_Stripe_Payment is Y (once, when first completed)
                String customerEmail = resolveCustomerEmailFromSession(session);
                String amountInfo = null;
                if (session.getAmountTotal() != null && session.getCurrency() != null) {
                    amountInfo = String.format("%.2f %s",
                            session.getAmountTotal() / 100.0,
                            session.getCurrency().toUpperCase());
                }
                PaymentEventNotificationMail.notifyCustomerPaymentSuccess(
                        context,
                        customerEmail,
                        "Stripe",
                        paymentIntentId != null ? paymentIntentId : session.getId(),
                        amountInfo);
            }

            if (paymentIntentId != null) {
                int backfilled = paymentBean.backfillStripeEventsByPaymentIntent(
                        paymentIntentId, requestId, session.getId());
                LogUtil.log(TAG, "Backfilled " + backfilled + " event rows for PI=" + paymentIntentId);
            }

        } catch (Exception e) {
            LogUtil.logException(TAG + " Error in checkout.session.completed", e);
        }
    }

    // =========================================================================
    // checkout.session.expired
    // =========================================================================

    private void handleCheckoutSessionExpired(Event event) {
        try {
            StripeObject eventData = getEventDataObject(event);
            if (!(eventData instanceof Session)) {
                LogUtil.logException(TAG + " checkout.session.expired: cannot deserialize Session");
                return;
            }

            Session session = (Session) eventData;
            LogUtil.trace(TAG, "checkout.session.expired -> " + session.getId());

            if (session.getMetadata() != null && session.getMetadata().containsKey("requestId")) {
                UUID requestId = UUID.fromString(session.getMetadata().get("requestId"));
                EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
                paymentBean.updatePaymentWithError(requestId,
                        "Checkout session expired - customer did not complete payment", "EXPIRED");
                LogUtil.trace(TAG, "Updated to EXPIRED -> requestId=" + requestId);
            }
        } catch (Exception e) {
            LogUtil.logException(TAG + " Error in checkout.session.expired", e);
        }
    }

    // =========================================================================
    // payment_intent.succeeded
    // =========================================================================

    private void handlePaymentIntentSucceeded(Event event) {
        try {
            StripeObject eventData = getEventDataObject(event);
            if (!(eventData instanceof PaymentIntent)) {
                LogUtil.logException(TAG + " payment_intent.succeeded: cannot deserialize PI");
                return;
            }

            PaymentIntent pi = (PaymentIntent) eventData;
            LogUtil.trace(TAG, "payment_intent.succeeded -> " + pi.getId());

            String chargeId = pi.getLatestCharge();
            String transactionId = null;
            String receiptUrl = null;

            if (chargeId != null) {
                try {
                    Charge charge = Charge.retrieve(chargeId);
                    transactionId = charge.getBalanceTransaction();
                    receiptUrl = charge.getReceiptUrl();
                    LogUtil.log(TAG, "PI charge -> chargeId=" + chargeId
                            + ", txnId=" + transactionId);
                } catch (Exception e) {
                    LogUtil.logException(TAG + " Could not retrieve charge " + chargeId, e);
                }
            }

            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
            int rows = paymentBean.updatePaymentDetailsByPaymentIntent(
                    pi.getId(), chargeId, transactionId, receiptUrl);

            LogUtil.trace(TAG, "PI succeeded fallback update -> PI=" + pi.getId() + ", rows=" + rows);

        } catch (Exception e) {
            LogUtil.logException(TAG + " Error in payment_intent.succeeded", e);
        }
    }

    // =========================================================================
    // payment_intent.payment_failed
    // =========================================================================

    private void handlePaymentIntentFailed(Event event, javax.servlet.ServletContext context) {
        try {
            StripeObject eventData = getEventDataObject(event);
            if (!(eventData instanceof PaymentIntent)) {
                LogUtil.logException(TAG + " payment_intent.payment_failed: cannot deserialize PI");
                return;
            }

            PaymentIntent pi = (PaymentIntent) eventData;
            String errorMessage = "Payment failed";
            if (pi.getLastPaymentError() != null) {
                errorMessage = pi.getLastPaymentError().getMessage();
                if (errorMessage == null || errorMessage.trim().isEmpty()) {
                    errorMessage = "Payment failed - " + pi.getLastPaymentError().getCode();
                }
            }

            LogUtil.trace(TAG, "payment_intent.failed -> PI=" + pi.getId() + ", error=" + errorMessage);

            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
            int rows = paymentBean.updatePaymentErrorByPaymentIntent(pi.getId(), errorMessage, "FAILED");

            // Fallback: Checkout often has only sessionId on the payment row until success;
            // resolve via PaymentIntent metadata.requestId when present.
            if (rows == 0) {
                UUID requestId = extractRequestIdFromMetadata(pi.getMetadata());
                if (requestId != null) {
                    rows = paymentBean.updatePaymentWithError(requestId, errorMessage, "FAILED");
                    LogUtil.trace(TAG, "Updated to FAILED via requestId metadata -> requestId="
                            + requestId + ", rows=" + rows);
                }
            } else {
                LogUtil.trace(TAG, "Updated to FAILED -> PI=" + pi.getId() + ", rows=" + rows);
            }

            // Stripe does not email customers for declined one-time Checkout payments — send ours.
            String customerEmail = resolveCustomerEmail(pi);
            PaymentEventNotificationMail.notifyCustomerPaymentFailed(
                    context,
                    customerEmail,
                    "Stripe",
                    pi.getId(),
                    errorMessage);

        } catch (Exception e) {
            LogUtil.logException(TAG + " Error in payment_intent.payment_failed", e);
        }
    }

    // =========================================================================
    // payment_intent.canceled
    // =========================================================================

    private void handlePaymentIntentCanceled(Event event) {
        try {
            StripeObject eventData = getEventDataObject(event);
            if (!(eventData instanceof PaymentIntent)) {
                LogUtil.logException(TAG + " payment_intent.canceled: cannot deserialize PI");
                return;
            }

            PaymentIntent pi = (PaymentIntent) eventData;
            String reason = pi.getCancellationReason() != null
                    ? "Payment canceled: " + pi.getCancellationReason()
                    : "Payment was canceled";

            LogUtil.trace(TAG, "payment_intent.canceled -> PI=" + pi.getId() + ", reason=" + reason);

            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
            int rows = paymentBean.updatePaymentErrorByPaymentIntent(pi.getId(), reason, "CANCELLED");
            if (rows == 0) {
                UUID requestId = extractRequestIdFromMetadata(pi.getMetadata());
                if (requestId != null) {
                    rows = paymentBean.updatePaymentWithError(requestId, reason, "CANCELLED");
                    LogUtil.trace(TAG, "Updated to CANCELLED via requestId metadata -> requestId="
                            + requestId + ", rows=" + rows);
                }
            } else {
                LogUtil.trace(TAG, "Updated to CANCELLED -> PI=" + pi.getId() + ", rows=" + rows);
            }

        } catch (Exception e) {
            LogUtil.logException(TAG + " Error in payment_intent.canceled", e);
        }
    }

    // =========================================================================
    // Notification helpers
    // =========================================================================

    private String buildSessionNotificationDetails(Event event) {
        try {
            StripeObject eventData = getEventDataObject(event);
            if (!(eventData instanceof Session)) {
                return null;
            }
            Session session = (Session) eventData;
            StringBuilder details = new StringBuilder();
            details.append("sessionId=").append(session.getId());
            if (session.getPaymentIntent() != null) {
                details.append(", paymentIntent=").append(session.getPaymentIntent());
            }
            UUID requestId = extractRequestIdFromMetadata(session.getMetadata());
            if (requestId != null) {
                details.append(", requestId=").append(requestId);
            }
            if (session.getCustomerEmail() != null) {
                details.append(", customerEmail=").append(session.getCustomerEmail());
            }
            if (session.getPaymentStatus() != null) {
                details.append(", paymentStatus=").append(session.getPaymentStatus());
            }
            return details.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String buildPaymentIntentNotificationDetails(Event event, boolean includeFailure) {
        try {
            StripeObject eventData = getEventDataObject(event);
            if (!(eventData instanceof PaymentIntent)) {
                return null;
            }
            PaymentIntent pi = (PaymentIntent) eventData;
            StringBuilder details = new StringBuilder();
            details.append("paymentIntent=").append(pi.getId());
            UUID requestId = extractRequestIdFromMetadata(pi.getMetadata());
            if (requestId != null) {
                details.append(", requestId=").append(requestId);
            }
            String customerEmail = resolveCustomerEmail(pi);
            if (customerEmail != null) {
                details.append(", customerEmail=").append(customerEmail);
            }
            if (includeFailure && pi.getLastPaymentError() != null) {
                if (pi.getLastPaymentError().getCode() != null) {
                    details.append(", failureCode=").append(pi.getLastPaymentError().getCode());
                }
                if (pi.getLastPaymentError().getDeclineCode() != null) {
                    details.append(", declineCode=").append(pi.getLastPaymentError().getDeclineCode());
                }
                if (pi.getLastPaymentError().getMessage() != null) {
                    details.append(", message=").append(pi.getLastPaymentError().getMessage());
                }
            }
            return details.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private UUID extractRequestIdFromMetadata(java.util.Map<String, String> metadata) {
        if (metadata == null || !metadata.containsKey("requestId")) {
            return null;
        }
        try {
            return UUID.fromString(metadata.get("requestId"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String resolveCustomerEmail(PaymentIntent pi) {
        if (pi == null) {
            return null;
        }
        if (pi.getReceiptEmail() != null && !pi.getReceiptEmail().trim().isEmpty()) {
            return pi.getReceiptEmail().trim();
        }
        if (pi.getMetadata() != null) {
            String fromMeta = pi.getMetadata().get("customerEmail");
            if (fromMeta != null && !fromMeta.trim().isEmpty()) {
                return fromMeta.trim();
            }
        }
        // Fallback: email may only exist on the Checkout Session (typed on Stripe page)
        try {
            SessionCollection sessions = Session.list(
                    SessionListParams.builder()
                            .setPaymentIntent(pi.getId())
                            .setLimit(1L)
                            .build());
            if (sessions != null && sessions.getData() != null && !sessions.getData().isEmpty()) {
                return resolveCustomerEmailFromSession(sessions.getData().get(0));
            }
        } catch (Exception e) {
            LogUtil.log(TAG, "Could not resolve customer email from Checkout session for PI="
                    + pi.getId() + ": " + e.getMessage());
        }
        return null;
    }

    private String resolveCustomerEmailFromSession(Session session) {
        if (session == null) {
            return null;
        }
        if (session.getCustomerEmail() != null && !session.getCustomerEmail().trim().isEmpty()) {
            return session.getCustomerEmail().trim();
        }
        if (session.getCustomerDetails() != null
                && session.getCustomerDetails().getEmail() != null
                && !session.getCustomerDetails().getEmail().trim().isEmpty()) {
            return session.getCustomerDetails().getEmail().trim();
        }
        if (session.getMetadata() != null) {
            String fromSessionMeta = session.getMetadata().get("customerEmail");
            if (fromSessionMeta != null && !fromSessionMeta.trim().isEmpty()) {
                return fromSessionMeta.trim();
            }
        }
        return null;
    }
}
