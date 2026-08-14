/*
 * EquipmentBean.java
 *
 * Created on June 2, 2008, 1:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author senthilkumar
 */
public class EquipmentBean implements Serializable {
    private int ID;
    private String equipmentcode;
    private String equipmentdesc;
    private int tareweight;
    
    /** Creates a new instance of EquipmentBean */
    public EquipmentBean(int ID,String equipmentcode,String equipmentdesc,int  tareweight) {
        this.ID=ID;
        this.equipmentcode=equipmentcode;
        this.equipmentdesc=equipmentdesc;
        this.tareweight=tareweight;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEquipmentcode() {
        return equipmentcode;
    }

    public void setEquipmentcode(String equipmentcode) {
        this.equipmentcode = equipmentcode;
    }

    public String getEquipmentdesc() {
        return equipmentdesc;
    }

    public void setEquipmentdesc(String equipmentdesc) {
        this.equipmentdesc = equipmentdesc;
    }

    public int getTareweight() {
        return tareweight;
    }

    public void setTareweight(int tareweight) {
        this.tareweight = tareweight;
    }
    
}
