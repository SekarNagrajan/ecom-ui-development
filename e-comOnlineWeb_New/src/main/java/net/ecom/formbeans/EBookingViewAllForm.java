/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import net.ecom.common.Validation;

/**
 *
 * @author prabakarans
 */
public class EBookingViewAllForm extends org.apache.struts.action.ActionForm {

    private String name;
    private String frdateport;
    private String todateport;
    private int number;

    public String getName() {
        return Validation.checkNull(name);
    }

    public void setName(String string) {
        name = string;
    }

    public int getNumber() {
        return number;
    }


    public void setNumber(int i) {
        number = i;
    }

    public EBookingViewAllForm() {
        super();
        // TODO Auto-generated constructor stub
    }


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getName() == null || getName().length() < 1) {
            errors.add("name", new ActionMessage("error.name.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        return errors;
    }

    public String getFrdateport() {
        return Validation.checkNull(frdateport);
    }

    public void setFrdateport(String frdateport) {
        this.frdateport = frdateport;
    }

    public String getTodateport() {
        return Validation.checkNull(todateport);
    }

    public void setTodateport(String todateport) {
        this.todateport = todateport;
    }
}
