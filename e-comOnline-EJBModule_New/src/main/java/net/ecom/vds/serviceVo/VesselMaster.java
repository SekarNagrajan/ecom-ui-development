/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import java.io.Serializable;

/**
 *
 * @author sasikumar.r
 */
public class VesselMaster implements Serializable {

    private String vesselname = null;
    private String vesselcode = null;
    private String callsign = null;
    private String vsloperator = null;
    private String vesselowner = null;
    private String vesseltype = null;
    private String flag = null;
    private String portofregistry = null;
    private String built = null;
    private String imo = null;
    private String loa = null;
    private String nominaltues = null;
    private String grt = null;
    private String nrt = null;
    private String vessel_info=null;

    /**
     * @return the vesselname
     */
    public String getVesselname() {
        return vesselname;
    }

    /**
     * @param vesselname the vesselname to set
     */
    public void setVesselname(String vesselname) {
        this.vesselname = vesselname;
    }

    /**
     * @return the vesselcode
     */
    public String getVesselcode() {
        return vesselcode;
    }

    /**
     * @param vesselcode the vesselcode to set
     */
    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    /**
     * @return the callsign
     */
    public String getCallsign() {
        return callsign;
    }

    /**
     * @param callsign the callsign to set
     */
    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    /**
     * @return the vsloperator
     */
    public String getVsloperator() {
        return vsloperator;
    }

    /**
     * @param vsloperator the vsloperator to set
     */
    public void setVsloperator(String vsloperator) {
        this.vsloperator = vsloperator;
    }

    /**
     * @return the vesselowner
     */
    public String getVesselowner() {
        return vesselowner;
    }

    /**
     * @param vesselowner the vesselowner to set
     */
    public void setVesselowner(String vesselowner) {
        this.vesselowner = vesselowner;
    }

    /**
     * @return the vesseltype
     */
    public String getVesseltype() {
        return vesseltype;
    }

    /**
     * @param vesseltype the vesseltype to set
     */
    public void setVesseltype(String vesseltype) {
        this.vesseltype = vesseltype;
    }

    /**
     * @return the flag
     */
    public String getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * @return the portofregistry
     */
    public String getPortofregistry() {
        return portofregistry;
    }

    /**
     * @param portofregistry the portofregistry to set
     */
    public void setPortofregistry(String portofregistry) {
        this.portofregistry = portofregistry;
    }

    /**
     * @return the built
     */
    public String getBuilt() {
        return built;
    }

    /**
     * @param built the built to set
     */
    public void setBuilt(String built) {
        this.built = built;
    }

    /**
     * @return the imo
     */
    public String getImo() {
        return imo;
    }

    /**
     * @param imo the imo to set
     */
    public void setImo(String imo) {
        this.imo = imo;
    }

    /**
     * @return the loa
     */
    public String getLoa() {
        return loa;
    }

    /**
     * @param loa the loa to set
     */
    public void setLoa(String loa) {
        this.loa = loa;
    }

    /**
     * @return the nominaltues
     */
    public String getNominaltues() {
        return nominaltues;
    }

    /**
     * @param nominaltues the nominaltues to set
     */
    public void setNominaltues(String nominaltues) {
        this.nominaltues = nominaltues;
    }

    /**
     * @return the grt
     */
    public String getGrt() {
        return grt;
    }

    /**
     * @param grt the grt to set
     */
    public void setGrt(String grt) {
        this.grt = grt;
    }

    /**
     * @return the nrt
     */
    public String getNrt() {
        return nrt;
    }

    /**
     * @param nrt the nrt to set
     */
    public void setNrt(String nrt) {
        this.nrt = nrt;
    }

    public String getVessel_info() {
        return vessel_info;
    }

    public void setVessel_info(String vessel_info) {
        this.vessel_info = vessel_info;
    }
    
}
