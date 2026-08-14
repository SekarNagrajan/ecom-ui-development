/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.ecom.vo.UnclearedCargoVo;

/**
 *
 * @author salomi.a
 */
public class CompleteModel implements Serializable {

    @SerializedName("booking")
    @Expose
    private List<Booking> booking = new ArrayList<Booking>();

    public List<Booking> getBooking() {
        return booking;
    }

    public void setBooking(List<Booking> booking) {
        this.booking = booking;
    }

    @SerializedName("booking_cust")
    @Expose
    private List<Booking_cust> booking_cust = new ArrayList<Booking_cust>();

    public List<Booking_cust> getBooking_cust() {
        return booking_cust;
    }

    public void setBooking_cust(List<Booking_cust> booking_cust) {
        this.booking_cust = booking_cust;
    }
//        
    @SerializedName("booking_cargo")
    @Expose
    private List<Booking_cargo> booking_cargo = new ArrayList<Booking_cargo>();

    public List<Booking_cargo> getBooking_cargo() {
        return booking_cargo;
    }

    public void setBooking_cargo(List<Booking_cargo> booking_cargo) {
        this.booking_cargo = booking_cargo;
    }
//        
//        @SerializedName("booking_reference")
//	@Expose
//	private List<Booking_reference> booking_reference = new ArrayList<Booking_reference>();
//
//
//	public List<Booking_reference> getBooking_reference() {
//		return booking_reference;
//	}
//	public void setBooking_reference(List<Booking_reference> booking_reference) {
//		this.booking_reference = booking_reference;
//	}

    @SerializedName("bl")
    @Expose
    private List<Bl> bl = new ArrayList<Bl>();

    public List<Bl> getBl() {
        return bl;
    }

    public void setBl(List<Bl> bl) {
        this.bl = bl;
    }

    @SerializedName("service")
    @Expose
    private List<Service> service = new ArrayList<Service>();

    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    @SerializedName("customermaster")
    @Expose
    private List<CustomerMaster> customermaster = new ArrayList<CustomerMaster>();

    public List<CustomerMaster> getCustomerMaster() {
        return customermaster;
    }

    public void setCustomerMaster(List<CustomerMaster> customermaster) {
        this.customermaster = customermaster;
    }

    @SerializedName("vesselmaster")
    @Expose
    private List<Vesselmaster> vesselmaster = new ArrayList<Vesselmaster>();

    public List<Vesselmaster> getVesselmaster() {
        return vesselmaster;
    }

    public void setVesselmaster(List<Vesselmaster> vesselmaster) {
        this.vesselmaster = vesselmaster;
    }

    @SerializedName("booking_commodity")
    @Expose
    private List<Booking_commodity> booking_commodity = new ArrayList<Booking_commodity>();

    public List<Booking_commodity> getBooking_commodity() {
        return booking_commodity;
    }

    public void setBooking_commodity(List<Booking_commodity> booking_commodity) {
        this.booking_commodity = booking_commodity;
    }

    @SerializedName("ports")
    @Expose
    private List<Ports> ports = new ArrayList<Ports>();

    public List<Ports> getPorts() {
        return ports;
    }

    public void setPorts(List<Ports> ports) {
        this.ports = ports;
    }

    @SerializedName("edimaster")
    @Expose
    private List<Edimaster> edimaster = new ArrayList<Edimaster>();

    public List<Edimaster> getEdimaster() {
        return edimaster;
    }

    public void setEdimaster(List<Edimaster> edimaster) {
        this.edimaster = edimaster;
    }

    @SerializedName("bl_cust")
    @Expose
    private List<Bl_cust> bl_cust = new ArrayList<Bl_cust>();

    public List<Bl_cust> getBl_cust() {
        return bl_cust;
    }

    public void setBl_cust(List<Bl_cust> bl_cust) {
        this.bl_cust = bl_cust;
    }

    @SerializedName("bl_header")
    @Expose
    private List<Bl_header> bl_header = new ArrayList<Bl_header>();

    public List<Bl_header> getBl_header() {
        return bl_header;
    }

    public void setBl_header(List<Bl_header> bl_header) {
        this.bl_header = bl_header;
    }

    @SerializedName("bl_cargo")
    @Expose
    private List<Bl_cargo> bl_cargo = new ArrayList<Bl_cargo>();

    public List<Bl_cargo> getBl_cargo() {
        return bl_cargo;
    }

    public void setBl_cargo(List<Bl_cargo> bl_cargo) {
        this.bl_cargo = bl_cargo;
    }

    @SerializedName("bl_commodity")
    @Expose
    private List<Bl_commodity> bl_commodity = new ArrayList<Bl_commodity>();

    public List<Bl_commodity> getBl_commodity() {
        return bl_commodity;
    }

    public void setBl_commodity(List<Bl_commodity> bl_commodity) {
        this.bl_commodity = bl_commodity;
    }

    @SerializedName("fcn_bl_cust")
    @Expose
    private List<Bl_cust> fcn_bl_cust = new ArrayList<Bl_cust>();

    public List<Bl_cust> getFcn_bl_cust() {
        return fcn_bl_cust;
    }

    public void setFcn_bl_cust(List<Bl_cust> fcn_bl_cust) {
        this.fcn_bl_cust = fcn_bl_cust;
    }

    @SerializedName("fcn_bl_cargo")
    @Expose
    private List<Bl_cargo> fcn_bl_cargo = new ArrayList<Bl_cargo>();

    public List<Bl_cargo> getFcn_bl_cargo() {
        return fcn_bl_cargo;
    }

    public void setFcn_bl_cargo(List<Bl_cargo> fcn_bl_cargo) {
        this.fcn_bl_cargo = fcn_bl_cargo;
    }
    
    @SerializedName("fcn_bl_header")
    @Expose
    private List<Bl_header> fcn_bl_header = new ArrayList<Bl_header>();

    public List<Bl_header> getFcn_bl_header() {
        return fcn_bl_header;
    }

    public void setFcn_bl_header(List<Bl_header> fcn_bl_header) {
        this.fcn_bl_header = fcn_bl_header;
    }
}
