/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.Currencyformat;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.SurchargeSummaryVO;
import net.ecom.phase2.service.vo.TariffSummaryVO;
import net.ecom.vo.RateVo;
import org.w3c.dom.DOMException;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "ScheduleRateAjax", urlPatterns = {"/ScheduleRateAjax"})
public class ScheduleRateAjax extends HttpServlet {

    ServletContext contexts;
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            // RateVo ratevo = null;
            //List<RateVo> rateList = null;
            List<Map> rateList = null;
            HttpSession session = request.getSession();
            String carriercode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
            String pol = request.getParameter("pol");
            String pod = request.getParameter("pod");
            String shipmentdate = request.getParameter("shipmentdate");
            String commodity = request.getParameter("commodity");
            String eqptype = request.getParameter("eqptype");
            String quantity = request.getParameter("quantity");
            pol = Validation.checkNull(pol != null ? pol.split("-")[0] : pol);
            pod = Validation.checkNull(pod != null ? pod.split("-")[0] : pod);
            commodity = Validation.checkNull(commodity != null ? commodity.split("-")[0] : commodity);
//             System.out.println("[ECOM-WAR] ScheduleRateAjax     pol--------------------> " + pol);
//             System.out.println("[ECOM-WAR] ScheduleRateAjax     pod--------------------> " + pod);
//             System.out.println("[ECOM-WAR] ScheduleRateAjax     shipmentdate--------------------> " + shipmentdate);
//             System.out.println("[ECOM-WAR] ScheduleRateAjax     commodity--------------------> " + commodity);
//             System.out.println("[ECOM-WAR] ScheduleRateAjax     eqptype--------------------> " + eqptype);
//             System.out.println("[ECOM-WAR] ScheduleRateAjax     quantity--------------------> " + quantity);
             System.out.println("[ECOM-WAR] ScheduleRateAjax     pol: " + pol + ", pod: " + pod + ", shipmentdate: " + shipmentdate + ", commodity: " + commodity + ", eqptype: " + eqptype + ", quantity: " + quantity);

