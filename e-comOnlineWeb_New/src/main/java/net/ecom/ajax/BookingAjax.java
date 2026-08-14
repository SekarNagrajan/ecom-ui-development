/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.vo.BkgContainerHaulageDetails;
import net.ecom.vo.CommoditySearchVO;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.ReturnVO;
import org.json.JSONObject;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "BookingAjax", urlPatterns = {"/BookingAjax"})
public class BookingAjax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    EcomBookingBeanRemote bookbean = EJBLookUp.getBookingBeanLookUp();
    EcomV1BookingBeanRemote v1BkgObj = EJBLookUp.getV1BKGBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String type = request.getParameter("type");
            System.out.println("[ECOM-WAR] BookingAjax >>type>>" + type);
            if ("cancelbooking".equalsIgnoreCase(type)) {
                String ecomBookNo = request.getParameter("ecombookno");
                String lrpBookNo = request.getParameter("lrpbookno");
                String appversion = request.getParameter("appversion");
                String result = null;         
                    if ("1".equalsIgnoreCase(appversion)) {
                        result = v1BkgObj.cancelEcomV1Booking(ecomBookNo, lrpBookNo);
                    } else {
                        result = bookbean.cancelEcomBooking(ecomBookNo, lrpBookNo);
                    }
                out.print(result);
            } else if ("validateCancellrpbooking".equalsIgnoreCase(type)) {
                String lrpBookNo = request.getParameter("lrpbookno");
                String validationtype = request.getParameter("validationtype");
                String disableConfirmBkgValidation = (String) context.getAttribute(GeneralConf.Disable_ConfirmBKG_Validation);
                String result = null;
                result = bookbean.validateCancelLRPBooking(lrpBookNo, validationtype,disableConfirmBkgValidation);
                out.print(result);
            } else if ("cancellrpbooking".equalsIgnoreCase(type)) {
                String lrpBookNo = request.getParameter("lrpbookno");
                String reason = request.getParameter("reason");
                String ebookno = request.getParameter("ebookno");
                String canceluser = request.getParameter("canceluser");
                String frommail = (String) context.getAttribute("clientfrommail");
                String client_code = (String) context.getAttribute("client_code");
                String useremail = (String) request.getSession().getAttribute("SESSION_EMAIL");
                String initiatorID = "ECOM-BKG";
                String result = null, message = null;
                ReturnVO returnVO = bookbean.cancelLRPBooking(lrpBookNo, canceluser, reason, ebookno);
                try {
                    message = returnVO.getStrMailMsg();
                    Mailer mail = new Mailer();
                    mail.setTo(returnVO.getStrToMailId());
                    mail.setSubject(returnVO.getStrSubject());
                    mail.setMessage(message);
                    if (message != null && message.trim().length() > 0) {
                        ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                    }
                    //
                    //
                    message = returnVO.getAdmMailMsg();
                    mail = new Mailer();
                    mail.setTo(returnVO.getAdmToMailId());
                    mail.setSubject(returnVO.getAdmSubject());
                    mail.setMessage(message);
                    if (message != null && message.trim().length() > 0) {
                        ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result = returnVO.getStrDisplayMsg();
                out.print(result);
            } else if ("commoditysearchjsp".equalsIgnoreCase(type)) {
                System.out.println("[ECOM-WAR] BookingAjax commoditysearchjsp");
                String comId = request.getParameter("comId");
                String hsCodeId = request.getParameter("hsCodeId");
                String rowid = request.getParameter("rowid");
                String screenname = request.getParameter("screenname");
                request.setAttribute("BKGcomid", comId);
                request.setAttribute("BKGhscodeid", hsCodeId);
                request.setAttribute("BKGrowid", rowid);
                request.setAttribute("screenname", screenname);
                RequestDispatcher rd = request.getRequestDispatcher("/CommoditySearch.jsp");
                rd.forward(request, response);
            } else if ("getCommodityName".equalsIgnoreCase(type)) {
                String code = request.getParameter("code");
                Hashtable comlist = (Hashtable) context.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                String name = (String) comlist.get(code);
                out.print(name);
            } else if ("seachcommodity".equalsIgnoreCase(type)) {
                System.out.println("[ECOM-WAR] BookingAjax seachcommodity...");
                String value = request.getParameter("value");
                String columnName = request.getParameter("columnName");
                String constraint = request.getParameter("constraint");
                String groupcode= request.getParameter("groupcode");
                List<CommoditySearchVO> list = bookbean.getComdtySearchList(value, columnName, constraint,groupcode);
                request.setAttribute("COMMODITYSEARCHVIEW", list);
                RequestDispatcher rd = request.getRequestDispatcher("/CommoditySearchView.jsp");
                rd.forward(request, response);
            } else if ("HaulageDocType".equalsIgnoreCase(type)) {
                String origincode = request.getParameter("origincode");
                String custcode = request.getParameter("custcode");
                String docType = bookbean.getBookingHaulageDocType(origincode, custcode);
                out.print(docType);
            } else if ("HaulageCustDetails".equalsIgnoreCase(type)) {
                String custcode = (String) request.getSession().getAttribute("SESSION_USERCODE");
                List custlis = bookbean.getBkgHaulCustDetails(custcode);
                request.setAttribute("type", "customer");
                request.setAttribute("HaulageCustomerDetails", custlis);
                RequestDispatcher rd = request.getRequestDispatcher("/BookingHaulageCustomerDetails.jsp");
                rd.forward(request, response);
            } else if ("HaulageContactDetails".equalsIgnoreCase(type)) {
                String custcode = request.getParameter("custcode");
                List contlis = bookbean.getBkgHaulContactDetails(custcode);
                request.setAttribute("type", "contact");
                request.setAttribute("HaulageContactDetails", contlis);
                RequestDispatcher rd = request.getRequestDispatcher("/BookingHaulageCustomerDetails.jsp");
                rd.forward(request, response);
            } else if ("HaulageTMSClientDetails".equalsIgnoreCase(type)) {
                String loadport = request.getParameter("loadport");
                Map tmsmap = bookbean.getBkgHaulTMSClientDetails(loadport);
                out.print(new JSONObject(tmsmap));
            } else if ("HaulageFirstContDetails".equalsIgnoreCase(type)) {
                String custcode = request.getParameter("custCode");
                String result = null;
                List contlis = bookbean.getBkgHaulContactDetails(custcode);
                CustomerDetailsVO custvo = null;
                if (contlis != null && contlis.size() > 0) {
                    custvo = (CustomerDetailsVO) contlis.get(0);
                    result = custvo.getName() + "~~" + custvo.getPhoneNo() + "~~" + custvo.getEmail();
                    out.println(result);
                }
            } else if ("porttypevalue".equalsIgnoreCase(type)) {
                String result = null, portPairType = null;
                String origin = null, delivery = null;
                String org = request.getParameter("org");
                String dest = request.getParameter("dest");
                if (org.contains("-")) {
                    origin = org.substring(0, org.indexOf('-')).trim();
                }
                if (dest.contains("-")) {
                    delivery = dest.substring(0, dest.indexOf('-')).trim();
                }
                String porttypeorg = bookbean.getPortType(origin);
                String porttypedest = bookbean.getPortType(delivery);
                if (porttypeorg != null && porttypeorg.length() > 0 && porttypedest != null && porttypedest.length() > 0) {
                    portPairType = (new StringBuilder()).append(porttypeorg).append(porttypedest).toString();
                    System.out.println("[ECOM-WAR] BookingAjax portPairType=====>" + portPairType);
                }
                Map<String, String> carriageContractMap = (Map<String, String>) context.getAttribute(GeneralConf.CarriageContractBasedOnPorts);
                System.out.println("[ECOM-WAR] BookingAjax >>>carriageContractMap>>>" + carriageContractMap);
                if (carriageContractMap != null) {
                    if (portPairType.equals("II")) {
                        result = carriageContractMap.get("II");
                        out.print(result);
                    } else if (portPairType.equals("PP")) {
                        result = carriageContractMap.get("PP");
                        out.print(result);
                    } else if (portPairType.equals("PI")) {
                        result = carriageContractMap.get("PI");
                        out.print(result);
                    } else if (portPairType.equals("IP")) {
                        result = carriageContractMap.get("IP");
                        out.print(result);
                    }
                }
            } else if ("bookingHaulageDetails".equalsIgnoreCase(type)) {

                String bookno = request.getParameter("bookno");
                List<BkgContainerHaulageDetails> bkgHaulageDetails = bookbean.getBookingHaulageDetails(bookno);
                System.out.println("[ECOM-WAR] BookingAjax bkgHaulageDetails size " + bkgHaulageDetails);
                request.setAttribute("bkgHaulageDetails", bkgHaulageDetails);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Booking/BookingHaulageDetails.jsp");
                rd.forward(request, response);

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
