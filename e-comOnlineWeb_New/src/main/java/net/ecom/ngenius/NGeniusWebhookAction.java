package net.ecom.ngenius;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;
import net.ecom.common.PaymentEventNotificationMail;
import net.ecom.lookup.EJBLookUp;
import net.ecom.ngenius.util.NGeniusWebhookDecryptor;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles incoming N-Genius webhook events.
 *
 * N-Genius webhook characteristics (unlike Stripe):
 * - No HMAC signature — uses optional shared-header validation instead.
 * - Optional AES-256-CBC body encryption (portal "Webhook Encryption").
 * - No automatic retries — always return HTTP 200 to avoid losing the ack path.
 * - Single event per POST payload.
 *
 * Relevant event names handled:
 *   PURCHASED / CAPTURED   → COMPLETED + insertPaymentGateway
 *   AUTHORISED             → update status to AUTHORISED (auth-only flow)
 *   DECLINED / *_FAILED    → FAILED
 *   *_REVERSED / CANCELLED → CANCELLED
 *
 * Column mapping (reusing existing Stripe columns in Ecom_OnlinePayment):
 *   stripeSessionId       → N-Genius order reference   (set at order creation)
 *   stripePaymentIntentId → N-Genius payment reference  (set here from webhook)
 *   stripeChargeId        → authorization code
 *   stripeTransactionId   → RRN (Retrieval Reference Number)
 *
 * Docs:
 *   https://docs.ngenius-payments.com/reference/consuming-web-hooks
 *   https://docs.ngenius-payments.com/reference/webhook-encryption-decryption-guide
 */
public class NGeniusWebhookAction extends Action {

