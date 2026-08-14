/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.exp.BOLPrintServiceVO.BLPrintServiceVo;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lrp.documentation.bl.print.bean.BLPrintServiceSessionBean;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;

public class MCNPrintAction extends org.apache.struts.action.Action {

    String SUCCESS = "success";
    EcomBLBeanRemote blLocObj = EJBLookUp.getBLBeanLookUp();
    Validation validate = new Validation();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        String sessionId = session.getId();
        System.out.println("[ECOM-WAR] MCNPrintAction   >>sessionId>>>" + sessionId);
        ServletContext context = servlet.getServletContext();
        BLPrintServiceSessionBean ecomCntrlObj = EJBLookUp.getEcomBLPrintBeanLookUp();
        String result = null, signature = null, path = null, mcnStatus = "D";
        try {
            String agencydocid = Validation.checkNull(request.getParameter("issueagency"));
            String type = Validation.checkNull(request.getParameter("type"));
            if ("O".equalsIgnoreCase(type)) {
                signature = blLocObj.AgencySigmature(agencydocid);
            } else {
                signature = "yes";
            }
            System.out.println("[ECOM-WAR] MCNPrintAction   >>signature>>>" + signature);

//            String blstatusandtypestr = blLocObj.getBLStatus(Validation.checkNull(request.getParameter("blno")));
//            String blstatusandtypestr = "";
            String blstatusandtypestr = blLocObj.getMCNStatus(Validation.checkNull(request.getParameter("fcnno")));

            System.out.println("[ECOM-WAR] MCNPrintAction   blstatusandtypestr=> " + blstatusandtypestr);
            String blstatusandtype[] = Validation.checkNull(blstatusandtypestr).split("~~");
            String printusr = Validation.checkNull(blstatusandtype[0]);
            String bltype = null;
            if (blstatusandtype.length > 1) {
                bltype = Validation.checkNull(blstatusandtype[1]);
            }
            if (blstatusandtype.length > 2) {
                mcnStatus = Validation.checkNull(blstatusandtype[2]);
            }
            System.out.println("[ECOM-WAR] MCNPrintAction   printusr=>  " + printusr + " &type=> " + type + " &bltype=> " + bltype);
            if (!("O".equalsIgnoreCase(type) && Validation.checkNull(printusr).length() > 0)) {
                if (signature != null && signature.length() > 0 && signature.equalsIgnoreCase("Yes")) {

                    String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                    String blnm = Validation.checkNull(request.getParameter("blno"));

                    String blstatus = Validation.checkNull(request.getParameter("blstatus"));
                    String agencycode = Validation.checkNull(request.getParameter("agencycode"));
                    String originalcp = Validation.checkNull(request.getParameter("originalcp"));
                    String draftcp = Validation.checkNull(request.getParameter("draftcp"));
                    String shipmentStatus = Validation.checkNull(request.getParameter("shipmentStatus"));
                    String shipmentdate = Validation.checkNull(request.getParameter("shipmentdate"));
                    String fcnno = Validation.checkNull(request.getParameter("fcnno"));

                    String blPrintWSPath = (String) context.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
                    System.out.println("[ECOM-WAR] MCNPrintAction   blPrintWSPath " + blPrintWSPath + 
                   " blnm " + blnm + 
                   " type " + type + 
                   " agencycode " + agencycode + 
                   " originalcp " + originalcp + 
                   " draftcp " + draftcp + 
                   " loginUser " + loginUser + 
                   " blstatus " + blstatus + 
                   " agencydocid " + agencydocid + 
                   " shipmentStatus " + shipmentStatus + 
                   " shipmentdate " + shipmentdate + 
                   " fcnno " + fcnno);

                    //System.out.println("[ECOM-WAR] MCNPrintAction   issueagency: " + issueagency);
                    //
//                blnm = "EPIRINDMUM00061";
//                originalcp = "1";
//                draftcp = "1";
//                agencydocid = "INDMUM";
                    //
                    if (loginUser != null) {
                        loginUser = "ECOM-" + loginUser;
                        if (loginUser.length() > 15) {
                            loginUser = loginUser.substring(0, 15);
                        }
                    }
                    //
                    BLPrintServiceVo printVO = new BLPrintServiceVo();
                    ArrayList printDetail = new ArrayList();
                    if (fcnno != null && Validation.checkNull(fcnno).length() > 0) {
                        System.out.println("[ECOM-WAR] MCNPrintAction   inside==> " + fcnno);
                        printVO.setModuleID("FCN");
                        if ("D".equalsIgnoreCase(mcnStatus) || "C".equalsIgnoreCase(mcnStatus)) {
                            blstatus = "D";
                        }
                        if ("A".equalsIgnoreCase(mcnStatus)) {
                            blstatus = "C";
                            if (printusr.length() > 0) {
                                blstatus = "I";
                            }
                        }
                        printVO.setFcnno(Validation.checkNull(fcnno));
                    } else {
                        printVO.setModuleID(GeneralConf.MODULE_ID);
                    }
                    printVO.setBlNo(blnm);//EPIRINDMUM00061
                    printVO.setBlStatus(blstatus);
                    printVO.setOriginals(originalcp);
                    printVO.setCopies(draftcp);
                    printVO.setExpImp(GeneralConf.MODULE_TYPE);
                    printVO.setAgencyId(agencydocid);
                    printVO.setIsSignature(false);
                    if ("O".equalsIgnoreCase(type)) {
                        if (!"SEAWAY B/L".equalsIgnoreCase(bltype)) {
                            System.out.println("[ECOM-WAR] MCNPrintAction   inside If SEAWAY B/L");
                            printVO.setIsSignature(true);
                        }
                        printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_O);
                    } else if ("I".equalsIgnoreCase(type) || ("C".equalsIgnoreCase(type))) {
                        boolean BLsignature = blLocObj.getBLServiceIsSignature(agencydocid);
                        if (BLsignature) {
                            System.out.println("[ECOM-WAR] MCNPrintAction   inside");
                            printVO.setIsSignature(true);
                        }
                        printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_N);
                    } else {
                        printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_D);
                    }
                    if ("R".equalsIgnoreCase(shipmentStatus)) {
                        printVO.setIsRFS(true);
                        printVO.setRfsDate(shipmentdate);
                    } else {
                        printVO.setIsRFS(false);
                    }

