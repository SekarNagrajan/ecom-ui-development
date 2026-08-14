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
public class CROSummaryVO implements Serializable {

    @SerializedName("flsbookingno")
    @Expose
    private String flsbookingno = null;

    @SerializedName("book_no")
    @Expose
    private String book_no = null;

    @SerializedName("servicecode")
    @Expose
    private String servicecode = null;

    @SerializedName("vslcode")
    @Expose
    private String vslcode = null;

    @SerializedName("voycode")
    @Expose
    private String voycode = null;

    @SerializedName("bound")
    @Expose
    private String bound = null;

    @SerializedName("terminal")
    @Expose
    private String terminal = null;

    @SerializedName("origin")
    @Expose
    private String origin = null;

    @SerializedName("delivery")
    @Expose
    private String delivery = null;

    @SerializedName("equipmenttype")
    @Expose
    private String equipmenttype = null;

    @SerializedName("bkgqty")
    @Expose
    private String bkgqty = null;

    @SerializedName("relqty")
    @Expose
    private String relqty = null;

    @SerializedName("conrelno")
    @Expose
    private String conrelno = null;

    @SerializedName("convaliditydate")
    @Expose
    private String convaliditydate = null;

    @SerializedName("conreldate")
    @Expose
    private String conreldate = null;

    @SerializedName("arrdate")
    @Expose
    private String arrdate = null;

    @SerializedName("depdate")
    @Expose
    private String depdate = null;

    @SerializedName("qty")
    @Expose
    private String qty = null;

    @SerializedName("remarks")
    @Expose
    private String remarks = null;

    @SerializedName("agencycode")
    @Expose
    private String agencycode = null;
    @SerializedName("pol")
    @Expose
    private String pol = null;

    @SerializedName("pod")
    @Expose
    private String pod = null;
    private String loadPortText = "";
    private String dischargePortText = "";
    private String originPortText = "";
    private String deliveryPortText = "";

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

    public String getLoadPortText() {
        return loadPortText;
    }

    public void setLoadPortText(String loadPortText) {
        this.loadPortText = loadPortText;
    }

    public String getDischargePortText() {
        return dischargePortText;
    }

    public void setDischargePortText(String dischargePortText) {
        this.dischargePortText = dischargePortText;
    }

    public String getOriginPortText() {
        return originPortText;
    }

    public void setOriginPortText(String originPortText) {
        this.originPortText = originPortText;
    }

    public String getDeliveryPortText() {
        return deliveryPortText;
    }

    public void setDeliveryPortText(String deliveryPortText) {
        this.deliveryPortText = deliveryPortText;
    }

  
    

    public String getAgencycode() {
        return agencycode;
    }

    public void setAgencycode(String agencycode) {
        this.agencycode = agencycode;
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

   public String getFlsbookingno() {
        return flsbookingno;
    }

    public void setFlsbookingno(String flsbookingno) {
        this.flsbookingno = flsbookingno;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getServicecode() {
        return servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    public String getVslcode() {
        return vslcode;
    }

    public void setVslcode(String vslcode) {
        this.vslcode = vslcode;
    }

    public String getVoycode() {
        return voycode;
    }

    public void setVoycode(String voycode) {
        this.voycode = voycode;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getEquipmenttype() {
        return equipmenttype;
    }

    public void setEquipmenttype(String equipmenttype) {
        this.equipmenttype = equipmenttype;
    }

    public String getBkgqty() {
        return bkgqty;
    }

    public void setBkgqty(String bkgqty) {
        this.bkgqty = bkgqty;
    }

    public String getRelqty() {
        return relqty;
    }

    public void setRelqty(String relqty) {
        this.relqty = relqty;
    }

    public String getConrelno() {
        return conrelno;
    }

    public void setConrelno(String conrelno) {
        this.conrelno = conrelno;
    }

    public String getConvaliditydate() {
        return convaliditydate;
    }

    public void setConvaliditydate(String convaliditydate) {
        this.convaliditydate = convaliditydate;
    }

    public String getConreldate() {
        return conreldate;
    }

    public void setConreldate(String conreldate) {
        this.conreldate = conreldate;
    }

    public String getArrdate() {
        return arrdate;
    }

    public void setArrdate(String arrdate) {
        this.arrdate = arrdate;
    }

    public String getDepdate() {
        return depdate;
    }

    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
