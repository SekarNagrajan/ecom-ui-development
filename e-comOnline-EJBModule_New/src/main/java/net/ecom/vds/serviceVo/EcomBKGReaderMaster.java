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
public class EcomBKGReaderMaster implements Serializable {

    //EdI Reader Master
    @SerializedName("ediid")
    @Expose
    private String ediid;
    @SerializedName("datecr")
    @Expose
    private String datecr;
    @SerializedName("requestdate")
    @Expose
    private String requestdate;
    @SerializedName("agencycode")
    @Expose
    private String agencycode;
    @SerializedName("locationcode")
    @Expose
    private String locationcode;
    @SerializedName("uploadstatus")
    @Expose
    private String uploadstatus;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    /**
     * @return the ediid
     */
    public String getEdiid() {
        return ediid;
    }

    /**
     * @param ediid the ediid to set
     */
    public void setEdiid(String ediid) {
        this.ediid = ediid;
    }

    /**
     * @return the datecr
     */
    public String getDatecr() {
        return datecr;
    }

    /**
     * @param datecr the datecr to set
     */
    public void setDatecr(String datecr) {
        this.datecr = datecr;
    }

    /**
     * @return the requestdate
     */
    public String getRequestdate() {
        return requestdate;
    }

    /**
     * @param requestdate the requestdate to set
     */
    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }

    /**
     * @return the agencycode
     */
    public String getAgencycode() {
        return agencycode;
    }

    /**
     * @param agencycode the agencycode to set
     */
    public void setAgencycode(String agencycode) {
        this.agencycode = agencycode;
    }

    /**
     * @return the locationcode
     */
    public String getLocationcode() {
        return locationcode;
    }

    /**
     * @param locationcode the locationcode to set
     */
    public void setLocationcode(String locationcode) {
        this.locationcode = locationcode;
    }

    /**
     * @return the uploadstatus
     */
    public String getUploadstatus() {
        return uploadstatus;
    }

    /**
     * @param uploadstatus the uploadstatus to set
     */
    public void setUploadstatus(String uploadstatus) {
        this.uploadstatus = uploadstatus;
    }

    /**
     * @return the edireaderid
     */
    public String getEdireaderid() {
        return edireaderid;
    }

    /**
     * @param edireaderid the edireaderid to set
     */
    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }
}
