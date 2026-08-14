/*
 * VO class representing cutoff information in API response
 */
package net.com.ecom.ws.vo;

import java.io.Serializable;

/**
 * VO class for Cutoff information in BetweenLocation API response
 * 
 * @author gobinath.t
 */
public class CutoffApiVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String docClosing;
    private String vgmClosing;
    private String contClosing;

    public CutoffApiVo() {
    }

    public CutoffApiVo(String docClosing, String vgmClosing, String contClosing) {
        this.docClosing = docClosing;
        this.vgmClosing = vgmClosing;
        this.contClosing = contClosing;
    }

    public String getDocClosing() {
        return docClosing;
    }

    public void setDocClosing(String docClosing) {
        this.docClosing = docClosing;
    }

    public String getVgmClosing() {
        return vgmClosing;
    }

    public void setVgmClosing(String vgmClosing) {
        this.vgmClosing = vgmClosing;
    }

    public String getContClosing() {
        return contClosing;
    }

    public void setContClosing(String contClosing) {
        this.contClosing = contClosing;
    }

    @Override
    public String toString() {
        return "CutoffApiVo{" +
                "docClosing='" + docClosing + '\'' +
                ", vgmClosing='" + vgmClosing + '\'' +
                ", contClosing='" + contClosing + '\'' +
                '}';
    }
}
