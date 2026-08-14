/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SpecialPrivilege extends org.apache.struts.action.Action {

    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomRegistrationBeanRemote regobj = EJBLookUp.getRegistrationBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "specialprivilege";
        try {
            System.out.println("[ECOM-WAR] SpecialPrivilege   Inside SpecialPrivilege --- > ");
            HttpSession session = request.getSession();
            List SplPriviList = null;

            String Type = request.getParameter("Type");
            Type = Validation.checkNull(Type);
            String CustCode = request.getParameter("CustCode");
            CustCode = Validation.checkNull(CustCode);
            String webid = request.getParameter("webid").toString().trim();//Added by Gnanamoorthi.S
            System.out.println("[ECOM-WAR] SpecialPrivilege   webid ========> "+webid);
            String MenuId = request.getParameter("menuid");
            MenuId = Validation.checkNull(MenuId);
            if (Type.trim().length() > 0 && Type.equalsIgnoreCase("onLoad")) {
                SplPriviList = regobj.getRegMenuDetails();
            }
            if (Type.trim().length() > 0 && Type.equalsIgnoreCase("customer")) {
                SplPriviList = locobj.getCustMenuDetails(webid, "C");
            }
            if (Type.trim().length() > 0 && Type.equalsIgnoreCase("removeMenu")) {
                SplPriviList = locobj.removeCustMenu(webid, MenuId, "R");
            }
            if (Type.trim().length() > 0 && Type.equalsIgnoreCase("addMenu")) {
                SplPriviList = locobj.addCustMenu(webid, MenuId, "A");
            }
            if (!CustCode.equalsIgnoreCase("") && CustCode.trim().length() > 0) {
                session.setAttribute("custcode", CustCode);
            }
            session.setAttribute("SplPriviList", SplPriviList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM-WAR] SpecialPrivilege   Forward ---> "+SUCCESS);
        return mapping.findForward(SUCCESS);
    }
}
