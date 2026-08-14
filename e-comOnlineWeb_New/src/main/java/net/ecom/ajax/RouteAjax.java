/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.CutoffConfigVO;

public class RouteAjax extends HttpServlet {

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
            HttpSession session = request.getSession();
            response.setContentType("text/html;charset=UTF-8");
            String type = request.getParameter("type");
            PrintWriter out = response.getWriter();
            List pluginlis = (List) context.getAttribute("LRP_PLUGIN_DETAILS");
            boolean chkbkgcutoff = false;
            List cutlis = null;
            Map cutmap = null;
             System.out.println("[ECOM-WAR] RouteAjax   type"+type);
            if ("select".equalsIgnoreCase(type)) {
                String id = request.getParameter("routeid");
                String cutOffPort = request.getParameter("cutoffport");
                String cutOffTerminal = request.getParameter("cutoffterminal");
                String cutOffDate = request.getParameter("cutoffdate");
                String pol = request.getParameter("pol");
                String pod = request.getParameter("pod");
                String grpcallid = request.getParameter("grpcallid");

//                 System.out.println("[ECOM-WAR] RouteAjax   >>cutOffDate>>" + cutOffDate);
//                 System.out.println("[ECOM-WAR] RouteAjax   >>cutOffPort>>" + cutOffPort);
//                 System.out.println("[ECOM-WAR] RouteAjax   >>cutOffTerminal>>" + cutOffTerminal);
//                 System.out.println("[ECOM-WAR] RouteAjax   >>pol>>" + pol);
//                 System.out.println("[ECOM-WAR] RouteAjax   >>pod>>" + pod);
//                 System.out.println("[ECOM-WAR] RouteAjax   >>grpcallid>>" + grpcallid);
                 System.out.println("[ECOM-WAR] RouteAjax   >>cutOffDate>>" + cutOffDate + ", >>cutOffPort>>" + cutOffPort + ", >>cutOffTerminal>>" + cutOffTerminal + ", >>pol>>" + pol + ", >>pod>>" + pod + ", >>grpcallid>>" + grpcallid);


                CutoffConfigVO cutOffVO = null;
                 System.out.println("[ECOM-WAR] RouteAjax   pluginlis===> " + pluginlis);
                if (pluginlis != null && pluginlis.contains("BEC")) {
                    chkbkgcutoff = true;
                    cutlis = bookingObj.getCutOffConfigDetails(grpcallid, pol, pod);
                     System.out.println("[ECOM-WAR] RouteAjax   cutlis====> " + cutlis);
                    if (cutlis != null && cutlis.size() > 0) {
                        cutmap = (Map) cutlis.get(0);
//                         System.out.println("[ECOM-WAR] RouteAjax   cutmap====> " + cutmap.size());
                        if (cutmap != null && cutmap.size() > 0) {
                            cutOffVO = new CutoffConfigVO();
                            if (cutmap.containsKey("Booking Cutoff")) {
                                cutOffVO.setBkgcutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Booking Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("Documentation Cutoff")) {
                                cutOffVO.setDoccutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Documentation Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("VGM Cutoff")) {
                                cutOffVO.setVgmcutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("VGM Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("BBRORO Cutoff")) {
                                cutOffVO.setBbrorocutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("BBRORO Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("Hazmat Cutoff")) {
                                cutOffVO.setHazcutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Hazmat Cutoff").toString().trim())));
                            }
                            if (cutmap.containsKey("Port Entry Cutoff")) {
                                cutOffVO.setPortentrycutoff(Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cutmap.get("Port Entry Cutoff").toString().trim())));
                            }
                        }
                    }
                }
                if (!chkbkgcutoff) {
                    cutOffVO = ecomCntrlObj.getRoutingCutOffDetails(cutOffPort, cutOffTerminal);
                    //  System.out.println("[ECOM-WAR] RouteAjax   >>doc date>>" + DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getDocClosing())));
                    if (cutOffVO != null) {
                        cutOffVO.setCfsGateClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getCfsGateClosing())));
                        cutOffVO.setDocClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getDocClosing())));
                        cutOffVO.setVgmClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getVgmClosing())));
                        cutOffVO.setEdiDecClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getEdiDecClosing())));
                        cutOffVO.setFullContClosing(DateConversion_New.cutOffCalc(cutOffDate, Integer.parseInt(cutOffVO.getFullContClosing())));
                    }
                }
                if (cutOffVO == null) {
                    cutOffVO = new CutoffConfigVO();
                }
                request.setAttribute("ROUTECUTOFFVO", cutOffVO);
                request.setAttribute("ISBKGCUTOFF", chkbkgcutoff);
                request.setAttribute("ROUTEVERSION", "2");
                session.setAttribute("RouteIndex_New", id);
                RequestDispatcher rd = request.getRequestDispatcher("/eBook_RoutingGrid.jsp");
                rd.forward(request, response);
            } else if ("version".equalsIgnoreCase(type)) {
                String version;
                String strDbFormat = "yyyy-MM-dd HH:mm:ss";
                String currentFormat = "yyyy-MM-dd";
                SimpleDateFormat source = new SimpleDateFormat(currentFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                String feederCutOffDate = (String) context.getAttribute(GeneralConf.V1FEEDERCUTOFFDATE);
//                 System.out.println("[ECOM-WAR] RouteAjax   >>>route version>>" + type);
                String depdate = request.getParameter("depdate");
                if (feederCutOffDate != null && feederCutOffDate.length() > 0) {
                    feederCutOffDate = traget.format(source.parse(feederCutOffDate));
                }
                 System.out.println("[ECOM-WAR] RouteAjax   >>depdate>>>>>>>" + depdate);
                if (depdate != null && depdate.length() > 0) {
                    depdate = DateConversion_New.convertDefaultDBFormat(depdate);
                }
                 System.out.println("[ECOM-WAR] RouteAjax   >>depdate>>>" + depdate);
                String service = request.getParameter("service");
                String vessel = request.getParameter("vessel");
                String voyage = request.getParameter("voyage");
                String bound = request.getParameter("bound");
                String svcType = request.getParameter("svcType");
                String bkgType = request.getParameter("bkgType");
                 System.out.println("[ECOM-WAR] RouteAjax   >>svcType>>>" + svcType);
                String appVersion = Validation.checkNull(request.getParameter("appVersion"));
                 System.out.println("[ECOM-WAR] RouteAjax   >>feederCutOffDate>>" + feederCutOffDate);
                version = ecomCntrlObj.getScheuleVersion(service, vessel, voyage, bound, depdate, svcType, feederCutOffDate);
//                 System.out.println("[ECOM-WAR] RouteAjax   >>>version>>>" + version);

                /**
                 * String range = ""; String arr[] = version.split("~~");
                 *
                 * if (arr != null && arr.length == 2) {
                 *  System.out.println("[ECOM-WAR] RouteAjax   >>arr[0]>>" + arr[0]);
                 *  System.out.println("[ECOM-WAR] RouteAjax   >>arr[1]>>" + arr[1]); version =
                 * Validation.checkNull(arr[0]).trim(); range =
                 * Validation.checkNull(arr[1]).trim();
                 *
                 * if ("amendbkg".equalsIgnoreCase(bkgType)) { if
                 * ("M".equalsIgnoreCase(svcType) &&
                 * !version.equalsIgnoreCase(appVersion)) { if
                 * ("1".equalsIgnoreCase(appVersion)) { version = "Kindly choose
                 * the schedule which has voyage range less than " + range + "
                 * for mainline schedule. For more assistance kindly contact
                 * your local agency."; } else { version = "Kindly choose the
                 * schedule which has voyage range greater than or equal to " +
                 * range + " for mainline schedule. For more assistance kindly
                 * contact your local agency."; } } else if
                 * ("F".equalsIgnoreCase(svcType) &&
                 * !version.equalsIgnoreCase(appVersion)) { if
                 * ("1".equalsIgnoreCase(appVersion)) { version = "Kindly choose
                 * the schedule which has departure date less than " + range + "
                 * for feeder schedule. For more assistance kindly contact your
                 * local agency."; } else { version = "Kindly choose the
                 * schedule which has departure date greater than or equal to "
                 * + range + " for feeder schedule. For more assistance kindly
                 * contact your local agency."; } } }*
                 *
                 *
                 * }
                 */
                if ("amendbkg".equalsIgnoreCase(bkgType)) {
                    if (!version.equalsIgnoreCase(appVersion)) {
                        version = "This booking can't be process using selected schedule due to system upgarde."
                                + " Kindly select different schedule (or) create a new booking to use this schedule.";
                    }
                }
                 System.out.println("[ECOM-WAR] RouteAjax   >>version in routeajax==>" + version);
                out.print(version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
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
