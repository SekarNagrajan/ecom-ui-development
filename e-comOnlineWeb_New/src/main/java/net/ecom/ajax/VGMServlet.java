/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomVGMBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.Validation;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1VGMBeanRemote;
import net.ecom.vo.VGMCommonVO;

/**
 *
 * @author deepa.p
 */
public class VGMServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    EcomVGMBeanRemote vgmBean = EJBLookUp.getESLVGMBeanLookUp();
    EcomV1VGMBeanRemote v1VgmBean = EJBLookUp.getV1VGMBeanLookUp();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        // System.out.println("[ECOM-WAR] VGMServlet  >>>session>>" + session);
        ServletContext contexts = getServletContext();
        String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
        try {
            if (session != null) {
                if (session.getId().equals((String) session.getAttribute("systemid"))) {
                    String cust_code = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
//                     System.out.println("[ECOM-WAR] VGMServlet  servelet called...");
                    String type = request.getParameter("type");
                     System.out.println("[ECOM-WAR] VGMServlet  type"+type);
                    if ("tareWT".equalsIgnoreCase(type)) {
                        String containerno = request.getParameter("containerNo");
                        String eqpType = request.getParameter("eqpType");
                        String result = vgmBean.getTareWeight(containerno, eqpType);
//                         System.out.println("[ECOM-WAR] VGMServlet  vgmtare------>" + result);
                        DecimalFormat df = new DecimalFormat("0.00");
                        result = result != null && result.length() > 0 ? df.format(Double.parseDouble(result)) : result.equals("0.0") ? "0.00" : "";
                         System.out.println("[ECOM-WAR] VGMServlet  >vgmtare()>>>>>conv" + result);
                        out.print(result);
                    } else if ("getversionno".equalsIgnoreCase(type)) {
                        String result = "2";
                        String searchType = request.getParameter("submissionBy");
                        String referenceNo = request.getParameter("referenceno");
                        if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                            result = v1VgmBean.getBookingVersion(searchType, referenceNo);
                        }
                         System.out.println("[ECOM-WAR] VGMServlet  >>appversion>>>for vgm>>" + result);
                        out.print(result);

                    } else if ("loadconfirm".equalsIgnoreCase(type)) {
                        String result = "success";
                        String bookno = request.getParameter("bookno");
                        String blno = request.getParameter("blno");
                        String appversion = request.getParameter("appversion");
                        String searchType = request.getParameter("submissionBy");
                        String result1 = null;
                        if ("1".equalsIgnoreCase(appversion)) {
                            result1 = v1VgmBean.checkLoadConfirm(bookno, blno, searchType, cust_code);
                        } else {
                            result1 = vgmBean.checkLoadConfirm(bookno, blno, searchType, cust_code);
                        }

                        if ("NOTVALIDBOOKING".equalsIgnoreCase(result1)) {
                            result = "notvalid";
                        } else if ("YES".equalsIgnoreCase(result1)) {
                            result = "failure";
                        }
                        out.print(result);
                    } else if ("search".equalsIgnoreCase(type)) {
                        String bookno = request.getParameter("booknos");
                        String searchType = request.getParameter("submissionBy");
//                         System.out.println("[ECOM-WAR] VGMServlet  >>>>" + bookno);
                        String appversion = request.getParameter("appversion");
                         System.out.println("[ECOM-WAR] VGMServlet  >>>searchType>>" + searchType+",bookno:"+bookno);
                        request.setAttribute("VGMCONTAINERLIST", null);
                        VGMCommonVO vgmVO = null;
                        if ("1".equalsIgnoreCase(appversion)) {
                            vgmVO = v1VgmBean.getVGMDetails(bookno, searchType);
                        } else {
                            vgmVO = vgmBean.getVGMDetails(bookno, searchType);
                        }
//                         System.out.println("[ECOM-WAR] VGMServlet  >>>>" + vgmVO);
                        boolean isMaskPort = MaskPort.isMaskPorts(vgmVO.getOrigin() + "," + vgmVO.getDestination() + "," + vgmVO.getLoadPortId() + "," + vgmVO.getDischargePortId(), contexts);
                        if (isMaskPort && vgmVO != null && (cust_code.length() > 0 || (!"D".equalsIgnoreCase(vgmVO.getVgmStatus())))) {
                             System.out.println("[ECOM-WAR] VGMServlet  >apname>>>>" + vgmVO.getApName());
                            
                            // Swap actual ports to mask ports for VGM fields
                            String maskedOrigin = MaskPortUtil.swapactualporttomaskport(vgmVO.getOrigin());
                            String maskedDestination = MaskPortUtil.swapactualporttomaskport(vgmVO.getDestination());
                            String maskedLoadPort = MaskPortUtil.swapactualporttomaskport(vgmVO.getLoadPortId());
                            String maskedDischargePort = MaskPortUtil.swapactualporttomaskport(vgmVO.getDischargePortId());
                            
                            vgmVO.setOrigin(maskedOrigin);
                            vgmVO.setDestination(maskedDestination);
                            vgmVO.setLoadPortId(maskedLoadPort);
                            vgmVO.setDischargePortId(maskedDischargePort);
                            
                            // System.out.println("[ECOM-WAR] VGMServlet Masked ports - Origin: " + maskedOrigin 
                            //     + ", Destination: " + maskedDestination 
                            //     + ", POL: " + maskedLoadPort 
                            //     + ", POD: " + maskedDischargePort);
                            
                            request.setAttribute("VGMCONTAINERLIST", vgmVO);
                            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/VGM/EcomVGMView.jsp");
                            rd.forward(request, response);
                        } else {
                            out.print("Invalid Booking Number.");
                        }
                    } else if ("containernocheck".equalsIgnoreCase(type)) {
                        String reference = request.getParameter("reference");
                        String eqpType = request.getParameter("eqpList");
                        String socType = request.getParameter("socList");
                        String searchType = request.getParameter("submissionBy");
                        String appversion = request.getParameter("appversion");
                        Map<String, String> map = new HashMap<String, String>();
                        List<String> list = new ArrayList<String>();
                        String referenceArr[] = reference.split("~~");
                        String eqpArr[] = eqpType.split("~~");
                        String socArr[] = socType.split("~~");
                        String refno = null;
                        int j = 0;
                        if (referenceArr != null && referenceArr.length >= 2) {
                            refno = referenceArr[0];
                            for (int i = 1; i < referenceArr.length; i++) {
                                list.add(referenceArr[i]);
//                                 System.out.println("[ECOM-WAR] VGMServlet  >>socArr[j]>>" + socArr[j]);
//                                 System.out.println("[ECOM-WAR] VGMServlet  >>referenceArr[i]>>" + referenceArr[i]);
//                                 System.out.println("[ECOM-WAR] VGMServlet  >>eqpArr[j]>>" + eqpArr[j]);
                                if (!"Y".equalsIgnoreCase(socArr[j])) {
                                    map.put(referenceArr[i], eqpArr[j]);
                                }
                                j++;
                            }

                        }
                         System.out.println("[ECOM-WAR] VGMServlet  >>>eqpcontainer>>" + map);
                        String result = null;
                        if (map != null && map.size() > 0) {
                            if ("1".equalsIgnoreCase(appversion)) {
                                result = v1VgmBean.checkDuplicateContainers(list, refno, searchType);
                            } else {
                                result = vgmBean.checkDuplicateContainers(list, refno, searchType, map);
                            }
                        } else {
                            result = "Valid";
                        }
                        out.print(result);
                    } else if ("validatecontainer".equalsIgnoreCase(type)) {

                        String result = null;
                        String eqptype = request.getParameter("eqptype");
                        String containerno = request.getParameter("containerno");
                        String appversion = request.getParameter("appversion");
//                         System.out.println("[ECOM-WAR] VGMServlet  >>eqptype>" + eqptype);
                         System.out.println("[ECOM-WAR] VGMServlet  >>containerno>" + containerno+",eqptype:"+eqptype);
                        try {
                            result = vgmBean.checkVGMContainerNo(eqptype, containerno);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        out.print(result);
                    }
                } else {
                    //RequestDispatcher rd = request.getRequestDispatcher("/SessionExpired.jsp");
                    //rd.forward(request, response);
                    out.print("SessionExpired");
                }
            } else {
                //RequestDispatcher rd = request.getRequestDispatcher("/SessionExpired.jsp");
                //rd.forward(request, response);
                out.print("SessionExpired");
            }
        } finally {
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
