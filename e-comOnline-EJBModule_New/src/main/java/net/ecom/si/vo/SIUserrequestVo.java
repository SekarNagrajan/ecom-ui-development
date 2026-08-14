/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import java.io.Serializable;

/**
 *
 * @author abirami
 */
public class SIUserrequestVo implements Serializable {

    private String strUserId = null;
    private int iRequestedSi;
    private int iCreatedSi;
    private String strCustCode=null;
    private boolean blManyBookingToSingleSi;// by balaji.d use  multiple booking for single si

    /**
     * @return the strUserId
     */
    public String getStrUserId() {
        return strUserId;
    }

    /**
     * @param strUserId the strUserId to set
     */
    public void setStrUserId(String strUserId) {
        this.strUserId = strUserId;
    }

    /**
     * @return the iRequestedSi
     */
    public int getiRequestedSi() {
        return iRequestedSi;
    }

    /**
     * @param iRequestedSi the iRequestedSi to set
     */
    public void setiRequestedSi(int iRequestedSi) {
        this.iRequestedSi = iRequestedSi;
    }

    /**
     * @return the iCreatedSi
     */
    public int getiCreatedSi() {
        return iCreatedSi;
    }

    /**
     * @param iCreatedSi the iCreatedSi to set
     */
    public void setiCreatedSi(int iCreatedSi) {
        this.iCreatedSi = iCreatedSi;
    }

    /**
     * @return the strCustCode
     */
    public String getStrCustCode() {
        return strCustCode;
    }

    /**
     * @param strCustCode the strCustCode to set
     */
    public void setStrCustCode(String strCustCode) {
        this.strCustCode = strCustCode;
    }

    /**
     * @return the blManyBookingToSingleSi
     */
    public boolean isBlManyBookingToSingleSi() {
        return blManyBookingToSingleSi;
    }

    /**
     * @param blManyBookingToSingleSi the blManyBookingToSingleSi to set
     */
    public void setBlManyBookingToSingleSi(boolean blManyBookingToSingleSi) {
        this.blManyBookingToSingleSi = blManyBookingToSingleSi;
    }
}
