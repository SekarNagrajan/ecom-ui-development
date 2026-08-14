/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.vo.RegisterMenuVO;

/**
 *
 * @author sasikumar.r
 */
public class ActionExcel extends HttpServlet {

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
        try {
            HttpSession session = request.getSession();
            ServletContext contexts = getServletContext();
            String client_code = (String) contexts.getAttribute("client_code");
            String Display_LCT_CUTOFF = (String) contexts.getAttribute(GeneralConf.Display_LCT_CUTOFF);
            ByteArrayOutputStream bos = null;
            String bwlocpol = null;
            String bwlocpod = null;
            Map menumap = (Map) contexts.getAttribute("usersubmenu");
            List<RegisterMenuVO> menulist = (List<RegisterMenuVO>) menumap.get("SCH");
            String strtype = request.getParameter("strtype");
            if ("Port".equalsIgnoreCase(strtype)) {
                Attechment attachmentHelper = new Attechment();
                ArrayList arr = (ArrayList) session.getAttribute("PortList");
                String atport = (String) session.getAttribute("atport");
                String fromdate = (String) session.getAttribute("fromdate");
                String todate = (String) session.getAttribute("todate");
                System.out.println("[ECOM-WAR] ActionExcel portcode:::::" + atport);
                // String portname=atport+ "-" +Validation.checkNull((String)portlist.get(atport));
                String portname = atport;
                bos = (ByteArrayOutputStream) attachmentHelper.buildAttachment("Excel", "Port", arr, portname, fromdate, todate, bwlocpol, bwlocpod, client_code, menulist, Display_LCT_CUTOFF);
            } else if ("Vessel".equalsIgnoreCase(strtype)) {
                Attechment attachmentHelper = new Attechment();
                ArrayList arr = (ArrayList) session.getAttribute("PortList");
                String atport = (String) session.getAttribute("atport");
                String fromdate = (String) session.getAttribute("fromdate");
                String todate = (String) session.getAttribute("todate");
                String portname = atport;
                //  String portname=atport+ "-" +Validation.checkNull((String)vessellist.get(atport));
                bos = (ByteArrayOutputStream) attachmentHelper.buildAttachment("Excel", "Vessel", arr, portname, fromdate, todate, bwlocpol, bwlocpod, client_code, menulist, Display_LCT_CUTOFF);
            } else if ("Location".equalsIgnoreCase(strtype)) {
                Attechment attachmentHelper = new Attechment();
                ArrayList arr = (ArrayList) session.getAttribute("PortList");
                bwlocpol = (String) session.getAttribute("bwlocpol");
                bwlocpod = (String) session.getAttribute("bwlocpod");
                String atport = (String) session.getAttribute("BTW_LOC_TYPE");
                String fromdate = (String) session.getAttribute("bwlocfromdate");
                String todate = (String) session.getAttribute("bwloctodate");
                String pol = bwlocpol;
                String pod = bwlocpod;
                //  String pol=bwlocpol+"-"+Validation.checkNull((String)portlist.get(bwlocpol));
                //  String pod=bwlocpod+"-"+Validation.checkNull((String)portlist.get(bwlocpod));
                System.out.println("[ECOM-WAR] ActionExcel ====>fromdate" + fromdate + todate);
                bos = (ByteArrayOutputStream) attachmentHelper.buildAttachment("Excel", "Location", arr, atport, fromdate, todate, pol, pod, client_code, menulist, Display_LCT_CUTOFF);
            }
            //  file = (File) exr.ExcelCration();
            response.setContentType("application/vnd.ms-excel;Content-Transfer-Encoding:binary");
            response.setHeader("Content-Disposition", "attachment; filename=Schedules-" + strtype + ".xls");

            response.getOutputStream().write(bos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            //  if (file.exists()) {
            //  FileUtils.deleteQuietly(file);
            //  }

        } catch (Exception e) {
            System.out.println("[ECOM-WAR] ActionExcel ===>" + e);
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
