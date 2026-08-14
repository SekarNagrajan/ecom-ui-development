/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.ARNSummaryVO;

/**
 *
 * @author Norashikin
 */
public class ArrivalNoticeActions extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    //urlhardcode
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();
        try {
            String fromDate = null, toDate = null;
            String fromDatet = null, toDatet = null;
            ServletContext context = servlet.getServletContext();
            HttpSession session = request.getSession();
            DateFormat dateFormat = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            Calendar cal = Calendar.getInstance();
            session.setAttribute("IS_ACTIVEPAGE", "Arrival Notice");
            session.setAttribute("NEWDESIGNPRODUCT", "arrivalnotice");
            session.setAttribute("IS_MENU_LOCK", "NO");
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            fromDate = Validation.checkNull(request.getParameter("frdate"));
            toDate = Validation.checkNull(request.getParameter("todate"));
            fromDatet = Validation.checkNull(request.getParameter("frdate"));
            toDatet = Validation.checkNull(request.getParameter("todate"));
            if (toDate.length() <= 0) {
                toDate = dateFormat.format(cal.getTime());
                //System.out.println("[ECOM-WAR]ArrivalNoticeActions ...todate.........." + toDate);
                toDatet = toDate;
                toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
                //System.out.println("[ECOM-WAR]ArrivalNoticeActions ......todate.........." + toDate);
            } else {
                fromDatet = fromDate;
                fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            }
            if (fromDate.length() <= 0) {
                cal.add(Calendar.DATE, -60);
                fromDate = dateFormat.format(cal.getTime());
                fromDatet = fromDate;
                fromDate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromDate);
            } else if (toDate.length() > 0) {
                toDatet = toDate;
                toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
            }

            List<ARNSummaryVO> obj = ecomPhase2Obj.getARNSummary(userCode, fromDate, toDate);
            System.out.println("[ECOM-WAR]ArrivalNoticeActions  ==arrival notice==>" + obj);
            request.setAttribute("arnsummarylist", obj);
            request.setAttribute("arnfromdate", fromDatet);
            request.setAttribute("arntodate", toDatet);
        
        } catch (Exception e) {
            request.setAttribute("Error_report", "error");
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
