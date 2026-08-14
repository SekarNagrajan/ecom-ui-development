/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author salomi.a
 */
public class Vesselmaster implements Serializable {

    @SerializedName("finalportdesc")
    @Expose
    private String finalportdesc;

    @SerializedName("oncarriage")
    @Expose
    private String oncarriage;

    @SerializedName("vesselcode")
    @Expose
    private String vesselcode;

    @SerializedName("originportdesc")
    @Expose
    private String originportdesc;

    @SerializedName("precarriage")
    @Expose
    private String precarriage;

    @SerializedName("bound")
    @Expose
    private String bound;

    @SerializedName("portname")
    @Expose
    private String portname;

    @SerializedName("loadportid")
    @Expose
    private String loadportid;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("vesselname")
    @Expose
    private String vesselname;

    @SerializedName("voyagecode")
    @Expose
    private String voyagecode;

    @SerializedName("dischargeportid")
    @Expose
    private String dischargeportid;

    @SerializedName("agencyrefno")
    @Expose
    private String agencyRefNo;

    @SerializedName("podat")
    @Expose
    private String podat;

    @SerializedName("polat")
    @Expose
    private String polat;

    @SerializedName("originportid")
    @Expose
    private String originportid;

    @SerializedName("finalportid")
    @Expose
    private String finalportid;

    @SerializedName("servicecode")
    @Expose
    private String servicecode;

    @SerializedName("bltype")
    @Expose
    private String bltype;

    @SerializedName("cargoprotectid")
    @Expose
    private String cargoprotectid;

    @SerializedName("t2lfiling")
    @Expose
    private String t2lfiling;

    @SerializedName("nvocc")
    @Expose
    private String nvocc;

    @SerializedName("enstype")
    @Expose
    private String ensdoctype;

    @SerializedName("ensfiling")
    @Expose
    private String ensfiling;

    public String getEnsfiling() {
        return ensfiling;
    }

    public void setEnsfiling(String ensfiling) {
        this.ensfiling = ensfiling;
    }

    public String getEnsdoctype() {
        return ensdoctype;
    }

    public void setEnsdoctype(String ensdoctype) {
        this.ensdoctype = ensdoctype;
    }

    public String getPodat() {
        return podat;
    }

    public void setPodat(String podat) {
        this.podat = podat;
    }

    public String getT2lfiling() {
        return t2lfiling;
    }

    public void setT2lfiling(String t2lfiling) {
        this.t2lfiling = t2lfiling;
    }

    public String getNvocc() {
        return nvocc;
    }

    public void setNvocc(String nvocc) {
        this.nvocc = nvocc;
    }

    public String getPolat() {
        return polat;
    }

    public void setPolat(String polat) {
        this.polat = polat;
    }

    public String getAgencyRefNo() {
        return agencyRefNo;
    }

    public void setAgencyRefNo(String agencyRefNo) {
        this.agencyRefNo = agencyRefNo;
    }

    public String getFinalportdesc() {
        return finalportdesc;
    }

    public void setFinalportdesc(String finalportdesc) {
        this.finalportdesc = finalportdesc;
    }

    public String getOncarriage() {
        return oncarriage;
    }

    public void setOncarriage(String oncarriage) {
        this.oncarriage = oncarriage;
    }

    public String getVesselcode() {
        return vesselcode;
    }

    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    public String getOriginportdesc() {
        return originportdesc;
    }

    public void setOriginportdesc(String originportdesc) {
        this.originportdesc = originportdesc;
    }

    public String getPrecarriage() {
        return precarriage;
    }

    public void setPrecarriage(String precarriage) {
        this.precarriage = precarriage;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
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

    public String getVesselname() {
        return vesselname;
    }

    public void setVesselname(String vesselname) {
        this.vesselname = vesselname;
    }

    public String getVoyagecode() {
        return voyagecode;
    }

    public void setVoyagecode(String voyagecode) {
        this.voyagecode = voyagecode;
    }

    public String getDischargeportid() {
        return dischargeportid;
    }

    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

    public String getOriginportid() {
        return originportid;
    }

    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    public String getFinalportid() {
        return finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }

    public String getBltype() {
        return bltype;
    }

    public void setBltype(String bltype) {
        this.bltype = bltype;
    }

    public String getServicecode() {
        return servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    public String getCargoprotectid() {
        return cargoprotectid;
    }

    public void setCargoprotectid(String cargoprotectid) {
        this.cargoprotectid = cargoprotectid;
    }

}
