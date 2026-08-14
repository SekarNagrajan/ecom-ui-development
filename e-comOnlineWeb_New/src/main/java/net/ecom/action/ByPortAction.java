/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author gobinath.t
 */
public class ByPortAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "byport";
        try {
            HttpSession session = request.getSession(false);
            if ("loginschedbyport".equalsIgnoreCase(request.getParameter("porttype"))) {
                request.setAttribute("porttype", request.getParameter("porttype"));
                request.setAttribute("loginbyport", request.getParameter("port"));
                request.setAttribute("loginportfdate", request.getParameter("portscdshipmentdate"));
                request.setAttribute("loginporttdate", request.getParameter("porttodateq"));
//  session.setAttribute("SESSION_LOGIN_TYPE", "U");
            }
            String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
            strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
            String strMenu = "YES";
            if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                strMenu = "NO";

            } else {
                session.setAttribute("systemid", session.getId());  // session Id
            }
             System.out.println("[ECOM-WAR] ByPortAction  strIsViaLogin IN ACCESS LOGIN" + strIsViaLogin);
            session.setAttribute("IS_MENU_LOCK", strMenu);
            session.setAttribute("IS_ACTIVEPAGE", "Schedule");
            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(forward);
    }
}
