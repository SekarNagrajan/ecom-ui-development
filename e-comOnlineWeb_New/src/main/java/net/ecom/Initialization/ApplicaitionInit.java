/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.Initialization;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.common.utils.MaskPortUtil;

public class ApplicaitionInit implements ServletContextListener, HttpSessionListener {

    EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();

    @Override
    @SuppressWarnings("static-access")
    public void contextInitialized(ServletContextEvent event) {
        // Initialize MaskPortUtil with ServletContext
        MaskPortUtil.init(event.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            HttpSession session = se.getSession();
            if ("YES".equalsIgnoreCase((String) session.getAttribute("IS_VIA_LOGIN"))) {
                String loginname = (String) session.getAttribute("SESSION_LOGINNAME");
                if (loginname != null && loginname.length() > 0) {
                    ecomreg.updatelogoutdate(loginname);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
