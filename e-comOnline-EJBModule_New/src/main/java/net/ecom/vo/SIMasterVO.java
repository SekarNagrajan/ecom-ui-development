/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author udhaya
 */
public class SIMasterVO implements Serializable {

    private String strCntNo = "";
    private int strSelect = 0;
    private String strType = "";
    private String strEqpType = "";
    private String strsoc = "";

    private String cargoNo = null;
    private String cargoweight = null;
    private String netweight = null;
    private String tareweight = null;
    private String grossweight = null;
    private String tempMin = null;
    private String tempMax = null;
    private String setTemp = null;
    private String tempunit = null;
    private String oog = null;
    private String nor = null;
    private String refer = null;
    private String eqpSize = null;
    private String weightUnit = null;
    private String eqpStatus = null;
    private String dgFlag = null;

    private String humidity;
    private String humidity_flag;
    private String drains;
    private String co2value;
    private String o2value;
    private String rateeqptypeid;
     private String ventilation;

    public String getRateeqptypeid() {
        return rateeqptypeid;
    }

    public void setRateeqptypeid(String rateeqptypeid) {
        this.rateeqptypeid = rateeqptypeid;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidity_flag() {
        return humidity_flag;
    }

    public void setHumidity_flag(String humidity_flag) {
        this.humidity_flag = humidity_flag;
    }

    public String getDrains() {
        return drains;
    }

    public void setDrains(String drains) {
        this.drains = drains;
    }

    public String getCo2value() {
        return co2value;
    }

    public void setCo2value(String co2value) {
        this.co2value = co2value;
    }

    public String getO2value() {
        return o2value;
    }

    public void setO2value(String o2value) {
        this.o2value = o2value;
    }

    public String getVentilation() {
        return ventilation;
    }

    public void setVentilation(String ventilation) {
        this.ventilation = ventilation;
    }

   
    

    public String getDgFlag() {
        return dgFlag;
    }

    public void setDgFlag(String dgFlag) {
        this.dgFlag = dgFlag;
    }

    public String getEqpStatus() {
        return eqpStatus;
    }

    public void setEqpStatus(String eqpStatus) {
        this.eqpStatus = eqpStatus;
    }
    private int conId;

    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
    }

    public String getCargoNo() {
        return cargoNo;
    }

    public void setCargoNo(String cargoNo) {
        this.cargoNo = cargoNo;
    }

    public String getCargoweight() {
        return cargoweight;
    }

    public void setCargoweight(String cargoweight) {
        this.cargoweight = cargoweight;
    }

    public String getGrossweight() {
        return grossweight;
    }

    public void setGrossweight(String grossweight) {
        this.grossweight = grossweight;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getOog() {
        return oog;
    }

    public void setOog(String oog) {
        this.oog = oog;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getSetTemp() {
        return setTemp;
    }

    public void setSetTemp(String setTemp) {
        this.setTemp = setTemp;
    }

    public String getStrsoc() {
        return strsoc;
    }

    public void setStrsoc(String strsoc) {
        this.strsoc = strsoc;
    }

    public String getTareweight() {
        return tareweight;
    }

    public void setTareweight(String tareweight) {
        this.tareweight = tareweight;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempunit() {
        return tempunit;
    }

    public void setTempunit(String tempunit) {
        this.tempunit = tempunit;
    }

    /**
     * @return the strCntNo
     */
    public String getStrCntNo() {
        return Validation.checkNull(strCntNo);
    }

    /**
     * @param strCntNo the strCntNo to set
     */
    public void setStrCntNo(String strCntNo) {
        this.strCntNo = strCntNo;
    }

    /**
     * @return the strType
     */
    public String getStrType() {
        return Validation.checkNull(strType);
    }

    /**
     * @param strType the strType to set
     */
    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getNetweight() {
        return netweight;
    }

    public void setNetweight(String netweight) {
        this.netweight = netweight;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.strCntNo != null ? this.strCntNo.hashCode() : 0);
        hash = 79 * hash + (this.strType != null ? this.strType.hashCode() : 0);
        hash = 79 * hash + (this.strEqpType != null ? this.strEqpType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SIMasterVO other = (SIMasterVO) obj;
        if ((this.strCntNo == null) ? (other.strCntNo != null) : !this.strCntNo.equals(other.strCntNo)) {
            return false;
        }
        if ((this.strType == null) ? (other.strType != null) : !this.strType.equals(other.strType)) {
            return false;
        }
        if ((this.strEqpType == null) ? (other.strEqpType != null) : !this.strEqpType.equals(other.strEqpType)) {
            return false;
        }
        return true;
    }

    /* @Override
     public int hashCode() {
     int hash = 3;
     hash = 89 * hash + (this.strCntNo != null ? this.strCntNo.hashCode() : 0);
     hash = 89 * hash + (this.strType != null ? this.strType.hashCode() : 0);
     return hash;
     }

     @Override
     public boolean equals(Object obj) {
     if (obj == null) {
     return false;
     }
     if (getClass() != obj.getClass()) {
     return false;
     }
     final SIMasterVO other = (SIMasterVO) obj;
     if ((this.strCntNo == null) ? (other.strCntNo != null) : !this.strCntNo.equals(other.strCntNo)) {
     return false;
     }
     if ((this.strType == null) ? (other.strType != null) : !this.strType.equals(other.strType)) {
     return false;
     }
     return true;
     }*/
 /*   @Override
     public int hashCode() {
     int hash = 7;
     hash = 53 * hash + (this.strCntNo != null ? this.strCntNo.hashCode() : 0);
     hash = 53 * hash + this.getStrSelect();
     hash = 53 * hash + (this.strType != null ? this.strType.hashCode() : 0);
     return hash;
     }

     @Override
     public boolean equals(Object obj) {
     if (obj == null) {
     return false;
     }
     if (getClass() != obj.getClass()) {
     return false;
     }
     final SIMasterVO other = (SIMasterVO) obj;
     if ((this.strCntNo == null) ? (other.strCntNo != null) : !this.strCntNo.equals(other.strCntNo)) {
     return false;
     }
     if (this.getStrSelect() != other.getStrSelect()) {
     return false;
     }
     if ((this.strType == null) ? (other.strType != null) : !this.strType.equals(other.strType)) {
     return false;
     }
     return true;
     }
     */
    /**
     * @return the strSelect
     */
    public int getStrSelect() {
        return strSelect;
    }

    /**
     * @param strSelect the strSelect to set
     */
    public void setStrSelect(int strSelect) {
        this.strSelect = strSelect;
    }

    /**
     * @return the strEqpType
     */
    public String getStrEqpType() {
        return strEqpType;
    }

    /**
     * @param strEqpType the strEqpType to set
     */
    public void setStrEqpType(String strEqpType) {
        this.strEqpType = strEqpType;
    }

    public String getEqpSize() {
        return eqpSize;
    }

    public void setEqpSize(String eqpSize) {
        this.eqpSize = eqpSize;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

}
