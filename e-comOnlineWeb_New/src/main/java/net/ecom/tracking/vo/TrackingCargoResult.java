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
public class TrackingCargoResult implements Serializable {

    private String message;
    private String code;
    private List<TrackingMasterVo> result;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the result
     */
    public List<TrackingMasterVo> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<TrackingMasterVo> result) {
        this.result = result;
    }
}
