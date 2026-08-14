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
 * @author Chandrahasan
 */
public class ForgottenFormBean extends org.apache.struts.action.ActionForm {

    private String name;
    private int number;
    private String strLoginName;
    private String strFirstName;
    private String strEmailId;
    private String strType;
    private String form_sumbit = "";
    private String strType1 = null;

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param i
     */
    public void setNumber(int i) {
        number = i;
    }

    /**
     *
     */
    public ForgottenFormBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getName() == null || getName().length() < 1) {
            errors.add("name", new ActionMessage("error.name.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        return errors;
    }

    /**
     * @return the strLoginName
     */
    public String getStrLoginName() {
        return strLoginName;
    }

    /**
     * @param strLoginName the strLoginName to set
     */
    public void setStrLoginName(String strLoginName) {
        this.strLoginName = strLoginName;
    }

    /**
     * @return the strFirstName
     */
    public String getStrFirstName() {
        return strFirstName;
    }

    /**
     * @param strFirstName the strFirstName to set
     */
    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    /**
     * @return the strEmailId
     */
    public String getStrEmailId() {
        return strEmailId;
    }

    /**
     * @param strEmailId the strEmailId to set
     */
    public void setStrEmailId(String strEmailId) {
        this.strEmailId = strEmailId;
    }

    /**
     * @return the strType
     */
    public String getStrType() {
        return strType;
    }

    /**
     * @param strType the strType to set
     */
    public void setStrType(String strType) {
        this.strType = strType;
    }

    /**
     * @return the form_sumbit
     */
    public String getForm_sumbit() {
        return form_sumbit;
    }

    /**
     * @param form_sumbit the form_sumbit to set
     */
    public void setForm_sumbit(String form_sumbit) {
        this.form_sumbit = form_sumbit;
    }

    public String getStrType1() {
        return strType1;
    }

    public void setStrType1(String strType1) {
        this.strType1 = strType1;
    }
}
