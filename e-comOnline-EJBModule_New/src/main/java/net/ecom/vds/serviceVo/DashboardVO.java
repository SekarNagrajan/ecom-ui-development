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
 * @author deepa.p
 */
public class DashboardVO implements Serializable {

    @SerializedName("countval")
    @Expose
    private String countVal;

    @SerializedName("originportid")
    @Expose
    private String originPortId;

    @SerializedName("finalportid")
    @Expose
    private String finalPortId;
    
    private String color;
    
    private String portPair;

    public String getPortPair() {
        return portPair;
    }

    public void setPortPair(String portPair) {
        this.portPair = portPair;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCountVal() {
        return countVal;
    }

    public void setCountVal(String countVal) {
        this.countVal = countVal;
    }

    public String getOriginPortId() {
        return originPortId;
    }

    public void setOriginPortId(String originPortId) {
        this.originPortId = originPortId;
    }

    public String getFinalPortId() {
        return finalPortId;
    }

    public void setFinalPortId(String finalPortId) {
        this.finalPortId = finalPortId;
    }

}
