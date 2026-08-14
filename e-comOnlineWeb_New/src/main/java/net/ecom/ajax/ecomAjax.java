/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import net.ecom.common.GeneralConf;
import net.com.ecom.ws.vo.MovementDetailsList;
import javax.servlet.RequestDispatcher;
import net.ecom.vo.MailFormatVO;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.Cookie;
import net.ecom.AppTierCntrlBean.EcomAmendmentBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.AppTierCntrlBean.EcomVGMBeanRemote;
import net.ecom.captcha.VerifyRecaptcha;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.MaskPort;
import net.ecom.common.PasswordExpiredOTPMail;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.tracking.vo.TrackingActvityResult;
import net.ecom.tracking.vo.TrackingCargoActivity;
import net.ecom.tracking.vo.TrackingCargoActivityResult;
import net.ecom.tracking.vo.TrackingCargoResult;
import net.ecom.tracking.vo.TrackingMasterVo;
import net.ecom.v1.cntlbean.EcomV1SIBeanRemote;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.FieldConfigVO;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessages;
import sun.misc.BASE64Encoder;

public class ecomAjax extends HttpServlet {

    Connection reqCon;
    ServletConfig config;
    ServletContext context;
    PreparedStatement ps;
    PreparedStatement ps1;
    String inputdet = "";
    String currentdate = "";
    String initiatorID = "ECOM-CNT";

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote bookbean = EJBLookUp.getBookingBeanLookUp();
    EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();
    EcomAmendmentBookingBeanRemote amenObj = EJBLookUp.getAmendmentBeanLookUp();
    EcomShippingInstructionBeanRemote sibeanobj = EJBLookUp.getSIBeanRemoteLookUp();
    EcomV1SIBeanRemote v1SIObj = EJBLookUp.getV1SIBeanLookUp();
    EcomVGMBeanRemote vgmObj = EJBLookUp.getESLVGMBeanLookUp();

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

        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        String agencymail = (String) context.getAttribute("clientfrommail");
        String client_code = (String) context.getAttribute("client_code");
        List EBK_plgn = (List) context.getAttribute("EBK_PLUGIN_DETAILS");
        Map<String, String> fieldShipmentMap = (Map<String, String>) context.getAttribute("FIELDCONFIGSHIPMENTMAP");

