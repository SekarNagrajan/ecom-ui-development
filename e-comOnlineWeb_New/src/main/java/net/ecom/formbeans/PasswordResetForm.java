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
 *
 * @author Norashikin
 */
public class PasswordResetForm extends org.apache.struts.action.ActionForm {

    private String username;
    private String email;
    private String oldpassword;
    private String newpassword;
    private String repassword;
    private String name;
    private String repassword_admin;
    private String newpassword_admin;
    private String oldpassword_admin;
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewpassword_admin() {
        return newpassword_admin;
    }

    public void setNewpassword_admin(String newpassword_admin) {
        this.newpassword_admin = newpassword_admin;
    }

    public String getOldpassword_admin() {
        return oldpassword_admin;
    }

    public void setOldpassword_admin(String oldpassword_admin) {
        this.oldpassword_admin = oldpassword_admin;
    }

    public String getRepassword_admin() {
        return repassword_admin;
    }

    public void setRepassword_admin(String repassword_admin) {
        this.repassword_admin = repassword_admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    /**
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param string
     */
    public void setUsername(String string) {
        username = string;
    }

    /**
     *
     */
    public PasswordResetForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getName() == null || getName().length() < 1) {
            errors.add("name", new ActionMessage("error.name.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        return errors;
    }
}
