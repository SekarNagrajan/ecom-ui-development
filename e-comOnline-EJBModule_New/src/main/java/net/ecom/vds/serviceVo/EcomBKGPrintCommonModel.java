/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author priyadharshinim
 */
public class EcomBKGPrintCommonModel {
    @SerializedName("rptbatchcro")
    @Expose
    private List<EcomBKGPrintVO> rptbatchcro = new ArrayList();

    public List<EcomBKGPrintVO> getRptbatchcro() {
        return rptbatchcro;
    }

    public void setRptbatchcro(List<EcomBKGPrintVO> rptbatchcro) {
        this.rptbatchcro = rptbatchcro;
    }

   
    
}
