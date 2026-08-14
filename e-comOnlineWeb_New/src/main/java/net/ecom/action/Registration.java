/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.formbeans.RegistrationForm;
import net.ecom.vo.RegistrationVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.BeanUtils;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.ajax.UserProfileAjax;
import net.ecom.captcha.VerifyRecaptcha;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.lookup.EJBLookUp;
import net.ecom.common.Validation;
import net.ecom.vo.ReturnVO;
import org.apache.struts.action.ActionErrors;

public class Registration extends org.apache.struts.action.Action {

    String Forward = "Failure";
    Validation validate = new Validation();
    EcomRegistrationBeanRemote ecomRejObj = EJBLookUp.getRegistrationBeanLookUp();
    EComCntrlBeanRemote cntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
   

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors err = new ActionErrors();
        HttpSession session = request.getSession();
        RegistrationForm RegObj = (RegistrationForm) form;
        String saveType = "";
        String companyId = "";
        String timezone = "";
        String locationt = "";
        String existinguser = "N";
        try {
            ServletContext contexts = servlet.getServletContext();
            String pagereq = (String) session.getAttribute("RegValidate");
            String checkclick = request.getParameter("checkclick");

            String secretKey = Validation.checkNull((String) contexts.getAttribute(GeneralConf.CaptchaSecretKey));
            String toDisplayGgleCaptcha = (String) contexts.getAttribute(GeneralConf.RegGoogleCaptcha);
            String KYC_fileupload = (String) contexts.getAttribute(GeneralConf.KYC_fileupload);
            String allowexistingUsertoReg = (String) contexts.getAttribute(GeneralConf.ALLOW_EXISTING_USER_TO_REGISTER);

            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

            boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse, secretKey);

