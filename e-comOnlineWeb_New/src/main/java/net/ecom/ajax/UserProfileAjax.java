package net.ecom.ajax;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.AjaxSplitter;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.NotificationVo;
import net.ecom.vo.ReturnVO;

public class UserProfileAjax extends HttpServlet {

    ServletContext contexts;
    AjaxSplitter ajaxObj = new AjaxSplitter();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String requrl = request.getRequestURL().toString();
        String requri = request.getRequestURI();
//         System.out.println("[ECOM-WAR] UserProfileAjax >>requrl>>" + requrl);
        System.out.println("[ECOM-WAR] UserProfileAjax >>requri>>" + requri + ",requrl:" + requrl);
        try {
            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            EcomRegistrationBeanRemote ecomobj = EJBLookUp.getRegistrationBeanLookUp();
            EcomBLBeanRemote ecomblobj = EJBLookUp.getBLBeanLookUp();
            EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
            String lportCode = "", lagencyCode = "";
            StringBuffer ports = null;
            Validation validate = new Validation();
            String type = request.getParameter("type");
            System.out.println("[ECOM-WAR] UserProfileAjax User Profile Type  " + type);
            if (type.equalsIgnoreCase("oblsigstauts")) {
                String bl_no = request.getParameter("blno");
                String sign = request.getParameter("sign");
                String Shipmentstatus = request.getParameter("Shipmentstatus");
                String result = ecomblobj.getBlSignatureStatus(bl_no, sign, Shipmentstatus);
                out.print(result);
            } else if (type.equalsIgnoreCase("getcity")) {
                String city = ecomCntrlObj.getCityList(request.getParameter("countrycode").trim());
                out.write("valid~~" + city);
            } else if ("emailupdateformate".equalsIgnoreCase(type)) {
                String strmailfmt = request.getParameter("strupdate");
                String strmodules = request.getParameter("strmodules");
                String stractivity = request.getParameter("stractivity");
                String strsubject = request.getParameter("strsubject");
                System.out.println("[ECOM-WAR] UserProfileAjax strsubject===>" + strsubject);
                String stremailformate = "";
                stremailformate = ecomCntrlObj.getemailupdateformate(strmailfmt, strmodules, stractivity, strsubject);
                out.println(stremailformate);
            } else if (type.equalsIgnoreCase("geCarriageTerm")) {
                String carriageTerm = request.getParameter("carriageCode");
                carriageTerm = (carriageTerm != null && (!"".equalsIgnoreCase(carriageTerm))) ? carriageTerm.trim() : "";
                String result = ecomCntrlObj.getCarriageTermName(carriageTerm);
                out.write(result);
            } else if ("location".equalsIgnoreCase(type)) {
                ports = new StringBuffer();
                String loc2 = null;
                String countrycode = request.getParameter("countrycode").trim();
                List ECC_plgn = (List) contexts.getAttribute("ECC_PLUGIN_DETAILS");
                String ecc_plgn = (ECC_plgn != null && ECC_plgn.contains("ECA")) ? "Y" : "N";
                System.out.println("[ECOM-WAR] UserProfileAjax ecc_plgn===>" + ecc_plgn);
                HashMap<String, String> locMap = null;
                if ("Y".equalsIgnoreCase(ecc_plgn)) {
                    locMap = ecomCntrlObj.getagencyloc();
                } else {
                    locMap = ecomCntrlObj.getlocnew(countrycode);
                }
                System.out.println("[ECOM-WAR] UserProfileAjax locMap---->" + locMap);
                if (!locMap.isEmpty()) {
                    for (HashMap.Entry<String, String> set : locMap.entrySet()) {

                        lportCode = set.getKey();

                        //  System.out.println("[ECOM-WAR] UserProfileAjax lportcodewar==========> " +  lportCode);
                        lagencyCode = set.getValue();

                        //  System.out.println("[ECOM-WAR] UserProfileAjax lagencycodeWAR==========> " +  lagencyCode);
                        String pol1 = String.valueOf(portlist.get(lportCode));
                        System.out.println("[ECOM-WAR] UserProfileAjax portnameeee" + pol1);
                        ports.append(pol1).append(" ~~ ");
                        ports.append(lportCode).append("##");
                        ports.append(lagencyCode).append("~#~");

                        loc2 = ports.toString();
                        System.out.println("[ECOM-WAR] UserProfileAjax loc2" + loc2);
                        String[] split = loc2.split("~#~");
                        Arrays.sort(split);
                        StringBuffer str = new StringBuffer();
                        for (int i = 0; i < split.length; i++) {
                            str.append(split[i]).append("~#~");
                        }
                        loc2 = str.toString();
                        System.out.println("[ECOM-WAR] UserProfileAjax loc2222" + loc2);

                    }
                } else {
                    ports.append("noports##noports");
                    loc2 = ports.toString();
                    System.out.println("[ECOM-WAR] UserProfileAjax loc2------" + loc2);
                }

                if (loc2 != null) {
                    out.println("valid~~~" + loc2);
                }

            } else if ("porttype".equalsIgnoreCase(type)) {
                String portcode = request.getParameter("portcode").trim();
                String porttype = bookingObj.getPortType(portcode);
                out.println(porttype);
            } else if ("portcountry".equalsIgnoreCase(type)) {
                String portcode = request.getParameter("portcode").trim();
                String countrycode = bookingObj.getCountryCodeForPort(portcode);
                System.out.println("[ECOM-WAR] UserProfileAjax portcountry - - - " + countrycode);
                out.println(countrycode);
            } else if ("adminPwdCheck".equalsIgnoreCase(type)) {
                String loginName = null;
                boolean resultval = false;
                try {
                    loginName = Validation.checkNull((String) session.getAttribute("SESSION_USERNAME"));
                    String pwd = Validation.checkNull(request.getParameter("q"));
                    String result = ecomobj.checkAdminPassword(loginName, pwd);
                    result = Validation.checkNull(result);
                    if (result.equalsIgnoreCase("valid")) {
                        resultval = true;
                    }
                    out.print(resultval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("city".equalsIgnoreCase(type)) {
                String countrycode = request.getParameter("countrycode").trim();
                String city = ecomCntrlObj.getCity(countrycode);
                if (city != null) {
                    out.println("valid~~" + city);
                }
            } else if ("state".equalsIgnoreCase(type)) {
                String countrycode = request.getParameter("countrycode").trim();
                String state = ecomCntrlObj.getState(countrycode);
                if (state != null) {
                    out.println("valid~~" + state);
                }
            } else if ("userid".equalsIgnoreCase(type)) {
                String userid = request.getParameter("userid").trim();
                String result = ecomobj.CheckUserId(userid);
                if (result != null && result.equalsIgnoreCase("Exists")) {
                    out.print(false);
                } else {
                    out.print(true);
                }
            } else if ("changepass".equalsIgnoreCase(type)) {
                String password = request.getParameter("userpassword").trim();
                String login = request.getParameter("login").trim();
                String result = ecomobj.CheckPassword(login, password);
                if (result != null && result.equalsIgnoreCase("Exists")) {
                    out.print(true);
                } else {
                    out.print(false);
                }
            } else if (type.equalsIgnoreCase("deleteuserdetails")) {
                ReturnVO retVo = ecomobj.deleteuserdetails(request.getParameter("loginname").trim(), request.getParameter("contactname").trim());
                //
                String initiatorID = "ECOM-REG";
                String client_code = (String) contexts.getAttribute("client_code");
                String agencymail = (String) contexts.getAttribute("clientfrommail");
                Mailer mail = new Mailer();
                mail.setTo(retVo.getStrToMailId());
                mail.setSubject(retVo.getStrSubject());
                String msg = retVo.getStrMailMsg();
                mail.setMessage(msg);
                try {
                    ecomCntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                //
                out.write(retVo.getStrName());
            } else if ("checkARNFile".equalsIgnoreCase(type)) {
                String result = "fail";
                try {
                    String arnNo = Validation.checkNull(request.getParameter("arnNo"));
                    System.out.println("[ECOM-WAR] UserProfileAjax .................arn no............" + arnNo);
                    // System.out.println("[ECOM-WAR] UserProfileAjax arrival notice........" + (String) contexts.getAttribute(GeneralConf.arnpdfpath));
                    //String oblpath = (String) contexts.getAttribute(GeneralConf.commonpath) + GeneralConf.signaturepath + "/LN99.PNG";
                    String path = (String) contexts.getAttribute(GeneralConf.ANPDFPATH) + arnNo + ".pdf";
                    //oblpath = "E:/pil/signatue/LN99.PNG";
                    //path = "E:/pil/arn/LANDXB0000000020.pdf";
                    System.out.println("[ECOM-WAR] UserProfileAjax >>path>>" + path);
                    File pdfFile = new File(path);
                    //File imgFile = new File(oblpath);
                    if (pdfFile.exists()) {
                        // System.out.println("[ECOM-WAR] UserProfileAjax    ........exist..........");
                        result = "success";
                    }
                    out.print(result);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    out.print(result);
                }
            } else if (type.equalsIgnoreCase("activeUserDetails")) {
                ReturnVO retVo = ecomobj.activeUserDetails(request.getParameter("loginname").trim(), request.getParameter("contactname").trim());
                //
                String initiatorID = "ECOM-REG";
                String client_code = (String) contexts.getAttribute("client_code");
                String agencymail = (String) contexts.getAttribute("clientfrommail");
                Mailer mail = new Mailer();
                mail.setTo(retVo.getStrToMailId());
                mail.setSubject(retVo.getStrSubject());
                String msg = retVo.getStrMailMsg();
                mail.setMessage(msg);
                try {
                    ecomCntrlObj.sendMailFromEjb(mail, agencymail, initiatorID, client_code);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                //
                out.write(retVo.getStrName());
            } else if ("useremail".equalsIgnoreCase(type)) {
                String useremail = request.getParameter("useremail").trim();
                String compid = Validation.checkNull(request.getParameter("compid").trim());
                String result = ecomCntrlObj.CheckUserEmail(compid, useremail);
                if (result != null && result.equalsIgnoreCase("Exists")) {
                    out.print(false);
                } else {
                    out.print(true);
                }
            } else if ("eqp".equalsIgnoreCase(type)) {
                String size = validate.checkNull((request.getParameter("size")));
                String result = ecomCntrlObj.getEqpimentSize(size);
                if (result != null) {
                    out.println("valid~~" + result);
                }
            } else if ("deleteconfig".equalsIgnoreCase(type)) {
                String key = request.getParameter("configkey");
                try {
                    String result = ecomCntrlObj.deleteGlobalConfigDetails(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("containernovalidate".equalsIgnoreCase(type)) {
                String result = null;
                String containerno = request.getParameter("containerno");
                String eqpSizeValue = request.getParameter("eqpSizeValue");
                String soc = request.getParameter("soc");
                try {
                    result = ecomCntrlObj.getContainerValidate(containerno, soc, eqpSizeValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                out.print(result);
            } else if ("companyname".equalsIgnoreCase(type)) {
                List CmpNameList = (List) contexts.getAttribute("COMPANYNAME");
                String query = request.getParameter("q");
                if (CmpNameList != null && CmpNameList.size() > 0) {
                    int shTermListSize = CmpNameList.size();
                    List<String> cmpNameListObj = ajaxObj.getMatchedData(query, CmpNameList, shTermListSize);
                    if (cmpNameListObj != null && cmpNameListObj.size() > 0) {
                        int matchSize = cmpNameListObj.size();
                        String strMatch = "";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = Validation.checkNull(cmpNameListObj.get(i).toString());
                            out.println(strMatch);
                        }
                    }
                }

            } else if ("SessionRefresh".equalsIgnoreCase(type)) {
//                 System.out.println("[ECOM-WAR] UserProfileAjax Session Refreshed!!");
            } else if ("putNotificationMessage".equalsIgnoreCase(type)) {
//                 System.out.println("[ECOM-WAR] UserProfileAjax putNotificationMessage!");
                String notifymsg = request.getParameter("messagedata");
                String msgid = request.getParameter("msgid");
                String currentdate = DateConversion_New.getCurrentDateTime();
                List<NotificationVo> nolis = (List) session.getAttribute("ECOM_NOTIFICATION_DETAILS");
                if (nolis == null) {
                    nolis = new ArrayList<NotificationVo>();
                }

                NotificationVo nvo = new NotificationVo();
                nvo.setMessage(notifymsg);
                nvo.setNotificationdate(DateConversion_New.getCurrentDateDbFormat());
                nvo.setMsgid(msgid);
                nvo.setIsviewed(false);
                nolis.add(0, nvo);
                session.setAttribute("ECOM_NOTIFICATION_DETAILS", nolis);

                String notifytemp = "                    <div class='card active' id='" + msgid + "' onclick=\"onNotificationMessageViewAction('" + msgid + "')\">\n"
                        + "                        <div class='card-content'>\n"
                        + "                            <div class='card-body'>\n"
                        + "                                <p class='card-text'>" + notifymsg + "</p>\n"
                        + "                                <span class='float-right m-b-5 enotification_time' >" + currentdate + "</span>\n"
                        + "                            </div>\n"
                        + "                        </div>\n"
                        + "                    </div>";

                out.print(notifytemp);
            } else if ("putnotificationviewdetails".equalsIgnoreCase(type)) {
//                 System.out.println("[ECOM-WAR] UserProfileAjax putnotificationviewdetails");
                String msgid = validate.checkNull((request.getParameter("msgid")));
                String loginname = (String) session.getAttribute("SESSION_LOGINNAME");
                if (loginname != null && loginname.length() > 0 && msgid != null && msgid.length() > 0) {
                    ecomCntrlObj.setEcomNotificationViewStatus(msgid, loginname);

                    List<NotificationVo> nolis = (List) session.getAttribute("ECOM_NOTIFICATION_DETAILS");
                    if (nolis != null && nolis.size() > 0) {
                        for (int i = 0; i < nolis.size(); i++) {
                            NotificationVo nvo = (NotificationVo) nolis.get(i);
                            if (msgid.equalsIgnoreCase(nvo.getMsgid())) {
                                nvo.setIsviewed(true);
                                nolis.set(i, nvo);
                            }

                        }

                        session.setAttribute("ECOM_NOTIFICATION_DETAILS", nolis);
                    }
                }
                out.println("success");

            } else if ("resetlock".equalsIgnoreCase(type)) {
                ecomobj.resetWrgPasAtmp(request.getParameter("username"));
                out.println("success");
            } else if ("schedulemailcount".equalsIgnoreCase(type)) {
                Object attemptCountObj = session.getAttribute("ScheduleMailCount");
                int passattemptCount = (attemptCountObj != null) ? (int) attemptCountObj : 0;
                out.print(passattemptCount);
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] UserProfileAjax Exception in UserProfileAjax");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
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
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>

}
