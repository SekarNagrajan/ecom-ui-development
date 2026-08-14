/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.AppTierCntrlBean.EcomNoticeBean;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vds.serviceVo.NoticeVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author preethi.elumalai
 */
public class NoticeAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
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
    EcomNoticeBean locobj = EJBLookUp.getNoticeBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "failed";

        ArrayList<NoticeVO> noticeview = null;
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            HttpSession session = request.getSession();
            noticeview = (ArrayList<NoticeVO>) locobj.getNoticeSummaryList(year);
            noticeview.forEach(vo -> {
//                System.out.println("[ECOM-WAR] NoticeAction    vo==>"+vo.getTitle()+" // "+vo.getFilename()+ " // "+vo.getCrdate());
                vo.setCrdate(Validation.checkNull(DateConversion_New.convertDefaultTimeFormat(vo.getCrdate())));
            });
            System.out.println("[ECOM-WAR] NoticeAction    noticeview===>" + noticeview.size());
            session.setAttribute("noticeView", noticeview);
        } catch (Exception e) {

            System.out.println("[ECOM-WAR] NoticeAction    [ECOM-WAR] Exception in NoticeAction execute - " + e);
            e.printStackTrace();
        }
        SUCCESS = "success";
        return mapping.findForward(SUCCESS);
    }
}
