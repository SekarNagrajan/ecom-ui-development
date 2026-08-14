package net.ecom.stripe;

import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.common.LogUtil;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
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

public class StripeSuccessAction extends Action {

    private static final String TAG = "[StripeSuccessAction]";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "execute -> payment success page");

        String sessionId = Validation.checkNull(request.getParameter("session_id"));

        if (sessionId.isEmpty()) {
            LogUtil.trace(TAG, "No session_id param, showing error");
            request.setAttribute("errorMsg", "Payment session not found.");
            return mapping.findForward("success");
        }

        LogUtil.log(TAG, "Stripe sessionId=" + sessionId);

        String requestIdStr = Validation.checkNull(request.getParameter("rid"));
        if (requestIdStr.isEmpty()) {
            HttpSession session = request.getSession(false);
            requestIdStr = (session != null)
                    ? (String) session.getAttribute("STRIPE_PAYMENT_REQUEST_ID") : null;
        }

        if (requestIdStr != null && !requestIdStr.isEmpty()) {
            try {
                UUID requestId = UUID.fromString(requestIdStr);
                EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();

                OnlinePaymentVO payment = paymentBean.getPaymentByRequestId(requestId);
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
            LogUtil.trace(TAG, "No requestId available, basic success page");
        }

        request.setAttribute("sessionId", sessionId);
        return mapping.findForward("success");
    }
}
