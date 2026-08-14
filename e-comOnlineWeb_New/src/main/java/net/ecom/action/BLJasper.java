package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.exp.BOLPrintServiceVO.BLPrintServiceVo;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lrp.documentation.bl.print.bean.BLPrintServiceSessionBean;
import net.ecom.common.EcomConsts;
import net.ecom.lookup.EJBLookUp;

public class BLJasper extends HttpServlet {

    ServletContext context = null;

    @Override
    public void init(ServletConfig config) {
        try {
            context = config.getServletContext();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        byte[] byteArray = null;
        String result = null;
        PrintWriter out = response.getWriter();
        BLPrintServiceSessionBean ecomCntrlObj = EJBLookUp.getEcomBLPrintBeanLookUp();
        HttpSession session = request.getSession();
        try {
            String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
            String blnm = Validation.checkNull(request.getParameter("blno"));
            String type = Validation.checkNull(request.getParameter("type"));
            String blstatus = Validation.checkNull(request.getParameter("blstatus"));
            String agencycode = Validation.checkNull(request.getParameter("agencycode"));
            String originalcp = Validation.checkNull(request.getParameter("originalcp"));
            String draftcp = Validation.checkNull(request.getParameter("draftcp"));
            String agencydocid = Validation.checkNull(request.getParameter("agencydocid"));
//             System.out.println("[ECOM-WAR] BLJasper         blnm: " + blnm);
//             System.out.println("[ECOM-WAR] BLJasper         type: " + type);
//             System.out.println("[ECOM-WAR] BLJasper         agencycode: " + agencycode);
//             System.out.println("[ECOM-WAR] BLJasper         originalcp: " + originalcp);
//             System.out.println("[ECOM-WAR] BLJasper         draftcp: " + draftcp);
//             System.out.println("[ECOM-WAR] BLJasper         loginUser: " + loginUser);
//             System.out.println("[ECOM-WAR] BLJasper         blstatus: " + blstatus);
//             System.out.println("[ECOM-WAR] BLJasper         agencydocid: " + agencydocid);
            String blPrintWSPath = (String) context.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
//             System.out.println("[ECOM-WAR] BLJasper         =======>blPrintWSPath" + blPrintWSPath);
             System.out.println("[ECOM-WAR] BLJasper         blPrintWSPath:" + blPrintWSPath + ",blnm: " + blnm + ", type: " + type + ", agencycode: " + agencycode + ", originalcp: " + originalcp + ", draftcp: " + draftcp + ", loginUser: " + loginUser + ", blstatus: " + blstatus + ", agencydocid: " + agencydocid);

            //
            if (loginUser != null && loginUser.length() > 15) {
                loginUser = loginUser.substring(0, 15);
            }
            BLPrintServiceVo printVO = new BLPrintServiceVo();
            ArrayList printDetail = new ArrayList();
            printVO.setModuleID(GeneralConf.MODULE_ID);
            printVO.setBlNo(blnm);//EPIRINDMUM00061
            printVO.setBlStatus(blstatus);
            printVO.setOriginals(originalcp);
            printVO.setCopies(draftcp);
            printVO.setExpImp(GeneralConf.MODULE_TYPE);
            printVO.setAgencyId(agencydocid);
            if ("O".equalsIgnoreCase(type)) {
                printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_O);
            } else if ("I".equalsIgnoreCase(type) || ("C".equalsIgnoreCase(type))) {
                printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_N);
            } else {
                printVO.setPrintType(GeneralConf.BL_PRINT_TYPE_D);
            }
            printVO.setUserCR(loginUser);
            printVO.setFooterText(GeneralConf.BL_FOOTER_TEXT);
            printVO.setIsPrint(true);
            printVO.setBlBased(GeneralConf.BL_BASED);
            printDetail.add(printVO);
            HashMap hm = ecomCntrlObj.callPrint(printDetail);
             System.out.println("[ECOM-WAR] BLJasper         ===>HM" + hm);
            Map<String, Object> data = new HashMap<String, Object>();
            if (!hm.containsKey("errors")) {
                data.put("input", hm.get("input"));
                data.put("details", hm.get("details"));
                String json = new Gson().toJson(data);
                //
                String url = blPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
                Client jaxrsClient = ClientBuilder.newClient();
                WebTarget webTarget = jaxrsClient.target(url);
                Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                Response respon = invocation.invoke();
                String results = respon.readEntity(String.class);
                Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
                }.getType());
//                 System.out.println("[ECOM-WAR] BLJasper         ===>ress" + hmdata.get("filename"));
//                 System.out.println("[ECOM-WAR] BLJasper         ===>type" + hmdata.get("type"));
                 System.out.println("[ECOM-WAR] BLJasper         ress: " + hmdata.get("filename") + ", type: " + hmdata.get("type"));

                String responseType = (String) hmdata.get("status");
                String viewType = (String) hmdata.get("type");
                if ("200".equalsIgnoreCase(responseType)) {
                    result = "Success";
                    if ("P".equalsIgnoreCase(viewType)) {
                        byteArray = (byte[]) Base64.getDecoder().decode(hmdata.get("content").toString());
                        session.setAttribute("jasperbyte", byteArray);
                    }
                } else//500 Error
                {
                    result = EcomConsts.SERVER_RETRIVE_ERROR;
                    session.setAttribute("jasperbyte", null);
                }
            } else {
                 System.out.println("[ECOM-WAR] BLJasper         ===>HM" + hm);
                Map hsmap = (HashMap) hm.get("errors");
                result = (String) hsmap.get(blnm.trim());
                session.setAttribute("jasperbyte", null);
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] BLJasper         =======>Error");
            session.setAttribute("jasperbyte", null);
            result = EcomConsts.SERVER_RETRIVE_ERROR;
            e.printStackTrace();
        } finally {
        }
        out.print(result);
         System.out.println("[ECOM-WAR] BLJasper         ======>errors" + result);

    }

    private void forward(String forwardpage, HttpServletRequest request, HttpServletResponse response) {
        try {
             System.out.println("[ECOM-WAR] BLJasper         ======Launching JNLP....");
            RequestDispatcher rd = request.getRequestDispatcher(forwardpage);
            rd.forward(request, response);
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] BLJasper         Exception while forwarding :" + e);
            e.printStackTrace();
        }
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
