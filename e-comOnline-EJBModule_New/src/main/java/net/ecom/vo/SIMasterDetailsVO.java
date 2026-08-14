package net.ecom.vo;

import java.io.Serializable;

public class SIMasterDetailsVO implements Serializable {
    private String bookNo = null;
    private String sino = null;
    private String pol = null;
    private String pla = null;
    private String pod = null;
    private String pld = null;
    private String bookDate = null;
    private String transportService = null;
    private String shipmentTerm = null;
    private String emptyPickUP = null;
    private String originPortDesc = null;
    private String finalPortDesc = null;
    private String arrivalDate = null;
    private String deptDate = null;
    private String service = null;
    private String vessel = null;
    private String voyage = null;
    private String bound = null;

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getDeptDate() {
        return deptDate;
    }

    public void setDeptDate(String deptDate) {
        this.deptDate = deptDate;
    }

    public String getEmptyPickUP() {
        return emptyPickUP;
    }

    public void setEmptyPickUP(String emptyPickUP) {
        this.emptyPickUP = emptyPickUP;
    }

    public String getFinalPortDesc() {
        return finalPortDesc;
    }

    public void setFinalPortDesc(String finalPortDesc) {
        this.finalPortDesc = finalPortDesc;
    }

    public String getOriginPortDesc() {
        return originPortDesc;
    }

    public void setOriginPortDesc(String originPortDesc) {
        this.originPortDesc = originPortDesc;
    }

    public String getPla() {
        return pla;
    }

    public void setPla(String pla) {
        this.pla = pla;
    }

    public String getPld() {
        return pld;
    }

    public void setPld(String pld) {
        this.pld = pld;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getShipmentTerm() {
        return shipmentTerm;
    }

    public void setShipmentTerm(String shipmentTerm) {
        this.shipmentTerm = shipmentTerm;
    }

    public String getSino() {
        return sino;
    }

    public void setSino(String sino) {
        this.sino = sino;
    }

    public String getTransportService() {
        return transportService;
    }

    public void setTransportService(String transportService) {
        this.transportService = transportService;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

}
