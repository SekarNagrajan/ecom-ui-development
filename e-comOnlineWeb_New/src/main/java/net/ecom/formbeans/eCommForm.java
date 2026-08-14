/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.Validation;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import net.ecom.lookup.EJBLookUp;

public class eCommForm extends org.apache.struts.action.ActionForm {

    private String name = "";
    private int number;
    private String vesselname = "";
    private String frdate = "";
    private String todate = "";
    private String atport = "";

    public String getName(){

        return Validation.checkNull(name);
    }

    public void setName(String string) {
        name = string;
    }


    public int getNumber() {
        return number;

    }

    public void setNumber(int i) {
        number = i;
    }

    public eCommForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        String method = Validation.checkNull(request.getParameter("type"));
       try{
       
       }catch(Exception e){
           e.printStackTrace();
           System.out.println("[ECOM-WAR] eCommForm Error in validate() in eCommForm-->"+e);
       }
        return errors;
    }

    public String getTodate() {
        return Validation.checkNull(todate);
    }


    public void setTodate(String todate) {
        this.todate = todate;
    }


    public String getAtport(){
        return Validation.checkNull(atport);
    }


    public void setAtport(String atport) {
        this.atport = atport;
    }


    public String getFrdate() {
        return frdate;
    }

    public void setFrdate(String frdate) {
        this.frdate = frdate;
    }

    public String getVesselname() {
        return vesselname;
    }

    public void setVesselname(String vesselname) {
        this.vesselname = vesselname;
    }
}
