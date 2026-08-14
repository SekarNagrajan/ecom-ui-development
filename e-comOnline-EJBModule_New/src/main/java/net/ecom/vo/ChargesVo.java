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
public class ChargesVo implements Serializable {
    
    private String book_no;
    private String chargeid;
    private String chargename;
    private String payagencyid;
    private String custtypeid;
    private String fprepaidcollect;
    private String eqptypeid;

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getChargeid() {
        return chargeid;
    }

    public void setChargeid(String chargeid) {
        this.chargeid = chargeid;
    }

    public String getChargename() {
        return chargename;
    }

    public void setChargename(String chargename) {
        this.chargename = chargename;
    }

    public String getPayagencyid() {
        return payagencyid;
    }

    public void setPayagencyid(String payagencyid) {
        this.payagencyid = payagencyid;
    }

    public String getCusttypeid() {
        return custtypeid;
    }

    public void setCusttypeid(String custtypeid) {
        this.custtypeid = custtypeid;
    }

    public String getFprepaidcollect() {
        return fprepaidcollect;
    }

    public void setFprepaidcollect(String fprepaidcollect) {
        this.fprepaidcollect = fprepaidcollect;
    }

    public String getEqptypeid() {
        return eqptypeid;
    }

    public void setEqptypeid(String eqptypeid) {
        this.eqptypeid = eqptypeid;
    }
    
}