            shipmentdate = DateConversion_New.convertDefaultDBFormatwithoutTime(shipmentdate);
            String surchargeDetails = (String) contexts.getAttribute(GeneralConf.surcharge_details);
            String showExternalRateTotalAmount = (String) contexts.getAttribute(GeneralConf.SHOW_EXTERNALRATE_TOTAL_AMOUNT);
            String checkRouting = (String) contexts.getAttribute(GeneralConf.TARIFFSUMMARY_CHECK_ROUTING);
            List<TariffSummaryVO> tariffObj = null;
            List<SurchargeSummaryVO> surchargeObj = null;
            boolean isMaskPort = MaskPort.isMaskPorts(pol + "," + pod, contexts);
            if (isMaskPort) {
                tariffObj = ecomPhase2Obj.getTarriffSummary(pol, pod, commodity, eqptype, shipmentdate, carriercode, surchargeDetails, showExternalRateTotalAmount, checkRouting);

                surchargeObj = ecomPhase2Obj.getSurchargeSummary(pol, pod, eqptype, shipmentdate);
            }
            //rateList = new ArrayList<RateVo>();
            rateList = new ArrayList<Map>();
            Map<String, String> ratemap;
            float totalamount = 0.0f;
            float totalExternalRate = 0.0f;
            if (tariffObj != null && tariffObj.size() > 0) {
                for (int i = 0; i < tariffObj.size(); i++) {
                    /*ratevo = new RateVo();
                    TariffSummaryVO tariffvo = tariffObj.get(i);
                    ratevo.setLoadport(tariffvo.getLoadport());
                    ratevo.setDischport(ratevo.getDischport());
                    ratevo.setEqptype(tariffvo.getEqptype());
                    ratevo.setAmount(tariffvo.getLocamt());
                    ratevo.setCurrency(tariffvo.getCurrency());
                    ratevo.setCommodity(tariffvo.getCommoditycode());
                    ratevo.setChargeCode("OFR");
                    ratevo.setChargeName("OCEAN FREIGHT");
                    ratevo.setValidfrom(DateConversion_New.convertDefaultTimeFormat(tariffvo.getFmdate()));
                    ratevo.setValidto(DateConversion_New.convertDefaultTimeFormat(tariffvo.getTodate()));
                    rateList.add(ratevo);*/

                    TariffSummaryVO tariffvo = tariffObj.get(i);
                    ratemap = new LinkedHashMap<String, String>();
                    ratemap.put("loadport", tariffvo.getLoadport());
                    ratemap.put("dischport", tariffvo.getDischport());
                    ratemap.put("eqptype", tariffvo.getEqptype());
                    ratemap.put("amount", tariffvo.getLocamt());
                    if (tariffvo.getLocamt() != null && tariffvo.getLocamt().length() > 0) {
                        totalamount += Float.parseFloat(tariffvo.getLocamt().trim());
                        ratemap.put("amount", Currencyformat.rupeeFormat(tariffvo.getLocamt().trim()));
                    }
                    if (tariffvo.getExternalrate() != null && tariffvo.getExternalrate().length() > 0) {
                        totalExternalRate += Float.parseFloat(tariffvo.getExternalrate().trim());
                        ratemap.put("externalrate", Currencyformat.rupeeFormat(tariffvo.getExternalrate().trim()));
                    }
                    ratemap.put("currency", tariffvo.getCurrency());
                    ratemap.put("commodity", tariffvo.getCommoditycode());
                    ratemap.put("chargecode", "OFR");
                    ratemap.put("chargename", "OCEAN FREIGHT");
                    ratemap.put("validfrom", DateConversion_New.convertDefaultTimeFormat(tariffvo.getFmdate()));
                    ratemap.put("validto", DateConversion_New.convertDefaultTimeFormat(tariffvo.getTodate()));
                    ratemap.put("quantity", quantity);

                    rateList.add(ratemap);
                }
            }
            if (surchargeObj != null && surchargeObj.size() > 0) {
                for (int i = 0; i < surchargeObj.size(); i++) {
                    /*ratevo = new RateVo();
                    SurchargeSummaryVO survo = surchargeObj.get(i);
                    ratevo.setLoadport(survo.getLoadRegion());
                    ratevo.setDischport(survo.getDischargeRegion());
                    ratevo.setEqptype(survo.getEqptype());
                    ratevo.setAmount(survo.getAmount());
                    ratevo.setCurrency(survo.getCurrency());
                    ratevo.setChargeCode(survo.getChargecode());
                    ratevo.setChargeName(survo.getChargename());
                    ratevo.setValidfrom(DateConversion_New.convertDefaultTimeFormat(survo.getValidityFrom()));
                    ratevo.setValidto(DateConversion_New.convertDefaultTimeFormat(survo.getValidityTo()));
                    rateList.add(ratevo);*/

                    SurchargeSummaryVO survo = surchargeObj.get(i);
                    ratemap = new LinkedHashMap<String, String>();
                    ratemap.put("loadport", survo.getLoadRegion());
                    ratemap.put("dischport", survo.getDischargeRegion());
                    ratemap.put("eqptype", survo.getEqptype());
                    ratemap.put("amount", survo.getAmount());
                    if (survo.getAmount() != null && survo.getAmount().length() > 0) {
                        totalamount += Float.parseFloat(survo.getAmount().trim());
                        ratemap.put("amount", Currencyformat.rupeeFormat(survo.getAmount().trim()));
                    }
                    ratemap.put("currency", survo.getCurrency());
                    ratemap.put("chargecode", survo.getChargecode());
                    ratemap.put("chargename", survo.getChargename());
                    ratemap.put("validfrom", DateConversion_New.convertDefaultTimeFormat(survo.getValidityFrom()));
                    ratemap.put("validto", DateConversion_New.convertDefaultTimeFormat(survo.getValidityTo()));
                    ratemap.put("quantity", quantity);

                    rateList.add(ratemap);

                }
            }

            Map resultMap = new HashMap();
            resultMap.put("totalamount_currencyformat", Currencyformat.rupeeFormat(String.valueOf(totalamount)));
            resultMap.put("totalamount", totalamount);
            resultMap.put("ratelist", rateList);
            Gson gson = new Gson();
            String jsonstr = gson.toJson(resultMap);
             System.out.println("[ECOM-WAR] ScheduleRateAjax     Schedule rate json string => " + jsonstr);
            session.setAttribute("SCHEDULERATEDETAILS", rateList);
            out.print(jsonstr);
//            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Schedules/ScheduleRateView.jsp");
//            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
