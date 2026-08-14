/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.lookup.EJBLookUp;

/**
 *
 * @author deepa.p
 */
public class DOPrintServlet extends HttpServlet {

    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try {
            //
            String dono = request.getParameter("dono");
            String responseType = request.getParameter("responseType");
            String viewtype = request.getParameter("viewtype");
            String res = (String) session.getAttribute("Oblcontent");

            if (res != null && res.length() > 0 && "200".equalsIgnoreCase(responseType) && "J".equalsIgnoreCase(viewtype)) {
                ecomPhase2Obj.updateDOStatus(dono);
                response.setContentType("application/x-java-jnlp-file");
                response.setHeader("Content-disposition", "attachment; filename=" + dono + ".jnlp;");
                response.getOutputStream().write(res.getBytes());
            } else if (res != null && res.length() > 0 && "200".equalsIgnoreCase(responseType) && "P".equalsIgnoreCase(viewtype)) {
//                 System.out.println("[ECOM-WAR]DOPrintServlet    ====Inside Pdf conditions====do>>");
                ecomPhase2Obj.updateDOStatus(dono);
                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", "attachment; filename=" + dono + ".pdf");
                response.getOutputStream().write(Base64.getDecoder().decode(res));
                //forward("deliveryorder.do", request, response);
            } else {
                forward("deliveryorder.do", request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forward(String forwardpage, HttpServletRequest request, HttpServletResponse response) {
        try {
//             System.out.println("[ECOM-WAR]DOPrintServlet    ======Launching JNLP....");
            RequestDispatcher rd = request.getRequestDispatcher(forwardpage);
            rd.forward(request, response);
        } catch (Exception e) {
             System.out.println("[ECOM-WAR]DOPrintServlet    Exception while forwarding :" + e);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
