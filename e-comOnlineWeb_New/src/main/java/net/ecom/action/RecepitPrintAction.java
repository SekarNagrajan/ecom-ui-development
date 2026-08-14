/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.imp.getjasperinputs.ejb.getjasperinputsBeanLocal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
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
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sasikumar.r
 */
public class RecepitPrintAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    String SUCCESS = "success";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
     HttpSession session = request.getSession();
        ServletContext context = servlet.getServletContext();
        getjasperinputsBeanLocal ecomCntrlObj = EJBLookUp.getEcomAVNPrintBeanLookUp();
        String result = null;
        try {
            System.out.println("[ECOM-WAR] RecepitPrintAction  =============>Calling<==========");
            String recpNumber = Validation.checkNull(request.getParameter("recpNumber"));
            System.out.println("[ECOM-WAR] RecepitPrintAction  ===========>recpNumber" + recpNumber);
            if (recpNumber != null && recpNumber.length() > 0) {

                String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));

                System.out.println("[ECOM-WAR] RecepitPrintAction  recpNumber: " + recpNumber);

                String blPrintWSPath = (String) context.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
                System.out.println("[ECOM-WAR] RecepitPrintAction  =======>blPrintWSPath" + blPrintWSPath);

                if (loginUser != null && loginUser.length() > 15) {
                    loginUser = loginUser.substring(0, 15);
                }
                //
                HashMap hashm = new HashMap();
                hashm.put("moduleid", "RCE");
                hashm.put("refno", recpNumber);//AEESADAN28
                hashm.put("username", loginUser);

                HashMap hm = ecomCntrlObj.getJasper(hashm);
                System.out.println("[ECOM-WAR] RecepitPrintAction  =====================================" + hm);
                if (!hm.containsKey("error")) {
                    //
                    HashMap refhm = (HashMap) hm.get("input");
                    String refno = (String) refhm.get("refno");
                    refhm.put("agencycode",refhm.get("agencycode"));
                    List reflst = new ArrayList();
                    reflst.add(refno);
                    refhm.put("refno", reflst);
                    hm.put("input", refhm);
                    System.out.println("[ECOM-WAR] RecepitPrintAction  ===>hmdata" + hm);
                    //
                    String json = new Gson().toJson(hm);
                    //
                    String url = blPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
                    Client jaxrsClient = ClientBuilder.newClient();
                    WebTarget webTarget = jaxrsClient.target(url);
                    Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                    Response respon = invocation.invoke();
                    String results = respon.readEntity(String.class);
                    Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                    String responseType = (String) hmdata.get("status");
                    String viewType = (String) hmdata.get("type");
                    if ("200".equalsIgnoreCase(responseType)) {
                        result = "Success";
                        System.out.println("[ECOM-WAR] RecepitPrintAction  ===>Original");
                        String res = hmdata.get("content").toString();
                        session.setAttribute("outputcontent", res);
                        request.setAttribute("viewType", viewType);
                        request.setAttribute("responetype", responseType);
                    } else if ("205".equalsIgnoreCase(responseType))//205 Already opened
                    {
                        result = "Success";
                        request.setAttribute("responetype", responseType);
                        session.setAttribute("outputcontent", null);
                    } else//500 Error
                    {
                        result = EcomConsts.SERVER_RETRIVE_ERROR;
                        session.setAttribute("outputcontent", null);
                    }
                } else {
                    result = (String) hm.get("error");
                    session.setAttribute("outputcontent", null);
                }
                //
            } else {
                SUCCESS = "fail";
                result = "fail";
            }
            //
            request.setAttribute("SuccessMsg", result);
            //
        } catch (Exception e) {
            SUCCESS = "fail";
            session.setAttribute("outputcontent", null);
            request.setAttribute("SuccessMsg", EcomConsts.SERVER_RETRIVE_ERROR);
            System.out.println("[ECOM-WAR] RecepitPrintAction  ==================>>>" + e);
        }
        return mapping.findForward(SUCCESS);
    }
}
