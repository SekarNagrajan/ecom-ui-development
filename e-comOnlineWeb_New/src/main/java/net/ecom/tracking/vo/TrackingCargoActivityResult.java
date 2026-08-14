/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.tracking.vo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author sasikumar.r
 */
public class TrackingCargoActivityResult implements Serializable {

    private String containerNo;
    private List<TrackingCargoActivity> containerActivity;
    private String nextportETD;

    /**
     * @return the containerNo
     */
    public String getContainerNo() {
        return containerNo;
    }

    /**
     * @param containerNo the containerNo to set
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    /**
     * @return the containerActivity
     */
    public List<TrackingCargoActivity> getContainerActivity() {
        return containerActivity;
    }

    /**
     * @param containerActivity the containerActivity to set
     */
    public void setContainerActivity(List<TrackingCargoActivity> containerActivity) {
        this.containerActivity = containerActivity;
    }

    public String getNextportETD() {
        return nextportETD;
    }

    public void setNextportETD(String nextportETD) {
        this.nextportETD = nextportETD;
    }

}
