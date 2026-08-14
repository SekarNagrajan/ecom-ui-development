/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.RegistrationVO;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "RegistrationAjax", urlPatterns = {"/RegistrationAjax"})
@MultipartConfig(maxFileSize = 4194304, maxRequestSize = 4194304, fileSizeThreshold = 0)
public class RegistrationAjax extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String firsttimesplit = null, secondparttimeslit = null;
            String type = request.getParameter("type");
            System.out.println("[ECOM-WAR] RegistrationAjax  >>type>>" + type);
            HttpSession session = request.getSession();
            if ("updateccmail".equalsIgnoreCase(type)) {
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                String loginid = request.getParameter("loginid");
                String loginname = (String) session.getAttribute("SESSION_LOGINNAME");

                if ("YES".equalsIgnoreCase(strIsViaLogin) && Validation.checkNull(loginid).equalsIgnoreCase(loginname)) {

                    String ccMailId = request.getParameter("ccmailid");
//                 System.out.println("[ECOM-WAR] RegistrationAjax  >>loginid>>" + loginid);
                    System.out.println("[ECOM-WAR] RegistrationAjax  >>ccMailId.." + ccMailId + ",loginid:" + loginid);
                    String resutl = ecomreg.updateCCMailId(loginid, ccMailId);
                    if ("Updated".equalsIgnoreCase(resutl)) {
                        session.setAttribute("SESSION_USER_CC_MAILID", ccMailId);
                    }
                    out.print(resutl);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }

            }

            if ("updatetimezone".equalsIgnoreCase(type)) {
                String loginid = request.getParameter("loginid");
                String updatedtimeZone = request.getParameter("timezone");
//                 System.out.println("[ECOM-WAR] RegistrationAjax  >>loginid>>" + loginid);
                System.out.println("[ECOM-WAR] RegistrationAjax  >>timezone.." + updatedtimeZone + ",loginid:" + loginid);
                String resultt = ecomreg.updateTimeZone(loginid, updatedtimeZone);
                if ("Updated".equalsIgnoreCase(resultt)) {
                    if (updatedtimeZone != null && updatedtimeZone.length() > 0) {
                        String aftrTimesplit[] = updatedtimeZone.split("~~");
                        if (aftrTimesplit.length > 0) {
                            firsttimesplit = Validation.checkNull(aftrTimesplit[0]).trim();
                            secondparttimeslit = Validation.checkNull(aftrTimesplit[1]).trim();
                            session.setAttribute("USERPREFERREDTIMEZONE", firsttimesplit);
                        }

                    }

                }
                out.print(resultt);

            }
            if ("getcompanydetails".equalsIgnoreCase(type)) {
                String custcode = Validation.checkNull(request.getParameter("custcode"));
                RegistrationVO regVo = ecomreg.getCompanyDetails(custcode);
                session.setAttribute("SESSION_BACKUP_REGVO", regVo);
                if (regVo != null) {
                    String details = regVo.getCompanyDetails();
                    out.println("valid~~" + details);
                }
            }
            if ("getcountryfileupload".equalsIgnoreCase(type)) {
//                 System.out.println("[ECOM-WAR] RegistrationAjax  getcountryfileupload------------------");
                String country = Validation.checkNull(request.getParameter("countrycode"));
                ArrayList<RegistrationVO> regVoList = ecomreg.getCountryFileDetails(country);
                JSONArray fieldDataArray = new JSONArray();
                if (regVoList != null) {
                    for (RegistrationVO fileuploadlist : regVoList) {
                        JSONObject fieldData = new JSONObject();
                        fieldData.put("fieldname", fileuploadlist.getFieldname());
                        fieldData.put("mandatory", fileuploadlist.getMandatory());
                        fieldDataArray.put(fieldData);
                    }
                    out.print(fieldDataArray);
                }
            }
            if ("uploadfileinregistration".equalsIgnoreCase(type)) {
//                 System.out.println("[ECOM-WAR] RegistrationAjax  uploadfileinregistration------------------");
                Collection<Part> filePart = request.getParts();
                System.out.println("[ECOM-WAR] RegistrationAjax  filePart=> " + filePart);
                ArrayList filenamelis = new ArrayList();
                ArrayList<InputStream> inputsreamlis = new ArrayList();
                if (filePart != null) {
                    for (Part filePart1 : filePart) {
                        String fileName = Paths.get(filePart1.getSubmittedFileName()).getFileName().toString();
                        InputStream fileContent = filePart1.getInputStream();
                        filenamelis.add(fileName);
                        inputsreamlis.add(fileContent);
                    }
                    System.out.println("[ECOM-WAR] RegistrationAjax  filenamelis=> " + filenamelis);
                    session.setAttribute("REG_FILE_Name_LIST", filenamelis);
                    session.setAttribute("REG_FILE_INPUT_STREAM", inputsreamlis);
                }
                out.print("success");
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
