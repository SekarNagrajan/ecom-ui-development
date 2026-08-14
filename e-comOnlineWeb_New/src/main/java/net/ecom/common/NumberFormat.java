/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *
 * @author gobinath.t
 */
public class NumberFormat implements Serializable {

    public static String numberFormat(String value) {
        String output = "";
        try {
            if (value != null && value.trim().length() > 0) {
                double val = Double.parseDouble(value);
                DecimalFormat formatter = new DecimalFormat("###,###,##0.0");
                output = formatter.format(val);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String decimalFormat(String value) {
        String output = "";
        try {
            if (value != null && value.trim().length() > 0) {
                double val = Double.parseDouble(value);
                DecimalFormat formatter = new DecimalFormat("#0.00");
                output = formatter.format(val);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
