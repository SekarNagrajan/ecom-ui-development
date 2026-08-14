package net.ecom.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;
import net.ecom.common.LogUtil;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.OnlinePaymentReferenceVO;
import net.ecom.vo.OnlinePaymentVO;

public class PaymentHistoryAction extends Action {

    private static final String TAG = "[PaymentHistoryAction]";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.trace(TAG, "execute");

        HttpSession session = request.getSession();
        String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));

        String fromDateParam = request.getParameter("fromDate");
        String toDateParam = request.getParameter("toDate");

        String dbToDate;
        String dbFromDate;
        String displayToDate;
        String displayFromDate;

        if (toDateParam != null && !toDateParam.trim().isEmpty()) {
            displayToDate = toDateParam.trim();
            dbToDate = DateConversion_New.convertDefaultDBFormat(displayToDate);
        } else {
            displayToDate = DateConversion_New.getCurrentDate();
            dbToDate = DateConversion_New.getCurrentDateDbFormat();
        }

        if (fromDateParam != null && !fromDateParam.trim().isEmpty()) {
            displayFromDate = fromDateParam.trim();
            dbFromDate = DateConversion_New.convertDefaultDBFormat(displayFromDate);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -3);
            dbFromDate = DateConversion_New.convertDateToString(cal.getTime());
            displayFromDate = DateConversion_New.convertDefaultFormat(dbFromDate);
        }

        LogUtil.trace(TAG, "userCode=" + userCode + ", from=" + dbFromDate + ", to=" + dbToDate);

        try {
            session.setAttribute("NEWDESIGNPRODUCT", "paymenthistory");

            EcomOnlinePaymentBeanRemote paymentBean = EJBLookUp.getEcomOnlinePaymentBean();
            List<OnlinePaymentVO> paymentList = paymentBean.getPaymentHistoryByCustomer(userCode, dbFromDate, dbToDate);
            List<OnlinePaymentReferenceVO> allRefs = paymentBean.getReferencesByCustomerPayments(userCode, dbFromDate, dbToDate);

            Map<String, List<OnlinePaymentReferenceVO>> refMap = new HashMap<>();
            if (allRefs != null) {
                for (OnlinePaymentReferenceVO ref : allRefs) {
                    String key = ref.getRequestId() != null ? ref.getRequestId().toString() : "";
                    refMap.computeIfAbsent(key, k -> new ArrayList<>()).add(ref);
                }
            }

            LogUtil.trace(TAG, "Loaded records=" + (paymentList != null ? paymentList.size() : 0)
                    + ", refs=" + (allRefs != null ? allRefs.size() : 0));

            request.setAttribute("PAYMENT_HISTORY_LIST", paymentList);
            request.setAttribute("PAYMENT_REF_MAP", refMap);
            request.setAttribute("FILTER_FROM_DATE", displayFromDate);
            request.setAttribute("FILTER_TO_DATE", displayToDate);

        } catch (Exception e) {
            LogUtil.logException(TAG, e);
        }

        return mapping.findForward("success");
    }
}
