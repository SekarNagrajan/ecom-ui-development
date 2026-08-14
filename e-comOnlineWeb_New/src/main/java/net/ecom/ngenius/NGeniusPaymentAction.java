package net.ecom.ngenius;

import com.google.gson.Gson;
import net.ecom.common.LogUtil;
import net.ecom.ngenius.util.NGeniusPaymentResponse;
import net.ecom.ngenius.util.NGeniusUtil;
import net.ecom.stripe.util.PaymentHelper;
import net.ecom.stripe.util.PaymentRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * Entry point for N-Genius Hosted Payment Page.
 * Mirrors {@code SecureStripePaymentAction} — same CSRF, session, and JSON contract.
 *
 * Request:  POST /secureNGeniusPayment.do  (Content-Type: application/json)
 * Response: JSON  {"success":true,"ngeniusOrderUrl":"https://paypage.sandbox...","requestId":"..."}
 */
public class NGeniusPaymentAction extends Action {

    private static final String TAG = "[NGeniusPaymentAction]";
    private static final int MAX_REQUEST_BODY_SIZE = 10 * 1024;
    private static final Gson GSON = new Gson();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "execute -> " + request.getMethod());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        try (PrintWriter out = response.getWriter()) {

            if (!"POST".equals(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                out.write(toJson(NGeniusPaymentResponse.error("Invalid request method")));
                return null;
            }

            String contentType = request.getContentType();
            if (contentType == null || !contentType.contains("application/json")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(toJson(NGeniusPaymentResponse.error("Invalid content type")));
                return null;
            }

            if (!isValidCSRFToken(request)) {
                LogUtil.trace(TAG, "Rejected: CSRF validation failed");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.write(toJson(NGeniusPaymentResponse.error("Invalid CSRF token")));
                return null;
            }

            String customerCode = (String) request.getSession().getAttribute("SESSION_CUSTCODE");
            String loginName    = (String) request.getSession().getAttribute("SESSION_LOGINNAME");
            if (customerCode == null || loginName == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write(toJson(NGeniusPaymentResponse.error("Session expired. Please login again.")));
                return null;
            }

            String jsonBody = readJsonRequest(request);
            if (jsonBody == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(toJson(NGeniusPaymentResponse.error("Empty request")));
                return null;
            }

            PaymentRequest paymentRequest;
            try {
                paymentRequest = PaymentHelper.parsePaymentRequest(jsonBody);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(toJson(NGeniusPaymentResponse.error("Invalid request format")));
                return null;
            }

            LogUtil.log(TAG, "Parsed -> type=" + paymentRequest.getPaymentType()
                    + ", amount=" + paymentRequest.getTotalAmount()
                    + ", customer=" + customerCode);

            NGeniusPaymentResponse paymentResponse = NGeniusUtil.createPaymentOrder(
                    paymentRequest, customerCode, loginName, request);

            if (paymentResponse.isSuccess() && paymentResponse.getRequestId() != null) {
                request.getSession().setAttribute("NGENIUS_PAYMENT_REQUEST_ID", paymentResponse.getRequestId());
            }

            response.setStatus(paymentResponse.isSuccess() ?
                    HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            out.write(toJson(paymentResponse));

            LogUtil.trace(TAG, "execute -> done, success=" + paymentResponse.isSuccess());

        } catch (Exception e) {
            LogUtil.logException(TAG, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.write(toJson(NGeniusPaymentResponse.error("Service temporarily unavailable")));
            }
        }

        return null;
    }

    private boolean isValidCSRFToken(HttpServletRequest request) {
        try {
            String requestToken = request.getHeader("X-NGENIUS-CSRF-Token");
            HttpSession session = request.getSession(false);
            if (session == null || requestToken == null) return false;

            String sessionToken = (String) session.getAttribute("NGENIUS_CSRF_TOKEN");
            session.removeAttribute("NGENIUS_CSRF_TOKEN");

            if (sessionToken != null && sessionToken.equals(requestToken)) {
                LogUtil.log(TAG, "CSRF token validated OK");
                return true;
            }
            LogUtil.logException(TAG + " CSRF token mismatch");
            return false;
        } catch (Exception e) {
            LogUtil.logException(TAG + " CSRF validation error", e);
            return false;
        }
    }

    private String readJsonRequest(HttpServletRequest request) {
        try {
            StringBuilder sb = new StringBuilder();
            int totalRead = 0;
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    totalRead += line.length();
                    if (totalRead > MAX_REQUEST_BODY_SIZE) return null;
                    sb.append(line);
                }
            }
            String json = sb.toString().trim();
            return json.isEmpty() ? null : json;
        } catch (Exception e) {
            LogUtil.logException(TAG + " readJsonRequest failed", e);
            return null;
        }
    }

    private String toJson(NGeniusPaymentResponse r) {
        return GSON.toJson(r);
    }
}
