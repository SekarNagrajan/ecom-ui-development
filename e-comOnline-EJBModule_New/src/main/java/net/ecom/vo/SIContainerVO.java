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
public class SIContainerVO implements Serializable 
{
    private String containerno=null;
    private String type=null;
    private String carrierseal = null;
    private String shipperseal = null;
    private String otherseal = null;
    private String commodity = null;
    private String cargowt = null;
    private String unit = null;

    private String commoditygrp=null;
    private String itemqty=null;

    /**
     * @return the containerno
     */
    public String getContainerno() {
        return containerno;
    }

    /**
     * @param containerno the containerno to set
     */
    public void setContainerno(String containerno) {
        this.containerno = containerno;
    }

    /**
     * @return the carrierseal
     */
    public String getCarrierseal() {
        return carrierseal;
    }

    /**
     * @param carrierseal the carrierseal to set
     */
    public void setCarrierseal(String carrierseal) {
        this.carrierseal = carrierseal;
    }

    /**
     * @return the shipperseal
     */
    public String getShipperseal() {
        return shipperseal;
    }

    /**
     * @param shipperseal the shipperseal to set
     */
    public void setShipperseal(String shipperseal) {
        this.shipperseal = shipperseal;
    }

    /**
     * @return the otherseal
     */
    public String getOtherseal() {
        return otherseal;
    }

    /**
     * @param otherseal the otherseal to set
     */
    public void setOtherseal(String otherseal) {
        this.otherseal = otherseal;
    }

    /**
     * @return the commodity
     */
    public String getCommodity() {
        return commodity;
    }

    /**
     * @param commodity the commodity to set
     */
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    /**
     * @return the cargowt
     */
    public String getCargowt() {
        return cargowt;
    }

    /**
     * @param cargowt the cargowt to set
     */
    public void setCargowt(String cargowt) {
        this.cargowt = cargowt;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

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
