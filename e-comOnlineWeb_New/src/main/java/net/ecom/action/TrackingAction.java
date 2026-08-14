/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.captcha.VerifyRecaptcha;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author gobinath.t
 */
public class TrackingAction extends org.apache.struts.action.Action {

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
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "tracking";
        try {
            HttpSession session = request.getSession(false);

            String secretKey = Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.CaptchaSecretKey));
            String toDisplayGgleCaptcha = (String) request.getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
            session.setAttribute("systemid", session.getId());  // session Id
            System.out.println("[ECOM-WAR] TrackingAction >>>request.getParameter('tracktype')>>" + request.getParameter("tracktype"));
            if ("logintracking".equalsIgnoreCase(request.getParameter("tracktype"))) {
                boolean verify = VerifyRecaptcha.verify((String) request.getParameter("g-recaptcha-response"), secretKey);
                if ((!"Y".equalsIgnoreCase(toDisplayGgleCaptcha) || verify)) {
                    session.setAttribute("systemid", session.getId());
                    request.setAttribute("tracktype", "logintracking");
                    request.setAttribute("logintrackrefno", request.getParameter("logintracno"));
                    String refNoType = ecomCntrlObj.checkBLBookingNo(request.getParameter("logintracno"));
                    System.out.println("[ECOM-WAR] TrackingAction >>refNoType>>>" + refNoType);
                    request.setAttribute("refNoType", refNoType);
                    System.out.println("[ECOM-WAR] TrackingAction >>>request.getParameter('logintracno')>>" + request.getParameter("logintracno"));
                }
            }
            Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
            session.setAttribute("SESSION_AUTHENTICATION", authObj);
            session.setAttribute("IS_ACTIVEPAGE", "Tracking");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(forward);
    }
}
