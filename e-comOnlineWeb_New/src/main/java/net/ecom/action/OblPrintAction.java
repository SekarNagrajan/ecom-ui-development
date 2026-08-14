/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.exp.BOLPrintServiceVO.BLPrintServiceVo;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.json.Json;
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
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lrp.documentation.bl.print.bean.BLPrintServiceSessionBean;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;

public class OblPrintAction extends org.apache.struts.action.Action {

    String SUCCESS = "success";
    EcomBLBeanRemote blLocObj = EJBLookUp.getBLBeanLookUp();
    Validation validate = new Validation();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        String sessionId = session.getId();
        System.out.println("[ECOM-WAR] OblPrintAction sessionId" + sessionId);
        ServletContext context = servlet.getServletContext();
        BLPrintServiceSessionBean ecomCntrlObj = EJBLookUp.getEcomBLPrintBeanLookUp();
        String result = null, signature = null, path = null,maskPortJasperId="";
        try {
            String printdetails = Validation.checkNull(request.getParameter("blprintdetails"));
//            String printype = Validation.checkNull(request.getParameter("printype"));
            System.out.println("[ECOM-WAR] OblPrintAction printdetails" + printdetails.length());
            ObjectMapper objectMapper = new ObjectMapper();

            List<Map<String, Object>> bookings = new ArrayList<>();
            try {
                bookings = objectMapper.readValue(printdetails, new TypeReference<List<Map<String, Object>>>() {
                });
                System.out.println("[ECOM-WAR] OblPrintAction bookings  successfully, size: " + bookings.size());
            } catch (Exception e) {
                System.out.println("[ECOM-WAR] Exception in OblPrintAction " + e.getMessage());
                e.printStackTrace();
            }
            String agencydocid = "", type = "", loginUser = "", blnm = "", blstatus = "", agencycode = "", originalcp = "", draftcp = "", shipmentStatus = "", shipmentdate = "", fcnno = "";
            String valuedOriginalBlList = "Yes", agencyDocIds = "", types = "", blNumbers = "";
            ArrayList<BLPrintServiceVo> printDetail = new ArrayList<>();
            String blPrintWSPath = (String) context.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
            System.out.println("[ECOM-WAR] OblPrintAction blPrintWSPath" + blPrintWSPath);
            for (Map<String, Object> booking : bookings) {
                type = Validation.checkNull((String) booking.get("type"));
//                System.out.println("[ECOM-WAR] OblPrintAction type" + type);
                if ("O".equalsIgnoreCase(type)) {
                    String currentAgencyDocId = Validation.checkNull((String) booking.get("issueagency"));
                    String currentBlnm = Validation.checkNull((String) booking.get("blno"));
                    agencyDocIds += (agencyDocIds.isEmpty() ? "" : ",") + currentAgencyDocId;
                    blNumbers += (blNumbers.isEmpty() ? "" : ",") + currentBlnm;
                } else {
                    signature = "yes";
                    break;
                }
            }
            System.out.println("[ECOM-WAR] OblPrintAction signature: " + signature + "/" + agencyDocIds + "/" + "/" + blNumbers);
            Map<String, String> blStatusAndTypeStr = blLocObj.getBLStatus(blNumbers);
            String blType = "";
            boolean blIssued = false;
            for (Map.Entry<String, String> entry : blStatusAndTypeStr.entrySet()) {
                blType = entry.getValue();
                blnm = entry.getKey();
                if (Validation.checkNull(blType).length() > 0 && blType.contains("~~")) {
                    blType = blType.split("~~")[0];
                }
                if ("O".equalsIgnoreCase(type) && "I".equalsIgnoreCase(blType)) {
                    blIssued = true;
                    break;
                }
            }
            System.out.println("[ECOM-WAR] OblPrintAction valueAtLastIndex" + blType);
            if (!blIssued) {
                signature = blLocObj.AgencySigmature(agencyDocIds);
                if (!signature.contains("Yes")) {
                    String[] blSignature = Validation.checkNull(signature).split("~~");
                    if (blSignature.length > 5) {
                        blnm = Validation.checkNull(blSignature[1]);
                    }
                    valuedOriginalBlList = "No";
                    SUCCESS = "fail";
                    result = "fail";
                }
            } else {
                valuedOriginalBlList = "No";
                SUCCESS = "fail";
                result = "BL Already Issued!!";
            }

            if ("yes".equalsIgnoreCase(valuedOriginalBlList)) {
                for (Map<String, Object> booking : bookings) {
                    agencydocid = Validation.checkNull((String) booking.get("issueagency"));
                    type = Validation.checkNull((String) booking.get("type"));
                    loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                    blnm = Validation.checkNull((String) booking.get("blno"));
                    blstatus = Validation.checkNull((String) booking.get("blstatus"));
                    agencycode = Validation.checkNull((String) booking.get("agencycode"));
                    originalcp = Validation.checkNull((String) booking.get("originalcp"));
                    draftcp = Validation.checkNull((String) booking.get("draftcp"));
                    shipmentStatus = Validation.checkNull((String) booking.get("shipmentStatus"));
                    shipmentdate = Validation.checkNull((String) booking.get("shipmentdate"));
                    fcnno = Validation.checkNull((String) booking.get("fcnno"));
                    System.out.println("[ECOM-WAR] OblPrintAction bookings.size()=>" + bookings.size());
                    System.out.println("[ECOM-WAR] OblPrintAction blnm: " + blnm);
                    System.out.println("[ECOM-WAR] OblPrintAction type: " + type);
                    System.out.println("[ECOM-WAR] OblPrintAction agencycode: " + agencycode);
                    System.out.println("[ECOM-WAR] OblPrintAction originalcp: " + originalcp);
                    System.out.println("[ECOM-WAR] OblPrintAction draftcp: " + draftcp);
                    System.out.println("[ECOM-WAR] OblPrintAction loginUser: " + loginUser);
                    System.out.println("[ECOM-WAR] OblPrintAction blstatus: " + blstatus);
                    System.out.println("[ECOM-WAR] OblPrintAction agencydocid: " + agencydocid);
                    System.out.println("[ECOM-WAR] OblPrintAction shipmentStatus: " + shipmentStatus);
                    System.out.println("[ECOM-WAR] OblPrintAction shipmentdate: " + shipmentdate);
                    System.out.println("[ECOM-WAR] OblPrintAction fcnno: " + fcnno);
                    //System.out.println("issueagency: " + issueagency);

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
                    if (fcnno != null && Validation.checkNull(fcnno).length() > 0) {
                        System.out.println("[ECOM-WAR] OblPrintAction inside " + fcnno);
                        printVO.setModuleID("FCN");
                        blstatus = "C";
                        String MCNstatusandtypestr = blLocObj.getMCNStatus(fcnno);
                        System.out.println("blstatusandtypestr=> " + MCNstatusandtypestr);
                        String mcnstatusandtype[] = Validation.checkNull(MCNstatusandtypestr).split("~~");
                        String printuser = Validation.checkNull(mcnstatusandtype[0]);
                        if (printuser.length() > 0) {
                            blstatus = "I";
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
                    String bltype = blStatusAndTypeStr.get(blnm);
                    if (Validation.checkNull(bltype).length() > 0) {
                        String[] splitValues = bltype.split("~~");
                        if (splitValues.length > 1) {
                            bltype = splitValues[1];
                        }
                    }
                    if ("O".equalsIgnoreCase(type)) {
                        if (!"SEAWAY B/L".equalsIgnoreCase(bltype)) {
                            System.out.println("inside If SEAWAY B/L");
                            printVO.setIsSignature(true);
                        }
                        printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_O);
                        maskPortJasperId="MBOJ";
                    } else if ("I".equalsIgnoreCase(type) || ("C".equalsIgnoreCase(type))) {
                        boolean BLsignature = blLocObj.getBLServiceIsSignature(agencydocid);
                        if (BLsignature) {
                            System.out.println("inside");
                            printVO.setIsSignature(true);
                        }
                        printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_N);
                        maskPortJasperId="MBOJ2";
                    } else {
                        printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_D);
                        maskPortJasperId="MBOJ1";
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

                }
                //
                if (printDetail != null && printDetail.size() > 0) {
                    System.out.println("[ECOM-WAR] OblPrintAction  printDetail: " + printDetail.size());
                    HashMap hm = ecomCntrlObj.callPrint(printDetail);
                    System.out.println("[ECOM-WAR] OblPrintAction hm====>" + hm);
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
                            hashm.put("jasperid", "FCJ1");
                            maskPortJasperId="MFCJ1";
                        }
                        if ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                            hashm.put("jasperid", maskPortJasperId);
                        }
                        data.put("input", hashm);
                        data.put("details", hm.get("details"));
                        System.out.println("[ECOM-WAR] OblPrintAction before jnlp>>" + data);
                        String json = new Gson().toJson(data);
                        //
                        String url = blPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
                        Client jaxrsClient = ClientBuilder.newClient();
                        WebTarget webTarget = jaxrsClient.target(url);
                        Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                        Response respon = invocation.invoke();
                        String results = respon.readEntity(String.class);
                        System.out.println("[ECOM-WAR] OblPrintAction results>>" + results);
                        Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
                        }.getType());
