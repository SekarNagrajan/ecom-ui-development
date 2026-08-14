/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

/**
 *
 * @author gobinath.t
 */
public class RateVo {

    private String loadport;
    private String dischport;
    private String eqptype;
    private String commodity;
    private String validfrom;
    private String validto;
    private String currency;
    private String amount;
    private int quantity;
    private String chargeName;
    private String chargeCode;
    private String shipmentdate;
    private String nor;
    private String soc;
    private String originPort;
    private String DeliveryPort;
    private String subToCharges;
    private String carrTerms;
    private String rateNo;
    private String externalrate;

    public String getSubToCharges() {
        return subToCharges;
    }

    public void setSubToCharges(String subToCharges) {
        this.subToCharges = subToCharges;
    }

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDeliveryPort() {
        return DeliveryPort;
    }

    public void setDeliveryPort(String DeliveryPort) {
        this.DeliveryPort = DeliveryPort;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getLoadport() {
        return loadport;
    }

    public void setLoadport(String loadport) {
        this.loadport = loadport;
    }

    public String getDischport() {
        return dischport;
    }

    public void setDischport(String dischport) {
        this.dischport = dischport;
    }

    public String getEqptype() {
        return eqptype;
    }

    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(String validfrom) {
        this.validfrom = validfrom;
    }

    public String getValidto() {
        return validto;
    }

    public void setValidto(String validto) {
        this.validto = validto;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getShipmentdate() {
        return shipmentdate;
    }

    public void setShipmentdate(String shipmentdate) {
        this.shipmentdate = shipmentdate;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getCarrTerms() {
        return carrTerms;
    }

    public void setCarrTerms(String carrTerms) {
        this.carrTerms = carrTerms;
    }

    public String getRateNo() {
        return rateNo;
    }

    public void setRateNo(String rateNo) {
        this.rateNo = rateNo;
    }

    public String getExternalrate() {
        return externalrate;
    }

    public void setExternalrate(String externalrate) {
        this.externalrate = externalrate;
    }

}
