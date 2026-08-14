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
public class EcomBKGMasterVO implements Serializable {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("book_no")
    @Expose
    private String book_no;
    @SerializedName("agencyid")
    @Expose
    private String agencyid;
    @SerializedName("oceanvslcode")
    @Expose
    private String oceanvslcode;
    @SerializedName("oceanvoyage")
    @Expose
    private String oceanvoyage;
    @SerializedName("oceansercode")
    @Expose
    private String oceansercode;
    @SerializedName("oceanbound")
    @Expose
    private String oceanbound;
    @SerializedName("servicecode")
    @Expose
    private String servicecode;
    @SerializedName("vesselcode")
    @Expose
    private String vesselcode;
    @SerializedName("voyagecode")
    @Expose
    private String voyagecode;
    @SerializedName("bound")
    @Expose
    private String bound;
    @SerializedName("originportid")
    @Expose
    private String originportid;
    @SerializedName("finalportid")
    @Expose
    private String finalportid;
    @SerializedName("loadportid")
    @Expose
    private String loadportid;
    @SerializedName("dischargeportid")
    @Expose
    private String dischargeportid;
    @SerializedName("polterminal")
    @Expose
    private String polterminal;
    @SerializedName("polat")
    @Expose
    private String polat;
    @SerializedName("podterminal")
    @Expose
    private String podterminal;
    @SerializedName("podat")
    @Expose
    private String podat;
    @SerializedName("bookdate")
    @Expose
    private String bookdate;
    @SerializedName("bookready")
    @Expose
    private String bookready;
    @SerializedName("precarriage")
    @Expose
    private String precarriage;
    @SerializedName("oncarriage")
    @Expose
    private String oncarriage;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("usercr")
    @Expose
    private String usercr;
    @SerializedName("datecr")
    @Expose
    private String datecr;
    @SerializedName("messagetype")
    @Expose
    private String messagetype;
    @SerializedName("transtype")
    @Expose
    private String transtype;
    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;
    @SerializedName("documentalagencyid")
    @Expose
    private String documentalagencyid;
    @SerializedName("agencyissueid")
    @Expose
    private String agencyissueid;
    @SerializedName("agencypolid")
    @Expose
    private String agencypolid;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("firstsvctype")
    @Expose
    private String firstsvctype;
    @SerializedName("booktype")
    @Expose
    private String booktype;
    @SerializedName("crossbook")
    @Expose
    private String crossBook;
    @SerializedName("originportdesc")
    @Expose
    private String originportdesc;
    @SerializedName("finalportdesc")
    @Expose
    private String finalportdesc;
    @SerializedName("shipment_type")
    @Expose
    private String shipment_type;
    @SerializedName("paymenttermsid")
    @Expose
    private String paymenttermsid;
    @SerializedName("booksource")
    @Expose
    private String booksource;
    @SerializedName("messagesender")
    @Expose
    private String messagesender;
    @SerializedName("carriercode")
    @Expose
    private String carriercode;
    @SerializedName("callid")
    @Expose
    private String callid;
    @SerializedName("custremarks")
    @Expose
    private String custremarks;
    @SerializedName("agencyrefno")
    @Expose
    private String agencyrefno;
    @SerializedName("transportserviceid")
    @Expose
    private String transportserviceid;
    @SerializedName("tbook_no")
    @Expose
    private String tbook_no;
    @SerializedName("intransitport")
    @Expose
    private String intransitport;
    @SerializedName("intransitportname")
    @Expose
    private String intransitportname;
    @SerializedName("tbl_no")
    @Expose
    private String tbl_no;
    @SerializedName("nat_code")
    @Expose
    private String nat_code;

    @SerializedName("transporttermid")
    @Expose
    private String transporttermid;

    @SerializedName("fcnno")
    @Expose
    private String fcnno;

    @SerializedName("cargoprotectid")
    @Expose
    private String cargoprotectid;

    @SerializedName("ensfiling")
    @Expose
    private String ensfiling;

    @SerializedName("t2lfiling")
    @Expose
    private String t2lfiling;

    @SerializedName("nvocc")
    @Expose
    private String nvocc;

    @SerializedName("enstype")
    @Expose
    private String ensdoctype;

    public String getEnsdoctype() {
        return ensdoctype;
    }

    public void setEnsdoctype(String ensdoctype) {
        this.ensdoctype = ensdoctype;
    }

    public String getTransporttermid() {
        return transporttermid;
    }

    public void setTransporttermid(String transporttermid) {
        this.transporttermid = transporttermid;
    }

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
     * @return the agencyid
     */
    public String getAgencyid() {
        return agencyid;
    }

    /**
     * @param agencyid the agencyid to set
     */
    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    /**
     * @return the oceanvslcode
     */
    public String getOceanvslcode() {
        return oceanvslcode;
    }

