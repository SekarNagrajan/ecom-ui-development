/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author priyadharshinim
 */
public class UnclearedCargoVo implements Serializable {
    private String blnumber;
    private String loadport;
    private String dischargeport;
    private String vesselarrdate;
    private String actcontrcnt;
    private String delcontrcnt;
    private String remcontrcnt;

    public String getBlnumber() {
        return blnumber;
    }

    public void setBlnumber(String blnumber) {
        this.blnumber = blnumber;
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

    public String getVesselarrdate() {
        return vesselarrdate;
    }

    public void setVesselarrdate(String vesselarrdate) {
        this.vesselarrdate = vesselarrdate;
    }

    public String getActcontrcnt() {
        return actcontrcnt;
    }

    public void setActcontrcnt(String actcontrcnt) {
        this.actcontrcnt = actcontrcnt;
    }

    public String getDelcontrcnt() {
        return delcontrcnt;
    }

    public void setDelcontrcnt(String delcontrcnt) {
        this.delcontrcnt = delcontrcnt;
    }

    public String getRemcontrcnt() {
        return remcontrcnt;
    }

    public void setRemcontrcnt(String remcontrcnt) {
        this.remcontrcnt = remcontrcnt;
    }
    
}
    
        
