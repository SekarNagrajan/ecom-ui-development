package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.formbeans.eCommForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.common.Validation;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionErrors;
import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.vds.serviceVo.ScheduleCommonVO;

public class eCommAction extends org.apache.struts.action.Action {

    private static String Schdsearch = "";
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String Forward = "success";
        try {
            ServletContext context = servlet.getServletContext();
            String Display_LCT_CUTOFF = (String) context.getAttribute(GeneralConf.Display_LCT_CUTOFF);
            Hashtable portlist = (Hashtable) context.getAttribute("SESSION_PORTLIST");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ActionErrors err = new ActionErrors();
            eCommForm ecomObj = (eCommForm) form;
            String sname = ecomObj.getName();
            String fdate = ecomObj.getFrdate();
            String tdate = ecomObj.getTodate();
            sname = Validation.checkNull(sname);
            sname = MaskPortUtil.swapmaskporttoactual(sname);
            fdate = Validation.checkNull(fdate);
            tdate = Validation.checkNull(tdate);
            ScheduleCommonVO sr = new ScheduleCommonVO();
            String portname = sname + "-" + Validation.checkNull((String) portlist.get(sname.toUpperCase()));
             boolean isMaskPort = MaskPort.isMaskPorts(sname, context);
            if (isMaskPort && err != null && err.size() == 0) {
                request.setAttribute("atport", portname);
                request.setAttribute("fromdate", fdate);
                request.setAttribute("todate", tdate);
                fdate = DateConversion_New.convertDefaultDBFormat(fdate);
                tdate = DateConversion_New.convertDefaultDBFormat(tdate);
                Date todate = formatter.parse(tdate);
                todate = DateConversion_New.convertaddDays(todate, 1);
                tdate = formatter.format(todate);
                System.out.println("[ECOM-WAR] eCommAction  ==>>>fdate" + fdate+ " =>>>tdate" + tdate);
                String date = fdate + "," + tdate;
                List<ScheduleCommonVO> routlst = ecomCntrlObj.getByPorts(sname, date, Display_LCT_CUTOFF);
                // Mask schedule data if it's a mask port
                routlst = MaskPortUtil.maskScheduleDataIfMaskPort(sname, routlst);
                request.setAttribute("SCHD", routlst);
                Forward = "success";
            }
            Schdsearch = "SchdByPort";
            request.setAttribute("SCHDSEARCH", Schdsearch);
        } catch (Exception e) {
            Forward = "failure";
            Schdsearch = "SchdByPort";
            request.setAttribute("SCHDSEARCH", Schdsearch);
            request.setAttribute("Error_report", EcomConsts.WS_ERROR_MESSAGE);
            System.out.println("[ECOM-WAR] eCommAction  e =>>>" + e);
        }
        return mapping.findForward(Forward);
    }
}
