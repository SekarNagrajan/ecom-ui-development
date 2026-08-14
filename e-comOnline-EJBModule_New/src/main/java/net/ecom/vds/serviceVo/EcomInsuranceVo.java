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
public class EcomInsuranceVo implements Serializable {

    @SerializedName("commodity")
    @Expose
    private String commodity;

    @SerializedName("policy_no")
    @Expose
    private String policy_no;

    @SerializedName("quoteno")
    @Expose
    private String quoteno;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    @SerializedName("policystatus")
    @Expose
    private String policystatus;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("insurance_amount")
    @Expose
    private float insurance_amount;

    @SerializedName("lrpbl_no")
    @Expose
    private String lrpbl_no;

    @SerializedName("partytype")
    @Expose
    private String partytype;

    @SerializedName("eqpid")
    @Expose
    private String eqpid;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("lrpbook_no")
    @Expose
    private String lrpbook_no;

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getPolicy_no() {
        return policy_no;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public String getQuoteno() {
        return quoteno;
    }

    public void setQuoteno(String quoteno) {
        this.quoteno = quoteno;
    }

    public String getEdireaderid() {
        return edireaderid;
    }

    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

    public String getPolicystatus() {
        return policystatus;
    }

    public void setPolicystatus(String policystatus) {
        this.policystatus = policystatus;
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

    public float getInsurance_amount() {
        return insurance_amount;
    }

    public void setInsurance_amount(float insurance_amount) {
        this.insurance_amount = insurance_amount;
    }

    public String getLrpbl_no() {
        return lrpbl_no;
    }

    public void setLrpbl_no(String lrpbl_no) {
        this.lrpbl_no = lrpbl_no;
    }

    public String getPartytype() {
        return partytype;
    }

    public void setPartytype(String partytype) {
        this.partytype = partytype;
    }

    public String getEqpid() {
        return eqpid;
    }

    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLrpbook_no() {
        return lrpbook_no;
    }

    public void setLrpbook_no(String lrpbook_no) {
        this.lrpbook_no = lrpbook_no;
    }

}
