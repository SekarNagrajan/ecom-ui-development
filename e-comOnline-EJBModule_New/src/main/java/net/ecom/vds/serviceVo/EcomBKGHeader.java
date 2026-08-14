/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class EcomBKGHeader implements Serializable {

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("pol")
    @Expose
    private String pol;

    @SerializedName("pod")
    @Expose
    private String pod;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("delivery")
    @Expose
    private String delivery;

    @SerializedName("precarriage")
    @Expose
    private String precarriage;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("version")
    @Expose
    private String version;

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

    @SerializedName("loadvsl")
    @Expose
    private String loadVSL;

    @SerializedName("originofcountry")
    @Expose
    private String originofcountry;

    public String getLoadVSL() {
        return loadVSL;
    }

    public void setLoadVSL(String loadVSL) {
        this.loadVSL = loadVSL;
    }

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

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
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

    public String getEdireaderid() {
        return edireaderid;
    }

    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOriginofcountry() {
        return originofcountry;
    }

    public void setOriginofcountry(String originofcountry) {
        this.originofcountry = originofcountry;
    }
}
