/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.phase2.service.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DOSummaryVO implements Serializable {

    @SerializedName("printstatus")
    @Expose
    private String printstatus = null;

    @SerializedName("ecomprintstatus")
    @Expose
    private String ecomprintstatus = null;

    @SerializedName("delordno")
    @Expose
    private String delordno = null;

    @SerializedName("delorddate")
    @Expose
    private String delorddate = null;

    @SerializedName("blnumber")
    @Expose
    private String blnumber = null;

    @SerializedName("vessel")
    @Expose
    private String vessel = null;

    @SerializedName("voyage")
    @Expose
    private String voyage = null;

    @SerializedName("bound")
    @Expose
    private String bound = null;

    @SerializedName("loadport")
    @Expose
    private String loadport = null;

    @SerializedName("dischargeport")
    @Expose
    private String dischargeport = null;

    @SerializedName("terminal")
    @Expose
    private String terminal = null;

    @SerializedName("arrdate")
    @Expose
    private String arrdate = null;
    
    @SerializedName("dovaliditydate")
    @Expose
    private String dovaldate = null;

    public String getDovaldate() {
        return dovaldate;
    }

    public void setDovaldate(String dovaldate) {
        this.dovaldate = dovaldate;
    }

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

    public String getDelordno() {
        return delordno;
    }

    public void setDelordno(String delordno) {
        this.delordno = delordno;
    }

    public String getDelorddate() {
        return delorddate;
    }

    public void setDelorddate(String delorddate) {
        this.delorddate = delorddate;
    }

    public String getBlnumber() {
        return blnumber;
    }

    public void setBlnumber(String blnumber) {
        this.blnumber = blnumber;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
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

    public String getLoadport() {
        return loadport;
    }

    public void setLoadport(String loadport) {
        this.loadport = loadport;
    }

    public String getDischargeport() {
        return dischargeport;
    }

    public void setDischargeport(String dischargeport) {
        this.dischargeport = dischargeport;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getArrdate() {
        return arrdate;
    }

    public void setArrdate(String arrdate) {
        this.arrdate = arrdate;
    }

  

}
