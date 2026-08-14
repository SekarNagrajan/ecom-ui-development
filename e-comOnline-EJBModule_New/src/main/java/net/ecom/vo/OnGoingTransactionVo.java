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
public class OnGoingTransactionVo implements Serializable {

    private String onlinerefno;
    private String originportid;
    private String finalportid;
    private String originportdesc;
    private String finalportdesc;
    private String agencyid;
    private String polat;
    private String loadportid;
    private String dischargeportid;
    private String bookdate;
    private String book_no;
    private String status;
    private String bl_no;
    private String containerno;
    private String latestact;
    private String tuescount;
    private String allcontainernos;

    public String getOnlinerefno() {
        return onlinerefno;
    }

    public void setOnlinerefno(String onlinerefno) {
        this.onlinerefno = onlinerefno;
    }

    public String getOriginportid() {
        return originportid;
    }

    public void setOriginportid(String originportid) {
        this.originportid = originportid;
    }

    public String getFinalportid() {
        return finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }

    public String getOriginportdesc() {
        return originportdesc;
    }

    public void setOriginportdesc(String originportdesc) {
        this.originportdesc = originportdesc;
    }

    public String getFinalportdesc() {
        return finalportdesc;
    }

    public void setFinalportdesc(String finalportdesc) {
        this.finalportdesc = finalportdesc;
    }

    public String getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    public String getPolat() {
        return polat;
    }

    public void setPolat(String polat) {
        this.polat = polat;
    }

    public String getLoadportid() {
        return loadportid;
    }

    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    public String getDischargeportid() {
        return dischargeportid;
    }

    public void setDischargeportid(String dischargeportid) {
        this.dischargeportid = dischargeportid;
    }

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBl_no() {
        return bl_no;
    }

    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    public String getContainerno() {
        return containerno;
    }

    public void setContainerno(String containerno) {
        this.containerno = containerno;
    }

    public String getLatestact() {
        return latestact;
    }

    public void setLatestact(String latestact) {
        this.latestact = latestact;
    }

    public String getTuescount() {
        return tuescount;
    }

    public void setTuescount(String tuescount) {
        this.tuescount = tuescount;
    }

    public String getAllcontainernos() {
        return allcontainernos;
    }

    public void setAllcontainernos(String allcontainernos) {
        this.allcontainernos = allcontainernos;
    }

  
    
}
