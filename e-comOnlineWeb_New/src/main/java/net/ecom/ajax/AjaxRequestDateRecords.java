package net.ecom.ajax;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import net.ecom.common.Validation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpSession;
import net.ecom.vo.EbookingViewAll;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.utils.DateConversion_New;

public class AjaxRequestDateRecords extends org.apache.struts.action.Action {

    private String SUCCESS = "success";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ServletContext contexts = servlet.getServletContext();
        Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_PORTLIST");
        EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
        String type = request.getParameter("type");
        String BKNO = request.getParameter("bookno");
        HttpSession session = request.getSession();
        String from_date = request.getParameter("fromdate");
        String to_date = request.getParameter("todate");
        String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));

        String loginUser = (String) session.getAttribute("SESSION_LOGINNAME");
        String userType = (String) session.getAttribute("SESSION_USERTYPE");
        String userCode = (String) session.getAttribute("SESSION_USERCODE");
        System.out.println("[ECOM-WAR] AjaxRequestDateRecords login userid : " + loginUser);
        ArrayList reqList = new ArrayList();
        String BookNo = "";
        if (type.equalsIgnoreCase("search")) {
            from_date = DateConversion_New.convertDefaultDBFormatwithoutTime(from_date);
            to_date = DateConversion_New.convertDefaultDBFormatwithoutTime(to_date);
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            reqList = bookingObj.getBookingDetail(userCode, from_date, to_date, "D", custTypeConfig, v1DataEnableStatus);
            if (reqList != null && reqList.size() > 0) {
                int len = reqList.size();
                for (int i = 0; i < len; i++) {
                    EbookingViewAll eBooking = (EbookingViewAll) reqList.get(i);

                    if (portlist.containsKey(Validation.checkNull(eBooking.getOriginPortId()).toUpperCase())) {
                        eBooking.setOriginPortText(eBooking.getOriginPortId().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getOriginPortId()).toUpperCase()).toString()));

                    }
                    if (portlist.containsKey(Validation.checkNull(eBooking.getFinalPortId()).toUpperCase())) {
                        eBooking.setFinalPortText(eBooking.getFinalPortId().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getFinalPortId()).toUpperCase()).toString()));

                    }
                    if (portlist.containsKey(Validation.checkNull(eBooking.getLoadPortID()).toUpperCase())) {
                        eBooking.setLoadPortText(eBooking.getLoadPortID().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getLoadPortID()).toUpperCase()).toString()));
                    }
                    if (portlist.containsKey(Validation.checkNull(eBooking.getDischargePortId()).toUpperCase())) {
                        eBooking.setDischargePortText(eBooking.getDischargePortId().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getDischargePortId()).toUpperCase()).toString()));
                    }
                    if (eBooking.getBookingDate().length() > 0) {
                        eBooking.setBookingDate(DateConversion_New.convertDefaultTimeFormat(eBooking.getBookingDate()));
                    }
                    if (eBooking.getCrdate().length() > 0) {
                        eBooking.setCrdate(DateConversion_New.convertDefaultTimeFormat(eBooking.getCrdate()));
                    }
                    if (eBooking.getSubmitdate().length() > 0) {
                        eBooking.setSubmitdate(DateConversion_New.convertDefaultTimeFormat(eBooking.getSubmitdate()));
                    }
                    if (Validation.checkNull(eBooking.getPoletd()).length() > 0) {
                        eBooking.setPoletd(DateConversion_New.convertDefaultTimeFormat(eBooking.getPoletd()));
                    }
                    reqList.set(i, eBooking);
                    eBooking = null;
                }
            }
        } else if (type.equalsIgnoreCase("searchAll")) {
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            reqList = bookingObj.getBookingDetail(userCode, "", "", "A", custTypeConfig, v1DataEnableStatus);
            if (reqList != null && reqList.size() > 0) {
                int len = reqList.size();
                for (int i = 0; i < len; i++) {
                    EbookingViewAll eBooking = (EbookingViewAll) reqList.get(i);
                    if (portlist.containsKey(Validation.checkNull(eBooking.getOriginPortId()).toUpperCase())) {
                        eBooking.setOriginPortText(eBooking.getOriginPortId().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getOriginPortId()).toUpperCase()).toString()));
                    }
                    if (portlist.containsKey(Validation.checkNull(eBooking.getFinalPortId()).toUpperCase())) {
                        eBooking.setFinalPortText(eBooking.getFinalPortId().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getFinalPortId()).toUpperCase()).toString()));
                    }
                    if (portlist.containsKey(Validation.checkNull(eBooking.getLoadPortID()).toUpperCase())) {
                        eBooking.setLoadPortText(eBooking.getLoadPortID().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getLoadPortID()).toUpperCase()).toString()));
                    }
                    if (portlist.containsKey(Validation.checkNull(eBooking.getDischargePortId()).toUpperCase())) {
                        eBooking.setDischargePortText(eBooking.getDischargePortId().trim() + "-" + Validation.checkNull(portlist.get(Validation.checkNull(eBooking.getDischargePortId()).toUpperCase()).toString()));
                    }
                    reqList.set(i, eBooking);
                    eBooking = null;
                }
            }
            SUCCESS = "success";

        }
        request.setAttribute("RequestList", reqList);
        session.setAttribute("BookNo", BookNo);
        System.out.println("[ECOM-WAR] AjaxRequestDateRecords success value is : " + SUCCESS);
        return mapping.findForward(SUCCESS);
    }
}
