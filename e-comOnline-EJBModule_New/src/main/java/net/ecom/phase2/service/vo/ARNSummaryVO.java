/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.phase2.service.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class ARNSummaryVO implements Serializable{

    @SerializedName("custcode")
    @Expose
    private String custcode = null;
    
    @SerializedName("blnumber")
    @Expose
    private String blnumber = null;
    
    @SerializedName("arrivalnoticeno")
    @Expose
    private String arrivalnoticeno = null;
    
    @SerializedName("originportid")
    @Expose
    private String originportid = null;
    
    @SerializedName("finalportid")
    @Expose
    private String deliveryportid = null;
    
    @SerializedName("vesselcode")
    @Expose
    private String vesselcode = null;
    
    @SerializedName("voyage")
    @Expose
    private String voyage = null;
    
    @SerializedName("bound")
    @Expose
    private String bound = null;
    
    @SerializedName("arrdate")
    @Expose
    private String arrdate = null;
    
    @SerializedName("podterminal")
    @Expose
    private String podterminal = null;
    
    @SerializedName("ecomprintstatus")
    @Expose
    private String ecomprintstatus = null;
    
    @SerializedName("hblnumber")
    @Expose
    private String hblnumber = null;

    public String getHblnumber() {
        return hblnumber;
    }

    public void setHblnumber(String hblnumber) {
        this.hblnumber = hblnumber;
    }
    

    public String getCustcode() {
        return custcode;
    }

    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }

    public String getBlnumber() {
        return blnumber;
    }

    public void setBlnumber(String blnumber) {
        this.blnumber = blnumber;
    }

    public String getArrivalnoticeno() {
        return arrivalnoticeno;
    }

    public void setArrivalnoticeno(String arrivalnoticeno) {
        this.arrivalnoticeno = arrivalnoticeno;
    }

    public String getOriginportid() {
        return originportid;
    }

    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    public String getDeliveryportid() {
        return deliveryportid;
    }

    public void setDeliveryportid(String deliveryportid) {
        this.deliveryportid = deliveryportid;
    }
    public String getVesselcode() {
        return vesselcode;
    }

    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getArrdate() {
        return arrdate;
    }

    public void setArrdate(String arrdate) {
        this.arrdate = arrdate;
    }

    public String getPodterminal() {
        return podterminal;
    }

    public void setPodterminal(String podterminal) {
        this.podterminal = podterminal;
    }

    public String getEcomprintstatus() {
        return ecomprintstatus;
    }

    public void setEcomprintstatus(String ecomprintstatus) {
        this.ecomprintstatus = ecomprintstatus;
    }

    
}
