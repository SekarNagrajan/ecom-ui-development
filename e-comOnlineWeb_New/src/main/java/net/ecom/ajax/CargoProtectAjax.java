/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.CargoProtectVo;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "CargoProtectAjax", urlPatterns = {"/CargoProtectAjax"})
public class CargoProtectAjax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ServletContext contexts;
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String value = null, type = null, originportcode = null, delivportcode = null, disable_Cargoportect = null, originPortCode = null, deliveryPortCode = null, DGFlag = null;
        List<CargoProtectVo> cplis = null;
        String DGFlag_CargoProtect_Export = null, DGFlag_CargoProtect_Import = null;
        String deliveryCountryCode = null, originCountryCode = null, origin = null, delivery = null;

        try {
            PrintWriter out = response.getWriter();
     String portlist = (String) contexts.getAttribute("SESSION_PORTLST");
            type = request.getParameter("type");
            disable_Cargoportect = "N";
            value = request.getParameter("searchvalue");
            System.out.println("[ECOM-WAR] CargoProtectAjax     shipmentdate=> " + value + "type" + type);
            if (value != null && "shipmentdate".equalsIgnoreCase(type)) {
                value = DateConversion_New.convertDefaultDBFormatwithoutTime(value);
                Map<String, Map<String, String>> cargoProtectRestrictionCountryDG = bookingObj.getCargoProtectRestrictionCountryDG();
                // System.out.println("[ECOM-WAR] CargoProtectAjax     cargoProtectRestrictionCountryDG=> " + cargoProtectRestrictionCountryDG);
                HashMap<String, String> Restrict_CargoProtect_Country = (HashMap<String, String>) cargoProtectRestrictionCountryDG.get("export");
                HashMap<String, String> Restrict_CargoProtect_ImportCountry = (HashMap<String, String>) cargoProtectRestrictionCountryDG.get("import");
               
                if (Restrict_CargoProtect_Country != null && Restrict_CargoProtect_Country.size() > 0 || Restrict_CargoProtect_ImportCountry != null && Restrict_CargoProtect_ImportCountry.size() > 0) {

                    origin = Validation.checkNull(request.getParameter("origincountry"));
                    delivery = Validation.checkNull(request.getParameter("deliverycountry"));
                    DGFlag = Validation.checkNull(request.getParameter("dgflag"));
                    // System.out.println("origin: " + origin + " delivery: " + delivery + " DGFlag: " + DGFlag);

                    //Export
                    originPortCode = origin.trim();
                    originCountryCode = bookingObj.getCountryCodeForPort(originPortCode);

                    //import
                    deliveryPortCode = delivery.trim();
                    deliveryCountryCode = bookingObj.getCountryCodeForPort(deliveryPortCode);

                    //DG Flag Export export={IN=Y, INNSA=N}}
                    // System.out.println("Restrict_CargoProtect_Country: " + Restrict_CargoProtect_Country+"//"+originPortCode+"//"+originCountryCode+"//"+Restrict_CargoProtect_Country.containsKey(originPortCode)+"//"+Restrict_CargoProtect_Country.containsKey(originCountryCode));
                    DGFlag_CargoProtect_Export = Restrict_CargoProtect_Country.containsKey(originCountryCode) ? Restrict_CargoProtect_Country.get(originCountryCode) : Restrict_CargoProtect_Country.containsKey(originPortCode) ? Restrict_CargoProtect_Country.get(originPortCode) : null;
                    // System.out.println("DGFlag_CargoProtect_Export: " + Restrict_CargoProtect_Country.get(originPortCode));
                    //DG Flag Import
                    DGFlag_CargoProtect_Import = Restrict_CargoProtect_ImportCountry.containsKey(deliveryCountryCode) ? Restrict_CargoProtect_ImportCountry.get(deliveryCountryCode) : Restrict_CargoProtect_ImportCountry.containsKey(deliveryPortCode) ? Restrict_CargoProtect_ImportCountry.get(deliveryPortCode) : null;

                    // System.out.println("[ECOM-WAR] CargoProtectAjax  DGFlag_CargoProtect_Import" + DGFlag_CargoProtect_Import + "//" + DGFlag_CargoProtect_Export + "//" + DGFlag);

                    //Check the DG Flag
                    if(Validation.checkNull(DGFlag_CargoProtect_Export).length() > 0 && DGFlag_CargoProtect_Export.equalsIgnoreCase("N") || Validation.checkNull(DGFlag_CargoProtect_Import).length() > 0 && DGFlag_CargoProtect_Import.equalsIgnoreCase("N")){
                        disable_Cargoportect = "Y";
                    }else if (Validation.checkNull(DGFlag_CargoProtect_Export).length() > 0 && DGFlag.equalsIgnoreCase(DGFlag_CargoProtect_Export) || Validation.checkNull(DGFlag_CargoProtect_Import).length() > 0 && DGFlag.equalsIgnoreCase(DGFlag_CargoProtect_Import)) {
                        disable_Cargoportect = "Y";
                    }
                }
            }
            cplis = bookingObj.getCargoProtectDetails(type, value);

            System.out.println("[ECOM-WAR] CargoProtectAjax     disable_Cargoportect==> " + disable_Cargoportect + "/" + originPortCode + "/" + deliveryPortCode);

            request.setAttribute("CargoProtectDetails", cplis);
            request.setAttribute("disable_Cargoportect", disable_Cargoportect);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Common/Cargo_Protect_View.jsp");
            rd.forward(request, response);
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
