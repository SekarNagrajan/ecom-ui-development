/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class CutoffConfigVO implements Serializable {

    private String port = null;
    private String terminal = "";
    private String cfsGateClosing = "";
    private String vgmClosing = "";
    private String docClosing = "";
    private String ediDecClosing = "";
    private String fullContClosing = "";
    private String saveType = null;
    private String cruser = null;

    private String bkgcutoff;
    private String doccutoff;
    private String vgmcutoff;
    private String bbrorocutoff;
    private String hazcutoff;
    private String portentrycutoff;
    private String excludeweekend;

    public String getBkgcutoff() {
        return bkgcutoff;
    }

    public void setBkgcutoff(String bkgcutoff) {
        this.bkgcutoff = bkgcutoff;
    }

    public String getDoccutoff() {
        return doccutoff;
    }

    public void setDoccutoff(String doccutoff) {
        this.doccutoff = doccutoff;
    }

    public String getVgmcutoff() {
        return vgmcutoff;
    }

    public void setVgmcutoff(String vgmcutoff) {
        this.vgmcutoff = vgmcutoff;
    }

    public String getBbrorocutoff() {
        return bbrorocutoff;
    }

    public void setBbrorocutoff(String bbrorocutoff) {
        this.bbrorocutoff = bbrorocutoff;
    }

    public String getHazcutoff() {
        return hazcutoff;
    }

    public void setHazcutoff(String hazcutoff) {
        this.hazcutoff = hazcutoff;
    }

    public String getPortentrycutoff() {
        return portentrycutoff;
    }

    public void setPortentrycutoff(String portentrycutoff) {
        this.portentrycutoff = portentrycutoff;
    }

    public String getCruser() {
        return cruser;
    }

    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getCfsGateClosing() {
        return cfsGateClosing;
    }

    public void setCfsGateClosing(String cfsGateClosing) {
        this.cfsGateClosing = cfsGateClosing;
    }

    public String getDocClosing() {
        return docClosing;
    }

    public void setDocClosing(String docClosing) {
        this.docClosing = docClosing;
    }

    public String getEdiDecClosing() {
        return ediDecClosing;
    }

    public void setEdiDecClosing(String ediDecClosing) {
        this.ediDecClosing = ediDecClosing;
    }

    public String getFullContClosing() {
        return fullContClosing;
    }

    public void setFullContClosing(String fullContClosing) {
        this.fullContClosing = fullContClosing;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getVgmClosing() {
        return vgmClosing;
    }

    public void setVgmClosing(String vgmClosing) {
        this.vgmClosing = vgmClosing;
    }

    public String getExcludeweekend() {
        return excludeweekend;
    }

    public void setExcludeweekend(String excludeweekend) {
        this.excludeweekend = excludeweekend;
    }

}
