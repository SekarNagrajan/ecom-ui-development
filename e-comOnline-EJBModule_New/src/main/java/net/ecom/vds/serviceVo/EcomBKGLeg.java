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
public class EcomBKGLeg implements Serializable {

    @SerializedName("book_no")
    @Expose
    private String book_no;
    @SerializedName("leg_no")
    @Expose
    private int leg_no;
    @SerializedName("bound")
    @Expose
    private String bound;
    @SerializedName("servicecode")
    @Expose
    private String servicecode;
    @SerializedName("vesselcode")
    @Expose
    private String vesselcode;
    @SerializedName("loadportid")
    @Expose
    private String loadportid;
    @SerializedName("dischargeportid")
    @Expose
    private String dischargeportid;
    @SerializedName("etddate")
    @Expose
    private String etddate;
    @SerializedName("etadate")
    @Expose
    private String etadate;
    @SerializedName("legtypeid")
    @Expose
    private String legtypeid;
    @SerializedName("precarriage")
    @Expose
    private String precarriage;
    @SerializedName("oncarriage")
    @Expose
    private String oncarriage;
    @SerializedName("loadtype")
    @Expose
    private String loadtype;
    @SerializedName("dischtype")
    @Expose
    private String dischtype;
    @SerializedName("voyagecode")
    @Expose
    private String voyagecode;
    @SerializedName("dateup")
    @Expose
    private String dateup;
    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;
    @SerializedName("version")
    @Expose
    private int version;
    @SerializedName("previousleg")
    @Expose
    private int previousleg;
    @SerializedName("groupcallid")
    @Expose
    private String groupcallid;
    @SerializedName("routeid")
    @Expose
    private String routeid;
    @SerializedName("callidloading")
    @Expose
    private String callidloading;
    @SerializedName("calliddischarge")
    @Expose
    private String calliddischarge;
    @SerializedName("dbound")
    @Expose
    private String dbound;
    @SerializedName("podterminal")
    @Expose
    private String podterminal;
    @SerializedName("polterminal")
    @Expose
    private String polterminal;

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
     * @return the leg_no
     */
    public int getLeg_no() {
        return leg_no;
    }

    /**
     * @param leg_no the leg_no to set
     */
    public void setLeg_no(int leg_no) {
        this.leg_no = leg_no;
    }

    /**
     * @return the bound
     */
    public String getBound() {
        return bound;
    }

    /**
     * @param bound the bound to set
     */
    public void setBound(String bound) {
        this.bound = bound;
    }

    /**
     * @return the servicecode
     */
    public String getServicecode() {
        return servicecode;
    }

    /**
     * @param servicecode the servicecode to set
     */
    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    /**
     * @return the vesselcode
     */
    public String getVesselcode() {
        return vesselcode;
    }

    /**
     * @param vesselcode the vesselcode to set
     */
    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    /**
     * @return the loadportid
     */
    public String getLoadportid() {
        return loadportid;
    }

    /**
     * @param loadportid the loadportid to set
     */
    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    /**
     * @return the dischargeportid
     */
    public String getDischargeportid() {
        return dischargeportid;
    }

    /**
     * @param dischargeportid the dischargeportid to set
     */
    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

    /**
     * @return the etddate
     */
    public String getEtddate() {
        return etddate;
    }

    /**
     * @param etddate the etddate to set
     */
    public void setEtddate(String etddate) {
        this.etddate = etddate;
    }

    /**
     * @return the etadate
     */
    public String getEtadate() {
        return etadate;
    }

    /**
     * @param etadate the etadate to set
     */
    public void setEtadate(String etadate) {
        this.etadate = etadate;
    }

    /**
     * @return the precarriage
     */
    public String getPrecarriage() {
        return precarriage;
    }

    /**
     * @param precarriage the precarriage to set
     */
    public void setPrecarriage(String precarriage) {
        this.precarriage = precarriage;
    }

    /**
     * @return the oncarriage
     */
    public String getOncarriage() {
        return oncarriage;
    }

    /**
     * @param oncarriage the oncarriage to set
     */
    public void setOncarriage(String oncarriage) {
        this.oncarriage = oncarriage;
    }

    /**
     * @return the loadtype
     */
    public String getLoadtype() {
        return loadtype;
    }

    /**
     * @param loadtype the loadtype to set
     */
    public void setLoadtype(String loadtype) {
        this.loadtype = loadtype;
    }

    /**
     * @return the dischtype
     */
    public String getDischtype() {
        return dischtype;
    }

    /**
     * @param dischtype the dischtype to set
     */
    public void setDischtype(String dischtype) {
        this.dischtype = dischtype;
    }

    /**
     * @return the voyagecode
     */
    public String getVoyagecode() {
        return voyagecode;
    }

    /**
     * @param voyagecode the voyagecode to set
     */
    public void setVoyagecode(String voyagecode) {
        this.voyagecode = voyagecode;
    }

    /**
     * @return the dateup
     */
    public String getDateup() {
        return dateup;
    }

    /**
     * @param dateup the dateup to set
     */
    public void setDateup(String dateup) {
        this.dateup = dateup;
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
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the previousLeg
     */
    public int getPreviousleg() {
        return previousleg;
    }

    /**
     * @param previousLeg the previousLeg to set
     */
    public void setPreviousleg(int previousleg) {
        this.previousleg = previousleg;
    }

    /**
     * @return the groupcallid
     */
    public String getGroupcallid() {
        return groupcallid;
    }

    /**
     * @param groupcallid the groupcallid to set
     */
    public void setGroupcallid(String groupcallid) {
        this.groupcallid = groupcallid;
    }

    /**
     * @return the routeid
     */
    public String getRouteid() {
        return routeid;
    }

    /**
     * @param routeid the routeid to set
     */
    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }

    /**
     * @return the legtypeid
     */
    public String getLegtypeid() {
        return legtypeid;
    }

    /**
     * @param legtypeid the legtypeid to set
     */
    public void setLegtypeid(String legtypeid) {
        this.legtypeid = legtypeid;
    }

    /**
     * @return the callidloading
     */
    public String getCallidloading() {
        return callidloading;
    }

    /**
     * @param callidloading the callidloading to set
     */
    public void setCallidloading(String callidloading) {
        this.callidloading = callidloading;
    }

    /**
     * @return the calliddischarge
     */
    public String getCalliddischarge() {
        return calliddischarge;
    }

    /**
     * @param calliddischarge the calliddischarge to set
     */
    public void setCalliddischarge(String calliddischarge) {
        this.calliddischarge = calliddischarge;
    }

    public String getDbound() {
        return dbound;
    }

    public void setDbound(String dbound) {
        this.dbound = dbound;
    }

    public String getPodterminal() {
        return podterminal;
    }

    public void setPodterminal(String podterminal) {
        this.podterminal = podterminal;
    }

    public String getPolterminal() {
        return polterminal;
    }

    public void setPolterminal(String polterminal) {
        this.polterminal = polterminal;
    }

}
