/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author salomi.a
 */
public class Bl_header implements Serializable {

    @SerializedName("delivery")
    @Expose
    private String delivery;

    @SerializedName("precarriage")
    @Expose
    private String precarriage;

    @SerializedName("pod")
    @Expose
    private String pod;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("pol")
    @Expose
    private String pol;

    @SerializedName("shipper")
    @Expose
    private String shipper;

    @SerializedName("shippername")
    @Expose
    private String shipperName;

    @SerializedName("consignee")
    @Expose
    private String consignee;

    @SerializedName("consigneename")
    @Expose
    private String consigneeName;

    @SerializedName("notify")
    @Expose
    private String notify;

    @SerializedName("notifiername")
    @Expose
    private String notifierName;

    @SerializedName("originofcountry")
    @Expose
    private String originofcountry;

    @SerializedName("telex_release")
    @Expose
    private String telex_release;

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getNotifierName() {
        return notifierName;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getPrecarriage() {
        return precarriage;
    }

    public void setPrecarriage(String precarriage) {
        this.precarriage = precarriage;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getOriginofcountry() {
        return originofcountry;
    }

    public void setOriginofcountry(String originofcountry) {
        this.originofcountry = originofcountry;
    }

    public String getTelex_release() {
        return telex_release;
    }

    public void setTelex_release(String telex_release) {
        this.telex_release = telex_release;
    }

}
