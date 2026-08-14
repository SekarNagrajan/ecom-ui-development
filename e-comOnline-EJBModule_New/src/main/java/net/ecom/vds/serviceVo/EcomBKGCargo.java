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
public class EcomBKGCargo implements Serializable {

    @SerializedName("book_no")
    @Expose
    private String book_no;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("eqptypeid")
    @Expose
    private String eqptypeid;
    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;
    @SerializedName("eqpid")
    @Expose
    private String eqpid;
    @SerializedName("cargono")
    @Expose
    private int cargono;
    @SerializedName("oog")
    @Expose
    private String oog;
    @SerializedName("shipperown")
    @Expose
    private String shipperown;
    @SerializedName("cargoweight")
    @Expose
    private String cargoweight;
    @SerializedName("grossweight")
    @Expose
    private String grossweight;
    @SerializedName("weightunit")
    @Expose
    private String weightunit;
    @SerializedName("tempmax")
    @Expose
    private String tempmax;
    @SerializedName("tempmin")
    @Expose
    private String tempmin;
    @SerializedName("settemp")
    @Expose
    private String settemp;
    @SerializedName("tempunit")
    @Expose
    private String tempunit;
    @SerializedName("nor")
    @Expose
    private String nor;
    @SerializedName("ventsetting")
    @Expose
    private String ventsetting;
    @SerializedName("ventpercentage")
    @Expose
    private String ventpercentage;
    @SerializedName("loadingcondition")
    @Expose
    private String loadingcondition;
    @SerializedName("isdummy")
    @Expose
    private String isdummy;
    @SerializedName("dgflag")
    @Expose
    private String dgflag;
    @SerializedName("eqpstatus")
    @Expose
    private String eqpstatus;
    @SerializedName("orderno")
    @Expose
    private String orderno;
    @SerializedName("tareweight")
    @Expose
    private String tareweight;
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
    private String rate_eqptypeid;

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

    public String getVentpercentage() {
        return ventpercentage;
    }

    public void setVentpercentage(String ventpercentage) {
        this.ventpercentage = ventpercentage;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the eqptypeid
     */
    public String getEqptypeid() {
        return eqptypeid;
    }

    /**
     * @param eqptypeid the eqptypeid to set
     */
    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
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
     * @return the oog
     */
    public String getOog() {
        return oog;
    }

    /**
     * @param oog the oog to set
     */
    public void setOog(String oog) {
        this.oog = oog;
    }

    /**
     * @return the shipperown
     */
    public String getShipperown() {
        return shipperown;
    }

    /**
     * @param shipperown the shipperown to set
     */
    public void setShipperown(String shipperown) {
        this.shipperown = shipperown;
    }

    /**
     * @return the cargoweight
     */
    public String getCargoweight() {
        return cargoweight;
    }

    /**
     * @param cargoweight the cargoweight to set
     */
    public void setCargoweight(String cargoweight) {
        this.cargoweight = cargoweight;
    }

    /**
     * @return the grossweight
     */
    public String getGrossweight() {
        return grossweight;
    }

    /**
     * @param grossweight the grossweight to set
     */
    public void setGrossweight(String grossweight) {
        this.grossweight = grossweight;
    }

    /**
     * @return the weightunit
     */
    public String getWeightunit() {
        return weightunit;
    }

    /**
     * @param weightunit the weightunit to set
     */
    public void setWeightunit(String weightunit) {
        this.weightunit = weightunit;
    }

    /**
     * @return the tempmax
     */
    public String getTempmax() {
        return tempmax;
    }

    /**
     * @param tempmax the tempmax to set
     */
    public void setTempmax(String tempmax) {
        this.tempmax = tempmax;
    }

    /**
     * @return the tempmin
     */
    public String getTempmin() {
        return tempmin;
    }

    /**
     * @param tempmin the tempmin to set
     */
    public void setTempmin(String tempmin) {
        this.tempmin = tempmin;
    }

    /**
     * @return the settemp
     */
    public String getSettemp() {
        return settemp;
    }

    /**
     * @param settemp the settemp to set
     */
    public void setSettemp(String settemp) {
        this.settemp = settemp;
    }

    /**
     * @return the tempunit
     */
    public String getTempunit() {
        return tempunit;
    }

    /**
     * @param tempunit the tempunit to set
     */
    public void setTempunit(String tempunit) {
        this.tempunit = tempunit;
    }

    /**
     * @return the nor
     */
    public String getNor() {
        return nor;
    }

    /**
     * @param nor the nor to set
     */
    public void setNor(String nor) {
        this.nor = nor;
    }

    /**
     * @return the ventsetting
     */
    public String getVentsetting() {
        return ventsetting;
    }

    /**
     * @param ventsetting the ventsetting to set
     */
    public void setVentsetting(String ventsetting) {
        this.ventsetting = ventsetting;
    }

    /**
     * @return the loadingcondition
     */
    public String getLoadingcondition() {
        return loadingcondition;
    }

    /**
     * @param loadingcondition the loadingcondition to set
     */
    public void setLoadingcondition(String loadingcondition) {
        this.loadingcondition = loadingcondition;
    }

    /**
     * @return the isdummy
     */
    public String getIsdummy() {
        return isdummy;
    }

    /**
     * @param isdummy the isdummy to set
     */
    public void setIsdummy(String isdummy) {
        this.isdummy = isdummy;
    }

    /**
     * @return the dgflag
     */
    public String getDgflag() {
        return dgflag;
    }

    /**
     * @param dgflag the dgflag to set
     */
    public void setDgflag(String dgflag) {
        this.dgflag = dgflag;
    }

    /**
     * @return the eqpstatus
     */
    public String getEqpstatus() {
        return eqpstatus;
    }

    /**
     * @param eqpstatus the eqpstatus to set
     */
    public void setEqpstatus(String eqpstatus) {
        this.eqpstatus = eqpstatus;
    }

    /**
     * @return the orderno
     */
    public String getOrderno() {
        return orderno;
    }

    /**
     * @param orderno the orderno to set
     */
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    /**
     * @return the tareweight
     */
    public String getTareweight() {
        return tareweight;
    }

    /**
     * @param tareweight the tareweight to set
     */
    public void setTareweight(String tareweight) {
        this.tareweight = tareweight;
    }

    public String getRate_eqptypeid() {
        return rate_eqptypeid;
    }

    public void setRate_eqptypeid(String rate_eqptypeid) {
        this.rate_eqptypeid = rate_eqptypeid;
    }

}
