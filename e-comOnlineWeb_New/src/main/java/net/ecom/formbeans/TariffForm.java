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

/**
 *
 * @author arunprasath
 */
public class TariffForm extends org.apache.struts.action.ActionForm {

    private String isSubmit = "0";
    private String polname;
    private String podname;
    private String eqtype;
    private ArrayList results;
    private String tarrifCommodity;
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

    public Validation getVal() {
        return val;
    }

    public void setVal(Validation val) {
        this.val = val;
    }
    Validation val = new Validation();

    /**
     * @return the polname
     */
    public String getPolname() {
        return val.checkNull(polname);
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
        return val.checkNull(podname);
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
        return val.checkNull(eqtype);
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

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
        System.out.println("[ECOM-WAR] TariffForm ---validateForm->"+getIsSubmit());

        if (eqtype == null || eqtype.length() < 1) {

            errors.add("etype", new ActionMessage("error.eqtype.required"));

        }

        //}
        return errors;



    }

    /**
     * @return the isSubmit
     */
    public String getIsSubmit() {
        return val.checkNull(isSubmit);
    }

    /**
     * @param isSubmit the isSubmit to set
     */
    public void setIsSubmit(String isSubmit) {
        this.isSubmit = isSubmit;
    }

    /**
     * @return the tarrifCommodity
     */
    public String getTarrifCommodity() {
        return tarrifCommodity;
    }

    /**
     * @param tarrifCommodity the tarrifCommodity to set
     */
    public void setTarrifCommodity(String tarrifCommodity) {
        this.tarrifCommodity = tarrifCommodity;
    }
}
