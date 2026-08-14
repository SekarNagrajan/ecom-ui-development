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
public class SIContainerContentVO implements Serializable 
{
    private String commoditygrp=null;
    private String itemqty=null;

    /**
     * @return the commoditygrp
     */
    public String getCommoditygrp() {
        return commoditygrp;
    }

    /**
     * @param commoditygrp the commoditygrp to set
     */
    public void setCommoditygrp(String commoditygrp) {
        this.commoditygrp = commoditygrp;
    }

    /**
     * @return the itemqty
     */
    public String getItemqty() {
        return itemqty;
    }

    /**
     * @param itemqty the itemqty to set
     */
    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }
    

}
