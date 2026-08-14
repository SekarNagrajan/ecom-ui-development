package net.ecom.ngenius;

import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.common.LogUtil;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.ngenius.util.NGeniusOrderStatusClient;
import net.ecom.ngenius.util.NGeniusOrderStatusClient.NGeniusOrderStatus;
import net.ecom.vo.OnlinePaymentVO;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Handles explicit cancellations from the N-Genius hosted payment page.
 *
 * <p><b>Fallback safety check:</b> N-Genius routes ALL outcomes (success AND cancel) through
 * the same redirect URL set in {@code merchantAttributes.cancelUrl}. In rare cases the browser
 * may hit this cancel URL even though the customer actually completed payment
 * (e.g. back-button, network glitch, or N-Genius redirect race with the webhook).
 *
 * <p>To protect against incorrectly marking a completed payment as CANCELLED, this action
 * queries the N-Genius order-status API before writing any update:
 * <ul>
 *   <li>If the live order is already PURCHASED/CAPTURED → skip the CANCELLED update (payment
 *       is/will be handled by the webhook or fallback in NGeniusSuccessAction).</li>
 *   <li>If the live order is AUTHORISED → also skip (auth-only flow, webhook will capture).</li>
 *   <li>If the live order is FAILED → mark FAILED with the API state.</li>
 *   <li>If the live order is genuinely CANCELLED/PENDING → mark CANCELLED as expected.</li>
 *   <li>If the API call itself fails → fall back to marking CANCELLED (original behaviour).</li>
 * </ul>
 *
 * Mirrors {@code StripeCancelAction}.
 */
public class NGeniusCancelAction extends Action {

