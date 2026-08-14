/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vasanthakumar.r
 */
public class CommonBKGJaserModel implements Serializable {

    @SerializedName("ediisspevent")
    @Expose
    List<CommonBKGJasperVO> ediisspevent = new ArrayList();

    @SerializedName("fcn_bl")
    @Expose
    List<CommonBKGJasperVO> fcn_bl = new ArrayList();

    @SerializedName("bkgportcombination")
    @Expose
    List<CommonBKGJasperVO> bkgportcombination = new ArrayList();

    @SerializedName("booking")
    @Expose
    List<CommonBKGJasperVO> booking = new ArrayList();

    @SerializedName("booking_cargo")
    @Expose
    List<CommonBKGJasperVO> booking_cargo = new ArrayList();

    @SerializedName("booking_commodity")
    @Expose
    List<CommonBKGJasperVO> booking_commodity = new ArrayList();

    @SerializedName("booking_imo")
    @Expose
    List<CommonBKGJasperVO> booking_imo = new ArrayList();

    @SerializedName("booking_contacts")
    @Expose
    List<CommonBKGJasperVO> booking_contacts = new ArrayList();

    @SerializedName("bl")
    @Expose
    List<CommonBKGJasperVO> bl = new ArrayList();

    @SerializedName("tdndetail")
    @Expose
    List<CommonBKGJasperVO> tdndetail = new ArrayList();

    @SerializedName("operationdetail")
    @Expose
    List<CommonBKGJasperVO> operationdetail = new ArrayList();

    @SerializedName("bl_header")
    @Expose
    List<CommonBKGJasperVO> bl_header = new ArrayList();

    @SerializedName("bl_cargo")
    @Expose
    List<CommonBKGJasperVO> bl_cargo = new ArrayList();

    @SerializedName("bl_commodity")
    @Expose
    List<CommonBKGJasperVO> bl_commodity = new ArrayList();

    @SerializedName("bl_imo")
    @Expose
    List<CommonBKGJasperVO> bl_imo = new ArrayList();

    @SerializedName("bl_contacts")
    @Expose
    List<CommonBKGJasperVO> bl_contacts = new ArrayList();

    @SerializedName("booking_conf")
    @Expose
    List<EcomBkgPrintBookingSaveVo> booking_conf = new ArrayList();

    @SerializedName("booking_validation_commodity")
    @Expose
    List<CommonBKGJasperVO> booking_validation_commodity = new ArrayList();

    @SerializedName("booking_validation_cargo")
    @Expose
    List<CommonBKGJasperVO> booking_validation_cargo = new ArrayList();

    @SerializedName("booking_validation_cust")
    @Expose
    List<CommonBKGJasperVO> booking_validation_cust = new ArrayList();

    @SerializedName("ndgdetail")
    @Expose
    List<CommonBKGJasperVO> ndgdetail = new ArrayList();

    public List<CommonBKGJasperVO> getBooking_validation_commodity() {
        return this.booking_validation_commodity;
    }

    public void setBooking_validation_commodity(List<CommonBKGJasperVO> booking_validation_commodity) {
        this.booking_validation_commodity = booking_validation_commodity;
    }

    public List<CommonBKGJasperVO> getBooking_validation_cargo() {
        return this.booking_validation_cargo;
    }

    public void setBooking_validation_cargo(List<CommonBKGJasperVO> booking_validation_cargo) {
        this.booking_validation_cargo = booking_validation_cargo;
    }

    public List<CommonBKGJasperVO> getBooking_validation_cust() {
        return this.booking_validation_cust;
    }

    public void setBooking_validation_cust(List<CommonBKGJasperVO> booking_validation_cust) {
        this.booking_validation_cust = booking_validation_cust;
    }

    public List<EcomBkgPrintBookingSaveVo> getBooking_conf() {
        return this.booking_conf;
    }

    public void setBooking_conf(List<EcomBkgPrintBookingSaveVo> booking_conf) {
        this.booking_conf = booking_conf;
    }

