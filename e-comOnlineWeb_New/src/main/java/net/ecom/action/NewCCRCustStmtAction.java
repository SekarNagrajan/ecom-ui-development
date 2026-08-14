/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.NewCCRCustomerStatementForm;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author gobinath.t
 */
public class NewCCRCustStmtAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();
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
        try {
                ServletContext context = servlet.getServletContext();
        HttpSession session = request.getSession();
            NewCCRCustomerStatementForm custstmtform = (NewCCRCustomerStatementForm) form;
            String ondate = custstmtform.getOndate();
            if(ondate==null || ondate.length()==0){
               ondate =  DateConversion_New.getCurrentDateDbFormat();
            }
            String customerCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            String carrierCode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
           Map custstmtMap=  ecomPhase2Obj.getCreditCustomerStatementDetails(ondate, customerCode, carrierCode);
           request.setAttribute("NewCuststmtResultMap", custstmtMap);
           request.setAttribute("requestedOnDate",  custstmtform.getOndate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
