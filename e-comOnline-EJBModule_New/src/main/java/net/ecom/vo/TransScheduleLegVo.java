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
public class TransScheduleLegVo implements Serializable {

    private String service;
    private String vessel;
    private String voyage;
    private String bound;
    private String depatureDate;
    private String arrivalDate;  // Load port arrival date
    private String dischargeBound;
    private String type;
    private String groupCallId;
    private String terminal;

    private BoundConfigVo boundConfig = null;

    public BoundConfigVo getBoundConfig() {
        return boundConfig;
    }

    public void setBoundConfig(BoundConfigVo boundConfig) {
        this.boundConfig = boundConfig;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
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

    public String getDepatureDate() {
        return depatureDate;
    }

    public void setDepatureDate(String depatureDate) {
        this.depatureDate = depatureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDischargeBound() {
        return dischargeBound;
    }

    public void setDischargeBound(String dischargeBound) {
        this.dischargeBound = dischargeBound;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupCallId() {
        return groupCallId;
    }

    public void setGroupCallId(String groupCallId) {
        this.groupCallId = groupCallId;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

}
