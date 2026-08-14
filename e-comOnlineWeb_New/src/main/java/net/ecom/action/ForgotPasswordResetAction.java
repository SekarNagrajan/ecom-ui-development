/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.Validation;
import net.ecom.formbeans.ForgotPasswordResetForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.PasswordResetTokenVo;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action class for handling forgot password reset functionality This action
 * validates the reset token and updates the user's password
 *
 * @author System
 */
public class ForgotPasswordResetAction extends org.apache.struts.action.Action {

    EcomRegistrationBeanRemote locobj = EJBLookUp.getRegistrationBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        ForgotPasswordResetForm resetForm = (ForgotPasswordResetForm) form;

        // Get form data
        String newPassword = Validation.checkNull(resetForm.getNewPassword());
        String confirmPassword = Validation.checkNull(resetForm.getConfirmPassword());
        String token = Validation.checkNull(resetForm.getToken());

        String result = "error";

        try {
            System.out.println("inside ForgotPasswordResetAction");

            // Validate password fields
            if (newPassword == null || newPassword.trim().isEmpty()) {
                request.setAttribute("ErrorMsg", "New password is required.");
                return mapping.findForward("failure");
            }

            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("ErrorMsg", "Please confirm your new password.");
                return mapping.findForward("failure");
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("ErrorMsg", "Passwords do not match. Please try again.");
                return mapping.findForward("failure");
            }

            // Validate password reset token
            PasswordResetTokenVo regvo = locobj.validatePasswordResetToken(token);

            // Check if token validation response is null
            if (regvo == null) {
                request.setAttribute("ErrorMsg", "Token validation failed. Please request a new password reset link.");
                return mapping.findForward("failure");
            }

            // Validate token status based on response
            String tokenStatus = regvo.getStrName();
            String tokenDbStatus = regvo.getStatus();

            if ("VALID".equals(tokenStatus)) {
                // Token is valid, check database status
                if ("R".equals(tokenDbStatus)) {
                    // Token is in Requested status, proceed with password update
                    String res = locobj.updatePasswordWithToken(regvo.getEmailID(), newPassword, token);

                    if ("success".equalsIgnoreCase(res)) {
                        request.setAttribute("SuccessMsg", "Your password has been successfully reset. You can now login with your new password.");
                        result = "success";
                    } else {
                        request.setAttribute("ErrorMsg", "Failed to update password: " + res + ". Please try again or contact support.");
                        result = "failure";
                    }
                } else if ("C".equals(tokenDbStatus)) {
                    // Token already completed
                    request.setAttribute("ErrorMsg", "This password reset link has already been used. Please request a new password reset link.");
                    result = "failure";
                } else if ("E".equals(tokenDbStatus)) {
                    // Token expired
                    request.setAttribute("ErrorMsg", "This password reset link has expired. Please request a new password reset link.");
                    result = "failure";
                } else {
                    // Invalid token status
                    request.setAttribute("ErrorMsg", "Invalid token status. Please request a new password reset link.");
                    result = "failure";
                }
            } else if ("EXPIRED".equals(tokenStatus)) {
                // Token expired (30 minutes validation)
                request.setAttribute("ErrorMsg", "This password reset link has expired. Please request a new password reset link.");
                result = "failure";
            } else if ("INVALID".equals(tokenStatus)) {
                // Invalid token
                request.setAttribute("ErrorMsg", "Invalid password reset link. Please request a new password reset link.");
                result = "failure";
            } else {
                // Unknown validation status
                request.setAttribute("ErrorMsg", "Token validation failed: " + regvo.getStrDisplayMsg() + ". Please request a new password reset link.");
                result = "failure";
            }
            System.out.println("[Ecom] ErrorMsg==> " + request.getAttribute("ErrorMsg"));
        } catch (Exception e) {
            System.err.println("Error in ForgotPasswordResetAction: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ErrorMsg", "An error occurred while processing your request. Please try again.");
            result = "failure";
        }

        return mapping.findForward(result);
    }

}
