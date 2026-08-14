package net.ecom.ngenius.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;
import net.ecom.stripe.util.PaymentHelper;
import net.ecom.stripe.util.PaymentRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Handles the two-step N-Genius API interaction:
 * 1. Fetch a short-lived Bearer access token.
 * 2. Create a Hosted Payment Page order and return the pay-page URL.
 *
 * Sandbox URLs are used by default. Switch to live by changing the base URL constant
 * or by adding a {@code Ngenius_Base_URL} entry to {@code globalconfigproduct}.
 */
public class NGeniusOrderCreator {

    private static final String TAG = "[NGeniusOrderCreator]";

    private static final String SANDBOX_BASE_URL = "https://api-gateway.sandbox.ngenius-payments.com";
    private static final String TOKEN_PATH        = "/identity/auth/access-token";
    private static final String ORDERS_PATH       = "/transactions/outlets/%s/orders";

    private static final int CONNECT_TIMEOUT_MS = 15_000;
    private static final int READ_TIMEOUT_MS    = 30_000;

    private NGeniusOrderCreator() {
    }

    /**
     * Full order-creation flow.
     *
     * @return {@link NGeniusOrderResult} with the N-Genius order reference and hosted pay-page URL,
     *         or {@code null} if any step fails.
     */
    public static NGeniusOrderResult create(UUID requestId,
                                            PaymentRequest paymentRequest,
                                            String apiKey,
                                            String outletRef,
                                            String currency,
                                            String loginName,
                                            HttpServletRequest request) {
        try {
            String baseUrl = resolveBaseUrl(request);

            // Step 1 — get access token
            String token = fetchAccessToken(apiKey, baseUrl);
            if (token == null || token.trim().isEmpty()) {
                LogUtil.logException(TAG + " Failed to obtain access token");
                return null;
            }
            LogUtil.trace(TAG, "Access token obtained");

            // Step 2 — create order
            String ordersUrl = baseUrl + String.format(ORDERS_PATH, outletRef);
            String successUrl = PaymentHelper.getBaseUrl(request) + "/ngeniusSuccess.do?rid=" + requestId;
            String cancelUrl  = PaymentHelper.getBaseUrl(request) + "/ngeniusCancel.do?rid=" + requestId;

            return createOrder(token, ordersUrl, requestId, paymentRequest, currency, loginName, successUrl, cancelUrl);

        } catch (Exception e) {
            LogUtil.logException(TAG + " create() failed", e);
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Step 1: fetch token
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

            int status = conn.getResponseCode();
            String responseBody = readResponse(conn);
            LogUtil.trace(TAG, "Token response HTTP=" + status);

            if (status != 200) {
                LogUtil.logException(TAG + " Token fetch failed, HTTP=" + status + ", body=" + responseBody);
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
    // Step 2: create order
    // -------------------------------------------------------------------------

    private static NGeniusOrderResult createOrder(String token,
                                                   String ordersUrl,
                                                   UUID requestId,
                                                   PaymentRequest paymentRequest,
                                                   String currency,
                                                   String loginName,
                                                   String successUrl,
                                                   String cancelUrl) {
        try {
            long amountMinor = PaymentHelper.convertToCents(paymentRequest.getTotalAmount());

            JsonObject amount = new JsonObject();
            amount.addProperty("currencyCode", currency != null && !currency.isEmpty() ? currency : "USD");
            amount.addProperty("value", amountMinor);

            JsonObject merchantAttr = new JsonObject();
            merchantAttr.addProperty("redirectUrl", successUrl);
            merchantAttr.addProperty("cancelUrl",   cancelUrl);

            JsonObject merchantDefined = new JsonObject();
            merchantDefined.addProperty("requestId",   requestId.toString());
            merchantDefined.addProperty("paymentType", paymentRequest.getPaymentType());

            JsonObject orderBody = new JsonObject();
            orderBody.addProperty("action", "PURCHASE");
            orderBody.add("amount", amount);
            orderBody.add("merchantAttributes",  merchantAttr);
            orderBody.add("merchantDefinedData", merchantDefined);
            orderBody.addProperty("emailAddress", loginName != null ? loginName : "");

            String bodyJson = new Gson().toJson(orderBody);
            LogUtil.trace(TAG, "Create order body: " + bodyJson);

            URL url = new URL(ordersUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",  "application/vnd.ni-payment.v2+json");
            conn.setRequestProperty("Accept",        "application/vnd.ni-payment.v2+json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(bodyJson.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            String responseBody = readResponse(conn);
            LogUtil.trace(TAG, "Order create HTTP=" + status);

            if (status != 200 && status != 201) {
                LogUtil.logException(TAG + " Order create failed, HTTP=" + status + ", body=" + responseBody);
                return null;
            }

            JsonObject json = new JsonParser().parse(responseBody).getAsJsonObject();

            String orderRef = json.has("reference") ? json.get("reference").getAsString() : null;
            String payPageUrl = null;

            if (json.has("_links")) {
                JsonObject links = json.getAsJsonObject("_links");
                if (links.has("payment")) {
                    payPageUrl = links.getAsJsonObject("payment").get("href").getAsString();
                }
            }

            LogUtil.log(TAG, "Order created -> ref=" + orderRef + ", payPage=" + payPageUrl);

            if (orderRef == null || payPageUrl == null) {
                LogUtil.logException(TAG + " Order response missing reference or payment link");
                return null;
            }

            return new NGeniusOrderResult(orderRef, payPageUrl);

        } catch (Exception e) {
            LogUtil.logException(TAG + " createOrder error", e);
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static String readResponse(HttpURLConnection conn) {
        try {
            BufferedReader reader;
            if (conn.getResponseCode() >= 400) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
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

    /**
     * Reads Ngenius_Base_URL from servlet context if configured (for live switch),
     * otherwise falls back to sandbox base URL.
     * Switch to live: insert into globalconfigproduct (pathname,pathvalue,status)
     *   values ('Ngenius_Base_URL','https://api-gateway.ngenius-payments.com','C')
     */
    private static String resolveBaseUrl(HttpServletRequest request) {
        Object ctxUrl = request.getServletContext().getAttribute(GeneralConf.Ngenius_Base_URL);
        if (ctxUrl instanceof String && !((String) ctxUrl).trim().isEmpty()) {
            return ((String) ctxUrl).trim();
        }
        return SANDBOX_BASE_URL;
    }

    // -------------------------------------------------------------------------
    // Inner result holder
    // -------------------------------------------------------------------------

    public static class NGeniusOrderResult {
        private final String orderRef;
        private final String payPageUrl;

        public NGeniusOrderResult(String orderRef, String payPageUrl) {
            this.orderRef   = orderRef;
            this.payPageUrl = payPageUrl;
        }

        public String getOrderRef()   { return orderRef; }
        public String getPayPageUrl() { return payPageUrl; }
    }
}
