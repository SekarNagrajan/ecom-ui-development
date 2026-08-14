/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class RatesContractsVO implements Serializable {

    @SerializedName("ratetype")
    @Expose
    private String ratesType;

    @SerializedName("eqptype")
    @Expose
    private String eqpType;

    @SerializedName("commodity")
    @Expose
    private String commodity;

    @SerializedName("commodityname")
    @Expose
    private String commodityDesc;

    @SerializedName("localamount")
    @Expose
    private String ofr;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("itemno")
    @Expose
    private String itemNo;

    @SerializedName("rateno")
    @Expose
    private String rateNo;

    @SerializedName("loadport")
    @Expose
    private String loadPort;

    @SerializedName("dischport")
    @Expose
    private String dischPort;

    @SerializedName("soc")
    @Expose
    private String soc;
   
    @SerializedName("suramt")
    @Expose
    private String surAmount;
    
    private String originPort;
    private String DeliveryPort;
    
    @SerializedName("carrterms")
    @Expose
    private String carrTerms;

    public String getSurAmount() {
        return surAmount;
    }

    public void setSurAmount(String surAmount) {
        this.surAmount = surAmount;
    }
    

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDeliveryPort() {
        return DeliveryPort;
    }

    public void setDeliveryPort(String DeliveryPort) {
        this.DeliveryPort = DeliveryPort;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getLoadPort() {
        return loadPort;
    }

    public void setLoadPort(String loadPort) {
        this.loadPort = loadPort;
    }

    public String getDischPort() {
        return dischPort;
    }

    public void setDischPort(String dischPort) {
        this.dischPort = dischPort;
    }

    public String getRateNo() {
        return rateNo;
    }

    public void setRateNo(String rateNo) {
        this.rateNo = rateNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public String getRatesType() {
        return ratesType;
    }

    public void setRatesType(String ratesType) {
        this.ratesType = ratesType;
    }

    public String getEqpType() {
        return eqpType;
    }

    public void setEqpType(String eqpType) {
        this.eqpType = eqpType;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getOfr() {
        return ofr;
    }

    public void setOfr(String ofr) {
        this.ofr = ofr;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCarrTerms() {
        return carrTerms;
    }

    public void setCarrTerms(String carrTerms) {
        this.carrTerms = carrTerms;
    }

}
