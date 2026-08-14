/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author deepa.p
 */
public class VGMVOModel implements Serializable{
    @SerializedName("vgmsolas")
    @Expose
    private List<VgmVO> vgmsolas = new ArrayList<VgmVO>();

    public List<VgmVO> getVgmsolas() {
        return vgmsolas;
    }

    public void setVgmsolas(List<VgmVO> vgmsolas) {
        this.vgmsolas = vgmsolas;
    }

    
}
