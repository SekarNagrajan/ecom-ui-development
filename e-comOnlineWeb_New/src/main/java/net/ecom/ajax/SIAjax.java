/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.common.CommonMail;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.formbeans.SIEditForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1SIBeanRemote;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.PortVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "SIAjax", urlPatterns = {"/SIAjax"})
public class SIAjax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    EcomShippingInstructionBeanRemote sibeanobj = EJBLookUp.getSIBeanRemoteLookUp();
    EcomV1SIBeanRemote v1SIObj = EJBLookUp.getV1SIBeanLookUp();
    EComCntrlBeanRemote cntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomShippingInstructionBeanRemote siObj = EJBLookUp.getSIBeanRemoteLookUp();

    ServletContext context;

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            context = config.getServletContext();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            PrintWriter out = response.getWriter();
            String type = request.getParameter("type");
            HttpSession session = request.getSession();
             System.out.println("[ECOM-WAR] SIAjax    type"+type);
            if ("getsideclinedmsg".equalsIgnoreCase(type)) {
                String siNo = request.getParameter("sino");
                String result = sibeanobj.getSIDeclinedRemarks(siNo);
                out.print(result);
            }
            if ("cancelsi".equalsIgnoreCase(type)) {
                String siNo = request.getParameter("sino");
                String lrpBookNo = request.getParameter("lrpbookno");
                String appversion = request.getParameter("appversion");
                String result = null;
                if ("1".equalsIgnoreCase(appversion)) {
                    result = v1SIObj.cancelSubmittedV1SI(siNo, lrpBookNo);
                } else {
                    result = sibeanobj.cancelSubmittedSI(siNo, lrpBookNo);
                }
                out.print(result);
            }
            if ("esimultifilingSearch".equalsIgnoreCase(type)) {
                String apcustcode = request.getParameter("apcustcode");
                List<CustomerProfile> list = siObj.getSIMultifilingCustomerInfo(apcustcode, "AP");
                request.setAttribute("MULTIFILINGSEARCH", list);
                RequestDispatcher rd = request.getRequestDispatcher("MultiFilingSearch.jsp");
                rd.forward(request, response);
            }
            if ("SICHANGEREQMAIL".equalsIgnoreCase(type)) {
                String agencymail = (String) context.getAttribute("clientfrommail");
                String client_code = (String) context.getAttribute("client_code");
                String userid = (String) session.getAttribute("SESSION_USERNAME");
                //String ccMail = request.getParameter("ccmail");
                String toMail = request.getParameter("tomail");
                String subject = request.getParameter("subjectmail");
                String message = request.getParameter("bodymail");
                String bookno = request.getParameter("bookno");
                String blno = request.getParameter("blno");
                String initiatorID = "ECOM-SI";
                 System.out.println("[ECOM-WAR] SIAjax    message=========> " + message);
                String requrl = request.getRequestURL().toString();
                String requri = request.getRequestURI();
                String contextpath = request.getContextPath();
                requrl = requrl.replace(requri, "");
                if (blno != null && Validation.checkNull(blno).length() > 0) {
                    requrl = requrl + contextpath + "/SIAjax?type=DeleteSILock&sourceType=BL&reqno=" + blno;
                } else {
                    requrl = requrl + contextpath + "/SIAjax?type=DeleteSILock&sourceType=BKG&reqno=" + bookno;
                }
                 System.out.println("[ECOM-WAR] SIAjax    requrl=========> " + requrl);
                message = message + "<hr><label style='font-size: 20px;margin:0;padding:3px;width:30%;'>After completing the requested changes,click the finish button <a href='" + requrl + "' target='_blank' style='margin-left: 20px;background-color: #4CAF50;color: white;padding: 6px 15px;border-radius: 4px;border: navajowhite;text-decoration: none;'>Finish</a></label>";
                Mailer mail = new Mailer();
                //mail.setCc(null);
                mail.setTo(toMail);
                mail.setSubject(subject);
                mail.setMessage(message);
                try {
                    cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                cntrlObj.insertSILock(bookno, blno, userid);
                out.print("Success");
            } else if ("DeleteSILock".equalsIgnoreCase(type)) {
                String reqno = (String) request.getParameter("reqno");
                String sourceType = (String) request.getParameter("sourceType");
                 System.out.println("[ECOM-WAR] SIAjax    sourceType==> " + sourceType + "&reqno==> " + reqno);
                out.print(cntrlObj.deleteSILock(sourceType, reqno));
            } else if ("ThirdParty".equalsIgnoreCase(type)) {
                String SIfrieght = (String) request.getParameter("SIfrieght");
                String SIPayor = (String) request.getParameter("SIPayor");
                String bookno = (String) request.getParameter("lrpbookno");
                List<PortVO> unlis = sibeanobj.getThirdPartyLocation(bookno);

                request.setAttribute("SIfrieght", SIfrieght);
                request.setAttribute("SIPayor", SIPayor);
                request.setAttribute("UNPortList", unlis);
                RequestDispatcher rd = request.getRequestDispatcher("UNPortList.jsp");
                rd.forward(request, response);

            } else if ("PrepaidCollect".equalsIgnoreCase(type)) {
                String SIfrieght = (String) request.getParameter("SIfrieght");
                String SIPayor = (String) request.getParameter("SIPayor");
                String bookno = (String) request.getParameter("lrpbookno");
                String output = sibeanobj.getPrepaidCollectLocation(bookno, SIfrieght);
                out.print(output);
            } else if ("SIContainernovalidation".equalsIgnoreCase(type)) {
                String bookno = (String) request.getParameter("lrpbookno");
                String containerlisstr = (String) request.getParameter("containerlisstr");
                String output = sibeanobj.checkContainerValidation(bookno, containerlisstr);
                out.print(output);
            } else if ("PrintSIData".equalsIgnoreCase(type)) {
                HashMap masterCache = (HashMap) session.getAttribute("SIPrintCache");
                String sino = (String) session.getAttribute("SIPrint_SINo");
                String bookno = (String) session.getAttribute("SIPrint_BookNo");
                SIPartiesVO parties_routing = (SIPartiesVO) session.getAttribute("SIPrint_routing");
                SIEditForm siform = (SIEditForm) session.getAttribute("SIPrint_form");
                String imagepath = (String) context.getAttribute("Imagepath");
                String logo_name = (String) context.getAttribute(GeneralConf.Logo_Name);
                Map<SIMasterVO, SIConatainerValueVO> siCntInfoHashObj = (Map) session.getAttribute("containerListForSI");
                if (siCntInfoHashObj != null && siCntInfoHashObj.size() > 0) {
                    Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = siCntInfoHashObj.entrySet();
                    for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                        SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                        ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                        if (cntList != null && cntList.size() > 0) {
                            for (int j = 0; j < cntList.size(); j++) {
                                SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                siCntDetVo.setStrCommodity(siCntDetVo.getCommodityName());
                                Hashtable pack_table = (Hashtable) session.getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                                String dtPkg = siCntDetVo.getStrPkg();
                                dtPkg = dtPkg != null && dtPkg.length() > 0 ? Validation.checkNull((String) pack_table.get(dtPkg)) : dtPkg;
                                siCntDetVo.setStrPkg(dtPkg);

                            }
                        }
                    }
                }
                String output = new CommonMail().SIBLcommonMailMail(imagepath, logo_name, sino, masterCache, bookno, "", siform, parties_routing, siCntInfoHashObj, "SI");

                out.print(output);
            }
            if("isafricaReqsinlandport".equalsIgnoreCase(type)){
                String lrpbookno=(String)request.getParameter("lrpbookno");
                System.out.println("lrpbookno"+lrpbookno);
                Boolean isInlandPort=sibeanobj.checkIsInlandPort(lrpbookno);
                System.out.println("isInlandPort"+isInlandPort);
                out.print(isInlandPort);
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
