/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.FieldConfigVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class FieldConfigurationView extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";
    EComCntrlBeanRemote beanObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            System.out.println("[ECOM-WAR] FieldConfigurationView  >>entered in view action");
            List<FieldConfigVO> fieldConfigList=beanObj.getFieldConfigDetailsList();
            request.setAttribute("FIELDCONFIGLIST", fieldConfigList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
