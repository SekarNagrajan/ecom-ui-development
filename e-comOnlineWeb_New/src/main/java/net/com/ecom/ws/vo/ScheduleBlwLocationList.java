/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.com.ecom.ws.vo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sasikumar.r
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ScheduleBlwLocation")
public class ScheduleBlwLocationList {

     @XmlElement(name = "Location")
    private List<ScheduleBlwLocationVo> schedulbtlocation=null;

    /**
     * @return the schedulbtlocation
     */
    public List<ScheduleBlwLocationVo> getSchedulbtlocation() {
        return schedulbtlocation;
    }

    /**
     * @param schedulbtlocation the schedulbtlocation to set
     */
    public void setSchedulbtlocation(List<ScheduleBlwLocationVo> schedulbtlocation) {
        this.schedulbtlocation = schedulbtlocation;
    }

 
}
