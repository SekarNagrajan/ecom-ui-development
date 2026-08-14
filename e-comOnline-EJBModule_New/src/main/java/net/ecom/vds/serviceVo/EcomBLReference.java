/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class EcomBLReference implements Serializable {
    @SerializedName("bl_no")
    @Expose
    private String bl_no;
    
    @SerializedName("refid")
    @Expose
    private String refid;
    
    @SerializedName("refval")
    @Expose
    private String refval;

    public String getBl_no() {
        return bl_no;
    }

    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getRefval() {
        return refval;
    }

    public void setRefval(String refval) {
        this.refval = refval;
    }
}
