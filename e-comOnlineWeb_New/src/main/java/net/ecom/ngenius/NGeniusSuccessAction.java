package net.ecom.ngenius;

import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.common.LogUtil;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.ngenius.util.NGeniusOrderStatusClient;
import net.ecom.ngenius.util.NGeniusOrderStatusClient.NGeniusOrderStatus;
import net.ecom.vo.OnlinePaymentReferenceVO;
import net.ecom.vo.OnlinePaymentVO;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * Handles the browser redirect after the customer completes (or cancels) on the N-Genius hosted page.
 * N-Genius redirects to the configured {@code merchantAttributes.redirectUrl} for all outcomes.
 *
 * <p><b>Fallback logic:</b> The webhook is the authoritative update path, but it may arrive
 * after (or be missed entirely due to network issues). When this action finds the payment still
 * in PENDING status, it calls the N-Genius order-status API directly to get the live result
 * and applies the same completion / failure / cancel update that the webhook would have done.
 *
 * <p>This ensures the customer sees an accurate status on first page load even if the webhook
 * has not yet fired.
 *
 * Mirrors {@code StripeSuccessAction}.
 */
public class NGeniusSuccessAction extends Action {

    private static final String TAG = "[NGeniusSuccessAction]";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "execute -> N-Genius success/return page");

        String requestIdStr = Validation.checkNull(request.getParameter("rid"));

        if (requestIdStr.isEmpty()) {
            HttpSession session = request.getSession(false);
            requestIdStr = (session != null)
                    ? Validation.checkNull((String) session.getAttribute("NGENIUS_PAYMENT_REQUEST_ID")) : "";
        }

        if (!requestIdStr.isEmpty()) {
            try {
                UUID requestId = UUID.fromString(requestIdStr);
                EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();

                OnlinePaymentVO payment = paymentBean.getPaymentByRequestId(requestId);

                // -----------------------------------------------------------
                // Fallback: if the webhook has not yet resolved the payment,
                // poll the N-Genius order-status API and apply the result now.
                //
                // Trigger for any status that is NOT a terminal resolved state:
                //   PENDING        — initial DB record (orderRef not yet stored)
                //   SESSION_CREATED — orderRef stored, webhook not yet received (normal
                //                     state when the browser redirect arrives)
                //   Any other unknown intermediate status — handled defensively.
                // Skip only when already COMPLETED, AUTHORISED, FAILED, or CANCELLED.
                // -----------------------------------------------------------
                if (payment != null
                        && !isTerminalStatus(payment.getStatus())) {
                    payment = applyFallbackCheck(paymentBean, requestId, payment, request);
                }

                if (payment != null) {
                    request.setAttribute("payment", payment);
                }

                List<OnlinePaymentReferenceVO> refs = paymentBean.getReferencesByRequestId(requestId);
                if (refs != null && !refs.isEmpty()) {
                    request.setAttribute("paymentRefs", refs);
                }

                LogUtil.trace(TAG, "Payment loaded -> requestId=" + requestId
                        + ", status=" + (payment != null ? payment.getStatus() : "null")
                        + ", refs=" + (refs != null ? refs.size() : 0));

            } catch (IllegalArgumentException e) {
                LogUtil.logException(TAG + " Invalid requestId format: " + requestIdStr, e);
            } catch (Exception e) {
                LogUtil.logException(TAG + " Error loading payment details", e);
            }
        } else {
            LogUtil.trace(TAG, "No requestId available, showing basic landing page");
        }

        request.setAttribute("requestId", requestIdStr);
        return mapping.findForward("success");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns true when the payment has already reached a terminal state and
     * no further API polling is needed.
     */
    private boolean isTerminalStatus(String status) {
        if (status == null) return false;
        switch (status.toUpperCase()) {
            case "COMPLETED":
            case "AUTHORISED":
            case "FAILED":
            case "CANCELLED":
                return true;
            default:
                return false;
        }
    }

    // -------------------------------------------------------------------------
    // Fallback: poll N-Genius API and apply result when payment is not yet resolved
    // -------------------------------------------------------------------------

    /**
     * Calls the N-Genius order-status API using the stored order reference ({@code stripeSessionId}).
     * Applies the appropriate DB update (COMPLETED / AUTHORISED / FAILED / CANCELLED) and
     * returns a refreshed {@link OnlinePaymentVO} so the JSP shows the correct status.
     *
     * @param paymentBean EJB remote interface
     * @param requestId   Our internal payment UUID
     * @param payment     Current DB record (status is PENDING or SESSION_CREATED)
     * @param request     HTTP request — used to read servlet context config
     * @return Refreshed payment record after DB update, or the original if API call fails
     */
    private OnlinePaymentVO applyFallbackCheck(EcomOnlinePaymentBeanRemote paymentBean,
                                               UUID requestId,
                                               OnlinePaymentVO payment,
                                               HttpServletRequest request) {
        try {
            String orderRef = payment.getStripeSessionId();
            if (orderRef == null || orderRef.trim().isEmpty()) {
                LogUtil.trace(TAG, "Fallback skipped — no orderRef stored for requestId=" + requestId);
                return payment;
            }

            LogUtil.log(TAG, "Fallback: polling N-Genius order status for requestId=" + requestId
                    + ", orderRef=" + orderRef);

            NGeniusOrderStatus liveStatus = NGeniusOrderStatusClient.fetch(
                    orderRef, request.getServletContext());

            if (liveStatus == null) {
                // API returned 404 or a network/parse error.
                // Leave payment unchanged — the hourly job will resolve it later.
                LogUtil.logException(TAG + " Fallback: N-Genius API returned null/404 for orderRef=" + orderRef
                        + " — will be resolved by background job");
                return payment;
            }

            LogUtil.log(TAG, "Fallback: live orderState=" + liveStatus.getOrderState()
                    + ", paymentState=" + liveStatus.getPaymentState()
                    + " for requestId=" + requestId);

            if (liveStatus.isCompleted()) {
                // Mirror webhook PURCHASED/CAPTURED handling
                int rows = paymentBean.updatePaymentWithStripeCompletion(
                        requestId,
                        liveStatus.getPaymentRef(),
                        liveStatus.getAuthCode(),
                        liveStatus.getRrn(),
                        null,
                        "COMPLETED");

                LogUtil.log(TAG, "Fallback: updated to COMPLETED -> requestId=" + requestId
                        + ", rows=" + rows);

                if (rows > 0) {
                    try {
                        boolean gatewayResult = paymentBean.insertPaymentGateway(requestId);
                        LogUtil.log(TAG, "Fallback: PaymentGateway insert -> requestId=" + requestId
                                + ", result=" + gatewayResult);
                    } catch (Exception ge) {
                        LogUtil.logException(TAG + " Fallback: PaymentGateway insert failed for requestId="
                                + requestId, ge);
                    }
                }

            } else if (liveStatus.isAuthorised()) {
                // Auth-only — store payment ref but do not insert gateway
                int rows = paymentBean.updatePaymentWithStripeCompletion(
                        requestId,
                        liveStatus.getPaymentRef(),
                        liveStatus.getAuthCode(),
                        liveStatus.getRrn(),
                        null,
                        "AUTHORISED");
                LogUtil.log(TAG, "Fallback: updated to AUTHORISED -> requestId=" + requestId
                        + ", rows=" + rows);

            } else if (liveStatus.isFailed()) {
                // Mirror webhook DECLINED/FAILED handling.
                // Use getEffectiveState() because orderState may be null when
                // the bank declines before N-Genius writes the top-level status.
                int rows = paymentBean.updatePaymentWithError(requestId,
                        "Payment failed: " + liveStatus.getEffectiveState(), "FAILED");
                LogUtil.log(TAG, "Fallback: updated to FAILED -> requestId=" + requestId
                        + ", rows=" + rows);

            } else if (liveStatus.isCancelled()) {
                // Mirror webhook CANCELLED/REVERSED handling
                int rows = paymentBean.updatePaymentWithError(requestId,
                        "Payment cancelled/reversed: " + liveStatus.getEffectiveState(), "CANCELLED");
                LogUtil.log(TAG, "Fallback: updated to CANCELLED -> requestId=" + requestId
                        + ", rows=" + rows);

            } else if (liveStatus.isAbandoned()) {
                // Customer closed/abandoned the N-Genius hosted payment page without paying.
                // The order stays SESSION_CREATED on N-Genius side and will never progress.
                // Mark CANCELLED so the record is resolved and the user sees a clear message.
                int rows = paymentBean.updatePaymentWithError(requestId,
                        "Payment session not completed — customer did not submit payment", "CANCELLED");
                LogUtil.log(TAG, "Fallback: updated to CANCELLED (abandoned) -> requestId=" + requestId
                        + ", rows=" + rows);

            } else if (liveStatus.isProcessing()) {
                // Customer submitted payment; N-Genius is still processing (e.g. 3DS challenge,
                // bank processing). The webhook will arrive shortly and resolve the record.
                // Signal the JSP to show an auto-refresh spinner — do NOT update the DB yet.
                LogUtil.log(TAG, "Fallback: N-Genius still processing orderState="
                        + liveStatus.getOrderState() + " for requestId=" + requestId
                        + " — page will auto-refresh");
                request.setAttribute("ngeniusProcessing", Boolean.TRUE);
                return payment;

            } else {
                // Truly unknown state — log and leave unchanged; webhook may still arrive.
                LogUtil.log(TAG, "Fallback: unrecognised live state=" + liveStatus.getEffectiveState()
                        + " — no DB update for requestId=" + requestId);
                return payment;
            }

            // Reload the record so the JSP reflects the updated status
            OnlinePaymentVO refreshed = paymentBean.getPaymentByRequestId(requestId);
            return refreshed != null ? refreshed : payment;

        } catch (Exception e) {
            LogUtil.logException(TAG + " applyFallbackCheck error for requestId=" + requestId, e);
            return payment;
        }
    }
}
