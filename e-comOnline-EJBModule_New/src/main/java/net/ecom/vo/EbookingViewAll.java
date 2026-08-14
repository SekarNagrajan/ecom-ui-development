/*
 * EbookingViewAll.java
 *
 * Created on June 3, 2008, 3:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author udhaya
 */
public class EbookingViewAll implements Serializable {

    /**
     * Creates a new instance of EbookingViewAll
     */
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
    private String action = "";
    private String usertype = "";
    private String status = "";
    private String status_desc = "";
    private String type = "";
    private String cruser = "";
    private String crdate = "";
    private String bookNo_Version = "";
    private String trust_book_no = "";
    private String trust_action = "";
    private String lrpstatus = ""; //added by Sakthikumar on 08.09.11
    private String submitdate = ""; //added by sasikumar
    private StringBuffer sb = new StringBuffer("");
    private StringBuffer sb1 = new StringBuffer("");
    private String booktype = "";
    private String lrpEcomStatus = "";
    private String agencyrefno = "";
    private String appVersion = "";
    private String dgStatus;
    private String confirmedDate = "";
    private String statusDesc = "";
    private String showExtBookNo = "";
    private String tuesCount = "";
    private String lrpusercr = null;
    private String poletd = null;

    private String haulageflag = "";

    private String payAmountUsd = "";
    private String payCustomerType = "";
    private String payAgency = "";
    private String paymentEligible = "N";
    private String paymentCompleted = "N";
    private String paymentRequestId = "";

    public String getTuesCount() {
        return tuesCount;
    }

