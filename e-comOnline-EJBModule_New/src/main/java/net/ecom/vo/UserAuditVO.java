/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

public class UserAuditVO implements  Serializable{
    private String UserName = "";
    private String DateTime = "";
    private String Module = "";
    private String Activity = "";
    private String Reference = "";
    private String Remarks = "";
    private String UserCode = "";
    private String customername="";

    /**
     * @return the UserName
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * @param UserName the UserName to set
     */
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    /**
     * @return the DateTime
     */
    public String getDateTime() {
        return DateTime;
    }

    /**
     * @param DateTime the DateTime to set
     */
    public void setDateTime(String DateTime) {
        this.DateTime = DateTime;
    }

    /**
     * @return the Module
     */
    public String getModule() {
        return Module;
    }

    /**
     * @param Module the Module to set
     */
    public void setModule(String Module) {
        this.Module = Module;
    }

    /**
     * @return the Activity
     */
    public String getActivity() {
        return Activity;
    }

    /**
     * @param Activity the Activity to set
     */
    public void setActivity(String Activity) {
        this.Activity = Activity;
    }

    /**
     * @return the Reference
     */
    public String getReference() {
        return Reference;
    }

    /**
     * @param Reference the Reference to set
     */
    public void setReference(String Reference) {
        this.Reference = Reference;
    }

    /**
     * @return the Remarks
     */
    public String getRemarks() {
        return Remarks;
    }

    /**
     * @param Remarks the Remarks to set
     */
    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    /**
     * @return the UserCode
     */
    public String getUserCode() {
        return UserCode;
    }

    /**
     * @param UserCode the UserCode to set
     */
    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    /**
     * @return the customername
     */
    public String getCustomername() {
        return customername;
    }

    /**
     * @param customername the customername to set
     */
    public void setCustomername(String customername) {
        this.customername = customername;
    }

}