        try {
            if (type.equalsIgnoreCase("sendmail")) {
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                String logintype = (String) session.getAttribute("SESSION_LOGIN_TYPE");
                if ("YES".equalsIgnoreCase(strIsViaLogin) && "U".equalsIgnoreCase(logintype)) {

                    PrintWriter out = response.getWriter();
                    net.ecom.ajax.Tracking track = new net.ecom.ajax.Tracking();
                    String text = checknull(request.getParameter("text"));
                    String id = checknull(request.getParameter("id"));
                    String responseText = track.sendMail(text, id, agencymail, client_code);
                    out.print(responseText);
                    response.setStatus(200, "Track Changes");
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } else if (type.equalsIgnoreCase("emptyPickUp")) {
                PrintWriter out = response.getWriter();
                String port = checknull(request.getParameter("port"));
                String pointtype = checknull(request.getParameter("pointtype"));
                String result = ecomCntrlObj.getEmptyPickPoint(port, pointtype);
                if (result != null) {
                    out.print(result);
                } else {
                    out.print("");
                }
            } else if (type.equalsIgnoreCase("podtransit")) {
                PrintWriter out = response.getWriter();
                String port = checknull(request.getParameter("port"));
                String result = bookbean.getPODIntransit(port);
                if (result != null) {
                    out.print(result);
                } else {
                    out.print("");
                }
            } else if (type.equalsIgnoreCase("contactusmail")) {
                PrintWriter out = response.getWriter();
                String text = checknull(request.getParameter("text"));
                String id = checknull(request.getParameter("id"));
                String responseText = sendContactMail(text, id, agencymail, client_code);
                out.print(responseText);
                response.setStatus(200, "Contactus Changes");
            } else if (type.equalsIgnoreCase("sendschedulemail")) {
                String schedulesessiontoken = (String) session.getAttribute("SessionScheduletoken");
                String schedulerequesttoken = checknull(request.getParameter("scheduletoken"));
                PrintWriter out = response.getWriter();
                Object attemptCountObj = session.getAttribute("ScheduleMailCount");
                int passattemptCount = (attemptCountObj != null) ? (int) attemptCountObj : 0;
                System.out.println("passattemptCount==> " + passattemptCount);
                session.setAttribute("ScheduleMailCount", passattemptCount + 1);
                String schedulecaptcha = Validation.checkNull((String) context.getAttribute(GeneralConf.ENABLE_MASS_SUBMISSION_CAPTCHA));
                boolean verify = true;
                if ("Y".equalsIgnoreCase(schedulecaptcha)) {
                    String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
                    System.out.println("gRecaptchaResponse==> " + gRecaptchaResponse);
                    if (passattemptCount >= 3) {
                        String secretKey = Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.CaptchaSecretKey));
                        verify = VerifyRecaptcha.verify(gRecaptchaResponse, secretKey);
                    }
                }
                if (verify) {

                    if (schedulerequesttoken.equalsIgnoreCase(schedulesessiontoken)) {
                        session.setAttribute("SessionScheduletoken", "");
                        String propertiesclientcode = (String) session.getAttribute("clientcode");
                        ArrayList schedulelist = (ArrayList) session.getAttribute("PortList");
                        String text = checknull(request.getParameter("text"));
                        String id = checknull(request.getParameter("id"));
                        int countList = schedulelist.size();
                        System.out.println("[ECOM-WAR] ecomAjax   countlist-- - - - " + countList);
                        String responseText = sendMail(text, id, agencymail, client_code, schedulelist, propertiesclientcode);
                        out.print(responseText);
                        response.setStatus(200, "schedule Changes");
                    } else {
                        out.print("Something went wrong please try again later.");
                        System.out.println("[ECOM-WAR] ecomAjax   Token Mismatch==> schedulesessiontoken=" + schedulesessiontoken + "&schedulerequesttoken=" + schedulerequesttoken);
                        response.setStatus(400, "Duplicate Request");
                    }
                } else {
                    out.print("INVALID");
                }

            } else if ("checkcontainer".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                String container = request.getParameter("container").trim();
                String eqptype = request.getParameter("eqptype");
                String nor = request.getParameter("nor");
                String oldtype = request.getParameter("oldtype");
                System.out.println("[ECOM-WAR] ecomAjax   >>eq[ptyepe in cntr check>>" + eqptype);
                String soc = request.getParameter("soc").trim();
                String eqp_sub_plgn = (EBK_plgn != null && EBK_plgn.contains("EES")) ? "Y" : "N";
                String result = ecomCntrlObj.checkContainerAndEqpType(container, eqptype, nor, oldtype, eqp_sub_plgn);
                System.out.println(container + soc + ":::::" + result);
                out.print(result);
                /**
                 * if (soc != null && "Y".equalsIgnoreCase(soc)) {
                 * out.print(true); } else if (result != null &&
                 * result.equalsIgnoreCase("Exists")) { out.print(true); } else
                 * { out.print(false); }*
                 */
            } else if ("checkvgmcontainer".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                String container = request.getParameter("container").trim();
                String eqptype = request.getParameter("eqptype");
                System.out.println("[ECOM-WAR] ecomAjax   check>>eq[ptyepe in cntr check>>" + eqptype);
                String result = ecomCntrlObj.checkContainerAndEqpType(container, eqptype, "", "", "N");
                System.out.println(container + ":::::" + result);
                if (result != null && result.length() > 0) {
                    result = result.split("~~")[0];
                }
                out.print(result);

            } else if ("checkblnofrombookno".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                String bookNo = request.getParameter("bookNo");
                String appVersion = request.getParameter("appversion");
                String result = null;
                if ("1".equalsIgnoreCase(appVersion)) {
                    result = v1SIObj.checkV1BLStatus(bookNo);
                } else {
                    result = sibeanobj.getBLNoFromBookNo(bookNo);
                }
                out.print(result);
            } else if ("gettareweight".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                double tarewt = 0;
                boolean checkTarewt = true;
                String container = request.getParameter("container").trim();
                String eqptype = request.getParameter("eqptype").trim();
                String nor = request.getParameter("nor").trim();
                String soc = request.getParameter("soc").trim();
                String result = sibeanobj.getTareWtForCntr(container, eqptype, nor, soc);
                if (result != null && result.length() > 0) {
                    tarewt = Double.parseDouble(result);
                    if (tarewt > 0) {
                        checkTarewt = true;
                    } else {
                        checkTarewt = false;
                    }
                } else {
                    checkTarewt = false;
                }
                if (!checkTarewt && (!"Y".equalsIgnoreCase(soc) || (container.contains(eqptype + "TNB") || (container.contains(eqptype + "TBN"))))) {
                    result = bookbean.getTareweight("", eqptype);
                }
                out.print(result);
                System.out.println("[ECOM-WAR] ecomAjax   gettareweight:::::" + result);

            } else if ("togettarewtforvgmwt".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                double tarewt = 0;
                boolean checkTarewt = true;
                String container = request.getParameter("container").trim();
                String eqptype = request.getParameter("eqptype").trim();
                String nor = request.getParameter("nor").trim();
                String soc = request.getParameter("soc").trim();
                String result = vgmObj.togetTareWtForCntr(container, eqptype, nor, soc);
//                 System.out.println("[ECOM-WAR] ecomAjax   get tarewt from vgm" + result + soc);
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("soc")) {
                    tarewt = Double.parseDouble(result);
                    if (tarewt > 0) {
                        checkTarewt = true;
                    } else {
                        checkTarewt = false;
                    }
                } else {
                    checkTarewt = false;
                }
                if (!checkTarewt && (!"Y".equalsIgnoreCase(soc) || (container.contains(eqptype + "TNB") || (container.contains(eqptype + "TBN")))) && !result.equalsIgnoreCase("soc")) {
                    result = bookbean.getTareweight("", eqptype);
                }
                out.print(result);
                System.out.println("[ECOM-WAR] ecomAjax   gettareweight VGM:::::" + result + ",soc:" + soc);

            } else if ("getdeclinedremarks".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                String ecombookno = request.getParameter("ecombookno").trim();
                String status = request.getParameter("status").trim();
                String result = bookbean.getBookingDeclinedRemarks(ecombookno, status);
                System.out.println("[ECOM-WAR] ecomAjax   getdeclinedremarks:::::" + result);
                out.print(result);
            } else if ("deletetempbooking".equalsIgnoreCase(type)) {
                response.setContentType("text/html;charset=UTF-8");
                String actiontype = (request.getParameter("actiontype") != null) ? request.getParameter("actiontype").trim() : "";
                if ("deletetempbooking".equalsIgnoreCase(actiontype)) {
                    ArrayList templateNoListObj = null;
                    ArrayList templateno = new ArrayList();
                    ArrayList backuptemplateno = new ArrayList();
                    ArrayList tempmangetemplate = new ArrayList();   // vasanth
                    String loginname = (String) session.getAttribute("SESSION_LOGINNAME");
                    String bookno = request.getParameter("bookno");
                    String result = bookbean.deleteTemplateBooking(bookno);
                    System.out.println("[ECOM-WAR] ecomAjax   Template delete::::" + result);
                    templateNoListObj = ecomCntrlObj.getTemplateNo(loginname, "T");
                    if (templateNoListObj != null && templateNoListObj.size() == 3) {
                        templateno = LabelBeanConversion.labelListConversion((List<DropDownVO>) templateNoListObj.get(0));
                        backuptemplateno = (ArrayList) templateNoListObj.get(1);
                        tempmangetemplate = (ArrayList) templateNoListObj.get(2);  // vasanth
                    }
                    session.setAttribute("SESSION_TEMPLATE_NO", templateNoListObj);
                    session.setAttribute("SESSION_TEMPLATE_NO", templateno);
                    session.setAttribute("SESSION_BACKUP_TEMPLATE_NO", backuptemplateno);
                    session.setAttribute("SESSION_MANAGE_TEMPLATE", tempmangetemplate); //vasanth
                }
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Booking/manageTemplate.jsp");
                rd.forward(request, response);
                return;
            } else if ("checkeqpsize".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                String container = request.getParameter("container").trim();
                String result = ecomCntrlObj.checkEQPSize(container);
                System.out.println(container + ":::::" + result);
                if (result != null && result.length() > 0) {
                    out.print(result);
                } else {
                    out.print("");
                }
            } else if (type.equalsIgnoreCase("templateCheck")) {
                PrintWriter out = response.getWriter();
                String templateName = request.getParameter("templateName");
                String loginname = (String) session.getAttribute("SESSION_LOGINNAME");
                try {
                    String result = bookbean.checkTemplateName(templateName, loginname);
                    out.print(result);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax getports method :");
                }
            } else if (type.equalsIgnoreCase("checksicreation")) {
                PrintWriter out = response.getWriter();
                String bookno = request.getParameter("lrpbookno");
                System.out.println("[ECOM-WAR] ecomAjax   lrpbookno==>" + request.getParameter("lrpbookno"));
                String result = "NO";
                String carrierCode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
                try {
                    if (!"GWF".equalsIgnoreCase(carrierCode)) {
                        if(bookno != null && checknull(bookno).length() > 0) {
                            result = amenObj.checkSICreation(bookno);
                        }
                    }
                    out.print(result);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax checksicreation method :");
                }
            } else if (type.equalsIgnoreCase("deletecutoffconfig")) {
                PrintWriter out = response.getWriter();
                String port = request.getParameter("port");
                String terminal = request.getParameter("terminal");
                String result = null;
                try {
                    int i = ecomCntrlObj.deleteCutoffConfig(port, terminal);
                    System.out.println("[ECOM-WAR] ecomAjax   >>deletecutof>>>" + i);
                    out.print(result);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax deletecutoffconfig method :");
                }
            } else if (type.equalsIgnoreCase("checkcutoffconfig")) {
                PrintWriter out = response.getWriter();
                String port = request.getParameter("port");
                String terminal = request.getParameter("terminal");
                String result = null;
                try {
                    result = ecomCntrlObj.checkCutOffDuplicate(port, terminal);
                    System.out.println("[ECOM-WAR] ecomAjax   >>checkcutoffconfig>>>" + result);
                    out.print(result);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax checkCutOffDuplicate method :");
                }
            } else if (type.equalsIgnoreCase("checkfieldconfig")) {
                PrintWriter out = response.getWriter();
                String result = null;
                String fieldcode = request.getParameter("fieldcode");
                String searchtype = request.getParameter("searchtype");
                String searchvalue = request.getParameter("searchvalue");
                String shipmenttyp = request.getParameter("shipmenttyp");
                String inputtype = request.getParameter("inputtype");
                String saveType = request.getParameter("saveType");
                String id = request.getParameter("id");
//                 System.out.println("[ECOM-WAR] ecomAjax   >>fieldcode>>" + fieldcode);
//                 System.out.println("[ECOM-WAR] ecomAjax   >>searchtype>>" + searchtype);
//                 System.out.println("[ECOM-WAR] ecomAjax   >>searchvalue>>" + searchvalue);
//                 System.out.println("[ECOM-WAR] ecomAjax   >>shipmenttyp>>" + shipmenttyp);
//                 System.out.println("[ECOM-WAR] ecomAjax   >>inputtype>>" + inputtype);
//                 System.out.println("[ECOM-WAR] ecomAjax   >>saveType>>" + saveType);
//                 System.out.println("[ECOM-WAR] ecomAjax   >>id>>" + id);
                System.out.println("[ECOM-WAR] ecomAjax   fieldcode: " + fieldcode + ", searchtype: " + searchtype + ", searchvalue: " + searchvalue + ", shipmenttyp: " + shipmenttyp + ", inputtype: " + inputtype + ", saveType: " + saveType + ", id: " + id);

                FieldConfigVO configVO = new FieldConfigVO();
                configVO.setFieldCode(fieldcode);
                configVO.setSearchType(searchtype);
                configVO.setSearchValue(searchvalue);
                configVO.setShipmentType(shipmenttyp);
                configVO.setInputType(inputtype);
                configVO.setSaveType(saveType);
                configVO.setId(id);
                result = ecomCntrlObj.fieldConfigDuplication(configVO);
                out.print(result);
            } else if (type.equalsIgnoreCase("deletefieldconfig")) {
                PrintWriter out = response.getWriter();
                String id = request.getParameter("id");
                ecomCntrlObj.deleteFieldConfg(id);
                out.print("");
            } else if (type.equalsIgnoreCase("fieldconfigshipmenttype")) {
                PrintWriter out = response.getWriter();
                String fieldcode = request.getParameter("fieldcode");
                out.print(fieldShipmentMap.get(fieldcode));
            } else if (type.equalsIgnoreCase("setfieldconfigvalidation")) {
                PrintWriter out = response.getWriter();
                String result = "No";
                String fieldcode = request.getParameter("fieldcode");
                String pol = request.getParameter("pol");
                String pod = request.getParameter("pod");
                String shipmentType = fieldShipmentMap.get(fieldcode);
                System.out.println("[ECOM-WAR] ecomAjax   >>shipmentType>>" + shipmentType);
                if ("E".equalsIgnoreCase(shipmentType)) {
                    String polCountry = Validation.checkNull(bookbean.getCountryCodeForPort(pol));
                    result = ecomCntrlObj.getFieldConfg(polCountry, pol, fieldcode, "E");
                } else if ("I".equalsIgnoreCase(shipmentType)) {
                    String podCountry = Validation.checkNull(bookbean.getCountryCodeForPort(pod));
                    result = ecomCntrlObj.getFieldConfg(podCountry, pod, fieldcode, "I");
                }
                out.print(result);
            } else if (type.equalsIgnoreCase("checktransitdatevalidation")) {
                PrintWriter out = response.getWriter();
                String date1 = request.getParameter("date1");
                String date2 = request.getParameter("date2");
                String result = null;
                try {
                    result = DateConversion_New.compareTwoDate(date1, date2);
                    out.print(result);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax compareTwoDate method :");
                }
            } else if (type.equalsIgnoreCase("checkhscodevalidation")) {
                System.out.println("[ECOM-WAR] ecomAjax   ");
                PrintWriter out = response.getWriter();
                String port = request.getParameter("port");
                String result = null;
                String hscodeconfig = "NO";
                try {
                    result = bookbean.getCountryCodeForPort(port);
//                     System.out.println("[ECOM-WAR] ecomAjax   >>port>>" + port);
//                     System.out.println("[ECOM-WAR] ecomAjax   >>result>>" + result);
                    result = Validation.checkNull(result).trim().toUpperCase();
                    Map<String, String> hsCodeCountryMap = (Map<String, String>) context.getAttribute(GeneralConf.hscodecountry);
//                     System.out.println("[ECOM-WAR] ecomAjax   >>>hsCodeCountryMap>>>" + hsCodeCountryMap);
                    System.out.println("[ECOM-WAR] ecomAjax   port: " + port + ", result: " + result + ", hsCodeCountryMap: " + hsCodeCountryMap);

                    if (hsCodeCountryMap != null) {
                        result = hsCodeCountryMap.get(result);
                    } else {
                        result = null;
                    }
                    if (result != null && result.length() > 0) {
                        result = "yes";
                        hscodeconfig = "YES";
                    } else {
                        result = "no";
                    }
                    session.setAttribute("HSCODECONFIG", hscodeconfig);
                    out.print(result);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax checkhscodevalidation method :");
                }
            } else if (type.equalsIgnoreCase("checknatcodevalidation")) {
//                 System.out.println("[ECOM-WAR] ecomAjax   ");
                PrintWriter out = response.getWriter();
                String port = request.getParameter("port");
                String result = null;
                try {
                    result = bookbean.getCountryCodeForPort(port);
//                     System.out.println("[ECOM-WAR] ecomAjax   >>port>>" + port);
//                     System.out.println("[ECOM-WAR] ecomAjax   >>result>>" + result);
                    result = Validation.checkNull(result).trim().toUpperCase();
                    Map<String, String> natCodeCountryMap = (Map<String, String>) context.getAttribute(GeneralConf.natcodecountry);
//                     System.out.println("[ECOM-WAR] ecomAjax   >>>natCodeCountryMap>>>" + natCodeCountryMap);
                    System.out.println("[ECOM-WAR] ecomAjax   port: " + port + ", result: " + result + ", natCodeCountryMap: " + natCodeCountryMap);

                    if (natCodeCountryMap != null) {
                        result = natCodeCountryMap.get(result);
                    } else {
                        result = null;
                    }
                    if (result != null && result.length() > 0) {
                        result = "yes";
                    } else {
                        result = "no";
                    }
                    out.print(result);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax checknatcodevalidation method :");
                }
            } else if ("containerHistoy".equalsIgnoreCase(type)) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                String strContainer = request.getParameter("containerNo");
                String strBookNo = request.getParameter("bookNo");
                String porturl = null;
                Client client = Client.create();
                porturl = (String) context.getAttribute(GeneralConf.WSURL) + "/bytrackingMovement?strContainerNos=" + strContainer + "&strBooknumbers=" + strBookNo;
                WebResource webResource = client.resource(porturl);
                String result = webResource.get(String.class);
                MovementDetailsList obj = new MovementDetailsList();
                JAXBContext jc = JAXBContext.newInstance(obj.getClass());
                Unmarshaller marshaller = jc.createUnmarshaller();
                obj = (MovementDetailsList) marshaller.unmarshal(new StringReader(result));
                session.setAttribute("ContMovemenet", obj);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Tracking/TrackingMovement.jsp");
                rd.forward(request, response);
                return;
            } else if ("containermovement".equalsIgnoreCase(type)) {   //new Web service
                response.setContentType("text/html;charset=UTF-8");
                String strContainer = request.getParameter("containerNo");
                String strBookNo = request.getParameter("bookNo");
                String userfuritcust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
                String movementcodestr = null;
                TrackingActvityResult resultvo = null;
                List<TrackingActvityResult> lstresult = new ArrayList<TrackingActvityResult>();
                String name = (String) context.getAttribute(GeneralConf.TRACKINGUSERNAME);
                String password = (String) context.getAttribute(GeneralConf.TRACKINGPASSWORD);
                String TrackingRestriction = (String) context.getAttribute(GeneralConf.TRACKING_RESTRICTION);
                String authString = name + ":" + password;
                String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
                System.out.println("[ECOM-WAR] ecomAjax   Base64 encoded auth string: " + authStringEnc);
                Client restClient = Client.create();
                String url = (String) context.getAttribute(GeneralConf.TRACKINGSEARCHURL) + "?key=" + strBookNo + "&strcontainer=" + strContainer + "&furitcust=" + userfuritcust + "&trackingrestriction=" + TrackingRestriction;
                System.out.println("[ECOM-WAR] ecomAjax   ======>url" + url + ",authStringEnc:" + authStringEnc);
//        String url1 = (String) context.getAttribute(GeneralConf.TRACKINGMOVEMENTURL) + "?bookNo=" + strBookNo;
                WebResource webResource1 = restClient.resource(url);
                ClientResponse resp1 = webResource1.accept("application/json").header("Authorization", "Basic " + authStringEnc).get(ClientResponse.class);
                if (resp1.getStatus() == 200) {
                    String output = resp1.getEntity(String.class);
                    Gson gson = new Gson();
                    Type types = new TypeToken<TrackingCargoResult>() {
                    }.getType();
                    TrackingCargoResult lstresults = gson.fromJson(output, types);
                    //
                    List<TrackingMasterVo> result = lstresults.getResult();
                    if (result != null && result.size() > 0 && (("200").equalsIgnoreCase(lstresults.getCode()))) {
                        int resultsize = result.size();
                        TrackingMasterVo res = null;
                        if (resultsize > 0) {
                            res = result.get(0);
                            TrackingCargoActivity trackcarVO = null;
                            String contnum = null;
                            List<TrackingCargoActivityResult> movementList = res.getMovementList();
                            if (movementList != null) {
                                for (int k = 0; k < movementList.size(); k++) {
                                    TrackingCargoActivityResult trackmstvo = (TrackingCargoActivityResult) movementList.get(k);
                                    contnum = trackmstvo.getContainerNo();
                                    List<TrackingCargoActivity> tracact = (List<TrackingCargoActivity>) trackmstvo.getContainerActivity();
                                    for (int j = 0; j < tracact.size(); j++) {
                                        if (strContainer != null && contnum != null && strContainer.equalsIgnoreCase(contnum)) {
                                            trackcarVO = (TrackingCargoActivity) tracact.get(j);
                                            resultvo = new TrackingActvityResult();
                                            resultvo.setContainerno(trackcarVO.getContainerNo());
                                            resultvo.setContainertype(trackcarVO.getEqpTypeId());
                                            resultvo.setMoverment(trackcarVO.getMovementDesc());
                                            resultvo.setActivitydate(trackcarVO.getActivityDate());
                                            resultvo.setActivityloccode(trackcarVO.getActivityloccode());
                                            resultvo.setActivitylocation(trackcarVO.getPortName());
                                            resultvo.setService(trackcarVO.getServiceCode());
                                            resultvo.setVessel(trackcarVO.getVessel());
                                            resultvo.setVoyage(trackcarVO.getVoyage());
                                            resultvo.setBound(trackcarVO.getBound());

                                            lstresult.add(resultvo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                session.setAttribute("ContMovemenet", lstresult);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Tracking/TrackingMovement.jsp");
                rd.forward(request, response);
                return;
            } else if ("vesseldetails".equalsIgnoreCase(type)) {
                response.setContentType("text/html;charset=UTF-8");
                String vesselcode = request.getParameter("vesselcode");
                ArrayList VesselDetails = ecomCntrlObj.getVesselDetails(vesselcode);
                session.setAttribute("Vesseldetails", VesselDetails);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Schedules/VesselDetails.jsp");
                rd.forward(request, response);
                return;
            } else if (type.equalsIgnoreCase("checkcompanydetails")) {
                PrintWriter out = response.getWriter();
                String custCode = request.getParameter("custcode");
                String country = request.getParameter("country");
                String result = ecomreg.checkCompanyDetails(custCode, country);
                out.print(result);
            } else if (type.equalsIgnoreCase("checkcompanynameandcity")) {
                String custcompanyname = "", custcityname = "", details = "", status = "";
                boolean custnameval = true, citynameval = true;
                JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();
                double similarityThreshold = ((Double) context.getAttribute(GeneralConf.COMPANY_NAME_SIMILARITY_THRESHOLD)).doubleValue();
                double citysimilarityThreshold = ((Double) context.getAttribute(GeneralConf.CITY_NAME_SIMILARITY_THRESHOLD)).doubleValue();
                PrintWriter out = response.getWriter();
                String custCode = request.getParameter("custcode");
                String companyName = checkdetailName(request.getParameter("companyname").toUpperCase());
                String city = checkdetailName(request.getParameter("cityname").toUpperCase());
//                 System.out.println("[ECOM-WAR] ecomAjax   custCode==>" + custCode + "=>" + companyName + "=>" + city);
                String result = ecomreg.getCustCompanyName(custCode);
                String[] values = result.split("~~");
                status = checkdetailName(values[1]);
                System.out.println("[ECOM-WAR] ecomAjax   status==> " + status + "custCode==>" + custCode + "=>" + companyName + "=>" + city);
                if (!"INVALID".equalsIgnoreCase(status)) {
                    custcompanyname = checkdetailName(values[2]);
                    custcityname = checkdetailName(values[3]);

//                     System.out.println("[ECOM-WAR] ecomAjax   companyname=>" + custcompanyname + "=" + custcityname);
//                     System.out.println("[ECOM-WAR] ecomAjax   custcompanyname==>" + custcompanyname + "companyName==>" + companyName + "status=>" + status);
                    System.out.println("[ECOM-WAR] ecomAjax   companyname => " + custcompanyname + " = " + custcityname + ", custcompanyname => " + custcompanyname + ", companyName => " + companyName + ", status => " + status);

                    if (!"".equalsIgnoreCase(custcompanyname) && !"".equalsIgnoreCase(companyName)) {
                        double similarityScore = jaroWinklerSimilarity.apply(custcompanyname, companyName);
                        System.out.println("[ECOM-WAR] ecomAjax   similarityScore==>" + similarityScore);
                        if (similarityScore >= similarityThreshold) {
                            custnameval = false;
                        }
                    }
                    System.out.println("[ECOM-WAR] ecomAjax   custcityname==>" + custcityname + "city==>" + city);
                    if (!"".equalsIgnoreCase(custcityname) && !"".equalsIgnoreCase(city)) {
                        double similarityScore = jaroWinklerSimilarity.apply(custcityname, city);
                        System.out.println("[ECOM-WAR] ecomAjax   similarityScore==>" + similarityScore);
                        if (similarityScore >= citysimilarityThreshold) {
                            citynameval = false;
                        }
                    }
                }
                details = custCode + "~~" + custnameval + "~~" + citynameval + "~~" + status;
                out.print(details);
            } else if (type.equalsIgnoreCase("checkAccess")) {
                PrintWriter out = response.getWriter();
                String strUsercode = checknullreq((String) session.getAttribute("SESSION_USERCODE"));
                String accessallowed = ecomreg.getalloweduser(strUsercode);
                try {
                    int accesscount = 0;
                    if (accessallowed != null && accessallowed.length() > 0) {
                        accesscount = Integer.parseInt(accessallowed);
                    }
                    out.print(accesscount);
                } catch (Exception e) {
                    System.out.println("[ECOM-WAR] ecomAjax   Exception from ajax getports method :");
                }
            } else if ("checkViaLogin".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                String userCode = (String) session.getAttribute("SESSION_USERNAME");
                String IS_VIA_LOGIN = (String) session.getAttribute("IS_VIA_LOGIN");
                String result = "NO";
                try {   
                    if (userCode != null && userCode.length() > 0 && ("YES".equalsIgnoreCase(IS_VIA_LOGIN))) {
                        result = "YES";
                    }
                    out.print(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("getStatsReport".equalsIgnoreCase(type)) {
                try {
                    String userCode = (String) session.getAttribute("SESSION_USERCODE");
                    String fromdate = request.getParameter("fromdate");
                    String todate = request.getParameter("todate");
                    String options = request.getParameter("options");
                    String service = request.getParameter("service");
                    String vessel = request.getParameter("vessel");
                    String voyage = request.getParameter("voyage");
                    String pla = request.getParameter("pla");
                    String pol = request.getParameter("pol");
                    String pod = request.getParameter("pod");
                    String pld = request.getParameter("pod");
                    String commodity = request.getParameter("commodity");
                    String equipmenttype = request.getParameter("equipmenttype");
                    String contractno = request.getParameter("contractno");
                    String bound = request.getParameter("bound");
                    String northbound = request.getParameter("northbound");
                    String southbound = request.getParameter("southbound");
                    String intertropics = request.getParameter("intertropics");
                    String europe = request.getParameter("europe");
                    String amendmentno = request.getParameter("amendmentno");
                    fromdate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromdate);
                    todate = DateConversion_New.convertDefaultDBFormatwithoutTime(todate);
                    List statsreportlis = ecomCntrlObj.getStatsReportDetails(userCode, fromdate, todate, options, service, vessel, voyage, pla, pol, pod, pld, commodity, equipmenttype, contractno, northbound, southbound, intertropics, europe, bound, amendmentno);
                    request.setAttribute("statsreportdetails", statsreportlis);
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/StatsReport/StatsReportView.jsp");
                    rd.forward(request, response);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("getvesseldetails".equalsIgnoreCase(type)) {
                try {
                    PrintWriter out = response.getWriter();
                    String service = request.getParameter("service");
                    String vesseljson = ecomCntrlObj.getVesselDetailsFromService(service);
                    out.print(vesseljson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("getvoyagedetails".equalsIgnoreCase(type)) {
                try {
                    String service = request.getParameter("service");
                    String vessel = request.getParameter("vessel");
                    List voyagelis = ecomCntrlObj.getVoyageDetails(service, vessel);
                    request.setAttribute("VoyageBoundDetails", voyagelis);
                    request.setAttribute("VoyageBoundContractType", "VoyageBound");
                    RequestDispatcher rd = request.getRequestDispatcher("VoyageBoundContractDetails.jsp");
                    rd.forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("getcontractdetails".equalsIgnoreCase(type)) {
                try {
                    String userCode = (String) session.getAttribute("SESSION_USERCODE");
                    List contlis = ecomCntrlObj.getContractDetails(userCode);
                    request.setAttribute("ContractDetails", contlis);
                    request.setAttribute("VoyageBoundContractType", "Contract");
                    RequestDispatcher rd = request.getRequestDispatcher("VoyageBoundContractDetails.jsp");
                    rd.forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("gettoken".equalsIgnoreCase(type)) {
                try {
                    PrintWriter out = response.getWriter();
                    long jsdate = new java.util.Date().getTime();
                    System.out.println("[ECOM-WAR] ecomAjax   gettoken==> " + jsdate);
                    session.setAttribute("SessionScheduletoken", jsdate + "");
                    out.print(jsdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("setcookieacceptance".equalsIgnoreCase(type)) {
                try {
                    Cookie cookie = new Cookie("ecom_cookie_acceptance", "accepted");
                    // Set attributes
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true); // Must be over HTTPS
                    cookie.setPath("/ecomonline");
                    cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                    // Add the cookie to the response
                    response.addCookie(cookie);
                    // Optionally add SameSite if needed manually
                    // Most servlet containers do not support SameSite directly via Cookie API
                    // Add it as a header instead:
                    response.setHeader("Set-Cookie", String.format(
                            "ecom_cookie_acceptance=accepted; Max-Age=%d; Path=/ecomonline; Secure; HttpOnly; SameSite=Strict",
                            30 * 24 * 60 * 60
                    ));

                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("ValidateOTP".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                boolean isValid = true;
                try {
                    if ("Y".equalsIgnoreCase(Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.Enable_OTP_Password_Expired)))) {
                        String otp = request.getParameter("otp");
                        isValid = PasswordExpiredOTPMail.validateOTP(request, otp);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                out.print(isValid);
            } else if ("ResendOTP".equalsIgnoreCase(type)) {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                try {
                    if ("Y".equalsIgnoreCase(Validation.checkNull((String) request.getServletContext().getAttribute(GeneralConf.Enable_OTP_Password_Expired)))) {
                        // Check if user is in password expired state
                        String isPasswordExpired = (String) session.getAttribute("isPasswordExpired");
                        if ("Y".equalsIgnoreCase(isPasswordExpired)) {
                            // Send new OTP
                            PasswordExpiredOTPMail.sendOTP(request);
                            out.print("{\"success\": true, \"message\": \"OTP sent successfully\"}");
                        } else {
                            out.print("{\"success\": false, \"message\": \"Password not expired\"}");
                        }
                    } else {
                        out.print("{\"success\": false, \"message\": \"OTP feature not enabled\"}");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.print("{\"success\": false, \"message\": \"Error sending OTP\"}");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String checknull(String value) {
        if (value != null) {
            value = value.trim();
        } else {
            value = "-";
        }
        return value;
    }

    private String checknullreq(String value) {
        if (value != null) {
            value = value.trim();
        } else {
            value = "";
        }
        return value;
    }

    public String sendMail(String Message, String id, String agencymail, String client_code, List arr, String propertiesclientcode) {
        String validationmessage = "";
        try {

            net.ecom.common.Mailer mail = new net.ecom.common.Mailer();
            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            MailFormatVO mailVo = ecomCntrlObj.getMailContent("schedule", "view", "C");
            //  mail.setFrom(mailVo.getStrFromAddr());
            mail.setTo(id);
            mail.setSubject(mailVo.getStrSubject());
            if (Message != null) {
//                 System.out.println("[ECOM-WAR] ecomAjax   message >>>>>>>>>>");
                String result = getScheduleSearch(Message, arr, propertiesclientcode);
                System.out.println("[ECOM-WAR] ecomAjax   result---" + result);
                mail.setMessage(result);
            }
            //1 mail.sendMail(authenticatedObj, agencymail);
            ecomCntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
            validationmessage = "sent";
        } catch (Exception e) {
            e.printStackTrace();
            validationmessage = "invalid";
        }
        return validationmessage;
    }

    private String checkdetailName(String str) {
        try {

            str = (str != null && (!"".equalsIgnoreCase(str)) && !"-".equalsIgnoreCase(str)) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getScheduleSearch(String Message, List arr, String propertiesclientcode) {
//         System.out.println("[ECOM-WAR] ecomAjax   message***" + Message);
        System.out.println("[ECOM-WAR] ecomAjax   arr" + arr + ",Message:" + Message);
        String Display_LCT_CUTOFF = (String) context.getAttribute(GeneralConf.Display_LCT_CUTOFF);
        String clientcode = propertiesclientcode;
        String mailmessage = "";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("com.myapp.struts.ApplicationResource_" + propertiesclientcode, Locale.getDefault());

            String service = bundle.getString("Schedule.View.Service");
            String vesselName = bundle.getString("Schedule.View.VesselName");
            String voyage = bundle.getString("Schedule.View.Voyage");
            String bound = bundle.getString("Schedule.View.Bound");
            String portName = bundle.getString("Schedule.View.PortName");
            String terminalName = bundle.getString("Schedule.View.TerminalName");
            String ETA = bundle.getString("Schedule.View.ETA");
            String ETD = bundle.getString("Schedule.View.ETD");
            String LCT = bundle.getString("Schedule.View.LCT");
            String POLName = bundle.getString("Schedule.View.POLName");
            String deptDate = bundle.getString("Schedule.View.DeptDate");
            String PODName = bundle.getString("Schedule.View.PODName");
            String arrivalDate = bundle.getString("Schedule.View.ArrivalDate");
            String legType = bundle.getString("Schedule.View.Legtype");

            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<HTML><HEAD>");
            htmlTable.append("<style type='text/css'>");
            htmlTable.append("th{background-color: #1FBBA6;!important;color:#fffff;font-size:14px !important;}");
            htmlTable.append("td{font-size:14px !important;}");
            htmlTable.append("h2 { color: #0070af !important;font-size: 14px !important;font-variant: normal;}");
            htmlTable.append("</style><title>Schedule Mail</title></HEAD>");
            htmlTable.append("<body style='width:1024px' MS_POSITIONING='GridLayout' bottomMargin='0' leftMargin='0' topMargin='0' rightMargin='0'>");
            htmlTable.append("<form  method='post'>");
            htmlTable.append("<div class='panel panel-primary'><div class='panel-heading'>");
            htmlTable.append("<span class='glyphicon glyphicon-arrow-right'></span>");
            htmlTable.append("<label style='font-size: 14px;color:#0033CC'>" + clientcode + "-Schedule Result</label></div></div>");
            htmlTable.append("<br><TABLE width='100%' border='1px' align='left' style='border:1px solid #CCC;'>");
            htmlTable.append("<thead>");
            htmlTable.append("<tr>");
            if ("datatablebylocation1".equalsIgnoreCase(Message)) {
                htmlTable.append("<th>").append(POLName).append("</th>");
                htmlTable.append("<th>").append(deptDate).append("</th>");
                htmlTable.append("<th>").append(service).append("</th>");
                htmlTable.append("<th>").append(vesselName).append("</th>");
                htmlTable.append("<th>").append(voyage).append("</th>");
                htmlTable.append("<th>").append(bound).append("</th>");
                htmlTable.append("<th>").append(PODName).append("</th>");
                htmlTable.append("<th>").append(arrivalDate).append("</th>");
                htmlTable.append("<th>").append(legType).append("</th>");
                if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                    htmlTable.append("<th>").append(LCT).append("</th>");
                }
            } else {
                htmlTable.append("<th>").append(service).append("</th>");
                htmlTable.append("<th>").append(vesselName).append("</th>");
                htmlTable.append("<th>").append(voyage).append("</th>");
                htmlTable.append("<th>").append(bound).append("</th>");
                htmlTable.append("<th>").append(portName).append("</th>");
                htmlTable.append("<th>").append(terminalName).append("</th>");
                htmlTable.append("<th>").append(ETA).append("</th>");
                htmlTable.append("<th>").append(ETD).append("</th>");
                if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                    htmlTable.append("<th>").append(LCT).append("</th>");
                }
            }
            htmlTable.append("</tr>");
            htmlTable.append("</thead>");
            htmlTable.append("<tbody>");

            int countList = arr.size();
            for (int n = 0; n < countList; n++) {
                Object element = arr.get(n);
                htmlTable.append("<tr>");
                if (element instanceof ArrayList) {
                    ArrayList portlist = (ArrayList) element;

                    for (int vv = 0; vv < portlist.size(); vv++) {

                        htmlTable.append("<td>").append((String) portlist.get(vv)).append("</td>");
                    }
                }
                htmlTable.append("</tr>");
            }

            htmlTable.append("</tbody>");
            htmlTable.append("</table>");
            htmlTable.append("<div style='font-size:12px !important;color:#C9780A !important;'>");
            htmlTable.append("<div class='col-sm-offset-0 col-sm-10' style='padding-left:0px'>");
            htmlTable.append("<p tyle='text-align:left !important;color:#C9780A !important;'>&nbsp;&nbsp;</p>");
            htmlTable.append("<p style='text-align:left !important;color:#C9780A !important;'>\"Schedule is subject to change and please contact our local offices for the details.\"</p>");
            htmlTable.append("</div></div>");
            htmlTable.append("<link href='newdesign/assets/css/bootstrap.min.css' rel='stylesheet'>");
            htmlTable.append("<link href='newdesign/assets/css/style.css' rel='stylesheet'>");
            htmlTable.append("</form></body></HTML>");

            mailmessage = htmlTable.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mailmessage;
    }

    public String sendContactMail(String Message, String id, String agencymail, String client_code) {
        try {

            net.ecom.common.Mailer mail = new net.ecom.common.Mailer();
            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            MailFormatVO mailVo = ecomCntrlObj.getMailContent("contactus", "view", "C");
            mail.setTo(id);
            mail.setSubject(mailVo.getStrSubject());
            mail.setMessage(Message);
            //1 mail.sendMail(authenticatedObj, agencymail);
            ecomCntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sent";
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
