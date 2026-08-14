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
 * @author sasikumar.r
 */
public class EcomBKGCommodity implements Serializable {

    //Commodity
    @SerializedName("commodityid")
    @Expose
    private String commodityid;
    @SerializedName("itemqty")
    @Expose
    private String itemqty;
    @SerializedName("itemweight")
    @Expose
    private String itemweight;
    @SerializedName("itemweightunit")
    @Expose
    private String itemweightunit;
    @SerializedName("hazardous")
    @Expose
    private String hazardous;
    @SerializedName("eqpid")
    @Expose
    private String eqpid;
    @SerializedName("alcohol")
    @Expose
    private String alcohol;
    @SerializedName("militarycargo")
    @Expose
    private String militarycargo;
    @SerializedName("cargono")
    @Expose
    private int cargono;
    @SerializedName("packageid")
    @Expose
    private String packageid;
    @SerializedName("itemvolume")
    @Expose
    private String itemvolume;
    @SerializedName("itemvolumeunit")
    @Expose
    private String itemvolumeunit;
    @SerializedName("itemforward")
    @Expose
    private String itemforward;
    @SerializedName("itemleft")
    @Expose
    private String itemleft;
    @SerializedName("itemtop")
    @Expose
    private String itemtop;
    @SerializedName("itemaft")
    @Expose
    private String itemaft;
    @SerializedName("itemright")
    @Expose
    private String itemright;
    @SerializedName("itemlengthunit")
    @Expose
    private String itemlengthunit;
    @SerializedName("comdtygrp")
    @Expose
    private String comdtygrp;
    @SerializedName("itemno")
    @Expose
    private String itemno;
    @SerializedName("carriercode")
    @Expose
    private String carriercode;
    @SerializedName("itemdesc")
    @Expose
    private String itemdesc;
    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("nettwt")
    @Expose
    private String nettwt;

    @SerializedName("marksdesc")
    @Expose
    private String marksdesc;

	@SerializedName("slname")
	@Expose
	private String slname;
	
	@SerializedName("bycode")
	@Expose
	private String bycode;
	
	@SerializedName("slcode")
	@Expose
	private String slcode;
	
	@SerializedName("byname")
	@Expose
	private String byname;
	
	@SerializedName("cmdty_hblno")
	@Expose
	private String cmdty_hblno;
    public String getSlname() {
        return slname;
    }

    public void setSlname(String slname) {
        this.slname = slname;
    }

    public String getBycode() {
        return bycode;
    }

    public void setBycode(String bycode) {
        this.bycode = bycode;
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

    public String getCmdty_hblno() {
        return cmdty_hblno;
    }

    public void setCmdty_hblno(String cmdty_hblno) {
        this.cmdty_hblno = cmdty_hblno;
    }

    /**
     * @return the commodityid
     */
    public String getCommodityid() {
        return commodityid;
    }

    /**
     * @param commodityid the commodityid to set
     */
    public void setCommodityid(String commodityid) {
        this.commodityid = commodityid;
    }

    /**
     * @return the itemQty
     */
    public String getItemqty() {
        return itemqty;
    }

    /**
     * @param itemQty the itemQty to set
     */
    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    /**
     * @return the itemweight
     */
    public String getItemweight() {
        return itemweight;
    }

    /**
     * @param itemweight the itemweight to set
     */
    public void setItemweight(String itemweight) {
        this.itemweight = itemweight;
    }

    /**
     * @return the itemweightunit
     */
    public String getItemweightunit() {
        return itemweightunit;
    }

    /**
     * @param itemweightunit the itemweightunit to set
     */
    public void setItemweightunit(String itemweightunit) {
        this.itemweightunit = itemweightunit;
    }

    /**
     * @return the hazardous
     */
    public String getHazardous() {
        return hazardous;
    }

    /**
     * @param hazardous the hazardous to set
     */
    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    /**
     * @return the eqpid
     */
    public String getEqpid() {
        return eqpid;
    }

    /**
     * @param eqpid the eqpid to set
     */
    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    /**
     * @return the alcohol
     */
    public String getAlcohol() {
        return alcohol;
    }

    /**
     * @param alcohol the alcohol to set
     */
    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    /**
     * @return the militarycargo
     */
    public String getMilitarycargo() {
        return militarycargo;
    }

    /**
     * @param militarycargo the militarycargo to set
     */
    public void setMilitarycargo(String militarycargo) {
        this.militarycargo = militarycargo;
    }

    /**
     * @return the cargono
     */
    public int getCargono() {
        return cargono;
    }

    /**
     * @param cargono the cargono to set
     */
    public void setCargono(int cargono) {
        this.cargono = cargono;
    }

    /**
     * @return the packageid
     */
    public String getPackageid() {
        return packageid;
    }

    /**
     * @param packageid the packageid to set
     */
    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }

