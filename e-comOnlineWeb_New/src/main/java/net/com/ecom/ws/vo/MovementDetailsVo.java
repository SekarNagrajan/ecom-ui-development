/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.com.ecom.ws.vo;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tracking")
public class MovementDetailsVo {

    private String strMovement = null;
    private String strActivityDate = null;
    private String strActivityLoc = null;
    private String strService = null;
    private String strVessel = null;
    private String strVoyage = null;
    private String strBound = null;
    private String strBlNo = null;
    private String strbookingNo = null;
    private String strPOl = null;
    private String strPOD = null;
    private String strPODETA = null;
    private String strNXTPODETA = null;
    private String strContainerNo = null;
    private String strValidRefer = null;
    private String strEQPType = null;


    private String strPLD=null;
    private String transitdate=null;

    /**
     * @return the strMovement
     */
    public String getStrMovement() {
        return strMovement;
    }

    /**
     * @param strMovement the strMovement to set
     */
    public void setStrMovement(String strMovement) {
        this.strMovement = strMovement;
    }

    /**
     * @return the strActivityDate
     */
    public String getStrActivityDate() {
        return strActivityDate;
    }

    /**
     * @param strActivityDate the strActivityDate to set
     */
    public void setStrActivityDate(String strActivityDate) {
        this.strActivityDate = strActivityDate;
    }

    /**
     * @return the strActivityLoc
     */
    public String getStrActivityLoc() {
        return strActivityLoc;
    }

    /**
     * @param strActivityLoc the strActivityLoc to set
     */
    public void setStrActivityLoc(String strActivityLoc) {
        this.strActivityLoc = strActivityLoc;
    }

    /**
     * @return the strService
     */
    public String getStrService() {
        return strService;
    }

    /**
     * @param strService the strService to set
     */
    public void setStrService(String strService) {
        this.strService = strService;
    }

    /**
     * @return the strVessel
     */
    public String getStrVessel() {
        return strVessel;
    }

    /**
     * @param strVessel the strVessel to set
     */
    public void setStrVessel(String strVessel) {
        this.strVessel = strVessel;
    }

    /**
     * @return the strVoyage
     */
    public String getStrVoyage() {
        return strVoyage;
    }

    /**
     * @param strVoyage the strVoyage to set
     */
    public void setStrVoyage(String strVoyage) {
        this.strVoyage = strVoyage;
    }

    /**
     * @return the strBound
     */
    public String getStrBound() {
        return strBound;
    }

    /**
     * @param strBound the strBound to set
     */
    public void setStrBound(String strBound) {
        this.strBound = strBound;
    }

    /**
     * @return the strBlNo
     */
    public String getStrBlNo() {
        return strBlNo;
    }

    /**
     * @param strBlNo the strBlNo to set
     */
    public void setStrBlNo(String strBlNo) {
        this.strBlNo = strBlNo;
    }

    /**
     * @return the strbookingNo
     */
    public String getStrbookingNo() {
        return strbookingNo;
    }

    /**
     * @param strbookingNo the strbookingNo to set
     */
    public void setStrbookingNo(String strbookingNo) {
        this.strbookingNo = strbookingNo;
    }

    /**
     * @return the strPOl
     */
    public String getStrPOl() {
        return strPOl;
    }

    /**
     * @param strPOl the strPOl to set
     */
    public void setStrPOl(String strPOl) {
        this.strPOl = strPOl;
    }

    /**
     * @return the strPOD
     */
    public String getStrPOD() {
        return strPOD;
    }

    /**
     * @param strPOD the strPOD to set
     */
    public void setStrPOD(String strPOD) {
        this.strPOD = strPOD;
    }

    /**
     * @return the strPODETA
     */
    public String getStrPODETA() {
        return strPODETA;
    }

    /**
     * @param strPODETA the strPODETA to set
     */
    public void setStrPODETA(String strPODETA) {
        this.strPODETA = strPODETA;
    }

    //Added by Chandrahasan
    /**
     * @return the strNXTPODETA
     */
    public String getStrNxtPODETA() {
        return strNXTPODETA;
    }

    /**
     * @param strNXTPODETA the strNXPODETA to set
     */
    public void setStrNxtPODETA(String strNXTPODETA) {
        this.strNXTPODETA = strNXTPODETA;
    }
    //

    /**
     * @return the strEQPType
     */
    public String getStrEQPType() {
        return strEQPType;
    }

    /**
     * @param strEQPType the strEQPType to set
     */
    public void setStrEQPType(String strEQPType) {
        this.strEQPType = strEQPType;
    }
    //

    /**
     * @return the strContainerNo
     */
    public String getStrContainerNo() {
        return strContainerNo;
    }

    /**
     * @param strContainerNo the strContainerNo to set
     */
    public void setStrContainerNo(String strContainerNo) {
        this.strContainerNo = strContainerNo;
    }

    /**
     * @return the strValidRefer
     */
    public String getStrValidRefer() {
        return strValidRefer;
    }

    /**
     * @param strValidRefer the strValidRefer to set
     */
    public void setStrValidRefer(String strValidRefer) {
        this.strValidRefer = strValidRefer;
    }

       public String getStrPLD() {
        return strPLD;
    }

    public void setStrPLD(String strPLD) {
        this.strPLD = strPLD;
    }

    public String getTransitdate() {
        return transitdate;
    }

    public void setTransitdate(String transitdate) {
        this.transitdate = transitdate;
    }

}
