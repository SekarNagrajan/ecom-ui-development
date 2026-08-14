/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.CustomerStatmentForm;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author priyadharshinim
 */
public class NewInvoiceAction extends org.apache.struts.action.Action {
    String success = "success";
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext context = servlet.getServletContext();
        HttpSession session = request.getSession();
        String strType = null;
        String fromDate = null;
        String toDate = null; 
        String option = null;
        Map<String, List> obj = null;
        try {
            if (session.getAttribute("STRIPE_CSRF_TOKEN") == null) {
                session.setAttribute("STRIPE_CSRF_TOKEN", UUID.randomUUID().toString());
            }
            if (session.getAttribute("NGENIUS_CSRF_TOKEN") == null) {
                session.setAttribute("NGENIUS_CSRF_TOKEN", UUID.randomUUID().toString());
            }
            String customerCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            System.out.println("[ECOM-WAR] NewInvoiceAction   new inv all values custcode.........." + customerCode);
            //String type = Validation.checkNull(request.getParameter("type"));
           // System.out.println("[ECOM-WAR] NewInvoiceAction   <<<customer stateemnt type <>" + type);
           String type="inv";
            CustomerStatmentForm custStmtForm = (CustomerStatmentForm) form;
          if (type.equalsIgnoreCase("inv")) { 
                    obj = null;
                    strType = Validation.checkNull(custStmtForm.getInvType());
                    fromDate = Validation.checkNull(custStmtForm.getInvFromDate());
                    toDate = Validation.checkNull(custStmtForm.getInvToDate());
                    option = Validation.checkNull(custStmtForm.getInvOption());
                    System.out.println("[ECOM-WAR] NewInvoiceAction   new inv all values.........." + strType + fromDate + toDate + option);
                    if (fromDate.length() > 0) {
                        fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
                    }
                    if (toDate.length() > 0) {
                        toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
                    }
                    obj = ecomPhase2Obj.getInvoiceDetails(strType, customerCode, fromDate, toDate, option);
                    session.setAttribute("CSINVOICESUMMARYLIST", obj);
                    
                    success = "invSuccess";
                    System.out.println("[ECOM-WAR] NewInvoiceAction   invvsucess-->"+success);
                }
        else {
                    success = "failure";
                }
                
    
        }
        catch (Exception e) {
            request.setAttribute("Error_report", "error");
            System.out.println("[ECOM-WAR] NewInvoiceAction   ==================>>>err" + e);
        }
        return mapping.findForward(success);
    }
}
