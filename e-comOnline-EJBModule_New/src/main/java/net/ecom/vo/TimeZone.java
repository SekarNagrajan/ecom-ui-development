/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class TimeZone implements Serializable {

    String tzid;
    String location;
    String locationdisplay;

    public String getTzid() {
        return tzid;
    }

    public void setTzid(String tzid) {
        this.tzid = tzid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationdisplay() {
        return locationdisplay;
    }

    public void setLocationdisplay(String locationdisplay) {
        this.locationdisplay = locationdisplay;
    }

}
