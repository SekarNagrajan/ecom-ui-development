/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class MailAlertSaveAction extends org.apache.struts.action.Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
        String loginUser = null;
        try {
            HttpSession session = request.getSession();
            List<MailAlertVO> listAlertVO = null;
            MailAlertVO alertVO = null;
            System.out.println("[ECOM-WAR] MailAlertSaveAction    >>entered in save aciont...");
            loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
            String hidMailAlertCode[] = request.getParameterValues("hidmailalertcode");
            String hidmailalertvalue[] = request.getParameterValues("hidmailalertvalue");
            String mailalreadyexist[] = request.getParameterValues("mailalreadyexist");
            String selectedLang = (String) session.getAttribute("SelectedLanguage");
            listAlertVO = new ArrayList<MailAlertVO>();
            if (hidMailAlertCode != null && hidMailAlertCode.length > 0) {
                for (int i = 0; i < hidMailAlertCode.length; i++) {
                    alertVO = new MailAlertVO();
                    alertVO.setActivityCode(hidMailAlertCode[i]);
                    alertVO.setMailAlert(hidmailalertvalue[i]);
                    alertVO.setAlreadyExist(mailalreadyexist[i]);
                    alertVO.setLoginName(loginUser);
                    alertVO.setLangType(selectedLang);
                    System.out.println("[ECOM-WAR] MailAlertSaveAction >>hidmailalertvalue>" + hidmailalertvalue[i]+"   >>hidMailAlert>" + hidMailAlertCode[i]);
                    listAlertVO.add(alertVO);
                }
            }
            String result = ecomCntrlObj.saveMailAlertActivities(listAlertVO);
            if (result != null && !result.equalsIgnoreCase("not updated")) {
                System.out.println("[ECOM-WAR] MailAlertSaveAction    >>updated>>");
                request.setAttribute("SUCCESSMAILALERTRESULT", result);
                Map<String, String> mailAlertMap = new HashMap<String, String>();
                if (listAlertVO != null && listAlertVO.size() > 0) {
                    for (int i = 0; i < listAlertVO.size(); i++) {
                        alertVO = listAlertVO.get(i);
                        mailAlertMap.put(alertVO.getActivityCode(), alertVO.getMailAlert());
                    }
                }
                session.setAttribute("USERMAILALERTSESSIONMAP", mailAlertMap);
                System.out.println("[ECOM-WAR] MailAlertSaveAction    >>map in save function mailAlertMap>>" + mailAlertMap);
                listAlertVO = ecomCntrlObj.getMailAlertActivities(loginUser);
                request.setAttribute("MAILALERTVIEWDETAILS", listAlertVO);
            } else {
                request.setAttribute("SUCCESSMAILALERTRESULT", "Failure! Your alert changes not updated.");
            }
        } catch (Exception e) {
            request.setAttribute("SUCCESSMAILALERTRESULT", "Failure! Your alert changes not updated.");
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
