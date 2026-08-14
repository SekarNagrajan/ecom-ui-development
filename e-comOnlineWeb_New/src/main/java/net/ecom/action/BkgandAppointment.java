/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.formbeans.BkgandAppointmentForm;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.Mailer;
import net.ecom.common.MailerNew;

public class BkgandAppointment extends org.apache.struts.action.Action {

    EcomRegistrationBeanRemote locobj = EJBLookUp.getRegistrationBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        //MailerNew mail = new MailerNew();
        Mailer mail = new Mailer();
        Hashtable contactmail = (Hashtable) session.getAttribute("SESSION_globalconfig");
        String agencymail = (String) contactmail.get("clientglobalmail");
        String SUCCESS = "bkgandapmt";
        String Message = "";
        ServletContext contexts = servlet.getServletContext();
        Hashtable countrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
        try {
            String username = null, city = null, compname = null, oth_city = null, countryname = null, country = null, agency = null, userphone = null, usermobile = null, useremail = null, preftime = null;
            BkgandAppointmentForm contform = (BkgandAppointmentForm) form;
            String messageText = "";
            String hiddenusername = (String) session.getAttribute("SESSION_LOGINNAME");
            String client_code = (String) contexts.getAttribute("client_code");
            String identify = request.getParameter("identify");
            String randomid = (String) session.getAttribute("bkgandapptrandomid");
//             System.out.println("[ECOM-WAR] BkgandAppointment   =====================>>>" + request.getParameter("randomnum"));
//             System.out.println("[ECOM-WAR] BkgandAppointment   ==========randomid===========>>>" + randomid);
             System.out.println("[ECOM-WAR] BkgandAppointment   randomnum: " + request.getParameter("randomnum") + ", randomid: " + randomid);

            String randomnum = (request.getParameter("randomnum") != null) ? request.getParameter("randomnum") : "";
            if (randomid != null && randomid.length() > 0) {
                if (randomid.equalsIgnoreCase(randomnum)) {
                    session.setAttribute("bkgandapptrandomid", "");
                    if (identify != null && identify.length() > 0) {
                        if (hiddenusername != null && hiddenusername.length() > 0) {
                            username = request.getParameter("username1");
                            compname = request.getParameter("compname1");
                            country = request.getParameter("country1");
                            city = request.getParameter("city1");
                            userphone = request.getParameter("userphone1");
                            usermobile = request.getParameter("strusermobile1");
                            useremail = request.getParameter("useremail1");
                            preftime = contform.getPreftime();
                            countryname = ((String) countrylist.get(country) != null) ? (String) countrylist.get(country) : country;
                        } else {
                            oth_city = request.getParameter("othercitystatus");
                            username = contform.getUsername();
                            compname = contform.getCompname();
                            country = request.getParameter("country");
                            if (oth_city != null && oth_city.equalsIgnoreCase("Y")) {
                                city = request.getParameter("other_city");
                            } else {
                                city = request.getParameter("city");
                            }
                            userphone = contform.getUserphone();
                            usermobile = contform.getStrusermobile();
                            useremail = contform.getUseremail();
                            preftime = contform.getPreftime();
                            countryname = ((String) countrylist.get(country) != null) ? (String) countrylist.get(country) : country;
                        }
                        String initiatorID = "ECOM-CNT";
                        HashMap mpobject = (HashMap) ecomCntrlObj.getEcomMailConfig(initiatorID, client_code);
                        if (agencymail != null && agencymail.length() > 0) {
                            messageText += "<div><table border=\"0\" width=\"100%\" ><tr><td colspan=\"3\">Dear Team,</td></tr>";
                            messageText += "<tr><td colspan=\"3\"></td></tr>";
                            messageText += "<tr><td colspan=\"3\">" + "The following details are submitted through e-Commerce application." + "</td></tr>";
                            messageText += "<tr><td colspan=\"3\"></td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Name</td><td width=\"2%\">:</td><td width=\"80%\">" + username + "</td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Company Name</td><td width=\"2%\">:</td><td width=\"80%\">" + compname + "</td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Country</td><td width=\"2%\">:</td><td width=\"80%\">" + countryname + "</td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">City</td><td width=\"2%\">:</td><td width=\"80%\">" + city + "</td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">User Phone</td><td width=\"2%\">:</td><td width=\"80%\">" + userphone + "</td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">User Mobile</td><td width=\"2%\">:</td><td width=\"80%\">" + usermobile + "</td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">User Email</td><td width=\"2%\">:</td><td width=\"80%\">" + useremail + "</td></tr>";
                            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Preferred Time</td><td width=\"2%\">:</td><td width=\"80%\">" + preftime + "</td></tr>";
                            messageText += "<tr><td colspan=\"3\"><br></td></tr>";
                            messageText += "</table></div>";
                            Message = "mailSend";
                            mail.setFrom(useremail);
                            mail.setTo(agencymail);
                            mail.setSubject("Book and Appointment Details");
                            mail.setMessage(messageText);
                            ecomCntrlObj.sendMailFromEjb(mail, useremail, initiatorID, client_code);
                        } else {
                            Message = "mailNotSend";
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        request.setAttribute("display_Message", Message);
        return mapping.findForward(SUCCESS);
    }
}
