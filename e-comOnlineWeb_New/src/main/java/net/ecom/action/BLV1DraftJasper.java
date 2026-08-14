/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "BLV1DraftJasper", urlPatterns = {"/BLV1DraftJasper"})
public class BLV1DraftJasper extends HttpServlet {

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
        JasperPrint pdfname = null;
        byte[] byteArray = null;
        URLConnection con = null;
        OutputStream outstr = null;
        ObjectOutputStream oos = null;
        ByteArrayOutputStream boas = null;
        try {
            HttpSession session = request.getSession();
            String blnm = Validation.checkNull(request.getParameter("blno"));
            String type = Validation.checkNull(request.getParameter("type"));
            String agencycode = Validation.checkNull(request.getParameter("agencycode"));
//             System.out.println("[ECOM-WAR] BLOriginalPrint          blnm: " + blnm);
//             System.out.println("[ECOM-WAR] BLOriginalPrint          type: " + type);
//             System.out.println("[ECOM-WAR] BLOriginalPrint          agencycode: " + agencycode);

            //
            con = getServletConnection(blnm, type);
            InputStream instr = con.getInputStream();
            InputStream buffer = new BufferedInputStream(instr);
            ObjectInputStream inputFromServlet = new ObjectInputStream(buffer);
//             System.out.println("[ECOM-WAR] BLOriginalPrint          >>inputFromServlet>>>" + inputFromServlet);
            Object results = (Object) inputFromServlet.readObject();
//             System.out.println("[ECOM-WAR] BLOriginalPrint          >>>results>>" + results);
             System.out.println("[ECOM-WAR] BLOriginalPrint          inputFromServlet: " + inputFromServlet + ", blnm: " + blnm + ", type: " + type + ", agencycode: " + agencycode + ", results: " + results);

            //pdfname = ((JasperPrint) results);
            // System.out.println("[ECOM-WAR] BLOriginalPrint          Page Heigth--->" + pdfname.getPageHeight());
            // System.out.println("[ECOM-WAR] BLOriginalPrint          Page Width--->" + pdfname.getPageWidth());
            //
            try {
                byteArray = ((byte[]) results);
                //byteArray = (byte[]) JasperExportManager.exportReportToPdf(pdfname);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if ("Original".equalsIgnoreCase(type)) {

                boas = new ByteArrayOutputStream();

                outstr = response.getOutputStream();
                oos = new ObjectOutputStream(outstr);

                // oos.writeObject(boas.toByteArray());   byteArray
                oos.writeObject(pdfname);
                boas.close();
                outstr.close();

            } else {
//                try {
//                    byteArray = (byte[]) JasperExportManager.exportReportToPdf(pdfname);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                session.setAttribute("jasperbyte", byteArray);
            }
            //
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private URLConnection getServletConnection(String blnm, String type) throws IOException {
        URLConnection conn = null;
        try {
            ServletContext context = getServletContext();
            String ws_url = (String) context.getAttribute(GeneralConf.V1OBLPRINTURL) + "/PrintConfirmedBooking-war/PrintConfirmedBookingServlet?type=" + type + "&blno=" + blnm;
            URL urlServlet = new URL(ws_url);
            conn = urlServlet.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(true);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
        } catch (Exception e) {
            e.printStackTrace();
             System.out.println("[ECOM-WAR] BLOriginalPrint          connection" + e.getMessage());
        }
        return conn;
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
