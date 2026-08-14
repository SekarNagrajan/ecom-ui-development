/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.Validation;
import net.ecom.formbeans.eCommForm;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.StringReader;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import net.ecom.common.GeneralConf;
import com.sun.jersey.api.client.ClientHandlerException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.UnmarshalException;
import net.ecom.common.EcomConsts;
import net.ecom.common.utils.DateConversion_New;
import java.util.Hashtable;
import java.util.List;
import net.ecom.vds.serviceVo.ScheduleCommonVO;

public class ecommSchdVesselAction extends org.apache.struts.action.Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String Forward = "success";
        String Schdsearch = "";
        try {
            ServletContext context = servlet.getServletContext();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Hashtable vessellist = (Hashtable) context.getAttribute("SESSION_VESSELLIST");
            String Display_LCT_CUTOFF = (String) context.getAttribute(GeneralConf.Display_LCT_CUTOFF);
            ActionErrors err = new ActionErrors();
            eCommForm ecomObj = (eCommForm) form;
            String sname = ecomObj.getName();
            String fdate = ecomObj.getFrdate();
            String tdate = ecomObj.getTodate();
            sname = Validation.checkNull(sname);
            fdate = Validation.checkNull(fdate);
            tdate = Validation.checkNull(tdate);
            String portname = sname + "-" + Validation.checkNull((String) vessellist.get(sname));
            request.setAttribute("vslatport", portname);
            request.setAttribute("vslfromdate", fdate);
            request.setAttribute("vsltodate", tdate);
            if (err != null && err.size() == 0) {
                fdate = DateConversion_New.convertDefaultDBFormat(fdate);
                tdate = DateConversion_New.convertDefaultDBFormat(tdate);
                Date todate = formatter.parse(tdate);
                todate = DateConversion_New.convertaddDays(todate, 1);
                tdate = formatter.format(todate);
                String date = fdate + "," + tdate;
                List<ScheduleCommonVO> routlst = ecomCntrlObj.getByVessel(sname, date, Display_LCT_CUTOFF);
                request.setAttribute("SCHD", routlst);
            }
            Schdsearch = "SchdByVessel";
            System.out.println("[ECOM-WAR] ecommSchdVesselAction FORWARD Action " + Forward);
            request.setAttribute("SCHDSEARCH", Schdsearch);
            return mapping.findForward(Forward);
        } catch (Exception e) {
            Forward = "failure";
            Schdsearch = "SchdByVessel";
            request.setAttribute("SCHDSEARCH", Schdsearch);
            request.setAttribute("Error_report", EcomConsts.WS_ERROR_MESSAGE);
            System.out.println("[ECOM-WAR] ecommSchdVesselAction ==================>>>" + e);
        }
        return mapping.findForward(Forward);
    }
}