    private static final String TAG = "[NGeniusCancelAction]";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "execute -> user cancel redirect from N-Genius");

        String requestIdStr = Validation.checkNull(request.getParameter("rid"));

        if (!requestIdStr.isEmpty()) {
            try {
                UUID requestId = UUID.fromString(requestIdStr);
                EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();

                applyWithFallbackCheck(paymentBean, requestId, request);

            } catch (IllegalArgumentException e) {
                LogUtil.logException(TAG + " Invalid requestId format: " + requestIdStr, e);
            } catch (Exception e) {
                LogUtil.logException(TAG + " Error handling cancel for requestId=" + requestIdStr, e);
            }
        } else {
            LogUtil.trace(TAG, "No requestId in cancel URL");
        }

        return mapping.findForward("success");
    }

    // -------------------------------------------------------------------------
    // Core logic: check live status before writing CANCELLED
    // -------------------------------------------------------------------------

    private void applyWithFallbackCheck(EcomOnlinePaymentBeanRemote paymentBean,
                                        UUID requestId,
                                        HttpServletRequest request) {
        // Step 1: load current DB record
        OnlinePaymentVO payment = null;
        try {
            payment = paymentBean.getPaymentByRequestId(requestId);
        } catch (Exception e) {
            LogUtil.logException(TAG + " Could not load payment for requestId=" + requestId, e);
        }

        // Step 2: if the record is already in a terminal state, do not overwrite
        if (payment != null) {
            String currentStatus = Validation.checkNull(payment.getStatus()).toUpperCase();
            if ("COMPLETED".equals(currentStatus)
                    || "FAILED".equals(currentStatus)
                    || "CANCELLED".equals(currentStatus)) {
                LogUtil.log(TAG, "Fallback: skipping cancel — current status is already '"
                        + currentStatus + "' for requestId=" + requestId);
                return;
            }
        }

        // Step 3: poll N-Genius API for live order state
        String orderRef = (payment != null) ? payment.getStripeSessionId() : null;

        if (orderRef != null && !orderRef.trim().isEmpty()) {
            LogUtil.log(TAG, "Fallback: polling N-Genius order status for requestId=" + requestId
                    + ", orderRef=" + orderRef);

            NGeniusOrderStatus liveStatus = NGeniusOrderStatusClient.fetch(
                    orderRef.trim(), request.getServletContext());

            if (liveStatus != null) {
                LogUtil.log(TAG, "Fallback: live orderState=" + liveStatus.getOrderState()
                        + ", paymentState=" + liveStatus.getPaymentState()
                        + " for requestId=" + requestId);

                if (liveStatus.isCompleted()) {
                    // Payment actually succeeded — do NOT cancel.
                    // NGeniusSuccessAction or the webhook will handle the completion.
                    LogUtil.log(TAG, "Fallback: N-Genius shows COMPLETED — skipping cancel update"
                            + " for requestId=" + requestId
                            + ". Webhook/SuccessAction will handle completion.");
                    applyCompletion(paymentBean, requestId, liveStatus);
                    return;
                }

                if (liveStatus.isAuthorised()) {
                    // Auth-only: do not cancel; the capture/webhook will settle this.
                    LogUtil.log(TAG, "Fallback: N-Genius shows AUTHORISED — skipping cancel update"
                            + " for requestId=" + requestId);
                    paymentBean.updatePaymentWithStripeCompletion(
                            requestId,
                            liveStatus.getPaymentRef(),
                            liveStatus.getAuthCode(),
                            liveStatus.getRrn(),
                            null,
                            "AUTHORISED");
                    return;
                }

                if (liveStatus.isFailed()) {
                    // Mark as failed rather than cancelled — more accurate reason.
                    // Use getEffectiveState() because orderState may be null when the bank
                    // declines before N-Genius writes the top-level order status.
                    int rows = paymentBean.updatePaymentWithError(requestId,
                            "Payment failed: " + liveStatus.getEffectiveState(), "FAILED");
                    LogUtil.log(TAG, "Fallback: updated to FAILED -> requestId=" + requestId
                            + ", rows=" + rows);
                    return;
                }

                // isCancelled() or any unknown pending state — fall through to CANCELLED below
                LogUtil.log(TAG, "Fallback: N-Genius state '" + liveStatus.getEffectiveState()
                        + "' — marking CANCELLED for requestId=" + requestId);
            } else {
                // API call failed — fall through to default CANCELLED update
                LogUtil.logException(TAG + " Fallback: API call returned null for orderRef=" + orderRef
                        + " — defaulting to CANCELLED for requestId=" + requestId);
            }
        } else {
            // No orderRef stored yet (very early cancel before order was persisted)
            LogUtil.trace(TAG, "Fallback: no orderRef stored — marking CANCELLED for requestId=" + requestId);
        }

        // Step 4: default — mark as CANCELLED
        int rows = paymentBean.updatePaymentWithError(requestId,
                "Payment cancelled by user", "CANCELLED");
        LogUtil.trace(TAG, "Updated to CANCELLED -> requestId=" + requestId + ", rows=" + rows);
    }

    // -------------------------------------------------------------------------
    // Apply completion (when cancel URL is hit but payment actually succeeded)
    // -------------------------------------------------------------------------

    private void applyCompletion(EcomOnlinePaymentBeanRemote paymentBean,
                                 UUID requestId,
                                 NGeniusOrderStatus liveStatus) {
        try {
            int rows = paymentBean.updatePaymentWithStripeCompletion(
                    requestId,
                    liveStatus.getPaymentRef(),
                    liveStatus.getAuthCode(),
                    liveStatus.getRrn(),
                    null,
                    "COMPLETED");
            LogUtil.log(TAG, "applyCompletion: updated to COMPLETED -> requestId=" + requestId
                    + ", rows=" + rows);
            if (rows > 0) {
                try {
                    boolean gatewayResult = paymentBean.insertPaymentGateway(requestId);
                    LogUtil.log(TAG, "applyCompletion: PaymentGateway insert -> requestId=" + requestId
                            + ", result=" + gatewayResult);
                } catch (Exception ge) {
                    LogUtil.logException(TAG + " applyCompletion: PaymentGateway insert failed"
                            + " for requestId=" + requestId, ge);
                }
            }
        } catch (Exception e) {
            LogUtil.logException(TAG + " applyCompletion error for requestId=" + requestId, e);
        }
    }
}
