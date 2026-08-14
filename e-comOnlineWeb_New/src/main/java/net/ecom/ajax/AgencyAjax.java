/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "AgencyAjax", urlPatterns = {"/AgencyAjax"})
public class AgencyAjax extends HttpServlet {

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
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomAdminCntrlBeanRemote adminBeanObj=EJBLookUp.getAdminBeanLookUp();

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            this.config = config;
            context = config.getServletContext();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String agencymail = (String) context.getAttribute("clientfrommail");
        String client_code = (String) context.getAttribute("client_code");
        System.out.println("[ECOM-WAR] AgencyAjax AgencyAjax>>type>>" + type);
        try {
            if ("checksvrcrestrict".equalsIgnoreCase(type)) {
                String pla = request.getParameter("pla");
                String pld = request.getParameter("pld");
                if (pla != null) {
                    pla = pla.split("-")[0].trim();
                }
                if (pld != null) {
                    pld = pld.split("-")[0].trim();
                }
                String service = request.getParameter("service");
                String id = request.getParameter("id");
                String result = ecomCntrlObj.checkSvcRestrict(id, pla, pld, service);
                System.out.println("[ECOM-WAR] AgencyAjax checksvrcrestrict>>result>>" + result);
                out.print(result);
            } else if ("deletesvrcrestrict".equalsIgnoreCase(type)) {
                String id = request.getParameter("id");
                System.out.println("[ECOM-WAR] AgencyAjax >>id>>" + id);
                String result = ecomCntrlObj.deleteSvrcRestrict(id);
                Map<String, List<String>> resSrvcMap = ecomCntrlObj.getSvrcRestrictCacheList();
                //System.out.println("[ECOM-WAR] AgencyAjax >>resSrvcMap> in ajax>" + resSrvcMap);
                context.setAttribute("CACHERESTRICTEDSERVICES", resSrvcMap);
            } else if ("deleteCustAdvis".equalsIgnoreCase(type)) {
                int key = Integer.parseInt(request.getParameter("advisoryKey"));
                try {
                    String result = adminBeanObj.deleteCustomerAdvisoryDetails(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if ("viewImageUpload".equalsIgnoreCase(type)) {
                int key = Integer.parseInt(request.getParameter("configkey"));
                String status = request.getParameter("Imgstatus");
                try {
                    String result = adminBeanObj.deleteImageUploadDetails(key, status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
