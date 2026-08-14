/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.phase2.service.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/**
 *
 * @author deepa.p
 */
public class SurchargeSummaryVO   implements Serializable{
    @SerializedName("chargecode")
    @Expose
    private String chargecode = null;
    
    @SerializedName("amttype")
    @Expose
    private String amttype = null;
    
    @SerializedName("chargename")
    @Expose
    private String chargename = null;
    
    @SerializedName("loadRegion")
    @Expose
    private String loadRegion = null;
    
    @SerializedName("dischargeRegion")
    @Expose
    private String dischargeRegion = null;
    
    @SerializedName("eqptype")
    @Expose
    private String eqptype = null;
    
    @SerializedName("formula")
    @Expose
    private String formula = null;
    
    @SerializedName("currency")
    @Expose
    private String currency = null;
    
    @SerializedName("amount")
    @Expose
    private String amount = null;
    
    @SerializedName("amountinusd")
    @Expose
    private String amountinusd = null;
    
    @SerializedName("validityFrom")
    @Expose
    private String validityFrom = null;
    
    @SerializedName("validityTo")
    @Expose
    private String validityTo = null;
     
    @SerializedName("nor")
    @Expose
    private String nor = null;
     
    @SerializedName("code")
    @Expose
    private String code = null;
    
    @SerializedName("type")
    @Expose
    private String surExcType = null;
    //
    private String origin;
    private String delivery;
    //

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }
     
   
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDischargeRegion() {
        return dischargeRegion;
    }

    public void setDischargeRegion(String dischargeRegion) {
        this.dischargeRegion = dischargeRegion;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getLoadRegion() {
        return loadRegion;
    }

    public void setLoadRegion(String loadRegion) {
        this.loadRegion = loadRegion;
    }

    public String getValidityFrom() {
        return validityFrom;
    }

    public void setValidityFrom(String validityFrom) {
        this.validityFrom = validityFrom;
    }

    public String getValidityTo() {
        return validityTo;
    }

    public void setValidityTo(String validityTo) {
        this.validityTo = validityTo;
    }

    public String getChargecode() {
        return chargecode;
    }

    public void setChargecode(String chargecode) {
        this.chargecode = chargecode;
    }

    public String getAmttype() {
        return amttype;
    }

    public void setAmttype(String amttype) {
        this.amttype = amttype;
    }

    public String getChargename() {
        return chargename;
    }

    public void setChargename(String chargename) {
        this.chargename = chargename;
    }

    public String getEqptype() {
        return eqptype;
    }

    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    public String getAmountinusd() {
        return amountinusd;
    }

    public void setAmountinusd(String amountinusd) {
        this.amountinusd = amountinusd;
    }

    /**
     * @return the surExcType
     */
    public String getSurExcType() {
        return surExcType;
    }

    /**
     * @param surExcType the surExcType to set
     */
    public void setSurExcType(String surExcType) {
        this.surExcType = surExcType;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the delivery
     */
    public String getDelivery() {
        return delivery;
    }

    /**
     * @param delivery the delivery to set
     */
    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

}
