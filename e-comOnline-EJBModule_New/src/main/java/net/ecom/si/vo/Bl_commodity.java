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
public class Bl_commodity implements Serializable {

    
	@SerializedName("bl_no")
	@Expose
	private String bl_no;

	@SerializedName("itemtop")
	@Expose
	private float itemtop;

	@SerializedName("marksdesc")
	@Expose
	private String marksdesc;

	@SerializedName("istransrwdone")
	@Expose
	private String istransrwdone;

	@SerializedName("netwtunit")
	@Expose
	private String netwtunit;

	@SerializedName("itemvolume")
	@Expose
	private float itemvolume;

	@SerializedName("marksnumbers")
	@Expose
	private String marksnumbers;

	@SerializedName("packageid")
	@Expose
	private String packageid;

	@SerializedName("box")
	@Expose
	private int box;

	@SerializedName("itemforward")
	@Expose
	private float itemforward;

	@SerializedName("commodityid")
	@Expose
	private String commodityid;

	@SerializedName("itemright")
	@Expose
	private float itemright;

	@SerializedName("itemdesc")
	@Expose
	private String itemdesc;

	@SerializedName("tier")
	@Expose
	private String tier;

	@SerializedName("bay")
	@Expose
	private String bay;

	@SerializedName("eqpid")
	@Expose
	private String eqpid;

	@SerializedName("itemweight")
	@Expose
	private float itemweight;

	@SerializedName("itemaft")
	@Expose
	private float itemaft;

	@SerializedName("uniqueid")
	@Expose
	private String uniqueid;

	@SerializedName("finaldestination")
	@Expose
	private String finaldestination;

	@SerializedName("hssuffixcode")
	@Expose
	private String hssuffixcode;

	@SerializedName("alcohol")
	@Expose
	private String alcohol;

	@SerializedName("itemleft")
	@Expose
	private float itemleft;

	@SerializedName("itemvolumeunit")
	@Expose
	private String itemvolumeunit;

	@SerializedName("itemqty")
	@Expose
	private int itemqty;

	@SerializedName("carriercode")
	@Expose
	private String carriercode;

	@SerializedName("imophone")
	@Expose
	private String imophone;

	@SerializedName("hatch")
	@Expose
	private String hatch;

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
	private float nettwt;

	@SerializedName("comsplititemno")
	@Expose
	private int comsplititemno;

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

	@SerializedName("remarks")
	@Expose
	private String remarks;

	@SerializedName("skuid")
	@Expose
	private String skuid;

	@SerializedName("cid")
	@Expose
	private long cid;


	public String getBl_no() {
		return bl_no;
	}
	public void setBl_no(String bl_no) {
		this.bl_no = bl_no;
	}
	public float getItemtop() {
		return itemtop;
	}
	public void setItemtop(float itemtop) {
		this.itemtop = itemtop;
	}
	public String getMarksdesc() {
		return marksdesc;
	}
	public void setMarksdesc(String marksdesc) {
		this.marksdesc = marksdesc;
	}
	public String getIstransrwdone() {
		return istransrwdone;
	}
	public void setIstransrwdone(String istransrwdone) {
		this.istransrwdone = istransrwdone;
	}
	public String getNetwtunit() {
		return netwtunit;
	}
	public void setNetwtunit(String netwtunit) {
		this.netwtunit = netwtunit;
	}
	public float getItemvolume() {
		return itemvolume;
	}
	public void setItemvolume(float itemvolume) {
		this.itemvolume = itemvolume;
	}
	public String getMarksnumbers() {
		return marksnumbers;
	}
	public void setMarksnumbers(String marksnumbers) {
		this.marksnumbers = marksnumbers;
	}
	public String getPackageid() {
		return packageid;
	}
	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	public int getBox() {
		return box;
	}
	public void setBox(int box) {
		this.box = box;
	}
	public float getItemforward() {
		return itemforward;
	}
	public void setItemforward(float itemforward) {
		this.itemforward = itemforward;
	}
	public String getCommodityid() {
		return commodityid;
	}
	public void setCommodityid(String commodityid) {
		this.commodityid = commodityid;
	}
	public float getItemright() {
		return itemright;
	}
	public void setItemright(float itemright) {
		this.itemright = itemright;
	}
	public String getItemdesc() {
		return itemdesc;
	}
	public void setItemdesc(String itemdesc) {
		this.itemdesc = itemdesc;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public String getBay() {
		return bay;
	}
	public void setBay(String bay) {
		this.bay = bay;
	}
	public String getEqpid() {
		return eqpid;
	}
	public void setEqpid(String eqpid) {
		this.eqpid = eqpid;
	}
	public float getItemweight() {
		return itemweight;
	}
	public void setItemweight(float itemweight) {
		this.itemweight = itemweight;
	}
	public float getItemaft() {
		return itemaft;
	}
	public void setItemaft(float itemaft) {
		this.itemaft = itemaft;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public String getFinaldestination() {
		return finaldestination;
	}
	public void setFinaldestination(String finaldestination) {
		this.finaldestination = finaldestination;
	}
	public String getHssuffixcode() {
		return hssuffixcode;
	}
	public void setHssuffixcode(String hssuffixcode) {
		this.hssuffixcode = hssuffixcode;
	}
	public String getAlcohol() {
		return alcohol;
	}
	public void setAlcohol(String alcohol) {
		this.alcohol = alcohol;
	}
	public float getItemleft() {
		return itemleft;
	}
	public void setItemleft(float itemleft) {
		this.itemleft = itemleft;
	}
	public String getItemvolumeunit() {
		return itemvolumeunit;
	}
	public void setItemvolumeunit(String itemvolumeunit) {
		this.itemvolumeunit = itemvolumeunit;
	}
	public int getItemqty() {
		return itemqty;
	}
	public void setItemqty(int itemqty) {
		this.itemqty = itemqty;
	}
	public String getCarriercode() {
		return carriercode;
	}
	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}
	public String getImophone() {
		return imophone;
	}
	public void setImophone(String imophone) {
		this.imophone = imophone;
	}
	public String getHatch() {
		return hatch;
	}
	public void setHatch(String hatch) {
		this.hatch = hatch;
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
	public float getNettwt() {
		return nettwt;
	}
	public void setNettwt(float nettwt) {
		this.nettwt = nettwt;
	}
	public int getComsplititemno() {
		return comsplititemno;
	}
	public void setComsplititemno(int comsplititemno) {
		this.comsplititemno = comsplititemno;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSkuid() {
		return skuid;
	}
	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
}
