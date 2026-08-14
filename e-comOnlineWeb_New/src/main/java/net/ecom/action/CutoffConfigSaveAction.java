/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.formbeans.CutoffConfigForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.CutoffConfigVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class CutoffConfigSaveAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    EComCntrlBeanRemote beanObj = EJBLookUp.getEcomCntrlBeanLookUp();

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
//         System.out.println("[ECOM-WAR] CutoffConfigSaveAction  >>entered in save");
        CutoffConfigVO cutOffVO = null;
        String agencyUserId = null;
        HttpSession session = request.getSession();
        try {
            agencyUserId = (String) session.getAttribute("SESSION_AGENCY_USERID");
            CutoffConfigForm cutOffForm = (CutoffConfigForm) form;
//             System.out.println("[ECOM-WAR] CutoffConfigSaveAction  >>port>" + cutOffForm.getPort());
//             System.out.println("[ECOM-WAR] CutoffConfigSaveAction  >>>terminal>>" + cutOffForm.getTerminal());
//             System.out.println("[ECOM-WAR] CutoffConfigSaveAction  >>savetype>>" + cutOffForm.getSaveType());
             System.out.println("[ECOM-WAR] CutoffConfigSaveAction  >>port> " + cutOffForm.getPort() + ", >>>terminal> " + cutOffForm.getTerminal() + ", >>savetype> " + cutOffForm.getSaveType());

            cutOffVO = new CutoffConfigVO();
            cutOffVO.setPort(cutOffForm.getPort().split("-")[0].trim());
            cutOffVO.setTerminal(cutOffForm.getTerminal());
            cutOffVO.setCfsGateClosing(cutOffForm.getCfsClosing());
            cutOffVO.setVgmClosing(cutOffForm.getVgmClosing());
            cutOffVO.setDocClosing(cutOffForm.getDocumentClosing());
            cutOffVO.setFullContClosing(cutOffForm.getFullCntrGateClosing());
            cutOffVO.setExcludeweekend(request.getParameter("excludeWeekEnds"));
            cutOffVO.setEdiDecClosing(cutOffForm.getEdiDecClosing());
            cutOffVO.setSaveType(cutOffForm.getSaveType());
            cutOffVO.setCruser(agencyUserId);
            if (request.getParameter("excludeWeekEnds") == null) {
                cutOffVO.setExcludeweekend("N");
            }

             System.out.println("[ECOM-WAR] CutoffConfigSaveAction  ******" + request.getParameter("excludeWeekEnds"));
            int i = beanObj.saveCutoffConfigDetails(cutOffVO);
            if (i > 0) {
                cutOffForm.setPort("");
                cutOffForm.setTerminal("");
                cutOffForm.setCfsClosing("");
                cutOffForm.setVgmClosing("");
                cutOffForm.setDocumentClosing("");
                cutOffForm.setFullCntrGateClosing("");
                cutOffForm.setEdiDecClosing("");
                cutOffForm.setSaveType("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
