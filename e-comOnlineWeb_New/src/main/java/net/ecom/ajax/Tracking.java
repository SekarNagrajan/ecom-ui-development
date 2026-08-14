/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.util.Hashtable;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.MailFormatVO;

/**
 *
 * @author subashini
 */
public class Tracking {

    public String sendMail(String Message, String id, String agencymail, String client_code) {
        try {
            String initiatorID = "ECOM-TRA";
            net.ecom.common.Mailer mail = new net.ecom.common.Mailer();
            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            MailFormatVO mailVo = ecomCntrlObj.getMailContent("tracking", "view", "C");
            //  mail.setFrom(mailVo.getStrFromAddr());
            mail.setTo(id);
            mail.setSubject(mailVo.getStrSubject());
            mail.setMessage(Message);
            //1  mail.sendMail(authenticatedObj,agencymail);
            ecomCntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sent";
    }
}
