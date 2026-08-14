/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.formbeans;

/**
 *
 * @author mahalakshmi.r
 */
public class SpecialPrivilegeForm extends org.apache.struts.action.ActionForm {

    private String selectuser;
    /**
     *
     */
    public SpecialPrivilegeForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the selectuser
     */
    public String getSelectuser() {
        return selectuser;
    }

    /**
     * @param selectuser the selectuser to set
     */
    public void setSelectuser(String selectuser) {
        this.selectuser = selectuser;
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
//    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
//        ActionErrors errors = new ActionErrors();
//        if (getName() == null || getName().length() < 1) {
//            errors.add("name", new ActionMessage("error.name.required"));
//            // TODO: add 'error.name.required' key to your resources
//        }
//        return errors;
//    }

}
