/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.RemoveDuplicateValues;
import net.ecom.common.UserTimeZoneConversion;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.OBLPrintVo;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author prakash.mani
 */
public class MCNAction extends org.apache.struts.action.Action {

    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBLBeanRemote ecombean = EJBLookUp.getBLBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomV1BookingBeanRemote v1BkgObj = EJBLookUp.getV1BKGBeanLookUp();
    Map stateMap = null, stateMapCountry = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String mapping_string = "mcnprint";
        try {
            HttpSession session = request.getSession();
            ServletContext contexts = servlet.getServletContext();
            String isPasswordExpired = Validation.checkNull((String) session.getAttribute("isPasswordExpired"));
            if ("Y".equalsIgnoreCase(isPasswordExpired)) {
                response.sendRedirect("Accesslogin.do?type=reset");
            }
            saveToken(request);
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE")); 
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            session.setAttribute("IS_ACTIVEPAGE", "Booking");
            String toEnableTimeZone = (String) contexts.getAttribute(GeneralConf.EnableTimeZone); 

            String preferredTimeZone = Validation.checkNull((String) session.getAttribute("USERPREFERREDTIMEZONE"));
            Map<String, String> versionUTCMap = (Map<String, String>) contexts.getAttribute(GeneralConf.versionUTCValue);
            System.out.println("[ECOM-WAR] MCNAction    versionUTCMapValue=====>" + versionUTCMap + 
                    " ENABLETIMEZONEVAlue=====>" + toEnableTimeZone + 
                    " user code in session--->" + userCode + 
                    " preferredtimezoneValue=====>" + preferredTimeZone);
            UserTimeZoneConversion obj = new UserTimeZoneConversion();

            String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
            String strMenu = "YES";
            if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                strMenu = "NO";
            } else {
                session.setAttribute("systemid", session.getId());
            }
            session.setAttribute("IS_MENU_LOCK", strMenu);
            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
            List<OBLPrintVo> mcnview = null;
            try {
                mcnview = ecombean.getMCNSummary(userCode, custTypeConfig, v1DataEnableStatus);
                mcnview.forEach(vo -> {
                    try {
                        if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                            if (vo.getCrdate().length() > 0) {
                                String date1 = vo.getCrdate();
                                Date resulttt = null;
                                SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());

                                resulttt = targettt.parse(date1);

                                System.out.println("[ECOM-WAR] MCNAction    StringtoDate========>" + resulttt);
                                String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                                System.out.println("[ECOM-WAR] MCNAction    userTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                                System.out.println("[ECOM-WAR] MCNAction    BLcreated date before conversion" + vo.getCrdate());
                                if (userTimeZoneConverted.length() > 0) {
                                    vo.setCrdate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                                }
                                System.out.println("[ECOM-WAR] MCNAction    blcreated date after conversion" + vo.getCrdate());

                                if ("C".equalsIgnoreCase(vo.getStatus()) || "I".equalsIgnoreCase(vo.getStatus())) {
                                    String date11 = vo.getConfirmedDate();
                                    Date resultt = null;
                                    SimpleDateFormat targett = new SimpleDateFormat(GeneralConf.getStrDbFormat());

                                    resultt = targett.parse(date11);

                                    System.out.println("[ECOM-WAR] MCNAction    StringtoDate========>" + resultt);
                                    String userTimeZoneConvertedbl = obj.getUserDateFormat(resultt, preferredTimeZone, versionUTCMap);
                                    System.out.println("[ECOM-WAR] MCNAction    userTimeZoneConvertedfromejb" + userTimeZoneConvertedbl);

//
                                    System.out.println("[ECOM-WAR] MCNAction    BLconfirmed date before conversion" + vo.getCrdate());
                                    if (userTimeZoneConvertedbl.length() > 0) {
                                        vo.setConfirmedDate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConvertedbl));
                                    }

                                    System.out.println("[ECOM-WAR] MCNAction    blconfirmed date after conversion" + vo.getConfirmedDate());

                                } else {
                                    vo.setConfirmedDate("");
                                    // vo.setCrdate("");
                                }
                            }
                        } else {

                            vo.setCrdate(Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(vo.getCrdate())));

                            if ("C".equalsIgnoreCase(vo.getStatus()) || "I".equalsIgnoreCase(vo.getStatus())) {
                                vo.setConfirmedDate(Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(vo.getConfirmedDate())));

                            } else {
                                vo.setConfirmedDate("");
                                // vo.setCrdate("");
                            }

                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(eBookingView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NamingException ex) {
                        Logger.getLogger(eBookingView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            session.setAttribute("mcnprintView", mcnview);
            mapping_string = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(mapping_string);

    }
}
