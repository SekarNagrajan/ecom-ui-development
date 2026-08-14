/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.Currencyformat;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.Validation;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.RatesForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.SurchargeSummaryVO;
import net.ecom.phase2.service.vo.TariffSummaryVO;
import net.ecom.vo.RateVo;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author gobinath.t
 */
public class RatesAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            ServletContext context = servlet.getServletContext();
            RatesForm rateform = (RatesForm) form;
            RateVo ratevo = null;
            List<RateVo> rateList = null;
            String carriercode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
            HttpSession session = request.getSession();
            String origin = rateform.getPolcode();
            String delivery = rateform.getPodcode();
            String shipmentdate = rateform.getShipmentdate();
            String commodity = rateform.getCommodity();
            String eqptype = rateform.getEqptype();
            int quantity = rateform.getQuantity();
            //   pol = Validation.checkNull(pol != null ? pol.split("-")[0] : pol);
            //   pod = Validation.checkNull(pod != null ? pod.split("-")[0] : pod);
            //   commodity = Validation.checkNull(commodity != null ? commodity.split("-")[0] : commodity);
            System.out.println("[ECOM-WAR] RatesAction pol " + origin + 
                   " pod " + delivery + 
                   " shipmentdate " + shipmentdate + 
                   " commodity " + commodity + 
                   " eqptype " + eqptype + 
                   " quantity " + quantity);
            origin = MaskPortUtil.swapmaskporttoactual(origin);
            delivery = MaskPortUtil.swapmaskporttoactual(delivery);
            String surchargeDetails = (String) context.getAttribute(GeneralConf.surcharge_details);
            String showExternalRateTotalAmount = (String) context.getAttribute(GeneralConf.SHOW_EXTERNALRATE_TOTAL_AMOUNT);
            String checkRouting = (String) context.getAttribute(GeneralConf.TARIFFSUMMARY_CHECK_ROUTING);
            shipmentdate = DateConversion_New.convertDefaultDBFormatwithoutTime(shipmentdate);
            List<TariffSummaryVO> tariffObj = null;
            List<SurchargeSummaryVO> surchargeObj = null;
            boolean isMaskPort = MaskPort.isMaskPorts(origin + "," + delivery, context);
            if (isMaskPort) {

                tariffObj = ecomPhase2Obj.getTarriffSummary(origin, delivery, commodity, eqptype, shipmentdate, carriercode, surchargeDetails, showExternalRateTotalAmount, checkRouting);


                System.out.println("[ECOM-WAR] RatesAction surchargeDetails==>  >>> " + surchargeDetails);

                if ("N".equalsIgnoreCase(surchargeDetails)) {
                    //surchargeObj = ecomPhase2Obj.getSurchargeSummaryNew(origin, delivery, eqptype, shipmentdate);
                } else {
                    surchargeObj = ecomPhase2Obj.getSurchargeSummary(origin, delivery, eqptype, shipmentdate);
                }
            }
            rateList = new ArrayList<RateVo>();

            float totalamount = 0.0f;  
            float totalExternalRate = 0.0f;
            DecimalFormat formatter = new DecimalFormat("0.00");
            String subToChargesAmt = null;
            if (surchargeObj != null && surchargeObj.size() > 0) {
                Double subToCharges = surchargeObj.stream().mapToDouble(f -> Double.parseDouble(f.getAmount().trim())).sum();
                subToChargesAmt = String.valueOf(subToCharges);
            }
            if (tariffObj != null && tariffObj.size() > 0) {
                for (int i = 0; i < tariffObj.size(); i++) {
                    TariffSummaryVO tariffvo = tariffObj.get(i);
                    if ("N".equalsIgnoreCase(surchargeDetails) || Validation.checkNull(commodity).equalsIgnoreCase(Validation.checkNull(tariffvo.getCommoditycode()))) {
                        ratevo = new RateVo();
                        ratevo.setLoadport(tariffvo.getLoadport());
                        ratevo.setDischport(tariffvo.getDischport());
                        ratevo.setEqptype(tariffvo.getEqptype());
                        ratevo.setOriginPort(origin);
                        ratevo.setDeliveryPort(delivery);

                        if (tariffvo.getLocamt() != null && tariffvo.getLocamt().length() > 0) {
                            totalamount += Float.parseFloat(tariffvo.getLocamt().trim());
                            //ratevo.setAmount(Currencyformat.rupeeFormat(tariffvo.getLocamt().trim()));
                            ratevo.setAmount(formatter.format(Double.parseDouble(tariffvo.getLocamt().trim())));
                        } 
                        if (tariffvo.getSuramt() != null && tariffvo.getSuramt().length() > 0) {
                            ratevo.setSubToCharges(formatter.format(Double.parseDouble(tariffvo.getSuramt())));
                        }
                        
                        if (subToChargesAmt != null && subToChargesAmt.length() > 0) {
                            ratevo.setSubToCharges(formatter.format(Double.parseDouble(subToChargesAmt.trim())));
                        } 
                        System.out.println("[ECOM-WAR] RatesAction subtocharges----->" + ratevo.getSubToCharges());
                        ratevo.setCurrency(tariffvo.getCurrency());
                        ratevo.setCommodity(tariffvo.getCommoditycode());
                        ratevo.setChargeCode("OFR");
                        ratevo.setChargeName("OCEAN FREIGHT");
                        ratevo.setValidfrom(DateConversion_New.convertDefaultTimeFormat(tariffvo.getFmdate()));
                        ratevo.setValidto(DateConversion_New.convertDefaultTimeFormat(tariffvo.getTodate()));
                        ratevo.setQuantity(quantity);
                        ratevo.setShipmentdate(rateform.getShipmentdate());
                        ratevo.setSoc(tariffvo.getSoc());
                        ratevo.setCarrTerms(tariffvo.getCarrTerms());
                        ratevo.setRateNo(tariffvo.getRateNo());
                        rateList.add(ratevo);
                        if (!"N".equalsIgnoreCase(surchargeDetails)) {
                            break;
                        }
                    }
                }
            }
            /* if (surchargeObj != null && surchargeObj.size() > 0) {
                for (int i = 0; i < surchargeObj.size(); i++) {
                    ratevo = new RateVo();
                    SurchargeSummaryVO survo = surchargeObj.get(i);
                    ratevo.setLoadport(survo.getLoadRegion());
                    ratevo.setDischport(survo.getDischargeRegion());
                    ratevo.setEqptype(survo.getEqptype());

                    if (survo.getNor() != null && survo.getNor().length() > 0) {
                        request.setAttribute("IsNorValueExists", "Y");
                    }

                    if (survo.getAmount() != null && survo.getAmount().length() > 0) {
                        totalamount += Float.parseFloat(survo.getAmount().trim());
                        //ratevo.setAmount(Currencyformat.rupeeFormat(survo.getAmount().trim()));
                        ratevo.setAmount(formatter.format(Double.parseDouble(survo.getAmount().trim())));
                    }
                    ratevo.setCurrency(survo.getCurrency());
                    ratevo.setChargeCode(survo.getChargecode());
                    ratevo.setChargeName(survo.getChargename());
                    ratevo.setValidfrom(DateConversion_New.convertDefaultTimeFormat(survo.getValidityFrom()));
                    ratevo.setValidto(DateConversion_New.convertDefaultTimeFormat(survo.getValidityTo()));
                    ratevo.setQuantity(quantity);
                    ratevo.setShipmentdate(rateform.getShipmentdate());
                    ratevo.setNor(survo.getNor());
                    ratevo.setOriginPort(origin);
                    ratevo.setDeliveryPort(delivery);
                    rateList.add(ratevo);

                }
            }*/

            Map resultMap = new HashMap();
            //resultMap.put("totalamount_currencyformat", Currencyformat.rupeeFormat(String.valueOf(totalamount)));
            resultMap.put("totalamount_currencyformat", formatter.format(Double.parseDouble(String.valueOf(totalamount))));
            resultMap.put("totalamount", totalamount);
            resultMap.put("ratelist", rateList);
            session.setAttribute("RATESDETAILS", rateList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
