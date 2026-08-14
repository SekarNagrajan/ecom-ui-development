/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.tracking.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sasikumar.r
 */
public class TrackingMasterVo implements Serializable {

    private String blNo;
    private String bookNo;
    private String originPortDesc;
    private String finalPortDesc;
    private String podAt;
    private String polAt;
    private String eta;
    private String etd;
    private String inlandEta;
    private String dischargePortDesc;
    private String loadarrdate;
    private String polterminal;
    private String loadportid;
    private List<TrackingCargoActivityResult> movementList = new ArrayList<TrackingCargoActivityResult>();
    private List<TrackingProtActivityResult> portacklist = new ArrayList<TrackingProtActivityResult>();
    private String originPortCode;
    private String deliveryPortCode;
    private String nextPod;
    private String nextPodat;
    private String nextActualPort;
    private String nextActualEta;

    /**
     * @return the blNo
     */
    public String getBlNo() {
        return blNo;
    }

    /**
     * @param blNo the blNo to set
     */
    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    /**
     * @return the bookNo
     */
    public String getBookNo() {
        return bookNo;
    }

    /**
     * @param bookNo the bookNo to set
     */
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    /**
     * @return the originPortDesc
     */
    public String getOriginPortDesc() {
        return originPortDesc;
    }

    /**
     * @param originPortDesc the originPortDesc to set
     */
    public void setOriginPortDesc(String originPortDesc) {
        this.originPortDesc = originPortDesc;
    }

    /**
     * @return the finalPortDesc
     */
    public String getFinalPortDesc() {
        return finalPortDesc;
    }

    /**
     * @param finalPortDesc the finalPortDesc to set
     */
    public void setFinalPortDesc(String finalPortDesc) {
        this.finalPortDesc = finalPortDesc;
    }

    /**
     * @return the podAt
     */
    public String getPodAt() {
        return podAt;
    }

    /**
     * @param podAt the podAt to set
     */
    public void setPodAt(String podAt) {
        this.podAt = podAt;
    }

    /**
     * @return the eta
     */
    public String getEta() {
        return eta;
    }

    /**
     * @param eta the eta to set
     */
    public void setEta(String eta) {
        this.eta = eta;
    }

    /**
     * @return the etb
     */
    public String getEtd() {
        return etd;
    }

    /**
     * @param etb the etb to set
     */
    public void setEtd(String etd) {
        this.etd = etd;
    }

    /**
     * @return the inlandEta
     */
    public String getInlandEta() {
        return inlandEta;
    }

    /**
     * @param inlandEta the inlandEta to set
     */
    public void setInlandEta(String inlandEta) {
        this.inlandEta = inlandEta;
    }

    /**
     * @return the dischargePortDesc
     */
    public String getDischargePortDesc() {
        return dischargePortDesc;
    }

    /**
     * @param dischargePortDesc the dischargePortDesc to set
     */
    public void setDischargePortDesc(String dischargePortDesc) {
        this.dischargePortDesc = dischargePortDesc;
    }

    /**
     * @return the movementList
     */
    public List<TrackingCargoActivityResult> getMovementList() {
        return movementList;
    }

    /**
     * @param movementList the movementList to set
     */
    public void setMovementList(List<TrackingCargoActivityResult> movementList) {
        this.movementList = movementList;
    }

    public String getPolAt() {
        return polAt;
    }

    public void setPolAt(String polAt) {
        this.polAt = polAt;
    }

    public String getLoadarrdate() {
        return loadarrdate;
    }

    public void setLoadarrdate(String loadarrdate) {
        this.loadarrdate = loadarrdate;
    }

    public String getPolterminal() {
        return polterminal;
    }

    public void setPolterminal(String polterminal) {
        this.polterminal = polterminal;
    }

    public String getLoadportid() {
        return loadportid;
    }

    public void setLoadportid(String loadportid) {
        this.loadportid = loadportid;
    }

    public List<TrackingProtActivityResult> getPortacklist() {
        return portacklist;
    }

    public void setPortacklist(List<TrackingProtActivityResult> portacklist) {
        this.portacklist = portacklist;
    }

    public String getOriginPortCode() {
        return originPortCode;
    }

    public void setOriginPortCode(String originPortCode) {
        this.originPortCode = originPortCode;
    }

    public String getDeliveryPortCode() {
        return deliveryPortCode;
    }

    public void setDeliveryPortCode(String deliveryPortCode) {
        this.deliveryPortCode = deliveryPortCode;
    }

    public String getNextPod() {
        return nextPod;
    }

    public void setNextPod(String nextPod) {
        this.nextPod = nextPod;
    }

    public String getNextPodat() {
        return nextPodat;
    }

    public void setNextPodat(String nextPodat) {
        this.nextPodat = nextPodat;
    }
    public String getNextActualPort() {
        return nextActualPort;
    }
    public void setNextActualPort(String nextActualPort) {
        this.nextActualPort = nextActualPort;
    }
    public String getNextActualEta() {
        return nextActualEta;
    }
    public void setNextActualEta(String nextActualEta) {
        this.nextActualEta = nextActualEta;
    }   

}
