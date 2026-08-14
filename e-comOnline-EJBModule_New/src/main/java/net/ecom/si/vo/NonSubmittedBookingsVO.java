/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.si.vo;

import java.io.Serializable;

/**
 *
 * @author anbuselvi
 */
public class NonSubmittedBookingsVO extends RetrieveBooking implements Serializable{
    private String strDate = null;
    private String strReferenceNo = null;

    /**
     * @return the strDate
     */
    public String getStrDate() {
        return strDate;
    }

    /**
     * @param strDate the strDate to set
     */
    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

       /**
     * @return the strReferenceNo
     */
    public String getStrReferenceNo() {
        return strReferenceNo;
    }

    /**
     * @param strReferenceNo the strReferenceNo to set
     */
    public void setStrReferenceNo(String strReferenceNo) {
        this.strReferenceNo = strReferenceNo;
    }

}
