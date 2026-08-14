/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.tracking.vo;

/**
 *
 * @author preethi.elumalai
 */
public class TrackingProtActivityResult {

    private String legType;
    private String pol;
    private String pod;
    private String etd;
    private String eta;
    private String loadportid;
    private String dischargeportid;

    public String getLegType() {
        return legType;
    }

    public void setLegType(String legType) {
        this.legType = legType;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getLoadportid() {
        return loadportid;
    }

    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    public String getDischargeportid() {
        return dischargeportid;
    }

    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

}
