/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author anitha.c
 */
public class OBLPrintVo implements Serializable {

    private String bookNo = "";
    private String agencyId = "";
    private String originPortId = "";
    private String finalPortId = "";
    private String loadPortID = "";
    private String dischargePortId = "";
    private String originPortText = "";
    private String finalPortText = "";
    private String loadPortText = "";
    private String dischargePortText = "";
    private String bookingDate = "";
    private String bookingStatus = "";
    private String blprintStatus = "";
    private String action = "";
    private String usertype = "";
    private String status = "";
    private String status_desc = "";
    private String type = "";
    private String custid = "";
    private String bookNo_Version = "";
    private String blNo = "";
    private String actionprint = "";
    private String signature = "";
    private String cruser = "";
    private String crdate = "";
    private String printstatus = "";
    private String originals = "";
    private String dateup = "";
    private String partlot = "";
    private String datestatus = "";
    private String servicecode = "";
    private String vesselcode = "";
    private String voyagecode = "";
    private String bound = "";
    private String polterminal = "";
    private String slottype = "";
    private String shipmentstatus = "";
    private String book_no = "";
    private String Copies = "";
    private Hashtable loadconfirm = new Hashtable();
    private int loadconfirmsize = 0;
    private StringBuffer sb = new StringBuffer("");
    private StringBuffer sb1 = new StringBuffer("");
    private String custoblprint = "";
    private String ecomsino = "";
    private String agencyRefNo = null;
    private String documentalagencyid = null;
    private String polAt = null;
    private String appVersion = null;
    private String confirmedDate;
    private String blAcceptByUserStatus = null;
    private String receiptshipdate = null;
    private String fcnno = null;
    private String isfcn = null;
    private String fcnverno = null;
    private String issilocked = null;

    private String isinsurance = "N";
    private String policyno = null;
    private String mcnstatus = null;

    private String payAmountUsd = "";
    private String payCustomerType = "";
    private String pay_agency = "";
    private String paymentEligible = "N";
    private String paymentCompleted = "N";
    private String paymentRequestId = "";

    public String getBlAcceptByUserStatus() {
        return blAcceptByUserStatus;
    }

