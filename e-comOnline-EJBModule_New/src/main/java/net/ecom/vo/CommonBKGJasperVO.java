/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

/**
 *
 * @author vasanthakumar.r
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CommonBKGJasperVO implements Serializable {

    @SerializedName("refno")
    @Expose
    String refno;

    @SerializedName("bookagency")
    @Expose
    String bookagency;

    @SerializedName("cruser")
    @Expose
    String cruser;

    @SerializedName("chguser")
    @Expose
    String chguser;

    @SerializedName("seal1no")
    @Expose
    String seal1no;

    @SerializedName("cblno")
    @Expose
    String cblno;

    @SerializedName("eqpid")
    @Expose
    String eqpid;

    @SerializedName("service")
    @Expose
    String service;

    @SerializedName("pol")
    @Expose
    String pol;

    @SerializedName("pod")
    @Expose
    String pod;

    @SerializedName("bound")
    @Expose
    String bound;

    @SerializedName("datecr")
    @Expose
    String datecr;

    @SerializedName("edimode")
    @Expose
    String edimode;

    @SerializedName("doctype")
    @Expose
    String doctype;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("isprocessed")
    @Expose
    String isProcessed;

    @SerializedName("crdate")
    @Expose
    String crdate;

    @SerializedName("book_no")
    @Expose
    String book_no;

    @SerializedName("sendtoecom")
    @Expose
    String sendToEcom;

    @SerializedName("servicecode")
    @Expose
    String servicecode;

    @SerializedName("vesselcode")
    @Expose
    String vesselcode;

    @SerializedName("voyagecode")
    @Expose
    String voyagecode;

    @SerializedName("grpcallid")
    @Expose
    String grpcallid;

    @SerializedName("oceansercode")
    @Expose
    String oceansercode;

    @SerializedName("oceanvslcode")
    @Expose
    String oceanvslcode;

    @SerializedName("oceanvoyage")
    @Expose
    String oceanvoyage;

    @SerializedName("oceanbound")
    @Expose
    String oceanbound;

    @SerializedName("polterminal")
    @Expose
    String polterminal;

    @SerializedName("polat")
    @Expose
    String polat;

    @SerializedName("originportid")
    @Expose
    String originportid;

    @SerializedName("loadportid")
    @Expose
    String loadportid;

    @SerializedName("finalportid")
    @Expose
    String finalportid;

    @SerializedName("dischargeportid")
    @Expose
    String dischargeportid;

    @SerializedName("podterminal")
    @Expose
    String podterminal;

    @SerializedName("hbookno")
    @Expose
    String hbookno;

    @SerializedName("vessel")
    @Expose
    String vessel;

    @SerializedName("voyage")
    @Expose
    String voyage;

    @SerializedName("pla")
    @Expose
    String pla;

    @SerializedName("pld")
    @Expose
    String pld;

    @SerializedName("blpodterminal")
    @Expose
    String blpodterminal;

    @SerializedName("deliveryport")
    @Expose
    String deliveryport;

    @SerializedName("blpod")
    @Expose
    String blpod;

    @SerializedName("blpolterminal")
    @Expose
    String blpolterminal;

    @SerializedName("originport")
    @Expose
    String originport;

    @SerializedName("blpol")
    @Expose
    String blpol;

    @SerializedName("bl_no")
    @Expose
    String bl_no;

    @SerializedName("precarriage")
    @Expose
    String precarriage;

    @SerializedName("oncarriage")
    @Expose
    String oncarriage;

    @SerializedName("transportserviceid")
    @Expose
    String transportserviceid;

    @SerializedName("bookno")
    @Expose
    String bookno;

    @SerializedName("containerno")
    @Expose
    String containerno;

    @SerializedName("isdummy")
    @Expose
    String isdummy;

    @SerializedName("movetype")
    @Expose
    String movetype;

    @SerializedName("operationdno")
    @Expose
    String operationdno;

    @SerializedName("cid")
    @Expose
    String cid;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("eqptypeid")
    @Expose
    String eqptypeid;

    @SerializedName("equipmenttype")
    @Expose
    String equipmenttype;

    @SerializedName("cargoweight")
    @Expose
    String cargoweight;

    @SerializedName("grossweight")
    @Expose
    String grossweight;

    @SerializedName("equiptype")
    @Expose
    String equiptype;

    @SerializedName("commoditycode")
    @Expose
    String commoditycode;

    @SerializedName("customercode")
    @Expose
    String customercode;

    @SerializedName("sid")
    @Expose
    String sid;

    @SerializedName("dgno")
    @Expose
    String dgno;

    @SerializedName("imoflag")
    @Expose
    String imoflag;

    @SerializedName("confirmeduser")
    @Expose
    String confirmeduser;

    @SerializedName("confirmeddate")
    @Expose
    String confirmeddate;

    public String getEquiptype() {
        return this.equiptype;
    }

    public void setEquiptype(String equiptype) {
        this.equiptype = equiptype;
    }

    public String getCommoditycode() {
        return this.commoditycode;
    }

    public void setCommoditycode(String commoditycode) {
        this.commoditycode = commoditycode;
    }

    public String getCustomercode() {
        return this.customercode;
    }

    public void setCustomercode(String customercode) {
        this.customercode = customercode;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCargoweight() {
        return this.cargoweight;
    }

    public void setCargoweight(String cargoweight) {
        this.cargoweight = cargoweight;
    }

    public String getGrossweight() {
        return this.grossweight;
    }

    public void setGrossweight(String grossweight) {
        this.grossweight = grossweight;
    }

    public String getEqptypeid() {
        return this.eqptypeid;
    }

    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
    }

    public String getEquipmenttype() {
        return this.equipmenttype;
    }

    public void setEquipmenttype(String equipmenttype) {
        this.equipmenttype = equipmenttype;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getOperationdno() {
        return this.operationdno;
    }

    public void setOperationdno(String operationdno) {
        this.operationdno = operationdno;
    }

    public String getOncarriage() {
        return this.oncarriage;
    }

    public void setOncarriage(String oncarriage) {
        this.oncarriage = oncarriage;
    }

    public String getTransportserviceid() {
        return this.transportserviceid;
    }

    public void setTransportserviceid(String transportserviceid) {
        this.transportserviceid = transportserviceid;
    }

    public String getBookno() {
        return this.bookno;
    }

    public void setBookno(String bookno) {
        this.bookno = bookno;
    }

    public String getContainerno() {
        return this.containerno;
    }

    public void setContainerno(String containerno) {
        this.containerno = containerno;
    }

    public String getIsdummy() {
        return this.isdummy;
    }

    public void setIsdummy(String isdummy) {
        this.isdummy = isdummy;
    }

    public String getMovetype() {
        return this.movetype;
    }

    public void setMovetype(String movetype) {
        this.movetype = movetype;
    }

    public String getServicecode() {
        return this.servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    public String getVesselcode() {
        return this.vesselcode;
    }

    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    public String getVoyagecode() {
        return this.voyagecode;
    }

    public void setVoyagecode(String voyagecode) {
        this.voyagecode = voyagecode;
    }

    public String getGrpcallid() {
        return this.grpcallid;
    }

    public void setGrpcallid(String grpcallid) {
        this.grpcallid = grpcallid;
    }

    public String getOceansercode() {
        return this.oceansercode;
    }

    public void setOceansercode(String oceansercode) {
        this.oceansercode = oceansercode;
    }

    public String getOceanvslcode() {
        return this.oceanvslcode;
    }

    public void setOceanvslcode(String oceanvslcode) {
        this.oceanvslcode = oceanvslcode;
    }

    public String getOceanvoyage() {
        return this.oceanvoyage;
    }

    public void setOceanvoyage(String oceanvoyage) {
        this.oceanvoyage = oceanvoyage;
    }

    public String getOceanbound() {
        return this.oceanbound;
    }

    public void setOceanbound(String oceanbound) {
        this.oceanbound = oceanbound;
    }

    public String getPolterminal() {
        return this.polterminal;
    }

    public void setPolterminal(String polterminal) {
        this.polterminal = polterminal;
    }

    public String getPolat() {
        return this.polat;
    }

    public void setPolat(String polat) {
        this.polat = polat;
    }

    public String getOriginportid() {
        return this.originportid;
    }

    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    public String getLoadportid() {
        return this.loadportid;
    }

    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    public String getFinalportid() {
        return this.finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }

    public String getDischargeportid() {
        return this.dischargeportid;
    }

    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

    public String getPodterminal() {
        return this.podterminal;
    }

    public void setPodterminal(String podterminal) {
        this.podterminal = podterminal;
    }

    public String getHbookno() {
        return this.hbookno;
    }

    public void setHbookno(String hbookno) {
        this.hbookno = hbookno;
    }

    public String getVessel() {
        return this.vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getVoyage() {
        return this.voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getPla() {
        return this.pla;
    }

    public void setPla(String pla) {
        this.pla = pla;
    }

    public String getPld() {
        return this.pld;
    }

    public void setPld(String pld) {
        this.pld = pld;
    }

    public String getBlpodterminal() {
        return this.blpodterminal;
    }

    public void setBlpodterminal(String blpodterminal) {
        this.blpodterminal = blpodterminal;
    }

    public String getDeliveryport() {
        return this.deliveryport;
    }

    public void setDeliveryport(String deliveryport) {
        this.deliveryport = deliveryport;
    }

    public String getBlpod() {
        return this.blpod;
    }

    public void setBlpod(String blpod) {
        this.blpod = blpod;
    }

    public String getBlpolterminal() {
        return this.blpolterminal;
    }

    public void setBlpolterminal(String blpolterminal) {
        this.blpolterminal = blpolterminal;
    }

    public String getOriginport() {
        return this.originport;
    }

    public void setOriginport(String originport) {
        this.originport = originport;
    }

    public String getBlpol() {
        return this.blpol;
    }

    public void setBlpol(String blpol) {
        this.blpol = blpol;
    }

    public String getBl_no() {
        return this.bl_no;
    }

    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    public String getPrecarriage() {
        return this.precarriage;
    }

    public void setPrecarriage(String precarriage) {
        this.precarriage = precarriage;
    }

    public String getBook_no() {
        return this.book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getSendToEcom() {
        return this.sendToEcom;
    }

    public void setSendToEcom(String sendToEcom) {
        this.sendToEcom = sendToEcom;
    }

    public String getRefno() {
        return this.refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getBookagency() {
        return this.bookagency;
    }

    public void setBookagency(String bookagency) {
        this.bookagency = bookagency;
    }

    public String getCruser() {
        return this.cruser;
    }

    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    public String getChguser() {
        return this.chguser;
    }

    public void setChguser(String chguser) {
        this.chguser = chguser;
    }

    public String getSeal1no() {
        return this.seal1no;
    }

    public void setSeal1no(String seal1no) {
        this.seal1no = seal1no;
    }

    public String getCblno() {
        return this.cblno;
    }

    public void setCblno(String cblno) {
        this.cblno = cblno;
    }

    public String getEqpid() {
        return this.eqpid;
    }

    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPol() {
        return this.pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return this.pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getBound() {
        return this.bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getDatecr() {
        return this.datecr;
    }

    public void setDatecr(String datecr) {
        this.datecr = datecr;
    }

    public String getEdimode() {
        return this.edimode;
    }

    public void setEdimode(String edimode) {
        this.edimode = edimode;
    }

    public String getDoctype() {
        return this.doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsProcessed() {
        return this.isProcessed;
    }

    public void setIsProcessed(String isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getCrdate() {
        return this.crdate;
    }

    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }

    public String getDgno() {
        return this.dgno;
    }

    public void setDgno(String dgno) {
        this.dgno = dgno;
    }

    public String getImoflag() {
        return this.imoflag;
    }

    public void setImoflag(String imoflag) {
        this.imoflag = imoflag;
    }

    public String getConfirmeduser() {
        return this.confirmeduser;
    }

    public void setConfirmeduser(String confirmeduser) {
        this.confirmeduser = confirmeduser;
    }

    public String getConfirmeddate() {
        return this.confirmeddate;
    }

    public void setConfirmeddate(String confirmeddate) {
        this.confirmeddate = confirmeddate;
    }
}
