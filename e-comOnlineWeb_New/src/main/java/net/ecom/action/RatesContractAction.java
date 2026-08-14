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
import net.ecom.common.MaskPort;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.formbeans.RatesContractForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.RatesContractsVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class RatesContractAction extends org.apache.struts.action.Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        try {

            DateFormat dateFormat = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            HttpSession session = request.getSession();
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            ServletContext contexts = servlet.getServletContext();
            System.out.println("[ECOM-WAR] RatesContractAction  <<rate contract action>>>");
            RatesContractForm rateConForm = (RatesContractForm) form;
            boolean isMaskPort = MaskPort.isMaskPorts(rateConForm.getPol() + "," + rateConForm.getPod(), contexts);
            if (Validation.checkNull(rateConForm.getPol()).length() == 0 && Validation.checkNull(rateConForm.getPol()).length() == 0) {
                isMaskPort = true;
            }
            if (isMaskPort) {
                String shipmentDate = rateConForm.getShipmentDate();
                if (shipmentDate != null && shipmentDate.length() > 0) {
                    shipmentDate = DateConversion_New.convertDefaultDBFormatwithoutTime(rateConForm.getShipmentDate());
                } else {
                    shipmentDate = dateFormat.format(new Date());
                    shipmentDate = DateConversion_New.convertDefaultDBFormatwithoutTime(shipmentDate);
                }
                System.out.println("[ECOM-WAR] RatesContractAction  >>shipmentDate>>>" + shipmentDate);
                System.out.println("[ECOM-WAR] RatesContractAction  >>>" + rateConForm.getEqpType());
                String pol = rateConForm.getPol();
                String pod = rateConForm.getPod();
                pol = MaskPortUtil.swapmaskporttoactual(pol);
                pod = MaskPortUtil.swapmaskporttoactual(pod);
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.EnableNewContract))) {
                    //userCode ="013041";
                    Map Contractmap = ecomPhase2Obj.getRateContract(shipmentDate, userCode, pol, pod, rateConForm.getEqpType(), rateConForm.getCommodity());
                    request.setAttribute("RATECONTRACTNEWMAP", Contractmap);
                    SUCCESS = "newcontract";
                } else {
                    List<RatesContractsVO> contractList = ecomCntrlObj.getAvailableRatesContract(userCode, pol, pod, rateConForm.getEqpType(), rateConForm.getCommodity(), shipmentDate);
                    request.setAttribute("RATECONTRACTLIST", contractList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
