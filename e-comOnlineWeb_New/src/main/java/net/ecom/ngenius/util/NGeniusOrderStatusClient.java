package net.ecom.ngenius.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Fallback client: fetches live N-Genius order status via the REST API.
 *
 * Used by {@link net.ecom.ngenius.NGeniusSuccessAction} when the browser
 * redirect arrives before (or instead of) the webhook — e.g. network delays,
 * server restarts, or a missed webhook header validation.
 *
 * Flow:
 *   1. POST /identity/auth/access-token  → Bearer token  (same as order creation)
 *   2. GET  /transactions/outlets/{outletId}/orders/{orderRef}  → order JSON
 *   3. Parse top-level {@code _embedded.payment[0]} for state, paymentRef, authCode, RRN
 *
 * Returns an {@link NGeniusOrderStatus} POJO, or {@code null} on any failure.
 */
public class NGeniusOrderStatusClient {

    private static final String TAG = "[NGeniusOrderStatusClient]";

    private static final String SANDBOX_BASE_URL = "https://api-gateway.sandbox.ngenius-payments.com";
    private static final String TOKEN_PATH        = "/identity/auth/access-token";
    private static final String ORDER_PATH        = "/transactions/outlets/%s/orders/%s";

    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS    = 20_000;

    private NGeniusOrderStatusClient() {
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Retrieves live order status from N-Genius.
     *
     * @param orderRef   The N-Genius order reference (stored in {@code stripeSessionId})
     * @param servletCtx Servlet context — used to read API key, outlet ID, base URL
     * @return Parsed status, or {@code null} if any step fails
     */
    public static NGeniusOrderStatus fetch(String orderRef, ServletContext servletCtx) {
        if (orderRef == null || orderRef.trim().isEmpty()) {
            LogUtil.logException(TAG + " orderRef is null/empty — cannot fetch status");
            return null;
        }

        try {
            String apiKey    = (String) servletCtx.getAttribute(GeneralConf.Ngenius_API_Key);
            String outletRef = (String) servletCtx.getAttribute(GeneralConf.Ngenius_Outlet_Id);
            String baseUrl   = resolveBaseUrl(servletCtx);

            if (apiKey == null || apiKey.trim().isEmpty()) {
                LogUtil.logException(TAG + " N-Genius API key not configured");
                return null;
            }
            if (outletRef == null || outletRef.trim().isEmpty()) {
                LogUtil.logException(TAG + " N-Genius Outlet ID not configured");
                return null;
            }

            String token = fetchAccessToken(apiKey.trim(), baseUrl);
            if (token == null || token.trim().isEmpty()) {
                LogUtil.logException(TAG + " Failed to obtain access token for fallback check");
                return null;
            }

            return fetchOrderStatus(token, baseUrl, outletRef.trim(), orderRef.trim());

        } catch (Exception e) {
            LogUtil.logException(TAG + " fetch() unexpected error for orderRef=" + orderRef, e);
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Step 1: fetch access token
    // -------------------------------------------------------------------------

    private static String fetchAccessToken(String apiKey, String baseUrl) {
        try {
            URL url = new URL(baseUrl + TOKEN_PATH);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",  "application/vnd.ni-identity.v1+json");
            conn.setRequestProperty("Accept",         "application/vnd.ni-identity.v1+json");
            conn.setRequestProperty("Authorization",  "Basic " + apiKey);

            byte[] body = "{\"realmName\":\"ni\"}".getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body);
            }

            int httpStatus = conn.getResponseCode();
            String responseBody = readResponse(conn);
            LogUtil.trace(TAG, "Token response HTTP=" + httpStatus);

            if (httpStatus != 200) {
                LogUtil.logException(TAG + " Token fetch failed, HTTP=" + httpStatus);
                return null;
            }

            JsonObject json = new JsonParser().parse(responseBody).getAsJsonObject();
            return json.has("access_token") ? json.get("access_token").getAsString() : null;

        } catch (Exception e) {
            LogUtil.logException(TAG + " fetchAccessToken error", e);
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Step 2: GET order status
    // -------------------------------------------------------------------------

    private static NGeniusOrderStatus fetchOrderStatus(String token,
                                                        String baseUrl,
                                                        String outletRef,
                                                        String orderRef) {
        try {
            String path = String.format(ORDER_PATH, outletRef, orderRef);
            URL url = new URL(baseUrl + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setRequestProperty("Accept",        "application/vnd.ni-payment.v2+json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int httpStatus = conn.getResponseCode();
            String responseBody = readResponse(conn);
            LogUtil.trace(TAG, "Order status HTTP=" + httpStatus + " for orderRef=" + orderRef);

            if (httpStatus != 200) {
                LogUtil.logException(TAG + " Order status fetch failed, HTTP=" + httpStatus
                        + " for orderRef=" + orderRef);
                return null;
            }

            return parseOrderStatus(responseBody, orderRef);

        } catch (Exception e) {
            LogUtil.logException(TAG + " fetchOrderStatus error for orderRef=" + orderRef, e);
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Parse response
    // -------------------------------------------------------------------------

    private static NGeniusOrderStatus parseOrderStatus(String responseBody, String orderRef) {
        try {
            JsonObject root = new JsonParser().parse(responseBody).getAsJsonObject();

            // Top-level order state (e.g. PURCHASED, PARTIALLY_CAPTURED, FAILED, CANCELLED)
            String orderState = safeString(root, "status");

            // Payment details inside _embedded.payment[0]
            String paymentRef   = null;
            String paymentState = null;
            String authCode     = null;
            String rrn          = null;

            if (root.has("_embedded")) {
                JsonObject embedded = root.getAsJsonObject("_embedded");
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

            LogUtil.log(TAG, "Parsed order status -> orderRef=" + orderRef
                    + ", orderState=" + orderState
                    + ", paymentState=" + paymentState
                    + ", paymentRef=" + paymentRef
                    + ", authCode=" + authCode
                    + ", rrn=" + rrn);

            return new NGeniusOrderStatus(orderRef, orderState, paymentRef, paymentState, authCode, rrn);

        } catch (Exception e) {
            LogUtil.logException(TAG + " parseOrderStatus error", e);
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static String resolveBaseUrl(ServletContext ctx) {
        Object ctxUrl = ctx.getAttribute(GeneralConf.Ngenius_Base_URL);
        if (ctxUrl instanceof String && !((String) ctxUrl).trim().isEmpty()) {
            return ((String) ctxUrl).trim();
        }
        return SANDBOX_BASE_URL;
    }

    private static String readResponse(HttpURLConnection conn) {
        try {
            BufferedReader reader;
            if (conn.getResponseCode() >= 400) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            LogUtil.logException(TAG + " readResponse error", e);
            return "";
        }
    }

    private static String safeString(JsonObject obj, String key) {
        if (obj == null || !obj.has(key) || obj.get(key).isJsonNull()) return null;
        return obj.get(key).getAsString();
    }

    // -------------------------------------------------------------------------
    // Result POJO
    // -------------------------------------------------------------------------

    /**
     * Holds the live N-Genius order status returned by the order-status API.
     *
     * Mapped to existing DB columns in {@code Ecom_OnlinePayment}:
     *   paymentRef   → stripePaymentIntentId
     *   authCode     → stripeChargeId
     *   rrn          → stripeTransactionId
     */
    public static class NGeniusOrderStatus {

        private final String orderRef;
        private final String orderState;      // e.g. PURCHASED, FAILED, CANCELLED
        private final String paymentRef;      // N-Genius payment reference
        private final String paymentState;    // state inside _embedded.payment[0]
        private final String authCode;        // authorizationCode
        private final String rrn;             // Retrieval Reference Number

        public NGeniusOrderStatus(String orderRef, String orderState,
                                  String paymentRef, String paymentState,
                                  String authCode, String rrn) {
            this.orderRef     = orderRef;
            this.orderState   = orderState;
            this.paymentRef   = paymentRef;
            this.paymentState = paymentState;
            this.authCode     = authCode;
            this.rrn          = rrn;
        }

        /** Returns true when N-Genius confirms payment was captured/purchased. */
        public boolean isCompleted() {
            return "PURCHASED".equalsIgnoreCase(orderState)
                    || "CAPTURED".equalsIgnoreCase(orderState)
                    || "PURCHASED".equalsIgnoreCase(paymentState)
                    || "CAPTURED".equalsIgnoreCase(paymentState);
        }

        /** Returns true when N-Genius confirms payment is authorised (not yet captured). */
        public boolean isAuthorised() {
            return "AUTHORISED".equalsIgnoreCase(orderState)
                    || "AUTHORISED".equalsIgnoreCase(paymentState);
        }

        /**
         * Returns true when N-Genius confirms payment failed.
         * Checks both orderState and paymentState because N-Genius sometimes returns
         * orderState=null with paymentState=FAILED when the bank declines the card
         * before the top-level order state is written back.
         */
        public boolean isFailed() {
            return matchesFailure(orderState) || matchesFailure(paymentState);
        }

        private static boolean matchesFailure(String s) {
            if (s == null) return false;
            String u = s.toUpperCase();
            return u.equals("FAILED") || u.contains("DECLINED");
        }

        /**
         * Returns true when N-Genius confirms payment was reversed/cancelled.
         * Checks both fields for the same reason as isFailed().
         */
        public boolean isCancelled() {
            return matchesCancellation(orderState) || matchesCancellation(paymentState);
        }

        private static boolean matchesCancellation(String s) {
            if (s == null) return false;
            String u = s.toUpperCase();
            return u.equals("CANCELLED") || u.contains("REVERSED");
        }

        /**
         * Returns true when the N-Genius order was created but the customer never
         * attempted payment (i.e. they closed/abandoned the hosted payment page).
         * N-Genius keeps the order in SESSION_CREATED state in this case.
         * These payments should be marked CANCELLED — they will never progress.
         */
        public boolean isAbandoned() {
            return "SESSION_CREATED".equalsIgnoreCase(orderState)
                    || "SESSION_CREATED".equalsIgnoreCase(paymentState);
        }

        /**
         * Returns true when the customer submitted their card details and
         * N-Genius is still processing the transaction (INITIATED / AWAIT_3DS / STARTED etc.).
         * The webhook will arrive shortly — the page should auto-refresh rather than
         * taking any irreversible DB action.
         * Also checks paymentState for cases where orderState is null.
         */
        public boolean isProcessing() {
            return matchesProcessing(orderState) || matchesProcessing(paymentState);
        }

        private static boolean matchesProcessing(String s) {
            if (s == null) return false;
            String u = s.toUpperCase();
            return u.equals("INITIATED") || u.startsWith("AWAIT") || u.equals("PROCESSING") || u.equals("STARTED");
        }

        /**
         * State label for log/error messages.
         * Prefers orderState; falls back to paymentState when orderState is absent
         * (which N-Genius sometimes omits on bank declines).
         */
        public String getEffectiveState() {
            return orderState != null ? orderState : (paymentState != null ? paymentState : "UNKNOWN");
        }

        public String getOrderRef()     { return orderRef; }
        public String getOrderState()   { return orderState; }
        public String getPaymentRef()   { return paymentRef; }
        public String getPaymentState() { return paymentState; }
        public String getAuthCode()     { return authCode; }
        public String getRrn()          { return rrn; }
    }
}
