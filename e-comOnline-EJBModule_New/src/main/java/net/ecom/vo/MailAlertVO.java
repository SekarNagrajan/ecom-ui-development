/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class MailAlertVO implements Serializable{

    private String module=null;
    private String activity=null;
    private String activityCode=null;
    private String mailAlert=null;
    private String loginName=null;
    private String alreadyExist=null;
      private String langType=null;

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }
      
      

    public String getAlreadyExist() {
        return alreadyExist;
    }

    public void setAlreadyExist(String alreadyExist) {
        this.alreadyExist = alreadyExist;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMailAlert() {
        return mailAlert;
    }

    public void setMailAlert(String mailAlert) {
        this.mailAlert = mailAlert;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


}
