package net.ecom.vo;

import java.io.Serializable;

/**
 * Value Object for Mask Port data
 * Contains port, mport, status, portname, mportname
 */
public class MaskPortVO implements Serializable {
    private String port;
    private String mport;
    private String status;
    private String portname;
    private String mportname;

   
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMport() {
        return mport;
    }

    public void setMport(String mport) {
        this.mport = mport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
    }

    public String getMportname() {
        return mportname;
    }

    public void setMportname(String mportname) {
        this.mportname = mportname;
    }
}

