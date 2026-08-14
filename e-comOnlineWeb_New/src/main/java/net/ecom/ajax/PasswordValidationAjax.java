/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.common.CommonPasswordValidation;
import net.ecom.common.CryptoLibrary;
import net.ecom.common.GeneralConf;
import net.ecom.common.utils.DateConversion_New;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "PasswordValidationAjax", urlPatterns = {"/PasswordValidationAjax"})
public class PasswordValidationAjax extends HttpServlet {

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
        Map pwdmap = null;
        String output = "";
        CryptoLibrary crypt = null;
        try {
            crypt = new CryptoLibrary();
            PrintWriter out = response.getWriter();
            String Enable_Password_Validation = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Password_Validation);
            String Password_Validation_API_URL = (String) request.getServletContext().getAttribute(GeneralConf.Password_Validation_API_URL);
            String type = (String) request.getParameter("type");
            String password = (String) request.getParameter("password");
            String username = (String) request.getParameter("username");
            password = crypt.encrypt(password);
             System.out.println("[ECOM-WAR] PasswordValidationAjax            PasswordValidationAjax => Enable_Password_Validation= " + Enable_Password_Validation + " &Password_Validation_API_URL= " + Password_Validation_API_URL + " &type= " + type + " &username= " + username + " &password=" + password);
            if ("PasswordValidation".equalsIgnoreCase(type)) {
                if ("Y".equalsIgnoreCase(Enable_Password_Validation)) {
                    pwdmap = new HashMap();
                    pwdmap.put("username", username);
                    pwdmap.put("password", password);
                    pwdmap.put("lastpwdup", DateConversion_New.getCurrentDateDbFormat());
                    pwdmap.put("isExpirycheck", "N");
                    pwdmap = CommonPasswordValidation.validatePassword(pwdmap, Password_Validation_API_URL);
                }
                if (pwdmap != null) {
                    if ((boolean) pwdmap.get("valid")) {
                        output = "valid~~" + pwdmap.get("pwdmsg").toString();
                    } else {
                        output = "invalid~~" + pwdmap.get("pwdmsg").toString();
                    }
                } else {
                    password = (String) request.getParameter("password");
                    if (password != null && password.length() >= 5) {
                        output = "valid~~success";
                    } else {
                        output = "invalid~~Password should contain Minimum of 5 characters.";
                    }
                }
                out.print(output);
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
    }// </editor-fold>

}
