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
import net.ecom.formbeans.ContactusForm;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.captcha.VerifyRecaptcha;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.MailerNew;
import net.ecom.common.Validation;
import org.displaytag.util.DependencyChecker;

public class Contactus extends org.apache.struts.action.Action {

    EcomRegistrationBeanRemote locobj = EJBLookUp.getRegistrationBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    Map stateMap = null, stateMapCountry = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        //MailerNew mail = new MailerNew();
        Mailer mail = new Mailer();
        Hashtable contactmail = (Hashtable) session.getAttribute("SESSION_globalconfig");
        String agencymail = (String) contactmail.get("clientglobalmail");
        System.out.println("[ECOM-WAR] Contactus   agencymail----->" + agencymail);
        String SUCCESS = "contactus";
        String Message = "";
        ServletContext contexts = servlet.getServletContext();
        Hashtable countrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
        stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
        try {
            String username = null, city = null, compname = null, oth_city = null, countryname = null, country = null, agency = null, userphone = null, usermobile = null, useremail = null, subject = null, remarks = null, state = null, stateName = null;
            ContactusForm contform = (ContactusForm) form;
            String messageText = "";
            String hiddenusername = (String) session.getAttribute("SESSION_LOGINNAME");
            String client_code = (String) contexts.getAttribute("client_code");
            String cargoprotectmail = (String) contexts.getAttribute(GeneralConf.CargoProtectMail);
            String cargoprotectmailsubject = (String) contexts.getAttribute(GeneralConf.CargoProtectMailSubject);
            String hideContactusState = (String) contexts.getAttribute(GeneralConf.HIDE_CONTACTUS_STATE);
            String hideContactusMob = (String) contexts.getAttribute(GeneralConf.HIDE_CONTACTUS_MOBILE);
            String enableCountryPhone = (String) contexts.getAttribute(GeneralConf.Enable_Country_Phone);
            String companytype = Validation.checkNull((String) request.getParameter("companytype"));
//             System.out.println("[ECOM-WAR] Contactus   companytype==> " + companytype);
            String identify = request.getParameter("identify");
            String randomid = (String) session.getAttribute("contactrandomid");
//             System.out.println("[ECOM-WAR] Contactus   =====================>>>" + request.getParameter("randomnum"));
//             System.out.println("[ECOM-WAR] Contactus   ==========randomid===========>>>" + randomid);

//             System.out.println("[ECOM-WAR] Contactus   token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN"));
//             System.out.println("[ECOM-WAR] Contactus   token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN"));
            System.out.println("[ECOM-WAR] Contactus   token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN") + ", token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN") + ", randomnum: " + request.getParameter("randomnum") + ", randomid: " + randomid + ", companytype: " + companytype);
            String contactcaptcha = Validation.checkNull((String) contexts.getAttribute(GeneralConf.Enable_ContactUs_Captcha));
            String secretKey = Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.CaptchaSecretKey));
            String CaptchaDefault = (String) contexts.getAttribute(GeneralConf.ENABLE_CONTACTUS_CAPTCHA_DEFAULT);
            
            String sessionToken = (String) session.getAttribute("org.apache.struts.action.TOKEN");
             String randomnum = request.getParameter("randomnum");
            // String randomnum = (request.getParameter("randomnum") != null) ? request.getParameter("randomnum") : "";
            if (randomid != null && randomid.length() > 0  && randomnum != null && randomnum.length() > 0 ) {
                boolean verify = true;
                int passattemptCount = 0;
                if ("Y".equalsIgnoreCase(contactcaptcha)) {
                    String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
                    Object attemptCountObj = session.getAttribute("ContactUsCount");
                    passattemptCount = (attemptCountObj != null) ? (int) attemptCountObj : 0;
                    if("Y".equalsIgnoreCase(CaptchaDefault)) {
                        passattemptCount = 3;
                    }
                    if (passattemptCount >= 3) {
                        verify = VerifyRecaptcha.verify(gRecaptchaResponse, secretKey);
                    }
                    passattemptCount = passattemptCount + 1;
                    session.setAttribute("ContactUsCount", passattemptCount);
                }
                if (randomid.equalsIgnoreCase(request.getParameter("randomnum")) && verify) {
                    if (isTokenValid(request)) {
                        session.setAttribute("contactrandomid", "");
                        resetToken(request);
                        if (identify != null && identify.length() > 0) {
                            if (hiddenusername != null && hiddenusername.length() > 0) {
                                username = request.getParameter("username1");
                                compname = request.getParameter("compname1");
                                country = request.getParameter("country1");
                                city = request.getParameter("city1");
                                 userphone = request.getParameter("userphone1");
                                 usermobile = request.getParameter("strusermobile1");
                                useremail = request.getParameter("useremail1");
                                subject = contform.getSubject();
                                remarks = contform.getRemarks();

                                countryname = ((String) countrylist.get(country) != null) ? (String) countrylist.get(country) : country;
                            } else {
//                            oth_city = request.getParameter("othercitystatus");
                                username = contform.getUsername();
                                compname = contform.getCompname();
                                country = request.getParameter("country");
//                            if (oth_city != null && oth_city.equalsIgnoreCase("Y")) {
//                                city = request.getParameter("other_city");
//                            } else {
//                                city = request.getParameter("city");
//                            }
            if("Y".equalsIgnoreCase(enableCountryPhone)){
              userphone = contform.getItiUserPhone();
               usermobile = contform.getItiUserMobile();
            }else{
                userphone = contform.getUserphone();
                usermobile = contform.getStrusermobile();
            }
                               
                               
                                useremail = contform.getUseremail();
                                subject = contform.getSubject();
                                remarks = contform.getRemarks();
                                state = contform.getState();
                                // city=contform.getCity();
                                city = contform.getCity_desc();
                                stateName = concatStateName(state, country);
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
                                if (!"Y".equalsIgnoreCase(hideContactusState)) {
                                    messageText += "<tr style='vertical-align:top;'><td width=\"10%\">State</td><td width=\"2%\">:</td><td width=\"80%\">" + stateName + "</td></tr>";
                                }
                                messageText += "<tr style='vertical-align:top;'><td width=\"10%\">City</td><td width=\"2%\">:</td><td width=\"80%\">" + city + "</td></tr>";
                                messageText += "<tr style='vertical-align:top;'><td width=\"10%\">User Phone</td><td width=\"2%\">:</td><td width=\"80%\">" + userphone + "</td></tr>";
                                if (!"Y".equalsIgnoreCase(hideContactusMob)) {
                                    messageText += "<tr style='vertical-align:top;'><td width=\"10%\">User Mobile</td><td width=\"2%\">:</td><td width=\"80%\">" + usermobile + "</td></tr>";
                                }
                                messageText += "<tr style='vertical-align:top;'><td width=\"10%\">User Email</td><td width=\"2%\">:</td><td width=\"80%\">" + useremail + "</td></tr>";
                                messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Subject</td><td width=\"2%\">:</td><td width=\"80%\">" + subject + "</td></tr>";
                                messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Message</td><td width=\"2%\">:</td><td width=\"80%\">" + remarks + "</td></tr>";
                                messageText += "<tr><td colspan=\"3\"><br></td></tr>";
                                messageText += "</table></div>";
                                Message = "mailSend";
                                mail.setFrom(useremail);
                                mail.setTo(agencymail);
                                if ("cargoprotect".equalsIgnoreCase(companytype)) {
                                    mail.setTo(cargoprotectmail);
                                    if (Validation.checkNull(cargoprotectmailsubject).length() > 0) {
                                        mail.setSubject(cargoprotectmailsubject + " - " + subject);
                                    } else {
                                        mail.setSubject(client_code + " cargo protect inquiry - " + subject);
                                    }
                                } else {
                                    mail.setSubject(client_code + " ecom inquiry - " + subject);
                                }
                                mail.setMessage(messageText);
                                //mail.sendMail(mpobject);
                                ecomCntrlObj.sendMailFromEjb(mail, useremail, initiatorID, client_code);
                            } else {
                                Message = "mailNotSend";//
                            }
                        }
                    } else {
                        System.out.println("[ECOM-WAR] Contactus   Duplicate Submission of the form");
                        Message = "duplicate";//duplicate
                    }
                } else {
                    System.out.println("[ECOM-WAR] Contactus   multiple time submission");
                    Message = "duplicate";//duplicate
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        saveToken(request);
        request.setAttribute("display_Message", Message);
        return mapping.findForward(SUCCESS);
    }

    public String concatStateName(String state, String country) {
        try {

            stateMapCountry = (HashMap) stateMap.get(Validation.checkNull(country).toUpperCase());
            if (state != null && state.length() > 0 && stateMapCountry != null) {
                // state = state + " - " + Validation.checkNull((String) stateMapCountry.get(state));
                state = Validation.checkNull((String) stateMapCountry.get(state.toUpperCase()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Validation.checkNull(state);
    }
}
