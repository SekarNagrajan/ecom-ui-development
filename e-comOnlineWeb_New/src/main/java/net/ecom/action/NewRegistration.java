/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class NewRegistration extends org.apache.struts.action.Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        try {
            String strType = Validation.checkNull(request.getParameter("type"));
            ServletContext context = servlet.getServletContext();
            EcomRegistrationBeanRemote ecomCntrlObj = EJBLookUp.getRegistrationBeanLookUp();
            EComCntrlBeanRemote ecomobj= EJBLookUp.getEcomCntrlBeanLookUp();
            HttpSession session = request.getSession();
            ArrayList CityObj = new ArrayList();
            ArrayList locObj = new ArrayList();
            ArrayList stateObj = new ArrayList();
            locObj.add(new LabelValueBean("N/A", ""));
            CityObj.add(new LabelValueBean("N/A", ""));
            stateObj.add(new LabelValueBean("N/A", ""));
            @SuppressWarnings("static-access")
            String term = null;
            Hashtable authObj = null;
            if ("terms".equalsIgnoreCase(strType)) {
                term = Validation.checkNull((String) context.getAttribute("Terms_and_Conditions"));
                SUCCESS = "terms";
            } else {
                authObj = (Hashtable) ecomobj.getAuthenticationMail();
                SUCCESS = "newRegistration";
            }
            request.setAttribute("REG_CITYLIST", CityObj);
            request.setAttribute("REG_STATELIST", stateObj);
            request.setAttribute("REG_LOCATIONLIST", locObj);
            request.setAttribute("TERMS", term);
            session.setAttribute("SESSION_AUTHENTICATION", authObj);
            session.setAttribute("NEWDESIGNPRODUCT", "registration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
