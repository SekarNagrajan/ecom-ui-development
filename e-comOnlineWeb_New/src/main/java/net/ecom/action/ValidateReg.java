/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ReturnVO;

public class ValidateReg extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    EcomRegistrationBeanRemote ecomCntrlObj = EJBLookUp.getRegistrationBeanLookUp();
    EComCntrlBeanRemote cntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
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
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String client_code = (String) context.getAttribute("client_code");
            String carrierCode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
            String agencymailplugin = (String) context.getAttribute(GeneralConf.Enable_Registration_Agency_Mail);
            String disableAutoRegVerification = (String) context.getAttribute(GeneralConf.disable_autoreg_verification);
            String enableRegAutoProcess = (String) context.getAttribute(GeneralConf.Enable_Reg_Auto_Process);
            String uniqueId = request.getParameter("uniqueId");
            String verifyAgencyMailPlugin = (String) context.getAttribute(GeneralConf.Enable_Verify_Agency_Mail);
            String Multiple_agencies_location = (String) context.getAttribute(GeneralConf.Multiple_agencies_single_location);
            String agencymail = (String) context.getAttribute("clientfrommail");
            System.out.println("[ECOM-WAR] ValidateReg uniqueId: " + uniqueId);
            String initiatorID = "ECOM-REG";
            ReturnVO rvo = ecomCntrlObj.validateRegUserbymail(uniqueId, client_code, verifyAgencyMailPlugin, disableAutoRegVerification, enableRegAutoProcess, Multiple_agencies_location);
            System.out.println("[ECOM-WAR] ValidateReg ValidateReg ==  uniqueId ==> " + rvo.getStrDisplayMsg());
            String result = rvo.getStrDisplayMsg();
            System.out.println("[ECOM-WAR] ValidateReg result--------" + result);
            if ("successautoreg".equalsIgnoreCase(result)) {

                Mailer mail = new Mailer();
                //  mail.setFrom(rvo.getStrFromMailId());
                mail.setTo(rvo.getStrToMailId());
                System.out.println("[ECOM-WAR] ValidateReg >>rvo.getStrToMailId()>>" + rvo.getStrToMailId() + ">>> rvo.getLoginName()>>" + rvo.getLoginName());
                mail.setSubject(rvo.getStrSubject());
                String msg = rvo.getStrMailMsg(); 
                msg = msg.replace("@customer", rvo.getFirstName());
                msg = msg.replace("@userid", rvo.getLoginName());
                msg = msg.replace("@password", rvo.getPassword());
                msg = msg.replace("@client", "");
                mail.setMessage(msg);
                try {
                    cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                }catch (Exception exp) {
                    exp.printStackTrace();
                }

                System.out.println("[ECOM-WAR] ValidateReg >>rvo.getAdmToMailId>>>>>>" + rvo.getAdmToMailId() + "--rvo.getAdmMailMsg()---" + rvo.getAdmMailMsg());

                if (rvo.getAdmToMailId() != null && rvo.getAdmToMailId().length() > 0 && rvo.getAdmMailMsg() != null && rvo.getAdmMailMsg().length() > 0) {
                    Mailer maill = new Mailer();
                    maill.setTo(rvo.getAdmToMailId());
                    maill.setSubject(rvo.getAdmSubject());
                    String msgg = rvo.getAdmMailMsg();
                    msgg = msgg.replaceAll("@userid", rvo.getLoginName());
                    msgg = msgg.replaceAll("@customername", rvo.getFirstName());
                    maill.setMessage(msgg);
                    System.out.println("[ECOM-WAR] ValidateReg Agency mail ----");
                    try {
                        System.out.println("[ECOM-WAR] ValidateReg send mail to agency");
                        cntrlObj.sendMailFromEjb(maill, agencymail, initiatorID, client_code);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }else if ("success".equalsIgnoreCase(result)) {
                Mailer mail = new Mailer();
                //  mail.setFrom(rvo.getStrFromMailId());
                mail.setTo(rvo.getStrToMailId());
                if ("Y".equalsIgnoreCase(agencymailplugin)) {
                    mail.setCc(rvo.getAdmToMailId());
                }
                System.out.println("[ECOM-WAR] ValidateReg >>rvo.getStrToMailId()>>" + rvo.getStrToMailId() + ">>> rvo.getLoginName()>>" + rvo.getLoginName());
                mail.setSubject(rvo.getStrSubject());
                String msg = rvo.getStrMailMsg(); 
                msg = msg.replace("@customer", rvo.getFirstName());
                mail.setMessage(msg);
                try {
                    if(msg != null && msg.length() > 0){
                    cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                    }
                }catch (Exception exp) {
                    exp.printStackTrace();
                }
                System.out.println("[ECOM-WAR] ValidateReg rvo.getAdmToMailId()==> "+rvo.getAdmToMailId() + ">>> rvo.getLoginName()>>" + rvo.getLoginName());
                if ("Y".equalsIgnoreCase(verifyAgencyMailPlugin) && rvo.getAdmToMailId()!=null) {
                    Mailer maill = new Mailer();
                    maill.setTo(rvo.getAdmToMailId());
                    maill.setSubject(rvo.getAdmSubject());
                    String msgs = rvo.getAdmMailMsg(); 
                    msgs = msgs.replace("@company_name", rvo.getCompany());
                    msgs = msgs.replace("@mail_id", rvo.getStrToMailId());
                    msgs = msgs.replace("@contact_num", rvo.getContact());
                    maill.setMessage(msgs);
                    try {
                        cntrlObj.sendMailFromEjb(maill, agencymail, initiatorID, client_code);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }

                }

            }
            String msg = null;
            if (result != null) {
                if (result.equalsIgnoreCase("success")) {
                    request.setAttribute("forwardmessage", (String) context.getAttribute("Registration_Link_Success_Message")); 
                    request.setAttribute("forwardmessagetype", "success");
                } else if (result.equalsIgnoreCase("successautoreg")) {
                    String autoSuccessMsg = (String) context.getAttribute("Registration_Link_Auto_Success_Message");
                    if (autoSuccessMsg != null && autoSuccessMsg.length() > 0) {
                        msg = autoSuccessMsg;
                    } else {
                        msg = "Your registration has been confirmed successfully! <a href='Welcome.do' style='color: #337ab7;text-decoration: underline;'> Click Here</a> to login.";
                    }
                    request.setAttribute("forwardmessagetype", "success");
                    request.setAttribute("forwardmessage", msg);
                } else if (result.equalsIgnoreCase("already")) {
                    request.setAttribute("forwardmessage", "<div style='font-size: 16px; font-weight: bold;'>Your registration has been verified already. Your account will be activated soon.</div> ");
                    if ("GWF".equalsIgnoreCase(carrierCode)) {
                        request.setAttribute("forwardmessage", "<div style='font-size: 16px; font-weight: bold;'>Your registration has been verified already!</div> <div> Kindly contact Great White Fleet.</div>");
                    }
                    request.setAttribute("forwardmessagetype", "failure");
                } else {
                    request.setAttribute("forwardmessage", "<div style='font-size: 16px; font-weight: bold;'>Invalid link! </div> <div> Kindly contact Admin User.</div>");
                    if ("GWF".equalsIgnoreCase(carrierCode)) {
                        request.setAttribute("forwardmessage", "<div style='font-size: 16px; font-weight: bold;'>Invalid link! </div> <div> Kindly contact  Great White Fleet.</div>");
                    }
                    request.setAttribute("forwardmessagetype", "failure");
                }
            } else {
                request.setAttribute("forwardmessage", "<div style='font-size: 16px; font-weight: bold;'>Invalid link! </div> <div> Kindly contact Admin User.</div>");
                if ("GWF".equalsIgnoreCase(carrierCode)) {
                    request.setAttribute("forwardmessage", "<div style='font-size: 16px; font-weight: bold;'>Invalid link! </div> <div> Kindly contact Great White Fleet.</div>");
                }
                request.setAttribute("forwardmessagetype", "failure");
            }
            RequestDispatcher requestdep = request.getRequestDispatcher("Accesslogin.do");
            request.setAttribute("resultreg", result);
            request.setAttribute("typeregistion", "registerresult");
            requestdep.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //    out.close();
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
