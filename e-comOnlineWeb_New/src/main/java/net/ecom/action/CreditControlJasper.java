/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.exp.croserviceSessionbean.croServiceSessionbean;
import com.svm.lrp.exp.vo.croservicevo;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import svm.lrp.ecom.imp.bean.CreditControlPrintLocal;

/**
 *
 * @author gobinath.t
 */
public class CreditControlJasper extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

    CreditControlPrintLocal CreditObj = EJBLookUp.getCreditControlPrintLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String result = null, path = null;
        byte[] byteArray = null;
        String SUCCESS = "success";
        try {
            ServletContext context = servlet.getServletContext();
            HttpSession session = request.getSession();
            String sessionId = session.getId();
             System.out.println("[ECOM-WAR] CreditControlJasper    >>sessionId>>>" + sessionId);
            /* TODO output your page here. You may use following sample code. */
            String activitydate = Validation.checkNull(request.getParameter("activitydate"));
            String customerCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            //
            activitydate = DateConversion_New.convertDefaultDBFormatwithoutTime(activitydate);
            HashMap inputmap = new HashMap();
            inputmap.put("activitydate",activitydate);
            inputmap.put("customercode",customerCode);
            
            Map data = CreditObj.invokeCreditControlPrint(inputmap);

//             System.out.println("[ECOM-WAR] CreditControlJasper    >>data>>>>" + data);
            String json = new Gson().toJson(data);
            String blPrintWSPath = (String) context.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
            String url = blPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
//             System.out.println("[ECOM-WAR] CreditControlJasper    >>>>cro print service>>>>" + blPrintWSPath);
            Client jaxrsClient = ClientBuilder.newClient();
            WebTarget webTarget = jaxrsClient.target(url);
            Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
            Response respon = invocation.invoke();
            String results = respon.readEntity(String.class);
            Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
            }.getType());
//             System.out.println("[ECOM-WAR] CreditControlJasper    ===>ress" + hmdata.get("filename"));
//             System.out.println("[ECOM-WAR] CreditControlJasper    ===>type" + hmdata.get("type"));
            String responseType = (String) hmdata.get("status");
            String viewType = (String) hmdata.get("type");
//             System.out.println("[ECOM-WAR] CreditControlJasper    >>>responseType>>>" + responseType);
             System.out.println("[ECOM-WAR] CreditControlJasper    responseType: " + responseType + ", ress: " + hmdata.get("filename") + ", type: " + hmdata.get("type") + ", cro print service: " + blPrintWSPath + ", data: " + data + ", sessionId: " + sessionId);

            if ("200".equalsIgnoreCase(responseType)) {
                result = "Success";
                if ("P".equalsIgnoreCase(viewType)) {
                    String res = hmdata.get("content").toString();
                    session.setAttribute("crojasperbyte", res);
                     System.out.println("[ECOM-WAR] CreditControlJasper    >>>res>>" + res);
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
        
    }
    catch (Exception e

    
        ) {
            request.setAttribute("croprintstatus", "fail");
        SUCCESS = "fail";
        e.printStackTrace();
    }

    System.out.println (
            

    "======>SUCCESS>" + SUCCESS);
        return mapping.findForward (SUCCESS);
}
}
