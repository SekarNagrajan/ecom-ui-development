/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class BoundConfigVo implements Serializable {

    String lbound = "N/A";
    String dbound = "N/A";
    String vessel = "";
    String voyage = "";

    public String getLbound() {
        return lbound;
    }

    public void setLbound(String lbound) {
        this.lbound = lbound;
    }

    public String getDbound() {
        return dbound;
    }

    public void setDbound(String dbound) {
        this.dbound = dbound;
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
}
