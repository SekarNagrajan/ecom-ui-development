/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

/**
 *
 * @author sasikumar.r
 */
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleDataModel implements Serializable {

    @SerializedName("allschedules")
    @Expose
    private List<Allschedules> allschedules = new ArrayList<Allschedules>();

    public List<Allschedules> getAllschedules() {
        return allschedules;
    }

    public void setAllschedules(List<Allschedules> allschedules) {
        this.allschedules = allschedules;
    }

}
