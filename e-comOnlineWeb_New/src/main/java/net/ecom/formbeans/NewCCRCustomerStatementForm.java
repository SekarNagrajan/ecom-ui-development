/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author gobinath.t
 */
public class NewCCRCustomerStatementForm extends org.apache.struts.action.ActionForm {
    
    private String ondate;

    public String getOndate() {
        return ondate;
    }

    public void setOndate(String ondate) {
        this.ondate = ondate;
    }
    
   

    public NewCCRCustomerStatementForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
  
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
  
        return errors;
    }
}
