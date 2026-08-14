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
import net.ecom.phase2.service.vo.Phase2Model;
import net.ecom.vds.serviceVo.RoutingVo;

/**
 *
 * @author deepa.p
 */
public interface EcomPhase2Service {

    @VDS(name = "getecomcrodetails", tenant = "svm")
    public Map getEcomCROSummaryDetails(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomcrobkgdetails", tenant = "svm")
    public Map getEcomCROBKGDetails(@FP(name = "flsbookingno") String flsbookingno, @FP(name = "equipmenttype") String equipmenttype);

    @VDS(name = "getecomcroreleasedetails", tenant = "svm")
    public Map getEcomCRODetails(@FP(name = "flsbookingno") String flsbookingno, @FP(name = "equipmenttype") String equipmenttype, @FP(name = "conrelno") String conrelno);

    @VDS(name = "getecomarnsummarydetails", tenant = "svm")
    public Map getEcomARNSummaryDetails(@FP(name = "custcode") String custcode, @FP(name = "arrdate", condition = Condition.BETWEEN) String arrdate);

    @VDS(name = "getecomdosummarydetails", tenant = "svm")
    public Map getEcomDOSummaryDetails(@FP(name = "custcode") String custcode, @FP(name = "delorddate", condition = Condition.BETWEEN) String delorddate);

    @VDS(name = "getecomdoprintstatus", tenant = "svm")
    public Map getEcomDOPrintStatus(@FP(name = "delordno") String delordno);

    @VDS(name = "putecomdostatus", tenant = "svm")
    public Map putEcomDOStatus(Phase2Model doUpdateList);

    @VDS(name = "putecomdohousebl", tenant = "svm")
    public Map putEcomDOHouseBL(Phase2Model doUpdateList);

    @VDS(name = "getecomtariffdetails", tenant = "svm")
    public Map getEcomTariffDetails(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomtariffdetails", tenant = "svm")
    public Map getEcomTariffDetCom(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomtariffdetails", tenant = "svm")
    public Map getEcomTariffDetEqp(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomtariffdetails", tenant = "svm")
    public Map getEcomTariffDetComEqp(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet", tenant = "svm")
    public Map getEcomBBLCLTariffDetails(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet", tenant = "svm")
    public Map getEcomBBLCLTariffDetCom(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet", tenant = "svm")
    public Map getEcomBBLCLTariffDetEqp(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet", tenant = "svm")
    public Map getEcomBBLCLTariffDetComEqp(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomsurchargefromflat1new", tenant = "svm")
    public Map getecomsurchargefromflat1new(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "eqptype") String equiptype, @FP(name = "validityfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validityto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomsurchargedetails", tenant = "svm")
    public Map getEcomSurchargeDetails(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecombblclsurchargedet", tenant = "svm")
    public Map getEcomBBLCLSurchargeDetails(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomsurchargeeqptype", tenant = "svm")
    public Map getEcomSurchargeEqpType(@FP(name = "equiptype") String equiptype);

    @VDS(name = "getecomcustomerstatementview", tenant = "svm")
    public Map getEcomCustomerStatementView(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomtariffdetails_rr", tenant = "svm")
    public Map getEcomTariffDetails_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomtariffdetails_rr", tenant = "svm")
    public Map getEcomTariffDetCom_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomtariffdetails_rr", tenant = "svm")
    public Map getEcomTariffDetEqp_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomtariffdetails_rr", tenant = "svm")
    public Map getEcomTariffDetComEqp_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet_rr", tenant = "svm")
    public Map getEcomBBLCLTariffDetails_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet_rr", tenant = "svm")
    public Map getEcomBBLCLTariffDetCom_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet_rr", tenant = "svm")
    public Map getEcomBBLCLTariffDetEqp_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecombblcltariffdet_rr", tenant = "svm")
    public Map getEcomBBLCLTariffDetComEqp_RR(@FP(name = "loadport") String loadPort, @FP(name = "dischport") String dischPort, @FP(name = "commodity") String commoditycode, @FP(name = "eqptype") String eqpType, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String fmdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String todate);

    @VDS(name = "getecomcuststmtnew", tenant = "svm")
    public Map getEcomCustStmtNew(SvmJsonList paramSvmJsonList);

    @VDS(name = "getecomroutinginformation", tenant = "svm")
    public RoutingVo getecomroutinginformation(@FP(name = "origin") String origin, @FP(name = "delivery") String delivery, @FP(name = "DefRoute") String DefRoute, @FP(name = "fromdate", condition = Condition.LESSTHANEQUALTO) String paramdate, @FP(name = "todate", condition = Condition.GREATERTHANEQUALTO) String paramdate1);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateTarriffEqpComdty(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateTarriffCust(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateTarriff(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateTarriffEqp(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecomratecontractnew", tenant = "svm")
    public Map getEcomRateTarriffComdty(SvmJsonList paramSvmJsonObject);
    
    @VDS(name = "getecomcontractnew", tenant = "svm")
    public Map getEcomRateContract(SvmJsonList paramSvmJsonObject);
    
    @VDS(name = "getecomcontractquote", tenant = "svm")
    public Map getEcomRateContractQuote(SvmJsonList paramSvmJsonObject);

    @VDS(name = "invoicecustomerstatementview", tenant = "svm")
    public Map getInvoiceCustomerStatementView(@FP(name = "custcode") String custCode, @FP(name = "invdate", condition = Condition.BETWEEN) String invdate);

    @VDS(name = "getecompaymenthistory", tenant = "svm")
    public Map getEcomPaymentHistory(SvmJsonList paramSvmJsonObject);
}