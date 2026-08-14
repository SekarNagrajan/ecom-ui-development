/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomVGMBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class ESLVGMAction extends Action {

    private static final String SUCCESS = "success";
    EcomVGMBeanRemote vgmBean = EJBLookUp.getESLVGMBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            ServletContext context = servlet.getServletContext();
            HttpSession session = request.getSession();
            saveToken(request);
            session.setAttribute("IS_ACTIVEPAGE", "vgm");
            session.setAttribute("NEWDESIGNPRODUCT","vgm");
            session.setAttribute("systemid", session.getId());
            if (session != null) {
                //session.invalidate();
                //session = request.getSession();
                //System.out.println("[ECOM-WAR] ESLVGMAction >>session>>>"+session.getId());
            }
//            System.out.println("[ECOM-WAR] ESLVGMAction >>>>>entered in ESLVGM....");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
