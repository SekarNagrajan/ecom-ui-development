package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.controller.EcomCacheLoader;
import net.ecom.vo.RegisterMenuVO;

public class MainPage extends org.apache.struts.action.Action {

    //EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
    // EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String SUCCESS = "success";
        ServletContext context = servlet.getServletContext();
        HttpSession session = request.getSession();

        try {
            ArrayList objlst = (ArrayList) context.getAttribute("PORTLISTOBJ");
            String checkCacheRefresh = (String) context.getAttribute("checkCacheRefresh");
            System.out.println("[ECOM-WAR] MainPage     context.getAttribute('checkCacheRefresh')===> " + context.getAttribute("checkCacheRefresh"));
            saveToken(request);
            if (objlst == null || checkCacheRefresh == null || (!"CACHEREFRESHED".equalsIgnoreCase(checkCacheRefresh))) {
                EcomCacheLoader loader = new EcomCacheLoader();
                System.out.println("[ECOM-WAR] MainPage     >>>object is null in mainpage");
                loader.cacheLoader(context);
                loader.cacheLoaderWS(context);
                System.out.println("[ECOM-WAR] MainPage     checkCacheRefresh========> CACHEREFRESHED");
                context.setAttribute("checkCacheRefresh", "CACHEREFRESHED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        try {
            System.out.println("[ECOM-WAR] MainPage     Mainpage action ========> " + session.getAttribute("SESSION_LOGIN_TYPE"));
            if (session.getAttribute("SESSION_LOGIN_TYPE") == null) {
                session.setAttribute("SESSION_LOGIN_TYPE", "U");
            }

            //Map<String, List> imgAdMap=locobj.getLoginImagAdvisoryDets();
            //context.setAttribute("BGIMGADVISORYMAP", imgAdMap);

            /* String isvialogin = (String) session.getAttribute("IS_VIA_LOGIN");
            if ("Yes".equalsIgnoreCase(isvialogin)) {
                response.sendRedirect("Accesslogin.do?type=dashboard");
            }*/
            String isPasswordExpired = Validation.checkNull((String) session.getAttribute("isPasswordExpired"));
            if ("Y".equalsIgnoreCase(isPasswordExpired)) {
                response.sendRedirect("Accesslogin.do?type=reset");
            }
            if (request.getParameter("track") != null) {
                session.setAttribute("websitetrackno", request.getParameter("track"));
            }
            String websitetrackno = (String) session.getAttribute("websitetrackno");
            String usrname = (String) session.getAttribute("SESSION_USERNAME");
            if (usrname != null && usrname.length() > 0 && websitetrackno != null && websitetrackno.length() > 0) {
                ArrayList RegMenus = (ArrayList) session.getAttribute("RegMenus");
                List<RegisterMenuVO> menulist = (List<RegisterMenuVO>) context.getAttribute("usermenu");
                String trackingMenuCategory = menulist.stream().filter(o -> "TRK".equalsIgnoreCase(o.getRefNo()))
                        .map(RegisterMenuVO::getCategory).reduce("", String::concat);

                if ((RegMenus != null && RegMenus.contains("TRK")) || "D".equalsIgnoreCase(trackingMenuCategory)) {
                    session.setAttribute("websitetrackno", null);
                    response.sendRedirect(
                            "Accesslogin.do?tracktype=logintracking&type=Tracking&logintracno=" + websitetrackno);
                } else {
                    session.setAttribute("websitetrackno", null);
                }
            }

            String redirectLink = (String) session.getAttribute("SESSION_REDIRECT_STARTING_PAGE");
            System.out.println("[ECOM-WAR] MainPage     redirectLink ================ > " + redirectLink);
            if (redirectLink != null && redirectLink.length() > 0 && (!("Y".equalsIgnoreCase(isPasswordExpired)))) {
                response.sendRedirect(redirectLink);
                return null;
            }

//            String res = (String) session.getAttribute("clientcode");
//            if ("zh".equalsIgnoreCase(res)) {
//                session.setAttribute("clientcode", "zh");
//            } else {
//                //String clientcode = "ESL";
//                session.setAttribute("SelectedLanguage", "English");
//                //session.setAttribute("clientcode", clientcode);
//            }
            //session.setAttribute("LOGINFORMENU", "NO");
            String selectedLang = (String) session.getAttribute("SelectedLanguage");
            if (selectedLang == null) {
                session.setAttribute("SelectedLanguage", "en");
            }

            if ("ESL".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_E";
            }
            if ("GWF".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_G";
            }
            if ("TSL".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_T";
            }
            if ("ALX".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_A";
            }
            if ("LSC".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE)) || "NMC".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_L";
            }
            if ("OEL".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_O";
            }
            if ("CSD".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_C";
            }
            if ("FLK".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_F";
            }
            if ("MIL".equalsIgnoreCase((String) context.getAttribute(GeneralConf.CARRIER_CODE))) {
                SUCCESS = "login_M";
            }
            if ("mobileapp".equalsIgnoreCase(request.getParameter("type"))) {
                SUCCESS = "login_mobile";
            }
            request.setAttribute("defaultloginopen", request.getParameter("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
