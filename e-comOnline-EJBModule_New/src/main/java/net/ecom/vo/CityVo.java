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
public class CityVo implements Serializable {
   private String strCityName="";

    /**
     * @return the strCityName
     */
    public String getStrCityName() {
        return Validation.checkNull(strCityName);
    }

    /**
     * @param strCityName the strCityName to set
     */
    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }
   
}
