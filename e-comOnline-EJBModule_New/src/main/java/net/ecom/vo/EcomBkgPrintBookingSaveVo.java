/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author vasanthakumar.r
 */
public class EcomBkgPrintBookingSaveVo implements Serializable {
  @SerializedName("book_no")
  @Expose
  public String bookNo = null;

  @SerializedName("shippername")
  @Expose
  public String shipperName = null;

  @SerializedName("shipperaddress")
  @Expose
  public String shipperAdd = null;

  @SerializedName("shipperreference")
  @Expose
  public String shipperRef = null;

  @SerializedName("fwdname")
  @Expose
  public String fwdName = null;

  @SerializedName("fwdaddress")
  @Expose
  public String fwdAdd = null;

  @SerializedName("mode")
  @Expose
  public String mode = null;

  @SerializedName("fwdreference")
  @Expose
  public String fwdRef = null;

  @SerializedName("emptypickup")
  @Expose
  public String emptyPickUp = null;

  @SerializedName("delivery")
  @Expose
  public String delivery = null;

  @SerializedName("remarks")
  @Expose
  public String remarks = null;

  @SerializedName("stuffpoint")
  @Expose
  public String stuffpoint = null;

  @SerializedName("loadportfreedays")
  @Expose
  public String loadPortFreeDays = null;

  @SerializedName("usercr")
  @Expose
  public String usercr = null;

  @SerializedName("userup")
  @Expose
  public String userup = null;

  @SerializedName("releaseterminal")
  @Expose
  public String releaseterminal = null;

  @SerializedName("acceptanceterminal")
  @Expose
  public String acceptanceterminal = null;

  @SerializedName("chasispickup")
  @Expose
  public String chasispickup = null;

  @SerializedName("chasisdropoff")
  @Expose
  public String chasisdropoff = null;

  @SerializedName("optionalpod")
  @Expose
  public String optionalpod = null;

  @SerializedName("shcustcode")
  @Expose
  public String shippercustcode = null;

  @SerializedName("fwcustcode")
  @Expose
  public String fwdcustcode = null;

  @SerializedName("showbound")
  @Expose
  public String showbound = null;

  @SerializedName("opscroremarks")
  @Expose
  public String OperationCroRemarks = null;

  @SerializedName("dateup")
  @Expose
  public String dateup = null;

  @SerializedName("datecr")
  @Expose
  public String datecr = null;

  @SerializedName("sg_to")
  @Expose
  public String sg_to = null;

  @SerializedName("sg_from")
  @Expose
  public String sg_from = null;

  @SerializedName("sg_attn")
  @Expose
  public String sg_attn = null;

  @SerializedName("sg_tel")
  @Expose
  public String sg_tel = null;

  @SerializedName("sg_fax")
  @Expose
  public String sg_fax = null;

  @SerializedName("sg_email")
  @Expose
  public String sg_email = null;

  @SerializedName("sg_cc")
  @Expose
  public String sg_cc = null;

  @SerializedName("sg_closingtime")
  @Expose
  public String sg_closingtime = null;

  @SerializedName("sg_scncode")
  @Expose
  public String sg_scncode = null;

  @SerializedName("sg_vesselid")
  @Expose
  public String sg_vesselid = null;

  @SerializedName("sg_agentcode")
  @Expose
  public String sg_agentcode = null;

  @SerializedName("sg_mainvsl")
  @Expose
  public String sg_mainvsl = null;

  @SerializedName("sg_tos")
  @Expose
  public String sg_tos = null;

  @SerializedName("sg_commodity")
  @Expose
  public String sg_commodity = null;

  public String getBookNo()
  {
    return this.bookNo;
  }

  public void setBookNo(String bookNo) {
    this.bookNo = bookNo;
  }

  public String getShipperName() {
    return this.shipperName;
  }

  public void setShipperName(String shipperName) {
    this.shipperName = shipperName;
  }

  public String getShipperAdd() {
    return this.shipperAdd;
  }

  public void setShipperAdd(String shipperAdd) {
    this.shipperAdd = shipperAdd;
  }

  public String getShipperRef() {
    return this.shipperRef;
  }

  public void setShipperRef(String shipperRef) {
    this.shipperRef = shipperRef;
  }

  public String getFwdName() {
    return this.fwdName;
  }

  public void setFwdName(String fwdName) {
    this.fwdName = fwdName;
  }

  public String getFwdAdd() {
    return this.fwdAdd;
  }

  public void setFwdAdd(String fwdAdd) {
    this.fwdAdd = fwdAdd;
  }

