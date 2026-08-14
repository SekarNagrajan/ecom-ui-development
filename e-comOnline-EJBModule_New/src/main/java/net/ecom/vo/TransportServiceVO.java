/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author sasikumar.r
 */
public class TransportServiceVO implements Serializable {
    private String trpcode=null;
    private String trpname=null;
    private String default_trpcode=null;
    private String default_trpname=null;

    /**
     * @return the trpcode
     */
    public String getTrpcode() {
        return trpcode;
    }

    /**
     * @param trpcode the trpcode to set
     */
    public void setTrpcode(String trpcode) {
        this.trpcode = trpcode;
    }

    /**
     * @return the trpname
     */
    public String getTrpname() {
        return trpname;
    }

    /**
     * @param trpname the trpname to set
     */
    public void setTrpname(String trpname) {
        this.trpname = trpname;
    }

    /**
     * @return the default_trpcode
     */
    public String getDefault_trpcode() {
        return default_trpcode;
    }

    /**
     * @param default_trpcode the default_trpcode to set
     */
    public void setDefault_trpcode(String default_trpcode) {
        this.default_trpcode = default_trpcode;
    }

    /**
     * @return the default_trpname
     */
    public String getDefault_trpname() {
        return default_trpname;
    }

    /**
     * @param default_trpname the default_trpname to set
     */
    public void setDefault_trpname(String default_trpname) {
        this.default_trpname = default_trpname;
    }

}
