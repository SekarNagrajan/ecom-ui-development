/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

/**
 *
 * @author sasikumar.r
 */
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.ecom.vo.BoundConfigVo;

public class Allschedules implements Serializable {

    @SerializedName("arrdate")
    @Expose
    private String arrdate;

    @SerializedName("service")
    @Expose
    private String service;

    @SerializedName("vessel")
    @Expose
    private String vessel;

    @SerializedName("bound")
    @Expose
    private String bound;

    @SerializedName("groupcallid")
    @Expose
    private String groupcallid;

    @SerializedName("terminal")
    @Expose
    private String terminal;

    @SerializedName("depdate")
    @Expose
    private String depdate;

    @SerializedName("voyage")
    @Expose
    private String voyage;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("closingtime")
    @Expose
    private String closingtime;

    private BoundConfigVo boundConfig=null;

 
    public BoundConfigVo getBoundConfig() {
        return boundConfig;
    }

    public void setBoundConfig(BoundConfigVo boundConfig) {
        this.boundConfig = boundConfig;
    }

    public String getArrdate() {
        return arrdate;
    }

    public void setArrdate(String arrdate) {
        this.arrdate = arrdate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getGroupcallid() {
        return groupcallid;
    }

    public void setGroupcallid(String groupcallid) {
        this.groupcallid = groupcallid;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getDepdate() {
        return depdate;
    }

    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getClosingtime() {
        return closingtime;
    }

    public void setClosingtime(String closingtime) {
        this.closingtime = closingtime;
    }

}
