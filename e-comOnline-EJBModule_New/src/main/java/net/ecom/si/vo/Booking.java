/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author salomi.a
 */
public class Booking implements Serializable {

    @SerializedName("bookReady")
    @Expose
    private String bookReady;

    @SerializedName("vesselcode")
    @Expose
    private String vesselcode;

    @SerializedName("bound")
    @Expose
    private String bound;

    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("shipperrefno")
    @Expose
    private String shipperrefno;

    @SerializedName("dischargeportid")
    @Expose
    private String dischargeportid;

    @SerializedName("custrefno")
    @Expose
    private String custrefno;

    @SerializedName("servicecode")
    @Expose
    private String servicecode;

    @SerializedName("polterminal")
    @Expose
    private String polterminal;

    @SerializedName("custid")
    @Expose
    private String custid;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("podterminal")
    @Expose
    private String podterminal;

    @SerializedName("loadportid")
    @Expose
    private String loadportid;

    @SerializedName("finalportid")
    @Expose
    private String finalportid;

    @SerializedName("originportid")
    @Expose
    private String originportid;

    @SerializedName("polat")
    @Expose
    private String polat;

    @SerializedName("voyagecode")
    @Expose
    private String voyagecode;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("custtypeid")
    @Expose
    private String custtypeid;

    @SerializedName("agencyrefno")
    @Expose
    private String agencyRefNo;

    public String getAgencyRefNo() {
        return agencyRefNo;
    }

    public void setAgencyRefNo(String agencyRefNo) {
        this.agencyRefNo = agencyRefNo;
    }

    public String getVesselcode() {
        return vesselcode;
    }

    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getShipperrefno() {
        return shipperrefno;
    }

    public void setShipperrefno(String shipperrefno) {
        this.shipperrefno = shipperrefno;
    }

    public String getDischargeportid() {
        return dischargeportid;
    }

    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

    public String getCustrefno() {
        return custrefno;
    }

    public void setCustrefno(String custrefno) {
        this.custrefno = custrefno;
    }

    public String getServicecode() {
        return servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    public String getPolterminal() {
        return polterminal;
    }

    public void setPolterminal(String polterminal) {
        this.polterminal = polterminal;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPodterminal() {
        return podterminal;
    }

    public void setPodterminal(String podterminal) {
        this.podterminal = podterminal;
    }

    public String getLoadportid() {
        return loadportid;
    }

    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    public String getFinalportid() {
        return finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }

    public String getOriginportid() {
        return originportid;
    }

    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    public String getPolat() {
        return polat;
    }

    public void setPolat(String polat) {
        this.polat = polat;
    }

    public String getVoyagecode() {
        return voyagecode;
    }

    public void setVoyagecode(String voyagecode) {
        this.voyagecode = voyagecode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCusttypeid() {
        return custtypeid;
    }

    public void setCusttypeid(String custtypeid) {
        this.custtypeid = custtypeid;
    }

    @SerializedName("oceanvslcode")
    @Expose
    private String oceanvslcode;

    @SerializedName("documentalagencyid")
    @Expose
    private String documentalagencyid;

    @SerializedName("bookdeadline")
    @Expose
    private String bookdeadline;

    @SerializedName("oncarriage")
    @Expose
    private String oncarriage;

    @SerializedName("precarriage")
    @Expose
    private String precarriage;

    @SerializedName("originportdesc")
    @Expose
    private String originportdesc;

    @SerializedName("podat")
    @Expose
    private String podat;

    @SerializedName("booktype")
    @Expose
    private String booktype;

    @SerializedName("agencyid")
    @Expose
    private String agencyid;

    @SerializedName("thirdparty")
    @Expose
    private String thirdparty;

    @SerializedName("oceanbound")
    @Expose
    private String oceanbound;

    @SerializedName("usercr")
    @Expose
    private String usercr;

    @SerializedName("finalportdesc")
    @Expose
    private String finalportdesc;

    @SerializedName("oceansercode")
    @Expose
    private String oceansercode;

    @SerializedName("transportserviceid")
    @Expose
    private String transportserviceid;

    @SerializedName("oceanvoyage")
    @Expose
    private String oceanvoyage;
    
    @SerializedName("intransitport")
    @Expose
    private String intransitport;
    
    @SerializedName("intransitportname")
    @Expose
    private String intransitportname;
    
    @SerializedName("transporttermid")
    @Expose
    private String transporttermid;

    public String getTransporttermid() {
        return transporttermid;
    }

    public void setTransporttermid(String transporttermid) {
        this.transporttermid = transporttermid;
    }

    public String getOceanvslcode() {
        return oceanvslcode;
    }

    public void setOceanvslcode(String oceanvslcode) {
        this.oceanvslcode = oceanvslcode;
    }

    public String getDocumentalagencyid() {
        return documentalagencyid;
    }

    public void setDocumentalagencyid(String documentalagencyid) {
        this.documentalagencyid = documentalagencyid;
    }

    public String getBookdeadline() {
        return bookdeadline;
    }

    public void setBookdeadline(String bookdeadline) {
        this.bookdeadline = bookdeadline;
    }

    public String getOncarriage() {
        return oncarriage;
    }

    public void setOncarriage(String oncarriage) {
        this.oncarriage = oncarriage;
    }

    public String getPrecarriage() {
        return precarriage;
    }

    public void setPrecarriage(String precarriage) {
        this.precarriage = precarriage;
    }

    public String getOriginportdesc() {
        return originportdesc;
    }

    public void setOriginportdesc(String originportdesc) {
        this.originportdesc = originportdesc;
    }

    public String getPodat() {
        return podat;
    }

    public void setPodat(String podat) {
        this.podat = podat;
    }

    public String getBooktype() {
        return booktype;
    }

    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }

    public String getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    public String getThirdparty() {
        return thirdparty;
    }

    public void setThirdparty(String thirdparty) {
        this.thirdparty = thirdparty;
    }

    public String getOceanbound() {
        return oceanbound;
    }

    public void setOceanbound(String oceanbound) {
        this.oceanbound = oceanbound;
    }

    public String getUsercr() {
        return usercr;
    }

    public void setUsercr(String usercr) {
        this.usercr = usercr;
    }

    public String getFinalportdesc() {
        return finalportdesc;
    }

    public void setFinalportdesc(String finalportdesc) {
        this.finalportdesc = finalportdesc;
    }

    public String getOceansercode() {
        return oceansercode;
    }

    public void setOceansercode(String oceansercode) {
        this.oceansercode = oceansercode;
    }

    public String getTransportserviceid() {
        return transportserviceid;
    }

    public void setTransportserviceid(String transportserviceid) {
        this.transportserviceid = transportserviceid;
    }

    public String getOceanvoyage() {
        return oceanvoyage;
    }

    public void setOceanvoyage(String oceanvoyage) {
        this.oceanvoyage = oceanvoyage;
    }

    @SerializedName("bookdate")
    @Expose
    private String bookdate;

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    public String getBookReady() {
        return bookReady;
    }

    public void setBookReady(String bookReady) {
        this.bookReady = bookReady;
    }

    public String getIntransitport() {
        return intransitport;
    }

    public void setIntransitport(String intransitport) {
        this.intransitport = intransitport;
    }

    public String getIntransitportname() {
        return intransitportname;
    }

    public void setIntransitportname(String intransitportname) {
        this.intransitportname = intransitportname;
    }

}
