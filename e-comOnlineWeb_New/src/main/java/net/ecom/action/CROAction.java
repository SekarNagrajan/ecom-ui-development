/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.CROSummaryVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class CROAction extends org.apache.struts.action.Action {

    private static String SUCCESS = "success";
    EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext context = servlet.getServletContext();
        HttpSession session = request.getSession();
        Hashtable portlist = (Hashtable) context.getAttribute("SESSION_PORTLIST");

        List<CROSummaryVO> croList = null;
        String type = null, bookNo = null, releaseNo = null, fromDate = null, toDate = null, fromDatet = null, toDatet = null;
        // System.out.println("[ECOM-WAR] CROAction    this is container release order.......");
        try {
//             System.out.println("[ECOM-WAR] CROAction    >>>entered in cro>>");
            session.setAttribute("NEWDESIGNPRODUCT", "cro");
            session.setAttribute("IS_ACTIVEPAGE", "cropage");
            session.setAttribute("IS_MENU_LOCK", "NO");
            DateFormat dateFormat = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            // System.out.println("[ECOM-WAR] CROAction    ............dateformate......" + GeneralConf.getStrConfDefDate());
            Calendar cal = null;
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            type = Validation.checkNull(request.getParameter("type"));
            bookNo = Validation.checkNull(request.getParameter("bookno"));
            releaseNo = Validation.checkNull(request.getParameter("releaseno"));
            fromDate = Validation.checkNull(request.getParameter("frdate"));
            toDate = Validation.checkNull(request.getParameter("todate"));
            fromDatet = Validation.checkNull(request.getParameter("frdate"));
            toDatet = Validation.checkNull(request.getParameter("todate"));

            if (toDate.length() <= 0) {
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 7);
                toDate = dateFormat.format(cal.getTime());
                toDatet = toDate;
                toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
            } else if (fromDate.length() > 0) {
                fromDatet = fromDate;
                fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            }
            if (fromDate.length() <= 0) {
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);
                fromDate = dateFormat.format(cal.getTime());
                fromDatet = fromDate;
                fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            } else if (toDate.length() > 0) {
                toDatet = toDate;
                toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
            }
              croList = ecomPhase2Obj.getCROSummaryDetails(userCode, fromDate, toDate, bookNo, releaseNo);

              if (croList != null && croList.size() > 0) {
                    int len = croList.size();
                    for (int i = 0; i < len; i++) {
                        CROSummaryVO crovo = (CROSummaryVO) croList.get(i);
                         if (portlist.containsKey(Validation.checkNull(crovo.getPol()).toUpperCase())) {
                         crovo.setLoadPortText(crovo.getPol() + "-" + Validation.checkNull((String) portlist.get(crovo.getPol().toUpperCase())));
                        }
                        if (portlist.containsKey(Validation.checkNull(crovo.getPod()).toUpperCase())) {
                           crovo.setDischargePortText(crovo.getPod() + "-" + Validation.checkNull((String) portlist.get(crovo.getPod().toUpperCase())));
                        }
                        
                        if (portlist.containsKey(Validation.checkNull(crovo.getOrigin()).toUpperCase())) {
                         crovo.setOriginPortText(crovo.getOrigin() + "-" + Validation.checkNull((String) portlist.get(crovo.getOrigin().toUpperCase())));
                        }
                        if (portlist.containsKey(Validation.checkNull(crovo.getDelivery()).toUpperCase())) {
                           crovo.setDeliveryPortText(crovo.getDelivery() + "-" + Validation.checkNull((String) portlist.get(crovo.getDelivery().toUpperCase())));
                        }
                        

                        croList.set(i, crovo);
                    }
            }
            
            request.setAttribute("crosummarylist", croList);
            request.setAttribute("crofromdate", fromDatet);
            request.setAttribute("crotodate", toDatet);
            request.setAttribute("crobookno", bookNo);
            request.setAttribute("croreleaseno", releaseNo);
            // System.out.println("[ECOM-WAR] CROAction    ............" + releaseNo + "~~~" + (String) request.getAttribute("croreleaseno"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
