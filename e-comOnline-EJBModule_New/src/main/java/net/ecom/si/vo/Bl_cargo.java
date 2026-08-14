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
public class Bl_cargo implements Serializable {

    @SerializedName("bl_no")
    @Expose
    private String bl_no;

    @SerializedName("orderno")
    @Expose
    private String orderno;

    @SerializedName("oog")
    @Expose
    private String oog;

    @SerializedName("shipperown")
    @Expose
    private String shipperown;

    @SerializedName("cargoweight")
    @Expose
    private float cargoweight;

    @SerializedName("ventsetting")
    @Expose
    private String ventsetting;

    @SerializedName("slotunit")
    @Expose
    private String slotunit;

    @SerializedName("gensetid")
    @Expose
    private String gensetid;

    @SerializedName("actualcontainerflag")
    @Expose
    private String actualcontainerflag;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("eqpvolumeunit")
    @Expose
    private String eqpvolumeunit;

    @SerializedName("weightunit")
    @Expose
    private String weightunit;

    @SerializedName("nor")
    @Expose
    private String nor;

    @SerializedName("eqpvolume")
    @Expose
    private float eqpvolume;

    @SerializedName("containerized")
    @Expose
    private String containerized;

    @SerializedName("seal2no")
    @Expose
    private String seal2no;

    @SerializedName("raterefno")
    @Expose
    private String raterefno;

    @SerializedName("eqpstatus")
    @Expose
    private String eqpstatus;

    @SerializedName("eqpid")
    @Expose
    private String eqpid;

    @SerializedName("seal1no")
    @Expose
    private String seal1no;

    @SerializedName("tempmin")
    @Expose
    private String tempmin;

    @SerializedName("uniqueid")
    @Expose
    private String uniqueid;

    @SerializedName("ratetype")
    @Expose
    private String ratetype;

    @SerializedName("loadingcondition")
    @Expose
    private String loadingcondition;

    @SerializedName("carriercode")
    @Expose
    private String carriercode;

    @SerializedName("raterule")
    @Expose
    private String raterule;

    @SerializedName("slotloss")
    @Expose
    private String slotloss;

    @SerializedName("tempunit")
    @Expose
    private String tempunit;

    @SerializedName("settemp")
    @Expose
    private String settemp;

    @SerializedName("containerposition")
    @Expose
    private String containerposition;

    @SerializedName("oneway")
    @Expose
    private String oneway;

    @SerializedName("dgflag")
    @Expose
    private String dgflag;

    @SerializedName("tempmax")
    @Expose
    private String tempmax;

    @SerializedName("otherseals")
    @Expose
    private String otherseals;

    @SerializedName("chassisid")
    @Expose
    private String chassisid;

    @SerializedName("roundtrip")
    @Expose
    private String roundtrip;

    @SerializedName("grossweight")
    @Expose
    private float grossweight;

    @SerializedName("eqptypeid")
    @Expose
    private String eqptypeid;

    @SerializedName("cargono")
    @Expose
    private String cargono;

    @SerializedName("tareweight")
    @Expose
    private float tareweight;

    @SerializedName("cid")
    @Expose
    private long cid;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ventpercentage")
    @Expose
    private String ventpercentage;
    @SerializedName("itemweightunit")
    @Expose
    private String itemweightunit;

    @SerializedName("itemvolumeunit")
    @Expose
    private String itemvolumeunit;

    @SerializedName("itemqty")
    @Expose
    private String itemqty;

    @SerializedName("marksdesc")
    @Expose
    private String marksdesc;

    @SerializedName("itemvolume")
    @Expose
    private float itemvolume;

    @SerializedName("marksnumbers")
    @Expose
    private String marksnumbers;

    @SerializedName("packageid")
    @Expose
    private String packageid;

    @SerializedName("commodityid")
    @Expose
    private String commodityid;

    @SerializedName("itemweight")
    @Expose
    private float itemweight;

    @SerializedName("imophone")
    @Expose
    private String imophone;

    @SerializedName("hazardous")
    @Expose
    private String hazardous;

    @SerializedName("imocontact")
    @Expose
    private String imocontact;

    @SerializedName("itemright")
    @Expose
    private String itemright;

    @SerializedName("itemdesc")
    @Expose
    private String itemdesc;

    @SerializedName("itemaft")
    @Expose
    private String itemaft;

    @SerializedName("itemlengthunit")
    @Expose
    private String itemlengthunit;

