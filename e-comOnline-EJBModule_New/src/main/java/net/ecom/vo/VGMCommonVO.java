/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author deepa.p
 */
public class VGMCommonVO implements Serializable {

    private List<VgmVO> vgmContList;
    private String apName;
    private String shName;
    private String origin;
    private String destination;
    private String loadPortId;
    private String bookNo;
    private String blNo;
    private String submissionType;
    private String dischargePortId;
    private String loginUser;
    private String vgmStatus;
    private String name;
    private String finalportid;
    private String loadportid;
    private String book_no;
    private String originportid;
    private String dischargeportid;
    private String custtypeid;
    private String status;
    private String bl_no;
    private String appVersion;
    private byte[] vgmFileData = null;
    private String vgmFileName = null;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBl_no() {
        return bl_no;
    }

    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFinalportid() {
        return finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }

    public String getLoadportid() {
        return loadportid;
    }

    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getOriginportid() {
        return originportid;
    }

    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    public String getDischargeportid() {
        return dischargeportid;
    }

    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

    public String getCusttypeid() {
        return custtypeid;
    }

    public void setCusttypeid(String custtypeid) {
        this.custtypeid = custtypeid;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getApName() {
        return apName;
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

    public void setApName(String apName) {
        this.apName = apName;
    }

    public String getVgmStatus() {
        return vgmStatus;
    }

    public void setVgmStatus(String vgmStatus) {
        this.vgmStatus = vgmStatus;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDischargePortId() {
        return dischargePortId;
    }

    public void setDischargePortId(String dischargePortId) {
        this.dischargePortId = dischargePortId;
    }

    public String getLoadPortId() {
        return loadPortId;
    }

    public void setLoadPortId(String loadPortId) {
        this.loadPortId = loadPortId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getShName() {
        return shName;
    }

    public void setShName(String shName) {
        this.shName = shName;
    }

    public List<VgmVO> getVgmContList() {
        return vgmContList;
    }

    public void setVgmContList(List<VgmVO> vgmContList) {
        this.vgmContList = vgmContList;
    }

    public String getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(String submissionType) {
        this.submissionType = submissionType;
    }
}
