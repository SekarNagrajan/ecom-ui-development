/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author priyadharshinim
 */
public class EcomBKGPrintVO {
    @SerializedName("conrelno")
    @Expose
    private String conrelno;

    @SerializedName("sessionid")
    @Expose
    private String sessionid;

    public String getConrelno() {
        return conrelno;
    }

    public void setConrelno(String conrelno) {
        this.conrelno = conrelno;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
    
    
}
