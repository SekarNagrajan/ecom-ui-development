/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * Form bean for handling forgot password reset functionality
 * This form handles the token-based password reset process
 * 
 * @author System
 */
public class ForgotPasswordResetForm extends org.apache.struts.action.ActionForm {

    private String newPassword;
    private String confirmPassword;
    private String token;
    private String email;

    /**
     * Default constructor
     */
    public ForgotPasswordResetForm() {
        super();
    }

    /**
     * Get the new password
     * @return new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Set the new password
     * @param newPassword the new password to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Get the confirm password
     * @return confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Set the confirm password
     * @param confirmPassword the confirm password to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Get the reset token
     * @return reset token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the reset token
     * @param token the reset token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get the email address
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Validate the form data
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP Request we are processing
     * @return ActionErrors object containing validation errors
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        // Validate new password
        if (newPassword == null || newPassword.trim().length() < 1) {
            errors.add("newPassword", new ActionMessage("error.newpassword.required"));
        } else if (newPassword.trim().length() < 8) {
            errors.add("newPassword", new ActionMessage("error.newpassword.length"));
        }
        
        // Validate confirm password
        if (confirmPassword == null || confirmPassword.trim().length() < 1) {
            errors.add("confirmPassword", new ActionMessage("error.confirmpassword.required"));
        }
        
        // Validate password match
        if (newPassword != null && confirmPassword != null && 
            !newPassword.equals(confirmPassword)) {
            errors.add("confirmPassword", new ActionMessage("error.password.mismatch"));
        }
        
        // Validate token
        if (token == null || token.trim().length() < 1) {
            errors.add("token", new ActionMessage("error.token.required"));
        }
        
        return errors;
    }

    /**
     * Reset all properties to their default values
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP Request we are processing
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        newPassword = null;
        confirmPassword = null;
        token = null;
        email = null;
    }
} 