/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.filters;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;

/**
 *
 * @author gobinath.t
 */
public class SecurityFilter implements Filter {

    private FilterConfig filterConfig = null;
    private Set<String> excludedPaths = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String excludedParam = filterConfig.getInitParameter("excludedPaths");
        if (excludedParam != null && !excludedParam.trim().isEmpty()) {
            for (String path : excludedParam.split(",")) {
                excludedPaths.add(path.trim().toLowerCase());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = null;
        String strParameter = null, strParamValue = null, imgsessionid = null;
        String strParameterValues[];
        boolean result = true;
        try {
            LogUtil.log("SecurityFilter Inside SecurityFilter");

            httpRequest = (HttpServletRequest) request;
            String servletPath = httpRequest.getServletPath();
            String enable_Xss_Script_Blocker = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Xss_Script_Blocker);

            if (!"Y".equalsIgnoreCase(enable_Xss_Script_Blocker)) {
                chain.doFilter(request, response);
                return;
            }
            if (excludedPaths.contains(servletPath.toLowerCase())) {
                chain.doFilter(request, response);
                return;
            }
            if ("/RegistrationAjax".equalsIgnoreCase(servletPath)) {
                String queryString = httpRequest.getQueryString();
                String contentType = httpRequest.getContentType();
                boolean isUploadFile = (queryString != null && queryString.toLowerCase().contains("type=uploadfileinregistration"))
                        || (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data"));
                if (isUploadFile) {
                    chain.doFilter(request, response);
                    return;
                }
            }
            HttpSession session = httpRequest.getSession();
            httpRequest.setCharacterEncoding("UTF-8");
            Enumeration en = httpRequest.getParameterNames();
            //System.out.println("[ECOM-WAR] SecurityFilter en=> " + en);
            XssScriptsBlocker xsb = new XssScriptsBlocker();
            //System.out.println("[ECOM-WAR] SecurityFilter xsb => " + xsb);
            if ("Y".equalsIgnoreCase(enable_Xss_Script_Blocker)) {
                while (en.hasMoreElements()) {
                    strParameter = (String) en.nextElement();
                    LogUtil.log("SecurityFilter strParameter: " + strParameter);
                    if (!("strupdate".equalsIgnoreCase(strParameter) && httpRequest.getServletPath().equals("/UserProfileAjax") && "emailupdateformate".equalsIgnoreCase(httpRequest.getParameter("type")))) {
                        if (!(httpRequest.getServletPath().equals("/ecomAjax") && "sendMail".equalsIgnoreCase(httpRequest.getParameter("type")))) {
                            strParameterValues = httpRequest.getParameterValues(strParameter);
                            for (String strparam : strParameterValues) {
                                //System.out.println("[ECOM-WAR] SecurityFilter  strparamvalue ---->" + strparam);
                                //System.out.println("[ECOM-WAR] SecurityFilter ===strParameter is===>" + strParameter);
                                strparam = strparam.contains("\"") ? strparam.replace("\"", "\\\"") : strparam;
                                //System.out.println("[ECOM-WAR] SecurityFilter  strparam ---->" + strparam);
                                result = xsb.sanitize(strparam, request, response);
                                if (!result) {
                                    break;
                                }
                            }
                            strParamValue = httpRequest.getParameter(strParameter);
                            //System.out.println("[ECOM-WAR] SecurityFilter strParam--------> " + strParamValue);
                            strParamValue = strParamValue.contains("\"") ? strParamValue.replace("\"", "\\\"") : strParamValue;
                            //System.out.println("[ECOM-WAR] SecurityFilter strParam--------> " + strParamValue);
                            result = xsb.sanitize(strParamValue, request, response);
                        } else {
                            LogUtil.log("SecurityFilter Email Template ELSE");
                        }
                        if (!result) {
                            break;
                        }
                    }
                }
            }
            if (result) {
                if ("Y".equalsIgnoreCase(enable_Xss_Script_Blocker)) {
                    LogUtil.log("SecurityFilter XSS Script Blocker enabled, wrapping request");

                    chain.doFilter(new FilterdRequest(request), response);

                } else {
                    LogUtil.log("SecurityFilter XSS Script Blocker not enabled, proceeding without wrapping");
                    chain.doFilter(request, response);
                }
            }
        } catch (Exception e) {
            LogUtil.logException("SecurityFilter error", e);
            request.getRequestDispatcher("SessionExpired.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static class FilterdRequest extends HttpServletRequestWrapper {

        public FilterdRequest(ServletRequest request) {
            super((HttpServletRequest) request);
        }

        @Override
        public String getParameter(String paramName) {
            String paramValue = super.getParameter(paramName);
            if (paramValue != null) {
                paramValue = paramValue.replaceAll(";", " ");
            }
            return paramValue;
        }

        @Override
        public String[] getParameterValues(String paramName) {
            String paramValues[] = super.getParameterValues(paramName);
            if (paramValues != null) {
                for (int index = 0; index < paramValues.length; index++) {
                    if (paramValues[index] != null) {
                        paramValues[index] = paramValues[index].replaceAll(";", " ");
                    }
                }
            }
            return paramValues;
        }
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
