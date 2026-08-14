/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

/**
 *
 * @author deepa.p
 */
public class Validation1 {

    public static String checkNullfloat(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str)) && !"null".equalsIgnoreCase(str)) ? str.trim() : "0";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertEmptytoNull(String str) {
        try {
          //  System.out.println("[ECOM] Validation1 convertEmptytoNull===>" + str + ">>");
            str = ((str != null && ("".equalsIgnoreCase(str.trim()) || str.trim().length() == 0 || "null".equalsIgnoreCase(str))) ? null : str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
