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
import com.svm.lrp.nfr.query.bean.helper.Operator;
import java.util.HashMap;
import java.util.Map;
import net.ecom.si.vo.CompleteModel;
import net.ecom.vds.serviceVo.EcomBKGCommonModel;

/**
 *
 * @author salomi.a
 */
public interface EcomSiVdsService {

    @VDS(name = "getecombookingsisummary", tenant = "svm")
    public CompleteModel getecombookingsisummary(@FP(name = "custid") String strUserId, @FP(name = "polat", condition = Condition.BETWEEN) String strFromToDate, @FP(name = "custtypeid", condition = Condition.IN) String custTypeId, @FP(name = "status", condition = Condition.IN) String status);

//    @VDS(name = "getagreementpartysi", tenant = "svm")
//    public CompleteModel getagreementpartysi(@FP(name = "book_no",condition = Condition.IN) String strBookNum);
//    
//    @VDS(name = "getTotalContainerssi", tenant = "svm")
//    public CompleteModel getTotalContainerssi(@FP(name = "book_no",condition = Condition.IN) String strBookNum);
//    
//    @VDS(name = "getsubmittedcontainerssi", tenant = "svm")
//    public CompleteModel getsubmittedcontainerssi(@FP(name = "book_no",condition = Condition.IN) String strBookNum);
//    
//    @VDS(name = "getbookingreferenceforsi", tenant = "svm")
//    public CompleteModel getbookingreferenceforsi(@FP(name = "book_no",condition = Condition.IN) String strBookNum);
    @VDS(name = "getblstatusforsi", tenant = "svm")
    public CompleteModel getblstatusforsi(@FP(name = "book_no", condition = Condition.IN) String strBookNum);

