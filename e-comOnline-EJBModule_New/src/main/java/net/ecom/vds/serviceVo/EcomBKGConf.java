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
public class EcomBKGConf implements Serializable {

    @SerializedName("hazcutoff")
    @Expose
    private String hazcutoff;

    @SerializedName("bkgcutoff")
    @Expose
    private String bkgcutoff;

    @SerializedName("portentrycutoff")
    @Expose
    private String portentrycutoff;

    @SerializedName("doccutoff")
    @Expose
    private String doccutoff;

    @SerializedName("bbrorocutoff")
    @Expose
    private String bbrorocutoff;

    @SerializedName("vgmcutoff")
    @Expose
    private String vgmcutoff;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEdireaderid() {
        return edireaderid;
    }

    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }
    
    public String getHazcutoff() {
        return hazcutoff;
    }

    public void setHazcutoff(String hazcutoff) {
        this.hazcutoff = hazcutoff;
    }

    public String getBkgcutoff() {
        return bkgcutoff;
    }

    public void setBkgcutoff(String bkgcutoff) {
        this.bkgcutoff = bkgcutoff;
    }

    public String getPortentrycutoff() {
        return portentrycutoff;
    }

    public void setPortentrycutoff(String portentrycutoff) {
        this.portentrycutoff = portentrycutoff;
    }

    public String getDoccutoff() {
        return doccutoff;
    }

    public void setDoccutoff(String doccutoff) {
        this.doccutoff = doccutoff;
    }

    public String getBbrorocutoff() {
        return bbrorocutoff;
    }

    public void setBbrorocutoff(String bbrorocutoff) {
        this.bbrorocutoff = bbrorocutoff;
    }

    public String getVgmcutoff() {
        return vgmcutoff;
    }

    public void setVgmcutoff(String vgmcutoff) {
        this.vgmcutoff = vgmcutoff;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

}
