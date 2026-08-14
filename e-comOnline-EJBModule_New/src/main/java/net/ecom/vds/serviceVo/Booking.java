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
 * @author sasikumar.r
 */
public class Booking implements Serializable {

    @SerializedName("dateup")
    @Expose
    private String dateup;

    @SerializedName("finalportid")
    @Expose
    private String finalportid;

    @SerializedName("datecr")
    @Expose
    private String datecr;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("originportid")
    @Expose
    private String originportid;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("agencyrefno")
    @Expose
    private String agencyrefno;

    @SerializedName("dgstatus")
    @Expose
    private String dgstatus;
    
    @SerializedName("confirmeddate")
    @Expose
    private String confirmedDate;
    
    @SerializedName("remarks")
    @Expose
    private String remarks;
    
    @SerializedName("remarks_cancel")
    @Expose
    private String remarks_cancel;
    
    @SerializedName("userup")
    @Expose
    private String userup;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks_cancel() {
        return remarks_cancel;
    }

    public void setRemarks_cancel(String remarks_cancel) {
        this.remarks_cancel = remarks_cancel;
    }

    public String getUserup() {
        return userup;
    }

    public void setUserup(String userup) {
        this.userup = userup;
    }
    
    

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getDgstatus() {
        return dgstatus;
    }

    public void setDgstatus(String dgstatus) {
        this.dgstatus = dgstatus;
    }

    public String getDateup() {
        return dateup;
    }

    public void setDateup(String dateup) {
        this.dateup = dateup;
    }

    public String getFinalportid() {
        return finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }

    public String getDatecr() {
        return datecr;
    }

    public void setDatecr(String datecr) {
        this.datecr = datecr;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the agencyrefno
     */
    public String getAgencyrefno() {
        return agencyrefno;
    }

    /**
     * @param agencyrefno the agencyrefno to set
     */
    public void setAgencyrefno(String agencyrefno) {
        this.agencyrefno = agencyrefno;
    }
}
