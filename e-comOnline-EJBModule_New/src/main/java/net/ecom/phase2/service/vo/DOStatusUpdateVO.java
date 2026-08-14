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
public class DOStatusUpdateVO implements Serializable {

    @SerializedName("printstatus")
    @Expose
    private String printstatus = null;

    @SerializedName("ecomprintstatus")
    @Expose
    private String ecomprintstatus = null;

    @SerializedName("chgusr")
    @Expose
    private String chgusr = null;

    @SerializedName("delordno")
    @Expose
    private String delordno = null;

    @SerializedName("doissued")
    @Expose
    private String doissued = null;

    public String getPrintstatus() {
        return printstatus;
    }

    public void setPrintstatus(String printstatus) {
        this.printstatus = printstatus;
    }

    public String getEcomprintstatus() {
        return ecomprintstatus;
    }

    public void setEcomprintstatus(String ecomprintstatus) {
        this.ecomprintstatus = ecomprintstatus;
    }

    public String getChgusr() {
        return chgusr;
    }

    public void setChgusr(String chgusr) {
        this.chgusr = chgusr;
    }

    public String getDelordno() {
        return delordno;
    }

    public void setDelordno(String delordno) {
        this.delordno = delordno;
    }

    public String getDoissued() {
        return doissued;
    }

    public void setDoissued(String doissued) {
        this.doissued = doissued;
    }

}
