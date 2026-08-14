/*
 * PortsBean.java
 *
 * Created on May 2, 2008, 10:09 AM
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
public class PortsBean implements Serializable  {
    
    /** Creates a new instance of PortsBean */
    private String portcode;
    private String portname;
    private int ID;
    public PortsBean()
    {
        
    }
    public PortsBean(int s,int d)
    {
        this.ID=s;
        this.ID=d;
    }
    public PortsBean(int ID,String portcode)
    {
        this.ID=ID;
        this.portcode=portcode;
    }
     public PortsBean(int ID,String portcode,String s)
    {
        this.ID=ID;
        this.portcode=portcode;
        this.portname=s;
    }
     /*
    public  PortsBean(int ID,String portcode,String portname) {
        this.ID=ID;
        this.portcode=portcode;
        this.portname=portname;
    }
      **/

    public String getPortcode() {
        return portcode;
    }

    public void setPortcode(String portcode) {
        this.portcode = portcode;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    
}
