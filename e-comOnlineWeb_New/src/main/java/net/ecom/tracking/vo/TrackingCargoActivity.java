/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.tracking.vo;

import java.io.Serializable;

/**
 *
 * @author sasikumar.r
 */
public class TrackingCargoActivity implements Serializable {

    private String containerNo;
    private String movementDesc;
    private String portName;
    private String vessel;
    private String activityDate;
    private String serviceCode;
    private String voyage;
    private String bound;
    private String bkSalesAgent;
    private String blSalesAgent;
    private String eqpTypeId;
    private String activityloccode;
    private String nextpod;
    private String nextpodETD; 
    private String nextActualPort;
    private String nextActualEta;
    /**
     * @return the containerNo
     */
    public String getContainerNo() {
        return containerNo;
    }

    /**
     * @param containerNo the containerNo to set
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    /**
     * @return the movementDesc
     */
    public String getMovementDesc() {
        return movementDesc;
    }

    /**
     * @param movementDesc the movementDesc to set
     */
    public void setMovementDesc(String movementDesc) {
        this.movementDesc = movementDesc;
    }

    /**
     * @return the portName
     */
    public String getPortName() {
        return portName;
    }

    /**
     * @param portName the portName to set
     */
    public void setPortName(String portName) {
        this.portName = portName;
    }

    /**
     * @return the vessel
     */
    public String getVessel() {
        return vessel;
    }

    /**
     * @param vessel the vessel to set
     */
    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    /**
     * @return the activityDate
     */
    public String getActivityDate() {
        return activityDate;
    }

    /**
     * @param activityDate the activityDate to set
     */
    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    /**
     * @return the serviceCode
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * @param serviceCode the serviceCode to set
     */
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    /**
     * @return the voyage
     */
    public String getVoyage() {
        return voyage;
    }

    /**
     * @param voyage the voyage to set
     */
    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    /**
     * @return the bound
     */
    public String getBound() {
        return bound;
    }

    /**
     * @param bound the bound to set
     */
    public void setBound(String bound) {
        this.bound = bound;
    }

    /**
     * @return the bkSalesAgent
     */
    public String getBkSalesAgent() {
        return bkSalesAgent;
    }

    /**
     * @param bkSalesAgent the bkSalesAgent to set
     */
    public void setBkSalesAgent(String bkSalesAgent) {
        this.bkSalesAgent = bkSalesAgent;
    }

    /**
     * @return the blSalesAgent
     */
    public String getBlSalesAgent() {
        return blSalesAgent;
    }

    /**
     * @param blSalesAgent the blSalesAgent to set
     */
    public void setBlSalesAgent(String blSalesAgent) {
        this.blSalesAgent = blSalesAgent;
    }

    public String getEqpTypeId() {
        return eqpTypeId;
    }

    public void setEqpTypeId(String eqpTypeId) {
        this.eqpTypeId = eqpTypeId;
    }

    public String getActivityloccode() {
        return activityloccode;
    }

    public void setActivityloccode(String activityloccode) {
        this.activityloccode = activityloccode;
    }

    public String getNextpod() {
        return nextpod;
    }

    public void setNextpod(String nextpod) {
        this.nextpod = nextpod;
    }

    public String getNextpodETD() {
        return nextpodETD;
    }

    public void setNextpodETD(String nextpodETD) {
        this.nextpodETD = nextpodETD;
    }

    public String getNextActualPort() {
        return nextActualPort;
    }
    public void setNextActualPort(String nextActualPort) {
        this.nextActualPort = nextActualPort;
    }
    public String getNextActualEta() {
        return nextActualEta;
    }
    public void setNextActualEta(String nextActualEta) {
        this.nextActualEta = nextActualEta;
    }   
}