    /**
     * @return the itemvolume
     */
    public String getItemvolume() {
        return itemvolume;
    }

    /**
     * @param itemvolume the itemvolume to set
     */
    public void setItemvolume(String itemvolume) {
        this.itemvolume = itemvolume;
    }

    /**
     * @return the itemvolumeunit
     */
    public String getItemvolumeunit() {
        return itemvolumeunit;
    }

    /**
     * @param itemvolumeunit the itemvolumeunit to set
     */
    public void setItemvolumeunit(String itemvolumeunit) {
        this.itemvolumeunit = itemvolumeunit;
    }

    /**
     * @return the itemforward
     */
    public String getItemforward() {
        return itemforward;
    }

    /**
     * @param itemforward the itemforward to set
     */
    public void setItemforward(String itemforward) {
        this.itemforward = itemforward;
    }

    /**
     * @return the itemleft
     */
    public String getItemleft() {
        return itemleft;
    }

    /**
     * @param itemleft the itemleft to set
     */
    public void setItemleft(String itemleft) {
        this.itemleft = itemleft;
    }

    /**
     * @return the itemtop
     */
    public String getItemtop() {
        return itemtop;
    }

    /**
     * @param itemtop the itemtop to set
     */
    public void setItemtop(String itemtop) {
        this.itemtop = itemtop;
    }

    /**
     * @return the itemaft
     */
    public String getItemaft() {
        return itemaft;
    }

    /**
     * @param itemaft the itemaft to set
     */
    public void setItemaft(String itemaft) {
        this.itemaft = itemaft;
    }

    /**
     * @return the itemright
     */
    public String getItemright() {
        return itemright;
    }

    /**
     * @param itemright the itemright to set
     */
    public void setItemright(String itemright) {
        this.itemright = itemright;
    }

    /**
     * @return the itemlengthunit
     */
    public String getItemlengthunit() {
        return itemlengthunit;
    }

    /**
     * @param itemlengthunit the itemlengthunit to set
     */
    public void setItemlengthunit(String itemlengthunit) {
        this.itemlengthunit = itemlengthunit;
    }

    /**
     * @return the comdtygrp
     */
    public String getComdtygrp() {
        return comdtygrp;
    }

    /**
     * @param comdtygrp the comdtygrp to set
     */
    public void setComdtygrp(String comdtygrp) {
        this.comdtygrp = comdtygrp;
    }

    /**
     * @return the itemno
     */
    public String getItemno() {
        return itemno;
    }

    /**
     * @param itemno the itemno to set
     */
    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    /**
     * @return the carriercode
     */
    public String getCarriercode() {
        return carriercode;
    }

    /**
     * @param carriercode the carriercode to set
     */
    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
    }

    /**
     * @return the itemdesc
     */
    public String getItemdesc() {
        return itemdesc;
    }

    /**
     * @param itemdesc the itemdesc to set
     */
    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    /**
     * @return the book_no
     */
    public String getBook_no() {
        return book_no;
    }

    /**
     * @param book_no the book_no to set
     */
    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    /**
     * @return the edireaderid
     */
    public String getEdireaderid() {
        return edireaderid;
    }

    /**
     * @param edireaderid the edireaderid to set
     */
    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public String getNettwt() {
        return nettwt;
    }

    public void setNettwt(String nettwt) {
        this.nettwt = nettwt;
    }

    public String getMarksdesc() {
        return marksdesc;
    }

    public void setMarksdesc(String marksdesc) {
        this.marksdesc = marksdesc;
    }

}
