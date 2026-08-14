/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.controller;

import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gobinath.t
 */
public class AdminMenuAccess {

    public boolean validateAccess(HttpServletRequest request) {
        boolean access = true;
        ServletContext context = null;
        HttpSession session = null;
        boolean isAdminMenu = false;

        try {
            session = request.getSession(false);
            context = request.getServletContext();
            String logintype = (String) session.getAttribute("SESSION_LOGIN_TYPE");
            if ("/AdminAccess.do".equals(request.getServletPath())) {
                if ("specialprivi".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                } else if ("emailtemplate".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                } else if ("adminreset".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                } else if ("globalConfigAdmin".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                } else if ("fieldconfig".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                } else if ("svrcrestrict".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                } else if ("ImageUploadAdmin".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                } else if ("CustomerAdvisoryAdmin".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                }  else if ("cutoffconfig".equalsIgnoreCase(request.getParameter("type"))) {
                    isAdminMenu = true;
                }
            }
            if (isAdminMenu && (!"V".equalsIgnoreCase(logintype))) {
                access = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM-WAR] AdminMenuAccess validateAccess==> " + access);
        return access;
    }
}
