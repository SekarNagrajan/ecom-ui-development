/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.exp.croserviceSessionbean.croServiceRemote;
import com.svm.lrp.exp.croserviceSessionbean.croServiceSessionbean;
import com.svm.lrp.exp.vo.croservicevo;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class CROJasper extends org.apache.struts.action.Action {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    croServiceSessionbean croObj = EJBLookUp.getCROBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    ServletContext context = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String result = null, path = null;
        byte[] byteArray = null;
        String SUCCESS = "success";
        try {
            ServletContext context = servlet.getServletContext();
//             System.out.println("[ECOM-WAR] CROJasper     >>>croObj >>" + croObj);
            HttpSession session = request.getSession();
            String sessionId = session.getId();
//             System.out.println("[ECOM-WAR] CROJasper     >>sessionId>>>"+sessionId);
            /* TODO output your page here. You may use following sample code. */
            String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
            String customerCode = Validation.checkNull(request.getParameter("customerCode"));
            String croNo = Validation.checkNull(request.getParameter("croNo"));
            String agencyCode = Validation.checkNull(request.getParameter("agencyCode"));
//             System.out.println("[ECOM-WAR] CROJasper     >>agencyCode>>>" + agencyCode);
            String corpAgency = ecomCntrlObj.getCorporteAgency(agencyCode);
//             System.out.println("[ECOM-WAR] CROJasper     >>corpAgency>cro>" + corpAgency);
             System.out.println("[ECOM-WAR] CROJasper     corpAgency: " + corpAgency + ", agencyCode: " + agencyCode + ", sessionId: " + sessionId + ", croObj: " + croObj);

            ArrayList list = new ArrayList();
            croservicevo vo = new croservicevo();
            vo.setAgencycode(corpAgency);
            vo.setLocationcode(agencyCode);
            vo.setUsercode(customerCode);
            vo.setCrono(croNo);
            list.add(vo);
            HashMap map = croObj.printCROBatch(list);
            if (loginUser != null && loginUser.length() > 16) {
                loginUser = loginUser.substring(0, 15);
            }
            //

             System.out.println("[ECOM-WAR] CROJasper     >>croPrintSessionId>>" + map);
            Map<String, Object> data = new HashMap<String, Object>();
            if (map != null && !map.containsKey("errors")) {
                HashMap hashm = (HashMap) map.get("input");
                hashm.put("moduleid", "CBP");
                path = (String) hashm.get("path");
                if (!path.endsWith(File.separator)) {
                    path = path + File.separator;
                }
//                 System.out.println("[ECOM-WAR] CROJasper     >>>path>>" + path);
                hashm.put("path", path);
                hashm.put("usessionid", sessionId);
                //hashm.put("agencycode", agencyCode);
                //hashm.put("locationcode", agencyCode);
                hashm.put("username", loginUser);
                //hashm.put("jasperid", "crobatch");

                data.put("input", hashm);
                data.put("details", map.get("details"));
//                 System.out.println("[ECOM-WAR] CROJasper     >>data>>>>" + data);
                String json = new Gson().toJson(data);
                String blPrintWSPath = (String) context.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
                String url = blPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
//                 System.out.println("[ECOM-WAR] CROJasper     >>>>cro print service>>>>" + blPrintWSPath);
                 System.out.println("[ECOM-WAR] CROJasper     cro print service: " + blPrintWSPath + ", data: " + data + ", path: " + path);

                Client jaxrsClient = ClientBuilder.newClient();
                WebTarget webTarget = jaxrsClient.target(url);
                Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                Response respon = invocation.invoke();
                String results = respon.readEntity(String.class);
                Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
                }.getType());
//                 System.out.println("[ECOM-WAR] CROJasper     ===>ress" + hmdata.get("filename"));
//                 System.out.println("[ECOM-WAR] CROJasper     ===>type" + hmdata.get("type"));
                String responseType = (String) hmdata.get("status");
                String viewType = (String) hmdata.get("type");
//                 System.out.println("[ECOM-WAR] CROJasper     >>>responseType>>>" + responseType);
                 System.out.println("[ECOM-WAR] CROJasper     responseType: " + responseType + ", ress: " + hmdata.get("filename") + ", type: " + hmdata.get("type"));

                if ("200".equalsIgnoreCase(responseType)) {
                    result = "Success";
                    if ("P".equalsIgnoreCase(viewType)) {
                        String res = hmdata.get("content").toString();
                        session.setAttribute("crojasperbyte", res);
                         System.out.println("[ECOM-WAR] CROJasper     >>>res>>" + res);
                        request.setAttribute("croprintstatus", "success");
                        request.setAttribute("viewType", viewType);
                        request.setAttribute("responetype", responseType);
                    }

                } else//500 Error
                {
                    SUCCESS = "fail";
                    request.setAttribute("croprintstatus", "fail");
                    result = EcomConsts.SERVER_RETRIVE_ERROR;
                    session.setAttribute("crojasperbyte", null);
                }
            } else {
                request.setAttribute("croprintstatus", "fail");
                SUCCESS = "fail";
                result = "fail";
            }
        } catch (Exception e) {
            request.setAttribute("croprintstatus", "fail");
            SUCCESS = "fail";
            e.printStackTrace();
        }
         System.out.println("[ECOM-WAR] CROJasper     ======>SUCCESS>" + SUCCESS);
        return mapping.findForward(SUCCESS);
    }
}
