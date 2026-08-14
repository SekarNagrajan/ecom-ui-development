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
public class ReturnVO implements Serializable {

    private String strRefNo = "";
    private String strDisplayMsg = "";
    private String strMailMsg = "";
    private String strToMailId = "";
    private String strFromMailId = "";
    private String strSubject = "";
    private String strName = "";
    private String strSaveType = "";
    private String admDisplayMsg = "";
    private String admMailMsg = "";
    private String admToMailId = "";
    private String admFromMailId = "";
    private String admSubject = "";
    private String activityCode = null;
    private String firstName = "";
    private String loginName = "";
    private String password = "";
    private String bccMail = "";
    private String readerid = null;
    private String agencycountry = "";
    private String company = "";
    private String contact = "";

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBccMail() {
        return bccMail;
    }

    public void setBccMail(String bccMail) {
        this.bccMail = bccMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String filenames = "";

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getRandomid() {
        return Validation.checkNull(randomid);
    }

    public void setRandomid(String randomid) {
        this.randomid = randomid;
    }
    private String admToBccId = "";

    private String randomid = "";

    /**
     * @return the strRefNo
     */
    public String getStrRefNo() {
        return Validation.checkNull(strRefNo);
    }

    /**
     * @param strRefNo the strRefNo to set
     */
    public void setStrRefNo(String strRefNo) {
        this.strRefNo = strRefNo;
    }

    /**
     * @return the strDisplayMsg
     */
    public String getStrDisplayMsg() {
        return Validation.checkNull(strDisplayMsg);
    }

    /**
     * @param strDisplayMsg the strDisplayMsg to set
     */
    public void setStrDisplayMsg(String strDisplayMsg) {
        this.strDisplayMsg = strDisplayMsg;
    }

    /**
     * @return the strMailMsg
     */
    public String getStrMailMsg() {
        return Validation.checkNull(strMailMsg);
    }

    /**
     * @param strMailMsg the strMailMsg to set
     */
    public void setStrMailMsg(String strMailMsg) {
        this.strMailMsg = strMailMsg;
    }

    /**
     * @return the strToMailId
     */
    public String getStrToMailId() {
        return Validation.checkNull(strToMailId);
    }

    /**
     * @param strToMailId the strToMailId to set
     */
    public void setStrToMailId(String strToMailId) {
        this.strToMailId = strToMailId;
    }

    /**
     * @return the strFromMailId
     */
    public String getStrFromMailId() {
        return Validation.checkNull(strFromMailId);
    }

    /**
     * @param strFromMailId the strFromMailId to set
     */
    public void setStrFromMailId(String strFromMailId) {
        this.strFromMailId = strFromMailId;
    }

    /**
     * @return the strSubject
     */
    public String getStrSubject() {
        return Validation.checkNull(strSubject);
    }

    /**
     * @param strSubject the strSubject to set
     */
    public void setStrSubject(String strSubject) {
        this.strSubject = strSubject;
    }

    /**
     * @return the strName
     */
    public String getStrName() {
        return Validation.checkNull(strName);
    }

    /**
     * @param strName the strName to set
     */
    public void setStrName(String strName) {
        this.strName = strName;
    }

    /**
     * @return the strSaveType
     */
    public String getStrSaveType() {
        return Validation.checkNull(strSaveType);
    }

    /**
     * @param strSaveType the strSaveType to set
     */
    public void setStrSaveType(String strSaveType) {
        this.strSaveType = strSaveType;
    }

    /**
     * @return the admDisplayMsg
     */
    public String getAdmDisplayMsg() {
        return admDisplayMsg;
    }

    /**
     * @param admDisplayMsg the admDisplayMsg to set
     */
    public void setAdmDisplayMsg(String admDisplayMsg) {
        this.admDisplayMsg = admDisplayMsg;
    }

    /**
     * @return the admMailMsg
     */
    public String getAdmMailMsg() {
        return admMailMsg;
    }

    /**
     * @param admMailMsg the admMailMsg to set
     */
    public void setAdmMailMsg(String admMailMsg) {
        this.admMailMsg = admMailMsg;
    }

    /**
     * @return the admToMailId
     */
    public String getAdmToMailId() {
        return admToMailId;
    }

    /**
     * @param admToMailId the admToMailId to set
     */
    public void setAdmToMailId(String admToMailId) {
        this.admToMailId = admToMailId;
    }

    /**
     * @return the admFromMailId
     */
    public String getAdmFromMailId() {
        return admFromMailId;
    }

    /**
     * @param admFromMailId the admFromMailId to set
     */
    public void setAdmFromMailId(String admFromMailId) {
        this.admFromMailId = admFromMailId;
    }

    /**
     * @return the admSubject
     */
    public String getAdmSubject() {
        return admSubject;
    }

    /**
     * @param admSubject the admSubject to set
     */
    public void setAdmSubject(String admSubject) {
        this.admSubject = admSubject;
    }

    /**
     * @return the admToBccId
     */
    public String getAdmToBccId() {
        return admToBccId;
    }

    /**
     * @param admToBccId the admToBccId to set
     */
    public void setAdmToBccId(String admToBccId) {
        this.admToBccId = admToBccId;
    }

    public String getFilenames() {
        return filenames;
    }

    /**
     * @param filenames the filenames to set
     */
    public void setFilenames(String filenames) {
        this.filenames = filenames;
    }

    public String getReaderid() {
        return readerid;
    }

    public void setReaderid(String readerid) {
        this.readerid = readerid;
    }

    public String getAgencycountry() {
        return agencycountry;
    }

    public void setAgencycountry(String agencycountry) {
        this.agencycountry = agencycountry;
    }

}
