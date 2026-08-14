/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.MailFormatVO;
import net.ecom.common.utils.OTPUtil;
import java.util.Date;

/**
 * Utility class for sending OTP emails when password expires
 *
 * @author gobinath.t
 */
public class PasswordExpiredOTPMail {

    // OTP expiration time in milliseconds (10 minutes)
    private static final long OTP_EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutes in milliseconds

    /**
     * Sends OTP email to the user when password expires
     *
     */
    public static void sendOTP(HttpServletRequest request) {
        String generatedOTP = null;
        try {
            if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_OTP_Password_Expired))) {
                System.out.println("Inside sendOTP");
                EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
                HttpSession session = request.getSession();
                String agencymail = (String) request.getServletContext().getAttribute("clientfrommail");
                String clientCode = (String) request.getServletContext().getAttribute("client_code");
                String userEmail = (String) session.getAttribute("SESSION_EMAIL");
                String firstName = (String) session.getAttribute("SESSION_USERNAME");
                // Generate 6-digit OTP
                generatedOTP = OTPUtil.generateOTP(6, true);
                // Get mail content template
                MailFormatVO mailVo = ecomCntrlObj.getMailContent("Login", "PassExpOTP", "C");

                // Prepare email content with OTP
                String emailContent = prepareOTPEmailContent(mailVo.getStrMailContent(), firstName, generatedOTP, userEmail);

                // Create mail object
                Mailer mail = new Mailer();
                mail.setTo(userEmail);
                mail.setSubject(mailVo.getStrSubject());
                mail.setMessage(emailContent);

                // Send mail
                String initiatorID = "ECOM-FOG";

                ecomCntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, clientCode);
                System.out.println("[Ecom] PasswordExpiredOTPMail Mail sent: " + generatedOTP);

                // Store OTP with timestamp for expiration validation
                session.setAttribute("PasswordExpiredOTP", generatedOTP);
                session.setAttribute("PasswordExpiredOTPTimestamp", new Date().getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String prepareOTPEmailContent(String template, String firstName, String otp, String emailId) {

        // Replace placeholders with actual values
        String content = template;
        content = content.replace("@Firstname", firstName != null ? firstName : "Customer");
        content = content.replace("@otp", otp);
        content = content.replace("@emailid", emailId);
        return content;
    }

    public static boolean validateOTP(HttpServletRequest request, String givenOTP) {
        boolean res = true;
        try {
            HttpSession session = request.getSession();
            String passexpired = (String) session.getAttribute("isPasswordExpired");
            if ("Y".equalsIgnoreCase(passexpired)) {
                String sessionOTP = Validation.checkNull((String) session.getAttribute("PasswordExpiredOTP"));
                Long otpTimestamp = (Long) session.getAttribute("PasswordExpiredOTPTimestamp");

                // Check if OTP exists and timestamp exists
                if (sessionOTP != null && !sessionOTP.isEmpty() && otpTimestamp != null) {
                    // Check if OTP matches
                    boolean otpMatches = sessionOTP.equals(Validation.checkNull(givenOTP));

                    // Check if OTP is not expired (within 10 minutes)
                    long currentTime = new Date().getTime();
                    boolean otpNotExpired = (currentTime - otpTimestamp) <= OTP_EXPIRATION_TIME;

                    res = otpMatches && otpNotExpired;

                    if (otpMatches && !otpNotExpired) {
                        System.out.println("[Ecom] PasswordExpiredOTPMail validateOTP: OTP expired");
                    } else if (!otpMatches) {
                        System.out.println("[Ecom] PasswordExpiredOTPMail validateOTP: OTP mismatch");
                    }
                } else {
                    System.out.println("[Ecom] PasswordExpiredOTPMail validateOTP: OTP or timestamp not found in session");
                }

                System.out.println("[Ecom] PasswordExpiredOTPMail validateOTP===>" + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }
}
