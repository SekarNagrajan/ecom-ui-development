package net.ecom.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class UserAudit extends org.apache.struts.action.Action {

    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "useraudit";
        try {
            List UserlogList = null;
            String ColumnName = "";
            String searchType = request.getParameter("serType");
            String datFormat = request.getParameter("datFormat");
            searchType = Validation.checkNull(searchType);
            String value = request.getParameter("value");
            value = Validation.checkNull(value);
            if (searchType.equalsIgnoreCase("username")) {
                ColumnName = "username";
            } else if (searchType.equalsIgnoreCase("Logdate")) {
                ColumnName = "Logdate";
            } else if (searchType.equalsIgnoreCase("referenceno")) {
                ColumnName = "referenceno";
            }
          //  UserlogList = locobj.getUserLog(ColumnName, value, datFormat);
            request.setAttribute("UserlogList", UserlogList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }
}