            System.out.println("[ECOM-WAR] Registration  token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN"));
            System.out.println("[ECOM-WAR] Registration  token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN"));
            String sessionToken = (String) session.getAttribute("org.apache.struts.action.TOKEN");
            if (isTokenValid(request)) {
                System.out.println("[ECOM-WAR] Registration  ::Captcha Verify value" + verify);

                if ("Send".equalsIgnoreCase(pagereq) && checkclick != null && checkclick.equalsIgnoreCase("yes") && (!"Y".equalsIgnoreCase(toDisplayGgleCaptcha) || verify)) {
                    resetToken(request);
                    session.setAttribute("RegValidate", "Saved");
                    if (err != null && err.size() == 0) {
                        String requrl = request.getRequestURL().toString();
                        String requri = request.getRequestURI();
                        String contextpath = request.getContextPath();
                        requrl = requrl.replace(requri, "");
                        requrl = requrl + contextpath + "/ValidateReg?uniqueId";
                        RegistrationVO regVO = new RegistrationVO();
                        RegistrationVO regBackUpVO = null;
                        String selectedLang = (String) session.getAttribute("SelectedLanguage");
                        regVO.setLangType(selectedLang);
                        RegistrationForm regForm = (RegistrationForm) form;
                        companyId = regForm.getCompanyId();
                        timezone = regForm.getTimezone();
                        locationt = regForm.getLocationt();
                        System.out.println("[ECOM-WAR] Registration  timezonesavetoreg" + timezone + "timezonesavetoregloc" + locationt);
                        BeanUtils.copyProperties(regVO, regForm);
                        if (companyId.length() > 0) {
                            saveType = "U";
                            regBackUpVO = (RegistrationVO) session.getAttribute("SESSION_BACKUP_REGVO");

                            if (regBackUpVO != null && regVO != null) {
                                System.out.print(regBackUpVO.equals(regVO));
                                if (regBackUpVO.equals(regVO)) {
                                    saveType = "C";
                                }
                            }
                        } else {
                            saveType = "N";
                        }
                        if ("Y".equalsIgnoreCase(allowexistingUsertoReg)) {
                            String userid = RegObj.getLoginname();
                            String useralreadyexists = ecomRejObj.CheckUserId(userid);
                            if (useralreadyexists != null && useralreadyexists.equalsIgnoreCase("Exists")) {
                                existinguser = "Y";
                            } else {
                                existinguser = "N";
                            }
                        }
                        ReturnVO rvo =null;
                        if("Y".equalsIgnoreCase(existinguser)){
                            rvo = ecomRejObj.getDuplicateRegMail(regVO);
                        } else {
                            rvo = ecomRejObj.saveToRegistration(regVO, saveType, companyId, KYC_fileupload);
                        }
                        String initiatorID = "ECOM-REG";
                        if (rvo != null && rvo.getStrSaveType().length() > 0) {
                            session.setAttribute("SESSION_BACKUP_REGVO", null);
                            Forward = "success";
                            String client_code = (String) contexts.getAttribute("client_code");
                            System.out.println("[ECOM-WAR] Registration  context.getAttribute('client_code')=====> " + contexts.getAttribute("client_code"));
                            String agencymail = (String) contexts.getAttribute("clientfrommail");
                            Mailer mail = new Mailer();
                            mail.setTo(rvo.getStrToMailId());
                            if (rvo.getBccMail() != null && rvo.getBccMail().length() > 0) {
                                mail.setBcc(rvo.getBccMail());
                            }
                            mail.setSubject(rvo.getStrSubject());
                            String msg = rvo.getStrMailMsg();
                            String mailLogo = "<img src='" + (String) contexts.getAttribute(GeneralConf.MAILLOGOIMAGE) + "' alt='Logo'/>";
                            msg = msg.replace("@url", requrl);
                            msg = msg.replace("@randomid", rvo.getRandomid());
                            msg = msg.replace("@maillogo", mailLogo);
                            System.out.println("[ECOM-WAR] Registration  >>msg>>" + msg);
                            mail.setMessage(msg);
                            try {
                                cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                            request.setAttribute("forwardmessage", rvo.getStrDisplayMsg());
                            request.setAttribute("forwardmessagetype", "emailsent");
                            if ("Y".equalsIgnoreCase(KYC_fileupload)) {
                                ArrayList filenamelis = (ArrayList) session.getAttribute("REG_FILE_Name_LIST");
                                ArrayList<InputStream> inputsreamlis = (ArrayList<InputStream>) session.getAttribute("REG_FILE_INPUT_STREAM");
                                session.setAttribute("REG_FILE_Name_LIST", null);
                                session.setAttribute("REG_FILE_INPUT_STREAM", null);
                                System.out.println("[ECOM-WAR] Registration  inputsreamlis=> " + inputsreamlis);
                                if (filenamelis != null && inputsreamlis != null) {
                                    for (int i = 0; i < filenamelis.size(); i++) {
                                        String refno = rvo.getStrRefNo();
                                        String country = Validation.checkNull(request.getParameter("countrycode"));
                                        String fileName = (String) filenamelis.get(i);
                                        InputStream fileContent = inputsreamlis.get(i);
                                        String refnoFolderPath = (String) contexts.getAttribute("Reg_File_Path");
                                        refnoFolderPath = refnoFolderPath + refno;
                                        Path refnoFolder = Paths.get(refnoFolderPath);
                                        try {
                                            Files.createDirectories(refnoFolder);

                                            String requestid = country + "_" + fileName;
                                            System.out.println("[ECOM-WAR] Registration  fileName-------" + fileName + "--requestid---- " + requestid);
                                            String file_src = refnoFolderPath + File.separator + requestid;
                                            try (OutputStream outputStream = new FileOutputStream(file_src)) {
                                                byte[] buffer = new byte[1024];
                                                int bytesRead;
                                                while ((bytesRead = fileContent.read(buffer)) != -1) {
                                                    outputStream.write(buffer, 0, bytesRead);
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            System.err.println("Failed to create the email folder: " + e.getMessage());
                                        }
                                    }
                                }
                            }
                        } else {
                            request.setAttribute("forwardmessage", "Page Error Try Again Later");
                            request.setAttribute("forwardmessagetype", "failure");
                        }
                    }
                } else {
                    Forward = "welcome";
                }
            } else {
                System.out.println("[ECOM-WAR] Registration  duplicate submission");
                request.setAttribute("forwardmessage", contexts.getAttribute("Registration_Success_Message"));
                request.setAttribute("forwardmessagetype", "duplicate");
            }
        } catch (Exception e) {
            request.setAttribute("forwardmessage", "Page Error Try Again Later");
            request.setAttribute("forwardmessagetype", "failure");
            e.printStackTrace();
        }
        return mapping.findForward(Forward);
    }
}
