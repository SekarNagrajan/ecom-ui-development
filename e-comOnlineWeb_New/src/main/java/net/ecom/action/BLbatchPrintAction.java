/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.UserTimeZoneConversion;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.OBLPrintVo;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author preethi.elumalai
 */
public class BLbatchPrintAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    EcomBLBeanRemote ecombean = EJBLookUp.getBLBeanLookUp();

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List<OBLPrintVo> oblview = null;
        try {
            HttpSession session = request.getSession();
            ServletContext contexts = servlet.getServletContext();
            String toEnableTimeZone = (String) contexts.getAttribute(GeneralConf.EnableTimeZone);
//             System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView ENABLETIMEZONEVAlue " + toEnableTimeZone);
            String preferredTimeZone = Validation.checkNull((String) session.getAttribute("USERPREFERREDTIMEZONE"));
//             System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView preferredtimezoneValue" + preferredTimeZone);
            Map<String, String> versionUTCMap = (Map<String, String>) contexts.getAttribute(GeneralConf.versionUTCValue);
//             System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView versionUTCMapValue" + versionUTCMap);
             System.out.println("[ECOM-WAR] BLbatchPrintAction  versionUTCMapValue: " + versionUTCMap + ", preferredTimeZone: " + preferredTimeZone + ", ENABLETIMEZONEValue: " + toEnableTimeZone);

            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            oblview = ecombean.getBLSummary(userCode, custTypeConfig, v1DataEnableStatus, "Y");
            UserTimeZoneConversion obj = new UserTimeZoneConversion();
            //TIMEZONE BL
            //BL CREATEDATE

            oblview.forEach(vo -> {
                try {
                    if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                        if (vo.getCrdate().length() > 0) {
                            String date1 = vo.getCrdate();
                            Date resulttt = null;
                            SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());

                            resulttt = targettt.parse(date1);

//                             System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView StringtoDate" + resulttt);
                            String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
//                             System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView userTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
//                             System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView BLcreated date before conversion" + vo.getCrdate());
                            if (userTimeZoneConverted.length() > 0) {
                                vo.setCrdate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                            }
//                             System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView blcreated date after conversion" + vo.getCrdate());
                             System.out.println("[ECOM-WAR] BLbatchPrintAction  BL created date after conversion: " + vo.getCrdate() + ", BL created date before conversion: " + vo.getCrdate() + ", userTimeZoneConverted: " + userTimeZoneConverted + ", StringToDate: " + resulttt);

                            if ("C".equalsIgnoreCase(vo.getStatus()) || "I".equalsIgnoreCase(vo.getStatus())) {
                                String date11 = vo.getConfirmedDate();
                                Date resultt = null;
                                SimpleDateFormat targett = new SimpleDateFormat(GeneralConf.getStrDbFormat());

                                resultt = targett.parse(date11);

//                                 System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView StringtoDate" + resultt);
                                String userTimeZoneConvertedbl = obj.getUserDateFormat(resultt, preferredTimeZone, versionUTCMap);
//                                 System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView userTimeZoneConvertedfromejb" + userTimeZoneConvertedbl);

//
//                                 System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView BLconfirmed date before conversion" + vo.getCrdate());
                                if (userTimeZoneConvertedbl.length() > 0) {
                                    vo.setConfirmedDate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConvertedbl));
                                }

//                                 System.out.println("[ECOM-WAR] BLbatchPrintAction  [ECOM-WAR] eBookingView blconfirmed date after conversion" + vo.getConfirmedDate());
 System.out.println("[ECOM-WAR] BLbatchPrintAction  BL confirmed date after conversion: " + vo.getConfirmedDate() + ", BL confirmed date before conversion: " + vo.getCrdate() + ", userTimeZoneConverted: " + userTimeZoneConvertedbl + ", StringToDate: " + resultt);

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
            session.setAttribute("oblbatchprintView", oblview);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         System.out.println("[ECOM-WAR] BLbatchPrintAction  SUCCESS=>" + SUCCESS);
        return mapping.findForward(SUCCESS);
    }
}
