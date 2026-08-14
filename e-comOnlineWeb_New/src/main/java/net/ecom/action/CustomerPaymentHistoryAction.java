package net.ecom.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.PaymentHistoryVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CustomerPaymentHistoryAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext context = servlet.getServletContext();
        HttpSession session = request.getSession();

        List<PaymentHistoryVO> paymentHistoryList = null;
        String fromDate = null, toDate = null, fromDatet = null, toDatet = null;
        String paymentHistoryCompCode = (String) context.getAttribute(GeneralConf.PAYMENT_hISTORY_COMP_CODE);
        String phDateIntervalStr = Validation.checkNull((String) context.getAttribute(GeneralConf.PAYMENT_hISTORY_DATE_INTERVAL));
        int phDateInterval = 0;
        if (phDateIntervalStr.length() > 0) {
            try { phDateInterval = Integer.parseInt(phDateIntervalStr); } catch (NumberFormatException ignored) {}
        }
        System.out.println("CustomerPaymentHistoryAction ==================>>>" + fromDate + " " + toDate + " " + fromDatet + " " + toDatet);         
        try {
            session.setAttribute("NEWDESIGNPRODUCT", "paymenthistory");
            session.setAttribute("IS_ACTIVEPAGE", "paymenthistorypage");
            session.setAttribute("IS_MENU_LOCK", "NO");

            DateFormat dateFormat = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            Calendar cal = null;
            String customerCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            fromDate = Validation.checkNull(request.getParameter("frdate"));
            toDate = Validation.checkNull(request.getParameter("todate"));
            fromDatet = Validation.checkNull(request.getParameter("frdate"));
            toDatet = Validation.checkNull(request.getParameter("todate"));

            if (toDate.length() <= 0) {
                cal = Calendar.getInstance();
                toDate = dateFormat.format(cal.getTime());
                toDatet = toDate;
                toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
            } else {
                toDatet = toDate;
                toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
            }
            if (fromDate.length() <= 0) {
                cal = Calendar.getInstance();
                if (phDateInterval > 0) {
                    cal.add(Calendar.DAY_OF_MONTH, -phDateInterval);
                }
                fromDate = dateFormat.format(cal.getTime());
                fromDatet = fromDate;
                fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            } else {
                fromDatet = fromDate;
                fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            }
            System.out.println("CustomerPaymentHistoryAction ==================>>>" + customerCode + " " + fromDate + " " + toDate + " " + paymentHistoryCompCode);
            paymentHistoryList = ecomPhase2Obj.getpaymentHistoryDetails(customerCode, fromDate, toDate, "", paymentHistoryCompCode);

            request.setAttribute("paymentHistoryList", paymentHistoryList);
            request.setAttribute("phFromDate", fromDatet);
            request.setAttribute("phToDate", toDatet);

            session.setAttribute("paymentHistoryListForPrint", paymentHistoryList);

        } catch (Exception e) {
            System.out.println("[ECOM-WAR] CustomerPaymentHistoryAction ==================>>>" + e);
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
