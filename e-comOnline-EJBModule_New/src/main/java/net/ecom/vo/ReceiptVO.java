/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author arunprasath
 */
public class ReceiptVO implements Serializable 
{
    private String recno = null;
    private String recdate = null;
    private String recamt = null;
    private String invnum = null;
    private String blnum = null;
    private String bookno= null;
    private String vessel = null;
    private String voycode = null;
    private String refnum = null;
    private String custcode = null;


    /**
     * @return the recno
     */
    public String getRecno() {
        return recno;
    }

    /**
     * @param recno the recno to set
     */
    public void setRecno(String recno) {
        this.recno = recno;
    }

    /**
     * @return the recdate
     */
    public String getRecdate() {
        return recdate;
    }

    /**
     * @param recdate the recdate to set
     */
    public void setRecdate(String recdate) {
        this.recdate = recdate;
    }

    /**
     * @return the recamt
     */
    public String getRecamt() {
        return recamt;
    }

    /**
     * @param recamt the recamt to set
     */
    public void setRecamt(String recamt) {
        this.recamt = recamt;
    }

    /**
     * @return the invnum
     */
    public String getInvnum() {
        return invnum;
    }

    /**
     * @param invnum the invnum to set
     */
    public void setInvnum(String invnum) {
        this.invnum = invnum;
    }

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
     * @return the vessel
     */
    public String getVessel() {
        return vessel;
    }

    /**
     * @param vessel the vessel to set
     */
    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    /**
     * @return the voycode
     */
    public String getVoycode() {
        return voycode;
    }

    /**
     * @param voycode the voycode to set
     */
    public void setVoycode(String voycode) {
        this.voycode = voycode;
    }

    /**
     * @return the refnum
     */
    public String getRefnum() {
        return refnum;
    }

    /**
     * @param refnum the refnum to set
     */
    public void setRefnum(String refnum) {
        this.refnum = refnum;
    }

    /**
     * @return the custcode
     */
    public String getCustcode() {
        return custcode;
    }

    /**
     * @param custcode the custcode to set
     */
    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }




}
