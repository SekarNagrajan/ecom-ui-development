/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import java.util.List;
import net.ecom.common.Validation;

/**
 *
 * @author arunprasath
 */
public class SIPartiesVO implements Serializable {

    private String custtype = null;
    private String address1 = null;
    private String address2 = null;
    private String custname = null;
    private String city = null;
    private String telephone = null;
    private String fax = null;
    private String email = null;
    private String countryid = null;
    private String taxId = null;
    private String custCode = "";
    private String notifyCheck = null;
    private String vesselcode = "";
    private String voyagecode = "";
    private String bound = "";
    private String pol = "";
    private String pod = "";
    private String receipt = "";
    private String delivery = "";
    private String shipmentTerm = null;
    private String transportOrigin = null;
    private String transportDestination = null;
    private String polName = null;
    private String podName = null;
    private String vesselName = null;
    private String pla_free_txt = "";
    private String pol_free_txt = "";
    private String pod_free_txt = "";
    private String pld_free_txt = "";
    private String vessel_free_txt = "";
    private String voyage_free_txt = "";
    private String vslvoyage_free_txt = "";
    private String toorder = null;
    private String agencyRefNo = null;
    private String ccMailId = null;
    private String shipperName = null;
    private String consigneeName = null;
    private String notifyName = null;
    private String shipperHeader = null;
    private String consigneeHeader = null;
    private String notifyHeader = null;
    private String polat = null;
    private String podat = null;
    private String Name = null;
    private String state = null;
    private String zipcode = null;
    private String AESNumber = null;
    private String AESDiscliamer = null;
    private String releasetype = null;
    private List<ChargesVo> chargeDetails = null;
    private String In_origin = null;
    private String In_delivery = null;
    private String declaredValue = null;
    private String servicecode = null;
    private String bltype = null;
    private String ACID = null;
    private String fcnno = null;
    private String Freight = null;
    private String eori = null;
    private String persontype = null;
    private String cargoprotectid;
    private String finalport;
    private boolean ismaskport = false;
    private boolean iseuport = false;
    private String ebook_t2lfiling = null;
    private String ebook_nvocc = null;
    private String packinglst = null;
    private String invoiceupload = null;
    private String orgTelType = null;
    private String ebook_EnsDocType = null;
    private String ebook_EnsFiling = null;
    private String bltype_UAE = null;
    private String mpci_id = null;

    public String getEbook_EnsFiling() {
        return ebook_EnsFiling;
    }

    public void setEbook_EnsFiling(String ebook_EnsFiling) {
        this.ebook_EnsFiling = ebook_EnsFiling;
    }

    private ENSDeclarantVo ensdecvo = null;

    public String getFinalport() {
        return finalport;
    }

    public String getEbook_t2lfiling() {
        return ebook_t2lfiling;
    }

    public void setEbook_t2lfiling(String ebook_t2lfiling) {
        this.ebook_t2lfiling = ebook_t2lfiling;
    }

    public String getEbook_nvocc() {
        return ebook_nvocc;
    }

    public void setEbook_nvocc(String ebook_nvocc) {
        this.ebook_nvocc = ebook_nvocc;
    }

    public void setFinalport(String finalport) {
        this.finalport = finalport;
    }

    public String getEori() {
        return eori;
    }

    public void setEori(String eori) {
        this.eori = eori;
    }

    public String getPersontype() {
        return persontype;
    }

    public void setPersontype(String persontype) {
        this.persontype = persontype;
    }

    public String getACID() {
        return ACID;
    }

    public void setACID(String ACID) {
        this.ACID = ACID;
    }

    public String getPolat() {
        return polat;
    }

    public void setPolat(String polat) {
        this.polat = polat;
    }

    public String getPodat() {
        return podat;
    }