    public void setTuesCount(String tuesCount) {
        this.tuesCount = tuesCount;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getShowExtBookNo() {
        return showExtBookNo;
    }

    public void setShowExtBookNo(String showExtBookNo) {
        this.showExtBookNo = showExtBookNo;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getDgStatus() {
        return dgStatus;
    }

    public void setDgStatus(String dgStatus) {
        this.dgStatus = dgStatus;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Validation getVal() {
        return val;
    }

    public void setVal(Validation val) {
        this.val = val;
    }

    public String getLrpEcomStatus() {
        return lrpEcomStatus;
    }

    public void setLrpEcomStatus(String lrpEcomStatus) {
        this.lrpEcomStatus = lrpEcomStatus;
    }
    Validation val = new Validation();

    public EbookingViewAll() {
    }

    public String getBookNo() {
        return Validation.checkNull(bookNo);
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getAgencyId() {

        return Validation.checkNull(agencyId);
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getOriginPortId() {

        return Validation.checkNull(originPortId);
    }

    public void setOriginPortId(String originPortId) {
        this.originPortId = originPortId;
    }

    public String getFinalPortId() {

        return Validation.checkNull(finalPortId);
    }

    public void setFinalPortId(String finalPortId) {
        this.finalPortId = finalPortId;
    }

    public String getLoadPortID() {

        return Validation.checkNull(loadPortID);
    }

    public void setLoadPortID(String loadPortID) {
        this.loadPortID = loadPortID;
    }
//added by Sakthikumar on 08.09.11

    public String getLrpstatus() {
        return Validation.checkNull(lrpstatus);
    }

    public void setLrpstatus(String lrpstatus) {
        this.lrpstatus = lrpstatus;
    }
//end

    public String getDischargePortId() {

        return Validation.checkNull(dischargePortId);
    }

    public void setDischargePortId(String dischargePortId) {
        this.dischargePortId = dischargePortId;
    }

    public String getOriginPortText() {

        return Validation.checkNull(originPortText);
    }

    public void setOriginPortText(String originPortText) {
        this.originPortText = originPortText;
    }

    public String getFinalPortText() {

        return Validation.checkNull(finalPortText);
    }

    public void setFinalPortText(String finalPortText) {
        this.finalPortText = finalPortText;
    }

    public String getLoadPortText() {

        return Validation.checkNull(loadPortText);
    }

    public void setLoadPortText(String loadPortText) {
        this.loadPortText = loadPortText;
    }

    public String getDischargePortText() {

        return Validation.checkNull(dischargePortText);
    }

    public void setDischargePortText(String dischargePortText) {
        this.dischargePortText = dischargePortText;
    }

    public String getBookingDate() {

        return Validation.checkNull(bookingDate);
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingStatus() {

        return Validation.checkNull(bookingStatus);
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getType() {

        return Validation.checkNull(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBookNo_Version() {

        return Validation.checkNull(bookNo_Version);
    }

    public void setBookNo_Version(String bookNo_Version) {
        this.bookNo_Version = bookNo_Version;
    }

    public String getHaulageflag() {
        return haulageflag;
    }

    public void setHaulageflag(String haulageflag) {
        this.haulageflag = haulageflag;
    }

    public String getAction() {
        String editclass = "";
        if ("old".equalsIgnoreCase(getType())) {
            editclass = "ebooking_crud.do";
        }
        if ("new".equalsIgnoreCase(getType())) {
            editclass = "ebooking_crud.do";
            //editclass = "ebooking_crudNew.do";
        }

        if (("S".equalsIgnoreCase(getUsertype())) || ("L".equalsIgnoreCase(getUsertype())) || ("A".equalsIgnoreCase(getUsertype()))) {
            if (("D").equalsIgnoreCase(getStatus())) {
                //System.out.println("[ECOM] EbookingViewAll    1====");
                getSb().append("<a href=\"javascript:void(0);\" title='View'");
                getSb().append(" onClick=\"popUpWindow('ViewEbookingEntry.do?type=bookingDetails&bookno=" + getBookNo() + "&book_type=" + getBooktype() + "',150,100,800,500)\"");
                getSb().append("><img src='images/viewBK.png' style='background-repeat:no-repeat'/>");
                getSb().append("</a>");
                getSb().append("&nbsp;|&nbsp;<a class=\"successtext\" href=\"" + editclass + "?action=edit&type=statusedit&flag=B&bookno=" + getBookNo() + "&book_type=" + getBooktype() + "\" title='Edit'><img src='images/edit.png' style='background-repeat:no-repeat'/>");
                getSb().append("</a>");
            } else {
                //System.out.println("[ECOM] EbookingViewAll    2======");
                getSb().append("<a href=\"javascript:void(0);\" title='View'");
                getSb().append(" onClick=\"popUpWindow('ViewEbookingEntry.do?type=bookingDetails&bookno=" + getBookNo() + "&book_type=" + getBooktype() + "',150,100,800,500)\"");
                getSb().append("><img src='images/viewBK.png' style='background-repeat:no-repeat'/>");
                getSb().append("</a>");
            }
        } else if ("C".equalsIgnoreCase(getUsertype())) {
            // if(("I".equalsIgnoreCase(getStatus().trim())) ||("D".equalsIgnoreCase(getStatus().trim())) || ("E".equalsIgnoreCase(getStatus().trim())) ){
            if (("I".equalsIgnoreCase(getStatus())) || ("E".equalsIgnoreCase(getStatus()))) {
                //System.out.println("[ECOM] EbookingViewAll    3==============");
                getSb().append("<a href=\"javascript:void(0);\" title='View'");
                getSb().append(" onClick=\"popUpWindow('ViewEbookingEntry.do?type=bookingDetails&bookno=" + getBookNo() + "&book_type=" + getBooktype() + "',150,100,800,500)\"");
                getSb().append("><img src='images/viewBK.png' style='background-repeat:no-repeat'/>");
                getSb().append("</a>");
                getSb().append("&nbsp;|&nbsp;<a class=\"successtext\" href=\"" + editclass + "?action=edit&flag=B&type=editdetails&bookno=" + getBookNo() + "&book_type=" + getBooktype() + "\" title='Edit'><img src='images/edit.png' style='background-repeat:no-repeat'/>");
                getSb().append("</a>");
            } else {
                //System.out.println("[ECOM] EbookingViewAll    4=========11"+ getBooktype());

                getSb().append("<a href=\"javascript:void(0);\" title='View'");
                getSb().append(" onClick=\"popUpWindow('ViewEbookingEntry.do?type=bookingDetails&bookno=" + getBookNo() + "&book_type=" + getBooktype() + "',150,100,800,500)\"");
                getSb().append("><img src='images/viewBK.png' style='background-repeat:no-repeat'/>");
                getSb().append("</a>");

            }
        }
        //System.out.println("[ECOM] EbookingViewAll    getSb().toString()======" + getSb().toString());
        return Validation.checkNull(getSb().toString());
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsertype() {

        return Validation.checkNull(usertype);

    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getStatus() {

        return Validation.checkNull(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_desc() {
        if ("D".equalsIgnoreCase(getStatus())) {
            status_desc = "Submitted";
        }
        if ("A".equalsIgnoreCase(getStatus())) {
            status_desc = "Accepted";
        }
        if ("I".equalsIgnoreCase(getStatus())) {
            status_desc = "Incorrect";
        }
        if ("R".equalsIgnoreCase(getStatus())) {
            status_desc = "Rejected";
        }
        if ("E".equalsIgnoreCase(getStatus())) {
            status_desc = "Draft";
        }

        return Validation.checkNull(status_desc);
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public StringBuffer getSb() {
        return sb;
    }

    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }

    public String getCruser() {

        return Validation.checkNull(cruser);
    }

    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    public String getCrdate() {

        return Validation.checkNull(crdate);
    }

    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }

    /**
     * @return the trust_book_no
     */
    public String getTrust_book_no() {

        return Validation.checkNull(trust_book_no);
    }

    /**
     * @param trust_book_no the trust_book_no to set
     */
    public void setTrust_book_no(String trust_book_no) {
        this.trust_book_no = trust_book_no;
    }

    /**
     * @return the trust_action
     */
    public String getTrust_action() {

        if (("S".equalsIgnoreCase(getUsertype())) || ("L".equalsIgnoreCase(getUsertype())) || ("A".equalsIgnoreCase(getUsertype()))) {
            if (("A".equalsIgnoreCase(getUsertype()))) {
                // getSb1().append("<a href=\"javascript:void(0);\"");
                // getSb1().append(" onClick=\"popUpWindow('ViewTrustBooking.do?type=bookingDetails&bookno=" + getTrust_book_no() + "&book_type=" + getType() + "',150,100,800,500)\">");
                getSb1().append(getTrust_book_no());
                // getSb1().append("</a>");
            }

        } else if ("C".equalsIgnoreCase(getUsertype())) {
            String str = getSb1().toString();
            String str1 = getTrust_book_no().toString();
            // if ((getStatus().trim().equalsIgnoreCase("A"))) {
            if (!str.equalsIgnoreCase(str1)) {
                if (getTrust_book_no().length() > 0) {
                    // getSb1().append("<a href=\"javascript:void(0);\"");
                    //  getSb1().append(" onClick=\"popUpWindow('ViewTrustBooking.do?type=bookingDetails&bookno=" + getTrust_book_no() + "&book_type=" + getType() + "',150,100,800,500)\">");
                    getSb1().append(getTrust_book_no());
                    // getSb1().append("</a>");
                }
            }

        }

        return Validation.checkNull(getSb1().toString());

    }

    /**
     * @param trust_action the trust_action to set
     */
    public void setTrust_action(String trust_action) {
        this.trust_action = trust_action;
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
     * @return the submitdate
     */
    public String getSubmitdate() {
        return submitdate;
    }

    /**
     * @param submitdate the submitdate to set
     */
    public void setSubmitdate(String submitdate) {
        this.submitdate = submitdate;
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

    public String getLrpusercr() {
        return lrpusercr;
    }

    public void setLrpusercr(String lrpusercr) {
        this.lrpusercr = lrpusercr;
    }

    public String getPoletd() {
        return poletd;
    }

    public void setPoletd(String poletd) {
        this.poletd = poletd;
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

    public String getPayAgency() {
        return payAgency;
    }

    public void setPayAgency(String payAgency) {
        this.payAgency = payAgency;
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

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

}
