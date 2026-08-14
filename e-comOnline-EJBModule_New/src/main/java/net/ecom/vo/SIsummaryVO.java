/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author arunprasath
 */
public class SIsummaryVO implements Cloneable,Serializable {

    private String bookno = null;
    private String custrefno = null;
    private String pol = null;
    private String pod = null;
    private String depdate = null;
    private String status = null;
    private String moddate = null;
    private String shipperrefno = null;
    private int countReq = 0;
    private int countCre = 0;
    //private String agencyid=null;
    private String action = null;
    private int docid = 0;
    private String si_type = null;
    private String SI_no = null;
    private String bl_no = null;
    private String bl_status = null;
    private String decline_status = ""; // Mahalakshmi
    private String bookstatus=null;
    private String agencyRefNo=null;
    private String appVersion=null;
    private String originPort=null;
    private String deliveryPort=null;
    private String issilocked=null;
    private String datecr = null;
    private String dateup = null;

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDeliveryPort() {
        return deliveryPort;
    }

    public void setDeliveryPort(String deliveryPort) {
        this.deliveryPort = deliveryPort;
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

    public String getBookstatus() {
        return bookstatus;
    }

    public void setBookstatus(String bookstatus) {
        this.bookstatus = bookstatus;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return the bookno
     */
    public String getBookno() {
        return Validation.checkNull(bookno);
    }

    /**
     * @param bookno the bookno to set
     */
    public void setBookno(String bookno) {
        this.bookno = bookno;
    }

    /**
     * @return the refno
     */
    /**
     * @return the pol
     */
    public String getPol() {
        return Validation.checkNull(pol);
    }

    /**
     * @param pol the pol to set
     */
    public void setPol(String pol) {
        this.pol = pol;
    }

    /**
     * @return the pod
     */
    public String getPod() {
        return Validation.checkNull(pod);
    }

    /**
     * @param pod the pod to set
     */
    public void setPod(String pod) {
        this.pod = pod;
    }

    /**
     * @return the depdate
     */
    public String getDepdate() {
        return Validation.checkNull(depdate);
    }

    /**
     * @param depdate the depdate to set
     */
    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return Validation.checkNull(status);
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the shipperrefno
     */
    public String getShipperrefno() {
        return Validation.checkNull(shipperrefno);
    }

    /**
     * @param shipperrefno the shipperrefno to set
     */
    public void setShipperrefno(String shipperrefno) {
        this.shipperrefno = shipperrefno;
    }

    /**
     * @return the custrefno
     */
    public String getCustrefno() {
        return Validation.checkNull(custrefno);
    }

    /**
     * @param custrefno the custrefno to set
     */
    public void setCustrefno(String custrefno) {
        this.custrefno = custrefno;
    }

    /**
     * @return the moddate
     */
    public String getModdate() {
        return Validation.checkNull(moddate);
    }

    /**
     * @param moddate the moddate to set
     */
    public void setModdate(String moddate) {
        this.moddate = moddate;
    }

    /**
     * @return the count
     */
    /**
     * @return the action
     */
    public String getAction() {
        return Validation.checkNull(action);
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the countReq
     */
    public int getCountReq() {
        return countReq;
    }

    /**
     * @param countReq the countReq to set
     */
    public void setCountReq(int countReq) {
        this.countReq = countReq;
    }

    /**
     * @return the countCre
     */
    public int getCountCre() {
        return countCre;
    }

    /**
     * @param countCre the countCre to set
     */
    public void setCountCre(int countCre) {
        this.countCre = countCre;
    }

    /**
     * @return the docid
     */
    public int getDocid() {
        return docid;
    }

    /**
     * @param docid the docid to set
     */
    public void setDocid(int docid) {
        this.docid = docid;
    }

    /**
     * @return the si_type
     */
    public String getSi_type() {
        return Validation.checkNull(si_type);
    }

    /**
     * @param si_type the si_type to set
     */
    public void setSi_type(String si_type) {
        this.si_type = si_type;
    }

    /**
     * @return the SI_no
     */
    public String getSI_no() {
        return Validation.checkNull(SI_no);
    }

    /**
     * @param SI_no the SI_no to set
     */
    public void setSI_no(String SI_no) {
        this.SI_no = SI_no;
    }

    /**
     * @return the bl_no
     */
    public String getBl_no() {

        return Validation.checkNull(bl_no);
    }

    /**
     * @param bl_no the bl_no to set
     */
    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    /**
     * @return the bl_status
     */
    public String getBl_status() {
        String s = Validation.checkNull(bl_status);
        if (s.equalsIgnoreCase("C")) {
            s = "Confirmed";
        } else if (s.equalsIgnoreCase("D")) {
            s = "Draft";
        } else if (s.equalsIgnoreCase("I")) {
            s = "Issued";
        }

        return s;

    }

    /**
     * @param bl_status the bl_status to set
     */
    public void setBl_status(String bl_status) {
        this.bl_status = bl_status;
    }

    /**
     * @return the decline_status
     */
    public String getDecline_status() {
        return decline_status;
    }

    /**
     * @param decline_status the decline_status to set
     */
    public void setDecline_status(String decline_status) {
        this.decline_status = decline_status;
    }

    public String getIssilocked() {
        return issilocked;
    }

    public void setIssilocked(String issilocked) {
        this.issilocked = issilocked;
    }

    public String getDatecr() {
        return datecr;
    }

    public void setDatecr(String datecr) {
        this.datecr = datecr;
    }

    public String getDateup() {
        return dateup;
    }

    public void setDateup(String dateup) {
        this.dateup = dateup;
    }
    
    
}
