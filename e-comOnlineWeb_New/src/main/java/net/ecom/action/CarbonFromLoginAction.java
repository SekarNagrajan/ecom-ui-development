package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Entry from public login widget (login_F.jsp) to the carbon calculator page,
 * mirroring {@link SchedulesAction} for session/menu flags and request carry-over.
 */
public class CarbonFromLoginAction extends org.apache.struts.action.Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "carbonCalculator";
        try {
            HttpSession session = request.getSession(false);
            if ("logincarbon".equalsIgnoreCase(request.getParameter("carbontype"))) {
                request.setAttribute("carbontype", "logincarbon");
                request.setAttribute("logincarbonpol", request.getParameter("cpol"));
                request.setAttribute("logincarbonpod", request.getParameter("cpod"));
                request.setAttribute("logincarbonqty", request.getParameter("cqty"));
                request.setAttribute("logincarbonweight", request.getParameter("cweight"));
            }
            String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
            strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
            String strMenu = "YES";
            if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                strMenu = "NO";
            } else {
                session.setAttribute("systemid", session.getId());
            }
            session.setAttribute("IS_MENU_LOCK", strMenu);
            session.setAttribute("IS_ACTIVEPAGE", "CarbonCalculator");
            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(forward);
    }
}