    /**
     * @param oceanvslcode the oceanvslcode to set
     */
    public void setOceanvslcode(String oceanvslcode) {
        this.oceanvslcode = oceanvslcode;
    }

    /**
     * @return the oceanvoyage
     */
    public String getOceanvoyage() {
        return oceanvoyage;
    }

    /**
     * @param oceanvoyage the oceanvoyage to set
     */
    public void setOceanvoyage(String oceanvoyage) {
        this.oceanvoyage = oceanvoyage;
    }

    /**
     * @return the oceansercode
     */
    public String getOceansercode() {
        return oceansercode;
    }

    /**
     * @param oceansercode the oceansercode to set
     */
    public void setOceansercode(String oceansercode) {
        this.oceansercode = oceansercode;
    }

    /**
     * @return the oceanbound
     */
    public String getOceanbound() {
        return oceanbound;
    }

    /**
     * @param oceanbound the oceanbound to set
     */
    public void setOceanbound(String oceanbound) {
        this.oceanbound = oceanbound;
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
     * @return the originportid
     */
    public String getOriginportid() {
        return originportid;
    }

    /**
     * @param originportid the originportid to set
     */
    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    /**
     * @return the finalportid
     */
    public String getFinalportid() {
        return finalportid;
    }

    /**
     * @param finalportid the finalportid to set
     */
    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
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
     * @return the polterminal
     */
    public String getPolterminal() {
        return polterminal;
    }

    /**
     * @param polterminal the polterminal to set
     */
    public void setPolterminal(String polterminal) {
        this.polterminal = polterminal;
    }

    /**
     * @return the polat
     */
    public String getPolat() {
        return polat;
    }

    /**
     * @param polat the polat to set
     */
    public void setPolat(String polat) {
        this.polat = polat;
    }

    /**
     * @return the podterminal
     */
    public String getPodterminal() {
        return podterminal;
    }

    /**
     * @param podterminal the podterminal to set
     */
    public void setPodterminal(String podterminal) {
        this.podterminal = podterminal;
    }

    /**
     * @return the podat
     */
    public String getPodat() {
        return podat;
    }

    /**
     * @param podat the podat to set
     */
    public void setPodat(String podat) {
        this.podat = podat;
    }

    /**
     * @return the bookdate
     */
    public String getBookdate() {
        return bookdate;
    }

    /**
     * @param bookdate the bookdate to set
     */
    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    /**
     * @return the bookready
     */
    public String getBookready() {
        return bookready;
    }

    /**
     * @param bookready the bookready to set
     */
    public void setBookready(String bookready) {
        this.bookready = bookready;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the usercr
     */
    public String getUsercr() {
        return usercr;
    }

    /**
     * @param usercr the usercr to set
     */
    public void setUsercr(String usercr) {
        this.usercr = usercr;
    }

    /**
     * @return the datecr
     */
    public String getDatecr() {
        return datecr;
    }

    /**
     * @param datecr the datecr to set
     */
    public void setDatecr(String datecr) {
        this.datecr = datecr;
    }

    /**
     * @return the messagetype
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * @param messagetype the messagetype to set
     */
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    /**
     * @return the transtype
     */
    public String getTranstype() {
        return transtype;
    }

    /**
     * @param transtype the transtype to set
     */
    public void setTranstype(String transtype) {
        this.transtype = transtype;
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
     * @return the documentalagencyid
     */
    public String getDocumentalagencyid() {
        return documentalagencyid;
    }

    /**
     * @param documentalagencyid the documentalagencyid to set
     */
    public void setDocumentalagencyid(String documentalagencyid) {
        this.documentalagencyid = documentalagencyid;
    }

    /**
     * @return the agencyissueid
     */
    public String getAgencyissueid() {
        return agencyissueid;
    }

    /**
     * @param agencyissueid the agencyissueid to set
     */
    public void setAgencyissueid(String agencyissueid) {
        this.agencyissueid = agencyissueid;
    }

    /**
     * @return the agencypolid
     */
    public String getAgencypolid() {
        return agencypolid;
    }

    /**
     * @param agencypolid the agencypolid to set
     */
    public void setAgencypolid(String agencypolid) {
        this.agencypolid = agencypolid;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the firstsvctype
     */
    public String getFirstsvctype() {
        return firstsvctype;
    }

    /**
     * @param firstsvctype the firstsvctype to set
     */
    public void setFirstsvctype(String firstsvctype) {
        this.firstsvctype = firstsvctype;
    }

    /**
     * @return the booktype
     */
    public String getBooktype() {
        return booktype;
    }

    /**
     * @param booktype the booktype to set
     */
    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }

    /**
     * @return the crossBook
     */
    public String getCrossBook() {
        return crossBook;
    }

    /**
     * @param crossBook the crossBook to set
     */
    public void setCrossBook(String crossBook) {
        this.crossBook = crossBook;
    }

    /**
     * @return the originportdesc
     */
    public String getOriginportdesc() {
        return originportdesc;
    }

    /**
     * @param originportdesc the originportdesc to set
     */
    public void setOriginportdesc(String originportdesc) {
        this.originportdesc = originportdesc;
    }

    /**
     * @return the finalportdesc
     */
    public String getFinalportdesc() {
        return finalportdesc;
    }

    /**
     * @param finalportdesc the finalportdesc to set
     */
    public void setFinalportdesc(String finalportdesc) {
        this.finalportdesc = finalportdesc;
    }

    /**
     * @return the shipment_type
     */
    public String getShipment_type() {
        return shipment_type;
    }

    /**
     * @param shipment_type the shipment_type to set
     */
    public void setShipment_type(String shipment_type) {
        this.shipment_type = shipment_type;
    }

    /**
     * @return the paymenttermsid
     */
    public String getPaymenttermsid() {
        return paymenttermsid;
    }

    /**
     * @param paymenttermsid the paymenttermsid to set
     */
    public void setPaymenttermsid(String paymenttermsid) {
        this.paymenttermsid = paymenttermsid;
    }

    /**
     * @return the booksource
     */
    public String getBooksource() {
        return booksource;
    }

    /**
     * @param booksource the booksource to set
     */
    public void setBooksource(String booksource) {
        this.booksource = booksource;
    }

    /**
     * @return the messagesender
     */
    public String getMessagesender() {
        return messagesender;
    }

    /**
     * @param messagesender the messagesender to set
     */
    public void setMessagesender(String messagesender) {
        this.messagesender = messagesender;
    }

    /**
     * @return the carriercode
     */
    public String getCarriercode() {
        return carriercode;
    }

    /**
     * @param carriercode the carriercode to set
     */
    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
    }

    /**
     * @return the callid
     */
    public String getCallid() {
        return callid;
    }

    /**
     * @param callid the callid to set
     */
    public void setCallid(String callid) {
        this.callid = callid;
    }

    /**
     * @return the custremarks
     */
    public String getCustremarks() {
        return custremarks;
    }

    /**
     * @param custremarks the custremarks to set
     */
    public void setCustremarks(String custremarks) {
        this.custremarks = custremarks;
    }

    /**
     * @return the agencyrefno
     */
    public String getAgencyrefno() {
        return agencyrefno;
    }

    /**
     * @param agencyrefno the agencyrefno to set
     */
    public void setAgencyrefno(String agencyrefno) {
        this.agencyrefno = agencyrefno;
    }

    /**
     * @return the transportserviceid
     */
    public String getTransportserviceid() {
        return transportserviceid;
    }

    /**
     * @param transportserviceid the transportserviceid to set
     */
    public void setTransportserviceid(String transportserviceid) {
        this.transportserviceid = transportserviceid;
    }

    /**
     * @return the tbook_no
     */
    public String getTbook_no() {
        return tbook_no;
    }

    /**
     * @param tbook_no the tbook_no to set
     */
    public void setTbook_no(String tbook_no) {
        this.tbook_no = tbook_no;
    }

    /**
     * @return the intransitport
     */
    public String getIntransitport() {
        return intransitport;
    }

    /**
     * @param intransitport the intransitport to set
     */
    public void setIntransitport(String intransitport) {
        this.intransitport = intransitport;
    }

    /**
     * @return the intransitportname
     */
    public String getIntransitportname() {
        return intransitportname;
    }

    /**
     * @param intransitportname the intransitportname to set
     */
    public void setIntransitportname(String intransitportname) {
        this.intransitportname = intransitportname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTbl_no() {
        return tbl_no;
    }

    public void setTbl_no(String tbl_no) {
        this.tbl_no = tbl_no;
    }

    /**
     * @return the nat_code
     */
    public String getNat_code() {
        return nat_code;
    }

    /**
     * @param nat_code the nat_code to set
     */
    public void setNat_code(String nat_code) {
        this.nat_code = nat_code;
    }

    public String getFcnno() {
        return fcnno;
    }

    public void setFcnno(String fcnno) {
        this.fcnno = fcnno;
    }

    public String getCargoprotectid() {
        return cargoprotectid;
    }

    public void setCargoprotectid(String cargoprotectid) {
        this.cargoprotectid = cargoprotectid;
    }

    public String getEnsfiling() {
        return ensfiling;
    }

    public void setEnsfiling(String ensfiling) {
        this.ensfiling = ensfiling;
    }

    public String getT2lfiling() {
        return t2lfiling;
    }

    public void setT2lfiling(String t2lfiling) {
        this.t2lfiling = t2lfiling;
    }

    public String getNvocc() {
        return nvocc;
    }

    public void setNvocc(String nvocc) {
        this.nvocc = nvocc;
    }

}