    public List<CommonBKGJasperVO> getBooking() {
        return this.booking;
    }

    public void setBooking(List<CommonBKGJasperVO> booking) {
        this.booking = booking;
    }

    public List<CommonBKGJasperVO> getBl() {
        return this.bl;
    }

    public void setBl(List<CommonBKGJasperVO> bl) {
        this.bl = bl;
    }

    public List<CommonBKGJasperVO> getTdndetail() {
        return this.tdndetail;
    }

    public void setTdndetail(List<CommonBKGJasperVO> tdndetail) {
        this.tdndetail = tdndetail;
    }

    public List<CommonBKGJasperVO> getOperationdetail() {
        return this.operationdetail;
    }

    public void setOperationdetail(List<CommonBKGJasperVO> operationdetail) {
        this.operationdetail = operationdetail;
    }

    public List<CommonBKGJasperVO> getBl_header() {
        return this.bl_header;
    }

    public void setBl_header(List<CommonBKGJasperVO> bl_header) {
        this.bl_header = bl_header;
    }

    public List<CommonBKGJasperVO> getBooking_cargo() {
        return this.booking_cargo;
    }

    public void setBooking_cargo(List<CommonBKGJasperVO> booking_cargo) {
        this.booking_cargo = booking_cargo;
    }

    public List<CommonBKGJasperVO> getBooking_commodity() {
        return this.booking_commodity;
    }

    public void setBooking_commodity(List<CommonBKGJasperVO> booking_commodity) {
        this.booking_commodity = booking_commodity;
    }

    public List<CommonBKGJasperVO> getBooking_imo() {
        return this.booking_imo;
    }

    public void setBooking_imo(List<CommonBKGJasperVO> booking_imo) {
        this.booking_imo = booking_imo;
    }

    public List<CommonBKGJasperVO> getBooking_contacts() {
        return this.booking_contacts;
    }

    public void setBooking_contacts(List<CommonBKGJasperVO> booking_contacts) {
        this.booking_contacts = booking_contacts;
    }

    public List<CommonBKGJasperVO> getBl_cargo() {
        return this.bl_cargo;
    }

    public void setBl_cargo(List<CommonBKGJasperVO> bl_cargo) {
        this.bl_cargo = bl_cargo;
    }

    public List<CommonBKGJasperVO> getBl_commodity() {
        return this.bl_commodity;
    }

    public void setBl_commodity(List<CommonBKGJasperVO> bl_commodity) {
        this.bl_commodity = bl_commodity;
    }

    public List<CommonBKGJasperVO> getBl_imo() {
        return this.bl_imo;
    }

    public void setBl_imo(List<CommonBKGJasperVO> bl_imo) {
        this.bl_imo = bl_imo;
    }

    public List<CommonBKGJasperVO> getBl_contacts() {
        return this.bl_contacts;
    }

    public void setBl_contacts(List<CommonBKGJasperVO> bl_contacts) {
        this.bl_contacts = bl_contacts;
    }

    public List<CommonBKGJasperVO> getEdiisspevent() {
        return this.ediisspevent;
    }

    public void setEdiisspevent(List<CommonBKGJasperVO> ediisspevent) {
        this.ediisspevent = ediisspevent;
    }

    public List<CommonBKGJasperVO> getFcn_bl() {
        return this.fcn_bl;
    }

    public void setFcn_bl(List<CommonBKGJasperVO> fcn_bl) {
        this.fcn_bl = fcn_bl;
    }

    public List<CommonBKGJasperVO> getBkgportcombination() {
        return this.bkgportcombination;
    }

    public void setBkgportcombination(List<CommonBKGJasperVO> bkgportcombination) {
        this.bkgportcombination = bkgportcombination;
    }

    public List<CommonBKGJasperVO> getNdgdetail() {
        return this.ndgdetail;
    }

    public void setNdgdetail(List<CommonBKGJasperVO> ndgdetail) {
        this.ndgdetail = ndgdetail;
    }
}
