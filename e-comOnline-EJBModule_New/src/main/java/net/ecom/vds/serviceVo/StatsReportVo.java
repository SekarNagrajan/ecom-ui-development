/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class StatsReportVo implements Serializable {

    @SerializedName("booking party")
    @Expose
    private String booking_party;

    @SerializedName("agreement party")
    @Expose
    private String agreement_party;

    @SerializedName("shipper")
    @Expose
    private String shipper;

    @SerializedName("consignee")
    @Expose
    private String consignee;

    @SerializedName("freight forwarder")
    @Expose
    private String freight_forwarder;

    @SerializedName("pick up location")
    @Expose
    private String pick_up_location;

    @SerializedName("bl number")
    @Expose
    private String bl_number;

    @SerializedName("mcn no")
    @Expose
    private String mcn_no;

    @SerializedName("number of stops")
    @Expose
    private String number_of_stops;

    @SerializedName("contract commodity")
    @Expose
    private String contract_commodity;

    @SerializedName("bl commodity")
    @Expose
    private String bl_commodity;

    @SerializedName("contract number")
    @Expose
    private String contract_number;

    @SerializedName("amendment number")
    @Expose
    private String amendment_number;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("pol")
    @Expose
    private String pol;

    @SerializedName("origin country")
    @Expose
    private String origin_country;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("destination country")
    @Expose
    private String destination_country;

    @SerializedName("pod")
    @Expose
    private String pod;

    @SerializedName("vessel")
    @Expose
    private String vessel;

    @SerializedName("voyage")
    @Expose
    private String voyage;

    @SerializedName("week no")
    @Expose
    private String week_no;

    @SerializedName("sailing date")
    @Expose
    private String sailing_date;

    @SerializedName("booking received date")
    @Expose
    private String booking_received_date;

    @SerializedName("booking accepted date")
    @Expose
    private String booking_accepted_date;

    @SerializedName("pick up date")
    @Expose
    private String pick_up_date;

    @SerializedName("in gate date")
    @Expose
    private String in_gate_date;

    @SerializedName("destination vessel arrival")
    @Expose
    private String destination_vessel_arrival;

    @SerializedName("arrival notice print date")
    @Expose
    private String arrival_notice_print_date;

    @SerializedName("dispatch full")
    @Expose
    private String dispatch_full;

    @SerializedName("received empty")
    @Expose
    private String received_empty;

    @SerializedName("booking_source")
    @Expose
    private String booking_source;

    @SerializedName("si source")
    @Expose
    private String si_source;

    @SerializedName("sales rep")
    @Expose
    private String sales_rep;

    @SerializedName("container no")
    @Expose
    private String container_no;

    @SerializedName("total quantity")
    @Expose
    private String total_quantity;

    @SerializedName("soc")
    @Expose
    private String soc;

    @SerializedName("size type")
    @Expose
    private String size_type;

    @SerializedName("nor")
    @Expose
    private String nor;

    @SerializedName("temp (c)")
    @Expose
    private String temp;

    @SerializedName("carriage contract")
    @Expose
    private String carriage_contract;

    @SerializedName("cargo weight (lbs)")
    @Expose
    private String cargo_weight_lbs;

    @SerializedName("cargo weight (kgs)")
    @Expose
    private String cargo_weight_kgs;

    @SerializedName("ft20ofr")
    @Expose
    private String ft20ofr;

    @SerializedName("ft20trev")
    @Expose
    private String ft20trev;

    @SerializedName("ft40ofr")
    @Expose
    private String ft40ofr;

    @SerializedName("ft40trev")
    @Expose
    private String ft40trev;

    @SerializedName("20 ft")
    @Expose
    private String _20ft;

    @SerializedName("40 ft")
    @Expose
    private String _40ft;

    @SerializedName("shipment type")
    @Expose
    private String shipment_type;

    @SerializedName("all in amount")
    @Expose
    private String all_in_amount;

    @SerializedName("total revenue")
    @Expose
    private String total_revenue;

    @SerializedName("totalrevenuec")
    @Expose
    private String totalrevenuec;

    @SerializedName("ofrc")
    @Expose
    private String ofrc;

    @SerializedName("ofr")
    @Expose
    private String ofr;

    @SerializedName("pre")
    @Expose
    private String pre;

    @SerializedName("onc")
    @Expose
    private String onc;

    @SerializedName("baf")
    @Expose
    private String baf;

    @SerializedName("uss")
    @Expose
    private String uss;

    @SerializedName("ifr")
    @Expose
    private String ifr;

    @SerializedName("sec")
    @Expose
    private String sec;

    @SerializedName("whf")
    @Expose
    private String whf;

    @SerializedName("lss")
    @Expose
    private String lss;

    @SerializedName("fif")
    @Expose
    private String fif;

    @SerializedName("hgt")
    @Expose
    private String hgt;

    @SerializedName("thl")
    @Expose
    private String thl;

    @SerializedName("thd")
    @Expose
    private String thd;

    @SerializedName("vhs")
    @Expose
    private String vhs;

    @SerializedName("cra")
    @Expose
    private String cra;

    @SerializedName("cas")
    @Expose
    private String cas;

    @SerializedName("irs")
    @Expose
    private String irs;

    @SerializedName("gri")
    @Expose
    private String gri;

    @SerializedName("others")
    @Expose
    private String others;

    public String getBooking_party() {
        return booking_party;
    }

    public void setBooking_party(String booking_party) {
        this.booking_party = booking_party;
    }

    public String getAgreement_party() {
        return agreement_party;
    }

    public void setAgreement_party(String agreement_party) {
        this.agreement_party = agreement_party;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getFreight_forwarder() {
        return freight_forwarder;
    }

    public void setFreight_forwarder(String freight_forwarder) {
        this.freight_forwarder = freight_forwarder;
    }

    public String getPick_up_location() {
        return pick_up_location;
    }

    public void setPick_up_location(String pick_up_location) {
        this.pick_up_location = pick_up_location;
    }

    public String getBl_number() {
        return bl_number;
    }

    public void setBl_number(String bl_number) {
        this.bl_number = bl_number;
    }

    public String getMcn_no() {
        return mcn_no;
    }

    public void setMcn_no(String mcn_no) {
        this.mcn_no = mcn_no;
    }

    public String getNumber_of_stops() {
        return number_of_stops;
    }

    public void setNumber_of_stops(String number_of_stops) {
        this.number_of_stops = number_of_stops;
    }

    public String getContract_commodity() {
        return contract_commodity;
    }

    public void setContract_commodity(String contract_commodity) {
        this.contract_commodity = contract_commodity;
    }

    public String getBl_commodity() {
        return bl_commodity;
    }

    public void setBl_commodity(String bl_commodity) {
        this.bl_commodity = bl_commodity;
    }

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
    }

    public String getAmendment_number() {
        return amendment_number;
    }

    public void setAmendment_number(String amendment_number) {
        this.amendment_number = amendment_number;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination_country() {
        return destination_country;
    }

    public void setDestination_country(String destination_country) {
        this.destination_country = destination_country;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getWeek_no() {
        return week_no;
    }

    public void setWeek_no(String week_no) {
        this.week_no = week_no;
    }

    public String getSailing_date() {
        return sailing_date;
    }

    public void setSailing_date(String sailing_date) {
        this.sailing_date = sailing_date;
    }

    public String getBooking_received_date() {
        return booking_received_date;
    }

    public void setBooking_received_date(String booking_received_date) {
        this.booking_received_date = booking_received_date;
    }

    public String getBooking_accepted_date() {
        return booking_accepted_date;
    }

    public void setBooking_accepted_date(String booking_accepted_date) {
        this.booking_accepted_date = booking_accepted_date;
    }

    public String getPick_up_date() {
        return pick_up_date;
    }

    public void setPick_up_date(String pick_up_date) {
        this.pick_up_date = pick_up_date;
    }

    public String getIn_gate_date() {
        return in_gate_date;
    }

    public void setIn_gate_date(String in_gate_date) {
        this.in_gate_date = in_gate_date;
    }

    public String getDestination_vessel_arrival() {
        return destination_vessel_arrival;
    }

    public void setDestination_vessel_arrival(String destination_vessel_arrival) {
        this.destination_vessel_arrival = destination_vessel_arrival;
    }

    public String getArrival_notice_print_date() {
        return arrival_notice_print_date;
    }

    public void setArrival_notice_print_date(String arrival_notice_print_date) {
        this.arrival_notice_print_date = arrival_notice_print_date;
    }

    public String getDispatch_full() {
        return dispatch_full;
    }

    public void setDispatch_full(String dispatch_full) {
        this.dispatch_full = dispatch_full;
    }

    public String getReceived_empty() {
        return received_empty;
    }

    public void setReceived_empty(String received_empty) {
        this.received_empty = received_empty;
    }

    public String getBooking_source() {
        return booking_source;
    }

    public void setBooking_source(String booking_source) {
        this.booking_source = booking_source;
    }

    public String getSi_source() {
        return si_source;
    }

    public void setSi_source(String si_source) {
        this.si_source = si_source;
    }

    public String getSales_rep() {
        return sales_rep;
    }

    public void setSales_rep(String sales_rep) {
        this.sales_rep = sales_rep;
    }

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getSize_type() {
        return size_type;
    }

    public void setSize_type(String size_type) {
        this.size_type = size_type;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCarriage_contract() {
        return carriage_contract;
    }

    public void setCarriage_contract(String carriage_contract) {
        this.carriage_contract = carriage_contract;
    }

    public String getCargo_weight_lbs() {
        return cargo_weight_lbs;
    }

    public void setCargo_weight_lbs(String cargo_weight_lbs) {
        this.cargo_weight_lbs = cargo_weight_lbs;
    }

    public String getCargo_weight_kgs() {
        return cargo_weight_kgs;
    }

    public void setCargo_weight_kgs(String cargo_weight_kgs) {
        this.cargo_weight_kgs = cargo_weight_kgs;
    }

    public String getFt20ofr() {
        return ft20ofr;
    }

    public void setFt20ofr(String ft20ofr) {
        this.ft20ofr = ft20ofr;
    }

    public String getFt20trev() {
        return ft20trev;
    }

    public void setFt20trev(String ft20trev) {
        this.ft20trev = ft20trev;
    }

    public String getFt40ofr() {
        return ft40ofr;
    }

    public void setFt40ofr(String ft40ofr) {
        this.ft40ofr = ft40ofr;
    }

    public String getFt40trev() {
        return ft40trev;
    }

    public void setFt40trev(String ft40trev) {
        this.ft40trev = ft40trev;
    }

    public String get20ft() {
        return _20ft;
    }

    public void set20ft(String _20ft) {
        this._20ft = _20ft;
    }

    public String get40ft() {
        return _40ft;
    }

    public void set40ft(String _40ft) {
        this._40ft = _40ft;
    }

    public String getShipment_type() {
        return shipment_type;
    }

    public void setShipment_type(String shipment_type) {
        this.shipment_type = shipment_type;
    }

    public String getAll_in_amount() {
        return all_in_amount;
    }

    public void setAll_in_amount(String all_in_amount) {
        this.all_in_amount = all_in_amount;
    }

    public String getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(String total_revenue) {
        this.total_revenue = total_revenue;
    }

    public String getTotalrevenuec() {
        return totalrevenuec;
    }

    public void setTotalrevenuec(String totalrevenuec) {
        this.totalrevenuec = totalrevenuec;
    }

    public String getOfrc() {
        return ofrc;
    }

    public void setOfrc(String ofrc) {
        this.ofrc = ofrc;
    }

    public String getOfr() {
        return ofr;
    }

    public void setOfr(String ofr) {
        this.ofr = ofr;
    }

    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getOnc() {
        return onc;
    }

    public void setOnc(String onc) {
        this.onc = onc;
    }

    public String getBaf() {
        return baf;
    }

    public void setBaf(String baf) {
        this.baf = baf;
    }

    public String getUss() {
        return uss;
    }

    public void setUss(String uss) {
        this.uss = uss;
    }

    public String getIfr() {
        return ifr;
    }

    public void setIfr(String ifr) {
        this.ifr = ifr;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getWhf() {
        return whf;
    }

    public void setWhf(String whf) {
        this.whf = whf;
    }

    public String getLss() {
        return lss;
    }

    public void setLss(String lss) {
        this.lss = lss;
    }

    public String getFif() {
        return fif;
    }

    public void setFif(String fif) {
        this.fif = fif;
    }

    public String getHgt() {
        return hgt;
    }

    public void setHgt(String hgt) {
        this.hgt = hgt;
    }

    public String getThl() {
        return thl;
    }

    public void setThl(String thl) {
        this.thl = thl;
    }

    public String getThd() {
        return thd;
    }

    public void setThd(String thd) {
        this.thd = thd;
    }

    public String getVhs() {
        return vhs;
    }

    public void setVhs(String vhs) {
        this.vhs = vhs;
    }

    public String getCra() {
        return cra;
    }

    public void setCra(String cra) {
        this.cra = cra;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getIrs() {
        return irs;
    }

    public void setIrs(String irs) {
        this.irs = irs;
    }

    public String getGri() {
        return gri;
    }

    public void setGri(String gri) {
        this.gri = gri;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

}
