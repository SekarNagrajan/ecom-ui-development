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
public class EcomBKGReference implements Serializable {

    //EdiTempBooking_Reference
    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("refid")
    @Expose
    private String refid;

    @SerializedName("refval")
    @Expose
    private String refval;

    @SerializedName("isedited")
    @Expose
    private String isedited;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("edicleanstatus")
    @Expose
    private String edicleanstatus;

    @SerializedName("version")
    @Expose
    private String version;

    /**
     * @return the book_no
     */
    public String getBook_no() {
        return book_no;
    }

    /**
     * @param book_no the book_no to set
     */
    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    /**
     * @return the refid
     */
    public String getRefid() {
        return refid;
    }

    /**
     * @param refid the refid to set
     */
    public void setRefid(String refid) {
        this.refid = refid;
    }

    /**
     * @return the refval
     */
    public String getRefval() {
        return refval;
    }

    /**
     * @param refval the refval to set
     */
    public void setRefval(String refval) {
        this.refval = refval;
    }

    /**
     * @return the isedited
     */
    public String getIsedited() {
        return isedited;
    }

    /**
     * @param isedited the isedited to set
     */
    public void setIsedited(String isedited) {
        this.isedited = isedited;
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

    /**
     * @return the edicleanstatus
     */
    public String getEdicleanstatus() {
        return edicleanstatus;
    }

    /**
     * @param edicleanstatus the edicleanstatus to set
     */
    public void setEdicleanstatus(String edicleanstatus) {
        this.edicleanstatus = edicleanstatus;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
