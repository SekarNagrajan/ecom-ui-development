package net.ecom.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author deepa.p
 */
public class RateContractSurchargeVO  implements Serializable{
    @SerializedName("currency")
    @Expose
    private String currency;
    
    @SerializedName("itemno")
    @Expose
    private String itemNo;
    
    @SerializedName("rateno")
    @Expose
    private String rateNo;
    
    @SerializedName("charge")
    @Expose
    private String chargeCode;
    
    @SerializedName("amount")
    @Expose
    private String amount;
    
    @SerializedName("vatos")
    @Expose
    private String vatos;
    
    @SerializedName("remarks")
    @Expose
    private String remarks;
    
    @SerializedName("ratetype")
    @Expose
    private String rateType;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getRateNo() {
        return rateNo;
    }

    public void setRateNo(String rateNo) {
        this.rateNo = rateNo;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVatos() {
        return vatos;
    }

    public void setVatos(String vatos) {
        this.vatos = vatos;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }
    
}
