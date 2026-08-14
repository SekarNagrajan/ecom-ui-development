/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author arunprasath
 */
public class SIDtableVO implements Serializable
{
private String[] commodity;
private String[] weight;
private String[] qty;
private String[] unit;

    /**
     * @return the commodity
     */
    public String[] getCommodity() {
        return commodity;
    }

    /**
     * @param commodity the commodity to set
     */
    public void setCommodity(String[] commodity) {
        this.commodity = commodity;
    }

    /**
     * @return the weight
     */
    public String[] getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(String[] weight) {
        this.weight = weight;
    }

    /**
     * @return the qty
     */
    public String[] getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String[] qty) {
        this.qty = qty;
    }

    /**
     * @return the unit
     */
    public String[] getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String[] unit) {
        this.unit = unit;
    }


}
