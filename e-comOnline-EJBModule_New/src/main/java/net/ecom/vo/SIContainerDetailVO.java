/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author udhaya
 */
public class SIContainerDetailVO implements Serializable {

    private String strCommodity = "";
    private String strWgt = "";
    private String strQty = "";
    private String strUnit = "";
    private String strCid = "";
    //Added by shikin 1/11/2012 for PLARPENH-440
    private String strPkg = "";
    private String strVol = "";
    private String strVut = "";
    private String strMrk = "";
    private String strDes = "";
    private String imocontact = null;
    private String imophone = null;
    private String itemforward = null;
    private String itemtop = null;
    private String itemright = null;
    private String itemaft = null;
    private String hazardous = null;
    private String itemlengthUnit = null;
    private String itemleft = null;
    private String marinePollutant = null;
    private String unNo = null;
    private String cClass = null;
    private String flashPt = null;
    private String flashPtUnit = null;
    private String alt = null;
    private String hsCode = null;
    private String hsCodeDesc = null;
    private String commodityName = null;
    private String appVersion;
    private String isFakCommodity = null;
    private String nettwt = null;
    private String hssuffixcode = null;

    private String isinsuranceavailable = null;
    private String quoteno = null;
    private String insuredemail = null;
    private String byscode = null;
    private String bysname = null;
    private String slscode = null;
    private String slsname = null;
    private String cmdty_hblno = null;

    public String getByscode() {
        return byscode;
    }

    public void setByscode(String byscode) {
        this.byscode = byscode;
    }

    public String getBysname() {
        return bysname;
    }

    public void setBysname(String bysname) {
        this.bysname = bysname;
    }

    public String getSlscode() {
        return slscode;
    }

    public void setSlscode(String slscode) {
        this.slscode = slscode;
    }

    public String getSlsname() {
        return slsname;
    }

    public void setSlsname(String slsname) {
        this.slsname = slsname;
    }

    public String getCmdty_hblno() {
        return cmdty_hblno;
    }

    public void setCmdty_hblno(String cmdty_hblno) {
        this.cmdty_hblno = cmdty_hblno;
    }

    public String getIsFakCommodity() {
        return isFakCommodity;
    }

    public void setIsFakCommodity(String isFakCommodity) {
        this.isFakCommodity = isFakCommodity;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getHsCodeDesc() {
        return hsCodeDesc;
    }

    public void setHsCodeDesc(String hsCodeDesc) {
        this.hsCodeDesc = hsCodeDesc;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getHazardous() {
        return hazardous;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public String getImocontact() {
        return imocontact;
    }

    public void setImocontact(String imocontact) {
        this.imocontact = imocontact;
    }

    public String getImophone() {
        return imophone;
    }

    public void setImophone(String imophone) {
        this.imophone = imophone;
    }

    public String getItemaft() {
        return itemaft;
    }

    public void setItemaft(String itemaft) {
        this.itemaft = itemaft;
    }

    public String getItemforward() {
        return itemforward;
    }

    public void setItemforward(String itemforward) {
        this.itemforward = itemforward;
    }

    public String getItemleft() {
        return itemleft;
    }

    public void setItemleft(String itemleft) {
        this.itemleft = itemleft;
    }

    public String getItemlengthUnit() {
        return itemlengthUnit;
    }

    public void setItemlengthUnit(String itemlengthUnit) {
        this.itemlengthUnit = itemlengthUnit;
    }

    public String getItemright() {
        return itemright;
    }

    public void setItemright(String itemright) {
        this.itemright = itemright;
    }

    public String getItemtop() {
        return itemtop;
    }

    public void setItemtop(String itemtop) {
        this.itemtop = itemtop;
    }

    public String getStrDes() {
        return Validation.checkNull(strDes);
    }

    public void setStrDes(String strDes) {
        this.strDes = strDes;
    }

    public String getStrMrk() {
        return Validation.checkNull(strMrk);
    }

    public void setStrMrk(String strMrk) {
        this.strMrk = strMrk;
    }

    public String getStrPkg() {
        return Validation.checkNull(strPkg);
    }

    public void setStrPkg(String strPkg) {
        this.strPkg = strPkg;
    }

    public String getStrVol() {
        return Validation.checkNull(strVol);

    }

    public void setStrVol(String strVol) {
        this.strVol = strVol;
    }

    public String getStrVut() {
        return Validation.checkNull(strVut);
    }

    public void setStrVut(String strVut) {
        this.strVut = strVut;
    }

    /**
     * @return the strCommodity
     */
    public String getStrCommodity() {
        return Validation.checkNull(strCommodity);
    }

    /**
     * @param strCommodity the strCommodity to set
     */
    public void setStrCommodity(String strCommodity) {
        this.strCommodity = strCommodity;
    }

    /**
     * @return the strWgt
     */
    public String getStrWgt() {
        return Validation.checkNull(strWgt);
    }

    /**
     * @param strWgt the strWgt to set
     */
    public void setStrWgt(String strWgt) {
        this.strWgt = strWgt;
    }

    /**
     * @return the strQty
     */
    public String getStrQty() {
        return Validation.checkNull(strQty);
    }

    /**
     * @param strQty the strQty to set
     */
    public void setStrQty(String strQty) {
        this.strQty = strQty;
    }

    /**
     * @return the strUnit
     */
    public String getStrUnit() {
        return Validation.checkNull(strUnit);
    }

    /**
     * @param strUnit the strUnit to set
     */
    public void setStrUnit(String strUnit) {
        this.strUnit = strUnit;
    }

    /**
     * @return the strCid
     */
    public String getStrCid() {
        return strCid;
    }

    /**
     * @param strCid the strCid to set
     */
    public void setStrCid(String strCid) {
        this.strCid = strCid;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.strCid != null ? this.strCid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SIContainerDetailVO other = (SIContainerDetailVO) obj;
        if ((this.strCid == null) ? (other.strCid != null) : !this.strCid.equals(other.strCid)) {
            return false;
        }
        return true;
    }

    public String getMarinePollutant() {
        return marinePollutant;
    }

    public void setMarinePollutant(String marinePollutant) {
        this.marinePollutant = marinePollutant;
    }

    public String getUnNo() {
        return unNo;
    }

    public void setUnNo(String unNo) {
        this.unNo = unNo;
    }

    public String getcClass() {
        return cClass;
    }

    public void setcClass(String cClass) {
        this.cClass = cClass;
    }

    public String getFlashPt() {
        return flashPt;
    }

    public void setFlashPt(String flashPt) {
        this.flashPt = flashPt;
    }

    public String getFlashPtUnit() {
        return flashPtUnit;
    }

    public void setFlashPtUnit(String flashPtUnit) {
        this.flashPtUnit = flashPtUnit;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getNettwt() {
        return nettwt;
    }

    public void setNettwt(String nettwt) {
        this.nettwt = nettwt;
    }

    public String getHssuffixcode() {
        return hssuffixcode;
    }

    public void setHssuffixcode(String hssuffixcode) {
        this.hssuffixcode = hssuffixcode;
    }

    public String getIsinsuranceavailable() {
        return isinsuranceavailable;
    }

    public void setIsinsuranceavailable(String isinsuranceavailable) {
        this.isinsuranceavailable = isinsuranceavailable;
    }

    public String getQuoteno() {
        return quoteno;
    }

    public void setQuoteno(String quoteno) {
        this.quoteno = quoteno;
    }

    public String getInsuredemail() {
        return insuredemail;
    }

    public void setInsuredemail(String insuredemail) {
        this.insuredemail = insuredemail;
    }

}
