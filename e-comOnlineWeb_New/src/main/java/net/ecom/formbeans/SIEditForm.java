package net.ecom.formbeans;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import net.ecom.common.Validation;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class SIEditForm extends org.apache.struts.action.ActionForm {

    private String spname;
    private String spaddr;
    private String spaddr1;
    private String spcity;
    private String spCityName = "";
    private String spCustCode = "";
    private String spphone;
    private String spfax;
    private String spemail;
    private String spstate;
    private String spzipcode;
    private String spcountry;
    private String spCountryName = "";
    private String spIsCountry = "";
    private String spIsCity = "";
    private String sptaxId = "";
    private String speori = "";
    private String sppersontype = "";
    private String consname;
    private String consaddr;
    private String consaddr1;
    private String conscity;
    private String consCityName = "";
    private String consCustCode = "";
    private String consphone;
    private String consfax;
    private String consemail;
    private String consstate;
    private String conszipcode;
    private String conscountry;
    private String consCountryName = "";
    private String consIsCountry = "";
    private String consIsCity = "";
    private String consTaxId = "";
    private String conseori = "";
    private String conspersontype = "";
    private String npname;
    private String npaddr;
    private String npaddr1;
    private String npcity;
    private String npCityName = "";
    private String npCustCode = "";
    private String npphone;
    private String npfax;
    private String npemail;
    private String npstate;
    private String npzipcode;
    private String npcountry;
    private String npCountryName = "";
    private String npIsCountry = "";
    private String npIsCity = "";
    private String npTaxId = "";
    private String npeori = "";
    private String nppersontype = "";
    private String containerno;
    private String type;
    private String[] carrierseal;
    private String[] shipperseal;
    private String[] otherseal;
    private String[] commodity;
    private String[] itemqty;
    private String[] cargowt;
    private String[] unit;
    private String[] select;
    private String savetype = "";
    private String sino = "";
    private String blno = "";
    private String bookno = "";
    private String notifyCheck = null;
    private String notify2Check = null;
    private String notify3Check = null;
    private String finalportid = null;
    private String originid = null;
    private String orgTelType = null;
    //
    private FormFile file_path1 = null;
    private FormFile file_path2 = null;
    private FormFile file_path3 = null;
    private FormFile file_path4 = null;
    private FormFile file_path5 = null;

    private FormFile file_path6 = null;
    private FormFile file_path7 = null;
    private FormFile file_path8 = null;
    private FormFile file_path9 = null;
    private FormFile file_path10 = null;
    private FormFile file_path11 = null;
    private FormFile file_path12 = null;
    private FormFile file_path13 = null;
    private FormFile file_path14 = null;
    private FormFile file_path15 = null;

    private String filename1 = null;
    private String filename2 = null;
    private String filename3 = null;
    private String filename4 = null;
    private String filename5 = null;
    private String sicustremarks = null;
    private String consigneeordercheck = null;
    private String notifyordercheck = null;
    private String custref = "";
    private String pla_free_txt = "";
    private String pol_free_txt = "";
    private String pod_free_txt = "";
    private String pld_free_txt = "";
    private String voyage_free_txt = "";
    private String vessel_free_txt = "";
    private String vslvoyage_free_txt = "";
    private String packinglst = null;
    private String invoiceupload = null;

    private String hid_pla_free_txt = "";
    private String hid_pol_free_txt = "";
    private String hid_pod_free_txt = "";
    private String hid_pld_free_txt = "";
    private String hid_vslvoyage_free_txt = "";
    private String resubmission = "";

    //
    private String np2name;
    private String np2addr;
    private String np2addr1;
    private String np2city;
    private String np2CityName = "";
    private String np2CustCode = "";
    private String np2phone;
    private String np2country;
    private String np2CountryName = "";
    private String np2fax;
    private String np2email;
    private String np2state;
    private String np2eori = "";
    private String np2persontype = "";
    //
    private String np3name;
    private String np3addr;
    private String np3addr1;
    private String np3city;
    private String np3CityName = "";
    private String np3CustCode = "";
    private String np3phone;
    private String np3country;
    private String np3CountryName = "";
    private String np3fax;
    private String np3email;
    private String np3state;
    private String np3eori = "";
    private String np3persontype = "";
    private String appVersion;
    private String ccMailId;
    private String spnameheader;
    private String headerShipper;
    private String headerConsignee;
    private String headerNotify;
    private String consnameheader;
    private String npnameheader;
    private String fwnameheader;
    private String whnameheader;
    private String fwname;
    private String fwaddr;
    private String fwaddr1;
    private String fwcity;
    private String fwCityName = "";
    private String fwCustCode = "";
    private String fwphone;
    private String fwcountry;
    private String fwCountryName = "";
    private String fwfax;
    private String fwemail;
    private String fwstate;
    private String fweori = "";
    private String fwpersontype = "";

    private String whname;
    private String whaddr;
    private String whaddr1;
    private String whcity;
    private String whCityName = "";
    private String whCustCode = "";
    private String whphone;
    private String whcountry;
    private String whCountryName = "";
    private String whfax;
    private String whemail;
    private String wheori = "";
    private String whpersontype = "";
    private String siAESnumber;
    private String AESDisclaimer;
//Buyer Customer
    private String byname;
    private String byaddr;
    private String byaddr1;
    private String bycity;
    private String byCityName = "";
    private String byCustCode = "";
    private String byphone;
    private String bycountry;
    private String byCountryName = "";
    private String byfax;
    private String byemail;
    private String bystate;
    private String byeori = "";
    private String bypersontype = "";
    private String byzipcode;
public String getByzipcode() {
        return byzipcode;
    }

    public void setByzipcode(String byzipcode) {
        this.byzipcode = byzipcode;
    }
    //Seller Customer
    private String slname;
    private String sladdr;
    private String sladdr1;
    private String slcity;
    private String slCityName = "";
    private String slCustCode = "";
    private String slphone;
    private String slcountry;
    private String slCountryName = "";
    private String slfax;
    private String slemail;
    private String slstate;
    private String sleori = "";
    private String slpersontype = "";
    private String slzipcode;

    public String getSlzipcode() {
        return slzipcode;
    }

    public void setSlzipcode(String slzipcode) {
        this.slzipcode = slzipcode;
    }
    private String deletedfilename = "";
    private String releasetype = null;
    private String cargoprotectid;

    String apname;
    String apCustCode;
    String apaddr;
    String apaddr1;
    String apcity;
    String apphone;
    String apfax;
    String apcountry;
    String apemail;
    String apCountryName;
    String apCityName;
    String apstate;
    String apzipcode;
    private String apeori = "";
    private String appersontype = "";

    String declaredValue = null;
    String acidValue = null;
    String blType_UAE = null;
    String mpciId_UAE = null;

    //ENS Filling
    private String euCustZone = "Y";
    private String blTypeENS = null;
    private String ensFillingType = null;
    private String paymentMethod = null;

    private String declarantName = null;
    private String declarantAddress = null;
    private String declarantAddress1 = null;
    private String declarantCity = null;
    private String declarantCountry = null;
    private String declarantState = null;
    private String declarantTelephone = null;
    private String declarantEORI = null;
    private String declarantEmail = null;
    private String declarantFillingType = null;
    private String declarantZipcode = null;

    public String getDeclarantZipcode() {
        return declarantZipcode;
    }

    public void setDeclarantZipcode(String declarantZipcode) {
        this.declarantZipcode = declarantZipcode;
    }
    //Zip Code
    private String np2zipcode = null;
    private String np3zipcode = null;
    private String fwzipcode = null;
    
    

    public String getNp2zipcode() {
        return np2zipcode;
    }

    public void setNp2zipcode(String np2zipcode) {
        this.np2zipcode = np2zipcode;
    }

    public String getNp3zipcode() {
        return np3zipcode;
    }

    public void setNp3zipcode(String np3zipcode) {
        this.np3zipcode = np3zipcode;
    }

    public String getFwzipcode() {
        return fwzipcode;
    }

    public void setFwzipcode(String fwzipcode) {
        this.fwzipcode = fwzipcode;
    }

    public String getOriginid() {
        return originid;
    }

    public void setOriginid(String originid) {
        this.originid = originid;
    }

    public String getFinalportid() {
        return finalportid;
    }

    public void setFinalportid(String finalportid) {
        this.finalportid = finalportid;
    }
    private String bltype = null;
    private String fcnno = null;
    private String blstatus = null;
    private String freightoption = null;
    private String ebook_t2lfiling = null;
    private String ebook_nvocc = null;
    private String ebook_EnsDocType = null;
   private String ebook_EnsFiling = null;

    public String getEbook_EnsFiling() {
    return ebook_EnsFiling;
}

 public void setEbook_EnsFiling(String ebook_EnsFiling) {
    this.ebook_EnsFiling = ebook_EnsFiling;
 }

    public String getEbook_EnsDocType() {
        return ebook_EnsDocType;
    }

    public void setEbook_EnsDocType(String ebook_EnsDocType) {
        this.ebook_EnsDocType = ebook_EnsDocType;
    }

    public String getAcidValue() {
        return acidValue;
    }

    public void setAcidValue(String acidValue) {
        this.acidValue = acidValue;
    }

    public String getSpnameheader() {
        return spnameheader;
    }

    public void setSpnameheader(String spnameheader) {
        this.spnameheader = spnameheader;
    }

    public String getHeaderShipper() {
        return headerShipper;
    }

    public void setHeaderShipper(String headerShipper) {
        this.headerShipper = headerShipper;
    }

    public String getHeaderConsignee() {
        return headerConsignee;
    }

    public void setHeaderConsignee(String headerConsignee) {
        this.headerConsignee = headerConsignee;
    }

    public String getHeaderNotify() {
        return headerNotify;
    }

    public void setHeaderNotify(String headerNotify) {
        this.headerNotify = headerNotify;
    }

    public String getConsnameheader() {
        return consnameheader;
    }

    public void setConsnameheader(String consnameheader) {
        this.consnameheader = consnameheader;
    }

    public String getNpnameheader() {
        return npnameheader;
    }

    public String getFwnameheader() {
        return fwnameheader;
    }

    public void setFwnameheader(String fwnameheader) {
        this.fwnameheader = fwnameheader;
    }

    public String getWhnameheader() {
        return whnameheader;
    }

    public void setWhnameheader(String whnameheader) {
        this.whnameheader = whnameheader;
    }

    public void setNpnameheader(String npnameheader) {
        this.npnameheader = npnameheader;
    }

    public String getCcMailId() {
        return ccMailId;
    }

    public void setCcMailId(String ccMailId) {
        this.ccMailId = ccMailId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    //Notify2
    private String notify2ordercheck = null;
    //
    //Notify3
    private String notify3ordercheck = null;

    public String getNotify2ordercheck() {
        return notify2ordercheck;
    }

    public void setNotify2ordercheck(String notify2ordercheck) {
        this.notify2ordercheck = notify2ordercheck;
    }

    public String getNotify3ordercheck() {
        return notify3ordercheck;
    }

    public void setNotify3ordercheck(String notify3ordercheck) {
        this.notify3ordercheck = notify3ordercheck;
    }

    public String getResubmission() {
        return resubmission;
    }

    public void setResubmission(String resubmission) {
        this.resubmission = resubmission;
    }

    public String getConsigneeordercheck() {
        return consigneeordercheck;
    }

    public void setConsigneeordercheck(String consigneeordercheck) {
        this.consigneeordercheck = consigneeordercheck;
    }

    public String getCustref() {
        return custref;
    }

    public void setCustref(String custref) {
        this.custref = custref;
    }

    public String getNotifyordercheck() {
        return notifyordercheck;
    }

    public void setNotifyordercheck(String notifyordercheck) {
        this.notifyordercheck = notifyordercheck;
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

    public String getSicustremarks() {
        return sicustremarks;
    }

    public void setSicustremarks(String sicustremarks) {
        this.sicustremarks = sicustremarks;
    }

    public String getBlno() {
        return blno;
    }

    public void setBlno(String blno) {
        this.blno = blno;
    }

    public String getBookno() {
        return bookno;
    }

    public void setBookno(String bookno) {
        this.bookno = bookno;
    }
    private ArrayList containerdetails;

    /**
     * @return the spname
     */
    public String getSpname() {
        return Validation.checkNull(spname);
    }

    /**
     * @param spname the spname to set
     */
    public void setSpname(String spname) {
        this.spname = spname;
    }

    /**
     * @return the spaddr
     */
    public String getSpaddr() {
        return Validation.checkNull(spaddr);
    }

    /**
     * @param spaddr the spaddr to set
     */
    public void setSpaddr(String spaddr) {
        this.spaddr = spaddr;
    }

    /**
     * @return the spaddr1
     */
    public String getSpaddr1() {
        return Validation.checkNull(spaddr1);
    }

    /**
     * @param spaddr1 the spaddr1 to set
     */
    public void setSpaddr1(String spaddr1) {
        this.spaddr1 = spaddr1;
    }

    /**
     * @return the spcity
     */
    public String getSpcity() {
        return Validation.checkNull(spcity);
    }

    /**
     * @param spcity the spcity to set
     */
    public void setSpcity(String spcity) {
        this.spcity = spcity;
    }

    /**
     * @return the spphone
     */
    public String getSpphone() {
        return Validation.checkNull(spphone);
    }

    /**
     * @param spphone the spphone to set
     */
    public void setSpphone(String spphone) {
        this.spphone = spphone;
    }

    /**
     * @return the consname
     */
    public String getConsname() {
        return Validation.checkNull(consname);
    }

    /**
     * @param consname the consname to set
     */
    public void setConsname(String consname) {
        this.consname = consname;
    }

    /**
     * @return the consaddr
     */
    public String getConsaddr() {
        return Validation.checkNull(consaddr);
    }

    /**
     * @param consaddr the consaddr to set
     */
    public void setConsaddr(String consaddr) {
        this.consaddr = consaddr;
    }

    /**
     * @return the consaddr1
     */
    public String getConsaddr1() {
        return Validation.checkNull(consaddr1);
    }

    /**
     * @param consaddr1 the consaddr1 to set
     */
    public void setConsaddr1(String consaddr1) {
        this.consaddr1 = consaddr1;
    }

    /**
     * @return the conscity
     */
    public String getConscity() {
        return Validation.checkNull(conscity);
    }

    /**
     * @param conscity the conscity to set
     */
    public void setConscity(String conscity) {
        this.conscity = conscity;
    }

    /**
     * @return the consphone
     */
    public String getConsphone() {
        return Validation.checkNull(consphone);
    }

    /**
     * @param consphone the consphone to set
     */
    public void setConsphone(String consphone) {
        this.consphone = consphone;
    }

    /**
     * @return the npname
     */
    public String getNpname() {
        return Validation.checkNull(npname);
    }

    /**
     * @param npname the npname to set
     */
    public void setNpname(String npname) {
        this.npname = npname;
    }

    /**
     * @return the npaddr
     */
    public String getNpaddr() {
        return Validation.checkNull(npaddr);
    }

    /**
     * @param npaddr the npaddr to set
     */
    public void setNpaddr(String npaddr) {
        this.npaddr = npaddr;
    }

    /**
     * @return the npaddr1
     */
    public String getNpaddr1() {
        return Validation.checkNull(npaddr1);
    }

    /**
     * @param npaddr1 the npaddr1 to set
     */
    public void setNpaddr1(String npaddr1) {
        this.npaddr1 = npaddr1;
    }

    /**
     * @return the npcity
     */
    public String getNpcity() {
        return Validation.checkNull(npcity);
    }

    /**
     * @param npcity the npcity to set
     */
    public void setNpcity(String npcity) {
        this.npcity = npcity;
    }

    /**
     * @return the npphone
     */
    public String getNpphone() {
        return Validation.checkNull(npphone);
    }

    /**
     * @param npphone the npphone to set
     */
    public void setNpphone(String npphone) {
        this.npphone = npphone;
    }

    /**
     * @return the np2name
     */
    public String getNp2name() {
        return Validation.checkNull(np2name);
    }

    /**
     * @param np2name the np2name to set
     */
    public void setNp2name(String np2name) {
        this.np2name = np2name;
    }

    /**
     * @return the np2addr
     */
    public String getNp2addr() {
        return Validation.checkNull(np2addr);
    }

    /**
     * @param np2addr the np2addr to set
     */
    public void setNp2addr(String np2addr) {
        this.np2addr = np2addr;
    }

    /**
     * @return the np2addr1
     */
    public String getNp2addr1() {
        return Validation.checkNull(np2addr1);
    }

    /**
     * @param np2addr1 the np2addr1 to set
     */
    public void setNp2addr1(String np2addr1) {
        this.np2addr1 = np2addr1;
    }

    /**
     * @return the np2city
     */
    public String getNp2city() {
        return Validation.checkNull(np2city);
    }

    /**
     * @param np2city the np2city to set
     */
    public void setNp2city(String np2city) {
        this.np2city = np2city;
    }

    /**
     * @return the np2phone
     */
    public String getNp2phone() {
        return Validation.checkNull(np2phone);
    }

    /**
     * @param np2phone the np2phone to set
     */
    public void setNp2phone(String np2phone) {
        this.np2phone = np2phone;
    }

    public String getFwname() {
        return fwname;
    }

    public void setFwname(String fwname) {
        this.fwname = fwname;
    }

    public String getFwaddr() {
        return fwaddr;
    }

    public void setFwaddr(String fwaddr) {
        this.fwaddr = fwaddr;
    }

    public String getFwaddr1() {
        return fwaddr1;
    }

    public void setFwaddr1(String fwaddr1) {
        this.fwaddr1 = fwaddr1;
    }

    public String getFwcity() {
        return fwcity;
    }

    public void setFwcity(String fwcity) {
        this.fwcity = fwcity;
    }

    public String getFwCityName() {
        return fwCityName;
    }

    public void setFwCityName(String fwCityName) {
        this.fwCityName = fwCityName;
    }

    public String getFwCustCode() {
        return fwCustCode;
    }

    public void setFwCustCode(String fwCustCode) {
        this.fwCustCode = fwCustCode;
    }

    public String getFwphone() {
        return fwphone;
    }

    public void setFwphone(String fwphone) {
        this.fwphone = fwphone;
    }

    public String getFwcountry() {
        return fwcountry;
    }

    public void setFwcountry(String fwcountry) {
        this.fwcountry = fwcountry;
    }

    public String getFwCountryName() {
        return fwCountryName;
    }

    public void setFwCountryName(String fwCountryName) {
        this.fwCountryName = fwCountryName;
    }

    public String getFwfax() {
        return fwfax;
    }

    public void setFwfax(String fwfax) {
        this.fwfax = fwfax;
    }

    public String getFwemail() {
        return fwemail;
    }

    public void setFwemail(String fwemail) {
        this.fwemail = fwemail;
    }

    public String getWhname() {
        return whname;
    }

    public void setWhname(String whname) {
        this.whname = whname;
    }

    public String getWhaddr() {
        return whaddr;
    }

    public void setWhaddr(String whaddr) {
        this.whaddr = whaddr;
    }

    public String getWhaddr1() {
        return whaddr1;
    }

    public void setWhaddr1(String whaddr1) {
        this.whaddr1 = whaddr1;
    }

    public String getWhcity() {
        return whcity;
    }

    public void setWhcity(String whcity) {
        this.whcity = whcity;
    }

    public String getWhCityName() {
        return whCityName;
    }

    public void setWhCityName(String whCityName) {
        this.whCityName = whCityName;
    }

    public String getWhCustCode() {
        return whCustCode;
    }

    public void setWhCustCode(String whCustCode) {
        this.whCustCode = whCustCode;
    }

    public String getWhphone() {
        return whphone;
    }

    public void setWhphone(String whphone) {
        this.whphone = whphone;
    }

    public String getWhcountry() {
        return whcountry;
    }

    public void setWhcountry(String whcountry) {
        this.whcountry = whcountry;
    }

    public String getWhCountryName() {
        return whCountryName;
    }

    public void setWhCountryName(String whCountryName) {
        this.whCountryName = whCountryName;
    }

    public String getWhfax() {
        return whfax;
    }

    public void setWhfax(String whfax) {
        this.whfax = whfax;
    }

    public String getWhemail() {
        return whemail;
    }

    public void setWhemail(String whemail) {
        this.whemail = whemail;
    }

    /**
     * @return the containerno
     */
    public String getContainerno() {
        return Validation.checkNull(containerno);
    }

    /**
     * @param containerno the containerno to set
     */
    public void setContainerno(String containerno) {
        this.containerno = containerno;
    }

    /**
     * @return the carrierseal
     */
    public String[] getCarrierseal() {
        return carrierseal;
    }

    /**
     * @param carrierseal the carrierseal to set
     */
    public void setCarrierseal(String[] carrierseal) {
        //System.out.println("[ECOM-WAR] SIEditForm carrierseal in AF=="+ carrierseal);
        this.carrierseal = carrierseal;
    }

    /**
     * @return the shipperseal
     */
    public String[] getShipperseal() {
        return shipperseal;
    }

    /**
     * @param shipperseal the shipperseal to set
     */
    public void setShipperseal(String[] shipperseal) {
        this.shipperseal = shipperseal;
    }

    /**
     * @return the otherseal
     */
    public String[] getOtherseal() {
        return otherseal;
    }

    /**
     * @param otherseal the otherseal to set
     */
    public void setOtherseal(String[] otherseal) {
        this.otherseal = otherseal;
    }

    /**
     * @return the commodity
     */
    public String[] getCommodity() {
        return commodity;
    }

    /**
     * @param commodity the commodity to set
     */
    public void setCommodity(String[] commodity) {
        this.commodity = commodity;

    }

    /**
     * @return the cargowt
     */
    public String[] getCargowt() {
        return cargowt;
    }

    /**
     * @param cargowt the cargowt to set
     */
    public void setCargowt(String[] cargowt) {
        this.cargowt = cargowt;
    }

    /**
     * @return the Unit
     */
    public String[] getUnit() {
        return unit;
    }

    /**
     * @param Unit the Unit to set
     */
    public void setUnit(String[] unit) {
        this.unit = unit;
    }

    /**
     * @return the containertype
     */
    /**
     * @return the containerdetails
     */
    public ArrayList getContainerdetails() {
        return containerdetails;
    }

    /**
     * @param containerdetails the containerdetails to set
     */
    public void setContainerdetails(ArrayList containerdetails) {
        this.containerdetails = containerdetails;
    }

    /**
     * @return the type
     */
    public String getType() {
        return Validation.checkNull(type);
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the select
     */
    public String[] getSelect() {
        return select;
    }

    /**
     * @param select the select to set
     */
    public void setSelect(String[] select) {
        this.select = select;
    }

    /**
     * @return the itemqty
     */
    public String[] getItemqty() {
        return itemqty;
    }

    /**
     * @param itemqty the itemqty to set
     */
    public void setItemqty(String[] itemqty) {
        this.itemqty = itemqty;
    }

    /**
     * @return the spfax
     */
    public String getSpfax() {
        return Validation.checkNull(spfax);
    }

    /**
     * @param spfax the spfax to set
     */
    public void setSpfax(String spfax) {
        this.spfax = spfax;
    }

    /**
     * @return the spemail
     */
    public String getSpemail() {
        return Validation.checkNull(spemail);
    }

    /**
     * @param spemail the spemail to set
     */
    public void setSpemail(String spemail) {
        this.spemail = spemail;
    }

    /**
     * @return the consfax
     */
    public String getConsfax() {
        return Validation.checkNull(consfax);
    }

    /**
     * @param consfax the consfax to set
     */
    public void setConsfax(String consfax) {
        this.consfax = consfax;
    }

    /**
     * @return the consemail
     */
    public String getConsemail() {
        return Validation.checkNull(consemail);
    }

    /**
     * @param consemail the consemail to set
     */
    public void setConsemail(String consemail) {
        this.consemail = consemail;
    }

    /**
     * @return the npfax
     */
    public String getNpfax() {
        return Validation.checkNull(npfax);
    }

    /**
     * @param npfax the npfax to set
     */
    public void setNpfax(String npfax) {
        this.npfax = npfax;
    }

    /**
     * @return the npemail
     */
    public String getNpemail() {
        return Validation.checkNull(npemail);
    }

    /**
     * @param npemail the npemail to set
     */
    public void setNpemail(String npemail) {
        this.npemail = npemail;
    }

    /**
     * @return the spcountry
     */
    public String getSpcountry() {
        return Validation.checkNull(spcountry);
    }

    /**
     * @param spcountry the spcountry to set
     */
    public void setSpcountry(String spcountry) {
        this.spcountry = spcountry;
    }

    /**
     * @return the conscountry
     */
    public String getConscountry() {
        return Validation.checkNull(conscountry);
    }

    /**
     * @param conscountry the conscountry to set
     */
    public void setConscountry(String conscountry) {
        this.conscountry = conscountry;
    }

    /**
     * @return the npcountry
     */
    public String getNpcountry() {
        return Validation.checkNull(npcountry);
    }

    /**
     * @param npcountry the npcountry to set
     */
    public void setNpcountry(String npcountry) {
        this.npcountry = npcountry;
    }

    /**
     * @return the np2country
     */
    public String getNp2country() {
        return Validation.checkNull(np2country);
    }

    /**
     * @param np2country the np2country to set
     */
    public void setNp2country(String np2country) {
        this.np2country = np2country;
    }

    /**
     * @return the spCountryName
     */
    public String getSpCountryName() {
        return Validation.checkNull(spCountryName);
    }

    /**
     * @param spCountryName the spCountryName to set
     */
    public void setSpCountryName(String spCountryName) {
        this.spCountryName = spCountryName;
    }

    /**
     * @return the consCountryName
     */
    public String getConsCountryName() {
        return Validation.checkNull(consCountryName);
    }

    /**
     * @param consCountryName the consCountryName to set
     */
    public void setConsCountryName(String consCountryName) {
        this.consCountryName = consCountryName;
    }

    /**
     * @return the npCountryName
     */
    public String getNpCountryName() {
        return Validation.checkNull(npCountryName);
    }

    /**
     * @param npCountryName the npCountryName to set
     */
    public void setNpCountryName(String npCountryName) {
        this.npCountryName = npCountryName;
    }

    /**
     * @return the np2CountryName
     */
    public String getNp2CountryName() {
        return Validation.checkNull(np2CountryName);
    }

    /**
     * @param np2CountryName the np2CountryName to set
     */
    public void setNp2CountryName(String np2CountryName) {
        this.np2CountryName = np2CountryName;
    }

    /**
     * @return the spCityName
     */
    public String getSpCityName() {
        return Validation.checkNull(spCityName);
    }

    /**
     * @param spCityName the spCityName to set
     */
    public void setSpCityName(String spCityName) {
        this.spCityName = spCityName;
    }

    /**
     * @return the consCityName
     */
    public String getConsCityName() {
        return Validation.checkNull(consCityName);
    }

    /**
     * @param consCityName the consCityName to set
     */
    public void setConsCityName(String consCityName) {
        this.consCityName = consCityName;
    }

    /**
     * @return the npCityName
     */
    public String getNpCityName() {
        return Validation.checkNull(npCityName);
    }

    /**
     * @param npCityName the npCityName to set
     */
    public void setNpCityName(String npCityName) {
        this.npCityName = npCityName;
    }

    /**
     * @return the np2CityName
     */
    public String getNp2CityName() {
        return Validation.checkNull(np2CityName);
    }

    /**
     * @param np2CityName the np2CityName to set
     */
    public void setNp2CityName(String np2CityName) {
        this.np2CityName = np2CityName;
    }

    /**
     * @return the spCustCode
     */
    public String getSpCustCode() {
        return Validation.checkNull(spCustCode);
    }

    /**
     * @param spCustCode the spCustCode to set
     */
    public void setSpCustCode(String spCustCode) {
        this.spCustCode = spCustCode;
    }

    /**
     * @return the consCustCode
     */
    public String getConsCustCode() {
        return Validation.checkNull(consCustCode);
    }

    /**
     * @param consCustCode the consCustCode to set
     */
    public void setConsCustCode(String consCustCode) {
        this.consCustCode = consCustCode;
    }

    /**
     * @return the npCustCode
     */
    public String getNpCustCode() {
        return Validation.checkNull(npCustCode);
    }

    /**
     * @param npCustCode the npCustCode to set
     */
    public void setNpCustCode(String npCustCode) {
        this.npCustCode = npCustCode;
    }

    /**
     * @return the np2CustCode
     */
    public String getNp2CustCode() {
        return Validation.checkNull(np2CustCode);
    }

    /**
     * @param np2CustCode the np2CustCode to set
     */
    public void setNp2CustCode(String np2CustCode) {
        this.np2CustCode = np2CustCode;
    }

    /**
     * @return the quantity
     */
    /**
     * @return the Sp_name
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        /*   HttpSession session = request.getSession();
         ServletContext contexts=getServlet().getServletContext();
         System.out.println("[ECOM-WAR] SIEditForm Context Path in Validation method "+contexts);
         String  strCity="",strCountryCode="";
         Hashtable countryHashObj=(Hashtable)contexts.getAttribute("SESSION_COUNTRYOBJECT");
         Hashtable cityHashObj=(Hashtable)contexts.getAttribute("SESSION_CITYLIST");
         List comListObj=(List)contexts.getAttribute("SESSION_COMMODITYLIST");
         Hashtable containerObj=(Hashtable)session.getAttribute("SI_CONTAINER_DETAILS");
         // Shipper Party
         if(getSpname().length()==0){
         errors.add("SI_SPNAME_ERROR", new ActionMessage("error.SI.SPNAME.Msg"));
         }
         if(getSpaddr().length()==0){
         errors.add("SI_SPADDR_ERROR",new ActionMessage("error.SI.SPADDR.Msg"));
         }
if(getSpphone().length()==0){
         errors.add("SI_SP_PHONE_ERROR",new ActionMessage("error.SI.SPPHONE.Msg"));
         }else if(getSpphone().length()>2 && (!Pattern.matches("^[+]{0,1}[0-9]{1,3}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{5,10}$",getSpphone()))){
         errors.add("SI_SP_PHONE_ERROR",new ActionMessage("error.SI.SPPHONEINVALID.Msg"));
         }else if(getSpphone().length()<3 && (!Pattern.matches("^[-]{2,2}$",getSpphone()))){
         errors.add("SI_SP_PHONE_ERROR",new ActionMessage("error.SI.SPPHONEINVALID.Msg"));
         }
         if(getSpfax().length()==0){
         errors.add("SI_SP_FAX_ERROR",new ActionMessage("error.SI.SPFAX.Msg"));
         }else if(getSpfax().length()>2 && (!Pattern.matches("^[+]{0,1}[0-9]{1,3}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{5,10}$",getSpfax()))){
         errors.add("SI_SP_FAX_ERROR",new ActionMessage("error.SI.SPFAXINVALID.Msg"));
         }else if(getSpfax().length()<3 && (!Pattern.matches("^[-]{2,2}$",getSpfax()))){
         errors.add("SI_SP_FAX_ERROR",new ActionMessage("error.SI.SPFAXINVALID.Msg"));
         }
         strCity=Validation.checkNull((String)cityHashObj.get(getSpcity()));
         strCountryCode=Validation.checkNull((String)countryHashObj.get(getSpcountry()));
         if(getSpcountry().length()>0 ){
         errors.add("SI_SPCOUNTRY_ERROR",new ActionMessage("error.SI.SPCOUNTRY.Msg"));
         }else if(getSpcountry().length()>0 && strCountryCode.length()==0){
         errors.add("SI_SPCOUNTRY_ERROR",new ActionMessage("error.SI.SPCOUNTRYINVALID.Msg"));
         }
         if(getSpcity().length()==0){
         errors.add("SI_SPCITY_ERROR",new ActionMessage("error.SI.SPCITY.Msg"));
         }else if(getSpcity().length()>0 && strCity.length()==0){
         errors.add("SI_SPCITY_ERROR",new ActionMessage("error.SI.SPCITYINVALID.Msg"));
         }
         if(getSpemail().length()==0){
         errors.add("SI_SPEMAIL_ERROR",new ActionMessage("error.SI.SPEMAIL.Msg"));
         }else if(!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", getSpemail())){
         errors.add("SI_SPEMAIL_ERROR",new ActionMessage("error.SI.SPEMAILINVALID.Msg"));
         }

         //Consignee
         if(getConsname().length()==0){
         errors.add("SI_CONSNAME_ERROR", new ActionMessage("error.SI.CONSNAME.Msg"));
         }
         if(getConsaddr().length()==0){
         errors.add("SI_CONSADDR_ERROR",new ActionMessage("error.SI.CONSADDR.Msg"));
         }

         if(getConsphone().length()==0){
         errors.add("SI_CONS_PHONE_ERROR",new ActionMessage("error.SI.CONSPHONE.Msg"));
         }else if(getConsphone().length()>2 && (!Pattern.matches("^[+]{0,1}[0-9]{1,3}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{5,10}$",getConsphone()))){
         errors.add("SI_CONS_PHONE_ERROR",new ActionMessage("error.SI.CONSPHONEINVALID.Msg"));
         }else if(getConsphone().length()<3 && (!Pattern.matches("^[-]{2,2}$",getConsphone()))){
         errors.add("SI_CONS_PHONE_ERROR",new ActionMessage("error.SI.CONSPHONEINVALID.Msg"));
         }
         if(getConsfax().length()==0){
         errors.add("SI_CONS_FAX_ERROR",new ActionMessage("error.SI.CONSFAX.Msg"));
         }else if(getConsfax().length()>2 && (!Pattern.matches("^[+]{0,1}[0-9]{1,3}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{5,10}$",getConsfax()))){
         errors.add("SI_CONS_FAX_ERROR",new ActionMessage("error.SI.CONSFAXINVALID.Msg"));
         }else if(getConsfax().length()<3 && (!Pattern.matches("^[-]{2,2}$",getConsfax()))){
         errors.add("SI_CONS_FAX_ERROR",new ActionMessage("error.SI.CONSFAXINVALID.Msg"));
         }
         strCity=Validation.checkNull((String)cityHashObj.get(getConscity()));
         strCountryCode=Validation.checkNull((String)countryHashObj.get(getConscountry()));
         if(getConscountry().length()>0 ){
         errors.add("SI_CONSCOUNTRY_ERROR",new ActionMessage("error.SI.CONSCOUNTRY.Msg"));
         }else if(getConscountry().length()>0 && strCountryCode.length()==0){
         errors.add("SI_CONSCOUNTRY_ERROR",new ActionMessage("error.SI.CONSCOUNTRYINVALID.Msg"));
         }
         if(getConscity().length()==0){
         errors.add("SI_CONSCITY_ERROR",new ActionMessage("error.SI.CONSCITY.Msg"));
         }else if(getConscity().length()>0 && strCity.length()==0){
         errors.add("SI_CONSCITY_ERROR",new ActionMessage("error.SI.CONSCITYINVALID.Msg"));
         }
         if(getConsemail().length()==0){
         errors.add("SI_CONSEMAIL_ERROR",new ActionMessage("error.SI.CONSEMAIL.Msg"));
         }else if(!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", getConsemail())){
         errors.add("SI_CONSEMAIL_ERROR",new ActionMessage("error.SI.CONSEMAILINVALID.Msg"));
         }

         //Notify
         if(getNpname().length()==0){
         errors.add("SI_NTNAME_ERROR", new ActionMessage("error.SI.NTNAME.Msg"));
         }
         if(getNpaddr().length()==0){
         errors.add("SI_NTADDR_ERROR",new ActionMessage("error.SI.NTADDR.Msg"));
         }

         if(getNpphone().length()==0){
         errors.add("SI_NT_PHONE_ERROR",new ActionMessage("error.SI.NTPHONE.Msg"));
         }else if(getNpphone().length()>2 && (!Pattern.matches("^[+]{0,1}[0-9]{1,3}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{5,10}$",getNpphone()))){
         errors.add("SI_NT_PHONE_ERROR",new ActionMessage("error.SI.NTPHONEINVALID.Msg"));
         }else if(getNpphone().length()<3 && (!Pattern.matches("^[-]{2,2}$",getNpphone()))){
         errors.add("SI_NT_PHONE_ERROR",new ActionMessage("error.SI.NTPHONEINVALID.Msg"));
         }
         if(getNpfax().length()==0){
         errors.add("SI_NT_FAX_ERROR",new ActionMessage("error.SI.NTFAX.Msg"));
         }else if(getNpfax().length()>2 && (!Pattern.matches("^[+]{0,1}[0-9]{1,3}[-]{0,1}[0-9]{1,5}[-]{0,1}[0-9]{5,10}$",getNpfax()))){
         errors.add("SI_NT_FAX_ERROR",new ActionMessage("error.SI.NTFAXINVALID.Msg"));
         }else if(getNpfax().length()<3 && (!Pattern.matches("^[-]{2,2}$",getNpfax()))){
         errors.add("SI_NT_FAX_ERROR",new ActionMessage("error.SI.NTFAXINVALID.Msg"));
         }
         strCity=Validation.checkNull((String)cityHashObj.get(getNpcity()));
         strCountryCode=Validation.checkNull((String)countryHashObj.get(getConscountry()));
         if(getNpcountry().length()>0 ){
         errors.add("SI_NTCOUNTRY_ERROR",new ActionMessage("error.SI.NTCOUNTRY.Msg"));
         }else if(getNpcountry().length()>0 && strCountryCode.length()==0){
         errors.add("SI_NTCOUNTRY_ERROR",new ActionMessage("error.SI.NTCOUNTRYINVALID.Msg"));
         }
         if(getNpcity().length()==0){
         errors.add("SI_NTCITY_ERROR",new ActionMessage("error.SI.NTCITY.Msg"));
         }else if(getNpcity().length()>0 && strCity.length()==0){
         errors.add("SI_NTCITY_ERROR",new ActionMessage("error.SI.NTCITYINVALID.Msg"));
         }
         if(getNpemail().length()==0){
         errors.add("SI_NTEMAIL_ERROR",new ActionMessage("error.SI.NTEMAIL.Msg"));
         }else if(!Pattern.matches("^[a-zA-Z0-9_+*-]+(?:\\.[a-zA-Z0-9_+*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$", getNpemail())){
         errors.add("SI_NTEMAIL_ERROR",new ActionMessage("error.SI.NTEMAILINVALID.Msg"));
         }

         // Container details   comListObj
         // Container master
         String selectCnt[] = getSelect();
         String carrsealCnt[] = getCarrierseal();
         String shippersealCnt[] = getShipperseal();
         String othersealCnt[] = getOtherseal();
         String commodityCnt[] = getCommodity();
         String cargowtCnt[] = getCargowt();
         String unitCnt[] = getUnit();
         String qtyCnt[] = getItemqty();
         ArrayList cList = (ArrayList) session.getAttribute("containerListForSI");
         String listno ="",cont_num="";
         SISubmitVO submitVO=null;
         String strSeal="",strCarieer="",strComm="",strCargoWt="",strUnit="",strQty="",strCommId="";
         int iHypenIndex=0;
         if(selectCnt!=null && selectCnt.length>0){
         int selectLen=select.length;
         for (int i = 0; i < selectLen; i++) {
         strSeal=Validation.checkNull(shippersealCnt[i]);
         strCarieer=Validation.checkNull(carrsealCnt[i]);
         strComm=Validation.checkNull(commodityCnt[i]);
         strCargoWt=Validation.checkNull(cargowtCnt[i]);
         strUnit=Validation.checkNull(unitCnt[i]);
         strQty=Validation.checkNull(qtyCnt[i]);
         if(strSeal.length()==0){
         errors.add("SI_SHIPPERSEAL_ERROR",new ActionMessage("error.SI.SHIPPERSEAL.Msg"));
         }else if(!Pattern.matches("^[a-zA-Z-0-9]$",strSeal)){
         errors.add("SI_SHIPPERSEAL_ERROR",new ActionMessage("error.SI.SHIPPERSEALINVALID.Msg"));
         }
         if(strCarieer.length()==0){
         errors.add("SI_CARRIERSEAL_ERROR",new ActionMessage("error.SI.CARRIERSEAL.Msg"));
         }else if(!Pattern.matches("^[a-zA-Z-0-9]$",strCarieer)){
         errors.add("SI_CARRIERSEAL_ERROR",new ActionMessage("error.SI.CARRIERSEALINVALID.Msg"));
         }
         if(strComm.length()==0){
         errors.add("SI_COMMODITY_ERROR",new ActionMessage("error.SI.COMMODITY.Msg"));
         }else if(comListObj.contains(strComm)){
         errors.add("SI_COMMODITY_ERROR",new ActionMessage("error.SI.COMMODITYINVALID.Msg"));
         }
         if(strCargoWt.length()==0){
         errors.add("SI_CARGOWT_ERROR",new ActionMessage("error.SI.CARGOWT.Msg"));
         }else if(!Pattern.matches("^[0-9]{1,7}[\\.][0-9]{0,2}$",strCargoWt)){
         errors.add("SI_CARGOWT_ERROR",new ActionMessage("error.SI.CARGOWTINVALID.Msg"));
         }
         if(strUnit.length()==0){
         errors.add("SI_CARGOUNIT_ERROR",new ActionMessage("error.SI.CARGOUNIT.Msg"));
         }else if(!Pattern.matches("^[a-z-A-Z]$",strUnit)){
         errors.add("SI_CARGOUNIT_ERROR",new ActionMessage("error.SI.CARGOUNITINVALID.Msg"));
         }
         if(strQty.length()==0){
         errors.add("SI_CARGOQTY_ERROR",new ActionMessage("error.SI.CARGOQTY.Msg"));
         }else if(!Pattern.matches("^[0-9]{1,7}[\\.][0-9]{0,2}$",strQty)){
         errors.add("SI_CARGOQTY_ERROR",new ActionMessage("error.SI.CARGOQTYINVALID.Msg"));
         }
         /*listno = select[i];
         submitVO = new SISubmitVO();
         int listnum = Integer.parseInt(listno);
         SIContainerVO contVO = (SIContainerVO) cList.get(listnum);
         submitVO.setContainerno(contVO.getContainerno());
         cont_num = Validation.checkNull(submitVO.getContainerno());*
         }
         }else{
         errors.add("SI_SELECT_ERROR",new ActionMessage("error.SI.SELECT.Msg"));
         }
         /* // additional container row  containerObj
         String commodity_dt[]=null;
         String weight_dt[]=null;
         String unit_dt[]=null;
         String qty_dt[]=null;
         Hashtable cntKeyObj=(Hashtable)session.getAttribute("SI_CONTAINER_KEY");
         if(cntKeyObj!=null && cntKeyObj.size()>0){

         }*/
        return errors;
    }

    /**
     * @return the spIsCountry
     */
    public String getSpIsCountry() {
        return Validation.checkNull(spIsCountry);
    }

    /**
     * @param spIsCountry the spIsCountry to set
     */
    public void setSpIsCountry(String spIsCountry) {
        this.spIsCountry = spIsCountry;
    }

    /**
     * @return the spIsCity
     */
    public String getSpIsCity() {
        return Validation.checkNull(spIsCity);
    }

    /**
     * @param spIsCity the spIsCity to set
     */
    public void setSpIsCity(String spIsCity) {
        this.spIsCity = spIsCity;
    }

    /**
     * @return the consIsCountry
     */
    public String getConsIsCountry() {
        return Validation.checkNull(consIsCountry);
    }

    /**
     * @param consIsCountry the consIsCountry to set
     */
    public void setConsIsCountry(String consIsCountry) {
        this.consIsCountry = consIsCountry;
    }

    /**
     * @return the consIsCity
     */
    public String getConsIsCity() {
        return Validation.checkNull(consIsCity);
    }

    /**
     * @param consIsCity the consIsCity to set
     */
    public void setConsIsCity(String consIsCity) {
        this.consIsCity = consIsCity;
    }

    /**
     * @return the npIsCountry
     */
    public String getNpIsCountry() {
        return Validation.checkNull(npIsCountry);
    }

    /**
     * @param npIsCountry the npIsCountry to set
     */
    public void setNpIsCountry(String npIsCountry) {
        this.npIsCountry = npIsCountry;
    }

    /**
     * @return the npIsCity
     */
    public String getNpIsCity() {
        return Validation.checkNull(npIsCity);
    }

    /**
     * @param npIsCity the npIsCity to set
     */
    public void setNpIsCity(String npIsCity) {
        this.npIsCity = npIsCity;
    }

    /**
     * @return the savetype
     */
    public String getSavetype() {
        return Validation.checkNull(savetype);
    }

    /**
     * @param savetype the savetype to set
     */
    public void setSavetype(String savetype) {
        this.savetype = savetype;
    }

    /**
     * @return the sino
     */
    public String getSino() {
        return Validation.checkNull(sino);
    }

    /**
     * @param sino the sino to set
     */
    public void setSino(String sino) {
        this.sino = sino;
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

    /**
     * @return the file_path1
     */
    public FormFile getFile_path1() {
        return file_path1;
    }

    /**
     * @param file_path1 the file_path1 to set
     */
    public void setFile_path1(FormFile file_path1) {
        this.file_path1 = file_path1;
    }

    /**
     * @return the file_path2
     */
    public FormFile getFile_path2() {
        return file_path2;
    }

    /**
     * @param file_path2 the file_path2 to set
     */
    public void setFile_path2(FormFile file_path2) {
        this.file_path2 = file_path2;
    }

    /**
     * @return the file_path3
     */
    public FormFile getFile_path3() {
        return file_path3;
    }

    /**
     * @param file_path3 the file_path3 to set
     */
    public void setFile_path3(FormFile file_path3) {
        this.file_path3 = file_path3;
    }

    /**
     * @return the file_path4
     */
    public FormFile getFile_path4() {
        return file_path4;
    }

    /**
     * @param file_path4 the file_path4 to set
     */
    public void setFile_path4(FormFile file_path4) {
        this.file_path4 = file_path4;
    }

    /**
     * @return the file_path5
     */
    public FormFile getFile_path5() {
        return file_path5;
    }

    /**
     * @param file_path5 the file_path5 to set
     */
    public void setFile_path5(FormFile file_path5) {
        this.file_path5 = file_path5;
    }

    /**
     * @return the filename1
     */
    public String getFilename1() {
        return filename1;
    }

    /**
     * @param filename1 the filename1 to set
     */
    public void setFilename1(String filename1) {
        this.filename1 = filename1;
    }

    /**
     * @return the filename2
     */
    public String getFilename2() {
        return filename2;
    }

    /**
     * @param filename2 the filename2 to set
     */
    public void setFilename2(String filename2) {
        this.filename2 = filename2;
    }

    /**
     * @return the filename3
     */
    public String getFilename3() {
        return filename3;
    }

    /**
     * @param filename3 the filename3 to set
     */
    public void setFilename3(String filename3) {
        this.filename3 = filename3;
    }

    /**
     * @return the filename4
     */
    public String getFilename4() {
        return filename4;
    }

    /**
     * @param filename4 the filename4 to set
     */
    public void setFilename4(String filename4) {
        this.filename4 = filename4;
    }

    /**
     * @return the filename5
     */
    public String getFilename5() {
        return filename5;
    }

    /**
     * @param filename5 the filename5 to set
     */
    public void setFilename5(String filename5) {
        this.filename5 = filename5;
    }

    public String getHid_pla_free_txt() {
        return hid_pla_free_txt;
    }

    public void setHid_pla_free_txt(String hid_pla_free_txt) {
        this.hid_pla_free_txt = hid_pla_free_txt;
    }

    public String getHid_pol_free_txt() {
        return hid_pol_free_txt;
    }

    public void setHid_pol_free_txt(String hid_pol_free_txt) {
        this.hid_pol_free_txt = hid_pol_free_txt;
    }

    public String getHid_pod_free_txt() {
        return hid_pod_free_txt;
    }

    public void setHid_pod_free_txt(String hid_pod_free_txt) {
        this.hid_pod_free_txt = hid_pod_free_txt;
    }

    public String getHid_pld_free_txt() {
        return hid_pld_free_txt;
    }

    public void setHid_pld_free_txt(String hid_pld_free_txt) {
        this.hid_pld_free_txt = hid_pld_free_txt;
    }

    public String getHid_vslvoyage_free_txt() {
        return hid_vslvoyage_free_txt;
    }

    public void setHid_vslvoyage_free_txt(String hid_vslvoyage_free_txt) {
        this.hid_vslvoyage_free_txt = hid_vslvoyage_free_txt;
    }

    public String getNotify2Check() {
        return notify2Check;
    }

    public void setNotify2Check(String notify2Check) {
        this.notify2Check = notify2Check;
    }

    public String getNotify3Check() {
        return notify3Check;
    }

    public void setNotify3Check(String notify3Check) {
        this.notify3Check = notify3Check;
    }

    public String getNp2fax() {
        return np2fax;
    }

    public void setNp2fax(String np2fax) {
        this.np2fax = np2fax;
    }

    public String getNp2email() {
        return np2email;
    }

    public void setNp2email(String np2email) {
        this.np2email = np2email;
    }

    public String getNp3name() {
        return np3name;
    }

    public void setNp3name(String np3name) {
        this.np3name = np3name;
    }

    public String getNp3addr() {
        return np3addr;
    }

    public void setNp3addr(String np3addr) {
        this.np3addr = np3addr;
    }

    public String getNp3addr1() {
        return np3addr1;
    }

    public void setNp3addr1(String np3addr1) {
        this.np3addr1 = np3addr1;
    }

    public String getNp3city() {
        return np3city;
    }

    public void setNp3city(String np3city) {
        this.np3city = np3city;
    }

    public String getNp3CityName() {
        return np3CityName;
    }

    public void setNp3CityName(String np3CityName) {
        this.np3CityName = np3CityName;
    }

    public String getNp3CustCode() {
        return np3CustCode;
    }

    public void setNp3CustCode(String np3CustCode) {
        this.np3CustCode = np3CustCode;
    }

    public String getNp3phone() {
        return np3phone;
    }

    public void setNp3phone(String np3phone) {
        this.np3phone = np3phone;
    }

    public String getNp3country() {
        return np3country;
    }

    public void setNp3country(String np3country) {
        this.np3country = np3country;
    }

    public String getNp3CountryName() {
        return np3CountryName;
    }

    public void setNp3CountryName(String np3CountryName) {
        this.np3CountryName = np3CountryName;
    }

    public String getNp3fax() {
        return np3fax;
    }

    public void setNp3fax(String np3fax) {
        this.np3fax = np3fax;
    }

    public String getNp3email() {
        return np3email;
    }

    public void setNp3email(String np3email) {
        this.np3email = np3email;
    }

    public String getSpstate() {
        return spstate;
    }

    public void setSpstate(String spstate) {
        this.spstate = spstate;
    }

    public String getConsstate() {
        return consstate;
    }

    public void setConsstate(String consstate) {
        this.consstate = consstate;
    }

    public String getNpstate() {
        return npstate;
    }

    public void setNpstate(String npstate) {
        this.npstate = npstate;
    }

    public String getNp2state() {
        return np2state;
    }

    public void setNp2state(String np2state) {
        this.np2state = np2state;
    }

    public String getNp3state() {
        return np3state;
    }

    public void setNp3state(String np3state) {
        this.np3state = np3state;
    }

    public String getFwstate() {
        return fwstate;
    }

    public void setFwstate(String fwstate) {
        this.fwstate = fwstate;
    }

    public String getSpzipcode() {
        return spzipcode;
    }

    public void setSpzipcode(String spzipcode) {
        this.spzipcode = spzipcode;
    }

    public String getConszipcode() {
        return conszipcode;
    }

    public void setConszipcode(String conszipcode) {
        this.conszipcode = conszipcode;
    }

    public String getNpzipcode() {
        return npzipcode;
    }

    public void setNpzipcode(String npzipcode) {
        this.npzipcode = npzipcode;
    }

    public FormFile getFile_path6() {
        return file_path6;
    }

    public void setFile_path6(FormFile file_path6) {
        this.file_path6 = file_path6;
    }

    public FormFile getFile_path7() {
        return file_path7;
    }

    public void setFile_path7(FormFile file_path7) {
        this.file_path7 = file_path7;
    }

    public FormFile getFile_path8() {
        return file_path8;
    }

    public void setFile_path8(FormFile file_path8) {
        this.file_path8 = file_path8;
    }

    public FormFile getFile_path9() {
        return file_path9;
    }

    public void setFile_path9(FormFile file_path9) {
        this.file_path9 = file_path9;
    }

    public FormFile getFile_path10() {
        return file_path10;
    }

    public void setFile_path10(FormFile file_path10) {
        this.file_path10 = file_path10;
    }

    public FormFile getFile_path11() {
        return file_path11;
    }

    public void setFile_path11(FormFile file_path11) {
        this.file_path11 = file_path11;
    }

    public FormFile getFile_path12() {
        return file_path12;
    }

    public void setFile_path12(FormFile file_path12) {
        this.file_path12 = file_path12;
    }

    public FormFile getFile_path13() {
        return file_path13;
    }

    public void setFile_path13(FormFile file_path13) {
        this.file_path13 = file_path13;
    }

    public FormFile getFile_path14() {
        return file_path14;
    }

    public void setFile_path14(FormFile file_path14) {
        this.file_path14 = file_path14;
    }

    public FormFile getFile_path15() {
        return file_path15;
    }

    public void setFile_path15(FormFile file_path15) {
        this.file_path15 = file_path15;
    }

    public String getDeletedfilename() {
        return deletedfilename;
    }

    public void setDeletedfilename(String deletedfilename) {
        this.deletedfilename = deletedfilename;
    }

    public String getSiAESnumber() {
        return siAESnumber;
    }

    public void setSiAESnumber(String siAESnumber) {
        this.siAESnumber = siAESnumber;
    }

    public String getAESDisclaimer() {
        return AESDisclaimer;
    }

    public void setAESDisclaimer(String AESDisclaimer) {
        this.AESDisclaimer = AESDisclaimer;
    }

    public String getReleasetype() {
        return releasetype;
    }

    public void setReleasetype(String releasetype) {
        this.releasetype = releasetype;
    }

    public String getSptaxId() {
        return sptaxId;
    }

    public void setSptaxId(String sptaxId) {
        this.sptaxId = sptaxId;
    }

    public String getConsTaxId() {
        return consTaxId;
    }

    public void setConsTaxId(String consTaxId) {
        this.consTaxId = consTaxId;
    }

    public String getNpTaxId() {
        return npTaxId;
    }

    public void setNpTaxId(String npTaxId) {
        this.npTaxId = npTaxId;
    }

    public String getApname() {
        return apname;
    }

    public void setApname(String apname) {
        this.apname = apname;
    }

    public String getApCustCode() {
        return apCustCode;
    }

    public void setApCustCode(String apCustCode) {
        this.apCustCode = apCustCode;
    }

    public String getApaddr() {
        return apaddr;
    }

    public void setApaddr(String apaddr) {
        this.apaddr = apaddr;
    }

    public String getApaddr1() {
        return apaddr1;
    }

    public void setApaddr1(String apaddr1) {
        this.apaddr1 = apaddr1;
    }

    public String getApcity() {
        return apcity;
    }

    public void setApcity(String apcity) {
        this.apcity = apcity;
    }

    public String getApphone() {
        return apphone;
    }

    public void setApphone(String apphone) {
        this.apphone = apphone;
    }

    public String getApfax() {
        return apfax;
    }

    public void setApfax(String apfax) {
        this.apfax = apfax;
    }

    public String getApcountry() {
        return apcountry;
    }

    public void setApcountry(String apcountry) {
        this.apcountry = apcountry;
    }

    public String getApemail() {
        return apemail;
    }

    public void setApemail(String apemail) {
        this.apemail = apemail;
    }

    public String getApCountryName() {
        return apCountryName;
    }

    public void setApCountryName(String apCountryName) {
        this.apCountryName = apCountryName;
    }

    public String getApCityName() {
        return apCityName;
    }

    public void setApCityName(String apCityName) {
        this.apCityName = apCityName;
    }

    public String getApstate() {
        return apstate;
    }

    public void setApstate(String apstate) {
        this.apstate = apstate;
    }

    public String getApzipcode() {
        return apzipcode;
    }

    public void setApzipcode(String apzipcode) {
        this.apzipcode = apzipcode;
    }

    public String getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(String declaredValue) {
        this.declaredValue = declaredValue;
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

    public String getBlstatus() {
        return blstatus;
    }

    public void setBlstatus(String blstatus) {
        this.blstatus = blstatus;
    }

    public String getFreightoption() {
        return this.freightoption;
    }

    public void setFreightoption(String freightoption) {
        this.freightoption = freightoption;
    }

    public String getCargoprotectid() {
        return cargoprotectid;
    }

    public void setCargoprotectid(String cargoprotectid) {
        this.cargoprotectid = cargoprotectid;
    }

    public String getSpeori() {
        return speori;
    }

    public void setSpeori(String speori) {
        this.speori = speori;
    }

    public String getSppersontype() {
        return sppersontype;
    }

    public void setSppersontype(String sppersontype) {
        this.sppersontype = sppersontype;
    }

    public String getConseori() {
        return conseori;
    }

    public void setConseori(String conseori) {
        this.conseori = conseori;
    }

    public String getConspersontype() {
        return conspersontype;
    }

    public void setConspersontype(String conspersontype) {
        this.conspersontype = conspersontype;
    }

    public String getNpeori() {
        return npeori;
    }

    public void setNpeori(String npeori) {
        this.npeori = npeori;
    }

    public String getNppersontype() {
        return nppersontype;
    }

    public void setNppersontype(String nppersontype) {
        this.nppersontype = nppersontype;
    }

    public String getNp2eori() {
        return np2eori;
    }

    public void setNp2eori(String np2eori) {
        this.np2eori = np2eori;
    }

    public String getNp2persontype() {
        return np2persontype;
    }

    public void setNp2persontype(String np2persontype) {
        this.np2persontype = np2persontype;
    }

    public String getNp3eori() {
        return np3eori;
    }

    public void setNp3eori(String np3eori) {
        this.np3eori = np3eori;
    }

    public String getNp3persontype() {
        return np3persontype;
    }

    public void setNp3persontype(String np3persontype) {
        this.np3persontype = np3persontype;
    }

    public String getFweori() {
        return fweori;
    }

    public void setFweori(String fweori) {
        this.fweori = fweori;
    }

    public String getFwpersontype() {
        return fwpersontype;
    }

    public void setFwpersontype(String fwpersontype) {
        this.fwpersontype = fwpersontype;
    }

    public String getWheori() {
        return wheori;
    }

    public void setWheori(String wheori) {
        this.wheori = wheori;
    }

    public String getWhpersontype() {
        return whpersontype;
    }

    public void setWhpersontype(String whpersontype) {
        this.whpersontype = whpersontype;
    }

    public String getApeori() {
        return apeori;
    }

    public void setApeori(String apeori) {
        this.apeori = apeori;
    }

    public String getAppersontype() {
        return appersontype;
    }

    public void setAppersontype(String appersontype) {
        this.appersontype = appersontype;
    }

    public String getEuCustZone() {
        return euCustZone;
    }

    public void setEuCustZone(String euCustZone) {
        this.euCustZone = euCustZone;
    }

    public String getBlTypeENS() {
        return blTypeENS;
    }

    public void setBlTypeENS(String blTypeENS) {
        this.blTypeENS = blTypeENS;
    }

    public String getEnsFillingType() {
        return ensFillingType;
    }

    public void setEnsFillingType(String ensFillingType) {
        this.ensFillingType = ensFillingType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeclarantName() {
        return declarantName;
    }

    public void setDeclarantName(String declarantName) {
        this.declarantName = declarantName;
    }

    public String getDeclarantAddress() {
        return declarantAddress;
    }

    public void setDeclarantAddress(String declarantAddress) {
        this.declarantAddress = declarantAddress;
    }

    public String getDeclarantAddress1() {
        return declarantAddress1;
    }

    public void setDeclarantAddress1(String declarantAddress1) {
        this.declarantAddress1 = declarantAddress1;
    }

    public String getDeclarantCity() {
        return declarantCity;
    }

    public void setDeclarantCity(String declarantCity) {
        this.declarantCity = declarantCity;
    }

    public String getDeclarantCountry() {
        return declarantCountry;
    }

    public void setDeclarantCountry(String declarantCountry) {
        this.declarantCountry = declarantCountry;
    }

    public String getDeclarantState() {
        return declarantState;
    }

    public void setDeclarantState(String declarantState) {
        this.declarantState = declarantState;
    }

    public String getDeclarantTelephone() {
        return declarantTelephone;
    }

    public void setDeclarantTelephone(String declarantTelephone) {
        this.declarantTelephone = declarantTelephone;
    }

    public String getDeclarantEORI() {
        return declarantEORI;
    }

    public void setDeclarantEORI(String declarantEORI) {
        this.declarantEORI = declarantEORI;
    }

    public String getDeclarantEmail() {
        return declarantEmail;
    }

    public void setDeclarantEmail(String declarantEmail) {
        this.declarantEmail = declarantEmail;
    }

    public String getDeclarantFillingType() {
        return declarantFillingType;
    }

    public void setDeclarantFillingType(String declarantFillingType) {
        this.declarantFillingType = declarantFillingType;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }

    public String getByaddr() {
        return byaddr;
    }

    public void setByaddr(String byaddr) {
        this.byaddr = byaddr;
    }

    public String getByaddr1() {
        return byaddr1;
    }

    public void setByaddr1(String byaddr1) {
        this.byaddr1 = byaddr1;
    }

    public String getBycity() {
        return bycity;
    }

    public void setBycity(String bycity) {
        this.bycity = bycity;
    }

    public String getByCityName() {
        return byCityName;
    }

    public void setByCityName(String byCityName) {
        this.byCityName = byCityName;
    }

    public String getByCustCode() {
        return byCustCode;
    }

    public void setByCustCode(String byCustCode) {
        this.byCustCode = byCustCode;
    }

    public String getByphone() {
        return byphone;
    }

    public void setByphone(String byphone) {
        this.byphone = byphone;
    }

    public String getBycountry() {
        return bycountry;
    }

    public void setBycountry(String bycountry) {
        this.bycountry = bycountry;
    }

    public String getByCountryName() {
        return byCountryName;
    }

    public void setByCountryName(String byCountryName) {
        this.byCountryName = byCountryName;
    }

    public String getByfax() {
        return byfax;
    }

    public void setByfax(String byfax) {
        this.byfax = byfax;
    }

    public String getByemail() {
        return byemail;
    }

    public void setByemail(String byemail) {
        this.byemail = byemail;
    }

    public String getBystate() {
        return bystate;
    }

    public void setBystate(String bystate) {
        this.bystate = bystate;
    }

    public String getByeori() {
        return byeori;
    }

    public void setByeori(String byeori) {
        this.byeori = byeori;
    }

    public String getBypersontype() {
        return bypersontype;
    }

    public void setBypersontype(String bypersontype) {
        this.bypersontype = bypersontype;
    }

    public String getSlname() {
        return slname;
    }

    public void setSlname(String slname) {
        this.slname = slname;
    }

    public String getSladdr() {
        return sladdr;
    }

    public void setSladdr(String sladdr) {
        this.sladdr = sladdr;
    }

    public String getSladdr1() {
        return sladdr1;
    }

    public void setSladdr1(String sladdr1) {
        this.sladdr1 = sladdr1;
    }

    public String getSlcity() {
        return slcity;
    }

    public void setSlcity(String slcity) {
        this.slcity = slcity;
    }

    public String getSlCityName() {
        return slCityName;
    }

    public void setSlCityName(String slCityName) {
        this.slCityName = slCityName;
    }

    public String getSlCustCode() {
        return slCustCode;
    }

    public void setSlCustCode(String slCustCode) {
        this.slCustCode = slCustCode;
    }

    public String getSlphone() {
        return slphone;
    }

    public void setSlphone(String slphone) {
        this.slphone = slphone;
    }

    public String getSlcountry() {
        return slcountry;
    }

    public void setSlcountry(String slcountry) {
        this.slcountry = slcountry;
    }

    public String getSlCountryName() {
        return slCountryName;
    }

    public void setSlCountryName(String slCountryName) {
        this.slCountryName = slCountryName;
    }

    public String getSlfax() {
        return slfax;
    }

    public void setSlfax(String slfax) {
        this.slfax = slfax;
    }

    public String getSlemail() {
        return slemail;
    }

    public void setSlemail(String slemail) {
        this.slemail = slemail;
    }

    public String getSlstate() {
        return slstate;
    }

    public void setSlstate(String slstate) {
        this.slstate = slstate;
    }

    public String getSleori() {
        return sleori;
    }

    public void setSleori(String sleori) {
        this.sleori = sleori;
    }

    public String getSlpersontype() {
        return slpersontype;
    }

    public void setSlpersontype(String slpersontype) {
        this.slpersontype = slpersontype;
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

    public String getBlType_UAE() {
        return blType_UAE;
    }

    public void setBlType_UAE(String blType_UAE) {
        this.blType_UAE = blType_UAE;
    }

    public String getMpciId_UAE() {
        return mpciId_UAE;
    }

    public void setMpciId_UAE(String mpciId_UAE) {
        this.mpciId_UAE = mpciId_UAE;
    }
}
