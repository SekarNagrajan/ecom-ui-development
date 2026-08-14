/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.text.DecimalFormat;

/**
 *
 * @author gobinath.t
 */
public class WeightConversion {

    public static String weightConversion(String fromunit, String value, String tounit) {
        // System.out.println("[ECOM] WeightConversion   ****weightConversion******");
        DecimalFormat df1 = new DecimalFormat("##0.000");
        String grossWeight = "";

        if (tounit == null || tounit.toString().trim().length() == 0) {
            grossWeight = "0.00";
            return grossWeight;
        }

        if (fromunit.equalsIgnoreCase(tounit.trim())) {
            grossWeight = df1.format(Double.parseDouble(value.toString().trim()));
        } else if ("P".equalsIgnoreCase(fromunit.trim())) {
            // System.out.println("[ECOM] WeightConversion   88888");
            if ("K".equalsIgnoreCase(tounit.trim())) {//p
                grossWeight = df1.format(Double.parseDouble(value.toString().trim()) * 2.2046);
            } else if ("T".equalsIgnoreCase(tounit.trim())) {
                grossWeight = df1.format(Double.parseDouble(value.toString().trim()) * 2204);
            }
        } else if ("K".equalsIgnoreCase(fromunit.trim())) {//p

            if ("P".equalsIgnoreCase(tounit.trim())) {
                grossWeight = df1.format(Double.parseDouble(value.toString().trim()) / 2.2046);
                // System.out.println("[ECOM] WeightConversion   grossWeight if is " + grossWeight);
            } else if ("T".equalsIgnoreCase(tounit.trim())) {
                grossWeight = df1.format(Double.parseDouble(value.toString().trim()) * 1000);
                // System.out.println("[ECOM] WeightConversion   grossWeight else is " + grossWeight);
            }
        } else if ("T".equalsIgnoreCase(fromunit.trim())) {
            // System.out.println("[ECOM] WeightConversion   1000");
            if ("K".equalsIgnoreCase(tounit.trim())) {//p
                grossWeight = df1.format(Double.parseDouble(value.toString().trim()) / 1000);
            } else if ("P".equalsIgnoreCase(tounit.trim())) {
                grossWeight = df1.format(Double.parseDouble(value.toString().trim()) / 2204);
            }

        }
        return grossWeight;
    }
}
