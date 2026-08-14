/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import java.io.Serializable;

/**
 *
 * @author sasikumar.r
 */
public class BlSummaryVo implements Serializable {

    private String book_no = null;
    private String bl_no = null;
    private String originportid = null;
    private String loadportid = null;
    private String dischargeportid = null;
    private String finalportid = null;
    private String polat = null;
    private String status = null;
    private String isfcn = null;
    private String agencyissueid = null;
    private String bltype = null;
    private String Systembltype = null;
    private String datecr = null;
    private String dateup = null;
    private String shipmentstatus = null;
    private String ecomprntstatus;
    private String originals = null;
    private String copies;
    private String agencyrefno = null;
    private String documentalagencyid = null;
    private String confirmeddate = null;
    private String refval = null;
    private String receiptshipdate = null;
    private String fcnno = null;
    private String fcnverno = null;
    private String ecomsino = null;
    private String policy_no = null;
    private String mcnstatus = null;

    public String getRefval() {
        return refval;
    }

    public void setRefval(String refval) {
        this.refval = refval;
    }

    public String getConfirmeddate() {
        return confirmeddate;
    }

    public void setConfirmeddate(String confirmeddate) {
        this.confirmeddate = confirmeddate;
    }

    public String getAgencyrefno() {
        return agencyrefno;
    }

    public void setAgencyrefno(String agencyrefno) {
        this.agencyrefno = agencyrefno;
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
     * @return the bl_no
     */
    public String getBl_no() {
        return bl_no;
    }

    /**
     * @param bl_no the bl_no to set
     */
    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
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
     * @return the isfcn
     */
    public String getIsfcn() {
        return isfcn;
    }

    /**
     * @param isfcn the isfcn to set
     */
    public void setIsfcn(String isfcn) {
        this.isfcn = isfcn;
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
     * @return the bltype
     */
    public String getBltype() {
        return bltype;
    }

    /**
     * @param bltype the bltype to set
     */
    public void setBltype(String bltype) {
        this.bltype = bltype;
    }

    /**
     * @return the Systembltype
     */
    public String getSystembltype() {
        return Systembltype;
    }

    /**
     * @param Systembltype the Systembltype to set
     */
    public void setSystembltype(String Systembltype) {
        this.Systembltype = Systembltype;
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
     * @return the dateup
     */
    public String getDateup() {
        return dateup;
    }

    /**
     * @param dateup the dateup to set
     */
    public void setDateup(String dateup) {
        this.dateup = dateup;
    }

    /**
     * @return the shipmentstatus
     */
    public String getShipmentstatus() {
        return shipmentstatus;
    }

    /**
     * @param shipmentstatus the shipmentstatus to set
     */
    public void setShipmentstatus(String shipmentstatus) {
        this.shipmentstatus = shipmentstatus;
    }

    /**
     * @return the ecomprntstatus
     */
    public String getEcomprntstatus() {
        return ecomprntstatus;
    }

    /**
     * @param ecomprntstatus the ecomprntstatus to set
     */
    public void setEcomprntstatus(String ecomprntstatus) {
        this.ecomprntstatus = ecomprntstatus;
    }

    /**
     * @return the originals
     */
    public String getOriginals() {
        return originals;
    }

    /**
     * @param originals the originals to set
     */
    public void setOriginals(String originals) {
        this.originals = originals;
    }

    /**
     * @return the copies
     */
    public String getCopies() {
        return copies;
    }

    /**
     * @param copies the copies to set
     */
    public void setCopies(String copies) {
        this.copies = copies;
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
     * @return the receiptshipdate
     */
    public String getReceiptshipdate() {
        return receiptshipdate;
    }

    /**
     * @param receiptshipdate the receiptshipdate to set
     */
    public void setReceiptshipdate(String receiptshipdate) {
        this.receiptshipdate = receiptshipdate;
    }

    public String getFcnno() {
        return fcnno;
    }

    public void setFcnno(String fcnno) {
        this.fcnno = fcnno;
    }

    public String getFcnverno() {
        return fcnverno;
    }

    public void setFcnverno(String fcnverno) {
        this.fcnverno = fcnverno;
    }

    public String getEcomsino() {
        return ecomsino;
    }

    public void setEcomsino(String ecomsino) {
        this.ecomsino = ecomsino;
    }

    public String getPolicy_no() {
        return policy_no;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public String getMcnstatus() {
        return mcnstatus;
    }

    public void setMcnstatus(String mcnstatus) {
        this.mcnstatus = mcnstatus;
    }

    private String pay_amount_usd;
    private String pay_customer_type;
    private String payagency;
    private String is_payment_eligible;
    private String is_payment_completed;
    private String paymentrequestid;

    public String getPay_amount_usd() {
        return pay_amount_usd;
    }

    public void setPay_amount_usd(String pay_amount_usd) {
        this.pay_amount_usd = pay_amount_usd;
    }

    public String getPay_customer_type() {
        return pay_customer_type;
    }

    public void setPay_customer_type(String pay_customer_type) {
        this.pay_customer_type = pay_customer_type;
    }

    public String getPayagency() {
        return payagency;
    }

    public void setPayagency(String payagency) {
        this.payagency = payagency;
    }

    public String getIs_payment_eligible() {
        return is_payment_eligible;
    }

    public void setIs_payment_eligible(String is_payment_eligible) {
        this.is_payment_eligible = is_payment_eligible;
    }

    public String getIs_payment_completed() {
        return is_payment_completed;
    }

    public void setIs_payment_completed(String is_payment_completed) {
        this.is_payment_completed = is_payment_completed;
    }

    public String getPaymentrequestid() {
        return paymentrequestid;
    }

    public void setPaymentrequestid(String paymentrequestid) {
        this.paymentrequestid = paymentrequestid;
    }

}
