/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.tracking.vo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author sasikumar.r
 */
public class TrackingActvityResult implements Serializable {

    private String bookno;//   Booking No
    private String blno;   //   BL No
    private String containerno;    // Container number
    private String latestactivity;  //Latest Activity
    private String activitylocation;   //Activity Location
    private String containertype;      // container type
    private String activitydate;      // activity date
    private String pol;    //POL
    private String pod; //POD
    private String destination;    // Destination
    private String eta;    //ETA
// More Result
    private String moverment;  //Movement
    private String moveactivitydate; // Activitydate
    private String movelatestactivitylocation; // Activity Location
    private String Service;   // Service
    private String Vessel;
    private String voyage;
    private String bound;
    private String etd;
    private String polterminal;
    private String loadportid;
    private String polarrdate;
    private String divation;
    private String activityloccode;
    private String nextportetd;
    private String nextport;
    private String nextPod;
    private String nextPodAt;
    private String nextActualPort;
    private String nextActualEta;
    private boolean isHasNoMovementRecords;

    public boolean isHasNoMovementRecords() {
        return isHasNoMovementRecords;
    }

    public void setIsHasNoMovementRecords(boolean isHasNoMovementRecords) {
        this.isHasNoMovementRecords = isHasNoMovementRecords;
    }

    private String result;

    public String getService() {
        return Service;
    }

    public void setService(String Service) {
        this.Service = Service;
    }

    public String getVessel() {
        return Vessel;
    }

    public void setVessel(String Vessel) {
        this.Vessel = Vessel;
    }

    public String getActivitydate() {
        return activitydate;
    }

    public void setActivitydate(String activitydate) {
        this.activitydate = activitydate;
    }

    public String getActivitylocation() {
        return activitylocation;
    }

    public void setActivitylocation(String activitylocation) {
        this.activitylocation = activitylocation;
    }

    public String getBlno() {
        return blno;
    }

    public void setBlno(String blno) {
        this.blno = blno;
    }

    public String getBookno() {
        return bookno;
    }

    public void setBookno(String bookno) {
        this.bookno = bookno;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getContainerno() {
        return containerno;
    }

    public void setContainerno(String containerno) {
        this.containerno = containerno;
    }

    public String getContainertype() {
        return containertype;
    }

    public void setContainertype(String containertype) {
        this.containertype = containertype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getLatestactivity() {
        return latestactivity;
    }

    public void setLatestactivity(String latestactivity) {
        this.latestactivity = latestactivity;
    }

    public String getMoveactivitydate() {
        return moveactivitydate;
    }

    public void setMoveactivitydate(String moveactivitydate) {
        this.moveactivitydate = moveactivitydate;
    }

    public String getMovelatestactivitylocation() {
        return movelatestactivitylocation;
    }

    public void setMovelatestactivitylocation(String movelatestactivitylocation) {
        this.movelatestactivitylocation = movelatestactivitylocation;
    }

    public String getMoverment() {
        return moverment;
    }

    public void setMoverment(String moverment) {
        this.moverment = moverment;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
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

    public String getPolarrdate() {
        return polarrdate;
    }

    public void setPolarrdate(String polarrdate) {
        this.polarrdate = polarrdate;
    }

    public String getDivation() {
        return divation;
    }

    public void setDivation(String divation) {
        this.divation = divation;
    }

    public String getActivityloccode() {
        return activityloccode;
    }

    public void setActivityloccode(String activityloccode) {
        this.activityloccode = activityloccode;
    }

    public String getNextportetd() {
        return nextportetd;
    }

    public void setNextportetd(String nextportetd) {
        this.nextportetd = nextportetd;
    }

    public String getNextport() {
        return nextport;
    }

    public void setNextport(String nextport) {
        this.nextport = nextport;
    }

    public String getNextPod() {
        return nextPod;
    }

    public void setNextPod(String nextPod) {
        this.nextPod = nextPod;
    }

    public String getNextPodAt() {
        return nextPodAt;
    }

    public void setNextPodAt(String nextPodAt) {
        this.nextPodAt = nextPodAt;
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
