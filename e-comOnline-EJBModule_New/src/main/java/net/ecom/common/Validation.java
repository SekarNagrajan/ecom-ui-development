/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.io.Serializable;

/**
 *
 * @author udhaya
 */
public class Validation implements Serializable {

    public static String checkNull(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null")  && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

   
}
