package net.ecom.action;

import net.ecom.common.LogUtil;
import net.ecom.stripe.util.PaymentHelper;
import net.ecom.stripe.util.PaymentRequest;
import net.ecom.stripe.util.PaymentResponse;
import net.ecom.stripe.util.StripeUtil;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class SecureStripePaymentAction extends Action {

    private static final String TAG = "[SecureStripePaymentAction]";
    private static final int MAX_REQUEST_BODY_SIZE = 10 * 1024;

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
                LogUtil.trace(TAG, "Rejected: invalid method " + request.getMethod());
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                out.write(PaymentHelper.toJson(PaymentResponse.error("Invalid request method")));
                return null;
            }

            String contentType = request.getContentType();
            if (contentType == null || !contentType.contains("application/json")) {
                LogUtil.trace(TAG, "Rejected: invalid content-type");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(PaymentHelper.toJson(PaymentResponse.error("Invalid content type")));
                return null;
            }

            if (!isValidCSRFToken(request)) {
                LogUtil.trace(TAG, "Rejected: CSRF validation failed");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.write(PaymentHelper.toJson(PaymentResponse.error("Invalid CSRF token")));
                return null;
            }

            String customerCode = (String) request.getSession().getAttribute("SESSION_CUSTCODE");
            String loginName = (String) request.getSession().getAttribute("SESSION_LOGINNAME");
            if (customerCode == null || loginName == null) {
                LogUtil.trace(TAG, "Rejected: session expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write(PaymentHelper.toJson(PaymentResponse.error("Session expired. Please login again.")));
                return null;
            }

            String jsonRequest = readJsonRequest(request);
            if (jsonRequest == null) {
                LogUtil.trace(TAG, "Rejected: empty request body");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(PaymentHelper.toJson(PaymentResponse.error("Empty request")));
                return null;
            }

            LogUtil.log(TAG, "Request body length=" + jsonRequest.length());

            PaymentRequest paymentRequest;
            try {
                paymentRequest = PaymentHelper.parsePaymentRequest(jsonRequest);
            } catch (IllegalArgumentException e) {
                LogUtil.trace(TAG, "Rejected: invalid JSON format");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(PaymentHelper.toJson(PaymentResponse.error("Invalid request format")));
                return null;
            }

            LogUtil.log(TAG, "Parsed -> type=" + paymentRequest.getPaymentType()
                    + ", amount=" + paymentRequest.getTotalAmount()
                    + ", customer=" + customerCode + ", login=" + loginName);

            PaymentResponse paymentResponse = StripeUtil.createPaymentSession(
                paymentRequest, customerCode, loginName, request);

            if (paymentResponse.isSuccess() && paymentResponse.getRequestId() != null) {
                request.getSession().setAttribute("STRIPE_PAYMENT_REQUEST_ID", paymentResponse.getRequestId());
            }

            response.setStatus(paymentResponse.isSuccess() ?
                HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            out.write(PaymentHelper.toJson(paymentResponse));

            LogUtil.trace(TAG, "execute -> done, success=" + paymentResponse.isSuccess());

        } catch (Exception e) {
            LogUtil.logException(TAG, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.write(PaymentHelper.toJson(PaymentResponse.error("Service temporarily unavailable")));
            }
        }

        return null;
    }

    private boolean isValidCSRFToken(HttpServletRequest request) {
        try {
            String requestToken = request.getHeader("X-CSRF-Token");
            HttpSession session = request.getSession(false);

            if (session == null || requestToken == null) {
                return false;
            }

            String sessionToken = (String) session.getAttribute("STRIPE_CSRF_TOKEN");
            session.removeAttribute("STRIPE_CSRF_TOKEN");
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
            StringBuilder jsonBuilder = new StringBuilder();
            int totalRead = 0;
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    totalRead += line.length();
                    if (totalRead > MAX_REQUEST_BODY_SIZE) {
                        LogUtil.trace(TAG, "Request body exceeds " + MAX_REQUEST_BODY_SIZE + " bytes");
                        return null;
                    }
                    jsonBuilder.append(line);
                }
            }
            String json = jsonBuilder.toString().trim();
            return json.isEmpty() ? null : json;
        } catch (Exception e) {
            LogUtil.logException(TAG + " readJsonRequest failed", e);
            return null;
        }
    }
}
