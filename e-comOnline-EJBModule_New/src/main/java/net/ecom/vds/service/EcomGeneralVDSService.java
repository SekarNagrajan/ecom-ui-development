/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.service;

import com.svm.json.SvmJsonList;
import com.svm.lrp.nfr.query.annotation.FP;
import com.svm.lrp.nfr.query.annotation.VDS;
import com.svm.lrp.nfr.query.bean.helper.Condition;
import java.util.Map;
import net.ecom.vds.serviceVo.RoutingVo;

/**
 *
 * @author deepa.p
 */
public interface EcomGeneralVDSService {

    @VDS(name = "getecomagencylocationnew", tenant = "svm")
    public Map getEcomAgencyLocation(@FP(name = "portcode") String portcode);

    @VDS(name = "getecomraterequestagencymail", tenant = "svm")
    public Map getEcomRateRequestAgencyMail(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomratecontractbblcl", tenant = "svm")
    public Map getEcomRateContractBBLCLCust(@FP(name = "custcode") String custCode, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomratecontractbblcl", tenant = "svm")
    public Map getEcomRateContractBBLCL(@FP(name = "custcode") String custCode, @FP(name = "loadPort") String loadPort, @FP(name = "dischport") String dischargePort, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomratecontractbblcl", tenant = "svm")
    public Map getEcomRateContractEqpBBLCL(@FP(name = "custcode") String custCode, @FP(name = "loadPort") String loadPort, @FP(name = "dischport") String dischargePort, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomratecontractbblcl", tenant = "svm")
    public Map getEcomRateContractComdtyBBLCL(@FP(name = "custcode") String custCode, @FP(name = "loadPort") String loadPort, @FP(name = "dischport") String dischargePort, @FP(name = "commodity") String commoditycode, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomratecontractbblcl", tenant = "svm")
    public Map getEcomRateContractEqpComdtyBBLCL(@FP(name = "custcode") String custCode, @FP(name = "loadPort") String loadPort, @FP(name = "dischport") String dischargePort, @FP(name = "eqptype") String eqpType, @FP(name = "commodity") String commoditycode, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomcontractsurcharge", tenant = "svm")
    public Map getEcomContractSurcharge(@FP(name = "rateno") String rateNo, @FP(name = "itemno") String itemNo);

    @VDS(name = "getecomcontractsurchargebblcl", tenant = "svm")
    public Map getEcomContractSurchargeBBLCL(@FP(name = "rateno") String rateNo, @FP(name = "itemno") String itemNo);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForContains(@FP(name = "comdtyname", condition = Condition.CONTAINS) String comdtyName);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForEqual(@FP(name = "comdtyname", condition = Condition.EQUALS) String comdtyName);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForStartWith(@FP(name = "comdtyname", condition = Condition.STARTSWITH) String comdtyName);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForEndWith(@FP(name = "comdtyname", condition = Condition.ENDWITH) String comdtyName);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForContains(@FP(name = "comdtycode", condition = Condition.CONTAINS) String comdtyCode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForEqual(@FP(name = "comdtycode", condition = Condition.EQUALS) String comdtyCode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForStartWith(@FP(name = "comdtycode", condition = Condition.STARTSWITH) String comdtyCode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForEndWith(@FP(name = "comdtycode", condition = Condition.ENDWITH) String comdtyCode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForContains(@FP(name = "comdtyname", condition = Condition.CONTAINS) String comdtyName, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForEqual(@FP(name = "comdtyname", condition = Condition.EQUALS) String comdtyName, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForStartWith(@FP(name = "comdtyname", condition = Condition.STARTSWITH) String comdtyName, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyForEndWith(@FP(name = "comdtyname", condition = Condition.ENDWITH) String comdtyName, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForContains(@FP(name = "comdtycode", condition = Condition.CONTAINS) String comdtyCode, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForEqual(@FP(name = "comdtycode", condition = Condition.EQUALS) String comdtyCode, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForStartWith(@FP(name = "comdtycode", condition = Condition.STARTSWITH) String comdtyCode, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcomdtysearchlist", tenant = "svm")
    public Map getEcomComdtyCodeForEndWith(@FP(name = "comdtycode", condition = Condition.ENDWITH) String comdtyCode, @FP(name = "grpcode") String grpcode);

    @VDS(name = "getecomcusttype", tenant = "svm")
    public Map getCustType();

    @VDS(name = "getecomreferenceforadd", tenant = "svm")
    public Map getRefidForAddDet();

    @VDS(name = "getecomroutinginformation", tenant = "svm")
    public RoutingVo getecomroutinginformation(@FP(name = "origin") String origin, @FP(name = "delivery") String delivery, @FP(name = "DefRoute") String DefRoute, @FP(name = "fromdate", condition = Condition.LESSTHANEQUALTO) String paramdate, @FP(name = "todate", condition = Condition.GREATERTHANEQUALTO) String paramdate1);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateContractEqpComdty(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateContractCust(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateContract(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateContractEqp(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateContractComdty(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomlangselection", tenant = "svm")
    public Map getEcomLangSelection();

    @VDS(name = "getecomversionutc", tenant = "svm")
    public Map getVersionUTC();

    @VDS(name = "getecomnewpluginconfiginfo", tenant = "svm")
    public Map getecompluginconfiginfo(@FP(name = "moduleid") String moduleid, @FP(name = "pluginid") String pluginid);

    @VDS(name = "getecomtimezonedetails", tenant = "svm")
    public Map getTimeZoneValues();

    @VDS(name = "getecomstatsreportdetails", tenant = "svm")
    public Map getEcomStatsReportDetails(SvmJsonList paramSvmJsonObject);
}
