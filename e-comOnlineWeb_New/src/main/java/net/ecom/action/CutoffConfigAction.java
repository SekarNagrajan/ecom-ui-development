/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.CutoffConfigVO;

/**
 *
 * @author deepa.p
 */
public class CutoffConfigAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    EComCntrlBeanRemote beanObj = EJBLookUp.getEcomCntrlBeanLookUp();

    /**
     * This is the action called from the Struts framework.
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
        List<CutoffConfigVO> cutOffList=null;
//         System.out.println("[ECOM-WAR] CustomerStatementAction >>>cutoff configuration");
        try {
            cutOffList=beanObj.getCutOffList();
            request.setAttribute("CUTOFFLIST", cutOffList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
