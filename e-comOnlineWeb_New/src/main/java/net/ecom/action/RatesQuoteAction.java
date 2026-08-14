/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.RatesQuoteForm;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class RatesQuoteAction extends org.apache.struts.action.Action {

    private static String SUCCESS = "success";
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            DateFormat dateFormat = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            HttpSession session = request.getSession();
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            ServletContext contexts = servlet.getServletContext();
            System.out.println("[ECOM-WAR] RatesQuoteAction  <<rate Quote action>>>");
            RatesQuoteForm rateConForm = (RatesQuoteForm) form;
            String shipmentDate = rateConForm.getShipmentDate();
            if (shipmentDate != null && shipmentDate.length() > 0) {
                shipmentDate = DateConversion_New.convertDefaultDBFormatwithoutTime(rateConForm.getShipmentDate());
            } else {
                shipmentDate = dateFormat.format(new Date());
                shipmentDate = DateConversion_New.convertDefaultDBFormatwithoutTime(shipmentDate);
            }
            System.out.println("[ECOM-WAR] RatesQuoteAction  >>shipmentDate>>>" + shipmentDate);
            System.out.println("[ECOM-WAR] RatesQuoteAction  >>>" + rateConForm.getEqpType());
            //userCode ="013041";
            Map quotemap = ecomPhase2Obj.getRateContractQuote(shipmentDate, userCode, rateConForm.getPol(), rateConForm.getPod(), rateConForm.getEqpType(), rateConForm.getCommodity());
            request.setAttribute("RATEQUOTENEWMAP", quotemap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
