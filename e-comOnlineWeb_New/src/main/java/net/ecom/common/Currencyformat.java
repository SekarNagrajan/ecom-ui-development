/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *
 * @author vasanthakumar.r
 */
public class Currencyformat implements Serializable {

    public static String rupeeFormat(String value) {
        String output = "";
        try {

            if (value != null && value.trim().length() > 0) {
                double val = Double.parseDouble(value);
                DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
                output = formatter.format(val);
            } else {
                output = "0.00";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String USDFormat(String value) {
        String output = "";
        try {

            if (value != null && value.trim().length() > 0) {
                double val = Double.parseDouble(value);
                DecimalFormat formatter = new DecimalFormat("#,##,##,##0.00");
                output = formatter.format(val);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String removeZero(String value) {
        String output = "";
        try {
            value = Validation.checkNull(value);
            if (value != null && value.trim().length() > 0) {
                double val = Double.parseDouble(value.replace(",", ""));
                if (val > 0) {
                    output = value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
