/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sasikumar.r
 */
public class EcomCommonModel {

    @SerializedName("editempbooking_reference")
    @Expose
    private List<EcomBKGReference> editempbooking_reference = new ArrayList();

    @SerializedName("editempbooking_remarks")
    @Expose
    private List<EcomBKGRemarks> editempbooking_remarks = new ArrayList();

    @SerializedName("edireadermaster")
    @Expose
    private List<EcomBKGReaderMaster> edireadermaster = new ArrayList();

    @SerializedName("editempbooking_cust")
    @Expose
    private List<EcomBKGCustomer> editempbooking_cust = new ArrayList();

    @SerializedName("editempbooking_commodity")
    @Expose
    private List<EcomBKGCommodity> editempbooking_commodity = new ArrayList();

    @SerializedName("editempbooking")
    @Expose
    private List<EcomBKGMasterVO> editempbooking = new ArrayList();

    @SerializedName("editempbooking_routing")
    @Expose
    private List<EcomBKGRouting> editempbooking_routing = new ArrayList();

    @SerializedName("editempbooking_leg")
    @Expose
    private List<EcomBKGLeg> editempbooking_leg = new ArrayList();

    @SerializedName("editempbooking_imo")
    @Expose
    private List<EcomBKGCommDG> editempbooking_imo = new ArrayList();

    @SerializedName("editempbooking_cargo")
    @Expose
    private List<EcomBKGCargo> editempbooking_cargo = new ArrayList();

    @SerializedName("edidocstatus")
    @Expose
    private List<EcomBKGEDIDocStatus> edidocstatus = new ArrayList();

    @SerializedName("customercontacts")
    @Expose
    private List<CustomerContacts> customercontacts = new ArrayList();

    @SerializedName("bl_reference")
    @Expose
    private List<EcomBLReference> bl_reference = new ArrayList();

    @SerializedName("booking")
    @Expose
    private List<Booking> booking = new ArrayList();

    @SerializedName("editempbooking_haulage")
    @Expose
    private List<EcomBKGHaulage> editempbooking_haulage = new ArrayList<EcomBKGHaulage>();

    @SerializedName("editempbooking_conf")
    @Expose
    private List<EcomBKGConf> editempbooking_conf = new ArrayList<EcomBKGConf>();

    @SerializedName("editempbooking_insurance")
    @Expose
    private List<EcomInsuranceVo> editempbooking_insurance = new ArrayList<EcomInsuranceVo>();

    public List<EcomInsuranceVo> getEditempbooking_insurance() {
        return editempbooking_insurance;
    }

    public void setEditempbooking_insurance(List<EcomInsuranceVo> editempbooking_insurance) {
        this.editempbooking_insurance = editempbooking_insurance;
    }

    @SerializedName("fsdoclog")
    @Expose
    private List<Fsdoclog> fsdoclog = new ArrayList<Fsdoclog>();

    public List<Fsdoclog> getFsdoclog() {
        return fsdoclog;
    }

    public void setFsdoclog(List<Fsdoclog> fsdoclog) {
        this.fsdoclog = fsdoclog;
    }

    public List<EcomBKGConf> getEditempbooking_conf() {
        return editempbooking_conf;
    }

    public void setEditempbooking_conf(List<EcomBKGConf> editempbooking_conf) {
        this.editempbooking_conf = editempbooking_conf;
    }

    public List<EcomBKGHaulage> getEditempbooking_haulage() {
        return editempbooking_haulage;
    }

    public void setEditempbooking_haulage(List<EcomBKGHaulage> editempbooking_haulage) {
        this.editempbooking_haulage = editempbooking_haulage;
    }

    public List<Booking> getBooking() {
        return booking;
    }

    public void setBooking(List<Booking> booking) {
        this.booking = booking;
    }

    public List<EcomBLReference> getBl_reference() {
        return bl_reference;
    }

    public void setBl_reference(List<EcomBLReference> bl_reference) {
        this.bl_reference = bl_reference;
    }

    /**
     * @return the editempbooking_reference
     */
    public List<EcomBKGReference> getEditempbooking_reference() {
        return editempbooking_reference;
    }

    /**
     * @param editempbooking_reference the editempbooking_reference to set
     */
    public void setEditempbooking_reference(List<EcomBKGReference> editempbooking_reference) {
        this.editempbooking_reference = editempbooking_reference;
    }

