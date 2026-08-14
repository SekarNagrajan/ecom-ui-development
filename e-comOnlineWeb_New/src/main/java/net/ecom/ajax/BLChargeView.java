/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vds.serviceVo.BLChargeVo;

/**
 *
 * @author preethi.elumalai
 */
@WebServlet(name = "BLChargeView", urlPatterns = {"/BLChargeView"})
public class BLChargeView extends HttpServlet {

    EcomBLBeanRemote locobj = EJBLookUp.getBLBeanLookUp();

    ServletContext context;

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            context = config.getServletContext();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

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
        String blno = Validation.checkNull(request.getParameter("blno"));
        System.out.println("[ECOM-WAR] BLChargeView BLChargeView processRequest ===blno is===>" + blno);
        List<BLChargeVo> chargeview = null;
        try {
            HttpSession session = request.getSession();
            chargeview = (ArrayList<BLChargeVo>) locobj.getBlCgargeSummaryView(blno);
            session.setAttribute("blChargeview", chargeview);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/BL/BLCharge.jsp");
            rd.forward(request, response);
        } catch (Exception e) {

            System.out.println("[ECOM-WAR] BLChargeView Exception in BLChargeView execute - " + e);
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
