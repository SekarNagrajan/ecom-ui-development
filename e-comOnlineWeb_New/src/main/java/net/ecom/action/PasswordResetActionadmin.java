/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.Validation;
import net.ecom.formbeans.PasswordResetForm;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sasikumar.r
 */
public class PasswordResetActionadmin extends org.apache.struts.action.Action {

    EcomRegistrationBeanRemote ecomLookUpObj = EJBLookUp.getRegistrationBeanLookUp();

    @Override
   public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";

        PasswordResetForm sdm = new PasswordResetForm();
        //System.out.println("[ECOM-WAR] PasswordResetActionadmin     Inside Action Reset now ==>");
        HttpSession session = request.getSession();
        String valid = Validation.checkNull((String) session.getAttribute("adminpwdreset"));
        //System.out.println("[ECOM-WAR] PasswordResetActionadmin     ******1********====>gnanamoorthi");
        sdm = (PasswordResetForm) form;
        String returnvalue = "";

        String loginName = null;
        loginName = Validation.checkNull((String) session.getAttribute("SESSION_USERNAME"));
        //System.out.println("[ECOM-WAR] PasswordResetActionadmin     username..........."+loginName+sdm.getOldpassword()+sdm.getNewpassword());
        String password = sdm.getOldpassword_admin();
        password = Validation.checkNull(password);
        String npassword = sdm.getNewpassword_admin();
        npassword = Validation.checkNull(npassword);
        String result = "fail";
        if (valid.equalsIgnoreCase("send")) {
            session.setAttribute("adminpwdreset", "success");
            result = ecomLookUpObj.resetAdminPassword(loginName, npassword, password);
            if ("fail".equalsIgnoreCase(result)) {
                session.setAttribute("ErrorMsg1", "invalid");
                SUCCESS = "failure";
            }else{
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("MENULIST",null);
                session.setAttribute("SESSION_USERNAME",null);
                session.setAttribute("NEWDESIGNAGENCY","NO");
            }
            System.out.println("[ECOM-WAR] PasswordResetActionadmin     result........."+result);
        }else{
            SUCCESS = "homepage";
        } 
        System.out.println("[ECOM-WAR] PasswordResetActionadmin     ..........failues..........."+SUCCESS);
        return mapping.findForward(SUCCESS);
    }
}