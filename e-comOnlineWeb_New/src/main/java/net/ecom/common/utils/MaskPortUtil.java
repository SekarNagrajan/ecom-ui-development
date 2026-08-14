package net.ecom.common.utils;

import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletContext;
import net.ecom.vo.MaskPortVO;
import net.ecom.vds.serviceVo.ScheduleCommonVO;
import net.ecom.phase2.service.vo.TariffSummaryVO;

/**
 * Utility class for MaskPort operations.
 * Initialize this from ApplicaitionInit.contextInitialized()
 * 
 * @author preethi.m
 */
public class MaskPortUtil {

    // Static holder for ServletContext - initialized once during app startup
    private static ServletContext servletContext;

    /**
     * Initialize the utility with ServletContext.
     * Call this from ApplicaitionInit.contextInitialized()
     * @param context The ServletContext to store
     */
    public static void init(ServletContext context) {
        servletContext = context;
        System.out.println("[ECOM-WAR] MaskPortUtil initialized with ServletContext");
    }

    /**
     * Get the stored ServletContext.
     * @return The ServletContext, or null if not initialized
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Check if the utility has been initialized.
     * @return true if ServletContext is available, false otherwise
     */
    public static boolean isInitialized() {
        return servletContext != null;
    }

 
    public static boolean isMaskPorts(String ports) {
        try {
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if("Y".equalsIgnoreCase(hideMaskPortRecords)){
            System.out.println("[ECOM-WAR] MaskPortUtil ports---> " + ports);
            
           if(ports != null && ports.contains("-")){
            ports = ports.trim().split("-")[0];
           }
            
            List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
            if (maskPortList != null) {
                for (MaskPortVO maskPortVO : maskPortList) {
                    if (maskPortVO != null && maskPortVO.getMport() != null 
                            && maskPortVO.getMport().trim().equalsIgnoreCase(ports.trim())) {
                        return true;
                    }
                }
            }
            return false;
            }
    
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil isMaskPorts Exception: " + e);
            e.printStackTrace();
        }
        return false;
    }

 
    public static String swapmaskporttoactual(String maskPort) {
        try {
            
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if("Y".equalsIgnoreCase(hideMaskPortRecords)){
            String trimmedMaskPort="";
            List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
            // System.out.println("[ECOM-WAR] MaskPortUtil swapmaskporttoactual maskPortList---> " + maskPortList);
            if (maskPortList != null && !maskPortList.isEmpty()) {
                if(maskPort.contains("-")){
                    trimmedMaskPort = maskPort.trim().split("-")[0];
                }else{
                    trimmedMaskPort = maskPort.trim();
                }
               
                for (MaskPortVO maskPortVO : maskPortList) {
                    if (maskPortVO != null && maskPortVO.getMport() != null 
                            && maskPortVO.getMport().trim().equalsIgnoreCase(trimmedMaskPort)) {
                        // Found matching mask port, return the actual port
                        String actualPort = maskPortVO.getPort();
                        return (actualPort != null && !actualPort.trim().isEmpty()) ? actualPort.trim() : maskPort;
                    }
                }
            }
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil swapmaskporttoactual Exception: " + e);
            e.printStackTrace();
        }
        
        // If not found in list or any error, return the input mask port
        return maskPort;
    }

   
    public static String swapmaskporttoactualportwithname(String maskPortWithName) {
        try {
            if (maskPortWithName != null && !maskPortWithName.trim().isEmpty()) {
            Hashtable portlist = (Hashtable) servletContext.getAttribute("SESSION_PORTLIST");
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if ("Y".equalsIgnoreCase(hideMaskPortRecords)) {
                List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
                
                if (maskPortList != null && !maskPortList.isEmpty()) {
                    String trimmedMaskPort = "";
                    // Extract the port code from the format "PORTCD - Port Name"
                    if (maskPortWithName.contains("-")) {
                        trimmedMaskPort = maskPortWithName.trim().split("-")[0].trim();
                    } else {
                        trimmedMaskPort = maskPortWithName.trim();
                    }
                    
                    for (MaskPortVO maskPortVO : maskPortList) {
                        if (maskPortVO != null && maskPortVO.getMport() != null 
                                && maskPortVO.getMport().trim().equalsIgnoreCase(trimmedMaskPort)) {
                            // Found matching mask port, get the actual port
                            String actualPort = maskPortVO.getPort();
                            if (actualPort != null && !actualPort.trim().isEmpty()) {
                                if (portlist != null) {
                                    String actualPortName = (String) portlist.get(actualPort.trim().toUpperCase());
                                    if (actualPortName != null && !actualPortName.trim().isEmpty()) {
                                        return actualPort.trim() + " - " + actualPortName.trim();
                                    }
                                }
                                return actualPort.trim();
                            }
                            return maskPortWithName;
                        }
                    }
                }
            }
        }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil swapmaskporttoactualportwithname Exception: " + e);
            e.printStackTrace();
        }
        
        // If not found in list or any error, return the input mask port with name
        return maskPortWithName;
    }

  
    public static String swapactualporttomaskport(String actualPort) {
        try {
            System.out.println("[ECOM-WAR] MaskPortUtil swapactualporttomaskport actualPort---> " + actualPort);
            
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if("Y".equalsIgnoreCase(hideMaskPortRecords)){
            List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
            // System.out.println("[ECOM-WAR] MaskPortUtil swapactualporttomaskport maskPortList---> " + maskPortList);
            if (maskPortList != null && !maskPortList.isEmpty()) {
                String trimmedActualPort="";
                if(actualPort.contains("-")){
                    trimmedActualPort = actualPort.trim().split("-")[0];
                }else{
                    trimmedActualPort = actualPort.trim();
                }
                for (MaskPortVO maskPortVO : maskPortList) {
                    if (maskPortVO != null && maskPortVO.getPort() != null 
                            && maskPortVO.getPort().trim().equalsIgnoreCase(trimmedActualPort)) {
                        // Found matching actual port, return the mask port
                        String maskPort = maskPortVO.getMport();
                        return (maskPort != null && !maskPort.trim().isEmpty()) ? maskPort.trim() : actualPort;
                    }
                }
            }
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil swapactualporttomaskport Exception: " + e);
            e.printStackTrace();
        }
        
        // If not found in list or any error, return the input actual port
        return actualPort;
    }

  
    public static List<ScheduleCommonVO> maskScheduleDataIfMaskPort(String sname, List<ScheduleCommonVO> scheduleList) {
        try {
            // System.out.println("[ECOM-WAR] MaskPortUtil maskScheduleDataIfMaskPort sname---> " + sname);
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if("Y".equalsIgnoreCase(hideMaskPortRecords)){
            if (isMaskPort(swapactualporttomaskport(sname))) {
                List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
                String portname = "";
                if (maskPortList != null) {
                    for (MaskPortVO maskPortVO : maskPortList) {
                        if (maskPortVO != null && maskPortVO.getPort() != null
                                && maskPortVO.getPort().trim().equalsIgnoreCase(sname.trim())) {
                            portname = maskPortVO.getMportname();
                            break;
                        }
                    }
                }
                // System.out.println("[ECOM-WAR] MaskPortUtil maskScheduleDataIfMaskPort portname---> " + portname);
                return maskScheduleData(scheduleList, portname);
            }
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil maskScheduleDataIfMaskPort Exception: " + e);
            e.printStackTrace();
        }
        
        return scheduleList;
    }

  
    private static boolean isMaskPort(String port) {
        try {
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if("Y".equalsIgnoreCase(hideMaskPortRecords)){
            List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
            
            if (maskPortList != null && !maskPortList.isEmpty()) {
                String trimmedPort = port.trim();
                for (MaskPortVO maskPortVO : maskPortList) {
                    if (maskPortVO != null && maskPortVO.getMport() != null 
                            && maskPortVO.getMport().trim().equalsIgnoreCase(trimmedPort)) {
                        return true;
                    }
                }
            }
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil isMaskPort Exception: " + e);
            e.printStackTrace();
        }
        
        return false;
    }
    public static boolean isActualPort(String port) {
        try {
        // System.out.println("[ECOM-WAR] MaskPortUtil isActualPort port---> " + port);
        String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
        // System.out.println("[ECOM-WAR] MaskPortUtil isActualPort HIDE_MASKPORT_RECORDS---> " + hideMaskPortRecords);
        if("Y".equalsIgnoreCase(hideMaskPortRecords)){
            List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
            // System.out.println("[ECOM-WAR] MaskPortUtil isActualPort maskPortList size---> " + (maskPortList != null ? maskPortList.size() : "null"));
            
            if (maskPortList != null && !maskPortList.isEmpty()) {
                String trimmedPort = port.trim();
                if(trimmedPort.contains("-")){
                    trimmedPort = trimmedPort.trim().split("-")[0].trim();
                }
                // System.out.println("[ECOM-WAR] MaskPortUtil isActualPort trimmedPort---> " + trimmedPort);
                for (MaskPortVO maskPortVO : maskPortList) {
                    // System.out.println("[ECOM-WAR] MaskPortUtil isActualPort checking against port---> " + maskPortVO.getPort());
                    if (maskPortVO != null && maskPortVO.getPort() != null 
                            && maskPortVO.getPort().trim().equalsIgnoreCase(trimmedPort)) {
                        // System.out.println("[ECOM-WAR] MaskPortUtil isActualPort MATCH FOUND returning true---> " + maskPortVO.getPort());
                        return true;
                    }
                }
            }
            } else {
                System.out.println("[ECOM-WAR] MaskPortUtil isActualPort HIDE_MASKPORT_RECORDS is NOT Y, returning false");
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil isActualPort Exception: " + e);
            e.printStackTrace();
        }
        
        return false;
    }

    public static String getDisplayPortName(String portCode, String originalPortName) {
        
        try {
        
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
            if ("Y".equalsIgnoreCase(hideMaskPortRecords)) {
           
            List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
            if (maskPortList != null && !maskPortList.isEmpty() && portCode != null) {
                String trimmedPortCode = portCode.trim();
                for (MaskPortVO maskPortVO : maskPortList) {
                    if (maskPortVO != null && maskPortVO.getMport() != null 
                            && maskPortVO.getPort().trim().equalsIgnoreCase(trimmedPortCode)) {
                        // Found matching mask port, return mportname
                        String mportname = maskPortVO.getMportname();
                        // System.out.println("[ECOM-WAR] MaskPortUtil getDisplayPortName found mportname---> " + mportname);
                        return (mportname != null && !mportname.trim().isEmpty()) ? mportname.trim() : originalPortName;
                    }
                }
            }
        }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil getDisplayPortName Exception: " + e);
            e.printStackTrace();
        }
        
 
        return originalPortName;
    }
    private static List<ScheduleCommonVO> maskScheduleData(List<ScheduleCommonVO> scheduleList, String portname) {
        // System.out.println("[ECOM-WAR] MaskPortUtil maskScheduleData portname---> " + portname);
        String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
        if("Y".equalsIgnoreCase(hideMaskPortRecords)){
        for (ScheduleCommonVO schedule : scheduleList) {
            // Set Terminalname, Bound, Voyage, Vesselname to "-"
            schedule.setTerminalname("-");
            schedule.setBound("-");
            schedule.setVoyage("-");
            schedule.setVesselname("-");
            schedule.setService("-");
            // Replace Portname with the provided portname
            schedule.setPortname(portname != null ? portname : "-");
        }
    }

        return scheduleList;
    }

    
    public static List<TariffSummaryVO> updateTariffSummaryIsMaskPort(List<TariffSummaryVO> tariffList, String pol, String pod) {
        try {
            // System.out.println("[ECOM-WAR] MaskPortUtil updateTariffSummaryIsMaskPort called");
            String hideMaskPortName = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if("Y".equalsIgnoreCase(hideMaskPortName)){
            // Check if POL/POD need masking

            boolean isMaskPol = isMaskPorts(pol);
            boolean isMaskPod = isMaskPorts(pod);
            // Early exit if neither port needs masking
            if (isMaskPol && isMaskPod) {
            List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
            // Find mask port names for POL and POD
            String maskPortNamePol = null;
            String maskPortNamePod = null;
            String polTrimmed = pol != null ? pol.trim() : "";
            String podTrimmed = pod != null ? pod.trim() : "";
            
            for (MaskPortVO maskPortVO : maskPortList) {
                String port = maskPortVO.getPort().trim();
                if (isMaskPol && maskPortNamePol == null && port.equalsIgnoreCase(polTrimmed)) {
                    maskPortNamePol = maskPortVO.getMportname();
                }
                if (isMaskPod && maskPortNamePod == null && port.equalsIgnoreCase(podTrimmed)) {
                    maskPortNamePod = maskPortVO.getMportname();
                }
                // Early break if both are found
                if ((!isMaskPol || maskPortNamePol != null) && (!isMaskPod || maskPortNamePod != null)) {
                    break;
                }
            }
            
            boolean hasMaskPol = maskPortNamePol != null && !maskPortNamePol.trim().isEmpty();
            boolean hasMaskPod = maskPortNamePod != null && !maskPortNamePod.trim().isEmpty();
            
            if (hasMaskPol || hasMaskPod) {
                for (TariffSummaryVO tariff : tariffList) {
                    if (tariff == null) {
                        continue;
                    }
                    // Replace loadport (POL) if mask port name exists
                    if (hasMaskPol) {
                        tariff.setLoadport(maskPortNamePol);
                    }
                    // Replace dischport (POD) if mask port name exists
                    if (hasMaskPod) {
                        tariff.setDischport(maskPortNamePod);
                    }
                }
            }
        }
       }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil updateTariffSummaryIsMaskPort Exception: " + e);
            e.printStackTrace();
        }
        return tariffList;
    }

  
    public static boolean checkIsMaskPort(String pol, String pod) {
        try {
            // System.out.println("[ECOM-WAR] MaskPortUtil checkIsMaskPort pol---> " + pol + ", pod---> " + pod);
            
          
            
            // Check if HIDE_MASKPORT_RECORDS plugin is 'Y'
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if ("Y".equalsIgnoreCase(hideMaskPortRecords)) {
            
            // Check if either POL or POD is a mask port
            boolean isPolMaskPort = (pol != null && !pol.trim().isEmpty()) && isActualPort(pol);
            boolean isPodMaskPort = (pod != null && !pod.trim().isEmpty()) && isActualPort(pod);
            
            // System.out.println("[ECOM-WAR] MaskPortUtil checkIsMaskPort isPolMaskPort---> " + isPolMaskPort + ", isPodMaskPort---> " + isPodMaskPort);
            
            return isPolMaskPort || isPodMaskPort;
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil checkIsMaskPort Exception: " + e);
            e.printStackTrace();
        }
        return false;
    }

    public static String swapActualPortToMaskPortWithName(String actualPortWithName) {
        try {
            // System.out.println("[ECOM-WAR] MaskPortUtil swapActualPortToMaskPortWithName input---> " + actualPortWithName);
            
            
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if ("Y".equalsIgnoreCase(hideMaskPortRecords)) {
                List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
                
                if (maskPortList != null && !maskPortList.isEmpty()) {
                    // Extract port code from "CODE - NAME" format
                    String portCode = actualPortWithName.trim();
                    if (portCode.contains("-")) {
                        portCode = portCode.split("-")[0].trim();
                    }
                    
                    // System.out.println("[ECOM-WAR] MaskPortUtil swapActualPortToMaskPortWithName portCode---> " + portCode);
                    
                    for (MaskPortVO maskPortVO : maskPortList) {
                        if (maskPortVO != null && maskPortVO.getPort() != null 
                                && maskPortVO.getPort().trim().equalsIgnoreCase(portCode)) {
                            // Found matching actual port, return mask port with name
                            String mport = maskPortVO.getMport();
                            String mportname = maskPortVO.getMportname();
                            
                            if (mport != null && !mport.trim().isEmpty()) {
                                String result = mport.trim();
                                if (mportname != null && !mportname.trim().isEmpty()) {
                                    result = result + " - " + mportname.trim();
                                }
                                // System.out.println("[ECOM-WAR] MaskPortUtil swapActualPortToMaskPortWithName result---> " + result);
                                return result;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil swapActualPortToMaskPortWithName Exception: " + e);
            e.printStackTrace();
        }
        
        // If not found in list or any error, return the input
        return actualPortWithName;
    }

    public static String getPortNameByCode(String portCode, String portName) {
        String result = checkNull(portName);
        try {
            if (checkNull(portCode).length()>0) {
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            Hashtable portlist = (Hashtable) servletContext.getAttribute("SESSION_PORTLIST");
            String trimmedPortCode = portCode.trim();
            // If input contains " - " (format: "CODE - Name"), extract just the code
            if (trimmedPortCode.contains("-")) {
                trimmedPortCode = trimmedPortCode.split("-")[0].trim();
            }
            
           
            if ("Y".equalsIgnoreCase(hideMaskPortRecords)) {
                List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
                
                if (maskPortList != null && !maskPortList.isEmpty()) {
                  
                    for (MaskPortVO maskPortVO : maskPortList) {
                        if (maskPortVO != null && maskPortVO.getMport() != null 
                                && maskPortVO.getMport().trim().equalsIgnoreCase(trimmedPortCode)) {
                            // It's a mask port, get the actual port and its name from SESSION_PORTLIST
                            String actualPort = maskPortVO.getPort();
                            if (actualPort != null && !actualPort.trim().isEmpty()) {
                                String actualPortName = null;
                                if (portlist != null) {
                                    actualPortName = (String) portlist.get(actualPort.trim().toUpperCase());
                                }
                              
                                if (actualPortName != null && !actualPortName.trim().isEmpty()) {
                                    result = actualPortName.trim();
                                }
                                System.out.println("[ECOM-WAR] MaskPortUtil getPortNameByCode - Mask port found: " + trimmedPortCode + ", swapped to actual port: " + result);
                                return result;
                            }
                            return result;
                        }
                    }
                }
            }
            }
            
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil getPortNameByCode Exception: " + e);
            e.printStackTrace();
        }
        
        // If not found anywhere, return the original input
        return result;
    }
    
    private static String checkNull(String value) {
        return (value != null && (!value.equalsIgnoreCase("")) && (!value.equalsIgnoreCase("null"))) ? value.trim() : "";
    }
   
    public static String getMportNameByActualPort(String actualPort) {
        try {
           System.out.println("[ECOM-WAR] MaskPortUtil getMportNameByActualPort actualPort---> " + actualPort);
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            if ("Y".equalsIgnoreCase(hideMaskPortRecords)) {
                List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
                
                if (maskPortList != null && !maskPortList.isEmpty()) {
                    String trimmedPort = actualPort.trim();
                    if (trimmedPort.contains("-")) {
                        trimmedPort = trimmedPort.split("-")[0].trim();
                    }
                    
                    for (MaskPortVO maskPortVO : maskPortList) {
                        if (maskPortVO != null && maskPortVO.getPort() != null 
                                && maskPortVO.getPort().trim().equalsIgnoreCase(trimmedPort)) {
                            // Found matching actual port, return the mportname
                            String mportname = maskPortVO.getMportname();
                            if (mportname != null && !mportname.trim().isEmpty()) {
                                return mportname.trim();
                            }
                            return null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil getMportNameByActualPort Exception: " + e);
            e.printStackTrace();
        }
        return null;
    }

    public static String getMaskPortNameByActualCode(String actualPortCode) {
        try {
            
            
            String hideMaskPortRecords = (String) servletContext.getAttribute("HIDE_MASKPORT_RECORDS");
            String trimmedPortCode = actualPortCode.trim();
            // If input contains " - " (format: "CODE - Name"), extract just the code
            if (trimmedPortCode.contains("-")) {
                trimmedPortCode = trimmedPortCode.split("-")[0].trim();
            }
            
            // System.out.println("[ECOM-WAR] MaskPortUtil getMaskPortNameByActualCode - Input actualPortCode: " + actualPortCode + ", trimmedPortCode: " + trimmedPortCode);
            
            if ("Y".equalsIgnoreCase(hideMaskPortRecords)) {
                List<MaskPortVO> maskPortList = (List<MaskPortVO>) servletContext.getAttribute("MaskPortListObj");
                
                if (maskPortList != null && !maskPortList.isEmpty()) {
                    // Check if actual port code has a mask port mapping (check against port field)
                    for (MaskPortVO maskPortVO : maskPortList) {
                        if (maskPortVO != null && maskPortVO.getPort() != null 
                                && maskPortVO.getPort().trim().equalsIgnoreCase(trimmedPortCode)) {
                            String maskPort = maskPortVO.getMport();
                            if (maskPort != null && !maskPort.trim().isEmpty()) {
                                return maskPort.trim();
                            }
                            }
                            return actualPortCode;
                        }
                    }
                }
           
            
         
            
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] MaskPortUtil getMaskPortNameByActualCode Exception: " + e);
            e.printStackTrace();
        }
        
        // If not found anywhere, return the original input
        return actualPortCode;
    }
}

