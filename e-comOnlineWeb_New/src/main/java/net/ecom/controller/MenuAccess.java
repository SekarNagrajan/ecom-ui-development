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

import net.ecom.common.LogUtil;

/**
 *
 * @author gobinath.t
 */
public class MenuAccess {

    public boolean validateAccess(HttpServletRequest request) {
        boolean access = true;
        ServletContext context = null;
        HttpSession session = null;
        Map menucategory = null;
        String currentmenu = "";

        try {
            session = request.getSession(false);
            context = request.getServletContext();
            String user = (String) session.getAttribute("sessionAdmin");
            if (!"Y".equalsIgnoreCase(user)) {
                String logintype = (String) session.getAttribute("SESSION_LOGIN_TYPE");
                ArrayList RegMenus = (ArrayList) session.getAttribute("RegMenus");
                //if ("U".equalsIgnoreCase(logintype)) {
                menucategory = (Map) context.getAttribute("userMenuCategory");
                if ("/tracking.do".equals(request.getServletPath())) {
                    currentmenu = "TRK";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "dashboard".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "DBD";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "usercreation".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "USC";
                } else if ("/ViewBookingAll.do".equals(request.getServletPath()) && "ViewBookingAll".equalsIgnoreCase(request.getParameter("BookingRequestType"))) {
                    currentmenu = "BKG";
                } else if ("/ViewBookingAll.do".equals(request.getServletPath()) && "ViewBookingAll".equalsIgnoreCase(request.getParameter("BookingRequestType"))) {
                    currentmenu = "BKG";
                } else if ("/ShippingInstruction.do".equals(request.getServletPath())) {
                    currentmenu = "SHI";
                } else if ("/arrival.do".equals(request.getServletPath())) {
                    currentmenu = "ARN";
                } else if ("/ViewBookingAll.do".equals(request.getServletPath()) && "oblprint".equalsIgnoreCase(request.getParameter("BookingRequestType"))) {
                    currentmenu = "BOL";
                } else if ("/ecomvgm.do".equals(request.getServletPath())) {
                    currentmenu = "VGM";
                } else if ("/croaction.do".equals(request.getServletPath())) {
                    currentmenu = "CRO";
                } else if ("/byport.do".equals(request.getServletPath())) {
                    currentmenu = "BPS";
                } else if ("/byvessel.do".equals(request.getServletPath())) {
                    currentmenu = "BVS";
                } else if ("/schedules.do".equals(request.getServletPath())) {
                    currentmenu = "BLS";
                } else if ("/carbonFromLogin.do".equals(request.getServletPath())) {
                    currentmenu = "BLS";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "tariff".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "TAR";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "surcharge".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "SUR";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "ratescontract".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "CON";
                } else if ("/deliveryorder.do".equals(request.getServletPath())) {
                    currentmenu = "DEO";
                } else if ("/newinvdet.do".equals(request.getServletPath())) {
                    currentmenu = "INV";
                } else if ("/newccrcuststmt.do".equals(request.getServletPath())) {
                    currentmenu = "CUS";
                } else if ("/custstmt.do".equals(request.getServletPath())) {
                    currentmenu = "CUS";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "rates".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "RTS";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "ratesquote".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "RAQ";
                } else if ("/Accesslogin.do".equals(request.getServletPath()) && "statsreport".equalsIgnoreCase(request.getParameter("type"))) {
                    currentmenu = "STR";
                } else if ("/ViewEbookingEntry.do".equals(request.getServletPath())) {
                    currentmenu = "BKG";
                } else if ("/NewBooking.do".equals(request.getServletPath())) {
                    currentmenu = "BKG";
                } else if ("/ebooking_crud.do".equals(request.getServletPath())) {
                    currentmenu = "BKG";
                } else if ("/editamendmentbooking.do".equals(request.getServletPath())) {
                    currentmenu = "BKG";
                } else if ("/ebookingsaveNew.do".equals(request.getServletPath())) {
                    currentmenu = "BKG";
                } else if ("/amendmentbookingsave.do".equals(request.getServletPath())) {
                    currentmenu = "BKG";
                } else if ("/EditSI.do".equals(request.getServletPath())) {
                    currentmenu = "SHI";
                } else if ("/SubmitSI.do".equals(request.getServletPath())) {
                    currentmenu = "SHI";
                } else if ("/BLEditAction.do".equals(request.getServletPath())) {
                    currentmenu = "BOL";
                } else if ("/OblPrintAction.do".equals(request.getServletPath())) {
                    currentmenu = "BOL";
                } else if ("/SubmitBL.do".equals(request.getServletPath())) {
                    currentmenu = "BOL";
                } else if ("/mcn.do".equals(request.getServletPath())) {
                    currentmenu = "MCN";
                } else if ("/BLbatchPrint.do".equals(request.getServletPath())) {
                    currentmenu = "BBP";
                } else if ("/notice.do".equals(request.getServletPath())) {
                    currentmenu = "NTE";
                } else if ("/customerPaymentHistory.do".equals(request.getServletPath())) {
                    currentmenu = "PAY";
                } 

                LogUtil.log("MenuAccess",
                        "currentmenu=> " + currentmenu + " menucategory=> " + menucategory + " RegMenus=> " + RegMenus);
                if (menucategory != null && "P".equalsIgnoreCase((String) menucategory.get(currentmenu))) {
                    if (RegMenus == null || (!RegMenus.contains(currentmenu))) {
                        access = false;
                    }
                }
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.log("MenuAccess", "validateAccess==> " + access);
        return access;
    }
}
