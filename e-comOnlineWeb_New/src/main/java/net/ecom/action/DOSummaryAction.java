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
import net.ecom.common.Validation;
import net.ecom.common.GeneralConf;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.phase2.service.vo.DOSummaryVO;

/**
 *
 * @author deepa.p
 */
//urlhardcode
public class DOSummaryAction extends org.apache.struts.action.Action {

    String success = "success";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        EcomPhase2BeanRemote ecomPhase2Obj = EJBLookUp.getPhase2BeanLookUp();
        try {
            ServletContext context = servlet.getServletContext();
            HttpSession session = request.getSession();
            List<DOSummaryVO> summaryList = null;
            DOSummaryVO summaryVo = null;
            session.setAttribute("NEWDESIGNPRODUCT", "deliveryorder");
            DateFormat dateFormat = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            Calendar cal = Calendar.getInstance();
            session.setAttribute("IS_ACTIVEPAGE", "deliveryorder");
            session.setAttribute("IS_MENU_LOCK", "NO");
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            //System.out.println("[ECOM-WAR] DOSummaryAction usercode................." + userCode);
            //dosummary?custCode=20100000011
            String fromDatet = null, toDatet = null;
            SimpleDateFormat source = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
            String fromDate = Validation.checkNull(request.getParameter("frdate"));
            String toDate = Validation.checkNull(request.getParameter("todate"));
            if (toDate.length() <= 0) {
                toDate = dateFormat.format(cal.getTime());
                //System.out.println("[ECOM-WAR] DOSummaryAction ..........todate.........." + toDate);
                toDatet = toDate;
                toDate = DateConversion_New.convertDefaultDBFormatwithoutTime(toDate);
                //System.out.println("[ECOM-WAR] DOSummaryAction ......todate.........." + toDate);
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

                summaryList = (List<DOSummaryVO>) ecomPhase2Obj.getDOSummary(userCode, fromDate, toDate);
                if (summaryList != null) {
                    int size = summaryList.size();
                    for (int i = 0; i < size; i++) {
                        summaryVo=(DOSummaryVO)summaryList.get(i);
                        summaryVo.setDelorddate(DateConversion_New.convertDefaultTimeFormat(summaryVo.getDelorddate()));
                        summaryVo.setArrdate(DateConversion_New.convertDefaultTimeFormat(summaryVo.getArrdate()));
                      summaryList.set(i, summaryVo);
                    }
                }
            
            //System.out.println("[ECOM-WAR] DOSummaryAction ====>" + obj.getDoListSummary());
            request.setAttribute("dosummarylist", summaryList);
            request.setAttribute("dofromdate", fromDatet);
            request.setAttribute("dotodate", toDatet);
            System.out.println("[ECOM-WAR] DOSummaryAction ....summary........."+(String)request.getAttribute("DOEDOCERROR"));
            request.setAttribute("DOEDOCERROR", (String)request.getAttribute("DOEDOCERROR"));

        } catch (Exception e) {
            success = "failure";
            request.setAttribute("Error_report", "error");
            System.out.println("[ECOM-WAR] DOSummaryAction ==================>>>" + e);
            e.printStackTrace();
        }
        return mapping.findForward("success");

    }
}
