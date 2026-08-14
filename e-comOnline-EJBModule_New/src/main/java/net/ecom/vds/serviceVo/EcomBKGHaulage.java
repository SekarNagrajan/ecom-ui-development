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
 * @author gobinath.t
 */
public class EcomBKGHaulage implements Serializable {

    @SerializedName("contactemail")
    @Expose
    private String contactemail;

    @SerializedName("loadlocation")
    @Expose
    private String loadlocation;

    @SerializedName("contactaddress")
    @Expose
    private String contactaddress;

    @SerializedName("contactperson")
    @Expose
    private String contactperson;

    @SerializedName("dropdate")
    @Expose
    private String dropdate;

    @SerializedName("stopsequence")
    @Expose
    private int stopsequence;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("referencenumber")
    @Expose
    private String referencenumber;

    @SerializedName("tmsclientcode")
    @Expose
    private String tmsclientcode;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("loadlocationname")
    @Expose
    private String loadlocationname;

    @SerializedName("doctype")
    @Expose
    private String doctype;

    @SerializedName("tmsrefno")
    @Expose
    private String tmsrefno;

    @SerializedName("eqpid")
    @Expose
    private String eqpid;

    @SerializedName("contactphone")
    @Expose
    private String contactphone;

    @SerializedName("overweight")
    @Expose
    private String overweight;

    @SerializedName("remarks")
    @Expose
    private String remarks;

    @SerializedName("eqptypeid")
    @Expose
    private String eqptypeid;

    public String getEqptypeid() {
        return eqptypeid;
    }

    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
    }

    public String getContactemail() {
        return contactemail;
    }

    public void setContactemail(String contactemail) {
        this.contactemail = contactemail;
    }

    public String getLoadlocation() {
        return loadlocation;
    }

    public void setLoadlocation(String loadlocation) {
        this.loadlocation = loadlocation;
    }

    public String getContactaddress() {
        return contactaddress;
    }

    public void setContactaddress(String contactaddress) {
        this.contactaddress = contactaddress;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getDropdate() {
        return dropdate;
    }

    public void setDropdate(String dropdate) {
        this.dropdate = dropdate;
    }

    public int getStopsequence() {
        return stopsequence;
    }

    public void setStopsequence(int stopsequence) {
        this.stopsequence = stopsequence;
    }

    public String getEdireaderid() {
        return edireaderid;
    }

    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    public String getReferencenumber() {
        return referencenumber;
    }

    public void setReferencenumber(String referencenumber) {
        this.referencenumber = referencenumber;
    }

    public String getTmsclientcode() {
        return tmsclientcode;
    }

    public void setTmsclientcode(String tmsclientcode) {
        this.tmsclientcode = tmsclientcode;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLoadlocationname() {
        return loadlocationname;
    }

    public void setLoadlocationname(String loadlocationname) {
        this.loadlocationname = loadlocationname;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getTmsrefno() {
        return tmsrefno;
    }

    public void setTmsrefno(String tmsrefno) {
        this.tmsrefno = tmsrefno;
    }

    public String getEqpid() {
        return eqpid;
    }

    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getOverweight() {
        return overweight;
    }

    public void setOverweight(String overweight) {
        this.overweight = overweight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
