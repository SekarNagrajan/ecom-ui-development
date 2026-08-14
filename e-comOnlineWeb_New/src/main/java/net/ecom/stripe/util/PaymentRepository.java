package net.ecom.stripe.util;

import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.common.LogUtil;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.OnlinePaymentReferenceVO;
import net.ecom.vo.OnlinePaymentVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Handles all database operations for online payment records.
 * Delegates persistence to the {@link EcomOnlinePaymentBeanRemote} EJB.
 */
public class PaymentRepository {

    private static final String TAG = "[PaymentRepository]";

    private PaymentRepository() {
    }

    public static boolean createPaymentRecord(UUID requestId, PaymentRequest paymentRequest,
                                              String customerCode, String loginName, String currency) {
        try {
            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();

            OnlinePaymentVO paymentVO = buildPaymentVO(requestId, paymentRequest, customerCode, loginName, currency);
            long paymentId = paymentBean.createPayment(paymentVO);
            LogUtil.trace(TAG, "Payment record created, id=" + paymentId + ", requestId=" + requestId);

            int refCount = insertReferenceRecords(paymentBean, requestId, paymentRequest);
            LogUtil.log(TAG, "References inserted: " + refCount + " for requestId=" + requestId);

            return paymentId > 0;

        } catch (Exception e) {
            LogUtil.logException(TAG + " createPaymentRecord failed for requestId=" + requestId, e);
            return false;
        }
    }

    public static void updateStripeSessionId(UUID requestId, String sessionId) {
        try {
            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
            paymentBean.updatePaymentWithStripeSession(requestId, sessionId, "SESSION_CREATED");
            LogUtil.log(TAG, "Updated stripeSessionId for requestId=" + requestId);
        } catch (Exception e) {
            LogUtil.logException(TAG + " updateStripeSessionId failed for requestId=" + requestId, e);
        }
    }

    private static OnlinePaymentVO buildPaymentVO(UUID requestId, PaymentRequest paymentRequest,
                                                  String customerCode, String loginName, String currency) {
        OnlinePaymentVO vo = new OnlinePaymentVO();
        vo.setRequestId(requestId);
        vo.setCustomerCode(customerCode);
        vo.setLoginName(loginName);
        vo.setPaymentType(PaymentHelper.mapPaymentType(paymentRequest.getPaymentType()));
        vo.setCurrency((currency != null && currency.trim().length() > 0) ? currency.trim().toUpperCase() : "USD");
        vo.setAmountUsd(paymentRequest.getTotalAmount());
        vo.setAmountCents(PaymentHelper.convertToCents(paymentRequest.getTotalAmount()));
        vo.setStatus("PENDING");
        vo.setDescription(PaymentHelper.getPaymentDescription(paymentRequest));
        vo.setAgencyCode(paymentRequest.getAgencyCode());
        vo.setCreatedAt(new Date());
        vo.setUpdatedAt(new Date());
        return vo;
    }

    private static int insertReferenceRecords(EcomOnlinePaymentBeanRemote paymentBean,
                                               UUID requestId, PaymentRequest paymentRequest) throws Exception {
        List<OnlinePaymentReferenceVO> references = new ArrayList<>();
        for (PaymentReference ref : paymentRequest.getReferences()) {
            OnlinePaymentReferenceVO refVO = new OnlinePaymentReferenceVO();
            refVO.setRequestId(requestId);
            refVO.setRefType(paymentRequest.getPaymentType());
            refVO.setRefNo(ref.getRefNo());
            String custType = ref.getCustomerType() != null ? ref.getCustomerType() : paymentRequest.getCustomerType();
            refVO.setCustType(custType);
            refVO.setAmountUsd(ref.getAmount());
            refVO.setAmountCents(PaymentHelper.convertToCents(ref.getAmount()));
            refVO.setCreatedAt(new Date());
            references.add(refVO);
        }
        return paymentBean.addPaymentReferences(references);
    }
}
