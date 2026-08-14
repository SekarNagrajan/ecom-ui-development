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
 * @author prakash.mani
 */
public class BkgContainerHaulageDetails implements Serializable {

    @SerializedName("book_no")
    @Expose
    private String bookno;
    @SerializedName("eqpid")
    @Expose
    private String eqpId;
    @SerializedName("eqptypeid")
    @Expose
    private String eqptypeid;
    @SerializedName("referencenumber")
    @Expose
    private String custRef;
    @SerializedName("loadlocation")
    @Expose
    private String locCode;
    @SerializedName("loadlocationname")
    @Expose
    private String locName;
    @SerializedName("stopsequence")
    @Expose
    private String stopSeq;
    @SerializedName("contactaddress")
    @Expose
    private String address;
    @SerializedName("bkgreference")
    @Expose
    private String bkgreference;
    @SerializedName("tmsschpickup")
    @Expose
    private String TMS_Sche_Pickup;
    @SerializedName("tmsactpickup")
    @Expose
    private String TMS_Act_Pickup;
    @SerializedName("tmsschdrop")
    @Expose
    private String TMS_Sche_Drop;
    @SerializedName("tmsactdrop")
    @Expose
    private String TMS_Act_Drop;

    public String getBookno() {
        return bookno;
    }

    public void setBookno(String bookno) {
        this.bookno = bookno;
    }

    public String getEqpId() {
        return eqpId;
    }

    public void setEqpId(String eqpId) {
        this.eqpId = eqpId;
    }

    public String getEqptypeid() {
        return eqptypeid;
    }

    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
    }

    public String getCustRef() {
        return custRef;
    }

    public void setCustRef(String custRef) {
        this.custRef = custRef;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getStopSeq() {
        return stopSeq;
    }

    public void setStopSeq(String stopSeq) {
        this.stopSeq = stopSeq;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBkgreference() {
        return bkgreference;
    }

    public void setBkgreference(String bkgreference) {
        this.bkgreference = bkgreference;
    }

    public String getTMS_Sche_Pickup() {
        return TMS_Sche_Pickup;
    }

    public void setTMS_Sche_Pickup(String TMS_Sche_Pickup) {
        this.TMS_Sche_Pickup = TMS_Sche_Pickup;
    }

    public String getTMS_Act_Pickup() {
        return TMS_Act_Pickup;
    }

    public void setTMS_Act_Pickup(String TMS_Act_Pickup) {
        this.TMS_Act_Pickup = TMS_Act_Pickup;
    }

    public String getTMS_Sche_Drop() {
        return TMS_Sche_Drop;
    }

    public void setTMS_Sche_Drop(String TMS_Sche_Drop) {
        this.TMS_Sche_Drop = TMS_Sche_Drop;
    }

    public String getTMS_Act_Drop() {
        return TMS_Act_Drop;
    }

    public void setTMS_Act_Drop(String TMS_Act_Drop) {
        this.TMS_Act_Drop = TMS_Act_Drop;
    }

}
