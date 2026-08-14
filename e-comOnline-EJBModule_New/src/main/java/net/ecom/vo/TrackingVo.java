/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;



/**
 *
 * @author Friends
 */
public class TrackingVo implements Serializable {
    private String strContainerNo=null;
    private String strBlNo=null;
    private String strBookNo=null;
    private String strPOLETA=null;
    private String strCurMovement=null;
    private String strPOL=null;
    private String strPOD=null;
    private String strCurrentPosition=null;

    /**
     * @return the strContainerNo
     */
    public String getStrContainerNo() {
        return Validation.checkNull(strContainerNo);
    }

    /**
     * @param strContainerNo the strContainerNo to set
     */
    public void setStrContainerNo(String strContainerNo) {
        this.strContainerNo = strContainerNo;
    }

    /**
     * @return the strBlNo
     */
    public String getStrBlNo() {
        return Validation.checkNull(strBlNo);
    }

    /**
     * @param strBlNo the strBlNo to set
     */
    public void setStrBlNo(String strBlNo) {
        this.strBlNo = strBlNo;
    }

    /**
     * @return the strBookNo
     */
    public String getStrBookNo() {
        return Validation.checkNull(strBookNo);
    }

    /**
     * @param strBookNo the strBookNo to set
     */
    public void setStrBookNo(String strBookNo) {
        this.strBookNo = strBookNo;
    }

    /**
     * @return the strPOLETA
     */
    public String getStrPOLETA() {
        return Validation.checkNull(strPOLETA);
    }

    /**
     * @param strPOLETA the strPOLETA to set
     */
    public void setStrPOLETA(String strPOLETA) {
        this.strPOLETA = strPOLETA;
    }

    /**
     * @return the strCurMovement
     */
    public String getStrCurMovement() {
        return Validation.checkNull(strCurMovement);
    }

    /**
     * @param strCurMovement the strCurMovement to set
     */
    public void setStrCurMovement(String strCurMovement) {
        this.strCurMovement = strCurMovement;
    }

    /**
     * @return the strPOL
     */
    public String getStrPOL() {
        return Validation.checkNull(strPOL);
    }

    /**
     * @param strPOL the strPOL to set
     */
    public void setStrPOL(String strPOL) {
        this.strPOL = strPOL;
    }

    /**
     * @return the strPOD
     */
    public String getStrPOD() {
        return Validation.checkNull(strPOD);
    }

    /**
     * @param strPOD the strPOD to set
     */
    public void setStrPOD(String strPOD) {
        this.strPOD = strPOD;
    }

    /**
     * @return the strCurrentPosition
     */
    public String getStrCurrentPosition() {
        return  Validation.checkNull(strCurrentPosition);
    }

    /**
     * @param strCurrentPosition the strCurrentPosition to set
     */
    public void setStrCurrentPosition(String strCurrentPosition) {
        this.strCurrentPosition = strCurrentPosition;
    }
    
    
}