//                System.out.println("===>hmdata" + hmdata);
//                System.out.println("===>ress" + hmdata.get("filename"));
//                System.out.println("===>type" + hmdata.get("type"));
                        String responseType = (String) hmdata.get("status");
                        String viewType = (String) hmdata.get("type");
                        System.out.println("[ECOM-WAR] OblPrintAction Original" + responseType);
                        request.setAttribute("viewType", viewType);
                        if ("200".equalsIgnoreCase(responseType)) {
                            result = "Success";
                            //    if ("J".equalsIgnoreCase(viewType)) {
//                            System.out.println("[ECOM-WAR] OblPrintAction Original");
                            String res = hmdata.get("content").toString();
                            session.setAttribute("Oblcontent", res);
                            // }
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
                        result = (String)hsmap.toString().replace("{", "").replace("}", "");
                        session.setAttribute("Oblcontent", null);
                    }
                }
            }
            request.setAttribute("SuccessMsg", result);
            request.setAttribute("BlNo", blnm);
//            request.setAttribute("printype", printype);
            //
        } catch (Exception e) {
            SUCCESS = "fail";
            session.setAttribute("Oblcontent", null);
            request.setAttribute("SuccessMsg", EcomConsts.SERVER_RETRIVE_ERROR);
            System.out.println("[ECOM-WAR] Exception in OblPrintAction==================>>>" + e);
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
