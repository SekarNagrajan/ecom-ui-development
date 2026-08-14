/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author deepa.p
 */
public class VGMSubmitForm extends org.apache.struts.action.ActionForm {

    private String vgmSubmission;
    private String referenceNo;
    private String vgmAPName;
    private String vgmOrigin;
    private String vgmDelivery;
    private String vgmSHName;
    private String vgmBLPOL;
    private String vgmBLPOD;
    private String vgmReference;
    private String vgmOrderNo;
    private String vgmObtainDate;
    private String vgmAddr1;
    private String vgmAddr2;
    private String vgmObtainMethod;
    private String vgmAuthPerson;
    private String vgmCountry;
    private String vgmPhone;
    private String vgmFax;
    private String vgmZipcode;
    private String vgmEmail;
    private String vgmCity;
    private String submitBy;
    private String sendemailid;
    private String vgmCompanyNmae;
    private String bookNo;
    private String blNo;
    private String vgmStatus;
    private FormFile vgmFile = null;
    private byte[] vgmFileData = null;
    private String vgmFileName = null;

    public byte[] getVgmFileData() {
        return vgmFileData;
    }

    public void setVgmFileData(byte[] vgmFileData) {
        this.vgmFileData = vgmFileData;
    }

    public String getVgmFileName() {
        return vgmFileName;
    }

    public void setVgmFileName(String vgmFileName) {
        this.vgmFileName = vgmFileName;
    }

    public FormFile getVgmFile() {
        return vgmFile;
    }

    public void setVgmFile(FormFile vgmFile) {
        this.vgmFile = vgmFile;
    }

    public String getVgmStatus() {
        return vgmStatus;
    }

    public void setVgmStatus(String vgmStatus) {
        this.vgmStatus = vgmStatus;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getVgmCompanyNmae() {
        return vgmCompanyNmae;
    }

    public void setVgmCompanyNmae(String vgmCompanyNmae) {
        this.vgmCompanyNmae = vgmCompanyNmae;
    }

    public String getSendemailid() {
        return sendemailid;
    }

    public void setSendemailid(String sendemailid) {
        this.sendemailid = sendemailid;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(String submitBy) {
        this.submitBy = submitBy;
    }

    public String getVgmAPName() {
        return vgmAPName;
    }

    public void setVgmAPName(String vgmAPName) {
        this.vgmAPName = vgmAPName;
    }

    public String getVgmAddr1() {
        return vgmAddr1;
    }

    public void setVgmAddr1(String vgmAddr1) {
        this.vgmAddr1 = vgmAddr1;
    }

    public String getVgmAddr2() {
        return vgmAddr2;
    }

    public void setVgmAddr2(String vgmAddr2) {
        this.vgmAddr2 = vgmAddr2;
    }

    public String getVgmAuthPerson() {
        return vgmAuthPerson;
    }

    public void setVgmAuthPerson(String vgmAuthPerson) {
        this.vgmAuthPerson = vgmAuthPerson;
    }

    public String getVgmBLPOD() {
        return vgmBLPOD;
    }

    public void setVgmBLPOD(String vgmBLPOD) {
        this.vgmBLPOD = vgmBLPOD;
    }

    public String getVgmBLPOL() {
        return vgmBLPOL;
    }

    public void setVgmBLPOL(String vgmBLPOL) {
        this.vgmBLPOL = vgmBLPOL;
    }

    public String getVgmCity() {
        return vgmCity;
    }

    public void setVgmCity(String vgmCity) {
        this.vgmCity = vgmCity;
    }

    public String getVgmCountry() {
        return vgmCountry;
    }

    public void setVgmCountry(String vgmCountry) {
        this.vgmCountry = vgmCountry;
    }

    public String getVgmDelivery() {
        return vgmDelivery;
    }

    public void setVgmDelivery(String vgmDelivery) {
        this.vgmDelivery = vgmDelivery;
    }

    public String getVgmEmail() {
        return vgmEmail;
    }

    public void setVgmEmail(String vgmEmail) {
        this.vgmEmail = vgmEmail;
    }

    public String getVgmFax() {
        return vgmFax;
    }

    public void setVgmFax(String vgmFax) {
        this.vgmFax = vgmFax;
    }

    public String getVgmObtainDate() {
        return vgmObtainDate;
    }

    public void setVgmObtainDate(String vgmObtainDate) {
        this.vgmObtainDate = vgmObtainDate;
    }

    public String getVgmObtainMethod() {
        return vgmObtainMethod;
    }

    public void setVgmObtainMethod(String vgmObtainMethod) {
        this.vgmObtainMethod = vgmObtainMethod;
    }

    public String getVgmOrderNo() {
        return vgmOrderNo;
    }

    public void setVgmOrderNo(String vgmOrderNo) {
        this.vgmOrderNo = vgmOrderNo;
    }

    public String getVgmOrigin() {
        return vgmOrigin;
    }

    public void setVgmOrigin(String vgmOrigin) {
        this.vgmOrigin = vgmOrigin;
    }

    public String getVgmPhone() {
        return vgmPhone;
    }

    public void setVgmPhone(String vgmPhone) {
        this.vgmPhone = vgmPhone;
    }

    public String getVgmReference() {
        return vgmReference;
    }

    public void setVgmReference(String vgmReference) {
        this.vgmReference = vgmReference;
    }

    public String getVgmSHName() {
        return vgmSHName;
    }

    public void setVgmSHName(String vgmSHName) {
        this.vgmSHName = vgmSHName;
    }

    public String getVgmSubmission() {
        return vgmSubmission;
    }

    public void setVgmSubmission(String vgmSubmission) {
        this.vgmSubmission = vgmSubmission;
    }

    public String getVgmZipcode() {
        return vgmZipcode;
    }

    public void setVgmZipcode(String vgmZipcode) {
        this.vgmZipcode = vgmZipcode;
    }

    public VGMSubmitForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
}
