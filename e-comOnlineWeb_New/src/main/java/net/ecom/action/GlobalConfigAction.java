/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.formbeans.GlobalConfigForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.GlobalConfigVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GlobalConfigAction extends org.apache.struts.action.Action {

    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        HttpSession session = request.getSession();
        String type = request.getParameter("Stype") != null ? request.getParameter("Stype") : "";
        GlobalConfigVO configVO = null;
        String result = null;
        String hsCodeArr[];
        String natCodeArr[];
        String CarriageContractArr[];
        String CarriageContractArr1[];
        ServletContext context = servlet.getServletContext();

        String adminmenu = (String) session.getAttribute("adminmenupage");
        try {
            configVO = new GlobalConfigVO();
            GlobalConfigForm configform = (GlobalConfigForm) form;
            System.out.println("[ECOM-WAR] GlobalConfigAction config form......." + configform.getKey());
            configVO.setKey(configform.getKey());
            configVO.setValue(configform.getValue());
            if (type.equalsIgnoreCase("edit")) {
                result = locobj.updateGlobalConfigDetails(configform.getKey(), configform.getValue());
                context.setAttribute(configform.getKey(), configform.getValue());
            } else {
                result = locobj.saveGlobalConfigDetails(configform.getKey(), configform.getValue());
                context.setAttribute(configform.getKey(), configform.getValue());
            }
            if (configform.getKey() != null && configform.getKey().equalsIgnoreCase(GeneralConf.hscodecountry)) {
                String getHSCodeCountry = configform.getValue();
                hsCodeArr = getHSCodeCountry.split("~~");
                Map<String, String> hsCodeCountryMap = new HashMap<String, String>();
                if (hsCodeArr != null && hsCodeArr.length > 0) {
                    for (int i = 0; i < hsCodeArr.length; i++) {
                        hsCodeCountryMap.put(Validation.checkNull(hsCodeArr[i]).trim(), Validation.checkNull(hsCodeArr[i]).trim());
                    }
                }
              
                    
                context.setAttribute(GeneralConf.hscodecountry, hsCodeCountryMap);
            }
            
            if (configform.getKey() != null && configform.getKey().equalsIgnoreCase(GeneralConf.natcodecountry)) {
                String getNATCodeCountry = configform.getValue();
                natCodeArr = getNATCodeCountry.split("~~");
                Map<String, String> natCodeCountryMap = new HashMap<String, String>();
                if (natCodeArr != null && natCodeArr.length > 0) {
                    for (int i = 0; i < natCodeArr.length; i++) {
                        natCodeCountryMap.put(Validation.checkNull(natCodeArr[i]).trim(), Validation.checkNull(natCodeArr[i]).trim());
                    }
                }
                context.setAttribute(GeneralConf.natcodecountry, natCodeCountryMap);
            }   
//            Map<String, String> carriageContractMap = new HashMap<>();
//            if (configform.getKey() != null && configform.getKey().equalsIgnoreCase(GeneralConf.CarriageContractBasedOnPorts)) {
//                String getCarriageContractVal = configform.getValue();
//                CarriageContractArr = getCarriageContractVal.split("~~");
//                if (CarriageContractArr != null && CarriageContractArr.length > 0) {
//                    for (int i = 0; i < CarriageContractArr.length; i++) {
//                        CarriageContractArr1 = null;
//                        CarriageContractArr1 = CarriageContractArr[i].split("=");
//                        carriageContractMap.put(Validation.checkNull(CarriageContractArr1[0]).trim(),Validation.checkNull(CarriageContractArr1[1]).trim());
//                    }
//                }
//                context.setAttribute(GeneralConf.CarriageContractBasedOnPorts, carriageContractMap);
//                System.out.println("[ECOM-WAR] GlobalConfigAction carriagecontractmap" + carriageContractMap);
//            }
            
            if (adminmenu != null && adminmenu.equalsIgnoreCase("loginmenupage")) {
                SUCCESS = "successadmin";
            } else {
                SUCCESS = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping.findForward(SUCCESS);
    }
}
