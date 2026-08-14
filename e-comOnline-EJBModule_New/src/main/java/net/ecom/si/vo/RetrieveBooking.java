/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.si.vo;

import java.io.Serializable;

/**
 *
 * @author anbuselvi
 */
public class RetrieveBooking implements Serializable {
    private PortVo polObj = null;
    private PortVo podObj = null;
    private String strAction = null;
    private String strDatasource = null;
    private String strDescription = null;
    private String strBookId  = null;
    /**
     * @return the polObj
     */
    public PortVo getPolObj() {
        return polObj;
    }

    /**
     * @param polObj the polObj to set
     */
    public void setPolObj(PortVo polObj) {
        this.polObj = polObj;
    }

    /**
     * @return the podObj
     */
    public PortVo getPodObj() {
        return podObj;
    }

    /**
     * @param podObj the podObj to set
     */
    public void setPodObj(PortVo podObj) {
        this.podObj = podObj;
    }

    /**
     * @return the strAction
     */
    public String getStrAction() {
        return strAction;
    }

    /**
     * @param strAction the strAction to set
     */
    public void setStrAction(String strAction) {
        this.strAction = strAction;
    }

    /**
     * @return the strDatasource
     */
    public String getStrDatasource() {
        return strDatasource;
    }

    /**
     * @param strDatasource the strDatasource to set
     */
    public void setStrDatasource(String strDatasource) {
        this.strDatasource = strDatasource;
    }

    /**
     * @return the strDescription
     */
    public String getStrDescription() {
        return strDescription;
    }

    /**
     * @param strDescription the strDescription to set
     */
    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    /**
     * @return the strBookId
     */
    public String getStrBookId() {
        return strBookId;
    }

    /**
     * @param strBookId the strBookId to set
     */
    public void setStrBookId(String strBookId) {
        this.strBookId = strBookId;
    }

}
