/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.lookup.EJBLookUp;

/**
 *
 * @author gobinath.t
 */
public class MaskPort {

    // static EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    public static boolean isMaskPorts(String ports, ServletContext context) {
        boolean res = false;
        try {
            // Check if mask ports should be shown
            System.out.println("[ECOM-WAR] MaskPort ports---> "+ports);
            if ("Y".equalsIgnoreCase((String) context.getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                List<String> maskportlist = (List<String>) context.getAttribute("Mask_Port_List");

                // Split ports into a list
                List<String> portlist = Arrays.asList(ports.split(","));

                // Check each port in portlist against maskportlist
                for (String port : portlist) {
                    if (maskportlist.contains(port.trim())) {
                        res = true;
                        break; // Exit loop early since we found a match
                    }
                }
            } else {
                res = true; // Show mask ports is not enabled, so return true
            }
            System.out.println("[ECOM-WAR] MaskPort isMaskPorts-> "+res);
        } catch (Exception e) {
            // Log the exception
            context.log("Exception in isMaskPorts: " + e.getMessage());
            // Return false or handle as appropriate based on requirements
        }
        return res;
    }

}
