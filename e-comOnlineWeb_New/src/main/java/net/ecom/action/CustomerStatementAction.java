/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class CustomerStatementAction extends org.apache.struts.action.Action {

    String success = "success";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//        System.out.println("[ECOM-WAR] CustomerStatementAction this is customer statement view............");
        HttpSession session = request.getSession();
        try {
            ServletContext context = servlet.getServletContext();
            session.setAttribute("IS_ACTIVEPAGE", "Customer Statement");
            session.setAttribute("NEWDESIGNPRODUCT", "custstmt");
            session.setAttribute("IS_MENU_LOCK", "NO");
//            System.out.println("[ECOM-WAR] CustomerStatementAction >>>>");
        } catch (Exception e) {
            request.setAttribute("Error_report", "error");
            System.out.println("[ECOM-WAR] CustomerStatementAction ==================>>>" + e);
        }
        return mapping.findForward("success");
    }
}
