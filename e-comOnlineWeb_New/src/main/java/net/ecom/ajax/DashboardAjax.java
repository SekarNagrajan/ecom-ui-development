/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
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
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.common.Currencyformat;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.OnGoingTransactionVo;
import net.ecom.vo.UnclearedCargoVo;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "DashboardAjax", urlPatterns = {"/DashboardAjax"})
public class DashboardAjax extends HttpServlet {

    EcomAdminCntrlBeanRemote adminBeanObj = EJBLookUp.getAdminBeanLookUp();
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
        try {
            HttpSession session = request.getSession();
            String userid = (String) session.getAttribute("SESSION_USERCODE");
            String type = request.getParameter("type");
            Hashtable portlist = (Hashtable) context.getAttribute("SESSION_PORTLIST");
            PrintWriter out = response.getWriter();
            if ("OnGoingTransaction".equalsIgnoreCase(type)) {
                List dashlis = adminBeanObj.getEcomOnGoingTransactionDetails(userid);
                if (dashlis != null && dashlis.size() > 0) {
                    for (int i = 0; i < dashlis.size(); i++) {
                        OnGoingTransactionVo ongodashvo = (OnGoingTransactionVo) dashlis.get(i);
                        ongodashvo.setOriginportid(ongodashvo.getOriginportid() + " - " + portlist.get(Validation.checkNull(ongodashvo.getOriginportid())));
                        ongodashvo.setDischargeportid(ongodashvo.getDischargeportid() + " - " + portlist.get(Validation.checkNull(ongodashvo.getDischargeportid())));
                        ongodashvo.setLoadportid(ongodashvo.getLoadportid() + " - " + portlist.get(Validation.checkNull(ongodashvo.getLoadportid())));
                        ongodashvo.setFinalportid(ongodashvo.getFinalportid() + " - " + portlist.get(Validation.checkNull(ongodashvo.getFinalportid())));
                        ongodashvo.setBookdate(DateConversion_New.convertDefaultFormatwithTime(ongodashvo.getBookdate()));
                        ongodashvo.setPolat(DateConversion_New.convertDefaultFormatwithTime(ongodashvo.getPolat()));
                        dashlis.set(i, ongodashvo);
                    }
                }
                request.setAttribute("ONGOINGDASHBOARDDETAILS", dashlis);

                RequestDispatcher rd = request.getRequestDispatcher("/OnGoingTransaction.jsp");
                rd.forward(request, response);
            } else if ("OnGoingTransactionCargodtls".equalsIgnoreCase(type)) {
                String bookno = request.getParameter("bookno");
                String blno = request.getParameter("blno");
                List dashlis = adminBeanObj.getEcomOnGoingTransactionCargoDetails(bookno, blno);
                request.setAttribute("ONGOINGDASHBOARDCARGODETAILS", dashlis);
                RequestDispatcher rd = request.getRequestDispatcher("/OnGoingTransactionCargoDetails.jsp");
                rd.forward(request, response);
            } /*else if ("UnclearedCargoDetail".equalsIgnoreCase(type)) {
                List dashlis = adminBeanObj.getEcomUnclearedCargoDetails(userid);
                if (dashlis != null && dashlis.size() > 0) {
                    for (int i = 0; i < dashlis.size(); i++) {
                        UnclearedCargoVo unclrdcargovo = (UnclearedCargoVo) dashlis.get(i);
                        unclrdcargovo.setLoadport(unclrdcargovo.getLoadport() + " - " + portlist.get(Validation.checkNull(unclrdcargovo.getLoadport())));
                        unclrdcargovo.setDischargeport(unclrdcargovo.getDischargeport() + " - " + portlist.get(Validation.checkNull(unclrdcargovo.getDischargeport())));
                        unclrdcargovo.setVesselarrdate(DateConversion_New.convertDefaultFormatwithTime(unclrdcargovo.getVesselarrdate()));
                        dashlis.set(i, unclrdcargovo);
                    }
                }
                request.setAttribute("UnclearedCargoDetails", dashlis);
                RequestDispatcher rd = request.getRequestDispatcher("/UnclearedCargoDetails.jsp");
                rd.forward(request, response);
            }*/
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
