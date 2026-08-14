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
@XmlRootElement(name = "TrackingDetails")
public class MovementDetailsList {

    @XmlElement(name = "tracking")
    private List<MovementDetailsVo> movementdetailsvo=null;

    /**
     * @return the movementdetailsvo
     */
    public List<MovementDetailsVo> getMovementdetailsvo() {
        return movementdetailsvo;
    }

    /**
     * @param movementdetailsvo the movementdetailsvo to set
     */
    public void setMovementdetailsvo(List<MovementDetailsVo> movementdetailsvo) {
        this.movementdetailsvo = movementdetailsvo;
    }

}
