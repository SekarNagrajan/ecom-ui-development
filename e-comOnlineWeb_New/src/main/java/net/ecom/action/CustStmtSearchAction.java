/*
 * To change this template, choose Tools | Templates
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
 * @author deepa.p
 */
//urlhardcode
public class CustStmtSearchAction extends org.apache.struts.action.Action {

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
            session.setAttribute("NEWDESIGNPRODUCT", "custstmt");
            String customerCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            String carrierCode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
            String type = Validation.checkNull(request.getParameter("type"));
//             System.out.println("[ECOM-WAR] CustStmtSearchAction     <<<customre stateemnt type<>" + type);
            String CustStatFromInv = (String) context.getAttribute(GeneralConf.CSFROMINV);
//             System.out.println("[ECOM-WAR] CustStmtSearchAction     CSFROMINV value" + CustStatFromInv);
             System.out.println("[ECOM-WAR] CustStmtSearchAction     CSFROMINV value: " + CustStatFromInv + ", customer statement type: " + type);
            String custstmtsp = (String) context.getAttribute(GeneralConf.USE_CUSTOMETSTATEMENT_SP);
            CustomerStatmentForm custStmtForm = (CustomerStatmentForm) form;
            if ("Y".equalsIgnoreCase(CustStatFromInv)) {
                if (type.equalsIgnoreCase("acc")) {
                    strType = Validation.checkNull(custStmtForm.getAccType());
                    fromDate = Validation.checkNull(custStmtForm.getAccFromDate());
                    toDate = Validation.checkNull(custStmtForm.getAccToDate());
                    option = Validation.checkNull(custStmtForm.getAccOption());

                    // System.out.println("[ECOM-WAR] CustStmtSearchAction     all values.........." + strType + fromDate + toDate + option);
                    if (fromDate.length() > 0) {
                        fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
                    }
                    if (toDate.length() > 0) {
                        toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
                    } 
                    if ("Y".equalsIgnoreCase(custstmtsp)) {
                        obj = ecomPhase2Obj.getAccountSummaryfromcuststmtsp(strType, customerCode, fromDate, toDate, option, carrierCode);
                    } else {
                        obj = ecomPhase2Obj.getAccountSummary(strType, customerCode, fromDate, toDate, option);
                    }
                    success = "accSuccess";
                    session.setAttribute("ACCOUNTSUMMARYLIST", obj);
                } else if (type.equalsIgnoreCase("inv")) { 
                    obj = null;
                    strType = Validation.checkNull(custStmtForm.getInvType());
                    fromDate = Validation.checkNull(custStmtForm.getInvFromDate());
                    toDate = Validation.checkNull(custStmtForm.getInvToDate());
                    option = Validation.checkNull(custStmtForm.getInvOption());
                    // System.out.println("[ECOM-WAR] CustStmtSearchAction     all values.........." + strType + fromDate + toDate + option);
                    if (fromDate.length() > 0) {
                        fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
                    }
                    if (toDate.length() > 0) {
                        toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
                    }
                    if ("Y".equalsIgnoreCase(custstmtsp)) {
                        obj = ecomPhase2Obj.getInvoiceDetailsfromcuststmtsp(strType, customerCode, fromDate, toDate, option, carrierCode);
                    } else {
                        obj = ecomPhase2Obj.getInvoiceDetails(strType, customerCode, fromDate, toDate, option);
                    }
                    session.setAttribute("CSINVOICESUMMARYLIST", obj);
                    success = "invSuccess";
                } else if (type.equalsIgnoreCase("pay")) {
                    obj = null;
                    strType = Validation.checkNull(custStmtForm.getPayType());
                    fromDate = Validation.checkNull(custStmtForm.getPayFromDate());
                    toDate = Validation.checkNull(custStmtForm.getPayToDate());
                    option = Validation.checkNull(custStmtForm.getPayOption());
                    // System.out.println("[ECOM-WAR] CustStmtSearchAction     all values.........." + strType + fromDate + toDate + option);
                    if (fromDate.length() > 0) {
                        fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
                    }
                    if (toDate.length() > 0) {
                        toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
                    }
                    if ("Y".equalsIgnoreCase(custstmtsp)) {
                        obj = ecomPhase2Obj.getPaymentDetialsfromcuststmtsp(strType, customerCode, fromDate, toDate, option, carrierCode);
                    } else {
                        obj = ecomPhase2Obj.getPaymentDetials(strType, customerCode, fromDate, toDate, option);
                    }
                    session.setAttribute("PAYMENTSUMMARYLIST", obj);
                    success = "paySuccess";
                } else if (type.equalsIgnoreCase("out")) {
                    obj = null;
                    strType = Validation.checkNull(custStmtForm.getOutType());
                    fromDate = Validation.checkNull(custStmtForm.getOutFromDate());
                    toDate = Validation.checkNull(custStmtForm.getOutToDate());
                    option = Validation.checkNull(custStmtForm.getOutOption());
                    // System.out.println("[ECOM-WAR] CustStmtSearchAction     all values.........." + strType + fromDate + toDate + option);
                    if (fromDate.length() > 0) {
                        fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
                    }
                    if (toDate.length() > 0) {
                        toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
                    }
                    if ("Y".equalsIgnoreCase(custstmtsp)) {
                        obj = ecomPhase2Obj.getOutstandingDetailsfromcuststmtsp(strType, customerCode, fromDate, toDate, option, carrierCode);
                    } else {
                        obj = ecomPhase2Obj.getOutstandingDetails(strType, customerCode, fromDate, toDate, option);
                    }
                    session.setAttribute("OUTSTANDINGSUMMARYLIST", obj);
                    success = "outSuccess";
                } else {
                    success = "failure";
                }
            } else {
                if (type.equalsIgnoreCase("acc")) {
                    fromDate = Validation.checkNull(custStmtForm.getAccFromDate());
                    if (fromDate.length() > 0) {
                        fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
                    }
                    obj = ecomPhase2Obj.getNewAccountSummaryFromCCR(fromDate,customerCode,carrierCode);
                    success = "accSuccess";
                    session.setAttribute("ACCOUNTSUMMARYLIST", obj);
                }
                else {
                    success = "failure";
                }
                
            }

        } catch (Exception e) {
            request.setAttribute("Error_report", "error");
             System.out.println("[ECOM-WAR] CustStmtSearchAction     ==================>>>" + e);
        }
        return mapping.findForward(success);
    }
}
