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
public class Booking_commodity implements Serializable {

    @SerializedName("itemtop")
    @Expose
    private String itemtop;

    @SerializedName("reworkblno")
    @Expose
    private String reworkblno;

    @SerializedName("marksdesc")
    @Expose
    private String marksdesc;

    @SerializedName("netwtunit")
    @Expose
    private String netwtunit;

    @SerializedName("marksnumbers")
    @Expose
    private String marksnumbers;

    @SerializedName("itemvolume")
    @Expose
    private String itemvolume;

    @SerializedName("isedited")
    @Expose
    private String isedited;

    @SerializedName("packageid")
    @Expose
    private String packageid;

    @SerializedName("itemforward")
    @Expose
    private String itemforward;

    @SerializedName("commodityid")
    @Expose
    private String commodityid;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("itemright")
    @Expose
    private String itemright;

    @SerializedName("itemdesc")
    @Expose
    private String itemdesc;

    @SerializedName("eqpid")
    @Expose
    private String eqpid;

    @SerializedName("itemweight")
    @Expose
    private String itemweight;

    @SerializedName("itemaft")
    @Expose
    private String itemaft;

    @SerializedName("finaldestination")
    @Expose
    private String finaldestination;

    @SerializedName("itemvolumeunit")
    @Expose
    private String itemvolumeunit;

    @SerializedName("itemleft")
    @Expose
    private String itemleft;

    @SerializedName("alcohol")
    @Expose
    private String alcohol;

    @SerializedName("itemqty")
    @Expose
    private String itemqty;

    @SerializedName("imophone")
    @Expose
    private String imophone;

    @SerializedName("carriercode")
    @Expose
    private String carriercode;

    @SerializedName("hazardous")
    @Expose
    private String hazardous;

    @SerializedName("imocontact")
    @Expose
    private String imocontact;

    @SerializedName("itemno")
    @Expose
    private int itemno;

    @SerializedName("nettwt")
    @Expose
    private String nettwt;

    @SerializedName("itemweightunit")
    @Expose
    private String itemweightunit;

    @SerializedName("itemlengthunit")
    @Expose
    private String itemlengthunit;

    @SerializedName("prno")
    @Expose
    private String prno;

    @SerializedName("cargono")
    @Expose
    private int cargono;

    @SerializedName("militarycargo")
    @Expose
    private String militarycargo;

    @SerializedName("comdtygrp")
    @Expose
    private String comdtygrp;
    
    @SerializedName("hssuffixcode")
    @Expose
    private String hssuffixcode;
    @SerializedName("slname")
    @Expose
    private String slname;
    @SerializedName("slcode")
    @Expose
    private String slcode;
    @SerializedName("byname")
    @Expose
    private String byname;
    @SerializedName("bycode")
    @Expose
    private String bycode;
    @SerializedName("cmdty_hblno")
    @Expose
    private String cmdty_hblno;


    public String getSlname() {
        return slname;
    }

    public void setSlname(String slname) {
        this.slname = slname;
    }

    public String getSlcode() {
        return slcode;
    }

    public void setSlcode(String slcode) {
        this.slcode = slcode;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }

    public String getBycode() {
        return bycode;
    }

    public void setBycode(String bycode) {
        this.bycode = bycode;
    }

    public String getCmdty_hblno() {
        return cmdty_hblno;
    }

    public void setCmdty_hblno(String cmdty_hblno) {
        this.cmdty_hblno = cmdty_hblno;
    }

    public String getItemtop() {
        return itemtop;
    }

    public void setItemtop(String itemtop) {
        this.itemtop = itemtop;
    }

    public String getReworkblno() {
        return reworkblno;
    }

    public void setReworkblno(String reworkblno) {
        this.reworkblno = reworkblno;
    }

    public String getMarksdesc() {
        return marksdesc;
    }

    public void setMarksdesc(String marksdesc) {
        this.marksdesc = marksdesc;
    }

    public String getNetwtunit() {
        return netwtunit;
    }

    public void setNetwtunit(String netwtunit) {
        this.netwtunit = netwtunit;
    }

    public String getMarksnumbers() {
        return marksnumbers;
    }

    public void setMarksnumbers(String marksnumbers) {
        this.marksnumbers = marksnumbers;
    }

    public String getItemvolume() {
        return itemvolume;
    }

    public void setItemvolume(String itemvolume) {
        this.itemvolume = itemvolume;
    }

    public String getIsedited() {
        return isedited;
    }

    public void setIsedited(String isedited) {
        this.isedited = isedited;
    }

    public String getPackageid() {
        return packageid;
    }

    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }

    public String getItemforward() {
        return itemforward;
    }

    public void setItemforward(String itemforward) {
        this.itemforward = itemforward;
    }

    public String getCommodityid() {
        return commodityid;
    }

    public void setCommodityid(String commodityid) {
        this.commodityid = commodityid;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getItemright() {
        return itemright;
    }

    public void setItemright(String itemright) {
        this.itemright = itemright;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public String getEqpid() {
        return eqpid;
    }

    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public String getItemweight() {
        return itemweight;
    }

    public void setItemweight(String itemweight) {
        this.itemweight = itemweight;
    }

    public String getItemaft() {
        return itemaft;
    }

    public void setItemaft(String itemaft) {
        this.itemaft = itemaft;
    }

    public String getFinaldestination() {
        return finaldestination;
    }

    public void setFinaldestination(String finaldestination) {
        this.finaldestination = finaldestination;
    }

    public String getItemvolumeunit() {
        return itemvolumeunit;
    }

    public void setItemvolumeunit(String itemvolumeunit) {
        this.itemvolumeunit = itemvolumeunit;
    }

    public String getItemleft() {
        return itemleft;
    }

    public void setItemleft(String itemleft) {
        this.itemleft = itemleft;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public String getImophone() {
        return imophone;
    }

    public void setImophone(String imophone) {
        this.imophone = imophone;
    }

    public String getCarriercode() {
        return carriercode;
    }

    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
    }

    public String getHazardous() {
        return hazardous;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public String getImocontact() {
        return imocontact;
    }

    public void setImocontact(String imocontact) {
        this.imocontact = imocontact;
    }

    public int getItemno() {
        return itemno;
    }

    public void setItemno(int itemno) {
        this.itemno = itemno;
    }

    public String getNettwt() {
        return nettwt;
    }

    public void setNettwt(String nettwt) {
        this.nettwt = nettwt;
    }

    public String getItemweightunit() {
        return itemweightunit;
    }

    public void setItemweightunit(String itemweightunit) {
        this.itemweightunit = itemweightunit;
    }

    public String getItemlengthunit() {
        return itemlengthunit;
    }

    public void setItemlengthunit(String itemlengthunit) {
        this.itemlengthunit = itemlengthunit;
    }

    public String getPrno() {
        return prno;
    }

    public void setPrno(String prno) {
        this.prno = prno;
    }

    public int getCargono() {
        return cargono;
    }

    public void setCargono(int cargono) {
        this.cargono = cargono;
    }

    public String getMilitarycargo() {
        return militarycargo;
    }

    public void setMilitarycargo(String militarycargo) {
        this.militarycargo = militarycargo;
    }

    public String getComdtygrp() {
        return comdtygrp;
    }

    public void setComdtygrp(String comdtygrp) {
        this.comdtygrp = comdtygrp;
    }

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    public String getEdireaderid() {
        return edireaderid;
    }

    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    @SerializedName("version")
    @Expose
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHssuffixcode() {
        return hssuffixcode;
    }

    public void setHssuffixcode(String hssuffixcode) {
        this.hssuffixcode = hssuffixcode;
    }

}
