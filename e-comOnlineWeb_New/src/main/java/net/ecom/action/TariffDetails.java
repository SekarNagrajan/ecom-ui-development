package net.ecom.action;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.formbeans.TariffForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.TariffSummaryVO;

public class TariffDetails extends org.apache.struts.action.Action {

    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        try {
            ServletContext context = servlet.getServletContext();
            String carriercode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
            TariffForm tariffForm = (TariffForm) form;
            String pol = tariffForm.getPolname();
            String pod = tariffForm.getPodname();
            String eqp = tariffForm.getEqtype();
            String fromDate = tariffForm.getFromDate();
            String cmttyCode = tariffForm.getTarrifCommodity();
            request.setAttribute("eqptype", eqp);
            request.setAttribute("pol", pol);
            request.setAttribute("pod", pod);
            request.setAttribute("cmttyCode", cmttyCode);
            fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            //System.out.println("[ECOM-WAR] TariffDetails     ===>" + porturl);
            String surchargeDetails = (String) context.getAttribute(GeneralConf.surcharge_details);
            String showExternalRateTotalAmount = (String) context.getAttribute(GeneralConf.SHOW_EXTERNALRATE_TOTAL_AMOUNT);
            String checkRouting = (String) context.getAttribute(GeneralConf.TARIFFSUMMARY_CHECK_ROUTING);
            boolean isMaskPort = MaskPort.isMaskPorts(pol + "," + pod, context);
            if (isMaskPort) {
                List<TariffSummaryVO> obj = ecomPhase2Obj.getTarriffSummary(pol, pod, cmttyCode, eqp, fromDate, carriercode, surchargeDetails, showExternalRateTotalAmount, checkRouting);
                System.out.println("[ECOM-WAR] TariffDetails     ??obj??");
                request.setAttribute("TARIFF_LIST", obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping.findForward(
                "success");
    }
}
