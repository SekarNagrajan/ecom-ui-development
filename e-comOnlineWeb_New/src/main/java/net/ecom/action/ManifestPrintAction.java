/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.exp.MftPrintServiceVO.ManifestPrintInputVo;
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
import lrp.documentation.mft.print.bean.ManifestPrintServiceSessionBean;
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
 * @author preethi.elumalai
 */
public class ManifestPrintAction extends org.apache.struts.action.Action {

    String SUCCESS = "success";
    Validation validate = new Validation();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        System.out.println("[ECOM-WAR] ManifestPrintAction      >>sessionId>>>" + sessionId);
        ServletContext context = servlet.getServletContext();
//        BLPrintServiceSessionBean ecomCntrlObj = EJBLookUp.getEcomBLPrintBeanLookUp();
        String result = null, signature = null, path = null;
        try {
            ManifestPrintServiceSessionBean manifestLocObj = EJBLookUp.getManifestPrintServiceLookUp();
            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
            String blnm = Validation.checkNull(request.getParameter("blno"));
//            String blnm = "EPIRINDMUM63352";
            String agencycode = Validation.checkNull(request.getParameter("issueagency"));
//            String agencycode = "INDMUM";
            String manfestype = Validation.checkNull(request.getParameter("manifestype"));
            String blPrintWSPath = (String) context.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
            String jasperid="C".equalsIgnoreCase(manfestype)?"CMR":"F".equalsIgnoreCase(manfestype)?"FME":"";
            System.out.println("[ECOM-WAR] ManifestPrintAction      =======>blPrintWSPath" + blPrintWSPath);
            System.out.println("[ECOM-WAR] ManifestPrintAction      blnm: " + blnm);
            System.out.println("[ECOM-WAR] ManifestPrintAction      agencycode: " + agencycode);
            System.out.println("[ECOM-WAR] ManifestPrintAction      loginUser: " + loginUser);
            System.out.println("[ECOM-WAR] ManifestPrintAction      manfestype: " + manfestype);
            System.out.println("[ECOM-WAR] ManifestPrintAction      jasperid: " + jasperid);
            if (loginUser != null) {
                loginUser = "ECOM-" + loginUser;
                if (loginUser.length() > 15) {
                    loginUser = loginUser.substring(0, 15);
                }
            }
            String corpagency = ecomCntrlObj.getCorporteAgency(agencycode);

            //
            ManifestPrintInputVo printVO = new ManifestPrintInputVo();
            ArrayList printDetail = new ArrayList();
            printVO.setBlNo(checkNull(blnm));//EPIRINDMUM00061
            printVO.setUserName(checkNull(loginUser));
            printVO.setAgencyCode(checkNull(corpagency));
            printVO.setManifestType(checkNull(manfestype));
            printVO.setManifest("L");
            printVO.setLocationCode(checkNull(agencycode));
            printDetail.add(printVO);
            System.out.println("[ECOM-WAR] ManifestPrintAction      blnm==>" + blnm + "loginUser==>" + loginUser + "agencycode==>" + agencycode + "corpagency=>" + corpagency + "manfestype==>" + manfestype);
            System.out.println("[ECOM-WAR] ManifestPrintAction      printDetail==>" + printDetail);
            HashMap hm = manifestLocObj.callManifestPrint(printDetail);
            System.out.println("[ECOM-WAR] ManifestPrintAction      >>hm====>" + hm);
            Map<String, Object> data = new HashMap<String, Object>();
            if (!hm.containsKey("errors")) {
                HashMap hashm = (HashMap) hm.get("Map");
                path = (String) hashm.get("path");
                if (!path.endsWith(File.separator)) {
                    path = path + File.separator;
                }
                ArrayList list=new ArrayList();
                list.add(blnm);
                hashm.put("usessionid", sessionId);
                hashm.put("path", path);
                hashm.put("jasperid",jasperid);
                hashm.put("agencycode",corpagency);
                hashm.put("locationcode",agencycode);
//                hashm.put("path","/home/Share/LRPJaspers/");
//                hashm.put("logo","/home/Share/LRPJaspers/images/LogoManifest.gif");
                hashm.put("refno",list);
                data.put("input", hashm);
                data.put("details", hm.get("Details"));
                System.out.println("[ECOM-WAR] ManifestPrintAction      >>before jnlp>>" + data);
                String json = new Gson().toJson(data);
                System.out.println("[ECOM-WAR] ManifestPrintAction      json==>"+json);
                //
//                 json="{\"input\":{\"refno\":[\"EPIRINDMUM63352\"],\"Heading\":\"Disc. Manifest\",\"UserName\":\"ECOM-arul.praka\",\"sessionID\":\"17128399630760UOKQZ\",\"sessionid\":\"17128399630760UOKQZ\",\"jasperid\":\"CMR\",\"path\":\"/home/Share/LRPJaspers/\",\"usercr\":\"ECOM-arul.praka\",\"usessionid\":\"YFGOhT2jOzhkbjrTY5fViqOkv_cpNlB8PHdS7pVZ\",\"logo\":\"/home/Share/LRPJaspers/images/LogoManifest.gif\",\"Summary\":\"false\",\"moduleid\":\"MFT\",\"Etype\":\"Disc. Manifest\",\"locationcode\":\"INDMUM\",\"agencycode\":\"EAGIND\"},\"details\":{\"datasource\":\"DOC\",\"title\":\"Manifest\"}}";
                String url = blPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
                System.out.println("[ECOM-WAR] ManifestPrintAction      url===>"+url);
                Client jaxrsClient = ClientBuilder.newClient();
                WebTarget webTarget = jaxrsClient.target(url);
                Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                Response respon = invocation.invoke();
                String results = respon.readEntity(String.class);
                System.out.println("[ECOM-WAR] ManifestPrintAction      >>results>>" + results);
                Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
                }.getType());
//              
                String responseType = (String) hmdata.get("status");
                String viewType = (String) hmdata.get("type");
                if ("200".equalsIgnoreCase(responseType)) {
                    result = "Success";
                    //    if ("J".equalsIgnoreCase(viewType)) {
                    System.out.println("[ECOM-WAR] ManifestPrintAction      ===>Original");
                    String res = hmdata.get("content").toString();
                    session.setAttribute("Oblcontent", res);
                    request.setAttribute("blno",blnm);
//                     request.setParameter("blno",(Object)blnm);
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
            request.setAttribute("SuccessMsg", result);
        } catch (Exception e) {
            SUCCESS = "fail";
            session.setAttribute("Oblcontent", null);
            request.setAttribute("SuccessMsg", EcomConsts.SERVER_RETRIVE_ERROR);
            System.out.println("[ECOM-WAR] ManifestPrintAction      ==================>>>" + e);
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
//   callManifestPrint 

    private String checkNull(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
