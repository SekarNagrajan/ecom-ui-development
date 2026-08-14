/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.captcha;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.Validation;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "CaptchaValidation", urlPatterns = {"/CaptchaValidation"})
public class CaptchaValidation extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String strCaptchatxt =null,strCaptcha=null;
        if("LoginRate".equalsIgnoreCase(Validation.checkNull((String) request.getParameter("type")))){
        strCaptchatxt = request.getParameter("captchaR");
        strCaptcha = (String) session.getAttribute("CaptchaCharacter_LoginRate");
        } else {
        strCaptchatxt = request.getParameter("captcha");    
        strCaptcha = (String) session.getAttribute("CaptchaCharacter");
        }
        boolean rslt = true;
        System.out.println("[ECOM-WAR] CaptchaValidation >>User entered Character >>" + strCaptchatxt + ">>Actual Character  >>" + strCaptcha);
        PrintWriter out = response.getWriter();
        if (!strCaptchatxt.equals(strCaptcha)) {
            rslt=false;
        }
        request.setAttribute("validate", rslt);
        System.out.println("[ECOM-WAR] CaptchaValidation captcha validation rslt" + rslt);
        out.print(rslt);
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
