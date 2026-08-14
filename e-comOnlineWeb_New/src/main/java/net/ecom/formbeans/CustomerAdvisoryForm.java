package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author gobinath.t
 */
public class CustomerAdvisoryForm extends org.apache.struts.action.ActionForm {

    private int keyid;
    private String advisoryname;
    private String description;

    public String getAdvisoryname() {
        return advisoryname;
    }

    public void setAdvisoryname(String advisoryname) {
        this.advisoryname = advisoryname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    
}
