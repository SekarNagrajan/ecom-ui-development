/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.insurance.vo;

import java.time.Instant;

/**
 *
 * @author vijay.j
 */
public class Transportation {

    private String vesselName;
    private String imoNumber;
    private String marksAndNumbers;
    private String portOfDestination;
    private String portOfOrigin;
    private String shipmentDateTime;
    private String shipmentServiceCode;
    /**
     * @return the vesselName
     */
    public String getVesselName() {
        return vesselName;
    }

    /**
     * @param vesselName the vesselName to set
     */
    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    /**
     * @return the imoNumber
     */
    public String getImoNumber() {
        return imoNumber;
    }

    /**
     * @param imoNumber the imoNumber to set
     */
    public void setImoNumber(String imoNumber) {
        this.imoNumber = imoNumber;
    }

    /**
     * @return the marksAndNumbers
     */
    public String getMarksAndNumbers() {
        return marksAndNumbers;
    }

    /**
     * @param marksAndNumbers the marksAndNumbers to set
     */
    public void setMarksAndNumbers(String marksAndNumbers) {
        this.marksAndNumbers = marksAndNumbers;
    }

    /**
     * @return the portOfDestination
     */
    public String getPortOfDestination() {
        return portOfDestination;
    }

    /**
     * @param portOfDestination the portOfDestination to set
     */
    public void setPortOfDestination(String portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    /**
     * @return the portOfOrigin
     */
    public String getPortOfOrigin() {
        return portOfOrigin;
    }

    /**
     * @param portOfOrigin the portOfOrigin to set
     */
    public void setPortOfOrigin(String portOfOrigin) {
        this.portOfOrigin = portOfOrigin;
    }

    /**
     * @return the shipmentDateTime
     */
    public String getShipmentDateTime() {
        return shipmentDateTime;
    }

    /**
     * @param shipmentDateTime the shipmentDateTime to set
     */
    public void setShipmentDateTime(String shipmentDateTime) {
        this.shipmentDateTime = shipmentDateTime;
    }

    public String getShipmentServiceCode() {
        return shipmentServiceCode;
    }

    public void setShipmentServiceCode(String shipmentServiceCode) {
        this.shipmentServiceCode = shipmentServiceCode;
    }
    
}
