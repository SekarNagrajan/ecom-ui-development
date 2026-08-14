/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.RateContractSurchargeVO;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "RatesAjax", urlPatterns = {"/RatesAjax"})
public class RatesAjax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            String rateNo = request.getParameter("rateno");
            String itemno = request.getParameter("itemno");
            String ratetype = request.getParameter("ratetype");
//             System.out.println("[ECOM-WAR] RatesAjax >>rateNo>>"+rateNo);
             System.out.println("[ECOM-WAR] RatesAjax >>rateNo>>"+itemno+",rateNo:"+rateNo);
            List<RateContractSurchargeVO> contractList = ecomCntrlObj.getRateContractSurchargeDets(rateNo, itemno, ratetype);
            request.setAttribute("RATECONTRACTSURCHARGELIST", contractList);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Rates/RateContractSurchargView.jsp");
            rd.forward(request, response);
            return;
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
