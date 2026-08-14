/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class EcomBookingCharges implements Serializable {

    @SerializedName("chargeid")
    @Expose
    private String chargeid;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("payagencyid")
    @Expose
    private String payagencyid;

    @SerializedName("prepaidcollect")
    @Expose
    private String prepaidcollect;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("custtypeid")
    @Expose
    private String custtypeid;

    @SerializedName("eqptypeid")
    @Expose
    private String eqptypeid;

    public String getChargeid() {
        return chargeid;
    }

    public void setChargeid(String chargeid) {
        this.chargeid = chargeid;
    }

    public String getEdireaderid() {
        return edireaderid;
    }

    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    public String getPayagencyid() {
        return payagencyid;
    }

    public void setPayagencyid(String payagencyid) {
        this.payagencyid = payagencyid;
    }

    public String getPrepaidcollect() {
        return prepaidcollect;
    }

    public void setPrepaidcollect(String prepaidcollect) {
        this.prepaidcollect = prepaidcollect;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCusttypeid() {
        return custtypeid;
    }

    public void setCusttypeid(String custtypeid) {
        this.custtypeid = custtypeid;
    }

    public String getEqptypeid() {
        return eqptypeid;
    }

    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
    }

}
