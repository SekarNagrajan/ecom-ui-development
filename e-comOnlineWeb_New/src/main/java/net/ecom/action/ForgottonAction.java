package net.ecom.action;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.formbeans.ForgottenFormBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.Mailer;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ReturnVO;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import net.ecom.captcha.VerifyRecaptcha;

public class ForgottonAction extends org.apache.struts.action.Action {

    EcomRegistrationBeanRemote locobj = EJBLookUp.getRegistrationBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        String SUCCESS = "success";

        try {
            ServletContext contexts = servlet.getServletContext();
            ForgottenFormBean agencyform = (ForgottenFormBean) form;
            String strType = Validation.checkNull(agencyform.getStrType());
            ReturnVO rvo = new ReturnVO();
            session.setAttribute("NEWDESIGNPRODUCT", "forgotpass");
            String requrl = request.getRequestURL().toString();
            String requri = request.getRequestURI();
            String client_code = (String) contexts.getAttribute("client_code");
            String secretKey = Validation.checkNull((String) contexts.getAttribute(GeneralConf.CaptchaSecretKey));
            String toDisplayGgleCaptcha = (String) contexts.getAttribute(GeneralConf.DisplayGoogleCaptcha);
            String forgotpasswordcheck = (String) contexts.getAttribute(GeneralConf.Temp_Password_For_ForgotPWD);
            requrl = requrl.replace(requri, "");
            requrl = requrl + request.getContextPath();
            String restURL = requrl + "/validatePasswordResetToken.do?token=";
            //captcha
            String enable_Invalid_PasswordAttempts = (String) contexts.getAttribute(GeneralConf.Enable_Invalid_PasswordAttempts);
            String enableresetpassword = (String) contexts.getAttribute(GeneralConf.Enable_Password_Reset_By_URL);
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

            boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse, secretKey);

            System.out.println("[ECOM-WAR] ForgottonAction ::Captcha Verify value" + verify);

            //captcha
            if ("forgotpassword".equalsIgnoreCase(strType) && (!"Y".equalsIgnoreCase(toDisplayGgleCaptcha) || verify)) {
                String output = "";
                if ("forgotpassword".equalsIgnoreCase(strType)) {
                    if ("Y".equalsIgnoreCase(enableresetpassword)) {
                        // Generate a unique token
                        String token = UUID.randomUUID().toString();
                        rvo = locobj.insertPasswordResetToken(agencyform.getStrLoginName(), token);
                        restURL = restURL + token;
                    } else {
                        rvo = locobj.getForgotPasswordDetail(agencyform.getStrLoginName(), agencyform.getStrEmailId(), agencyform.getStrFirstName(), agencyform.getStrType(), forgotpasswordcheck);
                        if ("Y".equalsIgnoreCase(enable_Invalid_PasswordAttempts)) {
                            locobj.resetWrgPasAtmp(agencyform.getStrLoginName());
                        }
                    }
                }
                output = rvo.getStrDisplayMsg();
                String Message = "";
                String initiatorID = "ECOM-FOG";
                if (!output.equalsIgnoreCase(null) && output.length() > 0) {
                    Message = "mailSend";
                    String mailLogo = null;
                    String agencymail = (String) contexts.getAttribute("clientfrommail");
                    Mailer mail = new Mailer();
                    mail.setTo(rvo.getStrToMailId());
                    mail.setSubject(rvo.getStrSubject());
                    String msg = rvo.getStrMailMsg();
                    mailLogo = "<img src='" + (String) contexts.getAttribute(GeneralConf.MAILLOGOIMAGE) + "' alt='Logo'/>";
                    msg = msg.replace("@url", requrl);
                    msg = msg.replace("@maillogo", mailLogo);
                    msg = msg.replace("@resetUrl", restURL);
                  //  System.out.println("msg==> "+msg);
                    mail.setMessage(msg);
                    try {
                        //1 mail.sendMail(authObj,agencymail);
                        ecomCntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                } else {
                    Message = "mailSend";//mailNotSend
                }
                request.setAttribute("forgotpassword_display_Message", Message);
                request.setAttribute("forgotpassword_select_Opion", rvo.getStrSaveType());

            } else {
                request.setAttribute("forgotpassword_display_Message", "mailNotSend");
            }
            SUCCESS = "forgotpass";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
