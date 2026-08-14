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
public class EcomBKGCommonModel implements Serializable {

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

    @SerializedName("editempbooking_header")
    @Expose
    private List<EcomBKGHeader> editempbooking_header = new ArrayList();

    @SerializedName("editempbooking_charges")
    @Expose
    private List<EcomBookingCharges> editempbooking_charges = new ArrayList<EcomBookingCharges>();

    @SerializedName("editempbooking_ics_new")
    @Expose
    private List<Editempbooking_ics_new> editempbooking_ics_new = new ArrayList<Editempbooking_ics_new>();

    public List<Editempbooking_ics_new> getEditempbooking_ics_new() {
        return editempbooking_ics_new;
    }

    public void setEditempbooking_ics_new(List<Editempbooking_ics_new> editempbooking_ics_new) {
        this.editempbooking_ics_new = editempbooking_ics_new;
    }

    public List<EcomBookingCharges> getEditempbooking_charges() {
        return editempbooking_charges;
    }

    public void setEditempbooking_charges(List<EcomBookingCharges> editempbooking_charges) {
        this.editempbooking_charges = editempbooking_charges;
    }

    public List<EcomBKGHeader> getEditempbooking_header() {
        return editempbooking_header;
    }

    public void setEditempbooking_header(List<EcomBKGHeader> editempbooking_header) {
        this.editempbooking_header = editempbooking_header;
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
}