    public void setBlAcceptByUserStatus(String blAcceptByUserStatus) {
        this.blAcceptByUserStatus = blAcceptByUserStatus;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getPolAt() {
        return polAt;
    }

    public void setPolAt(String polAt) {
        this.polAt = polAt;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAgencyRefNo() {
        return agencyRefNo;
    }

    public void setAgencyRefNo(String agencyRefNo) {
        this.agencyRefNo = agencyRefNo;
    }

    /**
     * @return the bookNo
     */
    /**
     * @param bookNo the bookNo to set
     */
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getBookNo() {
//        if ("C".equalsIgnoreCase(getUsertype().trim())) {
//
//
//            if ("C".equalsIgnoreCase(getStatus().trim()) && ("".equalsIgnoreCase(getBlprintStatus().trim()) || ("NULL".equalsIgnoreCase(getBlprintStatus().trim()))) && ("Y".equalsIgnoreCase(getPrintstatus().trim()))) {
//                getSb1().append("<span class=\"siViewLink\"");
//                getSb1().append("<a onClick=\"tracker('" + bookNo + "','" + getSignature() + "','" + getStatus() + "','" + getOriginals() + "','" + getDatestatus() + "','" + getLoadconfirm() + "','" + getLoadconfirmsize() + "')\">");
//                getSb1().append(bookNo);
//                getSb1().append("</a>");
//
//            } else if ("I".equalsIgnoreCase(getStatus().trim())&& ("Y".equalsIgnoreCase(getPrintstatus().trim()))) {
//                getSb1().append("<span class=\"siViewLink\"");
//                getSb1().append("<a onClick=\"tracker('" + bookNo + "','" + getSignature() + "','" + getStatus() + "','" + getCopies() + "','" + getDatestatus() + "','" + getLoadconfirm() + "','" + getLoadconfirmsize() + "')\">");
//                getSb1().append(bookNo);
//                getSb1().append("</a>");
//
//            } else {
//                getSb1().append(bookNo);
//            }
//
//        }
        return bookNo;

    }

    /**
     * @return the originPortId
     */
    public String getOriginPortId() {
        return originPortId;
    }

    /**
     * @param originPortId the originPortId to set
     */
    public void setOriginPortId(String originPortId) {
        this.originPortId = originPortId;
    }

    /**
     * @return the finalPortId
     */
    public String getFinalPortId() {
        return finalPortId;
    }

    /**
     * @param finalPortId the finalPortId to set
     */
    public void setFinalPortId(String finalPortId) {
        this.finalPortId = finalPortId;
    }

    /**
     * @return the loadPortID
     */
    public String getLoadPortID() {
        return loadPortID;
    }

    /**
     * @param loadPortID the loadPortID to set
     */
    public void setLoadPortID(String loadPortID) {
        this.loadPortID = loadPortID;
    }

    /**
     * @return the dischargePortId
     */
    public String getDischargePortId() {
        return dischargePortId;
    }

    /**
     * @param dischargePortId the dischargePortId to set
     */
    public void setDischargePortId(String dischargePortId) {
        this.dischargePortId = dischargePortId;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
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
     * @return the custid
     */
    public String getCustid() {
        return custid;
    }

    /**
     * @param custid the custid to set
     */
    public void setCustid(String custid) {
        this.custid = custid;
    }

    /**
     * @return the actionprint
     */
    public String getActionprint() {
        String editclass = "";
//
        editclass = getBlNo();

        if ("C".equalsIgnoreCase(getUsertype().trim())) {

//            if ("C".equalsIgnoreCase(getStatus().trim()) && ("".equalsIgnoreCase(getBlprintStatus().trim()) || ("NULL".equalsIgnoreCase(getBlprintStatus().trim()))) && ("Y".equalsIgnoreCase(getPrintstatus().trim()))) {
            if ("C".equalsIgnoreCase(getStatus().trim()) && ("".equalsIgnoreCase(getBlprintStatus().trim()) || ("NULL".equalsIgnoreCase(getBlprintStatus().trim()))) && ("Y".equalsIgnoreCase(getPrintstatus().trim())) && ("Y".equalsIgnoreCase(getCustoblprint().trim()))) {
                getSb().append(" <a onClick=\"callviewbl('" + bookNo + "','" + getStatus() + "')\">");
                getSb().append("<img src='images/viewBK.png' title=\"View\" style =\"cursor: pointer\" >");
                getSb().append("</a>");

                getSb().append("&nbsp;|&nbsp;<a onClick=\"tracker('" + bookNo + "','" + getSignature() + "','" + getStatus() + "','" + getOriginals() + "','" + getDatestatus() + "','" + getLoadconfirm() + "','" + getLoadconfirmsize() + "','" + getCopies() + "')\">");
                getSb().append("<img src='images/print.png' title=\"Print\" style =\"cursor: pointer\" >");
                getSb().append("</a>");

//            } else if ("I".equalsIgnoreCase(getStatus().trim()) && ("Y".equalsIgnoreCase(getPrintstatus().trim()))) {
            } else if ("I".equalsIgnoreCase(getStatus().trim()) && ("Y".equalsIgnoreCase(getPrintstatus().trim())) && ("Y".equalsIgnoreCase(getCustoblprint().trim()))) {
                getSb().append(" <a onClick=\"callviewbl('" + bookNo + "','" + getStatus() + "')\">");
                getSb().append("<img  src='images/viewBK.png' title=\"View\" style =\"cursor: pointer\">");
                getSb().append("</a>");

                getSb().append("&nbsp;|&nbsp;<a onClick=\"tracker('" + bookNo + "','" + getSignature() + "','" + getStatus() + "','" + getOriginals() + "','" + getDatestatus() + "','" + getLoadconfirm() + "','" + getLoadconfirmsize() + "','" + getCopies() + "')\">");
                getSb().append("<img src='images/print.png' title=\"Print\" style =\"cursor: pointer\">");
                getSb().append("</a>");

            } else {
                getSb().append(" <a onClick=\"callviewbl('" + bookNo + "','" + getStatus() + "')\">");
                getSb().append("<img src='images/viewBK.png' title=\"View\" style =\"cursor: pointer\">");
                getSb().append("</a>");
            }

        }
        return getSb().toString();
    }

    /**
     * @param actionprint the actionprint to set
     */
    public void setActionprint(String actionprint) {
        this.actionprint = actionprint;
    }

    /**
     * @return the sb
     */
    public StringBuffer getSb() {
        return sb;
    }

    /**
     * @param sb the sb to set
     */
    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }

    /**
     * @return the usertype
     */
    public String getUsertype() {
        return usertype;
    }

    /**
     * @param usertype the usertype to set
     */
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the status_desc
     */
    public String getStatus_desc() {
        if (getStatus().trim().equalsIgnoreCase("D")) {
            this.status_desc = "Draft";
        }
        if (getStatus().trim().equalsIgnoreCase("A")) {
            this.status_desc = "Accepted";
        }
        if (getStatus().trim().equalsIgnoreCase("I")) {
            this.status_desc = "Issued";
        }
        if (getStatus().trim().equalsIgnoreCase("R")) {
            this.status_desc = "Rejected";
        }
        if (getStatus().trim().equalsIgnoreCase("E")) {
            this.status_desc = "InComplete";
        }
        if (getStatus().trim().equalsIgnoreCase("C")) {
            this.status_desc = "Confirm";
        }
        return this.status_desc;
    }

    /**
     * @param status_desc the status_desc to set
     */
    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    /**
     * @return the originPortText
     */
    public String getOriginPortText() {
        return originPortText;
    }

    /**
     * @param originPortText the originPortText to set
     */
    public void setOriginPortText(String originPortText) {
        this.originPortText = originPortText;
    }

    /**
     * @return the finalPortText
     */
    public String getFinalPortText() {
        return finalPortText;
    }

    /**
     * @param finalPortText the finalPortText to set
     */
    public void setFinalPortText(String finalPortText) {
        this.finalPortText = finalPortText;
    }

    /**
     * @return the blNo
     */
    public String getBlNo() {
        return blNo;
    }

    /**
     * @param blNo the blNo to set
     */
    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    /**
     * @return the blprintStatus
     */
    public String getBlprintStatus() {
        return blprintStatus;
    }

    /**
     * @param blprintStatus the blprintStatus to set
     */
    public void setBlprintStatus(String blprintStatus) {
        this.blprintStatus = blprintStatus;
    }

    /**
     * @return the bookNo_Version
     */
    public String getBookNo_Version() {
        return bookNo_Version;
    }

    /**
     * @param bookNo_Version the bookNo_Version to set
     */
    public void setBookNo_Version(String bookNo_Version) {
        this.bookNo_Version = bookNo_Version;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return the cruser
     */
    public String getCruser() {
        return cruser;
    }

    /**
     * @param cruser the cruser to set
     */
    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    /**
     * @return the crdate
     */
    public String getCrdate() {
        return crdate;
    }

    /**
     * @param crdate the crdate to set
     */
    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }

    /**
     * @return the printstatus
     */
    public String getPrintstatus() {
        return printstatus;
    }

    /**
     * @param printstatus the printstatus to set
     */
    public void setPrintstatus(String printstatus) {
        this.printstatus = printstatus;
    }

    /**
     * @return the agencyId
     */
    public String getAgencyId() {
        return agencyId;
    }

    /**
     * @param agencyId the agencyId to set
     */
    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    /**
     * @return the loadPortText
     */
    public String getLoadPortText() {
        return loadPortText;
    }

    /**
     * @param loadPortText the loadPortText to set
     */
    public void setLoadPortText(String loadPortText) {
        this.loadPortText = loadPortText;
    }

    /**
     * @return the dischargePortText
     */
    public String getDischargePortText() {
        return dischargePortText;
    }

    /**
     * @param dischargePortText the dischargePortText to set
     */
    public void setDischargePortText(String dischargePortText) {
        this.dischargePortText = dischargePortText;
    }

    /**
     * @return the bookingDate
     */
    public String getBookingDate() {
        return bookingDate;
    }

    /**
     * @param bookingDate the bookingDate to set
     */
    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * @return the bookingStatus
     */
    public String getBookingStatus() {
        return bookingStatus;
    }

    /**
     * @param bookingStatus the bookingStatus to set
     */
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
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
     * @return the partlot
     */
    public String getPartlot() {
        return partlot;
    }

    /**
     * @param partlot the partlot to set
     */
    public void setPartlot(String partlot) {
        this.partlot = partlot;
    }

    /**
     * @return the datestatus
     */
    public String getDatestatus() {
        return datestatus;
    }

    /**
     * @param datestatus the datestatus to set
     */
    public void setDatestatus(String datestatus) {
        this.datestatus = datestatus;
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
     * @return the slottype
     */
    public String getSlottype() {
        return slottype;
    }

    /**
     * @param slottype the slottype to set
     */
    public void setSlottype(String slottype) {
        this.slottype = slottype;
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
     * @return the loadconfirm
     */
    public Hashtable getLoadconfirm() {
        return loadconfirm;
    }

    /**
     * @param loadconfirm the loadconfirm to set
     */
    public void setLoadconfirm(Hashtable loadconfirm) {
        this.loadconfirm = loadconfirm;
    }

    /**
     * @return the loadconfirmsize
     */
    public int getLoadconfirmsize() {
        return loadconfirmsize;
    }

    /**
     * @param loadconfirmsize the loadconfirmsize to set
     */
    public void setLoadconfirmsize(int loadconfirmsize) {
        this.loadconfirmsize = loadconfirmsize;
    }

    /**
     * @return the Copies
     */
    public String getCopies() {
        return Copies;
    }

    /**
     * @param Copies the Copies to set
     */
    public void setCopies(String Copies) {
        this.Copies = Copies;
    }

    /**
     * @return the sb1
     */
    public StringBuffer getSb1() {
        return sb1;
    }

    /**
     * @param sb1 the sb1 to set
     */
    public void setSb1(StringBuffer sb1) {
        this.sb1 = sb1;
    }

    /**
     * @return the custoblprint
     */
    public String getCustoblprint() {
        return custoblprint;
    }

    /**
     * @param custoblprint the custoblprint to set
     */
    public void setCustoblprint(String custoblprint) {
        this.custoblprint = custoblprint;
    }

    /**
     * @return the ecomsino
     */
    public String getEcomsino() {
        return ecomsino;
    }

    /**
     * @param ecomsino the ecomsino to set
     */
    public void setEcomsino(String ecomsino) {
        this.ecomsino = ecomsino;
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

    public String getIsfcn() {
        return isfcn;
    }

    public void setIsfcn(String isfcn) {
        this.isfcn = isfcn;
    }

    public String getFcnverno() {
        return fcnverno;
    }

    public void setFcnverno(String fcnverno) {
        this.fcnverno = fcnverno;
    }

    public String getIssilocked() {
        return issilocked;
    }

    public void setIssilocked(String issilocked) {
        this.issilocked = issilocked;
    }

    public String getIsinsurance() {
        return isinsurance;
    }

    public void setIsinsurance(String isinsurance) {
        this.isinsurance = isinsurance;
    }

    public String getPolicyno() {
        return policyno;
    }

    public void setPolicyno(String policyno) {
        this.policyno = policyno;
    }

    public String getMcnstatus() {
        return mcnstatus;
    }

    public void setMcnstatus(String mcnstatus) {
        this.mcnstatus = mcnstatus;
    }

    public String getPayAmountUsd() {
        return payAmountUsd;
    }

    public void setPayAmountUsd(String payAmountUsd) {
        this.payAmountUsd = payAmountUsd;
    }

    public String getPayCustomerType() {
        return payCustomerType;
    }

    public void setPayCustomerType(String payCustomerType) {
        this.payCustomerType = payCustomerType;
    }

    public String getPaymentEligible() {
        return paymentEligible;
    }

    public void setPaymentEligible(String paymentEligible) {
        this.paymentEligible = paymentEligible;
    }

    public String getPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(String paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public String getPay_agency() {
        return pay_agency;
    }

    public void setPay_agency(String pay_agency) {
        this.pay_agency = pay_agency;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

}
