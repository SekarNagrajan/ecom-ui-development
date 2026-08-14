/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.Validation;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import net.ecom.lookup.EJBLookUp;

public class eCommScheduleBlwLocationForm extends org.apache.struts.action.ActionForm {

    private String name;
    private int number;
    private String polname;
    private String podname;
    private String deptDate;
    private String arrDate;

    Validation validate = new Validation();

    /**
     * @return
     */
    public String getName() {
        return Validation.checkNull(name);
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param i
     */
    public void setNumber(int i) {
        number = i;
    }

    /**
     *
     */
    public eCommScheduleBlwLocationForm() {
        super();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        request.setAttribute("pagetype", "atbetweenlocation");
        String date = validate.checkNull((String) request.getParameter("date"));
        if (!(getPolname().trim().length() > 0)) {
            errors.add("er_polname", new ActionMessage("error.schd.polname"));
        }
        if (!(getPodname().trim().length() > 0)) {
            errors.add("er_podname", new ActionMessage("error.schd.podname"));
        }
        if (date != null && date.trim().length() > 0) {
            if ("arr".equalsIgnoreCase(date)) {
                if (!(getArrDate().trim().length() > 0)) {
                    errors.add("er_arrdate", new ActionMessage("error.schd.arrdate"));
                }

            } else if ("dept".equalsIgnoreCase(date)) {
                if (!(getDeptDate().trim().length() > 0)) {
                    errors.add("er_deptdate", new ActionMessage("error.schd.deptdate"));
                }
            }
        } else {
            errors.add("er_date", new ActionMessage("error.schd.date"));
        }
        //System.out.println("[ECOM-WAR] eCommScheduleBlwLocationForm Error is  " + errors);
        return errors;
    }

    /**
     * @return the polname
     */
    public String getPolname() {

        return validate.checkNull(polname);
    }

    /**
     * @param polname the polname to set
     */
    public void setPolname(String polname) {
        this.polname = polname;
    }

    /**
     * @return the podname
     */
    public String getPodname() {

        return validate.checkNull(podname);
    }

    /**
     * @param podname the podname to set
     */
    public void setPodname(String podname) {
        this.podname = podname;
    }

    /**
     * @return the deptDate
     */
    public String getDeptDate() {

        return validate.checkNull(deptDate);
    }

    /**
     * @param deptDate the deptDate to set
     */
    public void setDeptDate(String deptDate) {
        this.deptDate = deptDate;
    }

    /**
     * @return the arrDate
     */
    public String getArrDate() {

        return validate.checkNull(arrDate);
    }

    /**
     * @param arrDate the arrDate to set
     */
    public void setArrDate(String arrDate) {
        this.arrDate = arrDate;
    }

  
}
