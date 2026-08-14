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
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;

/**
 *
 * @author preethi.elumalai
 */
@WebServlet(name = "downloadPdf", urlPatterns = {"/downloadPdf"})
public class PdfDownloadServlet extends HttpServlet {
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
        try {
            String fileName = Validation.checkNull(request.getParameter("filename"));
            String Ecom_Notice_Path = Validation.checkNull((String) context.getAttribute(GeneralConf.Ecom_Notice_Path));
            String sharePath = Validation.checkNull(Ecom_Notice_Path);

             System.out.println("[ECOM-WAR] PdfDownloadServlet              fileName=>" + fileName+ "///"+sharePath);
            File directory = new File(sharePath);
            if (!directory.exists()) {
                directory.mkdir();
            }
            String pdfFilePath = sharePath + File.separator + fileName;
             System.out.println("[ECOM-WAR] PdfDownloadServlet              pdfFilePath" + pdfFilePath);
            File pdfFile = new File(pdfFilePath);
          
            if (pdfFile.exists()) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");

                // Write PDF content to response
                try (FileInputStream fis = new FileInputStream(pdfFilePath);
                        OutputStream os = response.getOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                 System.out.println("[ECOM-WAR] PdfDownloadServlet              PDF file not found: " + pdfFilePath);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("PDF file not found: " + fileName);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
