/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.si.vo;

import java.io.Serializable;
import java.util.ArrayList;


public class SiTemp extends NonSubmittedBookingsVO implements Serializable {
    private String strBookingNo = null;
    private String strStatus = null;
    private String strSIDocId = null;
    private SIUserrequestVo siUserRequestVoObj;
    private ArrayList lstObj = null;

    /**
     * @return the strBookingNo
     */
    public String getStrBookingNo() {
        return strBookingNo;
    }

    /**
     * @param strBookingNo the strBookingNo to set
     */
    public void setStrBookingNo(String strBookingNo) {
        this.strBookingNo = strBookingNo;
    }

    /**
     * @return the strStatus
     */
    public String getStrStatus() {
        return strStatus;
    }

    /**
     * @param strStatus the strStatus to set
     */
    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    /**
     * @return the strSIDocId
     */
    public String getStrSIDocId() {
        return strSIDocId;
    }

    /**
     * @param strSIDocId the strSIDocId to set
     */
    public void setStrSIDocId(String strSIDocId) {
        this.strSIDocId = strSIDocId;
    }

    /**
     * @return the siUserRequestVoObj
     */
    public SIUserrequestVo getSiUserRequestVoObj() {
        return siUserRequestVoObj;
    }

    /**
     * @param siUserRequestVoObj the siUserRequestVoObj to set
     */
    public void setSiUserRequestVoObj(SIUserrequestVo siUserRequestVoObj) {
        this.siUserRequestVoObj = siUserRequestVoObj;
    }

    /**
     * @return the lstObj
     */
    public ArrayList getLstObj() {
        return lstObj;
    }

    /**
     * @param lstObj the lstObj to set
     */
    public void setLstObj(ArrayList lstObj) {
        this.lstObj = lstObj;
    }


}
