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
public class EcomBKGEDIDocStatus implements Serializable {

    @SerializedName("book_no")
    @Expose
    private String book_no;
    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;
    @SerializedName("transtype")
    @Expose
    private String transtype;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ediparty")
    @Expose
    private String ediparty;
    @SerializedName("receivdate")
    @Expose
    private String receivdate;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("servprovname")
    @Expose
    private String servprovname;
    @SerializedName("amendment_version")
    @Expose
    private int amendment_version;

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
     * @return the transtype
     */
    public String getTranstype() {
        return transtype;
    }

    /**
     * @param transtype the transtype to set
     */
    public void setTranstype(String transtype) {
        this.transtype = transtype;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the ediparty
     */
    public String getEdiparty() {
        return ediparty;
    }

    /**
     * @param ediparty the ediparty to set
     */
    public void setEdiparty(String ediparty) {
        this.ediparty = ediparty;
    }

    /**
     * @return the receivdate
     */
    public String getReceivdate() {
        return receivdate;
    }

    /**
     * @param receivdate the receivdate to set
     */
    public void setReceivdate(String receivdate) {
        this.receivdate = receivdate;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the servprovname
     */
    public String getServprovname() {
        return servprovname;
    }

    /**
     * @param servprovname the servprovname to set
     */
    public void setServprovname(String servprovname) {
        this.servprovname = servprovname;
    }

    public int getAmendment_version() {
        return amendment_version;
    }

    public void setAmendment_version(int amendment_version) {
        this.amendment_version = amendment_version;
    }
}
