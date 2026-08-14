/*
 * VO class representing leg information in API response
 */
package net.com.ecom.ws.vo;

import java.io.Serializable;

/**
 * VO class for Leg/Portpair information in BetweenLocation API response
 * 
 * @author gobinath.t
 */
public class LegApiVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pol;
    private String pod;
    private String pol_name;
    private String pod_name;
    private String etd;
    private String eta;
    private String service_code;
    private String service_name;
    private String vessel_code;
    private String vessel_name;
    private String vessel_imo;
    private String vessel_operator;
    private String vessel_operator_name;
    private String vessel_owner_type;
    private String voyage_number;
    private String bound;
    private String schedule_type;
    private String pol_terminal;
    private String pod_terminal;
    private String pol_terminal_name;
    private String pod_terminal_name;
    private String load_callid;
    private String disch_callid;
    private String loadtype;
    private String disctype;
    private String dbound;
    private String groupcallid;
    private String pol_arrdate;
    private String lct_hrs;

    public LegApiVo() {
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol_name() {
        return pol_name;
    }

    public void setPol_name(String pol_name) {
        this.pol_name = pol_name;
    }

    public String getPod_name() {
        return pod_name;
    }

    public void setPod_name(String pod_name) {
        this.pod_name = pod_name;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getVessel_code() {
        return vessel_code;
    }

    public void setVessel_code(String vessel_code) {
        this.vessel_code = vessel_code;
    }

    public String getVessel_name() {
        return vessel_name;
    }

    public void setVessel_name(String vessel_name) {
        this.vessel_name = vessel_name;
    }

    public String getVessel_imo() {
        return vessel_imo;
    }

    public void setVessel_imo(String vessel_imo) {
        this.vessel_imo = vessel_imo;
    }

    public String getVessel_operator() {
        return vessel_operator;
    }

    public void setVessel_operator(String vessel_operator) {
        this.vessel_operator = vessel_operator;
    }

    public String getVessel_operator_name() {
        return vessel_operator_name;
    }

    public void setVessel_operator_name(String vessel_operator_name) {
        this.vessel_operator_name = vessel_operator_name;
    }

    public String getVessel_owner_type() {
        return vessel_owner_type;
    }

    public void setVessel_owner_type(String vessel_owner_type) {
        this.vessel_owner_type = vessel_owner_type;
    }

    public String getVoyage_number() {
        return voyage_number;
    }

    public void setVoyage_number(String voyage_number) {
        this.voyage_number = voyage_number;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getSchedule_type() {
        return schedule_type;
    }

    public void setSchedule_type(String schedule_type) {
        this.schedule_type = schedule_type;
    }

    public String getPol_terminal() {
        return pol_terminal;
    }

    public void setPol_terminal(String pol_terminal) {
        this.pol_terminal = pol_terminal;
    }

    public String getPod_terminal() {
        return pod_terminal;
    }

    public void setPod_terminal(String pod_terminal) {
        this.pod_terminal = pod_terminal;
    }

    public String getPol_terminal_name() {
        return pol_terminal_name;
    }

    public void setPol_terminal_name(String pol_terminal_name) {
        this.pol_terminal_name = pol_terminal_name;
    }

    public String getPod_terminal_name() {
        return pod_terminal_name;
    }

    public void setPod_terminal_name(String pod_terminal_name) {
        this.pod_terminal_name = pod_terminal_name;
    }

    public String getLoad_callid() {
        return load_callid;
    }

    public void setLoad_callid(String load_callid) {
        this.load_callid = load_callid;
    }

    public String getDisch_callid() {
        return disch_callid;
    }

    public void setDisch_callid(String disch_callid) {
        this.disch_callid = disch_callid;
    }

    public String getLoadtype() {
        return loadtype;
    }

    public void setLoadtype(String loadtype) {
        this.loadtype = loadtype;
    }

    public String getDisctype() {
        return disctype;
    }

    public void setDisctype(String disctype) {
        this.disctype = disctype;
    }

    public String getDbound() {
        return dbound;
    }

    public void setDbound(String dbound) {
        this.dbound = dbound;
    }

    public String getGroupcallid() {
        return groupcallid;
    }

    public void setGroupcallid(String groupcallid) {
        this.groupcallid = groupcallid;
    }

    public String getPol_arrdate() {
        return pol_arrdate;
    }

    public void setPol_arrdate(String pol_arrdate) {
        this.pol_arrdate = pol_arrdate;
    }

    public String getLct_hrs() {
        return lct_hrs;
    }

    public void setLct_hrs(String lct_hrs) {
        this.lct_hrs = lct_hrs;
    }

    @Override
    public String toString() {
        return "LegApiVo{" +
                "pol='" + pol + '\'' +
                ", pod='" + pod + '\'' +
                ", etd='" + etd + '\'' +
                ", eta='" + eta + '\'' +
                ", service_code='" + service_code + '\'' +
                ", vessel_code='" + vessel_code + '\'' +
                ", voyage_number='" + voyage_number + '\'' +
                '}';
    }
}
