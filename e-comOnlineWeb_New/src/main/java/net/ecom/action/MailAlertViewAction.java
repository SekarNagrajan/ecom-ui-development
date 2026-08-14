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
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.MailAlertVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class MailAlertViewAction extends org.apache.struts.action.Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "mailalert";
        String loginUser=null;
        EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
        try {
            HttpSession session = request.getSession();
            System.out.println("[ECOM-WAR] MailAlertViewAction >>entered in mail alert view?>>>");
            loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
            List<MailAlertVO> listAlertVO =ecomCntrlObj.getMailAlertActivities(loginUser);
            request.setAttribute("MAILALERTVIEWDETAILS",listAlertVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
