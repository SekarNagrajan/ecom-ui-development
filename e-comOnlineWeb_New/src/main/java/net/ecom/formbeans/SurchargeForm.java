/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;

public class SurchargeForm extends org.apache.struts.action.ActionForm {

    private String isSubmit = "0";
    private String polname;
    private String podname;
    private String eqtype;
    private ArrayList results;
    private String Search="";
    private String fromDate;
    private String toDate;
   

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    

    /**
     * @return
     */
    public SurchargeForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the polname
     */
    public String getPolname() {
        return Validation.checkNull(polname);
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
        return Validation.checkNull(podname);
    }

    /**
     * @param podname the podname to set
     */
    public void setPodname(String podname) {
        this.podname = podname;
    }

    /**
     * @return the eqtype
     */
    public String getEqtype() {
        return Validation.checkNull(eqtype);
    }

    /**
     * @param eqtype the eqtype to set
     */
    public void setEqtype(String eqtype) {
        this.eqtype = eqtype;
    }

    /**
     * @return the results
     */
    public ArrayList getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(ArrayList results) {
        this.results = results;
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();

            if (getEqtype() == null || getEqtype().length() < 1) {
                errors.add("etype", new ActionMessage("error.eqtype.required"));
            }
        return errors;
    }

    /**
     * @return the isSubmit
     */
    public String getIsSubmit() {
        return isSubmit;
    }

    /**
     * @param isSubmit the isSubmit to set
     */
    public void setIsSubmit(String isSubmit) {
        this.isSubmit = isSubmit;
    }

    /**
     * @return the Search
     */
    public String getSearch() {
        return Search;
    }

    /**
     * @param Search the Search to set
     */
    public void setSearch(String Search) {
        this.Search = Search;
    }
}
