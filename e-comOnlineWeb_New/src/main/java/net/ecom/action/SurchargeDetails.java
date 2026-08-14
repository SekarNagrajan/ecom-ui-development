/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.formbeans.SurchargeForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.SurchargeSummaryVO;

public class SurchargeDetails extends org.apache.struts.action.Action {

    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext context = servlet.getServletContext();
        String porturl = null;
        try {

            SurchargeForm surchargeForm = (SurchargeForm) form;
            String pol = surchargeForm.getPolname();
            String pod = surchargeForm.getPodname();
            String eqptype = surchargeForm.getEqtype();
            String fromDate = surchargeForm.getFromDate();
            String toDate = surchargeForm.getToDate();
            request.setAttribute("surchargeeqptype", eqptype);
            request.setAttribute("surchargepol", pol);
            request.setAttribute("surchargepod", pod);
            fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            // toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
            String surchargeDetails = (String) context.getAttribute(GeneralConf.surcharge_details);
            boolean isMaskPort = MaskPort.isMaskPorts(pol + "," + pod, context);
            if (isMaskPort) {
                if ("N".equalsIgnoreCase(surchargeDetails)) {
                    List<SurchargeSummaryVO> obj = ecomPhase2Obj.getSurchargeSummaryNew(pol, pod, eqptype, fromDate);
                    System.out.println("[ECOM-WAR] SurchargeDetails    >>obj>>" + obj);
                    request.setAttribute("SURCHARGE_LIST", obj);
                } else {
                    List<SurchargeSummaryVO> obj = ecomPhase2Obj.getSurchargeSummary(pol, pod, eqptype, fromDate);
                    System.out.println("[ECOM-WAR] SurchargeDetails    >>obj>>" + obj);
                    request.setAttribute("SURCHARGE_LIST", obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward("success");
    }
}