                    printVO.setUserCR(loginUser);
                    printVO.setFooterText(GeneralConf.BL_FOOTER_TEXT);
                    printVO.setIsPrint(true);
                    printVO.setIsPrintFreight(true);
                    printVO.setBlBased(GeneralConf.BL_BASED);
                    printDetail.add(printVO);
                    HashMap hm = ecomCntrlObj.callPrint(printDetail);
                    System.out.println("[ECOM-WAR] MCNPrintAction   >>hm====>" + hm);
                    Map<String, Object> data = new HashMap<String, Object>();
                    if (!hm.containsKey("errors")) {
                        HashMap hashm = (HashMap) hm.get("input");
                        path = (String) hashm.get("path");
                        if (!path.endsWith(File.separator)) {
                            path = path + File.separator;
                        }
                        hashm.put("usessionid", sessionId);
                        hashm.put("path", path);
                        if (fcnno != null && Validation.checkNull(fcnno).length() > 0) {
                            if ("O".equalsIgnoreCase(type)) {
                                hashm.put("jasperid", "FCJ");
                            } else {
                                hashm.put("jasperid", "FCJ1");
                            }
                        }
                        data.put("input", hashm);
                        data.put("details", hm.get("details"));
                        System.out.println("[ECOM-WAR] MCNPrintAction   >>before jnlp>>" + data);
                        String json = new Gson().toJson(data);
                        //
                        String url = blPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
                        Client jaxrsClient = ClientBuilder.newClient();
                        WebTarget webTarget = jaxrsClient.target(url);
                        Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                        Response respon = invocation.invoke();
                        String results = respon.readEntity(String.class);
                        System.out.println("[ECOM-WAR] MCNPrintAction   >>results>>" + results);
                        Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
                        }.getType());
//                System.out.println("[ECOM-WAR] MCNPrintAction   ===>hmdata" + hmdata + "===>ress" + hmdata.get("filename")+"===>type" + hmdata.get("type"));
                        String responseType = (String) hmdata.get("status");
                        String viewType = (String) hmdata.get("type");
                        if ("200".equalsIgnoreCase(responseType)) {
                            result = "Success";
                            //    if ("J".equalsIgnoreCase(viewType)) {
                            System.out.println("[ECOM-WAR] MCNPrintAction   ===>Original");
                            String res = hmdata.get("content").toString();
                            session.setAttribute("Oblcontent", res);
                            // }
                            request.setAttribute("viewType", viewType);
                            request.setAttribute("responetype", responseType);
                        } else if ("205".equalsIgnoreCase(responseType))//205 Already opened
                        {
                            result = "Success";
                            request.setAttribute("responetype", responseType);
                            session.setAttribute("Oblcontent", null);
                        } else//500 Error
                        {
                            result = EcomConsts.SERVER_RETRIVE_ERROR;
                            session.setAttribute("Oblcontent", null);
                        }
                    } else {
                        Map hsmap = (HashMap) hm.get("errors");
                        result = (String) hsmap.get(blnm.trim());
                        session.setAttribute("Oblcontent", null);
                    }
                    //
                } else {
                    SUCCESS = "fail";
                    result = "fail";
                }
            } else {
                SUCCESS = "fail";
                result = "This item has already been printed.";
            }
            //
            request.setAttribute("SuccessMsg", result);
            //
        } catch (Exception e) {
            SUCCESS = "fail";
            session.setAttribute("Oblcontent", null);
            request.setAttribute("SuccessMsg", EcomConsts.SERVER_RETRIVE_ERROR);
            System.out.println("[ECOM-WAR] MCNPrintAction   e ===>>>" + e);
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
