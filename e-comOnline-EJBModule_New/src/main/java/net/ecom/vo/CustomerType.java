/*
 * CustomerType.java
 *
 * Created on July 26, 2008, 1:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author lavanya
 */
public class CustomerType implements Serializable{
    
    /** Creates a new instance of CustomerType */
    public CustomerType() {
    }
    
    private String custtypecode;
    private String custtypename;

    public String getCusttypecode() {
        return custtypecode;
    }

    public void setCusttypecode(String custtypecode) {
        this.custtypecode = custtypecode;
    }

    public String getCusttypename() {
        return custtypename;
    }

    public void setCusttypename(String custtypename) {
        this.custtypename = custtypename;
    }
}
