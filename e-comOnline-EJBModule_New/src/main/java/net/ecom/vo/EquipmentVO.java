/*
 * Equipments.java
 *
 * Created on January 25, 2007, 5:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author muthu
 */
public class EquipmentVO implements Serializable {
    
    private String equipmenttype;
    private String equipmentdesc;
    
    public String getEquipmenttype(){
        return equipmenttype;
    }
    public void setEquipmenttype(String equipmenttype){
        this.equipmenttype = equipmenttype ;
    }
    public String getEquipmentdesc(){
        return equipmentdesc;
    }
    public void setEquipmentdesc(String equipmentdesc){
        this.equipmentdesc = equipmentdesc;
    }
    
}
