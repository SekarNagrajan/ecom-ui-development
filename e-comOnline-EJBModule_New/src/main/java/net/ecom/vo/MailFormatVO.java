/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author udhaya
 */
public class MailFormatVO implements Serializable{
    private String strMailContent="";
    private String strRegard="";
    private String strFromAddr="";
    private String strSubject="";
    private String activityCode="";

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

   Validation validate=new Validation();

    /**
     * @return the strMailContent
     */
    public String getStrMailContent() {
        return validate.checkNull(strMailContent);
    }

    /**
     * @param strMailContent the strMailContent to set
     */
    public void setStrMailContent(String strMailContent) {
        this.strMailContent = strMailContent;
    }

    /**
     * @return the strRegard
     */
    public String getStrRegard() {
        return validate.checkNull(strRegard);
    }

    /**
     * @param strRegard the strRegard to set
     */
    public void setStrRegard(String strRegard) {
        this.strRegard = strRegard;
    }

    /**
     * @return the strFromAddr
     */
    public String getStrFromAddr() {
        return validate.checkNull(strFromAddr);
    }

    /**
     * @param strFromAddr the strFromAddr to set
     */
    public void setStrFromAddr(String strFromAddr) {
        this.strFromAddr = strFromAddr;
    }
    /**
     * @return the strSubject
     */
    public String getStrSubject() {
        return validate.checkNull((strSubject));
    }

    /**
     * @param strSubject the strSubject to set
     */
    public void setStrSubject(String strSubject) {
        this.strSubject = strSubject;
    }
}
