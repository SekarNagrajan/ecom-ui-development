/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import java.io.Serializable;

/**
 *
 * @author preethi.elumalai
 */
public class BLChargeVo implements Serializable {

    private String chargeId = null;
    private String chargeName = null;
    private String currencyId = null;
    private String prepaidCollect = null;
    private String eqpTypeId = null;
    private String chargeAmount = null;
    private String chargeRoe = null;
    private String baseRate = null;
    private String chargename = null;
    private String baseRateUnit = null;
    private String payAgencyId = null;
    private String custTypeId = null;

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public void setChargeAmount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getEqpTypeId() {
        return eqpTypeId;
    }

    public void setEqpTypeId(String eqpTypeId) {
        this.eqpTypeId = eqpTypeId;
    }

    public String getChargeRoe() {
        return chargeRoe;
    }

    public void setChargeRoe(String chargeRoe) {
        this.chargeRoe = chargeRoe;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate) {
        this.baseRate = baseRate;
    }

    public String getChargename() {
        return chargename;
    }

    public void setChargename(String chargename) {
        this.chargename = chargename;
    }

    public String getBaseRateUnit() {
        return baseRateUnit;
    }

    public void setBaseRateUnit(String baseRateUnit) {
        this.baseRateUnit = baseRateUnit;
    }

    public String getPayAgencyId() {
        return payAgencyId;
    }

    public void setPayAgencyId(String payAgencyId) {
        this.payAgencyId = payAgencyId;
    }

    public String getCustTypeId() {
        return custTypeId;
    }

    public void setCustTypeId(String custTypeId) {
        this.custTypeId = custTypeId;
    }

}
