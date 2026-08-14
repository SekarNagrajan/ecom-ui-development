package net.ecom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.formbeans.CustomerAdvisoryForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.CustomerAdvisoryVo;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author gobinath.t
 */
public class CustomerAdvisoryAction extends org.apache.struts.action.Action {

    EcomAdminCntrlBeanRemote adminBeanObj=EJBLookUp.getAdminBeanLookUp();
    private static final String FORWARD = "SaveSuccess";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String type = request.getParameter("Stype");
        CustomerAdvisoryVo savevo = null;
        String result = null;
        CustomerAdvisoryForm custadvisform = null;
        try {
            savevo = new CustomerAdvisoryVo();
            custadvisform = (CustomerAdvisoryForm) form;

            int keyid = custadvisform.getKeyid();
            String advisoryname = custadvisform.getAdvisoryname();
            String description = custadvisform.getDescription();

            savevo.setAdvisoryname(advisoryname);
            savevo.setDescription(description);
            savevo.setKeyid(keyid);

            if ("edit".equalsIgnoreCase(type)) {
                result = adminBeanObj.updateCustomerAdvisoryDetails(savevo);
            } else {
                result = adminBeanObj.saveCustomerAdvisoryDetails(savevo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(FORWARD);
    }
}
