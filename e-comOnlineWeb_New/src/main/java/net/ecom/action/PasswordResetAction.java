/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.captcha.VerifyRecaptcha;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.PasswordExpiredOTPMail;
import net.ecom.common.Validation;
import net.ecom.formbeans.PasswordResetForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ReturnVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PasswordResetAction extends org.apache.struts.action.Action {

    EcomRegistrationBeanRemote ecomLookUpObj = EJBLookUp.getRegistrationBeanLookUp();
    EComCntrlBeanRemote cntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    Validation validate = new Validation();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String userDataObj = "error";
        HttpSession session = request.getSession();
        String str_type = (String) session.getAttribute("strType");
        ServletContext contexts = servlet.getServletContext();
        String chgPassCaptcha = Validation.checkNull((String) contexts.getAttribute(GeneralConf.Enable_ChangePassword_Captcha));
        String secretKey = Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.CaptchaSecretKey));
        String enableOTP = Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.Enable_OTP_Password_Expired));
        int passattemptCount = 0;
        boolean verify = true;
        boolean isValidOTP = true;
        PasswordResetForm sdm = new PasswordResetForm();
        sdm = (PasswordResetForm) form;
        if ("Y".equalsIgnoreCase(chgPassCaptcha)) {
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            Object attemptCountObj = session.getAttribute("ChangePasswordCount");
            passattemptCount = (attemptCountObj != null) ? (int) attemptCountObj : 0;
            if (passattemptCount >= 3) {
                verify = VerifyRecaptcha.verify(gRecaptchaResponse, secretKey);
            }
            passattemptCount = passattemptCount + 1;
            session.setAttribute("ChangePasswordCount", passattemptCount);
        }
        if ("Y".equalsIgnoreCase(enableOTP)) {
            isValidOTP = PasswordExpiredOTPMail.validateOTP(request, sdm.getOtp());
        }
        if (verify && isValidOTP) {
            if ("new".equalsIgnoreCase(str_type)) {
                session.setAttribute("strType", "");

                String loginName = sdm.getUsername();
                loginName = Validation.checkNull(loginName);
                String password = sdm.getOldpassword();
                password = Validation.checkNull(password);
                String email = sdm.getEmail();
                email = Validation.checkNull(email);
                String npassword = sdm.getNewpassword();
                npassword = Validation.checkNull(npassword);
                loginName = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                email = Validation.checkNull((String) session.getAttribute("SESSION_EMAIL"));
                String incompletedreg = Validation.checkNull((String) session.getAttribute("SESSION_INCOMPLETED_REG"));
                if ("Y".equalsIgnoreCase(incompletedreg)) {
                    userDataObj = ecomLookUpObj.getIncompletedRegUserResDet(loginName, password, npassword);
                } else {
                    userDataObj = ecomLookUpObj.getUserResDet(loginName, password, npassword, email);
                }
                try {
                    if (userDataObj.equalsIgnoreCase("Successfully Updated To database")) {
                        session.setAttribute("isPasswordExpired", "N");
                        String output = "";
                        ReturnVO rvo = null;
                        if ("Y".equalsIgnoreCase(incompletedreg)) {
                            rvo = ecomLookUpObj.getResetPasswordDetailForIncompletedReg(loginName);
                        } else {
                            rvo = ecomLookUpObj.getResetPasswordDetail(loginName, email);
                        }
                        output = rvo.getStrDisplayMsg();
                        String requrl = request.getRequestURL().toString();
                        String requri = request.getRequestURI();

                        requrl = requrl.replace(requri, "");
                        requrl = requrl + request.getContextPath();
                        String Message = "";
                        String initiatorID = "ECOM-REG";
                        if (!output.equalsIgnoreCase(null) && output.length() > 0) {
                            Message = "mailSend";
                            String agencymail = (String) contexts.getAttribute("clientfrommail");
                            String client_code = (String) contexts.getAttribute("client_code");
                            Mailer mail = new Mailer();
                            mail.setTo(rvo.getStrToMailId());
                            mail.setSubject(rvo.getStrSubject());
                            String msg = rvo.getStrMailMsg();
                            msg = msg.replace("@url", requrl);
                            mail.setMessage(msg);
                            try {
                                cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }

                        } else {
                            Message = "mailNotSend";

                        }
                        session.setAttribute("IS_MENU_LOCK", "");
                        session.setAttribute("SESSION_USERNAME", "");
                        session.setAttribute("systemid", session.getId());
                        userDataObj = "success";
                    } else if (userDataObj.equalsIgnoreCase("Invalid Email Address")) {
                        String errormsg1 = "Invalid Email Address";
                        request.setAttribute("ErrorMsg1", errormsg1);
                        session.setAttribute("LoginName", loginName);
                        session.setAttribute("OPassword", password);
                        session.setAttribute("NPassword", npassword);
                        session.setAttribute("UserEmail", email);
                        userDataObj = "failure";
                    } else if (userDataObj.equalsIgnoreCase("Invalid Old Password")) {
                        String errormsg2 = "Invalid Old Password";
                        request.setAttribute("ErrorMsg3", errormsg2);
                        session.setAttribute("LoginName", loginName);
                        session.setAttribute("OPassword", password);
                        session.setAttribute("NPassword", npassword);
                        session.setAttribute("UserEmail", email);
                        userDataObj = "failure";
                    } else {
                        String errormsg = "Invalid Username";
                        request.setAttribute("ErrorMsg2", errormsg);
                        session.setAttribute("LoginName", loginName);
                        session.setAttribute("OPassword", password);
                        session.setAttribute("NPassword", npassword);
                        session.setAttribute("UserEmail", email);
                        userDataObj = "failure";
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                userDataObj = "error";
                session.setAttribute("AgencyAdminMap", "");
            }
        } else {
            String errormsg1 = "Invalid Google Captcha!!";
            if (!isValidOTP) {
                errormsg1 = "Invalid OTP!!";
            }
            request.setAttribute("ErrorMsg1", errormsg1);
            userDataObj = "failure";
        }
        return mapping.findForward(userDataObj);

    }
}
