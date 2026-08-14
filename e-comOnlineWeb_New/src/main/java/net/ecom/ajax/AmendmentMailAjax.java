/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAmendmentBookingBean;
import net.ecom.AppTierCntrlBean.EcomAmendmentMailBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.CommonMailForAgency;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.BokingDetailds;
import net.ecom.vo.CargoProtectVo;

/**
 *
 * @author preethi.elumalai
 */
@WebServlet(name = "AmendmentMailAjax", urlPatterns = {"/AmendmentMailAjax"})
public class AmendmentMailAjax extends HttpServlet {

    EcomAmendmentMailBeanRemote ecomAmdMail = EJBLookUp.getEcomAmendmentMailLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();

    ServletContext contexts;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            String admTomailid = "", fileNames = "", admSubject = "", strRefNo = "", subject = "", client_code = "", mailMsg = "", finalMsg = "";
            net.ecom.vo.AmendmentBookingFormBean amendmentVO = new net.ecom.vo.AmendmentBookingFormBean();
            String LRPBook_no = request.getParameter("book_no");
            String EDIRead_id = request.getParameter("EDIread_id");
            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
            Map portlist = (Map) contexts.getAttribute("SESSION_PORTLIST");
            HashMap carriageContacts = (HashMap) session.getAttribute("CARRIERCONTACTSMAP");
            LinkedHashMap eqpmap = (LinkedHashMap) contexts.getAttribute("SESSION_HASH_EQUIPMENT_TYPE");
            String apname = (String) session.getAttribute("apname");
            String frommail = (String) contexts.getAttribute("clientfrommail");
            String initiatorID = "ECOM-BKG";
            client_code = (String) contexts.getAttribute("client_code");
            String type = request.getParameter("type");
            System.out.println("[ECOM-WAR] AmendmentMailAjax type==>" + type + "LRPBook_no==>" + LRPBook_no);
            Map masterChache = new HashMap();
            masterChache.put("comList", comlist);
            masterChache.put("EQUIPMENT_TYPE", eqpmap);
            if ("Booking".equalsIgnoreCase(type)) {
                String EcomBookNo = request.getParameter("EcomBookNo");
                HashMap maildetails = (HashMap) session.getAttribute("SESSION_AMEDMAIL_DETAILS");
                System.out.println("[ECOM-WAR] AmendmentMailAjax SESSION_AMEDMAIL_DETAILS maildetails-----" + maildetails);
                if (maildetails != null) {
                    admTomailid = (String) maildetails.get("AdmToMailId");
                    fileNames = (String) maildetails.get("Filenames");
                    admSubject = (String) maildetails.get("AdmSubject");
                    mailMsg = (String) maildetails.get("Mailmsg");
                    strRefNo = (String) maildetails.get("StrRefNo");
                    amendmentVO = (net.ecom.vo.AmendmentBookingFormBean) maildetails.get("amendmentMaster");
                    System.out.println("[ECOM-WAR] AmendmentMailAjax admTomailid-----" + admTomailid
                            + "  Filenames: " + fileNames + "  AdmSubject: " + admSubject
                            + "  Mailmsg: " + mailMsg + "  StrRefNo: " + strRefNo + "  amendmentVO: " + amendmentVO);
                    Map mlst = new HashMap();
                    mlst.put("LRPBook_no", LRPBook_no);
                    mlst.put("EcomBookNo", EcomBookNo);
                    mlst.put("EDIRead_id", EDIRead_id);
                    System.out.println("[ECOM-WAR] AmendmentMailAjax LRPBook_no=>" + LRPBook_no + "EDIRead_id=>" + EDIRead_id + "EcomBookNo=>" + EcomBookNo + "type=>" + type);
                    BokingDetailds previousbook = ecomAmdMail.getBookingDetails(mlst, type, "P");
                    BokingDetailds currectbook = ecomAmdMail.getBookingDetails(mlst, type, "C");
                    Map<String, List<String>> rsl = findDiffering(previousbook, currectbook);
//                System.out.println("[ECOM-WAR] AmendmentMailAjax rsl============>" + rsl);
                    String msgg = newAgencyamendmentBookingCompMail(masterChache, rsl, LRPBook_no, EDIRead_id, portlist, carriageContacts, type);
                    String msg = commanMasterDetails(carriageContacts, amendmentVO);
                    mailMsg = mailMsg.replace("@amendmnetDetails", msg + msgg);
                    finalMsg = mailMsg;
                    Mailer mail = new Mailer();
                    String finalmailid = agencyamendmentmailid(admTomailid, checkNull(previousbook.getAmendmentTomailId()));
                    mail.setTo(checkNull(finalmailid));
//                System.out.println("[ECOM-WAR] AmendmentMailAjax mailid===>" + finalmailid);
                    subject = admSubject;
                    subject = subject.replace("@apname", apname);
                    String filenames = fileNames;
                    mail.setSubject(subject);
                    mail.setMessage(finalMsg);
                    mail.setAttachment(filenames);
                    mail.setAttachmentpath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH) + File.separator + strRefNo + File.separator);
                    mail.setCc(null);
                    try {
                        ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
//                System.out.println("[ECOM-WAR] AmendmentMailAjax msgg=============>" + msgg);
                }
            } else if ("SI".equalsIgnoreCase(type)) {
                mailMsg = "";
                String agencyRefNo = (String) session.getAttribute("SIAGENCYREFNO_For_Mail");
                String SINo = request.getParameter("SINo");
                String BLNo = request.getParameter("BLNo");
                HashMap mlst = new HashMap();
                mlst.put("LRPBook_no", LRPBook_no);
                mlst.put("EDIRead_id", EDIRead_id);
                mlst.put("SINo", SINo);
                mlst.put("BLNo", BLNo);
                HashMap maildetails = (HashMap) session.getAttribute("SI_AMEDMAIL_DETAILS");
                System.out.println("[ECOM-WAR] AmendmentMailAjax SI_AMEDMAIL_DETAILS maildetails-----" + maildetails);
                if (maildetails != null) {
                    admTomailid = (String) maildetails.get("AdmToMailId");
                    fileNames = (String) maildetails.get("Filenames");
                    admSubject = (String) maildetails.get("AdmSubject");
                    mailMsg = (String) maildetails.get("Mailmsg");
                    strRefNo = (String) maildetails.get("StrRefNo");
                    System.out.println("[ECOM-WAR] AmendmentMailAjax  AdmToMailId: " + admTomailid
                            + "  Filenames: " + fileNames + "  AdmSubject: " + admSubject
                            + "  Mailmsg: " + mailMsg + "  StrRefNo: " + strRefNo);
                    try {
                        BokingDetailds previousbook = ecomAmdMail.getBookingDetails(mlst, type, "P");
                        BokingDetailds currectbook = ecomAmdMail.getBookingDetails(mlst, type, "C");
                        Map<String, List<String>> rsl = findDiffering(previousbook, currectbook);
//                    System.out.println("[ECOM-WAR] AmendmentMailAjax rsl============>" + rsl);
                        String msgg = newAgencyamendmentBookingCompMail(masterChache, rsl, LRPBook_no, EDIRead_id, portlist, carriageContacts, type);
                        String msg = commonSIMasterDetails(LRPBook_no, SINo, BLNo, agencyRefNo);
                        mailMsg = mailMsg.replace("@amendmnetDetails", msg + msgg);
                        finalMsg = mailMsg;
//                    System.out.println("[ECOM-WAR] AmendmentMailAjax finalMsg===>" + finalMsg);
                        Mailer mail = new Mailer();
//                    System.out.println("[ECOM-WAR] AmendmentMailAjax previousbook.getAmendmentTomailId()==>" + previousbook.getAmendmentTomailId());
                        String finalmailid = agencyamendmentmailid(admTomailid, checkNull(previousbook.getAmendmentTomailId()));
                        mail.setTo(checkNull(finalmailid));
                        String subjectt = admSubject;
                        subjectt = subjectt.replace("@apname", apname);
                        mail.setSubject(subjectt);
                        mail.setMessage(finalMsg);
                        mail.setCc(null);
                        mail.setAttachment(fileNames);
                        mail.setAttachmentpath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH) + File.separator + strRefNo + File.separator);
                        //1  mail.sendMail(authObj, agencymail);
                        ecomCntrlObj.sendMailFromEjb(mail, frommail, initiatorID, client_code);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            } else if ("BL".equalsIgnoreCase(type)) {
                String initiatorid = "ECOM-BL";
                String agencyRefNo = (String) session.getAttribute("SIAGENCYREFNO_For_Mail");
                String SINo = request.getParameter("SINo");
                String BLNo = request.getParameter("BLNo");
                HashMap mlst = new HashMap();
                mlst.put("LRPBook_no", LRPBook_no);
                mlst.put("EDIRead_id", EDIRead_id);
                mlst.put("SINo", SINo);
                mlst.put("BLNo", BLNo);
                HashMap maildetails = (HashMap) session.getAttribute("BL_AMEDMAIL_DETAILS");
                System.out.println("[ECOM-WAR] AmendmentMailAjax BL_AMEDMAIL_DETAILS maildetails-----" + maildetails);
                if (maildetails != null) {
                    admTomailid = (String) maildetails.get("AdmToMailId");
                    fileNames = (String) maildetails.get("Filenames");
                    admSubject = (String) maildetails.get("AdmSubject");
                    mailMsg = (String) maildetails.get("Mailmsg");
                    strRefNo = (String) maildetails.get("StrRefNo");
                    System.out.println("[ECOM-WAR] AmendmentMailAjax AdmToMailId: " + admTomailid
                            + "  Filenames: " + fileNames + "  AdmSubject: " + admSubject
                            + "  Mailmsg: " + mailMsg + "  StrRefNo: " + strRefNo);
                    try {
                        BokingDetailds previousbook = ecomAmdMail.getBookingDetails(mlst, type, "P");
                        BokingDetailds currectbook = ecomAmdMail.getBookingDetails(mlst, type, "C");
                        Map<String, List<String>> rsl = findDiffering(previousbook, currectbook);
//                    System.out.println("[ECOM-WAR] AmendmentMailAjax rsl============>" + rsl);
                        String msgg = newAgencyamendmentBookingCompMail(masterChache, rsl, LRPBook_no, EDIRead_id, portlist, carriageContacts, type);
                        String msg = commonSIMasterDetails(LRPBook_no, SINo, BLNo, agencyRefNo);
                        mailMsg = mailMsg.replace("@amendmnetDetails", msg + msgg);
                        finalMsg = mailMsg;
//                    System.out.println("[ECOM-WAR] AmendmentMailAjax finalMsg===>" + finalMsg);
                        Mailer maill = new Mailer();
                        String finalmailid = agencyamendmentmailid(admTomailid, checkNull(previousbook.getAmendmentTomailId()));
                        maill.setTo(checkNull(finalmailid));
//                    System.out.println("[ECOM-WAR] AmendmentMailAjax mailid===>" + finalmailid);
                        String subjectt = admSubject;
                        subjectt = subjectt.replace("@apname", apname);
                        maill.setSubject(subjectt);
                        maill.setMessage(finalMsg);
                        maill.setAttachment(fileNames);
                        maill.setAttachmentpath((String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH) + File.separator + strRefNo + File.separator);
                        //1  mail.sendMail(authObj, agencymail);
                        ecomCntrlObj.sendMailFromEjb(maill, frommail, initiatorid, client_code);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }

                }
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AmendmentMailAjax.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AmendmentMailAjax.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String checkNullval(String str) {
        try {
            str = str.replaceAll("\"", "");
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public Map<String, List<String>> findDiffering(BokingDetailds oldbooking, BokingDetailds newbooking) throws JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node1 = objectMapper.valueToTree(oldbooking);
        JsonNode node2 = objectMapper.valueToTree(newbooking);
        System.out.println("[ECOM-WAR] AmendmentMailAjax findDiffering:::::::::::::::");
        Map<String, List<String>> differingFields = new HashMap<>();
        if (isJsonNodeEmpty(node1) && isJsonNodeEmpty(node2)) {
            if (!node1.equals(node2)) {
//                System.out.println("[ECOM-WAR] AmendmentMailAjax *****************************************************************");
//                System.out.println("[ECOM-WAR] AmendmentMailAjax node1==>" + node1);
//                System.out.println("[ECOM-WAR] AmendmentMailAjax node2==>" + node2);
//                System.out.println("[ECOM-WAR] AmendmentMailAjax *****************************************************************");
                differingFields = findDifferingFields("Booking", node1, node2);
                differingFields.putAll(differingFields);
            } else {

            }
        }
        return differingFields;
    }

    private String commanMasterDetails(HashMap carriageContract, AmendmentBookingFormBean amendment) {
        String output;
        output = "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-bottom: 1px solid #e0e0e0;\">\n"
                + "                            <tbody>\n"
                + "                                <tr><td></td></tr>\n"
                + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                + "                                    <td colspan='6' ><h2>Master Details</h2></td>\n"
                + "                                </tr>\n"
                + "\n"
                + "\n"
                + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                + "                                            <span>" + checkNullafterValue(amendment.getEcomBookNo()) + "</span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                + "                                            <span>Online Booking No</span>\n"
                + "                                        </span>\n"
                + "                                    </td>\n"
                + "\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                + "                                             <span>" + checkNullafterValue(amendment.getLrpBookNo()) + "</span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">LRP Booking No</span>\n"
                + "                                    </td>\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                + "                                            <span>" + checkNullafterValue(amendment.getEbook_agreename()) + "</span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Agreement Party</span>\n"
                + "                                    </td>\n"
                + "\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                + "                                            <span>" + (carriageContract != null ? checkNullafterValue((String) carriageContract.get(amendment.getCarrierContact())) : "") + "</span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Carriage Contract</span>\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "\n"
                + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                + "                                            <span>" + ("C".equalsIgnoreCase(amendment.getPreCarriage()) ? "Carrier" : ("M".equalsIgnoreCase(amendment.getPreCarriage()) ? "Merchant" : "N/A")) + "</span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                + "                                            <span>Haulage Origin</span>\n"
                + "                                        </span>\n"
                + "                                    </td>\n"
                + "\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                + "                                            <span>" + ("C".equalsIgnoreCase(amendment.getPreCarriage()) ? "Carrier" : ("M".equalsIgnoreCase(amendment.getPreCarriage()) ? "Merchant" : "N/A")) + "</span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Haulage Destination</span>\n"
                + "                                    </td>\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                + "                                            <span></span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"></span>\n"
                + "                                    </td>\n"
                + "\n"
                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                + "                                            <span></span>\n"
                + "                                        </p>\n"
                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"></span>\n"
                + "                                    </td>\n"
                + "                                </tr>\n";

        return output;
    }

    private String commonSIMasterDetails(String bookno, String si_no, String blno, String agencyRefNo) {
        String output = "";
        output = "   <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-bottom: 1px solid #FFC107 !important;\">\n"
                + "                            <tbody>\n"
                + "                                <tr>\n"
                + "                                    <td></td>\n"
                + "                                </tr>\n"
                + "                                <tr style=\"height:60px;overflow:hidden;margin-top:20px;padding:0 0 5px;\">\n"
                + "                                    <td style=\"font-size:16px;\">\n"
                + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0\">\n"
                + "                                            <span>" + bookno + "</span>\n"
                + "                                            <br>\n"
                + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Booking No </span>\n"
                + "                                        </span>\n"
                + "\n"
                + "                                    </td>\n"
                + "                                    <td  style=\"font-size:16px;\">\n"
                + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px;\">\n"
                + "                                            <span>" + si_no + "</span>\n"
                + "                                            <br>\n"
                + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">SI No </span>\n"
                + "                                        </span>\n"
                + "\n"
                + "                                    </td>\n";
        if (blno != null && blno.trim().length() > 0) {
            output += "                                    <td  style=\"font-size:16px;\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                    + "                                            <span>" + blno + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">BL No   </span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n";
        }
        output += "                                    <td  style=\"font-size:16px;\">\n"
                + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                + "                                            <span>" + checkNull(agencyRefNo) + "</span>\n"
                + "                                            <br>\n"
                + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Agency Ref No.   </span>\n"
                + "                                        </span>\n"
                + "\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "\n"
                + "                                <tr>\n"
                + "                                    <td></td>\n"
                + "                                </tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n";

        return output;
    }

    private String newAgencyamendmentBookingCompMail(Map masterChache, Map<String, List<String>> differingFields, String LRPBook_no, String EdiReadId, Map portlist, HashMap carriageContract, String bookingtype) throws ParseException, RemoteException {
//        Map portlist = (Map) masterCache.get("PORTLIST");
        Hashtable comlist = (Hashtable) masterChache.get("comList");
        Map eqpmap = (Map) masterChache.get("EQUIPMENT_TYPE");
        HashMap unitlis = new HashMap();
        unitlis.put("D", "Dry");
        unitlis.put("O", "Open");
        unitlis.put("R", "Reefer");
        unitlis.put("K", "Kilos");
        unitlis.put("P", "Pounds");
        unitlis.put("T", "Metric Tons");
        unitlis.put("C", "Celsius");
        unitlis.put("F", "Fahrenheit");
//        String apname = (String) masterCache.get("apname");
//        Map vessellist = (Map) masterCache.get("VESSELLIST");
//        String Enable_Amendment_Comparison_Mail = (String) masterCache.get("Enable_Amendment_Comparison_Mail");
        String message = "";
        String totcomm = null, finalCargoDes = null;
        Map<String, List<String>> differ = differingFields;
        String agencyrefno = "", agencyrefnumber = "";
//        agencyrefno = Validation.checkNull(eBookingMasterData.getEbook_agency_reference());
//        if (agencyrefno.length() > 0 && "CN".equalsIgnoreCase(returnVO.getAgencycountry())) {
//            agencyrefnumber = "<br><br>Agency Reference Number:" + agencyrefno;
//        }
//        System.out.println("[ECOM-WAR] AmendmentMailAjax totcommo" + totcomm);
//        System.out.println("[ECOM-WAR] AmendmentMailAjax finaltotalcargodesc" + finalCargoDes);
        String cargoprotect_check = "N";
        String cargotier = "";
        String amendmentDetails = "", masterDetails = "", customerDetails = "", cargoDetails = "", customeraddDetails = "", cargoaddDetails = "", customerremoveDetails = "", cargoremoveDetails = "", scheduleDetailss = "";
        Map<String, String> typeMapping = new HashMap<>();
        typeMapping.put("SH", "Shipper");
        typeMapping.put("AP", "Agreement Party");
        typeMapping.put("BP", "Booking Party");
        typeMapping.put("CN", "Consignee");
        typeMapping.put("NT", "Notify");
        typeMapping.put("N1", "Notify 1");
        typeMapping.put("FW", "Forwarder");
        typeMapping.put("WH", "Named Customer");
        typeMapping.put("NC", "Named Customer");
        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat eminputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
//        System.out.println("[ECOM-WAR] AmendmentMailAjax out map ");
        // Your modified loop
        StringBuilder finalmail = null;
        if (differ != null && !differ.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : differ.entrySet()) {
                String type = entry.getKey();
                List<String> values = entry.getValue();
//                System.out.println("[ECOM-WAR] AmendmentMailAjax entry==>" + entry);
                StringBuilder details = null, custdetails = null, cargodetail = null, scheduledetails = null;
                StringBuilder custAdd = null, cargoAdd = null;
                StringBuilder cusRemove = null, cargoRemove = null;

                String label = "", before = "", after = "";

                if (type.contains("edited")) {
//                    System.out.println("[ECOM-WAR] AmendmentMailAjax inside mail contant");
                    if (type.contains("cargoDetails")) {
                        String[] key = type.split("~");
                        cargodetail = new StringBuilder("<h4><u> Eqp. No. " + key[2] + " </u></h4><table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-bottom: 1px solid #e0e0e0;\"><tr> <th style=\"font-size:15px;color:#999;margin:0;padding:0;width:10%;\">Label</th> <th style=\"font-size:15px;color:#999;margin:0;padding:0;width: 10%\">Before Amendment</th><th style=\"font-size:15px;color:#999;margin:0;padding:0;width: 10%;\">After Amendment</th></tr>");
                        boolean labelMatched = false;
                        String afvolume = "", bevolume = "";
                        boolean volunit = true;
                        for (String val : values) {
                            String[] fieldValues = val.split("~~~");
                            if (fieldValues.length >= 2) {
                                label = checkNullval(fieldValues[0]);
                                before = checkNullval(fieldValues[1]);
                                after = checkNullval(fieldValues[2]);
                            }

                            switch (label) {
//                                case "eqp":
////                                    cargodetail.append(processField("Equipment", (String) unitlis.get(before), (String) unitlis.get(after)));
//                                    labelMatched = true;
//                                    break;
                                case "containerNo":
                                    cargodetail.append(processField("Eqp. No.", before, after));
                                    labelMatched = true;
                                    break;
                                case "eqpType":
                                    cargodetail.append(processField("Eqp. Size.", (String) eqpmap.get(before), (String) eqpmap.get(after)));
                                    labelMatched = true;
                                    break;
                                case "soc":
                                    cargodetail.append(processField("SOC", scottype(checkNull(before)), scottype(checkNull(after))));
                                    labelMatched = true;
                                    break;
                                case "oogflag":
                                    cargodetail.append(processField("OOG", scottype(checkNull(before)), scottype(checkNull(after))));
                                    labelMatched = true;
                                    break;
                                case "hzd":
                                    cargodetail.append(processField("Hazardous", scottype(checkNull(before)), scottype(checkNull(after))));
                                    labelMatched = true;
                                    break;
                                case "cargoWtUnit":
                                    cargodetail.append(processField("Cargo Weight", before, after));
                                    labelMatched = true;
                                    break;
                                case "grossWtUnit":
                                    cargodetail.append(processField("Gross Weight", before, after));
                                    labelMatched = true;
                                    break;
                                case "packagename":
                                    cargodetail.append(processField("Package", before, after));
                                    labelMatched = true;
                                    break;
                                case "actCommoName":
                                    cargodetail.append(processField("Commodity Desc", before, after));
                                    labelMatched = true;
                                    break;
                                case "carrierseal":
                                    cargodetail.append(processField("Carrier Seal ", before, after));
                                    labelMatched = true;
                                    break;
                                case "shipperseal":
                                    cargodetail.append(processField("Shipper Seal", before, after));
                                    labelMatched = true;
                                    break;
                                case "commodity":
                                    cargodetail.append(processField("Commodity", checkNull((String) comlist.get(checkNull(before))), checkNull((String) comlist.get(checkNull(after)))));
                                    labelMatched = true;
                                    break;
                                case "marksNumbers":
                                    cargodetail.append(processField("Marks No.", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
//                                case "marksDesc":
//                                    cargodetail.append(processField("Commodity Desc", checkNull((String) before), checkNull((String) after)));
//                                    labelMatched = true;
//                                    break;
                                case "hscode":
                                    cargodetail.append(processField("HS Code", checkNull((String) comlist.get(checkNull(before))), checkNull((String) comlist.get(checkNull(after)))));
                                    labelMatched = true;
                                    break;
                                case "volUnit":
                                    cargodetail.append(processField("Volume", checkNullafterValue(bevolume) + " " + (("F".equalsIgnoreCase(checkNull(before))) ? "Cubic Feet" : (("M".equalsIgnoreCase(checkNull(before))) ? "Cubic Meter" : "")), checkNullafterValue(after) + " " + (("F".equalsIgnoreCase(checkNull(afvolume))) ? "Cubic Feet" : (("M".equalsIgnoreCase(checkNull(after))) ? "Cubic Meter" : ""))));
                                    volunit = false;
                                    labelMatched = true;
                                    break;
                                case "tareWt":
                                    cargodetail.append(processField("Tare Weight", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "packageQty":
                                    cargodetail.append(processField("No.of Packages", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "eqpStatus":
                                    cargodetail.append(processField("Eqp.Status ", ("M".equalsIgnoreCase(checkNull(before)) ? "Empty" : "Laden"), ("M".equalsIgnoreCase(checkNull(after)) ? "Empty" : "Laden")));
                                    labelMatched = true;
                                    break;
                                case "netWt":
                                    cargodetail.append(processField("Net Weight No.", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "dimUnit":
                                    cargodetail.append(processField("Unit", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "owLeft":
                                    cargodetail.append(processField("Left", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "owRight":
                                    cargodetail.append(processField("Right", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "olForward":
                                    cargodetail.append(processField("Forward", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "olAft":
                                    cargodetail.append(processField("Aft", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "oh":
                                    cargodetail.append(processField("Top", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                /* case "marinePollut":
                                    cargodetail.append(processField("Marine Pollutant", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "unno":
                                    cargodetail.append(processField("UN No", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "flashPoint":
                                    cargodetail.append(processField("Flash Point", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "dgClass":
                                    cargodetail.append(processField("DG Class*", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "shippingName":
                                    cargodetail.append(processField("Shipping Name", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;*/
                                case "nor":
                                    cargodetail.append(processField("Non-Operating Reefer", checkNull(scottype((String) before)), checkNull(scottype((String) after))));
                                    labelMatched = true;
                                    break;
                                case "presetTemp":
                                    cargodetail.append(processField("Set Temp", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "minTemp":
                                    cargodetail.append(processField("Min Temp", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "maxTemp":
                                    cargodetail.append(processField("Max Temp", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "tempUnit":
                                    cargodetail.append(processField("Temp Unit*", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "ventilation":
                                    cargodetail.append(processField("Ventilation(CMH)", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "volume":
                                    afvolume = checkNull(after);
                                    bevolume = checkNull(before);
                                    if (volunit) {
                                        cargodetail.append(processField("Volume", checkNull((String) bevolume), checkNull((String) afvolume)));
                                    }
                                    labelMatched = true;
                                    break;
                            }
                        }
                        if (labelMatched) {
                            cargodetail.append("</table>");
                        } else {
                            cargodetail = new StringBuilder();
                        }

                    } else if (type.contains("customerDetails")) {
                        String[] key = type.split("~");
                        custdetails = new StringBuilder("<h4><u>" + typeMapping.get(key[2]) + " </u></h4><table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-bottom: 1px solid #e0e0e0;\"><tr> <th style=\"font-size:15px;color:#999;margin:0;padding:0;width: 30%;\">Label</th> <th style=\"font-size:15px;color:#999;margin:0;padding:0;width: 30%\">Before Amendment</th><th style=\"font-size:15px;color:#999;margin:0;padding:0;width: 30%;\">After Amendment</th></tr>");
                        boolean labelMatched = false;
                        for (String val : values) {
                            String[] fieldValues = val.split("~~~");
                            if (fieldValues.length >= 2) {
                                label = checkNullval(fieldValues[0]);
                                before = checkNullval(fieldValues[1]);
                                after = checkNullval(fieldValues[2]);
                            }
                            switch (label) {
                                case "name":
                                    custdetails.append(processField("name", before, after));
                                    labelMatched = true;
                                    break;
                                case "address":
                                    custdetails.append(processField("address", before, after));
                                    labelMatched = true;
                                    break;
                                case "address1":
                                    custdetails.append(processField("address1", before, after));
                                    labelMatched = true;
                                    break;
                                case "city":
                                    custdetails.append(processField("City", before, after));
                                    labelMatched = true;
                                    break;
                                case "countryId":
                                    custdetails.append(processField("Coutry", before, after));
                                    labelMatched = true;
                                    break;
                                case "phoneNo":
                                    custdetails.append(processField("Telephone", before, after));
                                    labelMatched = true;
                                    break;
                                case "fax":
                                    custdetails.append(processField("Fax", before, after));
                                    labelMatched = true;
                                    break;
                                case "email":
                                    custdetails.append(processField("Email", before, after));
                                    labelMatched = true;
                                    break;
                            }
                        }
                        if (labelMatched) {
                            custdetails.append("</table>");
                        } else {
                            custdetails = new StringBuilder();
                        }
                        custdetails.append("</table>");
                    } else if (type.contains("scheduleDetails")) {
                        String[] key = type.split("~");
                        String rsl = ecomAmdMail.generateSheduledetails(LRPBook_no, EdiReadId, portlist);
//                        System.out.println("[ECOM-WAR] AmendmentMailAjax rsl===>" + rsl);
                        scheduledetails = new StringBuilder(rsl);
                    } else {
                        details = new StringBuilder("<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:15px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-bottom: 1px solid #e0e0e0;\"><tr><h4><u> + Additional Information </u></h4></tr><tr> <th style=\"font-size:15px;color:#999;margin:0;padding:0;width:30%;\">Label</th> <th style=\"font-size:15px;color:#999;margin:0;padding:0;width: 30%\">Before Amendment</th><th style=\"font-size:15px;color:#999;margin:0;padding:0;width:30%;\">After Amendment</th></tr>");
                        boolean labelMatched = false;
                        for (String val : values) {
                            String[] fieldValues = val.split("~~~");
                            if (fieldValues.length >= 2) {
                                label = checkNullval(fieldValues[0]);
                                before = checkNullval(fieldValues[1]);
                                after = checkNullval(fieldValues[2]);
                            }
                            if (label.contains("cargoprotectid")) {
                                List<CargoProtectVo> becplis = null;
                                List<CargoProtectVo> afcplis = null;
                                String bfcargo = "", bfmaxi = "", bfpreshi = "", afcargo = "", afmaxi = "", afpreshi = "";
                                int ebvalue = !"".equalsIgnoreCase(checkNull(before)) ? Integer.parseInt(before) : 0;
                                int afvalue = !"".equalsIgnoreCase(checkNull(after)) ? Integer.parseInt(after) : 0;
                                if (ebvalue != 0) {
                                    becplis = bookingObj.getCargoProtectDetails("id", checkNull(before));
                                    bfcargo = becplis.get(0).getTier().toString();
                                    bfmaxi = becplis.get(0).getMaxcoverage().toString();
                                    bfpreshi = becplis.get(0).getPershipment().toString();
//                                    System.out.println("[ECOM-WAR] AmendmentMailAjax becplis==>" + becplis.get(0).getTier() + "===>" + becplis.get(0).getMaxcoverage() + "====>" + becplis.get(0).getPershipment());
                                }
                                if (afvalue != 0) {
                                    afcplis = bookingObj.getCargoProtectDetails("id", checkNull(after));
//                                    System.out.println("[ECOM-WAR] AmendmentMailAjax afcplis==>" + afcplis.get(0).getTier() + "===>" + afcplis.get(0).getMaxcoverage() + "====>" + afcplis.get(0).getPershipment());
                                    afcargo = afcplis.get(0).getTier().toString();
                                    afmaxi = afcplis.get(0).getMaxcoverage().toString();
                                    afpreshi = afcplis.get(0).getPershipment().toString();
                                }
                                if (!"None".equalsIgnoreCase(checkNullbefourValue(bfcargo)) || !"None".equalsIgnoreCase(checkNullafterValue(afcargo))) {
                                    cargotier = "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">   <tr><td></td></tr>\n"
                                            + "                                 <tr><td></td></tr><tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                                            + "                                    <td colspan='6' ><h2>Cargo Details</h2></td>\n"
                                            + "                                </tr></table>";
                                    cargotier += "<span style=\"font-size:15px;color:#999;margin:0;padding:0;width: 30%\">Before Amendment</span></br>" + generatecargoTable(checkNullbefourValue(bfcargo), checkNullbefourValue(bfmaxi), checkNullbefourValue(bfpreshi));
                                    cargotier += "<span style=\"font-size:15px;color:#999;margin:0;padding:0;width: 30%\">After Amendment</span></br>" + generatecargoTable(checkNullbefourValue(afcargo), checkNullbefourValue(afmaxi), checkNullbefourValue(afpreshi));
                                    cargoprotect_check = "Y";
                                }
                            } else {
                                if ("Booking".equalsIgnoreCase(bookingtype)) {
                                    switch (label) {
                                        case "carrierContact":
                                            before = (before != null ? checkNullafterValue((String) carriageContract.get(before)) : "");
                                            after = (after != null ? checkNullafterValue((String) carriageContract.get(after)) : "");
                                            if (!"None".equalsIgnoreCase(checkNullbefourValue(before)) || !"None".equalsIgnoreCase(checkNullafterValue(after))) {
                                                details.append(processField("Carriage Contract", before, after));
                                                labelMatched = true;
                                            }
                                            break;
                                        case "ebook_haulagetype":
                                            details.append(processField("Haulage Type", gethaulagetype(checkNull(before)), gethaulagetype(checkNull(after))));
                                            labelMatched = true;
                                            break;
                                        case "ebook_pickup_date":
                                            Date bedate = null,
                                             afdate = null;
                                            String outputDate = "",
                                             afoutputDate = "";
                                            if (!"".equalsIgnoreCase(checkNull(before))) {
                                                bedate = inputFormatter.parse(before);
                                                outputDate = outputFormatter.format(bedate);
                                            }
                                            if (!"".equalsIgnoreCase(checkNull(after))) {
                                                afdate = inputFormatter.parse(after);
                                                afoutputDate = outputFormatter.format(afdate);
                                            }
                                            details.append(processField("Pickup Date", checkNull(outputDate), checkNull(afoutputDate)));
                                            break;
                                        case "ebook_drop_date":
                                            Date drbedate = null,
                                             drafdate = null;
                                            String droutputDate = "",
                                             drafoutputDate = "";
                                            if (!"".equalsIgnoreCase(checkNull(before))) {
                                                drbedate = inputFormatter.parse(before);
                                                droutputDate = outputFormatter.format(drbedate);
                                            }
                                            if (!"".equalsIgnoreCase(checkNull(after))) {
                                                drafdate = inputFormatter.parse(after);
                                                drafoutputDate = outputFormatter.format(drafdate);
                                            }
                                            details.append(processField("Drop Date", checkNull(droutputDate), checkNull(drafoutputDate)));
                                            break;
                                        case "ebook_haulercode":
                                            details.append(processField("Haulier Code", before, after));
                                            labelMatched = true;
                                            break;
                                        case "ebook_custpo":
                                            details.append(processField("Customer PO", before, after));
                                            break;
                                        case "ebook_exportref":
                                            details.append(processField("Export Reference", before, after));
                                            labelMatched = true;
                                            break;
                                        case "ebook_reftype":
                                            details.append(processField("Release Type", before, after));
                                            labelMatched = true;
                                            break;
                                        case "ebook_pickup_point":
                                            details.append(processField("Empty Place of Pick Up", ("T".equalsIgnoreCase(before) ? "Terminal" : (("D".equalsIgnoreCase(before) ? "Depot" : ""))), ("T".equalsIgnoreCase(after) ? "Terminal" : (("D".equalsIgnoreCase(after) ? "Depot" : "")))));
                                            labelMatched = true;
                                            break;
                                        case "emptyPickUpDate":
                                            Date embedate = null,
                                             emafdate = null;
                                            String emoutputDate = "",
                                             emafoutputDate = "";
                                            if (!"".equalsIgnoreCase(checkNull(before))) {
                                                embedate = eminputFormatter.parse(before);
                                                emoutputDate = outputFormatter.format(embedate);
                                            }
                                            if (!"".equalsIgnoreCase(checkNull(after))) {
                                                emafdate = eminputFormatter.parse(after);
                                                emafoutputDate = outputFormatter.format(emafdate);
                                            }
                                            details.append(processField("Empty Place of Pick Up Date", checkNull(emoutputDate), checkNull(emafoutputDate)));
                                            labelMatched = true;
                                            break;
                                        case "acid":
                                            if (!"None".equalsIgnoreCase(checkNullbefourValue(before)) || !"None".equalsIgnoreCase(checkNullafterValue(after))) {
                                                details.append(processField("ACID ", before, after));
                                                labelMatched = true;
                                            }
                                            break;
                                        case "agency_reference":
                                            details.append(processField("Agncy Ref No:", before, after));
                                            labelMatched = true;
                                            break;
                                        case "customerreference":
                                            details.append(processField("Customer Reference", before, after));
                                            labelMatched = true;
                                            break;
                                        case "shipperReference":
                                            details.append(processField("DPW Shipper Code", before, after));
                                            labelMatched = true;
                                            break;
                                        case "shipperReferenceType":
                                            details.append(processField("DPW Shipper Type", before, after));
                                            labelMatched = true;
                                            break;
                                    }
                                } else {
                                    switch (label) {
                                        case "acid":
                                            details.append(processField("ACID ", before, after));
                                            labelMatched = true;
                                            break;
                                        case "blType":
                                            details.append(processField("BL Type ", before, after));
                                            labelMatched = true;
                                            break;
                                    }

                                }
                            }
                        }
                        if (labelMatched) {
                            details.append("</table>");
                        } else {
                            details = new StringBuilder();
                        }

                    }
                } else if (type.contains("Added") && "Booking".equalsIgnoreCase(bookingtype)) {

                    if (type.contains("cargoDetails")) {
                        String[] key = type.split("~");

                        String flashPoint = "", maxTemp = "", tempUnit = "", ventilation = "", dgClass = "", shippingName = "", nor = "", presetTemp = "", minTemp = "", olForward = "", olAft = "", oh = "", unno = "", marinePollut = "", dimUnit = "", owLeft = "", owRight = "", oog = "", markno = "", netWt = "", volume = "", eqpStatus = "", packageQty = "", tareWt = "", volUnit = "", rsl = "", equipment = "", eqpNo = "", equipmentype = "", eqpSize = "", SOC = "", hazardous = "", cargoWeight = "", grossWeight = "", cargotype = "", packages = "", actualCommodityName = "", commodity = "", hsCode = "";
                        cargotype = "Eqp. No. " + key[1] + " Added";
                        boolean labelMatched = false;
                        for (String val : values) {
                            String[] fieldValues = val.split("~~~");
                            if (fieldValues.length == 2) {
                                label = checkNullval(fieldValues[0]);
                                after = checkNullval(fieldValues[1]);
                            }
                            switch (label) {
                                case "eqp":
                                    equipment = after;
                                    labelMatched = true;
                                    break;
                                case "eqpType":
                                    equipmentype = after;
                                    labelMatched = true;
                                    break;
                                case "containerNo":
                                    eqpNo = after;
                                    labelMatched = true;
                                    break;
                                case "cargo":
                                    eqpSize = after;
                                    labelMatched = true;
                                    break;
                                case "soc":
                                    SOC = scottype(checkNull(after));
                                    labelMatched = true;
                                    break;
                                case "hzd":
                                    hazardous = scottype(checkNull(after));
                                    labelMatched = true;
                                    break;
                                case "cargoWtUnit":
                                    cargoWeight = after;
                                    labelMatched = true;
                                    break;
                                case "grossWtUnit":
                                    grossWeight = after;
                                    break;
                                case "packagename":
                                    packages = after;
                                    labelMatched = true;
                                    break;
                                case "actCommoName":
                                    actualCommodityName = after;
                                    labelMatched = true;
                                    break;
                                case "commodity":
//                                    System.out.println("[ECOM-WAR] AmendmentMailAjax checkNull(after)==>" + checkNull(after));
//                                    System.out.println("[ECOM-WAR] AmendmentMailAjax checkNull(after)==>" + comlist.get(checkNull(after).toString()));
//                                    System.out.println("[ECOM-WAR] AmendmentMailAjax checkNull(after)==>" + checkNull((String) comlist.get(checkNull(after))));
                                    commodity = checkNull((String) comlist.get(checkNull(after)));
                                    labelMatched = true;
                                    break;
                                case "hscode":
                                    hsCode = checkNull((String) comlist.get(checkNull(after)));
                                    labelMatched = true;
                                    break;
                                case "volUnit":
                                    volUnit = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "tareWt":
                                    tareWt = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "packageQty":
                                    packageQty = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "eqpStatus":
                                    eqpStatus = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "volume":
                                    volume = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "netWt":
                                    netWt = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "marksNumbers":
                                    markno = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "oogflag":
                                    oog = checkNull(scottype(checkNull(after)));
                                    labelMatched = true;
                                    break;
                                case "dimUnit":
                                    dimUnit = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "owLeft":
                                    owLeft = checkNull((String) after);

                                    break;
                                case "owRight":
                                    owRight = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "olForward":
                                    olForward = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "olAft":
                                    olAft = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "oh":
                                    oh = checkNull((String) after);
                                    labelMatched = true;
                                    cargodetail.append(processField("Top", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "marinePollut":
                                    marinePollut = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "unno":
                                    unno = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "flashPoint":
                                    flashPoint = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "dgClass":
                                    dgClass = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "shippingName":
                                    shippingName = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "nor":
                                    nor = checkNull(scottype((String) after));
                                    labelMatched = true;

                                    break;
                                case "presetTemp":
                                    presetTemp = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "minTemp":
                                    minTemp = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "maxTemp":
                                    maxTemp = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "tempUnit":
                                    tempUnit = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "ventilation":
                                    ventilation = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                            }
                        }
                        if (labelMatched) {
                            rsl = generateCargo(eqpmap, equipmentype, cargotype, equipment, eqpNo, eqpSize, SOC, hazardous, cargoWeight, grossWeight, packages, actualCommodityName, commodity, hsCode, volUnit, tareWt, packageQty, eqpStatus, volume, netWt, markno, oog, flashPoint, maxTemp, tempUnit, ventilation, dgClass, shippingName, nor, presetTemp, minTemp, olForward, olAft, oh, unno, marinePollut, dimUnit, owLeft, owRight);
                            cargoAdd = new StringBuilder(rsl);
                        } else {
                            cargoAdd = new StringBuilder();
                        }

                    } else if (type.contains("customerDetails")) {
                        String[] key = type.split("~");
                        String name = "", address = "", address1 = "", City = "", Coutry = "", Telephone = "", Fax = "", Email = "", custype = "";
                        custype = typeMapping.get(key[1]) + "~Added";
                        boolean labelMatched = false;
                        for (String val : values) {
                            String[] fieldValues = val.split("~~~");
                            if (fieldValues.length == 2) {
                                label = checkNullval(fieldValues[0]);
                                after = checkNullval(fieldValues[1]);
                            }
                            switch (label) {
                                case "name":
                                    name = after;
                                    labelMatched = true;
                                    break;
                                case "address":
                                    address = after;
                                    labelMatched = true;
                                    break;
                                case "address1":
                                    address1 = after;
                                    labelMatched = true;
                                    break;
                                case "city":
                                    City = after;
                                    labelMatched = true;
                                    break;
                                case "countryId":
                                    Coutry = after;
                                    labelMatched = true;
                                    break;
                                case "phoneNo":
                                    Telephone = after;
                                    labelMatched = true;
                                    break;
                                case "fax":
                                    Fax = after;
                                    labelMatched = true;
                                    break;
                                case "email":
                                    Email = after;
                                    labelMatched = true;
                                    break;
                            }
                        }
                        if (labelMatched) {
                            String rsl = generateTable(custype, name, address, address1, City, Coutry, Telephone, Fax, Email);
//                            System.out.println("[ECOM-WAR] AmendmentMailAjax result==>" + rsl);
                            custAdd = new StringBuilder(rsl);
                        } else {
                            custAdd = new StringBuilder();
                        }

                    }
                } else if (type.contains("removed") && "Booking".equalsIgnoreCase(bookingtype)) {
                    if (type.contains("cargoDetails")) {
                        String[] key = type.split("~");

                        String flashPoint = "", maxTemp = "", tempUnit = "", ventilation = "", dgClass = "", shippingName = "", nor = "", presetTemp = "", minTemp = "", olForward = "", olAft = "", oh = "", unno = "", marinePollut = "", dimUnit = "", owLeft = "", owRight = "", oog = "", markno = "", netWt = "", volume = "", eqpStatus = "", packageQty = "", tareWt = "", volUnit = "", rsl = "", equipment = "", cargotype = "", equipmentype = "", eqpNo = "", eqpSize = "", SOC = "", hazardous = "", cargoWeight = "", grossWeight = "", packages = "", actualCommodityName = "", commodity = "", hsCode = "";
                        cargotype = "Eqp. No. " + key[1] + " Removed";
                        boolean labelMatched = false;
                        for (String val : values) {
                            String[] fieldValues = val.split("~~~");
                            if (fieldValues.length == 2) {
                                label = checkNullval(fieldValues[0]);
                                after = checkNullval(fieldValues[1]);
                            }
                            switch (label) {
                                case "eqp":
                                    equipment = after;
                                    labelMatched = true;
                                    break;
                                case "eqpType":
                                    equipmentype = after;
                                    labelMatched = true;
                                    break;
                                case "containerNo":
                                    eqpNo = after;
                                    labelMatched = true;
                                    break;
                                case "cargo":
                                    eqpSize = after;
                                    labelMatched = true;
                                    break;
                                case "soc":
                                    SOC = after;
                                    labelMatched = true;
                                    break;
                                case "hzd":
                                    hazardous = scottype(checkNull(after));
                                    labelMatched = true;
                                    break;
                                case "cargoWtUnit":
                                    cargoWeight = after;
                                    labelMatched = true;
                                    break;
                                case "grossWtUnit":
                                    grossWeight = after;
                                    break;
                                case "packagename":
                                    packages = after;
                                    labelMatched = true;
                                    break;
                                case "actCommoName":
                                    actualCommodityName = after;
                                    labelMatched = true;
                                    break;
                                case "commodity":
                                    commodity = checkNull((String) comlist.get(checkNull(after)));
                                    labelMatched = true;
                                    break;
                                case "hscode":
                                    hsCode = checkNull((String) comlist.get(checkNull(after)));
                                    labelMatched = true;
                                    break;
                                case "volUnit":
                                    volUnit = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "tareWt":
                                    tareWt = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "packageQty":
                                    packageQty = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "eqpStatus":
                                    eqpStatus = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "volume":
                                    volume = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "netWt":
                                    netWt = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "marksNumbers":
                                    markno = checkNull((String) after);
                                    labelMatched = true;
                                    break;
                                case "oogflag":
                                    oog = checkNull(scottype(checkNull(after)));
                                    labelMatched = true;
                                    break;
                                case "nor":
                                    nor = checkNull(scottype((String) after));
                                    labelMatched = true;
                                    break;
                                case "presetTemp":
                                    presetTemp = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "minTemp":
                                    minTemp = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "maxTemp":
                                    maxTemp = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "tempUnit":
                                    tempUnit = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "ventilation":
                                    ventilation = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "dimUnit":
                                    dimUnit = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "owLeft":
                                    owLeft = checkNull((String) after);

                                    break;
                                case "owRight":
                                    owRight = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "olForward":
                                    olForward = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "olAft":
                                    olAft = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "oh":
                                    oh = checkNull((String) after);
                                    labelMatched = true;
                                    cargodetail.append(processField("Top", checkNull((String) before), checkNull((String) after)));
                                    labelMatched = true;
                                    break;
                                case "marinePollut":
                                    marinePollut = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "unno":
                                    unno = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "flashPoint":
                                    flashPoint = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "dgClass":
                                    dgClass = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                                case "shippingName":
                                    shippingName = checkNull((String) after);
                                    labelMatched = true;

                                    break;
                            }
                        }
                        if (labelMatched) {
                            rsl = generateCargo(eqpmap, equipmentype, cargotype, equipment, eqpNo, eqpSize, SOC, hazardous, cargoWeight, grossWeight, packages, actualCommodityName, commodity, hsCode, volUnit, tareWt, packageQty, eqpStatus, volume, netWt, markno, oog, flashPoint, maxTemp, tempUnit, ventilation, dgClass, shippingName, nor, presetTemp, minTemp, olForward, olAft, oh, unno, marinePollut, dimUnit, owLeft, owRight);
//                            System.out.println("[ECOM-WAR] AmendmentMailAjax result==>" + rsl);
                            cargoRemove = new StringBuilder(rsl);
                        } else {
                            cargoRemove = new StringBuilder();
                        }

                    } else if (type.contains("customerDetails")) {
                        String[] key = type.split("~");
//                        System.out.println("[ECOM-WAR] AmendmentMailAjax key==>" + key);
                        String rsl = "", name = "", address = "", address1 = "", City = "", Coutry = "", Telephone = "", Fax = "", Email = "", custype = "";
                        custype = typeMapping.get(key[1]) + "~Removed";
                        boolean labelMatched = false;
                        for (String val : values) {
                            String[] fieldValues = val.split("~~~");
                            if (fieldValues.length == 2) {
                                label = checkNullval(fieldValues[0]);
                                after = checkNullval(fieldValues[1]);
                            }
                            switch (label) {
                                case "name":
                                    name = after;
                                    labelMatched = true;
                                    break;
                                case "address":
                                    address = after;
                                    labelMatched = true;
                                    break;
                                case "address1":
                                    address1 = after;
                                    labelMatched = true;
                                    break;
                                case "city":
                                    City = after;
                                    labelMatched = true;
                                    break;
                                case "countryId":
                                    Coutry = after;
                                    labelMatched = true;
                                    break;
                                case "phoneNo":
                                    Telephone = after;
                                    labelMatched = true;
                                    break;
                                case "fax":
                                    Fax = after;
                                    labelMatched = true;
                                    break;
                                case "email":
                                    Email = after;
                                    labelMatched = true;
                                    break;
                            }
                        }
                        if (labelMatched) {
                            rsl = generateTable(custype, name, address, address1, City, Coutry, Telephone, Fax, Email);
                            cusRemove = new StringBuilder(rsl);
                        } else {
                            cusRemove = new StringBuilder();
                        }

                    }
                }

                if (type.contains("Added") && "Booking".equalsIgnoreCase(bookingtype)) {
                    if (type.contains("customerDetails")) {
                        customeraddDetails += custAdd;
                    } else if (type.contains("cargoDetails")) {
                        cargoaddDetails += cargoAdd;
                    }
                } else if (type.contains("removed") && "Booking".equalsIgnoreCase(bookingtype)) {
                    if (type.contains("customerDetails")) {
                        customerremoveDetails += cusRemove;
                    } else if (type.contains("cargoDetails")) {
                        cargoremoveDetails += cargoRemove;
                    }
                } else if (type.contains("edited")) {
                    if (type.contains("customerDetails")) {
                        customerDetails += custdetails;
                    } else if (type.contains("cargoDetails")) {
                        cargoDetails += cargodetail;
                    } else if (type.contains("scheduleDetails")) {
                        scheduleDetailss += scheduledetails;
                    } else {
                        masterDetails += details;
                    }
                }

                /*    System.out.println("[ECOM-WAR] AmendmentMailAjax cargoDetails==>" + cargoDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax masterDetails==>" + masterDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax customerDetails==>" + customerDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax customeraddDetails==>" + customeraddDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax cargoaddDetails==>" + cargoaddDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax customerDetails==>" + customerDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax customerremoveDetails==>" + customerremoveDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax cargoremoveDetails==>" + cargoremoveDetails);
                System.out.println("[ECOM-WAR] AmendmentMailAjax scheduleDetailss==>" + scheduleDetailss);*/
            }
            finalmail = new StringBuilder();
            if (!"".equalsIgnoreCase(masterDetails) || !"".equalsIgnoreCase(scheduleDetailss)) {
                String cargoprotect = "Y".equalsIgnoreCase(cargoprotect_check) ? cargotier : "";
                finalmail.append(masterDetails + scheduleDetailss + cargoprotect);
            }
            if (!"".equalsIgnoreCase(customerDetails) || !"".equalsIgnoreCase(customeraddDetails) || !"".equalsIgnoreCase(customerremoveDetails)) {
                finalmail.append("<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">   <tr><td></td></tr>\n"
                        + "         <tr><td></td></tr><tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                        + "        <td colspan='6' ><h2>Customer Details</h2></td>\n"
                        + "  </tr></table>" + customerDetails + customeraddDetails + customerremoveDetails);
            }
            if (!"".equalsIgnoreCase(cargoDetails) || !"".equalsIgnoreCase(cargoaddDetails) || !"".equalsIgnoreCase(cargoremoveDetails)) {

                finalmail.append("<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">   <tr><td></td></tr>\n"
                        + "           <tr><td></td></tr><tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                        + "          <td colspan='6' ><h2>Cargo Details</h2></td>\n"
                        + "          </tr></table>" + cargoDetails + cargoaddDetails + cargoremoveDetails);
            }
            String finalMailContent = finalmail.toString();
            System.out.println(finalMailContent);
            message = finalMailContent;

        } else {
            message = "";
        }
//        System.out.println("[ECOM-WAR] AmendmentMailAjax amend agency<<returnVO.getmsg" + message + "================>");
        return message;
    }

    private String processField(String fieldName, String before, String after) {
        return "<tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\"><td>" + checkNullafterValue(fieldName) + "</td><td style=\"padding: 1px; word-wrap: break-word;\">" + checkNullbefourValue(before) + "</td><td style=\"padding: 1px; word-wrap: break-word;\">" + checkNullafterValue(after) + "</td></tr>";
    }

    private String generateTable(String type, String name, String address, String address1, String City, String Coutry, String Telephone, String Fax, String Email) {
        // Main table headers
        String[] header = type.split("~");
        StringBuilder tableCode = new StringBuilder("<h4><u>" + header[0] + " " + header[1] + "</u></h4><table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-bottom: 1px solid #e0e0e0;\">\n  <tr>\n    <th style=\"font-size:15px;color:#999;margin:0;padding:0;width:30%;\">Customer Type</th>\n    <th style=\"font-size:15px;color:#999;margin:0;padding:0;width:30%;\">Customer Name</th>\n    <th style=\"font-size:15px;color:#999;margin:0;padding:0;width:30%;\">Address</th>\n  </tr>\n");

        // Data row
        tableCode.append("  <tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n    <td style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">" + checkNullafterValue(header[0]) + "</td>\n    <td style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">" + checkNullafterValue(name) + "</td>\n    <td>\n");

        // Nested table for Address
        tableCode.append("      <table>\n");
        tableCode.append("        <tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n          <td>address1</td>\n                  <td style=\"word-wrap: break-word;\">" + checkNullafterValue(address1) + "</td>\n        </tr>\n");
        tableCode.append("        <tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n          <td>City</td>\n                  <td style=\"word-wrap: break-word;\">" + checkNullafterValue(City) + "</td>\n        </tr>\n");
        tableCode.append("        <tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n          <td>Country</td>\n                  <td style=\"word-wrap: break-word;\">" + checkNullafterValue(Coutry) + "</td>\n        </tr>\n");
        tableCode.append("        <tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n          <td>Telephone</td>\n                 <td style=\"word-wrap: break-word;\">" + checkNullafterValue(Telephone) + "</td>\n        </tr>\n");
        tableCode.append("        <tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n          <td>Fax</td>\n                  <td style=\"word-wrap: break-word;\">" + checkNullafterValue(Fax) + "</td>\n        </tr>\n");
        tableCode.append("        <tr style=\"font-size:12px;margin:0;padding:0\"><p style=\"font-weight:700;margin:0 0 5px;padding:0\"> \n          <td>Email</td>\n                <td style=\"word-wrap: break-word;\">" + checkNullafterValue(Email) + "</td>\n        </tr>\n");

        tableCode.append("      </table>\n    </td>\n  </tr>\n</table>");

        return tableCode.toString();
    }

    private String generateCargo(Map eqpmap, String equipmentype, String cargotype, String equipment, String eqpNo, String eqpSize, String SOC, String hazardous, String cargoWeight, String grossWeight, String packages, String actualCommodityName, String commodity, String hsCode, String volUnit, String tareWt, String packageQty, String eqpStatus, String volume, String netWt, String marksNo, String oog, String flashPoint, String maxTemp, String tempUnit, String ventilation, String dgClass, String shippingName, String nor, String presetTemp, String minTemp, String olForward, String olAft, String oh, String unno, String marinePollut, String dimUnit, String owLeft, String owRight) {
        HashMap unitlis = new HashMap();
        unitlis.put("D", "Dry");
        unitlis.put("O", "Open");
        unitlis.put("R", "Reefer");
        unitlis.put("K", "Kilos");
        unitlis.put("P", "Pounds");
        unitlis.put("T", "Metric Tons");
        unitlis.put("C", "Celsius");
        unitlis.put("F", "Fahrenheit");
//        System.out.println("[ECOM-WAR] AmendmentMailAjax tareWt==>" + tareWt + "");
        String output = "<h4><u>" + cargotype + "</u></h4><table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-bottom: 1px solid #e0e0e0;\">"
                + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(eqpNo) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp. No.</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue((String) eqpmap.get(equipmentype)) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp. Size.</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + ("M".equalsIgnoreCase(checkNull(eqpStatus)) ? "Empty" : "Laden") + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp.Status</span></td>"
                + "</tr>"
                + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(SOC) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">SOC</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(hazardous) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Hazardous</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(oog) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">OOG</span></td>"
                + "</tr>"
                + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(grossWeight) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Gross Weight</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(packages) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Package</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(volume) + " " + (("F".equalsIgnoreCase(checkNull(volUnit))) ? "Cubic Feet" : (("M".equalsIgnoreCase(checkNull(volUnit))) ? "Cubic Meter" : "")) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Volume</span></td>"
                + "</tr>"
                + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(tareWt) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Tare Weight</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(packageQty) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">No.of Packages</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + marksNo + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Marks No.</span></td>"
                + "</tr>"
                + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(hsCode) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">HS Code</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(actualCommodityName) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Actual Commodity Name</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(netWt) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Net Weight No.</span></td>"
                + "</tr>"
                + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(cargoWeight) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Cargo Weight</span></td>"
                + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:45%\" ><span>" + checkNullafterValue(commodity) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Commodity</span></td>"
                + "</tr>";
        if ("Yes".equalsIgnoreCase(nor)) {
            output += " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(nor) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Non-Operating Reefer</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(presetTemp) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Set Temp</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(minTemp) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Min Temp</span></td>"
                    + "</tr>"
                    + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(maxTemp) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Max Temp</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(tempUnit) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Temp Unit</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(ventilation) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Ventilation(CMH)</span></td>"
                    + "</tr>";

        }
        if ("Yes".equalsIgnoreCase(oog)) {
            output += " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(dimUnit) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Unit</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(owLeft) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Left</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(owRight) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Right</span></td>"
                    + "</tr>"
                    + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(olForward) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Forward</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(olAft) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Aft</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(oh) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Top</span></td>"
                    + "</tr>";

        }
        if ("Yes".equalsIgnoreCase(hazardous)) {
            output += " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(marinePollut) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Marine Pollutan</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(unno) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">UN No</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(flashPoint) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Flash Point</span></td>"
                    + "</tr>"
                    + " <tr style=\\\"margin:0;padding:0;line-height: 15px;\\\" >"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(dgClass) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">DG Class</span></td>"
                    + "<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%\" ><span>" + checkNullafterValue(shippingName) + "</span></br><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Shipping Name</span></td>"
                    + "</tr>";

        }
        output += "</table>";
//        System.out.println("[ECOM-WAR] AmendmentMailAjax output==>" + output);
        return output;
    }

    private static Map<String, List<String>> findDifferingFields(String path, JsonNode node1, JsonNode node2) {
        Map<String, List<String>> differingFields = new HashMap<>();
        List lststring = new ArrayList<>();
        if (node1.isObject() && node2.isObject()) {
            node1.fieldNames().forEachRemaining(fieldName -> {
                String fld = path.isEmpty() ? fieldName : path + "~" + fieldName + "~edited";
                if (node2.has(fieldName)) {
                    if (node1.get(fieldName).isObject() && node2.get(fieldName).isObject()) {
                        List<String> lst = new ArrayList<String>();
                        node1.get(fieldName).fieldNames().forEachRemaining(innerFieldName -> {
                            if (node2.get(fieldName).has(innerFieldName)) {
                                JsonNode innerNode1 = node1.get(fieldName).get(innerFieldName);
                                JsonNode innerNode2 = node2.get(fieldName).get(innerFieldName);

                                if (innerNode1 != null && innerNode2 != null) {

                                    if (finaldifference(innerFieldName, innerNode1, innerNode2) != null && finaldifference(innerFieldName, innerNode1, innerNode2) != "") {
                                        lst.add(finaldifference(innerFieldName, innerNode1, innerNode2));
                                    }
                                }
                            }
                        });
                        if (!lst.isEmpty()) {
                            differingFields.put(fld, lst);
                        }
                    } else if (node1.get(fieldName).isArray() && node2.get(fieldName).isArray()) {
//                        System.out.println("[ECOM-WAR] AmendmentMailAjax fieldName==>" + fieldName + "fieldName==>" + fieldName);
                        List<String> lst = new ArrayList<String>();
                        HashMap<String, String> hm = getmandatoryValue();
                        List<JsonNode> elements1 = StreamSupport.stream(node1.get(fieldName).spliterator(), false)
                                .collect(Collectors.toList());

                        List<JsonNode> elements2 = StreamSupport.stream(node2.get(fieldName).spliterator(), false)
                                .collect(Collectors.toList());
                        String mandatoryfield = hm.get(fieldName);
                        System.out.println("[ECOM-WAR] AmendmentMailAjax fld=>" + fld + "mandatoryfield==>" + mandatoryfield);
// Check if mandatoryfield is not null before using it
                        if (mandatoryfield != null) {
                            elements1.forEach(element1 -> {
                                String name1 = element1.get(mandatoryfield).asText();
//                                System.out.println("[ECOM-WAR] AmendmentMailAjax name1==>" + name1);
                                Optional<JsonNode> matchingElement = elements2.stream()
                                        .filter(element2 -> name1.equals(element2.get(mandatoryfield).asText()))
                                        .findFirst();
//                                System.out.println("[ECOM-WAR] AmendmentMailAjax matchingElement==>" + matchingElement);
                                if (matchingElement.isPresent()) {
                                    JsonNode matchedElement = matchingElement.get();
//                                    System.out.println("[ECOM-WAR] AmendmentMailAjax differingFields====>" + matchedElement);
                                    differingFields.putAll(findDifferingFields(path + "~" + fieldName + "~" + name1, element1, matchedElement));
                                } else {
                                    try {
                                        List<String> stringList = new ArrayList<>();
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        JsonNode jsonElement2 = objectMapper.readTree(element1.toString());
                                        Iterator<String> fieldNames = jsonElement2.fieldNames();
                                        while (fieldNames.hasNext()) {
                                            String fieldNameee = fieldNames.next();
                                            String value = jsonElement2.get(fieldNameee).asText();
                                            stringList.add(fieldNameee + "~~~" + value);
                                        }
                                        differingFields.put(fieldName + "~" + name1 + "~" + " removed", stringList);
                                    } catch (JsonProcessingException ex) {
                                        Logger.getLogger(EcomAmendmentBookingBean.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            differingFields.put(path, lst);

                            elements2.forEach(element2 -> {
                                String name2 = element2.get(mandatoryfield).asText();

                                boolean foundMatch = elements1.stream()
                                        .anyMatch(element1 -> name2.equals(element1.get(mandatoryfield).asText()));

                                if (!foundMatch) {
                                    try {
                                        List<String> stringList = new ArrayList<>();
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        JsonNode jsonElement2 = objectMapper.readTree(element2.toString());
                                        Iterator<String> fieldNames = jsonElement2.fieldNames();
                                        while (fieldNames.hasNext()) {
                                            String fieldNameee = fieldNames.next();
                                            String value = jsonElement2.get(fieldNameee).asText();
                                            stringList.add(fieldNameee + "~~~" + value);
                                        }
                                        differingFields.put(fieldName + "~" + name2 + "~" + " Added", stringList);
//                                        System.out.println("[ECOM-WAR] AmendmentMailAjax differingFields===>" + differingFields);
                                    } catch (JsonProcessingException ex) {
                                        Logger.getLogger(EcomAmendmentBookingBean.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                        }

                    } else {
//                        System.out.println("[ECOM-WAR] AmendmentMailAjax ");
                        if (finaldifference(fieldName, node1.get(fieldName), node2.get(fieldName)) != null && finaldifference(fieldName, node1.get(fieldName), node2.get(fieldName)) != "") {
                            lststring.add(finaldifference(fieldName, node1.get(fieldName), node2.get(fieldName)));

                        }
                    }
                }

            });
            if (!lststring.isEmpty()) {
                differingFields.put(path + "~edited", lststring);
            }

        }

        return differingFields;
    }

    private static HashMap<String, String> getmandatoryValue() {
        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("customerDetails", "custTypeId");
        keyValueMap.put("cargoDetails", "containerNo");
        keyValueMap.put("scheduleDetails", "ebook_origin_country");
        return keyValueMap;
    }

    private static String finaldifference(String field, JsonNode node1, JsonNode node2) {
        String finalval = "";
        if (!node1.toString().equalsIgnoreCase(node2.toString())) {
            finalval = field + "~~~" + node1 + "~~~" + node2;
        }
        return finalval;
    }

    private String checkNull(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String checkNullbefourValue(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "None";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String checkNullafterValue(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "None";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String gethaulagetype(String type) {
        String rsl = "";
        switch (type) {
            case "L":
                rsl = "Live Load";
                break;
            case "D":
                rsl = "Drop Only";
                break;
            case "P":
                rsl = "Pickup only";
                break;
            case "B":
                rsl = "Pickup & Drop";
                break;
            case "":
                rsl = "None";
                break;
        }
        return rsl;
    }

    private String scottype(String type) {
        String rsl = "";
        switch (type) {
            case "Y":
                rsl = "YES";
                break;
            case "N":
                rsl = "NO";
                break;
            case "":
                rsl = "";
                break;
        }
        return rsl;
    }

    private boolean isJsonNodeEmpty(JsonNode node) {
        boolean isempty = true;
        if (node == null || node.size() == 0 || node.isNull()) {
            isempty = false;
        }
        return isempty;
    }

    private String agencyamendmentmailid(String agencymailid, String usermailid) {
        System.out.println("[ECOM-WAR] AmendmentMailAjax agencymailid=>" + agencymailid + "usermailid=>" + usermailid);
        String rsl = "";
        boolean ismatch = true;
        String agencyid = checkNull(agencymailid);

        if (!"".equalsIgnoreCase(agencyid)) {
            if (agencymailid.contains(usermailid)) {
                rsl = agencymailid;
                ismatch = false;
            }
            if (ismatch) {
                rsl = agencymailid + "," + usermailid;
            }
        } else if (!"".equalsIgnoreCase(checkNull(usermailid))) {
            rsl = usermailid;
        }
        System.out.println("[ECOM-WAR] AmendmentMailAjax rsl Mail Id==================>" + rsl);
        return rsl;
    }

    public static String generatecargoTable(String tier, String maximumcoverage, String pershipment) {
        StringBuilder html = new StringBuilder();
        // First Table
        html.append("<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:#fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:70%;border-collapse: collapse;\">");
        html.append("<tr ></tr>");
        html.append("<tr style=\"margin:0;padding:0;line-height: 15px;\">");
        html.append("<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%;\"><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Tier</span></td>");
        html.append("<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%;\"><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Maximum Coverage</span></td>");
        html.append("<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%;\"><span style=\"font-size:12px;color:#999;margin:0;padding:0\">Per Shipment (USD)</span></td>");
        html.append("</tr>");
        html.append("<tr style=\"margin:0;padding:0;line-height: 15px;\">");
        html.append("<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%;\"><span>" + tier + "</span></td>");
        html.append("<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%;\"><span>" + maximumcoverage + "</span></td>");
        html.append("<td style=\"font-size:12px;margin:0;padding:0px;vertical-align:middle;width:33%;\"><span>" + pershipment + "</span></td>");
        html.append("</tr>");
        html.append("</table>");

        return html.toString();
    }
}
