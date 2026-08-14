/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.common.GeneralConf;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "FileAjax", urlPatterns = {"/FileAjax"})
public class FileAjax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ServletConfig config;
    ServletContext context;

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            this.config = config;
            context = config.getServletContext();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
//             System.out.println("[ECOM-WAR] FileAjax        inside FileAjax");
            //PrintWriter out = response.getWriter();
            OutputStream out = response.getOutputStream();
            String filename = request.getParameter("filename");
            String type = request.getParameter("type");
            String refno = request.getParameter("refno");
            String filepath = (String) context.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH);
            HashSet bookingAccessList = (HashSet) request.getSession().getAttribute("bookingAccessList");
            HashSet siAccessList = (HashSet) request.getSession().getAttribute("siAccessList");
            HashSet blAccessList = (HashSet) request.getSession().getAttribute("blAccessList");

            if ((bookingAccessList != null && bookingAccessList.contains(refno)) || (siAccessList != null && siAccessList.contains(refno)) || (blAccessList != null && blAccessList.contains(refno))) {
                if ("BL".equalsIgnoreCase(type)) {
                    filepath = filepath.replace("Ecom", "BL");
                } else if ("BKG".equalsIgnoreCase(type)) {
                    filepath = filepath.replace("Ecom", "BKG");
                }
                filepath = filepath + refno + File.separator;
                File file = new File(filepath + filename);
                if (file.exists()) {
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

                    FileInputStream fileInputStream = new FileInputStream(filepath + filename);

                    int i;
                    while ((i = fileInputStream.read()) != -1) {
                        out.write(i);
                    }
                    fileInputStream.close();
                    out.close();
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
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
        // processRequest(request, response);
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
