/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.lookup.EJBLookUp;

/**
 *
 * @author preethi.elumalai
 */
@WebServlet(name = "EnsFilingAjax", urlPatterns = {"/EnsFilingAjax"})
public class EnsFilingAjax extends HttpServlet {

    EcomBookingBeanRemote bookbean = EJBLookUp.getBookingBeanLookUp();

    ServletContext context;

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            context = config.getServletContext();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            String type = request.getParameter("type");
             System.out.println("[ECOM-WAR] EnsFilingAjax       Request type: " + type);
            if ("checkEUPort".equalsIgnoreCase(type)) {
                String result = "N";
                String origin = "", delivery = "";
                String org = request.getParameter("origin");
                String dest = request.getParameter("delivary");
                if (org.contains("-")) {
                    origin = org.substring(0, org.indexOf('-')).trim();
                }else{
                    origin=org;
                }
                if (dest.contains("-")) {
                    delivery = dest.substring(0, dest.indexOf('-')).trim();
                }else{
                    delivery=dest;
                }
                HashMap<String, String> hs = bookbean.checkEUPort(origin, delivery);
                String pol = hs.get(origin);
                String pod = hs.get(delivery);
//                boolean isEnsFiling = bookbean.checkEUPort(origin, delivery);
                if ("Y".equalsIgnoreCase(pod) && "N".equalsIgnoreCase(pol)) {
                    result = "Y";
                }
                session.setAttribute("IsEnableENSTabInBooking", result);
                out.print(result);
            } else if ("isChkAPEnsFilingAgreement".equalsIgnoreCase(type)) {
                String apCustomerCode = request.getParameter("apcode");
                String ensFilingType = bookbean.isChkAPEnsFilingAgreement(apCustomerCode);
                out.print(ensFilingType);
            } else if ("isT2LFiling".equalsIgnoreCase(type)) {
                String result = "N";
                String origin = "", delivery = "";
                String org = request.getParameter("origin");
                String dest = request.getParameter("delivary");
                if (org.contains("-")) {
                    origin = org.substring(0, org.indexOf('-')).trim();
                }
                if (dest.contains("-")) {
                    delivery = dest.substring(0, dest.indexOf('-')).trim();
                }
                HashMap<String, String> hs = bookbean.checkEUPort(origin, delivery);
                String pol = hs.get(origin);
                String pod = hs.get(delivery);
//                boolean isEnsFiling = bookbean.checkEUPort(origin, delivery);
                if ("Y".equalsIgnoreCase(pod) && "Y".equalsIgnoreCase(pol)) {
                    result = "Y";
                }
                 System.out.println("[ECOM-WAR] EnsFilingAjax       result=>"+result);
                out.print(result);
            }
        } catch (IOException e) {
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
