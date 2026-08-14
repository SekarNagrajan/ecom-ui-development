/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.phase2.service.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author deepa.p
 */
public class Phase2Model implements Serializable{
    @SerializedName("delordmaster")
    @Expose
    private List<DOStatusUpdateVO> doStatusList = new ArrayList<DOStatusUpdateVO>();

    public List<DOStatusUpdateVO> getDoStatusList() {
        return doStatusList;
    }

    public void setDoStatusList(List<DOStatusUpdateVO> doStatusList) {
        this.doStatusList = doStatusList;
    }
}