    public void setPodat(String podat) {
        this.podat = podat;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getShipperHeader() {
        return shipperHeader;
    }

    public void setShipperHeader(String shipperHeader) {
        this.shipperHeader = shipperHeader;
    }

    public String getConsigneeHeader() {
        return consigneeHeader;
    }

    public void setConsigneeHeader(String consigneeHeader) {
        this.consigneeHeader = consigneeHeader;
    }

    public String getNotifyHeader() {
        return notifyHeader;
    }

    public void setNotifyHeader(String notifyHeader) {
        this.notifyHeader = notifyHeader;
    }

    public String getCcMailId() {
        return ccMailId;
    }

    public void setCcMailId(String ccMailId) {
        this.ccMailId = ccMailId;
    }

    public String getAgencyRefNo() {
        return agencyRefNo;
    }

    public void setAgencyRefNo(String agencyRefNo) {
        this.agencyRefNo = agencyRefNo;
    }

    public String getPla_free_txt() {
        return pla_free_txt;
    }

    public void setPla_free_txt(String pla_free_txt) {
        this.pla_free_txt = pla_free_txt;
    }

    public String getPld_free_txt() {
        return pld_free_txt;
    }

    public void setPld_free_txt(String pld_free_txt) {
        this.pld_free_txt = pld_free_txt;
    }

    public String getPod_free_txt() {
        return pod_free_txt;
    }

    public void setPod_free_txt(String pod_free_txt) {
        this.pod_free_txt = pod_free_txt;
    }

    public String getPol_free_txt() {
        return pol_free_txt;
    }

    public void setPol_free_txt(String pol_free_txt) {
        this.pol_free_txt = pol_free_txt;
    }

    public String getToorder() {
        return toorder;
    }

    public void setToorder(String toorder) {
        this.toorder = toorder;
    }

    public String getVessel_free_txt() {
        return vessel_free_txt;
    }

    public void setVessel_free_txt(String vessel_free_txt) {
        this.vessel_free_txt = vessel_free_txt;
    }

    public String getVoyage_free_txt() {
        return voyage_free_txt;
    }

    public void setVoyage_free_txt(String voyage_free_txt) {
        this.voyage_free_txt = voyage_free_txt;
    }

    public String getVslvoyage_free_txt() {
        return vslvoyage_free_txt;
    }

    public void setVslvoyage_free_txt(String vslvoyage_free_txt) {
        this.vslvoyage_free_txt = vslvoyage_free_txt;
    }

    /**
     * @return the custtype
     */
    public String getCusttype() {
        return custtype;
    }

    /**
     * @param custtype the custtype to set
     */
    public void setCusttype(String custtype) {
        this.custtype = custtype;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the custname
     */
    public String getCustname() {
        return custname;
    }

    /**
     * @param custname the custname to set
     */
    public void setCustname(String custname) {
        this.custname = custname;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the vesselcode
     */
    public String getVesselcode() {
        return Validation.checkNull(vesselcode);
    }

    /**
     * @param vesselcode the vesselcode to set
     */
    public void setVesselcode(String vesselcode) {
        this.vesselcode = vesselcode;
    }

    /**
     * @return the voyagecode
     */
    public String getVoyagecode() {
        return Validation.checkNull(voyagecode);
    }

    /**
     * @param voyagecode the voyagecode to set
     */
    public void setVoyagecode(String voyagecode) {
        this.voyagecode = voyagecode;
    }

    /**
     * @return the bound
     */
    public String getBound() {
        return Validation.checkNull(bound);
    }

    /**
     * @param bound the bound to set
     */
    public void setBound(String bound) {
        this.bound = bound;
    }

    /**
     * @return the pol
     */
    public String getPol() {
        return Validation.checkNull(pol);
    }

    /**
     * @param pol the pol to set
     */
    public void setPol(String pol) {
        this.pol = pol;
    }

    /**
     * @return the pod
     */
    public String getPod() {
        return Validation.checkNull(pod);
    }

    /**
     * @param pod the pod to set
     */
    public void setPod(String pod) {
        this.pod = pod;
    }

    /**
     * @return the receipt
     */
    public String getReceipt() {
        return Validation.checkNull(receipt);
    }

    /**
     * @param receipt the receipt to set
     */
    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    /**
     * @return the delivery
     */
    public String getDelivery() {
        return Validation.checkNull(delivery);
    }

    /**
     * @param delivery the delivery to set
     */
    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the countryid
     */
    public String getCountryid() {
        return countryid;
    }

    /**
     * @param countryid the countryid to set
     */
    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    /**
     * @return the custCode
     */
    public String getCustCode() {
        return Validation.checkNull(custCode);
    }

    /**
     * @param custCode the custCode to set
     */
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    /**
     * @return the shipmentTerm
     */
    public String getShipmentTerm() {
        return shipmentTerm;
    }

    /**
     * @param shipmentTerm the shipmentTerm to set
     */
    public void setShipmentTerm(String shipmentTerm) {
        this.shipmentTerm = shipmentTerm;
    }

    /**
     * @return the transportOrigin
     */
    public String getTransportOrigin() {
        return transportOrigin;
    }

    /**
     * @param transportOrigin the transportOrigin to set
     */
    public void setTransportOrigin(String transportOrigin) {
        this.transportOrigin = transportOrigin;
    }

    /**
     * @return the transportDestination
     */
    public String getTransportDestination() {
        return transportDestination;
    }

    /**
     * @param transportDestination the transportDestination to set
     */
    public void setTransportDestination(String transportDestination) {
        this.transportDestination = transportDestination;
    }

    /**
     * @return the polName
     */
    public String getPolName() {
        return polName;
    }

    /**
     * @param polName the polName to set
     */
    public void setPolName(String polName) {
        this.polName = polName;
    }

    /**
     * @return the podName
     */
    public String getPodName() {
        return podName;
    }

    /**
     * @param podName the podName to set
     */
    public void setPodName(String podName) {
        this.podName = podName;
    }

    /**
     * @return the vesselName
     */
    public String getVesselName() {
        return vesselName;
    }

    /**
     * @param vesselName the vesselName to set
     */
    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    /**
     * @return the notifyCheck
     */
    public String getNotifyCheck() {
        return notifyCheck;
    }

    /**
     * @param notifyCheck the notifyCheck to set
     */
    public void setNotifyCheck(String notifyCheck) {
        this.notifyCheck = notifyCheck;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAESNumber() {
        return AESNumber;
    }

    public void setAESNumber(String AESNumber) {
        this.AESNumber = AESNumber;
    }

    public String getAESDiscliamer() {
        return AESDiscliamer;
    }

    public void setAESDiscliamer(String AESDiscliamer) {
        this.AESDiscliamer = AESDiscliamer;
    }

    public String getReleasetype() {
        return releasetype;
    }

    public void setReleasetype(String releasetype) {
        this.releasetype = releasetype;
    }

    public List<ChargesVo> getChargeDetails() {
        return chargeDetails;
    }

    public void setChargeDetails(List<ChargesVo> chargeDetails) {
        this.chargeDetails = chargeDetails;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;

    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIn_origin() {
        return In_origin;
    }

    public void setIn_origin(String In_origin) {
        this.In_origin = In_origin;
    }

    public String getIn_delivery() {
        return In_delivery;
    }

    public void setIn_delivery(String In_delivery) {
        this.In_delivery = In_delivery;
    }

    public String getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(String declaredValue) {
        this.declaredValue = declaredValue;
    }

    public String getServicecode() {
        return servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    public String getBltype() {
        return bltype;
    }

    public void setBltype(String bltype) {
        this.bltype = bltype;
    }

    public String getFcnno() {
        return fcnno;
    }

    public void setFcnno(String fcnno) {
        this.fcnno = fcnno;
    }

    public String getFreight() {
        return Freight;
    }

    public void setFreight(String Freight) {
        this.Freight = Freight;
    }

    public String getCargoprotectid() {
        return cargoprotectid;
    }

    public void setCargoprotectid(String cargoprotectid) {
        this.cargoprotectid = cargoprotectid;
    }

    public boolean isIsmaskport() {
        return ismaskport;
    }

    public void setIsmaskport(boolean ismaskport) {
        this.ismaskport = ismaskport;
    }

    public ENSDeclarantVo getEnsdecvo() {
        return ensdecvo;
    }

    public void setEnsdecvo(ENSDeclarantVo ensdecvo) {
        this.ensdecvo = ensdecvo;
    }

    public boolean isIseuport() {
        return iseuport;
    }

    public void setIseuport(boolean iseuport) {
        this.iseuport = iseuport;
    }

    public String getPackinglst() {
        return packinglst;
    }

    public void setPackinglst(String packinglst) {
        this.packinglst = packinglst;
    }

    public String getInvoiceupload() {
        return invoiceupload;
    }

    public void setInvoiceupload(String invoiceupload) {
        this.invoiceupload = invoiceupload;
    }

    public String getOrgTelType() {
        return orgTelType;
    }

    public void setOrgTelType(String orgTelType) {
        this.orgTelType = orgTelType;
    }

    public String getEbook_EnsDocType() {
        return ebook_EnsDocType;
    }

    public void setEbook_EnsDocType(String ensdoctype) {
        this.ebook_EnsDocType = ensdoctype;
    }

    public String getBltype_UAE() {
        return bltype_UAE;
    }

    public void setBltype_UAE(String bltype_UAE) {
        this.bltype_UAE = bltype_UAE;
    }

    public String getMpci_id() {
        return mpci_id;
    }

    public void setMpci_id(String mpci_id) {
        this.mpci_id = mpci_id;
    }

}
