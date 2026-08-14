/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "CROPrintServlet", urlPatterns = {"/CROPrintServlet"})
public class CROPrintServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try {
//            //
//             System.out.println("[ECOM-WAR] CROPrintServlet CRO PRINT DOWNLOAD>>>>>>>>>>>>>");
            String croNo = request.getParameter("croNo");
            String responseType = request.getParameter("responseType");
            String viewtype = request.getParameter("viewtype");
            String res = (String) session.getAttribute("crojasperbyte");
            
             System.out.println("[ECOM-WAR] CROPrintServlet croNo==> "+croNo+" &responseType=> "+responseType+" &viewtype=> "+viewtype);

            if (res != null && res.length() > 0 && "200".equalsIgnoreCase(responseType) && "J".equalsIgnoreCase(viewtype)) {
                response.setContentType("application/x-java-jnlp-file");
                response.setHeader("Content-disposition", "attachment; filename=" + croNo + ".jnlp;");
                response.getOutputStream().write(res.getBytes());
            } else if (res != null && res.length() > 0 && "200".equalsIgnoreCase(responseType) && "P".equalsIgnoreCase(viewtype)) {
//                 System.out.println("[ECOM-WAR] CROPrintServlet ====Inside Pdf conditions====");
                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", "attachment; filename=" + croNo + ".pdf");
                response.getOutputStream().write(Base64.getDecoder().decode(res));
            } else {
                forward("croaction.do", request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forward(String forwardpage, HttpServletRequest request, HttpServletResponse response) {
        try {
//             System.out.println("[ECOM-WAR] CROPrintServlet ======Launching JNLP....");
            RequestDispatcher rd = request.getRequestDispatcher(forwardpage);
            rd.forward(request, response);
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] CROPrintServlet Exception while forwarding :" + e);
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
