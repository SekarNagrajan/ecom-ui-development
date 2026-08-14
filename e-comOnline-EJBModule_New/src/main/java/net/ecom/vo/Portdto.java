/*
 * Portdto.java
 *
 * Created on October 24, 2007, 12:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author udhaya
 */
public class Portdto implements Serializable {
    
    /** Creates a new instance of Portdto */
    public Portdto() {
    }
    private ArrayList pcode=null;
    private ArrayList pname=null;

    public ArrayList getPcode() {
        return pcode;
    }

    public void setPcode(ArrayList pcode) {
        this.pcode = pcode;
    }

    public ArrayList getPname() {
        return pname;
    }

    public void setPname(ArrayList pname) {
        this.pname = pname;
    }
    
}
