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
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.CROSummaryVO;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "Phase2Ajax", urlPatterns = {"/Phase2Ajax"})
public class Phase2Ajax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
        EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();
        try {
            String type = request.getParameter("type");
            if ("crodetails".equalsIgnoreCase(type)) {
                String porturl = null;
                String strHTML = "";
                HttpSession session = request.getSession();
//                 System.out.println("[ECOM-WAR] processRequest               this is cro details in ajax calll...................");
                PrintWriter out = response.getWriter();
                String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
                String bookNo = Validation.checkNull(request.getParameter("bookNo"));
                String eqpType = Validation.checkNull(request.getParameter("eqpType"));
                String releaseNo = Validation.checkNull(request.getParameter("releaseno"));
                String agencycode = Validation.checkNull(request.getParameter("agencycode"));
                String divid = Validation.checkNull(request.getParameter("divid"));
                 System.out.println("[ECOM-WAR] processRequest               ........div id......." + divid);
                List<CROSummaryVO> summaryList = ecomPhase2Obj.getCRODetails(bookNo, releaseNo, eqpType);
                request.setAttribute("CRODETAILS", summaryList);
                request.setAttribute("CROAGENCYCODE", agencycode);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Phase2/CRODetails.jsp");
                rd.forward(request, response);

            }
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
    }
}
