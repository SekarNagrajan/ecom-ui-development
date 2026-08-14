package net.ecom.stripe;

import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.common.LogUtil;
import net.ecom.common.PaymentEventNotificationMail;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class StripeCancelAction extends Action {

    private static final String TAG = "[StripeCancelAction]";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "execute -> user cancelled payment");

        String requestIdStr = Validation.checkNull(request.getParameter("rid"));
        String notificationStatus = "CANCELLED";
        String additionalInfo = null;

        if (requestIdStr != null && !requestIdStr.isEmpty()) {
            try {
                UUID requestId = UUID.fromString(requestIdStr);
                EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
                int rows = paymentBean.updatePaymentWithError(requestId,
                        "Payment cancelled by user", "CANCELLED");
                LogUtil.trace(TAG, "Updated to CANCELLED -> requestId=" + requestId + ", rows=" + rows);
                additionalInfo = "requestId=" + requestId + ", rows=" + rows;
            } catch (IllegalArgumentException e) {
                LogUtil.logException(TAG + " Invalid requestId format: " + requestIdStr, e);
                notificationStatus = "ERROR";
                additionalInfo = "Invalid requestId: " + requestIdStr;
            } catch (Exception e) {
                LogUtil.logException(TAG + " Error updating cancelled payment", e);
                notificationStatus = "ERROR";
                additionalInfo = "Update error: " + e.getMessage();
            }
        } else {
            LogUtil.trace(TAG, "No requestId in cancel URL");
            additionalInfo = "No requestId in cancel URL";
        }

        PaymentEventNotificationMail.notifyIfConfigured(
                request.getServletContext(),
                "Stripe",
                "checkout.cancel",
                notificationStatus,
                requestIdStr != null && !requestIdStr.isEmpty() ? requestIdStr : "-",
                additionalInfo);

        return mapping.findForward("success");
    }
}
