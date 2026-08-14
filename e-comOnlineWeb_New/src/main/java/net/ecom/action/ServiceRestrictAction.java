/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.formbeans.ServiceRestrictionForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ServiceRestrictionVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class ServiceRestrictAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";
    EComCntrlBeanRemote beanObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String pla = null, pld = null;
        ServletContext context = servlet.getServletContext();
        try {
            HttpSession session = request.getSession(false);
            ServiceRestrictionForm svrcForm = (ServiceRestrictionForm) form;
            ServiceRestrictionVO vo = new ServiceRestrictionVO();
            session.setAttribute("IS_ACTIVEPAGE", "srvcrestriction");
            session.setAttribute("NEWDESIGNPRODUCT", "srvcrestriction");
            vo.setSaveType(svrcForm.getSaveType());
            pla = svrcForm.getPla();
            pld = svrcForm.getPld();
            if (pla != null) {
                pla = pla.split("-")[0].trim();
            }
            if (pld != null) {
                pld = pld.split("-")[0].trim();
            }
            System.out.println("[ECOM-WAR] ServiceRestrictAction >>pla>>" + pla + ">>svrcForm.getId()>>" + svrcForm.getId());
            vo.setPla(pla);
            vo.setPld(pld);
            vo.setService(svrcForm.getService());
            vo.setStatus(svrcForm.getStatus());
            vo.setId(svrcForm.getId());
            String result = beanObj.saveSvrcRestriction(vo);
            System.out.println("[ECOM-WAR] ServiceRestrictAction >>result>> updateion" + result);
            Map<String, List<String>> resSrvcMap = beanObj.getSvrcRestrictCacheList();
            //System.out.println("[ECOM-WAR] ServiceRestrictAction >>resSrvcMap>>" + resSrvcMap);
            context.setAttribute("CACHERESTRICTEDSERVICES", resSrvcMap);
            if ("success".equalsIgnoreCase(result)) {

                svrcForm.setPla("");
                svrcForm.setPld("");
                svrcForm.setService("");
                svrcForm.setStatus("A");
                svrcForm.setId("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
