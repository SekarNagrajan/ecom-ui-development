/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BLBeanRemote;
import net.ecom.vo.ReturnVO;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "BLAjax", urlPatterns = {"/BLAjax"})
public class BLAjax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    EcomBLBeanRemote locobj = EJBLookUp.getBLBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomV1BLBeanRemote v1BLObj = EJBLookUp.getV1BLBeanLookUp();
    ServletContext contexts;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            /* TODO output your page here. You may use following sample code. */
            String type = request.getParameter("type");
             System.out.println("[ECOM-WAR] BLAjax  type"+type);
            if ("cancelbl".equalsIgnoreCase(type)) {
                String siNo = request.getParameter("sino");
                String result = null;
                String appversion = request.getParameter("appversion");
                if ("1".equalsIgnoreCase(appversion)) {
                    result = v1BLObj.cancelSubmittedV1BL(siNo);
                } else {
                    result = locobj.cancelSubmittedBL(siNo);
                }
                out.print(result);
            } else if ("acceptbl".equalsIgnoreCase(type)) {
//                System.out.println("[ECOM-WAR] BLAjax inside acceptbl");
                String blNo = request.getParameter("blno");
                String message = null;
                String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                String bookno = request.getParameter("bookno");
                String agencycode = request.getParameter("agencycode");
                String agencyrefno = request.getParameter("agencyrefno");
                String origin = request.getParameter("origin");
                String delivery = request.getParameter("delivery");
                String apcust = locobj.getBLAPCustomer(blNo);
                ReturnVO vo = locobj.acceptBLByUser(blNo, agencycode);

                String frommail = (String) contexts.getAttribute("clientfrommail");
                String client_code = (String) contexts.getAttribute("client_code");
                String strDate = null;
                SimpleDateFormat reqFormat = null;
                reqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dt = new Date();
                strDate = reqFormat.format(dt);
                String initiatorID = "ECOM-BL";
                String formatDate = DateConversion_New.convertDefaultFormat(strDate);
                Mailer mail = null;
                if (vo != null) {
                    mail = new Mailer();
                    mail.setTo(loginUser);
                    message = vo.getStrMailMsg();
                    message = message.replaceAll("@bookno", bookno);
                    message = message.replace("@date", formatDate);
                    message = message.replaceAll("@agency_reference_no", checkNullAgencyRefNo(agencyrefno));
                    mail.setSubject(vo.getStrSubject());
                    mail.setMessage(message);
                    ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                }
                if (vo != null && vo.getStrToMailId() != null && vo.getStrToMailId().length() > 0) {
                    mail = new Mailer();
                    message = vo.getAdmMailMsg();
                    mail.setTo(vo.getAdmToMailId());
                    message = message.replaceAll("@bookno", bookno);
                    message = message.replace("@date", formatDate);
                    message = message.replaceAll("@agency_reference_no", checkNullAgencyRefNo(agencyrefno));
                    message = message.replaceAll("@origin", origin);
                    message = message.replaceAll("@delivery", delivery);
                    message = message.replaceAll("@apcustomer", apcust);
                    mail.setMessage(message);
                    mail.setSubject(vo.getAdmSubject());
                    ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                }
                out.print("");
            } else if ("v1signaturecheck".equalsIgnoreCase(type)) {
                String bookno = request.getParameter("book_no");
                String agencycode = request.getParameter("agencycode");
                String shipmentstatus = request.getParameter("shipmentstatus");
                String result = v1BLObj.getV1BLAgencySigmature(agencycode, bookno, shipmentstatus);
                System.out.println("[ECOM-WAR] BLAjax >>>result>>>" + result);
                out.print(result);
            } else if ("isvoyageclosed".equalsIgnoreCase(type)) {
                String blno = Validation.checkNull(request.getParameter("blno"));
                String result = Validation.checkNull(locobj.getisvoyage(blno)).toUpperCase();
                System.out.println("[ECOM-WAR] BLAjax >>>result>>>" + result);
                out.print(result);
            } else if ("blOpration".equalsIgnoreCase(type)) {
                String blno = Validation.checkNull(request.getParameter("blno"));
                String rsl = locobj.checkbloperationdetails(blno);
                System.out.println("[ECOM-WAR] BLAjax rsl=>" + rsl+"blno=>"+blno);
                out.print(rsl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String checkNullAgencyRefNo(String agencyrefno) {
        if (agencyrefno != null && agencyrefno.length() > 0) {
            agencyrefno = "<br><p>Agency Reference No : " + agencyrefno + "</p>";
        } else {
            agencyrefno = "";
        }
        return agencyrefno.trim();
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