    @SerializedName("itemtop")
    @Expose
    private String itemtop;

    @SerializedName("itemleft")
    @Expose
    private String itemleft;
    @SerializedName("itemforward")
    @Expose
    private String itemforward;
    @SerializedName("comdtygrp")
    @Expose
    private String comdtygrp;

    @SerializedName("humidity")
    @Expose
    private String humidity;
    @SerializedName("humidity_flag")
    @Expose
    private String humidity_flag;
    @SerializedName("drains")
    @Expose
    private String drains;
    @SerializedName("co2value")
    @Expose
    private String co2value;
    @SerializedName("o2value")
    @Expose
    private String o2value;
    @SerializedName("rate_eqptypeid")
    @Expose
    private String rate_eqpTypeid;
    @SerializedName("nettwt")
    @Expose
    private String nettwt;
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

    public String getRate_eqpTypeid() {
        return rate_eqpTypeid;
    }

    public void setRate_eqpTypeid(String rate_eqpTypeid) {
        this.rate_eqpTypeid = rate_eqpTypeid;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidity_flag() {
        return humidity_flag;
    }

    public void setHumidity_flag(String humidity_flag) {
        this.humidity_flag = humidity_flag;
    }

    public String getDrains() {
        return drains;
    }

    public void setDrains(String drains) {
        this.drains = drains;
    }

    public String getCo2value() {
        return co2value;
    }

    public void setCo2value(String co2value) {
        this.co2value = co2value;
    }

    public String getO2value() {
        return o2value;
    }

    public void setO2value(String o2value) {
        this.o2value = o2value;
    }

    public String getComdtygrp() {
        return comdtygrp;
    }

    public void setComdtygrp(String comdtygrp) {
        this.comdtygrp = comdtygrp;
    }

    public String getItemforward() {
        return itemforward;
    }

    public void setItemforward(String itemforward) {
        this.itemforward = itemforward;
    }

    public String getItemlengthunit() {
        return itemlengthunit;
    }

    public void setItemlengthunit(String itemlengthunit) {
        this.itemlengthunit = itemlengthunit;
    }

    public String getItemtop() {
        return itemtop;
    }

    public void setItemtop(String itemtop) {
        this.itemtop = itemtop;
    }

    public String getItemleft() {
        return itemleft;
    }

    public void setItemleft(String itemleft) {
        this.itemleft = itemleft;
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

    public String getItemaft() {
        return itemaft;
    }

    public void setItemaft(String itemaft) {
        this.itemaft = itemaft;
    }

    public String getImophone() {
        return imophone;
    }

    public void setImophone(String imophone) {
        this.imophone = imophone;
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

    public String getItemweightunit() {
        return itemweightunit;
    }

    public void setItemweightunit(String itemweightunit) {
        this.itemweightunit = itemweightunit;
    }

    public String getItemvolumeunit() {
        return itemvolumeunit;
    }

    public void setItemvolumeunit(String itemvolumeunit) {
        this.itemvolumeunit = itemvolumeunit;
    }

    public String getMarksdesc() {
        return marksdesc;
    }

    public void setMarksdesc(String marksdesc) {
        this.marksdesc = marksdesc;
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

    public String getCommodityid() {
        return commodityid;
    }

    public void setCommodityid(String commodityid) {
        this.commodityid = commodityid;
    }

    public float getItemweight() {
        return itemweight;
    }

    public void setItemweight(float itemweight) {
        this.itemweight = itemweight;
    }

    public String getBl_no() {
        return bl_no;
    }

    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOog() {
        return oog;
    }

    public void setOog(String oog) {
        this.oog = oog;
    }

    public String getShipperown() {
        return shipperown;
    }

    public void setShipperown(String shipperown) {
        this.shipperown = shipperown;
    }

    public float getCargoweight() {
        return cargoweight;
    }

    public void setCargoweight(float cargoweight) {
        this.cargoweight = cargoweight;
    }

    public String getSlotunit() {
        return slotunit;
    }

    public void setSlotunit(String slotunit) {
        this.slotunit = slotunit;
    }

    public String getGensetid() {
        return gensetid;
    }

    public void setGensetid(String gensetid) {
        this.gensetid = gensetid;
    }

    public String getActualcontainerflag() {
        return actualcontainerflag;
    }

    public void setActualcontainerflag(String actualcontainerflag) {
        this.actualcontainerflag = actualcontainerflag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEqpvolumeunit() {
        return eqpvolumeunit;
    }

    public void setEqpvolumeunit(String eqpvolumeunit) {
        this.eqpvolumeunit = eqpvolumeunit;
    }

    public String getWeightunit() {
        return weightunit;
    }

    public void setWeightunit(String weightunit) {
        this.weightunit = weightunit;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public float getEqpvolume() {
        return eqpvolume;
    }

    public void setEqpvolume(float eqpvolume) {
        this.eqpvolume = eqpvolume;
    }

    public String getContainerized() {
        return containerized;
    }

    public void setContainerized(String containerized) {
        this.containerized = containerized;
    }

    public String getSeal2no() {
        return seal2no;
    }

    public void setSeal2no(String seal2no) {
        this.seal2no = seal2no;
    }

    public String getRaterefno() {
        return raterefno;
    }

    public void setRaterefno(String raterefno) {
        this.raterefno = raterefno;
    }

    public String getEqpstatus() {
        return eqpstatus;
    }

    public void setEqpstatus(String eqpstatus) {
        this.eqpstatus = eqpstatus;
    }

    public String getEqpid() {
        return eqpid;
    }

    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public String getSeal1no() {
        return seal1no;
    }

    public void setSeal1no(String seal1no) {
        this.seal1no = seal1no;
    }

    public String getTempmin() {
        return tempmin;
    }

    public void setTempmin(String tempmin) {
        this.tempmin = tempmin;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getRatetype() {
        return ratetype;
    }

    public void setRatetype(String ratetype) {
        this.ratetype = ratetype;
    }

    public String getLoadingcondition() {
        return loadingcondition;
    }

    public void setLoadingcondition(String loadingcondition) {
        this.loadingcondition = loadingcondition;
    }

    public String getCarriercode() {
        return carriercode;
    }

    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
    }

    public String getRaterule() {
        return raterule;
    }

    public void setRaterule(String raterule) {
        this.raterule = raterule;
    }

    public String getTempunit() {
        return tempunit;
    }

    public void setTempunit(String tempunit) {
        this.tempunit = tempunit;
    }

    public String getSettemp() {
        return settemp;
    }

    public void setSettemp(String settemp) {
        this.settemp = settemp;
    }

    public String getContainerposition() {
        return containerposition;
    }

    public void setContainerposition(String containerposition) {
        this.containerposition = containerposition;
    }

    public String getOneway() {
        return oneway;
    }

    public void setOneway(String oneway) {
        this.oneway = oneway;
    }

    public String getDgflag() {
        return dgflag;
    }

    public void setDgflag(String dgflag) {
        this.dgflag = dgflag;
    }

    public String getTempmax() {
        return tempmax;
    }

    public void setTempmax(String tempmax) {
        this.tempmax = tempmax;
    }

    public String getOtherseals() {
        return otherseals;
    }

    public void setOtherseals(String otherseals) {
        this.otherseals = otherseals;
    }

    public String getChassisid() {
        return chassisid;
    }

    public void setChassisid(String chassisid) {
        this.chassisid = chassisid;
    }

    public String getRoundtrip() {
        return roundtrip;
    }

    public void setRoundtrip(String roundtrip) {
        this.roundtrip = roundtrip;
    }

    public float getGrossweight() {
        return grossweight;
    }

    public void setGrossweight(float grossweight) {
        this.grossweight = grossweight;
    }

    public String getEqptypeid() {
        return eqptypeid;
    }

    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
    }

    public float getTareweight() {
        return tareweight;
    }

    public void setTareweight(float tareweight) {
        this.tareweight = tareweight;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVentsetting() {
        return ventsetting;
    }

    public void setVentsetting(String ventsetting) {
        this.ventsetting = ventsetting;
    }

    public String getSlotloss() {
        return slotloss;
    }

    public void setSlotloss(String slotloss) {
        this.slotloss = slotloss;
    }

    public String getCargono() {
        return cargono;
    }

    public void setCargono(String cargono) {
        this.cargono = cargono;
    }

    public String getVentpercentage() {
        return ventpercentage;
    }

    public void setVentpercentage(String ventpercentage) {
        this.ventpercentage = ventpercentage;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public String getNettwt() {
        return nettwt;
    }

    public void setNettwt(String nettwt) {
        this.nettwt = nettwt;
    }

    public String getHssuffixcode() {
        return hssuffixcode;
    }

    public void setHssuffixcode(String hssuffixcode) {
        this.hssuffixcode = hssuffixcode;
    }

}
