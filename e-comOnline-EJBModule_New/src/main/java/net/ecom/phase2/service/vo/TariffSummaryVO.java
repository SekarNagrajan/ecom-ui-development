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
public class TariffSummaryVO implements Serializable {

    @SerializedName("loadport")
    @Expose
    private String loadport = null;

    @SerializedName("dischport")
    @Expose
    private String dischport = null;

    @SerializedName("eqptype")
    @Expose
    private String eqptype = null;

    @SerializedName("commodity")
    @Expose
    private String commoditycode = null;

    @SerializedName("validfrom")
    @Expose
    private String fmdate = null;

    @SerializedName("validto")
    @Expose
    private String todate = null;

    @SerializedName("localamount")
    @Expose
    private String locamt = null;

    @SerializedName("tariffamt")
    @Expose
    private String tariffamt = null;

    @SerializedName("currency")
    @Expose
    private String currency = null;

    @SerializedName("soc")
    @Expose
    private String soc = null;
    private String originPort;
    private String DeliveryPort;

    @SerializedName("rateno")
    @Expose
    private String rateNo;

    @SerializedName("carrterms")
    @Expose
    private String carrTerms;

    @SerializedName("suramt")
    @Expose
    private String suramt;

    @SerializedName("externalrate")
    @Expose
    private String externalrate;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLoadport() {
        return loadport;
    }

    public void setLoadport(String loadport) {
        this.loadport = loadport;
    }

    public String getDischport() {
        return dischport;
    }

    public void setDischport(String dischport) {
        this.dischport = dischport;
    }

    public String getEqptype() {
        return eqptype;
    }

    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    public String getCommoditycode() {
        return commoditycode;
    }

    public void setCommoditycode(String commoditycode) {
        this.commoditycode = commoditycode;
    }

    public String getFmdate() {
        return fmdate;
    }

    public void setFmdate(String fmdate) {
        this.fmdate = fmdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getLocamt() {
        return locamt;
    }

    public void setLocamt(String locamt) {
        this.locamt = locamt;
    }

    public String getTariffamt() {
        return tariffamt;
    }

    public void setTariffamt(String tariffamt) {
        this.tariffamt = tariffamt;
    }

    public String getRateNo() {
        return rateNo;
    }

    public void setRateNo(String rateNo) {
        this.rateNo = rateNo;
    }

    public String getCarrTerms() {
        return carrTerms;
    }

    public void setCarrTerms(String carrTerms) {
        this.carrTerms = carrTerms;
    }

    public String getSuramt() {
        return suramt;
    }

    public void setSuramt(String suramt) {
        this.suramt = suramt;
    }

    public String getExternalrate() {
        return externalrate;
    }

    public void setExternalrate(String externalrate) {
        this.externalrate = externalrate;
    }

}