    /**
     * @return the editempbooking_remarks
     */
    public List<EcomBKGRemarks> getEditempbooking_remarks() {
        return editempbooking_remarks;
    }

    /**
     * @param editempbooking_remarks the editempbooking_remarks to set
     */
    public void setEditempbooking_remarks(List<EcomBKGRemarks> editempbooking_remarks) {
        this.editempbooking_remarks = editempbooking_remarks;
    }

    /**
     * @return the edireadermaster
     */
    public List<EcomBKGReaderMaster> getEdireadermaster() {
        return edireadermaster;
    }

    /**
     * @param edireadermaster the edireadermaster to set
     */
    public void setEdireadermaster(List<EcomBKGReaderMaster> edireadermaster) {
        this.edireadermaster = edireadermaster;
    }

    /**
     * @return the editempbooking_cust
     */
    public List<EcomBKGCustomer> getEditempbooking_cust() {
        return editempbooking_cust;
    }

    /**
     * @param editempbooking_cust the editempbooking_cust to set
     */
    public void setEditempbooking_cust(List<EcomBKGCustomer> editempbooking_cust) {
        this.editempbooking_cust = editempbooking_cust;
    }

    /**
     * @return the editempbooking_commodity
     */
    public List<EcomBKGCommodity> getEditempbooking_commodity() {
        return editempbooking_commodity;
    }

    /**
     * @param editempbooking_commodity the editempbooking_commodity to set
     */
    public void setEditempbooking_commodity(List<EcomBKGCommodity> editempbooking_commodity) {
        this.editempbooking_commodity = editempbooking_commodity;
    }

    /**
     * @return the editempbooking
     */
    public List<EcomBKGMasterVO> getEditempbooking() {
        return editempbooking;
    }

    /**
     * @param editempbooking the editempbooking to set
     */
    public void setEditempbooking(List<EcomBKGMasterVO> editempbooking) {
        this.editempbooking = editempbooking;
    }

    /**
     * @return the editempbooking_routing
     */
    public List<EcomBKGRouting> getEditempbooking_routing() {
        return editempbooking_routing;
    }

    /**
     * @param editempbooking_routing the editempbooking_routing to set
     */
    public void setEditempbooking_routing(List<EcomBKGRouting> editempbooking_routing) {
        this.editempbooking_routing = editempbooking_routing;
    }

    /**
     * @return the editempbooking_leg
     */
    public List<EcomBKGLeg> getEditempbooking_leg() {
        return editempbooking_leg;
    }

    /**
     * @param editempbooking_leg the editempbooking_leg to set
     */
    public void setEditempbooking_leg(List<EcomBKGLeg> editempbooking_leg) {
        this.editempbooking_leg = editempbooking_leg;
    }

    /**
     * @return the editempbooking_imo
     */
    public List<EcomBKGCommDG> getEditempbooking_imo() {
        return editempbooking_imo;
    }

    /**
     * @param editempbooking_imo the editempbooking_imo to set
     */
    public void setEditempbooking_imo(List<EcomBKGCommDG> editempbooking_imo) {
        this.editempbooking_imo = editempbooking_imo;
    }

    /**
     * @return the editempbooking_cargo
     */
    public List<EcomBKGCargo> getEditempbooking_cargo() {
        return editempbooking_cargo;
    }

    /**
     * @param editempbooking_cargo the editempbooking_cargo to set
     */
    public void setEditempbooking_cargo(List<EcomBKGCargo> editempbooking_cargo) {
        this.editempbooking_cargo = editempbooking_cargo;
    }

    /**
     * @return the edidocstatus
     */
    public List<EcomBKGEDIDocStatus> getEdidocstatus() {
        return edidocstatus;
    }

    /**
     * @param edidocstatus the edidocstatus to set
     */
    public void setEdidocstatus(List<EcomBKGEDIDocStatus> edidocstatus) {
        this.edidocstatus = edidocstatus;
    }

    /**
     * @return the customercontacts
     */
    public List<CustomerContacts> getCustomercontacts() {
        return customercontacts;
    }

    /**
     * @param customercontacts the customercontacts to set
     */
    public void setCustomercontacts(List<CustomerContacts> customercontacts) {
        this.customercontacts = customercontacts;
    }

}
