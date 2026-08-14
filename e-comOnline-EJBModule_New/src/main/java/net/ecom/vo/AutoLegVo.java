/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 package net.ecom.vo;

 import java.io.Serializable;

 public class AutoLegVo implements Serializable {
    private boolean hasVesselVoyageInfo=false;
    private String strBoundThereLoad="";
    private BoundConfigVo boundConfig=null;

 
    public BoundConfigVo getBoundConfig() {
        return boundConfig;
    }

    public void setBoundConfig(BoundConfigVo boundConfig) {
        this.boundConfig = boundConfig;
    }
    public boolean isHasVesselVoyageInfo() {
        return hasVesselVoyageInfo;
    }

    public void setHasVesselVoyageInfo(boolean hasVesselVoyageInfo) {
        this.hasVesselVoyageInfo = hasVesselVoyageInfo;
    }   

    public String getStrBoundThereLoad() {
        return strBoundThereLoad;
    }

    public void setStrBoundThereLoad(String strBoundThereLoad) {
        this.strBoundThereLoad = strBoundThereLoad;
    }
    
 }
 