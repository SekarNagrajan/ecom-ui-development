/*
 * Routingdto.java
 *
 * Created on December 29, 2007, 6:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author udhaya
 */
public class RoutingVO implements Serializable  {

    /** Creates a new instance of Routingdto */
    public RoutingVO() {
    }
    private String origin = null;
    private String pol = null;
    private String polTerminal = null;
    private String polservice = null;
    private String mode = null;
    private String ts1 = null;
    private String trans1 = null;
    private String terminal1 = null;
    private String service1 = null;
    private String ts2 = null;
    private String trans2 = null;
    private String terminal2 = null;
    private String service2 = null;
    private String ts3 = null;
    private String trans3 = null;
    private String terminal3 = null;
    private String service3 = null;
    private String pod = null;
    private String podterminal = null;
    private String destination = null;
    private String defRoute = null;
    private String mainservice = null;
    private String originMode = null;
    private String deliveryMode = null;
    private String portname = null;
    private String Dportname = null;
    private String blpodname = null;
    private String blpodDelivery = null;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPolTerminal() {
        return polTerminal;
    }

    public void setPolTerminal(String polTerminal) {
        this.polTerminal = polTerminal;
    }

    public String getPolservice() {
        return polservice;
    }

    public void setPolservice(String polservice) {
        this.polservice = polservice;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTs1() {
        return ts1;
    }

    public void setTs1(String ts1) {
        this.ts1 = ts1;
    }

    public String getTrans1() {
        return trans1;
    }

    public void setTrans1(String trans1) {
        this.trans1 = trans1;
    }

    public String getTerminal1() {
        return terminal1;
    }

    public void setTerminal1(String terminal1) {
        this.terminal1 = terminal1;
    }

    public String getService1() {
        return service1;
    }

    public void setService1(String service1) {
        this.service1 = service1;
    }

    public String getTs2() {
        return ts2;
    }

    public void setTs2(String ts2) {
        this.ts2 = ts2;
    }

    public String getTrans2() {
        return trans2;
    }

    public void setTrans2(String trans2) {
        this.trans2 = trans2;
    }

    public String getTerminal2() {
        return terminal2;
    }

    public void setTerminal2(String terminal2) {
        this.terminal2 = terminal2;
    }

    public String getService2() {
        return service2;
    }

    public void setService2(String service2) {
        this.service2 = service2;
    }

    public String getTs3() {
        return ts3;
    }

    public void setTs3(String ts3) {
        this.ts3 = ts3;
    }

    public String getTrans3() {
        return trans3;
    }

    public void setTrans3(String trans3) {
        this.trans3 = trans3;
    }

    public String getTerminal3() {
        return terminal3;
    }

    public void setTerminal3(String terminal3) {
        this.terminal3 = terminal3;
    }

    public String getService3() {
        return service3;
    }

    public void setService3(String service3) {
        this.service3 = service3;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPodterminal() {
        return podterminal;
    }

    public void setPodterminal(String podterminal) {
        this.podterminal = podterminal;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDefRoute() {
        return defRoute;
    }

    public void setDefRoute(String defRoute) {
        this.defRoute = defRoute;
    }

    public String getMainservice() {
        return mainservice;
    }

    public void setMainservice(String mainservice) {
        this.mainservice = mainservice;
    }

    public String getOriginMode() {
        return originMode;
    }

    public void setOriginMode(String originMode) {
        this.originMode = originMode;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
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
     * @return the dportname
     */
    public String getDportname() {
        return Dportname;
    }

    /**
     * @param dportname the dportname to set
     */
    public void setDportname(String dportname) {
        this.Dportname = dportname;
    }

    /**
     * @return the blpodname
     */
    public String getBlpodname() {
        return blpodname;
    }

    /**
     * @param blpodname the blpodname to set
     */
    public void setBlpodname(String blpodname) {
        this.blpodname = blpodname;
    }

    /**
     * @return the blpodDelivery
     */
    public String getBlpodDelivery() {
        return blpodDelivery;
    }

    /**
     * @param blpodDelivery the blpodDelivery to set
     */
    public void setBlpodDelivery(String blpodDelivery) {
        this.blpodDelivery = blpodDelivery;
    }
}
