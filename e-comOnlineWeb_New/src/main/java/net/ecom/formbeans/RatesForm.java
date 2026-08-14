/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

/**
 *
 * @author gobinath.t
 */
public class RatesForm extends org.apache.struts.action.ActionForm {

    private String polcode;
    private String podcode;
    private String shipmentdate;
    private String eqptype;
    private String commodity;
    private int quantity;

    public String getPolcode() {
        return polcode;
    }

    public void setPolcode(String polcode) {
        this.polcode = polcode;
    }

    public String getPodcode() {
        return podcode;
    }

    public void setPodcode(String podcode) {
        this.podcode = podcode;
    }

    public String getShipmentdate() {
        return shipmentdate;
    }

    public void setShipmentdate(String shipmentdate) {
        this.shipmentdate = shipmentdate;
    }

    public String getEqptype() {
        return eqptype;
    }

    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
