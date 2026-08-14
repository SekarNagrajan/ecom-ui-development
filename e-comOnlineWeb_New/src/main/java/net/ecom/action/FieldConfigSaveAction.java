/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.Validation;
import net.ecom.formbeans.FieldConfigurationForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.FieldConfigVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author deepa.p
 */
public class FieldConfigSaveAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";
    EComCntrlBeanRemote beanObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            String searchCode = null,shipmentType=null;
            FieldConfigurationForm fieldForm = (FieldConfigurationForm) form;
            FieldConfigVO configVO = new FieldConfigVO();
            shipmentType=Validation.checkNull(fieldForm.getShipmentType()).trim();
            if("Export".equalsIgnoreCase(shipmentType)){
                shipmentType="E";
            }else{
                shipmentType="I";
            }
            searchCode = Validation.checkNull(fieldForm.getSearchValue());
            System.out.println("[ECOM-WAR] FieldConfigSaveAction >>searchCode>>"+searchCode);
            searchCode = searchCode.split("-")[0].trim();
            configVO.setFieldCode(fieldForm.getFieldName());
            configVO.setSearchType(fieldForm.getSearchType());
            configVO.setSearchValue(searchCode);
            configVO.setShipmentType(shipmentType);
            configVO.setInputType(fieldForm.getInputType());
            configVO.setStatus(fieldForm.getStatus());
            configVO.setSaveType(fieldForm.getSaveType());
            configVO.setId(fieldForm.getConfigid());
            int i = beanObj.saveFieldConfigurationDet(configVO);
            if (i > 0) {
                fieldForm.setFieldName("");
                fieldForm.setSearchType("");
                fieldForm.setShipmentType("");
                fieldForm.setInputType("");
                fieldForm.setStatus("");
                fieldForm.setSaveType("");
                fieldForm.setConfigid("");
                fieldForm.setSearchValue("");
            }
            System.out.println("[ECOM-WAR] FieldConfigSaveAction  >>fieldForm>>" + fieldForm.getFieldCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
