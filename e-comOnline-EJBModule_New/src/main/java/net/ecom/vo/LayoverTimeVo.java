package net.ecom.vo;

import java.io.Serializable;

/**
 * Value Object for layover_time configuration table.
 * Used for port-based transhipment layover time lookup.
 */
public class LayoverTimeVo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String terminal;        // Previous leg discharge terminal (optional)
    private String serviceCode;     // Previous leg service
    private String portcode;        // Previous leg discharge port
    private int sameTerminalHrs;    // Layover hours when terminals are same
    private int diffTerminalHrs;    // Layover hours when terminals differ
    
    public LayoverTimeVo() {
    }
    
    public LayoverTimeVo(String portcode, String serviceCode, String terminal, 
                         int sameTerminalHrs, int diffTerminalHrs) {
        this.portcode = portcode;
        this.serviceCode = serviceCode;
        this.terminal = terminal;
        this.sameTerminalHrs = sameTerminalHrs;
        this.diffTerminalHrs = diffTerminalHrs;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getPortcode() {
        return portcode;
    }

    public void setPortcode(String portcode) {
        this.portcode = portcode;
    }

    public int getSameTerminalHrs() {
        return sameTerminalHrs;
    }

    public void setSameTerminalHrs(int sameTerminalHrs) {
        this.sameTerminalHrs = sameTerminalHrs;
    }

    public int getDiffTerminalHrs() {
        return diffTerminalHrs;
    }

    public void setDiffTerminalHrs(int diffTerminalHrs) {
        this.diffTerminalHrs = diffTerminalHrs;
    }
    
    /**
     * Check if the layover hours values are valid (non-null and non-negative)
     */
    public boolean isValid() {
        return sameTerminalHrs >= 0 && diffTerminalHrs >= 0;
    }
    
    @Override
    public String toString() {
        return "LayoverTimeVo{" +
                "portcode='" + portcode + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", terminal='" + terminal + '\'' +
                ", sameTerminalHrs=" + sameTerminalHrs +
                ", diffTerminalHrs=" + diffTerminalHrs +
                '}';
    }
}
