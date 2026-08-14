/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomVGMBeanRemote;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.FileExtensionValidation;
import net.ecom.formbeans.VGMSubmitForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.VGMCommonVO;
import net.ecom.vo.VgmVO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author deepa.p
 */
public class SubmitVGMAction extends Action {

    EcomVGMBeanRemote vgmBean = EJBLookUp.getESLVGMBeanLookUp();
    EComCntrlBeanRemote cntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        ServletContext context = servlet.getServletContext();
        VGMSubmitForm vgmForm = (VGMSubmitForm) form;
        List<VgmVO> list = null;
        List<String> containerList = null;
        int size = 0;
        VgmVO vo = null;
        VGMCommonVO vgmVO = null;
        String book_no = null, country = null, fileName = null;
        String mailContent = null;
        HttpSession session = request.getSession();
        String ccMailid = null, bccMailId = null, ccAgencyMailId = null;
        String agencyId = null, originLocation = null;
        Map<String, String> mailMapForBCC = null;
        String submittedDate = DateConversion_New.getCurrentDate();
        String initiatorID = "ECOM-VGM";
        String agencymail = (String) context.getAttribute("clientfrommail");
        String client_code = (String) context.getAttribute("client_code");
        ServletContext contexts = servlet.getServletContext();
        try {
            String loginUser = ((String) session.getAttribute("SESSION_LOGINNAME"));
            String submitCheck = (String) session.getAttribute("VGESUBMITSESSION");
            System.out.println("[ECOM-WAR] SubmitVGMAction    >>>>submitCheck>>>" + submitCheck);
            loginUser = (loginUser != null && loginUser.length() > 0) ? loginUser : "Ecom";
            String enableFileBackendValidation = (String) contexts.getAttribute(GeneralConf.EnableFileBackendValidation);
            String ecomFileExt = (String) contexts.getAttribute(GeneralConf.EcomFileExtension);
            FileExtensionValidation flext = new FileExtensionValidation();

            String restrictedFiled = null, restrictedFiledSize = null;
            if ("send".equalsIgnoreCase(submitCheck)) {
                vgmVO = new VGMCommonVO();

                System.out.println("[ECOM-WAR] SubmitVGMAction    token in save action: " + session.getAttribute("org.apache.struts.action.TOKEN"));
                System.out.println("[ECOM-WAR] SubmitVGMAction    token in request: " + request.getParameter("org.apache.struts.taglib.html.TOKEN"));
                String sessionToken = (String) session.getAttribute("org.apache.struts.action.TOKEN");
                if (isTokenValid(request)) {
                    resetToken(request);
                    session.setAttribute("VGESUBMITSESSION", "submit");
                    String appversion = request.getParameter("appversion");
                    //strDate = traget.format(source.parse(strDate));
                    System.out.println("[ECOM-WAR] SubmitVGMAction    >>>>>entered in ehtered in submit....");
                    vgmBean.check();
                    vgmVO.setSubmissionType(vgmForm.getVgmSubmission());
                    System.out.println("[ECOM-WAR] SubmitVGMAction    >>(vgmForm.getVgmSubmission()>>" + (vgmForm.getVgmSubmission()));
                    vgmVO.setBookNo(vgmForm.getBookNo());
                    vgmVO.setBlNo(vgmForm.getBlNo());
                    vgmVO.setLoginUser(loginUser);
                    vgmVO.setAppVersion(appversion);
                    vgmVO.setVgmStatus(vgmForm.getVgmStatus());
                    FormFile file1 = vgmForm.getVgmFile();
                    if (file1 != null && file1.getFileName() != null && file1.getFileName().trim().length() > 0 && flext.checkFileValidation(enableFileBackendValidation, file1, ecomFileExt, request)) {
                        vgmVO.setVgmFileData(file1.getFileData());
                        vgmVO.setVgmFileName(file1.getFileName());
                        fileName = vgmForm.getBookNo() + "_" + EcomConsts.VGM_FILE_UPLOAD + "_" + file1.getFileName();
                    } else if (file1 != null && file1.getFileName() != null && file1.getFileName().trim().length() > 0) {
                        vgmVO.setVgmFileName(null);
                    }
                    restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
                    restrictedFiled = (restrictedFiled != null && restrictedFiled.length() > 0) ? restrictedFiled.substring(0, restrictedFiled.length() - 1) : "";
                    request.setAttribute("restrictedFile", restrictedFiled);
                    restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
                    restrictedFiledSize = (restrictedFiledSize != null && restrictedFiledSize.length() > 0) ? restrictedFiledSize.substring(0, restrictedFiledSize.length() - 1) : "";
                    request.setAttribute("restrictedFileSize", restrictedFiledSize);

                    String file_path = (String) contexts.getAttribute(GeneralConf.filepath);
                    String carriercode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
                    String sendVGMAgencyMail = (String) contexts.getAttribute(GeneralConf.EnableVGMAgencyMail);
                    String hideMaskportContainer = (String) contexts.getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER);
                    file_path = file_path + "VGM/";
                    vgmVO.setFilePath(file_path);
                    System.out.println("[ECOM-WAR] SubmitVGMAction    >>file_path>>" + file_path + "  >>file1>>" + file1);
                    agencyId = request.getParameter("bookingagencyid");
                    originLocation = request.getParameter("originlocation");
                    System.out.println("[ECOM-WAR] SubmitVGMAction    >>agencyId>>" + agencyId + " >>originLocation>>" + originLocation);
                    String bookno[] = request.getParameterValues("booknohid");
                    String containerno[] = request.getParameterValues("containernohid");
                    String containerSize[] = request.getParameterValues("eqptypehid");
                    String vgmWeight[] = request.getParameterValues("vgmWeight");
                    String vgmUnit[] = request.getParameterValues("vgmUnit");
                    country = vgmForm.getVgmCountry();
                    country = country != null && country.length() > 0 ? country.split("-")[0].trim() : null;
                    list = new ArrayList<VgmVO>();
                    containerList = new ArrayList<String>();
                    if (bookno != null && bookno.length > 0) {
                        mailContent = "<html><head>";
                        mailContent += "<style type='text/css'>  th{background-color: #1FBBA6;!important;color:#fffff;font-size:14px !important;} td{font-size:14px !important;} h2 { color: #0070af !important;font-size: 14px !important;font-variant: normal;}</style><title>SOLAS - VGM</title></HEAD>";
                        mailContent += "<body><div class='panel panel-primary'><div class='panel-heading'><span class='glyphicon glyphicon-arrow-right'></span><label style='font-size: 14px;'>Dear Customer,</label></div></div><br/><div class='panel panel-primary'><div class='panel-heading'><span class='glyphicon glyphicon-arrow-right'></span><label style='font-size: 14px;'>&nbsp;&nbsp;&nbsp;&nbsp;VGM has been submitted for the below containers from " + carriercode + " Online.</label></div></div><br/>";
                        mailContent += "<TABLE width='100%' align=center><tr><td style='font-size: 14px;color:#FF0000'>Date of Submission: <label style='font-size: 14px;color:#0033CC'>" + submittedDate + "</label></td></tr>"
                                + "<tr><td style='font-size: 14px;color:#FF0000'>VGM Submitted By: <label style='font-size: 14px;color:#0033CC'>" + loginUser + "</label></td></tr>"
                                + "<tr><td style='font-size: 14px;color:#FF0000'>Customer Name    : <label style='font-size: 14px;color:#0033CC'>" + "  " + vgmForm.getVgmAPName() + "</label></td></tr></TABLE><br/>";
                        mailContent += "<table style='border: 1px solid #ddd;width: 100%;background-color: transparent;max-width: 100%;'><thead style='background-color: #1FBBA6;color: #fff;'>"
                                + "<tr>";
                        if ("blno".equalsIgnoreCase(vgmForm.getVgmSubmission())) {
                            mailContent = mailContent + "<th>BL No.</th>";
                        }
                        mailContent = mailContent + "<th>Book No.</th><th>Equipment Id.</th><th>Equipment Type.</th>"
                                + "<th>VGM Weight</th><th>Unit</th></tr></thead><tbody>";
                        for (int i = 0; i < bookno.length; i++) {
                            vo = new VgmVO();
                            book_no = bookno[i];
                            System.out.println("[ECOM-WAR] SubmitVGMAction    vgmweight>>>" + vgmWeight[i]+" bookno>>>>" + bookno[i]);
                            vo.setBook_no(bookno[i]);
                            containerList.add(containerno[i]);
                            vo.setEqpid(containerno[i]);
                            vo.setEqptype(containerSize[i]);
                            vo.setVgm_ref_number(vgmForm.getVgmReference());
                            vo.setVgm_order_number(vgmForm.getVgmOrderNo());
                            vo.setVgm_obtained_date(DateConversion_New.convertDefaultDBFormat(vgmForm.getVgmObtainDate()));
                            vo.setVgm_weight(checkNull(vgmWeight[i]).replaceAll(",", ""));
                            vo.setVgm_unit(vgmUnit[i]);
                            vo.setVgm_obtained_method(vgmForm.getVgmObtainMethod());
                            vo.setVgm_company_name(vgmForm.getVgmCompanyNmae());
                            System.out.println("[ECOM-WAR] SubmitVGMAction    >>>vgmForm.getVgmAddr1()>>" + vgmForm.getVgmAddr1()+" >>vgmForm.getVgmZipcode()>>" + vgmForm.getVgmZipcode());
                            vo.setVgm_address1(vgmForm.getVgmAddr1());
                            vo.setVgm_address2(vgmForm.getVgmAddr2());
                            vo.setVgm_zipcode(vgmForm.getVgmZipcode());
                            vo.setVgm_city(vgmForm.getVgmCity());
                            vo.setVgm_country(country);
                            vo.setVgm_authorized_person(vgmForm.getVgmAuthPerson());
                            vo.setTelephone(vgmForm.getVgmPhone());
                            vo.setFax(vgmForm.getVgmFax());
                            vo.setEmail(vgmForm.getVgmEmail());
                            list.add(vo);
                            mailContent += "<tr>";
                            if ("blno".equalsIgnoreCase(vgmForm.getVgmSubmission())) {
                                mailContent = mailContent + "<td>" + vgmForm.getBlNo() + "</td>";
                            }
                            String displayContainerNo = "Y".equalsIgnoreCase(hideMaskportContainer) ? "Container " + (i + 1) : containerno[i];
                            mailContent += "<td>" + vgmForm.getBookNo() + "</td><td>" + displayContainerNo + "</td>" + "<td>" + containerSize[i] + "</td>"
                                    + "<td>" + vgmWeight[i] + "</td>";
                            //System.out.println("[ECOM-WAR] SubmitVGMAction    vgm weight>>>>"+vgmComapny[i]);
                            if ("T".equalsIgnoreCase(vgmUnit[i])) {
                                mailContent += "<td>Tons</td>";
                            } else {
                                mailContent += "<td>Kgs</td>";
                            }
                            mailContent += "</tr>";
                        }
                    }
                    vgmVO.setVgmContList(list);
                    //String result = null;
                    String result = vgmBean.saveVGMDetails(vgmVO);
                    if ("success".equalsIgnoreCase(result)) {
                        String fromMailId = (String) context.getAttribute("clientfrommail");
                        Hashtable authObj = (Hashtable) session.getAttribute("SESSION_AUTHENTICATION");
                        mailContent += "<tbody></table><br/><br/>";
                        if ("ESL".equalsIgnoreCase(carriercode)) {
                            mailContent += "<div class='panel panel-primary'><div class='panel-heading'><span class='glyphicon glyphicon-arrow-right'></span><label style='font-size: 14px;'>Disclaimer : The sender at all time ensuring  the correctness and completeness of the VGM submitted.  Emirates Shipping Line takes no responsibility for, and will not be liable for any loss/damage so arising.</label></div></div>";
                        }
                        mailContent += "<br/><div class='panel panel-primary'><div class='panel-heading'><span class='glyphicon glyphicon-arrow-right'></span><label style='font-size: 14px;color:#0033CC'>****This is system generated auto email, Please do not reply to this email****.</label></div></div></body></html>";
                        System.out.println("[ECOM-WAR] SubmitVGMAction    >>>" + result+"    >>fromMailId>>" + fromMailId);
                        Mailer mail = new Mailer();
                        mail.setFrom(fromMailId);
                        System.out.println("[ECOM-WAR] SubmitVGMAction    >>vgmForm.getSendemailid()>>>" + vgmForm.getSendemailid());
                        mail.setTo(vgmForm.getSendemailid());
                        bccMailId = "";
                        ccMailid = (String) context.getAttribute(GeneralConf.VGMCCMAILID);
                        bccMailId = (String) context.getAttribute(GeneralConf.VGMBCCMAILID);
                        if ("Y".equalsIgnoreCase(sendVGMAgencyMail)) {
                            ccAgencyMailId = vgmBean.getAgencyMailForVGM(agencyId);

                            if (ccAgencyMailId != null && ccAgencyMailId.trim().length() > 0 && ccMailid != null && ccMailid.trim().length() > 0) {
                                ccMailid = ccAgencyMailId + "," + ccMailid;

                            } else if (ccAgencyMailId != null && ccAgencyMailId.trim().length() > 0) {
                                ccMailid = ccAgencyMailId;
                            } else if (ccMailid != null && ccMailid.trim().length() > 0) {
                                ccMailid = ccMailid;
                            }
                        } else {
                            if (ccMailid != null && ccMailid.trim().length() > 0) {
                                ccMailid = ccMailid;
                            }
                        }
                        System.out.println("[ECOM-WAR] SubmitVGMAction    finalccvgmmailid" + ccMailid);

                        mail.setCc(ccMailid);
                        mailMapForBCC = vgmBean.getBCCMailidForAgency(agencyId, originLocation);
                        if (mailMapForBCC != null && mailMapForBCC.size() > 0) {
                            if (mailMapForBCC.get("AGENCYEQUAL").equalsIgnoreCase("Equal") && mailMapForBCC.get("BOOKINGAGENCY").length() > 0) {
                                bccMailId = bccMailId + "," + mailMapForBCC.get("BOOKINGAGENCY");
                            } else {
                                if (mailMapForBCC.get("BOOKINGAGENCY").length() > 0) {
                                    bccMailId = bccMailId + "," + mailMapForBCC.get("BOOKINGAGENCY");
                                }
                                if (mailMapForBCC.get("ORIGINAGENCY").length() > 0) {
                                    bccMailId = bccMailId + "," + mailMapForBCC.get("ORIGINAGENCY");
                                }
                            }
                        }
                        if (bccMailId != null && bccMailId.trim().length() > 0) {
                            mail.setBcc(bccMailId);
                        }
                        System.out.println("[ECOM-WAR] SubmitVGMAction    >>>cc mailid>>>" + ccMailid + " >>>bcc mailid>>>" + bccMailId);
                        mail.setSubject("VGM Submission Details - " + book_no);
                        mail.setMessage(mailContent);
                        mail.setAttachment(fileName);
                        System.out.println("[ECOM-WAR] SubmitVGMAction    fileName===> " + fileName);
                        mail.setAttachmentpath(file_path);
                        //1   mail.sendMail(authObj, fromMailId);
                        cntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                        session.setAttribute("VGMFAILURE", "");
                    } else {
                        SUCCESS = "failure";
                        session.setAttribute("VGMFAILURE", "An error occurred.Please check the data submitted.");
                    }
                } else {
                    System.out.println("[ECOM-WAR] SubmitVGMAction    Duplicate Submission of the form");
                }
            } else {
                SUCCESS = "failure";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM-WAR] SubmitVGMAction    >>>SUCCESS>>>" + SUCCESS);
        return mapping.findForward(SUCCESS);
    }

    private String checkNull(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
