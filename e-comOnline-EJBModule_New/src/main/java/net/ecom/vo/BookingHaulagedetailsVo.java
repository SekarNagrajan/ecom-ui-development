/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class BookingHaulagedetailsVo implements Serializable, Cloneable {

    private String ebookno;
    private String eqpid;
    private String eqptype;
    private int eqprowid;
    private String pickloccode;
    private String picklocname;
    private String address;
    private String refno;
    private String dropdate;
    private int stopsequence;
    private String contactperson;
    private String contactphone;
    private String contactemail;
    private String twrsdoctype;
    private String twrsoverweight;
    private String tmsclientcode;
    private String tmsclientname;
    private String remarks;
    private String dummyeqp;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getEbookno() {
        return ebookno;
    }

    public void setEbookno(String ebookno) {
        this.ebookno = ebookno;
    }

    public String getEqpid() {
        return eqpid;
    }

    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public String getEqptype() {
        return eqptype;
    }

    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    public int getEqprowid() {
        return eqprowid;
    }

    public void setEqprowid(int eqprowid) {
        this.eqprowid = eqprowid;
    }

    public String getPickloccode() {
        return pickloccode;
    }

    public void setPickloccode(String pickloccode) {
        this.pickloccode = pickloccode;
    }

    public String getPicklocname() {
        return picklocname;
    }

    public void setPicklocname(String picklocname) {
        this.picklocname = picklocname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getDropdate() {
        return dropdate;
    }

    public void setDropdate(String dropdate) {
        this.dropdate = dropdate;
    }

    public int getStopsequence() {
        return stopsequence;
    }

    public void setStopsequence(int stopsequence) {
        this.stopsequence = stopsequence;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getContactemail() {
        return contactemail;
    }

    public void setContactemail(String contactemail) {
        this.contactemail = contactemail;
    }

    public String getTwrsdoctype() {
        return twrsdoctype;
    }

    public void setTwrsdoctype(String twrsdoctype) {
        this.twrsdoctype = twrsdoctype;
    }

    public String getTwrsoverweight() {
        return twrsoverweight;
    }

    public void setTwrsoverweight(String twrsoverweight) {
        this.twrsoverweight = twrsoverweight;
    }

    public String getTmsclientcode() {
        return tmsclientcode;
    }

    public void setTmsclientcode(String tmsclientcode) {
        this.tmsclientcode = tmsclientcode;
    }

    public String getTmsclientname() {
        return tmsclientname;
    }

    public void setTmsclientname(String tmsclientname) {
        this.tmsclientname = tmsclientname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDummyeqp() {
        return dummyeqp;
    }

    public void setDummyeqp(String dummyeqp) {
        this.dummyeqp = dummyeqp;
    }

}
