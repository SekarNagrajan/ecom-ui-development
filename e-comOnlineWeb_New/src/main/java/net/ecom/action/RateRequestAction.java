/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.formbeans.RateRequestForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ReturnVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ;
 *
 * @author deepa.p
 */
public class RateRequestAction extends org.apache.struts.action.Action {

    private static String SUCCESS = "RateRequestResult";
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            ServletContext contexts = servlet.getServletContext();
            System.out.println("[ECOM-WAR] RateRequestAction >>entered in rate request submission>>");
            RateRequestForm rrForm = (RateRequestForm) form;
            request.setAttribute("RateRequestSuccess","fail");
            String polCode = Validation.checkNull(rrForm.getPol()).split("-")[0].trim();
            System.out.println("[ECOM-WAR] RateRequestAction >>polCode>>" + polCode);
            String messageText = "", subject, agencyMail;
            messageText += "<div><table border=\"0\" width=\"100%\" ><tr><td colspan=\"3\">Dear Team,</td></tr>";
            messageText += "<tr><td colspan=\"3\"></td></tr>";
            messageText += "<tr><td colspan=\"3\">" + "The following details are submitted through e-Commerce application." + "</td></tr>";
            messageText += "<tr><td colspan=\"3\"></td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Company Name</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getCompanyName() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Country</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getCountry() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">ContactName</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getContactName() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Email Id</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getEmailId() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Origin</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getOrigin() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">POL</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getPol() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">POD</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getPod() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Destination</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getDestination() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Cargo Size</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getEqpSize() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Cargo Quantity</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getCargoQty() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Cargo Weight</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getCargoWt() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Commodity</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getCommodity() + "</td></tr>";
                    
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Actual Freight</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getActualfreight() + "</td></tr>";
            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Request Rate</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getRequestrate() + "</td></tr>";

            messageText += "<tr style='vertical-align:top;'><td width=\"10%\">Comments</td><td width=\"2%\">:</td><td width=\"80%\">" + rrForm.getComments() + "</td></tr>";
            messageText += "<tr><td colspan=\"3\"><br></td></tr>";
            messageText += "</table></div>";
            ReturnVO rvo = ecomCntrlObj.getRateRequestMailDets(polCode);
            System.out.println("[ECOM-WAR] RateRequestAction messageText =========>>>>>>>> "+messageText);
            if (rvo != null) {
                Mailer mail = new Mailer();
                subject = rvo.getStrSubject();
                agencyMail = rvo.getStrToMailId();
                System.out.println("[ECOM-WAR] RateRequestAction <>>subject>>"+subject);
                System.out.println("[ECOM-WAR] RateRequestAction >>rr agencyMail>>"+agencyMail);
                if (agencyMail != null && agencyMail.length() > 0) {
                    mail.setTo(agencyMail);
                    mail.setSubject(subject);
                    mail.setMessage(messageText);
                    String frommail = (String) contexts.getAttribute("clientfrommail");
                    String initiatorID = "ECOM-CNT";
                    String client_code = (String) contexts.getAttribute("client_code");
                    ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                    request.setAttribute("RateRequestSuccess","success");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("RateRequestSuccess","fail");
        }
        return mapping.findForward(SUCCESS);
    }
}
