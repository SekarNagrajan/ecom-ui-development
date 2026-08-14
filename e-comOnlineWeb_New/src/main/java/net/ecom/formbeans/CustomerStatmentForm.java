/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author deepa.p
 */
public class CustomerStatmentForm extends org.apache.struts.action.ActionForm {

    private String accType = null;
    private String invType = null;
    private String payType = null;
    private String outType = null;
    private String accFromDate = null;
    private String accToDate = null;
    private String invFromDate = null;
    private String invToDate = null;
    private String payFromDate = null;
    private String payToDate = null;
    private String outFromDate = null;
    private String outToDate = null;
    private String accOption = null;
    private String invOption = null;
    private String payOption = null;
    private String outOption = null;

    public String getAccFromDate() {
        return accFromDate;
    }

    public void setAccFromDate(String accFromDate) {
        this.accFromDate = accFromDate;
    }

    public String getAccOption() {
        return accOption;
    }

    public void setAccOption(String accOption) {
        this.accOption = accOption;
    }

    public String getAccToDate() {
        return accToDate;
    }

    public void setAccToDate(String accToDate) {
        this.accToDate = accToDate;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getInvFromDate() {
        return invFromDate;
    }

    public void setInvFromDate(String invFromDate) {
        this.invFromDate = invFromDate;
    }

    public String getInvOption() {
        return invOption;
    }

    public void setInvOption(String invOption) {
        this.invOption = invOption;
    }

    public String getInvToDate() {
        return invToDate;
    }

    public void setInvToDate(String invToDate) {
        this.invToDate = invToDate;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public String getOutFromDate() {
        return outFromDate;
    }

    public void setOutFromDate(String outFromDate) {
        this.outFromDate = outFromDate;
    }

    public String getOutOption() {
        return outOption;
    }

    public void setOutOption(String outOption) {
        this.outOption = outOption;
    }

    public String getOutToDate() {
        return outToDate;
    }

    public void setOutToDate(String outToDate) {
        this.outToDate = outToDate;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getPayFromDate() {
        return payFromDate;
    }

    public void setPayFromDate(String payFromDate) {
        this.payFromDate = payFromDate;
    }

    public String getPayOption() {
        return payOption;
    }

    public void setPayOption(String payOption) {
        this.payOption = payOption;
    }

    public String getPayToDate() {
        return payToDate;
    }

    public void setPayToDate(String payToDate) {
        this.payToDate = payToDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public CustomerStatmentForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        return errors;
    }
}