  public String getMode() {
    return this.mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getFwdRef() {
    return this.fwdRef;
  }

  public void setFwdRef(String fwdRef) {
    this.fwdRef = fwdRef;
  }

  public String getEmptyPickUp() {
    return this.emptyPickUp;
  }

  public void setEmptyPickUp(String emptyPickUp) {
    this.emptyPickUp = emptyPickUp;
  }

  public String getDelivery() {
    return this.delivery;
  }

  public void setDelivery(String delivery) {
    this.delivery = delivery;
  }

  public String getRemarks() {
    return this.remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getStuffpoint() {
    return this.stuffpoint;
  }

  public void setStuffpoint(String stuffpoint) {
    this.stuffpoint = stuffpoint;
  }

  public String getLoadPortFreeDays() {
    return this.loadPortFreeDays;
  }

  public void setLoadPortFreeDays(String loadPortFreeDays) {
    this.loadPortFreeDays = loadPortFreeDays;
  }

  public String getUsercr() {
    return this.usercr;
  }

  public void setUsercr(String usercr) {
    this.usercr = usercr;
  }

  public String getUserup() {
    return this.userup;
  }

  public void setUserup(String userup) {
    this.userup = userup;
  }

  public String getReleaseterminal() {
    return this.releaseterminal;
  }

  public void setReleaseterminal(String releaseterminal) {
    this.releaseterminal = releaseterminal;
  }

  public String getAcceptanceterminal() {
    return this.acceptanceterminal;
  }

  public void setAcceptanceterminal(String acceptanceterminal) {
    this.acceptanceterminal = acceptanceterminal;
  }

  public String getChasispickup() {
    return this.chasispickup;
  }

  public void setChasispickup(String chasispickup) {
    this.chasispickup = chasispickup;
  }

  public String getChasisdropoff() {
    return this.chasisdropoff;
  }

  public void setChasisdropoff(String chasisdropoff) {
    this.chasisdropoff = chasisdropoff;
  }

  public String getOptionalpod() {
    return this.optionalpod;
  }

  public void setOptionalpod(String optionalpod) {
    this.optionalpod = optionalpod;
  }

  public String getShippercustcode() {
    return this.shippercustcode;
  }

  public void setShippercustcode(String shippercustcode) {
    this.shippercustcode = shippercustcode;
  }

  public String getFwdcustcode() {
    return this.fwdcustcode;
  }

  public void setFwdcustcode(String fwdcustcode) {
    this.fwdcustcode = fwdcustcode;
  }

  public String getShowbound() {
    return this.showbound;
  }

  public void setShowbound(String showbound) {
    this.showbound = showbound;
  }

  public String getOperationCroRemarks() {
    return this.OperationCroRemarks;
  }

  public void setOperationCroRemarks(String OperationCroRemarks) {
    this.OperationCroRemarks = OperationCroRemarks;
  }

  public String getDateup() {
    return this.dateup;
  }

  public void setDateup(String dateup) {
    this.dateup = dateup;
  }

  public String getDatecr() {
    return this.datecr;
  }

  public void setDatecr(String datecr) {
    this.datecr = datecr;
  }

  public String getSg_to() {
    return this.sg_to;
  }

  public void setSg_to(String sg_to) {
    this.sg_to = sg_to;
  }

  public String getSg_from() {
    return this.sg_from;
  }

  public void setSg_from(String sg_from) {
    this.sg_from = sg_from;
  }

  public String getSg_attn() {
    return this.sg_attn;
  }

  public void setSg_attn(String sg_attn) {
    this.sg_attn = sg_attn;
  }

  public String getSg_tel() {
    return this.sg_tel;
  }

  public void setSg_tel(String sg_tel) {
    this.sg_tel = sg_tel;
  }

  public String getSg_fax() {
    return this.sg_fax;
  }

  public void setSg_fax(String sg_fax) {
    this.sg_fax = sg_fax;
  }

  public String getSg_email() {
    return this.sg_email;
  }

  public void setSg_email(String sg_email) {
    this.sg_email = sg_email;
  }

  public String getSg_cc() {
    return this.sg_cc;
  }

  public void setSg_cc(String sg_cc) {
    this.sg_cc = sg_cc;
  }

  public String getSg_closingtime() {
    return this.sg_closingtime;
  }

  public void setSg_closingtime(String sg_closingtime) {
    this.sg_closingtime = sg_closingtime;
  }

  public String getSg_scncode() {
    return this.sg_scncode;
  }

  public void setSg_scncode(String sg_scncode) {
    this.sg_scncode = sg_scncode;
  }

  public String getSg_vesselid() {
    return this.sg_vesselid;
  }

  public void setSg_vesselid(String sg_vesselid) {
    this.sg_vesselid = sg_vesselid;
  }

  public String getSg_agentcode() {
    return this.sg_agentcode;
  }

  public void setSg_agentcode(String sg_agentcode) {
    this.sg_agentcode = sg_agentcode;
  }

  public String getSg_mainvsl() {
    return this.sg_mainvsl;
  }

  public void setSg_mainvsl(String sg_mainvsl) {
    this.sg_mainvsl = sg_mainvsl;
  }

  public String getSg_tos() {
    return this.sg_tos;
  }

  public void setSg_tos(String sg_tos) {
    this.sg_tos = sg_tos;
  }

  public String getSg_commodity() {
    return this.sg_commodity;
  }

  public void setSg_commodity(String sg_commodity) {
    this.sg_commodity = sg_commodity;
  }

}
