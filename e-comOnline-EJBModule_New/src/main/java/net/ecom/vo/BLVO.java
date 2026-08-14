/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;
import java.io.Serializable;
/**
 *
 * @author durkgadevi
 */
public class BLVO implements Serializable{

    private String blnum="";
    private String bookno="";
    private String loadportid="";
    private String dischargeportid="";
    private String departdate="";
    private String status="";
    private String ecomStatus="";
//    private String
    private String custrefno="";

    /**
     * @return the blnum
     */
    public String getBlnum() {
        return blnum;
    }

    /**
     * @param blnum the blnum to set
     */
    public void setBlnum(String blnum) {
        this.blnum = blnum;
    }

    /**
     * @return the bookno
     */
    public String getBookno() {
        return bookno;
    }

    /**
     * @param bookno the bookno to set
     */
    public void setBookno(String bookno) {
        this.bookno = bookno;
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
     * @return the departdate
     */
    public String getDepartdate() {
        return departdate;
    }

    /**
     * @param departdate the departdate to set
     */
    public void setDepartdate(String departdate) {
        this.departdate = departdate;
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
     * @return the custrefno
     */
    public String getCustrefno() {
        return custrefno;
    }

    /**
     * @param custrefno the custrefno to set
     */
    public void setCustrefno(String custrefno) {
        this.custrefno = custrefno;
    }

    /**
     * @return the ecomStatus
     */
    public String getEcomStatus() {
        return ecomStatus;
    }

    /**
     * @param ecomStatus the ecomStatus to set
     */
    public void setEcomStatus(String ecomStatus) {
        this.ecomStatus = ecomStatus;
    }

}