    @VDS(name = "getShipperOwn", tenant = "svm")
    public CompleteModel getShipperOwn(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getecombookingdetails", tenant = "svm")
    public CompleteModel getecombookingdetails(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getecomservicetype", tenant = "svm")
    public CompleteModel getecomservicetype(@FP(name = "servicecode") String strServiceCode);

//    @VDS(name = "getEcomBookNumsForSIParties", tenant = "svm")
//    public CompleteModel getEcomBookNumsForSIParties(@FP(name = "custid") String strCustCode,@FP(name = "bookDate",condition=Condition.GREATERTHANEQUALTO) String bookDate);
//    
//    @VDS(name = "getEcomCustCodeForSIParties", tenant = "svm")
//    public CompleteModel getEcomCustCodeForSIParties(@FP(name = "book_no",condition = Condition.IN) String strBookNum);
//    
//    @VDS(name = "getecommpdcustcodeforsiparties", tenant = "svm")
//    public CompleteModel getecommpdcustcodeforsiparties(@FP(name = "custcode",condition = Condition.IN) String strCustCodes);
//    
//    @VDS(name = "getecomcustmastforsiparties", tenant = "svm")
//    public CompleteModel getecomcustmastforsiparties(@FP(name = "custcode",condition = Condition.IN) String strCustCodes);
    @VDS(name = "getecomcustfrombooking", tenant = "svm")
    public HashMap getecomcustfrombooking(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getEcomPortDetForSI", tenant = "svm")
    public CompleteModel getEcomPortDetForSI(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getEcomPortDetForSIDisch", tenant = "svm")
    public CompleteModel getEcomPortDetForSIDisch(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getecombookingcargo", tenant = "svm")
    public CompleteModel getecombookingcargo(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getecombookingcommodity", tenant = "svm")
    public CompleteModel getecombookingcommodity(@FP(name = "book_no") String strBookNum, @FP(name = "eqpid") String eqpid);

    @VDS(name = "setecomeditempbookingmaster", tenant = "svm")
    public Map setecomeditempbooking(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbookingheader", tenant = "svm")
    public Map setecomeditempbookingheader(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbookingcustomer", tenant = "svm")
    public Map setecomeditempbookingcustomer(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecombookingcustnesedonAPFW", tenant = "svm")
    public CompleteModel getecombookingcustnesedonAPFW(@FP(name = "book_no") String strBookNum);

    @VDS(name = "setecomeditempbkgcargo", tenant = "svm")
    public Map setecomeditempbkgcargo(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbookingcommodity", tenant = "svm")
    public Map setecomeditempbookingcommodity(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbkgroutinginfo", tenant = "svm")
    public Map setecomeditempbkgroutinginfo(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecomportcheck", tenant = "svm")
    public CompleteModel getecomportcheck(@FP(name = "portname") String portName);

    @VDS(name = "setecomeditempdocstatus", tenant = "svm")
    public Map setecomeditempdocstatus(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "seteditempbookingremarks", tenant = "svm")
    public Map setEdiTempBookingRemarks(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecomedimaster", tenant = "svm")
    public CompleteModel getecomedimaster();

    @VDS(name = "setedireadermaster", tenant = "svm")
    public Map setedireadermaster(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "putecomedidocstatus", tenant = "svm")
    public Map putecomedidocstatus(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecombookingcargoforsi", tenant = "svm")
    public CompleteModel getecombookingcargoforsi(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getecombookingcust", tenant = "svm")
    public CompleteModel getecombookingcust(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getecomsiagencymail", tenant = "svm")
    public Map getEcomSIAgencyMail(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomtareweightforsi", tenant = "svm")
    public Map getEcomTareWeightForSI(@FP(name = "containerno") String containerno);

    @VDS(name = "getecomtarewtfromeqp", tenant = "svm")
    public Map getEcomTarewtFromEqp(@FP(name = "equiptype") String equipmenttype);

    @VDS(name = "getblnofrombookno", tenant = "svm")
    public Map getBLNoFromBookno(@FP(name = "book_no") String book_no);

    @VDS(name = "getecomequiptype", tenant = "svm")
    public Map getecomequiptype(@FP(name = "containerno") String containerno);

    @VDS(name = "getecomeqpmap", tenant = "svm")
    public Map getecomeqpmap();

    @VDS(name = "getecomeqpments", tenant = "svm")
    public Map getecomeqpments();

    @VDS(name = "getecomimdgclass", tenant = "svm")
    public Map getecomimdgclass();

    @VDS(name = "getecomcontainer", tenant = "svm")
    public Map getecomcontainer(@FP(name = "containerno") String containerno);

    @VDS(name = "getecomcontainer", tenant = "svm")
    public Map getecomcontainer(@FP(name = "containerno") String containerno, @FP(name = "grosswt", condition = Condition.ISNOTNULL) int paramInt1, @FP(name = "grosswt", condition = Condition.GREATERTHAN) int paramInt2);

    @VDS(name = "getecomcommodity", tenant = "svm")
    public Map getecomcommodity();

    @VDS(name = "getecomhscode", tenant = "svm")
    public Map getecomhscode();

    @VDS(name = "getecomcommodityrecords", tenant = "svm")
    public Map getecomcommodityrecords();

    @VDS(name = "getecomtareweightforsi", tenant = "svm")
    public Map checkCntrAndEqptype(@FP(name = "containerno") String containerno, @FP(name = "equipmenttype") String equipmenttype);

    @VDS(name = "getecomeqpsizemapping", tenant = "svm")
    public Map getecomeqpsizemapping(@FP(name = "containerno") String containerno, @FP(name = "ratesizetype") String ratesizetype, @FP(name = "ratenor", bracesOn = true, condition = Condition.EQUALS, operator = Operator.OR) String ratenor, @FP(name = "ratenor", bracesOff = true, condition = Condition.ISNULL, operator = Operator.AND) String ratenor1, @FP(name = "mappednor", bracesOn = true, condition = Condition.EQUALS, operator = Operator.OR) String mappednor, @FP(name = "mappednor", bracesOff = true, condition = Condition.ISNULL) String mappednor1);

    @VDS(name = "getecomeqpsizemapping", tenant = "svm")
    public Map getecomeqpsizemapping(@FP(name = "containerno") String containerno, @FP(name = "ratesizetype") String ratesizetype);

    @VDS(name = "getecombookingcharges", tenant = "svm")
    public Map getEcomBookingCharges(@FP(name = "book_no") String book_no);

    @VDS(name = "setecomeditempbookingcharges", tenant = "svm")
    public Map setecomeditempbookingcharges(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecombookingagency", tenant = "svm")
    public Map getEcomBookingAgency(@FP(name = "book_no") String book_no);

    @VDS(name = "getecomprepaidlocation", tenant = "svm")
    public Map getEcomPrepaidLocation(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomcollectlocation", tenant = "svm")
    public Map getEcomCollectLocation(@FP(name = "portcode") String portcode);

    @VDS(name = "getecomthirdpartylocation", tenant = "svm")
    public Map getEcomThirdPartyLocation(@FP(name = "location", condition = Condition.NOTIN) String location);

    @VDS(name = "getecomsicontainervalidation", tenant = "svm")
    public Map getEcomSIContainerValidation(@FP(name = "book_no") String book_no, @FP(name = "eqpid", condition = Condition.IN) String eqpid);

    @VDS(name = "getecompasswordconfig", tenant = "svm")
    public Map getEcomPasswordConfig(@FP(name = "attribute", condition = Condition.IN) String attribute);

    @VDS(name = "getecomvesselmaster", tenant = "svm")
    public Map getEcomVesselMaster(@FP(name = "servicecode") String servicecode);

    @VDS(name = "getecomvoyagedetails", tenant = "svm")
    public Map getEcomVoyageDetails(@FP(name = "servicecode") String servicecode, @FP(name = "vesselcode") String vesselcode);

    @VDS(name = "getecomcontractdetails", tenant = "svm")
    public Map getEcomContractDetails(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomedidocstatus", tenant = "svm")
    public Map getEcomEDIDocstatus(@FP(name = "book_no") String book_no);

    @VDS(name = "seteditempbookingicsnew", tenant = "svm")
    public Map seteditempbookingicsnew(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecomportsdetails", tenant = "svm")
    public Map getEUPort(@FP(name = "portcode", condition = Condition.IN) String portcode);
    
    @VDS(name = "getecomsimultifilingcustsearch", tenant = "svm")
    public HashMap getEcomsiMultiFilingCustSearch(SvmJsonList paramSvmJsonObject);
    
}
