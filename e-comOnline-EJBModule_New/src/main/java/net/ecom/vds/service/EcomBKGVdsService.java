package net.ecom.vds.service;

import com.svm.json.SvmJsonList;
import com.svm.lrp.nfr.query.annotation.FP;
import com.svm.lrp.nfr.query.annotation.VDS;
import com.svm.lrp.nfr.query.bean.helper.Condition;
import java.util.Map;
import net.ecom.si.vo.CompleteModel;
import net.ecom.vds.serviceVo.BookingSummaryVo;
import net.ecom.vds.serviceVo.EcomBKGCommonModel;
import net.ecom.vds.serviceVo.EcomCommonModel;
import net.ecom.vds.serviceVo.EcomBKGPrintCommonModel;
import net.ecom.vds.serviceVo.EcomCommonModelVo;
import net.ecom.vds.serviceVo.RoutingVo;
import net.ecom.vds.serviceVo.ScheduleDataModel;
import net.ecom.vo.CommonBKGJaserModel;

/**
 *
 * @author sasikumar.r
 */
public interface EcomBKGVdsService {

    @VDS(name = "getbkgtransittime", tenant = "svm")
    public abstract Map getbkgtransittimeorg(@FP(name = "loadport") String paramString1, @FP(name = "dischport") String paramString2, @FP(name = "dischterminal") String paramString3, @FP(name = "hmode") String paramString4, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String paramString5, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String paramString6);

    @VDS(name = "getbkgtransittime", tenant = "svm")
    public abstract Map getbkgtransittimedel(@FP(name = "loadport") String paramString1, @FP(name = "dischport") String paramString2, @FP(name = "loadterminal") String paramString3, @FP(name = "hmode") String paramString4, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String paramString5, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String paramString6);

    @VDS(name = "getecombookingsummarydetails", tenant = "svm")
    public BookingSummaryVo getecombookingsummarydetails(@FP(name = "custId") String custId, @FP(name = "bookdate", condition = Condition.BETWEEN) String bookdate, @FP(name = "custtypeid") String custTypeId);

    @VDS(name = "getecomroutinginformation", tenant = "svm")
    public RoutingVo getecomroutinginformation(@FP(name = "origin") String origin, @FP(name = "delivery") String delivery, @FP(name = "fromdate", condition = Condition.LESSTHANEQUALTO) String paramdate, @FP(name = "todate", condition = Condition.GREATERTHANEQUALTO) String paramdate1);

    @VDS(name = "getecomroutinginformation", tenant = "svm")
    public RoutingVo getecomroutinginformation(@FP(name = "origin") String origin, @FP(name = "delivery") String delivery, @FP(name = "fromdate", condition = Condition.LESSTHANEQUALTO) String paramdate, @FP(name = "todate", condition = Condition.GREATERTHANEQUALTO) String paramdate1, @FP(name = "defroute") String defroute);

    @VDS(name = "getecomscheduleforrouting", tenant = "svm")
    public ScheduleDataModel getecomscheduleforrouting(@FP(name = "port") String port, @FP(name = "service") String service, @FP(name = "depdate", condition = Condition.BETWEEN) String depdate, @FP(name = "terminal") String terminal);

    @VDS(name = "getecomscheduleforrouting", tenant = "svm")
    public ScheduleDataModel getecomscheduleforrouting(@FP(name = "port") String port, @FP(name = "service") String service, @FP(name = "depdate", condition = Condition.BETWEEN) String depdate, @FP(name = "terminal") String terminal, @FP(name = "bound") String bound);

    @VDS(name = "getecomscheduleforroutingforlc", tenant = "svm")
    public ScheduleDataModel getecomscheduleforroutingforlct(@FP(name = "port") String port, @FP(name = "service") String service, @FP(name = "depdate", condition = Condition.BETWEEN) String depdate, @FP(name = "terminal") String terminal);

    @VDS(name = "getecomscheduleforroutingforlc", tenant = "svm")
    public ScheduleDataModel getecomscheduleforroutingforlct(@FP(name = "port") String port, @FP(name = "service") String service, @FP(name = "depdate", condition = Condition.BETWEEN) String depdate, @FP(name = "terminal") String terminal, @FP(name = "bound") String bound);

