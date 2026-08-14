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
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.PasswordResetTokenVo;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action class for validating password reset tokens before page load
 * This action validates the reset token and redirects accordingly
 *
 * @author System
 */
public class ValidatePasswordResetTokenAction extends Action {

    EcomRegistrationBeanRemote locobj = EJBLookUp.getRegistrationBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();

        // Get token from URL parameter
        String token = Validation.checkNull(request.getParameter("token"));

        try {
            System.out.println("inside ValidatePasswordResetTokenAction - Token: " + token);
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Request Method: " + request.getMethod());

            // Check if token is provided
            if (token == null || token.trim().isEmpty()) {
                // If no token provided, redirect to forgot password page
                return mapping.findForward("redirect");
            }

            // Validate password reset token
            PasswordResetTokenVo regvo = locobj.validatePasswordResetToken(token);

            // Check if token validation response is null
            if (regvo == null) {
                request.setAttribute("ErrorMsg", "Token validation failed. Please request a new password reset link.");
                return mapping.findForward("invalid");
            }

            // Validate token status based on response
            String tokenStatus = regvo.getStrName();
            String tokenDbStatus = regvo.getStatus();

            if ("VALID".equals(tokenStatus)) {
                // Token is valid, check database status
                if ("R".equals(tokenDbStatus)) {
                    // Token is in Requested status, proceed to reset page
                    request.setAttribute("token", token);
                    return mapping.findForward("valid");
                } else if ("C".equals(tokenDbStatus)) {
                    // Token already completed
                    request.setAttribute("ErrorMsg", "This password reset link has already been used. Please request a new password reset link.");
                    return mapping.findForward("invalid");
                } else if ("E".equals(tokenDbStatus)) {
                    // Token expired
                    request.setAttribute("ErrorMsg", "This password reset link has expired. Please request a new password reset link.");
                    return mapping.findForward("invalid");
                } else {
                    // Invalid token status
                    request.setAttribute("ErrorMsg", "Invalid token status. Please request a new password reset link.");
                    return mapping.findForward("invalid");
                }
            } else if ("EXPIRED".equals(tokenStatus)) {
                // Token expired (30 minutes validation)
                request.setAttribute("ErrorMsg", "This password reset link has expired. Please request a new password reset link.");
                return mapping.findForward("invalid");
            } else if ("INVALID".equals(tokenStatus)) {
                // Invalid token
                request.setAttribute("ErrorMsg", "Invalid password reset link. Please request a new password reset link.");
                return mapping.findForward("invalid");
            } else {
                // Unknown validation status
                request.setAttribute("ErrorMsg", "Token validation failed: " + regvo.getStrDisplayMsg() + ". Please request a new password reset link.");
                return mapping.findForward("invalid");
            }

        } catch (Exception e) {
            System.err.println("Error in ValidatePasswordResetTokenAction: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ErrorMsg", "An error occurred while validating your request. Please try again.");
            return mapping.findForward("invalid");
        }
    }
} 