    private static final String TAG = "[NGeniusWebhook]";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "Webhook received");
        PaymentEventNotificationMail.notifyReceived(
                request.getServletContext(),
                "NGenius webhook received",
                "NGenius webhook received");

        // Always return 200 — N-Genius does NOT retry, so we must not signal errors
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            // Optional shared-header validation
            if (!isValidWebhookHeader(request)) {
                LogUtil.logException(TAG + " Webhook header validation failed — ignoring event");
                out.write("OK");
                out.flush();
                return null;
            }

            String payload;
            try (BufferedReader reader = request.getReader()) {
                payload = reader.lines().collect(Collectors.joining("\n"));
            }

            if (payload == null || payload.trim().isEmpty()) {
                LogUtil.trace(TAG, "Empty webhook payload");
                out.write("OK");
                out.flush();
                return null;
            }

            LogUtil.trace(TAG, "Webhook payload length=" + payload.length());

            String jsonPayload = resolveJsonPayload(payload, request);
            if (jsonPayload == null) {
                out.write("OK");
                out.flush();
                return null;
            }

            // Acknowledge quickly, then process (doc: respond within 15s)
            out.write("OK");
            out.flush();

            processEvent(jsonPayload, request);

        } catch (Exception e) {
            LogUtil.logException(TAG + " Unhandled error in webhook", e);
            try {
                out.write("OK");
                out.flush();
            } catch (Exception ignore) {
                // response may already be committed
            }
        }

        return null;
    }

    /**
     * Plain JSON is used as-is. Encrypted bodies (Base64 AES-256-CBC) are decrypted
     * using {@link GeneralConf#Ngenius_Webhook_Encryption_Secret}, falling back to
     * {@link GeneralConf#Ngenius_Webhook_Header_Value} (portal X-Webhook-Secret).
     */
    private String resolveJsonPayload(String payload, HttpServletRequest request) {
        if (NGeniusWebhookDecryptor.looksLikePlainJson(payload)) {
            return payload.trim();
        }

        String secret = resolveEncryptionSecret(request);
        if (secret == null || secret.trim().isEmpty()) {
            LogUtil.logException(TAG + " Payload is not JSON and no encryption secret configured — ignoring");
            return null;
        }

        try {
            String decrypted = NGeniusWebhookDecryptor.decrypt(payload, secret);
            if (!NGeniusWebhookDecryptor.looksLikePlainJson(decrypted)) {
                LogUtil.logException(TAG + " Decrypted payload is not valid JSON");
                return null;
            }
            return decrypted.trim();
        } catch (Exception e) {
            LogUtil.logException(TAG + " Failed to decrypt webhook payload", e);
            return null;
        }
    }

    private String resolveEncryptionSecret(HttpServletRequest request) {
        String dedicated = (String) request.getServletContext()
                .getAttribute(GeneralConf.Ngenius_Webhook_Encryption_Secret);
        if (dedicated != null && !dedicated.trim().isEmpty()) {
            return dedicated.trim();
        }
        // When portal encryption is enabled, Header Value is the 32-char AES secret
        String headerValue = (String) request.getServletContext()
                .getAttribute(GeneralConf.Ngenius_Webhook_Header_Value);
        if (headerValue != null && headerValue.trim().length() == 32) {
            return headerValue.trim();
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Header validation
    // -------------------------------------------------------------------------

    private boolean isValidWebhookHeader(HttpServletRequest request) {
        String expectedKey   = (String) request.getServletContext().getAttribute(GeneralConf.Ngenius_Webhook_Header_Key);
        String expectedValue = (String) request.getServletContext().getAttribute(GeneralConf.Ngenius_Webhook_Header_Value);

        if (expectedKey == null || expectedKey.trim().isEmpty()
                || expectedValue == null || expectedValue.trim().isEmpty()) {
            LogUtil.trace(TAG, "No webhook header configured — skipping header validation");
            return true;
        }

        String headerValue = request.getHeader(expectedKey.trim());
        if (expectedValue.trim().equals(headerValue)) {
            return true;
        }
        LogUtil.trace(TAG, "Webhook header mismatch for key=" + expectedKey);
        return false;
    }

    // -------------------------------------------------------------------------
    // Event processing
    // -------------------------------------------------------------------------

    private void processEvent(String payload, HttpServletRequest request) {
        try {
            JsonObject root = new JsonParser().parse(payload).getAsJsonObject();

            String eventName = safeString(root, "eventName");
            String eventId   = safeString(root, "eventId");
            String outletId  = safeString(root, "outletId");
            LogUtil.trace(TAG, "Event: eventName=" + eventName
                    + ", eventId=" + eventId
                    + ", outletId=" + outletId);

            if (eventName == null || eventName.trim().isEmpty()) {
                LogUtil.logException(TAG + " Missing eventName in webhook payload");
                return;
            }

            // Skip informational order-lifecycle events
            if ("ORDER".equalsIgnoreCase(eventName)) {
                LogUtil.trace(TAG, "Ignoring ORDER event");
                return;
            }

            JsonObject order = root.has("order") ? root.getAsJsonObject("order") : null;
            if (order == null) {
                LogUtil.logException(TAG + " No 'order' object in webhook payload");
                return;
            }

            String orderRef = safeString(order, "reference");
            String orderEmail = safeString(order, "emailAddress");
            LogUtil.log(TAG, "Order reference=" + orderRef + ", eventName=" + eventName);

            // Resolve our requestId from merchantDefinedData embedded in the order payload
            String requestIdStr = null;
            if (order.has("merchantDefinedData")) {
                JsonObject mdd = order.getAsJsonObject("merchantDefinedData");
                requestIdStr = safeString(mdd, "requestId");
            }

            if (requestIdStr == null || requestIdStr.trim().isEmpty()) {
                LogUtil.logException(TAG + " No requestId in merchantDefinedData for orderRef=" + orderRef);
                return;
            }

            UUID requestId;
            try {
                requestId = UUID.fromString(requestIdStr);
            } catch (IllegalArgumentException e) {
                LogUtil.logException(TAG + " Invalid requestId format: " + requestIdStr);
                return;
            }

            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
            LogUtil.log(TAG, "Using requestId=" + requestId + " from merchantDefinedData, orderRef=" + orderRef);

            // Extract payment details from embedded payment array
            String paymentRef   = null;
            String authCode     = null;
            String rrn          = null;
            String paymentState = null;

            if (order.has("_embedded")) {
                JsonObject embedded = order.getAsJsonObject("_embedded");
                if (embedded.has("payment")) {
                    JsonArray payments = embedded.getAsJsonArray("payment");
                    if (payments.size() > 0) {
                        JsonObject payment = payments.get(0).getAsJsonObject();
                        paymentRef   = safeString(payment, "reference");
                        paymentState = safeString(payment, "state");
                        if (payment.has("authResponse")) {
                            JsonObject auth = payment.getAsJsonObject("authResponse");
                            authCode = safeString(auth, "authorizationCode");
                            rrn      = safeString(auth, "rrn");
                        }
                    }
                }
            }

            LogUtil.log(TAG, "Payment detail -> ref=" + paymentRef
                    + ", state=" + paymentState
                    + ", authCode=" + authCode
                    + ", rrn=" + rrn);

            String notificationStatus = mapNGeniusEventStatus(eventName);
            switch (eventName.toUpperCase()) {
                case "PURCHASED":
                case "CAPTURED":
                    int completedRows = handleCompleted(paymentBean, requestId, paymentRef, authCode, rrn, orderRef);
                    // Send once when payment first becomes COMPLETED (second event typically updates 0 rows)
                    if (completedRows > 0) {
                        PaymentEventNotificationMail.notifyCustomerPaymentSuccess(
                                request.getServletContext(),
                                orderEmail,
                                "NGenius",
                                paymentRef != null ? paymentRef : orderRef,
                                null);
                    }
                    break;

                case "AUTHORISED":
                    paymentBean.updatePaymentWithStripeCompletion(
                            requestId, paymentRef, authCode, rrn, null, "AUTHORISED");
                    LogUtil.trace(TAG, "Updated to AUTHORISED -> requestId=" + requestId);
                    break;

                case "DECLINED":
                case "PURCHASE_DECLINED":
                case "PURCHASE_FAILED":
                case "AUTHORISATION_FAILED":
                    paymentBean.updatePaymentWithError(requestId,
                            "Payment declined: " + eventName, "FAILED");
                    LogUtil.trace(TAG, "Updated to FAILED -> requestId=" + requestId);
                    PaymentEventNotificationMail.notifyCustomerPaymentFailed(
                            request.getServletContext(),
                            orderEmail,
                            "NGenius",
                            paymentRef != null ? paymentRef : orderRef,
                            "Payment declined: " + eventName);
                    break;

                case "FULL_AUTH_REVERSED":
                case "PURCHASE_REVERSED":
                case "CANCELLED":
                    paymentBean.updatePaymentWithError(requestId,
                            "Payment reversed/cancelled: " + eventName, "CANCELLED");
                    LogUtil.trace(TAG, "Updated to CANCELLED -> requestId=" + requestId);
                    break;

                default:
                    LogUtil.log(TAG, "Unhandled eventName: " + eventName);
                    break;
            }

            PaymentEventNotificationMail.notifyIfConfigured(
                    request.getServletContext(),
                    "NGenius",
                    eventName,
                    notificationStatus,
                    orderRef,
                    "requestId=" + requestId
                            + (eventId != null ? ", eventId=" + eventId : "")
                            + (paymentState != null ? ", paymentState=" + paymentState : "")
                            + (paymentRef != null ? ", paymentRef=" + paymentRef : ""));

        } catch (Exception e) {
            LogUtil.logException(TAG + " processEvent error", e);
        }
    }

    private String mapNGeniusEventStatus(String eventName) {
        if (eventName == null) {
            return "UNKNOWN";
        }
        switch (eventName.toUpperCase()) {
            case "PURCHASED":
            case "CAPTURED":
                return "COMPLETED";
            case "AUTHORISED":
                return "AUTHORISED";
            case "DECLINED":
            case "PURCHASE_DECLINED":
            case "PURCHASE_FAILED":
            case "AUTHORISATION_FAILED":
                return "FAILED";
            case "FULL_AUTH_REVERSED":
            case "PURCHASE_REVERSED":
            case "CANCELLED":
                return "CANCELLED";
            default:
                return eventName;
        }
    }

    private int handleCompleted(EcomOnlinePaymentBeanRemote paymentBean,
                                 UUID requestId,
                                 String paymentRef,
                                 String authCode,
                                 String rrn,
                                 String orderRef) {
        try {
            // updatePaymentWithStripeCompletion(requestId, PI, chargeId, txnId, receiptUrl, status)
            // Mapped:  PI=paymentRef, chargeId=authCode, txnId=rrn, receiptUrl=null
            int rows = paymentBean.updatePaymentWithStripeCompletion(
                    requestId, paymentRef, authCode, rrn, null, "COMPLETED");
            LogUtil.trace(TAG, "Updated to COMPLETED -> requestId=" + requestId + ", rows=" + rows);

            if (rows > 0) {
                try {
                    boolean gatewayResult = paymentBean.insertPaymentGateway(requestId);
                    LogUtil.log(TAG, "PaymentGateway insert -> requestId=" + requestId + ", result=" + gatewayResult);
                } catch (Exception ge) {
                    LogUtil.logException(TAG + " PaymentGateway insert failed for requestId=" + requestId, ge);
                }
            }
            return rows;
        } catch (Exception e) {
            LogUtil.logException(TAG + " handleCompleted error", e);
            return 0;
        }
    }

    private String safeString(JsonObject obj, String key) {
        if (obj == null || !obj.has(key) || obj.get(key).isJsonNull()) return null;
        return obj.get(key).getAsString();
    }
}