    @VDS(name = "getecombookingprintstatus", tenant = "svm")
    public Map getecombookingprintstatus(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombkgportcombination", tenant = "svm")
    public Map getecombkgportcombination(@FP(name = "pol") String pol, @FP(name = "pod") String pod, @FP(name = "service") String service);

    @VDS(name = "getecombkgagencymail", tenant = "svm")
    public Map getEcomBookingAgencyMail(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomportsdetails", tenant = "svm")
    public Map getecomports(@FP(name = "portcode") String portcode);

    @VDS(name = "getecompodintransit", tenant = "svm")
    public Map getecompodintransit(@FP(name = "dmspod") String dmspod);

    @VDS(name = "getecomtareweight", tenant = "svm")
    public Map getecomtareweight(@FP(name = "equipType") String equipType);

    @VDS(name = "getecomimgclassdetails", tenant = "svm")
    public Map getecomimgclassdetails(@FP(name = "unno") String unno);

    @VDS(name = "getecomtransportservice", tenant = "svm")
    public Map getecomtransportservice();

    @VDS(name = "getecomunodetails", tenant = "svm")
    public Map getecomunodetails(@FP(name = "eqptype") String eqptype);

    @VDS(name = "getecomagencydetails", tenant = "svm")
    public Map getecomagencydetails(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomagencydetails", tenant = "svm")
    public Map getecomagencydetailswithloaction(@FP(name = "countrycode") String countrycode, @FP(name = "location") String location);

    @VDS(name = "getecomeqptareweight", tenant = "svm")
    public Map getecomeqptareweight(@FP(name = "equiptype") String equiptype);

    @VDS(name = "getecomagencylocationnew", tenant = "svm")
    public Map getEcomAgencyLocation(@FP(name = "portcode") String portcode);

    @VDS(name = "getecombkgconfigdetails", tenant = "svm")
    public Map getEcomBkgConfigDetails(@FP(name = "attribute") String attribute);

    @VDS(name = "getecombkgagencyconfig", tenant = "svm")
    public Map getEcomBkgAgencyConfig(@FP(name = "attribute") String attribute, @FP(name = "agencycode") String agencycode);

    @VDS(name = "getecombkgroutingserivice", tenant = "svm")
    public Map getecombkgroutingserivice(@FP(name = "id") String id);

    @VDS(name = "getecombookingdetails", tenant = "svm")
    public Map getecombookingdetails(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombookingreference", tenant = "svm")
    public Map getecombookingreference(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombookingleg", tenant = "svm")
    public Map getecombookingleg(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombookingcust", tenant = "svm")
    public Map getecombookingcust(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombookingcust", tenant = "svm")
    public Map getecombookingcust(@FP(name = "book_no") String book_no, @FP(name = "status") String status, @FP(name = "inactiveflag") String inactiveflag);

    @VDS(name = "getecombookingcargo", tenant = "svm")
    public Map getecombookingcargo(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombookingrouting", tenant = "svm")
    public Map getecombookingrouting(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombookingcommodity", tenant = "svm")
    public Map getecombookingcommodity(@FP(name = "book_no") String book_no, @FP(name = "eqpId") String eqpId);

    @VDS(name = "getecomgetoffterminal", tenant = "svm")
    public Map getecomgetoffterminal(@FP(name = "groupcallid") String groupcallid);

    @VDS(name = "getecomtransit", tenant = "svm")
    public Map getecomtransit();

    @VDS(name = "getecomemptypickup", tenant = "svm")
    public Map getecomemptypickup();

    @VDS(name = "getecomemptypickpoint", tenant = "svm")
    public Map getecomemptypickpoint(@FP(name = "portcode") String portcode);

    @VDS(name = "getecomemptypickpoint_terminal", tenant = "svm")
    public Map getecomemptypickpoint_terminal(@FP(name = "portcode") String portcode);

    @VDS(name = "getecomemptypickpoint_terminal", tenant = "svm")
    public Map getecomemptypickpoint_view();

    @VDS(name = "getecomcarrier", tenant = "svm")
    public Map getecomcarrier();

    @VDS(name = "getecomcheckdgdetails", tenant = "svm")
    public Map getecomcheckdgdetails(@FP(name = "unno") String unno, @FP(name = "shippingname") String shippingname, @FP(name = "imdgclass") String imdgclass);

    @VDS(name = "getecomcustomermaster", tenant = "svm")
    public Map getecomcustomermaster(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomportsdetails", tenant = "svm")
    public Map getAgencyLocation();

    @VDS(name = "getecomcitydetails", tenant = "svm")
    public Map getecomcitydetails();

    @VDS(name = "getecomcitydetails", tenant = "svm")
    public Map getecomcityincountry(@FP(name = "countryname") String countryname);

    @VDS(name = "getecomtransportterms", tenant = "svm")
    public Map getecomtransportterms(@FP(name = "trptcode") String trptcode);

    @VDS(name = "getecomvesseldetails", tenant = "svm")
    public Map getecomvesseldetails(@FP(name = "vesselcode") String vesselcode);

    @VDS(name = "getecomvesseldetails", tenant = "svm")
    public Map getecomvesseldetails();

    @VDS(name = "getecomcountrydetails", tenant = "svm")
    public Map getecomcountrydetails();

    @VDS(name = "getecomcountrydetails", tenant = "svm")
    public Map getecomcountryincode(@FP(name = "countrycode") String countrycode);

    @VDS(name = "getecommailauthservice", tenant = "svm")
    public Map getecommailauthservice();

    @VDS(name = "getecommequipmentsdetails", tenant = "svm")
    public Map getecommequipmentsdetails(@FP(name = "breakbulklcl") String breakbulklcl);

    @VDS(name = "getecommequipmentsdetails", tenant = "svm")
    public Map getecommequipmentswithtype(@FP(name = "type") String type);

    @VDS(name = "getecommcustomercontact", tenant = "svm")
    public Map getecommcustomercontact(@FP(name = "webid") String webid);

    @VDS(name = "getecomregagencylocation", tenant = "svm")
    public Map getecomagencydetailswithcountrycode(@FP(name = "countrycode") String countrycode);

    @VDS(name = "getecomallowedusercount", tenant = "svm")
    public Map getecomallowedusercount(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomlocationwithagency", tenant = "svm")
    public Map getecomlocationwithagency(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomstatedetails", tenant = "svm")
    public Map getecomstatedetailswithcountrycode(@FP(name = "countrycode") String countrycode);

    @VDS(name = "getecomsicount", tenant = "svm")
    public Map getecomsicount(@FP(name = "custid") String custid, @FP(name = "custtypeid") String custTypeId);

    @VDS(name = "getecomblcount", tenant = "svm")
    public Map getecomblcount(@FP(name = "custid") String custid, @FP(name = "custtypeid") String custtypeid);

    @VDS(name = "getecomshipmentterm", tenant = "svm")
    public Map getecomshipmentterm();

    @VDS(name = "getecombyport", tenant = "svm")
    public Map getEcomByPort(@FP(name = "port") String port, @FP(name = "arrdate", condition = Condition.BETWEEN) String arrdate);

    @VDS(name = "getecombyvessel", tenant = "svm")
    public Map getecombyvessel(@FP(name = "vessel") String vessel, @FP(name = "arrdate", condition = Condition.BETWEEN) String arrdate);

    @VDS(name = "getecombyportforlct", tenant = "svm")
    public Map getEcomByPortForLCT(@FP(name = "port") String port, @FP(name = "arrdate", condition = Condition.BETWEEN) String arrdate);

    @VDS(name = "getecombyvesselforlct", tenant = "svm")
    public Map getecombyvesselForLCT(@FP(name = "vessel") String vessel, @FP(name = "arrdate", condition = Condition.BETWEEN) String arrdate);

    @VDS(name = "getecomblsummary", tenant = "svm")
    public Map getecomblsummary(@FP(name = "polat", condition = Condition.BETWEEN) String polat, @FP(name = "custid") String custid, @FP(name = "custtypeid", condition = Condition.IN) String custTypeid);

    @VDS(name = "getecomblsummaryfromeditemp", tenant = "svm")
    public Map getecomblsummaryfromeditemp(@FP(name = "tbl_no") String tbl_no);

    @VDS(name = "getecomapdetails", tenant = "svm")
    public Map getecomapdetails(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomcheckstate", tenant = "svm")
    public Map getecomcheckstate(@FP(name = "statecode") String statecode);

    @VDS(name = "getecomcheckcountry", tenant = "svm")
    public Map getecomcheckcountry(@FP(name = "countrycode") String countrycode);

    @VDS(name = "getecomcontactdetails", tenant = "svm")
    public Map getecomcontactdetailsforreg(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomoblsignature", tenant = "svm")
    public Map getecomoblsignature(@FP(name = "locationcode", condition = Condition.IN) String agency);

    @VDS(name = "getecomtareweightforsi", tenant = "svm")
    public Map getecomtarewtforamendment(@FP(name = "containerno") String containerno, @FP(name = "equipmenttype") String equipmenttype);

    //Put
    @VDS(name = "putecomuseractive", tenant = "svm")
    public Map putecomuseractive(EcomCommonModelVo paramEcom);

    @VDS(name = "putecomuserdelete", tenant = "svm")
    public Map putecomuserdelete(EcomCommonModelVo paramEcom);

    //Other team services
    @VDS(name = "getservice", tenant = "svm")
    public Map getservice(@FP(name = "servicecode", condition = Condition.IN) String servicecode);

    //Insert Services
    @VDS(name = "seteditempbookingreference", tenant = "svm")
    public Map seteditempbookingreference(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "seteditempbookingremarks", tenant = "svm")
    public Map setEdiTempBookingRemarks(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setedireadermaster", tenant = "svm")
    public Map setedireadermaster(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbookingcustomer", tenant = "svm")
    public Map setecomeditempbookingcustomer(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbookingcommodity", tenant = "svm")
    public Map setecomeditempbookingcommodity(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbookingmaster", tenant = "svm")
    public Map setecomeditempbooking(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbkgroutinginfo", tenant = "svm")
    public Map setecomeditempbkgroutinginfo(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbkgleginfo", tenant = "svm")
    public Map setecomeditempbkgleginfo(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbkgdgdetails", tenant = "svm")
    public Map setecomeditempbkgdgdetails(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "setecomeditempbkgcargo", tenant = "svm")
    public Map setecomeditempbkgcargo(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "setecomeditempdocstatus", tenant = "svm")
    public Map setecomeditempdocstatus(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "setecomnewcontactdetails", tenant = "svm")
    public Map setecomnewcontactdetails(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "putecomedidocstatus", tenant = "svm")
    public Map putecomedidocstatus(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "getecomediid", tenant = "svm")
    public Map getecomediid(@FP(name = "ediname") String ediname);

    @VDS(name = "getecomcheckcontainer", tenant = "svm")
    public Map getecomcheckcontainer(@FP(name = "containerno") String containerno);

    @VDS(name = "getecomcheckcontainer", tenant = "svm")
    public Map getecomcheckcontainerwithtype(@FP(name = "containerno") String containerno, @FP(name = "equipmenttype") String equipmenttype);

    @VDS(name = "getecombookingcargoforsi", tenant = "svm")
    public Map getEcomBookingCargoForView(@FP(name = "book_no") String strBookNum);

    @VDS(name = "getecomcorpagency", tenant = "svm")
    public Map getEcomCorporateAgency(@FP(name = "locAgency") String agencyCode);

    @VDS(name = "getbkgconfdet", tenant = "svm")
    public Map getbkgconfdet(@FP(name = "book_no") String agencyCode);

    @VDS(name = "putbkgconf", tenant = "svm")
    public Map putbkgconf(CommonBKGJaserModel paramCommonBKGJaserModel);

    @VDS(name = "setbkgconf", tenant = "svm")
    public Map setbkgconf(CommonBKGJaserModel paramCommonBKGJaserModel);

    @VDS(name = "getecomcarrierterm", tenant = "svm")
    public Map getEcomCarrierTerm(@FP(name = "trptsvccode") String trptSvcCode);

    @VDS(name = "getecomjasperreportpath", tenant = "svm")
    public Map getecomjasperreportpath(@FP(name = "agencycode") String agencycode, @FP(name = "locationcode") String locationcode);

    @VDS(name = "getecomagencylocjasper", tenant = "svm")
    public Map getEcomAgencyLoc(@FP(name = "agencycode") String agencyCode);

    @VDS(name = "getecomedidocstatus", tenant = "svm")
    public Map getEcomEDIDocstatus(@FP(name = "book_no") String book_no);

    @VDS(name = "getecommailconfig", tenant = "svm")
    public Map getEcomMailConfig(@FP(name = "initiatorid") String initiatorid, @FP(name = "carriercode") String carriercode);

    @VDS(name = "getecombookingpluginconfig", tenant = "svm")
    public Map getEcomBookingPluginConfig(@FP(name = "pluginid", condition = Condition.IN) String pluginid);

    @VDS(name = "getecompluginconfigdetail", tenant = "svm")
    public Map getEcomEBKPluginConfig(@FP(name = "pluginid", condition = Condition.IN) String pluginid);

    @VDS(name = "getcrostatusforbookingcancel", tenant = "svm")
    public Map getCROStatusForBookingCancel(@FP(name = "flsbookingno") String flsbookingno);

    @VDS(name = "getblstatusforbookingcancel", tenant = "svm")
    public Map getBLStatusForBookingCancel(@FP(name = "book_no") String book_no);

    @VDS(name = "getoprstatusforbookingcancel", tenant = "svm")
    public Map getOperationStatusForBookingCancel(@FP(name = "bookno") String bookno);

    @VDS(name = "getusp_emsbookingcontmovement", tenant = "svm")
    public Map getuspemsbookingcontmovement(SvmJsonList paramSvmJsonList);

    @VDS(name = "getlrpbookingremarks", tenant = "svm")
    public Map getLrpBookingRemarks(@FP(name = "book_no") String book_no);

    @VDS(name = "putcancelecomlrpbooking", tenant = "svm")
    public Map putCancelEcomLRPBooking(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "getecomcustmappingagency", tenant = "svm")
    public Map getEcomCustMappingAgency(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomfeederschedules", tenant = "svm")
    public Map getEcomFeederSchedules();

    @VDS(name = "getbookinghaulagedoctype", tenant = "svm")
    public Map getBookingHaulageDocType(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomspecialcustomergroup", tenant = "svm")
    public Map getEcomSpecialCustomerGroup(@FP(name = "custcode") String custcode);

    @VDS(name = "gethaulagecustomerdetails", tenant = "svm")
    public Map getHaulageCustomerDetails(@FP(name = "custid") String custcode);

    @VDS(name = "gethaulagecontactdetails", tenant = "svm")
    public Map getHaulageContactDetails(@FP(name = "custcode") String custcode);

    @VDS(name = "gethaulagetmsclientdetails", tenant = "svm")
    public Map getHaulageTMSClientDetails(@FP(name = "portcode") String portcode);

    @VDS(name = "setecomeditempbookinghaulage", tenant = "svm")
    public Map setecomeditempbkghaulage(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "getlrpbookinghaulagedetails", tenant = "svm")
    public Map getlrpbookinghaulagedetails(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombookingdocagency", tenant = "svm")
    public Map getecombookingdocagency(@FP(name = "mappedagency") String mappedagency);

    @VDS(name = "getecombkgcutoffdate", tenant = "svm")
    public Map getecombkgcutoffdate(@FP(name = "pol") String paramString1, @FP(name = "pod") String paramString2, @FP(name = "status") String paramString3, @FP(name = "agency") String paramString4);

    @VDS(name = "setecomeditempbookingconf", tenant = "svm")
    public Map setecomeditempbkgconf(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "getlrpbookingconf", tenant = "svm")
    public Map getLrpBookingConf(@FP(name = "book_no") String book_no);

    @VDS(name = "getecomongoingdashboard", tenant = "svm")
    public Map getEcomOnGoingDashboard(SvmJsonList paramSvmJsonList);

    @VDS(name = "getecombkgdashcargodetails", tenant = "svm")
    public Map getEcomBkgDashCargoDetails(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombldashcargodetails", tenant = "svm")
    public Map getEcomBlDashCargoDetails(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecomdasunclearedcargodetail", tenant = "svm")
    public Map getecomdasunclearedcargodetail(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomcuststmtnew", tenant = "svm")
    public Map getEcomCustStmtNew(SvmJsonList paramSvmJsonList);

    @VDS(name = "getecombkgfileuploadpath", tenant = "svm")
    public Map getEcomBkgFileUploadPath();

    @VDS(name = "setecombkgprint", tenant = "svm")
    public Map setbkgprint(EcomBKGPrintCommonModel paramCommonBKGModel);

    @VDS(name = "delecombkgprint", tenant = "svm")
    public Map delbkgprint(EcomBKGPrintCommonModel paramCommonBKGModel);

    @VDS(name = "getecombkgjasperid", tenant = "svm")
    public Map getecombkgjasperid(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomregagencylocfromport", tenant = "svm")
    public Map getecomagencydetailswithports(@FP(name = "countrycode") String countrycode);

    @VDS(name = "getuserdetails", tenant = "svm")
    public Map getUserDetails(@FP(name = "loginname") String loginname);

    @VDS(name = "setecominsurancedetails", tenant = "svm")
    public Map setecominsurancedetails(EcomCommonModel paramEcomCommonModel);

    @VDS(name = "getecomblsummarynew", tenant = "svm")
    public Map getecomblsummarynew(SvmJsonList paramSvmJsonList);

    @VDS(name = "getecomnewpluginconfiginfo", tenant = "svm")
    public Map getecompluginconfiginfo(@FP(name = "moduleid") String moduleid, @FP(name = "pluginid") String pluginid);

    @VDS(name = "getecomagencylocwithports", tenant = "svm")
    public Map getecomagencywithports();

    @VDS(name = "getagencylocationdetails", tenant = "svm")
    public Map getecomagencylocationdetails();

    @VDS(name = "getecombkghaualagetracking", tenant = "svm")
    public Map getecombookinghaualagetracking(@FP(name = "book_no") String bookno);

    @VDS(name = "getecommcnsummarynew", tenant = "svm")
    public Map getecommcnsummarynew(SvmJsonList paramSvmJsonList);

    @VDS(name = "getecomcargoprotectdetails", tenant = "svm")
    public Map getEcomCargoProtectDetails(@FP(name = "id") String id);

    @VDS(name = "getecomcargoprotectdetails", tenant = "svm")
    public Map getEcomCargoProtectDetails(@FP(name = "status") String status, @FP(name = "validfrom", condition = Condition.LESSTHANEQUALTO) String shipmentdate, @FP(name = "validto", condition = Condition.GREATERTHANEQUALTO) String shipmentdate1);

    @VDS(name = "getecombkglegtransitdays", tenant = "svm")
    public Map getEcomBKGLegTransitDays(@FP(name = "pol") String pol, @FP(name = "pod") String pod);

    @VDS(name = "geteditempbookcargodetails", tenant = "svm")
    public Map geteditempbookcargodetails(@FP(name = "edireaderid") String edireaderid);

    @VDS(name = "geteditempbookingcustdetails", tenant = "svm")
    public Map geteditempbookingcustdetails(@FP(name = "edireaderid") String edireaderid);

    @VDS(name = "geteditembookdetails", tenant = "svm")
    public Map geteditembookdetails(@FP(name = "edireaderid") String edireaderid);

    @VDS(name = "getecomeditembookrefndetails", tenant = "svm")
    public Map getecomeditembookrefndetails(@FP(name = "edireaderid") String edireaderid);

    @VDS(name = "geteditempbookinglegdetails", tenant = "svm")
    public Map geteditempbookinglegdetails(@FP(name = "edireaderid") String edireaderid);

    @VDS(name = "geteditempbookcommodity", tenant = "svm")
    public Map geteditempbookcommodity(@FP(name = "edireaderid") String edireaderid, @FP(name = "eqpid") String eqpid);

    @VDS(name = "getransportplantrack", tenant = "svm")
    public Map getransportplantrack(@FP(name = "book_no") String book_no);

    @VDS(name = "getecommaskport", tenant = "svm")
    public Map getEcomMaskPort(@FP(name = "port", condition = Condition.IN) String ports);

    @VDS(name = "getecommaskport", tenant = "svm")
    public Map getEcomMaskPort();

    @VDS(name = "getecomsiblmaskport", tenant = "svm")
    public Map getEcomSIBLMaskPort(@FP(name = "book_no") String bookno);

    @VDS(name = "getecomopencommoditylist", tenant = "svm")
    public Map getEcomOpenCommodityList();

    @VDS(name = "getecomratecommodity", tenant = "svm")
    public Map getEcomRateCommodity(@FP(name = "rateno") String rateno, @FP(name = "itemno") String itemno);

    @VDS(name = "getecomblvgmcontainers", tenant = "svm")
    public Map getecomblvgmcontainers(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecomportsdetails", tenant = "svm")
    public Map getEUPort(@FP(name = "portcode", condition = Condition.IN) String portcode);

    @VDS(name = "getcountryagencyid", tenant = "svm")
    public Map getcountryagencyid(@FP(name = "countrycode", condition = Condition.IN) String countrycode);

    @VDS(name = "getecomapcustdetails", tenant = "svm")
    public Map getApCustEnsfiling(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomeuropecountries", tenant = "svm")
    public Map getEcomEuropeCountries();

    @VDS(name = "getecomportsdetails", tenant = "svm")
    public Map getecomporthlgmode(@FP(name = "portcode") String portcode);

    @VDS(name = "seteditempbookingicsnew", tenant = "svm")
    public Map seteditempbookingicsnew(EcomBKGCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecombookingicsdetails", tenant = "svm")
    public Map getecombookingicsdetails(@FP(name = "book_no") String book_no);

    @VDS(name = "getecomfsdocconfig", tenant = "svm")
    public Map getEcomFSDocConfig();

    @VDS(name = "getregisteredfilepath", tenant = "svm")
    public Map getEcomRegFilePath();

    @VDS(name = "setecomfsdoclog", tenant = "svm")
    public Map setEcomFSDocLog(EcomCommonModel model);

}
