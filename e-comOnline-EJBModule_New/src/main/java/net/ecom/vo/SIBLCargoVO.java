/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class SIBLCargoVO implements Serializable {

    private String siNo = null;
    private String cargoId = null;
    private String eqpType = null;
    private String eqpSize = null;
    private String containerNo = null;
    private String soc = null;
    private String carrierSeal = null;
    private String shipperSeal = null;
    private String tareWeight = null;
    private String cargoWeight = null;
    private String grossWeight = null;
    private String weightUnit = null;
    private String eqpStatus = null;
    private String oog = null;
    private String refNor = null;
    private String minTemp = null;
    private String maxTemp = null;
    private String setTemp = null;
    private String tempUnit = null;

    public String getSiNo() {
        return siNo;
    }

    public void setSiNo(String siNo) {
        this.siNo = siNo;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getEqpType() {
        return eqpType;
    }

    public void setEqpType(String eqpType) {
        this.eqpType = eqpType;
    }

    public String getEqpSize() {
        return eqpSize;
    }

    public void setEqpSize(String eqpSize) {
        this.eqpSize = eqpSize;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getCarrierSeal() {
        return carrierSeal;
    }

    public void setCarrierSeal(String carrierSeal) {
        this.carrierSeal = carrierSeal;
    }

    public String getShipperSeal() {
        return shipperSeal;
    }

    public void setShipperSeal(String shipperSeal) {
        this.shipperSeal = shipperSeal;
    }

    public String getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(String tareWeight) {
        this.tareWeight = tareWeight;
    }

    public String getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(String cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getEqpStatus() {
        return eqpStatus;
    }

    public void setEqpStatus(String eqpStatus) {
        this.eqpStatus = eqpStatus;
    }

    public String getOog() {
        return oog;
    }

    public void setOog(String oog) {
        this.oog = oog;
    }

    public String getRefNor() {
        return refNor;
    }

    public void setRefNor(String refNor) {
        this.refNor = refNor;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getSetTemp() {
        return setTemp;
    }

    public void setSetTemp(String setTemp) {
        this.setTemp = setTemp;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }
}
