package net.ecom.action;

import java.net.URLDecoder;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ReturnVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BookingCancelAction extends org.apache.struts.action.Action {

    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        ServletContext contexts = servlet.getServletContext();

        try {
            String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));

            if (loginUser == null || loginUser.isEmpty()) {
                return mapping.findForward("sessionex");
            }

            String lrpbookno = Validation.checkNull(request.getParameter("lrpbookno"));
            String ecombookno = Validation.checkNull(request.getParameter("ecombookno"));
            String cancelReason = Validation.checkNull(URLDecoder.decode(request.getParameter("cancelReason"), "UTF-8"));
            String cancelRemarks = Validation.checkNull(URLDecoder.decode(request.getParameter("cancelRemarks"), "UTF-8"));

            System.out.println("[ECOM-WAR] BookingCancelAction lrpbookno: " + lrpbookno + " ecombookno: " + ecombookno);
            System.out.println("[ECOM-WAR] BookingCancelAction cancelReason: " + cancelReason + " cancelRemarks: " + cancelRemarks);

            ReturnVO returnVO = bookingObj.cancelConfirmedBooking(lrpbookno, ecombookno, cancelReason, cancelRemarks, loginUser, userCode);

            System.out.println("[ECOM-WAR] BookingCancelAction result: " + returnVO.getStrRefNo());

            if ("Success".equalsIgnoreCase(returnVO.getStrRefNo())) {
                request.setAttribute("cancelSuccessMsg", "Your booking cancellation request has been submitted successfully!");

                String frommail = (String) contexts.getAttribute("clientfrommail");
                String client_code = (String) contexts.getAttribute("client_code");
                String useremail = Validation.checkNull((String) session.getAttribute("SESSION_EMAIL"));
                String initiatorID = "ECOM-BKG";
                String addccMailid = (String) contexts.getAttribute(GeneralConf.additionalbkgccmail);

                // Send mail to customer (AP)
                try {
                    String message = returnVO.getStrMailMsg();
                    if (message != null && message.trim().length() > 0) {
                        Mailer mail = new Mailer();
                        mail.setTo(returnVO.getStrToMailId());
                        mail.setSubject(returnVO.getStrSubject());
                        mail.setMessage(message);
                        if (addccMailid != null && addccMailid.trim().length() > 0) {
                            mail.setCc(addccMailid);
                        }
                        ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                        System.out.println("[ECOM-WAR] BookingCancelAction customer mail sent to: " + returnVO.getStrToMailId());
                    }
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] BookingCancelAction customer mail Exception: " + e.getMessage());
                    e.printStackTrace();
                }

                // Send mail to agency
                try {
                    String message2 = returnVO.getAdmMailMsg();
                    if (message2 != null && message2.trim().length() > 0
                            && returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0) {
                        Mailer mail2 = new Mailer();
                        mail2.setTo(returnVO.getAdmToMailId());
                        mail2.setSubject(returnVO.getAdmSubject());
                        mail2.setMessage(message2);
                        ecomCntrlObj.sendMailFromEjb(mail2, frommail, initiatorID, client_code);
                        System.out.println("[ECOM-WAR] BookingCancelAction agency mail sent to: " + returnVO.getAdmToMailId());
                    }
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] BookingCancelAction agency mail Exception: " + e.getMessage());
                    e.printStackTrace();
                }

            } else {
                request.setAttribute("cancelErrorMsg", "Unable to process the cancellation request. Please try again.");
            }

        } catch (Exception e) {
            System.out.println("[ECOM-WAR] BookingCancelAction Exception: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("cancelErrorMsg", "An error occurred while processing the cancellation request.");
        }

        return mapping.findForward("success");
    }
}
