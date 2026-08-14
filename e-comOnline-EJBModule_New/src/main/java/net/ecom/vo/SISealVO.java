/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author udhaya
 */
public class SISealVO implements Serializable {

    private String strShipperSeal = "";
    private String strCarrierSeal = "";
    private String strIsSelect = "NO";
    private String strCargoid = "";

    /**
     * @return the strShipperSeal
     */
    public String getStrShipperSeal() {
        return Validation.checkNull(strShipperSeal);
    }

    /**
     * @param strShipperSeal the strShipperSeal to set
     */
    public void setStrShipperSeal(String strShipperSeal) {
        this.strShipperSeal = strShipperSeal;
    }

    /**
     * @return the strCarrierSeal
     */
    public String getStrCarrierSeal() {
        return Validation.checkNull(strCarrierSeal);
    }

    /**
     * @param strCarrierSeal the strCarrierSeal to set
     */
    public void setStrCarrierSeal(String strCarrierSeal) {
        this.strCarrierSeal = strCarrierSeal;
    }

    /**
     * @return the strIsSelect
     */
    public String getStrIsSelect() {
        return Validation.checkNull(strIsSelect);
    }

    /**
     * @param strIsSelect the strIsSelect to set
     */
    public void setStrIsSelect(String strIsSelect) {
        this.strIsSelect = strIsSelect;
    }

    /**
     * @return the strCargoid
     */
    public String getStrCargoid() {
        return Validation.checkNull(strCargoid);
    }

    /**
     * @param strCargoid the strCargoid to set
     */
    public void setStrCargoid(String strCargoid) {
        this.strCargoid = strCargoid;
    }
}
