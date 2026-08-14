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
public class ByVesselAction extends org.apache.struts.action.Action {

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

        String forward = "byvessel";
        try {
            HttpSession session = request.getSession(false);
            if ("loginbyvesselsearch".equalsIgnoreCase(request.getParameter("vesseltype"))) {
                request.setAttribute("vesseltype", request.getParameter("vesseltype"));
                request.setAttribute("loginbyvessel", request.getParameter("vessel"));
                request.setAttribute("loginvesselfdate", request.getParameter("vesselscdshipmentdate"));
                request.setAttribute("loginvesseltdate", request.getParameter("vessltodateq"));
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
             System.out.println("[ECOM-WAR] ByVesselAction   strIsViaLogin IN ACCESS LOGIN" + strIsViaLogin);
            session.setAttribute("IS_MENU_LOCK", strMenu);
            session.setAttribute("IS_ACTIVEPAGE", "Schedule");
            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(forward);
    }
}
