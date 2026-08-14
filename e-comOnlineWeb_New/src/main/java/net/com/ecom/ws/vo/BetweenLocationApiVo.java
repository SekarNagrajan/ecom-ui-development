/*
 * VO class representing the API response for BetweenLocation
 */
package net.com.ecom.ws.vo;

import java.io.Serializable;
import java.util.List;

/**
 * VO class for BetweenLocation API response
 * Maps to the JSON response from Phase2/betweenlocation API
 * 
 * @author gobinath.t
 */
public class BetweenLocationApiVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<LegApiVo> portpair;
    private String origin;
    private String delivery;
    private String origin_name;
    private String delivery_name;
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
    private String leg_type;
    private String default_route;
    private int transit_time;
    private String load_callid;
    private String disch_callid;
    private int default_route_order;
    private String inland_flag;
    private int transshipment_count;
    private String sourcePortName;
    private String destinationPortName;
    private String sourceCountry;
    private String destinationCountry;
    private int noOfTransshipMent;
    private boolean feederInvolved;
    private String lct;
    private String routeid;
    private String secondary1;
    private String secondary2;
    private String advertisedRoute;
    private String first_service_type;
    private CutoffApiVo cutoff;

    public BetweenLocationApiVo() {
    }

    public List<LegApiVo> getPortpair() {
        return portpair;
    }

    public void setPortpair(List<LegApiVo> portpair) {
        this.portpair = portpair;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
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

    public String getLeg_type() {
        return leg_type;
    }

    public void setLeg_type(String leg_type) {
        this.leg_type = leg_type;
    }

    public String getDefault_route() {
        return default_route;
    }

    public void setDefault_route(String default_route) {
        this.default_route = default_route;
    }

    public int getTransit_time() {
        return transit_time;
    }

    public void setTransit_time(int transit_time) {
        this.transit_time = transit_time;
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

    public int getDefault_route_order() {
        return default_route_order;
    }

    public void setDefault_route_order(int default_route_order) {
        this.default_route_order = default_route_order;
    }

    public String getInland_flag() {
        return inland_flag;
    }

    public void setInland_flag(String inland_flag) {
        this.inland_flag = inland_flag;
    }

    public int getTransshipment_count() {
        return transshipment_count;
    }

    public void setTransshipment_count(int transshipment_count) {
        this.transshipment_count = transshipment_count;
    }

    public String getSourcePortName() {
        return sourcePortName;
    }

    public void setSourcePortName(String sourcePortName) {
        this.sourcePortName = sourcePortName;
    }

    public String getDestinationPortName() {
        return destinationPortName;
    }

    public void setDestinationPortName(String destinationPortName) {
        this.destinationPortName = destinationPortName;
    }

    public String getSourceCountry() {
        return sourceCountry;
    }

    public void setSourceCountry(String sourceCountry) {
        this.sourceCountry = sourceCountry;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public int getNoOfTransshipMent() {
        return noOfTransshipMent;
    }

    public void setNoOfTransshipMent(int noOfTransshipMent) {
        this.noOfTransshipMent = noOfTransshipMent;
    }

    public boolean isFeederInvolved() {
        return feederInvolved;
    }

    public void setFeederInvolved(boolean feederInvolved) {
        this.feederInvolved = feederInvolved;
    }

    public String getLct() {
        return lct;
    }

    public void setLct(String lct) {
        this.lct = lct;
    }

    public String getRouteid() {
        return routeid;
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }

    public String getSecondary1() {
        return secondary1;
    }

    public void setSecondary1(String secondary1) {
        this.secondary1 = secondary1;
    }

    public String getSecondary2() {
        return secondary2;
    }

    public void setSecondary2(String secondary2) {
        this.secondary2 = secondary2;
    }

    public String getAdvertisedRoute() {
        return advertisedRoute;
    }

    public void setAdvertisedRoute(String advertisedRoute) {
        this.advertisedRoute = advertisedRoute;
    }

    public String getFirst_service_type() {
        return first_service_type;
    }

    public void setFirst_service_type(String first_service_type) {
        this.first_service_type = first_service_type;
    }

    public CutoffApiVo getCutoff() {
        return cutoff;
    }

    public void setCutoff(CutoffApiVo cutoff) {
        this.cutoff = cutoff;
    }

    @Override
    public String toString() {
        return "BetweenLocationApiVo{" +
                "origin='" + origin + '\'' +
                ", delivery='" + delivery + '\'' +
                ", etd='" + etd + '\'' +
                ", eta='" + eta + '\'' +
                ", service_code='" + service_code + '\'' +
                ", vessel_code='" + vessel_code + '\'' +
                ", voyage_number='" + voyage_number + '\'' +
                ", routeid='" + routeid + '\'' +
                ", transshipment_count=" + transshipment_count +
                ", portpair=" + (portpair != null ? portpair.size() : 0) + " legs" +
                '}';
    }
}
