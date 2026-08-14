/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.com.ecom.ws.vo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Location")
public class ScheduleBlwLocationVo implements Serializable {

    private String service = "";
    private String voyage = "";
    private String bound = "";
    private String port = "";
    private String arrdate = "";
    private String depdate = "";
    private String type = "";
    private String terminal = "";
    private String vessel = "";
    private String portname = "";
    private String vesselname = "";
    private String terminalname = "";
    private String podeta = "";
    private String podetd = "";
    private String pod = null;
    private String podname = null;
    private String podterminal = null;
    private String groupcallid = null;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPodname() {
        return podname;
    }

    public String getPodterminal() {
        return podterminal;
    }

    public void setPodterminal(String podterminal) {
        this.podterminal = podterminal;
    }

    public void setPodname(String podname) {
        this.podname = podname;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the voyage
     */
    public String getVoyage() {

        return voyage;
    }

    /**
     * @param voyage the voyage to set
     */
    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    /**
     * @return the bound
     */
    public String getBound() {

        return bound;
    }

    /**
     * @param bound the bound to set
     */
    public void setBound(String bound) {
        this.bound = bound;
    }

    /**
     * @return the port
     */
    public String getPort() {

        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the arrdate
     */
    public String getArrdate() {

        return arrdate;
    }

    /**
     * @param arrdate the arrdate to set
     */
    public void setArrdate(String arrdate) {
        this.arrdate = arrdate;
    }

    /**
     * @return the depdate
     */
    public String getDepdate() {
        return depdate;
    }

    /**
     * @param depdate the depdate to set
     */
    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }

    /**
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the terminal
     */
    public String getTerminal() {

        return terminal;
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(String terminal) {
        this.terminal = terminal;
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
     * @return the portname
     */
    public String getPortname() {

        return portname;
    }

    /**
     * @param portname the portname to set
     */
    public void setPortname(String portname) {
        this.portname = portname;
    }

    /**
     * @return the vesselname
     */
    public String getVesselname() {

        return vesselname;
    }

    /**
     * @param vesselname the vesselname to set
     */
    public void setVesselname(String vesselname) {
        this.vesselname = vesselname;
    }

    /**
     * @return the terminalname
     */
    public String getTerminalname() {

        return terminalname;
    }

    /**
     * @param terminalname the terminalname to set
     */
    public void setTerminalname(String terminalname) {
        this.terminalname = terminalname;
    }

    /**
     * @return the podeta
     */
    public String getPodeta() {
        return podeta;
    }

    /**
     * @param podeta the podeta to set
     */
    public void setPodeta(String podeta) {
        this.podeta = podeta;
    }

    /**
     * @return the podetd
     */
    public String getPodetd() {
        return podetd;
    }

    /**
     * @param podetd the podetd to set
     */
    public void setPodetd(String podetd) {
        this.podetd = podetd;
    }

    /**
     * @return the groupcallid
     */
    public String getGroupcallid() {
        return groupcallid;
    }

    /**
     * @param groupcallid the groupcallid to set
     */
    public void setGroupcallid(String groupcallid) {
        this.groupcallid = groupcallid;
    }
}
